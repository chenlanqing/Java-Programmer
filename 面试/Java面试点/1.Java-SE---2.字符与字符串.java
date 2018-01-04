1.String 的内部实现? char[] 数组和 String 相比,在使用上有什么优势 ? 容量如何扩充?
	1.1.String 内部是由一个私有的 final 的字符数组
		private final char value[]
		String 对象一旦被创建就是固定不变的了,对 String 对象的任何改变都不影响到原对象,
		相关的任何修改操作都会生成新的对象
	1.2.char[] 数组和 String
		// https://www.zhihu.com/question/36734157/answer/68767786
		(1).String:
			String 在Java中是不可变对象,如果作为普通文本存储密码,那么它会一直存在内存中直至被垃圾收集器回收.
			意味着一旦创建了一个字符串,如果另一个进程把尝试内存的数据导出(dump),在GC进行垃圾回收之前该字符串
			会一直保留在内存中,那么该进程就可以轻易的读取到该字符串;
		(2).char[]:
			对于数组,可以在使用该数组之后显示地擦掉数组中的内容,你可以使用其他不相关的内容把数组内容覆盖掉.
			使用 char[]是相对安全的.使用加密的密码来代替普通的文本字符串密码;
	1.3.String 是不可变字符序列,不可扩充,需要使用 StringBuilder 和 StringBuffer 来处理
2.为什么 String 类是 final 的? 使用 String 时需要注意什么?
	2.1.String 类是 final 的:
		(1).String 是存储在字符串常量池的
		(2).String 的hash值也是不可变的
		(3).不可变对象是线程安全的,可以自由在多个线程之间共享
	2.2.String 使用时注意点:
		(1).任何时候,字符串的比较都应该使用 equals()
		(2).修改字符串的操作,应该使用 StringBuffer 和 StringBuilder
		(3).在使用 substring 和 split 时可能会造成内存泄露
3.String & StringBuilder & StringBuffer 区别,适用场景?
	(1).都是 final 类,不允许被继承
	(2).String 长度是不可变的, StringBuffer 和 StringBuilder 长度是可变的;
	(3).StringBuffer 是线程安全的,StringBuffer 和 StringBuilder 所有方法都是一样的,只是 StringBuffer 方法都加上了
		synchronized 修饰确保线程安全;
	(4).StringBuilder 性能优于 StringBuffer;
	(5).如果在编译期能确认是一个字符串常量,字符串会自动拼接成一个常量,此时 String 拥有更好的性能.
	(6).如果在多线程环境可以使用 StringBuffer 进行字符串连接操作,单线程环境使用 StringBuilder，它的效率更高
	(7).StringBuilder 是JDK1.5之后才出现.
	(8).StringBuffer 和 StringBuilder 都继承了抽象类 AbstractStringBuilder,	这个抽象类和 String 一样也定义了
		char[] value和 int count，但是与 String 类不同的是，它们没有 final 修饰符
4.String s = "aa"+"bb"; 编译器会做什么优化?
	直接变为:String s = "aabb";
	String m = "Hello,World";
	String u = m + ".";
	String v = "Hello,World.";
	u == v ==> false;
	如果 m 改为 final 修饰:
	u == v ==> true;
5.如何理解 String 的不可变?
	5.1.不可变对象:如果一个对象,在它创建完成之后,不能再改变它的状态,那么这个对象就是不可变的.不能改变状态的意思是,
		不能改变对象内的成员变量,包括基本数据类型的值不能改变,引用类型的变量不能指向其他的对象,
		引用类型指向的对象的状态也不能改变
	5.2.String 不可变:
		(1).final 修饰的类,是不可继承的
		(2).内部数据使用的是私有的 final 字符数组,在String内部实现时没有暴露对应的成员字段
	当然,反射可以去破坏其不可变
6.StringBuilder 与 String 性能对比[参考:/Java/Java源码解读/String.java]
7.StringBuilder 的实现方式,容量如何扩充;
	(1).继承抽象类 AbstractStringBuilder,该类里面定义了 char[] value和 int count,字符数组没有使用 final 修饰
	(2).数组容量不够的时候,会调用 AbstractStringBuilder 的expandCapacity()方法,将数组的容量扩大至原来的 2n+2;
	其中,expandCapacity()又调用了 Arrays 的copyOf()方法，目的是把原来数组的元素拷贝至新的数组








