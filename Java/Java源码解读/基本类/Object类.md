* Object 类是 Java 中的终极父类,任何类都默认继承Object类,然而接口是不继承Object类;
* ????为什么接口不继承Object类????

#### 1.Object 类中 clone() 方法:
	1.1.作用:
		clone()可以产生一个相同的类并且返回给调用者.

	1.2.clone()工作原理:
		Object将clone()作为一个本地方法来实现，这意味着它的代码存放在本地的库中;
		当代码执行的时候，将会检查调用对象的类(或者父类)是否实现了java.lang.Cloneable接口(Object类不实现Cloneable);
		如果没有实现这个接口,clone()将会抛出一个检查异常()——java.lang.CloneNotSupportedException,
		如果实现了这个接口,clone()会创建一个新的对象，并将原来对象的内容复制到新对象，最后返回这个新对象的引用	
			public class CloneDemo implements Cloneable {
				int x;

				public static void main(String[] args) throws CloneNotSupportedException {
					CloneDemo cd = new CloneDemo();
					cd.x = 5;
					System.out.printf("cd.x = %d%n", cd.x);
					CloneDemo cd2 = (CloneDemo) cd.clone();
					System.out.printf("cd2.x = %d%n", cd2.x);
				}
			}
	1.3.什么情况下需要覆盖clone()方法呢?
		调用clone()的代码是位于被克隆的类(即CloneDemo类)里面的,所以就不需要覆盖clone()了.
		但是,如果调用别的类中的clone(),就需要覆盖clone()了.否则,将会看到“clone在Object中是被保护的”
		// 提示:因为clone()在Object中的权限是protected	
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
					System.out.printf("data.x = %d%n", data.x);
					Data data2 = (Data) data.clone();
					System.out.printf("data2.x = %d%n", data2.x);
				}
			}

	1.4.浅克隆:
		(1).浅克隆(也叫做浅拷贝)仅复制了这个对象本身的成员变量,该对象如果引用了其他对象的话,也不对其复制.
		上述代码演示了浅克隆.新的对象中的数据包含在了这个对象本身中,不涉及对别的对象的引用.
		(2).如果一个对象中的所有成员变量都是原始类型,并且其引用了的对象都是不可改变的(大多情况下都是)时,使用浅克隆效果很好！
		但是,如果其引用了可变的对象,那么这些变化将会影响到该对象和它克隆出的所有对象.		
		// 浅克隆在复制引用了可变对象的对象时存在着问题
		// 克隆后的对象修改,同样会影响到被克隆的对象
		
	1.5.深克隆:会复制这个对象和它所引用的对象的成员变量，如果该对象引用了其他对象，深克隆也会对其复制;
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
				Employee(String name, int age, Address address) {
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
					Employee e = new Employee("John Doe", 49, new Address("Denver"));
					System.out.printf("%s: %d: %s%n", e.getName(), e.getAge(), e
							.getAddress().getCity());
					Employee e2 = (Employee) e.clone();
					System.out.printf("%s: %d: %s%n", e2.getName(), e2.getAge(), e2
							.getAddress().getCity());
					e.getAddress().setCity("Chicago");
					System.out.printf("%s: %d: %s%n", e.getName(), e.getAge(), e
							.getAddress().getCity());
					System.out.printf("%s: %d: %s%n", e2.getName(), e2.getAge(), e2
							.getAddress().getCity());
				}
			}	
		
		// 注意:从Address类中的clone()函数可以看出，这个clone()和我们之前写的clone()有些不同：
		(1).Address类没有实现Cloneable接口。因为只有在Object类中的clone()被调用时才需要实现，
			而Address是不会调用clone()的，所以没有实现Cloneable()的必要。
		(2).这个clone()函数没有声明抛出CloneNotSupportedException。这个检查异常只可能在调用Object类clone()的时候抛出。
			clone()是不会被调用的，因此这个异常也就没有被处理或者传回调用处的必要了。
		(3).Object类的clone()没有被调用(这里没有调用super.clone())。因为这不是对Address的对象进行浅克隆——
			只是一个成员变量复制而已。
		(4).为了克隆Address的对象，需要创建一个新的Address对象并对其成员进行初始化操作。最后将新创建的Address对象返回。
	1.6.序列化实现对象的拷贝:
		内存中通过字节流的拷贝是比较容易实现的.把母对象写入到一个字节流中,再从字节流中将其读出来,这样就可以创建一个新的对象了,
		并且该新对象与母对象之间并不存在引用共享的问题，真正实现对象的深拷贝
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
	1.7.String 的clone的特殊性? StringBuilder 和 StringBuffer 呢?
		(1).由于基本数据类型都能自动实现深度 clone,引用类型默认实现的是浅度 clone;而 String 是引用类型的一个特例,
			我们可以和操作基本数据类型一样认为其实现了深度 clone（实质是浅克隆，切记只是一个假象）.
			由于 String 是不可变类,对于 String 类中的很多修改操作都是通过新new对象复制处理的,所以当我们修改 clone 前后对象里面 
			String 属性的值时其实都是属性引用的重新指向操作,自然对 clone 前后对象里 String 属性是没有相互影响的,类似于深度克隆;
			所以虽然他是引用类型而且我们在深度克隆时无法调用其 clone 方法,但是其不影响我们深度克隆的使用;
		(2).如果要实现深度克隆则 StringBuffer 和 StringBuilder 是需要主动特殊处理的,否则就是真正的对象浅克隆,
			所以处理的办法就是在类的 clone 方法中对 StringBuffer 或者 StringBuilder 属性进行如下主动拷贝操作;
	1.8.Java 中集合的克隆:
		(1).集合中默认克隆方式都是浅克隆,而且集合类提供的拷贝构造方式或addAll,add等方法都是浅克隆.
			就是说存储在原集合和克隆集合中的对象会保持一致并指向堆中同一内存地址.
			List<Person> destList = (List<Person>)srcList.clone();
			List<Person> destList = new ArrayList<Person>(srcList.size());
			for(Person person : srcList){
				destList.add(person);
			}
			// 使用集合默认的 clone 方法复制（浅）
			List<InfoBean> destList1 = (List<InfoBean>) srcList.clone();
			// 使用 add 方法循环遍历复制（浅）
			List<InfoBean> destList = new ArrayList<InfoBean>(srcList.size());
			for (InfoBean bean : srcList) {
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
			System.arraycopy(srcBeans, 0, destBeans, 0, srcBeans.length);
		(2).集合实现深克隆的方法:
			==> 序列化:
				public static <T extends Serializable> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
				    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
				    ObjectOutputStream objOut = new ObjectOutputStream(byteOut);

				    objOut.writeObject(src);

				    ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
				    ObjectInputStream objIn = new ObjectInputStream(byteIn);
				    return (List<T>) objIn.readObject();
				}
			==> 集合中实体类实现 Cloneable 接口,拷贝时逐个拷贝克隆
				destList.add((InfoBean)srcLisdt.get(index).clone());
