
# 一、海量小文件问题

小文件问题：https://mp.weixin.qq.com/s/ibqBXPjLPtq2utAkdsRiOQ
http://www.idryman.org/blog/2013/09/22/process-small-files-on-hadoop-using-combinefileinputformat-1/

## 1、小文件问题

Hadoop中HDFS和MapReduce都是针对大文件来设计的，在小文件的处理上效率低下且十分消耗内存资源；针对HDFS而言，每一个小文件在 NameNode 中都会占用150字节的内存空间，最终会导致在集群中虽然存储了很多个文件，但是文件的体积并不大；

针对MapReduce而言，每一个小文件都是一个block，都会产生一个 InputSplit，最终每一个小文件都会产生一个map任务，会导致同时启动太多的map任务，map任务的启动很消耗性能，启动后执行了很短时间就停止了，影响MapReduce的执行效率；

## 2、Hadoop中解决方法

针对小文件问题，解决办法通常是选择一个容器，将这些小文件组织起来统一存储，HDFS提供了两种类型的容器： SequenceFile 和 MapFile

### 2.1、SequenceFile

SequenceFile 是hadoop提供的一种二进制人家，这种二进制文件直接`<key,value>`对序列化到文件中，一般对小文件可以使用这种文件合并，即将小文件的文件名作为key，文件内容作为value序列化到大文件中；

但是SequenceFile的一个缺点是：需要一个合并文件的过程，最终合并的文件会比较大，并且合并后的文件查看起来不方便，需要通过遍历才能查看到里面个每一个小文件；

SequenceFile 可以理解为把很多小文件压缩一个大的压缩包了

基本代码过程：
```java
private static void write(String input, String output) throws Exception {
    //创建一个配置对象
    Configuration conf = HadoopConfiguration.getConf();
    //获取操作HDFD的对象
    FileSystem fileSystem = FileSystem.get(conf);
    //删除HDFS上的输出文件
    fileSystem.delete(new Path(output), true);
    SequenceFile.Writer.Option[] options = new SequenceFile.Writer.Option[]{
            SequenceFile.Writer.file(new Path(output)),
            SequenceFile.Writer.keyClass(Text.class),
            SequenceFile.Writer.valueClass(Text.class)
    };
    SequenceFile.Writer writer = SequenceFile.createWriter(conf, options);
    File inputFiles = new File(input);
    if (inputFiles.isDirectory()) {
        File[] files = inputFiles.listFiles();
        for (File file : files) {
            String content = FileUtils.readFileToString(file, "UTF-8");
            String fileName = file.getName();
            Text key = new Text(fileName);
            Text value = new Text(content);
            // 想sequence写入文件
            writer.append(key, value);
        }
    }
    writer.close();
}
/**
* 读取SequenceFile文件
*
* @param inputFile SequenceFile文件路径
* @throws Exception
*/
private static void read(String inputFile) throws Exception {
    //创建一个配置对象
    Configuration conf = HadoopConfiguration.getConf();
    //创建阅读器
    SequenceFile.Reader reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(new Path(inputFile)));
    Text key = new Text();
    Text value = new Text();
    //循环读取数据，需要循环读取文件
    while (reader.next(key, value)) {
        //输出文件名称
        System.out.print("文件名：" + key.toString() + ",");
        //输出文件内容
        System.out.println("文件内容：" + value.toString() + "");
    }
    reader.close();
}
```

### 2.2、MapFile

MapFile是排序后的 SequenceFile，MapFile由两部分组成，分表是 index 和 data，其中index作为文件的数据索引，主要记录每个Record的key值，以及该Record在文件中的偏移位置。在MapFile被访问的时候，索引文件会被加载到内存，通过索引映射关系可以迅速定位到指定Record所在的文件位置，相对 SequenceFile，MapFile检索效率高，但是需要消耗一部分内存来存储index数据

