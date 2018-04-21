<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一.概念](#%E4%B8%80%E6%A6%82%E5%BF%B5)
- [二.MySQL操作](#%E4%BA%8Cmysql%E6%93%8D%E4%BD%9C)
  - [1.连接,命令行:](#1%E8%BF%9E%E6%8E%A5%E5%91%BD%E4%BB%A4%E8%A1%8C)
  - [2.SQL操作(structure query language)](#2sql%E6%93%8D%E4%BD%9Cstructure-query-language)
  - [3.创建数据库:](#3%E5%88%9B%E5%BB%BA%E6%95%B0%E6%8D%AE%E5%BA%93)
  - [4.数据库的查询:](#4%E6%95%B0%E6%8D%AE%E5%BA%93%E7%9A%84%E6%9F%A5%E8%AF%A2)
  - [5.表的定义:](#5%E8%A1%A8%E7%9A%84%E5%AE%9A%E4%B9%89)
  - [6.数据的操作(DML)](#6%E6%95%B0%E6%8D%AE%E7%9A%84%E6%93%8D%E4%BD%9Cdml)
  - [7.校对规则:](#7%E6%A0%A1%E5%AF%B9%E8%A7%84%E5%88%99)
- [三.MySQL数据类型](#%E4%B8%89mysql%E6%95%B0%E6%8D%AE%E7%B1%BB%E5%9E%8B)
  - [1.数值型](#1%E6%95%B0%E5%80%BC%E5%9E%8B)
  - [2.日期类型](#2%E6%97%A5%E6%9C%9F%E7%B1%BB%E5%9E%8B)
  - [3.字符串类型:M表示允许的字符串长度](#3%E5%AD%97%E7%AC%A6%E4%B8%B2%E7%B1%BB%E5%9E%8Bm%E8%A1%A8%E7%A4%BA%E5%85%81%E8%AE%B8%E7%9A%84%E5%AD%97%E7%AC%A6%E4%B8%B2%E9%95%BF%E5%BA%A6)
  - [4.列类型的选择:](#4%E5%88%97%E7%B1%BB%E5%9E%8B%E7%9A%84%E9%80%89%E6%8B%A9)
- [四.列属性](#%E5%9B%9B%E5%88%97%E5%B1%9E%E6%80%A7)
- [五.查询 SQL 执行顺序:](#%E4%BA%94%E6%9F%A5%E8%AF%A2-sql-%E6%89%A7%E8%A1%8C%E9%A1%BA%E5%BA%8F)
  - [1.一般SQL的写的顺序:](#1%E4%B8%80%E8%88%ACsql%E7%9A%84%E5%86%99%E7%9A%84%E9%A1%BA%E5%BA%8F)
  - [2.数据执行的顺序:前面括号的数据表示执行顺序](#2%E6%95%B0%E6%8D%AE%E6%89%A7%E8%A1%8C%E7%9A%84%E9%A1%BA%E5%BA%8F%E5%89%8D%E9%9D%A2%E6%8B%AC%E5%8F%B7%E7%9A%84%E6%95%B0%E6%8D%AE%E8%A1%A8%E7%A4%BA%E6%89%A7%E8%A1%8C%E9%A1%BA%E5%BA%8F)
  - [3.SQL性能下降的原因:](#3sql%E6%80%A7%E8%83%BD%E4%B8%8B%E9%99%8D%E7%9A%84%E5%8E%9F%E5%9B%A0)
- [六.高级查询:](#%E5%85%AD%E9%AB%98%E7%BA%A7%E6%9F%A5%E8%AF%A2)
  - [1.连接:](#1%E8%BF%9E%E6%8E%A5)
  - [2.](#2)
- [七.MySQL 存储引擎](#%E4%B8%83mysql-%E5%AD%98%E5%82%A8%E5%BC%95%E6%93%8E)
  - [1.MySQL 的数据库引擎:](#1mysql-%E7%9A%84%E6%95%B0%E6%8D%AE%E5%BA%93%E5%BC%95%E6%93%8E)
  - [2.MyISAM 和 InnoDB 引擎的区别:](#2myisam-%E5%92%8C-innodb-%E5%BC%95%E6%93%8E%E7%9A%84%E5%8C%BA%E5%88%AB)
  - [3.mysql存储引擎比较:](#3mysql%E5%AD%98%E5%82%A8%E5%BC%95%E6%93%8E%E6%AF%94%E8%BE%83)
  - [4.查看数据库引擎:](#4%E6%9F%A5%E7%9C%8B%E6%95%B0%E6%8D%AE%E5%BA%93%E5%BC%95%E6%93%8E)
  - [5.选择合适的存储引擎:](#5%E9%80%89%E6%8B%A9%E5%90%88%E9%80%82%E7%9A%84%E5%AD%98%E5%82%A8%E5%BC%95%E6%93%8E)
- [八.高级特性:](#%E5%85%AB%E9%AB%98%E7%BA%A7%E7%89%B9%E6%80%A7)
  - [1.数据库隔离级别介绍、举例说明](#1%E6%95%B0%E6%8D%AE%E5%BA%93%E9%9A%94%E7%A6%BB%E7%BA%A7%E5%88%AB%E4%BB%8B%E7%BB%8D%E4%B8%BE%E4%BE%8B%E8%AF%B4%E6%98%8E)
  - [2.数据库水平拆分(分表)和垂直拆分(分库)](#2%E6%95%B0%E6%8D%AE%E5%BA%93%E6%B0%B4%E5%B9%B3%E6%8B%86%E5%88%86%E5%88%86%E8%A1%A8%E5%92%8C%E5%9E%82%E7%9B%B4%E6%8B%86%E5%88%86%E5%88%86%E5%BA%93)
- [九.数据库锁:](#%E4%B9%9D%E6%95%B0%E6%8D%AE%E5%BA%93%E9%94%81)
  - [1.锁:](#1%E9%94%81)
  - [2.锁的分类:](#2%E9%94%81%E7%9A%84%E5%88%86%E7%B1%BB)
  - [3.表锁(偏读):偏向 MyISAM 存储引擎,开销小,加锁快,无死锁;锁粒度大,发生的锁冲突的概率最高,并发度最低;](#3%E8%A1%A8%E9%94%81%E5%81%8F%E8%AF%BB%E5%81%8F%E5%90%91-myisam-%E5%AD%98%E5%82%A8%E5%BC%95%E6%93%8E%E5%BC%80%E9%94%80%E5%B0%8F%E5%8A%A0%E9%94%81%E5%BF%AB%E6%97%A0%E6%AD%BB%E9%94%81%E9%94%81%E7%B2%92%E5%BA%A6%E5%A4%A7%E5%8F%91%E7%94%9F%E7%9A%84%E9%94%81%E5%86%B2%E7%AA%81%E7%9A%84%E6%A6%82%E7%8E%87%E6%9C%80%E9%AB%98%E5%B9%B6%E5%8F%91%E5%BA%A6%E6%9C%80%E4%BD%8E)
  - [4.行锁:](#4%E8%A1%8C%E9%94%81)
  - [5.死锁:](#5%E6%AD%BB%E9%94%81)
  - [6.乐观锁与悲观锁:(数据库)](#6%E4%B9%90%E8%A7%82%E9%94%81%E4%B8%8E%E6%82%B2%E8%A7%82%E9%94%81%E6%95%B0%E6%8D%AE%E5%BA%93)
- [十.表分区](#%E5%8D%81%E8%A1%A8%E5%88%86%E5%8C%BA)
  - [1.表分区:](#1%E8%A1%A8%E5%88%86%E5%8C%BA)
  - [2.与分表的区别:](#2%E4%B8%8E%E5%88%86%E8%A1%A8%E7%9A%84%E5%8C%BA%E5%88%AB)
  - [3.表分区的优点:](#3%E8%A1%A8%E5%88%86%E5%8C%BA%E7%9A%84%E4%BC%98%E7%82%B9)
  - [4.表分区的限制因素:](#4%E8%A1%A8%E5%88%86%E5%8C%BA%E7%9A%84%E9%99%90%E5%88%B6%E5%9B%A0%E7%B4%A0)
  - [5.查看分区:判断 MySQL 是否支持表分区:](#5%E6%9F%A5%E7%9C%8B%E5%88%86%E5%8C%BA%E5%88%A4%E6%96%AD-mysql-%E6%98%AF%E5%90%A6%E6%94%AF%E6%8C%81%E8%A1%A8%E5%88%86%E5%8C%BA)
  - [6.MySQL 支持的分区类型:](#6mysql-%E6%94%AF%E6%8C%81%E7%9A%84%E5%88%86%E5%8C%BA%E7%B1%BB%E5%9E%8B)
  - [7.RANGE分区:](#7range%E5%88%86%E5%8C%BA)
  - [8.LIST分区:](#8list%E5%88%86%E5%8C%BA)
  - [9.Columns分区:MySQL5.5中引入的分区类型,解决了5.5版本之前range分区和list分区只支持整数分区的问题](#9columns%E5%88%86%E5%8C%BAmysql55%E4%B8%AD%E5%BC%95%E5%85%A5%E7%9A%84%E5%88%86%E5%8C%BA%E7%B1%BB%E5%9E%8B%E8%A7%A3%E5%86%B3%E4%BA%8655%E7%89%88%E6%9C%AC%E4%B9%8B%E5%89%8Drange%E5%88%86%E5%8C%BA%E5%92%8Clist%E5%88%86%E5%8C%BA%E5%8F%AA%E6%94%AF%E6%8C%81%E6%95%B4%E6%95%B0%E5%88%86%E5%8C%BA%E7%9A%84%E9%97%AE%E9%A2%98)
  - [10.HASH分区:](#10hash%E5%88%86%E5%8C%BA)
  - [11.KEY分区:](#11key%E5%88%86%E5%8C%BA)
  - [12.分区对于NULL值的处理:](#12%E5%88%86%E5%8C%BA%E5%AF%B9%E4%BA%8Enull%E5%80%BC%E7%9A%84%E5%A4%84%E7%90%86)
  - [13.分区管理:](#13%E5%88%86%E5%8C%BA%E7%AE%A1%E7%90%86)
  - [14.分区查询:](#14%E5%88%86%E5%8C%BA%E6%9F%A5%E8%AF%A2)
- [十一.分布式ID:](#%E5%8D%81%E4%B8%80%E5%88%86%E5%B8%83%E5%BC%8Fid)
  - [1.ID生成的核心需求:](#1id%E7%94%9F%E6%88%90%E7%9A%84%E6%A0%B8%E5%BF%83%E9%9C%80%E6%B1%82)
  - [2.数据库自增长序列或字段:最常见的方式,利用数据库,全库唯一](#2%E6%95%B0%E6%8D%AE%E5%BA%93%E8%87%AA%E5%A2%9E%E9%95%BF%E5%BA%8F%E5%88%97%E6%88%96%E5%AD%97%E6%AE%B5%E6%9C%80%E5%B8%B8%E8%A7%81%E7%9A%84%E6%96%B9%E5%BC%8F%E5%88%A9%E7%94%A8%E6%95%B0%E6%8D%AE%E5%BA%93%E5%85%A8%E5%BA%93%E5%94%AF%E4%B8%80)
  - [3.UUID:](#3uuid)
  - [4.Redis生成ID](#4redis%E7%94%9F%E6%88%90id)
  - [5.Twitter-Snowflake](#5twitter-snowflake)
  - [6.MongoDB的ObjectId](#6mongodb%E7%9A%84objectid)
  - [7.分布式唯一ID需要满足的条件](#7%E5%88%86%E5%B8%83%E5%BC%8F%E5%94%AF%E4%B8%80id%E9%9C%80%E8%A6%81%E6%BB%A1%E8%B6%B3%E7%9A%84%E6%9D%A1%E4%BB%B6)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

* [数据库原理](http://blog.csdn.net/albertfly/article/details/51318995)
* http://coding-geek.com/how-databases-work/
* http://blog.codinglabs.org/articles/theory-of-mysql-index.html

# 一.概念
	1.Mysql是基于C/S架构的;


# 二.MySQL操作
## 1.连接,命令行:
	mysql -hlocalhost -P3306 -uroot -p	--(-h:主机host, -P:端口,-u用户,-p:密码); --select user();查看当前用户
## 2.SQL操作(structure query language)
## 3.创建数据库:
	create database 数据库名 [数据库选项]
	(1)数据库名称规则:对于一些特点的组合,如纯数字、特殊符号等，包括MySQL的关键字，应该使用标识限定符来包裹，限定符使用反引号;
	(2)数据库选项:存在数据库文件的.opt文件中
		default-character-set=utf8
		default-collation=utf8_general_ci
## 4.数据库的查询:
	show databases
	(1)查看数据库(表)的创建语句:
		show create database db_name
	(2)数据库删除:drop database db_name;
	(3)修改数据库,修改数据库的属性:alter database db_name [修改指令] --一般就是修改数据库的属性
		修改数据库名:备份旧数据库的数据,创建数据库,将备份的数据移动到新数据库;
		alter database charset set utf8
## 5.表的定义:
	(1)查看表show table [like pattern],如:show table like 'exam_%';模糊查询like通用适用于database
	(2)查看表的结构:show create table 表名 \G---是结构进行结构化;
	(3)drop table if exists 表名/数据库名
	(4)表名的修改:rename table 旧表名 to 新表名;---支持同时修改多个表的操作;
		支持跨数据库表的重命名:rename table 当前数据库.表名 to 目标数据库.表名
	(5)修改列:alter table table_name add(增加)|drop(删除)|change(重命名列名)|modify(修改)
		alter table 表名 add(列名 列的数据类型,...);
		alter table 表名 drop 列名;
		alter table 表名 modify(列名 列的数据类型,...);
		alter table 表名 change 列名 新列名 数据类型;
		alter table 表名 change character set utf8;---设置字符集
## 6.数据的操作(DML)
	(1)插入数据:
	(2)查询(DQL):
	(3)删除数据
	(4)修改数据
## 7.校对规则:
	show variables like 'character_set_%';	查看当前数据库的校对规则
	| character_set_client    			| utf8                                      |
	| character_set_connection 		| utf8                                      |
	| character_set_database   		| gbk 			---当前数据库的默认编码;
	| character_set_filesystem 		| binary                                 |
	| character_set_results   			| utf8                                      |
	| character_set_server     			| utf8 			---服务器的编码
	| character_set_system     		| utf8          ---标识符的编码
	| character_sets_dir       			| E:\MySQL\MySQL Server 5.0\share\charsets\ |
	设置变量:set 变量=值,变量可以是上述的变量
	设置字符集:set name gbk
# 三.MySQL数据类型
## 1.数值型
	1.1.整型:
		(1)tinyint:1个字节,-128~127(有符号),0~255(无符号);
		(2)smallint:2个字节;
		(3)mediumint:3个字节;
		(4)int:4个字节;
		(5)bigint:8位;
		◆注意:是否有符号
			可在定义时,使用unsigned标识,没有符号;不写就默认有符号;
		◆定义显示宽度:
			规定数据的显示宽度:类型(M)--M表示最小显示宽度;
			需要使用前导0达到填充目的,称为zerofill;
			A:不影响数的范围;B:宽度大的不影响,不会截取;
		◆bool型:0表示false,1表示true;	tinyint(1)
	1.2.小数类型:都支持控制数值的范围;
		type(M,D)--M表示的所有位数(不包括小数点和符号),D表示允许的小数位数;整数位数只能是M-D位
		(1)float:单精度,4个字节,默认有效位数为6位左右;				
		(2)double:双精度,8个字节,默认有效数字16位左右;
		(3)decimal:定点数，decimal(M,D),M表示总位数,D表示小数位数(范围存在),M默认是10,D默认是0;			
		◆浮点数支持科学计数法:0.23E3 == 0.23 * 10^3;
		◆小数也支持zerofill和unsigned
## 2.日期类型
	2.1.年月日时分秒,datetime,显示格式:YYYY-MM-DD HH:MM:SS;8个字节
		(1)存储范围:1000.1.1 00:00:00~9999.12.31 23:59:59,
		(2)支持任意分隔符的日期,所表示的时间符合范围;但是如果出现歧义,所有不建议使用特殊分隔符;
		(3)支持两位年份,不建议使用
			70~69		1970-2069
			70~99		19**
			0~69			20**
	2.2.年月日,date,跟datetime差不多;3个字节
	2.3.时间戳,timestamp	,存储时是整型,表示时是日期时间,格式YYYY-MM-DD HH:MM:SS,4个字节
		存储范围:1970.1.1 00:00:00~2038.1.19 03:14:07
		检索列时,+0可以检索到时间戳
		支持0值:表示当前是没有规定的,如2013-04-0表示4月整个月;
	2.4.time,3个字节,范围:-838:59:59 ~838:59:59
		表示一天中的时间或时间间隔,在表示时间间隔时可以使用天来表示,格式:D HH:MM:SS
	2.5.year,1个字节,1901~2155
## 3.字符串类型:M表示允许的字符串长度
	3.1.char[M],最大长度255个字节,固定长度,M严格限定字符长度;只能存储2000个字符
	3.2.varchar[M],可变长,最大65535个字节,M表示允许的最大字符长度;自能存储4000个字符
	3.3.text:有多种类型，2^16个字节；
	3.4.其他字符串类型 enum:枚举选项量;set:集合元素
		如:create table s_1(gender enum('female','male'));
	◆真实的varchar长度:总长度65535,;
		varchar的特点:当类型数据超过255个字符时,采用2个字节表示长度;
		65533;整条记录需要一个额外的字节用于保存当前字段的null值,除非所有字段不为null,
		才可以省略该字节,无论一条记录有多个字段存在null,都使用统一的字节来表示,而不是每个字段一个字节
		列:create table s_4(a varchar(65533))character set latin1---error
			 create table s_4(a varchar(65533) not null)character set latin1--right
## 4.列类型的选择:
	(1)应该使用最精确的类型,占用的空间少
	(2)考虑应用程序语言的处理;
	(3)考虑移植兼容性;

# 四.列属性
	1.是否为空:规定一个字段的值是否可以为null,设置字段值不为空not null;
		注意:字段属性最好不用 null:-- https://my.oschina.net/leejun2005/blog/1342985
		(1).所有使用 null 值的情况,都可以通过一个有意义的值的表示,这样有利于代码的可读性和可维护性,并能从约束上增强业务数据的规范性.
		(2).null 值到非 null 值的更新无法做到原地更新,容易发生索引分裂从而影响性能;
			==> 但把 NULL 列改为 NOT NULL 带来的性能提示很小,除非确定它带来了问题,否则不要把它当成优先的优化措施,最重要的是使用的列的类型的适当性
		(3).NULL 值在 timestamp 类型下容易出问题,特别是没有启用参数 explicit_defaults_for_timestamp;
		(4).NOT IN, != 等负向条件查询在有 NULL 值的情况下返回永远为空结果,查询容易出错:
			◆ NOT IN 子查询在有 NULL 值的情况下返回永远为空结果,查询容易出错;
			◆ 单列索引不存 null 值,复合索引不存全为null的值,如果列允许为null,可能会得到"不符合预期"的结果集,如果name允许为null,索引不存储null值,
				结果集中不会包含这些记录.所以,请使用 not null 约束以及默认值;
			◆ 如果在两个字段进行拼接:比如题号+分数,首先要各字段进行非 null 判断,否则只要任意一个字段为空都会造成拼接的结果为 null;
			◆ 如果有 Null column 存在的情况下,count(Null column)需要格外注意, null 值不会参与统计
			◆ 注意 Null 字段的判断方式, column = null 将会得到错误的结果;
		(5).Null 列需要更多的存储空间:需要一个额外字节作为判断是否为 NULL 的标志位
	2.默认值属性:default value,只有在没有给字段设值的时才会使用默认值;常跟not null搭配;
	3.主键约束:primary key  ,可以唯一标识某条记录的字段或者是字段的集合;主键是跟业务逻辑无关的属性;
		设置主键:primary key( 列名)
		联合主键设置:primary key(列名1,列名2,...);
	4.自动增长:auto_increment,为每条记录提供一个唯一标识
			列名 primary key auto_increment

# 五.查询 SQL 执行顺序:
	- http://www.cnblogs.com/Qian123/p/5666569.html
	- http://blog.csdn.net/bitcarmanlee/article/details/51004767
## 1.一般SQL的写的顺序:
	SELECT
	DISTINCT <select_list>
	FROM <left_table> <join_type> JOIN <right_table>
	ON <join_condition>
	WHERE <where_condition>
	GROUP BY <group_by_list>
	HAVING <having_condition>
	ORDER BY <order_by_condition>
	LIMIT <limit_number>
## 2.数据执行的顺序:前面括号的数据表示执行顺序
![image](https://github.com/chenlanqing/learningNote/blob/master/数据库/MySQL/image/SQL执行顺序.jpg)

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
	FROM:对FROM子句中的前两个表执行笛卡尔积(Cartesian product)(交叉联接), 生成虚拟表VT1
	ON:对VT1应用ON筛选器.只有那些使<join_condition>为真的行才被插入VT2.
	OUTER(JOIN):如果指定了OUTER JOIN(相对于CROSS JOIN 或(INNER JOIN),保留表(preserved table:左外部联接把左表标记为保留表, 右外部联接把右表标记为保留表, 完全外部联接把两个表都标记为保留表)中未找到匹配的行将作为外部行添加到 VT2,生成VT3.如果FROM子句包含两个以上的表, 则对上一个联接生成的结果表和下一个表重复执行步骤1到步骤3, 直到处理完所有的表为止.
	WHERE:对VT3应用WHERE筛选器.只有使<where_condition>为true的行才被插入VT4.
	GROUP BY:按GROUP BY子句中的列列表对VT4中的行分组, 生成VT5.
	CUBE|ROLLUP:把超组(Suppergroups)插入VT5,生成VT6.
	HAVING:对VT6应用HAVING筛选器.只有使<having_condition>为true的组才会被插入VT7.
	SELECT:处理SELECT列表, 产生VT8.
	DISTINCT:将重复的行从VT8中移除, 产生VT9.
	ORDER BY:将VT9中的行按ORDER BY 子句中的列列表排序, 生成游标(VC10).
	TOP:从VC10的开始处选择指定数量或比例的行, 生成表VT11,并返回调用者;
	==> 除非你确定要有序行,否则不要指定ORDER BY 子句
## 3.SQL性能下降的原因:

# 六.高级查询:
	* 参考图片: SQL-Joins-1.jpg,SQL-Joins-2.jpg
## 1.连接:
	场景:假设两张表:emp, dept. emp表中的deptId为dept表中的主键.
	MySQL 不支持 full join
	1.1.内连接:
		(1).select * from emp inner join dept on emp.deptId=dept.id;
			查询两张表中共有的数据.
			等同于:
			select * from emp, dept where emp.deptId=dept.id
	1.2.左外连接:
		(1).select * from emp a left join dept b on a.deptId=b.id
			查询emp独有的数据和查询emp与dept共有的数据
	1.3.左连接:
		(1).select * from emp a left join dept b on a.deptId=b.id where b.id is null;
			查询emp独有的数据
	1.4.右外连接:
		(1).select * from emp a right join dept b on a.deptId=b.id;
			查询dept独有的数据和查询emp与dept共有的数据
	1.5.右外连接:
		(1).select * from emp a right join dept b on a.deptId=b.id where a.id is null;
			查询dept独有的数据
	1.6.全连接:
		(1).select * from emp a left join dept b on a.deptId=b.id
			union
			select * from emp a right join dept b on a.deptId=b.id;
			查询所有emp和dept独有和共有的数据
	1.7.全连接(去除共有数据):
		(1).select * from emp a left join dept b on a.deptId=b.id where b.id is null
			union
			select * from emp a right join dept b on a.deptId=b.id where a.id is null;
			去除两张表的共有数据,查询emp和dept分别独有的数据
	1.8.union 和 union all:联合查询
		1.8.1.union:
			用于合并两个或多个 SELECT 语句的结果集,并消去表中任何重复行. union 内部的 SELECT 语句必须拥有
			相同数量的列,列也必须拥有相似的数据类型,每条 SELECT 语句中的列的顺序必须相同
			(1).基本语法:
				select column_name from table1
				union
				select column_name from table2
		1.8.2.union all:
			用途同 union all, 但是不消除重复行.
			SELECT column_name FROM table1
			UNION ALL
			SELECT column_name FROM table2
		1.8.3.union 使用注意事项:
			如果子句中有 order by 或 limit, 需要用括号括起来,推荐放到所有子句之后,即对最终合并的结果来排序或筛选
			在子句中,order by 需要配合limit使用才有意义.如果不配合limit使用,会被语法分析器优化分析时去除
			(1).如下语句:
				select * from emp a left join dept b on a.deptId=b.id order by id desc
				union
				select * from emp a right join dept b on a.deptId=b.id order by id desc;
				==> 报错:1221 - Incorrect usage of UNION and ORDER BY
## 2.

# 七.MySQL 存储引擎
## 1.MySQL 的数据库引擎:
### 1.1.MyISAM:5.5版本之前默认存储引擎
	check table tableName  检查表
	repair table tableName 修复表
	(1).myisam 支持数据压缩:myisam pack,压缩后的表示只读的
	(2).在5.0版本之前,单表默认大小为4G,如存储大表,需要修改:max_rows和avg_row_length
		在5.0之后,默认支持的大小256TB
	(3).适用场景:
		* 非事务型应用
		* 只读类应用
		* 空间类应用(空间函数:GPS数据等)
### 1.2.InnoDB:5.5之后的版本默认存储引擎
	InnoDB使用表空间进行数据存储:innodb_file_per_table.
	对InnoDB使用独立表空间
	1.2.1.表转移步骤:把原来存在于系统表空间中的表转移到独立表空间
		(1).使用mysqldump导出所有数据库表的数据
		(2).停止mysql服务,修改参数,并删除innodB相关文件;
		(3).重启mysql服务,重建innodb系统表空间
		(4).重新导入数据.
	1.2.2.存储特性:
		(1).事务性存储引擎
		(2).完全支持事务的ACID特性
		(3).Redo Log 和Undo Log
		(4).支持行级锁,是在存储引擎实现的,可以最大程度实现并发;
		(5).支持全文索引,空间函数
	1.2.3.状态检查
		show engine innodb status
### 1.3.CSV存储引擎是基于 CSV 格式文件存储数据
	1.3.1.特性:
		(1).CSV 存储引擎因为自身文件格式的原因,所有列必须强制指定 NOT NULL;
		(2).CSV 引擎也不支持索引,不支持分区;
		(3).CSV 存储引擎也会包含一个存储表结构的 .frm 文件、一个 .csv 存储数据的文件、一个同名的元信息文件,
			该文件的扩展名为 .CSM,用来保存表的状态及表中保存的数据量
		(4).每个数据行占用一个文本行
	1.3.2.适合作为数据交换的中间表
### 1.4.Archive
	1.4.1.特性：
		(1).以zlib对表数据进行压缩,磁盘I/O更少;
		(2).数据存储在arz为后缀的文件;
		(3).只支持insert和select操作;
		(4).只允许在自增ID列上增加索引;
	1.4.2.使用场景:
		日志和数据采集类应用
### 1.5.Memory:也称为heap存储引擎,所以数据保存在内存中
	1.5.1.特性:
		(1).支持hash 和btree索引
		(2).所有字段都为固定长度 varchar(10) = char(10)
		(3).不支持blob 和 text 等大字段
		(4).使用表级锁
		(5).最大大小由max_heap_table_size参数决定,不会对已经存在的表生效.
			如果需要生效,需重启服务重建数据
	1.5.2.使用场景:
		(1).用于查找或者是映射表,例如邮编和地区的对应表
		(2).用于保存数据分析中产生的中间表;
		(3).用于缓存周期性聚合数据的结果表;
			memory数据易丢失,所以要求数据可再生
### 1.6.Federated
	1.6.1.特性:
		(1).提供了访问远程mysql服务器上表的方法;
		(2).本地不存储数据,数据全部放到远程数据库上;
		(3).本地需要保存表结构和远程服务器的连接信息;
	--> 默认禁止的,启用需要在配置文件中开启federated;
	--> 连接方法:mysql://user_name[:password]@host_name[:port]/db_name/table_name
## 2.MyISAM 和 InnoDB 引擎的区别:
	1.2.主要区别:
		(1).MyISAM 是非事务安全型的, InnoDB 是事务安全型的;
		(2).MyISAM 锁的粒度是表级锁, InnoDB 是支持行级锁的;
		(3).MyISAM 支持全文本索引,而InnoDB不支持全文索引
		(4).MyISAM 相对简单,所以在效率上要优于 InnoDB,小型应用可以考虑使用 MyISAM;
			MyISAM 更小的表空间
		(5).MyISAM 表是保存成文件的形式,在跨平台的数据转移中使用MyISAM存储会省去不少的麻烦;
		(6).InnoDB 表比 MyISAM 表更安全,可以在保证数据不丢失的情况下,切换非事务表到事务表；
	2.2.适用场景:
		(1).MyISAM 管理非事务表,它提供高速存储和检索,以及全文搜索能力,如果应用中需要执行大量的select查询,那么MyISAM是更好的选择
		(2).InnoDB 用于事务处理应用程序,具有众多特性,包括ACID事务支持.如果应用中需要执行大量的insert或update操作,
			则应该使用 InnoDB,这样可以提高多用户并发操作的性能
	==> 阿里巴巴大部分 mysql 数据库其实使用的 percona 的原型加以修改
## 3.mysql存储引擎比较:
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

## 4.查看数据库引擎:
	(1).查看引擎:
		mysql> show engines;
		+--------------------+---------+----------------------------------------------------------------+--------------+------+------------+
		| Engine             | Support | Comment                                                        | Transactions | XA   | Savepoints |
		+--------------------+---------+----------------------------------------------------------------+--------------+------+------------+
		| FEDERATED          | NO      | Federated MySQL storage engine                                 | NULL         | NULL | NULL       |
		| MRG_MYISAM         | YES     | Collection of identical MyISAM tables                          | NO           | NO   | NO         |
		| MyISAM             | YES     | MyISAM storage engine                                          | NO           | NO   | NO         |
		| BLACKHOLE          | YES     | /dev/null storage engine (anything you write to it disappears) | NO           | NO   | NO         |
		| CSV                | YES     | CSV storage engine                                             | NO           | NO   | NO         |
		| MEMORY             | YES     | Hash based, stored in memory, useful for temporary tables      | NO           | NO   | NO         |
		| ARCHIVE            | YES     | Archive storage engine                                         | NO           | NO   | NO         |
		| InnoDB             | DEFAULT | Supports transactions, row-level locking, and foreign keys     | YES          | YES  | YES        |
		| PERFORMANCE_SCHEMA | YES     | Performance Schema                                             | NO           | NO   | NO         |
		+--------------------+---------+----------------------------------------------------------------+--------------+------+------------+

	(2).查看默认存储引擎:
		mysql> show variables like '%storage_engine%';
		+------------------------+--------+
		| Variable_name          | Value  |
		+------------------------+--------+
		| default_storage_engine | InnoDB |
		| storage_engine         | InnoDB |
		+------------------------+--------+
	(3).设置存储引擎:
		可以在my.cnf配置文件中设置需要的存储引擎,这个参数放在 [mysqld] 这个字段下面的 default_storage_engine 参数值
		[mysqld]
		default_storage_engine=CSV
## 5.选择合适的存储引擎:
	几个标准:
	(1).是否需要支持事务;
	(2).是否需要使用热备;
	(3).崩溃恢复,能否接受崩溃;
	(4).是否需要外键支持;
	(5).存储的限制;
	(6).对索引和缓存的支持

# 八.高级特性:
## 1.数据库隔离级别介绍、举例说明
	* http://www.cnblogs.com/fjdingsd/p/5273008.html
	1.1.不考虑隔离性发生的问题:
		(1).脏读:指在一个事务处理过程里读取了另一个未提交的事务中的数据;
		(2).不可重复读:在对于数据库中的某个数据,一个事务范围内多次查询却返回了不同的数据值,
			这是由于在查询间隔,被另一个事务修改并提交了;
			==> 脏读是某一事务读取了另一个事务未提交的脏数据,而不可重复读则是读取了前一事务提交的数据
		(3).虚读(幻读):事务非独立执行时发生的一种现象
	1.2.事务隔离级别有4个,由低到高依次,级别越高执行效率越低
		(1).Read uncommitted(未授权读取、读未提交):
			如果一个事务已经开始写数据,则另外一个事务则不允许同时进行写操作,但允许其他事务读此行数据.
			该隔离级别可以通过“排他写锁”实现.最低级别,任何情况都无法保证
		(2).Read committed(授权读取、读提交):
			该隔离级别避免了脏读,但是却可能出现不可重复读
			读取数据的事务允许其他事务继续访问该行数据,但是未提交的写事务将会禁止其他事务访问该行
		(3).Repeatable read(可重复读取):可避免脏读、不可重复读的发生
			读取数据的事务将会禁止写事务(但允许读事务),写事务则禁止任何其他事务.
			避免了不可重复读取和脏读,但是有时可能出现幻读.这可以通过“共享读锁”和“排他写锁”实现
		(4).Serializable(序列化):
			提供严格的事务隔离.它要求事务序列化执行.事务只能一个接着一个地执行,但不能并发执行。
	1.3.隔离级别越高,越能保证数据的完整性和一致性,但是对并发性能的影响也越大.对于多数应用程序,
		可以优先考虑把数据库系统的隔离级别设为 Read Committed.它能够避免脏读取,而且具有较好的并发性能.
		可以通过悲观锁和乐观锁来控制不可重复读,幻读等并发问题.
	1.4.大多数数据库的默认级别就是 Read committed,比如 Sql Server , Oracle
		MySQL 的默认隔离级别就是 Repeatable read.
		mysql中查看事务隔离级别:select @@tx_isolation;
			set  [glogal | session]  transaction isolation level 隔离级别名称;
		    set tx_isolation=’隔离级别名称;’
			查看当前数据库的事务隔离级别:show variables like 'tx_isolation'
## 2.数据库水平拆分(分表)和垂直拆分(分库)
    * Cobar:http://blog.csdn.net/shagoo/article/details/8191346
	2.1.数据切分(Sharing):基本思想是把一个数据库切成多个部分放到不同的数据库上,从而缓解单一数据库的性能问题.
		(1).对应海量数据,如果是因为表多而数据多,适合使用垂直切分,即把关系紧密的表切分放在一个server上;
		(2).如果表不多,但是每张表的数据非常多,适合水平切分,即把表的数据根据某种规则切分到多个数据库上.
	2.2.垂直切分:
		(1).最大特点:规则简单,实施也更为方便,尤其适合各业务之间的耦合度非常低,相互影响很小,业务逻辑非常清晰的系统;
			这种系统中可以很容易做到将不同业务模块所使用的表分拆到不同的数据库中;
	2.3.水平切分:
		(1).相对垂直切分来说,稍微复杂点.因为需要将同一个表的不同数据拆分到不同的数据库中,对于应用程序来说,拆分规则本身就
			较根据表名来拆分更为复杂,后期的数据维护也会更为复杂一些
		(2).多数系统会将垂直切分和水平切分联合使用:
			先对系统做垂直切分,再针对每一小搓表的情况选择性地做水平切分,从而将整个数据库切分成一个分布式矩阵
	2.4.切分策略:
		(1).是按先垂直切分再水平切分的步骤进行的.
		(2).垂直切分的思路就是分析表间的聚合关系,把关系紧密的表放在一起
	2.5.数据库切分后事务问题:
		分布式事务和通过应用程序与数据库共同控制实现事务.
		(1).分布式事务:
			优点:交由数据库管理，简单有效
		    缺点:性能代价高，特别是shard越来越多时
		(2).由应用程序和数据库共同控制:
			原理:将一个跨多个数据库的分布式事务分拆成多个仅处于单个数据库上面的小事务,并通过应用程序来总控各个小事务.
	     	优点:性能上有优势
	     	缺点:需要应用程序在事务控制上做灵活设计.如果使用了spring的事务管理，改动起来会面临一定的困难
	2.6.跨节点Join的问题:只要是进行切分,跨节点Join的问题是不可避免的
		解决这一问题的普遍做法是分两次查询实现:在第一次查询的结果集中找出关联数据的id,根据这些id发起第二次请求得到关联数据;
	2.7.跨节点的 count,order by,group by 以及聚合函数问题:
		因为它们都需要基于全部数据集合进行计算.多数的代理都不会自动处理合并工作;
		解决方案:与解决跨节点join问题的类似,分别在各个节点上得到结果后在应用程序端进行合并
	2.8.切分后主键问题:
		2.8.1.常见的主键生成策略:一旦数据库被切分到多个物理结点上,我们将不能再依赖数据库自身的主键生成机制,
			某个分区数据库自生成的ID无法保证在全局上是唯一的
			(1).UUID:使用UUID作主键是最简单的方案,但缺点非常明显:
				由于UUID非常的长,除占用大量存储空间外,最主要的问题是在索引上,在建立索引和基于索引进行查询时都存在性能问题
			(2). 结合数据库维护一个Sequence表:
				缺点同样明显:由于所有插入任何都需要访问该表,该表很容易成为系统性能瓶颈,同时它也存在单点问题
		2.8.2.优秀的主键生成策略:
# 九.数据库锁:
## 1.锁:
	是计算协调多个进程或者线程来并发访问某一资源的机制.数据库并发的一致性和有效性
## 2.锁的分类:
	2.1.按对数据库的操作类型分:
		(1).读锁(共享锁):针对同一份数据,多个读操作可以同时进行而不相互影响;
		(2).写锁(互斥锁):当前写操作没有完成前,它会阻塞其他写锁和读锁
	2.2.从对数据操作的粒度分:
		表锁
		行锁
## 3.表锁(偏读):偏向 MyISAM 存储引擎,开销小,加锁快,无死锁;锁粒度大,发生的锁冲突的概率最高,并发度最低;
	3.1.MyISAM 在执行查询语句之前,会自动给涉及的所有表加读锁,在执行增删改操作前,会自动给涉及的表加写锁.
	3.2.MySQL的表级锁有两种:
		(1).表共享读锁
		(2).表独占写锁
	3.3.结论:在对 MyISAM 表进行操作时,会出现以下情况:
		(1).对 MyISAM 表的读操作不会阻塞其他进程对同一表的请求,但会阻塞对同一表的写请求.只有当读锁释放后,
			才会进行其他进程的写操作;
		(2).对 MyISAM 表的写操作会阻塞其他进程对同一表的读和写操作,只有当写锁释放后,才会执行其他进程的读写操作
		==> 读锁会阻塞写锁,不会阻塞读; 而写锁会把读和写都阻塞.
	3.4.查看哪些表被加锁了:
		0 -> 表示没有加锁; 1 -> 表示加锁
		mysql> show open tables;
		+--------------------+----------------------------------------------+--------+-------------+
		| Database           | Table                                        | In_use | Name_locked |
		+--------------------+----------------------------------------------+--------+-------------+
		| mysql              | time_zone_transition_type                    |      0 |           0 |
		| performance_schema | events_waits_summary_global_by_event_name    |      0 |           0 |
		| performance_schema | file_summary_by_instance                     |      0 |           0 |
		| performance_schema | setup_instruments                            |      0 |           0 |
		| mysql              | servers                                      |      0 |           0 |
		+--------------------+----------------------------------------------+--------+-------------+
	3.5.分析表锁:
		可以通过检查 table_locks_immediate 和 table_locks_waited 状态变量来分析系统上的表锁定
		mysql> show status like 'table%';
		+-----------------------+-------+
		| Variable_name         | Value |
		+-----------------------+-------+
		| Table_locks_immediate | 36    |
		| Table_locks_waited    | 0     |
		+-----------------------+-------+
		(1).table_locks_immediate:
			产生表级锁定的次数,表示可以立即获取锁的查询次数,每立即获取锁值加1;
		(2).table_locks_waited:
			出现表级锁定争用而发生等待的次数(不能立即获取锁的次数,每等待一次锁值加1).
			此值高则说明存在着较为严重的表级锁定争用情况;
		==> MyISAM 的读写锁调度是写优先,这也是 MyISAM 不适合做写为主表的引擎.
			因为写锁后,其他线程不能做任何操作,大量的更新会使查询很难得到锁,从而造成永远阻塞
## 4.行锁:
	4.1.行锁偏向 InnoDB 存储引擎,开销大,加锁慢;会出现死锁;锁的粒度最小,发生锁冲突的概率最低,并发度也最高;
		InnoDB 与 MyISAM 最大不同点是:支持事务和采用了行级锁;
	4.2.索引失效后无索引行由行锁升级为表锁.
	4.3.间隙锁:
		(1).当用范围条件而不是相等条件检索数据,并请求共享或排他锁时,InnoDB 会给符合条件的已有数据记录的索引
			项加锁;对于键值在条件范围内但并不存在的记录,叫做"间隙"
			InnoDB 也会对这个"间隙"加锁,这种锁机制就是所谓的间隙锁;
		(2).危害:
			因为在查询的执行过程通过范围查找的话,它会锁定整个范围内所得索引键值,即使这个键值不存在;
			间隙锁有一个比较致命的弱点:就是当锁定一个范围键值之后,即使某些不存在的键值也会被无辜的锁定,
			而造成在锁定的时候无法插入锁定键值范围内的任何数据.在某些场景下可能会对性能造成很大的危害.
	4.4.如何手动锁定一行:
		begin:
		select xxx for update 锁定某一行后,其他的操作会被阻塞,直到锁定行的会话commit
	4.5.InnoDB 存储引擎由于实现了行级锁,虽然在锁定机制的实现方面锁带来的性能损耗可能会比表级锁定会更高些,
		但是在整体并发处理能力方面要远远优于 MyISAM 的表级锁定.当系统并发量较高时,InnoDB的整体性能和
		MyISAM 相比会有比较明显的优势;
		但是 InnoDB 的行级锁定同样有脆弱的一面,当我们使用不当时,可能会让 InnoDB 的整体性能表现不仅不能比
		MyISAM 高,甚至可能更差
	4.6.行级锁分析:
		mysql> show status like 'innodb_row_lock%';
		+-------------------------------+-------+
		| Variable_name                 | Value |
		+-------------------------------+-------+
		| Innodb_row_lock_current_waits | 0     |
		| Innodb_row_lock_time          | 41389 |
		| Innodb_row_lock_time_avg      | 13796 |
		| Innodb_row_lock_time_max      | 20024 |
		| Innodb_row_lock_waits         | 3     |
		+-------------------------------+-------+
		Innodb_row_lock_current_waits:当前正在等待的锁定数量;
		Innodb_row_lock_time:从系统启动到现在锁定的总时间长度;(******)
		Innodb_row_lock_time_avg:每次等待所花平均时间;(******)
		Innodb_row_lock_time_max:从系统启动到现在等待最长的一次所花的时间
		Innodb_row_lock_waits:等待总次数(******)


## 5.死锁:
	5.1.什么是死锁:
		是指两个或两个以上的进程在执行过程中,因争夺资源而造成的一种互相等待的现象,若无外力作用,它们都将无法推进下去.
		此时称系统处于死锁状态或系统产生了死锁,这些永远在互相等竺的进程称为死锁进程.
		表级锁不会产生死锁.所以解决死锁主要还是针对于最常用的InnoDB.
		==> 死锁的关键在于:两个(或以上)的Session加锁的顺序不一致.
	5.2.死锁产生原因:
		死锁一般是事务相互等待对方资源,最后形成环路造成的
	5.3.分析死锁日志:SHOW ENGINE INNODB STATUS;
	5.4.死锁案例:
		-- http://blog.jobbole.com/99208/
		(1).不同表相同记录行锁冲突:事务A和事务B操作两张表，但出现循环等待锁情况
		(2).相同表记录行锁冲突.这种情况比较常见:
			遇到两个job在执行数据批量更新时,jobA处理的的id列表为[1,2,3,4],
			而job处理的id列表为[8,9,10,4,2],这样就造成了死锁
		(3).不同索引锁冲突:
			事务A在执行时,除了在二级索引加锁外,还会在聚簇索引上加锁,在聚簇索引上加锁的顺序是[1,4,2,3,5],
			而事务B执行时,只在聚簇索引上加锁,加锁顺序是[1,2,3,4,5],这样就造成了死锁的可能性.
		(4).gap锁冲突:
			innodb在RR级别下,如下的情况也会产生死锁
	5.5.避免死锁:
		(1).以固定的顺序访问表和行.比如两个job批量更新的情形,简单方法是对id列表先排序,后执行,这样就避免了交叉等待锁的情形;
			又比如将两个事务的sql顺序调整为一致,也能避免死锁.
		(2).大事务拆小.大事务更倾向于死锁,如果业务允许,将大事务拆小.
		(3).在同一个事务中,尽可能做到一次锁定所需要的所有资源,减少死锁概率
		(4).降低隔离级别.如果业务允许,将隔离级别调低也是较好的选择,比如将隔离级别从RR调整为RC,可以避免掉很多因为gap锁造成的死锁
		(5).为表添加合理的索引.可以看到如果不走索引将会为表的每一行记录添加上锁,死锁的概率大大增大
	5.6.定位死锁问题:
		(1).通过应用业务日志定位到问题代码,找到相应的事务对应的sql;
		(2).确定数据库隔离级别
## 6.乐观锁与悲观锁:(数据库)
	-- http://www.hollischuang.com/archives/934
	6.1.悲观锁:
		(1).是对数据被外界(包括本系统当前的其他事务,以及来自外部系统的事务处理)修改持保守态度(悲观);
		因此,在整个数据处理过程中,将数据处于锁定状态.悲观锁的实现,往往依靠数据库提供的锁机制;
		(2).悲观并发控制实际上是"先取锁再访问"的保守策略,为数据处理的安全提供了保证.但是在效率方面,
		处理加锁的机制会让数据库产生额外的开销,还有增加产生死锁的机会
	6.2.乐观锁:
		(1).乐观锁假设认为数据一般情况下不会造成冲突,所以在数据进行提交更新的时候,才会正式对数据的冲突与否进行检测,
			如果发现冲突了,则让返回用户错误的信息,让用户决定如何去做;
		(2).相对于悲观,在对数据库进行处理的时候,乐观锁并不会使用数据库提供的锁机制.一般的实现乐观锁的方式就是记录数据版本
			==> 数据版本:为数据增加的一个版本标识。当读取数据时,将版本标识的值一同读出,数据每更新一,同时对版本标识进行更新.
				当我们提交更新的时候,，判断数据库表对应记录的当前版本信息与第一次取出来的版本标识进行比对,
				如果数据库表当前版本号与第一次取出来的版本标识值相等,则予以更新.否则认为是过期数据;
		(3).实现数据版本有两种方式:第一种是使用版本号.第二种是使用时间戳

# 十.表分区
## 1.表分区:
	是指根据一定规则,将数据库中的一张表分解成多个更小的,容易管理的部分.
	从逻辑上看,只有一张表,但是底层却是由多个物理分区组成.
	子分区:分区表中对每个分区再次分割，又成为复合分区
## 2.与分表的区别:
	(1).分表:指的是通过一定规则,将一张表分解成多张不同的表
	(2).表与分区的区别在于:分区从逻辑上来讲只有一张表,而分表则是将一张表分解成多张表
## 3.表分区的优点:
	(1).分区表的数据可以分布在不同的物理设备上,从而高效地利用多个硬件设备.
	(2).和单个磁盘或者文件系统相比,可以存储更多数据.
	(3).优化查询.在where语句中包含分区条件时,可以只扫描一个或多个分区表来提高查询效率.
		涉及sum和count语句时,也可以在多个分区上并行处理,最后汇总结果.
	(4).分区表更容易维护.例如:想批量删除大量数据可以清除整个分区.
	(5).可以使用分区表来避免某些特殊的瓶颈.例如InnoDB的单个索引的互斥访问,ext3问价你系统的inode锁竞争等;
## 4.表分区的限制因素:
	(1).一个表最多只能有1024个分区;
	(2).MySQL5.1中,分区表达式必须是整数,或者返回整数的表达式.在MySQL5.5中提供了非整数表达式分区的支持.
	(3).如果分区字段中有主键或者唯一索引的列,那么多有主键列和唯一索引列都必须包含进来.
		即:分区字段要么不包含主键或者索引列,要么包含全部主键和索引列.
	(4).分区表中无法使用外键约束.
	(5).MySQL 的分区适用于一个表的所有数据和索引,不能只对表数据分区而不对索引分区,也不能只对索引分区而不对表分区,
		也不能只对表的一部分数据分区;
## 5.查看分区:判断 MySQL 是否支持表分区:
		mysql> show variables like '%partition%';
		+-------------------+-------+
		| Variable_name     | Value |
		+-------------------+-------+
		| have_partitioning | YES   |
		+-------------------+-------+
		1 row in set (0.00 sec)
## 6.MySQL 支持的分区类型:
	(1).RANGE分区:按照数据的区间范围分区;
	(2).LIST分区:按照List中的值分区,与 RANGE的区别是,range分区的区间范围值是连续的;
	(3).HASH分区
	(4).KEY分区
	说明:在MySQL5.1版本中,RANGE,LIST,HASH 分区要求分区键必须是 int 类型,或者通过表达式返回INT类型.
		但KEY分区的时候,可以使用其他类型的列(BLOB，TEXT类型除外)作为分区键;
## 7.RANGE分区:
	(1).利用取值范围进行分区,区间要连续并且不能互相重叠.语法如下:
		partition by range(exp)( --exp可以为列名或者表达式，比如to_date(created_date)
			partition p0 values less than(num)
		)
		例子:
			create table emp (
				id       int not null,
				store_id int not null
			)
			partition by range (store_id) (
				partition p0 values less than (10),
				partition p1 values less than (20)
			);
			上面的语句创建了emp表,并根据store_id字段进行分区,小于10的值存在分区p0中,大于等于10,小于20的值存在分区p1中.
		==> 注意:每个分区都是按顺序定义的,从最低到最高.
			上面的语句,如果将less than(10) 和less than (20)的顺序颠倒过来,那么将报错,如下:
			ERROR 1493 (HY000): VALUES LESS THAN value must be strictly increasing for each partition
	(2).RANGE分区存在问题:
		A.range 范围覆盖问题:当插入的记录中对应的分区键的值不在分区定义的范围中的时候,插入语句会失败.
			上面的例子,如果我插入一条store_id = 30的记录会怎么样呢?我们上面分区的时候,最大值是20,如果插入一条超过20的记录,会报错:
			mysql> insert into emp value(30,30);
			ERROR 1526 (HY000): Table has no partition for value 30.
		==> 解决方案:
			①.预估分区键的值,及时新增分区.
			②.设置分区的时候,使用 values less than maxvalue 子句,MAXVALUE表示最大的可能的整数值.
			③.尽量选择能够全部覆盖的字段作为分区键,比如一年的十二个月等
		B.Range分区中,分区键的值如果是NULL,将被作为一个最小值来处理
## 8.LIST分区:
	List分区是建立离散的值列表告诉数据库特定的值属于哪个分区,语法:
	partition by list(exp)( --exp为列名或者表达式
		partition p0 values in (3,5)  --值为3和5的在p0分区
	)
	例子:
		create table emp1 (
			id       int not null,
			store_id int not null
		)
			partition by list (store_id) (
			partition p0 values in (3, 5),
			partition p1 values in (2, 6, 7, 9)
			)
	==> 注意:如果插入的记录对应的分区键的值不在list分区指定的值中,将会插入失败.并且,list不能像range分区那样提供maxvalue.
## 9.Columns分区:MySQL5.5中引入的分区类型,解决了5.5版本之前range分区和list分区只支持整数分区的问题
	Columns分区可以细分为 range columns分区和 list columns分区,他们都支持整数、日期时间、字符串三大数据类型.
	-- http://www.cnblogs.com/chenmh/p/5630834.html
	(1).与 RANGE分区 和 LIST分区区别:
		针对日期字段的分区就不需要再使用函数进行转换了,例如针对date字段进行分区不需要再使用YEAR()表达式进行转换;
		COLUMN分区支持多个字段作为分区键但是不支持表达式作为分区键;
	(2).COLUMNS支持的类型:
		整形支持:tinyint,smallint,mediumint,int,bigint;不支持decimal和float
		时间类型支持:date,datetime
		字符类型支持:char,varchar,binary,varbinary;不支持text,blob
	9.1.RANGE COLUMNS分区:
		(1).日期字段分区:
			create table members(
				id int,
				joined date not NULL
			)
				partition by range columns(joined)(
				partition a values less than('1980-01-01'),
				partition b values less than('1990-01-01'),
				partition c values less than('2000-01-01'),
				partition d values less than('2010-01-01'),
				partition e values less than MAXVALUE
				);
		(2).多个字段组合分区:
			CREATE TABLE rcx (
				a INT,
				b INT
				)
			PARTITION BY RANGE COLUMNS(a,b) (
					PARTITION p0 VALUES LESS THAN (5,10),
					PARTITION p1 VALUES LESS THAN (10,20),
					PARTITION p2 VALUES LESS THAN (15,30),
					PARTITION p3 VALUES LESS THAN (MAXVALUE,MAXVALUE)
			);
			注意:多字段的分区键比较是基于数组的比较.
				①.它先用插入的数据的第一个字段值和分区的第一个值进行比较,如果插入的第一个值小于分区的第一个值
					那么就不需要比较第二个值就属于该分区
				②.如果第一个值等于分区的第一个值,开始比较第二个值同样如果第二个值小于分区的第二个值那么就属于该分区.
		==> RANGE COLUMN的多列分区第一列的分区值一定是顺序增长的,不能出现交叉值,第二列的值随便,例如以下分区就会报错:
			PARTITION BY RANGE COLUMNS(a,b) (
					PARTITION p0 VALUES LESS THAN (5,10),
					PARTITION p1 VALUES LESS THAN (10,20),
					PARTITION p2 VALUES LESS THAN (8,30), -- p2 中第一列比p1第一列的要小,所以报错
					PARTITION p3 VALUES LESS THAN (MAXVALUE,MAXVALUE)
			);
	9.2.LIST COLUMNS分区:
		(1).非整型字段分区:
			create table listvar (
				id    int      not null,
				hired datetime not null
			)
				partition by list columns (hired)
				(
				partition a values in ('1990-01-01 10:00:00', '1991-01-01 10:00:00'),
				partition b values in ('1992-01-01 10:00:00'),
				partition c values in ('1993-01-01 10:00:00'),
				partition d values in ('1994-01-01 10:00:00')
				);
			LIST COLUMNS分区对分整形字段进行分区就无需使用函数对字段处理成整形,所以对非整形字段进行分区建议选择COLUMNS分区
		(2).多字段分区:
			create table listvardou (
				id    int      not null,
				hired datetime not null
			)
				partition by list columns (id, hired)
				(
					partition a values in ( (1, '1990-01-01 10:00:00'), (1, '1991-01-01 10:00:00') ),
					partition b values in ( (2, '1992-01-01 10:00:00') ),
					partition c values in ( (3, '1993-01-01 10:00:00') ),
					partition d values in ( (4, '1994-01-01 10:00:00') )
				);

## 10.HASH分区:
	(1).主要用来分散热点读,确保数据在预先确定个数的分区中尽可能平均分布.
	(2).MySQL支持两种Hash分区:常规Hash分区和线性Hash分区
	A.常规Hash分区-使用取模算法,语法如下:
		partition by hash(store_id) partitions 4;
		上面的语句,根据store_id对4取模,决定记录存储位置.比如store_id = 234的记录,
		MOD(234,4)=2,所以会被存储在第二个分区.
	==> 常规Hash分区的优点和不足-优点:能够使数据尽可能的均匀分布;缺点:不适合分区经常变动的需求.
		如果需要增加两个分区变成6个分区,大部分数据都要重新计算分区.线性Hash分区可以解决.
	B.线性Hash分区-分区函数是一个线性的2的幂的运算法则,语法如下:
		partition by LINER hash(store_id) partitions 4;
		算法介绍:假设要保存记录的分区编号为N,num为一个非负整数,表示分割成的分区的数量,那么N可以通过以下步骤得到：
		Step 1. 找到一个大于等于num的2的幂，这个值为V，V可以通过下面公式得到：
			V = Power(2,Ceiling(Log(2,num)))
			例如:刚才设置了4个分区，num=4，Log(2,4)=2,Ceiling(2)=2,power(2,2)=4,即V=4
		Step 2. 设置N=F(column_list)&(V-1)
			例如:刚才V=4，store_id=234对应的N值，N = 234&(4-1) =2
		Step 3. 当N>=num,设置V=Ceiling(V/2),N=N&(V-1)
			例如:store_id=234,N=2<4,所以N就取值2,即可.
			假设上面算出来的N=5,那么V=Ceiling(2.5)=3,N=234&(3-1)=1,即在第一个分区.
	==> 线性Hash的优点和不足-优点:在分区维护(增加,删除,合并,拆分分区)时,MySQL能够处理得更加迅速;
		缺点:与常规Hash分区相比,线性Hash各个分区之间的数据分布不太均衡
## 11.KEY分区:
	(1).类似Hash分区,Hash分区允许使用用户自定义的表达式,但Key分区不允许使用用户自定义的表达式.
		Hash仅支持整数分区,而Key分区支持除了Blob和text的其他类型的列作为分区键.
		partition by key(exp) partitions 4; --exp是零个或多个字段名的列表
	==> key分区的时候,exp可以为空,如果为空,则默认使用主键作为分区键,没有主键的时候,会选择非空惟一键作为分区键;
## 12.分区对于NULL值的处理:
	MySQ允许分区键值为NULL,分区键可能是一个字段或者一个用户定义的表达式.
	一般情况下,MySQL在分区的时候会把 NULL 值当作零值或者一个最小值进行处理.
	注意:
		Range分区中:NULL 值被当作最小值来处理
		List分区中:NULL 值必须出现在列表中,否则不被接受
		Hash/Key分区中:NULL 值会被当作零值来处理
## 13.分区管理:
	13.1.增加分区:
		(1).RANGE分区和LIST分区:
			alter table table_name add partition (partition p0 values ...(exp))
			values后面的内容根据分区的类型不同而不同
		(2).Hash分区和Key分区:
			alter table table_name add partition partitions 8; -- 指的是新增8个分区
	13.2.删除分区:
		(1).RANGE分区和LIST分区:
			alter table table_name drop partition p0; --p0为要删除的分区名称
		==> 删除了分区,同时也将删除该分区中的所有数据.
			同时,如果删除了分区导致分区不能覆盖所有值,那么插入数据的时候会报错.
		(2).Hash分区和Key分区:
			alter table table_name coalesce partition 2; --将分区缩减到2个
	13.3.移出分区:
		alter table members remove partitioning;
		使用remove移除分区是仅仅移除分区的定义.并不会删除数据和 drop PARTITION 不一样,后者会连同数据一起删除
## 14.分区查询:
	(1).查询某张表一共有多少个分区:
		SELECT
			partition_name                   part,
			partition_expression             expr,
			partition_description            descr,
			FROM_DAYS(partition_description) lessthan_sendtime,
			table_rows
		FROM
			INFORMATION_SCHEMA.partitions
		WHERE
			TABLE_SCHEMA = SCHEMA ()
			AND TABLE_NAME = 'emp';
	(2).查看执行计划,判断查询数据是否进行了分区过滤
		mysql> explain partitions select * from emp where store_id=5;
		+----+-------------+-------+------------+------+---------------+------+---------+------+------+-------------+
		| id | select_type | table | partitions | type | possible_keys | key  | key_len | ref  | rows | Extra       |
		+----+-------------+-------+------------+------+---------------+------+---------+------+------+-------------+
		|  1 | SIMPLE      | emp   | p1         | ALL  | NULL          | NULL | NULL    | NULL |    2 | Using where |
		+----+-------------+-------+------------+------+---------------+------+---------+------+------+-------------+
		1 row in set
		上面的结果:partitions:p1 表示数据在p1分区进行检索

# 十一.分布式ID:
[分布式唯一ID](https://mp.weixin.qq.com/s/cqIK5Bv1U0mT97C7EOxmnA)

## 1.ID生成的核心需求:
	1.1.全局唯一:需要单独设置一个和业务无关的主键,专业术语叫做代理主键
		这也是为什么数据库设计范式,唯一主键是第一范式!
	1.2.趋势有序:InnoDB引擎表是基于B+树的索引组织表(IOT),每个表都需要有一个聚集索引,所有的行记录都存储在B+树的叶子节点(leaf pages of the tree)
		基于聚集索引的增、删、改、查的效率相对是最高的
		(1).如果我们定义了主键(PRIMARY KEY),那么InnoDB会选择其作为聚集索引;
		(2).如果没有显式定义主键,则InnoDB会选择第一个不包含有 NULL .值的唯一索引作为主键索引
		(3).如果也没有这样的唯一索引,则InnoDB会选择内置6字节长的ROWID作为隐含的聚集索引
			(ROWID随着行记录的写入而主键递增,这个ROWID不像ORACLE的ROWID那样可引用,是隐含的)
		==> 如果InnoDB表的数据写入顺序能和B+树索引的叶子节点顺序一致的话,这时候存取效率是最高的,也就是下面几种情况效率最高:
			Ⅰ.使用自增列(INT/BIGINT类型)做主键，这时候写入顺序是自增的，和B+数叶子节点分裂顺序一致
			Ⅱ.该表不指定自增列做主键,同时也没有可以被选为主键的唯一索引(上面的条件),这时候InnoDB会选择内置的ROWID作为主键,写入顺序和ROWID增长顺序一致
			Ⅲ.除此以外,如果一个InnoDB表又没有显示主键,又有可以被选择为主键的唯一索引,但该唯一索引可能不是递增关系时
				(例如字符串、UUID、多字段联合唯一索引的情况),该表的存取效率就会比较差
## 2.数据库自增长序列或字段:最常见的方式,利用数据库,全库唯一
	2.1.优点:
		(1).简单,代码方便,性能可以接受;
		(2).数字ID天然排序,对分页或者需要排序的结果很有帮助
	2.2.缺点:
		(1).不同数据库语法和实现不同,数据库迁移的时候或多数据库版本支持的时候需要处理;
		(2).在单个数据库或读写分离或一主多从的情况下,只有一个主库可以生成.有单点故障的风险
		(3).在性能达不到要求的情况下,比较难于扩展;
		(4).如果遇见多个系统需要合并或者涉及到数据迁移会相当痛苦; 分表分库的时候会有麻烦
	2.3.优化方案:
		针对主库单点,如果有多个Master库,则每个Master库设置的起始数字不一样,步长一样,可以是Master的个数.
## 3.UUID:
	3.1.优点:
		(1).简单,代码方便.
		(2).生成ID性能非常好,基本不会有性能问题.
		(3).全球唯一,在遇见数据迁移,系统数据合并,或者数据库变更等情况下,可以从容应对
	3.2.缺点:
		(1).没有排序,无法保证趋势递增;
		(2).UUID往往是使用字符串存储,查询的效率比较低;
		(3).存储空间比较大,如果是海量数据库,就需要考虑存储量的问题;
		(4).传输数据量大\不可读
		(5).不可读
## 4.Redis生成ID
	当使用数据库来生成ID性能不够要求的时候,可以尝试使用Redis来生成.主要依赖于Redis是单线程的,所以可以用于
	生成全局唯一ID.(使用Redis的原子操作incr 和 incrby 来实现)
	比较适合使用Redis来生成每天从0开始的流水号.
	4.1.优点:
		(1).不依赖于数据库,灵活方便,且性能优于数据库;
		(2).数字ID天然排序,对分页或者需要排序的结果很有帮助;
	4.2.缺点:
		(1).如果系统中没有Redis,还需要引入新的组件,增加系统复杂度
		(2).需要编码和配置的工作量比较大
## 5.Twitter-Snowflake
	5.1.Snowflake算法组成:
		(1).41位的时间序列(精确到毫秒,41位的长度可以使用69年)
		(2).10位的机器标识(10位的长度最多支持部署1024个节点)
		(3).12位的计数顺序号(12位的计数顺序号支持每个节点每毫秒产生4096个ID序号).最高位是符号位,始终为0
	算法示意图:
![image](https://github.com/chenlanqing/learningNote/blob/master/数据库/MySQL/image/snowflake-64bit.jpg)

	5.2.优点:
		(1).不依赖数据库等第三方系统,以服务的方式部署,稳定性更高,生成ID的性能也是非常高的;
		(2).按时间有序,毫秒数在高位,自增序列在低位,整个ID都是趋势递增的
	5.3.缺点:
		强依赖机器时钟,如果机器上时钟回拨,会导致发号重复或者服务会处于不可用状态
		--> 解决时间问题:需要关闭ntp的时间同步功能,或者当检测到ntp时间调整后,拒绝分配id
## 6.MongoDB的ObjectId
	6.1.ObjectId使用12字节的存储空间,其生成方式如下:
		|0|1|2|3|4|5|6 |7|8|9|10|11|
		|时间戳 |机器ID|PID|计数器 |
		前四个字节时间戳是从标准纪元开始的时间戳,单位为秒,有如下特性:
		(1).时间戳与后边5个字节一块,保证秒级别的唯一性;
		(2).保证插入顺序大致按时间排序
		(3).隐含了文档创建时间;
		(4).时间戳的实际值并不重要,不需要对服务器之间的时间进行同步
	时间戳保证秒级唯一,机器ID保证设计时考虑分布式,避免时钟同步,PID保证同一台服务器运行
	多个mongod实例时的唯一性,最后的计数器保证同一秒内的唯一性

## 7.分布式唯一ID需要满足的条件
	(1).高可用:不能有单点故障
	(2).全局唯一性:不能出现重复的ID号,既然是唯一标识,这是最基本的要求;
	(3).趋势递增:在MySQL InnoDB引擎中使用的是聚集索引,
		在主键的选择上面我们应该尽量使用有序的主键保证写入性能
	(4).时间有序:以时间为序,或者ID里包含时间
		这样一是可以少一个索引,二是冷热数据容易分离;
	(5).分片支持:可以控制ShardingId
	(6).单调递增:保证下一个ID一定大于上一个ID,例如事务版本号、IM增量消息、排序等特殊需求;
	(7).长度适中:不要太长，最好64bit
	(8).信息安全:如果ID是连续的,恶意用户的扒取工作就非常容易做了,直接按照顺序下载指定URL即可;
