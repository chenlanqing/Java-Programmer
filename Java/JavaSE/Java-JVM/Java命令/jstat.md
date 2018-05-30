
* 以下使用的 JDK 版本为 1.8

# 1、类加载统计
```
C:\Users\BlueFish>jstat -class 2120
Loaded  Bytes  Unloaded  Bytes     Time
	3043  2819.6        0     0.0      12.47 
```
Loaded：加载class的数量 <br>
Bytes：所占用空间大小 <br>
Unloaded：未加载数量 <br>
Bytes：未加载占用空间 <br>
Time：时间

# 2、编译统计

```
C:\Users\BlueFish>jstat -compiler 2120
Compiled Failed Invalid   Time   FailedType FailedMethod
     536      0       0     0.45          0
```
Compiled：编译数量。<br>
Failed：失败数量<br>
Invalid：不可用数量<br>
Time：时间<br>
FailedType：失败类型<br>
FailedMethod：失败的方法<br>

# 3、垃圾回收统计：jstat -gc vmid
```
C:\Users\BlueFish>jstat -gc 2120
 S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT
512.0  512.0   0.0   512.0   4480.0   1758.0   10944.0     5708.8   9728.0 9404.9  0.0    0.0       15    0.065   1      0.031    0.095
```
- S0C：新生代中Survivor space中S0当前容量的大小（KB）
- S1C：新生代中Survivor space中S1当前容量的大小（KB）
- S0U：新生代中Survivor space中S0容量使用的大小（KB）
- S1U：新生代中Survivor space中S1容量使用的大小（KB）
- EC：Eden space当前容量的大小（KB）
- EU：Eden space容量使用的大小（KB）
- OC：Old space当前容量的大小（KB）
- OU：Old space使用容量的大小（KB）
- MC：方法区大小
- MU：方法区使用大小
- CCSC：压缩类空间大小
- CCSU：压缩类空间使用大小
- PC：Permanent space当前容量的大小（KB）
- PU：Permanent space使用容量的大小（KB）
- YGC：从应用程序启动到采样时发生 Young GC 的次数
- YGCT：从应用程序启动到采样时 Young GC 所用的时间(秒)
- FGC：从应用程序启动到采样时发生 Full GC 的次数
- FGCT：从应用程序启动到采样时 Full GC 所用的时间(秒)
- GCT：从应用程序启动到采样时用于垃圾回收的总时间(单位秒)，它的值等于YGC+FGC


# 4、堆内存统计
```
C:\Users\BlueFish>jstat -gccapacity 2120
 NGCMN    NGCMX     NGC     S0C   S1C       EC      OGCMN      OGCMX       OGC         OC       MCMN     MCMX      MC     CCSMN    CCSMX     CCSC    YGC    FGC
  5440.0 262144.0   5504.0  512.0  512.0   4480.0    10944.0   524288.0    10944.0    10944.0      0.0  10624.0   9728.0      0.0      0.0      0.0     15     1
```
- NGCMN：新生代最小容量
- NGCMX：新生代最大容量
- NGC：当前新生代容量
- S0C：第一个Survivor space的大小
- S1C：第二个Survivor space的大小
- EC：Eden space 伊甸园区的大小
- OGCMN：老年代最小容量
- OGCMX：老年代最大容量
- OGC：当前老年代大小
- OC ：当前老年代大小
- MCMN：最小元数据容量
- MCMX：最大元数据容量
- MC ：当前元数据空间大小
- CCSMN：最小压缩类空间大小
- CCSMX：最大压缩类空间大小
- CCSC：当前压缩类空间大小
- YGCT：年轻代gc次数
- FGCT：老年代GC次数

# 5、新生代回收统计
```
C:\Users\Administrator>jstat -gcnew 7172
 S0C    S1C    S0U    S1U   TT MTT  DSS       EC        EU     		YGC    YGCT
40960.0 40960.0 25443.1    0.0 15  15 20480.0 327680.0 222697.8     12    0.736
```
- S0C：第一个幸存区大小
- S1C：第二个幸存区的大小
- S0U：第一个幸存区的使用大小
- S1U：第二个幸存区的使用大小
- TT：对象在新生代存活的次数
- MTT：对象在新生代存活的最大次数
- DSS：期望的幸存区大小
- EC：伊甸园区的大小
- EU：伊甸园区的使用大小
- YGC：年轻代垃圾回收次数
- YGCT：年轻代垃圾回收消耗时间

