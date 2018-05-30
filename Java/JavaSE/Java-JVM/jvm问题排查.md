

# 一、问题1：线上tomcat运行几天后宕机

分析日志之后发现在tomcat刚启动的时候内存占用比较少，但是运行个几天之后内存占用越来越大，通过jmap命令可以查询到一些大对象引用没有被及时GC，这里就要求解决内存泄露的问题

## 1、一般步骤
- （1）用工具生成java应用程序heap dump（如jmap）；
- （2）使用java heap分析工具（如MAT），找出内存占用超出预期的嫌疑对象；
- （3）根据情况，分析嫌疑对象与其他对象的引用关系；
- （4）分析程序源代码，找出嫌疑对象数量过多的原因；

## 2、实际项目过程

- （1）获取tomcat的pid

    ps -ef| grep java

- （2）利用jmap初步分析内存映射，命令：

    jmap -histo:live [pid] | head -7

- （3）如果上面一步还无法定位到关键信息，那么需要拿到heap dump，生成离线文件，做进一步分析，命令：

    jmap -dump:live,format=b,file=heap.hprof [pid]

- （4）4. 拿到heap dump文件，利用eclipse插件MAT来分析heap profile


## 3、总结

内存泄漏的原因分析，总结出来只有一条：存在无效的引用
