
# 一、性能调优理论基础

所谓性能，就是使用有限的资源在有限的时间内完成工作

## 1、基本

- 借助监控预防问题、发现问题；
- 借助工具定位问题；
- 定期复盘，防止同类问题再现；
- 定好规范，一定程度上规避问题；

主要点：
- 并行、异步化
- 存储优化

## 2、性能优化衡量指标

- `QPS`：Queries Per Second，每秒查询数，即是每秒能够响应的查询次数，注意这里的查询是指用户发出请求到服务器做出响应成功的次数，简单理解可以认为查询=请求request；qps=每秒钟request数量

- `TPS`：Transactions Per Second 的缩写，每秒处理的事务数；一个事务是指一个客户机向服务器发送请求然后服务器做出反应的过程。客户机在发送请求时开始计时，收到服务器响应后结束计时，以此来计算使用的时间和完成的事务个数。针对单接口而言，TPS可以认为是等价于QPS的，比如访问一个页面/index.html，是一个TPS，而访问/index.html页面可能请求了3次服务器比如css、js、index接口，产生了3个QPS；

- `RT`：Response Time缩写，简单理解为系统从输入到输出的时间间隔，宽泛的来说，他代表从客户端发起请求到服务端接受到请求并响应所有数据的时间差。一般取平均响应时间；

- 并发数：系统能同时处理的请求/事务数量

`QPS = 并发数 / RT` 或者 `并发数 = QPS * RT`

### 2.1、吞吐量和响应速度

在交通非常繁忙的情况下，十字路口是典型的瓶颈点，当红绿灯放行时间非常长时，后面往往会排起长队。从我们开车开始排队，到车经过红绿灯，这个过程所花费的时间，就是`响应时间`。当然，我们可以适当地调低红绿灯的间隔时间，这样对于某些车辆来说，通过时间可能会短一些。但是，如果信号灯频繁切换，反而会导致单位时间内通过的车辆减少，换一个角度，我们也可以认为这个十字路口的车辆吞吐量减少了；

- 响应速度是串行执行的优化，通过优化执行步骤解决问题；
- 吞吐量是并行执行的优化，通过合理利用计算资源达到目标；

### 2.2、响应时间衡量

**平均响应时间**

它的本质是把所有的请求耗时加起来，然后除以请求的次数。举个最简单的例子，有 10 个请求，其中有 2 个 1ms、3 个 5ms、5 个 10ms，那么它的平均耗时就是`(2*1+3*5+5*10)/10=6.7ms`

**百分位数**

圈定一个时间范围，把每次请求的耗时加入一个列表中，然后按照从小到大的顺序将这些时间进行排序。这样，取出特定百分位的耗时，这个数字就是 TP 值。可以看到，TP 值(Top Percentile)和中位数、平均数等是类似的，都是一个统计学里的术语；

一般分为 TP50、TP90、TP95、TP99、TP99.9 等多个段，对高百分位的值要求越高，对系统响应能力的稳定性要求越高；

### 2.3、并发量

并发量是指系统同时能处理的请求数量，这个指标反映了系统的负载能力

### 2.4、秒开率

在移动互联网时代，尤其对于 App 中的页面，秒开是一种极佳的用户体验。如果能在 1 秒内加载完成页面，那用户可以获得流畅的体验，并且不会产生更多的焦虑感

### 2.5、正确性

## 3、理论方法

### 3.1、木桶理论

一只木桶若想要装最多的水，则需要每块木板都一样长而且没有破损才行。如果有一块木板不满足条件，那么这只桶就无法装最多的水。

能够装多少水，取决于最短的那块木板，而不是最长的那一块。

木桶效应在解释系统性能上，也非常适合。组成系统的组件，在速度上是良莠不齐的。系统的整体性能，就取决于系统中最慢的组件。

比如，在数据库应用中，制约性能最严重的是落盘的 I/O 问题，也就是说，硬盘是这个场景下的短板，我们首要的任务就是补齐这个短板

### 3.2、基准测试、预热

基准测试(Benchmark)并不是简单的性能测试，是用来测试某个程序的最佳性能。

应用接口往往在刚启动后都有短暂的超时。在测试之前，我们需要对应用进行预热，消除 JIT 编译器等因素的影响。而在 Java 里就有一个组件，即 JMH，就可以消除这些差异

## 4、优化注意点

- 依据数字而不是猜想：进行性能优化时，我们一般会把分析后的结果排一个优先级（根据难度和影响程度），从大处着手，首先击破影响最大的点，然后将其他影响因素逐一击破

- 个体数据不足信：

    这是因为个体请求的小批量数据，可参考价值并不是非常大。响应时间可能因用户的数据而异，也可能取决于设备和网络条件。

    合理的做法，是从统计数据中找到一些规律，比如上面所提到的平均响应时间、TP 值等，甚至是响应时间分布的直方图，这些都能够帮我们评估性能质量

- 不要过早优化和过度优化：正确的做法是，项目开发和性能优化，应该作为两个独立的步骤进行，要做性能优化，要等到整个项目的架构和功能大体进入稳定状态时再进行

