# 一、Number

# 二、BigDecimal


## 1、源码分析

### 1.1、签名

```java
public class BigDecimal extends Number implements Comparable<BigDecimal>
```

### 1.2、构造函数

### 1.3、属性

### 1.4、方法

## 2、注意问题

### 2.1、double和float转BigDecimal

将`double`或者`float`数据转换为`BigDecimal`类型，最好使用`BigDecimal.valueOf()`，而不应该使用`new BigDecimal(double)`，因为：`new BigDecimal(0.1`)会把`0.1`当成: `0.1000000000000000055511151231257827021181583404541015625`，而`valueOf`其内部使用了`String`作为构造函数的入参，所以使用valueOf后`0.1`还是`0.1`

### 2.2、BigDecimal对象比较问题

看如下代码，输出是什么？
```java
BigDecimal bigDecimal = new BigDecimal(1);
BigDecimal bigDecimal1 = new BigDecimal(1);
System.out.println(bigDecimal.equals(bigDecimal1)); // true

BigDecimal bigDecimal2 = new BigDecimal(1);
BigDecimal bigDecimal3 = new BigDecimal(1.0);
System.out.println(bigDecimal2.equals(bigDecimal3)); // true

BigDecimal bigDecimal4 = new BigDecimal("1");
BigDecimal bigDecimal5 = new BigDecimal("1.0");
System.out.println(bigDecimal4.equals(bigDecimal5)); // false
```
在使用BigDecimal的equals方法对1和1.0进行比较的时候，有的时候是true（当使用int、double定义BigDecimal时），有的时候是false（当使用String定义BigDecimal时）

首先看equals方法的描述：`Compares this BigDecimal with the specified Object for equality.  Unlike compareTo, this method considers two BigDecimal objects equal only if they are equal in value and scale (thus 2.0 is not equal to 2.00 when compared by  this method)`

意思是：equals方法和compareTo并不一样，equals方法会比较两部分内容，分别是值（value）和精度（scale）
```java
public boolean equals(Object x) {
    if (!(x instanceof BigDecimal))
        return false;
    BigDecimal xDec = (BigDecimal) x;
    if (x == this)
        return true;
    if (scale != xDec.scale) // 比较精度
        return false;
    long s = this.intCompact;
    long xs = xDec.intCompact;
    if (s != INFLATED) {
        if (xs == INFLATED)
            xs = compactValFor(xDec.intVal);
        return xs == s;
    } else if (xs != INFLATED)
        return xs == compactValFor(this.intVal);

    return this.inflated().equals(xDec.inflated());
}
```

BigDecimal不同的构造函数，对应的精度是不同的

**BigDecimal(long) 和BigDecimal(int)：**因为是整数，所以精度就是0；
```java
public BigDecimal(int val) {
    this.intCompact = val;
    this.scale = 0;
    this.intVal = null;
}
public BigDecimal(long val) {
    this.intCompact = val;
    this.intVal = (val == INFLATED) ? INFLATED_BIGINT : null;
    this.scale = 0;
}
```

**BigDecimal(double)：**当我们使用`new BigDecimal(0.1)`创建一个BigDecimal 的时候，其实创建出来的值并不是整好等于0.1的，而是`0.1000000000000000055511151231257827021181583404541015625` 。这是因为doule自身表示的只是一个近似值，那么他的精度就是这个数字的位数，即55
```java
public BigDecimal(double val) {
    this(val, MathContext.UNLIMITED);
}
public BigDecimal(double val, MathContext mc) {
    ....
}
```

**BigDecimal(string)：**当我们使用`new BigDecimal("0.1")`创建一个BigDecimal 的时候，其实创建出来的值正好就是等于`0.1`的。那么他的精度也就是1；如果使用`new BigDecimal("0.10000")`，那么创建出来的数就是0.10000，精度也就是5。

BigDecimal中提供了compareTo方法，这个方法就可以只比较两个数字的值，如果两个数相等，则返回0
```java
public int compareTo(BigDecimal val) {
    // Quick path for equal scale and non-inflated case.
    if (scale == val.scale) {
        long xs = intCompact;
        long ys = val.intCompact;
        if (xs != INFLATED && ys != INFLATED)
            return xs != ys ? ((xs > ys) ? 1 : -1) : 0;
    }
    int xsign = this.signum();
    int ysign = val.signum();
    if (xsign != ysign)
        return (xsign > ysign) ? 1 : -1;
    if (xsign == 0)
        return 0;
    int cmp = compareMagnitude(val);
    return (xsign > 0) ? cmp : -cmp;
}
```
如果把值为 1.0 的 BigDecimal 加入 HashSet，然后判断其是否存在值为 1 的 BigDecimal，得到的结果是 false，解决这个问题的办法有两个：
- 第一个方法是，使用 TreeSet 替换 HashSet。TreeSet 不使用 hashCode 方法，也不使用 equals 比较元素，而是使用 compareTo 方法，所以不会有问题；
    ```java
    Set<BigDecimal> treeSet = new TreeSet<>();
    treeSet.add(new BigDecimal("1.0"));
    System.out.println(treeSet.contains(new BigDecimal("1")));//返回true
    ```
- 第二个方法是，把 BigDecimal 存入 HashSet 或 HashMap 前，先使用 stripTrailingZeros 方法去掉尾部的零，比较的时候也去掉尾部的 0，确保 value 相同的 BigDecimal，scale 也是一致的：
    ```java
    Set<BigDecimal> hashSet2 = new HashSet<>();
    hashSet2.add(new BigDecimal("1.0").stripTrailingZeros());
    System.out.println(hashSet2.contains(new BigDecimal("1.000").stripTrailingZeros()));//返回true
    ```

