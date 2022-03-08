
# 一、ArrayList

## 1、Arraylist 类定义

```java
public class ArrayList<E> extends AbstractList<E> implements List<E>， RandomAccess， Cloneable， java.io.Serializable{}
```
- ArrayList 继承 AbstractList（这是一个抽象类，对一些基础的 List 操作进行封装）；
- 实现 List，RandomAccess，Cloneable，Serializable 几个接口；
- RandomAccess 是一个标记接口，用来表明其支持快速随机访问，为Lis提供快速访问功能的；实现了该接口的list可以通过for循环来遍历数据，比使用迭代器的效率要高；
- Cloneable 是克隆标记接口，覆盖了 clone 函数，能被克隆；
- java.io.Serializable 接口，ArrayList 支持序列化，能通过序列化传输数据。

## 2、构造方法

```java
// 默认构造函数，会使用长度为零的数组；
ArrayList()
// capacity是ArrayList的默认容量大小，当由于增加数据导致容量不足时，容量会增加到当前容器的1.5倍
ArrayList(int capacity)
// 创建一个包含collection的ArrayList，会使用collection的大小作为数组容量
ArrayList(Collection<? extends E> collection){
	elementData = c.toArray();
	if ((size = elementData.length) != 0) {
		// c.toArray might (incorrectly) not return Object[] (see 6260652)
		if (elementData.getClass() != Object[].class)
			elementData = Arrays.copyOf(elementData, size, Object[].class);
	} else {
		// replace with empty array.
		this.elementData = EMPTY_ELEMENTDATA;
	}
}
```

上面包含Collection参数的构造方法里面有个数字：6260652，这是一个Java的bug，意思当给定的集合内的元素不是Object类型时，会转换成Object类型。一般情况下不会触发此  [bug 6260652](https://bugs.java.com/bugdatabase/view_bug.do?bug_id=6260652)，只有在下面的场景下才会触发：ArrayList初始化之后（ArrayList元素非Object类型），再次调用toArray方法时，得到Object数组，并且往Object数组赋值时才会触发该bug：
```java
List<String> list = Arrays.asList("hello world", "Java");
Object[] objects = list.toArray();
System.out.println(objects.getClass().getSimpleName());
objects[0] = new Object();
```
输出结果：
```java
Exception in thread "main" java.lang.ArrayStoreException: java.lang.Object
	at com.jolly.demo.TestArrayList.main(TestArrayList.java:23)
String[]
```
上面bug在jdk9后解决了：Arrays.asList(x).toArray().getClass() should be Object[].class

## 3、成员变量

- `transient Object[] elementData`：Object 数组，Arraylist 实际存储的数据。如果通过不含参数的构造函数ArrayList()来创建ArrayList，默认为空数组
	```java
	public ArrayList() {
		this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
	}
	private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
	```
	ArrayList无参构造器初始化时，默认是空数组的，数组容量也是0，真正是在添加数据时默认初始化容量为10；
	- 为什么数组类型是 Object 而不是泛型E：如果是E的话在初始化的时候需要通过反射来实现，另外一个泛型是在JDK1.5之后才出现的，而ArrayList在jdk1.2就有了；
	- ①、为什么 transient Object[] elementData；

		ArrayList 实际上是动态数组，每次在放满以后自动增长设定的长度值，如果数组自动增长长度设为100，而实际只放了一个元素，那就会序列化 99 个 null 元素.为了保证在序列化的时候不
		会将这么多 null 同时进行序列化，	ArrayList 把元素数组设置为 transient；

	- ②、为什么要写方法：`writeObject and readObject`：
	
		前面提到为了防止一个包含大量空对象的数组被序列化，为了优化存储.所以，ArrayList 使用 transient 来声明elementData作为一个集合，在序列化过程中还必须保证其中的元素可以被持久化下来，所以，通过重写writeObject 和 readObject方法的方式把其中的元素保留下来writeObject方法把elementData数组中的元素遍历的保存到输出流ObjectOutputStream)中。readObject方法从输入流(ObjectInputStream)中读出对象并保存赋值到elementData数组中

- `private int size`：Arraylist 的容量
- `protected transient int modCount = 0`

	这个为父类 AbstractList 的成员变量，记录了ArrayList结构性变化的次数在ArrayList的所有涉及结构变化的方法中都增加modCount的值，AbstractList 中的iterator()方法（ArrayList直接继承了这个方法）使用了一个私有内部成员类Itr，该内部类中定义了一个变量 expectedModCount，这个属性在Itr类初始化时被赋予ArrayList对象的modCount属性的值，在next()方法中调用了checkForComodification()方法，进行对修改的同步检查；

## 4、数组容量

