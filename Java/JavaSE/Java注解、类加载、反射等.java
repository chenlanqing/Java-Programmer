一.java注解:Annotation(JDK5.0+)
1.内置注解,如:
	@Override:重写
	@Deprecated:过时
	@SuppressWarnings:取消警告
	(1).注解分为:
		按运行机制:源码注解,编译时注解,运行时注解
		按照来源注解:JDK,第三方,自定义注解;
2.自定义注解:使用 @interface 自定义注解时,自动集成了 Annotation 接口,要点如下:
	(1).@interface 用来声明一个注解: public @interface 注解名{};
	(2).其中的每一个方法实际上是声明了一个配置参数:
		①.方法的名称就是参数的名称,无参无异常;
		②.返回值类型就是参数的类型(返回值只能是基本类型,Class, String, Enum);
		③.可以通过 default 来声明参数的默认值
		④.如果只有一个参数成员,一般参数名为:value;
		⑤.没有成员的注解是标识注解;
	// 注意:注解元素必须要有值,在定义注解元素时经常使用空字符串,0作为默认值

3.元注解:负责注解其他注解, Java 定义了四个标准的 meta-annotation 类型,用来提供对其他 annotation 类型作说明
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

4.解析注解:通过反射获取类、函数或成员上的运行时注解信息,从而实现动态控制程序运行的逻辑;


5.注解处理器:
注解处理器是一个在javac中的，用来编译时扫描和处理的注解的工具;
一个注解的注解处理器，以Java代码（或者编译过的字节码）作为输入，生成文件（通常是.java文件）作为输出。
这具体的含义什么呢？你可以生成Java代码！这些生成的Java代码是在生成的.java文件中，所以你不能修改已经存在的Java类，
例如向已有的类中添加方法。这些生成的Java文件，会同其他普通的手动编写的Java源代码一样被javac编译;
(1).虚处理器 AbstractProcessor:

二.Java 动态性:
1.Java 动态加载与静态加载:
	(1).编译时加载类是静态加载类:new 创建对象是静态加载类,在编译时刻时需要加载所有的可能使用到的类;
	(2).运行时刻加载类是动态加载(Class.forName(""));
	
2.反射机制:(Reflection)运行时加载,探知使用编译期间完全未知的类;
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
		
3.动态编译:Java6.0引入动态编译
	3.1.动态编译的两种方法：
		(1).通过Runtime调用javac,启动新的进程去操作;
		(2).通过Javacompiler动态编译;
	3.2.Javacompiler动态编译:
		JavaCompiler compile = ToolProvider.getSystemJavaCompiler();
		int result = compile.run(null, null, null, "F:/class/HelloWorld.java");
		 /*
		  * run(InputStream in, OutputStream out, OutputStream err, String... arguments);
		  * 第一个参数: 为Java编译器提供参数;
		  * 第二个参数: 得到Java编译器输出的信息;
		  * 第三个参数: 接受编译器的错误信息;
		  * 第四个参数: 可变参数,能传入一个或多个Java源文件
		  * 返回值: 0 表示编译成功, 1 表示编译失败;
		  */
	3.3.动态运行动态编译的Java类
		
4.动态执行Javascript(JDK6.0以上)
	4.1.脚本引擎:Java 应该程序可以通过一套固定的接口与脚本引擎交互,从而可以在Java平台上调用各种脚本语言;
	4.2.js接口:Rhino 引擎,使用Java语言的javascript开源实现;
	
		
5.Java 字节码操作
	5.1.Java  动态操作:字节码操作,反射
		字节码操作:动态生存新的类;
		优势:比反射开销小,性能高;
	
	5.2.常见的字节码操作类库:
		(1).BCEL:apache
		(2).ASM:轻量级的字节码操作框架,涉及到jvm底层的操作和指令
		(3).CGLIB:基于asm实现
		(4).javaasist:性能比较差,使用简单
	5.3.Javasist:
		
	
	
