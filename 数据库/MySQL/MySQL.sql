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
	
五.MySQL 存储引擎	
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

六.SQL 执行顺序:
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
七.连接:"参考图片: SQL-Joins-1.jpg,SQL-Joins-2.jpg"
假设两张表:emp, dept. emp表中的deptId为dept表中的主键.
MySQL 不支持 full join
1.内连接:
	(1).select * from emp inner join dept on emp.deptId=dept.id;
		查询两张表中共有的数据.
		等同于:
		select * from emp, dept where emp.deptId=dept.id
2.左外连接:
	(1).select * from emp a left join dept b on a.deptId=b.id
		查询emp独有的数据和查询emp与dept共有的数据
3.左连接:
	(1).select * from emp a left join dept b on a.deptId=b.id where b.id is null;
		查询emp独有的数据
4.右外连接:
	(1).select * from emp a right join dept b on a.deptId=b.id;
		查询dept独有的数据和查询emp与dept共有的数据
5.右外连接:
	(1).select * from emp a right join dept b on a.deptId=b.id where a.id is null;
		查询dept独有的数据
6.全连接:
	(1).select * from emp a left join dept b on a.deptId=b.id 
		union 
		select * from emp a right join dept b on a.deptId=b.id;
		查询所有emp和dept独有和共有的数据
7.全连接(去除共有数据):
	(1).select * from emp a left join dept b on a.deptId=b.id where b.id is null 
		union 
		select * from emp a right join dept b on a.deptId=b.id where a.id is null;
		去除两张表的共有数据,查询emp和dept分别独有的数据
8.union 和 union all:联合查询
	8.1.union:
		用于合并两个或多个 SELECT 语句的结果集,并消去表中任何重复行. union 内部的 SELECT 语句必须拥有
		相同数量的列,列也必须拥有相似的数据类型,每条 SELECT 语句中的列的顺序必须相同
		(1).基本语法:
			select column_name from table1
			union
			select column_name from table2
	8.2.union all:
		用途同 union all, 但是不消除重复行.
		SELECT column_name FROM table1
		UNION ALL
		SELECT column_name FROM table2
	8.3.union 使用注意事项:
		如果子句中有 order by 或 limit, 需要用括号括起来,推荐放到所有子句之后,即对最终合并的结果来排序或筛选
		在子句中,order by 需要配合limit使用才有意义.如果不配合limit使用,会被语法分析器优化分析时去除
		(1).如下语句:
			select * from emp a left join dept b on a.deptId=b.id order by id desc 
			union 
			select * from emp a right join dept b on a.deptId=b.id order by id desc;
			==> 报错:1221 - Incorrect usage of UNION and ORDER BY



	
	