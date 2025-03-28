* String 核心点：字符串的堆栈和常量池

# 一、三个问题

- 1、Java 内存具体指哪块内存？这块内存区域为什么要进行划分？是如何划分的？划分之后每块区域的作用是什么？如何设置各个区域的大小？
- 2、String 类型在执行连接操作时，效率为什么会比 StringBuffer 或者 StringBuilder 低？ StringBuffer 和 StringBuilder 有什么联系和区别？
- 3、Java 中常量是指什么？String s = "s" 和 String s = new String("s") 有什么不一样？

# 二、Java 内存分配

## 1、JVM 的体系结构包含几个主要的子系统和内存区

- 垃圾回收器(Garbage Collection)：负责回收堆内存(Heap)中没有被使用的对象，即这些对象已经没有被引用了.
- 类装载子系统(Classloader Sub-System)：除了要定位和导入二进制class文件外，还必须负责验证被导入类的正确性，为类变量分配并初始化内存，以及帮助解析符号引用.
- 执行引擎(Execution Engine)：负责执行那些包含在被装载类的方法中的指令.
- 运行时数据区(Java Memory Allocation Area)：又叫虚拟机内存或者Java内存，虚拟机运行时需要从整个计算机内存划分一块内存区域存储许多东西.例如：字节码、从已装载的class文件中得到的其他信息、程序创建的对象、传递给方法的参数，返回值、局部变量等等；如：方法区，Java堆，虚拟机栈，程序计数器，本地方法栈；

## 2、Java 堆区域唯一目的是存放对象实例

几乎所有的对象实例都是在这里分配内存，但是这个对象引用却在栈(Stack)中分配，执行 String s = new String("s")时，

需要从两个地方分配内存：在堆中为String对象分配内存，在栈中为引用(这个堆对象的内存地址，即指针)分配内存：

	-Xms — 设置堆内存初始大小
	-Xmx — 设置堆内存最大值
	-XX:MaxTenuringThreshold — 设置对象在新生代中存活的次数
	-XX:PretenureSizeThreshold — 设置超过指定大小的大对象直接分配在旧生代中

# 三、String 类型

## 1、String 的本质

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

指的是在编译期被确定，并被保存在已编译的。class文件中的一些数据；它包括了关于类、方法、接口等中的常量，也包括字符串常量。常量池还具备动态性，运行期间可以将新的常量放入池中，虚拟机为每个被装载的类型维护一个常量池，池中为该类型所用常量的一个有序集合，包括直接常量 String、integer和 float 常量)和对其他类型、字段和方法的符号引用；

### 2.3、使用关键字new

在程序编译期，编译程序先去字符串常量池检查，是否存在"myString"，如果不存在，则在常量池中开辟一个内存空间存放"myString"；如果存在的话，则不用重新开辟空间，保证常量池中只有一个"myString"常量，节省内存空间，然后在内存堆中开辟一块空间存放new 出来的 String() 实例，在栈中开辟一块空间，命名为"s1"，存放的值为堆中 String 实例的内存地址，这个过程就是将引用s1指向new出来的String实例

### 2.4、直接定义

在程序编译期，编译程序先去字符串常量池检查，是否存在"myString"，如果不存在，则在常量池中开辟一个内存空间存放"myString"；如果存在的话，则不用重新开辟空间。然后在栈中开辟一块空间，命名为"s1"，存放的值为常量池中"myString"的内存地址

### 2.5、串联生成

其实际是通过StringBuilder的append方法来实现的，最后在调用toString方法

### 2.6、关于三个问题

- 堆中 new 出来的实例和常量池中的"myString"是什么关系呢？
- 常量池中的字符串常量与堆中的String对象有什么区别呢？
- 为什么直接定义的字符串同样可以调用String对象的各种方法呢？

## 3、String、StringBuffer、StringBuilder 的联系与区别

- StringBuffer 和 StringBuilder 都继承了抽象类 AbstractStringBuilder，这个抽象类和String一样也定义了`char[] value`和 `int count`，但是与String类不同的是，它们没有final修饰符。
	- String、StringBuffer和StringBuilder在本质上都是字符数组，不同的是，在进行连接操作时，String每次返回一个新的String实例，而StringBuffer和StringBuilder的append方法直接返回 this，所以这就是为什么在进行大量字符串连接运算时，不推荐使用 String，而推荐 StringBuffer 和 StringBuilder；
	- StringBuffer和StringBuilder默认 16 个字节数组的大小，超过默认的数组长度时扩容为`原来字节数组的长度 * 2 + 2`。所以使用StringBuffer和StringBuilder时可以适当考虑下初始化大小，以便通过减少扩容次数来提高代码的高效性；

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

