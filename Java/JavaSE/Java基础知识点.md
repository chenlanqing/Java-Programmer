# 一.Java 内部类:
## 1.为什么使用内部类?
	使用内部类最吸引人的原因是:每个内部类都能独立地继承一个(接口的)实现,所以无论外围类是否已经继承了某个(接口的)实现,对于内部类都没有影响
#### 1.1.使用内部类最大的优点就在于它能够非常好的解决多重继承的问题,使用内部类还能够为我们带来如下特性:
	(1).内部类可以用多个实例,每个实例都有自己的状态信息,并且与其他外围对象的信息相互独。
	(2).在单个外围类中,可以让多个内部类以不同的方式实现同一个接口,或者继承同一个类。
	(3).创建内部类对象的时刻并不依赖于外围类对象的创建。
	(4).内部类并没有令人迷惑的"is-a"关系,他就是一个独立的实体。
	(5).内部类提供了更好的封装,除了该外围类,其他类都不能访问。

## 2.内部类分类:
### (一).成员内部类:
	public class Outer{
		private int age = 99;
		String name = "Coco";
		public class Inner{
			String name = "Jayden";
			public void show(){
				System.out.println(Outer.this.name);
				System.out.println(name);
				System.out.println(age);
			}
		}
		public Inner getInnerClass(){
			return new Inner();
		}
		public static void main(String[] args){
			Outer o = new Outer();
			Inner in = o.new Inner();
			in.show();
		}
	}
	1.Inner 类定义在 Outer 类的内部,相当于 Outer 类的一个成员变量的位置,Inner 类可以使用任意访问控制符,如 public 、 protected 、 private 等
	2.Inner 类中定义的 show() 方法可以直接访问 Outer 类中的数据,而不受访问控制符的影响,如直接访问 Outer 类中的私有属性age
	3.定义了成员内部类后,必须使用外部类对象来创建内部类对象,而不能直接去 new 一个内部类对象,即:内部类 对象名 = 外部类对象.new 内部类();
	4.编译上面的程序后,会发现产生了两个 .class 文件: Outer.class,Outer$Inner.class{},在编译后成员内部类中有一个指向外部类对象的引用,
	  且成员内部类编译后构造方法也多了一个指向外部类对象的引用参数.所以说编译器会默认为成员内部类添加了一个指向外部类对象的引用并且在成员内
	  部类构造方法中对其进行赋值操作.我们可以在成员内部类中随意访问外部类的成员,同时也说明成员内部类是依赖于外部类的,如果没有创建外部类的对
	  象则也无法创建成员内部类的对象.
	5.成员内部类中不能存在任何 static 的变量和方法,可以定义常量:
		(1).因为非静态内部类是要依赖于外部类的实例,而静态变量和方法是不依赖于对象的,仅与类相关,
			简而言之:在加载静态域时,根本没有外部类,所在在非静态内部类中不能定义静态域或方法,编译不通过;非静态内部类的作用域是实例级别
		(2).常量是在编译器就确定的,放到所谓的常量池了
	6.成员内部类的继承:
		public class ChildInnerClass1 extends OutClass.InnerClass {
		    public ChildInnerClass1(OutClass outClass){
		        outClass.super();
		    }
		}
		成员内部类的继承语法格式要求继承引用方式为 Outter.Inner 形式且继承类的构造器中必须有指向外部类对象的引用,
		并通过这个引用调用 super(),其实这个要求就是因为成员内部类默认持有外部类的引用,外部类不先实例化则无法实例化自己.
	★★友情提示:
	1.外部类是不能直接使用内部类的成员和方法的,可先创建内部类的对象,然后通过内部类的对象来访问其成员变量和方法;
	2.如果外部类和内部类具有相同的成员变量或方法,内部类默认访问自己的成员变量或方法,如果要访问外部类的成员变量,可以使用 this 关键字,如:Outer.this.name
### (二).静态内部类: 是 static 修饰的内部类,
	1.静态内部类不能直接访问外部类的非静态成员,但可以通过 new 外部类().成员 的方式访问
	2.如果外部类的静态成员与内部类的成员名称相同,可通过"类名.静态成员"访问外部类的静态成员；
	如果外部类的静态成员与内部类的成员名称不相同,则可通过"成员名"直接调用外部类的静态成员
	3.创建静态内部类的对象时,不需要外部类的对象,可以直接创建 内部类 对象名 = new 内部类();
		public class Outer{
			private int age = 99;
			static String name = "Coco";
			public static class Inner{
				String name = "Jayden";
				public void show(){
					System.out.println(Outer.name);
					System.out.println(name);					
				}
			}
			public static void main(String[] args){
				Inner i = new Inner();
				i.show();
			}
		}
	4.非静态内部类中为什么不能有静态属性或者方法?
	sttaic 类型的属性和方法在类加载的时候就会存在于内存中,要使用某个类的 static 属性或者方法的前提是这个类已经加载到JVM中,
	非 static 内部类默认是持有外部类的引用且依赖外部类存在的,所以如果一个非 static 的内部类一旦具有 static 的属性或者方法
	就会出现内部类未加载时却试图在内存中创建的 static 属性和方法,这自然是错误的,类都不存在却希望操作它的属性和方法.
	从另一个角度讲非 static 的内部类在实例化的时候才会加载(不自动跟随主类加载),而static 的语义是类能直接通过类名访问类的 static
	属性或者方法,所以如果没有实例化非 static  的内部类就等于非 static 的内部类没有被加载,所以无从谈起通过类名访问 static 属性或者方法.
### (三).方法内部类:访问仅限于方法内或者该作用域内
	(1).局部内部类就像是方法里面的一个局部变量一样,是不能有 public、protected、private 以及 static 修饰符的
	(2).只能访问方法中定义的 final 类型的局部变量,因为:当方法被调用运行完毕之后,局部变量就已消亡了.但内部类对象可能还存在,
        直到没有被引用时才会消亡.此时就会出现一种情况,就是内部类要访问一个不存在的局部变量;
		==>使用final修饰符不仅会保持对象的引用不会改变,而且编译器还会持续维护这个对象在回调方法中的生命周期.
		局部内部类并不是直接调用方法传进来的参数,而是内部类将传进来的参数通过自己的构造器备份到了自己的内部,
		自己内部的方法调用的实际是自己的属性而不是外部类方法的参数;防止被篡改数据,而导致内部类得到的值不一致
		```
		/**
		 * 使用的形参为何要为 final???
		 * 在内部类中的属性和外部方法的参数两者从外表上看是同一个东西,但实际上却不是,所以他们两者是可以任意变化的,
		 * 也就是说在内部类中我对属性的改变并不会影响到外部的形参,而然这从程序员的角度来看这是不可行的,
		 * 毕竟站在程序的角度来看这两个根本就是同一个,如果内部类该变了,而外部方法的形参却没有改变这是难以理解
		 * 和不可接受的,所以为了保持参数的一致性,就规定使用 final 来避免形参的不改变
		 */
		public class Outer{
			public void Show(){
				final int a = 25;
				int b = 13;
				class Inner{
					int c = 2;
					public void print(){
						System.out.println("访问外部类:" + a);
						System.out.println("访问内部类:" + c);
					}
				}
				Inner i = new Inner();
				i.print();
			}
			public static void main(String[] args){
				Outer o = new Outer();
				o.show();
			}
		}
		```
	(3).注意:在JDK8版本之中,方法内部类中调用方法中的局部变量,可以不需要修饰为 final,匿名内部类也是一样的,主要是JDK8之后增加了 Effectively final 功能
		反编译jdk8编译之后的class文件,发现内部类引用外部的局部变量都是 final 修饰的
		参考文章:http://docs.oracle.com/javase/tutorial/java/javaOO/localclasses.html
		如下代码:
		```
		public class OutClass {
			private int out = 1;
			public void func(int param) {
				int in = 2;
				new Thread() {
					@Override
		            public void run() {
						out = param;
						out = in;
					}
				}.start();
			}
		}
		在java8中使用命令查看字节码:javap -l -v OutClass$1.class,如下:
		......
		class OutClass$1 extends java.lang.Thread
		......
		{
			//匿名内部类有了自己的 param 属性成员  
			final int val$param;
			......
			//匿名内部类持有了外部类的引用作为一个属性成员	  
			final OutClass this$0;  
			......
			//匿名内部类编译后构造方法自动多了两个参数，一个为外部类引用，一个为 param 参数。
			OutClass$1 (OutClass,int);
			......
			public void run();    
				......
				Code:
					stack=2, locals=1, args_size=1
					//out = param;语句，将匿名内部类自己的 param 属性赋值给外部类的成员 out。
					0: aload_0
					1: getfield #1 // Field this$0:LOutClass;
					4: aload_0
		         	5: getfield #2 // Field val$param:I
		         	8: invokestatic  #4 // Method OutClass.access$002:(LOutClass;I)I
		          	11: pop        
					//out = in;语句，将匿名内部类常量 2 (in在编译时确定值)赋值给外部类的成员 out。
		        	12: aload_0      
					13: getfield    #1 // Field this$0:LOutClass;
					//将操作数2压栈，因为如果这个变量的值在编译期间可以确定则编译器默认会在
					//匿名内部类或方法内部类的常量池中添加一个内容相等的字面量或直接将相应的
					//字节码嵌入到执行字节码中。
					16: iconst_2
					17: invokestatic  #4 // Method OutClass.access$002:(LOutClass;I)I
		        	20: pop
		        	21:	return
					......
		}
		......
		```
