# 一.数据库
## 1.在开发Java大型项目时,如何存储数据?
	(1)文件存储,但是存在如下问题:
		A:文件的安全性问题
		B:文件不利于查询和对数据的管理
		C:文件不利于存放海量数据
		D:文件在程序中控制不方便;
	(2)数据库:
		A:本质是一款软件,专门用于管理和维护数据的,对大量信息进行管理的高效解决方案,
		按照数据结构来组织、存储和管理数据的库；
		数据库系统:
			DataBase System = 数据库管理系统(DBMS) + 数据库(DataBase) + 管理员(DBA)
	(3)数据库的结构,分为三层:
		A:Client(专门用于访问数据库);sqlplus,pl/sql develope,sql develope
		B:Oracle dbms(database management system);
		C:db实例(一般就是sys)---数据对象:表,视图,序列,函数,包,存储过程,触发器(针对Oracle)
	(4)关系型数据库系统:建立在关系模型上的数据库系统;
		(4.1)关系模型:
			A:数据结构可以规定,同类数据,结构一致,就是一个二维表格
			B:数据之间的关系可以设置;
			◆NoSQL:not only sql,非关系型数据库系统;
			◆对象型数据库,ORM
		行列是以结构来说的,记录与字段是按数据来说的
## 2.数据库的分类:
	(1)目前，商品化的数据库管理系统以关系型数据库为主导产品，技术比较成熟。面向对象的数据库
	管理系统虽然技术先进，数据库易于开发、维护，但尚未有成熟的产品。
	国际国内的主导关系型数据库管理系统有SQL Server、ORACLE、SYBASE、INFORMIX和 DB2。	
	(2)项目中应当如何选择数据库?
		A:标的(成本);
		B:需要完成的功能;
		C:并发的用户量;
		D:安全和稳定性;
		E:部署的操作系统;
			unix:solaris,freebsd,hp unix,aix;
			linux,Windows nu

## 3.Oracle认证

## 二.Oracle操作
	1.解锁默认的账户，可以通过system用户来解锁
		alter user scott account unlock---scott为需要解锁的账户
	2.Oracle的常用sqlplus命令(如果是sql语句必须带分号,如果是sqlplus可带分号)
		(1)conn[ext] 用户名/密码@网络[as sysdba/as sysoper]: 如果是sys用户连接,后面加上as sysdba
			conn 用户名/密码:该命令经常用于切换用户;			
		(2)show user:显示当前用户名
		(3)disc:断开当前用户连接;即断开与Oracle连接,不退出qlplus窗口
		(4)exit:断开用户连接同时退出sqlplus窗口
		(5)passw[ord]:用于修改用户的密码;
			基本用法:password 用户名;
				如果是给自身修改密码,则可以不带用户名;
				如果修改其他用户的密码(前提:是system或sys用户才能修改用户密码),则需带上用户名;
				dba修改用户的密码:alter user 用户名 indentified by 新密码;
		(6)&:交互命令
			select * from cost where name='&name';
			name:
		(7)set linesize:设置每行显示的大小;setlinesize 140;
		(8)edit 文件名:编辑sql文件
			edit d:\aa.sql ---如果存在文件则打开该文件,如果不存在则创建该文件;
		(9)spool:
			需求如下:把屏幕显示的记录保存到文件中;
			spool f:/bak.sql;	--文件路径
			select * from cost --执行操作(增删查改)
			spool off;
		(10)linesize:用于控制每行显示多少个字符,默认显示80个字符;
				set linesize 120:设置每行;
		(11)pagesize:用于控制每页显示的数据条数,默认显示14行;
			set pagesize 14;
	3.Oracle用户管理:
		(1)创建用户(一般是由DBA创建):create user 用户名 indentified by 密码;
			Oracle要求用户密码不能使用数字开始,ORA-01031:表示权限不足
			如:创建一个普通用户
				create user xiaoming identified m123;
			--注:如果操作Oracle出现ORA-01031类似故障,一般都是oracle故障代码;			
		(2)创建用户的细节问题
			create user 用户名 indentified by 密码 default tablespace users temporary tablespace temp quota 3m on users;
			◆表空间:
				(1)什么是表空间:表存在的空间,一个表空间是指向具体的数据文件

		(3)创建好的用户无法直接登录?	
				因为Oracle新创建的用户是没有任何权限的,需要管理员给用户分配相应的权限才能登录;
		(4)分配/回收权限:需要是DBA
				grant 权限/角色 to 用户名; ---给用户名分配登录权限;
				revoke：回收权限
					如:revoke 权限/角色 from  用户
				with grant option:表示得到的对象权限用户,可以把权限继续分配---对象权限转移
				with admin option:系统权限转移;
		(5)Oracle管理机制:
			权限与角色管理
		(6)删除用户:
			drop user 用户名[cascade];
			◆当删除一个用户时,如果这个用户已经创建过数据对象(如表,触发器等),那么在删除该用户时需要加个
			选项:cascade,表示删除用户的同时把该用户创建的数据对象一并删除;
	4.Oracle方案:是Oracle管理数据对象的方式	
		(1)当一个用户创建之后,如果该用户创建了任意一个数据对象,这时dbms就会创建一个对应的方案
		与该用户对应,该方案的名称与用户名一致;
			◆如果希望看到某个用户的方案有哪些数据对象,可以用pl/sql工具
		(2)方案的运用:
			使用一个用户查询另一个用户中的数据对象;
				◆给一个用户分配权限使之可以操作当前用户的表格:
					grant select[delete,update,all] on 数据对象(表) to 用户名;
					select * from 方案名.数据对象(表)
	5.案例:用户创建练习
		(1)创建用户tea,stu,并给这两个用户resource,connect角色;
			create user tea identified by tea;
			grant resource to tea;
			grant connect to tea;
			create user stu identified by stu;
			grant resource to stu;
			grant connect to stu;
		(2)使用scott用户把对emp表的select权限给tea;
			grant select on emp to tea;			
			A:使用tea查询scott的emp表;
			connect tea/tea;
			select * from scott.emp;
			B:使用scott用户把对emp表的所有权限赋给tea;
			grant all on emp to tea;
			C:使用tea更新/删除/插入scott的emp表;
			update,select,delete,insert
			D:使用scott收回权限;
			revoke all on emp from tea;
		(3)将tea拥有对scott.emp的权限转给stu;
			connect scott/clq;
			grant all on scott.emp to tea with grant option
			--授权给用户tea,其可以将操作scott.emp的对象权限继续进行分配;
			--如果是分配系统权限,那么写成如下:
				grant connect to tea with admin option;
			A:使用stu查询scott用户的emp表;
			connect stu/stu;
			select * from scott.emp;
			B:使用tea收回stu的权限;
			connect tea/tea;
			revoke all on scott.emp from stu;
					
	6.使用profile对用户口令管理:
		(1)profile是口令限制,资源限制的命令集合,当简历数据时,Oracle会自动简历名称为default的profile,当简历用户没有指定profile选项,那么oracle就会讲default分配给用户;
		(2)账户锁定:指定该账户登录时最多可以输入密码的次数,同时还可以指定锁定的时间,一般是dba去执行该命令;
		(3)创建profile文件:
			例:tea用户输入密码的次数最多只能是3次,锁定时间为2天;
			create profile lock_account limit failed_login_attempts 3 password_lock_time 2;
			--密码输入错误次数3次以上将用户锁定两天
			分配文件
			alter user tea profile lock_accout;		
		(4)解锁账户(用户):暂时取消终止口令
			alter user 用户名 account unlock;
		(5)终止口令:
			提示用户定期修改密码,可以使用终止口令的指令来完成,同时这个命令也需要dba身份来操作;
			例如:给scott创建profile文件,要求用户每隔10天修改密码,宽限期两天
			create profile myprofile limit password_life_time 10 password_grace_time 2;
			--密码有效期10天,到期提示离密码过期还剩下2天,2天后强制更改密码;
			应用终止口令到对应的账户:
			alter user 用户名 profile myprofile;		
		(6)口令历史:
			修改密码时,不能使用之前的密码;如果修改时输入的密码跟以前一致,就会提示重新输入密码;
			create profile password_history limit password_life_time 10 password_grace_time 2 password_reuse_time 1
			--password_reuse_time:指定口令可重用的时间,即10天后就需要修改
			alter user scott profile myprofile		
		(7)删除profile
			drop profile 文件名;
		◆案例:	
			如果希望删除某用户,但同时保留该用户下的数据对象,要怎么处理?
				锁定该用户,该用户下的数据对象仍可使用:
				alter user username account lock;
				锁定的用户不能登录数据库了,但是sys用户仍然可以使用数据对象;
	7.Oracle数据库的启动流程
		systeminfo---查看当前操作系统信息
		(1)Windows操作系统
			dos命令:
				lsnrctl start :启动监听服务;
				oradim -startup -sid 数据库实例名 :启动数据库实例
		(2)unix操作系统/Linux系统
			lsnrctl start :启动监听服务;
			sqlplus sys/change_on_install as sysdba(以sysdba身份登录)可以这样写:
				sqlplus /nlog
				conn sys/change_on_install as sysdba
			startup
			
	8.Oracle登录认证方式-Windows系统
		Oracle登录认证在Windows和Linux下是不完全相同的;下面是Windows下Oracle的登录验证方式;
		(1)Oracle用户验证机制:
			A:普通用户,默认是以数据库方式验证,比如 connect scott/xx;
			B:特权用户,默认是以操作系统认证的(即只要当前登录操作系统的用户是在ora_dba组中,则可通过)
			比如:connect system/clq as sysdba,dbms一看到as sysdba,则认为要以特权用户登录,前面用户名和密码不看,登录后自动切换成sys用户;如果当前用户不再ora_dba组,这时再使用数据库验证方式;
		(2)可以修改sqlnet.ora文件,让特权用户登录时直接使用数据库验证;
				SQLNET.AUTHENTICATION_SERVICES = (NTS)---基于当前操作系统的验证;
				SQLNET.AUTHENTICATION_SERVICES = (NONE)---基于Oracle数据库的验证;
				SQLNET.AUTHENTICATION_SERVICES = (NONE,NTS)---两种验证方式共存		
		◆Linux系统下Oracle登录认证方式:
			A:默认情况下sqlnet.ora文件中没有SQLNET.AUTHENTICATION_SERVICES参数时,是基于操作系统和oracle数据库验证共存;
			B:加上SQLNET.AUTHENTICATION_SERVICE参数时,不管SQLNET.AUTHENTICATION_SERVICE设置为NONE或者NTS,都是基于oracle数据库验证的;
		
	9.丢失管理员密码	
		恢复方法:把原有密码文件删除,生成一个新的密码文件;
		(1)搜索名为:PWD数据库实例名.ora文件---数据库实例名是根据实际情况定的,比如orcl,xe;
		(2)拷贝上述文件,备份;
		(3)生成新的密码文件,在dos控制台输入如下命令:file表示文件的全路径
			orapwd file=原来文件的全路径\密码文件名.ora password=新密码 	entries=10;
			--entries:允许有多少个特权用户,即登录sys最多用户;
			密码文件名必须跟原来的文件名一致;
			如果希望新的密码生效,则需要重新启动数据库实例;
	10.案例:
		给scott用户分配一个profile文件,要求:
			(1)尝试登录次数最多为4次,如果4次连续错误,则锁定该用户两天;
				create profile profile1 limit failed_login_attempts 3 password_lock_time 2;
				alter user scott profile profile1;
			(2)密码每隔5天修改一次,宽限期为2天;
				create profile profile2 limit password_life_time 5 password_grace_time 2;
				alter user scott profile profile2;
			(3)解锁用户;
				alter user scott account unlock
			(4)删除profile;
				drop profile profile1;drop profile profile2;
		
