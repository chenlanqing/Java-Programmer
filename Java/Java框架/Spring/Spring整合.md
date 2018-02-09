<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一.Spring整合Hibernate:](#%E4%B8%80spring%E6%95%B4%E5%90%88hibernate)
- [二.Spring 整合 Struts2:](#%E4%BA%8Cspring-%E6%95%B4%E5%90%88-struts2)
- [三.Spring整合Hibernate和Struts2](#%E4%B8%89spring%E6%95%B4%E5%90%88hibernate%E5%92%8Cstruts2)
- [四.Spring整合Mybatis](#%E5%9B%9Bspring%E6%95%B4%E5%90%88mybatis)
- [五.Spring与SpringMVC](#%E4%BA%94spring%E4%B8%8Espringmvc)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 一.Spring整合Hibernate:
#### 1.Spring整合Hibernate的优点:
	(1).使用Spring的 IOC 容器来管理 Hibernate 的 SessionFactory;
	(2).使得 Hibernate 能够使用 Spring 的声明式事务.
	
#### 2.整合步骤:
	(1).编写hibernate.cfg.xml配置文件:一般在该配置文件中配置 hibernate 的基本属性,如:方言,sql显示及格式化等
		<?xml version="1.0" encoding="UTF-8"?>
		<!DOCTYPE hibernate-configuration PUBLIC
				"-//Hibernate/Hibernate Configuration DTD 3.0//EN"
				"http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">
		<hibernate-configuration>
			<session-factory>			
				<!-- 配置 hibernate 的基本属性 -->
				<!-- 1. 数据源需配置到 IOC 容器中, 所以在此处不再需要配置数据源 -->
				<!-- 2. 关联的 .hbm.xml 也在 IOC 容器配置 SessionFactory 实例时在进行配置 -->
				<!-- 3. 配置 hibernate 的基本属性: 方言, SQL 显示及格式化, 生成数据表的策略以及二级缓存等. -->
				<property name="hibernate.dialect">org.hibernate.dialect.MySQL5InnoDBDialect</property>
				<property name="hibernate.show_sql">true</property>
				<property name="hibernate.format_sql">true</property>				
				<property name="hibernate.hbm2ddl.auto">update</property>				
				<!-- 配置 hibernate 二级缓存相关的属性. -->						
			</session-factory>
		</hibernate-configuration>
	
	(2).编写相关实体类,及相应实体类的 *.hbm.xml, 例如:
		<?xml version="1.0"?>
		<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
		"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">
		<!-- Generated 2015-6-20 14:09:51 by Hibernate Tools 3.4.0.CR1 -->
		<hibernate-mapping>
			<class name="com.spring.hibernate.entity.Account" table="ACCOUNT">
				<id name="id" type="int">
					<column name="ID" />
					<generator class="native" />
				</id>
				<property name="username" type="java.lang.String">
					<column name="USERNAME" />
				</property>
				<property name="balance" type="double">
					<column name="BALANCE" />
				</property>
			</class>
		</hibernate-mapping>
	
	(3).Spring配置文件,整合hibernate配置:applicationContext.xml, 分三点:
		(3.1).配置数据源信息;
		(3.2).配置 Hibernate 的 SessionFactory 实例: 通过 Spring 提供的 LocalSessionFactoryBean 进行配置:
			需配置三个属性:数据源属性,hibernate配置文件(可以通过配置 hibernateProperties属性加载hibernate的配置文件),映射文件
		(3.3).配置 Spring 的声明式事务		
		<!-- 配置自动扫描的包 -->
		<context:component-scan base-package="com.atguigu.spring.hibernate"></context:component-scan>
		
		<!-- 配置数据源 -->
		<!-- 导入资源文件 -->
		<context:property-placeholder location="classpath:db.properties"/>		
		<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
			<property name="user" value="${jdbc.user}"></property>
			<property name="password" value="${jdbc.password}"></property>
			<property name="driverClass" value="${jdbc.driverClass}"></property>
			<property name="jdbcUrl" value="${jdbc.jdbcUrl}"></property>
			<property name="initialPoolSize" value="${jdbc.initPoolSize}"></property>
			<property name="maxPoolSize" value="${jdbc.maxPoolSize}"></property>
		</bean>		
		<!-- 配置 Hibernate 的 SessionFactory 实例: 通过 Spring 提供的 LocalSessionFactoryBean 进行配置 -->
		<bean id="sessionFactory" class="org.springframework.orm.hibernate4.LocalSessionFactoryBean">
			<!-- 配置数据源属性 -->
			<property name="dataSource" ref="dataSource"></property>
			<!-- 配置 hibernate 配置文件的位置及名称 -->
			<!--  
			<property name="configLocation" value="classpath:hibernate.cfg.xml"></property>
			-->
			<!-- 使用 hibernateProperties 属相来配置 Hibernate 原生的属性 -->
			<property name="hibernateProperties">
				<props>
					<prop key="hibernate.dialect">org.hibernate.dialect.MySQL5InnoDBDialect</prop>
					<prop key="hibernate.show_sql">true</prop>
					<prop key="hibernate.format_sql">true</prop>
					<prop key="hibernate.hbm2ddl.auto">update</prop>
				</props>
			</property>
			<!-- 配置 hibernate 映射文件的位置及名称, 可以使用通配符 -->
			<property name="mappingLocations" 
				value="classpath:com/atguigu/spring/hibernate/entities/*.hbm.xml"></property>
		</bean>
		*/
		<!-- 配置 Spring 的声明式事务 -->
		<!-- ①. 配置事务管理器 -->
		<bean id="transactionManager" class="org.springframework.orm.hibernate4.HibernateTransactionManager">
			<property name="sessionFactory" ref="sessionFactory"></property>
		</bean>

		<!-- ②. 配置事务属性, 需要事务管理器 -->
		<tx:advice id="txAdvice" transaction-manager="transactionManager">
			<tx:attributes>
				<tx:method name="get*" read-only="true"/>
				<tx:method name="purchase" propagation="REQUIRES_NEW"/>
				<tx:method name="*"/>
			</tx:attributes>
		</tx:advice>
		<!-- ③. 配置事务切点, 并把切点和事务属性关联起来 -->
		<aop:config>
			<aop:pointcut expression="execution(* com.atguigu.spring.hibernate.service.*.*(..))" 
				id="txPointcut"/>
			<aop:advisor advice-ref="txAdvice" pointcut-ref="txPointcut"/>
		</aop:config>
	
#### 3.Spring 整合 Hibernate 需要注意的几点:
	3.1.Dao如何与数据库交互:
		一般在Dao中注入:SessionFactory, 而不不推荐使用 HibernateTemplate 和 HibernateDaoSupport:
		因为这两个类为 Spring API中的,这样就会导致 Dao 和 Spring的API进行耦合,可移植性变差;
	//	SessionFactory.getCurrentSession();
	
	3.2.Spring hibernate 的事务流程:
		/**
		 * Spring hibernate 事务的流程
		 * 1. 在方法开始之前
		 * ①. 获取 Session
		 * ②. 把 Session 和当前线程绑定, 这样就可以在 Dao 中使用 SessionFactory 的getCurrentSession() 方法来获取 Session 了
		 * ③. 开启事务
		 * 
		 * 2. 若方法正常结束, 即没有出现异常, 则
		 * ①. 提交事务
		 * ②. 解除与当前线程绑定的Session
		 * ③. 关闭 Session
		 * 
		 * 3. 若方法出现异常, 则:
		 * ①. 回滚事务
		 * ②. 解除与当前线程绑定的Session
		 * ③. 关闭 Session
		 */
	
	3.3.hibernate.cfg.xml配置文件在与Spring整合时,可以省略,只需要在配置sessionFactory中加上如下配置:
			<property name="hibernateProperties">
				<props>
					<prop key="hibernate.dialect">org.hibernate.dialect.MySQL5InnoDBDialect</prop>
					<prop key="hibernate.show_sql">true</prop>
					<prop key="hibernate.format_sql">true</prop>
					<prop key="hibernate.hbm2ddl.auto">update</prop>
				</props>
			</property>
		
	
# 二.Spring 整合 Struts2:
#### 1.Spring 整合web应用时如何创建IOC容器:应该在web应用在服务器加载时就创建IOC容器,
	这里可以使用监听器,实现ServletContextListener类:
		public class SpringServletContextListener implements ServletContextListener {
			public void contextInitialized(ServletContextEvent sce) {
				ServletContext servletContext = sce.getServletContext();
				String config = servletContext.getInitParameter("configLocation");    	
				ApplicationContext ctx = new ClassPathXmlApplicationContext(config);    	
				servletContext.setAttribute("applicationContext", ctx);
			}
			public void contextDestroyed(ServletContextEvent sce) {
				// TODO Auto-generated method stub
			}	
		}
	(1).如何使用IOC容器:
		在加载时,将IOC容器放入 ServletContext 中的一个属性中
	(2).Spring的配置文件可以通过web.xml来设置,来实现配置文件可配置化
		  <context-param>
			<param-name>configLocation</param-name>
			<param-value>applicationContext.xml</param-value>
		  </context-param>
			
#### 2.在web环境下如何使用 Spring:
	(1).直接在web应用上的web.xml配置如下配置:
		<!-- 配置 Spring 配置文件的名称和位置 -->
		<context-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>classpath:applicationContext.xml</param-value>
		</context-param>
		
		<!-- 启动 IOC 容器的 ServletContextListener -->
		<listener>
			<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
		</listener>
	
	(2).获取IOC 容器:
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
	
#### 3.Spring整合Struts2:
	3.1.整合目的:
		使用IOC容器管理Struts的Action
	3.2.整合方法:
		(1).导入Struts 和 Spring 相关jar包
		(2).将Struts2加入web.xml配置:
			<filter>
				<filter-name>struts2</filter-name>
				<filter-class>org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter</filter-class>
			</filter>

			<filter-mapping>
				<filter-name>struts2</filter-name>
				<url-pattern>/*</url-pattern>*/
			</filter-mapping>
		(3).添加struts配置文件struts.xml文件;
		(4).在Spring IOC容器中配置Struts2的Action:
		// 注意:在Spring ICO容器中配置Struts的Action时,需要配置scope属性,其值不能为单例的,可以配置为:prototype
			<bean id="personAction" class="com.atguigu.spring.struts2.actions.PersonAction"
				scope="prototype">
				<property name="personService" ref="personService"></property>	
			</bean>
		(5).配置 Struts2 的配置文件: action 节点的 class 属性需要指向 IOC 容器中该 bean 的 id
			<action name="person-save" class="personAction">
				<result>/success.jsp</result>
			</action>
		(6).需要加入 struts2-spring-plugin-2.3.15.3.jar
	
	3.3.整合原理:通过添加 struts2-spring-plugin-2.3.15.3.jar 以后, Struts2 会先从 IOC 容器中获取 Action 的实例
		SpringObjectFactory的
		public Object buildBean(String beanName, Map<String, Object> extraContext, boolean injectInternal) throws Exception {
			Object o;			
			if (appContext.containsBean(beanName)) {
				o = appContext.getBean(beanName);
			} else {
				Class beanClazz = getClassInstance(beanName);
				o = buildBean(beanClazz, extraContext);
			}
			if (injectInternal) {
				injectInternalBeans(o);
			}
			return o;
		}


# 三.Spring整合Hibernate和Struts2

# 四.Spring整合Mybatis

# 五.Spring与SpringMVC
	