### (四).匿名内部类:
	(1).匿名内部类是直接使用 new 来生成一个对象的引用;
	(2).对于匿名内部类的使用它是存在一个缺陷的,就是它仅能被使用一次,创建匿名内部类时它会立即创建一个该类的实例,
		该类的定义会立即消失,所以匿名内部类是不能够被重复使用;
	(3).使用匿名内部类时,我们必须是继承一个类或者实现一个接口,但是两者不可兼得,同时也只能继承一个类或者实现一个接口;
	(4).匿名内部类中是不能定义构造函数的,匿名内部类中不能存在任何的静态成员变量和静态方法;
	(5).匿名内部类中不能存在任何的静态成员变量和静态方法,匿名内部类不能是抽象的,它必须要实现继承的类或者实现的接口的所有抽象方法
	(6).匿名内部类初始化:使用构造代码块！利用构造代码块能够达到为匿名内部类创建一个构造器的效果.
		匿名内部类不能通过构造方法初始化,只能通过构造代码块进行初始化
		public class OuterClass {
	        public InnerClass getInnerClass(final int   num,String str2){
	            return new InnerClass(){
	                int number = num + 3;
	                public int getNumber(){
	                    return number;
	                }
	            };        // 注意：分号不能省
	        }
	        public static void main(String[] args) {
	            OuterClass out = new OuterClass();
	            InnerClass inner = out.getInnerClass(2, "chenssy");
	            System.out.println(inner.getNumber());
	        }
	    }
	    interface InnerClass {
	        int getNumber();
	    }
	(7).如下代码:
		List list1 = new ArrayList();
		List list2 = new ArrayList(){};
		List list3 = new ArrayList(){{}};
		List list4 = new ArrayList(){{}{}{}};(
		System.out.println(list1.getClass() == list2.getClass()); // false
		System.out.println(list1.getClass() == list3.getClass()); // false
		System.out.println(list1.getClass() == list4.getClass()); // false
		System.out.println(list2.getClass() == list3.getClass()); // false
		System.out.println(list2.getClass() == list4.getClass()); // false
		System.out.println(list3.getClass() == list4.getClass()); // false

		首先 list1 指向一个 ArrayList 对象实例;
		list2 指向一个继承自 ArrayList 的匿名类内部类对象;
		list3 也指向一个继承自 ArrayList 的匿名内部类(里面一对括弧为初始化代码块)对象;
		list4 也指向一个继承自 ArrayList 的匿名内部类(里面多对括弧为多个初始化代码块)对象;
		由于这些匿名内部类都出现在同一个类中,所以编译后其实得到的是 OutClass$1、OutClass$2、OutClass$3 的形式,所以自然都互补相等了,
		可以通过 listX.getClass().getName() 进行验证:
		System.out.println(list1.getClass().getName()); // java.util.ArrayList
		System.out.println(list2.getClass().getName()); // com.demo.normal.OutClass$1
		System.out.println(list3.getClass().getName()); // com.demo.normal.OutClass$2
		System.out.println(list4.getClass().getName()); // com.demo.normal.OutClass$3
	(8).匿名内部类为什么不能直接使用构造方法?
		因为类是匿名的,而且每次创建的匿名内部类同时被实例化后只能使用一次,所以就无从创建一个同名的构造方法了,但是可以直接调用父类的构造方法.
		实质上类是有构造方法的,是通过编译器在编译时生成的,看如下代码:
		public class InnerClass{}
		public class OutClass{
		    InnerClass inner = new InnerClass(){};
		}
		编译之后使用命令 javap 可以很明显看到内部类的字节码中编译器为我们生成了参数为外部类引用的构造方法,
		其构造方法和普通类的构造方法没有区别,都是执行 <init> 方式;

# 二.HashMap vs. TreeMap vs. Hashtable vs. LinkedHashMap
##### (1).HashMap,TreeMap,HashTable父接口都是Map,LinkedHashMap是HashMap的子类;
##### (2).HashMap:如果HashMap的key是自定义的对象,则需要重写equals()和hashcode()方法:
◆★原因是:HashMap 不允许两个相同的元素;默认情况下,在Object类下实现的equals()和hashcode()方法被使用,
默认的hashcode()方法给出不同的整数为不同的对象,并在equals()方法中,只有当两个引用指向的是同一个对象时才返回true
```
	public class HashMapDemo {
			public static void main(String[] args) {
				HashMap<Dog, Integer> hashMap = new HashMap<Dog, Integer>();
				Dog d1 = new Dog("red");
				Dog d2 = new Dog("black");
				Dog d3 = new Dog("white");
				Dog d4 = new Dog("white");
				hashMap.put(d1, 10);
				hashMap.put(d2, 15);
				hashMap.put(d3, 5);
				hashMap.put(d4, 20);
				//print size
				System.out.println(hashMap.size());
				//loop HashMap
				for (Entry<Dog, Integer> entry : hashMap.entrySet()) {
					System.out.println(entry.getKey().toString() + " - " + entry.getValue());
				}
			}
		}
		class Dog {
			String color;
			Dog(String c) {
				color = c;
			}
			public String toString(){
				return color + " dog";
			}
			public boolean equals(Object o) {
				return ((Dog) o).color.equals(this.color);
			}
			public int hashCode() {
				return color.length();
			}
		}

```
##### (3).TreeMap:是按照key来排序的,因此如果自定义对象作为key必须能够相互比较,因此其必须实现Comparable接口,
如我们使用String作为key,是因为String已经实现了Comparable接口,如例子:

```
class Dog {
			String color;
			Dog(String c) {
				color = c;
			}
			public boolean equals(Object o) {
				return ((Dog) o).color.equals(this.color);
			}
			public int hashCode() {
				return color.length();
			}
			public String toString(){
				return color + " dog";
			}
		}
		public class TestTreeMap {
			public static void main(String[] args) {
				Dog d1 = new Dog("red");
				Dog d2 = new Dog("black");
				Dog d3 = new Dog("white");
				Dog d4 = new Dog("white");
				TreeMap<Dog, Integer> treeMap = new TreeMap<Dog, Integer>();
				treeMap.put(d1, 10);
				treeMap.put(d2, 15);
				treeMap.put(d3, 5);
				treeMap.put(d4, 20);
				for (Entry<Dog, Integer> entry : treeMap.entrySet()) {
					System.out.println(entry.getKey() + " - " + entry.getValue());
				}
			}
		}
	◆上述代码运行报异常:
		Exception in thread "main" java.lang.ClassCastException: collection.Dog cannot be cast to java.lang.Comparable
		at java.util.TreeMap.put(Unknown Source)
		at collection.TestHashMap.main(TestHashMap.java:35)
	修改上述代码:
		class Dog implements Comparable<Dog>{
			String color;
			int size;		 
			Dog(String c, int s) {
				color = c;
				size = s;
			}		 
			public String toString(){
				return color + " dog";
			}		 
			@Override
			public int compareTo(Dog o) {
				return  o.size - this.size;
			}
		}

		public class TestTreeMap {
			public static void main(String[] args) {
				Dog d1 = new Dog("red", 30);
				Dog d2 = new Dog("black", 20);
				Dog d3 = new Dog("white", 10);
				Dog d4 = new Dog("white", 10);
				TreeMap<Dog, Integer> treeMap = new TreeMap<Dog, Integer>();
				treeMap.put(d1, 10);
				treeMap.put(d2, 15);
				treeMap.put(d3, 5);
				treeMap.put(d4, 20);
				for (Entry<Dog, Integer> entry : treeMap.entrySet()) {
					System.out.println(entry.getKey() + " - " + entry.getValue());
				}
			}
		}
```


##### (4).LinkedHashMap 与 HashMap 的不同区别是:LinkedHashMap 保留了插入顺序.

##### (5).HashMap,HashTable,TreeMap:
	A:迭代顺序:HashMap,HashTable不会保证元素的顺序,但是TreeMap是有序的;
	B:key-value空值:HashMap的key-value都可以为空(只有一个key为 null,因为不能存在两个相同的key),
		HashTable的key-value不允许为 null;
		TreeMap因为key是有序,因此key不能为 null,value可以为 null;


2.HashCode与HashSet关系:

# 三.按照目录结构打印当前目录及子目录
	public class PrintDirectory {
		public static void main(String[] args) {
			File file = new File("E:\\下载");
			PrintDirectory pd = new PrintDirectory();
			pd.listDirectory(file,0);
		}
		//列出该目录的子目录
		private void listDirectory(File dir,int level){
			System.out.println(getSpace(level) + dir.getName());
			level++;
			File[] files = dir.listFiles();		
			for(int i=0;i<files.length;i++){
				if(files[i].isDirectory()){
					listDirectory(files[i],level);
				}else{
					System.out.println(getSpace(level)+files[i].getName());
				}
			}
		}
		//按照目录结构打印目录
		private String getSpace(int level){
			StringBuilder sb = new StringBuilder();
			for(int i=0;i<level;i++){
				sb.append("|--");
			}
			return sb.toString();
		}
	}

# 四.Java 关键字的意义:
### 1.native:
	native 关键字可以应用于方法,以指示该方法是用 Java 以外的语言实现的
### 2.transient:
	transient 关键字可以应用于类的成员变量,以便指出该成员变量不应在包含它的类实例已序列化时被序列化
	Java 的 serialization 提供了一种持久化对象实例的机制.当持久化对象时,可能有一个特殊的对象数据成员,
	我们不想用serialization机制来保存它,为了在一个特定对象的一个域上关闭serialization,可以在这个域前加上关键字 transient.   
	transient 是 Java 语言的关键字,用来表示一个域不是该对象串行化的一部分。当一个对象被串行化的时候,
	transient 型变量的值不包括在串行化的表示中,然而非 transient 型的变量是被包括进去的.
### 3.final:
	 * http://www.importnew.com/18586.html
	 * http://www.importnew.com/7553.html	 
	3.1.含义:
		final 在Java中是一个保留的关键字,可以声明成员变量、方法、类以及本地变量.一旦你将引用声明作 final,
		你将不能改变这个引用了,编译器会检查代码,如果你试图将变量再次初始化的话,编译器会报编译错误;
	3.2.final 修饰符:
		(1).修饰变量:
			对于一个 final 变量,如果是基本数据类型的变量,则其数值一旦在初始化之后便不能更改;
			如果是引用类型的变量,则在对其初始化之后便不能再让其指向另一个对象;
		(2).修饰方法:
			方法前面加上 final 关键字,代表这个方法不可以被子类的方法重写;
			final 方法比非 final 方法要快,因为在编译的时候已经静态绑定了,不需要在运行时再动态绑定
			==>类的 private 方法会隐式地被指定为final方法;
		(3).修饰类:
			当用 final 修饰一个类时,表明这个类不能被继承,final 类中的所有成员方法都会被隐式地指定为 final 方法;
			Java 中许多类都是 final 类,如:String,Integer
	3.3.注意点:
		(1).final 和 static:
			static 作用于成员变量用来表示只保存一份副本,而 final 的作用是用来保证变量不可变
			看代码:每次打印的两个j值都是一样的,而i的值却是不同的
			public class Demo01 {
				public static void main(String[] args) {
					MyDemo1 d1 = new MyDemo1();
					MyDemo1 d2 = new MyDemo1();
					System.out.println(d1.i);
					System.out.println(d2.i);
					System.out.println(d1.j);
					System.out.println(d2.j);
				}
			}
			class MyDemo1{
				public final double i = Math.random();
				public static double j = Math.random();
			}
		(2).匿名内部类中使用的外部局部变量为什么只能是 final 变量(参考上面内部类)
	3.4.为什么使用 final?
		(1).final 关键字提高了性能。JVM 和 Java 应用都会缓存 final 变量。
		(2).final 变量可以安全的在多线程环境下进行共享,而不需要额外的同步开销。
		(3).使用 final 关键字,JVM 会对方法、变量及类进行优化;
	3.5.不可变类:
		创建不可变类要使用 final 关键字。不可变类是指它的对象一旦被创建了就不能被更改了。String 是不可变类的代表。
		不可变类有很多好处,譬如它们的对象是只读的,可以在多线程环境下安全的共享,不用额外的同步开销等等;
		==> 不可变对象:
			如果某个对象在被创建后其状态不能被修改,那么这个对象就称为不可变对象.不可变对象一定是线程安全的.
		==> 如何编写不可变类:
			(1).将类声明为final,所以它不能被继承;
			(2).将所有的成员声明为私有的,这样就不允许直接访问这些成员;
			(3).对变量不要提供setter方法;
			(4).将所有可变的成员声明为final,这样只能对它们赋值一次;
			(5).通过构造器初始化所有成员,进行深拷贝(deep copy);
			(6).在getter方法中,不要直接返回对象本身,而是克隆对象,并返回对象的拷贝;
	3.6.知识点:
		(1).final 成员变量必须在声明的时候初始化或者在构造器中初始化,否则就会报编译错误;
		(2).接口中声明的所有变量本身是 final 的;
		(3).final 和 abstract 这两个关键字是反相关的,final 类就不可能是 abstract 的;
		(4).final 方法在编译阶段绑定,称为静态绑定(static binding)
		(5).将类、方法、变量声明为 final 能够提高性能,这样 JVM 就有机会进行估计,然后优化;
### 4.instanceof:
	4.1.一些使用注意事项
		(1).只能用于对象的判断,不能用于基本类型的判断;
		(2).若左操作数是 null 则结果直接返回 false,不再运算右操作数是什么类	    
		    (String)null instanceof String; // false;
		    因为 null 没有类型,所以即使做类型转换还是 null
		(3).instanceof 的右操作符必须是一个接口或者类:
			"demo" instanceof null; // 编译错误
		(4).数组类型也可以使用 instanceof 判断:
			String[] str = new String[10];
			str instanceof String[]; //  true
	4.2.instanceof 与 clazz.isInstance(obj):
		(1).instanceof 运算符用来在运行时指出对象是否是特定类的一个实例,通过返回一个布尔值来指出这个对象是否是这个特定类或者是它的子类的一个实例.
			result = object instanceof class
			但是 instanceof 在 java 的编译状态和运行状态是有区别的,在编译状态中 class可以是 object 对象的父类、自身类、子类,在这三种情况下
			java 编译时不会报错,在运行转态中 class 可以是 object 对象的父类、自身类但不能是子类;{}当为父类、自生类的情况下 result 结果为 true,
			为子类的情况下为 false;
		(2).clazz.isInstance(obj):表明这个对象能不能被转化为这个类
			一个对象能被转化为本身类所继承类(父类的父类等)和实现的接口(接口的父接口)强转;
	4.3.instanceof 与 clazz.getClass():
		(1).instanceof 进行类型检查规则是你属于该类吗？或者你属于该类的派生类吗？
		(2).clazz.getClass():获得类型信息采用 == 来进行检查是否相等的操作是严格比较,不存在继承方面的考虑;
	4.4.instanceof 实现原理:???

# 五.协变式重写和泛型重载:
## 1.协变式重写
	1.1.在Java1.4及以前,子类方法如果要覆盖超类的某个方法,必须具有完全相同的方法签名,包括返回值也必须完全一样;
	Java5.0放宽了这一限制,只要子类方法与超类方法具有相同的方法签名,或者子类方法的返回值是超类方法的子类型,就可以覆盖;
	可以不需要强制转换类型
	// 例如: 重写 Object 类的 clone()方法:
	(1).Object 中该方法的声明如下:
		protected native Object clone() throws CloneNotSupportedException;
	(2).在类中可以重写实现如下:
		@Override
		public Employee clone() throws CloneNotSupportedException {
			Employee e = (Employee) super.clone();
			e.address = address.clone();
			return e;
		}

## 2.泛型重载:
	(1).Java的方法重载一般指在同一个类中的两个同名方法,规则很简单：两个方法必须具有不同的方法签名;
	换句话说:就是这两个方法的参数必须不相同,使得编译器能够区分开这两个重载的方法;
	由于编译器不能仅仅通过方法的返回值类型来区分重载方法,所以如果两个方法只有返回类型不同,其它完全一样,编译是不能通过的。
	// 在泛型方法的重载时,这个规则稍微有一点变化,看如下代码:
		class Overloaded {
			 public static int sum(List<Integer> ints) {			
				return 0;
			 }
			 public static String sum(List<String> strings) {
				return null;
			 }
		}
	上面是两个泛型方法的重载例子,由于Java的泛型采用擦除法实现,List<Integer>和List<String>在运行时是完全一样的,
	都是List类型.也就是,擦除后的方法签名如下：
		int sum(List)
		String sum(List)

	(2).Java允许这两个方法进行重载,虽然它们的方法签名相同,只有返回值类型不同,这在两个普通方法的重载中是不允许的;
		当然了,如果两个泛型方法的参数在擦除后相同,而且返回值类型也完全一样,那编译肯定是不能通过的;
		// 类似地,一个类不能同时继承两个具有相同擦除类型的父类,也不能同时实现两个具有相同擦除的接口。
		// 如Class A implements Comparable<Integer>, Comparable<Long>。
	(3).总结一下:两个泛型方法在擦除泛型信息后,如果具有相同的参数类型,而返回值不一样,是可以进行重载的;
	Java有足够的信息来区分这两个重载的方法
## 3.重写与重载:
	3.1.两者的比较:
		(1).重载是一个编译期概念、重写是一个运行期间概念;
		(2).重载遵循所谓"编译期绑定",即在编译时根据参数变量的类型判断应该调用哪个方法。
		(3).重写遵循所谓"运行期绑定",即在运行的时候,根据引用变量所指向的实际对象的类型来调用方法
		(4).因为在编译期已经确定调用哪个方法,所以重载并不是多态。而重写是多态。重载只是一种语言特性,
			是一种语法规则,与多态无关,与面向对象也无关。(注:严格来说,重载是编译时多态,即静态多态。
			但是,Java中提到的多态,在不特别说明的情况下都指动态多态)
	3.2.重写的条件:
		参数列表必须完全与被重写方法的相同；
		返回类型必须完全与被重写方法的返回类型相同；
		访问级别的限制性一定不能比被重写方法的强；
		访问级别的限制性可以比被重写方法的弱；
		重写方法一定不能抛出新的检查异常或比被重写的方法声明的检查异常更广泛的检查异常
		重写的方法能够抛出更少或更有限的异常(也就是说,被重写的方法声明了异常,但重写的方法可以什么也不声明)
		不能重写被标示为final的方法；
		如果不能继承一个方法,则不能重写这个方法
		参数列表必须完全与被重写方法的相同；
	3.3.重载的条件:
		被重载的方法必须改变参数列表；
		被重载的方法可以改变返回类型；
		被重载的方法可以改变访问修饰符；
		被重载的方法可以声明新的或更广的检查异常；
		方法能够在同一个类中或者在一个子类中被重载;
## 4.重载:能够用一个统一的接口名称来调用一系列方法
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
		③.另外,重载是在编译期就已经确定了的,并不需要等到运行时才能确定,因此重载不是多态的一个原因.
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
			如果重载是一种多态的话,它这里应该去解析实际对象的类型并调用ArrayList的方法
## 5.重写:
    涉及到继承这个概念中的问题,子类继承了父类的方法,但是它可能需要有不同的操作行为,就需要在子类中重写这个父类方法.父类如果将方法声明为 final 的就可保证所有子类的调用此方法时调用的都是父类的方法;
## 6.两者的比较:
	(1).重载是一个编译期概念、重写是一个运行期间概念;
	(2).重载遵循所谓"编译期绑定",即在编译时根据参数变量的类型判断应该调用哪个方法。
	(3).重写遵循所谓"运行期绑定",即在运行的时候,根据引用变量所指向的实际对象的类型来调用方法
	(4).因为在编译期已经确定调用哪个方法,所以重载并不是多态。而重写是多态。重载只是一种语言特性,
		是一种语法规则,与多态无关,与面向对象也无关。(注:严格来说,重载是编译时多态,即静态多态。
		但是,Java中提到的多态,在不特别说明的情况下都指动态多态)
# 六.Java 序列化:序列化是一种对象持久化的手段
## 1.Java 对象序列化：
    JDK 1.1 中引入的一组开创性特性之一,用于作为一种将 Java 对象的状态转换为字节数组,以便存储或传输的机制,以后,
	仍可以将字节数组转换回 Java 对象原有的状态
	1.1.基本点:
		(1).对象序列化保存的是对象的"状态",即它的成员变量。由此可知,对象序列化不会关注类中的"静态变量";
		(2).在 Java 中,只要一个类实现了 java.io.Serializable 接口,那么它就可以被序列化;
			实现 Externalizable,自己要对序列化内容进行控制,控制哪些属性可以被序列化,哪些不能被序列化
		(3).通过 ObjectOutputStream 和 ObjectInputStream 对对象进行序列化及反序列化;
		(4).虚拟机是否允许反序列化,不仅取决于类路径和功能代码是否一致,一个非常重要的一点是两个类的序列化 ID 是否一致,
			就是 private static final long serialVersionUID;
		(5).transient 关键字的作用是控制变量的序列化,在变量声明前加上该关键字,可以阻止该变量被序列化到文件中,
		在被反序列化后,transient 变量的值被设为初始值,如 int 型的是 0,对象型的是 null
		(6).Java 序列化机制为了节省磁盘空间,具有特定的存储规则,当写入文件的为同一对象时,并不会再将对象的内容进行存储,
			而只是再次存储一份引用,上面增加的 5 字节的存储空间就是新增引用和一些控制信息的空间.反序列化时,恢复引用关系
			该存储规则极大的节省了存储空间;
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("result.obj"));
				Test test = new Test();
				test.i = 1;
				out.writeObject(test);
				out.flush();
				test.i = 2;
				out.writeObject(test);
				out.close();
				ObjectInputStream oin = new ObjectInputStream(new FileInputStream(
									"result.obj"));
				Test t1 = (Test) oin.readObject();
				Test t2 = (Test) oin.readObject();
				System.out.println(t1.i);// 1
				System.out.println(t2.i);// 1
				// 结果两个输出的都是 1, 原因就是第一次写入对象以后,第二次再试图写的时候,虚拟机根据引用关系
				// 知道已经有一个相同对象已经写入文件,因此只保存第二次写的引用,所以读取时,都是第一次保存的对象
	1.2.子类与父类序列化:
		(1).要想将父类对象也序列化,就需要让父类也实现 Serializable 接口;
		(2).如果父类实现了 Serializable 接口,子类但没有实现 Serializable 接口,子类拥有一切可序列化相关的特性,子类可以序列化;
		(3).如果子类实现 Serializable 接口,父类不实现,根据父类序列化规则,父类的字段数据将不被序列化,从而达到部分序列化的功能;
		(4).在反序列化时仍会调用父类的构造器,只能调用父类的无参构造函数作为默认的父对象.如果父类没有默认构造方法则在反序列化时会出异常.
		(5).如果父类实现了 Serializable 接口,要让子类不可序列化,可以在子类中写如下代码:(其实违反了里氏替换原则)
			private void writeObject(java.io.ObjectOutputStream out) throws IOException{
		        throw new NotSerializableException("不可写");
		    }
		    private void readObject(java.io.ObjectInputStream in) throws IOException{
		        throw new NotSerializableException("不可读");
		    }
		(6).序列化与反序列化时子类和父类构造方法调用关系:
			序列化时子类递归调用父类的构造函数,反序列化作用于子类对象时如果其父类没有实现序列化接口则其父类的默认无参构造函数会被调用.
			如果父类实现了序列化接口则不会调用构造方法.

