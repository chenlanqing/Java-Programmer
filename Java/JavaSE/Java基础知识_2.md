<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [十一、Java中length和length()的区别](#%E5%8D%81%E4%B8%80java%E4%B8%ADlength%E5%92%8Clength%E7%9A%84%E5%8C%BA%E5%88%AB)
- [十二、数组](#%E5%8D%81%E4%BA%8C%E6%95%B0%E7%BB%84)
  - [1、Java 中数组是对象吗](#1java-%E4%B8%AD%E6%95%B0%E7%BB%84%E6%98%AF%E5%AF%B9%E8%B1%A1%E5%90%97)
  - [2、Java中数组的类型](#2java%E4%B8%AD%E6%95%B0%E7%BB%84%E7%9A%84%E7%B1%BB%E5%9E%8B)
  - [3、Java中数组的继承关系](#3java%E4%B8%AD%E6%95%B0%E7%BB%84%E7%9A%84%E7%BB%A7%E6%89%BF%E5%85%B3%E7%B3%BB)
  - [4、Java 数组初始化](#4java-%E6%95%B0%E7%BB%84%E5%88%9D%E5%A7%8B%E5%8C%96)
  - [5、数组扩容](#5%E6%95%B0%E7%BB%84%E6%89%A9%E5%AE%B9)
  - [6、数组复制问题](#6%E6%95%B0%E7%BB%84%E5%A4%8D%E5%88%B6%E9%97%AE%E9%A2%98)
  - [7、数组转换为 List](#7%E6%95%B0%E7%BB%84%E8%BD%AC%E6%8D%A2%E4%B8%BA-list)
- [十三、switch](#%E5%8D%81%E4%B8%89switch)
  - [1、支持类型](#1%E6%94%AF%E6%8C%81%E7%B1%BB%E5%9E%8B)
  - [2、switch 对整型的支持](#2switch-%E5%AF%B9%E6%95%B4%E5%9E%8B%E7%9A%84%E6%94%AF%E6%8C%81)
  - [3、switch 对字符型支持的实现](#3switch-%E5%AF%B9%E5%AD%97%E7%AC%A6%E5%9E%8B%E6%94%AF%E6%8C%81%E7%9A%84%E5%AE%9E%E7%8E%B0)
  - [4、switch 对字符串支持的实现](#4switch-%E5%AF%B9%E5%AD%97%E7%AC%A6%E4%B8%B2%E6%94%AF%E6%8C%81%E7%9A%84%E5%AE%9E%E7%8E%B0)
  - [5、枚举类](#5%E6%9E%9A%E4%B8%BE%E7%B1%BB)
- [十四、抽象类与接口](#%E5%8D%81%E5%9B%9B%E6%8A%BD%E8%B1%A1%E7%B1%BB%E4%B8%8E%E6%8E%A5%E5%8F%A3)
  - [1、抽象类](#1%E6%8A%BD%E8%B1%A1%E7%B1%BB)
  - [2、接口](#2%E6%8E%A5%E5%8F%A3)
  - [3、接口与抽象类的区别](#3%E6%8E%A5%E5%8F%A3%E4%B8%8E%E6%8A%BD%E8%B1%A1%E7%B1%BB%E7%9A%84%E5%8C%BA%E5%88%AB)
  - [4、Java8下接口的不同之处](#4java8%E4%B8%8B%E6%8E%A5%E5%8F%A3%E7%9A%84%E4%B8%8D%E5%90%8C%E4%B9%8B%E5%A4%84)
- [十五、类型、类初始化、二进制](#%E5%8D%81%E4%BA%94%E7%B1%BB%E5%9E%8B%E7%B1%BB%E5%88%9D%E5%A7%8B%E5%8C%96%E4%BA%8C%E8%BF%9B%E5%88%B6)
  - [1、基本类型与引用类型的比较](#1%E5%9F%BA%E6%9C%AC%E7%B1%BB%E5%9E%8B%E4%B8%8E%E5%BC%95%E7%94%A8%E7%B1%BB%E5%9E%8B%E7%9A%84%E6%AF%94%E8%BE%83)
  - [2、关于String +和StringBuffer的比较](#2%E5%85%B3%E4%BA%8Estring-%E5%92%8Cstringbuffer%E7%9A%84%E6%AF%94%E8%BE%83)
  - [3、静态代码块、静态变量](#3%E9%9D%99%E6%80%81%E4%BB%A3%E7%A0%81%E5%9D%97%E9%9D%99%E6%80%81%E5%8F%98%E9%87%8F)
  - [4、给出一个表达式计算其可以按多少进制计算](#4%E7%BB%99%E5%87%BA%E4%B8%80%E4%B8%AA%E8%A1%A8%E8%BE%BE%E5%BC%8F%E8%AE%A1%E7%AE%97%E5%85%B6%E5%8F%AF%E4%BB%A5%E6%8C%89%E5%A4%9A%E5%B0%91%E8%BF%9B%E5%88%B6%E8%AE%A1%E7%AE%97)
  - [5、表达式的数据类型](#5%E8%A1%A8%E8%BE%BE%E5%BC%8F%E7%9A%84%E6%95%B0%E6%8D%AE%E7%B1%BB%E5%9E%8B)
  - [6、多态问题](#6%E5%A4%9A%E6%80%81%E9%97%AE%E9%A2%98)
- [十六、反射与注解](#%E5%8D%81%E5%85%AD%E5%8F%8D%E5%B0%84%E4%B8%8E%E6%B3%A8%E8%A7%A3)
  - [1、Java注解：Annotation](#1java%E6%B3%A8%E8%A7%A3annotation)
  - [2、Java 动态加载与静态加载](#2java-%E5%8A%A8%E6%80%81%E5%8A%A0%E8%BD%BD%E4%B8%8E%E9%9D%99%E6%80%81%E5%8A%A0%E8%BD%BD)
  - [3、反射机制：(Reflection)](#3%E5%8F%8D%E5%B0%84%E6%9C%BA%E5%88%B6reflection)
  - [4、动态编译：Java6.0引入动态编译](#4%E5%8A%A8%E6%80%81%E7%BC%96%E8%AF%91java60%E5%BC%95%E5%85%A5%E5%8A%A8%E6%80%81%E7%BC%96%E8%AF%91)
  - [5、动态执行Javascript(JDK6.0以上)](#5%E5%8A%A8%E6%80%81%E6%89%A7%E8%A1%8Cjavascriptjdk60%E4%BB%A5%E4%B8%8A)
  - [6、Java 字节码操作](#6java-%E5%AD%97%E8%8A%82%E7%A0%81%E6%93%8D%E4%BD%9C)
  - [7、反射存在问题](#7%E5%8F%8D%E5%B0%84%E5%AD%98%E5%9C%A8%E9%97%AE%E9%A2%98)
- [十七、比较器：Comparale、Comparator](#%E5%8D%81%E4%B8%83%E6%AF%94%E8%BE%83%E5%99%A8comparalecomparator)
  - [1、区别](#1%E5%8C%BA%E5%88%AB)
  - [2、Comparable](#2comparable)
  - [3、Comparator](#3comparator)
  - [4、如何选择](#4%E5%A6%82%E4%BD%95%E9%80%89%E6%8B%A9)
- [十八、枚举类](#%E5%8D%81%E5%85%AB%E6%9E%9A%E4%B8%BE%E7%B1%BB)
  - [1、枚举类概念](#1%E6%9E%9A%E4%B8%BE%E7%B1%BB%E6%A6%82%E5%BF%B5)
  - [2、枚举类本质](#2%E6%9E%9A%E4%B8%BE%E7%B1%BB%E6%9C%AC%E8%B4%A8)
  - [3、枚举类与常量](#3%E6%9E%9A%E4%B8%BE%E7%B1%BB%E4%B8%8E%E5%B8%B8%E9%87%8F)
  - [4、枚举类是如何保证线程安全的](#4%E6%9E%9A%E4%B8%BE%E7%B1%BB%E6%98%AF%E5%A6%82%E4%BD%95%E4%BF%9D%E8%AF%81%E7%BA%BF%E7%A8%8B%E5%AE%89%E5%85%A8%E7%9A%84)
  - [5、枚举与单例模式](#5%E6%9E%9A%E4%B8%BE%E4%B8%8E%E5%8D%95%E4%BE%8B%E6%A8%A1%E5%BC%8F)
  - [6、迭代器和枚举器区别](#6%E8%BF%AD%E4%BB%A3%E5%99%A8%E5%92%8C%E6%9E%9A%E4%B8%BE%E5%99%A8%E5%8C%BA%E5%88%AB)
- [十九、Java异常](#%E5%8D%81%E4%B9%9Djava%E5%BC%82%E5%B8%B8)
  - [1、异常](#1%E5%BC%82%E5%B8%B8)
  - [2、Error](#2error)
  - [3、Exception](#3exception)
  - [4、Exception分类](#4exception%E5%88%86%E7%B1%BB)
  - [5、常见异常](#5%E5%B8%B8%E8%A7%81%E5%BC%82%E5%B8%B8)
- [二十、Jar包](#%E4%BA%8C%E5%8D%81jar%E5%8C%85)
  - [1、Jar包本质](#1jar%E5%8C%85%E6%9C%AC%E8%B4%A8)
  - [2、Jar包下META-INF作用](#2jar%E5%8C%85%E4%B8%8Bmeta-inf%E4%BD%9C%E7%94%A8)
  - [3、MANIFEST.MF 文件解析](#3manifestmf-%E6%96%87%E4%BB%B6%E8%A7%A3%E6%9E%90)
  - [4、Jar 包签名](#4jar-%E5%8C%85%E7%AD%BE%E5%90%8D)
  - [5、IDEA打jar包](#5idea%E6%89%93jar%E5%8C%85)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 十一、Java中length和length()的区别

- 1、获取数组的长度是使用属性 length，获取字符串长度是使用方法 length()
- 2、为什么数组有length属性?
	- 数组是一个容器对象，其中包含固定数量的同一类型的值.一旦数组被创建，那么数组的长度就是固定的了。数组的长度可以作为final实例变量的长度。因此，长度可以被视为一个数组的属性
 	- 有两种创建数组的方法：1、通过数组表达式创建数组。2、通过初始化值创建数组。无论使用哪种方式，一旦数组被创建，其大小就固定了
- 3、Java 中为什么没有定义一个类似 String 一样 Array 类：数组包含所有从 Object 继承下来方法，为什么没有一个array类呢?一个简单的解释是它被隐藏起来了
- 4、为什么 String 有length()方法？
	背后的数据结构是一个 char 数组，所以没有必要来定义一个不必要的属性(因为该属性在 char 数值中已经提供了)

# 十二、数组

## 1、Java 中数组是对象吗

- 什么是对象：

	语言层面：对象是根据某个类创建出来的一个实例，表示某类事物中一个具体的个体.对象具有各种属性，并且具有一些特定的行为计算机层面：对象就是内存中的一个内存块，在这个内存块封装了一些数据，也就是类中定义的各个属性

- 数组：

	语言层面上，数组不是某类事物中的一个具体的个体，而是多个个体的集合，那么数组应该不是对象而在计算机的角度，数组也是一个内存块，也封装了一些数据，这样的话也可以称之为对象

	```java
	int[] a = new int[4];
	//a.length;  //对属性的引用不能当成语句
	int len = a.length;  //数组中保存一个字段， 表示数组的长度		
	//以下方法说明数组可以调用方法，java中的数组是对象.
	//这些方法是Object中的方法，所以可以肯定，数组的最顶层父类也是Object
	a.clone();
	a.toString();
	```

这基本上可以认定，java中的数组也是对象，它具有java中其他对象的一些基本特点：封装了一些数据，可以访问属性，也可以调用方法.所以：Java数组是对象

而在C++中，数组虽然封装了数据，但数组名只是一个指针，指向数组中的首个元素，既没有属性，也没有方法可以调用；所以 C++中的数组不是对象，只是一个数据的集合，而不能当做对象来使用

## 2、Java中数组的类型

**2.1、虚拟机自动创建了数组类型，可以把数组类型和8种基本数据类型一样， 当做java的内建类型：**

- 每一维度用一个"["表示;开头两个"["，就代表是二维数组.
- "["后面是数组中元素的类型(包括基本数据类型和引用数据类型).

**2.2、String[] s = new String[4];**

- 在java语言层面上，s是数组，也是一个对象，那么他的类型应该是 String[]
- 在JVM中，他的类型为 [java.lang.String

## 3、Java中数组的继承关系

**3.1、数组的顶层父类也必须是 Object**

这就说明数组对象可以向上直接转型到 Object，也可以向下强制类型转换，也可以使用 instanceof 关键字做类型判定

```java
//1		在test1()中已经测试得到以下结论： 数组也是对象， 数组的顶层父类是Object， 所以可以向上转型
int[] a = new int[8];
Object obj = a ; //数组的父类也是Object，可以将a向上转型到Object		
//2		那么能向下转型吗?
int[] b = (int[])obj;  //可以进行向下转型		
//3		能使用instanceof关键字判定吗?
if(obj instanceof int[]){  //可以用instanceof关键字进行类型判定
	System.out.println("obj的真实类型是int[]");
}
```
**3.2、Java中数组的另一种"继承"关系**

String[] s = new String[5];<br>
Object[] obja = s;   //成立，说明可以用Object[]的引用来接收String[]的对象<br>
s的直接父类是?<br>
//5那么String[] 的直接父类是Object[] 还是 Object?  <br>
System.out.println(s.getClass().getSuperclass().getName());  <br>
//打印结果为java.lang.Object，说明String[] 的直接父类是 Object而不是Object[]  <br>

- 数组类直接继承了 Object，关于 Object[]类型的引用能够指向 String[]类型的对象，这并不是严格意义上的继承，String[] 不继承自 Object[]，但是可以允许 String[]向上转型到 Object[]；可以理解为：<br>
	其实这种关系可以这样表述：如果有两个类A和B，如果B继承(extends)了A，那么A[]类型的引用就可以指向B[]类型的对象

- 数组的这种用法不能作用于基本类型数据：

```java
int[] aa = new int[4];  
Object[] objaa = aa;  //错误的，不能通过编译  
因为 int 不是引用类型，Object 不是 int 的父类，在这里自动装箱不起作用
Object 数组中可以存放任何值，包括基本数据类型
public class ArrayTest {
	public static void main(String[] args) {
		test1();
		test2();
		test3();
	}
	/**
		* 数组具有这种特性：
		* 如果有两个类A和B，如果B继承(extends)了A，那么A[]类型的引用就可以指向B[]类型的对象
		* 测试数组的特殊特性对参数传递的便利性
		*/
	private static void test3() {
		String[] a = new String[3];
		doArray(a);
	}
	private static void doArray(Object[] objs){

	}
	private static void doArray1(Object obj){
		//不能用Object接收数组，因为这样无法对数组的元素进行访问
		// obj[1]  //错误

		//如果在方法内部对obj转型到数组，存在类型转换异常的风险
		// Object[] objs = (Object[]) obj;
	}
	private static void doArray2(String[] strs){
		//如果适用特定类型的数组，就限制了类型，失去灵活性和通用性
	}
	private static void doArray3(String name， int age， String id， float account){
		//如果不适用数组而是依次传递参数，会使参数列表变得冗长，难以阅读
	}
	/**
		* 测试数组的集成关系， 并且他的继承关系是否和数组中元素的类型有关
		*/
	private static void test2() {

		//1		在test1()中已经测试得到以下结论： 数组也是对象， 数组的顶层父类是Object， 所以可以向上转型
		int[] a = new int[8];
		Object obj = a ; //数组的父类也是Object，可以将a向上转型到Object

		//2		那么能向下转型吗?
		int[] b = (int[])obj;  //可以进行向下转型

		//3		能使用instanceof关键字判定吗?
		if(obj instanceof int[]){  //可以用instanceof关键字进行类型判定
			System.out.println("obj的真实类型是int[]");
		}

		//4  	下面代码成立吗?
		String[] s = new String[5];
		Object[] obja = s;   //成立，说明可以用Object[]的引用来接收String[]的对象

		//5		那么String[] 的直接父类是Object[] 还是 Object?
		System.out.println(s.getClass().getSuperclass().getName());
		//打印结果为java.lang.Object，说明String[] 的直接父类是 Object而不是Object[]

		//6	  下面成立吗?  Father是Son的直接父类
		Son[] sons = new Son[3];
		Father[] fa = sons;  //成立

		//7		那么Son[] 的直接父类是Father[] 还是  Object[] 或者是Object?
		System.out.println(sons.getClass().getSuperclass().getName());
		//打印结果为java.lang.Object，说明Son[]的直接父类是Object

		/**
			* 做一下总结， 如果A是B的父类， 那么A[] 类型的引用可以指向 B[]类型的变量
			* 但是B[]的直接父类是Object， 所有数组的父类都是Object
			*/

		//8		上面的结论可以扩展到二维数组
		Son[][] sonss = new Son[2][4];
		Father[][] fathers = sonss;
		//将Father[][]数组看成是一维数组， 这是个数组中的元素为Father[]
		//将Son[][]数组看成是一维数组， 这是个数组中的元素为Son[]
		//因为Father[]类型的引用可以指向Son[]类型的对象
		//所以，根据上面的结论，Father[][]的引用可以指向Son[][]类型的对象

		/**
			* 扩展结论：
			* 因为Object是所有引用类型的父类
			* 所以Object[]的引用可以指向任何引用数据类型的数组的对象. 如：
			* Object[] objs = new String[1];
			* Object[] objs = new Son[1];
			*
			*/

		//9		下面的代码成立吗?
		int[] aa = new int[4];
		//Object[] objaa = aa;  //错误的，不能通过编译
		//这是错误的， 因为Object不是int的父类，在这里自动装箱不起作用

		//10 	这样可以吗？
		Object[] objss = {"aaa"， 1， 2.5};//成立
	}

	/**
		* 测试在java语言中，数组是不是对象
		* 如果是对象， 那么他的类型是什么?
		*/
	private static void test1() {
		int[] a = new int[4];
		//a.length;  //对属性的引用不能当成语句
		int len = a.length;  //数组中保存一个字段， 表示数组的长度

		//以下方法说明数组可以调用方法，java中的数组是对象.这些方法是Object中的方法，所以可以肯定，数组的最顶层父类也是Object
		a.clone();
		a.toString();
		/**
			* java是强类型的语言，一个对象总会有一个特定的类型，例如 Person p = new Person();
			* 对象p(确切的说是引用)的类型是Person类， 这个Person类是我们自己编写的
			* 那么数组的类型是什么呢? 下面使用反射的方式进行验证
			*/
		int[] a1 = {1， 2， 3， 4};
		System.out.println(a1.getClass().getName());
		//打印出的数组类的名字为[I

		String[] s = new String[2];
		System.out.println(s.getClass().getName());
		//打印出的数组类的名字为  [Ljava.lang.String;

		String[][] ss = new String[2][3];
		System.out.println(ss.getClass().getName());
		//打印出的数组类的名字为    [[Ljava.lang.String;

		/**
			* 所以，数组也是有类型的，只不过这个类型不是有程序员自己定义的类， 也不是jdk里面
			* 的类， 而是虚拟机在运行时专门创建的类
			* 类型的命名规则是：
			* 		每一维度用一个[表示;
			* 		[后面是数组中元素的类型(包括基本数据类型和引用数据类型)
			*
			* 在java语言层面上，s是数组，也是一个对象，那么他的类型应该是String[]，
			* 但是在JVM中，他的类型为[java.lang.String
			*
			* 顺便说一句普通的类在JVM里的类型为 包名+类名， 也就是全限定名
			*/
	}				
	public static class Father {
	}				
	public static class Son extends Father {
	}
}
```

## 4、Java 数组初始化

Java 数组是静态的，即当数组被初始化之后，该数组的长度是不可变的;

## 5、数组扩容

可以参照利用 List 集合中的add方法模拟实现

```java
// datas 原始数组    newLen 扩容大小
public static <T> T[] expandCapacity(T[] datas，int newLen){
	newLen = newLen < 0 ? datas.length ：datas.length + newLen;   
	//生成一个新的数组
	return Arrays.copyOf(datas， newLen);
}
// datas  原始数组
public static <T> T[] expandCapacity(T[] datas){
	int newLen = (datas.length * 3) / 2;      //扩容原始数组的1.5倍
	//生成一个新的数组
	return Arrays.copyOf(datas， newLen);
}
// datas 原始数组    mulitiple 扩容的倍数
public static <T> T[] expandCapacityMul(T[] datas，int mulitiple){
	mulitiple = mulitiple < 0 ? 1 ： mulitiple;
	int newLen = datas.length * mulitiple;
	return Arrays.copyOf(datas，newLen );
}
```

## 6、数组复制问题

所以通过 Arrays.copyOf() 方法产生的数组是一个浅拷贝。同时数组的 clone() 方法也是，集合的 clone() 方法也是，所以我们在使用拷贝方法的同时一定要注意浅拷贝这问题

## 7、数组转换为 List

asList 返回的是一个长度不可变的列表。数组是多长，转换成的列表就是多长，我们是无法通过 add、remove 来增加或者减少其长度的

```java
public static void main(String[] args) {
	int[] datas = new int[]{1，2，3，4，5};
	List list = Arrays.asList(datas);
	System.out.println(list.size()); // 1
}
```

***为什么上述结果输出为 1？***

首先看 asList的源码：
```java
public static <T> List<T> asList(T... a) {
	return new ArrayList<T>(a);
}
```

- 注意这个参数：T…a，这个参数是一个泛型的变长参数，我们知道基本数据类型是不可能泛型化的，也是就说 8 个基本数据类型是不可作为泛型参数的，但是为什么编译器没有报错呢？这是因为在 Java 中，数组会当做一个对象来处理，它是可以泛型的，所以我们的程序是把一个 int 型的数组作为了 T 的类型，所以在转换之后 List 中就只会存在一个类型为 int 数组的元素了；

- 这里是直接返回一个 ArrayList 对象返回，但是注意这个 ArrayList 并不是 java.util.ArrayList，而是 Arrays 工具类的一个内之类，这个内部类并没有提供 add() 方法，那么查看父类 AbstractList仅仅只是提供了方法，方法的具体实现却没有，所以具体的实现需要子类自己来提供，但是非常遗憾这个内部类 ArrayList 并没有提供 add 的实现方法

size：元素数量、toArray：转换为数组，实现了数组的浅拷贝、get：获得指定元素、contains：是否包含某元素

# 十三、switch

## 1、支持类型

JDK7 之后，switch 的参数可以是 String 类型了;到目前为止 switch 支持的数据类型：byte(Byte)、short(Short)、int(Integer)、char(Character)、String、枚举类型

## 2、switch 对整型的支持

switch 对 int 的判断是直接比较整数的值：

由于 byte 的存储范围小于 int，可以向 int 类型进行隐式转换，所以 switch 可以作用在 byte 类型变量上；

由于 long 的存储范围大于 int，不能向 int 进行隐式转换，只能强制转换，所以 switch 不可以作用在 long 类型变量上。

对于包装类型，其使用的时候都是通过 byteValue，shortValue等来转换为基本类型

## 3、switch 对字符型支持的实现

对 char 类型进行比较的时候，实际上比较的是 Ascii 码，编译器会把 char 型变量转换成对应的 int 型变量。对于包装类型Character，其需要转换为基本类型char，转换方法：charValue

## 4、switch 对字符串支持的实现

### 4.1、代码**

```java
	public class switchDemoString {
		public static void main(String[] args) {
			String str = "world";
			switch (str) {
			case "hello"：
				System.out.println("hello");
				break;
			case "world"：
				System.out.println("world");
				break;
			default：
				break;
			}
		}
	}
```

### 4.2、反编译上述代码

```java
public class switchDemoString{
	public switchDemoString(){}
	public static void main(String args[]){
		String str = "world";
		String s;
		switch((s = str).hashCode()){
		default：
			break;
		case 99162322：
			if(s.equals("hello"))
				System.out.println("hello");
			break;
		case 113318802：
			if(s.equals("world"))
				System.out.println("world");
			break;
		}
	}
}
```

### 4.3、分析

字符串的 switch 是通过equals()和hashCode()方法来实现的
- switch 中只能使用整型，hashCode()方法返回的是int，而不是long
- 进行 switch 的实际是哈希值，然后通过使用equals方法比较进行安全检查，这个检查是必要的，因为哈希可能会发生碰撞
- 其实 switch 只支持一种数据类型，那就是整型，其他数据类型都是转换成整型之后在使用 switch 的

## 5、枚举类

枚举类型之所以能够使用，因为编译器层面实现了，编译器会将枚举 switch 转换为类似 `switch(s.ordinal()) { case Status.START.ordinal() }` 形式，所以实质还是 int 参数类型。可以通过查看反编译字节码来查看

# 十四、抽象类与接口

* [抽象类与接口](http://blog.csdn.net/chenssy/article/details/12858267)
* [深入理解Java的接口和抽象类](http://www.cnblogs.com/dolphin0520/p/3811437.html)

## 1、抽象类

如果一个类含有一个被 abstract 修饰的方法，那么该类就是抽象类，抽象类必须在类前用 abstract 关键字修饰

### 1.1、相关概念

- 抽象类体现了数据抽象的思想，是实现多态的一种机制；
- 抽象类的存在就是为了继承的，所以说抽象类就是用来继承的；

### 1.2、注意点

- （1）抽象类不能被实例化，实例化的工作应该交由它的子类来完成，它只需要有一个引用即可；
- （2）抽象方法必须为 public 或者 protected- （因为如果为 private 子类便无法实现该方法），缺省情况下默认为 public；
- （3）抽象类中可以包含具体的方法，当然也可以不包含抽象方法；
- （4）子类继承父类必须实现所有抽象方法，如果不实现需要将子类也定义为抽象类；
- （5）抽象类可以实现接口- （ implements ），可以不实现接口方法；
- （6）abstract 不能与 final 并列修饰同一个类- （因为被 final 修饰的类不能被继承）；
- （7）abstract 不能与 private、static、final 或 native 并列修饰同一个方法；
	- private 修饰的方法不能被子类所见，所以也就不能被子类所重写；
	- final 与类类似，final 修饰的方法不能被重写；
	- static 修饰的方法是类的方法，而抽象方法还没被实现；
	- native 是本地方法，不是由 Java 来实现的，
- （8）abstract 类中定义的抽象方法必须在具体的子类中实现，所以不能有抽象构造方法或抽象静态方法；

## 2、接口

接口本身就不是类，在软件工程中，接口泛指供别人调用的方法或者函数，类使用 interface Demo{} 修饰

接口是用来建立类与类之间的协议，它所提供的只是一种形式，而没有具体的实现；实现该接口的实现类必须要实现该接口的所有方法，通过使用 implements 关键字

接口是抽象类的延伸，Java 中是不能多继承的，子类只能有一个父类；但是接口不同，一个类可以实现多个接口，接口之间可以没有任何联系；

### 2.1、接口使用注意事项

- （1）接口之间也可以继承，但只能是接口继承接口，接口也不能实现接口；抽象类不能继承接口，只能是使用实现；接口之间可以是继承关系，类- （抽象类）与接口是实现关系；**一个接口可以继承多个接口**
- （2）接口中的所有方法默认都是 public abstract 修饰的;接口中不能有静态代码块和静态方法；		
- （3）接口中可以定义"成员变量"，该成员变量会自动被 public static final 修饰，且必须赋值，访问直接使用接口名变量名称；
- （4）接口中不存在已经实现的方法，所有方法都是抽象的;实现接口的非抽象类必须实现接口所有的方法抽象类可以不用实现；
- （5）接口不能直接实例化，但可以声明接口变量引用指向接口的实现类对象使用 instanceof 检查一个对象实现了某个特点接口；
- （6）如果一个类中实现了两个接口，且两个接口有同名方法，那么默认情况下实现的是第一个接口的方法；

### 2.2、接口的默认修饰符

- 接口定义如下

```java
public interface A{
	// 编译错误：The blank final field name may not have been initialized，即需要初始化
	String name;
	// Illegal modifier for the interface method add; only public & abstract are permitted
	// 非法字符，只有public 和 abstract 被允许使用
	protected int add(int a， int b);
}
```

- 再看如下定义，编译如下代码，查看其字节码

```java
public interface A{
	String name = "Hello";
	int add(int a， int b);
}
// 如下，编译器默认都给加上了修饰符(javap -verbose A.class)
{
	public static final java.lang.String name;
	descriptor： Ljava/lang/String;
	flags： ACC_PUBLIC， ACC_STATIC， ACC_FINAL
	ConstantValue： String Hello
	public abstract int add(int， int);
	descriptor： (II)I
	flags： ACC_PUBLIC， ACC_ABSTRACT
}
```

### 2.3、常见空接口

空接口一般是作为一个标记接口，标记某些功能；Cloneable、Serializable这一类接口表示某个标志，实现 Cloneable 表示该类可以被克隆，实现 Serializable 表示该类可以被序列化;

- Serializable 序列化
- RandomAccess：List 实现所使用的标记接口,用来表明其支持快速(通常是固定时间)随机访问，此接口的主要目的是允许一般的算法更改其行为,从而在将其应用到随机或连续访问列表时能提供良好的性能
- Cloneable 克隆
- EventListener 事件监听

## 3、接口与抽象类的区别

### 3.1、语法层面上

- （1）抽象类可以提供成员方法的实现细节，而接口中只能存在 public abstract 方法；
- （2）抽象类中的成员变量可以是各种类型的，而接口中的成员变量只能是 public static final 类型的；
- （3）JDK1.7以前的版本接口中不能含有静态方法.JDK1.8之后可以有实现的静态方法，但是不能有未实现的静态方法。接口中不能存在的是静态代码块.且如果接口中包含了静态方法，则接口无法不能重写该静态方法。而抽象类可以有静态代码块和静态方法；
- （4）接口和抽象类不能实例化，接口中不能有构造，抽象类可以有构造方法；
- （5）一个类只能继承一个抽象类，而一个类却可以实现多个接口；
- （6）接口和抽象类都可以包含内部类- （抽象类)或者内部接口；

### 3.2、设计层面上

- （1）抽象层次不同：抽象类是对类抽象，而接口是对行为的抽象，抽象类是对整个类整体进行抽象，包括属性、行为，但是接口却是对类局部- （行为)进行抽象;抽象是：is-a 的关系，接口是：like-a 的关系；

- （2）跨域不同：抽象类所跨域的是具有相似特点的类，而接口却可以跨域不同的类，抽象类所体现的是一种继承关系，要想使得继承关系合理，父类和派生类之间必须存在"is-a" 关系，即父类和派生类在概念本质上应该是相同的。对于接口则不然，接口是"like-a "，并不要求接口的实现者和接口定义在概念本质上是一致的；

- （3）设计层次不同：抽象类是自下而上抽象出来的，需要先知道子类才能抽象出父类；	接口则是自顶向下的设计，接口根本不需要知道子类的存在，其只需要定义一个规则即可;

[接口静态初始化](http://stackoverflow.com/questions/19722847/static-initialization-in-interface)

## 4、Java8下接口的不同之处

### 4.1、默认方法（default）

在 Java8 中，使用默认方法和静态方法来扩展接口，类似如下代码，使用 default 关键字来实现默认方法

```java
public interface Demo {
	default int add(int a， int b){
		return a + b;
	}
	static int sub(int a， int b){
		return a - b;
	}
}
```

接口中非 default 和 static 的方法不能有方法体

### 4.2、接口实现

如果实现一个接口，默认方法可以不用覆盖重写实现，实现类默认可以直接调用该默认方法；实现类无法重写接口中的静态方法；

***注意：在声明一个默认方法前，请仔细思考是不是真的有必要使用默认方法，因为默认方法会带给程序歧义，并且在复杂的继承体系中容易产生编译错误***

[官方资料](http://docs.oracle.com/javase/tutorial/java/IandI/defaultmethods.html)

```java
// 1.如果一个类实现两个接口，两个接口中有同样签名的 default 方法，编译报错：
public interface DefaultMethodDemo1 {
	default void add(){}
}
public interface DefaultMethodDemo2 {
	default void add(){}
}
public class SubClassDemo implements DefaultMethodDemo1， DefaultMethodDemo2 {}
// 编译错误：SubClassDemo inherited unrelated defaults for add() from type DefaultMethodDemo1
// and DefaultMethodDemo2 因为相当于你在类里面定义了两个同样签名的方法
// 2.如果一个类继承一个抽象类和实现一个接口，抽象类定义了一个和接口的默认方法相同的抽象方法，则在类中需要实现该方法.
public interface DefaultMethodDemo2 {
	default void add(){}
}
public abstract class DefaultMethodDemo1 {
	abstract void add();
}
public class SubClassDemo extends DefaultMethodDemo1 implements DefaultMethodDemo2{
	@Override
	public void add() {}
}
// 3.抽象类、接口存在同样的签名方法，抽象类有实现体但是不是 public 修饰的，编译报错.但如果子类实现对应的方法，则编译通过
// 4.一个声明在类里面的方法优先于任何默认方法，优先选取最具体的实现;
public interface A {
	default void hello(){System.out.println("Interface A hello ： A");}
}
public interface B extends A {
	default void hello(){System.out.println("Interface B hello ： B");}
}
public class C implements A， B {
	public static void main(String[] args) {new C().hello();}
}

// 输出结果：Interface B hello ： B
```

接口的default方法不能重写Object的方法，但是可以对Object类的方法进行重载。因为若可以会很难确定什么时候该调用接口默认的方法

# 十五、类型、类初始化、二进制

## 1、基本类型与引用类型的比较

**1.1、如下四个变量，哪两个比较为 false**

```java
Integer i01 = 59;
int i02 = 59;
Integer i03 =Integer.valueOf(59);
Integer i04 = new Integer(59);
```

（1）Integer 为了节省空间和内存会在内存中缓存 -128~127 之间的数字;

（2）valueOf()：调用该方法时，内部实现作了个判断，判断当前传入的值是否在 -128~127 之间且 IntergCache	是否已存在该对象如果存在，则直接返回引用，如果不存在，则创建一个新对象

（3）基本类型存在内存的栈中，与引用类型比较时， 引用类型会自动装箱，比较数值而不比较内存地址;

**1.2、自动装箱拆箱机制是编译特性还是虚拟机运行时特性？分别是怎么实现的？**

- 自动装箱机制是编译时自动完成替换的.装箱阶段自动替换为了 valueOf 方法，拆箱阶段自动替换为了 xxxValue 方法;
- 对于 Integer 类型的 valueOf 方法参数如果是 -128~127 之间的值会直接返回内部缓存池中已经存在对象的引用，参数是其他范围值则返回新建对象;
- 而 Double 类型与 Integer 类型类似，一样会调用 Double 的 valueOf 方法，但是 Double 的区别在于不管传入的参数值是多少都会 new 一个对象来表达该数值(因为在指定范围内浮点型数据个数是不确定的，整型等个数是确定的，所以可以Cache)
- 注意：Integer、Short、Byte、Character、Long 的 valueOf 方法实现类似，而 Double 和 Float 比较特殊，每次返回新包装对象，对于两边都是包装类型的：== 比较的是引用，	equals 比较的是值；对于两边有一边是表达式(包含算数运算)： == 比较的是数值(自动触发拆箱过程)，对于包装类型 equals 方法不会进行类型转换;

**1.3.Integer i = 1; i += 1; 做了哪些操作**

- Integer i = 1; 做了自动装箱：使用 valueOf() 方法将 int 装箱为 Integer 类型
- i += 1; 先将 Integer 类型的 i 自动拆箱成 int(使用 intValue() 方法将 Integer 拆箱为 int)，完成加法运行之后的 i 再装箱成 Integer 类型

## 2、关于String +和StringBuffer的比较

在 String+写成一个表达式的时候(更准确的说，是写成一个赋值语句的时候)效率其实比 Stringbuffer更快

```java
public class Main{	    
	public static void main(String[] args){		
		String string = "a" + "b" + "c";

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("a").append("b").append("c");
		string = stringBuffer.toString();
	}	    
}
```
**2.1、String+的写法要比 Stringbuffer 快，是因为在编译这段程序的时候，编译器会进行常量优化。**

它会将a、b、c直接合成一个常量abc保存在对应的 class 文件当中{}，看如下反编译的代码：

```java
public class Main{}
	public static void main(String[] args){
		String string = "abc";
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("a").append("b").append("c");
		string = stringBuffer.toString();
	}
}
```

原因是因为 String+其实是由 Stringbuilder 完成的，而一般情况下 Stringbuilder 要快于 Stringbuffer，这是因为 Stringbuilder 线程不安全，少了很多线程锁的时间开销，因此这里依然是 string+的写法速度更快;

```java
/*   1   */
String a = "a";
String b = "b";
String c = "c";
String string = a + b + c;
/*   2   */
StringBuffer stringBuffer = new StringBuffer();
stringBuffer.append(a);
stringBuffer.append(b);
stringBuffer.append(c);
string = stringBuffer.toString();
```
**2.2、字符串拼接方式：+、concat() 以及 append() 方法，append()速度最快，concat()次之，+最慢**

- 编译器对+进行了优化，它是使用 StringBuilder 的 append() 方法来进行处理的，编译器使用 append() 方法追加后要同 toString() 转换成 String 字符串，变慢的关键原因就在于 new StringBuilder()和toString()，这里可是创建了 10 W 个 StringBuilder 对象，而且每次还需要将其转换成 String

- concat：<br>
	concat() 的源码，它看上去就是一个数字拷贝形式，我们知道数组的处理速度是非常快的，但是由于该方法最后是这样的：<br>
	return new String(0， count + otherLen， buf);这同样也创建了 10 W 个字符串对象，这是它变慢的根本原因

- append() 方法拼接字符串：并没有产生新的字符串对象；

## 3、静态代码块、静态变量

其作用级别为类；构造代码块、构造函数、构造，其作用级别为对象

- （1）、静态代码块，它是随着类的加载而被执行，只要类被加载了就会执行，而且只会加载一次，主要用于给类进行初始化。
- （2）、构造代码块，每创建一个对象时就会执行一次，且优先于构造函数，主要用于初始化不同对象共性的初始化内容和初始化实例环境。
- （3）、构造函数，每创建一个对象时就会执行一次.同时构造函数是给特定对象进行初始化，而构造代码是给所有对象进行初始化，作用区域不同.

==> 通过上面的分析，他们三者的执行顺序应该为：静态代码块 > 构造代码块 > 构造函数。

### 3.1、Java 类初始化过程

- 首先，初始化父类中的静态成员变量和静态代码块，按照在程序中出现的顺序初始化；
- 然后，初始化子类中的静态成员变量和静态代码块，按照在程序中出现的顺序初始化；
- 其次，初始化父类的普通成员变量和代码块，在执行父类的构造方法；
- 最后，初始化子类的普通成员变量和代码块，在执行子类的构造方法；

### 3.2、不要在构造器里调用可能被重载的虚方法

父类构造器执行的时候，调用了子类的重载方法，然而子类的类字段还在刚初始化的阶段，刚完成内存布局：

```java
public class Base{
	private String baseName = "base";
	public Base(){
		callName();
	}
	public void callName(){
		System. out. println(baseName);
	}
	static class Sub extends Base{
		private String baseName = "sub";
		public void callName(){
			System. out. println (baseName) ;
		}
	}
	public static void main(String[] args){
		Base b = new Sub();
	}
}
```

### 3.3、Java 中赋值顺序

- （1）父类的静态变量赋值
- （2）自身的静态变量赋值
- （3）父类成员变量赋值
- （4）父类块赋值
- （5）父类构造函数赋值
- （6）自身成员变量赋值
- （7）自身块赋值
- （8）自身构造函数赋值

### 3.4、Java 代码执行顺序

```java
public class TestExecuteCode {
	public static void main(String[] args) {
		System.out.println(new B().getValue());
	}
	static class A {
		protected int value;
		public A(int v){
			setValue(v);
		}
		public void setValue(int value) { this.value = value;}
		public int getValue() {
			try {
				value++;
				return value;
			} finally {
				this.setValue(value);
				System.out.println(value);
			}
		}
	}
	static class B extends A {
		public B(){
			super(5);
			setValue(getValue() - 3);
		}
		public void setValue(int value) {super.setValue(2 * value);}
	}
}
```

- 执行结果：22，34，17
	（1）子类 B 中重写了父类 A 中的setValue方法：
	
	`super(5)` // 调用了父类构造器，其中构造函数里面的`setValue(value)`，调用的是子类的setValue方法

	`finally`块中的：`this.setValue(value)` //调用的也是子类的setValue方法

	而子类`setValue`方法中的：`super.setValue(2*value);` //调用的是父类A的setValue方法

	（2）`try...catch...finally`块中有`return`返回值的情况：`finally` 块中虽然改变了value的值，但`try`块中返回的应该是 return 之前存储的值

- 父类执行时如果有子类的方法重写了父类的方法，调用的子类的重写方法

## 4、给出一个表达式计算其可以按多少进制计算

- 式子7*15=133成立，则用的是几进制？可以通过解方程来解决，上述式子可以转换为方程：
	```
	7 * (1 * x + 5) = 1 * x^2 + 3 * x + 3
	x^2 -4x - 32 = 0
	x = -4 或 x = 8
	```

- 如果下列的公式成立：78+78=123，则采用的是_______进制表示的：
	```
	7 * x + 8 + 7 * x + 8 = 1 * x^2 + 2 * x + 3
	x^2 - 12 * x - 13 = 0
	x = -1， x = 13
	```

## 5、表达式的数据类型

- 所有的 byte，short，char 型的值将被提升为 int 型；
- 如果有一个操作数是 long 型，计算结果是 long 型；
- 如果有一个操作数是 float 型，计算结果是 float 型；
- 如果有一个操作数是 double 型，计算结果是 double 型；
- final 修饰的变量是常量，如果运算时直接是已常量值进行计算，没有final修饰的变量相加后会被自动提升为int型
	byte b1=1，b2=2，b3，b6;<br>
	final byte b4=4，b5=6;<br>
	b6=b4+b5;// b4， b5是常量，则在计算时直接按原值计算，不提升为int型<br>
	b3=(b1+b2);// 编译错误<br>
	System.out.println(b3+b6);

## 6、多态问题

* [多态一道面试题](http://blog.csdn.net/clqyhy/article/details/78978785)

当超类对象引用变量引用子类对象时，被引用对象的类型而不是引用变量的类型决定了调用谁的成员方法，但是这个被调用的方法必须是在超类中定义过的，也就是说被子类覆盖的方法：优先级由高到低依次为：`this.show(O)、super.show(O)、this.show((super)O)、super.show((super)O)`

```java
public class A {
	public String show(D obj) {return ("A and D");}
	public String show(A obj) {return ("A and A");}
}
public class B extends A{
	public String show(B obj) {return ("B and B");}
	public String show(A obj) {return ("B and A");}
}
public class C extends B{}
public class D extends B{}
public class Test {
	public static void main(String[] args) {
		A a1 = new A();
		A a2 = new B();
		B b = new B();
		C c = new C();
		D d = new D();
		System.out.println("1--" + a1.show(b)); //  1--A and A
		System.out.println("2--" + a1.show(c)); //  2--A and A
		System.out.println("3--" + a1.show(d)); //  3--A and D
		System.out.println("4--" + a2.show(b)); //  4--B and A
		System.out.println("5--" + a2.show(c)); //  5--B and A
		System.out.println("6--" + a2.show(d)); //  6--A and D
		System.out.println("7--" + b.show(b));  //  7--B and B
		System.out.println("8--" + b.show(c));  //  8--B and B
		System.out.println("9--" + b.show(d));  //  9--A and D    
	}
}
```
- 多态是对象在不同时刻表现出来的多种状态，是一种编译时期状态和运行时期状态不一致的现象。

- 在编写或者分析代码时记住如下口诀：
	- 成员变量：编译看左，运行看左(因为无法重写)；
	- 成员方法：编译看左，运行看右(因为普通成员方法可以重写，变量不可以)；
	- 静态方法：编译看左，运行看左(因为属于类)；

- 当父类变量引用子类对象时 Base base = new Child();在这个引用变量 base 指向的对象中他的成员变量和静态方法与父类是一致的，他的非静态方法在编译时是与父类一致的，运行时却与子类一致(发生了复写);

# 十六、反射与注解

## 1、Java注解：Annotation

JDK5之后新增的功能

### 1.1、内置注解

- @Override：重写
- @Deprecated：过时
- @SuppressWarnings：取消警告

- 注解分为：
	按运行机制：源码注解，编译时注解，运行时注解
	按照来源注解：JDK，第三方，自定义注解；

### 1.2、自定义注解

使用`@interface`自定义注解时，自动集成了`Annotation`接口，要点如下：

- （1）`@interface`用来声明一个注解：`public @interface`注解名{}；
- （2）其中的每一个方法实际上是声明了一个配置参数：
	- ①、方法的名称就是参数的名称，无参无异常;
	- ②、返回值类型就是参数的类型(返回值只能是基本类型，`Class`，` String`， `Enum`);
	- ③、可以通过 default 来声明参数的默认值
	- ④、如果只有一个参数成员，一般参数名为：value;
	- ⑤、没有成员的注解是标识注解;

*注意：注解元素必须要有值，在定义注解元素时经常使用空字符串，0作为默认值*

### 1.3、元注解

负责注解其他注解，Java定义了四个标准的meta-annotation类型，用来提供对其他 annotation 类型作说明

- `@Target`：用于描述注解的使用范围：`@Target(value= ElementType.TYPE)`
	- 包：PACKAGE<br>
	- 类、接口、枚举、Annotation类型：TYPE
	- 方法参数：PARAMETER
	- 局部变量：LOCAL VARIABLE

- `@Retention`：表示需要在什么级别保存该注释信息，用于描述注解的生命周期：`(RetentionPolicy)`
	SOURCE：在源文件有效(即源文件保留)
	CLASS：在class文件中有效
	RUNTIME：在运行时有效(可被反射读取)

- `@Documented`：生成文档的时候会生成注解的注释

- `@Inherited`：允许子类继承

### 1.4、解析注解

通过反射获取类、函数或成员上的运行时注解信息，从而实现动态控制程序运行的逻辑;

### 1.5、注解处理器

注解处理器是一个在javac中的，用来编译时扫描和处理的注解的工具；一个注解的注解处理器，以Java代码(或者编译过的字节码)作为输入，生成文件(通常是`.java`文件)作为输出。这具体的含义什么呢？你可以生成Java代码！这些生成的Java代码是在生成的`.java`文件中，所以你不能修改已经存在的Java类，例如向已有的类中添加方法。这些生成的Java文件，会同其他普通的手动编写的Java源代码一样被`javac`编译；

虚处理器 AbstractProcessor

## 2、Java 动态加载与静态加载

- 编译时加载类是静态加载类：new 创建对象是静态加载类，在编译时刻时需要加载所有的可能使用到的类;
- 运行时刻加载类是动态加载(Class.forName(""));

## 3、反射机制：(Reflection)

运行时加载，探知使用编译期间完全未知的类；反射：将一个Java类中的所有信息映射成相应的Java类；

反射机制是在运行状态，对于任意一个类，都能够知道这个的类的所有属性和方法；对于任意一个对象，都能够调用它的任意方法和属性；

【一个类只有一个Class对象，这个对象包含了完整类的结构】

- `Class` 类[java.lang.Class]：反射的根源，各种类型----表示Java中的同一类事物
	- `Class`类获取：`.class、getClass、Class.forName(String className);`
	- `Field`：属性相关类，获取所有属性(包括 private)，`getDeclaredFields()`;
	- `Method`：方法相关类，`getDeclaredMethods();`，`method.invoke()`方法执行时，如果第一个参数为 null，则表示反射的方法为静态方法
	- `Constructor`： 构造器相关类，`getDeclaredConstructors()`;

	如果需要访问私有的，则需setAccessible(true);

- 反射机制性能问题：反射会降低程序的效率，如果在开发中确实需要使用到反射，可以将setAccessible设为 true ：即取消Java语言访问检查;

- 反射操作泛型：
	- ①、Java 采用泛型擦除的机制来引入泛型.Java中的泛型仅仅是给编译器使用的，确保数据的安全性和免去强制类型转换的麻烦；	但是一旦编译完成，所有和泛型有关的类型全部擦除;
	- ②、为了通过反射操作泛型，Java有 `ParameterizedType、GenericArrayType、TypeVariable、WildcardType`几种类型来代表不能被归一到Class类中的类型但是又和原始类型齐名的类型；

		- `Type`是Java编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型
		- `ParameterizedType` ：参数化类型
		- `GenericArrayType`：元素类型是参数化类型或者类型变量的数组类型
		- `TypeVariable`：各种类型变量的公共父接口
		- `WildcardType`：表示一种通配符类型表达式;

- 反射操作注解

	```
	getAnnotation(Class<A> annotationClass);
	getAnnotations();
	```

- 反射操作

	- ①、使用反射调用类的main方法：
		
		```
		Method method = Demo.class.getMethod("main"，String[].class);
		method.invoke(null， (Object)new String[]{"111"，"222"，"333"});
		```

	*注意：传入参数时不能直接传一个数组，jdk为了兼容1.5版本以下的，会将其拆包；因此这里将其强转或者直接将String数组放入Object数组也可以*

	- ②、数组的反射
		- 一个问题：
		```java
		int[] a1 = new int[]{1，2，3};<br>
		String[] a2 = new String[]{"a"，"b"，"c"};<br>
		System.out.println(Arrays.asList(a1)); // 输出： [[I@24c98b07]
		System.out.println(Arrays.asList(a2)); // 输出：[a， b， c]
		// 原因：
		// 在jdk1.4：asList(Object[] a);<br>
		// 在jdk1.5：asList(T... a);<br>
		```
		int数组在编译运行时不会被认为为一个Object数组，因此其将按照一个数组对象来处理；

		- 基本类型的一维数组可以被当作Object类型处理，不能被当作`Object[]`类型使用非基本类型的一维数组既可以当作Object类型使用，也可以当作Object[]类型使用；
		Array 工具类可完成数组的反射操作;

- 反射的应用：实现框架功能，使用类加载器加载文件

- 反射时基本类型与包装类型注意问题

	反射中基本类型的class和其对应包装类的class是不同的，所以在获得Method指定参数时需要精确指定参数的类型，即基本类型参数 int 是无法使用包装类型 Integer 来获得的

	反射调用基本类型参数方法除了可以通过 基本类型名`.class`外还可以通过对应包装类的静态字段`TYPE`获得，`int.class 等价于 Integer.TYPE`；
	
	```java
	Class clzss = byte.class;
	Class clzss = short.class;
	Class clzss = int.class;
	Class clzss = long.class;
	Class clzss = char.class;
	Class clzss = float.class;
	Class clzss = double.class;
	Class clzss = boolean.class;
	Class clzss = void.class;

	Byte.TYPE = byte.class;
	Short.TYPE = short.class;
	Integer.TYPE = int.class;
	Long.TYPE = long.class;
	Char.TYPE = char.class;
	Float.TYPE = float.class;
	Double.TYPE = double.class;
	Boolean.TYPE = boolean.class;
	Void.TYPE = void.class;

	Class clzss = int[].class; //int[] 数组
	Class clzss = String[].class; //String[] 数组
	Class clzss = String[][].class; //String[][] 数组的数组
	```

- 反射与工厂模式

## 4、动态编译：Java6.0引入动态编译

### 4.1.动态编译的两种方法

- 通过Runtime调用javac，启动新的进程去操作;
- 通过Javacompiler动态编译;

### 4.2.Javacompiler 动态编译

```java
JavaCompiler compile = ToolProvider.getSystemJavaCompiler();
int result = compile.run(null， null， null， "F：/class/HelloWorld.java");
/*
* run(InputStream in， OutputStream out， OutputStream err， String... arguments);
* 第一个参数： 为Java编译器提供参数;
* 第二个参数： 得到Java编译器输出的信息;
* 第三个参数： 接受编译器的错误信息;
* 第四个参数： 可变参数，能传入一个或多个Java源文件
* 返回值： 0 表示编译成功， 1 表示编译失败;
*/
```

### 4.3、动态运行动态编译的Java类

## 5、动态执行Javascript(JDK6.0以上)

- 脚本引擎：Java 应该程序可以通过一套固定的接口与脚本引擎交互，从而可以在Java平台上调用各种脚本语言;
- js接口：Rhino 引擎，使用Java语言的javascript开源实现;

## 6、Java 字节码操作

### 6.1、Java 动态操作：字节码操作，反射

字节码操作：动态生存新的类；

优势：比反射开销小，性能高；

### 6.2、常见的字节码操作类库

- BCEL：apache
- ASM：轻量级的字节码操作框架，涉及到jvm底层的操作和指令
- CGLIB：基于asm实现
- javaasist：性能比较差，使用简单

### 6.3、Javasist

## 7、反射存在问题

### 7.1、反射慢的原因

- 编译器不能对代码对优化.
- 所有的反射操作都需要类似查表的操作，参数需要封装，解封装，异常也重新封装，rethrow等等

### 7.2、优化方式

- 灵活运用API，如果只是寻找某个方法，不要使用`getMethods()`后在遍历筛选，而是直接用`getMethod(methodName)`来根据方法名获取方法;
- 使用缓存：需要多次动态创建一个类的实例的时候.
- 使用代码动态生成技术，通过调用代理类的方式来模拟反射

# 十七、比较器：Comparale、Comparator

## 1、区别

- `Comparable` & `Comparator`都是用来实现集合中元素的比较、排序的：
	- `Comparable` 是在集合内部定义的方法实现的排序;
	- `Comparator` 是在集合外部实现的排序;

	所以，如想实现排序，就需要在集合外定义 Comparator 接口的方法或在集合内实现 Comparable 接口的方法

- `Comparator` 位于包 java.util下，而 Comparable 位于包 java.lang下
- `Comparable` 是一个对象本身就已经支持自比较所需要实现的接口，（如 String、Integer 自己就可以完成比较大小操作，已经实现了Comparable接口）；
- `Comparator` 是一个专用的比较器，当这个对象不支持自比较或者自比较函数不能满足你的要求时，你可以写一个比较器来完成两个对象之间大小的比较；
- 可以说一个是自已完成比较，一个是外部程序实现比较的差别而已
- 用 `Comparator` 是策略模式(strategy design pattern)，就是不改变对象自身；Comparable 而用一个策略对象(strategy object)来改变它的行为
- 有时在实现 `Comparator` 接口时，并没有实现equals方法，可程序并没有报错.原因是实现该接口的类也是Object类的子类，而Object类已经实现了equals方法

## 2、Comparable

一个实现了 Comparable 接口的类，可以让其自身的对象和其他对象进行比较。也就是说：同一个类的两个对象之间要想比较，对应的类就要实现`Comparable`接口，并实现compareTo()方法

## 3、Comparator

在一些情况下，你不希望修改一个原有的类，但是你还想让他可以比较，Comparator接口可以实现这样的功能。

- 通过使用Comparator接口，你可以针对其中特定的属性/字段来进行比较。比如，当我们要比较两个人的时候，我可能通过年龄比较、也可能通过身高比较。这种情况使用Comparable就无法实现(因为要实现Comparable接口，其中的compareTo方法只能有一个，无法实现多种比较)

- 通过实现Comparator接口同样要重写一个方法：compare()。接下来的例子就通过这种方式来比较HDTV的大小。其实Comparator通常用于排序。Java中的Collections和Arrays中都包含排序的sort方法，该方法可以接收一个Comparator的实例(比较器)来进行排序：

```java
class HDTV {
	private int size;
	private String brand;
	public HDTV(int size， String brand) {
		this.size = size;
		this.brand = brand;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
}
class SizeComparator implements Comparator<HDTV> {
	@Override
	public int compare(HDTV tv1， HDTV tv2) {
		int tv1Size = tv1.getSize();
		int tv2Size = tv2.getSize();

		if (tv1Size > tv2Size) {
			return 1;
		} else if (tv1Size < tv2Size) {
			return -1;
		} else {
			return 0;
		}
	}
}
public class Main {
	public static void main(String[] args) {
		HDTV tv1 = new HDTV(55， "Samsung");
		HDTV tv2 = new HDTV(60， "Sony");
		HDTV tv3 = new HDTV(42， "Panasonic");
		ArrayList<HDTV> al = new ArrayList<HDTV>();
		al.add(tv1);
		al.add(tv2);
		al.add(tv3);
		Collections.sort(al， new SizeComparator());
		for (HDTV a ： al) {
			System.out.println(a.getBrand());
		}
	}
}
```

- 经常会使用`Collections.reverseOrder()`来获取一个倒序的 Comparator：

```java
ArrayList<Integer> al = new ArrayList<Integer>();
al.add(3);
al.add(1);
al.add(2);
System.out.println(al);
Collections.sort(al);
System.out.println(al);
Comparator<Integer> comparator = Collections.reverseOrder();
Collections.sort(al，comparator);
System.out.println(al);
```

## 4、如何选择

- 一个类如果实现 Comparable 接口，那么他就具有了可比较性，意思就是说它的实例之间相互直接可以进行比较
- 通常在两种情况下会定义一个实现 Comparator 类可以把一个Comparator的子类传递给Collections.sort()、Arrays.sort()等方法，用于自定义排序规则。用于初始化特定的数据结构。常见的有可排序的Set(TreeSet)和可排序的Map(TreeMap);

# 十八、枚举类

## 1、枚举类概念

枚举类是JDK1.5之后出现的，允许用常量来表示特定的数据片断，而且全部都以类型安全的形式来表示

### 1.1、枚举类特点

- 枚举类是一种特殊的Java类，枚举不可被继承
- 枚举类中声明的每一个枚举值代表枚举类的一个实例对象；
- 与java普通类一样，在声明枚举类时可以声明属性，方法，构造方法，但是枚举类必须是私有的
- 枚举可以实现接口或继承抽象方法
- 在JDK5之后，switch语句，可以接受int，byte，char，short外，还可以接受枚举类型
- 若枚举类只有一个枚举值，则可以当作单例设计模式

### 1.2、枚举类的一些方法

- values()：获得所有的枚举类
- valueOf(String str)：将一个字符串转为枚举类;

### 1.3、枚举类基类

```java
public abstract class Enum<E extends Enum<E>> implements Comparable<E>， Serializable{}
// 定义枚举类：
public enum Status{
	START()，
	STOP()，
	RUNNING();
}
```
除了`toString`方法，其余方法都不可重写。要么是`final`方法要么是私有方法。

### 1.4、枚举比较

Java 枚举类比较使用 == 或者 equals()都一样，因为枚举类 Enum 的 equals()方法的默认实现是通过 == 来比较的。

在`Enum`中`equals`和`hashCode`方法都是`final`，所以在枚举类中不可实现这两个方法。类似的`Enum`的`compareTo`方法比较的是`Enum`的`ordinal`顺序大小；类似的还有`Enum`的name方法和toString方法一样都返回的是Enum的name值

## 2、枚举类本质

枚举类本质是通过普通类来实现的，只是编译器进行了相应的处理，每个枚举类编译之后的字节码实质都是继承自`java.lang.Enum`的枚举类类型同名普通类.而每个枚举常量实质是一个枚举类型同名普通类的静态常量对象，所有枚举常量都通过静态代码块进行初始化实例赋值.

```java
public enum Status{
	START(),
	STOP(),
	RUNNING();
}
编译之后通过 javap -verbose 查看字节码文件：
.......
public final class Status extends java.lang.Enum<Status>
.......
{
	// 枚举类型值都成了status类型类的静态常量成员属性
	public static final Status start;
	public static final Status stop;		    
	public static final Status running;
	// 静态代码块
	static{};
}
```
- 所以从某种意义上可以说 JDK 1.5 后引入的枚举类型是上面枚举常量类的代码封装而已

```java
public enum  EnumSingleton {
    INSTANCE {
        @Override
        public void print() {
            System.out.println("Singleton Enum");
        }
    };

    public abstract void print();

    public static EnumSingleton getInstance(){
        return INSTANCE;
    }
}
```

通过Jad反编译后，看如下代码
```java
public abstract class EnumSingleton extends Enum{
    public static EnumSingleton[] values(){
        return (EnumSingleton[])$VALUES.clone();
    }
    public static EnumSingleton valueOf(String name){
        return (EnumSingleton)Enum.valueOf(com/blue/fish/design/pattern/creational/singleton/EnumSingleton, name);
    }
    private EnumSingleton(String s, int i){
        super(s, i);
    }
    public abstract void print();
    public static EnumSingleton getInstance(){
        return INSTANCE;
    }
    public static final EnumSingleton INSTANCE;
    private static final EnumSingleton $VALUES[];
    static {
		// 如果枚举类有抽象方法，对应的枚举中会使用匿名内部类来构建枚举
        INSTANCE = new EnumSingleton("INSTANCE", 0) {
            public void print(){
                System.out.println("Singleton Enum");
            }

        };
        $VALUES = (new EnumSingleton[] {
            INSTANCE
        });
    }
}
```

## 3、枚举类与常量

### 3.1、区别

- 枚举相对于常量类来说定义更简单，其不需要定义枚举值，而常量类中每个常量必须手动添加值.
- 枚举作为参数使用时可以避免在编译时避免弱类型错误，而常量类中的常量作为参数使用时无法避免类型错误.
- 枚举类自动具备内置方法，如 values() 方法可以获得所有值的集合遍历，ordinal 方法可以获得排序值，compareTo方法可以给予ordinal比较，而常量类不具备这些方法。
- 枚举的缺点是不能被继承（编译后生成的类是 final class），也不能通过 extends 继承其他类（枚举编译后实质是继承了 Enum 类，java是单继承的）。但是定义的枚举类也通过 implements 实现其他接口；
- 枚举值定义完毕后除非重构，否则无法做扩展，而常量类可以随意继承.

### 3.2、枚举与静态常量内存消耗比

Java枚举会比静态常量更消耗内存，一般场景下不仅编译后的字节码会比静态常量多，而且运行时也会比静态常量需要更多的内存，不过这个多取决于场景和枚举的规模等等

## 4、枚举类是如何保证线程安全的

Java 类加载与初始化是 JVM 保证线程安全，而Java enum枚举在编译器编译后的字节码实质是一个 final 类，每个枚举类型是这个 final 类中的一个静态常量属性，其属性初始化是在该`final`类的`static`块中进行，而 static的常量属性和代码块都是在类加载时初始化完成的， 所以自然就是 JVM 保证了并发安全；

也就是说，我们定义的一个枚举，在第一次被真正用到的时候，会被虚拟机加载并初始化，而这个初始化过程是线程安全的。解决单例的并发问题，主要解决的就是初始化过程中的线程安全问题

## 5、枚举与单例模式

- 除枚举实现的单例模式以外的其他实现方式都有一个比较大的问题是一旦实现了`Serializable`接口后就不再是单例了，因为每次调用`readObject()`方法返回的都是一个新创建出来的对象（当然可以通过使用 readResolve() 方法来避免)）

- Java规范中保证了每一个枚举类型及其定义的枚举变量在JVM中都是唯一的，在枚举类型的序列化和反序列化上Java做了特殊处理。序列化时 Java 仅仅是将枚举对象的 name 属性输出到结果中，反序列化时则是通过 `java.lang.Enum`的`valueOf`方法来根据名字查找枚举对象；同时，编译器是不允许任何对这种序列化机制的定制的，因此禁用了`writeObject、readObject、readObjectNoData、writeReplace和 readResolve`等方法

- Java 枚举序列化需要注意的点：

	如果我们枚举被序列化本地持久化了，那我们就不能删除原来枚举类型中定义的任何枚举对象，否则程序在运行过程中反序列化时JVM 就会找不到与某个名字对应的枚举对象了，所以我们要尽量避免多枚举对象序列化的使用

## 6、迭代器和枚举器区别

- `Enumeration<E>` 枚举器接口是1.0开始提供，适用于传统类，而`Iterator<E>`迭代器接口是1.2提供，适用于`Collections`
- `Enumeration` 只有两个方法接口，我们只能读取集合的数据而不能对数据进行修改，而`Iterator`有三个方法接口，除了能读取集合的数据外也能对数据进行删除操作
- `Enumeration` 不支持`fail-fast`机制，而`Iterator`支持`fail-fast`机制（一种错误检测机制，当多线程对集合进行结构上的改变的操作时就有可能会产生`fail-fast`机制，譬如`ConcurrentModificationException`异常）尽量使用`Iterator`迭代器而不是`Enumeration`枚举器；

# 十九、Java异常

## 1、异常

## 2、Error

## 3、Exception

## 4、Exception分类

### 4.1、运行时异常

### 4.2、非运行时异常

## 5、常见异常

### 5.1、RuntimeException

- NullpointException
- ClassCastException
- IllegalArgumentException
- IndexOutOfBoundException
- NumberFormatException

### 5.2、非RuntimeException

- ClassNotFoundException
- IOException

### 5.3、Error

- StackOverflowError：
- OutOfMemoryError
- NoClassDefFoundError和ClassNotFoundException：当 JVM 或 ClassLoader 在加载类时找不到对应类就会引发 NoClassDefFoundError 和 ClassNotFoundException，他们的区别如下：
	- NoClassDefFoundError 和 ClassNotFoundException 都是由于在 CLASSPATH 下找不到对应的类而引起的。当应用运行时没有找到对应的引用类就会抛出 NoClassDefFoundError，当在代码中通过类名显式加载类（如使用 Class.forName()）时没有找到对应的类就会抛出 ClassNotFoundException；
	- NoClassDefFoundError 表示该类在编译阶段可以找到，但在运行时找不到了，另外有时静态块的初始化过程也会导致 NoClassDefFoundError。而 ClassNotFoundException 一般发生在通过反射或者 ClassLoader 依据类名加载类时类不存在；
	- 此外 NoClassDefFoundError 是 Error，是不受检查类型的异常；而 ClassNotFoundException 是受检查类型的异常，需要进行异常捕获，否则会导致编译错误；
	- NoClassDefFoundError 是链接错误，发生在 JVM 类加载流程的链接阶段，当解析引用的时候找不到对应的类就会抛出 NoClassDefFoundError；而 ClassNotFoundException 一般发生在类加载流程的加载阶段

# 二十、Jar包

## 1、Jar包本质

Jar 包的本质是将多个文件聚集为一个 ZIP 包，与传统的 ZIP 文件不同的是 Jar 包不仅用于压缩和发布，而且还用于部署和封装库、组件和插件程序，并可被编译器和 JVM 等工具直接使用。在 Jar 包中包含特殊的文件，如 manifests 和部署描述符，用来指示工具如何处理特定的 Jar 包。此外 Jar 包提供了许多 ZIP 文件没有的优势和功能，譬如 Jar 包可以保证安全性（对 Jar 文件内容加上数字化签名）、增加传输平台扩展性（作为 Java 扩展框架）、密封性和跨平台特性；

## 2、Jar包下META-INF作用

很多 Jar 包包含一个 META-INF 目录，它用来存储包和扩展的配置数据（如安全性和版本信息），Java 平台会识别并解释 META-INF 目录下的文件来配置应用程序、扩展和类装载器。META-INF 目录包含的常见文件如下：
- MANIFEST.MF：这个 manifest 文件定义了与扩展和包相关的数据（譬如 java -jar 命令执行的 MainClass 就在这里面指定）。
- XXX.SF：这是 Jar 包的签名文件，其中 XXX 表示签名者。
- XXX.DSA：这是与签名文件相关联的签名程序块文件，它存储了用于签名 Jar 文件的公共签名。

## 3、MANIFEST.MF 文件解析

该文件包含了该Jar包的版本、创建人和类搜索路径等信息，当然如果是可执行Jar包，会包含Main-Class属性，表明Main方法入口，下面是从commons-io.jar下的MANIFEST.MF内容
```
Manifest-Version: 1.0
Export-Package: org.apache.commons.io;version="1.4.9999",org.apache.co
 mmons.io.comparator;version="1.4.9999",org.apache.commons.io.filefilt
 er;version="1.4.9999",org.apache.commons.io.input;version="1.4.9999",
 org.apache.commons.io.output;version="1.4.9999",org.apache.commons.io
 .monitor;version="2.5",org.apache.commons.io.serialization;version="2
 .5",org.apache.commons.io;version="2.5",org.apache.commons.io.compara
 tor;version="2.5",org.apache.commons.io.input;version="2.5",org.apach
 e.commons.io.output;version="2.5",org.apache.commons.io.filefilter;ve
 rsion="2.5"
Implementation-Title: Apache Commons IO
Built-By: bimargulies
Tool: Bnd-2.3.0.201405100607
Require-Capability: osgi.ee;filter:="(&(osgi.ee=JavaSE)(version=1.6))"
Implementation-Vendor: The Apache Software Foundation
Implementation-Vendor-Id: org.apache
Specification-Title: Apache Commons IO
Bundle-License: http://www.apache.org/licenses/LICENSE-2.0.txt
Bundle-SymbolicName: org.apache.commons.io
X-Compile-Target-JDK: 1.6
Implementation-Version: 2.5
Specification-Vendor: The Apache Software Foundation
Bundle-Name: Apache Commons IO
Created-By: Apache Maven Bundle Plugin
Implementation-URL: http://commons.apache.org/proper/commons-io/
X-Compile-Source-JDK: 1.6
Bundle-Vendor: The Apache Software Foundation
Build-Jdk: 1.7.0_85
Bundle-Version: 2.5.0
Bnd-LastModified: 1460639998694
Bundle-ManifestVersion: 2
Bundle-Description: The Apache Commons IO library contains utility cla
 sses, stream implementations, file filters, file comparators, endian 
 transformation classes, and much more.
Bundle-DocURL: http://commons.apache.org/proper/commons-io/
Include-Resource: META-INF/LICENSE.txt=LICENSE.txt,META-INF/NOTICE.txt
 =NOTICE.txt
Specification-Version: 2.5
Implementation-Build: tags/commons-io-2.5@r1739098; 2016-04-14 09:19:5
 4-0400
Archiver-Version: Plexus Archiver

```
### 3.1、格式规则

- 基本格式  属性名称：(空格)属性值 ;
- 每行最多72个字符，换行继续必须以空格开头 ;
- 文件最后一定是空行 ;
- Class-Path 当前路径是jar包所在目录，如果要引用当前目录下一个子目录中的jar包，使用以下格式  子目录/jar包名称 子目录/jar名称,注意多个jar包之间用空格分隔, 在任何平台上路径分割符都是 /;

### 3.2、内容分类

#### 3.2.1、一般属性
- Manifest-Version：用来定义manifest文件的版本，例如：Manifest-Version: 1.0
- Created-By：声明该文件的生成者，一般该属性是由jar命令行工具生成的，例如：Created-By: Apache Ant 1.5.3
- Signature-Version：定义jar文件的签名版本
- Class-Path：应用程序或者类装载器使用该值来构建内部的类搜索路径

#### 3.2.2、应用程序相关属性

Main-Class：定义jar文件的入口类，该类必须是一个可执行的类，一旦定义了该属性即可通过 java -jar xxx.jar来运行该jar文件

#### 3.2.3、包扩展属性

- Implementation-Title     定义了扩展实现的标题
- Implementation-Version  定义扩展实现的版本
- Implementation-Vendor  定义扩展实现的组织
- Implementation-Vendor-Id    定义扩展实现的组织的标识
- Implementation-URL 定义该扩展包的下载地址(URL)
- Specification-Title    定义扩展规范的标题
- Specification-Version      定义扩展规范的版本
- Specification-Vendor      声明了维护该规范的组织
- Sealed 定义jar文件是否封存，值可以是true或者false (这点我还不是很理解)

#### 3.2.4、小程序(Applet)相关属性

- Extendsion-List：该属性指定了小程序需要的扩展信息列表，列表中的每个名字对应以下的属性
- `<extension>`-Extension-Name：定义了Jar文件的唯一标识
- `<extension>`-Specification-Version：定义扩展规范的版本
- `<extension>`-Implementation-Version：定义了扩展实现的版本
- `<extension>`-Implementation-Vendor-Id：定义了扩展实现的供应商版本编号
- `<extension>`-Implementation-URL：该jar文件最新版本的下载地址

#### 3.2.5、扩展标识属性

Extension-Name该属性定义了jar文件的唯一标识符

#### 3.2.6、签名相关属性

### 3.3、MANIFEST.MF信息的获取

可以通过java.util.jar这个类库来获取
```java
import java.io.File;  
import java.util.Map;  
import java.util.jar.Attributes;  
import java.util.jar.JarFile;  
import java.util.jar.Manifest;  
publicclass ManifestUtil {  
    publicstaticvoid main(String[] args) throws Exception {  
       JarFile jar=new JarFile(new File("F:\\workspace\\simplecd\\WebContent\\WEB-INF\\lib\\ant.jar"));  
       Manifest manifest = jar.getManifest();  
       Attributes mainAttributes = manifest.getMainAttributes();  
       for(Map.Entry<Object, Object> attrEntry : mainAttributes.entrySet()){         System.out.println("main\t"+attrEntry.getKey()+"-->"+attrEntry.getValue());  
       }  
       Map<String, Attributes> entries = manifest.getEntries();  
       for(Map.Entry<String, Attributes> entry : entries.entrySet()){  
           Attributes values = entry.getValue();  
           for(Map.Entry<Object, Object> attrEntry : values.entrySet()){  
            System.out.println(attrEntry.getKey()+"-->"+attrEntry.getValue());  
           }  
       }  
    }  
}  
```

## 4、Jar 包签名

- Jar 包可以用 jarsigner 工具或者直接通过 java.security API 进行签名来保证安全性。一个签名的 Jar 包与原来的 Jar 包代码部分完全相同，只是更新了它的  manifest 且在 META-INF 目录中增加了一个签名文件和一个签名块文件；
- Jar 包签名采用公钥/密钥机制，通过公钥对被秘钥加密的文件进行解密来验证文件的安全性。Jar 包签名可以防止恶意者冒充某个组织发布 Jar 包，从而避免了被篡改的恶意安全风险。当 Jar 包被签名时，一个签名文件（XXX.SF）会自动在 META-INF 文件夹中生成，该文件夹中同时还含有 manifest 文件、公钥文件，manifest 文件中包含 Jar 包中每个文件的摘要信息，然而签名文件中则包含的是 manifest 文件中每个摘要项的摘要，当 Jar 签名被验证时首先对 Jar 中每个文件进行摘要计算，然后与 manifest 中已记录的摘要进行比较来判断文件是否更改过，同时还要计算 manifest 文件的摘要，并与签名文件比较，以验证 manifest 文件是否被修改过

## 5、IDEA打jar包

使用Idea打包生成指定的`MANIFEST.MF`文件

### 5.1、打包Java工程

- 新建Java工程
- 新建文件：`META-INF/MANIFEST.MF`

	![](image/MANIFEST.MF文件.png)

- 编写Java类:
	```java
	package agent;
	import java.lang.instrument.Instrumentation;
	public class AgentBoot {
		/**
		* 该方法在main方法之前运行，与main方法运行在同一个JVM中 并被同一个System ClassLoader装载
		* 被统一的安全策略(security policy)和上下文(context)管理
		*/
		public static void premain(String agentOps, Instrumentation inst) {
			System.out.println("=========premain方法执行========");
			System.out.println(agentOps);
			// 添加Transformer
			inst.addTransformer(new MyTransformer());
		}
		/**
		* 如果不存在 premain(String agentOps, Instrumentation inst) 则会执行 premain(String
		* agentOps)
		*/
		public static void premain(String agentOps) {
			System.out.println("====premain方法执行2====");
			System.out.println(agentOps);
		}
	}
	```
- 在`MANIFEST.MF` 添加需要添加的数据：
	```
	Manifest-Version: 1.0
	Premain-Class: agent.AgentBoot
	Can-Redefine-Classes: true
	Can-Retransform-Classes: true
	Boot-Class-Path: javassist-3.18.1-GA.jar
	```
- 打开：`File -> Project Structure`，找到`Artifacts`

	![](image/新建artifacts.png)

- 编辑该artifacts：

	![](image/update-artifacts.png)

- 指定`MainClass`

	![](image/mainClass.png)

- 选择 `Buile -> Build Artifacts`，弹出选择action，执行`build`

### 5.2、maven工程打包

直接在pom文件加入插件

```xml
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-jar-plugin</artifactId>
	<version>2.3.1</version>
	<configuration>
		<archive>
			<manifest>
				<addClasspath>true</addClasspath>
			</manifest>
			<manifestEntries>
				<Premain-Class>com.blue.fish.AgentBoot</Premain-Class>
				<Agent-Class>com.blue.fish.AgentBoot</Agent-Class>
				<Can-Redefine-Classes>true</Can-Redefine-Classes>
				<Can-Retransform-Classes>true</Can-Retransform-Classes>
			</manifestEntries>
		</archive>
	</configuration>
</plugin>
```









