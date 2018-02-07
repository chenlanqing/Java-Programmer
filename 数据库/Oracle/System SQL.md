#### Oracle一些重要的查询

###### 查询当前用户的所有表(自己的表)
	select table_name from user_tables;
###### 查询Oracle中所有的系统权限,一般是DBA
	select * from system_privilege_map order by name;
###### 查询Oracle所有的角色,一般是DBA;
	select * from dba_roles;
###### 查询Oracle中所有对象权限
	select distinct privilege from dba_tab_privs;
###### 查询数据库的表空间
	select tablespace_name from dba_tablespaces;
	
###### 查询当前用户具有什么样的系统权限
	select * from user_sys_privs
###### 查询当前用户在其他用户的表上具有什么样的对象权限
	select * from user_tab_privs; -- 查看对表的权限
	select * from user_col_privs; -- 查看对表中列的权限
###### 查看某个用户具有怎样的角色
	select * from dba_role_privs where grantee='用户名'; -- 用户名大写

###### 查看某个角色包括哪些系统权限
	select * from dba_sys_privs where grantee='DBA';
	select * from role_sys_privs where role='DBA';
###### 查看某个角色包括的对象权限
	select * from dba_tab_privs where grantee='角色名';
###### 显示用户具有的角色和默认角色
	select granted_role,default_role from dba_role_privs where grantee='用户名';--用户名大写
###### 显示执行的错误信息,在执行完语句之后,就执行下列语句
	show  error
###### 显示oracle某个操作的时间
	set timing on
