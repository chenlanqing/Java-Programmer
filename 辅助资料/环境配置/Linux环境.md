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

mysql目前最新版是8.0，启动mysql后，在容器内通过：`mysql -uroot -p123456` 进入到mysql命令行界面，可以通过如下操作解决通过IDE工具无法连接mysql（因为mysql8用的加密方式是不一样的）
- 修改加密规则：`ALTER USER 'root'@'%' IDENTIFIED BY '123456' PASSWORD EXPIRE NEVER;`
- 更新一下用户的密码：`ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '123456';`
- 刷新权限：`flush privileges;`

在同一台机器上启动多个mysql实例：
```bash
# --name 指定的是容器名称 3307:3306 是映射端口
# 最后 mysql:8.0.32指定的是mysql镜像
docker run -id --name=mysql3306 -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql:8.0.32
docker run -id --name=mysql3307 -p 3307:3306 -e MYSQL_ROOT_PASSWORD=123456 mysql:8.0.32
```

## 4、问题

比如说如果出现这个问题：`INSERT command denied to user 'root'@'172.17.0.1' for table '<table_name>'`

应该是没有给`'root'@'172.17.0.1'` 赋予权限：

**检查用户名的权限**
```sql
mysql> select * from mysql.user where user='root'\G
*************************** 1. row ***************************
                    Host: %
                    User: root
             Select_priv: Y
             Insert_priv: N
             Update_priv: Y
             Delete_priv: N
             Create_priv: N
               Drop_priv: N
             Reload_priv: Y
            .....
```
可以看到 `%`（表示通配符）其  `Insert_priv: N` 

**授权**
```sql
grant all privileges on *.* to 'root'@'%' identified by 'root密码';
flush privileges;
```
上面如果报错的话，可以登录控制台直接执行：
```sql
UPDATE mysql.user set Insert_priv='Y', Delete_priv='Y',Create_priv='Y',Drop_priv='Y' where user='root';
```

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

### 4.1、机器

Redis哨兵由一个一主两从，然后配置三个哨兵

Redis哨兵节点规划如下：主要三台机器：192.168.89.135/136/137，单台机器

| 机器IP         | 节点名称   | 端口号 | 主/从节点 | 复制节点 |
| -------------- | ---------- | ------ | --------- | -------- |
| 192.168.89.135 | master     | 6379   | 主节点    |          |
| 192.168.89.136 | slave      | 6379   | 从节点    |          |
| 192.168.89.137 | slave      | 6379   | 从节点    |          |
| 192.168.89.135 | sentinel-1 | 26379  | 哨兵-1    |          |
| 192.168.89.136 | sentinel-2 | 26379  | 哨兵-2    |          |
| 192.168.89.137 | sentinel-3 | 26379  | 哨兵-3    |          |

### 4.2、目录

三台机器上redis目录`/data/redis-5.0.10`下有：data、log、db 三个目录，结构如下
```
├── data
│   ├── master
├── db
├── log
│   ├── master.log
│   └── sentinel.log
│   ├── slave.log
├── master.conf
├── sentinel.conf
├── slave.conf
```

### 4.3、主从配置

**master节点（192.168.89.135）** 修改 `master.conf`配置文件
```conf
# 后台启动
daemonize yes
pidfile /var/run/master.pid
# 日志文件对应的位置
logfile /data/redis-5.0.10/log/master.log
# 端口号
port 6379
```
**两个slave节点（192.168.89.136/137）** 修改 `slave.conf`配置文件
```conf
# 后台启动
daemonize yes
pidfile /var/run/master.pid
# 日志文件对应的位置
logfile /data/redis-5.0.10/log/slave.log
# 端口号
port 6379
slaveof 192.168.89.135 6379
```

**启动主从：** 分别在三台机器上执行如下命令
```
[root@kafka1 redis-5.0.10]# src/redis-server master.conf
[root@kafka1 redis-5.0.10]# src/redis-server slave.conf
[root@kafka1 redis-5.0.10]# src/redis-server slave.conf
```
启动成功后，master上的日志：
```
1863:M 17 Jan 2022 18:41:18.032 * Ready to accept connections
1863:M 17 Jan 2022 18:41:45.922 * Replica 192.168.89.136:6379 asks for synchronization
1863:M 17 Jan 2022 18:41:45.922 * Full resync requested by replica 192.168.89.136:6379
1863:M 17 Jan 2022 18:41:45.922 * Starting BGSAVE for SYNC with target: disk
1863:M 17 Jan 2022 18:41:45.958 * Background saving started by pid 1893
1893:C 17 Jan 2022 18:41:45.991 * DB saved on disk
1893:C 17 Jan 2022 18:41:45.992 * RDB: 4 MB of memory used by copy-on-write
1863:M 17 Jan 2022 18:41:46.006 * Background saving terminated with success
1863:M 17 Jan 2022 18:41:46.006 * Synchronization with replica 192.168.89.136:6379 succeeded
1863:M 17 Jan 2022 18:41:52.755 * Replica 192.168.89.137:6379 asks for synchronization
1863:M 17 Jan 2022 18:41:52.755 * Full resync requested by replica 192.168.89.137:6379
1863:M 17 Jan 2022 18:41:52.755 * Starting BGSAVE for SYNC with target: disk
1863:M 17 Jan 2022 18:41:52.779 * Background saving started by pid 1900
1900:C 17 Jan 2022 18:41:52.779 * DB saved on disk
1900:C 17 Jan 2022 18:41:52.780 * RDB: 4 MB of memory used by copy-on-write
1863:M 17 Jan 2022 18:41:52.796 * Background saving terminated with success
1863:M 17 Jan 2022 18:41:52.797 * Synchronization with replica 192.168.89.137:6379 succeeded
```

### 4.4、配置启动

