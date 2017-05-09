1.表达式的数据类型:
	(1).所有的 byte,short,char 型的值将被提升为 int 型；
	(2).如果有一个操作数是 long 型，计算结果是 long 型；
	(3).如果有一个操作数是 float 型，计算结果是 float 型；
	(4).如果有一个操作数是 double 型，计算结果是 double 型；
	(5).final 修饰的变量是常量,如果运算时直接是已常量值进行计算,没有final修饰的变量相加后会被自动提升为int型
		byte b1=1,b2=2,b3,b6; 
		final byte b4=4,b5=6; 
		b6=b4+b5; // b4, b5是常量,则在计算时直接按原值计算,不提升为int型
		b3=(b1+b2); // 编译错误
		System.out.println(b3+b6);
2.给出一个表达式计算其可以按多少进制计算:
	(1).式子7*15=133成立，则用的是几进制?
		可以通过解方程来解决,上述式子可以转换为方程:
		7 * (1 * x + 5) = 1 * x^2 + 3 * x + 3
		==> x^2 -4x - 32 = 0
		==> x = -4 或 x = 8
	(2).如果下列的公式成立：78+78=123，则采用的是_______进制表示的
		7 * x + 8 + 7 * x + 8 = 1 * x^2 + 2 * x + 3
		==>
		x^2 - 12 * x - 13 = 0
		==> x = -1, x = 13
3.多态问题:当超类对象引用变量引用子类对象时，被引用对象的类型而不是引用变量的类型决定了调用谁的成员方法，
	但是这个被调用的方法必须是在超类中定义过的，也就是说被子类覆盖的方法
	[http://wiki.jikexueyuan.com/project/java-enhancement/java-three.html]
	优先级由高到低依次为：this.show(O)、super.show(O)、this.show((super)O)、super.show((super)O)
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
	            System.out.println("1--" + a1.show(b));
	            System.out.println("2--" + a1.show(c));
	            System.out.println("3--" + a1.show(d));
	            System.out.println("4--" + a2.show(b));
	            System.out.println("5--" + a2.show(c));
	            System.out.println("6--" + a2.show(d));
	            System.out.println("7--" + b.show(b));
	            System.out.println("8--" + b.show(c));
	            System.out.println("9--" + b.show(d));      
	        }
	    }
	          运行结果：
	    1--A and A
	    2--A and A
	    3--A and D
	    4--B and A
	    5--B and A
	    6--A and D
	    7--B and B
	    8--B and B
	    9--A and D

4.抽象类中的抽象方法为什么不能被 static,final 修饰:
	(1).static:
		抽象类不能被实例化,而 static 修饰的方法独立与对象实例之外,由类直接调用,static 的方法必须是被实现的;
	(2).final:
		final 修饰的的方法不能被重写

5.实现多重继承:接口和内部类

6.静态代码块、构造代码块、构造函数执行顺序:
	静态代码块，静态，其作用级别为类，构造代码块、构造函数，构造，其作用级别为对象
	(1)、静态代码块，它是随着类的加载而被执行，只要类被加载了就会执行，而且只会加载一次，主要用于给类进行初始化。
	(2)、构造代码块，每创建一个对象时就会执行一次，且优先于构造函数，主要用于初始化不同对象共性的初始化内容和初始化实例环境。
	(3)、构造函数，每创建一个对象时就会执行一次。同时构造函数是给特定对象进行初始化，而构造代码是给所有对象进行初始化，
		作用区域不同。
	==> 通过上面的分析，他们三者的执行顺序应该为：
		静态代码块 > 构造代码块 > 构造函数。
	6.1.Java 类初始化过程:
		(1).首先，初始化父类中的静态成员变量和静态代码块，按照在程序中出现的顺序初始化； 
		(2).然后，初始化子类中的静态成员变量和静态代码块，按照在程序中出现的顺序初始化； 
		(3).其次，初始化父类的普通成员变量和代码块，在执行父类的构造方法；
		(4).最后，初始化子类的普通成员变量和代码块，在执行子类的构造方法；
	6.2.不要在构造器里调用可能被重载的虚方法,
		父类构造器执行的时候，调用了子类的重载方法，然而子类的类字段还在刚初始化的阶段，刚完成内存布局:
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
7.关于 String+ 和 StringBuffer 的比较:在 String+写成一个表达式的时候(更准确的说，是写成一个赋值语句的时候)，
	效率其实比 Stringbuffer更快
	public class Main{	    
	    public static void main(String[] args){
	        /*   1   */
	        String string = "a" + "b" + "c";
	        /*   2   */
	        StringBuffer stringBuffer = new StringBuffer();
	        stringBuffer.append("a").append("b").append("c");
	        string = stringBuffer.toString();
	    }	    
	}
	7.1.String+的写法要比 Stringbuffer 快，是因为在编译这段程序的时候，编译器会进行常量优化,
	它会将a、b、c直接合成一个常量abc保存在对应的 class 文件当中{},看如下反编译的代码:
		public class Main{}
			public static void main(String[] args){
			    String string = "abc";
			    StringBuffer stringBuffer = new StringBuffer();
			    stringBuffer.append("a").append("b").append("c");
			    string = stringBuffer.toString();
			}
		}
	原因是因为 String+其实是由 Stringbuilder 完成的，而一般情况下 Stringbuilder 要快于 Stringbuffer，
	这是因为 Stringbuilder 线程不安全，少了很多线程锁的时间开销，因此这里依然是 string+的写法速度更快;	
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
	7.2.字符串拼接方式:	+、concat() 以及 append() 方法,  append()速度最快，concat()次之，+最慢
		(1).编译器对+进行了优化，它是使用 StringBuilder 的 append() 方法来进行处理的,
		编译器使用 append() 方法追加后要同 toString() 转换成 String 字符串,变慢的关键原因就在于 new StringBuilder() 和 
		toString()，这里可是创建了 10 W 个 StringBuilder 对象，而且每次还需要将其转换成 String
		(2).concat:
			concat() 的源码，它看上去就是一个数字拷贝形式，我们知道数组的处理速度是非常快的，但是由于该方法最后是这样的：
			return new String(0, count + otherLen, buf);这同样也创建了 10 W 个字符串对象，这是它变慢的根本原因
		(3).append() 方法拼接字符串:并没有产生新的字符串对象