6.类加载器:
	/**
	 * 参考文章:
	 * http://www.hollischuang.com/archives/201
	 * http://www.hollischuang.com/archives/199
	 */
	6.1.ClassLoader:用来动态加载class文件到内存当中用的;
	6.2.Java 默认提供的三个 ClassLoader:
		(2.1).BootStrap ClassLoader: 称为启动类加载器,是Java类加载层次中最顶层的类加载器,负责加载JDK中的核心类库,
			Bootstrap 类加载器没有任何父类加载器，如果你调用String.class.getClassLoader()，会返回null，
			任何基于此的代码会抛出NUllPointerException异常。Bootstrap 加载器被称为初始类加载器;			
			如:rt.jar、resources.jar、charsets.jar等,可通过如下程序获得该类加载器从哪些地方加载了相关的jar或class文件:				
				URL[] urLs = Launcher.getBootstrapClassPath().getURLs();
				for (URL url : urLs) {
					System.out.println(url.toExternalForm());
				}				
				System.out.println(System.getProperty("sun.boot.class.path"));
		(2.2).Extension ClassLoader:称为扩展类加载器,负责加载Java的扩展类库,默认加载JAVA_HOME/jre/lib/ext/目下的所有jar;
			将加载类的请求先委托给它的父加载器，也就是 Bootstrap，如果没有成功加载的话，再从jre/lib/ext目录下或者
			java.ext.dirs系统属性定义的目录下加载类; Extension 加载器由 sun.misc.Launcher$ExtClassLoader 实现
		(2.3).App ClassLoader:称为系统类加载器(System 类加载器),负责加载应用程序 classpath 目录下的所有jar和 类文件;
			Application 类加载器是 Extension 类加载器的子加载器。通过 sun.misc.Launcher$AppClassLoader 实现
		★★注意:
			除了Java提供的默认的 ClassLoader 外,用户还可以自定义 ClassLoader,自定义的 ClassLoader 都必须继承
			自 java.lang.ClassLoader 类,也包括:Extension ClassLoader 和 App ClassLoader;
			但是 Bootstrap ClassLoader 不继承自 ClassLoader,其不是一个普通 Java 类,其由 C++编写,已嵌入到 JVM 内核;当 JVM 启动后,
			Bootstrap ClassLoader 也随着启动,负责加载完核心类库后,并构造 Extension ClassLoader 和 App ClassLoader 类加载器;
	6.3.ClassLoader 加载类原理:
		6.3.1.类加载器的工作原理基于三个机制：委托、可见性和单一性:
			(1).委托机制是指将加载一个类的请求交给父类加载器，如果这个父类加载器不能够找到或者加载这个类，那么再加载它;
			(2).可见性的原理是子类的加载器可以看见所有的父类加载器加载的类，而父类加载器看不到子类加载器加载的类;
				当一个类已经被 Application 类加载器加载过了，然后如果想要使用 Extension 类加载器加载这个类，
				将会抛出 java.lang.ClassNotFoundException 异常:
				System.out.println("Main.class.getClassLoader():" + Main.class.getClassLoader());
				Class.forName("classloader.Main", true, Main.class.getClassLoader().getParent());//抛出异常
			(3).单一性原理是指仅加载一个类一次，这是由委托机制确保子类加载器不会再次加载父类加载器加载过的类;
				正确理解类加载器能够帮你解决 NoClassDefFoundError 和 java.lang.ClassNotFoundException
		6.3.2.ClassLoader 使用的是双亲委托模型来搜索类的
			(1).每个 ClassLoader 实例都有一个父类加载器的引用:(不是继承的关系，是一个包含的关系)，
			虚拟机内置的类加载器(Bootstrap ClassLoader)本身没有父类加载器,但可以用作其它 ClassLoader 实例的的父类加载器;
			(2).当一个 ClassLoader 实例需要加载某个类时，它会试图亲自搜索某个类之前，先把这个任务委托给它的父类加载器;
			(3).这个过程是由上至下依次检查的:
				(①).首先由最顶层的类加载器 Bootstrap ClassLoader 试图加载,如果没加载到,
					则把任务转交给 Extension ClassLoader 试图加载;
				(②).如果也没加载到,则转交给 App ClassLoader 进行加载,如果它也没有加载得到的话,则返回给委托的发起者;
				(③).由它到指定的文件系统或网络等URL中加载该类。如果它们都没有加载到这个类时，
					则抛出 ClassNotFoundException 异常。否则将这个找到的类生成一个类的定义，
					并将它加载到内存当中，最后返回这个类在内存中的 Class 实例对象
		6.3.3.为什么要使用双亲委托这种模型呢?
			这样可以避免重复加载,当父亲已经加载了该类的时候,就没有必要 ClassLoader 再加载一次;
			E.G.
				String 来动态替代java核心api中定义的类型,这样会存在非常大的安全隐患;
				而双亲委托的方式,就可以避免这种情况,因为 String 已经在启动时就被引导类加载器(Bootstrcp ClassLoader)加载;
				所以用户自定义的 ClassLoader 永远也无法加载一个自己写的 String，除非你改变JDK中 ClassLoader 搜索类的默认算法;
		6.3.4.在JVM搜索类时,判断两个class是否相同:
			JVM 在判定两个 class是否相同时:不仅要判断两个类名是否相同,而且要判断是否由同一个类加载器实例加载的;
			只有两者同时满足的情况下,JVM 才认为这两个 class是相同的
		6.3.5.ClassLoader 的体系架构:
			(1).检查类是否已经加载顺序:自底向上
				Custom ClassLoader(自定义加载) --> App ClassLoader --> Extension ClassLoader --> Bootstrap ClassLoader
			(2).加载类顺序:
				Load JRE\lib\rt.jar或者 -Xbootclasspath 选项指定的jar包;
				Load JRE\lib\ext\*.jar或者 -Djava.ext.dirs指定目录下的jar包;
				Load CLASSPATH或Djava.class.path所指定目录下的jar包;
				通过java.lang.ClassLoader 的子类自定义加载class;				
			(3).验证加载顺序:代码如下
				◆测试1:
					ClassLoader loader = ClassLoaderDemo.class.getClassLoader();
					while(loader != null){
						System.out.println(loader);
						loader = loader.getParent();
					}
					System.out.println(loader);
					输出结果:
						sun.misc.Launcher$AppClassLoader@4bb8d481 --> ClassLoaderDemo的类加载器是AppClassLoader
						sun.misc.Launcher$ExtClassLoader@538787fd --> AppClassLoader的类加器是ExtClassLoader
						null --> ExtClassLoader的类加器是Bootstrap ClassLoader,因为 Bootstrap ClassLoader 不是一个普通的 Java 类
								 Bootstrap ClassLoader 使用 C++ 编写的
				◆测试2:将ClassLoaderDemo.class打包成ClassLoaderDemo.jar,放在JAVA_HOME/jre/lib/ext下,重新运行上述代码
					sun.misc.Launcher$ExtClassLoader@155787fd --> ClassLoader的委托模型机制,当我们要用ClassLoaderDemo.class
						这个类的时候,AppClassLoader在试图加载之前,先委托给 Bootstrcp ClassLoader,Bootstracp ClassLoader
						发现自己没找到，它就告诉 ExtClassLoader
					null --> ExtClassLoader的父类加载器是Bootstrap ClassLoader。
					
				◆测试3:用 Bootstrcp ClassLoader 来加载 ClassLoaderDemo.class			
					(1).在jvm追加如下参数:-Xbootclasspath/a:c:\ClassLoaderDemo.jar -verbose
					(2).将 ClassLoaderDemo.jar解压后，放到 JAVA_HOME/jre/classes目录下;
	6.4.ClassLoader 源码分析: http://www.hollischuang.com/archives/199
		6.4.1.类定义:public abstract class ClassLoader{} 是一个抽象类;
		6.4.2.loadClass()方法的实现:
			/**
			 * 使用指定的二进制名称来加载类，这个方法的默认实现按照以下顺序查找类:
			 * 1.调用 findLoadedClass(String)方法检查这个类是否被加载过;
			 * 2.使用父加载器调用loadClass(String)方法，如果父加载器为 Null，
			 * 类加载器装载虚拟机内置的加载器调用findClass(String)方法装载类， 
			 * 如果，按照以上的步骤成功的找到对应的类，并且该方法接收的resolve参数的值为 true,
			 * 那么就调用resolveClass(Class)方法来处理类。 ClassLoader 的子类最好覆盖 findClass(String)而不是这个方法。 
			 * 除非被重写，这个方法默认在整个装载过程中都是同步的（线程安全的）			
			 */			
			protected Class<?> loadClass(String name, boolean resolve)throws ClassNotFoundException{
		        synchronized (getClassLoadingLock(name)) {
		            // First, check if the class has already been loaded
		            Class<?> c = findLoadedClass(name);
		            if (c == null) {
		                long t0 = System.nanoTime();
		                try {
		                    if (parent != null) {
		                        c = parent.loadClass(name, false);
		                    } else {
		                        c = findBootstrapClassOrNull(name);
		                    }
		                } catch (ClassNotFoundException e) {
		                    // ClassNotFoundException thrown if class not found
		                    // from the non-null parent class loader
		                }
		                if (c == null) {
		                    // If still not found, then invoke findClass in order
		                    // to find the class.
		                    long t1 = System.nanoTime();
		                    c = findClass(name);
		                    // this is the defining class loader; record the stats
		                    sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
		                    sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
		                    sun.misc.PerfCounter.getFindClasses().increment();
		                }
		            }
		            if (resolve) {
		                resolveClass(c);
		            }
		            return c;
		        }
		    }
		    (1).方法的声明:protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException;
		    	该方法同包内和派生类中可用,返回值类型 Class
		    	String name要查找的类的名字，boolean resolve，一个标志，true 表示将调用resolveClass(c)`处理该类
		    (2).synchronized (getClassLoadingLock(name)):同步代码块
		    	①.getClassLoadingLock(name):为类的加载操作返回一个锁对象,这个方法这样实现:如果当前的 Classloader 
		    		对象注册了并行能力,方法返回一个与指定的名字className相关联的特定对象,否则,直接返回当前的 ClassLoader对象
		    		A.在ClassLoader类中有一个静态内部类ParallelLoaders，他会指定的类的并行能力
		    		B.果当前的加载器被定位为具有并行能力，那么他就给parallelLockMap定义，就是 new ConcurrentHashMap<>()，
		    		那么这个时候，我们知道如果当前的加载器是具有并行能力的,那么parallelLockMap就不是 Null;
		    		判断parallelLockMap是不是 Null,如果是 null,说明该加载器没有注册并行能力,那么我们没有必要给他一个加锁的对象
		    (3).Class<?> c = findLoadedClass(name);	检查该类是否已经被加载过
		    	如果该类已经被加载过，那么就可以直接返回该对象.如果该类没有被加载过，那么执行以下的加载过程:
		    	try {
                    if (parent != null) {
                        c = parent.loadClass(name, false);// 如果父加载器不为空，那么调用父加载器的loadClass方法加载类
                    } else {
                        c = findBootstrapClassOrNull(name);// 如果父加载器为空，那么调用虚拟机的加载器来加载类
                    }
                } catch (ClassNotFoundException e) {
                    // ClassNotFoundException thrown if class not found
                    // from the non-null parent class loader
                }
            (4).如果以上两个步骤都没有成功的加载到类,那么:
            	c = findClass(name);调用自己的findClass(name)方法来加载类
            (5).已经得到了加载之后的类,那么就根据resolve的值决定是否调用resolveClass方法。resolveClass方法的作用是
            	链接指定的类:这个方法给 Classloader 用来链接一个类，如果这个类已经被链接过了，
            	那么这个方法只做一个简单的返回.否则,这个类将被按照 Java™ 规范中的 Execution 描述进行链接。
        6.4.3.类装载器 ClassLoader(一个抽象类)描述一下 JVM 加载 .class文件的原理机制
        	类装载器就是寻找类或接口字节码文件进行解析并构造JVM内部对象表示的组件,在java中类装载器把一个类装入JVM，经过以下步骤：
        	1、装载：查找和导入Class文件 
        	2、链接：其中解析步骤是可以选择的 
        		A.检查：检查载入的class文件数据的正确性 
        		B.准备：给类的静态变量分配存储空间 
        		C.解析：将符号引用转成直接引用 
        	3、初始化：对静态变量，静态代码块执行初始化工作
	6.5.自定义类加载器:
		(4.1).自定义类加载器的作用:
			Java 中提供的默认 ClassLoader,只加载指定目录下的jar和class,如果我们想加载其它位置的类或jar时,
			比如：我要加载网络上的一个class文件,通过动态加载到内存之后,要调用这个类中的方法实现我的业务逻辑.
			在这样的情况下，默认的 ClassLoader 就不能满足我们的需求了，所以需要定义自己的 ClassLoader;
			
		(4.2).如何自定义类加载器:
			(1).继承 java.lang.ClassLoader;
			(2).重写父类的 findClass方法;[NetworkClassLoader.java]
				JDK 已经在loadClass方法中帮我们实现了 ClassLoader 搜索类的算法,当在loadClass方法中搜索不到类时,
				loadClass方法就会调用findClass方法来搜索类,所以我们只需重写该方法即可。如没有特殊的要求，
				一般不建议重写loadClass搜索类的算法
				public class NetworkClassLoader extends ClassLoader {					
					private String rootUrl;
					public NetworkClassLoader(String rootUrl) {
						this.rootUrl = rootUrl;
					}
					@Override
					protected Class<?> findClass(String name) throws ClassNotFoundException {
						Class clazz = null;//this.findLoadedClass(name); // 父类已加载	
						//if (clazz == null) {	//检查该类是否已被加载过
							byte[] classData = getClassData(name);	//根据类的二进制名称,获得该class文件的字节码数组
							if (classData == null) {
								throw new ClassNotFoundException();
							}
							clazz = defineClass(name, classData, 0, classData.length);	//将class的字节码数组转换成Class类的实例
						//} 
						return clazz;
					}
				}
	6.6.显示的加载类:
		Class.forName(classname)
		Class.forName(classname, initialized, classloader)
		通过调用 java.lang.ClassLoader 的 loadClass()方法，而 loadClass()方法则调用了findClass()方法来定位相应类的字节码
	6.7.什么时候使用类加载器:
		类加载器是个很强大的概念，很多地方被运用。最经典的例子就是 AppletClassLoader，它被用来加载 Applet 使用的类，
		而 Applets 大部分是在网上使用，而非本地的操作系统使用。使用不同的类加载器，你可以从不同的源地址加载同一个类，
		它们被视为不同的类。J2EE 使用多个类加载器加载不同地方的类，例如WAR文件由 Web-app 类加载器加载，而 EJB-JAR
		中的类由另外的类加载器加载。有些服务器也支持热部署，这也由类加载器实现。你也可以使用类加载器来加载数据库或者
		其他持久层的数据
	6.8.Java 类的加载过程(http://www.importnew.com/18548.html)
		6.8.1.类生命周期:加载(Loading)、验证(Verification)、准备(Preparation)、解析(Resolution)、
			初始化(Initialization)、使用(Using)和卸载(Unloading)7个阶段,准备、验证、解析3个部分统称为连接			
		6.8.2.加载:在加载阶段,虚拟机需要完成以下3件事情:
			(1).通过类的全名产生对应类的二进制数据流。（如果没找到对应类文件，只有在类实际使用时才抛出错误。）
			(2).分析并将这些二进制数据流转换为方法区(JVM 的架构：方法区、堆，栈，本地方法栈，pc 寄存器)特定的数据结构（
				这些数据结构是实现有关的，不同 JVM 有不同实现）。这里处理了部分检验，比如类文件的魔数的验证，检查文件是
				否过长或者过短，确定是否有父类（除了 Obecjt 类）。
			(3).创建对应类的 java.lang.Class 实例(注意，有了对应的 Class 实例，并不意味着这个类已经完成了加载链链接！)
			==> 加载与链接阶段可能是交叉进行的,加载阶段尚未完成,连接阶段可能已经开始，但这些夹在加载阶段之中进行的动作，
				仍然属于连接阶段的内容;
		6.8.3.验证:连接阶段的第一步,这一阶段的目的是为了确保 Class 文件的字节流中包含的信息符合当前虚拟机的要求,
			并且不会危害虚拟机自身的安全; 验证阶段大致会完成 4 个阶段的检验动作
			(1).文件格式验证:验证字节流是否符合Class文件格式的规范;例如:是否以魔术0xCAFEBABE开头、主次版本号是否
				在当前虚拟机的处理范围之内、常量池中的常量是否有不被支持的类型;
			(2).元数据验证:对字节码描述的信息进行语义分析(注意：对比javac编译阶段的语义分析)，以保证其描述的信息
				符合Java语言规范的要求;例如:这个类是否有父类，除了 java.lang.Object 之外
			(3).字节码验证:通过数据流和控制流分析,确定程序语义是合法的、符合逻辑的;
			(4).符号引用验证:确保解析动作能正确执行;
			验证阶段是非常重要的，但不是必须的，它对程序运行期没有影响,
			可以考虑采用 -Xverifynone 参数来关闭大部分的类验证措施
		6.8.4.准备:
			(1).是正式为类变量分配内存并设置类变量初始值的阶段,变量所使用的内存都将在方法区中进行分配:
				这时候进行内存分配的仅包括类变量(static 修饰的变量)
			(2).假设一个类变量的定义为:public static int value=123;
				那变量value在准备阶段过后的初始值为 0 而不是 123.
				因为这时候尚未开始执行任何java方法，而把value赋值为123的putstatic指令是程序被编译后,
				存放于类构造器()方法之中,所以把value赋值为123的动作将在初始化阶段才会执行;
				static 引用类型为 null,其他都是默认值,int 默认为 0,boolean 默认为 false;
			(3).特殊情况是指:public static final int value=123,即当类字段的字段属性是ConstantValue时,会在准备阶段
				初始化为指定的值,所以标注为 final 之后，value的值在准备阶段初始化为123而非0.
		6.8.5.解析:解析阶段是虚拟机将常量池内的符号引用替换为直接引用的过程。解析动作主要针对类或接口、字段、类方法、
			接口方法、方法类型、方法句柄和调用点限定符7类符号引用进行;
		6.8.6.初始化:类初始化阶段是类加载过程的最后一步,真正开始执行类中定义的java程序代码
			(1).初始化阶段是执行类构造器<clinit>()方法的过程.
				<clinit>()方法是由编译器自动收集类中的所有类变量的赋值动作和静态语句块 static{}中的语句合并产生的
				编译器收集的顺序是由语句在源文件中出现的顺序所决定的;
			(2).<clinit>()方法与实例构造器<init>()方法不同，它不需要显示地调用父类构造器，虚拟机会保证在
				子类<init>()方法执行之前，父类的<clinit>()方法方法已经执行完毕;
			(3).<clinit>()方法对于类或者接口来说并不是必需的，如果一个类中没有静态语句块，也没有对变量的赋值操作，
				那么编译器可以不为这个类生产<clinit>()方法;
			(4).接口中不能使用静态语句块，但仍然有变量初始化的赋值操作，因此接口与类一样都会生成<clinit>()方法。
				但接口与类不同的是，执行接口的<clinit>()方法不需要先执行父接口的<clinit>()方法。只有当父接口中定
				义的变量使用时，父接口才会初始化。另外，接口的实现类在初始化时也一样不会执行接口的<clinit>()方法;
			(5).虚拟机会保证一个类的<clinit>()方法在多线程环境中被正确的加锁、同步,如果多个线程同时去初始化一个类，
				那么只会有一个线程去执行这个类的<clinit>()方法，其他线程都需要阻塞等待，直到活动线程执行<clinit>()方法完毕;
				其他线程虽然会被阻塞，但如果执行<clinit>()方法的那条线程退出<clinit>()方法后，其他线程唤醒之后不会再次进入<clinit>()方法。
				同一个类加载器下，一个类型只会初始化一次;
			(6).虚拟机规定有且只有5中情况(jdk1.7)必须对类进行“初始化”:
				①.遇到new,getstatic,putstatic,invokestatic这失调字节码指令时,如果类没有进行过初始化,则需要先触发其初始化;
				②.使用java.lang.reflect包的方法对类进行反射调用的时候，如果类没有进行过初始化，则需要先触发其初始化;
				③.当初始化一个类的时候，如果发现其父类还没有进行过初始化，则需要先触发其父类的初始化
				④.当虚拟机启动时，用户需要指定一个要执行的主类（包含main()方法的那个类），虚拟机会先初始化这个主类;
				⑤.当使用jdk1.7动态语言支持时，如果一个java.lang.invoke.MethodHandle实例最后的解析结果REF_getstatic,
				REF_putstatic,REF_invokeStatic的方法句柄，并且这个方法句柄所对应的类没有进行初始化，则需要先出触发其初始化
			(7).不触发类的初始化:
				①.通过子类引用父类的静态字段，不会导致子类初始化;
				②.通过数组定义来引用类，不会触发此类的初始化
				③.常量在编译阶段会存入调用类的常量池中,本质上并没有直接引用到定义常量的类,因此不会触发定义常量的类的初始化
	6.8.Java 类的链接:将 Java 类的二进制代码合并到 JVM 的运行状态之中的过程。在链接之前，这个类必须被成功加载
		分为三部分：verification检测, preparation准备 和 resolution解析:
		(1).verification 检测:
			验证是用来确保 Java 类的二进制表示在结构上是完全正确的.如果验证过程出现错误的话,会抛出 java.lang.VerifyError 错误;
			linking的resolve会把类中成员方法、成员变量、类和接口的符号引用替换为直接引用,而在这之前,需要检测被引用的类型
			正确性和接入属性是否正确(就是 public ,private 的的问题)诸如:检查 final class没有被继承，检查静态变量的正确性等等;
		(2).preparation准备:
			①.准备过程则是创建Java类中的静态域，并将这些域的值设为默认值。准备过程并不会执行代码。在一个Java类中
				会包含对其它类或接口的形式引用，包括它的父类、所实现的接口、方法的形式参数和返回值的Java类等。
			②.对类的成员变量分配空间。虽然有初始值，但这个时候不会对他们进行初始化（因为这里不会执行任何 Java 代码）。
				具体如下：所有原始类型的值都为 0。如 float: 0f, int: 0, boolean: 0(注意 boolean 底层实现大多使用 int)，
				引用类型则为 null。值得注意的是，JVM 可能会在这个时期给一些有助于程序运行效率提高的数据结构分配空间;
		(3).resolution解析:解析的过程就是确保这些被引用的类能被正确的找到。解析的过程可能会导致其它的Java类被加载
			可以在符号引用第一次被使用时完成，即所谓的延迟解析(late resolution)。但对用户而言，这一步永远是延迟解析的，
			即使运行时会执行 early resolution，但程序不会显示的在第一次判断出错误时抛出错误，而会在对应的类第一次主
			动使用的时候抛出错误！
	6.10.Java 类的初始化: 类的初始化也是延迟的，直到类第一次被主动使用(active use)，JVM 才会初始化类
		(1).初始化过程的主要操作是"执行静态代码块"和"初始化静态域"。在一个类被初始化之前，它的"直接父类"也需要被初始化。
			但是,一个接口的初始化,不会引起其父接口的初始化.在初始化的时候,会按照源代码中"从上到下"的顺序依次"执
			行静态代码块和初始化静态域"
		(2).如果基类没有被初始化，初始化基类。
			有类构造函数，则执行类构造函数。
			类构造函数是由 Java 编译器完成的。它把类成员变量的初始化和 static 区间的代码提取出，放到一个<clinit>方法中。
			这个方法不能被一般的方法访问(注意，static final 成员变量不会在此执行初始化，它一般被编译器生成 constant 值)。
			同时，<clinit>中是不会显示的调用基类的<clinit>的，因为 1 中已经执行了基类的初始化。
			该初始化过程是由 Jvm 保证线程安全的
		(3).Java 类和接口的初始化只有在特定的时机才会发生，这些时机包括：
			创建一个Java类的实例,如:
				MyClass obj = new MyClass()
			调用一个Java类中的静态方法,如:
				MyClass.sayHello()
			给Java类或接口中声明的静态域赋值,.如:
				MyClass.value = 10
			访问Java类或接口中声明的静态域，并且该域不是常值变量,如:
				int value = MyClass.value
			在顶层Java类中执行assert语句。
			==> 通过 Java 反射 API 也可能造成类和接口的初始化.需要注意的是,当访问一个 Java 类或接口中的静态域的时候，
				只有真正声明这个域的类或接口才会被初始化
		(4).对于静态字段,只有直接定义这个字段的类才会被初始化,如果子类调用父类的静态字段,
			只会触发父类的初始化而不会触发子类的初始化
	6.11.如下例子:http://www.importnew.com/18566.html
		public class StaticTest{
		    public static void main(String[] args){
		        staticFunction();
		        /**输出结果:
		         * 2
		         * 3
		         * a=110,b=0
		         * 1
		         * 4
		         */
		    }		 
		    static StaticTest st = new StaticTest();//对象的初始化是先初始化成员变量再执行构造方法		 
		    static{
		        System.out.println("1");
		    }		 
		    {
		        System.out.println("2");
		    }		 
		    StaticTest(){
		        System.out.println("3");
		        System.out.println("a="+a+",b="+b);
		    }		 
		    public static void staticFunction(){
		        System.out.println("4");
		    }		 
		    int a=110;
		    static int b =112;
		}
7.反射优化:反射是一种很重要的技术,然而它与直接调用相比性能要慢很多
	7.1.反射慢的原因:
		(1).编译器不能对代码对优化.
		(2).所有的反射操作都需要类似查表的操作,参数需要封装,解封装,异常也重新封装,rethrow等等
	7.2.优化方式:
		(1).灵活运用API,如果只是寻找某个方法,不要使用 getMethods() 后在遍历筛选,而是直接用
			getMethod(methodName)来根据方法名获取方法;
		(2).使用缓存:需要多次动态创建一个类的实例的时候.
		(3).使用代码动态生成技术,通过调用代理类的方式来模拟反射




















































