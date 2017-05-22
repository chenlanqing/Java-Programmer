一.J2EE
1.Cookie 和 Session 区别: https://my.oschina.net/kevinair/blog/192829
	Session 与 Cookie 的作用都是为了保持访问用户与后端服务器的交互状态
	1.1.Cookie:是服务器在本地机器上存储的小段文本并随每一个请求发送至同一个服务器,网络服务器用HTTP头向客户端
		发送cookies,在客户终端,浏览器解析这些cookies并将它们保存为一个本地文件.
		(1).c当一个用户通过 HTTP 协议访问一个服务器的时候,这个服务器会将一些 Key/Value 键值对返回给客户端浏览器,
			并给这些数据加上一些限制条件,在条件符合时这个用户下次访问这个服务器的时候,数据又被完整地带回给服务器;
		(2).cookie的内容主要包括:名字、值、过期时间、路径和域.路径与域一起构成cookie的作用范围.
			若不设置过期时间,则表示这个cookie的生命期为浏览器会话期间,关闭浏览器窗口,cookie就消失.
			这种生命期为浏览器会话期的cookie被称为会话cookie.会话cookie一般是存储在内存中的;
		(3).若设置了过期时间,浏览器就会把cookie保存到硬盘上,关闭后再次打开浏览器,
			这些cookie仍然有效直到超过设定的过期时间,存储在硬盘上的cookie可以在不同的浏览器进程间共享
	1.2.Session:session机制采用的是一种在服务器端保持状态的解决方案.由于采用服务器端保持状态的方案在客户端也需要
		保存一个标识,所以session机制可能需要借助于cookie机制来达到保存标识的目的.
		(1).session是针对每一个用户的,变量的值保存在服务器上,用一个sessionID来区分是哪个用户session变量,这个值是
			通过用户的浏览器在访问的时候返回给服务器,当客户禁用cookie时,这个值也可能设置为由get来返回给服务器.
		(2).就安全性来说,服务器端的session机制更安全些,因为它不会任意读取客户存储的信息.
		(3).当程序需要为某个客户端的请求创建一个session时,服务器首先检查这个客户端的请求里是否已包含了一个
			session标识(称为sessionId),如果已包含则说明以前已经为此客户端创建过session,服务器就按照sessionId
			把这个session检索出来使用(检索不到,会新建一个),如果客户端请求不包含session id,则为此客户端创建一个
			session并且生成一个与此session相关联的session id，session id的值应该是一个既不会重复，又不容易被找
			到规律以仿造的字符串,这个session id将被在本次响应中返回给客户端保存
			保存这个session id的方式可以采用cookie，这样在交互过程中浏览器可以自动的按照规则把这个标识发挥给服务器
	1.3.Cookie 与 Session 都能够进行会话跟踪,但是完成的原理不太一样:
		(1).存取方式不同:
			Cookie 中只能保管ASCII字符串,假如需求存取Unicode字符或者二进制数据,需求先进行编码;也不能直接存储Java对象;
			Session 中能够存取任何类型的数据,包括而不限于 String、Integer、List、Map 等;
			单个cookie保存的数据不能超过4K,很多浏览器都限制一个站点最多保存20个cookie
		(2).隐私策略的不同:
			Cookie 存储在客户端阅读器中,对客户端是可见的,客户端的一些程序可能会窥探、复制以至修正Cookie中的内容;
			Session 存储在服务器上,对客户端是透明的,不存在敏感信息泄露的风险;
		(3).有效期上的不同:
			Cookie 设置过期时间可以很大,保证长期有效.
			Session 依赖于名为JSESSIONID的Cookie,而Cookie JSESSIONID 的过期时间默许为–1,只需关闭了阅读器该Session就会失效
		(4).服务器压力的不同:
			Cookie 保管在客户端,不占用服务器资源,假如并发阅读的用户十分多,Cookie 是很好的选择
			Session 是保管在服务器端的,每个用户都会产生一个Session.假如并发访问的用户十分多,会产生十分多的Session,耗费大量的内存
		(5).浏览器支持的不同:
			Cookie 是需要客户端浏览器支持的.假如客户端禁用了Cookie,或者不支持Cookie,则会话跟踪会失效;
				假如客户端浏览器不支持Cookie，需要运用Session以及URL地址重写
		(6).跨域支持上的不同:
			Cookie 支持跨域名访问
			Session 则不会支持跨域名访问.Session 仅在他所在的域名内有效
2.掌握JSP内置对象、动作及相关特点和工作原理
	2.1.JSP 预先定义了9个这样的对象分别为:request、response、session、application、out、pagecontext、config、page、exception
		(1).request:是 javax.servlet.HttpServletRequest 类型的对象.该对象代表了客户端的请求信息,主要用于接受通过HTTP协议
			传送到服务器的数据(包括头信息,系统信息,请求方式及请求参数等).request对象的作用域为一次请求.
		(2).response:代表的是客户端的响应,主要是将JSP容器处理过的对象传回到客户端,response对象作用域只在JSP页面内有效.
		(3).session:是由服务器自动创建的与用户请求相关的对象.
			服务器为每个用户都生成一个session对象,用于保存该用户的信息,跟踪用户的操作状态.
			session对象内部使用Map类来保存数据,因此保存数据的格式为 "Key/value";
		(4).application:可将信息保存在服务器中,直到服务器关闭,否则application对象中保存的信息会在整个应用中都有效.
			与session对象相比,application对象生命周期更长,类似于系统的"全局变量";
		(5).out:用于在Web浏览器内输出信息,并且管理应用服务器上的输出缓冲区.
		(6).pageContext:是取得任何范围的参数,通过它可以获取 JSP 页面的 out、request、reponse、session、application 等对象.
			pageContext对象的创建和初始化都是由容器来完成的,在JSP页面中可以直接使用 pageContext对象.
		(7).config:主要作用是取得服务器的配置信息.通过 pageConext对象的 getServletConfig() 方法可以获取一个config对象.
			当一个Servlet 初始化时,容器把某些信息通过 config对象传递给这个 Servlet.
		(8).page:代表JSP本身,只有在JSP页面内才是合法的.page隐含对象本质上包含当前Servlet接口引用的变量,类似于Java编程中的 this 指针.
		(9).exception:exception 对象的作用是显示异常信息,只有在包含 isErrorPage="true" 的页面中才可以被使用,
			在一般的JSP页面中使用该对象将无法编译JSP文件
	2.2.四大作用域:
		(1).page:当前页面有效,有 out, response,pageContext,config,papge,exception
		(2).request:当前请求有效,有 request,
		(3).session:当前会话有效,有 session
		(4).application:所有进程中有效,有 application
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