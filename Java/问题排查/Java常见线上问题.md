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
    jmap -heap <pid>
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

# 四、死锁问题

## 1、问题背景

- 问题1：有一个系统A同时对接了两个系统B、C，B、C系统都会同时请求系统A去更新某个表的某个字段值，因为都是批量更新导致死锁问题；
- 问题2：有一个系统A同时对接了两个系统B、C，B、C系统都会通知系统A更新某个表的某个字段，更新的where条件中有两个字段，两个字段都是单值索引，导致更新时存在死锁；根据两个条件查询时的执行计划type是`index_merge`

    ```sql
    create table test(
        `id`  bigint(20) NOT NULL AUTO_INCREMENT,
        `code` varchar(255) not null comment '编号',
        `parentCode` varchar(255) not null comment '父类编号',
        `price` decimal(19,9) default null,
        PRIMARY KEY (`id`),
        KEY `key_code` (`code`),
        KEY `key_parentCode` (`parentCode`)
    )ENGINE=InnoDB;

    explain update test set price = 2.66 where code='001' and parentCode = 'XS0001';

    第一个事务：根据code索引，已经锁住primary id，然后再根据parentCode索引锁定primary id；
    第二个事务：根据parentCode索引锁定primary id，然后再根据code索引，已经锁住primary id；
    所以这样并发更新就可能出现死索引
    ```

## 2、问题定位

问题2定位
- 环境：MySQL的事务隔离级别Read Committed

## 3、解决方案

- 问题1：每次更新前都会特定的唯一键的进行排序处理，按照顺序去更新；
- 问题2：
    - 查询出对应数据的主键，通过主键更新数据；
    - 将code和parentCode作为组合索引，可以避免掉index_merge；
    - 将优化器的index merge优化关闭；

## 4、反思

对于第二个问题，创建合理的索引，更新时尽量使用主键来更新；

# 五、长事务、大事务

## 1、如何避免长事务对业务的影响

**首先，从应用开发端来看：**
- 确认是否使用了 `set autocommit=0`；
- 确认是否有不必要的只读事务；
- 业务连接数据库的时候，根据业务本身的预估，通过 `set max_execution_time` 命令来控制每个语句执行的最长时间，避免单个语句意外执行太长时间；

**其次，从数据库端来看：**
- 监控`information_schema.Innodb_trx`表，设置长事务阈值，超过就报警或者kill；
- Percona的pt-kill工具使用；
- 使用更高版本的MySQL

# 参考资料

- [官方文档：思路和工具](https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/memleaks004.html#CIHIEEFH)
- [一次堆外内存泄露的排查过程](https://mp.weixin.qq.com/s/bkzVCjm0qeREpCnF_8xX2A)
- [spring boot引起的“堆外内存泄漏”](https://mp.weixin.qq.com/s/warAkXqkpDXOA10wN0vv-Q)
- [一次HashSet所引起的并发问题](https://mp.weixin.qq.com/s/-_6fDP6OSse-tL-ptjcbJw)
- [YGC实战](https://mp.weixin.qq.com/s/6hNg955-DcnkUQ9f6mNEQg)
- [CPU与老年代占用过高问题排查](https://mp.weixin.qq.com/s/-LjR4zSEF0loIexqHJ9hUw)
- [JVM发生OOM的8种原因](https://mp.weixin.qq.com/s/dzZjCx1WzjLZCXLr8V-ukA)
- [数据库的死锁问题排查过程-由索引问题引起的死锁](https://mp.weixin.qq.com/s/3vTElAcvQk-CfgAb4bAiXw)