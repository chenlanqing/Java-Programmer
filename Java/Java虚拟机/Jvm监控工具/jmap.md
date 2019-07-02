
# 1、实例个数以及占用内存大小
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

# 2、堆信息
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

# 3、即将垃圾回收对象个数
```
C:\Users\Administrator>jmap -finalizerinfo 4284
Attaching to process ID 4284, please wait...
Debugger attached successfully.
Client compiler detected.
JVM version is 25.0-b70
Number of objects pending for finalization: 0
```

# 4、堆内存dump
```
C:\Users\Administrator>jmap -dump:format=b,file=D:/chu 4284
Dumping heap to D:\chu ...
Heap dump file created
```

# 5、可以通过jhat查看上述dump文件
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




