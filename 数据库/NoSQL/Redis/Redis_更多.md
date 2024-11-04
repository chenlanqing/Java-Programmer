 		
# 一、Redis 事务

## 1、事务

可以一次执行多个命令，本质是一组命令的集合，一个事务中的所有命令都会序列化，按顺序地串行执行而不会被其他命令插入，不允许加塞。所以可以任务事务是部分支持事务的。

Redis事务没有像MySQL等关系型数据库事务隔离概念，Redis 是不支持 roll back 的，因而不满足原子性的（而且不满足持久性）

事务执行流程：
- 开始事务（Multi）
- 命令入队；
- 执行事务（Exec）、撤销事务（Discard）

事务实现的主要命令：
- `Multi`：事务开始的命令，执行该命令后，后面执行的redis操作都会按照顺序放入队列中，等待exec命令后才会被执行；
- `Discard`：放弃执行队列中的命令，并且将当前状态从事务状态变为非事务状态；
- `Exec`：执行事务中的commands队列，恢复连接状态。如果`WATCH`在之前被调用，只有监测中的Keys没有被修改，命令才会被执行，否则停止执行；
- `Watch key`：将给出的Keys标记为监测态，作为事务执行的条件；该命令只能在`multi`命令执行之前执行；如果被监视的key被其他客户端修改，exec会放弃执行命令队列中的命令；
- `UNWATCH`：清除事务中Keys的监测态，如果调用了`EXEC or DISCARD`，则没有必要再手动调用UNWATCH；

## 2、如何使用

### 2.1、case1-正常执行

```bash
# 标记一个事务块的开始
127.0.0.1：6379[1]> MULTI
OK
# 设置key
127.0.0.1：6379[1]> set balance 80
QUEUED
127.0.0.1：6379[1]> set debt 20
QUEUED
# 实际执行事务
127.0.0.1：6379[1]> EXEC
1) OK
2) OK
```

### 2.2、放弃事务

```bash
127.0.0.1：6379[1]> MULTI
OK
127.0.0.1：6379[1]> set balance 80
QUEUED
127.0.0.1：6379[1]> set debt 20
QUEUED
# 取消事务，放弃执行事务块内的所有命令
127.0.0.1：6379[1]> DISCARD
```
Redis 中并没有提供回滚机制。虽然 Redis 提供了 DISCARD 命令，但是，这个命令只能用来主动放弃事务执行，把暂存的命令队列清空，起不到回滚的效果

### 2.3、命令存在语法错误

如果中间有一个命令发生错误，都不执行。这种情况下就是在`命令中发生了(error)`。类似于Java的非运行异常，需要在编译阶段捕获的异常信息，如 IOException；比如存在语法错误的命令
```bash
127.0.0.1：6379[1]> MULTI
OK
127.0.0.1：6379[1]> set k1 v1
QUEUED
127.0.0.1：6379[1]> set k2 v2
QUEUED
127.0.0.1：6379[1]> set 3
(error) ERR wrong number of arguments for 'set' command
127.0.0.1：6379[1]> set k4 4
QUEUED
127.0.0.1：6379[1]> EXEC
(error) EXECABORT Transaction discarded because of previous errors.
```
上面这种情况可以保证原子性

### 2.4、命令正确，但是执行出错

类似于Java的运行时异常，在运行阶段抛出来的，如：int i = 10 / 0；比如使用不同类型的操作命令操作不同数据类型会出现运行时错误，这种情况Redis在不执行命令的情况下，是无法发现的；
```bash
127.0.0.1：6379[1]> MULTI
OK
127.0.0.1：6379[1]> INCR k1
QUEUED
127.0.0.1：6379[1]> INCR k2
QUEUED
127.0.0.1：6379[1]> INCRBY k3 5
QUEUED
# 错误命令抛出响应的错误信息
127.0.0.1：6379[1]> exec
1) (error) ERR value is not an integer or out of range
2) (integer) 23
3) (integer) 38
127.0.0.1：6379[1]> 
```
上面这种情况是无法保证原子性的

### 2.5、实例故障

在执行事务的 EXEC 命令时，Redis 实例发生了故障，导致事务执行失败。

在这种情况下，如果 Redis 开启了 AOF 日志，那么，只会有部分的事务操作被记录到 AOF 日志中。需要使用 redis-check-aof 工具检查 AOF 日志文件，这个工具可以把未完成的事务操作从 AOF 文件中去除。这样一来，使用 AOF 恢复实例后，事务操作不会再被执行，从而保证了原子性。

如果我们没有开启 RDB 或 AOF，那么，实例故障重启后，数据都没有了，数据库是一致的；

如果使用了 RDB 快照，因为 RDB 快照不会在事务执行时执行，所以，事务命令操作的结果不会被保存到 RDB 快照中，使用 RDB 快照进行恢复时，数据库里的数据也是一致的

### 2.6、隔离性

- 并发操作在 EXEC 命令前执行，此时，隔离性的保证要使用 WATCH 机制来实现，否则隔离性无法保证；
- 并发操作在 EXEC 命令后执行，此时，隔离性可以保证；

**watch监控：** WATCH 机制的作用是，在事务执行前，监控一个或多个键的值变化情况，当事务调用 EXEC 命令执行时，WATCH 机制会先检查监控的键是否被其它客户端修改了。如果修改了，就放弃事务执行，避免事务的隔离性被破坏。然后，客户端可以再次执行事务，此时，如果没有并发修改事务数据的操作了，事务就能正常执行，隔离性也得到了保证

