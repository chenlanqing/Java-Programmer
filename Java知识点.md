```
基础1：-----------------------
- JAVA基础
        - JDK
                - 基础：
                        - 强引用、弱引用、虚引用、软引用
                        - final关键字的作用 (方法、变量、类)
                        - 泛型、泛型继承、泛型擦除
                        - jdk ServiceLoader
                        - LinkedList、LinkedHashMap、LRU
                        - 装饰者模式、代理模式、责任链模式、工厂模式、适配器模式、建造者模式、单例模式、模板模式、观察者模式..
                        - 关于精度损失问题：int、long 超过最大值
                        - 关于注解：元注解的种类、继承java.lang.Annotation、注解的基础类型、注解的常用方法                         
                        - 关于ClassLoader，类加载器，双亲委派模型                        
                - J.U.C
                        - 线程池参数说明，线程池的线程回收、shutdown
                        - 线程池的生命周期？
                        - 线程池的核心模型Worker对象的运作流程是怎样的？
                        - 线程池的拒绝策略有哪4种？
                        - 线程池的提交，execute与submit有什么区别？在实际开发中需要注意哪些问题？需要注意异常的处理、是否需要获取记过
                        - threadlocal原理，数据结构
                        - 并发集合类了解哪些？
                                - ConcurrentHashMap
                                - CopyOnWrite集合、原理、锁机制
                                - ConcurrentLinkedQueue、LinkedTransferQueue、ArrayBlockingQueue、PriorityBlockingQueue、SynchronousQueue、DelayQueue
                        - AQS 原理：
                                - 独占 & 共享
                                - state & CHL队列
                        - 锁：
                                - Synchronized、ReentrantLock、RWLock、Condition、LockSupport、StampedLock、
                                - 概念：CAS 自旋、重入、偏向
                        - volatile：
                                - 多线程共享 & 阻止指令重排序
                                - jvm的逃逸分析  & Tlab & 消除伪共享 & UNsafe &
                        - atomic:
                                - CAS的缺点，自旋、ABA问题
                                - atomic 原子性、Reference、referenceArray、longadder
                        - 并发控制：
                                 - barrier、countdownlatch、exchanger、future、semaphore
                - jvm虚拟机：
                        - 虚拟机内存模型
                        - 新生代（Eden S0 S1）、老年代 、MetaSpace （比例）
                        - 垃圾回收算法（引用计数、标记压缩、清除、复制算法、分区）、垃圾收集器
                        - GC停顿、吞吐量，进入老年代阈值、大对象回收问题等
                        - jvm性能调优、参数配置
                        - 常用命令：jstat、jmap、jstack等
                        - 内存溢出分析：堆内、堆外 （含义、如何设置）
                        - CPU飙升：死锁、线程阻塞
                        - 关于GC: minor major full
                         - stw，安全点等

        - 数据结构&算法
                - 数组、链表、树、队列..
                - 关于时间复杂度，时间换空间转换案例
                - 关于排序、冒泡、快排、递归、二分搜索、位运算




基础2：-----------------------
- Spring
        - Spring生命周期，流程梳理
        - Spring扩展点作用：BeanFactoryPostProcessor
        - Spring IOC AOP 基本原理
        - 动态代理
        - BeanPostProcessor 作用？
        - ApplicationContextAware 的作用和使用？
        - BeanNameAware与BeanFactoryAware的先后顺序？
        - InitializingBean 和 BeanPostProcessor 的after方法先后顺序？
        - ApplicationListener监控的Application事件有哪些？
        - Spring模块装配的概念，比如@EnableScheduling @EnableRetry @EnableAsync，@Import注解的作用？
        - ImportBeanDefinitionRegistrar 扩展点用于做什么事情？
        - ClassPathBeanDefinitionScanner 的作用？
        - NamespaceHandlerSupport 命名空间扩展点的作用？
        - 如何实现动态注入一个Bean？
        - 如何把自定义注解所在的Class 初始化注入到Spring容器？
        - BeanDefinition指的是什么，与BeanDefinitionHolder的区别，Spring如何存储BeanDefinition实例？
        - ASM 与 CGlib
        - Spring的条件装配，自动装配

- RPC通信框架
        - Dubbo
                - Dubbo的Spi机制？
                - Dubbo的核心模型 invoker、invocation、filter
                - Dubbo的隐式传递?
                - Dubbo的泛化调用？
                - Dubbo的export与importer时机？
                - Dubbo的服务调用过程？
                - Dubbo的负载均衡策略？
                - Dubbo的集群容错？

- 网络通信
        - IO / NIO
                - IO NIO区别？
                - 多路复用的概念，Selector
                - Channel的概念、Bytebuf的概念，flip、position...
                - FileChannel 如何使用？
                - RAF使用，seek、skip方法

        - Netty
                - 关于Netty的Reactor实现？
                - Netty的ByteBuf有哪些？
                - 内存与非内存Bytebuffer的区别与使用场景？
                - 池化与非池化buffer的区别与使用场景？
                - 关于Netty的请求Buffer和响应Buffer?
                - Netty的ChannelPipeline设计模式？
                - Netty的核心option参数配置？
                - Netty的ChannelInboundHandlerAdapter和SimpleChannelInboundHandler关系？
                - Netty的EventLoop核心实现？
                - Netty的连接管理事件接口有哪些常用方法（ChannelDuplexHandler）？
                - Netty的编解码与序列化手段
                - Netty的FastThreadLocal实现？
                - Netty中应用的装饰者 和 观察者模式在哪里体现？

- MQ
        - API使用，常用生产消费模型，集群架构搭建
        - 常见问题，消息可靠性投递、幂等性保障
        - 概念、原理、存储、消息投递、通信机制、性能相关优化
        - MQ常见的作用于目的、服务解耦、削峰填谷等
                - RocketMQ
                - Kafka
                - RabbitMQ
                - ActiveMQ

- 缓存
        - 内存缓存
                - 堆外内存缓存 回收释放
        - Redis
                - 缓存穿透、雪崩、热点Key、大Key、无底洞问题，缓存更新与淘汰、缓存与数据库的一致性
                - Redis的幂等性
                - Redis的分布式锁实现
                - Redis的原子性，Redis的特点
                - Redis集群相关问题、一致性hash、slot概念等

基础3：-----------------------
- 流控组件
        - Hystrix
        - Sentinel

- 高可用服务中间件
        - Zookeeper / Curator
        - Nginx
        - Haproxy
        - LVS
        - Haproxy

- 数据库存储 & 调度
        - Sharding-JDBC
        - ElasticJob
        - 调度平台相关：DAG、airflow等

- 搜索相关
        - ELK ，数据库加速、主搜（算法）
         - Logback、Slf4j2
         - Solr & Lucene

- BPM相关
        - JBPM
         - Activiti
- 大数据相关：
        - monogdb
                - 集群模式：replication复制集、shard分片
                - 复制集原理、分片原理
                - 分片键选择、最佳实践
        - hadoop
                - hfds原理、mr概念模型
                - 应用场景，使用；业务实践
        - hbase、hive
                - hbase特点、模型、应用场景（列簇模型）
                - hive、kylin实践场景（olap）
                - etl工具、etl实践、思路
        - 批处理/流处理：spark/flink
                - streaming/sql
                - flink 应用场景实践
        - 数据仓库、数据建模
                 - 数据采集
                - 标签系统实践、数据仓库实践
                - 用户画像、行为分析
                - 精准营销、推送；
        - 指标度量
                - OpenStack、kvm
                - Docker、k8s
                - devops/aiops相关
        - 全链路压测
                - 灰度、埋点、热变更
- 数据同步
        - mysql binlog、文件同步

- 操作系统
        - 命令：AWK、top、netstat、grep
        - 内存分页管理与 Swap 机制、任务队列与 CPU Load 
        - 内存屏障、指令乱序、分支预测、NUMA 与 CPU 亲和性等
```
