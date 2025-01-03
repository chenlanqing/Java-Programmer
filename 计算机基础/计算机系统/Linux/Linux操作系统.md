# 1、内核与内核设计

- 一种程序：
    - 管理硬件、资源和应用程序的能力

# 2、进程与线程

主要从调度、并发性、拥有的资源和系统开销等方面

进程是操作系统分配资源的最小单元，线程是操作系统调度的最小单元

## 2.1、进程

### 2.1.1、概述

- 进程表示一个逻辑控制流，就是一种计算过程，它造成一个假象，好像这个进程一直在独占CPU资源；
- 进程拥有一个独立的虚拟内存地址空间，它造成一个假象，好像这个进程一致在独占存储器资源；
- 操作系统利用fork系统调用来创建一个子进程。fork所创建的子进程会复制父进程的虚拟地址空间；
    - fork刚创建的子进程采用了共享的方式，只用指针指向了父进程的物理资源。当子进程真正要对某些物理资源写操作时，才会真正的复制一块物理资源来供子进程使用；
    - fork不只是复制了页表结构，还复制了父进程的文件描述符表，信号控制表，进程信息，寄存器资源等等；
    - fork创建的子进程开始执行的位置是fork函数返回的位置；
    - fork创建的进程pid = 0；
- 进程不能通过直接共享内存的方式来进行进程间通信，只能采用信号、管道等方式来进行进程间通信；

### 2.1.2、进程的状态

- 运行：进程在执行程序
- 阻塞：进程在等待某个外部信号，比如打印完成、读取磁盘完成
- 就绪：进程在排队等待执行

阻塞态可以知己到运行态吗？就绪态可以直接到阻塞态吗？

**进程如何响应中断：**
- 保存当前状态：中断程序第一时间保存当前状态；
- 跳转OS中断响应程序
- 保存当前寄存器：必须保存当前的寄存器确报不受污染；
- 设置新的栈指针：设置新的SP保证栈不受污染；
- 执行中断服务程序：读取中断关联的数据，例如形成缓冲区；
- 执行下一个进程

### 2.1.3、进程资源

进程上下文切换的资源：
- 页表 -- 对应虚拟内存资源
- 文件描述符表/打开文件表 -- 对应打开的文件资源
- 寄存器 -- 对应运行时数据
- 信号控制信息/进程运行信息

### 2.1.4、进程间通信

- 管道pipe：管道是一种半双工的通信方式，数据只能单向流动，而且只能在具有亲缘关系的进程间使用。进程的亲缘关系通常是指父子进程关系
- 命名管道FIFO：有名管道也是半双工的通信方式，但是它允许无亲缘关系进程间的通信；
- 消息队列MessageQueue：消息队列是由消息的链表，存放在内核中并由消息队列标识符标识。消息队列克服了信号传递信息少、管道只能承载无格式字节流以及缓冲区大小受限等缺点；
- 文件
- 共享存储SharedMemory：共享内存就是映射一段能被其他进程所访问的内存，这段共享内存由一个进程创建，但多个进程都可以访问。共享内存是最快的 IPC 方式，它是针对其他进程间通信方式运行效率低而专门设计的。它往往与其他通信机制，如信号两，配合使用，来实现进程间的同步和通信；
- 信号量Semaphore：信号量是一个计数器，可以用来控制多个进程对共享资源的访问。它常作为一种锁机制，防止某进程正在访问共享资源时，其他进程也访问该资源。因此，主要作为进程间以及同一进程内不同线程之间的同步手段；Semaphore 内部封装一个整数（P）和一个睡眠线程集合（S），并提供两个原子操作：
    - up()
        * p=p+1
        * 如果s.size() > 1，从s中选择一个唤醒并执行；
    - down()
        * if(p == 0) (sleep(&Semaphore))
        * p--
- 套接字Socket：套接字也是一种进程间通信机制，与其他通信机制不同的是，它可用于不同及其间的进程通信
- 信号(sinal) ： 信号是一种比较复杂的通信方式，用于通知接收进程某个事件已经发生。

## 2.2、线程

- 线程解决的最大问题就是它可以很简单地表示共享资源的问题，这里说的资源指的是存储器资源，资源最后都会加载到物理内存，一个进程的所有线程都是共享这个进程的同一个虚拟地址空间的，也就是说从线程的角度来说，它们看到的物理资源都是一样的，这样就可以通过共享变量的方式来表示共享资源，也就是直接共享内存的方式解决了线程通信的问题；
- 在Linux系统中，线程是使用clone系统调用，clone是一个轻量级的fork；它提供了一系列的参数来表示线程可以共享父类的哪些资源;
- 线程上下文切换需保存的内容：线程的id、寄存器中的值、栈数据、状态

