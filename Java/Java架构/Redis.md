<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一.Redis 基本知识:](#%E4%B8%80redis-%E5%9F%BA%E6%9C%AC%E7%9F%A5%E8%AF%86)
- [二.Redis 配置文件: "redis.conf" 常用配置介绍:](#%E4%BA%8Credis-%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6-redisconf-%E5%B8%B8%E7%94%A8%E9%85%8D%E7%BD%AE%E4%BB%8B%E7%BB%8D)
- [三.Redis 持久化:](#%E4%B8%89redis-%E6%8C%81%E4%B9%85%E5%8C%96)
- [四.Redis 事务:](#%E5%9B%9Bredis-%E4%BA%8B%E5%8A%A1)
- [五.主从复制:](#%E4%BA%94%E4%B8%BB%E4%BB%8E%E5%A4%8D%E5%88%B6)
- [六.Redis面试题:](#%E5%85%ADredis%E9%9D%A2%E8%AF%95%E9%A2%98)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 一.Redis 基本知识:
## 1.Redis:
    是一个开源,先进的key-value存储,并用于构建高性能,可扩展的Web应用程序的完美解决方案
	数据结构服务器,支持不同类型的值,高级键值 (key-value) 缓存 (cache) 和存储 (store) 系统
	为了高性能采用内存 (in-memory) 数据集 (dataset)
	1.1.特点:
		(1).Redis 数据库完全在内存中,使;用磁盘仅用于持久性;
		(2).相比许多键值数据存储,Redis 拥有一套较为丰富的数据类型;
		(3).Redis 可以将数据复制到任意数量的从服务器;
	1.2.优点:
		(1).异常快速:Redis 的速度非常快,每秒能执行约11万集合,每秒约81000+条记录
		(2).支持丰富的数据类型:Redis 支持最大多数开发人员已经知道像列表,集合,有序集合,散列数据类型
		(3).操作都是原子性:所有 Redis 操作是原子的,这保证了如果两个客户端同时访问的 Redis 服务器将获得更新后的值;
		(4).多功能实用工具:Redis 是一个多实用的工具,可以在多个用例如缓存,消息,队列使用(Redis 原生支持发布/订阅),
			任何短暂的数据,应用程序,如Web应用程序会话,网页命中计数等
	1.3.Redis 安装配置:

	1.4.启动: redis-server
		运行:redis-cli
			127.0.0.1:6379> 127.0.0.1是本机的IP地址,6379为Redis服务器运行的端口
		redis 127.0.0.1:6379> ping
		PONG > 表示成功地安装Redis在您的机器上
## 2.Redis 的数据类型:其支持五种数据类型
### 2.1.字符串:是字节序列
	(1).Redis 字符串是二进制安全的,这意味着他们有一个已知的长度没有任何特殊字符终止,所以你可以存储任何东西,512M为上限;
		如一张图片等;
	(2).使用场景:
		①.使用 INCR 命令族 (INCR,DECR,INCRBY),将字符串作为原子计数器
		②.使用 APPEND 命令追加字符串
		③.使用 GETRANGE 和 SETRANGE 命令,使字符串作为随机访问向量 (vectors)
		④.编码大量数据到很小的空间,或者使用 GETBIT 和 SETBIT 命令,创建一个基于 Redis 的布隆 (Bloom) 过滤器
	(3).例子:
		127.0.0.1:6379> set name "coco"
		OK
		127.0.0.1:6379> get name
		"coco"
### 2.2.哈希/散列(hashes):
	(1).Redis 的哈希是键值对的集合. Redis 的哈希值是字符串字段和字符串值之间的映射,因此它们被用来表示对象
	(2).例子:
		127.0.0.1:6379> hmset user:1000 username bluefish password 123 port 8080
		OK
		127.0.0.1:6379> hgetall user:1
		1) "username"
		2) "coco"
		3) "password"
		4) "coco"
		5) "points"
		6) "200"
	(3).使用场景:
		①.由于哈希主要用来表示对象,对象能存储很多元素
	(4).每个哈希可以存储多达 2^23-1 个字段值对 (field-value pair)(多于 40 亿个);
### 2.3.列表(lists)
	(1).Redis 的列表是简单的字符串列表,是按照插入顺序排序的字符串列表.您可以添加元素到 Redis 的列表的头部或尾部
		表的最大长度为 2^32 - 1 元素（4294967295,每个列表中可容纳超过4十亿的元素）
	(2).例子:
		127.0.0.1:6379> lpush tutorial redis
		(integer) 1
		127.0.0.1:6379> lpush tutorial mongodb
		(integer) 2
		127.0.0.1:6379> lpush tutorial rabitmq
		(integer) 3
		127.0.0.1:6379> lrange tutorial 0 10
		1) "rabitmq"
		2) "mongodb"
		3) "redis"
	(3).LPUSH 命令插入一个新元素到列表头部,而 RPUSH 命令 插入一个新元素到列表的尾部
		当这两个命令操作在一个不存在的键时,将会创建一个新的列表
	(4).从时间复杂度的角度来看,Redis 列表主要特性就是支持时间常数的插入和靠近头尾部元素的删除,即使是需要插入上百万的条目
		访问列表两端的元素是非常快的,但如果你试着访问一个非常大 的列表的中间元素仍然是十分慢的;
	(5).使用场景:
		①.为社交网络时间轴 (timeline) 建模,使用 LPUSH 命令往用户时间轴插入元素,使用 LRANGE 命令获得最近事项;
		②.使用 LPUSH 和 LTRIM 命令创建一个不会超出给定数量元素的列表,只存储最近的 N 个元素;
		③.列表可以用作消息传递原语,例如,众所周知的用于创建后台任务的 Ruby 库 Resque;
		④.你可以用列表做更多的事情,这种数据类型支持很多的命令,包括阻塞命令,如 BLPOP
