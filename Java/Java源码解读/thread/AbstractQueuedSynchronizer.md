<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一.概述:](#%E4%B8%80%E6%A6%82%E8%BF%B0)
- [二.源码分析:](#%E4%BA%8C%E6%BA%90%E7%A0%81%E5%88%86%E6%9E%90)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

 * http://www.cnblogs.com/xrq730/p/7056614.html
 * http://blog.csdn.net/pfnie/article/category/7161421
 * http://www.cnblogs.com/zhanjindong/p/java-concurrent-package-aqs-overview.html
 * http://blog.csdn.net/pfnie/article/category/7161421


# 一.概述:
	签名:
	public abstract class AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer implements java.io.Serializable{}
#### 1.设计思想:AQS 是构建锁或者其他同步组件的基础框架,是JUC并发包中的核心基础组件
	(1).仅从 AQS 本身来说,它仅仅提供独占锁和共享锁两种方式, AQS 本身不存在所谓的公平和非公平锁.
	(2).AQS 基于模板模式设计, 其任何一个子类只能支持 AQS 当中的独占锁和共享锁中的一种,所以 AQS 没有抽象方法,所有方法都有默认实现
	(3).AQS 是继承自 AbstractOwnableSynchronizer(AOS),AOS 里面只有一个属性:exclusiveOwnerThread--用来标识当前占有锁的线程,加上该属性的get和set方法.
		==> ??为什么需要将持有锁的线程的标识向上抽取?
		AOS 有段注释如下:
			同步器是需要被线程互斥访问的,AOS 提供了一个基本概念,那就是创建锁时赋予一个对于这个锁的所有权.AOS 本身不会去管理或者使用这些信息.
			然而子类或者工具类在适当的时候会去维护这些信息来控制和监听访问控制权.
		AQS 是在1.5产生, 而 AOS 是在1.6之后才产生的.也就是说在 AQS 的整个声明过程中,都没有用到 AOS 中声明的属性或者方法,这些属性或者方法
		是在 AQS 的子类中才用的到.也就是在 1.6之后对子类进行增强.为什么不把 AOS 声明的属性直接放到 AQS 中?可能是因为 AQS 不需要这些属性,
		不对 AQS 做过多侵入.
	(4).AQS 核心是通过一个共享变量来同步状态,变量的状态由子类去维护,AQS 需要做的是:
		线程阻塞队列维护, 线程阻塞和唤醒.
#### 2.AQS 对外公开的方法不需要子类实现的:
	(1).AQS 仅仅只是提供独占锁和共享锁两种方式,但是每种方式都有响应中断和不响应中断的区别,所以说AQS锁的更细粒度的划分为:
		(1).acquire:不响应中断的独占锁
		(2).acquireInterruptibly: 响应中断的独占锁
		(3).acquireShared:不响应中断的共享锁
		(4).acquireSharedInterruptibly:响应中断的共享锁
	释放锁的方式只有两种:
		release:独占锁的释放
		releaseShared:共享锁的释放
	上述方法都是 final 的.
#### 3.AQS 是基于模板模式的实现:
    不过其模板模式的实现有些特别,整个类中没有抽象方法,取而代之的是需要子类去实现那些方法通过一个方法体.
	在上面的方法都调用了与之相对应的try方法.在这里需要注意的一点是,acquire和acquireInterruptibly在 AQS 中调用的是同一个try方法;
	acquireShared和acquireSharedInterruptibly也是调用相同的try方法,并且try方法在AQS中都提供了空实现.		
	抛出 UnsupportedOperationException 异常来让子类直到.作者暗示着子类应该去重写这些try方法,至于如何去重写try方法,完全是子类的自由
	AQS 一共有五处方法供子类实现:
	(1).tryAcquire:尝试在独占模式下acquire,方法应当查询在独占模式下对象的 state 字段是否允许 acquire, 如果允许,那么可以 acquire.
		方法通常在线程中执行 acquire 调用,如果方法失败了, acquire 方法会将线程加入等待队列(如果线程还没有加入等待队列)直到它被其他线程发出的信号释放.
	(2).tryRelease:尝试在独占模式下设置状态来反映对节点的释放,方法通常在线程执行释放节点时调用;
	(3).tryAcquireShared:尝试在共享模式下 acquire, 方法应当查询在共享模式下对象的 state 字段是否允许 acquire,如果允许,那么可以 acquire,
		方法通常在线程中执行 acquire 调用,如果方法失败了, acquire 方法会将线程加入等待队列(如果线程还没有加入等待队列)直到它被其他线程发出的信号释放.
	(4).tryReleaseShared:尝试在共享模式下设置状态来反映对节点的释放,方法通常在线程执行释放节点时调用;
	(5).isHeldExclusively:当前同步器是否在独占模式下被线程占用,一般该方法表示是否被当前线程独占.

# 二.源码分析:
#### 1.基本数据结构:Node
	1.1.关于 Node 需要注意点:
		(1).AQS 的等待队列是 CLH lock队列,CLH 经常用于自旋锁,AQS 中的CLH可以简单的理解为"等待锁的线程队列",队列中每个节点(线程)只需要等待其前驱节点释放锁;
		(2).每个节点持有一个 "status" 字段用于是否一条线程应当阻塞的追踪, 但是 state 字段并不保证加锁;
		(3).一条线程所在节点如果它处于队列头的下一个节点,那么它会尝试 acquire, 但是 acquire 并不保证成功,只是有权利去竞争
		(4).要进入队列,你只需要自动将它拼接在队列尾部即可;要从队列中移出,你只需要设置 header字段;
	1.2.Node 结构:
			+------+  prev +-----+       +-----+
		head|      | <---- |     | <---- |     |  tail
			+------+       +-----+       +-----+
			head-头指针
			tail-尾指针
			prev-指向前驱节点指针
			next-与prev相反,指向后置节点;
		关键不同就是next指针,因为 AQS 中线程不是一直在自旋的,可能会返回睡眠和唤醒,这就需要前继释放锁的时候通过 next 指针找到其后继将其唤醒.
		也就是 AQS 的等待队列中后继是被前继唤醒的.AQS 结合了自旋和睡眠/唤醒两种方法的优点.
	1.3.Node 主要代码:
		//标记当前结点是共享模式
		static final Node SHARED = new Node();
		//标记当前结点是独占模式
		static final Node EXCLUSIVE = null;
		//代表线程已经被取消
		static final int CANCELLED = 1;
		//代表后续节点需要唤醒
		static final int SIGNAL = -1;
		//代表线程在condition queue中，等待某一条件
		static final int CONDITION = -2;
		//代表后续结点会传播唤醒的操作，共享模式下起作用
		static final int PROPAGATE = -3;
		//结点的等待状态,用来控制线程的阻塞/唤醒,以及避免不必要的调用LockSupport的park/unpark方法,主要值上述四个
		volatile int waitStatus; 
		//拥有当前结点的线程。
		volatile Thread thread;

#### 2.不响应中断的独占锁:
	public final void acquire(int arg) {
        if (!tryAcquire(arg) && acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }
    (1).tryAcquire 由子类实现本身不会阻塞线程,如果返回 true, 则线程继续,如果返回 false 那么就加入阻塞队列阻塞线程,并等待前继结点释放锁
    (2).acquireQueued返回true,说明当前线程被中断唤醒后获取到锁,重置其interrupt status为true
    	acquireQueued方法中会保证忽视中断,只有tryAcquire成功了才返回
    (3).addWaiter 入队操作,并返回当前线程所在节点.	









