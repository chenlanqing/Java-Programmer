目录:
	1.Java内部类
	2.HashMap vs. TreeMap vs. Hashtable vs. LinkedHashMap
一.Java内部类:
1.为什么使用内部类?
使用内部类最吸引人的原因是：每个内部类都能独立地继承一个（接口的）实现，所以无论外围类是否已经继承了某个（接口的）实现，
对于内部类都没有影响
1.1.使用内部类最大的优点就在于它能够非常好的解决多重继承的问题,使用内部类还能够为我们带来如下特性:
	(1)、内部类可以用多个实例，每个实例都有自己的状态信息，并且与其他外围对象的信息相互独。
	(2)、在单个外围类中，可以让多个内部类以不同的方式实现同一个接口，或者继承同一个类。
	(3)、创建内部类对象的时刻并不依赖于外围类对象的创建。
	(4)、内部类并没有令人迷惑的“is-a”关系，他就是一个独立的实体。
	(5)、内部类提供了更好的封装，除了该外围类，其他类都不能访问。
2.内部类分类:
(一).成员内部类:
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
	1.Inner 类定义在 Outer 类的内部，相当于 Outer 类的一个成员变量的位置，Inner 类可以使用任意访问控制符，
		如 public 、 protected 、 private 等
	2.Inner 类中定义的 show() 方法可以直接访问 Outer 类中的数据，而不受访问控制符的影响，
		如直接访问 Outer 类中的私有属性age
	3.定义了成员内部类后，必须使用外部类对象来创建内部类对象，而不能直接去 new 一个内部类对象，
	即：内部类 对象名 = 外部类对象.new 内部类( );
	4.编译上面的程序后，会发现产生了两个 .class 文件: Outer.class,Outer$Inner.class{}
	5.成员内部类中不能存在任何 static 的变量和方法,可以定义常量:
		(1).因为非静态内部类是要依赖于外部类的实例,而静态变量和方法是不依赖于对象的,仅与类相关,
		简而言之:在加载静态域时,根本没有外部类,所在在非静态内部类中不能定义静态域或方法,编译不通过;
		非静态内部类的作用域是实例级别
		(2).常量是在编译器就确定的,放到所谓的常量池了
	★★友情提示:
	1.外部类是不能直接使用内部类的成员和方法的，可先创建内部类的对象，然后通过内部类的对象来访问其成员变量和方法;
	2.如果外部类和内部类具有相同的成员变量或方法，内部类默认访问自己的成员变量或方法，如果要访问外部类的成员变量，
	可以使用 this 关键字,如:Outer.this.name
(二).静态内部类: 是 static 修饰的内部类,
	1.静态内部类不能直接访问外部类的非静态成员，但可以通过 new 外部类().成员 的方式访问 
	2.如果外部类的静态成员与内部类的成员名称相同，可通过“类名.静态成员”访问外部类的静态成员；
	如果外部类的静态成员与内部类的成员名称不相同，则可通过“成员名”直接调用外部类的静态成员
	3.创建静态内部类的对象时，不需要外部类的对象，可以直接创建 内部类 对象名 = new 内部类();
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
(三).方法内部类:访问仅限于方法内或者该作用域内	
	(1).局部内部类就像是方法里面的一个局部变量一样，是不能有 public、protected、private 以及 static 修饰符的
	(2).只能访问方法中定义的 final 类型的局部变量,因为:
		当方法被调用运行完毕之后，局部变量就已消亡了。但内部类对象可能还存在,
        直到没有被引用时才会消亡。此时就会出现一种情况，就是内部类要访问一个不存在的局部变量;
		==>使用final修饰符不仅会保持对象的引用不会改变,而且编译器还会持续维护这个对象在回调方法中的生命周期.
		局部内部类并不是直接调用方法传进来的参数，而是内部类将传进来的参数通过自己的构造器备份到了自己的内部，
		自己内部的方法调用的实际是自己的属性而不是外部类方法的参数;
		防止被篡改数据,而导致内部类得到的值不一致
		/*
			使用的形参为何要为 final???
		 在内部类中的属性和外部方法的参数两者从外表上看是同一个东西，但实际上却不是，所以他们两者是可以任意变化的，
		 也就是说在内部类中我对属性的改变并不会影响到外部的形参，而然这从程序员的角度来看这是不可行的，
		 毕竟站在程序的角度来看这两个根本就是同一个，如果内部类该变了，而外部方法的形参却没有改变这是难以理解
		 和不可接受的，所以为了保持参数的一致性，就规定使用 final 来避免形参的不改变
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
(四).匿名内部类:
	(1).匿名内部类是直接使用 new 来生成一个对象的引用;
	(2).对于匿名内部类的使用它是存在一个缺陷的，就是它仅能被使用一次，创建匿名内部类时它会立即创建一个该类的实例，
		该类的定义会立即消失，所以匿名内部类是不能够被重复使用;
	(3).使用匿名内部类时，我们必须是继承一个类或者实现一个接口，但是两者不可兼得，同时也只能继承一个类或者实现一个接口;
	(4).匿名内部类中是不能定义构造函数的,匿名内部类中不能存在任何的静态成员变量和静态方法;
	(5).匿名内部类中不能存在任何的静态成员变量和静态方法,匿名内部类不能是抽象的,它必须要实现继承的类或者实现的接口的所有抽象方法
	(6).匿名内部类初始化:使用构造代码块！利用构造代码块能够达到为匿名内部类创建一个构造器的效果
		public class OuterClass {
	        public InnerClass getInnerClass(final int   num,String str2){
	            return new InnerClass(){
	                int number = num + 3;
	                public int getNumber(){
	                    return number;
	                }
	            };        /* 注意：分号不能省 */
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
二.HashMap vs. TreeMap vs. Hashtable vs. LinkedHashMap
(1).HashMap,TreeMap,HashTable父接口都是Map,LinkedHashMap是HashMap的子类;
(2).HashMap:如果HashMap的key是自定义的对象,则需要重写equals()和hashcode()方法:
◆★原因是:HashMap 不允许两个相同的元素;默认情况下,在Object类下实现的equals()和hashcode()方法被使用,
默认的hashcode()方法给出不同的整数为不同的对象,并在equals()方法中,只有当两个引用指向的是同一个对象时才返回true
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
		
