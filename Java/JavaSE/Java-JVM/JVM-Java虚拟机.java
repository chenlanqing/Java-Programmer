/**
 * http://www.importnew.com/19946.html
 * Java虚拟机内存优化实践-http://www.codeceo.com/article/java-jvm-memory.html
 * 深入理解JVM内幕：从基本结构到Java 7新特性-http://www.importnew.com/1486.html
 * Java内存分配- http://www.codeceo.com/article/java-object-memory.html
 * JVM 调优:http://www.codeceo.com/article/twitter-jvm-performance.html
 */
1.Java 代码编译和执行的整个过程: // http://www.codeceo.com/article/java-complie-run.html
	1.1.包含三个重要机制:
		(1).Java 源码编译机制:Java 源码编译有三个过程:
			==> 分析和输入到符号表
			==> 注解处理
			==> 语义分析和生成 class 文件{}
			生成的class文件包括:
				结构信息:包括 class文件格式版本号及各部分的数量与大小的信息.
				元数据:对应于 Java 源码中声明与常量的信息.包含类/继承的超类/实现的接口的声明信息、域与方法声明信息和常量池。
				方法信息:对应 Java 源码中语句和表达式对应的信息.包含字节码、异常处理器表、求值栈与局部变量区大小、求值栈的
				类型记录、调试符号信息
		(2).类加载机制:
			JVM 的类加载是通过 ClassLoader 及其子类来完成的;
			Bootstrap ClassLoader:
				负责加载$JAVA_HOME中jre/lib/rt.jar里所有的 class，由 C++ 实现，不是 ClassLoader 子类
			Extension ClassLoader:
				负责加载Java平台中扩展功能的一些 jar 包，包括$JAVA_HOME中jre/lib*.jar或-Djava.ext.dirs指定目录下的 jar 包 
			App ClassLoader:
				负责记载 classpath 中指定的 jar 包及目录中 class
			Custom ClassLoader:
				属于应用程序根据自身需要自定义的 ClassLoader，如 Tomcat、jboss 都会根据 J2EE 规范自行实现 ClassLoader。
		(3).类执行机制:
			JVM 是基于栈的体系结构来执行 class 字节码的{}。线程创建后，都会产生程序计数器（PC）和栈（Stack），
			程序计数器存放下一条要执行的指令在方法内的偏移量，栈中存放一个个栈帧，每个栈帧对应着每个方法的每次调用，
			而栈帧又是有局部变量区和操作数栈两部分组成，局部变量区用于存放方法中的局部变量和参数，操作数栈中用于存
			放方法执行过程中产生的中间结果;
	1.2.JVM 运行原理:类加载器 classloader + 执行引擎 execution engine + 运行时数据区域 runtime data area
		类加载器将 class 文件加载到{JVM}中的运行时数据区域,其不负责类的执行,由执行引擎执行;
2.Java 内存区域
	2.1.Java 虚拟机:
		虚拟机: 模拟某种计算机体系机构,执行特定指令集的软件
		2.1.1.进程虚拟机:JVM 等
		2.1.2.高级语言虚拟机:JVM, .NET CLR
		2.1.3.Java tm 虚拟机:
			(1).必须通过 Java TCK 的兼容性测试的 Java 虚拟机才能成为 "Java TM 虚拟机"
			(2).Java tm 虚拟机并非一定要执行 Java 语言
			(3).业界三大商用 JVM:Oracle HotSpot, Oracle JRockit VM, IBM J9 VM

	2.2.内存区域:Java 虚拟机规范将 JVM 所管理的内存分为以下几个运行时数据区:
		程序计数器、Java 虚拟机栈、本地方法栈、Java 堆、方法区;
		其中"程序计数器、Java 虚拟机栈、本地方法栈"三者是线程私有内存区;
		"Java 堆、方法区"为线程共享内存区
		详细关系查看:[Java虚拟机内存区域.png]
		2.2.1.程序计数器:无内存溢出异常(PC 寄存器)
			(1).一块较小的内存空间，它是当前线程所执行的字节码的行号指示器,字节码解释器工作时通过改变该计数器的值来选择下一
			条需要执行的字节码指令，分支、跳转、循环等基础功能都要依赖它来实现.
			(2).每条线程都有一个独立的的程序计数器,各线程间的计数器互不影响,因此该区域是线程私有的;
			(3).当线程在执行一个 Java 方法时，该计数器记录的是正在执行的虚拟机字节码指令的地址，当线程在执行的是 Native 
			方法(调用本地操作系统方法)时，该计数器的值为空;
			(4).该内存区域是唯一一个在 Java 虚拟机规范中没有规定任何 OOM(内存溢出：OutOfMemoryError)情况的区域
		2.2.2.Java 虚拟机栈:该区域也是线程私有的，它的生命周期也与线程相同
			2.2.2.1.虚拟机栈特征:
				(1).虚拟机栈描述的是Java方法执行的内存模型:
					每个方法被执行的时候都会同时创建一个栈帧,栈它是用于支持续虚拟机进行方法调用和方法执行的数据结构
				(2).栈帧:用于支持续虚拟机进行方法调用和方法执行的数据结构
					==> 对于执行引擎来讲,活动线程中,只有栈顶的栈帧是有效的,称为当前栈帧,这个栈帧所关联的方法称为当前方法，
					执行引擎所运行的所有字节码指令都只针对当前栈帧进行操作;
					==> 栈帧用于存储局部变量表、操作数栈、动态链接、方法返回地址和一些额外的附加信息;
					==> 一个栈帧需要分配多少内存，不会受到程序运行期变量数据的影响，而仅仅取决于具体的虚拟机实现;
			2.2.2.2.该区域存在的异常情况:
				(1).如果线程请求的栈深度大于虚拟机所允许的深度,将抛出 StackOverflowError 异常
				(2).如果虚拟机在动态扩展栈时无法申请到足够的内存空间,则抛出 OutOfMemoryError 异常
			2.2.2.3.栈帧中所存放的各部分信息的作用和数据结构:
				(1).局部变量表:是一组变量值存储空间,用于存放方法参数和方法内部定义的局部变量,存放的数据的类型是编译期
					可知的各种基本数据类型、对象引用(reference)和returnAddress(它指向了一条字节码指令的地址)类型;
					①.局部变量表所需的内存空间在编译期间完成分配,即在Java程序被编译成Class文件时,
						在方法的 Code 属性的 max_locals 数据项中确定了所需分配的最大局部变量表的容量,
						方法运行期间不会改变局部变量表的大小;
					②.局部变量表的容量以变量槽(Slot)为最小单位,虚拟机规范中没有明确指明一个 Slot ,应占用的内存空间大小,
					一个Slot可以存放一个32位以内的数据类型:
						boolean、byte、char、short、int、float、reference和returnAddresss;
					对于64位的数据类型(long 和 double),虚拟机会以高位在前的方式为其分配两个连续的 Slot 空间
					③.虚拟机通过索引定位的方式使用局部变量表,索引值的范围是从0开始到局部变量表最大的Slot数量,
						对于32位数据类型的变量,索引n代表第n个 Slot,对于64位的,索引n代表第n和第 n+1两个 Slot
						对于两个相邻的共同存放一个64位数据的两个 Slot,不允许采用任何方式单独访问其中一个,
						虚拟机规范中要求如果遇到这种操作的字节码序列,虚拟机在类加载的校验阶段抛出异常;
					④.在方法执行时,虚拟机是使用局部变量表来完成参数值到参数变量列表的传递过程的,如果是非静态方法,
						则局部变量表中的第0位索引的Slot默认是用于传递方法所属对象实例的引用,在方法中可以通过关键字 this 来
						访问这个隐含的参数,其余参数则按照参数表的顺序来排列，占用从1开始的局部变量Slot，参数表分配完毕后;
					⑤.类变量有两次赋初始值的过程:
						一次是在准备阶段,赋予系统初始值;
						另一次是在初始化阶段,赋予程序员定义的初始值
				(2).操作数栈:其最大深度在编译时写入到 Code 属性的 max_stacks 数据项中.
					①.32位数据类型所占的栈容量为1,64为数据类型所占的栈容量为2;
					②.当方法开始执行时,它的操作数栈是空的,在方法的执行过程中,会有各种字节码指令向操作栈中写于和提取内容
						即出栈和入栈
					③.Java 虚拟机是基于栈的,Android 虚拟机是基于寄存器的:
						基于栈的指令集最主要的优点是可移植性强,主要的缺点是执行速度相对会慢些;
						基于寄存器指令集最主要的优点是执行速度快,主要的缺点是可移植性差,寄存器由硬件直接提供;
					④.操作数栈中元素的数据类型必须与字节码指令的序列严格匹配,
				(3).动态连接:
					①.每个栈桢都包含一个指向运行时常量池中该栈桢所属方法的引用,持有该引用是支持方法调用过程的动态连接;
					②.Class 文件的常量池中存在有大量的符号引用,字节码中的方法调用指令就以常量池中指向方法的符号引用为参数;
						这些符号引用，一部分会在类加载阶段或第一次使用的时候转化为直接引用(如 final、static 域等)
						称为静态解析;另一部分将在每一次的运行期间转化为直接引用，这部分称为动态连接
				(4).方法返回地址:
					①.当一个方法被执行后,有两种方式退出该方法:
						执行引擎遇到了任意一个方法返回的字节码指令或遇到了异常,并且该异常没有在方法体内得到处理
						一般来说，方法正常退出时，调用者的PC计数器的值就可以作为返回地址，栈帧中很可能保存了这个计数器值，
						而方法异常退出时，返回地址是要通过异常处理器来确定的，栈帧中一般不会保存这部分信息;
					②.方法退出的过程实际上等同于把当前栈帧出,因此退出时可能执行的操作有:恢复上层方法的局部变量表和操作数栈,
						如果有返回值，则把它压入调用者栈帧的操作数栈中，调整PC计数器的值以指向方法调用指令后面的一条指令
		2.2.3.本地方法栈:使用到的本地操作系统（Native）方法服务
			与虚拟机栈发挥的作用类似;
		2.2.4.Java 堆:
			(1).Java 虚拟机所管理的内存中最大的一块,它是所有线程共享的一块内存区域.几乎所有的对象实例和数组都在这类分配内存.
			(2).Java Heap 是垃圾收集器管理的主要区域,因此很多时候也被称为"GC堆"
			(3).如果在堆中没有内存可分配时,并且堆也无法扩展时,将会抛出 OutOfMemoryError 异常
			(4).Java 堆可以处在物理上不连续的内存空间中,只要逻辑上是连续的即可;
			(5).其大小可以通过-Xmx和-Xms 来控制;
			(6).Java 堆分为新生代和老生代,新生代又被分为 Eden 和 Survivor 组成.对象主要分配在 Eden 区上
				新建的对象分配在新生代中.新生代大小可以由-Xmn 来控制,也可以用-XX:SurvivorRatio 来控制Eden和Survivor的比例;
				老生代存放新生代中经过多次垃圾回收(也即Minor GC)仍然存活的对象和较大内处对象
		2.2.5.方法区:和Java堆一样,是各个线程共享的内存区域,又被称为"永久代"(仅对HotSpot虚拟机来说)
			可以通过-XX:PermSize 和- XX:MaxPermSize 来指定最小值和最大值
			(1).它用于存储已经被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等数据;
			(2).它和 Java Heap 一样不需要连续的内存,虚拟机规范允许该区域可以选择不实现垃圾回收
				该区域的内存回收目标主要针是对废弃常量的和无用类的回收;
			(3).根据Java虚拟机规范的规定,当方法区无法满足内存分配需求时,将抛出 OutOfMemoryError 异常
		2.2.6.运行时常量池:方法区的一部分
			(1).Class文件中除了有类的版本、字段、方法、接口等描述信息外，还有一项信息是常量池(Class文件常量池),
			用于存放编译器生成的各种字面量和符号引用,这部分内容将在类加载后存放到方法区的运行时常量池中;
			(2).运行时常量池相对于Class文件常量池的另一个重要特征是具备动态性;
				运行期间也可能将新的常量放入池中，这种特性被开发人员利用比较多的是 String类的intern()方法;
		2.2.7.直接内存:其并不是虚拟机运行时数据区的一部分,也不是Java虚拟机规范中定义的内存区域
			它直接从操作系统中分配，因此不受Java堆大小的限制，但是会受到本机总内存的大小及处理器寻址空间的限制,
			因此它也可能导致 OutOfMemoryError 异常出现
