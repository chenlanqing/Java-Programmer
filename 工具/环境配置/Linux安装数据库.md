<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、CentOS安装mysql数据库](#%E4%B8%80centos%E5%AE%89%E8%A3%85mysql%E6%95%B0%E6%8D%AE%E5%BA%93)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->





# 一、CentOS安装mysql数据库

[CentOS7.2使用yum安装MYSQL5.7.10](https://typecodes.com/linux/yuminstallmysql5710.html)

- **1、环境：centOS**

    查看Linux发行版本
    ```
    [root@localhost mysql]# cat /etc/redhat-release 
    CentOS Linux release 7.0.1406 (Core) 
    ```

- **2、下载MySQL官方的Yum Repository**

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

- **3、安装MySQL的Yum Repository**

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

- **4、安装MySQL数据库的服务器版本**

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

- **5、启动数据库**

    ```
    [root@localhost mysql]# systemctl start mysqld.service
    ```
    然后使用命令 systemctl status mysqld.service 查看MySQL数据库启动后的服务状态

- **6、获取初始密码**

    使用YUM安装并启动MySQL服务后，MySQL进程会自动在进程日志中打印root用户的初始密码
    ```
    [root@localhost mysql]# grep "password" /var/log/mysqld.log 
    2018-06-04T06:30:41.267699Z 1 [Note] A temporary password is generated for root@localhost: _)srryI*d2zX
    2018-06-04T06:30:42.150462Z 1 [ERROR] Failed to open the bootstrap file /var/lib/mysql-files/install-validate-password-plugin.Zf9heV.sql
    ```

- **7、修改root用户密码**  

    ```sql
    ALTER USER 'root'@'localhost' IDENTIFIED BY 'new password';
    ```

- **8、查看MySQL数据库的配置信息**

    MySQL的配置文件依然是/etc/my.cnf，其它安装信息可以通过mysql_config命令查看。其中，动态库文件存放在/usr/lib64/mysql目录下

- **9、解决外部无法连接mysql**

    - （1）查看是否打开3306端口：

        vi /etc/sysconfig/iptables

        增加下面一行：

        -A RH-Firewall-1-INPUT -m state --state NEW -m tcp -p tcp --dport 3306-j ACCEPT
        ```
        开放端口：
        firewall-cmd --zone=public --add-port=3306/tcp --permanent
        firewall-cmd --reload
        ```
    - （2）重启防火墙

        systemctl restart iptables 

        如果上述命令报错：Unit iptables.service failed to load，因为在CentOS 7或RHEL 7或Fedora中防火墙由firewalld来管理，执行如下命令：
        ```
        [root@localhost ~]# systemctl stop firewalld
        [root@localhost ~]# systemctl mask firewalld
        ln -s '/dev/null' '/etc/systemd/system/firewalld.service'
        [root@localhost ~]# yum install iptables-services
        ```
        设置开启启动：

        systemctl enable iptables
    
    - （3）授权支持远程登录

        - 支持所有用户远程连接：允许远程主机以 root 账号（密码是root）连接数据库
            ```sql
            grant all privileges on *.* to 'root'@'%' identified by 'root' with grant option;
            ```
        - 如果只允许某台主机连接root从1.1.1.1的主机连接到服务器
            ```sql
            grant all privileges on *.* to 'root'@'1.1.1.1' identified by 'root' with grant option;
            ```

*删除MySQL的Repository*

因为安装了MySQL的Yum Repository，所以以后每次执行yum操作时，都会去检查更新。如果想要去掉这种自动检查操作的话，可以使用如下命令卸载MySQL的Repository即可

[root@localhost mysql]# yum -y remove mysql57-community-release-el7-7.noarch.rpm