## 2.在序列化过程中,如果被序列化的类中定义了writeObject 和 readObject 方法,
	虚拟机会试图调用对象类里的 writeObject 和 readObject 方法,进行用户自定义的序列化和反序列化。
	如果没有这样的方法,则默认调用是 ObjectOutputStream 的 defaultWriteObject 方法以及
	 ObjectInputStream 的 defaultReadObject 方法。
	用户自定义的 writeObject 和 readObject 方法可以允许用户控制序列化的过程,比如可以在序列化的过程中动态改变序列化的数值;
	2.1.ArrayList 使用上述实现:为什么ArrayList要用这种方式来实现序列化呢?
		(1).为什么 transient Object[] elementData;
			ArrayList 实际上是动态数组,每次在放满以后自动增长设定的长度值,如果数组自动增长长度设为100,而实际只放了一个元素,
			那就会序列化 99 个 null 元素。为了保证在序列化的时候不会将这么多 null 同时进行序列化,ArrayList 把元素数组设置为 transient
		(2).为什么要写方法:writeObject and readObject
			前面提到为了防止一个包含大量空对象的数组被序列化,为了优化存储,所以,ArrayList 使用 transient 来声明elementData作为一个集合,
			在序列化过程中还必须保证其中的元素可以被持久化下来,所以,通过重写writeObject 和 readObject方法的方式把其中的元素保留下来
			writeObject方法把elementData数组中的元素遍历的保存到输出流(ObjectOutputStream)中.
			readObject方法从输入流(ObjectInputStream)中读出对象并保存赋值到elementData数组中
	2.2.如何自定义的序列化和反序列化策略
		可以通过在被序列化的类中增加writeObject 和 readObject方法。那么问题又来了;
		(1).那么如果一个类中包含writeObject 和 readObject 方法,那么这两个方法是怎么被调用的呢
			在使用 ObjectOutputStream 的writeObject方法和 ObjectInputStream 的readObject方法时,会通过反射的方式调用
			①.ObjectOutputStream 的writeObject 的调用栈：
				writeObject ---> writeObject0 --->writeOrdinaryObject--->writeSerialData--->invokeWriteObject
			②.这里看一下invokeWriteObject：
				其中writeObjectMethod.invoke(obj, new Object[]{ out });是关键,通过反射的方式调用writeObjectMethod方法
	2.3.Serializable 明明就是一个空的接口,它是怎么保证只有实现了该接口的方法才能进行序列化与反序列化的呢？
		看 ObjectOutputStream 的writeObject 的调用栈：
		writeObject ---> writeObject0 --->writeOrdinaryObject--->writeSerialData--->invokeWriteObject
		writeObject0方法中有这么一段代码：
		if (obj instanceof String) {
                writeString((String) obj, unshared);
            } else if (cl.isArray()) {
                writeArray(obj, desc, unshared);
            } else if (obj instanceof Enum) {
                writeEnum((Enum<?>) obj, desc, unshared);
            } else if (obj instanceof Serializable) {
                writeOrdinaryObject(obj, desc, unshared);
            } else {
                if (extendedDebugInfo) {
                    throw new NotSerializableException(
                        cl.getName() + "\n" + debugInfoStack.toString());
                } else {
                    throw new NotSerializableException(cl.getName());
                }
            }
		在进行序列化操作时,会判断要被序列化的类是否是 Enum、Array 和 Serializable 类型,如果不是则直接抛出 NotSerializableException
	2.4.writeReplace() 和 readResolve():
		Serializable 除过提供了 writeObject 和 readObject 标记方法外还提供了另外两个标记方法可以实现序列化对象的替换(即 writeReplace 和 readResolve),
		2.4.1.writeReplace:序列化类一旦实现了 writeReplace 方法后则在序列化时就会先调用 writeReplace 方法将当前对象替换成另一个对象,
			该方法会返回替换后的对象.接着系统将再次调用另一个对象的 writeReplace 方法,直到该方法不再返回另一个对象为止,程序最后将调用该对象的
			writeObject() 方法来保存该对象的状态.
			(1).实现了 writeReplace 的序列化类就不要再实现 writeObject 了,因为该类的 writeObject 方法就不会被调用了.
			(2).实现 writeReplace 的返回对象必须是可序列化的对象;
			(3).通过 writeReplace 序列化替换的对象在反序列化中无论实现哪个方法都是无法恢复原对象的.
			(4).所以 writeObject 只和 readObject 配合使用,一旦实现了 writeReplace 在写入时进行替换就不再需要 writeObject 和 readObject 了.
		2.4.2.readResolve:方法可以实现保护性复制整个对象,会紧挨着序列化类实现的 readObject() 之后被调用,该方法的返回值会代替原来反序列化的对象
			而原来序列化类中 readObject() 反序列化的对象将会立即丢弃.
			readObject() 方法在序列化单例类时尤其有用,单例序列化都应该提供 readResolve() 方法,这样才可以保证反序列化的对象依然正常.

## 3.private static final long serialVersionUID:每个可序列化类相关联
	(1).该序列号在反序列化过程中用于验证序列化对象的发送者和接收者是否为该对象加载了与序列化兼容的类;
	(2).如果接收者加载的该对象的类的 serialVersionUID 与对应的发送者的类的版本号不同,
		则反序列化将会导致 InvalidClassException;
	(3).为保证 serialVersionUID 值跨不同 java 编译器实现的一致性,序列化类必须声明一个明确的 serialVersionUID ;
	(4).使用 private 修饰符显示声明 serialVersionUID(如果可能),
		原因是这种声明仅应用于直接声明类 – serialVersionUID 字段作为继承成员没有用处;
	(5).类的serialVersionUID的默认值完全依赖于Java编译器的实现,对于同一个类,用不同的Java编译器编译,
		有可能会导致不同的serialVersionUID,也有可能相同
	(6).显式地定义serialVersionUID有两种用途:
		①.在某些场合,希望类的不同版本对序列化兼容,因此需要确保类的不同版本具有相同的serialVersionUID；在某些场合,
			不希望类的不同版本对序列化兼容,因此需要确保类的不同版本具有不同的serialVersionUID
		②.当你序列化了一个类实例后,希望更改一个字段或添加一个字段,不设置serialVersionUID,所做的任何更改都将导致
			无法反序化旧有实例,并在反序列化时抛出一个异常。如果你添加了serialVersionUID,在反序列旧有实例时,
			新添加或更改的字段值将设为初始化值(对象为null,基本类型为相应的初始默认值),字段被删除将不设置
## 4.反序列化:
	(1).实现 Serializable 接口的对象在反序列化时不需要调用对象所在类的构造方法,完全基于字节,如果是子类继承父类的序列化,那么将调用父类的构造方法;
	(2).实现 Externalizable  接口的对象在反序列化时会调用构造方法.该接口继承自 Serializable,使用该接口后基于 Serializable 接口的序列化机制就会失效,
		因为 Externalizable 不会主动序列化,当使用该接口时序列化的细节需要由我们自己去实现.
		使用 Externalizable 主动进行序列化时当读取对象时会调用被序列化类的无参构方法去创建一个新的对象,然后再将被保存对象的字段值分别填充到新对象中.
		所以 所以实现 Externalizable 接口的类必须提供一个无参 public 的构造方法,
		readExternal 方法必须按照与 writeExternal 方法写入值时相同的顺序和类型来读取属性值.

