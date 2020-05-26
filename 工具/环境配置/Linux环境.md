# 一、CentOS安装mysql数据库

[CentOS7.2使用yum安装MYSQL5.7.10](https://typecodes.com/linux/yuminstallmysql5710.html)

## 1、环境

centOS7，查看Linux发行版本
```
[root@localhost mysql]# cat /etc/redhat-release 
CentOS Linux release 7.0.1406 (Core) 
```

## 2、下载MySQL官方的Yum Repository

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

## 3、安装MySQL的Yum Repository

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

## 4、安装MySQL数据库的服务器版本

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

## 5、启动数据库

```
[root@localhost mysql]# systemctl start mysqld.service
```
然后使用命令 systemctl status mysqld.service 查看MySQL数据库启动后的服务状态

## 6、获取初始密码

使用YUM安装并启动MySQL服务后，MySQL进程会自动在进程日志中打印root用户的初始密码
```
[root@localhost mysql]# grep "password" /var/log/mysqld.log 
2018-06-04T06:30:41.267699Z 1 [Note] A temporary password is generated for root@localhost: _)srryI*d2zX
2018-06-04T06:30:42.150462Z 1 [ERROR] Failed to open the bootstrap file /var/lib/mysql-files/install-validate-password-plugin.Zf9heV.sql
```

## 7、修改root用户密码

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

## 8、查看MySQL数据库的配置信息

MySQL的配置文件依然是`/etc/my.cnf`，其它安装信息可以通过mysql_config命令查看。其中，动态库文件存放在`/usr/lib64/mysql`目录下

## 9、解决外部无法连接mysql

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
        grant all privileges on *.* to 'root'@'%' identified by 'root' with grant option;
        ```
    - 如果只允许某台主机连接root从1.1.1.1的主机连接到服务器
        ```sql
        grant all privileges on *.* to 'root'@'1.1.1.1' identified by 'root' with grant option;
        ```

## 10、删除MySQL的Repository

因为安装了MySQL的Yum Repository，所以以后每次执行yum操作时，都会去检查更新。如果想要去掉这种自动检查操作的话，可以使用如下命令卸载MySQL的Repository即可

`[root@localhost mysql]# yum -y remove mysql57-community-release-el7-7.noarch.rpm`

# 二、MySQL主从配置

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

# 四、RabbitMQ


# 五、RocketMQ


# 六、ElasticSearch