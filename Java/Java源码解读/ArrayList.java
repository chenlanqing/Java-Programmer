Arraylist:
	2.1.ArrayList 的大小是如何自动增加的?
		当有人试图在 Arraylist 中增加一个对象的时候,Java会去检查Arraylist,以确保已存在的数组中有足够的容量来存储这个新的对象。
		如果没有足够容量的话，那么就会新建一个长度更长的数组，旧的数组就会使用 Arrays.copyOf() 方法被复制到新的数组中去，
		现有的数组引用指向了新的数组;
		主要方法是:
			public boolean add(E e){}
			public void ensureCapacity(int minCapacity) {} // 处理 ArrayList的大小
	2.2.Arraylist 与 LinkedList 比较:
		(1).Arraylist 内部实现是数组, LinkedList 内部实现是链表
		(2).当频繁访问数据时,使用 Arraylist; 频繁增删数据时且不经常访问数据时,可以使用 LinkedList
			ArrayList 中访问元素的最糟糕的时间复杂度是”1″，而在 LinkedList 中可能就是”n”了
			ArrayList 中增加或者删除某个元素，通常会调用 System.arraycopy 方法，这是一种极为消耗资源的操作，
			因此，在频繁的插入或者是删除元素的情况下，LinkedList 的性能会更加好一点
	2.3.当传递 ArrayList 到某个方法中,或者某个方法返回 ArrayList,什么时候要考虑安全隐患?如何修复安全违规这个问题呢?
		当array被当做参数传递到某个方法中，如果array在没有被复制的情况下直接被分配给了成员变量，那么就可能发生这种情况，
		即当原始的数组被调用的方法改变的时候，传递到这个方法中的数组也会改变.
		下面的这段代码展示的就是安全违规以及如何修复这个问题:
		(1).ArrayList 被直接赋给成员变量——安全隐患：
			public void setMyArray(String[] myArray){
				this.myArray = myArray;
			}
		(2).修复代码:
			public void setMyArray(String[] newMyArray){
				if(newMyArray == null){
					this.myArray = new String[0];					
				}else{
					this.myArray = Arrays.copyOf(newMyArray, newMyArray.length);
				}
			}
	2.4.如果需要同步的 List 可以使用:
		Collections.synchronizedList(List<T> list)
	2.5.ArrayList 与 Vector(Vector 是同步的.Vector 是线程安全的动态数组.它的操作与 ArrayList 几乎一样):
		如果集合中的元素的数目大于目前集合数组的长度时，Vector 增长率为目前数组长度的 100%,而 Arraylist 增长率为目
		前数组长度的 50%.如过在集合中使用数据量比较大的数据，用 Vector 有一定的优势
	2.7.源码分析:
		2.7.1.底层使用数组实现:
			private transient Object[] elementData;
		2.7.1.构造函数,ArrayList 提供了三个构造函数：
			ArrayList()：默认构造函数，提供初始容量为 10 的空列表
			ArrayList(int initialCapacity)：构造一个具有指定初始容量的空列表。
			ArrayList(Collection<? extends E> c)：构造一个包含指定 collection 的元素的列表，
				这些元素是按照该 collection 的迭代器返回它们的顺序排列的

***********************************************源码分析**********************************************************
CRUD 操作:增加(Create)、读取(Retrieve)、更新(Update)和删除(Delete)这四个基本操作
一.ArrayList 类定义:
public class ArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, java.io.Serializable{}
1.可以看出 ArrayList 继承 AbstractList(这是一个抽象类，对一些基础的 List 操作进行封装),
实现 List,RandomAccess，Cloneable，Serializable 几个接口，RandomAccess 是一个标记接口，用来表明其支持快速随机访问

