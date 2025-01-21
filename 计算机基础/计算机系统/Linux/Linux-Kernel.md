
### 内核线程

在 Linux 中，用户态进程的“祖先”，都是 PID 号为 1 的 init 进程。现在主流的 Linux 发行版中，init 都是 systemd 进程；而其他的用户态进程，会通过 systemd 来进行管理。除了用户态进程外，还有大量的内核态线程。按说内核态的线程，应该先于用户态进程启动，可是 systemd 只管理用户态进程。那么，内核态线程又是谁来管理的呢？
实际上，Linux 在启动过程中，有三个特殊的进程，也就是 PID 号最小的三个进程。
- 0 号进程为 idle 进程，这也是系统创建的第一个进程，它在初始化 1 号和 2 号进程后，演变为空闲任务。当 CPU 上没有其他任务执行时，就会运行它。
- 1 号进程为 init 进程，通常是 systemd 进程，在用户态运行，用来管理其他用户态进程。
- 2 号进程为 kthreadd 进程，在内核态运行，用来管理内核线程。

所以，要查找内核线程，只需要从 2 号进程开始，查找它的子孙进程即可。比如，可以使用 ps 命令，来查找 kthreadd 的子进程：
```bash
$ ps -f --ppid 2 -p 2
UID         PID   PPID  C STIME TTY          TIME CMD
root          2      0  0 12:02 ?        00:00:01 [kthreadd]
root          9      2  0 12:02 ?        00:00:21 [ksoftirqd/0]
root         10      2  0 12:02 ?        00:11:47 [rcu_sched]
root         11      2  0 12:02 ?        00:00:18 [migration/0]
...
root      11094      2  0 14:20 ?        00:00:00 [kworker/1:0-eve]
root      11647      2  0 14:27 ?        00:00:00 [kworker/0:2-cgr]
# 内核线程的名称（CMD）都在中括号里
$ ps -ef | grep "\[.*\]"
root         2     0  0 08:14 ?        00:00:00 [kthreadd]
root         3     2  0 08:14 ?        00:00:00 [rcu_gp]
root         4     2  0 08:14 ?        00:00:00 [rcu_par_gp]
...
```
容易发生性能问题的内核线程：
- ksoftirqd：它是一个用来处理软中断的内核线程，并且每个 CPU 上都有一个
- kswapd0：用于内存回收。
- kworker：用于执行内核工作队列，分为绑定 CPU （名称格式为 kworker/CPU86330）和未绑定 CPU（名称格式为 kworker/uPOOL86330）两类。
- migration：在负载均衡过程中，把进程迁移到 CPU 上。每个 CPU 都有一个 migration 内核线程。
- jbd2/sda1-8：jbd 是 Journaling Block Device 的缩写，用来为文件系统提供日志功能，以保证数据的完整性；名称中的 sda1-8，表示磁盘分区名称和设备号。每个使用了 ext4 文件系统的磁盘分区，都会有一个 jbd2 内核线程。
- pdflush：用于将内存中的脏页（被修改过，但还未写入磁盘的文件页）写入磁盘（已经在 3.10 中合并入了 kworker 中）。


[SystemTap](https://sourceware.org/systemtap/) 是 Linux 的一种动态追踪框架，它把用户提供的脚本，转换为内核模块来执行，用来监测和跟踪内核的行为

# 参考资料

- [Linux Kernel内核归档](https://www.kernel.org/)
- [A Little bit about Linux kernel](https://github.com/0xAX/linux-insides)
- [LWN’s kernel page](https://lwn.net/Kernel/Index/)
- [Kernel Planet-Linux 内核开发者的 Blog](https://planet.kernel.org/)
- [Linux源代码](https://mirrors.edge.kernel.org/pub/linux/kernel/)
- [Linux性能介绍](https://unixism.net/2019/04/linux-applications-performance-introduction/)
- [Linux源码在线查看网站](https://elixir.bootlin.com/linux/v6.12.1/source)
- [Linux内核学习资料](https://github.com/0voice/linux_kernel_wiki)
- [Linux源码在线查看网站](https://elixir.bootlin.com/linux/latest/source)
- [eBPF-什么是eBPF](https://ebpf.io/zh-hans/what-is-ebpf/)
- [eBPF](https://ebpf.io/)