### 2.3、关于小数位数

BigDecimal 中有一些函数有一个参数 MathContext，该参数表示设置精度和舍入方法：
```java
public MathContext(int setPrecision,  RoundingMode setRoundingMode) {
    if (setPrecision < MIN_DIGITS)
        throw new IllegalArgumentException("Digits < 0");
    if (setRoundingMode == null)
        throw new NullPointerException("null RoundingMode");

    precision = setPrecision;
    roundingMode = setRoundingMode;
    return;
}
```
比如：new MathContext(2, RoundingMode.HALF_UP)，表示保留两位有效数字，且使用四舍五入方式；

如果要设置小数，一般调用 setScale 方法即可

# 三、BigInteger

# 四、Integer

## 1、类定义

```java
public final class Integer extends Number implements Comparable<Integer>{
}
```
- Integer类不能被继承；
- Integer类实现了Comparable 接口，所以可以用compareTo进行比较并且 Integer 对象只能和 Integer 类型的对象进行比较，不能和其他类型比较(至少调用compareTo方法无法比较)；
- Integer继承了Number 类，所以该类可以调用longValue、floatValue、doubleValue等系列方法返回对应的类型的值；
- Integer 类在对象中包装了一个基本类型 int 的值。Integer 类型的对象包含一个 int 类型的字段

## 2、属性

### 2.1、私有属性

`private final int value;`

Integer 对象中真正保存 int 值的，我们使用 new Integer(10)创建一个 Integer 对象的时候，就会用以下形式给value赋值
- 关于 Integer 对象的"可变性"：从value的定义形式中可以看出value被定义成final类型.也就说明，一旦一个Integer对象被初始化之后，就无法再改变value的值

看如下代码
```java
Integer i = new Integer(10);
i = 5;
```
查看反编译的代码：
```java
Integer i = new Integer(10);
i = Integer.valueOf(5);
```
i=5操作并没有改变使用 Integer i = new Integer(10)；创建出来的i中的value属性的值.要么是直接返回一个已有对象，要么新建一个对象；这里跟 valueOf 的实现细节相关

### 2.2、公共属性

```java
//值为 -2^31 的常量，它表示 int 类型能够表示的最小值。
public static final int   MIN_VALUE = 0x80000000;
//值为 2^31-1 的常量，它表示 int 类型能够表示的最大值。
public static final int   MAX_VALUE = 0x7fffffff;  
//表示基本类型 int 的 Class 实例。
public static final Class<Integer>  TYPE = (Class<Integer>) Class.getPrimitiveClass("int");
//用来以二进制补码形式表示 int 值的比特位数。
public static final int SIZE = 32;
//用来以二进制补码形式表示 int 值的字节数。since java8
public static final int BYTES = SIZE / Byte.SIZE;
```
以上属性可直接使用，因为他们已经定义成 publis static final 能用的时候尽量使用他们，这样不仅能使代码有很好的可读性，也能提高性能节省资源

## 3、方法

### 2.1、Integer构造方法

```java
//构造一个新分配的 Integer 对象，它表示指定的 int 值。
public Integer(int value) {
	this.value = value；
}
//构造一个新分配的 Integer 对象，它表示 String 参数所指示的 int 值。
public Integer(String s) throws NumberFormatException {
	this.value = parseInt(s， 10)；
}
```
从构造方法中我们可以知道，初始化一个 Integer 对象的时候只能创建一个十进制的整数

### 2.2、valueOf

```java
public static Integer valueOf(int i)；
```
- 通常情况下： `IntegerCache.low=-128，IntegerCache.high=127`，除非显示声明` java.lang.Integer.IntegerCache.high` 的值），Integer 中有一段动态代码块，该部分内容会在 Integer 类被加载的时候就执行：
```java
// 虚拟机初始化时，该段代码就加载，通过 VM 参数： -XX:AutoBoxCacheMax=<size> 控制其初始化
// java.lang.Integer.IntegerCache.high property may be set and saved in the 
// private system properties in the sun.misc.VM class
private static class IntegerCache{
	static final int low = -128;
	static final int high;
	static final Integer cache[];
	static {
		// high value may be configured by property
		int h = 127;
		String integerCacheHighPropValue =
			sun.misc.VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
		if (integerCacheHighPropValue != null) {
			try {
				int i = parseInt(integerCacheHighPropValue);
				i = Math.max(i， 127);
				// Maximum array size is Integer.MAX_VALUE
				h = Math.min(i， Integer.MAX_VALUE - (-low) -1);
			} catch( NumberFormatException nfe) {
				// If the property cannot be parsed into an int， ignore it.
			}
		}
		high = h;
		cache = new Integer[(high - low) + 1];
		int j = low;
		for(int k = 0； k < cache.length； k++)
			cache[k] = new Integer(j++);

		// range [-128， 127] must be interned (JLS7 5.1.7)
		assert IntegerCache.high >= 127;
	}
}
```
也就是说，当 Integer 被加载时，就新建了-128到127的所有数字并存放在 Integer 数组cache中

- 当调用valueOf方法（包括后面会提到的重载的参数类型包含 String 的valueOf方法）时，如果参数的值在-127到128之间，则直接从缓存中返回一个已经存在的对象。如果参数的值不在这个范围内，则new一个Integer对象返回，建议使用valueOf方法来代替构造函数；
```java
public static Integer valueOf(String s) throws NumberFormatException {
		return Integer.valueOf(parseInt(s， 10));
}
public static Integer valueOf(String s， int radix) throws NumberFormatException {
	return Integer.valueOf(parseInt(s，radix));
}
```
- 返回一个 Integer 对象。如果指定第二个参数radix，将第一个参数解释为用第二个参数指定的基数表示的有符号整数。如果没指定则按照十进制进行处理

