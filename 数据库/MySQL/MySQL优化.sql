#使用的数据库是 sakila
#文件地址:http://dev.mysql.com/doc/sakila/en/sakila-installation.html
#http://www.cnblogs.com/Qian123/p/5666569.html
1.数据库优化的目的:
	(1).避免页面访问出错:
		数据库连接超时
		慢查询造成页面无法加载
		阻塞造成数据无法提交
	(2).增加数据库稳定性
	(3).优化用户体验
2.从几个方面优化:
	硬件 > 系统设置 > 数据库表结构 > SQL及索引
	成本往右越低,效果往右越好
	(1).MySQL 查询优化器:MySQL有专门的优化 select 语句的优化器模块,通过计算分析系统中收集到的统计信息,
		为客户端请求的query提供其认为的最优执行计划.
	(2).MySQL 场景瓶颈:
		CPU:CPU在饱和一般发生在数据装入内存或从磁盘上读取数据的时候;
		IO:磁盘I/O瓶颈发生在装入数据远远大于内存的容量
		服务器硬件性能瓶颈:top,free,iostat 和 vmstat 查看系统的性能状态
3.使用MySQL慢查询日志对有效率问题的SQL进行监控
	show variables	like 'slow_query_log';
	set global slow_query_log_file='/home/mysql/sql_log/mysql_slow_log'; -- 慢查询日志存储的硬盘位置
	set global log_queries_not_using_indexes=on; -- 是否将未使用索引的sql记录到慢查询日志中
	set global long_query_time=1; --将超过多少秒的sql记录到慢查询日志中
4.慢查询日志包含的内容:
	(1).执行SQL的主机信息:
		# User@Host: root[root] @ localhost [127.0.0.1]
	(2).SQL的执行信息:
		# Query_time: 0.002000  Lock_time: 0.001000 Rows_sent: 2  Rows_examined: 2
	(3).SQL的执行时间
		# SET timestamp=1460268587;
	(4).SQL的内容:
5.慢查询日志分析查看:
	5.1.mysqldumpslow 
	5.2.pt-query-digest 工具
	5.3.如何发现 SQL 存在问题:
		(1).查询次数多且每次查询占用时间长的SQL
			通常为pt-query-digest分析的前几个查询
		(2).IO大的SQL;
			注意pt-query-digest分析中的rows examine
		(3).未命中索引的SQL
			注意pt-query-digest分钟中的rows examine和rows send 的对比





******************************************************************************************************************
6.分析SQL查询:使用 explain 查询SQL的执行计划
	explain select * from customer;
	+----+-------------+----------+------+---------------+------+---------+------+------+-------+
	| id | select_type | table    | type | possible_keys | key  | key_len | ref  | rows | Extra |
	+----+-------------+----------+------+---------------+------+---------+------+------+-------+
	|  1 | SIMPLE      | customer | ALL  | NULL          | NULL | NULL    | NULL |  646 |       |
	+----+-------------+----------+------+---------------+------+---------+------+------+-------+
	6.1.字段语义:
		(1).table:显示这一行的数据是关于哪张表的
		(2).type:这是重要的列,显示连接使用了何种类型.从最好到最差的连接类型为 const、eq_reg、ref、range、indexhe和all
		(3).possible_keys：显示可能应用在这张表中的索引.如果为空,没有可能的索引.可以为相关的域从where语句中选择一个合适的语句
		(4).key:实际使用的索引.如果为 null，则没有使用索引。很少的情况下，mysql会选择优化不足的索引.这种情况下,可以在
			select 语句中使用use index(indexname)来强制使用一个索引或者用ignore index（indexname）来强制mysql忽略索引
		(5).key_len:使用的索引的长度。在不损失精确性的情况下，长度越短越好.
		(6).ref:显示索引的哪一列被使用了，如果可能的话，是一个常数;
		(7).rows：mysql认为必须检查的用来返回请求数据的行数
		(8).xtra：关于mysql如何解析查询的额外信息;
	6.2.extra列返回的描述的意义:
		distinct:一旦mysql找到了与行相联合匹配的行，就不再搜索了
		not exists: mysql优化了left join，一旦它找到了匹配left join标准的行，就不再搜索了
		range checked for each record(index map:):没有找到理想的索引，因此对于从前面表中来的每一个行组合，
			mysql检查使用哪个索引，并用它来从表中返回行。这是使用索引的最慢的连接之一
		using filesort: 看到这个的时候，查询就需要优化了。mysql需要进行额外的步骤来发现如何对返回的行排序。
			它根据连接类型以及存储排序键值和匹配条件的全部行的行指针来排序全部行
		using index: 列数据是从仅仅使用了索引中的信息而没有读取实际的行动的表返回的，这发生在对表的全部的请求列都
			是同一个索引的部分的时候
		using temporary 看到这个的时候，查询需要优化了。这里，mysql需要创建一个临时表来存储结果，这通常发生在对
			不同的列集进行order by上，而不是group by上
		where used 使用了where从句来限制哪些行将与下一张表匹配或者是返回给用户。如果不想返回表中的全部行，并且
			连接类型all或index，这就会发生，或者是查询有问题不同连接类型的解释（按照效率高低的顺序排序）
		system 表只有一行：system表。这是const连接类型的特殊情况
		const:表中的一个记录的最大值能够匹配这个查询（索引可以是主键或惟一索引）。因为只有一行，这个值实际就是常数，
			因为mysql先读这个值然后把它当做常数来对待
		eq_ref:在连接中，mysql在查询时，从前面的表中，对每一个记录的联合都从表中读取一个记录，它在查询使用了索引为主
			键或惟一键的全部时使用
		ref:这个连接类型只有在查询使用了不是惟一或主键的键或者是这些类型的部分（比如，利用最左边前缀）时发生。
			对于之前的表的每一个行联合，全部记录都将从表中读出。这个类型严重依赖于根据索引匹配的记录多少—越少越好
		range:这个连接类型使用索引返回一个范围中的行，比如使用>或<查找东西时发生的情况
		index: 这个连接类型对前面的表中的每一个记录联合进行完全扫描（比all更好，因为索引一般小于表数据）
		all:这个连接类型对于前面的每一个记录联合进行完全扫描，这一般比较糟糕，应该尽量避免