### 2.4.集合:Sets,不允许相同成员存在
	(1).Redis 的集合是字符串的无序集合。在Redis您可以添加,删除和测试文件是否存在,在成员 O(1)的时间复杂度
	(2).一个集合最多可以包含 2^32-1个元素（4294967295,每个集合超过40亿个元素）
		127.0.0.1:6379> sadd tutoriallist redis
		(integer) 1
		127.0.0.1:6379> sadd tutoriallist mongodb
		(integer) 1
		127.0.0.1:6379> sadd tutoriallist rabitmq
		(integer) 1
		127.0.0.1:6379> sadd tutoriallist rabitmq
		(integer) 0
		127.0.0.1:6379> smembers tutoriallist
		1) "rabitmq"
		2) "mongodb"
		3) "redis"
	(3).使用场景:
		①.你可以使用 Redis 集合追踪唯一性的事情,访问某篇博客文章的所有唯一 IP 吗?
		②.Redis 集合可以表示关系;
		③.你可以使用 SPOP 或 SRANDMEMBER 命令来从集合中随机抽取元素
### 2.5.有序集合:
	(1).Redis 的有序集合类似于 Redis 的集合,字符串不重复的集合
		不同的是,一个有序集合的每个成员用分数,以便采取有序set命令,从最小的到最大的成员分数有关
	(2).使用有序集合,你可以非常快地(O(log(N)))完成添加,删除和更新元素的操作
		由于元素是有序的而无需事后排序,你可以通过分数或者排名 (位置) 很快地来获取一个范围内的元素;
	(3).使用场景:
		①.获取排行:例如多人在线游戏排行榜,每次提交一个新的分数,你就使用 ZADD 命令更新
		②.有序集合常用来索引存储在 Redis 内的数据
		③.有序集合或许是最高级的 Redis 数据类型
### 2.6.位图(bitmaps)和超重对数(hyperloglogs):两种基于字符串基本类型
## 3.Redis-keys:
	用于在Redis的管理键,二进制安全的,可以用任何二进制序列作为key值
	3.1.Redis keys命令使用语法如下所示：			
		127.0.0.1:6379> COMMAND KEY_NAME
	3.2.关于key的几条规则：
		(1).太长的键值不是个好主意,例如1024字节的键值就不是个好主意,不仅因为消耗内存,
			而且在数据中查找这类键值的计算成本很高。
		(2)太短的键值通常也不是好主意,如果你要用"u:1000:pwd"来代替"user:1000:password",这没有什么问题,
			但后者更易阅读,并且由此增加的空间消耗相对于key object和value object本身来说很小。
		(3).最好坚持一种模式。例如："object-type:id:field"就是个不错的注意,像这样"user:1000:password"。
			我喜欢对多单词的字段名中加上一个点,就像这样:"comment:1234:reply.to"
		(4).键值最大值为 512MB
## 4.Redis Strings:在Redis的管理字符串值
	(1).最简单Redis类型,如果你只用这种类型,Redis 就像一个可以持久化的memcached服务器
		(memcache的数据仅保存在内存中,服务器重启后,数据将丢失)
		127.0.0.1:6379> COMMAND KEY_NAME
	(2).字符串是Redis的基本值类型,仍然能通过它完成一些有趣的操作:
		127.0.0.1:6379> set counter 100
		OK
		127.0.0.1:6379> incr counter
		(integer) 101
		127.0.0.1:6379> incr counter
		(integer) 102
		127.0.0.1:6379> incrby counter 50
		(integer) 152
		127.0.0.1:6379>
		==> INCR 命令将字符串值解析成整型,将其加一,最后将结果保存为新的字符串值,类似的命令有 INCRBY, DECR 和 DECRBY
		==> INCR 是原子操作意味着即使多个客户端对同一个key发出 INCR 命令,也决不会导致竞争的情况
		==> GETSET:为key设置新值并且返回原值
		==> 为减少等待时间,也可以一次存储或获取多个key对应的值,使用 MSET 和 MGET 命令,MGET 命令返回由值组成的数组
			127.0.0.1:6379> mset a 10 b 20 c 30
			OK
			127.0.0.1:6379> mget a b c 
			1) "10"
			2) "20"
			3) "30"
			127.0.0.1:6379> 
	(3).如果键值已经存在,则执行失败:
			SET mykey newval nx
## 5.改变和查询键值空间:
	(1).有一些命令并不定义在特定的类型上,但是对键空间的交互很有用,因此他们能作用在任意键上
	(2).EXISTS 命令返回 1(存在) 或 0(不存在),来表示键在数据库是否存在;
		DEL 命令删除键极其关联的值,无论值是什么,删除成功返回 1,失败返回 0
			127.0.0.1:6379> EXISTS name
			(integer) 1
			127.0.0.1:6379> DEL name
			(integer) 1
			127.0.0.1:6379> EXISTS name
			(integer) 0
			127.0.0.1:6379> DEL user:1
			(integer) 1
			127.0.0.1:6379> EXISTS user:1
			(integer) 0
			127.0.0.1:6379> DEL name
			(integer) 0
	(3).TYPE 命令返回某个键的值的类型
			127.0.0.1:6379> set name chenlanqing
			OK
			127.0.0.1:6379> type name
			string
			127.0.0.1:6379> del name
			(integer) 1
			127.0.0.1:6379> type name
			none
## 6.Redis 过期 (expires):有限生存时间的键
	(1).Redis 过期:给键设置超时,也就是一个有限的生存时间.当生存时间到了,键就会自动被销毁,就像用户调用 DEL 命令一样。
	(2).特点：
		①.过期时间可以设置为秒或者毫秒精度;
		②.过期时间分辨率总是 1 毫秒;
		③.过期信息被复制和持久化到磁盘,当 Redis 停止时时间仍然在计算 (也就是说 Redis 保存了过期时间)
	(3).使用:也可以为一个已经设置过期时间的键设置不同的过期时间,就像 PERSIST 命令可以删除过期时间使键永远存在
		也可以直接设置过期时间:set name coco ex 10 
		127.0.0.1:6379> set name coco
		OK		
		127.0.0.1:6379> expire name 5
		(integer) 1
		127.0.0.1:6379> get name
		"coco"
		127.0.0.1:6379> get name (5s后)
		(nil)
	(4).EXPIRE 命令设置过期时间,单位:秒;
		PEXPIRE 命令设置过期时间,单位:毫秒
	(5).TTL 命令检查键的生存剩余时间,单位是:秒; 
		PTTL 命令检查键的生存剩余时间,单位是:毫秒
		ttl key
		pttl key
