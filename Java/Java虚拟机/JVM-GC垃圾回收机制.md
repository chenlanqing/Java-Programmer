
Java 垃圾回收器是一种"自适应的、分代的、停止—复制、标记-清扫"式的垃圾回收器

# 一、GC-GarbageCollection

垃圾回收，Java 的内存管理实际上就是对象的管理，GC 主要工作在 Heap 区和 MetaSpace 区（上图蓝色部分），在 Direct Memory 中，如果使用的是 DirectByteBuffer，那么在分配内存不够时则是 GC 通过 Cleaner#clean 间接管理。

任何自动内存管理系统都会面临的步骤：为新对象分配空间，然后收集垃圾对象空间

## 1、垃圾回收机制的意义

- Java 中的对象不再有"作用域"的概念，只有对象的引用才有"作用域"。垃圾回收可以有效的防止内存泄露，有效的使用空闲的内存。
- 提供系统性能，排查各种内存溢出、内存泄露问题

## 2、如何确定对象为垃圾对象

- 引用计数：如果一个对象没有任何引用与之关联，则说明该对象基本不太可能在其他地方被使用到，那么这个对象就成为可被回收的对象了无法解决循环引用的问题

- 可达性分析法-解决循环引用问题

	基本思想是通过一系列的"GC Roots"对象作为起点进行搜索，如果在"GC Roots"和一个对象之间没有可达路径，则称该对象是不可达的；要注意的是被判定为不可达的对象不一定就会成为可回收对象.被判定为不可达的对象要成为可回收对象必须至少经历两次标记过程

## 3、GC 回收区域

- 程序计数器、虚拟机栈、本地方法栈不需要进行垃圾回收的，因为他们的生命周期是和线程同步的，随着线程的销毁，其占用的内存会自动释放，
- 堆和方法区是线程共享的，需要进行垃圾回收；Java堆是对无用对象的回收，方法区是对废弃的常量以及无用的类进行回收；

# 二、垃圾回收对象判断算法

## 1、引用计数算法：Reference Counting Collector

**1.1、算法分析：**

给对象中添加一个引用计数器，每当有一个地方引用他时，计数器就加1；当引用失效时，引用计时器就减1；任何时刻计数器为0的对象就是不可能再被使用；

**1.2、优缺点：**

- 优点：算法实现简单，盘点效率也很高，如：Python语言
- 缺点：很难解决对象之间的循环引用问题；如父对象有一个对子对象的引用，子对象反过来引用父对象。这样，他们的引用计数永远不可能为0；

> 虽然循环引用的问题可通过 Recycler 算法解决，但是在多线程环境下，引用计数变更也要进行昂贵的同步操作，性能较低，早期的编程语言会采用此算法

**1.3、循环引用例子**

```java
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
```

## 2、可达性分析算法（Root Tracing）

### 2.1、算法分析

基本思路就是通过一系列的成为"GC Roots"的对象作为起始点，从这些节点开始向下搜索，搜索所走过的路径成为引用链（Refefence Chain），当一个对象的GC Roots没有任何引用链相连（用图论的话说，就是从 GC Roots到这个对象不可达）时，证明此对象是不可能被使用的；

其遍历图是BFS还是DFS？其实都可以，其要解决环的问题；

### 2.2、可作为GC Roots的对象包括

- 虚拟机栈（栈桢中的本地变量表）中引用的对象：类加载器、Thread等
- 方法区中类静态属性引用的对象；
- 方法区中常量引用的对象；
- 本地方法栈中 JNI（即一般说的 native 方法）引用的对象；
- 活跃线程的引用对象
- Java虚拟机内部的引用，如基本类型对应的class对象，一些常驻的异常对象，还有系统类加载器；
- 所有被同步锁持有的对象；
- 反应被Java虚拟机内部情况的JMXBean，JVMTI中注册的回调、本地代码缓存等；

GC管理的主要区域是Java堆，一般情况下只针对堆进行垃圾回收。方法区、栈和本地方法区不被GC所管理，因而选择这些区域内的对象作为GC roots，被GC roots引用的对象不被GC回收

### 2.3、两次标记

**对于用可达性分析法搜索不到的对象，GC 并不一定会回收该对象：要完全回收一个对象，至少需要经过两次标记的过程**

- 第一次标记：对于一个没有其他引用的对象，筛选该对象是否有必要执行 finalize 方法，如果没有必要执行，则意味着可以直接回收。筛选依据：是否复写或执行过finalize()方法；因为finalize方法只能被执行一次；

- 第二次标记：如果被筛选判定位有必要执行，则会放入FQueue队列，并自动创建一个低优先级的finalize线程来执行释放操作。如果在一个对象释放前被其他对象引用，则该对象会被移除FQueue队列；

## 3、Java中的引用

无论是引用计数器判断对象引用的数量，还是可达性分析判断对象的引用链是否可达，判定对象的是否存活都与"引用"有关；

JDK1.2以前，Java中的引用：如果reference类型的数据中存储的数值代表的是另一块内存的起始地址，就称为这块内存代表一个引用；

JDK1.2以后，Java对引用进行了扩充，分为：强引用（Strong Reference）、软引用（Soft Reference）、弱引用（Weak Reference）、虚引用（Phantom Reference），4种引用强度依次逐渐减弱；
- **强引用(Strong Reference)**：在代码中普遍存在的，类似 Object obj = new Object() 这类的引用，只要强引用还存在，垃圾收集器永远不会回收掉被引用的对象；当内存空间不足，Java 虚拟机宁愿抛出 OutOfMemoryError错误，使程序异常终止，也不会靠随意回收具有强引用的对象来解决内存不足的问题；

- **软引用(Soft Reference)**：用来描述一些还有用但并非必需的对象；对于软引用关联的对象，在系统将要发生内存溢出异常之前，将会把这些对象列进回收范围之中，进行第二次回收，如果这次回收还没有足够的内存，才会抛出内存溢出异常，JDK1.2 之后，提供了SoftReference类实现软引用；可以实现高速缓存；
    ```java
    Object o = new Object();
    SoftReference<Object> softReference = new SoftReference<>(o);
    ```
    应用场景：软引用通常用来实现内存敏感的缓存。如果还有空闲内存，就可以暂时保留缓存，当内存不足时清理掉，这样就保证了使用缓存的同时，不会耗尽内存。

    这里有一个相关的 JVM 参数。它的意思是：每 MB 堆空闲空间中 SoftReference 的存活时间。这个值的默认时间是1秒（1000）
    ```
    -XX:SoftRefLRUPolicyMSPerMB=<N>
    ```

- **弱引用(Weak Reference)**：用来描述非必需的对象，被弱引用关联的对象只能生存到下一个垃圾收集发生之前；当垃圾收集器工作时，无论当前内存是否足够，都会回收掉只被弱引用关联的对象；JDK1.2 之后，提供了 WeakReference 类来实现弱引用；
    ```java
    Object o = new Object();
    WeakReference<Object> weakReference = new WeakReference<>(o);
    ```
    应用场景：弱应用同样可用于内存敏感的缓存。JDK中使用场景：
    - WeakHashMap（java.util包）：键（Key）通过弱引用保存，当键不再被外部强引用时，对应的键值对会自动被垃圾回收；适用场景：缓存键为对象的场景（如元数据缓存），但需注意值对象可能仍需手动管理；
    - ThreadLocal 的内部实现：`ThreadLocal.ThreadLocalMap`中的Entry继承自`WeakReference<ThreadLocal<?>>`，将ThreadLocal对象作为弱引用键
    - 缓存与资源管理
    - 监听器与回调管理
    - 动态代理与反射缓存
    - 并发工具类

- **虚引用(Phantom Reference)**：虚引用并不会决定对象的生命周期，幽灵引用或者幻影引用，它是最弱的一种引用关系；一个对象是否有虚引用的存在完全不会对其生存时间构成影响，也无法通过虚引用来取得一个对象实例；为一个对象设置虚引用关联的唯一目的：能在这个对象被垃圾收集器回收时收到一个系统通知；JDK1.2 之后，提供了 PhantomReference 类来实现弱引用；必须和引用队列ReferenceQueue 联合使用；跟着对象垃圾收集器回收的活动，起哨兵作用；当虚引用被加入到引用队列的时候，说明这个对象已经被回收，可以在所引用的对象回收之后可以采取必要的行动
    ```java
    Object o = new Object();
    // 必须结合引用队列
    ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
    // 虚引用
    PhantomReference<Object> phantomReference = new PhantomReference<>(o, referenceQueue);
    ```
    应用场景：可用来跟踪对象被垃圾回收器回收的活动，当一个虚引用关联的对象被垃圾收集器回收之前会收到一条系统通知

    基于虚引用，有一个更加优雅的实现方式，那就是 Java 9 以后新加入的 Cleaner，用来替代 Object 类的 finalizer 方法

关于引用队列：
- 无实际存储结构，存储逻辑依赖于内部节点之间的关系来表达；
- 存储关联的且被GC的软引用，弱引用以及虚引用；
- 进行垃圾回收之后，会将需要垃圾回收的软引用、弱引用、虚引用放入到引用队列
```java
public static void main(String[] args) throws InterruptedException{
    Object o = new Object();
    ReferenceQueue<Object> referenceQueue = new ReferenceQueue<>();
    WeakReference<Object> weakReference = new WeakReference<>(o,referenceQueue);
    System.out.println(weakReference.get());
    System.out.println(referenceQueue.poll());// o没有被回收时，队列为空

    o = null;
    System.gc();
    Thread.sleep(500);
    System.out.println(weakReference.get());//o==null
    System.out.println(referenceQueue.poll());// o被回收以后，队列有值
}
```

## 4、对象的生存或死亡

![](image/对象死亡过程.png)

- 一个对象真正的死亡，至少经历两次标记过程：如果对象在进行可达性分析后发现没有GC Roots相连接的引用链，那它将会被第一次标记并且进行一次筛选，筛选的条件是此对象是否有必要执行 finalize() 方法;
- 当对象没有覆盖 finalize() 方法，或是 finalize() 方法已经被虚拟机调用过，虚拟机将这两种情况视为没有必要执行；
- 如果这个对象被判定为有必要执行 finalize() 方法，那么这个对象将会被放置在一个叫做 F-Queue 的队列中，并在稍后由一个虚拟机自动建立的，低优先级的 Finalizer 线程去执行它;

```java
/**
 * 1、对象可在被GC时自我拯救；
 * 2、这种自救的机会只有一次，因为一个对象的 finalize 方法最大只会被系统调用一次
 */
public class FinalizeEscapeGC {
    public static FinalizeEscapeGC SAVE_HOOK = null;
    public void isAlive() {
        System.out.println("yes i am still alive");
    }
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize method executed");
        SAVE_HOOK = this;
    }
    public static void main(String[] args) throws Throwable {
        SAVE_HOOK = new FinalizeEscapeGC();
        // 对象第一次自我拯救
        SAVE_HOOK = null;
        System.gc();
        // 因为 finalize 优先级很低，暂停0.5s，以等待
        Thread.sleep(500);
        if (SAVE_HOOK != null) {
            SAVE_HOOK.isAlive();
        } else {
            System.out.println("Oh, i adm dead:(");
        }
		// 与上面代码完全一样，但是这次自救失败，因为  finalize 只会被系统自动调用一次；
        SAVE_HOOK=null;
        System.gc();
        // 因为 finalize 优先级很低，暂停0.5s，以等待
        Thread.sleep(500);
        if (SAVE_HOOK != null) {
            SAVE_HOOK.isAlive();
        } else {
            System.out.println("Oh, i adm dead:(");
        }
    }
}
```

## 5、回收方法区

- 方法区中进行垃圾收集的性价比一般较低；
- 永久代的垃圾收集主要分为两部分；废弃常量和无用的类
	- 废弃常量：如果在常量池中没有任何引用的常量，即废弃常量
	- 无用的类：
		- ①、该类中所有实例都已经被回收，也就是 Java 堆中不存在该类的任何实例;
		- ②、加载该类的 ClassLoader 已经被回收;
		- ③、该类对应的 java.lang.Class 对象没有在任何地方引用，无法在任何对象地方通过反射访问该类的方法;

	JVM 可以对满足上述3个条件的无用类进行回收，仅仅是"可以"

- 在大量使用反射、动态代码、CGLib等byteCode框架，动态生存JSP以及OSGi这类自定义ClassLoader的场景都需要虚拟机具备类卸载功能，以保证永久代不会溢出。

可以通过 `-Xnoclassgc` 参数来控制是否对类进行卸载

# 三、垃圾收集算法

垃圾回收的三种方式：
- （1）清除（sweep），即把死亡对象所占据的内存标记为空闲内存，并记录在一个空闲列表（free list）之中。当需要新建对象时，内存管理模块便会从该空闲列表中寻找空闲内存，并划分给新建的对象；但是存在两个缺点：
    - 一是会造成内存碎片。由于 Java 虚拟机的堆中对象必须是连续分布的，因此可能出现总空闲内存足够，但是无法分配的极端情况。
    - 另一个则是分配效率较低。如果是一块连续的内存空间，那么我们可以通过指针加法（pointer bumping）来做分配。而对于空闲列表，Java 虚拟机则需要逐个访问列表中的项，来查找能够放入新建对象的空闲内存
- （2）压缩（compact），即把存活的对象聚集到内存区域的起始位置，从而留下一段连续的内存空间。这种做法能够解决内存碎片化的问题，但代价是压缩算法的性能开销
- （3）复制（copy），即把内存区域分为两等分，分别用两个指针 from 和 to 来维护，并且只是用 from 指针指向的内存区域来分配内存。当发生垃圾回收时，便把存活的对象复制到 to 指针指向的内存区域中，并且交换 from 指针和 to 指针的内容。复制这种回收方式同样能够解决内存碎片化的问题，但是它的缺点也极其明显，即堆空间的使用效率极其低下

## 1、标记-清除算法(Mark-Sweep)-最基础的收集算法

**1.1、算法分为"标记"和"清除"两个阶段：**

首先标记出所有需要回收的对象，在标记完成后统一回收所有被标记的对象

回收过程主要分为两个阶段，第一阶段为追踪（Tracing）阶段，即从 GC Root 开始遍历对象图，并标记（Mark）所遇到的每个对象，第二阶段为清除（Sweep）阶段，即回收器检查堆中每一个对象，并将所有未被标记的对象进行回收，整个过程不会发生对象移动。整个算法在不同的实现中会使用三色抽象（Tricolor Abstraction）、位图标记（BitMap）等技术来提高算法的效率，存活对象较多时较高效。

**1.2、不足之处**

- 效率：标记和清除两个过程的效率都不高；
- 空间：标记清除后会产生大量不连续的内存碎片，空间碎片太多可能会导致在程序运行过程中分配较大对象时，无法找到足够的连续内存而不得不提前触发另一次垃圾收集动作；

## 2、标记-整理算法(Mark-Compact)

- 算法分为"标记"和"清除"两个阶段：首先标记出所有需要回收的对象，让所有存活的对象都向一端移动，然后直接清理掉端边界意外的内存；

- 标记-整理算法是在标记-清除算法的基础上，又进行了对象的移动，因此成本更高，但是却解决了内存碎片的问题；适用于老年代区域的垃圾回收

这个算法的主要目的就是解决在非移动式回收器中都会存在的碎片化问题，也分为两个阶段，第一阶段与 Mark-Sweep 类似，第二阶段则会对存活对象按照整理顺序（Compaction Order）进行整理。主要实现有双指针（Two-Finger）回收算法、滑动回收（Lisp2）算法和引线整理（Threaded Compaction）算法等

## 3、复制算法（Copying）-新生代采用的收集算法

该算法的提出是为了克服句柄的开销和解决堆碎片的垃圾回收

**3.1、算法思路：**

将可用内存按容量划分为大小相等的两块，每次只使用其中的一块；当一块的内存用完了，就将存活的对象复制到另一块上面，然后再把已使用过的内存空间一次清理掉；每次回收时都是对整个半区进行内存回收，内存分配是不用考虑内存碎片等；

适用于对象存活率低的情况；解决了碎片化问题，顺序分配内存，简单高效，适用于年轻代区域垃圾回收；

有递归（Robert R. Fenichel 和 Jerome C. Yochelson提出）和迭代（Cheney 提出）算法，以及解决了前两者递归栈、缓存行等问题的近似优先搜索算法。复制算法可以通过碰撞指针的方式进行快速地分配内存，但是也存在着空间利用率不高的缺点，另外就是存活对象比较大时复制的成本比较高。

**3.2、空间如何划分：**

由于新生代中对象98%都是"朝生夕死"的，所有不需要按照`1:1`划分内存空间，而是将内存分为一块较大的Eden空间和两块较小的Survivor空间，每次使用 Eden 和 其中一块 Survivor；
- 当回收时将Eden 和Survivor中还存活的对象一次性的复制到另外一块Survivor 间上，最后清理掉 Eden 和刚才用过的Survivor 空间；
- HotSpot虚拟机默认Eden和Survivor的大小比例是 `8:1`，每次新生代可以内存空间为整个新生代容量的`90%（80%+10%）`，只要`10%`内存会被浪费掉
- 当 Survivor 空间不够用时，需要依赖其他内存（指老年代）进行分配担保：没有足够的空间存放上一次新生代收集下来的存活对象时，这些对象将直接通过分配担保机制进入老年代；

## 4、分代收集算法（Generation Collection）

基于不同的对象的生命周期是不一样的；一般是把 Java 堆分为新生代与老年代

### 4.1、新生代（Young Generation）

- 所有新生成的对象首先都是放在年轻代的，新生代的 GC 也叫做 MinorGC，MinorGC 发生频率比较高（不一定等 Eden 区满了才触发）
- 新生代中对象生存周期短，所以选用复制算法，只需付出少量存活对象的复制成本就可以完成收集；新生代的对象朝生夕死，大约90%的新建对象可以被很快回收，复制算法成本低，同时还能保证空间没有碎片。虽然标记整理算法也可以保证没有碎片，但是由于新生代要清理的对象数量很大，将存活的对象整理到待清理对象之前，需要大量的移动操作，时间复杂度比复制算法高；

新生代对象的内存分配看下详细过程：
- 新对象会先尝试在栈上分配，如果不行则尝试在TLAB分配，否则再看是否满足大对象条件要在老年代分配，最后才考虑在Eden区申请空间；
- 如果Eden区没有合适的空间，则触发YGC；
- YGC时，对Eden区和From Survivor区的存活对象进行处理，如果满足动态年龄判断的条件或者To Survivor区空间不够则直接进入老年代，如果老年代空间也不够了，则会发生promotion failed，触发老年代的回收。否则将存活对象复制到To Survivor区；
- 此时Eden区和From Survivor区的剩余对象均为垃圾对象，可直接抹掉回收

### 4.2、老年代（Old Generation）

- 在新生代中经历了N次垃圾回收后仍然存活的对象，就会被放到年老代中.可以认为年老代中存放的都是一些生命周期较长的对象；
- 当老年代内存满时触发 Major GC 即 FullGC，FullGC 发生频率比较低，老年代对象存活时间比较长，存活率高；
- 一般使用标记-清除和标记-整理算法来进行回收；

新生代( Young ) 与老年代( Old ) 的比例的值为1:2 ( 该值可以通过参数–XX:NewRatio 来指定)，即：新生代( Young ) = 1/3 的堆空间大小。 老年代( Old ) = 2/3 的堆空间大小

### 4.3、为什么分代

如果不分代，所有对象全部在一个区域，每次GC都需要对全堆进行扫描，存在效率问题。分代后，可分别控制回收频率，并采用不同的回收算法，确保GC性能全局最优
	
### 4.2、GC的分类

#### 4.2.1、MinorGC

当发生 Minor GC 时，Eden 区和 from 指向的 Survivor 区中的存活对象会被复制到 to 指向的 Survivor 区中，然后交换 from 和 to 指针，以保证下一次 Minor GC 时，to 指向的 Survivor 区还是空的。

当发生 Minor GC 时，我们应用了标记 - 复制算法，将 Survivor 区中的老存活对象晋升到老年代，然后将剩下的存活对象和 Eden 区的存活对象复制到另一个 Survivor 区中

## 5、HotSpot虚拟机算法实现

### 5.1、枚举根节点

从可达性分析中从GC Roots节点找引用链这个操作为例，可作为GC Roots的节点主要在全局性的引用（例如常量或类静态属性）与执行上下文（例如栈帧中的局部变量表）中，现在很多应用仅仅方法区就有数百兆，如果要逐个检查这里面的引用，那么必然会消耗很多时间。

目前所有收集器在根节点枚举在这一步骤时都是必须暂停用户线程的，枚举根节点与整理内存碎片同样面临 "Stop The World"的问题；可达性分析算法耗时最长的查找引用链的过程可以做到与用户线程一起并发；

