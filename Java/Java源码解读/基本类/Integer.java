Integer 类在对象中包装了一个基本类型 int 的值。Integer 类型的对象包含一个 int 类型的字段
Integer 源码:
一.类定义:
public final class Integer extends Number implements Comparable<Integer>{}
1.Integer 类不能被继承
2.Integer 类实现了 Comparable 接口,所以可以用compareTo进行比较并且 Integer 对象只能和 Integer 类型的对象进行比较,
	不能和其他类型比较(至少调用compareTo方法无法比较).
3.Integer 继承了 Number 类，所以该类可以调用longValue、floatValue、doubleValue等系列方法返回对应的类型的值

二.属性:
1.私有属性:
	1.1.private final int value; Integer 对象中真正保存 int 值的
		我们使用 new Integer(10)创建一个 Integer 对象的时候，就会用以下形式给value赋值
		(1).关于 Integer 对象的"可变性":
			从value的定义形式中可以看出value被定义成final类型.也就说明,一旦一个Integer对象被初始化之后,就无法再改变value的值
			看如下代码:
				Integer i = new Integer(10);
				i = 5;
			查看反编译的代码:
				Integer i = new Integer(10);
		        i = Integer.valueOf(5);
		    ==> i=5操作并没有改变使用 Integer i = new Integer(10);创建出来的i中的value属性的值.
		    	要么是直接返回一个已有对象,要么新建一个对象;这里跟 valueOf 的实现细节相关
2.公共属性:
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
	以上属性可直接使用,因为他们已经定义成 publis static final 能用的时候尽量使用他们,这样不仅能使代码有很好的可读性,
	也能提高性能节省资源

三.方法:
1.Integer 提供了两个构造方法：
	//构造一个新分配的 Integer 对象，它表示指定的 int 值。
	public Integer(int value) {
	    this.value = value;
	}
	//构造一个新分配的 Integer 对象，它表示 String 参数所指示的 int 值。
	public Integer(String s) throws NumberFormatException {
	    this.value = parseInt(s, 10);
	}
	==> 从构造方法中我们可以知道，初始化一个 Integer 对象的时候只能创建一个十进制的整数
2.valueOf: public static Integer valueOf(int i);
	(1).通常情况下: IntegerCache.low=-128，IntegerCache.high=127(除非显示声明 java.lang.Integer.IntegerCache.high 的值)，
	Integer 中有一段动态代码块，该部分内容会在 Integer 类被加载的时候就执行:
	// 虚拟机初始化时,该段代码就加载,通过 VM 参数: -XX:AutoBoxCacheMax=<size> 控制其初始化
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
	                i = Math.max(i, 127);
	                // Maximum array size is Integer.MAX_VALUE
	                h = Math.min(i, Integer.MAX_VALUE - (-low) -1);
	            } catch( NumberFormatException nfe) {
	                // If the property cannot be parsed into an int, ignore it.
	            }
	        }
	        high = h;
	        cache = new Integer[(high - low) + 1];
	        int j = low;
	        for(int k = 0; k < cache.length; k++)
	            cache[k] = new Integer(j++);

	        // range [-128, 127] must be interned (JLS7 5.1.7)
	        assert IntegerCache.high >= 127;
		}
	}
	也就是说，当 Integer 被加载时，就新建了-128到127的所有数字并存放在 Integer 数组cache中
	(2).当调用valueOf方法(包括后面会提到的重载的参数类型包含 String 的valueOf方法)时，如果参数的值在-127到128之间，
		则直接从缓存中返回一个已经存在的对象。如果参数的值不在这个范围内，则 new{}一个 Integer 对象返回
	==> 建议使用valueOf方法来代替构造函数
		public static Integer valueOf(String s) throws NumberFormatException {
		     return Integer.valueOf(parseInt(s, 10));
		}
		public static Integer valueOf(String s, int radix) throws NumberFormatException {
		    return Integer.valueOf(parseInt(s,radix));
		}
	(3).返回一个 Integer 对象。如果指定第二个参数radix，将第一个参数解释为用第二个参数指定的基数表示的有符号整数。
		如果没指定则按照十进制进行处理
