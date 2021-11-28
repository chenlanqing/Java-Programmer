# 一、安装MySQL数据库

[CentOS7.2使用yum安装MYSQL5.7.10](https://typecodes.com/linux/yuminstallmysql5710.html)

## 1、CentOS7安装mysql版本数据

### 1.1、环境

centOS7，查看Linux发行版本
```
[root@localhost mysql]# cat /etc/redhat-release 
CentOS Linux release 7.0.1406 (Core) 
```

### 1.2、下载MySQL官方的Yum Repository

根据Linux发行版本（CentOS、Fedora都属于红帽系），从[mysql官方](http://dev.mysql.com/downloads/repo/yum/)获取Yum Repository
```
[root@localhost mysql]# wget -i http://dev.mysql.com/get/mysql57-community-release-el7-7.noarch.rpm
--2018-06-04 11:51:39--  http://dev.mysql.com/get/mysql57-community-release-el7-7.noarch.rpm
正在解析主机 dev.mysql.com (dev.mysql.com)... 137.254.60.11
正在连接 dev.mysql.com (dev.mysql.com)|137.254.60.11|:80... 已连接。
已发出 HTTP 请求，正在等待回应... 301 Moved Permanently
位置：https://dev.mysql.com/get/mysql57-community-release-el7-7.noarch.rpm [跟随至新的 URL]
--2018-06-04 11:51:40--  https://dev.mysql.com/get/mysql57-community-release-el7-7.noarch.rpm
正在连接 dev.mysql.com (dev.mysql.com)|137.254.60.11|:443... 已连接。
已发出 HTTP 请求，正在等待回应... 302 Found
位置：https://repo.mysql.com//mysql57-community-release-el7-7.noarch.rpm [跟随至新的 URL]
...
```

### 1.3、安装MySQL的Yum Repository

安装完MySQL的Yum Repository，每次执行yum update都会检查MySQL是否更新
```
[root@localhost mysql]# yum -y install mysql57-community-release-el7-7.noarch.rpm
已加载插件：fastestmirror
正在检查 mysql57-community-release-el7-7.noarch.rpm: mysql57-community-release-el7-7.noarch
mysql57-community-release-el7-7.noarch.rpm 将被安装
正在解决依赖关系
--> 正在检查事务
---> 软件包 mysql57-community-release.noarch.0.el7-7 将被 安装
--> 解决依赖关系完成
...
```

### 1.4、安装MySQL数据库的服务器版本

```
[root@localhost mysql]# yum -y install mysql-community-server
已加载插件：fastestmirror
base                                                                                  | 3.6 kB  00:00:00 
extras                                                                                | 3.4 kB  00:00:00     
mysql-connectors-community                                                            | 2.5 kB  00:00:00     
mysql-tools-community                                                                 | 2.5 kB  00:00:00     
mysql57-community                                                                     | 2.5 kB  00:00:00     
updates                                                                               | 3.4 kB  00:00:00  
...
```

### 1.5、启动数据库

```
[root@localhost mysql]# systemctl start mysqld.service
```
然后使用命令 systemctl status mysqld.service 查看MySQL数据库启动后的服务状态

### 1.6、获取初始密码

使用YUM安装并启动MySQL服务后，MySQL进程会自动在进程日志中打印root用户的初始密码
```
[root@localhost mysql]# grep "password" /var/log/mysqld.log 
2018-06-04T06:30:41.267699Z 1 [Note] A temporary password is generated for root@localhost: _)srryI*d2zX
2018-06-04T06:30:42.150462Z 1 [ERROR] Failed to open the bootstrap file /var/lib/mysql-files/install-validate-password-plugin.Zf9heV.sql
```

### 1.7、修改root用户密码

进入mysql控制台
```sql
ALTER USER 'root'@'localhost' IDENTIFIED with mysql_native_password BY '123456';
```
如果上述命令报如下错误信息：
```
ERROR 1819 (HY000): Your password does not satisfy the current policy requirements
```
可以修改其密码策略：
```sql
-- 查看密码策略
SHOW VARIABLES LIKE 'validate_password%';
-- 首先需要设置密码的验证强度等级，设置 validate_password_policy 的全局参数为 LOW 即可
set global validate_password_policy=LOW; 
-- 当前密码长度为 8 ，如果不介意的话就不用修改了，按照通用的来讲，设置为 6 位的密码，设置 validate_password_length 的全局参数为 6
set global validate_password_length=6;
```

**关于mysql密码策略相关参数：**
```
1）、validate_password_length  固定密码的总长度；
2）、validate_password_dictionary_file 指定密码验证的文件路径；
3）、validate_password_mixed_case_count  整个密码中至少要包含大/小写字母的总个数；
4）、validate_password_number_count  整个密码中至少要包含阿拉伯数字的个数；
5）、validate_password_policy 指定密码的强度验证等级，默认为 MEDIUM；
关于 validate_password_policy 的取值：
0/LOW：只验证长度；
1/MEDIUM：验证长度、数字、大小写、特殊字符；
2/STRONG：验证长度、数字、大小写、特殊字符、字典文件；
6）、validate_password_special_char_count 整个密码中至少要包含特殊字符的个数；
```

新增用户：
```sql
create user 'test'@'%' identified with mysql_native_password by '123456';
```
这里使用mysql_native_password是因为mysql8使用了新的加密方式，如果我们需要使用老的加密方式来连接，需要加上 mysql_native_password
```
授权：grant all on *.* to 'test'@'%'
```

### 1.8、查看MySQL数据库的配置信息

MySQL的配置文件依然是`/etc/my.cnf`，其它安装信息可以通过mysql_config命令查看。其中，动态库文件存放在`/usr/lib64/mysql`目录下

### 1.9、解决外部无法连接mysql

- （1）查看是否打开3306端口：

    `vi /etc/sysconfig/iptables`

    增加下面一行：

    `-A RH-Firewall-1-INPUT -m state --state NEW -m tcp -p tcp --dport 3306-j ACCEPT`
    ```
    开放端口：
    firewall-cmd --zone=public --add-port=3306/tcp --permanent
    firewall-cmd --reload
    ```
- （2）重启防火墙

    `systemctl restart iptables`

    如果上述命令报错：`Unit iptables.service failed to load`，因为在CentOS 7或RHEL 7或Fedora中防火墙由firewalld来管理，执行如下命令：
    ```
    [root@localhost ~]# systemctl stop firewalld
    [root@localhost ~]# systemctl mask firewalld
    ln -s '/dev/null' '/etc/systemd/system/firewalld.service'
    [root@localhost ~]# yum install iptables-services
    ```
    设置开启启动：`systemctl enable iptables`

- （3）授权支持远程登录
    - 支持所有用户远程连接：允许远程主机以 root 账号（密码是root）连接数据库
        ```sql
        grant all privileges on *.* to 'root'@'%' identified by '123456' with grant option;
        ```
    - 如果只允许某台主机连接root从1.1.1.1的主机连接到服务器
        ```sql
        grant all privileges on *.* to 'root'@'127.0.0.1' identified by 'root' with grant option;
        ```

**MySQL8支持远程访问：**
- 登录mysql，切换数据库为mysql：`use mysql`；
- 更新域属性，'%'表示允许外部访问：`update user set host='%' where user ='root';`
- 执行以上语句之后再执行：`FLUSH PRIVILEGES;`
- 再执行授权语句：`GRANT ALL PRIVILEGES ON *.* TO 'root'@'%'WITH GRANT OPTION;`

### 1.10、删除MySQL的Repository

因为安装了MySQL的Yum Repository，所以以后每次执行yum操作时，都会去检查更新。如果想要去掉这种自动检查操作的话，可以使用如下命令卸载MySQL的Repository即可

`[root@localhost mysql]# yum -y remove mysql57-community-release-el7-7.noarch.rpm`

## 2、安装MariaDB数据库

### 2.1、下载安装包

这里安装的是 `MariaDB10.4.8` 版本
```
galera-4-26.4.2-1.rhel7.el7.centos.x86_64.rpm  
jemalloc-3.6.0-1.el7.x86_64.rpm                
jemalloc-devel-3.6.0-1.el7.x86_64.rpm          
MariaDB-client-10.4.8-1.el7.centos.x86_64.rpm
MariaDB-common-10.4.8-1.el7.centos.x86_64.rpm
MariaDB-compat-10.4.8-1.el7.centos.x86_64.rpm
MariaDB-server-10.4.8-1.el7.centos.x86_64.rpm
```

### 2.2、安装依赖包

`yum install rsync nmap lsof perl-DBI nc`

`rpm -ivh jemalloc-3.6.0-1.el7.x86_64.rpm`

`rpm -ivh jemalloc-devel-3.6.0-1.el7.x86_64.rpm`

### 2.3、卸载冲突的mariadb-libs

搜索：`rpm -qa | grep mariadb-libs`

如果有，比如如下，删除即可：`rpm -ev --nodeps mariadb-libs-5.5.60-1.el7_5.x86_64`

### 2.4、安装 boost-devel 依赖环境

`yum install boost-devel.x86_64`

### 2.5、导入MariaDB的key

`rpm --import http://yum.mariadb.org/RPM-GPG-KEY-MariaDB`

### 2.6、安装 galera 环境

`rpm -ivh galera-4-26.4.2-1.rhel7.el7.centos.x86_64.rpm`

### 2.7、安装MariaDB的4个核心包

注意：如果是MariaDB10.4.8需要安装 libaio
```
wget http://mirror.centos.org/centos/6/os/x86_64/Packages/libaio-0.3.107-10.el6.x86_64.rpm
rpm -ivh libaio-0.3.107-10.el6.x86_64.rpm
```
安装MariaDB的4个核心包：

`rpm -ivh MariaDB-common-10.4.7-1.el7.centos.x86_64.rpm MariaDB-compat-10.4.7-1.el7.centos.x86_64.rpm MariaDB-client-10.4.7-1.el7.centos.x86_64.rpm MariaDB-server-10.4.7-1.el7.centos.x86_64.rpm`

启动MariaDB：`service mysql start`

### 2.8、启动后配置：

启动成功后运行如下命令进行安全配置：`mysql_secure_installation`

进行如下配置：
```
1.输入当前密码，初次安装后是没有密码的，直接回车
2.询问是否使用`unix_socket`进行身份验证：n
3.为root设置密码：y
4.输入root的新密码：root
5.确认输入root的新密码：root
6.是否移除匿名用户，这个随意，建议删除：y
7.拒绝用户远程登录，这个建议开启：n
8.删除test库，可以保留：n
9.重新加载权限表：y
```

### 2.9、配置远程连接

```
grant all privileges on *.* to 'root'@'%' identified by 'root密码';
flush privileges;
```

## 3、docker中安装mysql

直接通过docker运行mysql镜像：`docker run -d -p 3306:3306 --name mysql -e MYSQL_ROOT_PASSWORD=123456 mysql`，如果没有指定版本，默认是最新的版本

mysql目前最新版是8.0，启动mysql后，通过IDE工具无法连接mysql，因为mysql8用的加密方式是不一样的，可以通过如下操作解决：
- 修改加密规则：`ALTER USER 'root'@'%' IDENTIFIED BY '123456' PASSWORD EXPIRE NEVER;`
- 更新一下用户的密码：`ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '123456';`
- 刷新权限：`flush privileges;`

# 二、MySQL主从复制

主要步骤：
- 配置主从数据库服务器参数；
- 在Master服务器上创建用于复制的数据库账号；
- 备份Master服务器上的数据并初始化Salve服务器数据；
- 启动复制链路；

## 1、环境准备

- MySQL版本：5.7.30，可以登录到mysql中，使用`select @@version;` 查看版本号；
- 两台服务器：CentOS7，分配如下：
    - Master:  192.168.89.141
    - Slave:   192.168.89.142

## 2、配置主服务器

- 修改`/etc/my.cnf`配置文件：
    - 开启binlog，配置log-bin；
    - 修改server-id；

    基本配置如下：
    ```cnf
    log-bin=mysql-bin
    server-id=141
    ```
- 创建用于复制的账号：
    ```
    create user 'repl'@'192.168.89.%' identified by '123456';
    grant replication slave on *.* to 'repl'@'192.168.89.%';
    ```
- 创建日志点，将当前master数据导出
    ```
    mysqldump -uroot -p --single-transaction --master-data --triggers --routines --all-databases > all.sql
    ```
- 将all.sql拷贝到Slave服务器上：`scp all.sql root@192.168.89.142:/root/software/`

## 3、配置从服务器

- 修改server-id，可以按照ip地址后缀
- 启用read_only = on
- 将之前从主库中拷贝过来的all.sql文件导入到从库中：`mysql -uroot -p < all.sql`
- 设置主从关系：`change master to master_host='192.168.89.141',master_user='repl',master_password='123456',MASTER_LOG_FILE='mysql-bin.000001',MASTER_LOG_POS=617;`

    其中 MASTER_LOG_FILE、MASTER_LOG_POS，可以从all.sql查看到，或者可以再主库上执行：
    ```
    mysql> SHOW MASTER STATUS;
    +------------------+----------+--------------+------------------+-------------------+
    | File             | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
    +------------------+----------+--------------+------------------+-------------------+
    | mysql-bin.000001 |      617 |              |                  |                   |
    +------------------+----------+--------------+------------------+-------------------+
    ```
- 启动：`start salves;`
- 查看状态：`show slave status;`

# 三、Redis安装

环境：centos7

## 1、安装

- 从官方下载包：https://redis.io/download

- 将其上传到Linux服务器上

- 解压redis：`tar -zxvf redis-5.0.5.tar.gz`；

- 安装gcc编译环境：`yum install gcc-c++`；

- 进入解压缩目录，这里是`redis-5.0.5`，执行：`make && make install`；

## 2、配置

- 将`redis-5.0.5/utils/redis_init_script`拷贝到`/etc/init.d`，作为开机启动的服务；

- 创建 `/usr/local/redis`，用于存放配置文件；

- 将 `redis-5.0.5/redis.conf`文件拷贝到 `/usr/local/redis` 目录下；

- 修改配置文件：`/usr/local/redis/redis.conf`

    - 修改 `daemonize no` -> `daemonize yes`，目的是为了让redis启动在linux后台运行 ；
    - 修改redis的工作目录：`dir /usr/local/redis/working`；
    - 修改`bind`，绑定IP改为 `0.0.0.0` ，代表可以让远程连接，不收ip限制；
    - 最关键的是密码，默认是没有的，一定要设置：`requirepass 123456`

- 修改 `/etc/init.d/redis_init_script` 文件中的redis核心配置文件为如下：`CONF="/usr/local/redis/${REDISPORT}.conf"`，其中REDISPORT变量为redis的端口号6379；

- 将`/usr/local/redis/redis.conf`文件名称为：`6379.conf`

- 为redis启动脚本添加执行权限，随后运行启动redis：`/etc/init.d/redis_init_script start`；

- 设置redis开机自启动，修改 redis_init_script，添加如下内容：
    ```
    #chkconfig: 22345 10 90
    #description: Start and Stop redis
    ```
    随后执行如下操作：`chkconfig redis_init_script on`

## 3、Redis主从配置

### 3.1、环境准备

准备三台虚拟机，ip分别是：192.168.89.132（master）、192.168.89.133（slave）、192.168.89.134（slave）

### 3.2、master节点配置修改

- 修改配置文件redis.conf，配置文件在当前虚拟机处于：`/usr/local/redis/redis.conf`目录下；

- 找到配置：

### 3.3、slave节点配置

- 修改配置文件redis.conf，配置文件在当前虚拟机处于：`/usr/local/redis/redis.conf`目录下；

- 找到配置：replicaof，修改配置如下：`replicaof 192.168.89.132 6379`，即配置主节点的ip地址和端口

- 配置主节点的访问密码：masterauth，配置如下：`masterauth test`；

    **注意：**如果主节点没有配置密码，就是`requirepass 123456`配置没有放开，这个在从节点处就不需要配置了；

- 配置从节点只读：`replica-read-only yes`

主节点状态：
```
127.0.0.1:6379> info replication
# Replication
role:master
connected_slaves:2
slave0:ip=192.168.89.136,port=6379,state=online,offset=98,lag=1
slave1:ip=192.168.89.137,port=6379,state=online,offset=98,lag=0
master_replid:80fcd520080d4e08418f43fcad03b0a7844ad482
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:98
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:98
```
从节点：
```
127.0.0.1:6379> info replication
# Replication
role:slave
master_host:192.168.89.135
master_port:6379
master_link_status:up
master_last_io_seconds_ago:3
master_sync_in_progress:0
slave_repl_offset:42
slave_priority:100
slave_read_only:1
connected_slaves:0
master_replid:80fcd520080d4e08418f43fcad03b0a7844ad482
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:42
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:42
```

## 4、Redis哨兵


## 5、Redis集群


# 四、RabbitMQ

## 1、单机安装

准备：`yum install build-essential openssl openssl-devel unixODBC unixODBC-devel make gcc gcc-c++ kernel-devel m4 ncurses-devel tk tc xz`

- erlang依赖下载：https://github.com/rabbitmq/erlang-rpm/releases

下载依赖：
```
wget www.rabbitmq.com/releases/erlang/erlang-18.3-1.el7.centos.x86_64.rpm
wget http://repo.iotti.biz/CentOS/7/x86_64/socat-1.7.3.2-5.el7.lux.x86_64.rpm
wget www.rabbitmq.com/releases/rabbitmq-server/v3.6.5/rabbitmq-server-3.6.5-1.noarch.rpm
```

配置文件：
```
vim /usr/lib/rabbitmq/lib/rabbitmq_server-3.6.5/ebin/rabbit.app
```
比如修改密码、配置等等，例如：loopback_users 中的 `<<"guest">>`，只保留guest

服务启动和停止：
- 启动 `rabbitmq-server start &`
- 停止 `rabbitmqctl app_stop`

管理插件：`rabbitmq-plugins enable rabbitmq_management`

访问地址：http://192.168.11.76:15672/

## 2、镜像集群安装

### 2.1、环境准备

三台服务器：
- IP地址： `192.168.89.152/153/154`
- hostname 分别为： `rabbitmq1、rabbitmq2、rabbitmq3`

### 2.2、安装步骤

https://juejin.im/post/6844903826332319757

# 五、RocketMQ


# 六、ElasticSearch

# 七、Zookeeper安装

## 1、zookeeper单机安装

第一步：从官网获取到安装包，将其解压：
```bash
[root@node1 opt]# tar zxvf zookeeper-3.4.12.tar.gz 
# 解压之后当前/opt目录下生成一个名为zookeeper-3.4.12的文件夹
[root@node1 opt]# cd zookeeper-3.4.12
[root@node1 zookeeper-3.4.12]# pwd
/opt/zookeeper-3.4.12
```

第二步，向/etc/profile 配置文件中添加如下内容，并执行 source /etc/profile 命令使配置生效：
```bash
export ZOOKEEPER_HOME=/opt/zookeeper-3.4.12
export PATH=$PATH:$ZOOKEEPER_HOME/bin
```

第三步，修改 ZooKeeper 的配置文件。首先进入$ZOOKEEPER_HOME/conf目录，并将zoo_sample.cfg 文件修改为 zoo.cfg：
```bash
[root@node1 zookeeper-3.4.12]# cd conf
[root@node1 conf]# cp zoo_sample.cfg zoo.cfg
```

然后修改 zoo.cfg 配置文件，zoo.cfg 文件的内容参考如下：
```bash
# ZooKeeper服务器心跳时间，单位为ms
tickTime=2000
# 允许follower连接并同步到leader的初始化连接时间，以tickTime的倍数来表示
initLimit=10
# leader与follower心跳检测最大容忍时间，响应超过syncLimit*tickTime，leader认为
# follower“死掉”，从服务器列表中删除follower
syncLimit=5
# 数据目录,需要创建目录
dataDir=/tmp/zookeeper/data
# 日志目录，需要创建目录
dataLogDir=/tmp/zookeeper/log
# ZooKeeper对外服务端口
clientPort=2181
```

第四步，在${dataDir}目录（也就是/tmp/zookeeper/data）下创建一个 myid 文件，并写入一个数值，比如0。myid 文件里存放的是服务器的编号

第五步，启动 Zookeeper 服务，详情如下：
```bash
[root@node1 conf]# zkServer.sh start
JMX enabled by default
Using config: /opt/zookeeper-3.4.6/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED
[root@node1 ]# zkServer.sh status
JMX enabled by default
Using config: /opt/zookeeper-3.4.12/bin/../conf/zoo.cfg
Mode: Standalone
```

## 2、zookeeper集群安装

开机启动：
```bash
cd /etc/rc.d/init.d/
创建 zookeeper： touch zookeeper
授予权限： chmod 777 zookeeper
```
开启启动脚本：
```bash
#!/bin/bash


#chkconfig:2345 20 90
#description:zookeeper
#processname:zookeeper

export JAVA_HOME=/usr/java/jdk1.8.0_231-amd64
export PATH=$PATH:$JAVA_HOME/bin
case $1 in
    start) /usr/local/zookeeper-3.4.12/bin/zkServer.sh start;;
    stop) /usr/local/zookeeper-3.4.12/bin/zkServer.sh stop;;
    status) /usr/local/zookeeper-3.4.12/bin/zkServer.sh status;;
    restart) /usr/local/zookeeper-3.4.12/bin/zkServer.sh restart;;
    *) echo "require start|stop|status|restart" ;;
esac
```
开机启动配置： chkconfig zookeeper on

验证：
chkconfig -add zookeeper

chkconfig --list zookeeper

# 八、Kafka

依赖：
- JDK安装
- Zookeeper安装
- 下载安装包

## 1、Kafka单机安装

下载安装包，并解压：
```bash
[root@node1 opt]# ll kafka_2.11-2.0.0.tgz 
-rw-r--r-- 1 root root 55751827 Jul 31 10:45 kafka_2.11-2.0.0.tgz
[root@node1 opt]# tar zxvf kafka_2.11-2.0.0.tgz
# 解压之后当前/opt目录下生成一个名为kafka_2.11-2.0.0的文件夹
[root@node1 opt]# cd kafka_2.11-2.0.0
[root@node1 kafka_2.11-2.0.0]#
# Kafka的根目录$KAFKA_HOME即为/opt/kafka_2.11-2.0.0，可以将Kafka_HOME添加到/etc/profile文件中
```

需要修改 broker 的配置文件 $KAFKA_HOME/conf/server.properties。主要关注以下几个配置参数即可：
```bash
# broker的编号，如果集群中有多个broker，则每个broker的编号需要设置的不同
broker.id=0
# broker对外提供的服务入口地址
listeners=PLAINTEXT://localhost:9092
# 存放消息日志文件的地址
log.dirs=/tmp/kafka-logs
# Kafka所需的ZooKeeper集群地址，为了方便演示，我们假设Kafka和ZooKeeper都安装在本机
zookeeper.connect=localhost:2181/kafka
```
注意：如果zookeeper.connect的地址后面跟上了具体 路径，在使用的时候也需要跟上相应的路径

单机模式下，修改完上述配置参数之后就可以启动服务
```
bin/kafka-server-start.sh -daemon config/server.properties
```

## 2、kafka集群安装

- 解压缩kafka包


## 3、安装kafka-manager

- 下载 kafka-manager-2.0.0.2.zip 包，解压：`unzip kafka-manager-2.0.0.2.zip -d /usr/local/`

- 修改配置内容：`vim /usr/local/kafka-manager-2.0.0.2/conf/application.conf`
    ```
    kafka-manager.zkhosts="localhost:2181"
    ```

- 启动kafka-manager: `/usr/local/kafka-manager-2.0.0.2/bin/kafka-manager &`

- 其默认访问端口是: 9200
