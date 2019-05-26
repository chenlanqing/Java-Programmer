<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、lucene](#%E4%B8%80lucene)
  - [1、全文检索](#1%E5%85%A8%E6%96%87%E6%A3%80%E7%B4%A2)
  - [2、Lucene介绍](#2lucene%E4%BB%8B%E7%BB%8D)
  - [3、索引结构](#3%E7%B4%A2%E5%BC%95%E7%BB%93%E6%9E%84)
  - [4、Lucene类](#4lucene%E7%B1%BB)
  - [5、倒排索引原理](#5%E5%80%92%E6%8E%92%E7%B4%A2%E5%BC%95%E5%8E%9F%E7%90%86)
- [二、Elasticsearch](#%E4%BA%8Celasticsearch)
  - [1、概述](#1%E6%A6%82%E8%BF%B0)
  - [2、安装](#2%E5%AE%89%E8%A3%85)
  - [3、基础概念](#3%E5%9F%BA%E7%A1%80%E6%A6%82%E5%BF%B5)
  - [4、基本用法](#4%E5%9F%BA%E6%9C%AC%E7%94%A8%E6%B3%95)
  - [5、ElasticSearch分布式架构原理](#5elasticsearch%E5%88%86%E5%B8%83%E5%BC%8F%E6%9E%B6%E6%9E%84%E5%8E%9F%E7%90%86)
- [面试题](#%E9%9D%A2%E8%AF%95%E9%A2%98)
  - [1、es的分布式架构原理能说一下么（es是如何实现分布式的啊）？](#1es%E7%9A%84%E5%88%86%E5%B8%83%E5%BC%8F%E6%9E%B6%E6%9E%84%E5%8E%9F%E7%90%86%E8%83%BD%E8%AF%B4%E4%B8%80%E4%B8%8B%E4%B9%88es%E6%98%AF%E5%A6%82%E4%BD%95%E5%AE%9E%E7%8E%B0%E5%88%86%E5%B8%83%E5%BC%8F%E7%9A%84%E5%95%8A)
  - [2、es写入数据的工作原理是什么啊？es查询数据的工作原理是什么啊？](#2es%E5%86%99%E5%85%A5%E6%95%B0%E6%8D%AE%E7%9A%84%E5%B7%A5%E4%BD%9C%E5%8E%9F%E7%90%86%E6%98%AF%E4%BB%80%E4%B9%88%E5%95%8Aes%E6%9F%A5%E8%AF%A2%E6%95%B0%E6%8D%AE%E7%9A%84%E5%B7%A5%E4%BD%9C%E5%8E%9F%E7%90%86%E6%98%AF%E4%BB%80%E4%B9%88%E5%95%8A)
  - [3、es在数据量很大的情况下（数十亿级别）如何提高查询性能啊？](#3es%E5%9C%A8%E6%95%B0%E6%8D%AE%E9%87%8F%E5%BE%88%E5%A4%A7%E7%9A%84%E6%83%85%E5%86%B5%E4%B8%8B%E6%95%B0%E5%8D%81%E4%BA%BF%E7%BA%A7%E5%88%AB%E5%A6%82%E4%BD%95%E6%8F%90%E9%AB%98%E6%9F%A5%E8%AF%A2%E6%80%A7%E8%83%BD%E5%95%8A)
  - [4、es生产集群的部署架构是什么？每个索引的数据量大概有多少？每个索引大概有多少个分片？](#4es%E7%94%9F%E4%BA%A7%E9%9B%86%E7%BE%A4%E7%9A%84%E9%83%A8%E7%BD%B2%E6%9E%B6%E6%9E%84%E6%98%AF%E4%BB%80%E4%B9%88%E6%AF%8F%E4%B8%AA%E7%B4%A2%E5%BC%95%E7%9A%84%E6%95%B0%E6%8D%AE%E9%87%8F%E5%A4%A7%E6%A6%82%E6%9C%89%E5%A4%9A%E5%B0%91%E6%AF%8F%E4%B8%AA%E7%B4%A2%E5%BC%95%E5%A4%A7%E6%A6%82%E6%9C%89%E5%A4%9A%E5%B0%91%E4%B8%AA%E5%88%86%E7%89%87)
- [参考资料](#%E5%8F%82%E8%80%83%E8%B5%84%E6%96%99)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


# 一、lucene

Lucene 是一个基于 Java 的全文信息检索工具包。

## 1、全文检索

什么是全文检索：将非结构化的数据中的一部分数据提取出来，重新组织，使其变的有一定的结构，然后对此结构进行搜索

### 1.1、全文检索的流程分为：索引流程、搜索流程。

- 索引流程：即采集数据构建文档对象分析文档（分词）创建索引。
- 搜索流程：即用户通过搜索界面创建查询执行搜索，搜索器从索引库搜索渲染搜索结果

### 1.2、数据分类

生活中的数据总体分为三种：
- 结构化数据，固定格式和长度，如数据库数据，元数据等
- 非结构化数据，无固定格式和长度，如邮件，word文档，商品描述信息，非结构化数据也称为为全文数据
- 半结构化数据，如XML，HTML等，当然根据需要按结构化数据来处理，也可抽取出纯文本按非结构化数据来处理

对非结构化数据（全文数据）搜索方法：
- 顺序扫描法(Serial Scanning)：从头到尾的扫描，比如windows的搜索文件，Linux下的grep命令，这种方法比较原始，但对于小数据量的文件，这种方法还是最直接，最方便的。但是对于大量的文件，这种方法就很慢了；
- 全文检索（Full-text Search）：即将非结构化数据中的一部分信息提取出来，重新组织，使其变得有一定结构，然后对此有一定结构的数据进行搜索，从而达到搜索相对较快的目的。这种先建立索引，再对索引进行搜索的过程就叫全文检索(Full-text Search)

### 1.3、全文检索过程

如果索引总能够保存从字符串到文件的映射，则会大大提高搜索速度。保存这种信息的索引称为反向索引或者倒排索引

**如何创建索引：**
- 准备一些要索引的文档（Document）；
- 将原文档传给分词组件（Tokenizer）：经过分词(Tokenizer)后得到的结果称为词元(Token)
    - 将文档分成一个一个单独的单词；
    - 去除标点符号；
    - 去除停词(Stop word)：语言中最普通的一些单词，比如：the”,“a”，“this” 由于没有特别的意义，因而大多数情况下不能成为搜索的关键词
- 将词元（Token）传给语言处理组件（Linguistic Processor），语言处理组件(linguistic processor)的结果称为词(Term)；
- 将得到的词(Term)传给索引组件(Indexer)：
    - 利用得到的词(Term)创建一个字典。
    - 对字典按字母顺序进行排序
    - 合并相同的词(Term)成为文档倒排(Posting List)链表，其中有几个定义：
        - Document Frequency 即文档频次，表示总共有多少文件包含此词(Term)。
        - Frequency 即词频率，表示此文件中包含了几个此词(Term)

**如何对索引进行搜索：**
- 用户输入查询语句；
- 搜索应用程序对用户输入查询语句进行词法分析，语法分析，及语言处理
- 搜索索引，得到符合语法树的文档；
- 根据得到的文档和查询语句的相关性，对结果进行排序。

全文搜索原理如下：

![](image/全文检索原理.jpg)

## 2、Lucene介绍

Lucene是一个高效的，可扩展的，基于Java的全文检索库；它不是一个完整的搜索应用程序，而是为你的应用程序提供索引和搜索功能，由于是它不是一个完整的搜索应用程序，所以有一些基于Lucene的开源搜索引擎产生，比如Elasticsearch和solr

## 3、索引结构

- Index：索引，由很多的Document组成。
- Segment：一个索引可以包含多个段，段与段之间是独立的，添加新文档可以生成新的段，不同的段可以合并
- Document：由很多的Field组成，是Index和Search的最小单位。文档是我们建索引的基本单位，不同的文档是保存在不同的段中的，一个段可以包含多 个文档
- Field：由很多的Term组成，包括Field Name和Field Value。
- Term：由很多的字节组成。一般将Text类型的Field Value分词之后的每个最小单元叫做Term

lucene 的索引结构中，即保存了正向信息，也保存了反向信息
- 正向：索引(Index) –> 段(segment) –> 文档(Document) –> 域(Field) –> 词(Term)
- 反向：词(Term) –> 文档(Document

## 4、Lucene类

- IndexWriter：lucene 中最重要的的类之一，它主要是用来将文档加入索引，同时控制索引过程中的一些参数使用。

- Analyzer：分析器，主要用于分析搜索引擎遇到的各种文本。常用的有StandardAnalyzer分析器、StopAnalyzer 分析器、WhitespaceAnalyzer 分析器等。

- Directory：索引存放的位置；lucene 提供了两种索引存放的位置，一种是磁盘，一种是内存。一般情况将索引放在磁盘上；相应地lucene 提供了FSDirectory 和RAMDirectory 两个类。

- Document：文档；Document 相当于一个要进行索引的单元，任何可以想要被索引的文件都必须转化为Document 对象才能进行索引。

- Field：字段。

- IndexSearcher：是lucene 中最基本的检索工具，所有的检索都会用到IndexSearcher工具;

- Query：查询，lucene 中支持模糊查询，语义查询，短语查询，组合查询等等，如有TermQuery、BooleanQuery、RangeQuery、WildcardQuery 等一些类。

- QueryParser：是一个解析用户输入的工具，可以通过扫描用户输入的字符串，生成Query对象。

- Hits：在搜索完成之后，需要把搜索结果返回并显示给用户，只有这样才算是完成搜索的目的。在lucene 中，搜索的结果的集合是用Hits 类的实例来表示的

## 5、倒排索引原理

它相反于一篇文章包含了哪些词，它从词出发，记载了这个词在哪些文档中出现过，由两部分组成——词典和倒排表。

词典结构尤为重要，有很多种词典结构，各有各的优缺点，常用的有：B+树、跳跃表、FST
- B+树：外存索引、可更新；空间大、速度不够快
- 跳跃表：结构简单、跳跃间隔、级数可控，Lucene3.0之前使用的也是跳跃表结构，后换成了FST，但跳跃表在Lucene其他地方还有应用如倒排表合并和文档号索引；缺点：模糊查询支持不好
- FST（Finite State Transducer）：Lucene现在使用的索引结构
    - 优点：内存占用率低，压缩率一般在3倍~20倍之间、模糊查询支持好、查询快
    - 缺点：结构复杂、输入要求有序、更新不易
    
    Lucene里有个FST的实现，从对外接口上看，它跟Map结构很相似，有查找，有迭代：

# 二、Elasticsearch

## 1、概述

Elasticsearch使用Java开发并使用Lucene作为其核心来实现所有索引和搜索的功能，通过简单的RESTful API来隐藏Lucene的复杂性，从而让全文搜索变得简单；

Elasticsearch不仅仅是Lucene和全文搜索，我们还能这样去描述它：
- 分布式的实时文件存储，每个字段都被索引并可被搜索
- 分布式的实时分析搜索引擎
- 可以扩展到上百台服务器，处理PB级结构化或非结构化数据

## 2、安装

### 2.1、单机安装

- 从官方网站下载对应的版本的文件，比如：elasticsearch-6.0.1
- 解压到文件
- 执行命令：`./bin/elasticsearch`，可以看到启动日志，通过访问：127.0.0.1:9200可以看到响应的数据；如果需要后台启动执行命令：`./bin/elasticsearch -d`

### 2.2、安装head插件，es数据展示美化

从 [github](https://github.com/mobz/elasticsearch-head)下载，使用node安装以及运行，npm run start 运行；

### 2.3、集群安装es

- 在配置文件`conf/elasticsearch.yml`最后加上如下配置，配置为master
    ```
    http.cors.enabled: true
    http.cors.allow-origin: "*"

    cluster.name: bluefish
    node.name: master
    node.master: true

    network.host: 127.0.0.1
    ```
- 在slave节点上增加如下配置：
    ```
    cluster.name: bluefish
    node.name: slave1

    network.host: 127.0.0.1
    http.port: 9201

    discovery.zen.ping.unicast.hosts: ["127.0.0.1"]
    ```

集群和节点：节点(node)是一个运行着的Elasticsearch实例。集群(cluster)是一组具有相同cluster.name的节点集合，他们协同工作，共享数据并提供故障转移和扩展功能，当然一个节点也可以组成一个集群

## 3、基础概念

- 索引：含有相同属性的文档集合；相当于sql的database
- 类型：索引可以定义为一个或多个类型，文档必须属于一个类型；相当于database里的table；
- 文档：是可以被索引的基本数据单位；相当于table的一行记录
- Mapping：type中定义的结构

- 分片：每个索引都有多个分片，每个分片是一个Lucene索引；

## 4、基本用法

其是用RESTFUL API风格来的

## 5、ElasticSearch分布式架构原理

Elasticsearch设计的理念就是分布式搜索引擎，底层其实还是基于lucene的。其核心思想就是在多台机器上启动多个es进程实例，组成了一个es集群。

- es中存储数据的基本单位是索引：index
- 一个index里面会有多个type，每个type可以认为是一个mysql中的表
- 每个type有一个mapping。mapping代表了这个type的表结构的定义，定义了这个type中每个字段名称，字段是什么类型的，然后还有这个字段的各种配置；
- 往index里的一个type里面写的一条数据，叫做一条document，一条document就代表了mysql中某个表里的一行给，每个document有多个field，每个field就代表了这个document中的一个字段的值；
- 索引可以拆分成多个shard，每个shard存储部分数据；shard的数据实际是有多个备份，就是说每个shard都有一个primary shard，负责写入数据，但是还有几个replica shard。primary shard写入数据之后，会将数据同步到其他几个replica shard上去；
- es集群多个节点，会自动选举一个节点为master节点，这个master节点其实就是干一些管理的工作的，比如维护索引元数据拉，负责切换primary shard和replica shard身份拉，之类的。要是master节点宕机了，那么会重新选举一个节点为master节点



# 面试题

## 1、es的分布式架构原理能说一下么（es是如何实现分布式的啊）？

## 2、es写入数据的工作原理是什么啊？es查询数据的工作原理是什么啊？

## 3、es在数据量很大的情况下（数十亿级别）如何提高查询性能啊？

- filesystem cache
- 数据预热
- 冷热分离
- document模型设计
- 分页性能优化

## 4、es生产集群的部署架构是什么？每个索引的数据量大概有多少？每个索引大概有多少个分片？


# 参考资料

* [ElasticSearch基本操作](http://www.ruanyifeng.com/blog/2017/08/elasticsearch.html)