- 为什么StringBuilder是非线程安全的？具体可能发生错误的代码地方在哪里？
	```java
	// StringBuilder的append方法
	public StringBuilder append(String str) {
		super.append(str);
		return this;
	}
	// super.append方法调用的是父类 AbstractStringBuilder 的append方法
	public AbstractStringBuilder append(String str) {
        if (str == null)
            return appendNull();
        int len = str.length();
        ensureCapacityInternal(count + len);
        str.getChars(0, len, value, count);
        count += len;
        return this;
    }
	```
	从代码中分析：
	- `count += len;`：这段代码其不是原子性操作，在多线程操作的时候可能出现问题；
	- `ensureCapacityInternal(count + len);`：
		```java
		private void ensureCapacityInternal(int minimumCapacity) {
			// 在这里操作的时候两个线程操作的时候正常应该扩容两个，但是在多线程环境下，可能存在只扩容了一个数量，那么在后面调用 System.arraycopy(value, srcBegin, dst, dstBegin, srcEnd - srcBegin); 的时候就会报越界操作
			if (minimumCapacity - value.length > 0) {
				value = Arrays.copyOf(value,
						newCapacity(minimumCapacity));
			}
		}
		```

- StringBuffer是JDK1.0就有的，而StringBuilder是JDK1.5之后才有的，JDK1.5是将StringBuffer中的部分功能移到 AbstractStringBuilder 中，再抽象出非线程安全但性能更高的StringBuilder；
- StringBuffer 和 StringBuilder 大部分都是在 AbstractStringBuilder 实现的，AbstractStringBuilder 在JDK9之前是以char数组来存储数据的，在JDK9之后都是byte数组加上一个标识编码的所谓 coder

### 3.1、StringBuilder 与 String 性能对比

#### 3.1.1、要点

写代码展示效率的差异、借助源代码的调用过程、分析时间复杂度，空间复杂度、调试验证

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
		数组初始容量为1，每次扩容，容量扩大至原来的2倍：`1 -> 2 -> 4 -> 8 -> 16 -> 32 -> 64 ... 65536`; 63356=2^16，故而，进行了 log2N 次开辟和释放
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
	- ①.同StringBuilder的append()，假设执行了65535次"+"，即：n=65535；那么，一共进行了多少次新对象、新数组的开辟，以及旧对象、旧数组的释放？每次"+"，要 new StringBuilder()，一共n次；每次"+"，要 `new char[str.length()+1]`一共n次，故而，进行了2n次的开辟和释放
	- ②.同样的道理，n=65535，复制了多少个字符？1、2、3、4、5、6 … 65535；根据等差数列求和公式()；Sn = 65535 * 65536 / 2；

在不同的版本之间，字符串通过"+"拼接，具体实现上有不同，上面是针对JDK8之前的版本，看如下代码：
```java
public class Concat {
    public static String concat(String str) {
        return str + "aa" + "bb";
    }
}
```
在不同版本下编译以及发编译，JDK8版本翻译如下：
```
> javap -v Concat
public static java.lang.String concat(java.lang.String);
    descriptor: (Ljava/lang/String;)Ljava/lang/String;
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=1, args_size=1
         0: new           #2                  // class java/lang/StringBuilder
         3: dup
         4: invokespecial #3                  // Method java/lang/StringBuilder."<init>":()V
         7: aload_0
         8: invokevirtual #4                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        11: ldc           #5                  // String aabb
        13: invokevirtual #4                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        16: invokevirtual #6                  // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
        19: areturn
      LineNumberTable:
        line 11: 0
```
而在 JDK9之后的版本 中，反编译的结果就会有点特别了，片段是：
```
public static java.lang.String concat(java.lang.String);
    descriptor: (Ljava/lang/String;)Ljava/lang/String;
    flags: (0x0009) ACC_PUBLIC, ACC_STATIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokedynamic #2,  0              // InvokeDynamic #0:makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;
         6: areturn
      LineNumberTable:
        line 6: 0
}
SourceFile: "Concat.java"
InnerClasses:
  public static final #27= #26 of #30;    // Lookup=class java/lang/invoke/MethodHandles$Lookup of class java/lang/invoke/MethodHandles
BootstrapMethods:
  0: #15 REF_invokeStatic java/lang/invoke/StringConcatFactory.makeConcatWithConstants:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;
```
从上面可以看到：
非静态的拼接逻辑在 JDK 8 中会自动被 javac 转换为 StringBuilder 操作；而在 JDK 9 里面，则是体现了思路的变化。Java 9 利用 InvokeDynamic，将字符串拼接的优化与 javac 生成的字节码解耦，假设未来 JVM 增强相关运行时实现，将不需要依赖 javac 的任何修改；