## 7.Redis 列表:
	7.1.使用链表实现,如果链表中有上百万个元素,增加一个元素到列表的头部或者尾部的操作都是在常量时间完成
		(1).缺点:访问链表的速度很慢;
	7.2.为什么使用链表实现列表?
		(1).对于数据库系统来说,快速插入一个元素到一个很长的列表非常重要
		(2).Redis 列表能在常数时间内获得常数长度
		(3).访问拥有大量数据的集合数据结构,使用另外称为 有序集合 的数据结果
	7.3.列表相关命令:
		(1).LPUSH 命令从左边(头部)添加一个元素到列表,
			RPUSH 命令从右边(尾部)添加一个元素的列表;
			这两个命令都是可变参数命令,一个命令调用中自由的添加多个元素到列表
		(2).LRANGE 命令从列表中提取一个范围内的元素:
			LRANGE 命令使用两个索引下标,分别是返回的范围的开始和结束元素
			两个索引坐标可以是负数,表示从后往前数,所以: -1 表示最后一个元素,-2 表示倒数第二个元素;
		(3).弹出元素:指的是从列表中检索元素,并同时将其从列表中清除的操作
			从左边或者右边弹出元素,类似于你可以从列表的两端添加元素
			LPOP:命令从左边(头部)弹出一个元素到列表
			RPOP:命令从右边(尾部)弹出一个元素的列表;
			如果列表已经没有元素了,Redis 返回一个 NULL 值来表明列表中没有元素了
	7.4.通用场景:具有代表性的场景:
		(1).记住社交网络中用户最近提交的更新
		(2).使用生产者消费者模式来进程间通信,生产者添加项(item)到列表,消费者(通常是 worker)消费项并执行任务
	7.5.上限列表(Capped Lists):
		(1).Redis 允许使用列表作为一个上限集合,使用 LTRIM 命令仅仅只记住最新的 N 项,丢弃掉所有老的项
		(2).LTRIM 命令类似于 LRANGE,但是不同于展示指定范围的元素,而是将其作为列表新值存储,所有范围外的元素都被删了;
			127.0.0.1:6379> rpush mlist 1 2 3 4 5
			(integer) 5
			127.0.0.1:6379> ltrim mlist 0 2
			OK
			127.0.0.1:6379> lrange mlist 0 -1
			1) "1"
			2) "2"
			3) "3"
			LTRIM 和 LPUSH 组合使用实现一个添加操作和一个修剪操作一起,实现新增一个元素抛弃超出元素
	7.6.列表的阻塞操作(blocking):列表有一个特别的特性使得其适合实现队列,通常作为进程间通信系统的积木:阻塞操作
		(1).案例:假设你想往一个进程的列表中添加项,用另一个进程来处理这些项,这就是通常的生产者消费者模式,可以使用如下方式:
			①.生产者调用 LPUSH 添加项到列表中;
			②.消费者调用 RPOP 从列表提取/处理项;有时候列表是空的,没有需要处理的,RPOP 就返回 NULL,
			所以消费者被强制等待一段时间并重试 RPOP 命令。这称为轮询(polling)
		(2).上述方式不适用的情况:
			①.强制 Redis 和客户端处理无用的命令 (当列表为空时的所有请求都没有执行实际的工作,只会返回 NULL).
			②.由于工作者受到一个 NULL 后会等待一段时间,这会延迟对项的处理。
		(3).针对上述情况,Redis 实现了 BRPOP 和 BLPOP 两个命令,它们是当列表为空时 RPOP 和 LPOP 的会阻塞版本:
			仅当一个新元素被添加到列表时,或者到达了用户的指定超时时间,才返回给调用者:
				127.0.0.1:6379> brpop tasks 5 // 等待 tasks 列表中的元素,如果 5 秒后还没有可用元素就返回
				1) "tasks"
				2) "do_something"
			注意:你可以使用 0 作为超时让其一直等待元素,你也可以指定多个列表而不仅仅只是一个,
				同时等待多个列表,当第一个列表收到元素后就能得到通知。
		(4).关于 BRPOP 的一些注意事项:
			①.客户端按顺序服务:第一个被阻塞等待列表的客户端,将第一个收到其他客户端添加的元素,等等
			②.与 RPOP 的返回值不同:返回的是一个数组,其中包括键的名字,因为 BRPOP 和 BLPOP 可以阻塞等待多个列表的元素
			③.如果超时时间到达,返回 NULL
		(5).更多列表和阻塞选项:
			使用 RPOLPUSH 构建更安全的队列和旋转队列。
			BRPOPLPUSH 命令是其阻塞变种命令
	7.7.自动创建和删除键:
		(1).当列表为空时 Redis 将删除该键,当向一个不存在的列表键(如使用 LPUSH)添加一个元素时,将创建一个空的列表;
		(2).基本规则:
			①.当我们向聚合(aggregate)数据类型添加一个元素,如果目标键不存在,添加元素前将创建一个空的聚合数据类型。
			②.当我们从聚合数据类型删除一个元素,如果值为空,则键也会被销毁。
			③.调用一个像 LLEN 的只读命令(返回列表的长度),或者一个写命令从空键删除元素,
				总是产生和操作一个持有空聚合类型值的键一样的结果
## 8.Redis 哈希/散列 (Hashes):
	哈希就是字段值对(fields-values pairs)的集合
	8.1.HMSET 命令为哈希设置多个字段,HGET 检索一个单独的字段.HMGET 类似于 HGET,但是返回值的数组:
		127.0.0.1:6379>hmget user:1000 username birthyear no-such-field
		1) "antirez"
		2) "1977"
		3) (nil)
	8.2.HINCRBY:针对单个字段进行操作:
		127.0.0.1:6379> hget user:1 birthday
		"1989"
		127.0.0.1:6379> hincrby user:1 birthday 10
		(integer) 1999
	8.3.小的哈希 (少量元素,不太大的值) 在内存中以一种特殊的方式编码以高效利用内存
