# 一、Vmware Fusion虚拟机配置静态IP

https://www.cnblogs.com/itbsl/p/10998696.html#commentform

`cd /etc/sysconfig/network-scripts`

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

# 二、设置hostname

Linux系统：CentOS7

`hostnamectl set-hostname <yourhostname>`

或者 

编辑文件： `vi /etc/sysconfig/network`，修改里面的 HOSTNAME 配置

# 三、11、ssh免密登录

机器环境，有三台机器，配置了对应的hosts和hostname
- 192.168.89.141 hadoop001
- 192.168.89.142 hadoop002
- 192.168.89.143 hadoop003

## 1、本机免密登录

在hadoop001机器上免密登录 hadoop001，那么操作如下：
- 生成公钥：`ssh-keygen -t rsa`，执行这个命令以后，需要连续按 4 次回车键回到 linux 命令行才表示这个操作执行 结束，在按回车的时候不需要输入任何内容
- 向本机复制公钥：`cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys`
- 免密登录：`ssh hadoop100`

## 2、免密登录远程机器

比如上面hadoop001需要免密登录 hadoop002、hadoop003 两台机器，由于hadoop001上已经生成了公钥，分别执行如下命令：
```
ssh-copy-id -i hadoop002
ssh-copy-id -i hadoop003
```

# 四、宿主机与虚拟机网络连通

## 1、Mac连接到虚拟机的docker网络

```
Mac IP地址: 192.168.31.196
虚拟机IP: 192.168.31.221
docker网络IP地址: 172.19.0.3

添加路由到Mac的路由表：
sudo route -n add -net 172.19.0.0 -netmask 255.255.255.0 192.168.31.221

路由表信息如下：
Internet:
Destination        Gateway            Flags        Refs      Use   Netif Expire
172.19/24          192.168.31.221     UGSc            0        3     en0

如果要删除路由表信息:
sudo route delete 172.19/24
```

**注意：**需要关闭虚拟机的防火墙
- ubuntu关闭防火墙：`sudo ufw disable`

## 2、Windows连接到虚拟机的docker网络

```
Windows IP地址: 192.168.31.196
虚拟机IP: 192.168.31.221
docker网络IP地址: 172.19.0.3

ROUTE -p add 172.19.0.0 mask 255.255.255.0 192.168.31.221

添加网络路由，172.19.0.0 mask 255.255.255.0 都由 192.168.31.221 来帮忙转发

删除路由：
route delete 172.19.0.0 mask 255.255.255.0 192.168.31.221
```
**注意：**需要关闭虚拟机的防火墙
