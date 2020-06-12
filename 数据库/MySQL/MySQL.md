<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、Mysql概述](#%E4%B8%80mysql%E6%A6%82%E8%BF%B0)
  - [1、MySQL服务器参数](#1mysql%E6%9C%8D%E5%8A%A1%E5%99%A8%E5%8F%82%E6%95%B0)
    - [1.1、内存配置相关参数](#11%E5%86%85%E5%AD%98%E9%85%8D%E7%BD%AE%E7%9B%B8%E5%85%B3%E5%8F%82%E6%95%B0)
    - [1.2、如何为缓存池分配内存](#12%E5%A6%82%E4%BD%95%E4%B8%BA%E7%BC%93%E5%AD%98%E6%B1%A0%E5%88%86%E9%85%8D%E5%86%85%E5%AD%98)
    - [1.3、I/O相关配置参数](#13io%E7%9B%B8%E5%85%B3%E9%85%8D%E7%BD%AE%E5%8F%82%E6%95%B0)
    - [1.4、安全配置参数](#14%E5%AE%89%E5%85%A8%E9%85%8D%E7%BD%AE%E5%8F%82%E6%95%B0)
    - [1.5、其他配置参数](#15%E5%85%B6%E4%BB%96%E9%85%8D%E7%BD%AE%E5%8F%82%E6%95%B0)
  - [2、MySQL版本](#2mysql%E7%89%88%E6%9C%AC)
    - [2.1、MySQL常见的发行版本](#21mysql%E5%B8%B8%E8%A7%81%E7%9A%84%E5%8F%91%E8%A1%8C%E7%89%88%E6%9C%AC)
    - [2.2、版本升级](#22%E7%89%88%E6%9C%AC%E5%8D%87%E7%BA%A7)
    - [2.3、MySQL版本特性](#23mysql%E7%89%88%E6%9C%AC%E7%89%B9%E6%80%A7)
  - [3、MySQL架构](#3mysql%E6%9E%B6%E6%9E%84)
    - [3.1、如何设计关系型数据库](#31%E5%A6%82%E4%BD%95%E8%AE%BE%E8%AE%A1%E5%85%B3%E7%B3%BB%E5%9E%8B%E6%95%B0%E6%8D%AE%E5%BA%93)
    - [3.2、MySQL架构](#32mysql%E6%9E%B6%E6%9E%84)
  - [4、MySQL与Oracle区别](#4mysql%E4%B8%8Eoracle%E5%8C%BA%E5%88%AB)
- [二、MySQL操作](#%E4%BA%8Cmysql%E6%93%8D%E4%BD%9C)
  - [1、连接命令行](#1%E8%BF%9E%E6%8E%A5%E5%91%BD%E4%BB%A4%E8%A1%8C)
  - [2、SQL操作](#2sql%E6%93%8D%E4%BD%9C)
  - [3、创建数据库](#3%E5%88%9B%E5%BB%BA%E6%95%B0%E6%8D%AE%E5%BA%93)
  - [4、数据库的查询](#4%E6%95%B0%E6%8D%AE%E5%BA%93%E7%9A%84%E6%9F%A5%E8%AF%A2)
  - [5、表的定义](#5%E8%A1%A8%E7%9A%84%E5%AE%9A%E4%B9%89)
  - [6、数据的操作（DML）](#6%E6%95%B0%E6%8D%AE%E7%9A%84%E6%93%8D%E4%BD%9Cdml)
    - [6.1、insert](#61insert)
    - [6.2、update](#62update)
    - [6.3、delete](#63delete)
  - [7、校对规则](#7%E6%A0%A1%E5%AF%B9%E8%A7%84%E5%88%99)
- [三、MySQL数据类型](#%E4%B8%89mysql%E6%95%B0%E6%8D%AE%E7%B1%BB%E5%9E%8B)
  - [1、数值型](#1%E6%95%B0%E5%80%BC%E5%9E%8B)
  - [2、日期类型](#2%E6%97%A5%E6%9C%9F%E7%B1%BB%E5%9E%8B)
    - [2.1、datetime](#21datetime)
    - [2.2、date](#22date)
    - [2.3、timestamp](#23timestamp)
    - [2.4、time](#24time)
    - [2.5、year](#25year)
    - [2.6、如何选择日期类型](#26%E5%A6%82%E4%BD%95%E9%80%89%E6%8B%A9%E6%97%A5%E6%9C%9F%E7%B1%BB%E5%9E%8B)
  - [3、字符串类型-M表示允许的字符串长度](#3%E5%AD%97%E7%AC%A6%E4%B8%B2%E7%B1%BB%E5%9E%8B-m%E8%A1%A8%E7%A4%BA%E5%85%81%E8%AE%B8%E7%9A%84%E5%AD%97%E7%AC%A6%E4%B8%B2%E9%95%BF%E5%BA%A6)
  - [4、列类型的选择](#4%E5%88%97%E7%B1%BB%E5%9E%8B%E7%9A%84%E9%80%89%E6%8B%A9)
- [四、列属性](#%E5%9B%9B%E5%88%97%E5%B1%9E%E6%80%A7)
- [五、查询 SQL 执行顺序](#%E4%BA%94%E6%9F%A5%E8%AF%A2-sql-%E6%89%A7%E8%A1%8C%E9%A1%BA%E5%BA%8F)
  - [1、一般SQL的写的顺序](#1%E4%B8%80%E8%88%ACsql%E7%9A%84%E5%86%99%E7%9A%84%E9%A1%BA%E5%BA%8F)
  - [2、数据执行的顺序：前面括号的数据表示执行顺序](#2%E6%95%B0%E6%8D%AE%E6%89%A7%E8%A1%8C%E7%9A%84%E9%A1%BA%E5%BA%8F%E5%89%8D%E9%9D%A2%E6%8B%AC%E5%8F%B7%E7%9A%84%E6%95%B0%E6%8D%AE%E8%A1%A8%E7%A4%BA%E6%89%A7%E8%A1%8C%E9%A1%BA%E5%BA%8F)
  - [3、SQL性能下降的原因](#3sql%E6%80%A7%E8%83%BD%E4%B8%8B%E9%99%8D%E7%9A%84%E5%8E%9F%E5%9B%A0)
- [六、高级查询](#%E5%85%AD%E9%AB%98%E7%BA%A7%E6%9F%A5%E8%AF%A2)
  - [1、连接](#1%E8%BF%9E%E6%8E%A5)
    - [1.1、内连接](#11%E5%86%85%E8%BF%9E%E6%8E%A5)
    - [1.2、左外连接](#12%E5%B7%A6%E5%A4%96%E8%BF%9E%E6%8E%A5)
    - [1.3、左连接](#13%E5%B7%A6%E8%BF%9E%E6%8E%A5)
    - [1.4、右外连接](#14%E5%8F%B3%E5%A4%96%E8%BF%9E%E6%8E%A5)
    - [1.5、右外连接](#15%E5%8F%B3%E5%A4%96%E8%BF%9E%E6%8E%A5)
    - [1.6、全连接](#16%E5%85%A8%E8%BF%9E%E6%8E%A5)
    - [1.7、全连接（去除共有数据）](#17%E5%85%A8%E8%BF%9E%E6%8E%A5%E5%8E%BB%E9%99%A4%E5%85%B1%E6%9C%89%E6%95%B0%E6%8D%AE)
    - [1.8、union 和 union all：联合查询](#18union-%E5%92%8C-union-all%E8%81%94%E5%90%88%E6%9F%A5%E8%AF%A2)
  - [2、连接使用注意事项](#2%E8%BF%9E%E6%8E%A5%E4%BD%BF%E7%94%A8%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9)
  - [3、group by...having](#3group-byhaving)
  - [4、行转列与列转行](#4%E8%A1%8C%E8%BD%AC%E5%88%97%E4%B8%8E%E5%88%97%E8%BD%AC%E8%A1%8C)
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
  - [6、InnoDB详述](#6innodb%E8%AF%A6%E8%BF%B0)
    - [6.1、存储方式](#61%E5%AD%98%E5%82%A8%E6%96%B9%E5%BC%8F)
- [八、高级特性](#%E5%85%AB%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7)
  - [1、数据库隔离级别介绍](#1%E6%95%B0%E6%8D%AE%E5%BA%93%E9%9A%94%E7%A6%BB%E7%BA%A7%E5%88%AB%E4%BB%8B%E7%BB%8D)
    - [1.1、不考虑隔离性发生的问题](#11%E4%B8%8D%E8%80%83%E8%99%91%E9%9A%94%E7%A6%BB%E6%80%A7%E5%8F%91%E7%94%9F%E7%9A%84%E9%97%AE%E9%A2%98)
    - [1.2、事务隔离级别](#12%E4%BA%8B%E5%8A%A1%E9%9A%94%E7%A6%BB%E7%BA%A7%E5%88%AB)
    - [1.3、数据库默认隔离级别](#13%E6%95%B0%E6%8D%AE%E5%BA%93%E9%BB%98%E8%AE%A4%E9%9A%94%E7%A6%BB%E7%BA%A7%E5%88%AB)
    - [1.4、RC、RR级别下InnoDB的非阻塞读](#14rcrr%E7%BA%A7%E5%88%AB%E4%B8%8Binnodb%E7%9A%84%E9%9D%9E%E9%98%BB%E5%A1%9E%E8%AF%BB)
    - [1.5、RR可重复读如何避免幻读](#15rr%E5%8F%AF%E9%87%8D%E5%A4%8D%E8%AF%BB%E5%A6%82%E4%BD%95%E9%81%BF%E5%85%8D%E5%B9%BB%E8%AF%BB)
  - [2、事务特性](#2%E4%BA%8B%E5%8A%A1%E7%89%B9%E6%80%A7)
- [九、数据库锁](#%E4%B9%9D%E6%95%B0%E6%8D%AE%E5%BA%93%E9%94%81)
  - [1、锁](#1%E9%94%81)
  - [2、锁的分类](#2%E9%94%81%E7%9A%84%E5%88%86%E7%B1%BB)
    - [2.1、按对数据库的操作类型分](#21%E6%8C%89%E5%AF%B9%E6%95%B0%E6%8D%AE%E5%BA%93%E7%9A%84%E6%93%8D%E4%BD%9C%E7%B1%BB%E5%9E%8B%E5%88%86)
    - [2.2.从对数据操作的粒度分](#22%E4%BB%8E%E5%AF%B9%E6%95%B0%E6%8D%AE%E6%93%8D%E4%BD%9C%E7%9A%84%E7%B2%92%E5%BA%A6%E5%88%86)
  - [3、表锁（偏读）](#3%E8%A1%A8%E9%94%81%E5%81%8F%E8%AF%BB)
  - [4、行锁](#4%E8%A1%8C%E9%94%81)
  - [5、死锁](#5%E6%AD%BB%E9%94%81)
    - [5.1、什么是死锁](#51%E4%BB%80%E4%B9%88%E6%98%AF%E6%AD%BB%E9%94%81)
    - [5.2、死锁产生原因](#52%E6%AD%BB%E9%94%81%E4%BA%A7%E7%94%9F%E5%8E%9F%E5%9B%A0)
    - [5.3、死锁处理策略](#53%E6%AD%BB%E9%94%81%E5%A4%84%E7%90%86%E7%AD%96%E7%95%A5)
    - [5.4、死锁案例](#54%E6%AD%BB%E9%94%81%E6%A1%88%E4%BE%8B)
    - [5.5、避免死锁](#55%E9%81%BF%E5%85%8D%E6%AD%BB%E9%94%81)
    - [5.6、定位死锁问题](#56%E5%AE%9A%E4%BD%8D%E6%AD%BB%E9%94%81%E9%97%AE%E9%A2%98)
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
- [十四、数据库编程](#%E5%8D%81%E5%9B%9B%E6%95%B0%E6%8D%AE%E5%BA%93%E7%BC%96%E7%A8%8B)
  - [1、存储过程](#1%E5%AD%98%E5%82%A8%E8%BF%87%E7%A8%8B)
  - [2、函数](#2%E5%87%BD%E6%95%B0)
  - [3、触发器](#3%E8%A7%A6%E5%8F%91%E5%99%A8)
  - [4、为什么放弃数据库编程](#4%E4%B8%BA%E4%BB%80%E4%B9%88%E6%94%BE%E5%BC%83%E6%95%B0%E6%8D%AE%E5%BA%93%E7%BC%96%E7%A8%8B)
- [十五、Mysql压力测试](#%E5%8D%81%E4%BA%94mysql%E5%8E%8B%E5%8A%9B%E6%B5%8B%E8%AF%95)
  - [1、主要关注指标](#1%E4%B8%BB%E8%A6%81%E5%85%B3%E6%B3%A8%E6%8C%87%E6%A0%87)
  - [2、压测工具](#2%E5%8E%8B%E6%B5%8B%E5%B7%A5%E5%85%B7)
  - [3、sysbench](#3sysbench)
    - [3.1、安装](#31%E5%AE%89%E8%A3%85)
    - [3.2、sysbench基本语法](#32sysbench%E5%9F%BA%E6%9C%AC%E8%AF%AD%E6%B3%95)
    - [3.3、开始测试](#33%E5%BC%80%E5%A7%8B%E6%B5%8B%E8%AF%95)
- [十六、MySql索引](#%E5%8D%81%E5%85%ADmysql%E7%B4%A2%E5%BC%95)
  - [1、索引](#1%E7%B4%A2%E5%BC%95)
  - [2、索引分类](#2%E7%B4%A2%E5%BC%95%E5%88%86%E7%B1%BB)
  - [3、索引的语法](#3%E7%B4%A2%E5%BC%95%E7%9A%84%E8%AF%AD%E6%B3%95)
  - [4、索引的结构](#4%E7%B4%A2%E5%BC%95%E7%9A%84%E7%BB%93%E6%9E%84)
  - [5、哪些情况需要创建索引](#5%E5%93%AA%E4%BA%9B%E6%83%85%E5%86%B5%E9%9C%80%E8%A6%81%E5%88%9B%E5%BB%BA%E7%B4%A2%E5%BC%95)
  - [6、哪些情况不需要创建索引](#6%E5%93%AA%E4%BA%9B%E6%83%85%E5%86%B5%E4%B8%8D%E9%9C%80%E8%A6%81%E5%88%9B%E5%BB%BA%E7%B4%A2%E5%BC%95)
  - [7、索引失效](#7%E7%B4%A2%E5%BC%95%E5%A4%B1%E6%95%88)
  - [8、索引建立的原则](#8%E7%B4%A2%E5%BC%95%E5%BB%BA%E7%AB%8B%E7%9A%84%E5%8E%9F%E5%88%99)
  - [9、索引的结构及底层实现](#9%E7%B4%A2%E5%BC%95%E7%9A%84%E7%BB%93%E6%9E%84%E5%8F%8A%E5%BA%95%E5%B1%82%E5%AE%9E%E7%8E%B0)
    - [9.1、索引的类型](#91%E7%B4%A2%E5%BC%95%E7%9A%84%E7%B1%BB%E5%9E%8B)
    - [9.2、B-树(B树)](#92b-%E6%A0%91b%E6%A0%91)
    - [9.3、B+树-基于B-树的一种变体](#93b%E6%A0%91-%E5%9F%BA%E4%BA%8Eb-%E6%A0%91%E7%9A%84%E4%B8%80%E7%A7%8D%E5%8F%98%E4%BD%93)
    - [9.4、稀疏索引与密集索引（聚簇索引）](#94%E7%A8%80%E7%96%8F%E7%B4%A2%E5%BC%95%E4%B8%8E%E5%AF%86%E9%9B%86%E7%B4%A2%E5%BC%95%E8%81%9A%E7%B0%87%E7%B4%A2%E5%BC%95)
    - [9.5、各引擎支持的索引](#95%E5%90%84%E5%BC%95%E6%93%8E%E6%94%AF%E6%8C%81%E7%9A%84%E7%B4%A2%E5%BC%95)
  - [10、聚簇索引与二级索引](#10%E8%81%9A%E7%B0%87%E7%B4%A2%E5%BC%95%E4%B8%8E%E4%BA%8C%E7%BA%A7%E7%B4%A2%E5%BC%95)
  - [11、主键索引、普通索引、覆盖索引等](#11%E4%B8%BB%E9%94%AE%E7%B4%A2%E5%BC%95%E6%99%AE%E9%80%9A%E7%B4%A2%E5%BC%95%E8%A6%86%E7%9B%96%E7%B4%A2%E5%BC%95%E7%AD%89)
    - [11.1、主键索引和普通索引区别](#111%E4%B8%BB%E9%94%AE%E7%B4%A2%E5%BC%95%E5%92%8C%E6%99%AE%E9%80%9A%E7%B4%A2%E5%BC%95%E5%8C%BA%E5%88%AB)
    - [11.2、覆盖索引](#112%E8%A6%86%E7%9B%96%E7%B4%A2%E5%BC%95)
  - [12、如何创建索引](#12%E5%A6%82%E4%BD%95%E5%88%9B%E5%BB%BA%E7%B4%A2%E5%BC%95)
    - [12.1、字符串字段如何创建索引](#121%E5%AD%97%E7%AC%A6%E4%B8%B2%E5%AD%97%E6%AE%B5%E5%A6%82%E4%BD%95%E5%88%9B%E5%BB%BA%E7%B4%A2%E5%BC%95)
- [十七、数据库优化](#%E5%8D%81%E4%B8%83%E6%95%B0%E6%8D%AE%E5%BA%93%E4%BC%98%E5%8C%96)
  - [1、数据库优化的目的](#1%E6%95%B0%E6%8D%AE%E5%BA%93%E4%BC%98%E5%8C%96%E7%9A%84%E7%9B%AE%E7%9A%84)
  - [2、影响 mysql 性能的因素](#2%E5%BD%B1%E5%93%8D-mysql-%E6%80%A7%E8%83%BD%E7%9A%84%E5%9B%A0%E7%B4%A0)
    - [2.1、一条SQL语句执行得很慢的原因](#21%E4%B8%80%E6%9D%A1sql%E8%AF%AD%E5%8F%A5%E6%89%A7%E8%A1%8C%E5%BE%97%E5%BE%88%E6%85%A2%E7%9A%84%E5%8E%9F%E5%9B%A0)
  - [3、MySQL 性能优化](#3mysql-%E6%80%A7%E8%83%BD%E4%BC%98%E5%8C%96)
  - [4、慢查询日志包含的内容](#4%E6%85%A2%E6%9F%A5%E8%AF%A2%E6%97%A5%E5%BF%97%E5%8C%85%E5%90%AB%E7%9A%84%E5%86%85%E5%AE%B9)
  - [5、慢查询日志分析查看](#5%E6%85%A2%E6%9F%A5%E8%AF%A2%E6%97%A5%E5%BF%97%E5%88%86%E6%9E%90%E6%9F%A5%E7%9C%8B)
  - [6、分析SQL查询](#6%E5%88%86%E6%9E%90sql%E6%9F%A5%E8%AF%A2)
    - [6.1、id：select的查询序列号，包含一组数字，表示在查询中执行 select 子句或者操作表的顺序](#61idselect%E7%9A%84%E6%9F%A5%E8%AF%A2%E5%BA%8F%E5%88%97%E5%8F%B7%E5%8C%85%E5%90%AB%E4%B8%80%E7%BB%84%E6%95%B0%E5%AD%97%E8%A1%A8%E7%A4%BA%E5%9C%A8%E6%9F%A5%E8%AF%A2%E4%B8%AD%E6%89%A7%E8%A1%8C-select-%E5%AD%90%E5%8F%A5%E6%88%96%E8%80%85%E6%93%8D%E4%BD%9C%E8%A1%A8%E7%9A%84%E9%A1%BA%E5%BA%8F)
    - [6.2、select_type：查询类型，主要用于区别普通查询，联合查询，子查询等复杂查询，主要有以下值](#62select_type%E6%9F%A5%E8%AF%A2%E7%B1%BB%E5%9E%8B%E4%B8%BB%E8%A6%81%E7%94%A8%E4%BA%8E%E5%8C%BA%E5%88%AB%E6%99%AE%E9%80%9A%E6%9F%A5%E8%AF%A2%E8%81%94%E5%90%88%E6%9F%A5%E8%AF%A2%E5%AD%90%E6%9F%A5%E8%AF%A2%E7%AD%89%E5%A4%8D%E6%9D%82%E6%9F%A5%E8%AF%A2%E4%B8%BB%E8%A6%81%E6%9C%89%E4%BB%A5%E4%B8%8B%E5%80%BC)
    - [6.3、type：显示查询使用了何种类型，主要有：all， index， range， ref， eq_ref， const， system](#63type%E6%98%BE%E7%A4%BA%E6%9F%A5%E8%AF%A2%E4%BD%BF%E7%94%A8%E4%BA%86%E4%BD%95%E7%A7%8D%E7%B1%BB%E5%9E%8B%E4%B8%BB%E8%A6%81%E6%9C%89all-index-range-ref-eq_ref-const-system)
    - [6.4、possible_keys、key、key_len](#64possible_keyskeykey_len)
    - [6.5、ref：显示索引的哪一列被使用了，如果可能的话是一个常数.哪些列或常量被用于查找索引列上的值.](#65ref%E6%98%BE%E7%A4%BA%E7%B4%A2%E5%BC%95%E7%9A%84%E5%93%AA%E4%B8%80%E5%88%97%E8%A2%AB%E4%BD%BF%E7%94%A8%E4%BA%86%E5%A6%82%E6%9E%9C%E5%8F%AF%E8%83%BD%E7%9A%84%E8%AF%9D%E6%98%AF%E4%B8%80%E4%B8%AA%E5%B8%B8%E6%95%B0%E5%93%AA%E4%BA%9B%E5%88%97%E6%88%96%E5%B8%B8%E9%87%8F%E8%A2%AB%E7%94%A8%E4%BA%8E%E6%9F%A5%E6%89%BE%E7%B4%A2%E5%BC%95%E5%88%97%E4%B8%8A%E7%9A%84%E5%80%BC)
    - [6.6、rows：根据表统计信息及索引选用的情况，大致估算出找到所需记录所需要读取的行数，即有多少条记录被优化器所查询](#66rows%E6%A0%B9%E6%8D%AE%E8%A1%A8%E7%BB%9F%E8%AE%A1%E4%BF%A1%E6%81%AF%E5%8F%8A%E7%B4%A2%E5%BC%95%E9%80%89%E7%94%A8%E7%9A%84%E6%83%85%E5%86%B5%E5%A4%A7%E8%87%B4%E4%BC%B0%E7%AE%97%E5%87%BA%E6%89%BE%E5%88%B0%E6%89%80%E9%9C%80%E8%AE%B0%E5%BD%95%E6%89%80%E9%9C%80%E8%A6%81%E8%AF%BB%E5%8F%96%E7%9A%84%E8%A1%8C%E6%95%B0%E5%8D%B3%E6%9C%89%E5%A4%9A%E5%B0%91%E6%9D%A1%E8%AE%B0%E5%BD%95%E8%A2%AB%E4%BC%98%E5%8C%96%E5%99%A8%E6%89%80%E6%9F%A5%E8%AF%A2)
    - [6.7、Extra：包含不适合在其他列显示但非常重要的额外信息](#67extra%E5%8C%85%E5%90%AB%E4%B8%8D%E9%80%82%E5%90%88%E5%9C%A8%E5%85%B6%E4%BB%96%E5%88%97%E6%98%BE%E7%A4%BA%E4%BD%86%E9%9D%9E%E5%B8%B8%E9%87%8D%E8%A6%81%E7%9A%84%E9%A2%9D%E5%A4%96%E4%BF%A1%E6%81%AF)
  - [7、索引优化](#7%E7%B4%A2%E5%BC%95%E4%BC%98%E5%8C%96)
    - [7.1、单表优化：查询 category_id 为1 且 comments > 1 的情况下，views 最多的 article_id**](#71%E5%8D%95%E8%A1%A8%E4%BC%98%E5%8C%96%E6%9F%A5%E8%AF%A2-category_id-%E4%B8%BA1-%E4%B8%94-comments--1-%E7%9A%84%E6%83%85%E5%86%B5%E4%B8%8Bviews-%E6%9C%80%E5%A4%9A%E7%9A%84-article_id)
    - [7.2、两表优化](#72%E4%B8%A4%E8%A1%A8%E4%BC%98%E5%8C%96)
    - [7.3、三表查询](#73%E4%B8%89%E8%A1%A8%E6%9F%A5%E8%AF%A2)
    - [7.4、索引失效](#74%E7%B4%A2%E5%BC%95%E5%A4%B1%E6%95%88)
  - [8、查询分析](#8%E6%9F%A5%E8%AF%A2%E5%88%86%E6%9E%90)
    - [8.1、如何进行优化](#81%E5%A6%82%E4%BD%95%E8%BF%9B%E8%A1%8C%E4%BC%98%E5%8C%96)
    - [8.2、查询优化](#82%E6%9F%A5%E8%AF%A2%E4%BC%98%E5%8C%96)
    - [8.3、慢查询日志](#83%E6%85%A2%E6%9F%A5%E8%AF%A2%E6%97%A5%E5%BF%97)
    - [8.4、Show Profile](#84show-profile)
  - [9、count和max优化](#9count%E5%92%8Cmax%E4%BC%98%E5%8C%96)
    - [9.1、max 优化](#91max-%E4%BC%98%E5%8C%96)
    - [9.2、count的优化](#92count%E7%9A%84%E4%BC%98%E5%8C%96)
  - [10、limit 优化](#10limit-%E4%BC%98%E5%8C%96)
  - [11、索引优化](#11%E7%B4%A2%E5%BC%95%E4%BC%98%E5%8C%96)
    - [11.1、如何选择合适的列建立索引](#111%E5%A6%82%E4%BD%95%E9%80%89%E6%8B%A9%E5%90%88%E9%80%82%E7%9A%84%E5%88%97%E5%BB%BA%E7%AB%8B%E7%B4%A2%E5%BC%95)
    - [11.2、重复及冗余索引](#112%E9%87%8D%E5%A4%8D%E5%8F%8A%E5%86%97%E4%BD%99%E7%B4%A2%E5%BC%95)
    - [11.3、索引维护：业务变更或表变更需要对索引进行调整或将不使用的索引删除](#113%E7%B4%A2%E5%BC%95%E7%BB%B4%E6%8A%A4%E4%B8%9A%E5%8A%A1%E5%8F%98%E6%9B%B4%E6%88%96%E8%A1%A8%E5%8F%98%E6%9B%B4%E9%9C%80%E8%A6%81%E5%AF%B9%E7%B4%A2%E5%BC%95%E8%BF%9B%E8%A1%8C%E8%B0%83%E6%95%B4%E6%88%96%E5%B0%86%E4%B8%8D%E4%BD%BF%E7%94%A8%E7%9A%84%E7%B4%A2%E5%BC%95%E5%88%A0%E9%99%A4)
  - [12、表的优化](#12%E8%A1%A8%E7%9A%84%E4%BC%98%E5%8C%96)
    - [12.1、选择合适的数据类型](#121%E9%80%89%E6%8B%A9%E5%90%88%E9%80%82%E7%9A%84%E6%95%B0%E6%8D%AE%E7%B1%BB%E5%9E%8B)
    - [12.2、表的范式化和反范式化](#122%E8%A1%A8%E7%9A%84%E8%8C%83%E5%BC%8F%E5%8C%96%E5%92%8C%E5%8F%8D%E8%8C%83%E5%BC%8F%E5%8C%96)
    - [12.3、垂直拆分](#123%E5%9E%82%E7%9B%B4%E6%8B%86%E5%88%86)
    - [12.4、水平拆分](#124%E6%B0%B4%E5%B9%B3%E6%8B%86%E5%88%86)
  - [13、系统的优化](#13%E7%B3%BB%E7%BB%9F%E7%9A%84%E4%BC%98%E5%8C%96)
    - [13.1、操作系统配置优化](#131%E6%93%8D%E4%BD%9C%E7%B3%BB%E7%BB%9F%E9%85%8D%E7%BD%AE%E4%BC%98%E5%8C%96)
    - [13.2、MySQL配置优化](#132mysql%E9%85%8D%E7%BD%AE%E4%BC%98%E5%8C%96)
    - [13.3、第三方配置优化](#133%E7%AC%AC%E4%B8%89%E6%96%B9%E9%85%8D%E7%BD%AE%E4%BC%98%E5%8C%96)
  - [14、批量插入大量数据](#14%E6%89%B9%E9%87%8F%E6%8F%92%E5%85%A5%E5%A4%A7%E9%87%8F%E6%95%B0%E6%8D%AE)
- [十八、MySQL常用sql](#%E5%8D%81%E5%85%ABmysql%E5%B8%B8%E7%94%A8sql)
  - [1、根据A表来更新B表的数据](#1%E6%A0%B9%E6%8D%AEa%E8%A1%A8%E6%9D%A5%E6%9B%B4%E6%96%B0b%E8%A1%A8%E7%9A%84%E6%95%B0%E6%8D%AE)
  - [2、mysql加密解密](#2mysql%E5%8A%A0%E5%AF%86%E8%A7%A3%E5%AF%86)
  - [3、批量插入记录时忽略错误的记录](#3%E6%89%B9%E9%87%8F%E6%8F%92%E5%85%A5%E8%AE%B0%E5%BD%95%E6%97%B6%E5%BF%BD%E7%95%A5%E9%94%99%E8%AF%AF%E7%9A%84%E8%AE%B0%E5%BD%95)
  - [4、插入时存在就更新](#4%E6%8F%92%E5%85%A5%E6%97%B6%E5%AD%98%E5%9C%A8%E5%B0%B1%E6%9B%B4%E6%96%B0)
  - [5、使用from语句替代子查询](#5%E4%BD%BF%E7%94%A8from%E8%AF%AD%E5%8F%A5%E6%9B%BF%E4%BB%A3%E5%AD%90%E6%9F%A5%E8%AF%A2)
  - [6、对字段值进行替换](#6%E5%AF%B9%E5%AD%97%E6%AE%B5%E5%80%BC%E8%BF%9B%E8%A1%8C%E6%9B%BF%E6%8D%A2)
- [十九、MySQL面试题](#%E5%8D%81%E4%B9%9Dmysql%E9%9D%A2%E8%AF%95%E9%A2%98)
  - [1、MySQL自增主键问题](#1mysql%E8%87%AA%E5%A2%9E%E4%B8%BB%E9%94%AE%E9%97%AE%E9%A2%98)
    - [1.1、数据库重启对自增主键的影响](#11%E6%95%B0%E6%8D%AE%E5%BA%93%E9%87%8D%E5%90%AF%E5%AF%B9%E8%87%AA%E5%A2%9E%E4%B8%BB%E9%94%AE%E7%9A%84%E5%BD%B1%E5%93%8D)
    - [1.2、插入语句对自增主键的影响](#12%E6%8F%92%E5%85%A5%E8%AF%AD%E5%8F%A5%E5%AF%B9%E8%87%AA%E5%A2%9E%E4%B8%BB%E9%94%AE%E7%9A%84%E5%BD%B1%E5%93%8D)
    - [1.3、为什么用自增列作为主键](#13%E4%B8%BA%E4%BB%80%E4%B9%88%E7%94%A8%E8%87%AA%E5%A2%9E%E5%88%97%E4%BD%9C%E4%B8%BA%E4%B8%BB%E9%94%AE)
    - [1.4、主键的使用](#14%E4%B8%BB%E9%94%AE%E7%9A%84%E4%BD%BF%E7%94%A8)
  - [2、Mysql唯一键问题](#2mysql%E5%94%AF%E4%B8%80%E9%94%AE%E9%97%AE%E9%A2%98)
  - [3、分页查询优化](#3%E5%88%86%E9%A1%B5%E6%9F%A5%E8%AF%A2%E4%BC%98%E5%8C%96)
    - [3.1、一般分页查询](#31%E4%B8%80%E8%88%AC%E5%88%86%E9%A1%B5%E6%9F%A5%E8%AF%A2)
    - [3.2、分页查询优化方案](#32%E5%88%86%E9%A1%B5%E6%9F%A5%E8%AF%A2%E4%BC%98%E5%8C%96%E6%96%B9%E6%A1%88)
  - [4、在线修改数据库结构](#4%E5%9C%A8%E7%BA%BF%E4%BF%AE%E6%94%B9%E6%95%B0%E6%8D%AE%E5%BA%93%E7%BB%93%E6%9E%84)
  - [5、物理删除与逻辑删除](#5%E7%89%A9%E7%90%86%E5%88%A0%E9%99%A4%E4%B8%8E%E9%80%BB%E8%BE%91%E5%88%A0%E9%99%A4)
    - [5.1、物理删除](#51%E7%89%A9%E7%90%86%E5%88%A0%E9%99%A4)
    - [5.2、逻辑删除](#52%E9%80%BB%E8%BE%91%E5%88%A0%E9%99%A4)
  - [6、读多写少与写多读少](#6%E8%AF%BB%E5%A4%9A%E5%86%99%E5%B0%91%E4%B8%8E%E5%86%99%E5%A4%9A%E8%AF%BB%E5%B0%91)
    - [6.1、读多写少](#61%E8%AF%BB%E5%A4%9A%E5%86%99%E5%B0%91)
    - [6.2、写多读少](#62%E5%86%99%E5%A4%9A%E8%AF%BB%E5%B0%91)
    - [6.3、写多读多业务场景](#63%E5%86%99%E5%A4%9A%E8%AF%BB%E5%A4%9A%E4%B8%9A%E5%8A%A1%E5%9C%BA%E6%99%AF)
  - [7、如何让MySQL的MyIsam引擎支持事务](#7%E5%A6%82%E4%BD%95%E8%AE%A9mysql%E7%9A%84myisam%E5%BC%95%E6%93%8E%E6%94%AF%E6%8C%81%E4%BA%8B%E5%8A%A1)
- [参考文章](#%E5%8F%82%E8%80%83%E6%96%87%E7%AB%A0)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 一、Mysql概述

基于C/S架构

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
	- ORACLE没有自动增长的数据类型，需要建立一个自动增长的序列号，插入记录时要把序列号的下一个值赋于此字段。CREATE SEQUENCE 序列号的名称 (最好是表名+序列号标记) 	`INCREMENT BY 1 START WITH 1 MAXVALUE 99999 CYCLE NOCACHE;`其中最大的值按字段的长度来定, 如果定义的自动增长的序列号 NUMBER(6)，最大值为999999；INSERT 语句插入这个字段值为: `序列号的名称.NEXTVAL`；

- 分页的处理
	- MYSQL处理翻页的SQL语句比较简单，用 LIMIT 开始位置, 记录个数；
	- ORACLE处理翻页的SQL语句就比较繁琐了。每个结果集只有一个ROWNUM字段标明它的位置, 并且只能用ROWNUM<100, 不能用ROWNUM>80

- 单引号的处理：MYSQL里可以用双引号包起字符串，ORACLE里只可以用单引号包起字符串。在插入和修改字符串前必须做单引号的替换：把所有出现的一个单引号替换成两个单引号；

- 长字符串的处理：

- 日期的处理：MYSQL日期字段分DATE和TIME、时间戳等多种，ORACLE日期字段只有DATE，包含年月日时分秒信息，用当前数据库的系统时间为SYSDATE, 精确到秒，或者用字符串转换成日期型函数；

- 空字符的处理：MYSQL的非空字段也有空的内容，ORACLE里定义了非空字段就不容许有空的内容。按MYSQL的NOT NULL来定义ORACLE表结构, 导数据的时候会产生错误。因此导数据时要对空字符进行判断，如果为NULL或空字符，需要把它改成一个空格的字符串；

- 字符串的模糊比较： MYSQL里用 字段名 like '%字符串%',ORACLE里也可以用 字段名 like '%字符串%' 但这种方法不能使用索引, 速度不快，用字符串比较函数 instr(字段名,'字符串')>0 会得到更精确的查找结果。

## 5、MySQL日志

### 5.1、mysql日志分类

mysql日志主要分为以下：
- 错误日志（error_log）：记录mysql在启动、运行或停止时出现的问题；
- 常规日志（general_log）：记录所有发现mysql的请求；
- 慢查询日志（slow_query_log）：记录符合慢查询条件的日志；
- 二进制日志（binary_log）：记录全部有效的数据修改日志；
- 中继日志（raly_log）：用于主从复制，临时存储从主库同步的二进制日志；

# 二、MySQL操作

## 1、连接命令行
```
mysql -hlocalhost -P3306 -uroot -p	--(-h：主机host， -P：端口，-u用户，-p：密码)； 
--select user()；查看当前用户
```

查看命令行帮助文档，进入到mysql控制台：`help create user;`，查看 create user 帮助

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
- delete语句不影响表所占用的extent，高水线(high watermark)保持原位置不动；drop语句将表所占用的空间全部释放。truncate 语句缺省情况下见空间释放到 minextents个 extent，除非使用reuse storage; truncate会将高水线复位(回到最开始)；
- 执行速度：`drop> truncate > delete`；
- 使用建议：
	- drop：完全删除表；
	- truncate：想保留表而将所有数据删除。如果和事务无关；
	- delete：如果和事务有关，或者想触发；删除部分数据；可以返回被删除的记录数；

#### 6.3.3、Mysql高水位问题

- 产生原因：数据库中有些表使用delete删除了一些行后，发现空间并未释放；
	- delete 不会释放文件高水位 
	- truncate会释放 ，实际是把`.ibd`文件删掉了，再建一个。
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

### 2.1、datetime

年月日时分秒，显示格式：`YYYY-MM-DD HH:MM:SS`；8个字节，与时区无关
- 存储范围：1000.1.1 00：00：00~9999.12.31 23：59：59，
- 支持任意分隔符的日期，所表示的时间符合范围；但是如果出现歧义，所有不建议使用特殊分隔符；
- 支持两位年份，不建议使用<br>
	70~69		1970-2069<br>
	70~99		19**<br>
	0~69		20**<br>

### 2.2、date

年月日，跟datetime差不多；3个字节

使用date类型可以利用日期时间函数进行日期之间的计算

### 2.3、timestamp

时间戳，存储时是整型，表示时是日期时间，格式`YYYY-MM-DD HH:MM:S`S，4个字节

- 存储范围：1970.1.1 00:00:00~2038.1.19 03:14:07
- 检索列时，+0可以检索到时间戳
- 支持0值：表示当前是没有规定的，如2013-04-0表示4月整个月；
- 依赖于所指定的时区；
- 在行的数据修改时可以自动修改timestamp列的值

### 2.4、time

3个字节，范围：-838：59：59 ~838：59：59

表示一天中的时间或时间间隔，在表示时间间隔时可以使用天来表示，格式：D HH:MM:SS

### 2.5、year

1个字节，1901~2155

### 2.6、如何选择日期类型

- 不要使用字符类型来存储日期时间数据；
	* 日期时间类型通常比字符串占用的字符串存储空间小；
	* 日期类型在进行查找过滤时可以利用日期来进行对比；
	* 日期时间类型有着丰富的处理函数，可以方便对日期类型进行日期计算
- 使用int存储日期时间，使用SQL函数unix_timestamp()和from_unixtime()来进行转换；

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
- TINYBLOB：0-255字节，不超过 255 个字符的二进制字符串
- TINYTEXT：0-255字节，短文本字符串
- BLOB：0-65535字节，二进制形式的长文本数据
- TEXT：0-65535字节，长文本数据
- MEDIUMBLOB：0-16777215字节，二进制形式的中等长度文本数据
- MEDIUMTEXT：0-16777215字节，中等长度文本数据
- LONGBLOB：0-4294967295字节，二进制形式的极大文本数据
- LONGTEXT：0-4294967295字节，极大文本数据

## 4、列类型的选择

当一个列可以选择多种数据类型时，应该优先考虑数字类型，其次是日期或二进制类型，最后是字符类型

- 应该使用最精确的类型，占用的空间少
- 考虑应用程序语言的处理；
- 考虑移植兼容性；

## 5、列的属性

**1、是否为空：规定一个字段的值是否可以为null，设置字段值不为空not null；**

*注意：字段属性最好不用 null：*

- 所有使用 NULL 值的情况，都可以通过一个有意义的值的表示，这样有利于代码的可读性和可维护性，并能从约束上增强业务数据的规范性。
- NULL 值到非 NULL 值的更新无法做到原地更新，容易发生索引分裂从而影响性能； 但 NULL列改为NOT NULL 来的性能提示很小，除非确定它带来了问题，否则不要把它当成优先的优化措施，最重要的是使用的列的类型的适当性
- NULL 值在 timestamp 类型下容易出问题，特别是没有启用参数 explicit_defaults_for_timestamp；
- NOT IN， != 等负向条件查询在有 NULL 值的情况下返回永远为空结果，查询容易出错：
	- NOT IN 子查询在有 NULL 值的情况下返回永远为空结果，查询容易出错；
	- 单列索引不存 NULL 值，复合索引不存全为null的值，如果列允许为null，可能会得到"不符合预期"的结果集，如果name允许为null，索引不存储null值，结果集中不会包含这些记录。所以，请使用 not NULL 约束以及默认值；
	- 如果在两个字段进行拼接：比如题号+分数，首先要各字段进行非 NULL 判断，否则只要任意一个字段为空都会造成拼接的结果为 null；
	- 如果有 NULL column 存在的情况下，count(NULL column)需要格外注意， NULL 值不会参与统计
	- 注意 NULL 字段的判断方式， column = NULL 将会得到错误的结果；
- Null 列需要更多的存储空间：需要一个额外字节作为判断是否为 NULL 的标志位

**2、默认值属性：default value，只有在没有给字段设值的时才会使用默认值；常跟not null搭配；**

**3、主键约束：primary key  ，可以唯一标识某条记录的字段或者是字段的集合；主键是跟业务逻辑无关的属性；**

设置主键：primary key( 列名) <br>
联合主键设置：primary key(列名1，列名2，...)；

**4、自动增长：auto_increment，为每条记录提供一个唯一标识**

	列名 primary key auto_increment

# 四、MySQL权限、配置

## 1、用户与用户权限

[账号管理](https://dev.mysql.com/doc/refman/8.0/en/account-management-statements.html)

### 1.1、创建、修改用户

**创建用户：**

创建账号一般格式：`create user 用户名@可访问控制列表' identified by '<password>' password <密码策略>`；

可访问控制列表可以包含如下格式：
- `%`：表示可以从所有外部主机访问；
- `192.168.1.%`：表示可以从192.168.1网段访问；
- `localhost`：DB服务器本地访问；

查询是否有某个用户：`select * from mysql.user where user='test'`

**修改用户密码：**

- 修改当前用户密码：`alter user user() identified by '<password>'`
- 使用root账号修改其他用户密码：`alter user <user@ip> identified by '<password>';`

### 1.2、常用用户权限

查看所有权限：show privileges；

**服务器管理权限：Admin**
- Create User：建立新的用户的权限；
- Grant Option：为其他用户授权的权限；
- Super：管理服务器的权限；

**DDL权限：**
- Create：新建数据库，表的权限；
- Alter：修改表结构的权限；
- Drop：删除数据库和表的权限；
- Index：建立和删除索引的权限

**DML权限：**
- Select：查询表中数据的权限；
- Insert：向表插入数据的权限；
- Update：更新表中数据的权限；
- Delete：删除表中数据的权限；
- Execute：执行存储过程的权限；

### 1.3、授权

遵循最小权限原则，使用grant命令对用户授权，格式：`grant <privileges> on database.table to user@ip;`
- privileges：即前面的用户权限；
- database.table：表示哪个数据库的哪张表；
- user@ip：创建的用户和访问的ip

使用revoke回收权限：`revoke <privileges> on database.table from user@ip;`

如果给用户与grant权限，使用 `with grant option`：`grant select on testdb.* to dba@localhost with grant option;`

### 1.4、保证数据库账号的安全

- 数据库用户管理流程规范；
	- 最小权限原则；
	- 密码强度策略；
	- 密码过期原则；
	- 现在历史密码重用原则；
- 密码管理策略

### 1.5、迁移数据库账号

思路：
数据库版本是否一致
- 是：备份mysql数据库，在目的实例恢复；
- 否：导出授权语句，在目的实例执行

导出用户建立及授权语句：`pt-show-grants u=root,p=123456,h=localhost`

### 1.6、关于用户其他常见SQL

- 查看当前登录用户：`SELECT CURRENT_USER();`

## 2、MySQL配置

比较配置文件，可以使用工具：pe-config-diff，格式：`pt-config-diff u=root,p=,h=localhost /etc/my.cnf`

在mysql控制台查看某个参数：`select @@<参数名称>`

### 2.1、SQL_MODE

主要配置mysql中处理sql的方式：`set [session/global/persit] sql_mode='';`

常用的 [SQL_MODE](https://dev.mysql.com/doc/refman/8.0/en/sql-mode.html) 值

### 2.2、mysql中关键性能参数

**服务器配置参数：**
- max_connections：设置mysql允许访问的最大连接数量；
- interactive_timeout：设置交互连接的timeout时间
- wait_timeout：设置非交互连接的timeout时间；
- max_allow_packet：控制mysql可以接收的数据包的大小；
- sync_binlog：表示每写多少缓冲会向磁盘同步一次binlog；
- sort_buffer_size：设置每个会话使用的排序缓存区的大小；
- join_buffer_size：设置每个会话所使用的连接缓冲的大小；
- read_buffer_size：指定了对一个myisam进行表扫描时锁分配的读缓存池的大小；
- read_rnd_buffer_size：设置控制索引缓冲区的大小；
- binlog_cache_size：设置每个会话用于缓存未提交的事务缓存大小；

**存储引擎参数：**
- innodb_flush_log_at_trx_commit：
	- 0：每秒进行一次重做日志的磁盘刷新操作；
	- 1：每次事务提交都会刷新事务日志到磁盘中；
	- 2：每次事务提交写入系统缓存每秒向磁盘刷新一次；
- innodb_buffer_pool_size：设置innodb缓冲池的大小，应为系统可用内存的75%；
- innodb_buffer_pool_instances：innodb缓冲池的实例个数，每个实例的大小为缓冲池大小/实例个数，一般为8个
- innodb_file_per_table：设置每个表独立使用一个表空间文件；

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

### 1.4、右连接

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

用于合并两个或多个 SELECT 语句的结果集，并消去表中任何重复行，union 内部的 SELECT 语句必须拥有相同数量的列，列也必须拥有相似的数据类型，每条 SELECT 语句中的列的顺序必须相同

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

如果子句中有 order by 或 limit， 需要用括号括起来，推荐放到所有子句之后，即对最终合并的结果来排序或筛选在子句中，order by 需要配合limit使用才有意义。如果不配合limit使用，会被语法分析器优化分析时去除
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

[行转列与列转行](https://www.cnblogs.com/xiaoxi/p/7151433.html)

### 4.1、行转列

![](image/数据行.png)

将上述数据按照下列各式数据：

![](image/数据行转列.png)

- **1、使用case...when....then 进行行转列**
	```sql
	SELECT user_name ,
		MAX(CASE course WHEN '数学' THEN score ELSE 0 END ) 数学,
		MAX(CASE course WHEN '语文' THEN score ELSE 0 END ) 语文,
		MAX(CASE course WHEN '英语' THEN score ELSE 0 END ) 英语
	FROM test_tb_grade
	GROUP BY user_name;
	```

- **2、使用IF() 进行行转列：**
	```sql
	SELECT userid,
	SUM(IF(`subject`='语文',score,0)) as '语文',
	SUM(IF(`subject`='数学',score,0)) as '数学',
	SUM(IF(`subject`='英语',score,0)) as '英语',
	SUM(IF(`subject`='政治',score,0)) as '政治' 
	FROM tb_score 
	GROUP BY userid
	```
	**注意点：**
	- SUM() 是为了能够使用GROUP BY根据userid进行分组，因为每一个userid对应的subject="语文"的记录只有一条，所以SUM() 的值就等于对应那一条记录的score的值。
	假如userid ='001' and subject='语文' 的记录有两条，则此时SUM() 的值将会是这两条记录的和，同理，使用Max()的值将会是这两条记录里面值最大的一个。但是正常情况下，一个user对应一个subject只有一个分数，因此可以使用SUM()、MAX()、MIN()、AVG()等聚合函数都可以达到行转列的效果。

	- `IF(subject='语文',score,0)` 作为条件，即对所有subject='语文'的记录的score字段进行SUM()、MAX()、MIN()、AVG()操作，如果score没有值则默认为0；

- **3、利用SUM(IF()) 生成列 + WITH ROLLUP 生成汇总行，并利用 IFNULL将汇总行标题显示为Total**
	```sql
	SELECT IFNULL(userid,'total') AS userid,
		SUM(IF(`subject`='语文',score,0)) AS 语文,
		SUM(IF(`subject`='数学',score,0)) AS 数学,
		SUM(IF(`subject`='英语',score,0)) AS 英语,
		SUM(IF(`subject`='政治',score,0)) AS 政治,
		SUM(IF(`subject`='total',score,0)) AS total
	FROM(
		SELECT userid,IFNULL(`subject`,'total') AS `subject`,SUM(score) AS score
		FROM tb_score
		GROUP BY userid,`subject`
		WITH ROLLUP
		HAVING userid IS NOT NULL
	)AS A 
	GROUP BY userid
	WITH ROLLUP;
	```

- **4、利用SUM(IF()) 生成列 + UNION 生成汇总行,并利用 IFNULL将汇总行标题显示为 Total**
	```sql
	SELECT userid,
	SUM(IF(`subject`='语文',score,0)) AS 语文,
	SUM(IF(`subject`='数学',score,0)) AS 数学,
	SUM(IF(`subject`='英语',score,0)) AS 英语,
	SUM(IF(`subject`='政治',score,0)) AS 政治,
	SUM(score) AS TOTAL 
	FROM tb_score
	GROUP BY userid
	UNION
	SELECT 'TOTAL',SUM(IF(`subject`='语文',score,0)) AS 语文,
	SUM(IF(`subject`='数学',score,0)) AS 数学,
	SUM(IF(`subject`='英语',score,0)) AS 英语,
	SUM(IF(`subject`='政治',score,0)) AS 政治,
	SUM(score) FROM tb_score
	```

- **5、利用SUM(IF()) 生成列，直接生成结果不再利用子查询**
	```sql
	SELECT IFNULL(userid,'TOTAL') AS userid,
	SUM(IF(`subject`='语文',score,0)) AS 语文,
	SUM(IF(`subject`='数学',score,0)) AS 数学,
	SUM(IF(`subject`='英语',score,0)) AS 英语,
	SUM(IF(`subject`='政治',score,0)) AS 政治,
	SUM(score) AS TOTAL 
	FROM tb_score
	GROUP BY userid WITH ROLLUP;
	```
- **6、动态，适用于列不确定情况**
	```sql
	SET @EE='';
	select @EE :=CONCAT(@EE,'sum(if(subject= \'',subject,'\',score,0)) as ',subject, ',') AS aa FROM (SELECT DISTINCT subject FROM tb_score) A ;

	SET @QQ = CONCAT('select ifnull(userid,\'TOTAL\')as userid,',@EE,' sum(score) as TOTAL from tb_score group by userid WITH ROLLUP');
	-- SELECT @QQ;

	PREPARE stmt FROM @QQ;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
	```
- **7、合并字段显示：利用group_concat()**
	```sql
	SELECT userid,GROUP_CONCAT(`subject`,":",score)AS 成绩 FROM tb_score
	GROUP BY userid
	```
	group_concat()函数可以很好的建属于同一分组的多个行转化为一个列

### 4.2、列转行

![](image/数据行2.png)

将上面的数据按照下列各式转换：

![](image/数据行2列转行.png)

列转行SQL：
```sql
select user_name, '语文' COURSE, CN_SCORE as SCORE from test_tb_grade2
union select user_name, '数学' COURSE, MATH_SCORE as SCORE from test_tb_grade2
union select user_name, '英语' COURSE, EN_SCORE as SCORE from test_tb_grade2
order by user_name,COURSE;
```

# 七、MySQL 存储引擎

## 1、MySQL 的数据库引擎

### 1.1、MyISAM：5.5版本之前默认存储引擎

check table tableName  检查表<br>
repair table tableName 修复表

- myisam 支持数据压缩：myisampack，压缩后的表示只读的
- 以堆表方式存储，使用表级锁；
- 在5.0版本之前，单表默认大小为4G，如存储大表，需要修改：max_rows和avg_row_length 在5.0之后，默认支持的大小256TB
- 适用场景：
	* 不需要使用事务的场景；
	* 只读类应用，读操作远远大于写操作的场景；
	* 空间类应用(空间函数：GPS数据等)

### 1.2、InnoDB存储引擎

[The InnoDB Storage Engine](https://dev.mysql.com/doc/refman/8.0/en/innodb-storage-engine.html)

5.5之后的版本默认存储引擎，InnoDB使用表空间进行数据存储：innodb_file_per_table ，对InnoDB使用独立表空间

- **1.2.1、表转移步骤：把原来存在于系统表空间中的表转移到独立表空间**

	- 使用mysqldump导出所有数据库表的数据
	- 停止mysql服务，修改参数，并删除innodB相关文件；
	- 重启mysql服务，重建innodb系统表空间
	- 重新导入数据.

- **1.2.2、存储特性：**

	- 事务性存储引擎，完全支持事务的ACID特性；
	- 数据按主键聚集存储；
	- Redo Log 和Undo Log
	- 支持行级锁，是在存储引擎实现的，可以最大程度实现并发，以及MVCC
	- 支持全文索引、空间索引；
	- Innodb默认使用的是行锁。而行锁是基于索引的，因此要想加上行锁，在加锁时必须命中索引，否则将使用表锁

- **1.2.3、状态检查**

	show engine innodb status

- **1.2.4、使用场景**

### 1.3、CSV存储引擎

基于 CSV 格式文件存储数据
- **1.3.1、特性：**
	- CSV 存储引擎因为自身文件格式的原因，所有列必须强制指定 NOT NULL；非事务型存储
	- CSV 引擎也不支持索引，不支持分区；
	- CSV 存储引擎也会包含一个存储表结构的 `.frm 文件`、一个 `.csv` 存储数据的文件、一个同名的元信息文件，该文件的扩展名为`.CSM`，用来保存表的状态及表中保存的数据量
	- 每个数据行占用一个文本行，列之间使用逗号分割；

- **1.3.2、适用场景**
	- 适合作为数据交换的中间表

### 1.4、Archive

- **1.4.1、特性：**
	- 非事务型存储引擎；
	- 以zlib对表数据进行压缩，磁盘I/O更少；
	- 数据存储在`arz`为后缀的文件；
	- 只支持insert和select操作；
	- 只允许在自增ID列上增加索引，最多只能存在一个索引

- **1.4.2、使用场景：**
	- 日志和数据采集类应用；
	- 数据归档存储；

### 1.5、Memory

也称为heap存储引擎，所以数据保存在内存中
- **1.5.1、特性：**
	- 非事务型存储引擎；
	- 所有数据保存在内存中
	- 支持hash 和 btree 索引，默认使用hash索引
	- 所有字段都为固定长度 varchar(10) = char(10)
	- 不支持blob 和 text 等大字段
	- 使用表级锁
	- 最大大小由`max_heap_table_size`参数决定，不会对已经存在的表生效。如果需要生效，需重启服务重建数据

- **1.5.2、使用场景：**
	- 用于查找或者是映射表，例如邮编和地区的对应表
	- 用于保存数据分析中产生的中间表；
	- 用于缓存周期性聚合数据的结果表；memory数据易丢失，所以要求数据可再生

### 1.6、Federated

- **1.6.1、特性：**

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

- **1.2、主要区别：**

	- MyISAM 是非事务安全型的， InnoDB 是事务安全型的；
	- MyISAM 锁的粒度是表级锁， InnoDB 是支持行级锁的；
	- MyISAM 支持全文本索引，而InnoDB不支持全文索引
	- MyISAM 相对简单，所以在效率上要优于 InnoDB，小型应用可以考虑使用 MyISAM；MyISAM 更小的表空间
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
或者：`SELECT * FROM INFORMATION_SCHEMA.ENGINES;`

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

### 6.1、存储方式

InnoDB采取的方式是：将数据划分为若干个页，以页作为磁盘和内存之间交互的基本单位，InnoDB中页的大小一般为 16 KB。在一般情况下，一次最少从磁盘中读取16KB的内容到内存中，一次最少把内存中的16KB内容刷新到磁盘中

### InnoDB最佳实践

- 为表指定一个自增的主键；
- 不要使用LOCK TABLES语句，如果需要锁定，可以使用 `SELECT ... FOR UPDATE` 锁定你需要操作的数据行；
- `--sql_mode=NO_ENGINE_SUBSTITUTION`，防止在创建表或者修改表的时候存储引擎不支持

# 八、MySQL事务

数据库的事务：是指一组sql语句组成的数据库逻辑处理单元，在这组的sql操作中，要么全部执行成功，要么全部执行失败

## 1、事务特性

### 1.2、ACID特性

- 原子性（Atomic）：指事务的原子性操作，对数据的修改要么全部执行成功，要么全部失败，实现事务的原子性，是基于日志的Redo/Undo机制
- 一致性（Consistency）：一致性是指事务必须使数据库从一个一致性状态变换到另一个一致性状态，也就是说一个事务执行之前和执行之后都必须处于一致性状态
- 隔离性（Isolation）：隔离性是当多个用户并发访问数据库时，比如操作同一张表时，数据库为每一个用户开启的事务，不能被其他事务的操作所干扰，多个并发事务之间要相互隔离；
- 持久性（Durability）：持久性是指一个事务一旦被提交了，那么对数据库中的数据的改变就是永久性的，即便是在数据库系统遇到故障的情况下也不会丢失提交事务的操作

### 1.3、Redo/Undo机制

Redo/Undo机制是将所有对数据的更新操作都写到日志中；

Redo log用来记录某数据块被修改后的值，可以用来恢复未写入 data file 的已成功事务更新的数据；Undo log是用来记录数据更新前的值，保证数据更新失败能够回滚；

假如数据库在执行的过程中，不小心崩了，可以通过该日志的方式，回滚之前已经执行成功的操作，实现事务的一致性

具体的实现流程：假如某个时刻数据库崩溃，在崩溃之前有事务A和事务B在执行，事务A已经提交，而事务B还未提交。当数据库重启进行 crash-recovery 时，就会通过Redo log将已经提交事务的更改写到数据文件，而还没有提交的就通过Undo log进行roll back

## 2、数据库隔离级别

### 2.1、不考虑隔离性发生的问题

- 脏读：指在一个事务处理过程里读取了另一个未提交的事务中的数据；
- 不可重复读：指在一个事务执行的过程中多次查询某一数据的时候结果不一致的现象，由于在执行的过程中被另一个事务修改了这个数据并提交了事务。

	在对于数据库中的某个数据，一个事务范围内多次查询却返回了不同的数据值，这是由于在查询间隔，被另一个事务修改并提交了；脏读是某一事务读取了另一个事务未提交的脏数据，而不可重复读则是读取了前一事务提交的数据；不可重复读重点在于update和delete

- 虚读（幻读）：幻读通常指的是对一批数据的操作完成后，有其他事务又插入了满足条件的数据导致的现象。指当用户读取某一范围的数据行时，B事务在该范围内插入了新行，当用户再读取该范围的数据行时，会发现有新的“幻影”行；事务非独立执行时发生的一种现象；幻读的重点在于insert。

	比如：第一个事务查询一个User表id=100发现不存在该数据行，这时第二个事务又进来了，新增了一条id=100的数据行并且提交了事务；这时第一个事务新增一条id=100的数据行会报主键冲突，第一个事务再select一下，发现id=100数据行已经存在，这就是幻读

不可重复读的和幻读很容易混淆，不可重复读侧重于修改，幻读侧重于新增或删除。解决不可重复读的问题只需锁住满足条件的行，解决幻读需要锁表

### 2.2、事务隔离级别

事务的隔离级别有4个，由低到高依次，级别越高执行效率越低
- READ_UNCOMMITTED（未授权读取、读未提交）：如果一个事务已经开始写数据，则另外一个事务则不允许同时进行写操作，但允许其他事务读此行数据。该隔离级别可以通过“排他写锁”实现.最低级别，任何情况都无法保证；存在脏读，不可重复读，幻读的问题

- READ_COMMITTED（授权读取、读提交）：该隔离级别避免了脏读，但是却可能出现不可重复读读取数据的事务允许其他事务继续访问该行数据，但是未提交的写事务将会禁止其他事务访问该行；一个事务只能看见已经提交事务所做的改变

	针对当前读，RC隔离级别保证对读取到的记录加锁 (记录锁)，存在幻读现象

- REPEATABLE_READ（可重复读取）：它确保同一事务的多个实例在并发读取数据时，会看到同样的数据行。可避免脏读、不可重复读的发生读取数据的事务将会禁止写事务(但允许读事务)，写事务则禁止任何其他事务。避免了不可重复读取和脏读，但是有时可能出现幻读。这可以通过“共享读锁”和“排他写锁”实现；

	针对当前读，RR隔离级别保证对读取到的记录加锁 (记录锁)，同时保证对读取的范围加锁，新的满足查询条件的记录不能够插入 (间隙锁)，不存在幻读现象

- SERIALIZABLE（序列化）：提供严格的事务隔离。它要求事务序列化执行。事务只能一个接着一个地执行，但不能并发执行。

	从MVCC并发控制退化为基于锁的并发控制。不区别快照读与当前读，所有的读操作均为当前读，读加读锁 (S锁)，写加写锁 (X锁)。Serializable隔离级别下，读写冲突，因此并发度急剧下降

**隔离级别越高，越能保证数据的完整性和一致性，但是对并发性能的影响也越大.对于多数应用程序，可以优先考虑把数据库系统的隔离级别设为 Read Committed。它能够避免脏读取，而且具有较好的并发性能。可以通过悲观锁和乐观锁来控制不可重复读，幻读等并发问题。**

### 2.3、数据库默认隔离级别

大多数数据库的默认级别就是 READ_COMMITTED ，比如 Sql Server、Oracle，MySQL 的默认隔离级别就是 `REPETABLE_READ`
- 查看MySQL事务隔离级别：`select @@transaction_isolation;` 或者 `show variables like 'transaction_isolation'`；
- 设置事务隔离级别：`set  [glogal | session]  transaction isolation level 隔离级别名称`;
- 设置事务隔离级别：`set transaction_isolation='隔离级别名称;'`

### 2.4、隔离级别性能比较

`READ_UNCOMMITTED -> READ_COMMITTED  -> REPEATABLE_READ -> SERIALIZABLE` 隔离的效果是逐渐增强，但是性能却是越来越差。

这个跟MySQL的锁有关，在Mysql中的锁可以分为：共享锁/读锁（Shared Locks」、排他锁/写锁（Exclusive Locks）、间隙锁（Gap Lock）、行锁（Record Locks）、表锁

因为隔离级别的实现肯定是跟锁相关的，READ_UNCOMMITTED 是未加锁的，所以对于它来说也就是没有隔离的效果，所以它的性能也是最好的；

对于读提交和可重复读，他们俩的实现是兼顾解决数据问题，然后又要有一定的并发行，所以在实现上锁机制会比串行化优化很多，提高并发性，所以性能也会比较好，底层实现采用的是MVCC（多版本并发控制）方式进行实现；

对于串行化加的是一把大锁，读的时候加共享锁，不能写，写的时候，家的是排它锁，阻塞其它事务的写入和读取，若是其它的事务长时间不能写入就会直接报超时，所以它的性能也是最差的，对于它来就没有什么并发性可言；

**MySQL加锁时机：**

在数据库的`增、删、改、查`中，只有`增、删、改`才会加上排它锁，而只是查询并不会加锁，只能通过在`select`语句后显式加`lock in share mode`或者`for update`来加共享锁或者排它锁；

### 2.4、RC、RR级别下InnoDB的非阻塞读

### 2.5、RR可重复读如何避免幻读

MySQL InnoDB的可重复读并不保证避免幻读，需要应用使用加锁读来保证。而这个加锁度使用到的机制就是next-key locks；Innodb 的 RR 隔离界别对范围会加上 GAP，理论上不会存在幻读。

事务隔离级别为可重复读时，如果检索条件有索引（包括主键索引）的时候，默认加锁方式是next-key 锁；如果检索条件没有索引，更新数据时会锁住整张表。一个间隙被事务加了锁，其他事务是不能在这个间隙插入记录的，这样可以防止幻读

多版本并发控制机制解决了幻读问题

# 九、数据库锁

[数据库锁机制](../数据库锁机制.md)

## 1、锁

是计算协调多个进程或者线程来并发访问某一资源的机制，数据库并发的一致性和有效性

## 2、锁的分类

### 2.1、按对数据库的操作类型分

- 读锁(共享锁)：针对同一份数据，多个读操作可以同时进行而不相互影响；
- 写锁(互斥锁)：当前写操作没有完成前，它会阻塞其他写锁和读锁

### 2.2、从对数据操作的粒度分

全局锁、表锁、行锁、页级锁

- 全局锁：对整个数据库实例进行枷锁，Mysql提供加全局读锁的方法：Flush table with read lock(FTWRL)。整个命令可以使整个数据库处于只读状态。使用该命令后，数据更新语句、数据定义语句和更新类事务的提交语句等操作都会被阻塞；

	主要使用场景：全库逻辑备份

## 3、表锁（偏读）

偏向 MyISAM 存储引擎，开销小，加锁快，无死锁；锁粒度大，发生的锁冲突的概率最高，并发度最低；

表锁机制：
	
	意向锁（升级锁）：当一个事务带着表锁去访问一个被加锁了行锁的资源，此时，行锁会升级成意向锁；
	自增锁：自增类型的数据

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

行锁的机制：
	
	Record Lock：普通行锁
	Gap Lock：间隙锁，对于键值不存在条件范围内，
	Next-key Lock：行&间隙锁，在键值范围条件内，同时键值又不存在条件范围内

- **4.1、索引失效后无索引行由行锁升级为表锁**

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
	- Innodb_row_lock_current_waits：当前正在等待的锁定数量；
	- Innodb_row_lock_time：从系统启动到现在锁定的总时间长度；(******)
	- Innodb_row_lock_time_avg：每次等待所花平均时间；(******)
	- Innodb_row_lock_time_max：从系统启动到现在等待最长的一次所花的时间
	- Innodb_row_lock_waits：等待总次数(******)

- **4.6、两阶段锁协议**

	在InnoDB事务中，行锁是在需要的时候才加上的，但并不是不需要了就立刻释放，而是要等到事务结束时才释放，并且锁是在同一时刻被释放。这就是两阶段锁协议。

	如果有事务需要锁多个行，要把最可能造成锁冲突、最可能影响并发度的锁尽量往后放；

## 5、死锁

### 5.1、什么是死锁

是指两个或两个以上的进程在执行过程中，因争夺资源而造成的一种互相等待的现象，若无外力作用，它们都将无法推进下去。此时称系统处于死锁状态或系统产生了死锁，这些永远在互相等竺的进程称为死锁进程。表级锁不会产生死锁.所以解决死锁主要还是针对于最常用的InnoDB。

*死锁的关键在于：两个（或以上）的Session加锁的顺序不一致。*

### 5.2、死锁产生原因

死锁一般是事务相互等待对方资源，最后形成环路造成的

分析死锁日志：`SHOW ENGINE INNODB STATUS;`

### 5.3、死锁处理策略

- 直接进入等待，知道超时。超时时间可以通过参数：`innodb_lock_wait_timeout` 来设置；

	在Innodb中`innodb_lock_wait_timeout` 参数默认值时50s，意味着如果采用该策略的话，当出现死锁一行，第一个被锁住的线程要等待50s才会超时退出。但是又不可能设置成很小值，会存在误伤不是死锁，只是简单的锁等待；

- 发起死锁检测，发生死锁后，将持有最少行级排它锁的事务进行回滚，让其他事务得以继续执行。将参数 `innodb_deadlock_detect` 设置为on，表示开启这个逻辑；

	`innodb_deadlock_detect`默认是on的，主动死锁检测在发生死锁的时候，能够快速发现并进行处理的。

### 5.4、死锁案例

- 不同表相同记录行锁冲突：事务A和事务B操作两张表，但出现循环等待锁情况
- 相同表记录行锁冲突，这种情况比较常见：遇到两个job在执行数据批量更新时，jobA处理的的id列表为[1，2，3，4]，	而job处理的id列表为[8，9，10，4，2]，这样就造成了死锁
- 不同索引锁冲突：事务A在执行时，除了在二级索引加锁外，还会在聚簇索引上加锁，在聚簇索引上加锁的顺序是[1，4，2，3，5]，而事务B执行时，只在聚簇索引上加锁，加锁顺序是[1，2，3，4，5]，这样就造成了死锁的可能性.
- gap锁冲突：innodb在RR级别下，如下的情况也会产生死锁

### 5.5、避免死锁

- 以固定的顺序访问表和行：比如两个job批量更新的情形，简单方法是对id列表先排序，后执行，这样就避免了交叉等待锁的情形；又比如将两个事务的sql顺序调整为一致，也能避免死锁；
- 大事务拆小：大事务更容易死锁，如果业务允许，将大事务拆小；
- 在同一个事务中，尽可能做到一次锁定所需要的所有资源，减少死锁概率；
- 降低隔离级别：如果业务允许，将隔离级别调低也是较好的选择，比如将隔离级别从RR调整为RC，可以避免掉很多因为gap锁造成的死锁；
- 为表添加合理的索引：可以看到如果不走索引将会为表的每一行记录添加上锁，死锁的概率大大增大；

可以从以下角度考虑：
- 如果确保业务一定不会出现死锁，可以临时把死锁检测关掉；
- 控制并发度；

### 5.6、定位死锁问题

- 通过应用业务日志定位到问题代码，找到相应的事务对应的sql；
- 确定数据库隔离级别

## 6、乐观锁与悲观锁：(数据库)

- **6.1、悲观锁：**

	- 是对数据被外界（包括本系统当前的其他事务，以及来自外部系统的事务处理）修改持保守态度（悲观）；因此，在整个数据处理过程中，将数据处于锁定状态。悲观锁的实现，往往依靠数据库提供的锁机制；

	- 悲观并发控制实际上是"先取锁再访问"的保守策略，为数据处理的安全提供了保证.但是在效率方面，处理加锁的机制会让数据库产生额外的开销，还有增加产生死锁的机会

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

## 3、表分区的优缺点

### 3.1、优点

- 分区表的数据可以分布在不同的物理设备上，从而高效地利用多个硬件设备；
- 和单个磁盘或者文件系统相比，可以存储更多数据；
- 优化查询.在where语句中包含分区条件时，可以只扫描一个或多个分区表来提高查询效率；涉及sum和count语句时，也可以在多个分区上并行处理，最后汇总结果。
- 分区表更容易维护.例如：想批量删除大量数据可以清除整个分区；
- 可以使用分区表来避免某些特殊的瓶颈.例如InnoDB的单个索引的互斥访问，ext3问价你系统的inode锁竞争等；

### 3.2、表分区的限制因素

- 一个表最多只能有1024个分区；
- MySQL5.1中，分区表达式必须是整数，或者返回整数的表达式。在MySQL5.5中提供了非整数表达式分区的支持；
- 如果分区字段中有主键或者唯一索引的列，那么多有主键列和唯一索引列都必须包含进来；即：分区字段要么不包含主键或者索引列，要么包含全部主键和索引列.
- 分区表中无法使用外键约束；
- MySQL 的分区适用于一个表的所有数据和索引，不能只对表数据分区而不对索引分区，也不能只对索引分区而不对表分区，也不能只对表的一部分数据分区；

## 4、查看分区：判断 MySQL 是否支持表分区

```
mysql> show variables like '%partition%'；
+-------------------+-------+
| Variable_name     | Value |
+-------------------+-------+
| have_partitioning | YES   |
+-------------------+-------+
1 row in set (0.00 sec)
```

## 5、分区表的原理


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
	- range 范围覆盖问题：当插入的记录中对应的分区键的值不在分区定义的范围中的时候，插入语句会失败。上面的例子，如果我插入一条`store_id = 30`的记录会怎么样呢？我们上面分区的时候，最大值是20，如果插入一条超过20的记录，会报错：<br>
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

	针对日期字段的分区就不需要再使用函数进行转换了，例如针对date字段进行分区不需要再使用`YEAR()`表达式进行转换；COLUMN分区支持多个字段作为分区键但是不支持表达式作为分区键；

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

		`partition by hash(store_id) partitions 4;`
		
		上面的语句，根据store_id对4取模，决定记录存储位置。比如store_id = 234的记录，MOD(234，4)=2，所以会被存储在第二个分区.

		常规Hash分区的优点和不足-优点：能够使数据尽可能的均匀分布；缺点：不适合分区经常变动的需求。如果需要增加两个分区变成6个分区，大部分数据都要重新计算分区。线性Hash分区可以解决。

	- 线性Hash分区-分区函数是一个线性的2的幂的运算法则，语法如下：

		`partition by LINER hash(store_id) partitions 4;`

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
	- RANGE分区和LIST分区：`alter table table_name add partition (partition p0 values ...(exp))`，values后面的内容根据分区的类型不同而不同
	- Hash分区和Key分区：`alter table table_name add partition partitions 8`； -- 指的是新增8个分区

- **13.2、删除分区：**
	- RANGE分区和LIST分区：`alter table table_name drop partition p0`； --p0为要删除的分区名称，删除了分区，同时也将删除该分区中的所有数据。同时，如果删除了分区导致分区不能覆盖所有值，那么插入数据的时候会报错.
	- Hash分区和Key分区：`alter table table_name coalesce partition 2`； --将分区缩减到2个

- **13.3、移除分区：**

	`alter table members remove partitioning;`
	
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

## 15、注意事项

- 结合业务场景选择分区键，避免跨分区查询
- 对分区表进行过滤的查询最好在WHERE从句中都包括分区键
- 对于具有主键或唯一索引的表，主键或唯一索引必须是分区键的一部分

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

所谓的反范式化是为了性能和读取效率的考虑而适当的对数据库设计范式的要求进行违反，而允许存在的少量的数据冗余，即反范式化就是使用空间来换取时间

# 十二、mysql高可用

## 1、mysql复制功能

mysql复制功能提供分担读负载，基于主库的二进制日志，异步的。无法保证主库与从库的延迟。

mysql复制解决了什么问题
- 实现在不同服务器上的数据分布，利用二进制日志增量进行；不需要太多的带宽；但是使用基于行的复制在进行大批量的更改时会对带宽带来一定的压力，特别是跨IDC环境进行复制；应该分批进行复制
- 实现数据读取的负载均衡，需要其他组件配合完成
- 增强了数据安全性，利用备库的备份来减少主库负载，并不能用复制来代替备份
- 实现数据库高可用和故障切换；避免mysql的单点故障
- 实现数据库在线升级

## 2、mysql binlog

记录了所有对mysql数据库的修改事件，包括增删查改事件和对表结构的修改事件。且二进制日志中记录是已执行成功的记录

`log_bin`：binlog日志是否打开

### 2.1、mysql二进制日志格式

查看二进制日志格式：`show variables like 'binlog_format';`

修改二进制日志格式：`set binlog_format=''`

- 基于段的格式：`binlog_format=STATEMENT`，mysql5.7之前默认的格式，主要记录的执行sql语句
	- 优点：日志记录量相对较小，节约磁盘及网络IO
	- 缺点：必须记录上下文信息，保证语句在从服务器上的执行结果与主服务器上执行结果相同，但是非确定性函数还是无法正确复制，有可能mysql主从服务器数据不一致
- 基于行的格式：`binlog_format=ROW`，mysql5.7之后的默认格式，可以避免主从服务器数据不一致情况
- 混合模式：`binlog_format=MIXED`

### 2.2、管理binlog

- show master logs：查看所有binlog的日志列表
- show master status：查看最后一个binlog日志的编号名称，及最后一个事件结束的位置
- flush logs：刷新binlog，此刻开始产生一个新编号的binlog日志文件
- reset master：情况所有的binlog日志

### 2.3、查看binlog日志

 `mysqlbinlog --no-defaults -vv --base64-output=DECODE-ROWS <binlog文件>`

## 3、mysql数据库备份

mysqldump

xtrabackup

## 4、mysql主从复制

## 5、mysql读写分离

### 5.1、概述


### 5.2、应用程序代码读写分离


### 5.3、中间件读写分离

## 6、负载均衡

# 十三、数据库分库分表

## 1、数据切分

基本思想是把一个数据库切成多个部分放到不同的数据库上，从而缓解单一数据库的性能问题.

- 对应海量数据，如果是因为表多而数据多，适合使用垂直切分，即把关系紧密的表切分放在一个server上；
- 如果表不多，但是每张表的数据非常多，适合水平切分，即把表的数据根据某种规则切分到多个数据库上.

## 2、垂直切分

### 2.1、最大特点

规则简单，实施也更为方便，尤其适合各业务之间的耦合度非常低，相互影响很小，业务逻辑非常清晰的系统；这种系统中可以很容易做到将不同业务模块所使用的表分拆到不同的数据库中；

### 2.2、垂直切分常见有

- 垂直分库：根据业务耦合性，将关联度低的表存储在不同的数据库，做法与大系统拆分为多个小系统类似，按业务分类进行独立划分；
- 垂直分表：是基于数据库中的“列”进行的，某个表字段比较多，可以新建一张扩展表，将不经常用的字段或长度较大的字段拆分到扩展表中。在字段很多的情况下，通过大表拆小表

### 2.3、垂直切分优缺点

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

### 3.1、特点

- 相对垂直切分来说，稍微复杂点。因为需要将同一个表的不同数据拆分到不同的数据库中，对于应用程序来说，拆分规则本身就较根据表名来拆分更为复杂，后期的数据维护也会更为复杂一些
- 多数系统会将垂直切分和水平切分联合使用：先对系统做垂直切分，再针对每一小搓表的情况选择性地做水平切分，从而将整个数据库切分成一个分布式矩阵；

### 3.2、水平切分

水平切分分为库内分表和分库分表，是根据表内数据内在的逻辑关系，将同一个表按不同的条件分散到多个数据库或多个表中，每个表中只包含一部分数据，从而使得单个表的数据量变小，达到分布式的效果；

库内分表只解决了单一表数据量过大的问题，但没有将表分布到不同机器的库上，因此对于减轻MySQL数据库的压力来说，帮助不是很大，大家还是竞争同一个物理机的CPU、内存、网络IO，最好通过分库分表来解决

### 3.3、水平切分优点

- 不存在单库数据量过大、高并发的性能瓶颈，提升系统稳定性和负载能力
- 应用端改造较小，不需要拆分业务模块

### 3.4、缺点

- 跨分片的事务一致性难以保证
- 跨库的join关联查询性能较差
- 数据多次扩展难度和维护量极大

### 3.5、水平切分典型分片规则

- （1）根据数值范围：按照时间区间或ID区间来切分
	- 优点：
		- 单表大小可控
		- 天然便于水平扩展，后期如果想对整个分片集群扩容时，只需要添加节点即可，无需对其他分片的数据进行迁移
		- 使用分片字段进行范围查找时，连续分片可快速定位分片进行快速查询，有效避免跨分片查询的问题
	- 缺点：热点数据成为性能瓶颈。连续分片可能存在数据热点，例如按时间字段分片，有些分片存储最近时间段内的数据，可能会被频繁的读写，而有些分片存储的历史数据，则很少被查询
- （2）根据数值取模：一般采用hash取模mod的切分方式
	- 优点：
		数据分片相对比较均匀，不容易出现热点和并发访问的瓶颈；
	- 缺点：
		- 后期分片集群扩容时，需要迁移旧的数据（使用一致性hash算法能较好的避免这个问题）
		- 容易面临跨分片查询的复杂问题。比如上例中，如果频繁用到的查询条件中不带cusno时，将会导致无法定位数据库，从而需要同时向4个库发起查询，再在内存中合并数据，取最小集返回给应用，分库反而成为拖累

## 4、切分策略

- 是按先垂直切分再水平切分的步骤进行的.
- 垂直切分的思路就是分析表间的聚合关系，把关系紧密的表放在一起

## 5、数据切分带来的问题

### 5.1、数据库切分后事务问题

分布式事务和通过应用程序与数据库共同控制实现事务
- 分布式事务：
	- 优点：交由数据库管理，简单有效
	- 缺点：性能代价高，特别是shard越来越多时
- 由应用程序和数据库共同控制：
	- 原理：将一个跨多个数据库的分布式事务分拆成多个仅处于单个数据库上面的小事务，并通过应用程序来总控各个小事务；
	- 优点：性能上有优势
	- 缺点：需要应用程序在事务控制上做灵活设计。如果使用了spring的事务管理，改动起来会面临一定的困难

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

## 7、分库分表中间件

- cobar：阿里 b2b 团队开发和开源的，属于 proxy 层方案，就是介于应用服务器和数据库服务器之间。应用程序通过 JDBC 驱动访问 cobar 集群，cobar 根据 SQL 和分库规则对 SQL 做分解，然后分发到 MySQL 集群不同的数据库实例上执行。早些年还可以用，但是最近几年都没更新了，基本没啥人用，差不多算是被抛弃的状态吧。而且不支持读写分离、存储过程、跨库 join 和分页等操作；

- [mycat](../数据库中间件.md#一Mycat)：基于 cobar 改造的，属于 proxy 层方案，支持的功能非常完善，而且目前应该是非常火的而且不断流行的数据库中间件。mycat 这种 proxy 层方案的缺点在于需要部署，自己运维一套中间件，运维成本高，但是好处在于对于各个项目是透明的；

- sharding-jdbc：当当开源的，属于 client 层方案，支持分库分表、读写分离、分布式 id 生成、柔性事务（最大努力送达型事务、TCC 事务）

# 十四、数据库编程

## 1、存储过程

最大的好处是不对票外暴露表结构

```sql
-- 批量插入数据到表中
delimiter ;;
create procedure idata()
begin
  declare i int;
  set i=1;
  while(i<=100000)do
    insert into t values(i, i, i);
    set i=i+1;
  end while;
end;;
delimiter ;
call idata();
```

## 2、函数

函数式特殊的存储过程

## 3、触发器

不需要手动调用，满足某个条件时会自动触发

## 4、为什么放弃数据库编程

- 在数据库集群场景里，因为存储过程、触发器和自定义函数都是在本地数据库执行，所以无法兼容集群场景；
- 数据库编程，如存储过程只是单机时代的产物，并不适合互联网时代

# 十五、Mysql压力测试

## 1、主要关注指标

- QPS：每秒钟处理完请求的次数
- TPS：每秒钟处理完的事务次数
- 响应时间：一次请求所需要的平均处理时间；
- 并发量：系统能同时处理的请求数；

## 2、压测工具

- mysqlslap
- sysbench：长时间高压力
- Jmeter

## 3、sysbench

### 3.1、安装

```
curl -s https://packagecloud.io/install/repositories/akopytov/sysbench/script.rpm.sh | sudo bash
yum -y install sysbench
```
注意：sysbench不能与mysql8安装在同一个虚拟机

### 3.2、sysbench基本语法

sysbench 

### 3.3、开始测试

准备测试数据

```shell
sysbench /usr/share/sysbench/tests/include/oltp_legacy/oltp.lua --mysql-host=192.168.99.202 --mysql-port=3306 --mysql-user=root --mysql-password=abc123456 --oltp-tables-count=10 --oltp-table-size=100000 prepare
```

执行测试

```shell
sysbench /usr/share/sysbench/tests/include/oltp_legacy/oltp.lua --mysql-host=192.168.99.202 --mysql-port=3306 --mysql-user=root --mysql-password=abc123456 --oltp-test-mode=complex --threads=10 --time=300 --report-interval=10 run >> /home/mysysbench.log
```

# 十六、MySql索引

## 1、索引

帮助MySQL高效获取数据的数据结构，索引是数据结构

- 可以理解为：排好序的快速查找数据结，主要影响 where 子句和 order by 子句
- 如何理解索引是数据结构：数据本身之外，数据库还维护着一个满足特定查找算法的数据结构，这些结构以某种方式指向数据，这样可以在这些数据的基础之上实现高级查找算法，这种数据结构就是索引。
- 索引的优点：
	- 创建唯一性索引，保证数据表中每一行数据的唯一性；
	- 大大加快数据的检索速度，降低数据库的IO成本，这是创建索引的最主要的原因；
	- 加快数据库表之间的连接，特别是在实现数据的参考完整性方面有意义；
	- 在使用分组和排序字句进行数据检索时，同样可以显著减少系统的查询中分组和排序的时间
- 索引的缺点：
	- 创建索引和维护索引要耗费时间，这种时间随着数据量的增加而增加；
	- 索引需要占用物理空间，除了数据表占用数据空间之外，每个表还需要占用一定的物理空间；
	- 索引虽然提高了查询速度，同时却降低了更新表的速度，对数据进行增删改的时候，除保存数据外，索引也需要维护，降低数据维护的速度；
	- 索引只是提高效率的一个因素，如果你的mysql有大量数据表，需要花时间建立索引

## 2、索引分类

- 普通索引(它没有任何限制) ，如单值索引-一个索引只包含一个列
- 唯一性索引(索引列的值必须唯一，但允许有空值) 
- 主键索引(一种特殊的唯一索引，不允许有空值.一般是在建表的时候同时创建主键索引) 
- 组合索引：最左匹配原则
- 聚集索引 按照每张表的主键构造一颗B+树，并且叶节点中存放着整张表的行记录数据，因此也让聚集索引的叶节点成为数据页；
- 非聚集索引(辅助索引)(页节点不存放一整行记录)

## 3、索引的语法

- `create [unique] index` 索引名称 on 表名(列名，列名) unique 表示唯一索引
- `alter 表名 add [unique] index [indexName] on 列名`
- `drop index [indexName] on 表名`
- `show index from` 表名

## 4、索引的结构

- B-Tree 索引
- Hash索引
- 全文索引
- R-Tree索引

## 5、哪些情况需要创建索引

- 经常出现在关键字 order by、group by、distinct 后面的字段；
- 在 union 等集合操作的结果集上建立索引；
- 为经常用作查询选择的字段建立索引；
- 在经常考虑用作表连接的属性上建立索引；
- 考虑使用索引覆盖，对数据很少被更新的表，如果用户经常只查询其中的几个字段，可以考虑在这几个字段上建立索引，从而将表的扫描改变为索引的扫描
- 主键自动为唯一索引；查询中与其他表关联的字段，外键关系建立索引

## 6、哪些情况不需要创建索引

- 频繁更新的字段不适合创建索引
- where 条件中用不到的字段不需要创建索引；
- 表记录太少时，不适合创建索引
- 数据重复且分布平均的字段，如果一个字段的值只有 true 和false 且每个值的分布概率大约为50%，对该字段创建索引效率一般没有太大提高

## 7、索引失效

- 如果条件中有or，即使其中有条件带索引，也不会使用(尽量少用or)； 
- Like 查询是以 %开头，例如SELECT * FROM mytable WHEREt Name like’%admin’，会导致全文搜索
- 如果列类型是字符串，那一定要在条件中使用引号引起来，否则不会使用索引；
- 应尽量避免在 where 子句中使用!=或<>操作符
- in 和 not in 也要慎用，否则会导致全表扫描
- 如果第一个筛选条件是范围查询，MySQL 不再使用剩下的索引；

## 8、索引原则

### 8.1、索引建立原则

MySQL中，只有 InnoDB 的表是按照主键方式创建的索引组织表，而 MyISAM 还是按堆表方式组织的
- 使用区分度高的列作为索引：区分度的公式是 count(distinct col)/count(*)，表示字段不重复的比例，区分度越高，索引树的分叉也就越多，一次性找到的概率也就越高；
- 尽量使用字段长度小的列作为索引
- 使用数据类型简单的列(int 型，固定长度)
- 尽量避免选用 NOT NULL 的列，在MySQL中，含有空值的列很难进行查询优化，因为它们使得索引、索引的统计信息以及比较运算更加复杂。你应该用0、一个特殊的值或者一个空串代替空值
- 尽量的扩展索引，不要新建索引。比如表中已经有a的索引，现在要加(a，b)的索引，那么只需要修改原来的索引即可.这样也可避免索引重复
- 选择合适的索引数据类型：
	- 越小的数据类型通常更好：越小的数据类型通常在磁盘、内存和CPU缓存中都需要更少的空间，处理起来更快
	- 简单的数据类型更好：整型数据比起字符，处理开销更小，因为字符串的比较更复杂
- 选择合适的标识符：
	- 整型：通常是作为标识符的最好选择，因为可以更快的处理，而且可以设置为 AUTO_INCREMENT
	- 字符串：尽量避免使用字符串作为标识符，它们消耗更好的空间，处理起来也较慢.而且，通常来说，字符串都是随机的，所以它们在索引中的位置也是随机的，这会导致页面分裂、随机访问磁盘，聚簇索引分裂(对于使用聚簇索引的存储引擎)
- 如果通过调整顺序，可以少维护一个索引，那么这个顺序往往是优先选择的；

### 8.2、索引高效使用原则

- 使用独立的列，即索引列不能是表达式的一部分，比如：`select actore_id from actor where actor_id + 1 = 5;`，这种情下，musql无法自动解析。始终将索引列单独放在比较符号的一侧；
- 使用前缀索引：对于很长的列来说，可以建立字段的部分前缀索引；
- 选择合适的索引列顺序，对于索引列的顺序，可以跑一下查询来确定在表中值的分布情况，并确定哪个列的选择性更高；

## 9、索引的结构及底层实现

大部分数据库系统及文件系统都采用 B-Tree 或其变种 B+Tree 作为索引结构

### 9.1、索引的类型

索引是在存储引擎中实现的而不是在服务器层中实现的。所以，每种存储引擎的索引都不一定完全相同，并不是所有的存储引擎都支持所有的索引类型；

#### 9.1.1、B-Tree 索引：利用 B-Tree 索引进行全关键字、关键字范围和关键字前缀查询，保证按索引的最左边前缀来进行查询

- 匹配全值(Match the full value)：对索引中的所有列都指定具体的值。
- 匹配最左前缀(Match a leftmost prefix)：你可以利用索引查找last name为Allen的人，仅仅使用索引中的第1列。mysql会一直向右匹配直到遇到范围查询就停止匹配；
- 匹配列前缀(Match a column prefix)：你可以利用索引查找last name以J开始的人，这仅仅使用索引中的第1列。
- 匹配值的范围查询(Match a range of values)：可以利用索引查找last name在Allen和Barrymore之间的人，仅仅使用索引中第1列。
- 匹配部分精确而其它部分进行范围匹配(Match one part exactly and match a range on another part)：可以利用索引查找last name为Allen，而first name以字母K开始的人。
- 仅对索引进行查询(Index-only queries)：如果查询的列都位于索引中，则不需要读取元组的值。

#### 9.1.2、Hash 索引：建立在哈希表的基础上，它只对使用了索引中的每一列的精确查找有用

在mysql中，只有memory存储引擎支持显式的哈希索引，是Memory表的默认索引类型
- Hash 索引仅仅能满足`=、IN和<=>`查询，不能使用范围查询。
- Hash 索引无法被用来避免数据的排序操作。
- Hash 索引不能利用部分索引键查询。
- Hash 索引在任何时候都不能避免表扫描。
- Hash 索引遇到大量Hash值相等的情况后性能并不一定就会比B-Tree索引高

 *等值查询。哈希索引具有绝对优势（前提是：没有大量重复键值，如果大量重复键值时，哈希索引的效率很低，因为存在所谓的哈希碰撞问题。）*

#### 9.1.3、空间索引(R-树)索引

MyISAM支持空间索引，主要用于GIS中空间数据的存储，但是MySQL的空间索引支持并不好，现在多使用PostgreSQL

#### 9.1.4、全文索引(Full-text)索引

文本字段上的普通索引只能加快对出现在字段内容最前面的字符串(也就是字段内容开头的字符)进行检索操作

全文索引弊病：
- 中文字段创建全文索引，切词结果太多，占用大量存储空间；
- 更新字段内容，全文索引不会更新，必须定期手动维护；
- 在数据库集群中维护全文索引难度更大；

### 9.2、B-树(B树)

![](image/MySQL索引树结构.png)

- 数据库索引为什么使用树结构存储？
	- 树的查询效率高；
	- 二叉查找树的查找速度和比较次数都是最小的，但不得不考虑的问题是：磁盘IO因为数据库索引是存储在磁盘上的，当数据量比较大的时候，索引大小就可能有几个G甚至更多。当利用索引查询的时候，不能把整个索引全部加在到内存中，能做的只有逐一加在每一个磁盘页，这里的磁盘页对应的索引数的节点。
	- 如果使用二叉查找树，磁盘的IO由树的高度决定，为了较少磁盘IO次数，需要把原来"瘦高"的树变成"矮胖"。

- B树是一种多路平衡查找树，它的每一个节点最多包含k 个孩子，k 称为 B树的阶。k的大小取决于磁盘页的大小。一个m阶的B树具有以下特征：
	- 根节点至少有两个子女
	- 每个中间节点都包含 k-1个元素和k个孩子，其中m/2 <= k <= m；
	- 每个叶子节点都包含 k-1个元素，其中m/2 <= k <= m；
	- 所有叶子结点都位于同一层；
	- 每个节点中的元素从小到大排列，节点当中k-1元素正好是k个孩子包含的元素的值域划分。

- B树的查询：B 树的查询比较次数其实不比二叉搜索树少，尤其是当单一节点中的元素数量很多时。开始相比于磁盘IO的速度，内存中比较耗时几乎可以忽略。所以只要树的高度足够低，IO次数足够少，就可以提交查询速度.只要不超过磁盘页的大小即可

- B树的插入：B 树自平衡
- B树的删除：
- B树主要应用于文件系统以及部分数据库索引，如非关系型数据库 MongoDB

### 9.3、B+树-基于B-树的一种变体

InnoDB存储引擎中索引使用的B+树

#### 9.3.1、一个m阶的B+树具有如下几个特征

- 有k个子树的中间节点包含有k个元素(B树中是k-1个元素)每个元素不保存数据，只用来索引，所有数据都保存在叶子节点.
- 所有的叶子结点中包含了全部元素的信息，及指向含这些元素记录的指针，且叶子结点本身依关键字的大小自小而大顺序链接.
- 所有的中间节点元素都同时存在于子节点，在子节点元素中是最大(或最小)元素.

在图片中，根节点元素8是子节点2，5，8的最大元素，也是叶子节点6，8的最大元素根节点的最大元素等同于整个B+树的最大元素，无论插入删除多少元素，始终保持最大元素在根节点中.

由于父节点的所有元素都出现在子节点，因此所有叶子节点包含了全量的元素信息，并且每一个叶子节点都带有指向下一个节点的指针，形成了一个有序链表

#### 9.3.2、卫星数据

B+树还有一个在索引外的特点，却是至关重要的特点，卫星数据的位置

所谓卫星数据：指的是索引元素所指向的数据记录，比如数据库中的某一行。在B-树种无论中间节点还是叶子节点都带有卫星数据。而B+树只有叶子节点有卫星数据，其余中间节点仅仅是索引，没有任何数据关联。
		
- 在数据库的聚集索引(Clustered Index)中，叶子节点直接包含卫星数据；
- 在非聚集索引(NonClustered Index)中，叶子节点带有指向卫星数据的指针；

#### 9.3.3、B+树的查询好处

- 单行范围：B+树会自顶向下查找节点，最终找到匹配到的叶子节点
	- ①、首先B+树的中间节点没有卫星数据，所以同样的大小的磁盘页可以容纳更多的节点元素。在数据量相同的情况下B+树结构比B-树更"矮胖"，因此查询时IO次数也更少
	- ②、其次，B+树查询必须最终查找到叶子节点，而B-树只要找到匹配元素即可，无论是叶子节点还是中间节点。因此B-树的查找性能不稳定(最好的情况时只查找根节点，最坏情况时查找到叶子节点)B+树的每一次查找都是稳定的
- 范围查询：B-树只能依靠繁琐的中序遍历。而B+树只需要在链表上遍历即可

#### 9.3.4、B+树相对于B-树的优势

- 单一节点存储更多的元素，使得查询的IO次数更少，磁盘读写代价更低；
- 所有查询都要查找到叶子节点，查询性能稳定，查询效率更加稳定；
- 所有叶子节点形成有序链表，便于范围查询；非终结点并不是最终指向文件内容的结点，而只是叶子结点中关键字的索引

大部分关系型数据库，比如Mysql都是用B+树作为索引的

#### 9.3.5、B+树与哈希索引

- B+树是一个平衡的多叉树，从根节点到每个叶子节点的高度差值不超过1，而且同层级的节点间有指针相互链接，是有序的；
- 哈希索引就是采用一定的哈希算法，把键值换算成新的哈希值，检索时不需要类似B+树那样从根节点到叶子节点逐级查找，只需一次哈希算法即可,是无序的；

#### 9.3.4、Btree怎么分裂的，什么时候分裂，为什么是平衡的

### 9.4、稀疏索引与密集索引（聚簇索引）

- 密集索引文件中的每个搜索码值都对应一个索引值；
- 稀疏索引文件只为索引码的某些值建立索引项；

InnoDB-使用的是密集索引
- 若一个主键被定义，该主键则作为密集索引；
- 若没有主键被定义，该表的第一个唯一非空索引作为密集索引；
- 若不满足以上条件，innodb内部会生成一个隐藏主键（密集索引）
- 非主键索引存储相关键位和其对应的主键值，需要两次查找；

### 9.5、各引擎支持的索引

MyISAM，InnoDB，Memonry 三个常用MySQL引擎类型比较：

|索引 			|MyISAM 索引 |InnoDB 索引 |Memonry 索引|
|---------------|-----------|------------|----------------|
|B-tree索引 	|支持		 |支持		  |支持        |
|Hash索引   	|不支持		 |不支持	  |支持        |
|R-tree索引 	|支持		 |不支持	  |不支持      |
|Full-text索引 	|不支持		 |暂不支持	  |不支持      |

## 10、聚簇索引与二级索引

mysql中每个表都有一个聚簇索引（clustered index ），除此之外的表上的每个非聚簇索引都是二级索引，又叫辅助索引（secondary indexes）

聚簇索引并不是一种单独的索引类型，而是一种数据存储方式。具体的细节依赖于其实现方式，但InnoDB的聚簇索引实际上在同一个结构中保存了B-Tree索引和数据行

由于聚簇索引是将数据跟索引结构放到一块，因此一个表仅有一个聚簇索引；

聚簇索引默认是主键，如果表中没有定义主键，InnoDB 会选择一个唯一的非空索引代替。如果没有这样的索引，InnoDB 会隐式定义一个主键来作为聚簇索引。InnoDB 只聚集在同一个页面中的记录。包含相邻健值的页面可能相距甚远。如果你已经设置了主键为聚簇索引，必须先删除主键，然后添加我们想要的聚簇索引，最后恢复设置主键即可

聚簇索引性能最好而且具有唯一性，所以非常珍贵，必须慎重设置。一般要根据这个表最常用的SQL查询方式来进行选择，某个字段作为聚簇索引，或组合聚簇索引

- InnoDB使用的是聚簇索引，将主键组织到一棵B+树中，而行数据就储存在叶子节点上，若使用"where id = 14"这样的条件查找主键，则按照B+树的检索算法即可查找到对应的叶节点，之后获得行数据。即主键索引中叶子节点是整行的数据；
- 若对Name列进行条件搜索，则需要两个步骤：第一步在辅助索引B+树中检索Name，到达其叶子节点获取对应的主键。第二步使用主键在主索引B+树种再执行一次B+树检索操作，最终到达叶子节点即可获取整行数据。（重点在于通过其他键需要建立辅助索引）
- MyISM使用的是非聚簇索引，非聚簇索引的两棵B+树看上去没什么不同；

**聚簇索引的优势：**
- 由于行数据和叶子节点存储在一起，同一页中会有多条行数据，访问同一数据页不同行记录时，已经把页加载到了Buffer中，再次访问的时候，会在内存中完成访问，不必访问磁盘。这样主键和行数据是一起被载入内存的，找到叶子节点就可以立刻将行数据返回了，如果按照主键Id来组织数据，获得数据更快
- 辅助索引使用主键作为"指针"而不是使用地址值作为指针的好处是，减少了当出现行移动或者数据页分裂时辅助索引的维护工作，使用主键值当作指针会让辅助索引占用更多的空间，换来的好处是InnoDB在移动行时无须更新辅助索引中的这个"指针"。也就是说行的位置（实现中通过16K的Page来定位）会随着数据库里数据的修改而发生变化（前面的B+树节点分裂以及Page的分裂），使用聚簇索引就可以保证不管这个主键B+树的节点如何变化，辅助索引树都不受影响
- 聚簇索引适合用在排序的场合，非聚簇索引不适合；
- 取出一定范围数据的时候，使用用聚簇索引；
- 二级索引需要两次索引查找，而不是一次才能取到数据，因为存储引擎第一次需要通过二级索引找到索引的叶子节点，从而找到数据的主键，然后在聚簇索引中用主键再次查找索引，再找到数据；
- 可以把相关数据保存在一起

**聚簇索引的劣势：**
- 维护索引很昂贵，特别是插入新行或者主键被更新导至要分页(page split)的时候；
- 表因为使用UUId（随机ID）作为主键，使数据存储稀疏，这就会出现聚簇索引有可能有比全表扫面更慢；
- 如果主键比较大的话，那辅助索引将会变的更大，因为辅助索引的叶子存储的是主键值；过长的主键值，会导致非叶子节点占用占用更多的物理空间

**为什么要使用自增ID作为主键：**

聚簇索引的数据的物理存放顺序与索引顺序是一致的，即：只要索引是相邻的，那么对应的数据一定也是相邻地存放在磁盘上的。如果主键不是自增id，那么可以想象，它会干些什么，不断地调整数据的物理地址、分页，当然也有其他一些措施来减少这些操作，但却无法彻底避免。但如果是自增的，那就简单了，它只需要一页一页地写，索引结构相对紧凑，磁盘碎片少，效率也高；

使用自增主键的插入数据模式，每次插入一条新纪录时，都是追加操作，都不涉及到挪动其他记录，也不会触发叶子节点的分裂，页数据页不会发生分裂；

主键长度越小，普通索引的叶子节点就越小，普通索引占用的空间也就越小

**何时使用聚簇索引与非聚簇索引**

动作 | 使用聚簇索引 | 使用非聚簇索引
----|-------------|-----------
列经常被分组排序|是|是
返回某范围内的数据|是|否
一个或极少不同值|否|否
小数目的不同值|是|否
大数目的不同值|否|是
频繁更新的列|否|是
外键列|是|是
主键列|是|是
频繁修改索引列|否|是

- 聚簇索引：索引和数据存储在一块（ 都存储在同一个B*tree 中）
- 非聚簇索引：索引数据和存储数据是分离的
- 二级索引：存储的是记录的主键，而不是数据存储的地址。唯一索引、普通索引、前缀索引等都是二级索引（辅助索引）

所有非聚簇索引即二级索引访问需要两次索引查找，因为在二级索引中保存的行指针。二级索引的叶子节点保存的不是指向行的物理位置的指针，而是行的主键值，这意味着通过二级索引查找行，在存储引擎需要找到二级索引的叶子节点获得对应的主键值，然后根据这个值再去聚簇索引中查找对应的行。即回表；

InnoDB在二级索引上使用共享锁，但访问主键索引需要排它锁，这消除了覆盖索引的可能性

## 11、主键索引、普通索引、覆盖索引等

表结构如下
```sql
mysql> create table T(
	id int primary key, 
	k int not null, 
	name varchar(16),
	index (k)
)engine=InnoDB;

-- 初始化数据
insert into T values(100,1, 'aa'),(200,2,'bb'),(300,3,'cc'),(500,5,'ee'),(600,6,'ff'),(700,7,'gg');
```

### 11.1、主键索引和普通索引区别

- 如果语句是`select * from T where ID=500`，即主键查询方式，则只需要搜索到ID这颗B+数索引；
- 如果语句是`select * from T where k = 5`，即普通索引查询方式，则需要先搜索k索引树，然后得到ID的值，再到ID索引树搜索一次。这个过程称为回表；

### 11.2、覆盖索引

执行语句`select ID from T where k between 3 and 5`，此时该SQL只需要查ID值，而ID的值已经在k索引树上了，因此可以提供查询结果，不需要回表。也就是说，在这个查询里面，索引 k 已经覆盖了我们的查询需求，称为覆盖索引；

**由于覆盖索引可以减少树的搜索次数，显著提升查询性能，所以使用覆盖索引是一个常用的性能优化手段**

覆盖索引需要存储索引列的值，覆盖索引只能是使用B+Tree

## 12、如何创建索引

### 12.1、字符串字段如何创建索引

可以使用前缀索引，定义好长度，就可以做到既节省空间，又不用增加太多的查询成本，比如：
```sql
-- 创建的索引里，每个记录都是只取前6个字节
alter table SUser add index index2(email(6));
```

**如何确定应该使用多长的前缀：**

在建立索引时，关注的是区分度，区分度越高越好。因为区分度越高，意味着重复的键值越少，可以通过下列语句算出该列上有多少个不同的值：
```sql
select count(distinct email) as L from SUser;
```
然后依次选取不同长度的前缀来看这个值，比如：
```sql
mysql> select 
  count(distinct left(email,4)）as L4,
  count(distinct left(email,5)）as L5,
  count(distinct left(email,6)）as L6,
  count(distinct left(email,7)）as L7,
from SUser;
```

如果碰到类似某个市或者某个县的居民身份信息，可以创建索引的方式是，但是都不支持范围查询
- 倒序存储：存储身份信息的时候把它倒过来存储：
	```sql
	mysql> select field_list from t where id_card = reverse('input_id_card_string');
	```
	由于身份证号后6位没有地址码这样的重复逻辑，最后6位可以提供足够的区分度
- 使用hash字段：可以再表上再创建一个整数字段，来保存身份证的校验码，同时在这个字段上创建索引
	```sql
	mysql> alter table t add id_card_crc int unsigned, add index(id_card_crc);
	```

# 十七、数据库优化

## 1、数据库优化的目的

- 避免页面访问出错：数据库连接超时、慢查询造成页面无法加载、阻塞造成数据无法提交
- 增加数据库稳定性
- 优化用户体验

## 2、影响 mysql 性能的因素

硬件 > 系统设置 > 数据库参数配置 > 数据库表结构 > SQL及索引； 成本往右越低，效果往右越好

- MySQL 查询优化器：MySQL有专门的优化 select 语句的优化器模块，通过计算分析系统中收集到的统计信息，为客户端请求的query提供其认为的最优执行计划.
- MySQL 场景瓶颈：
	- CPU：CPU在饱和一般发生在数据装入内存或从磁盘上读取数据的时候；
	- IO：磁盘I/O瓶颈发生在装入数据远远大于内存的容量
	- 服务器硬件性能瓶颈：top，free，iostat 和 vmstat 查看系统的性能状态

### 2.1、一条SQL语句执行得很慢的原因

- 针对偶尔很慢的情况：
	- 数据库在刷新脏页：当我们要往数据库插入一条数据、或者要更新一条数据的时候，我们知道数据库会在内存中把对应字段的数据更新了，但是更新之后，这些更新的字段并不会马上同步持久化到磁盘中去，而是把这些更新的记录写入到 redo log 日记中去，等到空闲的时候，在通过 redo log 里的日记把最新的数据同步到磁盘中去； redo log 写满了需要同步到磁盘
	- 等待锁：在执行的过程中在等待锁，可以通过 show processlist 来查看当前的状态；执行的时候，遇到锁，如表锁、行锁
- 一直很慢的情况：
	- 没有用到索引；
	- 数据库选择了错误的索引；

## 3、MySQL 性能优化

- 服务器硬件对性能的优化：
	- CPU：(目前不支持多CPU对同一sql进行处理)
		- 64位的CPU一定要工作在64位的系统上；
		- 对于并发比较高的场景CPU的数量(核数)比频率重要；
		- 对于CPU密集型场景和复杂SQL则频率越高越好；
	- 内存：
		- 选择主板所能使用的最高频率内存；
		- 内存的大小对性能很重要，所以尽可能大；
	- I/O子系统：
		PCIe > SSD > Raid10 >  磁盘 > SAN(网络存储)

## 4、慢查询日志包含的内容

使用MySQL慢查询日志对有效率问题的SQL进行监控
```sql
show variables like '%slow_queries%%'; -- 查看慢sql的条数
show variables	like 'slow_query_log';

set global slow_query_log_file='/home/mysql/sql_log/mysql_slow_log'; -- 慢查询日志存储的硬盘位置
set global log_queries_not_using_indexes=on; -- 是否将未使用索引的sql记录到慢查询日志中
set global long_query_time=1; --将超过多少秒的sql记录到慢查询日志中，需要重新连接数据库服务器
```
慢日志的信息：
```
# Time: 2020-06-06T09:18:09.015669Z
# User@Host: root[root] @ localhost []  Id:   626
# Query_time: 0.000231  Lock_time: 0.000103 Rows_sent: 3  Rows_examined: 3
SET timestamp=1591435089;
select * from customer_login_log;
```
- SQL开始执行的时间戳
	```
	# SET timestamp=1460268587;
	```
- 执行SQL的主机信息：
	```
	# User@Host： root[root] @ localhost [127.0.0.1]
	```
- SQL的执行详细信息：
	```
	# Query_time： 0.002000  Lock_time： 0.001000 Rows_sent： 2  Rows_examined： 2
	```
- SQL的详细内容

## 5、慢查询日志分析查看

- mysqldumpslow 
- pt-query-digest 工具
- 如何发现 SQL 存在问题：
	- 查询次数多且每次查询占用时间长的SQL，通常为pt-query-digest分析的前几个查询
	- IO大的SQL；注意pt-query-digest分析中的rows examine
	- 未命中索引的SQL：注意pt-query-digest分钟中的rows examine和rows send 的对比

## 6、分析SQL查询

使用 explain 查询SQL的执行计划：`explain select * from customer；`

id -> 表的读取顺序

select_type -> 数据读取操作的操作类型
```
+----+-------------+----------+------+---------------+------+---------+------+------+-------+
| id | select_type | table    | type | possible_keys | key  | key_len | ref  | rows | Extra |
+----+-------------+----------+------+---------------+------+---------+------+------+-------+
|  1 | SIMPLE      | customer | ALL  | NULL          | NULL | NULL    | NULL |  646 |       |
+----+-------------+----------+------+---------------+------+---------+------+------+-------+
```

查看优化后的SQL：`explain extended sql`语句，然后`show warnings`查看。`explain extended`会输出sql的执行计划，查询记录的方式(全表扫描、全索引扫描、索引范围扫描等)、是否用上索引；`show warnings`会看到优化器重写后的sql

### 6.1、id：select的查询序列号，包含一组数字，表示在查询中执行 select 子句或者操作表的顺序

- id相同，执行顺序由上而下
	```sql
	mysql> explain select film.* from film left join film_actor fa on film.film_id = fa.film_id
		left join actor on fa.actor_id=actor.actor_id where first_name = 'sandra'；
	+----+-------------+-------+--------+------------------------+---------+---------+-----------------------+------+--------------------------+
	| id | select_type | table | type   | possible_keys          | key     | key_len | ref                   | rows | Extra                    |
	+----+-------------+-------+--------+------------------------+---------+---------+-----------------------+------+--------------------------+
	|  1 | SIMPLE      | actor | ALL    | PRIMARY                | NULL    | NULL    | NULL                  |  200 | Using where              |
	|  1 | SIMPLE      | fa    | ref    | PRIMARY，idx_fk_film_id | PRIMARY | 2       | sakila.actor.actor_id |   13 | Using where； Using index |
	|  1 | SIMPLE      | film  | eq_ref | PRIMARY                | PRIMARY | 2       | sakila.fa.film_id     |    1 |                          |
	+----+-------------+-------+--------+------------------------+---------+---------+-----------------------+------+--------------------------+
	```

- id不同，如果是子查询，id的序号会递增，id值越大优先级越高，越先被执行
	```sql
	mysql> explain select title，release_year，length from film where film_id in(
				select film_id from film_actor WHERE actor_id in (
					select actor_id from actor where first_name='sandra'
				)
			)；
	+----+--------------------+------------+-----------------+----------------+----------------+---------+------+------+--------------------------+
	| id | select_type        | table      | type            | possible_keys  | key            | key_len | ref  | rows | Extra                    |
	+----+--------------------+------------+-----------------+----------------+----------------+---------+------+------+--------------------------+
	|  1 | PRIMARY            | film       | ALL             | NULL           | NULL           | NULL    | NULL | 1128 | Using where              |
	|  2 | DEPENDENT SUBQUERY | film_actor | index_subquery  | idx_fk_film_id | idx_fk_film_id | 2       | func |    2 | Using index； Using where |
	|  3 | DEPENDENT SUBQUERY | actor      | unique_subquery | PRIMARY        | PRIMARY        | 2       | func |    1 | Using where              |
	+----+--------------------+------------+-----------------+----------------+----------------+---------+------+------+--------------------------+
	```

- id相同不同，同时存在，DERIVED-衍生，如下中 table 为 ```<derived2>```表示是根据id为2衍生的表格
	```sql
	mysql> explain select title，	release_year，	length from film left join (
			select film_id from film_actor where actor_id in (
					select actor_id from actor where first_name = 'sandra'
				)
			)s on film.film_id = s.film_id；
	+----+--------------------+------------+-----------------+---------------+----------------+---------+------+------+--------------------------+
	| id | select_type        | table      | type            | possible_keys | key            | key_len | ref  | rows | Extra                    |
	+----+--------------------+------------+-----------------+---------------+----------------+---------+------+------+--------------------------+
	|  1 | PRIMARY            | film       | ALL             | NULL          | NULL           | NULL    | NULL | 1128 |                          |
	|  1 | PRIMARY            | <derived2> | ALL             | NULL          | NULL           | NULL    | NULL |   56 |                          |
	|  2 | DERIVED            | film_actor | index           | NULL          | idx_fk_film_id | 2       | NULL | 5143 | Using where； Using index |
	|  3 | DEPENDENT SUBQUERY | actor      | unique_subquery | PRIMARY       | PRIMARY        | 2       | func |    1 | Using where              |
	+----+--------------------+------------+-----------------+---------------+----------------+---------+------+------+--------------------------+
	```

### 6.2、select_type：查询类型，主要用于区别普通查询，联合查询，子查询等复杂查询，主要有以下值

- SIMPLE：简单的select查询，查询中不包含子查询或者union
- PRIMARY：查询中若包含任何复杂的子查询，最外层的查询则被标记为 primary
- SUBQUERY：在 select 或者 where 列表中包含了子查询
- DERIVED：在 from 列表中包含的子查询被标记为 DERIVED，MySQL 会递归执行这些子查询，把结果放在临时表中
- UNION：若第二个 select 出现在 union 之后，则被标记为union；若 union 包含在 from 子句的子查询中， 外层 select 将被标记为 DERIVED
- UNION RESULT：从 union 中获取结果的 select

### 6.3、type：显示查询使用了何种类型，主要有：all， index， range， ref， eq_ref， const， system

从最好到最差依次为：system > const > eq_ref > ref > range > index > all

- system：表只有一行记录(等于系统表)，这时const类型的特例，平时不会出现，基本上日常优化可以忽略
- const：表示通过索引一次就找到了，const 用于比较 primary key 和 unique 索引.因为只匹配一行数据，所以很快.如将主键置于 where 列表中，MySQL能将该查询转换为一个常量.
- eq_ref：唯一性索引扫描，对于每个索引键，表中只有一条记录.常见于主键或唯一性扫描
- ref：非唯一性索引扫描，返回匹配某个单独值的所有行.本质上也是一种索引访问，返回的是某个单独值匹配的所有行，但是其可能会找到多个符合条件的行.
- index_merge：使用了索引合并优化方法；
- range：只检索给定范围的行，使用一个索引来选择行。key列显示使用了哪个索引。一般是在 where 语句中使用了 `between、<、>、in` 等的查询.这种范围扫描索引比全表扫描要好，因为其只需要开始于索引的某一点，而结束于另一点，不需要扫描全部索引。
- index：Full index scan， index 和 all 区别为 index 类型只遍历索引树，这通常比all快.因为索引文件通常比数据文件小.(即两者虽然都是读全表，但是index 是从索引中读取，而all是从硬盘读取)
- all：遍历全表以匹配到相应的行

**一般来说，type在查询中能保证到range级别，最好能达到 ref**

### 6.4、possible_keys、key、key_len

- possible_keys：显示可能应用在这张表上的索引，一个或者多个。查询涉及到的字段上若存在索引，则将该索引列出，但不一定被查询实际使用。
- key：实际使用的索引。如果为 NULL，则没有使用索引；查询中如果使用了覆盖索引，则该索引和查询的 select 字段重叠；message 表中字段 conversation_id 有加上索引
	```sql
	mysql> EXPLAIN select * FROM message；
	+----+-------------+---------+------+---------------+------+---------+------+------+-------+
	| id | select_type | table   | type | possible_keys | key  | key_len | ref  | rows | Extra |
	+----+-------------+---------+------+---------------+------+---------+------+------+-------+
	|  1 | SIMPLE      | message | ALL  | NULL          | NULL | NULL    | NULL |    1 |       |
	+----+-------------+---------+------+---------------+------+---------+------+------+-------+
	1 row in set

	mysql> EXPLAIN select conversation_id FROM message；
	+----+-------------+---------+-------+---------------+--------------------+---------+------+------+-------------+
	| id | select_type | table   | type  | possible_keys | key                | key_len | ref  | rows | Extra       |
	+----+-------------+---------+-------+---------------+--------------------+---------+------+------+-------------+
	|  1 | SIMPLE      | message | index | NULL          | conversation_index | 137     | NULL |    1 | Using index |
	+----+-------------+---------+-------+---------------+--------------------+---------+------+------+-------------+
	1 row in set
	```
- key_len：表示索引中使用的字节数，可通过查询该列计算查询中使用的索引长度。在不损失精度的情况下，长度越短越好，key_len显示的值为该索引最大的可能长度，并非实际使用的长度。即 key_len 是根据表定义计算而得，不是通过表内检索出的。key_len 的计算规则和三个因素有关：数据类型、字符编码、是否为 NULL，如果是 null 的话需要判断是否为 null 的标识长度，所以索引字段最好不要为 null，因为 null 会使索引，索引统计和值更加复杂，并且需要额外一个字节的存储空间

### 6.5、ref：显示索引的哪一列被使用了，如果可能的话是一个常数.哪些列或常量被用于查找索引列上的值.

```sql
mysql> EXPLAIN SELECT n.*  FROM news n， `user` u WHERE n.user_id=u.id AND n.title=''；
+----+-------------+-------+--------+---------------+---------+---------+-------------------+------+--------------------------+
| id | select_type | table | type   | possible_keys | key     | key_len | ref               | rows | Extra                    |
+----+-------------+-------+--------+---------------+---------+---------+-------------------+------+--------------------------+
|  1 | SIMPLE      | n     | ALL    | NULL          | NULL    | NULL    | NULL              |    2 | Using where              |
|  1 | SIMPLE      | u     | eq_ref | PRIMARY       | PRIMARY | 4       | toutiao.n.user_id |    1 | Using where； Using index |
+----+-------------+-------+--------+---------------+---------+---------+-------------------+------+--------------------------+
toutiao.n.user_id
```
toutiao：表示数据库名称， n：表示对应的表格， user_id：n表对应的字段

### 6.6、rows：根据表统计信息及索引选用的情况

大致估算出找到所需记录所需要读取的行数，即有多少条记录被优化器所查询

### 6.7、Extra：包含不适合在其他列显示但非常重要的额外信息

- using filesort：说明 mysql中会对数据使用一个外部的索引排序，而不是按照表内的索引顺序进行读取。MySQL中无法利用索引完成的排序称为"文件排序"
	```sql
	mysql> EXPLAIN SELECT * FROM message ORDER BY created_date；
	+----+-------------+---------+------+---------------+------+---------+------+------+----------------+
	| id | select_type | table   | type | possible_keys | key  | key_len | ref  | rows | Extra          |
	+----+-------------+---------+------+---------------+------+---------+------+------+----------------+
	|  1 | SIMPLE      | message | ALL  | NULL          | NULL | NULL    | NULL |    1 | Using filesort |
	+----+-------------+---------+------+---------------+------+---------+------+------+----------------+
	```
- using temporary：使用了临时表保存中间结果，MySQL在对查询结果排序时使用了临时表。常见于排序 order by 和分组查询 group by 中
	```sql
	mysql> EXPLAIN SELECT * FROM message GROUP BY created_date；
	+----+-------------+---------+------+---------------+------+---------+------+------+---------------------------------+
	| id | select_type | table   | type | possible_keys | key  | key_len | ref  | rows | Extra                           |
	+----+-------------+---------+------+---------------+------+---------+------+------+---------------------------------+
	|  1 | SIMPLE      | message | ALL  | NULL          | NULL | NULL    | NULL |    1 | Using temporary； Using filesort |
	+----+-------------+---------+------+---------------+------+---------+------+------+---------------------------------+
	```
- using index：表示相应的查询操作中使用了覆盖索引，避免访问表的数据行。如果同时出现 using where，说明索引是用来执行索引键值的查找；如果没有同时出现 using where，说明索引是用来读取数据而非执行查找动作；
	- 覆盖索引：select 的数据列只用从索引中就能够取得，不必读取数据行，MySQL 可以利用索引返回 select 列表中的字段，而不必根据索引再次读取数据文件，换句话说查询列要被所建的索引覆盖。如果使用覆盖索引，要注意 select 列表只读取所需要的列，不要使用 select *，因为如果将所有字段一起做成索引会导致索引文件过大，查询性能下降。

- using where：使用 where 子句

- using join buffer：使用了连接缓存
	```sql
	mysql> explain select * from category inner join book on category.card=book.card inner join phone on category.card=phone.card；
	+----+-------------+----------+------+---------------+------+---------+------+------+--------------------------------+
	| id | select_type | table    | type | possible_keys | key  | key_len | ref  | rows | Extra                          |
	+----+-------------+----------+------+---------------+------+---------+------+------+--------------------------------+
	|  1 | SIMPLE      | category | ALL  | NULL          | NULL | NULL    | NULL |   20 |                                |
	|  1 | SIMPLE      | book     | ALL  | NULL          | NULL | NULL    | NULL |   20 | Using where； Using join buffer |
	|  1 | SIMPLE      | phone    | ALL  | NULL          | NULL | NULL    | NULL |   20 | Using where； Using join buffer |
	+----+-------------+----------+------+---------------+------+---------+------+------+--------------------------------+
	```
- impossible where：where子句的值总是false，不能用来获取任何元祖
- select table optimized away：在没有 group by 子句的情况下，基于索引优化 mix/max 操作或者对于 MyISAM 存储引擎优化 count(*)操作，不必等到执行阶段在进行计算，查询执行计划生成的阶段即可完成优化
- distinct：优化 distinct 操作，在找第一匹配的元祖后即停止找同样值的操作.

## 7、索引优化

表相关操作SQL参加文件：[数据库脚本.sql](数据库脚本-用于索引优化.sql)

### 7.1、单表优化：查询 category_id 为1 且 comments > 1 的情况下，views 最多的 article_id**

基本sql：```sqlselect id from article where category_id=1 and comments > 1 order by views desc limit 1；```

- 通过查看执行计划查看，可以知道其为全表扫描且是 using filesort的.

	```sql
	mysql> explain select id from article where category_id=1 and comments > 1 order by views desc limit 1\G
	*************************** 1. row ***************************
				id： 1
		select_type： SIMPLE
			table： article
				type： ALL
	possible_keys： NULL
				key： NULL
			key_len： NULL
				ref： NULL
				rows： 3
			Extra： Using where； Using filesort
	```

- 创建索引，索引字段为：category_id，comments，views
	```sql
	create index idx_article_ccv on article(category_id， comments， views)；
	```
	再次查看执行计划，可以发现，使用了索引且type也为ref了，但是 依然使用了 using filesort
	```sql
	mysql> explain select id，author_id from article where category_id=1 and comments > 1 order by views desc limit 1\G
	*************************** 1. row ***************************
				id： 1
		select_type： SIMPLE
			table： article
				type： range
	possible_keys： idx_article_ccv
				key： idx_article_ccv
			key_len： 8
				ref： NULL
				rows： 1
			Extra： Using where； Using filesort
	```
- 继续优化，删除索引，重新创建索引，索引字段为 category_id和views
	```sql
	drop index idx_article_ccv on article；
	create index idx_article_cv on article(category_id， views)；
	```
	再次查看执行计划，这时已经达到最优了
	```sql
	mysql> explain select id，author_id from article where category_id=1 and comments > 1 order by views desc limit 1\G
	*************************** 1. row ***************************
				id： 1
		select_type： SIMPLE
			table： article
				type： ref
	possible_keys： idx_article_cv
				key： idx_article_cv
			key_len： 4
				ref： const
				rows： 2
			Extra： Using where
	```
- 在上述第一次优化时，type 变成了 range，可以接收。但是extra里仍然存在 using filesort。我们已经建立了索引，为什么没有作用呢？

	因为按照 Btree索引的工作原理，先排序 category_id， 如果遇到相同的 category_id 则再排序 comments，如果遇到相同的 comments 则再排序 views，当 comments 字段在联合索引里处于中间位置时，因为 comments >1条件是一个范围，MySQL无法利用索引再对后面的 views 部分进行检索，即 range类型查询字段后面的索引无效；

### 7.2、两表优化

左连接查询优化：
- 没有任何索引除主键外：
	```sql
	mysql> EXPLAIN SELECT * FROM `category` c LEFT JOIN  book b on c.card=b.card；
	+----+-------------+-------+------+---------------+------+---------+------+------+-------+
	| id | select_type | table | type | possible_keys | key  | key_len | ref  | rows | Extra |
	+----+-------------+-------+------+---------------+------+---------+------+------+-------+
	|  1 | SIMPLE      | c     | ALL  | NULL          | NULL | NULL    | NULL |   20 |       |
	|  1 | SIMPLE      | b     | ALL  | NULL          | NULL | NULL    | NULL |   20 |       |
	+----+-------------+-------+------+---------------+------+---------+------+------+-------+
	```
- 给book 表 card 字段创建索引：
	```sql
	alter table book add index idx_book_card(card)；
	mysql> EXPLAIN SELECT * FROM `category` c LEFT JOIN  book b on c.card=b.card；
	+----+-------------+-------+------+---------------+---------------+---------+-------------+------+-------------+
	| id | select_type | table | type | possible_keys | key           | key_len | ref         | rows | Extra       |
	+----+-------------+-------+------+---------------+---------------+---------+-------------+------+-------------+
	|  1 | SIMPLE      | c     | ALL  | NULL          | NULL          | NULL    | NULL        |   20 |             |
	|  1 | SIMPLE      | b     | ref  | idx_book_card | idx_book_card | 4       | demo.c.card |    1 | Using index |
	+----+-------------+-------+------+---------------+---------------+---------+-------------+------+-------------+
	```
	上述中 category 表仍然查全表，而 book 表是根据索引来查询的.

- 去除book表的索引，给 category 表 card 字段加上索引
	```sql
	drop index idx_book_card on book；
	alter table category add index idx_cat_card(card)；
	mysql> EXPLAIN SELECT * FROM `category` c LEFT JOIN  book b on c.card=b.card；
	+----+-------------+-------+-------+---------------+--------------+---------+------+------+-------------+
	| id | select_type | table | type  | possible_keys | key          | key_len | ref  | rows | Extra       |
	+----+-------------+-------+-------+---------------+--------------+---------+------+------+-------------+
	|  1 | SIMPLE      | c     | index | NULL          | idx_cat_card | 4       | NULL |   20 | Using index |
	|  1 | SIMPLE      | b     | ALL   | NULL          | NULL         | NULL    | NULL |   20 |             |
	+----+-------------+-------+-------+---------------+--------------+---------+------+------+-------------+
	```
	可以看到上面 category 表使用了索引，但是rows 仍然是20行.

- 从上述步骤中可以看出：left join 条件用于确定如何从右表中搜索行，左边一定都有：

	一般左右连接查询一般都是索引创建在相反的方向上：如果是左连接，则相关字段索引建立在右边表上；如果是右连接，则相关字段索引建立在左边表上；

### 7.3、三表查询

- 没有创建索引：
	```sql
	mysql> explain select * from category left join book on category.card=book.card left join phone on category.card=phone.card；
	+----+-------------+----------+------+---------------+------+---------+------+------+-------+
	| id | select_type | table    | type | possible_keys | key  | key_len | ref  | rows | Extra |
	+----+-------------+----------+------+---------------+------+---------+------+------+-------+
	|  1 | SIMPLE      | category | ALL  | NULL          | NULL | NULL    | NULL |   20 |       |
	|  1 | SIMPLE      | book     | ALL  | NULL          | NULL | NULL    | NULL |   20 |       |
	|  1 | SIMPLE      | phone    | ALL  | NULL          | NULL | NULL    | NULL |   20 |       |
	+----+-------------+----------+------+---------------+------+---------+------+------+-------+
	```

- book 和 phone 表创建索引后：
	```sql
	create index idx_b_card on book(card)；
	create index idx_p_card on phone
	mysql> explain select * from category left join book on category.card=book.card left join phone on category.card=phone.card；
	+----+-------------+----------+------+---------------+------------+---------+--------------------+------+-------------+
	| id | select_type | table    | type | possible_keys | key        | key_len | ref                | rows | Extra       |
	+----+-------------+----------+------+---------------+------------+---------+--------------------+------+-------------+
	|  1 | SIMPLE      | category | ALL  | NULL          | NULL       | NULL    | NULL               |   20 |             |
	|  1 | SIMPLE      | book     | ref  | idx_b_card    | idx_b_card | 4       | demo.category.card |    1 | Using index |
	|  1 | SIMPLE      | phone    | ref  | idx_p_card    | idx_p_card | 4       | demo.category.card |    1 | Using index |
	+----+-------------+----------+------+---------------+------------+---------+--------------------+------+-------------+
	```

- join语句的优化：

	- 尽可能的减少 join 语句中的 nestedLooop 的循环总次数，永远用小结果驱动大的结果集
	- 优先优化 nestedLooop 的内层循环；
	- 保证 join 语句中被驱动表上的 join 条件字段已经被索引；
	- 当无法保证被驱动表的join条件字段被索引且内存资源充足的情况下，不要太吝惜 joinBuffer 的设置

### 7.4、索引失效

- （1）全值匹配，最佳左前缀法则：如果索引了多列，要遵守最左前缀法则.指的是查询从索引的最左前列开始并且不跳过索引中的列.

	有表 staffs ，其有id，name，age，pos，add_time 五个字段，给对应字段创建索引，如下：
	```sql
	alter table staffs add index idx_staffs_nap(name， age， pos)；
	explain select * from staffs where name='Jayden'
	explain select * from staffs where name='Jayden' and age=35 
	explain select * from staffs where name='Jayden' and age=35 and pos='dev'；
	```
	上述三条执行计划中，都有用到了索引，再看下面一条sql的执行计划，其并没有用到索引：
	```sql
	mysql> explain select * from staffs where age=35 and pos='dev'；
	+----+-------------+--------+------+---------------+------+---------+------+------+-------------+
	| id | select_type | table  | type | possible_keys | key  | key_len | ref  | rows | Extra       |
	+----+-------------+--------+------+---------------+------+---------+------+------+-------------+
	|  1 | SIMPLE      | staffs | ALL  | NULL          | NULL | NULL    | NULL |    3 | Using where |
	+----+-------------+--------+------+---------------+------+---------+------+------+-------------+
	```
	因为按照最佳左前缀法则，name 字段是创建的索引最左前列， 而该sql是从age开始查询的。再看如下sql的执行计划：
	```sql
	mysql> explain select * from staffs where name='Jayden' and pos='dev'；
	+----+-------------+--------+------+----------------+----------------+---------+-------+------+-------------+
	| id | select_type | table  | type | possible_keys  | key            | key_len | ref   | rows | Extra       |
	+----+-------------+--------+------+----------------+----------------+---------+-------+------+-------------+
	|  1 | SIMPLE      | staffs | ref  | idx_staffs_nap | idx_staffs_nap | 767     | const |    1 | Using where |
	+----+-------------+--------+------+----------------+----------------+---------+-------+------+-------------+
	```
	上述sql中只用到了部分索引，而不是全值匹配

- （2）不再索引列上做任何操作(计算，函数，自动或者手动类型转换)，会导致索引失效而转向全表扫描字符串不加引号会引起数据库内部隐式的类型转换.
- （3）如果第一个筛选条件是范围查询，MySQL 不再使用剩下的索引；
- （4）尽量使用覆盖索引(只访问索引的查询-索引列和查询列一致)，减少使用 select *
- （5）使用不等于(!=， <>)或者 or 会导致索引失效.同样 is null 和 is not null 也无法使用索引。
- （6）like 以通配符开头('%aa'，'%aa%')mysql索引失效，变成全表扫描。

	但是 'aa%' 仍然可以使用索引.因为其可以通过 aa 进行相应的检索.而 % 开头其无法确定是一个还是多个有表 t_user，该表中有id，name，age，email四个字段，给 name 和age 字段创建联合索引。
	```sql
	create index idx_user_nameAge on t_user(name， age)；
	mysql> explain select * from t_user where name like '%aa%'；
	+----+-------------+--------+------+---------------+------+---------+------+------+-------------+
	| id | select_type | table  | type | possible_keys | key  | key_len | ref  | rows | Extra       |
	+----+-------------+--------+------+---------------+------+---------+------+------+-------------+
	|  1 | SIMPLE      | t_user | ALL  | NULL          | NULL | NULL    | NULL |    5 | Using where |
	+----+-------------+--------+------+---------------+------+---------+------+------+-------------+
	mysql> explain select * from t_user where name like '%aa'；
	+----+-------------+--------+------+---------------+------+---------+------+------+-------------+
	| id | select_type | table  | type | possible_keys | key  | key_len | ref  | rows | Extra       |
	+----+-------------+--------+------+---------------+------+---------+------+------+-------------+
	|  1 | SIMPLE      | t_user | ALL  | NULL          | NULL | NULL    | NULL |    5 | Using where |
	+----+-------------+--------+------+---------------+------+---------+------+------+-------------+
	mysql> explain select * from t_user where name like 'aa%'；
	+----+-------------+--------+-------+------------------+------------------+---------+------+------+-------------+
	| id | select_type | table  | type  | possible_keys    | key              | key_len | ref  | rows | Extra       |
	+----+-------------+--------+-------+------------------+------------------+---------+------+------+-------------+
	|  1 | SIMPLE      | t_user | range | idx_uset_nameAge | idx_uset_nameAge | 195     | NULL |    1 | Using where |
	+----+-------------+--------+-------+------------------+------------------+---------+------+------+-------------+
	```
	通过对比可以看出，'字符串%'这种方式的 like 仍然使用索引；*问题：*如何解决在 '%aa%'情况下索引不被使用的情况？

	使用覆盖索引，即查询列在索引列里，避免使用 select * 或者 不再索引中的字段
	```sql
	mysql> explain select id， name， age from t_user where name like '%aa%'；
	+----+-------------+--------+-------+---------------+------------------+---------+------+------+--------------------------+
	| id | select_type | table  | type  | possible_keys | key              | key_len | ref  | rows | Extra                    |
	+----+-------------+--------+-------+---------------+------------------+---------+------+------+--------------------------+
	|  1 | SIMPLE      | t_user | index | NULL          | idx_uset_nameAge | 200     | NULL |    5 | Using where； Using index |
	+----+-------------+--------+-------+---------------+------------------+---------+------+------+--------------------------+
	mysql> explain select id， name， age， email from t_user where name like '%aa%'；
	+----+-------------+--------+------+---------------+------+---------+------+------+-------------+
	| id | select_type | table  | type | possible_keys | key  | key_len | ref  | rows | Extra       |
	+----+-------------+--------+------+---------------+------+---------+------+------+-------------+
	|  1 | SIMPLE      | t_user | ALL  | NULL          | NULL | NULL    | NULL |    5 | Using where |
	+----+-------------+--------+------+---------------+------+---------+------+------+-------------+
	```

## 8、查询分析

### 8.1、如何进行优化

- 开启慢查询日志，设置慢查询的阙值，比如超过多少秒的日志打印出来，观察线上环境的慢sql情况
- 通过explain 和 慢sql进行相应的分析；
- show profile 查询sql在 MySQL 服务器里面执行的细节和生命周期情况；
- SQL数据库服务器参数的优化(由运维或者DBA完成)

### 8.2、查询优化

**8.2.1、小表驱动大表：即小的数据集驱动大的数据集**

- ```select * from A where id in(select id from B)```：当B表的数据集小于A表的数据集时，in 优于 exists

- ```select * from A where exists(select 1 from B where B.id=A.id)``` 当A表的数据集小于A表的数据集时， exists 优于 in

- exists：```select ... from table where exists(subquery)```

	该语法可以理解为：将主查询的数据放到子查询中做条件验证，根据验证结果来决定主查询的数据结果是否保留

	注意：
	- exists(subquery)只返回 true 和 false，因此子查询中 select * 可以是 select 1或者其他官方说法是实际执行时会忽略 select 清单.
	- exists 子查询的实际执行过程可能经过了优化；
	- exists 子查询往往也可以用条件表达式，其他子查询或者join来替代.

**8.2.2、order by：**

- （1）order by 子句尽量使用 index 方式来排序，避免使用 fileSort 方式排序。mysql 支持两种方式的排序：filesort(效率低)，index(可以扫描索引本身完成排序，效率高)。

	order by 满足两种情况下，会使用 index 方式排序：
	- order by 语句使用索引最左前列；
	- 使用 where 子句与 order by 子句条件组合满足索引最左前列.

	*尽可能在索引上完成排序操作，遵照索引建的最左前列*

- （2）如果不在索引列上排序，fileSort有两种排序算法：双路排序和单路排序

	- 双路排序：MySQL4.1之前是使用双路排序.字面意思是两次扫描磁盘，最终取得数据。读取行指针和order by 列，对他们进行排序，然后扫描已经排序好的列表，按照列表中的值重新从列表中读取对应的数据输出。取一批数据时，要对磁盘进行两次扫描.I\O本身是很耗时的.

	- 单路排序：从磁盘中读取查询需要的所有列，按照order by列在buffer 中进行排序，然后扫描排序后的列表进行输出。它的效率更块一些.避免了二次读取数据.，并且把随机I/O变成了顺序IO，但它使用更多的空间.

		- 单路排序的问题：

			在 sort_buffer 中，单路方法比双路方法要占用更多的空间，因为单路是把所有字段取出，所以有可能取出的数据超出了 sort_buffer的容量，导致了每次只能去sort_buffer容量大小的数据，进行排序(创建临时文件，多路合并)排序完再取 sort_buffer的容量大小，再排...，从而多次I\O

		- 针对单路排序：
			- 增大 sort_buffer_size 参数的设置；
			- 增大 max_length_for_sort_data 参数的设置

- （3）提高 order by 速度：
	- order by 时 select * 是一个大忌，只查询需要的字段，主要产生的影响：
		- 当查询的字段大小总和小于 max_length_for_sort_data 而且排序字段不是 text|blob 类型时，会用改进后的算法，单路排序
		- 两种算法的数据都可能超出 sort_buffer的容量，超出之后会创建临时文件进行多路合并，导致多次I/O使用单路排序算法风险更大先.
	- 尝试提高 sort_buffer的容量大小。根据系统能力来进行提高，因为这个参数是针对每个进程的.
	- 尝试提高 max_length_for_sort_data 的大小：会增加改进算法的效率.但是如果设置的太高，数据总容量超出 sort_buffer_size的概率就增大。明显症状是高磁盘IO和低的处理器使用率

- （4）总结：为排序使用索引.

	MySQL能为排序与查询使用相同的索引 index a_b_c(a，b，c)

	- order by 能使用索引最左前缀
		```
		order by a
		order by a，b
		order by a，b，c
		order by a desc， b desc， c desc
		```
	- 如果 where 子句使用索引的最左前缀为常量，order by 能使用索引：
		```
		where a= const order by b，c
		where a= const and b = const order by c
		where a= const and b> const order by b， c 
		```
	- 不能使用索引进行排序：
		```
		order by a asc， b desc， c desc --排序不一致
		where g = const order by b，c -- 丢失a 索引
		where a = const order by c -- 丢失 b 索引
		where a = const order by a，d -- d 不是索引的一部分
		where a in (...) order by b，c --对于排序来说， 多个相等的条件也是范围查询
		```
		
**8.2.3、group by：实质是先排序后进行分组，遵照索引建的最佳左前缀**

当无法使用索引列时，增大 max_length_for_sort_data 和 sort_buffer_size 参数的设置；where 高于 having，能写在 where 中的限定条件不要去使用 having 限定了

group by 的优化
```
explain select actor.first_name，actor.last_name， count(*) from film_actor inner join actor USING(actor_id)
group by film_actor.actor_id；
+----+-------------+------------+------+---------------+---------+---------+-----------------------+------+---------------------------------+
| id | select_type | table      | type | possible_keys | key     | key_len | ref                   | rows | Extra                           |
+----+-------------+------------+------+---------------+---------+---------+-----------------------+------+---------------------------------+
|  1 | SIMPLE      | actor      | ALL  | PRIMARY       | NULL    | NULL    | NULL                  |  200 | Using temporary； Using filesort |
|  1 | SIMPLE      | film_actor | ref  | PRIMARY       | PRIMARY | 2       | sakila.actor.actor_id |   13 | Using index                     |
+----+-------------+------------+------+---------------+---------+---------+-----------------------+------+---------------------------------+
```
 优化后：
```
explain select actor.first_name， actor.last_name， c.cnt from actor inner join 
(select actor_id， count(*) cnt from film_actor group by actor_id) as c using(actor_id)；
+----+-------------+------------+--------+---------------+---------+---------+------------+------+-------------+
| id | select_type | table      | type   | possible_keys | key     | key_len | ref        | rows | Extra       |
+----+-------------+------------+--------+---------------+---------+---------+------------+------+-------------+
|  1 | PRIMARY     | <derived2> | ALL    | NULL          | NULL    | NULL    | NULL       |  200 |             |
|  1 | PRIMARY     | actor      | eq_ref | PRIMARY       | PRIMARY | 2       | c.actor_id |    1 |             |
|  2 | DERIVED     | film_actor | index  | NULL          | PRIMARY | 4       | NULL       | 4354 | Using index |
+----+-------------+------------+--------+---------------+---------+---------+------------+------+-------------+
```

### 8.3、慢查询日志

- （1）慢查询日志是 MySQL提供的一种日志记录，用来记录在mysql中响应时间超过阀值的语句，具体指运行的时间超过 long_query_time 值的sql则会被记录到慢查询日志中。long_query_time 默认值为 10，意思是指运行时间超过10秒的sql

- （2）如何设置：
	- 默认情况下，MySQL数据库没有开启慢查询日志，需要手动设置该参数。当然，如果不是在调优的情况下，一般不建议开启该参数.因为其对性能会带来一定影响.
	- 查看是否开启：
		```
		show variables like '%slow_query_log%'
		mysql> show variables like 'slow_query_log%'；
		+---------------------+-----------------------------------------------------------------+
		| Variable_name       | Value                                                           |
		+---------------------+-----------------------------------------------------------------+
		| slow_query_log      | OFF                                                             |
		| slow_query_log_file | C：\ProgramData\MySQL\MySQL Server 5.5\Data\BlueFish-PC-slow.log |
		+---------------------+-----------------------------------------------------------------+
		```
		slow_query_log OFF 表示关闭

		slow_query_log_file ==> 表示慢查询日志文件位置

	- 开启慢查询日志：

		```set global slow_query_log=1```，只对当前数据库有效。如果 mysql 重启后会失效，如果需要永久生效，需要修改配置文件，在相应的配置文件中增加或修改如下配置：
		```
		slow_query_log=1
		slow_query_log_file=/var/lib/mysql/mysql-slow.log
		```
		然后重启mysql服务器。

		如果没有指定慢查询日志文件的存放路径，系统默认会给一个缺省路径 host_name-slow.log

	- 什么样的sql会被认为是慢查询sql的：主要有参数 long_query_time 来控制，默认情况下是 10s；查看参数 long_query_time 的设置：
		```
		mysql> show variables like '%long_query_time%'；
		+-----------------+-----------+
		| Variable_name   | Value     |
		+-----------------+-----------+
		| long_query_time | 10.000000 |
		+-----------------+-----------+
		```
		在mysql源码里是判断大于 long_query_time，而非大于等于的.

	- 设置慢sql查询：
		```
		set global long_query_time=3
		```
		一般在一个会话中设置之后查看不出来，需要重新连接或者重新打开一个会话或者使用 global 来查看：
		```
		mysql> set global long_query_time=3；
		mysql> show variables like '%long_query_time%'；
		+-----------------+-----------+
		| Variable_name   | Value     |
		+-----------------+-----------+
		| long_query_time | 10.000000 |
		+-----------------+-----------+
		mysql> show global variables like '%long_query_time%'；
		+-----------------+----------+
		| Variable_name   | Value    |
		+-----------------+----------+
		| long_query_time | 3.000000 |
		+-----------------+----------+
		```
	- 在配置文件中配置上述慢查询日志开关：
		```
		slow_query_log=1
		slow_query_log_file=/var/lib/mysql/mysql-slow.log
		long_query_time=3
		log_output=FILE
		```
- （3）慢查询日志分析工具：mysqldumpslow-mysql提供的日志分析工具
	- 查看帮助信息：```mysqldumpslow --help```
		```
		-s：表示按照何种方式排序
		c：访问次数
		l：锁定时间
		r：返回记录
		t：查询时间
		al：平均锁定时间
		ar：平均返回记录
		at：平均查询时间
		-t num：为返回前面多少条数据
		g：后边接正则表达式，大小写不敏感
		```
	- 常用参考：
		- 得到返回记录集最多的10个sql：```mysqldumpslow -s r -t 10 /var/lib/mysql/mysql-slow.log```
		- 得到访问次数最多的10个SQL：```mysqldumpslow -s c -t 10 /var/lib/mysql/mysql-slow.log```
		- 得到按照时间排序的前10条里面含有左连接的查询语句：```mysqldumpslow -s t -t 10 -g "left join" /var/lib/mysql/mysql-slow.log```
		- 建议在使用这些命令时集合 | 或者 more 使用，否则可能出现爆屏现象：```mysqldumpslow -s r -t 10 /var/lib/mysql/mysql-slow.log | more```

### 8.4、Show Profile

- （1）其是 mysql提供可以用来分析当前会话中语句执行的资源消耗情况.可以用于sql调优测量；

- （2）该值默认情况下是关闭的，并保存最近15次的运行结果；

	- 查看是否支持和关闭状态：
		```
		mysql> show variables like '%profil%'；
		+------------------------+-------+
		| Variable_name          | Value |
		+------------------------+-------+
		| have_profiling         | YES   |
		| profiling              | OFF   |
		| profiling_history_size | 15    |
		+------------------------+-------+
		```
	- 开启profiling：
		```
		mysql> set profiling=on； ## 开启方式
		mysql> show variables like '%profiling%'；
		+------------------------+-------+
		| Variable_name          | Value |
		+------------------------+-------+
		| have_profiling         | YES   |
		| profiling              | ON    |
		| profiling_history_size | 15    |
		+------------------------+-------+
		```
- （3）运行相关的sql，可以通过show profiles 查出所有的历史查询子句：
	```
	mysql> show profiles；
	+----------+------------+-----------------------------------------------+
	| Query_ID | Duration   | Query                                         |
	+----------+------------+-----------------------------------------------+
	|        1 |   0.002309 | show variables like 'profiling'               |
	|        2 |   0.000889 | select * from dept                            |
	|        3 | 0.07644575 | select * from book                            |
	|        4 |   1.520539 | select * from emp group by id%10 limit 150000 |
	|        5 | 1.57117675 | select * from emp group by id%20 order by 5   |
	|        6 |  0.0002625 | show profiling                                |
	+----------+------------+-----------------------------------------------+
	```
- （4）sql诊断：可以通过 show profile相关参数来查看cpu和io情况
	```
	mysql> show profile cpu，block io for query 5；
	+----------------------+----------+----------+------------+--------------+---------------+
	| Status               | Duration | CPU_user | CPU_system | Block_ops_in | Block_ops_out |
	+----------------------+----------+----------+------------+--------------+---------------+
	| starting             | 0.000112 | 0        | 0          | NULL         | NULL          |
	| checking permissions | 1.6E-5   | 0        | 0          | NULL         | NULL          |
	| Opening tables       | 4.6E-5   | 0        | 0          | NULL         | NULL          |
	| System lock          | 1.9E-5   | 0        | 0          | NULL         | NULL          |
	| init                 | 4.9E-5   | 0        | 0          | NULL         | NULL          |
	| optimizing           | 9E-6     | 0        | 0          | NULL         | NULL          |
	| statistics           | 4.5E-5   | 0        | 0          | NULL         | NULL          |
	| preparing            | 2E-5     | 0        | 0          | NULL         | NULL          |
	| Creating tmp table   | 0.000508 | 0        | 0          | NULL         | NULL          |
	| executing            | 8E-6     | 0        | 0          | NULL         | NULL          |
	| Copying to tmp table | 1.569976 | 1.57561  | 0          | NULL         | NULL          |
	| Sorting result       | 4.8E-5   | 0        | 0          | NULL         | NULL          |
	| Sending data         | 6.1E-5   | 0        | 0          | NULL         | NULL          |
	| end                  | 5E-6     | 0        | 0          | NULL         | NULL          |
	| removing tmp table   | 1.3E-5   | 0        | 0          | NULL         | NULL          |
	| end                  | 6E-6     | 0        | 0          | NULL         | NULL          |
	| query end            | 6E-6     | 0        | 0          | NULL         | NULL          |
	| closing tables       | 1.2E-5   | 0        | 0          | NULL         | NULL          |
	| freeing items        | 0.000216 | 0        | 0          | NULL         | NULL          |
	| logging slow query   | 3E-6     | 0        | 0          | NULL         | NULL          |
	| cleaning up          | 3E-6     | 0        | 0          | NULL         | NULL          |
	+----------------------+----------+----------+------------+--------------+---------------+
	```
	相关参数：
	- all：显示所有开销信息
	- block io：显示块IO相关开销
	- context switches：上下文切换相关开销
	- cpu：显示cpu相关开销信息
	- ipc：显示发送和接收相关开销信息
	- memory：显示内存相关开销信息
	- page faults：显示页面错误相关开销信息
	- source：显示和source_function，source_file，source_line 相关的开销信息
	- swaps：显示交换次数相关开销信息

- （5）开发中需要注意的点：
	- converting HEAP to MyISAM：查询结果太大，内存都不够用了往磁盘上搬；
	- creating tmp table：创建临时表(拷贝数据到临时表，用完再删除临时表)；
	- Copying to temp table on disk：把内存中临时表复制到磁盘-很危险
	- locked

## 9、count和max优化

### 9.1、max 优化
```
explain select max(payment_date) from payment；
+----+-------------+---------+------+---------------+------+---------+------+-------+-------+
| id | select_type | table   | type | possible_keys | key  | key_len | ref  | rows  | Extra |
+----+-------------+---------+------+---------------+------+---------+------+-------+-------+
|  1 | SIMPLE      | payment | ALL  | NULL          | NULL | NULL    | NULL | 15123 |       |
+----+-------------+---------+------+---------------+------+---------+------+-------+-------+
```

创建索引后：create index ix_paymentdate on payment(payment_date)；

```
explain select max(payment_date) from payment；
+----+-------------+-------+------+---------------+------+---------+------+------+------------------------------+
| id | select_type | table | type | possible_keys | key  | key_len | ref  | rows | Extra                        |
+----+-------------+-------+------+---------------+------+---------+------+------+------------------------------+
|  1 | SIMPLE      | NULL  | NULL | NULL          | NULL | NULL    | NULL | NULL | Select tables optimized away |
+----+-------------+-------+------+---------------+------+---------+------+------+------------------------------+
```

总结：一般索引的字段都是按顺序排列的，索引对于max和min之类的可以使用索引来优化；

### 9.2、count的优化

在一条SQL语句中，同时查出2006年和2007年发行的电影数量：
- 错误一：无法分开计算2006和2007年的电影数量：```sql select count(release_year='2006' or release_year='2007') from film;```
- 错误二：release_year不可能同时为2006，2007，逻辑存在错误：```sql select count(*) from film where release_year='2006' and release_year='2007';```
- 优化SQL：```sql select count(release_year='2006' or null) as '2006年'，count(release_year='2007' or null) as '2007年'	from film；```

*count 是不计算 null 的；*

### 9.3、count

**1、简介：**

- COUNT(expr)，返回SELECT语句检索的行中expr的值不为NULL的数量。结果是一个BIGINT值；
- 如果查询结果没有命中任何记录，则返回0；
- `COUNT(*) `的统计结果中，会包含值为`NULL`的行数；

**2、除了`COUNT(id)`和`COUNT(*)`以外，还可以使用`COUNT(常量)`（如COUNT(1)）来统计行数，那么这三条SQL语句有什么区别呢？**

- `COUNT(expr)`用于做行数统计，统计的是expr不为NULL的行数，那么`COUNT(列名)`、 `COUNT(常量)` 和 `COUNT(*)`这三种语法中，expr分别是`列名`、 `常量` 和 `*`；
- 那么`列名`、 `常量` 和 `*`这三个条件中，`常量` 是一个固定值，肯定不为NULL。`*`可以理解为查询整行，所以肯定也不为NULL，那么就只有`列名`的查询结果有可能是NULL了；
- `COUNT(常量)` 和 `COUNT(*)`表示的是直接查询符合条件的数据库表的行数。而`COUNT(列名)`表示的是查询符合条件的列的值不为NULL的行数；
- 除了查询得到结果集有区别之外，`COUNT(*)`相比`COUNT(常量)` 和 `COUNT(列名)`来讲，`COUNT(*)`是SQL92定义的标准统计行数的语法，因为他是标准语法，所以MySQL数据库对他进行过很多优化；

**3、COUNT(*)的优化：**

需要区分不同的存储引擎
- 因为MyISAM的锁是表级锁，同一张表上面的操作需要串行进行，所以，MyISAM做了一个简单的优化，那就是它可以把表的总行数单独记录下来，如果从一张表中使用`COUNT(*)`进行查询的时候，可以直接返回这个记录下来的数值就可以了，当然，前提是不能有where条件；MyISAM之所以可以把表中的总行数记录下来供COUNT(*)查询使用，那是因为MyISAM数据库是表级锁，不会有并发的数据库行数修改，所以查询得到的行数是准确的；
- InnoDB针对`COUNT(*)`的优化：MySQL会优先选择最小的非聚簇索引来扫表

优化的前提都是查询语句中不包含WHERE以及GROUP BY条件

**4、COUNT(*)和COUNT(1)：**

官方文档的描述：InnoDB handles SELECT COUNT(*) and SELECT COUNT(1) operations in the same way. There is no performance difference

所以说对于COUNT(1)和COUNT(*)，MySQL的优化是完全一样的，根本不存在谁比谁快！建议用`count(*)`，因为这个是SQL92定义的标准统计行数的语法；

**5、COUNT(字段)：**

进行全表扫描，然后判断指定字段的值是不是为NULL，不为NULL则累加；相比`COUNT(*)`，`COUNT(字段)`多了一个步骤就是判断所查询的字段是否为NULL，所以他的性能要比`COUNT(*)`慢；

## 10、limit 优化

- limit 常用于分页处理，时常会伴随 order by 从句使用，因此大多时候会使用 Filesorts 这样会造成大量的IO问题
	```sql
	explain select film_id，description from film order by title limit 50，5；
	+----+-------------+-------+------+---------------+------+---------+------+------+----------------+
	| id | select_type | table | type | possible_keys | key  | key_len | ref  | rows | Extra          |
	+----+-------------+-------+------+---------------+------+---------+------+------+----------------+
	|  1 | SIMPLE      | film  | ALL  | NULL          | NULL | NULL    | NULL |  883 | Using filesort |
	+----+-------------+-------+------+---------------+------+---------+------+------+----------------+
	```
- 优化：
	- 优化步骤1：使用索引的列或主键进行 order by 操作
		```sql
		explain select film_id，description from film order by film_id limit 50，5；
		+----+-------------+-------+-------+---------------+---------+---------+------+------+-------+
		| id | select_type | table | type  | possible_keys | key     | key_len | ref  | rows | Extra |
		+----+-------------+-------+-------+---------------+---------+---------+------+------+-------+
		|  1 | SIMPLE      | film  | index | NULL          | PRIMARY | 2       | NULL |   55 |       |
		+----+-------------+-------+-------+---------------+---------+---------+------+------+-------+
		```
	- 优化步骤2：记录上次返回的主键，在下次查询时使用主键过滤
		```sql
		explain select film_id，description from film where film_id>55 and film_id<=60 order by film_id limit 1，5；
		+----+-------------+-------+-------+---------------+---------+---------+------+------+-------------+
		| id | select_type | table | type  | possible_keys | key     | key_len | ref  | rows | Extra       |
		+----+-------------+-------+-------+---------------+---------+---------+------+------+-------------+
		|  1 | SIMPLE      | film  | range | PRIMARY       | PRIMARY | 2       | NULL |    5 | Using where |
		+----+-------------+-------+-------+---------------+---------+---------+------+------+-------------+
		```
		注意：这里的主键必须是有序的且中间没有缺失

## 11、索引优化

索引过多会影响查询效率，因为在查询是需要去查找索引

### 11.1、如何选择合适的列建立索引

- 在 where 从句，group by，order by，on 从句出现的列；
- 索引字段越小越好
- 离散度大的列放到联合索引的前面；

	```select * from payment where staff_id = 2 and customer_id=584;```	是 index(staff_id，customer_id)好？还是index(customer_id，staff_id)好？

	由于customer_id的离散度更大，所以应该使用index(customer_id，staff_id)，唯一值越多，说明其离散度就越大：```select count(distinct customer_id)， count(distinct staff_id) from payment;```

### 11.2、重复及冗余索引

- 重复索引是指相同的列以相同的顺序建立的同类型的索引，如下表所示 primary key 和 id 列上的索引就是重复索引
	```sql
	create table test{
		id int not null primary key，
		name varchar(10) not null，
		unique(id)
	}engine=innodb;
	```

- 冗余索引：是指多个索引的前缀列相同，或是在联合索引中包含了主键的索引，下面的例子中的key(name，id)就是一个冗余索引
	```sql
	create table test{
		id int not null primary key，
		name varchar(10) not null，
		key(name，id)
	}engine=innodb;
	```
- 查找重复或冗余索引：

	- 使用sql查询，切换到imformation_schema数据库：不包含主键等
		```
		SELECT
			a.table_schema AS 'database'，
			a.table_name AS 'tableName'，
			a.index_name AS 'index_one'，
			b.index_name AS 'index_two'，
			a.column_name AS 'repeat_column'
		FROM
			STATISTICS a
		JOIN STATISTICS b ON a.table_schema = b.table_schema
		AND a.table_name = b.table_name
		AND a.seq_in_index = b.seq_in_index
		AND a.column_name = b.column_name
		WHERE
			a.seq_in_index = 1
		AND a.index_name <> b.index_name;
		```
	- 使用工具： pt-duplicate-key-checker 检查重复索引：
		```
		pt-duplicate-key-checker \
		-uroot\
		-p  ''\
		-h 127.0.0.1
		```

### 11.3、索引维护：业务变更或表变更需要对索引进行调整或将不使用的索引删除

MySQL 目前没有记录索引的使用情况，但是在 PerconMySQL 和 MariaDB 中可以通过 index_statistics 表来查看那些索引未使用；
在 MySQL中目前只能通过慢查询日志配置pt-index-usage 工具来进行索引使用情况的分析
```
	pt-index-usage \
		-uroot -p '' \
		mysql-slow.log 
```
## 12、表的优化

### 12.1、选择合适的数据类型

- （1）如何选择合适的数据类型：
	- 使用可以存下数据的最小的数据类型；
	- 使用简单的数据类型，int 要比 varchar 类型 在mysql处理上更简单；
	- 尽可能的使用 not null 定义字段
	- 尽量少使用 text 类型，非用不可时最好考虑分表

- （2）使用 int 类存储日期和时间，利用 from_unixtime()，unix_timestamp()两个函数来进行转换from_unixtime() 将 int 类型的时间戳转换为日期格式
	```select from_unixtime(timestr) from test	unix_timestamp()``` 将正常的日期时间转换为 int 类型
	```insert into test(timestr) values (unix_timestamp('2014-06-01 13：12：00'))```

- （3）使用 bigint 来存储 ip地址，利用 inet_aton()，inet_ntoa()两个函数来进行转换；
	```
	inet_aton()：将正常的ip地址转换为 bigint 类型：
		INSERT INTO ip_add(name， ip) VALUE('ss'， inet_aton('192.168.139.129'))；
	inet_ntoa()：将 bigint 类型的数据转换为正常的ip地址
		SELECT name， inet_ntoa(ip) ip FROM ip_add
	```

### 12.2、表的范式化和反范式化

- 范式化：是指数据库的设计范式，目前一般指第三设计范式，也就是要求数据表中不存在非关键字段对任意候选
	关键字段的传递函数依赖则符合第三范式
	- 第一范式(1NF)：字段值具有原子性，不能再分(所有关系型数据库系统都满足第一范式)；例如：姓名字段，其中姓和名是一个整体，如果区分姓和名那么必须设立两个独立字段；
	- 第二范式(2NF)：一个表必须有主键，即每行数据都能被唯一的区分；备注：必须先满足第一范式；
	- 第三范式(3NF)：一个表中不能包涵其他相关表中非关键字段的信息，即数据表不能有沉余字段；备注：必须先满足第二范式；

- 反范式化：指为了查询效率的考虑把原来符合第三范式的表适当的增加冗余，以达到优化查询的目的，反范式化是一种以空间换取时间的操作

### 12.3、垂直拆分

所谓垂直拆分，就是把原来一个有很多列的表拆分成多个表，这个可以解决表的宽度问题，通常垂直拆分按照如下原则进行：
- 把不常有的字段单独存放到一个表中；
- 把大字段独立存放到一个表中；
- 把经常一起使用的字段放到一起；

### 12.4、水平拆分

是为了解决单表的数据量过大的问题，水平拆分的表每一个表的结构都是完全一致的，面临的问题：
- 跨分区表进行数据查询
- 统计及后台报表操作

## 13、系统的优化

### 13.1、操作系统配置优化

数据库是基于操作系统，大多数MySQL都是安装在 Linux 服务器上，所以对于操作系统的一些参数配置也会影响到MySQL的性能，下面是一些常用的配置项：
- 网络方面的配置，要修改 /etc/sysctl.conf 文件
	```
	#增加tcp支持的队列数
	net.ipv4.tcp_max_syn_backlog=65535
	#减少断开连接时，资源回收：
	net.ipv4.tcp_max_tw_buckets=8000
	net.ipv4.tcp_twreuse=1
	net.ipv4.tcp_tw_recycle=1
	net.ipv4.tcp_fin_timeout=10
	```

- 打开文件数的限制：

	可以使用 ulimit -a 查看目录的限制，可以修改/etc/security/limits.conf文件，增加以下内容修改打开文件数量的限制：
	```
	soft nofile 65535
	hard nofile 65535
	```
	除此之外，最好在 mysql 服务器上关闭iptables，selinux等防火墙软件

### 13.2、MySQL配置优化

MySQL可以通过启动时指定配置参数和使用配置文件两种方法进行配置，在大多数情况下配置文件位于 /etc/my.cnf 或者是 /etc/mysql/my.cnf 下，在windows系统配置文件可以是位于 C://windows/my.ini文件，MySQL查找配置文件的顺序可以通过以下方法获得：$/usr/sbin/mysqld --verbose --help | grep -A 1 'Default options'注意：如果存在多个位置存在配置文件，后面的会覆盖前面的

### 13.3、第三方配置优化

## 14、批量插入大量数据

- 创建表格：
	```sql
	-- 新建数据库
	create database bigdata;
	use bigdata;
	-- 1.dept 表格：
	CREATE TABLE `dept` (
		`id` int(10) unsigned NOT NULL AUTO_INCREMENT,
		`deptno` mediumint(8) unsigned NOT NULL DEFAULT '0',
		`dname` varchar(20) NOT NULL DEFAULT '',
		`loc` varchar(13) NOT NULL DEFAULT '',
		PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	-- 2.emp 表格
	CREATE TABLE `emp` (
		`id` int(10) unsigned NOT NULL AUTO_INCREMENT,
		`empno` mediumint(8) unsigned NOT NULL DEFAULT '0' COMMENT '编号',
		`ename` varchar(20) NOT NULL DEFAULT '' COMMENT '名字',
		`job` varchar(9) NOT NULL DEFAULT '' COMMENT '工作',
		`mgr` mediumint(8) unsigned NOT NULL DEFAULT '0' COMMENT '上级编号',
		`hirdate` date NOT NULL COMMENT '入职时间',
		`sal` decimal(7,2) NOT NULL COMMENT '薪水',
		`comm` decimal(7,2) NOT NULL COMMENT '红利',
		`deptno` mediumint(8) unsigned NOT NULL DEFAULT '0' COMMENT '部门编号',
		PRIMARY KEY (`id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	```
- 设置参数：log_bin_trust_function_creators

	创建函数如果报错：This function has none of DETERMINISTIC....由于开启过慢查询日志，因为我们开启了 bin-log，必须为function指定一个参数
	```
	show variables like '%log_bin_trust_function_creators%'；
	set global log_bin_trust_function_creators=1；
	```
	同样，该配置在mysql服务重启后将失效，也可以将其配置在配置文件中

- 创建函数，确保每条数据不一样：
	- 随机产生字符串：
		```sql
		-- 入参为随机字符串的长度
		CREATE FUNCTION rand_string(n INT)
			RETURNS VARCHAR(255)
			BEGIN
			DECLARE chars_str VARCHAR(100) DEFAULT 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
			DECLARE return_str VARCHAR(255) DEFAULT '';
			DECLARE i INT DEFAULT 0;
			WHILE i < n DO
				SET return_str = CONCAT(return_str, substring(chars_str, floor(1 + rand() * 52), 1));
				SET i = i + 1;
			END WHILE;
			RETURN return_str;
			END
		```
	- 随机产生部门编号：
		```sql
			CREATE FUNCTION rand_num()
				RETURNS INT(5)
				BEGIN
						DECLARE i INT DEFAULT 0;
						SET i = FLOOR(100 + rand() * 10);
						RETURN i;
				END;
		```
	- 要删除函数：

		drop function function_name

- .创建存储过程：

	- 往emp表插入数据的存储过程

		```sql
		CREATE PROCEDURE insert_emp(IN start INT(10), IN max_num INT(10))
			BEGIN
			DECLARE i INT DEFAULT 0;
			# set autocommit =0 即关闭自动提交,否则会造成多次提交
			SET AUTOCOMMIT = 0;
			REPEAT
				SET i = i + 1;
				INSERT INTO emp (empno, ename, job, mgr, hirdate, sal, comm, deptno) 
				VALUES ((start + i),rand_string(6), 'salesman', 0001,curdate(), 2000, 400, rand_num()
				);
			UNTIL i = max_num END REPEAT;
			COMMIT;
			END;
		```
		(2).往dept表插入数据的存储过程
		```sql
			CREATE PROCEDURE insert_dept(IN start INT(10), IN max_num INT(10))
				BEGIN
						DECLARE i INT DEFAULT 0;
						SET AUTOCOMMIT = 0;
						REPEAT
								SET i = i + 1;
								INSERT INTO dept (deptno, dname, loc) VALUES ((start + i), rand_string(10), rand_string(8));
						UNTIL i = max_num END REPEAT;
						COMMIT;
				END;
		```
- 执行存储过程：
	```
	call insert_dept(10,100);
	call insert_emp(100000,500000);
	```

# 十八、MySQL常用sql

## 1、根据A表来更新B表的数据

可以直接通过两个表关联，进行进行数据更新
```
update b join a on b.id = a.id set b.score = a.score
```

删除也可以用表关联来删除数据

`delete e,d from e join d on e.id = d.eid and e.name=''`

## 2、mysql加密解密

```sql
select aes_encrypt("需要加密的字符串","密钥");
-- 此时的加密后的字符串是二进制，可以转成十六进制的使用函数：hex()
select aes_decrypt("加密后的字符串","密钥");
-- 将十六进制转换为二进制：unhex()
```

## 3、批量插入记录时忽略错误的记录

`insert ignore into table()values(),(),();`

## 4、插入时存在就更新

`insert into table() value() on duplicate key update col=''`

## 5、使用from语句替代子查询

## 6、对字段值进行替换

```sql
SELECT
	GROUP_CONCAT( `name` SEPARATOR ',' ) 
FROM
	t_a t 
WHERE
	FIND_IN_SET ( t.id, ( SELECT REPLACE ( TRIM( BOTH '#' FROM a_id ), '#', ',' ) FROM t_b t ))
```
将t_b表中的字段a_id与t_a表关联，并拼接起来

t_a表中的数据：
```
1	喜剧
2	动作
3	爱情
4	战争
```
t_b表中的数据：
```
1	醉拳	#1#2#3#
```
需要将`#1#2#3#`其展示为对应的类型的中文

# 十九、MySQL面试题

## 1、MySQL自增主键问题

### 1.1、数据库重启对自增主键的影响

问题：一张表里有自增主键，当自增到 17后，删除了低15，16，17三条记录，再把mysql重启，在插入一条记录，该记录的ID是18还是15？
- `AUTO_INCREMENT` 列在 InnoDB 里如何工作:
	- 如果为一个表指定 AUTO_INCREMENT 列，在数据词典里的InnoDB表句柄包含一个名为自动增长计数器的计数器，被用在为该列赋新值。自动增长计数器仅被存储在主内存中，而不是存在磁盘上.
	- InnoDB使用下列算法来为包含一个名为`ai_col`的`AUTO_INCREMENT`列的表T初始化自动增长计数器：服务器启动之后，当一个用户对表T做插入之时，InnoDB执行等价如下语句的动作:`SELECT MAX(ai_col) FROM T FOR UPDATE;`
- 如果 mysql 服务重启， 因为 自动增长计数器仅被存储在主内存中，所以每次重启mysql都会重置。解决方法:
	- 先不重启mysql，继续插入表一行记录，这行记录的id为 18，
	- 重启mysql，插入表一行记录，这行记录的id为 19

### 1.2、插入语句对自增主键的影响

- `REPLACE INTO...`对主键的影响：`REPLACE INTO...`每次插入的时候如果唯一索引对应的数据已经存在，会删除原数据，然后重新插入新的数据，这也就导致id会增大，但实际预期可能是更新那条数据；

- `INSERT ... ON DUPLICATE KEY UPDATE ...`对自增主键的影响：

	每次执行时主键ID都会自动加1，但是实际记录并没有增加；

	“INSERT ... ON DUPLICATE KEY UPDATE ...”影响的行数是1为什么返回2？插入影响1行，更新影响2行，0的话就是存在且更新前后值一样

	***原因：***

	mysql主键自增有个参数 innodb_autoinc_lock_mode，他有三种可能只 0, 1, 2，mysql5.1之后加入的，默认值是 1，之前的版本可以看做都是 0

	- 数据库默认值也是1，当做简单插入（可以确定插入行数）的时候，直接将auto_increment加1，而不会去锁表，这也就提高了性能
	- 模式 0的话就是不管什么情况都是加上表锁，等语句执行完成的时候在释放，如果真的添加了记录，将 auto_increment加1
	- 模式 2，什么情况都不加 AUTO_INC锁，存在安全问题，当 binlog格式设置为 Statement模式的时候，从库同步的时候，执行结果可能跟主库不一致，问题很大；

	由于 `innodb_autoinc_lock_mode`值是1， `INSERT...ON DUPLICATE KEY UPDATE...`是简单的语句，预先就可以计算出影响的行数，所以不管是否更新，这里都将 auto_increment加1（多行的话大于1）

### 1.3、为什么用自增列作为主键

- 如果我们定义了主键(PRIMARY KEY)，那么InnoDB会选择主键作为聚集索引、如果没有显式定义主键，则InnoDB会选择第一个不包含有NULL值的唯一索引作为主键索引、如果也没有这样的唯一索引，则InnoDB会选择内置6字节长的ROWID作为隐含的聚集索引(ROWID随着行记录的写入而主键递增，这个ROWID不像ORACLE的ROWID那样可引用，是隐含的)；
- 数据记录本身被存于主索引（一颗B+Tree）的叶子节点上。这就要求同一个叶子节点内（大小为一个内存页或磁盘页）的各条数据记录按主键顺序存放，因此每当有一条新的记录插入时，MySQL会根据其主键将其插入适当的节点和位置，如果页面达到装载因子（InnoDB默认为15/16），则开辟一个新的页（节点）；
- 如果表使用自增主键，那么每次插入新的记录，记录就会顺序添加到当前索引节点的后续位置，当一页写满，就会自动开辟一个新的页
- 如果使用非自增主键（如果身份证号或学号等），由于每次插入主键的值近似于随机，因此每次新纪录都要被插到现有索引页得中间某个位置，此时MySQL不得不为了将新记录插到合适位置而移动数据，甚至目标页面可能已经被回写到磁盘上而从缓存中清掉，此时又要从磁盘上读回来，这增加了很多开销，同时频繁的移动、分页操作造成了大量的碎片，得到了不够紧凑的索引结构，后续不得不通过OPTIMIZE TABLE来重建表并优化填充页面

### 1.4、主键的使用

当mysql中的自增ID用完了，再次往里面插入数据会报什么错？

这里是以无符号int类型作为自增主键ID的，会报主键冲突：`1062 - Duplicate entry '4294967295' for key 'PRIMARY', Time: 0.000000s`；

为了尽量避免id用完，可以用bigint unsigned；

不过，还存在另一种情况，如果在创建表没有显示申明主键，会怎么办？如果是这种情况，InnoDB会自动帮你创建一个不可见的、长度为6字节的row_id，而且InnoDB 维护了一个全局的 dictsys.row_id，所以未定义主键的表都共享该row_id，每次插入一条数据，都把全局row_id当成主键id，然后全局row_id加1；该全局row_id在代码实现上使用的是bigint unsigned类型，但实际上只给row_id留了6字节，这种设计就会存在一个问题：如果全局row_id一直涨，一直涨，直到2的48幂次-1时，这个时候再+1，row_id的低48位都为0，结果在插入新一行数据时，拿到的row_id就为0，存在主键冲突的可能性

### 1.5、为什么不建议使用外键

- 性能影响
- 热更新问题
- 降低耦合度
- 数据库分库分表

## 2、Mysql唯一键问题

mysql唯一键可以为null

## 3、分页查询优化

### 3.1、一般分页查询

一般的分页查询使用简单的 limit 子句就可以实现。limit 子句声明如下：

`SELECT * FROM table LIMIT [offset,] rows | rows OFFSET offset`

LIMIT 子句可以被用于指定 SELECT 语句返回的记录数。需注意以下几点：
- 第一个参数指定第一个返回记录行的偏移量，注意从 0开始
- 第二个参数指定返回记录行的最大数目
- 如果只给定一个参数：它表示返回最大的记录行数目
- 第二个参数为 -1 表示检索从某一个偏移量到记录集的结束所有的记录行
- 初始记录行的偏移量是 0(而不是 1)

上面查询对于较少数据量查询时没有问题的，看下面一组SQL
```sql
SELECT * FROM emp  LIMIT 100, 100;
SELECT * FROM emp  LIMIT 1000, 100;
SELECT * FROM emp  LIMIT 10000, 100;
SELECT * FROM emp  LIMIT 100000, 100;
SELECT * FROM emp  LIMIT 1000000, 100;
```
随着查询偏移的增大，尤其查询偏移大于10万以后，查询时间急剧增加。

MySQL 执行此类SQL时需要先扫描到N行，然后再去取M行。对于此类操作，获取前面少数几行数据会很快，但是随着扫描的记录数越多，SQL的性能就会越差，因为N的值越大，MySQL需要扫描越多的数据来定位到具体的N行，这样耗费大量的 IO 成本和时间成本；

### 3.2、分页查询优化方案

#### 3.2.1、常见方案

- 前端加缓存、搜索，减少落到库的查询操作。比如海量商品可以放到搜索里面，使用瀑布流的方式展现数据；
- 优化 SQL 访问数据的方式，直接快速定位到要访问的数据行：通过使用覆盖索引查询返回需要的主键，再根据主键关联原表获得需要的数据

	```sql
	select t.id, t.name from t_test t join(select id from t_test limit 5000000, 100) tmp on t.id = tmp.id
	```

- 使用书签方式，记录上次查询最新/大的id值，向后追溯 M行记录

#### 3.2.2、使用 id 限定优化

这种方式假设数据表的id是连续递增的，则我们根据查询的页数和查询的记录数可以算出查询的id的范围，可以使用 `id between and` 来查询或者用`id>=`来查询；

```sql
select * from table where id>= 5000000 limit 10;
select * from table where id>= 5000000 and i <= 5000000 + 10;
```

这是前提条件：必须是主键自增的且是连续的；

#### 3.2.2、使用临时表优化

对于使用 id 限定优化中的问题，需要 id 是连续递增的，但是在一些场景下，比如使用历史表的时候，或者出现过数据缺失问题时，可以考虑使用临时存储的表来记录分页的id，使用分页的id来进行 in 查询。这样能够极大的提高传统的分页查询速度，尤其是数据量上千万的时候

#### 3.2.3、在业务上限定不可用查询早期的数据

## 4、在线修改数据库结构

percona提供了维护mysql的perconaTookit工具包，pt-online-schema-change可以完成在线修改表结构

其修改表结构原理：复制原表结构，使用触发器同步原表的数据到新表，删除新表，重命名复制出来的新表

## 5、物理删除与逻辑删除

### 5.1、物理删除

- 物理删除是真删除，所以数据库恢复起来难度很大；可以使用binlog或者数据库延时；
- 物理删除造成主键的不连续，导致分页查询变慢；
- 核心业务表的数据不建议做物理删除；
- 不删除数据又能缩小数据表体积，可以把记录转移到历史表；

### 5.2、逻辑删除

- 逻辑删除就是在数据表中添加一个字段，用字段值标记该数据已经逻辑删除，查询的时候跳过这些数据；
- 核心业务数据表，必须要采用逻辑删除；

## 6、读多写少与写多读少

### 6.1、读多写少


### 6.2、写多读少

- 如果是低价值的数据，可以使用nosql数据库来存储数据；
- 如果是高价值的数据，可以用TokuDB来作为数据库引擎保存；

### 6.3、写多读多业务场景

- 社交软件：qq、微信

## 7、如何让MySQL的MyIsam引擎支持事务

可以用lock table来实现，但是只能实现串行化隔离级别；

因为MyIsam不支持崩溃恢复，所以即使用lock table实现也是问题多多：
- ACID里面，原子性和持久性做不到；
- 隔离性只能实现基本用不上串行化；

## 8、InnoDB相关面试题

**在什么情况下InnoDB无法在线修改表结构？**
- 不支持在线修改表结构的场景：
	- 加全文索引： `create fulltext index name on table(column);`
	- 加空间索引： `alter table geom add spatial index(g);`
	- 删除主键： `alter table table_name drop primary key;`
	- 增加自增列： `alter table table_name add column id int auto_increment not null primary key;`
	- 修改类类型： `alter table table_name change c1 c1 new_type;`
	- 修改表的字符集： `alter table table_name character set=charset_name`

- 在线DDL存在问题：
	- 有部分语句不支持在线DDL；
	- 长时间DDL操作会引起严重的主从延迟；
	- 无法对DDL操作进行资源限制；

**在无法进行在线修改表结构的情况下，要如何操作？**

pt-onlinbe-schema-change [options] DSN

**InnoDB是如何实现事务的**

事务的实现方式：
- 原子性：回滚日志（Undo Log），用于记录数据修改前的状态；
- 一致性：重作日志（Redo Log），用于记录数据修改后的状态；
- 隔离性：锁，用于资源隔离，分为共享锁和排它锁；
- 持久性：重作认知（Redo Log） + 回滚日志（Undo Log）

**innodb读操作是否会阻塞写操作**

- 查询需要对资源加共享锁；
- 数据修改需要对资源加排它锁

排它锁和共享锁是不兼容

**假如两个事务执行写操作，又怎么保证并发呢：**

假如事务1和事务2都要执行update操作，事务1先update数据行的时候，先回获取行锁，锁定数据，当事务2要进行update操作的时候，也会取获取该数据行的行锁，但是已经被事务1占有，事务2只能wait；若是事务1长时间没有释放锁，事务2就会出现超时异常；若是没有索引的条件下，就获取所有行，都加上行锁，然后Mysql会再次过滤符合条件的的行并释放锁，只有符合条件的行才会继续持有锁

## 9、mysql单表最优数量

MySQL单表可以存储10亿级数据，只是这时候性能比较差，业界公认MySQL单表容量在1KW量级是最佳状态，因为这时它的BTREE索引树高在3~5之间

## 10、大数量存储方案


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
* [MySQL索引原理](https://www.cnblogs.com/bypp/p/7755307.html)
* [MySQL索引分析](https://mp.weixin.qq.com/s/CgMUV_rEQYtIRfwW12OBmA)
* [MySQL大表优化方案](https://segmentfault.com/a/1190000006158186)
* [sakila文件地址](http://dev.mysql.com/doc/sakila/en/sakila-installation.html)
* [SQL调优](http://www.cnblogs.com/Qian123/p/5666569.html)
* [SQL优化](https://mp.weixin.qq.com/s/hU2EkRW_PC3pRZ4vF4VvOw)
* [MySQL常见面试题](https://mp.weixin.qq.com/s/ZtuUg79OFLh20-HWs2Qs4A)
* [Mysql的binlog日志](https://www.cnblogs.com/martinzhang/p/3454358.html)
* [Mysql主从同步延迟](https://www.cnblogs.com/cnmenglang/p/6393769.html)
* [Mysql数据库主从](http://blog.51cto.com/wangwei007/965575)
* [MySQL事务实现](https://juejin.im/post/5ede6436518825430c3acaf4)