### 2.3、String 转成 Integer（int）的方法

```java
Integer getInteger(String nm)
Integer getInteger(String nm, int val)
Integer getInteger(String nm, Integer val)
Integer decode(String nm)	
int parseUnsignedInt(String s)
int parseUnsignedInt(String s, int radix)
int parseInt(String s)
int parseInt(String s, int radix)
```
- 以上所有方法都能实现将 String 类型的值转成 Integer(int)类型，如果 String 不包含可解析整数将抛出 NumberFormatException；
- 可以说，所有将 String 转成 Integer 的方法都是基于parseInt方法实现的。简单看一下以上部分方法的调用栈。<br>
	getInteger(String nm) ---> getInteger(nm， null)；--->Integer.decode()--->Integer.valueOf()--->parseInt()
	
#### 2.3.1、getInteger(String nm)：确定具有指定名称的系统属性的整数值

- 第一个参数被视为系统属性的名称，通过 System.getProperty(java.lang.String) 方法可以访问系统属性然后，将该属性的字符串值解释为一个整数值，并返回表示该值的 Integer 对象：代码中可以用以下形式使用该方法：
```java
Properties props = System.getProperties();
props.put("hollis.integer.test.key"，"10000");
Integer i = Integer.getInteger("hollis.integer.test.key");
System.out.println(i);
```
另外：
```java
getInteger(String nm，int val)
getInteger(String nm， Integer val)
```
- 第二个参数是默认值.如果未具有指定名称的属性，或者属性的数字格式不正确，或者指定名称为空或 null，则返回默认值。
- 具体实现细节：public static Integer getInteger(String nm， Integer val) 先按照nm作为key从系统配置中取出值，然后调用 Integer.decode方法将其转换成整数并返回

#### 2.3.2、decode

`public static Integer decode(String nm) throws NumberFormatException`

将 String 解码为 Integer，接受十进制、十六进制和八进制数字
- 根据要解码的 String（mn)的形式转成不同进制的数字。 mn由三部分组成：符号、基数说明符和字符序列。—0X123中-是符号位，0X是基数说明符（0表示八进制，0x，0X，#表示十六进制，什么都不写则表示十进制），123是数字字符序列；
- decode方法的具体实现也比较简单，首先就是判断 String 类型的参数mn是否以(+/—)符号开头。然后再依次判断是否以”0x”、“#”、“0”开头，确定基数说明符的值。然后将字符串mn进行截取，只保留其中纯数字部分。在用截取后的纯数字和基数调用valueOf(String s， int radix)方法并返回其值；

#### 2.3.3、parseInt

public static int parseInt(String s) throws NumberFormatException