JDK11的版本中，是基于StringConcatFactory.makeConcatWithConstants动态生成一个方法来实现。这个会比StringBuilder更快，不需要创建StringBuilder对象，也会减少一次数组拷贝；这里由于是内部使用的数组，所以用了UNSAFE.allocateUninitializedArray的方式更快分配byte[]数组。通过：StringConcatFactory.makeConcatWithConstants

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
- String 的equals 方法都对参数进行类型校验： instanceof String，因为 StringBuffer(StringBuilder)父类为 CharSequence，所以不相等；
- StringBuffer 没有重写 Object 的 equals 方法，所以 Object 的 equals 方法实现是 == 判断，故为 false；
- 因为 Object 的 toString 方法返回为 String 类型，String 重写了 equals 方法实现为值比较；

# 四、String类

## 1、各版本变化

- 在 Java6 以及之前的版本中，String 对象是对 char 数组进行了封装实现的对象，主要有四个成员变量：char 数组、偏移量 offset、字符数量 count、哈希值 hash。

String 对象是通过 offset 和 count 两个属性来定位 char[] 数组，获取字符串。这么做可以高效、快速地共享数组对象，同时节省内存空间，但这种方式很有可能会导致内存泄漏。

- 从 Java7 版本开始到 Java8 版本，Java 对 String 类做了一些改变。String 类中不再有 offset 和 count 两个变量了。这样的好处是 String 对象占用的内存稍微少了些，同时，String.substring 方法也不再共享 char[]，从而解决了使用该方法可能导致的内存泄漏问题。

- 从 Java9 版本开始，工程师将 char[] 字段改为了 byte[] 字段，又维护了一个新的属性 coder，它是一个编码格式的标识：一个 char 字符占 16 位，2 个字节。这个情况下，存储单字节编码内的字符（占一个字节的字符）就显得非常浪费，JDK1.9 的 String 类为了节约内存空间，于是使用了占 8 位，1 个字节的 byte 数组来存放字符串，而新属性 coder 的作用是，在计算字符串长度或者使用 indexOf（）函数时，我们需要根据这个字段，判断如何计算字符串长度。coder 属性默认有 0 和 1 两个值，0 代表 Latin-1（单字节编码），1 代表 UTF-16。如果 String 判断字符串只包含了 Latin-1，则 coder 属性值为 0，反之则为 1

## 2、不可变好处

String 类被 final 关键字修饰了，而且变量 char 数组也被 final 修饰了，Java 实现的这个特性叫作 String 对象的不可变性，即 String 对象一旦创建成功，就不能再对它进行改变

> 如果一个对象，在它创建完成之后，不能再改变它的状态，那么这个对象就是不可变的，不能改变状态的意思是：不能改变对象内的成员变量，包括基本数据类型的值不能改变，引用类型的变量不能指向其他的对象，引用类型指向的对象的状态也不能改变；

对象与对象引用：
```java
String s = "ABCabc";
s = "123456";
```
- s只是一个 String 对象的引用，并不是对象本身，对象在内存中是一块内存区，成员变量越多，这块内存区占的空间越大。引用只是一个4字节的数据，里面存放了它所指向的对象的地址，通过这个地址可以访问对象
- s只是一个引用，它指向了一个具体的对象，当s="123456"; 这句代码执行过之后，又创建了一个新的对象"123456"，而引用s重新指向了这个新的对象，原来的对象"ABCabc"还在内存中存在，并没有改变

**不可变的好处：**
- 保证 String 对象的安全性。假设 String 对象是可变的，那么 String 对象将可能被恶意修改。
- 保证 hash 属性值不会频繁变更，确保了唯一性，使得类似 HashMap 容器才能实现相应的 key-value 缓存功能
- 可以实现字符串常量池。在 Java 中，通常有两种创建字符串对象的方式，一种是通过字符串常量的方式创建，如 String str=“abc”；另一种是字符串变量通过 new 形式的创建，如 String str = new String(“abc”)

# 五、源码分析

- String 表示字符串，Java 中所有字符串的字面值都是 String 类的实例，例如"ABC"。字符串是常量，在定义之后不能被改变，字符串缓冲区支持可变的字符串.因为 String 对象是不可变的，所以可以共享它们

	String str = "abc"; <==> char[] data = {'a'，'b'，'c'}; String str = new String(data);

