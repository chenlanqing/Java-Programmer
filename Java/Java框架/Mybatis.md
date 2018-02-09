<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一.使用Mybatis一般过程:](#%E4%B8%80%E4%BD%BF%E7%94%A8mybatis%E4%B8%80%E8%88%AC%E8%BF%87%E7%A8%8B)
- [二.注意:](#%E4%BA%8C%E6%B3%A8%E6%84%8F)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 一.使用Mybatis一般过程:
    1.创建基本配置文件mybatis-config.xml,该配置文件存放数据库连接信息以及引入mapper映射文件和缓存设置等待;
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

    2.创建实体类及其对应的Mapper映射文件:如userMapper.xml
        <?xml version="1.0" encoding="UTF-8"?>
        <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
        <!-- 命名空间,一般跟Mapper映射接口一致 -->
        <mapper namespace="com.mybatis.service.IUserService">
            <select id="queryUserById" parameterType="int" resultType="User">
                select * from user where id=#{id}
            </select>
            ...
        </mapper>
        
    3.将Mapper映射文件引入到基本配置文件:
                <mappers>
                    <mapper resource="com/mybatis/mapper/userMapper.xml"/>
                    <mapper resource="com/mybatis/mapper/cuserMapper.xml"/>
                </mappers>

    4.获取sqlSessionFactory和sqlSession:
            String resource = "configuration.xml";
            InputStream in = UserTestDemo.class.getClassLoader().getResourceAsStream(resource);
            SqlSessionFactory ssf = new SqlSessionFactoryBuilder().build(in);
            SqlSession session = ssf.openSession();
    5.根据相应的增删改查,调用sqlSession的方法

# 二.注意:
    1.mybatis完成两件事：
        (1).根据JDBC规范建立与数据库的连接;
        (2).通过Annotation/XML + Java反射技术,实现Java对象与关系数据库之间的相互转化;
    2.typeAliases属性:类型别名是为 Java 类型命名一个短的名字。 它只和 XML 配置有关, 只用来减少类完全 限定名的多余部分
        <typeAliases>
            <typeAlias type="com.neusoft.pojo.User" alias="User"/>
        </typeAliases>
        或者
        <typeAliases>
            <package name="com.neusoft.pojo"/>
        </typeAliases>
        在实体类中通过注解形式
        @Alias("User")
        public class User {
        }
        
    3.字段名与实体类属性名不相同的冲突
        在Mapper文件中配置,如下所示:select元素中resultMap的值需跟定义的resultMap元素中id属性值一样
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
        
        如果是通过接口形式来实现的，那么在mapper的配置文件中namespace应该是接口的路径名
        
    4.关联表查询	
        4.1.一对一查询:
            association:用于一对一的关联查询的;
            property:对象属性的名称;
            javatype:对象属性的类型
            column:所对应的外键字段名称;
            select:使用另一个查询封装的结果
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
        
        4.2.一对多查询：
            collection:做一对多关联查询的集合;
            ofType:指定集合中的类型
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

        
    5.调用存储过程：
        parameterMap:引用即传入值与输出值
        statementType:知道statement的真实类型;CALLABLE执行调用存储过程的语句
        mode:表示输出或输入
        <select id="getCount" statementType="CALLABLE" parameterMap="getCountMap">
            call mybatis.get_user_count(?,?)
        </select>
        <parameterMap type="java.util.Map" id="getCountMap">
            <parameter property="sex_id" mode="IN" jdbcType="INTEGER"/>
            <parameter property="user_count" mode="OUT" jdbcType="INTEGER"/>
        </parameterMap>


	