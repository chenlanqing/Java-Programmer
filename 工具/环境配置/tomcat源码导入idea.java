// http://www.jb51.net/article/95120.htm
1.下载安装maven;
2.下载tomcat源代码,下载地址-http://svn.apache.org/repos/asf/tomcat/
3.在下载的源码目录中新建 pom.xml 文件,文件内容如下:
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
4.将该工程导入到idea中,这时如果尝试其他 tomcat会报错(org.apache.catalina.startup.Bootstrap#main)
5.需要设置一个运行时的 catalina_home目录,需要下载一个tomcat软件的压缩包,下载跟你源代码对应的tomcat软件;
6.将该tomcat软件放在和源码同一级目录,建一个和该目录平行的目录catalina-home,将tomcat软件解压后,在解压的目录下,将以下目录拷贝到catalina-home中:
	bin
    conf
    lib
    logs
    temp
    webapps
    work
7.最终目录结构是这样的:
	|-catalina_home
	|-tomcat8