8.关于数组问题:
	8.1.数组扩容:可以参照利用 List 集合中的add方法模拟实现:
		// datas 原始数组    newLen 扩容大小
		public static <T> T[] expandCapacity(T[] datas,int newLen){
           	newLen = newLen < 0 ? datas.length :datas.length + newLen;   
           	//生成一个新的数组
           	return Arrays.copyOf(datas, newLen);
        }
        // datas  原始数组
        public static <T> T[] expandCapacity(T[] datas){
            int newLen = (datas.length * 3) / 2;      //扩容原始数组的1.5倍
            //生成一个新的数组
            return Arrays.copyOf(datas, newLen);
        }
        // datas 原始数组    mulitiple 扩容的倍数
        public static <T> T[] expandCapacityMul(T[] datas,int mulitiple){
            mulitiple = mulitiple < 0 ? 1 : mulitiple;
            int newLen = datas.length * mulitiple;
            return Arrays.copyOf(datas,newLen );
        }
	8.2.数组复制问题:
		所以通过 Arrays.copyOf() 方法产生的数组是一个浅拷贝。同时数组的 clone() 方法也是，集合的 clone() 方法也是，
		所以我们在使用拷贝方法的同时一定要注意浅拷贝这问题
	8.3.数组转换为 List: asList 返回的是一个长度不可变的列表。数组是多长，转换成的列表就是多长，
		我们是无法通过 add、remove 来增加或者减少其长度的
		public static void main(String[] args) {
            int[] datas = new int[]{1,2,3,4,5};
            List list = Arrays.asList(datas);
            System.out.println(list.size()); // 1
        }
        ==> 为什么上述结果输出为 1??
        	首先看 asList的源码:
        	public static <T> List<T> asList(T... a) {
            	return new ArrayList<T>(a);
        	}
        	(1).注意这个参数:T…a，这个参数是一个泛型的变长参数，我们知道基本数据类型是不可能泛型化的，也是就说 8 个基本数据类
        	型是不可作为泛型参数的，但是为什么编译器没有报错呢？这是因为在 Java 中，数组会当做一个对象来处理，它是可以泛型的，
        	所以我们的程序是把一个 int 型的数组作为了 T 的类型，所以在转换之后 List 中就只会存在一个类型为 int 数组的元素了;
        	(2).这里是直接返回一个 ArrayList 对象返回，但是注意这个 ArrayList 并不是 java.util.ArrayList,
        		而是 Arrays 工具类的一个内之类,这个内部类并没有提供 add() 方法，那么查看父类 AbstractList
        		仅仅只是提供了方法，方法的具体实现却没有，所以具体的实现需要子类自己来提供,
        		但是非常遗憾这个内部类 ArrayList 并没有提供 add 的实现方法
        	size：元素数量、toArray：转换为数组，实现了数组的浅拷贝、get：获得指定元素、contains：是否包含某元素
