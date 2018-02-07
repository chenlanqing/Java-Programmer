
参考文章:
 * [深入理解java垃圾回收机制](http://www.cnblogs.com/sunniest/p/4575144.html)
 * [Java GC 工作原理](http://www.hollischuang.com/archives/76)
 * [入浅出Java垃圾回收机制](http://www.importnew.com/1993.html)
 *  [Minor GC、Major GC和Full GC之间的区别](http://www.importnew.com/15820.html)
 * 参考书:《深入理解Java虚拟机-JVM高级特性与最佳实践[周志明]》
 
Java 垃圾回收器是一种"自适应的、分代的、停止—复制、标记-清扫"式的垃圾回收器
# 一.GC(GarbageCollection):垃圾回收,Java 的内存管理实际上就是对象的管理
## 1.垃圾回收机制的意义:
	Java 中的对象不再有"作用域"的概念,只有对象的引用才有"作用域".垃圾回收可以有效的防止内存泄露,有效的使用空闲的内存.
## 2.如何确定对象为垃圾对象:
	(1).引用计数:如果一个对象没有任何引用与之关联,则说明该对象基本不太可能在其他地方被使用到,那么这个对象就成为可被回收的对象了
		==> 无法解决循环引用的问题
	(2).可达性分析法:解决循环引用问题:
		基本思想是通过一系列的"GC Roots"对象作为起点进行搜索,如果在"GC Roots"和一个对象之间没有可达路径,则称该对象是不可达的;
		要注意的是被判定为不可达的对象不一定就会成为可回收对象.被判定为不可达的对象要成为可回收对象必须至少经历两次标记过程
## 3.GC 回收区域:
	(1).程序计数器、虚拟机栈、本地方法栈不需要进行垃圾回收的,因为他们的生命周期是和线程同步的,随着线程的销毁,
	其占用的内存会自动释放,
	(2).堆和方法区是线程共享的,需要进行垃圾回收
# 二.垃圾回收机制的对象判断算法:
## 1.引用计数算法:Reference Counting Collector
	1.1.算法分析:给对象中添加一个引用计数器,每当有一个地方引用他时,计数器就加 1;当引用失效时,引用计时器就减 1;
		任何时刻计数器为 0 的对象就是不可能再被使用;
	1.2.优缺点:
		(1).优点:算法实现简单,盘点效率也很高,如:Python语言
		(2).缺点:很难解决对象之间的循环引用问题;
			如父对象有一个对子对象的引用,子对象反过来引用父对象.这样,他们的引用计数永远不可能为 0;
	1.3.循环引用例子:
		public class Main {
		    public static void main(String[] args) {
		        MyObject object1 = new MyObject();
		        MyObject object2 = new MyObject();
		        /**
		         * 最后面两句将object1和object2赋值为null，也就是说object1和object2指向的对象已经不可能再被访问，
		         * 但是由于它们互相引用对方，导致它们的引用计数器都不为0，那么垃圾收集器就永远不会回收它们
		         */
		        object1.object = object2;
		        object2.object = object1;		         
		        object1 = null;
		        object2 = null;
		    }
		}

## 2.可达性分析算法:主流的实现,判定对象的存活
	2.1.算法分析:
		基本思路就是通过一系列的成为"GC Roots"的对象作为起始点,从这些节点开始向下搜索,
		搜索所走过的路径成为引用链(Refefence Chain),当一个对象的 GC Roots 没有任何引用链相连(用图论的话说,就是从 GC Roots
		到这个对象不可达)时,证明此对象是不可用的
	2.2.Java 中,可作为 GC Roots 的对象包括:
		(1).虚拟机栈(栈桢中的本地变量表)中引用的对象;
		(2).方法区中类静态属性引用的对象;
		(3).方法区中常量引用的对象;
		(4).本地方法栈中 JNI(即一般说的 native 方法)引用的对象;
	2.3.对于用可达性分析法搜索不到的对象,GC 并不一定会回收该对象:要完全回收一个对象,至少需要经过两次标记的过程
		(1).第一次标记:对于一个没有其他引用的对象,筛选该对象是否有必要执行 finalize 方法,如果没有必要执行,则意味着可以直接回收
			筛选依据:是否复写或执行过finalize()方法；因为finalize方法只能被执行一次
		(2).第二次标记:如果被筛选判定位有必要执行,则会放入FQueue队列,并自动创建一个低优先级的finalize线程来执行释放操作.
			如果在一个对象释放前被其他对象引用,则该对象会被移除FQueue队列;
## 3.再谈引用:
	3.1.无论是引用计数器判断对象引用的数量,还是可达性分析判断对象的引用链是否可达,
		判定对象的是否存活都与"引用"有关;
	3.2.JDK1.2 以前,Java 中的引用:如果reference类型的数据中存储的数值代表的是另一块内存的起始地址,
		就称为这块内存代表一个引用;
	3.3.JDK1.2 以后,Java 对引用进行了扩充,分为:
		强引用(Strong Refefence),软引用(Soft Refefence),弱引用(Weak Refefence),虚引用(Phantom Refefence)
		4种引用强度依次逐渐减弱
		(1).强引用(Strong Refefence):在代码中普遍存在的,类似 Object obj = new Object() 这类的引用,
			只要强引用还存在,垃圾收集器永远不会回收掉被引用的对象;当内存空间不足.Java 虚拟机宁愿抛出 OutOfMemoryError
			错误,使程序异常终止,也不会靠随意回收具有强引用的对象来解决内存不足的问题
		(2).软引用(Soft Refefence):用来描述一些还有用但并非必需的对象;对于软引用关联的对象,在系统将要发生内存溢出异常之前,
			将会把这些对象列进回收范围之中,进行第二次回收,如果这次回收还没有足够的内存,才会抛出内存溢出异常
			JDK1.2 之后,提供了 SoftReference 类实现软引用
		(3).弱引用(Weak Refefence):用来描述非必需的对象,被弱引用关联的对象只能生存到下一个垃圾收集发生之前;
			当垃圾收集器工作时,无论当前内存是否足够,都会回收掉只被弱引用关联的对象;	
			JDK1.2 之后,提供了 WeakRefefence 类来实现弱引用;
		(4).虚引用(Phantom Refefence):幽灵引用或者幻影引用,它是最弱的一种引用关系;一个对象是否有虚引用的存在完全不会对其
			生存时间构成影响,也无法通过虚引用来取得一个对象实例;
			为一个对象设置虚引用关联的唯一目的:能在这个对象被垃圾收集器回收时收到一个系统通知;
			JDK1.2 之后,提供了 PhantomRefefence 类来实现弱引用;
## 4.对象的生存或死亡:
	(1).一个对象真正的死亡,至少经历两次标记过程:如果对象在进行可达性分析后发现没有 GC Roots 相连接的引用链,那它将会被第一次标记
	并且进行一次筛选,筛选的条件是此对象是否有必要执行 finalize() 方法;
	(2).当对象没有覆盖 finalize() 方法,或是 finalize() 方法已经被虚拟机调用过,虚拟机将这两种情况视为没有必要执行;
	(3).如果这个对象被判定为有必要执行 finalize() 方法,那么这个对象将会被放置在一个叫做 F-Queue 的队列中,
		并在稍后由一个虚拟机自动建立的,低优先级的 Finalizer 线程去执行它;

## 5.回收方法区:
	(1).方法区中进行垃圾收集的性价比一般较低;
	(2).永久代的垃圾收集主要分为两部分:废弃常量和无用的类
		废弃常量:如果在常量池中没有任何引用的常量,即废弃常量
		无用的类:
			①.该类中所有实例都已经被回收,也就是 Java 堆中不存在该类的任何实例;
			②.加载该类的 ClassLoader 已经被回收;
			③.该类对应的 java.lang.Class 对象没有在任何地方引用,无法在任何对象地方通过反射访问该类的方法;
		JVM 可以对满足上述3个条件的无用类进行回收,仅仅是"可以"
	(3).在大量使用反射,动态代码,CGLib 等byteCode框架,动态生存JSP以及OSGi这类自定义 ClassLoader 的场景
		都需要虚拟机具备类卸载功能,以保证永久代不会溢出.
# 三.垃圾收集算法:
## 1.标记-清除算法(Marke-Sweep):最基础的收集算法
	1.1.算法分为"标记"和"清除"两个阶段:
		首先标记出所有需要回收的对象,在标记完成后统一回收所有被标记的对象
	1.2.不足之处:
		(1).效率:标记和清除两个过程的效率都不高;
		(2).空间:标记清除之后会产生大量不连续的内存碎片,空间碎片太多可能会导致在程序运行过程中分配较大对象时,
			无法找到足够的连续内存而不得不提前触发另一次垃圾收集动作;
## 2.标记-整理算法(Mark-Compact):
	2.1.算法分为"标记"和"清除"两个阶段:
		首先标记出所有需要回收的对象,让所有存活的对象都向一端移动,然后直接清理掉端边界意外的内存
	2.2.标记-整理算法是在标记-清除算法的基础上,又进行了对象的移动,因此成本更高,但是却解决了内存碎片的问题
## 3.复制算法(Copying):该算法的提出是为了克服句柄的开销和解决堆碎片的垃圾回收
	3.1.算法思路:将可用内存按容量划分为大小相等的两块,每次只使用其中的一块;当一块的内存用完了,
		就将存活的对象复制到另一块上面,然后再把已使用过的内存空间一次清理掉;
		每次回收时都是对整个半区进行内存回收,内存分配是不用考虑内存碎片等;
	3.2.由于新生代中对象 98% 都是"朝生夕死"的,所有不需要按照 1:1 划分内存空间,而是将内存分为一块较大的 Eden 空间和
		两块较小的 Survivor 空间,每次使用 Eden 和 其中一块 Survivor;
		(1).当回收时将 Eden 和 Survivor 中还存活的对象一次性的复制到另外一块 Survivor 空间上,最后清理掉 Eden 和刚才用过的
		Survivor 空间;
		(2).HotSpot 虚拟机默认 Eden 和 Survivor 的大小比例是 8:1,每次新生代可以内存空间为 整个新生代容量的 90%(80%+10%),
			只要 10% 内存会被浪费掉
		(3).当 Survivor 空间不够用时,需要依赖其他内存(指老年代)进行分配担保:
			没有足够的空间存放上一次新生代收集下来的存活对象时,这些对象将直接通过分配担保机制进入老年代;
## 4.分代收集算法(Generation Collection):基于不同的对象的生命周期是不一样的
	一般是把 Java 堆分为新生代与老年代
	4.1.新生代(Young Generation):
		(1).所有新生成的对象首先都是放在年轻代的,新生代的 GC 也叫做 MinorGC,MinorGC 发生频率比较高(不一定等 Eden 区满了才触发)
		(2).新生代中对象生存周期短,所以选用复制算法,只需付出少量存活对象的复制成本就可以完成收集
	4.2.老年代(Old Generation):
		(1).在新生代中经历了N次垃圾回收后仍然存活的对象,就会被放到年老代中.可以认为年老代中存放的都是一些生命周期较长的对象
		(2).当老年代内存满时触发 Major GC 即 FullGC,FullGC 发生频率比较低,老年代对象存活时间比较长,存活率高
		(3).一般使用标记-清除和标记-整理算法来进行回收;
	4.3.持久代(Permanent Generation)
		用于存放静态文件,如Java类、方法等.持久代对垃圾回收没有显著影响,但是有些应用可能动态生成或者调用一些class
# 四.垃圾收集器:内存回收的具体实现
	新生代收集器使用的收集器：Serial、PraNew、Parallel Scavenge
	老年代收集器使用的收集器：Serial Old、Parallel Old、CMS
## 1.Serial 收集器:
	1.1.单线程收集器:
		(1).使用一个CPU或一条收集线程去完成垃圾收集工作;
		(2).它进行垃圾收集时,必须暂停其他所有的工作线程,直到它收集结束
	1.2.虚拟机运行在 Client 模式下默认新生代收集器;
	1.3.简单而高效(与其他收集器的单线程比):
		对于限定单个CPU环境来说, Serial 收集器由于没有线程交互的开销,专心做垃圾收集自然可以获得最高的单线程收集效率;
	1.4.Serial 收集器对于新生代采用复制算法实现,对于老年代采用标记-整理算法实现
## 2.ParNew 收集器:
	2.1.新生代收集器,可以认为是 Serial 收集器的多线程版本,在多核CPU环境下有着比 Serial ;更好的表现;
		其他与 Serial 收集器实现基本差不多;
	2.2.是运行在 Server 模式下的虚拟机中首选的新生代选择器,其中与性能无关的是:只有 Serial 收集器和 ParNew 收集器
		能与 CMS 收集配合工作;
	2.3.在 JDK1.5 中使用 CMS 来收集老年代的时候,新生代只能选用 Serial 或 ParNew 收集器中的一个
		ParNew 收集器也是使用 -XX:UseConcMarkSweepGC 选项后的默认新生代收集器,
		也可以使用 -XX:+UseParNewGC 选项来强制指定它
	2.4.新生代采用复制算法,对于老年代采用标记-整理算法实现

## 3.Parallel Scavenge 收集器
	3.1.是新生代收集器,使用复制算法的收集器,且是并行的多线程收集器
	3.2.Parallel Scavenge 收集器目标是达到一个控制的吞吐量(Throughput,CPU 运行用户代码的时间与CPU总消耗的时间的比值)
	3.3.GC 自适应调整策略
	3.4.无法与 CMS 收集器配合工作;
## 4.Serial Old 收集器:Serial 收集器的老年代版本,采用标记-整理算法实现
	4.1.主要是在 Client 模式下的虚拟机使用,在 Server 模式下,有两大用途:
		(1).在 JDK1.5 以及之前的版本中与 Parallel Scavenge 收集器搭配使用;
		(2).作为 CMS 收集器的后备预案在并发收集发生 Concurrent Mode Failure 时使用
## 5.Parallel Old 收集器:
	(1).Parallel Scavenge 收集器的老年代版本,使用标记-整理算法
	(2).JDK1.6 之后才开始提供的
## 6.CMS(Concurrent Mark Sweep) 收集器:
	6.1.追求最短 GC 回收停顿时间
	6.2.基于标记-清除算法实现的,运作过程可以分为4个步骤:
		(1).初始标记(CMS initial mark)
		(2).并发标记(CMS concurrent mark)
		(3).重新标记(CMS remark)
		(4).并发清除(CMS concurrent sweep)
		其中初始标记、重新标记这两个步骤仍然需要"Stop the world".初始标记仅仅是标记以下 GC Roots 能直接关联到的对象,速度很快,
		并发标记阶段就是进行 GC Roots Tracing 的过程,而重新阶段则是为了修正并发标记期间因用户程序继续运作而导致
		标记常数变动的那一部分对象的标记记录,这个阶段的停顿时间一般会比初始标记的阶段稍微长点,但远比并发标记阶段时间短
	6.3.优缺点:
		(1).优点:并发收集,低停顿
		(2).缺点:
			CMS 收集器对CPU资源非常敏感
			CMS 收集无法处理浮动垃圾(Floating Garbage),可能出现 Concurrent Mode Failure 失败而导致一次 Full GC 的产生
			CMS 基于标记-清除算法实现的,那么垃圾收集结束后会产生大量的空间碎片

## 7.G1 收集器(Garbage First):面向服务端应用的垃圾收集器
	7.1.特点:
		(1).并行与并发
		(2).分代收集
		(3).空间整合
		(4).可预测停顿:降低停顿时间
	7.2.与其他收集器区别:其将整个Java堆划分为多个大小相等的独立区域(Region).虽然还保留了新生代和老年代的概念,
		但是新生代和老年代不再是物理隔离的,它们都是一部分独立区域(不要求连续)的集合.
	7.3.G1 收集器的运作大致可划分为:(不计算维护 Remembered Set 的操作):
		(1).初始标记(Initial Marking)
		(2).并发标记(Concurrent Marking)
		(3).最终标记(Final Marking)
		(4).筛选回收(Live Data Counting and Evacuation)

# 五.GC 执行机制:Minor GC 和 Full GC
## 1.Minor GC:
	一般情况下,当新对象生成,并且在 Eden 申请空间失败时,就会触发 Minor GC,主要是针对新生代对 Eden 区域进行GC,清除非存活对象,
	并且把尚且存活的对象移动到 Survivor 区,然后整理 Survivor 的两个区.
	这种方式的 GC 是对年轻代的 Eden 进行,不会影响到年老代.因为大部分对象都是从 Eden 区开始的,同时 Eden 区不会分配的很大,
	所以 Eden 区的 GC 会频繁进行.因而,一般在这里需要使用速度快、效率高的算法,使Eden去能尽快空闲出来
## 2.Full GC:对整个堆进行整理,包括 新生代,老年代和持久代
	Full GC 因为需要对整个对进行回收，所以比 Scavenge GC 要慢,因此应该尽可能减少 Full GC 的次数。
	在对JVM调优的过程中,很大一部分工作就是对于 FullGC 的调节。
	有如下原因可能导致 Full GC：
		(1).老年代(Old)被写满
		(2).持久代(Perm,方法区)被写满
		(3).System.gc()被显示调用;
		(4).上一次GC之后Heap的各域分配策略动态变化;
		(5).CMS GC 时出现promotion failed和concurrent mode failure
			promotion failed:是在进行 Minor GC时,survivor空间放不下、对象只能放入老生代,而此时老生代也放不下造成的
			concurrent mode failure:在执行 CMS GC 的过程中同时有对象要放入老生代,而此时老生代空间不足造成的
## 3.内存回收与分配策略:
	3.1.对象优先分配在 Eden 区域:大多数情况下,对象在新生代的Eden区中分配,如果Eden区没有足够的空间时,虚拟机将发起一次 MinorGC
		(1).查看代码的具体GC日志VM参数
			-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
		(2).打印日志:
			private static final int _1MB = 1024 * 1024;
		    public static void testAllocation(){
		        byte[] a1, a2, a3, a4;
		        a1 = new byte[2 * _1MB];
		        a2 = new byte[2 * _1MB];
		        a3 = new byte[2 * _1MB];
		        a4 = new byte[4 * _1MB];
		    }
		    [GC (Allocation Failure) [PSYoungGen: 14925K->1011K(18432K)] 14925K->13308K(38912K), 0.0042158 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
		    [Full GC (Ergonomics) [PSYoungGen: 1011K->0K(18432K)] [ParOldGen: 12296K->13203K(20480K)] 13308K->13203K(38912K), [Metaspace: 3395K->3395K(1056768K)], 0.0056106 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
		    [GC (Allocation Failure) [PSYoungGen: 6307K->96K(18432K)] 19511K->19443K(38912K), 0.0027672 secs] [Times: user=0.06 sys=0.00, real=0.00 secs] 
		    [Full GC (Ergonomics) [PSYoungGen: 96K->0K(18432K)] [ParOldGen: 19347K->19250K(20480K)] 19443K->19250K(38912K), [Metaspace: 3419K->3419K(1056768K)], 0.0068909 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
		    Heap
		     PSYoungGen      total 18432K, used 12779K [0x00000000fec00000, 0x0000000100000000, 0x0000000100000000)
		      eden space 16384K, 78% used [0x00000000fec00000,0x00000000ff87afb0,0x00000000ffc00000)
		      from space 2048K, 0% used [0x00000000ffe00000,0x00000000ffe00000,0x0000000100000000)
		      to   space 2048K, 0% used [0x00000000ffc00000,0x00000000ffc00000,0x00000000ffe00000)
		     ParOldGen       total 20480K, used 19250K [0x00000000fd800000, 0x00000000fec00000, 0x00000000fec00000)
		      object space 20480K, 93% used [0x00000000fd800000,0x00000000feaccb48,0x00000000fec00000)
		     Metaspace       used 3425K, capacity 4494K, committed 4864K, reserved 1056768K
		      class space    used 378K, capacity 386K, committed 512K, reserved 1048576K {}
		    ①.GC 日志开头"[GC"和 "[FULL GC"说明了这次垃圾收集停顿的类型,不是用来区分新生代GC和老年代GC的
		    如果有Full 说明这次GC发生了的"Stop-the-world"
		    ②.[PSYoungGen: 14925K->1011K(18432K)] 14925K->13308K(38912K), 0.0042158 secs
		    	[PSYoungGen, [ParOldGen, [Metaspace 表示GC发生的区域
		    	PSYoungGen ==> 表示使用的是 Parallel Scavenge 收集器来回收新生代的
		    	ParOldGen ==> Parallel Old 收集器
		    	14925K->1011K(18432K)==>GC前盖该内存区域已使用容量->GC后该区域已使用容量(该区域总容量)
		    	14925K->13308K(38912K) ==> GC前Java堆已使用容量->GC后Java堆已使用容量(Java堆总容量)
		    	0.0042158 secs ==> 该区域GC所占用时间,单位是秒
		    ③.[Times: user=0.00 sys=0.00, real=0.00 secs] 
		    user,sys,real 与Linux的time命令输出的时间含义一致,分别代表用户态消耗的CPU时间,内核态消耗的CPU时间和操作从开始到结束锁经过的墙钟时间
	3.2.大对象直接进入老年代:
		(1).所谓大对象是指需要大量连续内存空间的Java对象,最典型的大对象就是那种很长的字符串以及数组.
		(2).经常出现大对象容易导致内存还有不少空间时就提前触发垃圾收集来获取足够的连续空间分配给新生成的大对象.
		(3).虚拟机提供了:-XX:PertenureSizeThreshold 参数,令大于这个设置值的对象直接在老年代分配,避免在Eden区和两个Survivor
			区之间发生大量的内存复制
	3.3.长期存活的对象将进入老年代:
		(1).虚拟机给每个对象定义了一个对象年龄计数器,来识别哪些对象放在新生代,哪些对象放在老年代中.
			如果对象在Eden出生并经过一次 MinorGC 后仍然存活,并且能够被 Survivor 容纳,将被移动到 Survivor 空间,并且对象年龄为1
			当它的对象年龄增加到一定程度(默认15岁),将会进入到老年代.
			对象晋升老年代的年龄阈值,可以通过参数 --XX:MaxTenuringThreshold 设置
	3.4.动态对象年龄判定:
		如果在 Survivor 空间中相同年龄所有对象大小的总和大于 Survivor 空间的一般,年龄大于或等于该年龄的对象可以直接进入老年代,
		无须等到 MaxTenuringThreshold 中要求的年龄
	3.5.空间分配担保:
		在发生 MinorGC 之前,虚拟机会先检查老年代最大可用的连续空间是否大于新生代所有对象总空间,如果这个条件成立,那么MinorGC
		可用确保是安全的.如果不成立,则虚拟机会查看 HandlePromotionFailure 设置是否允许打包失败,如果允许,那么会继续检查老年代
		最大可用连续空间是否大于历次晋升到老年代对象的平均大小,如果大于,将尝试着进行一次MinorGC,有风险;
		如果小于或者 HandlePromotionFailure 设置不允许毛线,那时会改为进行一次 FullGC.
# 六.详解 finalize()方法:finalize是位于 Object 类的一个方法,该方法的访问修饰符为 protected
	1.finalize函数没有自动实现链式调用,必须手动的实现,因此finalize函数的最后一个语句通常是 super.finalize(),
	通过这种方式,我们可以实现从下到上实现finalize的调用,即先释放自己的资源,然后再释放父类的资源;
	2.根据 Java 语言规范，JVM 保证调用finalize函数之前,这个对象是不可达的,但是 	JVM 不保证这个函数一定会被调用.另外,规范还保证finalize函数最多运行一次
	3.GC 为了能够支持finalize函数，要对覆盖这个函数的对象作很多附加的工作。
	在finalize运行完成之后,该对象可能变成可达的,GC 还要再检查一次该对象是否是可达的.因此，使用finalize会降低GC的运行性能.
	由于GC调用finalize的时间是不确定的,因此通过这种方式释放资源也是不确定的
	4.尽量少用finalize函数。finalize函数是Java提供给程序员一个释放对象或资源的机会.	但是，它会加大GC的工作量，因此尽量少采用finalize方式回收资源

# 七.Java 有了GC同样会出现内存泄露问题
	1.静态集合类像 HashMap、Vector 等的使用最容易出现内存泄露,这些静态变量的生命周期和应用程序一致，
	所有的对象Object也不能被释放,因为他们也将一直被Vector等应用着;
	2.各种连接,数据库连接,网络连接,IO 连接等没有显示调用close关闭,不被 GC 回收导致内存泄露。
	3.监听器的使用,在释放对象的同时没有相应删除监听器的时候也可能导致内存泄露

# 八.如何优化 GC 
	* http://www.importnew.com/3146.html
## 1.监控 GC:
	1.1.垃圾回收收集监控指的是搞清楚 JVM 如何执行 GC 的过程,如可以查明
		(1).何时一个新生代中的对象被移动到老年代时,所花费的时间
		(2).Stop-the-world 何时发生的,持续了多长时间
		GC 监控是为了鉴别 JVM 是否在高效地执行 GC,以及是否有必要进行额外的性能调优
	1.2.如何监控:
	[参考:
		/Java/JavaSE/Java-JVM/jstat命令执行结果.java
		/Java/JavaSE/Java-JVM/JVM-Java虚拟机.java ---12.2
	]
		1.2.1.jstat:是 HotSpot JVM 提供的一个监控工具
			jstat 不仅提供 GC 操作的信息,还提供类装载操作的信息以及运行时编译器操作的信息
			$> jstat –gc  $<vmid$> 1000
			==> vmid (虚拟机 ID)
## 2.GC 优化:如果你没有设定内存的大小,并且系统充斥着大量的超时日志时,你就需要在你的系统中进行GC优化了
	2.1.GC 优化的目的:
		(1).将转移到老年代的对象数量降到最少:减少被移到老年代空间对象的数量,可以调整新生代的空间.
			减少 FullGC 的频率.
		(2).减少 Full GC 执行的时间:FullGC 的执行时间要比 MinorGC 要长很多.
			如果试图通过削减老年代空间来减少 FullGC 的执行时间,可能会导致 OutOfMemoryError 或者增加 FullGC 执行此数
			与之相反,如果你试图通过增加老年代空间来减少 Full GC 执行次数,执行时间会增加
	2.2.GC 优化需要考虑的Java参数:
		(1).堆内存空间:
			-Xms : Heap area size when starting JVM,启动JVM时的堆内存空间
			-Xmx : Maximum heap area size,堆内存最大限制
		(2).新生代空间:
			-XX:NewRatio : Ratio of New area and Old area,新生代和老年代的占比
			-XX:NewSize : New area size,新生代空间
			-XX:SurvivorRatio : Ratio ofEdenarea and Survivor area,Eden 和 Suvivor 空间的占比
	2.3.当 OutOfMemoryError 错误发生并且是由于Perm空间不足导致时,另一个可能影响GC性能的参数是GC类型
		Serial GC  	-XX:+UseSerialGC
		Parallel GC：	-XX:+UseParallelGC
						-XX:ParallelGCThreads=value
		Parallel Compacting GC	-XX:+UseParallelOldGC
		CMS GC	-XX:+UseConcMarkSweepGC
				-XX:+UseParNewGC
				-XX:+CMSParallelRemarkEnabled
				-XX:CMSInitiatingOccupancyFraction=value
				-XX:+UseCMSInitiatingOccupancyOnly
		G1	-XX:+UnlockExperimentalVMOptions
			-XX:+UseG1GC --> 在JDK6中这两个参数必须同时使用
		==> 最常用的GC类型是Serial GC
	2.4.GC 优化过程:
		(1).监控GC状态:
		(2).在分析监控结果后,决定是否进行GC优化:
		(3).调整GC类型/内存空间
		(4).分析结果
		(5).如果结果令人满意,你可以将该参数应用于所有的服务器,并停止GC优化
		-XX:+PrintGCDetails  表示在控制台上打印出GC具体细节
		-XX:+PrintGC 表示在控制台上打印出GC信息
# 九.减少GC开销:
## 1.避免隐式的 String 字符串:
	1.1."+"操作就会分配一个链接两个字符串的新的字符串,这里分配了一个隐式的StringBuilder对象来链接两个 String 字符串
		如:a = a + b; // a and b are Strings
		编译器在背后就会生成这样的一段儿代码:
			StringBuilder temp = new StringBuilder(a).
			temp.append(b);
			a = temp.toString(); // 一个新的 String 对象被分配
			// 第一个对象 "a"现在可以说是垃圾了
	1.2.解决方案:减少垃圾对象的一种方式就是善于使用 StringBuilder 来建对象
## 2.计划好 List 的容量	
	2.1.问题:
		(1).ArrayList 的构造器并不知道这个值的大小，构造器会分配一个默认的Object数组的大小。一旦内部数组溢出，
		它就会被一个新的、并且足够大的数组代替，这就使之前分配的数组成为了垃圾
		(2).如果执行数千次的循环，那么就会进行更多次数的新数组分配操作，以及更多次数的旧数组回收操作。
		对于在大规模环境下运行的代码，这些分配和释放的操作应该尽可能从CPU周期中剔除
	2.2.无论什么时候，尽可能的给 List 或者 Map 分配一个初始容量，就像这样:
		因为 List 初始化,有足够的容量,所有这样可以减少内部数组在运行时不必要的分配和释放
## 3.使用高效的含有原始类型的集合:
	3.1.当前版本的Java编译器对于含有基本数据类型的键的数组以及Map的支持，是通过“装箱”来实现的-
		自动装箱就是将原始数据装入一个对应的对象中，这个对象可被GC分配和回收

## 4.使用数据流(Streams)代替内存缓冲区（in-memory buffers）
	4.1.问题:
		通过过ByteArrayInputStream,ByteBuffer 把数据读入内存中，然后再进行反序列化,完整的数据在构造新的对象的时候，
		你需要为其分配空间，然后立刻又释放空间。并且，由于数据的大小你又不知道，你只能猜测 – 当超过初始化容量的时候，
		不得不分配和释放 byte[]数组来存储数据
	4.2.解决方案
		像Java自带的序列化工具以及Google的Protocol Buffers等，它们可以将来自于文件或网络流的数据进行反序列化，
		而不需要保存到内存中，也不需要分配新的byte数组来容纳增长的数据
## 5.List 集合
	5.1.问题:
		当方法返回一个集合，通常会很明智的在方法中创建一个集合对象（如ArrayList），填充它，并以不变的集合的形式返回;
	5.2.解决方案:
		这种情况的解决方案将不会返回新的集合,而是通过使用单独的集合当做参数传入到那些方法代替组合的集合	



