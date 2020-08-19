
# 1、核心概念

## 1.1、主要特点

Kafka是分布式发布-订阅消息系统。它最初由LinkedIn公司开发，之后成为Apache项目的一部分。Kafka是一个分布式的，可划分的，冗余备份的持久性的日志服务，它主要用于处理活跃的流式数据。

主要特点：
- 是基于pull的模式来处理消息消费，追求高吞吐量
- 同时为发布和订阅提供高吞吐量。据了解，Kafka每秒可以生产约25万消息（50 MB），每秒处理55万消息（110 MB）。同时kafka也支持点对点的消息投递，消费者都隶属于一个消费组，相当于点对点模型；
- 可进行持久化操作。将消息持久化到磁盘，因此可用于批量消费，例如ETL，以及实时应用程序。通过将数据持久化到硬盘以及replication防止数据丢失。
- 分布式系统，易于向外扩展。所有的producer、broker和consumer都会有多个，均为分布式的。无需停机即可扩展机器。
- 消息被处理的状态是在consumer端维护，而不是由server端维护。当失败时能自动平衡。
- 支持online和offline的场景。
- 支持分区，并支持跨平台、伸缩性、实时性；

## 1.2、kafka架构

Kafka的整体架构非常简单，是显式分布式架构，producer、broker（kafka）和consumer都可以有多个。Producer，consumer实现Kafka注册的接口，数据从producer发送到broker，broker承担一个中间缓存和分发的作用。broker分发注册到系统中的consumer。broker的作用类似于缓存，即活跃的数据和离线处理系统之间的缓存。客户端和服务器端的通信，是基于简单，高性能，且与编程语言无关的TCP协议。

基本概念：
- Message：消息，是通信的基本单位，每个producer可以向一个topic（主题）发布一些消息。
- Producers：消息和数据生产者，向Kafka的一个topic发布消息的过程叫做producers。
- Consumers：消息和数据消费者，订阅topics并处理其发布的消息的过程叫做consumers。
- Consumer Group (CG)：消费者组，由多个 consumer 组成。消费者组内每个消费者负 责消费不同分区的数据，一个分区只能由一个组内消费者消费;消费者组之间互不影响。所有的消费者都属于某个消费者组，即消费者组是逻辑上的一个订阅者
- Broker：缓存代理，Kafka集群中的一台或多台服务器统称为broker。一台 kafka 服务器就是一个 broker。一个集群由多个 broker 组成。一个 broker 可以容纳多个 topic；
- Topic：特指Kafka处理的消息源（feeds of messages）的不同分类。
- Partition：Topic物理上的分组，一个topic可以分为多个partition，每个partition是一个有序的队列。partition中的每条消息都会被分配一个有序的id（offset）。
- Replica副本，为保证集群中的某个节点发生故障时，该节点上的 partition 数据不丢失，且 kafka 仍然能够继续工作，kafka 提供了副本机制，一个 topic 的每个分区都有若干个副本， 一个 leader 和若干个 follower
- In Sync Replicas（ISR）：
    - HW：High Watermark，高水位线
    - LEO：Log End offset，

**发送消息的流程：**
- Producer根据指定的partition方法（round-robin、hash等），将消息发布到指定topic的partition里面
- kafka集群接收到Producer发过来的消息后，将其持久化到硬盘，并保留消息指定时长（可配置），而不关注消息是否被消费。
- Consumer从kafka集群pull数据，并控制获取消息的offset

## 1.3、设计思路

## 1.4、高性能原因

- 顺序写，PageCache空中接力，高效读写，避免了随机写；
- 后台异步、主动Flush；
- 高性能、高吞吐;
- 预读策略；

## 1.5、kafka零拷贝