主要是配置 sentinel.conf 配置文件
```conf
# 端口，也是默认端口
port 26379
# 后台启动
daemonize yes
# sentinel监控Redis主节点的配置
# 对应的命令：sentinel monitor <master-name> <ip> <redis-port> <quorum>  
# master-name 指的是主节点的名字，可以自己定义
# ip 指的是主节点的ip地址
# redis-port 指的是主节点的端口
# quorum 这个值既用于主节点的客观下线，又用于sentinel的leader选举，具体可见上面的原理 
sentinel monitor mymaster 192.168.89.135 6379 2

# 主节点响应sentinel的最大时间间隔，超过这个时间，sentinel认为主节点下线，默认30秒  
sentinel down-after-milliseconds mymaster 3000

# 进行故障转移时，设置最多有多少个slave同时复制新的master
# 由于slave在复制时，会处于不可用的状态(要先清空数据，然后再加载主节点的数据)
# 所以设置一次允许一个slave去复制master
sentinel parallel-syncs master 1
# 故障转移的超时时间，默认3分钟。 
# 当前sentinel对一个master节点进行故障转移后，需要等待这个时间后才能
# 再次对这个master节点进行故障转移(此时其它sentinel依然可以对该master进行故障转移)
# 进行故障转移时，配置所有slave复制master的最大时间，如果超时了，就不会按照parallel-syncs规则进行了
# sentinel进行leader选举时，如果没有选出leader，需要等到2倍这个时间才能进行下一次选举
sentinel failover-timeout master 180000
# 主节点如果设置了密码，就在这里配置
sentinel auth-pass mymaster 123456
# log文件的位置
logfile /data/redis-5.0.10/log/sentinel.log
```
启动sentinel，分别在三台机器上执行如下命令：
```
[root@kafka1 redis-5.0.10]# src/redis-server sentinel.conf --sentinel
[root@kafka1 redis-5.0.10]# src/redis-server sentinel.conf --sentinel
[root@kafka1 redis-5.0.10]# src/redis-server sentinel.conf --sentinel
```
sentinel启动日志：
```
2602:X 17 Jan 2022 18:55:37.739 # Sentinel ID is 07355d2b76e0e90955bf4e3447e590c6037c2baa
2602:X 17 Jan 2022 18:55:37.739 # +monitor master mymaster 192.168.89.135 6379 quorum 2
2602:X 17 Jan 2022 18:55:37.741 * +slave slave 192.168.89.136:6379 192.168.89.136 6379 @ mymaster 192.168.89.135 6379
2602:X 17 Jan 2022 18:55:37.749 * +slave slave 192.168.89.137:6379 192.168.89.137 6379 @ mymaster 192.168.89.135 6379
2602:X 17 Jan 2022 18:55:49.234 * +sentinel sentinel 7769328e521738999693b4d4a22101ea42cf988c 192.168.89.136 26379 @ mymaster 192.168.89.135 6379
2602:X 17 Jan 2022 18:55:53.964 * +sentinel sentinel 55f5b1ba4a030620c2547aea8edcf75bf466d129 192.168.89.137 26379 @ mymaster 192.168.89.135 6379
```

### 4.5、sentinel客户端使用

连接sentinel客户端
```
src/redis-cli -p 26379
```
查看当前sentinel的信息：
```
127.0.0.1:26379> info sentinel
# Sentinel
sentinel_masters:1
sentinel_tilt:0
sentinel_running_scripts:0
sentinel_scripts_queue_length:0
sentinel_simulate_failure_flags:0
master0:name=mymaster,status=ok,address=192.168.89.135:6379,slaves=2,sentinels=3
```
查看监控的所有master的信息
```
127.0.0.1:26379> sentinel masters
1)  1) "name"
    2) "mymaster"
    3) "ip"
    4) "192.168.89.135"
    5) "port"
    6) "6379"
    7) "runid"
    8) "35b88cb640543ffdddeec36cf163a5c377a1d2ce"
...
```
查看指定master节点的信息，因为这里只监控了一个master节点，所以显示和上面一样
```
sentinel master <master_name>
```
查看指定的master的地址和端口
```
127.0.0.1:26379> sentinel get-master-addr-by-name mymaster
1) "192.168.89.135"
2) "6379"
```