WATCH 机制的具体实现是由 WATCH 命令实现的

unwatch：取消watch命令对所有key的监视

### 2.7、三阶段：总的来说事务可以概括为3个阶段

- （1）开启事务：以 multi 开始一个事务；
- （2）入队：将多个命令进入到事务的队列，接到这些命令并不会立即执行，而是放到等到执行的事务队列里.
- （3）执行：有exec触发事务执行

### 2.8、事务的三个特性

- 单独的隔离操作：事务中的所有命令都会序列化、按顺序地执行。事务在执行的过程中，不会被其他客户端发送来的命令请求所打断；
- 没有隔离级别的概念：队列中的命令没有提交之前都不会实际的被执行，因为事务提交前任何指令都不会被实际执行，也就不存在"事务内的查询要看到事务里的更新，在事务外查询不能看到"这个让人万分头痛的问题
- 关于原子性：
	- 命令入队时就报错，会放弃事务执行，保证原子性；
	- 命令入队时没报错，实际执行时报错，不保证原子性；
	- EXEC 命令执行时实例故障，如果开启了 AOF 日志，可以保证原子性。

# 二、Redis安全

Redis安全防范

## 1、禁止一些高危命令

高危命令
```bash
KEYS        # 虽然该命令的模糊匹配功能很强大，但只适用于小数据量，当数据量很大时会导致Redis锁住及CPU飙升，建议禁用或重命名 
FLUSHDB     # 删除Redis中当前所在数据库中的所有记录，并且此命令不会执行失败
FLUSHALL    # 删除Redis中所有数据库的记录，并且此命令不会失败
CONFIG      # 客户端可修改Redis配置
EVAL
```
修改 redis.conf 文件，禁用远程修改 DB 文件地址，禁止一些高危的命令，在配置文件redis.conf中找到SECURITY区域，添加如下命令
```
rename-command FLUSHALL ""
rename-command CONFIG ""
rename-command EVAL ""
```

## 2、以低权限运行 Redis 服务

为 Redis 服务创建单独的用户和家目录，并且配置禁止登陆
```
groupadd -r redis && useradd -r -g redis redis
```
限制 Redis 配置文件的目录访问权限

## 3、为 Redis 添加密码验证

修改 redis.conf 文件，添加：`requirepass mypassword`；（注意redis不要用-a参数，明文输入密码，连接后使用auth认证）

## 4、禁止外网访问 Redis

修改 redis.conf 文件，添加或修改，使得 Redis 服务只在当前主机可用
```
bind 127.0.0.1
```
在redis3.2之后，redis增加了protected-mode，在这个模式下，非绑定IP或者没有配置密码访问时都会报错

## 5、修改默认端口

修改配置文件redis.conf文件
```
port 6379
```
默认端口是6379，可以改变成其他端口（不要冲突就好）

## 6、保证 authorized_keys 文件的安全

为了保证安全，您应该阻止其他用户添加新的公钥。

将 `authorized_keys` 的权限设置为对拥有者只读，其他用户没有任何权限：

`chmod 400 ~/.ssh/authorized_keys`

为保证 authorized_keys 的权限不会被改掉，您还需要设置该文件的 immutable 位权限:

`chattr +i ~/.ssh/authorized_keys`

然而，用户还可以重命名 `~/.ssh`，然后新建新的 `~/.ssh` 目录和 authorized_keys 文件。要避免这种情况，需要设置 ~./ssh 的 immutable 权限：

`chattr +i ~/.ssh`

# 三、Redis管道pipeline

## 1、单条命令的执行步骤

- 客户端把命令发送到服务器，然后阻塞客户端，等待着从socket读取服务器的返回结果；
- 服务器处理命令并将结果返回给客户端；

`每个命令的执行时间` =` 客户端发送时间+服务器处理和返回时间` + `一个网络来回的时间`；其中一个网络来回的时间是不固定的，它的决定因素有很多

## 2、Redis管道技术

管道的基本含义是，客户端可以向服务器发送多个请求，而不必等待回复，并最终在一个步骤中读取回复；

redis本身是基于request/response模式，每一个命令都需要等待上一个命令响应后进行处理，中间需要经过RTT（Round Time Trip，往返延时，表示发送端从发送数据开始，到发送端收到来自接收端的确认，所需要的时间。），并且需要频繁调用系统IO

客户端可以一次发送多条命令，不用逐条等待命令的返回值，而是到最后一起读取返回结果，这样只需要一次网络开销，速度就会得到明显的提升；

在Redis中，如果客户端使用管道发送了多条命令，那么服务器就会将多条命令放入一个队列中，这一操作会消耗一定的内存，所以管道中命令的数量并不是越大越好（太大容易撑爆内存），而是应该有一个合理的值；

对于管道的大部分应用场景而言，使用Redis脚本（Redis2.6及以后的版本）会使服务器端有更好的表现。使用脚本最大的好处就是可以以最小的延迟读写数据
- 使用管道技术可以显著提升Redis处理命令的速度，其原理就是将多条命令打包，只需要一次网络开销，在服务器端和客户端各一次read()和write()系统调用，以此来节约时间。
- 管道中的命令数量要适当，并不是越多越好。
- Redis2.6版本以后，脚本在大部分场景中的表现要优于管道

## 3、pipeline 用法

### 3.1、jedis客户端

