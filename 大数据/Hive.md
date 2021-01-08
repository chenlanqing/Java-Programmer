
# 1、基本介绍

## 1.1、Hive

- Hive：由 Facebook 开源用于解决海量结构化日志的数据统计工具。
- Hive 是基于 Hadoop 的一个数据仓库工具，可以将结构化的数据文件映射为一张表，并提供类 SQL 查询功能

**Hive的本质：**将 HQL 转化成 MapReduce 程序

![](image/Hive-Hive基本.png)

- Hive处理的数据存储在HDFS上；
- Hive分析数据底层的实现是MapReduce
- 执行程序是在Yarn上；

## 1.2、Hive优缺点

**优点：**
- 操作接口采用类sql语法，提供快速开发的能力；
- 避免了编写MapReduce，减少开发人员学习成本；
- Hive执行延迟比较高，常用于数据分析、对实时性要求不高的场景；
- Hive优势是处理大数据；
- Hive支持自定义函数，可以根据自己的需求来实现自己的函数；
- 统一的元数据管理，可与 presto／impala／sparksql 等共享数据；

**缺点：**
- Hive的HQL表达能力有限，迭代式算法无法表达，数据挖掘方面不擅长，由于MapReduce数据处理流程的限制，效率更高的算法无法实现；
- Hive效率比较低，Hive自动生成的MapReduce作业。通常情况下不够智能化；Hive调优比较困难，粒度较粗；

## 1.3、Hive架构原理

![](image/Hive-基本架构.png)

- **Client**：CLI（Command-Line interface）、JDBC（JDBC访问Hive）、WEBUI（浏览器你）；
- **Mata Store（元数据）**：在 Hive 中，表名、表结构、字段名、字段类型、表的分隔符等统一被称为元数据。所有的元数据默认存储在 Hive 内置的 derby 数据库中，但由于 derby 只能有一个实例，也就是说不能有多个命令行客户端同时访问，所以在实际生产环境中，通常使用 MySQL 代替 derby；
    
    Hive 进行的是统一的元数据管理，就是说你在 Hive 上创建了一张表，然后在 presto／impala／sparksql 中都是可以直接使用的，它们会从 Metastore 中获取统一的元数据信息，同样的你在 presto／impala／sparksql 中创建一张表，在 Hive 中也可以直接使用
- **Hadoop**：使用HDFS进行存，使用MapReduce进行计算；
- **Driver**：驱动器
    - 解析器：将SQL字符转成抽象语法树AST，一般使用第三方工具库完成，对AST进行语法分析，比如表是否存在、字段是否存在、SQL语义是否有误；
    - 编译器：将AST编译生成逻辑执行计划；
    - 优化器：对逻辑执行计划进行优化；
    - 执行器：把逻辑执行疾患转换成可以运行的物理计划，对Hive来说，就是MR/Spark

Hive的运行机制：

![](image/Hive-Hive的运行机制.png)

Hive 通过给用户提供的一系列交互接口，接收到用户的指令(SQL)，使用自己的 Driver，结合元数据(MetaStore)，将这些指令翻译成 MapReduce，提交到 Hadoop 中执行，最后，将 执行返回的结果输出到用户交互接口；

可以用 command-line shell 和 thrift／jdbc 两种方式来操作数据：
- **command-line shell**：通过 hive 命令行的的方式来操作数据；
- **thrift／jdbc**：通过 thrift 协议按照标准的 JDBC 的方式操作数据。

Hive详细运行参考美团技术文章：[Hive SQL 的编译过程](https://tech.meituan.com/2014/02/12/hive-sql-to-mapreduce.html)

## 1.4、Hive与数据库比较

由于 Hive 采用了类似 SQL 的查询语言 HQL(Hive Query Language)，因此很容易将 Hive 理解为数据库。其实从结构上来看，Hive 和数据库除了拥有类似的查询语言，再无类似之处。数据库可以用在 Online 的应用中，但是 Hive 是为数据仓库而设计的；
- 查询语言：熟悉SQL的开发者能够快速上手Hive的开发；
- 数据更新：由于Hive是针对数据仓库应用设计的，而数据仓库的内容是读多写少的。因此，Hive 中不建议对数据的改写，所有的数据都是在加载的时候确定好的。而数据库中的数据通常是需要经常进行修改的，因此可以使用 `INSERT INTO ... VALUES` 添加数据，使用`UPDATE ... SET` 修改数据；
- 执行延迟：Hive在查询数据的时候，由于没有索引，需要扫描整个表，因此延迟较高。另外一个导 致 Hive 执行延迟高的因素是 MapReduce 框架，由于 MapReduce 本身具有较高的延迟，因此在利用 MapReduce 执行 Hive 查询时，也会有较高的延迟。相对的，数据规模较小时数据库的执行延迟较低，当数据规模大到超过数据库的处理能力的时候，Hive的并行计算显然能体现出优势；
- 数据规模：Hive能够支持海量数据的查询，而数据库相对的数据量没那么大；

# 2、安装

[](../辅助资料/环境配置/大数据环境.md#3Hive环境配置)

# 3、基本用法

创建表：
```
CREATE  TABLE table_name 
  [(col_name data_type [COMMENT col_comment])]
```

加载数据到表中：
```
LOAD DATA LOCAL INPATH 'filepath' INTO TABLE tablename

load data local inpath '/home/hadoop/data/hello.txt' into table hive_wordcount;
```

Hive中null值时用    `\N` 表示
在mysql中 null 值时用 null 表示，在mysql同步数据到hive中时需要注意空值的处理


# Tez

Hive基础 Tez


# 参考资料

- [Hive官方文档](https://cwiki.apache.org/confluence/display/Hive/GettingStarted)