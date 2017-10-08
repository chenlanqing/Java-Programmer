-- 数据库原理 http://blog.csdn.net/albertfly/article/details/51318995
-- http://coding-geek.com/how-databases-work/
-- http://blog.codinglabs.org/articles/theory-of-mysql-index.html
MySQL
一.概念
	1.Mysql是基于C/S架构的;
	
	
二.MySQL操作
1.连接,命令行:mysql -hlocalhost -P3306 -uroot -p	--(-h:主机host, -P:端口,-u用户,-p:密码); --select user();查看当前用户
2.SQL操作(structure query language)
3.创建数据库:create database 数据库名 [数据库选项]
	(1)数据库名称规则:对于一些特点的组合,如纯数字、特殊符号等，包括MySQL的关键字，应该使用标识限定符来包裹，限定符使用反引号:"`";
	(2)数据库选项:存在数据库文件的.opt文件中
		default-character-set=utf8
		default-collation=utf8_general_ci
4.数据库的查询:show databases
	(1)查看数据库(表)的创建语句:
		show create database db_name
	(2)数据库删除:drop database db_name;
	(3)修改数据库,修改数据库的属性:alter database db_name [修改指令] --一般就是修改数据库的属性
		修改数据库名:备份旧数据库的数据,创建数据库,将备份的数据移动到新数据库;
		alter database charset set utf8
5.表的定义:
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
6.数据的操作(DML)
	(1)插入数据:
	(2)查询(DQL):
	(3)删除数据
	(4)修改数据
7.校对规则:show variables like 'character_set_%';	查看当前数据库的校对规则
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
三.MySQL数据类型
1.数值型
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
2.日期类型
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
3.字符串类型:M表示允许的字符串长度
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
4.列类型的选择:
	(1)应该使用最精确的类型,占用的空间少
	(2)考虑应用程序语言的处理;
	(3)考虑移植兼容性;
	
四.列属性
1.是否为空:规定一个字段的值是否可以为null,设置字段值不为空not null;
2.默认值属性:default value,只有在没有给字段设值的时才会使用默认值;常跟not null搭配;
3.主键约束:primary key  ,可以唯一标识某条记录的字段或者是字段的集合;主键是跟业务逻辑无关的属性;
	设置主键:primary key( 列名)
	联合主键设置:primary key(列名1,列名2,...);
4.自动增长:auto_increment,为每条记录提供一个唯一标识
		列名 primary key auto_increment

五.查询 SQL 执行顺序:
--http://www.cnblogs.com/Qian123/p/5666569.html
-- http://blog.csdn.net/bitcarmanlee/article/details/51004767
1.一般SQL的写的顺序:
	SELECT 
	DISTINCT <select_list>
	FROM <left_table> <join_type> JOIN <right_table>
	ON <join_condition>
	WHERE <where_condition>
	GROUP BY <group_by_list>
	HAVING <having_condition>
	ORDER BY <order_by_condition>
	LIMIT <limit_number>
2.数据执行的顺序:前面括号的数据表示执行顺序
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
3.SQL性能下降的原因:
	
六.高级查询:"参考图片: SQL-Joins-1.jpg,SQL-Joins-2.jpg"
1.连接:
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
2.

七.MySQL 存储引擎	
1.MySQL 的数据库引擎:MyISAM 和 InnoDB 引擎的区别:
	1.1.主要区别:
		(1).MyISAM 是非事务安全型的, InnoDB 是事务安全型的;
		(2).MyISAM 锁的粒度是表级锁, InnoDB 是支持行级锁的;
		(3).MyISAM 支持全文本索引,而InnoDB不支持全文索引
		(4).MyISAM 相对简单,所以在效率上要优于 InnoDB,小型应用可以考虑使用 MyISAM;
			MyISAM 更小的表空间
		(5).MyISAM 表是保存成文件的形式,在跨平台的数据转移中使用MyISAM存储会省去不少的麻烦;
		(6).InnoDB 表比 MyISAM 表更安全,可以在保证数据不丢失的情况下,切换非事务表到事务表；
	1.2.适用场景:
		(1).MyISAM 管理非事务表,它提供高速存储和检索,以及全文搜索能力,如果应用中需要执行大量的select查询,那么MyISAM是更好的选择
		(2).InnoDB 用于事务处理应用程序,具有众多特性,包括ACID事务支持.如果应用中需要执行大量的insert或update操作,
			则应该使用 InnoDB,这样可以提高多用户并发操作的性能
	==> 阿里巴巴大部分 mysql 数据库其实使用的 percona 的原型加以修改

2.查看数据库引擎:
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

	(2).查看存储引擎:
		mysql> show variables like '%storage_engine%';
		+------------------------+--------+
		| Variable_name          | Value  |
		+------------------------+--------+
		| default_storage_engine | InnoDB |
		| storage_engine         | InnoDB |
		+------------------------+--------+
