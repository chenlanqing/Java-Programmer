<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一.并发与多线程简介:](#%E4%B8%80%E5%B9%B6%E5%8F%91%E4%B8%8E%E5%A4%9A%E7%BA%BF%E7%A8%8B%E7%AE%80%E4%BB%8B)
  - [1.多线程优点:](#1%E5%A4%9A%E7%BA%BF%E7%A8%8B%E4%BC%98%E7%82%B9)
  - [2.多线程的代价:](#2%E5%A4%9A%E7%BA%BF%E7%A8%8B%E7%9A%84%E4%BB%A3%E4%BB%B7)
  - [3.并发编程模型:](#3%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B%E6%A8%A1%E5%9E%8B)
- [二.Java 多线程:](#%E4%BA%8Cjava-%E5%A4%9A%E7%BA%BF%E7%A8%8B)
  - [1.Java 线程类是继承自 java.lang.Thread 或其子类的,](#1java-%E7%BA%BF%E7%A8%8B%E7%B1%BB%E6%98%AF%E7%BB%A7%E6%89%BF%E8%87%AA-javalangthread-%E6%88%96%E5%85%B6%E5%AD%90%E7%B1%BB%E7%9A%84)
  - [2.线程的创建及状态变化](#2%E7%BA%BF%E7%A8%8B%E7%9A%84%E5%88%9B%E5%BB%BA%E5%8F%8A%E7%8A%B6%E6%80%81%E5%8F%98%E5%8C%96)
  - [3.竞态条件与临界区:](#3%E7%AB%9E%E6%80%81%E6%9D%A1%E4%BB%B6%E4%B8%8E%E4%B8%B4%E7%95%8C%E5%8C%BA)
  - [4.线程安全与共享资源:](#4%E7%BA%BF%E7%A8%8B%E5%AE%89%E5%85%A8%E4%B8%8E%E5%85%B1%E4%BA%AB%E8%B5%84%E6%BA%90)
  - [5.synchronized 关键字:](#5synchronized-%E5%85%B3%E9%94%AE%E5%AD%97)
  - [6.synchronized 同步块:](#6synchronized-%E5%90%8C%E6%AD%A5%E5%9D%97)
  - [7.synchronized 底层实现及 synchronized 锁优化.](#7synchronized-%E5%BA%95%E5%B1%82%E5%AE%9E%E7%8E%B0%E5%8F%8A-synchronized-%E9%94%81%E4%BC%98%E5%8C%96)
  - [8.volatile:](#8volatile)
  - [9.线程安全及不可变性:](#9%E7%BA%BF%E7%A8%8B%E5%AE%89%E5%85%A8%E5%8F%8A%E4%B8%8D%E5%8F%AF%E5%8F%98%E6%80%A7)
  - [10.线程通信:](#10%E7%BA%BF%E7%A8%8B%E9%80%9A%E4%BF%A1)
  - [11.ThreadLocal 类:](#11threadlocal-%E7%B1%BB)
  - [12.深入理解 ThreadLocal:](#12%E6%B7%B1%E5%85%A5%E7%90%86%E8%A7%A3-threadlocal)
  - [13.死锁:](#13%E6%AD%BB%E9%94%81)
  - [14.饥饿和公平:](#14%E9%A5%A5%E9%A5%BF%E5%92%8C%E5%85%AC%E5%B9%B3)
- [三.JUC(java.util.concurrent)包](#%E4%B8%89jucjavautilconcurrent%E5%8C%85)
  - [1.JUC 原子类:](#1juc-%E5%8E%9F%E5%AD%90%E7%B1%BB)
  - [2.锁的相关概念:](#2%E9%94%81%E7%9A%84%E7%9B%B8%E5%85%B3%E6%A6%82%E5%BF%B5)
  - [3.独占锁:](#3%E7%8B%AC%E5%8D%A0%E9%94%81)
  - [4.共享锁-ReentrantReadWriteLock 读写锁:](#4%E5%85%B1%E4%BA%AB%E9%94%81-reentrantreadwritelock-%E8%AF%BB%E5%86%99%E9%94%81)
  - [5.共享锁-闭锁:CountDownLatch:](#5%E5%85%B1%E4%BA%AB%E9%94%81-%E9%97%AD%E9%94%81countdownlatch)
  - [6.栅栏:CyclicBarrier:](#6%E6%A0%85%E6%A0%8Fcyclicbarrier)
  - [7.共享锁-信号量:Semaphore](#7%E5%85%B1%E4%BA%AB%E9%94%81-%E4%BF%A1%E5%8F%B7%E9%87%8Fsemaphore)
  - [8.Condition](#8condition)
  - [9.LockSupport:](#9locksupport)
  - [10.Callable & Future](#10callable--future)
  - [11.FutureTask:](#11futuretask)
  - [12.Fork/Join框架](#12forkjoin%E6%A1%86%E6%9E%B6)
  - [13.Exchanger](#13exchanger)
- [四.并发容器:](#%E5%9B%9B%E5%B9%B6%E5%8F%91%E5%AE%B9%E5%99%A8)
  - [1.并发容器类:](#1%E5%B9%B6%E5%8F%91%E5%AE%B9%E5%99%A8%E7%B1%BB)
  - [2.CopyOnWriteArrayList:](#2copyonwritearraylist)
  - [3.CopyOnWriteArraySet:(HashSet)](#3copyonwritearraysethashset)
  - [4.ConcurrentHashMap:](#4concurrenthashmap)
  - [5.ConcurrentSkipListMap:(TreeMap)](#5concurrentskiplistmaptreemap)
  - [6.ConcurrentSkipListSet: (TreeSet)](#6concurrentskiplistset-treeset)
  - [7.阻塞队列:](#7%E9%98%BB%E5%A1%9E%E9%98%9F%E5%88%97)
- [五.JUC 包核心与算法](#%E4%BA%94juc-%E5%8C%85%E6%A0%B8%E5%BF%83%E4%B8%8E%E7%AE%97%E6%B3%95)
  - [1.AQS:AbstractQueuedSynchronizer,抽象队列同步器](#1aqsabstractqueuedsynchronizer%E6%8A%BD%E8%B1%A1%E9%98%9F%E5%88%97%E5%90%8C%E6%AD%A5%E5%99%A8)
  - [2.CAS:Compare and Swap-比较与交换](#2cascompare-and-swap-%E6%AF%94%E8%BE%83%E4%B8%8E%E4%BA%A4%E6%8D%A2)
- [六.线程池](#%E5%85%AD%E7%BA%BF%E7%A8%8B%E6%B1%A0)
  - [1.线程池技术:](#1%E7%BA%BF%E7%A8%8B%E6%B1%A0%E6%8A%80%E6%9C%AF)
  - [2.重要类:](#2%E9%87%8D%E8%A6%81%E7%B1%BB)
  - [4.线程池配置:](#4%E7%BA%BF%E7%A8%8B%E6%B1%A0%E9%85%8D%E7%BD%AE)
- [七.多线程并发最佳实践](#%E4%B8%83%E5%A4%9A%E7%BA%BF%E7%A8%8B%E5%B9%B6%E5%8F%91%E6%9C%80%E4%BD%B3%E5%AE%9E%E8%B7%B5)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


 参考资料:
 * http://ifeve.com/java-concurrency-thread-directory/
 * [线程池](http://www.importnew.com/19011.html)
 * http://www.cnblogs.com/skywang12345/p/java_threads_category.html
 * http://blog.csdn.net/chenssy/article/category/6701493/2
 * [线程中断机制](http://ifeve.com/java-interrupt-mechanism/)

# 一.并发与多线程简介:
## 1.多线程优点:
	(1).资源利用率更好:文件读写操作
	(2).程序设计在某些情况下更简单:
	(3).程序响应更快:端口监听操作
## 2.多线程的代价:
	(1).设计更复杂:多线程共享数据时尤其需要注意
	(2).上下文切换的开销:
		CPU 会在一个上下文中执行一个线程,然后切换到另外一个上下文中执行另外一个线程
		上下文切换并不廉价。如果没有必要，应该减少上下文切换的发生.
		--> 上下文切换:从任务保存到再加载的过程就是一次上下文切换
			上下文切换过程中,CPU会停止处理当前运行的程序,并保存当前程序运行的具体位置以便之后继续运行;

	(3).增加资源消耗:如线程管理中消耗的资源
## 3.并发编程模型:
	Java 的并发采用的是共享内存模型
	3.1.并行工作者:传入的作业会被分配到不同的工作者上
		(1).并行工作者模型中，委派者(Delegator)将传入的作业分配给不同的工作者。
			每个工作者完成整个任务。工作者们并行运作在不同的线程上，甚至可能在不同的CPU上
		(2).优点:容易理解,只需要添加更多的工作者来提高系统的并行度
		(3).缺点:
			①.共享状态可能会很复杂
				在等待访问共享数据结构时，线程之间的互相等待将会丢失部分并行性。许多并发数据结构是阻塞的,
				意味着在任何一个时间只有一个或者很少的线程能够访问
			②.无状态的工作者:
				工作者无法在内部保存这个状态(但是每次需要的时候可以重读)称为无状态的
				共享状态能够被系统中得其他线程修改。所以工作者在每次需要的时候必须重读状态，以确保每次都能访问
				到最新的副本，不管共享状态是保存在内存中的还是在外部数据库中
			③.任务顺序是不确定的:使得很难在任何特定的时间点推断系统的状态.这也使得它也更难(如果不是不可能的话)
				保证一个作业在其他作业之前被执行
	3.2.流水线模式(反应器系统，或事件驱动系统):类似于工厂中生产线上的工人们那样组织工作者
		(1).每个工作者只负责作业中的部分工作。当完成了自己的这部分工作时工作者会将作业转发给下一个工作者。
			每个工作者在自己的线程中运行，并且不会和其他工作者共享状态。有时也被成为"无共享并行模型"
		(2).通常使用"非阻塞的 IO"来设计使用流水线并发模型的系统:
			非阻塞 IO 意味着:一旦某个工作者开始一个 IO 操作的时候,这个工作者不会一直等待 IO 操作的结束
			有了非阻塞 IO，就可以使用 IO 操作确定工作者之间的边界;
		(3).流水作业甚至也有可能被转发到超过一个工作者上并发处理:
		(4).反应器,事件驱动系统:用流水线并发模型的系统有时候也称为反应器系统或事件驱动系统
			①.系统内的工作者对系统内出现的事件做出反应,这些事件也有可能来自于外部世界或者发自其他工作者;
			②.流行的反应器/事件驱动平台:Vert.x,AKKa,Node.JS(JavaScript)
		(5).Actors 和 Channels:两种比较类似的流水线(或反应器/事件驱动)模型:
			①.在Actor模型中每个工作者被称为actor.Actor 之间可以直接异步地发送和处理消息。Actor 可以被
				用来实现一个或多个像前文描述的那样的作业处理流水线;
			②.在Channel模型中，工作者之间不直接进行通信。相反，它们在不同的通道中发布自己的消息(事件).
				其他工作者们可以在这些通道上监听消息，发送者无需知道谁在监听
		(6).优点:
			①.无需共享的状态:工作者之间无需共享状态，意味着实现的时候无需考虑所有因并发访问共享对象而产生的并发性问题;
			②.有状态的工作者:可以在内存中保存它们需要操作的数据，只需在最后将更改写回到外部存储系统;
			③.较好的硬件整合:单线程代码在整合底层硬件的时候往往具有更好的优势
				当能确定代码只在单线程模式下执行的时候，通常能够创建更优化的数据结构和算法
				单线程有状态的工作者能够在内存中缓存数据
			④.合理的作业顺序:作业的有序性使得它更容易地推出系统在某个特定时间点的状态
		(7).缺点:
			①.最大缺点:作业的执行往往分布到多个工作者上，并因此分布到项目中的多个类上。
				这样导致在追踪某个作业到底被什么代码执行时变得困难
			②.加大了代码编写的难度,有时会将工作者的代码写成回调处理的形式。若在代码中嵌入过多的回调处理，
				往往会出现所谓的回调地狱(callback hell)现象
	3.3.函数式并行(Functional Parallelism):
		(1).基本思想:是采用函数调用实现程序;
		(2).函数式并行里面最难的是确定需要并行的那个函数调用:
			跨CPU协调函数调用需要一定的开销。某个函数完成的工作单元需要达到某个大小以弥补这个开销。
			如果函数调用作用非常小，将它并行化可能比单线程、单CPU执行还慢
	3.4.并行与并发:
		(1).并行:表示两个线程同时做事情
		(2).表示一会做这个事情,一会做另一个事情,存在着调度.单核 CPU 不可能存在并行(微观上)

# 二.Java 多线程:
## 1.Java 线程类是继承自 java.lang.Thread 或其子类的,
	Thread thread = new Thread();
	thread.start();// 调用线程
## 2.线程的创建及状态变化
### 2.1.创建线程的方式:
	2.1.1创建 Thread 子类的一个实例并重写run方法:run方法会在调用start()方法之后被执行
	(1).方式1:继承类
		class MyThread extends Thread{
			public void run(){
				System.out.println("my thread is running");
			}
		}
		可用如下方式创建并运行上述thread的子类:
		MyThread myThread = new MyThread();
		myTread.start();
	(2).方式2:创建一个Thread的匿名子类
		Thread thread = new Thread(){
			public void run(){
				System.out.println("my thread is running");
			}
		};
		thread.start();
	2.1.2.创建类的时候实现 Runnable 接口:
		(1).新建一个实现了 java.lang.Runnable 接口的类的实例
			class MyRunnable implements Runnable{
				public void run(){
					System.out.println("my thread is running");
				}
			}
			为了使线程能够执行run()方法，需要在 Thread 类的构造函数中传入 MyRunnable 的实例对象
			Thread thread = new Thread(new MyRunnable());
			thread.start();
		(2).创建一个实现了 Runnable 接口的匿名类:
			Runnable myRunnable = new Runnable(){
				public void run(){
					System.out.println("Runnable running");
				}
			}
			Thread thread = new Thread(myRunnable);
			thread.start();
	2.1.3.实现 Callable 接口,实现call()方法,使用 FutureTask 类来包装 Callable 对象,FutureTask
		对象封装了该 Callable 对象的call()方法的返回值;使用 FutureTask 对象作为 Thread 对象的
		target创建并启动新线程
		执行 Callable 方式,需要 FutureTask 实现类的支持,用于接收运算结果
		FutureTask<Integer> task = new FutureTask<>(new MyCallable());// FutureTask 也有闭锁的功能
		new Thread(task).start();
### 2.3.创建 Thread 子类还是实现 Runnable 接口?
	实现 Runnable 接口,线程池可以有效的管理实现了 Runnable 接口的线程
	如果多个线程正在运行中,如果某个线程调用 System.exit()指示终结程序，那么全部的线程都会结束执行
### 2.4.常见错误问题:
	调用run()方法而非start()方法
### 2.5.线程名称:
	当创建一个线程的时候，可以给线程起一个名字
	MyRunnable runnable = new MyRunnable();
	Thread thread = new Thread(runnable, "New Thread");
	thread.start();
	System.out.println(thread.getName());
	也可以通过:
	Thread.currentThread()获取当前线程的引用
### 2.6.Thread 的部分属性:
	(1).ID: 每个线程的独特标识。
	(2).Name: 线程的名称。
	(3).Priority: 线程对象的优先级。优先级别在1-10之间，1是最低级，10是最高级。
		不建议改变它们的优先级，但是你想的话也是可以的。
	(4).Status: 线程的状态。在Java中，线程只能有这6种中的一种状态：
		new{}, runnable, blocked, waiting, time waiting, 或 terminated.
### 2.7.线程的中断与停止,暂停:
	2.7.1.线程中断:
		Java 提供中断机制来通知线程表明我们想要结束它.中断机制的特性是线程需要检查是否被中断,
		而且还可以决定是否响应结束的请求
	2.7.2.线程停止:
		2.7.2.1.线程停止的方法:
			Java并没有提供安全停止线程的方法
			①.使用退出标志,使线程正常退出,也就是当run方法完成后线程终止;
			②.使用stop方法强行终止线程,但不推荐使用,因为stop和suspend及resume一样,是过期的方法,使用会造成不可预料的结果
			③.使用interrupt 方法中断线程;
		2.7.2.2.interrup 终止线程:
			(1).判断线程是否是停止状态
				①.interruputed:测试当前线程是否有已经中断,执行后具有将状态标志清除为 false 的功能
					如果连续调用两次该方法,第二次调用则返回 false
				②.isInterrupteed:测试线程Thread对象是否已经中断,但不清除状态标志;
		2.7.2.3.stop()方法:过期方法
			(1).暴力停止线程的方法;
			(2).调用 stop 方法时会抛出 java.lang.ThreadDeath 异常,通常情况下,该异常不需要显示捕获;
			(3).强制停止线程,可能对锁定的对象进行了"解锁",导致数据得不到同步处理,出现数据不一致的情况
		2.7.2.4.线程停止:在代码中增加异常处理
		2.7.2.5.使用 return 停止线程:将方法 interrupt() 和 return 结合使用
	2.7.3.暂停线程:可以使用 suspend()方法暂停线程,使用resume() 方法恢复线程的执行
		这两个方法的缺点:
		(1).使用这两个方法时,如果使用不当极易造成公共的同步对象的独占,使得其他线程无法访问公共同步对象
		(2).数据的不同步:容易出现因为线程的暂停而导致数据不同步的情况
	2.7.4.线程中断原理:
		(1).Java线程中断机制是一种协作机制,也就是说通过中断并不能直接停止另一个线程,需要被中断的线程自己处理中断.
		(2).中断模型:
			每个线程对象里都有一个boolean类型的标识,代表着是否有中断请求(该请求可以来自所有线程,包括被中断的线程本身),
			Thread提供了几个方法来操作中断状态:
			①.public static boolean interrupted():测试当前线程是否中断,线程的中断状态由该方法清除.
				如果连续两次调用该方法,则第二次调用将返回 false
			②.public boolean isInterrupted():测试线程是否已经中断.线程的中断状态不受该方法的影响;
			③.public void interrupt():中断线程,唯一能将中断状态设置为true的方法
		(3).中断处理:被中断线程只需在合适的时候处理即可，如果没有合适的时间点，甚至可以不处理
			* https://www.ibm.com/developerworks/cn/java/j-jtp05236.html
			①.中断状态管理:一般来说当可能阻塞的方法声明中有抛出InterruptedException则暗示该方法是可中断的;
				如果程序捕获到这些可中断的阻塞方法抛出的InterruptedException或检测到中断后,可以按照如下原则处理:
				* 如果遇到的是可中断的阻塞方法抛出InterruptedException,可以继续向方法调用栈的上层抛出该异常,
				  如果是检测到中断,则可清除中断状态并抛出InterruptedException,使当前方法也成为一个可中断的方法
				* 若有时候不太方便在方法上抛出InterruptedException,比如要实现的某个接口中的方法签名上没有
				  throws InterruptedException，这时就可以捕获可中断方法的InterruptedException并通过
				  Thread.currentThread.interrupt()来重新设置中断状态。如果是检测并清除了中断状态，亦是如此.
			②.中断响应:根据实际情况而定
				有些程序可能一检测到中断就立马将线程终止,有些可能是退出当前执行的任务,继续执行下一个任务;
				如做一些事务回滚操作,一些清理工作,一些补偿操作
		(4).中断的使用:
			* 点击某个桌面应用中的取消按钮时；
			* 某个操作超过了一定的执行时间限制需要中止时；
			* 多个线程做相同的事情，只要一个线程成功其它线程都可以取消时；
			* 一组线程中的一个或多个出现错误导致整组都无法继续时；
			* 当一个应用或服务需要停止时

### 2.8.线程的睡眠:
	Thread.sleep()
	TimeUnit.SECONDS.sleep();
### 2.9.等待线程的终结:
	Thread.join():当前线程调用某个线程的这个方法时，它会暂停当前线程，直到被调用线程执行完成
	Thread.join(long miseconds);	这方法让调用线程等待特定的毫秒数。
	Thread.join(long milliseconds, long nanos)第二个版本的join方法和第一个很像,只不过它接收一个毫秒数和一个纳秒数
### 2.10.异常处理:
	当一个非检查异常被抛出，默认的行为是在控制台写下stack trace并退出程序
	(1).必须实现一个类来处理非检查异常。这个类必须实现 UncaughtExceptionHandler 接口并实现在接口
		内已声明的 uncaughtException() 方法:
		public class ExceptionHandler implements UncaughtExceptionHandler{
			public void uncaughtException(Thread t, Throwable e){
				System.out.printf("An exception has been captured\n");
				System.out.printf("Thread: %s\n",t.getId());
				System.out.printf("Exception: %s: %s\n",e.getClass().getName(),e.getMessage());
				System.out.printf("Stack Trace: \n");
				e.printStackTrace(System.out);
				System.out.printf("Thread status: %s\n",t.getState());
			}
		}
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
	(2).当在一个线程里抛出一个异常，但是这个异常没有被捕获(这肯定是非检查异常):
		JVM 检查线程的相关方法是否有设置一个未捕捉异常的处理者.
		如果有,JVM 使用 Thread 对象和 Exception 作为参数调用此方法;
		如果没有定义,那么 JVM 会寻找默认非捕捉异常 handle
		如果线程没有捕捉未捕获异常的处理者,那么 JVM会 把异常的 stack trace 写入操控台并结束任务;
### 2.11.本地线程变量:
	ThreadLocal(详情查看第 9 点)
	(1).本地线程变量为每个使用这些变量的线程储存属性值。可以用 get() 方法读取值和使用 set() 方法改变值
### 2.12.线程组:可以把线程分组
	Java 提供 ThreadGroup 类来组织线程.
	ThreadGroup 对象可以由 Thread 对象组成和由另外的 ThreadGroup 对象组成,生成线程树结构
### 2.13.用线程工厂创建线程
	线程对象工厂 ThreadFactory
### 2.14.join()和yield()方法:
	2.14.1.join()方法:
		(1).等待该线程终止,指的是主线程等待子线程的终止,子线程调用了join()方法后面的代码,
			只有等到子线程结束了才能执行;
			让调用该方法的thread完成 run 方法的里面的东西后,在执行 join 方法后面的代码
			当前运行着的线程将阻塞直到这个线程实例完成了执行
		(2).join 具有使线程排队运行的作用.join 与 synchronized 区别:
			join 内部是 wait 方法进行等待,而 synchronized 关键字使用的是"对象监视器"原理作为同步
		(3).在执行 join 的过程中,如果当前线程对象被中断,则当前线程出现异常;
		(3).join(long) 内部是使用 wait(long)方法来实现的,所以join(long)方法具有释放锁的特点,
			而 sleep(long)不释放锁的
	2.14.2.yield()方法:
		(1).使当前线程从执行状态(运行状态)变为可执行状态(就绪状态),调用yield的时候锁并没有被释放
			放弃当前的CPU资源,将它让给其他的任务去占用CPU执行时间,放弃的时间不确定
			将CPU让给其他资源导致速度变慢,一般是把机会给到线程池拥有相同优先级的线程
	2.14.3.两者的区别:
		(1).join 是 final 的实例方法,yield是原生静态方法
### 2.15.线程的优先级:
	(1).线程的优先级具有继承性,比如A线程启动B线程,则B线程的优先级与A是一样的;
	(2).优先级具有规则性:
		线程的优先级与代码执行顺序无关,CPU 尽量将执行资源让给优先级比较高的线程
		高优先级的线程总是大部分先执行完的,但不带表高优先级的线程全部不执行完;
	(3).优先级具有随机性:
		也就是优先级较高的线程不一定每次都先执行完
		不要把线程的优先级与运行结果的顺序作为衡量的标准,线程优先级与打印顺序无关
### 2.16.守护线程:守护线程优先级非常低,通常在程序里没有其他线程运行时才会执行;
	当守护线程是唯一在运行的线程时,JVM 会结束守护线程并终止程序;
	(1).守护线程通常用于在同一程序里给普通线程(也叫使用者线程)提供服务
		它们通常无限循环的等待服务请求或执行线程任务
		JAVA 中最典型的这种类型代表就是垃圾回收器
		public class Daemon extends Thread{
			public Daemon(){
				setDaemon(true);//在这个构造函数,用setDaemon() 方法让此线程成为守护线程
			}
		}
	(2).只能在start() 方法之前可以调用 setDaemon() 方法。一旦线程运行了，就不能修改守护状态。
		可以使用 isDaemon() 方法来检查线程是否是守护线程(方法返回 true) 或者是使用者线程 (方法返回 false)
	(3).典型的守护线程是垃圾回收线程,当进程中没有非守护线程时,则垃圾回收线程也就没有存在的必要了
		守护线程的作用是为其他线程的运行提供便利服务,最典型的应用:GC
		当只有守护线程运行时，JVM会自动退出
### 2.17.线程的生命周期:
![image](https://github.com/chenlanqing/learningNote/blob/master/Java/JavaSE/多线程/image/线程生命周期.png)

	(1).新建态(New):通过线程的创建方式创建线程后,进入新建态态;
	(2).就绪(Runnable):调用 Tread 的start 方法,就会为线程分配私有的方法栈,程序计数器资源,如果得到CPU资源,线程就转为运行状态.
	(3).运行(Running):就绪态得到CPU资源后转为运行态,执行run方法.在调用 yield 方法后,线程由运行转为就绪
	(4).阻塞(Bolcking):线程因为某种原因放弃CPU使用权,暂时停止运行.直到线程进入就绪状态,才有机会转到运行状态.阻塞的情况分三种:
		A.等待阻塞 -- 通过调用线程的wait()方法，让线程等待某工作的完成。
		B.同步阻塞 -- 线程在获取synchronized同步锁失败(因为锁被其它线程所占用)，它会进入同步阻塞状态。
		C.其他阻塞 -- 通过调用线程的sleep()或join()或发出了I/O请求时，线程会进入到阻塞状态.
					当sleep()状态超时、join()等待线程终止或者超时、或者I/O处理完毕时，线程重新转入就绪状态
	(5).死亡状态(Dead):线程执行完了或者因异常退出了run()方法,该线程结束生命周期
## 3.竞态条件与临界区:
	3.1.在同一程序中运行多个线程本身不会导致问题,问题在于多个线程访问了相同的资源:
		如果多个线程对这些相同资源进行了"写操作"才会引发线程安全问题;
	3.2.竞态条件:
		当两个线程竞争同一资源时，如果对资源的访问顺序敏感，就称存在竞态条件
	3.3.临界区:
		致竞态条件发生的代码区称作临界区
## 4.线程安全与共享资源:
	4.1.允许被多个线程同时执行的代码称作线程安全的代码,线程安全的代码不包含竞态条件
	4.2.局部变量:存储在线程自己的栈中.也就是说,局部变量永远也不会被多个线程共享
		基础类型的局部变量是线程安全的
	4.3.局部的对象引用:
		(1).尽管引用本身没有被共享,但引用所指的对象并没有存储在线程的栈内.所有的对象都存在共享堆中
		(2).如果在某个方法中创建的对象不会逃逸出(即该对象不会被其它方法获得,也不会被非局部变量引用到)该方法,
			那么它就是线程安全的
		(3).实际上,哪怕将这个对象作为参数传给其它方法,只要别的线程获取不到这个对象,那它仍是线程安全的:
			public void someMethod(){			  
			  LocalObject localObject = new LocalObject();
			  localObject.callMethod();
			  method2(localObject);
			}
			public void method2(LocalObject localObject){
			  localObject.setValue("value");
			}
	4.4.对象成员:(实例变量)
		(1).对象成员存储在堆上.如果两个线程同时更新同一个对象的同一个成员,那这个代码就不是线程安全的;
		(2).案例:
			public class NotThreadSafe{
			    StringBuilder builder = new StringBuilder();			    
			    public add(String text){
			        this.builder.append(text);
			    }
			}
			如果两个线程同时调用同一个NotThreadSafe实例上的add()方法，就会有竞态条件问题
				NotThreadSafe sharedInstance = new NotThreadSafe();
				new Thread(new MyRunnable(sharedInstance)).start(); // 两个MyRunnable共享了同一个NotThreadSafe对象
				new Thread(new MyRunnable(sharedInstance)).start();
				public class MyRunnable implements Runnable{
				  NotThreadSafe instance = null;			  
				  public MyRunnable(NotThreadSafe instance){
				    this.instance = instance;
				  }
				  public void run(){
				    this.instance.add("some text");
				  }
				}
			new Thread(new MyRunnable(new NotThreadSafe())).start();
			new Thread(new MyRunnable(new NotThreadSafe())).start();
			现在两个线程都有自己单独的NotThreadSafe对象,调用add()方法时就会互不干扰,再也不会有竞态条件问题了
	4.5.线程控制逃逸规则:可以帮助你判断代码中对某些资源的访问是否是线程安全的
		(1).规则:如果一个资源的创建,使用,销毁都在同一个线程内完成,且永远不会脱离该线程的控制,则该资源的使用就是线程安全的.
		(2).即使对象本身线程安全,但如果该对象中包含其他资源(文件，数据库连接)整个应用也许就不再是线程安全的了;
## 5.synchronized 关键字:
	synchronized 取得的锁是都是对象锁,而不是把一段代码或方法当作锁,哪个线程先执行代 synchronized
	关键字的方法,哪个线程就持有该方法所属对象的锁,其他线程只能呈等待状态
	5.1.synchronized 方法与锁对象:
		(1).只有共享资源的读写访问才需要同步化实现;
		(2).A 线程先持有 object 对象的 Lock 锁,B 线程可以以异步化的方式调用 object 对象中的非
			synchronized 类型的方法;
		(3).A 线程先持有 object 对象的 Lock 锁,B 线程如果在这时调用 object 对象中的 synchronized 类型
			的方法则需要等待,即同步
	5.2.脏读:读取实例变量时,此值已经被其他线程修改过的,脏读可以通过 synchronized 关键字解决
		(1).当 A 线程调用 anyObject 对象加入 synchronized 关键字的 X 方法时, A 线程就获得了 X 方法锁,
		准确的来说是获得了对象的锁,所以其他线程必须等待 A 线程执行完毕后才可以调用 X 方法,但 B 线程
		可以随意调用其他非 synchronized 的方法;
		(2).当 A 线程调用 anyObject 对象中对象加入 synchronized 关键字的 X 方法时, A 线程就获得了 X 方法
		所在对象的锁,所以其他线程必须等待 A 线程执行完毕后才可以调用 X 方法,而 B 线程如果调用声明了
		synchronized 关键字的非 X 方法时,必须等待 A 线程将 X 方法执行完毕,也就释放对象锁后才可以调用
	5.3.synchronized 锁重入:synchronized 关键字拥有锁重入功能,也就是在使用 synchronized 时,当一个线程
		得到一个对象的锁之后,再次请求此对象锁时可以再次得到该对象的锁.在一个 synchronized 方法/块内
		部调用本类的其他 synchronized 方法/块时,是永远可以得到锁的;
		可重入锁:自己可以再次获得自己的内部锁,也支持父子类继承的环境
		子类可以通过可重入锁调用父类的同步方法
	5.4.出现异常时,锁自动释放:
		线程a调用 synchronized 方法时出现异常,会自动释放锁,线程b进入方法正常执行;
	5.5.同步不具有继承性:
		当子类重写父类的同步方法时,如果子类的重写方法不加入同步标志的化,一样不具备同不性
	5.6.静态方法同步:
		静态方法的同步是指同步在该方法所在的类对象上。因为在Java虚拟机中一个类只能对应一个类对象，
		所以同时只允许一个线程执行同一个类中的静态同步方法
	5.7.synchronized 与 Lock 的区别
		5.7.1.区别:
			(1).synchronized 是Java的一个关键字,其是在JVM层面上实现的,如果线程执行时发生异常,JVM 会自动释放锁.
				因此不会导致死锁现象发生;
				Lock 是接口,通过代码实现的,在发生异常时,如果没有主动通过unLock()去释放锁,则很可能造成死锁现象,因此使用 Lock
				时需要在finally块中释放锁;
			(2).Lock 可以让等待锁的线程响应中断,而 synchronized 使用时等待的线程会一直等待下去,不能响应中断.
			(3).通过 Lock 可以知道有没有成功获取锁,而 synchronized 不行.
			(4).在资源竞争不是很激烈的情况下, synchronized 的性能要优于 Lock,但是在资源竞争很激烈的情况下,synchronized
				性能会下降几十倍,但是 Lock 是保持常态的.
			(5).在 JDK1.5 之后 synchronized 作了很多优化,在性能上已经有很大提升.
				如:自旋锁、锁消除、锁粗化、轻量级锁、偏向锁
			(6).synchronized 和 ReentrantLock 都是可重入锁;
			(7).公平锁:即尽量以请求锁的顺序来获取锁
				synchronized 是非公平锁,无法保证等待的线程获取锁的顺序;
				ReentrantLock和ReentrantReadWriteLock,默认情况下是非公平锁,但是可以设置为 公平锁
		5.7.2.Lock 适用场景:
			(1).某个线程在等待一个锁的控制权的这段时间需要中断;
			(2).需要分开处理一些wait-notify,ReentrantLock 里面的 Condition应用,能够控制notify哪个线程,锁可以绑定多个条件
			(3).具有公平锁功能,每个到来的线程都将排队等候
## 6.synchronized 同步块:
	synchronized 声明方法在某些情况下是有弊端的,
	6.1.实例方法中的同步块:不需要同步整个方法，而是同步方法中的一部分
		在非同步的Java方法中的同步块的例子如下所示:
		public  void add(int value){
			synchronized(this){
				this.value = value;
			}
		}
		①.注意Java同步块构造器用括号将对象括起来,在同步构造器中用括号括起来的对象叫做监视器对象
		(4).静态方法中的同步块:这些方法同步在该方法所属的类对象上
			public class MyClass {
			    public static synchronized void log1(String msg1, String msg2){
			       log.writeln(msg1);
			       log.writeln(msg2);
			    }
			    public static void log2(String msg1, String msg2){
			       synchronized(MyClass.class){
			          log.writeln(msg1);
			          log.writeln(msg2);
			       }
			    }
			}
	6.2.同步代码块中,不在 synchronized 块中的是异步执行的,在 synchronized 块中就是同步执行的;
	6.3.在使用 synchronized(this) 代码块时(即访问的是实例方法)需要注意:
		当一个线程访问 object 的 一个 synchronized(this) 代码块时,其他线程对同一个 object 中所有
		其他 synchronized(this) 同步代码块的访问将被阻塞,说明 synchronized 使用的"对象监视器"是一个;
		和 synchronized 方法一样,synchronized(this) 代码块也是锁定当前对象的
	6.4.多个线程调用同一个对象中的不同名称的 synchronized 同步方法或同步代码块时,调用的效果就是按
		顺序执行,也是同步的,阻塞的;	说明 synchronized 同步方法或同步代码块 两种作用:
		(1).对其他 synchronized 同步方法或同步代码块调用呈阻塞状态;
		(2).同一个时间只要一个线程看执行 synchronized 同步方法或同步代码块中的代码
		6.4.1.Java 支持任意对象作为对象监视器来实现同步的功能;
			锁定非 this 对象具有:如果一个类中有很多个 synchronized 方法,虽然实现了同步,但会收到阻塞,影响
			运行效率;但是如果使用同步代码块锁非this对象,则代码块中的程序与同步方法是异步的,不与其他锁
			this同步方法争抢this锁,提高运行效率;
		6.4.2.同步代码块放在非同步方法中声明,并不能保证调用方法的线程的执行是同步/顺序性的,也就是线程调
			用方法的顺序是无序的,虽然在同步块中执行的顺序是同步的,这样容易出现"脏读"问题;
	6.5.3个结论:
		synchronized(非this对象的x) 是将x对象本身作为"对象监视器",这样得到以下3个结论:
		(1).当多个线程同时执行 synchronized(x){}同步代码块时是同步的;
		(2).当其他线程执行x对象中 synchronized 同步方法时是同步的;
		(3).当其他线程执行x对象方法里面 synchronized(this)代码块是也呈现同步效果;
		但是需要注意的是:如果其他线程调用不加 synchronized 关键字的方法时,还是异步调用;

	6.6.静态同步 synchronized 方法与 synchronized(Class.class)代码块:
		(1).如果 static 加在 synchronized 同步方法上,那么对当前的*.java文件对应的 Class 类进行加锁
		(2).如果是 synchronized(Class.class)代码块,则效果是是一样的,也是对整个类加锁;
		将 synchronized(string) 同步块与 String 联合使用时,需要注意字符串常量池带来的一些意外情况;
	6.7.

## 7.synchronized 底层实现及 synchronized 锁优化.

	* http://blog.csdn.net/shandian000/article/details/54927876
	* http://www.cnblogs.com/paddix/p/5367116.html
	* http://www.cnblogs.com/javaminer/p/3889023.html
	* http://cmsblogs.com/?p=2071
	可以通过反编译字节码 -->javap -c SyncDemo.class
	synchronized 的优化借鉴了锁的CAS操作
	7.1.同步代码块的实现:
		同步代码块是使用 monitorenter 和 monitorexit 指令来实现的.
		7.1.1.monitorenter:每个对象都有一个监视器锁(monitor),当monitor被占用时就会处于锁定状态,线程执行monitorenter指令时尝试获取
			monitor的所有权.
			(1).如果 monitor 的进入数为 0,则该线程进入monitor,然后将进入数设置为 1,该线程为 monitor的所有者.
			(2).如果线程已经占用该monitor,只是重新进入,则monitor的进入数加 1;
			(3).如果其他线程已经占用了monitor,则该线程进入阻塞状态,直到 monitor 的进入数为 0,再尝试重新获取 monitord的所有权.
		7.1.2.monitorexit:执行该指令的线程必须是objectref所对应的monitor的持有者.
			指令执行时,monitor的进入数减1,如果减1后为0,那么线程退出monitor,不再持有monitor,
		==> synchronized 代码块的语义底层是通过一个monitor的对象来完成,其实wait/notify等方法也依赖于monitor对象,
			这就是为什么只有在同步的块或者方法中才能调用wait/notify等方法,否则会抛出java.lang.IllegalMonitorStateException 的异常的原因
		7.1.3.源码分析:(https://www.jianshu.com/p/c5058b6fe8e5)
	7.2.同步方法的实现:
		方法的同步并没有通过指令monitorenter和monitorexit来完成,不过相对于普通方法,其常量池中多了 ACC_SYNCHRONIZED 标示符.
		JVM 就是根据该标示符来实现方法的同步:
		当方法调用时会检查方法的 ACC_SYNCHRONIZED 访问标示是否被设置,如果设置了,执行线程将先获取monitor,获取成功后,执行方法体.
		方法值完释放monitor,在方法执行期间,其他线程无法再获得同一个monitor对象.
	7.3.重量级锁:synchronized 是通过对象内部的一个叫做监视器锁(monitor)来实现的.但是监视器锁本质又是依赖底层操作系统的
		Mutex Lock 来实现的,而操作系统实现线程间的切换成本非常高,状态之间的转换需要相对较长的时间.
		依赖于底层操作系统的 Mutex Lock 所实现的锁我们称之为"重量级锁"
	7.4.轻量级锁:
		// http://www.cnblogs.com/paddix/p/5405678.html
		7.4.1.锁的状态总共有四种:无锁状态、偏向锁、轻量级锁和重量级锁;随着锁的竞争,锁可以从偏向锁升级到轻量级锁,再升级到
			重量级锁(锁的升级是单向的,也就是说只能从低到高,不会出现锁的降级).JDK6 中是默认开启偏向锁和轻量级锁的,也可以通过
			 -XX:UseBiasedLocking 来禁用偏向锁.锁的状态保存在对象的头文件中.
			轻量级锁是相对于使用操作系统互斥量来实现的传统锁而言的.另外轻量级锁并不是用来替代重量级锁的.
			轻量级锁适应的场景是线程交替执行同步块的情况,如果存在同一时间访问同一锁的情况,会导致轻量级锁膨胀为重量级锁.
		7.4.2.轻量级锁的加锁过程:
			(1).在代码块进入同步块的时候,如果同步对象锁状态为无锁状态(锁标志位为"01"状态,是否偏向锁为"0").,虚拟机首先将
			在当前线程的栈桢中建立一个名为锁记录(Lock Record)的空间,用于存储锁对象目前的 Mark Word 的拷贝;
			(2).拷贝对象头中的 Mark Word 复制到锁记录中;
			(3).拷贝成功后,虚拟机将使用CAS操作尝试将对的 Mark Word 更新为指向 Lock Record 的指针,并将 Lock Record 里的owner
				指针指向object mark word.如果执行成功,则执行步骤(4),如果失败则执行步骤(5).
			(4).如果更新成功,那么这个线程就拥有了该对象的锁,并且对象 Mark Word 的锁标志位设置为"00",即表示对象处于轻量级锁定状态.
			(5).如果更新失败,虚拟机首先会检查对象的 Mark Word 是否指向当前线程的栈帧,如果是就说明当前线程已经拥有了这个对象的锁,
				那就可以直接进入同步块继续执行.否则说明多个线程竞争锁,轻量级锁就要膨胀为重量级锁,锁标志的状态值变为“10”,
				Mark Word 中存储的就是指向重量级锁（互斥量）的指针,后面等待锁的线程也要进入阻塞状态.
				而当前线程便尝试使用自旋来获取锁，自旋就是为了不让线程阻塞，而采用循环去获取锁的过程
		7.4.3.轻量级锁解锁过程:
			(1).通过CAS操作尝试把线程中复制的 Displaced Mark Word 对象替换当前的 Mark Word。
			(2).如果替换成功,整个同步过程就完成了。
			(3).如果替换失败,说明有其他线程尝试过获取该锁(此时锁已膨胀)那就要在释放锁的同时,唤醒被挂起的线程.
	7.5.偏向锁:
		引入偏向锁是为了在无多线程环境的情况下尽可能减少不必要的轻量级锁执行路径.因为轻量级锁的获取及释放依赖多次CAS原子指令,
		而偏向锁只需要在置换ThreadID的时候依赖一次CAS原子指令,偏向锁是在只有一个线程执行同步块时进一步提高性能.
		7.5.1.偏向锁的获取过程:
			(1).访问 Mark Word 中偏向锁的标识是否设置为1,锁标志位是否为01--确认可偏向状态.
			(2).如果为可偏向状态,则测试线程ID是否指向当前线程,如果是进入步骤(5),否则进入步骤(3).
			(3).如果线程ID并未指向当前线程,则通过 CAS 操作竞争锁.如果竞争成功,则将 Mark Word 中线程ID设置为当前线程ID,
				然后值(5);如果竞争失败,执行(4).
			(4).如果 CAS 获取偏向锁失败,则表示有竞争,当到达全局安全点(safepoint)时获得偏向锁的线程被挂起,偏向锁升级为轻量级锁,
				然后被阻塞在安全点的线程继续往下执行同步代码;
			(5).执行同步代码;
		7.5.2.偏向锁释放过程:
			偏向锁只有遇到其他线程尝试竞争偏向锁时,持有偏向锁的线程才会释放锁,线程不会主动去释放偏向锁;
	7.6.其他优化:
		7.6.1.适应性自旋(Adaptive Spinning):
			(1).从轻量级锁的获取过程中知道,当前线程在获取轻量级锁的过程中执行 CAS 操作失败时,
			是要通过自旋来获取重量级锁.问题是:自旋是需要消耗CPU的,如果一直获取不到锁,那么该线程就一直处在自旋状态.
			解决该问题最简单的方法是指定自旋的次数.但是JDK采用了更合适的方法-适应性自旋.
			简单来说就是如果自旋成功了,那么下次自旋的次数会更多,如果自旋失败了,则自旋的次数就会减少.
			(2).自旋锁的实现:
```java
		public class MyWaitNotify3{
			MonitorObject myMonitorObject = new MonitorObject();
			boolean wasSignalled = false;

			public void doWait(){
					synchronized(myMonitorObject){
							while(!wasSignalled){
									try{
											myMonitorObject.wait();
										} catch(InterruptedException e){...}
							}
							//clear signal and continue running.
							wasSignalled = false;
					}
			}

			public void doNotify(){
					synchronized(myMonitorObject){
							wasSignalled = true;
							myMonitorObject.notify();
					}
			}
		}
```
		(2).锁粗化(Lock Coarsening):是将多次连接在一起的加锁和解锁操作合并在一起,将多个连续的锁扩展成一个范围更大的锁.
			如:
			public class StringBufferTest {
			    public void append(){
			    	StringBuffer stringBuffer = new StringBuffer();
			        stringBuffer.append("a");
			        stringBuffer.append("b");
			    }
			}
			里每次调用stringBuffer.append方法都需要加锁和解锁,如果虚拟机检测到有一系列连串的对同一个对象加锁和解锁操作,
			就会将其合并成一次范围更大的加锁和解锁操作,即在第一次append方法时进行加锁,最后一次append方法结束后进行解锁
		(3).锁消除(Lock Elimination):锁消除即删除不必要的加锁操作.
			根据代码逃逸技术,如果判断到一段代码中,堆上的数据不会逃逸出当前线程,那么可以认为这段代码是线程安全的,不必要加锁.
			如:方法内的局部变量
	7.7.总结:
		JDk 中采用轻量级锁和偏向锁等对 synchronized 的优化,但是这两种锁也不是完全没缺点的,比如竞争比较激烈的时候,不但无法提升效率,
		反而会降低效率,因为多了一个锁升级的过程,这个时候就需要通过 -XX:-UseBiasedLocking 来禁用偏向锁.
		(1).偏向锁:
			==> 优点:加锁和解锁不需要额外的消耗,和执行非同步方法比仅存在纳秒级的差距;
			==> 缺点:如果线程存在锁竞争,会带来额外的锁撤销的消耗.
			==> 适用场景:适用于只有一个线程访问同步块的场景;
		(2).轻量级锁:
			==> 优点:竞争的线程不会阻塞,提高了程序的响应速度;
			==> 缺点:如果始终得不到锁竞争的线程使用自旋会消耗CPU;
			==> 适用场景:追求响应时间,同步执行速度非常块;
		(3).重量级锁:
			==> 优点:线程竞争不使用自旋,不会消耗CPU;
			==> 缺点:线程阻塞,响应时间缓慢;
			==> 适用场景:追求吞吐量,同步块执行速度较长.
## 8.volatile:
	参考:http://note.youdao.com/noteshare?id=edebb78765a3925d235c41d9a71b52c6&sub=CDD93342E9564120A27007EF2020140E
## 9.线程安全及不可变性:
	线程安全包含原子性和可见性两个方面;
	(1).当多个线程同时访问同一个资源，并且其中的一个或者多个线程对这个资源进行了写操作，才会产生竞态条件,
		多个线程同时读同一个资源不会产生竞态条件;
	(2).可以通过创建不可变的共享对象来保证对象在线程间共享时不会被修改，从而实现线程安全:
		不变与只读:
		当一个变量是“只读”时，变量的值不能直接改变，但是可以在其它变量发生改变的时候发生改变
		如一个人的出生年月日与年龄,出生年月是不变的,年龄是只读的;
	(3).引用不是线程安全的！
		即使一个对象是线程安全的不可变对象，指向这个对象的引用也可能不是线程安全的
		如果一个类是线程安全的,但如果使用它的类不是线程安全的,则整体也不是线程安全的

## 10.线程通信:
	目标是使线程间能够互相发送信号.另一方面,线程通信使线程能够等待其他线程的信号
	10.1.使用 sleep 和 while(true)来实现线程通信:
		弊端:线程需要不停的通过 while 语句轮询机制来检测某一个条件,注意会浪费CPU资源,
		如果轮询的时间间隔很小,更浪费CPU资源;如果时间间隔很大,有可能取不到想要得到的数据
	10.2.等待/唤醒机制:
		(1).方法 wait 的作用是使当前线程执行的代码进行等待,wait 方法是 Object 类的方法,该方法用来将当前线程置入"预执行队列",
			并且在wait 所在的代码行处停止执行,直到接到通知或者被中断为止.在调用 wait 之前,线程必须获得该对象的对象级别锁,
			即只能在同步方法或者同步代码块中调用wait 方法.在执行wait方法之后,当前线程释放锁.在从 wati 返回之前,线程与其他
			线程竞争重新获得锁.如果调用wait 时没有合适的锁,则抛出 IllegalMonitorStateException 异常.不需要 catch;
		(2).方法 notify 也要在同步方法或者同步块中调用,即在调用前,线程必须获得该对象的对象级别锁.如果调用 notify 是没有适当的
			锁,也会抛出 IllegalMonitorStateException.该方法用来通知那些可能等待该对象的对象锁的其他线程,如果有多个线程等待,
			则由线程规划其随机挑选一个呈wait状态的线程,对其发出 notify 通知,并使它等待获取该对象的对象锁.
			值的注意的是:在执行 notify 方法后,当前线程不会马上释放该对象锁,呈wait状态的线程也并不能马上获取该对象锁,
			需要等到执行 notify 方法的线程将程序执行完,也就是退出 synchronized 代码块后,当前线程才会释放锁,而呈wait状态所在的线程
			才可以获取该对象锁.
			当第一个获得了该对象锁的wait线程执行完毕后,它会释放该对象锁,此时如果该对象没有再次使用 notify 语句,则即便该对象已经
			空闲,其他wait状态等待的线程由于没有得到该对象的通知,还会继续阻塞在 wait状态,直到这个对象发挥一个 notify 或者 notifyAll
		==> wait使线程停止运行,而 notify 使停止的线程继续运行
		(3).通知过早问题:
			如果在 调用 wait 方法之前就调用过 notify 方法,即通知过早问题,会打乱程序的正常逻辑
		(4).等待wait的条件发生了变化,也容易造成程序逻辑混乱.最典型的为对集合的操作
	10.3.生产者/消费者模式:等待/通知模式最经典的案例
		10.3.1.一生产与一消费:操作值:
			1个生产者与1个消费者进行数据交互
		10.3.2.多生产与多消费:操作值,有可能"假死"
			(1).假死:其实就是线程进入 WAITING 等待状态,如果全部线程都进入 WAITING 状态,
				则程序就不再执行任何业务功能了,整个项目呈停止状态;
			(2).多个生产者与消费者为什么会产生"假死"?
				虽然在代码中使用来 wait/notify,但不保证 notify 唤醒的是异类,也许是同类,比如:
				"生产者"唤醒"生产者","消费者"唤醒"消费者",按照这样情况运行的比率积少成多,就会导致
				所有的线程都不能继续运行下去,大家都在等待;
			(3).解决"假死"现象:
				将 生产者和消费者中的 notify 改为 notifyAll,那么其不光通知同类线程,也包括异类线程
		10.3.3.一生产与一消费:操作栈
			生产者向堆栈 List 对象中放入数据,使消费者从 List 堆栈中取出数据
		10.3.4.生产者消费者的不同模式实现:

[生产者消费者](https://github.com/chenlanqing/learningNote/blob/master/Java/Java%E6%BA%90%E7%A0%81%E8%A7%A3%E8%AF%BB/thread/生产者与消费者.md)

	10.4.通过管道进行线程间通信:字节流
		一个线程发送数据到输出管道,另一个线程从输入管道中读取数据,实现不同线程间通信
		使用代码 inputStream.connect(outputStream) 或者 outputStream.connect(inputStream) 的作用
		使两个Stream之间产生通信链接,这样才可以将数据进行输出与输入
	10.5.wait(),notify()和notifyAll():java.lang.Object 类定义了三个方法
		(1).一个线程一旦调用了任意对象的wait()方法，就会变为非运行状态，直到另一个线程调用了同一个对象的notify()方法。
			为了调用wait()或者notify()，线程必须先获得那个对象的锁,也就是说线程必须在同步块里调用wait()或者notify()
		(2).不管是等待线程还是唤醒线程都在同步块里调用wait()和notify()。这是强制性的！一个线程如果没有持有对象锁,
			将不能调用wait()，notify()或者notifyAll()。否则，会抛出 IllegalMonitorStateException 异常;
		(3).一旦线程调用了wait()方法，它就释放了所持有的监视器对象上的锁.这将允许其他线程也可以调用wait()或者notify()
		(4).一旦一个线程被唤醒，不能立刻就退出wait()的方法调用，直到调用notify()的线程退出了它自己的同步块,
			换句话说:
			被唤醒的线程必须重新获得监视器对象的锁，才可以退出wait()的方法调用，因为wait方法调用运行在同步块里面
			如果多个线程被notifyAll()唤醒,那么在同一时刻将只有一个线程可以退出wait()方法,
				因为每个线程在退出wait()前必须获得监视器对象的锁
	10.6.丢失的信号(Missed Signals):
		(1).notify()和notifyAll()方法不会保存调用它们的方法，因为当这两个方法被调用时,
			有可能没有线程处于等待状态,通知信号过后便丢弃了,
			如果一个线程先于被通知线程调用wait()前调用了notify()，等待的线程将错过这个信号
	10.7.假唤醒:
		(1).由于莫名其妙的原因,线程有可能在没有调用过notify()和notifyAll()的情况下醒来.这就是所谓的假唤醒(spurious wakeups)
		(2).为了防止假唤醒，保存信号的成员变量将在一个while循环里接受检查,而不是在if表达式里.这样的一个while循环叫做自旋锁
			注意:这种做法要慎重,目前的JVM实现自旋会消耗CPU,如果长时间不调用doNotify方法,doWait方法会一直自旋,CPU会消耗太大
	10.8.多个线程等待相同信号:
	10.9.不要在字符串常量或全局对象中调用wait()
		在wait()/notify()机制中，不要使用全局对象，字符串常量等。应该使用对应唯一的对象
## 11.ThreadLocal 类:
	存放每个线程的共享变量,解决变量在不同线程间的隔离性
	参考资料
	 * http://www.importnew.com/14398.html, http://www.importnew.com/16112.html
	 * http://www.cnblogs.com/dolphin0520/p/3920407.html,
	 * https://vence.github.io/2016/05/28/threadlocal-info/
	 * https://segmentfault.com/a/1190000000537475
	 * http://www.jianshu.com/p/33c5579ef44f
	 * https://toutiao.io/posts/nic1qr/preview
	 * http://blog.brucefeng.info/post/threadlocal-resultin-fullgc?utm_source=tuicool&utm_medium=referral

	11.1.创建 ThreadLocal 对象:private ThreadLocal myThreadLocal = new ThreadLocal();
		每个线程仅需要实例化一次即可
		虽然不同的线程执行同一段代码时,访问同一个ThreadLocal变量，但是每个线程只能看到私有的ThreadLocal实例
	11.2.访问 ThreadLocal 对象:
		(1).一旦创建了一个ThreadLocal对象，你就可以通过以下方式来存储此对象的值:
			myThreadLocal.set("a thread local value");
		(2).可以直接读取一个 ThreadLocal 对象的值:
			String threadLocalValue = (String) myThreadLocal.get();
	11.3.ThreadLocal 泛型:
		可以创建一个泛型化的ThreadLocal对象,当你从此ThreadLocal实例中获取值的时候，就不必要做强制类型转换
		private ThreadLocal myThreadLocal1 = new ThreadLocal<String>();
	11.4.初始化 ThreadLocal:
		(1).由于ThreadLocal对象的set()方法设置的值只对当前线程可见,那有什么方法可以为
			ThreadLocal 对象设置的值对所有线程都可见?
			通过 ThreadLocal 子类的实现，并覆写initialValue()方法，就可以为 ThreadLocal 对象指定一个初始化值
			private ThreadLocal myThreadLocal = new ThreadLocal<String>() {
			   @Override
			   protected String initialValue() {
			       return "This is the initial value";
			   }
			};
	11.5.完整的 ThreadLocal 实例:
		public class ThreadLocalExample {
		    public static class MyRunnable implements Runnable {
		        private ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();
		        @Override
		        public void run() {
		            threadLocal.set( (int) (Math.random() * 100D) );
		            try {
		                Thread.sleep(2000);
		            } catch (InterruptedException e) {
		            }
		            System.out.println(threadLocal.get());
		        }
		    }
		    public static void main(String[] args) {
		        MyRunnable sharedRunnableInstance = new MyRunnable();
		        Thread thread1 = new Thread(sharedRunnableInstance);
		        Thread thread2 = new Thread(sharedRunnableInstance);
		        thread1.start();
		        thread2.start();

		        thread1.join(); //wait for thread 1 to terminate
		        thread2.join(); //wait for thread 2 to terminate
		    }

		}
	11.6.InheritableThreadLocal:ThreadLocal的子类.为了解决ThreadLocal实例内部每个线程都只能看到自己的私有值，
		所以 InheritableThreadLocal 允许一个线程创建的所有子线程访问其父线程的值.
	11.7.ThreadLocal 在什么情况下会发生内存泄露:
		// http://blog.jobbole.com/104364/
		11.7.1.ThreadLocal 的实现:
			每个 Thread 维护一个 ThreadLocalMap 映射表,这个映射表的 key 是 ThreadLocal 实例本身,value 是真正需要存储的 Object.
			就是说 ThreadLocal 本身并不存储值,它只是作为一个 key 来让线程从 ThreadLocalMap 获取 value.
			ThreadLocalMap 是使用 ThreadLocal 的弱引用作为 Key 的,弱引用的对象在 GC 时会被回收.
		11.7.2.为什么会内存泄漏:
			(1).ThreadLocalMap 使用 ThreadLocal 的弱引用作为key,如果一个 ThreadLocal 没有外部强引用来引用它,那么系统 GC 的时候,
				这个 ThreadLocal 势必会被回收,这样一来,ThreadLocalMap 中就会出现key为 null 的 Entry,就没有办法访问这些key为null的
				Entry 的value.如果当前线程再迟迟不结束的话,这些key为null的Entry的value就会一直存在一条强引用链:
				Thread Ref -> Thread -> ThreaLocalMap -> Entry -> value永远无法回收,造成内存泄漏.
				只有thead退出以后,value的强引用链条才会断掉
			(2).ThreadLocalMap 的设计中已经考虑到这种情况,加上了一些防护措施:在ThreadLocal的get(),set(),remove()的时候都会清除线程
				ThreadLocalMap 里所有key为null的value
			(3).但是这些被动的预防措施并不能保证不会内存泄漏:
				==> 使用线程池的时候,这个线程执行任务结束,ThreadLocal 对象被回收了,线程放回线程池中不销毁,这个线程一直不被使用,导致内存泄漏;
				==> 分配使用了ThreadLocal又不再调用get(),set(),remove()方法,那么这个期间就会发生内存泄漏
		11.7.3.为什么使用弱引用:
			(1).key 使用强引用:
				引用的 ThreadLocal 的对象被回收了,但是 ThreadLocalMap 还持有 ThreadLocal 的强引用,
				如果没有手动删除,ThreadLocal 不会被回收,导致 Entry 内存泄漏
			(2).key 使用弱引用:
				引用的 ThreadLocal 的对象被回收了,由于 ThreadLocalMap 持有 ThreadLocal 的弱引用,
				即使没有手动删除,ThreadLocal 也会被回收.value在下一次ThreadLocalMap调用set,get的时候会被清除
			==> 对比上述情况可以发现:
				由于 ThreadLocalMap 的生命周期跟 Thread 一样长,如果都没有手动删除对应key,都会导致内存泄漏,但是使用弱引用可以多一层保障:
				弱引用ThreadLocal不会内存泄漏,对应的value在下一次ThreadLocalMap调用set,get,remove的时候会被清除
			==> ThreadLocal内存泄漏的根源是:
				由于ThreadLocalMap的生命周期跟Thread一样长,如果没有手动删除对应key就会导致内存泄漏,而不是因为弱引用
		11.7.4.ThreadLocal 最佳实践:如何避免内存泄漏:
			每次使用完 ThreadLocal,都调用它的remove()方法,清除数据.
			在使用线程池的情况下,没有及时清理 ThreadLocal,不仅是内存泄漏的问题,更严重的是可能导致业务逻辑出现问题.
	11.8.ThreadLocal 的应用场景:
		使用场合主要解决多线程中数据数据因并发产生不一致问题
		11.8.1.最常见的 ThreadLocal 使用场景为 用来解决 数据库连接、Session 管理等.
		11.8.2.参数传递:
			场景:如果方法一层一层调用,调用了很多层,但是有个别参数只需要第一层方法和最后一层方式使用,如何传递?
			可以使用 ThreadLocal 来操作.
			public class ThreadLocalCache {
			    public static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();
			}
			在拦截器或者AOP 中设置需要传输的参数
			==> 注意:在请求结束后一定要调用 remove 方法,移出不必要的键值对,以免造成内存泄漏.
		11.8.3.父子线程数据传递:InheritableThreadLocal
			(1).该类继承自 ThreadLocal
```java
				public class InheritableThreadLocal<T> extends ThreadLocal<T> {
				    protected T childValue(T parentValue) {
				        return parentValue;
				    }
				    /**
				     * 重写Threadlocal类中的getMap方法，在原Threadlocal中是返回
				     *t.theadLocals，而在这么却是返回了inheritableThreadLocals，因为
				     * Thread类中也有一个要保存父子传递的变量
				     */
				    ThreadLocalMap getMap(Thread t) {
				       return t.inheritableThreadLocals;
				    }
				    /**
				     * 同理，在创建ThreadLocalMap的时候不是给t.threadlocal赋值
				     *而是给inheritableThreadLocals变量赋值
				     *
				     */
				    void createMap(Thread t, T firstValue) {
				        t.inheritableThreadLocals = new ThreadLocalMap(this, firstValue);
				    }
				}
```
			(2).InheritableThreadLocal 是如何实现在子线程中能拿到当前父线程中的值的呢?
				创建线程时,init(....)方法里有如下代码:
				if (parent.inheritableThreadLocals != null)
			       //这句话的意思大致不就是，copy父线程parent的map，创建一个新的map赋值给当前线程的inheritableThreadLocals。
		           	this.inheritableThreadLocals = ThreadLocal.createInheritedMap(parent.inheritableThreadLocals);
		        拷贝 parentMap 的数据,在copy过程中是浅拷贝,key和value都是原来的引用地址
		        A.在创建 InheritableThreadLocal 对象的时候赋值给线程的 t.inheritableThreadLocals 变量
		        B.在创建新线程的时候会check父线程中t.inheritableThreadLocals变量是否为 null,如果不为null则copy一份
		        	ThradLocalMap 到子线程的t.inheritableThreadLocals成员变量中去
		        C.因为复写了getMap(Thread)和CreateMap()方法,所以get值得时候,就可以在getMap(t)的时候就会从
		        	t.inheritableThreadLocals中拿到map对象,从而实现了可以拿到父线程ThreadLocal中的值
		    (3).InheritableThreadLocal 问题:在线程池中,会缓存之前使用过的线程
		    	①.问题场景:
		    		有两个线程A,B, 如果A线程先执行后,将InheritableThreadLocal中的value已经被更新了,
		    		由于B是获取到缓存线程,直接从t.InheritableThreadLocal 中获得值,而此时获取的值是 A 线程修改过后的值,
		    		而不是原来父线程的值.
		    	②.造成问题的原因:
		    		线程在执行完毕的时候并没有清除ThreadLocal中的值,导致后面的任务重用现在的localMap
		    	③.解决方案:
		    		在使用完这个线程的时候清除所有的localMap,在submit新任务的时候在重新重父线程中copy所有的Entry.
		    		然后重新给当前线程的t.inhertableThreadLocal赋值
		    		阿里巴巴有一套解决方案:transmittable-thread-local // https://github.com/chenlanqing/transmittable-thread-local
## 12.深入理解 ThreadLocal:
	12.1.理解 ThreadLocal:
		ThreadLocal 在每个线程中对该变量会创建一个副本,即每个线程内部都会有一个该变量,且在线程内部任何地方都可以使用,
		线程之间互不影响，这样一来就不存在线程安全问题,也不会严重影响程序执行性能;
	12.2.深入解析 ThreadLocal 类:
		(1).public T get() { }:用来获取ThreadLocal在当前线程中保存的变量副本
		(2).public void set(T value) { }:用来设置当前线程中变量的副本
		(3).public void remove() { }:用来移除当前线程中变量的副本
		(4).protected T initialValue() { }:是一个 protected 方法,一般是用来在使用时进行重写的,它是一个延迟加载方法
		(5).ThreadLocal 是如何为每个线程创建变量的副本的:
			①.在每个线程 Thread 内部有一个 ThreadLocal.ThreadLocalMap 类型的成员变量 threadLocals，这个threadLocals就是
			用来存储实际的变量副本的,键值为当前ThreadLocal变量，value为变量副本(即T类型的变量)
			②.初始时，在Thread里面，threadLocals为空，当通过ThreadLocal变量调用get()方法或者set()方法,就会对Thread类中的
			threadLocals进行初始化,并且以当前ThreadLocal变量为键值,以ThreadLocal要保存的副本变量为value,存到threadLocals;
			③.为何threadLocals的类型ThreadLocalMap的键值为ThreadLocal对象，因为每个线程中可有多个threadLocal变量，
				就像上面代码中的longLocal和stringLocal
		(6).在进行get之前，必须先set，否则会报空指针异常;
			如果想在get之前不需要调用set就能正常访问的话，必须重写initialValue()方法
			ThreadLocal<Long> longLocal = new ThreadLocal<Long>(){
			 	protected Long initialValue() {
			 		return Thread.currentThread().getId();
			 	};
			};
	12.4.变量:
		private final int threadLocalHashCode = nextHashCode();
		// 即将分配的下一个ThreadLocal实例的threadLocalHashCode 的值
		private static AtomicInteger nextHashCode = new AtomicInteger();
		// 表示了连续分配的两个ThreadLocal实例的threadLocalHashCode值的增量
		private static final int HASH_INCREMENT = 0x61c88647;
		12.4.1.哈希策略:
			所有 ThreadLocal 对象共享一个AtomicInteger对象nextHashCode用于计算hashcode,一个新对象产生时它的
			hashcode就确定了,算法是从0开始,以 HASH_INCREMENT = 0x61c88647 为间隔递增.
			这是ThreadLocal唯一需要同步的地方
		12.4.1.0x61c88647 这个魔数是怎么确定的呢?
			ThreadLocalMap 的初始长度为16,每次扩容都增长为原来的2倍,即它的长度始终是2的n次方,
			上述算法中使用 0x61c88647 可以让hash的结果在2的n次方内尽可能均匀分布,减少冲突的概率.
	12.5.ThreadLocalMap:
		(1).义在 ThreadLocal 类内部的私有类,它是采用"开放定址法"解决冲突的hashmap.
			key是 ThreadLocal 对象.当调用某个ThreadLocal对象的get或put方法时,首先会从当前线程中取出
			ThreadLocalMap,然后查找对应的value.
			ThreadLocalMap 实例是作为 java.lang.Thread 的成员变量存储的,每个线程有唯一的一个 threadLocalMap
			public T get() {
			    Thread t = Thread.currentThread();
			    ThreadLocalMap map = getMap(t);     //拿到当前线程的ThreadLocalMap
			    if (map != null) {
			        ThreadLocalMap.Entry e = map.getEntry(this);    // 以该ThreadLocal对象为key取value
			        if (e != null)
			            return (T)e.value;
			    }
			    return setInitialValue();
			}
			ThreadLocalMap getMap(Thread t) {
			    return t.threadLocals;
			}
		(2).将 ThreadLocalMap 作为 Thread 类的成员变量的好处是:
			当线程死亡时,threadLocalMap被回收的同时,保存的"线程局部变量"如果不存在其它引用也可以同时被回收.
			同一个线程下,可以有多个treadLocal实例,保存多个"线程局部变量".
			同一个threadLocal实例,可以有多个线程使用,保存多个线程的"线程局部变量".
	12.6.碰撞解决与神奇的 0x61c88647:既然 ThreadLocal 用map就避免不了冲突的产生
		12.6.1.碰撞的类型:
			(1).只有一个ThreadLocal实例的时候,当向thread-local变量中设置多个值的时产生的碰撞,
				碰撞解决是通过开放定址法,且是线性探测(linear-probe)
			(2).多个ThreadLocal实例的时候,最极端的是每个线程都new一个ThreadLocal实例,此时利用特殊的
				哈希码0x61c88647大大降低碰撞的几率, 同时利用开放定址法处理碰撞
		12.6.2.神奇的0x61c88647:注意 0x61c88647 的利用主要是为了多个ThreadLocal实例的情况下用的.
			private final int threadLocalHashCode = nextHashCode();
			每当创建ThreadLocal实例时这个值都会累加 0x61c88647.为了让哈希码能均匀的分布在2的N次方的数组里,即Entry[] table
			==> threadLocalHashCode 的使用:
				private void set(ThreadLocal<?> key, Object value) {
		            Entry[] tab = table;
		            int len = tab.length;
		            int i = key.threadLocalHashCode & (len-1);
					...		           
		        }
	            key.threadLocalHashCode & (len-1)
	            ThreadLocalMap 中 Entry[] table 的大小必须是2的N次方呀(len = 2^N), 那 len-1
	            的二进制表示就是低位连续的N个1,那 key.threadLocalHashCode & (len-1) 的值就是
	            threadLocalHashCode 的低N位.产生的哈希码分布真的是很均匀,而且没有任何冲突啊
	            可以使用python验证:
	            	import sys
	            	HASH_INCREMENT = 0x61c88647
	            	def magic_hash(n):
	            		for i in range(n):
	            			nextHashCode = i * HASH_INCREMENT + HASH_INCREMENT;
	            			result = nextHashCode & (n-1)
	            			sys.stdout.write(str(result))
	            			sys.stdout.write("\t")
	            		print()
	            	magic_hash(16)
	            	magic_hash(64)
	12.7.ThreadLocal 和 synchronized:
		(1).ThreadLocal 和 synchronized 都用于解决多线程并发访问.
		(2).ThreadLocal 与 synchronized 有本质的区别:
			synchronized 是利用锁的机制,使变量或代码块在某一时该只能被一个线程访问.
			ThreadLocal 为每一个线程都提供了变量的副本,使得每个线程在某一时间访问到的并不是同一个对象,
				这样就隔离了多个线程对数据的数据共享
		(3).synchronized 用于线程间的数据共享,而 ThreadLocal 则用于线程间的数据隔离

## 13.死锁:
	两个或更多线程阻塞着等待其它处于死锁状态的线程所持有的锁
	13.1.死锁通常发生在多个线程同时但以不同的顺序请求同一组锁的时候
		死锁产生的必要条件:
		(1).互斥条件
		(2).请求和保持条件
		(3).不剥夺条件
	13.2.死锁程序:
```java
		public class DeadLock{
			public void method1(){
				synchronized(Integer.class){
					System.out.println("Aquired lock on Integer.class object");
					synchronized(String.class){
						System.out.println("Aquired lock on String.class object");
					}
				}
			}
			public void method2(){
				synchronized(String.class){
					System.out.println("Aquired lock on String.class object");
					synchronized(Integer.class){
						System.out.println("Aquired lock on Integer.class object");
					}
				}
			}
		}
```
	13.3.避免死锁:
		(1).加锁顺序:如果能确保所有的线程都是按照相同的顺序获得锁，那么死锁就不会发生
			按照顺序加锁是一种有效的死锁预防机制。
			但是,这种方式需要你事先知道所有可能会用到的锁(并对这些锁做适当的排序)，但总有些时候是无法预知的
		(2).加锁时限:
			①.在尝试获取锁的时候加一个超时时间,这也就意味着在尝试获取锁的过程中若超过了这个时限该线程则放弃对该锁请求
			若一个线程没有在给定的时限内成功获得所有需要的锁,则会进行回退并释放所有已经获得的锁,然后等待一段随机的时间再重试
			②.这种机制存在一个问题，在Java中不能对 synchronized 同步块设置超时时间
				你需要创建一个自定义锁，或使用 Java5 中 java.util.concurrent 包下的工具
		(3).死锁检测:是一个更好的死锁预防机制，它主要是针对那些不可能实现按序加锁并且锁超时也不可行的场景
			①.如何检测:
				每当一个线程获得了锁,会在线程和锁相关的数据结构中(map、graph等等)将其记下.除此之外,每当有线程请求锁,
				也需要记录在这个数据结构中.当一个线程请求锁失败时,这个线程可以遍历锁的关系图看看是否有死锁发生
			②.当检测出死锁时，这些线程该做些什么呢?
				A.一个可行的做法是释放所有锁，回退，并且等待一段随机的时间后重试
				然有回退和等待，但是如果有大量的线程竞争同一批锁，它们还是会重复地死锁
				B.一个更好的方案是给这些线程设置优先级，让一个(或几个)线程回退，
				剩下的线程就像没发生死锁一样继续保持着它们需要的锁
	13.4.监测是否有死锁现象:
		(1).执行 jps 命令,可以得到运行的线程 3244,再执行 jstack命令
		jstack -l 3244;
## 14.饥饿和公平:
	14.1.饥饿:如果一个线程因为CPU时间全部被其他线程抢走而得不到CPU运行时间，这种状态被称之为“饥饿”
		解决饥饿的方案被称之为“公平性” – 即所有线程均能公平地获得运行机会
	14.2.Java 中导致饥饿的原因:
		(1).高优先级线程吞噬所有的低优先级线程的CPU时间:
			优先级越高的线程获得的CPU时间越多，线程优先级值设置在1到10之间，而这些优先级值所表示行为的准确解释则
			依赖于你的应用运行平台,对大多数应用来说,你最好是不要改变其优先级值;
		(2).线程被永久堵塞在一个等待进入同步块的状态,因为其他线程总是能在它之前持续地对该同步块进行访问:
		(3).线程在等待一个本身也处于永久等待完成的对象(比如调用这个对象的wait方法)
			如果多个线程处在wait()方法执行上，而对其调用notify()不会保证哪一个线程会获得唤醒，
			任何线程都有可能处于继续等待的状态;
	14.3.在Java中实现公平性方案:
		(1).使用锁，而不是同步块:为了提高等待线程的公平性，我们使用锁方式来替代同步块
		(2).公平锁。
		(3).注意性能方面
# 三.JUC(java.util.concurrent)包
	从整体来看,concurrent包的实现示意图:
![image](https://github.com/chenlanqing/learningNote/blob/master/Java/JavaSE/多线程/image/concurrent包的实现示意图.png)
## 1.JUC 原子类:
	目的是对相应的数据进行原子操作.所谓原子操作,是指操作过程不会被中断,保证数据操作是以原子方式进行的
	(1).基本类型: AtomicInteger, AtomicLong, AtomicBoolean ;
		JDK8 新增: DoubleAdder, LongAdder
	(2).数组类型: AtomicIntegerArray, AtomicLongArray, AtomicReferenceArray ;
	(3).引用类型: AtomicReference, AtomicStampedRerence, AtomicMarkableReference ;
	(4).对象的属性修改类型: AtomicIntegerFieldUpdater, AtomicLongFieldUpdater, AtomicReferenceFieldUpdater
	1.1.AtomicLong:
		(1).AtomicLong 是作用是对长整形进行原子操作.
			在32位操作系统中,64位的 long 和 double 变量由于会被JVM当作两个分离的32位来进行操作,所以不具有原子性.
			而使用 AtomicLong 能让 long 的操作保持原子型.
			long foo = 65465498L;  ==> 非原子操作,Java 会分两步写入 long 变量,先写32位,再写后32位,就非线程安全的.
			private volatile long foo;  ==> 原子性操作

## 2.锁的相关概念:
	* https://www.cnblogs.com/charlesblc/p/5994162.html
	2.1.同步锁:通过synchronized关键字来进行同步
		同步锁的原理是,对于每一个对象,有且仅有一个同步锁:不同的线程能共同访问该同步锁.
		但是,在同一个时间点,该同步锁能且只能被一个线程获取到
	2.2.JUC 包中的锁，包括:Lock 接口,ReadWriteLock 接口,LockSupport 阻塞原语,Condition 条件,
		AbstractOwnableSynchronizer/AbstractQueuedSynchronizer/AbstractQueuedLongSynchronizer 三个抽象类,
		ReentrantLock 独占锁,ReentrantReadWriteLock 读写锁.由于 CountDownLatch，CyclicBarrier 和 Semaphore 也是通过AQS来实现的
	2.3.可重入锁:synchronized 和 ReentrantLock 都是可重入锁,锁基于线程的分配,而不是基于方法调用的分配.
		线程可以进入任何一个它已经拥有的锁所同步着的代码块.
		可重入锁是用来最大的作用是用来解决死锁的;
	2.4.AQS(AbstractQueuedSynchronizer)类:
		是java中管理"锁"的抽象类,锁的许多公共方法都是在这个类中实现.
		AQS 是独占锁(例如:ReentrantLock)和共享锁(例如:Semaphore)的公共父类.
		AQS 锁的分类:
		(1).独占锁: 锁在一个时间点只能被一个线程锁占有.根据锁的获取机制,它又划分为"公平锁"和"非公平锁".
			公平锁,是按照通过CLH等待线程按照先来先得的规则,公平的获取锁;
			非公平锁,则当线程要获取锁时,它会无视CLH等待队列而直接获取锁.
			独占锁的典型实例子是 ReentrantLock,此外,ReentrantReadWriteLock.WriteLock 也是独占锁
		(2).共享锁:能被多个线程同时拥有,能被共享的锁.
			JUC 包中的 ReentrantReadWriteLock.ReadLock,CyclicBarrier,CountDownLatch 和 Semaphore 都是共享锁.
	2.5.CLH 队列-Craig, Landin, and Hagersten lock queue
		CLH 队列是 AQS 中"等待锁"的线程队列.在多线程中,为了保护竞争资源不被多个线程同时操作而起来错误,常常需要通过锁来保护这些资源
		在独占锁中,竞争资源在一个时间点只能被一个线程锁访问;而其它线程则需要等待.CLH 就是管理这些"等待锁"的线程的队列.
		CLH 是一个非阻塞的 FIFO 队列.也就是说往里面插入或移除一个节点的时候,在并发条件下不会阻塞,
		而是通过自旋锁和 CAS 保证节点插入和移除的原子性
	2.6.CAS:Compare And Swap
		是比较并交换函数,它是原子操作函数;即通过 CAS 操作的数据都是以原子方式进行的.
## 3.独占锁:
	3.1.ReentrantLock 类(可重入锁),又称为独占锁.

[ReentantLock](https://github.com/chenlanqing/learningNote/blob/master/Java/Java%E6%BA%90%E7%A0%81%E8%A7%A3%E8%AF%BB/thread/ReentrantLock.md)

		3.1.1.ReentrantLock 基本:
			(1).在同一个时间点只能被一个线程持有,而可重入即可以被单个线程多次获取.
			(2).ReentrantLock 分为"公平锁"和"非公平锁",区别在于获取锁的机制上是否公平.
			(3).ReentrantLock 是通过一个 FIFO 的等待队列来管理获取该锁的所有线程."公平锁"的机制下,线程依次排队获取,
				而"非公平锁"在锁是可获取状态时,不管自己是不是在队列的开头都会获取锁.
				ReentrantLock中,包含了Sync对象.而且,Sync 是 AQS 的子类;更重要的是,Sync 有两个子类 FairSync(公平锁)和
				NonFairSync(非公平锁).ReentrantLock 是一个独占锁,至于它到底是公平锁还是非公平锁,
				就取决于sync对象是"FairSync的实例"还是"NonFairSync的实例"
			(4).提供了一个Condition类,可以分组唤醒需要唤醒的线程.
		3.1.2.ReentrantLock 函数列表:
			// 创建一个 ReentrantLock ，默认是“非公平锁”。
			ReentrantLock()
			// 创建策略是fair的 ReentrantLock。fair为true表示是公平锁，fair为false表示是非公平锁。
			ReentrantLock(boolean fair)
			// 查询当前线程保持此锁的次数。
			int getHoldCount()
			// 返回目前拥有此锁的线程，如果此锁不被任何线程拥有，则返回 null。
			protected Thread getOwner()
			// 返回一个 collection，它包含可能正等待获取此锁的线程。
			protected Collection<Thread> getQueuedThreads()
			// 返回正等待获取此锁的线程估计数。
			int getQueueLength()
			// 返回一个 collection，它包含可能正在等待与此锁相关给定条件的那些线程。
			protected Collection<Thread> getWaitingThreads(Condition condition)
			// 返回等待与此锁相关的给定条件的线程估计数。
			int getWaitQueueLength(Condition condition)
			// 查询给定线程是否正在等待获取此锁。
			boolean hasQueuedThread(Thread thread)
			// 查询是否有些线程正在等待获取此锁。
			boolean hasQueuedThreads()
			// 查询是否有些线程正在等待与此锁有关的给定条件。
			boolean hasWaiters(Condition condition)
			// 如果是“公平锁”返回true，否则返回false。
			boolean isFair()
			// 查询当前线程是否保持此锁。
			boolean isHeldByCurrentThread()
			// 查询此锁是否由任意线程保持。
			boolean isLocked()
			// 获取锁。
			void lock()
			// 如果当前线程未被中断，则获取锁。
			void lockInterruptibly()
			// 返回用来与此 Lock 实例一起使用的 Condition 实例。
			Condition newCondition()
			// 仅在调用时锁未被另一个线程保持的情况下，才获取该锁。
			boolean tryLock()
			// 如果锁在给定等待时间内没有被另一个线程保持，且当前线程未被中断，则获取该锁。
			boolean tryLock(long timeout, TimeUnit unit)
			// 试图释放此锁。
			void unlock()
		3.1.3.公平锁:是按照通过CLH等待线程按照先来先得的规则,公平的获取锁
			3.1.3.1.获取公平锁:获取锁是通过lock()函数
				(1).lock():是在ReentrantLock.java的FairSync类中实现
		3.1.4.非公平锁:则当线程要获取锁时,它会无视CLH等待队列而直接获取锁.
		3.1.5.非公平锁获取:

## 4.共享锁-ReentrantReadWriteLock 读写锁:
	4.1.ReadWriteLock,读写锁,维护了一对锁:读取锁和写入锁.
		读取锁-只用于读取数据操作,是"共享锁",能被多个线程同时获取;
		写入锁-用于写入操作,是"独占锁",只能被一个线程获取.
		==> 不能同时存在读取锁和写入锁
		ReadWriteLock 是一个接口,ReentrantReadWriteLock 是它的实现类.ReentrantReadWriteLock 包括内部类 ReadLock 和 WriteLock
	4.2.
		// 返回用于读取操作的锁。
		ReentrantReadWriteLock.ReadLock readLock()
		// 返回用于写入操作的锁。
		ReentrantReadWriteLock.WriteLock writeLock()
## 5.共享锁-闭锁:CountDownLatch:
	参考文章: http://www.cnblogs.com/skywang12345/p/3533887.html
	(1).是一个同步辅助类,在完成一组正在其他线程中执行的操作之前,它允许一个或多个线程一直等待.允许1或N个线程等待其他线程完成执行
	(2).数据结构:CountDownLatch 包含了sync对象,sync是 Sync 类型.CountDownLatch 的 Sync 是实例类,它继承于 AQS
		通过"共享锁"实现.CountDownLatch 中3个核心函数: CountDownLatch(int count), await(), countDown()
		--> CountDownLatch(int count):
			public CountDownLatch(int count) {
				if (count < 0) throw new IllegalArgumentException("count < 0");
				this.sync = new Sync(count);
			}
			Sync(int count) {
				setState(count);
			}
			protected final void setState(long newState) {
				state = newState;
			}
			在AQS中，state是一个private volatile long类型的对象.对于CountDownLatch而言，state表示的”锁计数器“.
			CountDownLatch中的getCount()最终是调用AQS中的getState()，返回的state对象，即”锁计数器“
	(3).使用场景:并行计算
	(4).实现原理:
		A.CountDownLatch是通过“共享锁”实现的.
		B.在创建CountDownLatch中时，会传递一个int类型参数count，该参数是“锁计数器”的初始状态，表示该“共享锁”最多能被count给线程同时获取.
		C.当某线程调用该CountDownLatch对象的await()方法时，该线程会等待“共享锁”可用时，才能获取“共享锁”进而继续运行。
		D.而“共享锁”可用的条件，就是“锁计数器”的值为0！而“锁计数器”的初始值为count，每当一个线程调用该CountDownLatch对象
			的countDown()方法时，才将“锁计数器”-1；
		E.通过这种方式，必须有count个线程调用countDown()之后，“锁计数器”才为0，而前面提到的等待线程才能继续运行！
	(5).使用例子:
```java
	private final static int threadCount = 200;
		public static void main(String[] args)throws Exception {
			final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
			ExecutorService exec = Executors.newCachedThreadPool();
			for (int i = 1; i <= threadCount; i++) {
				final int count = i;
				exec.execute(() -> {
					try{
						test(count);
					} catch (Exception e){
						log.error("exception", e);
					} finally {
						countDownLatch.countDown();
					}
				});
			}
			// 等待线程池中所有线程执行完毕后,main方法线程才继续执行
			countDownLatch.await();
			// 可以设置等待时长,即等待多少时间后执行main方法线程
	//        countDownLatch.await(10, TimeUnit.MILLISECONDS);
			log.info("~~~~~~~~main method finish {}", Thread.currentThread().getName());
			exec.shutdown();
		}
		private static void test(int count) throws Exception {
			Thread.sleep(100);
			log.info("{}, {}", count, Thread.currentThread().getName());
		}
```
## 6.栅栏:CyclicBarrier:
	参考资料:http://www.cnblogs.com/skywang12345/p/3533995.html
	6.1.是一个同步辅助类,允许一组线程互相等待,直到到达某个公共屏障点 (common barrier point).
		因为该 barrier 在释放等待线程后可以重用,所以称它为循环 的 barrier;
		CyclicBarrier 是包含了"ReentrantLock对象lock"和"Condition对象",它是通过独占锁实现的;
	6.2.主要方法:
		CyclicBarrier(int parties)
			创建一个新的 CyclicBarrier，它将在给定数量的参与者（线程）处于等待状态时启动，
			但它不会在启动 barrier 时执行预定义的操作。
		CyclicBarrier(int parties, Runnable barrierAction)
			创建一个新的 CyclicBarrier，它将在给定数量的参与者（线程）处于等待状态时启动，
			并在启动 barrier 时执行给定的屏障操作，该操作由最后一个进入 barrier 的线程执行。
		int await()
			在所有参与者都已经在此 barrier 上调用 await 方法之前，将一直等待。
		int await(long timeout, TimeUnit unit)
			在所有参与者都已经在此屏障上调用 await 方法之前将一直等待,或者超出了指定的等待时间。
		int getNumberWaiting()
			返回当前在屏障处等待的参与者数目。
		int getParties()
			返回要求启动此 barrier 的参与者数目。
		boolean isBroken()
			查询此屏障是否处于损坏状态。
		void reset()
			将屏障重置为其初始状态。
	6.3.使用场景:并行计算等
	6.4.CountDownLatch 与 CyclicBarrier 两者的区别：
		(1).CountDownLatch 的作用是允许1或N个线程等待其他线程完成执行;
			CyclicBarrier 则是允许N个线程相互等待;
		(2).CountDownLatch 的计数器无法被重置
			CyclicBarrier 的计数器可以被重置后使用,因此它被称为是循环的barrier;
	6.5.例子:
```java
	static CyclicBarrier barrier = new CyclicBarrier(5);
	// 到达屏障后执行某个回调
	static CyclicBarrier barrier = new CyclicBarrier(5, ()->{
        log.info("sdasdasdasdasdas");
    });
    public static void main(String[] args)throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 10; i++) {
            final int count = i;
            Thread.sleep(1000);
            executorService.execute(() -> {
                try {
                    race(count);
                } catch (Exception e) {
                    log.error("exception", e);
                }
            });
        }
        executorService.shutdown();
    }
    private static void race(int count) throws Exception{
        Thread.sleep(1000);
        log.info("{} is ready", count);
        barrier.await();
        log.info("{} continue",count);
    }
```
## 7.共享锁-信号量:Semaphore
	参考文章:http://www.cnblogs.com/skywang12345/p/3534050.html
	是一个计数信号量,它的本质是一个"共享锁";它的作用是限制某段代码块的并发数
	(1).信号量维护了一个信号量许可集.线程可以通过调用acquire()来获取信号量的许可;
		当信号量中有可用的许可时,线程能获取该许可;否则线程必须等待,直到有可用的许可为止.
		线程可以通过release()来释放它所持有的信号量许可
	(2).Semaphore 包含了sync对象,sync是 Sync 类型;而且,Sync 也是一个继承于 AQS 的抽象类.
		Sync包括两个子类："公平信号量"FairSync 和 "非公平信号量"NonfairSync.
		默认情况下，sync是NonfairSync(即，默认是非公平信号量).
	(3)."公平信号量"和"非公平信号量"的释放信号量的机制是一样的!不同的是它们获取信号量的机制:
		线程在尝试获取信号量许可时，对于公平信号量而言，如果当前线程不在CLH队列的头部，则排队等候；
		而对于非公平信号量而言，无论当前线程是不是在CLH队列的头部，它都会直接获取信号量。
		该差异具体的体现在，它们的tryAcquireShared()函数的实现不同
	(4).使用场景:在有限资源的场景下,比如数据库连接池的连接数
	(5).例子:
```java
	private final static int threadCount = 20;
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(3);
        for (int i = 1; i <= threadCount; i++) {
            final int count = i;
            executorService.execute(() -> {
                try{
                    // 写法1: 获取许可,执行方法,释放许可
                    semaphore.acquire();
                    test(count);
                    semaphore.release();

                    // 写法2:尝试获取许可,获取成功则执行方法;如果没有获取成功,则不丢弃;
                    // 尝试获取可以设置超时时间:tryAcquire(long timeout, TimeUnit unit)
                    if (semaphore.tryAcquire()){
                        test(count);
                        semaphore.release();
                    }
                }catch (Exception e){
                    log.error("exception", e);
                }
            });
        }
        executorService.shutdown();
    }
    private static void test(int count) throws Exception {
        Thread.sleep(1000);
        log.info("{}, {}", count, Thread.currentThread().getName());
    }
```
## 8.Condition
	(1).在使用 notify 和 notifyAll 方法进行通知时,被通知的线程是由JVM随机选择的.但是 ReentrantLock 集合
		Condition 类就实现选择性通知.线程可以注册在指定的 Condition 中,从而可以有选择性的进行线程通知
	(2).synchronized 就相当于整个 Lock 对象中只有一个单一的 Condition 对象,所有的线程都注册在它的一个
		对象上,线程开始 notifyAll 时,需要通知所有的 waitin线程,没有选择权;
		Object 中的 wait(),notify(),notifyAll()方法是和"同步锁"(synchronized关键字)捆绑使用的.
		而Condition是需要与"互斥锁"/"共享锁"捆绑使用的
	(3).函数列表:
		// 造成当前线程在接到信号或被中断之前一直处于等待状态。
		void await()
		// 造成当前线程在接到信号、被中断或到达指定等待时间之前一直处于等待状态。
		boolean await(long time, TimeUnit unit)
		// 造成当前线程在接到信号、被中断或到达指定等待时间之前一直处于等待状态。
		long awaitNanos(long nanosTimeout)
		// 造成当前线程在接到信号之前一直处于等待状态。
		void awaitUninterruptibly()
		// 造成当前线程在接到信号、被中断或到达指定最后期限之前一直处于等待状态。
		boolean awaitUntil(Date deadline)
		// 唤醒一个等待线程。
		void signal()
		// 唤醒所有等待线程。
		void signalAll()
## 9.LockSupport:
	是用来创建锁和其他同步类的基本线程阻塞原语.
	park() 和 unpark() 的作用分别是阻塞线程和解除阻塞线程,
	而且park()和unpark()不会遇到"Thread.suspend 和 Thread.resume所可能引发的死锁"问题.
	因为park() 和 unpark()有许可的存在;调用 park() 的线程和另一个试图将其 unpark() 的线程之间的竞争将保持活性.
	函数列表:
	// 返回提供给最近一次尚未解除阻塞的 park 方法调用的 blocker 对象，如果该调用不受阻塞，则返回 null。
	static Object getBlocker(Thread t)
	// 为了线程调度，禁用当前线程，除非许可可用。
	static void park()
	// 为了线程调度，在许可可用之前禁用当前线程。
	static void park(Object blocker)
	// 为了线程调度禁用当前线程，最多等待指定的等待时间，除非许可可用。
	static void parkNanos(long nanos)
	// 为了线程调度，在许可可用前禁用当前线程，并最多等待指定的等待时间。
	static void parkNanos(Object blocker, long nanos)
	// 为了线程调度，在指定的时限前禁用当前线程，除非许可可用。
	static void parkUntil(long deadline)
	// 为了线程调度，在指定的时限前禁用当前线程，除非许可可用。
	static void parkUntil(Object blocker, long deadline)
	// 如果给定线程的许可尚不可用，则使其可用。
	static void unpark(Thread thread)
	==> LockSupport 是通过调用 Unsafe 函数中的接口实现阻塞和解除阻塞的
	==> park和wait的区别:wait让线程阻塞前,必须通过synchronized获取同步锁; park 面向对象不同; 实现机制不一样,因此两者没有交集;
## 10.Callable & Future
	10.1.Callable 是类似于 Runnable 的接口，实现Callable接口的类和实现Runnable的类都是可被其它线程执行的任务。
		Callable 和 Runnable 有几点不同：
		(1).Callable规定的方法是call()，而Runnable规定的方法是run().
		(2).Callable的任务执行后可返回值，而Runnable的任务是不能返回值的。
		(3).call()方法可抛出异常，而run()方法是不能抛出异常的。
		(4).运行 Callable 任务可拿到一个 Future 对象,Future 表示异步计算的结果。
			它提供了检查计算是否完成的方法，以等待计算的完成，并检索计算的结果。
			通过Future对象可了解任务执行情况，可取消任务的执行，还可获取任务执行的结果.
	10.2.如果需要获取线程的执行结果,需要使用到Future,Callable用于产生结果,Future用于获取结果
		Callabl接口使用泛型来定义结果的返回值类型,在线程池提交Callable任务后返回了一个Future对象

## 11.FutureTask:
	(1).可用于异步获取执行结果或取消执行任务的场景.通过传入Runnable或者Callable的任务给FutureTask,
		直接调用其run方法或者放入线程池执行，之后可以在外部通过FutureTask的get方法异步获取执行结果.
		FutureTask非常适合用于耗时的计算，主线程可以在完成自己的任务后，再去获取结果.
		FutureTask还可以确保即使调用了多次run方法，它都只会执行一次Runnable或者Callable任务，
		或者通过cancel取消FutureTask的执行等;
	(2).FutureTask执行多任务计算:
		利用FutureTask和ExecutorService,可以用多线程的方式提交计算任务,主线程继续执行其他任务,
		当主线程需要子线程的计算结果时,在异步获取子线程的执行结果
```java
public class FutureTaskDemo {
    public static void main(String[] args) {
        FutureTaskDemo task = new FutureTaskDemo();
        List<FutureTask<Integer>> taskList = new ArrayList<FutureTask<Integer>>();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i =0;i<10;i++){
            FutureTask<Integer> f = new FutureTask<Integer>(task.new ComputeTask(i, "" + i));
            taskList.add(f);
            executorService.submit(f);
        }
        System.out.println("所有计算任务提交完毕,主线程做其他事情");
        Integer total = 0;
        for (FutureTask<Integer> t : taskList){
            try {
                total = total + t.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
        System.out.println("计算任务执行完毕,执行结果:" + total);

    }
    private class ComputeTask implements Callable<Integer> {
        private Integer result = 0;
        private String taskName = "";
        public ComputeTask(Integer result, String taskName) {
            this.result = result;
            this.taskName = taskName;
        }
        public String getTaskName() {
            return taskName;
        }
        public Integer call() throws Exception {
            for (int i = 0; i < 100; i++) {
                result = +i;
            }
            Thread.sleep(5000);
            System.out.println("子线程任务:" + taskName + " 计算完毕");
            return result;
        }
    }
}
```
	(3).高并发环境下,能够确保任务只执行一次:
		下面代码保证了在高并发环境下不会多次创建连接或者多次锁的出现
```java
private ConcurrentHashMap<String, FutureTask<Connection>> connectionPool
            = new ConcurrentHashMap<String, FutureTask<Connection>>();
    public Connection getConnection(String key) throws Exception{
        FutureTask<Connection> connectionTask = connectionPool.get(key);
        if (connectionTask != null){
            return connectionTask.get();
        }
        Callable<Connection> callable = new Callable<Connection>() {
            public Connection call() throws Exception {
                return createConnection();
            }
        };
        FutureTask<Connection> newTask = new FutureTask<Connection>(callable);
        connectionTask = connectionPool.putIfAbsent(key, newTask);
        if (connectionTask == null){
            connectionTask = newTask;
            connectionTask.run();
        }
        return connectionTask.get();
    }
```
## 12.Fork/Join框架
	设计思想与Mapreduce类似

## 13.Exchanger

# 四.并发容器:
## 1.并发容器类:
	(1).List 和 Set:
		CopyOnWriteArrayList, CopyOnWriteArraySet 和 ConcurrentSkipListSet
	(2).Map:
		ConcurrentHashMap 和 ConcurrentSkipListMap
	(3).Queue:
		ArrayBlockingQueue, LinkedBlockingQueue, LinkedBlockingDeque,
		ConcurrentLinkedQueue 和 ConcurrentLinkedDeque
	(4).同步容器与并发容器:
		同步容器将所有对容器状态的访问都串行化,以实现他们的安全性.这种方法严重降低了并发性.
## 2.CopyOnWriteArrayList:
	2.1.基本:相当于线程安全的 ArrayList,和 ArrayList 一样,是个可变数组;不同的是,具有以下几个特性:
		(1).最适合于应用程序:List 大小通常保持很小,只读操作远多于可变操作,需要在遍历期间防止线程间的冲突;
		(2).线程安全的
		(3).因为通常要复制整个基础数组,所以可变操作(add()、set() 和 remove() 等等)的开销很大.
		(4).迭代器支持hasNext(), next()等不可变操作，但不支持可变 remove()等操作;
		(5).使用迭代器进行遍历的速度很快,并且不会与其他线程发生冲突.在构造迭代器时,迭代器依赖于不变的数组快照;
	2.2.签名:
		public class CopyOnWriteArrayList<E>  implements List<E>, RandomAccess, Cloneable, Serializable{}
		(1).包含了成员lock.每一个CopyOnWriteArrayList都和一个互斥锁lock绑定,通过lock,实现了对CopyOnWriteArrayList的互斥访问
		(2).CopyOnWriteArrayList 本质上通过数组实现的
	2.3.实现原理:
		(1).动态数组:内部存在一个 volatile 数组来保存数据.在"添加/删除/修改"数据时,都会新建一个数组,
			并将更新后的数据拷贝到新建的数组中,最后再将该数组赋值给 volatile数组.
			由于它在“添加/修改/删除”数据时,都会新建数组,所以涉及到修改数据的操作,CopyOnWriteArrayList 效率很
			低;但是单单只是进行遍历查找的话,效率比较高;
		(2).线程安全:是通过volatile和互斥锁来实现的
			A.CopyOnWriteArrayList 是通过"volatile数组"来保存数据的;
			  一个线程读取volatile数组时,总能看到其它线程对该volatile变量最后的写入.
			  通过volatile提供了"读取到的数据总是最新的"这个机制的保证.
			B.通过互斥锁来保护数据.在"添加/修改/删除"数据时,会先"获取互斥锁",再修改完毕之后,
			  先将数据更新到"volatile数组"中,然后再"释放互斥锁"
## 3.CopyOnWriteArraySet:(HashSet)
	3.1.线程安全的无序的集合,可以将它理解成线程安全的HashSet.CopyOnWriteArraySet 和 HashSet 虽然都继承于共同的
		父类 AbstractSet;但是 HashSet 是通过 HashMap 来实现的,而 CopyOnWriteArraySet 是通过 CopyOnWriteArrayList
		来实现的.
		特性同 CopyOnWriteArrayList
	3.2.实现:
		(1).CopyOnWriteArraySet 继承于 AbstractSet,这就意味着它是一个集合;
		(2).CopyOnWriteArraySet 包含 CopyOnWriteArrayList 对象,它是通过 CopyOnWriteArrayList 实现的,
			而 CopyOnWriteArrayList 中允许有重复的元素,但是,CopyOnWriteArraySet 是一个集合不能有重复元素.
			CopyOnWriteArrayList 额外提供了addIfAbsent()和addAllAbsent()这两个添加元素的API,
			通过这些API来添加元素时,只有当元素不存在时才执行添加操作
## 4.ConcurrentHashMap:
	参考: /Java/Java源码解读/ConcurrentHashMap.java
## 5.ConcurrentSkipListMap:(TreeMap)
	线程安全的有序的哈希表
	* http://www.cnblogs.com/skywang12345/p/3498556.html
	(1).ConcurrentSkipListMap 和 TreeMap,它们虽然都是有序的哈希表;但是 ConcurrentSkipListMap 是线程安全的,
		TreeMap 是线程不安全的;另外 ConcurrentSkipListMap 是通过跳表来实现的,而 TreeMap 是通过红黑树实现的.
	==> 跳表:平衡树的一种替代的数据结构,和红黑树不相同的是,跳表对于树的平衡的实现是基于一种随机化的算法的,
		这样也就是说跳表的插入和删除的工作是比较简单的.
	(2).

## 6.ConcurrentSkipListSet: (TreeSet)
	* http://www.cnblogs.com/skywang12345/p/3498556.html

## 7.阻塞队列:
### 7.1.什么是阻塞队列:
	是一个在队列基础上又支持了两个附加操作的队列.
	2个附加操作:
	* 支持阻塞的插入方法:队列满时,队列会阻塞插入元素的线程,直到队列不满时;
	* 支持阻塞的移除方法:队列空时,获取元素的线程会等待队列变为非空;
### 7.2.应用场景:
	(1).常用于生产者与消费者:生产者是向队列中添加元素的线程,消费者是从队列中取元素的线程.
		简而言之:阻塞队列是生产者用来存放元素、消费者获取元素的容器;
	(2).如何使用阻塞队列来实现生产者消费者模型:
		通知模式:就是当生产者往满的队列里添加元素时会阻塞住生产者,当消费者消费了一个队
		列中的元素后,会通知生产者当前队列可用;
	(3).为什么BlockingQueue适合解决生产者消费者问题?
		任何有效的生产者-消费者问题解决方案都是通过控制生产者put()方法（生产资源）和
		消费者take()方法（消费资源）的调用来实现的,一旦你实现了对方法的阻塞控制,
		那么你将解决该问题.Java通过BlockingQueue提供了开箱即用的支持来控制这些
		方法的调用(一个线程创建资源，另一个消费资源).
		BlockingQueue是一种数据结构，支持一个线程往里存资源，另一个线程从里取资源;
	(4).实现:

### 7.3.几个方法
| 方法处理方式 | 抛出异常    |  返回特殊值  | 一直阻塞| 超时退出 |
| --------    | --------   | ----------- |--------|----------|
| 插入方法     | add(e)     | offer(e)    |put(e)  | offer(e,time,unit)|
| 移除方法     | remove     | poll()      |take()  | poll(time,unit)|
| 检查方法     | element()  | peek()      |不可用   | 不可用|

	这四类方法分别对应的是：
	(1).ThrowsException:如果操作不能马上进行,则抛出异常
	(2).SpecialValue:如果操作不能马上进行,将会返回一个特殊的值,一般是true或者false
	(3).Blocks:如果操作不能马上进行,操作会被阻塞
	(4).TimesOut:如果操作不能马上进行,操作会被阻塞指定的时间,如果指定时间没执行,
		则返回一个特殊值,一般是true或者false

### 7.4.Java的阻塞队列
#### 7.4.1.ArrayBlockingQueue:一个由数组结构组成的有界阻塞队列
	* http://www.cnblogs.com/skywang12345/p/3498652.html
	(1).此队列按照先进先出（FIFO）的原则对元素进行排序，但是默认情况下不保证线程公平的访问队列,
	即如果队列满了，那么被阻塞在外面的线程对队列访问的顺序是不能保证线程公平（即先阻塞，先插入）的

#### 7.4.2.LinkedBlockingQueue:一个由链表结构组成的有界阻塞队列
	* http://www.cnblogs.com/skywang12345/p/3503458.html
	(1).此队列按照先出先进的原则对元素进行排序
#### 7.4.3.PriorityBlockingQueue:支持优先级的无界阻塞队列
#### 7.4.4.DelayQueue:支持延时获取元素的无界阻塞队列,即可以指定多久才能从队列中获取当前元素
#### 7.4.5.SynchronousQueue不存储元素的阻塞队列
	每一个put必须等待一个take操作,否则不能继续添加元素。并且他支持公平访问队列
#### 7.4.6.LinkedTransferQueue:由链表结构组成的无界阻塞TransferQueue队列
	相对于其他阻塞队列,多了tryTransfer和transfer方法
#### 7.4.7.ConcurrentLinkedQueue:
	* http://www.cnblogs.com/skywang12345/p/3498995.html
#### 7.4.8.LinkedBlockingDeque:链表结构的双向阻塞队列,优势在于多线程入队时,减少一半的竞争

# 五.JUC 包核心与算法
## 1.AQS:AbstractQueuedSynchronizer,抽象队列同步器
[AbstractQueuedSynchronizer.java](https://github.com/chenlanqing/learningNote/blob/master/Java/Java%E6%BA%90%E7%A0%81%E8%A7%A3%E8%AF%BB/thread/AbstractQueuedSynchronizer.md)
## 2.CAS:Compare and Swap-比较与交换

* [非阻塞同步算法与CAS(Compare and Swap)无锁算法](http://www.cnblogs.com/Mainz/p/3546347.html)
* [Java CAS 和ABA问题](http://www.cnblogs.com/549294286/p/3766717.html、http://www.importnew.com/20472.html)
* [Unsafe与CAS](http://www.cnblogs.com/xrq730/p/4976007.html)
* [并发相关源码](http://www.cnblogs.com/xrq730/category/1021774.html)
* [CAS原理](https://blog.52itstyle.com/archives/948/)

	2.1.CAS:cpu指令,在大多数处理器架构,包括 IA32,Space 中采用的都是 CAS 指令.
		(1).CAS 语义:
			CAS 有3个操作数,内存值 V,旧的预期值 A, 要修改的新值 B,当且仅当预期值 A 和
			内存值 V 相同时,将内存值修改为 B 并返回 true,否则什么都不做并返回 false;
		(2).CAS 是乐观锁技术:
			当多个线程尝试使用CAS同时更新同一个变量时,只有其中一个线程能更新变量的值,
			而其它线程都失败,失败的线程并不会被挂起,而是被告知这次竞争中失败,并可以再次尝试.
			CAS 有3个操作数:内存值V,旧的预期值A,要修改的新值B.当且仅当预期值A和内存值V相同时,
			将内存值V修改为B,否则什么都不做.
		CAS 操作是基于共享数据不会被修改的假设.
	2.2.Java 中 CAS 的实现:
		伪代码:
		do{   
		       备份旧数据；  
		       基于旧数据构造新数据；  
		}while(!CAS( 内存地址，备份的旧数据，新数据 ))  
		JDK1.5 之前,需要编写明确的代码来执行CAS操作.在JDK1.5 之后,引入了底层的支持.并且JVM把它们
		编译为底层硬件提供的最有效的方法,在运行CAS的平台上,运行时把它们编译为相应的机器指令,
		如果处理器/CPU 不支持CAS指令,那么JVM将使用自旋锁;
		2.2.1.Unsafe 是 CAS 实现的核心类:
			(1).Java 无法直接访问底层操作系统,而是通过本地 native 方法来访问.不过 JVM 还是开了个后门,
				JDK 中有一个类 Unsafe,它提供了硬件级别的原子操作
				对于 Unsafe 类的使用都是受限制的,只有授信的代码才能获得该类的实例
			(2).对 CAS 的实现:
```java	
/*		
compareAndSwap方法的参数含义:
第一个参数:要修改的参数
第二个参数:对象中要修改变量的偏移量
第三个参数:修改之前的值
第四个参数:预想修改后的值
*/
public final native boolean compareAndSwapObject(Object paramObject1, long paramLong, Object paramObject2, Object paramObject3);
public final native boolean compareAndSwapInt(Object paramObject, long paramLong, int paramInt1, int paramInt2);
public final native boolean compareAndSwapLong(Object paramObject, long paramLong1, long paramLong2, long paramLong3);
```
			(3).可以查看原子类的实现,比如:AtomicInteger#addAndGet 方法的实现:
				==> JDK7:在addAndGet作一部分操作,然后调用compareAndSet,由该方法调用 Unsafe#getAndAddInt
					public final int addAndGet(int delta) {
					    for (;;) {
					        int current = get();
					        int next = current + delta;
					        if (compareAndSet(current, next))
					            return next;
					    }
					}
					public final boolean compareAndSet(int expect, int update) {
				        return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
				    }
				==> JDK8:直接调用 Unsafe#getAndAddInt
					public final int addAndGet(int delta) {
				        return unsafe.getAndAddInt(this, valueOffset, delta) + delta;
				    }
		2.2.2.Unsafe 方法实现:使用 C++ 来实现的.
			注意:对应于windows操作系统,X86 处理器
			sun.misc.Unsafe 类的compareAndSwapInt()方法的源代码
			public final native boolean compareAndSwapInt(Object o, long offset, int expected, int x);
			该方法是本地方法,这个本地方法在openjdk中依次调用的c++代码主要有三个文件:
			openjdk/openjdk/hotspot/src/share/vm/prims/unsafe.cpp
			openjdk/openjdk/hotspot/src/share/vm/runtime/atomic.cpp
			openjdk/openjdk/hotspot/src/os_cpu/windows_x86/vm/atomic_windows_x86.inline.hpp
			对应部分源码片段:
```c
inline jint Atomic::cmpxchg(jint exchange_value, volatile jint* dest, jint compare_value) {
	// alternative for InterlockedCompareExchange
	int mp = os::is_MP();
	__asm {
	mov edx, dest
	mov ecx, exchange_value
	mov eax, compare_value
	LOCK_IF_MP(mp)
	cmpxchg dword ptr [edx], ecx
	}
}
```
			==> 如上面源代码所示,程序会根据当前处理器的类型来决定是否为cmpxchg指令添加lock前缀.
				如果程序是在多处理器上运行,就为cmpxchg指令加上lock前缀(lock cmpxchg)
			==> lock前缀说明:
				Ⅰ.确保对内存的读-改-写操作原子执行
				Ⅱ.禁止该指令与之前和之后的读和写指令重排序
				Ⅲ.把写缓冲区中的所有数据刷新到内存中
	2.3.CAS 使用场景:
		(1).原子类的实现;
		(2).AbstractQueuedSynchronizer(AQS)
	2.4.CAS 缺点:
		2.4.1.ABA 问题:
			// http://www.cnblogs.com/549294286/p/3766717.html
			(1)问题:在运用CAS做Lock-Free 操作中有一个经典的ABA问题.
				线程1准备用CAS将变量的值由A替换为B,在此之前,线程2将变量的值由A替换为C,又由C替换为A,然后线程1执行CAS时发现变量的值仍然为A，所以CAS成功.
				但实际上这时的现场已经和最初不同了,尽管CAS成功,但可能存在潜藏的问题
			(2).解决思路是:每次变量更新的时候把变量的版本号加 1,那么 A-B-A 就会变成 A1-B2-A3,只要变量被某一线程修改过,改变量对应的版本号就会发生递增变化.
				==> 可以参考:AtomicStampedReference#compareAndSet 方法:
					public boolean compareAndSet(V expectedReference, V newReference, int expectedStamp, int newStamp) {
					    Pair<V> current = pair;
					    return expectedReference == current.reference && expectedStamp == current.stamp &&
					            ((newReference == current.reference && newStamp == current.stamp) || casPair(current, Pair.of(newReference, newStamp)));
					}
				该类检查了当前引用与当前标志是否与预期相同,如果全部相等,才会以原子方式将该引用和该标志的值设为新的更新值
		2.4.2.CPU 开销较大:
			在并发量比较高的情况下,如果许多线程反复尝试更新某一个变量,却又一直更新不成功,循环往复,会给CPU带来很大的压力;
			主要是自旋CAS操作如果长时间不成功,会给CPU带来非常大的执行开销.
		2.4.3.不能保证代码块的原子性:
			CAS 机制所保证的只是一个变量的原子性操作,而不能保证整个代码块的原子性.比如需要保证3个变量共同进行原子性的更新.就不得不使用 synchronized;
	2.5.CAS 与 synchronized 的区别:
		(1).synchronized 关键字会让没有得到锁资源的线程进入 BLOCKED 状态,而后在争夺到锁资源后恢复为 RUNNABLE 状态,
			这个过程中涉及到操作系统用户模式和内核模式的转换,代价比较高;
			尽管Java1.6为Synchronized做了优化,增加了从偏向锁到轻量级锁再到重量级锁的过度,但是在最终转变为重量级锁之后,性能仍然较低.
		(2).从锁的分类来看,CAS 属于乐观锁,乐观地认为程序中的并发情况不那么严重,所以让线程不断去尝试更新;
			而 synchronized 属于悲观锁,悲观地认为程序中的并发情况严重,所以严防死守;
		(3).两者没有绝对的好坏,关键看使用场景.在1.6版本后,synchronized 变为重量级锁之前也是采用 CAS 机制.
		(4).使用CAS在线程冲突严重时,会大幅降低程序性能;CAS只适合于线程冲突较少的情况使用

# 六.线程池
参考文章:
* [线程池的使用](http://www.cnblogs.com/dolphin0520/p/3932921.html)
* [线程池原理](http://www.cnblogs.com/cm4j/p/thread-pool.html)
* [ThreadPoolExecutor源码分析](https://mp.weixin.qq.com/s/vVFbVZUqSsTdoAb9Djvk5A)
* [Java线程池设计思想及源码解读](https://javadoop.com/2017/09/05/java-thread-pool/?hmsr=toutiao.io&utm_medium=toutiao.io&utm_source=toutiao.io)

## 1.线程池技术:
	1.1.为什么使用线程池:
		(1).在多线程技术中,线程的创建和销毁很消耗时间,因为创建线程需要获取内存资源或者其他更多的资源.提高效率就是减少线程的创建和销毁次数.
			可以利用已有线程来解决这个问题,这就是池化技术产生的原因.就如同数据库连接池一样
		(2).线程池真正关注的点：如何缩短创建线程的时间和销毁线程的时间;
		(3).可有效的控制最大并发线程数,提高系统资源利用率,同时可以避免过多资源竞争,避免阻塞;
		(4).提供定时执行\定期执行\单线程\并发控制数等功能;
	1.2.什么是线程池：
		线程池是一种多线程处理方法，处理过程中将任务添加到队列，然后在创建线程后自动启动这些任务。
	1.3.应用范围：
		(1).需要大量线程来完成的任务,且完成任务时间较短,如web服务完成网页请求这样的任务.但是对于长时间的任务,比如一个ftp连接请求.
		(2).对性能要求苛刻的应用,比如要求服务器迅速响应客户请求.
		(3).接受突发性的大量请求,但不至于使服务器因此产生大量线程的应用
	1.4.如何设计一个线程池:
		1.4.1.基本组成部分:
			(1).线程池管理器:用于创建并管理线程池,包含c黄金线程池,销毁线程池,添加新任务等功能
			(2).工作线程:线程池中的线程;
			(3).任务接口:每个任务必须实现的接口,以供工作线程调度任务执行;
			(4).任务队列:用于存放没有处理的任务,提供一种缓存机制;
	1.5.线程池原理:
		预先启动一些线程,线程无限循环从任务队列中获取一个任务进行执行,直到线程池被关闭.如果某个线程因为执行某个任务发生异常而终止,
		那么重新创建一个新的线程而已.如此反复.线程池的实现类是 ThreadPoolExecutor 类
## 2.重要类:
### 2.1.ExecutorService:真正的线程池接口
### 2.2.ScheduledExecutorService:和Timer/TimerTask类似，解决那些需要任务重复执行的问题
### 2.3.ThreadPoolExecutor:ExecutorService的默认实现,线程池中最核心的一个类:
#### 2.3.1.核心参数:
	(1).corePoolSize 核心线程数大小，当线程数<corePoolSize,会创建线程执行runnable
	(2).maximumPoolSize 最大线程数， 当线程数 >= corePoolSize的时候,会把runnable放入workQueue中
		largestPoolSize:记录了曾经出现的最大线程个数
	(3).keepAliveTime 保持存活时间，当线程数大于corePoolSize的空闲线程能保持的最大时间。
	(4).unit 时间单位
	(5).workQueue 保存任务的阻塞队列
	(6).threadFactory 创建线程的工厂
	(7).handler 拒绝策略,默认有四种拒绝策略
#### 2.3.2.参数关系:
	(1).corePoolSize 与 maximumPoolSize:
		* 如果线程池中的实际线程数 < corePoolSize, 新增一个线程处理新的任务;
		* 如果线程池中的实际线程数 >= corePoolSize, 新任务会放到workQueue中;
		* 如果阻塞队列达到上限,且当前线程池的实际线程数 < maximumPoolSize,新增线程来处理任务;
		* 如果阻塞队列满了,且这时线程池的实际线程数 >= maximumPoolSize,那么线程池已经达到极限,
			会根据拒绝策略RejectedExecutionHandler拒绝新的任务.
	(2).如果线程池阻塞队列达到极限时,在运行一段时间后,阻塞队列中的任务执行完成了,线程池会将超过核心
		线程数的线程在一段时间内自动回收,在秒杀的业务场景中会有这样的情况发生.
#### 2.3.3.任务执行顺序:
![image](https://github.com/chenlanqing/learningNote/blob/master/Java/JavaSE/多线程/image/线程池主要处理流程.png)

	(1).一个任务提交,如果线程池大小没达到corePoolSize，则每次都启动一个worker也就是一个线程来立即执行;(执行这个步骤时需要获取全局锁)
	(2).如果来不及执行，则把多余的线程放到workQueue，等待已启动的worker来循环执行;
	(3).如果队列workQueue都放满了还没有执行，则在maximumPoolSize下面启动新的worker来循环执行workQueue;
	(4).如果启动到maximumPoolSize还有任务进来，线程池已达到满负载，此时就执行任务拒绝RejectedExecutionHandler
		线程池核心代码:
```java
		public void execute(Runnable command) {
			if (command == null)
				throw new NullPointerException();
			int c = ctl.get();
			// 判断当前线程数是否小于 corePoolSize,如果是,使用入参任务通过 addWork方法创建一个新的线程.
			// 如果能完成新线程的创建execute方法结束,成果提交任务.
			if (workerCountOf(c) < corePoolSize) {
				if (addWorker(command, true))// true表示会再次检查workCount是否小于corePoolSize
					return;
				c = ctl.get();
			}
			// 如果上面没有完成任务提交;状态为运行并且能发成功加入任务到工作队列后,在进行一次check,如果状态在任务
			// 加入了任务队列后变为非运行(可能线程池被关闭了),非运行状态下当然需要reject;
			// 然后在判断当前线程数是否为0,如果是,新增一个线程;
			if (isRunning(c) && workQueue.offer(command)) {
				int recheck = ctl.get();
				if (! isRunning(recheck) && remove(command))
					reject(command);
				else if (workerCountOf(recheck) == 0)
					addWorker(null, false);
			}
			// 如果任务不能加入到工作队列,将尝试使用任务增加一个线程,如果失败,则是线程池已经shutdown或者线程池已经
			// 达到饱和状态,所以reject这个任务.
			else if (!addWorker(command, false))
				reject(command);
		}

```
### 2.4.ScheduledThreadPoolExecutor:
	继承ThreadPoolExecutor的ScheduledExecutorService接口实现，周期性任务调度的类实现


## 3.线程池配置
### 3.1.不同业务场景如何配置线程池参数
	CPU密集型任务:需要尽量压榨CPU,参考值可以设为NCPU + 1;
	IO密集型任务:参考值可以设置为 2*NCPU
### 3.2.科学设置线程池:
	(1).如果需要达到某个QPS,使用如下计算公式:
		设置的线程数 = 目标QPS / (1 / 任务实际处理时间)
		假设目标QPS=100, 任务的实际处理时间 0.2s, 100 * 0.2 = 20个线程,这里的20个线程必须对应物理的20个CPU核心,
		否则不能达到预估的QPS目标.
	(2).如果IO任务较多,使用阿姆达尔定律来计算:
		设置的线程数 = CPU 核数 * (1 + io/computing)
		假设4核 CPU,每个任务中的 IO 任务占总任务的80%, 4 * (1 + 4) = 20个线程,这里的20个线程对应的是4核心的 CPU.
	(3).线程队列大小的设置:按照目标响应时间计算队列大小
		队列大小 = 线程数 * (目标相应时间/任务实际处理时间)
		假设目标相应时间为0.4s,计算阻塞队列的长度为20 * (0.4 / 0.2) = 40
## 4.线程池最佳实践
	(1).线程池的使用要考虑线程最大数量和最小数最小数量.
	(2).对于单部的服务,线程的最大数量应该等于线程的最小数量,而混布的服务,
		适当的拉开最大最小数量的差距，能够整体调整 CPU 内核的利用率.
	(3).线程队列大小一定要设置有界队列,否则压力过大就会拖垮整个服务.
	(4).必要时才使用线程池,须进行设计性能评估和压测.
	(5).须考虑线程池的失败策略,失败后的补偿.
	(6).后台批处理服务须与线上面向用户的服务进行分离.

# 七.多线程并发最佳实践
	1.使用本地变量;
	2.使用不可变类
	3.最小化锁的作用域范围:S = 1 / (1 - a + a/n)
	4.使用线程池,而不是直接new Thread执行;
	5.宁可使用同步也不要使用线程的wait和notify(可以使用CountDownLatch)
	6.使用BlockingQueue实现生产-消费模式
	7.使用并发集合而不是加了锁的同步集合;
	8.使用Semaphore创建有界的访问;
	9.宁可使用同步代码块也不要使用同步方法(synchronized)
	10.避免使用静态变量,如果一定要用静态变量,可以声明为 final