7.count和max优化:
	7.1.max 优化:
		(1).explain select max(payment_date) from payment;
			+----+-------------+---------+------+---------------+------+---------+------+-------+-------+
			| id | select_type | table   | type | possible_keys | key  | key_len | ref  | rows  | Extra |
			+----+-------------+---------+------+---------------+------+---------+------+-------+-------+
			|  1 | SIMPLE      | payment | ALL  | NULL          | NULL | NULL    | NULL | 15123 |       |
			+----+-------------+---------+------+---------------+------+---------+------+-------+-------+
		(2).创建索引后:create index ix_paymentdate on payment(payment_date);
			explain select max(payment_date) from payment;
			+----+-------------+-------+------+---------------+------+---------+------+------+------------------------------+
			| id | select_type | table | type | possible_keys | key  | key_len | ref  | rows | Extra                        |
			+----+-------------+-------+------+---------------+------+---------+------+------+------------------------------+
			|  1 | SIMPLE      | NULL  | NULL | NULL          | NULL | NULL    | NULL | NULL | Select tables optimized away |
			+----+-------------+-------+------+---------------+------+---------+------+------+------------------------------+
		(3).总结:一般索引的字段都是按顺序排列的,索引对于max和min之类的可以使用索引来优化;
	7.2.count的优化:
		在一条SQL语句中,同时查出2006年和2007年发行的电影数量:
		(1).错误一:无法分开计算2006和2007年的电影数量
			select count(release_year='2006' or release_year='2007') from film;
		(2).错误二:release_year不可能同时为2006,2007,逻辑存在错误
			select count(*) from film where release_year='2006' and release_year='2007';
		(3).优化SQL:
			select count(release_year='2006' or null) as '2006年',count(release_year='2007' or null) as '2007年'
			from film;
		==> count 是不计算 null 的;
8.子查询的优化:
	通常情况下,需要把子查询优化为 join 查询,但在优化时要注意关联键是否有一对多的关系,需注意重复数据;
	如查询Sandra出演的所有影片:
	(1).SQL:
		explain select title,release_year,length from film where film_id in(
			select film_id from film_actor WHERE actor_id in (
				select actor_id from actor where first_name='sandra'
			)
		)
