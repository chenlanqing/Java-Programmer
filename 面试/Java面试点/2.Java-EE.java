一.Web
1.Cookie 和 Session 区别
	https://my.oschina.net/kevinair/blog/192829
	1.1.Cookie:


2.掌握JSP内置对象、动作及相关特点和工作原理


3.掌握Servlet的特点和工作原理

4.掌握AJAX的工作原理

5.Tomcat 服务器:架构,原理,调优等

6.Nginx
二.Spring/SpringMVC
1.为什么选择使用 Spring 框架
	(1).Spring 采用的是层次结构,有超过20个模块可以选择.可以自由取舍.
		Spring 通过简单Java对象(Plain Old Java Object, POJO)编程简化了J2EE
		DAO、ORM、JEE、WEB、AOP、CORE
	(2).Spring 框架的核心功能是依赖注入.DI 使得代码的单元测试更加方便、系统更好维护、代码也更加灵活.
	(3).Spring 支持面向切面编程(Aspect Oriented Programming ,AOP),允许通过分离应用业务逻辑和系统服务
		从而进行内聚性的开发;
	(4).Spring 还提供了许多实现基本功能的模板类,使得J2EE开发更加容易.例如:JdbcTemplate类 和 JDBC、
		JpaTemplate 类和 JPA, JmsTemplate 类和 JMS 都可以很好地结合起来使用.
	(5).尽量把中间层代码从业务逻辑中剥离出来是很重要的
	(6).Spring 提供了声明性事务处理,工作调度,身份认证,成熟的 MVC web框架以及和其他框架的集成;
	(7).Spring bean对象可以通过Terracotta在不同的JVM之间共享.这就允许使用已有的bean并在集群中共享 ,
		将Spring应用上下文事件变为分布式事件
	(8).Spring 倾向于使用未检查异常(unchecked exceptions)和减少不当 try,catch 和 finally 代码块(或者
		finally 中的 try/catch 块)
	(9).在非 Spring 或者 Guice 这种DI框架中,工厂模式和单例模式可以用来提高代码的松耦合度;
2.Spring 有缺陷吗?有哪些缺陷?
	(1).Spring 变得过于庞大和笨重.可以选择适合自己项目的功能;
	(2).XML 文件过于臃肿庞大,这一点可以通过其他手段来得到改善.
3.Spring AOP
4.Spring AOP 如何配置? Spring 装配 Bean 的方式
5.如何编写 AOP
6.Spring IOC(控制反转)-DI()
	6.1.DIP(依赖倒置原则):
		高层模块不应当依赖低层模块,它们都应当依赖抽象.
		抽象不应该依赖具体实现.具体实现应该依赖抽象;
	6.2.依赖注入:在运行时将类的依赖注入到代码中.
		通过将依赖定义为接口,并将实现这个接口的实体类注入到主类的构造器中来实现这个模式;
	6.3.IOC:一个支持依赖注入的容器,在Spring中是对应的Spring的容器
		IOC 主要可以实现松耦合;
		IOC 的基本概念是,不去实际生成对象,而是去定义如何生成对象;
		IOC 就是关于一个对象如何获得其协作对象的引用的一种责任反转机制,通过Java反射来实现
	6.4.DI 和 IOC 都是在运行时而非编译时绑定类之间的关系.
7.依赖注入有哪些方式?
	(1).构造子注入:依赖是通过构造器参数提供的。
	(2).设值方法注入:依赖是通过JavaBeans属性注入的（ex：setter方法）
	(3).接口注入:注入通过接口完成
8.Spring Bean:构成Spring应用核心的Java对象.这些对象由 Spring IOC 容器实例化、组装、管理
	8.1.Spring Bean 支持的作用域:
		(1).singleton:在Spring IOC 容器中只存在一个实例,Bean 以单例形式存在.
		(2).prototype:一个Bean 可以定义多个实例
		(3).request:每次HTTP请求都会创建一个新的Bean,该作用域只适用于 WebApplicationContext 环境
		(4).session:一个HTTP Session 定义一个Bean,该作用域只适用于 WebApplicationContext 环境
		(5).globalSession:同一个全局 HTTP Session 定义一个Bean.该作用域同样仅适用于 WebApplicationContext 环境;
		==> bean 的默认作用域为 singleton,且 Spring 框架中单例Bean不是线程安全的.
	8.2.Spring Bean 的声明周期:
		(1).Spring 容器读取XML文件中bean的定义并实例化bean;
		(2).Spring 根据bean的定义设置属性值;
		(3).如果该Bean实现了BeanNameAware接口,Spring 将bean的id传递给setBeanName()方法;
		(4).如果该Bean实现了BeanFactoryAware接口,Spring 将beanfactory传递给setBeanFactory()方法;
		(5).如果任何 bean BeanPostProcessors 和该bean相关,Spring 调用postProcessBeforeInitialization()方法;
		(6).如果该Bean实现了InitializingBean接口,调用Bean中的afterPropertiesSet方法,
			如果bean有初始化函数声明,调用相应的初始化方法;
		(7).如果任何bean BeanPostProcessors 和该bean相关,调用postProcessAfterInitialization()方法;
		(8).如果该bean实现了DisposableBean,调用destroy()方法.
		bean标签有两个重要的属性(init-method 和 destroy-method),你可以通过这两个属性定义自己的初始化方法和
		析构方法.Spring 也有相应的注解:@PostConstruct 和 @PreDestroy
	8.3.自动装配的各种模式:共5种
		(1).no:默认的方式是不进行自动装配,通过手工设置ref 属性来进行装配bean;
		(2).byName:通过参数名自动装配,Spring 容器查找beans的属性,这些beans在XML配置文件中被设置为byName.
			之后容器试图匹配、装配和该bean的属性具有相同名字的bean。
		(3).byType:通过参数的数据类型自动自动装配，Spring容器查找beans的属性，这些beans在XML配置文件中被设置
			为byType.之后容器试图匹配和装配和该bean的属性类型一样的bean.如果有多个bean符合条件,则抛出错误.	
		(4).constructor:这个同byType类似.不过是应用于构造函数的参数.如果在BeanFactory中不是恰好有一个bean
		与构造函数参数相同类型，则抛出一个严重的错误。
		(5).autodetect:如果有默认的构造方法,通过 construct的方式自动装配,否则使用 byType的方式自动装配


9.SpringMVC 请求过程
10.MVC 理解.

三.Mybatis
1.Hibernate 与 Mybatis 区别