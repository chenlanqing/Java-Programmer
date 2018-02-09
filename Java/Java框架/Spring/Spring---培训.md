<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [二.Spring整合应用](#%E4%BA%8Cspring%E6%95%B4%E5%90%88%E5%BA%94%E7%94%A8)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

#### 1.什么是Spring,作用
	Spring是一个容器框架,可以配置各种bean,并且可以维护bean与bean之间的关系;如果需要使用某
		个bean 的时候,可以通过getBean(id)来获取;
	(1).Spring框架属于整合框架,可以将Struts,Hibernate等技术整合在一起使用;
	(2).作用:降低组件之间耦合度,有利于程序的扩展和升级;
	Spring框架主要具有以下几点功能:
		---Spring框架提供了与其他技术整合的API,整合API可以引用原Struts,Hibernate功能;
		---Spring框架提供了一个容器,该容器可以创建和管理程序中的组件对象(Spring核心容器);
		---Spring框架提供了IOC特性,可以降低两个组件对象的耦合度;
		---Spring框架容器提供了AOP特性,可以将事务等共通功能以低耦合方式作用到原功能组件上;

#### 2.Spring基本使用
	(1).Spring容器的核心使用:可以创建和管理组件对象
		使用步骤
			---引入Spring核心开发包;
			---添加Spring的配置文件;
			---将程序组件定义到Spring配置文件中;
				//定义一个组件costDAO
				<bean id="costDao" scope="singleton" class="com.tarena.dao.JdbcCostDAO"></bean>
			---获取Spring容器对象,从容器中获取Bean组件对象;
				//实例化Spring容器
				ApplicationContext ac = new ClassPathXmlApplicationContext(conf);
			//执行过程:
				● new ClassPathXmlApplicationContext(“applicationContext.xml”),执行这句话时Spring容器被创建,
				同时applicationContext.xml配置中对应的bean会被创建置于内存中,以类似HashMap存在;
				● 
	(2).Spring容器对Bean的管理:
		A:控制Bean对象创建模式:
			在<bean>元素中,利用scope属性可以指定Bean组件创建对象的方式:
				---prototype:非单例模式
				---singleton:单例模式(默认是单例模式)
			//在web程序中,通过一些配置,可以扩展出request,session等属性值;
		B:可以控制单例模式的创建时机:
			---singleton模式的Bean组件,默认是在ApplicationContext容器实例化时就创建了组件;
				可以在<bean>元素中追加属性lazy-init="true",将singleton模式创建对象推迟到getBean()方法
			---prototype模式是在调用getBean()方法时创建了组件;
			
		C:可以指定Bean对象初始化和销毁方法:
			<bean init-method="初始化方法" destroy-method="销毁方法">
			---Spring将Bean对象创建完毕后,会自动调用init-method里指定的方法
			---destroy-method指定的方法需满足下面条件才能执行:
				----scope="singleton";才能使用;
				----执行AbstractApplicationContext容器的close()方法触发;

		BeanFactory -->ApplicationContext

	(3)Spring框架的IoC特性--------------------------IOC机制实现Bean之间的调用;
		IoC解决的问题:可以降低两个组件对象之间的关联,降低耦合度;
		(3.1).IoC:Inverse of Controller,控制反转(控制转移);
			控制:指的是组件对象创建,初始化,销毁以及关系指定等逻辑;
			将上述控制逻辑由第三方容器或框架(Spring)负责,从而被称为IoC,
			当遇到组件的替换或功能扩展,只需要修改Spring配置,相关联的组件不需要修改(即低耦合度);
			//控制反转:把创建对象(Bean)和维护对象(Bean)的关系的权利从程序中转移到Spring容器中,程序不再控制;
		(3.2).DI(Dependency Injection)依赖注入:是Spring实现IoC的技术途径;
			
			依赖注入的方式:
				◆setter方式注入:(推荐使用)
						使用步骤:
						---在Action中定义dao接口变量及其set方法;
						---在Spring配置Action组件的<bean>元素,使用下面格式:
							<property name="属性名" ref="要注入的Bean对象的id属性值"></property>
				◆构造方式注入:
						使用步骤:
						---在Action中定义dao接口变量以及带参数的构造方法(参数为dao接口类型的变量);
						---在Spring配置Action组件的<bean>元素,使用下面格式:
							<constructor-arg index="指定参数索引(从0开始)" ref="要注入的Bean对象的id属性值">
							</constructor-arg>
						
#### 3.各种类型信息的注入:
	(1).注入Bean对象:(使用最多)--------------------------------------------------★★★★★
			<property name="属性名" ref="要注入的Bean对象的id属性值"></property>

	(2).注入基本类型
		A:注入数值或字符串类型
			<property name="属性名" value="属性值" ></property>
			<!-- 以set方法绝对name值 ,注入的类型有set方法参数类型决定-->
			<property name="includeTypes" value="jpg,jpeg,gif"></property>
			
	(3).注入集合类型:(以下property都是写在<bean>元素内的)
		◆注入List集合配置方法:
				<!-- 注入List集合 -->
				<property name="属性名">
					<list>
						<!-- 集合的泛型是对象 -->
						<bean></bean>
						<!-- 集合的泛型是String字符串 -->
						<value>属性值</value>
						<value>上海</value>
						<value>杭州</value>
					</list>
				</property>
				
		◆注入Set集合配置方法:
					<!-- 注入Set集合 -->
					<property name="属性名">
						<set>
							<value>Tom</value>
							<value>Sam</value>
							<value>Coco</value>
						</set>
					</property>

		◆注入Map类型配置方法:
					<!-- 注入Map类型数据 -->
					<property name="books">
						<map>
							<entry key="1001" value="Core Jave"></entry>
							<entry key="1002" value="Java Web"></entry>
							<entry key="1003" value="SSH"></entry>
						</map>
					</property>
					
		◆注入Properties类型配置方法:
					<!-- 注Properties类型数据 -->
					<property name="prop">
						<props>
							<prop key="show_sql">true</prop>
							<prop key="dialect">org.hibernate.dialect.OracleDialect</prop>
						</props>
					</property>


#### 4.什么是AOP，作用----------------------------AOP机制实现共通Bean与目标Bean之间的调用;
	4.1.AOP
		Aspect Oriented Programming:面向方面编程;
		(1).AOP是以OOP(面向对象编程)为基础,用于改善程序组件对象之间的结构,降低耦合度;
		(2).AOP是以方面组件为关注点,方面组件是用于封装共通处理逻辑的,将来可以被其他的目标组件调用;
	
	4.2.AOP作用:
		解决共通处理与目标组件之间解耦	
				

#### 5.AOP的基本使用
	---引入IOC和AOP开发包;
	---找出共通处理,写成一个Bean组件;
	---在Spring配置中定义AOP
		将共通处理组件定义成一个<bean>
		定义<aop:pointcut>,指定哪些组件为目标;
		定义<aop:aspect>,将组件定义策划那个方面;
		定义<aop:before>,指定目标和方面作用的时机:
				<bean id="checkRoleBean" class="aop.CheckRoleBean"></bean>
				<!-- 利用Spring的AOP机制将CheckRoleBean作用到各个Action的execute方法 -->
				<aop:config >
					<!-- 指定方面组件作用到哪些目标组件上 -->
					<aop:pointcut id="actionPoint" expression="within(action.DeleteCostAction)" />
					<!-- 指定哪个组件为方面组件 -->
					<aop:aspect id="checkRoleAspect" ref="checkRoleBean">
						<!-- 
							指定checkRoleBEan组件处理和Action组件方法作用的时机,
							before表示在处理前调用,after表示在处理之后调用 
						-->
						<aop:before pointcut-ref="actionPoint" method="check"/>			
					</aop:aspect>
				</aop:config>


#### 6.AOP相关概念:
	6.1.方面(Aspect):--------------------------------------------------------------★★★★★
		方面封装了共通处理,可以切入到其他目标组件方法上;
		
	6.2.切入点(Pointcut):---------------------------------------------------------★★★★★
		用于指定哪些组件为目标,利用特殊表达式指定,within(类型);
		
	6.3.通知(Advice):--------------------------------------------------------------★★★★★
		用于指定方面和目标之间的作用时机
		◆通知类型:
			---前置通知:先执行方面,再执行目标;<aop:before>
			---后置通知:先执行目标,再执行方面(目标方法未抛出异常);<aop:after-Returning>
			---最终通知:先执行目标,再执行方面(不管目标有无异常);<aop:after>
			---环绕通知:先执行方面的前半部分,再执行目标,最后执行方面的后半部分;<aop:around>
			---异常通知:先执行目标,发生异常后再执行方面;<aop:after-Throwing>
				try{
					//执行方面---前置通知------<aop:before>
					/**执行方面---环绕通知------<aop:around>*/
					执行目标组件处理
					/**执行方面---环绕通知------<aop:around>*/
					//执行方面---后置通知-------<aop:afterReturning>
				}catch{
					//执行方面---异常通知-------<aop:afterThrowing>
				}finally{
					//执行方面---最终通知-------<aop:after>
				}
	
	6.4.连接点(JoinPoint):
		切入点是连接点的集合,指定是方面和某一个目标的某个方法的作用点;
		利用该对象可以获取方面和目标方法作用的信息;
		例如:目标方法名,目标组件类型等;

	6.5.目标组件(Target)
		切入点指定的目标组件,将来调用方面的组件;

	6.6.动态代理技术(AutoProxy)
		(1).当目标组件使用了AOP切入方面组件后,getBean()方法返回的类型是采用动态代理技术生成的一个
			新类型,当使用代理对象执行业务方法时,代理对象会调用方面和原目标对象的处理方法
		(2).Spring框架提供了两种代理技术的实现:
			---CGLIB技术:
				适用于没有接口的目标组件;
						//利用父类变量接收代理类对象
						public class 代理类型 extends 目标类型{
							//重写Action的execute方法
							public String execute(){
								//调用方面组件
								//调用目标组件super.execute();
							}
						}
						AddCostAction action = (AddCostAction)ac.geBean("jdbcCostDao");
						action.execute();
			---JDK Proxy API
				适用于有接口的目标组件;
						public class 代理类型 implement 目标接口{
						
						}
						//利用接口变量接收代理类对象
						ICostDao dao = (ICostDao)ac.getBean("jdbcCostDao");
						dao.save();//调用代理类型的save()方法


#### 7.切入点的使用
	7.1.切入点表达式可以指定目标组件及其方法;
	表达式的写法:
		7.1.1.方法限定表达式:
			可以指定哪些方法当作目标启用方面功能;
				execution(修饰符? 返回类型 方法名(参数列表) throws 异常类型?)
				//问号表示:修饰符 或 异常可以不写,必须指定返回类型,方法名,方法参数列表
			
			示例1:
				匹配方法名以find开头的Bean对象
				execution(* find*(..))
					---" * " 表示有返回值或者没有返回值, " ..":表示参数是0个以上;" find* ":匹配方法名以find开头的
			
			示例2:
				匹配JdbcCostDao的save方法
				execution(* org.dao.JdbcCostDao.save(..))
			
			示例3:
				匹配org.dao包下所有类的所有方法
				execution(* org.dao.*.*(..))

			示例4:
				匹配org.dao包及其子包下所有类的所有方法
				execution(* org.dao..*.*(..))
			
			示例5:
				必须有返回值
				execution(!void set*(..))
				
		7.1.2.类型限定表达式
			可以指定哪个组件的所有方法都启用方面功能
			within(类型);
			
			示例1:
				AddCostAction中所有方法:
				within(action.AddCostAction);
			
			示例2:
				匹配org.action包下所有类的所有方法
				within(org.action.*);
			
			示例3:
				匹配org.action包下及其子包下所有类的所有方法
				within(org.action..*);

		7.1.3.Bean组件id或name限定表达式
			bean(id或name属性值)
			//注意:
				id和name属性都是用于指定Bean组件的标识符,
				但是id更严格,不允许使用" / "等特殊字符,而name允许;
				
			示例1:
				匹配bean元素id或name属性为jdbcCostDao的组件的所有方法
				bean("jdbcCostDao");

			示例2:
				匹配<bean>元素id或name属性以Action结尾的组件的所有方法
				bean(*Action)

		7.1.4.方法参数限定表达式
			args(参数列表)
			
			示例:
				匹配只有一个参数的类型为String的方法
				args(java.lang.String)

			提示:上述表达式可以使用 ||,&&进行拼接
			如:within(org.action..*) && !execution(...);

#### 8.AOP编程
	步骤:
		(1).找出共通处理,写成方面组件;
		(2).找出目标组件,写成切入点表达式;
		(3).找出通知类型,使用相应的通知类型切入;
	
	案例1:
		记录用户执行操作的信息
		分析:
			(1).方面组件:可以根据用户操作记录日志信息;
			(2).目标组件(Action,一个Action昂分代表一个请求处理):
					写成切入点within;
			(3).方面组件功能在目标方法之后执行,由于需要使用连接点获取类名和方法,
					因此使用环绕(可以传入连接点信息)
					public Object xxx(ProceedingJoinPoint pjp)
			
			◆代码:
				(1).方面组件类代码:
							public class OptLoggerBean {
								public Object logger(ProceedingJoinPoint pjp) throws Throwable{
									//proceed():执行目标组件方法,先执行目标组件方法
									Object retval = pjp.proceed();
									/**
									 * 1.获取当前执行的组件类型和方法
									 */
									//getTarget():获取目标组件类型
									String className = pjp.getTarget().getClass().getName();
									//getSignature():获取目标组件的方法
									String methodName = pjp.getSignature().getName();									
									/**
									 * 2.根据组件类和方法判断操作并记录
									 */
									//利用className+methodName当作key读取opt.properties获取操作名的信息		
									String key = className + "." + methodName;		
									String msg = PropertiesUtil.getOptMsg(key);
									Date time = new Date();
									SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									System.out.println("某某在"  + format.format(time) + "执行了" + msg);
									return retval;
								}
							}
							
				(2).配置文件新加配置:
							<bean id="optLoggerBean" class="aop.OptLoggerBean"></bean>
							<!-- 利用Spring的AOP机制将OptLoggerBean作用到各个Action的execute方法 -->
							<aop:config >		
								<aop:pointcut id="actionPoint" expression="within(action..*)" />
								<!-- 指定哪个组件为方面组件 -->
								<aop:aspect id="optLoggerAspect" ref="optLoggerBean">
									<!-- 指定optLoggerBean组件处理和Action组件方法作用的时机 -->
									<aop:around pointcut-ref="actionPoint" method="logger"/>			
								</aop:aspect>
							</aop:config>
				
				(3).可以把需要处理的操作写在opt.properties中
					按如下格式://#className.methodName=opt msg
							action.AddCostAction.execute=\u8D44\u8D39\u6DFB\u52A0 (ASCII编码)
							action.DeleteCostAction.execute=\u8D44\u8D39\u5220\u9664

	
	案例2:
		将程序抛出的异常信息记录下来
			(1).方面组件:记录异常信息;
			(2).目标组件:Action为目标组件;
			(3).在Action发生异常之后执行方面组件:异常通知;
				public void xxx(异常类型参数);
			
	
#### 9.Log4j日志工具的使用:
	(1).Log4j日志是Apache组织的,用于信息输出的工具包;
	(2).Log4j中有3种重要组件
		◆Logger日志器:
			提供日志信息输出的方法,将信息分成不同级别,不同级别信息用不同方法输出;
				debug,info,warn,error,fatal(输出级别从低到高)
		◆Appender输出器:
			可以提供不同的输出方式,例如控制台输出,文件输出等;
		◆Layout布局器:
			指定消息输出的格式
	(3).使用步骤:
		---引入log4j.jar包
		---在代码中使用Logger日志器输出消息;
		---在src下添加log4j.properties配置文件,在配置中定义Logger,Appender,Layout
		配置文件:
			log4j.rootLogger=debug,myconsole----debug是输出级别,指定一个输出名称)
			//输出到控制台			
			log4j.appender.myconsole=org.apache.log4j.ConsoleAppender---指定输出的组件,这里指定是是控制台
			log4j.appender.myconsole.layout=org.apache.log4j.SimpleLayout----输出的布局
			
			//输出到文件中
			log4j.appender.myfile=org.apache.log4j.FileAppender
			log4j.appender.myfile.File=D:\\error.log -----指定文件输出存放的位置
			log4j.appender.myfile.layout=org.apache.log4j.SimpleLayout
			
			//输出到文件中,输出布局可以设置为HTMLLayout等;
			log4j.appender.myfile.File=D:\\error.html
			log4j.appender.myfile.layout=org.apache.log4j.HTMLLayout
			
