
 ***以下使用的 JDK 版本为 1.8***

# 一、jstat

## 1、类加载统计
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

## 2、编译统计

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

## 3、垃圾回收统计：jstat -gc vmid
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
- MC：metaspace总量大小
- MU：metaspace使用大小
- CCSC：压缩类空间大小
- CCSU：压缩类空间使用大小
- PC：Permanent space当前容量的大小（KB），在JDK8已已经没有了
- PU：Permanent space使用容量的大小（KB），在JDK8已已经没有了
- YGC：从应用程序启动到采样时发生 Young GC 的次数
- YGCT：从应用程序启动到采样时 Young GC 所用的时间(秒)
- FGC：从应用程序启动到采样时发生 Full GC 的次数
- FGCT：从应用程序启动到采样时 Full GC 所用的时间(秒)
- GCT：从应用程序启动到采样时用于垃圾回收的总时间(单位秒)，它的值等于YGC+FGC


## 4、堆内存统计
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

## 5、新生代回收统计
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

## 6、新生代内存统计
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

## 7、老年代垃圾回收统计
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

## 8、老年代内存统计
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

## 9、元数据空间统计
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

## 10、总结垃圾回收统计
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

## 11、JVM编译方法统计
```
C:\Users\Administrator>jstat -printcompilation 7172
Compiled  Size  Type Method
    4608     16    1 org/eclipse/emf/common/util/SegmentSequence$SegmentSequencePool$SegmentsAccessUnit reset
```
- Compiled：最近编译方法的数量
- Size：最近编译方法的字节码数量
- Type：最近编译方法的编译类型。
- Method：方法名标识

# 二、jstack

## 1、使用jstack分析cpu消耗过高的问题

当linux出现cpu被java程序消耗过高时，可以使用如下步骤来进行问题排查

- （1）top查找出哪个进程消耗的cpu高

- （2）top中shift+h查找出哪个线程消耗的cpu高 

    先输入top -p [pid]，比如21125进程，然后再按shift+h。这里意思为只查看21125的进程，并且显示线程

- （3）jstack查找这个线程的信息 

    jstack [进程]|grep -A 10

    ``` 
    "http-8081-11" daemon prio=10 tid=0x00002aab049a1800 nid=0x52f1 in Object.wait() [0x0000000042c75000] 
   java.lang.Thread.State: WAITING (on object monitor)  
     at java.lang.Object.wait(Native Method)  
     at java.lang.Object.wait(Object.java:485)  
     at org.apache.tomcat.util.net.JIoEndpoint$Worker.await(JIoEndpoint.java:416)  
    ```

