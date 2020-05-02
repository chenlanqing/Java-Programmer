
# 1、Hbase概述

## 1.1、概念

特点：
- HBase单表可以有百亿行、百万列，数据矩阵横向和纵向两个维度所支持的数据量级都非常具有弹性；
- HBase是面向列的存储和权限控制，并支持独立检索；列式存储，其数据在表中是按照某列式存储的，在查询时只需要少数几个字段的时候；
- 多版本：HBase每一个列的数据存储有多个version；
- 稀疏性：为空的列并不占用存储空间，表可以设计的非常稀疏；
- 扩展性：底层依赖于HDFS
- 高可靠性：WAL机制保障了数据写入时不会因集群异常而导致写入数据丢失：Replication 机制保证了在集群出现严重的问题时，数据不会丢失或损坏；
- 高性能：底层的LSM数据结构和RowKey有序排序等架构上的独特设计，使得HBase具有非常高的写入性能。region切分、主键索引和缓存机制使得HBase在海量数据下具备一定的随机读取性能，该性能针对RowKey的查询能够达到毫秒级；

## 1.2、应用场景

主要特点：
- 海量数据存储
- 准实时查询

实际业务使用场景：
- 交通，比如gps信息
- 金融数据
- 电商数据
- 移动数据

## 1.3、架构体系与设计模型

### 1.3.1、架构体系


### 1.3.2、设计模型

**表结构模型：**


**数据模型：**

一张表的列簇不会超过5个，每个列簇中的列数没有限制，列在列簇中是有序的



# 2、HBase安装

## 2.1、版本选择

选择合适的HBase版本，主要版本类型：
- 官网版本：http://archive.apache.org/dist/hbase/
- CDH版本：http://archive.cloudera.com/cdh5/cdh/5/

## 2.2、前提条件

- JDK1.7以上
- Hadoop-2.5.0以上
- zookeeper-3.4.5以上

## 2.3、Hadoop2.x分布式安装配置

- 解压 Hadoop-2.5.0
- 配置 `etc/hadoop/hadoop-env.sh`：配置 `export JAVA_HOME=/usr/java/jdk1.8.0_231-amd64`，即设置Java的安装目录；
- 配置文件 `etc/hadoop/hdfs-site.xml`：
    ```xml
    <configuration>
        <property>
            <name>dfs.replication</name>
            <value>1</value>
        </property>
        <property>
            <name>dfs.permissions.enabled</name>
            <value>false</value>
        </property>
    </configuration>
    ```
- 配置文件 `etc/hadoop/core-site.xml`：
    ```xml
    <configuration>
        <property>
            <name>fs.defaultFS</name>
            <!-- hdfs的目录地址，这里配置的是hostname:port  -->
            <value>hdfs://bigdata1:9000</value>
        </property>
        <property>
            <name>hadoop.tmp.dir</name>
            <!-- hadoop的临时目录 -->
            <value>/opt/modules/hadoop-2.5.0/data/tmp</value>
        </property>
    </configuration>
    ```
- 配置文件 `etc/hadoop/slaves`：单机的话，配置当前机器的 hostname 即可；
- 格式化hdfs：`bin/hdfs namenode -format`
- 启动Hadoop：
    - 启动namnode： `sbin/hadoop-daemon.sh start namenode`
    - 启动datanode： `sbin/hadoop-daemon.sh start datanode`

## 2.4、配置zookeeper集群

hbase 依赖zookeeper，一般不使用hbase内置的zookeeper

[配置zookeeper集群](../Java/Java架构/分布式.md#71集群环境)

## 2.5、配置hbase

- 解压文件
- 修改`conf/hbase-env.sh`配置文件
    - 配置JDK目录：JAVA_HOME=${JAVA_HOME}
    - 配置不使用默认的zookeeper： HBASE_MANAGES_ZK=false
- 配置`conf/hbase-site.xml`文件
    ```xml
    <configuration>
        <property>
            <name>hbase.tmp.dir</name>
            <value>/opt/modules/hbase-0.98.6-cdh5.3.0/data/tmp</value>
        </property>
        <property>
            <name>hbase.rootdir</name>
            <value>hdfs://bigdata1:9000/hbase</value>
        </property>
        <property>
            <name>hbase.cluster.distributed</name>
            <value>true</value>
        </property>
        <property>
            <name>hbase.zookeeper.quorum</name>
            <value>zookeeper-node-1,zookeeper-node-2,zookeeper-node-3</value>
        </property>
    </configuration>
    ```
- 配置 `conf/regionservers`文件：一般是使用主机名
- 启动hbase：`bin/start-hbase.sh`

其web页面的默认端口是： 60010

# 3、HBase Shell操作



# 参考资料

- [HBase官网](http://hbase.apache.org/)

