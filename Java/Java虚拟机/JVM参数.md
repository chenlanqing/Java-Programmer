查看虚拟机的参数：
```
root@xxx-mac：~|=> java -XX:+PrintFlagsFinal -XX:+UnlockDiagnosticVMOptions -version | wc -l
java version "1.8.0_151"
Java(TM) SE Runtime Environment (build 1.8.0_151-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.151-b12, mixed mode)
     828
```
JVM参数可以分为如下：
- 标准参数
- 非标准参数
- 运行时参数
- JIT编译器参数
- 高级服务能力参数
- 垃圾回收参数

```
java [options] classname [args]
java [options] -jar filename [args]
```
选项说明 ：  
- `-XX:+option` 启用选项 
- `-XX:-option` 不启用选项 
- `-XX:option=number` 给选项设置一个数字类型值，可跟单位，例如 128k, 256m, 1g 
- `-XX:option=string` 给选项设置一个字符串值，例如`-XX:HeapDumpPath=./dump.core`

所有参数文件都可以再OpenJDK中 [globals.hpp](https://github.com/chenlanqing/openjdk/blob/jdk/jdk/src/hotspot/share/runtime/globals.hpp)文件中找到

*任何一个JVM参数的默认值可以通过 `java -XX:+PrintFlagsFinal -version |grep JVMParamName` 获取，例如：`java -XX:+PrintFlagsFinal -version |grep MetaspaceSize`*

**查看默认参数：**`java -XX:+PrintCommandLineFlags -version`：查看初始默认参数

根据`JVM`参数开头可以区分参数类型，共三类：“`-`”、“`-X`”、“`-XX`”，

- 标准参数（-）：所有的JVM实现都必须实现这些参数的功能，而且向后兼容，例子：`-verbose:class`，`-verbose:gc`，`-verbose:jni……`

- 非标准参数（-X）：默认jvm实现这些参数的功能，但是并不保证所有jvm实现都满足，且不保证向后兼容，例子：`Xms20m`，`-Xmx20m`，`-Xmn20m`，`-Xss128k……`

- 非Stable参数（-XX）：此类参数各个jvm实现会有所不同，将来可能会随时取消，需要慎重使用，例子：`-XX:+PrintGCDetails`，`-XX:-UseParallelGC`，`-XX:+PrintGCTimeStamps……`

## 1、内存各个区域参数

### 1.1、堆参数设置

`-Xms` 初始堆大小，ms是memory start的简称 ，等价于`-XX:InitialHeapSize`；`-Xmx`最大堆大小，mx是memory max的简称 ，等价于参数`-XX:MaxHeapSize`

> 注意：在通常情况下，服务器项目在运行过程中，堆空间会不断的收缩与扩张，势必会造成不必要的系统压力；所以在生产环境中，JVM的Xms和Xmx要设置成大小一样的，能够避免GC在调整堆大小带来的不必要的压力；

`-XX:NewSize=n` 设置年轻代大小`-XX:NewRatio=n` 设置年轻代和年老代的比值；

如:`-XX:NewRatio=3`，表示年轻代与年老代比值为`1：3`，年轻代占整个年轻代年老代和的1/4，`默认新生代和老年代的比例=1:2`。`-XX:SurvivorRatio=n` 年轻代中Eden区与两个Survivor区的比值。

注意Survivor区有两个，默认是8，表示：`Eden:S0:S1=8:1:1`

如：`-XX:SurvivorRatio=3`，表示`Eden：Survivor`=3：2，一个Survivor区占整个年轻代的1/5

### 1.2、元空间参数

**-XX:MetaspaceSize**：`Metaspace` 空间初始大小，如果不设置的话，默认是20.79M，这个初始大小是触发首次 `Metaspace Full GC`的阈值。

例如：`-XX:MetaspaceSize=256M`

**-XX:MaxMetaspaceSize**：`Metaspace` 最大值，默认不限制大小，但是线上环境建议设置。

例如：`-XX:MaxMetaspaceSize=256M`

**-XX:MinMetaspaceFreeRatio**：最小空闲比，当 `Metaspace` 发生 GC 后，会计算 `Metaspace` 的空闲比，如果空闲比(空闲空间/当前 `Metaspace` 大小)小于此值，就会触发 `Metaspace` 扩容。默认值是 40，也就是 40%，例如 `-XX:MinMetaspaceFreeRatio=40`

**-XX:MaxMetaspaceFreeRatio**：最大空闲比，当 `Metaspace`发生 GC 后，会计算 `Metaspace` 的空闲比，如果空闲比(空闲空间/当前 Metaspace 大小)大于此值，就会触发 `Metaspace` 释放空间。默认值是 70 ，也就是 70%，例如 -`XX:MaxMetaspaceFreeRatio=70`

> 建议将 `MetaspaceSize` 和 `MaxMetaspaceSize`设置为同样大小，避免频繁扩容；

### 1.3、栈参数设置

**-Xss**：栈空间大小，栈是线程独占的，所以是一个线程使用栈空间的大小。

例如：`-Xss256K`，如果不设置此参数，默认值是`1M`，一般来讲设置成 `256K` 就足够了

### 1.4、收集器参数设置

Serial垃圾收集器（新生代）

> 开启：-XX:+UseSerialGC 关闭：-XX:-UseSerialGC //新生代使用Serial  老年代则使用SerialOld

ParNew垃圾收集器（新生代）

> 开启 -XX:+UseParNewGC 关闭 -XX:-UseParNewGC //新生代使用功能ParNew 老年代则使用功能CMS

Parallel Scavenge收集器（新生代）

> 开启 -XX:+UseParallelOldGC 关闭 -XX:-UseParallelOldGC //新生代使用功能Parallel Scavenge 老年代将会使用Parallel Old收集器

ParallelOl垃圾收集器（老年代）

> 开启 -XX:+UseParallelGC 关闭 -XX:-UseParallelGC //新生代使用功能Parallel Scavenge 老年代将会使用Parallel Old收集器

CMS垃圾收集器（老年代）

> 开启 -XX:+UseConcMarkSweepGC 关闭 -XX:-UseConcMarkSweepGC

G1垃圾收集器

> 开启 -XX:+UseG1GC 关闭 -XX:-UseG1GC

### 1.5、GC策略参数

GC停顿时间，垃圾收集器会尝试用各种手段达到这个时间，比如减小年轻代

> -XX:MaxGCPauseMillis

堆占用了多少比例的时候触发GC，就即触发标记周期的 Java 堆占用率阈值。默认占用率是整个 Java 堆的 45%

> -XX:InitiatingHeapOccupancyPercent=n

新生代可容纳的最大对象,大于则直接会分配到老年代，0代表没有限制。

> -XX:PretenureSizeThreshold=1000000 //

进入老年代最小的GC年龄,年轻代对象转换为老年代对象最小年龄值，默认值7

> -XX:InitialTenuringThreshol=7

升级老年代年龄，最大值15

> -XX:MaxTenuringThreshold

GC并行执行线程数

> -XX:ParallelGCThreads=16

禁用 System.gc()，由于该方法默认会触发 FGC，并且忽略参数中的 UseG1GC 和 UseConcMarkSweepGC，因此必要时可以禁用该方法。

> -XX:-+DisableExplicitGC

设置吞吐量大小,默认99

> XX:GCTimeRatio

打开自适应策略,各个区域的比率，晋升老年代的年龄等参数会被自动调整。以达到吞吐量，停顿时间的平衡点。

> XX:UseAdaptiveSizePolicy

设置GC时间占用程序运行时间的百分比

> GCTimeRatio

### 1.6、Dump异常快照

```
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath
```

堆内存出现`OOM`的概率是所有内存耗尽异常中最高的，出错时的堆内信息对解决问题非常有帮助。

所以给`JVM`设置这个参数(`-XX:+HeapDumpOnOutOfMemoryError`)，让`JVM`遇到`OOM`异常时能输出堆内信息，并通过（`-XX:+HeapDumpPath`）参数设置堆内存溢出快照输出的文件地址。

这对于特别是对相隔数月才出现的`OOM`异常尤为重要。

```
-Xms10M -Xmx10M -Xmn2M -XX:SurvivorRatio=8 -XX:+HeapDumpOnOutOfMemoryError 
-XX:HeapDumpPath=D:\study\log_hprof\gc.hprof
-XX:OnOutOfMemoryError
```

表示发生`OOM后`，运行`jconsole.exe`程序。

这里可以不用加“”，因为`jconsole.exe`路径Program Files含有空格。利用这个参数，我们可以在系统`OOM`后，自定义一个脚本，可以用来发送邮件告警信息，可以用来重启系统等等。

```
-XX:OnOutOfMemoryError="C:\Program Files\Java\jdk1.8.0_151\bin\jconsole.exe"
```

### 1.7、实践

8G内存的服务器该如何设置

```
java -Xmx3550m -Xms3550m -Xss128k -XX:NewRatio=4 -XX:SurvivorRatio=4 -XX:MaxPermSize=16m -XX:MaxTenuringThreshold=0
```

`-Xmx3500m` 设置`JVM`最大可用内存为3550M。

`-Xms3500m` 设置`JVM`初始`内存为`3550m`。此值可以设置与`-Xmx`相同，以避免每次垃圾回收完成后JVM重新分配内存。`-Xmn2g` 设置年轻代大小为`2G`。

> 整个堆大小=年轻代大小 + 年老代大小 + 方法区大小

`-Xss128k` 设置每个线程的堆栈大小。

`JDK1.5`以后每个线程堆栈大小为1M，以前每个线程堆栈大小为256K。更具应用的线程所需内存大小进行调整。在相同物理内存下，减小这个值能生成更多的线程。但是操作系统对一个进程内的线程数还是有限制的，不能无限生成，经验值在3000~5000左右。

`-XX:NewRatio=4` 设置年轻代（包括Eden和两个Survivor区）与年老代的比值（除去持久代）。设置为4，则年轻代与年老代所占比值为1：4，年轻代占整个堆栈的1/5 。

`-XX:SurvivorRatio=4` 设置年轻代中Eden区与Survivor区的大小比值。

设置为4，则两个Survivor区与一个Eden区的比值为2:4，一个Survivor区占整个年轻代的1/6 `-XX:MaxPermSize=16m` 设置持久代大小为16m。

`-XX:MaxTenuringThreshold=0` 设置垃圾最大年龄。

如果设置为0的话，则年轻代对象不经过Survivor区，直接进入年老代。对于年老代比较多的应用，可以提高效率。如果将此值设置为一个较大值，则年轻代对象会在Survivor区进行多次复制，这样可以增加对象在年轻代的存活时间，增加在年轻代即被回收的概论。





## 2、标准参数

- [JDK8-标准参数](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/java.html#BABDJJFI)
- [JDK11-标准参数](https://docs.oracle.com/en/java/javase/11/tools/java.html#GUID-3B1CE181-CD30-4178-9602-230B800D4FAE)

标准参数是被所有JVM实现都要支持的参数。用于做一些常规的通用的动作，比如检查版本、设置classpath等，查看支持的参数：`java -help`

- `-agentlib:libname[=options]`：这个命令加载指定的native agent库。理论上这条option出现后，JVM会到本地固定路径下LD_LIBRARY_PATH这里加载名字为libxxx.so的库

- `-agentpath:pathname[=options]`：这个参数指定了从哪个绝对路径加载agent库。与`-agentlib`相同，只不过是用绝对路径来指明库文件

- `-client、-server`：指定JVM的启动模式是client模式还是server模式，具体就是 Java HotSpot Client(Server) VM 版本。目前64位的JDK启动，一定是server模式，会忽略这个参数

- `-Dproperty=value`： 设置系统属性，设置后的属性可以在代码中`System.getProperty`方法获取到

- `disableassertions[:[packagename]...|:classname] 和 -da[:[packagename]...|:classname] `<br>
    关闭指定包或类下的assertion，默认是关闭的

- `-disablesystemassertions、-dsa`：关闭系统包类下的assertion

- `-verbose:class、-verbose:gc、-verbose:jni` <br>
    这些组合都是用来展示信息的，class展示的每个class的信息，gc展示每个GC事件的信息，jni开启展示JNI调用信息

- `-javaagent:jarpath[=options]`：加载指定的java agent，具体需要传入jar路径

- `–class-path classpath, -classpath classpath, or -cp classpath` ： 通知JVM类搜索路径。如果指定了-classpath，则JVM就忽略CLASSPATH中指定的路径。各路径之间以分号隔开。如果-classpath和CLASSPATH都没有指定，则JVM从当前路径寻找class;

- `-verbose:class`：显示类加载相关的信息，当报找不到类或者类冲突时可用此参数诊断

- `-verbose:gc`：显示垃圾收集事件的相关信息

- `-verbose:jni`：显示本机方法和其他Java本机接口（JNI）的相关信息

## 3、非标准参数

- [JDK8-非标准参数](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/java.html#BABHDABI)
- [JDK11-额外参数](https://docs.oracle.com/en/java/javase/11/tools/java.html#GUID-3B1CE181-CD30-4178-9602-230B800D4FAE)

针对官方JVM也就是HotSpot的，不同的虚拟机有各自的非标准参数，在JDK11中叫做额外参数

- `-X` <br>
    展示所有-X开头的参数说明

- `-Xcomp` ：在第一次调用时强制编译方法。默认情况下，client模式下会解释执行1000次（JDK 11下），server模式下会解释执行10000次，并收集信息，此后才可能编译运行。指定该选项将禁用解释方法调用。此外，还可使用 `-XX:CompileThreshold` 选项更改在编译之前解释执行方法的调用次数；

- `-Xbatch` <br>
    禁止后台编译。将编译过程放到前台任务执行。JVM默认会将编译任务当做后台任务执行。这个参数等价于`-XX:-BackgroundCompilation`

- `-Xfuture` <br>
    强制class文件格式检查

- `-Xint` <br>
    在 interpreted-only模式运行程序。编译为native的模式不再生效，所有的字节码都在解释器环境下解释执行

- `-Xinternalversion` <br>
    打印一个更详细的java版本信息，执行后退出

- `-Xloggc:filename` <br>
    设置gc日志文件，gc相关信息会重定向到该文件。这个配置如果和-verbose:gc同时出现，会覆盖-verbose:gc参数，比如`-Xloggc:/home/admin/logs/gc.log`

- `-Xmaxjitcodesize=size` <br>
    为JIT编译的代码设置最大的code cache。默认的设置是240m，如果关闭了tiered compilation，那么默认大小是48m。这个参数和`-XX:ReservedCodeCacheSize`是等价的

- `-Xmixed` <br>
    用解释器执行所有的字节码，除了被编译为native code的hot method

- `-Xmssize` <br>
    设置初始的堆大小，如果没有设置这个值，那么它的初始化大小就是年轻代和老年代的和，等价于`-XX:InitialHeapSize`；这个值的大小必须是1024的倍数，并且大于1M

- `-Xmxsize` <br>
    设置最大的内存分配大小，一般的服务端部署，-Xms和-Xmx设置为同样大小。与`-XX:MaxHeapSize`具有同样的作用，避免在GC后调整堆大小带来的压力

- `-Xmnsize` <br>
    设置年轻代大小，还可以通过其他两个选项来代替这个选项来指定年轻代最小和最大内存：`-XX:NewSize`指定初始化大小,`-XX:MaxNewSize`指定最大内存大小

- `-Xnoclassgc` <br>
    关闭对class的GC。这样设置可以节约一点GC的时间，不过带来的影响就是class永驻内存，不当的使用会导致OOM风险

- `-XshowSettings:category` <br>
    查看settings信息，category可以是all、locale、properties和vm几部分

- `-Xsssize` <br>
    设置thread stack大小，一般默认的几个系统参数如下，这个选项和`-XX:ThreadStackSize`相同，默认值取决于平台：
    - Linux / x64（64位）：1024 KB
    - macOS（64位）：1024 KB
    - Oracle Solaris / x64（64位）：1024 KB
    - Windows：默认值取决于虚拟内存

- `-Xverify:mode` <br>
    设置字节码校验器的模式，默认是remote，即只校验那些不是通过bootstrap类加载器加载的字节码。而还有一个模式还all，即全部都校验。虽然还有一个模式是none，但是本质上jvm不生效这个参数

## 4、高级选项

- 查看支持的参数：

**使用如下命令：**

`java -XX:+UnlockExperimentalVMOptions -XX:+UnlockDiagnosticVMOptions -XX:+PrintFlagsInitial`

其中：
- UnlockExperimentalVMOptions：用于解锁实验性参数，如果不加该标记，不会打印实验性参数
- UnlockDiagnosticVMOptions：用于解锁诊断性参数，如果不加该标记，不会打印诊断性参数
- PrintFlagsInitial：打印支持的XX选项，并展示默认值。如需获得程序运行时生效值，用PrintFlagsFinal；

**使用格式**
- 如果选项的类型是boolean：那么配置格式是 `-XX:(+/-)`选项 即可。+表示将选项设置为true，-表示设置为false，例如：-XX:+PrintGC
- 如果选项的类型不是boolean，那么配置格式是 `-XX:选项=值` ，例如：-XX:NewRatio=4

**JDK  11对高级选项的细分**
<li><a href="https://docs.oracle.com/en/java/javase/11/tools/java.html#GUID-3B1CE181-CD30-4178-9602-230B800D4FAE__BABCBGHF">Advanced Runtime Options for Java</a> ：高级运行时选项，控制HotSpot VM的运行时行为</li>
<li><a href="https://docs.oracle.com/en/java/javase/11/tools/java.html#GUID-3B1CE181-CD30-4178-9602-230B800D4FAE__BABDDFII">Advanced JIT Compiler Options for java</a> ：高级JIT编译器选项：控制HotSpot VM执行的JIT编译</li>
<li><a href="https://docs.oracle.com/en/java/javase/11/tools/java.html#GUID-3B1CE181-CD30-4178-9602-230B800D4FAE__BABFJDIC">Advanced Serviceability Options for Java</a> ：高级可服务性选项：系统信息收集与调试支持</li>
<li><a href="https://docs.oracle.com/en/java/javase/11/tools/java.html#GUID-3B1CE181-CD30-4178-9602-230B800D4FAE__BABFAFAE">Advanced Garbage Collection Options for Java</a> ：高级垃圾收集选项：控制HotSpot如何执行垃圾收集</li>

**JDK 8对高级选项的细分**
<li><a href="https://docs.oracle.com/javase/8/docs/technotes/tools/windows/java.html#BABCBGHF">Advanced Runtime Options</a></li>
<li><a href="https://docs.oracle.com/javase/8/docs/technotes/tools/windows/java.html#BABDDFII">Advanced JIT Compiler Options</a></li>
<li><a href="https://docs.oracle.com/javase/8/docs/technotes/tools/windows/java.html#BABFJDIC">Advanced Serviceability Options</a></li>
<li><a href="https://docs.oracle.com/javase/8/docs/technotes/tools/windows/java.html#BABFAFAE">Advanced Garbage Collection Options</a></li>

### 4.1、运行时参数

这类参数控制Java HotSpot VM在运行时的行为

- `-XX:ActiveProcessorCount=x`：JVM使用多少个CPU核心去计算用于执行垃圾收集或ForkJoinPool线程池的大小

- `-XX:+DisableAttachMechanism` <br>
    设置JVM的attach模式，如果启动了这个参数，jvm将不允许attach，这样类似jmap等程序就无法使用了

- `-XX:ErrorFile=filename` <br>
    当不可恢复的错误发生时，错误信息记录到哪个文件。默认是在当前目录的一个叫做hs_err_pid pid.log的文件。如果指定的目录没有写权限，这时候文件会创建到/tmp目录下

- `-XX:MaxDirectMemorySize=size` <br>
    为NIO的direct-buffer分配时指定最大的内存大小。默认是0，意思是JVM自动选择direct-buffer的大小，以字节为单位也可在size后追加字母 k 或 K 表示千字节， m 或 M 表示兆字节，或 g 或 G 表示千兆字节

- `-XX:ObjectAlignmentInBytes=alignment` <br>
    java对象的内存对齐大小。默认是8字节，JVM实际计算堆内存上限的方法是

- `-XX:OnError=string` <br>
    在jvm出现错误(不可恢复）的时候，执行哪些命令，具体例子如下：`-XX:OnError="gcore %p;dbx - %p"`，多个命令使用逗号分割

- `-XX:OnOutOfMemoryError=string` <br>
    同`-XX:OnError=string`，具体错误是OOM

- `-XX:-UseCompressedClassPoniters` <br>
    禁止使用压缩命令来压缩指针引用。压缩指针是默认开启的，如果使用压缩命令压缩指针，可以在JVM内存小于32G时做到内存压缩，即在64位机器上做到内存指针对齐只占用32位而不是64位。这样对于小于32G的JVM有非常高的性能提升。该参数只在64位JVM有效

- `-XX:+UseLargePages` <br>
    使用大页内存[4]，默认关闭，使用该参数后开启

- `-XX:+PrintCommandLineFlags`：打印命令行标记，默认关闭

- `-XX:-UseBiasedLocking`：禁用偏向锁

- `-XX:-UseCompressedOops`：禁用压缩指针。默认情况下，启用此选项，并且当Java堆大小小于32 GB时，将使用压缩指针。启用此选项后，对象引用将表示为32位偏移量，而非64位指针，这通常会在运行Java堆大小小于32GB的应用程序时提高性能。此选项仅适用于64位JVM。当Java堆大小大于32 GB时，使用 -XX:ObjectAlignmentInBytes 选项。

- `-XX:VMOptionsFile=filename`：允许用户在文件中指定VM选项。例如 java -XX:VMOptionsFile=/var/my_vm_options HelloWorld。

### 4.2、JIT编译器参数

主要是在JIT编译时用到

- `-XX:+BackgroundCompilation` <br>
    后台编译，默认是开启的，如果要关闭，使用-XX:-BackgroundCompilation或者-Xbatch

- `-XX:CICompilerCount=threads` <br>
    编译时的编译器线程数。server版的JVM默认设置为2，client版本默认设置为1。如果tiered编译开启，则会伸缩到核数个线程

- `-XX:CodeCacheMinimumFreeSpace=size` <br>
    编译使用的最小空闲空间。默认是500KB，如果最小空闲空间不足，则编译会停止

- `-XX:CompileThreshold=invocations` <br>
    编译前解释型方法调用次数。设置这个值，在编译前，会用解释器执行方法若干次用于收集信息，从而可以更高效率的进行编译。默认这个值在JIT中是10000次。可以通过使用-Xcomp参数来禁止编译时的解释执行

- `-XX:+DoEscapeAnalysis` <br>
    开启逃逸分析，默认是开启的

- `-XX:+Inline` <br>
    编译时方法内联。默认是开启的

- `-XX:InlineSmallCode=size` <br>
    设置最大内联方法的代码长度，默认是1000字节，只有小于这个设置的方法才会被编译内联

- `-XX:+LogCompilation` <br>
    编译时日志输出，在编译时会有一个hotspot.log的日志输出到当前工作目录下。可以用`-XX:LogFile`指定不同的目录。默认这个参数是关闭的，即编译日志不输出。这个参数需要和`-XX:UnlockDiagnosticVMOptions`一起使用。也可以使用`-XX:+PrintCompilation`选项在控制台打印编译过程信息

- `-XX:MaxInlineSize=size`：编译内联的方法的最大byte code大小。默认是35，高于35个字节的字节码不会被内联

- `-XX:+OptimizeStringConcat`： 字符串concat优化。默认开启

- `-XX:+PrintAssembly`：通过使用外部的disassembler.so库打印汇编的字节码和native方法来辅助分析。默认是不开启的，需要和`-XX:UnlockDiagnosticVMOptions`一起使用

- `-XX:+PrintCompilation`：将方法编译过程打印到控制台。默认不开启

- `-XX:+PrintInlining`：将内联方法打印出来。默认不开启

- `-XX:-TieredCompilation`：关闭tiered编译，默认是开启的。只有Hotspot支持这个参数

- `-XX:+EliminateAllocations`：是否开启标量替换，JDK8默认开启

- `-XX:+EliminateLokcs`：是否开启锁消除

- `-XX:CompileCommand=command,method[,option]`，在指定方法上执行指定command。command可选项：

**代码缓存（Code Cache）:**

- `-XX:InitialCodeCacheSize=size`：初始化的`code cache`的大小，默认500KB。这个值应该不小于系统最小内存页的大小；

- `-XX:ReservedCodeCacheSize=size`：设置为了JIT编译代码的最大代码cache大小。这个设置默认是`240MB`，如果关掉了tiered编译，则大小是48MB。这个设置必须比初始化的`-XX:InitialCodeCacheSize=size`设置值大；

- `-XX:-PrintCodeCache`：在JVM停止时打印代码缓存的使用情况，默认是关闭的；

- `-XX:-PrintCodeCacheOnCompilation`：每当方法被编译后，就打印一下代码缓存区的使用情况，默认是关闭的；

- `-XX:+UseCodeCacheFlushing`：支持在关闭编译器之前清除code cache。默认是开启的，要关闭就把+换成-

- `-XX:-SegmentedCodeCache`：是否使用分段的代码缓存区，默认关闭，表示使用整体的代码缓存区；

### 4.3、高级服务能力参数

可以做系统信息收集和扩展性的debug

- `-XX:LargePageSizeInBytes=size`:设置用于Java堆的大页面尺寸。单位字节，数值必须是2的次幂。也可在size后追加字母 k 或 K 表示千字节， m 或 M 表示兆字节，或 g 或 G 表示千兆字节

- `-XX:+ExtendedDTraceProbes` <br>
    支持dtrace探测，默认是关闭的

- `-XX:+HeapDumpOnOutOfMemoryError` <br>
    设置当java.lang.OutOfMemoryError发生时，将heap内存dump到当前目录的一个文件。默认是不开启的

- `-XX:HeapDumpPath=path` <br>
    设置在dump heap时将文件dump到哪里。默认是当前目录下 java_pidpid.hprof这样形式的文件。指定文件时例子如下：`-XX:HeapDumpPath=/var/log/java/java_heapdump.hprof`

- `-XX:+HeapDumpAfterFullGC`和`-XX:+HeapDumpBeforeFullGC`：在Full GC前后生成dump文件；需要注意的是一定是发生FGC，而不是CMS GC或者G1这种并发GC

- `-XX:LogFile=path` <br>
    指定日志数据被记录在哪里，默认是在当前目录的hotspot.log下。设置例子如下：`-XX:LogFile=/var/log/java/hotspot.log`

- `-XX:+PrintClassHistogram` <br>
    支持打印类实例的直方图，在按下ctrl+c时（SIGTERM）触发。默认是关闭的。等价于运行jmap -histo命令或者jcmd pid GC.class_histogram命令

- `-XX:+PrintConcurrentLocks` <br>
    支持打印java.util.concurrent的锁信息，在SIGTERM时触发。默认关闭，等价于运行jstack -l或者jcmd pid Thread.print -l命令

- `-XX:+UnlockDiagnosticVMOptions` <br>
    解锁对JVM进行诊断的选项参数。默认是关闭的，开启后支持一些特定参数对JVM进行诊断

### 4.4、垃圾回收参数

这部分参数控制JVM如何进行垃圾回收

**常用堆参数**

- `-XX:NewSize=size` ：设置初始的年轻代的大小。年轻代是分配新对象的地方，是 GC经常发生的地方。设置太低，会频繁minor GC，设置太高的话就只会发生Full GC了。Oracle推荐设置为整体内存的一半或者1/4。该参数等价于-Xmn

- `-XX:InitialHeapSize=size`：设置初始堆内存大小，需要设置为0或者1024的倍数，设置为0说明初始堆大小等于年轻代加年老代的大小；等价于 -Xms

- `-XX:MaxHeapSize=size`：设置最大堆大小，这个值需要大于2MB，且是1024的整数倍。等价于-Xmx

- `-XX:NewRatio=ratio`：设置年轻代和年老代的比例，默认是2；

- `-XX:SurvivorRatio=ratio`：新生代eden区和survivor区的比例。默认是8，最小值是1；

- `-XX:InitialSurvivorRatio=ratio`：设置初始的survivor空间占比，默认值是8，最小值时3；当使用throughput型的GC时有效（即`-XX:+UseParallelGC` 或`-XX:+UseParallelOldGC`）。运行过程中survivor空间占比会自动根据应用运行调整，如果关闭了自适应调整策略（`-XX:-UseAdaptiveSizePolicy`），则`XX:SurvivorRatio`参数会成为survivor空间占比。计算survivor空间大小，依赖young的空间大小，计算公式如下：`S=Y/(R+2)`，其中Y是young空间大小，R是survivor空间占比。一个例子就是如果young空间大小是2MB，而survivor默认占比是8，那么survivor的空间就是0.2MB；

- `-XX:MinSurvivorRatio`：Eden和1个Survivor区的最小比值，默认是 3，在GC之后，如果需要重新计算 Survivor 的值，Survivor 的新值不能低于根据它算出来的值；

- `-XX:TargetSurvivorRatio=percent`：设置在YGC后的期望的survivor空间占比。默认是50%。

- `-XX:InitialTenuringThreshold`：晋升到老年代的对象的初始年龄阈值，默认值是：7

- `-XX:MaxTenuringThreshold=threshold`：设置在自适应GC大小的使用占有最大阈值，默认对于ParallelGC（throughput）的是15（为什么一个对象最多经过15次ygc就回晋升到老生代？是因为对象头里就分配了4个bit来存age属性，所以最大就是1111，即15）；对于CMS的是6。当设置了这个值得时候，第一次会以它为准，后面就不一定了，因为晋升的阈值是动态调整的。如果在CMS GC下设置该值为0的话，相当于每次Minor GC都直接晋升到老年代，此时如果 SurvivorRatio 没有设置的话，会将 SurvivorRatio 默认设置为 1024；

- `-XX:MinHeapFreeRatio=percent`：设置在一次GC后最小的空闲堆内存占比。如果空闲堆内存小于该值，则堆内存扩展。默认是40%；在G1收集器，是针对整个堆，在SerialGC、ParalleGC、CMS GC是针对老年代；`Xminf`和`MinHeapFreeRatio`是等价的，如`-Xminf0.4`等价于`-XX:MinHeapFreeRatio=40`

- `-XX:MaxHeapFreeRatio=percent`：设置在一次GC后最大的堆空闲空间占比。如果空闲堆空间超过这个值，堆空间会被收缩。默认是70%；在G1收集器，是针对整个堆，在SerialGC、ParalleGC、CMS GC是针对老年代；`Xmaxf`和`MaxHeapFreeRatio`是等价的，如`-Xmaxf0.7`等价于`-XX:MaxHeapFreeRatio=70`

- `-XX:MinHeapDeltaBytes`：表示当我们要扩容或者缩容的时候，决定是否要做或者尝试扩容的时候最小扩多少，默认为192K；

- `-XX:+ShrinkHeapInSteps`：是否要逐步地根据`–XX:MaxHeapFreeRatio`的设置，减少分配的堆内存。默认情况下启用该选项，如禁用该参数，那么将会在下一个Full GC时将Java堆直接减少到目标大小，而无需在多个GC周期中“逐步”减少。因此，如果想要让使用的堆内存尽量小，可禁用此选项。

- `-XX:SoftRefLRUPolicyMSPerMB=time`：设置一个软引用对象在上次被引用后在堆内存中保存的时间。默认是每1MB保存1秒钟。该参数对于client模式和server模式有不同的动作，因为client模式JVM在回收时会强制flush掉软引用，然而server模式会尝试先扩容堆空间；

- `-XX:+UseTLAB`：在年轻代支持thread-local分配block，是否启用线程私有分配缓存区（thread-local allocation buffer），默认开启；

- `-XX:MinTLABSize`：最小TLAB大小，单位字节，默认是 2048K；

- `-XX:+ResizeTLAB`：是否动态调整TLAB的大小，默认 是；

- `-XX:TLABRefillWasteFraction`：默认是64K，由于TLAB空间比较小，因此很容易装满。比如TLAB 100K，已使用80KB，当需要再分配一个30KB的对象时，就无法分配到这个TLAB了。这时虚拟机会有两种选择，第一，废弃当前TLAB，这样就会浪费20KB空间；第二，保留当前的TLAB并将这30KB的对象直接分配在堆上，这样将来有小于20KB的对象时，仍可使用这块空间。实际上虚拟机内部维护了一个叫作refill_waste的值，当请求对象大于refill_waste时，会在堆中分配；若小于该值，则会废弃当前TLAB，新建TLAB分配对象。TLABRefillWasteFraction来调整该阈值，它表示TLAB中允许产生这种浪费的比例，默认值为64，即允许使用1/64的TLAB空间作为refill_waste。默认情况下，TLAB和refill_waste都会在运行时不断调整，使系统的运行状态达到最优。如果想要禁用自动调整TLAB的大小，可以使用-XX:-ResizeTLAB禁用ResizeTLAB，并使用-XX:TLABSize手工指定一个TLAB的大小；

- `-XX:+TLABStats`：是否提供详细的TLAB的统计信息，默认是开启的

- `-XX:TLABSize=size`：设置thread-local allocation buffer (TLAB)的初始化大小，默认是 0；

- `-XX:TLABWasteTargetPercent`：允许TLAB占用Eden空间百分比，默认是 1

- `-XX:+AggressiveHeap` <br>
    java堆内存优化。默认是关闭的，如果开启后，针对一些长时间运行的且有密集的内存分配操作，JVM根据系统cpu和内存的配置参数来进行优化

- `-XX:+AlwaysPreTouch`：支持在JVM启动时touch每一页，这样做会导致每页都会进入内存。可以用来模拟测试长时间运行的任务，将虚拟内存全部映射到物理内存。默认是关闭的

- `-XX:+CMSClassUnloadingEnabled` <br>
    支持CMS垃圾回收下的类卸载。默认是开启的。

- `-XX:CMSExpAvgFactor=percent` <br>
    设置一个时间百分比，用来加权并发回收统计的指数平均的样本。默认是25%

- `-XX:CMSInitiatingOccupancyFraction=percent` <br>
    设置一个年老代的占比，达到多少会触发CMS回收。默认是92%，任何一个负值的设定都表示了用-XX:CMSTriggerRatio来做真实的初始化值。设置方法如下：`-XX:CMSInitiatingOccupancyFraction=20`；CMS GC下如果没有指定老生代固定使用率触发CMS GC的阈值，那么MinHeapFreeRatio配合CMSTriggerRatio(默认80)参数会计算出触发CMS GC的老生代使用率阈值，具体算法是`((100 -MinHeapFreeRatio) + CMSTriggerRatio* MinHeapFreeRatio) / 100.0)  / 100.0`

    比如 MinHeapFreeRatio 默认是 40， CMSTriggerRatio 默认是 80，那么 `((100-40) + 80*40/100))/100 = 92%`

- `-XX:+CMSScavengeBeforeRemark` <br>
    开启功能在CMSremark前进行Scavenge。默认是关闭的

- `-XX:CMSTriggerRatio=percent` <br>
    设置一个在CMS开始前的内存的触发百分比，针对的是由`-XX:MinHeapFreeRatio`分配的内存。默认是80

- `-XX:ConcGCThreads=threads`：设置支持并发GC的线程数。默认值依赖于给JVM的CPU数目

- `-XX:+DisableExplicitGC`：关闭显式GC调用，即关闭System.gc()。默认是可以调用的

- `-XX:+ExplicitGCInvokesConcurrent` <br>
    支持通过System.gc()来做并发的GC。默认是不支持的。该参数一定要和-XX:+UseConcMarkSweepGC一起使用

- `-XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses` <br>
    支持通过System.gc()来做并发的GC并且卸载类。默认是不支持的。该参数一定要和-XX:+UseConcMarkSweepGC一起使用

- `-XX:G1HeapRegionSize=size`：设置在使用G1收集器时Java堆被划分为子区域的大小。在1MB到32MB之间，默认会根据Java堆的大小自动检测

- `-XX:+G1PrintHeapRegions` <br>
    打印出哪些region是被分配的，哪些是被G1取回的。默认是关闭打印的

- `-XX:G1ReservePercent=percent` <br>
    设置一个堆内存的百分比用来作为false ceiling，从而降低使用G1时晋升失败的可能性。默认是10%

- `-XX:InitiatingHeapOccupancyPercent=percent` <br>
    设置一个触发并发GC的堆占用百分比。这个值对于基于整体内存的垃圾回收器有效，比如G1。默认是45%，如果设置为0表示无停顿GC

- `-XX:MaxGCPauseMillis=time` <br>
    设置一个最大的GC停顿时间（毫秒），这是个软目标，JVM会尽最大努力去实现它，默认值200ms

- `-XX:MaxMetaspaceSize=size` <br>
    为类的元数据进行分配的metaspace最大native内存大小，默认情况这个值无限制。该值依赖于当前的JVM、其他在运行的JVM和系统可用内存

- `-XX:MaxNewSize=size` <br>
    设置最大的年轻代的堆大小。默认自动检测

- `-XX:MetaspaceSize=size` <br>
    设置一个metaspace的大小，第一次超出该分配后会触发GC。默认值依赖于平台，该值会在运行时增加或减少

- `-XX:CompressedClassSpaceSize`<br>
    启用CCS，设置压缩类空间大小

- `-XX:ParallelGCThreads=threads`：并行GC时的线程数。默认值是CPU数

- `-XX:+ParallelRefProcEnabled` <br>
    支持并发引用处理，默认是关闭的。

- `-XX:+PrintAdaptiveSizePolicy` <br>
    打印自适应调整策略。默认关闭

- `-XX:+PrintGC` <br>
    打印每次GC的消息，默认是关闭的

- `-XX:+PrintGCApplicationConcurrentTime` <br>
    打印上次GC暂停到目前的时间。默认不打印

- `-XX:+PrintGCApplicationStoppedTime` <br>
    打印GC暂停的时间长度。默认不打印

- `-XX:+PrintGCDateStamps` <br>
    打印每个GC的日期时间戳。默认不打印。

- `-XX:+PrintGCDetails` <br>
    打印每次GC的细节信息。默认不打印

- `-XX:+PrintGCTaskTimeStamps`：打印每个独立的GC线程任务的时间戳。默认不打印

- `-XX:+PrintGCTimeStamps` <br>
    打印每次GC的时间戳。默认不打印。

- `-XX:+PrintStringDeduplicationStatistics` <br>
    打印细节的deduplication信息。默认不打印。

- `-XX:+PrintTenuringDistribution` <br>
    打印所在的年龄代的信息。具体例子如下：
    `
    Desired survivor size 48286924 bytes, new threshold 10 (max 10) 
    - age 1: 28992024 bytes, 28992024 total 
    - age 2: 1366864 bytes, 30358888 total 
    - age 3: 1425912 bytes, 31784800 total 
    ...
    `
    其中age1是最年轻的survivor，age2存活了2代，以此类推。默认该项关闭。

- `-XX:+ScavengeBeforeFullGC` <br>
    在每次Full GC前做一次年轻代的GC。该项默认是开启的。

- `-XX:StringDeduplicationAgeThreshold=threshold` <br>
    string对象到达特定的age后会去除重复数据。默认是3，jvm中每次gc后存活的对象，age会加一。string对象在晋升为年老代之前都是去除重复数据的候选对象

- `-XX:+UseAdaptiveSizePolicy` <br>
    使用自适应分代大小。默认是开启的

- `-XX:+UseCMSInitiatingOccupancyOnly` <br>
    设置使用占用值作为初始化CMS收集器的唯一条件。默认是不开启

- `-XX:+UseConcMarkSweepGC` <br>
    设置让CMS也支持老年代的回收。默认是不开启的，如果开启，那么`-XX:+UseParNewGC`也会自动被设置。Java 8 不支持`-XX:+UseConcMarkSweepGC`，` -XX:-UseParNewGC`这种组合

- `-XX:+UseG1GC ` <br>
    设置使用G1作为GC收集器。G1比较推荐在大堆应用场景下使用（大于6GB）

- `-XX:+UseGCOverheadLimit` <br>
    设置一种策略用来设置一个时间比率来限制在OOM之前的GC时间。默认是开启的，并行GC时如果有多于98%以上的时间用来gc就会抛出OOM。当堆空间较小时这个参数有助于保护应用程序不至于长时间的停顿没有进展

- `-XX:+UseNUMA` <br>
    使用NUMA[5]开启性能优化。默认不开启，该项只有在开启了`-XX:+UseParallelGC`后才有效

- `-XX:+UseParallelGC` <br>
    支持并行的垃圾收集器，即throughput垃圾收集。这可以在多核处理器下提升垃圾收集性能。默认不开启，收集器由系统根据JVM和机器配置自动选择。开启后`-XX:+UseParallelOldGC`选项也自动开启。

- `-XX:+UseParallelOldGC` <br>
    支持FULL GC的并行收集器。默认不开启

- `-XX:+UseParNewGC` <br>
    支持在年轻代用多线程进行垃圾收集。默认不开启，使用`-XX:+UseConcMarkSweepGC`时会自动被开启

- `-XX:+UseSerialGC` <br>
    支持使用串行收集器。默认不开启

- `-XX:+UseSHM` <br>
    在Linux环境下支持JVM使用共享内存来设置大页

- `-XX:+UseStringDeduplication` <br>
    支持string的去重存储。默认关闭，要使用该选项，必须使用G1垃圾回收器`-XX:+UseG1GC`。

- `-XX:GCLogFileSize=number`：处理大型日志文件，默认为512K

## 5、线上参数配置

`java -Xmx4096m -Xms4096m -Xss256k -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=14 -XX:ParallelGCThreads=2 -XX:ConcGCThreads=2 -XX:+UseG1GC -XX:+DisableExplicitGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -Xloggc:./logs/app_gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./logs/localhost.dump -jar /home/admin/application.jar -Ddubbo.application.qos.enable=false --spring.profiles.active=prod`

`-Xms12288m -Xmx12288m -XX:SurvivorRatio=8 -Dspring.profiles.active=prod -XX:ParallelGCThreads=4 -XX:G1HeapRegionSize=16m -XX:+UseG1GC `

# 参考文章

* [Java8 JVM参数解读](https://www.zybuluo.com/changedi/note/975529)
* [JVM参数优化](https://blog.csdn.net/liuxinghao/article/details/73963399)
* [JVM命令参数大全](https://blog.csdn.net/zero__007/article/details/52848040)
* [Java HotSpot VM Command-Line Options](https://docs.oracle.com/javase/8/docs/technotes/guides/troubleshoot/clopts001.html)
* [JDK Hotspot VM Options](https://chriswhocodes.com/)
