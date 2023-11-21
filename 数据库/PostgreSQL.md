# 一、基本介绍

PostgreSQL 是一种先进的企业级开源关系型数据库系统。PostgreSQL 支持 SQL（关系型）和 JSON（非关系型）查询。

PostgreSQL 是许多网络应用程序以及移动和分析应用程序的主要数据库。

## 1、特点

- 用户自定义类型
- 表继承
- 先进的锁定机制
- 外键参照完整性
- 视图、规则、子查询
- 嵌套事务（保存点）
- 多版本并发控制（MVCC）
- 异步复制

适用场景：
- 用于Gis + 地图场景
- 普通事务数据库；


## 2、安装

### 2.1、windows安装

直接下载安装包，一步一步安装即可

### 2.2、Linux安装

### 2.3、Docker安装

- [Install postgresql with docker](https://www.baeldung.com/ops/postgresql-docker-setup)

```bash
$ docker pull postgres
$ docker run -itd -e POSTGRES_USER=root -e POSTGRES_PASSWORD=root -p 5432:5432 --name postgresql postgres
# pgAdmin
$ docker pull dpage/pgadmin4:latest
$ docker run --name pgadmin -p 5051:80 -e "PGADMIN_DEFAULT_EMAIL=743633145@qq.com" -e "PGADMIN_DEFAULT_PASSWORD=root" -d dpage/pgadmin4
```

### 2.4、Mac安装

### 2.5、连接PG

**适用psql**

psql 是 PostgreSQL 提供的交互式终端程序。它允许与 PostgreSQL 数据库服务器交互，如执行 SQL 语句和管理数据库对象。

其次，输入服务器、数据库、端口、用户名和密码等所有信息。如果按回车键，程序将使用方括号[]中指定的默认值，并将光标移到新行。例如，localhost 是默认的数据库服务器。在为用户 postgres 输入密码的步骤中，需要输入在安装 PostgreSQL 时选择的用户 postgres 的密码。

**适用pgAdmin**

连接数据库的第二种方法是使用 pgAdmin 应用程序。pgAdmin 应用程序允许你通过直观的用户界面与 PostgreSQL 数据库服务器交互。通过ip+端口的形式访问；

## 3、如何加载数据

`pg_restore -U postgres -d dvdrental <file>`
- `-U postgres`：指定登录 PostgreSQL 数据库服务器的 postgres 用户。
- `-d dvdrental`：指定要加载的目标数据库。

也可以通过pgAdmin加载数据

## 4、常见操作

进入交互界面：`psql`

切换数据库：`postgres=# \c dvdrental`

退出交互界面：`postgres=# \q`

# 二、基础知识

> 请注意，SQL 关键字不区分大小写。这意味着 SELECT 等同于 select 或 Select。按照惯例，将使用大写的所有 SQL 关键字，以便查询更容易阅读。分号不是 SQL 语句的一部分。它是 PostgreSQL 结束 SQL 语句的信号。分号也用于分隔两条 SQL 语句。

## 1、select

基本语法：
```sql
SELECT select_list FROM table_name;
```
可以在 select_list 中使用表达式，比如字符串拼接：
```sql
SELECT  first_name || ' ' || last_name, email FROM customer;
```
其中`||` 是连接运算符；

当然也可以是下面的示例：
```sql
SELECT 5 * 3;
```

## 2、列别名

别名的语法如下：
```sql
SELECT column_name AS alias_name FROM table_name;
SELECT column_name alias_name FROM table_name;
SELECT expression AS alias_name FROM table_name;
-- 如果列别名包含一个或多个空格，则需要用双引号将其包围起来，如下所示
SELECT first_name || ' ' || last_name "full name" FROM customer;
```

## 3、order by

```sql
SELECT select_list FROM table_name
ORDER BY
	sort_expression1 [ASC | DESC],
        ...
	sort_expressionN [ASC | DESC]; -- 默认排序是：ASC
```
执行顺序：`FROM, SELECT, ORDER BY`

函数 LENGTH() 接受一个字符串，并返回该字符串的长度，以下语句选择姓名及其长度。它按姓名长度对行排序：
```sql
SELECT first_name, LENGTH(first_name) len FROM customer ORDER BY len DESC;
```

**order by 和 NULL**

在数据库领域，`NULL` 是一个标记，表示数据缺失或记录时数据未知。对包含 `NULL` 的记录进行排序时，可以使用 `ORDER BY` 子句的 `NULLS FIRST` 或 `NULLS LAST` 选项指定 `NULL` 与其他非空值的排序顺序。
```sql
ORDER BY sort_expresssion [ASC | DESC] [NULLS FIRST | NULLS LAST]
```
`NULLS FIRST` 选项将 `NULL` 置于其他非空值之前，`NULL LAST` 选项将 `NULL` 置于其他非空值之后。
- 如果使用 `ASC` 选项，`ORDER BY` 子句默认使用 `NULLS LAST` 选项；
- 如果使用 `DESC` 选项，`ORDER BY` 子句默认使用 `NULLS FIRST` 选项；

## 4、distinct

DISTINCT 子句在 SELECT 语句中用于删除结果集中的重复行。DISTINCT 子句为每组重复行保留一条记录。DISTINCT 子句可应用于 SELECT 语句选择列表中的一列或多列：
```sql
SELECT DISTINCT column1, column2 FROM table_name; -- 需要多个字段值，需要将两列的值组合起来确定唯一性
SELECT DISTINCT ON (column1) column_alias, column2 FROM table_name ORDER BY column1, column2; -- 对 column1 去重，并对每组重复的结果集保留第一行。
```
SELECT 语句返回记录的顺序未指定，因此重复数据中每组的 "第一 "行也未指定。好的做法是始终使用 ORDER BY 子句和 DISTINCT ON(表达式)，以使结果集可预测。

> 请注意，DISTINCT ON 表达式必须与 ORDER BY 子句中最左边的表达式相匹配。

## 5、where

```sql
SELECT select_list FROM table_name WHERE condition ORDER BY sort_expression
```
条件必须为 "true"、"false "或 "未知"。它可以是一个布尔表达式，也可以是使用 AND 和 OR 运算符的布尔表达式组合

执行顺序：`FROM, WHERE, SELECT, ORDER BY`；

除了 SELECT 语句，您还可以在 UPDATE 和 DELETE 语句中使用 WHERE 子句指定要更新或删除的记录；

操作符，大部分同MySQL，其中：`NOT`-否定其他运算符的结果，所有操作符：
`= 、 > 、 < 、 >= 、 <= 、 <> or != 、 AND 、 OR 、 IN 、 BETWEEN.AND. 、 LIKE 、 IS NULL 、 NOT`

## 6、limit

```sql
SELECT select_list FROM table_name WHERE condition ORDER BY sort_expression LIMIT row_count
```
语句返回查询生成的 `row_count` 行数。如果 `row_count` 为零，查询将返回空结果集。如果 `row_count` 为 NULL，查询将返回相同的结果集，因为它没有 LIMIT 子句。

如果想在返回 `row_count` 行数之前跳过若干行，可以使用放在 LIMIT 子句之后的 OFFSET 子句，语句如下
```sql
SELECT select_list FROM table_name LIMIT row_count OFFSET row_to_skip;
```
语句首先会跳过 `row_too_skip` 行，然后才会返回查询生成的 `row_count` 行。如果 `row_too_skip` 为零，语句就会像没有 `OFFSET` 子句一样工作；由于表可能会以未指定的顺序存储行，因此在使用 LIMIT 子句时，应始终使用 ORDER BY 子句来控制行的顺序。如果不使用 ORDER BY 子句，可能会得到不指定记录顺序的结果集

## 7、fetch

要限制查询返回的行数，通常要使用 `LIMIT` 子句。许多关系数据库管理系统（如 MySQL、H2 和 HSQLDB）都广泛使用 `LIMIT` 子句。但是，`LIMIT` 子句并不是 SQL 标准的；

为了与 SQL 标准保持一致，PostgreSQL 支持 `FETCH` 子句来检索查询返回的记录数。请注意，`FETCH` 子句是在 `SQL:2008` 中引入的：
```sql
OFFSET start { ROW | ROWS } FETCH { FIRST | NEXT } [ row_count ] { ROW | ROWS } ONLY
```
- `ROW` 是 `ROWS` 的同义词，`FIRST` 是 `NEXT` 的同义词。因此可以互换使用
- `start` 是一个整数，必须为零或正数。如果没有指定 `OFFSET` 子句，默认值为零。如果起始行数大于结果集中的行数，则不会返回任何行；
- `row_count` 为 1 或更大。如果没有明确指定，默认情况下 `row_count` 值为 1。

由于未指定表中存储行的顺序，因此应始终使用带有 ORDER BY 子句的 FETCH 子句，以使返回结果集中行的顺序一致；

> 请注意，在 SQL:2008 中，`OFFSET` 子句必须出现在 `FETCH` 子句之前。但在 PostgreSQL 中，`OFFSET` 和 `FETCH` 子句可以以任何顺序出现；

**FETCH vs. LIMIT**

FETCH 子句在功能上等同于 LIMIT 子句。如果计划使应用程序与其他数据库系统兼容，则应使用 FETCH 子句，因为它遵循标准 SQL。

示例：
```sql
-- 下面的查询使用 FETCH 子句选择按片名升序排序的第一部影片，以下两个SQL是等价的
SELECT * FROM  film ORDER BY title FETCH FIRST ROW ONLY;
SELECT * FROM  film ORDER BY title FETCH FIRST 1 ROW ONLY;

-- 下面的查询使用FETCH子句来选择按标题排序的前五部电影:
SELECT * FROM  film ORDER BY title FETCH FIRST 5 ROW ONLY;

-- 以下语句按片名排序，返回前五部影片之后的后五部影片
SELECT * FROM  film ORDER BY title OFFSET 5 ROWS FETCH FIRST 5 ROW ONLY;
```

# 参考资料

- [PostgreSQL官方文档](https://www.postgresql.org/docs/current/tutorial.html)
- [Postgresql Tutorial](https://www.postgresqltutorial.com/)
- [PostgreSQL作为搜索引擎](https://xata.io/blog/postgres-full-text-search-engine)