## 9.Redis 集合 (Sets):是无序的字符串集合 (collections)
	9.1.SADD 命令添加元素到集合:
		SADD myset 1 2 3 ==> 添加三个元素到myset中
		SMEMBERS myset ==> 返回 sets 中所有元素,
	9.2.SMEMBERS 命令获取集合的所有元素
	9.3.SISMEMBER 命令判断集合中是否存在某个元素,如果存在则返回 1,否则返回 0
		SISMEMBER myset 3
	9.4.集合适用于表达对象间关系。例如,我们可以很容易的实现标签。对这个问题的最简单建模,就是有一个为每个
		需要标记的对象的集合。集合中保存着与对象相关的标记的 ID。
		①.假设,我们想标记新闻。如果我们的 ID 为 1000 的新闻,被标签 1,2,5 和 77 标记,我们可以有一个这篇新闻
			被关联标记 ID 的集合:
			SADD news:1000:tags 1 2 5 77
	9.5.SINTER 命令实现对不同的集合执行交集:
		SINTER tag:1:news tag:2:news tag:10:news tag:27:news
		也可以执行并集,差集,随机抽取元素操作等等
	9.6.SPOP 命令随机删除一个元素,
		如扑克牌游戏
	9.7.SUNIONSTORE 命令对多个集合执行交集,然后把结果存储在另一个集合中,对单个集合求交集就是其自身即拷贝
	9.8.集合的基数(集合的势):
		对应的 Redis 命令:SCARD
		当你只需要获得随机元素而不需要从集合中删除,SRANDMEMBER 命令则适合你完成任务。它具有返回重复的和非重复的元素的能力
## 10.Redis 有序集合 (Sorted sets):
	10.1.有序集合类似于集合和哈希的混合体的一种数据类型;有序集合由唯一的,不重复的字符串元素组成,
		在某种意义上,有序集合也就是集合
		(1).集合中的每个元素是无序的,但有序集合中的每个元素都关联了一个浮点值,称为分数(score,这就是为什么该
			类型也类似于哈希,因为每一个元素都映射到一个值).
		(2).有序集合中的元素是按序存储的,不是请求时才排序的,顺序是依赖于表示有序集合的数据结构,按如下规则排序:
			①.如果 A 和 B 是拥有不同分数的元素,A.score > B.score,则 A > B
			②.如果 A 和 B 是有相同的分数的元素,如果按字典顺序 A 大于 B,则 A > B.A 和 B 不能相同,因为排序集合只能有唯一元素
	10.2.ZADD 命令添加一个或多个元素到有序集合,也可以是可变参数的
		ZADD key score member
		==>注意:有序集合是通过双端(dual-ported)数据结构实现的,包括跳跃表(skiplist)和哈希表(hashtable),
			每次添加元素时 Redis 执行 O(log(N)) 的操作,请求有序元素时,Redis 根本不需要做什么工作
	10.3.ZRANGE 命令正序获取有序集合的元素;
		ZREVRANGE 命令按相反的顺序获取有序集合的元素;
		也可以同时返回分数,使用 WITHSCORES 参数
		ZRANGE hackers 0 -1 WITHSCORES
	10.4.范围操作:可以在范围上操作(Page 43)
		使用 ZRANGEBYSCORE 命令