为了保证枚举期间一致性问题，枚举根节点时都是必须要停顿的；

由于目前的主流Java虚拟机使用的都是准确式GC（即使用准确式内存管理，虚拟机可用知道内存中某个位置的数据具体是什么类型），所以当执行系统停顿下来后，并不需要一个不漏地检查完所有执行上下文和全局的引用位置，虚拟机应当是有办法直接得知哪些地方存放着对象引用。在HotSpot的实现中，是使用一组称为OopMap的数据结构来达到这个目的的，在类加载完成的时候，HotSpot就把对象内什么偏移量上是什么类型的数据计算出来，在JIT编译过程中，也会在特定的位置记录下栈和寄存器中哪些位置是引用。这样，GC在扫描时就可以直接得知这些信息了，并不需要真正一个不漏的从方法区等GC Roots开始查找

- **GC停顿（STW）**

	另外，可达性分析工作必须在一个能确保一致性的快照中进行——这里“一致性”的意思是指在整个分析期间整个执行系统看起来就像被冻结在某个时间点上，不可以出现分析过程中对象引用关系还在不断变化的情况，这是保证分析结果准确性的基础。这点是导致GC进行时必须停顿所有Java执行线程（Sun将这件事情称为“Stop The World”）的其中一个重要原因，即使是在号称（几乎）不会发生停顿的CMS收集器中，枚举根节点时也是必须要停顿的

	***GC是怎么实现STW的？***

### 5.2、安全点