#### 10.Spring中的注解配置	
		◆从JDK5.0开始,提供了注解,泛型,新for循环,自动装箱,拆箱;
		◆目前框架利用注解替代XML配置内容,
		◆注解是一种标记(@标记),可以写在类定义前,方法定义前,属性变量定义前
		(1).组件自动扫描技术:
			可以指定一个包路径,Spring会自动扫描该包及其子包下所有的Class组件,当发现class有指定
			的注解标记,会转化成原XML配置中的<bean>定义;
			使用方法:
				---在Spring的主配置中开启组件自动扫描:
					<context:component-scan base-package="包路径" />
				---在需要扫描进入Spring容器的Class中,在类定义前使用下面注解标记之一:
					@Controller 	: 	Action组件
					@Service		:	业务组件
					@Repository : DAO组件
					@Component :其他组件
					
					◆示例:@Repository("jdbcCostDao")-----引号里内容表示:Bean组件的id
					
				---如果需要注入Bean对象,在属性变量或者set方法前使用下面标记:注入注解标记
					@Resource,默认按照名称注入，找不到对于名称时按类型装配；
						----@Resource:注入注解标记,按类型
						----@Resource(name="指定的Bean的Id"):注入指定的Bean对象
					@Autowired:按类型注入
						---如果使用@Autowired标记需要注入指定的Bean对象需要按照如下格式写:
							@Autowired(required=true):
							@Qualifier("指定的Bean")
				
		(2).AOP注解配置:
			使用方法如下:
				---在Spring的主配置中开启AOP注解配置:
					<aop:aspectj-autoproxy />
				---编写方面组件,在组件中使用下列注解标记:
					@Component:将Bean扫描到Spring容器;
					@Aspect:将Bean指定为方面组件;
					通知标记:
						--@Before : 前置通知
						--@After	:最终通知
						--@AfterReurning 	: 后置通知
						--@AfterThrowing	:异常通知
						--@Around	:环绕通知
			 可以加注解标记:@Scope()-----表示是否为单例
		(3).XML和注解比较:
			---注解方式简单,快捷;
			---xml方式可读性强,便于维护和修改;
			