# 二.Redis 配置文件: "redis.conf" 常用配置介绍:
    1. Redis 默认不是以守护进程的方式运行,可以通过该配置项修改,使用yes启用守护进程
    	daemonize no
    2. 当Redis以守护进程方式运行时,Redis默认会把pid写入/var/run/redis.pid文件,可以通过pidfile指定
    	pidfile /var/run/redis.pid
    3. 指定Redis监听端口,默认端口为6379,作者在自己的一篇博文中解释了为什么选用6379作为默认端口,因为6379在手机按键上MERZ对应的号码,而MERZ取自意大利歌女Alessia Merz的名字
    	port 6379
    4. 绑定的主机地址
    	bind 127.0.0.1
    5.当 客户端闲置多长时间后关闭连接,如果指定为0,表示关闭该功能
    	timeout 300
    6. 指定日志记录级别,Redis总共支持四个级别：debug、verbose、notice、warning,默认为verbose
    	loglevel verbose
    7. 日志记录方式,默认为标准输出,如果配置Redis为守护进程方式运行,而这里又配置为日志记录方式为标准输出,则日志将会发送给/dev/null
    	logfile stdout
    8. 设置数据库的数量,默认数据库为0,可以使用SELECT <dbid>命令在连接上指定数据库id
    	databases 16
    9. 指定在多长时间内,有多少次更新操作,就将数据同步到数据文件,可以多个条件配合
		save <seconds> <changes>
		Redis默认配置文件中提供了三个条件：
		save 900 1
		save 300 10
		save 60 10000
		分别表示900秒（15分钟）内有1个更改,300秒（5分钟）内有10个更改以及60秒内有10000个更改。
    10. 指定存储至本地数据库时是否压缩数据,默认为yes,Redis采用LZF压缩,如果为了节省CPU时间,可以关闭该选项,但会导致数据库文件变的巨大
    	rdbcompression yes
    11. 指定本地数据库文件名,默认值为dump.rdb
    	dbfilename dump.rdb
    12. 指定本地数据库存放目录
    	dir ./
    13. 设置当本机为slav服务时,设置master服务的IP地址及端口,在Redis启动时,它会自动从master进行数据同步
    	slaveof <masterip> <masterport>
    14. 当master服务设置了密码保护时,slav服务连接master的密码
		masterauth <master-password>
    15. 设置Redis连接密码,如果配置了连接密码,客户端在连接Redis时需要通过AUTH <password>命令提供密码,默认关闭
    	requirepass foobared
    16. 设置同一时间最大客户端连接数,默认无限制,Redis可以同时打开的客户端连接数为Redis进程可以打开的最大文件描述符数,
		如果设置 maxclients 0,表示不作限制。当客户端连接数到达限制时,Redis会关闭新的连接并向客户端返回max number of clients reached错误信息
    	maxclients 128
    17. 指定Redis最大内存限制,Redis在启动时会把数据加载到内存中,达到最大内存后,Redis会先尝试清除已到期或即将到期的Key,
	当此方法处理 后,仍然到达最大内存设置,将无法再进行写入操作,但仍然可以进行读取操作。Redis新的vm机制,会把Key存放内存,Value会存放在swap区
		maxmemory <bytes>
			(1)volatile-lru:使用LRU算法移除key,只对设置了过期时间的键
			(2)allkeys-lru:使用LRU算法移除key
			(3)volatile-random:在过期集合中移除随机的key,只对设置了过期时间的键
			(4)allkeys-random:移除随机的key
			(5)volatile-ttl:移除那些TTL值最小的key,即那些最近要过期的key
			(6)noeviction:不进行移除。针对写操作,只是返回错误信息
    18. 指定是否在每次更新操作后进行日志记录,Redis在默认情况下是异步的把数据写入磁盘,如果不开启,
	可能会在断电时导致一段时间内的数据丢失。因为 redis本身同步数据文件是按上面save条件来同步的,
	所以有的数据会在一段时间内只存在于内存中。默认为no
    	appendonly no
    19. 指定更新日志文件名,默认为appendonly.aof
    	appendfilename appendonly.aof
    20. 指定更新日志条件,共有3个可选值： 
        no：表示等操作系统进行数据缓存同步到磁盘（快） 
        always：表示每次更新操作后手动调用fsync()将数据写到磁盘（慢,安全） 
        everysec：表示每秒同步一次（折衷,默认值）
        appendfsync everysec
    21. 指定是否启用虚拟内存机制,默认值为no,简单的介绍一下,VM机制将数据分页存放,由Redis将访问量较少的页即冷数据swap到磁盘上,
	访问多的页面由磁盘自动换出到内存中（在后面的文章我会仔细分析Redis的VM机制）
    	vm-enabled no
    22. 虚拟内存文件路径,默认值为/tmp/redis.swap,不可多个Redis实例共享
   		vm-swap-file /tmp/redis.swap
    23. 将所有大于vm-max-memory的数据存入虚拟内存,无论vm-max-memory设置多小,所有索引数据都是内存存储的
	(Redis的索引数据 就是keys),也就是说,当vm-max-memory设置为0的时候,其实是所有value都存在于磁盘。默认值为0
    	vm-max-memory 0
    24. Redis swap文件分成了很多的page,一个对象可以保存在多个page上面,但一个page上不能被多个对象共享,
	vm-page-size是要根据存储的 数据大小来设定的,作者建议如果存储很多小对象,page大小最好设置为32或者64bytes；
	如果存储很大大对象,则可以使用更大的page,如果不 确定,就使用默认值
    	vm-page-size 32
    25. 设置swap文件中的page数量,由于页表（一种表示页面空闲或使用的bitmap）是在放在内存中的,,在磁盘上每8个pages将消耗1byte的内存。
    	vm-pages 134217728
    26. 设置访问swap文件的线程数,最好不要超过机器的核数,如果设置为0,那么所有对swap文件的操作都是串行的,可能会造成比较长时间的延迟。默认值为4
    	vm-max-threads 4
    27. 设置在向客户端应答时,是否把较小的包合并为一个包发送,默认为开启
    	glueoutputbuf yes
    28. 指定在超过一定的数量或者最大的元素超过某一临界值时,采用一种特殊的哈希算法
		hash-max-zipmap-entries 64
		hash-max-zipmap-value 512
    29. 指定是否激活重置哈希,默认为开启（后面在介绍Redis的哈希算法时具体介绍）
    	activerehashing yes
    30. 指定包含其它的配置文件,可以在同一主机上多个Redis实例之间使用同一份配置文件,而同时各个实例又拥有自己的特定配置文件
    	include /path/to/local.conf

# 三.Redis 持久化:
## 1.rdb:(Redis Datbase):保存为 dump.rdb
	1.1.RDB:在指定的时间间隔内将内存中的数据集快照写入磁盘,也就是行话讲的Snapshot快照,它恢复时是将快照文件直接读到内存里;
		Redis 会单独创建(fork)一个子进程来进行持久化,会先将数据写入到 一个临时文件中,待持久化过程都结束了,再用这个临时文件替换
		上次持久化好的文件.整个过程中,主进程是不进行任何IO操作的,这就确保了极高的性能 如果需要进行大规模数据的恢复,
		且对于数据恢复的完整性不是非常敏感,那RDB方式要比AOF方式更加的高效.RDB 的缺点是最后一次持久化后的数据可能丢失.
	1.2.关于 fork:
		Fork 的作用是复制一个与当前进程一样的进程.新进程的所有数据(变量、环境变量、程序计数器等)数值都和原进程一致,
		但是是一个全新的进程,并作为原进程的子进程.
	1.3.配置位置:
		################################################################ SNAPSHOTTING  ################################################################
		# Save the DB on disk:
		#   save <seconds> <changes>
		#   Will save the DB if both the given number of seconds and the given
		#   number of write operations against the DB occurred.
		#   In the example below the behaviour will be to save:
		#   after 900 sec (15 min) if at least 1 key changed
		#   after 300 sec (5 min) if at least 10 keys changed
		#   after 60 sec if at least 10000 keys changed
		#   Note: you can disable saving completely by commenting out all "save" lines.
		#   It is also possible to remove all the previously configured save
		#   points by adding a save directive with a single empty string argument
		#   like in the following example:
		#
		#   save ""
		save 900 1
		save 300 10
		save 60 10000
	1.3.触发RD中B快照:
		(1).配置文件中默认的快照配置.
		(2).命令 save 或者 bgsave 
			==> save:时只管保存,其他不管,全部阻塞; 
			==> bgsave:redis会在后台异步进行快照操作,同时还可以响应客户端请求;
		(3).执行 flushall 命令,也会产生 dump.rdb 文件将,但里面是空的.
	1.4.将备份文件移动到 redis 安装目录并启动服务即可;
		config get dir 获取当前rdb文件存放的目录;
	1.5.优势与劣势:
		(1).优势:适合大规模的数据恢复,对数据完整性和一致性要求不高的;
		(2).劣势:在一定时间间隔做一次,如果redis意外宕机,就会丢失最后一次快照后的所有修改.
				fork 的时候,内存中的数据被克隆了一份,大致2倍的膨胀性需要考虑.
	1.6.停止RBD保存:
		动态停止RDB保存规则的方法:config set save ""