ArrayList 初始默认是空数组，待需要添加元素时，判断是否需要扩容；新增元素主要有两步：
- 判断是否需要扩容，如果需要执行扩容操作；
- 扩容主要是：扩容的数组容量以及数组的拷贝操作；

如果数组容量不够，对数组进行扩容，JDK7 以后及 JDK7 前的实现不一样。扩容本质上是对数组之间的数据拷贝；

**JDK6：直接扩容，且扩容一般是源数组的 1.5 倍：**
```java
int newCapacity = (oldCapacity * 3)/2 + 1;
```

**JDK7：扩容，且一般是之前的 1.5 倍**
```java
int newCapacity = oldCapacity + (oldCapacity >> 1);
// 拷贝数组
elementData = Arrays.copyOf(elementData， newCapacity);
```

**JDK8：首次扩容为10，再次扩容一般是之前的1.5倍**
```java
private void ensureCapacityInternal(int minCapacity) {
	ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
}
private static int calculateCapacity(Object[] elementData, int minCapacity) {
	if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
		return Math.max(DEFAULT_CAPACITY, minCapacity);
	}
	return minCapacity;
}
private void ensureExplicitCapacity(int minCapacity) {
	modCount++;
	// 如果我们期望的最小容量大于目前数组的长度，那么就扩容
	if (minCapacity - elementData.length > 0)
		grow(minCapacity);
}
// 扩容，并把现有数据拷贝到新的数组里面去
private void grow(int minCapacity) {
	// overflow-conscious code
	int oldCapacity = elementData.length;
	int newCapacity = oldCapacity + (oldCapacity >> 1);
	if (newCapacity - minCapacity < 0)
		newCapacity = minCapacity;
	if (newCapacity - MAX_ARRAY_SIZE > 0)
		newCapacity = hugeCapacity(minCapacity);
	// minCapacity is usually close to size, so this is a win:
	elementData = Arrays.copyOf(elementData, newCapacity);
}
```

> 在实际添加大量元素前，我也可以使用`ensureCapacity`来手动增加ArrayList实例的容量，以减少递增式再分配的数量；

> 数组进行扩容时，会将老数组中的元素重新拷贝一份到新的数组中，每次数组容量的增长大约是其原容量的1.5倍。这种操作的代价是很高的，因此在实际使用时，我们应该尽量避免数组容量的扩张。当我们可预知要保存的元素的多少时，要在构造ArrayList实例时，就指定其容量，以避免数组扩容的发生。或者根据实际需求，通过调用ensureCapacity方法来手动增加ArrayList实例的容量；

## 5、方法

### 5.1、add和addAll

`add(int index, E e)`需要先对元素进行移动，然后完成插入操作，也就意味着该方法有着线性的时间复杂度。

`addAll()`方法能够一次添加多个元素，根据位置不同也有两个把本，一个是在末尾添加的`addAll(Collection<? extends E> c)`方法，一个是从指定位置开始插入的`addAll(int index, Collection<? extends E> c)`方法。跟`add()`方法类似，在插入之前也需要进行空间检查，如果需要则自动扩容；如果从指定位置插入，也会存在移动元素的情况。 `addAll()`的时间复杂度不仅跟插入元素的多少有关，也跟插入的位置相关；

addAll() 时，没有元素时，扩容为`Math.max(10, 实际元素个数)`，有元素时为 `Math.max(原容量的1.5倍, 实际元素个数)`；

### 5.2、set()

既然底层是一个数组*ArrayList*的`set()`方法也就变得非常简单，直接对数组的指定位置赋值即可

```java
public E set(int index, E element) {
    rangeCheck(index);//下标越界检查
    E oldValue = elementData(index);
    elementData[index] = element;//赋值到指定位置，复制的仅仅是引用
    return oldValue;
}
```

### 5.3、get()

`get()`方法同样很简单，唯一要注意的是由于底层数组是Object[]，得到元素后需要进行类型转换

### 5.4、indexOf和lastIndexOf

这两个方法实现差不多，一个是从0开始遍历，一个是尾部开始遍历，
```java
public int indexOf(Object o) {
	return indexOfRange(o, 0, size);
}
int indexOfRange(Object o, int start, int end) {
	Object[] es = elementData;
	if (o == null) {
		for (int i = start; i < end; i++) {
			if (es[i] == null) {
				return i;
			}
		}
	} else {
		for (int i = start; i < end; i++) {
			if (o.equals(es[i])) {
				return i;
			}
		}
	}
	return -1;
}
```
需要注意的一点是：如果参数为null，只要集合中有null的元素，匹配到直接返回；

### 5.5、removeAll和retainAll