八.高级特性:
1.数据库隔离级别介绍、举例说明:http://www.cnblogs.com/fjdingsd/p/5273008.html
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

2.数据库水平拆分(分表)和垂直拆分(分库)
    -- Cobar:http://blog.csdn.net/shagoo/article/details/8191346
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
九.数据库锁:
1.锁:是计算协调多个进程或者线程来并发访问某一资源的机制.
	数据库并发的一致性和有效性
2.锁的分类:
	2.1.按对数据库的操作类型分:
		(1).读锁(共享锁):针对同一份数据,多个读操作可以同时进行而不相互影响;
		(2).写锁(互斥锁):当前写操作没有完成前,它会阻塞其他写锁和读锁
	2.2.从对数据操作的粒度分:
		表锁
		行锁
3.表锁(偏读):偏向 MyISAM 存储引擎,开销小,加锁快,无死锁;锁粒度大,发生的锁冲突的概率最高,并发度最低;
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
4.







3.死锁:
	3.1.什么是死锁:
		是指两个或两个以上的进程在执行过程中,因争夺资源而造成的一种互相等待的现象,若无外力作用,它们都将无法推进下去.
		此时称系统处于死锁状态或系统产生了死锁,这些永远在互相等竺的进程称为死锁进程.
		表级锁不会产生死锁.所以解决死锁主要还是针对于最常用的InnoDB.
		==> 死锁的关键在于:两个(或以上)的Session加锁的顺序不一致.
	3.2.死锁产生原因:
		死锁一般是事务相互等待对方资源,最后形成环路造成的
	3.3.分析死锁日志:SHOW ENGINE INNODB STATUS;
	3.4.死锁案例:
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
	3.5.避免死锁:
		(1).以固定的顺序访问表和行.比如两个job批量更新的情形,简单方法是对id列表先排序,后执行,这样就避免了交叉等待锁的情形;
			又比如将两个事务的sql顺序调整为一致,也能避免死锁.
		(2).大事务拆小.大事务更倾向于死锁,如果业务允许,将大事务拆小.
		(3).在同一个事务中,尽可能做到一次锁定所需要的所有资源,减少死锁概率
		(4).降低隔离级别.如果业务允许,将隔离级别调低也是较好的选择,比如将隔离级别从RR调整为RC,可以避免掉很多因为gap锁造成的死锁
		(5).为表添加合理的索引.可以看到如果不走索引将会为表的每一行记录添加上锁,死锁的概率大大增大
	3.6.定位死锁问题:
		(1).通过应用业务日志定位到问题代码,找到相应的事务对应的sql;
		(2).确定数据库隔离级别
4.乐观锁与悲观锁:(数据库)
	-- http://www.hollischuang.com/archives/934
	4.1.悲观锁:
		(1).是对数据被外界(包括本系统当前的其他事务,以及来自外部系统的事务处理)修改持保守态度(悲观);
		因此,在整个数据处理过程中,将数据处于锁定状态.悲观锁的实现,往往依靠数据库提供的锁机制;
		(2).悲观并发控制实际上是"先取锁再访问"的保守策略,为数据处理的安全提供了保证.但是在效率方面,
		处理加锁的机制会让数据库产生额外的开销,还有增加产生死锁的机会
	4.2.乐观锁:
		(1).乐观锁假设认为数据一般情况下不会造成冲突,所以在数据进行提交更新的时候,才会正式对数据的冲突与否进行检测,
			如果发现冲突了,则让返回用户错误的信息,让用户决定如何去做;
		(2).相对于悲观,在对数据库进行处理的时候,乐观锁并不会使用数据库提供的锁机制.一般的实现乐观锁的方式就是记录数据版本
			==> 数据版本:为数据增加的一个版本标识。当读取数据时,将版本标识的值一同读出,数据每更新一,同时对版本标识进行更新.
				当我们提交更新的时候,，判断数据库表对应记录的当前版本信息与第一次取出来的版本标识进行比对,
				如果数据库表当前版本号与第一次取出来的版本标识值相等,则予以更新.否则认为是过期数据;
		(3).实现数据版本有两种方式:第一种是使用版本号.第二种是使用时间戳