**故障转移：**
```
127.0.0.1:26379> sentinel failover mymaster
OK
127.0.0.1:26379> info sentinel
# Sentinel
sentinel_masters:1
sentinel_tilt:0
sentinel_running_scripts:0
sentinel_scripts_queue_length:0
sentinel_simulate_failure_flags:0
master0:name=mymaster,status=ok,address=192.168.89.136:6379,slaves=2,sentinels=3
```
对应的日志为：
```
2602:X 17 Jan 2022 18:58:38.534 # Executing user requested FAILOVER of 'mymaster'
2602:X 17 Jan 2022 18:58:38.534 # +new-epoch 1
2602:X 17 Jan 2022 18:58:38.534 # +try-failover master mymaster 192.168.89.135 6379
2602:X 17 Jan 2022 18:58:38.625 # +vote-for-leader 07355d2b76e0e90955bf4e3447e590c6037c2baa 1
2602:X 17 Jan 2022 18:58:38.625 # +elected-leader master mymaster 192.168.89.135 6379
2602:X 17 Jan 2022 18:58:38.625 # +failover-state-select-slave master mymaster 192.168.89.135 6379
2602:X 17 Jan 2022 18:58:38.702 # +selected-slave slave 192.168.89.136:6379 192.168.89.136 6379 @ mymaster 192.168.89.135 6379
2602:X 17 Jan 2022 18:58:38.702 * +failover-state-send-slaveof-noone slave 192.168.89.136:6379 192.168.89.136 6379 @ mymaster 192.168.89.135 6379
2602:X 17 Jan 2022 18:58:38.786 * +failover-state-wait-promotion slave 192.168.89.136:6379 192.168.89.136 6379 @ mymaster 192.168.89.135 6379
2602:X 17 Jan 2022 18:58:39.658 # +promoted-slave slave 192.168.89.136:6379 192.168.89.136 6379 @ mymaster 192.168.89.135 6379
2602:X 17 Jan 2022 18:58:39.658 # +failover-state-reconf-slaves master mymaster 192.168.89.135 6379
2602:X 17 Jan 2022 18:58:39.748 * +slave-reconf-sent slave 192.168.89.137:6379 192.168.89.137 6379 @ mymaster 192.168.89.135 6379
2602:X 17 Jan 2022 18:58:40.663 * +slave-reconf-inprog slave 192.168.89.137:6379 192.168.89.137 6379 @ mymaster 192.168.89.135 6379
2602:X 17 Jan 2022 18:58:41.725 * +slave-reconf-done slave 192.168.89.137:6379 192.168.89.137 6379 @ mymaster 192.168.89.135 6379
2602:X 17 Jan 2022 18:58:41.800 # +failover-end master mymaster 192.168.89.135 6379
2602:X 17 Jan 2022 18:58:41.800 # +switch-master mymaster 192.168.89.135 6379 192.168.89.136 6379
2602:X 17 Jan 2022 18:58:41.801 * +slave slave 192.168.89.137:6379 192.168.89.137 6379 @ mymaster 192.168.89.136 6379
2602:X 17 Jan 2022 18:58:41.801 * +slave slave 192.168.89.135:6379 192.168.89.135 6379 @ mymaster 192.168.89.136 6379
2602:X 17 Jan 2022 19:00:58.491 # +sdown master mymaster 192.168.89.136 6379
2602:X 17 Jan 2022 19:00:58.550 # +odown master mymaster 192.168.89.136 6379 #quorum 2/2
2602:X 17 Jan 2022 19:00:58.550 # +new-epoch 2
2602:X 17 Jan 2022 19:00:58.550 # +try-failover master mymaster 192.168.89.136 6379
2602:X 17 Jan 2022 19:00:58.557 # +vote-for-leader 07355d2b76e0e90955bf4e3447e590c6037c2baa 2
2602:X 17 Jan 2022 19:00:58.560 # 7769328e521738999693b4d4a22101ea42cf988c voted for 07355d2b76e0e90955bf4e3447e590c6037c2baa 2
2602:X 17 Jan 2022 19:00:58.560 # 55f5b1ba4a030620c2547aea8edcf75bf466d129 voted for 07355d2b76e0e90955bf4e3447e590c6037c2baa 2
2602:X 17 Jan 2022 19:00:58.624 # +elected-leader master mymaster 192.168.89.136 6379
2602:X 17 Jan 2022 19:00:58.624 # +failover-state-select-slave master mymaster 192.168.89.136 6379
2602:X 17 Jan 2022 19:00:58.687 # +selected-slave slave 192.168.89.135:6379 192.168.89.135 6379 @ mymaster 192.168.89.136 6379
2602:X 17 Jan 2022 19:00:58.687 * +failover-state-send-slaveof-noone slave 192.168.89.135:6379 192.168.89.135 6379 @ mymaster 192.168.89.136 6379
2602:X 17 Jan 2022 19:00:58.751 * +failover-state-wait-promotion slave 192.168.89.135:6379 192.168.89.135 6379 @ mymaster 192.168.89.136 6379
2602:X 17 Jan 2022 19:00:59.302 # +promoted-slave slave 192.168.89.135:6379 192.168.89.135 6379 @ mymaster 192.168.89.136 6379
2602:X 17 Jan 2022 19:00:59.302 # +failover-state-reconf-slaves master mymaster 192.168.89.136 6379
2602:X 17 Jan 2022 19:00:59.400 * +slave-reconf-sent slave 192.168.89.137:6379 192.168.89.137 6379 @ mymaster 192.168.89.136 6379
2602:X 17 Jan 2022 19:00:59.403 * +slave-reconf-inprog slave 192.168.89.137:6379 192.168.89.137 6379 @ mymaster 192.168.89.136 6379
2602:X 17 Jan 2022 19:00:59.690 # -odown master mymaster 192.168.89.136 6379
2602:X 17 Jan 2022 19:01:00.425 * +slave-reconf-done slave 192.168.89.137:6379 192.168.89.137 6379 @ mymaster 192.168.89.136 6379
2602:X 17 Jan 2022 19:01:00.502 # +failover-end master mymaster 192.168.89.136 6379
2602:X 17 Jan 2022 19:01:00.503 # +switch-master mymaster 192.168.89.136 6379 192.168.89.135 6379
2602:X 17 Jan 2022 19:01:00.504 * +slave slave 192.168.89.137:6379 192.168.89.137 6379 @ mymaster 192.168.89.135 6379
2602:X 17 Jan 2022 19:01:00.504 * +slave slave 192.168.89.136:6379 192.168.89.136 6379 @ mymaster 192.168.89.135 6379
```

## 5、Redis集群

