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

