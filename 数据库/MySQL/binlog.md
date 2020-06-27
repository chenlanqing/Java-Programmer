# 一、BinaryLog

## 1、binlog概述

记录了所有对mysql数据库的修改事件，包括增删查改事件和对表结构的修改事件。且二进制日志中记录是已执行成功的记录

主要用途：
- 复制：MySQL 的 Master-Slave 协议，让 Slave 可以通过监听 Binlog 实现数据复制，达到数据一致的目的；
- 数据恢复：通过 mysqlbinlog 工具恢复数据；
- 增量备份

`log_bin`：binlog日志是否打开，一般来说开启binlog日志大概会有1%的性能损耗，在`my.cnf`配置文件中

## 2、mysql二进制日志格式

查看二进制日志格式：`show variables like 'binlog_format';`

修改二进制日志格式：`set binlog_format=''`

- 基于段的格式：`binlog_format=STATEMENT`，mysql5.7之前默认的格式，每一条会修改数据的 SQL 都会记录在 Binlog 中：只需要记录执行语句的细节和上下文环境，避免了记录每一行的变化，在一些修改记录较多的情况下相比 ROW 类型能大大减少 Binlog 日志量，节约IO，提高性能；还可以用于实时的还原；同时主从版本可以不一样，从服务器版本可以比主服务器版本高
	- 优点：日志记录量相对较小，节约磁盘及网络IO
	- 缺点：必须记录上下文信息，保证语句在从服务器上的执行结果与主服务器上执行结果相同，但是非确定性函数还是无法正确复制，有可能mysql主从服务器数据不一致
- 基于行的格式：`binlog_format=ROW`，mysql5.7之后的默认格式，可以避免主从服务器数据不一致情况，仅保存记录被修改细节，不记录SQL语句上下文相关信息；能非常清晰的记录下每行数据的修改细节，不需要记录上下文相关信息，因此不会发生某些特定情况下的 procedure、function、及 trigger 的调用触发无法被正确复制的问题，任何情况都可以被复制，且能加快从库重放日志的效率，保证从库数据的一致性；
- 混合模式：`binlog_format=MIXED`，上面两种方式混合使用；

## 3、管理binlog

- 查看binlog是否开启：`show variables like 'log_bin';`
- 查看binlog日志格式：`show variables like 'binlog_format';`
- `show master logs`：查看所有binlog的日志列表
- `show master status`：查看最后一个binlog日志的编号名称，及最后一个事件结束的位置
- `flush logs`：刷新binlog，此刻开始产生一个新编号的binlog日志文件
- `reset master`：清空所有的binlog日志

```properties
# 事件查询命令
# IN 'log_name' ：指定要查询的binlog文件名(不指定就是第一个binlog文件)
# FROM pos ：指定从哪个pos起始点开始查起(不指定就是从整个文件首个pos点开始算)
# LIMIT [offset,] ：偏移量(不指定就是0)
# row_count ：查询总条数(不指定就是所有行)
show binlog events [IN 'log_name'] [FROM pos] [LIMIT [offset,] row_count];

# 查看 binlog 内容
show binlog events;
# 查看具体一个binlog文件的内容 （in 后面为binlog的文件名）
show binlog events in 'master.000003';
# 设置binlog文件保存事件，过期删除，单位天
set global expire_log_days=3; 
# 删除当前的binlog文件
reset master; 
# 删除slave的中继日志
reset slave;
# 删除指定日期前的日志索引中binlog日志文件
purge master logs before '2019-03-09 14:00:00';

# 删除指定日志文件
purge master logs to 'master.000003';
```

## 4、查看binlog日志

- `mysqlbinlog --no-defaults -vv --base64-output=DECODE-ROWS <binlog文件>`
- `show binlog events;`：查看第一个 Binlog 日志
- `show binlog events in ‘binlog.000030’;`：查看指定的 Binlog 日志
- `show binlog events in ‘binlog.000030’ from 931;`：从指定的位置开始，查看指定的 Binlog 日志
- `show binlog events in ‘binlog.000030’ from 931 limit 2;`：从指定的位置开始，查看指定的 Binlog 日志，限制查询的条数
- `show binlog events in ‘binlog.000030’ from 931 limit 1, 2;`：从指定的位置开始，带有偏移，查看指定的 Binlog 日志，限制查询的条数

`show binlog events;`查看日志：
```
*************************** 82. row ***************************
   Log_name: mysql-bin.000001
        Pos: 10379
 Event_type: Table_map
  Server_id: 141
End_log_pos: 10446
       Info: table_id: 172 (test.user)
*************************** 83. row ***************************
   Log_name: mysql-bin.000001
        Pos: 10446
 Event_type: Write_rows
  Server_id: 141
End_log_pos: 10506
       Info: table_id: 172 flags: STMT_END_F
```
## 5、写 Binlog 的时机

对支持事务的引擎如InnoDB而言，必须要提交了事务才会记录binlog。binlog 什么时候刷新到磁盘跟参数 sync_binlog 相关：
- 如果设置为0，则表示MySQL不控制binlog的刷新，由文件系统去控制它缓存的刷新；
- 如果设置为不为0的值，则表示每 sync_binlog 次事务，MySQL调用文件系统的刷新操作刷新binlog到磁盘中。
- 设为1是最安全的，在系统故障时最多丢失一个事务的更新，但是会对性能有所影响