- [Redis Cluster搭建方式](https://juejin.cn/post/6844904057044205582)

### 5.1、机器

Redis集群一般由多个节点组成，节点数量至少为 6 个，才能保证组成完整高可用的集群。Redis集群节点规划如下：

主要三台机器：192.168.89.135/136/137，单台机器

| 机器IP         | 节点名称   | 端口号 | 主/从节点 | 复制节点   |
| -------------- | ---------- | ------ | --------- | ---------- |
| 192.168.89.135 | redis-6378 | 6378   | 主节点    |            |
| 192.168.89.135 | redis-6379 | 6379   | 从节点    | redis-6379 |
| 192.168.89.136 | redis-6378 | 6378   | 主节点    |            |
| 192.168.89.136 | redis-6379 | 6379   | 从节点    | redis-6379 |
| 192.168.89.137 | redis-6378 | 6378   | 主节点    |            |
| 192.168.89.137 | redis-6379 | 6379   | 从节点    | redis-6379 |

### 5.2、目录安排

三台机器上redis目录`/data/redis-5.0.10`下有：data、log、db 三个目录，结构如下
```
├── data
│   ├── redis-6378
│   └── redis-6379
├── db
├── log
│   ├── redis-6378.log
│   └── redis-6379.log
├── redis-6378.conf
├── redis-6379.conf
```

### 5.3、修改配置文件

- vim redis-6378.conf
```conf
# redis后台运行
daemonize yes
# 数据存放目录
dir /data/redis-5.0.10/data/redis-6378
# 日志文件
logfile /data/redis-5.0.10/log/redis-6378.log
# 端口号
port 6378
# 开启集群模式
cluster-enabled yes
# 集群的配置，配置文件首次启动自动生成
# 这里只需指定文件名即可，集群启动成功后会自动在data目录下创建
cluster-config-file "nodes-6378.conf"
# 请求超时，设置10秒
cluster-node-timeout 10000
```
其他机器的配置也是类似

### 5.4、启动所有节点

分别在三台机器上执行如下命令：
```
src/redis-server redis-6379.conf
src/redis-server redis-6378.conf
```
使用脚本
```bash
#! /bin/bash

case $1 in
"start"){
        for i in kafka1 kafka2 kafka3
        do
                echo " --------启动 $i Redis 6379 & 6378-------"

                ssh $i " mkdir /data/redis-5.0.10/data/redis-6378 /data/redis-5.0.10/data/redis-6379 ; /data/redis-5.0.10/src/redis-server /data/redis-5.0.10/redis-6379.conf ; /data/redis-5.0.10/src/redis-server /data/redis-5.0.10/redis-6378.conf"
        done
};;
"stop"){
        for i in kafka1 kafka2 kafka3
        do
                echo " --------停止 $i Redis 6379 & 6378-------"
                ssh $i " /data/redis-5.0.10/src/redis-cli -p 6379 shutdown ; /data/redis-5.0.10/src/redis-cli -p 6378 shutdown ; rm -rf /data/redis-5.0.10/data/redis-6378 /data/redis-5.0.10/data/redis-6379 ;  "
        done
};;
esac
```

### 5.5、手动配置集群

**（1）节点握手**

Redis Cluster集群模式下的节点彼此通过Gossip协议进行通信，但集群中的节点需要先进行握手通信。节点握手需要由客户端发起命令：cluster meet {ip} {port}

比如进入到`192.168.89.135`的redis-6379这台redis的客户端发起握手
```
127.0.0.1:6379> cluster meet 192.168.89.135 6378
127.0.0.1:6379> cluster meet 192.168.89.136 6378
127.0.0.1:6379> cluster meet 192.168.89.136 6379
127.0.0.1:6379> cluster meet 192.168.89.137 6378
127.0.0.1:6379> cluster meet 192.168.89.137 6379
```
查看集群信息：
```
127.0.0.1:6379> cluster nodes
c9f871a0355129a37ba8fc20e8fbe26b0604c209 192.168.89.137:6378@16378 master - 0 1641104128000 4 connected
6809869bb98a455ea42a31b921a2e3a061eba4c7 192.168.89.135:6378@16378 master - 0 1641104128951 2 connected
cff7279c90fd23fdf11f320fffe4af7d1665d599 192.168.89.136:6378@16378 master - 0 1641104129963 0 connected
22fc815b0005623bf06ff5085f7e160255746ab0 192.168.89.137:6379@16379 master - 0 1641104127000 5 connected
63461b3a68993bc10ea3ea02b1a3b712654abf29 192.168.89.135:6379@16379 myself,master - 0 1641104126000 1 connected
cb941c2bd75c20fa72cab1f9f2ae9d61a9f2a78b 192.168.89.136:6379@16379 master - 0 1641104126000 3 connected
```
但是此时集群还不能使用，因为还没有给节点分配槽，所以不能对数据进行读写操作。
可以查看集群的信息(集群的状态处于fail状态，且分配的槽为0)：
```
127.0.0.1:6379> cluster info
cluster_state:fail
cluster_slots_assigned:0
cluster_slots_ok:0
cluster_slots_pfail:0
cluster_slots_fail:0
cluster_known_nodes:6
cluster_size:0
cluster_current_epoch:5
cluster_my_epoch:1
cluster_stats_messages_ping_sent:59
cluster_stats_messages_pong_sent:72
cluster_stats_messages_meet_sent:5
cluster_stats_messages_sent:136
cluster_stats_messages_ping_received:72
cluster_stats_messages_pong_received:64
cluster_stats_messages_received:136
```

**（2）分配槽**

Redis集群将所有的数据映射到16384个槽中，每个key都会对应一个槽，只有把槽分配给了节点，节点才能响应与槽相关的命令，分配槽的命令：
```
src/redis-cli -h 192.168.89.135 -p 6378 cluster addslots {0..5461}
src/redis-cli -h 192.168.89.136 -p 6378 cluster addslots {5462..10922}
src/redis-cli -h 192.168.89.137 -p 6378 cluster addslots {10922..16383}
```
将16384个槽平均的分给3个节点，然后查看集群的状态，可以发现变成了OK
```
127.0.0.1:6379> cluster info
cluster_state:ok
cluster_slots_assigned:16384
cluster_slots_ok:16384
cluster_slots_pfail:0
```
再查看集群的槽，可以看到每个节点分配到的槽的范围和节点的信息
```
127.0.0.1:6379> cluster slots
1) 1) (integer) 10922
   2) (integer) 16383
   3) 1) "192.168.89.137"
      2) (integer) 6378
      3) "c9f871a0355129a37ba8fc20e8fbe26b0604c209"
2) 1) (integer) 0
   2) (integer) 5461
   3) 1) "192.168.89.135"
      2) (integer) 6378
      3) "6809869bb98a455ea42a31b921a2e3a061eba4c7"
3) 1) (integer) 5462
   2) (integer) 10921
   3) 1) "192.168.89.136"
      2) (integer) 6378
      3) "cff7279c90fd23fdf11f320fffe4af7d1665d599"
```
我们一共启动了6个节点，但这里分配槽只用了3个节点，其它3个节点就是用于主从复制的，保证主节点出现故障时可以进行故障转移，从节点负责复制主节点槽信息和相关数据。可以用以下命令让一个节点变成从节点(该命令一定要在从节点客户端上执行)
```
src/redis-cli -h 192.168.89.135 -p 6379 cluster replicate 6809869bb98a455ea42a31b921a2e3a061eba4c7 （当前机器6378节点id）
src/redis-cli -h 192.168.89.136 -p 6379 cluster replicate cff7279c90fd23fdf11f320fffe4af7d1665d599 （当前机器6378节点id）
src/redis-cli -h 192.168.89.137 -p 6379 cluster replicate c9f871a0355129a37ba8fc20e8fbe26b0604c209 （当前机器6378节点id）
```
到此为止，Redis Cluster集群就搭建成功了，最后来看一下集群节点的状态（三主三从）
```
127.0.0.1:6379> cluster nodes
c9f871a0355129a37ba8fc20e8fbe26b0604c209 192.168.89.137:6378@16378 master - 0 1641104460778 4 connected 10922-16383
6809869bb98a455ea42a31b921a2e3a061eba4c7 192.168.89.135:6378@16378 master - 0 1641104462000 2 connected 0-5461
cff7279c90fd23fdf11f320fffe4af7d1665d599 192.168.89.136:6378@16378 master - 0 1641104462000 0 connected 5462-10921
22fc815b0005623bf06ff5085f7e160255746ab0 192.168.89.137:6379@16379 slave c9f871a0355129a37ba8fc20e8fbe26b0604c209 0 1641104462801 5 connected
63461b3a68993bc10ea3ea02b1a3b712654abf29 192.168.89.135:6379@16379 myself,slave 6809869bb98a455ea42a31b921a2e3a061eba4c7 0 1641104461000 1 connected
cb941c2bd75c20fa72cab1f9f2ae9d61a9f2a78b 192.168.89.136:6379@16379 slave cff7279c90fd23fdf11f320fffe4af7d1665d599 0 1641104460000 3 connected
```

### 5.6、自动配置集群

前面集群搭建非常的麻烦(先进行节点通信，然后分配槽，最后还要指定从节点去复制主节点)。好在Redis为我们提供了工具可以让我们轻松的搭建集群

如果手动搭建过集群，需要把手动搭建的集群节点全部停止，并删除数据目录下的所有文件。

如果在之前的集群中的某个节点存储过数据或者集群配置没有删除，会报类似错误[ERR] `Node 127.0.0.1:6378 is not empty. Either the node already knows other nodes (check with CLUSTER NODES) or contains some key in database 0`.如果集群配置未删除，则删除所有集群配置，否则进入之前存储数据的客户端执行flushall和cluster reset清空数据并重置集群。然后重新启动所有节点

只需键入以下内容即可为Redis 5创建集群：
```
src/redis-cli --cluster create 192.168.89.135:6378 192.168.89.135:6379 192.168.89.136:6378 192.168.89.136:6379 192.168.89.137:6378 192.168.89.137:6379 --cluster-replicas 1
```
create 后面跟着的是6个节点的地址和端口，选项`--cluster-replicas` 1意味着为每个主节点都提供一个从节点。创建结果如下：
```bash
[root@kafka1 redis-5.0.10]# src/redis-cli --cluster create 192.168.89.135:6378 192.168.89.135:6379 192.168.89.136:6378 192.168.89.136:6379 192.168.89.137:6378 192.168.89.137:6379 --cluster-replicas 1
>>> Performing hash slots allocation on 6 nodes...
Master[0] -> Slots 0 - 5460
Master[1] -> Slots 5461 - 10922
Master[2] -> Slots 10923 - 16383
Adding replica 192.168.89.136:6379 to 192.168.89.135:6378
Adding replica 192.168.89.137:6379 to 192.168.89.136:6378
Adding replica 192.168.89.135:6379 to 192.168.89.137:6378
M: ea667d2a062b5815748abeec78d431816c9dfbd8 192.168.89.135:6378
   slots:[0-5460] (5461 slots) master
S: 9883ca2356ab953a7397aa17341913949036e286 192.168.89.135:6379
   replicates cc131938e7a078174bc981d2cc943cb1018d431e
M: c184a4ce979fe71ef9ea89d9e012c5a0676d596e 192.168.89.136:6378
   slots:[5461-10922] (5462 slots) master
S: 7b5e6b1aca9325b2876ac74a408d4d6fa2ddcf0d 192.168.89.136:6379
   replicates ea667d2a062b5815748abeec78d431816c9dfbd8
M: cc131938e7a078174bc981d2cc943cb1018d431e 192.168.89.137:6378
   slots:[10923-16383] (5461 slots) master
S: 949410487471a417afca1ed77aa35748dfd38ce9 192.168.89.137:6379
   replicates c184a4ce979fe71ef9ea89d9e012c5a0676d596e
Can I set the above configuration? (type 'yes' to accept): yes
>>> Nodes configuration updated
>>> Assign a different config epoch to each node
>>> Sending CLUSTER MEET messages to join the cluster
Waiting for the cluster to join
...
>>> Performing Cluster Check (using node 192.168.89.135:6378)
M: ea667d2a062b5815748abeec78d431816c9dfbd8 192.168.89.135:6378
   slots:[0-5460] (5461 slots) master
   1 additional replica(s)
S: 7b5e6b1aca9325b2876ac74a408d4d6fa2ddcf0d 192.168.89.136:6379
   slots: (0 slots) slave
   replicates ea667d2a062b5815748abeec78d431816c9dfbd8
M: cc131938e7a078174bc981d2cc943cb1018d431e 192.168.89.137:6378
   slots:[10923-16383] (5461 slots) master
   1 additional replica(s)
S: 949410487471a417afca1ed77aa35748dfd38ce9 192.168.89.137:6379
   slots: (0 slots) slave
   replicates c184a4ce979fe71ef9ea89d9e012c5a0676d596e
M: c184a4ce979fe71ef9ea89d9e012c5a0676d596e 192.168.89.136:6378
   slots:[5461-10922] (5462 slots) master
   1 additional replica(s)
S: 9883ca2356ab953a7397aa17341913949036e286 192.168.89.135:6379
   slots: (0 slots) slave
   replicates cc131938e7a078174bc981d2cc943cb1018d431e
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
```

### 5.7、Redis Cluster 请求路由方式

客户端直连 Redis 服务，进行读写操作时，Key 对应的 Slot 可能并不在当前直连的节点上，经过“重定向”才能转发到正确的节点；

如下所示，直接连接`192.168.89.135:6379`进行 Set 操作，当 Key 对应的 Slot 不在当前节点时（如 key-test)，客户端会报错并返回正确节点的 IP 和端口。Set 成功则返回 OK
```
[root@kafka1 redis-5.0.10]# src/redis-cli -h 192.168.89.135 -p 6379
192.168.89.135:6379> set key-test value-test
(error) MOVED 11804 192.168.89.137:6378
192.168.89.135:6379> 
```
以集群模式登录`192.168.89.135:6379`客户端（注意命令的差别：-c 表示集群模式)，则可以清楚的看到“重定向”的信息，并且客户端也发生了切换：“6379” -> “6378”
```
[root@kafka1 redis-5.0.10]# src/redis-cli -c -h 192.168.89.135 -p 6379
192.168.89.135:6379> set key-test value-test
-> Redirected to slot [11804] located at 192.168.89.137:6378
OK
```
和普通的查询路由相比，Redis Cluster 借助客户端实现的请求路由是一种混合形式的查询路由，它并非从一个 Redis 节点到另外一个 Redis，而是借助客户端转发到正确的节点；

