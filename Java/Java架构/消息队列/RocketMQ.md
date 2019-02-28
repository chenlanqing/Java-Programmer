
# 一、RocketMQ介绍

## 1、基本概念

RocketMQ是一款分布式、队列模型的消息中间件；最新版4.3.x版本（支持分布式事务）；

- 其支持集群模型、负载均衡、水平扩展能力；
- 亿级别的消息堆积能力；
- 采用零拷贝的原理、顺序写盘、随机读；
- 丰富的API使用；
- 底层通信框架采用netty nio框架；
- 使用NameServer代替zookeeper；
- 强调集群无单点；
- 消息失败重试机制、消息可查询

## 2、概念模型

- Producer：消息生产者，负责生产消息，一般由业务系统负责生产消息
- Consumer：消息消费者，负责消费消息，一般是后台系统负责异步消费；
- Push Cosnumer：Consumer的一种，需要向Consumer对象注册监听；
- Pull Cosnumer：Consumer的一种，需要主动请求Broker拉取消息；
- Producer Group：生产者集合，一般用于发送一类消息；主要是在事务消息回查；
- Consumer Group：消费者集合，一般用于接受一类消息进行消费；
- Broker：MQ消息服务，用于消息存储和生产消费转发；

## 3、搭建RocketMQ环境

### 3.1、单机Rocket

- 下载[rocketMQ](https://github.com/apache/rocketmq)源代码，这里选择4.3.0版本的；
- 使用命令编译项目：`mvn -Prelease-all -DskipTests clean install -U`
- 在项目`distribution/target/`目录中找到：apache-rocketmq.tar.gz，上传到服务器（192.168.56.101）目录：`/usr/local/software/`
- 修改host，添加如下信息：
    ```
    192.168.56.101 rocketmq-nameserver1
    192.168.56.101 rocketmq-master1
    ```
- 解压文件到`/usr/local/apache-rocketmq`，并创建软连接：`ln -s apache-rocketmq rocketmq`
- 创建存储路径
    ```
    mkdir /usr/local/rocketmq/store
    mkdir /usr/local/rocketmq/store/commitlog
    mkdir /usr/local/rocketmq/store/consumequeue
    mkdir /usr/local/rocketmq/store/index
    ```
- 修改rokcetMQ配置文件：`vim /usr/local/rocketmq/conf/2m-2s-async/broker-a.properties`
    ```properties
    brokerClusterName=rocketmq-cluster
    #broker 名字，注意此处不同的配置文件填写的不一样
    brokerName=broker-a
    #0 表示 Master，>0 表示 Slave
    brokerId=0
    #nameServer 地址，分号分割
    namesrvAddr=rocketmq-nameserver1:9876
    #在发送消息时，自动创建服务器不存在的 topic，默认创建的队列数
    defaultTopicQueueNums=4
    #是否允许 Broker 自动创建 Topic，建议线下开启，线上关闭
    autoCreateTopicEnable=true
    #是否允许 Broker 自动创建订阅组，建议线下开启，线上关闭
    autoCreateSubscriptionGroup=true

    #Broker 对外服务的监听端口
    listenPort=10911
    #删除文件时间点，默认凌晨 4 点
    deleteWhen=04
    #文件保留时间，默认 48 小时
    fileReservedTime=120
    #commitLog 每个文件的大小默认 1G
    mapedFileSizeCommitLog=1073741824
    #ConsumeQueue 每个文件默认存 30W 条，根据业务情况调整
    mapedFileSizeConsumeQueue=300000
    #destroyMapedFileIntervalForcibly=120000
    #redeleteHangedFileInterval=120000
    #检测物理文件磁盘空间
    diskMaxUsedSpaceRatio=88
    #存储路径
    storePathRootDir=/usr/local/rocketmq/store
    #commitLog 存储路径
    storePathCommitLog=/usr/local/rocketmq/store/commitlog
    #消费队列存储路径存储路径
    storePathConsumeQueue=/usr/local/rocketmq/store/consumequeue
    #消息索引存储路径
    storePathIndex=/usr/local/rocketmq/store/index
    #checkpoint 文件存储路径
    storeCheckpoint=/usr/local/rocketmq/store/checkpoint
    #abort 文件存储路径
    abortFile=/usr/local/rocketmq/store/abort
    #限制的消息大小
    maxMessageSize=65536
    #flushCommitLogLeastPages=4
    #flushConsumeQueueLeastPages=2
    #flushCommitLogThoroughInterval=10000
    #flushConsumeQueueThoroughInterval=60000
    #Broker 的角色
    #- ASYNC_MASTER 异步复制 Master
    #- SYNC_MASTER 同步双写 Master
    #- SLAVE
    brokerRole=ASYNC_MASTER
    #刷盘方式
    #- ASYNC_FLUSH 异步刷盘
    #- SYNC_FLUSH 同步刷盘
    flushDiskType=ASYNC_FLUSH
    #checkTransactionMessageEnable=false
    #发消息线程池数量
    #sendMessageThreadPoolNums=128
    #拉消息线程池数量
    #pullMessageThreadPoolNums=128
    ```
- 修改配置文件：
    ```
    mkdir -p /usr/local/rocketmq/logs
    cd /usr/local/rocketmq/conf && sed -i 's#${user.home}#/usr/local/rocketmq#g' *.xml
    ```
- 修改启动脚本参数，如果是个人虚拟机，可以修改如下配置：
    - `vim /usr/local/rocketmq/bin/runbroker.sh`，修改JVM参数：`-Xms1g -Xmx1g -Xmn512m`
    - `vim /usr/local/rocketmq/bin/runserver.sh`，修改JVM参数：`-Xms1g -Xmx1g -Xmn512m`
- 进入到bin目录，启动nameServer：`nohup sh mqnamesrv &`；
- 启动broker-a：`nohup sh mqbroker -c /usr/local/rocketmq/conf/2m-2s-async/broker-a.properties >/dev/null 2>&1 &`
- 数据清理：
    ```
    # cd /usr/local/rocketmq/bin
    # sh mqshutdown broker
    # sh mqshutdown namesrv
    # --等待停止
    # rm -rf /usr/local/rocketmq/store
    # mkdir /usr/local/rocketmq/store
    # mkdir /usr/local/rocketmq/store/commitlog
    # mkdir /usr/local/rocketmq/store/consumequeue
    # mkdir /usr/local/rocketmq/store/index
    # --按照上面步骤重启 NameServer 与 BrokerServer
    ```
   
### 3.2、集群

四种集群环境构建：单点、主从、双主、多主多从

主从模式集群环境构建：
- 主从模式环境构建可以保障消息的即时性与可靠性；投递一条消息后，关闭主节点，从节点继续可以提供消费者数据进行消费，但是不能接收消息；主节点上线后进行消费进度offset同步





## 4、RocketMQ控制台

可以参考：[rocketmq-externals](https://github.com/apache/rocketmq-externals)，其有一个子项目：rocketmq-console，修改其配置文件：application.properties中的配置：`rocketmq.config.namesrvAddr=192.168.56.101:9876`，其余不用动，其是一个springBoot项目


# 二、RocketMQ入门

## 1、生产者使用

- 创建生产者对象：DefaultMQProducer
- 设置NamesrvAddr
- 启动生产者服务；
- 创建消息并发送

## 2、消费者使用

- 创建消费者对象：DefaultMQPushConsumer
- 设置namesrvAddr及其消费位置ConsumeFromWhere；
- 进行订阅主题subscribe；
- 注册监听并消费registerMessageListener；