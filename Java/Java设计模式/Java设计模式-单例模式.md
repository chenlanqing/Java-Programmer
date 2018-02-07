* http://www.hollischuang.com/archives/205
#### 1.单例类只能有一个实例、单例类必须自己创建自己的唯一实例、单例类必须给所有其他对象提供这一实例。
	如：配置文件、线程池、工具类、缓存、日志对象
	在应用中如果有两个或者两个以上的实例会引起错误，又或者我换句话说，就是这些类,
	在整个应用中,同一时刻,有且只能有一种状态
#### 2.创建单例的方法:懒汉，恶汉，双重校验锁，枚举和静态内部类
	2.1.懒汉式，线程不安全
		(1).是否 Lazy 初始化：是
		(2).是否多线程安全：否
		(3).实现难度：易
		(4).最大缺点:不支持多线程
			public class Singleton {
				private static Singleton instance;
				private Singleton (){
					// 为了防止client利用反射调用私有的构造方法，可以再创建第二个实例时抛出异常
					if(INSTANC != null){
						throw new IllegalArgumentException("No exist the second object");
					}
				}
				public static Singleton getInstance() {
					if (instance == null) {
						instance = new Singleton();
				}
				return instance;
				}
			}
	2.2.懒汉式，线程安全
		(1).是否 Lazy 初始化：是
		(2).是否多线程安全：是
		(3).实现难度：易
		(4).优点:线程安全,第一次调用才舒适化,避免内存浪费;
		(5).缺点:必须加锁 synchronized 才能保证单例，但加锁会影响效率
			public class Singleton {
				private static Singleton instance;
				private Singleton (){
					// 为了防止client利用反射调用私有的构造方法，可以再创建第二个实例时抛出异常
					if(INSTANC != null){
						throw new IllegalArgumentException("No exist the second object");
					}
				}
				public static synchronized Singleton getInstance() {
					if (instance == null) {
						instance = new Singleton();
				}
				return instance;
				}
			}
	2.3.饿汉式:
		(1).是否 Lazy 初始化：是
		(2).是否多线程安全：是
		(3).实现难度：易
		(4).优点:没有加锁，执行效率会提高
		(5).缺点:类加载时就初始化，浪费内存
			public class Singleton {
				private static Singleton instance = new Singleton();
				private Singleton (){}
				public static Singleton getInstance() {
					return instance;
				}
			}
			public class Singleton {  
			    private Singleton instance = null;  
			    static {  
			    	instance = new Singleton();  
			    }  
			    private Singleton (){}  
			    public static Singleton getInstance() {  
			    	return this.instance;  
			    }  
			}  
	2.4.防止反序列化时创建新的实例:
		* http://www.hollischuang.com/archives/1144
		/**
		 * 单例实现3：公有的成员为静态工厂方法，序列化时，要实现readResolve方法，防止反序列化出新的实例
		 */
		public class Singleton implements Serializable{
			private static final Singleton INSTANCE = new Singleton();
			private Singleton(){
			}	
			public static Singleton getInstance(){
				return INSTANCE;
			}
			private Object readResolve(){
				return INSTANCE;
			}
		}
	2.5.枚举实现单例,目前最好的方式，避免了反射的攻击和序列化的问题
		public enum SingletonEnum {
			INSTANCE;
			// 在反射时，通过私有构造器newInstance是会抛出非法参数异常：IllegalArgumentException
			// Exception in thread "main" java.lang.IllegalArgumentException: Cannot reflectively create enum objects
			public static void main(String... args)throws Exception{
				Constructor[] array = SingletonEnum.INSTANCE.getClass().getDeclaredConstructors();
				for (Constructor constructor : array) {
					constructor.setAccessible(true);
					constructor.newInstance(null);
				}
			}
		}
	2.6.延迟加载的单例模式:使用静态内部类的方式, 要明确实现lazy loading效果时才会使用
		public class FooSingleton4 {
		    private FooSingleton4() {
		    }
		    public static FooSingleton4 getInstance() {
		        return FooSingleton4Holder.INSTANCE;
		    }
		    private static class FooSingleton4Holder {
		        private static final FooSingleton4 INSTANCE = new FooSingleton4();
		    }
		}
		这种方式同样利用了classloder的机制来保证初始化instance时只有一个线程,跟饿汉式不同的是:
		饿汉式只要 FooSingleton4 类被装载了，那么instance就会被实例化（没有达到lazy loading效果），
		而这种方式是 FooSingleton4 类被装载了，instance不一定被初始化,因为 FooSingleton4Holder 类没有被主动使用，
		只有显示通过调用getInstance方法时，才会显示装载SingletonHolder类，从而实例化instance
	2.7.双重校验锁:
		2.7.1.代码1:
			public class Singleton {  
			    private static Singleton singleton;  
			    private Singleton (){}  
			    public static Singleton getSingleton() {  
				    if (singleton == null) { /
				        synchronized (Singleton.class) {  
				        	if (singleton == null) {  
				            	singleton = new Singleton();  
				        	}  
				        }  
				    }  
				    return singleton;  
			    }  
			}  
			上述代码也有问题:singleton = new Singleton();  不是原子操作,其在JVM上大概做了三件事情:
				(1).给 singleton 分配内存
				(2).调用 Singleton 的构造函数来初始化成员变量;
				(3).将singleton对象指向分配的内存空间(执行完这步 singleton 就为非 null 了)	
				但是在 JVM 的即时编译器中存在指令重排序的优化,上面的第二步骤和第三步骤执行的顺序是不能保障的;
				如果在步骤3执行完毕,步骤2还未执行之前,被其他线程抢占,这时 singleton 已经是非 null 了但是未被初始化
				所以线程会直接返回 singleton, 然后使用，然后顺理成章地报错
		2.7.2.volatile 保证单例是线程安全的,会禁止指令重排序优化
			public class Singleton {  
			    private volatile static Singleton singleton;  
			    private Singleton (){}  
			    public static Singleton getSingleton() {  
				    if (singleton == null) {  
				        synchronized (Singleton.class) {  
				        	if (singleton == null) {  
				            	singleton = new Singleton();  
				        	}  
				        }  
				    }  
				    return singleton;  
			    }  
			}  
			注意:在 Java 5 以前的版本使用了 volatile 的双检锁还是有问题的
				Java 5 以前的 JMM (Java 内存模型)是存在缺陷的,即时将变量声明成 volatile 也不能完全避免重排序,
				主要是 volatile 变量前后的代码仍然存在重排序问题
	2.8.总结:有两个问题需要注意
		(1).如果单例由不同的类装载器装入,那便有可能存在多个单例类的实例.假定不是远端存取,例如一些servlet容器
			对每个servlet使用完全不同的类装载器，这样的话如果有两个servlet访问一个单例类，它们就都会有各自的实例
		==> 解决方案:
			private static Class getClass(String classname)throws ClassNotFoundException {
				ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

			      if(classLoader == null)     
			         classLoader = Singleton.class.getClassLoader();     

			      return (classLoader.loadClass(classname));     
			   }     
			}  
		(2).如果Singleton实现了 java.io.Serializable 接口，那么这个类的实例就可能被序列化和复原。不管怎样，如果你
			序列化一个单例类的对象，接下来复原多个那个对象，那你就会有多个单例类的实例:
			public class Singleton implements java.io.Serializable {     
			   public static Singleton INSTANCE = new Singleton(); 
			   protected Singleton() { }     
			   private Object readResolve() {     
			            return INSTANCE;     
			   }    
			} 
