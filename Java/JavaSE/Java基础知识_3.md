<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [二十一、Java Agent](#%E4%BA%8C%E5%8D%81%E4%B8%80java-agent)
  - [1、Java agent](#1java-agent)
  - [2、手动编写java agent](#2%E6%89%8B%E5%8A%A8%E7%BC%96%E5%86%99java-agent)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->



# 二十一、Java Agent

- [Java探针技术](https://www.cnblogs.com/aspirant/p/8796974.html)
- [Java Agent](https://www.jianshu.com/p/5bfe16c9ce4e)
- [Java Agent类隔离](https://mp.weixin.qq.com/s/6dyHV2yyccJxgTEOKBUgTA)

## 1、Java agent

JDK1.5之后引进的，也可以叫做Java代理，JavaAgent 是运行在 main方法之前的拦截器，它内定的方法名叫 premain ，也就是说先执行 premain 方法然后再执行 main 方法
- 支持方法执行耗时范围抓取设置，根据耗时范围抓取系统运行时出现在设置耗时范围的代码运行轨迹。
- 支持抓取特定的代码配置，方便对配置的特定方法进行抓取，过滤出关系的代码执行耗时情况。
- 支持APP层入口方法过滤，配置入口运行前的方法进行监控，相当于监控特有的方法耗时，进行方法专题分析。
- 支持入口方法参数输出功能，方便跟踪耗时高的时候对应的入参数。
- 提供WEB页面展示接口耗时展示、代码调用关系图展示、方法耗时百分比展示、可疑方法凸显功能

Java agent也是一个jar包，只是其启动方式和普通Jar包有所不同，Java agent并不能单独启动，必须依附在一个应用程序中运行；

其原理：
我们利用Java代理和ASM字节码技术，在JVM加载class二进制文件的时候，利用ASM动态的修改加载的class文件，在监控的方法前后添加计时器功能，用于计算监控方法耗时，同时将方法耗时及内部调用情况放入处理器，处理器利用栈先进后出的特点对方法调用先后顺序做处理，当一个请求处理结束后，将耗时方法轨迹和入参map输出到文件中，然后根据map中相应参数或耗时方法轨迹中的关键代码区分出我们要抓取的耗时业务。最后将相应耗时轨迹文件取下来，转化为xml格式并进行解析，通过浏览器将代码分层结构展示出来，方便耗时分析

## 2、手动编写java agent

- 在META-INF目录下创建MANIFEST文件
    ```
    Manifest-Version: 1.0
    Agent-Class: com.blue.fish.agent.AgentBoot
    Premain-Class: com.blue.fish.agent.AgentBoot
    Can-Redefine-Classes: true
    Can-Retransform-Classes: true
    ```

- 并在MANIFEST文件中指定Agent的启动类， 在加载Java Agent之后，会找到Agent-Class或者Premain-Class指定的类，并运行对应的agentmain或者premain方法

    ```java
    /**
     * 以vm参数的方式载入，在Java程序的main方法执行之前执行
     */
    public static void premain(String agentArgs, Instrumentation inst);
    /**
     * 以Attach的方式载入，在Java程序启动后执行
     */
    public static void agentmain(String agentArgs, Instrumentation inst);
    ```
