## 2.aof:(append only file)
	2.1.AOF 是什么:以日志的形式记录每个操作,将 Redis 执行过的所有写指令记录下来(读操作不记录),只许追加但不可以改写文件,
	 	redis启动之初会读取该文件重新构建数据,换言之,redis重启的话会根据日志文件的内容将写指令从前到后执行一次以完成数据的恢复工作.
	2.2.对应配置:
		appendonly					是否开启aof持久化,默认为 no
 		appendfilename				aof持久化名称,默认是:"appendonly.aof"			
		appendfsync 				持久化的时间,有三种模式
				always:同步持久化,每次发生数据变更会被立即记录到磁盘,性能较差但数据完整性比较好
				everysec:出厂默认推荐,异步操作,每秒记录,如果一秒内宕机,有数据丢失
				no:从不同步
		no-appendfsync-on-rewrite	重写时是否可以运用Appendfsync,用默认no即可,保证数据安全性。
		auto-aof-rewrite-min-size	设置重写的基准值,aof重写的最小值
		auto-aof-rewrite-percentage	设置重写的基准值,上次重写的比例
	2.3.AOF 启动/恢复/修复:
		2.3.1.正常操作:
			(1).启动:设置 appendonly yes,将有数据的aof文件拷贝一份备份到对应目录;
			(2).恢复:重启redis时然后重新加载;
		2.3.2.异常操作:
			备份被写坏的文件,运行 redis-check-aof --fix 进行修复;
			重启redis将重新加载;
	2.4.Rewrite:重写机制:
		(1).什么是重写:
			AOF 采用文件追加方式,文件会越来越大为避免出现此种情况,新增了重写机制,当AOF文件的大小超过所设定的阈值时,Redis 
			就会启动AOF文件的内容压缩,只保留可以恢复数据的最小指令集.可以使用命令 bgrewriteaof.
		(2).重写原理:
			AOF 文件持续增长而过大时,会fork出一条新进程来将文件重写(也是先写临时文件最后再rename),遍历新进程的内存中数据,
			每条记录有一条的Set语句.重写aof文件的操作,并没有读取旧的aof文件, 而是将整个内存中的数据库内容用命令的方式
			重写了一个新的aof文件,这点和快照有点类似;
		(3).触发:
			Redis 会记录上次重写时的AOF大小,默认配置是当AOF文件大小是上次 rewrite 后大小的一倍且文件大于64M时触发;
			==> 配置:
				auto-aof-rewrite-min-size 64M
	2.5.优势与劣势:
		2.5.1.优势:
			每修改同步:appendfsync always   同步持久化每次发生数据变更会被立即记录到磁盘,性能较差但数据完整性比较好
			每秒同步:appendfsync everysec    异步操作,每秒记录,如果一秒内宕机,有数据丢失
			不同步:appendfsync no   从不同步
		2.5.2.劣势:
			相同数据集的数据而言aof文件要远大于rdb文件,恢复速度慢于rdb
			Aof 运行效率要慢于rdb,每秒同步策略效率较好,不同步效率和rdb相同
## 3.关于持久化方案选择:
	(1).RDB 持久化方式能够在指定的时间间隔能对你的数据进行快照存储;
	(2).AOF 持久化方式记录每次对服务器写的操作,当服务器重启的时候会重新执行这些 命令来恢复原始的数据,AOF 命令以redis协议
		追加保存每次写的操作到文件末尾. Redis 还能对AOF文件进行后台重写,使得AOF文件的体积不至于过大;
	(3).只做缓存:如果你只希望你的数据在服务器运行的时候存在,你也可以不使用任何持久化方式.
	(4).同时开启RDB和AOF:
		在这种情况下,当redis重启的时候会优先载入AOF文件来恢复原始的数据,
		因为在通常情况下AOF文件保存的数据集要比RDB文件保存的数据集要完整.
		==> RDB 的数据不实时,同时使用两者时服务器重启也只会找AOF文件.那要不要只使用AOF呢?
		作者建议不要,因为RDB更适合用于备份数据库(AOF在不断变化不好备份),快速重启,而且不会有AOF可能潜在的bug,留着作为一个万一的手段。
## 4.性能建议:
	(1).因为RDB文件只用作后备用途,建议只在Slave上持久化RDB文件,而且只要15分钟备份一次就够了,只保留save 900 1这条规则。
	(2).如果Enalbe AOF,好处是在最恶劣情况下也只会丢失不超过两秒数据,启动脚本较简单只load自己的AOF文件就可以了.
		代价一是带来了持续的IO,二是AOF rewrite的最后将rewrite过程中产生的新数据写到新文件造成的阻塞几乎是不可避免的.
		只要硬盘许可,应该尽量减少AOF rewrite的频率,AOF重写的基础大小默认值64M太小了,可以设到5G以上。
		默认超过原大小100%大小时重写可以改到适当的数值。
 	(3).如果不Enable AOF ,仅靠Master-Slave Replication 实现高可用性也可以.能省掉一大笔IO也减少了rewrite时带来的系统波动.
 		代价是如果Master/Slave同时倒掉,会丢失十几分钟的数据,启动脚本也要比较两个Master/Slave中的RDB文件,载入较新的那个.
 		新浪微博就选用了这种架构
 		
# 四.Redis 事务:
## 1.事务:
    可以一次执行多个命令,本质是一组命令的集合,一个事务中的所有命令都会序列化,按顺序地串行执行而不会被其他命令插入,不允许加塞.
	所以可以任务事务是部分支持事务的.