- 使用第二个参数指定的基数(如果没指定，则按照十进制处理），将字符串参数解析为有符号的整数
- 如果发生以下任意一种情况，则抛出一个 NumberFormatException 类型的异常;第一个参数为 null 或一个长度为零的字符串。基数小于 Character.MIN_RADIX 或者大于 Character.MAX_RADIX。假如字符串的长度超过 1，那么除了第一个字符可以是减号 ‘-‘ (‘u002D’) 外，字符串中存在任意不是由指定基数的数字表示的字符。字符串表示的值不是 int 类型的值。

#### 2.3.4、将 String 转成 Integer 的方法之间有哪些区别

- parseInt方法返回的是基本类型 int，其他的方法返回的是 Integer，valueOf（String）方法会调用valueOf(int)方法。
- 如果只需要返回一个基本类型，而不需要一个对象，可以直接使用Integert.parseInt("123")；如果需要一个对象，那么建议使用valueOf()，因为该方法可以借助缓存带来的好处。如果和进制有关，那么就是用decode方法。如果是从系统配置中取值，那么就是用getInteger

### 2.4、int 转为 String 的方法：

```java
String  toString()
static String   toString(int i)
static String   toString(int i， int radix)
static String   toBinaryString(int i)
static String   toHexString(int i)
static String   toOctalString(int i)
static String   toUnsignedString(int i)
static String   toUnsignedString(int i， int radix)
```

#### 2.4.1、toString(int i)

- 4.1.1.实现代码：
	```java
	public static String toString(int i) {
		// 片段 1
		if (i == Integer.MIN_VALUE)
			return "-2147483648";
		// 片段 2
		int size = (i < 0) ? stringSize(-i) + 1 ： stringSize(i);
		char[] buf = new char[size];
		// 片段 3
		getChars(i， size， buf);
		// 片段 4
		return new String(buf， true);
	}
	```
	- 片段 1：这里先对i的值做检验，如果等于Int能表示的最小值，则直接返回最小值的字符串形式那么为什么-2147483648要特殊处理呢？ 看片段2
	- 片段 2：这段代码的主要目的是体取出整数i的位数，并创建一个字符数组，提取I的位数使用stringSize()方法，该方法实现如下：
		```java
		final static int [] sizeTable = {9，99，999，9999，99999，999999，9999999，99999999，999999999，Integer.MAX_VALUE};
		// Requires positive x， 该方法要求传入一个正整数
		static int stringSize(int x) {
			for (int i=0;; i++)
				if (x <= sizeTable[i])
					return i+1;
		}
		```
		设置size时，当i<0的时候返回的size数组在stringSize方法的基础上+1的目的是这一位用来存储负号

		==>代码片段一中，将 -2147483648 的值直接返回的原因就是整数最大只能表示 2147483647，无法将stringSize(-i)中的i赋值成-2147483648，局部性原理之空间局部性：sizeTable为数组，存储在相邻的位置，cpu一次加载一个块数据数据到cache中(多个数组数据)，此后访问sizeTable 不需要访问内存
	- 片段三：getChars(i， size， buf)；getChars的实现：
		```java
		static void getChars(int i, int index, char[] buf) {
			int q, r;
			int charPos = index;
			char sign = 0;
			if (i < 0) {
				sign = '-';
				i = -i;
			}
				// 每次循环过后，都会将i中的走后两位保存到字符数组buf中的最后两位中，
				// 将数字i设置为12345678测试一下， 
				// 第一次循环结束之后，buf[7] = 8，buf[6]=7。第二次循环结束之后，buf[5] = 6，buf[4] = 5。
			while (i >= 65536) {
				q = i / 100;
			// really： r = i - (q * 100);
				r = i - ((q << 6) + (q << 5) + (q << 2));
				i = q;
				//取DigitOnes[r]的目的其实取数字r%10的结果
				buf [--charPos] = DigitOnes[r];
				//取DigitTens[r]的目的其实是取数字r/10的结果
				buf [--charPos] = DigitTens[r];
			}
			// Fall thru to fast mode for smaller numbers
			// assert(i <= 65536， i);
			//循环将其他数字存入字符数组中空余位置
			for (;;) {
					//这里其实就是除以10。取数52429和16+3的原因在后文分析。
				q = (i * 52429) >>> (16+3);
				// r = i-(q*10) ...
				r = i - ((q << 3) + (q << 1));   
				//将数字i的最后一位存入字符数组，
				//还是12345678那个例子，这个for循环第一次结束后，buf[3]=4。
				buf [--charPos] = digits [r];
				i = q;
				//for循环结束后，buf内容为“12345678”;
				if (i == 0) break;
			}
			if (sign != 0) {
				buf [--charPos] = sign;
			}
		}
		```
		其中用到的几个数组：
		```java
		//100以内的数字除以10的结果（取整），
		//比如取DigitTens[78]，返回的是数字7
		//只要是70-79的数字，返回的都是7，依次类推，所以总结出规律，其实就是返回的对应数字除10取整的结果。
		final static char [] DigitTens = {}；
		//100以内的数字对10取模的结果，
		//比如取DigitTens[78]，返回的8
		final static char [] DigitOnes = {}
		```
		- 两个问题：
			- ①.为什么在getChars方法中，将整型数字写入到字符数组的过程中为什么按照数字65536分成了两部分呢?这个65535是怎么来的?
			- ②.在上面两段代码的部分二中，在对i进行除十操作的过程中为什么选择先乘以52429在向右移位19位.其中52429和19是怎么来的?
		- 明确两点：
			- ①.移位的效率比直接乘除的效率要高乘法的效率比除法的效率要高
			- ②.r = i - ((q << 6) + (q << 5) + (q << 2))； 等价于：r = i - (q * 100)；，i-q*2^6 - q*2^5 - q*2^2 = i-64q-32q-4q = i-100q。
			- ③.q = (i * num2) >>> (num3)；中，>>>表示无符号向右移位。代表的意义就是除以2^num3，q = (i * 52429) >>> (16+3)；可以理解为：q = (i * 52429) / 524288；，那么就相当于 q= i * 0.1也就是q=i/10，这样通过乘法和向右以为的组合的形式代替了除法，能提高效率；
		- while 循环和 for 循环中最大的区别就是 while 部分代码使用了除法，for 部分只使用了乘法和移位
			- ①.那么为什么不都使用乘法加移位的形式呢？为什么大于num1(65536)的数字要使用除法呢？原因是int型变量最大不能超过(2^31-1)。如果使用一个太大的数字进行乘法加移位运算很容易导致溢出
			- ②.那么为什么是65536这个数字呢？第二阶段用到的乘法的数字和移位的位数又是怎么来的呢？那么是怎么敲定num1=65536，num2= 524288， num3=19的呢？ 这三个数字之间是有这样一个操作的：(num1* num2)>>> num3，num1和num2就相互约束。两个数的乘积是有一定范围的，不成超过这个范围，所以，num1增大，num2就要随之减小
			- ③.那么52429这个数是怎么来的呢？<br>
				52429/524288=0.10000038146972656精度足够高。<br>
				下一个精度较高的num2和num3的组合是419431和22。2^31/2^22 = 2^9 = 512。512这个数字实在是太小了。<br>
				65536正好是2^16，一个整数占4个字节。65536正好占了2个字节，选定这样一个数字有利于CPU访问数据。<br>
				65536* 52429是超过了int的最大值的 ，与>>>有关，因为>>>表示无符号右移，他会在忽略符号位，空位都以0补齐
		- 其他
			- ①.乘法比除法高效：q = ( i * 52429) >>> (16+3)； => 约等于q = i* 0.1，但是整数乘法器，结合位移避免除法。
			- ②.重复利用计算结果：在获取r(i%100)时，充分利用了除法的结果，结合位移避免重复计算。
			- ③.位移比乘法高效：r = i – (( q << 6) + ( q << 5) + ( q << 2))； = >等价于r = i – (q * 100)；
			- ④.局部性原理之空间局部性 buf[–charPos] =DigitOnes[r]； buf[–charPos] =DigitTens[r]；通过查找数组，实现快速访问，避免除法计算buf [–charPos ] = digits [ r]；

- 4.1.2.一般在要使用String的，可以使用如下形式：
	```java
	Integer s = new Integer(199);
	System.out.println(s + "");
	// 反编译如下：
	Integer s = new Integer(199);
	System.out.println((new StringBuilder()).append(s).append("").toString());
	```
	
### 2.5、public int compareTo(Integer anotherInteger)

Integer 类实现了 Comparable<Integer>接口，所以 Integer 对象可以和另外一个 Integer 对象进行比较代码实现比较简单，就是拿出其中的 int 类型的value进行比较。

### 2.6、实现 Number 的方法

## 4、Integer 缓存机制

### 4.1、看代码

```java
public class JavaIntegerCache {
	public static void main(String... strings) {
		Integer integer1 = 3；
		Integer integer2 = 3；
		if (integer1 == integer2)
			System.out.println("integer1 == integer2")；
		else
			System.out.println("integer1 != integer2")；
		Integer integer3 = 300；
		Integer integer4 = 300；
		if (integer3 == integer4)
			System.out.println("integer3 == integer4")；
		else
			System.out.println("integer3 != integer4")；
	}
}
```
输出：
```java
integer1 == integer2
integer3 != integer4
```
### 4.2、Java 中 Integer 的缓存实现

JDK5 之后，在Integer的操作上， 整型对象通过使用相同的对象引用实现了缓存和重用
- 适用于整数值区间-128 至 +127。
- 只适用于自动装箱(Java的编译器把基本数据类型自动转换成封装类对象的过程叫做)， 使用构造函数创建对象不适用。

但是在特别的应用场景下，比如明确知道应用会频繁的使用更大的值，缓存的上限是可以根据需要调整的，JVM提供了参数设置 “-XX：AutoBoxCacheMax=N”，在Integer的源码可以看到体现；

### 4.3、valueOf的实现

```java
public static Integer valueOf(int i) {
	if (i >= IntegerCache.low && i <= IntegerCache.high)
		return IntegerCache.cache[i + (-IntegerCache.low)]；
	return new Integer(i)；
}
```
在创建对象之前先从 IntegerCacheCcache 中寻找。如果没找到才使用 new {}新建对象

### 4.4、IntegerCache

是 Integer 类中定义的一个 private static 的内部类；缓存支持 -128 到 127 之间的自动装箱过程。最大值 127 可以通过 `-XX:AutoBoxCacheMax=size` 修改，缓存通过一个 for 循环实现。从低到高并创建尽可能多的整数并存储在一个整数数组中。这个缓存会在 Integer 类第一次被使用的时候被初始化出来。就可以使用缓存中包含的实例对象，而不是创建一个新的实例(在自动装箱的情况下)

```java
private static class IntegerCache {
	static final int low = -128；
	static final int high；
	static final Integer cache[]；
	static {
		// high value may be configured by property
		int h = 127；
		String integerCacheHighPropValue =
			sun.misc.VM.getSavedProperty("java.lang.Integer.IntegerCache.high")；
		if (integerCacheHighPropValue != null) {
			try {
				int i = parseInt(integerCacheHighPropValue)；
				i = Math.max(i， 127)；
				// Maximum array size is Integer.MAX_VALUE
				h = Math.min(i， Integer.MAX_VALUE - (-low) -1)；
			} catch( NumberFormatException nfe) {
				// If the property cannot be parsed into an int， ignore it.
			}
		}
		high = h；

		cache = new Integer[(high - low) + 1]；
		int j = low；
		for(int k = 0； k < cache.length； k++)
			cache[k] = new Integer(j++)；

		// range [-128， 127] must be interned (JLS7 5.1.7)
		assert IntegerCache.high >= 127；
	}

	private IntegerCache() {}
}
```

### 4.5、其他缓存的对象

有 ByteCache 用于缓存 Byte 对象<br>
有 ShortCache 用于缓存 Short 对象<br>
有 LongCache 用于缓存 Long 对象<br>
有 CharacterCache 用于缓存 Character 对象<br>
Byte， Short， Long 有固定范围： -128 到 127。对于 Character， 范围是 0 到 127。除了 Integer 以外，这个范围都不能改变

# 五、Long

# 六、Math

# 七、数值相关

## 1、小数处理

### 1.1、目前 Java 支持7中舍入法

RoundingMode：
- ROUND_UP：远离零方向舍入。向绝对值最大的方向舍入，只要舍弃位非0即进位；需要注意的是，此舍入模式始终不会减少原始值。
- ROUND_DOWN：接近零的舍入模式，在丢弃某部分之前始终不增加数字（从不对舍弃部分前面的数字加 1，即截断）。 需要注意的是，此舍入模式始终不会增加原始值；
- ROUND_CEILING：向正无穷方向舍入。向正最大方向靠拢。若是正数，舍入行为类似于 ROUND_UP，若为负数，舍入行为类似于 ROUND_DOWN。 Math.round() 方法就是使用的此模式；需要注意的是，此舍入模式始终不会减少原始值。
- ROUND_FLOOR：向负无穷方向舍入。向负无穷方向靠拢。若是正数，舍入行为类似于 ROUND_DOWN；若为负数，舍入行为类似于 ROUND_UP。
- HALF_UP：最近数字舍入(5进)。这是我们最经典的四舍五入。
- HALF_DOWN：向“最接近的”数字舍入。如果舍弃部分 > 0.5，则舍入行为与 ROUND_UP 相同；否则，舍入行为与 ROUND_DOWN 相同（五舍六入）。
- HAIL_EVEN：向“最接近的”数字舍入。这种算法叫做银行家算法，具体规则是，四舍六入，五则看前一位，如果是偶数舍入，如果是奇数进位，比如 5.5 -> 6，2.5 -> 2；
- ROUND_UNNECESSARY：假设请求的操作具有精确的结果，也就是不需要进行舍入。如果计算结果产生不精确的结果，则抛出 ArithmeticException。

详细：

<table class="striped">
  <caption><b>Summary of Rounding Operations Under Different Rounding Modes</b></caption>
  <thead>
  <tr><th scope="col" rowspan="2">Input Number</th><th scope="col"colspan=8>Result of rounding input to one digit with the given rounding mode</th>
  <tr style="vertical-align:top">
    <th>{@code UP}</th>
    <th>{@code DOWN}</th>
    <th>{@code CEILING}</th>
    <th>{@code FLOOR}</th>
    <th>{@code HALF_UP}</th>
    <th>{@code HALF_DOWN}</th>
    <th>{@code HALF_EVEN}</th>
    <th>{@code UNNECESSARY}</th>
  </thead>
  <tbody style="text-align:right">
  <tr><th scope="row">5.5</th>  <td>6</td>  <td>5</td>    <td>6</td>    <td>5</td>  <td>6</td>      <td>5</td>       <td>6</td>       <td>throw {@code ArithmeticException}</td>
  <tr><th scope="row">2.5</th>  <td>3</td>  <td>2</td>    <td>3</td>    <td>2</td>  <td>3</td>      <td>2</td>       <td>2</td>       <td>throw {@code ArithmeticException}</td>
  <tr><th scope="row">1.6</th>  <td>2</td>  <td>1</td>    <td>2</td>    <td>1</td>  <td>2</td>      <td>2</td>       <td>2</td>       <td>throw {@code ArithmeticException}</td>
  <tr><th scope="row">1.1</th>  <td>2</td>  <td>1</td>    <td>2</td>    <td>1</td>  <td>1</td>      <td>1</td>       <td>1</td>       <td>throw {@code ArithmeticException}</td>
  <tr><th scope="row">1.0</th>  <td>1</td>  <td>1</td>    <td>1</td>    <td>1</td>  <td>1</td>      <td>1</td>       <td>1</td>       <td>1</td>
  <tr><th scope="row">-1.0</th> <td>-1</td> <td>-1</td>   <td>-1</td>   <td>-1</td> <td>-1</td>     <td>-1</td>      <td>-1</td>      <td>-1</td>
  <tr><th scope="row">-1.1</th> <td>-2</td> <td>-1</td>   <td>-1</td>   <td>-2</td> <td>-1</td>     <td>-1</td>      <td>-1</td>      <td>throw {@code ArithmeticException}</td>
  <tr><th scope="row">-1.6</th> <td>-2</td> <td>-1</td>   <td>-1</td>   <td>-2</td> <td>-2</td>     <td>-2</td>      <td>-2</td>      <td>throw {@code ArithmeticException}</td>
  <tr><th scope="row">-2.5</th> <td>-3</td> <td>-2</td>   <td>-2</td>   <td>-3</td> <td>-3</td>     <td>-2</td>      <td>-2</td>      <td>throw {@code ArithmeticException}</td>
  <tr><th scope="row">-5.5</th> <td>-6</td> <td>-5</td>   <td>-5</td>   <td>-6</td> <td>-6</td>     <td>-5</td>      <td>-6</td>      <td>throw {@code ArithmeticException}</td>
  </tbody>
  </table>

### 1.2、保留位

- 四舍五入：

```
double   f   =   111231.5585;
BigDecimal   b   =   new   BigDecimal(f);
double   f1   =   b.setScale(2，   RoundingMode.HALF_UP).doubleValue();
```

- 格式化：

```
java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00″);
df.format(你要格式化的数字);
```

- 类C语言：

```
double d = 3.1415926;
String result = String .format("%.2f");
%.2f %. 表示 小数点前任意位数   2 表示两位小数 格式后的结果为f 表示浮点型
```
- 此外如果使用 struts 标签做输出的话， 有个 format 属性，设置为 format="0.00″就是保留两位小数
	`<bean：write name="entity" property="dkhAFSumPl"  format="0.00" />`
	或者
	`<fmt：formatNumber type="number" value="${10000.22/100}" maxFractionDigits="0"/>`
	maxFractionDigits表示保留的位数

### 1.3、Math相关函数

```java
double d1 = -0.5;
System.out.println("Ceil d1=" + Math.ceil(d1)); // -0.0
System.out.println("floor d1=" + Math.floor(d1)); // -1.0
System.out.println("floor d1=" + Math.round(d1)); // 0
System.out.println(Math.ceil(-0)); // 0.0
System.out.println(Math.ceil(-0.0)); // -0.0
System.out.println(Math.floor(-0)); // 0.0
System.out.println(Math.floor(-0.0)); // -0.0
```
- ceil()：该方法返回的是一个 double 类型数据；返回一个大于该参数的最小 double 值，等于某个整数，特殊情况：
	- ①.如果参数小于0且大于-1.0，则结果为-0.0；
	- ②.如果参数数学上等于某个整数，则结果与该参数相同；如：5.0；
	- ③.如果参数为 NaN，无穷大，正0.0或负0.0，那么结果与参数相同；如果是-0，则结果是0.0

	==> 特别注意：`Math.ceil(d1) == -Math.floor(-d1)；// true`

- floor()：返回 double 类型数据，返回一个小于该参数的最大 double 值，等于某个整数
	- ①.如果参数数学上等于某个整数，则结果与该参数相同；如：5.0；
	- ②.如果参数为 NaN，无穷大，正0.0或负0.0，那么结果与参数相同；如果是-0，则结果是0.0

- round()：返回一个整数，如果参数为 float，返回 int 类型；如果参数为 double，返回 long 类型
	`(int)Math.floor(a + 0.5f);`、`(long)Math.floor(a + 0.5d);`

	返回最接近参数的 int 或 long 类型数据，将参数加上 1/2， 对结果调用 floor 将所得结果强转为 int 或 long
	- ①.如果参数为 NaN， 结果为 0
	- ②.如果结果为负无穷大或任何小于等于 `Integer.MIN_VALUE` 或 `Long.MIN_VALUE` 的值，那么结果等于 `Integer.MIN_VALUE` 或 `Long.MIN_VALUE` 的值。
	- ③.如果参数为正无穷大或任何大于等于 `Integer.MAX_VALUE` 或 `Long.MAX_VALUE` 的值，那么结果等于 `Integer.MAX_VALUE` 或 `Long.MAX_VALUE` 的值

### 1.4、使用 BigDecimal，保留小数点后两位

```java
public static String format1(double value) {
	BigDecimal bd = new BigDecimal(value);
	bd = bd.setScale(2， RoundingMode.HALF_UP);
	return bd.toString();
}
```

### 1.5、使用 DecimalFormat，保留小数点后两位

```java
	public static String format2(double value) {
	    DecimalFormat df = new DecimalFormat("0.00");
	    df.setRoundingMode(RoundingMode.HALF_UP);
	    return df.format(value);
	}
```

### 1.6、使用 NumberFormat，保留小数点后两位

```java
public static String format3(double value) {
	NumberFormat nf = NumberFormat.getNumberInstance();
	nf.setMaximumFractionDigits(2);
	// setMinimumFractionDigits设置成2，如果不这么做，那么当value的值是100.00的时候返回100，而不是100.00
	nf.setMinimumFractionDigits(2);
	nf.setRoundingMode(RoundingMode.HALF_UP);
	// 如果想输出的格式用逗号隔开，可以设置成true
	nf.setGroupingUsed(false);
	return nf.format(value);
}
```

### 1.7、使用 java.util.Formatter，保留小数点后两位

```java
public static String format4(double value) {
	// %.2f % 表示 小数点前任意位数 2 表示两位小数 格式后的结果为 f 表示浮点型
	return new Formatter().format("%.2f"， value).toString();
}
```

### 1.8、使用 String.format来实现

String.format 采用四舍五入的方式进行舍入
```java
public static String format5(double value) {
	return String.format("%.2f"， value).toString();
}
```

**5.1、对浮点数进行格式化：占位符格式为： `%[index$][标识] * [最小宽度][.精度]`转换符**

```java
double num = 123.4567899;
System.out.print(String.format("%f %n"， num)); // 123.456790
System.out.print(String.format("%a %n"， num)); // 0x1.edd3c0bb46929p6
System.out.print(String.format("%g %n"， num)); // 123.457
```
- 可用标识符
	- `-`，在最小宽度内左对齐，不可以与0标识一起使用。
	- `0`，若内容长度不足最小宽度，则在左边用0来填充。
	- `#`，对8进制和16进制，8进制前添加一个0，16进制前添加0x。
	- `+`，结果总包含一个+或-号。
	- `空格`，正数前加空格，负数前加-号。
	- `,`，只用与十进制，每3位数字间用，分隔。
	- `(`，若结果为负数，则用括号括住，且不显示符号。
- 可用转换符：
	- `b`，布尔类型，只要实参为非false的布尔类型，均格式化为字符串true，否则为字符串false。
	- `n`，平台独立的换行符， 也可通过System.getProperty("line.separator")获取。
	- `f`，浮点数型(十进制)。显示9位有效数字，且会进行四舍五入。如99.99。
	- `a`，浮点数型(十六进制)。
	- `e`，指数类型。如9.38e+5。
	- `g`，浮点数型(比%f，%a长度短些，显示6位有效数字，且会进行四舍五入)

### 1.9、最佳实践

- 使用 BigDecimal 表示和计算浮点数，且务必使用字符串的构造方法来初始化 BigDecimal：
- 通过 DecimalFormat 来精确控制舍入方式，double 和 float 的问题也可能产生意想不到的结果，所以浮点数避坑第二原则：**浮点数的字符串格式化也要通过 BigDecimal 进行**

## 2、数值溢出

数值计算有一个要小心的点是溢出，不管是 int 还是 long，所有的基本数值类型都有超出表达范围的可能性；

对Long的最大值进行+1操作：
```java
// 输出结果是一个负数，因为 Long 的最大值 +1 变为了 Long 的最小值：
long l = Long.MAX_VALUE;
System.out.println(l + 1);
System.out.println(l + 1 == Long.MIN_VALUE);
```
有人如下两种方式解决：
- 考虑使用 Math 类的 addExact、subtractExact 等 xxExact 方法进行数值运算，这些方法可以在数值溢出时主动抛出异常:
	```java
	try {
		long l = Long.MAX_VALUE;
		System.out.println(Math.addExact(l, 1));
	} catch (Exception ex) {
		ex.printStackTrace();
	}
	// 执行后可以得到 ArithmeticException，这是一个 RuntimeException：
	java.lang.ArithmeticException: long overflow
	at java.base/java.lang.Math.addExact(Math.java:845)
	at com.blue.fish.example.base.type.number.TestNumOverLimit.main(TestNumOverLimit.java:13)
	```
- 使用大数类 BigInteger。BigDecimal 是处理浮点数的专家，而 BigInteger 则是对大数进行科学计算的专家；使用 BigInteger 对 Long 最大值进行 +1 操作；如果希望把计算结果转换一个 Long 变量的话，可以使用 BigInteger 的 longValueExact 方法，在转换出现溢出时，同样会抛出 ArithmeticException：
	```java
	BigInteger i = new BigInteger(String.valueOf(Long.MAX_VALUE));
	System.out.println(i.add(BigInteger.ONE).toString());

	try {
		long l = i.add(BigInteger.ONE).longValueExact();
	} catch (Exception ex) {
		ex.printStackTrace();
	}
	// 输出
	java.lang.ArithmeticException: BigInteger out of long range
	9223372036854775808
		at java.base/java.math.BigInteger.longValueExact(BigInteger.java:4765)
		at com.blue.fish.example.base.type.number.TestNumOverLimit.main(TestNumOverLimit.java:25)
	```
	注意：不要调用BigInteger的longValue()方法，该方法溢出不报错，它会变成负数，比如通过 BigInteger 调用 Long.MAX_VALUE + 1的操作，然后调用 longValue方法，其值会变成 Long.MIN_VALUE

# 八、面试

## 1、int范围问题

- 看如下代码，会不会产生问题？
	```java
	// a、b、c 都是int类型数据
	if(a + b < c){
		...
	}
	```
- 这里需要注意int类型溢出，不然会带来逻辑上的错误，上述代码可以改为如下
	```java
	if(a < c - b) {
		...
	}
	```
- 检测int类型相加是否溢出可以使用如下方法：
	```java
	int x = a + b;
	boolean isOverFlow = (c ^ a) < 0 && (c ^ b) < 0
	// 下列判断存在问题，f2判断时如果两个int数据a、b都为 Integer.MIN_VALUE， a + b = 0
	boolean f1 = a > 0 && b > 0 && x < 0;
	boolean f2 = a < 0 && b < 0 && x > 0;
	```

整数相加范围问题：Math 类中实现了相关的代码：
```java
// 加法
public static int addExact(int x, int y) {
	int r = x + y;
	// HD 2-12 Overflow iff both arguments have the opposite sign of the result
	if (((x ^ r) & (y ^ r)) < 0) {
		throw new ArithmeticException("integer overflow");
	}
	return r;
}
// 减法
public static int subtractExact(int x, int y) {
	int r = x - y;
	// HD 2-12 Overflow iff the arguments have different signs and
	// the sign of the result is different than the sign of x
	if (((x ^ y) & (x ^ r)) < 0) {
		throw new ArithmeticException("integer overflow");
	}
	return r;
}
// 乘法
public static int multiplyExact(int x, int y) {
	long r = (long)x * (long)y;
	if ((int)r != r) {
		throw new ArithmeticException("integer overflow");
	}
	return (int)r;
}
```

## 2、int整数转换为完整二进制表示

```java
public class IntegerToFullBinary{
	/**
	 * 将一个int整数转换为完整二进制表示
	 * 如 2=> 00000000 00000000 00000000 00000010
	 * @param  num [description]
	 * @return     [description]
	 */
	public static String toFullBinaryString(int num) {
        char[] chs = new char[Integer.SIZE];
        for (int i = 0; i < Integer.SIZE; i++) {
            chs[Integer.SIZE - 1 - i] = (char) (((num >> i) & 1) + '0');
        }
        return new String(chs);
    }
}
```

## 3、关于除以0的问题

`System.out.println(2 / 0)`  -> `ArithmeticException: / by zero`

`System.out.println(2.0 / 0);` -> `Infinity`
```
double i = 1.0 / 0;                
System.out.println(i);             //Infinity
System.out.println(i + 1);         //Infinity
System.out.println(i == i + 1);    //true
 
i = 0.0 / 0;
System.out.println(i);             //NaN
System.out.println(i + 1);         //NaN
System.out.println(i == i + 1);    //false
```
在Double和Float中都定义了`正无穷`和`负无穷`这两个概念：
```java
public final class Float extends Number implements Comparable<Float> {
    /**
     * A constant holding the positive infinity of type {@code float}. It is equal to the value returned by {@code Float.intBitsToFloat(0x7f800000)}.
     */
    public static final float POSITIVE_INFINITY = 1.0f / 0.0f;
    /**
     * A constant holding the negative infinity of type {@code float}. It is equal to the value returned by {@code Float.intBitsToFloat(0xff800000)}.
     */
    public static final float NEGATIVE_INFINITY = -1.0f / 0.0f;
    /**
     * A constant holding a Not-a-Number (NaN) value of type {@code float}.  It is equivalent to the value returned by {@code Float.intBitsToFloat(0x7fc00000)}.
     */
    public static final float NaN = 0.0f / 0.0f;
}
// Double
public final class Double extends Number implements Comparable<Double> {
    /**
     * A constant holding the positive infinity of type {@code double}. It is equal to the value returned by {@code Double.longBitsToDouble(0x7ff0000000000000L)}.
     */
    public static final double POSITIVE_INFINITY = 1.0 / 0.0;
    /**
     * A constant holding the negative infinity of type {@code double}. It is equal to the value returned by {@code Double.longBitsToDouble(0xfff0000000000000L)}.
     */
    public static final double NEGATIVE_INFINITY = -1.0 / 0.0;
    /**
     * A constant holding a Not-a-Number (NaN) value of type  {@code double}. It is equivalent to the value returned by {@code Double.longBitsToDouble(0x7ff8000000000000L)}.
     */
    public static final double NaN = 0.0d / 0.0;
}
```

总结：在 Java 里面，除数作为 0，不一定会抛出 ArithmeticException
- 整数相除，除数为0，会直接编译报错；
- 浮点数，会返回正负无穷大（小）

> 你要问我为什么，我只能告诉你我遵守的是 IEEE 754 这个国际规范