如果 `sync_binlog=0` 或 `sync_binlog`大于1，当发生数据库崩溃时，可能有一部分已提交但其binlog未被同步到磁盘的事务会被丢失，恢复程序将无法恢复这部分事务。

在MySQL 5.7.7之前，默认值 `sync_binlog` 是0，MySQL 5.7.7和更高版本使用默认值1，这是最安全的选择。一般情况下会设置为100或者0，牺牲一定的一致性来获取更好的性能；

当遇到以下3种情况时，MySQL会重新生成一个新的日志文件，文件序号递增：
- MySQL服务器停止或重启时
- 使用 `flush logs` 命令；
- 当 binlog 文件大小超过 `max_binlog_size` 变量的值时；

## 6、binlog的Event类型

二进制日志中存储的内容称之为事件，每一个数据库更新操作(Insert、Update、Delete，不包括Select)等都对应一个事件，MySQL Binlog Event 类型有很多种（MySQL 官方定义了 36 种），例如：XID、TABLE_MAP、QUERY 等等。常用的类型：

Event Type|	事件|	重要程度
----------| ----|--------
QUERY_EVENT	|与数据无关的操作， begin、drop table、truncate table 等 |	了解即可
XID_EVENT	| 标记事务提交 |	了解即可
TABLE_MAP_EVENT	| 记录下一个操作所对应的表信息，存储了数据库名和表名|	非常重要
WRITE_ROWS_EVENT|	插入数据，即 insert 操作|	非常重要
UPDATE_ROWS_EVENT|	更新数据，即 update 操作|	非常重要
DELETE_ROWS_EVENT|	删除数据，即 delete 操作|	非常重要

每个 Event 包含 header 和 data 两个部分；header 提供了 Event 的创建时间，哪个服务器等信息，data 部分提供的是针对该 Event 的具体信息，如具体数据的修改。对 Binlog 的解析，即为对 Event 的解析

## 7、mysqlbinlog 命令的使用

```bash
# mysqlbinlog 的执行格式
mysqlbinlog [options] log_file ...

# 查看bin-log二进制文件（shell方式）
mysqlbinlog -v --base64-output=decode-rows /var/lib/mysql/master.000003

# 查看bin-log二进制文件（带查询条件）
mysqlbinlog -v --base64-output=decode-rows /var/lib/mysql/master.000003 \
    --start-datetime="2019-03-01 00:00:00"  \
    --stop-datetime="2019-03-10 00:00:00"   \
    --start-position="5000"    \
    --stop-position="20000"
```

输出日志分析：
```bash
# at 21019
#190308 10:10:09 server id 1  end_log_pos 21094 CRC32 0x7a405abc     Query   thread_id=113   exec_time=0 error_code=0
SET TIMESTAMP=1552011009/*!*/;
BEGIN
/*!*/;
```
上面输出包括信息：
- position: 位于文件中的位置，即第一行的（# at 21019）,说明该事件记录从文件第21019个字节开始
- timestamp: 事件发生的时间戳，即第二行的（#190308 10:10:09）
- server id: 服务器标识（1）
- end_log_pos 表示下一个事件开始的位置（即当前事件的结束位置+1）
- thread_id: 执行该事件的线程id （thread_id=113）
- exec_time: 事件执行的花费时间
- error_code: 错误码，0意味着没有发生错误
- type:事件类型Query

# 二、BinaryLog解析工具

## 1、MaxWell

- [MaxWell 监听binlog](https://github.com/zendesk/maxwell.git)
- [MaxWell使用](https://juejin.im/post/5c8616fc5188251bde6bd43a)

### 1.1、概述

Maxwell是一个能实时读取MySQL二进制日志binlog，并生成 JSON 格式的消息，作为生产者发送给 Kafka，Kinesis、RabbitMQ、Redis、Google Cloud Pub/Sub、文件或其它平台的应用程序

Maxwell主要提供了下列功能：
- 支持 SELECT * FROM table 的方式进行全量数据初始化
- 支持在主库发生failover后，自动恢复binlog位置(GTID)
- 可以对数据进行分区，解决数据倾斜问题，发送到kafka的数据支持database、table、column等级别的数据分区
- 工作方式是伪装为Slave，接收binlog events，然后根据schemas信息拼装，可以接受ddl、xid、row等各种event

MaxWell与Canal：
- canal 由Java开发，分为服务端和客户端，拥有众多的衍生应用，性能稳定，功能强大；canal 需要自己编写客户端来消费canal解析到的数据。
- maxwell相对于canal的优势是使用简单，它直接将数据变更输出为json字符串，不需要再编写客户端；



docker run -ti --rm zendesk/maxwell bin/maxwell --user='root' --password='123456' --host='10.206.0.6' --producer=stdout

docker run -it --rm zendesk/maxwell bin/maxwell --user='maxwell' --password='123456' --host='10.206.0.6' --producer=kafka --kafka.bootstrap.servers='119.45.13.206:9092' --kafka_topic=maxwell --log_level=debug


# 参考资料


- [MySQL BinaryLog](https://dev.mysql.com/doc/internals/en/binary-log-overview.html)