3.内存溢出与内存泄漏:
	3.1.内存溢出:指程序所需要的内存超出了系统所能分配的内存(包括动态扩展)的上限
	3.2.内存泄漏:指分配出去的内存没有被回收回来,由于失去了对该内存区域的控制,因而造成了资源的浪费;
		Java 中一般不会产生内存泄露,因为有垃圾回收器自动回收垃圾,但这也不绝对,当我们 new {}了对象,并保存了其引用，
		但是后面一直没用它,而垃圾回收器又不会去回收它,这边会造成内存泄露
	3.3.内存溢出测试方法:
		(1).Java堆:无限循环的创建对象,在List中保存引用,以不被垃圾收集器回收;
		(2).方法区:生成大量的动态类,或无限循环调用 String 的intern()方法产生不同的String对象实例,并在List中保存其引用,
			以不被垃圾收集器回收;
		(3).虚拟机栈和本地方法栈:
			单线程:递归调用一个简单的方法,如不累积方法,会抛出 StackOverflowError
			多线程:无限循环的创建线程,并为每个线程无限循环的增加内存,会抛出 OutOfMemoryError
	3.4.通过参数优化虚拟机内存的参数如下所示:
		Xms:初始Heap大小
		Xmx:java heap最大值
		Xmn:young generation的heap大小
		Xss:每个线程的Stack大小
		XX:MinHeapFreeRatio=40
			Minimum percentage of heap free after GC to avoid expansion.
		XX:MaxHeapFreeRatio=70
			Maximum percentage of heap free after GC to avoid shrinking.
		XX:NewRatio=2
			Ratio of new/old generation sizes. [Sparc -client:8; x86 -server:8; x86 -client:12.]-client:8 (1.3.1+), x86:12]
		XX:NewSize=2.125m
			Default size of new generation (in bytes) [5.0 and newer: 64 bit VMs are scaled 30% larger; x86:1m; 
			x86, 5.0 and older: 640k]
		XX:MaxNewSize=
			Maximum size of new generation (in bytes). Since 1.4, MaxNewSize is computed as a function of NewRatio.
		XX:SurvivorRatio=25
			Ratio of eden/survivor space size [Solaris amd64: 6; Sparc in 1.3.1: 25; other Solaris platforms in 5.0 
			and earlier: 32]
		XX:PermSize=
			Initial size of permanent generation
		XX:MaxPermSize=64m
			Size of the Permanent Generation. [5.0 and newer: 64 bit VMs are scaled 30% larger; 1.4 amd64: 96m;
			 1.3.1 -client: 32m.]
	3.5.提高内存利用率,降低内存风险:
		(1).尽早释放无用对象的引用(XX = null;)
		(2).谨慎使用集合数据类型，如数组，树，图，链表等数据结构，这些数据结构对GC来说回收更复杂。
		(3).避免显式申请数组空间，不得不显式申请时，尽量准确估计其合理值。
		(4).尽量避免在类的默认构造器中创建、初始化大量的对象，防止在调用其自类的构造器时造成不必要的内存资源浪费
		(5).尽量避免强制系统做垃圾内存的回收，增长系统做垃圾回收的最终时间
		(6).尽量做远程方法调用类应用开发时使用瞬间值变量，除非远程调用端需要获取该瞬间值变量的值。
		(7).尽量在合适的场景下使用对象池技术以提高系统性能
	3.6.java.lang.OutOfMemoryError: PermGen Space:
		由于方法区主要存储类的相关信息.所以对于动态生成类的情况比较容易出现永久代的内存溢出
	3.7.Java8 移除永久代:
		(1).在JDK1.7中,已经把原本放在永久代的字符串常量池移出,放在堆中,因为使用永久代来实现方法区不是个好主意
			很容易遇到内存溢出的问题. 
		(2).我们通常使用 PermSize 和 MaxPermSize 设置永久代的大小, 这个大小就决定了永久代的上限, 但是我们不是总是知道应该
			设置为多大的, 如果使用默认值容易遇到 OOM 错误.这两个配置在 JDK8 后失效了
		(3).建议JVM的实现中将类的元数据放入 native memory, 将字符串池和类的静态变量放入java堆中. 这样可以加载多少类的元数据就不
			在由MaxPermSize控制, 而由系统的实际可用空间来控制
		(4).更深层的原因还是要合并HotSpot和JRockit的代码, JRockit 从来没有一个叫永久代的东西, 但是运行良好, 也不需要开发运维人员设
			置这么一个永久代的大小.
	3.8.元空间:
		(1).元空间本质和永久代类似,都是对JVM规范中方法区的实现.
		(2).元空间与永久代之间最大的区别在于:元空间并不在虚拟机中,而是使用本地内存.
		(3).默认情况下,元空间的大小仅受本地内存限制,但可以通过以下参数来指定元空间的大小:
			-XX:MetaspaceSize,初始空间大小,达到该值就会触发垃圾收集进行类型卸载,同时GC会对该值进行调整;
				如果释放了大量的空间,就适当降低该值;如果释放了很少的空间,那么在不超过MaxMetaspaceSize时,适当提高该值.
			-XX:MaxMetaspaceSize, 最大空间，默认是没有限制的;
			除了上面两个指定大小的选项以外，还有两个与 GC 相关的属性:
			-XX:MinMetaspaceFreeRatio,在GC之后,最小的Metaspace剩余空间容量的百分比,减少为分配空间所导致的垃圾收集
			-XX:MaxMetaspaceFreeRatio,在GC之后,最大的Metaspace剩余空间容量的百分比,减少为释放空间所导致的垃圾收集
4.对象实例化分析:
	Object obj = new Object();
	这段代码的执行会涉及 Java 栈、Java 堆、方法区三个最重要的内存区域;
	4.1.假设该语句出现在方法体中:
		obj 会作为引用类型的数据保存Java栈的本地变量中,而会在Java堆中保存该引用的实例化对象.
		Java 堆中还必须包含能查到此对象类型的地址信息(如对象类型,父类,实现的接口,方法等),这些类型数据保存在方法区中;
	4.2.访问Java堆栈中具体对象的位置:
		(1).使用句柄池访问:
			最大好处是reference中存放的是最稳定的句柄地址,在对象被移动时只会改变句柄中的实例数据指针.
			而reference本身不需要修改
		(2).直接使用指针:
			最大好处是速度快,节省了一次指针定位的时间开销.