- Java 语言提供了对字符串连接运算符的特别支持(+)，该符号也可用于将其他类型转换成字符串。字符串的连接实际上是通过 StringBuffer 或者 StringBuilder 的append()方法来实现的，字符串的转换通过toString方法实现，该方法由 Object 类定义，并可被 Java 中的所有类继承；

## 1、定义：(与 JDK8 一致)

```java
public final class String implements java.io.Serializable， Comparable<String>， CharSequence{}
```
从该类的声明中我们可以看出 String 是 final 类型的，表示该类不能被继承，同时该类实现了三个接口：表示可序列化、可比较、是字符序列

### 1.1、String 为什么要设计成不可变

- 字符串池：字符串池是方法区中的一部分特殊存储.当一个字符串被被创建的时候，首先会去这个字符串池中查找，如果找到，直接返回对该字符串的引用；如果字符串可变的话，当两个引用指向指向同一个字符串时，对其中一个做修改就会影响另外一个；
- 缓存hashcode：String 类不可变，所以一旦对象被创建，该hash值也无法改变.所以，每次想要使用该对象的hashcode的时候，直接返回即可；这就使得字符串很适合作为 Map 中的键，字符串的处理速度要快过其它的键对象.这就是 HashMap 中的键往往都使用字符串；
- 使其他类的使用更加便利：如对于 set 的操作；
- 安全性：String 被广泛的使用在其他 Java 类中充当参数，如果字符串可变，那么类似操作可能导致安全问题，可变的字符串也可能导致反射的安全问题，因为他的参数也是字符串；类加载器要用到字符串，不可变性提供了安全性，以便正确的类被加载；
- 不可变对象天生就是线程安全的：因为不可变对象不能被改变，所以他们可以自由地在多个线程之间共享。不需要任何同步处理？
- 如果字符串是可变的则会引起很严重的安全问题，譬如数据库的用户名密码都是以字符串的形式传入来获得数据库的连接，或者在 socket 编程中主机名和端口都是以字符串的形式传入，因为字符串是不可变的，所以它的值是不可改变的，否则黑客们可以钻到空子改变字符串指向的对象的值造成安全漏洞；
	
## 2、属性

- private final char value[];

	这是一个字符数组，并且是 final 类型，他用于存储字符串内容，从 final 这个关键字中我们可以看出，String 的内容一旦被初始化了是不能被更改的. 虽然有这样的例子： String s = "a"; s = "b" 但是，这并不是对s的修改，而是重新指向了新的字符串， 从这里我们也能知道，String 其实就是用 char[] 实现的

- private int hash;

	缓存字符串的hash Code，默认值为 0

- private static final long serialVersionUID = -6849794470754667710L;

	private static final ObjectStreamField[] serialPersistentFields = new ObjectStreamField[0];

	Java 的序列化机制是通过在运行时判断类的serialVersionUID来验证版本一致性的.在进行反序列化时，JVM 会把传来的字节流中的serialVersionUID与本地相应实体(类)的serialVersionUID进行比较，如果相同就认为是一致的，可以进行反序列化，否则就会出现序列化版本不一致的异常(InvalidCastException)；

**关于底层存储的变化：**

在 Java 9 中，引入了 Compact Strings 的设计，对字符串进行了大刀阔斧的改进。将数据存储方式从 char 数组，改变为一个 byte 数组加上一个标识编码的所谓 coder，并且将相关字符串操作类都进行了修改；当然，在极端情况下，字符串也出现了一些能力退化，比如最大字符串的大小。你可以思考下，原来 char 数组的实现，字符串的最大长度就是数组本身的长度限制，但是替换成 byte 数组，同样数组长度下，存储能力是退化了一倍的

## 3、构造方法

### 3.1、使用字符数组、字符串构造一个 String

- 使用一个字符数组来创建一个 String，那么这里值得注意的是，当我们使用字符数组创建 String 的时候，会用到 Arrays.copyOf方法和 Arrays.copyOfRange方法，这两个方法是将原有的字符数组中的内容逐一的复制到 String 中的字符数组中;

	当然，在使用字符数组来创建一个新的 String 对象的时候，不仅可以使用整个字符数组，也可以使用字符数组的一部分，只要多传入两个参数 int offset和 int count就可以了

- 可以用一个 String 类型的对象来初始化一个 String。这里将直接将源 String 中的value和hash两个属性直接赋值给目标 String.因为String一旦定义之后是不可以改变的，所以也就不用担心改变源 String 的值会影响到目标 String 的值

### 3.2、使用字节数组构造一个 String