3.String 转成 Integer（int）的方法
	Integer getInteger(String nm)
	Integer getInteger(String nm, int val)
	Integer getInteger(String nm, Integer val)
	Integer decode(String nm)	
	int parseUnsignedInt(String s)
	int parseUnsignedInt(String s, int radix)
	int parseInt(String s)
	int parseInt(String s, int radix)
	(1).以上所有方法都能实现将 String 类型的值转成 Integer(int)类型,如果 String 不包含可解析整数将抛出 NumberFormatException;
	(2).可以说，所有将 String 转成 Integer 的方法都是基于parseInt方法实现的。简单看一下以上部分方法的调用栈。
		getInteger(String nm) ---> getInteger(nm, null);--->Integer.decode()--->Integer.valueOf()--->parseInt()
	3.1.getInteger(String nm):确定具有指定名称的系统属性的整数值,
		(1).第一个参数被视为系统属性的名称,通过 System.getProperty(java.lang.String) 方法可以访问系统属性
		然后，将该属性的字符串值解释为一个整数值，并返回表示该值的 Integer 对象:
			代码中可以用以下形式使用该方法：
			Properties props = System.getProperties();
			props.put("hollis.integer.test.key","10000");
			Integer i = Integer.getInteger("hollis.integer.test.key");
			System.out.println(i);
		另外:
			getInteger(String nm,int val)
			getInteger(String nm, Integer val)
		(2).第二个参数是默认值.如果未具有指定名称的属性,或者属性的数字格式不正确,或者指定名称为空或 null,则返回默认值。
		(3).具体实现细节:
			public static Integer getInteger(String nm, Integer val) {}
			先按照nm作为key从系统配置中取出值，然后调用 Integer.decode方法将其转换成整数并返回

	3.2.public static Integer decode(String nm) throws NumberFormatException:
		将 String 解码为 Integer,接受十进制、十六进制和八进制数字
		(1).根据要解码的 String（mn)的形式转成不同进制的数字。 mn由三部分组成：符号、基数说明符和字符序列。
		 —0X123中-是符号位，0X是基数说明符（0表示八进制，0x,0X，#表示十六进制，什么都不写则表示十进制），
		 123是数字字符序列
		(2).decode方法的具体实现也比较简单，首先就是判断 String 类型的参数mn是否以(+/—)符号开头。然后再依次判断
		是否以”0x”、“#”、“0”开头，确定基数说明符的值。然后将字符串mn进行截取，只保留其中纯数字部分。在用截取后
		的纯数字和基数调用valueOf(String s, int radix)方法并返回其值

	3.3.public static int parseInt(String s, int radix)throws NumberFormatException
		public static int parseInt(String s) throws NumberFormatException
		(1).使用第二个参数指定的基数(如果没指定，则按照十进制处理），将字符串参数解析为有符号的整数
		(2).如果发生以下任意一种情况，则抛出一个 NumberFormatException 类型的异常
			第一个参数为 null 或一个长度为零的字符串。
			基数小于 Character.MIN_RADIX 或者大于 Character.MAX_RADIX。
			假如字符串的长度超过 1，那么除了第一个字符可以是减号 ‘-‘ (‘u002D’) 外，字符串中存在任意不是由指定基数
			的数字表示的字符.
			字符串表示的值不是 int 类型的值。

	3.4.将 String 转成 Integer 的方法之间有哪些区别
		(1).parseInt方法返回的是基本类型 int
			其他的方法返回的是 Integer
			valueOf（String）方法会调用valueOf(int)方法。
		(2).如果只需要返回一个基本类型，而不需要一个对象，可以直接使用Integert.parseInt("123");
			如果需要一个对象，那么建议使用valueOf(),因为该方法可以借助缓存带来的好处。
			如果和进制有关，那么就是用decode方法。
			如果是从系统配置中取值，那么就是用getInteger
4.int 转为 String 的方法:
	String  toString()
	static String   toString(int i)
	static String   toString(int i, int radix)
	static String   toBinaryString(int i)
	static String   toHexString(int i)
	static String   toOctalString(int i)
	static String   toUnsignedString(int i)
	static String   toUnsignedString(int i, int radix)
	4.1.toString(int i):
		4.1.1.实现代码:
			public static String toString(int i) {
				// 片段 1
		        if (i == Integer.MIN_VALUE)
		            return "-2147483648";
		        // 片段 2
		        int size = (i < 0) ? stringSize(-i) + 1 : stringSize(i);
		        char[] buf = new char[size];
		        // 片段 3
		        getChars(i, size, buf);
		        // 片段 4
		        return new String(buf, true);
		    }
			(1).片段 1:
				这里先对i的值做检验，如果等于Int能表示的最小值，则直接返回最小值的字符串形式
				那么为什么-2147483648要特殊处理呢? 看片段2
			(2).片段 2:
				这段代码的主要目的是体取出整数i的位数，并创建一个字符数组
				提取I的位数使用stringSize()方法,该方法实现如下:
				final static int [] sizeTable = {9,99,999,9999,99999,999999,9999999,99999999,999999999,Integer.MAX_VALUE};
				// Requires positive x, 该方法要求传入一个正整数
				static int stringSize(int x) {
				    for (int i=0; ; i++)
				        if (x <= sizeTable[i])
				            return i+1;
				}
				设置size时，当i<0的时候返回的size数组在stringSize方法的基础上+1的目的是这一位用来存储负号
				==>代码片段一中,将 -2147483648 的值直接返回的原因就是整数最大只能表示 2147483647,
					无法将stringSize(-i)中的i赋值成-2147483648
				局部性原理之空间局部性:sizeTable为数组，存储在相邻的位置，cpu一次加载一个块数据数据到cache中(多个数组数据)，
				此后访问sizeTable 不需要访问内存
			(3).片段三:getChars(i, size, buf);getChars的实现:
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
				     // 第一次循环结束之后，buf[7] = 8,buf[6]=7。第二次循环结束之后，buf[5] = 6,buf[4] = 5。
				    while (i >= 65536) {
				        q = i / 100;
				    // really: r = i - (q * 100);
				        r = i - ((q << 6) + (q << 5) + (q << 2));
				        i = q;
				        //取DigitOnes[r]的目的其实取数字r%10的结果
				        buf [--charPos] = DigitOnes[r];
				        //取DigitTens[r]的目的其实是取数字r/10的结果
				        buf [--charPos] = DigitTens[r];
				    }
				    // Fall thru to fast mode for smaller numbers
				    // assert(i <= 65536, i);
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
				        //for循环结束后，buf内容为“12345678”；
				        if (i == 0) break;
				    }
				    if (sign != 0) {
				        buf [--charPos] = sign;
				    }
				}
				其中用到的几个数组:
				//100以内的数字除以10的结果（取整），
				//比如取DigitTens[78]，返回的是数字7
				//只要是70-79的数字，返回的都是7，依次类推，所以总结出规律，其实就是返回的对应数字除10取整的结果。
				final static char [] DigitTens = {};
				//100以内的数字对10取模的结果，
				//比如取DigitTens[78]，返回的8
				final static char [] DigitOnes = {}
				(3.1).两个问题:
					①.为什么在getChars方法中,将整型数字写入到字符数组的过程中为什么按照数字65536分成了两部分呢?这个65535是怎么来的?
					②.在上面两段代码的部分二中,在对i进行除十操作的过程中为什么选择先乘以52429在向右移位19位.其中52429和19是怎么来的?
				(3.2).明确两点:
					①.移位的效率比直接乘除的效率要高
					乘法的效率比除法的效率要高
					②.r = i - ((q << 6) + (q << 5) + (q << 2)); 等价于:
						r = i - (q * 100);，i-q*2^6 - q*2^5 - q*2^2 = i-64q-32q-4q = i-100q。
					③.q = (i * num2) >>> (num3);中，>>>表示无符号向右移位。代表的意义就是除以2^num3
						q = (i * 52429) >>> (16+3);可以理解为：q = (i * 52429) / 524288;,那么就相当于 q= i * 0.1也就是q=i/10，
						这样通过乘法和向右以为的组合的形式代替了除法，能提高效率;
				(3.3).while 循环和 for 循环中最大的区别就是 while 部分代码使用了除法，for 部分只使用了乘法和移位
					①.那么为什么不都使用乘法加移位的形式呢？为什么大于num1(65536)的数字要使用除法呢？
					==>原因是int型变量最大不能超过(2^31-1)。如果使用一个太大的数字进行乘法加移位运算很容易导致溢出
					②.那么为什么是65536这个数字呢？第二阶段用到的乘法的数字和移位的位数又是怎么来的呢？						
						那么是怎么敲定num1=65536,num2= 524288, num3=19的呢？ 这三个数字之间是有这样一个操作的：
						(num1* num2)>>> num3
						num1和num2就相互约束。两个数的乘积是有一定范围的，不成超过这个范围，所以，num1增大，num2就要随之减小
					③.那么52429这个数是怎么来的呢?
						52429/524288=0.10000038146972656精度足够高。
						下一个精度较高的num2和num3的组合是419431和22。2^31/2^22 = 2^9 = 512。512这个数字实在是太小了。
						65536正好是2^16，一个整数占4个字节。65536正好占了2个字节，选定这样一个数字有利于CPU访问数据。
						65536* 52429是超过了int的最大值的 ,与>>>有关，因为>>>表示无符号右移，他会在忽略符号位，空位都以0补齐
				(3.4).
					①.乘法比除法高效：q = ( i * 52429) >>> (16+3); => 约等于q = i* 0.1,但是整数乘法器，结合位移避免除法。
					②.重复利用计算结果:在获取r(i%100)时，充分利用了除法的结果，结合位移避免重复计算。
					③.位移比乘法高效:r = i – (( q << 6) + ( q << 5) + ( q << 2)); = >等价于r = i – (q * 100);
					④.局部性原理之空间局部性
						buf[–charPos] =DigitOnes[r]; buf[–charPos] =DigitTens[r];通过查找数组，实现快速访问,避免除法计算
						buf [–charPos ] = digits [ r];
		4.1.2.一般在要使用String的,可以使用如下形式:
				Integer s = new Integer(199);
				System.out.println(s + "");
			反编译如下:
				Integer s = new Integer(199);
				System.out.println((new StringBuilder()).append(s).append("").toString());
