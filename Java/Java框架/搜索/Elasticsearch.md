
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

Lucene是一个高效的，可扩展的，基于Java的全文检索库；它不是一个完整的搜索应用程序，而是为你的应用程序提供索引和搜索功能，由于是它不是一个完整的搜索应用程序，所以有一些基于Lucene的开源搜索引擎产生，比如 Elasticsearch 和 solr

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

它相反于一篇文章包含了哪些词，它从词出发，记载了这个词在哪些文档中出现过，由两部分组成：`词典和倒排表`。

词典结构尤为重要，有很多种词典结构，各有各的优缺点，常用的有：B+树、跳跃表、FST
- B+树：外存索引、可更新；空间大、速度不够快
- 跳跃表：结构简单、跳跃间隔、级数可控，Lucene3.0之前使用的也是跳跃表结构，后换成了FST，但跳跃表在Lucene其他地方还有应用如倒排表合并和文档号索引；缺点：模糊查询支持不好
- FST（Finite State Transducer）：Lucene现在使用的索引结构
    - 优点：内存占用率低，压缩率一般在3倍~20倍之间、模糊查询支持好、查询快
    - 缺点：结构复杂、输入要求有序、更新不易
    
    Lucene里有个FST的实现，从对外接口上看，它跟Map结构很相似，有查找，有迭代；

## 6、中文分词

分词插件：hanlp.com

## 7、为什么引入搜索引擎

- 海量数据的查询
- 拆词查询
- 支持空格
- 搜索内容高亮

# 二、Elasticsearch

## 1、概述

### 1.1、简介

Elasticsearch使用Java开发并使用Lucene作为其核心来实现所有索引和搜索的功能，通过简单的RESTful API来隐藏Lucene的复杂性，从而让全文搜索变得简单；

Elasticsearch不仅仅是Lucene和全文搜索，我们还能这样去描述它：
- 分布式的实时文件存储，每个字段都被索引并可被搜索
- 分布式的实时分析搜索引擎
- 可以扩展到上百台服务器，处理PB级结构化或非结构化数据

Elasticsearch 使用的是一种名为倒排索引的数据结构，这一结构的设计可以允许十分快速地进行全文本搜索。倒排索引会列出在所有文档中出现的每个特有词汇，并且可以找到包含每个词汇的全部文档。在索引过程中，Elasticsearch 会存储文档并构建倒排索引，这样用户便可以近实时地对文档数据进行搜索

### 1.2、优势

- Elasticsearch 很快：由于 Elasticsearch 是在 Lucene 基础上构建而成的，所以在全文本搜索方面表现十分出色。Elasticsearch 同时还是一个近实时的搜索平台，这意味着从文档索引操作到文档变为可搜索状态之间的延时很短；

- Elasticsearch 具有分布式的本质特征：Elasticsearch 中存储的文档分布在不同的容器中，这些容器称为分片，可以进行复制以提供数据冗余副本，以防发生硬件故障。Elasticsearch 的分布式特性使得它可以扩展至数百台（甚至数千台）服务器，并处理 PB 量级的数据；

- Elastic Stack 简化了数据采集、可视化和报告过程

## 2、安装

### 2.1、基础安装

- 从官方网站下载对应的版本的文件，比如：elasticsearch-7.4.2

- 解压到文件，并将其移动的`/usr/local/`目录下，目录下文件：
    - bin：可执行文件在里面，运行es的命令就在这个里面，包含了一些脚本文件等
    - config：配置文件目录
    - JDK：java环境
    - lib：依赖的jar，类库
    - logs：日志文件
    - modules：es相关的模块
    - plugins：可以自己开发的插件

- 新建目录`/usr/local/elasticsearch-7.4.2/data`，这个作为索引目录；

- 修改配置文件：`elasticearch.yml`
    - 修改集群名称，默认是elasticsearch：`cluster.name: test-elasticsearch`
    - 为当前的es节点取个名称，名称随意，如果在集群环境中，都要有相应的名字：`node.name: es-node0`
    - 修改data数据保存地址：`path.data: /usr/local/elasticsearch-7.4.2/data`
    - 修改日志数据保存地址：`path.logs: /usr/local/elasticsearch-7.4.2/logs`
    - 绑定es网络ip：`network.host: 0.0.0.0`，所有都可以访问
    - 默认端口号，可以自定义修改：`http.port: 9200`
    - 集群节点名字：`cluster.initial_master_nodes: ["es-node0"]`；

- 如果需要修改jvm参数，修改`config/jvm.options`文件

- elasticsearch不允许root用户启动，需要添加一个用户来进行操作：
    - 添加用户：`useradd esuser`
    - 授权用户：`chown -R esuser:esuser /usr/local/elasticsearch-7.4.2`
    - 切换到新建的用户：`su esuser`
    - 查看当前用户：`whoami`

    使用root用户启动报错如下：
    ```
    [2020-01-04T10:37:56,991][WARN ][o.e.b.ElasticsearchUncaughtExceptionHandler] [es-node0] uncaught exception in thread [main]
    org.elasticsearch.bootstrap.Startu
    pException: java.lang.RuntimeException: can not run elasticsearch as root
            at org.elasticsearch.bootstrap.Elasticsearch.init(Elasticsearch.java:163) ~[elasticsearch-7.4.2.jar:7.4.2]
            at org.elasticsearch.bootstrap.Elasticsearch.execute(Elasticsearch.java:150) ~[elasticsearch-7.4.2.jar:7.4.2]
            at org.elasticsearch.cli.EnvironmentAwareCommand.execute(EnvironmentAwareCommand.java:86) ~[elasticsearch-7.4.2.jar:7.4.2]
            at org.elasticsearch.cli.Command.mainWithoutErrorHandling(Command.java:125) ~[elasticsearch-cli-7.4.2.jar:7.4.2]
            at org.elasticsearch.cli.Command.main(Command.java:90) ~[elasticsearch-cli-7.4.2.jar:7.4.2]
            at org.elasticsearch.bootstrap.Elasticsearch.main(Elasticsearch.java:115) ~[elasticsearch-7.4.2.jar:7.4.2]
            at org.elasticsearch.bootstrap.Elasticsearch.main(Elasticsearch.java:92) ~[elasticsearch-7.4.2.jar:7.4.2]
    Caused by: java.lang.RuntimeException: can not run elasticsearch as root
            at org.elasticsearch.bootstrap.Bootstrap.initializeNatives(Bootstrap.java:105) ~[elasticsearch-7.4.2.jar:7.4.2]
            at org.elasticsearch.bootstrap.Bootstrap.setup(Bootstrap.java:172) ~[elasticsearch-7.4.2.jar:7.4.2]
            at org.elasticsearch.bootstrap.Bootstrap.init(Bootstrap.java:349) ~[elasticsearch-7.4.2.jar:7.4.2]
            at org.elasticsearch.bootstrap.Elasticsearch.init(Elasticsearch.java:159) ~[elasticsearch-7.4.2.jar:7.4.2]
    ```

- 执行启动命令`./elasticsearch`，发现如下报错信息：
    ```
    ERROR: [3] bootstrap checks failed
    [1]: max file descriptors [4096] for elasticsearch process is too low, increase to at least [65535]
    [2]: max number of threads [3795] for user [esuser] is too low, increase to at least [4096]
    [3]: max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
    ```
    需要切换到root用户修改配置文件：`vim /etc/security/limits.conf`，新增如下信息：
    ```
    * soft nofile 65536
    * hard nofile 131072
    * soft nproc 2048
    * hard nproc 4096
    ```
    然后在修改文件：`vim /etc/sysctl.conf`，增加配置：`vm.max_map_count=262145`，然后刷新sysctl：`sysctl -p`

- 执行命令：`./bin/elasticsearch`，可以看到启动日志，通过访问：127.0.0.1:9200可以看到响应的数据；如果需要后台启动执行命令：`./bin/elasticsearch -d`

### 2.2、控制台

#### 2.2.1、head插件

