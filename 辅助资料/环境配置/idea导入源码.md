<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、Spring源码导入](#%E4%B8%80spring%E6%BA%90%E7%A0%81%E5%AF%BC%E5%85%A5)
  - [1、本地环境](#1%E6%9C%AC%E5%9C%B0%E7%8E%AF%E5%A2%83)
  - [2、获取源码](#2%E8%8E%B7%E5%8F%96%E6%BA%90%E7%A0%81)
- [二、Tomcat源码导入](#%E4%BA%8Ctomcat%E6%BA%90%E7%A0%81%E5%AF%BC%E5%85%A5)
  - [1、环境配置](#1%E7%8E%AF%E5%A2%83%E9%85%8D%E7%BD%AE)
  - [2、导入步骤](#2%E5%AF%BC%E5%85%A5%E6%AD%A5%E9%AA%A4)
- [参考资料](#%E5%8F%82%E8%80%83%E8%B5%84%E6%96%99)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->



# 一、Spring源码导入

## 1、本地环境

- Gradle
- Git
- JDK8+
- IntelliJ IDEA-2018
- Windows/Mac

## 2、获取源码

从[官方仓库](https://github.com/spring-projects/spring-framework)Fork出属于自己的仓库，这样针对修改或者注释都可以直接提交到自己的仓库中去；

- 使用idea从git创建新项目（https://github.com/chenlanqing/spring-framework.git）
- gradle会自动build项目；
- 预编译spring-oxm项目，在idea上终端上执行命令：`./gradlew :spring-oxm:compileTestJava`


# 二、Tomcat源码导入

## 1、环境配置

- JDK8+
- Maven
- Windows/Mac
- Tomcat环境

## 2、导入步骤

- 下载Tomcat源码，可以从[svn](http://svn.apache.org/repos/asf/tomcat/)上下载
- 在下载的源码目录中新建`pom.xml`文件，文件内容如下：
	```xml
	<?xml version="1.0" encoding="UTF-8"?>
	<project xmlns="http://maven.apache.org/POM/4.0.0"
	         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	    <modelVersion>4.0.0</modelVersion>
	    <groupId>org.apache.tomcat</groupId>
	    <artifactId>Tomcat8.0</artifactId>
	    <name>Tomcat8.0</name>
	    <version>8.0</version>
	    <build>
	        <finalName>Tomcat8.0</finalName>
	        <sourceDirectory>java</sourceDirectory>
	        <testSourceDirectory>test</testSourceDirectory>
	        <resources>
	            <resource>
	                <directory>java</directory>
	            </resource>
	        </resources>
	        <testResources>
	            <testResource>
	                <directory>test</directory>
	            </testResource>
	        </testResources>
	        <plugins>
	            <plugin>
	                <groupId>org.apache.maven.plugins</groupId>
	                <artifactId>maven-compiler-plugin</artifactId>
	                <version>2.3</version>
	                <configuration>
	                    <encoding>UTF-8</encoding>
	                    <source>1.8</source>
	                    <target>1.8</target>
	                </configuration>
	            </plugin>
	        </plugins>
	    </build>
	    <dependencies>
	        <dependency>
	            <groupId>junit</groupId>
	            <artifactId>junit</artifactId>
	            <version>4.12</version>
	            <scope>test</scope>
	        </dependency>
	        <dependency>
	            <groupId>org.easymock</groupId>
	            <artifactId>easymock</artifactId>
	            <version>3.4</version>
	        </dependency>
	        <dependency>
	            <groupId>ant</groupId>
	            <artifactId>ant</artifactId>
	            <version>1.7.0</version>
	        </dependency>
	        <dependency>
	            <groupId>wsdl4j</groupId>
	            <artifactId>wsdl4j</artifactId>
	            <version>1.6.2</version>
	        </dependency>
	        <dependency>
	            <groupId>javax.xml</groupId>
	            <artifactId>jaxrpc</artifactId>
	            <version>1.1</version>
	        </dependency>
	        <dependency>
	            <groupId>org.eclipse.jdt.core.compiler</groupId>
	            <artifactId>ecj</artifactId>
	            <version>4.5.1</version>
	        </dependency>
	    </dependencies>
	</project>	
	```
- 将该工程导入到idea中，这时如果尝试其他tomcat会报错(`org.apache.catalina.startup.Bootstrap#main`)
- 需要设置一个运行时的 catalina_home目录，需要下载一个tomcat软件的压缩包，下载跟你源代码对应的tomcat软件；
- 将该tomcat软件放在和源码同一级目录，建一个和该目录平行的目录catalina-home，将tomcat软件解压后，在解压的目录下，将以下目录拷贝到catalina-home中：
	```
	bin
    conf
    lib
    logs
    temp
    webapps
    work
	```
- 最终目录结构是这样的：
	```
	|-catalina_home
	|-tomcat8
	```

# 参考资料
* [IDEA导入Tomcat源码](http://www.jb51.net/article/95120.htm)


