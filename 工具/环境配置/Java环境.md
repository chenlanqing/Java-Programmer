<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、Windows 下配置环境变量:](#%E4%B8%80windows-%E4%B8%8B%E9%85%8D%E7%BD%AE%E7%8E%AF%E5%A2%83%E5%8F%98%E9%87%8F)
- [二、Linux 下安装 jdk](#%E4%BA%8Clinux-%E4%B8%8B%E5%AE%89%E8%A3%85-jdk)
  - [1、rpm安装](#1rpm%E5%AE%89%E8%A3%85)
  - [2、压缩包安装](#2%E5%8E%8B%E7%BC%A9%E5%8C%85%E5%AE%89%E8%A3%85)
- [三、配置 Tomcat 服务器:](#%E4%B8%89%E9%85%8D%E7%BD%AE-tomcat-%E6%9C%8D%E5%8A%A1%E5%99%A8)
- [四、添加 tomcat 启动到 service 中.](#%E5%9B%9B%E6%B7%BB%E5%8A%A0-tomcat-%E5%90%AF%E5%8A%A8%E5%88%B0-service-%E4%B8%AD)
- [五、windows下tomcat和mave配置](#%E4%BA%94windows%E4%B8%8Btomcat%E5%92%8Cmave%E9%85%8D%E7%BD%AE)
  - [1、Maven环境配置:](#1maven%E7%8E%AF%E5%A2%83%E9%85%8D%E7%BD%AE)
  - [2、Tomcat环境配置:](#2tomcat%E7%8E%AF%E5%A2%83%E9%85%8D%E7%BD%AE)
  - [3、Eclipse配置Maven:](#3eclipse%E9%85%8D%E7%BD%AEmaven)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 一、Windows 下配置环境变量

- 下载对应的jdk安装包(下载地址:http://www.oracle.com/technetwork/java/javase/downloads/index.html)

- 安装JDK，注意安装目录不要有中文信息

- win+r 键运行cmd，输入 `java -version`，回车出现如下信息，表示安装成功：
	```
	java version "1.7.0_67"
	Java(TM) SE Runtime Environment (build 1.7.0_67-b01)
	Java HotSpot(TM) 64-Bit Server VM (build 24.65-b04, mixed mode)
	```

- 配置环境变量：
	- 右击`【我的电脑】--->【属性】----->【高级】--->【环境变量】`
	- 选择`【新建系统变量】`--弹出"新建系统变量"对话框，在"变量名"文本框输入 `JAVA_HOME` ，在"变量值"文本框输入JDK的安装路径，单击确定按钮：
	- 在"系统变量"选项区域中查看 PATH 变量，如果不存在，则新建变量 PATH;否则选中该变量，单击"编辑"按钮，在"变量值"文本框的起始位置添加
		`%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;`或者是直接`%JAVA_HOME%\bin;`，单击确定按钮;
	- 在"系统变量"选项区域中查看 `CLASSPATH` 变量，如果不存在,则新建变量`CLASSPATH`；否则选中该变量，单击"编辑"按钮，在"变量值"文本框的起始位置添加
		`.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar;`

	**注意 classpath 变量中，前面的 `.;` 不能去掉**

# 二、Linux 下安装 jdk

- 安装环境：CentOS7
- JDK 下载地址: http://www.oracle.com/technetwork/java/javase/downloads/index.html

## 1、rpm安装

- 检验系统原版本：
	```
	[root@zck ~]# java -version
	java version "1.7.0_"
	OpenJDK Runtime Environment (IcedTea6 1.11.1) (rhel-1.45.1.11.1.el6-x86_64)
	OpenJDK 64-Bit Server VM (build 20.0-b12, mixed mode)
	```
- 进一步查看JDK信息：
	```
	[root@localhost ~]#  rpm -qa | grep java
	javapackages-tools-3.4.1-6.el7_0.noarch
	tzdata-java-2014i-1.el7.noarch
	java-1.7.0-openjdk-headless-1.7.0.71-2.5.3.1.el7_0.x86_64
	java-1.7.0-openjdk-1.7.0.71-2.5.3.1.el7_0.x86_64
	python-javapackages-3.4.1-6.el7_0.noarch

	[root@localhost ~]#  rpm -qa | grep openjdk -i
	```
- 卸载OpenJDK:
	```
	[root@localhost ~]# rpm -e --nodeps tzdata-java-2014i-1.el7.noarch
	[root@localhost ~]# rpm -e --nodeps java-1.7.0-openjdk-headless-1.7.0.71-2.5.3.1.el7_0.x86_64
	[root@localhost ~]# rpm -e --nodeps java-1.7.0-openjdk-1.7.0.71-2.5.3.1.el7_0.x86_64
	```
- 执行rpm安装命令，默认安装在 /usr 下
	```
	[root@zck local]# rpm -ivh jdk-8u144-linux-x64.rpm
	```
- 验证安装:
	```
	[root@localhost usr]# java -version
	java version "1.8.0_144"
	Java(TM) SE Runtime Environment (build 1.8.0_144-b01)
	Java HotSpot(TM) 64-Bit Server VM (build 25.144-b01, mixed mode)
	```
- 配置环境变量:
	```
	[root@localhost usr]# vi /etc/profile
	编辑该文件，在该文件后追加如下配置:
		JAVA_HOME=/usr/java/jdk1.8.0_144
		JRE_HOME=/usr/java/jdk1.8.0_144/jre
		PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin
		CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JRE_HOME/lib
		export JAVA_HOME JRE_HOME PATH CLASSPATH
	[root@localhost ~]# source /etc/profile   //使修改立即生效
	[root@localhost ~]# echo $PATH   //查看PATH值
    ```	

## 2、压缩包安装

- 下载相应的压缩包：[jdk-8u191-linux-x64.tar.gz](http://download.oracle.com/otn-pub/java/jdk/8u191-b12/2787e4a523244c269598db4e85c51e0c/jdk-8u191-linux-x64.tar.gz)
- 解压缩到相应的目录：/root/software/jdk1.8.0_191
- 在当前登录用户下找到 .bash_profile文件，添加如下配置:
	```bash
	# 配置环境变量
	PATH=$PATH:$HOME/bin
	export PATH
	export JAVA_HOME=/home/root/software/jdk1.7.0_79
	export PATH=$JAVA_HOME/bin:$PATH
	export MAVEN_HOME=/home/root/software/apache-maven-3.5.0
	export PATH=${PATH}:${MAVEN_HOME}/bin
	export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
	
	# 配置别名
	alias base="cd /home/root/software"
	alias webapps="cd /home/root/software/apache-tomcat-7.0.69/tomcat_instance1/webapps"
	alias log="tail -f /home/root/software/apache-tomcat-7.0.69/tomcat_instance1/logs/catalina.out"
	alias log2="tail -f /home/root/software/apache-tomcat-7.0.69/tomcat_instance2/logs/catalina.out"
	alias cdlog="cd /home/root/software/apache-tomcat-7.0.69/tomcat_instance1/logs"
	```
    	
# 三、配置 Tomcat 服务器:	
    1、安装说明 
    	安装环境：CentOS-7
    	安装方式：源码安装 
    	软件：apache-tomcat-8.0.14.tar.gz
    	下载地址：http://tomcat.apache.org/download-80.cgi	
    	安装前提：系统必须已安装配置JDK6+
    
    2、拷贝文件到 /usr/local 目录
    	[root@localhost ~]# cd /usr/local  
    	[root@localhost ~]# tar -zxv -f apache-tomcat-8.0.14.tar.gz // 解压压缩包  
    	[root@localhost ~]# rm -rf apache-tomcat-8.0.14.tar.gz // 删除压缩包  
    	[root@localhost ~]# mv apache-tomcat-8.0.14 tomcat //重命名 tomcat目录
    	
    3、启动 tomcat
    	
    4、防火墙开放8080端口增加8080端口到防火墙配置中，执行以下操作：	
    	[root@localhost ~]# vi + /etc/sysconfig/iptables
    	增加如下代码：
    	-A RH-Firewall-1-INPUT -m state --state NEW -m tcp -p tcp --dport 8080 -j ACCEPT
    	重启防火墙
    	[root@localhost ~]# service iptables restart

# 四、添加 tomcat 启动到 service 中.
    在 CentOS-7 使用 systemctl 进行配置
    1、centos7 使用 systemctl 替换了 service命令
    	参考：redhat文档 https://access.redhat.com/documentation/en-US/Red_Hat_Enterprise_Linux/7/html/System_Administrators_Guide/sect-Managing_Services_with_systemd-Services.html#sect-Managing_Services_with_systemd-Services-List 
    	查看全部服务命令：
    	[root@localhost /]# systemctl list-unit-files --type service
    	查看服务
    	[root@localhost /]# systemctl status name.service
    	启动服务
    	[root@localhost /]# systemctl start name.service
    	停止服务
    	[root@localhost /]# systemctl stop name.service
    	重启服务
    	[root@localhost /]# systemctl restart name.service增加开机启动
    	[root@localhost /]# systemctl enable name.service
    	删除开机启动
    	[root@localhost /]# systemctl disable name.service
    	// 其中.service 可以省略。
    
    2、tomcat增加启动参数:tomcat 需要增加一个pid文件
    	在tomca/bin 目录下面，增加 setenv.sh 配置，catalina.sh启动的时候会调用，同时配置java内存参数
    	#add tomcat pid
    	CATALINA_PID="$CATALINA_BASE/tomcat.pid"
    	#add java opts
    	JAVA_OPTS="-server -XX:PermSize=256M -XX:MaxPermSize=1024m -Xms512M -Xmx1024M -XX:MaxNewSize=256m"
    
    3、增加tomcat.service
    	在/usr/lib/systemd/system目录下增加tomcat.service，目录必须是绝对目录。
    	[Unit]
    	Description=Tomcat
    	After=syslog.target network.target remote-fs.target nss-lookup.target
    	 
    	[Service]
    	Type=forking
    	PIDFile=/data/tomcat/tomcat.pid
    	ExecStart=/data/tomcat/bin/startup.sh 
    	ExecReload=/bin/kill -s HUP $MAINPID
    	ExecStop=/bin/kill -s QUIT $MAINPID
    	PrivateTmp=true
    	 
    	[Install]
    	WantedBy=multi-user.target
    
    	[unit]配置了服务的描述，规定了在network启动之后执行。[service]配置服务的pid，服务的启动，停止，重启。[install]配置了使用用户。
    
    4，使用tomcat.service配置开机启动
    	[root@localhost /]# systemctl enable tomcat
    	启动tomcat
    	[root@localhost /]# systemctl start tomcat
    	停止tomcat
    	[root@localhost /]# systemctl stop tomcat
    	重启tomcat
    	[root@localhost /]# systemctl restart tomcat
    
    	因为配置pid，在启动的时候会再tomcat根目录生成tomcat.pid文件，停止之后删除。
    	同时tomcat在启动时候，执行start不会启动两个tomcat，保证始终只有一个tomcat服务在运行。
    	多个tomcat可以配置在多个目录下，互不影响	
    	
# 五、windows下tomcat和mave配置
	通用步骤: 1.【我的电脑】--> 【属性】-->【高级系统设置】 --> 【环境变量】;
## 1、Maven环境配置:
	(1).在系统变量中添加:MAVEN_HOME,变量值为maven的安装目录
	(2).在变量:PATH 后面添加:%MAVEN_HOME%\bin
## 2、Tomcat环境配置:
	(1).新建变量名：CATALINA_BASE,变量值为tomcat的安装目录
	(2).新建变量名：CATALINA_HOME,变量值为tomcat的安装目录
	(3).打开PATH,添加变量值：%CATALINA_HOME%\lib;%CATALINA_HOME%\bin
## 3、Eclipse配置Maven:
	preference--> Maven--->Installtion[添加当前Maven的配置路径]
	User setting --> 选择Maven安装目录下conf下的settings.xml文件

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