## 2.如何使用:
	2.1.case1-正常执行:
		127.0.0.1:6379[1]> MULTI ==> 标记一个事务块的开始
		OK
		127.0.0.1:6379[1]> set balance 80
		QUEUED
		127.0.0.1:6379[1]> set debt 20
		QUEUED
		127.0.0.1:6379[1]> EXEC 	==> 执行所有事务块内的命令
	2.2.放弃事务:
		127.0.0.1:6379[1]> MULTI
		OK
		127.0.0.1:6379[1]> set balance 80
		QUEUED
		127.0.0.1:6379[1]> set debt 20
		QUEUED
		127.0.0.1:6379[1]> DISCARD  ==> 取消事务,放弃执行事务块内的所有命令
	2.3.全体连坐:如果中间有一个命令发生错误,都不执行.这种情况下就是在命令中发生了(error)
		类似于Java的非运行异常,需要在编译阶段捕获的异常信息.如 IOException
		127.0.0.1:6379[1]> MULTI
		OK
		127.0.0.1:6379[1]> set k1 v1
		QUEUED
		127.0.0.1:6379[1]> set k2 v2
		QUEUED
		127.0.0.1:6379[1]> set 3
		(error) ERR wrong number of arguments for 'set' command
		127.0.0.1:6379[1]> set k4 4
		QUEUED
		127.0.0.1:6379[1]> EXEC
		(error) EXECABORT Transaction discarded because of previous errors.
	2.4.冤头债主:即正确的命令执行,错误的抛出.
		类似于Java的运行时异常,在运行阶段抛出来的,如:int i = 10 / 0;
		127.0.0.1:6379[1]> MULTI
		OK
		127.0.0.1:6379[1]> INCR k1
		QUEUED
		127.0.0.1:6379[1]> INCR k2
		QUEUED
		127.0.0.1:6379[1]> INCRBY k3 5
		QUEUED
		127.0.0.1:6379[1]> exec
		1) (error) ERR value is not an integer or out of range ==> 错误命令抛出响应的错误信息
		2) (integer) 23
		3) (integer) 38
		127.0.0.1:6379[1]> 
	2.5.watch 监控:
		2.5.1.乐观锁与悲观锁/CAS:
			(1).乐观锁:每次操作数据都认为别人不会修改,所以不会加锁,但是每次更新的时候会判断在此期间有没有人去更新该数据,
				可以使用版本号的机制.乐观锁适用于多读的应用类型,这样可以提高吞吐量.
				==> 乐观锁策略: 提交的版本号必须大于记录当前版本才能更新.
			(2).悲观锁:每次操作数据的时候都认为别人会修改数据,所以每次都会加锁,这样其他的只能阻塞.
				传统的关系型数据库用到了很多悲观锁的机制:如行锁,表锁,读锁,写锁等.
		2.5.2.使用watch监控某个key或者多个key时,如果在事务还没提交时,有外部对这些key进行了写操作,那么整个事务队列不会执行.
			即 exec 命令执行的事务将被放弃,同时返回 null multi-bulk 应答通知调用者事务执行失败.
		2.5.3.unwatch:取消watch命令对所有key的监视.
	2.6.三阶段:	总的来说事务可以概括为3个阶段:
		(1).开启事务:以 multi 开始一个事务;
		(2).入队:将多个命令进入到事务的队列,接到这些命令并不会立即执行,而是放到等到执行的事务队列里.
		(3).执行:有exec触发事务执行.
	2.7.事务的三个特性:
		(1).单独的隔离操作:事务中的所有命令都会序列化、按顺序地执行.事务在执行的过程中.不会被其他客户端发送来的命令请求所打断;
		(2).没有隔离级别的概念:队列中的命令没有提交之前都不会实际的被执行,因为事务提交前任何指令都不会被实际执行,
			也就不存在"事务内的查询要看到事务里的更新,在事务外查询不能看到"这个让人万分头痛的问题
		(3).不保证原子性:redis同一个事务中如果有一条命令执行失败,其后的命令仍然会被执行,没有回滚

# 五.主从复制:
## 1.Redis 的复制:
	就是我们所说的主从复制,主机数据更新后根据配置和策略,自动同步到备机的 master/slaver机制,Master 以写为主,Slave 以读为主;
## 2.主从复制:
	读写分离
	容灾恢复
## 3.主从的配置:
	3.1.一般是配从(库)不配主(库);
	3.2.从库的配置:salveof 127.0.0.1(主库IP) 6379(主库端口)
		从库每次与 master断开之后,都需要重新连接,除非修复 redis.conf 配置文件;
		可以通过命令:info replication 查看当前库是主库还是从库
		==> 与主库断开:salveof no one
	3.3.操作细节(如果在同一台机器上)
		(1).拷贝多个 redis.conf 文件;
		(2).开启 daemonize 为 yes;
			daemonize yes
		(3).指定pid进程
			pidfile /var/run/redis_6379.pid
		(4).指定端口
			port 6379
		(5).log 文件名字
			logfile ""
		(6).dump.rdb 名字
			dbfilename dump.rdb
## 4.常用的主从模式:
### 4.1.一主二仆:即配置一台主库,两台从库
	4.1.1.主从显示的信息:
		(1).未配置主从时,显示如下:
			127.0.0.1:6379> info replication
			# Replication
			role:master
			connected_slaves:0
			master_repl_offset:0
			repl_backlog_active:0
			repl_backlog_size:1048576
			repl_backlog_first_byte_offset:0
			repl_backlog_histlen:0
		(2).配置主库后,主库显示如:
			127.0.0.1:6380> info replication
			# Replication
			role:master 														==> 当前库的角色
			connected_slaves:1
			slave0:ip=127.0.0.1,port=6379,state=online,offset=15,lag=0			==> 从库的信息
			master_repl_offset:15
			repl_backlog_active:1
			repl_backlog_size:1048576
			repl_backlog_first_byte_offset:2
			repl_backlog_histlen:14
		(3).配置从库后,从库显示如:
			127.0.0.1:6379> info replication
			# Replication
			role:slave 															==> 当前库的角色
			master_host:127.0.0.1
			master_port:6380
			master_link_status:up
			master_last_io_seconds_ago:6
			master_sync_in_progress:0
			slave_repl_offset:1
			slave_priority:100
			slave_read_only:1
			connected_slaves:0
			master_repl_offset:0
			repl_backlog_active:0
			repl_backlog_size:1048576
			repl_backlog_first_byte_offset:0
			repl_backlog_histlen:0
	4.1.2.一主二仆问题:
		(1).切入点问题?slave1、slave2是从头开始复制还是从切入点开始复制? 比如从k4进来,那之前的123是否也可以复制
			每次连接都都是全量复制数据
		(2).从机是否可以写?set可否?
			从库不能写,主库写,从库读
		(3).主机shutdown后情况如何?从机是上位还是原地待命?
			主机shutdown之后,从库原地待命,等到主机响应,"master_link_status:up"这个会变成:"master_link_status:down"
		(4).主机又回来了后,主机新增记录,从机还能否顺利复制?
			从库还是能顺利复制的.
		(5).其中一台从机down后情况如何?依照原有它能跟上大部队吗?
			从库宕机之后,与主库断开连接,如果从库在重启后,需要重新连接主库,除非有在redis.conf的配置
