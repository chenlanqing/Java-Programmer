<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、三个问题](#%E4%B8%80%E4%B8%89%E4%B8%AA%E9%97%AE%E9%A2%98)
- [二、Java 内存分配](#%E4%BA%8Cjava-%E5%86%85%E5%AD%98%E5%88%86%E9%85%8D)
  - [1.JVM 的体系结构包含几个主要的子系统和内存区](#1jvm-%E7%9A%84%E4%BD%93%E7%B3%BB%E7%BB%93%E6%9E%84%E5%8C%85%E5%90%AB%E5%87%A0%E4%B8%AA%E4%B8%BB%E8%A6%81%E7%9A%84%E5%AD%90%E7%B3%BB%E7%BB%9F%E5%92%8C%E5%86%85%E5%AD%98%E5%8C%BA)
  - [2、Java 堆区域唯一目的是存放对象实例](#2java-%E5%A0%86%E5%8C%BA%E5%9F%9F%E5%94%AF%E4%B8%80%E7%9B%AE%E7%9A%84%E6%98%AF%E5%AD%98%E6%94%BE%E5%AF%B9%E8%B1%A1%E5%AE%9E%E4%BE%8B)
- [三、String 类型](#%E4%B8%89string-%E7%B1%BB%E5%9E%8B)
  - [1、String 的本质-字符数组](#1string-%E7%9A%84%E6%9C%AC%E8%B4%A8-%E5%AD%97%E7%AC%A6%E6%95%B0%E7%BB%84)
  - [2、String 定义方式](#2string-%E5%AE%9A%E4%B9%89%E6%96%B9%E5%BC%8F)
    - [2.1、三种方式](#21%E4%B8%89%E7%A7%8D%E6%96%B9%E5%BC%8F)
    - [2.2、常量池](#22%E5%B8%B8%E9%87%8F%E6%B1%A0)
    - [2.3.使用关键字new](#23%E4%BD%BF%E7%94%A8%E5%85%B3%E9%94%AE%E5%AD%97new)
    - [2.4.直接定义](#24%E7%9B%B4%E6%8E%A5%E5%AE%9A%E4%B9%89)
    - [2.5、串联生成](#25%E4%B8%B2%E8%81%94%E7%94%9F%E6%88%90)
    - [2.6、关于三个问题](#26%E5%85%B3%E4%BA%8E%E4%B8%89%E4%B8%AA%E9%97%AE%E9%A2%98)
  - [3、String、StringBuffer、StringBuilder 的联系与区别](#3stringstringbufferstringbuilder-%E7%9A%84%E8%81%94%E7%B3%BB%E4%B8%8E%E5%8C%BA%E5%88%AB)
    - [3.1、StringBuilder 与 String 性能对比](#31stringbuilder-%E4%B8%8E-string-%E6%80%A7%E8%83%BD%E5%AF%B9%E6%AF%94)
    - [3.2、关于equals比较，看如下代码](#32%E5%85%B3%E4%BA%8Eequals%E6%AF%94%E8%BE%83%E7%9C%8B%E5%A6%82%E4%B8%8B%E4%BB%A3%E7%A0%81)
- [四、关于 String 的不可变](#%E5%9B%9B%E5%85%B3%E4%BA%8E-string-%E7%9A%84%E4%B8%8D%E5%8F%AF%E5%8F%98)
- [五、源码分析](#%E4%BA%94%E6%BA%90%E7%A0%81%E5%88%86%E6%9E%90)
  - [1、定义：(与 JDK8 一致)](#1%E5%AE%9A%E4%B9%89%E4%B8%8E-jdk8-%E4%B8%80%E8%87%B4)
    - [1.1、String 为什么要设计成不可变](#11string-%E4%B8%BA%E4%BB%80%E4%B9%88%E8%A6%81%E8%AE%BE%E8%AE%A1%E6%88%90%E4%B8%8D%E5%8F%AF%E5%8F%98)
  - [2、属性](#2%E5%B1%9E%E6%80%A7)
  - [3、构造方法](#3%E6%9E%84%E9%80%A0%E6%96%B9%E6%B3%95)
    - [3.1、使用字符数组、字符串构造一个 String](#31%E4%BD%BF%E7%94%A8%E5%AD%97%E7%AC%A6%E6%95%B0%E7%BB%84%E5%AD%97%E7%AC%A6%E4%B8%B2%E6%9E%84%E9%80%A0%E4%B8%80%E4%B8%AA-string)
    - [3.2、使用字节数组构造一个 String](#32%E4%BD%BF%E7%94%A8%E5%AD%97%E8%8A%82%E6%95%B0%E7%BB%84%E6%9E%84%E9%80%A0%E4%B8%80%E4%B8%AA-string)
    - [3.3、使用 StringBuffer 和 StringBuider 构造一个 String](#33%E4%BD%BF%E7%94%A8-stringbuffer-%E5%92%8C-stringbuider-%E6%9E%84%E9%80%A0%E4%B8%80%E4%B8%AA-string)
    - [3.4、一个特殊的保护类型的构造方法：(JDK7 以上版本)](#34%E4%B8%80%E4%B8%AA%E7%89%B9%E6%AE%8A%E7%9A%84%E4%BF%9D%E6%8A%A4%E7%B1%BB%E5%9E%8B%E7%9A%84%E6%9E%84%E9%80%A0%E6%96%B9%E6%B3%95jdk7-%E4%BB%A5%E4%B8%8A%E7%89%88%E6%9C%AC)
  - [4、实例方法](#4%E5%AE%9E%E4%BE%8B%E6%96%B9%E6%B3%95)
    - [4.1、getBytes()](#41getbytes)
    - [4.2、比较方法](#42%E6%AF%94%E8%BE%83%E6%96%B9%E6%B3%95)
    - [4.3、hashCode()](#43hashcode)
    - [4.4、substring](#44substring)
    - [4.2、JDK 7 中的substring](#42jdk-7-%E4%B8%AD%E7%9A%84substring)
    - [4.3、substring的细节](#43substring%E7%9A%84%E7%BB%86%E8%8A%82)
  - [5、replaceFirst、replaceAll、replace](#5replacefirstreplaceallreplace)
  - [6、copyValueOf 和 valueOf](#6copyvalueof-%E5%92%8C-valueof)
  - [7、String 对 + 的重载](#7string-%E5%AF%B9--%E7%9A%84%E9%87%8D%E8%BD%BD)
  - [8、String.valueOf 和 Integer.toString的区别](#8stringvalueof-%E5%92%8C-integertostring%E7%9A%84%E5%8C%BA%E5%88%AB)
  - [9、String intern()方法](#9string-intern%E6%96%B9%E6%B3%95)
    - [9.1、Java常量池](#91java%E5%B8%B8%E9%87%8F%E6%B1%A0)
    - [9.2、intern 的实现原理](#92intern-%E7%9A%84%E5%AE%9E%E7%8E%B0%E5%8E%9F%E7%90%86)
    - [9.3、JDK6 和 JDK7 下intern的区别](#93jdk6-%E5%92%8C-jdk7-%E4%B8%8Bintern%E7%9A%84%E5%8C%BA%E5%88%AB)
    - [9.4、intern 的使用](#94intern-%E7%9A%84%E4%BD%BF%E7%94%A8)
  - [10、indexOf方法](#10indexof%E6%96%B9%E6%B3%95)
- [六、关于 String 需要注意的点](#%E5%85%AD%E5%85%B3%E4%BA%8E-string-%E9%9C%80%E8%A6%81%E6%B3%A8%E6%84%8F%E7%9A%84%E7%82%B9)
  - [1、注意点](#1%E6%B3%A8%E6%84%8F%E7%82%B9)
  - [2、用final修饰String变量注意点](#2%E7%94%A8final%E4%BF%AE%E9%A5%B0string%E5%8F%98%E9%87%8F%E6%B3%A8%E6%84%8F%E7%82%B9)
- [七、String 相关的面试题](#%E4%B8%83string-%E7%9B%B8%E5%85%B3%E7%9A%84%E9%9D%A2%E8%AF%95%E9%A2%98)
  - [1、下面这段代码的输出结果是什么](#1%E4%B8%8B%E9%9D%A2%E8%BF%99%E6%AE%B5%E4%BB%A3%E7%A0%81%E7%9A%84%E8%BE%93%E5%87%BA%E7%BB%93%E6%9E%9C%E6%98%AF%E4%BB%80%E4%B9%88)
  - [3、语句 String str = new String("abc"); 一共创建了多少个对象](#3%E8%AF%AD%E5%8F%A5-string-str--new-stringabc-%E4%B8%80%E5%85%B1%E5%88%9B%E5%BB%BA%E4%BA%86%E5%A4%9A%E5%B0%91%E4%B8%AA%E5%AF%B9%E8%B1%A1)
  - [4、String的长度限制](#4string%E7%9A%84%E9%95%BF%E5%BA%A6%E9%99%90%E5%88%B6)
    - [4.1、编译期](#41%E7%BC%96%E8%AF%91%E6%9C%9F)
    - [4.2、运行期](#42%E8%BF%90%E8%A1%8C%E6%9C%9F)
- [八、String的使用技巧](#%E5%85%ABstring%E7%9A%84%E4%BD%BF%E7%94%A8%E6%8A%80%E5%B7%A7)
  - [1、数字前补0](#1%E6%95%B0%E5%AD%97%E5%89%8D%E8%A1%A50)
- [参考文章](#%E5%8F%82%E8%80%83%E6%96%87%E7%AB%A0)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

 * String 核心点：字符串的堆栈和常量池

# 一、三个问题

- 1、Java 内存具体指哪块内存？这块内存区域为什么要进行划分？是如何划分的？划分之后每块区域的作用是什么？如何设置各个区域的大小？
- 2、String 类型在执行连接操作时，效率为什么会比 StringBuffer 或者 StringBuilder 低？ StringBuffer 和 StringBuilder 有什么联系和区别？
- 3、Java 中常量是指什么？String s = "s" 和 String s = new String("s") 有什么不一样？

# 二、Java 内存分配

## 1.JVM 的体系结构包含几个主要的子系统和内存区

- 垃圾回收器(Garbage Collection)：负责回收堆内存(Heap)中没有被使用的对象，即这些对象已经没有被引用了.
- 类装载子系统(Classloader Sub-System)：除了要定位和导入二进制class文件外，还必须负责验证被导入类的正确性，为类变量分配并初始化内存，以及帮助解析符号引用.
- 执行引擎(Execution Engine)：负责执行那些包含在被装载类的方法中的指令.
- 运行时数据区(Java Memory Allocation Area)：又叫虚拟机内存或者Java内存，虚拟机运行时需要从整个计算机内存划分一块内存区域存储许多东西.例如：字节码、从已装载的class文件中得到的其他信息、程序创建的对象、传递给方法的参数，返回值、局部变量等等；如：方法区，Java堆，虚拟机栈，程序计数器，本地方法栈；

## 2、Java 堆区域唯一目的是存放对象实例

几乎所有的对象实例都是在这里分配内存，但是这个对象引用却在栈(Stack)中分配，执行 String s = new String("s")时，

需要从两个地方分配内存：在堆中为String对象分配内存，在栈中为引用(这个堆对象的内存地址，即指针)分配内存：

	-Xms — 设置堆内存初始大小
	-Xmx — 设置堆内存最大值
	-XX：MaxTenuringThreshold — 设置对象在新生代中存活的次数
	-XX：PretenureSizeThreshold — 设置超过指定大小的大对象直接分配在旧生代中

# 三、String 类型

## 1、String 的本质-字符数组

String 是值不可变(immutable)的常量，是线程安全的(can be shared)，String 类使用了 final 修饰符，表明了 String 类的第二个特点：String 类是不可继承的，其声明如下：
```java
public final class String implements java.io.Serializable, Comparable<String>, CharSequence {}
```

## 2、String 定义方式

### 2.1、三种方式

- 使用关键字 new： String s1 = new String("myString");
- 直接定义，如：String s1 = "myString";
- 串联生成，如：String s1 = "my" + "String";

### 2.2、常量池

指的是在编译期被确定，并被保存在已编译的。class文件中的一些数据；它包括了关于类、方法、接口等中的常量，也包括字符串常量.常量池还具备动态性，运行期间可以将新的常量放入池中，虚拟机为每个被装载的类型维护一个常量池，池中为该类型所用常量的一个有序集合，包括直接常量 String、integer和 float 常量)和对其他类型、字段和方法的符号引用；

### 2.3.使用关键字new

在程序编译期，编译程序先去字符串常量池检查，是否存在"myString"，如果不存在，则在常量池中开辟一个内存空间存放"myString"；如果存在的话，则不用重新开辟空间，保证常量池中只有一个"myString"常量，节省内存空间.然后在内存堆中开辟一块空间存放new 出来的 String() 实例，在栈中开辟一块空间，命名为"s1"，存放的值为堆中 String 实例的内存地址，这个过程就是将引用s1指向new出来的String实例

### 2.4.直接定义

在程序编译期，编译程序先去字符串常量池检查，是否存在"myString"，如果不存在，则在常量池中开辟一个内存空间存放"myString"；如果存在的话，则不用重新开辟空间.然后在栈中开辟一块空间，命名为"s1"，存放的值为常量池中"myString"的内存地址

### 2.5、串联生成

其实际是通过StringBuilder的append方法来实现的，最后在调用toString方法

### 2.6、关于三个问题

- 堆中 new {}出来的实例和常量池中的"myString"是什么关系呢？
- 常量池中的字符串常量与堆中的String对象有什么区别呢？
- 为什么直接定义的字符串同样可以调用String对象的各种方法呢？

## 3、String、StringBuffer、StringBuilder 的联系与区别

- StringBuffer 和 StringBuilder 都继承了抽象类 AbstractStringBuilder，这个抽象类和String一样也定义了`char[] value`和 `int count`，但是与String类不同的是，它们没有final修饰符。
	- String、StringBuffer和StringBuilder在本质上都是字符数组，不同的是，在进行连接操作时，String每次返回一个新的String实例，而StringBuffer和StringBuilder的append方法直接返回 this，所以这就是为什么在进行大量字符串连接运算时，不推荐使用 String，而推荐 StringBuffer 和 StringBuilder；
	- StringBuffer和StringBuilder默认 16 个字节数组的大小，超过默认的数组长度时扩容为原来字节数组的长度 * 2 + 2。所以使用StringBuffer和StringBuilder时可以适当考虑下初始化大小，以便通过减少扩容次数来提高代码的高效性；

- 哪种情况使用 StringBuffer？哪种情况使用 StringBuilder 呢？
	```java
	public synchronized StringBuffer append(String str) {
		toStringCache = null;
		super.append(str);
		return this;
	}
	public StringBuilder append(String str) {
		super.append(str);
		return this;
	}
	```
	因此，如果在多线程环境可以使用 StringBuffer 进行字符串连接操作，单线程环境使用 StringBuilder，它的效率更高

- StringBuffer是JDK1.0就有的，而StringBuilder是JDK1.5之后才有的，JDK1.5是将StringBuffer中的部分功能移到 AbstractStringBuilder中，再抽象出非线程安全但性能更高的StringBuilder；

### 3.1、StringBuilder 与 String 性能对比

#### 3.1.1、要点

写代码展示效率的差异、借助ctrl建剖析源代码的调用过程、分析时间复杂度，空间复杂度、调试验证

#### 3.1.2、StringBuilder 关键代码

- （1）以 append(String str)为例，涉及关键代码：

|对应类|方法|备注|
|---|---|---|
|StringBuilder			| append(String)					|在末尾追加字符串|
|AbstractStringBuilder	|append(String)					|在末尾追加字符串|
|AbstractStringBuilder	|char value[]					|存储字符数组	|
|String					|getChars(int， int， char[]，int )|复制字符数组|
|AbstractStringBuilder	|expandCapacity(int)			|扩充容量		|
|Arrays					|copyOf(char[]， int)			|复制字符数组    |

- 调用过程：
- 附加以下"面向对象"的回答，会更加出彩：
	StringBuilder 是抽象类 AbstractStringBuilder 的一个具体实现，(StringBuffer 也实现 AbstractStringBuilder)
	StringBuilder 与 AbstractStringBuilder 重载了不同的append()方法
	所有的append()方法都会返回 this，这样就实现了链式编程
- 详细描述：
	- ①.当数组容量不够的时候，会调用 AbstractStringBuilder 的expandCapacity()方法，将数组的容量扩大至原来的 2n+2；其中，expandCapacity()又调用了 Arrays 的copyOf()方法，目的是把原来数组的元素拷贝至新的数组
	- ②.假设执行了65535次append("H")，即：n=65535；那么，一共进行了多少次新数组内存的开辟，以及旧数组内存的释放？为了方便，进行一些简化：
		数组初始容量为1，每次扩容，容量扩大至原来的2倍：1 -> 2 -> 4 -> 8 -> 16 -> 32 -> 64 ... 65536; 63356=2^16，故而，进行了 log2N 次开辟和释放
	- ③.同样的道理，n=65535，复制了多少个字符？首先，65535次复制无法避免。其次，计算数组扩容所复制字符的个数.1、2、4、8、16 … 32768根据等比数列求和公式：
		a1=1，q=2，n=16代入可得sn=65535
		所以，一共复制2n个字符

#### 3.1.3、String 源码剖析

- String 的 "+"，涉及到的源码：

|对应类|对应方法|备注|
|-----|-------|----|
|StringBuilder|StringBuilder(String)		|StringBuilder 的构造函数|
|StringBuilder|append(String)				|在末尾追加字符串|
|StringBuilder|toString()					|StringBuilder 转换为String|
|String		|String(char[]， int，int)		|String 的构造函数|
|Arrays		|copyOfRange(char[]，int，int)	|复制字符数组|
	
- 调用过程：
- 详细描述：
	- ①.同StringBuilder的append()，假设执行了65535次"+"，即：n=65535；那么，一共进行了多少次新对象、新数组的开辟，以及旧对象、旧数组的释放？每次"+"，要 new StringBuilder()，一共n次；每次"+"，要 new char[str.length()+1]一共n次，故而，进行了2n次的开辟和释放
	- ②.同样的道理，n=65535，复制了多少个字符？1、2、3、4、5、6 … 65535；根据等差数列求和公式()；Sn = 65535 * 65536 / 2；

#### 3.1.4、数据对比

|方法|内存操作|复杂度|
|-----|-----|-----|
|StringBuilder 的append	|开辟、释放内存	|O(log2N)|
|String 的 +			|开辟、释放内存	|O(N)|
|StringBuilder 的append	|字符复制	|O(N)|
|String 的 +			|字符复制	|O(N^2/2)|

### 3.2、关于equals比较，看如下代码
```java
String s1 = "abc";
StringBuffer s2 = new StringBuffer(s1);
System.out.println(s1.equals(s2)); // 1.false
StringBuffer s3 = new StringBuffer("abc");
System.out.println(s3.equals("abc")); // 2.false
System.out.println(s3.toString().equals("abc"));// 3.true
```
- String 的equals 方法都对参数进行类型校验： instanceof String.因为 StringBuffer(StringBuilder)父类为 CharSequence，所以不相等；
- StringBuffer 没有重写 Object 的 equals 方法，所以 Object 的 equals 方法实现是 == 判断，故为 false；
- 因为 Object 的 toString 方法返回为 String 类型，String 重写了 equals 方法实现为值比较；

# 四、关于 String 的不可变

- 1、不可变对象：

	如果一个对象，在它创建完成之后，不能再改变它的状态，那么这个对象就是不可变的.不能改变状态的意思是，不能改变对象内的成员变量，包括基本数据类型的值不能改变，引用类型的变量不能指向其他的对象，引用类型指向的对象的状态也不能改变；

- 2、对象与对象引用：
	```java
	String s = "ABCabc";
	s = "123456";
	```
	- s只是一个 String 对象的引用，并不是对象本身.对象在内存中是一块内存区，成员变量越多，这块内存区占的空间越大.引用只是一个4字节的数据，里面存放了它所指向的对象的地址，通过这个地址可以访问对象
	- s只是一个引用，它指向了一个具体的对象，当s="123456"; 这句代码执行过之后，又创建了一个新的对象"123456"，而引用s重新指向了这个新的对象，原来的对象"ABCabc"还在内存中存在，并没有改变
- 3、JDK1.7中， String 类做了一些改动，主要是改变了substring方法执行时的行为
- 4、String 类不能修改 其成员变量，且其是 final 类型的，一旦初始化就不能改变. 通过反射是可以修改所谓的"不可变"对象的

# 五、源码分析

- String 表示字符串，Java 中所有字符串的字面值都是 String 类的实例，例如"ABC".字符串是常量，在定义之后不能被改变，字符串缓冲区支持可变的字符串.因为 String 对象是不可变的，所以可以共享它们

	String str = "abc"; <==> char[] data = {'a'，'b'，'c'}; String str = new String(data);

- Java 语言提供了对字符串连接运算符的特别支持(+)，该符号也可用于将其他类型转换成字符串。字符串的连接实际上是通过 StringBuffer 或者 StringBuilder 的append()方法来实现的，字符串的转换通过toString方法实现，该方法由 Object 类定义，并可被 Java 中的所有类继承；

## 1、定义：(与 JDK8 一致)
```java
public final class String implements java.io.Serializable， Comparable<String>， CharSequence{}
```
从该类的声明中我们可以看出 String 是 final 类型的，表示该类不能被继承，同时该类实现了三个接口：表示可序列化，可比较，是字符序列

### 1.1、String 为什么要设计成不可变

- 字符串池：字符串池是方法区中的一部分特殊存储.当一个字符串被被创建的时候，首先会去这个字符串池中查找，如果找到，直接返回对该字符串的引用；如果字符串可变的话，当两个引用指向指向同一个字符串时，对其中一个做修改就会影响另外一个；
- 缓存hashcode：String 类不可变，所以一旦对象被创建，该hash值也无法改变.所以，每次想要使用该对象的hashcode的时候，直接返回即可；这就使得字符串很适合作为 Map 中的键，字符串的处理速度要快过其它的键对象.这就是 HashMap 中的键往往都使用字符串；
- 使其他类的使用更加便利：如对于 set 的操作；
- 安全性：String 被广泛的使用在其他 Java 类中充当参数，如果字符串可变，那么类似操作可能导致安全问题，可变的字符串也可能导致反射的安全问题，因为他的参数也是字符串；类加载器要用到字符串，不可变性提供了安全性，以便正确的类被加载；
- 不可变对象天生就是线程安全的：因为不可变对象不能被改变，所以他们可以自由地在多个线程之间共享.不需要任何同步处理？
- 如果字符串是可变的则会引起很严重的安全问题，譬如数据库的用户名密码都是以字符串的形式传入来获得数据库的连接，或者在 socket 编程中主机名和端口都是以字符串的形式传入，因为字符串是不可变的，所以它的值是不可改变的，否则黑客们可以钻到空子改变字符串指向的对象的值造成安全漏洞;
	
## 2、属性

- private final char value[];

	这是一个字符数组，并且是 final 类型，他用于存储字符串内容，从 final 这个关键字中我们可以看出，String 的内容一旦被初始化了是不能被更改的. 虽然有这样的例子： String s = "a"; s = "b" 但是，这并不是对s的修改，而是重新指向了新的字符串， 从这里我们也能知道，String 其实就是用 char[] 实现的

- private int hash;

	缓存字符串的hash Code，默认值为 0

- private static final long serialVersionUID = -6849794470754667710L;

	private static final ObjectStreamField[] serialPersistentFields = new ObjectStreamField[0];

	Java 的序列化机制是通过在运行时判断类的serialVersionUID来验证版本一致性的.在进行反序列化时，JVM 会把传来的字节流中的serialVersionUID与本地相应实体(类)的serialVersionUID进行比较，如果相同就认为是一致的，可以进行反序列化，否则就会出现序列化版本不一致的异常(InvalidCastException)；

## 3、构造方法

### 3.1、使用字符数组、字符串构造一个 String

- 使用一个字符数组来创建一个 String，那么这里值得注意的是，当我们使用字符数组创建 String 的时候，会用到 Arrays.copyOf方法和 Arrays.copyOfRange方法.这两个方法是将原有的字符数组中的内容逐一的复制到 String 中的字符数组中;

	当然，在使用字符数组来创建一个新的 String 对象的时候，不仅可以使用整个字符数组，也可以使用字符数组的一部分，只要多传入两个参数 int offset和 int count就可以了

- 可以用一个 String 类型的对象来初始化一个 String。这里将直接将源 String 中的value和hash两个属性直接赋值给目标 String.因为String一旦定义之后是不可以改变的，所以也就不用担心改变源 String 的值会影响到目标 String 的值

### 3.2、使用字节数组构造一个 String

- String 实例中保存有一个 char[]字符数组，char[]字符数组是以unicode码来存储的，String 和 char 为内存形式，byte 是网络传输或存储的序列化形式
- String(byte[] bytes， Charset charset)是指通过charset来解码指定的 byte 数组，将其解码成unicode的char[]数组，够造成新的 String;

	也可构造字节数组的部分 String(byte bytes[]) String(byte bytes[]， int offset， int length)

- 使用如下四种构造方法，就会使用 StringCoding.decode方法进行解码，使用的解码的字符集就是我们指定的charsetName或者charset
	```java
	String(byte bytes[]， Charset charset)
	String(byte bytes[]， String charsetName)
	String(byte bytes[]， int offset， int length， Charset charset)
	String(byte bytes[]， int offset， int length， String charsetName)
	```
	注意：在使用 byte[]构造 String 的时候，如果没有指明解码使用的字符集的话，那么 StringCoding 的decode方法首先调用系统的默认编码格式，如果没有指定编码格式则默认使用 ISO-8859-1编码格式进行编码操作：

### 3.3、使用 StringBuffer 和 StringBuider 构造一个 String

StringBuffer 和 StringBuider 也可以被当做构造String的参数；Java 的官方文档有提到说使用 StringBuilder 的toString方法会更快一些，原因是 StringBuffer 的toString方法是 synchronized 的

### 3.4、一个特殊的保护类型的构造方法：(JDK7 以上版本)
```java
String(char[] value， boolean share) {
	// assert share ： "unshared not supported";
	this.value = value;
}
```
该方法和 String(char[] value)有两点区别
- 该方法多了一个参数： boolean share，其实这个参数在方法体中根本没被使用，也给了注释，目前不支持使用 false，只使用 true，加入这个share的只是为了区分于 String(char[] value)方法
- 第二个区别就是具体的方法实现不同，这个方法构造出来的 String 和参数传过来的 char[] value共享同一个数组

#### 3.4.1、为什么Java会提供这样一个方法呢

- 优点：首先性能好，一个是直接给数组赋值（相当于直接将 String 的value的指针指向 char[]数组），一个是逐一拷贝.当然是直接赋值快了；其次，共享内部数组节约内存

- 该方法之所以设置为 protected，是因为一旦该方法设置为公有，在外面可以访问的话，那就破坏了字符串的不可变性：
	```java
	char[] arr = new char[] {'h'， 'e'， 'l'， 'l'， 'o'， ' '， 'w'， 'o'， 'r'， 'l'， 'd'};
	String s = new String(0， arr.length， arr); // "hello world"
	arr[0] = 'a'; // replace the first character with 'a'
	System.out.println(s); // aello world
	```
	如果构造方法没有对arr进行拷贝，那么其他人就可以在字符串外部修改该数组，由于它们引用的是同一个数组，因此对arr的修改就相当于修改了字符串

#### 3.4.2、在Java7之前很多String里面的方法都使用这种"性能好的、节约内存的、安全"的构造函数

substring、replace、concat、valueOf等方法（实际上他们使用的是 public String(char[]， int， int)方法，原理和本方法相同，已经被本方法取代）

#### 3.4.3、在 Java 7 中，substring已经不再使用这种"优秀"的方法了，为什么呢

有个致命的缺点：可能造成内存泄露，虽然 String 本身可以被回收，但它的内部数组却不能

## 4、实例方法
```java
length() //返回字符串长度
isEmpty() // 返回字符串是否为空
charAt(int index) // 返回字符串中第(index+1)个字符
char[] toCharArray() // 转化成字符数组
trim() // 去掉全部空格
toUpperCase() // 转化为大写
toLowerCase() // 转化为小写
String concat(String str) //拼接字符串，使用了String(char[] value， boolean share)；
String replace(char oldChar， char newChar) //将字符串中的oldChar字符换成newChar字符，使用了String(char[] value， boolean share)；
boolean matches(String regex) //判断字符串是否匹配给定的regex正则表达式
boolean contains(CharSequence s) //判断字符串是否包含字符序列s
String[] split(String regex， int limit) //按照字符regex将字符串分成limit份.
String[] split(String regex)//按照字符regex分割字符串
```
### 4.1、getBytes()

将一个字符串转换成字节数组，那么String提供了很多重载的getBytes方法；值得注意的是，在使用这些方法的时候一定要注意编码问题，一般为了保持跟机器环境无关需要指定编码方式
```java
String s = "你好，世界！"; 
byte[] bytes = s.getBytes("utf-8");
```
### 4.2、比较方法
```java	
boolean equals(Object anObject);
boolean contentEquals(StringBuffer sb);
boolean contentEquals(CharSequence cs);
boolean equalsIgnoreCase(String anotherString);
```
- 前三个比较 String 和要比较的目标对象的字符数组的内容，一样就返回 true，不一样就返回 false;
- 核心代码：
	```java
	int n = value.length;
	while (n-- != 0) {
		if (v1[i] != v2[i])
			return false;
		i++;
	}
	```
	v1 v2分别代表String的字符数组和目标对象的字符数组

- 第四个和前三个唯一的区别就是他会将两个字符数组的内容都使用toUpperCase方法转换成大写再进行比较，以此来忽略大小写进行比较.相同则返回 true，不想同则返回 false
```java
int compareTo(String anotherString)；
int compareToIgnoreCase(String str)；
boolean regionMatches(int toffset， String other， int ooffset，int len)  //局部匹配
boolean regionMatches(boolean ignoreCase， int toffset，String other， int ooffset， int len)   //局部匹配
```
### 4.3、hashCode()

hashCode的实现其实就是使用数学公式： s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]

s[i]是string的第i个字符，n是String的长度.那为什么这里用31，而不是其它数呢？ 计算机的乘法涉及到移位计算。

当一个数乘以2时，就直接拿该数左移一位即可！选择31原因是因为31是一个素数！

### 4.4、substring

Java 7 中的substring方法使用 String(value， beginIndex， subLen)方法创建一个新的String并返回，这个方法会将原来的 char[]中的值逐一复制到新的String中，两个数组并不是共享的，虽然这样做损失一些性能，但是有效地避免了内存泄露
```java
public String substring(int beginIndex) {
	if (beginIndex < 0) {
		throw new StringIndexOutOfBoundsException(beginIndex);
	}
	int subLen = value.length - beginIndex;
	if (subLen < 0) {
		throw new StringIndexOutOfBoundsException(subLen);
	}
	return (beginIndex == 0) ？ this ： new String(value， beginIndex， subLen);
}
```
- 直到Java 1.7版本之前，substring会保存一份原字符串的字符数组的引用，这意味着，如果你从1GB大小的字符串里截取了5个字符，而这5个字符也会阻止那1GB内存被回收，因为这个引用是强引用.
- 到了Java 1.7，这个问题被解决了，原字符串的字符数组已经不再被引用，但是这个改变也使得substring()创建字符串的操作更加耗时，以前的开销是O(1)，现在最坏情况是O(n)；

#### 4.1、JDK6中的substring
```java
public String substring(int beginIndex, int endIndex) {
	if (beginIndex < 0) {
	    throw new StringIndexOutOfBoundsException(beginIndex);
	}
	if (endIndex > count) {
	    throw new StringIndexOutOfBoundsException(endIndex);
	}
	if (beginIndex > endIndex) {
	    throw new StringIndexOutOfBoundsException(endIndex - beginIndex);
	}
	return ((beginIndex == 0) && (endIndex == count)) ? this :
	    new String(offset + beginIndex, endIndex - beginIndex, value);
}
```
- 在jdk 6 中，String 类包含三个成员变量：char value[]， int offset，int count；他们分别用来存储真正的字符数组，数组的第一个位置索引以及字符串中包含的字符个数
- 当调用substring方法的时候，会创建一个新的 String 对象，但是这个 String 的值仍然指向堆中的同一个字符数组.这两个对象中只有count和offset 的值是不同的
- JDK 6中的substring导致的问题：

	如果你有一个很长很长的字符串，但是当你使用substring进行切割的时候你只需要很短的一段.这可能导致性能问题，因为你需要的只是一小段字符序列，但是你却引用了整个字符串(因为这个非常长的字符数组一直在被引用，所以无法被回收，就可能导致内存泄露)，
	
	在 JDK 6中，一般用以下方式来解决该问题，原理其实就是生成一个新的字符串并引用他：x = x.substring(x， y) + ""
		
### 4.2、JDK 7 中的substring

	上面提到的问题，在jdk 7中得到解决.在jdk 7 中，substring方法会在堆内存中创建一个新的数组

### 4.3、substring的细节

substring 方法实现里面有个 index == 0 的判断，当 index 等于 0 就直接返回当前对象，否则新 new() 一个 sub 的对象返回

## 5、replaceFirst、replaceAll、replace
```java
String replaceFirst(String regex， String replacement)
String replaceAll(String regex， String replacement)
String replace(CharSequence target， CharSequence replacement)
```
- replace的参数是char和 CharSequence，即可以支持字符的替换，也支持字符串的替换 
- replaceAll和replaceFirst的参数是regex，即基于规则表达式的替换:

	相同点是都是全部替换，即把源字符串中的某一字符或字符串全部换成指定的字符或字符串， 如果只想替换第一次出现的，可以使用 replaceFirst()，这个方法也是基于规则表达式的替换，但与replaceAll()不同的是，只替换第一次出现的字符串; 另外，如果replaceAll()和replaceFirst()所用的参数据不是基于规则表达式的，则与replace()替换字符串的效果是一样的，即这两者也支持字符串的操作；

## 6、copyValueOf 和 valueOf

valueOf六个重载方法可以看到这些方法可以将六种基本数据类型的变量转换成String类型

## 7、String 对 + 的重载

Java 是不支持重载运算符，String 的 + 是java中唯一的一个重载运算符，如何实现的？
```java
public static void main(String[] args) {
	String string="hollis";
	String string2 = string + "chuang";
}
```
反编译后：
```java
public static void main(String args[]){
	String string = "hollis";
	String string2 = (new StringBuilder(String.valueOf(string))).append("chuang").toString();
}
```
其实 String 对 + 的支持其实就是使用了 StringBuilder 以及他的append、toString两个方法

## 8、String.valueOf 和 Integer.toString的区别

有三种方式将一个int类型的变量变成呢过String类型，那么他们有什么区别？
```java
int i = 5;
String i1 = "" + i;
String i2 = String.valueOf(i);
String i3 = Integer.toString(i);
```
- 第三行和第四行没有任何区别，因为 String.valueOf(i)也是调用 Integer.toString(i)来实现的. 
- 第二行代码其实是 String i1 = (new StringBuilder()).append(i).toString();，首先创建一个 StringBuilder 对象，然后再调用append方法，再调用toString方法

## 9、String intern()方法

public native String intern();

### 9.1、Java常量池

Java 中8种基本类型和一种比较特殊的类型 String，常量池就类似一个 JAVA 系统级别提供的缓存，8种基本类型的常量池都是系统协调的，String 类型的常量池比较特殊。它的主要使用方法有两种：

- 直接使用双引号声明出来的String对象会直接存储在常量池中;
- 如果不是用双引号声明的 String 对象，可以使用 String 提供的intern方法.intern 方法会从字符串常量池中查询当前字符串是否存在，若不存在就会将当前字符串放入常量池中；

### 9.2、intern 的实现原理

如果常量池中存在当前字符串，就会直接返回当前字符串。如果常量池中没有此字符串，会将此字符串放入常量池中后，再返回

- 大体实现结构就是：JAVA 使用 jni 调用c++实现的 StringTable 的 intern方法， StringTable 的intern方法跟Java 中的 HashMap 的实现是差不多的，只是不能自动扩容.默认大小是 1009;
- String 的 String Pool 是一个固定大小的 Hashtable，默认值大小长度是1009，如果放进 String Pool的 String 非常多，就会造成 Hash 冲突严重，从而导致链表会很长，而链表长了后直接会造成的影响就是当调用 String.intern时性能会大幅下降
- JDK6 中 StringTable 是固定的，就是 1009 的长度，所以如果常量池中的字符串过多就会导致效率下降很快；在jdk7中，StringTable 的长度可以通过一个参数指定： -XX：StringTableSize=99991；从 Java7u40 开始，该默认值增大到 60013

### 9.3、JDK6 和 JDK7 下intern的区别

#### 9.3.1、关于创建对象问题

String s = new String("abc")这个语句创建了几个对象

第一个对象是"abc"字符串存储在常量池中，第二个对象在 JAVA Heap 中的 String 对象

#### 9.3.2、看一段代码

- 代码片段1：
	```java
	public static void main(String[] args) {
		String s = new String("1");
		s.intern();
		String s2 = "1";
		System.out.println(s == s2);		 
		String s3 = new String("1") + new String("1");
		s3.intern();
		String s4 = "11";
		System.out.println(s3 == s4);
	}
	```
	运行结果：<br>
	JDK6： false false<br>
	JDK7： false true<br>

- 代码片段2：将s3.intern();语句下调一行，放到String s4 = "11";后面.将s.intern(); 放到String s2 = "1";后面.是什么结果呢
	```java
	public static void main(String[] args) {
		String s = new String("1");
		String s2 = "1";
		s.intern();
		System.out.println(s == s2);			 
		String s3 = new String("1") + new String("1");
		String s4 = "11";
		s3.intern();
		System.out.println(s3 == s4);
	}
	```
	运行结果：<br>
	JDK6： false false<br>
	JDK7： false false

- 上述 JDK6 中解释：

	- 首先说一下 jdk6中的情况，在 jdk6中上述的所有打印都是 false 的，因为 jdk6中的常量池是放在 Perm 区中的，Perm 区和正常的 JAVA Heap 区域是完全分开的
	- 如果是使用引号声明的字符串都是会直接在字符串常量池中生成，而 new {}出来的 String 对象是放在 JAVA Heap 区域
	- 所以拿一个 JAVA Heap 区域的对象地址和字符串常量池的对象地址进行比较肯定是不相同的，即使调用 String.intern方法也是没有任何关系的

- 上述 JDK7 中的解释：
	- 需要注意的一点：在 Jdk6 以及以前的版本中，字符串的常量池是放在堆的 Perm 区的，Perm 区是一个类静态的区域，主要存储一些加载类的信息、常量池、方法片段等内容，默认大小只有4m，一旦常量池中大量使用 intern 是会直接产生 java.lang.OutOfMemoryError： PermGen space错误的

	- 在 JDK7 的版本中，字符串常量池已经从 Perm 区移到正常的 Java Heap 区域了。为什么要移动？Perm 区域太小是一个主要原因，当然据消息称 JDK8 已经直接取消了 Perm 区域，而新建立了一个元空间，应该是 jdk 开发者认为 Perm 区域已经不适合现在 JAVA 的发展了

	- intern方法还是会先去查询常量池中是否有已经存在，如果存在，则返回常量池中的引用，这一点与之前没有区别，区别在于，如果在常量池找不到对应的字符串，则不会再将字符串拷贝到常量池，而只是在常量池中生成一个对原字符串的引用
	- 代码片段1：
		- 在第一段代码中，先看s3和s4字符串：String s3 = new String("1") + new String("1");这句代码中现在生成了 2 最终个对象，是字符串常量池中的"1"和 JAVA Heap 中的 s3引用指向的对象。中间还有2个匿名的 new String("1")我们不去讨论它们.此时s3引用对象内容是"11"，但此时常量池中是没有 "11"对象的.
		- 接下来s3.intern();这一句代码，是将 s3中的"11"字符串放入 String 常量池中，因为此时常量池中不存在"11"字符串，因此常规做法是跟 jdk6 图中表示的那样，在常量池中生成一个 "11"的对象，关键点是 jdk7 中常量池不在 Perm 区域了，这块做了调整。常量池中不需要再存储一份对象了，可以直接存储堆中的引用.这份引用指向 s3 引用的对象.也就是说引用地址是相同的.
		- 最后String s4 = "11"; 这句代码中"11"是显示声明的，因此会直接去常量池中创建，创建的时候发现已经有这个对象了，此时也就是指向 s3 引用对象的一个引用.所以 s4 引用就指向和 s3 一样了.因此最后的比较 s3 == s4 是 true.
		- 再看 s 和 s2 对象. String s = new String("1"); 第一句代码，生成了2个对象.常量池中的"1" 和 JAVA Heap 中的字符串对象.s.intern(); 这一句是 s 对象去常量池中寻找后发现 "1" 已经在常量池里了
		- 接下来String s2 = "1"; 这句代码是生成一个 s2 的引用指向常量池中的"1"对象. 结果就是 s 和 s2 的引用地址明显不同。
	- 代码片段2：
		- 第一段代码和第二段代码的改变就是 s3.intern(); 的顺序是放在String s4 = "11";后了。这样，首先执行String s4 = "11";声明 s4 的时候常量池中是不存在"11"对象的，执行完毕后，"11"对象是 s4 声明产生的新对象.然后再执行s3.intern();时，常量池中"11"对象已经存在了，因此 s3 和 s4 的引用是不同的.
		- 第二段代码中的 s 和 s2 代码中，s.intern();，这一句往后放也不会有什么影响了，因为对象池中在执行第一句代码 String s = new String("1");的时候已经生成"1"对象了.下边的s2声明都是直接从常量池中取地址引用的。s 和 s2 的引用地址是不会相等的；

#### 9.3.3、总结

从上述的例子代码可以看出 jdk7 版本对 intern 操作和常量池都做了一定的修改.主要包括2点
- 将 String常量池 从 Perm 区移动到了 Java Heap区
- String#intern 方法时，如果存在堆中的对象，会直接保存对象的引用，而不会重新创建对象；

### 9.4、intern 的使用

- 正确使用：
	```java
	/**
	 * Runtime Parameter：
	 * -Xmx2g -Xms2g -Xmn1500M
	 */
	static final int MAX = 1000 * 10000;
	static final String[] arr = new String[MAX];		 
	public static void main(String[] args) throws Exception {
		Integer[] DB_DATA = new Integer[10];
		Random random = new Random(10 * 10000);
		for (int i = 0; i < DB_DATA.length; i++) {
			DB_DATA[i] = random.nextInt();
		}
		long t = System.currentTimeMillis();
		for (int i = 0; i < MAX; i++) {
			//arr[i] = new String(String.valueOf(DB_DATA[i % DB_DATA.length]));
			arr[i] = new String(String.valueOf(DB_DATA[i % DB_DATA.length])).intern();
		}
		
		System.out.println((System.currentTimeMillis() - t) + "ms");
		System.gc();
	}
	```
	- 通过上述结果，我们发现不使用 intern 的代码生成了1000w 个字符串，占用了大约640m 空间.。使用了 intern 的代码生成了1345个字符串，占用总空间 133k 左右。其实通过观察程序中只是用到了10个字符串，所以准确计算后应该是正好相差100w 倍
	- 使用了 intern 方法后时间上有了一些增长.这是因为程序中每次都是用了 new String() 后，然后又进行 intern 操作的耗时时间，这一点如果在内存空间充足的情况下确实是无法避免的；

- 不正确使用：

	fastjson 中对所有的 json 的 key 使用了 intern 方法，缓存到了字符串常量池中，这样每次读取的时候就会非常快，大大减少时间和空间.而且 json 的 key 通常都是不变的.这个地方没有考虑到大量的 json key 如果是变化的，那就会给字符串常量池带来很大的负担

## 10、indexOf方法

# 六、关于 String 需要注意的点

## 1、注意点

- 任何时候，比较字符串内容都应该使用equals方法;
- 修改字符串操作，应该使用 StringBuffer，StringBuilder;
- 可以使用intern方法让运行时产生字符串的复用常量池中的字符串
- 字符串操作可能会复用原字符数组，在某些情况可能造成内存泄露的问题；substring、split等方法得到的结果都是引用原字符数组的. 如果某字符串很大，而且不是在常量池里存在的，当你采用substring等方法拿到一小部分新字符串之后，长期保存的话(例如用于缓存等)，会造成原来的大字符数组意外无法被GC的问题
	
## 2、用final修饰String变量注意点
```java
String m = "Hello，World";
String u = m + ".";
String v = "Hello，World.";
```
```u == v``` ==> false;

如果 m 改为 final 修饰：

```u == v``` ==> true;

# 七、String 相关的面试题

## 1、下面这段代码的输出结果是什么

- 1.1、"hello" + 2;在编译期间已经被优化为 "hello2"， 因此在运行期间，变量a和变量b指向的是同一个对象
	```java
	String a = "hello2"; 　　
	String b = "hello" + 2; 　　
	System.out.println((a == b)); // true
	```
- 1.2、由于有符号引用的存在，所以  String c = b + 2;不会在编译期间被优化，不会把b+2当做字面常量来处理的，因此这种方式生成的对象事实上是保存在堆上的
	```java
	String a = "hello2"; 　　
	String b = "hello";
	String c = b + 2;　　
	System.out.println((a == c));// false
	```
- 1.3、对于被 final 修饰的变量，会在class文件常量池中保存一个副本，也就是说不会通过连接而进行访问，对 final 变量的访问在编译期间都会直接被替代为真实的值.那么 String c = b + 2;在编译期间就会被优化成：
	```java	
	String c = "hello" + 2; 
	String a = "hello2"; 　　
	final String b = "hello";
	String c = b + 2;　　
	System.out.println((a == c));// true
	```
- 1.4、这里面虽然将b用 final 修饰了，但是由于其赋值是通过方法调用返回的，那么它的值只能在运行期间确定，因此a和c指向的不是同一个对象
	```java
	public class Main {
		public static void main(String[] args) {
			String a = "hello2";
			final String b = getHello();
			String c = b + 2;
			System.out.println((a == c)); // false
		}		 
		public static String getHello() {
			return "hello";
		}
	}
	```
## 2、怎样将 GB2312 编码的字符串转换为 ISO-8859-1 编码的字符串？

```java
String s1 = "你好";
String s2 = new	String(s1.getBytes("GB2312")， "ISO-8859-1");
```
*注意：* 上面代码会抛出 UnsupportedEncodingException 异常

## 3、语句 String str = new String("abc"); 一共创建了多少个对象

在常量池中查找是否有“abc”对象，有则返回对应的引用；

没有则创建对应的实例对象；在堆中创建一个String("abc")对象，将对象地址赋值给str，创建一个引用


## 4、String的长度限制

### 4.1、编译期

看String的源码`public String(char value[], int offset, int count)`，count是int类型的，所以char[] value最多可以保存 Integer.MAX_VALUE个；

但是在实际证明，String中最多可以有65534个字符，如果超过了这个个数，就会在编译期报错
```java
String s = "a...a"; // 65534个a
System.out.println(s.length());

String s1 = "a...a"; // 65535个a
System.out.println(s1.length()); // 或报错，提示常量字符串过长；
```

当我们使用字符串字面量直接定义String的时候，是会把字符串在常量池中存储一份的。上面的65534其实是常量池的限制；常量池中每一种数据项也有自己的类型。Java中的UTF-8编码的unicode字符串在常量池中以CONSTANT_Utf8类型表示；

CONSTANT_Utf8_INFO是一个CONSTANT_Utf8类型的常量池数据项，它存储的是一个常量字符串。常量池中的所有字面量几乎都是通过CONSTANT_Utf8_INFO描述的。CONSTANT_Utf8_INFO的定义：
```
CONSTANT_Utf8_INFO{
	u1 tag;
	u2 length;
	u1 bytes[length];
}
```
使用字面量定义的字符串在class文件中是使用CONSTANT_Utf8_INFO存储的，而CONSTANT_Utf8_INFO中有u2 length；表明了该类型存储数据的长度；

u2是无符号的16位整数，因此理论上允许的最大长度是2^16=65536。而Java class文件是使用一种变体的UTF-8格式来存放字符串的，null值用两个字节来表述，因此值剩下65534个字节

### 4.2、运行期

上面的限制是使用 `String s = ""`这种字面值方式的定义的才会有限制；

String在运行期也是有限制的，也就是 Integer.MAX_VALUE，约为4G。在运行期，如果String的长度超过这个范围，就有可能抛出异常（JDK9之前）

# 八、String的使用技巧

## 1、数字前补0
```java
	String.format("%05d"， 1)
```

# 参考文章

* [java内存分配和String类型的深度解析](http://www.importnew.com/15671.html)
* [String类型相关问题](http://www.importnew.com/12845.html)
* [字符串常量池](http://blog.csdn.net/gaopeng0071/article/details/11741027)
* [JDK7-String源码](http://www.hollischuang.com/archives/99)
* [Java 7、8中的String.intern](http://www.importnew.com/12681.html)
* [深入解析String#intern](https://tech.meituan.com/in_depth_understanding_string_intern.html)
* [String、StringBuilder、StringBuffer](http://www.cnblogs.com/dolphin0520/p/3778589.html)
* [String对象创建问题](http://rednaxelafx.iteye.com/blog/774673/)