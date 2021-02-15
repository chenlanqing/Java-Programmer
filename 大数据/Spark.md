
# 1、Spark概述

## 1.1、什么是Spark

Spark 是一种基于内存的快速、通用、可扩展的大数据分析计算引擎

## 1.2、Spark发展

MapReduce的局限性：
- 代码繁琐；
- 只能够支持map和reduce方法；
- 执行效率低下；
- 不适合迭代多次、交互式、流式的处理；

Spark是一种由Scala语言开发的快速、通用、可扩展的大数据分析引擎
- SparkCore中提供了Spark最基础与最核心的功能
- Spark SQL是Spark用来操作结构化数据的组件。通过Spark SQL，用户可以使用SQL 或者 Apache Hive 版本的 SQL 方言(HQL)来查询数据。
- Spark Streaming 是 Spark 平台上针对实时数据进行流式计算的组件，提供了丰富的处理数据流的 API；
- 丰富的类库支持：包括 SQL，MLlib，GraphX 和 Spark Streaming 等库，并且可以将它们无缝地进行组合；  
- 丰富的部署模式：支持本地模式和自带的集群模式，也支持在 Hadoop，Mesos，Kubernetes 上运行；

**Spark 和 Hadoop 的根本差异是多个作业之间的数据通信问题**: Spark 多个作业之间数据 通信是基于内存，而 Hadoop 是基于磁盘

## 1.3、Spark模块

Spark 基于 Spark Core 扩展了四个核心组件，分别用于满足不同领域的计算需求。

![](image/spark-stack.png)

### 1.3.1、Spark Core

Spark Core 中提供了 Spark 最基础与最核心的功能，Spark 其他的功能如：Spark SQL、Spark Streaming、GraphX,、MLlib 都是在 Spark Core 的基础上进行扩展的；

### 1.3.2、Spark SQL

Spark SQL 主要用于结构化数据的处理。其具有以下特点：

- 能够将 SQL 查询与 Spark 程序无缝混合，允许您使用 SQL 或 DataFrame API 对结构化数据进行查询；
- 支持多种数据源，包括 Hive，Avro，Parquet，ORC，JSON 和 JDBC；
- 支持 HiveQL 语法以及用户自定义函数 (UDF)，允许你访问现有的 Hive 仓库；
- 支持标准的 JDBC 和 ODBC 连接；
- 支持优化器，列式存储和代码生成等特性，以提高查询效率。

### 1.3.2、Spark Streaming

Spark Stream主要用于快速构建可扩展，高吞吐量，高容错的流处理程序。支持从 HDFS，Flume，Kafka，Twitter 和 ZeroMQ 读取数据，并进行处理；

![](image/spark-streaming-arch.png)

 Spark Streaming 的本质是微批处理，它将数据流进行极小粒度的拆分，拆分为多个批处理，从而达到接近于流处理的效果。

![](image/spark-streaming-flow.png)

### 1.3.3、MLlib

MLlib 是 Spark 的机器学习库。其设计目标是使得机器学习变得简单且可扩展。它提供了以下工具：

- **常见的机器学习算法**：如分类，回归，聚类和协同过滤；
- **特征化**：特征提取，转换，降维和选择；
- **管道**：用于构建，评估和调整 ML 管道的工具；
- **持久性**：保存和加载算法，模型，管道数据；
- **实用工具**：线性代数，统计，数据处理等。

### 1.3.4、Graphx

GraphX 是 Spark 中用于图形计算和图形并行计算的新组件。在高层次上，GraphX 通过引入一个新的图形抽象来扩展 RDD(一种具有附加到每个顶点和边缘的属性的定向多重图形)。为了支持图计算，GraphX 提供了一组基本运算符（如： subgraph，joinVertices 和 aggregateMessages）以及优化后的 Pregel API。此外，GraphX 还包括越来越多的图形算法和构建器，以简化图形分析任务

# 2、Spark安装与运行环境

