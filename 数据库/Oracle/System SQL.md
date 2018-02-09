<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Oracle一些重要的查询](#oracle%E4%B8%80%E4%BA%9B%E9%87%8D%E8%A6%81%E7%9A%84%E6%9F%A5%E8%AF%A2)
    - [查询当前用户的所有表(自己的表)](#%E6%9F%A5%E8%AF%A2%E5%BD%93%E5%89%8D%E7%94%A8%E6%88%B7%E7%9A%84%E6%89%80%E6%9C%89%E8%A1%A8%E8%87%AA%E5%B7%B1%E7%9A%84%E8%A1%A8)
    - [查询Oracle中所有的系统权限,一般是DBA](#%E6%9F%A5%E8%AF%A2oracle%E4%B8%AD%E6%89%80%E6%9C%89%E7%9A%84%E7%B3%BB%E7%BB%9F%E6%9D%83%E9%99%90%E4%B8%80%E8%88%AC%E6%98%AFdba)
    - [查询Oracle所有的角色,一般是DBA;](#%E6%9F%A5%E8%AF%A2oracle%E6%89%80%E6%9C%89%E7%9A%84%E8%A7%92%E8%89%B2%E4%B8%80%E8%88%AC%E6%98%AFdba)
    - [查询Oracle中所有对象权限](#%E6%9F%A5%E8%AF%A2oracle%E4%B8%AD%E6%89%80%E6%9C%89%E5%AF%B9%E8%B1%A1%E6%9D%83%E9%99%90)
    - [查询数据库的表空间](#%E6%9F%A5%E8%AF%A2%E6%95%B0%E6%8D%AE%E5%BA%93%E7%9A%84%E8%A1%A8%E7%A9%BA%E9%97%B4)
    - [查询当前用户具有什么样的系统权限](#%E6%9F%A5%E8%AF%A2%E5%BD%93%E5%89%8D%E7%94%A8%E6%88%B7%E5%85%B7%E6%9C%89%E4%BB%80%E4%B9%88%E6%A0%B7%E7%9A%84%E7%B3%BB%E7%BB%9F%E6%9D%83%E9%99%90)
    - [查询当前用户在其他用户的表上具有什么样的对象权限](#%E6%9F%A5%E8%AF%A2%E5%BD%93%E5%89%8D%E7%94%A8%E6%88%B7%E5%9C%A8%E5%85%B6%E4%BB%96%E7%94%A8%E6%88%B7%E7%9A%84%E8%A1%A8%E4%B8%8A%E5%85%B7%E6%9C%89%E4%BB%80%E4%B9%88%E6%A0%B7%E7%9A%84%E5%AF%B9%E8%B1%A1%E6%9D%83%E9%99%90)
    - [查看某个用户具有怎样的角色](#%E6%9F%A5%E7%9C%8B%E6%9F%90%E4%B8%AA%E7%94%A8%E6%88%B7%E5%85%B7%E6%9C%89%E6%80%8E%E6%A0%B7%E7%9A%84%E8%A7%92%E8%89%B2)
    - [查看某个角色包括哪些系统权限](#%E6%9F%A5%E7%9C%8B%E6%9F%90%E4%B8%AA%E8%A7%92%E8%89%B2%E5%8C%85%E6%8B%AC%E5%93%AA%E4%BA%9B%E7%B3%BB%E7%BB%9F%E6%9D%83%E9%99%90)
    - [查看某个角色包括的对象权限](#%E6%9F%A5%E7%9C%8B%E6%9F%90%E4%B8%AA%E8%A7%92%E8%89%B2%E5%8C%85%E6%8B%AC%E7%9A%84%E5%AF%B9%E8%B1%A1%E6%9D%83%E9%99%90)
    - [显示用户具有的角色和默认角色](#%E6%98%BE%E7%A4%BA%E7%94%A8%E6%88%B7%E5%85%B7%E6%9C%89%E7%9A%84%E8%A7%92%E8%89%B2%E5%92%8C%E9%BB%98%E8%AE%A4%E8%A7%92%E8%89%B2)
    - [显示执行的错误信息,在执行完语句之后,就执行下列语句](#%E6%98%BE%E7%A4%BA%E6%89%A7%E8%A1%8C%E7%9A%84%E9%94%99%E8%AF%AF%E4%BF%A1%E6%81%AF%E5%9C%A8%E6%89%A7%E8%A1%8C%E5%AE%8C%E8%AF%AD%E5%8F%A5%E4%B9%8B%E5%90%8E%E5%B0%B1%E6%89%A7%E8%A1%8C%E4%B8%8B%E5%88%97%E8%AF%AD%E5%8F%A5)
    - [显示oracle某个操作的时间](#%E6%98%BE%E7%A4%BAoracle%E6%9F%90%E4%B8%AA%E6%93%8D%E4%BD%9C%E7%9A%84%E6%97%B6%E9%97%B4)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

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
