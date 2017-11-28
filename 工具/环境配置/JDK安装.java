一.Windows 下配置环境变量:
1.下载对应的jdk安装包(下载地址:http://www.oracle.com/technetwork/java/javase/downloads/index.html)
2.安装JDK,注意安装目录不要有中文信息
3.win+r 键运行cmd,输入 java -version,回车出现如下信息,表示安装成功:
	java version "1.7.0_67"
	Java(TM) SE Runtime Environment (build 1.7.0_67-b01)
	Java HotSpot(TM) 64-Bit Server VM (build 24.65-b04, mixed mode)
4.配置环境变量:
	4.1.右击【我的电脑】---【属性】-----【高级】---【环境变量】
	4.2.选择【新建系统变量】--弹出"新建系统变量"对话框,在"变量名"文本框输入 JAVA_HOME ,在"变量值"文本框输入JDK的安装路径,单击确定按钮:
	4.3.在"系统变量"选项区域中查看 PATH 变量,如果不存在,则新建变量 PATH;否则选中该变量,单击"编辑"按钮,在"变量值"文本框的起始位置添加
		"%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;"或者是直接"%JAVA_HOME%\bin;",单击确定按钮;
	4.4.在"系统变量"选项区域中查看 CLASSPATH 变量,如果不存在,则新建变量CLASSPATH;否则选中该变量,单击"编辑"按钮,在"变量值"文本框的起始位置添加
		".;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar;"
		==> 注意 classpath 变量中,前面的 ".;" 不能去掉
二.Linux 下安装 jdk
1.安装环境:CentOS7
2.JDK 下载地址: http://www.oracle.com/technetwork/java/javase/downloads/index.html
3.检验系统原版本:
	[root@zck ~]# java -version
	java version "1.7.0_"
	OpenJDK Runtime Environment (IcedTea6 1.11.1) (rhel-1.45.1.11.1.el6-x86_64)
	OpenJDK 64-Bit Server VM (build 20.0-b12, mixed mode)
4.进一步查看JDK信息:
	[root@localhost ~]#  rpm -qa | grep java
	javapackages-tools-3.4.1-6.el7_0.noarch
	tzdata-java-2014i-1.el7.noarch
	java-1.7.0-openjdk-headless-1.7.0.71-2.5.3.1.el7_0.x86_64
	java-1.7.0-openjdk-1.7.0.71-2.5.3.1.el7_0.x86_64
	python-javapackages-3.4.1-6.el7_0.noarch
5.卸载OpenJDK:
	[root@localhost ~]# rpm -e --nodeps tzdata-java-2014i-1.el7.noarch
	[root@localhost ~]# rpm -e --nodeps java-1.7.0-openjdk-headless-1.7.0.71-2.5.3.1.el7_0.x86_64
	[root@localhost ~]# rpm -e --nodeps java-1.7.0-openjdk-1.7.0.71-2.5.3.1.el7_0.x86_64
6.执行rpm安装命令，默认安装在 /usr 下
	[root@zck local]# rpm -ivh jdk-8u144-linux-x64.rpm
7.验证安装:
	[root@localhost usr]# java -version
	java version "1.8.0_144"
	Java(TM) SE Runtime Environment (build 1.8.0_144-b01)
	Java HotSpot(TM) 64-Bit Server VM (build 25.144-b01, mixed mode)
8.配置环境变量:
	[root@localhost usr]# vi /etc/profile
	编辑该文件，在该文件后追加如下配置:
		JAVA_HOME=/usr/java/jdk1.8.0_144
		JRE_HOME=/usr/java/jdk1.8.0_144/jre
		PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin
		CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JRE_HOME/lib
		export JAVA_HOME JRE_HOME PATH CLASSPATH
	[root@localhost ~]# source /etc/profile   //使修改立即生效
	[root@localhost ~]# echo $PATH   //查看PATH值