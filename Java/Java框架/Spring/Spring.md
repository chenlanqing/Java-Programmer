<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、Spring概念](#%E4%B8%80spring%E6%A6%82%E5%BF%B5)
  - [1、Spring是什么](#1spring%E6%98%AF%E4%BB%80%E4%B9%88)
  - [2、具体描述 Spring](#2%E5%85%B7%E4%BD%93%E6%8F%8F%E8%BF%B0-spring)
- [二、IOC(DI)](#%E4%BA%8Ciocdi)
  - [1、IOC(Inversion of Control)](#1iocinversion-of-control)
  - [2、Spring容器](#2spring%E5%AE%B9%E5%99%A8)
  - [3、DI(Dependency Injection)](#3didependency-injection)
  - [4、各种类型信息的注入](#4%E5%90%84%E7%A7%8D%E7%B1%BB%E5%9E%8B%E4%BF%A1%E6%81%AF%E7%9A%84%E6%B3%A8%E5%85%A5)
- [三、Spring AOP](#%E4%B8%89spring-aop)
  - [1、关于面向切面编程](#1%E5%85%B3%E4%BA%8E%E9%9D%A2%E5%90%91%E5%88%87%E9%9D%A2%E7%BC%96%E7%A8%8B)
  - [2、AOP 的优势](#2aop-%E7%9A%84%E4%BC%98%E5%8A%BF)
  - [3、AOP 的术语](#3aop-%E7%9A%84%E6%9C%AF%E8%AF%AD)
  - [6、Aop：通知](#6aop%E9%80%9A%E7%9F%A5)
- [四、IOC与AOP原理分析](#%E5%9B%9Bioc%E4%B8%8Eaop%E5%8E%9F%E7%90%86%E5%88%86%E6%9E%90)
  - [1、IOC原理](#1ioc%E5%8E%9F%E7%90%86)
  - [2、AOP原理](#2aop%E5%8E%9F%E7%90%86)
- [五、Spring的 事务](#%E4%BA%94spring%E7%9A%84-%E4%BA%8B%E5%8A%A1)
  - [1、事务：一组逻辑操作](#1%E4%BA%8B%E5%8A%A1%E4%B8%80%E7%BB%84%E9%80%BB%E8%BE%91%E6%93%8D%E4%BD%9C)
  - [2、Spring 的事务管理](#2spring-%E7%9A%84%E4%BA%8B%E5%8A%A1%E7%AE%A1%E7%90%86)
  - [3、声明式事务管理](#3%E5%A3%B0%E6%98%8E%E5%BC%8F%E4%BA%8B%E5%8A%A1%E7%AE%A1%E7%90%86)
  - [6、事务隔离级别：通过隔离事务属性指定 isolation](#6%E4%BA%8B%E5%8A%A1%E9%9A%94%E7%A6%BB%E7%BA%A7%E5%88%AB%E9%80%9A%E8%BF%87%E9%9A%94%E7%A6%BB%E4%BA%8B%E5%8A%A1%E5%B1%9E%E6%80%A7%E6%8C%87%E5%AE%9A-isolation)
  - [9、Spring 中的部分注解配置](#9spring-%E4%B8%AD%E7%9A%84%E9%83%A8%E5%88%86%E6%B3%A8%E8%A7%A3%E9%85%8D%E7%BD%AE)
- [六、Spring的三种配置方式](#%E5%85%ADspring%E7%9A%84%E4%B8%89%E7%A7%8D%E9%85%8D%E7%BD%AE%E6%96%B9%E5%BC%8F)
  - [1、基于xml方式配置](#1%E5%9F%BA%E4%BA%8Exml%E6%96%B9%E5%BC%8F%E9%85%8D%E7%BD%AE)
  - [2、基于注解方式配置](#2%E5%9F%BA%E4%BA%8E%E6%B3%A8%E8%A7%A3%E6%96%B9%E5%BC%8F%E9%85%8D%E7%BD%AE)
  - [3、基于Java方式配置](#3%E5%9F%BA%E4%BA%8Ejava%E6%96%B9%E5%BC%8F%E9%85%8D%E7%BD%AE)
- [七、Spring中涉及的设计模式](#%E4%B8%83spring%E4%B8%AD%E6%B6%89%E5%8F%8A%E7%9A%84%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F)
- [参考资料](#%E5%8F%82%E8%80%83%E8%B5%84%E6%96%99)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 一、Spring概念
## 1、Spring是什么

- Spring 是一个开源框架.
- Spring 为简化企业级应用开发而生. 使用 Spring 可以使简单的 JavaBean 实现以前只有 EJB 才能实现的功能.
- Spring 是一个 IOC(DI) 和 AOP 容器框架;

## 2、具体描述 Spring

- 轻量级：Spring 是非侵入性的 - 基于 Spring 开发的应用中的对象可以不依赖于 Spring 的 API
- 依赖注入(DI --- dependency injection、IOC)
- 面向切面编程(AOP --- aspect oriented programming)
- 容器：Spring 是一个容器，因为它包含并且管理应用对象的生命周期
- 框架：Spring 实现了使用简单的组件配置组合成一个复杂的应用. 在 Spring 中可以使用 XML 和 Java 注解组合这些对象
- 一站式：在 IOC 和 AOP 的基础上可以整合各种企业应用的开源框架和优秀的第三方类库 (实际上 Spring 自身也提供了展现层的 SpringMVC 和 持久层的 Spring JDBC)

Spring容器初始化时：首先会初始化 bean，即构造相关类

# 二、IOC(DI)

## 1、IOC(Inversion of Control)

其思想是反转资源获取的方向. 传统的资源查找方式要求组件向容器发起请求查找资源。作为回应，容器适时的返回资源.而应用了 IOC 之后，则是容器主动地将资源推送给它所管理的组件，组件所要做的仅是选择一种合适的方式来接受资源。这种行为也被称为查找的被动形式

控制反转：把创建对象(Bean)和维护对象(Bean)的关系的权利从程序中转移到Spring容器中，程序不再控制

- IOC 机制实现Bean之间的调用;
- IOC 解决的问题：可以降低两个组件对象之间的关联，降低耦合度;
- Spring IOC 控制反转，哪些方面的控制被反转了呢？获得依赖对象的过程被反转了，由主动获取变为被动获取
- 使用 IOC 的优势：
	- 维护性比较好，非常便于进行单元测试，便于调试程序和诊断故障。代码中的每一个Class都可以单独测试，彼此之间互不影响，只要保证自身的功能无误即可，这就是组件之间低耦合或者无耦合带来的好处;
	- 开发团队的成员都只需要关心实现自身的业务逻辑，完全不用去关心其它的人工作进展，因为你的任务跟别人没有任何关系，你的任务可以单独测试，你的任务也不用依赖于别人的组件
	- 可复用性好，我们可以把具有普遍性的常用组件独立出来，反复利用到项目中的其它部分，或者是其它项目，当然这也是面向对象的基本特征，IOC 不仅更好地贯彻了这个原则，提高了模块的可复用性。符合接口标准的实现，都可以插接到支持此标准的模块中;
	- 完全具有热插拨的特性，IOC 生成对象的方式转为外置方式，也就是把对象生成放在配置文件里进行定义；

- IOC 使用的技术：最基本的技术就是反射

	IOC 容器的工作模式可以看成是工厂模式的升华；IOC 容器看作是一个工厂，这个工厂里要生产的对象都在配置文件中给出定义，然后利用编程语言的的反射编程，根据配置文件中给出的类名生成相应的对象。从实现来看，IOC 是把以前在工厂方法里写死的对象生成代码，改变为由配置文件来定义，也就是把工厂和对象生成这两者独立分隔开来，目的就是提高灵活性和可维护性；

- Spring 中的 IOC
	- Spring 中的 org.springframework.beans 包和 org.springframework.context包构成了Spring框架IoC容器的基础
	- BeanFactory 接口提供了一个先进的配置机制，使得任何类型的对象的配置成为可能：ApplicationContex 接口对 BeanFactory（是一个子接口）进行了扩展，在BeanFactory的基础上添加了其他功能，比如与Spring的AOP更容易集成，也提供了处理message resource的机制(用于国际化)、事件传播以及应用层的特别配置，比如针对Web应用的WebApplicationContext
	- org.springframework.beans.factory.BeanFactory 是 Spring IOC 容器的具体实现，用来包装和管理前面提到的各种bean。BeanFactory接口是Spring IoC 容器的核心接口

## 2、Spring容器

在 Spring IOC 容器读取 Bean 配置创建 Bean 实例之前，必须对它进行实例化. 只有在容器实例化后，才可以从 IOC 容器里获取 Bean 实例并使用

Spring 提供了两种类型的 IOC 容器实现：

- BeanFactory：IOC 容器的基本实现.
- ApplicationContext：提供了更多的高级特性. 是 BeanFactory 的子接口.


- （1）BeanFactory 是 Spring 框架的基础设施，面向 Spring 本身；ApplicationContext 面向使用 Spring 框架的开发者，几乎所有的应用场合都直接使用 ApplicationContext;而非底层的 BeanFactory；但是无论使用何种方式，配置文件时相同的。

	ApplicationContext 的主要实现类：【 ApplicationContext 在初始化上下文时就实例化所有单例的 Bean】
	- ①、ClassPathXmlApplicationContext：从类路径下加载配置文件;
	- ②、FileSystemXmlApplicationContext：从文件系统中加载配置文件;
	- ③、WebApplicationContext：是专门为 WEB 应用而准备的，它允许从相对于 WEB 根目录的路径中完成初始化工作;

- （2）Spring 容器对 Bean 的管理：
	- 控制Bean对象创建模式：在bean元素中，利用scope属性可以指定Bean组件创建对象的方式：
		- prototype：非单例模式
		- singleton：单例模式(默认是单例模式)

		在web程序中，通过一些配置，可以扩展出request，session等属性值;

	- 可以控制单例模式的创建时机：
		- singleton模式的Bean组件，默认是在 ApplicationContext 容器实例化时就创建了组件;可以在bean元素中追加属性lazy-init="true"，将singleton模式创建对象推迟到getBean()方法
		- prototype模式是在调用getBean()方法时创建了组件;
		
	- 可以指定Bean对象初始化和销毁方法：```<bean init-method="初始化方法" destroy-method="销毁方法">```
		- Spring 将 Bean 对象创建完毕后，会自动调用init-method里指定的方法
		- destroy-method指定的方法需满足下面条件才能执行：
			- scope="singleton";才能使用;
			- 执行AbstractApplicationContext容器的close()方法触发;
- BeanFactory 和 ApplicationContext 区别：
	- ①、BeanFactory 可以理解为含有bean集合的工厂类，包含了各种bean的定义，以便在接收请求时对应的Bean实例化BeanFactory 包含了bean生命周期的控制
	- ②、application context如同bean factory一样具有bean定义、bean关联关系的设置，根据请求分发bean的功能。但application context在此基础上还提供了其他的功能。
		- Ⅰ.提供了支持国际化的文本消息
		- Ⅱ.统一的资源文件读取方式
		- Ⅲ.已在监听器中注册的bean的事件

## 3、DI(Dependency Injection)

IOC 的另一种表述方式：即组件以一些预先定义好的方式(例如：setter 方法)接受来自如容器的资源注入.。相对于 IOC 而言，这种表述更直接。依赖注入(DI)和控制反转(IOC)是从不同的角度的描述的同一件事情：就是指通过引入IOC容器，利用依赖关系注入的方式，实现对象之间的解耦

以来注入是Spring实现IoC的技术途径，依赖注入的方式：
- setter方式注入：(推荐使用)，使用步骤：
	- 在Action中定义dao接口变量及其set方法;
	- 在Spring配置Action组件的bean元素，使用下面格式：
		```
		<property name="属性名" ref="要注入的Bean对象的id属性值"></property>
		```
- 构造方式注入：使用步骤：
	- 在Action中定义dao接口变量以及带参数的构造方法(参数为dao接口类型的变量);
	- 在Spring配置Action组件的bean元素，使用下面格式：
		```
		<constructor-arg index="指定参数索引(从0开始)" ref="要注入的Bean对象的id属性值">
		</constructor-arg>
		```
				
## 4、各种类型信息的注入

- 注入Bean对象：(使用最多)
	```xml
	<property name="属性名" ref="要注入的Bean对象的id属性值"></property>
	```

- 注入基本类型
	- 注入数值或字符串类型
		```xml
		<property name="属性名" value="属性值" ></property>
		<!-- 以set方法绝对name值 ，注入的类型有set方法参数类型决定-->
		<property name="includeTypes" value="jpg，jpeg，gif"></property>
		```

- 注入集合类型：(以下property都是写在bean元素内的)

	- 注入List集合配置方法：
		```xml
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
		```	
	- 注入Set集合配置方法：
		```xml
		<!-- 注入Set集合 -->
		<property name="属性名">
			<set>
				<value>Tom</value>
				<value>Sam</value>
				<value>Coco</value>
			</set>
		</property>
		```
	- 注入Map类型配置方法：
		```xml
		<!-- 注入Map类型数据 -->
		<property name="books">
			<map>
				<entry key="1001" value="Core Jave"></entry>
				<entry key="1002" value="Java Web"></entry>
				<entry key="1003" value="SSH"></entry>
			</map>
		</property>
		```
				
	- 注入Properties类型配置方法：
		```xml
		<!-- 注Properties类型数据 -->
		<property name="prop">
			<props>
				<prop key="show_sql">true</prop>
				<prop key="dialect">org.hibernate.dialect.OracleDialect</prop>
			</props>
		</property>	
		```	
		
# 三、Spring AOP

AOP(Aspect-Oriented Programming，面向切面编程	

## 1、关于面向切面编程

- AOP 的主要编程对象是切面(aspect)，而切面模块化横切关注点
- 在应用 AOP 编程时，仍然需要定义公共功能，但可以明确的定义这个功能在哪里，以什么方式应用，并且不必修改受影响的类. 这样一来横切关注点就被模块化到特殊的对象(切面)里

## 2、AOP 的优势

- 每个事物逻辑位于一个位置，代码不分散，便于维护和升级
- 业务模块更简洁，只包含核心业务代码;
	
## 3、AOP 的术语
- 切面(Aspect)：横切关注点(跨越应用程序多个模块的功能)被模块化的特殊对象
- 通知(Advice)：切面必须要完成的工作
- 目标(Target)：被通知的对象
- 代理(Proxy)：向目标对象应用通知之后创建的对象;
	- 当目标组件使用了AOP切入方面组件后，getBean()方法返回的类型是采用动态代理技术生成的一个新类型，当使用代理对象执行业务方法时，代理对象会调用方面和原目标对象的处理方法
	- Spring框架提供了两种代理技术的实现：
		- CGLIB技术：适用于没有接口的目标组件;
			```java
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
			```
		- JDK Proxy API:适用于有接口的目标组件;
			```java
			public class 代理类型 implement 目标接口{
			
			}
			//利用接口变量接收代理类对象
			ICostDao dao = (ICostDao)ac.getBean("jdbcCostDao");
			dao.save();//调用代理类型的save()方法
			```
- 连接点(Joinpoint)：程序执行的某个特定位置：如类某个方法调用前、调用后、方法抛出异常后等。连接点由两个信息确定：方法表示的程序执行点；相对点表示的方位。例如 ArithmethicCalculator#add() 方法执行前的连接点，执行点为 ArithmethicCalculator#add()； 方位为该方法执行前的位置；

- 切点(pointcut)：每个类都拥有多个连接点：例如 ArithmethicCalculator 的所有方法实际上都是连接点，即连接点是程序类中客观存在的事务.AOP 通过切点定位到特定的连接点.类比：连接点相当于数据库中的记录，切点相当于查询条件。切点和连接点不是一对一的关系，一个切点匹配多个连接点，切点通过 org.springframework.aop.Pointcut 接口进行描述，它使用类和方法作为连接点的查询条件；

- advisor：Advice和Pointcut组成的独立的单元，并且能够传给proxy factory 对象
	```xml
	<aop：config>
		<aop：pointcut expression="execution(* *.BookShopDao.*(..))" id="bookShop"/>
		<aop：advisor advice-ref="bookShopTXDao" pointcut-ref="bookShop"/>
	</aop：config>
	```	
## 4、基本使用

- 4.1、使用注解来使用AOP

	- 引入相关的jar包：aopalliance.jar、aspectj.weaver.jar 和 spring-aspects.jar
	- 配置文件引入相关的命名空间，并在配置文件中加入如下配置：
		```<aop：aspectj-autoproxy></aop：aspectj-autoproxy>```
	- 在配置文件中加入IOC容器扫描Bean的配置：
		```<context：component-scan base-package="com.bluefish.aop.impl"></context：component-scan>```
	- 编写切面类，即共通属性：
		- ①.首先切面是一个Bean，需要加入注解：@Component
		- ②.其次，类是一个切面，需加入注解：@Aspect;
	- 在切面类中声明通知：(这里以前置通知为准)
		- ①.声明一个方法;
		- ②.在方法上加入注解：@Before("execution(*)")
		- ③.如果需要获取访问链接的细节问题，如方法名和参数等，可以在声明的方法中加入JoinPoint参数<br>
			获取方法签名：joinPoint.getSignature()<br>
			获取方法参数：joinPoint.getSignature()<br>
		如：
		```java
		@Aspect
		@Component
		public class LoggingAspectJ {
			@Before("execution(int com.bluefish.aop.impl.ICaculator.*(int，int))")
			public void beforeMethod(JoinPoint joinPoint){
				String methodName = joinPoint.getSignature().getName();
				List<Object> args = Arrays.asList(joinPoint.getArgs());
				System.out.println("The method " + methodName + " begins and the args is ：" + args);
			}
		}
		```
- 4.2、使用xml使用AOP
	```
	将共通处理组件定义成一个<bean>
	定义<aop：pointcut>，指定哪些组件为目标，即切入点;
	定义<aop：aspect>，切面;
	定义<aop：before>，指定通知：
			<bean id="loggingAspectJ" class="com.bluefish.aop.xml.LoggingAspectJ"></bean>	
			<bean id="validationAspectJ" class="com.bluefish.aop.xml.ValidationAspectJ"></bean>
			<!-- 利用Spring的AOP机制将CheckRoleBean作用到各个Action的execute方法 -->
			<aop：config>
				<!-- 配置切入点表达式 -->
				<aop：pointcut expression="execution(* *.*(..))" id="pointcut"/>
				<!-- 指定切面，ref：引用配置好的bean，order：切面优先级 -->
				<aop：aspect ref="loggingAspectJ" order="2">
					<!-- 前置通知：method：切面中的方法，pointcut-ref：切入点 --> 
					<aop：before method="beforeMethod" pointcut-ref="pointcut"/>
					<!-- 
						返回通知：returning：连接点执行的结果，对应方法中的返回值参数
						public void afterReturnningMethod(JoinPoint joinPoint，Object result){}
					-->
					<aop：after-returning method="afterReturnningMethod" pointcut-ref="pointcut" returning="result"/>
					<!-- 
						异常通知：throwing：表示该方法中的异常参数
						public void afterThrowingMethod(JoinPoint joinPoint，Exception e){}
					-->
					<aop：after-throwing method="afterThrowingMethod" pointcut-ref="pointcut" throwing="e"/>
				</aop：aspect>
				<aop：aspect ref="validationAspectJ" order="1">
					<aop：before method="beforeMethod" pointcut-ref="pointcut"/>
				</aop：aspect>
			</aop：config>
	```
## 5、切面

切面的优先级：
- 在同一个连接点上应用不止一个切面时，除非明确指定，否则它们的优先级是不确定的.
- 切面的优先级可以通过实现 Ordered 接口或利用 @Order 注解指定.
- 实现 Ordered 接口，getOrder() 方法的返回值越小，优先级越高.
- 若使用 @Order 注解，序号出现在注解中	

## 6、Aop：通知
```java
try{
	// 前置通知：在连接点执行前执行的通知
	// 方法执行体
	// 返回通知：在连接点正常完成后执行的通知，返回通知可以访问连接点执行的结果;不包括抛出异常的情况
} catch (Exception e){
	// 异常通知：连接点执行时抛出异常后执行的通知
} finally {
	// 后置通知：当某连接点退出的时候执行的通知(不论正常返回还是抛出异常)，其访问不到连接点执行的结果
}
```
- 6.1、前置通知(Before)：在方法执行之前执行的通知，前置通知使用 @Before 注解，并将切入点表达式的值作为注解值.
	
- 6.2、后置通知(After)：后置通知是在连接点完成之后执行的，即连接点返回结果或者抛出异常的时候；
	
- 6.3、返回通知(AfterReturning)：连接点是正常返回时执行的通知. 

	只要将 returning 属性添加到 @AfterReturning 注解中，就可以访问连接点的返回值。该属性的值即为用来传入返回值的参数名称.必须在通知方法的签名中添加一个同名参数。在运行时，Spring AOP 会通过这个参数传递返回值.原始的切点表达式需要出现在 pointcut 属性中
	
	 @AfterReturning(pointcut="execution(int com.bluefish.aop.impl.ICaculator.*(int，int))"，returning="result")
	
- 6.4.异常通知(AfterThrowing)：连接点执行异常时执行的通知	
	- 可以访问到异常对象; 且可以指定在出现特定异常时在执行通知代码
		```java
		@AfterThrowing(value="declareJointPointExpression()"，throwing="e")
		public void afterThrowing(JoinPoint joinPoint，Exception e){}
		```
	- 如果只对某种特殊的异常类型感兴趣，可以将参数声明为其他异常的参数类型. 通知就只在抛出这个类型及其子类的异常时才被执行
		```java
		@AfterThrowing(value="declareJointPointExpression()"，throwing="e")
		public void afterThrowing(JoinPoint joinPoint，NullPointerException e){}
		```
		
- 6.5、环绕通知(Around)：环绕通知是所有通知类型中功能最为强大的，能够全面地控制连接点. 甚至可以控制是否执行连接点
	- 环绕通知需要携带 ProceedingJoinPoint 类型的参数. 它是 JoinPoint 的子接口
	- 环绕通知类似于动态代理的全过程：ProceedingJoinPoint 类型的参数可以决定是否执行目标方法。proceed() 方法来执行被代理的方法，如果忘记这样做就会导致通知被执行了，但目标方法没有被执行;
	- 环绕通知必须有返回值，返回值即为目标方法的返回值，即调用 joinPoint.proceed(); 的返回值，否则会出现空指针异常;
		```java
		@Around("execution(public int com.atguigu.spring.aop.ArithmeticCalculator.*(..))")
		public Object aroundMethod(ProceedingJoinPoint pjd){
			
			Object result = null;
			String methodName = pjd.getSignature().getName();
			
			try {
				//前置通知
				System.out.println("The method " + methodName + " begins with " + Arrays.asList(pjd.getArgs()));
				//执行目标方法
				result = pjd.proceed();
				//返回通知
				System.out.println("The method " + methodName + " ends with " + result);
			} catch (Throwable e) {
				//异常通知
				System.out.println("The method " + methodName + " occurs exception：" + e);
				throw new RuntimeException(e);
			}
			//后置通知
			System.out.println("The method " + methodName + " ends");
			
			return result;
		}
		```
## 7、切入点表达式

- 7.1、重用切入点表达式：
	- 在AOP中，编写AspectJ切面时，同一个切点表达式可能会在多个通知中重复出现，可以通过 @Pointcut注解将一个切入点声明为简单的方法，其方法体通常都是空的;
	- 切入点方法的访问控制符同时也控制着这个切入点的可见性，如果切入点要在多个切面中共用，最好将它们集中在一个公共的类中在这种情况下，它们必须被声明为 public. 在引入这个切入点时，必须将类名也包括在内. 如果类没有与这个切面放在同一个包中，还必须包含包名.
		```java
		@Pointcut("execution(* *.*(..))")
		public void declarePointExpression(){}
		
		@Before("declarePointExpression()")
		public void beforeMethod(JoinPoint joinPoint){...}
		```
- 7.2、execution(修饰符? 返回类型 方法名(参数列表) throws 异常类型?)
	```
	问号表示：修饰符 或 异常可以不写，必须指定返回类型，方法名，方法参数列表
	7.2.1.方法限定表达式：
		可以指定哪些方法当作目标启用方面功能;
			execution(修饰符? 返回类型 方法名(参数列表) throws 异常类型?)
			//问号表示：修饰符 或 异常可以不写，必须指定返回类型，方法名，方法参数列表			
		示例1：
			匹配方法名以find开头的Bean对象
			execution(* find*(..))
				---" * " 表示有返回值或者没有返回值，" .."：表示参数是0个以上;" find* "：匹配方法名以find开头的			
		示例2：
			匹配JdbcCostDao的save方法
			execution(* org.dao.JdbcCostDao.save(..))			
		示例3：
			匹配org.dao包下所有类的所有方法
			execution(* org.dao.*.*(..))
		示例4：
			匹配org.dao包及其子包下所有类的所有方法
			execution(* org.dao..*.*(..))			
		示例5：
			必须有返回值
			execution(!void set*(..))				
	7.2.2.类型限定表达式
		可以指定哪个组件的所有方法都启用方面功能
		within(类型);			
		示例1：
			AddCostAction中所有方法：
			within(action.AddCostAction);			
		示例2：
			匹配org.action包下所有类的所有方法
			within(org.action.*);			
		示例3：
			匹配org.action包下及其子包下所有类的所有方法
			within(org.action..*);

	7.2.3.Bean组件id或name限定表达式
		bean(id或name属性值)
		//注意：
			id和name属性都是用于指定Bean组件的标识符，
			但是id更严格，不允许使用" / "等特殊字符，而name允许;				
		示例1：
			匹配bean元素id或name属性为jdbcCostDao的组件的所有方法
			bean("jdbcCostDao");
		示例2：
			匹配<bean>元素id或name属性以Action结尾的组件的所有方法
			bean(*Action)

	7.2.4.方法参数限定表达式
		args(参数列表)
		
		示例：
			匹配只有一个参数的类型为String的方法
			args(java.lang.String)

		提示：上述表达式可以使用 ||，&&进行拼接
		如：within(org.action..*) && !execution(...);
	```
        
# 四、IOC与AOP原理分析

## 1、IOC原理



## 2、AOP原理

- OOP 允许你定义从上到下的关系，但并不适合定义从左到右的关系，AOP 技术则恰恰相反，它利用一种称为“横切”的技术，剖解开封装的对象内部，并将那些影响了多个类的公共行为封装到一个可重用模块，并将其名为“Aspect”，就是将那些与业务无关，却为业务模块所共同调用的逻辑或责任封装起来，便于减少系统的重复代码，降低模块间的耦合度，并有利于未来的可操作性和可维护性；

- AOP 是面向对象的一种补充，应用于处理一些具有横切性质的系统级服务，如事务管理、安全检查、缓存、对象池管理等；

- AOP 实现的关键：AOP 框架自动创建的 AOP 代理
	- 静态代理：指使用 AOP 框架提供的命令进行编译，从而在编译阶段就可生成 AOP 代理类，因此也称为编译时增强
	- 动态代理：在运行时借助于 JDK 动态代理、CGLIB 等在内存中“临时”生成 AOP 动态代理类，因此也被称为运行时增强

# 五、Spring的 事务

## 1、事务：一组逻辑操作

用来确保数据的完整性和一致性，是一系列动作，这些动作要么全部完成要么全部不起作用；

- 1.1、四个关键属性：
	- 原子性(atomicity)：事务是个不可分割的工作单位，事务是一个原子操作，由一系列动作组成. 事务的原子性确保动作要么全部完成要么完全不起作用.
	- 一致性(consistency)：事务前后数据的完整性必须保持一致，一旦所有事务动作完成，事务就被提交. 数据和资源就处于一种满足业务规则的一致性状态中.
	- 隔离性(isolation)：多个用户并发访问数据库时，一个用户的事务不能被其他用户的事务所干扰，多个用户的并发事务相互隔离的，可能有许多事务会同时处理相同的数据，因此每个事物都应该与其他事务隔离开来，防止数据损坏.
	- 持久性(durability)：一个事务一旦被提交，它对数据库中数据的改变是永久性的，即使数据库发生故障也不会有任何影响，一旦事务完成，无论发生什么系统错误，它的结果都不应该受到影响. 通常情况下，事务的结果被写到持久化存储器；

- 1.2、Spring 事务接口：主要有三个接口

	- PlatformTransactionManager-平台事务管理器：Spring 为不同的持久框架提供了不同的接口实现；DataSourceTransactionManager：使用 Spring JDBC 或 mybatis 进行持久化数据时使用
	- TransactionDefinition-事务定义信息(隔离，传播，超时，只读)
	- TransactionStatus-事务具体运行状态

## 2、Spring 的事务管理

- 2.1、支持编程式事务管理与声明式事务管理
	- 编程式事务管理：将事务管理代码嵌入到业务方法中来控制事务的提交和回滚。在编程式管理事务时，必须在每个事务操作中包含额外的事务管理代码. 
	- 声明式事务管理：大多数情况下比编程式事务管理更好用. 它将事务管理代码从业务方法中分离出来，以声明的方式来实现事务管理。事务管理作为一种横切关注点，可以通过 AOP 方法模块化. Spring 通过 Spring AOP 框架支持声明式事务管理

- 2.2、Spring 事务管理核心：TransactionManager，其为事务管理封装了一组独立于技术的方法。对于JDBC，JavaEE，Hibernate 等都实现了相应的事务管理器;

## 3、声明式事务管理

- 事务管理是一种横切关注点
- xml配置声明式事务管理：
	```XML
	<!-- 声明事务管理器-->
	<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSource" />
	</bean>
	<!-- 声明事务通知-->
	<tx：advice id="bookShopDao" transaction-manager="transactionManager"></tx：advice>
	<!-- -->
	<aop：config>
		<aop：pointcut expression="execution(* *.BookShopDao.*(..))" id="bookShop"/>
		<aop：advisor advice-ref="bookShopTXDao" pointcut-ref="bookShop"/>
	</aop：config>
	```
- 注解声明式事务管理：
	```XML
	<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSource" />
	</bean>		
	<tx：annotation-driven transaction-manager="transactionManager"/>
	// 在需要事务管理的方法前加上注解，或者可以直接加在类上
	@Transactional
	```
## 5、事务的传播性：当事务方法被另一个事务方法调用时，必须指定事务应该如何传播

- 5.1、Spring 支持的事务传播行为：
	- REQUIRED：业务方法需要在一个容器里运行。如果方法运行时，已经处在一个事务中，那么加入到这个事务，否则自己新建一个新的事务。
	- REQUIRES_NEW：不管是否存在事务，该方法总汇为自己发起一个新的事务。如果方法已经运行在一个事务中，则原有事务挂起，新的事务被创建。
	- NOT_SUPPORTED：声明方法不需要事务。如果方法没有关联到一个事务，容器不会为他开启事务，如果方法在一个事务中被调用，该事务会被挂起，调用结束后，原先的事务会恢复执行。
	- SUPPORTS：该方法在某个事务范围内被调用，则方法成为该事务的一部分.如果方法在该事务范围外被调用，该方法就在没有事务的环境下执行。
	- MANDATORY：该方法只能在一个已经存在的事务中执行，业务方法不能发起自己的事务。如果在没有事务的环境下被调用，容器抛出例外。
	- NEVER：该方法绝对不能在事务范围内执行。如果在就抛例外。只有该方法没有关联到任何事务，才正常执行。
	- NESTED：如果一个活动的事务存在，则运行在一个嵌套的事务中。如果没有活动事务，则按REQUIRED属性执行，它使用了一个单独的事务，这个事务拥有多个可以回滚的保存点。内部事务的回滚不会对外部事务造成影响。它只对 DataSourceTransactionManager 事务管理器起效;
	```
		类A有事务方法：buyMany();
		类B有事务方法：buy();
		buyMany()方法调用了 buy()方法
	```
- 5.2、传播行为：REQUIRED(Spring 事务的默认传播行为)

	buy()方法被 buyMany()方法调用，其会默认在 buyMany()方法中现有的事务中运行，因此 buyMany()方法的开始和终止边界内只有一个事务. 如果buyMany()方法出现异常或buy()方法出现异常，则全部回滚，或全部成功后全部提交
	
- 5.3、传播行为：REQUIRES_NEW
	
	如果 buy()方法中配置了：@Transactional(propagation=Propagation.REQUIRES_NEW)，则buyMany()调用buy()时，buyMany()方法的事务会挂起，buy()方法开启新事务，buy()的事务提交完毕后，buyMany()方法的事务继续.

- 5.4、配置事务的传播行为：使用propagation属性指定事务的传播行为

	- 配置：
		```xml
		<tx：advice id="bookShopDao" transaction-manager="transactionManager">
			<tx：attributes>
				<tx：method name="buyMany" propagation="REQUIRES_NEW"/>
			</tx：attributes>
		</tx：advice>
		```
	注解：@Transactional(propagation=Propagation.REQUIRES_NEW)
			
## 6、事务隔离级别：通过隔离事务属性指定 isolation	

- 6.1、务隔离级别：

	- DEFAULT：使用底层数据库的默认隔离级别，对于大多数数据库来说，默认的隔离级别都是：READ_COMMITTED
	- READ_UNCOMMITTED：允许事务读取未被其他事务提交的变更，脏读、不可重复度、幻读的问题都会出现
	- READ_COMMITTED：只允许事务读取已被其他事务提交的变更，可以避免脏读，但不可重复读、幻读的问题仍可能出现;
	- SERIALIZABLE：确保事务可以从一个表中读取相同的行，在这个事务持续期间，禁止其他事务对该表执行插入，更新，和删除等操作，所有并发问题都可以避免，但性能十分低下;串行
	- REPEATABLE_READ：对相同字段的多次读取是一致的，除非数据被事务本身改变.可防止脏读，不可重复读，但幻读仍可能发生；

- 6.2、事务的隔离级别要得到底层数据库引擎的支持，而不是应用程序或者框架的支持

	- Oracle 支持两种事务隔离级别：READ_COMMITED(默认级别) ，SERIALIZABLE
	- Mysql 支持四种隔离级别：默认是 REPEATABLE_READ 级别

- 6.3、设置事务隔离属性：@Transactional(isolation=Isolation.READ_COMMITTED)	

- 6.4、设置事务回滚属性：默认情况下只有未检查异常(RuntimeException和Error类型的异常)会导致事务回滚

	事务的回滚规则可以通过 @Transactional 注解的 rollbackFor 和 noRollbackFor 属性来定义。这两个属性被声明为 Class[] 类型的，因此可以为这两个属性指定多个异常类。rollbackFor： 遇到时必须进行回滚
	
	rollback-for="Exception" ，noRollbackFor：一组异常类，遇到时必须不回滚

- 6.5、事务超时和只读属性：
	- 由于事务可以在行和表上获得锁，因此长事务会占用资源，并对整体性能产生影响。超时事务属性：事务在强制回滚之前可以保持多久. 这样可以防止长期运行的事务占用资源。超时属性以秒为单位来计算：@Transactional(timeout=10)	

	- 如果一个事物只读取数据但不做修改，数据库引擎可以对这个事务进行优化，只读事务属性：表示这个事务只读取数据但不更新数据，这样可以帮助数据库引擎优化事务：@Transactional(readOnly=false)；

- 6.6、脏读：一个事务读取了另一个事务改写但还未提交的数据，如果这些数据被回滚，则读到的数据是无效的.

	不可重复读：在同一个事务中，多次读取同一数据返回的结果有所不同;

	虚读(幻读)：一个事务读取了几行记录后，另一个事务插入一些记录，幻读就发生了，在后来的查询中，第一个事务就会发现有些原来没有的记录

- 6.7、事务的状态：

	调用 PlatformTransactionManager 接口的getTransaction()的方法得到的是 TransactionStatus 接口的一个实现；这个接口描述的是一些处理事务提供简单的控制事务执行和查询事务状态的方法
	```java
	public interface TransactionStatus{
		boolean isNewTransaction(); // 是否是新的事物
		boolean hasSavepoint(); // 是否有恢复点
		void setRollbackOnly();  // 设置为只回滚
		boolean isRollbackOnly(); // 是否为只回滚
		boolean isCompleted; // 是否已完成
	}
	```
## 7、xml方式配置Spring事务：使用AOP来实现的，即使用动态代理
详细步骤

- 配置事务管理器，包括配置hibernate、mybatis等：
	```xml
	<bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSource"></property>
	</bean>
	```
- 配置事务管理器的相关属性，如事务传播性、事务隔离、只读、超时、回滚等：
	```xml
	<tx：advice id="txAdvice" transaction-manager="transactionManager">
		<tx：attributes>
			<!-- 根据方法名指定事务的属性 "*" 表示通配符-->
			<tx：method name="purchase" propagation="REQUIRES_NEW"/>
			<tx：method name="get*" read-only="true"/>
			<tx：method name="find*" read-only="true"/>
			<tx：method name="*"/>
		</tx：attributes>
	</tx：advice>
	```
- 配置事务切入点，以及把事务切入点和事务属性关联起来
	```xml
	<aop：config>
		<aop：pointcut expression="execution(* *.*(..))" id="txPointCut"/>
		<aop：advisor advice-ref="txAdvice" pointcut-ref="txPointCut"/>	
	</aop：config>
	```
## 8、编程式事务管理

## 9、Spring 中的部分注解配置	

从JDK5.0开始，提供了注解，泛型，新for循环，自动装箱，拆箱；目前框架利用注解替代XML配置内容，注解是一种标记(@标记)，可以写在类定义前，方法定义前，属性变量定义前

- 组件自动扫描技术：

	可以指定一个包路径，Spring会自动扫描该包及其子包下所有的Class组件，当发现class有指定的注解标记，会转化成原XML配置中的bean定义;使用方法：
	```
	---在Spring的主配置中开启组件自动扫描：
		<context：component-scan base-package="包路径" />
	---在需要扫描进入Spring容器的Class中，在类定义前使用下面注解标记之一：
		@Controller 	：	Action组件
		@Service		：	业务组件
		@Repository ：DAO组件
		@Component ：其他组件
		
		◆示例：@Repository("jdbcCostDao")-----引号里内容表示：Bean组件的id
		
	---如果需要注入Bean对象，在属性变量或者set方法前使用下面标记：注入注解标记
		@Resource，默认按照名称注入，找不到对于名称时按类型装配；
			----@Resource：注入注解标记，按类型
			----@Resource(name="指定的Bean的Id")：注入指定的Bean对象
		@Autowired：按类型注入
			---如果使用@Autowired标记需要注入指定的Bean对象需要按照如下格式写：
				@Autowired(required=true)：
				@Qualifier("指定的Bean")
	```
- AOP注解配置：
	使用方法如下：
	```
	---在Spring的主配置中开启AOP注解配置：
		<aop：aspectj-autoproxy />
	---编写方面组件，在组件中使用下列注解标记：
		@Component：将Bean扫描到Spring容器;
		@Aspect：将Bean指定为方面组件;
		通知标记：
			--@Before ：前置通知
			--@After	：最终通知
			--@AfterReurning 	：后置通知
			--@AfterThrowing	：异常通知
			--@Around	：环绕通知
	可以加注解标记：@Scope()-----表示是否为单例	
	```
	
# 六、Spring的三种配置方式

## 1、基于xml方式配置


## 2、基于注解方式配置



## 3、基于Java方式配置	

# 七、Spring中涉及的设计模式	

- BeanFactory 和 ApplicationContext 使用了工厂模式
- 在Bean的创建中，Spring为不同的scope定义的对象提供了单例、原型等模式的实现；
- AOP领域则使用了代理模式、装饰器模式、适配器模式
- 各种事件监听器，是观察者模式的典型应用；
- 类似JdbcTemplate则应用了模板模式


# 参考资料

* [Spring框架的设计理念与设计模式分析](https：//www.ibm.com/developerworks/cn/java/j-lo-spring-principle/)
* [Spring中涉及的设计模式](https://mp.weixin.qq.com/s/Hy-qxNT0nJzcAkanbH93eA)


