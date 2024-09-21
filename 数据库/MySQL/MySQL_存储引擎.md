# 一、MySQL 存储引擎

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

## 6、表空间

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

# 二、InnoDB存储引擎

- [InnoDB-architecture](https://dev.mysql.com/doc/refman/8.0/en/innodb-architecture.html)

## 1、InnoDB详述

- [InnoDB Startup Options and System Variables](https://dev.mysql.com/doc/refman/5.7/en/innodb-parameters.html)

InnoDB采用[MVCC](../数据库锁机制.md#1MVCC:多版本并发控制)来支持高并发，并且实现了四个标准的隔离级别。其默认级别是`Repeatable read`（可重复读），并且通过间隙锁（next-key locking）策略防止幻读的出现。间隙锁使得InnoDB不仅仅锁定查询涉及的行，还会多索引中的间隙进行锁定，以防止幻影行的插入；

InnoDB是基于聚簇索引建立的。其二级索引（非主键索引）中必须包含主键列，如果主键列很大的话，其他的所有索引都会很大。若表上的索引较多的话，主键应当尽可能的小；

### 1.1、存储方式

InnoDB采取的方式是：将数据划分为若干个页，以页作为磁盘和内存之间交互的基本单位，InnoDB中页的大小一般为 16 KB。在一般情况下，一次最少从磁盘中读取16KB的内容到内存中，一次最少把内存中的16KB内容刷新到磁盘中；

### 1.2、InnoDB最佳实践

- 为表指定一个自增的主键；
- 不要使用`LOCK TABLES`语句，如果需要锁定，可以使用 `SELECT ... FOR UPDATE` 锁定你需要操作的数据行；
- `--sql_mode=NO_ENGINE_SUBSTITUTION`，防止在创建表或者修改表的时候存储引擎不支持

## 2、InnoDB体系架构

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

## 3、Buffer Pool

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

# 三、MySQL事务

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

# 四、数据库锁

[数据库锁机制](../数据库锁机制.md)

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
