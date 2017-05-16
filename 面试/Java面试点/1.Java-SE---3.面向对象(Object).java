1.Object 有哪些公用方法?[../Java/Object类.java]
2.clone 方法如何使用?[../Java/Object类.java]
3.反射机制与代理,反射实现泛型数组的初始化;
4.反射中,Class.forName 和 Classloader.loadClass()的区别?
	Class的装载分了三个阶段:加载,链接,初始化
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
7.动态代理的原理,JDK 实现和 CGLib 实现的区别?
8.构造方法(c++与java之间的区别)
9.接口与抽象类的区别
10.在匿名内部类使用局部变量为什么要加上 final 修饰?

