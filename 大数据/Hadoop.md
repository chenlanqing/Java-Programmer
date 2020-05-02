
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

# 2、Hadoop安装

## 2.1、伪集群安装

**环境**
- 系统：CentOS7.4
- Hadoop版本：3.2.0
- JDK版本：1.8.0_231
- 机器IP，静态地址：192.168.89.141
- hostname：hadoop001
- 免密SSH登录

**配置步骤**
- 解压缩 hadoop.3.2.tar.gz 到目录：`/data/soft/`
- 创建目录：`mkdir -p /data/hadoop_repo/logs/hadoop`
- 修改 `etc/hadoop-env.sh`配置，增加环境变量信息：
    ```
    export JAVA_HOME=/usr/java/jdk1.8.0_231-amd64
    export HADOOP_LOG_DIR=/data/hadoop_repo/logs/hadoop
    ```
- 修改 `etc/core-site.xml`，注意 `fs.defaultFS` 属性中的主机名需要和你配置的主机名保持一致：
    ```xml
    <configuration>
        <property>
            <name>fs.defaultFS</name>
            <value>hdfs://hadoop001:9000</value>
        </property>
        <property>
            <name>hadoop.tmp.dir</name>
            <value>/data/hadoop_repo</value>
        </property>
    </configuration>
    ```
- 修改 `etc/hdfs-site.xml`，把 hdfs 中文件副本的数量设置为 1，因为现在伪分布集群只有一 个节点
    ```xml
    <configuration>
        <property>
            <name>dfs.replication</name>
            <value>1</value>
        </property>
    </configuration>
    ```    

- 修改 `etc/mapred-site.xml`，设置 mapreduce 使用的资源调度框架
    ```xml
    <configuration>
        <property>
            <name>mapreduce.framework.name</name>
            <value>yarn</value>
        </property>
    </configuration>
    ```

- 修改 `etc/yarn-site.xml`，设置 yarn 上支持运行的服务和环境变量白名单
    ```xml
    <configuration>
        <property>
            <name>yarn.nodemanager.aux-services</name>
            <value>mapreduce_shuffle</value>
        </property>
        <property>
            <name>yarn.nodemanager.env-whitelist</name>
            <value>JAVA_HOME,HADOOP_COMMON_HOME,HADOOP_HDFS_HOME,HADOOP_CONF_DIR,CL ASSPATH_PREPEND_DISTCACHE,HADOOP_YARN_HOME,HADOOP_MAPRED_HOME</value>
        </property>
    </configuration>
    ```
- 格式化 namenode： `bin/hdfs namenode -format`

    如果在后面的日志信息中能看到这一行，则说明 namenode 格式化成功。 `common.Storage: Storage directory /data/hadoop_repo/dfs/name has been successfully formatted.`
- 启动hadoop：`sbin/start-all.sh`，直接启动会报错，报错信息：
    ```
    [root@hadoop001 hadoop-3.2.0]# sbin/start-all.sh
    ERROR: Attempting to operate on hdfs namenode as root
    ERROR: but there is no HDFS_NAMENODE_USER defined. Aborting operation.
    Starting datanodes
    ERROR: Attempting to operate on hdfs datanode as root
    ERROR: but there is no HDFS_DATANODE_USER defined. Aborting operation.
    Starting secondary namenodes [hadoop100]
    ERROR: Attempting to operate on hdfs secondarynamenode as root
    ERROR: but there is no HDFS_SECONDARYNAMENODE_USER defined. Aborting operation. 2019-07-25 10:04:25,993 WARN util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
    Starting resourcemanager
    ERROR: Attempting to operate on yarn resourcemanager as root
    ERROR: but there is no YARN_RESOURCEMANAGER_USER defined. Aborting operation.
    Starting nodemanagers
    ERROR: Attempting to operate on yarn nodemanager as root
    ERROR: but there is no YARN_NODEMANAGER_USER defined. Aborting operation
    ```
- 修改脚本：`sbin/start-dfs.sh, sbin/stop-dfs.sh`，增加如下内容：
    ```
    HDFS_DATANODE_USER=root
    HDFS_DATANODE_SECURE_USER=hdfs
    HDFS_NAMENODE_USER=root
    HDFS_SECONDARYNAMENODE_USER=root
    ```

- 修改脚本：`sbin/start-yarn.sh, sbin/stop-yarn.sh`，增加如下内容：
    ```
    YARN_RESOURCEMANAGER_USER=root
    HADOOP_SECURE_DN_USER=yarn
    YARN_NODEMANAGER_USER=root
    ```
- 启动集群：`sbin/start-all.sh`
- 验证集群是否正常：`jps`，或者通过页面访问
    ```
    # 执行 jps 命令可以查看集群的进程信息，抛出 Jps 这个进程之外还需要有 5 个进程才说明 集群是正常启动的
    [root@hadoop001 sbin]# jps
    2882 ResourceManager
    2420 DataNode
    3365 Jps
    2619 SecondaryNameNode
    2315 NameNode
    2988 NodeManager
    ```
    还可以通过 webui 界面来验证集群服务是否正常:
    - hdfs webui 界面: http://192.168.89.141:9870
    - yarn webui 界面: http://192.168.89.141:8088

## 2.2、集群部署安装

**环境**
- 系统：CentOS7.4
- Hadoop版本：3.2.0
- JDK版本：1.8.0_231
- 三台机器：
    - 192.168.89.141 hadoop001（主）
    - 192.168.89.142 hadoop002
    - 192.168.89.143 hadoop003
