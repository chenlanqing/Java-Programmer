
# 一、Mybatis概述

## 1、ORM框架对比

Hibernate与mybatis框架比较

### 1.1、Hibernate 的优点

- hibernate是全自动，hibernate完全可以通过对象关系模型实现对数据库的操作，拥有完整的JavaBean对象与数据库的映射结构来自动生成sql；
- 功能强大，数据库无关性好，O/R映射能力强，需要写的代码很少，开发速度很快；
- 有更好的二级缓存机制，可以使用第三方缓存；
- 数据库移植性良好；
- hibernate拥有完整的日志系统，hibernate日志系统非常健全，涉及广泛，包括sql记录、关系异常、优化警告、缓存提示、脏数据警告等；
- 是面向对象支持的最好的框架了

### 1.2、Hibernate的缺点

- 学习门槛高，精通门槛更高，程序员如何设计O/R映射，在性能和对象模型之间如何取得平衡，以及怎样用好Hibernate方面需要的经验和能力都很强才行
- hibernate的sql很多都是自动生成的，无法直接维护sql；虽然有hql查询，但功能还是不及sql强大，见到报表等变态需求时，hql查询要虚，也就是说hql查询是有局限的；hibernate虽然也支持原生sql查询，但开发模式上却与orm不同，需要转换思维，因此使用上有些不方便。总之写sql的灵活度上hibernate不及mybatis；
- 在复杂关联中往往会带来严重的性能问题，也就是N+1的问题

### 1.3、Mybatis的优点

- 易于上手和掌握，提供了数据库查询的自动对象绑定功能，而且延续了很好的SQL使用经验，对于没有那么高的对象模型要求的项目来说，相当完美.
- sql写在xml里，便于统一管理和优化， 解除sql与程序代码的耦合.
- 提供映射标签，支持对象与数据库的orm字段关系映射
- 提供对象关系映射标签，支持对象关系组建维护
- 提供xml标签，支持编写动态sql.
- 速度相对于Hibernate的速度较快

### 1.4、Mybatis的缺点

- 关联表多时，字段多的时候，sql工作量很大；
- sql依赖于数据库，导致数据库移植性差；
- 由于xml里标签id必须唯一，导致DAO中方法不支持方法重载；
- 对象关系映射标签和字段映射标签仅仅是对映射关系的描述，具体实现仍然依赖于sql；
- DAO 层过于简单，对象组装的工作量较大；
- 不支持级联更新、级联删除；
- Mybatis 的日志除了基本记录功能外，其它功能薄弱很多；
- 编写动态sql时，不方便调试，尤其逻辑复杂时；
- 提供的写动态sql的xml标签功能简单，编写动态sql仍然受限，且可读性低；

## 2、为什么要使用mybatis等orm框架

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

在Mapper文件中配置，如下所示：select元素中resultMap的值需跟定义的resultMap元素中id属性值一样
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

## 6、批量插入

### 6.1、mybatis-foreach处理


### 6.2、mybatis代码处理


### 6.3、JDBC批处理 + 事务


### 6.4、数据分批 + JDBC批处理 + 事务



# 四、Mybatis与Spring整合

# 五、MyBatis原理

