<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、Tomcat](#%E4%B8%80tomcat)
- [二、Tomcat生命周期](#%E4%BA%8Ctomcat%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F)
- [三、Tomcat 类加载](#%E4%B8%89tomcat-%E7%B1%BB%E5%8A%A0%E8%BD%BD)
  - [1、Web服务器需要解决的问题](#1web%E6%9C%8D%E5%8A%A1%E5%99%A8%E9%9C%80%E8%A6%81%E8%A7%A3%E5%86%B3%E7%9A%84%E9%97%AE%E9%A2%98)
  - [2、Tomcat类库结构](#2tomcat%E7%B1%BB%E5%BA%93%E7%BB%93%E6%9E%84)
  - [3、Tomcat类加载器机制](#3tomcat%E7%B1%BB%E5%8A%A0%E8%BD%BD%E5%99%A8%E6%9C%BA%E5%88%B6)
  - [4、线程上下文类加载器](#4%E7%BA%BF%E7%A8%8B%E4%B8%8A%E4%B8%8B%E6%96%87%E7%B1%BB%E5%8A%A0%E8%BD%BD%E5%99%A8)
  - [5、问题扩展](#5%E9%97%AE%E9%A2%98%E6%89%A9%E5%B1%95)
- [参考文章](#%E5%8F%82%E8%80%83%E6%96%87%E7%AB%A0)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 一、Tomcat

# 二、Tomcat生命周期

# 三、Tomcat 类加载

## 1、Web服务器需要解决的问题

- 部署在同一个Web容器上的两个Web应用程序所使用的Java类库可以实现相互隔离；
- 部署在同一个Web容器上的两个Web应用程序所使用的Java类库可以互相共享；
- Web容器需要尽可能地保证自身的安全不受部署的Web应用程序影响；
- 支持JSP应用的Web容器，大多数都需要支持HotSwap功能；

## 2、Tomcat类库结构

在Tomcat目录结构中，有3组目录("/common/*"、"/server/*"、"/shared/*")可以存放Java类库，另外还可以加上Web应用程序自身的目录“/WEB-INF/*”.放在这四个目录下的含义分别为：

- （1）/common 目录下的：类库可以被Tomcat和所有Web程序共同使用；
- （2）/server目录：类库可以被Tomcat使用，对所有web应用程序不可见；
- （3）/shared目录：类库可被所有的web应用程序共同使用，但对Tomcat自己不可见；
- （4）/WebApp/WEB-INF目录：类库仅仅可以被此Web应用程序使用，对Tomcat和其他Web应用程序都不可见。

为了支持上述目录结构，并对目录里面的类库进行加载和隔离。Tomcat自定义了多个类加载器。

对于Tomcat6.x以后的版本，只有指定了tomcat/conf/catalina.properties配置文件的server.loader和share.loader项后才会真正建立Catalina ClassLoader和Shared ClassLoader的实例，否则在用到这两个类加载器的地方都会用Common ClassLoader的实例代替。而默认的配置文件中没有设置这两个loader项，所以Tomcat 6.x顺理成章地把/common、/server和/shared三个目录默认合并到一起变成一个/lib目录，这个目录里的类库相当于以前/common目录中类库的作用；

## 3、Tomcat类加载器机制

![image](https：//github.com/chenlanqing/learningNote/blob/master/Java/Java源码解读/tomcat/Tomcat类加载机制.jpg)

### 3.1、Tomcat中的类加载器

- Bootstrap 引导类加载器：加载JVM启动所需的类，以及标准扩展类（位于jre/lib/ext下）

- System 系统类加载器：加载tomcat启动的类，比如bootstrap.jar，通常在catalina.bat或者catalina.sh中指定。位于CATALINA_HOME/bin下

- Common 通用类加载器：加载tomcat使用以及应用通用的一些类，位于CATALINA_HOME/lib下，比如servlet-api.jar

- webapp 应用类加载器：每个应用在部署后，都会创建一个唯一的类加载器.该类加载器会加载位于 WEB-INF/lib下的jar文件中的class和 WEB-INF/classes下的class文件

### 3.2、tomcat 类加载顺序

当应用需要到某个类时，则会按照下面的顺序进行类加载：

- （1）使用bootstrap引导类加载器加载
- （2）使用system系统类加载器加载
- （3）使用应用类加载器在WEB-INF/classes中加载
- （4）使用应用类加载器在WEB-INF/lib中加载
- （5）使用common类加载器在CATALINA_HOME/lib中加载

### 3.3、tomcat 如何隔离多个应用

对于每个webapp应用，都会对应唯一的StandContext，在StandContext会引用WebAppLoader，该类又会引用WebAppClassLoader，WebAppClassLoader 就是真正加载webappd的classLoader。WebappClassLoader加载class的步骤：

- （1）先检查webappclassloader的缓冲容器是否有该类；
- （2）为防止webapp覆盖j2se类，尝试用systemclassloader加载；
- （3）进行安全性检查
- （4）通过检查后，判断delegate的值来决定是否委托给父类加载器（默认是否）；
- （5）通过WebappClassLoader自己加载class
- （6）最后无条件地委托给父加载器；
- （7）如果都没有加载成功，则抛出ClassNotFoundException异

*不同的StandardContext有不同的WebappClassLoader，那么不同的webapp的类装载器就是不一致的。装载器的不一致带来了名称空间不一致，所以webapp之间是相互隔离的*

### 3.4、如何破坏双亲委托

- webappClassLoader上面有一个common的类加载器，它是所有webappClassLoader的父加载器，多个应用汇存在公有的类库，而公有的类库都会使用commonclassloader来实现；
- 如果不是公有的类呢，这些类就会使用webappClassLoader加载，而webappClassLoader的实现并没有走双亲委派的模式
- 加载本类的classloader未知时，为了隔离不同的调用者，即类的隔离，采用了上下文类加载的模式加载类.
- 当前高层的接口在低层去实现，而高层的类有需要低层的类加载的时候，这个时候，需要使用上下文类加载器去实现
		
## 4、线程上下文类加载器-ThreadContextClassLoader（TCCL）

问题：在《深入理解java虚拟机》一书中，作者在类加载实践分析tomcat一节中，提出了一个思考题

***如果有10个Web应用程序都是用Spring来进行组织和管理的话，Spring要对用户程序的类进行管理，自然要能访问到用户程序的类，而用户的程序”显然是放在/WebApp/WEB-INF目录中的，那么被CommonClassLoader或 SharedClassLoader加载的Spring如何访问并不在其加载范围内的用户程序呢？***

Java上下文类加载器的作用就是为了SPI机制才存在的；

### 4.1、线程上下文类加载器的产生

Java提供了很多服务提供者接口（Service Provider Interface，SPI），允许第三方接口为这些接口提供实现。常见的SPI有：JDBC、JCE、JNDI、JBI等；这些SPI的接口由Java核心库来提供，而这些SPI的实现代码则作为Java应用所依赖的jar包被包含进类路径里。SPI接口的代码则经常需要加载具体的实现类。

那么问题来了，SPI的接口是Java核心库的一部分，是由启动类加载器来加载的；SPI的实现类是由系统类加载器来加载的。引导类加载器是无法找到SPI的实现类的，因为按照双亲委托模型，Bootstrap ClassLoader无法委派给AppClassLoader来加载类；而线程上下文类加载器破坏了“双亲委托模型”，可以再执行中抛弃双亲委派模型，使程序逆向使用类加载器。有了线程上下文类加载器，JNDI服务使用这个线程上下文类加载器去加载所需要的SPI代码，也就是父类加载器请求子类加载器去完成类加载动作；

使用线程上下文类加载器，可以在执行线程中抛弃双亲委派加载链模式，使用线程上下文里的类加载器加载类。线程上下文从根本解决了一般应用不能违背双亲委派模式的问题。使java类加载体系显得更灵活

### 4.2、线程上下文类加载器应用
java提供是jdbc Driver就是基于SPI的


## 5、问题扩展

- 为什么web应用中src文件夹的会优先jar包中的class？

    因为src文件夹中的文件java以及webContent，中的JSP都会在tomcat启动时，被编译成class文件放在WEB-INF/class 中，外部引用的jar包，则相当于放在 WEB-INF/lib 中；java文件或者JSP文件编译出的class优先加载

- 版本问题：

    在 CATALINA_HOME/lib以及WEB-INF/lib中放置了不同版本的jar包，此时就会导致某些情况下报加载不到类的错误。还有如果多个应用使用同一jar包文件，当放置了多份，就可能导致多个应用间出现类加载不到的错误；

# 四、Tomcat与数据源

https://www.cnblogs.com/ShawnYang/p/7451459.html

# 五、Tomcat调试与监控

## 1、远程调试Tomcat

### 1.1、JDWP协议
Java Debug Wire Protocol缩写，它定义了调试器与被调试的java虚拟机之间通信协议

### 1.2、配置tomcat远程调试

- 修改文件：startup.sh

    在如下脚本中加入 jpda：
    ```shell
    exec "$PRGDIR"/"$EXECUTABLE" jpda start "$@"
    ```

- 修改catalina.sh：
    ```shell
    if [ "$1" = "jpda" ] ; then
        if [ -z "$JPDA_TRANSPORT" ]; then
            JPDA_TRANSPORT="dt_socket"
        fi
        if [ -z "$JPDA_ADDRESS" ]; then
            JPDA_ADDRESS="localhost:8000"
        fi
        if [ -z "$JPDA_SUSPEND" ]; then
            JPDA_SUSPEND="n"
        fi
        if [ -z "$JPDA_OPTS" ]; then
            JPDA_OPTS="-agentlib:jdwp=transport=$JPDA_TRANSPORT,address=$JPDA_ADDRESS,server=y,suspend=$JPDA_SUSPEND"
        fi
        CATALINA_OPTS="$JPDA_OPTS $CATALINA_OPTS"
        shift
    fi
    ```
    修改上述脚本的 JPDA_ADDRESS="localhost:8000"，只需要配置端口即可
    
## 2.tomcat-manager监控
在低版本是默认开启的，而高版本因为安全因素默认是关闭的；

- 文档地址：{tomcat}/webapps/docs/manager-howto.html

- 开启步骤：
    - conf/tomcat-users.xml 添加用户
    - conf/Catalina/localhost/manager.xml 配置允许的远程连接

# 六、Tomcat优化

## 1、内存优化

## 2、线程优化

参考文档：{tomcat}/webapps/docs/config/http.html

- maxConnections：最大连接数

- acceptCount

- maxThreads：工作线程数

- minSpareThreads：最小空闲的工作线程

## 3、配置优化

- autoDeploy

    host.html

- enableLookups：DNS查询

    http.html

- reloadable：false

    contex.html

- protocol

    server.xml

# 七、其他
## 1、Tomcat控制输出乱码：
在catalina.sh文件中找到`JAVA_OPTS`
```
JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDetails  -XX:+PrintGCTimeStamps  -XX:+PrintGCDateStamps  -Xloggc:$CATALINA_HOME/logs/gc.log -Dorg.apache.catalina.security.SecurityListener.UMASK=`umask`"
```
加上：`-Dfile.encoding=UTF8 -Dsun.jnu.encoding=UTF8`

# 参考文章

* [类加载体系](http：//blog.csdn.net/beliefer/article/details/50995516)
* [Tomcat源码分析](https：//blog.csdn.net/column/details/tomcat7-internal.html)
* [Tomcat基本结构](http：//zouzls.github.io/2017/03/29/SpringStart/)
* [线程上下文类加载器](https://blog.csdn.net/yangcheng33/article/details/52631940)
* [JDWP 协议及实现](https://www.ibm.com/developerworks/cn/java/j-lo-jpda3/)