(3).TreeMap:是按照key来排序的,因此如果自定义对象作为key必须能够相互比较,因此其必须实现Comparable接口,
如我们使用String作为key,是因为String已经实现了Comparable接口,如例子:
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

(4).LinkedHashMap 与 HashMap 的不同区别是:LinkedHashMap 保留了插入顺序.

(5).HashMap,HashTable,TreeMap:
	A:迭代顺序:HashMap,HashTable不会保证元素的顺序,但是TreeMap是有序的;
	B:key-value空值:HashMap的key-value都可以为空(只有一个key为 null,因为不能存在两个相同的key),
		HashTable的key-value不允许为 null;
		TreeMap因为key是有序,因此key不能为 null,value可以为 null;

		
2.HashCode与HashSet关系:

3.Java 中几个的结构是什么?如何继承等?	
衍生问题:
	Java内存泄漏?为什么?

三.按照目录结构打印当前目录及子目录
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

四.Java 关键字的意义:
1.native:
	native 关键字可以应用于方法，以指示该方法是用 Java 以外的语言实现的
2.transient:
	transient 关键字可以应用于类的成员变量，以便指出该成员变量不应在包含它的类实例已序列化时被序列化
	Java 的 serialization 提供了一种持久化对象实例的机制。当持久化对象时，可能有一个特殊的对象数据成员，
	我们不想用serialization机制来保存它,为了在一个特定对象的一个域上关闭serialization，可以在这个域前加上关键字 transient.   
	transient 是 Java 语言的关键字，用来表示一个域不是该对象串行化的一部分。当一个对象被串行化的时候，
	transient 型变量的值不包括在串行化的表示中，然而非 transient 型的变量是被包括进去的。  
