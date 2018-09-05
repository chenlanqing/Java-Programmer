
# 一、线上tomcat宕机

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

- （4）拿到heap dump文件，利用eclipse插件MAT来分析heap profile

## 3、总结

内存泄漏的原因分析，总结出来只有一条：存在无效的引用

# 二、后台服务出现明显“变慢”，如何诊断

## 1、首先明确问题定义
- 服务是突然变慢还是长时间运行后观察到变慢？类似问题是否重复出现？
- “慢”的定义是什么？是否能够理解为系统对其他方面的请求的反应延时变长？

## 2、理清问题症状，有以下思路
- 问题可能来自Java服务自身，也可能是仅仅受系统其他服务的影响。

    初始判断可以先确定是否出现了意外的程序错误，列入检查应用本身的错误日志；

    对于分布式系统，一些Java诊断工具也可以使用，如JFR，监控应用是否大量出现了某种可惜异常；如果有，那么异常可能是个突破点；如果没有，可以先检查系统级别的资源等情况，监控CPU、内存等资源是否被其他进程大量占用，并且这种占用不符合系统正常运行状况；
- 监控Java服务自身，例如GC日志是否观察到了Full GC等恶劣情况出现，或者是否出现MinorGC在变长等；利用jstat等工具，获取内存使用的统计信息也是常有手段，利用jstack检查是否出现死锁等；
- 如果还不能确定具体问题，对应用进行Profiling也是办法，但是因为其对系统产生侵入性，如果非必要，不建议在生成系统进行；
- 定位了程序错误或者JVM配置问题后，采取相应的补救措施，验证是否解决，否则重复上述步骤；

# 三、线上持久代溢出

## 1、问题背景
    
线上服务在某个时间点从注册中心断开，服务无法被调用，在运行定时任务时，抛出```Caused by: java.lang.OutOfMemoryError: PermGen space```

## 2、问题定位

- 查看持久代空间，通过jmap查看持久代，发现持久代到达99%
    ```
    jmap -haap <pid>
    ```
- 通过```jmap -histo <pid>```查看后发现 ```org.codehaus.groovy.runtime.metaclass.MetaMethodIndex$Entry```数量非常多；
- 查看系统日志，发现很多sql操作的地方报如下异常:
    ```
    Caused by: java.lang.NullPointerException: null
        at io.shardingsphere.core.jdbc.metadata.dialect.JDBCShardingRefreshHandler.execute(JDBCShardingRefreshHandler.java:49)
        at io.shardingsphere.core.jdbc.core.statement.ShardingPreparedStatement.execute(ShardingPreparedStatement.java:159)
        at org.apache.ibatis.executor.statement.PreparedStatementHandler.query(PreparedStatementHandler.java:63)
        at org.apache.ibatis.executor.statement.RoutingStatementHandler.query(RoutingStatementHandler.java:79)
        at org.apache.ibatis.executor.SimpleExecutor.doQuery(SimpleExecutor.java:63)
        at org.apache.ibatis.executor.BaseExecutor.queryFromDatabase(BaseExecutor.java:324)
        at org.apache.ibatis.executor.BaseExecutor.query(BaseExecutor.java:156)
        at org.apache.ibatis.executor.CachingExecutor.query(CachingExecutor.java:109)
        at org.apache.ibatis.executor.CachingExecutor.query(CachingExecutor.java:83)
        at org.apache.ibatis.session.defaults.DefaultSqlSession.selectList(DefaultSqlSession.java:148)
    ```
    最后查到是sharding-jdbc的问题；
- 查询sharding-jdbc的官方资料发现其3.0.0.M1版本在长时间运行之后，会发生空指针异常，[详情异常信息](https://github.com/sharding-sphere/sharding-sphere/issues/909)

## 3、解决方案

- 待官方把该问题修复后及时升级版本；
- 把相应的定时任务、同步任务暂时去掉

# 参考资料

- [官方文档：思路和工具](https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/memleaks004.html#CIHIEEFH)
