# 1、 Pulsar简介

## 1.1、pulsar概述

Pulsar 非常灵活，可以像Kafka 一样作为分布式日志系统，也可以作为类似RabbitMQ 这类简单的消息系统。

Pulsar 有多种订阅类型、传递保障、保存策略

## 1.2、特点

- 内置多租户：不同的团队可以使用同一个集群，互相隔离。支持隔离、认证授权、配额；
- 多层架构：
    - Pulsar 使用特定的数据层来存储 topic 数据，使用了 Apache BookKeeper 作为数据账本。Broker 与存储分离。
    - 使用分隔机制可以解决集群的扩展、再平衡、维护等问题。也提升了可用性，不会丢失数据。
    - 因为使用了多层架构，对于 topic 数量没有限制，topic 与存储是分离的，也可以创建非持久化的 topic；
- 多层存储：Kafka中存储是很昂贵的，所以很少存储冷数据。Pulsar 使用了多层存储，可以自动把旧数据移动到专门的存储设备；
- Functions：Pulsar Function 是一种部署简单，轻量级计算、对开发人员友好的 API，无需像 Kafka 那样运行自己的流处理引擎；
- 安全：内置了代理、多租户安全机制、可插入的身份验证等功能；
- 快速再平衡：partition 被分为了小块儿，所以再平衡时非常快

## 1.3、Pulsar与Kafka

- 流式处理和队列的合体：Pulsar 不仅可以像 Kafka 那样处理高速率的实时场景，还支持标准的消息队列模式，Pulsar 具备传统消息队列（如 RabbitMQ）的功能；
- 支持分区：Kafka通过分区进而划分到不同的 broker，单个主题的处理速率可以得到大幅提升。但如果某些主题不需要太高的处理速率；Pulsar 就可以做到。如果只需要一个主题，可以使用一个主题而无需使用分区。如果需要保持多个消费者实例的处理速率，也不需要使用分区，Pulsar 的共享订阅可以达到这一目的；
- 无状态：
    - Kafka 不是无状态的，每个 broker 都包含了分区的所有日志，如果一个 broker 宕机，不是所有 broker 都可以接替它的工作。如果工作负载太高，也不能随意添加新的 broker 来分担，而是必须与持有其分区副本的 broker 进行状态同步；
    - 在 Pulsar 架构中，broker 是无状态的。但是完全无状态的系统无法持久化消息，所以 Pulsar 不是依靠 broker 来实现消息持久化的。在 Pulsar 架构中，数据的分发和保存是相互独立的。broker 从生产者接收数据，然后将数据发送给消费者，但数据保存在 BookKeeper 中。Pulsar 的 broker 是无状态的，所以如果工作负载很高，可以直接添加新的 broker，快速接管工作负载；
- 简单的跨域复制

**Kafka的缺点：**
- 存储计算没有分离, 难以扩容和缩容
- 没有 IO 隔离, 写高了读不动, 读高了写不动
- 没有多租户IO 
- 模型过于简单, 面对大量 topic 时性能下降严重
- 消费模型有限, 无法利用部分业务可以接受消息无序的特点



# 参考资料

- [官方文档](http://pulsar.apache.org/)
- [理解Pulsar工作原理](https://blog.csdn.net/u010869257/article/details/83211152)
- [理解Pulsar工作原理-原文](https://jack-vanlightly.com/blog/2018/10/2/understanding-how-apache-pulsar-works)
- [Pulsar架构与核心概念](https://zhuanlan.zhihu.com/p/88618994)
- [Pulsar与Kafka对比](https://www.infoq.cn/article/1uaxfkwuhukty1t_5gpq)