<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、线程栈（thread dump）](#%E4%B8%80%E7%BA%BF%E7%A8%8B%E6%A0%88thread-dump)
  - [1、概念：](#1%E6%A6%82%E5%BF%B5)
  - [2、线程栈信息](#2%E7%BA%BF%E7%A8%8B%E6%A0%88%E4%BF%A1%E6%81%AF)
  - [3、线程状态](#3%E7%BA%BF%E7%A8%8B%E7%8A%B6%E6%80%81)
  - [3、如何输出线程栈](#3%E5%A6%82%E4%BD%95%E8%BE%93%E5%87%BA%E7%BA%BF%E7%A8%8B%E6%A0%88)
  - [4、如何使用线程栈定位问题](#4%E5%A6%82%E4%BD%95%E4%BD%BF%E7%94%A8%E7%BA%BF%E7%A8%8B%E6%A0%88%E5%AE%9A%E4%BD%8D%E9%97%AE%E9%A2%98)
- [二、Java线上问题排查思路](#%E4%BA%8Cjava%E7%BA%BF%E4%B8%8A%E9%97%AE%E9%A2%98%E6%8E%92%E6%9F%A5%E6%80%9D%E8%B7%AF)
  - [1、常见线上问题](#1%E5%B8%B8%E8%A7%81%E7%BA%BF%E4%B8%8A%E9%97%AE%E9%A2%98)
  - [2、问题定位](#2%E9%97%AE%E9%A2%98%E5%AE%9A%E4%BD%8D)
  - [3、Linux常用的性能分析工具](#3linux%E5%B8%B8%E7%94%A8%E7%9A%84%E6%80%A7%E8%83%BD%E5%88%86%E6%9E%90%E5%B7%A5%E5%85%B7)
- [线上问题排查神器：btrace](#%E7%BA%BF%E4%B8%8A%E9%97%AE%E9%A2%98%E6%8E%92%E6%9F%A5%E7%A5%9E%E5%99%A8btrace)
- [参考资料](#%E5%8F%82%E8%80%83%E8%B5%84%E6%96%99)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


# 一、线程栈（thread dump）

## 1、概念：

线程栈是某个时间点，JVM所有线程的活动状态的一个汇总；通过线程栈，可以查看某个时间点，各个线程正在做什么，通常使用线程栈来定位软件运行时的各种问题，例如 CPU 使用率特别高，或者是响应很慢，性能大幅度下滑

## 2、线程栈信息

线程栈包含了多个线程的活动信息，一个线程的活动信息通常看起来如下所示
```
"main" prio=10 tid=0x00007faac0008800 nid=0x9f0 waiting on condition [0x00007faac6068000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
        at java.lang.Thread.sleep(Native Method)
        at ThreadDump.main(ThreadDump.java:4)
```

- 线程的名字：其中 main 就是线程的名字，需要注意的是，当使用 Thread 类来创建一条线程，并且没有指定线程的名字时，这条线程的命名规则为 Thread-i，i 代表数字。如果使用 ThreadFactory 来创建线程，则线程的命名规则为 ** pool-i-thread-j**，i 和 j 分别代表数字；

- 线程的优先级：prio=10 代表线程的优先级为 10

- 线程 id：tid=0x00007faac0008800 代表线程 id 为 0x00007faac0008800，而** nid=0x9f0** 代表该线程对应的操作系统级别的线程 id。所谓的 nid，换种说法就是 native id。在操作系统中，分为内核级线程和用户级线程，JVM 的线程是用户态线程，内核不知情，但每一条 JVM 的线程都会映射到操作系统一条具体的线程；

- 线程的状态：java.lang.Thread.State: TIMED_WAITING (sleeping) 以及 waiting on condition 代表线程当前的状态；

- 线程占用的内存地址：[0x00007faac6068000] 代表当前线程占用的内存地址；

- 线程的调用栈：at java.lang.Thread.sleep(Native Method)* 以及它之后的相类似的信息，代表线程的调用栈

## 3、线程状态

- 代码1：

    ```java
    public static void main(String[] args) throws InterruptedException {
            int sum = 0;
            while (true) {
                int i = 0;
                int j = 1;
                sum = i + j;
            }
    }
    ```
    main 线程对应的线程栈就是

    ```java
    "main" prio=10 tid=0x00007fe1b4008800 nid=0x1292 runnable [0x00007fe1bd88f000]
    java.lang.Thread.State: RUNNABLE
            at ThreadDump.main(ThreadDump.java:7)
    ```
    其线程状态是：runnable

- 如果是以下代码，两个线程会竞争同一个锁，其中只有一个线程能获得锁，然后进行 sleep(time)，从而进入 TIMED_WAITING 状态，另外一个线程由于等待锁，会进入 BLOCKED 状态

    ```java
    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    fun1();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.setDaemon(false);
        t1.setName("MyThread1");
        Thread t2 = new Thread(new Runnable() {
            
            @Override
            public void run() {
                try {
                    fun2();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        t2.setDaemon(false);
        t2.setName("MyThread2");
        t1.start();
        t2.start();
        */
        
    }
    private static synchronized void fun1() throws InterruptedException {
        System.out.println("t1 acquire");
        Thread.sleep(Integer.MAX_VALUE);
    }
    private static synchronized void fun2() throws InterruptedException {
        System.out.println("t2 acquire");
        Thread.sleep(Integer.MAX_VALUE);
    }
    ```
    对应的线程栈为：
    ```
    "MyThread2" prio=10 tid=0x00007ff1e40b1000 nid=0x12eb waiting for monitor entry [0x00007ff1e07f6000]
    java.lang.Thread.State: BLOCKED (on object monitor)
            at ThreadDump.fun2(ThreadDump.java:45)
            - waiting to lock <0x00000000eb8602f8> (a java.lang.Class for ThreadDump)
            at ThreadDump.access$100(ThreadDump.java:1)
            at ThreadDump$2.run(ThreadDump.java:25)
            at java.lang.Thread.run(Thread.java:745)

    "MyThread1" prio=10 tid=0x00007ff1e40af000 nid=0x12ea waiting on condition [0x00007ff1e08f7000]
    java.lang.Thread.State: TIMED_WAITING (sleeping)
            at java.lang.Thread.sleep(Native Method)
            at ThreadDump.fun1(ThreadDump.java:41)
            - locked <0x00000000eb8602f8> (a java.lang.Class for ThreadDump)
            at ThreadDump.access$000(ThreadDump.java:1)
            at ThreadDump$1.run(ThreadDump.java:10)
            at java.lang.Thread.run(Thread.java:745)
    ```
    可以看到，t1 线程的调用栈里有这么一句 ** - locked <0x00000000eb8602f8> (a java.lang.Class for ThreadDump)**，说明它获得了锁，并且进行 sleep(sometime)操作，因此状态为 TIMED_WAITING。而 t2 线程由于获取不到锁，所以在它的调用栈里能看到 - waiting to lock <0x00000000eb8602f8> (a java.lang.Class for ThreadDump)，说明它正在等待锁，因此进入 BLOCKED 状态

- 对于 WAITING 状态的线程栈
    ```java
        private static final Object lock = new Object();
            public static void main(String[] args) throws InterruptedException {
                synchronized (lock) {
                    lock.wait();
                }
            }
    ```

    得到的线程栈为：
    ```
    "main" prio=10 tid=0x00007f1fdc008800 nid=0x13fe in Object.wait() [0x00007f1fe1fec000]
    java.lang.Thread.State: WAITING (on object monitor)
        at java.lang.Object.wait(Native Method)
        - waiting on <0x00000000eb860640> (a java.lang.Object)
        at java.lang.Object.wait(Object.java:503)
        at ThreadDump.main(ThreadDump.java:7)
        - locked <0x00000000eb860640> (a java.lang.Object)
    ```

## 3、如何输出线程栈

由于线程栈反映的是 JVM 在某个时间点的线程状态，因此分析线程栈时，为避免偶然性，有必要多输出几份进行分析

- （1）获得JVM进程ID

    - jps 命令：
    ```
    [root@localhost ~]# jps
    5163 ThreadDump
    5173 Jps
    ```
    - ps -ef | grep java
    ```
    [root@localhost ~]# ps -ef | grep java
    root       5163   2479  0 01:18 pts/0    00:00:00 java ThreadDump
    root       5185   2553  0 01:18 pts/1    00:00:00 grep --color=auto java
    ```

- （2）获取线程栈信息

    - jstack命令：JDK自带
        ```
        [root@localhost ~]# jstack 5163
        ```
        详细日志查看：[threadump](https://github.com/chenlanqing/learningNote/blob/master/Java/log/threadump.md)

    - kill -3 [pid]

        Java虚拟机提供了线程转储(Thread dump)的后门， 通过这个后门， 可以将线程堆栈打印出来，这个后门就是通过向Java进程发送一个QUIT信号， Java虚拟机收到该信号之后， 将系统当前的JAVA线程调用堆栈打印出来；

## 4、如何使用线程栈定位问题

### 4.1、发现死锁
当两个或多个线程正在等待被对方占有的锁，死锁就会发生。死锁会导致两个线程无法继续运行，被永远挂起

下列代码发生死锁：
```java
ublic class ThreadDump {    
    public static void main(String[] args) throws InterruptedException {
        Object lock1 = new Object();
        Object lock2 = new Object();        
        new Thread1(lock1, lock2).start();
        new Thread2(lock1, lock2).start();
    }
    private static class Thread1 extends Thread {
        Object lock1 = null;
        Object lock2 = null;        
        public Thread1(Object lock1, Object lock2) {
            this.lock1 = lock1;
            this.lock2 = lock2;
            this.setName(getClass().getSimpleName());
        }        
        public void run() {
            synchronized (lock1) {
                try {
                    Thread.sleep(2);
                } catch(Exception e) {
                    e.printStackTrace();
                }                
                synchronized (lock2) {                    
                }
            }
        }
    }
    
    private static class Thread2 extends Thread {
        Object lock1 = null;
        Object lock2 = null;        
        public Thread2(Object lock1, Object lock2) {
            this.lock1 = lock1;
            this.lock2 = lock2;
            this.setName(getClass().getSimpleName());
        }        
        public void run() {
            synchronized (lock2) {
                try {
                    Thread.sleep(2);
                } catch(Exception e) {
                    e.printStackTrace();
                }                
                synchronized (lock1) {
                    
                }
            }
        }
    }
}
```

对线线程栈为：
```
"Thread2" prio=10 tid=0x00007f9bf40a1000 nid=0x1472 waiting for monitor entry [0x00007f9bf8944000]
   java.lang.Thread.State: BLOCKED (on object monitor)
        at ThreadDump$Thread2.run(ThreadDump.java:63)
        - waiting to lock <0x00000000eb860498> (a java.lang.Object)
        - locked <0x00000000eb8604a8> (a java.lang.Object)

"Thread1" prio=10 tid=0x00007f9bf409f000 nid=0x1471 waiting for monitor entry [0x00007f9bf8a45000]
   java.lang.Thread.State: BLOCKED (on object monitor)
        at ThreadDump$Thread1.run(ThreadDump.java:38)
        - waiting to lock <0x00000000eb8604a8> (a java.lang.Object)
        - locked <0x00000000eb860498> (a java.lang.Object)

Found one Java-level deadlock:
=============================
"Thread2":
  waiting to lock monitor 0x00007f9be4004f88 (object 0x00000000eb860498, a java.lang.Object),
  which is held by "Thread1"
"Thread1":
  waiting to lock monitor 0x00007f9be40062c8 (object 0x00000000eb8604a8, a java.lang.Object),
  which is held by "Thread2"

Java stack information for the threads listed above:
===================================================
"Thread2":
        at ThreadDump$Thread2.run(ThreadDump.java:63)
        - waiting to lock <0x00000000eb860498> (a java.lang.Object)
        - locked <0x00000000eb8604a8> (a java.lang.Object)
"Thread1":
        at ThreadDump$Thread1.run(ThreadDump.java:38)
        - waiting to lock <0x00000000eb8604a8> (a java.lang.Object)
        - locked <0x00000000eb860498> (a java.lang.Object)

Found 1 deadlock.
```
可以看到，当发生了死锁的时候，堆栈中直接打印出了死锁的信息** Found one Java-level deadlock: **，并给出了分析信息

### 4.2、定位 CPU 过高的原因

首先需要借助操作系统提供的一些工具，来定位消耗 CPU 过高的 native 线程。不同的操作系统，提供的不同的 CPU 统计命令如下所示：

操作系统|	solaris	|linux	|aix
-------|----------|--------|-----
命令名称|prstat -L <pid>|top -p <pid>|ps -emo THREAD

以 Linux 为例，首先通过 top -p <pid> 输出该进程的信息，然后输入 H，查看所有的线程的统计情况
```
top - 02:04:54 up  2:43,  3 users,  load average: 0.10, 0.05, 0.05
Threads:  13 total,   0 running,  13 sleeping,   0 stopped,   0 zombie
%Cpu(s):  97.74 us,  0.2 sy,  0.0 ni, 2.22 id,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st
KiB Mem:   1003456 total,   722012 used,   281444 free,        0 buffers
KiB Swap:  2097148 total,    62872 used,  2034276 free.    68880 cached Mem

PID USER PR NI VIRT RES SHR S %CPU %MEM TIME+ COMMAND
3368 zmw2 25 0 256m 9620 6460 R 93.3 0.7 5:42.06 java
3369 zmw2 15 0 256m 9620 6460 S 0.0 0.7 0:00.00 java
3370 zmw2 15 0 256m 9620 6460 S 0.0 0.7 0:00.00 java
3371 zmw2 15 0 256m 9620 6460 S 0.0 0.7 0:00.00 java
3372 zmw2 15 0 256m 9620 6460 S 0.0 0.7 0:00.00 java
3373 zmw2 15 0 256m 9620 6460 S 0.0 0.7 0:00.00 java
3374 zmw2 15 0 256m 9620 6460 S 0.0 0.7 0:00.00 java
3375 zmw2 15 0 256m 9620 6460 S 0.0 0.7 0:00.00 java
```
这个命令输出的PID代表的是 native 线程的id，如上所示，id为 3368 的 native 线程消耗 CPU最高。在Java ThreadDump文件中，每个线程都有tid=...nid=...的属性，其中nid就是native thread id，只不过nid中用16进制来表示。例如上面的例子中3368的十六进制表示为0xd28.在Java线程中查找nid=0xd28即是本地线程对应Java线程

```
"main" prio=1 tid=0x0805c988 nid=0xd28 runnable [0xfff65000..0xfff659c8]
at java.lang.String.indexOf(String.java:1352)
at java.io.PrintStream.write(PrintStream.java:460)
- locked <0xc8bf87d8> (a java.io.PrintStream)
at java.io.PrintStream.print(PrintStream.java:602)
at MyTest.fun2(MyTest.java:16)
- locked <0xc8c1a098> (a java.lang.Object)
at MyTest.fun1(MyTest.java:8)
- locked <0xc8c1a090> (a java.lang.Object)
at MyTest.main(MyTest.java:26)
```
导致 CPU 过高的原因有以下几种原因：
- Java 代码死循环
- Java 代码使用了复杂的算法，或者频繁调用
- JVM 自身的代码导致 CPU 很高

如果在Java线程堆栈中找到了对应的线程ID,并且该Java线程正在执行Native code,说明导致CPU过高的问题代码在JNI调用中，此时需要打印出 Native 线程的线程栈，在 linux 下，使用 pstack <pid> 命令。

如果在 native 线程堆栈中可以找到对应的消耗 CPU 过高的线程 id，可以直接定位为 native 代码的问题;

### 4.3、定位性能下降原因
性能下降一般是由于资源不足所导致。如果资源不足， 那么有大量的线程在等待资源， 打印的线程堆栈如果发现大量的线程停在同样的调用上下文上， 那么就说明该系统资源是瓶颈；

导致资源不足的原因可能有：
- 资源数量配置太少（如连接池连接配置过少等），而系统当前的压力比较大，资源不足导致了某些线程不能及时获得资源而等待在那里(即挂起)；
- 获得资源的线程把持资源时间太久， 导致资源不足，例如以下代码：
    ```
    void fun1() {
        Connection conn = ConnectionPool.getConnection();//获取一个数据库连接
        //使用该数据库连接访问数据库
        //数据库返回结果，访问完成
        //做其它耗时操作,但这些耗时操作数据库访问无关，
        conn.close(); //释放连接回池
    }
    ```
- 设计不合理导致资源占用时间过久，如SQL语句设计不恰当，或者没有索引导致的数据库访问太慢等；
- 资源用完后，在某种异常情况下，没有关闭或者回池，导致可用资源泄漏或者减少，从而导致资源竞；

### 4.4、定位系统假死原因

导致系统挂死的原因有很多，其中有一个最常见的原因是线程挂死。每次打印线程堆栈，该线程必然都在同一个调用上下文上，因此定位该类型的问题原理是，通过打印多次堆栈，找出对应业务逻辑使用的线程， 通过对比前后打印的堆栈确认该线程执行的代码段是否一直没有执行完成。 通过打印多次堆栈，找到挂起的线程（即不退出）；

导致线程无法退出的原因可能有：
- 线程正在执行死循环的代码
- 资源不足或者资源泄漏，造成当前线程阻塞在锁对象上（即wait在锁对象上），长期得不到唤醒(notify)。
- 如果当前程序和外部通信，当外部程序挂起无返回时，也会导致当前线程挂起

# 二、Java线上问题排查思路

[Java 线上问题排查思路与工具使用](https://blog.csdn.net/gitchat/article/details/79019454)

## 1、常见线上问题
所有 Java 服务的线上问题从系统表象来看归结起来总共有四方面：CPU、内存、磁盘、网络，基于这些现象我们可以将线上问题分成两大类: 系统异常、业务服务异常

- **系统异常**

    常见的系统异常现象包括：CPU 占用率过高、CPU 上下文切换频率次数较高、磁盘满了、磁盘 I/O 过于频繁、网络流量异常（连接数过多）、系统可用内存长期处于较低值（导致oom killer）等等；

    这些问题可以通过 top（cpu）、free（内存）、df（磁盘）、dstat（网络流量)=）、pstack、vmstat、strace（底层系统调用）等工具获取系统异常现象数据

- **业务异常**

    常见的业务服务异常现象包括: PV 量过高、服务调用耗时异常、线程死锁、多线程并发问题、频繁进行 Full GC、异常安全攻击扫描等

## 2、问题定位

一般会采用排除法，从外部排查到内部排查的方式来定位线上服务问题：
- 首先我们要排除其他进程 (除主进程之外) 可能引起的故障问题；
- 然后排除业务应用可能引起的故障问题；
- 可以考虑是否为运营商或者云服务提供商所引起的故障

### 2.1、定位流程

- 系统异常排查流程

    ![image](https://github.com/chenlanqing/learningNote/blob/master/Java/线上问题排查/image/系统异常排查流程.gif)

- 业务异常排查流程

    ![image](https://github.com/chenlanqing/learningNote/blob/master/Java/线上问题排查/image/业务异常排查流程.gif)

## 3、Linux常用的性能分析工具
Linux 常用的性能分析工具使用包括：top（cpu）、free（内存）、df（磁盘）、dstat（网络流量)=）、pstack、vmstat、strace（底层系统调用）

### 3.1、CPU
CPU 是系统重要的监控指标，能够分析系统的整体运行状况。监控指标一般包括运行队列、CPU 使用率和上下文切换等；

top命令是Linux下常用的CPU性能分析工具，能够实时显示系统中各个进程的资源占用状况，常用于服务端性能分析

![image](https://github.com/chenlanqing/learningNote/blob/master/Java/线上问题排查/image/top.jpg)

top命令显示了各个进程CPU使用情况，一般CPU使用率从高到低排序展示输出。其中Load Average显示最近1分钟、5分钟和15分钟的系统平均负载，上图各值为0.33、0.15、0.05

一般会关注 CPU 使用率最高的进程

- PID : 进程 id
- USER : 进程所有者
- PR : 进程优先级
- NI : nice 值。负值表示高优先级，正值表示低优先级
- VIRT : 进程使用的虚拟内存总量，单位 kb。VIRT=SWAP+RES
- RES : 进程使用的、未被换出的物理内存大小，单位 kb。RES=CODE+DATA
- SHR : 共享内存大小，单位 kb
- S : 进程状态。D= 不可中断的睡眠状态 R= 运行 S= 睡眠 T= 跟踪 / 停止 Z= 僵尸进程
- %CPU : 上次更新到现在的 CPU 时间占用百分比
- %MEM : 进程使用的物理内存百分比
- TIME+ : 进程使用的 CPU 时间总计，单位 1/100 秒
- COMMAND : 进程名称

### 3.2、内存
内存是排查线上问题的重要参考依据，内存问题很多时候是引起 CPU 使用率较高的见解因素

系统内存：free是显示的当前内存的使用，-m 的意思是 M 字节来显示内容

![image](https://github.com/chenlanqing/learningNote/blob/master/Java/线上问题排查/image/free.jpg)

部分参数：
- total 内存总数: 979M
- used 已经使用的内存数: 247M
- free 空闲的内存数: 732M
- shared 当前已经废弃不用 , 总是 0
- buffers Buffer 缓存内存数: 0M

### 3.3、磁盘

- df
```
[root@localhost ~]# df -h
文件系统                 容量   已用   可用   已用%   挂载点
/dev/mapper/centos-root   18G  1.6G   16G    9%     /
devtmpfs                 484M     0  484M    0%     /dev
tmpfs                    490M     0  490M    0%     /dev/shm
tmpfs                    490M  6.6M  484M    2%     /run
tmpfs                    490M     0  490M    0%     /sys/fs/cgroup
/dev/sda1                497M   96M  401M   20%     /boot
```
- du命令是查看当前指定文件或目录（会递归显示子目录）占用磁盘空间大小

### 3.4、网络
dstat 命令可以集成了 vmstat、iostat、netstat 等等工具能完成的任务， 

dstat参数：
- -c：cpu 情况
- -d：磁盘读写
- -n：网络状况
- -l：显示系统负载
- -m：显示形同内存状况
- -p：显示系统进程信息
- -r：显示系统IO 情况

```
root@localhost ~]# dstat
You did not select any stats, using -cdngy by default.
----total-cpu-usage---- -dsk/total- -net/total- ---paging-- ---system--
usr sys idl wai hiq siq| read  writ| recv  send|  in   out | int   csw 
  0   1  99   1   0   0| 113k   22k|   0     0 |   0     0 |  53   103 
  0   0 100   0   0   0|   0     0 | 180B  934B|   0     0 |  21    20 
  0   0 100   0   0   0|   0     0 |  60B  406B|   0     0 |  20    19 
  0   0 100   0   0   0|   0     0 | 120B  406B|   0     0 |  16    12 
  0   0 100   0   0   0|   0     0 | 419B  346B|   0     0 |  27    33 
  0   0 100   0   0   0|   0     0 | 180B  346B|   0     0 |  18    18 
  0   1  99   0   0   0|   0   396k| 120B  346B|   0     0 |  76   133 
  0   0 100   0   0   0|   0     0 | 212B  346B|   0     0 |  36    57 
  0   0 100   0   0   0|   0     0 |  60B  346B|   0     0 |  38    55 
  0   0 100   0   0   0|   0     0 | 212B  466B|   0     0 |  41    63 
  0   0 100   0   0   0|   0     0 | 451B  346B|   0     0 |  44    70 
  0   0 100   0   0   0|   0     0 | 212B  346B|   0     0 |  38    58 
```

### 3.5、其他
vmstat：是Virtual Meomory Statistics（虚拟内存统计）的缩写，是实时系统监控工具；该命令通过使用 knlist 子程序和 /dev/kmen 伪设备驱动器访问这些数据，输出信息直接打印在屏幕



# 线上问题排查神器：btrace





# 参考资料

* [Btrace简介](https://www.jianshu.com/p/dbb3a8b5c92f)
* [使用线程栈](https://www.jianshu.com/p/e92532f29349)
* [Java 线上问题排查思路与工具使用](https://blog.csdn.net/gitchat/article/details/79019454)
* [java问题排查工具单](https://yq.aliyun.com/articles/69520?utm_content=m_10360)
* [Java问题排查工具箱](https://mp.weixin.qq.com/s/X4l9LhjZybqr5jc7RLfcOA)
* [线程堆栈分析](http://fastthread.io/)