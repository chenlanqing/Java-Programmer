# 1、简介

Apache Flink 是一个框架和分布式处理引擎，用于在无边界和有边界数据流上进行有状态的计算

## 1、1、为什么选择Flink

- 流数据更真实的反映了生活方式；
- 传统的数据结构是基于有限数据集的；
- 需要实现的目标：
    - 低延迟
    - 高吞吐
    - 结果的准确性和良好的容错性；

## 1.2、使用场景

## 1.3、主要特点

- 事件驱动
- 基于流：在Flink中，一切都是由流组成的，离线数据是有界的流；实时数据是没有界限的流
- 分层API：越顶层越抽象；越底层越具体
- 支持高度灵活的窗口操作；
- 支持有状态计算的Exactly-once语义

## 1.4、Flink与Spark

- 数据模型：
    - Spark采用RDD模型，Spark Stream的 DStream 实际上是一组小批数据RDD的集合；
    - Flink 是基本数据模型是基于流的，以及事件序列；
- 运行时架构：
    - Spark是批计算的，将DAG划分为不同的stage，一个完成后才会计算下一个；
    - Flink是标准的流计算，一个事件在一个节点处理完成后可以直接发往下一个节点进行处理；

## 1.5、编程模型与核心概念

### 1.5.1、编程模型


### 1.5.2、核心概念

- 窗口
- 时间：
    - 事件时间
    - 摄入时间
    - 处理时间
- 并行度，并行度不能大于slot个数
    - 算子级别
    - 运行环境级别
    - 客户端级别
    - 系统级别

## 1.6、基本架构



# 2、运行与部署Flink

## 2.1、三种运行模式

## 2.2、standlone模式

- 提交任务：`./flink run -c com.blue.fish.StreamWordCount /Users/bluefish/Documents/workspace/bigdata/flink-demo/target/flink-demo-1.0-SNAPSHOT-jar-with-dependencies.jar --host localhost --port 7777`

- 获取正在运行任务列表：`./flink list`
    ```
    Waiting for response...
    ------------------ Running/Restarting Jobs -------------------
    18.04.2020 20:33:23 : 5b656bb6de0911c0301c1b0479c91f07 : stream word count job (RUNNING)
    --------------------------------------------------------------
    ```

- 获取所有任务列表：`./flink list --all`
    ```
    Waiting for response...
    No running jobs.
    No scheduled jobs.
    ---------------------- Terminated Jobs -----------------------
    18.04.2020 20:24:01 : af34e90c87b4c6d7a8ed2a7e7491b731 : stream word count job (CANCELED)
    18.04.2020 20:27:27 : c2cce314180b0d2aa0cd20f8b942e614 : stream word count job (CANCELED)
    18.04.2020 20:33:23 : 5b656bb6de0911c0301c1b0479c91f07 : stream word count job (CANCELED)
    --------------------------------------------------------------
    ```
- 取消任务：
    ```
    ./flink cancel 5b656bb6de0911c0301c1b0479c91f07
    ```

# 3、运行架构

# 4、流处理API

Enviroment  -> Source -> Tranform -> Sink

## 4.1、Enviroment

- getExecutionEnvironment：
    StreamExecutionEnvironment.getExecutionEnvironment
- createLocalEnvironment
- createRemoteEnvironment

## 4.2、Source

## 4.3、Transform

## 4.4、支持的数据类型

## 4.5、UDF函数

## 4.6、Sink





# 参考资料

- [官方代码](https://github.com/apache/flink)
- [Flink实现推荐系统](https://github.com/CheckChe0803/flink-recommandSystem-demo)
- [Flink中文视频课程](https://github.com/flink-china/flink-training-course)
- [Flink系列](http://wuchong.me/categories/Flink/)
- [Flink入门示例](https://github.com/zhisheng17/flink-learning)
- [Flink-CEP复杂事件](https://cloud.tencent.com/developer/article/1448608)
- [Flink知识图谱](https://yq.aliyun.com/articles/744741?spm=a2c4e.11153940.0.0.69bc12ecS2IswO)
- [Apache Flink 精选PDF下载](https://yq.aliyun.com/articles/81743?spm=a2c4e.11153940.0.0.69bc12ecS2IswO)
- [Apache Flink CEP 实战](https://yq.aliyun.com/articles/738451?utm_content=g_1000094637)
- [Flink基础理论](https://blog.csdn.net/oTengYue/article/details/102689538)
- [基于flink和drools的实时日志处理](https://www.cnblogs.com/luxiaoxun/p/13197981.html)
- [Flink 精进学习](https://www.yuque.com/docs/share/a4b45fed-7417-4789-8df3-071abb9b3cac)