# 6、新生代内存统计
```
C:\Users\Administrator>jstat -gcnewcapacity 7172
NGCMN      NGCMX       NGC      S0CMX     S0C     S1CMX     S1C       ECMX        EC      YGC   FGC
409600.0   409600.0   409600.0  40960.0  40960.0  40960.0  40960.0   327680.0   327680.0    12     0
```
- NGCMN：新生代最小容量
- NGCMX：新生代最大容量
- NGC：当前新生代容量
- S0CMX：最大幸存1区大小
- S0C：当前幸存1区大小
- S1CMX：最大幸存2区大小
- S1C：当前幸存2区大小
- ECMX：最大伊甸园区大小
- EC：当前伊甸园区大小
- YGC：年轻代垃圾回收次数
- FGC：老年代回收次数

# 7、老年代垃圾回收统计
```
C:\Users\Administrator>jstat -gcold 7172
   MC       MU      CCSC     CCSU       OC          OU       YGC    FGC    FGCT     GCT
 33152.0  31720.8      0.0      0.0    638976.0    184173.0     12     0    0.000    0.736
```
- MC：方法区大小
- MU：方法区使用大小
- CCSC:压缩类空间大小
- CCSU:压缩类空间使用大小
- OC：老年代大小
- OU：老年代使用大小
- YGC：年轻代垃圾回收次数
- FGC：老年代垃圾回收次数
- FGCT：老年代垃圾回收消耗时间
- GCT：垃圾回收消耗总时间

# 8、老年代内存统计
```
C:\Users\Administrator>jstat -gcoldcapacity 7172
OGCMN       OGCMX        OGC         OC       YGC   FGC    FGCT     GCT
638976.0    638976.0    638976.0    638976.0    12     0    0.000    0.736
```
- OGCMN：老年代最小容量
- OGCMX：老年代最大容量
- OGC：当前老年代大小
- OC：老年代大小
- YGC：年轻代垃圾回收次数
- FGC：老年代垃圾回收次数
- FGCT：老年代垃圾回收消耗时间
- GCT：垃圾回收消耗总时间

# 9、元数据空间统计
```
C:\Users\Administrator>jstat -gcmetacapacity 7172
MCMN       MCMX        MC       CCSMN      CCSMX       CCSC     YGC   FGC    FGCT     GCT
0.0    33152.0    33152.0        0.0        0.0        0.0    12     0    0.000    0.736
```
- MCMN:最小元数据容量
- MCMX：最大元数据容量
- MC：当前元数据空间大小
- CCSMN：最小压缩类空间大小
- CCSMX：最大压缩类空间大小
- CCSC：当前压缩类空间大小
- YGC：年轻代垃圾回收次数
- FGC：老年代垃圾回收次数
- FGCT：老年代垃圾回收消耗时间
- GCT：垃圾回收消耗总时间

# 10、总结垃圾回收统计
```
C:\Users\Administrator>jstat -gcutil 7172
  S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT
 62.12   0.00  81.36  28.82  95.68      -     12    0.736     0    0.000    0.736
```
- S0：幸存1区当前使用比例
- S1：幸存2区当前使用比例
- E：伊甸园区使用比例
- O：老年代使用比例
- M：元数据区使用比例
- CCS：压缩使用比例
- YGC：年轻代垃圾回收次数
- FGC：老年代垃圾回收次数
- FGCT：老年代垃圾回收消耗时间
- GCT：垃圾回收消耗总时间

# 11、JVM编译方法统计
```
C:\Users\Administrator>jstat -printcompilation 7172
Compiled  Size  Type Method
    4608     16    1 org/eclipse/emf/common/util/SegmentSequence$SegmentSequencePool$SegmentsAccessUnit reset
```
- Compiled：最近编译方法的数量
- Size：最近编译方法的字节码数量
- Type：最近编译方法的编译类型。
- Method：方法名标识

# 参考资料

* [jstat命令使用](http://blog.csdn.net/maosijunzi/article/details/46049117)
