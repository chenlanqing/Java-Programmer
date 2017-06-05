***********注意:各个不同JDK版本可能有对集合进行相应的优化
1.ArrayList、LinkedList、Vector 的底层实现和区别?
	ArrayList 如何实现序列化?
2.HashMap 和 HashTable 的底层实现和区别,两者和 ConcurrentHashMap 的区别?CocurrentHashMap 的桶分割原理,
	Java8 中 HashMap 有什么区别?
	两个线程并发访问map中同一条链,一个线程在尾部删除,一个线程在前面遍历查找,问为什么前面的线程还能正确的查找到后面被另一个线程删除的节点
3.HashMap 的 hashcode 的作用?什么时候需要重写?如何解决哈希冲突?查找的时候流程是如何
  HashMap 冲突很厉害,最差性能,你会怎么解决?从O(n)提升到log(n)用二叉排序树的思路说了一通
  HashMap 是不是有序的? 有没有有顺序的Map实现类? 
4.HashMap 中是否任何对象都可以做为key,用户自定义对象做为key有没有什么要求?
5.Arraylist 和 HashMap 如何扩容?负载因子有什么作用?如何保证读写进程安全?
6.TreeMap,HashMap,LinkedHashMap 的底层实现区别? TreeMap 和 LinkedHashMap 是如何保证它的顺序的
7.Collection 包结构的组成,Map、Set 等内部接口的特点与用法
8.Collection 与 Map 的继承关系?List,Set,Map 的区别?
9.PriorityQueue,WeakHashMap,EnumMap
10.CopyOnWriteArrayList、CopyOnWriteArraySet、ConcurrentHashMap 的实现原理和适用场景
11.如何自己实现一个 Map 类,借鉴 HashMap 的原理,说了一通 HashMap 实现
12.fail-fast机制(F:\Knowledge\Java\Java源码解读\ArrayList.java)
13.一个Java对象作为Map的Key时需要满足的前提条件是什么
14.Collections.sort()排序内部原理,如何优化?