**谁创建了线程**

**谁销毁线程**

**谁调度线程：** 内核负责调度线程

## 2.3、[协程](https://en.wikipedia.org/wiki/Coroutine)

- [在性能优化中使用协程](../../../性能优化/性能优化.md#5协程)
- [协程（coroutine）简介](https://yearn.xyz/posts/techs/%E5%8D%8F%E7%A8%8B/)

协程就是用户态的线程；协程在异步化之上包了一层外衣，兼顾了开发效率与运行效率；

**协程如何实现高并发**

协程与异步编程相似的地方在于，它们必须使用非阻塞的系统调用与内核交互，把切换请求的权力牢牢掌握在用户态的代码中。但不同的地方在于，协程把异步化中的两段函数（业务执行和回调函数），封装为一个阻塞的协程函数。这个函数执行时，会使调用它的协程无感知地放弃执行权，由协程框架切换到其他就绪的协程继续执行。当这个函数的结果满足后，协程框架再选择合适的时机，切换回它所在的协程继续执行。

**协程的切换是如何完成**

用户态的代码切换协程，与内核切换线程的原理是一样的。内核通过管理 CPU 的寄存器来切换线程，以最重要的栈寄存器和指令寄存器为例，看看协程切换时如何切换程序指令与内存；
- 线程的切换：每个线程有独立的栈，而栈既保留了变量的值，也保留了函数的调用关系、参数和返回值，CPU 中的栈寄存器 SP 指向了当前线程的栈，而指令寄存器 IP 保存着下一条要执行的指令地址。因此，从线程 1 切换到线程 2 时，首先要把 SP、IP 寄存器的值为线程 1 保存下来，再从内存中找出线程 2 上一次切换前保存好的寄存器值，写入 CPU 的寄存器，这样就完成了线程切换
- 协程的切换与此相同，只是把内核的工作转移到协程框架实现而已 - 创建协程时，会从进程的堆中分配一段内存作为协程的栈。线程的栈有 8MB，而协程栈的大小通常只有几十 KB。而且，C 库内存池也不会为协程预分配内存，它感知不到协程的存在。这样，更低的内存占用空间为高并发提供了保证，毕竟十万并发请求，就意味着 10 万个协程。当然，栈缩小后，就尽量不要使用递归函数，也不能在栈中申请过多的内存，这是实现高并发必须付出的代价；

## 2.4、区别

- 进程采用fork创建，线程采用clone创建；
- 线程是程序执行的最小单位，而进程是操作系统分配资源的最小单位；一个进程由一个或多个线程组成，线程是一个进程中代码的不同执行路线；
- 进程fork创建的子进程的逻辑流位置在fork返回的位置，线程clone创建的KSE的逻辑流位置在clone调用传入的方法位置，比如Java的Thread的run方法位置；
- 进程拥有独立的虚拟内存地址空间和内核数据结构(页表，打开文件表等)，当子进程修改了虚拟页之后，会通过写时拷贝创建真正的物理页。线程共享进程的虚拟地址空间和内核数据结构，共享同样的物理页；
- 多个进程通信只能采用进程间通信的方式，比如信号，管道，而不能直接采用简单的共享内存方式，原因是每个进程维护独立的虚拟内存空间，所以每个进程的变量采用的虚拟地址是不同的。多个线程通信就很简单，直接采用共享内存的方式，因为不同线程共享一个虚拟内存地址空间，变量寻址采用同一个虚拟内存；
- 进程上下文切换需要切换页表等重量级资源，线程上下文切换只需要切换寄存器等轻量级数据；
- 进程的用户栈独享栈空间，线程的用户栈共享虚拟内存中的栈空间，没有进程高效；
- 一个应用程序可以有多个进程，执行多个程序代码，多个线程只能执行一个程序代码，共享进程的代码段；
- 进程采用父子结构，线程采用对等结构

# 3、存储与寻址

CPU不仅采用了扩充的存储器段式管理机制，而且还提供了可选的存储器分页管理机制

# 4、中断的概念和流程

# 5、调度

https://mp.weixin.qq.com/s/ZifCDDU-zdUa7fOFo7epiw

## 5.1、调度任务特征


## 5.2、调度时机


## 5.3、调度算法

# 6、内存管理

- [Memory Barriers: a Hardware View for Software Hackers](https://www.puppetmastertrading.com/images/hwViewForSwHackers.pdf)
- [A Tutorial Introduction to the ARM and POWER Relaxed Memory Models](https://www.cl.cam.ac.uk/~pes20/ppc-supplemental/test7.pdf)
- [x86-TSO: A Rigorous and Usable Programmer’s Model for x86 Multiprocessors](https://www.cl.cam.ac.uk/~pes20/weakmemory/cacm.pdf)

## 6.1、分层存储体系

## 6.2、地址空间

地址空间是进程可以用来寻址的独立地址集合


如果操作系统允许应用程序访问内存会造成什么后果？

多个进程如何复用两个寄存器？进程表中存储了所有用到寄存器

内存超载的解决办法：
- 交换
- 虚拟内存

如何解决进程增长空间不足的问题？
- 将内存用数据结构组织，切割成更小的块，一块块的分配给每个进程；

如何解决大应用存回磁盘缓慢的问题？
- 存一部分

MMU：内存管理单元


# 7、CPU上下文

每个任务运行前，CPU 都需要知道任务从哪里加载、又从哪里开始运行，也就是说，需要系统事先帮它设置好 **CPU 寄存器和程序计数器（Program Counter，PC）**
- CPU 寄存器，是 CPU 内置的容量小、但速度极快的内存。
- 程序计数器，则是用来存储 CPU 正在执行的指令位置、或者即将执行的下一条指令位置。<br/>
它们都是 CPU 在运行任何任务前，必须的依赖环境，因此也被叫做 **CPU 上下文**

**CPU 上下文切换**：就是先把前一个任务的 CPU 上下文（也就是 CPU 寄存器和程序计数器）保存起来，然后加载新任务的上下文到这些寄存器和程序计数器，最后再跳转到程序计数器所指的新位置，运行新任务。

根据任务的不同，CPU 的上下文切换就可以分为几个不同的场景：
- 进程上下文切换
- 线程上下文切换
- 中断上下文切换

## 7.1、进程上下文切换

Linux 按照特权等级，把进程的运行空间分为内核空间和用户空间，分别对应着下图中， CPU 特权等级的 Ring 0 和 Ring 3
- 内核空间（Ring 0）具有最高权限，可以直接访问所有资源；
- 用户空间（Ring 3）只能访问受限资源，不能直接访问内存等硬件设备，必须通过系统调用陷入到内核中，才能访问这些特权资源。

![](image/CPU特权等级.png)

换个角度看，进程既可以在用户空间运行，又可以在内核空间中运行。进程在用户空间运行时，被称为进程的用户态，而陷入内核空间的时候，被称为进程的内核态

从用户态到内核态的转变，需要通过**系统调用**来完成。比如，当我们查看文件内容时，就需要多次系统调用来完成：首先调用 open() 打开文件，然后调用 read() 读取文件内容，并调用 write() 将内容写到标准输出，最后再调用 close() 关闭文件；

系统调用的过程中，发生 CPU 上下文的切换：
- CPU 寄存器里原来用户态的指令位置，需要先保存起来。接着，为了执行内核态代码，CPU 寄存器需要更新为内核态指令的新位置。最后才是跳转到内核态运行内核任务。
- 系统调用结束后，CPU 寄存器需要恢复原来保存的用户态，然后再切换到用户空间，继续运行进程。所以，一次系统调用的过程，其实是发生了两次 CPU 上下文切换

系统调用过程中，并不会涉及到虚拟内存等进程用户态的资源，也不会切换进程。这跟通常所说的进程上下文切换是不一样的：
- 进程上下文切换，是指从一个进程切换到另一个进程运行。
- 而系统调用过程中一直是同一个进程在运行；因此：系统调用过程通常称为特权模式切换，而不是上下文切换

进程上下文切换跟系统调用的区别：
- **进程是由内核来管理和调度的，进程的切换只能发生在内核态**。所以，进程的上下文不仅包括了虚拟内存、栈、全局变量等用户空间的资源，还包括了内核堆栈、寄存器等内核空间的状态；
- 进程的上下文切换就比系统调用时多了一步：在保存当前进程的内核状态和 CPU 寄存器之前，需要先把该进程的虚拟内存、栈等保存下来；而加载了下一进程的内核态后，还需要刷新进程的虚拟内存和用户栈

每次上下文切换都需要几十纳秒到数微秒的 CPU 时间，详细参考：[How long does it take to make a context switch?](https://blog.tsunanet.net/2010/11/how-long-does-it-take-to-make-context.html)，在进程上下文切换次数较多的情况下，很容易导致 CPU 将大量时间耗费在寄存器、内核栈以及虚拟内存等资源的保存和恢复上，进而大大缩短了真正运行进程的时间

另外 Linux 通过 TLB（Translation Lookaside Buffer）来管理虚拟内存到物理内存的映射关系。所以当虚拟内存更新后，TLB 也需要刷新，内存的访问也会随之变慢

**什么时候会切换进程上下文？**

进程切换时才需要切换上下文，换句话说，只有在进程调度的时候，才需要切换上下文。Linux 为每个 CPU 都维护了一个就绪队列，将活跃进程（即正在运行和正在等待 CPU 的进程）按照优先级和等待 CPU 的时间排序，然后选择最需要 CPU 的进程，也就是优先级最高和等待 CPU 时间最长的进程来运行

进程在什么时候才会被调度到 CPU 上运行呢？
- 进程执行完终止了；
- 当某个进程的时间片耗尽了，就会被系统挂起，切换到其它正在等待 CPU 的进程运行；
- 进程在系统资源不足（比如内存不足）时，要等到资源满足后才可以运行，这个时候进程也会被挂起，并由系统调度其他进程运行；
- 当进程通过睡眠函数 sleep 这样的方法将自己主动挂起时，自然也会重新调度；
- 当有优先级更高的进程运行时，为了保证高优先级进程的运行，当前进程会被挂起，由高优先级进程来运行；
- 发生硬件中断时，CPU 上的进程会被中断挂起，转而执行内核中的中断服务程序；

## 7.2、线程上下文切换

线程与进程最大的区别在于，线程是调度的基本单位，而进程则是资源拥有的基本单位。所谓内核中的任务调度，实际上的调度对象是线程；而进程只是给线程提供了虚拟内存、全局变量等资源；

线程有自己的私有数据，比如栈和寄存器等，这些在上下文切换时是需要保存的。线程的上下文切换其实就可以分为两种情况：
- 前后两个线程属于不同进程。此时，因为资源不共享，所以切换过程就跟进程上下文切换是一样。
- 前后两个线程属于同一个进程。此时，因为虚拟内存是共享的，所以在切换时，虚拟内存这些资源就保持不动，只需要切换线程的私有数据、寄存器等不共享的数据

## 7.3、中断上下文切换

为了快速响应硬件的事件，中断处理会打断进程的正常调度和执行，转而调用中断处理程序，响应设备事件。而在打断其他进程时，就需要将进程当前的状态保存下来，这样在中断结束后，进程仍然可以从原来的状态恢复运行

中断上下文切换不涉及到进程的用户态。所以，即便中断过程打断了一个正处在用户态的进程，也不需要保存和恢复这个进程的虚拟内存、全局变量等用户态资源。中断上下文，其实只包括内核态中断服务程序执行所必需的状态，包括 CPU 寄存器、内核堆栈、硬件中断参数等。

对同一个 CPU 来说，中断处理比进程拥有更高的优先级，所以**中断上下文切换并不会与进程上下文切换同时发生**。中断会打断正常进程的调度和执行

另外，跟进程上下文切换一样，中断上下文切换也需要消耗 CPU，切换次数过多也会耗费大量的 CPU，甚至严重降低系统的整体性能。

## 7.4、查看系统的上下文切换

过多的上下文切换，会把 CPU 时间消耗在寄存器、内核栈以及虚拟内存等数据的保存和恢复上，缩短进程真正运行的时间，成了系统性能大幅下降的一个元凶

使用 `vmstat` 这个工具，来查询系统的上下文切换情况，`vmstat`是一个常用的系统性能分析工具，主要分析系统的内存使用情况，也可分析 CPU 上下文切换和中断的次数
```bash
# 每隔 5 秒输出 1 组数据
$ vmstat 5
procs -----------memory---------- ---swap-- -----io---- -system-- ------cpu-----
 r  b   swpd   free   buff  cache   si   so    bi    bo   in    cs us sy id  wa st
 0  0      0 7005360  91564 818900    0    0     0     0   25   33  0  0 100  0  0
```
特别关注的四列内容：
- `cs（context switch）`是每秒上下文切换的次数。
- `in（interrupt）`则是每秒中断的次数。
- `r（Running or Runnable）`是就绪队列的长度，也就是正在运行和等待 CPU 的进程数。
- `b（Blocked）`则是处于不可中断睡眠状态的进程数。

`pidstat` 查看每个进程的详细情况
```bash
# 每隔 5 秒输出 1 组数据
$ pidstat -w 5
Linux 4.15.0 (ubuntu)  09/23/18  _x86_64_  (2 CPU)
08:18:26      UID       PID   cswch/s nvcswch/s  Command
08:18:31        0         1      0.20      0.00  systemd
08:18:31        0         8      5.40      0.00  rcu_sched
...
```
- 一个是 cswch ，表示每秒自愿上下文切换（voluntary context switches）的次数；所谓**自愿上下文切换**，是指进程无法获取所需资源，导致的上下文切换。比如说， I/O、内存等系统资源不足时，就会发生自愿上下文切换
- 另一个则是 nvcswch ，表示每秒非自愿上下文切换（non voluntary context switches）的次数；**非自愿上下文切换**，则是指进程由于时间片已到等原因，被系统强制调度，进而发生的上下文切换。比如说，大量进程都在争抢 CPU 时，就容易发生非自愿上下文切换

## 7.5、例子

可以使用 sysbench 来模拟系统多线程调度切换的情况，比如：
```bash
# 以 10 个线程运行 5 分钟的基准测试，模拟多线程切换的问题
$ sysbench --threads=10 --max-time=300 threads run
```
观察情况：
```bash
# 每隔 1 秒输出 1 组数据（需要 Ctrl+C 才结束）
$ vmstat 1
procs -----------memory---------- ---swap-- -----io---- -system-- ------cpu-----
 r  b   swpd   free   buff  cache   si   so    bi    bo   in   cs us sy id wa st
 6  0      0 6487428 118240 1292772    0    0     0     0 9019 1398830 16 84  0  0  0
 8  0      0 6487428 118240 1292772    0    0     0     0 10191 1392312 16 84  0  0  0
```
系统的就绪队列(`r`)过长，也就是正在运行和等待 CPU 的进程数过多，导致了大量的上下文切换，而上下文切换又导致了系统 CPU 的占用率升高；

使用 pidstat 观察
```bash
# 每隔 1 秒输出 1 组数据（需要 Ctrl+C 才结束）
# -w 参数表示输出进程切换指标，而 -u 参数则表示输出 CPU 使用指标
$ pidstat -w -u 1
08:06:33      UID       PID    %usr %system  %guest   %wait    %CPU   CPU  Command
08:06:34        0     10488   30.00  100.00    0.00    0.00  100.00     0  sysbench
08:06:34        0     26326    0.00    1.00    0.00    0.00    1.00     0  kworker/u4:2
08:06:33      UID       PID   cswch/s nvcswch/s  Command
08:06:34        0         8     11.00      0.00  rcu_sched
```
在 vmstat 命令的结果中，中断次数也上升到了 1 万，如何知道中断发生的类型，从 `/proc/interrupts` 这个只读文件中读取。`/proc` 实际上是 Linux 的一个虚拟文件系统，用于内核空间与用户空间之间的通信。`/proc/interrupts` 就是这种通信机制的一部分，提供了一个只读的中断使用情况
```bash
# -d 参数表示高亮显示变化的区域
$ watch -d cat /proc/interrupts
           CPU0       CPU1
...
RES:    2450431    5279697   Rescheduling interrupts
...
```
变化速度最快的是`重调度中断（RES）`，这个中断类型表示，唤醒空闲状态的 CPU 来调度新的任务运行。这是多处理器系统（SMP）中，调度器用来分散任务到不同 CPU 的机制，通常也被称为处理器间中断（Inter-Processor Interrupts，IPI）

## 7.6、总结

**每秒上下文切换多少次才算正常？** 这个数值其实取决于系统本身的 CPU 性能。如果系统的上下文切换次数比较稳定，那么从数百到一万以内，都应该算是正常的。但当上下文切换次数超过一万次，或者切换次数出现数量级的增长时，就很可能已经出现了性能问题，可能得问题：
- 自愿上下文切换变多了，说明进程都在等待资源，有可能发生了 I/O 等其他问题；
- 非自愿上下文切换变多了，说明进程都在被强制调度，也就是都在争抢 CPU，说明 CPU 的确成了瓶颈；
- 中断次数变多了，说明 CPU 被中断处理程序占用，还需要通过查看 `/proc/interrupts` 文件来分析具体的中断类型

# 8、CPU使用率

## 8.1、节拍率

为了维护 CPU 时间，Linux 通过事先定义的节拍率（内核中表示为 HZ），触发时间中断，并使用全局变量 Jiffies 记录了开机以来的节拍数。每发生一次时间中断，Jiffies 的值就加 1；

节拍率 HZ 是内核的可配选项，可以设置为 100、250、1000 等，查看节拍率，可以通过查询 /boot/config 内核选项
```bash
$ grep 'CONFIG_HZ=' /boot/config-$(uname -r)
CONFIG_HZ=1000
```
如上，节拍率设置成了 1000，也就是每秒钟触发 1000 次时间中断；

因为节拍率 HZ 是内核选项，所以用户空间程序并不能直接访问。为了方便用户空间程序，内核还提供了一个用户空间节拍率 USER_HZ，它总是固定为 100，也就是 1/100 秒。这样，用户空间程序并不需要关心内核中 HZ 被设置成了多少，因为它看到的总是固定值 USER_HZ

## 8.2、CPU使用率

Linux 通过 /proc 虚拟文件系统，向用户空间提供了系统内部状态的信息，而 /proc/stat 提供的就是系统的 CPU 和任务统计信息，只关注 CPU 的话，可以执行下面的命令：
```bash
# 只保留各个 CPU 的数据
$ cat /proc/stat | grep ^cpu
cpu  280580 7407 286084 172900810 83602 0 583 0 0 0
cpu0 144745 4181 176701 86423902 52076 0 301 0 0 0
cpu1 135834 3226 109383 86476907 31525 0 282 0 0 0
```
其中，第一列表示的是 CPU 编号，如 cpu0、cpu1 ，而第一行没有编号的 cpu ，表示的是所有 CPU 的累加。其他列则表示不同场景下 CPU 的累加节拍数，它的单位是 USER_HZ，也就是 10 ms（1/100 秒），所以这其实就是不同场景下的 CPU 时间。每一列的含义：
- user（通常缩写为 us），代表用户态 CPU 时间。注意，它不包括下面的 nice 时间，但包括了 guest 时间。
- nice（通常缩写为 ni），代表低优先级用户态 CPU 时间，也就是进程的 nice 值被调整为 1-19 之间时的 CPU 时间。这里注意，nice 可取值范围是 -20 到 19，数值越大，优先级反而越低。
- system（通常缩写为 sys），代表内核态 CPU 时间。
- idle（通常缩写为 id），代表空闲时间。注意，它不包括等待 I/O 的时间（iowait）。
- iowait（通常缩写为 wa），代表等待 I/O 的 CPU 时间。
- irq（通常缩写为 hi），代表处理硬中断的 CPU 时间。
- softirq（通常缩写为 si），代表处理软中断的 CPU 时间。
- steal（通常缩写为 st），代表当系统运行在虚拟机中的时候，被其他虚拟机占用的 CPU 时间。
- guest（通常缩写为 guest），代表通过虚拟化运行其他操作系统的时间，也就是运行虚拟机的 CPU 时间。
- guest_nice（通常缩写为 gnice），代表以低优先级运行虚拟机的时间。

CPU 使用率，就是除了空闲时间外的其他时间占总 CPU 时间的百分比，用公式来表示就是：

$$ CPU使用率 = 1 - \frac{空闲时间}{总CPU时间} $$

根据公式，可以从 `/proc/stat` 中的数据，很容易地计算出 CPU 使用率，这是开机以来的节拍数累加值，所以直接算出来的，是开机以来的平均 CPU 使用率，一般没啥参考价值；为了计算 CPU 使用率，性能工具一般都会取间隔一段时间（比如 3 秒）的两次值，作差后，再计算出这段时间内的平均 CPU 使用率，即

$$ 平均CPU使用率 = 1 - \frac{空闲时间_{new} - 空闲时间_{old}}{总CPU时间_{new} - 总CPU时间_{old}} $$

这个公式，就是各种性能工具所看到的 CPU 使用率的实际计算方法;

进程的CPU使用率：跟系统的指标类似，Linux 也给每个进程提供了运行情况的统计信息，也就是 `/proc/[pid]/stat`。不过，这个文件包含的数据就比较丰富了，总共有 52 列的数据

性能分析工具给出的都是间隔一段时间的平均 CPU 使用率，所以要注意间隔时间的设置

## 8.3、查看CPU使用率

top 和 ps 是最常用的性能分析工具：
- top 显示了系统总体的 CPU 和内存使用情况，以及各个进程的资源使用情况。
- ps 则只显示了每个进程的资源使用情况。

top的输出格式为：
```bash
# 默认每 3 秒刷新一次
$ top
top - 11:58:59 up 9 days, 22:47,  1 user,  load average: 0.03, 0.02, 0.00
Tasks: 123 total,   1 running,  72 sleeping,   0 stopped,   0 zombie
%Cpu(s):  0.3 us,  0.3 sy,  0.0 ni, 99.3 id,  0.0 wa,  0.0 hi,  0.0 si,  0.0 st
KiB Mem :  8169348 total,  5606884 free,   334640 used,  2227824 buff/cache
KiB Swap:        0 total,        0 free,        0 used.  7497908 avail Mem
 
  PID USER      PR  NI    VIRT    RES    SHR S  %CPU %MEM     TIME+ COMMAND
    1 root      20   0   78088   9288   6696 S   0.0  0.1   0:16.83 systemd
    2 root      20   0       0      0      0 S   0.0  0.0   0:00.05 kthreadd
    4 root       0 -20       0      0      0 I   0.0  0.0   0:00.00 kworker/0:0H
...
```
- 第三行 %Cpu 就是系统的 CPU 使用率，需要注意，top 默认显示的是所有 CPU 的平均值，这个时候你只需要按下数字 1 ，就可以切换到每个 CPU 的使用率了。
- 空白行之后是进程的实时信息，每个进程都有一个 %CPU 列，表示进程的 CPU 使用率。它是用户态和内核态 CPU 使用率的总和，包括进程用户空间使用的 CPU、通过系统调用执行的内核空间 CPU 、以及在就绪队列等待运行的 CPU。在虚拟化环境中，它还包括了运行虚拟机占用的 CPU；

top 并没有细分进程的用户态 CPU 和内核态 CPU，可以使用 pidstat ，它正是一个专门分析每个进程 CPU 使用情况的工具没，比如下面的 pidstat 命令，就间隔 1 秒展示了进程的 5 组 CPU 使用率，包括：
- 用户态 CPU 使用率 （%usr）；
- 内核态 CPU 使用率（%system）；
- 运行虚拟机 CPU 使用率（%guest）；
- 等待 CPU 使用率（%wait）；
- 总的 CPU 使用率（%CPU）。
```bash
# 每隔 1 秒输出一组数据，共输出 5 组
$ pidstat 1 5
15:56:02      UID       PID    %usr %system  %guest   %wait    %CPU   CPU  Command
15:56:03        0     15006    0.00    0.99    0.00    0.00    0.99     1  dockerd
...
Average:      UID       PID    %usr %system  %guest   %wait    %CPU   CPU  Command
Average:        0     15006    0.00    0.99    0.00    0.00    0.99     -  dockerd
```

## 8.4、CPU使用率过高

通过 top、ps、pidstat 等工具，能够轻松找到 CPU 使用率较高（比如 100% ）的进程。接下来，如何知道占用 CPU 的到底是代码里的哪个函数呢？
- GDB（The GNU Project Debugger），GDB 在调试程序错误方面很强大，但是因为 GDB 调试程序的过程会中断程序运行，这在线上环境往往是不允许的。所以，GDB 只适合用在性能分析的后期，当找到了出问题的大致函数后，线下再借助它来进一步调试函数内部的问题；
- perf 是 Linux 2.6.31 以后内置的性能分析工具。它以性能事件采样为基础，不仅可以分析系统的各种事件和内核性能，还可以用来分析指定应用程序的性能问题，使用 perf 分析 CPU 性能问题，两种常见的用法：
    -  perf top，类似于 top，它能够实时显示占用 CPU 时钟最多的函数或者指令，因此可以用来查找热点函数
        ```bash
        $ perf top
        Samples: 833  of event 'cpu-clock', Event count (approx.): 97742399
        Overhead  Shared Object       Symbol
        7.28%  perf                [.] 0x00000000001f78a4
        4.72%  [kernel]            [k] vsnprintf
        4.32%  [kernel]            [k] module_get_kallsym
        3.65%  [kernel]            [k] _raw_spin_unlock_irqrestore
        ...
        ```
        输出结果中，第一行包含三个数据，分别是采样数（Samples）、事件类型（event）和事件总数量（Event count）。比如这个例子中，perf 总共采集了 833 个 CPU 时钟事件，而总事件数则为 97742399，表格式样的数据，每一行包含四列，分别是：
        - 第一列 Overhead ，是该符号的性能事件在所有采样中的比例，用百分比来表示。
        - 第二列 Shared ，是该函数或指令所在的动态共享对象（Dynamic Shared Object），如内核、进程名、动态链接库名、内核模块名等。
        - 第三列 Object ，是动态共享对象的类型。比如 `[.]` 表示用户空间的可执行程序、或者动态链接库，而 `[k]` 则表示内核空间。
        - 最后一列 Symbol 是符号名，也就是函数名。当函数名未知时，用十六进制的地址来表示。
    - perf record 和 perf report：perf top 虽然实时展示了系统的性能信息，但它的缺点是并不保存数据，也就无法用于离线或者后续的分析。而 perf record 则提供了保存数据的功能，保存后的数据，需要你用 perf report 解析展示
        ```bash
        $ perf record # 按 Ctrl+C 终止采样
        [ perf record: Woken up 1 times to write data ]
        [ perf record: Captured and wrote 0.452 MB perf.data (6093 samples) ]
        $ perf report # 展示类似于 perf top 的报告
        ```
        还可以 perf top 和 perf record 加上 `-g` 参数，开启调用关系的采样，方便根据调用链来分析性能问题

## 8.5、案例：CPU使用率高特殊场景

- [CPU过高问题排查](../../../Java/实际场景/线上问题排查思路.md#42CPU过高)

系统的 CPU 使用率，不仅包括进程用户态和内核态的运行，还包括中断处理、等待 I/O 以及内核线程等。所以，当你发现系统的 CPU 使用率很高的时候，不一定能找到相对应的高 CPU 使用率的进程。

介绍两个工具：
- pstree： 可以用树状形式显示所有进程之间的关系
    ```bash
    pstree | grep stress
    ```
- [execsnoop](https://www.brendangregg.com/blog/2014-07-28/execsnoop-for-linux.html)： 使用 top、pidstat、pstree 等工具分析了系统 CPU 使用率高的问题，但是分析过程会比较复杂，execsnoop 就是一个专为短时进程设计的工具。它通过 ftrace 实时监控进程的 exec() 行为，并输出短时进程的基本信息，包括进程 PID、父进程 PID、命令行参数以及执行的结果；execsnoop 所用的 ftrace 是一种常用的动态追踪技术，一般用于分析 Linux 内核的运行时行为

碰到常规问题无法解释的 CPU 使用率情况时，首先要想到有可能是短时应用导致的问题，比如有可能是下面这两种情况。
- 第一，应用里直接调用了其他二进制程序，这些程序通常运行时间比较短，通过 top 等工具也不容易发现。
- 第二，应用本身在不停地崩溃重启，而启动过程的资源初始化，很可能会占用相当多的 CPU。

对于这类进程，可以用 pstree 或者 execsnoop 找到它们的父进程，再从父进程所在的应用入手，排查问题的根源

# 发行版

Linux 是一个开源的操作系统，有许多不同的发行版。以下是一些流行的 Linux 发行版，以及它们的官方网站链接：
- **Ubuntu**：Ubuntu 是最流行的 Linux 发行版之一，以其易用性和广泛的软件库而闻名。官方网站：[Ubuntu](https://ubuntu.com/)
- **Debian**：Debian 是一个稳定且可高度定制的 Linux 发行版，是许多其他发行版的基础。官方网站：[Debian](https://www.debian.org/)
-  **Fedora**：Fedora 是由 Red Hat 支持的社区驱动的 Linux 发行版，以其创新和快速更新而闻名。官方网站：[Fedora](https://getfedora.org/)
- **CentOS**：CentOS 是一个基于 Red Hat Enterprise Linux (RHEL) 的免费企业级 Linux 发行版。官方网站：[CentOS](https://www.centos.org/)
- **Arch Linux**：Arch Linux 是一个滚动发布的 Linux 发行版，以其简洁和强大的包管理器（AUR）而闻名。官方网站：[Arch Linux](https://www.archlinux.org/)
- **openSUSE**：openSUSE 是一个由 Novell（现为 SUSE）维护的 Linux 发行版，以其易用性和强大的软件库而闻名。官方网站：[openSUSE](https://www.opensuse.org/)
- **Manjaro**：Manjaro 是一个基于 Arch Linux 的发行版，旨在提供简单易用的安装过程和强大的软件库。最接口 OSX 系统的Linux发行版，官方网站：[Manjaro](https://manjaro.org/)
- **Gentoo**：Gentoo 是一个高度可定制的 Linux 发行版，以其强大的包管理器和灵活的配置选项而闻名。官方网站：[Gentoo](https://www.gentoo.org/)
- **Kali Linux**：Kali Linux 是一个基于 Debian 的发行版，专为渗透测试和安全审计而设计。官方网站：[Kali Linux](https://www.kali.org/)
- **Linux Mint**：Linux Mint 是一个基于 Ubuntu 的发行版，以其易用性和美观的用户界面而闻名。官方网站：[Linux Mint](https://linuxmint.com/)

# 参考资料

* [内存寻址](https://liam.page/2016/05/01/Introduction-to-Memory-Addressing/)
* [线程的调度](https://wizardforcel.gitbooks.io/wangdaokaoyan-os/content/8.html)
* [Linux内核碎片](https://pingcap.com/zh/blog/linux-kernel-vs-memory-fragmentation-2)
* [Linux 0.11-内核源码分析](https://github.com/dibingfa/flash-linux0.11-talk)
* [Linux启动流程](https://mp.weixin.qq.com/s/s1YpeLc9K-tX59REh9Wz0A)
* [Linux启动](https://opensource.com/article/17/2/linux-boot-and-startup)
* [Grub2 配置](https://opensource.com/article/17/3/introduction-grub2-configuration-linux)
* [一个64位操作系统的实现](https://github.com/yifengyou/The-design-and-implementation-of-a-64-bit-os)
* [操作系统-3个部分](https://pages.cs.wisc.edu/~remzi/OSTEP/)
* [Linux硬件-内核-网络](https://github.com/yanfeizhang/coder-kung-fu)
* [Red Hat Enterprise Linux 文档](https://docs.redhat.com/en/documentation/red_hat_enterprise_linux/9)
* [What every programmer should know about memory](https://people.freebsd.org/~lstewart/articles/cpumemory.pdf)