3.final:(部分内容参考: "JMM-Java内存管理模型与并发.java")
	/**
	 * http://www.importnew.com/18586.html
	 * http://www.importnew.com/7553.html
	 */
	3.1.含义:
		final 在Java中是一个保留的关键字,可以声明成员变量、方法、类以及本地变量.一旦你将引用声明作 final,
		你将不能改变这个引用了,编译器会检查代码,如果你试图将变量再次初始化的话，编译器会报编译错误;
	3.2.final 修饰符:
		(1).修饰变量:
			对于一个 final 变量，如果是基本数据类型的变量，则其数值一旦在初始化之后便不能更改;
			如果是引用类型的变量，则在对其初始化之后便不能再让其指向另一个对象;
		(2).修饰方法:
			方法前面加上 final 关键字，代表这个方法不可以被子类的方法重写;
			final 方法比非 final 方法要快,因为在编译的时候已经静态绑定了,不需要在运行时再动态绑定
			==>类的 private 方法会隐式地被指定为final方法;
		(3).修饰类:
			当用 final 修饰一个类时，表明这个类不能被继承,final 类中的所有成员方法都会被隐式地指定为 final 方法;
			Java 中许多类都是 final 类,如:String,Integer
	3.3.注意点:
		(1).final 和 static:
			static 作用于成员变量用来表示只保存一份副本，而 final 的作用是用来保证变量不可变
			看代码:每次打印的两个j值都是一样的，而i的值却是不同的
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
		(2).final 变量可以安全的在多线程环境下进行共享，而不需要额外的同步开销。
		(3).使用 final 关键字，JVM 会对方法、变量及类进行优化;
	3.5.不可变类:
		创建不可变类要使用 final 关键字。不可变类是指它的对象一旦被创建了就不能被更改了。String 是不可变类的代表。
		不可变类有很多好处，譬如它们的对象是只读的，可以在多线程环境下安全的共享，不用额外的同步开销等等;
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
		(1).final 成员变量必须在声明的时候初始化或者在构造器中初始化，否则就会报编译错误;
		(2).接口中声明的所有变量本身是 final 的;
		(3).final 和 abstract 这两个关键字是反相关的，final 类就不可能是 abstract 的;
		(4).final 方法在编译阶段绑定，称为静态绑定(static binding)
		(5).将类、方法、变量声明为 final 能够提高性能，这样 JVM 就有机会进行估计，然后优化;
五.协变式重写和泛型重载:
1.协变式重写
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
	
2.泛型重载:
	(1).Java的方法重载一般指在同一个类中的两个同名方法,规则很简单：两个方法必须具有不同的方法签名;
	换句话说:就是这两个方法的参数必须不相同,使得编译器能够区分开这两个重载的方法;
	由于编译器不能仅仅通过方法的返回值类型来区分重载方法,所以如果两个方法只有返回类型不同,其它完全一样，编译是不能通过的。
	// 在泛型方法的重载时，这个规则稍微有一点变化,看如下代码:
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
		// 类似地，一个类不能同时继承两个具有相同擦除类型的父类，也不能同时实现两个具有相同擦除的接口。
		// 如Class A implements Comparable<Integer>, Comparable<Long>。
	(3).总结一下:两个泛型方法在擦除泛型信息后,如果具有相同的参数类型,而返回值不一样,是可以进行重载的;
	Java有足够的信息来区分这两个重载的方法
3.重写与重载:
	3.1.两者的比较:
		(1).重载是一个编译期概念、重写是一个运行期间概念;
		(2).重载遵循所谓“编译期绑定”，即在编译时根据参数变量的类型判断应该调用哪个方法。
		(3).重写遵循所谓“运行期绑定”，即在运行的时候，根据引用变量所指向的实际对象的类型来调用方法
		(4).因为在编译期已经确定调用哪个方法，所以重载并不是多态。而重写是多态。重载只是一种语言特性，
			是一种语法规则，与多态无关，与面向对象也无关。(注:严格来说，重载是编译时多态，即静态多态。
			但是，Java中提到的多态，在不特别说明的情况下都指动态多态)
	3.2.重写的条件:
		参数列表必须完全与被重写方法的相同；
		返回类型必须完全与被重写方法的返回类型相同；
		访问级别的限制性一定不能比被重写方法的强；
		访问级别的限制性可以比被重写方法的弱；
		重写方法一定不能抛出新的检查异常或比被重写的方法声明的检查异常更广泛的检查异常
		重写的方法能够抛出更少或更有限的异常（也就是说，被重写的方法声明了异常，但重写的方法可以什么也不声明）
		不能重写被标示为final的方法；
		如果不能继承一个方法，则不能重写这个方法
	3.3.重载的条件:
		被重载的方法必须改变参数列表；
		被重载的方法可以改变返回类型；
		被重载的方法可以改变访问修饰符；
		被重载的方法可以声明新的或更广的检查异常；
		方法能够在同一个类中或者在一个子类中被重载;
	