# 二.Spring整合应用
#### 1.Spring对数据库访问技术的支持
	---提供了整合的API:
		|---DaoSupport组件:编写DAO的积累,提供了一些DAO需要的方法;
				JDBCDaoSupport,HibernateDaoSupport
		|---Template组件:封装了增删改查的操作方法
	---提供了一致的异常处理层次:
			将各种数据库访问技术异常类型统一成了DataAccessException,
			在业务处理时,对该异常进行捕获处理即可
	---提供了声明式事务管方法:
			基于AOP机制,只需要添加配置就可以完成事务管理;
			
#### 2.Spring对JDBC技术整合应用			
	开发步骤:
		---引入开发包和Spring配置
			Spring开发包,数据库驱动,连接池开发包;
			Spring配置文件;				
		---定义实体类和DAO接口:
			根据cost表编写一个Cost类,定义接口
		---根据Dao接口编写JDBC实现类,实现类需继承JdbcDaoSupport,并实现上面定义的接口,
			利用DaoSupport提供的Template完成增删改查:
				◆super.getJdbcTemplate()获取Template对象,使用该对象的update(sql, Object[]参数数组);
				◆queryForObject():查询单行记录;
				◆query():查询多行记录;
				◆queryForInt():查询单行单列;
				◆RowMppaer组件:用于将记录转换成实体对象
		---将DAO组件定义到Spring容器中;
		---在Spring容器中定义个连接池Bean对象,将连接池给DAO注入,为Template提供连接
					<!-- 定义一个连接池 -->
					<bean id="myDataSource" class="org.apache.commons.dbcp.BasicDataSource">
						<property name="url" value="jdbc:oracle:thin:@localhost:1521:xe"></property>
						<property name="driverClassName" value="oracle.jdbc.OracleDriver"></property>
						<property name="username" value="clq"></property>
						<property name="password" value="clq323"></property>
					</bean>
					
					<!-- 将连接池给dao注入,给daosupport的template对象提供连接 -->
					<bean id="jdbcCostDao" class="org.tarena.dao.JdbcCostDAO">
						<!-- 
							将myDataSource对象给daosupport中的setDataSource()方法,
							为getJdbcTemplate()对象提供连接资源
						 -->
						<property name="dataSource" ref="myDataSource"></property>
					</bean>
		---