#### 3.写单例时需要注意序列化对单例的破坏: 
    * http://www.hollischuang.com/archives/1144
	防止反射与反序列化
	3.1.在反序列化的过程中到底发生了什么,使得反序列化后的单例不是唯一的?
		分析一下 ObjectInputputStream 的readObject 方法执行情况到底是怎样的
		(1).ObjectInputStream 的readObject的调用栈：
			readObject--->readObject0--->readOrdinaryObject--->checkResolve
		(2).看下 readOrdinaryObject方法的代码片段			
				/**
				 * 这里创建的这个obj对象，就是本方法要返回的对象，也可以暂时理解为是ObjectInputStream的readObject返回的对象
				 * (1).isInstantiable：如果一个serializable/externalizable的类可以在运行时被实例化，那么该方法就返回true。
				 * 针对serializable和externalizable我会在其他文章中介绍。
				 * (2).desc.newInstance：该方法通过反射的方式调用无参构造方法新建一个对象
				 */
				Object obj;
		        try {
		            obj = desc.isInstantiable() ? desc.newInstance() : null;
		        } catch (Exception ex) {
		            throw (IOException) new InvalidClassException(
		                desc.forClass().getName(),
		                "unable to create instance").initCause(ex);
		        }
	        ==> 结论:为什么序列化可以破坏单例了?
	        		序列化会通过反射调用无参数的构造方法创建一个新的对象
	        	/**
	        	 * hasReadResolveMethod:如果实现了serializable 或者 externalizable接口的类中包含readResolve则返回true
				 * invokeReadResolve:通过反射的方式调用要被反序列化的类的readResolve方法
	        	 */
		        if (obj != null &&
	                handles.lookupException(passHandle) == null &&
	                desc.hasReadResolveMethod())
	            {
	                Object rep = desc.invokeReadResolve(obj);
	                if (unshared && rep.getClass().isArray()) {
	                    rep = cloneArray(rep);
	                }
	                if (rep != obj) {
	                    handles.setObject(passHandle, obj = rep);
	                }
	            }
	           ==> 如何防止序列化/反序列化破坏单例模式
	           		在类中定义readResolve就可以解决该问题,定义的readResolve方法，
	           		并在该方法中指定要返回的对象的生成策略，就可以防止单例被破坏
	    /**
	     * 使用双重校验锁方式实现单例
	     */
	    public class Singleton implements Serializable{
	        private volatile static Singleton singleton;
	        private Singleton (){
	        	// 防止反射创建新的实例
	        	if (singleton != null){
		            throw new IllegalArgumentException("cannot exist two instance");
		        }
	        }
	        public static Singleton getSingleton() {
	            if (singleton == null) {
	                synchronized (Singleton.class) {
	                    if (singleton == null) {
	                        singleton = new Singleton();
	                    }
	                }
	            }
	            return singleton;
	        }
	        private Object readResolve() {
	            return singleton;
	        }
	    }
#### 4.单例与JVM垃圾回收:当一个单例的对象长久不用时,会不会被jvm的垃圾收集机制回收?(Hotspot 虚拟机)
	* 参考文章:http://blog.csdn.net/zhengzhb/article/details/7331354
	4.1.分析思路:
		(1).Hotspot 垃圾收集算法:GC Root
		(2).方法区的垃圾收集方法,JVM卸载类的判断方法