- String 实例中保存有一个 `char[]字符数组`，`char[]`字符数组是以unicode码来存储的，String 和 char 为内存形式，byte 是网络传输或存储的序列化形式
- `String(byte[] bytes， Charset charset)`是指通过charset来解码指定的 byte 数组，将其解码成unicode的`char[]`数组，够造成新的 String;

	也可构造字节数组的部分 `String(byte bytes[]) String(byte bytes[]， int offset， int length)`

- 使用如下四种构造方法，就会使用 StringCoding.decode方法进行解码，使用的解码的字符集就是我们指定的charsetName或者charset
	```java
	String(byte bytes[], Charset charset)
	String(byte bytes[], String charsetName)
	String(byte bytes[], int offset, int length, Charset charset)
	String(byte bytes[], int offset, int length, String charsetName)
	```
	注意：在使用 byte[]构造 String 的时候，如果没有指明解码使用的字符集的话，那么 StringCoding 的decode方法首先调用系统的默认编码格式，如果没有指定编码格式则默认使用 `ISO-8859-1`编码格式进行编码操作：

### 3.3、使用 StringBuffer 和 StringBuilder 构造一个 String

StringBuffer 和 StringBuilder 也可以被当做构造String的参数；Java 的官方文档有提到说使用 StringBuilder 的toString方法会更快一些，原因是 StringBuffer 的toString方法是 synchronized 的

### 3.4、一个特殊的保护类型的构造方法：(JDK7 以上版本)

```java
String(char[] value, boolean share) {
	// assert share ： "unshared not supported";
	this.value = value;
}
```
该方法和 String(char[] value)有两点区别
- 该方法多了一个参数： boolean share，其实这个参数在方法体中根本没被使用，也给了注释，目前不支持使用 false，只使用 true，加入这个share的只是为了区分于 String(char[] value)方法
- 第二个区别就是具体的方法实现不同，这个方法构造出来的 String 和参数传过来的 char[] value共享同一个数组

**为什么Java会提供这样一个方法呢**

- 优点：首先性能好，一个是直接给数组赋值（相当于直接将 String 的value的指针指向 char[]数组），一个是逐一拷贝。当然是直接赋值快了；其次，共享内部数组节约内存
- 该方法之所以设置为 protected，是因为一旦该方法设置为公有，在外面可以访问的话，那就破坏了字符串的不可变性：
	```java
	char[] arr = new char[] {'h'， 'e'， 'l'， 'l'， 'o'， ' '， 'w'， 'o'， 'r'， 'l'， 'd'};
	String s = new String(0， arr.length， arr); // "hello world"
	arr[0] = 'a'; // replace the first character with 'a'
	System.out.println(s); // aello world
	```
	如果构造方法没有对arr进行拷贝，那么其他人就可以在字符串外部修改该数组，由于它们引用的是同一个数组，因此对arr的修改就相当于修改了字符串

**在Java7之前很多String里面的方法都使用这种"性能好的、节约内存的、安全"的构造函数**

substring、replace、concat、valueOf等方法（实际上他们使用的是 public String(char[]， int， int)方法，原理和本方法相同，已经被本方法取代）