9.基本类型与引用类型的比较:
	9.1.如下四个变量,哪两个比较为 false
		Integer i01 = 59;
		int i02 = 59;
		Integer i03 =Integer.valueOf(59);
		Integer i04 = new Integer(59);
		(1).Integer 为了节省空间和内存会在内存中缓存 -128~127 之间的数字;
		(2).valueOf():调用该方法时,内部实现作了个判断,判断当前传入的值是否在 -128~127 之间且 IntergCache 是否已存在该对象
			如果存在,则直接返回引用,如果不存在,则创建一个新对象
		(3).基本类型存在内存的栈中,与引用类型比较时, 引用类型会自动装箱,比较数值而不比较内存地址;

10.Java 重载与重写
	10.1.重载:能够用一个统一的接口名称来调用一系列方法
		(1).重载本身并不是多态,同时运行时绑定重载方法也不是多态的表现;
		(2).如下例子:重载方法"3"注释与不注释,结果有和不一样				 
			public class NullArguementOverloading {
			    public static void main(String[] args) {
			        NullArguementOverloading obj = new NullArguementOverloading();
			        obj.overLoad(null); // Double array argument method.
			    }
			    private void overLoad(Object o){ // 1
			        System.out.println("Object o arguement method.");
			    }
			    private void overLoad(double[] dArray){ //2
			        System.out.println("Double array argument method.");
			    }
			    private void overLoad(String str) { //3
			        System.out.println("String argument method.");
			    }
			}
			①.注释掉"3",运行结果:Double array argument method
			②.不注释掉:obj.overLoad(null);编译错误
		(3).Java 对重载的处理有最精确匹配原则:
			①.Java 的重载解析过程是以两阶段运行的:
				==>第一阶段 选取所有可获得并且可应用的方法或构造器;
				==>第二阶段在第一阶段选取的方法或构造器中选取最精确的一个;
			②.上面代码:String 也是继承自 Object, 数组也是可认为继承自 Object, 两个为平行等级,null 不确定到底是哪个;
			③.另外,重载是在编译期就已经确定了的，并不需要等到运行时才能确定,因此重载不是多态的一个原因.
			public class OverridePuzzle {			 
			    private void overloadList(List list){
			        System.out.println("List arguement method.");
			    }			 
			    private void overloadList(ArrayList arrayList){
			        System.out.println("ArrayList arguement method");
			    }
			    public static void main(String[] args) {
			        OverridePuzzle op = new OverridePuzzle();
			        List list = new ArrayList<String>();
			        op.overloadList(list); // List arguement method
			    }			 
			}
			④.重载对于传入的参数类型只认了引用的类型,并没有去解析实际对象的类型.
				如果重载是一种多态的话，它这里应该去解析实际对象的类型并调用ArrayList的方法
	10.2.重写:涉及到继承这个概念中的问题,子类继承了父类的方法,但是它可能需要有不同的操作行为,就需要在子类中重写这个父类方法
		(1).父类如果将方法声明为 final 的就可保证所有子类的调用此方法时调用的都是父类的方法;	
	10.3.两者的比较:
		(1).重载是一个编译期概念、重写是一个运行期间概念;
		(2).重载遵循所谓“编译期绑定”，即在编译时根据参数变量的类型判断应该调用哪个方法。
		(3).重写遵循所谓“运行期绑定”，即在运行的时候，根据引用变量所指向的实际对象的类型来调用方法
		(4).因为在编译期已经确定调用哪个方法，所以重载并不是多态。而重写是多态。重载只是一种语言特性，
			是一种语法规则，与多态无关，与面向对象也无关。(注:严格来说，重载是编译时多态，即静态多态。
			但是，Java中提到的多态，在不特别说明的情况下都指动态多态)
	10.4.重写的条件:
		参数列表必须完全与被重写方法的相同；
		返回类型必须完全与被重写方法的返回类型相同；
		访问级别的限制性一定不能比被重写方法的强；
		访问级别的限制性可以比被重写方法的弱；
		重写方法一定不能抛出新的检查异常或比被重写的方法声明的检查异常更广泛的检查异常
		重写的方法能够抛出更少或更有限的异常（也就是说，被重写的方法声明了异常，但重写的方法可以什么也不声明）
		不能重写被标示为final的方法；
		如果不能继承一个方法，则不能重写这个方法
	10.5.重载的条件:
		被重载的方法必须改变参数列表；
		被重载的方法可以改变返回类型；
		被重载的方法可以改变访问修饰符；
		被重载的方法可以声明新的或更广的检查异常；
		方法能够在同一个类中或者在一个子类中被重载;


