[零拷贝](../../Java框架/NIO-Netty.md#14.1零拷贝Zero-Copy技术)

## 1.6、Kafka应用场景

- 异步化、服务解耦、削峰填谷
- 海量日志手机
- 数据同步应用
- 实时计算分析

# 2、kafka配置安装

|功能	 |            启动命令	|备注|
|-------|---------------------|----|
|启动  ZK	        | `bin/zookeeper-server-start.sh -daemon config/zookeeper.properties`	|Kafka 安装包自带 ZK，可以单节点启动|
|启动Kafka服务器      | `bin/kafka-server-start.sh config/server.properties` || 	
|创建 Topic（test）	 | `bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test`|	|
|Topic 列表	        | `bin/kafka-topics.sh --list --zookeeper localhost:2181`	||
|启动 Producer	    | `bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test`	||
|启动 Consumer	    | `bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning`	||
|Topic相关信息（test) | `bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic test` || 

# 3、Kafka入门


# 4、kafka生产者

## 4.1、生产者API

ProducerConfig

KafkaProducer

ProducerRecord

KafkaProducer消息发送重试

## 4.2、kafka生产者重要参数

- `acks`：指定消息发送后，Broker端至少有多个副本接收该消息，默认为1；
    - acks=0表示生产者发送消息之后不需要等到任何服务端的响应；
    - acks=-1， acks=all：生成在消息发送之后，需要等待ISP中的所有副本都成功写入消息后才能收到服务端的成功响应；但是有并不代表kafka高可用；

- `max.request.size`：该参数用来限制生产者客户端能发送的消息的最大值；默认是1M；

- `retries 和 retry.backoff.msretries`：重试次数（默认是0）和重试间隔（默认是100ms）；

- `compression.type`：用来指定的消息的压缩方式，默认值为`none`，可选配置：`gzip`、`snappy`、`lz4`；

- `connections.max.idle.ms`：用来指定在多久之后关闭限制的连接，默认是 54000ms，即9分钟；

- `linger.ms`：用来指定生产者发送 ProducerBatch 之前等待更多的消息（ProducerRecord）加入 ProducerBatch的时间，默认是0；

- `batch.size`：累计多少条消息，则一次进行批量发送；只要满足 linger.ms 和 batch.size 一个，都会发生的；

- `buffer.memory`：缓存提示性能参数，默认是32M；

- `receive.buffer.bytes`：用来设置Socket接收消息缓冲区（SO_RECBUF）的大小，默认是32k；

- `send.buffer.bytes`：用来设置socket发送消息缓冲区（SO_SNDBUF）的大小，默认是128k；

- `request.timeout.ms`：用来配置Producer等待请求响应的最长时间，默认是 30000ms

## 4.3、kafka拦截器

- 生产者实现接口：ProducerInterceptor
    
    添加拦截器到
    ```java
    properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, CustromProducerInterceptor.class.getName());
    ```

- 消费者实现接口：ConsumerInterceptor

    `properties.put(ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG, CustomConsumerInterceptor.class.getName());`

## 4.4、kafka序列化与反序列化

- 序列化接口：Serializer
- 反序列化接口：Deserializer

# 5、Kafka消费者与消费者组

## 5.1、点对点与发布/订阅模式


## 5.2、kafka消费者必要参数


## 5.3、消费者提交位移



# 参考资料

* [Kafka详解](https://mp.weixin.qq.com/s/d9KIz0xvp5I9rqnDAlZvXw)
* [Kafka入门](https://blog.csdn.net/hmsiwtv/article/details/46960053)
* [Kafka面试题](https://juejin.im/post/6844903837614997518)
* [Kafka进阶面试题](https://mp.weixin.qq.com/s/CFzd7rwMFWtqc8xzCQ8vVw)
* [Kafka进阶面试题2](https://mp.weixin.qq.com/s/2QA_UIE_ciTJDQ4kbUkl-A)
* [Kafka与RocketMQ](https://mp.weixin.qq.com/s/WwHnyrOnw_io7G3uviim3Q)
* [Kafka高性能原因](https://mp.weixin.qq.com/s/XhJl90DnprNsI8KxFfdyVw)