# 三.Oracle操作		
	1.基本概念:
		(1)数据在数据库的中的存储方式:表;
			表的一行称为一条记录,表的一条记录对应一个Java的数据对象;
	2.Oracle常用数据类型:
		(1)char(size):存放字符串,最大2000个字符,是定长;---ascii编码
			如:char(32),那么其只能存放32个字符,如果超出32个字符则报错,如果不够则使用空格填充;
			dump(列名):查看该列中存放的数据;
			select name,dump(name) from users;
			NAME             DUMP(NAME)
			---------------- --------------------------------------------------------------------------------
			abc              Typ=96 Len=16: 97,98,99,32,32,32,32,32,32,32,32,32,32,32,32,32
		(2)varchar2(size),最大长度是4000,变长;---ascii编码
		◆如果是数据长度是固定的,如电话号码等,则应该使用char来存放,因为这样存取速度快;
			如果存放的数据长度是变化的,则使用varchar2();
		(3)nchar(size):最大字符数2000,是定长的;
			其编码方式unicode编码(一个汉字占用nchar的一个字符空间,而一个汉字占用char的两个字符空间);
		(4)nvarchar2(size):最大字符数4000,是变长的;
			其编码方式unicode编码;
		(5)clob(character large object):字符型大对象,最大值为8TB,变长,只能存放字符类型
		(6)blob(binary large object):二进制数据,可以方图片/声音,最大值8TB,变长;
			注:在实际开发中很少把图片和声音文件存放到数据库中,效率太低,一般都是记录图片和声音的一个路径;
		(7)数值类型:number(p,s)
			A:可以存放整数也可以存放小数;
			B:number(p,s),p为有效位,s为小数位,1<=p<=38, -84<=s<=127;
				(有效位是从第一个非0数字开始的,所有小数位数与有效位数字不同不冲突);
				保存范围:-1.0e-130 <= number value <= 1.0e+126;保存在机器内部的范围:1~22bytes
				◆有效位:从左到右, 第一个非0数字就是第一个有效位;
			C:使用原则:在实际开发中,有明确要求保留小数点几位,则明确指定;如果没有,则可以直接使用number;
				例子:
				插入数据	列的定义			存入的数据
				123.89		number				123.89
				123.89		number(3)		124
				123.89		number(6,2)		123.89
				123.89		number(6,1)		123.9
				123.89		number(4,2)		值大于为此列指定的允许精度
				123.89		number(6,-2)	100
		(8)date日期类型		
			A:表示时间,年/月/日/时/分/秒,Oracle的默认格式:"dd-mm-yy"
			B:timestamp(n),修改表的数据该列会同时更新;
	3.表的管理:
		alter table 表名 add(新的列名 列的数据类型)---添加列
		alter table 表名 modify(新的列名 列的数据类型)--修改列
		alter table 表名 drop column 列名--删除列
		rename 旧表的名字  to 新的名字--修改表名
	4.表的CRUD(create/retrieve/update/delete增删查改)操作	
		(1)插入操作insert
			注:Oracle中往数据库中插入数据时,总是尝试将要插入的数据转换为对应列的数据类型;
		(2)修改操作update
		(3)删除操作:delete from table,保存日志文件,可恢复;truncate table 表名,不可恢复,速度块;
	5.SQL简单查询:oracle的sql语句不区分大小写,但其内字符串是区分大小写的
		5.1.distinct:去重
		5.2.nvl(arg1,arg2):空值函数arg1为空,返回arg2,不为空,则返回arg1本身;
		5.3.||连接字符串,con
		5.4.like,in
		5.5.is null :where子查询
			--查询第N高工资的记录		
			select max(sal) from emp  where level = &n  connect by prior sal>sal  start with sal = 
			( select max(sal) from emp);
	6.SQL高级查询		
		6.1.max,min,sum,avg,count,聚合函数
			◆注意:
				(1)avg(列名)是不会把null的进行统计,如果需要考虑null,可以这样处理avg(nvl(列名,0));
				(2)count(列名)不将null计入统计;
		6.2.group by having,分组	
			◆注意:
				(1)聚合函数只能出现在select、having、order by子句中;
				(2)在select中如果有列、表达式和聚合函数，那么这些列和表达式必须有一个出现在group by字句中,
				否则就出错
		6.3.多表查询:笛卡尔积
			自连接
		6.4.子查询:
			(1)多列子查询:可以使用any(任意一个)或者all(全部)
			(2)在from子句中使用子查询
				例子:
				select dname,dept.deptno,nvl(cou,0) from dept,(select deptno,count(deptno) cou from emp group by deptno)c where dept.deptno=c.deptno(+);
				其中(+)--表示外连接符号;
		6.5.分页查询(★★)
			①mysql的分页查询
				select * from 表名 where 条件 limit 从第几条,取几条;
			②sqlserver
				select top 1 * from 表名 where id not in(select top4 id from 表名 where 条件);
				取出第5~8条;
			③Oracle:使用rownum
				--可以测试sql的效率
				--模拟大量数据,查看sql执行时间:
				--自我复制数据:
					insert into 表(列1,列2,列3,...) select 列1,列2,列3,... from 表;----从自己表中查询数据往自己表中插入
		6.5.集合运算
			①cube:统计所有分组的列:
		6.6.表的连接	
			Ⅰ.内连接
			Ⅱ.外连接
				①左外连接:
					select exam.id,name,nvl(grade,0) from stu left outer join exam on stu.id=exam.id;
					或者:
					select exam.id,name,nvl(grade,0) from stu ,exam where stu.id=exam.id(+);
				②右外连接:
					select exam.id,name,nvl(grade,0) from stu right outer join exam on stu.id=exam.id;
					或者
					select exam.id,name,nvl(grade,0) from stu,exam where stu.id(+)=exam.id;

