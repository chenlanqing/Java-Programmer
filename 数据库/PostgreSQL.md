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

## 8、in

在 WHERE 子句中使用 IN 操作符来检查某个值是否与值列表中的任何值相匹配：
```sql
value IN (value1,value2,...)
```
值列表可以是数字、字符串等文字值列表，也可以是 SELECT 语句的结果，如下所示：`value IN (SELECT column_name FROM table_name);`

**NOT IN**

选择其值与列表中的值不匹配的行

## 9、between

可以使用 BETWEEN 操作符将一个值与一系列值进行匹配。下面是 BETWEEN 操作符的语法说明：
```sql
value BETWEEN low AND high;
```
如果值大于或等于低值，小于或等于高值，表达式返回 true，否则返回 false。
等价于：
```sql
value >= low and value <= high
```
如果要检查某个值是否超出某个范围，可以将 NOT 运算符与 BETWEEN 运算符组合使用，如下所示：
```sql
value NOT BETWEEN low AND high;
-- 等价于
value < low OR value > high
```

## 10、like

将字面值与通配符结合起来构建模式，然后使用 LIKE 或 NOT LIKE 操作符查找匹配项。PostgreSQL 提供了两种通配符：
- 百分号 (`%`) 匹配零个或多个字符的任何序列。
- 下划线符号 (`_`) 匹配任何单个字符。

```sql
value LIKE pattern
value NOT LIKE pattern
```
如果模式不包含任何通配符，LIKE 运算符的行为与等号 ( =) 运算符类似。
```sql
SELECT
	'foo' LIKE 'foo', -- true
	'foo' LIKE 'f%', -- true
	'foo' LIKE '_o_', -- true
	'bar' LIKE 'b_'; -- false
```
PostgreSQL 支持 `ILIKE` 操作符，其工作原理与 `LIKE` 操作符类似。此外，`ILIKE` 运算符不区分大小写的匹配值，比如：
```sql
SELECT first_name, last_name FROM customer WHERE first_name ILIKE 'BAR%';
```
`BAR%`匹配任何以 `BAR`、`Bar`、`BaR` 等开头的字符串。如果使用 LIKE 操作符，查询将不会返回任何行

如下所示，PostgreSQL 还提供了一些类似于 `LIKE`、`NOT LIKE`、`ILIKE` 和 `NOT ILIKE` 操作符的操作符：

操作符 | 等价
------|-----
`~~` | LIKE
`~~*` | ILIKE
`!~~` |	NOT LIKE
`!~~*` |	NOT ILIKE

## 11、is null

在数据库世界中，NULL 表示缺少信息或不适用。NULL 不是一个值，因此不能与任何其他值（如数字或字符串）进行比较。将 NULL 与某个值进行比较的结果总是 NULL，即未知结果。此外，NULL 不等于 NULL，因此下面的表达式返回 NULL：
```sql
NULL = NULL
```
要检查一个值是否为 NULL，可以使用 `IS NULL` 操作符来代替：`value IS NULL`；如果值为 NULL，表达式返回 true；如果不是 NULL，表达式返回 false。

**IS NOT NULL**

检查值为非空，则使用 `is not null`：`value IS NOT NULL`

## 12、关联表：Joins

- 内连接
- 左连接
- 右连接
- 自连接
- 全外连接

## 13、交叉连接：cross join

通过 CROSS JOIN 子句，可以生成两个或多个表中记录的笛卡尔积。与 `LEFT JOIN` 或 `INNER JOIN` 等其他连接子句不同，`CROSS JOIN` 子句没有连接谓词。

假设要对两个表 T1 和 T2 进行交叉连接。如果 T1 有 n 行，T2 有 m 行，那么结果集将有 nxm 行。例如，T1 有 1,000 行，T2 有 1,000 行，则结果集将有 1,000 x 1,000 = 1,000,000 行。
```sql
SELECT select_list FROM T1 CROSS JOIN T2;
-- 等价于
SELECT select_list FROM T1, T2;
-- 也可以使用 inner join 模拟
SELECT * FROM T1 INNER JOIN T2 ON true;
```

## 14、自然连接：natural join

自然连接是根据连接表中相同的列名创建隐式连接的连接。
```sql
SELECT select_list FROM T1
NATURAL [INNER, LEFT, RIGHT] JOIN T2;
```
自然连接可以是内连接、左连接或右连接。如果没有明确指定连接，例如内连接、左连接、右连接，**PostgreSQL 默认使用内连接**。