5.Class 类文件结构: // http://www.importnew.com/19987.html
	3.1.平台无关性:虚拟机并不关心 Class 的来源是什么语言,只要它符合一定的结构,就可以在 Java 中运行
		Java 语言中的各种变量,关键字和运算符的语义最终都是由多条字节码命令组合而成的,
	3.2.类文件结构:
		3.2.1.Class 文件是一组以8位字节为基础单位的二进制流,各个数据项目严格按照顺序紧凑地排列在 Class 文件中,中间没有
			添加任何分隔符,这使得整个 Class 文件中存储的内容几乎全部都是程序运行的必要数据;根据Java虚拟机规范的规定,
			Class 文件格式采用一种类似于C语言结构体的伪结构来存储,这种伪结构中只有两种数据类型:无符号数和表
			(1).无符号数:无符号数属于基本数据类型,以 u1、u2、u4、u8 来分别代表 1、2、4、8 个字节的无符号数;
			(2).表:由多个无符号数或其他表作为数据项构成的符合数据类型,所有的表都习惯性地以"_info"结尾
		3.2.2.Class 文件格式:(查看图:"class类文件格式.jpg")
			magic,minor_version,major_version,constant_pool_count,constant_pool,access_flag
			this_class,super_class,interfaces_count,interfaces
			fields_count,fields,methods_count,methods,attributes_count,attributes
			从图中可以看出当需要描述同一类型但数量不定的多个数据时,经常会在其前面使用一个前置的容量计数器来记录其数量,
			而便跟着若干个连续的数据项,称这一系列连续的某一类型的数据为某一类型的集合;
			Class 文件中各数据项是按照上表的顺序和数量被严格限定的,每个字节代表的含义、长度、先后顺序都不允许改变
			(1).magic与version:
				①.每个Class文件的头4个字节称为魔数(magic),唯一作用是判断该文件是否为一个能被虚拟机接受的 Class 文件,
				它的值固定:0xCAFEBAB()
				②.紧接着magic的4个字节存储的是 Class 文件的次版本号和主版本号,高版本的JDK能向下兼容低版本的Class文件
				但不能运行更高版本的Class文件(即向下兼容)
			(2).constant_pool:major_version(主版本号)之后是常量池的入口,它是 Class 文件中与其他项目关联最多的数据类型,
				也是占用Class文件空间最大的数据项目之一
				①.常量池中主要存放两大类常量:字面量和符号引用;
					字面量比较接近于Java层面的常量概念,如文本字符串、被声明	为 final 的常量值等;
					符号引用总结起来则包括了下面三类常量:
						==> 类和接口的全限定名(即带有包名的 Class 名,如:java.lang.String)
						==> 字段的名称和描述符(private,static 等描述符);
						==> 方法的名称和描述符(private,static 等描述符);
					当虚拟机运行时,需要从常量池中获得对应的符号引用,再在类加载过程中的解析阶段将其替换为直接引用,
					并翻译到具体的内存地址中
				②.符号引用与直接引用的区别与关联:
					==> 符号引用:符号引用是以一组符号来描述所引用的目标,符号可以是任何形式的字面量,只要使用时能无歧义的定位
						到目标即可,符号引用与虚拟机实现的内存布局无关,引用的目标并不一定已经加载到内存中;
					==> 直接引用:直接引用可以是直接指向目标的指针,相对偏移量或者是一个能间接定位到目标的句柄;
						直接引用是与虚拟机实现的内存布局相关的,同一个符号引用在不同的虚拟机实例上翻译出来的直接
						引用一般不会相同;如果有了直接引用,那说明引用的目标必定已经存在于内存中了
				③.常量池中的每一项常量都是一个表,共有11种(JDK1.7之前)结构各不相同的表结构数据,每种表开始的第一位
					是一个u1类型的标志位(1-12，缺少2),代表当前这个常量属于的常量类型,在JDK1.7之后,为了更好的支持
					动态语言调用,又额外的增加了3中
			(3).access_flag:在常量池结束之后,紧接着的"2个字节"代表访问标志,
				这个标志用于识别一些类或接口层次的访问信息,包括:Class 是类还是接口,是否定义为 public 或 abstract 类型,
				如果是的话是否声明为 final.
				每种访问信息都由一个十六进制的标志值表示,如果同时具有多种访问信息,则得到的标志值为这几种访问信息的
				标志值的逻辑或;
			(4).this_class、super_class、interfaces
				①.this_class(类索引),super_class(父类索引)都是一个 u2 类型的数据,而interfaces(接口索引集合)
					则是一组u2类型的数据集合,Class 文件中由这三项数据来确定这个类的继承关系;
				②.类索引,父类索引和接口索引集合都按照顺序排列在访问标志之后,类索引和父类索引两个 u2 类型的索引值表示:
					它们各自执行一个类型为 CONSTANT_Class_info 的类描述符常量,通过该常量中的索引值找到定义在
					CONSTANT_Utf8_info 类型的常量中的全限定名字符串;
					而接口索引集合就用来描述这个类实现了哪些接口,这些被实现的接口将按 implements 语句(如果这个类本身是个接口,
					则应当是 extends 语句)后的接口顺序从左到右排列在接口的索引集合中
			(5).fields:字段表用于描述接口或类中声明的变量;字段包括了类级变量或实例级变量,但不包含在方法内声明的变量;
				字段的名字、数据类型、修饰符等都是无法固定的,只能引用常量池中的常量来描述
				+----------------------------------------------------+
				类型			名称				数量
				u2				access_flags		1
				u2				name_index			1
				u2				descriptor_index	1
				u2				attributes_count	1
				Attribute_info	attributes_count	attributes_count
				+----------------------------------------------------+
				access_flags:与类中的 access_flag非常类似
				name_index和descriptor_index都是对常量池的引用,分别代表字段的简单名称及字段和方法的描述符;
					注意:简单名称,描述符,全限定名:
						==> 简单名称:指没有类型或参数修饰的方法或字段名称
							private final static int m 	==> m
							boolean get(int index)		==> get()
						==> 描述符:是用来描述字段的数据类型、方法的参数列表(包括数量、类型以及顺序等)和返回值的
							B -> byte
							C -> char
							S -> short							
							I -> int
							F -> float
							D -> double
							J -> long
							Z -> boolean
							V -> void
							L -> 对象类型,一般表示:()Ljava/lang/Object;
							int[] -> [I
							int[][] -> [[I
							String[] -> [Ljava/lang/String
							用方法描述符描述方法时,按照先参数后返回值的顺序描述,参数要按照严格的顺序放在一组小括号内
				字段表包含的固定数据项目到descriptor_index为止就结束了,但它之后还紧跟着一个属性表集合用于存储一些额外的信息
				★注意:字段表集合中不会列出从父类或接口中继承而来的字段,但有可能列出原本Java代码中不存在的字段
					比如:比如在内部类中为了保持对外部类的访问性,会自动添加指向外部类实例的字段
			(6).methods:方法表的结构与属性表的结构相同;方法里的Java代码经过编译器编译程字节码指令后存放在方法属性表集合中
				一个名为"Code"的属性里;
				如果父类在子类中没有被覆盖,方法表集合中就不会出现来自父类的方法信息;但同样,有可能会出现由编译器自动添加
				的方法,最典型的便是类构造器"<clinit>"方法和实例构造器"<init>"方法
				重载的方法必须是拥有相同的简单名称和一个与原方法不同的特征签名,特征签名就是一个方法中各个参数在常量池中
				的字段符号引用的集合,也就是说返回值不会包含在特征签名内,因此方法重载跟方法返回值没有关系;
			(7).attributes:属性表,在 Class 文件、字段表、方法表中都可以携带自己的属性表集合
				Java 虚拟机运行时会忽略掉他不认识的属性.Java 虚拟机规范中预定义了9项虚拟机应当能识别的属性(JDK1.5之后新增了
				部分属性),9个基本属性, 对于每个属性,它的名称都需要从常量池中引用一个CONSTANT_Utf8_info类型的常量来表示,
				每个属性值的结构是完全可以自定义的,只需说明属性值所占用的位数长度即可,
				一个符合规则的属性表至少应具有"attribute_name_info"、"attribute_length"和至少一项信息属性
				(7.1)Code 属性:方法表,Java 代码编译成的字节码指令:Java 程序方法体经过Javac编译后,生成的字节码指令便会储存
					在 Code 属性中,但并非所有的方法表都必须存在这个属性,比如接口或抽象类的方法不存在 Code 属性;
					Code 属性的结构:()					
				(7.2)ConstantVlue:字段表,final 关键字定义的常量值,通知虚拟机自动为静态变量赋值,只有被 static 修饰的变量才
					可以使用这项属性
					①.在Java中,对非 static 类型的变量(也就是实例变量)的赋值是在实例构造器<init>方法中进行的;
						而对于类变量(static 变量),则有两种方式可以选择:在类构造其中赋值,或使用 ConstantValue 属性赋值
					②.目前 Sun Javac 编译器的选择是:如果同时使用 final 和 static 修饰一个变量(即全局变量),并且这个变
					量的数据类型是基本类型或 String 的话,就生成ConstantValue属性来进行初始化,在类加载的准备阶段虚拟机便会
					根据ConstantValue为常量设置相应的值,如果该变量没有被 final 修饰,或者并非基本类型及字符串,
					则选择在<clinit>方法中进行初始化
					==> 在实际的程序中,只有同时被 final 和 static 修饰的字段才有ConstantValue属性
					==> 注意:简要说明下 final、static、static final 修饰的字段赋值的区别
						★.final 修饰的字段在运行时才被初始化(可以直接赋值,也可以在实例构造器中赋值),一旦赋值不可更改;
						★.static 修饰的字段在类加载过程中的准备阶段被初始化 0 或 null 等默认值后,而后在初始化阶段(触发
							类构造器<clinit>)才会被赋予代码中指定的值,如果没有设定值,那么它的值就为默认值;
						★.static final 修饰的字段在 Javac 时生成 ConstantVlue 属性,在类加载准备阶段根据 ConstantVlue
						 的值为该字段赋值,它没有默认值,必须显式的赋值,否则Javac会报错,可以理解为在编译期即把结果放入常量池
				(7.3)Deprecated:类,方法表,字段表;被声明为 deprecated 的方法和字段
					该属性用于表示某个类、字段和方法,已经被程序作者定为不再推荐使用,它可以通过在代码中使用
					@Deprecated 注释进行设置
				(7.4)Exceptions:方法表,方法抛出的异常,作用是列举出方法中可能抛出的受查异常,也就是方法描述时在 throws 
					关键字后面列举的异常;结构很简单,
					只有attribute_name_index、attribute_length、number_of_exceptions、exception_index_table四项
				(7.5)InnerClass:类文件,内部类列表
					该属性用于记录内部类与宿主类之间的关联.如果一个类中定义了内部类,
					那么编译器将会为它及它所包含的内部类生成InnerClasses属性
				(7.6)LinerNumberTable:Code 属性,Java 源码的行号与字节码指令间的对应关系
					它用于描述Java源码行号与字节码行号之间的对应关系
				(7.7)LocalVariableTable:Code 属性,方法的局部变量描述
					用于描述栈帧中局部变量表中的变量与Java源码中定义的变量之间的对应关系
				(7.8)SourceFile:类文件,源文件名称
					它用于记录生成这个Class文件的源码文件名称
				(7.9)Synthetic:类,方法表,字段表;标示类,方法,字段等是编译器自动生成的;
					该属性代表此字段,方法并不是Java源代码直接生成的,而是由编译器自行添加的,如this和实例构造器、类构造器等
6.虚拟机类加载机制:
	类加载机制:
		虚拟机把描述类的数据从Class文件加载到内存,并对数据进行校验、转换解析和初始化,最终形成可以被虚拟机直接使用的
		Java 类型.
	/**
	 * 参考文章:
	 * http://www.hollischuang.com/archives/201
	 * http://www.hollischuang.com/archives/199
	 */
	6.1.ClassLoader:用来动态加载class文件到内存当中用的;
		从 Java 虚拟机的角度来讲,只存在两种不同的类加载器:一种是启动类加载器,这个类加载器是使用C++语言实现的(仅限于
			HotSpot 虚拟机),是虚拟机自身的一部分,另一种就是所有其他的类加载器,都是由Java语言实现的,独立于虚拟机外部,
		全都继承自抽象类:java.lang.ClassLoader
	6.2.Java 默认提供的三个 ClassLoader:
		(2.1).BootStrap ClassLoader: 称为启动类加载器,是Java类加载层次中最顶层的类加载器,负责加载JDK中的核心类库,
			Bootstrap 类加载器没有任何父类加载器，如果你调用String.class.getClassLoader()，会返回null，
			任何基于此的代码会抛出NUllPointerException异常。Bootstrap 加载器被称为初始类加载器;
			如:rt.jar、resources.jar、charsets.jar等,可通过如下程序获得该类加载器从哪些地方加载了相关的jar或class文件:
			将存放在 JAVA_HOME\lib 目录下或者被-Xbootclasspath 参数所指定的路径中,并且是虚拟机识别的类加载虚拟机内存中
			URL[] urLs = Launcher.getBootstrapClassPath().getURLs();
			for (URL url : urLs) {
				System.out.println(url.toExternalForm());
			}				
			System.out.println(System.getProperty("sun.boot.class.path"));
		(2.2).Extension ClassLoader:称为扩展类加载器,负责加载Java的扩展类库,默认加载JAVA_HOME/jre/lib/ext/目下的所有jar;
			将加载类的请求先委托给它的父加载器，也就是 Bootstrap，如果没有成功加载的话，再从jre/lib/ext目录下或者
			java.ext.dirs系统属性定义的目录下加载类; Extension 加载器由 sun.misc.Launcher$ExtClassLoader 实现
		(2.3).App ClassLoader:称为系统类加载器(System 类加载器),负责加载应用程序 classpath 目录下的所有jar和 类文件;
			Application 类加载器是 Extension 类加载器的子加载器。通过 sun.misc.Launcher$AppClassLoader 实现;
			这个类加载器是 ClassLoader 中 getSystemClassLoader() 方法的返回值,所以也称为系统类加载器
		★★注意:
			除了Java提供的默认的 ClassLoader 外,用户还可以自定义 ClassLoader,自定义的 ClassLoader 都必须继承
			自 java.lang.ClassLoader 类,也包括:Extension ClassLoader 和 App ClassLoader;
			但是 Bootstrap ClassLoader 不继承自 ClassLoader,其不是一个普通 Java 类,其由 C++编写,已嵌入到 JVM 内核;
			当 JVM 启动后,Bootstrap ClassLoader 也随着启动,负责加载完核心类库后,并构造 Extension ClassLoader 和
			App ClassLoader 类加载器;
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
			(1).双亲委派模型要求除了顶层的启动类加载器(Bootstrap ClassLoader)外,其余的类加载器都应该有自己的父类加载器,
				这里的类加载器之间的父子关系不是以继承的关系来实现的,而是都使用组合关系来复用父加载器的代码;
				启动类加载器(Bootstrap ClassLoader)本身没有父类加载器,但可以用作其它 ClassLoader 实例的的父类加载器;
			(2).当一个 ClassLoader 实例需要加载某个类时，它会试图亲自搜索某个类之前，先把这个任务委托给它的父类加载器;
			(3).这个过程是由上至下依次检查的:
				如果一个类加载器收到了类加载的请求,首先并不会自己去尝试加载这个类,而是把这个请求委派给父类加载器去完成,
				每一个层次的类加载器都是如此,因此所有的加载请求最终都应该传送到顶层的启动类加载器中,只有当父类加载器
				反馈自己无法完成这个加载请求时,子类才会尝试自己去加载;
				(①).首先由最顶层的类加载器 Bootstrap ClassLoader 试图加载,如果没加载到,
					则把任务转交给 Extension ClassLoader 试图加载;
				(②).如果也没加载到,则转交给 App ClassLoader 进行加载,如果它也没有加载得到的话,则返回给委托的发起者;
				(③).由它到指定的文件系统或网络等URL中加载该类。如果它们都没有加载到这个类时，
					则抛出 ClassNotFoundException 异常。否则将这个找到的类生成一个类的定义，
					并将它加载到内存当中，最后返回这个类在内存中的 Class 实例对象
		6.3.3.为什么要使用双亲委托这种模型呢?
			使用双亲委拖好处:Java 类随着它的类加载器一起具备了一种带有优先级的层次关系.
			如类:java.lang.Object,存放在 rt.jar 中,无论哪一个类加载器要加载这个类,最终都是委派给处于模型最顶端的
			启动类加载器进行加载,因此 Object 类在程序各种类加载器环境都是同一个类
			这样可以避免重复加载,当父亲已经加载了该类的时候,就没有必要 ClassLoader 再加载一次;
			E.G.
				String 来动态替代java核心api中定义的类型,这样会存在非常大的安全隐患;
				而双亲委托的方式,就可以避免这种情况,因为 String 已经在启动时就被引导类加载器(Bootstrcp ClassLoader)加载;
				所以用户自定义的 ClassLoader 永远也无法加载一个自己写的 String，除非你改变JDK中 ClassLoader 
				搜索类的默认算法;
		6.3.4.在JVM搜索类时,判断两个class是否相同:
			JVM 在判定两个 class是否相同时:不仅要判断两个类名是否相同,而且要判断是否由同一个类加载器实例加载的;
			只有两者同时满足的情况下,JVM 才认为这两个 class是相同的;
			如果两个类来源于同一个 Class 文件,被同一个虚拟机加载,只要加载它们的类加载器不同,那这两个类必定不相等;
			这里的"相等"包括代表类的Clas 对象的equals()方法、isAssignaleFrom()方法、isInstance()方法的返回结果,
			也包括使用 instanceof 关键字做对象所属关系判定等情况;
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
						null --> ExtClassLoader的类加器是Bootstrap ClassLoader,因为 Bootstrap ClassLoader 不是一个普通的 
								Java 类 Bootstrap ClassLoader 使用 C++ 编写的
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
			 * 双亲委派模型的实现:
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
	    		那么这个时候，我们知道如果当前的加载器是具有并行能力的,那么parallelLockMap就不是 Null;判断
	    		parallelLockMap是不是 Null,如果是 null,说明该加载器没有注册并行能力,那么我们没有必要给他一个加锁的对象
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
        	类装载器就是寻找类或接口字节码文件进行解析并构造JVM内部对象表示的组件,在java中类装载器把一个类装入JVM,
        	经过以下步骤：
        	1)、装载：查找和导入Class文件 
        	2)、链接：其中解析步骤是可以选择的 
        		A.检查：检查载入的class文件数据的正确性 
        		B.准备：给类的静态变量分配存储空间 
        		C.解析：将符号引用转成直接引用 
        	3)、初始化：对静态变量，静态代码块执行初始化工作
		6.4.4.破坏双亲委派模型:
			(1).在JDK1.2发布之前,双亲委派模型是在JDK1.2之后引入的,而类加载器和抽象类 ClassLoader 在JDK1.1时代就已
				存在,面对已经存在的用户自定义类加载器的代码实现,作出了妥协
				JDK1.2 后的 ClassLoader 添加了 protected 方法 findClass().用户重写的唯一目的就是重写 loadClass()
				方法.			
			(2).线程上下文类加载器:这个类加载器可以通过 Thread 类的 setContextClassLoader()方法进行设置,如果创建线程
				时还为设置,它将会从父线程中继承一个,如果在应用程序的全局范围内都没有设置过的话,这个类加载器默认就是
				应用程序类加载器; (Spring的应:http://blog.csdn.net/u013095337/article/details/53609398)
				有了线程上下文类加载器,JNDI 服务使用这个线程上下文类加载器去加载所需要的SPI代码,也就是父类加载器
				请求子类加载器去完成类加载的工作,这种行为实际是打通双亲委派模型的层次结构来逆向使用类加载器
			(3).代码热替换、模块热部署等

	6.5.自定义类加载器:
		(6.5.1).自定义类加载器的作用:
			Java 中提供的默认 ClassLoader,只加载指定目录下的jar和class,如果我们想加载其它位置的类或jar时,
			比如：我要加载网络上的一个class文件,通过动态加载到内存之后,要调用这个类中的方法实现我的业务逻辑.
			在这样的情况下，默认的 ClassLoader 就不能满足我们的需求了，所以需要定义自己的 ClassLoader;			
		(6.5.2).如何自定义类加载器:
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
							//将class的字节码数组转换成Class类的实例
							clazz = defineClass(name, classData, 0, classData.length);	
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
	// http://www.importnew.com/18548.html
	6.8.Java 类类生命周期:加载(Loading)、验证(Verification)、准备(Preparation)、解析(Resolution)、初始化(Initialization)、
		使用(Using)和卸载(Unloading)7个阶段,准备、验证、解析3个部分统称为连接,
		其中"类加载"的过程包括了加载、验证、准备、解析、初始化五个阶段,
		加载、验证、准备和初始化这四个阶段发生的顺序是确定的
		而解析阶段则不一定,它在某些情况下可以在初始化阶段之后开始,这是为了支持Java语言的运行时绑定
		★.注意这里的几个阶段是按顺序开始.而不是按顺序进行或完成
		★.动态绑定和静态绑定:绑定指的是把一个方法的调用与方法所在的类(方法主体)关联起来
			==> 动态绑定:即晚期绑定,也叫运行时绑定.在运行时根据具体对象的类型进行绑定.在Java 中,几乎所有的方法都是动态绑定;
			==> 静态绑定:即前期绑定,在程序执行前方法已经被绑定,此时由编译器或其它连接程序实现.在Java中,可以理解为编译期绑定
				Java 当作只有 final,static,private 和构造方法是前期绑定的
		==> 注意:类加载过程中,除了在加载阶段用过可以通过自定义类加载器参与之外,其余的动作完全由虚拟机主导和控制
		6.8.1.加载:在加载阶段,虚拟机需要完成以下3件事情:
			(1).通过类的全名产生对应类的二进制数据流。(如果没找到对应类文件,只有在类实际使用时才抛出错误)
				这里第1条中的二进制字节流并不只是单纯地从Class文件中获取，比如它还可以从Jar包中获取、从网络中获取(最典型的
				应用便是Applet)、由其他文件生成（JSP应用）等
			(2).分析并将这些二进制数据流转换为方法区(JVM 的架构：方法区、堆，栈，本地方法栈，pc 寄存器)特定的数据结构(
				这些数据结构是实现有关的，不同 JVM 有不同实现).这里处理了部分检验，比如类文件的魔数的验证，检查文件是
				否过长或者过短，确定是否有父类(除了 Obecjt 类)
			(3).创建对应类的 java.lang.Class 实例(注意，有了对应的 Class 实例，并不意味着这个类已经完成了加载链链接！)
			==> 加载与链接阶段可能是交叉进行的,加载阶段尚未完成,连接阶段可能已经开始，但这些夹在加载阶段之中进行的动作，
				仍然属于连接阶段的内容;
			★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
			数组的加载过程:数组类本身不通过类加载器创建,但数组类与类加载器仍然有密切联系,因为数组类的元素类型
			最终是要靠类加载器去创建,一个数组类的创建过程遵循如下原则:
				A.如果数组的组件类型是引用类型,那就递归采用上述定义的加载过程去加载这个组件类型,数组类将在
				加载该组件类型的类加载器的类名空间上被标识.
				B.如果数组的组件类型不是引用类型(如 int[]),Java 虚拟机会把数组类标记为与引导类加载器关联
				C.数组类的可见性与它的组件类型的可见性一致,如果组件类型不是引用类型,那数组类的可见性默认为 public
			★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
		6.8.2.验证:连接阶段的第一步,这一阶段的目的是为了确保 Class 文件的字节流中包含的信息符合当前虚拟机的要求,
			并且不会危害虚拟机自身的安全; 验证阶段大致会完成 4 个阶段的检验动作
			(1).文件格式验证:验证字节流是否符合Class文件格式的规范,并且能被当前版本的虚拟机处理,主要验证点:
				◆ 是否以魔术0xCAFEBABE开头;
				◆ 主次版本号是否在当前虚拟机的处理范围之内;
				◆ 常量池中的常量是否有不被支持的类型(检测常量 tag 标志);
				◆ 指向常量的各种索引值使用有执行不存在的常量或不符合类型的常量;
				◆ CONSTANT_Utf8_info 型常量中是否有不符合UTF8编码的数据;
				◆ Class 文件中各个部分及文件本身是否有被删除的或附件的其他信息;
				......
				该验证的主要目的是保证输入的字节流能正确的解析并存储与方法区之内,格式上符合描述的一个Java类型信息的
				要求.基于二进制字节流进行验证的,只有通过了这个阶段的验证之后,字节流才会进入内存的方法区进行存储,
				后面的三个验证都是基于方法区的存储结构进行的,不会在直接操作字节流.
			(2).元数据验证:对字节码描述的信息进行语义分析(注意：对比javac编译阶段的语义分析)，以保证其描述的信息
				符合Java语言规范的要求;验证点如下:
				◆ 这个类是否有父类，除了 java.lang.Object 之外,所有的类都应当有父类;
				◆ 这个类的父类是否继承了不允许被继承的类(被 final 修饰的类);
				◆ 如果这个类不是抽象类,是否实现了其父类或接口之中要求实现的所有方法;
				◆ 类中的字段,方法是否与父类产生矛盾(例如覆盖了父类的 final 字段,出现不符合规则的方法重载);
				......
				主要目的是:对类的元数据进行语义校验,保证不存在不符合Java语言规范的元数据信息.
			(3).字节码验证:最复杂的阶段;
				主要目的:通过数据流和控制流分析,确定程序语义是合法的、符合逻辑的;
				这个阶段会类的方法体进行校验分析,保证被校验类的方法在运行时不会做出危害虚拟机安全的事件
				◆ 保证任意时刻操作数栈的数据类型与指令代码序列都能配合工作;
				◆ 保证跳转指令不会跳转到方法体以外的字节码指令上;
				◆ 保证方法体中的类型转换是有效的.
				通过程序去校验程序的逻辑是无法做到绝对正确的,不能通过程序准确的检查出程序是否能在有限的时间内结束运行
			(4).符号引用验证:确保解析动作能正确执行;它发生在虚拟机将符号引用转化为直接引用的时候,这个转换动作将在
				连接的第三个阶段-解析阶段中发生.
				符合引用验证可以看作是对类自身以外(常量池中各种符号引用)的信息进程匹配性校验.校验点:
				◆ 符号引用中用过字符串描述的全限定名是否能找到对应的类;
				◆ 在指定类中是否存在符合方法的字段描述以及简单名称所描述的方法和字段;
				◆ 符号引用中的类,字段,方法的访问性是否可被当前类访问
				......
				如果无法通过符号引用验证,那么将抛出一个 java.lang.IncompatibleClassChangeError 异常的子类.
				验证阶段是非常重要的，但不是必须的，它对程序运行期没有影响.如果所运行的的全部代码都已经被反复使用和
				验证,那么在实施阶段可以考虑采用 -Xverifynone 参数来关闭大部分的类验证措施,缩短虚拟机加载的时间
		6.8.3.准备:
			(1).是正式为类变量分配内存并设置类变量初始值的阶段,变量所使用的内存都将在方法区中进行分配:这时候进行内存分
				配的仅包括类变量(static 修饰的变量),而不包括实例变量,实例变量会在对象实例化时随着对象一块分配在Java堆中
			(2).这里所说的初始值"通常情况"下是数据类型的零值,假设一个类变量的定义为:public static int value=123;
				那变量value在准备阶段过后的初始值为 0 而不是 123.
				因为这时候尚未开始执行任何java方法，而把value赋值为123的putstatic指令是程序被编译后,
				存放于类构造器<clinit>()方法之中,所以把value赋值为123的动作将在初始化阶段才会执行;
				static 引用类型为 null,其他都是默认值,int 默认为 0,boolean 默认为 false;
				int -> 0	long -> 0L	short -> (short)0	char -> '\u0000'	byte -> (byte)0
				boolean -> false	float -> 0.0f 	double -> 0.0d 	reference -> null
			(3).特殊情况是指:public static final int value=123,即当类字段的字段属性是 ConstantValue时,会在准备阶段
				初始化为指定的值,所以标注为 final 之后，value的值在准备阶段初始化为123而非 0.
		6.8.4.解析:解析阶段是虚拟机将常量池内的符号引用替换为直接引用的过程.
			(1).虚拟机要求在执行 anewarray,checkcast,getfield,getstatic,instanceof,invokedynamic,invokeinterface,
				invokespecial,invokestatic,invokevirtual,ldc,ldc_w,multianewarray,new,putstatic,putfield{} 这16个
				用于操作符合引用的字节码指令之前,先对它们所使用的符号进行解析.
				虚拟机可以根据需要来判断是在类加载器加载时就对常量池中的符号引用进行解析,还是等到一个符号引用
				将要被使用前才去解析他;
			(2).除invokedynamic指令外,虚拟机实现可以对第一次解析的结果进行缓存(在运行时常量池中记录直接引用,并
				把常量标记为已解析状态)从而避免解析动作重复.无论是否真正执行了多次解析动作,虚拟机需要保证的是在
				同一个实体中.
			(3).解析动作主要针对类或接口、字段、类方法、接口方法、方法类型、方法句柄和调用点限定符7类符号引用进行;
				分别对应常量池的:CONSTANT_Class_info,CONSTANT_Fieldref_info,CONSTANT_Methodref_info,
				CONSTANT_InterfaceMethodref_info,CONSTANT_MethodType_info
				CONSTANT_MethodHandle_info,CONSTANT_InvokeDynamic_info.
					
		6.8.5.初始化:类初始化阶段是类加载过程的最后一步,真正开始执行类中定义的java程序代码
			(1).初始化阶段是执行类构造器<clinit>()方法的过程.
				<clinit>()方法是由编译器自动收集类中的所有类变量的赋值动作和静态语句块 static{}中的语句合并产生的,
				编译器收集的顺序是由语句在源文件中出现的顺序所决定的,静态语句块中只能访问到定义在静态语句块之前的变量,
				定义在它之后的变量,在前面的静态语句块可以赋值,但是并不能访问.
			(2).<clinit>()方法与实例构造器<init>()方法不同,它不需要显示地调用父类构造器,虚拟机会保证在
				子类<clinit>()方法执行之前,父类的<clinit>()方法方法已经执行完毕;
				因此在虚拟机中第一个被执行的<clinit>()方法类肯定是 java.lang.Object
				由于父类的<clinit>()方法先执行,也就意味着父类中定义的静态语句块要优先于子类的变量赋值操作.
			(3).<clinit>()方法对于类或者接口来说并不是必需的,如果一个类中没有静态语句块,也没有对类变量的赋值操作，
				那么编译器可以不为这个类生产<clinit>()方法;
			(4).接口中不能使用静态语句块，但仍然有变量初始化的赋值操作，因此接口与类一样都会生成<clinit>()方法。
				但接口与类不同的是，执行接口的<clinit>()方法不需要先执行父接口的<clinit>()方法。只有当父接口中定
				义的变量使用时，父接口才会初始化。另外，接口的实现类在初始化时也一样不会执行接口的<clinit>()方法;
			(5).虚拟机会保证一个类的<clinit>()方法在多线程环境中被正确的加锁、同步,如果多个线程同时去初始化一个类，
				那么只会有一个线程去执行这个类的<clinit>()方法，其他线程都需要阻塞等待，直到活动线程执行<clinit>()方法完毕;
				其他线程虽然会被阻塞，但如果执行<clinit>()方法的那条线程退出<clinit>()方法后，
				其他线程唤醒之后不会再次进入<clinit>()方法。同一个类加载器下，一个类型只会初始化一次;
			(6).虚拟机规定有且只有 5 种情况(jdk1.7)必须对类进行“初始化”:即对一个类的主动引用
				①.遇到new,getstatic,putstatic,invokestatic这几个调字节码指令时,如果类没有进行过初始化,则需要先触发其初始化;
				②.使用java.lang.reflect包的方法对类进行反射调用的时候,如果类没有进行过初始化,则需要先触发其初始化;
				③.当初始化一个类的时候,如果发现其父类还没有进行过初始化,则需要先触发其父类的初始化
				④.当虚拟机启动时，用户需要指定一个要执行的主类（包含main()方法的那个类），虚拟机会先初始化这个主类;
				⑤.当使用jdk1.7动态语言支持时，如果一个java.lang.invoke.MethodHandle 实例最后的解析结果REF_getstatic,
				REF_putstatic,REF_invokeStatic的方法句柄，并且这个方法句柄所对应的类没有进行初始化，则需要先出触发其初始化
			(7).不触发类的初始化:所有引用类的方式不会触发初始化,称为被动引用(见下文【6.9】)
				①.通过子类引用父类的静态字段，不会导致子类初始化;
					对于 HotSpot 虚拟机,可以通过 -XX:+TraceClassLoading 参数观察到此操作会导致子类的加载
				②.通过数组定义来引用类,不会触发此类的初始化
				③.常量在编译阶段会存入调用类的常量池中,本质上并没有直接引用到定义常量的类,因此不会触发定义常量的类的初始化
		6.8.6.Java 类的链接:将 Java 类的二进制代码合并到 JVM 的运行状态之中的过程。在链接之前，这个类必须被成功加载
			分为三部分：verification检测, preparation准备 和 resolution解析:
			(1).verification 检测:
				验证是用来确保 Java 类的二进制表示在结构上是完全正确的.如果验证过程出现错误的话,会抛出 java.lang.VerifyError
				错误;linking的resolve会把类中成员方法、成员变量、类和接口的符号引用替换为直接引用,而在这之前,需要检测被引用
				的类型正确性和接入属性是否正确(就是 public ,private 的的问题)诸如:检查 final class没有被继承，检查静态变量的
				正确性等等;
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
	6.9.Java 类的初始化: 类的初始化也是延迟的,直到类第一次被主动使用(active use)，JVM 才会初始化类
			// http://www.importnew.com/20040.html
			(1).初始化过程的主要操作是"执行静态代码块"和"初始化静态域".
				在一个类被初始化之前，它的"直接父类"也需要被初始化。
				但是,一个接口的初始化,不会引起其父接口的初始化.
				在初始化的时候,会按照源代码中"从上到下"的顺序依次"执行静态代码块和初始化静态域"
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
			6.9.1.虚拟机规定只有这四种情况才会触发类的初始化,称为对一个类进行主动引用;
				除此之外所有引用类的方式都不会触发其初始化,称为被动引用;下面是写被动引用的例子:
				(1).通过子类引用父类的静态字段,这时对子类的引用为被动引用,因此不会初始化子类,只会初始化父类;
					对于静态字段，只有直接定义这个字段的类才会被初始化.因此,通过其子类来引用父类中定义的静态字段,
					只会触发父类的初始化而不会触发子类的初始化
					public class Father {
						public static int m = 30;
						static{
							System.out.println("父类初始化");
						}
					}
					public class Son extends Father{
						static{
							System.out.println("子类初始化");
						}
					}
					public class SonFatertest {
						public static void main(String[] args) {
							System.out.println(Son.m);
						}
					}
					==> 输出结果:
					父类初始化
					30 
				(2).常量在编译阶段会存入调用它的类的常量池中,本质上没有直接引用到定义该常量的类,因此不会触发定义常量
					的类的初始化:
					public class Father {
						public static final int m = 30;
						static{
							System.out.println("父类初始化");
						}
					}
					public class Test{
						public static void main(String[] args){
							System.out.println(Father.m)
						}
					}
					==> 输出结果:30
					虽然程序中引用了Father类的常量 m,但是在编译阶段将此常量的值"30"存储到了调用它的类 Test 的常量池中,
					对常量 Father.m 的引用实际上转化为了对 Test 类的常量池的引用.也就是说 Test 的 Class 文件之中并没
					有 Father 类的符号引用入口;
				(3).通过数组定义来引用类,不会触发类的初始化
					public class Father {
						static{
							System.out.println("父类初始化");
						}
					}
					public class Test{
						public static void main(String[] args){
							Father[] father = new Father[5];
						}
					}
					这是一个对数组引用类型的初初始化,而该数组中的元素仅仅包含一个对Const类的引用,并没有对其进行初始化
			6.9.3.接口的初始化过程与类初始化过程的不同:
				(1).接口也有初始化过程,上面的代码中我们都是用静态语句块来输出初始化信息的,而在接口中不能使用
				“static{}”语句块,但编译器仍然会为接口生成<clinit>类构造器,用于初始化接口中定义的成员变量
				(实际上是 static final 修饰的全局常量)
				(2).主要区别:
					当一个类在初始化时,要求其父类全部已经初始化过了;但是一个接口在初始化时,并不要求其父接口全部
					都完成了初始化,只有在真正使用到父接口的时候(如引用接口中定义的常量).才会初始化该父接口.
	6.10.如下例子:http://www.importnew.com/18566.html
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
7.字节码执行引擎:
	7.1.物理机的执行引擎是直接建立在处理器、硬件、指令集和操作系统层面上的;
		虚拟机的执行引擎是字节实现的,可以自行制定指令集和执行引擎的结果体系,能够执行不被硬件直接支持的指令集格式;
		不同虚拟机实现里,执行引擎在执行Java代码时可能会解释执行和编译执行
		所有Java虚拟机的执行引擎都是一致的:输入的是字节码文件,处理过程是字节码解析的等效过程,输出的执行结果
	7.2.运行时栈桢结构:
		详情查看:【2.2.2.Java 虚拟机栈】
	7.3.方法调用:方法调用不等同于方法执行,其唯一的任务就是确定被调用方法的版本.
		【8.方法调用:多态性实现机制——静态分派与动态分派】
	7.4.动态类型语言支持:
		7.4.1.动态类型语言:关键特征是它的类型检查的主体过程是在运行期而不是编译期

8.方法调用:多态性实现机制——静态分派与动态分派:// http://www.importnew.com/20071.html
	8.1.方法解析:
		(1).Class 文件的编译过程不包含传统编译中的连接步骤,一切方法调用在Class 文件里面存储的都只是符号引用,
			而不是方法在实际运行时内存布局中入口地址.这个特性使得Java可以在类运行期间才能确定某些目标方法的直接引用,
			称为动态连接,
			部分方法的符号引用在类加载阶段或第一次使用时转为直接引用,这种转化称为静态解析;
		(2).静态解析成立的前提:
			方法在程序真正执行前就有一个可确定的调用版本,并且这个方法的调用版本在运行期是不可改变的
			换句话说:调用目标在编译器进行编译时就必须确定下来,这类方法的调用称为"解析";
		(3).Java 中符合"编译器可知,运行期不可变"这个要求的方法主要有静态方法和私有方法两大类,
			这两种方法都不可能通过继承或别的方式重写出其他的版本,因此它们都适合在类加载阶段进行解析.
		(4).调用方法的字节指令:
			①.invokestatic:调用静态方法
			②.invokespecial:调用实例构造器<init>方法、私有方法和父类方法.
			③.invokevirtual:调用所有的虚方法
			④.invokeinterface:调用接口方法,会在运行时再确定一个实现此接口的对象
			⑤.invokedynamic:
			只要能被invokestatic和invokespecial指令调用的方法,都可以在解析阶段确定唯一的调用版本,符合这个条件的有:
			静态方法、私有方法、实例构造器和父类方法四类,它们在类加载时就会把符号引用解析为该方法的直接引用.这类方法
			称为非虚方法(包括 final 方法),与之相反,其他方法称为虚方法(final 方法除外).
			==> 注意:
			虽然调用 final 方法使用的是 invokevirtual 指令,但由于它无法覆盖,没有其他版本,所以也无需对方发接收者
			进行多态选择.在Java语言规范中明确说明了 final 方法是一种非虚方法
		(5).解析调用:一定是个静态过程,在编译期间就完全确定,在类加载的解析阶段就会把涉及的符号引用转化为可确定的直接引用
			不会延迟到运行期再去完成;
			分派调用:可能是静态的也可能是动态,根据分派依据的宗量数(方法的调用者和方法的参数统称为方法的宗量),
			又可分为单分派和多分派,两类分派方式两两组合便构成了静态单分派、静态多分派、动态单分派、动态多分派四种分派情况;
			分派调用过程将会揭示多态特征的最基本体现:重载和重写
	8.2.静态分派:
		所有依赖静态类型来定位方法执行版本的分派动作,都称为静态分派
		(1).静态分派的最典型应用就是"多态性中的方法重载"
		(2).静态分派发生在编译阶段,因此确定静态分配的动作实际上不是由虚拟机来执行的.
			编译器虽然能确定出方法的重载版本,但在很多情况下这个重载版本并不是"唯一的",往往只是确定一个"更加合适"的版本
		(3).代码:
			class Human{}  
			class Man extends Human{}
			class Woman extends Human{}
			public class StaticPai{
			    public void say(Human hum){
			        System.out.println("I am human");
			    }
			    public void say(Man hum){
			        System.out.println("I am man");
			    }
			    public void say(Woman hum){
			        System.out.println("I am woman");
			    }
			    public static void main(String[] args){
			        Human man = new Man();
			        Human woman = new Woman();
			        StaticPai sp = new StaticPai();
			        sp.say(man);// I am human
			        sp.say(woman);//I am human
			    }
			}
		==> 分析:
			①.Human man = new Man();把"Human"称为变量的静态类型,后面的"Man"称为变量的实际类型,静态类型和实际类型在程序
				中都可以发生一些变化,区别是:静态类型的变化仅仅在使用时发生变化,变量本身的静态类型不会改变,并且最终的
				静态类型是在编译器可知的,而实际类型变化的结果在运行期才可以确定;
			②.在上面的代码中,调用 say() 方法时,方法的调用者都为 sp的前提下,调用哪个重载版本完全取决于传入参数的数量和
				数据类型;代码中刻意定义了两个静态类型相同、实际类型不同的变量,
				"可见编译器在重载时是通过参数的静态类型而不是实际类型来作为判定的依据"
				并且静态类型是编译期可知的,所以在编译阶段,Javac 编译器就根据参数的静态类型决定使用哪个重载版本
	8.3.动态分派:与方法重写紧密联系
		8.3.1.根据变量的实际类型来分派方法的执行版本的,而实际类型的确定需要在程序运行时才能确定下来,
			这种在运行期根据实际类型确定方法执行版本的分派过程称为动态分派;
		8.3.2.invokevirtual 指令的多态查找过程,其运行时解析过程大致分为:
			(1).找到操作数栈顶的第一个元素所指向的对象的实际类型,记作C;
			(2).如果在类型C中找到与常量中描述符和简单名称都相符的方法,则间进行访问权限校验,如果通过则返回这个方法的
				直接引用,查找过程结束;如果不通过,则返回 java.lang.IllegalAccessError 异常
			(3).否则,按照继承关系从下往上依次对C的各个父类进行第二步的搜索和验证过程;
			(4).如果始终没有找到合适的方法,则抛出 java.lang.AbstractMethodError 异常;
			由于 invokevirtual 指令执行的第一步就是在运行期确定接收者的实际类型,所以两次调用中的 invokevirtual 指令
			把常量池中的类方法符号引用解析到了不同的直接引用上,这个过程就是 Java 语言中方法重写的本质
	8.4.单分派和多分派:根据分派基于多少种宗量(方法的接收者与方法的参数统称为方法的宗量)
		目前的Java语言(JDK1.6)是一门静态多分派、动态单分派的语言
		8.4.1.单分派:根据一个宗量对目标方法进行选择
		8.4.2.多分派:根据多于一个宗量对目标进行选择
		8.4.3.静态分派的过程:编译阶段编译器的选择过程.选择目标方法的依据:静态类型、方法参数;
			选择结果的最终产物是产生了两条 invokevirtual 指令,两条指令分别为常量池中指向方法的符号引用
			即:静态分派属于多分派类型;
		8.4.4.运行阶段虚拟机的选择即动态分派的过程:由于编译器已经决定了目标方法的签名,虚拟机此时不会关系传递过来的参数,
			因为此时参数的静态类型、实际类型都对方法的选择不会构成任何影响,唯一可以影响虚拟机选择的因素是此方法的
			接受者的实际类型.所以动态分派为单分派类型.
9.Java 语法糖: // http://www.importnew.com/20093.html
	9.1.语法糖:指在计算机中添加的某种语法,这种语法对语言的功能并没有影响,但是更方便程序员使用.
		Java 中的语法糖主要有泛型、变长参数、条件编译、自动装拆箱、内部类等;虚拟机并不支持这些语法,它们在编译阶段就还原回了
		简单的基础语法结构,这个过程称为解语法糖
	9.2.泛型在编译阶段会被擦除
		
10.Java 编译:// http://www.importnew.com/20109.html
	10.1.无论是物理机还是虚拟机,大部分程序代码从开始编译到最终转化成物理机的目标代码或虚拟机能执行的指令集之前,
		都会按如下步骤进行:
		程序源码 --> 词法分析 --> 单词流 --> 语法分析 --> 抽象语法树
			--> 指令流 -->  解释器 --> 解释执行     			==>	解释执行的过程
			--> 优化器 --> 中间代码 --> 生成器 --> 目标代码		==> 传统编译原理从源代码到目标代码的生成过程
		(1).其中指令流,中间代码,优化器模块可以选择性的实现
		(2).现今大部分遵循现代编译原理的思路:先对程序源码进行词法解析和语法解析处理,把源码转化为抽象语法树
		(3).词法和语法分析乃至后面的优化器和目标代码生成器都可以选择独立于执行引擎
			形成一个完整意义的编译器去实现,这类代表是C/C++语言;
		(4).抽象语法树或指令流之前的步骤实现一个半独立的编译器,这类代表是Java语言;
		(5).可以把这些步骤和执行引擎全部集中在一起实现，如大多数的JavaScript执行器
		10.1.2.编译器
		(1).前端编译器:Javac 编译器将*.java文件编译成为*.class文件的过程,Javac 编译器,
			其他的前端编译器还有诸如Eclipse JDT 的增量式编译器ECJ等;
		(2).后端编译器:在程序运行时期间将字节码转变成机器码,如 HotSpot 虚拟机自带的JIT(Just In Time Compiler)编译器
			另外还有可能会碰到静态提前编译器直接将*.java文件编译成本地机器码,如GCJ等
	10.2.javac 编译
		10.2.1.Javac 编译过程:
			从 Sun Javac 的代码来看,编译过程大致分为:
			(1).解析与填充符号表的过程;
			(2).插入式注解处理器的注解处理过程;
			(3).分析与字节码生成过程;
			javac编译动作的入口是 com.sun.tools.javac.main.JavaCompiler 类,上述三个过程逻辑集中在这个类的:
			compile()和compile2()方法中.
			
		10.2.2.解析与填充符号表的过程
			(1).词法、语法分析:
				==> 词法分析是将源代码的字符流变为标记(Token)集合.单个字符是程序的最小元素,而标记则是编译过程的最小元素,
					关键字、变量名、字面量、运算符等都可以成为标记;如整型标志 int 由三个字符构成,但它只是一个标记,不可拆分
				在Javac的源码中,词法分析过程由 com.sun.tools.javac.parser.Scanner 类实现.
				==> 语法分析:根据 Token 序列来构造抽象语法树的过程.抽象语法树是一种用来描述程序代码语法结构的树形表示方式
					语法树的每一个节点都代表着程序代码中的一个语法结构.例如包、类型、修饰符、运算符、接口、返回值都是语法结构
				语法分析过程由 com.sun.tools.javac.parser.Parser 类来实现,这个阶段抽象语法树由 com.sun.tools.javac.tree.JCTree
				类来表示,经过语法分析步骤之后,编译器基本不会对源码文件进行操作了;
			(2).填充符号表:完成词法和语法分析后就是填充符号表,符号表是由一组符号地址和符号信息构成的表格,
				符号表中所登记的信息在编译的不同阶段都要用到,在语义分析(后面的步骤)中，符号表所登记的内容将用于语义检查
				和产生中间代码;在目标代码生成阶段,当对符号名进行地址分配时,符号表是地址分配的依据.
				填充符号表过程由 com.sun.tools.javac.comp.Enter 类实现,此过程的出口是一个待处理列表,包含了每一个编译
				单元的抽象语法树的顶级节点.
		10.2.3.注解处理器:
			在Javac的源码中，插入式注解处理器的初始化过程时在initProcessAnnotations()方法中完成的,而它的执行过程
			则是在 processAnnotations()方法中完成的,这个方法判断是否还有新的注解处理器需要执行,如果有的话,通过
			com.sun.tools.javac.processing.JavacProcessingEnvironment 类的 doProcessing()方法生成一个新的
			JavaCompiler 对象对编译的后续步骤进行处理.
		10.2.4.语义分析与字节码生成:
			语义分析的主要任务是对抽象语法结构数上正确的源程序进行上下文有关性质的审查,如类型审查等.
			(1).标注检查:
				检查的内容,包括诸如变量使用前是否已经声明、变量与赋值之间的数据类型是否能够匹配等.
				在标注检查步骤中,还有一个重要的动作称为常量折叠,如:
				int a = 1 + 2;
				在语法树上仍可以看到字面量 "1","2"以及操作符"+",但是在经过常量折叠之后,将会被折叠成字面量 "3";
				由于编译期间进行了常量折叠,所以在代码中定义 "a=1+2"和"a=3"并不会对程序性能上有所影响.
				其在Javac的源码实现类是:com.sun.tools.javac.comp.Attr 和 com.sun.tools.javac.comp.Check;
			(2).数据及控制流分析:是对程序上下文逻辑的更进一步的验证,它可以检查出诸如程序局部变量在使用前是否
				赋值,方法的每条路径是否都有返回值,是否所有的受检查异常都被正确处理的等问题.
				将局部变量声明为 final,对运行期是没有影响的,变量的不变性仅仅由编译器在编译期间保障.
				com.sun.tools.javac.comp.Flow
			(3).解语法糖:com.sun.tools.javac.comp.Lower
			(4).字节码生成:是Javac编译过程的最后一个阶段
				仅仅是把前面各个步骤所生成的信息转化成字节码写到磁盘中,编译器还进行了少量的代码添加和转换工作:
				实例构造器<init>()方法和类构造器<clinit>()方法就是在这个阶段添加到语法树之中的
				com.sun.tools.javac.jvm.Gen
	10.3.语法糖:(参考上述第9节)
		10.3.1.泛型与类型擦除:
			(1).C#的泛型技术:C#中的泛型无论是在程序的源码中、编译后的IL中,或是运行期的CLR中都是实际存在的,
			List<int>与 List<String>就是两个不同的类型,它们在系统运行期生成,有自己的虚方法表和类型数据,这种实现
			称为类型膨胀,基于这种方法实现的泛型是真实泛型;
			(2).Java 的泛型技术:它只在程序源码中存在,在编译后的字节码文件中就已经替换为原来的原声类型,并在相应
				的地方加入了强制转型代码,实际是Java语言的一颗语法糖,这种泛型实现方法称为类型擦除,为伪泛型.
				在重载使用时尤其注意:
			==> 代码如下:
				import java.util.List;
				public class FanxingTest{
				    public void method(List<String> list){
				        System.out.println("List String");
				    }
				    public void method(List<Integer> list){
				        System.out.println("List Int");
				    }
				}
				编译时报错:名称冲突：
				因为泛型 List<String>和 List<Integer> 编译后都被擦除了,变成了一样的原生类型List,擦除动作导致这两个方法
				的特征签名变得一模一样,在 Class 类文件结构一文中讲过，Class 文件中不能存在特征签名相同的方法
			==> 上述代码改成如下: // 这段代码需要验证,
				import java.util.List;
				public class FanxingTest{
				    public int method(List<String> list){
				        System.out.println("List String");
				        return 1;
				    }
				    public boolean method(List<Integer> list){
				        System.out.println("List Int");
				        return true;
				    }
				}
				上述代码在JDK7以后无法通过编译
				在 Java 代码中的方法特征签名只包括了方法名称、参数顺序和参数类型,并不包含方法的返回值,
				因此方法的返回值并不参与重载方法的选择;对于重载方法的选择来说,加入返回值是多余的,但现在需要的解决的问题是
				让代码通过编译,使之可以共存于一个类中,这需要看字节码的方法特征签名,其不仅包括了 Java 代码中方法特征签名中
				包含的那些信息,还包括方法返回值及受查异常表.
				为两个重载方法加入不同的返回值后,因为有了不同的字节码特征签名,它们便可以共存于一个Class文件之中
		10.3.2.条件编译:根据布尔常量值的真假,编译器将会把分支中 不成立的代码块消除掉.
	10.4.JIT 编译:
		(1).Java 程序最初是仅仅通过解释器解释执行的，即对字节码逐条解释执行，这种方式的执行速度相对会比较慢，
			尤其当某个方法或代码块运行的特别频繁时，这种方式的执行效率就显得很低
			当虚拟机发现某个方法或代码块运行特别频繁时，就会把这些代码认定为“Hot Spot Code”(热点代码)，为了提高热
			点代码的执行效率，在运行时，虚拟机将会把这些代码编译成与本地平台相关的机器码，并进行各层次的优化，完成
			这项任务的正是JIT编译器
		(2).当前主流的商用虚拟机中几乎都同时包含解释器和编译器,二者各有优势:
			当程序需要迅速启动和执行时,解释器可以首先发挥作用,省去编译的时间,立即执行;
			当程序运行后,随着时间的推移,编译器逐渐发挥最用,把越来越多的代码编译成本地代码后,可以获取更高的执行效率.
			解释器可以节约内存,而编译执行可以提升效率;
		(3).HotSpot 虚拟机中内置了两个JIT编译器:Client Complier 和 Server Complier,分别用在客户端和服务端
			运行过程中会被即时编译器编译的"热点代码"有两类:
				**被多次调用的方法;
				**被多次调用的循环体
				--> 两种编译器都是以整个方法作为编译对象,这种编译也是虚拟机中标准的编译方式
			==> 热点代码的判定方式:
				①.基于采样的热点探测:采用这种方法的虚拟机会周期性地检查各个线程的栈顶,如果发现某些方法经常出现在栈顶,
					那这段方法代码就是"热点代码"
					★优点:实现简单高效,还可以很容易地获取方法调用关系
					★缺点:很难精确地确认一个方法的热度,容易因为受到线程阻塞或别的外界因素的影响而扰乱热点探测;
				②.基于计数器的热点探测:采用这种方法的虚拟机会为每个方法，甚至是代码块建立计数器，统计方法的执行次数，
					如果执行次数超过一定的阀值，就认为它是"热点方法"
					★优点:统计结果相对更加精确严谨
					★缺点:实现复杂一些，需要为每个方法建立并维护计数器，而且不能直接获取到方法的调用关系
			HotSpot 虚拟机中使用的是第二种——基于计数器的热点探测方法,因此它为每个方法准备了两个计数器:
			方法调用计数器和回边计数器
				①.方法调用计数器:用来统计方法调用的次数,在默认设置下,方法调用计数器统计的并不是方法被调用的绝对次数,
					而是一个相对的执行频率,即一段时间内方法被调用的次数
				②.回边计数器:回边计数器用于统计一个方法中循环体代码执行的次数
					在字节码中遇到控制流向后跳转的指令就称为“回边”
				==> 在确定虚拟机运行参数的前提下,这两个计数器都有一个确定的阀值,当计数器的值超过了阀值,就会触发JIT编译
					触发了JIT编译后,在默认设置下，执行引擎并不会同步等待编译请求完成，而是继续进入解释器按照解释方式
					执行字节码，直到提交的请求被编译器编译完成为止;
					当编译工作完成后，下一次调用该方法或代码时，就会使用已编译的版本
	==> Javac 字节码编译器与虚拟机内的 JIT 编译器的执行过程合起来其实就等同于一个传统的编译器所执行的编译过程				
11.Java 垃圾收集机制 :// http://www.importnew.com/20129.html
	参考文件:/Java-GC垃圾回收机制.java

12.虚拟机监控及故障处理
	12.1.jps:虚拟机进程状况工具(JVM Process Status Tool)
		(1).功能:列出正在运行的虚拟机进程,并显示虚拟机执行主类名称以及这些进程的本地虚拟机唯一ID(LVMID)
			对于本地虚拟机进程来说,LVMID 与操作系统的进程ID是一致的,使用 windows 任务管理和UNINX的ps命令
			也可以查询虚拟机进程的 LVMID,如果同时启动了多个虚拟机进程,无法根据进程名称定位时,只能依赖 jps
			命令显示主类的功能才能区分;
		(2).用法:jps [option] [hostid],执行样例:
			C:\Users\BlueFish>jps -lm
			2120 org.jetbrains.idea.maven.server.RemoteMavenServer
			5736 sun.tools.jps.Jps -lm
			1372
			==> jps 可以通过RMI协议查询开启了RMI服务的远程虚拟机进程状态, hostid 为RMI注册表中注册的主机名
		(3).主要参数:
			-q:只输出 LVMID,省略主类的名称
			-m:输出虚拟机进程启动时传递给主类 main() 函数的参数
			-l:输出主类的全名,如果进程执行的是 jar包,输出 jar 路径
			-v:输出虚拟机进程启动时的 JVM 参数
	12.2.jstat:虚拟机统计信息监视工具:(JVM Statistics Monitoring Tool):
		(1).功能:用于监视虚拟机各种运行状态信息的命令行工具.它可以显示本地或者远程虚拟机进程中类装载、内存
			垃圾收集、JIT 编译等运行数据,在没有GUI图形界面,只提供了纯文本控制台环境的服务器上,是运行期定位
			虚拟机性能问题的首选工具.
		(2).用法:
			jstat [option vmid [interval [s|ms] [count]]]
			==> 注意:如果是本地虚拟机进行,则 VMID 和 LVMID 是一致的,如果远程虚拟机进程,那 VMID 格式为:
				[protocol:][//] lvmid[@hostname[:port]/servername]
			参数  interval 和 count 代表查询间隔和次数,如果省略这两个参数,说明只差一次.
			例子:
				jstat -gc 2764 250 20 ==> 表示需要每250ms查询一次进程2764的垃圾收集情况,一个查询20次
		(3).参数:option 代表用户希望查询的虚拟机信息,主要分为 3类:类装载、垃圾收集、运行期编译状况.
			-class : 监视类装载,卸载数量,总空间以及类状态所耗费的时间
			-gc    : 监视Java对状况,包括Eden区、两个 survivor区、老年代、永久代等的容量、已用空间、GC时间合计等
			-gccapacity:监视内容与-gc基本相同,但输出主要关注Java堆各个区域使用到的最大、最小空间
			-gcutil:监视内容与-gc基本相同,但输出主要关注已使用空间占总空间的百分比
			-gccause:与-gcutil 功能一样,但是会输出导致上一次GC产生的原因
			-gcnew:监视新生代GC状况
			-gcnewcapacity:监视内容与-gcnew基本相同,但输出主要关注使用到的最大、最小空间
			-gcold:监视老年代GC状态
			-gcoldcapacity:监视内容与-gcold基本相同,输出主要关注使用到的最大、最小空间
			-gcpermcapacity:输出永久代使用到的最大、最小空间
			-printcompilation:输出已经被JIT编译的方法
		(4).按上述参数执行结果各个代表的意思,参考如下文件:
			http://blog.csdn.net/maosijunzi/article/details/46049117
			F:\Knowledge\Java\JavaSE\Java-JVM\jstat命令执行结果.java

	12.3.jinfo:Java 配置信息工具(Configuration Info for Java)
		(1).功能:实时的查看和调整虚拟机各项参数,使用 jps 命令的 -v 参数可以查看虚拟机启动时显式指定的参数列表
		jinfo [option] pid
		jinfo -flag pid

	12.4.jmap:Java 内存映像工具(Memory Map for Java)
		(1).功能:用于生成堆转储快照(一般称为 heapdump 或 dump 文件).
			不使用 jmap 命令也可以暴力获取堆转储快照,如使用 -XX:+HeapDumpOnOutOfMemoryError,可以让虚拟机在 OOM
			异常后自动生存 dump 文件
			jmap 的作用不仅仅是为了获取 dump 文件,它还可以查询 finalize 执行队列、Java 堆和永久代的详细信息,
			如空间使用率,当前用的哪种收集器等
		(2).jmap 命令在 windows 平台下是受限的,除了生存 dump文件的 -dump 选项和用于查看每个类的实例、空间占用
			统计的 -histo 选项在所有操作系统都提供之外,其余的选项只能在 Linux/Solaris 下使用:
			jmap [option] vmid
		(3).option选项含义:
			-dump : 生成 Java 堆转储快照.格式为: -dump:[live,] format=b, file=<filename>,其中live子参数说明是否只dump出存活的对象
			-finalizerinfo:显示在F-Queue中等待Finalize 线程执行finalize 方法的对象,只能在 Linux/Solaris 下使用
			-heap:显示 Java 堆的详细信息,如使用哪种回收器,参数配置,分代状况等.只能在 Linux/Solaris 下使用
			-histo:显示堆中对象统计信息,包括类,实例数量,合计容量
			-permstat:以ClassLoader 为统计口径显示永久代内存状态,只能在 Linux/Solaris 下使用
			-F: 当虚拟机进程对 -dump 选项没有响应时,可以使用该选项强制生成dump快照,只能在 Linux/Solaris 下使用

	12.5.jhat:虚拟机堆转储快照分析工具(JVM Heap Analysis Tool)与jmap搭配使用,用来分析堆转储快照
		jhat 内置了一个微型的HTTP/HTML 服务器,生成的dump文件的分析结果后可以在浏览器查看.
		(1).在实际工作中,一般不会直接用jhat命令分析dump文件:
			A.一般不会在部署应用的服务器上直接分析dump文件,即使可以这样做,也会尽量将dump文件复制到其他机器上进行分析.
			因为分析工作是一个耗时而且消耗硬件资源的过程,既然要在其他机器上进行,就不必要受到命令行的限制
			B.jhat的分析功能相对来说很简陋,有专门的分析dump文件的工具:
				Eclipse Memory Analyzer 等
		jhat idea.bin (这个文件为jamp生成的 dump 文件)

	11.6.jstack:Java 堆栈跟踪工具(Stack Trace for Java):
		(1).功能:用于生成虚拟机当前时刻的线程快照(一般称为 threaddump 或者 javacore 文件)
			线程快照就是当前虚拟机内每一条线程正在执行的方法堆栈的集合,生成线程快照的主要目的是定位线程出现
			长时间停顿的原因,如线程间死锁,死循环,请求外部资源导致的长时间等待.线程出现停顿的时候通过jstack来查看
			各个线程的调用堆栈,就可以知道没有响应的线程到底在后台做什么
		(2).用法:jstack [option] vmid
			-F:当正常输出的请求不被响应时,强制输出线程堆栈
			-l:除堆栈外,显示关于锁的附加信息
			-m:如果调用本地方法的话,来可以显示 C/C++ 堆栈

	12.7.可视化工具:
		11.7.1.JConsole:Java 监视与管理控制台(Java Monitoring and Management Console):
			(1).用法:一种基于 JMX 的可视化监视\管理工具,它管理部分的功能是针对 JMX MBean 进行管理的
		11.7.2.VisualVM:多合一故障处理工具
13.JVM 虚拟机调优:
	13.1.在高性能硬件上的程序部署:
		(1).对于用户交互性强、对停顿时间敏感的系统,可以给Java虚拟机分配超大堆的前提是有把握把应用程序的FullGC
			频率控制的足够低,至少不要影响到用户.可以通过在深夜执行定时任务的方式触发 Full GC 甚至自动重启应用
			服务器来保持内存可用空间在一个稳定的水平
			控制Full GC 频率关键是看应用中大多数对象是否符合"朝生夕灭"的原则,即大多数对象的生存时间不应太长,
			尤其是不能有成批量的、长生存时间的大对象产生
		(2).使用若干个32位虚拟机建立逻辑集群来利用硬件资源.具体的做法是在一台物理机上启动多个应用服务器进程池.
			每个服务器进程分配不同端口,然后在前端搭建一个负载均衡器,以反向代理的方式来分配访问请求
	13.2.集群间同步导致的内存溢出
	13.3.堆外内存导致的溢出错误
	13.4.外部命令导致系统缓慢
	13.5.服务器JVM进程崩溃
	13.6.不恰当数据结果导致内存占用过大
	13.7.由windows虚拟内存导致的长时间停顿
14.JVM 优化技术:
	14.1.逃逸分析:
		https://my.oschina.net/hosee/blog/638573
		(1).逃逸分析的基本行为就是分析对象动态作用域:当一个对象在方法中被定义之后,它可能被外部所引用.
			例如作为调用参数传递到其他地方中,称为方法逃逸
			public static StringBuffer craeteStringBuffer(String s1, String s2) {
		        StringBuffer sb = new StringBuffer();
		        sb.append(s1);
		        sb.append(s2);
		        // StringBuffer sb是一个方法内部变量，上述代码中直接将sb返回，这样这个StringBuffer有可能被其他
		        // 方法所改变，这样它的作用域就不只是在方法内部，虽然它是一个局部变量，称其逃逸到了方法外部
		        return sb;
		        // 如果想要 StringBuffer sb 不逃逸出方法,可以使用 sb.toString();
		    }
	14.2.
15.

























































