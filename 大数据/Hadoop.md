
# 1、Hadoop

## 1.1、概述

开源的分布式存储和分布式计算平台，包含三大核心：
- HDFS：存储大数据技术的基础
- Mapreduce：分布式计算的大数据平台解决方案；
- Yarn：资源管理调度，管理资源

优势：
- 高扩展
- 低成本
- 成熟的生态圈

生态圈，除了自带的HDFS、Mapreduce
- Hive
- Hbase
- zookeeper

## 1.2、基础原理与架构


## 1.3、hadoop序列化机制



# 2、Hadoop安装

[Hadoop环境安装](../工具/环境配置/大数据环境.md#1Hadoop环境搭建)

# 3、HDFS

主要负责海量数据的分布式存储，支持主从结构，主节点支持多个NameNode，从节点支持多个DataNode

## 3.1、概念

- 数据块： 抽象块而非整个存储，一般大小是64M；
- Namenode： 管理文件系统的命名空间，存储文件元数据；维护着文件系统的所有文件和目录，文件与数据块的映射；记录每个文件中各个块所在的数据节点的信息；负责接收用户请求
- DataNode： 存储并检索数据块、存储数据

## 3.2、HDFS Shell

使用格式： `bin/hdfs dfs -xxx schema://authority/path`

HDFS的schema是hdfs，authority是NameNode的节点ip和对应的端口号(默认是9000)，path 是我们要操作的路径信息

常见操作：
- `-ls`：查询指定路径信息
- `-put`：从本地上传文件
- `-cat`：查看HDFS文件内容
- `-get`：下载文件到本地
- `-mkdri[-p]`：创建文件夹
- `-rm[-r]`：删除文件/文件夹

## 3.3、HDFS体系架构

HDFS包含：NameNode、SecondaryNameNode、DataNode，前面两个是在主节点中启动的，DataNode是在从节点上启动的

### 3.3.1、NameNode

- NameNode 是整个文件系统的管理节点，它主要维护者文件系统的文件目录树，文件/目录的元信息，每个文件对应的数据块列表，并且还负责接收用户的操作请求；
- NamNode主要包含以下文件： fsimage、edits、seed_txid、VERSION，存储在配置文件`core-site.xml`的`hadoop.tmp.dir`配置的目录下，比如目录：`/data/hadoop_repo/dfs/name/current`
- 以上文件保存的路径是由 `hdfs-default.xml` 文件中的 `dfs.namenode.name.dir` 属性控制的；

NameNode维护了两份关系：
- 第一份关系：File与Block list的关系，对应的关系信息存储在fsimage 和 edits 文件中（当NameNode启动的时候会把文件中的内容加载到内存中）；
- 第二份关系：DataNode 和 Block的关系，当DataNode启动的时候，会把当前节点上的Block信息和节点信息上报给NameNode

### 3.3.2、SecondaryNameNode

- 主要负责定期的把edits文件的内容合并到fsimage中
- 这个合作操作成为checkpoint，在合并的时候会对edits中内容进行转换，生成新的内容保存到fsimage文件中
- 在NameNode的HA架构中没有 SecondaryNameNode 进程的。

### 3.3.3、DataNode

- 提供真实文件数据的存储服务；
- HDFS会按照固定的带下，顺序对文件进行划分并编号，划分好的每一个块成为一个Block，HDFS默认Block大小是128 M

## 3.4、HDFS读、写数据过程

### 3.4.1、HDFS读数据过程


### 3.4.2、HDFS写数据过程

# 4、Mapreduce

## 4.1、概述

MapReduce是一个分布式计算编程模型，主要负责海量数据离线计算，主要由两个阶段Map和Reduce阶段；核心思想：分而治之


**核心概念：**
- Split：

- InputFormat

- OutputFormat

- Combiner

    使用场景：适用于求和、次数

- Partitioner


## 4.2、原理与执行过程

### 4.2.1、原理

### 4.2.2、MapReduce之 map 阶段执行过程

- 框架会把输入文件划分为很多 InputSplit，默认情况下每个HDFS把Block对应一个 InputSplit 通过 RecordReader类，把每个InputSplit解析成一个个`<k1,v1>`，默认情况下，每一行会被解析成一`<k1,v1>`；
- 框架调用Mapper类的map函数，map函数的形参是 `<k1,v1>`，输出是 `<k2,v2>`，一个 InputSplit 对应一个map task；
- 框架对map函数输出的`<k2,v2>` 进行分区，不同分区中 `<k2,v2>` 由不同的 reduce task处理，默认只有一个分区；
- 框架对每个分区中的数据，按照 k2 进行排序、分组；  分组指的是相同的 k2 的 v2 分成一个组；
- 在map节点上，框架可以执行 reduce 归约，为可选项；
- 框架会把map task 输出的 `<k2,v2>` 写入到 Linux 的磁盘文件中；

### 4.2.3、MapReduce之 reduce 阶段执行过程

- 框架对多个map任务的输出，按照不同的分区，通过网络 copy 到不同的 reduce 节点，这个过程称作 shuffle
- 框架对reduce端接收到的相同分区的`<k2,v2>`数据进行合并、排序、分组；
- 框架调用 Reducer类的reduce方法，输入是`<k2,{v2...}>`，输出是`<k3,v3>`；一个 `<k2,{v2...}>` 调用一次 reduce 函数；
- 框架把reduce的输出结果保存到HDFS中；

### 4.2.4、MapReduce之 shuffle 过程

## 4.3、架构

### 4.3.1、MapReduce1.x架构

- Job & JobTask
- JobTracker：作业调度、分配任务、监控任务执行进度；监控TaskTracker的状态
- TaskTracker：执行任务，汇报任务状态
- MapTask：自己开发的map任务交由给Task处理
- ReduceTask：将Map Task 输出的数据进行读取；

### 4.3.2、MapReduce2.x架构



## 4.4、WordCount示例

### 4.4.1、编写代码

需要的依赖：
```xml
<dependencies>
    <!-- hadoop-client依赖 -->
    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-client</artifactId>
        <version>3.2.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

```java
public class WordCount {
    public static class MyMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        @Override
        protected void map(LongWritable k1, Text v1, Context context) throws IOException, InterruptedException {
            // k1代表的是每一行的行首偏移量，v1代表的是每一行内容
            String[] words = v1.toString().split(" ");
            for (String word : words) {
                // 把迭代出来的单次封装<k2,v2>的形式
                Text k2 = new Text(word);
                LongWritable v2 = new LongWritable(1L);
                System.out.println("k2:" + k2.toString() + " --->" + v2.get());
                // 把 <k2,v2> 写出去
                context.write(k2, v2);
            }
        }
    }
    public static class MyReduce extends Reducer<Text, LongWritable, Text, LongWritable> {
        /**
         * @param v2s     已经对k1进行分组的数据
         */
        @Override
        protected void reduce(Text k2, Iterable<LongWritable> v2s, Context context)
                throws IOException, InterruptedException {
            long sum = 0L;
            for (LongWritable v2 : v2s) {
                sum += v2.get();
            }
            Text k3 = k2;
            LongWritable v3 = new LongWritable(sum);
            System.out.println("k3:" + k3.toString() + " --->" + v3.get());
            context.write(k3, v3);
        }
    }
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.exit(100);
        }
        Configuration conf = new Configuration();
        // 创建job = map + reduce
        Job job = Job.getInstance(conf);
        // 设置jar的执行class，否则集群找打不到对应的class
        job.setJarByClass(WordCount.class);
        // 设置输入路径
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        // 设置输出文件的路径，都是HDFS上的路径
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // 设置map方法的class
        job.setMapperClass(MyMapper.class);
        // 设置map输出参数key的类型
        job.setMapOutputKeyClass(Text.class);
        // 设置map输出数据的value的类型
        job.setMapOutputValueClass(LongWritable.class);

        // 指定 reduce的代码
        job.setReducerClass(MyReduce.class);
        // 指定 k3 类型
        job.setOutputKeyClass(Text.class);
        // 指定 v3 类型
        job.setMapOutputValueClass(LongWritable.class);

        job.waitForCompletion(true);
    }
}
```

### 4.4.2、编译

将代码编译成jar包

### 4.4.3、提交任务

将编译好的jar上传到hadoop集群的主节点上，执行如下命令：

`hadoop jar hadoop.jar WordCount hdfs://hadoop001:9000/hello.txt hdfs://hadoop001:9000/out`

