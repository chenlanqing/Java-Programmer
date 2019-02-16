<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、数据库优化](#%E4%B8%80%E6%95%B0%E6%8D%AE%E5%BA%93%E4%BC%98%E5%8C%96)
  - [1、数据库优化的目的](#1%E6%95%B0%E6%8D%AE%E5%BA%93%E4%BC%98%E5%8C%96%E7%9A%84%E7%9B%AE%E7%9A%84)
  - [2、影响 mysql 性能的因素](#2%E5%BD%B1%E5%93%8D-mysql-%E6%80%A7%E8%83%BD%E7%9A%84%E5%9B%A0%E7%B4%A0)
  - [3、MySQL 性能优化](#3mysql-%E6%80%A7%E8%83%BD%E4%BC%98%E5%8C%96)
  - [4、慢查询日志包含的内容](#4%E6%85%A2%E6%9F%A5%E8%AF%A2%E6%97%A5%E5%BF%97%E5%8C%85%E5%90%AB%E7%9A%84%E5%86%85%E5%AE%B9)
  - [5、慢查询日志分析查看](#5%E6%85%A2%E6%9F%A5%E8%AF%A2%E6%97%A5%E5%BF%97%E5%88%86%E6%9E%90%E6%9F%A5%E7%9C%8B)
  - [6、分析SQL查询](#6%E5%88%86%E6%9E%90sql%E6%9F%A5%E8%AF%A2)
  - [7、索引优化](#7%E7%B4%A2%E5%BC%95%E4%BC%98%E5%8C%96)
  - [8、查询分析](#8%E6%9F%A5%E8%AF%A2%E5%88%86%E6%9E%90)
  - [9、count和max优化](#9count%E5%92%8Cmax%E4%BC%98%E5%8C%96)
  - [10、limit 优化](#10limit-%E4%BC%98%E5%8C%96)
  - [11、索引优化](#11%E7%B4%A2%E5%BC%95%E4%BC%98%E5%8C%96)
  - [12、表的优化](#12%E8%A1%A8%E7%9A%84%E4%BC%98%E5%8C%96)
  - [13、系统的优化](#13%E7%B3%BB%E7%BB%9F%E7%9A%84%E4%BC%98%E5%8C%96)
  - [14、批量插入大量数据：](#14%E6%89%B9%E9%87%8F%E6%8F%92%E5%85%A5%E5%A4%A7%E9%87%8F%E6%95%B0%E6%8D%AE)
- [参考资料](#%E5%8F%82%E8%80%83%E8%B5%84%E6%96%99)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


# 一、数据库优化

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
	show variables	like 'slow_query_log';
	set global slow_query_log_file='/home/mysql/sql_log/mysql_slow_log'; -- 慢查询日志存储的硬盘位置
	set global log_queries_not_using_indexes=on; -- 是否将未使用索引的sql记录到慢查询日志中
	set global long_query_time=1; --将超过多少秒的sql记录到慢查询日志中
```
- 执行SQL的主机信息：
	```
	# User@Host： root[root] @ localhost [127.0.0.1]
	```
- SQL的执行信息：
	```
	# Query_time： 0.002000  Lock_time： 0.001000 Rows_sent： 2  Rows_examined： 2
	```
- SQL的执行时间
	```
	# SET timestamp=1460268587;
	```
- SQL的内容

## 5、慢查询日志分析查看

- mysqldumpslow 
- pt-query-digest 工具
- 如何发现 SQL 存在问题：
	- 查询次数多且每次查询占用时间长的SQL，通常为pt-query-digest分析的前几个查询
	- IO大的SQL；注意pt-query-digest分析中的rows examine
	- 未命中索引的SQL：注意pt-query-digest分钟中的rows examine和rows send 的对比

## 6、分析SQL查询

使用 explain 查询SQL的执行计划

explain select * from customer；

id -> 表的读取顺序

select_type -> 数据读取操作的操作类型
```
+----+-------------+----------+------+---------------+------+---------+------+------+-------+
| id | select_type | table    | type | possible_keys | key  | key_len | ref  | rows | Extra |
+----+-------------+----------+------+---------------+------+---------+------+------+-------+
|  1 | SIMPLE      | customer | ALL  | NULL          | NULL | NULL    | NULL |  646 |       |
+----+-------------+----------+------+---------------+------+---------+------+------+-------+
```

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
- range：只检索给定范围的行，使用一个索引来选择行.key列显示使用了哪个索引。一般是在 where 语句中使用了 between，<，>，in 等的查询.这种范围扫描索引比全表扫描要好，因为其只需要开始于索引的某一点，而结束于另一点，不需要扫描全部索引.
- index：Full index scan， index 和 all 区别为 index 类型只遍历索引树，这通常比all快.因为索引文件通常比数据文件小.(即两者虽然都是读全表，但是index 是从索引中读取，而all是从硬盘读取)
- all：遍历全表以匹配到相应的行

**一般来说，type在查询中能保证到range级别，最好能达到 ref**

### 6.4、possible_keys、key、key_len

- possible_keys：显示可能应用在这张表上的索引，一个或者多个.查询涉及到的字段上若存在索引，则将该索引列出，但不一定被查询实际使用.
- key：实际使用的索引.如果为 NULL，则没有使用索引；查询中如果使用了覆盖索引，则该索引和查询的 select 字段重叠；message 表中字段 conversation_id 有加上索引
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
- key_len：表示索引中使用的字节数，可通过查询该列计算查询中使用的索引长度.在不损失精度的情况下，长度越短越好，key_len显示的值为该索引最大的可能长度，并非实际使用的长度。即 key_len 是根据表定义计算而得，不是通过表内检索出的。key_len 的计算规则和三个因素有关：数据类型、字符编码、是否为 NULL，如果是 null 的话需要判断是否为 null 的标识长度，所以索引字段最好不要为 null，因为 null 会使索引，索引统计和值更加复杂，并且需要额外一个字节的存储空间

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

### 6.6、rows：根据表统计信息及索引选用的情况，大致估算出找到所需记录所需要读取的行数，即有多少条记录被优化器所查询

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
- using temporary：使用了临时表保存中间结果，MySQL在对查询结果排序时使用了临时表.常见于排序 order by 和分组查询 order by 中
	```sql
	mysql> EXPLAIN SELECT * FROM message GROUP BY created_date；
	+----+-------------+---------+------+---------------+------+---------+------+------+---------------------------------+
	| id | select_type | table   | type | possible_keys | key  | key_len | ref  | rows | Extra                           |
	+----+-------------+---------+------+---------------+------+---------+------+------+---------------------------------+
	|  1 | SIMPLE      | message | ALL  | NULL          | NULL | NULL    | NULL |    1 | Using temporary； Using filesort |
	+----+-------------+---------+------+---------------+------+---------+------+------+---------------------------------+
	```
- using index：表示相应的查询操作中使用了覆盖索引，避免访问表的数据行。如果同时出现 using where，说明索引是用来执行索引键值的查找；如果没有同时出现 using where，说明索引是用来读取数据而非执行查找动作；
	- 覆盖索引：
		
		select 的数据列只用从索引中就能够取得，不必读取数据行，MySQL 可以利用索引返回 select 列表中的字段，而不必根据索引再次读取数据文件，换句话说查询列要被所建的索引覆盖。如果使用覆盖索引，要注意 select 列表只读取所需要的列，不要使用 select *，因为如果将所有字段一起做成索引会导致索引文件过大，查询性能下降.

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

表相关操作SQL参加文件：[数据库脚本.sql](https://github.com/chenlanqing/learningNote/blob/master/%E6%95%B0%E6%8D%AE%E5%BA%93/MySQL/%E6%95%B0%E6%8D%AE%E5%BA%93%E8%84%9A%E6%9C%AC-%E7%94%A8%E4%BA%8E%E7%B4%A2%E5%BC%95%E4%BC%98%E5%8C%96.md)

**7.1、单表优化：查询 category_id 为1 且 comments > 1 的情况下，views 最多的 article_id**

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

**7.2、两表优化：**

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

**7.3、三表查询：**

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

**7.4、索引失效**

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

## 14、批量插入大量数据：

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
	call insert_dept(10，100)
	call insert_emp(100000，500000);
	```




# 参考资料
- [MySQL大表优化方案](https://segmentfault.com/a/1190000006158186)
- [sakila文件地址](http://dev.mysql.com/doc/sakila/en/sakila-installation.html)
- [SQL调优](http://www.cnblogs.com/Qian123/p/5666569.html)
- [SQL优化](https://mp.weixin.qq.com/s/hU2EkRW_PC3pRZ4vF4VvOw)













