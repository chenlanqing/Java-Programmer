1.Object 有哪些公用方法?[../Java/Object类.java]
2.clone 方法如何使用?[../Java/Object类.java]
3.反射机制与代理,反射实现泛型数组的初始化;
4.反射中,Class.forName 和 Classloader.loadClass()的区别?
	Class 的装载分了三个阶段:加载,链接,初始化
	(1).Class.forName(string)实际上是调用的是 ClassLoader.loadClass(name, false),
		第二个参数指出Class是否被link
	(2).ClassLoader.loadClass(className)实际上调用的是 ClassLoader.loadClass(name, false),
		第二个参数指出Class是否被link
	区别:
	(1).Class.forName(className)装载的class已经被初始化，而ClassLoader.loadClass(className)
		装载的class还没有被link
	(2).但如果程序依赖于Class是否被初始化，就必须用Class.forName(name)了
5.编程思想:POP,OOP,SOA,AOP
	(1).POP:面向过程编程(Process Oriented Programming):
	(2).OOP:面向对象编程(Object Oriented Programming):
		面向对象以对象为中心,将对象的内部组织与外部环境区分开来,将表征对象的内部属性数据与外部隔离开来,
		其行为与属性构成一个整体,而系统功能则表现为一系列对象之间的相互作用的序列,能更加形象的模拟或表达现实世界.
		针对业务处理过程的实体及其属性和行为进行抽象封装，以获得更加清晰高效的逻辑单元划分
	(3).SOA:面向服务架构
	(4).AOP:面向方面编程(Aspect Oriented Programming)
		是对业务逻辑又进行了进一步的抽取,将多种业务逻辑中的公用部分抽取出来做成一种服务,从而实现代码复用.另外这种
		服务通过配置可以动态的给程序添加统一控制,利用AOP可以对业务逻辑的各个部分进行分离,
		从而使得业务逻辑各部分之间的耦合度降低
6.hashCode的作用? hashcode 有哪些算法
7.动态代理的原理?JDK 实现和 CGLib 实现的区别?
	http://blog.csdn.net/luanlouis/article/details/24589193
	/Java知识点/Java/Java设计模式/Java-代理模式.java
	7.1.代理:是一种常用的设计模式.其目的就是为其他对象提供一个代理以控制对某个对象的访问.
		代理类负责为委托类预处理消息,过滤消息并转发消息,以及进行消息被委托类执行后的后续处理.
		为了保持行为的一致性,代理类和委托类通常会实现相同的接口;
	7.2.静态代理:
		 缺点:
		 静态的代理模式固然在访问无法访问的资源,增强现有的接口业务功能方面有很大的优点,但是大量使用这种静态代理,
		 会使我们系统内的类的规模增大,并且不易维护;并且由于Proxy和RealSubject的功能 本质上是相同的,
		 Proxy 只是起到了中介的作用,这种代理在系统中的存在,导致系统结构比较臃肿和松散
	7.3.动态代理相关类和接口:
		(1).java.lang.reflect.Proxy:是 Java 动态代理机制的主类,它提供了一组静态方法来为一组接口动态地生成代理类及其对象
		(2).java.lang.reflect.InvocationHandler:这是调用处理器接口,它自定义了一个 invoke 方法,
			A.用于集中处理在动态代理类对象上的方法调用,通常在该方法中实现对委托类的代理访问.
			每次生成动态代理类对象时都需要指定一个实现了该接口的调用处理器对象.
			B.在调用某个方法前及方法后做一些额外的业务.
			可以将所有的触发真实角色动作交给一个触发的管理器,这种管理器就是 InvocationHandler
			C.动态代理工作的基本模式就是将自己的方法功能的实现交给 InvocationHandler角色,外界对Proxy角色中的每一个方法的调用,
			Proxy 角色都会交给 InvocationHandler 来处理,而 InvocationHandler 则调用具体对象角色的方法
		(3).java.lang.ClassLoader:类装载器类,负责将类的字节码装载到 Java 虚拟机(JVM)中并为其定义类对象,然后该类才能被使用.
	7.4.JDK 动态代理实现步骤:
		(1).通过实现 InvocationHandler 接口创建自己的调用处理器;
		(2).通过为 Proxy 类指定 ClassLoader 对象和一组接口来创建动态代理类;
		(3).通过反射机制获得动态代理类的构造函数,其唯一参数类型是调用处理器接口类型;
		(4).通过构造函数创建动态代理类实例,构造时调用处理器对象作为参数被传入
			// InvocationHandlerImpl 实现了 InvocationHandler 接口，并能实现方法调用从代理类到委托类的分派转发
			InvocationHandler handler = new InvocationHandlerImpl(..); 
			// 通过 Proxy 直接创建动态代理类实例
			Interface proxy = (Interface)Proxy.newProxyInstance(classLoader, new Class[] { Interface.class }, handler );
		==> JDK 动态代理:某个类必须有实现的接口,而生成的代理类也只能代理某个类接口定义的方法
		==> 如果某个类没有实现接口,那么这个类就不能同JDK产生动态代理了
	7.5.CGLib 动态代理:
		7.5.1.cglib 创建某个类 A 动态代理类的模式:
			(1).查找A上的所有非 final 的 public 类型的方法定义;
			(2).将这些方法的定义转换成字节码;
			(3).将组成的字节码转换成相应的代理的class对象;
			(4).实现 MethodInterceptor 接口,用来处理对代理类上所有方法的请求(这个接口和JDK动态代理 InvocationHandler
				的功能和角色是一样的)
		7.5.2.
8.构造方法(c++与java之间的区别)
9.接口与抽象类的区别
10.在匿名内部类使用局部变量为什么要加上 final 修饰?
	参考:/Java知识点/Java/Java细节知识点.java --方法内部类
11.static 和 final 区别:
  11.1.static:
    (1).修饰变量:静态变量是随着类加载时完成初始化,内存中只有一个,且JVM只会为它分配一次内存,所有类共享静态变量;
    (2).修饰方法:在类加载的时候就存在,不依赖任何实例;static 方法必须实现,不能使用 abstract 修饰;
    (3).静态代码块:在类加载完之后就会执行代码块的内容
    (4).静态代码块执行顺序:
      父类静态代码块->子类静态代码块->父类非静态代码块->父类构造方法->子类非静态代码块->子类构造方法;
    (5).静态导包:import static com.className.*;
      是 JDK1.5 的新特性,意思是导入这个类的静态方法.当然也可以只导入某个静态方法,只需要将后面的通配符*换成
      静态方法名即可.在该类就可以直接使用方法名调用静态方法,不需要在使用className.方法名的方式来调用了
      ==> 注意:提防含糊不清的命名static成员.
        例如,如果你对Integer类和Long类执行了静态导入,引用MAX_VALUE将导致一个编译器错误,因为Integer和Long
        都有一个MAX_VALUE常量,并且Java不会知道你在引用哪个MAX_VALUE
  11.2.final: