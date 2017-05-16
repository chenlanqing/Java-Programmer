三.String 
	1.String 的内部实现?char[] 数组和 String 相比,在使用上有什么优势? 
		容量如何扩充?为什么 String 类是 final 的? 使用 String 时需要注意什么?
	2.String & StringBuilder & StringBuffer 区别,适用场景?
	3.String s = "aa"+"bb"; 编译器会做什么优化?
	4.如何理解 String 的不可变? StringBuilder 与 String 性能对比
	5.StringBuffer 的实现方式,容量如何扩充;

四.面向对象与 Object 类
	1.Object 有哪些公用方法?
	2.clone 方法如何使用?
	3.反射机制与代理,反射实现泛型数组的初始化;
	4.反射中，Class.forName 和 Classloader.loadClass()的区别?
		Class的装载分了三个阶段:加载,链接,初始化
		(1).Class.forName(string)实际上是调用的是 ClassLoader.loadClass(name, false),
			第二个参数指出Class是否被link
		(2).ClassLoader.loadClass(className)实际上调用的是 ClassLoader.loadClass(name, false),
			第二个参数指出Class是否被link
		区别:
		(1).Class.forName(className)装载的class已经被初始化，而ClassLoader.loadClass(className)
			装载的class还没有被link
		(2).但如果程序依赖于Class是否被初始化，就必须用Class.forName(name)了
	5.AOP 与 OOP 区别
	6.hashCode的作用?hashcode 有哪些算法
	7.动态代理的原理,JDK 实现和 CGLib 实现的区别?
	8.构造方法(c++与java之间的区别)
五.异常

六.Java IO 与 NIO:
	1.序列化与反序列化:
		1.1.ArrayList 如何实现序列化
			(1).为什么 transient Object[] elementData;
				ArrayList 实际上是动态数组，每次在放满以后自动增长设定的长度值,如果数组自动增长长度
				设为100,而实际只放了一个元素,那就会序列化 99 个 null 元素.为了保证在序列化的时候不
				会将这么多 null 同时进行序列化,	ArrayList 把元素数组设置为 transient
			(2).为什么要写方法:writeObject and readObject
				前面提到为了防止一个包含大量空对象的数组被序列化，为了优化存储，所以，ArrayList 
				使用 transient 来声明elementData
				作为一个集合,在序列化过程中还必须保证其中的元素可以被持久化下来，
				所以,通过重写writeObject 和 readObject方法的方式把其中的元素保留下来
				writeObject方法把elementData数组中的元素遍历的保存到输出流（ObjectOutputStream）中。
				readObject方法从输入流（ObjectInputStream）中读出对象并保存赋值到elementData数组中
		1.2
	2.InputStream、OutputStream、Reader、Writer 的继承体系
	3.IO 框架主要用到什么设计模式
	4.NIO 包有哪些结构?分别起到的作用?
	5.NIO 针对什么情景会比 IO 有更好的优化?为什么使用NIO? NIO 有什么优势?
	6.String 编码UTF-8 和GBK的区别?
	7.什么时候使用字节流、什么时候使用字符流?
七.多线程与并发
	1.创建多线程的方式?创建线程几种方式的不同之处
		继承 Thread
		实现 Runnable 接口
		Callable:实现 Callable 接口,该接口中的call方法可以在线程执行结束时产生一个返回值
	2.sleep()、wait()方法都是暂停线程,有什么区别? wait方法为什么会定义在 Object 中?
		(1).sleep():在指定的毫秒数内让当前"正在执行的线程"休眠(暂停执行),这个"正在执行的线程"是指:
			this.currentThread()返回的线程,这时sleep()是不释放锁的
		(2).为什么要放在 Object 中?
			JAVA 提供的锁是对象级的而不是线程级的,每个对象都有锁,通过线程获得,
			如果线程需要等待某些锁那么调用对象中的 wait()方法就有意义了,
			如果 wait ()方法定义在 Thread 类中,线程正在等待的是哪个锁就不明显了;
			wait,notify和notifyAll 都是锁级别的操作,所以把他们定义在 Object 类中因为锁属于对象
	3.多线程同步的原理
	4.线程同步的实现方式?为什么会出现线程安全问题?
		4.1.同步:synchronized 关键字,volatile 变量,显式锁以及原子变量
	5.如何停止一个线程?Thread.setDeamon()的含义
	6.解释是一下什么是线程安全?举例说明一个线程不安全的例子
		6.1.线程安全性:当多个线程访问某个类时,这个类始终都表现出正确的行为,那么称这个类是线程安全的;
			由于线程访问无状态对象的行为并不会影响其他线程中操作的正确性,因此无状态对象是线程安全的.
			大都数 Servlet 都是无状态的.只有当 Servlet 在处理请求时需要保存一些信息时,线程安全才会称为问题
	7.什么是线程池?如果让你设计一个线程池,如何设计,应该有哪些方法?
	8.volatile 关键字如何保证内存可见性:
	9.synchronized 与 Lock 的区别
	10.synchronized 用在代码块和方法上有什么区别? 底层是如何实现的?
		偏向锁、轻量级锁、自旋锁、重量级锁,锁的膨胀模型,以及锁的优化原理,为什么要这样设计
	11.线程间如何通信?
	12.生产者消费者模式的几种实现
	13.阻塞队列实现
	14.ThreadLocal 的设计理念与作用,ThreadPool 用法与优势
	15.Executors 创建的三种(JAVA8增加了一种，共四种)线程池的特点及适用范围
	16.Concurrent 包,java.util.concurrent 包下用过哪些类
	17.乐观锁与悲观锁:乐观锁的设计要点和使用方法
	18.锁的等级:方法锁、对象锁、类锁	
	19.如果想实现所有的线程一起等待某个事件的发生,当某个事件发生时,所有线程一起开始往下执行的话,
		有什么好的办法吗?
		CyclicBarrier 实现原理
	20.CAS、AQS
	21.锁的优化策略
		① 读写分离
		② 分段加锁
		③ 减少锁持有的时间
		④ 多个线程尽量以相同的顺序去获取资源 
	22.何为幂等性控制? 举例说明如何实现幂等性
	23.线程间通信方式?
	24.分布式锁?
	25.
八.集合:注意各个不同JDK版本可能有对集合进行相应的优化
	1.ArrayList、LinkedList、Vector 的底层实现和区别?
		ArrayList 如何实现序列化？
	2.HashMap 和 HashTable 的底层实现和区别,两者和 ConcurrentHashMap 的区别?CocurrentHashMap 的桶分割原理,
		Java8 中 HashMap 有什么区别?
		两个线程并发访问map中同一条链,一个线程在尾部删除,一个线程在前面遍历查找,问为什么前面的线程还能正确的查找到后面被另一个线程删除的节点
	3.HashMap 的 hashcode 的作用?什么时候需要重写?如何解决哈希冲突?查找的时候流程是如何
	  HashMap 冲突很厉害,最差性能,你会怎么解决?从O(n)提升到log(n)用二叉排序树的思路说了一通
	  HashMap 是不是有序的? 有没有有顺序的Map实现类?TreeMap 和 LinkedHashMap 是如何保证它的顺序的
	4.HashMap 中是否任何对象都可以做为key,用户自定义对象做为key有没有什么要求?
	5.Arraylist 和 HashMap 如何扩容?负载因子有什么作用?如何保证读写进程安全?
	6.TreeMap,HashMap,LinkedHashMap 的底层实现区别
	7.Collection 包结构的组成,Map、Set 等内部接口的特点与用法
	8.Collection 与 Map 的继承关系?List,Set,Map 的区别?
	9.PriorityQueue,WeakHashMap,EnumMap
	10.CopyOnWriteArrayList、CopyOnWriteArraySet、ConcurrentHashMap 的实现原理和适用场景
	11.如何自己实现一个 Map 类,借鉴 HashMap 的原理,说了一通 HashMap 实现
	12.fail-fast机制(F:\Knowledge\Java\Java源码解读\ArrayList.java)
	13.一个Java对象作为Map的Key时需要满足的前提条件是什么
	14.Collections.sort()排序内部原理,如何优化?

九.时间与正则表达式

十.网络编程