六.Java 序列化:序列化是一种对象持久化的手段
1.Java 对象序列化是 JDK 1.1 中引入的一组开创性特性之一,用于作为一种将 Java 对象的状态转换为字节数组,
	以便存储或传输的机制,以后，仍可以将字节数组转换回 Java 对象原有的状态
	(1).对象序列化保存的是对象的"状态"，即它的成员变量。由此可知，对象序列化不会关注类中的"静态变量";
	(2).在 Java 中，只要一个类实现了 java.io.Serializable 接口，那么它就可以被序列化;
		实现 Externalizable,自己要对序列化内容进行控制,控制哪些属性可以被序列化,哪些不能被序列化
	(3).通过 ObjectOutputStream 和 ObjectInputStream 对对象进行序列化及反序列化;
	(4).虚拟机是否允许反序列化，不仅取决于类路径和功能代码是否一致，一个非常重要的一点是两个类的序列化 ID 是否一致,
		就是 private static final long serialVersionUID
	(5).要想将父类对象也序列化，就需要让父类也实现 Serializable 接口;
		如果父类实现了 Serializable 接口,子类但没有实现 Serializable 接口,子类拥有一切可序列化相关的特性,子类可以序列化;
		如果子类实现 Serializable 接口,父类不实现,根据父类序列化规则,父类的字段数据将不被序列化,从而达到部分序列化的功能;
		在反序列化时仍会调用父类的构造器,只能调用父类的无参构造函数作为默认的父对象
	(6).transient 关键字的作用是控制变量的序列化，在变量声明前加上该关键字，可以阻止该变量被序列化到文件中，
		在被反序列化后，transient 变量的值被设为初始值，如 int 型的是 0，对象型的是 null
	(7).Java 序列化机制为了节省磁盘空间，具有特定的存储规则，当写入文件的为同一对象时，并不会再将对象的内容进行存储，
	而只是再次存储一份引用，上面增加的 5 字节的存储空间就是新增引用和一些控制信息的空间。反序列化时，恢复引用关系
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
		// 结果两个输出的都是 1， 原因就是第一次写入对象以后，第二次再试图写的时候，虚拟机根据引用关系
		// 知道已经有一个相同对象已经写入文件，因此只保存第二次写的引用，所以读取时，都是第一次保存的对象
2.在序列化过程中,如果被序列化的类中定义了writeObject 和 readObject 方法，
	虚拟机会试图调用对象类里的 writeObject 和 readObject 方法，进行用户自定义的序列化和反序列化。
	如果没有这样的方法，则默认调用是 ObjectOutputStream 的 defaultWriteObject 方法以及
	 ObjectInputStream 的 defaultReadObject 方法。
	用户自定义的 writeObject 和 readObject 方法可以允许用户控制序列化的过程，比如可以在序列化的过程中动态改变序列化的数值;
	2.1.ArrayList 使用上述实现:为什么ArrayList要用这种方式来实现序列化呢?
		(1).为什么 transient Object[] elementData;
			ArrayList 实际上是动态数组，每次在放满以后自动增长设定的长度值，如果数组自动增长长度设为100，
			而实际只放了一个元素，那就会序列化 99 个 null 元素。为了保证在序列化的时候不会将这么多 null 同时进行序列化，
			ArrayList 把元素数组设置为 transient
		(2).为什么要写方法:writeObject and readObject
			前面提到为了防止一个包含大量空对象的数组被序列化，为了优化存储，所以，ArrayList 使用 transient 来声明elementData
			作为一个集合，在序列化过程中还必须保证其中的元素可以被持久化下来，
			所以，通过重写writeObject 和 readObject方法的方式把其中的元素保留下来
			writeObject方法把elementData数组中的元素遍历的保存到输出流（ObjectOutputStream）中。
			readObject方法从输入流（ObjectInputStream）中读出对象并保存赋值到elementData数组中
	2.2.如何自定义的序列化和反序列化策略
		可以通过在被序列化的类中增加writeObject 和 readObject方法。那么问题又来了;
		(1).那么如果一个类中包含writeObject 和 readObject 方法，那么这两个方法是怎么被调用的呢
			在使用 ObjectOutputStream 的writeObject方法和 ObjectInputStream 的readObject方法时，会通过反射的方式调用
			①.ObjectOutputStream 的writeObject 的调用栈：
				writeObject ---> writeObject0 --->writeOrdinaryObject--->writeSerialData--->invokeWriteObject
			②.这里看一下invokeWriteObject：
				其中writeObjectMethod.invoke(obj, new Object[]{ out });是关键，通过反射的方式调用writeObjectMethod方法
	2.3.Serializable 明明就是一个空的接口，它是怎么保证只有实现了该接口的方法才能进行序列化与反序列化的呢？
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
		在进行序列化操作时，会判断要被序列化的类是否是 Enum、Array 和 Serializable 类型，
		如果不是则直接抛出 NotSerializableException