9.group by 的优化:
	9.1.explain select actor.first_name,actor.last_name, count(*) from film_actor inner join actor USING(actor_id)
		group by film_actor.actor_id;
		==>
		+----+-------------+------------+------+---------------+---------+---------+-----------------------+------+---------------------------------+
		| id | select_type | table      | type | possible_keys | key     | key_len | ref                   | rows | Extra                           |
		+----+-------------+------------+------+---------------+---------+---------+-----------------------+------+---------------------------------+
		|  1 | SIMPLE      | actor      | ALL  | PRIMARY       | NULL    | NULL    | NULL                  |  200 | Using temporary; Using filesort |
		|  1 | SIMPLE      | film_actor | ref  | PRIMARY       | PRIMARY | 2       | sakila.actor.actor_id |   13 | Using index                     |
		+----+-------------+------------+------+---------------+---------+---------+-----------------------+------+---------------------------------+
	9.2.优化后:
		explain select actor.first_name, actor.last_name, c.cnt from actor inner join 
		(select actor_id, count(*) cnt from film_actor group by actor_id) as c using(actor_id);
		==>
		+----+-------------+------------+--------+---------------+---------+---------+------------+------+-------------+
		| id | select_type | table      | type   | possible_keys | key     | key_len | ref        | rows | Extra       |
		+----+-------------+------------+--------+---------------+---------+---------+------------+------+-------------+
		|  1 | PRIMARY     | <derived2> | ALL    | NULL          | NULL    | NULL    | NULL       |  200 |             |
		|  1 | PRIMARY     | actor      | eq_ref | PRIMARY       | PRIMARY | 2       | c.actor_id |    1 |             |
		|  2 | DERIVED     | film_actor | index  | NULL          | PRIMARY | 4       | NULL       | 4354 | Using index |
		+----+-------------+------------+--------+---------------+---------+---------+------------+------+-------------+
10.limit 优化:
	(1).limit 常用于分页处理,时常会伴随 order by 从句使用,因此大多时候会使用 Filesorts 这样会造成大量的IO问题
		explain select film_id,description from film order by title limit 50,5;
		+----+-------------+-------+------+---------------+------+---------+------+------+----------------+
		| id | select_type | table | type | possible_keys | key  | key_len | ref  | rows | Extra          |
		+----+-------------+-------+------+---------------+------+---------+------+------+----------------+
		|  1 | SIMPLE      | film  | ALL  | NULL          | NULL | NULL    | NULL |  883 | Using filesort |
		+----+-------------+-------+------+---------------+------+---------+------+------+----------------+
	(2).优化:
		①.优化步骤1:使用索引的列或主键进行 order by 操作
			explain select film_id,description from film order by film_id limit 50,5;
			+----+-------------+-------+-------+---------------+---------+---------+------+------+-------+
			| id | select_type | table | type  | possible_keys | key     | key_len | ref  | rows | Extra |
			+----+-------------+-------+-------+---------------+---------+---------+------+------+-------+
			|  1 | SIMPLE      | film  | index | NULL          | PRIMARY | 2       | NULL |   55 |       |
			+----+-------------+-------+-------+---------------+---------+---------+------+------+-------+
		②.优化步骤2:记录上次返回的主键,在下次查询时使用主键过滤
			explain select film_id,description from film where film_id>55 and film_id<=60 order by film_id limit 1,5;
			+----+-------------+-------+-------+---------------+---------+---------+------+------+-------------+
			| id | select_type | table | type  | possible_keys | key     | key_len | ref  | rows | Extra       |
			+----+-------------+-------+-------+---------------+---------+---------+------+------+-------------+
			|  1 | SIMPLE      | film  | range | PRIMARY       | PRIMARY | 2       | NULL |    5 | Using where |
			+----+-------------+-------+-------+---------------+---------+---------+------+------+-------------+
			注意:这里的主键必须是有序的且中间没有缺失
11.索引优化:
	索引过多会影响查询效率,因为在查询是需要去查找索引
	11.1.如何选择合适的列建立索引:
		(1).在 where 从句,group by,order by,on 从句出现的列;
		(2).索引字段越小越好
		(3).离散度大的列放到联合索引的前面;
			select * from payment where staff_id = 2 and customer_id=584;
			是 index(staff_id,customer_id)好?还是index(customer_id,staff_id)好?
			==> 由于customer_id的离散度更大,所以应该使用index(customer_id,staff_id)
			唯一值越多,说明其离散度就越大
			select count(distinct customer_id), count(distinct staff_id) from payment;
	11.2.重复及冗余索引:
		11.2.1.重复索引是指相同的列以相同的顺序建立的同类型的索引,如下表所示 primary key 和 id 列上的索引就是重复索引
			create table test{
				id int not null primary key,
				name varchar(10) not null,
				unique(id)
			}engine=innodb;
		11.2.2.冗余索引:是指多个索引的前缀列相同,或是在联合索引中包含了主键的索引,下面的例子中的key(name,id)就是一个冗余索引
			create table test{
				id int not null primary key,
				name varchar(10) not null,
				key(name,id)
			}engine=innodb;
		11.2.3.查找重复或冗余索引:
			(1).使用sql查询,切换到imformation_schema数据库:不包含主键等
				SELECT
					a.table_schema AS 'database',
					a.table_name AS 'tableName',
					a.index_name AS 'index_one',
					b.index_name AS 'index_two',
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
			(2).使用工具: pt-duplicate-key-checker 检查重复索引:
				pt-duplicate-key-checker \
				-uroot\
				-p  ''\
				-h 127.0.0.1
	11.3.索引维护:业务变更或表变更需要对索引进行调整或将不使用的索引删除
		MySQL 目前没有记录索引的使用情况,但是在 PerconMySQL 和 MariaDB 中可以通过 index_statistics 表来查看那些索引未使用;
		在 MySQL中目前只能通过慢查询日志配置pt-index-usage 工具来进行索引使用情况的分析
		pt-index-usage \
			-uroot -p '' \
			mysql-slow.log 