### 4.2.薪火相传:
	上一个Slave可以是下一个slave的Master,Slave 同样可以接收其他 slaves的连接和同步请求,那么该slave作为了链条中下一个的master,
	可以有效减轻master的写压力
	中途变更转向:会清除之前的数据,重新建立拷贝最新的
	slaveof 新主库IP 新主库端口
### 4.3.反客为主:
	slaveof no one
	使当前数据库停止与其他数据库的同步,转成主数据库
## 5.复制原理:
	(1).slave启动成功连接到master后会发送一个sync命令
	(2).master 接到命令启动后台的存盘进程,同时收集所有接收到的用于修改数据集命令,在后台进程执行完毕之后,
		master将传送整个数据文件到slave,以完成一次完全同步
	(3).全量复制：而slave服务在接收到数据库文件数据后,将其存盘并加载到内存中。
	(4).增量复制：Master 继续将新的所有收集到的修改命令依次传给slave,完成同步
		但是只要是重新连接master,一次完全同步（全量复制)将被自动执行
## 6.哨兵模式(sentinel):
	6.1.什么是哨兵模式:
		反客为主的自动版,能够后台监控主机是否故障,如果故障了根据投票数自动将从库转换为主库
	6.2.使用步骤:
		(1).在响应的目录下新建:sentinel.conf文件,名字绝不能错;
		(2).在 sentinel.conf 增加如下配置:
			sentinel monitor 被监控数据库名字(自己起名字) 127.0.0.1 6379 1
			上面最后一个数字1,表示主机挂掉后salve投票看让谁接替成为主机,得票数多少后成为主机;
		(3).启动哨兵:redis-sentinel sentinel.conf
		(4).原有的master挂了,投票新选,重新主从继续开工,info replication查查看
	6.5.问题:如果之前的master重启回来,会不会双master冲突?
		不会,之前的从库重启回来之后,会自动切换为从库,挂到之前从库转换为的主库上;
		一组sentinel能同时监控多个Master
## 7.复制的缺点:
	由于所有的写操作都是先在Master上操作,然后同步更新到Slave上,所以从Master同步到Slave机器有一定的延迟,当系统很繁忙的时候,
	延迟问题会更加严重,slave 机器数量的增加也会使这个问题更加严重

# 六.Redis应用
## 1.使用场景
	(1).缓存:将热点数据放到内存中
	(2).消息队列:List类型是双向链表,很适合用于消息队列;
	(3).计数器:Redis支持计数器频繁的读写操作
	(4).好友关系:使用 Set 类型的交集操作很容易就可以知道两个用户的共同好友
## 2.Redis数据淘汰策略:
	可以设置内存最大使用量，当内存使用量超过时施行淘汰策略，具体有 6 种淘汰策略。

| 策略 | 描述 |
| :--: | :--: |
| volatile-lru | 从已设置过期时间的数据集中挑选最近最少使用的数据淘汰 |
| volatile-ttl | 从已设置过期时间的数据集中挑选将要过期的数据淘汰 |
|volatile-random | 从已设置过期时间的数据集中任意选择数据淘汰 |
| allkeys-lru | 从所有数据集中挑选最近最少使用的数据淘汰 |
| allkeys-random | 从所有数据集中任意选择数据进行淘汰 |
| noeviction | 禁止驱逐数据 |

	如果使用 Redis 来缓存数据时,要保证所有数据都是热点数据,可以将内存最大使用量设置为热点数据占用的内存量,
	然后启用 allkeys-lru 淘汰策略，将最近最少使用的数据淘汰.
	作为内存数据库,出于对性能和内存消耗的考虑,Redis 的淘汰算法(LRU、TTL)实际实现上并非针对所有 key,
	而是抽样一小部分 key 从中选出被淘汰 key.抽样数量可通过 maxmemory-samples 配置.


# Redis面试题:
## 1.redis如何用作缓存? 如何确保不脏数据

## 2.Redis 和 Memcache区别:
	两者都是非关系型数据库.主要区别如下:
	(1).数据类型:
		* Memcached 仅支持字符串类型;
		* Redis 支持五种不同种类的数据类型,使得它可以更灵活地解决问题
	(2).数据持久化:
		* Memcached 不支持持久化;
		* Redis 支持两种持久化策略：RDB 快照和 AOF 日志
	(3).分布式:
		* Memcached 不支持分布式.只能通过在客户端使用像一致性哈希这样的分布式算法来实现分布式存储,
		  这种方式在存储和查询时都需要先在客户端计算一次数据所在的节点.
		* Redis Cluster 实现了分布式的支持
	(4).内存管理机制:
		* Memcached 将内存分割成特定长度的块来存储数据,以完全解决内存碎片的问题,但是这种方式会使得内存的利用率不高.
			例如块的大小为 128 bytes，只存储 100 bytes 的数据，那么剩下的 28 bytes 就浪费掉了
		* 在 Redis 中,并不是所有数据都一直存储在内存中,可以将一些很久没用的 value 交换到磁盘.而 Memcached 的数据则会一直在内存中