## 15、group by

GROUP BY 子句将 SELECT 语句返回的记录分成若干组。对于每个组，可以使用聚合函数，例如使用 SUM() 计算项的总和，或使用 COUNT() 获得组中项的数量。
```sql
SELECT 
   column_1, 
   column_2,
   ...,
   aggregate_function(column_3)
FROM 
   table_name
GROUP BY 
   column_1,
   column_2,
   ...;
```
可以将 SELECT 语句的其他子句与 GROUP BY 子句一起使用。

PostgreSQL 在 **FROM 和 WHERE 子句之后** `GROUP BY`  **HAVING SELECT、DISTINCT、ORDER BY 和 LIMIT 子句之前**执行  子句。

## 16、Having

HAVING 子句指定了组或汇总的搜索条件。HAVING 子句通常与 GROUP BY 子句一起使用，用于根据指定条件筛选组或汇总表：
```sql
SELECT column1, aggregate_function (column2)
FROM table_name
GROUP BY
	column1
HAVING
	condition;
```
HAVING 在 group by 语句之后执行，在select语句之前执行；由于 HAVING 子句先于 SELECT 子句求值，因此不能在 HAVING 子句中使用列别名。因为在执行 HAVING 子句时，SELECT 子句中指定的列别名不可用。

**HAVING vs. WHERE**
- 通过 WHERE 子句，可以根据指定条件过滤记录。而 HAVING 子句则可以根据指定条件过滤记录组。
- 换句话说，WHERE 子句适用于记录，而 HAVING 子句适用于记录组

## 17、union

UNION 运算符将两个或多个 SELECT 语句的结果集合并为一个结果集：
```sql
SELECT select_list_1 FROM table_expresssion_1
UNION
SELECT select_list_2 FROM table_expression_2
```
要使用 UNION 操作符合并两个查询的结果集，查询必须符合以下规则：
- 两个查询的选择列表中列的数量和顺序必须相同。
- 数据类型必须兼容。

UNION 操作符会删除合并数据集中的所有重复行。如果要保留重复行，可以使用 `UNION ALL` 来代替。

**UNION 集合 ORDER BY**

UNION 运算符可以将第一个查询结果集中的记录放在第二个查询结果集中的记录之前、之后或之间。要对最终结果集中的记录进行排序，可以在第二个查询中使用 ORDER BY 子句。
```sql
SELECT * FROM top_rated_films
UNION ALL
SELECT * FROM most_popular_films
ORDER BY title;
```
如果将 ORDER BY 子句放在每个查询的末尾，合并后的结果集将不会按照预期进行排序。因为当 UNION 操作符组合每个查询的排序结果集时，并不保证最终结果集中记录的顺序。

## 18、intersect

与 UNION 和 EXCEPT 操作符一样，PostgreSQL INTERSECT 操作符将两个或多个 SELECT 语句的结果集合并为一个结果集。INTERSECT 操作符会返回两个结果集的交集；
```sql
SELECT select_list FROM A
INTERSECT
SELECT select_list FROM B;
```
要使用 INTERSECT 操作符，SELECT 语句中出现的列必须遵循以下规则：
- 列的数量及其在 SELECT 语句中的顺序必须相同。
- 列的数据类型必须兼容。

如果要对 INTERSECT 操作符返回的结果集进行排序，可以将 ORDER BY 放在查询列表的最后一个查询处，如下所示：
```sql
SELECT select_list FROM A
INTERSECT
SELECT select_list FROM B
ORDER BY sort_expression;
```

## 19、except

与 UNION 和 INTERSECT 操作符一样，EXCEPT 操作符通过比较两个或多个查询的结果集来返回记录。

EXCEPT 操作符返回第一个（左侧）查询中不在第二个（右侧）查询输出中的不同记录，即只保留 A 中的数据跟B中不同的数据；
```sql
SELECT select_list FROM A
EXCEPT 
SELECT select_list FROM B;
```
涉及 "except "的查询需要遵循这些规则：
- 在两个查询中，列的数量及其顺序必须相同。
- 各列的数据类型必须兼容。


# 参考资料

- [PostgreSQL官方文档](https://www.postgresql.org/docs/current/tutorial.html)
- [Postgresql Tutorial](https://www.postgresqltutorial.com/)
- [PostgreSQL作为搜索引擎](https://xata.io/blog/postgres-full-text-search-engine)