# 四.创建数据库实例:
	1.使用向导工具创建;
	2.手工创建数据库实例;
	①在创建数据库实例后,Windows系统中服务中会出现创建的数据库实例服务;
	②同一台电脑上可以同时启动多个数据库实例,在连接或登录时,需指定对应的主机字符串		
	3.行迁移(使用子查询来完成行迁移)
		①使用sql建表
			create table  temp# as select empno,ename from emp where ename like 'S%';
	4.使用子查询来完成更新：
		希望scott员工的工作,薪水,补助与Smith员工一样
		update emp set (job,sal,comm)=(select job,sal,comm from emp where ename='SMITH') where ename='SCOTT'
	
# 五.SQL函数	
	Ⅰ.单行函数
		1.字符函数:
			①replace(char1,search_string,replace_string):替换字符
			②trunc :select trunc(456.899,-2) from dual; ==>400
			③to_char(number/date/char)..
				薪水显示带上货币
				select ename,to_char(sal,'L999G999D99')  from emp;
				显示:￥1,600.00
				L--表示本地货币符号
				$--美元符号
				to_char(sysdate,'day')--可以显示系统日期的星期
		2.系统函数:用于查询系统的信息,sys_ ontext
			①terminal:当前会话客户所对应的终端的标识符;
			②language:语言;
			③db_name:当前数据库名称;
			④nls_date_format:当前会话客户所对应的日期格式;
			⑤session_user:当前会话客户所对应的数据库用户名;
			⑥current_schema:当前会话客户所对应的默认方案名;
			⑦host:返回数据库所在的主机的名称;
				select sys_context('userenv','db_name') from dual;---查看当前数据库实例
	Ⅱ.多行函数
	
# 六.Oracle事务:
	1.事务:是对数据库的一系列操作(DML)看成一个整体,要不全部成功,要么全部失败;
		利用事务可以保证数据的完整性;
	2.事务和锁:当执行事务操作时,Oracle会在被作用的表上加锁,防止其他用户改变表的结构;
	3.事务提交:使用commit语句提交事务时,会确认事务的变化、结束失误、删除保存点、释放锁；
		commit语句执行完毕后，其他会话就口可以查看到事务变化后的新数据;
	4.回退事务:
		(1)保存点savepoint:是事务中的一点,用于取消部分事务,当结束事务时,
			会自动删除该事务所定义的所有保存点;
			①语法:savepoint 保存点名称;
			②在一个事务中可以保存多个保存点;
			③可以使用rollback保存点回退到指定保存点,一旦回退,那么该保存点就被删除了,不能再次回退;
			④一旦提交事务后,那么就不能在回退到保存点
			◆回退到保存点:rollback 保存点
			◆取消全部事务:rollback
	5.如果一个事务中,只是select语句,那么事务控制可以忽略,如果一个事务有多个DML语句,则需要考虑事务;
	6.事务隔离级别:定义了事务与事务之间的隔离程度,用于指定事务的隔离程度;
		(1)脏读(dirty read):当一个事务读取另个事务尚未提交的修改时,产生脏读;Oracle不存在脏读;
		(2)不可重复度(nonrepeatable read):同一查询在同一事务中多次进行,由于其他提交事务所做的修改或删除,
		每次返回不同的结果集,此时发生非重复读;
		(3)幻读(phantom read):同一查询在同一事务中对此进行,由于其他提交事务所在的插入操作,
		每次返回不同的结果集,此时发生幻读;
		(4)如何设置Oracle事务隔离级别设为:serializable;
			set transaction isolation level read committed;--默认的
			set transaction isolation level serializable;
		(5)Oracle的事务隔离级别:
			①read committed:默认事务隔离级别,保证不会出现脏读,但可能出现非重复读或幻读;
			②serializable:使事务看起来像一个接着一个地顺序执行,仅在本事务开始前由其他事务提交的更改和
				在本事务中所做的更改,保证不出现脏读、非重复读、幻读
			③read only:保证不出现脏读、非重复读、幻读
				serializable可以执行DML语句;而read only只能执行DQL(即查询)
			Java中设置隔离级别:
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED)
	