13.表分区? 
	13.1.表分区:
		是指根据一定规则,将数据库中的一张表分解成多个更小的,容易管理的部分.
		从逻辑上看,只有一张表,但是底层却是由多个物理分区组成.
		子分区:分区表中对每个分区再次分割，又成为复合分区
	13.2.与分表的区别:
		(1).分表:指的是通过一定规则,将一张表分解成多张不同的表
		(2).表与分区的区别在于:分区从逻辑上来讲只有一张表,而分表则是将一张表分解成多张表
	13.3.表分区的优点:
		(1).分区表的数据可以分布在不同的物理设备上,从而高效地利用多个硬件设备.
		(2).和单个磁盘或者文件系统相比,可以存储更多数据.
		(3).优化查询.在where语句中包含分区条件时,可以只扫描一个或多个分区表来提高查询效率.
			涉及sum和count语句时,也可以在多个分区上并行处理,最后汇总结果. 
		(4).分区表更容易维护.例如:想批量删除大量数据可以清除整个分区. 
		(5).可以使用分区表来避免某些特殊的瓶颈.例如InnoDB的单个索引的互斥访问,ext3问价你系统的inode锁竞争等;
	13.4.表分区的限制因素:
		(1).一个表最多只能有1024个分区;
		(2).MySQL5.1中,分区表达式必须是整数,或者返回整数的表达式.在MySQL5.5中提供了非整数表达式分区的支持.
		(3).如果分区字段中有主键或者唯一索引的列,那么多有主键列和唯一索引列都必须包含进来.
			即:分区字段要么不包含主键或者索引列,要么包含全部主键和索引列.
		(4).分区表中无法使用外键约束.
		(5).MySQL 的分区适用于一个表的所有数据和索引,不能只对表数据分区而不对索引分区,也不能只对索引分区而不对表分区,
			也不能只对表的一部分数据分区;
	13.5.查看分区:判断 MySQL 是否支持表分区:
			mysql> show variables like '%partition%';
			+-------------------+-------+
			| Variable_name     | Value |
			+-------------------+-------+
			| have_partitioning | YES   |
			+-------------------+-------+
			1 row in set (0.00 sec)
	13.6.MySQL 支持的分区类型:
		(1).RANGE分区:按照数据的区间范围分区;
		(2).LIST分区:按照List中的值分区,与 RANGE的区别是,range分区的区间范围值是连续的;
		(3).HASH分区
		(4).KEY分区
		说明:在MySQL5.1版本中,RANGE,LIST,HASH 分区要求分区键必须是 int 类型,或者通过表达式返回INT类型.
			但KEY分区的时候,可以使用其他类型的列(BLOB，TEXT类型除外)作为分区键;
	13.7.RANGE分区:
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
	13.8.LIST分区:
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
	13.9.Columns分区:MySQL5.5中引入的分区类型,解决了5.5版本之前range分区和list分区只支持整数分区的问题
		Columns分区可以细分为 range columns分区和 list columns分区,他们都支持整数、日期时间、字符串三大数据类型.
		-- http://www.cnblogs.com/chenmh/p/5630834.html
		(1).与 RANGE分区 和 LIST分区区别:
			针对日期字段的分区就不需要再使用函数进行转换了,例如针对date字段进行分区不需要再使用YEAR()表达式进行转换;
			COLUMN分区支持多个字段作为分区键但是不支持表达式作为分区键;
		(2).COLUMNS支持的类型:
			整形支持:tinyint,smallint,mediumint,int,bigint;不支持decimal和float
			时间类型支持:date,datetime
			字符类型支持:char,varchar,binary,varbinary;不支持text,blob
		13.9.1.RANGE COLUMNS分区:
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
		13.9.2.LIST COLUMNS分区:
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

	13.10.HASH分区:
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
	13.11.KEY分区:
		(1).类似Hash分区,Hash分区允许使用用户自定义的表达式,但Key分区不允许使用用户自定义的表达式.
			Hash仅支持整数分区,而Key分区支持除了Blob和text的其他类型的列作为分区键.
			partition by key(exp) partitions 4; --exp是零个或多个字段名的列表
		==> key分区的时候,exp可以为空,如果为空,则默认使用主键作为分区键,没有主键的时候,会选择非空惟一键作为分区键;
	13.12.分区对于NULL值的处理:
		MySQ允许分区键值为NULL,分区键可能是一个字段或者一个用户定义的表达式.
		一般情况下,MySQL在分区的时候会把 NULL 值当作零值或者一个最小值进行处理.
		注意:
			Range分区中:NULL 值被当作最小值来处理
			List分区中:NULL 值必须出现在列表中,否则不被接受
			Hash/Key分区中:NULL 值会被当作零值来处理
	13.13.分区管理:
		1.13.1.增加分区:
			(1).RANGE分区和LIST分区:
				alter table table_name add partition (partition p0 values ...(exp))
				values后面的内容根据分区的类型不同而不同
			(2).Hash分区和Key分区:
				alter table table_name add partition partitions 8; -- 指的是新增8个分区
		1.13.2.删除分区:
			(1).RANGE分区和LIST分区:
				alter table table_name drop partition p0; --p0为要删除的分区名称
			==> 删除了分区,同时也将删除该分区中的所有数据.
				同时,如果删除了分区导致分区不能覆盖所有值,那么插入数据的时候会报错.
			(2).Hash分区和Key分区:
				alter table table_name coalesce partition 2; --将分区缩减到2个
		1.13.3.移出分区:
			alter table members remove partitioning;
			使用remove移除分区是仅仅移除分区的定义.并不会删除数据和 drop PARTITION 不一样,后者会连同数据一起删除
	13.14.分区查询:
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
	