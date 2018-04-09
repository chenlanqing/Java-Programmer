<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一.ArrayList](#%E4%B8%80arraylist)
  - [1.Arraylist 类定义:](#1arraylist-%E7%B1%BB%E5%AE%9A%E4%B9%89)
  - [2.构造方法:](#2%E6%9E%84%E9%80%A0%E6%96%B9%E6%B3%95)
  - [3.成员变量:](#3%E6%88%90%E5%91%98%E5%8F%98%E9%87%8F)
  - [4.数组扩容:](#4%E6%95%B0%E7%BB%84%E6%89%A9%E5%AE%B9)
  - [5.ArrayList 安全隐患:](#5arraylist-%E5%AE%89%E5%85%A8%E9%9A%90%E6%82%A3)
- [二. Vector:](#%E4%BA%8C-vector)
- [三.面试题:](#%E4%B8%89%E9%9D%A2%E8%AF%95%E9%A2%98)
  - [1.ArrayList 与 Vector](#1arraylist-%E4%B8%8E-vector)
  - [2.ArrayList的sublist修改是否影响list本身](#2arraylist%E7%9A%84sublist%E4%BF%AE%E6%94%B9%E6%98%AF%E5%90%A6%E5%BD%B1%E5%93%8Dlist%E6%9C%AC%E8%BA%AB)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 一.ArrayList
## 1.Arraylist 类定义:
	public class ArrayList<E> extends AbstractList<E> 
		implements List<E>, RandomAccess, Cloneable, java.io.Serializable{}
	(1).ArrayList 继承 AbstractList(这是一个抽象类，对一些基础的 List 操作进行封装),
	(2).实现 List,RandomAccess,Cloneable,Serializable 几个接口.
	(3).RandomAccess 是一个标记接口,用来表明其支持快速随机访问,为List提供快速访问功能的;
	(4).Cloneable 是克隆标记接口,覆盖了 clone 函数,能被克隆
	(5).java.io.Serializable 接口,ArrayList 支持序列化,能通过序列化传输数据.
## 2.构造方法:
	// 默认构造函数
	ArrayList()
	// capacity是ArrayList的默认容量大小.当由于增加数据导致容量不足时,容量会添加上一次容量大小的一半.
	ArrayList(int capacity)
	// 创建一个包含collection的ArrayList
	ArrayList(Collection<? extends E> collection)
## 3.成员变量:
	(1).transient Object[] elementData:Object 数组,Arraylist 实际存储的数据.
		如果通过不含参数的构造函数ArrayList()来创建ArrayList,默认为空数组
		public ArrayList() {
	        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
	    }
	    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
	    添加数据时默认初始化容量为10
	    ==> 为什么数组类型是 Object 而不是泛型 E?如果是E的话再初始化的时候需要通过反射来实现
	    ==> 为什么使用 transient 修饰:
		①.为什么 transient Object[] elementData;
			ArrayList 实际上是动态数组，每次在放满以后自动增长设定的长度值,如果数组自动增长长度
			设为100,而实际只放了一个元素,那就会序列化 99 个 null 元素.为了保证在序列化的时候不
			会将这么多 null 同时进行序列化,	ArrayList 把元素数组设置为 transient
		②.为什么要写方法:writeObject and readObject 前面提到为了防止一个包含大量空对象的数组被序列化,
			为了优化存储.所以，ArrayList 使用 transient 来声明elementData作为一个集合,在序列化过程中
			还必须保证其中的元素可以被持久化下来,所以,通过重写writeObject 和 readObject方法的方式把
			其中的元素保留下来writeObject方法把elementData数组中的元素遍历的保存到输出流
			ObjectOutputStream)中.
			readObject方法从输入流(ObjectInputStream)中读出对象并保存赋值到elementData数组中
	(2).private int size: Arraylist 的容量
	(3).protected transient int modCount = 0;这个为父类 AbstractList 的成员变量,记录了ArrayList结构性变化的次数
		在ArrayList的所有涉及结构变化的方法中都增加modCount的值
		AbstractList 中的iterator()方法(ArrayList直接继承了这个方法)使用了一个私有内部成员类Itr,
		该内部类中定义了一个变量 expectedModCount,这个属性在Itr类初始化时被赋予ArrayList对象的modCount属性的值,
		在next()方法中调用了checkForComodification()方法,进行对修改的同步检查;
## 4.数组扩容:
	如果数组容量不够,对数组进行扩容.JDK7 以后及 JDK7 前的实现不一样:
	①.JDK6:直接扩容,且扩容一般是源数组的 1.5 倍
		 int newCapacity = (oldCapacity * 3)/2 + 1;
	②.JDK7:扩容,且一般是之前的 1.5 倍
		int newCapacity = oldCapacity + (oldCapacity >> 1);
	然后拷贝数组: elementData = Arrays.copyOf(elementData, newCapacity);
## 5.ArrayList 安全隐患:
    当传递 ArrayList 到某个方法中,或者某个方法返回 ArrayList,什么时候要考虑安全隐患?如何修复安全违规这个问题呢?
	当array被当做参数传递到某个方法中,如果array在没有被复制的情况下直接被分配给了成员变量,那么就可能发生这种情况,
	即当原始的数组被调用的方法改变的时候,传递到这个方法中的数组也会改变..
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



# 二. Vector:
    1.public class Vector<E>
        extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable{}
    2.大致同 Arraylist,只是 Vector 的方法都是同步的,其是线程安全的
    3.Vector 多一种迭代方式:
        Vector vec = new Vector();
        Integer value = null;
        Enumeration enu = vec.elements();
        while (enu.hasMoreElements()) {
            value = (Integer)enu.nextElement();
        }


# 三.面试题:
## 1.ArrayList 与 Vector
### 1.1.区别:
### 1.2.关于Vector线程安全: 
	(Vector 是同步的.Vector 是线程安全的动态数组.它的操作与 ArrayList 几乎一样):
	如果集合中的元素的数目大于目前集合数组的长度时，Vector 增长率为目前数组长度的 100%,而 Arraylist 增长率为目
	前数组长度的 50%.如过在集合中使用数据量比较大的数据，用 Vector 有一定的优势
	注意:
	在某些特殊场合下,Vector并非线程安全的,看如下代码:
```java
	public static void main(String[] args) {
		Vector<Integer> vector = new Vector<>();
		while (true) {
			for (int i = 0; i < 10; i++) {
				vector.add(i);
			}

			Thread t1 = new Thread() {
				public void run() {
					for (int i = 0; i < vector.size(); i++) {
						vector.remove(i);
					}
				}
			};
			Thread t2 = new Thread() {
				public void run() {
					for (int i = 0; i < vector.size(); i++) {
						vector.get(i);
					}
				}
			};
			t1.start();
			t2.start();
		}
	}	
```
	上述代码会抛出异常:
	java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 9
## 2.ArrayList的sublist修改是否影响list本身
	(1).方法实现:
```java
// fromIndex: 集合开始的索引,toIndex:集合结束的索引,左开右闭
public List<E> subList(int fromIndex, int toIndex) {
	// 边界校验
	subListRangeCheck(fromIndex, toIndex, size);
	return new SubList(this, 0, fromIndex, toIndex);
}
private class SubList extends AbstractList<E> implements RandomAccess {
	private final AbstractList<E> parent; // parent的具体实现类是 ArrayList
	private final int parentOffset;
	private final int offset;
	int size;
	SubList(AbstractList<E> parent,
			int offset, int fromIndex, int toIndex) {
		this.parent = parent;
		this.parentOffset = fromIndex;
		this.offset = offset + fromIndex;
		this.size = toIndex - fromIndex;
		this.modCount = ArrayList.this.modCount;
	}
}
```	
	subList 可以做集合的任何操作
	(2).调用该方法后的生成的新的集合的操作都会对原集合有影响,在subList集合后面添加元素,添加的第一个元素的
		位置就是上述toIndex的值,而原始集合中toIndex的元素往后移动.
		其add方法调用过程:
		add(element) --> AbstractList.add(e) --> SubList.add(index, e)
		--> parent.add(index + parentOffset, e) --> ArrayList.add(newIndex, e)
		