#### 2.Object 中 equals()方法:
	public boolean equals(Object obj){
		return (this == obj);
	}
	基本原则:一致性、传递性、对称性、自反性、【对于任意的非空引用值x，x.equals(null)必须返回假】
	2.1.用途:用来检查一个对象与调用这个equals()的这个对象是否相等;
		对象都拥有标识(内存地址)和状态(数据),默认实现就是使用"=="比较,即比较两个对象的内存地址
		
	2.2.为什么不用“==”运算符来判断两个对象是否相等呢？
		(1).虽然“==”运算符可以比较两个数据是否相等，但是要来比较对象的话，恐怕达不到预期的结果。
		就是说，“==”通过是否引用了同一个对象来判断两个对象是否相等，这被称为“引用相等”。
		这个运算符不能通过比较两个对象的内容来判断它们是不是逻辑上的相等。	
		(2).使用Object的equals()方法比较的依据:调用它的对象和传入的对象的引用是否相等;
			也就是说,默认的equals()方法进行的是引用比较,如果相同引用返回true,否则返回false;
		
	2.3.equals()和继承:在重写 equals的时候推荐使用getClass进行类型判断
		class Employee {
			private String name;
			private int age;

			Employee(String name, int age) {
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
		(1).当Employee类被继承的时候,上述代码就存在问题:假如SaleRep类继承了Employee类,这个类中也有基于字符串类型的变量,
			equals()可以对其进行比较。假设你创建的Employee对象和SaleRep对象都有相同的“名字”和“年龄”。
			但是，SaleRep中还是添加了一些内容;
			会违背传递性原则:
	2.4.在 java 中进行比较，我们需要根据比较的类型来选择合适的比较方式：
		(1).对象域,使用 equals 方法;
		(2).类型安全的枚举，使用 equals 或==;
		(3).可能为 null 的对象域 : 使用 == 和 equals;
		(4).数组域 : 使用 Arrays.equals
		(5).除 float 和 double 外的原始数据类型 : 使用 == 
		(6).float 类型: 使用 Float.foatToIntBits 转换成 int 类型，然后使用==
			float 重写的equals():
			①.当且仅当参数不是 null 而是 Float 对象时,且表示 Float 对象的值相同,结果为 true,
			②.当且仅当将方法 foatToIntBits 应用于两个值所返回的 int 值相同时,才认为两个值相同;
			③.注意:在大多数情况下,对于 Float 的两个实例 f1 和 f2,当且仅当 f1.floatValue() == f2.floatValue() 为 true 时,
			f1.equals(f2)的值才为 true ,但是存在两种例外情况:
				==> 如果 f1 和 f2 都表示 Float.NAN,那么即使 Float.NaN == Float.NaN 的值为 false, equals方法也返回 true;
				==> 如果 f1 表示 +0.0f, 而 f2 表示 -0.0f,或相反,那么即使 0.0f == -0.0f 的值为 true,equals方法也返回 false
			这样情况下使得哈希表得意正确操作
		(7).double 类型: 使用 Double.doubleToLongBit 转换成 long 类型，然后使用==。
			理由同上

#### 3.hashCode()方法:
	3.1.用途: hashCode()方法返回给调用者此对象的哈希码(其值由一个hash函数计算得来);
		这个方法通常用在基于hash的集合类中，像java.util.HashMap,java.until.HashSet和java.util.Hashtable
	3.2.在覆盖equals()时,同时覆盖hashCode():保证对象的功能兼容于hash集合
	3.3.hashCode()方法的规则:
		(1).在同一个Java程序中,对一个相同的对象,无论调用多少次hashCode(),hashCode()返回的整数必须相同,
			因此必须保证equals()方法比较的内容不会更改.但不必在另一个相同的Java程序中也保证返回值相同;
		(2).如果两个对象用equals()方法比较的结果是相同的,那么这两个对象调用hashCode()应该返回相同的整数值;
		(3).当两个对象使用equals()方法比较的结果是不同的,hashCode()返回的整数值可以不同.然而,
			hashCode()的返回值不同可以提高哈希表的性能。

#### 4.finalize()方法:finalize()方法不会被调用第二次;finalize()方法对于虚拟机来说不是轻量级的程序;
	4.1.用途: finalize()方法可以被子类对象所覆盖,然后作为一个终结者,当GC被调用的时候完成最后的清理工作(例如释放系统资源之类);
		这就是终止。默认的finalize()方法什么也不做，当被调用时直接返回;

	4.2.避免使用finalize()方法:
		相对于其他JVM实现，终结器被调用的情况较少——可能是因为终结器线程的优先级别较低的原因。
		如果你依靠终结器来关闭文件或者其他系统资源，可能会将资源耗尽，当程序试图打开一个新
		的文件或者新的系统资源的时候可能会崩溃，就因为这个缓慢的终结器
		
	4.3.finalize()方法可以作为一个安全保障,以防止声明的终结方法未被调用	
		(1).如何实现finalize()方法:子类终结器一般会通过调用父类的终结器来实现	
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
	
#### 5.toString()方法:
	当编译器遇到 name + ": " + age 的表达时，会生成一个 java.lang.StringBuilder 对象，
	并调用 append() 方法来对字符串添加变量值和分隔符。最后调用 toString() 方法返回一个包含各个元素的字符串对象
#### 6.wait/notifAll	

#### 7.registerNatives

#### 8.getClass:其定义:
	public final native Class<?> getClass();
	final 的方法,不可重写