```java
public boolean removeAll(Collection<?> c) {
	return batchRemove(c, false, 0, size);
}
public boolean retainAll(Collection<?> c) {
	return batchRemove(c, true, 0, size);
}
boolean batchRemove(Collection<?> c, boolean complement, final int from, final int end) {
	Objects.requireNonNull(c);
	final Object[] es = elementData;
	int r;
	// Optimize for initial run of survivors
	for (r = from;; r++) {
		if (r == end)
			return false;
		if (c.contains(es[r]) != complement)
			break;
	}
	int w = r++;
	try {
		for (Object e; r < end; r++)
			if (c.contains(e = es[r]) == complement)
				es[w++] = e;
	} catch (Throwable ex) {
		// Preserve behavioral compatibility with AbstractCollection,
		// even if c.contains() throws.
		System.arraycopy(es, r, es, w, end - r);
		w += end - r;
		throw ex;
	} finally {
		modCount += end - w;
		shiftTailOverGap(es, w, end);
	}
	return true;
}
```
- `A.removeAll(B)`：从集合A中删除A中包含给定集合B的所有元素，即保留集合A中不在集合B中的元素
- `A.retainAll(B)`：从集合A中删除不包含集合B中的元素，即取交集

示例：
```java
public static void testRemoveRetain() {
	ArrayList<Integer> list = new ArrayList<>();
	list.add(1);
	list.add(4);
	list.add(6);

	ArrayList<Integer> list1 = new ArrayList<>();
	for (int i = 0; i < 5; i++) {
		list1.add(i);
	}
	ArrayList<Integer> list2 = new ArrayList<>(list1);
	System.out.println("执行前-list1 = " + list1);
	list1.removeAll(list);
	System.out.println("执行后-list1 = " + list1);
	System.out.println("执行前-list2 = " + list2);
	list2.retainAll(list);
	System.out.println("执行后-list2 = " + list2);
}
// 输出：
执行前-list1 = [0, 1, 2, 3, 4]
执行后-list1 = [0, 2, 3]
执行前-list2 = [0, 1, 2, 3, 4]
执行后-list2 = [1, 4]
```

### 5.6、removeIf

该函数接收一个 Predicate 对象，即一个lambda表达式，表示过滤数据，即该表达式中为true的数据过滤掉
```java
public interface Predicate<T> {
    boolean test(T t);
}
```

### 5.7、toArray



## 6、ArrayList 安全隐患

- **当传递 ArrayList 到某个方法中，或者某个方法返回 ArrayList，什么时候要考虑安全隐患？如何修复安全违规这个问题呢？**

	当array被当做参数传递到某个方法中，如果array在没有被复制的情况下直接被分配给了成员变量，那么就可能发生这种情况，即当原始的数组被调用的方法改变的时候，传递到这个方法中的数组也会改变。下面的这段代码展示的就是安全违规以及如何修复这个问题：
	- ArrayList 被直接赋给成员变量——安全隐患：
		```java
		public void setMyArray(String[] myArray){
			this.myArray = myArray;
		}
		```
	- 修复代码
		```java
		public void setMyArray(String[] newMyArray){
			if(newMyArray == null){
				this.myArray = new String[0];					
			}else{
				this.myArray = Arrays.copyOf(newMyArray， newMyArray.length);
			}
		}
		```

- 线程安全问题的本质

	因为ArrayList自身的elementData、size、modCount在进行各种操作时，都没有加锁，而且这些数据并不是可见的（volatile），如果多个线程对变量进行操作时，可能存在数据被覆盖问题；

# 二、Vector

## 1、签名：
```java
public class Vector<E> extends AbstractList<E>	implements List<E>， RandomAccess， Cloneable， java.io.Serializable
```
## 2、方法与变量：

大致同 Arraylist，只是 Vector 的方法都是同步的，其是线程安全的

## 3、Vector 多一种迭代方式
```java
Vector vec = new Vector();
Integer value = null;
Enumeration enu = vec.elements();
while (enu.hasMoreElements()) {
	value = (Integer)enu.nextElement();
}
```

# 三、面试题

## 1、ArrayList 与 Vector

### 1.1、区别

### 1.2、关于Vector线程安全

（Vector 是同步的，Vector 是线程安全的动态数组，它的操作与 ArrayList 几乎一样）如果集合中的元素的数目大于目前集合数组的长度时，Vector 增长率为目前数组长度的 100%，而 Arraylist 增长率为目前数组长度的 50%，如过在集合中使用数据量比较大的数据，用 Vector 有一定的优势

注意：在某些特殊场合下，Vector并非线程安全的，看如下代码
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
上述代码会抛出异常：java.lang.ArrayIndexOutOfBoundsException: Array index out of range: 9

## 2、ArrayList的subList修改是否影响list本身