## 5.序列化实现对象的拷贝:
	内存中通过字节流的拷贝是比较容易实现的.把母对象写入到一个字节流中,再从字节流中将其读出来,这样就可以创建一个新的对象了,
	并且该新对象与母对象之间并不存在引用共享的问题,真正实现对象的深拷贝
	public class CloneUtils {
        @SuppressWarnings("unchecked")
        public static <T extends Serializable> T clone(T   obj){
            T cloneObj = null;
            try {
                //写入字节流
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ObjectOutputStream obs = new ObjectOutputStream(out);
                obs.writeObject(obj);
                obs.close();

                //分配内存,写入原始对象,生成新对象
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
## 6.常见的序列化协议:
	(1).COM:主要用于windows 平台,并没有实现跨平台,其序列化原理是利用编译器中的虚表
	(2).CORBA:早期比较好的实现了跨平台,跨语言的序列化协议,COBRA 的主要问题是参与方过多带来的版本过多,
		版本之间兼容性较差,以及使用复杂晦涩;
	(3).XML&SOAP:
		XML 是一种常用的序列化和反序列化协议,具有跨机器,跨语言等优点;
		SOAP(Simple Object Access protocol)是一种被广泛应用的,基于XML为序列化和反序列化协议的结构化消息传递协议;
		SOAP具有安全、可扩展、跨语言、跨平台并支持多种传输层协议
	(4).JSON(Javascript Object Notation)
		①.这种Associative array格式非常符合工程师对对象的理解;
		②.它保持了XML的人眼可读(Human-readable)的优点;
		③.相对xml而言,序列化都的数据更简洁;
		④.它具备Javascript的先天性支持,所以被广泛应用于Web browser的应用常景中,是Ajax的事实标准协议;
		⑤.与XML相比,其协议比较简单,解析速度比较快;
		⑥.松散的Associative array使得其具有良好的可扩展性和兼容性
	(5).Thrift:是 Facebook 开源提供的一个高性能,轻量级 RPC 服务框架,其产生正是为了满足当前大数据量、分布式、跨语言、
		跨平台数据通讯的需求;其并不仅仅是序列化协议,而是一个 RPC 框架;
		由于Thrift的序列化被嵌入到Thrift框架里面,Thrift框架本身并没有透出序列化和反序列化接口,
		这导致其很难和其他传输层协议共同使用;		
	(6).Protobuf:
		①.标准的IDL和IDL编译器,这使得其对工程师非常友好;
		②.序列化数据非常简洁,紧凑,与XML相比,其序列化之后的数据量约为1/3到1/10;
		③.解析速度非常快,比对应的XML快约20-100倍;
		④.提供了非常友好的动态库,使用非常简介,反序列化只需要一行代码;
## 7.JSON 序列化:
	7.1.关于Map转json输出顺序问题:
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("b", "2");
		map.put("a", "1");
		map.put("c", "3");
		System.out.println(JSON.toJSON(map));// {"a":"1","b":"2","c":"3"}

		Map<String, String> map1 = new LinkedHashMap<String, String>();
		map1.put("b", "2");
		map1.put("a", "1");
		map1.put("c", "3");
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        System.out.println(gson.toJson(map1)); // {"b":"2","a":"1","c":"3"}
        (1).使用fastjson或者jdk自带的序列化,默认是无序输出的,如果需要使用fastJson输出有序的json:JSONObject,构造的时候使用 new JSONObject(true)
        	JSONObject object = new JSONObject(true);
	        Map<String, String> map2 = new LinkedHashMap<String, String>();
	        map2.put("b", "2");
	        map2.put("a", "1");
	        map2.put("c", "3");
	        object.putAll(map2);
	        System.out.println(JSONObject.toJSON(object));// {"b":"2","a":"1","c":"3"}
	    (2).Gson 保证了你插入的顺序,就是正常的Map迭代操作
## 8.序列化安全:
	(1).序列化在传输中是不安全的:因为序列化二进制格式完全编写在文档中且完全可逆,所以只需将二进制序列化流的内容转储到控制台就可以看清类及其包含的内容,
		故序列化对象中的任何 private 字段几乎都是以明文的方式出现在序列化流中.
	(2).要解决序列化安全问题的核心原理就是避免在序列化中传递敏感数据,所以可以使用关键字 transient 修饰敏感数据的变量.
		或者通过自定义序列化相关流程对数据进行签名加密机制再存储或者传输
# 七.泛型:
## 1.JDK1.5 引入的新特性：
    允许在定义类和接口的时候使用类型参数(type parameter),泛型最主要的应用是在JDK 5中的新集合类框架中;
	其本质是参参数化类型
	==> 为什么要有泛型?
		* 在1.5之前没有泛型的情况下,只能通过对类型 Object 的引用来实现参数的任意化,其带来的缺点是需要显示强制转换,而强制转换在编译期不做检查,
		  容易把代码流到运行时.使用泛型的好处是在编译时检查类型安全,提高了代码重用率, 避免产生 ClassCastException
## 2.类型擦除(type erasure):
    使用泛型的时候加上的类型参数,会被编译器在编译的时候去掉,这个过程就称为类型擦除.
	2.1.Java 中的泛型基本上都是在编译器这个层次来实现的,在生成的 Java 字节代码中是不包含泛型中的类型信息的;		
	2.2.泛型的类型参数不能用在Java异常处理的catch语句中.因为异常处理是由JVM在运行时刻来进行的,
		而由泛型附加的类型信息对JVM来说是不可见的;
	2.2.类型擦除的基本过程:
		(1).首先是找到用来替换类型参数的具体类,这个具体类一般是 Object,如果指定了类型参数的上界的话,则使用这个上界.
			把代码中的类型参数都替换成具体的类,同时去掉出现的类型声明,即去掉<>的内容
		(2).可能需要生成一些桥接方法(bridge method)
	2.3.编译器承担了全部的类型检查工作,编译器禁止某些泛型的使用方式,正是为了确保类型的安全性:
		public void inspect(List<Object> list) {    
		    for (Object obj : list) {        
		        System.out.println(obj);    
		    }    
		    list.add(1);
		//这个操作在当前方法的上下文是合法的。
		}
		public void test() {    
		    List<String> strs = new ArrayList<String>();    
		    inspect(strs);
		// 编译错误
		}

		 // 假设这样的做法是允许的,那么在inspect方法就可以通过list.add(1)来向集合中添加一个数字。这样在test方法看来,
		 // 其声明为List<String>的集合中却被添加了一个Integer类型的对象。这显然是违反类型安全的原则的,
		 // 在某个时候肯定会抛出ClassCastException

## 3.通配符与上下界:
	3.1.在使用泛型类的时候,既可以指定一个具体的类型,也可以用通配符?来表示未知类型,如 List<?>
 	3.2.通配符所代表的其实是一组类型,但具体的类型是未知的,但是 List<?>并不等同于 List<Object>
 		List<Object> 实际上确定了 List 中包含的是 Object 及其子类,在使用的时候都可以通过 Object 来进行引用。
 		而 List<?>则其中所包含的元素类型是不确定;
 	3.3.对于 List<?>中的元素只能用 Object 来引用,在有些情况下不是很方便.在这些情况下,可以使用上下界来限制未知类型的范围
 		如:List<? extends Number>说明 List 中可能包含的元素类型是 Number 及其子类
 		而:List<? super Number> 则说明 List 中包含的是 Number 及其父类
 		当引入了上界之后,在使用类型的时候就可以使用上界类中定义的方法
 	3.4.关于 <? extends T> 和 <? super T>
 		3.4.1.<? extends T>:表示参数化的类型可能是所指定的类型,或者是此类型的子类
	 		public class DemoGenerice {
	 			public static void main(String[] args) {
	 				List<? extends Season> list = new LinkedList<Season>();
	 				list.add(new Spring()); //  编译错误
	 			}

	 		}
	 		class Season{}
	 		class Spring extends Season{}
	 		(1).编译错误原因:
	 			List<? extends Season> 表示 "具有任何从 Season 继承类型的列表",编译器无法确定 List 所持有的类型,
	 			所以无法安全的向其中添加对象。可以添加 null,因为 null 可以表示任何类型。
	 			所以 List 的add 方法不能添加任何有意义的元素;
	 	3.4.2.<? super T>:表示参数化的类型可能是所指定的类型,或者是此类型的父类型,直至Object

	 	3.4.3.PECS原则:
	 		(1).如果要从集合中读取类型T的数据,并且不能写入,可以使用 ? extends 通配符；(Producer Extends)
	 		(2).如果要从集合中写入类型T的数据,并且不需要读取,可以使用 ? super 通配符；(Consumer Super)
	 		(3).如果既要存又要取,那么就不要使用任何通配符
## 4.Java 类型系统:
	4.1.在 Java 中,通过继承机制而产生的类型体系结构是大家熟悉的.
		根据Liskov替换原则,子类是可以替换父类的,但是反过来的话,即用父类的引用替换子类引用的时候,就需要进行强制类型转换
	4.2.引入泛型后,类型系统增加了两个维度:
		(1).一个是类型参数自身的继承体系结构.List<String>和List<Object>这样的情况,类型参数String是继承自Object的
		(2).一个是泛型类或接口自身的继承体系结构.第二种指的是 List 接口继承自 Collection 接口
		对于这个类型系统,有如下规则:
		==> 相同类型参数的泛型类的关系取决于泛型类自身的继承体系结构,即 List<String>是 Collection<String> 的子类型,
			List<String>可以替换 Collection<String>
		==> 当泛型类的类型声明中使用了通配符的时候,其子类型可以在两个维度上分别展开:
			对于 Collection<? extends Number>来说,
			①.其子类型可以在 Collection 这个维度上展开 List<? extends Number>和 Set<? extends Number>等
			②.也可以在 Number 这个层次上展开,即 Collection<Double>和 Collection<Integer>等
				ArrayList<Long>和 HashSet<Double>等也都算是 Collection<? extends Number>的子类型
## 5.开发自己的泛型类:
	(1).泛型类与一般的Java类基本相同,只是在类和接口定义上多出来了用<>声明的类型参数
	(2).所声明的类型参数在Java类中可以像一般的类型一样作为方法的参数和返回值,或是作为域和局部变量的类型
	(3).由于类型擦除机制,类型参数并不能用来创建对象或是作为静态变量的类型
		class ClassTest<X extends Number, Y, Z> {    
		    private X x;    
		    private static Y y;
		//编译错误,不能用在静态变量中    
		    public X getFirst() {		        
		//正确用法        
		        return x;    
		    }    
		    public void wrong() {        
		        Z z = new Z();
		//编译错误,不能创建对象    
		    }
		}
## 6.在使用泛型的时候可以遵循一些基本的原则:
	(1).在代码中避免泛型类和原始类型的混用;
	(2).在使用带通配符的泛型类的时候,需要明确通配符所代表的一组类型的概念

## 八.关于 try...catch...finally:
    (http://www.cnblogs.com/aigongsi/archive/2012/04/19/2457735.html)
	首先看如下例子,最终结果是什么? // false
	public boolean returnTest(){
		try{
			return true;
		} catch (Exception e) {

		} finally {
			return false;
		}
	}
	1.关于 try...catch...finally 使用的几点总结:
		(1).try、catch、finally 语句中,在如果 try 语句有 return 语句,则返回的之后当前 try 中变量此时对应的值,
			此后对变量做任何的修改,都不影响 try 中 return 的返回值;
		(2).如果 finally 块中有 return 语句,则 try 或 catch 中的返回语句忽略;
		(3).如果 finally 块中抛出异常,则整个 try、catch、finally 块中抛出异常;
		(4).如果 catch 异常中写了多个需要 catch 的异常,可以如果匹配到了捕获的异常,则后面其他的异常都将被忽略
	2.使用 try...catch...finally 需要注意:
		(1).尽量在 try 或者 catch 中使用 return 语句.通过 finally 块中达到对 try 或者 catch 返回值修改是不可行的;
		(2).finally 块中避免使用 return 语句,因为 finally 块中如果使用 return 语句,
			会显示的消化掉 try、catch 块中的异常信息,屏蔽了错误的发生;
		(3).finally 块中避免再次抛出异常,如果 try 或者 catch 中抛出的异常信息会被覆盖掉.
			public static void main(String[] args) throws Exception {
					test1();
				}
			public static void test1()throws Exception {
				try{
					int[] arr = new int[5];
					arr[5] = 10;// 这里会抛出: ArrayIndexOutOfBoundsException
				} finally {
					System.out.println(1/0);// 这里会抛出: ArithmeticException
				}
			}
			上述代码最终抛出的异常信息为:
			Exception in thread "main" java.lang.ArithmeticException: / by zero
				at com.exe1.TestSort.test1(TestSort.java:14)
				at com.exe1.TestSort.main(TestSort.java:6)
	3.在 try 里面通过 System.exit(0) 来退出 JVM 的情况下 finally 块中的代码才不会执行.
		其他 return 等情况都会调用,所以在不终止 JVM 的情况下 finally 中的代码一定会执行.


# 九.Java 四舍五入:
## 1.目前 Java 支持7中舍入法
	(1).ROUND_UP：远离零方向舍入。向绝对值最大的方向舍入,只要舍弃位非0即进位
	(2).ROUND_DOWN：趋向零方向舍入。向绝对值最小的方向输入,所有的位都要舍弃,不存在进位情况
	(3).ROUND_CEILING：向正无穷方向舍入。向正最大方向靠拢。若是正数,舍入行为类似于 ROUND_UP,
		若为负数,舍入行为类似于 ROUND_DOWN。 Math.round() 方法就是使用的此模式。
	(4).ROUND_FLOOR：向负无穷方向舍入。向负无穷方向靠拢。若是正数,舍入行为类似于 ROUND_DOWN；
		若为负数,舍入行为类似于 ROUND_UP。
	(5).HALF_UP：最近数字舍入(5进)。这是我们最经典的四舍五入。
	(6).HALF_DOWN：最近数字舍入(5舍)。在这里5是要舍弃的。
	(7).HAIL_EVEN：银行家舍入法。
## 2.保留位:
	(1).四舍五入:
		double   f   =   111231.5585;
	    BigDecimal   b   =   new   BigDecimal(f);
	    double   f1   =   b.setScale(2,   RoundingMode.HALF_UP).doubleValue();
	(2).格式化:
		java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00″);
		df.format(你要格式化的数字);
	(3).类C语言:
		double d = 3.1415926;
		String result = String .format("%.2f");
		%.2f %. 表示 小数点前任意位数   2 表示两位小数 格式后的结果为f 表示浮点型
	(4).此外如果使用 struts 标签做输出的话, 有个 format 属性,设置为 format="0.00″就是保留两位小数
		<bean:write name="entity" property="dkhAFSumPl"  format="0.00" />
		或者
		<fmt:formatNumber type="number" value="${10000.22/100}" maxFractionDigits="0"/>
		maxFractionDigits表示保留的位数
## 3.Math:
	3.1.四舍五入:
		double d1=-0.5;
		System.out.println("Ceil d1="+Math.ceil(d1)); // -0.0
		System.out.println("floor d1="+Math.floor(d1)); // -1.0
		System.out.println("floor d1="+Math.round(d1)); // 0
		(1).ceil():该方法返回的是一个 double 类型数据;返回一个大于该参数的最小 double 值,等于某个整数,特殊情况:
			①.如果参数小于0且大于-1.0,则结果为-0.0;
			②.如果参数数学上等于某个整数,则结果与该参数相同;如:5.0;
			③.如果参数为 NaN,无穷大,正0或负0,那么结果与参数相同;
			==> 特别注意:Math.ceil(d1) == -Math.floor(-d1);
		(2).floor():返回 double 类型数据,返回一个小于该参数的最大 double 值,等于某个整数
			①.如果参数数学上等于某个整数,则结果与该参数相同;如:5.0;
			②.如果参数为 NaN,无穷大,正0或负0,那么结果与参数相同;
		(3).round():返回一个整数,如果参数为 float,返回 int 类型;如果参数为 double,返回 long 类型
			(int)Math.floor(a + 0.5f);
			(long)Math.floor(a + 0.5d);
			返回最接近参数的 int 或 long 类型数据,将参数加上 1/2, 对结果调用 floor 将所得结果强转为 int 或 long
			①.如果参数为 NaN, 结果为 0
			②.如果结果为负无穷大或任何小于等于 Integer.MIN_VALUE 或 Long.MIN_VALUE 的值,
				那么结果等于 Integer.MIN_VALUE 或 Long.MIN_VALUE 的值。
			③.如果参数为正无穷大或任何大于等于 Integer.MAX_VALUE 或 Long.MAX_VALUE 的值,
				那么结果等于 Integer.MAX_VALUE 或 Long.MAX_VALUE 的值

# 十.Java 中保留小数位数的处理:
## 1.使用 BigDecimal,保留小数点后两位
	public static String format1(double value) {
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(2, RoundingMode.HALF_UP);
	    return bd.toString();
	}
## 2.使用 DecimalFormat,保留小数点后两位
	public static String format2(double value) {
	    DecimalFormat df = new DecimalFormat("0.00");
	    df.setRoundingMode(RoundingMode.HALF_UP);
	    return df.format(value);
	}

## 3.使用 NumberFormat,保留小数点后两位
	public static String format3(double value) {
	    NumberFormat nf = NumberFormat.getNumberInstance();
	    nf.setMaximumFractionDigits(2);
	    // setMinimumFractionDigits设置成2,如果不这么做,那么当value的值是100.00的时候返回100,而不是100.00
	    nf.setMinimumFractionDigits(2);
	    nf.setRoundingMode(RoundingMode.HALF_UP);
	    // 如果想输出的格式用逗号隔开,可以设置成true
	    nf.setGroupingUsed(false);
	    return nf.format(value);
	}

## 4.使用 java.util.Formatter,保留小数点后两位
	public static String format4(double value) {
	    // %.2f % 表示 小数点前任意位数 2 表示两位小数 格式后的结果为 f 表示浮点型
	    return new Formatter().format("%.2f", value).toString();
	}

## 5.使用 String.format来实现
	public static String format5(double value) {
	    return String.format("%.2f", value).toString();
	}
	5.1.对浮点数进行格式化:占位符格式为： %[index$][标识] * [最小宽度][.精度]转换符
		ouble num = 123.4567899;
		System.out.print(String.format("%f %n", num)); // 123.456790
		System.out.print(String.format("%a %n", num)); // 0x1.edd3c0bb46929p6
		System.out.print(String.format("%g %n", num)); // 123.457
		(1).可用标识符
			-,在最小宽度内左对齐,不可以与0标识一起使用。
			0,若内容长度不足最小宽度,则在左边用0来填充。
			#,对8进制和16进制,8进制前添加一个0,16进制前添加0x。
			+,结果总包含一个+或-号。
			空格,正数前加空格,负数前加-号。
			,,只用与十进制,每3位数字间用,分隔。
			(,若结果为负数,则用括号括住,且不显示符号。
		(2).可用转换符：
			b,布尔类型,只要实参为非false的布尔类型,均格式化为字符串true,否则为字符串false。
			n,平台独立的换行符, 也可通过System.getProperty("line.separator")获取。
			f,浮点数型(十进制)。显示9位有效数字,且会进行四舍五入。如99.99。
			a,浮点数型(十六进制)。
			e,指数类型。如9.38e+5。
			g,浮点数型(比%f,%a长度短些,显示6位有效数字,且会进行四舍五入)

# 十一.Java 中 length 和 length() 的区别:
    1.获取数组的长度是使用属性 length,获取字符串长度是使用方法 length()
    2.为什么数组有length属性?
	(1).数组是一个容器对象,其中包含固定数量的同一类型的值.一旦数组被创建,那么数组的长度就是固定的了.
		数组的长度可以作为final实例变量的长度。因此,长度可以被视为一个数组的属性
	(2).有两种创建数组的方法：1、通过数组表达式创建数组。2、通过初始化值创建数组。
		无论使用哪种方式,一旦数组被创建,其大小就固定了
    3.Java 中为什么没有定义一个类似 String 一样 Array 类:
	数组包含所有从 Object 继承下来方法,为什么没有一个array类呢?一个简单的解释是它被隐藏起来了
    4.为什么 String 有length()方法?
	背后的数据结构是一个 char 数组,所以没有必要来定义一个不必要的属性(因为该属性在 char 数值中已经提供了)

# 十二.数组:
## 1.Java 中数组是对象吗?
	(1).什么是对象:
		语言层面:对象是根据某个类创建出来的一个实例,表示某类事物中一个具体的个体.对象具有各种属性,并且具有一些特定的行为
		计算机层面:对象就是内存中的一个内存块,在这个内存块封装了一些数据,也就是类中定义的各个属性
	(2).数组:
		语言层面上,数组不是某类事物中的一个具体的个体,而是多个个体的集合,那么数组应该不是对象;
		而在计算机的角度,数组也是一个内存块,也封装了一些数据,这样的话也可以称之为对象
			int[] a = new int[4];
			//a.length;  //对属性的引用不能当成语句
			int len = a.length;  //数组中保存一个字段, 表示数组的长度		
			//以下方法说明数组可以调用方法,java中的数组是对象.
			//这些方法是Object中的方法,所以可以肯定,数组的最顶层父类也是Object
			a.clone();
			a.toString();
	==> 这基本上可以认定,java中的数组也是对象,它具有java中其他对象的一些基本特点:
		封装了一些数据,可以访问属性,也可以调用方法.所以:Java数组是对象
	==> 而在 C++中,数组虽然封装了数据,但数组名只是一个指针,指向数组中的首个元素,既没有属性,也没有方法可以调用
		所以 C++中的数组不是对象,只是一个数据的集合,而不能当做对象来使用
## 2.Java中数组的类型:数组也是有类型的
	2.1.虚拟机自动创建了数组类型,可以把数组类型和8种基本数据类型一样, 当做java的内建类型:
		(1).每一维度用一个"["表示;开头两个"[",就代表是二维数组.
		(2)."["后面是数组中元素的类型(包括基本数据类型和引用数据类型).
	2.2.String[] s = new String[4];
		在java语言层面上,s是数组,也是一个对象,那么他的类型应该是 String[]
		在JVM中,他的类型为 [java.lang.String
## 3.Java中数组的继承关系:
	3.1.数组的顶层父类也必须是 Object,这就说明数组对象可以向上直接转型到 Object,也可以向下强制类型转换,
		也可以使用 instanceof 关键字做类型判定
		//1		在test1()中已经测试得到以下结论: 数组也是对象, 数组的顶层父类是Object, 所以可以向上转型
		int[] a = new int[8];
		Object obj = a ; //数组的父类也是Object,可以将a向上转型到Object		
		//2		那么能向下转型吗?
		int[] b = (int[])obj;  //可以进行向下转型		
		//3		能使用instanceof关键字判定吗?
		if(obj instanceof int[]){  //可以用instanceof关键字进行类型判定
			System.out.println("obj的真实类型是int[]");
		}
	3.2.Java中数组的另一种"继承"关系:
		String[] s = new String[5];
		Object[] obja = s;   //成立,说明可以用Object[]的引用来接收String[]的对象
		s的直接父类是?
		//5那么String[] 的直接父类是Object[] 还是 Object?  
		System.out.println(s.getClass().getSuperclass().getName());  
		//打印结果为java.lang.Object,说明String[] 的直接父类是 Object而不是Object[]  
		(1).数组类直接继承了 Object,关于 Object[]类型的引用能够指向 String[]类型的对象,这并不是严格意义上的继承,
			String[] 不继承自 Object[],但是可以允许 String[]向上转型到 Object[]
			可以理解为:
			其实这种关系可以这样表述：如果有两个类A和B,如果B继承(extends)了A,那么A[]类型的引用就可以指向B[]类型的对象
		(2).数组的这种用法不能作用于基本类型数据:
			int[] aa = new int[4];  
			Object[] objaa = aa;  //错误的,不能通过编译  
			因为 int 不是引用类型,Object 不是 int 的父类,在这里自动装箱不起作用
			Object 数组中可以存放任何值,包括基本数据类型
			public class ArrayTest {
				public static void main(String[] args) {
					test1();
					test2();
					test3();
				}
				/**
				 * 数组具有这种特性：
				 * 如果有两个类A和B,如果B继承(extends)了A,那么A[]类型的引用就可以指向B[]类型的对象
				 * 测试数组的特殊特性对参数传递的便利性
				 * /
				private static void test3() {
					String[] a = new String[3];
					doArray(a);
				}
				private static void doArray(Object[] objs){

				}
				private static void doArray1(Object obj){
					//不能用Object接收数组,因为这样无法对数组的元素进行访问
					// obj[1]  //错误

					//如果在方法内部对obj转型到数组,存在类型转换异常的风险
					// Object[] objs = (Object[]) obj;
				}
				private static void doArray2(String[] strs){
					//如果适用特定类型的数组,就限制了类型,失去灵活性和通用性
				}
				private static void doArray3(String name, int age, String id, float account){
					//如果不适用数组而是依次传递参数,会使参数列表变得冗长,难以阅读
				}
				/**
				 * 测试数组的集成关系, 并且他的继承关系是否和数组中元素的类型有关
				 * /
				private static void test2() {

					//1		在test1()中已经测试得到以下结论: 数组也是对象, 数组的顶层父类是Object, 所以可以向上转型
					int[] a = new int[8];
					Object obj = a ; //数组的父类也是Object,可以将a向上转型到Object

					//2		那么能向下转型吗?
					int[] b = (int[])obj;  //可以进行向下转型

					//3		能使用instanceof关键字判定吗?
					if(obj instanceof int[]){  //可以用instanceof关键字进行类型判定
						System.out.println("obj的真实类型是int[]");
					}

					//4  	下面代码成立吗?
					String[] s = new String[5];
					Object[] obja = s;   //成立,说明可以用Object[]的引用来接收String[]的对象

					//5		那么String[] 的直接父类是Object[] 还是 Object?
					System.out.println(s.getClass().getSuperclass().getName());
					//打印结果为java.lang.Object,说明String[] 的直接父类是 Object而不是Object[]

					//6	  下面成立吗?  Father是Son的直接父类
					Son[] sons = new Son[3];
					Father[] fa = sons;  //成立

					//7		那么Son[] 的直接父类是Father[] 还是  Object[] 或者是Object?
					System.out.println(sons.getClass().getSuperclass().getName());
					//打印结果为java.lang.Object,说明Son[]的直接父类是Object

					/**
					 * 做一下总结, 如果A是B的父类, 那么A[] 类型的引用可以指向 B[]类型的变量
					 * 但是B[]的直接父类是Object, 所有数组的父类都是Object
					 * /

					//8		上面的结论可以扩展到二维数组
					Son[][] sonss = new Son[2][4];
					Father[][] fathers = sonss;
					//将Father[][]数组看成是一维数组, 这是个数组中的元素为Father[]
					//将Son[][]数组看成是一维数组, 这是个数组中的元素为Son[]
					//因为Father[]类型的引用可以指向Son[]类型的对象
					//所以,根据上面的结论,Father[][]的引用可以指向Son[][]类型的对象

					/**
					 * 扩展结论:
					 * 因为Object是所有引用类型的父类
					 * 所以Object[]的引用可以指向任何引用数据类型的数组的对象. 如:
					 * Object[] objs = new String[1];
					 * Object[] objs = new Son[1];
					 *
					 * /

					//9		下面的代码成立吗?
					int[] aa = new int[4];
					//Object[] objaa = aa;  //错误的,不能通过编译
					//这是错误的, 因为Object不是int的父类,在这里自动装箱不起作用

					//10 	这样可以吗？
					Object[] objss = {"aaa", 1, 2.5};//成立
				}

				/**
				 * 测试在java语言中,数组是不是对象
				 * 如果是对象, 那么他的类型是什么?
				 * /
				private static void test1() {
					int[] a = new int[4];
					//a.length;  //对属性的引用不能当成语句
					int len = a.length;  //数组中保存一个字段, 表示数组的长度

					//以下方法说明数组可以调用方法,java中的数组是对象.这些方法是Object中的方法,所以可以肯定,数组的最顶层父类也是Object
					a.clone();
					a.toString();


					/**
					 * java是强类型的语言,一个对象总会有一个特定的类型,例如 Person p = new Person();
					 * 对象p(确切的说是引用)的类型是Person类, 这个Person类是我们自己编写的
					 * 那么数组的类型是什么呢? 下面使用反射的方式进行验证
					 * /
					int[] a1 = {1, 2, 3, 4};
					System.out.println(a1.getClass().getName());
					//打印出的数组类的名字为[I

					String[] s = new String[2];
					System.out.println(s.getClass().getName());
					//打印出的数组类的名字为  [Ljava.lang.String;

					String[][] ss = new String[2][3];
					System.out.println(ss.getClass().getName());
					//打印出的数组类的名字为    [[Ljava.lang.String;

					/**
					 * 所以,数组也是有类型的,只不过这个类型不是有程序员自己定义的类, 也不是jdk里面
					 * 的类, 而是虚拟机在运行时专门创建的类
					 * 类型的命名规则是:
					 * 		每一维度用一个[表示;
					 * 		[后面是数组中元素的类型(包括基本数据类型和引用数据类型)
					 *
					 * 在java语言层面上,s是数组,也是一个对象,那么他的类型应该是String[],
					 * 但是在JVM中,他的类型为[java.lang.String
					 *
					 * 顺便说一句普通的类在JVM里的类型为 包名+类名, 也就是全限定名
					 * /
				}				
				public static class Father {
				}				
				public static class Son extends Father {
				}
			}
## 4.Java 数组初始化:
	(1).Java 数组是静态的,即当数组被初始化之后,该数组的长度是不可变的;
## 5.数组扩容:可以参照利用 List 集合中的add方法模拟实现:
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
## 6.数组复制问题:
	所以通过 Arrays.copyOf() 方法产生的数组是一个浅拷贝。同时数组的 clone() 方法也是,集合的 clone() 方法也是,
	所以我们在使用拷贝方法的同时一定要注意浅拷贝这问题
## 7.数组转换为 List:
    asList 返回的是一个长度不可变的列表。数组是多长,转换成的列表就是多长,
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
    	(1).注意这个参数:T…a,这个参数是一个泛型的变长参数,我们知道基本数据类型是不可能泛型化的,也是就说 8 个基本数据类
    	型是不可作为泛型参数的,但是为什么编译器没有报错呢？这是因为在 Java 中,数组会当做一个对象来处理,它是可以泛型的,
    	所以我们的程序是把一个 int 型的数组作为了 T 的类型,所以在转换之后 List 中就只会存在一个类型为 int 数组的元素了;
    	(2).这里是直接返回一个 ArrayList 对象返回,但是注意这个 ArrayList 并不是 java.util.ArrayList,
    		而是 Arrays 工具类的一个内之类,这个内部类并没有提供 add() 方法,那么查看父类 AbstractList
    		仅仅只是提供了方法,方法的具体实现却没有,所以具体的实现需要子类自己来提供,
    		但是非常遗憾这个内部类 ArrayList 并没有提供 add 的实现方法
    	size：元素数量、toArray：转换为数组,实现了数组的浅拷贝、get：获得指定元素、contains：是否包含某元素
# 十三.switch:
## 1.支持类型：
    JDK7 之后,switch 的参数可以是 String 类型了;到目前为止 switch 支持的数据类型:byte(Byte)、short(Short)、int(Integer)、char(Character)、String、枚举类型
## 2.switch 对整型的支持:
    switch 对 int 的判断是直接比较整数的值
	由于 byte 的存储范围小于 int,可以向 int 类型进行隐式转换,所以 switch 可以作用在 byte 类型变量上;
	由于 long 的存储范围大于 int,不能向 int 进行隐式转换,只能强制转换,所以 switch 不可以作用在 long 类型变量上.
	对于包装类型,其使用的时候都是通过 byteValue,shortValue等来转换为基本类型
## 3.switch 对字符型支持的实现:
	对 char 类型进行比较的时候,实际上比较的是 Ascii 码,编译器会把 char 型变量转换成对应的 int 型变量.
	对于包装类型Character,其需要转换为基本类型char,转换方法:charValue
## 4.switch 对字符串支持的实现c
	4.1.代码片段1:
		public class switchDemoString {
		    public static void main(String[] args) {
		        String str = "world";
		        switch (str) {
		        case "hello":
		            System.out.println("hello");
		            break;
		        case "world":
		            System.out.println("world");
		            break;
		        default:
		            break;
		        }
		    }
		}
	4.2.反编译上述代码:
		public class switchDemoString{
		    public switchDemoString(){}
		    public static void main(String args[]){
		        String str = "world";
		        String s;
		        switch((s = str).hashCode()){
		        default:
		            break;
		        case 99162322:
		            if(s.equals("hello"))
		                System.out.println("hello");
		            break;
		        case 113318802:
		            if(s.equals("world"))
		                System.out.println("world");
		            break;
		        }
		    }
		}
	4.3.分析:字符串的 switch 是通过equals()和hashCode()方法来实现的
		(1).switch 中只能使用整型,hashCode()方法返回的是int,而不是long
		(2).进行 switch 的实际是哈希值,然后通过使用equals方法比较进行安全检查,这个检查是必要的,因为哈希可能会发生碰撞
		(3).其实 switch 只支持一种数据类型,那就是整型,其他数据类型都是转换成整型之后在使用 switch 的
## 5.枚举类:
	枚举类型之所以能够使用,因为编译器层面实现了,编译器会将枚举 switch 转换为类似 switch(s.ordinal()) { case Status.START.ordinal() } 形式,
	所以实质还是 int 参数类型.
	可以通过查看反编译字节码来查看
## 十四.抽象类与接口:
    抽象类与接口是 Java 语言中对抽象概念进行定义的两种机制
 * http://blog.csdn.net/chenssy/article/details/12858267
 * http://www.cnblogs.com/dolphin0520/p/3811437.html

## 1.抽象类:
    如果一个类含有一个被 abstract 修饰的方法,那么该类就是抽象类,抽象类必须在类前用 abstract 关键字修饰
	1.1.相关概念:
		抽象类体现了数据抽象的思想,是实现多态的一种机制;
		抽象类的存在就是为了继承的,所以说抽象类就是用来继承的;
	1.2.注意点:
		(1).抽象类不能被实例化,实例化的工作应该交由它的子类来完成,它只需要有一个引用即可;
		(2).抽象方法必须为 public 或者 protected(因为如果为 private 子类便无法实现该方法),缺省情况下默认为 public;
		(3).抽象类中可以包含具体的方法,当然也可以不包含抽象方法;
		(4).子类继承父类必须实现所有抽象方法,如果不实现需要将子类也定义为抽象类;
		(5).抽象类可以实现接口( implements ),可以不实现接口方法;
		(6).abstract 不能与 final 并列修饰同一个类(因为被 final 修饰的类不能被继承);
		(7).abstract 不能与 private、static、final 或 native 并列修饰同一个方法;
			private 修饰的方法不能被子类所见,所以也就不能被子类所重写;
			final 与类类似,final 修饰的方法不能被重写;
			static 修饰的方法是类的方法,而抽象方法还没被实现;
			native 是本地方法,不是由 Java 来实现的,
		(8).abstract 类中定义的抽象方法必须在具体的子类中实现,所以不能有抽象构造方法或抽象静态方法;
## 2.接口:
    接口本身就不是类,在软件工程中,接口泛指供别人调用的方法或者函数,类使用 interface Demo{} 修饰
	2.1.接口是用来建立类与类之间的协议,它所提供的只是一种形式,而没有具体的实现;
		实现该接口的实现类必须要实现该接口的所有方法,通过使用 implements 关键字
	2.2.接口是抽象类的延伸,Java 中是不能多继承的,子类只能有一个父类;但是接口不同,一个类可以实现多个接口,接口之间可以
		没有任何联系;
	2.3.接口使用注意事项:
		(1).接口之间也可以继承,但只能是接口继承接口,接口也不能实现接口;抽象类不能继承接口,只能是使用实现;
			接口之间可以是继承关系,类(抽象类)与接口是实现关系;
		(2).接口中的所有方法默认都是 public abstract 修饰的;接口中不能有静态代码块和静态方法;		
		(3).接口中可以定义"成员变量",该成员变量会自动被 public static final 修饰,且必须赋值,访问直接使用接口名.变量名称;
		(4).接口中不存在已经实现的方法,所有方法都是抽象的;实现接口的非抽象类必须实现接口所有的方法.抽象类可以不用实现;
		(5).接口不能直接实例化,但可以声明接口变量引用指向接口的实现类对象.使用 instanceof 检查一个对象实现了某个特点接口;
		???(6).如果一个类中实现了两个接口,且两个接口有同名方法,那么默认情况下实现的是第一个接口的方法;
	2.4.为什么有些接口是个空接口,没有定义任何方法和变量?
		Cloneable,Serializable 这一类接口表示某个标志,实现 Cloneable 表示该类可以被克隆,
		实现 Serializable 表示该类可以被序列化;
	2.5.针对接口的默认修饰符看如下代码:
		(1).接口定义如下:
			public interface A{
				String name; // 编译错误:The blank final field name may not have been initialized,即需要初始化
				// Illegal modifier for the interface method add; only public & abstract are permitted
				// 非法字符,只有public 和 abstract 被允许使用
				protected int add(int a, int b);
			}
		(2).再看如下定义,编译如下代码,查看其字节码:
			public interface A{
				String name = "Hello";
				int add(int a, int b);
			}
			如下,编译器默认都给加上了修饰符(javap -verbose A.class)
			{
			  public static final java.lang.String name;
			    descriptor: Ljava/lang/String;
			    flags: ACC_PUBLIC, ACC_STATIC, ACC_FINAL
			    ConstantValue: String Hello
			  public abstract int add(int, int);
			    descriptor: (II)I
			    flags: ACC_PUBLIC, ACC_ABSTRACT
			}
## 3.接口与抽象类的区别:
	3.1.语法层面上:
		(1).抽象类可以提供成员方法的实现细节,而接口中只能存在 public abstract 方法；
		(2).抽象类中的成员变量可以是各种类型的,而接口中的成员变量只能是 public static final 类型的
		(3).JDK1.7以前的版本接口中不能含有静态方法.JDK1.8之后可以有实现的静态方法,但是不能有未实现的静态方法.
			接口中不能存在的是静态代码块.且如果接口中包含了静态方法,则接口无法不能重写该静态方法.
			而抽象类可以有静态代码块和静态方法;
		(4).接口和抽象类不能实例化,接口中不能有构造,抽象类可以有构造方法;
		(5).一个类只能继承一个抽象类,而一个类却可以实现多个接口;
		(6).接口和抽象类都可以包含内部类(抽象类)或者内部接口
	3.2.设计层面上:
		(1).抽象层次不同:抽象类是对类抽象,而接口是对行为的抽象,
			抽象类是对整个类整体进行抽象,包括属性、行为,但是接口却是对类局部(行为)进行抽象;
			抽象是:is-a 的关系
			接口是:like-a 的关系
		(2).跨域不同:抽象类所跨域的是具有相似特点的类,而接口却可以跨域不同的类
			抽象类所体现的是一种继承关系,要想使得继承关系合理,父类和派生类之间必须存在"is-a" 关系,即父类和派生类在概念
			本质上应该是相同的.对于接口则不然,接口是"like-a ",并不要求接口的实现者和接口定义在概念本质上是一致的
		(3).设计层次不同:抽象类是自下而上抽象出来的,需要先知道子类才能抽象出父类;
			接口则是自顶向下的设计,接口根本不需要知道子类的存在,其只需要定义一个规则即可;
//http://stackoverflow.com/questions/19722847/static-initialization-in-interface
## 4.Java8 下接口的不同之处(上述是针对 JDK7 之前的)
	4.1.在 Java8 中,使用默认方法和静态方法来扩展接口,类似如下代码:
		使用 default 关键字来实现默认方法
		public interface Demo {
			default int add(int a, int b){
				return a + b;
			}
			static int sub(int a, int b){
				return a - b;
			}
		}
		接口中非 default 和 static 的方法不能有方法体;
	4.2.如果实现一个接口,默认方法可以不用覆盖重写实现,实现类默认可以直接调用该默认方法;实现类无法重写接口中的静态方法;
		注意:在声明一个默认方法前,请仔细思考是不是真的有必要使用默认方法,因为默认方法会带给程序歧义,并且在复杂的继承体系中容易产生编译错误
		http://docs.oracle.com/javase/tutorial/java/IandI/defaultmethods.html
		(1).如果一个类实现两个接口,两个接口中有同样签名的 default 方法,编译报错:
			public interface DefaultMethodDemo1 {
			    default void add(){}
			}
			public interface DefaultMethodDemo2 {
			    default void add(){}
			}
			public class SubClassDemo implements DefaultMethodDemo1, DefaultMethodDemo2 {}
			==> 编译错误:SubClassDemo inherited unrelated defaults for add() from type DefaultMethodDemo1 and DefaultMethodDemo2
			因为相当于你在类里面定义了两个同样签名的方法
		(2).如果一个类继承一个抽象类和实现一个接口,抽象类定义了一个和接口的默认方法相同的抽象方法,则在类中需要实现该方法.
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
		(3).抽象类、接口存在同样的签名方法,抽象类有实现体但是不是 public 修饰的,编译报错.
			但如果子类实现对应的方法,则编译通过
		(4).一个声明在类里面的方法优先于任何默认方法,优先选取最具体的实现;
			public interface A {
			    default void hello(){System.out.println("Interface A hello : A");}
			}
			public interface B extends A {
			    default void hello(){System.out.println("Interface B hello : B");}
			}
			public class C implements A, B {
			    public static void main(String[] args) {new C().hello();}
			}
			输出结果:Interface B hello : B
	4.3.接口的 default 方法不能重写 Object 的方法,但是可以对 Object 类的方法进行重载.
		因为若可以会很难确定什么时候该调用接口默认的方法

# 十五.基本类型与引用类型
## 1.基本类型与引用类型的比较
	1.1.如下四个变量,哪两个比较为 false
		Integer i01 = 59;
		int i02 = 59;
		Integer i03 =Integer.valueOf(59);
		Integer i04 = new Integer(59);
		(1).Integer 为了节省空间和内存会在内存中缓存 -128~127 之间的数字;
		(2).valueOf():调用该方法时,内部实现作了个判断,判断当前传入的值是否在 -128~127 之间且 IntergCache 是否已存在该对象
			如果存在,则直接返回引用,如果不存在,则创建一个新对象
		(3).基本类型存在内存的栈中,与引用类型比较时, 引用类型会自动装箱,比较数值而不比较内存地址;
	1.2.自动装箱拆箱机制是编译特性还是虚拟机运行时特性？分别是怎么实现的？
		(1).自动装箱机制是编译时自动完成替换的.装箱阶段自动替换为了 valueOf 方法,拆箱阶段自动替换为了 xxxValue 方法;
		(2).对于 Integer 类型的 valueOf 方法参数如果是 -128~127 之间的值会直接返回内部缓存池中已经存在对象的引用,参数是其他范围值则返回新建对象;
		(3).而 Double 类型与 Integer 类型类似,一样会调用 Double 的 valueOf 方法,但是 Double 的区别在于不管传入的参数值是多少都会 new 一个对象来表
			达该数值(因为在指定范围内浮点型数据个数是不确定的,整型等个数是确定的,所以可以 Cache)
		==> 注意:Integer、Short、Byte、Character、Long 的 valueOf 方法实现类似,而 Double 和 Float 比较特殊,每次返回新包装对象,
			对于两边都是包装类型的:== 比较的是引用,	equals 比较的是值;
			对于两边有一边是表达式(包含算数运算): == 比较的是数值(自动触发拆箱过程),对于包装类型 equals 方法不会进行类型转换;
	1.3.Integer i = 1; i += 1; 做了哪些操作:
		(1).Integer i = 1; 做了自动装箱:使用 valueOf() 方法将 int 装箱为 Integer 类型
		(2).i += 1; 先将 Integer 类型的 i 自动拆箱成 int(使用 intValue() 方法将 Integer 拆箱为 int),完成加法运行之后的 i 再装箱成 Integer 类型
## 2.关于 String+ 和 StringBuffer 的比较:
    在 String+写成一个表达式的时候(更准确的说,是写成一个赋值语句的时候)
	效率其实比 Stringbuffer更快
	public class Main{	    
	    public static void main(String[] args){
	        /*   1   * /
	        String string = "a" + "b" + "c";
	        /*   2   * /
	        StringBuffer stringBuffer = new StringBuffer();
	        stringBuffer.append("a").append("b").append("c");
	        string = stringBuffer.toString();
	    }	    
	}
	2.1.String+的写法要比 Stringbuffer 快,是因为在编译这段程序的时候,编译器会进行常量优化,
	它会将a、b、c直接合成一个常量abc保存在对应的 class 文件当中{},看如下反编译的代码:
		public class Main{}
			public static void main(String[] args){
			    String string = "abc";
			    StringBuffer stringBuffer = new StringBuffer();
			    stringBuffer.append("a").append("b").append("c");
			    string = stringBuffer.toString();
			}
		}
	原因是因为 String+其实是由 Stringbuilder 完成的,而一般情况下 Stringbuilder 要快于 Stringbuffer,
	这是因为 Stringbuilder 线程不安全,少了很多线程锁的时间开销,因此这里依然是 string+的写法速度更快;
			/*   1   * /
	        String a = "a";
	        String b = "b";
	        String c = "c";
	        String string = a + b + c;
	        /*   2   * /
	        StringBuffer stringBuffer = new StringBuffer();
	        stringBuffer.append(a);
	        stringBuffer.append(b);
	        stringBuffer.append(c);
	        string = stringBuffer.toString();
	2.2.字符串拼接方式:	+、concat() 以及 append() 方法,  append()速度最快,concat()次之,+最慢
		(1).编译器对+进行了优化,它是使用 StringBuilder 的 append() 方法来进行处理的,
		编译器使用 append() 方法追加后要同 toString() 转换成 String 字符串,变慢的关键原因就在于 new StringBuilder() 和
		toString(),这里可是创建了 10 W 个 StringBuilder 对象,而且每次还需要将其转换成 String
		(2).concat:
			concat() 的源码,它看上去就是一个数字拷贝形式,我们知道数组的处理速度是非常快的,但是由于该方法最后是这样的：
			return new String(0, count + otherLen, buf);这同样也创建了 10 W 个字符串对象,这是它变慢的根本原因
		(3).append() 方法拼接字符串:并没有产生新的字符串对象
## 3.静态代码块、静态,其作用级别为类; 构造代码块、构造函数、构造,其作用级别为对象
	(1)、静态代码块,它是随着类的加载而被执行,只要类被加载了就会执行,而且只会加载一次,主要用于给类进行初始化。
	(2)、构造代码块,每创建一个对象时就会执行一次,且优先于构造函数,主要用于初始化不同对象共性的初始化内容和初始化实例环境。
	(3)、构造函数,每创建一个对象时就会执行一次.同时构造函数是给特定对象进行初始化,而构造代码是给所有对象进行初始化,
		作用区域不同.
	==> 通过上面的分析,他们三者的执行顺序应该为：
		静态代码块 > 构造代码块 > 构造函数。
	3.1.Java 类初始化过程:
		(1).首先,初始化父类中的静态成员变量和静态代码块,按照在程序中出现的顺序初始化；
		(2).然后,初始化子类中的静态成员变量和静态代码块,按照在程序中出现的顺序初始化；
		(3).其次,初始化父类的普通成员变量和代码块,在执行父类的构造方法；
		(4).最后,初始化子类的普通成员变量和代码块,在执行子类的构造方法；
	3.2.不要在构造器里调用可能被重载的虚方法,
		父类构造器执行的时候,调用了子类的重载方法,然而子类的类字段还在刚初始化的阶段,刚完成内存布局:
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
	3.1.Java 类初始化过程:
		(1).首先,初始化父类中的静态成员变量和静态代码块,按照在程序中出现的顺序初始化；
		(2).然后,初始化子类中的静态成员变量和静态代码块,按照在程序中出现的顺序初始化；
		(3).其次,初始化父类的普通成员变量和代码块,在执行父类的构造方法；
		(4).最后,初始化子类的普通成员变量和代码块,在执行子类的构造方法；
		Java 中赋值顺序：
			(1).父类的静态变量赋值
			(2).自身的静态变量赋值
			(3).父类成员变量赋值
			(4).父类块赋值
			(5).父类构造函数赋值
			(6).自身成员变量赋值
			(7).自身块赋值
			(8).自身构造函数赋值
	3.2.Java 代码执行顺序:
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
	==> 执行结果:22,34,17
		(1).子类 B 中重写了父类 A 中的setValue方法:
				super(5) // 调用了父类构造器,其中构造函数里面的setValue(value),调用的是子类的setValue方法
			finally块中的
				this.setValue(value) //调用的也是子类的setValue方法
			而子类setValue方法中的
				super.setValue(2*value); //调用的是父类A的setValue方法
		(2).try...catch...finally块中有return返回值的情况
			finally 块中虽然改变了value的值,但 try 块中返回的应该是 return 之前存储的值.
	==> 父类执行时如果有子类的方法重写了父类的方法,调用的子类的重写方法
## 4.给出一个表达式计算其可以按多少进制计算:
	(1).式子7*15=133成立,则用的是几进制?
		可以通过解方程来解决,上述式子可以转换为方程:
		7 * (1 * x + 5) = 1 * x^2 + 3 * x + 3
		==> x^2 -4x - 32 = 0
		==> x = -4 或 x = 8
	(2).如果下列的公式成立：78+78=123,则采用的是_______进制表示的
		7 * x + 8 + 7 * x + 8 = 1 * x^2 + 2 * x + 3
		==>
		x^2 - 12 * x - 13 = 0
		==> x = -1, x = 13
## 5.表达式的数据类型:
	(1).所有的 byte,short,char 型的值将被提升为 int 型；
	(2).如果有一个操作数是 long 型,计算结果是 long 型；
	(3).如果有一个操作数是 float 型,计算结果是 float 型；
	(4).如果有一个操作数是 double 型,计算结果是 double 型；
	(5).final 修饰的变量是常量,如果运算时直接是已常量值进行计算,没有final修饰的变量相加后会被自动提升为int型
		byte b1=1,b2=2,b3,b6;
		final byte b4=4,b5=6;
		b6=b4+b5;// b4, b5是常量,则在计算时直接按原值计算,不提升为int型
		b3=(b1+b2);// 编译错误
		System.out.println(b3+b6);
## 6.多态问题:
    当超类对象引用变量引用子类对象时,被引用对象的类型而不是引用变量的类型决定了调用谁的成员方法,
	但是这个被调用的方法必须是在超类中定义过的,也就是说被子类覆盖的方法
	* http://blog.csdn.net/clqyhy/article/details/78978785
	* http://blog.csdn.net/simon901/article/details/25078397
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
	(1).多态是对象在不同时刻表现出来的多种状态,是一种编译时期状态和运行时期状态不一致的现象.
	(2).在编写或者分析代码时记住如下口诀:
		成员变量:编译看左,运行看左(因为无法重写);
		成员方法:编译看左,运行看右(因为普通成员方法可以重写,变量不可以);
		静态方法:编译看左,运行看左(因为属于类);
	    ==> 当父类变量引用子类对象时 Base base = new Child();在这个引用变量 base 指向的对象中他的成员变量和静态方法与父类是一致的,
	    	他的非静态方法在编译时是与父类一致的,运行时却与子类一致(发生了复写);

# 十六.反射与注解
## 1.Java 注解:Annotation(JDK5.0+)
	1.1.内置注解,如:
		@Override:重写
		@Deprecated:过时
		@SuppressWarnings:取消警告
		(1).注解分为:
			按运行机制:源码注解,编译时注解,运行时注解
			按照来源注解:JDK,第三方,自定义注解;
	1.2.自定义注解:使用 @interface 自定义注解时,自动集成了 Annotation 接口,要点如下:
		(1).@interface 用来声明一个注解: public @interface 注解名{};
		(2).其中的每一个方法实际上是声明了一个配置参数:
			①.方法的名称就是参数的名称,无参无异常;
			②.返回值类型就是参数的类型(返回值只能是基本类型,Class, String, Enum);
			③.可以通过 default 来声明参数的默认值
			④.如果只有一个参数成员,一般参数名为:value;
			⑤.没有成员的注解是标识注解;
		// 注意:注解元素必须要有值,在定义注解元素时经常使用空字符串,0作为默认值

	1.3.元注解:负责注解其他注解, Java 定义了四个标准的 meta-annotation 类型,用来提供对其他 annotation 类型作说明
		(1).@Target
			用于描述注解的使用范围:@Target(value= ElementType.TYPE)
			包 -----> PACKAGE
			类.接口.枚举.Annotation类型 -----> TYPE
			方法参数 -----> PARAMETER
			局部变量 -----> LOCAL VARIABLE
		(2).@Retention
			表示需要在什么级别保存该注释信息,用于描述注解的生命周期:(RetentionPolicy)
			SOURCE --- 在源文件有效(即源文件保留)
			CLASS --- 在class文件中有效
			RUNTIME --- 在运行时有效(可被反射读取)
		(3).@Documented:生成文档的时候会生成注解的注释
		(4).@Inherited:允许子类继承
	1.4.解析注解:通过反射获取类、函数或成员上的运行时注解信息,从而实现动态控制程序运行的逻辑;
	1.5.注解处理器:
		注解处理器是一个在javac中的,用来编译时扫描和处理的注解的工具;
		一个注解的注解处理器,以Java代码(或者编译过的字节码)作为输入,生成文件(通常是.java文件)作为输出。
		这具体的含义什么呢？你可以生成Java代码！这些生成的Java代码是在生成的.java文件中,所以你不能修改已经存在的Java类,
		例如向已有的类中添加方法。这些生成的Java文件,会同其他普通的手动编写的Java源代码一样被javac编译;
		(1).虚处理器 AbstractProcessor:
## 2.Java 动态加载与静态加载:
	(1).编译时加载类是静态加载类:new 创建对象是静态加载类,在编译时刻时需要加载所有的可能使用到的类;
	(2).运行时刻加载类是动态加载(Class.forName(""));
## 3.反射机制:(Reflection)运行时加载,探知使用编译期间完全未知的类;
	反射:将一个Java类中的所有信息映射成相应的Java类;
	【一个类只有一个Class对象,这个对象包含了完整类的结构】
	(1).Class 类[java.lang.Class]:反射的根源,各种类型----表示Java中的同一类事物
		(1.1)Class 类获取: .class, getClass, Class.forName(String className);
		Field : 属性相关类,获取所有属性(包括 private),getDeclaredFields();
		Method : 方法相关类,getDeclaredMethods(); method.invoke()方法执行时,如果第一个参数为 null,则表示反射的方法为静态方法
		Constructor : 构造器相关类,getDeclaredConstructors();
		如果需要访问私有的,则需setAccessible(true;
	(2).反射机制性能问题：反射会降低程序的效率
		如果在开发中确实需要使用到反射,可以将setAccessible设为 true :即取消Java语言访问检查;
	(3).反射操作泛型:
		(①).Java 采用泛型擦除的机制来引入泛型.Java中的泛型仅仅是给编译器使用的,确保数据的安全性和免去强制类型转换的麻烦;
			但是一旦编译完成,所有和泛型有关的类型全部擦除;
		(②).为了通过反射操作泛型,Java有 ParameterizedType,GenericArrayType,TypeVariable,WildcardType 几种类型来代表不能
			被归一到Class类中的类型但是又和原始类型齐名的类型;
			Type 是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型
			ParameterizedType : 参数化类型
			GenericArrayType : 元素类型是参数化类型或者类型变量的数组类型
			TypeVariable : 各种类型变量的公共父接口
			WildcardType : 表示一种通配符类型表达式;
	(4).反射操作注解:
		getAnnotation(Class<A> annotationClass);
		getAnnotations();
	(5).反射操作:
		(①).使用反射调用类的main方法：
			Method method = Demo.class.getMethod("main",String[].class);
			method.invoke(null, (Object)new String[]{"111","222","333"});
		★★注意:传入参数时不能直接传一个数组,jdk为了兼容1.5版本以下的,会将其拆包;
		因此这里将其强转或者直接将String数组放入Object数组也可以
		(②).数组的反射:
			◆一个问题:
			int[] a1 = new int[]{1,2,3};
			String[] a2 = new String[]{"a","b","c"};
			System.out.println(Arrays.asList(a1)); // 输出: [[I@24c98b07]
			System.out.println(Arrays.asList(a2)); // 输出:[a, b, c]
			原因:
				在jdk1.4:asList(Object[] a);
				在jdk1.5:asList(T... a);
				int数组在编译运行时不会被认为为一个Object数组,因此其将按照一个数组对象来处理;
			◆基本类型的一维数组可以被当作Object类型处理,不能被当作Object[]类型使用;
				非基本类型的一维数组既可以当作Object类型使用,也可以当作Object[]类型使用;
			◆Array 工具类可完成数组的反射操作;
	(6).反射的应用:实现框架功能
		使用类加载器加载文件
## 4.动态编译:Java6.0引入动态编译
	4.1.动态编译的两种方法：
		(1).通过Runtime调用javac,启动新的进程去操作;
		(2).通过Javacompiler动态编译;
	4.2.Javacompiler 动态编译:
		JavaCompiler compile = ToolProvider.getSystemJavaCompiler();
		int result = compile.run(null, null, null, "F:/class/HelloWorld.java");
        ```
		 /*
		  * run(InputStream in, OutputStream out, OutputStream err, String... arguments);
		  * 第一个参数: 为Java编译器提供参数;
		  * 第二个参数: 得到Java编译器输出的信息;
		  * 第三个参数: 接受编译器的错误信息;
		  * 第四个参数: 可变参数,能传入一个或多个Java源文件
		  * 返回值: 0 表示编译成功, 1 表示编译失败;
		  */
          ```
	4.3.动态运行动态编译的Java类		
## 5.动态执行Javascript(JDK6.0以上)
	5.1.脚本引擎:Java 应该程序可以通过一套固定的接口与脚本引擎交互,从而可以在Java平台上调用各种脚本语言;
	5.2.js接口:Rhino 引擎,使用Java语言的javascript开源实现;		
## 6.Java 字节码操作
	6.1.Java  动态操作:字节码操作,反射
		字节码操作:动态生存新的类;
		优势:比反射开销小,性能高;
	6.2.常见的字节码操作类库:
		(1).BCEL:apache
		(2).ASM:轻量级的字节码操作框架,涉及到jvm底层的操作和指令
		(3).CGLIB:基于asm实现
		(4).javaasist:性能比较差,使用简单
	6.3.Javasist:
## 7.反射存在问题:
	7.1.反射慢的原因:
		(1).编译器不能对代码对优化.
		(2).所有的反射操作都需要类似查表的操作,参数需要封装,解封装,异常也重新封装,rethrow等等
	7.2.优化方式:
		(1).灵活运用API,如果只是寻找某个方法,不要使用 getMethods() 后在遍历筛选,而是直接用
			getMethod(methodName)来根据方法名获取方法;
		(2).使用缓存:需要多次动态创建一个类的实例的时候.
		(3).使用代码动态生成技术,通过调用代理类的方式来模拟反射

# 十七.比较器:Comparale and Comparator
## 1.区别:
	(1).Comparable & Comparator 都是用来实现集合中元素的比较、排序的:
		Comparable 是在集合内部定义的方法实现的排序;
		Comparator 是在集合外部实现的排序
		所以,如想实现排序,就需要在集合外定义 Comparator 接口的方法或在集合内实现 Comparable 接口的方法
	(2).Comparator 位于包 java.util下,而 Comparable 位于包 java.lang下
	(3).Comparable 是一个对象本身就已经支持自比较所需要实现的接口(如 String、Integer 自己就可以完成比较大小操作,已经实现了Comparable接口);
	(4).Comparator 是一个专用的比较器,当这个对象不支持自比较或者自比较函数不能满足你的要求时,你可以写一个比较器来完成两个对象之间大小的比较;
	(5).可以说一个是自已完成比较,一个是外部程序实现比较的差别而已
	(6).用 Comparator 是策略模式(strategy design pattern),就是不改变对象自身;
		Comparable 而用一个策略对象(strategy object)来改变它的行为
	(7).有时在实现 Comparator 接口时,并没有实现equals方法,可程序并没有报错.原因是实现该接口的类也是Object类的子类,
		而Object类已经实现了equals方法
## 2.Comparable:
	一个实现了 Comparable 接口的类,可以让其自身的对象和其他对象进行比较。
	也就是说:同一个类的两个对象之间要想比较,对应的类就要实现 Comparable 接口,并实现compareTo()方法
## 3.Comparator:
    在一些情况下,你不希望修改一个原有的类,但是你还想让他可以比较,Comparator接口可以实现这样的功能。
	(1).通过使用Comparator接口,你可以针对其中特定的属性/字段来进行比较。比如,当我们要比较两个人的时候,
	我可能通过年龄比较、也可能通过身高比较。这种情况使用Comparable就无法实现(因为要实现Comparable接口,
	其中的compareTo方法只能有一个,无法实现多种比较)
	(2).通过实现Comparator接口同样要重写一个方法：compare()。接下来的例子就通过这种方式来比较HDTV的大小。
	其实Comparator通常用于排序。Java中的Collections和Arrays中都包含排序的sort方法,该方法可以接收一个Comparator
	的实例(比较器)来进行排序:
			class HDTV {
			    private int size;
			    private String brand;
			    public HDTV(int size, String brand) {
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
			    public int compare(HDTV tv1, HDTV tv2) {
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
			        HDTV tv1 = new HDTV(55, "Samsung");
			        HDTV tv2 = new HDTV(60, "Sony");
			        HDTV tv3 = new HDTV(42, "Panasonic");
			        ArrayList<HDTV> al = new ArrayList<HDTV>();
			        al.add(tv1);
			        al.add(tv2);
			        al.add(tv3);
			        Collections.sort(al, new SizeComparator());
			        for (HDTV a : al) {
			            System.out.println(a.getBrand());
			        }
			    }
			}
	(3).经常会使用 Collections.reverseOrder()来获取一个倒序的 Comparator:
		ArrayList<Integer> al = new ArrayList<Integer>();
		al.add(3);
		al.add(1);
		al.add(2);
		System.out.println(al);
		Collections.sort(al);
		System.out.println(al);
		Comparator<Integer> comparator = Collections.reverseOrder();
		Collections.sort(al,comparator);
		System.out.println(al);
## 4.如何选择:
	(1).一个类如果实现 Comparable 接口,那么他就具有了可比较性,意思就是说它的实例之间相互直接可以进行比较
	(2).通常在两种情况下会定义一个实现 Comparator 类
		可以把一个Comparator的子类传递给Collections.sort()、Arrays.sort()等方法,用于自定义排序规则。
		用于初始化特定的数据结构。常见的有可排序的Set(TreeSet)和可排序的Map(TreeMap);

# 十八.枚举类:
## 1.枚举类概念:
    枚举类是JDK1.5之后出现的,允许用常量来表示特定的数据片断,而且全部都以类型安全的形式来表示
	1.1.枚举类特点
		1).枚举类是一种特殊的Java类;
		2).枚举类中声明的每一个枚举值代表枚举类的一个实例对象;
		3).与java普通类一样,在声明枚举类时可以声明属性,方法,构造方法,但是枚举类必须是私有的
		4).枚举可以实现接口或继承抽象方法
		5).在JDK5之后,switch语句,可以接受int,byte,char,short外,还可以接受枚举类型
		6).若枚举类只有一个枚举值,则可以当作单例设计模式
	1.2.枚举类的一些方法
		values():获得所有的枚举类
		valueOf(String str):将一个字符串转为枚举类;
	1.3.枚举类基类:
		public abstract class Enum<E extends Enum<E>> implements Comparable<E>, Serializable{}
		定义枚举类:
		public enum Status{
			START(),
			STOP(),
			RUNNING();
		}
		==> 除了 toString 方法,其余方法都不可重写.要么是 final 方法要么是私有方法.
	1.4.Java 枚举类比较使用 == 或者 equals()都一样,因为枚举类 Enum 的 equals()方法的默认实现是通过 == 来比较的.
		在 Enum 中 equals 和 hashCode 方法都是 final, 所以在枚举类中不可实现这两个方法.
		类似的 Enum 的 compareTo 方法比较的是 Enum 的 ordinal 顺序大小;
		类似的还有 Enum 的 name 方法和 toString 方法一样都返回的是 Enum 的 name 值.

## 2.枚举类本质:
	枚举类本质是通过普通类来实现的,只是编译器进行了相应的处理,每个枚举类编译之后的字节码实质都是继承自 java.lang.Enum 的枚举类类型同名普通类.
	而每个枚举常量实质是一个枚举类型同名普通类的静态常量对象,所有枚举常量都通过静态代码块进行初始化实例赋值.
	==> 如下代码:
		public enum Status{
			START(),
			STOP(),
			RUNNING();
		}
		编译之后通过 javap -v 查看字节码文件:
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
	==> 所以从某种意义上可以说 JDK 1.5 后引入的枚举类型是上面枚举常量类的代码封装而已
## 3.枚举类与常量:
	3.1.区别:
		(1).枚举相对于常量类来说定义更简单,其不需要定义枚举值,而常量类中每个常量必须手动添加值.
		(2).枚举作为参数使用时可以避免在编译时避免弱类型错误,而常量类中的常量作为参数使用时无法避免类型错误.
		(3).枚举类自动具备内置方法,如 values() 方法可以获得所有值的集合遍历,ordinal 方法可以获得排序值,compareTo方法可以给予ordinal比较,
			而常量类不具备这些方法.
		(4).枚举的缺点是不能被继承(编译后生成的类是 final class{}),也不能通过 extends 继承其他类(枚举编译后实质是继承了 Enum 类,java是单继承的).
			但是定义的枚举类也通过 implements 实现其他接口.
		(5).枚举值定义完毕后除非重构,否则无法做扩展,而常量类可以随意继承.
	3.2.Java 枚举会比静态常量更消耗内存吗?// TODO 查看资料
		会更消耗,一般场景下不仅编译后的字节码会比静态常量多,而且运行时也会比静态常量需要更多的内存,不过这个多取决于场景和枚举的规模等等
## 4.枚举类是如何保证线程安全的:
	 Java 类加载与初始化是 JVM 保证线程安全,而 Java enum{} 枚举在编译器编译后的字节码实质是一个 final 类,每个枚举类型是这个 final 类中的
	 一个静态常量属性，其属性初始化是在该 final 类的 static 块中进行,而 static 的常量属性和代码块都是在类加载时初始化完成的,
	 所以自然就是 JVM 保证了并发安全
## 5.枚举与单例模式:
	(1).除枚举实现的单例模式以外的其他实现方式都有一个比较大的问题是一旦实现了 Serializable 接口后就不再是单例了,
		因为每次调用 readObject() 方法返回的都是一个新创建出来的对象(当然可以通过使用 readResolve() 方法来避免).
	(2).Java 规范中保证了每一个枚举类型及其定义的枚举变量在 JVM 中都是唯一的,在枚举类型的序列化和反序列化上 Java 做了特殊处理.
		序列化时 Java 仅仅是将枚举对象的 name 属性输出到结果中,反序列化时则是通过 java.lang.Enum 的 valueOf 方法来根据名字查找枚举对象
		同时禁用了 writeObject、readObject、readObjectNoData、writeReplace 和 readResolve 等方法
	(3).Java 枚举序列化需要注意的点:
		如果我们枚举被序列化本地持久化了,那我们就不能删除原来枚举类型中定义的任何枚举对象,否则程序在运行过程中反序列化时 JVM 就会找不到
		与某个名字对应的枚举对象了,所以我们要尽量避免多枚举对象序列化的使用
## 6.迭代器和枚举器区别:
	(1).Enumeration<E> 枚举器接口是1.0开始提供,适用于传统类,而 Iterator<E>迭代器接口是1.2提供,适用于 Collections
	(2).Enumeration 只有两个方法接口,我们只能读取集合的数据而不能对数据进行修改.
		而 Iterator 有三个方法接口,除了能读取集合的数据外也能对数据进行删除操作
	(3).Enumeration 不支持 fail-fast 机制，而 Iterator 支持 fail-fast 机制
		(一种错误检测机制，当多线程对集合进行结构上的改变的操作时就有可能会产生 fail-fast 机制,譬如 ConcurrentModificationException 异常)
	尽量使用 Iterator 迭代器而不是 Enumeration 枚举器
