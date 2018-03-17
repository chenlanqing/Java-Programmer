<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一.项目介绍](#%E4%B8%80%E9%A1%B9%E7%9B%AE%E4%BB%8B%E7%BB%8D)
- [二.Java基础](#%E4%BA%8Cjava%E5%9F%BA%E7%A1%80)
- [三.Java 并发](#%E4%B8%89java-%E5%B9%B6%E5%8F%91)
- [四.Spring](#%E5%9B%9Bspring)
- [五.Netty](#%E4%BA%94netty)
- [六.分布式相关](#%E5%85%AD%E5%88%86%E5%B8%83%E5%BC%8F%E7%9B%B8%E5%85%B3)
- [七.数据库](#%E4%B8%83%E6%95%B0%E6%8D%AE%E5%BA%93)
- [八.缓存](#%E5%85%AB%E7%BC%93%E5%AD%98)
- [九.JVM](#%E4%B9%9Djvm)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 一.项目介绍

    大部分情况，这是一场面试的开门题，面试官问这个问题，主要是考察你的概述能力和全局视野。有的人经常抱怨自己每天在堆业务，但没有成长。
    事实上，很多情况下确实在堆业务，但并不是没有成长的。并非做中间件或者技术架构才是成长，例如我们的需求分析能力，沟通协作能力，产品思
    维能力，抽象建模能力等都是一个非常重要的硬实力。

    1、明确项目是做什么的 
    2、明确项目的价值。（为什么做这个项目，它解决了用户什么痛点，它带来什么价值？） 
    3、明确项目的功能。（这个项目涉及哪些功能？） 
    4、明确项目的技术。（这个项目用到哪些技术？） 
    5、明确个人在项目中的位置和作用。（你在这个项目的承担角色？） 
    6、明确项目的整体架构。 
    7、明确项目的优缺点,如果重新设计你会如何设计。 
    8、明确项目的亮点。（这个项目有什么亮点？） 
    9、明确技术成长。（你通过这个项目有哪些技术成长？）

# 二.Java基础
    1、List 和 Set 的区别 
    2、HashSet 是如何保证不重复的 
    3、HashMap 是线程安全的吗，为什么不是线程安全的（最好画图说明多线程环境下不安全）? 
    4、HashMap 的扩容过程 
    5、HashMap 1.7 与 1.8 的 区别，说明 1.8 做了哪些优化，如何优化的？ 
    6、final finally finalize 
    7、强引用 、软引用、 弱引用、虚引用 
    8、Java反射 
    9、Arrays.sort 实现原理和 Collection 实现原理 
    10、LinkedHashMap的应用 
    11、cloneable接口实现原理 
    12、异常分类以及处理机制 
    13、wait和sleep的区别 
    14、数组在内存中如何分配

# 三.Java 并发

    1、synchronized 的实现原理以及锁优化？ 
    2、volatile 的实现原理？ 
    3、Java 的信号灯？ 
    4、synchronized 在静态方法和普通方法的区别？ 
    5、怎么实现所有线程在等待某个事件的发生才会去执行？ 
    6、CAS？CAS 有什么缺陷，如何解决？ 
    7、synchronized 和 lock 有什么区别？ 
    8、Hashtable 是怎么加锁的 ？ 
    9、HashMap 的并发问题？
    10、ConcurrenHashMap 介绍？1.8 中为什么要用红黑树？ 
    11、AQS 
    12、如何检测死锁？怎么预防死锁？ 
    13、Java 内存模型？ 
    14、如何保证多线程下 i++ 结果正确？ 
    15、线程池的种类，区别和使用场景？ 
    16、分析线程池的实现原理和线程的调度过程？ 
    17、线程池如何调优，最大数目如何确认？ 
    18、ThreadLocal原理，用的时候需要注意什么？ 
    19、CountDownLatch 和 CyclicBarrier 的用法，以及相互之间的差别? 
    20、LockSupport工具 
    21、Condition接口及其实现原理 
    22、Fork/Join框架的理解 
    23、分段锁的原理,锁力度减小的思考 
    24、八种阻塞队列以及各个阻塞队列的特性

# 四.Spring

    1、BeanFactory 和 FactoryBean？ 
    2、Spring IOC 的理解，其初始化过程？ 
    3、BeanFactory 和 ApplicationContext？ 
    4、Spring Bean 的生命周期，如何被管理的？ 
    5、Spring Bean 的加载过程是怎样的？ 
    6、如果要你实现Spring AOP，请问怎么实现？ 
    7、如果要你实现Spring IOC，你会注意哪些问题？ 
    8、Spring 是如何管理事务的，事务管理机制？ 
    9、Spring 的不同事务传播行为有哪些，干什么用的？ 
    10、Spring 中用到了那些设计模式？ 
    11、Spring MVC 的工作原理？ 
    12、Spring 循环注入的原理？ 
    13、Spring AOP的理解，各个术语，他们是怎么相互工作的？ 
    14、Spring 如何保证 Controller 并发的安全？

# 五.Netty

    1、BIO、NIO和AIO 
    2、Netty 的各大组件 
    3、Netty的线程模型 
    4、TCP 粘包/拆包的原因及解决方法 
    5、了解哪几种序列化协议？包括使用场景和如何去选择 
    6、Netty的零拷贝实现 
    7、Netty的高性能表现在哪些方面

# 六.分布式相关

    1、Dubbo的底层实现原理和机制 
    2、描述一个服务从发布到被消费的详细过程 
    3、分布式系统怎么做服务治理 
    4、接口的幂等性的概念 
    5、消息中间件如何解决消息丢失问题 
    6、Dubbo的服务请求失败怎么处理 
    7、重连机制会不会造成错误 
    8、对分布式事务的理解 
    9、如何实现负载均衡，有哪些算法可以实现？ 
    10、Zookeeper的用途，选举的原理是什么？ 
    11、数据的垂直拆分水平拆分。 
    12、zookeeper原理和适用场景 
    13、zookeeper watch机制 
    14、redis/zk节点宕机如何处理 
    15、分布式集群下如何做到唯一序列号 
    16、如何做一个分布式锁 
    17、用过哪些MQ，怎么用的，和其他mq比较有什么优缺点，MQ的连接是线程安全的吗 
    18、MQ系统的数据如何保证不丢失 
    19、列举出你能想到的数据库分库分表策略；分库分表后，如何解决全表查询的问题 
    20、zookeeper的选举策略 
    21、全局ID

# 七.数据库

    1、mysql分页有什么优化 
    2、悲观锁、乐观锁 
    3、组合索引，最左原则 
    4、mysql 的表锁、行锁 
    5、mysql 性能优化 
    6、mysql的索引分类：B+，hash；什么情况用什么索引 
    7、事务的特性和隔离级别

# 八.缓存

    1、Redis用过哪些数据数据，以及Redis底层怎么实现 
    2、Redis缓存穿透，缓存雪崩 
    3、如何使用Redis来实现分布式锁 
    4、Redis的并发竞争问题如何解决 
    5、Redis持久化的几种方式，优缺点是什么，怎么实现的 
    6、Redis的缓存失效策略 
    7、Redis集群，高可用，原理 
    8、Redis缓存分片 
    9、Redis的数据淘汰策略

# 九.JVM

    1、详细jvm内存模型 
    2、讲讲什么情况下回出现内存溢出，内存泄漏？  
    3、说说Java线程栈 
    4、JVM 年轻代到年老代的晋升过程的判断条件是什么呢？ 
    5、JVM 出现 fullGC 很频繁，怎么去线上排查问题？ 
    6、类加载为什么要使用双亲委派模式，有没有什么场景是打破了这个模式？ 
    7、类的实例化顺序 
    8、JVM垃圾回收机制，何时触发MinorGC等操作 
    9、JVM 中一次完整的 GC 流程（从 ygc 到 fgc）是怎样的 
    10、各种回收器，各自优缺点，重点CMS、G1 
    11、各种回收算法 
    12、OOM错误，stackoverflow错误，permgen space错误