**在 Java 7 中，substring已经不再使用这种"优秀"的方法了，为什么呢**

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
public String repeat(int count);// JDK11增加的方法：返回一个重复count次的字符串
```

### 4.1、getBytes()

将一个字符串转换成字节数组，那么String提供了很多重载的getBytes方法；值得注意的是，在使用这些方法的时候一定要注意编码问题，一般为了保持跟机器环境无关需要指定编码方式
```java
public byte[] getBytes()
void getBytes(byte dst[], int dstBegin, byte coder)
public byte[] getBytes(Charset charset)
public byte[] getBytes(String charsetName)
```
示例：
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

- 第四个和前三个唯一的区别就是他会将两个字符数组的内容都使用toUpperCase方法转换成大写再进行比较，以此来忽略大小写进行比较。相同则返回 true，不想同则返回 false

另外还有compare的相关方法
```java
int compareTo(String anotherString)；
int compareToIgnoreCase(String str)；
boolean regionMatches(int toffset， String other， int ooffset，int len)  //局部匹配
boolean regionMatches(boolean ignoreCase， int toffset，String other， int ooffset， int len)   //局部匹配
```

### 4.3、hashCode()

hashCode的实现其实就是使用数学公式： `s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]`

`s[i]`是string的第i个字符，n是String的长度，那为什么这里用31，而不是其它数呢？ 计算机的乘法涉及到移位计算。

当一个数乘以2时，就直接拿该数左移一位即可！选择31原因是因为31是一个素数！

**为什么选择质数：31**
- 31是一个不大不小的质数，是作为 hashCode 乘子的优选质数之一；一般在设计哈希算法时，会选择一个特殊的质数。至于为啥选择质数，应该是可以降低哈希算法的冲突率；
- 31可以被 JVM 优化：`31 * i = (i << 5) - i`；选择数字31是因为它是一个奇质数，如果选择一个偶数会在乘法运算中产生溢出，导致数值信息丢失，因为乘二相当于移位运算；
- 如果选择的质数太大，比较容易导致溢出，而较小又有可能哈希冲突率高；

### 4.4、substring

Java 7 中的substring方法使用 `String(value， beginIndex， subLen)`方法创建一个新的String并返回，这个方法会将原来的 char[]中的值逐一复制到新的String中，两个数组并不是共享的，虽然这样做损失一些性能，但是有效地避免了内存泄露
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

**JDK6中的substring**

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
- JDK 6中的substring导致的问题：如果你有一个很长很长的字符串，但是当你使用substring进行切割的时候你只需要很短的一段.这可能导致性能问题，因为你需要的只是一小段字符序列，但是你却引用了整个字符串(因为这个非常长的字符数组一直在被引用，所以无法被回收，就可能导致内存泄露)，
	
	在 JDK 6中，一般用以下方式来解决该问题，原理其实就是生成一个新的字符串并引用他：x = x.substring(x, y) + ""
		
**JDK 7 中的substring**

上面提到的问题，在jdk 7中得到解决.在jdk 7 中，substring方法会在堆内存中创建一个新的数组

**substring的细节**

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
其实 String 对 + 的支持其实就是使用了 StringBuilder 以及他的append、toString两个方法，前面也提到在JDK9之后使用了另外一种方式

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

```java
public native String intern();
```

### 9.1、Java常量池

Java 中8种基本类型和一种比较特殊的类型 String，常量池就类似一个 JAVA 系统级别提供的缓存，8种基本类型的常量池都是系统协调的，String 类型的常量池比较特殊。它的主要使用方法有两种：
- 直接使用双引号声明出来的String对象会直接存储在常量池中;
- 如果不是用双引号声明的 String 对象，可以使用 String 提供的intern方法。intern 方法会从字符串常量池中查询当前字符串是否存在，若不存在就会将当前字符串放入常量池中；

打印常量池：`-XX:+PrintStringTableStatistics`

### 9.2、intern 的实现原理

如果常量池中存在当前字符串，就会直接返回当前字符串。如果常量池中没有此字符串，会将此字符串放入常量池中后，再返回
- 大体实现结构就是：JAVA 使用 jni 调用c++实现的 StringTable 的 intern方法， StringTable 的intern方法跟Java 中的 HashMap 的实现是差不多的，只是不能自动扩容。默认大小是 1009;
- String 的 String Pool 是一个固定大小的 Hashtable，默认值大小长度是1009，如果放进 String Pool的 String 非常多，就会造成 Hash 冲突严重，从而导致链表会很长，而链表长了后直接会造成的影响就是当调用 String.intern时性能会大幅下降
- JDK6 中 StringTable 是固定的，就是 1009 的长度，所以如果常量池中的字符串过多就会导致效率下降很快；在jdk7中，StringTable 的长度可以通过一个参数指定： `-XX:StringTableSize=99991`；从 Java7u40 开始，该默认值增大到 60013

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
	- 如果是使用引号声明的字符串都是会直接在字符串常量池中生成，而 new 出来的 String 对象是放在 JAVA Heap 区域
	- 所以拿一个 JAVA Heap 区域的对象地址和字符串常量池的对象地址进行比较肯定是不相同的，即使调用 String.intern方法也是没有任何关系的

- 上述 JDK7 中的解释：
	- 需要注意的一点：在 Jdk6 以及以前的版本中，字符串的常量池是放在堆的 Perm 区的，Perm 区是一个类静态的区域，主要存储一些加载类的信息、常量池、方法片段等内容，默认大小只有4m，一旦常量池中大量使用 intern 是会直接产生 java.lang.OutOfMemoryError： PermGen space错误的

	- 在 JDK7 的版本中，字符串常量池已经从 Perm 区移到正常的 Java Heap 区域了。为什么要移动？Perm 区域太小是一个主要原因，当然据消息称 JDK8 已经直接取消了 Perm 区域，而新建立了一个元空间，应该是 jdk 开发者认为 Perm 区域已经不适合现在 JAVA 的发展了

	- intern方法还是会先去查询常量池中是否有已经存在，如果存在，则返回常量池中的引用，这一点与之前没有区别，区别在于，如果在常量池找不到对应的字符串，则不会再将字符串拷贝到常量池，而只是在常量池中生成一个对原字符串的引用
	- 代码片段1：
		- 在第一段代码中，先看s3和s4字符串：String s3 = new String("1") + new String("1");这句代码中现在生成了 2 最终个对象，是字符串常量池中的"1"和 JAVA Heap 中的 s3引用指向的对象。中间还有2个匿名的 new String("1")我们不去讨论它们.此时s3引用对象内容是"11"，但此时常量池中是没有 "11"对象的.
		- 接下来s3.intern();这一句代码，是将 s3中的"11"字符串放入 String 常量池中，因为此时常量池中不存在"11"字符串，因此常规做法是跟 jdk6 图中表示的那样，在常量池中生成一个 "11"的对象，关键点是 jdk7 中常量池不在 Perm 区域了，这块做了调整。常量池中不需要再存储一份对象了，可以直接存储堆中的引用.这份引用指向 s3 引用的对象.也就是说引用地址是相同的.
		- 最后String s4 = "11"; 这句代码中"11"是显示声明的，因此会直接去常量池中创建，创建的时候发现已经有这个对象了，此时也就是指向 s3 引用对象的一个引用.所以 s4 引用就指向和 s3 一样了.因此最后的比较 s3 == s4 是 true.
		- 再看 s 和 s2 对象. String s = new String("1"); 第一句代码，生成了2个对象。常量池中的"1" 和 JAVA Heap 中的字符串对象。s.intern(); 这一句是 s 对象去常量池中寻找后发现 "1" 已经在常量池里了
		- 接下来String s2 = "1"; 这句代码是生成一个 s2 的引用指向常量池中的"1"对象。结果就是 s 和 s2 的引用地址明显不同。
	- 代码片段2：
		- 第一段代码和第二段代码的改变就是 s3.intern(); 的顺序是放在String s4 = "11";后了。这样，首先执行String s4 = "11";声明 s4 的时候常量池中是不存在"11"对象的，执行完毕后，"11"对象是 s4 声明产生的新对象.然后再执行s3.intern();时，常量池中"11"对象已经存在了，因此 s3 和 s4 的引用是不同的.
		- 第二段代码中的 s 和 s2 代码中，s.intern();，这一句往后放也不会有什么影响了，因为对象池中在执行第一句代码 String s = new String("1");的时候已经生成"1"对象了.下边的s2声明都是直接从常量池中取地址引用的。s 和 s2 的引用地址是不会相等的；

#### 9.3.3、总结

从上述的例子代码可以看出 jdk7 版本对 intern 操作和常量池都做了一定的修改，主要包括2点
- 将 String常量池 从 Perm 区移动到了 Java Heap区
- `String#intern` 方法时，如果存在堆中的对象，会直接保存对象的引用，而不会重新创建对象；