```java
private static void write(String inputDir, String outputDir) throws Exception {
    Configuration conf = HadoopConfiguration.getConf();
    FileSystem fileSystem = FileSystem.get(conf);
    fileSystem.delete(new Path(outputDir), true);
    SequenceFile.Writer.Option[] options = new SequenceFile.Writer.Option[]{
            MapFile.Writer.keyClass(Text.class),
            MapFile.Writer.valueClass(Text.class)
    };
    MapFile.Writer writer = new MapFile.Writer(conf, new Path(outputDir), options);
    File inputDirFile = new File(inputDir);
    if (inputDirFile.isDirectory()) {
        File[] files = inputDirFile.listFiles();
        List<File> fileList = Arrays.asList(files);
        fileList.sort(Comparator.comparing(File::getName));
        //迭代文件
        for (File file : fileList) {
            //获取文件的全部内容
            String content = FileUtils.readFileToString(file, "UTF-8");
            //获取文件名
            String fileName = file.getName();
            //向SequenceFile中写入数据
            writer.append(new Text(fileName), new Text(content));
        }
    }
    writer.close();
}
private static void read(String inputDir) throws Exception {
    Configuration conf = HadoopConfiguration.getConf();
    MapFile.Reader reader = new MapFile.Reader(new Path(inputDir), conf);
    Text key = new Text();
    Text value = new Text();
    //循环读取数据
    while (reader.next(key, value)) {
        //输出文件名称
        System.out.print("文件名：" + key.toString() + ",");
        //输出文件内容
        System.out.println("文件内容：" + value.toString() + "");
    }
    reader.close();
}
```

### 2.3、SequenceFile存储与计算

如何通过MapReduce读取SequenceFile？针对SequenceFile读取只需要在wordCount相关代码中设置输入处理类：SequenceFileInputFormat
```
job.setInputFormatClass(SequenceFileInputFormat.class);
```

# 二、数据倾斜问题

**数据倾斜原理：**

在做数据运算的时候会设计到，countdistinct、group by、join等操作，都会触发Shuffle动作。一旦触发，所有相同 key 的值就会拉到一个或几个节点上，发生单点问题。
一个简单的场景，在订单表中，北京和上海两个地区的订单数量比其他地区高几个数量级。那么进行聚合的时候就会出现数据热点

**解决数据倾斜的几个思路：**
- 业务上：避免热点key的设计或者打散热点key，例如可以把北京和上海分成地区，然后单独汇总。
- 技术上：在热点出现时，需要调整方案避免直接进行聚合，可以借助框架本身的能力，例如进行mapside-join。
- 参数上：无论是Hadoop、Spark还是Flink都提供了大量的参数可以调整

## 1、MapReduce中数据倾斜问题

想要提升MapReduce的执行效率，其实就是提高Map和Reduce阶段的执行效率，默认情况下，Map阶段的map任务的个数是 InputSplit有关， InputSplit的个数一般是和Block块有关联的，针对map任务的个数正常情况下不用干预的，除非是海量小文件，考虑文件合并问题。

在Reduce阶段，默认情况下reduce的个数是1，如果说数据量比较大的时候，一个reduce处理起来很慢，可以考虑增加reduce的个数，可以实现数据分流，提高计算效率；但是需要注意的是，如果增加reduce的个数，需要对数据进行分区，分区之后，每一个分区的数据会被一个reduce任务处理。

如何增加分区：`job.setPartitionerClass` 设置分区类，如果没有设置，框架会存在默认值的
```java
public Class<? extends Partitioner<?,?>> getPartitionerClass() throws ClassNotFoundException {
    // 默认情况下，没有配置使用的是 HashPartitioner
    return (Class<? extends Partitioner<?,?>>) conf.getClass(PARTITIONER_CLASS_ATTR, HashPartitioner.class);
}
// HashPartitioner
public class HashPartitioner<K, V> extends Partitioner<K, V> {
    // map里的每一条数据都会进入到这个方法获取所在的分区信息， key 就是 k2，vlaue 就是 v2
    public int getPartition(K key, V value, int numReduceTasks) {
        // 决定因素是 numReduceTasks，其值默认是1，可以通过  job.getNumReduceTasks() 获取，最终任何值 % 1 都返回0，也就是只有一个 0号分区，所有数据都在
        return (key.hashCode() & Integer.MAX_VALUE) % numReduceTasks;
    }
}
```
根据上面代码显示，如果需要多个分区，很简单，只需要把 numReduceTasks 数目调大即可，这个其实就是 reduce 任务数量。

增加reduce任务个数在一定场景下是可以提高效率的，但是在一些特殊场景下，单纯增加reduce个数无法达到质的提升；来分析一个场景：假设一个文件有1000W数据，主要值都是数字`1~10`，希望统计每个数字出现的次数，这份数据大致情况如下：值为5的数据有910W，其余数据总共90W，意味着值为5的数据比较集中，或者值为5的数据属于倾斜的数据；