实际应用中，可以在客户端缓存 Slot 与 Redis 节点的映射关系，当接收到 MOVED 响应时修改缓存中的映射关系。如此，基于保存的映射关系，请求时会直接发送到正确的节点上，从而减少一次交互，提升效率；

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

## 3、[Docker安装RabbitMQ](https://juejin.cn/post/6844903970545090574)

- 获取镜像：使用 `docker search rabbitMq` 命令获取镜像列表；
- 使用 `docker pull docker.io/rabbitmq:3.8-management` 拉取镜像；
- 创建rabbitMq容器，使用docker images获取查看rabbitMQ镜像ID；
- 执行`docker run --name rabbitmq -d -p 15672:15672 -p 5672:5672 4b23cfb64730`命令创建rabbitMq容器;
- 执行docker ps可以查看正在运行的容器，我们能看到rabbitMq已经运行

# 五、RocketMQ


# 六、ElasticSearch

## 1、基础安装

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

## 2、控制台

### 2.2.1、head插件

从 [github](https://github.com/mobz/elasticsearch-head)下载，使用node安装以及运行，npm run start 运行；

如果head插件和elasticsearch运行不在一台服务器上，那么会存在跨域问题，只需要在配置文件：`elasticearch.yml` 增加如下配置
```yml
# ------------- NetWork -----------
http.cors.enabled: true
http.cors.allow-origin: "*"
```

### 2.2.2、cerebro

从[Cerebro](https://github.com/lmenezes/cerebro) 下载zip包，解压缩安装即可

## 3、通过docker安装

### 3.1、安装elasticsearch

```bash
docker run --name elasticsearch -d -e ES_JAVA_OPTS="-Xms512m -Xmx512m" -e "discovery.type=single-node" -p 9200:9200 -p 9300:9300 elasticsearch:7.7.0
```
- `--name` 表示容器名称  
- `-d`: 后台运行容器，并返回容器ID；
- `-e`: 指定容器内的环境变量
- `-p`: 指定端口映射，格式为：主机(宿主)端口:容器端口，es默认端口为9200
- `elasticsearch:7.7.0` 指定镜像，如果本地没有镜像，会从远程拉取对应的镜像

### 3.2、安装Head插件

```bash
docker run --name elasticsearch-head -p 9100:9100 mobz/elasticsearch-head:5
```
完成安装后，直接使用域名加端口9100即可访问

跨域问题：执行命令`docker exec -it elasticsearch /bin/bash` 进入到第一步创建的ElasticSearch容器中，修改配置文件`vi config/elasticsearch.yml`即可。
- `http.cors.enabled: true`
- `http.cors.allow-origin: "*"`

### 3.3、通过docker-compose安装es和head插件

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

# 七、Zookeeper安装

## 1、zookeeper单机安装

### 1.1、tar包安装

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

### 1.2、docker安装

```
docker run -d --name zookeeper -p 2181:2181 -t wurstmeister/zookeeper
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

如果是集群启动，在一台机器上管理脚本：
```bash
#! /bin/bash

# kafka1 kafka2 kafka3 是三台机器的，需要在/etc/hosts 上配置对应

case $1 in
"start"){
        for i in kafka1 kafka2 kafka3
        do
                echo " --------启动 $i zookeeper-------"
                ssh $i "source /etc/profile ; /data/apache-zookeeper-3.6.0-bin/bin/zkServer.sh start"
        done
};;
"stop"){
        for i in kafka1 kafka2 kafka3
        do
                echo " --------停止 $i zookeeper-------"
                ssh $i " source /etc/profile ; /data/apache-zookeeper-3.6.0-bin/bin/zkServer.sh stop"
        done
};;
"status"){
        for i in kafka1 kafka2 kafka3
        do
                echo " --------停止 $i zookeeper-------"
                ssh $i " source /etc/profile ; /data/apache-zookeeper-3.6.0-bin/bin/zkServer.sh status"
        done
};;
esac
```

## 3、docker-compose安装zk集群

```yml
version: "3.9"
services:
  zk1:
    container_name: zk1
    hostname: zk1
    image: bitnami/zookeeper:3.6.2
    ports:
      - 2181:2181
    environment:
      - ALLOW_ANONYMOUS_LOGIN=yes
      - ZOO_SERVER_ID=1
      - ZOO_SERVERS=0.0.0.0:2888:3888,zk2:2888:3888,zk3:2888:3888
  zk2:
    container_name: zk2
    hostname: zk2
    image: bitnami/zookeeper:3.6.2
    ports:
      - 2182:2181
    environment:
      - ALLOW_ANONYMOUS_LOGIN=yes
      - ZOO_SERVER_ID=2
      - ZOO_SERVERS=zk1:2888:3888,0.0.0.0:2888:3888,zk3:2888:3888
  zk3:
    container_name: zk3
    hostname: zk3
    image: bitnami/zookeeper:3.6.2
    ports:
      - 2183:2181
    environment:
      - ALLOW_ANONYMOUS_LOGIN=yes
      - ZOO_SERVER_ID=3
      - ZOO_SERVERS=zk1:2888:3888,zk2:2888:3888,0.0.0.0:2888:3888
  zoonavigator:
    container_name: zoonavigator
    image: elkozmon/zoonavigator
    ports:
      - 8081:9000