二.底层存储:
1.ArrayList 是一个可以动态增长的顺序表,内部使用数组elementData存储元素
	private transient Object[] elementData; // 为什么不是 T 类型?因为新建一个数组时 泛型会编译出错,需要使用反射来实现
	private int size;
	(1).elementData数组是使用 transient 修饰的，关于 transient 关键字的作用简单说就是java自带默认机制进行序列化的时候,
		被其修饰的属性不需要维持
	(2).ArrayList :使用了自定义的序列化方式:
		private void writeObject(java.io.ObjectOutputStream s){};
		private void readObject(java.io.ObjectInputStream s)
	(3).为什么要如此设计?
		elementData 是一个数据存储数组,而数组是定长的，它会初始化一个容量，等容量不足时再扩充容量(扩容方式为数据拷贝)
		再通俗一点说就是比如 elementData 的长度是10，而里面只保存了3个对象，那么数组中其余的7个元素(null)是没有意义的，
		所以也就不需要保存，以节省序列化后的内存容量，好了到这里就明白了这样设计的初衷和好处，
		顺便好像也明白了长度单独用一个int变量保存，而不是直接使用elementData.length的原因
2.modCount属性:modify count的简写，当ArrayList的结构发生修改的时候，modCount自动加1,主要有添加、删除、清空、扩容四种操作
3.elementData:对象数组
	为什么数组类型是 Object 而不是泛型 E?如果是E的话再初始化的时候需要通过反射来实现
三.构造方法:
1.public ArrayList( int initialCapacity) {}
	构造一个具有指定容量的list
2.public ArrayList() {}
	构造一个初始容量为10的list
3.public ArrayList(Collection<? extends E> c) {}
	造一个包含指定元素的 List，这些元素的是按照 Collection 的迭代器返回的顺序排列的
4.既然默认为 10 就可以那么为什么还要提供一个可以指定容量大小的构造方法呢?

四.方法:
1.添加:
	public void ensureCapacity(int minCapacity){}	数组扩容			时间复杂度 O(N)
	public boolean add(E e)							数组尾部添加元素	时间复杂度 O(1)
	public void add(int index, E element)		数组的index位置插入元素	时间复杂度 O(N)
	(1).void ensureCapacity(int minCapacity):数组扩容是使用拷贝数组
		JDK7 以后及 JDK7 前的实现不一样:
		①.JDK6:直接扩容,且扩容一般是源数组的 1.5 倍
			 int newCapacity = (oldCapacity * 3)/2 + 1;
		②.JDK7:扩容,且一般是之前的 1.5 倍
			int newCapacity = oldCapacity + (oldCapacity >> 1);
	(2).add(E e):
		数组扩容,调用ensureCapacity()方法
	(3).add(int index, E element) 判断下标是否越界
		数组是否需要扩容,
		复制数组,调用 System.arraycopy(..);
		size++;
2.删除方法:
	public E remove(int index)		删除index位置的元素，并返回			时间复杂度 O(N)
	public boolean remove(Object o) 先找到对象o所在的位置，然后再删除	时间复杂度 O(N)
	(1).remove(int index):
		边界检查
		数组拷贝:System.arraycopy(elementData, index+1, elementData, index,numMoved);
		将数组最后一个元素置为 null:elementData[--size] = null;
	(2).remove(Object o):
		循环找到匹配的 o;
		调用 fastRemove()方法,数组拷贝:System.arraycopy(elementData, index+1, elementData, index,numMoved);
		将数组最后一个元素置为 null:elementData[--size] = null;
3.查询和修改：
	public E get(int index)				取得index位置的元素				时间复杂度 O(1)
	public int indexOf(Object o)		找到对象o所在的位置(调用equals)	时间复杂度 O(N)
	public E set(int index, E element)	将index位置的元素设置为element	时间复杂度 O(1)
	(1).E get(int index):边界判断
	(2).int indexOf(Object o):使用 equals判断是否相等
	(3).E set(int index, E element) 边界检查,直接赋值
4.迭代:
	public Iterator<E> iterator(){
		return new Itr();// Itr 实现了 Iterator接口
	}
	(1).private class Itr implements Iterator<E>{}:Arraylist 的内部类
	(2).hashNext：判断游标是否已经指向数组末尾
		next：返回当前元素，并且游标向后移动一位
	(3).由于有expectedModCount以及modCount的存在,迭代的过程中,不建议进行添加或删除:
		int expectedModCount = modCount;
		final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }



































