http://blog.csdn.net/maosijunzi/article/details/46049117
以下使用的 JDK 版本为 1.8
1.类加载统计:
	C:\Users\BlueFish>jstat -class 2120
	Loaded  Bytes  Unloaded  Bytes     Time
	  3043  2819.6        0     0.0      12.47 
{}
	Loaded:加载class的数量
	Bytes：所占用空间大小
	Unloaded：未加载数量
	Bytes:未加载占用空间
	Time：时间

2.编译统计
C:\Users\BlueFish>jstat -compiler 2120
Compiled Failed Invalid   Time   FailedType FailedMethod
     536      0       0     0.45          0
     Compiled：编译数量。
     Failed：失败数量
     Invalid：不可用数量
     Time：时间
     FailedType：失败类型
     FailedMethod：失败的方法

3.jstat -gc vmid:执行时结果代表意思:
C:\Users\BlueFish>jstat -gc 2120
 S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT
512.0  512.0   0.0   512.0   4480.0   1758.0   10944.0     5708.8   9728.0 9404.9  0.0    0.0       15    0.065   1      0.031    0.095
	S0C		新生代中Survivor space中S0当前容量的大小（KB）
	S1C		新生代中Survivor space中S1当前容量的大小（KB）
	S0U		新生代中Survivor space中S0容量使用的大小（KB）
	S1U		新生代中Survivor space中S1容量使用的大小（KB）
	EC		Eden space当前容量的大小（KB）
	EU		Eden space容量使用的大小（KB）
	OC		Old space当前容量的大小（KB）
	OU		Old space使用容量的大小（KB）
	MC		方法区大小
	MU		方法区使用大小
	CCSC	压缩类空间大小
	CCSU	压缩类空间使用大小
	PC		Permanent space当前容量的大小（KB）
	PU		Permanent space使用容量的大小（KB）
	YGC		从应用程序启动到采样时发生 Young GC 的次数
	YGCT	从应用程序启动到采样时 Young GC 所用的时间(秒)
	FGC		从应用程序启动到采样时发生 Full GC 的次数
	FGCT	从应用程序启动到采样时 Full GC 所用的时间(秒)
	GCT		从应用程序启动到采样时用于垃圾回收的总时间(单位秒)，它的值等于YGC+FGC
4.堆内存统计:
C:\Users\BlueFish>jstat -gccapacity 2120
 NGCMN    NGCMX     NGC     S0C   S1C       EC      OGCMN      OGCMX       OGC         OC       MCMN     MCMX      MC     CCSMN    CCSMX     CCSC    YGC    FGC
  5440.0 262144.0   5504.0  512.0  512.0   4480.0    10944.0   524288.0    10944.0    10944.0      0.0  10624.0   9728.0      0.0      0.0      0.0     15     1
  	NGCMN	新生代最小容量
  	NGCMX	新生代最大容量
  	NGC    	当前新生代容量
  	S0C		第一个Survivor space的大小
  	S1C		第二个Survivor space的大小
  	EC：	Eden space 伊甸园区的大小
  	OGCMN   老年代最小容量
  	OGCMX   老年代最大容量
  	OGC     当前老年代大小
  	OC      当前老年代大小
  	MCMN    最小元数据容量
  	MCMX    最大元数据容量
  	MC     	当前元数据空间大小
  	CCSMN	最小压缩类空间大小
  	CCSMX	最大压缩类空间大小
  	CCSC	当前压缩类空间大小
  	YGCT	年轻代gc次数
  	FGCT	老年代GC次数
5.