```

# 八、Kafka

依赖：
- JDK安装
- Zookeeper安装
- 下载安装包

## 1、Kafka单机安装

### 1.1、tar包安装

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

### 1.2、容器安装

- [Kafka镜像](https://hub.docker.com/r/bitnami/kafka)

创建docker-compose.yml文件：
```yml
version: "3"
services:
   kafka:
     image: 'bitnami/kafka:3.2'
     ports:
       - '9092:9092'
     environment:
       - KAFKA_ENABLE_KRAFT=yes
       - KAFKA_CFG_PROCESS_ROLES=broker,controller
       - KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER
       - KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093
       - KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT
       - KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092
       - KAFKA_BROKER_ID=1
       - KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=1@kafka:9093
       - ALLOW_PLAINTEXT_LISTENER=yes
```
或者直接通过docker安装：
```
docker run -d --name kafka \
-p 9092:9092 \
-e KAFKA_BROKER_ID=1 \
-e KAFKA_ZOOKEEPER_CONNECT=ip:2181 \
-e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://ip:9092 \
-e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 wurstmeister/kafka
```

## 2、kafka集群安装

### 2.1、解压缩kafka包

Kafka集群启动：
```bash
#! /bin/bash

case $1 in
"start"){
        for i in kafka1 kafka2 kafka3
        do
                echo " --------启动 $i Kafka-------"

                ssh $i "source /etc/profile ; /data/kafka_2.12-2.7.0/bin/kafka-server-start.sh -daemon /data/kafka_2.12-2.7.0/config/server.properties "
        done
};;
"stop"){
        for i in kafka1 kafka2 kafka3
        do
                echo " --------停止 $i Kafka-------"
                ssh $i " source /etc/profile ; /data/kafka_2.12-2.7.0/bin/kafka-server-stop.sh stop"
        done
};;
esac
```

## 3、Kafka管理工台

- [8中kafka管理后台工具](https://juejin.cn/post/7055572207891644429)
- [插件化构建企业级Kafka服务](https://github.com/didi/KnowStreaming)

### 3.1、CMAk

- 下载 kafka-manager-2.0.0.2.zip 包，解压：`unzip kafka-manager-2.0.0.2.zip -d /usr/local/`
- 修改配置内容：`vim /usr/local/kafka-manager-2.0.0.2/conf/application.conf`
    ```
    kafka-manager.zkhosts="localhost:2181"
    ```
- 启动kafka-manager: `/usr/local/kafka-manager-2.0.0.2/bin/kafka-manager &`
- 其默认访问端口是: 9200

[cmak](https://github.com/yahoo/CMAK)默认端口是9000，也可以指定端口：`bin/cmak -java-home /usr/jdk-17.0.6 -Dconfig.file=conf/application.conf -Dhttp.port=8080`

## 4、docker安装kafka集群

可以通过docker-compose来进行安装，对应配置文件（docker-compose.yml），其中：
- 创建-kafka-net：`docker network create --subnet=172.31.0.0/24 kafka-net`
- `xx.xx.xx.xx`：表示虚拟机IP
```yml
version: '2'
services:
  zoo-22181:
    image: bitnami/zookeeper:3.6.0
    hostname: zoo-22181
    container_name: zoo-22181
    environment:
      ALLOW_ANONYMOUS_LOGIN: yes
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
      ZOO_MY_ID: 1
      ZOO_SERVERS: 0.0.0.0:2888:3888,zoo-32181:2888:3888,zoo-42181:2888:3888
    ports:
      - 22181:2181
    networks:
      kafka-net:
        ipv4_address: 172.31.0.11
  zoo-32181:
    image: bitnami/zookeeper:3.6.0
    hostname: zoo-32181
    container_name: zoo-32181
    environment:
      ALLOW_ANONYMOUS_LOGIN: yes
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
      ZOO_MY_ID: 2
      ZOO_SERVERS: 0.0.0.0:2888:3888,zoo-22181:2888:3888,zoo-42181:2888:3888
    ports:
      - 32181:2181
    networks:
      kafka-net:
        ipv4_address: 172.31.0.12
  zoo-42181:
    image: bitnami/zookeeper:3.6.0
    hostname: zoo-42181
    container_name: zoo-42181
    environment:
      ALLOW_ANONYMOUS_LOGIN: yes
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
      ZOO_MY_ID: 3
      ZOO_SERVERS: 0.0.0.0:2888:3888,zoo-22181:2888:3888,zoo-32181:2888:3888
    ports:
      - 42181:2181
    networks:
      kafka-net:
        ipv4_address: 172.31.0.13
  
  kafka-1:
    image: bitnami/kafka:3.2
    hostname: kafka-1
    container_name: kafka-1
    depends_on:
      - zoo-22181
      - zoo-32181
      - zoo-42181
    ports:
      - 29092:29092
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zoo-22181:2181,zoo-32181:2181,zoo-42181:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka-1:9092,PLAINTEXT_HOST://xx.xx.xx.xx:29092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    networks:
      kafka-net:
        ipv4_address: 172.31.0.14
  kafka-2:
    image: bitnami/kafka:3.2
    hostname: kafka-2
    container_name: kafka-2
    depends_on:
      - zoo-22181
      - zoo-32181
      - zoo-42181
    ports:
      - 39092:39092
    environment:
      KAFKA_BROKER_ID: 2
      KAFKA_ZOOKEEPER_CONNECT: zoo-22181:2181,zoo-32181:2181,zoo-42181:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka-2:9092,PLAINTEXT_HOST://xx.xx.xx.xx:39092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    networks:
      kafka-net:
        ipv4_address: 172.31.0.15
  kafka-3:
    image: bitnami/kafka:3.2
    hostname: kafka-3
    container_name: kafka-3
    depends_on:
      - zoo-22181
      - zoo-32181
      - zoo-42181
    ports:
      - 49092:49092
    environment:
      KAFKA_BROKER_ID: 3
      KAFKA_ZOOKEEPER_CONNECT: zoo-22181:2181,zoo-32181:2181,zoo-42181:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka-3:9092,PLAINTEXT_HOST://xx.xx.xx.xx:49092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    networks:
      kafka-net:
        ipv4_address: 172.31.0.16

