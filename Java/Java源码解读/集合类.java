一.集合
1.集合框架:Java 中的集合框架定义了一套规范，用来表示、操作集合，使具体操作与实现细节解耦
2.设计理念:
	(1).为了保证核心接口足够小，最顶层的接口（也就是Collection与Map接口）并不会区分该集合是否可变（mutability）,
		是否可更改（modifiability）,是否可改变大小（resizability）这些细微的差别。相反，一些操作是可选的，
		在实现时抛出UnsupportedOperationException即可表示集合不支持该操作;
	(2).框架提供一套方法，让集合类与数组可以相互转化，并且可以把Map看作成集合;
3.Collection 和 Map:
	3.1.集合框架的类继承体系中,最顶层有两个接口：
		Collection 表示一组纯数据
		Map	表示一组key-value对
	3.2.一般继承自 Collection 或 Map 的集合类，会提供两个"标准"的构造函数:
		没有参数的构造函数,创建一个空的集合类
		有一个类型与基类(Collection或Map)相同的构造函数,创建一个与给定参数具有相同元素的新集合类
	3.3.Collection 接口主要有三个接口
		(1).Set 表示不允许有重复元素的集合(A collection that contains no duplicate elements);
		(2).List 表示允许有重复元素的集合(An ordered collection (also known as a sequence));
		(3).Queue DK1.5新增,与上面两个集合类主要是的区分在于Queue主要用于存储数据,而不是处理数据
			(A collection designed for holding elements prior to processing)
	3.4.Map 并不是一个真正意义上的集合,这个接口提供了三种"集合视角",使得可以像操作集合一样操作它们:
		(1).把 map的内容看作key的集合(map’s contents to be viewed as a set of keys)
			Set<K> keySet()，提供key的集合视角
		(2).把map的内容看作value的集合(map’s contents to be viewed as a collection of values)
			Collection<V> values()，提供value的集合视角
		(3).把map的内容看作key-value映射的集合(map’s contents to be viewed as a set of key-value mappings)
			Set<Map.Entry<K, V>> entrySet()，提供key-value序对的集合视角，这里用内部类 Map.Entry 表示序对
	3.5.Collections:集合的工具类,提供了操作集合类型的静态方法的类;

二.fail-fast机制:
1.基本:是Java集合的一种错误检测机制,当多个线程对集合进行结构上的改变操作时,有可能会发生 fail-fast机制.
	记住:是有可能,而不是一定;
	假设有两个线程A,B 要对集合进行操作,当A线程在遍历集合的元素时,B 线程修改了集合(增删改),
	这个时候抛出 ConcurrentModificationException 异常,从而就会触发fail-fast机制,
2.fail-fast机制产生的原因:
	2.1.ConcurrentModificationException 产生的原因:
		当方法检测到对象的并发修改,但不允许这种修改时就会抛出该异常.同时需要注意的是,该异常不会始终指出对象已经由
		不同线程并发修改,如果单线程违反了规则,同样也有可能会抛出异常;
		诚然,迭代器的快速失败行为无法得到保证,它不能保证一定出现错误,但是快速失败操作会尽最大努力抛出 
		ConcurrentModificationException 异常 ConcurrentModificationException 应该仅用于检测 bug
	2.2.以 ArrayList 为例分析fail-fast产生的原因:
		(1).查看 ArrayList 的 Itr 的源码可以发现,当迭代器在调用 next 和 remove 时都会调用 checkForComodification()方法:
			final void checkForComodification() {
	            if (modCount != expectedModCount)
	                throw new ConcurrentModificationException();
	        }
	    	该方法检测modCount 与 expectedModCount是否相等,若不等则抛出 ConcurrentModificationException 异常,
	    	从而产生fail-fast机制;
	    (2).为什么 modCount != expectedModCount,他们的值是在何时发生改变的?
	    		int expectedModCount = modCount;(JDK1.7.0._79-b15)
	    	modCount是在 AbstractList 中定义的,为全局变量:
	    		protected transient int modCount = 0;
	    	ArrayList 中无论add、remove、clear方法只要是涉及了改变 ArrayList 元素的个数的方法都会导致 "modCount" 的改变,
	    	初步判断由于expectedModCount 得值与modCount的改变不同步,导致两者之间不等从而产生fail-fast机制:
	    	即期望的值跟修改后的值不等
3.fail-fast 解决方法:
	3.1.在遍历过程中所有涉及到改变modCount值得地方全部加上 synchronized 或者直接使用 Collections.synchronizedList,
		这样就可以解决;但是不推荐,因为增删造成的同步锁可能会阻塞遍历操作
	3.2.使用 CopyOnWriteArrayList 来替换 ArrayList:
		CopyOnWriteArrayList 是 ArrayList 的一个线程安全的变体其中所有可变操作(add、set 等)都是通过对底层数组进
		行一次新的复制来实现的;
		3.2.1.在两种情况下非常适用:
			(1).在不能或不想进行同步遍历,但又需要从并发线程中排除冲突时
			(2).当遍历操作的数量大大超过可变操作的数量时
		3.2.2.为什么 CopyOnWriteArrayList 可以替代 ArrayList 呢?	
			(1).CopyOnWriteArrayList 的无论是从数据结构、定义都和 ArrayList 一样.
				它和 ArrayList 一样,同样是实现 List 接口,底层使用数组实现.在方法上也包含add、remove、clear、iterator等方法;
			(2).CopyOnWriteArrayList 根本就不会产生 ConcurrentModificationException 异常,也就是它使用迭代器完
				全不会产生fail-fast机制
		3.2.3.CopyOnWriteArrayList 所代表的核心概念就是:任何对array在结构上有所改变的操作(add、remove、clear等),
			CopyOnWriteArrayList 都会copy现有的数据,再在copy的数据上修改,这样就不会影响COWIterator中的数据了,
			修改完成之后改变原有数据的引用即可.同时这样造成的代价就是产生大量的对象,同时数组的copy也是相当有损耗的
4.fail-safe机制:fail-safe任何对集合结构的修改都会在一个复制的集合上进行修改,因此不会抛出 ConcurrentModificationException
	fail-safe机制有两个问题:
		(1).需要复制集合,产生大量的无效对象,开销大
		(2).无法保证读取的数据是目前原始数据结构中的数据
	如:CopyOnWriteArrayList












