- 保持良好的编码习惯：保持好的编码规范，就可以非常方便地进行代码重构；使用合适的设计模式，合理的划分模块，就可以针对性能问题和结构问题进行聚焦、优化

## 5、性能优化技术手段

### 5.1、复用优化

- 重复代码抽取为公共的方法；
- 缓冲（Buffer），常见于对数据的暂存，然后批量传输或者写入。多使用顺序方式，用来缓解不同设备之间频繁地、缓慢地随机写，缓冲主要针对的是写操作。
- 缓存（Cache），常见于对已读取数据的复用，通过将它们缓存在相对高速的区域，缓存主要针对的是读操作；比如计算结果、数据缓存等；
- 池化操作，比如数据库连接池、线程池；

### 5.2、计算优化

**并行执行：**

并行执行有以下三种模式。
- 第一种模式是多机，采用负载均衡的方式，将流量或者大的计算拆分成多个部分，同时进行处理。比如，Hadoop 通过 MapReduce 的方式，把任务打散，多机同时进行计算。
- 第二种模式是采用多进程。比如 Nginx，采用 NIO 编程模型，Master 统一管理 Worker 进程，然后由 Worker 进程进行真正的请求代理，这也能很好地利用硬件的多个 CPU。
- 第三种模式是使用多线程，这也是 Java 程序员接触最多的。比如 Netty，采用 Reactor 编程模型，同样使用 NIO，但它是基于线程的。Boss 线程用来接收请求，然后调度给相应的 Worker 线程进行真正的业务计算；

**变同步为异步：**

**惰性加载：**使用设计模式来优化业务，提高体验，比如单例模式、代理模式

### 5.3、结果集优化

- 压缩：比如xml与json
- 批量
- 索引
- 复用

### 5.4、资源冲突优化

- 锁粒度
- 事务范围：隔离级别、传输机制、分布式事务
- 锁级别：悲观锁、乐观锁
- 锁类型：公平锁、非公平锁

### 5.5、算法优化

算法属于代码调优，代码调优涉及很多编码技巧，需要使用者对所使用语言的 API 也非常熟悉。有时候，对算法、数据结构的灵活使用，也是代码优化的一个重要内容。比如，常用的降低时间复杂度的方式，就有递归、二分、排序、动态规划等

### 5.6、高效实现

使用一些设计理念良好、性能优越的组件

### 5.7、JVM 优化

对JVM的性能优化，一定程度上能够提升程序的性能，但是如果使用不当的话，可能导致OOM等比较严重的问题；

## 6、资源瓶颈

哪些资源比较容易成为瓶颈呢？

### 6.1、CPU

- 通过 top 命令，来观测 CPU 的性能；
- 通过负载，评估 CPU 任务执行的排队情况；
- 通过 vmstat，看 CPU 的繁忙程度。

### 6.2、内存

逻辑地址可以映射到两个内存段上：物理内存和虚拟内存，那么整个系统可用的内存就是两者之和。比如你的物理内存是 4GB，分配了 8GB 的 SWAP 分区，那么应用可用的总内存就是 12GB
**top 命令**

```
top - 21:52:25 up 4 days, 27 min,  2 users,  load average: 0.08, 0.04, 0.05
Tasks: 103 total,   1 running, 102 sleeping,   0 stopped,   0 zombie
%Cpu(s):  0.1 us,  0.2 sy,  0.0 ni, 99.8 id,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st
KiB Mem : 16266200 total, 10455748 free,  4614764 used,  1195688 buff/cache
KiB Swap:        0 total,        0 free,        0 used. 11375436 avail Mem 

  PID USER      PR  NI    VIRT    RES    SHR S  %CPU %MEM     TIME+ COMMAND                                          
17483 root      20   0  157320   5924   4568 S   0.3  0.0   0:00.05 sshd                                             
17571 root      20   0  162116   2220   1552 R   0.3  0.0   0:00.01 top                                              
    1 root      20   0   43548   3920   2564 S   0.0  0.0   0:35.73 systemd                                          
    2 root      20   0       0      0      0 S   0.0  0.0   0:00.06 kthreadd       
```
从 top 命令可以看到几列数据，解释如下：
- VIRT 这里是指虚拟内存，一般比较大，不用做过多关注；
- RES 我们平常关注的是这一列的数值，它代表了进程实际占用的内存，平常在做监控时，主要监控的也是这个数值；
- SHR 指的是共享内存，比如可以复用的一些 so 文件等

**CPU 缓存**

由于 CPU 和内存之间的速度差异非常大，解决方式就是加入高速缓存，但是针对缓存存在伪共享问题（False sharing）

**页大小太大：Huge page**

**预先加载：**

一些程序的默认行为也会对性能有所影响，比如 JVM 的 `-XX:+AlwaysPreTouch` 参数；默认情况下，JVM 虽然配置了 Xmx、Xms 等参数，指定堆的初始化大小和最大大小，但它的内存在真正用到时，才会分配；但如果加上 AlwaysPreTouch 这个参数，JVM 会在启动的时候，就把所有的内存预先分配。这样，启动时虽然慢了些，但运行时的性能会增加