* [Mybatis初始化机制详解](https://blog.csdn.net/luanlouis/article/details/37744073)

## 1、mybatis的初始化

- 首先会创建SqlSessionFactoryBuilder建造者对象，然后由它进行创建SqlSessionFactory
- 然后会解析xml配置文件，实际为configuration节点的解析操作，还要解析transactionManager及datasource，最后将解析后的结果存到configuration对象中。
- 解析完MyBatis配置文件后，configuration就初始化完成了，然后根据configuration对象来创建SqlSession，到这里时，MyBatis的初始化的征程已经走完了。

## 2、mybatis的sql查询流程

- 调用selectOne方法进行SQL查询，selectOne方法最后调用的是selectList，在selectList中，会查询configuration中存储的MappedStatement对象，mapper文件中一个sql语句的配置对应一个MappedStatement对象，然后调用执行器进行查询操作。
- 执行器在query操作中，优先会查询缓存是否命中，命中则直接返回，否则从数据库中查询。
- 真正的doQuery操作是由SimplyExecutor代理来完成的，该方法中有2个子流程，一个是SQL参数的设置，另一个是SQL查询操作和结果集的封装。
    - 首先获取数据库connection连接，然后准备statement，然后就设置SQL查询中的参数值。打开一个connection连接，在使用完后不会close，而是存储下来，当下次需要打开连接时就直接返回。

# 六、Mybatis插件

- [IDEA Mybatis 插件](https://mp.weixin.qq.com/s/4R4-GjVfZsQlnMJ59FvPwQ)

## 1、插件原理

MyBatis Plugin 主要拦截的是 MyBatis 在执行 SQL 的过程中涉及的一些方法

MyBatis 底层是通过 Executor 类来执行 SQL 的。Executor 类会创建 StatementHandler、ParameterHandler、ResultSetHandler 三个对象，并且，首先使用 ParameterHandler 设置 SQL 中的占位符参数，然后使用 StatementHandler 执行 SQL 语句，最后使用 ResultSetHandler 封装执行结果。所以，我们只需要拦截 Executor、ParameterHandler、ResultSetHandler、StatementHandler 这几个类的方法，基本上就能满足我们对整个 SQL 执行流程的拦截了；

Mybatis插件通过代理模式来实现职责链的

（1）集成了 MyBatis 的应用在启动的时候，MyBatis 框架会读取全局配置文件，解析出 Interceptor实现类，并且将它注入到 Configuration 类的 InterceptorChain 对象中；解析完配置文件之后，所有的 Interceptor 都加载到了 InterceptorChain 中
```java
// org.apache.ibatis.builder.xml.XMLConfigBuilder
public class XMLConfigBuilder extends BaseBuilder {
    // 解析配置
    private void parseConfiguration(XNode root) {
        try {
            // ...
            pluginElement(root.evalNode("plugins")); // 解析插件
            // ...
        } catch (Exception e) {
            throw new BuilderException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
    }
    // 解析插件
    private void pluginElement(XNode parent) throws Exception {
        if (parent != null) {
            for (XNode child : parent.getChildren()) {
                String interceptor = child.getStringAttribute("interceptor");
                Properties properties = child.getChildrenAsProperties();
                // 创建Interceptor类对象
                Interceptor interceptorInstance = (Interceptor) resolveClass(interceptor).getDeclaredConstructor().newInstance();
                // 调用Interceptor上的setProperties()方法设置properties，单纯的属性设置，主要是为了方便通过配置文件配置 Interceptor 的一些属性值，没有其他作用
                interceptorInstance.setProperties(properties);
                // //下面这行代码会调用InterceptorChain.addInterceptor()方法
                configuration.addInterceptor(interceptorInstance);
            }
        }
    }
}
// org.apache.ibatis.session.Configuration#addInterceptor
public void addInterceptor(Interceptor interceptor) {
    interceptorChain.addInterceptor(interceptor);
}
```
（2）在执行 SQL 的过程中，MyBatis 会创建 Executor、StatementHandler、ParameterHandler、ResultSetHandler 这几个类的对象，对应的创建代码在 Configuration 类中：
```java
public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
    executorType = executorType == null ? defaultExecutorType : executorType;
    executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
    Executor executor;
    if (ExecutorType.BATCH == executorType) {
        executor = new BatchExecutor(this, transaction);
    } else if (ExecutorType.REUSE == executorType) {
        executor = new ReuseExecutor(this, transaction);
    } else {
        executor = new SimpleExecutor(this, transaction);
    }
    if (cacheEnabled) {
        executor = new CachingExecutor(executor);
    }
    executor = (Executor) interceptorChain.pluginAll(executor);
    return executor;
}

public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
    ParameterHandler parameterHandler = mappedStatement.getLang().createParameterHandler(mappedStatement, parameterObject, boundSql);
    parameterHandler = (ParameterHandler) interceptorChain.pluginAll(parameterHandler);
    return parameterHandler;
}
public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, RowBounds rowBounds, ParameterHandler parameterHandler, ResultHandler resultHandler, BoundSql boundSql) {
    ResultSetHandler resultSetHandler = new DefaultResultSetHandler(executor, mappedStatement, parameterHandler, resultHandler, boundSql, rowBounds);
    resultSetHandler = (ResultSetHandler) interceptorChain.pluginAll(resultSetHandler);
    return resultSetHandler;
}
public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
    StatementHandler statementHandler = new RoutingStatementHandler(executor, mappedStatement, parameterObject, rowBounds, resultHandler, boundSql);
    statementHandler = (StatementHandler) interceptorChain.pluginAll(statementHandler);
    return statementHandler;
}
```
这几个类对象的创建过程都调用了 InteceptorChain 的 pluginAll() 方法
```java
// org.apache.ibatis.plugin.InterceptorChain
public Object pluginAll(Object target) {
    for (Interceptor interceptor : interceptors) {
        // plugin() 是一个接口方法（不包含实现代码），需要由用户给出具体的实现代码，该方法会为目标对象创建一个代理对象；
        target = interceptor.plugin(target);
    }
    return target;
}
```
Plugin 是借助 Java InvocationHandler 实现的动态代理类。用来代理给 target 对象添加 Interceptor 功能。其中，要代理的 target 对象就是 Executor、StatementHandler、ParameterHandler、ResultSetHandler 这四个类的对象。wrap() 静态方法是一个工具函数，用来生成 target 对象的动态代理对象

等到真正执行具体方法的时候，其实是执行创建代理类时指定的InvocationHandler的invoke方法，可以发现在指定的InvocationHandler是Plugin对象，Plugin本身也是继承于InvocationHandler；首先从signatureMap中获取拦截类对应的方法列表，然后检查当前执行的方法是否在要拦截的方法列表中，如果在则调用自定义的插件interceptor，否则执行默认的invoke操作；interceptor调用intercept方法的时候是传入的Invocation对象；

只有 interceptor 与 target 互相匹配的时候，wrap() 方法才会返回代理对象，否则就返回 target 对象本身。怎么才算是匹配呢？那就是 interceptor 通过 `@Signature` 注解要拦截的类包含 target 对象，具体可以参看 wrap() 函数的代码实现；

它对同一个目标对象嵌套多次代理（也就是 InteceptorChain 中的 pluginAll() 函数要执行的任务）。每个代理对象（Plugin 对象）代理一个拦截器（Interceptor 对象）功能


## 2、编写插件

### 2.1、基本步骤

- 编写`org.apache.ibatis.plugin.Interceptor`的实现类；
- 使用`@Intercepts`注解完成插件的签名
- 将写好的插件注册到全局配置文件中（注意插件配置的顺序）

### 2.2、示例

- 编写实现类：
    ```java
    /**
    * 同 Signature注解 完成插件签名，告诉插件拦截哪个对象的哪个方法
    */
    @Intercepts({
        // 这里需要拦截的是 StatementHandler 里的 parameterize 方法
        @Signature(type = StatementHandler.class, method = "parameterize", args = Statement.class)
    })
    public class FirstMyBatisPlugin implements Interceptor {
        /**
        * 拦截目标对象的目标方法的执行
        */
        @Override
        public Object intercept(Invocation invocation) throws Throwable {
            // 执行目标方法
            System.out.println("FirstMyBatisPlugin....intercept：" + invocation.getMethod());
            Object proceed = invocation.proceed();
            return proceed;
        }
        /**
        * 包装目标对象，为目标对象创建一个代理对象，可以使用 Plugin.wrap 包装
        */
        @Override
        public Object plugin(Object target) {
            System.out.println("FirstMyBatisPlugin....plugin...需要包装的对象" + target);
            Object wrap = Plugin.wrap(target, this);
            return wrap;
        }
        /**
        * 将插件注册时的property属性设置进来
        */
        @Override
        public void setProperties(Properties properties) {
            System.out.println("插件属性：" + properties);
        }
    }
    ```
- 配置文件配置：
    ```xml
    <configuration>
        <properties>...</properties>
        <settings>...</settings>
        <typeAliases>...</typeAliases>
        <typeHandlers>...</typeHandlers>
        <objectFactory>...</objectFactory>
        <objectWrapperFactory>...</objectWrapperFactory>
        <plugins>
            <plugin interceptor="com.blue.fish.plugin.FirstMyBatisPlugin">
                // 配置属性，在setProperties方法中可以获取到这些属性
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </plugin>
        </plugins>
        <environments>...</environments>
        <databaseIdProvider>...</databaseIdProvider>
        <mappers>...</mappers>
    </configuration>
    ```
上面插件的输出结果：
```
插件属性：{password=root, username=root}
FirstMyBatisPlugin....plugin...需要包装的对象org.apache.ibatis.executor.CachingExecutor@5442a311
FirstMyBatisPlugin....plugin...需要包装的对象org.apache.ibatis.scripting.defaults.DefaultParameterHandler@56ac3a89
FirstMyBatisPlugin....plugin...需要包装的对象org.apache.ibatis.executor.resultset.DefaultResultSetHandler@4566e5bd
FirstMyBatisPlugin....plugin...需要包装的对象org.apache.ibatis.executor.statement.RoutingStatementHandler@ff5b51f
FirstMyBatisPlugin....intercept：public abstract void org.apache.ibatis.executor.statement.StatementHandler.parameterize(java.sql.Statement) throws java.sql.SQLException
```

### 2.3、SpringBoot配置插件

比如上面的配置：
```java
@Bean
public Interceptor firstMyBatisPlugin() {
    FirstMyBatisPlugin firstMyBatisPlugin = new FirstMyBatisPlugin();
    Properties properties = new Properties();
    properties.put("abc", "abc");
    firstMyBatisPlugin.setProperties(properties);
    return firstMyBatisPlugin;
}
```
`org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration`
```java
public MybatisAutoConfiguration(MybatisProperties properties, ObjectProvider<Interceptor[]> interceptorsProvider,
      ObjectProvider<TypeHandler[]> typeHandlersProvider, ObjectProvider<LanguageDriver[]> languageDriversProvider,
      ResourceLoader resourceLoader, ObjectProvider<DatabaseIdProvider> databaseIdProvider,
      ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
    ...
    this.interceptors = interceptorsProvider.getIfAvailable(); // 自动识别 org.apache.ibatis.plugin.Interceptor 的实现类
    ...
}
@Bean
@ConditionalOnMissingBean
public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
    SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
    ...
    if (!ObjectUtils.isEmpty(this.interceptors)) { // 添加拦截器
        factory.setPlugins(this.interceptors);
    }
    ...
}
```

## 3、多个插件

多个插件会产生多层代理，创建代理的时候，是按照配置的插件顺序创建层层代理的，执行目标方法是按照逆向顺序执行；

编写两个插件，两个插件拦截同一个对象的同一个方法，其代理对象的结构：

![](image/Mybatis-插件代理对象结构.png)

执行结果：

![](image/Mybatis-多个插件包装与执行顺序.png)

## 4、获取插件拦截对象的属性

```java
public Object intercept(Invocation invocation) throws Throwable {
    // 执行目标方法
    System.out.println("FirstMyBatisPlugin....intercept：" + invocation.getMethod());
    Object target = invocation.getTarget();
    System.out.println("当前对象：" + target);
    // 需要对当前查询重新赋值操作，可以拿到 StatementHandler -> ParameterHandler -> ParameterObject
    // 通过mybatis提供的 SystemMetaObject 从对象中获取元信息
    MetaObject metaObject = SystemMetaObject.forObject(target);
    Object value = metaObject.getValue("parameterHandler.parameterObject");
    System.out.println("sql语句的参数是：" + value);
    metaObject.setValue("parameterHandler.parameterObject",2);

    Object proceed = invocation.proceed();
    return proceed;
}
```

## 5、常用分页插件

PageHelper

## 6、MyBatis Generator

- [MyBatis Mapper](https://mapper.mybatis.io/)
- [mybatis-generator-gui](https://github.com/zouzg/mybatis-generator-gui)

# 七、Mybatis的设计模式

- [Mybatis使用设计模式](http://www.crazyant.net/2022.html)
- [设计模式](../../软件工程/软件设计/设计模式.md)

## 1、工厂模式

工厂模式在 MyBatis 中的典型代表是 SqlSessionFactory；SqlSession 是 MyBatis 中的重要 Java 接口，可以通过该接口来执行 SQL 命令、获取映射器示例和管理事务，而 SqlSessionFactory 正是用来产生 SqlSession 对象的，所以它在 MyBatis 中是比较核心的接口之一；

工厂模式应用解析：SqlSessionFactory 是一个接口类，它的子类 DefaultSqlSessionFactory 有一个 openSession(ExecutorType execType) 的方法，其中使用了工厂模式，源码如下：
```java
private SqlSession openSessionFromDataSource(ExecutorType execType, TransactionIsolationLevel level, boolean autoCommit) {
    Transaction tx = null;
    try {
        final Environment environment = configuration.getEnvironment();
        final TransactionFactory transactionFactory = getTransactionFactoryFromEnvironment(environment);
        tx = transactionFactory.newTransaction(environment.getDataSource(), level, autoCommit);
        // configuration.newExecutor(tx, execType) 读取对应的环境配置
        final Executor executor = configuration.newExecutor(tx, execType);
        return new DefaultSqlSession(configuration, executor, autoCommit);
    } catch (Exception e) {
        closeTransaction(tx); // may have fetched a connection so lets call close()
        throw ExceptionFactory.wrapException("Error opening session.  Cause: " + e, e);
    } finally {
        ErrorContext.instance().reset();
    }
}
```
newExecutor() 方法为标准的工厂模式，它会根据传递 ExecutorType 值生成相应的对象然后进行返回
```java
public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
    executorType = executorType == null ? defaultExecutorType : executorType;
    executorType = executorType == null ? ExecutorType.SIMPLE : executorType;
    Executor executor;
    if (ExecutorType.BATCH == executorType) {
        executor = new BatchExecutor(this, transaction);
    } else if (ExecutorType.REUSE == executorType) {
        executor = new ReuseExecutor(this, transaction);
    } else {
        executor = new SimpleExecutor(this, transaction);
    }
    if (cacheEnabled) {
        executor = new CachingExecutor(executor);
    }
    executor = (Executor) interceptorChain.pluginAll(executor);
    return executor;
  }
```

## 2、建造者模式

建造者模式在 MyBatis 中的典型代表是 SqlSessionFactoryBuilder

普通的对象都是通过 new 关键字直接创建的，但是如果创建对象需要的构造参数很多，且不能保证每个参数都是正确的或者不能一次性得到构建所需的所有参数，那么就需要将构建逻辑从对象本身抽离出来，让对象只关注功能，把构建交给构建类，这样可以简化对象的构建，也可以达到分步构建对象的目的，而 SqlSessionFactoryBuilder 的构建过程正是如此。

在 SqlSessionFactoryBuilder 中构建 SqlSessionFactory 对象的过程是这样的，首先需要通过 XMLConfigBuilder 对象读取并解析 XML 的配置文件，然后再将读取到的配置信息存入到 Configuration 类中，然后再通过 build 方法生成我们需要的 DefaultSqlSessionFactory 对象，实现源码如下（在 SqlSessionFactoryBuilder 类中）：
```java
public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
    try {
        XMLConfigBuilder parser = new XMLConfigBuilder(inputStream, environment, properties);
        return build(parser.parse());
    } catch (Exception e) {
        throw ExceptionFactory.wrapException("Error building SqlSession.", e);
    } finally {
        ErrorContext.instance().reset();
        try {
            inputStream.close();
        } catch (IOException e) {
            // Intentionally ignore. Prefer previous error.
        }
    }
}
```
SqlSessionFactoryBuilder 类相当于一个建造工厂，先读取文件或者配置信息、再解析配置、然后通过反射生成对象，最后再把结果存入缓存，这样就一步步构建造出一个 SqlSessionFactory 对象

## 3、单例模式

在Mybatis中有两个地方用到单例模式， ErrorContext 和 LogFactory，其中ErrorContext是用在每个线程范围内的单例，用于记录该线程的执行环境错误信息，而LogFactory则是提供给整个Mybatis使用的日志工厂，用于获得针对项目配置好的日志对象
```java
public class ErrorContext {
	private static final ThreadLocal<ErrorContext> LOCAL = ThreadLocal.withInitial(ErrorContext::new);
	private ErrorContext() {
	}
	public static ErrorContext instance() {
		ErrorContext context = LOCAL.get();
		if (context == null) {
			context = new ErrorContext();
			LOCAL.set(context);
		}
		return context;
	}
}
```

## 4、适配器模式

而这个转换头就相当于程序中的适配器模式，适配器模式在 MyBatis 中的典型代表是 Log。

MyBatis 中的日志模块适配了以下多种日志类型：
- SLF4J
- Apache Commons Logging
- Log4j 2
- Log4j
- JDK logging

## 5、代理模式

代理模式在 MyBatis 中的典型代表是 MapperProxyFactory， MapperProxyFactory 的 newInstance() 方法就是生成一个具体的代理来实现功能的，源码如下：
```java
// 在这里，先通过T newInstance(SqlSession sqlSession)方法会得到一个MapperProxy对象，然后调用T newInstance(MapperProxy<T> mapperProxy)生成代理对象然后返回。
public class MapperProxyFactory<T> {
    private final Class<T> mapperInterface;
    private final Map<Method, MapperMethodInvoker> methodCache = new ConcurrentHashMap<>();
    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }
    public Class<T> getMapperInterface() {
        return mapperInterface;
    }
    public Map<Method, MapperMethodInvoker> getMethodCache() {
        return methodCache;
    }
    @SuppressWarnings("unchecked")
    protected T newInstance(MapperProxy<T> mapperProxy) {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, mapperProxy);
    }
    public T newInstance(SqlSession sqlSession) {
        final MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, mapperInterface, methodCache);
        return newInstance(mapperProxy);
    }
}
// 而查看MapperProxy的代码，可以看到如下内容：
public class MapperProxy<T> implements InvocationHandler, Serializable {
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		try {
			if (Object.class.equals(method.getDeclaringClass())) {
				return method.invoke(this, args);
			} else if (isDefaultMethod(method)) {
				return invokeDefaultMethod(proxy, method, args);
			}
		} catch (Throwable t) {
			throw ExceptionUtil.unwrapThrowable(t);
		}
		final MapperMethod mapperMethod = cachedMapperMethod(method);
		return mapperMethod.execute(sqlSession, args);
	}
}
```
通过这种方式，我们只需要编写`Mapper.java`接口类，当真正执行一个Mapper接口的时候，就会转发给`MapperProxy.invoke`方法，而该方法则会调用后续的`sqlSession.cud>executor.execute>prepareStatement`等一系列方法，完成SQL的执行和返回；

## 6、模板方法模式

模板方法在 MyBatis 中的典型代表是 BaseExecutor。在 MyBatis 中 BaseExecutor 实现了大部分 SQL 执行的逻辑，然后再把几个方法交给子类来实现，它的继承关系如下图所示：

![](image/Mybatis-BaseExecutor-UML图.png)

- `简单 SimpleExecutor`：每执行一次update或select，就开启一个Statement对象，用完立刻关闭Statement对象。（可以是Statement或PrepareStatement对象）
- `重用 ReuseExecutor`：执行update或select，以sql作为key查找Statement对象，存在就使用，不存在就创建，用完后，不关闭Statement对象，而是放置于`Map<String, Statement>`内，供下一次使用。（可以是Statement或PrepareStatement对象）
- `批量 BatchExecutor`：执行update（没有select，JDBC批处理不支持select），将所有sql都添加到批处理中（addBatch()），等待统一执行（executeBatch()），它缓存了多个Statement对象，每个Statement对象都是addBatch()完毕后，等待逐一执行executeBatch()批处理的；BatchExecutor相当于维护了多个桶，每个桶里都装了很多属于自己的SQL，就像苹果蓝里装了很多苹果，番茄蓝里装了很多番茄，最后，再统一倒进仓库。（可以是Statement或PrepareStatement对象）

```java
// 比如 doUpdate() 就是交给子类自己去实现的，它在 BaseExecutor 中的定义如下：
protected abstract int doUpdate(MappedStatement ms, Object parameter) throws SQLException;
// SimpleExecutor 实现的 doUpdate 方法
public class SimpleExecutor extends BaseExecutor {
    @Override
    public int doUpdate(MappedStatement ms, Object parameter) throws SQLException {
        Statement stmt = null;
        try {
        Configuration configuration = ms.getConfiguration();
        StatementHandler handler = configuration.newStatementHandler(this, ms, parameter, RowBounds.DEFAULT, null, null);
        stmt = prepareStatement(handler, ms.getStatementLog());
        return handler.update(stmt);
        } finally {
        closeStatement(stmt);
        }
    }
    ...
}
```

## 7、装饰器模式

装饰器模式在 MyBatis 中的典型代表是 Cache，Cache 除了有数据存储和缓存的基本功能外（由 PerpetualCache 永久缓存实现），还有其他附加的 Cache 类，比如先进先出的 FifoCache、最近最少使用的 LruCache、防止多线程并发访问的 SynchronizedCache 等众多附加功能的缓存类，用于装饰PerpetualCache的标准装饰器共有8个（全部在org.apache.ibatis.cache.decorators包中）：
- FifoCache：先进先出算法，缓存回收策略
- LoggingCache：输出缓存命中的日志信息
- LruCache：最近最少使用算法，缓存回收策略
- ScheduledCache：调度缓存，负责定时清空缓存
- SerializedCache：缓存序列化和反序列化存储
- SoftCache：基于软引用实现的缓存管理策略
- SynchronizedCache：同步的缓存装饰器，用于防止多线程并发访问
- WeakCache：基于弱引用实现的缓存管理策略

Cache对象之间的引用顺序为：`SynchronizedCache –> LoggingCache –> SerializedCache –> ScheduledCache –> LruCache –> PerpetualCache`

## 8、解释器模式

SqlNode：利用解释器模式来解析动态 SQL；

动态 SQL 的语法规则是 MyBatis 自定义的。如果想要根据语法规则，替换掉动态 SQL 中的动态元素，生成真正可以执行的 SQL 语句，MyBatis 还需要实现对应的解释器。这一部分功能就可以看做是解释器模式的应用

## 9、迭代器模式

PropertyTokenizer：利用迭代器模式实现一个属性解析器；Mybatis 的 PropertyTokenizer 类实现了 Java Iterator 接口，是一个迭代器，用来对配置属性进行解析

# 八、Mybatis扩展

## 1、Tkmapper

## 2、mybatis-plus

- [Mybatis-Plus](https://baomidou.com/)

# 九、实践

## 1、打印SQL

```properties
logging.level.xxx.xxx.mapper = debug
```
上面的xxxx换成自己的mapper包的路径

## 2、表达式错误

在mybatis中的`OgnlOps.equal(0,"")`返回的是true

有如下Mapper：
```java
List<Order> selectList(@Param("orderStatus") Integer orderStatus);
```
mybatis对应的xml为：
```xml
<select id="selectList" resultType="io.model.Order">
    select * from tb_order
    where 1= 1
    <if test="orderStatus != null and orderStatus != '' ">
        and order_status = #{orderStatus}
    </if>
</select>
```
请注意：如果这时候 selectList 的参数 orderStatus = 0 的话，则实际出来的SQL：
```sql
select * from tb_open_api_user where 1= 1
```
也就是说表达式没有生效；

日志打印在 org.apache.ibatis.logging.jdbc.BaseJdbcLogger,debug方法中打印了日志：
```java
protected void debug(String text, boolean input) {
    if (statementLog.isDebugEnabled()) {
        statementLog.debug(prefix(input) + text);
    }
}
```
sql拼接处理：
```java
// org.apache.ibatis.executor.CachingExecutor#query()
public <E> List<E> query(MappedStatement ms, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
    BoundSql boundSql = ms.getBoundSql(parameterObject);
    CacheKey key = createCacheKey(ms, parameterObject, rowBounds, boundSql);
    return query(ms, parameterObject, rowBounds, resultHandler, key, boundSql);
}
```
关键源码：
```java
// org.apache.ibatis.ognl.ASTNotEq#getValueBody
protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
    Object v1 = this._children[0].getValue(context, source);
    Object v2 = this._children[1].getValue(context, source);
    return OgnlOps.equal(v1, v2) ? Boolean.FALSE : Boolean.TRUE;
}
```
最终调用到：
```java
// org.apache.ibatis.ognl.OgnlOps#compareWithConversion
double dv1 = doubleValue(v1);
double dv2 = doubleValue(v2);
return dv1 == dv2 ? 0 : (dv1 < dv2 ? -1 : 1);
// org.apache.ibatis.ognl.OgnlOps#doubleValue
public static double doubleValue(Object value) throws NumberFormatException {
    if (value == null) {
        return 0.0;
    } else {
        Class c = value.getClass();
        if (c.getSuperclass() == Number.class) {
            return ((Number)value).doubleValue();
        } else if (c == Boolean.class) {
            return (Boolean)value ? 1.0 : 0.0;
        } else if (c == Character.class) {
            return (double)(Character)value;
        } else {
            String s = stringValue(value, true); // 最终会走到这里
            return s.length() == 0 ? 0.0 : Double.parseDouble(s);
        }
    }
}
```

总结：如果在处理动态SQL时，除了字符串或者字符外，其余使用判断条件只需要判断是否为 null 即可，不需要判断 `!= ''`


# 参考资料

- [Hibernate与Mybatis对比](http://www.cnblogs.com/inspurhaitian/p/4647485.html)
- [Mybatis使用教程](https://blog.csdn.net/hellozpc/article/details/80878563)