[Spark编译安装](../辅助资料/环境配置/大数据环境.md#4Spark编译安装)

## 2.1、spark-submit

Spark 所有模式均使用 `spark-submit` 命令提交作业，其格式如下：

```shell
./bin/spark-submit \
  --class <main-class> \        # 应用程序主入口类
  --master <master-url> \       # 集群的 Master Url
  --deploy-mode <deploy-mode> \ # 部署模式
  --conf <key>=<value> \        # 可选配置       
  ... # other options    
  <application-jar> \           # Jar 包路径 
  [application-arguments]       #传递给主入口类的参数  
```

需要注意的是：在集群环境下，`application-jar` 必须能被集群中所有节点都能访问，可以是 HDFS 上的路径；也可以是本地文件系统路径，如果是本地文件系统路径，则要求集群中每一个机器节点上的相同路径都存在该 Jar 包。

### 2.1.1、deploy-mode

deploy-mode 有 `cluster` 和 `client` 两个可选参数，默认为 `client`。这里以 Spark On Yarn 模式对两者进行说明 ：
- 在 cluster 模式下，Spark Drvier 在应用程序的 Master 进程内运行，该进程由群集上的 YARN 管理，提交作业的客户端可以在启动应用程序后关闭；
- 在 client 模式下，Spark Drvier 在提交作业的客户端进程中运行，Master 进程仅用于从 YARN 请求资源；

### 2.1.2、master-url

master-url 的所有可选参数如下表所示：

| Master URL                        | Meaning                                                      |
| --------------------------------- | ------------------------------------------------------------ |
| `local`                           | 使用一个线程本地运行 Spark                                    |
| `local[K]`                        | 使用 K 个 worker 线程本地运行 Spark                          |
| `local[K,F]`                      | 使用 K 个 worker 线程本地运行 , 第二个参数为 Task 的失败重试次数 |
| `local[*]`                        | 使用与 CPU 核心数一样的线程数在本地运行 Spark                   |
| `local[*,F]`                      | 使用与 CPU 核心数一样的线程数在本地运行 Spark<br/>第二个参数为 Task 的失败重试次数 |
| `spark://HOST:PORT`               | 连接至指定的 standalone 集群的 master 节点。端口号默认是 7077。 |
| `spark://HOST1:PORT1,HOST2:PORT2` | 如果 standalone 集群采用 Zookeeper 实现高可用，则必须包含由 zookeeper 设置的所有 master 主机地址。 |
| `mesos://HOST:PORT`               | 连接至给定的 Mesos 集群。端口默认是 5050。对于使用了 ZooKeeper 的 Mesos cluster 来说，使用 `mesos://zk://...` 来指定地址，使用 `--deploy-mode cluster` 模式来提交。 |
| `yarn`                            | 连接至一个 YARN 集群，集群由配置的 `HADOOP_CONF_DIR` 或者 `YARN_CONF_DIR` 来决定。使用 `--deploy-mode` 参数来配置 `client` 或 `cluster` 模式。 |

## 2.2、Local模式

Local 模式，就是不需 要其他任何节点资源就可以在本地执行 Spark 代码的环境，一般用于教学、调试、演示
```shell
# 本地模式提交应用
spark-submit \
--class org.apache.spark.examples.SparkPi \
--master local[2] \
examples/jars/spark-examples_2.11-2.4.0.jar \
100   # 传给 SparkPi 的参数
```

`spark-examples_2.11-2.4.0.jar` 是 Spark 提供的测试用例包，`SparkPi` 用于计算 Pi 值，执行结果如下：

![](image/Spark-local模式运行结果.png)

进入交互界面：
```
bin/spark
```
![](image/Spark-Local模式交互界面.png)

## 2.3、Standlone模式

Standlone模式的架构和Hadoop HDFS/YARN很类似的，Standalone 是 Spark 提供的一种内置的集群模式，采用内置的资源管理器进行管理。下面按照如图所示演示 1 个 Mater 和 2 个 Worker 节点的集群配置，这里使用两台主机进行演示：

![](image/spark-集群模式.png)

### 2.3.1、环境配置

首先需要保证 Spark 已经解压在两台主机的相同路径上。然后进入 hadoop001 的 `${SPARK_HOME}/conf/` 目录下，将`spark-env.sh.template`重命名为`spark-env.sh`，需要在配置文件`spark-env.sh`配置如下
```
JAVA_HOME=/usr/java/jdk1.8.0_201
```
完成后将该配置使用 scp 命令分发到 hadoop002 上

### 2.3.2、集群配置

在 `${SPARK_HOME}/conf/` 目录下，拷贝集群配置样本并进行相关配置：`slaves.template`重命名为`slaves`，在slaves中指定所有 Worker 节点的主机名：
```shell
# A Spark Worker will be started on each of the machines listed below.
hadoop001
hadoop002
```
这里需要注意以下三点：
- 主机名与 IP 地址的映射必须在 `/etc/hosts` 文件中已经配置，否则就直接使用 IP 地址；
- 每个主机名必须独占一行；
- Spark 的 Master 主机是通过 SSH 访问所有的 Worker 节点，所以需要预先配置免密登录；

### 2.3.3、启动standlone模式

```
start-all.sh   会在 hadoop1机器上启动master进程，在slaves文件配置的所有hostname的机器上启动worker进程
```
访问 8080 端口，查看 Spark 的 Web-UI 界面,，此时应该显示有两个有效的工作节点：

### 2.3.4、提交作业

```shell
# 以client模式提交到standalone集群 
spark-submit \
--class org.apache.spark.examples.SparkPi \
--master spark://hadoop001:7077 \
--executor-memory 2G \
--total-executor-cores 10 \
/usr/app/spark-2.4.0-bin-hadoop2.6/examples/jars/spark-examples_2.11-2.4.0.jar \
100

# 以cluster模式提交到standalone集群 
spark-submit \
--class org.apache.spark.examples.SparkPi \
--master spark://207.184.161.138:7077 \
--deploy-mode cluster \
--supervise \  # 配置此参数代表开启监督，如果主应用程序异常退出，则自动重启 Driver
--executor-memory 2G \
--total-executor-cores 10 \
/usr/app/spark-2.4.0-bin-hadoop2.6/examples/jars/spark-examples_2.11-2.4.0.jar \
100
```

### 2.3.5、进入交互界面

```
spark-shell  --master spark://bluefish:7077
```

### 2.3.6、资源问题

在虚拟机上提交作业时经常出现一个的问题是作业无法申请到足够的资源：

```properties
Initial job has not accepted any resources; 
check your cluster UI to ensure that workers are registered and have sufficient resources
```

可以查看 Web UI，内存空间不足：提交命令中要求作业的 `executor-memory` 是 2G，但是实际的工作节点的 `Memory` 只有 1G，这时候你可以修改 `--executor-memory`，也可以修改 Woker 的 `Memory`，其默认值为主机所有可用内存值减去 1G

关于 Master 和 Woker 节点的所有可选配置如下，可以在 `spark-env.sh` 中进行对应的配置：    

| Environment Variable（环境变量） | Meaning（含义）                                              |
| -------------------------------- | ------------------------------------------------------------ |
| `SPARK_MASTER_HOST`              | master 节点地址                                              |
| `SPARK_MASTER_PORT`              | master 节点地址端口（默认：7077）                            |
| `SPARK_MASTER_WEBUI_PORT`        | master 的 web UI 的端口（默认：8080）                        |
| `SPARK_MASTER_OPTS`              | 仅用于 master 的配置属性，格式是 "-Dx=y"（默认：none）,所有属性可以参考官方文档：[spark-standalone-mode](https://spark.apache.org/docs/latest/spark-standalone.html#spark-standalone-mode) |
| `SPARK_LOCAL_DIRS`               | spark 的临时存储的目录，用于暂存 map 的输出和持久化存储 RDDs。多个目录用逗号分隔 |
| `SPARK_WORKER_CORES`             | spark worker 节点可以使用 CPU Cores 的数量。（默认：全部可用）  |
| `SPARK_WORKER_MEMORY`            | spark worker 节点可以使用的内存数量（默认：全部的内存减去 1GB）； |
| `SPARK_WORKER_PORT`              | spark worker 节点的端口（默认： random（随机））              |
| `SPARK_WORKER_WEBUI_PORT`        | worker 的 web UI 的 Port（端口）（默认：8081）               |
| `SPARK_WORKER_DIR`               | worker 运行应用程序的目录，这个目录中包含日志和暂存空间（default：SPARK_HOME/work） |
| `SPARK_WORKER_OPTS`              | 仅用于 worker 的配置属性，格式是 "-Dx=y"（默认：none）。所有属性可以参考官方文档：[spark-standalone-mode](https://spark.apache.org/docs/latest/spark-standalone.html#spark-standalone-mode) |
| `SPARK_DAEMON_MEMORY`            | 分配给 spark master 和 worker 守护进程的内存。（默认： 1G）  |
| `SPARK_DAEMON_JAVA_OPTS`         | spark master 和 worker 守护进程的 JVM 选项，格式是 "-Dx=y"（默认：none） |
| `SPARK_PUBLIC_DNS`               | spark master 和 worker 的公开 DNS 名称。（默认：none）       |


## 2.4、YARN

Spark 支持将作业提交到 Yarn 上运行，此时不需要启动 Master 节点，也不需要启动 Worker 节点；建议在生产上使用该模式，统一使用YARN进行整个集群作业(MR、Spark)的资源调度

### 2.4.1、配置

在 `spark-env.sh` 中配置 hadoop 的配置目录的位置，可以使用 `YARN_CONF_DIR` 或 `HADOOP_CONF_DIR` 进行指定：

```properties
YARN_CONF_DIR=/usr/app/hadoop-2.6.0-cdh5.15.2/etc/hadoop
# JDK安装位置
JAVA_HOME=/usr/java/jdk1.8.0_201
```

### 2.4.2、启动

必须要保证 Hadoop 已经启动，这里包括 YARN 和 HDFS 都需要启动，因为在计算过程中 Spark 会使用 HDFS 存储临时文件，如果 HDFS 没有启动，则会抛出异常。
```shell
# start-yarn.sh
# start-dfs.sh
```

### 2.4.3、提交应用

```shell
#  以client模式提交到yarn集群 
spark-submit \
--class org.apache.spark.examples.SparkPi \
--master yarn \
--deploy-mode client \
--executor-memory 2G \
--num-executors 10 \
/usr/app/spark-2.4.0-bin-hadoop2.6/examples/jars/spark-examples_2.11-2.4.0.jar \
100

#  以cluster模式提交到yarn集群 
spark-submit \
--class org.apache.spark.examples.SparkPi \
--master yarn \
--deploy-mode cluster \
--executor-memory 2G \
--num-executors 10 \
/usr/app/spark-2.4.0-bin-hadoop2.6/examples/jars/spark-examples_2.11-2.4.0.jar \
100
```

## 2.4、Mesos

# 3、Spark运行架构

## 3.1、运行架构

Spark框架的核心是一个计算引擎，整体来说，它采用了标准的 master-slave的结构；如下图所示，其展示了一个Spark执行的基本结构

![](image/Spark-运行架构原理.png)

## 3.2、核心组件

### 3.2.1、Driver

Spark驱动器节点，用于执行spark任务中的main方法，复制实际代码的执行工作，Driver在Spark作业执行时主要负责：
- 将用户程序转化为作业（job）
- 在executor直接调度任务（task）
- 跟踪executor的执行情况
- 通过ui展示查询运行情况

### 3.2.2、Executor

Spark Executor 是集群中工作节点（Worker）中的一个JVM进程，负责在Spark作业中运行具体任务Task，任务彼此相互独立，Spark应用启动时，Executor节点被同时启动，伴随着整个Spark应用的生命周期而存在，如果有executor节点发生 了故障或崩溃，Spark应用可以继续执行，会将出错节点上的任务调度到其他executor节点上继续运行；

Executor主要有两个核心功能：
- 负责运行组成Spark应用的任务，并将结果返回给驱动器进程；
- 通过自身的块管理器（Block Manager）为用户程序中要求换成的RDD提供内存式存储；

### 3.2.3、Master & Worker

Spark集群的独立部署环境中，不需要依赖其他的资源调度框架，自身实现了资源调度的功能，所以环境中还有其他两个核心组件：Master 和 Worker，这里Master 是一个进程，主要负责资源的调度和分配，并进行集群的监控等职责；Worker也是一个进程，一个worker运行在集群中的一台服务器上，由Master分配资源对数据进行并行的处理和计算；

### 3.2.4、Application Master

Hadoop 用户向yarn集群提交应用程序时，提交的程序中应该包含 ApplicationMaster，用于向资源调度器申请执行任务的资源容器 container，运行用户自己的程序任务job，监控整个任务的执行，跟踪整个任务的状态，任务处理失败等情况，简而言之 RM 和 Driver 直接的解耦合靠的就是 ApplicationMaster

## 3.3、核心概念

### 3.3.1、Executor 与 Core

Spark Executor 是集群中运行在工作节点中的一个JVM进程，是整个集群中的专门用于计算的节点，在提交应用中，可以提供参数指定计算节点的个数，以及对应的资源。这里的资源一般指的是工作节点 Executor 的内存大小 和使用的虚拟CPU和（Core）数量；相关启动参数如下：

|名称|说明|
|---|---|
|--num-executors|配置Executor的数量|
|--executor-memory|配置每个Executor的内存大小|
|--executor-cores|配置每个Executor的虚拟CPU Core的数量|

### 3.3.2、DAG

DAG，有向无环图

## 3.4、提交流程

### 3.4.1、Yarn Client模式

Client模式将用于监控和调度的Driver模块在客户端执行，而不是在Yarn中，所以一般用于测试：
- Driver 在任务提交的本地机器上运行；
- Driver 启动后会和 ResourceManager 通讯申请启动 ApplicationMaster
- ResourceManager 分配 Container，在合适的 NodeManager上启动 ApplicationMaster，负责向 ResourceManager 申请Executor内存；
- ResourceManager 接到 ApplicationMaster 的资源申请后会分配 container，然后 ApplicationMaster 在资源分配指定的 NodeManager 上启动 Executor 进程；
- Executor进程启动会向Driver反向注册，Executor 全部注册完成后 Driver开始执行Main 函数
- 之后执行到 Action 算子时，触发一个 job，并根据宽依赖开始划分 stage，每个 stage生成对应的 TaskSet，之后将 task分发到各个Executor上执行；

### 3.4.2、Yarn Cluster 模式

Cluster 模式将用于监控和调度的Driver模块启动在Yarn集群资源中执行，一般用于生产环境；
- 在 Yarn Cluster 模式下，任务提交后会和 ResourceManager 通讯申请启动 ApplicationMaster;
- 随后 ResourceManager 分配 container，在合适的 NodeManager上启动了 ApplicationMaster，此时的 ApplicationMaster 就是 Driver；
- Driver 启动后向 ResourceManager 申请Executor内存，ResourceManager 接到 ApplicationMaster 的资源申请后会分配 container，然后在合适的 NodeManager上启动 Executor 进程；
- Executor进程启动后会向 Driver反向注册，Executor 全部注册完成后Driver开始执行 main 函数；
- 之后执行到Action 算子时，触发一个job，并根据宽依赖开始划分stage，每个stage生成对应的 taskset，之后框task分发到各个Executor上

# 4、SparkCore

Spark计算框架封装了三大数据结构，用于处理不同的应用场景：
- RDD（Resilient Distributed Dataset）：弹性分布式数据集
- 累加器：分布式共享只写变量
- 广播变量：分布式工作只读变量

## 4.1、RDD

### 4.1.1、什么是RDD

RDD（Resilient Distributed Dataset），称作弹性分布式数据集，是Spark中最基本的数据处理模型，代码中是一个抽象类，它代表一个弹性的、不可变的、可分区的、元素可并行计算的集合
- 弹性：
    - 存储的弹性：内存与磁盘的自动切换；
    - 容错的弹性：数据丢失可以自动恢复；
    - 计算的弹性：计算出错重试机制
    - 分片的弹性：可根据需要重新分片；
- 分布式：数据存储在大数据集群的不同节点上；
- 数据集：RDD封装了计算逻辑，并不保存数据；
- 数据抽象：RDD是一个抽象类，需要子类具体实现；
- 不可变：RDD封装了计算逻辑，是不可改变，如果向要改变，只能产生新的RDD
- 可分区、并行计算

### 4.1.2、核心属性

- 分区列表：RDD数据结构中存在分区列表，用于执行任务时并行计算，是实现分布式计算的重要属性
    ```scala
    protected def getPartitions: Array[Partition]
    ```
- 分区计算函数：Spark在计算时，是使用分区函数对每一个分区进行计算
    ```scala
    @DeveloperApi
    def compute(split: Partition, context: TaskContext): Iterator[T]
    ```
- RDD之间的依赖关系：RDD是计算模型的封装，当需求中需要将多个计算模型进行组合时，就需要将多个RDD建立依赖关系
- 分区器（可选）：当数据为KV类型数据时，可以通过设定分区器自定义数据的分区
- 首选位置（可选）：计算数据时，可以根据计算节点的状态选择不同的节点位置进行计算

Spark对RDD的操作整体可以分为两类：Transformation 和 Action
- Transformation 可以理解为转换，表示针对RDD中数据的转换操作，主要会针对已有的RDD创建一个新的RDD；
- Action 可以理解为执行，表示触发任务执行的操作，主要对RDD进行最后的操作，比如遍历、reduce、保存文件等，并且还可以把结果返回给Driver程序
- Transformation 算子有一个特性：lazy，指的是如果一个spark任务只定义了 transformation，那么即时执行中国任务，任务中的算子也不会执行，也就是说，transformation不会触发spark任务的执行，只是记录了对RDD所做的操作，不会执行；
- 只有当transformation之后，接着执行了一个action操作，那么所有的transformation才会执行，避免产生过多中间结果；

### 4.1.3、执行原理

数据处理过程需要计算资源和计算模型逻辑，执行时，需要将计算资源和计算模型逻辑进行协调和整合。

Spark框架在执行时，先申请资源，然后将应用程序的数据处理逻辑分解成一个一个的计算任务，然后将任务发到已经分配资源的计算节点上，按照指定的计算模型进行数据计算，最后得到结果；

在Yarn环境中 RDD的工作原理：RDD主要是用于将逻辑进行封装，并生成Task发送给Executor节点执行计算；

**RDD并行度与分区**

默认情况下，Spark可以将一个作业切分多个任务后，发送给Executor节点进行计算，而能够并行计算的任务数量称之为并行度，这个数量在构建RDD时指定；**注意：这里的并行执行的任务数量并不是指的切分任务的数量**

读取内存数据时，数据可以按照并行度的设定进行数据的分区操作，数据分区规则spark核心源码：
```
def positions(length: Long, numSlices: Int): Iterator[(Int, Int)] = {
    (0 until numSlices).iterator.map { i =>
    val start = ((i * length) / numSlices).toInt
    val end = (((i + 1) * length) / numSlices).toInt
    (start, end)
    }
}
```
读取文件数据时，数据是按照 hadoop文件读取的规则进行切片分区，而切片规则和数据读取的规则有些差异

### 4.1.4、RDD的创建

Spark创建RDD的方式可以分为四种：

**（1）从集合（内存）中创建RDD**

从集合中创建RDD，Spark主要提供了两个方法： parallelize 和 makeRDD
```scala
val rdd1 = sc.parallelize(List(1,2,3,4,5))
val rdd2 = sc.makeRDD(Array(1,2,3,4,5))
val sum1 = rdd1.reduce(_ + _)
val sum2 = rdd2.reduce(_ + _)
```
从代码实现上来看，makeRDD调用的是 parallelize方法：
```scala
def makeRDD[T: ClassTag](seq: Seq[T], numSlices: Int = defaultParallelism): RDD[T] = withScope {
    parallelize(seq, numSlices)
}
```

**（2）从外部存储文件创建RDD**

由外部存储系统的数据创建RDD包括：本地的文件系统、所有Hadoop支持的数据集，比如HDFS、HBase
```scala
val linesRdd = sc.textFile(path)
val wordRdd = linesRdd.flatMap(_.split(" "))
val wordCountMap = wordRdd.map((_, 1))
wordCountMap.reduceByKey(_ + _).foreach(word => println(word._1 + " -> " + word._2))
```
通过SparkContext的 textFile() 方法，可以针对本地文件或HDFS创建RDD，RDD个每个元素就是文件中的一行文本内容，textFile 方法支持对穆、压缩文件以及通配符创建RDD；

Spark默认会为HDFS文件的每一个Block创建一个partition，也可以通过 textFile() 第二个参数手动设置分区数量，只能比Block数量多，不能比Block数量少，比Block数量少的设置不生效；

**（3）从其他RDD创建**

主要通过一个RDD进行算子运算之后，再产生新的RDD

**（4）直接创建RDD（new）**

使用new 的方式直接构造RDD，一般由Spark框架自身使用

### 4.1.5、RDD转换算子:Transformation

[RDD转换算子](http://spark.apache.org/docs/3.0.0/rdd-programming-guide.html#transformations)

spark 常用的 Transformation 算子如下表：

| Transformation 算子                                           | Meaning（含义）                                              |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| **map**(*func*)                                              | 对原 RDD 中每个元素运用 *func* 函数，并生成新的 RDD             |
| **filter**(*func*)                                           | 对原 RDD 中每个元素使用*func* 函数进行过滤，并生成新的 RDD      |
| **flatMap**(*func*)                                          | 与 map 类似，但是每一个输入的 item 被映射成 0 个或多个输出的 items（ *func* 返回类型需要为 Seq ）。 |
| **mapPartitions**(*func*)                                    | 与 map 类似，但函数单独在 RDD 的每个分区上运行， *func*函数的类型为  Iterator\<T> => Iterator\<U> ，其中 T 是 RDD 的类型，即 RDD[T] |
| **mapPartitionsWithIndex**(*func*)                           | 与 mapPartitions 类似，但 *func* 类型为 (Int, Iterator\<T>) => Iterator\<U> ，其中第一个参数为分区索引 |
| **sample**(*withReplacement*, *fraction*, *seed*)            | 数据采样，有三个可选参数：设置是否放回（withReplacement）、采样的百分比（*fraction*）、随机数生成器的种子（seed）； |
| **union**(*otherDataset*)                                    | 合并两个 RDD                                                  |
| **intersection**(*otherDataset*)                             | 求两个 RDD 的交集                                              |
| **distinct**([*numTasks*]))                                  | 去重                                                         |
| **groupByKey**([*numTasks*])                                 | 按照 key 值进行分区，即在一个 (K, V) 对的 dataset 上调用时，返回一个 (K, Iterable\<V>) <br/>**Note:** 如果分组是为了在每一个 key 上执行聚合操作（例如，sum 或 average)，此时使用 `reduceByKey` 或 `aggregateByKey` 性能会更好<br>**Note:** 默认情况下，并行度取决于父 RDD 的分区数。可以传入 `numTasks` 参数进行修改。 |
| **reduceByKey**(*func*, [*numTasks*])                        | 按照 key 值进行分组，并对分组后的数据执行归约操作。            |
| **aggregateByKey**(*zeroValue*,*numPartitions*)(*seqOp*, *combOp*, [*numTasks*]) | 当调用（K，V）对的数据集时，返回（K，U）对的数据集，其中使用给定的组合函数和 zeroValue 聚合每个键的值。与 groupByKey 类似，reduce 任务的数量可通过第二个参数进行配置。 |
| **sortByKey**([*ascending*], [*numTasks*])                   | 按照 key 进行排序，其中的 key 需要实现 Ordered 特质，即可比较      |
| **join**(*otherDataset*, [*numTasks*])                       | 在一个 (K, V) 和 (K, W) 类型的 dataset 上调用时，返回一个 (K, (V, W)) pairs 的 dataset，等价于内连接操作。如果想要执行外连接，可以使用 `leftOuterJoin`, `rightOuterJoin` 和 `fullOuterJoin` 等算子。 |
| **cogroup**(*otherDataset*, [*numTasks*])                    | 在一个 (K, V) 对的 dataset 上调用时，返回一个 (K, (Iterable\<V>, Iterable\<W>)) tuples 的 dataset。 |
| **cartesian**(*otherDataset*)                                | 在一个 T 和 U 类型的 dataset 上调用时，返回一个 (T, U) 类型的 dataset（即笛卡尔积）。 |
| **coalesce**(*numPartitions*)                                | 将 RDD 中的分区数减少为 numPartitions。                         |
| **repartition**(*numPartitions*)                             | 随机重新调整 RDD 中的数据以创建更多或更少的分区，并在它们之间进行平衡。 |
| **repartitionAndSortWithinPartitions**(*partitioner*)        | 根据给定的 partitioner（分区器）对 RDD 进行重新分区，并对分区中的数据按照 key 值进行排序。这比调用 `repartition` 然后再 sorting（排序）效率更高，因为它可以将排序过程推送到 shuffle 操作所在的机器。 |

#### 4.1.5.1、map

将处理的数据逐条进行映射转换，这里的转换可以是类型的转换，也可以是值的转换
```scala
val rdd = sc.parallelize(Array(1,2,3,4,5))
val mapRdd = rdd.map(_ * 2)
mapRdd.foreach(println(_))
```

#### 4.1.5.2、mapPartitions

将待处理的数据以分区为单位发生到计算节点进行处理，这里的处理指的是可以任意进行的处理，哪怕是过滤数据
```scala
val list = List(1,2,3,4,5,6)
sc.parallelize(list, 3).mapPartitions(iterator =>{
    var buffer = new ListBuffer[Int]
    while (iterator.hasNext) {
    buffer.append(iterator.next() * 100)
    }
    buffer.toIterator
}).foreach(println(_))
``` 
**map与mapPartitions区别：**
- 数据处理角度：map算子时分区内的一个数据一个数据的执行，类似与串行操作；mapPartitions算子是以分区为单位进行批处理操作；
- 功能的角度：map算子主要目的是将数据源的数据进行转换和改变，但是不会减少或增多数据；mapPartitions算子需要传递一个迭代器，返回一个迭代器，没有要求的元素个数保持不变，所以可以增加或减少数据；
- 性能的角度：map算子类似于串行操作，性能较低；mapPartitions算子类似于批处理，性能较高。但是mapPartitions算子会长时间占用内存，这样会导致内存可能不够用，出现内存溢出的情况

**mapPartitionsWithIndex 算子**

将待处理的数据以分区为单位发生到计算节点进行吹，处理时了可以进行任意的处理，在处理的同时可以获取当前分区索引；与 mapPartitions 类似，但 *func* 类型为 `(Int, Iterator<T>) => Iterator<U>` ，其中第一个参数为分区索引。
```scala
val list = List(1, 2, 3, 4, 5, 6)
sc.parallelize(list, 3).mapPartitions(iterator => {
    var buffer = new ListBuffer[Int]
    while (iterator.hasNext) {
    buffer.append(iterator.next() * 100)
    }
    buffer.toIterator
}).foreach(println(_))
```

#### 4.1.5.3、flatMap

将处理的数据进行扁平化后在进行映射处理，
```scala
val list = List("hello scala", "hello spark")
sc.parallelize(list).flatMap(_.split(" ")).foreach(println(_))
// 输出结果
hello
hello
scala
spark
```

#### 4.1.5.4、groupBy

将数据根据指定的规则进行分组，分组默认不变，但是数据会被打乱重新组合，将这样的操作称为shuffle，极限情况下，数据可能被分在同一个分区中；

一个组的数据在一个分区中，但是并不是说一个分区中只有一个组
```scala
val dataRDD = sc.parallelize(Array((150001, "US","male"), (150002, "CN","female"), (150003, "CN","male"), (150004, "IN","female")))
dataRDD.groupBy(_._1 % 2).foreach(println(_))
```

**groupByKey**

将数据源的数据根据key对value进行分组
```scala
val dataRDD = context.parallelize(Array((150001, "US", "male"), (150002, "CN", "female"), (150003, "CN", "male"), (150004, "IN", "female")))
dataRDD.map(tup => (tup._2, (tup._1, tup._3))).groupByKey().foreach(tup => {
    val area = tup._1
    print(area + ":")
    val it = tup._2
    for (uid <- it) {
        print("<" + uid._1 + "," + uid._2 + "> ")
    }
    println()
})
```

#### 4.1.5.5、filter

将数据根据指定的规则进行筛选过滤，符合规则的数据保留，不符合规则的数据丢弃，当数据进行过筛选过滤后，分区不变，但是分区内的数据可能不均衡，可能会出现数据倾斜
```scala
val rdd = sc.parallelize(Array(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
// 找出数据中所有的偶数
rdd.filter(_ % 2 == 0).foreach(println(_))
```

#### 4.1.5.6、sample

根据指定的规则从数据集中抽取数据，有三个可选参数：设置是否放回 (withReplacement)、采样的百分比 (fraction)、随机数生成器的种子 (seed) ：
```scala
val dataRDD = sparkContext.makeRDD(List(1,2,3,4),1)
// 抽取数据不放回(伯努利算法)
// 伯努利算法:又叫 0、1 分布。例如扔硬币，要么正面，要么反面。
// 具体实现:根据种子和随机算法算出一个数和第二个参数设置几率比较，小于第二个参数要，大于不 要
// 第一个参数:抽取的数据是否放回，false:不放回
// 第二个参数:抽取的几率，范围在[0,1]之间,0:全不取;1:全取;
// 第三个参数:随机数种子
val dataRDD1 = dataRDD.sample(false, 0.5)
// 抽取数据放回(泊松算法)
// 第一个参数:抽取的数据是否放回，true:放回;false:不放回
// 第二个参数:重复数据的几率，范围大于等于 0.表示每一个元素被期望抽取到的次数
// 第三个参数:随机数种子
val dataRDD2 = dataRDD.sample(true, 2)
```
使用场景：比如抽奖

#### 4.1.5.7、distinct

将数据集中重复的数据去重
```scala
val dataRDD = sc.parallelize(Array((150001, "US"), (150002, "CN"), (150003, "CN"), (150004, "IN")))
dataRDD.map(_._2).distinct().foreach(println(_))
```

#### 4.1.5.8、sortBy

该操作用于排序数据，在排序之前，可以将数据通过f函数进行处理，之后按照f函数处理的结果进行排序，默认为升序排序。排序新产生的RDD分区数与原RDD分区数一致，中间存在shuffle过错
```scala
val dataRDD = sc.parallelize(Array((150005, 400), (150002, 200), (150003, 300), (150004, 100)))
// 对于上述数据，需要按照 _.2 排序，如果用 sortByKey 需要调换下顺序
dataRDD.map(tup => (tup._2, tup._1)).sortByKey().foreach(println(_))
// 使用sortBy 直接指定需要排序的字段
dataRDD.sortBy(_._2).foreach(println)
```
如果在本地运行时 SparkConf设置`setMaster("local[2]")`，上述代码运行得不到正确的顺序，需要在sortBy或sortByKey中指定参数
```scala
val dataRDD = sc.parallelize(Array((150005, 400), (150002, 200), (150003, 300), (150004, 100)))
// 指定 numPartitions
dataRDD.map(tup => (tup._2, tup._1)).sortByKey(numPartitions = 1).foreach(println(_))
dataRDD.sortBy(_._2, numPartitions = 1).foreach(println)
```

如何实现二次排序：
```scala
object SecondarySortDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("SecondarySortDemo").setMaster("local[1]")
    val sc = new SparkContext(conf)

    val rdd = sc.textFile("data/second.txt")

    val pair = rdd.map(line => {
      val arr = line.split(" ")
      (new SecondarySort(arr(0).toInt, arr(1).toInt), line)
    })
    pair.sortByKey(ascending = true).map(_._1).foreach(se => println(se.first + "->" + se.second))
    sc.stop()
  }
  // 使用Scala创建自定义二次排序Key的时候，需要实现Ordered以及Serializable接口
  class SecondarySort(val first: Int, val second: Int) extends Ordered[SecondarySort] with Serializable {
    override def compare(that: SecondarySort): Int = {
      if (this.first - that.first != 0) {
        this.first - that.first
      } else {
        that.second - this.second
      }
    }
  }
}
```

#### 4.1.5.9、交集、并集、差集、拉链

**交集：intersection**

对源RDD 和参数RDD 求交集后返回一个新的RDD
```scala
val rdd1 = context.makeRDD(Array(1, 2, 3, 4, 5, 6))
val rdd2 = context.makeRDD(Array(3, 2, 6, 7))
rdd1.intersection(rdd2).foreach(println)// 3 6 2
```
注意：两个RDD的类型需要一致，如果不一致的话，可以将其中一个RDD的通过map转成同源rdd类型一致

**并集：union**

对源RDD和参数RDD求并集后返回一个新的RDD
```scala
val rdd1 = context.makeRDD(Array(1, 2, 3, 4, 5, 6))
val rdd2 = context.makeRDD(Array(3, 2, 6, 7, 8))
rdd1.union(rdd2).foreach(println)
```

**差集：subtract**

以一个RDD元素为主，去除两个RDD中重复的元素，将其他元素保留下来
```scala
val rdd1 = context.makeRDD(Array(1, 2, 3, 4, 5, 6))
val rdd2 = context.makeRDD(Array(3, 2, 6, 7, 8))
// 将rdd1与rdd2中的重复元素去掉
rdd1.subtract(rdd2).foreach(println)
```

**拉链：zip**

将两个RDD中的元素，以键值对的形式进行合并，其中，键值对中的key为第一个RDD中的元素，value为第2个RDD中的相同位置的元素
```scala
val rdd1 = context.makeRDD(Array(1, 2, 3, 4, 5, 6))
val rdd2 = context.makeRDD(Array(3, 2, 6, 7, 8, 9))
rdd1.zip(rdd2).foreach(println)
```
注意：两个RDD的数量必须一致，否则会抛出异常：
```
org.apache.spark.SparkException: Can only zip RDDs with same number of elements in each partition
```

#### 4.1.5.10、reduceByKey

将数据按照相同的ky对value进行聚合
```scala
val dataRDD = context.parallelize(Array((150001, "US"), (150002, "CN"), (150003, "CN"), (150004, "IN")))
dataRDD.map(tup => (tup._2, 1)).reduceByKey(_ + _).foreach(println)
```

**reduceByKey 与 groupByKey区别：**
- 从shuffle角度：reduceByKey 和 groupByKey 都存在shuffle的操作，但是 reduceByKey 可以在shuffle前对分区内相同key的数据进行预聚合（combine）功能，这样会减少落盘的数据量，而 groupByKey 只是进行分组，不存在数据量减少的问题；
- 从功能角度：reduceByKey 其实包含分组和聚合的功能；groupByKey只是分组，不能聚合；

#### 4.1.5.11、aggregateByKey

将数据根据不同的规则进行分区内计算和分区间计算。当调用（K，V）对的数据集时，返回（K，U）对的数据集，其中使用给定的组合函数和 zeroValue 聚合每个键的值。与 `groupByKey` 类似，reduce 任务的数量可通过第二个参数 `numPartitions` 进行配置
```scala
// 在分区内取得相同key的最大值，在分区间相同的key进行求和操作
val rdd = context.makeRDD(List(("a", 1), ("a", 2), ("a", 3), ("a", 4)), 2)
// (a,【1,2】), (a, 【3，4】)
// (a, 2), (a, 4)
// (a, 6)
// aggregateByKey 存在函数柯里化，有两个参数列表
// 第一个参数列表,需要传递一个参数，表示为初始值
//       主要用于当碰见第一个key的时候，和value进行分区内计算
// 第二个参数列表需要传递2个参数
//      第一个参数表示分区内计算规则
//      第二个参数表示分区间计算规则
rdd.aggregateByKey(0)(
    (x, y) => math.max(x, y),
    (x, y) => x + y
).foreach(println(_))
```

**foldByKey**

当分区内计算规则和分区间计算规则相同时，aggregateByKey 就可以简化为 foldByKey

#### 4.1.5.12、combineByKey

最通用的对 key-value 型RDD 进行聚集操作的聚集函数，类似aggregate，combineBykey允许用户返回值的类型与输入不一致
```scala
val list: List[(String, Int)] = List(("a", 88), ("b", 95), ("a", 91), ("b", 93), ("a", 95), ("b", 98))
val rdd = context.makeRDD(list, 2)
// combineByKey : 方法需要三个参数
// 第一个参数表示：将相同key的第一个数据进行结构的转换，实现操作
// 第二个参数表示：分区内的计算规则
// 第三个参数表示：分区间的计算规则
rdd.combineByKey(
    (_, 1),
    (acc: (Int, Int), v) => (acc._1 + v, acc._2 + v),
    (acc1: (Int, Int), acc2: (Int, Int)) => (acc1._1 + acc2._1, acc1._2 + acc2._2)
).foreach(println(_))
```

**reduceByKey、foldByKey、aggregateByKey、combineBykey区别：**
- reduceByKey：相同的key的第一个数据不进行任何计算，分区内和分区间计算规则相同；
- foleByKey：相同的key第一个数据和初始值进行分区内计算，分区内和分区间计算规则相同；
- aggregateByKey：相同key的第一个数据和初始值进行分区内计算，分区内和分区间计算规则可以不相同；
- combineByKey：当计算时，发现数据结构不满足要求时，可以让第一个数据转换结构，分区内和分区间计算规则不相同；

#### 4.1.5.13、sortByKey

- sortByKey：在一个 (k,v) 的RDD上调用， K必须实现 Ordered接口，返回一个按照key进行排序的，
- sortBy：可以指定 key 进行排序
```scala
val rdd = context.makeRDD(List((100, "hadoop"), (90, "spark"), (120, "storm")))
// 指定按照tuple中第二个元素排序
rdd.sortBy(_._2, ascending = false).foreach(println)
// 默认是按照 tuple 中第一个元素排序
rdd.sortByKey(ascending = false).foreach(println)
```

#### 4.1.5.14、join

在类型为(k,v) 和 (k,w)的RDD上调用，返回一个相同key对应的所有的元素连接在一起的 (k,(v,w)) 的RDD
```scala
val rdd1 = context.makeRDD(List(("hadoop", 100), ("spark", 90), ("storm", 120)))
val rdd2 = context.makeRDD(List(("hadoop", 200), ("spark", 30), ("Flink", 300)))
rdd1.join(rdd2).foreach(println)
// 输出结果
(spark,(90,30))
(hadoop,(100,200))
```
如果key存在不相等的情况，会将这两个不相等的key丢弃

**leftOuterJoin：**类似sql语句中的左外连接
```scala
val rdd1 = context.makeRDD(List(("hadoop", 100), ("spark", 90), ("storm", 120)))
val rdd2 = context.makeRDD(List(("hadoop", 200), ("spark", 30), ("Flink", 300)))
rdd1.leftOuterJoin(rdd2).foreach(println)
// 输出结果
(spark,(90,Some(30)))
(hadoop,(100,Some(200)))
(storm,(120,None))
```
同理 rightOuterJoin 类似sql中的右外连接

### 4.1.6、RDD算子：Action

Spark 常用的 Action 算子如下：

| Action（动作）                                     | Meaning（含义）                                              |
| -------------------------------------------------- | ------------------------------------------------------------ |
| **reduce**(*func*)                                 | 使用函数*func*执行归约操作                                   |
| **collect**()                                      | 以一个 array 数组的形式返回 dataset 的所有元素，适用于小结果集。 |
| **count**()                                        | 返回 dataset 中元素的个数。                                  |
| **first**()                                        | 返回 dataset 中的第一个元素，等价于 take(1)。                |
| **take**(*n*)                                      | 将数据集中的前 *n* 个元素作为一个 array 数组返回。           |
| **takeSample**(*withReplacement*, *num*, [*seed*]) | 对一个 dataset 进行随机抽样                                  |
| **takeOrdered**(*n*, *[ordering]*)                 | 按自然顺序（natural order）或自定义比较器（custom comparator）排序后返回前 *n* 个元素。只适用于小结果集，因为所有数据都会被加载到驱动程序的内存中进行排序。 |
| **saveAsTextFile**(*path*)                         | 将 dataset 中的元素以文本文件的形式写入本地文件系统、HDFS 或其它 Hadoop 支持的文件系统中。Spark 将对每个元素调用 toString 方法，将元素转换为文本文件中的一行记录。 |
| **saveAsSequenceFile**(*path*)                     | 将 dataset 中的元素以 Hadoop SequenceFile 的形式写入到本地文件系统、HDFS 或其它 Hadoop 支持的文件系统中。该操作要求 RDD 中的元素需要实现 Hadoop 的 Writable 接口。对于 Scala 语言而言，它可以将 Spark 中的基本数据类型自动隐式转换为对应 Writable 类型。(目前仅支持 Java and Scala) |
| **saveAsObjectFile**(*path*)                       | 使用 Java 序列化后存储，可以使用 `SparkContext.objectFile()` 进行加载。(目前仅支持 Java and Scala) |
| **countByKey**()                                   | 计算每个键出现的次数。                                       |
| **foreach**(*func*)                                | 遍历 RDD 中每个元素，并对其执行*fun*函数                       |

#### 4.1.6.1、reduce

聚焦RDD中的所有元素，先聚合分区内的数据，再聚合分区间的数据
```scala
val rdd = sc.makeRDD(List(1,2,3,4,5))
rdd.reduce(_ + _)
```

#### 4.1.6.2、collect

在Driver中，以数组Array的形式返回数据集的所有元素，collect返回的是一个Array数组
```scala
val dataRDD = sc.parallelize(Array(1, 2, 3, 4, 5))
//collect返回的是一个Array数组
//注意：如果RDD中数据量过大，不建议使用collect，因为最终的数据会返回给Driver进程所在的节点
// 如果想要获取几条数据，查看一下数据格式，可以使用take(n)
val res = dataRDD.collect()
for (item <- res) {
    println(item)
}
```

#### 4.1.6.3、save相关算子

```scala
rdd.saveAsObjectFile()
// 指定HDFS的路径信息即可，需要指定一个不存在的目录
rdd.saveAsTextFile()
```

#### 4.1.6.4、foreach

分布式遍历RDD中每一个元素，foreach内部可以传入一个函数

### 4.1.7、RDD 持久化

**RDD持久化原理**

当对RDD执行持久化操作时，每个节点都会将自己操作的RDD的partition数据持久化到内存中，并且在之后对该RDD反复使用，直接使用内存中的缓存的partition数据；针对一个RDD反复执行多个操作的场景，就只需要对RDD计算一次即可，而不需要反复计算多次该RDD；对于迭代式算法和快速交互式应用，RDD持久化，对性能提升有很大帮助

**如何持久化RDD**

如果要持久化一个RDD，只需要调用它的cache() 和 persist() 方法即可，在该RDD第一次被计算出来时，就会直接缓存在每一个节点中，而且spark的持久化机制是自动容错的，如果持久化的RDD的任何partition 数据丢失了，那么spark会自动通过其源RDD，使用 transformation算子重新计算该partition的数据；

cache() 和 persist()区别在于：cache 是persist 的一种简化方式，cache的底层就是调用的 persist 的无参斑斑，也就是persist(MEMORY_ONLY)，将数据持久化到内存中，如果需要从内存中清除缓存，那么可以使用 unpersist() 方法

**RDD缓存策略**

Spark 支持多种缓存级别 ：

| Storage Level<br/>（存储级别）                 | Meaning（含义）                                              |
| ---------------------------------------------- | ------------------------------------------------------------ |
| `MEMORY_ONLY`                                  | 默认的缓存级别，将 RDD 以反序列化的 Java 对象的形式存储在 JVM 中。如果内存空间不够，则部分分区数据将不再缓存。 |
| `MEMORY_AND_DISK`                              | 将 RDD 以反序列化的 Java 对象的形式存储 JVM 中。如果内存空间不够，将未缓存的分区数据存储到磁盘，在需要使用这些分区时从磁盘读取。 |
| `MEMORY_ONLY_SER`<br/>     | 将 RDD 以序列化的 Java 对象的形式进行存储（每个分区为一个 byte 数组）。这种方式比反序列化对象节省存储空间，但在读取时会增加 CPU 的计算负担。仅支持 Java 和 Scala 。  |
| `MEMORY_AND_DISK_SER`<br/> | 类似于 `MEMORY_ONLY_SER`，但是溢出的分区数据会存储到磁盘，而不是在用到它们时重新计算。仅支持 Java 和 Scala。 |
| `DISK_ONLY`                                    | 只在磁盘上缓存 RDD                                            |
| `MEMORY_ONLY_2`, <br/>`MEMORY_AND_DISK_2`, etc | 与上面的对应级别功能相同，但是会为每个分区在集群中的两个节点上建立副本。如果数据丢失，不需要重新计算，只需要使用备份数据 |
| `OFF_HEAP`                                     | 与 `MEMORY_ONLY_SER` 类似，但将数据存储在堆外内存中。这需要启用堆外内存。 |

> 启动堆外内存需要配置两个参数：
>
> + **spark.memory.offHeap.enabled** ：是否开启堆外内存，默认值为 false，需要设置为 true；
> + **spark.memory.offHeap.size** : 堆外内存空间的大小，默认值为 0，需要设置为正值。

**如何选择RDD持久化策略：**

Spark提供了多种持久化级别，主要是为了在CPU和内存消耗之间进行取舍：
- 优先使用 memory_only，纯内存速度最快，而且没有序列化不需要消耗CPU进行反序列操作；缺点就是比较消耗内存；
- memory_only_ser 将数据进行序列化存储，纯内存操作还是非常快的，但是使用的时候需要消耗CPU进行反序列化；

> 注意：
> 如果需要进行数据的快速失败恢复，那么就选择带后缀为 _2 的策略，进行数据的备份，这样在失败时，就不需要重新计算了；
> 能不使用 DISK 相关的策略，就不要使用，因为有的时候从磁盘读取数据，还不如重新计算一次；

**示例：**
```scala
val conf = new SparkConf()
    conf.setAppName("CreateRddByArray").setMaster("local")
    val sc = new SparkContext(conf)

    val path = ""

    val rdd = sc.textFile(path).cache()
    var start = System.currentTimeMillis()
    println(rdd.count())
    var end = System.currentTimeMillis()
    println("第一次耗时：" + (end - start))

    start = System.currentTimeMillis()
    println(rdd.count())
    end = System.currentTimeMillis()
    println("第二次耗时：" + (end - start))
}
```

### 4.1.8、RDD序列化

从计算的角度，算子以外的代码都是在Driver端执行的，算子里面的代码都是在Executor端执行，那么在scala的函数式编程中，就会导致算子内经常用到算子外的数据，这样就形成了闭包的效果，如果使用的算子外的数据无法序列化，就意味着无法传值给Executor端执行，就会发生错误，所以在执行任务计算前，检测闭包内的对象是否可以进行序列化，这个操作称为闭包检测；

Spark2.0开始，处于性能考虑，从2.0开始之初另外一种Kryo序列化机制。当RDD在shuffle数据的时候，简单数据类型、数组和字符串类型以及在spark内部使用 kryo来序列化

## 4.2、共享变量

### 4.2.1、概述

默认情况下，如果一个算子函数中使用到了某个外部的变量，那么这个变量的值会被拷贝到每个task中，此时每个task只能操作自己的那份变量数据。如果多个task想要共享某个变量，普通的方式是做不到的；

Spark为此提供了两种共享变量：
- 广播变量：broadcase variable
- 累加变量：accumulator

### 4.2.2、广播变量

broadcast variable 会将使用到的变量，仅仅为每个节点拷贝一份，而不会为每个task都拷贝一份副本，其最大的作用：减少了变量到各个节点的网络传输消耗，以及在节点上的内存消耗；

通过调用SparkContext的broadcast方法，针对某个变量创建广播变量；然后在算子函数内，使用到广播变量时，每个节点只会拷贝一份副本，可以使用广播变量的value()方法获取值

> 注意：广播变量是只读的

使用广播变量：
```scala
def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("CreateRddByArray").setMaster("local")
    val sc = new SparkContext(conf)
    val rdd = sc.parallelize(List(1, 2, 3, 4, 5))
    var variable = 2
    // 不使用广播变量
    //    rdd.map(_ * variable)
    // 使用广播变量
    var br = sc.broadcast(variable)
    rdd.map(_ * br.value).foreach(println)
    sc.stop()
}
```

### 4.2.3、累加变量

Spark提供的accumulator，主要用于多个节点对一个变量进行共享性的操作

累加器用来把Executor端变量信息聚合到Driver端，在Driver程序中定义的变量在Executor端的每个task都会得到这个变量的一份新的副本，每个task更新这些副本的值后，传回Driver端进行merge；

> 注意：accumulator只提供了累加的功能，在task只能对 accumulator进行累加操作，不能读取它的值，只有在Drive进程中才可以读取accumulator的值；

案例：
```scala
def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("AccumulatorOpScala").setMaster("local")
    val sc = new SparkContext(conf)
    val dataRDD = sc.parallelize(Array(1,2,3,4,5))
    //这种写法是错误的，因为foreach代码是在worker节点上执行的: var total = 0 和 println(total) 是在Driver进程中执行的
    // 所以无法实现累加操作, 并且foreach算子可能会在多个task中执行，这样foreach内部实现的累加也不是最终全局累加的结果
    /*var total = 0
    dataRDD.foreach(num=>total += num)
    println(total)*/

    //所以此时想要实现累加操作就需要使用累加变量了
    //1：定义累加变量
    val sumAccumulator = sc.longAccumulator

    //2：使用累加变量
    dataRDD.foreach(num=>sumAccumulator.add(num))

    // 只能在Driver进程中获取累加变量的结果，如果在 worker进程中获取累加变量的结果，不是最终的数值
    println(sumAccumulator.value)
}
```

**自定义累加器**

```scala
object CustomAccumulatorDemo {
  def main(args: Array[String]): Unit = {
    val sparConf = new SparkConf().setMaster("local").setAppName("Acc")
    val sc = new SparkContext(sparConf)
    val rdd = sc.makeRDD(List("hello", "spark", "hello"))
    // 累加器 : WordCount
    // 创建累加器对象
    val wcAcc = new MyAccumulator()
    // 向Spark进行注册
    sc.register(wcAcc, "wordCountAcc")
    rdd.foreach(word => {wcAcc.add(word)}) // 数据的累加（使用累加器）
    // 获取累加器累加的结果
    println(wcAcc.value)
    sc.stop()
  }
  /*
      自定义数据累加器：WordCount
      1. 继承AccumulatorV2, 定义泛型
         IN : 累加器输入的数据类型 String
         OUT : 累加器返回的数据类型 mutable.Map[String, Long]
      2. 重写方法（6）
     */
  class MyAccumulator extends AccumulatorV2[String, mutable.Map[String, Long]] {
    private var wcMap = mutable.Map[String, Long]()
    // 判断是否初始状态
    override def isZero: Boolean = {
      wcMap.isEmpty
    }
    override def copy(): AccumulatorV2[String, mutable.Map[String, Long]] = {
      new MyAccumulator()
    }
    override def reset(): Unit = {
      wcMap.clear()
    }
    // 获取累加器需要计算的值
    override def add(word: String): Unit = {
      val newCnt = wcMap.getOrElse(word, 0L) + 1
      wcMap.update(word, newCnt)
    }
    // Driver合并多个累加器
    override def merge(other: AccumulatorV2[String, mutable.Map[String, Long]]): Unit = {
      val map1 = this.wcMap
      val map2 = other.value
      map2.foreach {
        case (word, count) => {
          val newCount = map1.getOrElse(word, 0L) + count
          map1.update(word, newCount)
        }
      }
    }
    // 累加器结果
    override def value: mutable.Map[String, Long] = {
      wcMap
    }
  }
}
```

## 4.3、宽窄依赖问题

## 4.4、shuffle介绍

# 5、SparkSQL

## 5.1、SqlContext

```java
public static void main(String[] args) {
    String path = "file:///workspace/scala/spark-sql-demo/people.json";
    //1)创建相应的Context
    SparkConf conf = new SparkConf();
    //在测试或者生产中，AppName和Master我们是通过脚本进行指定
    conf.setAppName("SQLContextApp").setMaster("local[2]");
    SparkContext sc = new SparkContext(conf);
    SQLContext sqlContext = new SQLContext(sc);
    Dataset<Row> json = sqlContext.read().json(path);
    json.printSchema();
    json.show();
    sc.stop();
}
```
如果打包到服务器上运行：
```
spark-submit \
--name SQLContextApp \
--class com.blue.fish.spark.SQLContextApp \
--master local[2] \
/root/lib/spark-sql.jar \
/root/software/spark-2.4.7/examples/src/main/resources/people.json
```

## 5.2、HiveContext

```java
public static void main(String[] args) {
    //1)创建相应的Context
    SparkConf conf = new SparkConf();
    SparkContext sc = new SparkContext(conf);
    // hiveContext 是过期API
    HiveContext hc = new HiveContext(sc);
    hc.table("emp").show();
    sc.stop();
}
```
这个需要部署打包到服务器上运行：
```
spark-submit \
--name HiveContextApp \
--class com.blue.fish.spark.HiveContextApp \
--master local[2] \
--jars /root/spark-log/mysql-connector-java-5.1.28.jar \
/root/lib/spark-sql.jar
```
- `--class`：指定运行的类
- `--master`：指定spark的启动模式
- `--jars`：指定外部依赖的类，这里需要依赖mysql驱动，因为hive的元数据信息存储在mysql里的

## 5.3、SparkSession

在Spark2.0之后，主要使用SparkSession来处理了
```java
public static void main(String[] args) {
    String path = "file:///workspace/scala/spark-sql-demo/people.json";
    SparkSession session = SparkSession.builder().appName("JavaSparkSessionContext")
            .master("local[2]").getOrCreate();
    Dataset<Row> dataset = session.read().json(path);
    dataset.printSchema();
    dataset.show();
    session.stop();
}
```

## 5.4、spark-shell方式使用

spark-shell 要访问hive里面的表需要将hive-site.xml 拷贝到spark的conf目录下

进入spark-shell控制台访问hive的表
```
[root@bluefish lib]# spark-shell --master local[2] --jars /root/spark-log/mysql-connector-java-5.1.28.jar 
Spark context available as 'sc' (master = local[2], app id = local-1606538300252).
Spark session available as 'spark'.
Welcome to
      ____              __
     / __/__  ___ _____/ /__
    _\ \/ _ \/ _ `/ __/  '_/
   /___/ .__/\_,_/_/ /_/\_\   version 2.4.6
      /_/
Using Scala version 2.11.12 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_271)
Type in expressions to have them evaluated.
Type :help for more information.

scala> 
```
因为hive的元数据信息存储在mysql中，需要知道mysql的驱动包地址

比如查询表，执行查询等：
```
scala> spark.sql("show tables").show
20/11/28 12:39:28 WARN ObjectStore: Failed to get database global_temp, returning NoSuchObjectException
+--------+--------------+-----------+
|database|     tableName|isTemporary|
+--------+--------------+-----------+
| default|          dept|      false|
| default|           emp|      false|
| default|hive_wordcount|      false|
+--------+--------------+-----------+
scala> spark.sql("select * from dept").show
+---+------+----------+--------+
| id|deptno|     dname|     loc|
+---+------+----------+--------+
|  1|    11|KOMvRWiYwm|TGBPuCDj|
|  2|    12|nKNLpqLGXT|AWjcLvYD|
|  3|    13|WXZfDzOvMx|ZIrLDIFd|
+---+------+----------+--------+
only showing top 20 rows
```

还可以通过spark—sql命令进入交互界面：

## 5.5、thriftserver

thriftserver/beeline的使用
- 启动thriftserver: 默认端口是10000 ，可以修改：
    ```
    ./start-thriftserver.sh  \
    --master local[2] \
    --jars /root/spark-log/mysql-connector-java-5.1.28.jar  \
    ```
- 启动beeline：`beeline -u jdbc:hive2://localhost:10000 -n root`

修改thriftserver启动占用的默认端口号：
```
./start-thriftserver.sh  \
--master local[2] \
--jars /root/spark-log/mysql-connector-java-5.1.28.jar  \
--hiveconf hive.server2.thrift.port=14000
```
那么使用beeline访问时：`beeline -u jdbc:hive2://localhost:14000 -n root`

**thriftserver和普通的spark-shell/spark-sql有什么区别？**
- spark-shell、spark-sql都是一个spark  application；
- thriftserver， 不管你启动多少个客户端(beeline/code)，永远都是一个spark application；解决了一个数据共享的问题，多个客户端可以共享数据；

## 5.6、使用jdbc连接thriftserver

```java
public static void main(String[] args) throws Exception{
    Class.forName("org.apache.hive.jdbc.HiveDriver");
    Connection conn = DriverManager.getConnection("jdbc:hive2://localhost:10000", "root", "");
    PreparedStatement statement = conn.prepareStatement("select empno, ename, sal from emp limit 10");
    ResultSet rs = statement.executeQuery();
    while (rs.next()) {
        System.out.println("empno:" + rs.getInt("empno") +
                " , ename:" + rs.getString("ename") +
                " , sal:" + rs.getDouble("sal"));
    }
    rs.close();
    statement.close();
    conn.close();
}
```
**注意事项**：在使用jdbc开发时，一定要先启动thriftserver
```
Exception in thread "main" java.sql.SQLException:
Could not open client transport with JDBC Uri: jdbc:hive2://bluefish:14000:
java.net.ConnectException: Connection refused
```

## 5.7、SparkSQL使用场景


## 5.8、处理json的复杂场景：

### 5.8.1、数组

```json
{"name":"zhangsan", "nums":[1,2,3,4,5,6]}
{"name":"lisi", "nums":[6,7,8,9]}
```
可以输出如下：
```
scala> spark.sql("select name,nums[1] from json_table").show
+--------+-------+
|    name|nums[1]|
+--------+-------+
|zhangsan|      2|
|    lisi|      7|
+--------+-------+
scala> spark.sql("select name, explode(nums) from json_table").show
+--------+---+
|    name|col|
+--------+---+
|zhangsan|  1|
|zhangsan|  2|
|zhangsan|  3|
|zhangsan|  4|
|zhangsan|  5|
|zhangsan|  6|
|    lisi|  6|
|    lisi|  7|
|    lisi|  8|
|    lisi|  9|
+--------+---+
```

### 5.8.2、对象

```json
{"name":"Yin", "address":{"city":"Columbus","state":"Ohio"}}
{"name":"Michael", "address":{"city":null, "state":"California"}}
```
输出如下结果：
```
scala> spark.sql("select * from json_tables").show
+----------------+-------+
|         address|   name|
+----------------+-------+
|[Columbus, Ohio]|    Yin|
|  [, California]|Michael|
+----------------+-------+
scala> spark.sql("select name, address.city,address.state from json_tables").show
+-------+--------+----------+
|   name|    city|     state|
+-------+--------+----------+
|    Yin|Columbus|      Ohio|
|Michael|    null|California|
+-------+--------+----------+
```


# 6、DataFrame

## 6.1、概述

- DataSet：分布式的数据集；
- DataFrame：以列（列名、列的类型、列值）的形式构成的分布式数据集，按照列赋予不同的名称

以往RDD的方式：
- java/scala  ==> jvm
- python ==> python runtime

这样往往对耗时比较高，而DataFrame是：java/scala/python ==> Logic Plan

将所有提交的逻辑执行计划里面去，而不是在JVM或者Python的环境中执行

## 6.2、DataFrame 与 RDD

### 6.2.1、DataFrame使用

```java
public static void main(String[] args) {
    SparkSession session = SparkSession.builder().appName("JavaSparkSessionContext")
            .master("local[2]").getOrCreate();
    Dataset<Row> df = session.read().json("file:///workspace/scala/spark-sql-demo/people.json");
    df.printSchema();
    df.show();
    df.select("name").show();
    df.select(df.col("name"), df.col("age")).show();
    df.select(df.col("name"), df.col("age").plus(10).as("age2")).show();
    // select * from table where age > 35
    df.filter(df.col("age").gt(35)).show();
    df.groupBy(df.col("age")).count().show();
    session.stop();
}
```

- [DataFrame与RDD交互](http://spark.apache.org/docs/2.4.7/sql-getting-started.html#interoperating-with-rdds)

DataFrame和RDD互操作的两种方式：
- 反射：case class   前提：事先需要知道你的字段、字段类型    
- 编程：Row          如果第一种情况不能满足你的要求（事先不知道列）

**选型**：优先考虑第一种

### 6.2.2、反射操作方式：

```java
public static void infer(SparkSession session) {
    JavaRDD<Info> map = session.read()
            .textFile("file:///Users/bluefish/Documents/workspace/scala/spark-sql-demo/infos.txt")
            .toJavaRDD()
            .map(line -> {
                String[] lines = line.split(",");
                return new Info(Integer.parseInt(lines[0]), lines[1], Integer.parseInt(lines[2]));
            });
    Dataset<Row> df = session.createDataFrame(map, Info.class);
    df.printSchema();
    df.show();
    df.createOrReplaceTempView("infos");
    session.sql("select * from infos where age > 35").show();
    session.stop();
}
public static class Info {
    public int id;
    public String name;
    public int age;
    public Info(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
```

### 6.2.3、编程

```java
public static void program(SparkSession session) {
    JavaRDD<String> rdd = session.read()
            .textFile("file:///Users/bluefish/Documents/workspace/scala/spark-sql-demo/infos.txt")
            .toJavaRDD();
    // 定义schema
    String schema = "id name age";

    List<StructField> fields = new ArrayList<>();
    for (String fieldName : schema.split(" ")) {
        StructField field = DataTypes.createStructField(fieldName, DataTypes.StringType, true);
        fields.add(field);
    }
    StructType structType = DataTypes.createStructType(fields);
    JavaRDD<Row> javaRDD = rdd.map((Function<String, Row>) record -> {
        String[] lines = record.split(",");
        return RowFactory.create(lines[0], lines[1], lines[2]);
    });

    Dataset<Row> df = session.createDataFrame(javaRDD, structType);
    df.printSchema();
    df.show();
    df.createOrReplaceTempView("infos");
    session.sql("select * from infos where age > 35").show();
    session.stop();
}
```

## 6.3、DataFrame与Spark SQL

- DataFrame = RDD + Schema
- DataFrame 实际上是一个Row
- DataFrame over RDD

# 7、外部数据源

[数据源](http://spark.apache.org/docs/2.4.7/sql-data-sources.html)

默认处理的格式：parquet

```java
val DEFAULT_DATA_SOURCE_NAME = SQLConfigBuilder("spark.sql.sources.default")
    .doc("The default data source to use in input/output.")
    .stringConf
    .createWithDefault("parquet")
```

输出默认的模式是 ErrorIfExists，除了其还有 append、overwrite、ignore

## 7.1、parquet


## 7.2、Hive

往hive里面输出数据：
```
scala> spark.sql("select deptno, count(1) as cnt from emp group by deptno").write.saveAsTable("emp_statistics")
```
可能存在的问题：`org.apache.spark.sql.AnalysisException: Attribute name "count(1)" contains invalid character(s) among " ,;{}()\n\t=". Please use alias to rename it.;`

```
spark.sqlContext.setConf("spark.sql.shuffle.partitions","10")
```
在生产环境中一定要注意设置spark.sql.shuffle.partitions，默认是200

## 7.3、MySQL

```
scala> val jdbcDF2 = spark.read.format("jdbc") \
.option("url", "jdbc:mysql://localhost:3306/sparksql") \
.option("dbtable", "sparksql.TBLS") \
.option("user", "root") \
.option("password", "root") \
.option("driver", "com.mysql.jdbc.Driver").load()
```
或者是：
```
import java.util.Properties
val connectionProperties = new Properties()
connectionProperties.put("user", "root")
connectionProperties.put("password", "root")
connectionProperties.put("driver", "com.mysql.jdbc.Driver")

val jdbcDF2 = spark.read.jdbc("jdbc:mysql://localhost:3306", "hive.TBLS", connectionProperties)
```

# 8、性能优化

- 存储格式

- 压缩格式：压缩速度、压缩文件的可分割性

    设置压缩格式：`config("spark.sql.parquet.compression.codec","gzip")`

- 代码：选用高性能算子

- 代码：复用已有的数据

- 参数：并行度选择`spark.sql.shuffle.partitions`

- 参数：分区字段类型推测`spark.sql.sources.paritionsColumnTypeInference.enabled`，默认是开启的


# 参考资料

- [深入Spark设计与原理分析](https://github.com/JerryLead/SparkInternals)
- [官方文档](http://spark.apache.org/docs/latest/index.html)