5.public int compareTo(Integer anotherInteger);
	Integer 类实现了 Comparable<Integer>接口，所以 Integer 对象可以和另外一个 Integer 对象进行比较
	代码实现比较简单，就是拿出其中的 int 类型的value进行比较。
6.实现 Number 的方法

四.Integer 缓存机制:
1.看代码:
public class JavaIntegerCache {
    public static void main(String... strings) {
        Integer integer1 = 3;
        Integer integer2 = 3;
        if (integer1 == integer2)
            System.out.println("integer1 == integer2");
        else
            System.out.println("integer1 != integer2");
        Integer integer3 = 300;
        Integer integer4 = 300;
        if (integer3 == integer4)
            System.out.println("integer3 == integer4");
        else
            System.out.println("integer3 != integer4");
    }
}
输出:
	integer1 == integer2
	integer3 != integer4
2.Java 中 Integer 的缓存实现
	JDK5 之后,在Integer的操作上, 整型对象通过使用相同的对象引用实现了缓存和重用
	(1).适用于整数值区间-128 至 +127。
	(2).只适用于自动装箱(Java的编译器把基本数据类型自动转换成封装类对象的过程叫做), 使用构造函数创建对象不适用。
3.valueOf的实现:
	public static Integer valueOf(int i) {
        if (i >= IntegerCache.low && i <= IntegerCache.high)
            return IntegerCache.cache[i + (-IntegerCache.low)];
        return new Integer(i);
    }
    在创建对象之前先从 IntegerCacheCcache 中寻找。如果没找到才使用 new {}新建对象