# 七.Oracle数据的完整性:
	1.维护数据的完整性:
		①约束
		②触发器
		③应用程序(函数,过程);
	2.约束:用于确保数据库满足特定的商业规则,在Oracle中约束包括
		①not null(非空):用于指定某列的值不可以是null;			
		②unique(唯一):用于指定某列的值不能重复但可以为null且可以是多行为null;
			修改一个字段为unique:
			alter table customer add constraint uni_id unique(id)
		③primary key(主键):用于唯一的标示数据,当定义主键约束后,该列不能重复且不为null;
			一个表只能有一个主键,但可以有多个唯一约束
			◆主键和唯一约束的区别:
				--一个表可以有多个unique,但只能有一个主键;
				--unique的值可以为null,但主键不可以;
				--主键的所在列会自动创建的index(索引),但unique不会自动创建索引
					设置主键
					create table test1(id number primary key,name varchar2(20),classId number references test2(id));
					create table test2(id number primary key,classname varchar2(20));					
		④foreign key(外键):用于定义表和从表之间的关系,外键指向主键;外键也可以执行unique约束的列;
			外键列和主键列的数据类型要一致;外键列的值必须存在主表的主键列中,外键可以为null			
		⑤check(检查约束):用于强制行数据必须满足的条件
			create table user1(id primary key,sal number check(sal>=1000 and sal<=2000));
			alter table customer add constraint ch_address check(address in('',''));
	3.修改或删除约束
		(1)修改约束:
			alter table 表名 add constraint 约束名(check/unique/primary key等) 约束字段;
			alter table 表名 modify 字段名 not null;	
		(2)删除约束:
			alter table 表面 drop constraint 约束名;--约束名:一个表的每个约束都对应一个名称
			说明:因为一张表只能有一个主键,因此删除主键时可以直接使用primary key
				alter table 表名 drop constraint primary key;
			如果有外键指向该主键的某个值,则不能删除成功,需要使用cascade[级联]
				alter table 表名 drop constraint primary key cascade;
	4.列级定义和表级定义:
		(1)在定义表后,直接在列后定义约束,称为列级定义;
			id number  primary key;
		(2)表级定义:把各个列都定义完毕后再分别说明约束;
			create table user1(id number,name varchar2(20),constraint pk_key primary key id);
			主键:constraint 约束名 primary key (字段)
			外键constraint 约束名 foreign key(字段) references 主表(字段);
			唯一:constraint 约束名 unique(字段)
			不为空:not null(字段)
			检查约束:constraint 约束名 check(字段条件)
		(3)复合主键:两个字段一起作为主键,使用表级定义
	◆综合案例:	现有一个商店的数据库,记录客户及其购物情况,由下面三个表组成:
		商品表goods(商品号goodsId,商品名goodsName,单价unitprice,商品类别category,供应商provider);
		客户customer(客户号customerId,姓名name,住址address,电邮email,性别sex,身份证cardId);
		购买purchase(客户号customerId,商品号goodsId,购买数量nums);
		完成如下功能:
			建表,在定义中要求声明:
			①每个表的主外键;
			②客户的姓名不能为空值;
			③单价必须大于0,购买数量必须在1~30之间;
			④电邮不能重复;
			⑤客户的性别必须是男或者女默认为男;
			create table goods(
				goodsId number primary key,
				goodsName varchar2(100),
				unitprice number check(unitprice>0),
				category varchar2(100),
				provider varchar2(100)
			);
			create table customer(
				customerId number primary key,
				name varchar2(20) not null,
				address varchar2(200),
				email varchar2(30) unique,
				sex char(3) default '男' check(sex in('男','女')) ,
				cardId char(18)				
			);
			create table purchase(
				customerId number references customer(customerId),
				goodsId number references goods(goodsId),
				nums number check(nums>=1 and nums<=30)
			);
		
# 八.序列sequence:
	一般用在主键或唯一约束,
	只有Oracle有,在mysql中都是使用自增长来实现的;
	MYSQL:
		create table temp1(id int primary key auto_increment);
	1.创建一个序列:
		create sequence myseq
		start with 1
		increment by 1
		minvalue 1
		maxvalue 30000
		cycle	--cycle表示序列增长到30000,又从1重新开始;
		nocache	--表示不缓存[cache 10:表示一次产生10个号供使用,使用缓存产生号:优点是提高效率,缺点是可能产生跳号!]
	2.使用序列:
		myseq.nextval:查看序列的下一个值(序列会递增)
		mysqe.currval:查看序列的当前值(序列不会递增),在使用过nextval才能使用currval
	
# 九.索引
	1.创建索引:
		①单列索引:create index 索引名称 on 表名(列名)
		②符合索引:create index 索引名称 on 表名(列名1,列名2,...);
	2.使用原则:
		①在大数据表上建立索引才有意义
		②在where子句或是连接条件上经常引用的列上建立索引;
		③索引的层次不超过4层;
		④在逻辑型类型字段上或者值是固定的几种的列上不要建立索引;
	3.索引缺点:
		①建立索引,系统要占用大约表的1.2倍硬盘和内存空间来保存索引;
		②更新数据时,系统必须有额外的时间来同时对索引进行更新,以维持数据和索引的一致性;
			索引会影响数据插入、修改、删除的效率;	
	4.索引的分类:
		(1)按数据存储方式:B树,反向索引,位图索引;
		(2)按照索引列的个数分类:单列索引,符合索引;
		(3)索引列值的唯一性分:唯一索引和非唯一索引;
		此外还有函数索引、全局索引、分区索引
	
# 十.管理权限和角色
	1.权限：
		(1)系统权限:指对数据库管理的操作以及对数据库对象的操作(创建,删除,修改);--166种
			①查看oracle中所有的系统权限:查询数据字典视图system_privilege_map,可以显示所有的系统权限
				select * from system_privilege_map order by name
			②常用系统权限:
				create session、create table、create tablespace、create trigger、create user、create view ；
			③授予系统权限:一般是DBA来授予权限,其他用户需要有grant any privilege
				系统权限才能给其他用户授予权限
				◆系统权限使用管理案例:
					给用户授予权限时,带with admin option就表示当前用户获得的权限继续向别的用户转发
					grant 权限名称 to 用户名 with admin option;
			④回收系统权限:不会级联回收;
				即: 	system		>>>> 			scott		>>>>	monkey
					create session			create session		create session
					system用户将create session系统权限授予scott,scott转授于monkey用户;
					scott和monkey用户都具有该权限;system用户回收scott的create session权限时,
					monkey用户的create session权限不会被回收;
		(2)对象权限:对数据对象的操作,访问别的方案的对象的权利,那么必须具有对象的权限--17种
			①查看Oracle提供的所有对象权限
				select distinct privilege from dba_tab_privs
			②授予对象权限:
				grant 对象权限 on 数据库对象[scott.tmp] to 用户[角色名] [with grant option]
				★可以把对象权限直接授予给角色;
				★alter权限:如果需要修改别的数据库对象,那么需要授予alter对象权限;
				★execute权限:如果用户需要执行其他方案的包/过程/函数,则必须授予execute权限;
				★index权限:如果需要在别的用户上创建索引,需要授予对象的index权限;
				★with grant option:用于转授对象权限,但该选择只能被授予用户而不能授予角色;
			③回收对象权限:级联回收即回收对象权限时,被转授的对象权限同时也被回收了;
				revoke 对象权限 on 数据库对象  from 用户名[角色];
	2.角色:本质是多个相关权限的集合,主要用于简化权限的管理;可以包含对象权限和系统权限
		查询某个角色所具有的权限:
			select * from dba_sys_privs where grantee='DBA';
		授予角色:grant 角色名 to user
		(1)预定义角色:是指Oracle所提供的角色,每种角色都用于执行一定的管理任务;33种预定义角色,主要如下:
			①connect角色:
			②resource角色:其有个隐藏系统权限unlimited tablespace
			③dba角色
		(2)自定义角色:33种预定义角色可能满足不了所有需求,因此可以使用自定义角色来解决相关问题;
			①建立自定义角色:具有create role系统权限的用户或具有DBA角色的用户来创建;
				Ⅰ.不验证角色:
					create role 角色名 not identified;
				Ⅱ.数据库验证方式:角色名和口令存放在数据库中,激活该绝世时,必须提供恐龙;
					create role 角色名 identified by 口令;
			②给定义角色授予权限(系统权限和对象权限)
				grant create session to myrole [with admin option]
			③删除角色:具有drop any role系统权限或者具有dba角色的用户来执行;--一般删除自定义角色;
				drop role 角色名
	
	3.精细访问控制:指用户可以使用函数、策略实现更加细微的安全访问控制;
		如果使用精细访问控制,则当在客户端发出sql语句时,oracle会自动在sql语句后追加谓词(where 子句)
		并执行sql语句,通过这样的控制,可以使得不同的数据库用户在访问相同的表时,返回不同的数据信息;
		
	
