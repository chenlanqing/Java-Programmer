# 一、容器

## 1、Java程序运行在Docket等容器环境存在什么问题？

对于Java来说，Docker是一个新的环境，如其内存、CPU等资源限制都是通过CGroup（Control Group）实现的，早期的JDK版本并不能识别这些限制，进而导致一些基础问题：
- 如果未配置合适的JVM堆和元数据区、直接内存等参数，Java就有可能试图使用超过容器限制的内存，最终被容器OOM Kill，或者自身发生OOM；
- 错误判断了可获取的CPU资源，例如，Docker限制了CPU核数，JVM就可能设置不合适的GC并行线程数等；

## 2、为什么类似Docker这种容器环境，会有点欺负JAVA？从JVM内部机制来讲，问题出现在哪里？

### 2.1、出现问题的原因

- Docker与虚拟机非常相似，但是Docker并不是一种完全的虚拟机化技术，而更是一种轻量级的隔离技术；基于namespace，Docket为每个容器提供了单独的命名空间，对网络、PID、用户、IPC通信、文件系统挂载等实现了隔离，对于CPU、内存、磁盘IO等计算资源，则是用过CGroup进行管理。
- 容器虽然省略了虚拟操作系统的开销，实现了轻量级的目标，但也带来了额外的复杂性，它限制对于应用不是透明的，需要用户理解Docker的新行为；
- 对于Java平台来说，未隐藏的底层信息带来了意外，主要体现在：
    - 容器环境对于计算机资源的管理方式是全新的，CGroup作为相对比较新的技术，历史版本的Java显然并不能自然理解相应的资源限制；
    - namespace对于容器内的应用细节增量一些微妙差异

- 从JVM运行角度，这些为什么会导致OOM等问题？
    
    这就是所谓的Ergonomics机制
    - JVM会大概根据检测到的内存大小，设置最初启动时的堆大小为系统内存的1/64；并将堆最大值设置为系统内存的1/4；
    - 而JVM检测到系统的CPU核数，则直接影响到了ParallelGC并行线程数目和JIT compiler线程数目，设置是应用中ForkJoinPool等机制的并行登记；

    这些是默认参数，是根据通用场景选择的初始值，但是由于容器环境的差异，Java的判断很可能是基于错误信息而做出的，更加严重的是JVM的一些原有诊断或备用机制也会受到影响

### 2.2、如何解决
- 首先，如果能够升级到最新的JDK版本，问题就解决了；
- 如果只用老版本JDK，需要注意以下几点：
    - 明确设置堆大小、元数据等内存区域大小，保证Java进程的总大小可控；
    - 明确配置GC和JIT并行线程数目，以避免二者占用过多计算资源