从 [github](https://github.com/mobz/elasticsearch-head)下载，使用node安装以及运行，npm run start 运行；

如果head插件和elasticsearch运行不在一台服务器上，那么会存在跨域问题，只需要在配置文件：`elasticearch.yml` 增加如下配置
```yml
# ------------- NetWork -----------
http.cors.enabled: true
http.cors.allow-origin: "*"
```

#### 2.2.2、cerebro

从[Cerebro](https://github.com/lmenezes/cerebro 下载zip包，解压缩安装即可

### 2.3、通过docker安装

这里通过docker-compose安装es和head插件
（1）创建docker-compose.yml文件
```yml
version: '2'
services:
  elasticsearch:
    image: elasticsearch:6.8.5
    environment:
      - discovery.type=single-node
    ports:
      - "9200:9200"
      - "9300:9300"
    networks:
      - es_network
  elasticsearch-head:
    image: mobz/elasticsearch-head:5-alpine
    container_name: elasticsearch-head
    restart: always
    ports:
      - 9100:9100

networks:
  es_network:
    external: true
```
（2）启动：
```
docker-compose -f docker-compose.yml up -d elasticsearch
docker-compose -f docker-compose.yml up -d elasticsearch-head
```
（3）关于跨域问题同上面的配置方式

## 3、核心术语

- 索引（index）：含有相同属性的文档集合；相当于sql的database；
- 类型（type）：索引可以定义为一个或多个类型，文档必须属于一个类型；相当于database里的table；
- 文档（document）：是可以被索引的基本数据单位；相当于table的一行记录；文档是所有可搜索数据的最小单位，会被序列化为json格式；每个文档都有一个UniqueID，可以直接指定，也可以自动生成；
- Mapping：type中定义的结构
- 字段（fields）：对应表的每一列；
- 分片：每个索引都有多个分片，每个分片是一个Lucene索引；
- 节点：node

## 4、基本用法

### 4.1、基本操作

- 查看集群健康：`GET  /_cluster/health`；

- 创建索引：
    ```json
    PUT  /index_test    // 其中 index_test索引名称
    {
        "settings": {
            "index": {
                "number_of_shards": "2",
                "number_of_replicas": "0"
            }
        }
    }
    ```

- 查看索引：`GET _cat/indices?v`

- 删除索引：`DELETE  /index_test`，其中 index_test 为索引名称

- Index的API操作
    ```
    #查看索引相关信息
    GET kibana_sample_data_ecommerce

    #查看索引的文档总数
    GET kibana_sample_data_ecommerce/_count

    #查看前10条文档，了解文档格式
    POST kibana_sample_data_ecommerce/_search
    {
    }

    #_cat indices API
    #查看indices
    GET /_cat/indices/kibana*?v&s=index

    #查看状态为绿的索引
    GET /_cat/indices?v&health=green

    #按照文档个数排序
    GET /_cat/indices?v&s=docs.count:desc

    #查看具体的字段
    GET /_cat/indices/kibana*?pri&v&h=health,index,pri,rep,docs.count,mt

    #How much memory is used per index?
    GET /_cat/indices?v&h=i,tm&s=tm:desc

    get _cat/nodes?v
    GET /_nodes/es7_01,es7_02
    GET /_cat/nodes?v
    GET /_cat/nodes?v&h=id,ip,port,v,m

    GET _cluster/health
    GET _cluster/health?level=shards
    GET /_cluster/health/kibana_sample_data_ecommerce,kibana_sample_data_flights
    GET /_cluster/health/kibana_sample_data_flights?level=shards

    #### cluster state
    The cluster state API allows access to metadata representing the state of the whole cluster. This includes information such as
    GET /_cluster/state

    #cluster get settings
    GET /_cluster/settings
    GET /_cluster/settings?include_defaults=true

    GET _cat/shards
    GET _cat/shards?h=index,shard,prirep,state,unassigned.reason
    ```

### 4.2、文档操作

- 添加文档：`POST /my_doc/_doc/1` -> `{索引名}/_doc/{索引ID}`（是指索引在es中的id，而不是这条记录的id，比如记录的id从数据库来是1001，并不是这个。如果不写，则自动生成一个字符串。建议和数据id保持一致 ），可以是如下body
    ```json
    POST users/_doc
    {
        "user" : "Mike",
        "post_date" : "2019-04-15T14:12:12",
        "message" : "trying out Kibana"
    }
    ```
    如果索引没有手动建立mappings，那么当插入文档数据的时候，会根据文档类型自动设置属性类型。这个就是es的动态映射，帮我们在index索引库中去建立数据结构的相关配置信息
    - 该索引的mapping属性：`"fields":{"keyword":{"type":"keyword","ignore_above":256}}`：对一个字段设置多种索引模式，使用text类型做全文检索，也可使用keyword类型做聚合和排序
    - `"ignore_above":256`：设置字段索引和存储的长度最大值，超过则被忽略；
    ```json
    // create document. 自动生成 _id
    POST users/_doc
    {
        "user" : "Mike",
        "post_date" : "2019-04-15T14:12:12",
        "message" : "trying out Kibana"
    }
    // create document. 指定Id。如果id已经存在，报错
    PUT users/_doc/1?op_type=create
    {
        "user" : "Jack",
        "post_date" : "2019-05-15T14:12:12",
        "message" : "trying out Elasticsearch"
    }
    // create document. 指定 ID 如果已经存在，就报错: version_conflict_engine_exception
    PUT users/_create/1
    {
        "user" : "Jack",
        "post_date" : "2019-05-15T14:12:12",
        "message" : "trying out Elasticsearch"
    }
    ```

- 删除文档：`DELETE /my_doc/_doc/1` -> `{索引名}/_doc/{索引ID}`；文档删除不是立即删除，文档还是保存在磁盘上，索引增长越来越多，才会把那些曾经标识过删除的，进行清理，从磁盘上移出去

- 修改文档：
    - Update 指定 ID  (先删除，在写入)
        ```json
        PUT users/_doc/1
        {
            "user" : "Mike"
        }
        ```
    - 在原文档上增加字段:
        ```json
        POST users/_update/1/
        {
            "doc":{
                "post_date" : "2019-05-15T14:12:12",
                "message" : "trying out Elasticsearch"
            }
        }
        ```
    - 全量替换：`PUT /my_doc/_doc/1`
        ```json
        PUT /my_doc/_doc/1
        {
            "id": 1001,
            "name": "test-1",
            "desc": "test is very good, 测试网非常牛！",
            "create_date": "2019-12-24"
        }
        ```

    **每次修改、删除后，version会更改**

- 查询：
    - `GET users/_doc/1`：返回指定id的数据
    - `GET users/_doc/_search`：该文档下的所有数据
    ```json
    {
        "_index" : "users",
        "_type" : "_doc",
        "_id" : "1",
        "_version" : 1,
        "_seq_no" : 1,
        "_primary_term" : 1,
        "found" : true,
        "_source" : {
            "user" : "Jack",
            "post_date" : "2019-05-15T14:12:12",
            "message" : "trying out Elasticsearch"
        }
    }
    ```
    - `_index`：文档数据所属那个索引，理解为数据库的某张表即可。
    - `_type`：文档数据属于哪个类型，新版本使用_doc。
    - `_id`：文档数据的唯一标识，类似数据库中某张表的主键。可以自动生成或者手动指定。
    - `_score`：查询相关度，是否契合用户匹配，分数越高用户的搜索体验越高。
    - `_version`：版本号。
    - `_source`：文档数据，json格式
    - `_seq_no`：文档版本号，作用同`_version`
    - `_primary_term`：文档所在位置

    上述返回的source中包含全集字段，如果字段很多可以自定义字段：
    `GET /index_demo/_doc/1?_source=id,name`  或者 `GET /index_demo/_doc/_search?_source=id,name`

- 判断文档是否存在：`HEAD /index_demo/_doc/1`，判断http返回码，如果存在则是200，不存在则是404；

- Bulk 操作:
    ```json
    // 执行第1次
    POST _bulk
    { "index" : { "_index" : "test", "_id" : "1" } }
    { "field1" : "value1" }
    { "delete" : { "_index" : "test", "_id" : "2" } }
    { "create" : { "_index" : "test2", "_id" : "3" } }
    { "field1" : "value3" }
    { "update" : {"_id" : "1", "_index" : "test"} }
    { "doc" : {"field2" : "value2"} }
    ```


    #执行第2次
    POST _bulk
    { "index" : { "_index" : "test", "_id" : "1" } }
    { "field1" : "value1" }
    { "delete" : { "_index" : "test", "_id" : "2" } }
    { "create" : { "_index" : "test2", "_id" : "3" } }
    { "field1" : "value3" }
    { "update" : {"_id" : "1", "_index" : "test"} }
    { "doc" : {"field2" : "value2"} }
    ```

### 4.3、并发控制

- 插入新数据：
    ```json
    POST /my_doc/_doc
    {
        "id": 1010,
        "name": "test-1010",
        "desc": "test",
        "create_date": "2019-12-24"
    }
    # 此时 _version 为 1
    ```
- 修改数据：
    ```json
    POST    /my_doc/_doc/{_id}/_update
    {
        "doc": {
            "name": "测试"
        }
    }
    # 此时 _version 为 2
    ```

- 模拟两个客户端操作同一个文档数据，_version都携带为一样的数值
    ```json
    # 操作1
    POST    /my_doc/_doc/{_id}/_update?if_seq_no={数值}&if_primary_term={数值}
    {
        "doc": {
            "name": "测试1"
        }
    }

    # 操作2
    POST    /my_doc/_doc/{_id}/_update?if_seq_no={数值}&if_primary_term={数值}
    {
        "doc": {
            "name": "测试2"
        }
    }
    ```

### 4.4、内置分词

- 分词：把文本转换为一个个的单词，分词称之为analysis。es默认只对英文语句做分词，中文不支持，每个中文字都会被拆分为独立的个体
    ```json
    POST /_analyze
    {
        "analyzer": "standard",
        "text": "text文本"
    }
    结果：
    {
        "tokens": [
            {
                "token": "text",
                "start_offset": 0,
                "end_offset": 4,
                "type": "<ALPHANUM>",
                "position": 0
            },
            {
                "token": "文",
                "start_offset": 4,
                "end_offset": 5,
                "type": "<IDEOGRAPHIC>",
                "position": 1
            },
            {
                "token": "本",
                "start_offset": 5,
                "end_offset": 6,
                "type": "<IDEOGRAPHIC>",
                "position": 2
            }
        ]
    }

    POST /my_doc/_analyze
    {
        "analyzer": "standard",
        "field": "name",
        "text": "text文本"
    }
    ```
-  es内置分词器：
    - standard：默认分词，单词会被拆分，大小会转换为小写。
    - simple：按照非字母分词。大写转为小写。
    - whitespace：按照空格分词。忽略大小写。
    - Simple Analyzer – 按照非字母切分（符号被过滤），小写处理
    - Patter Analyzer – 正则表达式，默认 \W+ (非字符分隔)
    - Language – 提供了30多种常见语言的分词器
    - stop：去除无意义单词，比如`the/a/an/is…`
    - keyword：不做分词。把整个文本作为一个单独的关键词：
        ```json
        {
            "analyzer": "keyword",
            "field": "name",
            "text": "I am a Java programmer, I study Java in imooc!"
        }
        结果：
        {
            "tokens": [
                {
                    "token": "I am a Java programmer, I study Java in imooc!",
                    "start_offset": 0,
                    "end_offset": 46,
                    "type": "word",
                    "position": 0
                }
            ]
        }
        ```

### 4.5、中文分词器

IK中文分词器：[elasticsearch-analysis-ik](https://github.com/medcl/elasticsearch-analysis-ik)

- 安装：在elasticsearch的目录下的`plugin`新建目录`ik`，将下载对应的zip包解压到该目录下，重启elasticsearch即可；
  
    或者：`./bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.8.5/elasticsearch-analysis-ik-6.8.5.zip`

- 支持分词器类型：
    - `ik_max_word`：会将文本做最细粒度的拆分，比如会将“中华人民共和国国歌”拆分为“中华人民共和国,中华人民,中华,华人,人民共和国,人民,人,民,共和国,共和,和,国国,国歌”，会穷尽各种可能的组合，适合 Term Query；
    - `ik_smart`：会做最粗粒度的拆分，比如会将“中华人民共和国国歌”拆分为“中华人民共和国,国歌”，适合 Phrase 查询

- 使用：
    ```json
    {
        "analyzer": "ik_smart",
        "field": "name",
        "text": "中华人民共和国国歌"
    }
    结果：
    {
        "tokens": [
            {
                "token": "中华人民共和国",
                "start_offset": 0,
                "end_offset": 7,
                "type": "CN_WORD",
                "position": 0
            },
            {
                "token": "国歌",
                "start_offset": 7,
                "end_offset": 9,
                "type": "CN_WORD",
                "position": 1
            }
        ]
    }
    ```

- 自定义中文词库
    - 在`{es}/plugins/ik/config`下，创建：`vim custom.dic`
    - 添加需要添加的中文词库，比如：`骚年` 等网络用语；
    - 配置自定义扩展词典，在ik的配置文件：`{es}/plugins/ik/config/IKAnalyzer.cfg.xml`，加入自定义的文件

## 5、DSL搜索

### 5.1、请求参数的查询

**URI Query**：在URL中使用参数
```
GET kibana_sample_data_ecommerce/_search?q=customer_first_name:Eddie
GET kibana*/_search?q=customer_first_name:Eddie
GET /_all/_search?q=customer_first_name:Eddie
```
**REQUEST Body**：使用elasticsearch基于json的更加完备的DSL查询
```json
POST kibana_sample_data_ecommerce/_search
{
	"profile": true,
	"query": {
		"match_all": {}
	}
}
```

**URI Search：**
```
GET /movies/_search?q=2012&df=title&sort=year:desc&from=0&size=10&timeout=1s
{ 
    "profile": true
}
```
- `q`：指定查询语句，使用 Query String Syntax
- `df`：默认字段，不指定时；
- `sort：排序/ from 和 size 用于分页`
- `profile`：可以查看查询时如何执行的

**Query String Syntax**
- 指定查询 vs 泛查询：`q=title:2012 /  q=2012`
- Term 与 Phrase
    - `Beautiful Mind` 等效于 `Beautiful OR Mind`
    - `"Beautiful Mind"` 等效于 `Beautiful AND Mind`，Phrase查询还邀请前后顺序保持一致
- 分组与引号
    - `title:(Beautiful AND Mind)`
    - `title="Beautiful Mind"`
- 布尔操作
    - `AND OR NOT 或者 && || !`：必须大写，`title:(+Martix -reloaded)`，可以通过 + 或者 - 替代
- 分组：
    - `+` 表示 must
    - `-` 表示 must_not
    - `title:(+Martix -reloaded)`
- 范围查询
    - 区间表示: `[]`表示闭区间，`{}`表示开区间
        - `year:{2019 TO 2018}`
        - `year:[* TO 2018]` 
- 算数符号
    - `year:>2010`
    - `year:(>2010 && <= 2018)`
    - `year:(+>2010 +<=2018)`
- 通配符：通配符查询效率低，占用内存大，不建议使用，特别是放在最前面
    - `?` 代表1个字符，`*` 代表 0 或多个字符
- 正则表达式：`title:[bt]oy`
- 模糊匹配与近似查询
    - `title:beautifu~1`
    - `title:"lord rings"~2`

```json
#基本查询
GET /movies/_search?q=2012&df=title&sort=year:desc&from=0&size=10&timeout=1s
// 带profile
GET /movies/_search?q=2012&df=title
{
	"profile":"true"
}
// 泛查询，正对_all,所有字段
GET /movies/_search?q=2012
{
	"profile":"true"
}
// 指定字段
GET /movies/_search?q=title:2012&sort=year:desc&from=0&size=10&timeout=1s
{
	"profile":"true"
}
// 查找美丽心灵, Mind为泛查询
GET /movies/_search?q=title:Beautiful Mind
{
	"profile":"true"
}
// 泛查询
GET /movies/_search?q=title:2012
{
	"profile":"true"
}
// 使用引号，Phrase查询
GET /movies/_search?q=title:"Beautiful Mind"
{
	"profile":"true"
}
// 分组，Bool查询
GET /movies/_search?q=title:(Beautiful Mind)
{
	"profile":"true"
}
// 布尔操作符
// 查找美丽心灵
GET /movies/_search?q=title:(Beautiful AND Mind)
{
	"profile":"true"
}
// 查找美丽心灵
GET /movies/_search?q=title:(Beautiful NOT Mind)
{
	"profile":"true"
}
// 查找美丽心灵
GET /movies/_search?q=title:(Beautiful +Mind)
{
	"profile":"true"
}
// 范围查询 ,区间写法
GET /movies/_search?q=title:beautiful AND year:[2002 TO 2018]
{
	"profile":"true"
}
// 通配符查询
GET /movies/_search?q=title:b*
{
	"profile":"true"
}
// 模糊匹配&近似度匹配
GET /movies/_search?q=title:beautifl~1
{
	"profile":"true"
}
GET /movies/_search?q=title:"Lord Rings"~2
{
	"profile":"true"
}
```

### 5.2、DSL基本语法

QueryString用的很少，一旦参数复杂就难以构建，所以大多查询都会使用dsl来进行查询更好
- Domain Specific Language
- 特定领域语言
- 基于JSON格式的数据查询
- 查询更灵活，有利于复杂查询

DSL格式语法：
```json
# 查询
POST     /shop/_doc/_search
{
    "query": {
        "match": {
            "desc": "慕课网"
        }
    }
}
# 判断某个字段是否存在
{
    "query": {
        "exists": {
	        "field": "desc"
	    }
    }
}
```
语法格式为一个json object，内容都是key-value键值对，json可以嵌套；key可以是一些es的关键字，也可以是某个field字段；

```json
# ignore_unavailable=true，可以忽略尝试访问不存在的索引“404_idx”导致的报错
# 查询movies分页
POST /movies,404_idx/_search?ignore_unavailable=true
{
  "profile": true,
	"query": {
		"match_all": {}
	}
}
POST /kibana_sample_data_ecommerce/_search
{
  "from":10,
  "size":20,
  "query":{
    "match_all": {}
  }
}
#对日期排序
POST kibana_sample_data_ecommerce/_search
{
  "sort":[{"order_date":"desc"}],
  "query":{
    "match_all": {}
  }

}
#source filtering
POST kibana_sample_data_ecommerce/_search
{
  "_source":["order_date"],
  "query":{
    "match_all": {}
  }
}
// 脚本字段
GET kibana_sample_data_ecommerce/_search
{
  "script_fields": {
    "new_field": {
      "script": {
        "lang": "painless",
        "source": "doc['order_date'].value+'hello'"
      }
    }
  },
  "query": {
    "match_all": {}
  }
}
POST movies/_search
{
  "query": {
    "match": {
      "title": "last christmas"
    }
  }
}
POST movies/_search
{
  "query": {
    "match": {
      "title": {
        "query": "last christmas",
        "operator": "and"
      }
    }
  }
}
POST movies/_search
{
  "query": {
    "match_phrase": {
      "title":{
        "query": "one love"

      }
    }
  }
}
POST movies/_search
{
  "query": {
    "match_phrase": {
      "title":{
        "query": "one love",
        "slop": 1

      }
    }
  }
}
```

QueryString与SimpleQueryString
```json
PUT /users/_doc/1
{
  "name":"Ruan Yiming",
  "about":"java, golang, node, swift, elasticsearch"
}
PUT /users/_doc/2
{
  "name":"Li Yiming",
  "about":"Hadoop"
}
POST users/_search
{
  "query": {
    "query_string": {
      "default_field": "name",
      "query": "Ruan AND Yiming"
    }
  }
}
POST users/_search
{
  "query": {
    "query_string": {
      "fields":["name","about"],
      "query": "(Ruan AND Yiming) OR (Java AND Elasticsearch)"
    }
  }
}
// Simple Query 默认的operator是 Or
POST users/_search
{
  "query": {
    "simple_query_string": {
      "query": "Ruan AND Yiming",
      "fields": ["name"]
    }
  }
}
POST users/_search
{
  "query": {
    "simple_query_string": {
      "query": "Ruan Yiming",
      "fields": ["name"],
      "default_operator": "AND"
    }
  }
}
GET /movies/_search
{
	"profile": true,
	"query":{
		"query_string":{
			"default_field": "title",
			"query": "Beafiful AND Mind"
		}
	}
}
// 多fields
GET /movies/_search
{
	"profile": true,
	"query":{
		"query_string":{
			"fields":[
				"title",
				"year"
			],
			"query": "2012"
		}
	}
}
GET /movies/_search
{
	"profile":true,
	"query":{
		"simple_query_string":{
			"query":"Beautiful +mind",
			"fields":["title"]
		}
	}
}
```

### 5.3、查询所有与分页

**在索引中查询所有的文档：**
```
GET     /shop/_doc/_search
```
或者通过DSL的 match_all 来实现查询所有
```json
POST     /shop/_doc/_search
{
    "query": {
        "match_all": {}
    },
    "_source": ["id", "nickname", "age"]   # 显示需要查询的字段
}
```

**分页：**
```json
POST  /shop/_doc/_search
{
	"query": {
		"match_all": {}
	},
	"_source": [
		"id",
		"nickname",
		"age"
	],
	"from": 5, # 从第几条开始，默认是0开始的，
	"size": 5 # 每页的数据
}
```

### 5.6、match 扩展：operator/ids

**operator：**
- `or`：搜索内容分词后，只要存在一个词语匹配就展示结果
- `and`：搜索内容分词后，都要满足词语匹配
```json
POST     /shop/_doc/_search
{
    "query": {
        "match": {
            "desc": "慕课网"
        }
    }
}
# 等同于
{
    "query": {
        "match": {
            "desc": {
                "query": "慕课网",
                "operator": "or"
            }
        }
    }
}
```
默认是or，其还可以有一个最低匹配精度，
- `minimum_should_match`: 最低匹配精度，至少有`[分词后的词语个数]x百分百`，得出一个数据值取整。举个例子：当前属性设置为70，若一个用户查询检索内容分词后有10个词语，那么匹配度按照 10x70%=7，则desc中至少需要有7个词语匹配，就展示；若分词后有8个，则 8x70%=5.6，则desc中至少需要有5个词语匹配，就展示。minimum_should_match 也能设置具体的数字，表示个数；
```json
POST     /shop/_doc/_search
{
    "query": {
        "match": {
            "desc": {
                "query": "女友生日送我好玩的xbox游戏机",
                "minimum_should_match": "60%"
            }
        }
    }
}
```

**ids：根据文档主键**
```json
GET /shop/_doc/1001
```
查询多个
```json
POST     /shop/_doc/_search
{
    "query": {
        "ids": {
            "type": "_doc",
            "values": ["1001", "1010", "1008"]
        }
    }
}
```

### 5.7、multi_match/boost

**multi_match**：满足使用match在多个字段中进行查询的需求

比如在`desc`和`nickname`中查询“皮特帕克慕课网”
```json
POST     /shop/_doc/_search
{
    "query": {
        "multi_match": {
                "query": "皮特帕克慕课网",
                "fields": ["desc", "nickname"]

        }
    }
}
```
**boost**：权重，为某个字段设置权重，权重越高，文档相关性得分就越高。通畅来说搜索商品名称要比商品简介的权重更高
```json
POST     /shop/_doc/_search
{
    "query": {
        "multi_match": {
                "query": "皮特帕克慕课网",
                "fields": ["desc", "nickname^10"]

        }
    }
}
```
`nickname^10` 代表搜索提升10倍相关性，也就是说用户搜索的时候其实以这个nickname为主，desc为辅，nickname的匹配相关度当然要提高权重比例了

### 5.8、布尔查询

可以组合多重查询
- must：查询必须匹配搜索条件，譬如 and
- should：查询匹配满足1个以上条件，譬如 or
- must_not：不匹配搜索条件，一个都不要满足，跟should相反

```json
POST     /shop/_doc/_search
{
    "query": {
        "bool": {
            "must": [
                {
                    "multi_match": {
                        "query": "慕课网",
                        "fields": ["desc", "nickname"]
                    }
                },
                {
                    "term": {
                        "sex": 1
                    }
                },
                {
                    "term": {
                        "birthday": "1996-01-14"
                    }
                }
            ]
        }
    }
}
{
    "query": {
        "bool": {
            "should（must_not）": [
                {
                    "multi_match": {
                        "query": "学习",
                        "fields": ["desc", "nickname"]
                    }
                },
                {
                	"match": {
                		"desc": "游戏"
                	}	
                },
                {
                    "term": {
                        "sex": 0
                    }
                }
            ]
        }
    }
}
```
```json
{
    "query": {
        "bool": {
            "must": [
                {
                	"match": {
                		"desc": "慕"
                	}	
                },
                {
                	"match": {
                		"nickname": "慕"
                	}	
                }
            ],
            "should": [
                {
                	"match": {
                		"sex": "0"
                	}	
                }
            ],
            "must_not": [
                {
                	"term": {
                		"birthday": "1992-12-24"
                	}	
                }
            ]
        }
    }
}
```
为指定词语加权：特殊场景下，某些词语可以单独加权，这样可以排得更加靠前
```json
POST     /shop/_doc/_search
{
    "query": {
        "bool": {
            "should": [
            	{
            		"match": {
            			"desc": {
            				"query": "律师",
            				"boost": 18
            			}
            		}
            	},
            	{
            		"match": {
            			"desc": {
            				"query": "进修",
            				"boost": 2
            			}
            		}
            	}
            ]
        }
    }
}
```

### 5.9、过滤器

对搜索出来的结果进行数据过滤。不会到es库里去搜，不会去计算文档的相关度分数，所以过滤的性能会比较高，过滤器可以和全文搜索结合在一起使用。post_filter元素是一个顶层元素，只会对搜索结果进行过滤。不会计算数据的匹配度相关性分数，不会根据分数去排序，query则相反，会计算分数，也会按照分数去排序
- query：根据用户搜索条件检索匹配记录
- post_filter：用于查询后，对结果数据的筛选，也可以单独使用

过滤操作：
- gte：大于等于
- lte：小于等于
- gt：大于
- lt：小于

比如：查询账户金额大于80元，小于160元的用户。并且生日在1998-07-14的用户
```json
POST     /shop/_doc/_search
{
	"query": {
		"match": {
			"desc": "慕课网游戏"
		}	
    },
    "post_filter": {
		"range": {
			"money": {
				"gt": 60,
				"lt": 1000
			}
		}
	}	
}
```
另外在match里也可以做：
```json
{
    "query": {
        "bool": {
            "must": [
                {
                    "range": {
                        "money": {
                            "gt": "60",
                            "lt": "100"
                        }
                    }
                }
            ]
        }
    }
}
```

### 5.10、排序

es的排序同sql，可以desc也可以asc。也支持组合排序，最好在数字型与日期型字段上排序
```json
POST     /shop/_doc/_search
{
	"query": {
		"match": {
			"desc": "慕课网游戏"
		}
    },
    "post_filter": {
    	"range": {
    		"money": {
    			"gt": 55.8,
    			"lte": 155.8
    		}
    	}
    },
    "sort": [
        {
            "age": "desc"
        },
        {
            "money": "desc"
        }
    ]
}
```
**对文本排序：**由于文本会被分词，所以往往要去做排序会报错，通常我们可以为这个字段增加额外的一个附属属性，类型为keyword，用于做排序
```json
POST        /shop2/_mapping
{
    "properties": {
        "id": {
            "type": "long"
        },
        "nickname": {
            "type": "text",
            "analyzer": "ik_max_word",
            "fields": {
                "keyword": {
                    "type": "keyword"
                }
            }
        }
    }
}
排序的使用如下：
{
    "sort": [
        {
            "nickname.keyword": "desc"
        }
    ]
}
```

### 5.11、高亮highlight

```json
POST     /shop/_doc/_search
{
    "query": {
        "match": {
            "desc": "慕课网"
        }
    },
    "highlight": {
        "pre_tags": ["<tag>"],
        "post_tags": ["</tag>"],
        "fields": {
            "desc": {}
        }
    }
}
```

### 5.12、其他查询

- prefix：按照前缀查询
    ```json
    POST     /shop/_doc/_search
    {
        "query": {
            "prefix": {
                "desc": "imo"
            }
        }
    }
    ````
- [fuzzy](https://www.elastic.co/guide/cn/elasticsearch/guide/current/fuzzy-match-query.html)：模糊搜索，并不是指的sql的模糊搜索，而是用户在进行搜索的时候的打字错误现象，搜索引擎会自动纠正，然后尝试匹配索引库中的数据
    ```json
    POST     /shop/_doc/_search
    {
        "query": {
            "fuzzy": {
            "desc": "imoov.coom"
            }
        }
    }
    # 或多字段搜索
    {
        "query": {
            "multi_match": {
            "fields": [ "desc", "nickname"],
            "query": "imcoc supor",
            "fuzziness": "AUTO"
            }
        }
    }
    {
        "query": {
            "multi_match": {
            "fields": [ "desc", "nickname"],
            "query": "演说",
            "fuzziness": "1"
            }
        }
    }
    ```
- [wildcard](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-wildcard-query.html)：占位符查询
    - `?`：1个字符
    - `*`：1个或多个字符
    ```json
    {
        "query": {
            "wildcard": {
            "desc": "*oo?"
            }
        }
    }
    {
        "query": {
            "wildcard": {
                "desc": "演*"
            }
        }
    }
    ```


## 6、深度分页与批量操作

### 6.1、深度分页

深度分页其实就是搜索的深浅度，比如第1页，第2页，第10页，第20页，是比较浅的；第10000页，第20000页就是很深了，比如如下操作：
```json
{
    "query": {
        "match_all": {}
    },
    "from": 9999,
    "size": 10
}
```
在获取第9999条到10009条数据的时候，其实每个分片都会拿到10009条数据，然后集合在一起，总共是10009*3=30027条数据，针对30027数据再次做排序处理，最终会获取最后10条数据；

如此一来，搜索得太深，就会造成性能问题，会耗费内存和占用cpu。而且es为了性能，其不支持超过一万条数据以上的分页查询。

那么如何解决深度分页带来的性能呢？其实我们应该避免深度分页操作（限制分页页数），比如最多只能提供100页的展示，从第101页开始就没了，毕竟用户也不会搜的那么深，一般也就看个10来页就顶多了；

通过设置`index.max_result_window`来突破10000数据
```json
获取配置
GET     /shop/_settings

修改配置
PUT     /shop/_settings
{ 
    "index.max_result_window": "20000"
}
```

**[scroll](https://www.elastic.co/guide/cn/elasticsearch/guide/current/scroll.html)滚动搜索**

一次性查询1万+数据，往往会造成性能影响，因为数据量太多了。这时可以使用滚动搜索，即：scroll。

滚动搜索可以先查询出一些数据，然后再紧接着依次往下查询。在第一次查询的时候会有一个滚动id，相当于一个锚标记，随后再次滚动搜索会需要上一次搜索的锚标记，根据这个进行下一次的搜索请求。每次搜索都是基于一个历史的数据快照，查询数据的期间，如果有数据变更，那么和搜索是没有关系的，搜索的内容还是快照中的数据；

scroll=1m，相当于是一个session会话时间，搜索保持的上下文时间为1分钟
```json
POST    /shop/_search?scroll=1m
{
    "query": { 
    	"match_all": {
    	}
    },  
    "sort" : ["_doc"], 
    "size":  5
}
```
上面第一次执行后，返回的数据里面有个`"_scroll_id": "1234444444=="`，滚动的id

```json
POST    /_search/scroll
{
    "scroll": "1m", 
    "scroll_id" : "1234444444=="
}
```
下一次请求时需要带上这个`scroll_id`

### 6.2、批量查询

**批量查询方式1：**
```json
POST    /shop/_doc/_search
{
    "query": {
        "ids": {
            "type" : "_doc",
            "values" : ["1002","1001"]
        }
    }
}
```

**mget方式**
```json
POST    /shop/_doc/_mget
{
    "ids": ["1002","1001"]
}
```

这两种方式对比：
- mget方式返回的数据中，只包含需要查询的数据，不包含其他多余的信息；
- mget中批量查询的数据如果不存在，返回的数据中也会包含该数据，只不过其中的found是false
    ```json
    {
        "_index": "shop",
        "_type": "_doc",
        "_id": "10022",
        "found": false
    }
    ```

### 6.3、批量操作

**基本语法**

[bulk](https://www.elastic.co/guide/cn/elasticsearch/guide/current/bulk.html)操作和以往的普通请求格式有区别。不要格式化json，不然就不在同一行了，这个需要注意，每个操作后面都有一个换行符（\n）
```json
{ action: { metadata }}\n
{ request body        }\n
{ action: { metadata }}\n
{ request body        }\n
...
```
- `{ action: { metadata }}`代表批量操作的类型，可以是新增、删除或修改
- `\n`是每行结尾必须填写的一个规范，每一行包括最后一行都要写，用于es的解析
- `{ request body }`是请求body，增加和修改操作需要，删除操作则不需要

**批量操作的类型：**

action 必须是以下选项之一：
- create：如果文档不存在，那么就创建它。存在会报错。发生异常报错不会影响其他操作。
- index：创建一个新文档或者替换一个现有的文档。
- update：部分更新一个文档。
- delete：删除一个文档

`metadata` 中需要指定要操作的文档的`_index` 、 `_type` 和 `_id`，`_index` 、 `_type`也可以在url中指定

**操作实例：**

- create新增文档数据，在metadata中指定index以及type
    ```json
    POST    /_bulk
    {"create": {"_index": "shop2", "_type": "_doc", "_id": "2001"}}
    {"id": "2001", "nickname": "name2001"}
    {"create": {"_index": "shop2", "_type": "_doc", "_id": "2002"}}
    {"id": "2002", "nickname": "name2002"}
    {"create": {"_index": "shop2", "_type": "_doc", "_id": "2003"}}
    {"id": "2003", "nickname": "name2003"}
    ```

- create创建已有id文档，在url中指定index和type
    ```json
    POST    /shop/_doc/_bulk
    {"create": {"_id": "2003"}}
    {"id": "2003", "nickname": "name2003"}
    {"create": {"_id": "2004"}}
    {"id": "2004", "nickname": "name2004"}
    {"create": {"_id": "2005"}}
    {"id": "2005", "nickname": "name2005"}
    ```

- index创建，已有文档id会被覆盖，不存在的id则新增
    ```json
    POST    /shop/_doc/_bulk
    {"index": {"_id": "2004"}}
    {"id": "2004", "nickname": "index2004"}
    {"index": {"_id": "2007"}}
    {"id": "2007", "nickname": "name2007"}
    {"index": {"_id": "2008"}}
    {"id": "2008", "nickname": "name2008"}
    ```

- update跟新部分文档数据
    ```json
    POST    /shop/_doc/_bulk
    {"update": {"_id": "2004"}}
    {"doc":{ "id": "3004"}}
    {"update": {"_id": "2007"}}
    {"doc":{ "nickname": "nameupdate"}}
    ```

- delete批量删除
    ```json
    POST    /shop/_doc/_bulk
    {"delete": {"_id": "2004"}}
    {"delete": {"_id": "2007"}}
    ```

- 综合批量各种操作
    ```json
    POST    /shop/_doc/_bulk
    {"create": {"_id": "8001"}}
    {"id": "8001", "nickname": "name8001"}
    {"update": {"_id": "2001"}}
    {"doc":{ "id": "20010"}}
    {"delete": {"_id": "2003"}}
    {"delete": {"_id": "2005"}}
    ```

## 7、Mapping

### 7.1、介绍

Mapping类似数据库中的 schema 的定义，主要作用：
- 定义索引中的字段名称
- 定义字段的数据类型，例如字符串、数字、布尔等；
- 字段，倒排索引的相关配置

Mappinhg会把json文档映射成 Lucene 所需要的扁平格式；

一个Mapping属于一个索引的 Type
- 每个文档都属于一个 Type
- 一个Type有一个Mapping定义；
- 7.0开始，不需要在Mapping定义中指定 Type信息；

字段的数据类型：
- 简单类型：
    - Text Keyword
        - text：文字类需要被分词被倒排索引的内容，比如商品名称，商品详情，商品介绍，使用text；
        - keyword：不会被分词，不会被倒排索引，直接匹配搜索，比如订单状态，用户qq，微信号，手机号等，这些精确匹配，无需分词
    - Date
    - Integer  Floating
    - Boolean
    - IPV4 & IPV6
- 复杂类型：对象和嵌套对象
- 特殊类型：geo_point & geo_shape/ percolator

### 7.2、DynamicMapping

在写入文档的时候，如果索引不存在，会自动创建索引；

DynamicMapping机制使得在创建索引的时候无需指定 Mappings，Elasticsearch 会自动根据文档信息推算出字段的类型，但是在有些情况下，推算出字段类型不对，比如地理位置信息等；

查看索引的Mapping信息
```json
GET movies/_mapping
// 结果
{
  "movies" : {
    "mappings" : {
      "_source" : {
        "excludes" : [
          "_id"
        ]
      },
      "properties" : {
        "@version" : {
          "type" : "text",
          "fields" : {
            "keyword" : {
              "type" : "keyword",
              "ignore_above" : 256
            }
          }
        },
      }
    }
  }
}
```

### 7.3、类型的自动识别

| JSON类型 | Elasticsearch类型                                            |
| -------- | ------------------------------------------------------------ |
| 字符串   | 匹配日期格式，设置成Date；<br/>配置成数字设置为float或long，该选型默认关闭 <br/>设置为text，并且增加 keyword 子字段 |
| 布尔值   | boolean                                                      |
| 浮点数   | float                                                        |
| 整数     | long                                                         |
| 对象     | Object                                                       |
| 数组     | 由第一个非空数值的类型所决定                                 |
| 空值     | 忽略                                                         |

**注意：**
- 数字或者布尔值如果用引号包裹，默认推导的类型是 TEXT
- 日期格式如：`2018-07-24T10:29:48.103Z`或者`2020-12-19T00:00:00`会推导成Date类型，但是如`2020-12-19 00:00:00` 会被推导为text

**如何更改Mapping的字段类型**：
- 新增字段：
    - dynamic 设为true时，一旦有新增字段的文档写入，Mappin同时也会被更新；
    - dynamic 设为false时，Mapping不会被更新，新增字段的数据无法被索引，但是信息会出现在 _source 中；
    - dynamic 设为strict，文档写入失败；
        ```json
        {
            "error": {
                "root_cause": [
                {
                    "type": "strict_dynamic_mapping_exception",
                    "reason": "mapping set to strict, dynamic introduction of [lastField] within [_doc] is not allowed"
                }
                ],
                "type": "strict_dynamic_mapping_exception",
                "reason": "mapping set to strict, dynamic introduction of [lastField] within [_doc] is not allowed"
            },
            "status": 400
        }
        ```
- 对已有字段，一旦已经有数据写入，就不再支持修改字段定义，Lucene实现的倒排索引，一旦生成后，就不允许修改；
- 如果希望改变字段类型，必须 reindex API，重建索引，原因是：
    - 如果修改了字段的数据类型，会导致已被索引的数据无法被搜索；
    - 但是如果是新增的字段，就不会有这样的影响；

```json
PUT moives
{
    "mapping":{
        "_doc":{
            "dynamic":"false"
        }
    }
}
```

### 7.4、自定义mappings

建议：
- 参考API手册，纯手写；
- 为了减少工作量、出错概率：
    - 创建一个临时的index，写入一些样本数据；
    - 通过访问 Mapping API 获得该临时文件的动态 Mapping 定义；
    - 修改后使用该配置创建你的索引；
    - 删除临时索引数据；

**控制当前字段是否被索引：**

index：控制当前字段是否被索引，默认为true；如果设置成false，表示该字段不可被搜索
```json
PUT users
{
    "mappings" : {
        "properties" : {
            "firstName" : {
                "type" : "text"
            },
            "lastName" : {
                "type" : "text"
            },
            "mobile" : {
                "type" : "text",
                "index": false // 如果对该字段索引，会报错：Cannot search on field [mobile] since it is not indexed.
            }
        }
    }
}
```
**index options：**四种不同级别的 index options，可以控制倒排索引记录的内容
- docs：记录doc id
- freqs：记录doc id 和 term frequencies
- positions：记录 doc id 、term frequencies、term position
- offsets：doc id 、term frequencies、term position、character offsets

Text类型默认记录 positions，其他默认为 docs，记录内容越多，占用存储空间越大；

**null_value**
- 需要对 Null 值实现搜索；
- 只有 keyword 类型支持设定 null_value
```json
PUT users
{
    "mappings" : {
      "properties" : {
        "firstName" : {
          "type" : "text"
        },
        "lastName" : {
          "type" : "text"
        },
        "mobile" : {
          "type" : "keyword",
          "null_value": "NULL"
        }

      }
    }
}
PUT users/_doc/1
{
  "firstName":"Ruan",
  "lastName": "Yiming",
  "mobile": null
}
PUT users/_doc/2
{
  "firstName":"Ruan2",
  "lastName": "Yiming2"

}

GET users/_search
{
  "query": {
    "match": {
      "mobile":"NULL"
    }
  }
}
```
**copy_to设置**
- `_all` 在 7中被`copy_to`所替代；
- 可以满足一些特定的搜索需求，`copy_to`将字段的数值拷贝到目标字典，实现类似`_all`的作用；
- `copy_to`的目标字段不出现在 _source 中
```json
PUT users
{
  "mappings": {
    "properties": {
      "firstName":{
        "type": "text",
        "copy_to": "fullName"
      },
      "lastName":{
        "type": "text",
        "copy_to": "fullName"
      }
    }
  }
}
PUT users/_doc/1
{
  "firstName":"Ruan",
  "lastName": "Yiming"
}
GET users/_search?q=fullName:(Ruan Yiming)
POST users/_search
{
  "query": {
    "match": {
       "fullName":{
        "query": "Ruan Yiming",
        "operator": "and"
      }
    }
  }
}
```
**数组类型：**
- Elasticsearch不提供专门的数组类型，但是任何字段，都可以包含多个相同类型的数值；
```json
PUT users/_doc/1
{
  "name":"onebird",
  "interests":"reading"
}
PUT users/_doc/1
{
  "name":"twobirds",
  "interests":["reading","music"]
}
POST users/_search
{
  "query": {
		"match_all": {}
	}
}
GET users/_mapping
```

- 为已存在的索引创建或创建mappings
    ```json
    POST  /index_str/_mapping
    {
        "properties": {
            "id": {
                "type": "long"
            },
            "age": {
                "type": "integer"
            },
            "nickname": {
                "type": "keyword"
            },
            "money1": {
                "type": "float"
            },
            "money2": {
                "type": "double"
            },
            "sex": {
                "type": "byte"
            },
            "score": {
                "type": "short"
            },
            "is_teenager": {
                "type": "boolean"
            },
            "birthday": {
                "type": "date"
            },
            "relationship": {
                "type": "object"
            }
        }
    }
    ```

### 7.5、多字段类型与自定义分词

- 如果要实现精准匹配：增加一个keyword字段；
- 使用不同的analyzer：
    - 不同语言
    - pinyin字段的搜索
    - 还支持位搜索和索引指定不同的 analyzer
```
PUT products
{
  "mappings": {
    "properties": {
      "company": {
        "type": "text",
        "fields": {
          "keyword": {
            "type": "keyword",
            "ignore_above": 256
          }
        }
      },
      "comment": {
        "type": "text",
        "fields": {
          "english_comment": {
            "type": "text",
            "analyzer": "english",
            "search_analyzer": "english"
          }
        }
      }
    }
  }
}
```

**Exact Values & Full Test**
- Exact Values： 包括数字/日期/具体一个字符串，在elasticsearch中使用keyword；在索引的时候，不需要做特殊的分词处理；
- 全文本，非结构化的文本数据，elasticsearch中的text；

**自定义分词：**

当elasticsearch自带的分词器无法满足时，可以自定义分词器，通过自组合不同的组件实现：
- Character Filter：在 tokenizer之前对文本进行处理，例如增加删除以及替换字符，可以配置多个 character filter。会影响 Tokenizer的 position 和 offset信息，一些自带的 Character Filters：
    - html strip：去除html标签
    - mapping：字符串替换
    - pattern replace：正在匹配替换
- Tokenizer：将原始的文本按照一定的规则，切分为词；elasticsearch内置的tokenizer：whitespace、standard、uax_url_emali、pattern、keyword、path hierarchy；另外也可以使用Java开发插件，实现自己的tokenizer；
- Token Filter：将 tokenizer输出的单词进行增加、修改、删除；自带的token filters：lowercase、stop、synonym（添加近义词）

示例：
```json
POST _analyze
{
  "tokenizer":"keyword",
  "char_filter":["html_strip"],
  "text": "<b>hello world</b>"
}
POST _analyze
{
  "tokenizer":"path_hierarchy",
  "text":"/user/ymruan/a/b/c/d/e"
}
// 使用char filter进行替换
POST _analyze
{
  "tokenizer": "standard",
  "char_filter": [
      {
        "type" : "mapping",
        "mappings" : [ "- => _"]
      }
    ],
  "text": "123-456, I-test! test-990 650-555-1234"
}
// char filter 替换表情符号
POST _analyze
{
  "tokenizer": "standard",
  "char_filter": [
      {
        "type" : "mapping",
        "mappings" : [ ":) => happy", ":( => sad"]
      }
    ],
    "text": ["I am felling :)", "Feeling :( today"]
}
// white space and snowball
GET _analyze
{
  "tokenizer": "whitespace",
  "filter": ["stop","snowball"],
  "text": ["The gilrs in China are playing this game!"]
}
// whitespace与stop
GET _analyze
{
  "tokenizer": "whitespace",
  "filter": ["stop","snowball"],
  "text": ["The rain in Spain falls mainly on the plain."]
}
//remove 加入lowercase后，The被当成 stopword删除
GET _analyze
{
  "tokenizer": "whitespace",
  "filter": ["lowercase","stop","snowball"],
  "text": ["The gilrs in China are playing this game!"]
}
//正则表达式
GET _analyze
{
  "tokenizer": "standard",
  "char_filter": [
      {
        "type" : "pattern_replace",
        "pattern" : "http://(.*)",
        "replacement" : "$1"
      }
    ],
    "text" : "http://www.elastic.co"
}
```

设置一个 Custom Analyzer

### 7.6、index template 和 dynamic template

为了管理更多的索引，管理数据，提高性能，可以创建一些模板

**index template**

帮助你设置Mappings 和Settings，并按照一定的规则，自动匹配到新创建的索引之上
- 模板仅在一个索引被新创建时才会产生作用；修改模板不会影响到已经创建的索引；
- 可以设定多个索引模板，这些设置会被merge在一起；
- 可以知道 order 数值，控制 merge 的过程
```json
PUT _template/template_default
{
  "index_patterns": ["*"],
  "order" : 0,
  "version": 1,
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas":1
  }
}
PUT /_template/template_test
{
    "index_patterns" : ["test*"],
    "order" : 1,
    "settings" : {
    	"number_of_shards": 1,
        "number_of_replicas" : 2
    },
    "mappings" : {
    	"date_detection": false,
    	"numeric_detection": true
    }
}
```

**index template工作方式**

当一个索引被创建时：
- 应该elasticsearch默认的settings 和 mappings
- 应用 order 数值第的 index template 中的设定；
- 应用 order 高的 index template 中的设定，之前的设定会被覆盖；
- 应用场景索引时，用户指定的 settings 和 mappings 会覆盖之前模板中的设定
```json
// 写入新的数据，index以test开头
PUT testtemplate/_doc/1
{
	"someNumber":"1",
	"someDate":"2019/01/01"
}
GET testtemplate/_mapping
get testtemplate/_settings
```

**dynamic template**

根据elasticsearch识别的数据类型，结合字段名称，来动态设定字段类型
- 所有的字符串类型都设定成 keyword，或者关闭 keyword字段；
- is 开头的字段都伸出boolean
- long_开头的都设置成long类型

## 8、聚合（agg）

elasticsearch除搜索外，提供了针对es数据进行统计分析的功能：实时高，不像Hadoop的（T+1）的数据；

通过聚合，会得到一个数据的概览，是分析和总结的数据，而不是寻找单个文档；

高性能，且只需要一条语句，就可以从elasticsearch得到分析的结果，无需在客户端自己去实现分析逻辑；

聚合的分类：
- Bucket Aggregation：一些列满足特定条件的文旦的集合；
- Metric Aggregation：一些数学运算，可以对文档字段进行统计分析；metric 会基于数据集计算结果，除了支持在字段上进行计算，同样也支持在脚本产生的结果之上进行计算；大多数metric是数学计算，仅输出一个值：min、max、sum、avg、cardinality
- Pipeline Aggregation：对其他的聚合结果进行二次聚合；
- Matrix Aggragation：支持对多个字段的操作并提供一个结果矩阵

示例：
```json
// 按照目的地进行分桶统计
GET kibana_sample_data_flights/_search
{
	"size": 0,
	"aggs":{
		"flight_dest":{
			"terms":{
				"field":"DestCountry"
			}
		}
	}
}
// 查看航班目的地的统计信息，增加平均，最高最低价格
GET kibana_sample_data_flights/_search
{
	"size": 0,
	"aggs":{
		"flight_dest":{
			"terms":{
				"field":"DestCountry"
			},
			"aggs":{
				"avg_price":{
					"avg":{
						"field":"AvgTicketPrice"
					}
				},
				"max_price":{
					"max":{
						"field":"AvgTicketPrice"
					}
				},
				"min_price":{
					"min":{
						"field":"AvgTicketPrice"
					}
				}
			}
		}
	}
}
// 价格统计信息+天气信息
GET kibana_sample_data_flights/_search
{
	"size": 0,
	"aggs":{
		"flight_dest":{
			"terms":{
				"field":"DestCountry"
			},
			"aggs":{
				"stats_price":{
					"stats":{
						"field":"AvgTicketPrice"
					}
				},
				"wather":{
				  "terms": {
				    "field": "DestWeather",
				    "size": 5
				  }
				}

			}
		}
	}
}

```

## 9、深入搜索

### 9.1、基于term的查询

term 的表达语义的最小单位，搜索和利用统计语言模型进行自然语言处理都需要处理term

主要特点：
- term level query： Term Query / Range Query / Exists Query / Prefix Query / Wildcard Query
- 在ES中，term 查询，对输入不做分词。会将输入作为一个整体，在倒排索引中查找准确的词项，并且使用相关度算分公式为每个包含该词项的文档进行相关度算分；
- 可以通过 Constant Score 将查询转换成一个 filtering ，避免算分，并利用缓存，提高性能；将Query转成Filter，忽略TF-IDF计算，避免相关性算分的开销；
```json
PUT products
{
  "settings": {
    "number_of_shards": 1
  }
}
POST /products/_bulk
{ "index": { "_id": 1 }}
{ "productID" : "XHDK-A-1293-#fJ3","desc":"iPhone" }
{ "index": { "_id": 2 }}
{ "productID" : "KDKE-B-9947-#kL5","desc":"iPad" }
{ "index": { "_id": 3 }}
{ "productID" : "JODL-X-1937-#pV7","desc":"MBP" }
GET /products
// 下面两个查询分别查询
POST /products/_search
{
  "query": {
    "term": {
      "desc": {
        //"value": "iPhone"
        "value":"iphone"
      }
    }
  }
}
POST /products/_search
{
  "query": {
    "term": {
      "desc.keyword": {
        "value": "iPhone"
        //"value":"iphone"
      }
    }
  }
}
// 查询不到
POST /products/_search
{
  "query": {
    "term": {
      "productID": {
        "value": "XHDK-A-1293-#fJ3"
      }
    }
  }
}
// 不要分词查询
POST /products/_search
{
  //"explain": true, // explain可以查看算分过程
  "query": {
    "term": {
      "productID.keyword": {
        "value": "XHDK-A-1293-#fJ3"
      }
    }
  }
}
// 使用 constant_score 
POST /products/_search
{
  "explain": true,
  "query": {
    "constant_score": {
      "filter": {
        "term": {
          "productID.keyword": "XHDK-A-1293-#fJ3"
        }
      }
    }
  }
}
```

### 9.2、基于全文的查询

基于全文的查询：Match Query / Match Phrase Query / Query String Quer

特点：
- 索引和搜索时都会进行分词，查询字符串先传递到一个合适的分词器，然后生成一个供查询的词项列表；
- 查询的时候，先会对输入的查询进行分词，然后每个词项逐个进行底层的查询，最终将结果进行合并；并为每个文档生成一个算分；比如查询“Matrxi reloaded” 会查询包括 Matrix 或者 Reload 的所有结果；

**Match Query Result**
```json
POST movies/_search
{
    "query":{
        "match":{
            "title":{
                "query":"Matrix reloaded"
            }
        }
    }
}
// 上述查询结果：
"hits" : [
    {
        "_index" : "movies",
        "_type" : "_doc",
        "_id" : "6365",
        "_score" : 16.501785,
        "_source" : {
            "year" : 2003,
            "genre" : [
                "Action",
                "Adventure",
                "Sci-Fi",
                "Thriller",
                "IMAX"
            ],
            "@version" : "1",
            "id" : "6365",
            "title" : "Matrix Reloaded, The"
        }
    }
]
```
**Operator**
```json
POST movies/_search
{
    "query":{
        "match":{
            "title":{
                "query":"Matrix reloaded",
                "operator": "AND"
            }
        }
    }
}
// Result结果
{
  "hits" : {
    "total" : {
      "value" : 1,
      "relation" : "eq"
    },
    "max_score" : 16.501785,
    "hits" : [
      {
        "_index" : "movies",
        "_type" : "_doc",
        "_id" : "6365",
        "_score" : 16.501785,
        "_source" : {
          "year" : 2003,
          "genre" : [
            "Action",
            "Adventure",
            "Sci-Fi",
            "Thriller",
            "IMAX"
          ],
          "@version" : "1",
          "id" : "6365",
          "title" : "Matrix Reloaded, The"
        }
      }
    ]
  }
}
```
**Minimum_should_match**
```json
POST movies/_search
{
  "profile": "true", 
  "query": {
    "match": {
      "title": {
        "query": "Matrix reloaded",
        "minimum_should_match": 2
      }
    }
  }
}
```
**match_phrase：短语匹配**
- match：分词后只要有匹配就返回；
- match_phrase：分词结果必须在text字段分词中都包含，而且顺序必须相同，而且必须都是连续的。（搜索比较严格）

其中 slop 表示允许词语间跳过的数量
```json
POST  movies/_search
{
    "query": {
        "match_phrase": {
            "title": {
                "query": "Matrix reloaded",
                "slop": 2
            }
        }
    }
}
```

**Match Query 查询过程**

![](image/Elasticsearch-MatchQuery查询过程.png)

### 9.3、结构化数据

结构化搜索是指对结构化数据的搜索，日期、布尔类型和数字都是结构化的；

文本也可以是结构化的：
- 如彩色笔可以有离散的颜色集合：红（red）、绿（green）、蓝（blue）
- 一个博客可能被标记了标签，例如，分布式（distributed）和搜索（search）
- 电商网站上的商品都有 UPCs或其他唯一的标识，都需要遵循严格的规定、结构化的格式；

es中的结构化搜索
- 布尔、时间、日期和数字这类结构化数据：有精确的格式，可以对这些格式进行逻辑操作，包括比较数字或时间的范围，或判断两个值的大小；
- 结构化的文本可以做精确匹配或者部分匹配：term查询 、Prefix前缀查询；
- 结构化结果只有`是`和`否`两个值，根据场景需要，可以决定结构化搜索是否需要打分；
- 使用exist查询处理非空 null 值

对搜索出来的结果进行数据过滤。不会到es库里去搜，不会去计算文档的相关度分数，所以过滤的性能会比较高，过滤器可以和全文搜索结合在一起使用。post_filter元素是一个顶层元素，只会对搜索结果进行过滤。不会计算数据的匹配度相关性分数，不会根据分数去排序，query则相反，会计算分数，也会按照分数去排序
- query：根据用户搜索条件检索匹配记录
- post_filter：用于查询后，对结果数据的筛选，也可以单独使用

过滤操作：
- gte：大于等于
- lte：小于等于
- gt：大于
- lt：小于

```json
// 对布尔值 match 查询，有算分
POST products/_search
{
  "profile": "true",
  "explain": true,
  "query": {
    "term": {
      "avaliable": true
    }
  }
}
// 对布尔值，通过constant score 转成 filtering，没有算分
POST products/_search
{
  "profile": "true",
  "explain": true,
  "query": {
    "constant_score": {
      "filter": {
        "term": {
          "avaliable": true
        }
      }
    }
  }
}
// 数字类型 Term
POST products/_search
{
  "profile": "true",
  "explain": true,
  "query": {
    "term": {
      "price": 30
    }
  }
}
// 数字类型 terms
POST products/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "terms": {
          "price": [
            "20",
            "30"
          ]
        }
      }
    }
  }
}
// 数字 Range 查询
GET products/_search
{
    "query" : {
        "constant_score" : {
            "filter" : {
                "range" : {
                    "price" : {
                        "gte" : 20,
                        "lte"  : 30
                    }
                }
            }
        }
    }
}
// 日期 range
POST products/_search
{
    "query" : {
        "constant_score" : {
            "filter" : {
                "range" : {
                    "date" : {
                      "gte" : "now-1y"
                    }
                }
            }
        }
    }
}
// exists查询
POST products/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "exists": {
          "field": "date"
        }
      }
    }
  }
}
POST products/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "bool": {
          "must_not": {
            "exists": {
              "field": "date"
            }
          }
        }
      }
    }
  }
}
// 处理多值字段
POST /movies/_bulk
{ "index": { "_id": 1 }}
{ "title" : "Father of the Bridge Part II","year":1995, "genre":"Comedy"}
{ "index": { "_id": 2 }}
{ "title" : "Dave","year":1993,"genre":["Comedy","Romance"] }
// 处理多值字段，term 查询是包含，而不是等于
POST movies/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "term": {
          "genre.keyword": "Comedy"
        }
      }
    }
  }
}
// 查找多个精确的值
POST products/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "terms": {
          "productID.keyword": [
            "QQPX-R-3956-#aD8",
            "JODL-X-1937-#pV7"
          ]
        }
      }
    }
  }
}
```

### 9.4、搜索算分

**相关性和相关性算分**
- 相关性（Relevance）：
    - 搜索的相关性算分，描述了一个文档和查询语句匹配的成都；ES会对每个匹配查询条件的结果进行算法 _score；
    - 打分的本质是排序，需要把最符合用户需求的文档排在最前面；ES5之前，默认的相关性算法采用的 TF-IDF，现在采用 BM25；

| 词（Term） |                       文档（Doc Id）                        |
| :--------: | :---------------------------------------------------------: |
|   大数据   |                           1，2，3                           |
|     的     | 2，3，4，5，6，7，8，11，34，45，56，67，89，90，92，93，94 |
|    应用    |                 2，3，4，5，89，92，93，94                  |

**词频：TF**
- Term Frequency：检索词在一篇文档中出现的频率（检索词出现的次数除以文档的总字数）；
- 度量一条查询和结果文档的相关性简单方法：简单将搜索中每一个词的TF进行相加（TF(大数据) + TF(的) + TF(应用)）
- Stop Word：`的`在文档中出现了很多次，但是对贡献相关度几乎没有用处，不应该考虑他的 TF

**逆文档频率：IDF**
- IDF：检索词在所有文档中出现的频率：
    - `大数据`在相对比较少的文档中出现；
    - `应用`在相对比较多的文档中出现；
    - `Stop Word`在大量的文档中出现；
- Inverse Document Frequency：简单说=log(全部文档数/检索词出现过的文档总数)
- TF-IDF 本质上就是将 TF 求和变成了加权求和： `TF(大数据)*IDF(大数据) + TF(的)*IDF(的) + TF(应用)*IDF(应用)`

    |        | 出现的文档数 | 总文档数 |      IDF      |
    | :----: | :----------: | :------: | :-----------: |
    | 大数据 |    200万     |   10亿   | log(500)=8.96 |
    |   的   |     10亿     |   10亿   |   log(1)=0    |
    |  应用  |     5亿      |   10亿   |   log(2)=1    |

**TF-IDF概念**：被公认为信息检索领域最重要的发明

Lucene中 TF-IDF的评分公式：

![](image/Elasticsearch-Lucene-TF-IDF评分公式.png)

**BM 25**
- 从ES5开始，默认改为`BM 25`；
- 和经典的TF-IDF相比，当TF无限增加时，`BM 25`算分会趋于一个数值

![](image/Elasticsearch-BM25.png)

可以通过explain API查看 TF-IDF
```json

```

**Boosting Revelance**
- Boosting是控制相关度的一种手段：索引、字段或查询子条件；
- 参数 boost的含义：
    - 当 `boost > 1` 时，打分的相关度相对性提升；
    - 当 `0 < boost < 1` 时，打分的权重相对性降低；
    - 当 `boost < 0` 时，贡献负分
```json
POST     /shop/_doc/_search
{
    "query": {
        "multi_match": {
                "query": "皮特帕克慕课网",
                "fields": ["desc", "nickname^10"]

        }
    }
}
```
`nickname^10` 代表搜索提升10倍相关性，也就是说用户搜索的时候其实以这个nickname为主，desc为辅，nickname的匹配相关度当然要提高权重比例了

## 8、Elasticsearch集群

### 8.1、集群概念

分片机制：每个索引可以被分片
- 副本分片是主分片的备份，主挂了，备份还是可以访问，这就需要用到集群了。
- 同一个分片的主与副本是不会放在同一个服务器里的，因为一旦宕机，这个分片就没了

docker-compose配置集群： https://juejin.im/post/6844903682950037518

### 8.2、集群安装es

在配置文件`conf/elasticsearch.yml`配置如下：
```yml
# 配置集群名称，保证每个节点的名称相同，如此就能都处于一个集群之内了
cluster.name: imooc-es-cluster
# 每一个节点的名称，必须不一样
node.name: es-node1
# http端口（使用默认即可）
http.port: 9200
# 主节点，作用主要是用于来管理整个集群，负责创建或删除索引，管理其他非master节点，表示当前节点是否可以为master
node.master: true
# 数据节点，用于对文档数据的增删改查
node.data: true
# 集群列表
discovery.seed_hosts: ["192.168.89.161", "192.168.89.162", "192.168.89.163"]
# 启动的时候使用一个master节点
cluster.initial_master_nodes: ["es-node1"]
```
比如我这里准备三台机器："192.168.89.161", "192.168.89.162", "192.168.89.163"，启动后，节点如下：

![](image/es-cluster.png)


集群和节点：节点(node)是一个运行着的Elasticsearch实例。集群(cluster)是一组具有相同cluster.name的节点集合，他们协同工作，共享数据并提供故障转移和扩展功能，当然一个节点也可以组成一个集群

### 8.3、

## 9、ElasticSearch分布式架构原理

Elasticsearch设计的理念就是分布式搜索引擎，底层其实还是基于lucene的。其核心思想就是在多台机器上启动多个es进程实例，组成了一个es集群。
- ES中存储数据的基本单位是索引：index
- 一个index里面会有多个type，每个type可以认为是一个mysql中的表
- 每个type有一个mapping。mapping代表了这个type的表结构的定义，定义了这个type中每个字段名称，字段是什么类型的，然后还有这个字段的各种配置；
- 往index里的一个type里面写的一条数据，叫做一条document，一条document就代表了mysql中某个表里的一行给，每个document有多个field，每个field就代表了这个document中的一个字段的值；
- 索引可以拆分成多个shard，每个shard存储部分数据；shard的数据实际是有多个备份，就是说每个shard都有一个primary shard，负责写入数据，但是还有几个replica shard。primary shard写入数据之后，会将数据同步到其他几个replica shard上去；
- es集群多个节点，会自动选举一个节点为master节点，这个master节点其实就是干一些管理的工作的，比如维护索引元数据拉，负责切换 primary shard（主分片）和 replica shard（备份）身份，之类的。要是master节点宕机了，那么会重新选举一个节点为master节点

## 10、mysql与ElasticSearch数据同步

使用开源中间件
- binlog订阅：
    - [canal](https://github.com/alibaba/canal)
    - go-mysql-elasticsearch
    - mysql-binlog-connector-java： 监听binlog
- logstash

# 三、ElasticSearch整合SpringBoot

# 四、Kibana

# 五、Logstash


# 面试题

https://zhuanlan.zhihu.com/p/102500311

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

* [Elasticsearch下载](https://www.elastic.co/cn/downloads/past-releases#elasticsearch)
* [Elasticsearch官方文档](https://www.elastic.co/guide/en/elasticsearch/reference/7.1/index.html)
* [What is Elasticsearch](https://www.elastic.co/cn/what-is/elasticsearch)
* [Springboot+Elasticsearch](https://docs.spring.io/spring-data/elasticsearch/docs/)
* [ElasticSearch父子文档](https://blog.csdn.net/laoyang360/article/details/82950393)
* [ElasticSearch脑图](https://juejin.cn/post/6898505457485217806)
* [Elasticsearch时间指南](https://juejin.cn/post/6898926871191224334)