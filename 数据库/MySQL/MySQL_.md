# 一、Mysql概述

基于C/S架构

## 1、MySQL服务器参数

* [MySQL Server配置](https://dev.mysql.com/doc/refman/5.6/en/server-system-variable-reference.html)

### 1.1、内存配置相关参数

- 确定可以使用的内存上限；
- 确定MySQL的每个连接使用的内存<br>
	sort_buffer_size ： 排序操作缓冲区<br>
	join_buffer_size ： 连接缓冲区大小<br>
	read_buffer_size ： 读缓冲区大小<br>
	read_rnd_buffer_size ： 索引缓冲区大小<br>
	上述参数是为每个线程分配的；
- 确定需要为操作系统保留多少内存

### 1.2、如何为缓存池分配内存

innodb_buffer_pool_size -> 修改该配置 <br>
总内存 - (每个线程所需要的内存 * 连接处数) - 系统保留内存

mysql内存参数：https://www.cnblogs.com/kissdb/p/4009614.html

### 1.3、I/O相关配置参数

- InnoDB I/O相关配置：
	- innodb_log_file_size：单个事务日志文件的大小
	- innodb_log_files_in_group：事务日志文件的数量
	- innodb_log_buffer_size：事务日志缓冲区
	- innodb_flush_log_at_trx_commit：log写入cache并刷新到缓存
	- innodb_flush_method：刷新方式
	- innodb_file_per_table：设置表空间
	- innodb_doublewrite：是否支持双写缓存

- MyISAM I/O配置：delay_key_write：

### 1.4、安全配置参数

- expire_logs_days：指定自动清理binlog的天数
- max_allowed_packet：控制mysql可以接收的包的大小
- skip_name_resolve：禁用DNS查找
- sysdate_is_now：确保sysdate()返回确定性日期
- read_only：进行非uper权限的用户写权限
- skip_slave_start：禁用slave自动恢复
- sql_mode：设置mysql所使用的sql模式

### 1.5、其他配置参数

- sync_binlog：控制mysql如何向磁盘刷新binlog
- tmp_table_size 和 max_heap_table_size：控制内存临时表大小
- max_connections：控制允许的最大连接数

## 2、MySQL版本

### 2.1、MySQL常见的发行版本

- MySQL官方版本-Oracle官方维护，包含社区版本和企业版本
- Percona MySQL：同官方版本完全兼容，性能优于mysql官方版本
- MariaDB

上述三个版本的主要区别：
- 服务器特性：都是开源，都支持分区表；Mysql是InnoDB引擎；其他是XtraDB引擎，两者是兼容的；
- 高可用特性：基于日志点复制；基于gtid复制；但是MariaDB的gtid同mysql不兼容；
- 安全特性：防火墙、审计、用户密码以及密码加密算法；

### 2.2、版本升级

- 升级之前需要考虑什么：
	- 升级对业务的好处：是否解决业务上某一方面的痛点、是否解决运维上某一个方面的痛点；
	- 升级对业务的影响：对原业务程序的支持是否有影响、对原业务程序的性能是否有影响；
	- 数据库升级的方案：评估受影响的业务系统、升级的详细步骤、升级后的数据库环境检查、升级后的业务检查；
	- 升级失败的回滚方案：升级失败回滚的步骤、回滚后的数据库环境检查、回滚后的业务检查；

- 升级的步骤
	- 对升级的数据库进行备份
	- 升级slave服务器版本；
	- 手动进行主从切换；
	- 升级Master服务器版本；
	- 升级完成后进行业务检查；

### 2.3、MySQL版本特性

- MySQL8.0特性
	- 所有元数据使用InnoDB引擎存储，无frm文件；
	- 系统表采用InnoDB存储并采用独立表空间；
	- 支持定义资源管理组（目前仅支持CPU资源）；
	- 支持不可见索引和降序索引，支持直方图优化；
	- 支持窗口函数；
	- 支持在线修改全局参数持久化；
	- 默认使用caching_sha2_password认证插件；
	- 新增支持定义角色（role）；
	- 新增密码历史记录功能，限制重复使用密码；
	- InnoDB DDL语句支持原子操作；
	- 支持在线修改UNDO表空间；
	- 新增管理试图用于建innodb表状态；
	- 新增innodb_dedicated_server配置项

## 3、MySQL架构

### 3.1、如何设计关系型数据库

- 存储文件系统
- 程序实例
	- 存储管理
	- 缓存机制
	- SQL解析
	- 日志管理
	- 权限划分
	- 容灾机制
	- 索引管理-优化效率
	- 锁管理

### 3.2、MySQL架构

![](image/mysql架构.png)

## 4、MySQL与Oracle区别

- 自动增长的数据类型处理：
	- MYSQL有自动增长的数据类型，插入记录时不用操作此字段，会自动获得数据值；
	- ORACLE没有自动增长的数据类型，需要建立一个自动增长的序列号，插入记录时要把序列号的下一个值赋于此字段。`CREATE SEQUENCE 序列号的名称 (最好是表名+序列号标记) `	`INCREMENT BY 1 START WITH 1 MAXVALUE 99999 CYCLE NOCACHE;`其中最大的值按字段的长度来定, 如果定义的自动增长的序列号 NUMBER(6)，最大值为`999999`；INSERT 语句插入这个字段值为: `序列号的名称.NEXTVAL`；

- 分页的处理
	- MYSQL处理翻页的SQL语句比较简单，用 LIMIT 开始位置, 记录个数；
	- ORACLE处理翻页的SQL语句就比较繁琐了。每个结果集只有一个ROWNUM字段标明它的位置, 并且只能用`ROWNUM<100`, 不能用`ROWNUM>80`

- 单引号的处理：MYSQL里可以用双引号包起字符串，ORACLE里只可以用单引号包起字符串。在插入和修改字符串前必须做单引号的替换：把所有出现的一个单引号替换成两个单引号；

- 长字符串的处理：

- 日期的处理：MYSQL日期字段分DATE和TIME、时间戳等多种，ORACLE日期字段只有DATE，包含年月日时分秒信息，用当前数据库的系统时间为SYSDATE, 精确到秒，或者用字符串转换成日期型函数；

- 空字符的处理：MYSQL的非空字段也有空的内容，ORACLE里定义了非空字段就不容许有空的内容。按MySQL的NOT NULL来定义ORACLE表结构, 导数据的时候会产生错误。因此导数据时要对空字符进行判断，如果为NULL或空字符，需要把它改成一个空格的字符串；

- 字符串的模糊比较： MYSQL里用 `字段名 like '%字符串%'`，ORACLE里也可以用` 字段名 like '%字符串%'` 但这种方法不能使用索引, 速度不快，用字符串比较函数 `instr(字段名,'字符串')>0` 会得到更精确的查找结果；

## 5、MySQL日志

### 5.1、mysql日志分类

mysql日志主要分为以下：
- 错误日志（error_log）：记录mysql在启动、运行或停止时出现的问题；
- 常规日志（general_log）：记录所有发现mysql的请求；
- 慢查询日志（slow_query_log）：记录符合慢查询条件的日志；
- 二进制日志（binary_log）：记录全部有效的数据修改日志；
- 中继日志（raly_log）：用于主从复制，临时存储从主库同步的二进制日志；


# 七、MySQL 存储引擎

## 1、MySQL 的数据库引擎

### 1.1、MyISAM：5.5版本之前默认存储引擎

```
check table tableName  检查表
repair table tableName 修复表
```
- myisam 支持数据压缩：myisampack，压缩后的表示只读的
- 以堆表方式存储，使用表级锁；
- 在5.0版本之前，单表默认大小为4G，如存储大表，需要修改：max_rows和avg_row_length 在5.0之后，默认支持的大小256TB
- 适用场景：
	* 不需要使用事务的场景；
	* 只读类应用，读操作远远大于写操作的场景；
	* 空间类应用(空间函数：GPS数据等)

### 1.2、InnoDB存储引擎

[The InnoDB Storage Engine](https://dev.mysql.com/doc/refman/8.0/en/innodb-storage-engine.html)

5.5之后的版本默认存储引擎，InnoDB使用表空间进行数据存储：innodb_file_per_table，对InnoDB使用独立表空间

- 表转移步骤：把原来存在于系统表空间中的表转移到独立表空间
	- 使用mysqldump导出所有数据库表的数据
	- 停止mysql服务，修改参数，并删除innodB相关文件；
	- 重启mysql服务，重建innodb系统表空间
	- 重新导入数据。

- 存储特性：
	- 事务性存储引擎，完全支持事务的ACID特性；
	- 数据按主键聚集存储；
	- 支持 Redo Log 和 Undo Log
	- 支持行级锁，是在存储引擎实现的，可以最大程度实现并发，以及MVCC
	- 支持全文索引、空间索引；
	- Innodb默认使用的是行锁。而行锁是基于索引的，因此要想加上行锁，在加锁时必须命中索引，否则将使用表锁

- 状态检查：`show engine innodb status`

### 1.3、CSV存储引擎

基于 CSV 格式文件存储数据
- 特性：
	- CSV 存储引擎因为自身文件格式的原因，所有列必须强制指定 NOT NULL；非事务型存储
	- CSV 引擎也不支持索引，不支持分区；
	- CSV 存储引擎也会包含一个存储表结构的 `.frm 文件`、一个 `.csv` 存储数据的文件、一个同名的元信息文件，该文件的扩展名为`.CSM`，用来保存表的状态及表中保存的数据量
	- 每个数据行占用一个文本行，列之间使用逗号分割；

- 适用场景：适合作为数据交换的中间表

### 1.4、Archive

- 特性：
	- 非事务型存储引擎；
	- 以zlib对表数据进行压缩，磁盘I/O更少；
	- 数据存储在`arz`为后缀的文件；
	- 只支持insert和select操作；
	- 只允许在自增ID列上增加索引，最多只能存在一个索引

- 适用场景：
	- 日志和数据采集类应用；
	- 数据归档存储；

### 1.5、Memory

也称为heap存储引擎，所以数据保存在内存中
- 特性
	- 非事务型存储引擎；
	- 所有数据保存在内存中
	- 支持hash 和 btree 索引，默认使用hash索引；如果是范围查询的，会走全表扫描
	- 所有字段都为固定长度 varchar(10) = char(10)
	- 不支持blob 和 text 等大字段
	- 使用表级锁
	- 最大大小由`max_heap_table_size`参数决定，不会对已经存在的表生效。如果需要生效，需重启服务重建数据
	- 内存表的数据部分以数组的方式单独存放，而主键 id 索引里，存的是每个数据的位置。主键 id 是 hash 索引，可以看到索引上的 key 并不是有序的

- 适用场景：
	- 用于查找或者是映射表，例如邮编和地区的对应表
	- 用于保存数据分析中产生的中间表；
	- 用于缓存周期性聚合数据的结果表；memory数据易丢失，所以要求数据可再生

对比innodb：
- InnoDB 引擎把数据放在主键索引上，其他索引上保存的是主键 id。这种方式，我们称之为索引组织表（Index Organizied Table）；
- Memory 引擎采用的是把数据单独存放，索引上保存数据位置的数据组织形式，我们称之为堆组织表（Heap Organizied Table）；
- InnoDB 表的数据总是有序存放的，而内存表的数据就是按照写入顺序存放的；
- 当数据文件有空洞的时候，InnoDB 表在插入新数据的时候，为了保证数据有序性，只能在固定的位置写入新值，而内存表找到空位就可以插入新值；
- 数据位置发生变化的时候，InnoDB 表只需要修改主键索引，而内存表需要修改所有索引；
- InnoDB 表用主键索引查询时需要走一次索引查找，用普通索引查询的时候，需要走两次索引查找。而内存表没有这个区别，所有索引的“地位”都是相同的；
- InnoDB 支持变长数据类型，不同记录的长度可能不同；内存表不支持 Blob 和 Text 字段，并且即使定义了 varchar(N)，实际也当作 char(N)，也就是固定长度字符串来存储，因此内存表的每行数据长度相同；
- 每个数据行被删除以后，空出的这个位置都可以被接下来要插入的数据复用；

不建议在生产环境使用内存表：
- 锁粒度问题：内存表不支持行锁，只支持表锁。因此，一张表只要有更新，就会堵住其他所有在这个表上的读写操作；
- 数据持久化问题：数据放在内存中，是内存表的优势，但也是一个劣势。因为，数据库重启的时候，所有的内存表都会被清空；

	比如在主备场景下：业务正常访问主库；备库硬件升级，备库重启，内存表 t1 内容被清空；备库重启后，客户端发送一条 update 语句，修改表 t1 的数据行，这时备库应用线程就会报错“找不到要更新的行”；这样就会导致主备同步停止，当然，如果这时候发生主备切换的话，客户端会看到，表 t1 的数据“丢失”了

### 1.6、Federated

- 特性：
	- 提供了访问远程mysql服务器上表的方法；
	- 本地不存储数据，数据全部放到远程数据库上；
	- 本地需要保存表结构和远程服务器的连接信息；

默认禁止的，启用需要在配置文件中开启federated；

连接方法：`mysql://user_name[:password]@host_name[:port]/db_name/table_name`

### 1.7、NDB引擎

特点：
- 事务型存储引擎，只支持读已提交隔离级别；
- 数据存储在内存中，会保存在磁盘中；
- 支持行级锁；
- 支持高可用集群；
- 支持 Ttree索引

适用场景：
- 需要数据完全同步的高可用场景

## 2、MyISAM 和 InnoDB 引擎的区别

- 主要区别：
	- MyISAM 是非事务安全型的， InnoDB 是事务安全型的；
	- MyISAM 锁的粒度是表级锁， InnoDB 是支持行级锁的；
	- MyISAM 支持全文本索引，而InnoDB不支持全文索引
	- MyISAM 相对简单，所以在效率上要优于 InnoDB，小型应用可以考虑使用 MyISAM；MyISAM 更小的表空间
	- MyISAM 表是保存成文件的形式，在跨平台的数据转移中使用MyISAM存储会省去不少的麻烦；
	- InnoDB 表比 MyISAM 表更安全，可以在保证数据不丢失的情况下，切换非事务表到事务表；
	- InnoDB 数据和索引是集中存储的`.ibd`；MyISAM数据和索引是分别存储的，`数据.MYD，索引.MYI`；（mysql的物理目录：`/var/lib/mysql/`）
	- MyISAM是非聚集索引，也是使用B+Tree作为索引结构，索引和数据文件是分离的，索引保存的是数据文件的指针。主键索引和辅助索引是独立的。也就是说：InnoDB的B+树主键索引的叶子节点就是数据文件，辅助索引的叶子节点是主键的值；而MyISAM的B+树主键索引和辅助索引的叶子节点都是数据文件的地址指针；
	- InnoDB是聚集索引，数据文件是和（主键）索引绑在一起的，即索引 + 数据 = 整个表数据文件，通过主键索引到整个记录，必须要有主键，通过主键索引效率很高

- 适用场景：
	- MyISAM 管理非事务表，它提供高速存储和检索，以及全文搜索能力，如果应用中需要执行大量的select查询，那么MyISAM是更好的选择
	- InnoDB 用于事务处理应用程序，具有众多特性，包括ACID事务支持。如果应用中需要执行大量的insert或update操作，则应该使用 InnoDB，这样可以提高多用户并发操作的性能

	阿里巴巴大部分 mysql 数据库其实使用的 percona 的原型加以修改

## 3、mysql存储引擎比较

| 特性 | InnoDB | MyISAM | MEMORY | ARCHIVE |
| ---- | ----- | ------- | ------ | ------- |
| 存储限制(Storage limits) | 64TB | No | YES | No |
| 支持事物(Transactions) | Yes | No | No | No |
| 锁机制(Locking granularity) | 行锁 | 表锁 | 表锁 | 行锁 |
| B树索引(B-tree indexes) | Yes | Yes | Yes | No |
| T树索引(T-tree indexes) | No | No | No | No |
| 哈希索引(Hash indexes) | Yes | No | Yes | No |
| 全文索引(Full-text indexes) | Yes | Yes | No | No |
| 集群索引(Clustered indexes) | Yes | No | No | No |
| 数据缓存(Data caches) | Yes | No | N/A | No |
| 索引缓存(Index caches) | Yes | Yes | N/A | No |
| 数据可压缩(Compressed data) | Yes | Yes | No | Yes |
| 加密传输(Encrypted data<sup>[1]</sup>) | Yes | Yes | Yes | Yes |
| 集群数据库支持(Cluster databases support) | No | No | No | No |
| 复制支持(Replication support<sup>[2]</sup>) | Yes | No | No | Yes |
| 外键支持(Foreign key support) | Yes | No | No | No |
| 存储空间消耗(Storage Cost) | 高 | 低 | N/A | 非常低 |
| 内存消耗(Memory Cost) | 高 | 低 | N/A | 低 |
| 数据字典更新(Update statistics for data dictionary) | Yes | Yes | Yes | Yes |
| 备份/时间点恢复(backup/point-in-time recovery<sup>[3]</sup>) | Yes | Yes | Yes | Yes |
| 多版本并发控制(Multi-Version Concurrency Control/MVCC) | Yes | No | No | No |
| 批量数据写入效率(Bulk insert speed) | 慢 | 快 | 快 | 非常快 |
| 地理信息数据类型(Geospatial datatype support) | Yes | Yes | No | Yes |
| 地理信息索引(Geospatial indexing support<sup>[4]</sup>) | Yes | Yes | No | Yes |

## 4、查看数据库引擎

- 查看引擎：
```sql
mysql> show engines；
+--------------------+---------+----------------------------------------------------------------+--------------+------+------------+
| Engine             | Support | Comment                                                        | Transactions | XA   | Savepoints |
+--------------------+---------+----------------------------------------------------------------+--------------+------+------------+
| FEDERATED          | NO      | Federated MySQL storage engine                                 | NULL         | NULL | NULL       |
| MRG_MYISAM         | YES     | Collection of identical MyISAM tables                          | NO           | NO   | NO         |
| MyISAM             | YES     | MyISAM storage engine                                          | NO           | NO   | NO         |
| BLACKHOLE          | YES     | /dev/null storage engine (anything you write to it disappears) | NO           | NO   | NO         |
| CSV                | YES     | CSV storage engine                                             | NO           | NO   | NO         |
| MEMORY             | YES     | Hash based， stored in memory， useful for temporary tables      | NO           | NO   | NO         |
| ARCHIVE            | YES     | Archive storage engine                                         | NO           | NO   | NO         |
| InnoDB             | DEFAULT | Supports transactions， row-level locking， and foreign keys     | YES          | YES  | YES        |
| PERFORMANCE_SCHEMA | YES     | Performance Schema                                             | NO           | NO   | NO         |
+--------------------+---------+----------------------------------------------------------------+--------------+------+------------+
```
或者：`SELECT * FROM INFORMATION_SCHEMA.ENGINES;`

- 查看默认存储引擎

```sql
mysql> show variables like '%storage_engine%';
+------------------------+--------+
| Variable_name          | Value  |
+------------------------+--------+
| default_storage_engine | InnoDB |
| storage_engine         | InnoDB |
+------------------------+--------+
```

- 设置存储引擎

可以在my.cnf配置文件中设置需要的存储引擎，这个参数放在 [mysqld] 这个字段下面的 default_storage_engine 参数值<br>
[mysqld]<br>
default_storage_engine=CSV

## 5、选择合适的存储引擎

几个标准：
- 是否需要支持事务；
- 是否需要使用热备；
- 崩溃恢复，能否接受崩溃；
- 是否需要外键支持；
- 存储的限制；
- 对索引和缓存的支持；

不要混合使用存储引擎

## 6、InnoDB详述

- [InnoDB Startup Options and System Variables](https://dev.mysql.com/doc/refman/5.7/en/innodb-parameters.html)

InnoDB采用[MVCC](../数据库锁机制.md#1MVCC:多版本并发控制)来支持高并发，并且实现了四个标准的隔离级别。其默认级别是`Repeatable read`（可重复读），并且通过间隙锁（next-key locking）策略防止幻读的出现。间隙锁使得InnoDB不仅仅锁定查询涉及的行，还会多索引中的间隙进行锁定，以防止幻影行的插入；

InnoDB是基于聚簇索引建立的。其二级索引（非主键索引）中必须包含主键列，如果主键列很大的话，其他的所有索引都会很大。若表上的索引较多的话，主键应当尽可能的小；

### 6.1、存储方式

InnoDB采取的方式是：将数据划分为若干个页，以页作为磁盘和内存之间交互的基本单位，InnoDB中页的大小一般为 16 KB。在一般情况下，一次最少从磁盘中读取16KB的内容到内存中，一次最少把内存中的16KB内容刷新到磁盘中；

### 6.2、InnoDB最佳实践

- 为表指定一个自增的主键；
- 不要使用`LOCK TABLES`语句，如果需要锁定，可以使用 `SELECT ... FOR UPDATE` 锁定你需要操作的数据行；
- `--sql_mode=NO_ENGINE_SUBSTITUTION`，防止在创建表或者修改表的时候存储引擎不支持

### 6.3、InnoDB体系架构

InnoDB 存储引擎由内存池和一些后台线程组成

**内存池：**

InnoDB 存储引擎是基于磁盘存储的，并将其中的记录按照页的方式进行管理。因此可将其视为基于磁盘的数据库系统（Disk-base Database），在这样的系统中，众所周知，由于 CPU 速度与磁盘速度之间的不匹配，通常会使用缓冲池技术来提高数据库的整体性能；缓冲池其实就是一块内存区域，在 CPU 与磁盘之间加入内存访问，通过内存的速度来弥补磁盘速度较慢对数据库性能的影响

拥有了缓冲池后，“读取页” 操作的具体步骤就是这样的：
- 首先将从磁盘读到的页存放在缓冲池中；
- 下一次再读相同的页时，首先判断该页是否在缓冲池中。若在缓冲池中，称该页在缓冲池中被命中，直接读取该页。否则，读取磁盘上的页

“修改页” 操作的具体步骤就是这样的：
- 首先修改在缓冲池中的页；然后再以一定的频率刷新到磁盘上；

所谓 ”脏页“ 就发生在修改这个操作中，如果缓冲池中的页已经被修改了，但是还没有刷新到磁盘上，那么我们就称缓冲池中的这页是 ”脏页“，即缓冲池中的页的版本要比磁盘的新。

**后台线程：**

后台线程的主要作用就是刷新内存池中的数据，保证内存池中缓存的是最近的数据；此外将已修改的数据文件刷新到磁盘文件，同时保证在数据库发生异常的情况下 InnoDB 能恢复到正常运行状态；

**InnoDB 存储引擎是多线程的模型**，也就是说它拥有多个不同的后台线程，负责处理不同的任务。这里简单列举下几种不同的后台线程：

- **Master Thread**：主要负责将缓冲池中的数据异步刷新到磁盘，保证数据的一致性
- **IO Thread**：在 InnoDB 存储引擎中大量使用了 AIO（Async IO）来处理写 IO 请求，这样可以极大提高数据库的性能。IO Thread 的工作主要是负责这些 IO 请求的回调（call back）处理
- **Purge Thread**：回收已经使用并分配的 undo 页
- **Page Cleaner Thread**：将之前版本中脏页的刷新操作都放入到单独的线程中来完成。其目的是为了减轻原 Master Thread 的工作及对于用户查询线程的阻塞，进一步提高 InnoDB 存储引擎的性能

### 6.4、Buffer Pool

MySQL以数据页为单位，从磁盘中读取数据。数据页被读取到内存中，所谓的内存其实就是Buffer Pool。Buffer Pool中维护的数据结构是缓存页，而且每个缓存页都有它对应的描述信息。InnoDB 访问表数据和索引数据的时候，会顺便把对应的数据页缓存到缓冲池中

Buffer Pool中的默认大小为128MB，数据页大小16KB
```
mysql> select @@innodb_buffer_pool_size/1024/1024;
+-------------------------------------+
| @@innodb_buffer_pool_size/1024/1024 |
+-------------------------------------+
|                        128.00000000 |
+-------------------------------------+
```
Buffer Pool中存在三个双向链表。分别是FreeList、LRUList以及FlushList。这三个双向链表中维护着缓存页的描述信息；

缓冲池LRU：当需要把新页面存储到缓冲池中的时候，将淘汰最近最少使用的页面，并将新页面添加到旧子列表的头部；

相关优化参数：
- `innodb_old_blocks_pct`：控制 LRU 列表中旧子列表的百分比，默认是 37，也就是 3/8，可选范围为 5~95；
- `innodb_old_blocks_time` ：指定第一次访问页面后的时间窗口，该时间窗口内访问页面不会使其移动到 LRU 列表的最前面。默认是 1000，也就是 1 秒。

## 7、表空间

一个InnoDB表及其索引可以在建在系统表空间中，或者是在一个 独立表空间 中，或在 通用表空间
- 当`innodb_file_per_table`启用时，通常是将表存放在独立表空间中，这是默认配置；
- 当`innodb_file_per_table`禁用时，则会在系统表空间中创建表；
- 要在通用表空间中创建表，请使用 `CREATE TABLE … TABLESPACE`语法

表空间涉及的文件：相关文件默认在磁盘中的`innodb_data_home_dir`目录下：
```
|- ibdata1  // 系统表空间文件
|- ibtmp1  // 默认临时表空间文件，可通过innodb_temp_data_file_path属性指定文件位置
|- test/  // 数据库文件夹
    |- db.opt  // test数据库配置文件，包含数据库字符集属性
    |- t.frm  // 数据表元数据文件，不管是使用独立表空间还是系统表空间，每个表都对应有一个
    |- t.ibd  // 数据库表独立表空间文件，如果使用的是独立表空间，则一个表对应一个ibd文件，否则保存在系统表空间文件中
```

# 八、MySQL事务

数据库的事务：是指一组sql语句组成的数据库逻辑处理单元，在这组的sql操作中，要么全部执行成功，要么全部执行失败

MySQL使用MVCC 多版本控制机制、事务隔离机制、锁机制等办法来解决事务并发问题；

## 1、事务特性

### 1.1、ACID特性

- `原子性（Atomic）`：指事务的原子性操作，对数据的修改要么全部执行成功，要么全部失败，实现事务的原子性，是基于日志的Redo/Undo机制；
- `一致性（Consistency）`：数据库在事务执行前后都保持一致性状态。在一致性状态下，所有事务对一个数据的读取结果都是相同的；
- `隔离性（Isolation）`：一个事务所做的修改在最终提交以前，对其它事务是不可见的；
- `持久性（Durability）`：持久性是指一个事务一旦被提交了，那么对数据库中的数据的改变就是永久性的，即便是在数据库系统遇到故障的情况下也不会丢失提交事务的操作。

### 1.2、Redo/Undo机制

Redo/Undo机制是将所有对数据的更新操作都写到日志中；

Redo log用来记录某数据块被修改后的值，可以用来恢复未写入 data file 的已成功事务更新的数据；Undo log是用来记录数据更新前的值，保证数据更新失败能够回滚；

假如数据库在执行的过程中，不小心崩了，可以通过该日志的方式，回滚之前已经执行成功的操作，实现事务的一致性；

具体的实现流程：假如某个时刻数据库崩溃，在崩溃之前有事务A和事务B在执行，事务A已经提交，而事务B还未提交。当数据库重启进行 crash-recovery 时，就会通过Redo log将已经提交事务的更改写到数据文件，而还没有提交的就通过Undo log进行roll back

- redo log 通常是物理日志，记录的是数据页的物理修改，而不是某一行或某几行修改成怎样怎样，它用来恢复提交后的物理数据页（恢复数据页，且只能恢复到最后一次提交的位置）；
- undo log 用来回滚行记录到某个版本。undo log一般是逻辑日志，根据每行记录进行记录；

### 1.3、原子性实现

InnoDB存储引擎提供了两种事务日志：redo log(重做日志)和undo log(回滚日志)。其中redo log用于保证事务持久性；undo log则是事务原子性和隔离性实现的基础；

实现原子性的关键：是当事务回滚时能够撤销所有已经成功执行的sql语句。InnoDB实现回滚，靠的是undo log：当事务对数据库进行修改时，InnoDB会生成对应的undo log；如果事务执行失败或调用了rollback，导致事务需要回滚，便可以利用undo log中的信息将数据回滚到修改之前的样子；

undo log属于逻辑日志，它记录的是sql执行相关的信息。当发生回滚时，InnoDB会根据undo log的内容做与之前相反的工作：对于每个insert，回滚时会执行delete；对于每个delete，回滚时会执行insert；对于每个update，回滚时会执行一个相反的update，把数据改回去；

### 1.4、持久性实现

主要是基于redo log来实现

InnoDB提供了缓存(Buffer Pool)，Buffer Pool中包含了磁盘中部分数据页的映射，作为访问数据库的缓冲：当从数据库读取数据时，会首先从Buffer Pool中读取，如果Buffer Pool中没有，则从磁盘读取后放入Buffer Pool；当向数据库写入数据时，会首先写入Buffer Pool，Buffer Pool中修改的数据会定期刷新到磁盘中（这一过程称为刷脏）；

Buffer Pool的使用大大提高了读写数据的效率，但是也带了新的问题：如果MySQL宕机，而此时Buffer Pool中修改的数据还没有刷新到磁盘，就会导致数据的丢失，事务的持久性无法保证；

`redo log`被引入来解决这个问题：当数据修改时，除了修改Buffer Pool中的数据，还会在redo log记录这次操作；当事务提交时，会调用fsync接口对redo log进行刷盘。如果MySQL宕机，重启时可以读取redo log中的数据，对数据库进行恢复。redo log采用的是WAL（Write-ahead logging，预写式日志），所有修改先写入日志，再更新到Buffer Pool，再由Buffer Pool控制将数据写入磁盘，保证了数据不会因MySQL宕机而丢失，从而满足了持久性要求；可以通过`innodb_flush_log_at_trx_commit`来控制redo log刷磁盘的策略

比直接将Buffer Pool中修改的数据写入磁盘(即刷脏)要快主要原因：
- 采用预写日志（WAL）方式将随机写入变成顺序追加写入，提升事务性能；
- 刷脏是随机IO，因为每次修改的数据位置随机，但写redo log是追加操作，属于顺序IO。
- 刷脏是以数据页（Page）为单位的，MySQL默认页大小是16KB，一个Page上一个小修改都要整页写入；而redo log中只包含真正需要写入的部分，无效IO大大减少

**redo log与binlog：**
- 作用不同：redo log是用于crash recovery的，保证MySQL宕机也不会影响持久性；binlog是用于point-in-time recovery的，保证服务器可以基于时间点恢复数据，此外binlog还用于主从复制；
- 层次不同：redo log是InnoDB存储引擎实现的，而binlog是MySQL的服务器层实现的，同时支持InnoDB和其他存储引擎；
- 内容不同：redo log是物理日志，内容基于磁盘的Page；binlog的内容是二进制的，根据`binlog_format`参数的不同，可能基于sql语句、基于数据本身或者二者的混合；
- 写入时机不同：binlog在事务提交时写入；redo log的写入时机相对多元；

### 1.5、隔离性实现

隔离性实现主要基于以下两点：
- (一个事务)写操作对(另一个事务)写操作的影响：锁机制保证隔离性
- (一个事务)写操作对(另一个事务)读操作的影响：MVCC保证隔离性

**锁机制：**

锁机制的基本原理可以概括为：事务在修改数据之前，需要先获得相应的锁；获得锁之后，事务便可以修改数据；该事务操作期间，这部分数据是锁定的，其他事务如果需要修改数据，需要等待当前事务提交或回滚后释放锁；

查看锁信息：
```sql
select * from information_schema.innodb_locks; #锁的概况
show engine innodb status; #InnoDB整体状态，其中包括锁的情况
```
InnoDB实现的RR，通过锁机制、数据的隐藏列、undo log和类next-key lock，实现了一定程度的隔离性；

### 1.6、一致性实现

实现一致性的措施包括：
- 保证原子性、持久性和隔离性，如果这些特性无法保证，事务的一致性也无法保证；
- 数据库本身提供保障，例如不允许向整形列插入字符串值、字符串长度不能超过列的限制等；
- 应用层面进行保障，例如如果转账操作只扣除转账者的余额，而没有增加接收者的余额，无论数据库实现的多么完美，也无法保证状态的一致

## 2、数据库隔离级别

### 2.1、不考虑隔离性发生的问题

- **脏写**：是指一个事务修改且已经提交的数据被另外一个事务给回滚了；事务 B 已经写入的记录被事务 A 给回滚了；主要通过锁来解决；
- **脏读**：A事务读取B事务尚未提交的数据，此时如果B事务由于某些原因执行了回滚操作，那么A事务读取到的数据就是脏数据；
- **不可重复读**：指在一个事务执行的过程中多次查询某一数据的时候结果不一致的现象，由于在执行的过程中被另一个事务修改了这个数据并提交了事务。

	A事务读取一个数据，B事务对这个数据进行了修改；如果A事务再次读取这个数据，此时读取的结果和第一次读取的结果不同；

	在对于数据库中的某个数据，一个事务范围内多次查询却返回了不同的数据值，这是由于在查询间隔，被另一个事务修改并提交了；脏读是某一事务读取了另一个事务未提交的脏数据，而不可重复读则是读取了前一事务提交的数据；不可重复读重点在于update和delete

- **幻读**：A 读取某个范围的数据，B 在这个范围内插入新的数据，A 再次读取这个范围的数据，此时读取的结果和和第一次读取的结果不同；幻读的重点在于insert。

	比如：第一个事务查询一个User表id=100发现不存在该数据行，这时第二个事务又进来了，新增了一条id=100的数据行并且提交了事务；这时第一个事务新增一条id=100的数据行会报主键冲突，第一个事务再select一下，发现id=100数据行已经存在，这就是幻读

**不可重复读的和幻读很容易混淆，不可重复读侧重于修改，幻读侧重于新增或删除。解决不可重复读的问题只需锁住满足条件的行，解决幻读需要锁表**

### 2.2、事务隔离级别

事务的隔离级别有4个，由低到高依次，级别越高执行效率越低
- READ_UNCOMMITTED（未授权读取、读未提交）：一个事务还没提交时，它做的变更就能被别的事务看到。该隔离级别可以通过“排他写锁”实现。最低级别，任何情况都无法保证；存在脏读，不可重复读，幻读的问题

- READ_COMMITTED（授权读取、读提交）：一个事务提交之后，它做的变更才会被其他事务看到；针对当前读，RC隔离级别保证对读取到的记录加锁 (记录锁)，存在幻读现象

- REPEATABLE_READ（可重复读取）：保证在同一个事务中多次读取同样数据的结果是一样的。可避免脏读、不可重复读的发生读取数据的事务将会禁止写事务(但允许读事务)，写事务则禁止任何其他事务。避免了不可重复读取和脏读，但是有时可能出现幻读。针对当前读，RR隔离级别保证对读取到的记录加锁 (记录锁)，同时保证对读取的范围加锁，新的满足查询条件的记录不能够插入 (间隙锁)，不存在幻读现象，主要是通过多版本并发控制（MVCC）、Next-key Lock等技术解决了幻读问题；事务启动时的视图可以认为是静态的，不受其他事务更新的影响。

- SERIALIZABLE（序列化）：提供严格的事务隔离。强制事务串行执行。事务只能一个接着一个地执行，但不能并发执行。从MVCC并发控制退化为基于锁的并发控制。不区别快照读与当前读，所有的读操作均为当前读，读加读锁 (S锁)，写加写锁 (X锁)。Serializable隔离级别下，读写冲突，因此并发度急剧下降

**隔离级别越高，越能保证数据的完整性和一致性，但是对并发性能的影响也越大。对于多数应用程序，可以优先考虑把数据库系统的隔离级别设为 Read Committed。它能够避免脏读取，而且具有较好的并发性能。可以通过悲观锁和乐观锁来控制不可重复读，幻读等并发问题。**

隔离级别 |	脏读（Dirty Read）|	不可重复读（NonRepeatable Read）|	幻读（Phantom Read）
-------|-------------------|------------------------------|-----------------
未提交读（Read uncommitted）|	可能|	可能	|可能
已提交读（Read committed）|	不可能|	可能|	可能
可重复读（Repeatable read）|	不可能|	不可能|	可能
可串行化（Serializable ）|	不可能|	不可能|	不可能

### 2.3、默认隔离级别

大多数数据库的默认级别就是 READ_COMMITTED ，比如 Sql Server、Oracle，MySQL 的默认隔离级别就是 `REPEATABLE_READ`
- 查看MySQL事务隔离级别：`select @@transaction_isolation;` 或者 `show variables like 'transaction_isolation'`；
- 设置事务隔离级别：`set  [global | session]  transaction isolation level 隔离级别名称`;
- 设置事务隔离级别：`set transaction_isolation='隔离级别名称;'`

### 2.4、隔离级别性能比较

`READ_UNCOMMITTED -> READ_COMMITTED  -> REPEATABLE_READ -> SERIALIZABLE` 隔离的效果是逐渐增强，但是性能却是越来越差。

这个跟MySQL的锁有关，在Mysql中的锁可以分为：共享锁/读锁（Shared Locks）、排他锁/写锁（Exclusive Locks）、间隙锁（Gap Lock）、行锁（Record Locks）、表锁

因为隔离级别的实现肯定是跟锁相关的，READ_UNCOMMITTED 是未加锁的，所以对于它来说也就是没有隔离的效果，所以它的性能也是最好的；

对于读提交和可重复读，他们俩的实现是兼顾解决数据问题，然后又要有一定的并发行，所以在实现上锁机制会比串行化优化很多，提高并发性，所以性能也会比较好，底层实现采用的是MVCC（多版本并发控制）方式进行实现；

对于串行化加的是一把大锁，读的时候加共享锁，不能写，写的时候，家的是排它锁，阻塞其它事务的写入和读取，若是其它的事务长时间不能写入就会直接报超时，所以它的性能也是最差的，对于它来就没有什么并发性可言；

**MySQL加锁时机：**

在数据库的`增、删、改、查`中，只有`增、删、改`才会加上排它锁，而只是查询并不会加锁，只能通过在`select`语句后显式加`lock in share mode`或者`for update`来加共享锁或者排它锁；

### 2.4、RC、RR级别下InnoDB的非阻塞读

[MySQL当前读与快照读](../数据库锁机制.md#1.2MySQL当前读与快照读)

### 2.5、RR可重复读如何避免幻读

MySQL InnoDB的可重复读并不保证避免幻读，需要应用使用加锁读来保证。而这个加锁度使用到的机制就是next-key locks；Innodb 的 RR 隔离级别对范围会加上 GAP，理论上不会存在幻读。

事务隔离级别为可重复读时，如果检索条件有索引（包括主键索引）的时候，默认加锁方式是next-key 锁；如果检索条件没有索引，更新数据时会锁住整张表。一个间隙被事务加了锁，其他事务是不能在这个间隙插入记录的，这样可以防止幻读

多版本并发控制机制解决了幻读问题

## 3、事务启动方式

- `begin;`：一致性读的视图不会马上创建，而是在执行begin后面的第一个操作innodb表的SQL语句时生成。这个SQL可以是select,update,delete,insert。事务ID也是此时被分配的
- `start transaction;`：和begin的功能效果一样；
- `start transaction with consistent snapshot;`：该语句执行后，会马上创建一致性读的视图。这个是它和begin的区别。事务ID也是此时被分配的

`commit work and chain`：提交上一个事务，并且再开启一个新的事务。它的功能等效于：commit + begin

# 九、数据库锁

[数据库锁机制](../数据库锁机制.md)

# 十、数据库结构设计

## 1、数据库结构设计的步骤

- 需求分析：全面了解产品设计的存储需求，存储需求，数据处理需求，数据的安全性与完整性
- 逻辑设计：设计数据的逻辑存储结构：数据实体之间的逻辑关系，解决数据冗余和数据维护异常
- 物理设计：根据所使用的数据库特点进行表结构设计；

## 2、数据库设计范式

数据库三范式

### 2.1、数据库设计第一范式

- 数据库表中的所有字段都只具有单一属性；
- 单一属性的列基本由基本数据类型所构成；
- 设计出来的表都是简单的二维表；

### 2.2、数据库设计第二范式

要求数据库表中只有一个业务主键，符合第二范式的表不能存在非主键列只对部分主键的依赖关系

### 2.3、数据库设计第三范式

指每一个业务主键既不部分依赖于也不传递依赖于业务主键，也就是在第二范式的基础上消除了非主属性对主键的传递依赖

## 3、反范式化设计

所谓的反范式化是为了性能和读取效率的考虑而适当的对数据库设计范式的要求进行违反，而允许存在的少量的数据冗余，即反范式化就是使用空间来换取时间

## 4、表设计原则

- 注释：字段和表的注释；
- 表数据量：
	- 单表 >= 800W，可考虑分库分表；
	- 如果没有分库分表的计划，可将数据归档到其他地方；
- 表字段原则：
	- 不保存大字段；
	- 单表字段不宜过多，不建议超过30个；
- 平衡冗余和范式设计

## 5、索引原则

- 索引个数：普通表不建议超过10个，写入频繁的表不建议超过5个；
- 单个索引包含的字段不建议超过5个；
- 组合索引：满足最左前缀原则，将区分度大的字段放在前面；
- 不要在索引列进行计算；
- join的字段创建索引，且拥有相同的类型和字符集，避免隐式转换；


# 十一、MySQL系统库

- [information-schema-introduction](https://dev.mysql.com/doc/refman/8.0/en/information-schema-introduction.html)

## 1、information_schema

information_schema 数据库中保存了MySQL服务器所有数据库的信息。如数据库名，数据库的表，表栏的数据类型与访问权限等。简而言之，这台MySQL服务器上，到底有哪些数据库、各个数据库有哪些表，每张表的字段类型是什么，各个数据库要什么权限才能访问，等等信息都保存在 information_schema 对应的表里面；

information_schema 数据库中的数据是只读的，不能对其进行增删改操作，且其实际上都是视图，不是基表，数据库中没有文件与其关联

主要的表：
```
mysql> show tables;
+---------------------------------------+
| Tables_in_information_schema          |
+---------------------------------------+
| CHARACTER_SETS                        |
| COLLATIONS                            |
| COLLATION_CHARACTER_SET_APPLICABILITY |
| COLUMNS                               |
| COLUMN_PRIVILEGES                     |
| ENGINES                               |
| EVENTS                                |
| FILES                                 |
| GLOBAL_STATUS                         |
| GLOBAL_VARIABLES                      |
| KEY_COLUMN_USAGE                      |
| OPTIMIZER_TRACE                       |
| PARAMETERS                            |
| PARTITIONS                            |
| PLUGINS                               |
| PROCESSLIST                           |
| PROFILING                             |
| REFERENTIAL_CONSTRAINTS               |
| ROUTINES                              |
| SCHEMATA                              |
| SCHEMA_PRIVILEGES                     |
| SESSION_STATUS                        |
| SESSION_VARIABLES                     |
| STATISTICS                            |
| TABLES                                |
| TABLESPACES                           |
| TABLE_CONSTRAINTS                     |
| TABLE_PRIVILEGES                      |
| TRIGGERS                              |
| USER_PRIVILEGES                       |
| VIEWS                                 |
| INNODB_LOCKS                          |
| INNODB_TRX                            |
| INNODB_SYS_DATAFILES                  |
| INNODB_FT_CONFIG                      |
| INNODB_SYS_VIRTUAL                    |
| INNODB_CMP                            |
| INNODB_FT_BEING_DELETED               |
| INNODB_CMP_RESET                      |
| INNODB_CMP_PER_INDEX                  |
| INNODB_CMPMEM_RESET                   |
| INNODB_FT_DELETED                     |
| INNODB_BUFFER_PAGE_LRU                |
| INNODB_LOCK_WAITS                     |
| INNODB_TEMP_TABLE_INFO                |
| INNODB_SYS_INDEXES                    |
| INNODB_SYS_TABLES                     |
| INNODB_SYS_FIELDS                     |
| INNODB_CMP_PER_INDEX_RESET            |
| INNODB_BUFFER_PAGE                    |
| INNODB_FT_DEFAULT_STOPWORD            |
| INNODB_FT_INDEX_TABLE                 |
| INNODB_FT_INDEX_CACHE                 |
| INNODB_SYS_TABLESPACES                |
| INNODB_METRICS                        |
| INNODB_SYS_FOREIGN_COLS               |
| INNODB_CMPMEM                         |
| INNODB_BUFFER_POOL_STATS              |
| INNODB_SYS_COLUMNS                    |
| INNODB_SYS_FOREIGN                    |
| INNODB_SYS_TABLESTATS                 |
+---------------------------------------+
61 rows in set (0.00 sec)
```

### 1.1、数据库元信息：SCHEMATA

主要存储的是MySQL中所有数据库的元信息，比如数据库名称、编码等；

表结构：
```
+----------------------------+--------------+------+-----+---------+-------+
| Field                      | Type         | Null | Key | Default | Extra |
+----------------------------+--------------+------+-----+---------+-------+
| CATALOG_NAME               | varchar(512) | NO   |     |         |       |
| SCHEMA_NAME                | varchar(64)  | NO   |     |         |       |
| DEFAULT_CHARACTER_SET_NAME | varchar(32)  | NO   |     |         |       |
| DEFAULT_COLLATION_NAME     | varchar(32)  | NO   |     |         |       |
| SQL_PATH                   | varchar(512) | YES  |     | NULL    |       |
+----------------------------+--------------+------+-----+---------+-------+
```
主要数据
```
mysql> select * from schemata;
+--------------+--------------------+----------------------------+------------------------+----------+
| CATALOG_NAME | SCHEMA_NAME        | DEFAULT_CHARACTER_SET_NAME | DEFAULT_COLLATION_NAME | SQL_PATH |
+--------------+--------------------+----------------------------+------------------------+----------+
| def          | information_schema | utf8                       | utf8_general_ci        | NULL     |
| def          | imooc_ad_data      | utf8                       | utf8_general_ci        | NULL     |
| def          | mysql              | latin1                     | latin1_swedish_ci      | NULL     |
| def          | performance_schema | utf8                       | utf8_general_ci        | NULL     |
| def          | sakila             | latin1                     | latin1_swedish_ci      | NULL     |
| def          | sys                | utf8                       | utf8_general_ci        | NULL     |
| def          | test               | utf8mb4                    | utf8mb4_general_ci     | NULL     |
+--------------+--------------------+----------------------------+------------------------+----------+
```

### 1.2、表信息：TABLES

主要是存储的是MySQL所有数据库表的元数据信息，比如：存储引擎、表的行数，还有自增序列的值

表结构：
```
mysql> desc tables;
+-----------------+---------------------+------+-----+---------+-------+
| Field           | Type                | Null | Key | Default | Extra |
+-----------------+---------------------+------+-----+---------+-------+
| TABLE_CATALOG   | varchar(512)        | NO   |     |         |       |
| TABLE_SCHEMA    | varchar(64)         | NO   |     |         | 数据库名称 |
| TABLE_NAME      | varchar(64)         | NO   |     |         |表名    |
| TABLE_TYPE      | varchar(64)         | NO   |     |         |       |
| ENGINE          | varchar(64)         | YES  |     | NULL    |存储引擎 |
| VERSION         | bigint(21) unsigned | YES  |     | NULL    |       |
| ROW_FORMAT      | varchar(10)         | YES  |     | NULL    |       |
| TABLE_ROWS      | bigint(21) unsigned | YES  |     | NULL    |       |
| AVG_ROW_LENGTH  | bigint(21) unsigned | YES  |     | NULL    |       |
| DATA_LENGTH     | bigint(21) unsigned | YES  |     | NULL    |       |
| MAX_DATA_LENGTH | bigint(21) unsigned | YES  |     | NULL    |       |
| INDEX_LENGTH    | bigint(21) unsigned | YES  |     | NULL    |       |
| DATA_FREE       | bigint(21) unsigned | YES  |     | NULL    |       |
| AUTO_INCREMENT  | bigint(21) unsigned | YES  |     | NULL    |       |
| CREATE_TIME     | datetime            | YES  |     | NULL    |       |
| UPDATE_TIME     | datetime            | YES  |     | NULL    |       |
| CHECK_TIME      | datetime            | YES  |     | NULL    |       |
| TABLE_COLLATION | varchar(32)         | YES  |     | NULL    |       |
| CHECKSUM        | bigint(21) unsigned | YES  |     | NULL    |       |
| CREATE_OPTIONS  | varchar(255)        | YES  |     | NULL    |       |
| TABLE_COMMENT   | varchar(2048)       | NO   |     |         |       |
+-----------------+---------------------+------+-----+---------+-------+
```

### 1.3、列信息：COLUMNS

主要存储的是MySQL中所有表的列信息，主要是数据库名称、表名、列名称、列所处的位置、编码等信息，这个位置有在解析binlog时可以根据binlog去映射对应的列名称；

```
mysql> desc columns;
+--------------------------+---------------------+------+-----+---------+-------+
| Field                    | Type                | Null | Key | Default | Extra |
+--------------------------+---------------------+------+-----+---------+-------+
| TABLE_CATALOG            | varchar(512)        | NO   |     |         |       |
| TABLE_SCHEMA             | varchar(64)         | NO   |     |         |       |
| TABLE_NAME               | varchar(64)         | NO   |     |         |       |
| COLUMN_NAME              | varchar(64)         | NO   |     |         |       |
| ORDINAL_POSITION         | bigint(21) unsigned | NO   |     | 0       |       |
| COLUMN_DEFAULT           | longtext            | YES  |     | NULL    |       |
| IS_NULLABLE              | varchar(3)          | NO   |     |         |       |
| DATA_TYPE                | varchar(64)         | NO   |     |         |       |
| CHARACTER_MAXIMUM_LENGTH | bigint(21) unsigned | YES  |     | NULL    |       |
| CHARACTER_OCTET_LENGTH   | bigint(21) unsigned | YES  |     | NULL    |       |
| NUMERIC_PRECISION        | bigint(21) unsigned | YES  |     | NULL    |       |
| NUMERIC_SCALE            | bigint(21) unsigned | YES  |     | NULL    |       |
| DATETIME_PRECISION       | bigint(21) unsigned | YES  |     | NULL    |       |
| CHARACTER_SET_NAME       | varchar(32)         | YES  |     | NULL    |       |
| COLLATION_NAME           | varchar(32)         | YES  |     | NULL    |       |
| COLUMN_TYPE              | longtext            | NO   |     | NULL    |       |
| COLUMN_KEY               | varchar(3)          | NO   |     |         |       |
| EXTRA                    | varchar(30)         | NO   |     |         |       |
| PRIVILEGES               | varchar(80)         | NO   |     |         |       |
| COLUMN_COMMENT           | varchar(1024)       | NO   |     |         |       |
| GENERATION_EXPRESSION    | longtext            | NO   |     | NULL    |       |
+--------------------------+---------------------+------+-----+---------+-------+
```

比如查询test库中user表的列信息：
```
mysql> select table_schema,table_name, column_name,ordinal_position from columns where table_schema ='test' and table_name='user';
+--------------+------------+-------------+------------------+
| table_schema | table_name | column_name | ordinal_position |
+--------------+------------+-------------+------------------+
| test         | user       | id          |                1 |
| test         | user       | username    |                2 |
| test         | user       | password    |                3 |
+--------------+------------+-------------+------------------+
```

### 1.4、分区信息：PARTITIONS

主要是存储的表的分区信息，在该表中的每一行对应于一个分区表的单个分区或子分区
```
mysql> desc partitions;
+-------------------------------+---------------------+------+-----+---------+-------+
| Field                         | Type                | Null | Key | Default | Extra |
+-------------------------------+---------------------+------+-----+---------+-------+
| TABLE_CATALOG                 | varchar(512)        | NO   |     |         |       |
| TABLE_SCHEMA                  | varchar(64)         | NO   |     |         |       |
| TABLE_NAME                    | varchar(64)         | NO   |     |         |       |
| PARTITION_NAME                | varchar(64)         | YES  |     | NULL    |       |
| SUBPARTITION_NAME             | varchar(64)         | YES  |     | NULL    |       |
| PARTITION_ORDINAL_POSITION    | bigint(21) unsigned | YES  |     | NULL    |       |
| SUBPARTITION_ORDINAL_POSITION | bigint(21) unsigned | YES  |     | NULL    |       |
| PARTITION_METHOD              | varchar(18)         | YES  |     | NULL    |       |
| SUBPARTITION_METHOD           | varchar(12)         | YES  |     | NULL    |       |
| PARTITION_EXPRESSION          | longtext            | YES  |     | NULL    |       |
| SUBPARTITION_EXPRESSION       | longtext            | YES  |     | NULL    |       |
| PARTITION_DESCRIPTION         | longtext            | YES  |     | NULL    |       |
| TABLE_ROWS                    | bigint(21) unsigned | NO   |     | 0       |       |
| AVG_ROW_LENGTH                | bigint(21) unsigned | NO   |     | 0       |       |
| DATA_LENGTH                   | bigint(21) unsigned | NO   |     | 0       |       |
| MAX_DATA_LENGTH               | bigint(21) unsigned | YES  |     | NULL    |       |
| INDEX_LENGTH                  | bigint(21) unsigned | NO   |     | 0       |       |
| DATA_FREE                     | bigint(21) unsigned | NO   |     | 0       |       |
| CREATE_TIME                   | datetime            | YES  |     | NULL    |       |
| UPDATE_TIME                   | datetime            | YES  |     | NULL    |       |
| CHECK_TIME                    | datetime            | YES  |     | NULL    |       |
| CHECKSUM                      | bigint(21) unsigned | YES  |     | NULL    |       |
| PARTITION_COMMENT             | varchar(80)         | NO   |     |         |       |
| NODEGROUP                     | varchar(12)         | NO   |     |         |       |
| TABLESPACE_NAME               | varchar(64)         | YES  |     | NULL    |       |
+-------------------------------+---------------------+------+-----+---------+-------+
```

# 十三、其他

## 1、关于时区

- [Timezone引起的MySQL服务器高负载](https://plantegg.github.io/2023/10/03/time_zone是怎么打爆你的MySQL的/)

### 1.1、数据库连接中时间问题

数据库连接时的基本参数：
```
jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimeZone=UTC
```
serverTimeZone的作用就是指定web服务器和mysql服务器的会话期间的mysql服务器时区，一般配置 serverTimeZone 跟MySQL服务器时间保持一致即可；
- 如果MySQL服务器的时间为：UTC，则这里也需要指定 `serverTimeZone=UTC`；
- 如果MySQL服务器时间为：UTC+8，则这里可以指定为：`serverTimeZone=Asia/Shanghai`

### 1.2、时间零值异常

如果发现如下异常：`com.mysql.cj.exceptions.DataReadException: Zero date value prohibited`，使用MyBatis从MySQL表中查询DATETIME类型的列数据，如果列值是0000-00-00 00:00:00，程序将抛出异常Java.sql.SQLException。

异常 "com.mysql.cj.exceptions.DataReadException：当试图从MySQL数据库中读取一个已经被设置为 "零 "值的日期值时，会抛出 "零日期值被禁止 "的异常，这个日期值是'0000-00-00'。

在MySQL中，日期的 "零 "值不是一个有效的日期，因此，不能用于日期计算或操作。当试图从数据库中读取日期值时，MySQL会抛出这个异常，以表明日期值是无效的，不能被使用。

如何解决：
- 确保存储在数据库中的所有日期值都是有效的日期，并且没有'零'值；
- 将现有的'零'日期值更新为有效日期值；
- 修改你的应用程序代码来处理这个异常，并向用户显示一个有意义的错误信息
- 将零值日期设置为null：`jdbc:mysql://127.0.0.1/test?zeroDateTimeBehavior=convertToNull`
- 将零日期将被转换为0001-01-01 00:00:00.0，相当于一年：`jdbc:mysql://127.0.0.1/test?zeroDateTimeBehavior=round`

## 2、关于字符集

- [Case Sensitivity in String Searches](https://dev.mysql.com/doc/refman/8.0/en/case-sensitivity.html)
- [Character Sets, Collations, Unicode](https://dev.mysql.com/doc/refman/8.0/en/charset.html)

常用的是utf8字符集：
- utf8_bin：utf8_bin将字符串中的每一个字符用二进制数据存储，区分大小写。
- utf8_general_ci：utf8_genera_ci不区分大小写，ci为case insensitive的缩写，即大小写不敏感。

# 参考文章

* [MySQL官方中文文档](https://www.docs4dev.com/docs/mysql/5.7/)
* [洞悉 MySQL 底层架构：游走在缓冲与磁盘之](https://xie.infoq.cn/article/ed531f74ecfd44eacb1a98258)
* [数据库分库分表思路](http://www.cnblogs.com/butterfly100/p/9034281.html)
* [MySQL加锁分析](https://mp.weixin.qq.com/s/lmKnhBM78ZgEF70kuQiTLg)
* [sakila文件地址](http://dev.mysql.com/doc/sakila/en/sakila-installation.html)
* [Mysql数据库主从](http://blog.51cto.com/wangwei007/965575)
* [面试关于MySQL事务实现](https://juejin.im/post/5ede6436518825430c3acaf4)
* [事务实现原理](https://www.cnblogs.com/kismetv/p/10331633.html)
* [MySQL-GTID复制](https://dbaplus.cn/news-11-857-1.html)
* [MySQL生成随机数据工具](https://github.com/Percona-Lab/mysql_random_data_load)
* [数据库优化](https://mp.weixin.qq.com/s/4us0c3My6H7Yikgg6rPHnA)
- [Innodb_Ruby-InnoDB结构分析工具](https://github.com/jeremycole/innodb_ruby)