12.表的优化:
	12.1.选择合适的数据类型:
		12.1.1.如何选择合适的数据类型:
			(1).使用可以存下数据的最小的数据类型;
			(2).使用简单的数据类型,int 要比 varchar 类型 在mysql处理上更简单;
			(3).尽可能的使用 not null 定义字段
			(4).尽量少使用 text 类型,非用不可时最好考虑分表
		12.1.2.使用 int 类存储日期和时间,利用 from_unixtime(),unix_timestamp()两个函数来进行转换
			from_unixtime() 将 int 类型的时间戳转换为日期格式
				select from_unixtime(timestr) from test
			unix_timestamp() 将正常的日期时间转换为 int 类型
				insert into test(timestr) values (unix_timestamp('2014-06-01 13:12:00'))
		12.1.3.使用 bigint 来存储 ip地址,利用 inet_aton(),inet_ntoa()两个函数来进行转换;
			inet_aton():将正常的ip地址转换为 bigint 类型:
				INSERT INTO ip_add(name, ip) VALUE('ss', inet_aton('192.168.139.129'));
			inet_ntoa():将 bigint 类型的数据转换为正常的ip地址
				SELECT name, inet_ntoa(ip) ip FROM ip_add
	12.2.表的范式化和反范式化:
		(1).范式化:是指数据库的设计范式,目前一般指第三设计范式,也就是要求数据表中不存在非关键字段对任意候选
			关键字段的传递函数依赖则符合第三范式
			第一范式(1NF)：字段值具有原子性,不能再分(所有关系型数据库系统都满足第一范式);
			 例如：姓名字段,其中姓和名是一个整体,如果区分姓和名那么必须设立两个独立字段;
			第二范式(2NF)：一个表必须有主键,即每行数据都能被唯一的区分;
			 备注：必须先满足第一范式;
			第三范式(3NF)：一个表中不能包涵其他相关表中非关键字段的信息,即数据表不能有沉余字段;
			 备注：必须先满足第二范式;
		(2).反范式化:指为了查询效率的考虑把原来符合第三范式的表适当的增加冗余,以达到优化查询的目的,
			反范式化是一种以空间换取时间的操作
	12.3.垂直拆分:
		所谓垂直拆分,就是把原来一个有很多列的表拆分成多个表,这个可以解决表的宽度问题,通常垂直拆分按照如下原则进行:
		(1).把不常有的字段单独存放到一个表中;
		(2).把大字段独立存放到一个表中;
		(3).把经常一起使用的字段放到一起;
	12.4.水平拆分:是为了解决单表的数据量过大的问题,水平拆分的表每一个表的结构都是完全一致的
		面临的问题:
			跨分区表进行数据查询
			统计及后台报表操作
13.系统的优化:
	13.1.操作系统配置优化
		数据库是基于操作系统,大多数MySQL都是安装在 Linux 服务器上,所以对于操作系统的一些参数配置也会影响到MySQL
		的性能,下面是一些常用的配置项:
		(1).网络方面的配置,要修改 /etc/sysctl.conf 文件
			#增加tcp支持的队列数
			net.ipv4.tcp_max_syn_backlog=65535
			#减少断开连接时,资源回收:
			net.ipv4.tcp_max_tw_buckets=8000
			net.ipv4.tcp_twreuse=1
			net.ipv4.tcp_tw_recycle=1
			net.ipv4.tcp_fin_timeout=10
		(2).打开文件数的限制:
			可以使用 ulimit -a 查看目录的限制,可以修改/etc/security/limits.conf文件,增加以下内容修改打开文件数量的限制:
			soft nofile 65535
			hard nofile 65535
			除此之外,最好在 mysql 服务器上关闭iptables,selinux等防火墙软件
	13.2.MySQL配置优化:
		(1).MySQL可以通过启动时指定配置参数和使用配置文件两种方法进行配置,在大多数情况下配置文件位于 /etc/my.cnf 或者是
			/etc/mysql/my.cnf下,在windows系统配置文件可以是位于 C://windows/my.ini文件,
			MySQL查找配置文件的顺序可以通过以下方法获得:
			$/usr/sbin/mysqld --verbose --help | grep -A 1 'Default options'
			注意:如果存在多个位置存在配置文件,后面的会覆盖前面的

	13.3.第三方配置优化:


