networks:
  kafka-net:
    external: true
```

## 5、kafka外网访问配置

在Kafka服务器上修改Kafka配置文件`server.properties`，设置`advertised.listeners`参数。这个参数指定了Broker向外部客户端公开的连接地址。

例如，如果您想通过公网IP地址访问Kafka服务器，那么可以将该参数设置为：
```
advertised.listeners=PLAINTEXT://your_public_ip:9092
```
如果您的Kafka服务器在内网中，可以将该参数设置为：
```
advertised.listeners=PLAINTEXT://your_internal_ip:9092
```
> 注意：如果Kafka服务器使用了SSL/TLS协议进行安全连接，那么上述参数需要设置为SSL/TLS的连接方式和端口

# 九、Nginx

## 1、Nginx安装

### 1.1、yum安装

> （1）配置 EPEL 源
```
sudo yum install -y epel-release
sudo yum -y update
```
> （2）安装 Nginx
```
sudo yum install -y nginx
```
- 安装成功后，默认的网站目录为： `/usr/share/nginx/html`
- 默认的配置文件为：`/etc/nginx/nginx.conf`
- 自定义配置文件目录为: `/etc/nginx/conf.d/`

启动 Nginx
```
systemctl start nginx
```
停止 Nginx
```
systemctl stop nginx
```
重启 Nginx
```
systemctl restart nginx
```
查看 Nginx 状态
```
systemctl status nginx
```
启用开机启动 Nginx
```
systemctl enable nginx
```
禁用开机启动 Nginx
```
systemctl disable nginx
```

### 1.2、tar包安装

- 安装依赖环境：
	- 安装gcc环境：`yum install gcc-c++`
	- 安装PCRE库，用于解析正则表达式：`yum install -y pcre pcre-devel`
	- zlib压缩和解压缩依赖：`yum install -y zlib zlib-devel`
	- SSL 安全的加密的套接字协议层，用于HTTP安全传输，也就是https：`yum install -y openssl openssl-devel`

- 下载tar包
- 解压，需要注意，解压后得到的是源码，源码需要编译后才能安装：`tar -zxvf nginx-1.16.1.tar.gz` 
- 编译之前，先创建nginx临时目录，如果不创建，在启动nginx的过程中会报错：`mkdir /var/temp/nginx -p`
- 在nginx目录，输入如下命令进行配置，目的是为了创建makefile文件：
	```
	./configure --prefix=/usr/local/nginx --pid-path=/var/run/nginx/nginx.pid --lock-path=/var/lock/nginx.lock --error-log-path=/var/log/nginx/error.log --http-log-path=/var/log/nginx/access.log --with-http_gzip_static_module --http-client-body-temp-path=/var/temp/nginx/client --http-proxy-temp-path=/var/temp/nginx/proxy --http-fastcgi-temp-path=/var/temp/nginx/fastcgi --http-uwsgi-temp-path=/var/temp/nginx/uwsgi --http-scgi-temp-path=/var/temp/nginx/scgi
	```
	|命令	 |解释 |
	| ------|------|
	|–prefix	|指定nginx安装目录|
	|–pid-path	|指向nginx的pid|
	|–lock-path	|锁定安装文件，防止被恶意篡改或误操作|
	|–error-log	|错误日志|
	|–http-log-path	|http日志|
	|–with-http_gzip_static_module	|启用gzip模块，在线实时压缩输出数据流|
	|–http-client-body-temp-path	|设定客户端请求的临时目录|
	|–http-proxy-temp-path	|设定http代理临时目录|
	|–http-fastcgi-temp-path	|设定fastcgi临时目录|
	|–http-uwsgi-temp-path	|设定uwsgi临时目录|
	|–http-scgi-temp-path	|设定scgi临时目录|
- make编译：`make`
- 安装：`make install`
- 进入`/usr/local/nginx/sbin`目录启动nginx：`./nginx`
- 停止：`./nginx -s stop`
- 重新加载：`./nginx -s reload`

## 2、安装openresty

Mac安装

- 下载安装包，解压缩，进入到目录：`cd openresty-1.19.3.2`
- 执行`./configure`，然后执行 make & make install

### 2.1、configure报错

执行 configure 报错，报错信息如下：
```
./configure: error: SSL modules require the OpenSSL library.
You can either do not enable the modules, or install the OpenSSL library
into the system, or build the OpenSSL library statically from the source
with nginx by using --with-openssl=<path> option.
ERROR: failed to run command: sh ./configure --prefix=/usr/local/openresty/nginx \...
```
因为没有配置 openssl，需要下载 openssl
```
./configure --with-openssl=/usr/local/opt/openssl@3   -j8
```

### 2.2、make报错

```
&& if [ -f Makefile ]; then /Library/Developer/CommandLineTools/usr/bin/make clean; fi \
&& ./config --prefix=/usr/local/Cellar/openssl/1.0.2q/.openssl no-shared \
&& /Library/Developer/CommandLineTools/usr/bin/make \
&& /Library/Developer/CommandLineTools/usr/bin/make install_sw LIBDIR=lib
/bin/sh: ./config: No such file or directory
make[2]: *** [/usr/local/Cellar/openssl/1.0.2q/.openssl/include/openssl/ssl.h] Error 127
make[1]: *** [build] Error 2
make: *** [all] Error 2
```
这是上面configure时没有指定openssl源码，需要从[OpenSSL](https://www.openssl.org/source/)下载对应的源码，我这里下载的是openssl3.0，那么configure如下：
```
./configure --with-openssl=/Users/user/Documents/develop/env/openssl-3.0.0 -j8
```

## 3、前后端分离项目部署

```conf
server
    {
        listen 80;
        server_name 127.0.0.1;
        index index.html;
        root  /home/sonar/dist;  #dist上传的路径

        # 避免访问出现 404 错误
        location / {
          try_files $uri $uri/ @router;
          index  index.html;
        }

        location @router {
          rewrite ^.*$ /index.html last;
        }

        # 接口
        location /api {
          proxy_pass http://172.17.0.1:8000;
          proxy_set_header X-Forwarded-Proto $scheme;
          proxy_set_header X-Forwarded-Port $server_port;
          proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
          proxy_set_header Upgrade $http_upgrade;
          proxy_set_header Connection "upgrade";
        }

        # 授权接口
        location /auth {
          proxy_pass http://172.17.0.1:8000;
          proxy_set_header X-Forwarded-Proto $scheme;
          proxy_set_header X-Forwarded-Port $server_port;
          proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
          proxy_set_header Upgrade $http_upgrade;
          proxy_set_header Connection "upgrade";
        }

        # WebSocket 服务
        location /webSocket {
          proxy_pass http://172.17.0.1:8000;
          proxy_set_header X-Forwarded-Proto $scheme;
          proxy_set_header X-Forwarded-Port $server_port;
          proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
          proxy_set_header Upgrade $http_upgrade;
          proxy_set_header Connection "upgrade";
        }

        # 头像
        location /avatar {
          proxy_pass http://172.17.0.1:8000;
        }

        # 文件
        location /file {
          proxy_pass http://172.17.0.1:8000;
        }
    }

```