3.private static final long serialVersionUID:每个可序列化类相关联
	(1).该序列号在反序列化过程中用于验证序列化对象的发送者和接收者是否为该对象加载了与序列化兼容的类;
	(2).如果接收者加载的该对象的类的 serialVersionUID 与对应的发送者的类的版本号不同,
		则反序列化将会导致 InvalidClassException;
	(3).为保证 serialVersionUID 值跨不同 java 编译器实现的一致性，序列化类必须声明一个明确的 serialVersionUID ;
	(4).使用 private 修饰符显示声明 serialVersionUID（如果可能），
		原因是这种声明仅应用于直接声明类 – serialVersionUID 字段作为继承成员没有用处;
	(5).类的serialVersionUID的默认值完全依赖于Java编译器的实现，对于同一个类，用不同的Java编译器编译，
		有可能会导致不同的serialVersionUID，也有可能相同
	(6).显式地定义serialVersionUID有两种用途:
		①.在某些场合，希望类的不同版本对序列化兼容，因此需要确保类的不同版本具有相同的serialVersionUID；在某些场合，
			不希望类的不同版本对序列化兼容，因此需要确保类的不同版本具有不同的serialVersionUID
		②.当你序列化了一个类实例后，希望更改一个字段或添加一个字段，不设置serialVersionUID，所做的任何更改都将导致
			无法反序化旧有实例，并在反序列化时抛出一个异常。如果你添加了serialVersionUID，在反序列旧有实例时，
			新添加或更改的字段值将设为初始化值（对象为null，基本类型为相应的初始默认值），字段被删除将不设置
4.反序列化:
	(1).实现 Serializable 接口的对象在反序列化时不需要调用对象所在类的构造方法,完全基于字节,
		如果是子类继承父类的序列化,那么将调用父类的构造方法;
	(2).实现 Externalizable  接口的对象在反序列化时会调用构造方法

5.序列化实现对象的拷贝:
	内存中通过字节流的拷贝是比较容易实现的.把母对象写入到一个字节流中,再从字节流中将其读出来,这样就可以创建一个新的对象了,
	并且该新对象与母对象之间并不存在引用共享的问题，真正实现对象的深拷贝
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
6.常见的序列化协议:
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
		⑤.与XML相比，其协议比较简单，解析速度比较快;
		⑥.松散的Associative array使得其具有良好的可扩展性和兼容性
	(5).Thrift:是 Facebook 开源提供的一个高性能,轻量级 RPC 服务框架,其产生正是为了满足当前大数据量、分布式、跨语言、
		跨平台数据通讯的需求;其并不仅仅是序列化协议,而是一个 RPC 框架;
		由于Thrift的序列化被嵌入到Thrift框架里面,Thrift框架本身并没有透出序列化和反序列化接口,
		这导致其很难和其他传输层协议共同使用;		
	(6).Protobuf:
		①.标准的IDL和IDL编译器，这使得其对工程师非常友好;
		②.序列化数据非常简洁,紧凑,与XML相比,其序列化之后的数据量约为1/3到1/10;
		③.解析速度非常快，比对应的XML快约20-100倍;
		④.提供了非常友好的动态库，使用非常简介，反序列化只需要一行代码;

七.Java 垃圾回收机制 http://www.hollischuang.com/archives/76

