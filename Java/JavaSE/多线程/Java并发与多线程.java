/**
 * 参考资料:
 * 	http://ifeve.com/java-concurrency-thread-directory/
 *  线程池:http://www.importnew.com/19011.html
 * 
 */
一.并发与多线程简介:
1.多线程优点:
	(1).资源利用率更好:文件读写操作
	(2).程序设计在某些情况下更简单:
	(3).程序响应更快:端口监听操作
2.多线程的代价:
	(1).设计更复杂:多线程共享数据时尤其需要注意
	(2).上下文切换的开销:	
		CPU 会在一个上下文中执行一个线程,然后切换到另外一个上下文中执行另外一个线程
		上下文切换并不廉价。如果没有必要，应该减少上下文切换的发生
	(3).增加资源消耗:如线程管理中消耗的资源
3.并发编程模型:
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
二.Java 多线程:
1.Java 线程类是继承自 java.lang.Thread 或其子类的,
	Thread thread = new Thread();
	thread.start();// 调用线程
2.线程的创建:
	2.1.创建线程的方式:
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
	2.3.创建 Thread 子类还是实现 Runnable 接口?
		实现 Runnable 接口,线程池可以有效的管理实现了 Runnable 接口的线程
		如果多个线程正在运行中,如果某个线程调用 System.exit()指示终结程序，那么全部的线程都会结束执行
	2.4.常见错误问题:
		调用run()方法而非start()方法
	2.5.线程名称:
		当创建一个线程的时候，可以给线程起一个名字
		MyRunnable runnable = new MyRunnable();
		Thread thread = new Thread(runnable, "New Thread");
		thread.start();
		System.out.println(thread.getName());
		也可以通过:
		Thread.currentThread()获取当前线程的引用
	2.6.Thread 的部分属性:
		(1).ID: 每个线程的独特标识。
		(2).Name: 线程的名称。
		(3).Priority: 线程对象的优先级。优先级别在1-10之间，1是最低级，10是最高级。
			不建议改变它们的优先级，但是你想的话也是可以的。
		(4).Status: 线程的状态。在Java中，线程只能有这6种中的一种状态： 
			new{}, runnable, blocked, waiting, time waiting, 或 terminated.
	2.7.线程的中断与停止,暂停:
		2.7.1.线程中断:Java 提供中断机制来通知线程表明我们想要结束它.中断机制的特性是线程需要检查是否被中断,
			而且还可以决定是否响应结束的请求
		2.7.2.线程停止:
			2.7.2.1.线程停止的方法:
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
	2.8.线程的睡眠:
		Thread.sleep()
		TimeUnit.SECONDS.sleep();
	2.9.等待线程的终结:
		Thread.join():当前线程调用某个线程的这个方法时，它会暂停当前线程，直到被调用线程执行完成
		Thread.join(long miseconds);	这方法让调用线程等待特定的毫秒数。
		Thread.join(long milliseconds, long nanos)第二个版本的join方法和第一个很像,只不过它接收一个毫秒数和一个纳秒数
	2.10.守护线程:守护线程优先级非常低,通常在程序里没有其他线程运行时才会执行;
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
		
	2.11.异常处理:
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
	2.12.本地线程变量:ThreadLocal(详情查看第 9 点)
		(1).本地线程变量为每个使用这些变量的线程储存属性值。可以用 get() 方法读取值和使用 set() 方法改变值
	2.13.线程组:可以把线程分组
		Java 提供 ThreadGroup 类来组织线程.
		ThreadGroup 对象可以由 Thread 对象组成和由另外的 ThreadGroup 对象组成,生成线程树结构
	2.14.用线程工厂创建线程:线程对象工厂 ThreadFactory
	2.15.join()和yield()方法:
		2.15.1.join()方法:
			(1).等待该线程终止,指的是主线程等待子线程的终止,子线程调用了join()方法后面的代码,
				只有等到子线程结束了才能执行;
				让调用该方法的thread完成 run 方法的里面的东西后,在执行 join 方法后面的代码
				当前运行着的线程将阻塞直到这个线程实例完成了执行
			(2).join 具有使线程排队运行的作用.join 与 synchronized 区别:
				join 内部是 wait 方法进行等待,而 synchronized 关键字使用的是"对象监视器"原理作为同步
			(3).在执行 join 的过程中,如果当前线程对象被中断,则当前线程出现异常;
			(3).join(long) 内部是使用 wait(long)方法来实现的,所以join(long)方法具有释放锁的特点,
				而 sleep(long)不释放锁的
		2.15.2.yield()方法:
			(1).使当前线程从执行状态(运行状态)变为可执行状态(就绪状态),调用yield的时候锁并没有被释放
				放弃当前的CPU资源,将它让给其他的任务去占用CPU执行时间,放弃的时间不确定
				将CPU让给其他资源导致速度变慢,一般是把机会给到线程池拥有相同优先级的线程
		2.15.3.两者的区别:
			(1).join 是 final 的实例方法,yield是原生静态方法
	2.16.线程的优先级:
		(1).线程的优先级具有继承性,比如A线程启动B线程,则B线程的优先级与A是一样的;
		(2).优先级具有规则性:
			线程的优先级与代码执行顺序无关,CPU 尽量将执行资源让给优先级比较高的线程
			高优先级的线程总是大部分先执行完的,但不带表高优先级的线程全部不执行完;
		(3).优先级具有随机性:
			也就是优先级较高的线程不一定每次都先执行完
			不要把线程的优先级与运行结果的顺序作为衡量的标准,线程优先级与打印顺序无关
