

# 二十一、Java Agent
[Java探针技术](https://www.cnblogs.com/aspirant/p/8796974.html)
[Java Agent](https://www.jianshu.com/p/5bfe16c9ce4e)

## 1、Java agent
JDK1.5之后引进的，也可以叫做Java代理，JavaAgent 是运行在 main方法之前的拦截器，它内定的方法名叫 premain ，也就是说先执行 premain 方法然后再执行 main 方法
- 支持方法执行耗时范围抓取设置，根据耗时范围抓取系统运行时出现在设置耗时范围的代码运行轨迹。
- 支持抓取特定的代码配置，方便对配置的特定方法进行抓取，过滤出关系的代码执行耗时情况。
- 支持APP层入口方法过滤，配置入口运行前的方法进行监控，相当于监控特有的方法耗时，进行方法专题分析。
- 支持入口方法参数输出功能，方便跟踪耗时高的时候对应的入参数。
- 提供WEB页面展示接口耗时展示、代码调用关系图展示、方法耗时百分比展示、可疑方法凸显功能

Java agent也是一个jar包，只是其启动方式和普通Jar包有所不同，Java agent并不能单独启动，必须依附在一个应用程序中运行；

## 2、手动编写java agent
- 在META-INF目录下创建MANIFEST文件
















