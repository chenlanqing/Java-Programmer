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
		JAVA_HOME=/usr/java/jdk1.8.0_231-amd64
		JRE_HOME=/usr/java/jdk1.8.0_231-amd64/jre
		PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin
		CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JRE_HOME/lib
		export JAVA_HOME JRE_HOME PATH CLASSPATHec
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
export JAVA_HOME=/usr/local/jdk1.8.0_271
export PATH=$JAVA_HOME/bin:$PATH
export MAVEN_HOME=/usr/local/apache-maven-3.6.3
export PATH=${PATH}:${MAVEN_HOME}/bin
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
	
	# 配置别名
	alias base="cd /home/root/software"
	alias webapps="cd /home/root/software/apache-tomcat-7.0.69/tomcat_instance1/webapps"
	alias log="tail -f /home/root/software/apache-tomcat-7.0.69/tomcat_instance1/logs/catalina.out"
	alias log2="tail -f /home/root/software/apache-tomcat-7.0.69/tomcat_instance2/logs/catalina.out"
	alias cdlog="cd /home/root/software/apache-tomcat-7.0.69/tomcat_instance1/logs"
	```
    	
# 三、配置 Tomcat 服务器

## 1、安装说明 

- 安装环境：CentOS-7
- 安装方式：源码安装 
- 软件：apache-tomcat-8.0.14.tar.gz
- 下载地址：http://tomcat.apache.org/download-80.cgi	
- 安装前提：系统必须已安装配置JDK6+

## 2、拷贝文件到 /usr/local 目录

```
[root@localhost ~]# cd /usr/local  
[root@localhost ~]# tar -zxv -f apache-tomcat-8.0.14.tar.gz // 解压压缩包  
[root@localhost ~]# rm -rf apache-tomcat-8.0.14.tar.gz // 删除压缩包  
[root@localhost ~]# mv apache-tomcat-8.0.14 tomcat //重命名 tomcat目录
```
	
## 3、启动 tomcat

执行tomcat目录下的bin里的startup.sh文件
	
## 4、防火墙开放8080端口增加8080端口到防火墙配置中，执行以下操作

```
[root@localhost ~]# vi + /etc/sysconfig/iptables
增加如下代码：
-A RH-Firewall-1-INPUT -m state --state NEW -m tcp -p tcp --dport 8080 -j ACCEPT
重启防火墙
[root@localhost ~]# service iptables restart
```

对于centos7的话，使用firewall，添加端口：
```
firewall-cmd --zone=public --add-port=8080/tcp --permanent
刷新防火墙
firewall-cmd --reload
```

# 四、添加 tomcat 启动到 service 中.
在 CentOS-7 使用 systemctl 进行配置

## 1、centos7 使用 systemctl 替换了 service命令

```
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
```

## 2、tomcat增加启动参数

tomcat 需要增加一个pid文件，在`tomca/bin` 目录下面，增加 `setenv.sh` 配置，`catalina.sh`启动的时候会调用，同时配置java内存参数
```
#add tomcat pid
CATALINA_PID="$CATALINA_BASE/tomcat.pid"
#add java opts
JAVA_OPTS="-server -XX:PermSize=256M -XX:MaxPermSize=1024m -Xms512M -Xmx1024M -XX:MaxNewSize=256m"
```

## 3、增加tomcat.service

在`/usr/lib/systemd/system`目录下增加`tomcat.service`，目录必须是绝对目录。
```
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
```
`[unit]`配置了服务的描述，规定了在network启动之后执行。`[service]`配置服务的pid，服务的启动，停止，重启。`[install]`配置了使用用户。

## 4、使用tomcat.service配置开机启动

```
[root@localhost /]# systemctl enable tomcat
启动tomcat
[root@localhost /]# systemctl start tomcat
停止tomcat
[root@localhost /]# systemctl stop tomcat
重启tomcat
[root@localhost /]# systemctl restart tomcat
```
因为配置pid，在启动的时候会再tomcat根目录生成`tomcat.pid`文件，停止之后删除。同时tomcat在启动时候，执行start不会启动两个tomcat，保证始终只有一个tomcat服务在运行。多个tomcat可以配置在多个目录下，互不影响	
    	
# 五、windows下tomcat

编辑路径: `【我的电脑】--> 【属性】-->【高级系统设置】 --> 【环境变量】`;

- 新建变量名：`CATALINA_BASE`，变量值为tomcat的安装目录
- 新建变量名：`CATALINA_HOME`，变量值为tomcat的安装目录
- 打开PATH,添加变量值：`%CATALINA_HOME%\lib;%CATALINA_HOME%\bin`


# 六、Maven 

下载对应文件，一般下载binary文件，如下载：[apache-maven-3.3.9-bin.zip](下载地址:http://maven.apache.org/download.cgi)；

## 1、windows

- 解压该文件到对应的目录下，比如D盘，具体目录：`D:\develop\Java\apache-maven-3.3.9`；
- 右击`【我的电脑】--->【属性】----->【高级】--->【环境变量】`；
- 新建系统变量，变量名：`MAVEN_HOME`，变量值：`D:\develop\Java\apache-maven-3.3.9`；
- 修改变量名`PATH`，追加值：`;%MAVEN_HOME%\bin`，注意前面的分号不能少；
- 在dos下执行命令：`mvn --version`，输出如下结果，表示配置成功
	```
	Apache Maven 3.3.9 (bb52d8502b132ec0a5a3f4c09453c07478323dc5; 2015-11-11T00:41:47+08:00)
	Maven home: D:\develop\Java\apache-maven-3.3.9
	Java version: 1.7.0_67, vendor: Oracle Corporation
	Java home: D:\develop\Java\jdk1.7.0_67\jre
	Default locale: zh_CN, platform encoding: GBK
	OS name: "windows 8.1", version: "6.3", arch: "amd64", family: "windows"
	```
- 修改本地仓库地址，在目录 `D:\develop\Java\apache-maven-3.3.9\conf` 找到 `settings.xml`，修改对应的配置：`<localRepository>D:/repo</localRepository>`；不修改的话默认是用户目录下的 `.m2/repository`
- 国外的镜像仓库maven下载速度比较慢，可以使用国内的镜像，比如阿里云，在 settings.xml 下 的 mirros 节点下新增内容:
	```xml
	<mirrors>
		<mirror>
			<id>alimaven</id>
			<name>aliyun maven</name>
			<url>http://maven.aliyun.com/nexus/content/groups/public/</url>
			<mirrorOf>central</mirrorOf>        
		</mirror>
	</mirrors>
	```

## 2、Linux（Mac等）

- 解压文件到对应的目录，比如：`/usr/local/apache-maven-3.3.9`
- 编辑文件：`/etc/profile`，需要root权限，增加如下：
	```
	export M2_HOME=/usr/local/apache-maven-3.3.9
	export PATH=$PATH:$JAVA_HOME/bin:$M2_HOME/bin:
	```
- 使文件`/etc/profile`生效：`source /etc/profile`；
- 同理按照windows环境下配置本地Maven仓库和远程镜像仓库；
 











# 参考文档

- [CentOS-7](https://access.redhat.com/documentation/en-us/red_hat_enterprise_linux/7/html/migration_planning_guide/index)