- 免密SSH登录，需要hadoop001能够免密登录hadoop002、hadoop003

**配置步骤**
- 解压缩 hadoop.3.2.tar.gz 到目录：`/data/soft/`
- 创建目录：`mkdir -p /data/hadoop_repo/logs/hadoop`
- 修改 `etc/hadoop-env.sh`配置，增加环境变量信息：
    ```
    export JAVA_HOME=/usr/java/jdk1.8.0_231-amd64
    export HADOOP_LOG_DIR=/data/hadoop_repo/logs/hadoop
    ```
- 修改 `etc/core-site.xml`，注意 `fs.defaultFS` 属性中的主机名需要和你配置的主机名保持一致：
    ```xml
    <configuration>
        <property>
            <name>fs.defaultFS</name>
            <value>hdfs://hadoop001:9000</value>
        </property>
        <property>
            <name>hadoop.tmp.dir</name>
            <value>/data/hadoop_repo</value>
        </property>
    </configuration>
    ```
- 修改 `etc/hdfs-site.xml`，把 hdfs 中文件副本的数量设置为 2，小于集群的节点数
    ```xml
    <configuration>
        <property>
            <name>dfs.replication</name>
            <value>2</value>
        </property>
        <property>
            <name>dfs.namenode.secondary.http-address</name>
            <value>hadoop001:50090</value>
        </property>
    </configuration>
    ```    
- 修改 `etc/mapred-site.xml`，设置 mapreduce 使用的资源调度框架
    ```xml
    <configuration>
        <property>
            <name>mapreduce.framework.name</name>
            <value>yarn</value>
        </property>
    </configuration>
    ```
- 修改 `etc/yarn-site.xml`，设置 yarn 上支持运行的服务和环境变量白名单，`yarn.resourcemanager.hostname` 配置主节点
    ```xml
    <configuration>
        <property>
            <name>yarn.nodemanager.aux-services</name>
            <value>mapreduce_shuffle</value>
        </property>
        <property>
            <name>yarn.nodemanager.env-whitelist</name>
            <value>JAVA_HOME,HADOOP_COMMON_HOME,HADOOP_HDFS_HOME,HADOOP_CONF_DIR,CL ASSPATH_PREPEND_DISTCACHE,HADOOP_YARN_HOME,HADOOP_MAPRED_HOME</value>
        </property>
        <property>
            <name>yarn.resourcemanager.hostname</name>
            <value>hadoop001</value>
        </property>
    </configuration>
    ```
- 修改 `etc/workers` 文件，增加所有从节点的主机名，一个一行
    ```
    hadoop002
    hadoop003
    ```
- 修改脚本：`sbin/start-dfs.sh, sbin/stop-dfs.sh`，增加如下内容：
    ```
    HDFS_DATANODE_USER=root
    HDFS_DATANODE_SECURE_USER=hdfs
    HDFS_NAMENODE_USER=root
    HDFS_SECONDARYNAMENODE_USER=root
    ```
- 修改脚本：`sbin/start-yarn.sh, sbin/stop-yarn.sh`，增加如下内容：
    ```
    YARN_RESOURCEMANAGER_USER=root
    HADOOP_SECURE_DN_USER=yarn
    YARN_NODEMANAGER_USER=root
    ```
- 回到hadoop安装包的上级目录，把 `hadoop001` 节点上修改好配置的安装包拷贝到其他两个从节点：
    ```
    [root@hadoop100 soft]# scp -rq hadoop-3.2.0 hadoop002:/data/soft/ 
    [root@hadoop100 soft]# scp -rq hadoop-3.2.0 hadoop003:/data/soft/
    ```
- 在`hadoop001`节点上格式化 namenode：`bin/hdfs namenode -format`
- 启动集群，在 hadoop001 节点上执行下面命令：`sbin/start-all.sh`，启动和停止都只需要在 hadoop001 节点上操作
    ```
    [root@hadoop001 hadoop-3.2.0]# sbin/start-all.sh
    Starting namenodes on [hadoop001]
    Starting datanodes
    Starting secondary namenodes [hadoop001]
    Starting resourcemanager
    Starting nodemanagers
    ```
- 验证集群：分别在三台机器上执行jps命令：
    ```
    [root@hadoop001 hadoop-3.2.0]# jps
    10627 NameNode
    10900 SecondaryNameNode
    11480 Jps
    11147 ResourceManager

    [root@hadoop002 hadoop-3.2.0]# jps
    2066 DataNode
    2184 NodeManager
    2286 Jps

    [root@hadoop003 hadoop-3.2.0]# jps
    2113 Jps
    1890 DataNode
    2008 NodeManager
    ```

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

# 4、Mapreduce

## 4.1、概述

MapReduce是一个编程模型，主要负责海量数据计算，主要由两个阶段Map和Reduce阶段

其使用的是分而治之的思想，基本概念：
- Job & JobTask
- JobTracker：作业调度、分配任务、监控任务执行进度；监控TaskTracker的状态
- TaskTracker：执行任务，汇报任务状态

## 4.2、架构

# 5、Yarn

## 5.1、Yarn概述

主要负责集群资源的管理和调度


# 参考资料

- [Hadoop官方](http://hadoop.apache.org/)
- [Hadoop发展](https://www.infoq.cn/article/hadoop-ten-years-interpretation-and-development-forecast)
- [Hadoop CDH 版本下载](http://archive.cloudera.com/cdh5/cdh/5/)
