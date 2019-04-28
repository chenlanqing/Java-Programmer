<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、Mybatis基础](#%E4%B8%80mybatis%E5%9F%BA%E7%A1%80)
  - [1、ORM框架对比](#1orm%E6%A1%86%E6%9E%B6%E5%AF%B9%E6%AF%94)
- [二、使用Mybatis一般过程](#%E4%BA%8C%E4%BD%BF%E7%94%A8mybatis%E4%B8%80%E8%88%AC%E8%BF%87%E7%A8%8B)
  - [1、创建配置文件](#1%E5%88%9B%E5%BB%BA%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6)
  - [2、创建实体类](#2%E5%88%9B%E5%BB%BA%E5%AE%9E%E4%BD%93%E7%B1%BB)
  - [3、将Mapper映射文件引入到基本配置文件](#3%E5%B0%86mapper%E6%98%A0%E5%B0%84%E6%96%87%E4%BB%B6%E5%BC%95%E5%85%A5%E5%88%B0%E5%9F%BA%E6%9C%AC%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6)
  - [4、获取sqlSessionFactory和sqlSession](#4%E8%8E%B7%E5%8F%96sqlsessionfactory%E5%92%8Csqlsession)
  - [5、根据相应的增删改查,调用sqlSession的方法](#5%E6%A0%B9%E6%8D%AE%E7%9B%B8%E5%BA%94%E7%9A%84%E5%A2%9E%E5%88%A0%E6%94%B9%E6%9F%A5%E8%B0%83%E7%94%A8sqlsession%E7%9A%84%E6%96%B9%E6%B3%95)
- [三、常见使用](#%E4%B8%89%E5%B8%B8%E8%A7%81%E4%BD%BF%E7%94%A8)
  - [1、mybatis完成两件事](#1mybatis%E5%AE%8C%E6%88%90%E4%B8%A4%E4%BB%B6%E4%BA%8B)
  - [2、typeAliases属性](#2typealiases%E5%B1%9E%E6%80%A7)
  - [3、字段名与实体类属性名不相同的冲突](#3%E5%AD%97%E6%AE%B5%E5%90%8D%E4%B8%8E%E5%AE%9E%E4%BD%93%E7%B1%BB%E5%B1%9E%E6%80%A7%E5%90%8D%E4%B8%8D%E7%9B%B8%E5%90%8C%E7%9A%84%E5%86%B2%E7%AA%81)
  - [4、关联表查询](#4%E5%85%B3%E8%81%94%E8%A1%A8%E6%9F%A5%E8%AF%A2)
  - [5、调用存储过程](#5%E8%B0%83%E7%94%A8%E5%AD%98%E5%82%A8%E8%BF%87%E7%A8%8B)
- [四、Mybatis与Spring整合](#%E5%9B%9Bmybatis%E4%B8%8Espring%E6%95%B4%E5%90%88)
- [参考资料](#%E5%8F%82%E8%80%83%E8%B5%84%E6%96%99)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 一、Mybatis基础

## 1、ORM框架对比
Hibernate与mybatis框架比较

### 1.1、Hibernate 的优点

- hibernate是全自动，hibernate完全可以通过对象关系模型实现对数据库的操作，拥有完整的JavaBean对象与数据库的映射结构来自动生成sql；
- 功能强大，数据库无关性好，O/R映射能力强，需要写的代码很少，开发速度很快；
- 有更好的二级缓存机制，可以使用第三方缓存；
- 数据库移植性良好；
- hibernate拥有完整的日志系统，hibernate日志系统非常健全，涉及广泛，包括sql记录).关系异常).优化警告).缓存提示).脏数据警告等
- 是面向对象支持的最好的框架了

### 1.2、Hibernate的缺点

- 学习门槛高，精通门槛更高，程序员如何设计O/R映射，在性能和对象模型之间如何取得平衡，以及怎样用好Hibernate方面需要的经验和能力都很强才行
- hibernate的sql很多都是自动生成的，无法直接维护sql；虽然有hql查询，但功能还是不及sql强大，见到报表等变态需求时，hql查询要虚，也就是说hql查询是有局限的；hibernate虽然也支持原生sql查询，但开发模式上却与orm不同，需要转换思维，因此使用上有些不方便.总之写sql的灵活度上hibernate不及mybatis；
- 在复杂关联中往往会带来严重的性能问题，也就是N+1的问题

### 1.3、Mybatis的优点

- 易于上手和掌握，提供了数据库查询的自动对象绑定功能，而且延续了很好的SQL使用经验，对于没有那么高的对象模型要求的项目来说，相当完美.
- sql写在xml里，便于统一管理和优化， 解除sql与程序代码的耦合.
- 提供映射标签，支持对象与数据库的orm字段关系映射
- 提供对象关系映射标签，支持对象关系组建维护
- 提供xml标签，支持编写动态sql.
- 速度相对于Hibernate的速度较快

### 1.4、Mybatis的缺点

- 关联表多时，字段多的时候，sql工作量很大.
- sql依赖于数据库，导致数据库移植性差.
- 由于xml里标签id必须唯一，导致DAO中方法不支持方法重载.
- 对象关系映射标签和字段映射标签仅仅是对映射关系的描述，具体实现仍然依赖于sql.
- DAO 层过于简单，对象组装的工作量较大.
- 不支持级联更新).级联删除.
- Mybatis 的日志除了基本记录功能外，其它功能薄弱很多.
- 编写动态sql时，不方便调试，尤其逻辑复杂时.
- 提供的写动态sql的xml标签功能简单，编写动态sql仍然受限，且可读性低


# 二、使用Mybatis一般过程

## 1、创建配置文件

创建基本配置文件mybatis-config.xml,该配置文件存放数据库连接信息以及引入mapper映射文件和缓存设置等待;
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!-- 设置数据库连接信息,通过外部配置文件 -->
    <properties resource="db.properties">	
    </properties>
    <typeAliases>
        <package name="com.mybatis.model" />
    </typeAliases>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC" />
            <dataSource type="POOLED">
                <property name="driver" value="${driver}" />
                <property name="url" value="${url}" />
                <property name="username" value="${username}" />
                <property name="password" value="${password}" />
            </dataSource>
        </environment>
    </environments>
    <!-- 引入mapper映射文件 -->
    <mappers>
        <mapper resource="com/mybatis/mapper/userMapper.xml"/>
        <mapper resource="com/mybatis/mapper/cuserMapper.xml"/>
    </mappers>
</configuration>
```

## 2、创建实体类

创建实体类及其对应的Mapper映射文件：如userMapper.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!-- 命名空间,一般跟Mapper映射接口一致 -->
<mapper namespace="com.mybatis.service.IUserService">
    <select id="queryUserById" parameterType="int" resultType="User">
        select * from user where id=#{id}
    </select>
    ...
</mapper>
```

## 3、将Mapper映射文件引入到基本配置文件
```xml
<mappers>
    <mapper resource="com/mybatis/mapper/userMapper.xml"/>
    <mapper resource="com/mybatis/mapper/cuserMapper.xml"/>
</mappers>
```

## 4、获取sqlSessionFactory和sqlSession
```java
String resource = "configuration.xml";
InputStream in = UserTestDemo.class.getClassLoader().getResourceAsStream(resource);
SqlSessionFactory ssf = new SqlSessionFactoryBuilder().build(in);
SqlSession session = ssf.openSession();
```

## 5、根据相应的增删改查,调用sqlSession的方法

# 三、常见使用

## 1、mybatis完成两件事

- 根据JDBC规范建立与数据库的连接；
- 通过Annotation/XML + Java反射技术，实现Java对象与关系数据库之间的相互转化；

## 2、typeAliases属性
类型别名是为 Java 类型命名一个短的名字。 它只和 XML 配置有关, 只用来减少类完全 限定名的多余部分
```xml
<typeAliases>
    <typeAlias type="com.neusoft.pojo.User" alias="User"/>
</typeAliases>
```
或者
```xml
<typeAliases>
    <package name="com.neusoft.pojo"/>
</typeAliases>
```
在实体类中通过注解形式
```java
@Alias("User")
public class User {
}
```
    
## 3、字段名与实体类属性名不相同的冲突

在Mapper文件中配置,如下所示:select元素中resultMap的值需跟定义的resultMap元素中id属性值一样
```xml
<mapper namespace="com.mybatis.orderMapper">
    <select id="getOrder" parameterType="int" resultMap="orderBean">
        <!--select order_id id,order_no orderNo,order_price price from orders where order_id=#{id} -->
        select * from orders where order_id=#{id}
    </select>
    <resultMap type="Order" id="orderBean">
        <id property="id" column="order_id"/>
        <result property="orderNo" column="order_no"/>
        <result property="price" column="order_price"/>
    </resultMap>
</mapper>
```  
如果是通过接口形式来实现的，那么在mapper的配置文件中namespace应该是接口的路径名
    
## 4、关联表查询

### 4.1、一对一查询

- association：用于一对一的关联查询的;
- property：对象属性的名称;
- javatype：对象属性的类型
- column：所对应的外键字段名称;
- select：使用另一个查询封装的结果

```xml
<mapper namespace="com.mybatis.classMapper">
    <!-- 方式一：嵌套结果：使用嵌套结果映射来处理重复的联合结果的子集
        封装联表查询的数据(去除重复的数据
    -->	
    <select id="getClass" parameterType="int" resultMap="classResultMap">
        select * from class c,teacher t where c.teacher_id=t.t_id and c.c_id=#{id}
    </select>
    
    <resultMap type="Classes" id="classResultMap">
        <id property="id" column="c_id"/>
        <result property="className" column="c_name"/>
        <association property="teacher" javaType="Teacher">
            <id property="id" column="t_id"/>
            <result property="teacherName" column="t_name"/>
        </association>
    </resultMap>
    
    <!-- 
        方式二：嵌套查询：通过执行另外一个SQL映射语句来返回预期的复杂类型
            SELECT * FROM class WHERE c_id=1;
            SELECT * FROM teacher WHERE t_id=1   //1 是上一个查询得到的teacher_id的值
    -->
    
    <select id="getClass2" parameterType="int" resultMap="classMap">
        select * from class where c_id=#{id}
    </select>
    <resultMap type="Classes" id="classMap">
        <id property="id" column="c_id"/>
        <result property="className" column="c_name"/>
        <association property="teacher" column="teacher_id" select="getTeacher">
        </association>
    </resultMap>
    <select id="getTeacher" parameterType="int" resultType="Teacher">
        select t_id id, t_name teacherName from teacher where t_id=#{id}
    </select>
</mapper>
```

### 4.2、一对多查询

- collection：做一对多关联查询的集合;
- ofType：指定集合中的类型
```xml
<!-- 
方式一: 嵌套结果: 使用嵌套结果映射来处理重复的联合结果的子集
SELECT * FROM class c, teacher t,student s WHERE c.teacher_id=t.t_id AND c.C_id=s.class_id AND  c.c_id=1
-->
<select id="getClass3" parameterType="int" resultMap="ClassResultMap3">
    select * from class c, teacher t,student s where c.teacher_id=t.t_id and c.C_id=s.class_id and  c.c_id=#{id}
</select>
<resultMap type="_Classes" id="ClassResultMap3">
    <id property="id" column="c_id"/>
    <result property="name" column="c_name"/>
    <association property="teacher" column="teacher_id" javaType="_Teacher">
        <id property="id" column="t_id"/>
        <result property="name" column="t_name"/>
    </association>
    <!-- ofType指定students集合中的对象类型 -->
    <collection property="students" ofType="_Student">
        <id property="id" column="s_id"/>
        <result property="name" column="s_name"/>
    </collection>
</resultMap>

<!-- 
    方式二：嵌套查询：通过执行另外一个SQL映射语句来返回预期的复杂类型
        SELECT * FROM class WHERE c_id=1;
        SELECT * FROM teacher WHERE t_id=1   //1 是上一个查询得到的teacher_id的值
        SELECT * FROM student WHERE class_id=1  //1是第一个查询得到的c_id字段的值
-->
<select id="getClass4" parameterType="int" resultMap="ClassResultMap4">
    select * from class where c_id=#{id}
</select>
<resultMap type="_Classes" id="ClassResultMap4">
    <id property="id" column="c_id"/>
    <result property="name" column="c_name"/>
    <association property="teacher" column="teacher_id" javaType="_Teacher" select="getTeacher2"></association>
    <collection property="students" ofType="_Student" column="c_id" select="getStudent"></collection>
</resultMap>

<select id="getTeacher2" parameterType="int" resultType="_Teacher">
    SELECT t_id id, t_name name FROM teacher WHERE t_id=#{id}
</select>

<select id="getStudent" parameterType="int" resultType="_Student">
    SELECT s_id id, s_name name FROM student WHERE class_id=#{id}
</select>
```
    
## 5、调用存储过程

- parameterMap：引用即传入值与输出值
- statementType：知道statement的真实类型;CALLABLE执行调用存储过程的语句
- mode：表示输出或输入
```xml
<select id="getCount" statementType="CALLABLE" parameterMap="getCountMap">
    call mybatis.get_user_count(?,?)
</select>
<parameterMap type="java.util.Map" id="getCountMap">
    <parameter property="sex_id" mode="IN" jdbcType="INTEGER"/>
    <parameter property="user_count" mode="OUT" jdbcType="INTEGER"/>
</parameterMap>
```

# 四、Mybatis与Spring整合

# 五、MyBatis原理

* [Mybatis初始化机制详解](https://blog.csdn.net/luanlouis/article/details/37744073)
* []()

# 六、MyBatis中的设计模式

http://www.crazyant.net/2022.html

# 参考资料

- [Hibernate与Mybatis对比](http://www.cnblogs.com/inspurhaitian/p/4647485.html)