```java
Pipeline pipelined = jedis.pipelined();
String key = "key";
for (int i = 0; i < 10; i++) {
	pipelined.set(key + ":" + i, String.valueOf((i + 1)));
}
pipelined.close();
```

### 3.2、Lettuce客户端

```java
RedisURI uri = RedisURI.create(RedisConstant.redis_host, RedisConstant.port);
RedisClient client = RedisClient.create(uri);
StatefulRedisConnection<String, String> connect = client.connect();
RedisAsyncCommands<String, String> commands = connect.async();
// disable auto-flushing
commands.setAutoFlushCommands(false);
List<RedisFuture<?>> futures = Lists.newArrayList();
for (int i = 0; i < 10; i++) {
	futures.add(commands.set("key-" + i, "value-" + i));
}
// write all commands to the transport layer
commands.flushCommands();
// synchronization example: Wait until all futures complete
LettuceFutures.awaitAll(5, TimeUnit.SECONDS, futures.toArray(new RedisFuture[futures.size()]));
connect.close();
```

## 3、mget与pipeline

- mget和mset命令也是为了减少网络连接和传输时间所设置的，其本质和pipeline的应用区别不大，但是在特定场景下只能用pipeline实现，例如：
	```
	get a
	set b ‘1’
	incr b
	```
	pipeline适合执行这种连续，且无相关性的命令；

- pipeline应用场景：多个命令需要被及时提交，且这些命令的响应结果没有相互依赖；但是需要注意以下几点：
	- 不要和常规命令client共用一个链接，因为pipeline是独占连接的，如果管道内命令太多，可能会造成请求超时；
	- 可发送命令数量受到客户端缓冲区大小限制，如超过限制，则flush到redis
	- redis server存在query buffer限制，默认是1GB，如果超过这个值，客户端会被强制断掉；
	- redis server存在output buffer限制，受到maxmemory配置限制；
	- 要实现pipeline，同时需要服务器端和客户端的支持
	- redis cluster不建议使用pipeline，容易产生max redirect错误
	- twem proxy可以支持pipeline

	局限性：当某个命令的执行需要依赖前一个命令的返回结果时，无法使用pipeline。

# 四、Redis与Java

## 1、Jedis

Redis的Java实现的客户端，其API提供了比较全面的Redis命令的支持，Jedis简单使用阻塞的I/O和redis交互

## 2、Redission

Redission通过Netty支持非阻塞I/O

## 3、Lettuce

### 3.1、介绍

Lettuce是一个高性能基于Java编写的Redis驱动框架，底层集成了Project Reactor提供天然的反应式编程，通信框架集成了Netty使用了非阻塞IO，5.x版本之后融合了JDK1.8的异步编程特性，在保证高性能的同时提供了十分丰富易用的API，5.1版本的新特性如下：
- 支持Redis的新增命令ZPOPMIN, ZPOPMAX, BZPOPMIN, BZPOPMAX。
- 支持通过Brave模块跟踪Redis命令执行。
- 支持Redis Streams。
- 支持异步的主从连接。
- 支持异步连接池。
- 新增命令最多执行一次模式（禁止自动重连）。
- 全局命令超时设置（对异步和反应式命令也有效）

**Redis的版本至少需要2.6**