4.IntegerCache:是 Integer 类中定义的一个 private static 的内部类:
	4.1.缓存支持 -128 到 127 之间的自动装箱过程。最大值 127 可以通过 -XX:AutoBoxCacheMax=size 修改,
		缓存通过一个 for 循环实现。从低到高并创建尽可能多的整数并存储在一个整数数组中。这个缓存会在 Integer 类第一次
		被使用的时候被初始化出来。就可以使用缓存中包含的实例对象，而不是创建一个新的实例(在自动装箱的情况下)
		private static class IntegerCache {
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
	                    i = Math.max(i, 127);
	                    // Maximum array size is Integer.MAX_VALUE
	                    h = Math.min(i, Integer.MAX_VALUE - (-low) -1);
	                } catch( NumberFormatException nfe) {
	                    // If the property cannot be parsed into an int, ignore it.
	                }
	            }
	            high = h;

	            cache = new Integer[(high - low) + 1];
	            int j = low;
	            for(int k = 0; k < cache.length; k++)
	                cache[k] = new Integer(j++);

	            // range [-128, 127] must be interned (JLS7 5.1.7)
	            assert IntegerCache.high >= 127;
	        }

	        private IntegerCache() {}
	    }

5.其他缓存的对象:
	有 ByteCache 用于缓存 Byte 对象
	有 ShortCache 用于缓存 Short 对象
	有 LongCache 用于缓存 Long 对象
	有 CharacterCache 用于缓存 Character 对象
Byte, Short, Long 有固定范围: -128 到 127。对于 Character, 范围是 0 到 127。除了 Integer 以外，这个范围都不能改变


















