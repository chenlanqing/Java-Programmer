一.Java线程池
1.顶级接口是Executor,严格意义上讲Executor并不是一个线程池，而只是一个执行线程的工具。真正的线程池接口是ExecutorService
	(1).如何实现线程死掉后重新启动:线程池中的Executor.newSingleThreadExecutor(),实现单线程;
	(2).线程池的作用：线程池作用就是限制系统中执行线程的数量。
		根据系统的环境情况，可以自动或手动设置线程数量，达到运行的最佳效果；少了浪费了系统资源，多了造成系统拥挤效率不高。
		用线程池控制线程数量，其他线程排队等候。一个任务执行完毕，再从队列的中取最前面的任务开始执行。若队列中没有等待进程，
		线程池的这一资源处于等待。当一个新任务需要运行时，如果线程池中有等待的工作线程，就可以开始运行了；否则进入等待队列。
	(3).为什么要用线程池:
		①.减少了创建和销毁线程的次数，每个工作线程都可以被重复利用，可执行多个任务。
		②.可以根据系统的承受能力，调整线程池中工作线线程的数目，防止因为消耗过多的内存，
			而把服务器累趴下(每个线程需要大约1MB内存，线程开的越多，消耗的内存也就越大，最后死机)
		
2.Executor:存在的目的是提供一种将"任务提交"与"任务如何运行"分离开来的机制;
  Executors为Executor，ExecutorService，ScheduledExecutorService，ThreadFactory和Callable类提供了一些工具方法;
  Java通过Executors提供四种线程池，分别为：
	(1).newCachedThreadPool:创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
		该线程池比较适合没有固定大小并且比较快速就能完成的小任务，它将为每个任务创建一个线程;		
	//优点:相对于new Thread来说60秒内能够重用已创建的线程线程重用;
	(2).newFixedThreadPool:创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
	(3).newScheduledThreadPool:创建一个定长线程池，支持定时及周期性任务执行。即线程数量为1的FixedThreadPool;
		如果提交了多个任务,这些任务将排队;
	(4).newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，
		保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
//注意:
	(1).除了CachedThreadPool使用的是直接提交策略的缓冲队列以外，其余两个用的采用的都是无界缓冲队列，
		也就说，FixedThreadPool和SingleThreadExecutor创建的线程数量就不会超过 corePoolSize。
	(2).三个线程池采用的ThreadPoolExecutor构造方法都是同一个，使用的都是默认的ThreadFactory和handler;
		也就说三个线程池创建的线程对象都是同组，优先权等级为正常的Thread.NORM_PRIORITY（5）的非守护线程，
		使用的被拒绝任务处理方式是直接抛出异常的AbortPolicy策略
	
3.ExecutorService任务周期管理接口
	为了解决执行服务的生命周期问题，EecutorService扩展了Executor接口，添加了一些用于生命周期管理的方法;
	
4.ThreadPoolExecutor线程池实现类
	//这个类中定义的重要变量
	private final BlockingQueue<Runnable> workQueue;              // 阻塞队列
	private final ReentrantLock mainLock = new ReentrantLock();   // 互斥锁
	private final HashSet<Worker> workers = new HashSet<Worker>();// 线程集合.一个Worker对应一个线程
	private final Condition termination = mainLock.newCondition();// 终止条件
	private int largestPoolSize;           // 线程池中线程数量曾经达到过的最大值。
	private long completedTaskCount;       // 已完成任务数量
	private volatile ThreadFactory threadFactory;     // ThreadFactory对象，用于创建线程。
	private volatile RejectedExecutionHandler handler;// 拒绝策略的处理句柄
	private volatile long keepAliveTime;   // 线程池维护线程所允许的空闲时间
	private volatile boolean allowCoreThreadTimeOut;
	private volatile int corePoolSize;     // 线程池维护线程的最小数量，哪怕是空闲的
	private volatile int maximumPoolSize;  // 线程池维护的最大线程数量

(1).corePoolSize与maximumPoolSize 
由于ThreadPoolExecutor 将根据 corePoolSize和 maximumPoolSize设置的边界自动调整池大小，
当新任务在方法 execute(java.lang.Runnable) 中提交时：
    A:如果运行的线程少于 corePoolSize，则创建新线程来处理请求，即使其他辅助线程是空闲的；
　　B:如果设置的corePoolSize 和 maximumPoolSize相同，则创建的线程池是大小固定的，如果运行的线程与corePoolSize相同，
	  当有新请求过来时，若workQueue未满，则将请求放入workQueue中，等待有空闲的线程去从workQueue中取任务并处理
　　C:如果运行的线程多于 corePoolSize 而少于 maximumPoolSize，则仅当队列满时才创建新线程才创建新的线程去处理请求；
　　D:如果运行的线程多于corePoolSize 并且等于maximumPoolSize，若队列已经满了，则通过handler所指定的策略来处理新请求；
　　E:如果将 maximumPoolSize 设置为基本的无界值（如 Integer.MAX_VALUE），则允许池适应任意数量的并发任务
也就是说，处理任务的优先级为： 
	A:核心线程corePoolSize > 任务队列workQueue > 最大线程maximumPoolSize，如果三者都满了，使用handler处理被拒绝的任务。
	B:当池中的线程数大于corePoolSize的时候，多余的线程会等待keepAliveTime长的时间，如果无请求可处理就自行销毁。
	
5.














	
			