- [Lettuce介绍](https://mp.weixin.qq.com/s/1O9qT3rPDHGlNV9D-64bow)
- [Lettuce使用](https://juejin.im/post/5d8eb73ff265da5ba5329c66#heading-21)

### 3.2、

# 五、Redis监控与运维

- [Redis Info Command](https://redis.io/docs/latest/commands/info/)

## 1、Redis监控指标

监控时最好重点关注以下指标：
- 客户端相关：当前连接数、总连接数、输入缓冲大小、OPS
- CPU相关：主进程 CPU 使用率、子进程 CPU 使用率
- 内存相关：当前内存、峰值内存、内存碎片率
- 网络相关：输入、输出网络流量
- 持久化相关：最后一次 RDB 时间、RDB fork 耗时、最后一次 AOF rewrite 时间、AOF rewrite 耗时
- key 相关：过期 key 数量、淘汰 key 数量、key 命中率
- 复制相关：主从节点复制偏移量、主库复制缓冲区

涉及相关配置主要从一下几个方面考虑：
- 性能指标：Performance
- 内存指标: Memory
- 基本活动指标：Basic activity
- 持久性指标: Persistence
- 错误指标：Error

### 1.1、性能指标：Performance

- `latency`：Redis响应一个请求的时间
- `instantaneous_ops_per_sec`：平均每秒处理请求总数
- `hi rate(calculated)`：缓存命中率（计算出来的）

### 1.2、内存指标: Memory

- `used_memory`：已使用内存
- `mem_fragmentation_ratio`：内存碎片率
- `evicted_keys`：由于最大内存限制被移除的key的数量
- `blocked_clients`：由于BLPOP,BRPOP,or BRPOPLPUSH而备阻塞的客户端

### 1.3、基本活动指标：Basic activity

- `connected_clients`：客户端连接数
- `conected_laves`：slave数量
- `master_last_io_seconds_ago`：最近一次主从交互之后的秒数
- `keyspace`：数据库中的key值总数；

### 1.4、持久性指标: Persistence

- `rdb_last_save_time`：最后一次持久化保存磁盘的时间戳；
- `rdb_changes_sice_last_save`：自最后一次持久化以来数据库的更改数；

### 1.5、错误指标：Error

- `rejected_connections`：由于达到maxclient限制而被拒绝的连接数
- `keyspace_misses`：key值查找失败(没有命中)次数
- `master_link_down_since_seconds`：主从断开的持续时间（以秒为单位)

### 1.6、其他指标

- expired_keys， 自 Redis 实例启动以来，已经过期并被删除的键的总数。通过监控 expired_keys，你可以了解系统中过期键的频率。如果该值过高，可能表示有大量的键被设置了较短的过期时间，或 Redis 中存储了过多的短生命周期数据
- latest_fork_usec：表示 Redis 最近一次执行 fork() 操作所花费的时间，单位是微秒（usec）。fork() 是操作系统中用于创建子进程的系统调用，Redis 在执行某些操作时（如生成 RDB 快照或执行 AOF 重写）会使用 fork() 创建一个子进程来处理这些任务；如果该值过大，可能意味着 Redis 占用了过多的内存，导致 fork() 操作时间延长，进而影响 Redis 的响应时间

## 2、监控方式

- redis-benchmark
- redis-stat
- redis-faina
- redislive
- redis-cli
- monitor
- showlog
```
slowlog-log-slower-than 1000 # 设置慢查询的时间下线，单位：微秒
slowlog-max-len 100 # 设置慢查询命令对应的日志显示长度，单位：命令数
```

### 2.1、redis-benchmark

redis性能测试命令：`./redis-benchmark -c 100 -n 5000`，100个连接，5000次请求对应的性能；

### 2.2、redis-cli

info（可以一次性获取所有的信息，也可以按块获取信息），命令使用：
```
./redis-cli info 按块获取信息 | grep 需要过滤的参数
./redis-cli info stats | grep ops
```
交互式：
```
 #./redis-cli 
> info server
```

**内存监控：**
```bash
[root@CombCloud-2020110836 src]# ./redis-cli info | grep used | grep human       
used_memory_human:2.99M  # 内存分配器从操作系统分配的内存总量
used_memory_rss_human:8.04M  #操作系统看到的内存占用，top命令看到的内存
used_memory_peak_human:7.77M # redis内存消耗的峰值
used_memory_lua_human:37.00K   # lua脚本引擎占用的内存大小
```
...

### 2.3、monitor

基本语法：
```bash
redis 127.0.0.1:6379> MONITOR 
```
Redis MONITOR命令用于实时打印出 Redis 服务器接收到的命令 。MONITOR 用来帮助我们知道数库正在做什么。 可以通过 redis-cli 和 telnet 调用MONITOR 。

当 Redis 用做数据库或者分布式缓存时，MONITOR 可以帮助我们发现程序中的 bug 
```bash
$ redis-cli monitor
1339518083.107412 [0 127.0.0.1:60866] "keys" "*"
1339518090.420270 [0 127.0.0.1:60866] "set" "x" "6"
1339518096.506257 [0 127.0.0.1:60866] "get" "x"
1339518099.363765 [0 127.0.0.1:60866] "del" "x"
```

需要注意的是，`MONITOR`命令虽然非常有用，但它会对Redis服务器的性能产生一定影响。

因为MONITOR需要记录并输出所有命令，这会占用CPU资源和网络带宽。在高负载环境中，使用`MONITOR`命令可能会导致吞吐量显著下降。因此，在生产环境中应谨慎使用，并尽量缩短监控时间

通过 redis-faina 工具实现 MONITOR 的分析与定位，redis-faina 通过解析 Redis 的 MONITOR 命令输出，帮助用户对 Redis 实例进行性能诊断。
Facebook Instagram 开源的 redis-faina(Python)，提供了对 Monitor 的一些分析与定位。
redis-faina 是由 Instagram 开发并开源的一个 Redis 查询分析工具

出于安全方面的考虑，所有的管理相关的命令不会记录到MONITOR的输出者。下面几个命令也不会记录：AUTH、EXEC、HELLO、QUIT

## 3、日常运维

主要注意如下几个方面：

（1）**禁止使用 KEYS/FLUSHALL/FLUSHDB 命令**；

（2）**扫描线上实例时，设置休眠时间**

不管是使用 SCAN 扫描线上实例，还是对实例做 bigkey 统计分析，建议在扫描时一定记得设置休眠时间，防止在扫描过程中，实例 OPS 过高对 Redis 产生性能抖动；

（3）**慎用 MONITOR 命令**

但如果 Redis OPS 比较高，在执行 MONITOR 会导致 Redis 输出缓冲区的内存持续增长，这会严重消耗 Redis 的内存资源，甚至会导致实例内存超过 maxmemory，引发数据淘汰；

（4）**从库必须设置为 slave-read-only**

从库必须设置为 slave-read-only 状态，避免从库写入数据，导致主从数据不一致。除此之外，从库如果是非 read-only 状态，如果你使用的是 4.0 以下的 Redis，它存在这样的 Bug：从库写入了有过期时间的数据，不会做定时清理和释放内存。

（5）**合理配置 timeout 和 tcp-keepalive 参数**

如果因为网络原因，导致你的大量客户端连接与 Redis 意外中断，恰好你的 Redis 配置的 maxclients 参数比较小，此时有可能导致客户端无法与服务端建立新的连接（服务端认为超过了 maxclients）

造成这个问题原因在于，客户端与服务端每建立一个连接，Redis 都会给这个客户端分配了一个 client fd。当客户端与服务端网络发生问题时，服务端并不会立即释放这个 client fd；

什么时候释放呢？Redis 内部有一个定时任务，会定时检测所有 client 的空闲时间是否超过配置的 timeout 值。如果 Redis 没有开启 tcp-keepalive 的话，服务端直到配置的 timeout 时间后，才会清理释放这个 client fd。在没有清理之前，如果还有大量新连接进来，就有可能导致 Redis 服务端内部持有的 client fd 超过了 maxclients，这时新连接就会被拒绝，优化建议：
- 不要配置过高的 timeout：让服务端尽快把无效的 client fd 清理掉
- Redis 开启 tcp-keepalive：这样服务端会定时给客户端发送 TCP 心跳包，检测连接连通性，当网络异常时，可以尽快清理僵尸 client fd

（6）**调整 maxmemory 时，注意主从库的调整顺序**

Redis 5.0 以下版本存在这样一个问题：从库内存如果超过了 maxmemory，也会触发数据淘汰。某些场景下，从库是可能优先主库达到 maxmemory 的（例如在从库执行 MONITOR 命令，输出缓冲区占用大量内存），那么此时从库开始淘汰数据，主从库就会产生不一致。要想避免此问题，在调整 maxmemory 时，一定要注意主从库的修改顺序：
- 调大 maxmemory：先修改从库，再修改主库
- 调小 maxmemory：先修改主库，再修改从库

直到 Redis 5.0，Redis 才增加了一个配置 replica-ignore-maxmemory，默认从库超过 maxmemory 不会淘汰数据，才解决了此问题。

## 4、Redis命令统计

`INFO COMMANDSTATS` 是 Redis 提供的一个命令，用于获取关于 Redis 命令的统计信息。这些统计信息包括每个命令的调用次数、总耗时和平均耗时等。通过这些信息，你可以了解哪些命令被频繁调用，以及它们的性能表现。
```sh
INFO COMMANDSTATS
```
`INFO COMMANDSTATS` 命令返回一个字符串，其中包含了各个命令的统计信息。每条统计信息的格式如下：
```
cmdstat_<command_name>: calls=XXX,usec=XXX,usec_per_call=XXX,rejected_calls=XXX,failed_calls=XXX
```
- `<command_name>`: 命令的名称。
- `calls`: 该命令被调用的次数。
- `usec`: 该命令所有调用的总耗时（微秒）。
- `usec_per_call`: 该命令每次调用的平均耗时（微秒）。
- `rejected_calls`：拒绝调用的数量（命令执行之前的错误）
- `failed_calls`：失败调用的数量（命令执行中的错误）

假设你执行了 `INFO COMMANDSTATS` 命令，返回的结果可能如下：
```
# Commandstats
cmdstat_zcount:calls=526,usec=2918,usec_per_call=5.55,rejected_calls=0,failed_calls=0
```
**`cmdstat_zcount`**:
- `calls=526`: `zcount` 命令被调用了 526 次。
- `usec=2918`: `zcount` 命令所有调用的总耗时为 2918 微秒。
- `usec_per_call=5.55`: `zcount` 命令每次调用的平均耗时为 5.55 微秒。
- `rejected_calls=0`: `zcount` 命令拒绝的调用数量为 0。
- `rejected_calls=0`: `zcount` 命令失败调用的数量0。

**使用场景**
- 性能分析：通过 `usec` 和 `usec_per_call` 字段，可以分析哪些命令的执行时间较长，从而优化性能。
- 监控命令频率：通过 `calls` 字段，可以监控哪些命令被频繁调用，帮助发现潜在的热点操作。
- 故障排查：在遇到性能问题时，可以通过 `INFO COMMANDSTATS` 命令快速定位哪些命令的执行时间异常，从而进行进一步的排查。

**重置统计信息**：
- 统计信息会持续累加，直到 Redis 重启或手动重置。可以使用 `CONFIG RESETSTAT` 命令重置所有统计信息。
```sh
CONFIG RESETSTAT
```
- 如果你使用了自定义命令（如 Lua 脚本中的 `EVAL` 和 `EVALSHA`），这些命令也会出现在 `INFO COMMANDSTATS` 的输出中。

### 示例代码

以下是一个简单的示例，展示如何在 Java 中使用 `Jedis` 客户端获取 `INFO COMMANDSTATS` 的信息：

```java
import redis.clients.jedis.Jedis;

public class RedisInfoCommandStatsExample {

    public static void main(String[] args) {
        // 连接到 Redis 服务器
        Jedis jedis = new Jedis("localhost", 6379);

        // 获取命令统计信息
        String commandStats = jedis.info("COMMANDSTATS");

        // 打印统计信息
        System.out.println(commandStats);

        // 关闭连接
        jedis.close();
    }
}
```

通过这种方式，你可以在应用程序中获取和分析 Redis 命令的统计信息，从而更好地进行性能优化和故障排查。

# 六、Redis6.0

Redis6.0主要特性如下：
- 多线程处理网络 IO；
- 客户端缓存；
- 细粒度权限控制（ACL）；
- RESP3 协议的使用；
- 用于复制的 RDB 文件不在有用，将立刻被删除；
- RDB 文件加载速度更快；

## 1、多线程处理

在 Redis 6.0 中，非常受关注的第一个新特性就是多线程。这是因为，Redis 一直被大家熟知的就是它的单线程架构；

随着网络硬件的性能提升，Redis 的性能瓶颈有时会出现在网络 IO 的处理上，也就是说，单个主线程处理网络请求的速度跟不上底层网络硬件的速度，有两种方式来应对：
- 用用户态网络协议栈（例如 DPDK）取代内核网络协议栈，让网络请求的处理不用在内核里执行，直接在用户态完成处理就行；对于高性能的 Redis 来说，避免频繁让内核进行网络请求处理，可以很好地提升请求处理效率。但是，这个方法要求在 Redis 的整体架构中，添加对用户态网络协议栈的支持，需要修改 Redis 源码中和网络相关的部分
- 采用多个 IO 线程来处理网络请求，提高网络请求处理的并行度；Redis6采用的是这种方式；

Redis 的多 IO 线程只是用来处理网络请求的，对于读写命令，Redis 仍然使用单线程来处理；

可以主线程和多 IO 线程的协作分成四个阶段：

**阶段一：服务端和客户端建立 Socket 连接，并分配处理线程**

首先，主线程负责接收建立连接请求。当有客户端请求和实例建立 Socket 连接时，主线程会创建和客户端的连接，并把 Socket 放入全局等待队列中。紧接着，主线程通过轮询方法把 Socket 连接分配给 IO 线程。

**阶段二：IO 线程读取并解析请求**

主线程一旦把 Socket 分配给 IO 线程，就会进入阻塞状态，等待 IO 线程完成客户端请求读取和解析。因为有多个 IO 线程在并行处理，所以，这个过程很快就可以完成

**阶段三：主线程执行请求操作**

等到 IO 线程解析完请求，主线程还是会以单线程的方式执行这些命令操作

**阶段四：IO 线程回写 Socket 和主线程清空全局队列**

当主线程执行完请求操作后，会把需要返回的结果写入缓冲区，然后，主线程会阻塞等待 IO 线程把这些结果回写到 Socket 中，并返回给客户端。

在 Redis 6.0 中，多线程机制默认是关闭的，如果需要使用多线程功能，需要在 redis.conf 中完成两个设置：
- 设置 io-thread-do-reads 配置项为 yes，表示启用多线程。
	```bash
	io-threads-do-reads yes
	```
- 设置线程个数。一般来说，线程个数要小于 Redis 实例所在机器的 CPU 核个数，例如，对于一个 8 核的机器来说，Redis 官方建议配置 6 个 IO 线程。
	```bash
	io-threads  6
	```
如果在实际应用中，发现 Redis 实例的 CPU 开销不大，吞吐量却没有提升，可以考虑使用 Redis 6.0 的多线程机制，加速网络处理，进而提升实例的吞吐量

## 2、实现服务端协助的客户端缓存

Redis 6.0 新增了一个重要的特性，就是实现了服务端协助的客户端缓存功能，也称为跟踪（Tracking）功能。有了这个功能，业务应用中的 Redis 客户端就可以把读取的数据缓存在业务应用本地了，应用就可以直接在本地快速读取数据；

*如果数据被修改了或是失效了，如何通知客户端对缓存的数据做失效处理？* 6.0 实现的 Tracking 功能实现了两种模式，来解决这个问题

**（1）普通模式**：在这个模式下，实例会在服务端记录客户端读取过的 key，并监测 key 是否有修改。一旦 key 的值发生变化，服务端会给客户端发送 invalidate 消息，通知客户端缓存失效了；

需要注意一下，服务端对于记录的 key 只会报告一次 invalidate 消息，也就是说，服务端在给客户端发送过一次 invalidate 消息后，如果 key 再被修改，此时，服务端就不会再次给客户端发送 invalidate 消息；

只有当客户端再次执行读命令时，服务端才会再次监测被读取的 key，并在 key 修改时发送 invalidate 消息

可以通过执行下面的命令，打开或关闭普通模式下的 Tracking 功能：
```bash
CLIENT TRACKING ON|OFF
```
**（2）广播模式**：服务端会给客户端广播所有 key 的失效情况，不过，这样做了之后，如果 key 被频繁修改，服务端会发送大量的失效广播消息，这就会消耗大量的网络带宽资源。

在实际应用时，会让客户端注册希望跟踪的 key 的前缀，当带有注册前缀的 key 被修改时，服务端会把失效消息广播给所有注册的客户端。和普通模式不同，在广播模式下，即使客户端还没有读取过 key，但只要它注册了要跟踪的 key，服务端都会把 key 失效消息通知给这个客户端

普通模式和广播模式，需要客户端使用 RESP 3 协议，RESP 3 协议是 6.0 新启用的通信协议

## 3、细粒度的权限控制

在 Redis 6.0 版本之前，要想实现实例的安全访问，只能通过设置密码来控制，例如，客户端连接实例前需要输入密码。此外，对于一些高风险的命令（例如 KEYS、FLUSHDB、FLUSHALL 等），在 Redis 6.0 之前，也只能通过 rename-command 来重新命名这些命令，避免客户端直接调用

Rdis6.0提供了细粒度的访问权限控制：

首先，6.0 版本支持创建不同用户来使用 Redis，可以使用 ACL SETUSER 命令创建用户
```bash
ACL SETUSER normaluser on > abc
```
6.0 版本还支持以用户为粒度设置命令操作的访问权限

操作  |  作用
------|-------
`+<command>`	 |	将一个命令添加到用户可以调用的命令列表中
`-<command>`	 |	将一个命令从用户可以调用的命令列表中移除
`+@<category>` |	将一类命令添加到用户可以调用的命令列表中
`-@<category>` |	将一类命令从用户可以调用的命令列表中移除
`+@all`		 |	允许调用所有命令
`-@all`		 |	禁止调用所有命令操作


假设要设置用户 normaluser 只能调用 Hash 类型的命令操作，而不能调用 String 类型的命令操作，可以执行如下命令：
```bash
ACL SETUSER normaluser +@hash -@string
```
除了设置某个命令或某类命令的访问控制权限，6.0 版本还支持以 key 为粒度设置访问权限；具体的做法是使用波浪号“~”和 key 的前缀来表示控制访问的 key。例如，执行下面命令，就可以设置用户 normaluser 只能对以“user:”为前缀的 key 进行命令操作
```bash
ACL SETUSER normaluser ~user:* +@all
```

## 4、[启用 RESP3 协议](https://redis.io/docs/latest/develop/reference/protocol-spec/)

Redis 6.0 实现了 RESP 3 通信协议，而之前都是使用的 RESP 2。在 RESP 2 中，客户端和服务器端的通信内容都是以字节数组形式进行编码的，客户端需要根据操作的命令或是数据类型自行对传输的数据进行解码，增加了客户端开发复杂度。

而 RESP 3 直接支持多种数据类型的区分编码，包括空值、浮点数、布尔值、有序的字典集合、无序的集合等。

# 七、Redis模块

## 1、RedisJSON

RedisJSON是一个Redis模块，实现了ECMA-404 JSON数据交换标准作为本地数据类型。它允许存储，更新和获取JSON值从Redis键(文档)

# 八、Redis踩坑

- [使用Redis的坑](http://kaito-kidd.com/2021/03/14/redis-trap/)

![](image/Redis-使用存在的坑.png)

## 1、命令的坑

### 1.1、过期时间意外丢失

SET 除了可以设置 key-value 之外，还可以设置 key 的过期时间，就像下面这样：
```
127.0.0.1:6379> SET testkey val1 EX 60
OK
127.0.0.1:6379> TTL testkey
(integer) 59
```
此时如果你想修改 key 的值，但只是单纯地使用 SET 命令，而没有加上「过期时间」的参数，那这个 key 的过期时间将会被「擦除」
```
127.0.0.1:6379> SET testkey val2
OK
127.0.0.1:6379> TTL testkey  // key永远不过期了！
(integer) -1
```
导致这个问题的原因在于：SET 命令如果不设置过期时间，那么 Redis 会自动「擦除」这个 key 的过期时间；

所以，你在使用 SET 命令时，如果刚开始就设置了过期时间，那么之后修改这个 key，也务必要加上过期时间的参数，避免过期时间丢失问题

### 1.2、DEL会阻塞 Redis

删除一个 key 的耗时（时间复杂度），与这个 key 的类型有关：
- key 是 String 类型，DEL 时间复杂度是 O(1)；
- key 是 List/Hash/Set/ZSet 类型，DEL 时间复杂度是 O(M)，M 为元素数量

也就是说，如果你要删除的是一个非 String 类型的 key，这个 key 的元素越多，那么在执行 DEL 时耗时就越久。因为删除这种 key 时，Redis 需要依次释放每个元素的内存，元素越多，这个过程就会越耗时，而这么长的操作耗时，势必会阻塞整个 Redis 实例，影响 Redis 的性能；

当你在删除 List/Hash/Set/ZSet 类型的 key 时，一定要格外注意，不能无脑执行 DEL，而是应该用以下方式删除：
- 查询元素数量：执行 LLEN/HLEN/SCARD/ZCARD 命令；
- 判断元素数量：如果元素数量较少，可直接执行 DEL 删除，否则分批删除；
- 分批删除：执行 LRANGE/HSCAN/SSCAN/ZSCAN + LPOP/RPOP/HDEL/SREM/ZREM 删除

另外对于 String 类型来说，最好也不要存储过大的数据，否则在删除它时，也会有性能问题，因为 Redis 释放这么大的内存给操作系统，也是需要时间的，所以操作耗时也会变长

### 1.3、RANDOMKEY 阻塞 Redis

如果你想随机查看 Redis 中的一个 key，通常会使用 RANDOMKEY 这个命令，这个命令会从 Redis 中「随机」取出一个 key，基本国策：
- master上 RANDOMKEY 在随机拿出一个 key 后，首先会先检查这个 key 是否已过期；
- 如果该 key 已经过期，那么 Redis 会删除它，这个过程就是懒惰清理，但清理完了还不能结束，Redis 还要找出一个「不过期」的 key，返回给客户端；
- Redis 则会继续随机拿出一个 key，然后再判断是它否过期，直到找出一个未过期的 key 返回给客户端

但这里就有一个问题了：如果此时 Redis 中，有大量 key 已经过期，但还未来得及被清理掉，那这个循环就会持续很久才能结束，而且，这个耗时都花费在了清理过期 key + 寻找不过期 key 上。导致的结果就是，RANDOMKEY 执行耗时变长，影响 Redis 性能；

上述是在master上执行 RANDOMKEY，如果在 slave 上执行 RANDOMEKY，那么问题会更严重，主要原因就在于，slave 自己是不会清理过期 key
还是同样的场景：Redis 中存在大量已过期，但还未被清理的 key，那在 slave 上执行 RANDOMKEY 时，就会发生以下问题：
- slave 随机取出一个 key，判断是否已过期
- key 已过期，但 slave 不会删除它，而是继续随机寻找不过期的 key
- 由于大量 key 都已过期，那 slave 就会寻找不到符合条件的 key，此时就会陷入「死循环」！

这其实是 Redis 的一个 Bug，这个 Bug 一直持续到 5.0 才被修复；修复的解决方案是，在 slave 上执行 RANDOMKEY 时，会先判断整个实例所有 key 是否都设置了过期时间，如果是，为了避免长时间找不到符合条件的 key，slave 最多只会在哈希表中寻找 100 次，无论是否能找到，都会退出循环；

### 1.4、SETBIT导致Redis OOM

在使用 Redis 的 String 类型时，可以把它当做 bitmap 来用，可以把一个 String 类型的 key，拆分成一个个 bit 来操作，就像下面这样：
```
127.0.0.1:6379> SETBIT testkey 10 1
(integer) 1
127.0.0.1:6379> GETBIT testkey 10
(integer) 1
```
操作的每一个 bit 位叫做 offset，但是，这里需要注意的一个点是：如果这个 key 不存在，或者 key 的内存使用很小，此时你要操作的 offset 非常大，那么 Redis 就需要分配「更大的内存空间」，这个操作耗时就会变长，影响性能；所以，当你在使用 SETBIT 时，也一定要注意 offset 的大小，操作过大的 offset 也会引发 Redis 卡顿。

这种类型的 key，也是典型的 bigkey，除了分配内存影响性能之外，在删除它时，耗时同样也会变长

### 1.5、monitor 命令导致OOM

当你在执行 MONITOR 命令时，Redis 会把每一条命令写到客户端的「输出缓冲区」中，然后客户端从这个缓冲区读取服务端返回的结果；但是，如果你的 Redis QPS 很高，这将会导致这个输出缓冲区内存持续增长，占用 Redis 大量的内存资源，如果恰好你的机器的内存资源不足，那 Redis 实例就会面临被 OOM 的风险

## 2、数据持久化的坑


# 九、Redis工具

## 1、连接工具

- [Redis GUI Client](https://github.com/tiny-craft/tiny-rdm)
- [Redis工具-AnotherRedisDesktopManager](https://github.com/qishibo/AnotherRedisDesktopManager)

## 2、数据dump

[使用命令行或node.js将redis数据库转储到redis命令或json中](https://www.npmjs.com/package/redis-dump)

# 十、Redis规范

## 1、键值对使用规范

- key 的命名规范，只有命名规范，才能提供可读性强、可维护性好的 key，方便日常管理；
- value 的设计规范，包括避免 bigkey、选择高效序列化方法和压缩方法、使用整数对象共享池、数据类型选择。

### 1.1、key的命名规范

**把业务名作为前缀，然后用冒号分隔，再加上具体的业务数据名；**

key 本身是字符串，底层的数据结构是 SDS。SDS 结构中会包含字符串长度、分配空间大小等元数据信息。从 Redis 3.2 版本开始，当 key 字符串的长度增加时，SDS 中的元数据也会占用更多内存空间

在设置 key 的名称时，要注意控制 key 的长度。否则，如果 key 很长的话，就会消耗较多内存空间，而且，SDS 元数据也会额外消耗一定的内存空间

**避免使用 bigkey**

bigkey 通常有两种情况。
- 情况一：键值对的值大小本身就很大，例如 value 为 1MB 的 String 类型数据。为了避免 String 类型的 bigkey，在业务层，要尽量把 String 类型的数据大小控制在 10KB 以下。
- 情况二：键值对的值是集合类型，集合元素个数非常多，例如包含 100 万个元素的 Hash 集合类型数据。为了避免集合类型的 bigkey，我建议是，尽量把集合类型的元素个数控制在 1 万以下。

**使用高效序列化方法和压缩方法**

分别是使用高效的序列化方法和压缩方法，这样可以减少 value 的大小

为了避免数据占用过大的内存空间，建议使用压缩工具（例如 snappy 或 gzip），把数据压缩后再写入 Redis，这样就可以节省内存空间了。

**使用整数对象共享池**

整数是常用的数据类型，Redis 内部维护了 0 到 9999 这 1 万个整数对象，并把这些整数作为一个共享池使用。换句话说，如果一个键值对中有 0 到 9999 范围的整数，Redis 就不会为这个键值对专门创建整数对象了，而是会复用共享池中的整数对象

那什么时候不能用整数对象共享池呢？主要有两种情况：
- 第一种情况是，如果 Redis 中设置了 maxmemory，而且启用了 LRU 策略（allkeys-lru 或 volatile-lru 策略），那么，整数对象共享池就无法使用了。这是因为，LRU 策略需要统计每个键值对的使用时间，如果不同的键值对都共享使用一个整数对象，LRU 策略就无法进行统计了。
- 第二种情况是，如果集合类型数据采用 ziplist 编码，而集合元素是整数，这个时候，也不能使用共享池。因为 ziplist 使用了紧凑型内存结构，判断整数对象的共享情况效率低。

## 2、数据保存规范

- 使用 Redis 保存热数据
- 不同的业务数据分实例存储
- 在数据保存时，要设置过期时间
- 控制 Redis 实例的容量，建议设置在 2~6G

## 3、命令使用规范

- 线上禁用部分命令：KEYS、FLUSHALL、FLUSHDB，用 rename-command 命令在配置文件中对这些命令进行重命名，让客户端无法使用这些命令
- 慎用 MONITOR 命令
- 慎用全量操作命令


# 参考资料

- [Redis官方资料-命令](https://redis.io/commands)
- [Redis-开发运维规范](https://help.aliyun.com/zh/redis/use-cases/development-and-o-and-m-standards-for-apsaradb-for-redis)
- 《Redis 深度历险：核心原理与应用实践》