*注：*上述获取相应过程可以使用[vjtools](https://github.com/vipshop/vjtools)中的vjtop来实现 来实现

# 三、jmap

## 1、实例个数以及占用内存大小
```
C:\Users\Administrator>jmap -histo 4284  > d:/log.txt
```
打开log.txt文件
```
num     #instances         #bytes  class name
----------------------------------------------
   1:       1496092      127664200  [C
   2:        157665       46778984  [I
   3:        100289       25426744  [B
   4:        736941       17686584  java.util.HashMap$Node
   5:         74396       11077256  [Ljava.util.HashMap$Node;
   6:        192701       10228688  [J
   7:        564943        9039088  java.lang.String
   8:         83340        8667360  org.eclipse.jdt.internal.compiler.ast.MethodDeclaration
   9:        125450        7025200  org.eclipse.jdt.internal.compiler.ast.SingleNameReference
  10:        125867        6903952  [Ljava.lang.Object;
  11:         67093        6440928  org.eclipse.jdt.internal.compiler.ast.MessageSend
  12:        188979        6183520  [[C
  13:        116393        5586864  org.eclipse.jdt.internal.compiler.ast.QualifiedTypeReference
  14:         77426        5574672  org.eclipse.emf.ecore.util.EContentsEList$FeatureIteratorImpl
  15:         49078        5104112  org.eclipse.jdt.internal.compiler.lookup.MethodScope
  16:         90784        4357632  org.eclipse.jdt.internal.compiler.ast.SingleTypeReference
  17:         85628        3275744  [Ljava.lang.String;
  18:         49565        3172160  org.eclipse.jdt.internal.compiler.lookup.MethodBinding
  .......
```
- num：序号
- instances：实例数量
- bytes：占用空间大小
- class name：类名称

## 2、堆信息

```
C:\Users\Administrator>jmap -heap 4284
Attaching to process ID 4284, please wait...
Debugger attached successfully.
Client compiler detected.
JVM version is 25.0-b70

using parallel threads in the new generation.
using thread-local object allocation.
Concurrent Mark-Sweep GC

Heap Configuration:
   MinHeapFreeRatio         = 40
   MaxHeapFreeRatio         = 70
   MaxHeapSize              = 1073741824 (1024.0
   NewSize                  = 419430400 (400.0MB
   MaxNewSize               = 419430400 (400.0MB
   OldSize                  = 654311424 (624.0MB
   NewRatio                 = 2
   SurvivorRatio            = 8
   MetaspaceSize            = 104857600 (100.0MB
   CompressedClassSpaceSize = 52428800 (50.0MB)
   MaxMetaspaceSize         = 104857600 (100.0MB
   G1HeapRegionSize         = 0 (0.0MB)

Heap Usage:
New Generation (Eden + 1 Survivor Space):
   capacity = 377487360 (360.0MB)
   used     = 346767024 (330.7028045654297MB)
   free     = 30720336 (29.297195434570312MB)
   91.8618901570638% used
Eden Space:
   capacity = 335544320 (320.0MB)
   used     = 317925456 (303.1973419189453MB)
   free     = 17618864 (16.802658081054688MB)
   94.74916934967041% used
From Space:
   capacity = 41943040 (40.0MB)
   used     = 28841568 (27.505462646484375MB)
   free     = 13101472 (12.494537353515625MB)
   68.76365661621094% used
To Space:
   capacity = 41943040 (40.0MB)
   used     = 0 (0.0MB)
   free     = 41943040 (40.0MB)
   0.0% used
concurrent mark-sweep generation:
   capacity = 654311424 (624.0MB)
   used     = 190777600 (181.939697265625MB)
   free     = 463533824 (442.060302734375MB)
   29.157002766927082% used

32953 interned Strings occupying 2775672 bytes.
```

## 3、即将垃圾回收对象个数

```
C:\Users\Administrator>jmap -finalizerinfo 4284
Attaching to process ID 4284, please wait...
Debugger attached successfully.
Client compiler detected.
JVM version is 25.0-b70
Number of objects pending for finalization: 0
```

## 4、堆内存dump

```
C:\Users\Administrator>jmap -dump:format=b,file=D:/chu 4284
Dumping heap to D:\chu ...
Heap dump file created
```

## 5、可以通过jhat查看上述dump文件

```
C:\Users\Administrator>jhat -J-Xmx500m D:/chu
Reading from D:/haha...
Dump file created Thu May 28 09:46:36 CST 2015
Snapshot read, resolving...
Resolving 3915212 objects...
Chasing references, expect 783 dots.......................
..........................................................
..........................................................
..........................................................
..................
Eliminating duplicate references..........................
..........................................................
..........................................................
..........................................................
...............
Snapshot resolved.
Started HTTP server on port 7000
Server is ready.
```

# 四、jps

# 五、jinfo

# 六、jhat

# 七、jconsole

# 八、MAT

# 九、jprofiler

# 十、Btrace


**btrace注意事项**
- 默认只能本地运行，如果希望本地调试远程机器，需要修改源代码
- 生产环境下可以使用，但是被修改的字节码不会被还原，直到JVM重启之前，一旦需要在生产环境下使用，最好是在本地编译通过

# 十一、visualvm

##  添加jmx连接

###  监控tomcat
需要修改catalina.sh，加入如下配置：
```
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9004 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.net.preferIP4Stack=true -Djava.rmi.server.hostname=127.0.0.1"
```
- -Dcom.sun.management.jmxremote=true 
- -Dcom.sun.management.jmxremote.port=8099（配置远程 connection 的端口号的） 
- -Dcom.sun.management.jmxremote.ssl=false(指定了 JMX 是否启用 ssl) 
- -Dcom.sun.management.jmxremote.authenticate=false（ 指定了JMX 是否启用鉴权（需要用户名，密码鉴权）） 
- -Djava.rmi.server.hostname=192.168.0.1（配置 server 的 IP）

### java应用
主要是jar包启动的，在jar启动的时添加跟tomcat一样的参数


# 参考资料

* [jstat命令使用](http://blog.csdn.net/maosijunzi/article/details/46049117)
* [visualvm](https://visualvm.github.io/documentation.html)
* [线程堆栈分析](http://fastthread.io/)