正常情况下一个reduce任务处理压力会很大，根据前面的分析，我们可以增加reduce的任务数量，如果我们增加reduce任务的数量调整到10，此时会把1000W的数据让这10个任务进行处理，会有效率提升吗？其实性能提升有限，为什么？值为5的数据有910W，占了总数量的90%多，那么这90%的数据都会被一个reduce任务处理，假设是reduce5，那么reduce5这个任务执行会很慢，其他reduce任务执行结束很长时间了，reduce5这个任务还没有结束，因为reduce5任务处理数据规模比其他任务大很多；

针对这种情况，如何解决呢？可以将值为5的数据尽量打散，把这个倾斜的数据分配到其他reduce任务中去计算，可以针对值为5的数据加上一个随机数
```
String key = words[0];
if("5".equals(key)){
    //把倾斜的key打散，分成10份
    key= "5"+"_"+random.nextInt(10);
}
```
打算后，获取的数据是一个半成品，还需要进行一次加工，其实前面把这个倾斜的数据打散之后做了一个局部聚合，还需要开发一个mapreduce任务在做一个任务的聚合；

## 2、Hive中数据倾斜问题

## 3、Spark数据倾斜

Spark中的数据倾斜也很常见，Spark中一个 stage 的执行时间受限于最后那个执行完的 task，因此运行缓慢的任务会拖累整个程序的运行速度。过多的数据在同一个task中执行，将会把executor撑爆，造成OOM，程序终止运行；

解决方案：
- 使用map join 代替reduce join
- 提高shuffle并行度

## 4、Flink数据倾斜

使用Window、GroupBy、Distinct等聚合函数时，频繁出现反压，消费速度很慢，个别的task会出现OOM，调大资源也无济于事

解决方案：
- MiniBatch设置
- 并行度设置

# 三、面试系列

ETL主要考察点：
数仓的知识，如何分层，sql高级函数：分组ton之类的，sql倾斜问题，主要还是和hive相关的知识


## 1、Hadoop

### 1.1、yarn的调度策略

### 1.2、hadoop中数据文件格式

### 1.3、Hadoop客户端节点是怎么识别Hadoop集群的？

- Hadoop客户端节点是怎么知道Hadoop集群的节点信息的？
- Hadoop客户端节点都需要配置什么内容？是否需要启动进程？

### 1.4、HDFS集群之间是否可以实现数据迁移？

### 1.5、Hadoop中必须要有SecondaryNameNode进程吗？

- 分析SecondaryNameNode都做了哪些事情？
- 如果没有SecondaryNameNode会出现什么现象？
- 是否有SecondaryNameNode的替代品？

### 1.6、HDFS中的安全模式有什么意义？

- HDFS中安全模式起了什么作用？
- 集群在安全模式期间都做了什么事情？

### 1.7、HDFS中NameNode内存将要耗尽，有什么解决方案？

如果排查发现HDFS中存储了海量的小文件？

### 1.8、如何查找HDFS中的大文件？

当Hadoop集群从节点的磁盘空间占满的时候，从节点就不可用了，此时需要给从节点扩容磁盘，或者删除从节点上的部分数据

从节点磁盘中存储的数据主要都是HDFS文件系统中的数据，不能直接在从节点磁盘上删除，这样会让集群误认为HDFS中的部分数据丢失了，会导致集群状态不正常。

此时想要删除文件就需要在HDFS中进行操作，找出大文件，以及一些无用的垃圾文件

- 如何查看HDFS中的大文件，需要使用什么命令？
- 注意：如果开启了回收站，在删除文件的时候一定要注意使用-skipTrash参数，否则删除的文件会进入回收站，回收站还是会占用HDFS存储空间。

### 1.9、MR中的Combiner阶段在什么场景下适合使用

- Combiner有什么作用？
- 在哪些场景下适合使用Combiner？

### 1.10、能不能使用zip或者rar文件解决HDFS中的小文件问题？

- 如果可以使用zip或者rar文件解决HDFS中的小文件问题，为什么？如果不可以，为什么？
- 使用zip或者rar文件和Hadoop提供的小文件解决方案(SquenceFile和MapFile)有什么区别？

### 1.11、如何从一批数据中找出倾斜的key？



### 1.12、分析一下Hadoop中的RPC框架？

- RPC框架的架构是什么样的？
- RPC框架可以解决什么问题？
- RPC框架的特点？
- Hadoop中在哪些地方用到RPC框架？

### 1.13、HDFS的存储（写）过程