# 十一.pl/sql编程:
	(procedural language/sql):可以开发过程、函数、触发器、包,基本单元是:块
	1.基本概念:pl/sql是在标准sql语句基础上扩展的一种对Oracle数据库进行编程的语言;
	2.纯sql的技术缺陷:
		①不能模块化编程,为了完成某个功能,可能会多次发送sql;
		②执行速度,因为发送过来的sql可能数据库还得再次进行编译;
		③安全性问题
		④浪费带宽
	3.pl/sql入门案例:
		开发一个简单的存储过程,可以完成向某表添加记录
			create procedure pro1(in_empno number,in_ename varchar2) 
			is begin 
				insert into emp(empno,ename) values(in_empno,in_ename);
			end;
			/
		调用存储过程:
			①在控制台调用
				exec 过程名(参数1,参数2,...);
			②Java程序调用:
		◆注意:	
			①is和begin之间不能对变量重新赋值，需在begin后再次赋值；
			②pl/sql不能对输入的参数值重新赋值;
	4.pl/sql编程编写规范:
		①注释
			单行:--
			多行:/*	*/
		②标识符的规范:
			变量:以v_作为前缀
			常量:以c_作为前缀
			游标:以_cursor作为后缀
			例外:以e_作为前缀
	5.pl/sql块(block):是pl/sql的编程单元
		5.1.块的开发
			[declare]
				--定义部分,包括常量,变量,游标,例外,复杂数据类型等
			begin
				--执行部分,要执行的pl/sql语句和sql语句
			[exception]
				--例外的处理部分,处理运行的各种例外
			end;
			/
			①只包含执行部分的案例
					--开发一个只包括执行部分的块
					set serveroutput on;--默认情况下控制台不输出,需打开输出
					begin
						dbms_output.put_line('hello world');--dbms_output表示包,put_line表示存储过程;
					end;
					/
			②&:表示需要接收控制台输入的数据;||表示两个字符串连接;
				根据用户输入的empno输出雇员信息;
					declare
					--定义变量格式：变量名称 变量类型
					v_ename varchar2(20);
					begin
					--into v_ename表示把查询的值放入v_ename中
					select ename into v_ename from emp where empno=&empno;
					--输出v_ename
					dbms_output.put_line('ename is ' || v_ename);
					end;
					/

					--将上述代码该为一个过程
					create procedure pro3(in_empno number)is
					--定义变量格式：变量名称 变量类型
					v_ename varchar2(20);
					begin
					--into v_ename表示把查询的值放入v_ename中
					select ename into v_ename from emp where empno=in_empno;
					--输出v_ename
					dbms_output.put_line('ename is ' || v_ename);
					end;
					/
			③包含定义部分、执行部分和例外处理部分
					declare
						v_ename varchar2(20);
					begin
						select ename into v_ename from emp where empno=&empno;
						dbms_output.put_line('ename is ' || v_ename);
					exception
						when no_data_found then
						dbms_output.put_line('查无此人');
					end;
					/
				★★细节问题:
					涉及到异常处理:
						①异常的基本语法:
							exception 
								when 异常名称1 then 处理异常1代码;
								when 异常名称2 then 处理异常2代码;
								when 异常名称3 then 处理异常3代码;
						②oracle提供的异常:参考文档
						③异常处理:
							Ⅰ.可以捕获异常,并给出明确提示;
							Ⅱ.可以利用异常进行业务处理;
	
		5.2.过程的开发
			(1)过程用于执行特定的操作;建立过程时可以指定输入参数(in),也可以指定输出参数(out),
				未指定输入输出则为输入参数
				基本语法:
					create procedure 过程名(变量名 in 变量类型,..,变量名 out 变量类型,...) is
						--变量的定义
					begin
						--执行过程
					end;
					/
					或者:
					create procedure 过程名(变量名 in 变量类型,..,变量名 out 变量类型,...) as
						--变量的定义
					begin
						--执行过程
					end 过程名;
					/
			(2)例子:--编写过程,可以输入雇员工资名,新工资,可修改雇员的工资
							create or replace procedure pro5(in_ename in varchar2,in_new_sal in number)is 
							begin
							update emp set sal=in_new_sal where ename=in_ename; 
							end;
							/
			(3)调用过程:
				exec 过程(参数1,参数2,..);
				call 过程(参数1,....);
			(4)在java中调用存储过程:
				CallableStatement cs = Connection.prepareCall("{call pro5(?,?)}");
				cs.setString(1, "SMITH");
			cs.setDouble(2, 5555.00);
		5.3.函数：用于返回特定的数据,建立函数时,在函数头部必须包含return子句,
			在函数体内必须包含return语句返回的数据
			(1).基本语法:
				create function 函数名(参数1,...)
				return 返回的数据类型 is
					--定义变量
				begin 
					--执行语句;
				end;
				/
			(2).入门案例
				--写一个函数,可以接收用户名,输出该用户的年薪;
				create or replace function fun1(in_v_ename in varchar2)
				return number is
					--definition a varialible
					v_annual_sal number;
				begin
					select (sal+nvl(comm,0))*13 into v_annual_sal from emp where ename=in_v_ename;
					return v_annual_sal;
				end;
				
				--调用函数:
					select 函数名(参数1,...) from dual;
				--Java中调用
					就是执行上面的sql语句,返回一个结果集;
			◆函数和过程的区别:
				①函数必须有返回值,而过程可以没有;
				②在Java中调用方式不一样:
					函数:select 函数名(列) from dual;
					过程:{call 过程名(参数)}
		5.4.包:使用包可以更好的管理自定义函数、过程;
			(1).基本语法:
				create [or replace] package is
					--声明过程
					procedure 过程名(参数1,...);
					--声明函数
					function 函数名(参数1,...) return 数据类型;
				end;
			(2).入门案例:
				--请编写一个包,该包有一个过程,该过程可以接收用户名和新的薪水,还有一个函数,
				--该函数可以接收一个用户名(将来要实现得到该用户的年薪是多少)
					create or replace package mypackage is
					--声明一个过程
					procedure pro1(v_in_ename varchar2,v_in_newsal number);
					--声明一个函数
					function fun1(v_in_ename varchar2) return number;
					end;
					/
			(3).包体:把包中的过程或函数实现的对象;
					create or replace package body 包名 is
						procedure 过程名(参数1,...)is
						begin
						--执行语句
						end;
						function 函数名(参数1,...)
						return 数据类型 is
						--定义变量;
						begin
						--执行语句;
						end;
					end;
				◆◆注意:
					①包体中要实现的方法或者是过程应当先在包中声明;
					②在控制台中调用包中的函数或过程时,应当在前面加上包名,如果访问其他方案的,还需加上方案名
						exec 方案名.包名.过程(函数);
						call	方案名.包名.过程(函数);						
		5.5.触发器:
			触发器是隐含执行的存储过程,当定义触发器时,必须指定触发的事件和触发的操作,
			常用的触发事件包括insert,update,delete语句,而触发操作实际就是一个pl/sql操作
	6.	pl/sql语法:
		6.1.数据类型
			(1)标量类型:可以简单理解为Oracle数据库的数据类型;
				①变量定义:identified(变量名) [constant(常量,赋初始值,且不能变化)] 
							datatype(数据类型) [not null] :=default(指定初始值) expr(pl/sql表达式)
						v_ename varchar2(36):='SMITH';
						v_sal constant number(6,2):=500.00;
				②使用%type类型:在pl/sql编程中,让变量的类型和大小与表对应类的类型和大小一致:
					create or replace procedure pro7(v_in_empno in number)is
					--定义变量
					v_tax_rate number(3,2):=0.03;
					v_sal emp.sal%type;--v_sal变量跟表emp中sal列的类型和大小一致;
					v_ename emp.ename%type;
					v_tax number;
					begin
					select ename,sal into v_ename,v_sal from emp where empno=v_in_empno;
					v_tax := v_sal * v_tax_rate;
					dbms_output.put_line(v_ename || '工资是=' || v_sal ||',个人所得税'||v_tax);
					end;
					/
			(2)复合类型:用于存放多个值的变量,常用包括pl/sql记录,pl/sql表
				①pl/sql记录:类似与高级语言的结构体,当引用pl/sql记录成员时,必须加记录变量作为前缀
					type 自定义的pl/sql记录名 is record(
						变量名 变量类型,变量名 变量类型 ,...
					);
					案例:
						create or replace procedure pro7(v_in_empno in number)is
						--定义一个记录数据类型
						type clq_emp_record is record(
						v_ename emp.ename%type,
						v_sal emp.sal%type,
						v_job emp.job%type
						);
						--定义一个变量，该变量类型为clq_emp_record
						v_emp_record clq_emp_record;
						begin
						select ename,sal,job into v_emp_record from emp where empno=v_in_empno;
						dbms_output.put_line('名字:' ||v_emp_record.v_ename);--访问pl/sql记录里的变量
						end;
						/
				②pl/sql表(了解):相对于高级语言的数组,pl/sql表的下标可以为负数,表示表元素的下标没有限制;
					语法:
						declare
						type sp_table_type is table of emp.ename%type--sp_table_type是表的类型
						index by binary_integer;--emp.ename%type指定了表的元素类型和长度
						sp_table sp_table_type;--定义一个变量:sp_table 类型pl/sql表变量
						begin
						select ename into sp_table(-1) from emp where empno=7788;
						dbms_output.put_line('员工:' || sp_table(-1));
						end;
			(3)参照类型:用于存放数值指针的变量,通过使用参照变量,可以使得应用程序共享相同对象,
			从而降低占用的空间;pl/sql中,可以使用游标变量和对象类型变量;
				(3.1)游标变量:通过游标,可以取得返回结果集的任何一行数据,从而提高
					①定义游标:
						type 自定义游标名 is ref cursor;
						变量名 自定义游标名;
					②打开游标
						open 游标变量 for select 语句;
					③取出当前游标指向的行
						fetch 游标变量  into 其他变量;
					④判断游标是否执行记录最后
						游标变量%notfound
					⑤入门案例:
						Ⅰ.编写一个过程,可以输入部门号,并显示该部门所有员工和他的工资;
							--编写一个过程,可以输入部门号,并显示该部门所有员工和他的工资;
							create or replace procedure pro8(v_in_deptno in number)is
							--定义一个游标变量类型
							type clq_emp_cursor is ref cursor;
							--定义一个游标变量
							v_emp_cursor clq_emp_cursor;
							--定义两个变量
							v_ename emp.ename%type;
							v_sal emp.sal%type;
							begin 
							--打开游标
							open v_emp_cursor for select ename,sal from emp where deptno=v_in_deptno;
							--取出游标指向的每行数据;
							loop
							fetch v_emp_cursor into v_ename,v_sal;--引起游标向下走
							--判断当前游标是否到达最后
							exit when v_emp_cursor%notfound;
							--s输出
							dbms_output.put_line('用户名:'||v_ename||',薪水:'||v_sal);end loop;
							--关闭游标
							close v_emp_cursor;
							end;
							/
						Ⅱ.在Ⅰ的基础上,如果某个员工的工资低于200,就增加100;
							--编写一个过程,可以输入部门号,并显示该部门所有员工和他的工资;
							create or replace procedure pro8(v_in_deptno in number)is
							--定义一个游标变量类型
							type clq_emp_cursor is ref cursor;
							--定义一个游标变量
								v_emp_cursor clq_emp_cursor;
								--定义两个变量
								v_ename emp.ename%type;
								v_sal emp.sal%type;
								v_empno emp.empno%type;
							begin 
							--打开游标
								open v_emp_cursor for select ename,sal,empno from emp where deptno=v_in_deptno;
								--取出游标指向的每行数据;
								loop
									fetch v_emp_cursor into v_ename,v_sal,v_empno;--引起游标向下走
									--判断当前游标是否到达最后
									exit when v_emp_cursor%notfound;
									--s输出
									--dbms_output.put_line('用户名:'||v_ename||',薪水:'||v_sal);end loop;
									if v_sal<200 then update emp set sal=sal+100 where empno=v_empno;
									end if;
								end loop;
								--关闭游标
								close v_emp_cursor;
							end;
							/		
	7.pl/sql控制语句
		7.1.条件分支语句
			(1).if...then...
				①基本语法
					if 条件表达式  then
						执行语句...
					end if;
				②案例:
					--编写一个过程,输入一个雇员名,如果该雇员的工资低于2000,并给该雇员工资增加10%;
					create or replace procedure pro9(v_in_ename varchar2)is
						v_sal emp.sal%type;
					begin
						select sal into v_sal from emp where ename=v_in_ename;
						if v_sal<2000 then 
							update emp set sal=sal*0.1+sal where ename=v_in_ename;
						end if;
					end;
					/
			(2).if...then...else
				①语法:
					if 条件表达式  then--字符串的比较就直接=
						执行语句..;
					else 执行语句;
					end if;
			(3).if...then...elsif...elsif...else;
				①基本语法:
					if条件表达式 then 执行语句...;
					elsif 添加表达式	then 执行语句
					else 执行语句..;
					end if;
		7.2.循环语句
			①loop循环--至少执行一次循环
				语法:
					loop 执行语句...;
					exit when 条件表达式--退出当前loop循环
					end loop
			②while循环--只有条件为true才执行循环体
				语法:
					while 条件表达式 loop 
						执行语句...;
					end loop;
			③for循环--不推荐使用
				for i in reverse 1..10 loop 
					insert into users values(...);
				end loop;
		7.3.顺序控制语句:goto和null
			(1)goto语句
				①用于跳转到特定标号去执行语句,由于goto语句会增加程序的复杂性,
					并使得应用程度可读性变差,不建议使用goto语句
				②分析案例:
						declare
							i number:=1;
						begin
							<<start_loop>>--goto语句可跳转的标号
							loop
								dbms_output.put_line('输出i='||i);
								if i=12 then goto end_loop;
								end if;
								i:=i+1;
								if i=10 then goto start_loop;
								end if;
							end loop;
							<<end_loop>>--标号
							dbms_output.put_line('循环结束');
						end;
						/
			(2)null语句:不会执行任何操作,并且会直接讲控制传递到下调语句;
				null的主要用途是提高代码的可读性;
	8.pl/sql编写分页过程:
		8.1.Java调用无返回值的存储过程:
			Java调用关键代码:
				con = JDBCUtil.getConnection();
				CallableStatement cs = con.prepareCall("{call pro5(?,?)}");
				/*设置参数值,这里都是输入参数*/
				cs.setString(1, "SMITH");
				cs.setDouble(2, 5555.00);
				cs.execute();
		8.2.Java调用有返回值的存储过程(非列表)
			案例:编写一个过程,可以输入雇员编号,返回雇员的姓名;
			①编写过程:
				--out是输出参数的关键字
				create or replace procedure pro11(v_in_empno in number,v_out_ename out varchar2)is
				begin
					select ename into v_out_ename from emp where empno=v_in_empno; 
				end;
				/
			②在Java调用该过程:关键代码
				/*执行调用过程的语句,问号对应过程名的参数*/
				CallableStatement cs = con.prepareCall("{call pro11(?,?)}");
				/* 该过程中第一个问号是输入参数,需给其设置值*/
				cs.setString(1, "7839");
				/*第二个?表示的是输出参数,所以使用registerOutParameter给输出参数设置输出类型*/
				/*这里的类型对应的是Oracle数据的类型:是OracleTypes类下的静态变量*/
				cs.registerOutParameter(2, OracleTypes.VARCHAR);
				/*执行依然是execute方法()*/
				cs.execute();
				/*获得过程返回的值,getXxxx(列索引);其中列索引对应的是过程中输出参数所对应的位置*/
				String str = cs.getString(2);					
				◆注意:
					Ⅰ.对于过程的输入值,使用setXXX,对于输出值使用registerOutParameter(?,类型),这里需要注意
						输出值所对应问号的顺序,同时需要考虑类型(OracleTypes.VARCHAR2,...);
					Ⅱ.取出过程的返回值方法是CallableStatement提供的getXXX(输出的参数的位置),
						同时考虑输出的参数类型
		8.3.Java调用有返回值的存储过程(返回值为列表集合,即多行数据)	
			案例:编写一个过程,输入部门号,返回该部门所有雇员信息;
			分析:由于Oracle存储过程没有返回值,它的所有返回值都是通过out参数来替代的,列表同样不列外,但由于是集合,所以不能用一般的参数,必须使用到package了:
				步骤如下:
				(1)建立一个包,该包中定义一个游标类型	
					create or replace package pack1 is
					--定义一个游标数据类型
					type my_cursor is ref cursor;
					end;
					/
				(2)建立存储过程		
					create or replace procedure pro12(v_in_deptno in number,v_out_result out pack1.my_cursor)is
					begin
						open v_out_result for select * from emp where deptno=v_in_deptno;
						--不能在过程中关闭游标
						--close v_in_deptno
					end;
					/
				(3)在Java中调用该过程:关键代码
						CallableStatement cs = con.prepareCall("{call pro12(?,?)}");
						cs.setInt(1, 20);
						cs.registerOutParameter(2, OracleTypes.CURSOR);
						cs.execute();
						ResultSet rs = (ResultSet) cs.getObject(2);
						while(rs.next()){
							System.out.println(rs.getString("ename") + "," + rs.getDouble("sal"));							
						}
		8.4.pl/sql分页:
			需求:编写一个存储过程,要求可以输入表名,每页显示记录数,当前页,返回总记录数,总页数,结果集
				create or replace procedure pagingPro(
					v_in_table in varchar2,v_in_pagesiez in number,v_in_pagenow in number,
					v_out_result out pack1.my_cursor,v_out_rows out number,v_out_pagecount out number)	is
					--定义变量
					v_sql varchar2(2000);
					v_start number;
					v_end number;
					v_rows_cursor pack1.my_cursor;
				begin
					--执行分页代码
					v_start:= v_in_pagesiez * (v_in_pagenow - 1) + 1;
					v_end:= v_in_pagesiez * v_in_pagenow;
					v_sql:='select t2.* from (select t1.*,rownum rn from(select * from '||v_in_table||')t1 where rownum<='||v_end||') t2 where rn>='||v_start;
					--打开游标,让游标指向结果集
					open v_out_result for v_sql;
					--查询输入的表有多少条记录
					v_sql:='select count(*) from ' ||v_in_table;
					open v_rows_cursor for v_sql;
					loop
						fetch v_rows_cursor into v_out_rows;
						exit when v_rows_cursor%notfound;
					end loop;
					close v_rows_cursor;
					if mod(v_out_rows,v_in_pagesiez)=0 then v_out_pagecount:=v_out_rows/v_in_pagesiez;
					else v_out_pagecount:=v_out_rows/v_in_pagesiez + 1; 
					end if;
				end;
				/
	9.视图(view):Oracle中一种数据对象
		是一种虚拟表,其内容由查询定义,同真实表一样;主要是简化操作,提高安全,满足不同用户的查询需求;
		视图不是一个真正存在的物理表;
		9.1.创建视图基本语法
			create view 视图名 as select 语句 [with read only]
			--with read only 则表示该视图只能读,不能进行其他操作
			如果创建视图时,不希望通过视图对源表进行操作,可以加上with read only
		9.2.需求:查询emp的名字,部门编号和部门名称
			create or replace view myview as select emp.ename,emp.deptno,dept.dname
			from emp,dept where emp.deptno=dept.deptno;
		9.3.视图管理:
			(1)创建或修改视图:
				create or replace view 视图 as select语句[可以是多表语句] [with read only]
			(2)删除视图:
				drop view 视图名;
		9.4.视图和表区别:
			①表需要占用磁盘空间,而视图不需要;
			②视图不能加索引;
			③视图可以简化操作;可以将多表查询的结果创建为一个视图;
			④提高操作安全性;使得不同的用户看到不同的视图;
	10.触发器
		10.1.基本概念:
			是指存放在数据库中,被隐含执行的存储过程,可以支持dml触发器,还支持基于系统事件
			(启动数据库,关闭数据库,登陆和ddl操作建立触发器);
		10.2.触发器分类:
			◆在Oracle中dml语句需要手动提交(如果没有手动提交,当退出控制台时,Oracle会自动提交),
				ddl语句会自动提交
			(1).dml触发器			
				①创建触发器			
					create [or replace] trigger 触发器名称{before |after}--在操作前还是操作后
					{insert|delete|update[of column[,column....]}--什么操作
					on [scheme.]表名 --操作的表名
					[for each row] --是否针对某一行
					[when 条件表达式]
					begin
						trigger_body;--触发器体
					end;
					/
				②入门案例:
					Ⅰ--在某张表添加一条数据的时候,提示'添加了一条数据'
						create or replace trigger tri1 after insert on scott.my_emp
						begin
						dbms_output.put_line('添加了一条数据');
						end;
						/			
					◆注:行级触发器和语句级触发器
					Ⅱ--在某张表修改多条数据时,提示多次修改了数据
						create or replace trigger tri2 after update on scott.emp
						for each row--表示这是一个行级触发器
						begin
						dbms_output.put_line('修改了一条数据');
						end;
						/
					Ⅲ--禁止工作人员在休息日改变员工信息;
						create or replace trigger tri3 before delete on scott.emp
						begin
						if to_char(sysdate,'day') in('星期五','星期六')then
						dbms_output.put_line('Sorry,in the weekend, you can not delete the emp');
						RAISE_APPLICATION_ERROR(-20000,'Sorry,in the weekend, you can not delete the emp');
						end if;
						end;
						/
					◆说明:RAISE_APPLICATION_ERROR这个过程是oracle提供的,可以传入两个参数;
						第一个自定义的错误号,-20000,-20999;第二个参数提示错误信息;
						抛出异常,使之不能执行对应的操作
				③使用条件谓词:当触发器同时包含多个触发时间时(insert,delete,update),
					可以使用条件谓词来精确提示用户;inserting,updating,deleting
					create or replace trigger tri3
					before insert or update or delete on scott.emp
					begin
						case
							when inserting then
								 dbms_output.put_line('do not insert');
								 RAISE_APPLICATION_ERROR(-20001,'do not insert');
							when updating then 
								 dbms_output.put_line('do not modify');
								 RAISE_APPLICATION_ERROR(-20003,'do not modify');
							when deleting then
								 dbms_output.put_line('do not delete');
								 RAISE_APPLICATION_ERROR(-20003,'do not delete');
						end case;
					end;
					/
				④:old与:new
				当触发器被触发时,要使用被插入、更新或删除的记录中的列值,有时要使用操作前、后列的值
					Ⅰ.案例:在修改emp表雇员的薪水时,显示雇员工资修改前和修改后的值;
							 确保在修改员工的工资不能低于原有工资;
						create or replace trigger tri4 before update on scott.emp
						for each row
						begin
							if :new.sal<:old.sal then
								dbms_output.put_line('工资不能低于原来的工资');
								RAISE_APPLICATION_ERROR(-20005,'工资不能低于原来的工资');
							else
								dbms_output.put_line('原来的工资:'||:old.sal ||',现在工资是:' ||:new.sal);
							end if;
						end;
						/
					思考:有一个student表和class表,删除student表的一个学生后,相应的class表的人数减一;
			(2).ddl触发器:针对create,alter,drop操作的--系统管理员来完成触发器的建立
				用来记录在Oracle中发生的ddl操作
				①基本语法:
					create or replace trigger 触发器名
					after ddl on 方案名.schema
					begin
						--执行语句
					end;
					/					
				②案例:编写触发器,记录某个用户的ddl操作
					--创建一个表
					create table my_ddl_record(
						event varchar2(64),
						username varchar2(64),
						ddl_time date
					);
					--创建触发器
					create or replace trigger ddl_trigger after ddl on scott.schema
					begin
					insert into my_ddl_record values(ora_sysevent,ora_login_user,sysdate);
					end;
					/
			(3).系统触发器(与系统相关的触发器)--系统管理员来完成触发器的建立
				①系统事件是指基于Oracle事件(登录登出数据库,启动退出数据库)所建立的触发器,
				通过使用系统触发器提供了跟踪系统或数据库变化的机制;
				②常用事件属性函数:
					ora_client_ip_address---返回客户端的ip
					ora_database_name---返回数据库名
					ora_login_user---返回登录用户名;
					ora_sysevent---返回触发触发器的系统事件名;
					ora_des_encrypted_password--返回用户des(md5)加密后的密码
				③建立登录和退出触发器
					Ⅰ.基本语法:
							create orreplace trigger 触发器名
							after[before] logon[logoff] on database
							begin
							--执行语句
							end;
					Ⅱ.案例:完成登录和退出的触发器
							--创建一张表,用于保存用户登录和退出的情况(以sys用户创建表格)
							create table log_table(
								 username varchar2(20),
								 logon_time date,
								 logoff_time date,
								 address varchar2(20)
							);
							--创建登录触发器
							create or replace trigger log_trigger	after logon on database
							begin
								insert into log_table(username,logon_time,address) 
								values(ora_login_user,sysdate,ora_client_ip_address);
							end;
							/
							--退出触发器
							create or replace trigger logoff_trigger
							before logoff on database
							begin
								insert into log_table(username,logoff_time,address) 
								values(ora_login_user,sysdate,ora_client_ip_address);
							end;
							/
		10.3.触发器的管理:
			(1).禁止触发器:指让触发器临时失效
				alter trigger 触发器名 disable;
			(2).激活触发器
				alter trigger 触发器名 enable;
			(3).禁止或激活表的所有触发器
				alter table 表名 disable all triggers;
				alter table 表名 enable all triggers;
			(4).删除触发器
				drop trigger 触发器名
			(5).Java操作数据库时,也会触发相应的触发器;
	11.pl/sql例外处理
		11.1.例外基本概念:
			pl/sql在执行过程中发生异常时系统所作的处理称为一个例外情况;
			通常例外有三种:
			(1)Oracle预定义的例外情况
			(2)非预定义的oracle例外情况有使用者增加例外情况,然后oracle自动将其触发执行;
			(3)自定义例外
			捕获异常的目的:
				给用户提示更加明确;可能需要对异常进行业务处理;				
		11.2.基本语法
			Exception
			when <异常情况名> then
				<异常处理代码>
			when <异常情况名> then
				<异常处理代码>
			....
			when others then
				<异常处理代码>
		11.3.如果不处理例外,出现啥情况:
# 十二.数据库管理员(dba)和数据库中表的备份与恢复;
	1.数据库管理员(dba)
		(1)主要职责:
			①安装和升级oracle数据库;
			②建表,表空间,表,视图,索引,...
			③制定并实施备份与恢复计划;
			④数据库权限管理,优化,故障排除;
			⑤对于高级dba,要求能参与项目开发,会编写sql语句、存储过程、触发器、规则、约束、包;
		(2).管理数据库的用户:sys,system,主要区别:
			①存储数据的重要性不同
				sys:所有oracle的数据字典的基表和视图都存放在sys用户中,有数据库自己维护,任何用户都不能手动
				更改,sys用户用于dba(角色)、sysdba(系统权限)、sysoper(系统权限)角色或权限,是权限最高的用户;
				system:用于存放次一级的内部数据,如oracle的一些特性或工具的管理信息,
					system用户拥有dba,sysdba或系统权限;
			②权限不同:
				sys用户必须以as sysdba或as sysoper形式登录,不能以normal方式登录数据库;
				system如果正常登录,其实就是一个普通的dba用户;但如果以as sysdba登录,实际上还是以sys用户登录的
		(3)管理初始化参数:
			①显示初始化参数命令:
				show parameter
			②修改参数:init.ora
	11.2.数据库的表的备份与恢复
		使用import和export对数据库(方案,表)进行备份与恢复
		(1)介绍:逻辑备份只能在open状态下进行;
			①逻辑备份是使用工具export将数据对象额结构和数据导出到文件的过程;
			②逻辑恢复是指当数据库对象被误操作而损坏后使用工具import利用备份的文件把数据对象导入到数据库的过程;
			③物理备份可在数据库open的状态下进行也可在关闭数据库后进行;
		(2)导出表:(dos操作)--导出表后缀一般是以.dmp			
			①导出自己的表
				exp userid=用户名/密码@数据库实例名 tables=(表名1,表名2,...) file=备份路径
			②导出其它方案的表:需要dba权限或exp_full_database的权限
				exp userid=用户名/密码@数据库实例名 tables=(方案名.表名1,...) file=备份路径
			③导出表的结构:
				exp userid=用户名/密码@数据库实例名 tables=(表名1,...) file=备份路径 rows=n;
			④使用直接导出:比默认的常规方式速度要快,当数据量大时,可以考虑这样的方式
				这里需要数据库的字符集与客户端的字符集完全一致,否则会报错
				exp userid=用户名/密码@数据库实例名 tables=(表名1,...) file=备份路径 direct=y
			⑤导出方案:把方案整体导出
				exp userid=用户名/密码@数据库实例名 owner=用户名 file=备份路径
				如果导出别的方案,一般使用system用户,可以导出多个方案
				exp userid=用户名/密码@数据库实例名 owner=(所有这1,所有者2,..) file=备份路径
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
			
		
		
		
		
		
		