### 9.4、intern 的使用

**正确使用：**
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
- 通过上述结果，发现不使用 intern 的代码生成了1000w 个字符串，占用了大约640m 空间。使用了 intern 的代码生成了1345个字符串，占用总空间 133k 左右。其实通过观察程序中只是用到了10个字符串，所以准确计算后应该是正好相差100w 倍
- 使用了 intern 方法后时间上有了一些增长，这是因为程序中每次都是用了 new String() 后，然后又进行 intern 操作的耗时时间，这一点如果在内存空间充足的情况下确实是无法避免的；

> 使用 intern 方法需要注意的一点是，一定要结合实际场景。因为常量池的实现是类似于一个 HashTable 的实现方式，HashTable 存储的数据越大，遍历的时间复杂度就会增加。如果数据过大，会增加整个字符串常量池的负担

**不正确使用：** fastjson 中对所有的 json 的 key 使用了 intern 方法，缓存到了字符串常量池中，这样每次读取的时候就会非常快，大大减少时间和空间.而且 json 的 key 通常都是不变的.这个地方没有考虑到大量的 json key 如果是变化的，那就会给字符串常量池带来很大的负担

## 10、indexOf方法

## 11、split方法

Split() 方法使用了正则表达式实现了其强大的分割功能，而正则表达式的性能是非常不稳定的，使用不恰当会引起回溯问题，很可能导致 CPU 居高不下。

所以应该慎重使用 Split() 方法，可以用 String.indexOf() 方法代替 Split() 方法完成字符串的分割。如果实在无法满足需求，在使用 Split() 方法时，对回溯问题需要加以重视


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
`u == v` ==> false;

如果 m 改为 final 修饰：`u == v` ==> true;

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

