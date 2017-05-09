一、Java 内存模型:
*******************************************************************************************************
1.Java反编译字节码命令：
	javap -classpath . -c HelloWorld
This leads us to take a look at the constant pool, which
can be done by using "javap -verbose" command.
	javap -classpath . -verbose HelloWorld

本机回送地址：127.0.0.1
二、多线程:Java 的并发采用的是共享内存模型
	1.如果创建线程之后,调用的是run方法,那么该线程就未执行,需要执行完成之后才会进入下一线程中;
	2.如果一个线程sleep进入睡眠状态,那么如果唤醒时先进入阻塞状态,等待执行;
	3.自定义线程名称:
		this.getName();可以在子类的构造方法中给予值:super("");
		Thread.currentThread():获取当前线程对象;
	4.同步函数需要被对象调用,那么函数都有一个所属对象引用,这就是 this,所以同步函数使用的锁是 this;
	5.如何找问题:	
		①明确哪些代码是多线程运行代码;
		②明确共享数据;
		③明确多线程运行代码中哪些语句是操作共享数据的;
	6.使用同步的前提:
		①必须要有两个或两个以上的线程;
		②必须是有多个线程使用同一个锁;
	7.如果同步函数被 static 修饰后,使用的锁不在是 this,因为静态方法中不可以定义 this;
		静态进内存,内存中没有本类的对象,但一定有该类对应的字节码文件对象:类名.class
		synchronized(Demo.class)
		静态的同步函数使用的锁是该方法所在类的字节码文件对象;
		
		(1).线程同步问题:synchronized 依靠"锁"机制进行多线程同步, "锁"有2种, 一种是对象锁, 一种是类锁.
			①.两个线程(线程1与线程2)访问同一个对象的内同步方法syn1()与同步方法syn2()
				结果: 线程1访问对象sameObj的同步方法syn1()时, 线程2访问对象sameObj中的同步方法syn2()阻塞. 
				或者线程2访问时, 线程1阻塞.
				★结论: 通常知道不同线程访问同一个对象的相同同步方法时, 线程间是互斥的. 
						实际上, 不同线程访问同一个对象的不同同步方法时, 线程间也是互斥的. 
						将同步方法换成同步块结论也是成立的. 关键的因素在于: 一个对象就一把锁.		
			②.两个线程(线程1与线程2)访问同一个类的静态同步方法syn() 
				结果: 线程1访问对象sameObj1的静态同步方法syn()时, 线程2访问对象sameObj2中的静态同步方法syn()阻塞. 
				或者线程2访问时, 线程1阻塞. 
				★结论: 不同线程访问同一个类的静态同步方法时, 线程间是互斥的. 不光是类实例, 每一个类也对应一把锁
			③.线程1访问线程安全对象 StringBuffer 的实例, 线程2要访问时该对象则会出现阻塞现象. 
			
	8.关于单例模式线程安全问题:
			/**
			 * 单例模式
			 * ①饿汉式
			 */
			private static final Single instance = new Single();
			private Single(){
				
			}
			public static Single getInstance(){
				return instance;
			}
			
			/**
			 * 单例模式
			 * ②懒汉式:特点在于延迟加载,如果多线程访问时会出现安全问题,使用双层判断问题,
			 * 加锁的对象是所属类所在的字节码文件对象
			 */
			private static Single instance = null;
			private Single(){				
			}
			public static Single getInstance(){
				/*
				 * 通过外层判断,当有线程进来是先判断instance是否为空,而不是直接判断是否已经锁上,提高效率
				 */
				if(instance == null){
					synchronized (Single.class) {
						if(instance == null)
							instance = new Single();
					}
				}
				return instance;
			}
	9.死锁:同步中嵌套同步
		死锁程序:
			public class DeadLock{
				public static void main(String[] args) {
					Thread t1 = new Thread(new Test(true));
					Thread t2 = new Thread(new Test(false));
					t1.start();
					t2.start();
				}
			}
			class Test implements Runnable{
				private boolean flag;
				public Test(boolean flag){
					this.flag = flag;
				}
				@Override
				public void run() {
					if(flag){
						synchronized(MyLock.obj1){
							System.out.println("if obj1");
							synchronized(MyLock.obj2){
								System.out.println("if obj2");
							}
						}
					}else{
						synchronized(MyLock.obj2){
							System.out.println("else obj2");
							synchronized(MyLock.obj1){
								System.out.println("else obj1");
							}
						}
					}
				}	
			}
			class MyLock{
				static Object obj1 = new Object();
				static Object obj2 = new Object();	
			}
	10.线程间通讯:其实是多个线程操作同一资源,但是操作的动作不同;
		(1).等待唤醒机制:
			wait(),notify(),notifyAll()都使用在同步中,因为其要对持有锁的线程操作;
			这些方法定义在Object类中,因为这些方法在操作同步线程时,都必须标识它们所操作线程只有的锁,
			只有同一个锁上的被等待线程可以被同一个锁上notify唤醒;
			等待和唤醒是同一个锁,锁可以是任意对象;
			class Res {
				String name;
				String sex;
				boolean flag = false;
				public synchronized void set(String name, String sex) {
					if (flag)
						try {
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					this.name = name;
					this.sex = sex;
					this.flag = true;
					this.notify();
				}
				public synchronized void out() {
					if (!flag)
						try {
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					System.out.println(this.name + "," + this.sex);
					this.flag = false;
					this.notify();
				}
			}
			class Input implements Runnable {
				Res r;
				int x;

				Input(Res r) {
					this.r = r;
				}

				@Override
				public void run() {
					while (true) {
						if (x == 0) {
							r.set("Coco", "female");
						} else {
							r.set("顾夏阳", "男");
						}
						x = (x + 1) % 2;
					}
				}
			}
			class Output implements Runnable {
				Res r;

				Output(Res r) {
					this.r = r;
				}

				@Override
				public void run() {
					while (true) {
						r.out();
					}
				}
			}
		(2).生产者消费者:多个生产者和消费者时,定义 while 循环使被唤醒的线程再次判断标记,
			使用notifyAll()需要唤醒对方线程,只使用notify,容易出现只唤醒了本方线程,导致程序的所有线程都等待;
			class Resource{
				private String name;
				private int count=1;
				private boolean flag = false;
				
				public synchronized void set(String name){
					while(flag){
						try{
							this.wait();
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					this.name = name + "--" + count++;
					System.out.println(Thread.currentThread().getName() + "...生产者..." + this.name);
					flag = true;
					this.notifyAll();
				}
				public synchronized void out(){
					while(!flag){
						try{
							this.wait();
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					System.out.println(Thread.currentThread().getName() + "...消费者..........." + this.name);
					flag = false;
					this.notifyAll();
				}
			}
			①.JDK5之后提供多线程升级解决方案,将同步 synchronized 替换成 Lock 操作,将 Object 中wait,notify,notifyAll
			替换成 Condition 对象,该对象可以使用 Lock 锁获取,可以获取多个condition对象;
			★ Lock 是显示锁机制;
			★释放锁是必须执行,所以放在 finally 块中
			class Resource {
				private String name;
				private int count = 1;
				private boolean flag = false;

				private Lock lock = new ReentrantLock();
				private Condition condition_pro = lock.newCondition();
				private Condition condition_con = lock.newCondition();

				public void set(String name) {
					lock.lock();
					try {
						while (flag)
							condition_pro.await();
						this.name = name + "--" + count++;
						System.out.println(Thread.currentThread().getName() + "...生产者..."
								+ this.name);
						flag = true;
						condition_con.signalAll();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						lock.unlock();
					}
				}
				public void out() {
					lock.lock();
					try {
						while (!flag)
							condition_con.await();
						System.out.println(Thread.currentThread().getName()
								+ "...消费者..........." + this.name);
						flag = false;
						condition_pro.signalAll();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						lock.unlock();
					}

				}
			}
		(3).读写锁:读线程之间是同时的,读写互斥,写之间互斥;
			public class ReadWriteLockTest {
				static ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
				public static void main(String[] args) {
					// 是否可以进入多个reader - 可以
					// 是否可以进入多个writer - 不可以
					// 当有reader进入后, writer是否可以进入 - 不可以
					// 当有writer进入后, reader是否可以进入 - 不可以
					MyThread t1 = new MyThread(0, "t1");
					MyThread t2 = new MyThread(0, "t2");
					MyThread t3 = new MyThread(1, "t3");
					MyThread t4 = new MyThread(1, "t4");
					t1.start();
					t2.start();
					t3.start();
					t4.start();
				}

				private static class MyThread extends Thread {
					private int type;
					private String threadName;
					public MyThread(int type, String threadName) {
						this.threadName = threadName;
						this.type = type;
					}
					@Override
					public void run() {
						while (true) {
							if (type == 0) {
								// read
								ReentrantReadWriteLock.ReadLock readLock = null;
								try {
									readLock = lock.readLock();
									readLock.lock();
									System.err.println("to read...." + threadName);
									try {
										Thread.sleep(5 * 1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								} finally {
									readLock.unlock();
								}
							} else {
								// write
								ReentrantReadWriteLock.WriteLock writeLock = null;
								try {
									writeLock = lock.writeLock();
									writeLock.lock();
									System.err.println("to write...." + threadName);
									try {
										Thread.sleep(5 * 1000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								} finally {
									writeLock.unlock();
								}
							}
						}
					}
				}
			}

	11.多线程控制:
		(1).停止线程:stop方法已经过时.
			★如何停止线程?
				只有一种方法:run方法结束;一般情况下run中的代码是循环结构的,只要控制循环,就可以让run方法结束,
				也就是线程结束;
				★特殊情况:当线程处于等待状态,就不会读取到标记,那么线程就不会结束;
					当没有指定的方式让等待状态的线程恢复到运行状态,这是需强制让线程恢复到运行状态(interrupt);
			public class StopThread {
				public static void main(String[] args) {
					Stop s = new Stop();
					
					Thread t1 = new Thread(s);
					Thread t2 = new Thread(s);
					
					t1.start();
					t2.start();
					
					int num = 0;
					while(true){
						if(num++ == 60){
							t1.interrupt();//强制中断线程的等待状态
							t2.interrupt();
							s.changeFlag();
							break;
						}
						System.out.println(Thread.currentThread().getName() + "...." + num);
					}
				}
			}
			//如果run方法变成同步方法,且其内调用了wait()方法,那么线程将无法stop,
			//需强制中断其等待状态,恢复到运行状态
			class Stop implements Runnable{
				private boolean flag = true;
				public synchronized void run() {
					while(flag){
						try {
							wait();
						} catch (InterruptedException e) {
							System.out.println(Thread.currentThread().getName() + "....Exception");
							flag = false;
						}
						System.out.println(Thread.currentThread().getName() + "....run");
					}
				}
				
				public void changeFlag(){
					flag = false;
				}
			}
		(2).守护线程:后台线程,默认产生的线程全部是Non-daemon线程
			setDaemon(boolean on),该方法必须在启动线程前调用。
			如果虚拟机中只有Daemon thread 在运行，则虚拟机退出。虚拟机中可能会同时有很多个线程在运行，
			只有当所有的非守护线程都结束的时候，虚拟机的进程才会结束，不管在运行的线程是不是main()线程。
		(3).join方法
			当A线程执行到了B线程的join()方法时,那么A线程就会处于等待状态,等B线程执行完A才会执行;
			join可以用来临时加入线程执行;
		(4).线程优先级:默认优先级为5
			yield方法
	
	12.线程范围内共享数据:ThreadLocal
		(1).多个线程之间共享数据的方式:
			①、将共享数据封装在另外一个对象中，然后将这个对象逐一传递给各个Runnable对象。
				每个线程对共享数据的操作方法也分配到那个对象身上去完成，这样容易实现针对该数据进行的
				各个操作的互斥和通信。
				public class ThreadShareDataTest {					 
					public static void main(String[] args) {						
						ShareData shareData = new ShareData();
						
						Add add = new Add(shareData);
						Sub sub = new Sub(shareData);
						
						new Thread(add).start();
						new Thread(add).start();
						new Thread(sub).start();
						new Thread(sub).start();
					}
				}

				class ShareData{
					private Integer j=100;					
					public synchronized void add(){
						j++;
						System.out.println(Thread.currentThread().getName()+" 对j进行加法运算   "+j);
					}
					
					public synchronized  void sub(){
						j--;
						System.out.println(Thread.currentThread().getName()+"  对j进行减法运算  "+j);
					}
				}

				class Add implements Runnable{

					private ShareData data;
					public Add(ShareData data)
					{
						this.data = data;
					}
					@Override
					public void run() {
						data.add();
					}
				}
				class Sub implements Runnable{
					private ShareData data;
					public Sub(ShareData data){
						this.data = data;
					}
					@Override
					public void run() {
						
						data.sub();
					}
				}

			②、将这些Runnable对象作为某一个类中的内部类，共享数据作为这个外部类中的成员变量，每个线程
				对共享数据的操作方法也分配给外部类，以便实现对共享数据进行的各个操作的互斥和通信，作为内部
				类的各个Runnable对象调用外部类的这些方法。 
				public class ThreadShareDataTest2 {
					public static void main(String[] args) {
						final ShareData2 shareData = new ShareData2();
						for (int i = 0; i < 2; i++) {
							new Thread() {
								@Override
								public void run() {
									shareData.add();
								}
							}.start();
						}
						for (int i = 0; i < 2; i++) {
							new Thread() {
								@Override
								public void run() {
									shareData.sub();
								}
							}.start();
						}
					}
				}

				class ShareData2 {
					private Integer j = 100;
					public synchronized void add() {
						j++;
						System.out.println(Thread.currentThread().getName() + "  对j进行加法运算    "	+ j);
					}

					public synchronized void sub() {
						j--;
						System.out.println(Thread.currentThread().getName() + "  对j进行减法运算   "	+ j);
					}
				}

			◆上面两种方式的组合：将共享数据封装在另外一个对象中，每个线程对共享数据的操作方法也分配到那
			个对象身上去完成，对象作为这个外部类中的成员变量或方法中的局部变量，每个线程的Runnable对象
			作为外部类中的成员内部类或局部内部类。 
			总之，要同步互斥的几段代码最好是分别放在几个独立的方法中，这些方法再放在同一个类中，
			这样比较容易实现它们之间的同步互斥和通信
	
	13.线程池:顶级接口是 Executor,严格意义上讲 Executor 并不是一个线程池，而只是一个执行线程的工具。
		真正的线程池接口是 ExecutorService
		(1).如何实现线程死掉后重新启动:线程池中的Executor.newSingleThreadExecutor(),实现单线程;
		(2).线程池的作用：线程池作用就是限制系统中执行线程的数量。
			根据系统的环境情况，可以自动或手动设置线程数量，达到运行的最佳效果；少了浪费了系统资源，多了造成系统拥挤效率不高。
			用线程池控制线程数量，其他线程排队等候。一个任务执行完毕，再从队列的中取最前面的任务开始执行。若队列中没有等待进程，
			线程池的这一资源处于等待。当一个新任务需要运行时，如果线程池中有等待的工作线程，就可以开始运行了；否则进入等待队列。
		(3).为什么要用线程池:
			①.减少了创建和销毁线程的次数，每个工作线程都可以被重复利用，可执行多个任务。
			②.可以根据系统的承受能力，调整线程池中工作线线程的数目，防止因为消耗过多的内存，
				而把服务器累趴下(每个线程需要大约1MB内存，线程开的越多，消耗的内存也就越大，最后死机)。
				
	14.Callable Future:
		Callable接口类似于Runnable，从名字就可以看出来了，但是Runnable不会返回结果，并且无法抛出返回结果的异常，
		而Callable功能更强大一些，被线程执行后，可以返回值，这个返回值可以被Future拿到，也就是说，Future可以拿到异步执行任务的返回值
		public class ThreadCallableFuture {
			public static void main(String[] args) {
				ExecutorService pool = Executors.newFixedThreadPool(3);
				Future<String> future = 
				pool.submit(new Callable<String>() {			
					@Override
					public String call() throws Exception {
						return "hello";
					}
				});;
				try {
					System.out.println(future.get());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				CompletionService<Integer> com = new ExecutorCompletionService<Integer>(pool);
				for(int i=0;i<10;i++){
					final int task = i;
					com.submit(new Callable<Integer>() {
						@Override
						public Integer call() throws Exception {
							return task;
						}
					});
				}
				for(int i=0;i<10;i++){
					try {
						System.out.println(com.take().get());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		import java.util.concurrent.*;
		/**
		 * Callable 和 Future接口
		 * Callable是类似于Runnable的接口，实现Callable接口的类和实现Runnable的类都是可被其它线程执行的任务。
		 * Callable和Runnable有几点不同： 
		 * （1）Callable规定的方法是call()，而Runnable规定的方法是run().
		 * （2）Callable的任务执行后可返回值，而Runnable的任务是不能返回值的。 
		 * （3）call()方法可抛出异常，而run()方法是不能抛出异常的。
		 * （4）运行Callable任务可拿到一个Future对象， Future表示异步计算的结果。
		 * 它提供了检查计算是否完成的方法，以等待计算的完成，并检索计算的结果。
		 * 通过Future对象可了解任务执行情况，可取消任务的执行，还可获取任务执行的结果。
		 */
		public class CallableAndFuture {

			/**
			 * 自定义一个任务类，实现Callable接口
			 */
			public static class MyCallableClass implements Callable {
				// 标志位
				private int flag = 0;

				public MyCallableClass(int flag) {
					this.flag = flag;
				}

				public String call() throws Exception {
					if (this.flag == 0) {
						// 如果flag的值为0，则立即返回
						return "flag = 0";
					}
					if (this.flag == 1) {
						// 如果flag的值为1，做一个无限循环
						try {
							while (true) {
								System.out.println("looping......");
								Thread.sleep(2000);
							}
						} catch (InterruptedException e) {
							System.out.println("Interrupted");
						}
						return "false";
					} else {
						// falg不为0或者1，则抛出异常
						throw new Exception("Bad flag value!");
					}
				}
			}

			public static void main(String[] args) {
				// 定义3个Callable类型的任务
				MyCallableClass task1 = new MyCallableClass(0);
				MyCallableClass task2 = new MyCallableClass(1);
				MyCallableClass task3 = new MyCallableClass(2);

				// 创建一个执行任务的服务
				ExecutorService es = Executors.newFixedThreadPool(3);
				try {
					// 提交并执行任务，任务启动时返回了一个Future对象，
					// 如果想得到任务执行的结果或者是异常可对这个Future对象进行操作
					Future future1 = es.submit(task1);
					// 获得第一个任务的结果，如果调用get方法，当前线程会等待任务执行完毕后才往下执行
					System.out.println("task1: " + future1.get());

					Future future2 = es.submit(task2);
					// 等待5秒后，再停止第二个任务。因为第二个任务进行的是无限循环
					Thread.sleep(5000);
					System.out.println("task2 cancel: " + future2.cancel(true));

					// 获取第三个任务的输出，因为执行第三个任务会引起异常
					// 所以下面的语句将引起异常的抛出
					Future future3 = es.submit(task3);
					System.out.println("task3: " + future3.get());
				} catch (Exception e) {
					System.out.println(e.toString());
				}
				// 停止任务执行服务
				es.shutdownNow();
			}
		}
	15.线程同步工具类:
		(1).Semaphore实现信号灯:Semaphore可以控制某个资源可被同时访问的个数
			①.单个信号量的Semaphore对象可以实现互斥锁的功能,并且可以由一个线程获得了"锁",再由另一个线程释放锁,可以应用于死锁恢复的场合;
			/**
			 * Java 5.0里新加了4个协调线程间进程的同步装置，它们分别是：
			 * Semaphore, CountDownLatch, CyclicBarrier和Exchanger.
			 * 本例主要介绍Semaphore。
			 * Semaphore是用来管理一个资源池的工具，可以看成是个通行证，
			 * 线程要想从资源池拿到资源必须先拿到通行证，
			 * 如果线程暂时拿不到通行证，线程就会被阻断进入等待状态。
			 */
			public class SemaphoreTest {
				/**
				 * 模拟资源池的类
				 * 只为池发放2个通行证，即同时只允许2个线程获得池中的资源。
				 */
				public static class Pool {
					// 保存资源池中的资源
					ArrayList<String> pool = null;
					// 通行证
					Semaphore pass = null;
					Lock lock = new ReentrantLock();
					public Pool(int size) {
						// 初始化资源池
						pool = new ArrayList<String>();
						for (int i = 0; i < size; i++) {
							pool.add("Resource " + i);
						}
						// 发放2个通行证
						pass = new Semaphore(2);
					}

					public String get() throws InterruptedException {
						// 获取通行证,只有得到通行证后才能得到资源
						System.out.println("Try to get a pass...");
						pass.acquire();
						System.out.println("Got a pass");
						return getResource();
					}

					public void put(String resource) {
						// 归还通行证，并归还资源
						System.out.println("Released a pass");
						pass.release();
						releaseResource(resource);
					}

					private String getResource() {
						lock.lock();
						String result = pool.remove(0);
						System.out.println("资源 " + result + " 被取走");
						lock.unlock();
						return result;
					}

					private void releaseResource(String resource) {
						lock.lock();
						System.out.println("资源 " + resource + " 被归还");
						pool.add(resource);
						lock.unlock();
					} 
				}
				
				public static void testPool() {
					// 准备10个资源的资源池
					final Pool aPool = new Pool(10);
					Runnable worker = new Runnable() {
						public void run() {
							String resource = null;
							try {
								//取得resource
								resource = aPool.get();
								//用resource做工作
								System.out.println("I am working on " + resource);
								Thread.sleep(500);
								System.out.println("I finished on " + resource);
							} catch (InterruptedException ex) {
							}
							//归还resource
							aPool.put(resource);
						}
					};
					// 启动5个任务
					ExecutorService service = Executors.newCachedThreadPool();
					for (int i = 0; i < 5; i++) {
						service.submit(worker);
					}
					service.shutdown();
				} 
				
				public static void main(String[] args) {
					SemaphoreTest.testPool();
					
					ExecutorService service = Executors.newCachedThreadPool();
					final  Semaphore sp = new Semaphore(3);
					for(int i=0;i<10;i++){
						Runnable runnable = new Runnable(){
								public void run(){
								try {
									sp.acquire();
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}
								System.out.println("线程" + Thread.currentThread().getName() + 
										"进入，当前已有" + (3-sp.availablePermits()) + "个并发");
								try {
									Thread.sleep((long)(Math.random()*10000));
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								System.out.println("线程" + Thread.currentThread().getName() + 
										"即将离开");					
								sp.release();
								//下面代码有时候执行不准确，因为其没有和上面的代码合成原子单元
								System.out.println("线程" + Thread.currentThread().getName() + 
										"已离开，当前已有" + (3-sp.availablePermits()) + "个并发");					
							}
						};
						service.execute(runnable);			
					}
				}
			}
		
		(2).CyclicBarrier:
			/**
			 * CyclicBarrier类似于CountDownLatch也是个计数器，
			 * 不同的是CyclicBarrier数的是调用了CyclicBarrier.await()进入等待的线程数，
			 * 当线程数达到了CyclicBarrier初始时规定的数目时，所有进入等待状态的线程被唤醒并继续。
			 * CyclicBarrier就象它名字的意思一样，可看成是个障碍，
			 * 所有的线程必须到齐后才能一起通过这个障碍。
			 * CyclicBarrier初始时还可带一个Runnable的参数，
			 * 此Runnable任务在CyclicBarrier的数目达到后，所有其它线程被唤醒前被执行。
			 */
			 public class CyclicBarrierTest {
				public static void main(String[] args) {
					ExecutorService service = Executors.newCachedThreadPool();
					final  CyclicBarrier cb = new CyclicBarrier(3);
					for(int i=0;i<3;i++){
						Runnable runnable = new Runnable(){
								public void run(){
								try {
									Thread.sleep((long)(Math.random()*10000));	
									System.out.println("线程" + Thread.currentThread().getName() + 
											"即将到达集合地点1，当前已有" + (cb.getNumberWaiting()+1) + "个已经到达，" + (cb.getNumberWaiting()==2?"都到齐了，继续走啊":"正在等候"));						
									cb.await();
									
									Thread.sleep((long)(Math.random()*10000));	
									System.out.println("线程" + Thread.currentThread().getName() + 
											"即将到达集合地点2，当前已有" + (cb.getNumberWaiting()+1) + "个已经到达，" + (cb.getNumberWaiting()==2?"都到齐了，继续走啊":"正在等候"));
									cb.await();	
									Thread.sleep((long)(Math.random()*10000));	
									System.out.println("线程" + Thread.currentThread().getName() + 
											"即将到达集合地点3，当前已有" + (cb.getNumberWaiting() + 1) + "个已经到达，" + (cb.getNumberWaiting()==2?"都到齐了，继续走啊":"正在等候"));						
									cb.await();						
								} catch (Exception e) {
									e.printStackTrace();
								}				
							}
						};
						service.execute(runnable);
					}
					service.shutdown();
				}							
				public static class ComponentThread implements Runnable {
					CyclicBarrier barrier;// 计数器
					int ID;	// 组件标识
					int[] array;	// 数据数组

					// 构造方法
					public ComponentThread(CyclicBarrier barrier, int[] array, int ID) {
						this.barrier = barrier;
						this.ID = ID;
						this.array = array;
					}

					public void run() {
						try {
							array[ID] = new Random().nextInt(100);
							System.out.println("Component " + ID + " generates: " + array[ID]);
							// 在这里等待Barrier处
							System.out.println("Component " + ID + " sleep...");
							barrier.await();
							System.out.println("Component " + ID + " awaked...");
							// 计算数据数组中的当前值和后续值
							int result = array[ID] + array[ID + 1];
							System.out.println("Component " + ID + " result: " + result);
						} catch (Exception ex) {
						}
					}
				}
				/**
				 * 测试CyclicBarrier的用法
				 */
				public static void testCyclicBarrier() {
					final int[] array = new int[3];
					CyclicBarrier barrier = new CyclicBarrier(2, new Runnable() {
						// 在所有线程都到达Barrier时执行
						public void run() {
							System.out.println("testCyclicBarrier run...");
							array[2] = array[0] + array[1];
						}
					});

					// 启动线程
					new Thread(new ComponentThread(barrier, array, 0)).start();
					new Thread(new ComponentThread(barrier, array, 1)).start();
				}

				public static void main(String[] args) {
					CyclicBarrierTest.testCyclicBarrier();
				}
			}
			
		(3).CountDownLatch
			/**
			 * CountDownLatch是个计数器，它有一个初始数，
			 * 等待这个计数器的线程必须等到计数器倒数到零时才可继续。
			 */
			public class CountDownLatchTest {
				/**
				 * 初始化组件的线程
				 */
				public static class ComponentThread implements Runnable {
					// 计数器
					CountDownLatch latch;
					// 组件ID
					int ID;
					// 构造方法
					public ComponentThread(CountDownLatch latch, int ID) {
						this.latch = latch;
						this.ID = ID;
					}

					public void run() {
						// 初始化组件
						System.out.println("Initializing component " + ID);
						try {
							Thread.sleep(500 * ID);
						} catch (InterruptedException e) {
						}
						System.out.println("Component " + ID + " initialized!");
						//将计数器减一
						latch.countDown();
					}
				}

				/**
				 * 启动服务器
				 */
				public static void startServer() throws Exception {
					System.out.println("Server is starting.");
					//初始化一个初始值为3的CountDownLatch
					CountDownLatch latch = new CountDownLatch(3);
					//起3个线程分别去启动3个组件
					ExecutorService service = Executors.newCachedThreadPool();
					service.submit(new ComponentThread(latch, 1));
					service.submit(new ComponentThread(latch, 2));
					service.submit(new ComponentThread(latch, 3));
					service.shutdown();

					//等待3个组件的初始化工作都完成
					latch.await();

					//当所需的三个组件都完成时，Server就可继续了
					System.out.println("Server is up!");
				}

				public static void main(String[] args) throws Exception {
					CountDownLatchTest.startServer();
					ExecutorService service = Executors.newCachedThreadPool();
					final CountDownLatch cdOrder = new CountDownLatch(1);
					final CountDownLatch cdAnswer = new CountDownLatch(3);		
					for(int i=0;i<3;i++){
						Runnable runnable = new Runnable(){
								public void run(){
								try {
									System.out.println("线程" + Thread.currentThread().getName() + "正准备接受命令");						
									cdOrder.await();
									System.out.println("线程" + Thread.currentThread().getName() + "已接受命令");								
									Thread.sleep((long)(Math.random()*10000));	
									System.out.println("线程" + Thread.currentThread().getName() + "回应命令处理结果");						
									cdAnswer.countDown();						
								} catch (Exception e) {
									e.printStackTrace();
								}				
							}
						};
						service.execute(runnable);
					}		
					try {
						Thread.sleep((long)(Math.random()*10000));
					
						System.out.println("线程" + Thread.currentThread().getName() + "即将发布命令");						
						cdOrder.countDown();
						System.out.println("线程" + Thread.currentThread().getName() + "已发送命令，正在等待结果");	
						cdAnswer.await();
						System.out.println("线程" + Thread.currentThread().getName() + "已收到所有响应结果");	
					} catch (Exception e) {
						e.printStackTrace();
					}				
					service.shutdown();
				}
			}
		
		(4).Exchanger
			/**
			 * Exchanger让两个线程可以互换信息。
			 * 例子中服务生线程往空的杯子里倒水，顾客线程从装满水的杯子里喝水，
			 * 然后通过Exchanger双方互换杯子，服务生接着往空杯子里倒水，顾客接着喝水，
			 * 然后交换，如此周而复始。
			 */
			public class ExchangerTest {
				// 描述一个装水的杯子
				public static class Cup{
					// 标识杯子是否有水
					private boolean full = false;
					public Cup(boolean full){
						this.full = full;
					}
					// 添水，假设需要5s
					public void addWater(){
						if (!this.full){
							try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
							}
							this.full = true;
						}
					}
					// 喝水，假设需要10s
					public void drinkWater(){
						if (this.full){
							try {
								Thread.sleep(10000);
							} catch (InterruptedException e) {
							}
							this.full = false;
						}
					}
				}
				
				public static void testExchanger() {
					//	初始化一个Exchanger，并规定可交换的信息类型是杯子
					final Exchanger<Cup> exchanger = new Exchanger<Cup>();
					// 初始化一个空的杯子和装满水的杯子
					final Cup initialEmptyCup = new Cup(false); 
					final Cup initialFullCup = new Cup(true);

					//服务生线程
					class Waiter implements Runnable {
						public void run() {
							Cup currentCup = initialEmptyCup;
							try {
								int i=0;
								while (i < 2){
									System.out.println("服务生开始往杯子中添水："
											+ System.currentTimeMillis());
									// 往空的杯子里加水
									currentCup.addWater();
									System.out.println("服务生添水完毕："
											+ System.currentTimeMillis());
									// 杯子满后和顾客的空杯子交换
									System.out.println("服务生等待与顾客交换杯子："
											+ System.currentTimeMillis());
									currentCup = exchanger.exchange(currentCup);
									System.out.println("服务生与顾客交换杯子完毕："
											+ System.currentTimeMillis());
									i++;
								}

							} catch (InterruptedException ex) {
							}
						}
					}

					//顾客线程
					class Customer implements Runnable {
						public void run() {
							Cup currentCup = initialFullCup;
							try {
								int i=0;
								while (i < 2){
									System.out.println("顾客开始喝水："
											+ System.currentTimeMillis());
									//把杯子里的水喝掉
									currentCup.drinkWater();
									System.out.println("顾客喝水完毕："
											+ System.currentTimeMillis());
									//将空杯子和服务生的满杯子交换
									System.out.println("顾客等待与服务生交换杯子："
											+ System.currentTimeMillis());
									currentCup = exchanger.exchange(currentCup);
									System.out.println("顾客与服务生交换杯子完毕："
											+ System.currentTimeMillis());
									i++;
								}
							} catch (InterruptedException ex) {
							}
						}
					}
					
					new Thread(new Waiter()).start();
					new Thread(new Customer()).start();
				}
				
				public static void main(String[] args) {
					ExchangerTest.testExchanger();
				}
			}
			
	16.可阻塞队列:		
		①.BlockingQueue 是一种特殊的 Queue，若 BlockingQueue 是空的，从 BlockingQueue 取东西的操作将会被阻断进入等待状
			态直到BlocingkQueue进了新货才会被唤醒。
		同样，如果BlockingQueue是满的任何试图往里存东西的操作也会被阻断进入等待状态，直到BlockingQueue里有新的空间
			才会被唤醒继续操作。
		②.BlockingQueue提供的方法主要有：
			add(anObject): 把anObject加到BlockingQueue里，如果BlockingQueue可以容纳返回true，否则抛出IllegalStateException异常。 
			offer(anObject)：把anObject加到BlockingQueue里，如果BlockingQueue可以容纳返回true，否则返回false。 
			put(anObject)：把anObject加到BlockingQueue里，如果BlockingQueue没有空间，调用此方法的线程被阻断直到BlockingQueue里有新的空间再继续。 
			poll(time)：取出BlockingQueue里排在首位的对象，若不能立即取出可等time参数规定的时间。取不到时返回null。 
			take()：取出BlockingQueue里排在首位的对象，若BlockingQueue为空，阻断进入等待状态直到BlockingQueue有新的对象被加入为止。

		③.根据不同的需要BlockingQueue有4种具体实现：
			(1)ArrayBlockingQueue：规定大小的BlockingQueue，其构造函数必须带一个int参数来指明其大小。其所含的对象是以FIFO(先入先出)顺序排序的。 
			(2)LinkedBlockingQueue：大小不定的BlockingQueue，若其构造函数带一个规定大小的参数，生成的BlockingQueue有大小限制，
				若不带大小参数，所生成的BlockingQueue的大小由Integer.MAX_VALUE来决定。其所含的对象是以FIFO(先入先出)顺序排序的。
				LinkedBlockingQueue和ArrayBlockingQueue比较起来，它们背后所用的数据结构不一样，
				导致LinkedBlockingQueue的数据吞吐量要大于ArrayBlockingQueue，但在线程数量很大时其性能的可预见性低于ArrayBlockingQueue。 
			(3)PriorityBlockingQueue：类似于LinkedBlockingQueue，但其所含对象的排序不是FIFO，而是依据对象的自然排序顺序
				或者是构造函数所带的Comparator决定的顺序。 
			(4)SynchronousQueue：特殊的BlockingQueue，对其的操作必须是放和取交替完成的。


















