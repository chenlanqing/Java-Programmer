<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、Spring概念](#%E4%B8%80spring%E6%A6%82%E5%BF%B5)
  - [1、Spring是什么](#1spring%E6%98%AF%E4%BB%80%E4%B9%88)
  - [2、具体描述 Spring](#2%E5%85%B7%E4%BD%93%E6%8F%8F%E8%BF%B0-spring)
- [二、IoC(DI)](#%E4%BA%8Ciocdi)
  - [1、IoC(Inversion of Control)](#1iocinversion-of-control)
  - [2、Spring容器](#2spring%E5%AE%B9%E5%99%A8)
  - [3、DI(Dependency Injection)](#3didependency-injection)
  - [4、各种类型信息的注入](#4%E5%90%84%E7%A7%8D%E7%B1%BB%E5%9E%8B%E4%BF%A1%E6%81%AF%E7%9A%84%E6%B3%A8%E5%85%A5)
- [三、Spring AOP](#%E4%B8%89spring-aop)
  - [1、关于面向切面编程](#1%E5%85%B3%E4%BA%8E%E9%9D%A2%E5%90%91%E5%88%87%E9%9D%A2%E7%BC%96%E7%A8%8B)
  - [2、AOP 的优势](#2aop-%E7%9A%84%E4%BC%98%E5%8A%BF)
  - [3、AOP 的术语](#3aop-%E7%9A%84%E6%9C%AF%E8%AF%AD)
  - [6、Aop：通知](#6aop%E9%80%9A%E7%9F%A5)
- [四、IoC与AOP原理分析](#%E5%9B%9Bioc%E4%B8%8Eaop%E5%8E%9F%E7%90%86%E5%88%86%E6%9E%90)
  - [1、IoC原理](#1ioc%E5%8E%9F%E7%90%86)
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
- Spring 是一个 IoC(DI) 和 AOP 容器框架;

## 2、具体描述 Spring

- 轻量级：Spring 是非侵入性的 - 基于 Spring 开发的应用中的对象可以不依赖于 Spring 的 API
- 依赖注入(DI --- dependency injection、IoC)
- 面向切面编程(AOP --- aspect oriented programming)
- 容器：Spring 是一个容器，因为它包含并且管理应用对象的生命周期
- 框架：Spring 实现了使用简单的组件配置组合成一个复杂的应用. 在 Spring 中可以使用 XML 和 Java 注解组合这些对象
- 一站式：在 IoC 和 AOP 的基础上可以整合各种企业应用的开源框架和优秀的第三方类库 (实际上 Spring 自身也提供了展现层的 SpringMVC 和 持久层的 Spring JDBC)

Spring容器初始化时：首先会初始化 bean，即构造相关类

## 3、Spring的模块结构

![](image/SpringFramework.png)

### 3.1、Spring 核心容器

对应图上的Core Container，该层基本上是 Spring Framework 的核心。它包含以下模块：
- Spring Core
- Spring Bean：核心容器提供 Spring 框架的基本功能。核心容器的主要组件是 BeanFactory，它是工厂模式的实现。BeanFactory 使用控制反转 （IoC）模式将应用程序的配置和依赖性规范与实际的应用程序代码分开
- Spring Context：Spring 上下文是一个配置文件，向 Spring 框架提供上下文信息。Spring 上下文包括企业服务，例如 JNDI、EJB、电子邮件、国际化、事件机制、校验和调度功能
- SpEL (Spring Expression Language)：Spring 表达式语言全称为 “Spring Expression Language”，缩写为 “SpEL” ，类似于 Struts2 中使用的 OGNL 表达式语言，能在运行时构建复杂表达式、存取对象图属性、对象方法调用等等，并且能与 Spring 功能完美整合，如能用来配置 Bean 定义

或者说这块就是IoC

### 3.2、数据访问

对应图中，Data Access；该层提供与数据库交互的支持。它包含以下模块：
- JDBC (Java DataBase Connectivity)：Spring 对 JDBC 的封装模块，提供了对关系数据库的访问。
- ORM (Object Relational Mapping)：Spring ORM 模块，提供了对 hibernate5 和 JPA 的集成
- OXM (Object XML Mappers)：Spring 提供了一套类似 ORM 的映射机制，用来将 Java 对象和 XML 文件进行映射。这就是 Spring 的对象 XML 映射功能，有时候也成为 XML 的序列化和反序列化；
- Transaction：Spring 简单而强大的事务管理功能，包括声明式事务和编程式事务。

### 3.3、Web

该层提供了创建 Web 应用程序的支持。它包含以下模块：

- WebMVC：MVC 框架是一个全功能的构建 Web 应用程序的 MVC 实现。通过策略接口，MVC 框架变成为高度可配置的，MVC 容纳了大量视图技术，其中包括 JSP、Velocity、Tiles、iText 和 POI
- WebFlux：基于 Reactive 库的响应式的 Web 开发框架；
- WebSocket：Spring 4.0 的一个最大更新是增加了对 Websocket 的支持。Websocket 提供了一个在 Web 应用中实现高效、双向通讯，需考虑客户端(浏览器)和服务端之间高频和低延时消息交换的机制。一般的应用场景有：在线交易、网页聊天、游戏、协作、数据可视化等

### 3.4、AOP

该层支持面向切面编程。它包含以下模块：

- AOP：通过配置管理特性，Spring AOP 模块直接将面向方面的编程功能集成到了 Spring 框架中。所以，可以很容易地使 Spring 框架管理的任何对象支持 AOP。Spring AOP 模块为基于 Spring 的应用程序中的对象提供了事务管理服务。通过使用 Spring AOP，不用依赖 EJB 组件，就可以将声明性事务管理集成到应用程序中；
- Aspects：该模块为与 AspectJ 的集成提供支持；
- Instrumentation：该层为类检测和类加载器实现提供支持

### 3.5、其它

- JMS (Java Messaging Service)：提供了一个 JMS 集成框架，简化了 JMS API 的使用。
- Test：该模块为使用 JUnit 和 TestNG 进行测试提供支持；
- Messaging：该模块为 STOMP 提供支持。它还支持注解编程模型，该模型用于从 WebSocket 客户端路由和处理 STOMP 消息

# 二、IoC(DI)

## 1、IoC(Inversion of Control)

其思想是反转资源获取的方向。传统的资源查找方式要求组件向容器发起请求查找资源。作为回应，容器适时的返回资源。而应用了 IoC 之后，则是容器主动地将资源推送给它所管理的组件，组件所要做的仅是选择一种合适的方式来接受资源。这种行为也被称为查找的被动形式

控制反转：把创建对象(Bean)和维护对象(Bean)的关系的权利从程序中转移到Spring容器中，程序不再控制

- IoC 机制实现Bean之间的调用;
- IoC 解决的问题：可以降低两个组件对象之间的关联，降低耦合度;
- Spring IoC 控制反转，哪些方面的控制被反转了呢？获得依赖对象的过程被反转了，由主动获取变为被动获取
- 使用 IoC 的优势：
	- 维护性比较好，非常便于进行单元测试，便于调试程序和诊断故障。代码中的每一个Class都可以单独测试，彼此之间互不影响，只要保证自身的功能无误即可，这就是组件之间低耦合或者无耦合带来的好处;
	- 开发团队的成员都只需要关心实现自身的业务逻辑，完全不用去关心其它的人工作进展，因为你的任务跟别人没有任何关系，你的任务可以单独测试，你的任务也不用依赖于别人的组件
	- 可复用性好，我们可以把具有普遍性的常用组件独立出来，反复利用到项目中的其它部分，或者是其它项目，当然这也是面向对象的基本特征，IoC 不仅更好地贯彻了这个原则，提高了模块的可复用性。符合接口标准的实现，都可以插接到支持此标准的模块中;
	- 完全具有热插拨的特性，IoC 生成对象的方式转为外置方式，也就是把对象生成放在配置文件里进行定义；

- IoC 使用的技术：最基本的技术就是反射，设计模式是工厂模式

	IoC 容器的工作模式可以看成是工厂模式的升华；IoC 容器看作是一个工厂，这个工厂里要生产的对象都在配置文件中给出定义，然后利用编程语言的的反射编程，根据配置文件中给出的类名生成相应的对象。从实现来看，IoC 是把以前在工厂方法里写死的对象生成代码，改变为由配置文件来定义，也就是把工厂和对象生成这两者独立分隔开来，目的就是提高灵活性和可维护性；

	***为什么不使用工厂模式，而使用IoC？*** 

	IoC是通过反射机制来实现的。当我们的需求出现变动时，工厂模式会需要进行相应的变化。但是IoC的反射机制允许我们不重新编译代码，因为它的对象都是动态生成的

- Spring 中的 IoC
	- Spring 中的 `org.springframework.beans` 包和 `org.springframework.context`包构成了Spring框架IoC容器的基础
	- BeanFactory 接口提供了一个先进的配置机制，使得任何类型的对象的配置成为可能：`ApplicationContex` 接口对 `BeanFactory`（是一个子接口）进行了扩展，在`BeanFactory`的基础上添加了其他功能，比如与Spring的AOP更容易集成，也提供了处理message resource的机制(用于国际化)、事件传播以及应用层的特别配置，比如针对Web应用的`WebApplicationContext`
	- `org.springframework.beans.factory.BeanFactory` 是 Spring IoC 容器的具体实现，用来包装和管理前面提到的各种bean。BeanFactory接口是Spring IoC 容器的核心接口

## 2、Spring容器

在 Spring IoC 容器读取 Bean 配置创建 Bean 实例之前，必须对它进行实例化. 只有在容器实例化后，才可以从 IoC 容器里获取 Bean 实例并使用

Spring 提供了两种类型的 IoC 容器实现：（1）BeanFactory：IoC 容器的基本实现；（2）ApplicationContext：提供了更多的高级特性。是BeanFactory的子接口.

![](image/SpringIOC容器层级关系.png)

- BeanFactory是Spring框架的基础设施，面向Spring本身；ApplicationContext，面向使用Spring框架的开发者，几乎所有的应用场合都直接使用 ApplicationContext;而非底层的 BeanFactory；但是无论使用何种方式，配置文件时相同的。常用的BeanFactory容器是`XmlBeanFactory`，它可以根据 XML 文件中定义的内容，创建相应的 Bean；BeanFactory是IOC容器的核心接口，它的职责包括：实例化、定位、配置应用程序中的对象及建立这些对象间的依赖

- ApplicationContext 的主要实现类：【 ApplicationContext 在初始化上下文时就实例化所有单例的 Bean】
	- ①、ClassPathXmlApplicationContext：从类路径下加载配置文件;
	- ②、FileSystemXmlApplicationContext：从文件系统中加载配置文件;
	- ③、WebApplicationContext：是专门为 WEB 应用而准备的，它允许从相对于 WEB 根目录的路径中完成初始化工作;

- Spring容器对Bean的管理：
	- 控制Bean对象创建模式：在bean元素中，利用scope属性可以指定Bean组件创建对象的方式：
		- prototype：非单例模式
		- singleton：单例模式(默认是单例模式)，Spring不关心bean是否线程安全，当然，但实际上，大部分的 Spring Bean 并没有可变的状态(比如Serview 类和 DAO 类)，所以在某种程度上说 Spring 的单例 Bean 是线程安全的

		在web程序中，通过一些配置，可以扩展出request，session等属性值;

	- 可以控制单例模式的创建时机：
		- singleton模式的Bean组件，默认是在 ApplicationContext 容器实例化时就创建了组件;可以在bean元素中追加属性lazy-init="true"，将singleton模式创建对象推迟到getBean()方法
		- prototype模式是在调用getBean()方法时创建了组件;
		
	- 可以指定Bean对象初始化和销毁方法：`<bean init-method="初始化方法" destroy-method="销毁方法">`
		- Spring 将 Bean 对象创建完毕后，会自动调用init-method里指定的方法
		- destroy-method指定的方法需满足下面条件才能执行：
			- scope="singleton";才能使用;
			- 执行AbstractApplicationContext容器的close()方法触发;
- BeanFactory 和 ApplicationContext 区别：
	- ①、BeanFactory 可以理解为含有bean集合的工厂类，包含了各种bean的定义，以便在接收请求时对应的Bean实例化BeanFactory 包含了bean生命周期的控制
	- ②、application context如同bean factory一样具有bean定义、bean关联关系的设置，根据请求分发bean的功能。但application context在此基础上还提供了其他的功能。
		- 提供了支持国际化的文本消息
		- 统一的资源文件读取方式
		- 已在监听器中注册的bean的事件
	- 详细比较：

	|BeanFactory	| ApplicationContext|
	|------------|-------------------|
	|它使用懒加载  |	它使用即时加载|
	|它使用语法显式提供资源对象 | 它自己创建和管理资源对象|
	|不支持国际化  |	支持国际化|
	|不支持基于依赖的注解 | 支持基于依赖的注解|

- BeanFactory和FactoryBean的区别：
	- BeanFactory是接口，提供了OC容器最基本的形式，给具体的IOC容器的实现提供了规范；
	- FactoryBean也是接口，为IOC容器中Bean的实现提供了更加灵活的方式，FactoryBean在IOC容器的基础上给Bean的实现加上了一个简单工厂模式和装饰模式；
	- BeanFactory是个Factory，也就是IOC容器或对象工厂，FactoryBean是个Bean。在Spring中，所有的Bean都是由BeanFactory(也就是IOC容器)来进行管理的。但对FactoryBean而言，这个Bean不是简单的Bean，而是一个能生产或者修饰对象生成的工厂Bean,它的实现与设计模式中的工厂模式和修饰器模式类似；
	- org.springframework.bean.factory.FactoryBean工厂类接口，用户可以通过实现该接口定制实例化Bean的逻辑

## 3、DI(Dependency Injection)

IoC 的另一种表述方式：即组件以一些预先定义好的方式(例如：setter 方法)接受来自如容器的资源注入。相对于 IoC 而言，这种表述更直接。依赖注入(DI)和控制反转(IoC)是从不同的角度的描述的同一件事情：就是指通过引入IoC容器，利用依赖关系注入的方式，实现对象之间的解耦

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

依赖倒置原则、IOC、DI、IOC容器的关系：

![](image/IOC与DI关系.png)


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
	- 在配置文件中加入IoC容器扫描Bean的配置：
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
	```xml
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

## 8、AOP的实现方式

- 静态代理：指使用 AOP 框架提供的命令进行编译，从而在编译阶段就可生成 AOP 代理类，因此也称为编译时增强；
- 动态代理：在运行时在内存中“临时”生成 AOP 动态代理类，因此也被称为运行时增强。目前 Spring 中使用了两种动态代理库；

        
# 四、IoC与AOP原理分析

## 1、IoC原理

[IOC原理](https://github.com/chenlanqing/learningNote/blob/master/Java/Java%E6%BA%90%E7%A0%81%E8%A7%A3%E8%AF%BB/%E6%A1%86%E6%9E%B6/spring/Spring%E6%BA%90%E7%A0%81.md#%E4%B8%80ioc)

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

***Spring事务默认地只在抛出RuntimeException和Error时才标识事务回滚，从事务方法中抛出的Checked exceptions将不被标识进行事务回滚***

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

- 代理模式：在 AOP 和 remoting 中被用的比较多。`org.springframework.aop.framework.ProxyFactoryBean`该工厂根据Spring bean构建AOP代理
- 单例模式：在 Spring 配置文件中定义的 Bean 默认为单例模式。
- 模板方法：用来解决代码重复的问题。比如 RestTemplate、JmsTemplate、JdbcTemplate.Spring在`org.springframework.context.support.AbstractApplicationContext`类中使用模板方法
- 前端控制器：Spring提供了 DispatcherServlet 来对请求进行分发。
- 视图帮助(View Helper) ：Spring 提供了一系列的 JSP 标签，高效宏来辅助将分散的代码整合在视图里。
- 依赖注入：贯穿于 BeanFactory / ApplicationContext 接口的核心理念。
- 工厂模式：BeanFactory 用来创建对象的实例。通过它的实现，我们可以从Spring的容器访问bean
- 解释器模式：SpEL是一种由Spring的`org.springframework.expression.ExpressionParser`实现分析和执行的语言；这些实现使用作为字符串给出的Spel表达式，并将它们转换为`org.springframework.expression.Expression`的实例。上下文组件由`org.springframework.expression.EvaluationContext`实现表示；
- 建造者模式：BeanDefinitionBuilder，允许我们以编程方式定义bean的类；
- 复合模式：在Spring世界中，我们检索复合对象的概念是`org.springframework.beans.BeanMetadataElement`接口，用于配置bean对象。它是所有继承对象的基本界面

# 八、Spring常见问题

## 1、循环依赖问题
- [Spring循环依赖](https://mp.weixin.qq.com/s/ziSZeWlU5me1WMKvoKobbQ)
- [Spring循环依赖处理](http://cmsblogs.com/?p=2887)

### 1.1、什么是循环依赖

循环依赖，其实就是循环引用，就是两个或者两个以上的 bean 互相引用对方，最终形成一个闭环，如 A 依赖 B，B 依赖 C，C 依赖 A；

循环依赖，其实就是一个死循环的过程，在初始化 A 的时候发现引用了 B，这时就会去初始化 B，然后又发现 B 引用 C，跑去初始化 C，初始化 C 的时候发现引用了 A，则又会去初始化 A，依次循环永不退出，除非有终结条件

### 1.2、循环依赖的场景

- 构造器的循环依赖：Spring是无法解决的，只能抛出`BeanCurrentlyInCreationException`异常表示循环依赖；
- field 属性的循环依赖

Spring只解决`scope=singleton`的循环依赖。对于`scope=prototype`的bean ，Spring 无法解决，直接抛出 BeanCurrentlyInCreationException 异常；

### 1.3、解决循环依赖

- 首先 A 完成初始化第一步并将自己提前曝光出来（通过 ObjectFactory 将自己提前曝光），在初始化的时候，发现自己依赖对象 B，此时就会去尝试 get(B)，这个时候发现 B 还没有被创建出来
- 然后 B 就走创建流程，在 B 初始化的时候，同样发现自己依赖 C，C 也没有被创建出来
- 这个时候 C 又开始初始化进程，但是在初始化的过程中发现自己依赖 A，于是尝试 get(A)，这个时候由于 A 已经添加至缓存中（一般都是添加至三级缓存 singletonFactories ），通过 ObjectFactory 提前曝光，所以可以通过 ObjectFactory#getObject() 方法来拿到 A 对象，C 拿到 A 对象后顺利完成初始化，然后将自己添加到一级缓存中
- 回到 B ，B 也可以拿到 C 对象，完成初始化，A 可以顺利拿到 B 完成初始化。到这里整个链路就已经完成了初始化过程了

## 2、Spring与SpringMVC父子容器配置

### 2.1、Spring父子容器的关系

- `Spring`和`SpringMVC`共存时，会有两个容器：一个`SpringMVC`的`ServletWebApplicationContext`为子容器，一个Spring的`RootWebApplicationContext`为父容器。当子容器中找不到对应的Bean会委托于父容器中的Bean。
	* `RootWebApplicationContext`中的`Bean`对`ServletWebApplicationContext`可见，而`ServletWebApplicationContext`中的`Bean`对`RootWebApplicationContext`不可见。

- 如果在父容器中开启了 `@AspectJ` 注解与事务配置，子容器和父容器均加载了所有Bean。造成子容器中的services覆盖了父容器的Services，导致父容器中的动态代理的services不生效，事务也不生效。

    ![](image/Spring父子容器.png)

### 2.2、如何解决Spring父子容器关系

可以参考[Spring官方文档](https://docs.spring.io/spring/docs/4.3.16.RELEASE/spring-framework-reference/htmlsingle/#mvc-servlet) 中的`Figure 22.2. Typical context hierarchy in Spring Web MVC`

- 子容器包含`Controllers、HandlerMapping、viewResolver`，其他bean都在父容器中；

- 子容器不加载任何bean，均有父容器加载


# 参考资料

* [Spring框架的设计理念与设计模式分析](https：//www.ibm.com/developerworks/cn/java/j-lo-spring-principle/)
* [Spring中涉及的设计模式](https://mp.weixin.qq.com/s/Hy-qxNT0nJzcAkanbH93eA)
* [Spring中的设计模式](http://www.iocoder.cn/Spring/DesignPattern-1/)
* [SpringIOC面试点](https://www.jianshu.com/p/17b66e6390fd)
* [SpringAOP面试点](https://www.jianshu.com/p/e18fd44964eb)
* [AOP理论知识](https://segmentfault.com/a/1190000007469968)