3.竞态条件与临界区:
	3.1.在同一程序中运行多个线程本身不会导致问题,问题在于多个线程访问了相同的资源:
		如果多个线程对这些相同资源进行了"写操作"才会引发线程安全问题;
	3.2.竞态条件:
		当两个线程竞争同一资源时，如果对资源的访问顺序敏感，就称存在竞态条件
	3.3.临界区:
		致竞态条件发生的代码区称作临界区
4.线程安全与共享资源:
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
5.synchronized 关键字:
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
6.synchronized 同步块:
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

7.synchronized 底层实现及 synchronized 锁优化.
	参考文章:
		http://blog.csdn.net/shandian000/article/details/54927876
		http://www.cnblogs.com/paddix/p/5367116.html
		http://www.cnblogs.com/javaminer/p/3889023.html
	可以通过反编译字节码 -->javap -c SyncDemo.class
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
		(1).适应性自旋(Adaptive Spinning):从轻量级锁的获取过程中知道,当前线程在获取轻量级锁的过程中执行 CAS 操作失败时,
			是要通过自旋来获取重量级锁.问题是:自旋是需要消耗CPU的,如果一直获取不到锁,那么该线程就一直处在自旋状态.
			解决该问题最简单的方法是指定自旋的次数.但是JDK采用了更合适的方法-适应性自旋.
			简单来说就是如果自旋成功了,那么下次自旋的次数会更多,如果自旋失败了,则自旋的次数就会减少.
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
8.volatile(http://www.cnblogs.com/dolphin0520/p/3920373.html)
	8.1.volatile 关键字用于表示可以被多个线程异步修改的成员变量,强制从公共堆栈中取得变量的值,不是从私有线程获取
			volatile 关键字在许多 Java 虚拟机中都没有实现.
			volatile 的目标用途是为了确保所有线程所看到的指定变量的值都是相同的;
			Java 语言中的 volatile 变量可以被看作是一种 “程度较轻的 synchronized”:与 synchronized 块相比,
			volatile 变量所需的编码较少,并且运行时开销也较少,但是它所能实现的功能也仅是 synchronized 的一部分
			缓存一致性问题:	
	8.2.volatile关键字的两层语义:一旦一个共享变量(类的成员变量、类的静态成员变量)被volatile修饰之后，那么就具备了两层语义：		
		(1).保证了不同线程对这个变量进行操作时的可见性，即一个线程修改了某个变量的值，这新值对其他线程来说是立即可见的;
		(2).禁止进行指令重排序
			//线程1
			boolean stop = false;
			while(!stop){
			    doSomething();
			}			 
			//线程2
			stop = true;
			如果要线程1和线程2正确执行,用volatile修饰 stop 变量:
			第一：使用volatile关键字会强制将修改的值立即写入主存；
			第二：使用volatile关键字的话，当线程2进行修改时，会导致线程1的工作内存中缓存变量stop的缓存行无效
			(反映到硬件层的话，就是CPU的L1或者L2缓存中对应的缓存行无效)；
			第三：由于线程1的工作内存中缓存变量stop的缓存行无效，所以线程1再次读取变量stop的值时会去主存读取。
		8.2.1.可见性:当一个共享变量被volatile修饰时,它会保证修改的值会立即被更新到主存,
			当有其他线程需要读取时,它会去内存中读取新值;多个线程共享数据
			另外:通过 synchronized 和 Lock 也能够保证可见性，synchronized 和Lock 能保证同一时刻只有一个线程获取锁然
			后执行同步代码，并且在释放锁之前会将对变量的修改刷新到主存当中。因此可以保证可见性
			==>解决异步死循环:
				JVM 在设置 -server 时出现死循环:一个变量存在与公共堆栈中和线程的私有堆栈中.JVM 被设置了-server
				是为了线程运行的效率,线程一直在私有堆栈中获取变量的值,而调用 set 方法时虽然被执行了,但是更新的
				却是公共堆栈中的变量值;
				造成这样的原因就是私有堆栈中的值和公共堆栈的值不同步造成的
	8.3.volatile保证原子性吗? volatile也无法保证对变量的任何操作都是原子性的
		JVM 只保证从主内存中加载到工作内存的值是最新的;
		(1).原子性:在Java中，对基本数据类型的变量的读取和赋值操作是原子性操作,即这些操作是不可被中断的,
			要么执行，要么不执行
			x = 10;         //语句1,原子性操作
			y = x;         //语句2
			x++;           //语句8
			x = x + 1;     //语句4
			上述四句那些是原子操作?
			①.其实只有语句1是原子性操作，其他三个语句都不是原子性操作。
			②.语句1是直接将数值10赋值给x，也就是说线程执行这个语句的会直接将数值10写入到工作内存中
			③.语句2实际上包含2个操作，它先要去读取x的值，再将x的值写入工作内存，虽然读取x的值以及 将x的值写入工作内存 
				这2个操作都是原子性操作，但是合起来就不是原子性操作了。
			④.同样的，x++和 x = x+1包括3个操作：读取x的值，进行加1操作，写入新的值。
			所以上面4个语句只有语句1的操作具备原子性
		(2).自增操作不是原子性操作，而且volatile也无法保证对变量的任何操作都是原子性的
			在java 1.5的 java.util.concurrent.atomic 包下提供了一些原子操作类,即对基本数据类型的 自增(加1操作)，
			自减(减1操作)、以及加法操作(加一个数)，减法操作(减一个数)进行了封装，保证这些操作是原子性操作。
			atomic是利用 CAS 来实现原子性操作的(Compare And Swap)，CAS 实际上是利用处理器提供的 CMPXCHG 指令实现的，
			而处理器执行 CMPXCHG 指令是一个原子性操作;
		(3).原子类也不一定是安全的, addAndGet() (或者其他方法)是原子的,但是方法与方法之间的调用却不是原子的.
			所以也需要使用同步来解决
	8.4.volatile能保证有序性吗? volatile关键字能禁止指令重排序，所以volatile能在一定程度上保证有序性
		8.4.1.有序性:在Java内存模型中，允许编译器和处理器对指令进行重排序，但是重排序过程不会影响到单线程程序的执行，
			却会影响到多线程并发执行的正确性
		8.4.2.volatile关键字禁止指令重排序有两层意思:
			(1).当程序执行到volatile变量的读操作或者写操作时，在其前面的操作的更改肯定全部已经进行，
				且结果已经对后面的操作可见;	在其后面的操作肯定还没有进行；
			(2).在进行指令优化时，不能将在对volatile变量访问的语句放在其后面执行，也不能把volatile变量
				后面的语句放到其前面执行
	8.5.volatile的原理和实现机制
		“观察加入volatile关键字和没有加入volatile关键字时所生成的汇编代码发现，加入volatile关键字时，
		会多出一个lock前缀指令”
	　　lock前缀指令实际上相当于一个内存屏障(也成内存栅栏)，内存屏障会提供8个功能：
	　　(1).它确保指令重排序时不会把其后面的指令排到内存屏障之前的位置，也不会把前面的指令排到内存屏障的后面；
			即在执行到内存屏障这句指令时，在它前面的操作已经全部完成；
	　　(2).它会强制将对缓存的修改操作立即写入主存；
	　　(3).如果是写操作，它会导致其他CPU中对应的缓存行无效。
	8.6.使用volatile关键字的场景:
		通常来说，使用volatile必须具备以下2个条件
		(1).对变量的写操作不依赖于当前值;
		(2).该变量没有包含在具有其他变量的不变式中;
		使用场景:
			多个线程中可以感知实例变量被更改了,并且可以获得最新的值使用
	8.7.volatile 和 synchronized 的区别:
		(1).volatile 不会进行加锁操作
			volatile变量是一种稍弱的同步机制在访问volatile变量时不会执行加锁机制,因此也就不会使执行线程阻塞,
			因此volatile变量是一种比 synchronized 关键字更轻量级的同步机制;
		(2).volatile 是线程同步的轻量级实现,所以 volatile 性能上比 synchronized 的要好;volatile 只能修饰变量,
			而 synchronized 可以修饰方法以及代码块;后续JDK新版本中 synchronized 关键字在执行效率上有明显提升;
		(3).多线程访问 volatile 变量不会发生阻塞,而 synchronized 会出现阻塞;
		(4).volatile 能保证数据的可见性,但不能保证原子性;而 synchronized 可以保证原子性,也可以间接保证可见性,
			它会将私有内存和公共内存中的数据作同步;
		(5).volatile 解决的是变量在多个线程之间的可见性,不具备同步性和原子性;
			而 synchronized 解决的是多个线程之间访问资源的同步性;

9.线程安全及不可变性:
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

10.线程通信:目标是使线程间能够互相发送信号.另一方面,线程通信使线程能够等待其他线程的信号
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
	10.4.通过管道进行线程间通信:字节流
		一个线程发送数据到输出管道,另一个线程从输入管道中读取数据,实现不同线程间通信
		使用代码 inputStream.connect(outputStream) 或者 outputStream.connect(inputStream) 的作用
		使两个Stream之间产生通信链接,这样才可以将数据进行输出与输入

	8.3.wait(),notify()和notifyAll():java.lang.Object 类定义了三个方法
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
	8.4.丢失的信号(Missed Signals):
		(1).notify()和notifyAll()方法不会保存调用它们的方法，因为当这两个方法被调用时,
			有可能没有线程处于等待状态,通知信号过后便丢弃了,
			如果一个线程先于被通知线程调用wait()前调用了notify()，等待的线程将错过这个信号
	8.5.假唤醒:
		(1).由于莫名其妙的原因,线程有可能在没有调用过notify()和notifyAll()的情况下醒来.这就是所谓的假唤醒(spurious wakeups)
		(2).为了防止假唤醒，保存信号的成员变量将在一个while循环里接受检查,而不是在if表达式里.这样的一个while循环叫做自旋锁
			注意:这种做法要慎重,目前的JVM实现自旋会消耗CPU,如果长时间不调用doNotify方法,doWait方法会一直自旋,CPU会消耗太大
	8.6.多个线程等待相同信号:
	8.7.不要在字符串常量或全局对象中调用wait()
		在wait()/notify()机制中，不要使用全局对象，字符串常量等。应该使用对应唯一的对象
9.ThreadLocal 类:
	存放每个线程的共享变量,解决变量在不同线程间的隔离性
	/**
	 * 参考资料
	 * http://www.importnew.com/14398.html
	 * http://www.importnew.com/16112.html
	 * http://www.importnew.com/17849.html
	 */
	9.1.创建 ThreadLocal 对象:private ThreadLocal myThreadLocal = new ThreadLocal();
		每个线程仅需要实例化一次即可
		虽然不同的线程执行同一段代码时,访问同一个ThreadLocal变量，但是每个线程只能看到私有的ThreadLocal实例
	9.2.访问 ThreadLocal 对象:
		(1).一旦创建了一个ThreadLocal对象，你就可以通过以下方式来存储此对象的值:
			myThreadLocal.set("a thread local value");
		(2).可以直接读取一个 ThreadLocal 对象的值:
			String threadLocalValue = (String) myThreadLocal.get();
	9.3.ThreadLocal 泛型:
		可以创建一个泛型化的ThreadLocal对象,当你从此ThreadLocal实例中获取值的时候，就不必要做强制类型转换
		private ThreadLocal myThreadLocal1 = new ThreadLocal<String>();
	9.4.初始化 ThreadLocal:
		(1).由于ThreadLocal对象的set()方法设置的值只对当前线程可见,那有什么方法可以为
			ThreadLocal 对象设置的值对所有线程都可见?
			通过 ThreadLocal 子类的实现，并覆写initialValue()方法，就可以为 ThreadLocal 对象指定一个初始化值
			private ThreadLocal myThreadLocal = new ThreadLocal<String>() {
			   @Override 
			   protected String initialValue() {
			       return "This is the initial value";
			   }
			};
	9.5.完整的 ThreadLocal 实例:
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
	9.6.InheritableThreadLocal:ThreadLocal的子类。为了解决ThreadLocal实例内部每个线程都只能看到自己的私有值，
		所以 InheritableThreadLocal 允许一个线程创建的所有子线程访问其父线程的值
10.深入理解 ThreadLocal:
	10.1.理解 ThreadLocal:
		ThreadLocal 在每个线程中对该变量会创建一个副本,即每个线程内部都会有一个该变量,且在线程内部任何地方都可以使用,
		线程之间互不影响，这样一来就不存在线程安全问题,也不会严重影响程序执行性能;
	10.2.深入解析 ThreadLocal 类:
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
	10.3.ThreadLocal 的应用场景:
		(1).最常见的 ThreadLocal 使用场景为 用来解决 数据库连接、Session 管理等
11.死锁:两个或更多线程阻塞着等待其它处于死锁状态的线程所持有的锁
	11.1.死锁通常发生在多个线程同时但以不同的顺序请求同一组锁的时候
	11.2.死锁程序:
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
	11.3.避免死锁:
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
	11.4.监测是否有死锁现象:
		(1).执行 jps 命令,可以得到运行的线程 3244,再执行 jstack命令
		jstack -l 3244;
12.饥饿和公平:
	12.1.饥饿:如果一个线程因为CPU时间全部被其他线程抢走而得不到CPU运行时间，这种状态被称之为“饥饿”
		解决饥饿的方案被称之为“公平性” – 即所有线程均能公平地获得运行机会
	12.2.Java 中导致饥饿的原因:
		(1).高优先级线程吞噬所有的低优先级线程的CPU时间:
			优先级越高的线程获得的CPU时间越多，线程优先级值设置在1到10之间，而这些优先级值所表示行为的准确解释则
			依赖于你的应用运行平台,对大多数应用来说,你最好是不要改变其优先级值;
		(2).线程被永久堵塞在一个等待进入同步块的状态,因为其他线程总是能在它之前持续地对该同步块进行访问:
		(3).线程在等待一个本身也处于永久等待完成的对象(比如调用这个对象的wait方法)
			如果多个线程处在wait()方法执行上，而对其调用notify()不会保证哪一个线程会获得唤醒，
			任何线程都有可能处于继续等待的状态;
	12.3.在Java中实现公平性方案:
		(1).使用锁，而不是同步块:为了提高等待线程的公平性，我们使用锁方式来替代同步块
		(2).公平锁。
		(3).注意性能方面
	
13.锁:
	(1).同步锁:通过synchronized关键字来进行同步
		同步锁的原理是,对于每一个对象,有且仅有一个同步锁:不同的线程能共同访问该同步锁.
		但是,在同一个时间点,该同步锁能且只能被一个线程获取到
	(2).JUC包中的锁，包括:Lock 接口,ReadWriteLock 接口,LockSupport 阻塞原语,Condition 条件,
		AbstractOwnableSynchronizer/AbstractQueuedSynchronizer/AbstractQueuedLongSynchronizer 三个抽象类,
		ReentrantLock 独占锁,ReentrantReadWriteLock 读写锁.由于 CountDownLatch，CyclicBarrier 和 Semaphore 也是通过AQS来实现的

14.闭锁:CountDownLatch 、栅栏:CyclicBarrier、信号量:Semaphore 
	14.1.CountDownLatch:
		(1).是一个同步辅助类,在完成一组正在其他线程中执行的操作之前,它允许一个或多个线程一直等待
		// http://www.cnblogs.com/skywang12345/p/3533887.html
		CountDownLatch 包含了sync对象,sync是 Sync 类型.CountDownLatch 的 Sync 是实例类,它继承于 AQS
	14.2.CyclicBarrier:
		(1).是一个同步辅助类,允许一组线程互相等待,直到到达某个公共屏障点 (common barrier point).
			因为该 barrier 在释放等待线程后可以重用,所以称它为循环 的 barrier;
			CyclicBarrier 是包含了"ReentrantLock对象lock"和"Condition对象trip",它是通过独占锁实现的
	14.3.CountDownLatch 与 CyclicBarrier 两者的区别：
		(1).CountDownLatch 的作用是允许1或N个线程等待其他线程完成执行;
			CyclicBarrier 则是允许N个线程相互等待;
		(2).CountDownLatch 的计数器无法被重置
			CyclicBarrier 的计数器可以被重置后使用,因此它被称为是循环的barrier;
	14.4.Semaphore:是一个计数信号量,它的本质是一个"共享锁";
		(1).信号量维护了一个信号量许可集.线程可以通过调用acquire()来获取信号量的许可;
			当信号量中有可用的许可时,线程能获取该许可;否则线程必须等待,直到有可用的许可为止.
			线程可以通过release()来释放它所持有的信号量许可
		(2).Semaphore 包含了sync对象,sync是 Sync 类型;而且,Sync 也是一个继承于 AQS 的抽象类.
			Sync 也包括"公平信号量" FairSync 和"非公平信号量" NonfairSync
15.Callable
	Callable是类似于Runnable的接口，实现Callable接口的类和实现Runnable的类都是可被其它线程执行的任务。
		 Callable和Runnable有几点不同： 
		 (1).Callable规定的方法是call()，而Runnable规定的方法是run().
		 (2).Callable的任务执行后可返回值，而Runnable的任务是不能返回值的。 
		 (3).call()方法可抛出异常，而run()方法是不能抛出异常的。
		 (4).运行Callable任务可拿到一个Future对象， Future表示异步计算的结果。
		 	它提供了检查计算是否完成的方法，以等待计算的完成，并检索计算的结果。
		 	通过Future对象可了解任务执行情况，可取消任务的执行，还可获取任务执行的结果。
16.Lock 锁:
	16.1.ReentrantLock 类(可重入锁),又称为独占锁.
		16.1.1.ReentrantLock 基本:
			(1).在同一个时间点只能被一个线程持有,而可重入即可以被单个线程多次获取.
			(2).ReentrantLock 分为"公平锁"和"非公平锁",区别在于获取锁的机制上是否公平.
			(3).ReentrantLock 是通过一个 FIFO 的等待队列来管理获取该锁的所有线程.公平锁的机制下,线程依次排队获取,
				而"非公平锁"在锁是可获取状态时,不管自己是不是在队列的开头都会获取锁.
		16.1.2.ReentrantLock 函数列表:
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
		16.1.3.
	16.2.Condition
		(1).在使用 notify 和 notifyAll 方法进行通知时,被通知的线程是由JVM随机选择的.但是 ReentrantLock 集合
			Condition 类就实现选择性通知.线程可以注册在指定的 Condition 中,从而可以有选择性的进行线程通知
		(2).synchronized 就相当于整个 Lock 对象中只有一个单一的 Condition 对象,所有的线程都注册在它的一个
			对象上,线程开始 notifyAll 时,需要通知所有的 waitin线程,没有选择权;
	16.3.
三.多线程与并发涉及算法
1.CAS:Compare and Swap-比较与交换
http://www.cnblogs.com/Mainz/p/3546347.html
http://www.cnblogs.com/549294286/p/3766717.html
http://blog.csdn.net/heyutao007/article/details/19975665
http://www.importnew.com/20472.html




