#### 3.	Spring+JDBC注解整合
  ---在Spring的配置文件中开启组件扫描
  ---在Dao组件中使用扫描注解@Repository
  ---在Dao中定义一个set方法,使用@Resource将DataSource连接池注入,给daosupport传入
  ---在Spring的配置文件中定义dataSource连接池
  ---获取Spring容器的DAO对象，测试			
			
		
#### 4.Spring和Hibernate框架的整合应用
	  整合步骤如下：
		  ---引入开发包:
				Spring开发包,Hibernate开发包,数据库驱动包,连接池开发包;
		  ---根据表编写实体类和hbm.xml;
		  ---编写DAO接口;
		  ---根据DAO接口编写实现类实现类继承HibernateDaoSupport,利用它提供的
				HibernateTemplate完成增删改查操作;------可以通过Session对象来操作;
					super.getHibernateTemplate()返回一个HibernateTemplate
					◆新增:
						调用该对象的save(entity)方法;
					◆删除:
						调用该对象的delete(entity)方法;
					◆修改:
						调用该对象的update(entity)方法;
					◆查询:
						---通过ID查询:get(Cost.class, id),返回一个实体类型对象;
						---查询所有:find(hql),返回一个集合,这里写个hql:from Cost;						
		  ---将DAO实现类定义到Spring容器中;
		  ---定义SessionFactory，将SessionFactory给DAO注入;
		  ---获取Spring容器的DAO对象,测试;	
				◆主配置文件的内容
						//定义个连接池
						<bean id="myDataSource" class="org.apache.commons.dbcp.BasicDataSource">
							<property name="url" value="jdbc:oracle:thin:@192.168.0.199:1521:tarena"></property>
							<property name="driverClassName" value="oracle.jdbc.OracleDriver"></property>
							<property name="username" value="NETRPRO"></property>
							<property name="password" value="clq323"></property> 
						</bean>
						//<!-- 指定SessionFactory -->
						<bean id="mySessionFactory" 
								class="org.springframework.orm.hibernate3.LocalSessionFactoryBean">
						//<!-- 指定连接池DataSource -->
						<property name="dataSource" 
								ref="myDataSource">
						</property>
						//<!-- 指定Hibernate配置参数 -->
						<property name="hibernateProperties">
							<props>
								<prop key="hibernate.dialect">org.hibernate.dialect.OracleDialect</prop>
								<prop key="hibernate.show_sql">true</prop>
								<prop key="hibernate.format_sql">true</prop>
							</props>
						</property>
						//<!-- 指定hbm.xml文件 -->
						<property name="mappingResources">
							<list>
								<value>org/tarena/pojo/Cost.hbm.xml</value>
							</list>
						</property>
					</bean>	
					<bean id="hibernateCostDao" class="org.tarena.dao.HibernateCostDAO">
						<property name="sessionFactory"	ref="mySessionFactory"></property>
					</bean>	
			