- [理解安全点](https://tech.dewu.com/article?id=47)
- [Safepoints: Meaning, Side Effects and Overheads](http://psy-lob-saw.blogspot.com/2015/12/safepoints.html)
- [安全点引起的代码问题](https://mp.weixin.qq.com/s/KDUccdLALWdjNBrFjVR74Q)
- [Stop the word 和 安全点](https://zhuanlan.zhihu.com/p/161710652)

安全点（SafePoint），即程序执行时并非在所有地方都能停顿下来开始GC，只有在到达安全点时才能暂停。SafePoint的选定既不能太少以至于让GC等待时间太长，也不能过于频繁以致于过分增大运行时的负荷。

安全点的初始目的并不是让其他线程停下，而是找到一个稳定的执行状态。在这个执行状态下，Java虚拟机的堆栈不会发生变化。这么一来，垃圾回收器便能够“安全”地执行可达性分析。只要不离开这个安全点，Java虚拟机便能够在垃圾回收的同时，继续运行这段本地代码。

程序运行时并非在所有地方都能停顿下来开始GC，只有在到达安全点时才能暂停。安全点的选定基本上是以程序“是否具有让程序长时间执行的特征”为标准进行选定的。“长时间执行”的最明显特征就是指令序列复用，例如方法调用、循环跳转、异常跳转等，所以具有这些功能的指令才会产生SafePoint。

在 SafePoint 保存了其他位置没有的一些当前线程的运行信息，供其他线程读取。这些信息包括：线程上下文的任何信息，例如对象或者非对象的内部指针等等。一般可以这么理解 SafePoint，就是线程只有运行到了 SafePoint 的位置，他的一切状态信息，才是确定的，也只有这个时候，才知道这个线程用了哪些内存，没有用哪些；并且，只有线程处于 SafePoint 位置，这时候对 JVM 的堆栈信息进行修改，例如回收某一部分不用的内存，线程才会感知到，之后继续运行，每个线程都有一份自己的内存使用快照，这时候其他线程对于内存使用的修改，线程就不知道了，只有再进行到 SafePoint 的时候，才会感知

对于安全点，另一个需要考虑的问题就是如何在GC发生时让所有线程（这里不包括执行JNI调用的线程）都“跑”到最近的安全点上再停顿下来。

两种解决方案：
- 抢先式中断（Preemptive Suspension）

    抢先式中断不需要线程的执行代码主动去配合，在GC发生时，首先把所有线程全部中断，如果发现有线程中断的地方不在安全点上，就恢复线程，让它“跑”到安全点上。现在几乎没有虚拟机采用这种方式来暂停线程从而响应GC事件。

- 主动式中断（Voluntary Suspension）

    主动式中断的思想是当GC需要中断线程的时候，不直接对线程操作，仅仅简单地设置一个标志，各个线程执行时主动去轮询这个标志，发现中断标志为真时就自己中断挂起。轮询标志地地方和安全点是重合的，另外再加上创建对象需要分配内存的地方

### 5.3、安全区域（Safe Region）

安全点机制保证了程序执行时，在不太长的时间内就会遇到可进入垃圾收集过程的安全点；但是，程序不执行的时候呢？所谓不执行是没有分配处理时间，典型的场景便是用户线程处理sleep状态或者 blocked状态，这时候线程无法响应虚拟机的中断请求，不能再走到安全的地方去中毒挂起自己。为了解决这种情况，引入了安全区域；

安全区域是指能够确保在某一段代码片段之中，引用关系不会发生变化，因此，在这个区域中任意地方来时垃圾收集都是安全的。

当用户线程执行到安全区域里面的代码时，首先会标识自己已经进入了安全区域，那样当这段时间里虚拟机要发起垃圾收集时就不必去管这些已声明自己在安全区域内想的线程了；当线程要离开安全区域时，它要检查虚拟机是否已完成根节点枚举（或者垃圾收集过程中其他需要暂停用户线程的阶段），如果完成了，那线程就当中没事发生过，继续执行；否则它必须一致等待，直达收到可以离开安全区域的信号为止；

### 5.4、记忆集与卡表

为解决对象跨代引用所带来的问题，垃圾收集器在新生代中建立了名为记忆集（Remembered Set）的数据结构，用以避免把整个老年代加进GC Roots扫描范围。事实上并不只是新生代、老年代之间才有跨代引用的问题，所有涉及部分区域收集（Partial GC）行为的垃圾收集器，典型的如G1、ZGC和Shenandoah收集器，都会面临相同的问题

记忆集是一种用于记录从非收集区域指向收集区域的指针集合的抽象数据结构。如果不考虑效率和成本的话，最简单的实现可以用非收集区域中所有含跨代引用的对象数组来实现这个数据结构，这种记录全部含跨代引用对象的实现方案，无论是空间占用还是维护成本都相当高昂；

在实现记忆集的时候，便可以选择更为粗犷的记录粒度来节省记忆集的存储和维护成本，下面列举了一些可供选择（当然也可以选择这个范围以外的）的记录精度：
- 字长精度： 每个记录精确到一个机器字长（就是处理器的寻址位数，如常见的32位或64位，这个精度决定了机器访问物理内存地址的指针长度），该字包含跨代指针；
- 对象精度： 每个记录精确到一个对象，该对象里有字段含有跨代指针；
- 卡精度： 每个记录精确到一块内存区域，该区域内有对象含有跨代指针；

第三种“卡精度”所指的是用一种称为“卡表”（Card Table）的方式去实现记忆集，这也是目前最常用的一种记忆集实现形式；卡表最简单的形式可以只是一个字节数组，主要是用来标记卡页的状态，每个卡表项对应一个卡页，将整个堆划分为一个个大小为 512 字节的卡，并且维护一个卡表，用来存储每张卡的一个标识位。这个标识位代表对应的卡是否可能存有指向新生代对象的引用。如果可能存在，那么我们就认为这张卡是脏的；
```java
CARD_TABLE [this address >> 9] = 0;
```
字节数组CARD_TABLE的每一个元素都对应着其标识的内存区域中一块特定大小的内存块，这个内存块被称作“卡页”（Card Page）。一般来说，卡页大小都是以2的N次幂的字节数，通过上面代码可以看出HotSpot中使用的卡页是2的9次幂，即512字节（地址右移9位，相当于用地址除以512）。那如果卡表标识内存区域的起始地址是0x0000的话，数组CARD_TABLE的第0、1、2号元素，分别对应了地址范围为`0x0000～0x01FF`、`0x0200～0x03FF`、`0x0400～0x05FF`的卡页内存块;

一个卡页的内存中通常包含不止一个对象，只要卡页内有一个（或更多）对象的字段存在着跨代指针，那就将对应卡表的数组元素的值标识为1，称为这个元素变脏（Dirty），没有则标识为0。在垃圾收集发生时，只要筛选出卡表中变脏的元素，就能轻易得出哪些卡页内存块中包含跨代指针，把它们加入GC Roots中一并扫描，详细资料参考：[how-actually-card-table-and-writer-barrier-works](https://stackoverflow.com/questions/19154607/how-actually-card-table-and-writer-barrier-works)；

在进行 Minor GC 的时候，我们便可以不用扫描整个老年代，而是在卡表中寻找脏卡，并将脏卡中的对象加入到 Minor GC 的 GC Roots 里。当完成所有脏卡的扫描之后，Java 虚拟机便会将所有脏卡的标识位清零。

### 5.5、写屏障

前面已经解决如何使用记忆集来缩减`GC Roots`扫描范围的问题，但还没有解决卡表元素如何维护的问题，例如它们何时变脏、谁来把它们变脏等

**卡表何时变脏的？**
有其他分代区域中对象引用了本区域对象时，其对应的卡表元素就应该变脏，变脏时间点原则上应该发生在引用类型字段赋值的那一刻；

**如何维护卡表状态：**在HotSpot虚拟机里是通过写屏障（Write Barrier）技术维护卡表状态的，写屏障可以看作在虚拟机层面对“引用类型字段赋值”这个动作的AOP切面；在引用对象赋值时会产生一个环形（Around）通知，供程序执行额外的动作，也就是说赋值的前后都在写屏障的覆盖范畴内。在赋值前的部分的写屏障叫作写前屏障（Pre-Write Barrier），在赋值后的则叫作写后屏障（Post-Write Barrier）；

卡表在高并发场景下还面临着“伪共享”（False Sharing）问题；在JDK 7之后，HotSpot虚拟机增加了一个新的参数-XX：+UseCondCardMark，用来决定是否开启卡表更新的条件判断。开启会增加一次额外判断的开销，但能够避免伪共享问题，两者各有性能损耗

## 6、STW（Stop The World）

- [SafePoint与STW全解](https://juejin.cn/post/6854573211968143373)
- [JIT Compile](https://krzysztofslusarski.github.io/2021/08/25/monday-jit.html)

全局停顿，Java代码停止运行，native代码继续运行，但是不能与JVM进行交互；

Java 虚拟机中的 Stop-the-world 是通过安全点（safepoint）机制来实现的。当 Java 虚拟机收到 Stop-the-world 请求，它便会等待所有的线程都到达安全点，才允许请求 Stop-the-world 的线程进行独占的工作

### 6.1、哪些情况会发生STW

哪些情况下会让所有线程进入 SafePoint， 即发生 Stop the world：
- **定时进入 SafePoint**：每经过`-XX:GuaranteedSafepointInterval` 配置的时间，都会让所有线程进入 Safepoint，一旦所有线程都进入，立刻从 Safepoint 恢复。这个定时主要是为了一些没必要立刻 Stop the world 的任务执行，可以设置`-XX:GuaranteedSafepointInterval=0`关闭这个定时，推荐关闭；
- **由于 jstack，jmap 和 jstat 等命令**，也就是 Signal Dispatcher 线程要处理的大部分命令，都会导致 Stop the world：这种命令都需要采集堆栈信息，所以需要所有线程进入 Safepoint 并暂停；
- **偏向锁取消**（这个不一定会引发整体的 Stop the world）：高并发的情况下，偏向锁会经常失效，导致需要取消偏向锁，取消偏向锁的时候，需要 Stop the world，因为要获取每个线程使用锁的状态以及运行状态；
- **Java Instrument 导致的 Agent 加载以及类的重定义**：由于涉及到类重定义，需要修改栈上和这个类相关的信息，所以需要 Stop the world；
- **Java Code Cache相关**：当发生 JIT 编译优化或者去优化，需要 OSR 或者 Bailout 或者清理代码缓存的时候，由于需要读取线程执行的方法以及改变线程执行的方法，所以需要 Stop the world；
- **GC**：这个由于需要每个线程的对象使用信息，以及回收一些对象，释放某些堆内存或者直接内存，所以需要 Stop the world；
- **JFR 的一些事件**：如果开启了 JFR 的 OldObject 采集，这个是定时采集一些存活时间比较久的对象，所以需要 Stop the world。同时，JFR 在 dump 的时候，由于每个线程都有一个 JFR 事件的 buffer，需要将 buffer 中的事件采集出来，所以需要 Stop the world

所有的参考：[vmOperation.hpp](https://github.com/openjdk/jdk/blob/master/src/hotspot/share/runtime/vmOperation.hpp)

## 7、三色标记

- [三色标记过程](https://www.cnblogs.com/jmcui/p/14165601.html#!comments)
- [什么是三色标记](https://mp.weixin.qq.com/s/01V4wDOmlodnR4mLpZrm8A)

垃圾回收算法，基本就是那么几种：标记-清除、标记-复制、标记-整理。在此基础上可以增加分代（新生代/老年代），每代采取不同的回收算法，以提高整体的分配和回收效率。

无论使用哪种算法，标记总是必要的一步。这是理算当然的，如果不先找到垃圾，怎么进行回收？垃圾回收器的工作流程大体如下：
- 标记出哪些对象是存活的，哪些是垃圾（可回收）；
- 进行回收（清除/复制/整理），如果有移动过对象（复制/整理），还需要更新引用

三色标记法是一种垃圾回收法，它可以让JVM不发生或仅短时间发生STW(Stop The World)，从而达到清除JVM内存垃圾的目的。JVM中的CMS、G1垃圾回收器 所使用垃圾回收算法即为三色标记法

### 7.1、双色标记

在之前的可达性（Root-Tracing）GC中一般包含两个过程：第一个是标记、第二个是清除，标记过程通常被抽象为上色的过程

比如双色标记：

![](image/GC-双色标记.png)

从Root Object出发，如果发现可以标记的，涂成黑色。这样结束的时候所有白色都可以清除

常规的标记-清除过程：

mark -> sweep -> mutation -> mark -> sweep ...

标记、清除是GC的执行环节，然后中间还要安插应用执行的环境，这个环节对于GC来说，本质上是变更（mutation），在发生变更的时候，会有新的垃圾产生。在并发要求较高的场景下，标记、清除连续执行延迟可能会较高，这个时候，标记进行完（甚至一半），有可能需要变更

![](image/GC-双色标记-mutation.png)

标记进行完成后，由于延迟控制等原因，不能执行清除工作（因为STW时间不能太长），需要马上进行工作（变更）。此时，看到原本标记好的，不可达的节点C又可达了，如果此时马上进行清除，就会将CDE全部清除掉，这是错误的回收；为什么B会引用到已删除的C呢？因为程序执行在一个并发环境，在某一个时刻可能有一个西线程删除了 X->C的引用，另一个线程又添加了B->C的引用

双色标记清除算法比较适合单线程工作

### 7.2、三色标记思想与过程

三色标记在双色标记的基础上增加了一种颜色（灰色），代表未完成的工作；

![](image/GC-三色标记-1.png)

我们把遍历对象图过程中遇到的对象，按“是否访问过”这个条件标记成以下三种颜色：
- 白色：尚未访问过。该对象没有被标记过。（对象垃圾）
- 灰色：本对象已访问过，但是本对象引用到的其他对象尚未全部访问完（GC需要从此对象中去寻找垃圾）
- 黑色：本对象已访问过，而且本对象引用到的其他对象也全部访问过了。全部访问后，会转换为黑色（程序所需要的对象）

当存在不确定数据时，sweep无法进行，继续标记

![](image/GC-三色标记-2.png)

假设现在有白、灰、黑三个集合（表示当前对象的颜色），其遍历访问过程为：
- （1）初始时，所有对象都在【白色集合】中；
- （2）从 GC Roots 开始遍历所有对象（注意这里并不递归遍历 ），把遍历到的对象从白色集合放入【灰色集合】
- （3）从灰色集合中获取对象：
    - 将灰色对象引用的对象从【白色集合】放入 【灰色集合】中；
    - 将此灰色对象放入【黑色集合】里面。
- （4）重复步骤3，直至【灰色集合】为空时结束。
- （5）结束后，仍在【白色集合】的对象即为 GC Roots 不可达，可以进行回收

![](image/GC-三色标记-3.png)

### 7.3、三色标记存在问题

- 浮动垃圾：并发标记的过程中，若一个已经被标记成黑色或者灰色的对象，突然变成了垃圾，由于不会再对黑色标记过的对象重新扫描，所以不会被发现，那么这个对象不是白色的但是不会被清除，重新标记也不能从GC Root中去找到，所以成为了浮动垃圾，浮动垃圾对系统的影响不大，留给下一次GC进行处理即可；

- 对象漏标问题（需要的对象被回收）：并发标记的过程中，一个业务线程将一个未被扫描过的白色对象断开引用成为垃圾（删除引用），同时黑色对象引用了该对象（增加引用）（这两部可以不分先后顺序）；因为黑色对象的含义为其属性都已经被标记过了，重新标记也不会从黑色对象中去找，导致该对象被程序所需要，却又要被GC回收，此问题会导致系统出现问题，而CMS与G1，两种回收器在使用三色标记法时，都采取了一些措施来应对这些问题，CMS对增加引用环节进行处理（Increment Update），G1则对删除引用环节进行处理(SATB)

在JVM虚拟机中有两种常见垃圾回收器使用了该算法：CMS(Concurrent Mark Sweep)、G1(Garbage First) ，为了解决三色标记法对对象漏标问题各自有各自的法

### 7.4、CMS解决办法：增量更新

在应对漏标问题时，CMS使用了增量更新(Increment Update)方法来做：在一个未被标记的对象（白色对象）被重新引用后，**引用它的对象若为黑色则要变成灰色，在下次二次标记时让GC线程继续标记它的属性对象** 。

但是就算时这样，其仍然是存在漏标的问题：
- 在一个灰色对象正在被一个GC线程回收时，当它已经被标记过的属性指向了一个白色对象（垃圾）；
- 而这个对象的属性对象本身还未全部标记结束，则为灰色不变；
- **而这个GC线程在标记完最后一个属性后，认为已经将所有的属性标记结束了，将这个灰色对象标记为黑色，被重新引用的白色对象，无法被标记**；

CMS的其他问题：
- CMS采用了`Mark-Sweep`算法，最后会产生许多内存碎片，当到一定数量时，CMS无法清理这些碎片了，CMS会让`Serial Old`垃圾处理器来清理这些垃圾碎片，而`Serial Old`垃圾处理器是单线程操作进行清理垃圾的，效率很低。所以使用CMS就会出现一种情况，硬件升级了，却越来越卡顿，其原因就是因为进行`Serial Old GC`时，效率过低。
    - 解决方案：使用`Mark-Sweep-Compact`算法，减少垃圾碎片
    - 调优参数（配套使用）：
    ```
    -XX:+UseCMSCompactAtFullCollection  开启CMS的压缩
    -XX:CMSFullGCsBeforeCompaction      默认为0，指经过多少次CMS FullGC才进行压缩
    ```
- 当JVM认为内存不够，再使用CMS进行并发清理内存可能会发生OOM的问题，而不得不进行`Serial Old GC`，`Serial Old`是单线程垃圾回收，效率低
    - 解决方案：降低触发`CMS GC`的阈值，让浮动垃圾不那么容易占满老年代
    - 调优参数：
    ```
    -XX:CMSInitiatingOccupancyFraction 92%     可以降低这个值，让老年代占用率达到该值就进行CMS GC
    ```

### 7.5、G1解决办法：SATB

SATB(Snapshot At The Beginning), 在应对漏标问题时，G1使用了SATB方法来做,具体流程：
- 在开始标记的时候生成一个快照图标记存活对象
- 在一个引用断开后，要将此引用推到GC的堆栈里，保证白色对象（垃圾）还能被GC线程扫描到(在write barrier(写屏障) 里把所有旧的引用所指向的对象都变成非白的)。
- 配合Rset，去扫描哪些Region引用到当前的白色对象，若没有引用到当前对象，则回收；

# 四、垃圾收集器

- 内存回收的具体实现：
	- 新生代收集器使用的收集器：Serial、PraNew、Parallel Scavenge
	- 老年代收集器使用的收集器：Serial Old、Parallel Old、CMS

- 分类：
	- 串行收集器Serial：Serial、Serial Old
	- 并行收集器Parallel：Parallel Scavenge、Parallel Old，以吞吐量

		指多条垃圾收集线程并行工作，但此时用户线程仍然处于等待状态，适合科学计算、后台处理等弱交互场景

	- 并发收集器Concurrent：CMS、G1，有一定的停顿时间

		指用户线程与垃圾收集线程同时执行（但不一定是并行的，可能会交替执行），垃圾收集线程在执行的时候不会停顿用户程序的运行，适合对响应时间有要求的场景，如web

- 停顿时间与吞吐量
	- 停顿时间：垃圾收集器做垃圾回收中断应用执行的时间。`-XX:MaxGCPauseMillis`
	- 吞吐量：花在垃圾收集的时间和花在应用时间的占比。`-XX:GCTimeRatio=<n>`，垃圾收集时间占：1/（1+n）
	
	*最优结果：在最大吞吐量的时候，停顿时间最短*

## 1、Serial 收集器

**算法：**

Serial收集器对于新生代采用复制算法实现，对于老年代采用标记-整理算法实现，使用JVM参数`-XX:UseSerialGC`开启

**单线程收集器**
- 使用一个CPU或一条收集线程去完成垃圾收集工作；
- 它进行垃圾收集时，必须暂停其他所有的工作线程，直到它收集结束，又称为STOP THE WORLD（STW）；
- 依然是虚拟机运行在 Client 模式下默认新生代收集器

**特点：**

简单而高效（与其他收集器的单线程比），对于限定单个CPU环境来说， Serial 收集器由于没有线程交互的开销，专心做垃圾收集自然可以获得最高的单线程收集效率；

***适用场景：**
- 吞吐量小：内存回收工作量不大；
- 容忍延迟：不在意卡顿；
- 单核、内存小：0~100M


## 2、ParNew 收集器

- 算法：新生代采用复制算法
- 提高最大量的吞吐量（Throughput）
- ParNew收集器，可以认为是Serial收集器的多线程版本，在多核CPU环境下有着比Serial更好的表现；其他与Serial收集器实现基本差不多；
- 是运行在 Server 模式下的虚拟机中首选的新生代收集器，其中与性能无关的是：只有 Serial 收集器和 ParNew 收集器能与 CMS 收集配合工作；
- 在JDK1.5中使用CMS来收集老年代的时候，新生代只能选用Serial、ParNew收集器中的一个，ParNew收集器使用`-XX:UseConcMarkSweepGC`选项后的默认新生代收集器，也可以使用 `-XX:+UseParNewGC` 选项来强制指定它；
- ParNew 收集器在单 CPU 的环境中绝对不会有比 Serial 收集器有更好的效果，甚至由于存在线程交互的开销，该收集器在通过超线程技术实现的两个CP 的环境中都不能百分之百地保证可以超越；
- 场景：吞吐量要求 > 延迟要求
- 可以通过 `-XX:ParallelGCThreads` 参数来控制收集的线程数，整个过程都是 STW 的，常与 CMS 组合使用

## 3、Parallel Scavenge 收集器

- 算法：堆内存年轻代采用“复制算法”；配合收集器：`ParallelOldGC`，堆内存老年代采用“标记-整理算法”；
- 并行的多线程收集器；只适用于新生代；在Server模式下的默认的年轻代收集器
- Parallel Scavenge 收集器目标是达到一个控制的吞吐量（Throughput，CPU 运行用户代码的时间与CPU总消耗的时间的比值）；提供了两个参数用于控制吞吐量，分别是控制最大垃圾收集停顿时间的：`-XX:MaxGCPauseMillis`，其值是一个大于0的毫秒数；以及直接设置吞吐量大小的：`-XX:GCTimeRatio`参数，其值时0~100之间的整数
- GC 自适应调整策略；`-XX:+UseAdaptiveSizePolicy`，这个是一个开关参数，当参数被激活之后，不需要人工指定新生代的大小、Ende和Survivor的比例、晋升老年代对象的大小等细节参数；（HotSpot VM里，Parallel Scavenge系的GC（UseParallelGC / UseParallelOldGC）默认行为是SurvivorRatio如果不显式设置就没啥用。显式设置到跟默认值一样的值则会有效果）
- 无法与 CMS 收集器配合工作：Parallel Scavenge没有使用原本HotSpot其它GC通用的那个GC框架，所以不能跟使用了那个框架的CMS搭配使用；有一个分代式GC框架，Serial/Serial Old/ParNew/CMS都在这个框架内；在该框架内的young collector和old collector可以任意搭配使用，所谓的“mix-and-match”。 
而ParallelScavenge与G1则不在这个框架内，而是各自采用了自己特别的框架。这是因为新的GC实现时发现原本的分代式GC框架用起来不顺手。
- 适用于吞吐量的

## 4、Serial Old 收集器

Serial 收集器的老年代版本，采用标记-整理算法实现（-XX:+UseSerialOldGC）

- 主要是在 Client 模式下的虚拟机使用，在 Server 模式下，有两大用途：
	- 在 JDK1.5 以及之前的版本中与 Parallel Scavenge 收集器搭配使用；
	- 作为 CMS 收集器的后备预案在并发收集发生 Concurrent Mode Failure 时使用；

## 5、Parallel Old 收集器

- Parallel Scavenge 收集器的老年代版本，使用标记-整理算法
- JDK1.6 之后才开始提供的
- 在注重吞吐量一级CPU资源敏感的场合，可以优先考虑Parallel Scavenge加parallel old收集器

## 6、CMS（Concurrent Mark Sweep）收集器-老年代

它管理新生代的方式与Parallel收集器和Serial收集器相同，而在老年代则是尽可能得并发执行，每个垃圾收集器周期只有2次短停顿；能与 CMS 搭配使用的新生代垃圾收集器有 Serial 收集器和 ParNew 收集器；

追求最短 GC 回收停顿时间，为了消除throughput收集器和Serial收集器在Full GC周期中的长时间停顿，核心是减少停顿时间;

> 注意：在JDK9 被标记弃用，JDK14 被删除。

### 6.1、实现

基于标记-清除算法实现的，运作过程可以分为4个步骤：
- 初始标记（CMS initial mark）：仅仅只是标记一下 GC Roots 能直接关联到的对象，速度很快，需要“Stop TheWorld”；
- 并发标记（CMS concurrent mark）：进行GC Roots追溯所有对象的过程，在整个过程中耗时最长，可以与垃圾收集线程并行
- 重新标记（CMS remark）：为了修正并发标记期间因用户程序继续运作而导致标记产生变动的那一部分对象的标记记录，这个阶段的停顿时间一般会比初始标记阶段稍长一些，但远比并发标记的时间短。此阶段也需要“Stop The World”；
- 并发清除（CMS concurrent sweep）：清理删除掉标记阶段判断的已经死亡的对象，由于不需要移动存活对象，可以与用户线程同时并发；

其中初始标记、重新标记这两个步骤仍然需要"Stop the world"。初始标记仅仅是标记以下 GC Roots 能直接关联到的对象，速度很快，并发标记阶段就是进行GC Roots Tracing 的过程，而重新阶段则是为了修正并发标记期间因用户程序继续运作而导致标记常数变动的那一部分对象的标记记录，这个阶段的停顿时间一般会比初始标记的阶段稍微长点，但远比并发标记阶段时间短；

当GC线程标记好了一个对象的时候，此时我们程序的线程又将该对象重新加入了“关系网”中，当执行二次标记的时候，该对象也没有重写finalize()方法，因此回收的时候就会回收这个不该回收的对象。虚拟机的解决方法就是在一些特定指令位置设置一些“安全点”，当程序运行到这些“安全点”的时候就会暂停所有当前运行的线程（Stop The World 所以叫STW），暂停后再找到“GC Roots”进行关系的组建，进而执行标记和清除

### 6.2、优缺点

- 优点：低延迟，尤其对于大堆来说。大部分垃圾回收过程并发执行。
- 缺点：
	- CMS 收集器对CPU资源非常敏感，导致应用吞吐量的降低
	- CMS 收集无法处理浮动垃圾(Floating Garbage)，可能出现 Concurrent Mode Failure 失败而导致一次 Full GC 的产生
	- CMS 基于标记-清除算法实现的，那么垃圾收集结束后会产生大量的空间碎片，空间碎片过多时，将会给大对象的分配带来很大麻烦，往往出现老年代还有很大空间剩余，但是无法找到足够大的连续空间来分配当前对象们，不得不提前触发一次 Full GC。

[CMS碎片率](https://mp.weixin.qq.com/s/VnzupPEFTl7Z_Zbdsfw6_Q)
常见以下场景会触发内存碎片压缩：
- 新生代 Young GC 出现新生代晋升担保失败（promotion failed)）
- 程序主动执行System.gc()；

### 6.3、CMS相关参数

- `-XX:+UseConcMarkSweepGC`
- `-XX:ConcGCThreads`：并发的GC线程数；
- `-XX:+UseCMSCompactAtFullCollection`：（默认开启），表示在要进行 Full GC 的时候，进行内存碎片整理。内存整理的过程是无法并发的，所以停顿时间会变长
- `-XX:CMSFullGCsBeforeCompaction`：每隔多少次不压缩的 Full GC 后，执行一次带压缩的 Full GC。默认值为 0，表示每次进入 Full GC 时都进行碎片整理
- `-XX:CMSInitiatingOccupancyFraction`：触发Full GC，老年代内存使用占比达到 `CMSInitiatingOccupancyFraction`，默认为 92%
- `-XX:+UseCMSInitiatingOccupancyOnly`：是否动态调整
- `-XX:+CMSScavengeBeforeRemark`：full gc之前先做YGC
- `-XX:+CMSClassUnloadingEnabled`：启用回收Perm区，针对JDK8之前的

### 6.4、CMS中Young Gc的实现过程

- 先找出根对象，如Java栈中引用的对象、静态变量引用的对象和系统词典中引用的对象等待，把这些对象标记成活跃对象，并复制到to区；
- 接着遍历这些活跃对象中引用的对象并标记，找出老年代对象在eden区有引用关系的对象并标记；
- 最后把这些标记的对象复制到to，在复制过程还要判断活跃对象的gc年龄是否已经达到阈值，如果已经达到阈值，就直接晋升到老年代，YGC结束之后把from和to的引用互换

CMS GC 时出现`promotion failed`和`concurrent mode failure`
- promotion failed：是在进行 Minor GC时，survivor空间放不下、对象只能放入老生代，而此时老生代也放不下造成的；
	
	产生的原因即问题定位：
	- survivor 区太小，对象过早进入老年代：
		- `jstat -gcutil pid 1000` 观察内存运行情况；
		- `jinfo pid` 查看 SurvivorRatio 参数；
	- 大对象分配，没有足够的内存：
		- 日志查找关键字 “allocating large”；
		- `profiler` 查看内存概况大对象分布；
	- old 区存在大量对象：
		- 实例数量前十的类：`jmap -histo pid | sort -n -r -k 2 | head -10`
		- 实例容量前十的类：`jmap -histo pid | sort -n -r -k 3 | head -10`
		- dump 堆，profiler 分析对象占用情况
	
	解决办法：`-XX:UseCMSCompactAtFullCollection -XX:CMSFullGCBeforeCompaction=5` 或者调大新生代或者Survivor空间

- concurrent mode failure：在执行 CMS GC 的过程中同时有对象要放入老生代，而此时老生代空间不足造成的；

	- fgc 触发比例过大，导致老年代占用过多，并发收集时用户线程持续产生对象导致达到触发 FGC 比例。
		- `jinfo` 查看 `CMSInitiatingOccupancyFraction` 参数，一般 70~80 即可
	- 老年代存在内存碎片。
		- `jinfo` 查看 `UseCMSCompactAtFullCollection` 参数，在 FullGC 后整理内存

	解决办法：`+XX:CMSInitiatingOccupancyFraction`，调大老年代的空间，`+XX:CMSMaxAbortablePrecleanTime`

### 6.5、场景

- 覆盖Serial、Parallel的场景；
- 需要减少Pause Time 的场景；

## 7、G1收集器（Garbage First）

* [G1-GC](https://tech.meituan.com/2016/09/23/g1.html)
* [G1垃圾收集器](https://mp.weixin.qq.com/s/9-NFMt4I9Hw2nP0fjR8JCg)
* [Hotspot-G1-GC的一些关键技术](https://zhuanlan.zhihu.com/p/22591838)

通过`-XX:+UseG1GC`参数来启用，作为体验版随着JDK 6u14版本面世，在JDK 7u4版本发行时被正式推出，在JDK 9中，G1被提议设置为默认垃圾收集器；取代之前 Parallel Scavenge 加 Parallel Old的组合，成为服务端模式下的默认垃圾收集器；在JDK9之后的版本中，CMS垃圾收集器被声明为不推荐使用了，如果使用`-XX:+UseConcMarkSweepGC`来开启CMS收集器，会收到一个警告信息； 

G1是一种服务器端的垃圾收集器，应用在多处理器和大容量内存环境中，在实现高吞吐量的同时，尽可能的满足垃圾收集暂停时间的要求；

G1 最主要的设计目标是：实现可预期及可配置的 STW 停顿时间

**为什么需要G1？**

CMS 垃圾回收器在发生 Young GC 时，由于 Survivor 区已经放不下了，多出的对象只能提升（promotion）到老年代。但是此时老年代因为空间碎片的缘故，会发生 concurrent mode failure 的错误。这个时候，就需要降级为 Serail Old 垃圾回收器进行收集。这就是比 concurrent mode failure 更加严重的 promotion failed 问题；一次简单的 Young GC，竟然能演化成耗时最长的 Full GC。最要命的是，这个停顿时间是不可预知的

### 7.1、特点

- 并行与并发
- 分代收集
- 空间整合：整体上看来是基于“标记-整理”算法实现的，从局部（两个Region）上看来是基于“复制”算法实现的；这两种算法意味着G1运行期间不会产生内存空间碎片；
- 可预测停顿：降低停顿时间，G1收集器可以非常精确地控制停顿，既能让使用者明确指定在一个长度为M毫秒的时间片段内，消耗在垃圾收集上的时间不得超过N毫秒，这几乎已经是实时Java的垃圾收集器的特征；
- G1将整个Java堆（包括新生代、老年代）划分为多个大小相等的内存块（Region），每个Region 是逻辑连续的一段内存，在后台维护一个优先列表，每次根据允许的收集时间，优先回收垃圾最多的区域；进行垃圾回收时，将Region作为单次回收的最小单元，每次收集到的内存空间都是Region大小的整数倍，可以有计划的避免在整个Java堆中进行全区域的垃圾收集；

G1收集器的设计目标是取代CMS收集器，它同CMS相比，在以下方面表现的更出色：
- G1是一个有整理内存过程的垃圾收集器，不会产生很多内存碎片；
- G1的Stop The World(STW)更可控，G1在停顿时间上添加了预测机制，用户可以指定期望停顿时间；

与其他收集器的区别：其将整个Java堆划分为多个大小相等的独立区域(Region)。虽然还保留了新生代和老年代的概念，但是新生代和老年代不再是物理隔离的，它们都是一部分独立区域(不要求连续)的集合

### 7.2、G1中重要的概念

在G1的实现过程中，引入了一些新的概念，对于实现高吞吐、没有内存碎片、收集时间可控等功能起到了关键作用

#### 7.2.1、Region

传统的GC收集器将连续的内存空间划分为`新生代、老年代和永久代（JDK 8去除了永久代，引入了元空间Metaspace）`，这种划分的特点是各代的存储地址是连续的；而G1的各代存储地址是不连续的，每一代都使用了n个不连续的大小相同的Region，每个Region占有一块连续的虚拟内存地址：

![image](image/G1-Region.jpg)

region的大小是一致，数值在`1M到32M字节之间的一个2的幂值`，`JVM会尽量划分2048个左右、同等大小的region`，这点从源码[heapRegionBounds.hpp](http://hg.openjdk.java.net/jdk/jdk/file/fa2f93f99dbc/src/hotspot/share/gc/g1/heapRegionBounds.hpp)可以看到；

为实现大内存空间的低停顿时间的回收，将划分为多个大小相等的 Region。每个小堆区都可能是 Eden 区，Survivor 区或者 Old 区，但是在同一时刻只能属于某个代；

在上图中，我们注意到还有一些Region标明了H，它代表Humongous，这表示这些Region存储的是巨大对象（humongous object，H-obj），即大小大于等于region一半的对象；H-obj有如下几个特征：
- H-obj直接分配到了`old gen`，防止了反复拷贝移动；
- H-obj在`global concurrent marking`阶段的`cleanup 和 full GC`阶段回收
- 在分配H-obj之前先检查是否超过`initiating heap occupancy percent`和`the marking threshold`, 如果超过的话，就启动`global concurrent marking`，为的是提早回收，防止`evacuation failures` 和full GC

为了减少连续H-objs分配对GC的影响，需要把大对象变为普通的对象，建议增大Region size

一个Region的大小可以通过参数`-XX:G1HeapRegionSize`设定，取值范围从1M到32M，且是2的指数，默认是1M。如果不设定，那么G1会根据Heap大小自动决定。相关的设置可以参考hostspot源文件：heapRegion.cpp
```c++
void HeapRegion::setup_heap_region_size
```

G1 把堆内存划分成一个个 Region 的意义在于：
- 每次 GC 不必都去处理整个堆空间，而是每次只处理一部分 Region，实现大容量内存的 GC；
- 通过计算每个 Region 的回收价值，包括回收所需时间、可回收空间，在有限时间内尽可能回收更多的垃圾对象，把垃圾回收造成的停顿时间控制在预期配置的时间范围内；

G1 也是有 Eden 区和 Survivor 区的概念的，只不过它们在内存上不是连续的，而是由一小份一小份组成的

**region的设计有什么副作用：**

region的大小和大对象很难保证一致，这会导致空间浪费；并且region太小不合适，会令你在分配大对象时更难找到连续空间；

#### 7.2.2、SATB（Snapshot-At-The-Beginning）

GC开始时活着的对象的一个快照，其是通过Root Tracing得到的，作用是维持并发GC的正确性

标记数据结构包括了两个位图：previous位图和next位图。previous位图保存了最近一次完成的标记信息，并发标记周期会创建并更新next位图，随着时间的推移，previous位图会越来越过时，最终在并发标记周期结束的时候，next位图会将previous位图覆盖掉

#### 7.2.3、CardTable

由于在进行YoungGC时，我们在进行对一个对象是否被引用的过程，需要扫描整个Old区，所以JVM设计了CardTable，将Old区分为一个一个Card，一个Card有多个对象；如果一个Card中的对象有引用指向Young区，则将其标记为Dirty Card，下次需要进行YoungGC时，只需要去扫描Dirty Card即可。

Card Table 在底层数据结构以 Bit Map实现

#### 7.2.4、RSet（Remembered Set）

是辅助GC过程的一种结构，典型的空间换时间工具，和Card Table有些类似，还有一种数据结构也是辅助GC的：Collection Set（CSet），它记录了GC要收集的Region集合，集合里的Region可以是任意年代的；在GC的时候，对于`old->young`和`old->old`的跨代对象引用，只要扫描对应的CSet中的RSet即可

- **RSet究竟是怎么辅助GC的呢？**

	在做YGC的时候，只需要选定`young generation region`的RSet作为根集，这些RSet记录了`old->young`的跨代引用，避免了扫描整个old generation。 而mixed gc的时候，old generation中记录了`old->old`的RSet，`young->old`的引用由扫描全部young generation region得到，这样也不用扫描全部old generation region。所以RSet的引入大大减少了GC的工作量

#### 7.2.5、Pause Prediction Model-停顿预测模型

G1 GC是一个响应时间优先的GC算法，它与CMS最大的不同是，用户可以设定整个GC过程的期望停顿时间，参数`-XX:MaxGCPauseMillis`指定一个G1收集过程目标停顿时间，默认值200ms，不过它不是硬性条件，只是期望值。

G1根据这个模型统计计算出来的历史数据来预测本次收集需要选择的Region数量，从而尽量满足用户设定的目标停顿时间，停顿预测模型是以衰减标准偏差为理论基础实现的


### 7.3、G1 GC过程

G1 还有一个 CSet 的概念。这个就比较好理解了，它的全称是 Collection Set，即收集集合，保存一次 GC 中将执行垃圾回收的区间（Region）。GC 是在 CSet 中的所有存活数据（Live Data）都会被转移

#### 7.3.1、GC模式

G1提供了两种GC模式，Young GC和Mixed GC，两种都是完全Stop The World的
- Young GC：选定所有年轻代里的Region。通过控制年轻代的region个数，即年轻代内存大小，来控制young GC的时间开销；
- Mixed GC：当老年代空间达到阈值会触发 Mixed GC时，会选定所有年轻代里的Region，外加根据global concurrent marking统计得出收集收益高的若干老年代Region。在用户指定的开销目标范围内尽可能选择收益高的老年代Region；

Mixed GC不是full GC，它只能回收部分老年代的Region，如果mixed GC实在无法跟上程序分配内存的速度，导致老年代填满无法继续进行Mixed GC，就会使用serial old GC（Full GC）来收集整个GC heap，G1 GC 没有Full GC；

为了避免G1引发FullGC，实践思路：
- 增加预留内存（增大 `-XX:G1ReservePercent`，默认为堆的10%）；
- 更早的回收垃圾（减少 `-XX:InitiatingHeapOccupancyPercent`，老年代达到该值时就触发Mixed GC，默认是45%）
- 增加并发阶段使用的线程数（增大 -`XX:ConcGCThreads`）;

#### 7.3.2、年轻代回收

年轻代回收是一个 STW 的过程，它的跨代引用使用 RSet 数据结构来追溯，会一次性回收掉年轻代的所有 Region；

JVM 启动时，G1 会先准备好 Eden 区，程序在运行过程中不断创建对象到 Eden 区，当所有的 Eden 区都满了，G1 会启动一次年轻代垃圾回收过程
年轻代的收集包括下面的回收阶段：
- 扫描根：根，可以看作是 GC Roots，加上 RSet 记录的其他 Region 的外部引用；
- 更新 RS：处理 dirty card queue 中的卡页，更新 RSet。此阶段完成后，RSet 可以准确的反映老年代对所在的内存分段中对象的引用。可以看作是第一步的补充；
- 处理 RS：识别被老年代对象指向的 Eden 中的对象，这些被指向的 Eden 中的对象被认为是存活的对象
- 复制对象：收集算法依然使用的是 Copy 算法；在这个阶段，对象树被遍历，Eden 区内存段中存活的对象会被复制到 Survivor 区中空的 Region。这个过程和其他垃圾回收算法一样，包括对象的年龄和晋升；
- 处理引用：处理 Soft、Weak、Phantom、Final、JNI Weak 等引用。结束收集

#### 7.3.3、global concurrent marking

它的执行过程类似CMS，但是不同的是，在G1 GC中，它主要是为Mixed GC提供标记服务的，并不是一次GC过程的一个必须环节。

全局并发标记主要是为 Mixed GC 计算找出回收收益较高的 Region 区域，具体分为 5 个阶段：
- 初始标记（Initial Marking）：仅仅只是标记一下 GC Roots 能直接关联到的对象，速度很快，需要“Stop TheWorld”，暂停所有应用线程（STW），并发地进行标记从 GC Root 开始直接可达的对象（原生栈对象、全局对象、JNI 对象）；
- 并发标记（Concurrent Marking）：进行 GC Roots 追溯所有对象的过程，可与用户程序并发执行，并且收集各个Region的存活对象信息
- 最终标记（Final Marking）：修正在并发标记期间因用户程序继续运作而导致标记产生变动的那一部分标记记录；该阶段是 STW的
- 清除垃圾（Cleanup）：清除空Region（没有存活对象的），加入到free list；该阶段不会清理垃圾对象，也不会执行存活对象的复制。该阶段是 STW的

复制阶段停顿分析：复制算法中的转移阶段需要分配新内存和复制对象的成员变量。转移阶段是 STW的，其中内存分配通常耗时非常短，但对象成员变量的复制耗时有可能较长，这是因为复制耗时与存活对象数量与对象复杂度成正比。对象越复杂，复制耗时越长。

第一阶段initial mark是共用了Young GC的暂停，这是因为他们可以复用root scan操作，所以可以说global concurrent marking是伴随Young GC而发生的。第四阶段Cleanup只是回收了没有存活对象的Region，所以它并不需要STW

#### 7.3.4、G1 GC发生时机

**1、YoungGC发生时机**

当新生代的空间不足时，G1 触发 Young GC 回收新生代空间。

Young GC 主要是对 Eden 区进行 GC，它在 Eden 空间耗尽时触发，基于分代回收思想和复制算法，每次 Young GC 都会选定所有新生代的 Region。

同时计算下次 Young GC 所需的 Eden 区和 Survivor 区的空间，动态调整新生代所占 Region 个数来控制 Young GC 开销

**2、Mixed GC发生时机**

其实是由一些参数控制着的,另外也控制着哪些老年代Region会被选入CSet
- `G1HeapWastePercent`：global concurrent marking结束之后，可以知道old gen regions中有多少空间要被回收，在每次YGC之后和再次发生Mixed GC之前，会检查垃圾占比是否达到此参数，只有达到了，下次才会发生Mixed GC；
- `G1MixedGCLiveThresholdPercent`：old generation region中的存活对象的占比，只有在此参数之下，才会被选入CSet；
- `G1MixedGCCountTarget`：一次global concurrent marking之后，最多执行Mixed GC的次数；
- `G1OldCSetRegionThresholdPercent`：一次Mixed GC中能被选入CSet的最多old generation region数量
- `-XX:G1HeapRegionSize=n`：设置Region大小，并非最终值
- `-XX:MaxGCPauseMillis`：设置G1收集过程目标时间，默认值200ms，不是硬性条件
- `-XX:G1NewSizePercent`：新生代最小值，默认值5%
- `-XX:G1MaxNewSizePercent`：新生代最大值，默认值60%
- `-XX:ParallelGCThreads`：STW期间，并行GC线程数
- `-XX:ConcGCThreads=n`：并发标记阶段，并行执行的线程数
- `-XX:InitiatingHeapOccupancyPercent`：设置触发标记周期的 Java 堆占用率阈值。默认值是45%

#### 7.3.5、G1 GC存在问题

**Full GC 问题**

G1 的正常处理流程中没有 Full GC，只有在垃圾回收处理不过来（或者主动触发）时才会出现（当内存满了的时候就会进行Full GC），且JDK10之前的Full GC，为单线程执行的 Serial old gc，会导致非常长的 STW，是调优的重点，需要尽量避免 Full GC；

常见原因如下：
- 程序主动执行 System.gc()；
- 全局并发标记期间老年代空间被填满（并发模式失败）；
- Mixed GC 期间老年代空间被填满（晋升失败）；
- Young GC 时 Survivor 空间和老年代没有足够空间容纳存活对象；

常见的解决是：
- 增大 `-XX:ConcGCThreads=n` 选项增加并发标记线程的数量，或者 STW 期间并行线程的数量：-XX:ParallelGCThreads=n。
- 减小 `-XX:InitiatingHeapOccupancyPercent` 提前启动标记周期。
- 增大预留内存 `-XX:G1ReservePercent=n`，默认值是 10，代表使用 10% 的堆内存为预留内存，当 Survivor 区域没有足够空间容纳新晋升对象时会尝试使用预留内存
- 提高CPU性能，加快GC回收速度，而对象增加速度赶不上回收速度，则Full GC可以避免；
- 降低进行Mixed GC触发的阈值，让Mixed GC提早发生（默认45%）

### 7.4、最佳实践

- 年轻代大小：避免使用-Xmn、-XX:NewRatio 等显式设置Young区大小，会覆盖暂停时间目标；
- 暂停时间目标：暂停时间不要太严苛，其吞吐量目标是90%的应用程序时间个和10%的垃圾回收时间，太严苛会直接影响到吞吐量
- 关于MixGC调优：一些参数
	```
	-XX:InitiatingHeapOccupancyPercent=percent
	-XX:G1MixedGCLiveThresholdPercent
	-XX:G1HeapWastePercent
	-XX:G1MixedGCCountTarget
	-XX:G1OldCSetRegionThresholdPercent
	```

### 7.5、是否需要切换到G1

如果存在下列问题，可以切换到G1垃圾收集器
- 50%以上的堆被存活对象占用；
- 对象分配和晋升的速度变化非常大；
- 垃圾回收时间特别长，超过了1秒；
- 内存空间占用比较大，比如超过6G；

### 7.6、G1与CMS

[G1与CMS垃圾收集器区别](https://blog.chriscs.com/2017/06/20/g1-vs-cms/)

**区别：**
- 使用范围不一样：CMS是针对老年代收集器，可以配合新生代的Serial和ParNew收集器一起使用；G1是针对老年代和新生代的，不需要结合其他收集器使用；
- STW时间：CMS收集器以最小的停顿时间为目标的收集器；G1收集器可预测垃圾回收的停顿时间（建立可预测的停顿时间模型）；
- 垃圾碎片：CMS收集器是使用“标记-清除”算法进行的垃圾回收，容易产生内存碎片；G1收集器使用的是“标记-整理”算法，进行了空间整合，降低了内存空间碎片；

**适用场景：**
- CMS：与Parallel Scavenge 收集器搭配使用；注重吞吐量。jdk7、jdk8 默认使用该收集器作为老年代收集器，使用 -XX:+UseParallelOldGC 来指定使用 Parallel Old 收集器；
- G1：要求尽可能可控GC停顿时间；内存占用较大的应用。可以用 `-XX:+UseG1GC` 使用 G1 收集器，jdk9 默认使用 G1 收集器；

## 8、ZGC

* [ZGC垃圾收集器](https://www.jianshu.com/p/6f89fd5842bf)
* [ZGC垃圾收集器](https://club.perfma.com/article/679812)
* [ZGC调优](https://tech.meituan.com/2020/08/06/new-zgc-practice-in-meituan.html)

### 8.1、概述

低延迟的GC，ZGC几乎在所有地方并发执行的，除了初始标记的是STW的。所以停顿时间几乎就耗费在初始标记上，这部分的实际是非常少的

ZGC主要新增了两项技术，一个是着色指针`Colored Pointer`，另一个是读屏障`Load Barrier`；

JDK11 中推出的一款低延迟垃圾回收器，适用于大内存低延迟服务的内存管理和回收，SPECjbb 2015 基准测试，在 128G 的大堆下，最大停顿时间才 1.68 ms，停顿时间远胜于 G1 和 CMS

ZGC 是一个并发、基于区域（region）、增量式压缩的收集器。Stop-The-World 阶段只会在根对象扫描（root scanning）阶段发生，这样的话 GC 暂停时间并不会随着堆和存活对象的数量而增加。

其设计目标：
- 停顿时间不超过10ms（STW减少了）
- 支持8MB~4TB的级别的堆

**启用参数：** `-XX:+UnlockExperimentalVMOptions -XX:+UseZGC`；

### 8.2、原理

CMS新生代的Young GC、G1和ZGC都基于标记-复制算法，不过ZGC对该算法做了重大改进：ZGC在标记、转移和重定位阶段几乎都是并发的，这是ZGC实现停顿时间小于10ms目标的最关键原因；

ZGC只有三个STW阶段：`初始标记`、`再标记`、`初始转移`。其中初始标记和初始转移分别都只需要扫描所有GC Roots，其处理时间和GC Roots的数量成正比，一般情况耗时非常短；再标记阶段STW时间很短，最多1ms，超过1ms则再次进入并发标记阶段。即，ZGC几乎所有暂停都只依赖于GC Roots集合大小，停顿时间不会随着堆的大小或者活跃对象的大小而增加。与ZGC对比，G1的转移阶段完全STW的，且停顿时间随存活对象的大小增加而增加

### 8.3、关键技术

ZGC通过`着色指针`和`读屏障技`术，解决了转移过程中准确访问对象的问题，实现了并发转移。

大致原理描述如下：并发转移中“并发”意味着GC线程在转移对象的过程中，应用线程也在不停地访问对象。假设对象发生转移，但对象地址未及时更新，那么应用线程可能访问到旧地址，从而造成错误。而在ZGC中，应用线程访问对象将触发“读屏障”，如果发现对象被移动了，那么“读屏障”会把读出来的指针更新到对象的新地址上，这样应用线程始终访问的都是对象的新地址。那么，JVM是如何判断对象被移动过呢？就是利用对象引用的地址，即着色指针。

#### 8.3.1、着色指针

着色指针是一种将信息存储在指针中的技术；

ZGC仅支持64位系统

#### 8.3.2、读屏障

读屏障是JVM向应用代码插入一小段代码的技术。当应用线程从堆中读取对象引用时，就会执行这段代码。需要注意的是，仅“从堆中读取对象引用”才会触发这段代码；

ZGC中读屏障的代码作用：在对象标记和转移过程中，用于确定对象的引用地址是否满足条件，并作出相应动作。

### 8.4、ZGC触发时机

相比于CMS和G1的GC触发机制，ZGC的GC触发机制有很大不同。ZGC的核心特点是并发，GC过程中一直有新的对象产生。如何保证在GC完成之前，新产生的对象不会将堆占满，是ZGC参数调优的第一大目标

ZGC有多种GC触发机制，总结如下：
- 阻塞内存分配请求触发：当垃圾来不及回收，垃圾将堆占满时，会导致部分线程阻塞。我们应当避免出现这种触发方式。日志中关键字是“Allocation Stall”。
- 基于分配速率的自适应算法：最主要的GC触发方式，其算法原理可简单描述为”ZGC根据近期的对象分配速率以及GC时间，计算出当内存占用达到什么阈值时触发下一次GC”。通过ZAllocationSpikeTolerance参数控制阈值大小，该参数默认2，数值越大，越早的触发GC。我们通过调整此参数解决了一些问题。日志中关键字是“Allocation Rate”。
- 基于固定时间间隔：通过ZCollectionInterval控制，适合应对突增流量场景。流量平稳变化时，自适应算法可能在堆使用率达到95%以上才触发GC。流量突增时，自适应算法触发的时机可能会过晚，导致部分线程阻塞。我们通过调整此参数解决流量突增场景的问题，比如定时活动、秒杀等场景。日志中关键字是“Timer”。
- 主动触发规则：类似于固定间隔规则，但时间间隔不固定，是ZGC自行算出来的时机，我们的服务因为已经加了基于固定时间间隔的触发机制，所以通过-ZProactive参数将该功能关闭，以免GC频繁，影响服务可用性。 日志中关键字是“Proactive”。
- 预热规则：服务刚启动时出现，一般不需要关注。日志中关键字是“Warmup”。
- 外部触发：代码中显式调用System.gc()触发。 日志中关键字是“System.gc()”。
- 元数据分配触发：元数据区不足时导致，一般不需要关注。 日志中关键字是“Metadata GC Threshold”。

### 8.5、内存布局

ZGC也采用基于Region的堆内存布局，但与它们不同的是，ZGC的Region（在一些官方资料中将它称为Page或者ZPage）具有动态性，可以动态创建和销毁，以及动态的区域容量大小。region的容量：
- 小型Region（Small Region）：容量固定为2MB，用于放置小于256KB的小对象。
- 中型Region（Medium Region）：容量固定为32MB，用于放置大于等于256KB但小于4MB的对象。
- 大型Region（Large Region）：容量不固定，可以动态变化，但必须为2MB的整数倍，用于放置4MB或以上的大对象。每个大型Region中只会存放一个大对象，这也预示着虽然名字叫作“大型Region”，但它的实际容量完全有可能小于中型Region，最小容量可低至4MB。大型Region在ZGC的实现中是不会被重分配（重分配是ZGC的一种处理动作，用于复制对象的收集器阶段）的，因为复制一个大对象的代价非常高昂

### 8.6、zgc过程

**工作步骤：**
- 并发标记（Concurrent Mark）：与G1、Shenandoah一样，并发标记是遍历对象图做可达性分析的阶段，前后也要经过类似于G1、Shenandoah的初始标记、最终标记（尽管ZGC中的名字不叫这些）的短暂停顿，而且这些停顿阶段所做的事情在目标上也是相类似的。与G1、Shenandoah不同的是，ZGC的标记是在指针上而不是在对象上进行的，标记阶段会更新染色指针中的Marked 0、Marked 1标志位。
- 并发预备重分配（Concurrent Prepare for Relocate）：这个阶段需要根据特定的查询条件统计得出本次收集过程要清理哪些Region，将这些Region组成重分配集（Relocation Set）。重分配集与G1收集器的回收集（Collection Set）还是有区别的，ZGC划分Region的目的并非为了像G1那样做收益优先的增量回收。相反，ZGC每次回收都会扫描所有的Region，用范围更大的扫描成本换取省去G1中记忆集的维护成本。因此，ZGC的重分配集只是决定了里面的存活对象会被重新复制到其他的Region中，里面的Region会被释放，而并不能说回收行为就只是针对这个集合里面的Region进行，因为标记过程是针对全堆的。此外，在JDK 12的ZGC中开始支持的类卸载以及弱引用的处理，也是在这个阶段中完成的。
- 并发重分配（Concurrent Relocate）：重分配是ZGC执行过程中的核心阶段，这个过程要把重分配集中的存活对象复制到新的Region上，并为重分配集中的每个Region维护一个转发表（Forward Table），记录从旧对象到新对象的转向关系。
- 并发重映射（Concurrent Remap）：重映射所做的就是修正整个堆中指向重分配集中旧对象的所有引用，这一点从目标角度看是与Shenandoah并发引用更新阶段一样的，但是ZGC的并发重映射并不是一个必须要“迫切”去完成的任务，因为前面说过，即使是旧引用，它也是可以自愈的，最多只是第一次使用时多一次转发和修正操作；

## 9、垃圾收集器比较

- 收集器比较

	|收集器|运行机制|区域|算法|目标|适用场景|
	|-----|--------|----|----|----|------|
	|Serial|串行|新生代|复制算法|响应速度优先|单CPU环境下的Client模式|
	|Serial Old|串行|老年代|标记-整理|响应速度优先|单CPU环境下的client模式、CMS的后备预案|
	|ParNew|并行|新生代|复制算法|响应速度优先|多CPU环境时在Server模式下与CMS配合|
	|Parallel Scavenge|并行|新生代|复制算法|吞吐量优先|在后台运算而不需要太多的交互的任务|
	|Parallel Old|并行|老年代|标记-整理|吞吐量优先|在后台运算而不需要太多的交互的任务|
	|CMS|并发|老年代|标记-清除|响应速度优先|集中在互联网站或B/S系统服务端上的java应用|
	|G1|并发|both|标记-整理+复制|响应速度优先|面向服务端应用，将来替换CMS|

- 收集器如何搭配

	![image](image/GC搭配.png)

- 新生代收集器还是老年代收集器：
	- 新生代收集器：Serial、ParNew、Parallel Scavenge；
	- 老年代收集器：Serial Old、Parallel Old、CMS；
	- 整堆收集器：G1
- 吞吐量优先、停顿时间优先：
	- 吞吐量优先：Parallel Scavenge收集器、Parallel Old 收集器。
	- 停顿时间优先：CMS（Concurrent Mark-Sweep）收集器。
- 吞吐量与停顿时间适用场景：
	- 停顿时间优先：交互多，对响应速度要求高
	- 吞吐量优先：交互少，计算多，适合在后台运算的场景。
- 串行并行并发：
	- 串行：Serial、Serial Old
	- 并行：ParNew、Parallel Scavenge、Parallel Old
	- 并发：CMS、G1
- 算法
	- 复制算法：Serial、ParNew、Parallel Scavenge、G1
	- 标记-清除：CMS
	- 标记-整理：Serial Old、Parallel Old、G1

## 10、如何选择垃圾收集器

- 优先调整堆的大小让服务器自己来选择；
- 如果内存小于100M，使用串行收集器；
- 如果是单核，并且没有停顿时间的要求，串行或者JVM自己选择；
- 如果允许停顿时间超过1秒，选择并行或者JVM自己选择；
- 如果响应时间最重要，并且不能超过1秒，使用并发收集器

具体实践：
- 如果你的堆大小不是很大（比如 100MB），选择串行收集器一般是效率最高的。参数：-XX:+UseSerialGC；
- 如果你的应用运行在单核的机器上，或者你的虚拟机核数只有 1C，选择串行收集器依然是合适的，这时候启用一些并行收集器没有任何收益。参数：`-XX:+UseSerialGC`；
- 如果你的应用是“吞吐量”优先的，并且对较长时间的停顿没有什么特别的要求。选择并行收集器是比较好的。参数：`-XX:+UseParallelGC`；
- 如果你的应用对响应时间要求较高，想要较少的停顿。甚至 1 秒的停顿都会引起大量的请求失败，那么选择 G1、ZGC、CMS 都是合理的；

## 11、新版本JDK增加的垃圾收集器

### 11.1、Shenandoah

有RedHat贡献给OpenJDK，Oracle JDK无法使用；是低延迟垃圾收集器，目前还是处于实验性的，

**和G1对比，相同点：**
- 基于Region的内存布局；
- 有用于存放大对象的Humongous Region；
- 回收策略也同样是优先处理回收价值最大的Region；

**和G1对比，不同点：**
- 并发的整理算法
- Shenandoah默认是不使用分代收集的
- 解决跨region引用的机制不同，G1主要基于Remembered Set、CardTable，而Shenandoah是基于连接矩阵（Connection Matrix）去实现的。

**启用参数：**`-XX:+UnlockExperimentalVMOptions  -XX:+UseShenandoahGC`

**适用场景：**低延迟、响应快的业务场景

**工作步骤：**

- 初始标记（Initial Marking）：与G1一样，首先标记与GC Roots直接关联的对象，存在Stop The World

- 并发标记（Concurrent Marking）：与G1一样，标记出全部可达的对象，该阶段并发执行，无Stop The World

- 最终标记（Final Marking）：统计出回收价值最高的Region、构建回收集（Collection Set）。存在Stop The World

- 并发清理（Concurrent Cleanup）：用于清理那些整个区域内连一个存活对象都没有找到的Region（这类Region被称为Immediate Garbage Region）

- 并发回收（Concurrent Evacuation）：并发回收阶段是Shenandoah与之前HotSpot中其他收集器的核心差异。在这个阶段，Shenandoah要把回收集里面的存活对象先复制一份到其他未被使用的Region之中。**复制对象这件事情如果将用户线程冻结起来再做那是相当简单的，但如果两者必须要同时并发进行的话，就变得复杂起来了。**其困难点是在移动对象的同时，用户线程仍然可能不停对被移动的对象进行读写访问，移动对象是一次性的行为，但移动之后整个内存中所有指向该对象的引用都还是旧对象的地址，这是很难一瞬间全部改变过来的。对于并发回收阶段遇到的这些困难，Shenandoah将会通过读屏障和被称为“Brooks Pointers”的转发指针来解决。并发回收阶段运行的时间长短取决于回收集的大小。

- 初始引用更新（Initial Update Reference）：并发回收阶段复制对象结束后，还需要把堆中所有指向旧对象的引用修正到复制后的新地址，这个操作称为引用更新。引用更新的初始化阶段实际上并未做什么具体的处理，设立这个阶段只是为了建立一个线程集合点，确保所有并发回收阶段中进行的收集器线程都已完成分配给它们的对象移动任务而已。初始引用更新时间很短，会产生一个非常短暂的停顿。

- 并发引用更新（Concurrent Update Reference）：真正开始进行引用更新操作，这个阶段是与用户线程一起并发的，时间长短取决于内存中涉及的引用数量的多少。主要是按照内存物理地址的顺序，线性地搜索出引用类型，把旧值改为新值即可；

- 最终引用更新（Final Update Reference）：解决了堆中的引用更新后，还要修正存在于GC Roots中的引用。这个阶段是Shenandoah的最后一次停顿，停顿时间只与GC Roots的数量相关；

- 并发清理（Concurrent Cleanup）：经过并发回收和引用更新之后，整个回收集中所有的Region已再无存活对象，这些Region都变成Immediate Garbage Regions了，最后再调用一次并发清理过程来回收这些Region的内存空间，供以后新对象分配使用；

### 11.2、Epsilon

Epsilon（A No-Op Garbage Collector）垃圾回收器控制内存分配，但是不执行任何垃圾回收工作。一旦java的堆被耗尽，jvm就直接关闭。设计的目的是提供一个完全消极的GC实现，分配有限的内存分配，最大限度降低消费内存占用量和内存吞吐时的延迟时间。一个好的实现是隔离代码变化，不影响其他GC，最小限度的改变其他的JVM代码；

**启用参数：** `-XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC`

**适用场景：**
- Performance testing,什么都不执行的GC非常适合用于差异性分析。no-op GC可以用于过滤掉GC诱发的新能损耗，比如GC线程的调度，GC屏障的消耗，GC周期的不合适触发，内存位置变化等。此外有些延迟者不是由于GC引起的，比如scheduling hiccups, compiler transition hiccups，所以去除GC引发的延迟有助于统计这些延迟。
- Memory pressure testing, 在测试java代码时，确定分配内存的阈值有助于设置内存压力常量值。这时no-op就很有用，它可以简单地接受一个分配的内存分配上限，当内存超限时就失败。例如：测试需要分配小于1G的内存，就使用-Xmx1g参数来配置no-op GC，然后当内存耗尽的时候就直接crash。
- VM interface testing, 以VM开发视角，有一个简单的GC实现，有助于理解VM-GC的最小接口实现。它也用于证明VM-GC接口的健全性。
- Extremely short lived jobs, 一个短声明周期的工作可能会依赖快速退出来释放资源，这个时候接收GC周期来清理heap其实是在浪费时间，因为heap会在退出时清理。并且GC周期可能会占用一会时间，因为它依赖heap上的数据量。
- Last-drop latency improvements, 对那些极端延迟敏感的应用，开发者十分清楚内存占用，或者是几乎没有垃圾回收的应用，此时耗时较长的GC周期将会是一件坏事；
- Last-drop throughput improvements, 即便对那些无需内存分配的工作，选择一个GC意味着选择了一系列的GC屏障，所有的OpenJDK GC都是分代的，所以他们至少会有一个写屏障。避免这些屏障可以带来一点点的吞吐量提升；

## 12、垃圾收集器参数

|收集器 |	参数及默认值 |	备注|
|------| --------------|--------|
|Serial |	-XX:+UseSerialGC |	虚拟机在Client模式下的默认值，开启后，使用 Serial + Serial Old 的组合|
|ParNew |	-XX:+UseParNewGC |	开启后，使用ParNew + Serial Old的组合|
|      |-XX:ParallelGCThreads=n| 设置垃圾收集器在并行阶段使用的垃圾收集线程数，当逻辑处理器数量小于8时，n的值与逻辑处理器数量相同；如果逻辑处理器数量大于8个，则n的值大约为逻辑处理器数量的5/8，大多数情况下是这样，除了较大的SPARC系统，其中n的值约为逻辑处理器的5/16。|
|Parallel Scavenge	|-XX:+UseParallelGC	|虚拟机在Server模式下的默认值，开启后，使用 Parallel Scavenge + Serial Old的组合|
| |-XX:MaxGCPauseMillis=n|	收集器尽可能保证单次内存回收停顿的时间不超过这个值，但是并不保证不超过该值|
||-XX:GCTimeRatio=n|	设置吞吐量的大小，取值范围0-100，假设 GCTimeRatio 的值为 n，那么系统将花费不超过 1/(1+n) 的时间用于垃圾收集|
||-XX:+UseAdaptiveSizePolicy|开启后，无需人工指定新生代的大小（-Xmn）、 Eden和Survisor的比例（-XX:SurvivorRatio）以及晋升老年代对象的年龄（-XX:PretenureSizeThreshold）等参数，收集器会根据当前系统的运行情况自动调整|
|Serial Old	|无	|Serial Old是Serial的老年代版本，主要用于 Client 模式下的老生代收集，同时也是 CMS 在发生 Concurrent Mode Failure时的后备方案|
|Parallel Old|	-XX:+UseParallelOldGC|	开启后，使用Parallel Scavenge + Parallel Old的组合。Parallel Old是Parallel Scavenge的老年代版本，在注重吞吐量和 CPU 资源敏感的场合，可以优先考虑这个组合|
|CMS|-XX:+UseConcMarkSweepGC|开启后，使用ParNew + CMS的组合；Serial Old收集器将作为CMS收集器出现 Concurrent Mode Failure 失败后的后备收集器使用|
||-XX:CMSInitiatingOccupancyFraction=68	|CMS 收集器在老年代空间被使用多少后触发垃圾收集，默认68%|
||-XX:+UseCMSCompactAtFullCollection	|在完成垃圾收集后是否要进行一次内存碎片整理，默认开启|
||-XX:CMSFullGCsBeforeCompaction=0|	在进行若干次Full GC后就进行一次内存碎片整理，默认0|
||-XX:+UseCMSInitiatingOccupancyOnly|	允许使用占用值作为启动CMS收集器的唯一标准，一般和CMSFullGCsBeforeCompaction配合使用。如果开启，那么当CMSFullGCsBeforeCompaction达到阈值就开始GC，如果关闭，那么JVM仅在第一次使用CMSFullGCsBeforeCompaction的值，后续则自动调整，默认关闭。|
||-XX:+CMSParallelRemarkEnabled	|重新标记阶段并行执行，使用此参数可降低标记停顿，默认打开（仅适用于ParNewGC）|
||-XX:+CMSScavengeBeforeRemark|	开启或关闭在CMS重新标记阶段之前的清除（YGC）尝试。新生代里一部分对象会作为GC Roots，让CMS在重新标记之前，做一次YGC，而YGC能够回收掉新生代里大多数对象，这样就可以减少GC Roots的开销。因此，打开此开关，可在一定程度上降低CMS重新标记阶段的扫描时间，当然，开启此开关后，YGC也会消耗一些时间。PS. 开启此开关并不保证在标记阶段前一定会进行清除操作，生产环境建议开启，默认关闭。|
|CMS-Precleaning|	-XX:+CMSPrecleaningEnabled|	是否启用并发预清理，默认开启|
|CMS-AbortablePreclean|	-XX:CMSScheduleRemark EdenSizeThreshold=2M	|如果伊甸园的内存使用超过该值，才可能进入“并发可中止的预清理”这个阶段|
|CMS-AbortablePreclean	|-XX:CMSMaxAbortablePrecleanLoops=0	|“并发可终止的预清理阶段”的循环次数，默认0，表示不做限制|
|CMS-AbortablePreclean	|-XX:+CMSMaxAbortablePrecleanTime=5000|	“并发可终止的预清理”阶段持续的最大时间|
||-XX:+CMSClassUnloadingEnabled|	使用CMS时，是否启用类卸载，默认开启|
||-XX:+ExplicitGCInvokesConcurrent	|显示调用System.gc()会触发Full GC，会有Stop The World，开启此参数后，可让System.gc()触发的垃圾回收变成一次普通的CMS GC。|
|G1|-XX:+UseG1GC|	使用G1收集器|
||-XX:G1HeapRegionSize=n|	设置每个region的大小，该值为2的幂，范围为1MB到32MB，如不指定G1会根据堆的大小自动决定|
||-XX:MaxGCPauseMillis=200|	设置最大停顿时间，默认值为200毫秒。|
||-XX:G1NewSizePercent=5|	设置年轻代占整个堆的最小百分比，默认值是5，这是个实验参数。需用-XX:+UnlockExperimentalVMOptions解锁试验参数后，才能使用该参数。|
||-XX:G1MaxNewSizePercent=60|	设置年轻代占整个堆的最大百分比，默认值是60，这是个实验参数。需用-XX:+UnlockExperimentalVMOptions解锁试验参数后，才能使用该参数。|
||-XX:ParallelGCThreads=n|	设置垃圾收集器在并行阶段使用的垃圾收集线程数，当逻辑处理器数量小于8时，n的值与逻辑处理器数量相同；如果逻辑处理器数量大于8个，则n的值大约为逻辑处理器数量的5/8，大多数情况下是这样，除了较大的SPARC系统，其中n的值约为逻辑处理器的5/16。|
||-XX:ConcGCThreads=n	|设置垃圾收集器并发阶段使用的线程数量，设置n大约为ParallelGCThreads的1/4。|
||-XX:InitiatingHeapOccupancyPercent=45	|老年代大小达到该阈值，就触发Mixed GC，默认值为45。|
||-XX:G1MixedGCLiveThresholdPercent=85|	Region中的对象，活跃度低于该阈值，才可能被包含在Mixed GC收集周期中，默认值为85，这是个实验参数。需用-XX:+UnlockExperimentalVMOptions解锁试验参数后，才能使用该参数。|
||-XX:G1HeapWastePercent=5|	设置浪费的堆内存百分比，当可回收百分比小于浪费百分比时，JVM就不会启动Mixed GC，从而避免昂贵的GC开销。此参数相当于用来设置允许垃圾对象占用内存的最大百分比。|
||-XX:G1MixedGCCountTarget=8|	设置在标记周期完成之后，最多执行多少次Mixed GC，默认值为8。|
||-XX:G1OldCSetRegionThresholdPercent=10|	设置在一次Mixed GC中被收集的老年代的比例上限，默认值是Java堆的10%，这是个实验参数。需用-XX:+UnlockExperimentalVMOptions解锁试验参数后，才能使用该参数。|
||-XX:G1ReservePercent=10	|设置预留空闲内存百分比，虚拟机会保证Java堆有这么多空间可用，从而防止对象晋升时无空间可用而失败，默认值为Java堆的10％。|
||-XX:-G1PrintHeapRegions|	输出Region被分配和回收的信息，默认false|
||-XX:-G1PrintRegionLivenessInfo	|在清理阶段的并发标记环节，输出堆中的所有Regions的活跃度信息，默认false|
|Shenandoah|	-XX:+UseShenandoahGC	|使用UseShenandoahGC，这是个实验参数，需用-XX:+UnlockExperimentalVMOptions解锁试验参数后，才能使用该参数；另外该参数只能在Open JDK中使用，Oracle JDK无法使用|
|ZGC|	-XX:+UseZGC	|使用ZGC，这是个实验参数，需用-XX:+UnlockExperimentalVMOptions解锁试验参数后，才能使用该参数；|
|Epsilon|	-XX:+UseEpsilonGC|	使用EpsilonGC，这是个实验参数，需用-XX:+UnlockExperimentalVMOptions解锁试验参数后，才能使用该参数；|

# 五、GC 执行机制

Minor GC和Full GC垃圾收集过程：
- （1）JVM 将堆空间分成新生代（young）和老年代（old）两个区域，创建对象的时候，只在新生代创建，当新生代空间不足的时候，只对新生代进行垃圾回收，这样需要处理的内存空间就比较小，垃圾回收速度就比较快。
- （2）新生代又分为 Eden 区、From 区和 To 区三个区域，每次垃圾回收都是扫描 Eden 区和 From 区，将存活对象复制到 To 区，然后交换 From 区和 To 区的名称引用，下次垃圾回收的时候继续将存活对象从 From 区复制到 To 区。当一个对象经过几次新生代垃圾回收，也就是几次从 From 区复制到 To 区以后，依然存活，那么这个对象就会被复制到老年代区域；类似步骤2的过程发生很多次，直到对象年龄计数达到阈值，此时发生所谓的晋升，超过阈值的对象会被晋升到老年代。这个阈值可以通过参数`-XX:MaxTenuringThreshold`指定；
- （3）当老年代空间已满，也就是无法将新生代中多次复制后依然存活的对象复制进去的时候，就会对新生代和老年代的内存空间进行一次全量垃圾回收，即 Full GC。所以根据应用程序的对象存活时间，合理设置老年代和新生代的空间比例对 JVM 垃圾回收的性能有很大影响，JVM 设置老年代新生代比例的参数是 -XX:NewRatio

通常把老年代的GC成为major gc，对整个堆进行的清理叫做Full GC

## 1、YGC

对新生代进行GC

### 1.1、什么是YGC

YGC是JVM GC当前最为频繁的一种GC，一个高并发的服务在运行期间，会进行大量的YGC，发生YGC时，会进行STW(Stop The World)，一般时间都很短，除非碰到YGC时，存在大量的存活对象需要进行拷贝，主要是针对新生代对 Eden 区域进行GC，清除非存活对象。并且把尚且存活的对象移动到 Survivor 区，然后整理 Survivor 的两个区.这种方式的 GC 是对年轻代的 Eden 进行，不会影响到年老代。因为大部分对象都是从 Eden 区开始的，同时 Eden 区不会分配的很大.所以 Eden 区的 GC 会频繁进行。因而，一般在这里需要使用速度快、效率高的算法，使Eden去能尽快空闲出来；

### 1.2、触发条件

一般情况下，当新对象生成，并且在 Eden 申请空间失败时，就会触发 Minor GC。即当Eden区满时，触发Minor GC

### 1.3、YGC过程

主要分为两个步骤
- 查找GC Roots，拷贝所引用的对象到 to 区;
- 递归遍历步骤1中对象，并拷贝其所引用的对象到 to 区，当然可能会存在自然晋升，或者因为 to 区空间不足引起的提前晋升的情况；

上述整个过程都是需要暂停业务线程的（STW），不过ParNew等新生代回收器可以多线程并行执行，提高处理效率

### 1.4、YGC细节

- 如果触发的YGC顺利执行完，期间没有发生任何问题，垃圾回收完成后，正常的分配内存；
- 如果YGC刚要开始执行，却不幸的发生了JNI的GC locker，本次的YGC会被放弃，如果是给对象分配内存，会在老年代中直接分配内存，如果是TLAB的话，就要等JNI结束了；
- 如果没有JNI的干扰，在YGC过程中，对象年纪达到阈值，正常晋升，或to空间不足，对象提前晋升，但老年代又没这么多空间容纳晋升上来的对象，这时会发生“promotion failed”，而且eden和from区的空间没办法清空， 把from区和to区进行swap，所以当前eden和from的使用率都是接近100%的，如果当前是给对象（非TLAB）申请内存，会继续触发一次老年代的回收动作；
```java
/**
 * -Xmx20m -Xms20m -Xmn14m -XX:+UseParNewGC  -XX:+UseConcMarkSweepGC
 *-XX:+UseCMSInitiatingOccupancyOnly  -XX:CMSInitiatingOccupancyFraction=75
 *-XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintHeapAtGC
 */
public class JVM {
    private static final int _1MB = 1024 * 1024;
    private static final int _1K = 1024;
    public static void main(String[] args) throws Exception {
        byte[][] arr = new byte[10000][];
        for (int i = 0; i< 1200; i++) {
            arr[i] = new byte[10* _1K];
        }
        System.in.read();
    }
}
```

### 1.5、YGC耗时久原因

- （1）对存活对象标注时间过长：比如重载了Object类的Finalize方法，导致标注Final Reference耗时过长；或者String.intern方法使用不当，导致YGC扫描StringTable时间过长；
- （2）长周期对象积累过多：比如本地缓存使用不当，积累了太多存活对象；或者锁竞争严重导致线程阻塞，局部变量的生命周期变长；
- （3）存活对象copy to S区，晋升到Old区；
- （4）等待各线程到达安全点时间：GC发生时，程序是会STW的(Serial, ParNew, Parallel Scanvange, ParallelOld, Serial Old全程都会STW，CMS等在初始标记重新标记阶段也会STW), JVM这时候只运行GC线程，不运行用户线程

对于第一类问题可以通过参数显示GC处理Reference的耗时`-XX:+PrintReferenceGC`

## 2、Mixed GC/Old GC

Old GC：只清理老年代空间的 GC 事件，只有 CMS 的并发收集是这个模式；

Mixed GC：清理整个新生代以及部分老年代的 GC，只有 G1 有这个模式；

## 3、Full-GC

对整个堆进行整理，包括 新生代，老年代和持久代；堆空间使用到达80%(可调整)的时候会触发fgc；Full GC 因为需要对整个对进行回收，所以比Scavenge GC要慢，因此应该尽可能减少 Full GC 的次数。在对JVM调优的过程中，很大一部分工作就是对于 FullGC 的调节。

有如下原因可能导致 Full GC：
- 老年代被写满
- 方法区被写满（元空间不足）
- System.gc()被显示调用;
- 上一次GC之后Heap的各域分配策略动态变化；MinorGC晋升到老年代的平均大小大于老年代的剩余空间；
- CMS GC 时出现`promotion failed`和`concurrent mode failure`<br>
	- promotion failed：是在进行 Minor GC时，survivor空间放不下、对象只能放入老生代，而此时老生代也放不下造成的；
	- concurrent mode failure：在执行 CMS GC 的过程中同时有对象要放入老生代，而此时老生代空间不足造成的
- 使用RMI来进行RPC或管理的JDK应用，每小时执行1次FullGC

FullGC次数过多的原因：
- 代码中一次获取了大量的对象，导致内存溢出，此时可以通过eclipse的mat工具查看内存中有哪些对象比较多；
- 内存占用不高，但是Full GC次数还是比较多，此时可能是显示的 System.gc()调用导致GC次数过多，这可以通过添加 -XX:+DisableExplicitGC来禁用JVM对显示GC的响应

## 4、内存回收与分配策略

### 4.1、对象优先分配在 Eden 区域

大多数情况下，对象在新生代的Eden区中分配，如果Eden区没有足够的空间时，虚拟机将发起一次 MinorGC

- 查看代码的具体GC日志VM参数：
```
-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
```
- 打印日志：
```JAVA
private static final int _1MB = 1024 * 1024;
public static void testAllocation(){
	byte[] a1， a2， a3， a4;
	a1 = new byte[2 * _1MB];
	a2 = new byte[2 * _1MB];
	a3 = new byte[2 * _1MB];
	a4 = new byte[4 * _1MB];
}
```
```
[GC (Allocation Failure) [PSYoungGen: 14925K->1011K(18432K)] 14925K->13308K(38912K)， 0.0042158 secs] [Times: user=0.00 sys=0.00， real=0.00 secs] 
[Full GC (Ergonomics) [PSYoungGen: 1011K->0K(18432K)] [ParOldGen: 12296K->13203K(20480K)] 13308K->13203K(38912K)， [Metaspace: 3395K->3395K(1056768K)]， 0.0056106 secs] [Times: user=0.00 sys=0.00， real=0.01 secs] 
[GC (Allocation Failure) [PSYoungGen: 6307K->96K(18432K)] 19511K->19443K(38912K)， 0.0027672 secs] [Times: user=0.06 sys=0.00， real=0.00 secs] 
[Full GC (Ergonomics) [PSYoungGen: 96K->0K(18432K)] [ParOldGen: 19347K->19250K(20480K)] 19443K->19250K(38912K)， [Metaspace: 3419K->3419K(1056768K)]， 0.0068909 secs] [Times: user=0.00 sys=0.00， real=0.01 secs] 
Heap
	PSYoungGen      total 18432K， used 12779K [0x00000000fec00000， 0x0000000100000000， 0x0000000100000000)
	eden space 16384K， 78% used [0x00000000fec00000，0x00000000ff87afb0，0x00000000ffc00000)
	from space 2048K， 0% used [0x00000000ffe00000，0x00000000ffe00000，0x0000000100000000)
	to   space 2048K， 0% used [0x00000000ffc00000，0x00000000ffc00000，0x00000000ffe00000)
	ParOldGen       total 20480K， used 19250K [0x00000000fd800000， 0x00000000fec00000， 0x00000000fec00000)
	object space 20480K， 93% used [0x00000000fd800000，0x00000000feaccb48，0x00000000fec00000)
	Metaspace       used 3425K， capacity 4494K， committed 4864K， reserved 1056768K
	class space    used 378K， capacity 386K， committed 512K， reserved 1048576K {}
```
- ①、GC 日志开头"[GC"和 "[FULL GC"说明了这次垃圾收集停顿的类型，不是用来区分新生代GC和老年代GC的如果有Full 说明这次GC发生了的"Stop-the-world"
- ②、[PSYoungGen: 14925K->1011K(18432K)] 14925K->13308K(38912K)， 0.0042158 secs <br>
	[PSYoungGen， [ParOldGen， [Metaspace 表示GC发生的区域<br>
	PSYoungGen ==> 表示使用的是 Parallel Scavenge 收集器来回收新生代的<br>
	ParOldGen ==> Parallel Old 收集器<br>
	14925K->1011K(18432K)==>GC前盖该内存区域已使用容量->GC后该区域已使用容量(该区域总容量)<br>
	14925K->13308K(38912K) ==> GC前Java堆已使用容量->GC后Java堆已使用容量(Java堆总容量)<br>
	0.0042158 secs ==> 该区域GC所占用时间，单位是秒
- ③、[Times: user=0.00 sys=0.00， real=0.00 secs] user，sys，real 与Linux的time命令输出的时间含义一致，分别代表用户态消耗的CPU时间，内核态消耗的CPU时间和操作从开始到结束锁经过的墙钟时间

### 4.2、大对象直接进入老年代

- 所谓大对象是指需要大量连续内存空间的Java对象，最典型的大对象就是那种很长的字符串以及数组.
- 经常出现大对象容易导致内存还有不少空间时就提前触发垃圾收集来获取足够的连续空间分配给新生成的大对象.
- 虚拟机提供了 `-XX:PretenureSizeThreshold` 参数，令大于这个设置值的对象直接在老年代分配，避免在Eden区和两个Survivor区之间发生大量的内存复制

### 4.3、长期存活的对象将进入老年代

虚拟机给每个对象定义了一个对象年龄计数器，来识别哪些对象放在新生代，哪些对象放在老年代中。如果对象在Eden出生并经过一次 MinorGC 后仍然存活，并且能够被 Survivor 容纳，将被移动到 Survivor 空间，并且对象年龄为1，当它的对象年龄增加到一定程度(默认15岁)，将会进入到老年代。对象晋升老年代的年龄阈值，可以通过参数 `-XX:MaxTenuringThreshold` 设置

**为什么是：`-XX:MaxTenuringThreshold` 默认值是15？**

我们在JVM虚拟机对象内存结构中可以看到，Hotspot虚拟机中 MarkWord 中，用了4个bit去表示分代年龄，那么能表示的最大范围就是`[0-16]`

### 4.4、动态对象年龄判定

如果在 Survivor 空间中相同年龄所有对象大小的总和大于 Survivor 空间的一半，年龄大于或等于该年龄的对象可以直接进入老年代，无须等到 MaxTenuringThreshold 中要求的年龄

### 4.5、空间分配担保

在发生 YGC 之前，虚拟机会先检查老年代最大可用的连续空间是否大于新生代所有对象总空间，如果这个条件成立，那么YGC可用确保是安全的。如果不成立，则虚拟机会查看 HandlePromotionFailure 设置是否允许打包失败，如果允许，那么会继续检查老年代最大可用连续空间是否大于历次晋升到老年代对象的平均大小，如果大于，将尝试着进行一次 YGC，有风险；如果小于或者 HandlePromotionFailure 设置不允许冒险，那时会改为进行一次 FullGC.

## 5、GC参数

- **5.1、JVM的GC日志的主要参数包括如下几个：**

	- `-XX:+PrintGC` 输出GC日志
	- `-XX:+PrintGCDetails` 输出GC的详细日志
	- `-XX:+PrintGCTimeStamps` 输出GC的时间戳（以基准时间的形式）
	- `-XX:+PrintGCDateStamps` 输出GC的时间戳（以日期的形式，如 2017-09-04T21:53:59.234+0800）
	- `-XX:+PrintHeapAtGC` 在进行GC的前后打印出堆的信息
	- `-Xloggc:../logs/gc.log` 日志文件的输出路径

	在生产环境中，根据需要配置相应的参数来监控JVM运行情况

- **5.2、Tomcat 设置示例：**
```
JAVA_OPTS="-server -Xms2000m -Xmx2000m -Xmn800m -XX:PermSize=64m -XX:MaxPermSize=256m -XX:SurvivorRatio=4
-verbose:gc -Xloggc:$CATALINA_HOME/logs/gc.log 
-Djava.awt.headless=true 
-XX:+PrintGCTimeStamps -XX:+PrintGCDetails 
-Dsun.rmi.dgc.server.gcInterval=600000 -Dsun.rmi.dgc.client.gcInterval=600000
-XX:+UseConcMarkSweepGC -XX:MaxTenuringThreshold=15"
```

## 6、元空间垃圾回收

- Metaspace 在空间不足时，会进行扩容，并逐渐达到设置的 MetaspaceSize。Metaspace 扩容到 -XX:MetaspaceSize 参数指定的量，就会发生 FGC。如果配置了 -XX:MetaspaceSize，那么触发 FGC 的阈值就是配置的值。如果 Old 区配置 CMS 垃圾回收，那么扩容引起的FGC也会使用CMS算法进行回

	新建类导致 Metaspace 容量不够，触发 GC，GC 完成后重新计算 Metaspace 新容量，决定是否对 Metaspace 扩容或缩容

- 老年代回收设置成非CMS时，Metaspace占用到达 -XX:MetaspaceSize会引发什么 GC？
	- 当老年代回收设置成 CMS GC 时，会触发一次 CMS GC
	- 不设置为 CMS GC时，使用如下配置进行测试，该配置并未设置 CMS GC，JDK 1.8 默认的老年代回收算法为 ParOldGen
		```
		-Xmx2048m -Xms2048m -Xmn1024m 
		-XX:MetaspaceSize=40m -XX:MaxMetaspaceSize=128m
		-XX:+PrintGCDetails -XX:+PrintGCDateStamps 
		-XX:+PrintHeapAtGC -Xloggc:d:/heap_trace.txt
		```
		由于 Metasapce 到达 -XX:MetaspaceSize = 40m 时候，触发了一次 YGC 和一次 Full GC，所以一般都会直描述，当 Metasapce 到达 -XX:MetaspaceSize 时会触发一次 Full GC

- 如何人工模拟 Metaspace 内存占用上升？

	Metaspace 会保存类的描述信息，JVM 需要根据 Metaspace 中的信息，才能找到堆中类 java.lang.Class 所对应的对象，既然 Metaspace 中会保存类描述信息，可以通过新建类来增加 Metaspace 的占用，于是想到，使用 CGlib 动态代理，生成被代理类的子类

## 7、GC触发

JVM 什么样的条件下选择进行 GC 操作，具体 Cause 的分类可以看一下 Hotspot 源码：`hotspot/src/share/vm/gc_interface/gcCause.hpp` 和 `hotspot/src/share/vm/gc_interface/gcCause.cpp` 中

重点需要关注的几个GC Cause：
- System.gc()： 手动触发GC操作。
- CMS： CMS GC 在执行过程中的一些动作，重点关注 CMS Initial Mark 和 CMS Final Remark 两个 STW 阶段。
- Promotion Failure： Old 区没有足够的空间分配给 Young 区晋升的对象（即使总可用内存足够大）。
- Concurrent Mode Failure： CMS GC 运行期间，Old 区预留的空间不足以分配给新的对象，此时收集器会发生退化，严重影响 GC 性能，下面的一个案例即为这种场景。
- GCLocker Initiated GC： 如果线程执行在 JNI 临界区时，刚好需要进行 GC，此时 GC Locker 将会阻止 GC 的发生，同时阻止其他线程进入 JNI 临界区，直到最后一个线程退出临界区时触发一次 GC

### 7.1、CMS GC触发

什么时机使用这些 Cause 触发回收，大家可以看一下 CMS 的代码，这里就不讨论了，具体在 `/src/hotspot/share/gc/cms/concurrentMarkSweepGeneration.cpp` 中

# 六、详解 finalize()方法

finalize是位于 Object 类的一个方法，该方法的访问修饰符为 protected.

- finalize函数没有自动实现链式调用，必须手动的实现，因此finalize函数的最后一个语句通常是 super.finalize()，通过这种方式，我们可以实现从下到上实现finalize的调用，即先释放自己的资源，然后再释放父类的资源;

- 根据Java语言规范，JVM保证调用finalize函数之前，这个对象是不可达的，但是JVM不保证这个函数一定会被调用。另外，规范还保证finalize函数最多运行一次

- GC 为了能够支持finalize函数，要对覆盖这个函数的对象作很多附加的工作。在finalize运行完成之后，该对象可能变成可达的，GC 还要再检查一次该对象是否是可达的。因此，使用finalize会降低GC的运行性能。由于GC调用finalize的时间是不确定的，因此通过这种方式释放资源也是不确定的

- 尽量少用finalize函数。finalize函数是Java提供给程序员一个释放对象或资源的机会。但是，它会加大GC的工作量，因此尽量少采用finalize方式回收资源
- 与C++的析构函数不同，析构函数调用确定，而它的是不都确定的

# 七、Java 有了GC同样会出现内存泄露问题

- 静态集合类像 HashMap、Vector 等的使用最容易出现内存泄露，这些静态变量的生命周期和应用程序一致，所有的对象Object也不能被释放，因为他们也将一直被Vector等应用着;

- 各种连接，数据库连接，网络连接，IO 连接等没有显示调用close关闭，不被 GC 回收导致内存泄露。

- 监听器的使用，在释放对象的同时没有相应删除监听器的时候也可能导致内存泄露

# 八、GC调优

* [垃圾优先型垃圾回收器调优](http://www.oracle.com/technetwork/cn/articles/java/g1gc-1984535-zhs.html)
* [Java11-GC调优](https://docs.oracle.com/en/java/javase/11/gctuning/introduction-garbage-collection-tuning.html)

## 1、监控GC

**1.1、垃圾回收收集监控指的是搞清楚 JVM 如何执行 GC 的过程，如可以查明**

- 何时一个新生代中的对象被移动到老年代时，所花费的时间
- Stop-the-world 何时发生的，持续了多长时间GC 监控是为了鉴别 JVM 是否在高效地执行 GC，以及是否有必要进行额外的性能调优

**1.2、如何监控**

- jstat：是 HotSpot JVM 提供的一个监控工具；jstat 不仅提供 GC 操作的信息，还提供类装载操作的信息以及运行时编译器操作的信息<br>
	`$> jstat –gc  $<vmid$> 1000`<br>
	==> vmid (虚拟机 ID)

## 2、GC 优化

如果你没有设定内存的大小，并且系统充斥着大量的超时日志时，你就需要在你的系统中进行GC优化了；多数的 Java 应用不需要在服务器上进行 GC 优化；

### 2.1、GC优化的目的

- 将转移到老年代的对象数量降到最少：减少被移到老年代空间对象的数量，可以调整新生代的空间。减少 FullGC 的频率.
- 减少 Full GC 执行的时间：FullGC 的执行时间要比 MinorGC 要长很多。如果试图通过削减老年代空间来减少 FullGC 的执行时间，可能会导致OutOfMemoryError 或者增加 FullGC 执行此数与之相反，如果你试图通过增加老年代空间来减少 Full GC 执行次数，执行时间会增加；

目标：
- 响应速度（Responsiveness）：响应速度指程序或系统对一个请求的响应有多迅速；比如，用户订单查询响应时间，对响应速度要求很高的系统，较大的停顿时间是不可接受的。调优的重点是在短的时间内快速响应。
- 吞吐量（Throughput）：吞吐量关注在一个特定时间段内应用系统的最大工作量；例如每小时批处理系统能完成的任务数量，在吞吐量方面优化的系统，较长的 GC 停顿时间也是可以接受的，因为高吞吐量应用更关心的是如何尽可能快地完成整个任务，不考虑快速响应用户请求；

> 在 GC 调优中，GC 导致的应用暂停时间影响系统响应速度，GC 处理线程的 CPU 使用率影响系统吞吐量

### 2.2、GC优化需要考虑的Java参数

- 堆内存空间：
	- -Xms：Heap area size when starting JVM，启动JVM时的堆内存空间
	- -Xmx：Maximum heap area size，堆内存最大限制

- 新生代空间:
	- -XX:NewRatio：Ratio of New area and Old area，新生代和老年代的占比
	- -XX:NewSize：New area size，ret新生代空间
	- -XX:SurvivorRatio：Ratio ofEdenarea and Survivor area，Eden 和 Suvivor 空间的占比

- -server参数

### 2.3、当OutOfMemoryError错误发生并且是由于Perm空间不足导致时，另一个可能影响GC性能的参数是GC类型

收集器|参数|备注
--------|------|------
Serial GC |-XX:+UseSerialGC|
Parallel GC|-XX:+UseParallelGC<br>-XX:ParallelGCThreads=value|
Parallel Compacting GC|-XX:+UseParallelOldGC|
CMS GC|-XX:+UseConcMarkSweepGC<br>-XX:+UseConcMarkSweepGC<br>-XX:+UseParNewGC<br>-XX:+CMSParallelRemarkEnabled<br>-XX:CMSInitiatingOccupancyFraction=value<br>-XX:+UseCMSInitiatingOccupancyOnly|
G1|-XX:+UnlockExperimentalVMOptions<br>-XX:+UseG1GC|在JDK6中这两个参数必须同时使用


==> 最常用的GC类型是Serial GC

### 2.4、GC优化过程

- 监控GC状态；
- 在分析监控结果后，决定是否进行GC优化；
- 调整GC类型/内存空间
- 分析结果
- 如果结果令人满意，你可以将该参数应用于所有的服务器，并停止GC优化<br>
	-XX:+PrintGCDetails  表示在控制台上打印出GC具体细节<br>
	-XX:+PrintGC 表示在控制台上打印出GC信息
- 初始参数：
	```
	-XX:+DisableExplicitGC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./ -XX:+PrintGCDetails -XX:+PringGCTimeStamps -XX:+PrintGCDateStamps -Xloggc:/logs/gc.log
	```

### 2.5、调优参数

- Parallel GC：
	```
	-XX:+UseParallelGC -XX:+UseParallelOldGC -XX:MaxGCPauseMillis=100 -XX:GCTimeRatio=99 -XX:YoungGenerationSizeIncrement=30
	```

- CMS GC

	```
	-XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=92 -XX:+UseCMSInitiatingOccupancyOnly -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCBeforeCompaction=5
	```

- G1 GC
	```
	-XX:+UseG1GC -Xms128M -Xmx128M -XX:MetaspaceSize=64M -XX:MaxGCPauseMillis=100 -XX:+UseStringDeduplication -XX:StringDeduplicationAgeThreshold=3
	```

### 2.6、GC可视化工具

- [gceasy](https://gceasy.io/)

- [GCViewer](https://github.com/chewiebug/GCViewer)

	*注意：* GCViewer是个maven工程，手动编译：maven clean install -Dmaven.test.skip=true，直接打开jar包导入gc.log文件即可有相应的统计信息


### 2.7、常见调优思路

- 理解应用需求和问题，确定调优目标。比如开发了一个应用服务，但发现偶尔会出现性能抖动，出现较长的服务停顿。评估用户可接受的响应时间和业务量，将目标简化为，希望GC暂停尽量控制在200ms以内，并保证一定的标准吞吐量；
- 掌握JVM和GC的状态，定位具体的问题，确定是否有GC调优的必要。比如通过jstat等工具查看GC等相关状态，可以开启GC日志，或者利用操作系统提供的诊断工具；比如通过追踪GC日志，可以查找是不是GC在特定的实际发生了长时间的暂停；
- 需要考虑选择的GC类型是否符合我们的应用特征，如果是，具体问题表现在那里，是MinorGC过长还是MixedGC等出现异常停顿情况；如果不是，考虑切换到什么类型，如果CMS个G1都是更侧重于低延迟的GC选型；
- 通过分析确定具体调整的参数或者软硬件配置；
- 验证是否达到调优目标，如果达到调优目标，可以考虑结束调优；否则重复完成分析、调整、验证整个过程；

## 3、ParallelGC调优

### 3.1、基本原则

- 除非确定，否则不要设置最大堆内存；
- 优先设置吞吐量目标；
- 如果吞吐量目标达不到，调大最大内存，不能让OS使用Swap，如果仍然达不到，降低目标
- 吞吐量能达到，GC时间太长，设置停顿时间的目标

## 4、G1 GC调优

### 4.1、关于global concurrent marking

- 堆占有率达到-XX:InitiatinHeapOccupancyPercent的数值触发 global concurrent marking，默认值为45%
- 在global concurrent marking结束之后，可以知道young区有多少空间要被回收，在每次YGC之后和每次发生MixedGC之前，会检查垃圾占比是否达到-XX:G1HeapWastePercent，只有达到了下次才会发生MixedGC

### 4.2、主要调优步骤

- （1）首先开启G1 GC：-XX:+UseG1GC
- （2）如果发现是metaspace分配引起GC次数多，可以适当调大metaspace大小：-XX:MetaspaceSize=xx
- （3）增大堆内存 -Xmxsize -Xmssize

## 5、CMS GC调优

* [JVM发生CMS GC的5中情况](https://mp.weixin.qq.com/s/ezmD1XXgPoVoCo0nkKavdg)
* [CMS GC常见问题](https://tech.meituan.com/2020/11/12/java-9-cms-gc.html)

## 6、常见调优策略

- 将新对象预留在新生代，由于 Full GC 的成本远高于 Minor GC，因此尽可能将对象分配在新生代是明智的做法，实际项目中根据 GC 日志分析新生代空间大小分配是否合理，适当通过“-Xmn”命令调节新生代大小，最大限度降低新对象直接进入老年代的情况；
- 大对象进入老年代，虽然大部分情况下，将对象分配在新生代是合理的。但是对于大对象这种做法却值得商榷，大对象如果首次在新生代分配可能会出现空间不足导致很多年龄不够的小对象被分配的老年代，破坏新生代的对象结构，可能会出现频繁的 full gc。因此，对于大对象，可以设置直接进入老年代（当然短命的大对象对于垃圾回收老说简直就是噩梦）。-XX:PretenureSizeThreshold 可以设置直接进入老年代的对象大小；
- 合理设置进入老年代对象的年龄，-XX:MaxTenuringThreshold 设置对象进入老年代的年龄大小，减少老年代的内存占用，降低 full gc 发生的频率；
- 设置稳定的堆大小，堆大小设置有两个参数：-Xms 初始化堆大小，-Xmx 最大堆大小；
- 如果满足下面的指标，则一般不需要进行 GC 优化：
	- MinorGC 执行时间不到50ms；
	- Minor GC 执行不频繁，约10秒一次；
	- Full GC 执行时间不到1s；
	- Full GC 执行频率不算频繁，不低于10分钟1次。

**调优的不可能三角：**
- 内存占用（Footprint）
- 吞吐量（Throughput）
- 延迟（Latency）

比如，对于特定软件，GC要回收的垃圾总量基本是确定的。那么要想回收垃圾，提高GC的频率，这样每次GC的时间就比较少；要么少做几次GC，每次多停顿一些时间。

又比如，而为了降低延迟，CMS/G1采用了并发收集，并发阶段GC线程和业务线程同时运行。但这样做，并发阶段GC线程和业务线程互相都有影响，吞吐量会有所降低。

因此，以上三者最多只能选其二

**如下配置可获得较高的吞吐量：** `java -server -XX:+UseParallelGC -XX:+UseLargePages -Xmn10g -Xms26g -Xmx26g`

**如下配置可获得较低的延迟：** `java -XX:+UseG1GC -Xms26g Xmx26g -XX:MaxGCPauseMillis=500 -XX:+PrintGCTimeStamp`

**如下配置可让堆内存比较小，并可减少动态内存分配的占用：** `-XX:MaxHeapFreeRatio=10 -XX:MinHeapFreeRatio=5`；默认情况下这两个值是70和40。`空闲内存>70%`才会收缩堆；`<40%`才会扩展堆。意味着大量的内存都是动态分配的；

# 九、减少GC开销

## 1、避免隐式的 String 字符串

- "+"操作就会分配一个链接两个字符串的新的字符串，这里分配了一个隐式的StringBuilder对象来链接两个 String 字符串
	如:a = a + b; // a and b are Strings<br>
	编译器在背后就会生成这样的一段儿代码:
	```java
	StringBuilder temp = new StringBuilder(a).
	temp.append(b);
	a = temp.toString(); // 一个新的 String 对象被分配
	// 第一个对象 "a"现在可以说是垃圾了
	```
- 解决方案:减少垃圾对象的一种方式就是善于使用 StringBuilder 来建对象

## 2、计划好 List 的容量

- 问题
	- ArrayList 的构造器并不知道这个值的大小，构造器会分配一个默认的Object数组的大小。一旦内部数组溢出，它就会被一个新的、并且足够大的数组代替，这就使之前分配的数组成为了垃圾
	- 如果执行数千次的循环，那么就会进行更多次数的新数组分配操作，以及更多次数的旧数组回收操作。对于在大规模环境下运行的代码，这些分配和释放的操作应该尽可能从CPU周期中剔除

- 无论什么时候，尽可能的给List或者Map分配一个初始容量，就像这样：因为List初始化，有足够的容量，所有这样可以减少内部数组在运行时不必要的分配和释放

## 3、使用高效的含有原始类型的集合

当前版本的Java编译器对于含有基本数据类型的键的数组以及Map的支持，是通过“装箱”来实现的-自动装箱就是将原始数据装入一个对应的对象中，这个对象可被GC分配和回收

## 4、使用数据流(Streams)代替内存缓冲区（in-memory buffers）

- 问题：<br>
	通过过ByteArrayInputStream，ByteBuffer 把数据读入内存中，然后再进行反序列化，完整的数据在构造新的对象的时候，你需要为其分配空间，然后立刻又释放空间。并且，由于数据的大小你又不知道，你只能猜测 – 当超过初始化容量的时候，不得不分配和释放 byte[]数组来存储数据；

- 解决方案：
	像Java自带的序列化工具以及Google的Protocol Buffers等，它们可以将来自于文件或网络流的数据进行反序列化，而不需要保存到内存中，也不需要分配新的byte数组来容纳增长的数据

## 5、List集合

- 问题：当方法返回一个集合，通常会很明智的在方法中创建一个集合对象（如ArrayList），填充它，并以不变的集合的形式返回；
- 解决方案：这种情况的解决方案将不会返回新的集合，而是通过使用单独的集合当做参数传入到那些方法代替组合的集合；

# 十、JVM日志

* [Xlog](https://docs.oracle.com/en/java/javase/11/tools/java.html#GUID-BE93ABDC-999C-4CB5-A88B-1994AAAC74D5)

## 1、打印GC、运行时日志

JDK 8垃圾收集日志打印参数：`-Xms50m -Xmx50m -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+PrintGCCause -Xloggc:./gclog.log`

JDK8运行时日志打印参数：`-XX:+TraceClassLoading -XX:+TraceBiasedLocking`

上述打印的日志毫无规律，在JDK9之后，使用Xlog进行统一日志管理

## 2、Xlog

### 2.1、什么是Xlog

统一日志管理，适用于JDK 9及更高版本；可使用`-Xlog`选项，启用统一日志管理。

Xlog选项支持的参数如下：
- `-Xlog`：使用info级别启用JVM日志，如果只指定了 -Xlog，其等价于：`-Xlog:all=warning:stdout:uptime,level,tags`
- `-Xlog:help`：打印Xlog帮助文档
- `-Xlog:disable`：关闭所有日志记录并清除日志记录框架的所有配置，包括警告和错误的默认配置
- `-Xlog[:option]`：按照命令行上出现的顺序应用多个参数。同一输出的多个参数按其给定顺序覆盖。option的格式为：`[:[what][:[output][:[decorators][:output-options[,...]]]]]`，其中：
	- what：指定level和tag的组合，格式：tag1[+tag2...][*][=level][,...] 。除非用 * 指定了通配符，否则只有匹配了指定tag的日志消息才会被匹配。详见 [Xlog标签和级别]
	- output：设置输出类型。默认为stdout，详见 Xlog输出
	- decorators：使用一系列自定义的装饰器去配置output。缺省的装饰器为uptime、level和tags。详见 装饰器
	- output-options：设置Xlog的日志输出选项，格式：`filecount=file-count filesize=file size with optional K, M or G suffix`

### 2.2、Xlog标签和级别

每个日志消息都有一个级别和与之关联的tag集合。消息的级别与其详细信息相对应，tag集与消息包含的内容或者消息所涉及的JVM组件（例如GC、编译器或线程）相对应。

- 可用的日志级别：off、trace、debug、info、warning、error；

- 可用的日志标签（ 如指定为all，则表示下面所有标签的组合）：`add, age, alloc, annotation, aot, arguments, attach, barrier, biasedlocking, blocks, bot, breakpoint, bytecode, census, class, classhisto, cleanup, compaction, comparator, constraints, constantpool, coops, cpu, cset, data, defaultmethods, dump, ergo, event, exceptions, exit, fingerprint, freelist, gc, hashtables, heap, humongous, ihop, iklass, init, itables, jfr, jni, jvmti, liveness, load, loader, logging, mark, marking, metadata, metaspace, method, mmu, modules, monitorinflation, monitormismatch, nmethod, normalize, objecttagging, obsolete, oopmap, os, pagesize, parser, patch, path, phases, plab, preorder, promotion, protectiondomain, purge, redefine, ref, refine, region, remset, resolve, safepoint, scavenge, scrub, setting, stackmap, stacktrace, stackwalk, start, startuptime, state, stats, stringdedup, stringtable, subclass, survivor, sweep, system, task, thread, time, timer, tlab, unload, update, verification, verify, vmoperation, vtables, workgang`；

下表描述了标签和级别的组合：
<table>
    <tr>
        <td align="left"><code>-Xlog:gc</code></td>
        <td align="left">打印 <code>gc</code> 信息以及垃圾回收发生的时间。</td>
    </tr>
    <tr>
        <td align="left"><code>-Xlog:gc*</code></td>
        <td align="left">打印至少包含 <code>gc</code> 标签的日志消息。它还可以具有与其关联的其他标签。但是，它不会提供<code>phase</code>级别信息。</td>
    </tr>
    <tr>
        <td align="left"><code>-Xlog:gc*=trace</code></td>
        <td align="left">打印trace级别及更高的<code>gc</code>日志记录信息。输出显示所有<code>gc</code>相关标签以及详细的日志记录信息。</td>
    </tr>
    <tr>
        <td align="left"><code>-Xlog:gc+phases=debug</code></td>
        <td align="left">打印不同的<code>phase</code>级别信息。这提供了在<code>debug</code>级别记录的详细信息级别。</td>
    </tr>
    <tr>
        <td align="left"><code>-Xlog:gc+heap=debug</code></td>
        <td align="left">在gc之前和之后打印堆的使用详细。这将会以debug级别打印带有tag和heap的标记的日志</td>
    </tr>
    <tr>
        <td align="left"><code>-Xlog:safepoint</code></td>
        <td align="left">在同一级别上打印有关应用并发时间（application concurrent time）和停顿时间（application stop time）的详细信息。</td>
    </tr>
    <tr>
        <td align="left"><code>-Xlog:gc+ergo*=trace</code></td>
        <td align="left">以<code>trace</code> 级别同时打印<code>gc</code>和<code>ergo</code>消息的组合。该信息包括有关堆大小和收集集构造的所有详细信息。</td>
    </tr>
    <tr>
        <td align="left"><code>-Xlog:gc+age=trace</code></td>
        <td align="left">以trace级别 打印存活区的大小、以及存活对象在存活区的年龄分布</td>
    </tr>
    <tr>
        <td align="left"><code>-Xlog:gc*:file=::filecount=,filesize=</code></td>
        <td align="left">将输出重定向到文件，在其中指定要使用的文件数和文件大小，单位 <code>kb</code></td>
    </tr>
</table>

### 2.3、-Xlog输出

-Xlog 支持以下类型的输出：
- `stdout` ：将输出发送到标准输出
- `stderr` ：将输出发送到stderr
- `file=filename` ：将输出发送到文本文件。你还可以让文件按照文件大小轮换，例如每记录10M就轮换，只保留5个文件等。默认情况下，最多保留5个20M的文件。可使用 filesize=10M, filecount=5 格式去指定文件大小和保留的文件数。

### 2.4、装饰器

装饰器用来装饰消息，记录与消息有关的信息。可以为每个输出配置一组自定义的装饰器，输出顺序和定义的顺序相同。缺省的装饰器为uptime、level和tags。none表示禁用所有的装饰器。

下表展示了所有可用的装饰器：
<table>
    <tr>
        <th align="left">装饰器</th><th align="left">描述</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td align="left"><code>time</code> or <code>t</code></td>
        <td align="left">ISO-8601格式的当前日期时间</td>
    </tr>
    <tr>
        <td align="left"><code>utctime</code> or <code>utc</code></td>
        <td align="left">Universal Time Coordinated or Coordinated Universal Time.</td>
    </tr>
    <tr>
        <td align="left"><code>uptime</code> or <code>u</code></td>
        <td align="left">JVM启动了多久，以秒或毫秒为单位。例如6.567s.</td>
    </tr>
    <tr>
        <td align="left"><code>timemillis</code> or <code>tm</code></td>
        <td align="left">相当于 <code>System.currentTimeMillis()</code></td>
    </tr>
    <tr>
        <td align="left"><code>uptimemillis</code> or <code>um</code></td>
        <td align="left">JVM启动以来的毫秒数</td>
    </tr>
    <tr>
        <td align="left"><code>timenanos</code> or <code>tn</code></td>
        <td align="left">相当于 <code>System.nanoTime()</code></td>
    </tr>
    <tr>
        <td align="left"><code>uptimenanos</code> or <code>un</code></td>
        <td align="left">JVM启动以来的纳秒数</td>
    </tr>
    <tr>
        <td align="left"><code>hostname</code> or <code>hn</code></td>
        <td align="left">主机名</td>
    </tr>
    <tr>
        <td align="left"><code>pid</code> or <code>p</code></td>
        <td align="left">The process identifier.</td>
    </tr>
    <tr>
        <td align="left"><code>tid</code> or <code>ti</code></td>
        <td align="left">打印线程号</td>
    </tr>
    <tr>
        <td align="left"><code>level</code> or <code>l</code></td>
        <td align="left">与日志消息关联的级别</td>
    </tr>
    <tr>
        <td align="left"><code>tags</code> or <code>tg</code></td>
        <td align="left">与日志消息关联的标签集</td>
    </tr>
</table>

### 2.5、使用示例

```bash
# 示例1：使用info级别记录所有信息到stdout，装饰器使用uptime、level及tags
# 等价于-Xlog:all=info:stdout:uptime,levels,tags
-Xlog

# 示例2：以info级别打印使用了gc标签的日志到stdout
-Xlog:gc

# 示例3：使用默认装饰器，info级别，将使用gc或safepoint标签的消息记录到stdout。
# 如果某个日志同时标签了gc及safepoint，不会被记录
-Xlog:gc,safepoint

# 示例4：使用默认装饰器，debug级别，打印同时带有gc和ref标签的日志。
# 仅使用gc或ref的日志不会被记录
-Xlog:gc+ref=debug

# 示例5：不使用装饰器，使用debug级别，将带有gc标签的日志记录到gc.txt中
-Xlog:gc=debug:file=gc.txt:none

# 示例6：以trace级别记录所有带有gc标签的日志到gctrace.txt文件集中，该文件集中的文件最大1M，保留5个文件；使用的装饰器是uptimemillis、pids
-Xlog:gc=trace:file=gctrace.txt:uptimemillis,pids:filecount=5,filesize=1024

# 示例7：使用trace级别，记录至少带有gc及meta标签的日志到gcmetatrace.txt，同时关闭带有class的日志。某个消息如果同时带有gc、meta及class，将不会被记录，因为class标签被关闭了。
-Xlog:gc+meta*=trace,class*=off:file=gcmetatrace.txt
```

### 2.6、旧式日志和Xlog的对照

**旧式GC日志和Xlog的对照：**

<table>
    <thead>
    <tr>
        <th align="left">旧式GC标记</th>
        <th align="left">Xlog配置</th>
        <th align="left">注释</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td align="left"><code>G1PrintHeapRegions</code></td>
        <td align="left"><code>-Xlog:gc+region=trace</code></td>
        <td align="left">-</td>
    </tr>
    <tr>
        <td align="left"><code>GCLogFileSize</code></td>
        <td align="left">No configuration available</td>
        <td align="left">日志轮换由框架处理</td>
    </tr>
    <tr>
        <td align="left"><code>NumberOfGCLogFiles</code></td>
        <td align="left">Not Applicable</td>
        <td align="left">日志轮换由框架处理</td>
    </tr>
    <tr>
        <td align="left"><code>PrintAdaptiveSizePolicy</code></td>
        <td align="left"><code>-Xlog:gc+ergo*=level</code></td>
        <td align="left">使用debug级别可打印大部分信息，使用trace级别可打印所有 <code>PrintAdaptiveSizePolicy</code> 打印的信息</td>
    </tr>
    <tr>
        <td align="left"><code>PrintGC</code></td>
        <td align="left"><code>-Xlog:gc</code></td>
        <td align="left">-</td>
    </tr>
    <tr>
        <td align="left"><code>PrintGCApplicationConcurrentTime</code></td>
        <td align="left"><code>-Xlog:safepoint</code></td>
        <td align="left">注意： <code>PrintGCApplicationConcurrentTime</code> 和 <code>PrintGCApplicationStoppedTime</code>  是记录在同一tag之上的，并且没有被分开</td>
    </tr>
    <tr>
        <td align="left"><code>PrintGCApplicationStoppedTime</code></td>
        <td align="left"><code>-Xlog:safepoint</code></td>
        <td align="left">注意： <code>PrintGCApplicationConcurrentTime</code> 和 <code>PrintGCApplicationStoppedTime</code>  是记录在同一tag之上的，并且没有被分开</td>
    </tr>
    <tr>
        <td align="left"><code>PrintGCCause</code></td>
        <td align="left">Not Applicable</td>
        <td align="left">Xlog总是会记录GC cause</td>
    </tr>
    <tr>
        <td align="left"><code>PrintGCDateStamps</code></td>
        <td align="left">Not Applicable</td>
        <td align="left">日期戳由框架记录</td>
    </tr>
    <tr>
        <td align="left"><code>PrintGCDetails</code></td>
        <td align="left"><code>-Xlog:gc*</code></td>
        <td align="left">-</td>
    </tr>
    <tr>
        <td align="left"><code>PrintGCID</code></td>
        <td align="left">Not Applicable</td>
        <td align="left">Xlog总是会记录GC ID</td>
    </tr>
    <tr>
        <td align="left"><code>PrintGCTaskTimeStamps</code></td>
        <td align="left"><code>-Xlog:gc+task*=debug</code></td>
        <td align="left">-</td>
    </tr>
    <tr>
        <td align="left"><code>PrintGCTimeStamps</code></td>
        <td align="left">Not Applicable</td>
        <td align="left">时间戳由框架记录</td>
    </tr>
    <tr>
        <td align="left"><code>PrintHeapAtGC</code></td>
        <td align="left"><code>-Xlog:gc+heap=trace</code></td>
        <td align="left">-</td>
    </tr>
    <tr>
        <td align="left"><code>PrintReferenceGC</code></td>
        <td align="left"><code>-Xlog:gc+ref*=debug</code></td>
        <td align="left">注意：旧式写法中，<code>PrintGCDetails</code>启用时， <code>PrintReferenceGC</code> 才会生效</td>
    </tr>
    <tr>
        <td align="left"><code>PrintStringDeduplicationStatistics</code></td>
        <td align="left"><code>-Xlog:gc+stringdedup*=debug</code></td>
        <td align="left">-</td>
    </tr>
    <tr>
        <td align="left"><code>PrintTenuringDistribution</code></td>
        <td align="left"><code>-Xlog:gc+age*=level</code></td>
        <td align="left">使用debug日志级别记录最相关信息；trace级别记录所有 <code>PrintTenuringDistribution</code> 会打印的信息。</td>
    </tr>
    <tr>
        <td align="left"><code>UseGCLogFileRotation</code></td>
        <td align="left">Not Applicable</td>
        <td align="left">用来记录 <code>PrintTenuringDistribution</code></td>
    </tr>
    </tbody>
</table>

**旧式运行时日志和Xlog的对照：**

<table>
    <thead>
    <tr>
        <th align="left">旧式运行时标记</th>
        <th align="left">Xlog配置</th>
        <th align="left">注释</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td align="left"><code>TraceExceptions</code></td>
        <td align="left"><code>-Xlog:exceptions=info</code></td>
        <td align="left">-</td>
    </tr>
    <tr>
        <td align="left"><code>TraceClassLoading</code></td>
        <td align="left"><code>-Xlog:class+load=level</code></td>
        <td align="left">使用info级别记录常规信息，debug级别记录额外信息。在统一日志记录语法中， <code>-verbose:class</code> 等价于 <code>-Xlog:class+load=info,class+unload=info</code>.</td>
    </tr>
    <tr>
        <td align="left"><code>TraceClassLoadingPreorder</code></td>
        <td align="left"><code>-Xlog:class+preorder=debug</code></td>
        <td align="left">-</td>
    </tr>
    <tr>
        <td align="left"><code>TraceClassUnloading</code></td>
        <td align="left"><code>-Xlog:class+unload=level</code></td>
        <td align="left">使用info级别记录常规信息，debug级别记录额外信息。在统一日志记录语法中， <code>-verbose:class</code> 等价于 <code>-Xlog:class+load=info,class+unload=info</code>.</td>
    </tr>
    <tr>
        <td align="left"><code>VerboseVerification</code></td>
        <td align="left"><code>-Xlog:verification=info</code></td>
        <td align="left">-</td>
    </tr>
    <tr>
        <td align="left"><code>TraceClassPaths</code></td>
        <td align="left"><code>-Xlog:class+path=info</code></td>
        <td align="left">-</td>
    </tr>
    <tr>
        <td align="left"><code>TraceClassResolution</code></td>
        <td align="left"><code>-Xlog:class+resolve=debug</code></td>
        <td align="left">-</td>
    </tr>
    <tr>
        <td align="left"><code>TraceClassInitialization</code></td>
        <td align="left"><code>-Xlog:class+init=info</code></td>
        <td align="left">-</td>
    </tr>
    <tr>
        <td align="left"><code>TraceLoaderConstraints</code></td>
        <td align="left"><code>-Xlog:class+loader+constraints=info</code></td>
        <td align="left">-</td>
    </tr>
    <tr>
        <td align="left"><code>TraceClassLoaderData</code></td>
        <td align="left"><code>-Xlog:class+loader+data=level</code></td>
        <td align="left">使用info级别记录常规信息，debug级别记录额外信息。</td>
    </tr>
    <tr>
        <td align="left"><code>TraceSafepointCleanupTime</code></td>
        <td align="left"><code>-Xlog:safepoint+cleanup=info</code></td>
        <td align="left">-</td>
    </tr>
    <tr>
        <td align="left"><code>TraceSafepoint</code></td>
        <td align="left"><code>-Xlog:safepoint=debug</code></td>
        <td align="left">-</td>
    </tr>
    <tr>
        <td align="left"><code>TraceMonitorInflation</code></td>
        <td align="left"><code>-Xlog:monitorinflation=debug</code></td>
        <td align="left">-</td>
    </tr>
    <tr>
        <td align="left"><code>TraceBiasedLocking</code></td>
        <td align="left"><code>-Xlog:biasedlocking=level</code></td>
        <td align="left">使用info级别记录常规信息，debug级别记录额外信息。</td>
    </tr>
    <tr>
        <td align="left"><code>TraceRedefineClasses</code></td>
        <td align="left"><code>-Xlog:redefine+class*=level</code></td>
        <td align="left">使用level=info，level=debug和level=trace提供越来越多的信息。</td>
    </tr>
    </tbody>
</table>

## 3、GC日志

### 3.1、JDK8中Serial等垃圾收集器日志

GC日志打印相关参数：
<table>
	<thead>
	<tr>
		<th>参数</th><th>作用</th><th>默认值</th>
	</tr>
	</thead>
	<tbody>
	<tr>
		<td>-XX:+PrintGC</td><td>输出GC日志</td><td>关闭</td>
	</tr>
	<tr>
		<td>-XX:+PrintGCDetails</td><td>打印GC的详情</td><td>关闭</td>
	</tr>
	<tr>
		<td>-XX:+PrintGCCause</td><td>是否在GC日志中打印造成GC的原因</td><td>打开</td>
	</tr>
	<tr>
		<td>-XX:+PrintGCID</td><td>打印垃圾GC的唯一标识</td><td>关闭</td>
	</tr>
	<tr>
		<td>-XX:+PrintGCDateStamps</td><td>以日期的格式输出GC的时间戳，如 2013-05-04T21:53:59.234+0800</td><td>关闭</td>
	</tr>
	<tr>
		<td>-XX:+PrintGCTimeStamps</td><td>以基准时间的格式，打印GC的时间戳</td><td>关闭</td>
	</tr>
	<tr>
		<td>-XX:+PrintGCTaskTimeStamps</td><td>为每个GC工作线程的任务打印时间戳</td><td>关闭</td>
	</tr>
	<tr>
		<td>-XX:+PrintHeapAtGC</td><td>在GC前后打印堆信息</td><td>关闭</td>
	</tr>
	<tr>
		<td>-XX:+PrintHeapAtGCExtended</td><td>在开启PrintHeapAtGC的前提下，额外打印更多堆相关的信息</td><td>关闭</td>
	</tr>
	<tr>
		<td>-XX:+PrintGCApplicationStoppedTime</td><td>打印垃圾回收期间程序暂停的时间</td><td>关闭</td>
	</tr>
	<tr>
		<td>-XX:+PrintGCApplicationConcurrentTime</td><td>打印每次垃圾回收前,程序未中断的执行时间，可与PrintGCApplicationStoppedTime配合使用</td>
		<td>关闭</td>
	</tr>
	<tr>
		<td>-XX:+PrintClassHistogramAfterFullGC</td><td>Full GC之后打印堆直方图</td><td>关闭</td>
	</tr>
	<tr>
		<td>-XX:+PrintClassHistogramBeforeFullGC</td>
		<td>Full GC之前打印堆直方图</td>
		<td>关闭</td>
	</tr>
	<tr>
		<td>-XX:+PrintReferenceGC</td>
		<td>打印处理引用对象的时间消耗，需开启PrintGCDetails才有效</td>
		<td>关闭</td>
	</tr>
	<tr>
		<td>-XX:+PrintTLAB</td>
		<td>查看TLAB空间的使用情况</td>
		<td>关闭</td>
	</tr>
	<tr>
		<td>-XX:-UseGCLogFileRotation</td>
		<td>轮换文件，日志文件达到一定大小后，就创建一个新的日志文件。需指定-Xloggc:时才有效。</td>
		<td>关闭</td>
	</tr>
	<tr>
		<td>-XX:GCLogFileSize</td>
		<td>设置单个日志文件的大小，需开启UseGCLogFileRotation才有效</td>
		<td>8KB</td>
	</tr>
	<tr>
		<td>-XX:NumberOfGCLogFiles</td>
		<td>日志轮换时，保留几个日志文件，默认0，保留所有日志</td>
		<td>0</td>
	</tr>
	<tr>
		<td>-Xloggc:文件路径</td>
		<td>指定GC日志文件路径</td>
		<td>-</td>
	</tr>
	<tr>
		<td>-XX:+PrintAdaptiveSizePolicy</td>
		<td>某些GC收集器有自适应策略，自适应调整策略会动态调整Eden、Survivor、老年代的大小。使用该标记，可打印自适应调节策略相关的信息</td>
		<td>关闭</td>
	</tr>
	<tr>
		<td>-XX:+PrintTenuringDistribution</td>
		<td>查看每次minor GC后新的存活周期的阈值。Desired survivor size 1048576 bytes, new threshold 7 (max 15)。其中，7新的存活周期的阈值为7</td>
		<td>关闭</td>
	</tr>
	</tbody>
</table>

示例：`java -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+UseSerialGC -Xmx50m -Xloggc:./gc.log xxx.jar`

youngGC日志
```log
SerialGC：
2020-07-18T19:33:17.044-0800: 1.127: [GC (Allocation Failure) 2020-07-18T19:33:17.044-0800: 1.128: [DefNew: 14339K->1250K(15360K), 0.0028567 secs] 16974K->3885K(49536K), 0.0029588 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 

ParallelGC
2020-07-18T19:45:56.995-0800: 0.701: [GC (Allocation Failure) [PSYoungGen: 12800K->1878K(14848K)] 12800K->1886K(49152K), 0.0025563 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
```
其中Young GC日志含义如下：
<ul>
	<li>2020-07-18T19:33:17.044-0800：当前时间戳，由PrintGCDateStamps控制</li>
	<li>1.127：当前相对时间戳，表示应用启动多久后触发，由PrintGCTimeStamps控制</li>
	<li>GC (Allocation Failure)：造成GC的原因，由PrintGCCause控制</li>
	<li>[DefNew: 14339K->1250K(15360K), 0.0028567 secs]：
		<ul>
			<li>DefNew：使用不同垃圾收集器，这里的展示不同：
				<ul>
					<li>使用Serial收集器：显示DefNew，表示Default New</li>
					<li>使用ParNew收集器：显示ParNew</li>
					<li>使用Paralle Scavenge收集器：显示PSYoungGen</li>
					<li>使用G1：G1格式和这个日志格式不一样，很好区分</li>
				</ul>
			</li>
			<li>14339K：回收前，年轻代使用的大小</li>
			<li>1250K：回收后，年轻代使用的大小</li>
			<li>15360K：年轻代总大小</li>
			<li>0.0028567：花费了多久</li>
		</ul>
	</li>
	<li>16974K：回收前，堆使用的大小</li>
	<li>3885K：回收后，堆使用的大小</li>
	<li>(49536K)：堆的总大小</li>
	<li>0.0029588 secs：花费时间</li>
	<li>user=0.00：用户耗时</li>
	<li>sys=0.00：系统耗时</li>
	<li>real=0.00：实际耗时</li>
</ul>

Full GC日志：
```log
SerialOld日志
2020-07-18T19:33:17.566-0800: 1.649: [Full GC (Metadata GC Threshold) 2020-07-18T19:33:17.566-0800: 1.650: [Tenured: 4802K->6349K(34176K), 0.0206760 secs] 16930K->6349K(49536K), [Metaspace: 20585K->20585K(1067008K)], 0.0207847 secs] [Times: user=0.02 sys=0.00, real=0.02 secs]  

ParallelOld日志
2020-07-18T19:45:58.133-0800: 1.840: [Full GC (Metadata GC Threshold) [PSYoungGen: 757K->0K(12800K)] [ParOldGen: 8276K->5730K(23040K)] 9033K->5730K(35840K), [Metaspace: 20555K->20554K(1067008K)], 0.0297490 secs] [Times: user=0.10 sys=0.00, real=0.03 secs] 

-- CMS日志，有清晰的CMS的过程
2020-07-18T19:49:16.307-0800: 5.104: [GC (CMS Initial Mark) [1 CMS-initial-mark: 22937K(34176K)] 24340K(49536K), 0.0005405 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2020-07-18T19:49:16.308-0800: 5.104: [CMS-concurrent-mark-start]
2020-07-18T19:49:16.337-0800: 5.133: [CMS-concurrent-mark: 0.026/0.029 secs] [Times: user=0.08 sys=0.00, real=0.03 secs] 
2020-07-18T19:49:16.337-0800: 5.133: [CMS-concurrent-preclean-start]
2020-07-18T19:49:16.338-0800: 5.134: [CMS-concurrent-preclean: 0.001/0.001 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2020-07-18T19:49:16.338-0800: 5.135: [GC (CMS Final Remark) [YG occupancy: 2219 K (15360 K)]2020-07-18T19:49:16.338-0800: 5.135: [Rescan (parallel) , 0.0015166 secs]2020-07-18T19:49:16.340-0800: 5.136: [weak refs processing, 0.0017947 secs]2020-07-18T19:49:16.341-0800: 5.138: [class unloading, 0.0050460 secs]2020-07-18T19:49:16.347-0800: 5.143: [scrub symbol table, 0.0069897 secs]2020-07-18T19:49:16.354-0800: 5.150: [scrub string table, 0.0005613 secs][1 CMS-remark: 22937K(34176K)] 25157K(49536K), 0.0164867 secs] [Times: user=0.02 sys=0.00, real=0.02 secs] 
2020-07-18T19:49:16.355-0800: 5.151: [CMS-concurrent-sweep-start]
2020-07-18T19:49:16.365-0800: 5.162: [CMS-concurrent-sweep: 0.010/0.010 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
2020-07-18T19:49:16.365-0800: 5.162: [CMS-concurrent-reset-start]
2020-07-18T19:49:16.365-0800: 5.162: [CMS-concurrent-reset: 0.000/0.000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
```
Full GC日志含义：
<ul>
	<li>2020-07-18T19:33:17.566-0800：当前时间戳，由PrintGCDateStamps控制</li>
	<li>1.649：当前相对时间戳，表示应用启动多久后触发，由PrintGCTimeStamps控制</li>
	<li>[Full GC (Metadata GC Threshold) ：造成GC的原因，由PrintGCCause控制</li>
	<li>[Tenured: 4802K->6349K(34176K), 0.0206760 secs]
		<ul>
			<li>Tenured：使用不同垃圾收集器，这里的展示不同：
				<ul>
					<li>使用Serial Old收集器：显示Tenured</li>
					<li>使用Parallel Old收集器：显示ParOldGen</li>
					<li>使用CMS收集器：显示CMS</li>
				</ul>
			</li>
			<li>4802K：回收前，老年代使用的大小</li>
			<li>6349K：回收后，老年代使用的大小</li>
			<li>(34176K)：老年代总大小</li>
			<li>0.0206760：花费时间</li>
		</ul>
	</li>
	<li>16930K：回收前，堆使用的大小</li>
	<li>6349K：回收后，堆使用的大小</li>
	<li>49536K：堆的总大小</li>
	<li>[Metaspace: 20585K->20585K(1067008K)], 0.0207847 secs]：元空间的使用情况</li>
	<li>[Times: user=0.03 sys=0.00, real=0.03 secs] ：同新生代日志</li>
</ul>

###  3.2、JDK8中G1垃圾收集日志

- [G1垃圾收集器日志官方解读](https://blogs.oracle.com/poonam/understanding-g1-gc-logs)

```bash
# 这是一个年轻代GC，花费了0.721。下面的缩进，表示这行日志的子任务
2020-07-18T20:46:19.339-0800: 0.721: [GC pause (G1 Evacuation Pause) (young), 0.0056601 secs]
	# 并行任务，并行GC花费3.6毫秒，并行阶段有8个线程
   [Parallel Time: 3.6 ms, GC Workers: 8]
	  # 表示各个GC工作线程在应用启动多久(毫秒)后启动。
      # 同时还做了个统计，例如这些GC线程最早启动的那个线程在应用启动后847.9毫秒后启动等
      [GC Worker Start (ms): Min: 721.4, Avg: 721.4, Max: 721.4, Diff: 0.1]
	  # 表示各个GC工作线程扫描跟对象花费的时间的统计
      [Ext Root Scanning (ms): Min: 0.1, Avg: 0.6, Max: 3.2, Diff: 3.1, Sum: 5.2]
	  # 表示各个GC工作线程更新Remembered Sets花费的时间的统计
      # Remembered Sets是保存到堆中的区域的跟踪引用。设值方法线程持续改变对象图，自此引指向一个特定的区域。我们保存这些改变的跟踪信息到叫作Update Buffers的更新缓存中。Update RS子任务不能并发的处理更新缓存，更新一致所有区域的Remembered Sets
      [Update RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
	     # 表示每个GC工作线程处理的Update Buffers的数量统计
         [Processed Buffers: Min: 0, Avg: 0.0, Max: 0, Diff: 0, Sum: 0]
	  # 每个GC工作线程扫描Remembered Sets花费的时间
      # 一个区域的Remembered Sets包含指向这个区域的引用的相符合的卡片。这个阶段扫描这些卡片寻找指向所有这些区域的Collection Set的引用
      [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
	  # 扫描Code Root耗时统计。Code Root是JIT编译后的代码里引用了heap中的对象
      [Code Root Scanning (ms): Min: 0.0, Avg: 0.1, Max: 0.3, Diff: 0.3, Sum: 0.9]
	  # 拷贝存活对象到新的Region耗时统计
      [Object Copy (ms): Min: 0.0, Avg: 2.0, Max: 2.8, Diff: 2.8, Sum: 16.3]
	  # 各个GC工作线程完成任务后尝试中断GC线程到真正中断的耗时统计
      # 在某个GC线程中断之前，会检查其它线程的工作队列，如果发现依然有任务，会帮助处理，之后再中断
      [Termination (ms): Min: 0.0, Avg: 0.4, Max: 0.7, Diff: 0.7, Sum: 3.2]
	     # 尝试中断次数统计
         [Termination Attempts: Min: 1, Avg: 9.6, Max: 16, Diff: 15, Sum: 77]
	  # GC工作线程花费在其他工作上的时间统计
      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
	  # 各个GC工作线程花费的时间总和统计
      [GC Worker Total (ms): Min: 3.2, Avg: 3.2, Max: 3.2, Diff: 0.1, Sum: 25.6]
	  # 各个GC工作线程线程的结束时间，min|max分别表示第一个|最后一个线程的结束时间。
      [GC Worker End (ms): Min: 724.6, Avg: 724.6, Max: 724.6, Diff: 0.0]
   # 串行任务，修复GC期间code root指针改变的耗时
   [Code Root Fixup: 0.1 ms]
   # 串行任务，清除Code Root耗时
   [Code Root Purge: 0.0 ms]
   # 清除Card Table中的Dirty Card的耗时
   [Clear CT: 0.2 ms]
   # 其他任务
   [Other: 1.8 ms]
      # 为Collection Set选择区域所花费的时间
      [Choose CSet: 0.0 ms]
	  # 花费在处理引用对象上的时间
      [Ref Proc: 1.5 ms]
	  # 引用入队到ReferenceQueues花费的时间，可用-XX:+ParallelRefProcEnabled，并行处理这一步
      [Ref Enq: 0.0 ms]
      [Redirty Cards: 0.2 ms]
	  # 处理超大对象
      [Humongous Register: 0.0 ms]
      [Humongous Reclaim: 0.0 ms]
	  # 释放Collection Set数据结构花费的时间
      [Free CSet: 0.0 ms]
   # 各个区域的内存变化。
   # 24.0M：伊甸园当前占用24.0M
   # (24.0M)：伊甸园总大小(24.0M)
   # 0.0B：收集后，伊甸园占用将会变成0
   # (27.0M)：伊甸园的目标大小（如有需要，JVM可能会自动增加伊甸园大小）
   [Eden: 24.0M(24.0M)->0.0B(27.0M) Survivors: 0.0B->3072.0K Heap: 24.0M(50.0M)->2717.6K(50.0M)]
 # 用户耗时、系统耗时、实际耗时
 [Times: user=0.02 sys=0.00, real=0.01 secs] 
# 开始扫描初始标记阶段Survivor区的Root Region
2020-07-18T20:46:20.367-0800: 1.749: [GC concurrent-root-region-scan-start]
# 扫描完成
2020-07-18T20:46:20.369-0800: 1.751: [GC concurrent-root-region-scan-end, 0.0021777 secs]
# 2. 并发标记，标记线程数可用-XX:ConcGCThreads指定
2020-07-18T20:46:20.369-0800: 1.751: [GC concurrent-mark-start]
# 并发标记结束
2020-07-18T20:46:20.373-0800: 1.755: [GC concurrent-mark-end, 0.0039610 secs]
# 3. 最终标记(stop the world)
2020-07-18T20:46:20.373-0800: 1.756: [GC remark 2020-07-18T20:46:20.373-0800: 1.756: [Finalize Marking, 0.0003239 secs] 2020-07-18T20:46:20.374-0800: 1.756: [GC ref-proc, 0.0006212 secs] 2020-07-18T20:46:20.374-0800: 1.757: [Unloading, 0.0021714 secs], 0.0033509 secs]
 [Times: user=0.01 sys=0.00, real=0.00 secs] 
2020-07-18T20:46:20.377-0800: 1.759: [GC cleanup 12M->12M(50M), 0.0006202 secs]
 [Times: user=0.00 sys=0.01, real=0.00 secs] 
# 4. 筛选回收(stop the world)
# 没有存活对象的Old Region和Humongous Region将被释放和清空。
# 为了准备下次GC，在CSets中的Old Regions会根据他们的回收收益的大小排序。
2020-07-18T20:46:20.373-0800: 1.114: [GC cleanup 15M->14M(30M), 0.0006027 secs]
 [Times: user=0.00 sys=0.00, real=0.00 secs] 
# 并发清理开始
2020-07-18T20:46:20.373-0800: 1.115: [GC concurrent-cleanup-start]
# 并发清理结束
2020-07-18T20:46:20.373-0800: 1.115: [GC concurrent-cleanup-end, 0.0000133 secs]
```

### 3.3、JDK11垃圾收集日志

在JDK9之后，可以使用 `-Xlog:gc` 来查看GC日志

示例：`-Xmx30m -XX:+UseSerialGC -Xlog:gc*:file=./gc-xlog.log`
```bash
# 打印使用的垃圾收集器
# 0.016s指的是应用启动后过了多久
[0.016s][info][gc] Using Serial
# 打印内存概览，例如堆内存地址、堆内存总大小、压缩指针模式等
[0.016s][info][gc,heap,coops] Heap address: 0x00000007fe200000, size: 30 MB, Compressed Oops mode: Zero based, Oop shift amount: 3
# 发生了年轻代GC，原因是Allocation Failure
# GC(0)中的0，是对垃圾收集的次数统计，从0开始
[0.374s][info][gc,start     ] GC(0) Pause Young (Allocation Failure)
# 年轻代占用情况：回收前占用8192K，回收后占用1024K，年轻代总大小9216K
[0.379s][info][gc,heap      ] GC(0) DefNew: 8192K->1024K(9216K)
# 老年代占用情况：回收前、回收后、总大小
[0.379s][info][gc,heap      ] GC(0) Tenured: 0K->3147K(20480K)
# 元数据占用情况：回收前、回收后、总大小
[0.379s][info][gc,metaspace ] GC(0) Metaspace: 6042K->6042K(1056768K)
# 整个堆的占用情况：回收前、回收后、总大小
[0.379s][info][gc           ] GC(0) Pause Young (Allocation Failure) 8M->4M(29M) 5.180ms
# 用户耗时、系统耗时、实际耗时
[0.379s][info][gc,cpu       ] GC(0) User=0.00s Sys=0.00s Real=0.01s
```

### 3.4、GC日志可视化分析工具

- GCEasy：https://www.gceasy.io/

- GCViewer：https://github.com/chewiebug/GCViewer

- GCPlot：https://github.com/dmart28/gcplot

# 参考文章

* [深入理解java垃圾回收机制](http://www.cnblogs.com/sunniest/p/4575144.html)
* [Java GC 工作原理](http://www.hollischuang.com/archives/76)
* [YGC过程](https://www.jianshu.com/p/04eff13f3707)
* 《深入理解Java虚拟机-JVM高级特性与最佳实践[周志明]》
* [String.intern()导致的YGC](http://lovestblog.cn/blog/2016/11/06/string-intern/)
* [如何优化Java GC](https://crowhawk.github.io/2017/08/21/jvm_4/)
* [由「Metaspace容量不足触发CMS GC」从而引发的思考](https://www.jianshu.com/p/468fb4c5b28d)
* [频繁FullGC的案例](https://mp.weixin.qq.com/s/X-oOlXomjOyBe_8E4bWQLQ)
* [CMS垃圾收集器](https://mp.weixin.qq.com/s/-yqJa4dOyzLaK_tJ1x9E7w)

