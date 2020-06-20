
## 1、核心概念

### 1.1、主要特点

Kafka是分布式发布-订阅消息系统。它最初由LinkedIn公司开发，之后成为Apache项目的一部分。Kafka是一个分布式的，可划分的，冗余备份的持久性的日志服务，它主要用于处理活跃的流式数据。

主要特点：
- 同时为发布和订阅提供高吞吐量。据了解，Kafka每秒可以生产约25万消息（50 MB），每秒处理55万消息（110 MB）。
- 可进行持久化操作。将消息持久化到磁盘，因此可用于批量消费，例如ETL，以及实时应用程序。通过将数据持久化到硬盘以及replication防止数据丢失。
- 分布式系统，易于向外扩展。所有的producer、broker和consumer都会有多个，均为分布式的。无需停机即可扩展机器。
- 消息被处理的状态是在consumer端维护，而不是由server端维护。当失败时能自动平衡。
- 支持online和offline的场景。

### 1.2、kafka架构

Kafka的整体架构非常简单，是显式分布式架构，producer、broker（kafka）和consumer都可以有多个。Producer，consumer实现Kafka注册的接口，数据从producer发送到broker，broker承担一个中间缓存和分发的作用。broker分发注册到系统中的consumer。broker的作用类似于缓存，即活跃的数据和离线处理系统之间的缓存。客户端和服务器端的通信，是基于简单，高性能，且与编程语言无关的TCP协议。

基本概念：
- Topic：特指Kafka处理的消息源（feeds of messages）的不同分类。
- Partition：Topic物理上的分组，一个topic可以分为多个partition，每个partition是一个有序的队列。partition中的每条消息都会被分配一个有序的id（offset）。
- Message：消息，是通信的基本单位，每个producer可以向一个topic（主题）发布一些消息。
- Producers：消息和数据生产者，向Kafka的一个topic发布消息的过程叫做producers。
- Consumers：消息和数据消费者，订阅topics并处理其发布的消息的过程叫做consumers。
- Broker：缓存代理，Kafka集群中的一台或多台服务器统称为broker。

**发送消息的流程：**
- Producer根据指定的partition方法（round-robin、hash等），将消息发布到指定topic的partition里面
- kafka集群接收到Producer发过来的消息后，将其持久化到硬盘，并保留消息指定时长（可配置），而不关注消息是否被消费。
- Consumer从kafka集群pull数据，并控制获取消息的offset

### 1.3、设计思路



使用 Kafka
功能	            启动命令	备注
启动  ZK	        bin/zookeeper-server-start.sh -daemon config/zookeeper.properties	Kafka 安装包自带 ZK，可以单节点启动
启动Kafka服务器      bin/kafka-server-start.sh config/server.properties	
创建 Topic（test）	 bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test	
Topic 列表	        bin/kafka-topics.sh --list --zookeeper localhost:2181	
启动 Producer	    bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test	
启动 Consumer	    bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning	
Topic相关信息（test) bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic test

https://mp.weixin.qq.com/s/d9KIz0xvp5I9rqnDAlZvXw

https://blog.csdn.net/hmsiwtv/article/details/46960053

http://cmsblogs.com/?p=10502