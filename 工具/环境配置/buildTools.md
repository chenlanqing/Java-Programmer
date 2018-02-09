<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

  - [一.Maven](#%E4%B8%80maven)
- [二.gradle](#%E4%BA%8Cgradle)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## 一.Maven 
    1.下载对应文件,一般下载binary文件,如下载:apache-maven-3.3.9-bin.zip 文件(下载地址:http://maven.apache.org/download.cgi)
    2.解压该文件到对应的目录下;
    3.右击【我的电脑】---【属性】-----【高级】---【环境变量】
    4.新建系统变量:
    	变量名:"MAVEN_HOME"	变量值:"D:\develop\Java\apache-maven-3.3.9"
    5.修改变量名"PATH",追加值:";%MAVEN_HOME%\bin"
    6.在dos下执行命令:mvn --version,输出如下结果,表示配置成功
    	Apache Maven 3.3.9 (bb52d8502b132ec0a5a3f4c09453c07478323dc5; 2015-11-11T00:41:47+08:00)
    	Maven home: D:\develop\Java\apache-maven-3.3.9
    	Java version: 1.7.0_67, vendor: Oracle Corporation
    	Java home: D:\develop\Java\jdk1.7.0_67\jre
    	Default locale: zh_CN, platform encoding: GBK
    	OS name: "windows 8.1", version: "6.3", arch: "amd64", family: "windows"
    7.修改本地仓库地址,在目录 D:\develop\Java\apache-maven-3.3.9\conf 找到 settings.xml,修改对应的配置:
    	<localRepository>D:/repo</localRepository>
    8.国外的镜像仓库maven下载速度比较慢,可以使用国内的镜像,在 settings.xml 下 的 mirros 节点下新增内容:
    	<mirrors>
    		<mirror>
    			<id>alimaven</id>
    			<name>aliyun maven</name>
    			<url>http://maven.aliyun.com/nexus/content/groups/public/</url>
    			<mirrorOf>central</mirrorOf>        
    		</mirror>
    	</mirrors>
    
    9.如果使用 eclipse的话需要在其配置如下:
    	windows-preferences-Maven-User Settings
    	将 global settings 设置为当前配置好的maven的 settings 路径:D:\develop\Java\apache-maven-3.3.9\conf\settings.xml
# 二.gradle
    1.安装jdk,且JDK版本不低于1.5.因为Gradle是用Groovy编写的,而Groovy基于JAVA
    2.下载对应的文件,可以下载完整版或者只下载二进制文件(下载地址 https://gradle.org/releases/)
    3.解压该文件到对应的目录下,包含如下内容:
    	二进制文件
    	用户手册(包括PDF和HTML两种版本)
    	DSL参考指南
    	API手册(包括Javadoc和Groovydoc)
    	样例
    	源代码,仅供参考使用
    4.配置环境变量:配置 GRADLE_HOME 到你的gradle根目录当中,%GRADLE_HOME%/bin 添加到 path 变量中
    5.验证安装:gradle -v
    	------------------------------------------------------------
    	Gradle 4.3
    	------------------------------------------------------------
    	Build time:   2017-10-30 15:43:29 UTC
    	Revision:     c684c202534c4138b51033b52d871939b8d38d72
    	Groovy:       2.4.12
    	Ant:          Apache Ant(TM) version 1.9.6 compiled on June 29 2015
    	JVM:          1.7.0_67 (Oracle Corporation 24.65-b04)
    	OS:           Windows 8.1 6.3 amd64









