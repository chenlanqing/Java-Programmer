<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [1、关于System.nanoTime() 与 System.currentTimeMillis()](#1%E5%85%B3%E4%BA%8Esystemnanotime-%E4%B8%8E-systemcurrenttimemillis)
  - [1.1、MAC 不同JDK版本下nanoTime实现异同](#11mac-%E4%B8%8D%E5%90%8Cjdk%E7%89%88%E6%9C%AC%E4%B8%8Bnanotime%E5%AE%9E%E7%8E%B0%E5%BC%82%E5%90%8C)
  - [1.2、System.nanoTime()的值很奇怪，究竟是怎么算出来的？](#12systemnanotime%E7%9A%84%E5%80%BC%E5%BE%88%E5%A5%87%E6%80%AA%E7%A9%B6%E7%AB%9F%E6%98%AF%E6%80%8E%E4%B9%88%E7%AE%97%E5%87%BA%E6%9D%A5%E7%9A%84)
  - [1.3、System.currentTimeMillis()为何不是 System.nanoTime()的 1000000 倍：](#13systemcurrenttimemillis%E4%B8%BA%E4%BD%95%E4%B8%8D%E6%98%AF-systemnanotime%E7%9A%84-1000000-%E5%80%8D)
  - [1.4、为什么计算时间从 “1970年1月1日” 开始？](#14%E4%B8%BA%E4%BB%80%E4%B9%88%E8%AE%A1%E7%AE%97%E6%97%B6%E9%97%B4%E4%BB%8E-1970%E5%B9%B41%E6%9C%881%E6%97%A5-%E5%BC%80%E5%A7%8B)
- [2、System.gc方法](#2systemgc%E6%96%B9%E6%B3%95)
  - [2.1、方法实现](#21%E6%96%B9%E6%B3%95%E5%AE%9E%E7%8E%B0)
  - [2.2、作用](#22%E4%BD%9C%E7%94%A8)
- [参考文章](#%E5%8F%82%E8%80%83%E6%96%87%E7%AB%A0)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 1、关于System.nanoTime() 与 System.currentTimeMillis()

三个问题：
	
- 在mac下发现System.nanoTime()在JDK7和JDK8下输出的值怎么完全不一样？
- System.nanoTime()的值很奇怪，究竟是怎么算出来的？
- System.currentTimeMillis()为何不是 System.nanoTime()的 1000000 倍？
	
## 1.1、MAC 不同JDK版本下nanoTime实现异同

分析JDK7和JDK8的C语言实现，发现JDK8下多了一个 __APPLE__ 宏下定义的实现，和JDK7及之前的版本的实现是不一样的。不过其他BSD系统是一样的，只是 MacOC 有点不一样

## 1.2、System.nanoTime()的值很奇怪，究竟是怎么算出来的？

nanoTime其实算出来的是一个相对的时间，相对于系统启动的时候的时间(在linux下JDK7和JDK8的实现都是一样的)

## 1.3、System.currentTimeMillis()为何不是 System.nanoTime()的 1000000 倍：

currentTimeMillis其实是通过gettimeofday来实现的。System.currentTimeMillis()就是返回的当前时间距离 1970/01/01 08：00：00 的毫秒数

## 1.4、为什么计算时间从 “1970年1月1日” 开始？

- Java 起源于 UNIX 系统，而 UNIX 认为 1970 年 1 月 1 日 0 点是时间纪元；
- 最初计算机操作系统是32 位，而时间也是用 32 位表示，System.out.println(Integer.MAX_VALUE); 2147483647

	因为用32 位来表示时间的最大间隔是68年，而最早出现的UNIX操作系统考虑到计算机产生的年代和应用的时限综合取了1970年1月1日作为UNIX TIME的纪元时间，Integer在JAVA内用32位表示，因此32位能表示的最大值是2147483647，另外1年 365天的总秒数是31536000，2147483647/31536000 = 68.1，也就是说32 位能表示的最长时间是68年，而实际上到2038年01月19日03时14分07秒，便会到达最大时间；这就是 [2038](https://en.wikipedia.org/wiki/Year_2038_problem) 问题
- 至于时间回归的现象相信随着64 为操作系统 的产生逐渐得到解决，因为用64位操作系统可以表示到“292，277，026，596年12月4日15时30分08秒”；
- System.out.println(new Date(0))，打印出来的时间是8点而非0点存在系统时间和本地时间的问题，其实系统时间依然是0点，不过跟你所在时区有关。

# 2、System.gc方法

## 2.1、方法实现

```java
public static void gc() {
	Runtime.getRuntime().gc();
}
/*
运行垃圾收集器。
调用此方法表明，java虚拟机扩展。努力回收未使用的对象，以便内存可以快速复用，当控制从方法调用返回的时候，虚拟机尽力回收被丢弃的对象
*/
public native void gc();
```
## 2.2、作用

- 做一次full gc
- 执行后会暂停整个进程。
- System.gc我们可以禁掉，使用-XX:+DisableExplicitGC，其实一般在cms gc下我们通过-XX:+ExplicitGCInvokesConcurrent也可以做稍微高效一点的gc，也就是并行gc。
- 最常见的场景是RMI/NIO下的堆外内存分配等；

# 参考文章

* [System.nanoTime的实现原理](https：//yq.aliyun.com/articles/67089？spm=5176.8091938.0.0.eWP39h)