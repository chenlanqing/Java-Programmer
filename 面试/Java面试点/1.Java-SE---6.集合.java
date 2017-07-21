***********注意:各个不同JDK版本可能有对集合进行相应的优化
1.集合总览:Collection, Map
	降低编程难度
	提高程序性能
	提高API间的互操作性
	降低学习难度
	降低设计和实现相关API的难度
	增加程序的重用性
2.fail-fast机制:
	参考:/Java/Java源码解读/集合/集合类.java
3.ArrayList、LinkedList、Vector 三者区别
	// https://blog.52itstyle.com/archives/15/
	实现 RandomAccess 接口的集合比如 ArrayList,应当使用最普通的for循环而不是foreach循环来遍历
4.ArrayList 如何实现序列化?
	参考:Java/Java源码解读/集合/ArrayList.java
5.List 和 Set 区别
6.HashMap 和 HashTable 的区别,两者和 ConcurrentHashMap 的区别?
7.各个JDK版本之间 HashMap 的实现区别?
8.HashMap 的 hashcode 的作用?什么时候需要重写?如何解决哈希冲突?
	8.1.HashMap 本身没有重写 hashCode ,但是其内部类 Entry 或 Node 都需要重写.
	8.2.一般的地方不需要重载hashCode,只有当类需要放在 HashTable、HashMap、HashSet 等等hash结构的集合时才会重载hashCode
	8.3.如何解决哈希冲突:
		(1).哈希冲突发生的原因:如果两个key通过hashCode 计算之后,如果hash值相同,则发生了hash冲突.
		(2).JDK7 之前版本:使用链表解决;
			当发生hash冲突时,则将存放在数组中的Entry设置为新值的next.比如A和B都hash后都映射到下标i中,之前已经有A了,
			当map.put(B)时,将B放到下标i中,A则为B的next,所以新值存放在数组中,旧值在新值的链表上.
			当hash冲突很多时,HashMap 退化成链表.查找时间从O(1)到O(n)
		(3).JDK8 之后:采用的是位桶+链表/红黑树,当某个位桶的链表的长度达到某个阀值的时候,这个链表就将转换成红黑树.
			当同一个hash值的节点数不小于8时,将不再以单链表的形式存储了,会被调整成一颗红黑树
			static final int TREEIFY_THRESHOLD = 8;
9.HashMap 冲突很厉害,最差性能,你会怎么解决?从O(n)提升到log(n)用二叉排序树的思路说了一通
	二叉排序树
10.HashMap 是不是有序的? 有没有有顺序的Map实现类? 
	10.1.HashMap 不是有序的;
	10.2.TreeMap 和 LinkedHashMap: TreeMap 默认是升序的, LinkedHashMap 则记录了插入顺序
11.HashMap 中是否任何对象都可以做为key,用户自定义对象做为key有没有什么要求?
	一个Java对象作为Map的Key时需要满足的前提条件是什么?
	==> 自定义key必须重写 equals 和 hashcode 方法,如果可以的话,将自定义设计成不可变,如 final 修饰等
12.Arraylist 和 HashMap 如何扩容?负载因子有什么作用? 如何保证读写进程安全?
	12.1.Arraylist 扩容:
		如果数组容量不够,对数组进行扩容.JDK7 以后及 JDK7 前的实现不一样:
		①.JDK6:直接扩容,且扩容一般是源数组的 1.5 倍
			 int newCapacity = (oldCapacity * 3)/2 + 1;
		②.JDK7:扩容,且一般是之前的 1.5 倍
			int newCapacity = oldCapacity + (oldCapacity >> 1);
		然后拷贝数组: elementData = Arrays.copyOf(elementData, newCapacity);
	12.2.HashMap 如何扩容:
		http://blog.csdn.net/aichuanwendang/article/details/53317351
13.TreeMap, LinkedHashMap 的底层实现区别? 
	参考:Java/Java源码解读/集合/LinkedHashMap.java 和 TreeMap.java
14.TreeMap 和 LinkedHashMap 是如何保证它的顺序的
	14.1.TreeMap 里通过红黑树来实现的,根据其键的自然顺序进行排序,或者根据创建映射时提供的 Comparator 进行排序.
		TreeMap 中的key-value对总是处于有序状态
	14.2.LinkedHashMap 是 HashMap 的子类,内部维护了一个双向链表来保证有序.可以按照插入顺序遍历
		static class Entry<K,V> extends HashMap.Node<K,V> {
	        Entry<K,V> before, after;
	        Entry(int hash, K key, V value, Node<K,V> next) {
	            super(hash, key, value, next);
	        }
	    }
	    然后定义了一个before与after用来存储当前key的上一个值引用和下一个值的引用.
	    LinkedHashMap 还可以根据近期访问某个键值对的顺序(从近期访问最少到近期访问最多的顺序)来遍历元素,
	    这可让需要利用LRU方法进行缓存结果的用户提供一个新的解决方案
15.Collection 包结构的组成,Map、Set 等内部接口的特点与用法
16.Collection 与 Map 的继承关系?List,Set,Map 的区别?
17.PriorityQueue,WeakHashMap,EnumMap 的用途及区别
18.CopyOnWriteArrayList、CopyOnWriteArraySet、ConcurrentHashMap 的实现原理和适用场景?CocurrentHashMap 的桶分割原理.
19.如何自己实现一个 Map 类,借鉴 HashMap 的原理,说了一通 HashMap 实现
20.fail-fast机制(F:\Knowledge\Java\Java源码解读\ArrayList.java)
21.Collections.sort()排序内部原理,如何优化?
22.两个线程并发访问map中同一条链,一个线程在尾部删除,一个线程在前面遍历查找,问为什么前面的线程还能正确的查找到
后面被另一个线程删除的节点
23.WeakHashMap 会发生内存泄露
24.用 HashMap 怎么去实现 ConcurrentHashMap:Collections.synchronizedMap(map)
25.那些线程安全的队列:ConcurrentLinkedQueue, ConcurrentLinkedDeque 以及 BlockingQueue 系列









