<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、Mysql基本：基于C/S架构](#%E4%B8%80mysql%E5%9F%BA%E6%9C%AC%E5%9F%BA%E4%BA%8Ecs%E6%9E%B6%E6%9E%84)
  - [1、MySQL服务器参数](#1mysql%E6%9C%8D%E5%8A%A1%E5%99%A8%E5%8F%82%E6%95%B0)
    - [1.1、内存配置相关参数](#11%E5%86%85%E5%AD%98%E9%85%8D%E7%BD%AE%E7%9B%B8%E5%85%B3%E5%8F%82%E6%95%B0)
    - [1.2、如何为缓存池分配内存](#12%E5%A6%82%E4%BD%95%E4%B8%BA%E7%BC%93%E5%AD%98%E6%B1%A0%E5%88%86%E9%85%8D%E5%86%85%E5%AD%98)
    - [1.3、I/O相关配置参数](#13io%E7%9B%B8%E5%85%B3%E9%85%8D%E7%BD%AE%E5%8F%82%E6%95%B0)
    - [1.4、安全配置参数](#14%E5%AE%89%E5%85%A8%E9%85%8D%E7%BD%AE%E5%8F%82%E6%95%B0)
    - [1.5、其他配置参数](#15%E5%85%B6%E4%BB%96%E9%85%8D%E7%BD%AE%E5%8F%82%E6%95%B0)
- [二、MySQL操作](#%E4%BA%8Cmysql%E6%93%8D%E4%BD%9C)
  - [1、连接命令行](#1%E8%BF%9E%E6%8E%A5%E5%91%BD%E4%BB%A4%E8%A1%8C)
  - [2、SQL操作](#2sql%E6%93%8D%E4%BD%9C)
  - [3、创建数据库](#3%E5%88%9B%E5%BB%BA%E6%95%B0%E6%8D%AE%E5%BA%93)
  - [5、表的定义](#5%E8%A1%A8%E7%9A%84%E5%AE%9A%E4%B9%89)
  - [6、数据的操作（DML）](#6%E6%95%B0%E6%8D%AE%E7%9A%84%E6%93%8D%E4%BD%9Cdml)
  - [7、校对规则](#7%E6%A0%A1%E5%AF%B9%E8%A7%84%E5%88%99)
- [三、MySQL数据类型](#%E4%B8%89mysql%E6%95%B0%E6%8D%AE%E7%B1%BB%E5%9E%8B)
  - [1、数值型](#1%E6%95%B0%E5%80%BC%E5%9E%8B)
  - [2、日期类型](#2%E6%97%A5%E6%9C%9F%E7%B1%BB%E5%9E%8B)
  - [3、字符串类型-M表示允许的字符串长度](#3%E5%AD%97%E7%AC%A6%E4%B8%B2%E7%B1%BB%E5%9E%8B-m%E8%A1%A8%E7%A4%BA%E5%85%81%E8%AE%B8%E7%9A%84%E5%AD%97%E7%AC%A6%E4%B8%B2%E9%95%BF%E5%BA%A6)
  - [4、列类型的选择](#4%E5%88%97%E7%B1%BB%E5%9E%8B%E7%9A%84%E9%80%89%E6%8B%A9)
- [四、列属性](#%E5%9B%9B%E5%88%97%E5%B1%9E%E6%80%A7)
- [五、查询 SQL 执行顺序](#%E4%BA%94%E6%9F%A5%E8%AF%A2-sql-%E6%89%A7%E8%A1%8C%E9%A1%BA%E5%BA%8F)
  - [1、一般SQL的写的顺序](#1%E4%B8%80%E8%88%ACsql%E7%9A%84%E5%86%99%E7%9A%84%E9%A1%BA%E5%BA%8F)
  - [2\数据执行的顺序：前面括号的数据表示执行顺序](#2%5C%E6%95%B0%E6%8D%AE%E6%89%A7%E8%A1%8C%E7%9A%84%E9%A1%BA%E5%BA%8F%E5%89%8D%E9%9D%A2%E6%8B%AC%E5%8F%B7%E7%9A%84%E6%95%B0%E6%8D%AE%E8%A1%A8%E7%A4%BA%E6%89%A7%E8%A1%8C%E9%A1%BA%E5%BA%8F)
  - [3、SQL性能下降的原因](#3sql%E6%80%A7%E8%83%BD%E4%B8%8B%E9%99%8D%E7%9A%84%E5%8E%9F%E5%9B%A0)
- [六、高级查询](#%E5%85%AD%E9%AB%98%E7%BA%A7%E6%9F%A5%E8%AF%A2)
  - [1、连接](#1%E8%BF%9E%E6%8E%A5)
- [七、MySQL 存储引擎](#%E4%B8%83mysql-%E5%AD%98%E5%82%A8%E5%BC%95%E6%93%8E)
  - [1、MySQL 的数据库引擎](#1mysql-%E7%9A%84%E6%95%B0%E6%8D%AE%E5%BA%93%E5%BC%95%E6%93%8E)
    - [1.1、MyISAM：5.5版本之前默认存储引擎](#11myisam55%E7%89%88%E6%9C%AC%E4%B9%8B%E5%89%8D%E9%BB%98%E8%AE%A4%E5%AD%98%E5%82%A8%E5%BC%95%E6%93%8E)
    - [1.2、InnoDB：5.5之后的版本默认存储引擎](#12innodb55%E4%B9%8B%E5%90%8E%E7%9A%84%E7%89%88%E6%9C%AC%E9%BB%98%E8%AE%A4%E5%AD%98%E5%82%A8%E5%BC%95%E6%93%8E)
    - [1.3、CSV存储引擎是基于 CSV 格式文件存储数据](#13csv%E5%AD%98%E5%82%A8%E5%BC%95%E6%93%8E%E6%98%AF%E5%9F%BA%E4%BA%8E-csv-%E6%A0%BC%E5%BC%8F%E6%96%87%E4%BB%B6%E5%AD%98%E5%82%A8%E6%95%B0%E6%8D%AE)
    - [1.4、Archive](#14archive)
    - [1.5、Memory：也称为heap存储引擎，所以数据保存在内存中](#15memory%E4%B9%9F%E7%A7%B0%E4%B8%BAheap%E5%AD%98%E5%82%A8%E5%BC%95%E6%93%8E%E6%89%80%E4%BB%A5%E6%95%B0%E6%8D%AE%E4%BF%9D%E5%AD%98%E5%9C%A8%E5%86%85%E5%AD%98%E4%B8%AD)
    - [1.6、Federated](#16federated)
  - [2、MyISAM 和 InnoDB 引擎的区别](#2myisam-%E5%92%8C-innodb-%E5%BC%95%E6%93%8E%E7%9A%84%E5%8C%BA%E5%88%AB)
  - [3、mysql存储引擎比较](#3mysql%E5%AD%98%E5%82%A8%E5%BC%95%E6%93%8E%E6%AF%94%E8%BE%83)
  - [4、查看数据库引擎](#4%E6%9F%A5%E7%9C%8B%E6%95%B0%E6%8D%AE%E5%BA%93%E5%BC%95%E6%93%8E)
  - [5、选择合适的存储引擎](#5%E9%80%89%E6%8B%A9%E5%90%88%E9%80%82%E7%9A%84%E5%AD%98%E5%82%A8%E5%BC%95%E6%93%8E)
- [八、高级特性](#%E5%85%AB%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7)
  - [1、数据库隔离级别介绍、举例说明](#1%E6%95%B0%E6%8D%AE%E5%BA%93%E9%9A%94%E7%A6%BB%E7%BA%A7%E5%88%AB%E4%BB%8B%E7%BB%8D%E4%B8%BE%E4%BE%8B%E8%AF%B4%E6%98%8E)
- [九、数据库锁](#%E4%B9%9D%E6%95%B0%E6%8D%AE%E5%BA%93%E9%94%81)
  - [1、锁](#1%E9%94%81)
  - [2、锁的分类](#2%E9%94%81%E7%9A%84%E5%88%86%E7%B1%BB)
  - [3、表锁（偏读）](#3%E8%A1%A8%E9%94%81%E5%81%8F%E8%AF%BB)
  - [4、行锁](#4%E8%A1%8C%E9%94%81)
  - [5、死锁](#5%E6%AD%BB%E9%94%81)
  - [6、乐观锁与悲观锁：(数据库)](#6%E4%B9%90%E8%A7%82%E9%94%81%E4%B8%8E%E6%82%B2%E8%A7%82%E9%94%81%E6%95%B0%E6%8D%AE%E5%BA%93)
- [十、表分区](#%E5%8D%81%E8%A1%A8%E5%88%86%E5%8C%BA)
  - [1、表分区](#1%E8%A1%A8%E5%88%86%E5%8C%BA)
  - [2、与分表的区别](#2%E4%B8%8E%E5%88%86%E8%A1%A8%E7%9A%84%E5%8C%BA%E5%88%AB)
  - [3、表分区的优点](#3%E8%A1%A8%E5%88%86%E5%8C%BA%E7%9A%84%E4%BC%98%E7%82%B9)
  - [4、表分区的限制因素](#4%E8%A1%A8%E5%88%86%E5%8C%BA%E7%9A%84%E9%99%90%E5%88%B6%E5%9B%A0%E7%B4%A0)
  - [5、查看分区：判断 MySQL 是否支持表分区](#5%E6%9F%A5%E7%9C%8B%E5%88%86%E5%8C%BA%E5%88%A4%E6%96%AD-mysql-%E6%98%AF%E5%90%A6%E6%94%AF%E6%8C%81%E8%A1%A8%E5%88%86%E5%8C%BA)
  - [6、MySQL 支持的分区类型](#6mysql-%E6%94%AF%E6%8C%81%E7%9A%84%E5%88%86%E5%8C%BA%E7%B1%BB%E5%9E%8B)
  - [7、RANGE分区](#7range%E5%88%86%E5%8C%BA)
  - [8、LIST分区](#8list%E5%88%86%E5%8C%BA)
  - [9、Columns分区](#9columns%E5%88%86%E5%8C%BA)
  - [10、HASH分区](#10hash%E5%88%86%E5%8C%BA)
  - [11、KEY分区](#11key%E5%88%86%E5%8C%BA)
  - [12、分区对于NULL值的处理](#12%E5%88%86%E5%8C%BA%E5%AF%B9%E4%BA%8Enull%E5%80%BC%E7%9A%84%E5%A4%84%E7%90%86)
  - [13、分区管理](#13%E5%88%86%E5%8C%BA%E7%AE%A1%E7%90%86)
  - [14、分区查询](#14%E5%88%86%E5%8C%BA%E6%9F%A5%E8%AF%A2)
- [十一、数据库结构设计](#%E5%8D%81%E4%B8%80%E6%95%B0%E6%8D%AE%E5%BA%93%E7%BB%93%E6%9E%84%E8%AE%BE%E8%AE%A1)
  - [1、数据库结构设计的步骤](#1%E6%95%B0%E6%8D%AE%E5%BA%93%E7%BB%93%E6%9E%84%E8%AE%BE%E8%AE%A1%E7%9A%84%E6%AD%A5%E9%AA%A4)
  - [2、数据库设计范式](#2%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AE%BE%E8%AE%A1%E8%8C%83%E5%BC%8F)
    - [2.1、数据库设计第一范式](#21%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AE%BE%E8%AE%A1%E7%AC%AC%E4%B8%80%E8%8C%83%E5%BC%8F)
    - [2.2、数据库设计第二范式](#22%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AE%BE%E8%AE%A1%E7%AC%AC%E4%BA%8C%E8%8C%83%E5%BC%8F)
    - [2.3、数据库设计第三范式](#23%E6%95%B0%E6%8D%AE%E5%BA%93%E8%AE%BE%E8%AE%A1%E7%AC%AC%E4%B8%89%E8%8C%83%E5%BC%8F)
  - [3、反范式化设计](#3%E5%8F%8D%E8%8C%83%E5%BC%8F%E5%8C%96%E8%AE%BE%E8%AE%A1)
- [十二、mysql高可用架构设计](#%E5%8D%81%E4%BA%8Cmysql%E9%AB%98%E5%8F%AF%E7%94%A8%E6%9E%B6%E6%9E%84%E8%AE%BE%E8%AE%A1)
  - [1、mysql复制功能](#1mysql%E5%A4%8D%E5%88%B6%E5%8A%9F%E8%83%BD)
  - [2、Mysql日志](#2mysql%E6%97%A5%E5%BF%97)
  - [3、mysql二进制日志](#3mysql%E4%BA%8C%E8%BF%9B%E5%88%B6%E6%97%A5%E5%BF%97)
    - [3.1、mysql二进制日志格式](#31mysql%E4%BA%8C%E8%BF%9B%E5%88%B6%E6%97%A5%E5%BF%97%E6%A0%BC%E5%BC%8F)
- [十三、数据库分库分表](#%E5%8D%81%E4%B8%89%E6%95%B0%E6%8D%AE%E5%BA%93%E5%88%86%E5%BA%93%E5%88%86%E8%A1%A8)
  - [1、数据切分](#1%E6%95%B0%E6%8D%AE%E5%88%87%E5%88%86)
  - [2、垂直切分](#2%E5%9E%82%E7%9B%B4%E5%88%87%E5%88%86)
  - [3、水平切分](#3%E6%B0%B4%E5%B9%B3%E5%88%87%E5%88%86)
  - [4、切分策略](#4%E5%88%87%E5%88%86%E7%AD%96%E7%95%A5)
  - [5、数据切分带来的问题](#5%E6%95%B0%E6%8D%AE%E5%88%87%E5%88%86%E5%B8%A6%E6%9D%A5%E7%9A%84%E9%97%AE%E9%A2%98)
    - [5.1、数据库切分后事务问题](#51%E6%95%B0%E6%8D%AE%E5%BA%93%E5%88%87%E5%88%86%E5%90%8E%E4%BA%8B%E5%8A%A1%E9%97%AE%E9%A2%98)
    - [5.2、跨节点Join的问题：只要是进行切分，跨节点Join的问题是不可避免的](#52%E8%B7%A8%E8%8A%82%E7%82%B9join%E7%9A%84%E9%97%AE%E9%A2%98%E5%8F%AA%E8%A6%81%E6%98%AF%E8%BF%9B%E8%A1%8C%E5%88%87%E5%88%86%E8%B7%A8%E8%8A%82%E7%82%B9join%E7%9A%84%E9%97%AE%E9%A2%98%E6%98%AF%E4%B8%8D%E5%8F%AF%E9%81%BF%E5%85%8D%E7%9A%84)
    - [5.3、跨节点的 count，order by，group by 以及聚合函数问题](#53%E8%B7%A8%E8%8A%82%E7%82%B9%E7%9A%84-countorder-bygroup-by-%E4%BB%A5%E5%8F%8A%E8%81%9A%E5%90%88%E5%87%BD%E6%95%B0%E9%97%AE%E9%A2%98)
    - [5.4、切分后主键问题](#54%E5%88%87%E5%88%86%E5%90%8E%E4%B8%BB%E9%94%AE%E9%97%AE%E9%A2%98)
    - [5.5、数据库迁移、扩容问题](#55%E6%95%B0%E6%8D%AE%E5%BA%93%E8%BF%81%E7%A7%BB%E6%89%A9%E5%AE%B9%E9%97%AE%E9%A2%98)
  - [6、什么时候考虑数据切分](#6%E4%BB%80%E4%B9%88%E6%97%B6%E5%80%99%E8%80%83%E8%99%91%E6%95%B0%E6%8D%AE%E5%88%87%E5%88%86)
- [参考文章](#%E5%8F%82%E8%80%83%E6%96%87%E7%AB%A0)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 一、Mysql基本：基于C/S架构

## 1、MySQL服务器参数

### 1.1、内存配置相关参数

- 确定可以使用的内存上限；
- 确定MySQL的每个连接使用的内存<br>
	sort_buffer_size ： 排查操作缓冲区<br>
	join_buffer_size ： 连接缓冲区大小<br>
	read_buffer_size ： 读缓冲区大小<br>
	read_rnd_buffer_size ： 索引缓冲区大小<br>
	上述参数是为每个线程分配的；
- 确定需要为操作系统保留多少内存

### 1.2、如何为缓存池分配内存

innodb_ buffer_pool_size -> 修改该配置 <br>
总内存 - (每个线程所需要的内存 * 连接处数) - 系统保留内存

### 1.3、I/O相关配置参数

- InnoDB I/O相关配置：

	- innodb_log_file_size：单个事务日志文件的大小
	- innodb_log_files_in_group：事务日志文件的数量
	- innodb_log_buffer_size：事务日志缓冲区
	- innodb_flush_log_at_trx_commit：log写入cache并刷新到缓存
	- innodb_flush_method：刷新方式
	- innodb_file_per_table：设置表空间
	- innodb_doublewrite：是否支持双写缓存

- myisam I/O配置：delay_key_write：

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
- PerCona MySQL：同官方版本完全兼容，性能优于mysql官方版本
- MariaDB

上述三个版本的主要区别：
- 服务器特性：都是开源，都支持分区表；MYsql是InnoDB引擎；其他是XtraDB引擎，两者是兼容的；
- 高可用特性：基于日志点复制；基于gtid复制；但是MariaDB的gtig同mysql不兼容；
- 安全特性：防火墙、审计、用户密码以及密码加密算法

### 2.2、版本升级

- 升级之前需要考虑什么：
	- 升级对业务的好处：是否解决业务上某一方面的痛点、是否解决运维上某一个方面的痛点；
	- 升级对业务的影响：对原业务程序的支持是否有影响、对原业务程序的性能是否有影响；
	- 数据库升级的方案：评估受影响的业务系统、升级的详细步骤、升级后的数据库环境检查、升级后的业务检查
	- 升级失败的回滚方案：升级失败回滚的步骤、回滚后的数据库环境检查、回滚后的业务检查

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
	- ORACLE没有自动增长的数据类型，需要建立一个自动增长的序列号，插入记录时要把序列号的下一个值赋于此字段。CREATE SEQUENCE 序列号的名称 (最好是表名+序列号标记) 	`INCREMENT BY 1 START WITH 1 MAXVALUE 99999 CYCLE NOCACHE;`其中最大的值按字段的长度来定, 如果定义的自动增长的序列号 NUMBER(6)，最大值为999999；INSERT 语句插入这个字段值为: `序列号的名称.NEXTVAL`；

- 分页的处理
	- MYSQL处理翻页的SQL语句比较简单，用 LIMIT 开始位置, 记录个数；
	- ORACLE处理翻页的SQL语句就比较繁琐了。每个结果集只有一个ROWNUM字段标明它的位置, 并且只能用ROWNUM<100, 不能用ROWNUM>80

- 单引号的处理：MYSQL里可以用双引号包起字符串，ORACLE里只可以用单引号包起字符串。在插入和修改字符串前必须做单引号的替换：把所有出现的一个单引号替换成两个单引号；

- 长字符串的处理：

- 日期的处理： MYSQL日期字段分DATE和TIME、时间戳等多种，ORACLE日期字段只有DATE，包含年月日时分秒信息，用当前数据库的系统时间为SYSDATE, 精确到秒，或者用字符串转换成日期型函数；

- 空字符的处理： MYSQL的非空字段也有空的内容，ORACLE里定义了非空字段就不容许有空的内容。按MYSQL的NOT NULL来定义ORACLE表结构, 导数据的时候会产生错误。因此导数据时要对空字符进行判断，如果为NULL或空字符，需要把它改成一个空格的字符串；

- 字符串的模糊比较： MYSQL里用 字段名 like '%字符串%',ORACLE里也可以用 字段名 like '%字符串%' 但这种方法不能使用索引, 速度不快，用字符串比较函数 instr(字段名,'字符串')>0 会得到更精确的查找结果。

-

# 二、MySQL操作

## 1、连接命令行
```
mysql -hlocalhost -P3306 -uroot -p	--(-h：主机host， -P：端口，-u用户，-p：密码)； 
--select user()；查看当前用户
```
## 2、SQL操作

## 3、创建数据库

create database 数据库名 [数据库选项]

- 数据库名称规则：对于一些特点的组合，如纯数字、特殊符号等，包括MySQL的关键字，应该使用标识限定符来包裹，限定符使用反引号；
- 数据库选项：存在数据库文件的.opt文件中 <br>
	`default-character-set=utf8`<br>
	`default-collation=utf8_general_ci`<br>

## 4、数据库的查询

	show databases

- 查看数据库(表)的创建语句：show create database db_name
- 数据库删除：drop database db_name；
- 修改数据库，修改数据库的属性：alter database db_name [修改指令] --一般就是修改数据库的属性修改数据库名：备份旧数据库的数据，创建数据库，将备份的数据移动到新数据库；alter database charset set utf8

## 5、表的定义

- 查看表show table [like pattern]，如：show table like 'exam_%'；模糊查询like通用适用于database
- 查看表的结构：show create table 表名 \G---是结构进行结构化；
- drop table if exists 表名/数据库名
- 表名的修改：rename table 旧表名 to 新表名；---支持同时修改多个表的操作；支持跨数据库表的重命名：rename table 当前数据库.表名 to 目标数据库.表名
- 修改列：alter table table_name add(增加)|drop(删除)|change(重命名列名)|modify(修改)<br>
	alter table 表名 add(列名 列的数据类型，...)；<br>
	alter table 表名 drop 列名；<br>
	alter table 表名 modify(列名 列的数据类型，...)；<br>
	alter table 表名 change 列名 新列名 数据类型；<br>
	alter table 表名 change character set utf8；---设置字符集

## 6、数据的操作（DML）

### 6.1、insert

### 6.2、update

### 6.3、delete

#### 6.3.1、删除操作

`delete from table_name [where]`

`truncate table_name`

#### 6.3.2、delete、truncate、drop的区别

- delete和truncate操作只删除表中数据，而不删除表结构；
- delete删除时对于auto_increment类型的字段，值不会从1开始，truncate可以实现删除数据后，auto_increment类型的字段值从1开始；
- delete属于DML，这个操作会放到rollback segement中，事务提交之后才生效；
- truncate和drop属于DDL，操作立即生效，原数据不放到rollback segment中，不能回滚，操作不触发trigger；
- delete语句不影响表所占用的extent，高水线(high watermark)保持原位置不动；drop语句将表所占用的空间全部释放。truncate 语句缺省情况下见空间释放到 minextents个 extent,除非使用reuse storage; truncate会将高水线复位(回到最开始)；
- 执行速度：`drop> truncate > delete`；
- 使用建议：
	- drop：完全删除表；
	- truncate：想保留表而将所有数据删除。如果和事务无关；
	- delete：如果和事务有关，或者想触发；删除部分数据；可以返回被删除的记录数；

#### 6.3.3、Mysql高水位问题

- 产生原因：数据库中有些表使用delete删除了一些行后，发现空间并未释放；
	- delete 不会释放文件高水位 
	- truncate会释放 ，实际是把.ibd文件删掉了，再建一个。
	- delete + alter engine=innodb会释放， 相当于重建表。

- 解决方案：
	- 执行 OPTIMIZE TABLE 表名：只对MyISAM, BDB和InnoDB表起作用，而且会锁表！
	- 写一SQL，创建新表，删除旧表，新表重命名：生产环境中不停机情况下，数据比较难处理；
	- 考虑用表分区，过期的表分区直接删除，不存在高水位问题；


## 7、校对规则

show variables like 'character_set_%';	查看当前数据库的校对规则

| Variable_name            | Value                                                   |
|--------------------------|---------------------------------------------------------|
| character_set_client     | utf8mb4                                                 |
| character_set_connection | utf8mb4                                                 |
| character_set_database   | utf8 （当前数据库的默认编码）                             |
| character_set_filesystem | binary                                                  |
| character_set_results    | utf8mb4                                                 |
| character_set_server     | utf8（服务器的编码）                                     |
| character_set_system     | utf8                                                    |
| character_sets_dir       | C:\Program Files\MySQL\MySQL Server 5.6\share\charsets\ |


设置变量：set 变量=值，变量可以是上述的变量

设置字符集：set name gbk

# 三、MySQL数据类型

## 1、数值型

**1.1、整型：**

- tinyint：1个字节，-128~127(有符号)，0~255(无符号)；
- smallint：2个字节； -32468~32767
- mediumint：3个字节；
- int：4个字节；
- bigint：8位；

*注意：是否有符号*

可在定义时，使用unsigned标识，没有符号；不写就默认有符号；

*定义显示宽度：*

规定数据的显示宽度：类型(M)--M表示最小显示宽度；需要使用前导0达到填充目的，称为zerofill；A：不影响数的范围；B：宽度大的不影响，不会截取；

bool型：0表示false，1表示true；	tinyint(1)

**1.2、小数类型：都支持控制数值的范围；**

type(M，D)--M表示的所有位数(不包括小数点和符号)，D表示允许的小数位数；整数位数只能是M-D位

- float：单精度，4个字节，默认有效位数为6位左右；				
- double：双精度，8个字节，默认有效数字16位左右；
- decimal：定点数，decimal(M，D)，M表示总位数，D表示小数位数(范围存在)，M默认是10，D默认是0；	

*浮点数支持科学计数法：0.23E3 == 0.23 * 10^3；*

*小数也支持zerofill和unsigned*

## 2、日期类型

**2.1、年月日时分秒，datetime，显示格式：YYYY-MM-DD HH：MM：SS；8个字节，与时区无关**

- 存储范围：1000.1.1 00：00：00~9999.12.31 23：59：59，
- 支持任意分隔符的日期，所表示的时间符合范围；但是如果出现歧义，所有不建议使用特殊分隔符；
- 支持两位年份，不建议使用<br>
	70~69		1970-2069<br>
	70~99		19**<br>
	0~69		20**<br>

**2.2、年月日，date，跟datetime差不多；3个字节**

使用date类型可以利用日期时间函数进行日期之间的计算.

**2.3、时间戳，timestamp，存储时是整型，表示时是日期时间，格式YYYY-MM-DD HH：MM：SS，4个字节**

存储范围：1970.1.1 00：00：00~2038.1.19 03：14：07<br>
检索列时，+0可以检索到时间戳<br>
支持0值：表示当前是没有规定的，如2013-04-0表示4月整个月；<br>
依赖于所指定的时区；<br>
在行的数据修改时可以自动修改timestamp列的值

**2.4、time，3个字节，范围：-838：59：59 ~838：59：59**

表示一天中的时间或时间间隔，在表示时间间隔时可以使用天来表示，格式：D HH：MM：SS

**2.5、year，1个字节，1901~2155**

**2.6、如何选择日期类型**

- 不要使用字符类型来存储日期时间数据；
	* 日期时间类型通常比字符串占用的字符串存储空间小；
	* 日期类型在进行查找过滤时可以利用日期来进行对比；
	* 日期时间类型有着丰富的处理函数，可以方便对日期类型进行日期计算
- 使用int存储日期时间不如使用 timestamp 类型

## 3、字符串类型-M表示允许的字符串长度

- char[M]，最大长度255个字节，固定长度，M严格限定字符长度；只能存储2000个字符；字符串存储在char类型的列中会删除末尾的空格
- varchar[M]，可变长，最大65535个字节，M表示允许的最大字符长度；自能存储4000个字符
- text：有多种类型，2^16个字节；
- 其他字符串类型 enum：枚举选项量；set：集合元素，如：create table s_1(gender enum('female'，'male'))；

	真实的varchar长度：总长度65535；<br>
	varchar的特点：当类型数据超过255个字符时，采用2个字节表示长度；<br>
	65533；整条记录需要一个额外的字节用于保存当前字段的null值，除非所有字段不为null，才可以省略该字节，无论一条记录有多个字段存在null，都使用统一的字节来表示，而不是每个字段一个字节；<br>
	列：create table s_4(a varchar(65533))character set latin1---error<br>
		create table s_4(a varchar(65533) not null)character set latin1--right

- varchar长度选择问题：使用最小的符合需求的长度
- varchar适用场景
	- 字符串列的最大长度比平均长度大很多
	- 字符串列很少被更新
	- 使用了多字节字符集存储字符串

## 4、列类型的选择

当一个列可以选择多种数据类型时，应该优先考虑数字类型，其次是日期或二进制类型，最后是字符类型

- 应该使用最精确的类型，占用的空间少
- 考虑应用程序语言的处理；
- 考虑移植兼容性；

# 四、列属性

**1、是否为空：规定一个字段的值是否可以为null，设置字段值不为空not null；**

*注意：字段属性最好不用 null：*

- 所有使用 null 值的情况，都可以通过一个有意义的值的表示，这样有利于代码的可读性和可维护性，并能从约束上增强业务数据的规范性.
- null 值到非 null 值的更新无法做到原地更新，容易发生索引分裂从而影响性能； 但 NULL列改为NOT NULL 来的性能提示很小，除非确定它带来了问题，否则不要把它当成优先的优化措施，最重要的是使用的列的类型的适当性
- NULL 值在 timestamp 类型下容易出问题，特别是没有启用参数 explicit_defaults_for_timestamp；
- NOT IN， != 等负向条件查询在有 NULL 值的情况下返回永远为空结果，查询容易出错：
	- NOT IN 子查询在有 NULL 值的情况下返回永远为空结果，查询容易出错；
	- 单列索引不存 null 值，复合索引不存全为null的值，如果列允许为null，可能会得到"不符合预期"的结果集，如果name允许为null，索引不存储null值，结果集中不会包含这些记录.所以，请使用 not null 约束以及默认值；
	- 如果在两个字段进行拼接：比如题号+分数，首先要各字段进行非 null 判断，否则只要任意一个字段为空都会造成拼接的结果为 null；
	- 如果有 Null column 存在的情况下，count(Null column)需要格外注意， null 值不会参与统计
	- 注意 Null 字段的判断方式， column = null 将会得到错误的结果；
- Null 列需要更多的存储空间：需要一个额外字节作为判断是否为 NULL 的标志位

**2、默认值属性：default value，只有在没有给字段设值的时才会使用默认值；常跟not null搭配；**

**3、主键约束：primary key  ，可以唯一标识某条记录的字段或者是字段的集合；主键是跟业务逻辑无关的属性；**

设置主键：primary key( 列名) <br>
联合主键设置：primary key(列名1，列名2，...)；

**4、自动增长：auto_increment，为每条记录提供一个唯一标识**

	列名 primary key auto_increment

# 五、查询 SQL 执行顺序

## 1、一般SQL的写的顺序

```sql
SELECT
DISTINCT <select_list>
FROM <left_table> <join_type> JOIN <right_table>
ON <join_condition>
WHERE <where_condition>
GROUP BY <group_by_list>
HAVING <having_condition>
ORDER BY <order_by_condition>
LIMIT <limit_number>
```
## 2、数据执行的顺序：前面括号的数据表示执行顺序

![image](image/SQL执行顺序.jpg)
```
(7)     SELECT
(8)     DISTINCT <select_list>
(1)     FROM <left_table>
(3)     <join_type> JOIN <right_table>
(2)     ON <join_condition>
(4)     WHERE <where_condition>
(5)     GROUP BY <group_by_list>
(6)     HAVING <having_condition>
(9)     ORDER BY <order_by_condition>
(10)    LIMIT <limit_number>
```
- FROM：对FROM子句中的前两个表执行笛卡尔积(Cartesian product)(交叉联接)， 生成虚拟表VT1
- ON：对VT1应用ON筛选器.只有那些使`<join_condition>`为真的行才被插入VT2.
- OUTER(JOIN)：如果指定了OUTER JOIN(相对于CROSS JOIN 或(INNER JOIN)，保留表(preserved table：左外部联接把左表标记为保留表， 右外部联接把右表标记为保留表， 完全外部联接把两个表都标记为保留表)中未找到匹配的行将作为外部行添加到 VT2，生成VT3.如果FROM子句包含两个以上的表， 则对上一个联接生成的结果表和下一个表重复执行步骤1到步骤3， 直到处理完所有的表为止
- WHERE：对VT3应用WHERE筛选器.只有使`<where_condition>`为true的行才被插入VT4.
- GROUP BY：按GROUP BY子句中的列列表对VT4中的行分组， 生成VT5.
- CUBE|ROLLUP：把超组(Suppergroups)插入VT5，生成VT6.
- HAVING：对VT6应用HAVING筛选器.只有使`<having_condition>`为true的组才会被插入VT7.
- SELECT：处理SELECT列表， 产生VT8.
- DISTINCT：将重复的行从VT8中移除， 产生VT9.
- ORDER BY：将VT9中的行按ORDER BY 子句中的列列表排序， 生成游标(VC10).
- TOP：从VC10的开始处选择指定数量或比例的行， 生成表VT11，并返回调用者；

*除非你确定要有序行，否则不要指定ORDER BY 子句*

## 3、SQL性能下降的原因

# 六、高级查询

![image](image/SQL-Joins-1.jpg)
![image](image/SQL-Joins-2.jpg)

## 1、连接

场景：假设两张表：emp， dept. emp表中的deptId为dept表中的主键。MySQL 不支持 full join

### 1.1、内连接
```sql
select * from emp inner join dept on emp.deptId=dept.id；
```
查询两张表中共有的数据。等同于：
```sql
select * from emp， dept where emp.deptId=dept.id
```

### 1.2、左外连接
```sql
select * from emp a left join dept b on a.deptId=b.id
```
查询emp独有的数据和查询emp与dept共有的数据

### 1.3、左连接
```sql
select * from emp a left join dept b on a.deptId=b.id where b.id is null；
```
查询emp独有的数据

### 1.4、右外连接
```sql
select * from emp a right join dept b on a.deptId=b.id；
```
查询dept独有的数据和查询emp与dept共有的数据

### 1.5、右外连接
```sql
select * from emp a right join dept b on a.deptId=b.id where a.id is null；
```
查询dept独有的数据

### 1.6、全连接
```sql
select * from emp a left join dept b on a.deptId=b.id
union
select * from emp a right join dept b on a.deptId=b.id；
```
查询所有emp和dept独有和共有的数据

### 1.7、全连接（去除共有数据）
```sql
select * from emp a left join dept b on a.deptId=b.id where b.id is null
union
select * from emp a right join dept b on a.deptId=b.id where a.id is null；
```
去除两张表的共有数据，查询emp和dept分别独有的数据

### 1.8、union 和 union all：联合查询

- **1.8.1、union：**

用于合并两个或多个 SELECT 语句的结果集，并消去表中任何重复行. union 内部的 SELECT 语句必须拥有相同数量的列，列也必须拥有相似的数据类型，每条 SELECT 语句中的列的顺序必须相同

基本语法：
```sql
select column_name from table1
union
select column_name from table2
```

- **1.8.2、union all：**

用途同 union all， 但是不消除重复行.
```sql
SELECT column_name FROM table1
UNION ALL
SELECT column_name FROM table2
```

- **1.8.3、union 使用注意事项：**

如果子句中有 order by 或 limit， 需要用括号括起来，推荐放到所有子句之后，即对最终合并的结果来排序或筛选在子句中，order by 需要配合limit使用才有意义.如果不配合limit使用，会被语法分析器优化分析时去除
```sql
select * from emp a left join dept b on a.deptId=b.id order by id desc
union
select * from emp a right join dept b on a.deptId=b.id order by id desc
```
==> 报错：1221 - Incorrect usage of UNION and ORDER BY

## 2、连接使用注意事项

- 关于 `A LEFT JOIN B ON 条件表达式` 的一点提醒：ON 条件（“A LEFT JOIN B ON 条件表达式”中的ON）用来决定如何从 B 表中检索数据行。如果 B 表中没有任何一行数据匹配 ON 的条件,将会额外生成一行所有列为 NULL 的数据

- 对于 `A LEFT JOIN B ON 条件表达式`中，如果on后面的条件有关于A表的过滤条件，其是不生效的；类似如下的SQL
	```sql
	select user.*, score.score from user left join score on user.id = score.userId and user.status = 1
	```
	使用user.status对user表进行过滤是不生效的，只能加在where语句中

## 3、group by...having

## 4、行转列与列转行

# 七、MySQL 存储引擎

## 1、MySQL 的数据库引擎

### 1.1、MyISAM：5.5版本之前默认存储引擎

check table tableName  检查表<br>
repair table tableName 修复表

- myisam 支持数据压缩：myisam pack，压缩后的表示只读的
- 在5.0版本之前，单表默认大小为4G，如存储大表，需要修改：max_rows和avg_row_length 在5.0之后，默认支持的大小256TB
- 适用场景：
	* 非事务型应用
	* 只读类应用
	* 空间类应用(空间函数：GPS数据等)

### 1.2、InnoDB：5.5之后的版本默认存储引擎

InnoDB使用表空间进行数据存储：innodb_file_per_table，对InnoDB使用独立表空间
- **1.2.1、表转移步骤：把原来存在于系统表空间中的表转移到独立表空间**

	- 使用mysqldump导出所有数据库表的数据
	- 停止mysql服务，修改参数，并删除innodB相关文件；
	- 重启mysql服务，重建innodb系统表空间
	- 重新导入数据.

- **1.2.2.存储特性：**

	- 事务性存储引擎
	- 完全支持事务的ACID特性
	- Redo Log 和Undo Log
	- 支持行级锁，是在存储引擎实现的，可以最大程度实现并发；
	- 支持全文索引，空间函数；
	- Innodb默认使用的是行锁。而行锁是基于索引的，因此要想加上行锁，在加锁时必须命中索引，否则将使用表锁

- **1.2.3、状态检查**

show engine innodb status

### 1.3、CSV存储引擎是基于 CSV 格式文件存储数据

- **1.3.1.特性：**
	- CSV 存储引擎因为自身文件格式的原因，所有列必须强制指定 NOT NULL；
	- CSV 引擎也不支持索引，不支持分区；
	- CSV 存储引擎也会包含一个存储表结构的 .frm 文件、一个 .csv 存储数据的文件、一个同名的元信息文件，该文件的扩展名为.CSM，用来保存表的状态及表中保存的数据量
	- 每个数据行占用一个文本行

- **1.3.2.适合作为数据交换的中间表**

### 1.4、Archive

- **1.4.1.特性：**

	- 以zlib对表数据进行压缩，磁盘I/O更少；
	- 数据存储在arz为后缀的文件；
	- 只支持insert和select操作；
	- 只允许在自增ID列上增加索引；

- **1.4.2.使用场景：**

日志和数据采集类应用

### 1.5、Memory：也称为heap存储引擎，所以数据保存在内存中

- **1.5.1.特性：**
	- 支持hash 和btree索引
	- 所有字段都为固定长度 varchar(10) = char(10)
	- 不支持blob 和 text 等大字段
	- 使用表级锁
	- 最大大小由max_heap_table_size参数决定，不会对已经存在的表生效。如果需要生效，需重启服务重建数据

- **1.5.2.使用场景：**

	- 用于查找或者是映射表，例如邮编和地区的对应表
	- 用于保存数据分析中产生的中间表；
	- 用于缓存周期性聚合数据的结果表；memory数据易丢失，所以要求数据可再生

### 1.6、Federated

- **1.6.1.特性：**

	- 提供了访问远程mysql服务器上表的方法；
	- 本地不存储数据，数据全部放到远程数据库上；
	- 本地需要保存表结构和远程服务器的连接信息；

默认禁止的，启用需要在配置文件中开启federated；

连接方法：`mysql：//user_name[:password]@host_name[:port]/db_name/table_name`

## 2、MyISAM 和 InnoDB 引擎的区别

- **1.2、主要区别：**

	- MyISAM 是非事务安全型的， InnoDB 是事务安全型的；
	- MyISAM 锁的粒度是表级锁， InnoDB 是支持行级锁的；
	- MyISAM 支持全文本索引，而InnoDB不支持全文索引
	- MyISAM 相对简单，所以在效率上要优于 InnoDB，小型应用可以考虑使用 MyISAM；	MyISAM 更小的表空间
	- MyISAM 表是保存成文件的形式，在跨平台的数据转移中使用MyISAM存储会省去不少的麻烦；
	- InnoDB 表比 MyISAM 表更安全，可以在保证数据不丢失的情况下，切换非事务表到事务表；

- **2.2、适用场景：**

	- MyISAM 管理非事务表，它提供高速存储和检索，以及全文搜索能力，如果应用中需要执行大量的select查询，那么MyISAM是更好的选择
	- InnoDB 用于事务处理应用程序，具有众多特性，包括ACID事务支持.如果应用中需要执行大量的insert或update操作，则应该使用 InnoDB，这样可以提高多用户并发操作的性能

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

- 查看默认存储引擎

```sql
mysql> show variables like '%storage_engine%'；
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
- 对索引和缓存的支持

不要混合使用存储引擎

## 6、InnoDB详述

InnoDB采用[MVCC](../数据库锁机制.md#1MVCC:多版本并发控制)来支持高并发，并且实现了四个标准的隔离级别。其默认级别是`Repeatable read`（可重复读），并且通过间隙锁（next-key locking）策略防止幻读的出现。间隙锁使得InnoDB不仅仅锁定查询涉及的行，还会多索引中的间隙进行锁定，以防止幻影行的插入；

InnoDB是基于聚簇索引建立的。其二级索引（非主键索引）中必须包含主键列，如果主键列很大的话，其他的所有索引都会很大。若表上的索引较多的话，主键应当尽可能的小；

# 八、高级特性

## 1、数据库隔离级别介绍

### 1.1、不考虑隔离性发生的问题

- 脏读：指在一个事务处理过程里读取了另一个未提交的事务中的数据；
- 不可重复读：在对于数据库中的某个数据，一个事务范围内多次查询却返回了不同的数据值，这是由于在查询间隔，被另一个事务修改并提交了；脏读是某一事务读取了另一个事务未提交的脏数据，而不可重复读则是读取了前一事务提交的数据；不可重复读重点在于update和delete
- 虚读（幻读）：指当用户读取某一范围的数据行时，B事务在该范围内插入了新行，当用户再读取该范围的数据行时，会发现有新的“幻影”行;事务非独立执行时发生的一种现象；幻读的重点在于insert

不可重复读的和幻读很容易混淆，不可重复读侧重于修改，幻读侧重于新增或删除。解决不可重复读的问题只需锁住满足条件的行，解决幻读需要锁表

### 1.2、事务隔离级别

事务的隔离级别有4个，由低到高依次，级别越高执行效率越低
- Read uncommitted（未授权读取、读未提交）：如果一个事务已经开始写数据，则另外一个事务则不允许同时进行写操作，但允许其他事务读此行数据。该隔离级别可以通过“排他写锁”实现.最低级别，任何情况都无法保证；存在脏读，不可重复读，幻读的问题

- Read committed（授权读取、读提交）：该隔离级别避免了脏读，但是却可能出现不可重复读读取数据的事务允许其他事务继续访问该行数据，但是未提交的写事务将会禁止其他事务访问该行；一个事务只能看见已经提交事务所做的改变

	针对当前读，RC隔离级别保证对读取到的记录加锁 (记录锁)，存在幻读现象

- Repeatable read（可重复读取）：它确保同一事务的多个实例在并发读取数据时，会看到同样的数据行。可避免脏读、不可重复读的发生读取数据的事务将会禁止写事务(但允许读事务)，写事务则禁止任何其他事务。避免了不可重复读取和脏读，但是有时可能出现幻读。这可以通过“共享读锁”和“排他写锁”实现；

	针对当前读，RR隔离级别保证对读取到的记录加锁 (记录锁)，同时保证对读取的范围加锁，新的满足查询条件的记录不能够插入 (间隙锁)，不存在幻读现象

- Serializable（序列化）：提供严格的事务隔离.它要求事务序列化执行。事务只能一个接着一个地执行，但不能并发执行。

	从MVCC并发控制退化为基于锁的并发控制。不区别快照读与当前读，所有的读操作均为当前读，读加读锁 (S锁)，写加写锁 (X锁)。Serializable隔离级别下，读写冲突，因此并发度急剧下降

**隔离级别越高，越能保证数据的完整性和一致性，但是对并发性能的影响也越大.对于多数应用程序，可以优先考虑把数据库系统的隔离级别设为 Read Committed.它能够避免脏读取，而且具有较好的并发性能。可以通过悲观锁和乐观锁来控制不可重复读，幻读等并发问题.**

### 1.3、数据库默认隔离级别

大多数数据库的默认级别就是Read committed，比如Sql Server、Oracle

MySQL 的默认隔离级别就是 `Repeatable read`
- mysql中查看事务隔离级别：`select @@tx_isolation;`
- `set  [glogal | session]  transaction isolation level 隔离级别名称`;
- `set tx_isolation='隔离级别名称;'`
- 查看当前数据库的事务隔离级别：`show variables like 'tx_isolation'`

### 1.4、RC、RR级别下InnoDB的非阻塞读

### 1.5、RR可重复读如何避免幻读

MySQL InnoDB的可重复读并不保证避免幻读，需要应用使用加锁读来保证。而这个加锁度使用到的机制就是next-key locks；Innodb 的 RR 隔离界别对范围会加上 GAP，理论上不会存在幻读。

事务隔离级别为可重复读时，如果检索条件有索引（包括主键索引）的时候，默认加锁方式是next-key 锁；如果检索条件没有索引，更新数据时会锁住整张表。一个间隙被事务加了锁，其他事务是不能在这个间隙插入记录的，这样可以防止幻读

多版本并发控制机制解决了幻读问题

## 2、事务特性

ACID
- 原子性（Atomic）：指事务包含的所有操作要么全部成功，要么全部失败回滚
- 一致性（Consistency）：一致性是指事务必须使数据库从一个一致性状态变换到另一个一致性状态，也就是说一个事务执行之前和执行之后都必须处于一致性状态
- 隔离性（Isolation）：隔离性是当多个用户并发访问数据库时，比如操作同一张表时，数据库为每一个用户开启的事务，不能被其他事务的操作所干扰，多个并发事务之间要相互隔离；
- 持久性（Durability）：持久性是指一个事务一旦被提交了，那么对数据库中的数据的改变就是永久性的，即便是在数据库系统遇到故障的情况下也不会丢失提交事务的操作

# 九、数据库锁

## 1、锁

是计算协调多个进程或者线程来并发访问某一资源的机制.数据库并发的一致性和有效性

## 2、锁的分类

### 2.1、按对数据库的操作类型分

- 读锁(共享锁)：针对同一份数据，多个读操作可以同时进行而不相互影响；
- 写锁(互斥锁)：当前写操作没有完成前，它会阻塞其他写锁和读锁

### 2.2.从对数据操作的粒度分

表锁、行锁、页级锁

## 3、表锁（偏读）

偏向 MyISAM 存储引擎，开销小，加锁快，无死锁；锁粒度大，发生的锁冲突的概率最高，并发度最低；

- **3.1、MyISAM 在执行查询语句之前，会自动给涉及的所有表加读锁，在执行增删改操作前，会自动给涉及的表加写锁.**

- **3.2、MySQL的表级锁有两种：**

	- 表共享读锁
	- 表独占写锁

- **3.3、结论：在对 MyISAM 表进行操作时，会出现以下情况：**

	- 对 MyISAM 表的读操作不会阻塞其他进程对同一表的请求，但会阻塞对同一表的写请求.只有当读锁释放后，才会进行其他进程的写操作；
	- 对 MyISAM 表的写操作会阻塞其他进程对同一表的读和写操作，只有当写锁释放后，才会执行其他进程的读写操作；
	
	读锁会阻塞写锁，不会阻塞读； 而写锁会把读和写都阻塞.

- **3.4、看哪些表被加锁了：**

0 -> 表示没有加锁； 1 -> 表示加锁
```
mysql> show open tables；
+--------------------+----------------------------------------------+--------+-------------+
| Database           | Table                                        | In_use | Name_locked |
+--------------------+----------------------------------------------+--------+-------------+
| mysql              | time_zone_transition_type                    |      0 |           0 |
| performance_schema | events_waits_summary_global_by_event_name    |      0 |           0 |
| performance_schema | file_summary_by_instance                     |      0 |           0 |
| performance_schema | setup_instruments                            |      0 |           0 |
| mysql              | servers                                      |      0 |           0 |
+--------------------+----------------------------------------------+--------+-------------+
```
- **3.5、分析表锁：**

可以通过检查 table_locks_immediate 和 table_locks_waited 状态变量来分析系统上的表锁定
```
mysql> show status like 'table%'；
+-----------------------+-------+
| Variable_name         | Value |
+-----------------------+-------+
| Table_locks_immediate | 36    |
| Table_locks_waited    | 0     |
+-----------------------+-------+
```
- table_locks_immediate：产生表级锁定的次数，表示可以立即获取锁的查询次数，每立即获取锁值加1；
- table_locks_waited：出现表级锁定争用而发生等待的次数（不能立即获取锁的次数，每等待一次锁值加1）此值高则说明存在着较为严重的表级锁定争用情况；

MyISAM 的读写锁调度是写优先，这也是 MyISAM 不适合做写为主表的引擎。因为写锁后，其他线程不能做任何操作，大量的更新会使查询很难得到锁，从而造成永远阻塞

## 4、行锁

行锁偏向InnoDB存储引擎，开销大，加锁慢；会出现死锁；锁的粒度最小，发生锁冲突的概率最低，并发度也最高；InnoDB与MyISAM最大不同点是：支持事务和采用了行级锁；

- **4.1、索引失效后无索引行由行锁升级为表锁.**

- **4.2、间隙锁：**

	- 当用范围条件而不是相等条件检索数据，并请求共享或排他锁时，InnoDB 会给符合条件的已有数据记录的索引项加锁；对于键值在条件范围内但并不存在的记录，叫做“间隙”，InnoDB 也会对这个“间隙”加锁，这种锁机制就是所谓的间隙锁；
	
	- 危害：因为在查询的执行过程通过范围查找的话，它会锁定整个范围内所得索引键值，即使这个键值不存在；间隙锁有一个比较致命的弱点：就是当锁定一个范围键值之后，即使某些不存在的键值也会被无辜的锁定，造成在锁定的时候无法插入锁定键值范围内的任何数据.在某些场景下可能会对性能造成很大的危害.
		
- **4.3、如何手动锁定一行：**

	begin：<br>
	select xxx for update 锁定某一行后，其他的操作会被阻塞，直到锁定行的会话commit

- **4.4、InnoDB 存储引擎由于实现了行级锁虽然在锁定机制的实现方面锁带来的性能损耗可能会比表级锁定会更高些，但是在整体并发处理能力方面要远远优于 MyISAM 的表级锁定。当系统并发量较高时，InnoDB的整体性能和MyISAM 相比会有比较明显的优势；但是InnoDB的行级锁定同样有脆弱的一面，当我们使用不当时，可能会让InnoDB 的整体性能表现不仅不能比MyISAM 高，甚至可能更差**

- **4.5、行级锁分析：**
```
mysql> show status like 'innodb_row_lock%'；
+-------------------------------+-------+
| Variable_name                 | Value |
+-------------------------------+-------+
| Innodb_row_lock_current_waits | 0     |
| Innodb_row_lock_time          | 41389 |
| Innodb_row_lock_time_avg      | 13796 |
| Innodb_row_lock_time_max      | 20024 |
| Innodb_row_lock_waits         | 3     |
+-------------------------------+-------+
```
Innodb_row_lock_current_waits：当前正在等待的锁定数量；<br>
Innodb_row_lock_time：从系统启动到现在锁定的总时间长度；(******)<br>
Innodb_row_lock_time_avg：每次等待所花平均时间；(******)<br>
Innodb_row_lock_time_max：从系统启动到现在等待最长的一次所花的时间<br>
Innodb_row_lock_waits：等待总次数(******)<br>

## 5、死锁

- **5.1、什么是死锁：**

	是指两个或两个以上的进程在执行过程中，因争夺资源而造成的一种互相等待的现象，若无外力作用，它们都将无法推进下去。此时称系统处于死锁状态或系统产生了死锁，这些永远在互相等竺的进程称为死锁进程。表级锁不会产生死锁.所以解决死锁主要还是针对于最常用的InnoDB。

*死锁的关键在于：两个（或以上）的Session加锁的顺序不一致。*

- **5.2、死锁产生原因：**

	死锁一般是事务相互等待对方资源，最后形成环路造成的

- **5.3、分析死锁日志：**

SHOW ENGINE INNODB STATUS;

- **5.4、死锁案例：**

	- 不同表相同记录行锁冲突：事务A和事务B操作两张表，但出现循环等待锁情况
	- 相同表记录行锁冲突.这种情况比较常见：遇到两个job在执行数据批量更新时，jobA处理的的id列表为[1，2，3，4]，	而job处理的id列表为[8，9，10，4，2]，这样就造成了死锁
	- 不同索引锁冲突：事务A在执行时，除了在二级索引加锁外，还会在聚簇索引上加锁，在聚簇索引上加锁的顺序是[1，4，2，3，5]，而事务B执行时，只在聚簇索引上加锁，加锁顺序是[1，2，3，4，5]，这样就造成了死锁的可能性.
	- gap锁冲突：innodb在RR级别下，如下的情况也会产生死锁

- **5.5、避免死锁：**

- 以固定的顺序访问表和行.比如两个job批量更新的情形，简单方法是对id列表先排序，后执行，这样就避免了交叉等待锁的情形；又比如将两个事务的sql顺序调整为一致，也能避免死锁；
- 大事务拆小。大事务更倾向于死锁，如果业务允许，将大事务拆小；
- 在同一个事务中，尽可能做到一次锁定所需要的所有资源，减少死锁概率；
- 降低隔离级别：如果业务允许，将隔离级别调低也是较好的选择，比如将隔离级别从RR调整为RC，可以避免掉很多因为gap锁造成的死锁；
- 为表添加合理的索引：可以看到如果不走索引将会为表的每一行记录添加上锁，死锁的概率大大增大；

- **5.6、定位死锁问题：**
	- 通过应用业务日志定位到问题代码，找到相应的事务对应的sql；
	- 确定数据库隔离级别

## 6、乐观锁与悲观锁：(数据库)

[数据库锁机制](../数据库锁机制.md)

- **6.1、悲观锁：**

	- 是对数据被外界（包括本系统当前的其他事务，以及来自外部系统的事务处理）修改持保守态度（悲观）；因此，在整个数据处理过程中，将数据处于锁定状态。悲观锁的实现，往往依靠数据库提供的锁机制；

	(2).悲观并发控制实际上是"先取锁再访问"的保守策略，为数据处理的安全提供了保证.但是在效率方面，处理加锁的机制会让数据库产生额外的开销，还有增加产生死锁的机会

- **6.2、乐观锁：**
	- 乐观锁假设认为数据一般情况下不会造成冲突，所以在数据进行提交更新的时候，才会正式对数据的冲突与否进行检测，如果发现冲突了，则让返回用户错误的信息，让用户决定如何去做；
	- 相对于悲观，在对数据库进行处理的时候，乐观锁并不会使用数据库提供的锁机制.一般的实现乐观锁的方式就是记录数据版本

		- 数据版本：为数据增加的一个版本标识。当读取数据时，将版本标识的值一同读出，数据每更新一，同时对版本标识进行更新。当我们提交更新的时候，，判断数据库表对应记录的当前版本信息与第一次取出来的版本标识进行比对，如果数据库表当前版本号与第一次取出来的版本标识值相等，则予以更新.否则认为是过期数据；

	- 实现数据版本有两种方式：第一种是使用版本号.第二种是使用时间戳

# 十、表分区

## 1、表分区

是指根据一定规则，将数据库中的一张表分解成多个更小的，容易管理的部分。从逻辑上看，只有一张表，但是底层却是由多个物理分区组成。

子分区：分区表中对每个分区再次分割，又成为复合分区

## 2、与分表的区别

- 分表：指的是通过一定规则，将一张表分解成多张不同的表
- 表与分区的区别在于：分区从逻辑上来讲只有一张表，而分表则是将一张表分解成多张表

## 3、表分区的优点

- 分区表的数据可以分布在不同的物理设备上，从而高效地利用多个硬件设备；
- 和单个磁盘或者文件系统相比，可以存储更多数据；
- 优化查询.在where语句中包含分区条件时，可以只扫描一个或多个分区表来提高查询效率；涉及sum和count语句时，也可以在多个分区上并行处理，最后汇总结果。
- 分区表更容易维护.例如：想批量删除大量数据可以清除整个分区；
- 可以使用分区表来避免某些特殊的瓶颈.例如InnoDB的单个索引的互斥访问，ext3问价你系统的inode锁竞争等；

## 4、表分区的限制因素

- 一个表最多只能有1024个分区；
- MySQL5.1中，分区表达式必须是整数，或者返回整数的表达式.在MySQL5.5中提供了非整数表达式分区的支持；
- 如果分区字段中有主键或者唯一索引的列，那么多有主键列和唯一索引列都必须包含进来；即：分区字段要么不包含主键或者索引列，要么包含全部主键和索引列.
- 分区表中无法使用外键约束；
- MySQL 的分区适用于一个表的所有数据和索引，不能只对表数据分区而不对索引分区，也不能只对索引分区而不对表分区，也不能只对表的一部分数据分区；

## 5、查看分区：判断 MySQL 是否支持表分区
```
mysql> show variables like '%partition%'；
+-------------------+-------+
| Variable_name     | Value |
+-------------------+-------+
| have_partitioning | YES   |
+-------------------+-------+
1 row in set (0.00 sec)
```
## 6、MySQL 支持的分区类型

- RANGE分区：按照数据的区间范围分区；
- LIST分区：按照List中的值分区，与 RANGE的区别是，range分区的区间范围值是连续的；
- HASH分区
- KEY分区

说明：在MySQL5.1版本中，RANGE，LIST，HASH 分区要求分区键必须是 int 类型，或者通过表达式返回INT类型.但KEY分区的时候，可以使用其他类型的列（BLOB，TEXT类型除外）作为分区键；

## 7、RANGE分区

- 利用取值范围进行分区，区间要连续并且不能互相重叠.语法如下：
```sql
partition by range(exp)( --exp可以为列名或者表达式，比如to_date(created_date)
	partition p0 values less than(num)
)
-- 例子：
create table emp (
	id       int not null，
	store_id int not null
)
partition by range (store_id) (
	partition p0 values less than (10)，
	partition p1 values less than (20)
)；
```
上面的语句创建了emp表，并根据store_id字段进行分区，小于10的值存在分区p0中，大于等于10，小于20的值存在分区p1中;

注意：每个分区都是按顺序定义的，从最低到最高;

上面的语句，如果将less than(10) 和less than (20)的顺序颠倒过来，那么将报错，如下：

ERROR 1493 (HY000)： VALUES LESS THAN value must be strictly increasing for each partition

- RANGE分区存在问题:

	- range 范围覆盖问题：当插入的记录中对应的分区键的值不在分区定义的范围中的时候，插入语句会失败。上面的例子，如果我插入一条store_id = 30的记录会怎么样呢？我们上面分区的时候，最大值是20，如果插入一条超过20的记录，会报错：<br>
		mysql> insert into emp value(30，30)；<br>
		ERROR 1526 (HY000)： Table has no partition for value 30.<br>
		解决方案：
		- ①、预估分区键的值，及时新增分区.
		- ②、设置分区的时候，使用 values less than maxvalue 子句，MAXVALUE表示最大的可能的整数值.
		- ③、尽量选择能够全部覆盖的字段作为分区键，比如一年的十二个月等
	- Range分区中，分区键的值如果是NULL，将被作为一个最小值来处理

## 8、LIST分区

List分区是建立离散的值列表告诉数据库特定的值属于哪个分区，语法：

```sql
partition by list(exp)( --exp为列名或者表达式
	partition p0 values in (3，5)  --值为3和5的在p0分区
)
```
例子：
```sql
create table emp1 (
	id       int not null，
	store_id int not null
)
	partition by list (store_id) (
	partition p0 values in (3， 5)，
	partition p1 values in (2， 6， 7， 9)
	)
```
注意：如果插入的记录对应的分区键的值不在list分区指定的值中，将会插入失败.并且，list不能像range分区那样提供maxvalue.

## 9、Columns分区

MySQL5.5中引入的分区类型，解决了5.5版本之前range分区和list分区只支持整数分区的问题

Columns分区可以细分为 range columns分区和 list columns分区，他们都支持整数、日期时间、字符串三大数据类型；

- 与 RANGE分区 和 LIST分区区别：

	针对日期字段的分区就不需要再使用函数进行转换了，例如针对date字段进行分区不需要再使用YEAR()表达式进行转换；COLUMN分区支持多个字段作为分区键但是不支持表达式作为分区键；

- COLUMNS支持的类型：

	整形支持：tinyint，smallint，mediumint，int，bigint；不支持decimal和float<br>
	时间类型支持：date，datetime<br>
	字符类型支持：char，varchar，binary，varbinary；不支持text，blob<br>

- **9.1、RANGE COLUMNS分区：**

	- 日期字段分区：

	```sql
	create table members(
		id int，
		joined date not NULL
	)
		partition by range columns(joined)(
		partition a values less than('1980-01-01')，
		partition b values less than('1990-01-01')，
		partition c values less than('2000-01-01')，
		partition d values less than('2010-01-01')，
		partition e values less than MAXVALUE
		);
	```

	- 多个字段组合分区：
		注意：多字段的分区键比较是基于数组的比较：
		- ①、它先用插入的数据的第一个字段值和分区的第一个值进行比较，如果插入的第一个值小于分区的第一个值那么就不需要比较第二个值就属于该分区
		- ②、如果第一个值等于分区的第一个值，开始比较第二个值同样如果第二个值小于分区的第二个值那么就属于该分区；
	```sql
	CREATE TABLE rcx (
		a INT，
		b INT
		)
	PARTITION BY RANGE COLUMNS(a，b) (
		PARTITION p0 VALUES LESS THAN (5，10)，
		PARTITION p1 VALUES LESS THAN (10，20)，
		PARTITION p2 VALUES LESS THAN (15，30)，
		PARTITION p3 VALUES LESS THAN (MAXVALUE，MAXVALUE)
	);
	```

	*RANGE COLUMN的多列分区第一列的分区值一定是顺序增长的，不能出现交叉值，第二列的值随便，例如以下分区就会报错：*

	```sql
	PARTITION BY RANGE COLUMNS(a，b) (
			PARTITION p0 VALUES LESS THAN (5，10)，
			PARTITION p1 VALUES LESS THAN (10，20)，
			PARTITION p2 VALUES LESS THAN (8，30)， -- p2 中第一列比p1第一列的要小，所以报错
			PARTITION p3 VALUES LESS THAN (MAXVALUE，MAXVALUE)
	);
	```
- **9.2、LIST COLUMNS分区：**

- 非整型字段分区：
```sql
create table listvar (
	id    int      not null，
	hired datetime not null
)
	partition by list columns (hired)
	(
	partition a values in ('1990-01-01 10：00：00'， '1991-01-01 10：00：00')，
	partition b values in ('1992-01-01 10：00：00')，
	partition c values in ('1993-01-01 10：00：00')，
	partition d values in ('1994-01-01 10：00：00')
	)；
```
LIST COLUMNS分区对分整形字段进行分区就无需使用函数对字段处理成整形，所以对非整形字段进行分区建议选择COLUMNS分区

- 多字段分区：
```sql
create table listvardou (
	id    int      not null，
	hired datetime not null
)
	partition by list columns (id， hired)
	(
		partition a values in ( (1， '1990-01-01 10：00：00')， (1， '1991-01-01 10：00：00') )，
		partition b values in ( (2， '1992-01-01 10：00：00') )，
		partition c values in ( (3， '1993-01-01 10：00：00') )，
		partition d values in ( (4， '1994-01-01 10：00：00') )
	)；
```
## 10、HASH分区

- 主要用来分散热点读，确保数据在预先确定个数的分区中尽可能平均分布.
- MySQL支持两种Hash分区：常规Hash分区和线性Hash分区

	- 常规Hash分区-使用取模算法，语法如下：

		partition by hash(store_id) partitions 4；<br>
		上面的语句，根据store_id对4取模，决定记录存储位置.比如store_id = 234的记录，MOD(234，4)=2，所以会被存储在第二个分区.

		常规Hash分区的优点和不足-优点：能够使数据尽可能的均匀分布；缺点：不适合分区经常变动的需求。如果需要增加两个分区变成6个分区，大部分数据都要重新计算分区.线性Hash分区可以解决。

	- 线性Hash分区-分区函数是一个线性的2的幂的运算法则，语法如下：

		partition by LINER hash(store_id) partitions 4；

		算法介绍：假设要保存记录的分区编号为N，num为一个非负整数，表示分割成的分区的数量，那么N可以通过以下步骤得到：

		- Step 1. 找到一个大于等于num的2的幂，这个值为V，V可以通过下面公式得到：<br>
			V = Power(2，Ceiling(Log(2，num)))<br>
			例如：刚才设置了4个分区，num=4，Log(2，4)=2，Ceiling(2)=2，power(2，2)=4，即V=4
		- Step 2. 设置N=F(column_list)&(V-1)<br>
			例如：刚才V=4，store_id=234对应的N值，N = 234&(4-1) =2<br>
		- Step 3. 当N>=num，设置V=Ceiling(V/2)，N=N&(V-1)<br>
			例如：store_id=234，N=2<4，所以N就取值2，即可.<br>
			假设上面算出来的N=5，那么V=Ceiling(2.5)=3，N=234&(3-1)=1，即在第一个分区.<br>

		线性Hash的优点和不足-优点：在分区维护(增加，删除，合并，拆分分区)时，MySQL能够处理得更加迅速；

		缺点：与常规Hash分区相比，线性Hash各个分区之间的数据分布不太均衡

## 11、KEY分区

类似Hash分区，Hash分区允许使用用户自定义的表达式，但Key分区不允许使用用户自定义的表达式。Hash仅支持整数分区，而Key分区支持除了Blob和text的其他类型的列作为分区键。

partition by key(exp) partitions 4； --exp是零个或多个字段名的列表

key分区的时候，exp可以为空，如果为空，则默认使用主键作为分区键，没有主键的时候，会选择非空惟一键作为分区键；

## 12、分区对于NULL值的处理

MySQ允许分区键值为NULL，分区键可能是一个字段或者一个用户定义的表达式。一般情况下，MySQL在分区的时候会把 NULL 值当作零值或者一个最小值进行处理。

*注意：*

- Range分区中：NULL 值被当作最小值来处理
- List分区中：NULL 值必须出现在列表中，否则不被接受
- Hash/Key分区中：NULL 值会被当作零值来处理

## 13、分区管理

- **13.1、增加分区**

	- RANGE分区和LIST分区：

		alter table table_name add partition (partition p0 values ...(exp))：
		values后面的内容根据分区的类型不同而不同

	- Hash分区和Key分区：

		alter table table_name add partition partitions 8； -- 指的是新增8个分区

- **13.2、删除分区：**

	- RANGE分区和LIST分区：

		alter table table_name drop partition p0； --p0为要删除的分区名称，删除了分区，同时也将删除该分区中的所有数据。同时，如果删除了分区导致分区不能覆盖所有值，那么插入数据的时候会报错.

	- Hash分区和Key分区：

		alter table table_name coalesce partition 2； --将分区缩减到2个

- **13.3、移除分区：**

alter table members remove partitioning；
使用remove移除分区是仅仅移除分区的定义.并不会删除数据和 drop PARTITION 不一样，后者会连同数据一起删除

## 14、分区查询

- 查询某张表一共有多少个分区：
```sql
	SELECT
		partition_name                   part，
		partition_expression             expr，
		partition_description            descr，
		FROM_DAYS(partition_description) lessthan_sendtime，
		table_rows
	FROM
		INFORMATION_SCHEMA.partitions
	WHERE
		TABLE_SCHEMA = SCHEMA ()
		AND TABLE_NAME = 'emp'；
```

- 查看执行计划，判断查询数据是否进行了分区过滤
```
mysql> explain partitions select * from emp where store_id=5；
+----+-------------+-------+------------+------+---------------+------+---------+------+------+-------------+
| id | select_type | table | partitions | type | possible_keys | key  | key_len | ref  | rows | Extra       |
+----+-------------+-------+------------+------+---------------+------+---------+------+------+-------------+
|  1 | SIMPLE      | emp   | p1         | ALL  | NULL          | NULL | NULL    | NULL |    2 | Using where |
+----+-------------+-------+------------+------+---------------+------+---------+------+------+-------------+
1 row in set
```
上面的结果：partitions：p1 表示数据在p1分区进行检索

https://mp.weixin.qq.com/s/K40FKzM5gUJIVQCvX6YtnQ

# 十一、数据库结构设计

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

所谓的反范式化是为了性能和读取效率的考虑而适当的对数据库设计范式的要求进行违反，而允许存在的少量的数据冗余.即反范式化就是使用空间来换取时间

# 十二、mysql高可用架构设计
## 1、mysql复制功能

mysql复制功能提供分担读负载，基于主库的二进制日志，异步的。无法保证主库与从库的延迟。

mysql复制解决了什么问题
- 实现在不同服务器上的数据分布，利用二进制日志增量进行；不需要太多的带宽；但是使用基于行的复制在进行大批量的更改时会对带宽带来一定的压力，特别是跨IDC环境进行复制；应该分批进行复制
- 实现数据读取的负载均衡，需要其他组件配合完成
- 增强了数据安全性，利用备库的备份来减少主库负载，并不能用复制来代替备份
- 实现数据库高可用和故障切换；避免mysql的单点故障
- 实现数据库在线升级

## 2、Mysql日志

- mysql存储引擎层日志

	重做日志、回滚日志

- mysql服务处日志

	二进制日志、慢查日志、通用日志

## 3、mysql二进制日志

记录了所有对mysql数据库的修改事件，包括增删查改事件和对表结构的修改事件。且二进制日志中记录是已执行成功的记录

### 3.1、mysql二进制日志格式

查看二进制日志格式：show variables like 'binlog_format';

修改二进制日志格式：set binlog_format=''

- 基于段的格式：binlog_format=STATEMENT，mysql5.7之前默认的格式，主要记录的执行sql语句
	- 优点：日志记录量相对较小，节约磁盘及网络IO
	- 缺点：必须记录上下文信息，保证语句在从服务器上的执行结果与主服务器上执行结果相同，但是非确定性函数还是无法正确复制，有可能mysql主从服务器数据不一致

- 基于行的格式：binlog_format=ROW，mysql5.7之后的默认格式，可以避免主从服务器数据不一致情况

? 数据库如何实现 rollback 的？

# 十三、数据库分库分表
## 1、数据切分

基本思想是把一个数据库切成多个部分放到不同的数据库上，从而缓解单一数据库的性能问题.

- 对应海量数据，如果是因为表多而数据多，适合使用垂直切分，即把关系紧密的表切分放在一个server上；
- 如果表不多，但是每张表的数据非常多，适合水平切分，即把表的数据根据某种规则切分到多个数据库上.

## 2、垂直切分

- **2.1、最大特点：**

	规则简单，实施也更为方便，尤其适合各业务之间的耦合度非常低，相互影响很小，业务逻辑非常清晰的系统；这种系统中可以很容易做到将不同业务模块所使用的表分拆到不同的数据库中；

- **2.2、垂直切分常见有**

	- 垂直分库：
		根据业务耦合性，将关联度低的表存储在不同的数据库，做法与大系统拆分为多个小系统类似，按业务分类进行独立划分；
	- 垂直分表：
		是基于数据库中的“列”进行的，某个表字段比较多，可以新建一张扩展表，将不经常用的字段或长度较大的字段拆分到扩展表中。在字段很多的情况下，通过大表拆小表

- **2.3、垂直切分优缺点：**

	- 优点：
		- 解决业务系统层面的耦合，业务清晰
		- 与微服务的治理类似，也能对不同业务的数据进行分级管理、维护、监控、扩展等
		- 高并发场景下，垂直切分一定程度的提升IO、数据库连接数、单机硬件资源的瓶颈
	- 缺点：
		- 部分表无法join，只能通过接口聚合方式解决，提升了开发的复杂度
		- 分布式事务处理复杂
		- 依然存在单表数据量过大的问题（需要水平切分）


## 3、水平切分

当一个应用难以再细粒度的垂直切分，或切分后数据量行数巨大，存在单库读写、存储性能瓶颈，这时候就需要进行水平切分了；

- **3.1、特点：**

	- 相对垂直切分来说，稍微复杂点.因为需要将同一个表的不同数据拆分到不同的数据库中，对于应用程序来说，拆分规则本身就较根据表名来拆分更为复杂，后期的数据维护也会更为复杂一些
	- 多数系统会将垂直切分和水平切分联合使用：先对系统做垂直切分，再针对每一小搓表的情况选择性地做水平切分，从而将整个数据库切分成一个分布式矩阵；

- **3.2、水平切分：**

	水平切分分为库内分表和分库分表，是根据表内数据内在的逻辑关系，将同一个表按不同的条件分散到多个数据库或多个表中，每个表中只包含一部分数据，从而使得单个表的数据量变小，达到分布式的效果；

	库内分表只解决了单一表数据量过大的问题，但没有将表分布到不同机器的库上，因此对于减轻MySQL数据库的压力来说，帮助不是很大，大家还是竞争同一个物理机的CPU、内存、网络IO，最好通过分库分表来解决

- **3.3、水平切分优点**

	- 不存在单库数据量过大、高并发的性能瓶颈，提升系统稳定性和负载能力
	- 应用端改造较小，不需要拆分业务模块

- **3.4、缺点：**
	- 跨分片的事务一致性难以保证
	- 跨库的join关联查询性能较差
	- 数据多次扩展难度和维护量极大

- **3.5、水平切分典型分片规则**

	- （1）根据数值范围：按照时间区间或ID区间来切分
		- 优点：
			- 单表大小可控
			- 天然便于水平扩展，后期如果想对整个分片集群扩容时，只需要添加节点即可，无需对其他分片的数据进行迁移
			- 使用分片字段进行范围查找时，连续分片可快速定位分片进行快速查询，有效避免跨分片查询的问题
		- 缺点：<br>
			热点数据成为性能瓶颈。连续分片可能存在数据热点，例如按时间字段分片，有些分片存储最近时间段内的数据，可能会被频繁的读写，而有些分片存储的历史数据，则很少被查询
	- （2）根据数值取模：一般采用hash取模mod的切分方式
		- 优点：
			数据分片相对比较均匀，不容易出现热点和并发访问的瓶颈；
		- 缺点：<br>
			- 后期分片集群扩容时，需要迁移旧的数据（使用一致性hash算法能较好的避免这个问题）
			- 容易面临跨分片查询的复杂问题。比如上例中，如果频繁用到的查询条件中不带cusno时，将会导致无法定位数据库，从而需要同时向4个库发起查询，再在内存中合并数据，取最小集返回给应用，分库反而成为拖累


## 4、切分策略

- 是按先垂直切分再水平切分的步骤进行的.
- 垂直切分的思路就是分析表间的聚合关系，把关系紧密的表放在一起

## 5、数据切分带来的问题

### 5.1、数据库切分后事务问题

分布式事务和通过应用程序与数据库共同控制实现事务.

- 分布式事务：
	- 优点：交由数据库管理，简单有效
	- 缺点：性能代价高，特别是shard越来越多时
- 由应用程序和数据库共同控制：
	- 原理：将一个跨多个数据库的分布式事务分拆成多个仅处于单个数据库上面的小事务，并通过应用程序来总控各个小事务.
	- 优点：性能上有优势
	- 缺点：需要应用程序在事务控制上做灵活设计.如果使用了spring的事务管理，改动起来会面临一定的困难

### 5.2、跨节点Join的问题：只要是进行切分，跨节点Join的问题是不可避免的

解决这一问题的普遍做法是分两次查询实现：在第一次查询的结果集中找出关联数据的id，根据这些id发起第二次请求得到关联数据；

另外解决这一问题的方法：
- （1）全局表：也可看做是"数据字典表"，就是系统中所有模块都可能依赖的一些表，为了避免跨库join查询，可以将这类表在每个数据库中都保存一份。这些数据通常很少会进行修改，所以也不担心一致性的问题；
- （2）字段冗余：典型的反范式设计，利用空间换时间，为了性能而避免join查询
- （3）数据组装：在系统层面，分两次查询，第一次查询的结果集中找出关联数据id，然后根据id发起第二次请求得到关联数据。最后将获得到的数据进行字段拼装；
- （4）ER分片：关系型数据库中，如果可以先确定表之间的关联关系，并将那些存在关联关系的表记录存放在同一个分片上，那么就能较好的避免跨分片join问题。在1:1或1:n的情况下，通常按照主表的ID主键切分

### 5.3、跨节点的 count，order by，group by 以及聚合函数问题

因为它们都需要基于全部数据集合进行计算.多数的代理都不会自动处理合并工作；解决方案：与解决跨节点join问题的类似，分别在各个节点上得到结果后在应用程序端进行合并；

分页需要按照指定字段进行排序，当排序字段就是分片字段时，通过分片规则就比较容易定位到指定的分片；当排序字段非分片字段时，就变得比较复杂了。需要先在不同的分片节点中将数据进行排序并返回，然后将不同分片返回的结果集进行汇总和再次排序，最终返回给用户；

在使用Max、Min、Sum、Count之类的函数进行计算的时候，也需要先在每个分片上执行相应的函数，然后将各个分片的结果集进行汇总和再次计算，最终将结果返回。

### 5.4、切分后主键问题

- 常见的主键生成策略：一旦数据库被切分到多个物理结点上，我们将不能再依赖数据库自身的主键生成机制，某个分区数据库自生成的ID无法保证在全局上是唯一的
	- UUID：使用UUID作主键是最简单的方案，但缺点非常明显：由于UUID非常的长，除占用大量存储空间外，最主要的问题是在索引上，在建立索引和基于索引进行查询时都存在性能问题
	- 结合数据库维护一个Sequence表，缺点同样明显：由于所有插入任何都需要访问该表，该表很容易成为系统性能瓶颈，同时它也存在单点问题

[分布式主键ID](https://github.com/chenlanqing/learningNote/blob/master/Java/Java%E6%9E%B6%E6%9E%84/%E5%88%86%E5%B8%83%E5%BC%8F.md#%E4%B8%83%E5%88%86%E5%B8%83%E5%BC%8Fid)
	
### 5.5、数据库迁移、扩容问题

## 6、什么时候考虑数据切分

- 能不切分尽量不要切分：不到万不得已不用轻易使用分库分表这个大招，避免"过度设计"和"过早优化"；
- 数据量过大，正常运维影响业务访问：
	- （1）对数据库备份，如果单表太大，备份时需要大量的磁盘IO和网络IO；
	- （2）对一个很大的表进行DDL修改时，MySQL会锁住全表，这个时间会很长，这段时间业务不能访问此表，影响很大。如果使用pt-online-schema-change，使用过程中会创建触发器和影子表，也需要很长的时间。在此操作过程中，都算为风险时间。将数据表拆分，总量减少，有助于降低这个风险；
	- （3）大表会经常访问与更新，就更有可能出现锁等待。将数据切分，用空间换时间，变相降低访问压力；
- 随着业务发展，需要对某些字段垂直拆分；
- 数据量快速增长
- 安全性和可用性 

# 参考文章

* [数据库原理](http://blog.csdn.net/albertfly/article/details/51318995)
* [How does a relational database work](http://coding-geek.com/how-databases-work/)
* [MySQL索引背后的数据结构及算法原理](http://blog.codinglabs.org/articles/theory-of-mysql-index.html)
* [一千个不用 Null 的理由](https://my.oschina.net/leejun2005/blog/1342985)
* [SQL调优](http://www.cnblogs.com/Qian123/p/5666569.html)
* [sql语句查询执行顺序](http://blog.csdn.net/bitcarmanlee/article/details/51004767)
* [数据库事务的特性](http://www.cnblogs.com/fjdingsd/p/5273008.html)
* [MySQL 死锁问题分析](http://blog.jobbole.com/99208/)
* [数据库分库分表思路](http://www.cnblogs.com/butterfly100/p/9034281.html)
* [MySQL加锁分析](https://mp.weixin.qq.com/s/lmKnhBM78ZgEF70kuQiTLg)