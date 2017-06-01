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
	
	
	
	
	
	
	