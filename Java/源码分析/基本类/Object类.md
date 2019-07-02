<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [1、Object 类中 clone() 方法](#1object-%E7%B1%BB%E4%B8%AD-clone-%E6%96%B9%E6%B3%95)
  - [1.1、作用](#11%E4%BD%9C%E7%94%A8)
  - [1.2、clone()工作原理](#12clone%E5%B7%A5%E4%BD%9C%E5%8E%9F%E7%90%86)
  - [1.3、什么情况下需要覆盖clone()方法呢](#13%E4%BB%80%E4%B9%88%E6%83%85%E5%86%B5%E4%B8%8B%E9%9C%80%E8%A6%81%E8%A6%86%E7%9B%96clone%E6%96%B9%E6%B3%95%E5%91%A2)
  - [1.4、浅克隆](#14%E6%B5%85%E5%85%8B%E9%9A%86)
  - [1.5、深克隆](#15%E6%B7%B1%E5%85%8B%E9%9A%86)
  - [1.6、序列化实现对象的拷贝](#16%E5%BA%8F%E5%88%97%E5%8C%96%E5%AE%9E%E7%8E%B0%E5%AF%B9%E8%B1%A1%E7%9A%84%E6%8B%B7%E8%B4%9D)
  - [1.7、String的clone的特殊性以及StringBuilder和StringBuffer](#17string%E7%9A%84clone%E7%9A%84%E7%89%B9%E6%AE%8A%E6%80%A7%E4%BB%A5%E5%8F%8Astringbuilder%E5%92%8Cstringbuffer)
  - [1.8、Java中集合的克隆](#18java%E4%B8%AD%E9%9B%86%E5%90%88%E7%9A%84%E5%85%8B%E9%9A%86)
- [2、Object 中 equals()方法](#2object-%E4%B8%AD-equals%E6%96%B9%E6%B3%95)
- [3、hashCode()方法](#3hashcode%E6%96%B9%E6%B3%95)
- [4、finalize()方法](#4finalize%E6%96%B9%E6%B3%95)
- [5、toString()方法：](#5tostring%E6%96%B9%E6%B3%95)
- [6、wait/notifAll](#6waitnotifall)
  - [6.1、wait](#61wait)
  - [6.2、notify](#62notify)
  - [6.3、notifyAll](#63notifyall)
  - [6.4、wait/notify存在的一些问题](#64waitnotify%E5%AD%98%E5%9C%A8%E7%9A%84%E4%B8%80%E4%BA%9B%E9%97%AE%E9%A2%98)
- [7、registerNatives](#7registernatives)
- [8、getClass：其定义：](#8getclass%E5%85%B6%E5%AE%9A%E4%B9%89)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

* Object 类是 Java 中的终极父类，任何类都默认继承Object类，然而接口是不继承Object类;
* ????为什么接口不继承Object类????

# 1、Object 类中 clone() 方法

## 1.1、作用

clone()可以产生一个相同的类并且返回给调用者.

## 1.2、clone()工作原理

Object将clone()作为一个本地方法来实现，这意味着它的代码存放在本地的库中；当代码执行的时候，将会检查调用对象的类(或者父类)是否实现了java.lang.Cloneable接口(Object类不实现Cloneable)；如果没有实现这个接口，clone()将会抛出一个检查异常()——java.lang.Clon eNotSupportedException，如果实现了这个接口，clone()会创建一个新的对象，并将原来对象的内容复制到新对象，最后返回这个新对象的引用	
```java
public class CloneDemo implements Cloneable {
	int x;

	public static void main(String[] args) throws CloneNotSupportedException {
		CloneDemo cd = new CloneDemo();
		cd.x = 5;
		System.out.printf("cd.x = %d%n"， cd.x);
		CloneDemo cd2 = (CloneDemo) cd.clone();
		System.out.printf("cd2.x = %d%n"， cd2.x);
	}
}
```
## 1.3、什么情况下需要覆盖clone()方法呢
调用clone()的代码是位于被克隆的类(即CloneDemo类)里面的，所以就不需要覆盖clone()了。但是，如果调用别的类中的clone()，就需要覆盖clone()了。否则，将会看到“clone在Object中是被保护的”

```java
// 提示：因为clone()在Object中的权限是protected	
class Data implements Cloneable {
	int x;

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}

public class CloneDemo {
	public static void main(String[] args) throws CloneNotSupportedException {
		Data data = new Data();
		data.x = 5;
		System.out.printf("data.x = %d%n"， data.x);
		Data data2 = (Data) data.clone();
		System.out.printf("data2.x = %d%n"， data2.x);
	}
}
```

## 1.4、浅克隆

- 浅克隆(也叫做浅拷贝)仅复制了这个对象本身的成员变量，该对象如果引用了其他对象的话，也不对其复制。上述代码演示了浅克隆；新的对象中的数据包含在了这个对象本身中，不涉及对别的对象的引用；
- 如果一个对象中的所有成员变量都是原始类型，并且其引用了的对象都是不可改变的(大多情况下都是)时，使用浅克隆效果很好！但是，如果其引用了可变的对象，那么这些变化将会影响到该对象和它克隆出的所有对象。

***浅克隆在复制引用了可变对象的对象时存在着问题，克隆后的对象修改，同样会影响到被克隆的对象***
	
## 1.5、深克隆
会复制这个对象和它所引用的对象的成员变量，如果该对象引用了其他对象，深克隆也会对其复制;
```java
public class Address {
	private String city;
	Address(String city) {
		this.city = city;
	}
	@Override
	public Address clone() {
		return new Address(new String(city));
	}
	String getCity() {
		return city;
	}
	void setCity(String city) {
		this.city = city;
	}
}
public class Employee implements Cloneable {
	private String name;
	private int age;
	private Address address;
	Employee(String name， int age， Address address) {
		this.name = name;
		this.age = age;
		this.address = address;
	}
	@Override
	public Employee clone() throws CloneNotSupportedException {
		Employee e = (Employee) super.clone();
		e.address = address.clone();
		return e;
	}
	Address getAddress() {
		return address;
	}
	String getName() {
		return name;
	}
	int getAge() {
		return age;
	}
}
public class CloneDemo {
	public static void main(String[] args) throws CloneNotSupportedException {
		Employee e = new Employee("John Doe"， 49， new Address("Denver"));
		System.out.printf("%s： %d： %s%n"， e.getName()， e.getAge()， e
				.getAddress().getCity());
		Employee e2 = (Employee) e.clone();
		System.out.printf("%s： %d： %s%n"， e2.getName()， e2.getAge()， e2
				.getAddress().getCity());
		e.getAddress().setCity("Chicago");
		System.out.printf("%s： %d： %s%n"， e.getName()， e.getAge()， e
				.getAddress().getCity());
		System.out.printf("%s： %d： %s%n"， e2.getName()， e2.getAge()， e2
				.getAddress().getCity());
	}
}	
```
***注意：从Address类中的clone()函数可以看出，这个clone()和我们之前写的clone()有些不同：***

- Address类没有实现Cloneable接口。因为只有在Object类中的clone()被调用时才需要实现，而Address是不会调用clone()的，所以没有实现Cloneable()的必要。
- 这个clone()函数没有声明抛出CloneNotSupportedException。这个检查异常只可能在调用Object类clone()的时候抛出。clone()是不会被调用的，因此这个异常也就没有被处理或者传回调用处的必要了。
- Object类的clone()没有被调用(这里没有调用super.clone())。因为这不是对Address的对象进行浅克隆——只是一个成员变量复制而已。
- 为了克隆Address的对象，需要创建一个新的Address对象并对其成员进行初始化操作。最后将新创建的Address对象返回。

## 1.6、序列化实现对象的拷贝
内存中通过字节流的拷贝是比较容易实现的.把母对象写入到一个字节流中，再从字节流中将其读出来，这样就可以创建一个新的对象了，并且该新对象与母对象之间并不存在引用共享的问题，真正实现对象的深拷贝
```java
public class CloneUtils {
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T clone(T   obj){
		T cloneObj = null;
		try {
			//写入字节流
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream obs = new   ObjectOutputStream(out);
			obs.writeObject(obj);
			obs.close();

			//分配内存，写入原始对象，生成新对象
			ByteArrayInputStream ios = new  ByteArrayInputStream(out.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(ios);
			//返回生成的新对象
			cloneObj = (T) ois.readObject();
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cloneObj;
	}
}
```
## 1.7、String的clone的特殊性以及StringBuilder和StringBuffer

- 由于基本数据类型都能自动实现深度clone，引用类型默认实现的是浅度clone；而String是引用类型的一个特例，我们可以和操作基本数据类型一样认为其实现了深度 clone（实质是浅克隆，切记只是一个假象）.由于 String 是不可变类，对于 String 类中的很多修改操作都是通过新new对象复制处理的，所以当我们修改 clone 前后对象里面 String 属性的值时其实都是属性引用的重新指向操作，自然对 clone 前后对象里 String 属性是没有相互影响的，类似于深度克隆；所以虽然他是引用类型而且我们在深度克隆时无法调用其 clone 方法，但是其不影响我们深度克隆的使用；
- 如果要实现深度克隆则 StringBuffer 和 StringBuilder 是需要主动特殊处理的，否则就是真正的对象浅克隆，所以处理的办法就是在类的 clone 方法中对 StringBuffer 或者 StringBuilder 属性进行如下主动拷贝操作；

## 1.8、Java中集合的克隆
- 集合中默认克隆方式都是浅克隆，而且集合类提供的拷贝构造方式或addAll，add等方法都是浅克隆；就是说存储在原集合和克隆集合中的对象会保持一致并指向堆中同一内存地址。
```java
List<Person> destList = (List<Person>)srcList.clone();
List<Person> destList = new ArrayList<Person>(srcList.size());
for(Person person ： srcList){
	destList.add(person);
}
// 使用集合默认的 clone 方法复制（浅）
List<InfoBean> destList1 = (List<InfoBean>) srcList.clone();
// 使用 add 方法循环遍历复制（浅）
List<InfoBean> destList = new ArrayList<InfoBean>(srcList.size());
for (InfoBean bean ： srcList) {
	destList.add(bean);
}
// 使用 addAll 方法复制（浅）
List<InfoBean> destList2 = new ArrayList<InfoBean>();
destList.addAll(srcList);
// 使用构造方法复制（浅）
List<InfoBean> destList3 = new ArrayList<InfoBean>(srcList);
// 使用System.arraycopy()方法复制（浅）
InfoBean[] srcBeans = srcList.toArray(new InfoBean[0]);
InfoBean[] destBeans = new InfoBean[srcBeans.length];
System.arraycopy(srcBeans， 0， destBeans， 0， srcBeans.length);
```
- 集合实现深克隆的方法

==> 序列化：
```java
public static <T extends Serializable> List<T> deepCopy(List<T> src) throws IOException， ClassNotFoundException {
	ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
	ObjectOutputStream objOut = new ObjectOutputStream(byteOut);

	objOut.writeObject(src);

	ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
	ObjectInputStream objIn = new ObjectInputStream(byteIn);
	return (List<T>) objIn.readObject();
}
```
==> 集合中实体类实现 Cloneable 接口，拷贝时逐个拷贝克隆：destList.add((InfoBean)srcLisdt.get(index).clone());

# 2、Object 中 equals()方法
```java
public boolean equals(Object obj){
	return (this == obj);
}
```
基本原则：一致性、传递性、对称性、自反性、【对于任意的非空引用值x，x.equals(null)必须返回假】

- **2.1、用途：用来检查一个对象与调用这个equals()的这个对象是否相等;**

	对象都拥有标识(内存地址)和状态(数据)，默认实现就是使用"=="比较，即比较两个对象的内存地址
	
- **2.2、为什么不用“==”运算符来判断两个对象是否相等呢？**
	- 虽然“==”运算符可以比较两个数据是否相等，但是要来比较对象的话，恐怕达不到预期的结果。就是说，“==”通过是否引用了同一个对象来判断两个对象是否相等，这被称为“引用相等”。这个运算符不能通过比较两个对象的内容来判断它们是不是逻辑上的相等。	
	- 使用Object的equals()方法比较的依据：调用它的对象和传入的对象的引用是否相等；也就是说，默认的equals()方法进行的是引用比较，如果相同引用返回true，否则返回false;
	
- **2.3、equals()和继承：在重写 equals的时候推荐使用getClass进行类型判断**
	```java
	class Employee {
		private String name;
		private int age;

		Employee(String name， int age) {
			this.name = name;
			this.age = age;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Employee))
				return false;

			Employee e = (Employee) o;
			return e.getName().equals(name) && e.getAge() == age;
		}

		String getName() {
			return name;
		}

		int getAge() {
			return age;
		}
	}
	```
	当Employee类被继承的时候，上述代码就存在问题：假如SaleRep类继承了Employee类，这个类中也有基于字符串类型的变量，equals()可以对其进行比较。假设你创建的Employee对象和SaleRep对象都有相同的“名字”和“年龄”。但是，SaleRep中还是添加了一些内容;会违背传递性原则

- **2.4、在 java 中进行比较，我们需要根据比较的类型来选择合适的比较方式：**

	- 对象域，使用 equals 方法;
	- 类型安全的枚举，使用 equals 或==;
	- 可能为 null 的对象域 ： 使用 == 和 equals;
	- 数组域 ： 使用 Arrays.equals
	- 除 float 和 double 外的原始数据类型 ： 使用 == 
	- float 类型： 使用 Float.foatToIntBits 转换成 int 类型，然后使用==，float 重写的equals()：
		- ①.当且仅当参数不是 null 而是 Float 对象时，且表示 Float 对象的值相同，结果为 true，
		- ②.当且仅当将方法 foatToIntBits 应用于两个值所返回的 int 值相同时，才认为两个值相同;
		- ③.注意：在大多数情况下，对于 Float 的两个实例 f1 和 f2，当且仅当 f1.floatValue() == f2.floatValue() 为 true 时，f1.equals(f2)的值才为 true ，但是存在两种例外情况：
			- 如果 f1 和 f2 都表示 Float.NAN，那么即使 Float.NaN == Float.NaN 的值为 false， equals方法也返回 true;
			- 如果 f1 表示 +0.0f， 而 f2 表示 -0.0f，或相反，那么即使 0.0f == -0.0f 的值为 true，equals方法也返回 false<br>
		这样情况下使得哈希表得意正确操作
	- double 类型： 使用 Double.doubleToLongBit 转换成 long 类型，然后使用==。
		理由同上

# 3、hashCode()方法

- 用途： hashCode()方法返回给调用者此对象的哈希码(其值由一个hash函数计算得来)；这个方法通常用在基于hash的集合类中，像java.util.HashMap，java.until.HashSet和java.util.Hashtable
- 在覆盖equals()时，同时覆盖hashCode()：保证对象的功能兼容于hash集合
- hashCode()方法的规则：
	- 在同一个Java程序中，对一个相同的对象，无论调用多少次hashCode()，hashCode()返回的整数必须相同，因此必须保证equals()方法比较的内容不会更改.但不必在另一个相同的Java程序中也保证返回值相同;
	- 如果两个对象用equals()方法比较的结果是相同的，那么这两个对象调用hashCode()应该返回相同的整数值

		假如两个Java对象A和B，A和B相等（eqauls结果为true），但A和B的哈希码不同，则A和B存入HashMap时的哈希码计算得到的HashMap内部数组位置索引可能不同，那么A和B很有可能允许同时存入HashMap，显然相等/相同的元素是不允许同时存入HashMap，HashMap不允许存放重复元素

	- 当两个对象使用equals()方法比较的结果是不同的，hashCode()返回的整数值可以不同。然而，hashCode()的返回值不同可以提高哈希表的性能。

**重写equals方法必须重写hashCode方法：**如果不这样做，那么在使用基于散列的的集合时，无法正常运行，如：HashMap、HashSet、HashTable；

# 4、finalize()方法

[详解finalize方法](../../Java虚拟机/JVM-GC垃圾回收机制.md#六详解-finalize方法)

finalize()方法不会被调用第二次；finalize()方法对于虚拟机来说不是轻量级的程序;

- 4.1、用途： 

	finalize()方法可以被子类对象所覆盖，然后作为一个终结者，当GC被调用的时候完成最后的清理工作(例如释放系统资源之类)；这就是终止。默认的finalize()方法什么也不做，当被调用时直接返回;

- 4.2、避免使用finalize()方法：

	相对于其他JVM实现，终结器被调用的情况较少——可能是因为终结器线程的优先级别较低的原因。如果你依靠终结器来关闭文件或者其他系统资源，可能会将资源耗尽，当程序试图打开一个新的文件或者新的系统资源的时候可能会崩溃，就因为这个缓慢的终结器
	
- 4.3、finalize()方法可以作为一个安全保障，以防止声明的终结方法未被调用	

	如何实现finalize()方法：子类终结器一般会通过调用父类的终结器来实现	
	```java
	@Override
	protected void finalize() throws Throwable{
		try{
			// Finalize the subclass state.
			// ...
		}
		finally{
			super.finalize();
		}
	}
	```
- 4.4、finalize方法存在问题

不建议使用finalize，在Java9甚至将该方法标记为deprecated，如果没有特殊原因，不要实现finalize方法，为什么？因为无法保证finalize什么时候执行，执行的是否符合预期。使用不当会影响性能，导致程序死锁、挂起

*为什么导致这些问题呢？*

finalize的执行是和垃圾收集关联在一起的，一旦实现了非空的finalize方法的对象是个“特殊公民”，JVM要对它进行额外处理。finalize本质上成为了快速回收的阻碍者，可能导致你的对象经过多个垃圾收集周期才回收；

对于重载了 Object 类的 finalize 方法的类实例化的对象（这里称为 f 对象），JVM 为了能在 GC 对象时触发 f 对象的 finalize 方法的调用，将每个 f 对象包装生成一个对应的 FinalReference 对象，方便 GC 时进行处理

实践中因为finalize拖慢垃圾收集，导致大量对象堆积，也是一种典型的OOM的原因；

从另一个角度，需要确保回收资源是因为资源有限，垃圾收集的时间不可预测，可能极大加剧资源占用，推荐资源用完即释放或者利用资源池来尽量重用；finalize还会掩盖资源回收时的出错信息，看如下代码：
```java
private void runFinalizer(JavaLangAccess jla) {
	synchronized (this) {
		if (hasBeenFinalized()) return;
		remove();
	}
	try {
		Object finalizee = this.get();
		if (finalizee != null && !(finalizee instanceof java.lang.Enum)) {
			jla.invokeFinalize(finalizee);

			/* Clear stack slot containing this variable, to decrease
				the chances of false retention with a conservative GC */
			finalizee = null;
		}
	} catch (Throwable x) { } // 异常信息被吞掉，意味着一旦出错或异常，得不到任何有效的信息。
	super.clear();
}
```

- 4.5、finalize的替代机制

Java平台目前正在使用Cleaner来替换掉原来的finalize实现。Cleaner实现利用了幻象引用，这是一种常见所谓post-mortem清理机制。

吸取finalize的教训，每个Cleaner的操作都是独立的，它有自己的运行线程，可以避免死锁；

# 5、toString()方法：

当编译器遇到 name + "： " + age 的表达时，会生成一个 java.lang.StringBuilder 对象，并调用 append() 方法来对字符串添加变量值和分隔符。最后调用 toString() 方法返回一个包含各个元素的字符串对象

# 6、wait/notifAll

## 6.1、wait

该方法用来将当前线程置入休眠状态，直到接到通知或被中断为止；在调用 wait()之前，线程必须要获得该对象的对象监视器锁，即只能在同步方法或同步块中调用 wait()方法；调用wait()方法之后，当前线程会释放锁。如果调用wait()方法时，线程并未获取到锁的话，则会抛出`IllegalMonitorStateException`异常，这是以个RuntimeException。如果再次获取到锁的话，当前线程才能从wait()方法处成功返回

**为什么wait()方法要放在同步块中？**

- 如果wait()方法不在同步块中，代码的确会抛出异常：IllegalMonitorStateException；
- Java设计者为了避免使用者出现lost wake up问题而搞出来的；
- 首先看`Lost Wake-Up Problem`，该问题是会在所有的多线程环境下出现；为了避免不经意间出现这种lost wake up问题，包括java.util.concurrent.locks.Condition的await()/signal()也必须要在同步块中；一定要处于锁对象的同步块中；下面的代码一样出现`IllegalMonitorStateException`
	```java
	private Object obj = new Object();
	private Object another = new Object();
	public void produce(){
		// 因为锁住的是obj对象，而调用notify是another对象
		synchronized(obj){
			try{
				another.notify();
			} catch(Exception e){

			}
		}
	}
	```
- wait和sleep的区别：
	- sleep: 是 Thread 类的静态方法，调用此方法会让当前线程暂停执行指定的时间，将执行机会(CPU)让给其他线程，但是对象的锁依然保持，因此休眠结束后会自动恢复；
	- wait: 是 Object 类的方法；调用对象的 wait 方法导致当前线程放弃对象的锁(线程暂停执行)，进入对象的等待池，只有调用的对象的 notify(notifyAll)时才能唤醒等待池中的线程进入等锁池,如果线程重新获得对象的锁就可以进入就绪状态:
	
	***为什么 wait 方法要定义在 Object 类中?***
	
	因为这些方法在操作同步线程时，都必须要标识它们操作线程的锁，只有同一个锁上的被等待线程，可以被同一个锁上的notify唤醒，不可以对不同锁中的线程进行唤醒，等待和唤醒必须是同一个锁。而锁可以是任意对象，所以可以被任意对象调用的方法是定义在 Object 类中。而jdk1.5以后提供了 Lock 接口和 Condition 对象，`Condition 中的await(), signal().signalAll()`代替 `Object 中的wait(),notify(),notifyAll()`

## 6.2、notify

该方法也要在同步方法或同步块中调用，即在调用前，线程也必须要获得该对象的对象级别锁，如果调用 notify()时没有持有适当的锁，也会抛出 IllegalMonitorStateException；

该方法任意从WAITTING状态的线程中挑选一个进行通知，使得调用wait()方法的线程从等待队列移入到同步队列中，等待有机会再一次获取到锁，从而使得调用wait()方法的线程能够从wait()方法处退出。

调用notify后，当前线程不会马上释放该对象锁，要等到程序退出同步块后，当前线程才会释放锁。

## 6.3、notifyAll

该方法与 notify ()方法的工作方式相同，重要的一点差异是：

notifyAll 使所有原来在该对象上 wait 的线程统统退出WAITTING状态，使得他们全部从等待队列中移入到同步队列中去，等待下一次能够有机会获取到对象监视器锁

## 6.4、wait/notify存在的一些问题

### 6.4.1、notify早期通知

notify通知的遗漏很容易理解，即threadA还没开始wait的时候，发生上下文切换，threadB已经notify了，这样，threadB通知是没有任何响应的，当 threadB 退出 synchronized 代码块后，threadA 再开始 wait，便会一直阻塞等待，直到被别的线程打断：Lost Wake-Up Problem问题
```java
public class EarlyNotify {
    private static String lockObject = "";
    public static void main(String[] args) {
        WaitThread w = new WaitThread(lockObject);
        NotifyThread n = new NotifyThread(lockObject);
        n.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        w.start();
    }
    static class WaitThread extends Thread {
        private String lock;
        public WaitThread(String lock) {
            this.lock = lock;
        }
        @Override
        public void run() {
            synchronized (lock) {
                try {
                    System.out.println(Thread.currentThread().getName() + " 进去代码块");
                    System.out.println(Thread.currentThread().getName() + " 开始wait");
                    lock.wait();
                    System.out.println(Thread.currentThread().getName() + " 结束wait");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    static class NotifyThread extends Thread {
        private String lock;
        public NotifyThread(String lock) {
            this.lock = lock;
        }
        @Override
        public void run() {
            synchronized (lock) {
                try {
                    System.out.println(Thread.currentThread().getName() + " 进去代码块");
                    System.out.println(Thread.currentThread().getName() + " 开始notify");
                    lock.notify();
                    System.out.println(Thread.currentThread().getName() + " 结束notify");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

```
上述代码中，示例中开启了**两个线程，一个是WaitThread，另一个是NotifyThread。NotifyThread会先启动，先调用notify方法。然后WaitThread线程才启动，调用wait方法，但是由于通知过了，wait方法就无法再获取到相应的通知，因此WaitThread会一直在wait方法出阻塞，这种现象就是通知过早的现象；

```java
public class ResolveEarlyNotify {
    private static String lockObject = "";
    private static boolean isWait = true;
    public static void main(String[] args) {
        WaitThread w = new WaitThread(lockObject);
        NotifyThread n = new NotifyThread(lockObject);
        n.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        w.start();
    }
    static class WaitThread extends Thread {
        private String lock;
        public WaitThread(String lock) {
            this.lock = lock;
        }
        @Override
        public void run() {
            synchronized (lock) {
                try {
                    while (isWait) {
                        System.out.println(Thread.currentThread().getName() + " 进去代码块");
                        System.out.println(Thread.currentThread().getName() + " 开始wait");
                        lock.wait();
                        System.out.println(Thread.currentThread().getName() + " 结束wait");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    static class NotifyThread extends Thread {
        private String lock;
        public NotifyThread(String lock) {
            this.lock = lock;
        }
        @Override
        public void run() {
            synchronized (lock) {
                try {
                    System.out.println(Thread.currentThread().getName() + " 进去代码块");
                    System.out.println(Thread.currentThread().getName() + " 开始notify");
                    lock.notifyAll();
                    isWait = false;
                    System.out.println(Thread.currentThread().getName() + " 结束notify");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```
在使用线程的等待/通知机制时，一般都要配合一个 boolean 变量值（或者其他能够判断真假的条件），在 notify 之前改变该 boolean 变量的值，让 wait 返回后能够退出 while 循环（一般都要在 wait 方法外围加一层 while 循环，以防止早期通知），或在通知被遗漏后，不会被阻塞在 wait 方法处。这样便保证了程序的正确性

### 6.4.2、wait等待条件发生变化

如果线程在等待时接受到了通知，但是之后等待的条件发生了变化，并没有再次对等待条件进行判断，也会导致程序出现错误

在使用线程的等待/通知机制时，一般都要在while循环中调用wait()方法，因此需要配合使用一个boolean变量（或其他能判断真假的条件），满足while循环的条件时，进入while循环，执行wait()方法，不满足while循环的条件时，跳出循环，执行后面的代码；

### 6.4.3、“假死”状态

- 现象：如果是多消费者和多生产者情况，如果使用notify方法可能会出现“假死”的情况，即唤醒的是同类线程。
- 原因分析：假设当前多个生产者线程会调用wait方法阻塞等待，当其中的生产者线程获取到对象锁之后使用notify通知处于WAITTING状态的线程，如果唤醒的仍然是生产者线程，就会造成所有的生产者线程都处于等待状态。
- 解决办法：将notify方法替换成notifyAll方法，如果使用的是lock的话，就将signal方法替换成signalAll方法

## 6.5、wait/notify通知机制使用条件

- 永远在while循环中对条件进行判断而不是if语句中进行wait条件的判断；
- 使用notifyAll而不是notify

基本使用范式：
```java
// The standard idiom for calling the wait method in Java 
synchronized (sharedObject) { 
    while (condition) { 
    sharedObject.wait(); 
        // (Releases lock, and reacquires on wakeup) 
    } 
    // do action based upon condition e.g. take or put into queue 
}
```

# 7、registerNatives

# 8、getClass：其定义：
	public final native Class<?> getClass();
	final 的方法，不可重写


# 参考文章

* [生产者消费者](https://juejin.im/post/5aeec675f265da0b7c072c56)