八.关于 try...catch...finally:(http://www.cnblogs.com/aigongsi/archive/2012/04/19/2457735.html)
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
		(1).try、catch、finally 语句中，在如果 try 语句有 return 语句，则返回的之后当前 try 中变量此时对应的值，
			此后对变量做任何的修改，都不影响 try 中 return 的返回值;
		(2).如果 finally 块中有 return 语句，则 try 或 catch 中的返回语句忽略;
		(3).如果 finally 块中抛出异常，则整个 try、catch、finally 块中抛出异常;
		(4).如果 catch 异常中写了多个需要 catch 的异常,可以如果匹配到了捕获的异常,则后面其他的异常都将被忽略
	2.使用 try...catch...finally 需要注意:
		(1).尽量在 try 或者 catch 中使用 return 语句.通过 finally 块中达到对 try 或者 catch 返回值修改是不可行的;
		(2).finally 块中避免使用 return 语句，因为 finally 块中如果使用 return 语句，
			会显示的消化掉 try、catch 块中的异常信息，屏蔽了错误的发生;
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

九.Java 四舍五入:
1.目前 Java 支持7中舍入法
	(1).ROUND_UP：远离零方向舍入。向绝对值最大的方向舍入，只要舍弃位非0即进位
	(2).ROUND_DOWN：趋向零方向舍入。向绝对值最小的方向输入，所有的位都要舍弃，不存在进位情况
	(3).ROUND_CEILING：向正无穷方向舍入。向正最大方向靠拢。若是正数，舍入行为类似于 ROUND_UP，
		若为负数，舍入行为类似于 ROUND_DOWN。 Math.round() 方法就是使用的此模式。
	(4).ROUND_FLOOR：向负无穷方向舍入。向负无穷方向靠拢。若是正数，舍入行为类似于 ROUND_DOWN；
		若为负数，舍入行为类似于 ROUND_UP。
	(5).HALF_UP：最近数字舍入(5进)。这是我们最经典的四舍五入。
	(6).HALF_DOWN：最近数字舍入(5舍)。在这里5是要舍弃的。
	(7).HAIL_EVEN：银行家舍入法。
2.保留位:
	(1).四舍五入:
		double   f   =   111231.5585;
	    BigDecimal   b   =   new   BigDecimal(f);
	    double   f1   =   b.setScale(2,   RoundingMode.HALF_UP).doubleValue();
	(2).格式化:
		java.text.DecimalFormat   df   =new   java.text.DecimalFormat(”#.00″);
		df.format(你要格式化的数字);
	(3).类C语言:
		double d = 3.1415926;
		String result = String .format(”%.2f”);
		%.2f %. 表示 小数点前任意位数   2 表示两位小数 格式后的结果为f 表示浮点型
	(4).此外如果使用 struts 标签做输出的话, 有个 format 属性,设置为 format=”0.00″就是保留两位小数
		<bean:write name="entity" property="dkhAFSumPl"  format="0.00" />
		或者
		<fmt:formatNumber type="number" value="${10000.22/100}" maxFractionDigits="0"/>
		maxFractionDigits表示保留的位数
3.Math:
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

十.Java 中保留小数位数的处理:
1.使用 BigDecimal,保留小数点后两位
	public static String format1(double value) {
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(2, RoundingMode.HALF_UP);
	    return bd.toString();
	}
2.使用 DecimalFormat,保留小数点后两位
	public static String format2(double value) {
	    DecimalFormat df = new DecimalFormat("0.00");
	    df.setRoundingMode(RoundingMode.HALF_UP);
	    return df.format(value);
	}

3.使用 NumberFormat,保留小数点后两位
	public static String format3(double value) {
	    NumberFormat nf = NumberFormat.getNumberInstance();
	    nf.setMaximumFractionDigits(2);
	    /*
	     * setMinimumFractionDigits设置成2
	     * 如果不这么做，那么当value的值是100.00的时候返回100
	     * 而不是100.00
	     */
	    nf.setMinimumFractionDigits(2);
	    nf.setRoundingMode(RoundingMode.HALF_UP);
	    /*
	     * 如果想输出的格式用逗号隔开，可以设置成true
	     */
	    nf.setGroupingUsed(false);
	    return nf.format(value);
	}

4.使用 java.util.Formatter,保留小数点后两位
	public static String format4(double value) {
	    /*
	     * %.2f % 表示 小数点前任意位数 2 表示两位小数 格式后的结果为 f 表示浮点型
	     */
	    return new Formatter().format("%.2f", value).toString();
	}

5.使用 String.format来实现
	public static String format5(double value) {
	    return String.format("%.2f", value).toString();
	}
	5.1.对浮点数进行格式化:占位符格式为： %[index$][标识]*[最小宽度][.精度]转换符
		ouble num = 123.4567899;
		System.out.print(String.format("%f %n", num)); // 123.456790 
		System.out.print(String.format("%a %n", num)); // 0x1.edd3c0bb46929p6 
		System.out.print(String.format("%g %n", num)); // 123.457
		(1).可用标识符
			-，在最小宽度内左对齐,不可以与0标识一起使用。
			0，若内容长度不足最小宽度，则在左边用0来填充。
			#，对8进制和16进制，8进制前添加一个0,16进制前添加0x。
			+，结果总包含一个+或-号。
			空格，正数前加空格，负数前加-号。
			,，只用与十进制，每3位数字间用,分隔。
			(，若结果为负数，则用括号括住，且不显示符号。
		(2).可用转换符：
			b，布尔类型，只要实参为非false的布尔类型，均格式化为字符串true，否则为字符串false。
			n，平台独立的换行符, 也可通过System.getProperty("line.separator")获取。
			f，浮点数型（十进制）。显示9位有效数字，且会进行四舍五入。如99.99。
			a，浮点数型（十六进制）。
			e，指数类型。如9.38e+5。
			g，浮点数型（比%f，%a长度短些，显示6位有效数字，且会进行四舍五入）

十一.Java 中 length 和 length() 的区别:
1.获取数组的长度是使用属性 length,获取字符串长度是使用方法 length() 
2.为什么数组有length属性?
	(1).数组是一个容器对象,其中包含固定数量的同一类型的值.一旦数组被创建,那么数组的长度就是固定的了.
		数组的长度可以作为final实例变量的长度。因此，长度可以被视为一个数组的属性
	(2).有两种创建数组的方法：1、通过数组表达式创建数组。2、通过初始化值创建数组。
		无论使用哪种方式，一旦数组被创建，其大小就固定了
3.Java 中为什么没有定义一个类似 String 一样 Array 类:
	数组包含所有从 Object 继承下来方法,为什么没有一个array类呢?一个简单的解释是它被隐藏起来了
4.为什么 String 有length()方法?
	背后的数据结构是一个 char 数组,所以没有必要来定义一个不必要的属性（因为该属性在 char 数值中已经提供了）

十二.数组:
1.Java 中数组是对象吗?
	(1).什么是对象:
		语言层面:对象是根据某个类创建出来的一个实例,表示某类事物中一个具体的个体.对象具有各种属性，并且具有一些特定的行为
		计算机层面:对象就是内存中的一个内存块,在这个内存块封装了一些数据,也就是类中定义的各个属性
	(2).数组:
		语言层面上,数组不是某类事物中的一个具体的个体，而是多个个体的集合,那么数组应该不是对象;
		而在计算机的角度，数组也是一个内存块，也封装了一些数据，这样的话也可以称之为对象
			int[] a = new int[4];
			//a.length;  //对属性的引用不能当成语句
			int len = a.length;  //数组中保存一个字段, 表示数组的长度		
			//以下方法说明数组可以调用方法,java中的数组是对象.
			//这些方法是Object中的方法,所以可以肯定,数组的最顶层父类也是Object
			a.clone();
			a.toString();
	==> 这基本上可以认定,java中的数组也是对象,它具有java中其他对象的一些基本特点:
		封装了一些数据,可以访问属性,也可以调用方法.所以:Java数组是对象
	==> 而在 C++中，数组虽然封装了数据，但数组名只是一个指针，指向数组中的首个元素，既没有属性，也没有方法可以调用
		所以 C++中的数组不是对象，只是一个数据的集合，而不能当做对象来使用
2.Java中数组的类型:数组也是有类型的
	2.1.虚拟机自动创建了数组类型，可以把数组类型和8种基本数据类型一样， 当做java的内建类型:
		(1).每一维度用一个"["表示;开头两个"["，就代表是二维数组.
		(2)."["后面是数组中元素的类型(包括基本数据类型和引用数据类型).
	2.2.String[] s = new String[4];
		在java语言层面上,s是数组,也是一个对象,那么他的类型应该是 String[]
		在JVM中，他的类型为 [java.lang.String
3.Java中数组的继承关系:
	3.1.数组的顶层父类也必须是 Object，这就说明数组对象可以向上直接转型到 Object，也可以向下强制类型转换，
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
		(1).数组类直接继承了 Object，关于 Object[]类型的引用能够指向 String[]类型的对象,这并不是严格意义上的继承,
			String[] 不继承自 Object[]，但是可以允许 String[]向上转型到 Object[]
			可以理解为:
			其实这种关系可以这样表述：如果有两个类A和B,如果B继承(extends)了A,那么A[]类型的引用就可以指向B[]类型的对象
		(2).数组的这种用法不能作用于基本类型数据:
			int[] aa = new int[4];  
			Object[] objaa = aa;  //错误的，不能通过编译  
			因为 int 不是引用类型，Object 不是 int 的父类,在这里自动装箱不起作用
			Object 数组中可以存放任何值，包括基本数据类型
			public class ArrayTest {
				public static void main(String[] args) {
					test1();
					test2();
					test3();
				}
				/**
				 * 数组具有这种特性：
				 * 如果有两个类A和B，如果B继承（extends）了A，那么A[]类型的引用就可以指向B[]类型的对象
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
				private static void doArray3(String name, int age, String id, float account){
					//如果不适用数组而是依次传递参数，会使参数列表变得冗长，难以阅读
				}
				/**
				 * 测试数组的集成关系, 并且他的继承关系是否和数组中元素的类型有关
				 */
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
					 */
					
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
					 */
					
					//9		下面的代码成立吗?
					int[] aa = new int[4];
					//Object[] objaa = aa;  //错误的，不能通过编译
					//这是错误的, 因为Object不是int的父类,在这里自动装箱不起作用
					
					//10 	这样可以吗？
					Object[] objss = {"aaa", 1, 2.5};//成立
				}

				/**
				 * 测试在java语言中,数组是不是对象
				 * 如果是对象, 那么他的类型是什么?
				 */
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
					 */
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
					 */
				}				
				public static class Father {
				}				
				public static class Son extends Father {
				}
			}
4.Java 数组初始化:
	(1).Java 数组是静态的，即当数组被初始化之后，该数组的长度是不可变的;

十三.switch:
1.JDK7 之后,switch 的参数可以是 String 类型了;到目前为止 switch 支持的数据类型:byte short int char String 枚举类型
2.switch 对整型的支持:switch 对 int 的判断是直接比较整数的值
3.switch 对字符型支持的实现:
	对 char 类型进行比较的时候，实际上比较的是 Ascii 码，编译器会把 char 型变量转换成对应的 int 型变量
4.switch 对字符串支持的实现c
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
		(1).switch 中只能使用整型,hashCode()方法返回的是int，而不是long
		(2).进行 switch 的实际是哈希值，然后通过使用equals方法比较进行安全检查，这个检查是必要的，因为哈希可能会发生碰撞
		(3).其实 switch 只支持一种数据类型，那就是整型，其他数据类型都是转换成整型之后在使用 switch 的

十四.抽象类与接口:抽象类与接口是 Java 语言中对抽象概念进行定义的两种机制
/**
 * 参考文章:
 * http://blog.csdn.net/chenssy/article/details/12858267
 * http://www.cnblogs.com/dolphin0520/p/3811437.html
 */
1.抽象类:如果一个类含有一个被 abstract 修饰的方法,那么该类就是抽象类,抽象类必须在类前用 abstract 关键字修饰
	1.1.相关概念:
		抽象类体现了数据抽象的思想,是实现多态的一种机制;
		抽象类的存在就是为了继承的,所以说抽象类就是用来继承的;
	1.2.注意点:
		(1).抽象类不能被实例化，实例化的工作应该交由它的子类来完成，它只需要有一个引用即可;
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
2.接口:接口本身就不是类,在软件工程中,接口泛指供别人调用的方法或者函数,类使用 interface Demo{} 修饰
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
3.接口与抽象类的区别:
	3.1.语法层面上:
		(1).抽象类可以提供成员方法的实现细节,而接口中只能存在 public abstract 方法；
		(2).抽象类中的成员变量可以是各种类型的,而接口中的成员变量只能是 public static final 类型的
		???(3).接口中不能含有静态代码块以及静态方法,而抽象类可以有静态代码块和静态方法;
		???(4).接口和抽象类不能实例化,接口中不能有构造,抽象类可以有构造方法;
		(5).一个类只能继承一个抽象类,而一个类却可以实现多个接口;
		(6).接口和抽象类都可以包含内部类(抽象类)或者内部接口
	3.2.设计层面上:
		(1).抽象层次不同:抽象类是对类抽象,而接口是对行为的抽象,
			抽象类是对整个类整体进行抽象,包括属性、行为,但是接口却是对类局部(行为)进行抽象;
			抽象是:is-a 的关系
			接口是:like-a 的关系
		(2).跨域不同:抽象类所跨域的是具有相似特点的类,而接口却可以跨域不同的类
			抽象类所体现的是一种继承关系，要想使得继承关系合理，父类和派生类之间必须存在"is-a" 关系,即父类和派生类在概念
			本质上应该是相同的.对于接口则不然,接口是"like-a ",并不要求接口的实现者和接口定义在概念本质上是一致的
		(3).设计层次不同:抽象类是自下而上抽象出来的,需要先知道子类才能抽象出父类;
			接口则是自顶向下的设计,接口根本不需要知道子类的存在,其只需要定义一个规则即可;
//http://stackoverflow.com/questions/19722847/static-initialization-in-interface
4.Java8 下接口的不同之处,(上述是针对 JDK7 之前的)
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
	4.2.如果实现一个接口,默认方法可以不用覆盖重写实现,实现类默认可以直接调用该默认方法;
		实现类无法重写接口中的静态方法;
		注意:在声明一个默认方法前，请仔细思考是不是真的有必要使用默认方法，因为默认方法会带给程序歧义,
			并且在复杂的继承体系中容易产生编译错误
		http://docs.oracle.com/javase/tutorial/java/IandI/defaultmethods.html










































































































