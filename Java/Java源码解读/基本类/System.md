<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [1.关于 System.nanoTime() 与 System.currentTimeMillis(),三个问题:](#1%E5%85%B3%E4%BA%8E-systemnanotime-%E4%B8%8E-systemcurrenttimemillis%E4%B8%89%E4%B8%AA%E9%97%AE%E9%A2%98)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

#### 1.关于 System.nanoTime() 与 System.currentTimeMillis(),三个问题:
	* https://yq.aliyun.com/articles/67089?spm=5176.8091938.0.0.eWP39h
	(1).在mac下发现System.nanoTime()在JDK7和JDK8下输出的值怎么完全不一样?
	(2).System.nanoTime()的值很奇怪,究竟是怎么算出来的?
	(3).System.currentTimeMillis()为何不是 System.nanoTime()的 1000000 倍?
	1.1.MAC 不同JDK版本下nanoTime实现异同:
		分析JDK7和JDK8的C语言实现,发现JDK8下多了一个 __APPLE__ 宏下定义的实现,和JDK7及之前的版本的实现是不一样的.
		不过其他BSD系统是一样的.只是 MacOC 有点不一样
	1.2.System.nanoTime()的值很奇怪,究竟是怎么算出来的?
		nanoTime其实算出来的是一个相对的时间,相对于系统启动的时候的时间(在linux下JDK7和JDK8的实现都是一样的)
	1.3.System.currentTimeMillis()为何不是 System.nanoTime()的 1000000 倍:
		currentTimeMillis其实是通过gettimeofday来实现的.
		System.currentTimeMillis()就是返回的当前时间距离 1970/01/01 08:00:00 的毫秒数
	P.S:为什么计算时间从 "1970年1月1日" 开始?
		(1).Java 起源于 UNIX 系统,而 UNIX 认为 1970 年 1 月 1 日 0 点是时间纪元;
		(2).最初计算机操作系统是32 位,而时间也是用 32 位表示
			System.out.println(Integer.MAX_VALUE);
			2147483647
			因为用32 位来表示时间的最大间隔是 68 年,而最早出现的 UNIX 操作系统考虑到计算
			机产生的年代和应用的时限综合取了 1970 年 1 月 1 日作为 UNIX TIME 的纪元时间.
			Integer在 JAVA 内用 32 位表 示，因此 32 位能表示的最大值是 2147483647.
			另外1 年 365 天的总秒数是 31536000,
			2147483647/31536000 = 68.1
			也就是说32 位能表示的最长时间是 68 年,而实际上到 2038年 01 月 19 日 03 时 14 分 07 秒,便会到达最大时间
		(3).至于时间回归的现象相信随着64 为操作系统 的产生逐渐得到解决,因为用 64 位操作系统可以表示到"292,277,026,596年12月4日15时30分08秒"
		(4).System.out.println(new Date(0)),打印出来的时间是8点而非0点
			存在系统时间和本地时间的问题,其实系统时间依然是0点,不过跟你所在时区有关.