#### 5.Spring和Struts2整合的应用
		hello.action ---> HelloAction --->hello.jsp
		---添加Struts2开发框架:
				引入jar包,struts.xml文件,web.xml添加filter配置;
		---添加Spring开发框架:
				引入jar包,applicationContext.xml;
		---*编写Action,DAO,利用Spring组件扫描,将Action和Dao扫描到Spring容器,将Dao给Action注入;
		---*在struts.xml中添加Action的配置;
			<action name="" class="spring容器中的Action组件的id" ></action>
		---*引入一个struts-spring-plugin.jar开发包(作用"利用class值去访问Spring容器取Bean对象);
		---在web.xml定义listener(作用:在启动tomcat时,实例化Spring容器);
		//各个配置文件配置的内容:	
			◆web.xml
					<!-- 配置过滤器 -->
					<filter>
						<filter-name>struts2filter</filter-name>
						<filter-class>org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter</filter-class>
				  </filter>
				  <filter-mapping>
					<filter-name>struts2filter</filter-name>
					<url-pattern>/*</url-pattern>                                                      */
				  </filter-mapping>
				  
				  <!-- 指定Spring配置文件位置和名称 -->
				  <context-param>
					<param-name>contextConfigLocation</param-name>
					<param-value>classpath:applicationContext.xml</param-value>
					/*classpath:表示是src目录*/
				  </context-param>
				  
				  <!-- 在服务器启动时,实例化Spring容器对象,配置监听器 -->
				  <listener>
					<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
				  </listener>
			
		◆struts.xml
				//注:这里class写的是对应Spring容器的Action组件的id,这里用的是注解方式
				<!-- 
					由于引入了struts-spring-plugin.jar
					当发出hello.action请求时,struts会使用该插件
					获取Action对象,去Spring容器以class值当做id值寻找
				 -->
				<action name="hello" class="helloAction">
		
		◆applicationContext.xml
				//扫描各个Bean对象
				<context:component-scan base-package="org.tarena"/>	
			
#### 6.struts-spring-plugin.jar开发包的作用:
		(1).该包是由Struts2框架提供,该包提供了一个StrutsSpringObjectFactory组件;
		(2).Struts2有一个ObjectFactory组件,该组件提供了buildAction(),buildBean方法,
			用于获取Action和其它组件的对象;
		(3).当引入struts-spring-plugin.jar时,会自动利用StrutsSpringObjectFactory替代
			Struts原有的ObjectFactory组件;	
			public class StrutsSpringObjectFactory{
				//调用此方法获取Action对象
				public Object buildAction(){
					try{
						/**-------获取Action对象的第一种途径-------*/
						//1.获取Spring容器对象
						
						//2.从容器中获取Action对象;
						Object action  = ac.getBean("class属性值");
						
						//3.返回Action对象
					}catch(){
						/**-------获取Action对象的第二种途径--------*/
						//1.通过反射机制创建一个Action对象
						Class c = Class.forName("org.tarena.action.HelloAction");
						Object action = c.newInstance();
						/*2.将Spring容器中的Bean对象给Action注入
						(默认是名称匹配,指的是Bean的id和Action的属性名匹配)*/
						
						//3.返回action对象;
					}
				}
			}
			
			
#### 基于StrutsSpringObjectFactory特点:
		(1).Struts与Spring集合可以有两种方法:
			参考图:
		(2).两种主要区别在与Action组件的位置不同
			---一种是交给Spring容器创建;
			---一种是交给ObjectFactory创建;
		(3).使用时有<action>配置的class值决定:
				如果class指定的是包名,类名,是交给ObjectFactory创建;	
				如果class指定的Bean的id,则是在Spring容器创建;
		(4).上述两种整合方法的使用:
			如果是Action + DAO 模式,建议采用上述:ObjectFactory创建;
			如果是Action +Service + DAO模式,建议采用:Spring容器创建;
			
#### SSH流程中的关键环节:
		(1).web.xml是否配置Struts2的Filter控制器-----决定请求能够进入Struts2处理流程;
		(2).struts.xml的<action name="" class="">配置
				------name决定请求是否有Action处理,
				------class属性决定Action由Spring创建还是由StrutsSpringObjectFactory创建;	
		(3).是否引入struts-spring-plugin.jar------决定是否能够完成Struts和Spring的整合
		(4).如果是走Spring容器创建流程:
				检查Action和DAO是否在Spring容器给Action注入;
				Action的id值是否与<action>的 class一致
		(5).如果是ObjectFactory创建流程:
			检查DAO是在Spring容器,DAO的id值是否与Action的属性名一致;
			<action>的class指定的包名,类名是否正确
		(6).检查web.xml是否包含listener
			<listener>
				<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
			</listener>
			-----决定Spring容器是否实例化
			
#### 9.	
    public class MyFilter implement Filter{
        public void doFilter(
            request,response,chain){
            //调用下一个filter或jsp,servlet
            chain.doFilter(request,response);
        }
    }

    public class StrutsFilter implement Filter{
        public void doFilter(
            request,response,chain){
            //1.判断是否为Struts请求
            //2.如果是/login,/login.action要根据struts.xml信息获取Action,调用Interceptor,Action,Result组件处理
            //3.如果是login.jsp请求,会执行chain.doFilter(request,response)方法去调用JSP资源
        }
    }
		//注意：
			Struts2的Filter控制器，如果是Action处理流程，不会执行chain.doFilter()代码,
			因此后续的Filter不会调用到.在使用中需要将其他的Filter定义到Struts2控制器之前。

    9.1.使用OpenSessionInViewFilter注意事项：
    --为了支持Hibernate延迟加载操作'
    --Spring容器中的SessionFactory必须命名为"sessionFactory"
    --该Filter必须定义在Struts的Filter之前
    --该Filter启用后,会将Session设置成只读模式,原有增删改操作不支持了,需要使用事务控制;		
			
			
#### 10.注解方式使用事务
		---在Spring配置中开启注解事务管理;
				<!-- 开启注解事务管理 -->
				<!-- 事务管理的方面组件 -->
				<bean id="txManager" class="org.springframework.orm.hibernate3.HibernateTransactionManager">
					<property name="sessionFactory" ref="sessionFactory"></property>
				</bean>
				<tx:annotation-driven transaction-manager="txManager" />
		---在目标组件中,类定义前或方法定义前加上@Transactional;
			
#### 11.XML事务配置
	Spring事务管理采用AOP机制实现;
	提供以下两种方式:	
		---声明式事务管理(基于配置方法)
			有XML和注解两种
			
			XML事务配置:
			<!-- xml配置事务 -->
			<bean id="txManager"	 class="org.springframework.orm.hibernate3.HibernateTransactionManager">
				<property name="sessionFactory" ref="sessionFactory"></property>
			</bean>
			<tx:advice id="txAdvice" transaction-manager="txManager">
				<tx:attributes>
					<tx:method name="execute" propagation="REQUIRED/>
					<tx:method name="*" />
				</tx:attributes>	
			</tx:advice>
			
			<aop:config>
				<aop:pointcut id="actionPointcut" expression="within(com.tarena.action)" />
				<aop:advisor advice-ref="txAdvice" pointcut-ref="actionPointcut" />
			</aop:config>
		---编程式事务管理(基于Java编程)
			利用TransactionTemplate将若干个操作封装;
			
#### 12.Spring的MVC简介		
	(1).Spring MVC主要构成组件:
		---DispatcherServlet			主控制器(C);
		---HandlerMapping			根据请求定位Controller;
		---Controller						业务控制器等价与Action;
		---ModelAndView			封装了模型数据和视图标识;
		---ViewResolver				视图解析器,定位视图组件生成响应界面(等同于struts.xml中的result);
		
	(2).Spring MVC主要流程:
		---客户端发出MVC请求,请求交给DspatcherServlet	主控制器处理;
		---主控制器调用HandlerMapping组件,根据请求名找到相应的Controller组件;
		---主控制器调用Controller组件,处理请求,处理完毕返回一个ModelAndView对象
			(Controller也可以调用DAO等组件);
		---主控制器调用ViewResolver解析ModelAndView对象,定位View组件并解析生成响应内容;
		---将响应内容输出给客户 ;
			
	(3).入门示例:
		login.do --> Controller -- > login.jsp
		---引入spring和spring mvc的开发包
		---在web.xml中添加DispatcherServlet配置
					<servlet>
						<servlet-name>springmvc</servlet-name>
						<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
						<init-param>
							<param-name>contextConfigLocation</param-name>
							<param-value>classpath:applicationContext.xml</param-value>
						</init-param>
				    </servlet>
				    <servlet-mapping>
						<servlet-name>springmvc</servlet-name>
						<url-pattern>*.do</url-pattern>
				    </servlet-mapping>
		---在src目录引入spring的主配置文件;
		---在applicationContext.xml配置中
			定义HandlerMapping组件
			定义ViewResolver组件
			定义Controller组件.
				<!-- 定义请求映射器,根据请求找Controller -->
					<bean id="handlerMapping" 
							class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
						<property name="mappings">
							<props>
								<prop key="tologin.do">toLoginController</prop>
								<prop key="login.do">loginController</prop>
							</props>
						</property>
					</bean>
					<!-- 定义一个视图解析器 -->
					<bean id="viewResolver" 
							class="org.springframework.web.servlet.view.InternalResourceViewResolver">
						<!-- 前缀 -->
						<property name="prefix" value="/"></property>
						<!-- 后缀 -->
						<property name="suffix" value=".jsp"></property>
					</bean>
					<bean id="toLoginController" scope="prototype" class="org.tarena.controller.ToLoginController">		
					</bean>
					<bean id="loginController" scope="prototype" class="org.tarena.controller.LoginController">		
					</bean>
		---在web.xml给DispatcherServlet指定位置和名称	
		---定义Controller组件:
			//注意:这里Controller必须实现Controller接口
			public class LoginController implements Controller{
				public ModelAndView handleRequest(HttpServletRequest req,HttpServletResponse res)throws Exception{
					String user = req.getParameter("user");
					String password = req.getParameter("password");
					Map<String, String> data = new HashMap<String, String>();
					if("scott".equals(user) && "tiger".equals(password)){
						data.put("user", user);
						return new ModelAndView("ok",data);		
					}
					data.put("error", "用户名或密码错误");
					return new ModelAndView("login",data);
				}
			}
			◆示例:
	(4).使用注解Spring MVC		
		login.do --> Controller -- > login.jsp
		---引入spring和spring mvc的开发包
		---在web.xml中添加DispatcherServlet配置
						<servlet>
							<servlet-name>springmvc</servlet-name>
							<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
							<init-param>
								<param-name>contextConfigLocation</param-name>
								<param-value>classpath:applicationContext.xml</param-value>
							</init-param>
					   </servlet>
					  
					    <servlet-mapping>
							<servlet-name>springmvc</servlet-name>
							<url-pattern>*.do</url-pattern>
					    </servlet-mapping>
		---在applicationContext.xml配置中:
				<!-- 定义请求映射器,根据请求找Controller,支持在Controller -->
				<bean id="handlerMapping" 
						class="org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter">		
				</bean>
				<!-- 定义一个视图解析器 -->
				<bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
					<!-- 前缀 -->
					<property name="prefix" value="/"></property>
					<!-- 后缀 -->
					<property name="suffix" value=".jsp"></property>
				</bean>
				<!-- 扫描组件 -->
				<context:component-scan base-package="org.tarena"/>
		---在Controller组件的业务方法上直接使用:
				@RequestMapping("请求")指定
		---Controller组件可以是一个简单的Class,业务方法可以灵活定义成以下格式
			public String execute(){}
			public String execute(request){}
			public String execute(request,response){}
			public String execute(User user){}
			public String execute(Model model){}
			public String execute(User user,Model model){}
			
			
			◆示例:
				@Controller
				@Scope("prototype")
				public class LoginController{
					/**
					 * 
					 * @param user 	接收请求的参数,属性名需与表单的name属性值一致
					 * @param model	用于向响应页面传递数据,实质是个map集合
					 * @return
					 * 如果需要request,response直接在execute方法中定义参数即可
					 */
					@RequestMapping("/login.do")
					public String execute(User user,Model model){
						String username = user.getUser();
						String password = user.getPassword();
						if("scott".equals(username) && "tiger".equals(password)){
							model.addAttribute("user",username);
							return "ok";
						}
						model.addAttribute("error","用户名或密码错误");
						return "login";
					}
					
				}


				










				
				
				
				