## 4.5、MapReduce任务日志查看

- 开启yarn的日志聚合功能，把散落在 nodemanager 节点上的日志统一收集管理，方便查看日志；
- 需要修改 `yarn-site.xml` 中的 `yarn.log-aggregation-enable` 和 `yarn.log.server.url`，三台机器都需要修改
    ```xml
    <property> 
        <name>yarn.log-aggregation-enable</name>  
        <value>true</value>
    </property>
    <property>
        <name>yarn.log.server.url</name>
        <value>http://hadoop001:19888/jobhistory/logs/</value>
    </property>
    ```
- 启动 historyserver： sbin/mr-jobhistory-daemon.sh start historyserver

## 4.6、停止提交的任务

可以通过yarn application -list查看正在运行的任务
yarn application -kill application_id

# 5、Yarn

## 5.1、Yarn 产生的背景

在Hadoop1.X时，MapReduce的

## 5.2、Yarn概述

主要负责集群资源的管理和调度，Yarn目前支持三种调度器：
- FIFO Scheduler：先进先出（first in， first out）调度策略；
- Capacoty Scheduler：可以看做是 FIFO Scheduler 多队列
- Fair Scheduler：多队列，多用户共享资源

## 5.3、Yarn架构

## 5.4、Yarn执行流程

## 5.5、Yarn环境搭建


# 参考资料

- [Hadoop官方](http://hadoop.apache.org/)
- [Hadoop发展](https://www.infoq.cn/article/hadoop-ten-years-interpretation-and-development-forecast)
- [Hadoop CDH 版本下载](http://archive.cloudera.com/cdh5/cdh/5/)
- [HDFS读写数据原理](https://www.cnblogs.com/jonty666/p/9905352.html)