### 6.3、IO

- iostat
- 零拷贝

### 6.4、工具

- nmon —— 获取系统性能数据

## 7、性能分析

### 7.1、缓冲区优化

- IO缓冲区
- 日志缓冲：Logback 性能也很高，其中一个原因就是异步日志，它在记录日志时，使用了一个缓冲队列，当缓冲的内容达到一定的阈值时，才会把缓冲区的内容写到文件里

缓冲区是可以提高性能的，但它通常会引入一个异步的问题，使得编程模型变复杂；

虽然缓冲区可以帮我们大大地提高应用程序的性能，但同时它也有不少问题，在我们设计时，要注意这些异常情况。

其中，比较严重就是缓冲区内容的丢失。即使你使用 addShutdownHook 做了优雅关闭，有些情形依旧难以防范避免，比如机器突然间断电，应用程序进程突然死亡等。这时，缓冲区内未处理完的信息便会丢失，尤其金融信息，电商订单信息的丢失都是比较严重的。

所以，内容写入缓冲区之前，需要先预写日志，故障后重启时，就会根据这些日志进行数据恢复。在数据库领域，文件缓冲的场景非常多，一般都是采用 WAL 日志（Write-Ahead Logging）解决。对数据完整性比较严格的系统，甚至会通过电池或者 UPS 来保证缓冲区的落地。如果预写日志不成功，一般不会给予客户端ACK，调用方会知晓这个状态。也就是返回给客户端OK的，预写就一定成功。服务只管自己不丢数据即可，至于客户端有没有做异常处理

### 7.2、缓存

一般分为进程内缓存、进程外缓存，在 Java 中，进程内缓存，就是我们常说的堆内缓存。Spring 的默认实现里，就包含 Ehcache、JCache、Caffeine、Guava Cache 等

**Guava 的 LoadingCache**

- [Cache](https://segmentfault.com/a/1190000011105644)

回收策略：
- 基于容量：就是说如果缓存满了，就会按照 LRU 算法来移除其他元素
- 基于时间：
  - 通过 expireAfterWrite 方法设置数据写入以后在某个时间失效；
  - 通过 expireAfterAccess 方法设置最早访问的元素，并优先将其删除；
- 基于 JVM 的垃圾回收：对象的引用有强、软、弱、虚等四个级别，通过 weakKeys 或者 weakValues 等函数即可设置相应的引用级别。当 JVM 垃圾回收的时候，会主动清理这些数据

问题：如果你同时设置了 weakKeys 和 weakValues函数，LC 会有什么反应？

答案：如果同时设置了这两个函数，它代表的意思是，当没有任何强引用，与 key 或者 value 有关系时，就删掉整个缓存项
```java
public static void main(String[] args) {
    LoadingCache<String, Object> cache = CacheBuilder.newBuilder()
            .weakValues()
            .build(new CacheLoader<String, Object>() {
                @Override
                public Object load(String key) throws Exception {
                    return slowMethod(key);
                }
            });
    Object value = new Object();
    cache.put("key1", value);
    value = new Object(); // 原对象不再有强引用
    System.gc(); // 显示调用GC，cache弱引用会被回收
    System.out.println(cache.getIfPresent("key1"));
}
private static String slowMethod(String key) throws Exception {
    Thread.sleep(1000);
    return key + ".result";
}
```
LC 可以通过 recordStats 函数，对缓存加载和命中率等情况进行监控。值得注意的是：LC 是基于数据条数而不是基于缓存物理大小的，所以如果你缓存的对象特别大，就会造成不可预料的内存占用

**缓存算法**

堆内缓存最常用的有 FIFO、LRU、LFU 这三种算法
- FIFO：这是一种先进先出的模式。如果缓存容量满了，将会移除最先加入的元素。这种缓存实现方式简单，但符合先进先出的队列模式场景的功能不多，应用场景较少；
- LRU：是最近最少使用的意思，当缓存容量达到上限，它会优先移除那些最久未被使用的数据，LRU是目前最常用的缓存算法
- LFU：LFU 是最近最不常用的意思。相对于 LRU 的时间维度，LFU 增加了访问次数的维度。如果缓存满的时候，将优先移除访问次数最少的元素；而当有多个访问次数相同的元素时，则优先移除最久未被使用的元素

# 二、优化工具

## 1、Arthas

## 2、应用性能调优-监控工具

### 2.1、Skywalking

[参考](分布式架构/分布式.md#2skyWalking)

### 2.2、SpringBoot Actuator

### 2.3、Javamelody

### 2.4、Tomcat Manager

https://gitee.com/chenlanqing/platform

PSI Probe

## 3、JVM调优

## 4、数据库调优

## 5、架构调优

## 6、操作系统调优

## 7、性能调优技巧

- 池化技术
- 异步化
- 锁优化

# switch 与 if 性能比较

https://mp.weixin.qq.com/s/JkqKg4ZdHBLP_u2COZ4oPw


[性能优化](https://xie.infoq.cn/article/a0d418bf29915ecad5d5eeab0)