- Client端发送写文件请求，NameNode检查文件是否存在，如果已存在，直接返回错误信息，否则，发送给client一些可用DataNode节点<br>
- Client将文件分块，并行存储到不同节点的DataNode上，发送完成后，Client同时发送信息给NameNode和DataNode<br>
- NameNode收到的Client信息后，发送确认信息给DataNode<br>
- DataNode同时收到NameNode和Client的确认信息后，提交写操作。</p>

## 2、Hive

### 2.1、hive中的row_number,rank 这些开窗函数有什么区别

### 2.2、hive数据倾斜怎么解决

### 2.3、hiveSQL怎么调优

### 2.4、提交到一条SQL到hive后，hive的执行流程

### 2.5、Hive的表存储模型有哪些？分区分桶表的作用

### 2.6、如何解决数据倾斜问题


### 2.7、Hive的存储格式


### 2.8、Hive查询时的优化项


### 2.9、生产环境中为什么建议使用Hive外部表？

### 2.10、Hive分区表如何开启自动加载分区？

### 2.11、分析Hive中数据的序列化格式？



### sql面试题

- [最新Hive/Hadoop高频面试点小集合](https://mp.weixin.qq.com/s/QtuQ9TFITLVN9QhFxnEsIA)
- [4万字Hive基础调优面试小总结](https://mp.weixin.qq.com/s/sJO3_0ycVrwScM_lYtmfZA)
- [HiveSQL 面试题](https://mp.weixin.qq.com/s/KvmR2ftgPBP7MMurcROAFg)

## 3、Flume

### 3.1、Flume中哪些地方用到事务机制





## 4、Spark

## 5、Flink

- [Flink面试题](https://mp.weixin.qq.com/s/mLwHlaZb-N0yTZUhlpcQbQ)

### Flink哪些算子容易产生数据倾斜

### Flink的Window处理过程中如果出现了数据倾斜

### Flink中的窗口有哪些？各自的区别是什么？举例说明你在什么场景下选择的是哪种窗口，为什么要这么选择

### Flink 如何实现 Exactly-once 语义？

### Flink 时间类型的分类和各自的实现原理？

### Flink 如何处理数据乱序和延迟？

### 基于Flink的分组TopN的实现思路及数据倾斜的解决方案

### Flink SQL的执行流程



## 6、数仓

[数据仓库面试题](https://mp.weixin.qq.com/s/pus1MhfEWhN9oOCSPlYGxQ)

### 6.1、数仓分层

### 6.2、数据仓库和数据库的区别

### 6.3、

## 7、HBase

[HBase面试题](https://mp.weixin.qq.com/s/TYmXgThj5Y_zBhm7YS_Fiw)

[大数据面试进阶系列](https://mp.weixin.qq.com/s/9Y1Mv8qtCwcvkf6BJuI0Ug)


## 语言和计算机基础

- [你不得不知道的知识-零拷贝](https://mp.weixin.qq.com/s/zQ0KdPFl34AllB01MHi03A)
- [阿里云Redis开发规范](https://mp.weixin.qq.com/s/6a6ydm3CxUQfZtzA4lCIcA)
- [面试系列：十个海量数据处理方法大总结](https://mp.weixin.qq.com/s/1IYi-uOWTxhkZcT830jMTg)
- [一致性协议浅析：从逻辑时钟到Raft](https://mp.weixin.qq.com/s/U9RtZkyqqGRQP3Y4xO6Icw)
- [你确定不来了解一下Redis中字符串的原理吗](https://mp.weixin.qq.com/s/pV1clfZkTXZlcRNlfBU5uA)
- [关于Redis的几件小事 | 使用目的与问题及线程模型](https://mp.weixin.qq.com/s/wmuaZfi6K0s3gWix5zft-g)
- [关于Redis的几件小事 | Redis的数据类型/过期策略/内存淘汰](https://mp.weixin.qq.com/s/069J8hxfzhZXbV3Ca8seaA)
- [关于Redis的几件小事 | 高并发和高可用](https://mp.weixin.qq.com/s/ElFO-OEKsWO08upz8mp_yQ)
- [一个细节 | Java中asList的缺陷](https://mp.weixin.qq.com/s/4Q0uQSzuA3KRkacT26iu8w)



## 离线数据框架面试

- [面试必备技能-HiveSQL优化](https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzU3MzgwNTU2Mg==&scene=124#wechat_redirect)
- [一篇文章入门Hbase](https://mp.weixin.qq.com/s/YhhD9jA7kkJuKM8JLLn-PQ)
- [敲黑板：HBase的RowKey设计](https://mp.weixin.qq.com/s/LzPaZ0znIEs34NMbYTHmjA)
- [Hive/HiveSQL常用优化方法全面总结](https://mp.weixin.qq.com/s/DfvN7S_00oYw1hqAQDr48g)
- [Hive面试题](https://my.oschina.net/u/4101357/blog/3229802)

## 实时计算面试系列

1. [剑谱总纲 | 大数据方向学习面试知识图谱](https://mp.weixin.qq.com/s/mi7ZhIpbgqGi9yu0_nuVTA)
2. [助力秋招-独孤九剑破剑式 | 10家企业面试真题](https://mp.weixin.qq.com/s/jk6y-uMQeZixBhMItEU_LQ)


### Flink

1. [你有必要了解一下Flink底层RPC使用的框架和原理](https://mp.weixin.qq.com/s/db7lRwuhLvsrfcfsZ8dpLw)
2. [昨天面试别人说他熟悉Flink，结果我问了他Flink是如何实现exactly-once语义的？](https://mp.weixin.qq.com/s/G1as9FtfFPCgfOwydglrEQ)
3. [Flink UDF自动注册实践](https://mp.weixin.qq.com/s/bdIuRKZg2DDfK0P4rPD9TQ)
4. [Stream SQL的执行原理与Flink的实现](https://mp.weixin.qq.com/s/CAZUzaGnujI6GvoVmOmgkw)
5. [分布式快照算法: Chandy-Lamport 算法](https://mp.weixin.qq.com/s/lgi_b7s7USsy7pARzp4kMQ)
6. [科学使用HBase Connection](https://mp.weixin.qq.com/s/ualjrwTX3Df5EgTnkc3Q2Q)
7. [全网第一份 | Flink学习面试灵魂40问，看看你能答上来几个？](https://mp.weixin.qq.com/s/-J-UZ6vs8BD9sYjdeMOmTQ)
8. [全网第一 | Flink学习面试灵魂40问答案，文末有福利!](https://mp.weixin.qq.com/s/k26RLt-aWjWv1Ts7XIdscw)


### Spark

1. [Spark之数据倾斜调优](https://mp.weixin.qq.com/s/mLi6dQpvv45Ptthvwq67EA)
2. [Structured Streaming 实现思路与实现概述](https://mp.weixin.qq.com/s/aTq19nQ9NlyZYAch0AyH2A)
3. [Spark内存调优](https://mp.weixin.qq.com/s/-wMHIZDh0cIDq5RfvlFRwg)
4. [广告点击数实时统计：Spark StructuredStreaming + Redis Streams](https://mp.weixin.qq.com/s/0a70Bhyc_6PeJMm1wIKuOQ)
5. [Structured Streaming 之状态存储解析](https://mp.weixin.qq.com/s/YPbry9dpI6iEOJh3wnDQAg)
6. [周期性清除Spark Streaming流状态的方法](https://mp.weixin.qq.com/s/8EHn7R5OEt2KJCTj2FVznA)
7. [SparkSQL的3种Join实现](https://mp.weixin.qq.com/s/4EQj_FDXK2znyiHx-H9MtQ)
8. [Spark将Dataframe数据写入Hive分区表的方案](https://mp.weixin.qq.com/s/dCSUCqvc78Th_UgD6LRGrg)


### Kafka

1. [万字长文干货 | Kafka 事务性之幂等性实现](https://mp.weixin.qq.com/s/SQ1Ya-eX4Kt1CVDcbMPhVA)
2. [一道真实的阿里面试题 | 如何保证消息队列的高可用](https://mp.weixin.qq.com/s/hYfTl8eR2Vkue8-EpgZY7g)
3. [关于MQ面试的几件小事 | 消息队列的用途、优缺点、技术选型](https://mp.weixin.qq.com/s/yID2OPYk40CzIAxmZEQpvw)
4. [关于MQ面试的几件小事 | 如何保证消息不丢失](https://mp.weixin.qq.com/s/EaJbOLabVd2YGWznDjGoNQ)
5. [关于MQ面试的几件小事 | 如何保证消息按顺序执行](https://mp.weixin.qq.com/s/KNrsKLakgOPde2Tmw3viaA)

[大数据面试](https://blog.csdn.net/qq_43259670/article/details/105927827)

- [MapReduce/HDFS/YARN面试题](https://mp.weixin.qq.com/s/_qFd-v3TF0W8lBo6ci-17w)
- [Flink企业级面试题60连击](https://mp.weixin.qq.com/s/FWit1b_6Me4Ay7UF6NtL3Q)