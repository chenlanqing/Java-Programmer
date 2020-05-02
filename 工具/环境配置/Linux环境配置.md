# 一、Vmware Fusion虚拟机配置静态IP

https://www.cnblogs.com/itbsl/p/10998696.html#commentform

网卡配置：
``` 
TYPE="Ethernet"
PROXY_METHOD="none"
BROWSER_ONLY="no"
BOOTPROTO="static"
DEFROUTE="yes"
IPV4_FAILURE_FATAL="no"
IPV6INIT="yes"
IPV6_AUTOCONF="yes"
IPV6_DEFROUTE="yes"
IPV6_FAILURE_FATAL="no"
IPV6_ADDR_GEN_MODE="stable-privacy"
NAME="ens33"
UUID="dace9c25-39e1-40d5-9fd7-515e81e45d76"
DEVICE="ens33"
ONBOOT="yes"
IPADDR="192.168.89.165"
NETMASK="255.255.255.0"
GATEWAY="192.168.89.2"
DNS1="192.168.31.1"
```

# 二、Redis安装

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