- 方法实现
	```java
	// fromIndex: 集合开始的索引，toIndex:集合结束的索引，左开右闭
	public List<E> subList(int fromIndex， int toIndex) {
		// 边界校验
		subListRangeCheck(fromIndex， toIndex， size);
		// subList 返回是一个视图
		return new SubList(this， 0， fromIndex， toIndex);
	}
	// ArrayList 的内部类，这个类中单独定义了 set、get、size、add、remove 等方法
	private class SubList extends AbstractList<E> implements RandomAccess {
		private final AbstractList<E> parent; // parent的具体实现类是 ArrayList
		private final int parentOffset;
		private final int offset;
		int size;
		SubList(AbstractList<E> parent，int offset， int fromIndex， int toIndex) {
			this.parent = parent;
			this.parentOffset = fromIndex;
			this.offset = offset + fromIndex;
			this.size = toIndex - fromIndex;
			this.modCount = ArrayList.this.modCount;
		}
		public E set(int index, E e) {
            rangeCheck(index);
            checkForComodification();
            E oldValue = ArrayList.this.elementData(offset + index);
            ArrayList.this.elementData[offset + index] = e;
            return oldValue;
        }
        public E get(int index) {
            rangeCheck(index);
            checkForComodification();
            return ArrayList.this.elementData(offset + index);
        }
        public int size() {
            checkForComodification();
            return this.size;
        }
        public void add(int index, E e) {
            rangeCheckForAdd(index);
            checkForComodification();
			// 添加直接调用父类的添加元素的方法
            parent.add(parentOffset + index, e);
			// subList 添加的元素后，会同步父集合的modCount 修改到 subList的modCount，
            this.modCount = parent.modCount;
            this.size++;
        }
        public E remove(int index) {
            rangeCheck(index);
            checkForComodification();
            E result = parent.remove(parentOffset + index);
            this.modCount = parent.modCount;
            this.size--;
            return result;
        }
		private void checkForComodification() {
            if (ArrayList.this.modCount != this.modCount)
                throw new ConcurrentModificationException();
        }
	}
	```
	subList 可以做集合的任何操作
- 调用该方法后的生成的新的集合的操作都会对原集合有影响，在subList集合后面添加元素，添加的第一个元素的位置就是上述toIndex的值，而原始集合中toIndex的元素往后移动。其add方法调用过程：

	`add(element) --> AbstractList.add(e) --> SubList.add(index， e) --> parent.add(index + parentOffset， e) --> ArrayList.add(newIndex， e)`
	
- List 的 subList 方法并没有创建一个新的 List，而是使用了原List的视图，这个视图使用内部类 SubList 表示；不能把 subList 方法返回的 List 强制转换成 ArrayList 等类，因为他们之间没有继承关系；

视图和原 List 的修改还需要注意几点，尤其是他们之间的相互影响：
- 对 父 (sourceList) 子 (subList)List 做的非结构性修改(non-structural changes)，都会影响到彼此；
- 对`子List` 做结构性修改，操作同样会反映到`父List` 上；子List的 add 是直接调用父集合的add方法来添加的元素的：
	```java
	public void add(int index, E e) {
		rangeCheckForAdd(index);
		checkForComodification();
		parent.add(parentOffset + index, e);
		this.modCount = parent.modCount;
		this.size++;
	}
	```
- 对`父List` 做结构性修改（增加、删除），均会导致`子List`的遍历、增加、删除抛出异常 ConcurrentModificationException；因为其迭代的时候会对比`父List的modCount`和`子集合的modCount`：
	```java
	private void checkForComodification() {
		// ArrayList.this.modCount 表示父List的 modCount，this.modCount表示 子List的modCount
		if (ArrayList.this.modCount != this.modCount)
			throw new ConcurrentModificationException();
	}
	```

## 3、为什么最好在new ArrayList的时候最好指定容量？

避免频繁的扩容导致的数据拷贝

## 4、SynchronizedList、Vector有什么区别

- SynchronizedList 是java.util.Collections的静态内部类；Vector是java.util包中的一个类；
- 使用add方法时，扩容机制不一样；
- SynchronizedList有很好的扩展和兼容功能，可以将所有的List的子类转成线程安全的类；
- 使用SynchronizedList的时候，进行遍历时需要手动进行同步处理；
- SynchronizedList可以指定锁的对象

## 5、Arrays.asList(T...args)获得的List特点

- 其返回的List是Arrays的一个内部类，是原来数组的视图，不支持增删操作；
- 如果需要对其进行操作的话，可以通过ArrayList的构造器将其转为ArrayList；

## 6、Iterator和ListIterator区别

- 都是用于遍历集合的，Iterator可以用于遍历Set、List；ListIterator只可用于List；
- ListIterator实现的Iterator接口；
- ListIterator可向前和向后遍历；Iterator只可向后遍历；