`CONSTANT_Utf8_INFO`是一个`CONSTANT_Utf8`类型的常量池数据项，它存储的是一个常量字符串。常量池中的所有字面量几乎都是通过`CONSTANT_Utf8_INFO`描述的。`CONSTANT_Utf8_INFO`的定义：
```
CONSTANT_Utf8_INFO{
	u1 tag;
	u2 length;
	u1 bytes[length];
}
```
使用字面量定义的字符串在class文件中是使用`CONSTANT_Utf8_INFO`存储的，而`CONSTANT_Utf8_INFO`中有u2 length；表明了该类型存储数据的长度；

u2是无符号的16位整数，因此理论上允许的最大长度是`2^16=65536`。而Java class文件是使用一种变体的UTF-8格式来存放字符串的，null值用两个字节来表述，因此值剩下65534个字节

### 4.2、运行期

上面的限制是使用 `String s = ""`这种字面值方式的定义的才会有限制；

String在运行期也是有限制的，也就是 `Integer.MAX_VALUE`，约为4G。在运行期，如果String的长度超过这个范围，就有可能抛出异常（JDK9之前）

## 5、String为什么是不可变的

- 保存字符串的数组被 final 修饰且为私有的，并且String 类没有提供/暴露修改这个字符串的方法；
- String 类被 final 修饰导致其不能被继承，进而避免了子类破坏 String 不可变；

## 6、Java 9 为何要将 String 的底层实现由 char[] 改成了 byte[]

新版的 String 其实支持两个编码方案：Latin-1 和 UTF-16。如果字符串中包含的汉字没有超过 Latin-1 可表示范围内的字符，那就会使用 Latin-1 作为编码方案。Latin-1 编码方案下，byte 占一个字节(8 位)，char 占用 2 个字节（16），byte 相较 char 节省一半的内存空间。

如果字符串中包含的汉字超过 Latin-1 可表示范围内的字符，byte 和 char 所占用的空间是一样的；

## 27、关于String +和StringBuffer的比较

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
**7.1、String+的写法要比 Stringbuffer 快，是因为在编译这段程序的时候，编译器会进行常量优化。**

它会将a、b、c直接合成一个常量abc保存在对应的 class 文件当中，看如下反编译的代码：
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
原因是因为 String+其实是由 StringBuilder 完成的，而一般情况下 StringBuilder 要快于 StringBuffer，这是因为 StringBuilder 线程不安全，少了很多线程锁的时间开销，因此这里依然是 string+的写法速度更快;

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
**7.2、字符串拼接方式：+、concat() 以及 append() 方法，append()速度最快，concat()次之，+最慢**

- 编译器对+进行了优化，它是使用 StringBuilder 的 append() 方法来进行处理的，编译器使用 append() 方法追加后要同 toString() 转换成 String 字符串，变慢的关键原因就在于 new StringBuilder()和toString()，这里可是创建了 10 W 个 StringBuilder 对象，而且每次还需要将其转换成 String

- concat：
	concat() 的源码，它看上去就是一个数字拷贝形式，我们知道数组的处理速度是非常快的，但是由于该方法最后是这样的：
	`return new String(0， count + otherLen， buf);`这同样也创建了 10 W 个字符串对象，这是它变慢的根本原因

- append() 方法拼接字符串：并没有产生新的字符串对象；

# 八、String的使用技巧

## 1、数字前补0

```java
String.format("%05d"， 1)
```
输出二进制前面补0：输出二进制需要前面补齐总共32位，可以借助 commons-lang3包下得 StringUtils 类： `StringUtils.leftPad(str, 32, '0');`

## 2、首字母小写

```java
public static String captureName(String name) {
	char[] cs = name.toCharArray();
	if (cs[0] >= 'A' && cs[0] <= 'Z') {
		cs[0] += 32;
	}
	return String.valueOf(cs);
}
```


# 参考文章

* [JDK7-String源码](http://www.hollischuang.com/archives/99)
* [String、StringBuilder、StringBuffer](http://www.cnblogs.com/dolphin0520/p/3778589.html)
* [String对象创建问题](http://rednaxelafx.iteye.com/blog/774673/)
* [为什么计算Hash使用31](https://www.cnblogs.com/nullllun/p/8350178.html)
* [深入解析String#intern](https://tech.meituan.com/2014/03/06/in-depth-understanding-string-intern.html)
* [Java字符串拼接技术演进](https://mp.weixin.qq.com/s/JDil0hDZD3M7Zk_13Ppj9w)
* [String全解析](http://keaper.cn/2020/09/08/java-string-mian-mian-guan/)
* [String from JDK1~JDK21](https://www.unlogged.io/post/java-and-the-string-odyssey-navigating-changes-from-jdk-1-to-jdk-21)
