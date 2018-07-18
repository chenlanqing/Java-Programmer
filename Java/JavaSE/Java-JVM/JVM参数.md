
# 一、JVM参数
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
- -XX:+option 启用选项 
- -XX:-option 不启用选项 
- -XX:option=number 给选项设置一个数字类型值，可跟单位，例如 128k, 256m, 1g 
- -XX:option=string 给选项设置一个字符串值，例如-XX:HeapDumpPath=./dump.core

## 1、标准参数

标准参数是被所有JVM实现都要支持的参数。用于做一些常规的通用的动作，比如检查版本、设置classpath等

- ```-agentlib:libname[=options]``` <br>
    这个命令加载指定的native agent库。理论上这条option出现后，JVM会到本地固定路径下LD_LIBRARY_PATH这里加载名字为libxxx.so的库

- ```-agentpath:pathname[=options]``` <br>
    这个参数指定了从哪个绝对路径加载agent库。与-agentlib相同，只不过是用绝对路径来指明库文件

- ```-client、-server``` <br>
    指定JVM的启动模式是client模式还是server模式，具体就是 Java HotSpot Client(Server) VM 版本。目前64位的JDK启动，一定是server模式，会忽略这个参数

- ```-Dproperty=value``` <br>
    设置系统属性，设置后的属性可以在代码中System.getProperty方法获取到

- ```disableassertions[:[packagename]...|:classname] 和 -da[:[packagename]...|:classname] ```<br>
    关闭指定包或类下的assertion，默认是关闭的

- ```-disablesystemassertions、-dsa``` <br>
    关闭系统包类下的assertion

- ```-verbose:class、-verbose:gc、-verbose:jni``` <br>
    这些组合都是用来展示信息的，class展示的每个class的信息，gc展示每个GC事件的信息，jni开启展示JNI调用信息

- ```-javaagent:jarpath[=options]``` <br>
    加载指定的java agent，具体需要传入jar路径

## 2、非标准参数

针对官方JVM也就是HotSpot的，不同的虚拟机有各自的非标准参数

- ```-X``` <br>
    展示所有-X开头的参数说明

- ```-Xbatch``` <br>
    禁止后台编译。将编译过程放到前台任务执行。JVM默认会将编译任务当做后台任务执行。这个参数等价于```-XX:-BackgroundCompilation```

- ```-Xfuture``` <br>
    强制class文件格式检查

- ```-Xint``` <br>
    在 interpreted-only模式运行程序。编译为native的模式不再生效，所有的字节码都在解释器环境下解释执行

- ```-Xinternalversion``` <br>
    打印一个更详细的java版本信息，执行后退出

- ```-Xloggc:filename``` <br>
    设置gc日志文件，gc相关信息会重定向到该文件。这个配置如果和-verbose:gc同时出现，会覆盖-verbose:gc参数，比如```-Xloggc:/home/admin/logs/gc.log```

- ```-Xmaxjitcodesize=size``` <br>
    为JIT编译的代码设置最大的code cache。默认的设置是240m，如果关闭了tiered compilation，那么默认大小是48m。这个参数和```-XX:ReservedCodeCacheSize```是等价的

- ```-Xmixed``` <br>
    用解释器执行所有的字节码，除了被编译为native code的hot method

- ```-Xmssize``` <br>
    设置初始的堆大小，如果没有设置这个值，那么它的初始化大小就是年轻代和老年代的和，等价于```-XX:InitialHeapSize```；这个值的大小必须是1024的倍数，并且大于1M

- ```-Xmxsize``` <br>
    设置最大的内存分配大小，一般的服务端部署，-Xms和-Xmx设置为同样大小。与```-XX:MaxHeapSize```具有同样的作用

- ```-Xmnsize``` <br>
    设置年轻代大小，还可以通过其他两个选项来代替这个选项来指定年轻代最小和最大内存：```-XX:NewSize```指定初始化大小,```-XX:MaxNewSize```指定最大内存大小

- ```-Xnoclassgc``` <br>
    关闭对class的GC。这样设置可以节约一点GC的时间，不过带来的影响就是class永驻内存，不当的使用会导致OOM风险

- ```-XshowSettings:category``` <br>
    查看settings信息，category可以是all、locale、properties和vm几部分

- ```-Xsssize``` <br>
    设置thread stack大小，一般默认的几个系统参数如下，这个选项和```-XX:ThreadStackSize```相同

- ```-Xverify:mode``` <br>
    设置字节码校验器的模式，默认是remote，即只校验那些不是通过bootstrap类加载器加载的字节码。而还有一个模式还all，即全部都校验。虽然还有一个模式是none，但是本质上jvm不生效这个参数

## 3、运行时参数
这类参数控制Java HotSpot VM在运行时的行为

- ```-XX:+DisableAttachMechanism``` <br>
    设置JVM的attach模式，如果启动了这个参数，jvm将不允许attach，这样类似jmap等程序就无法使用了

- ```-XX:ErrorFile=filename``` <br>
    当不可恢复的错误发生时，错误信息记录到哪个文件。默认是在当前目录的一个叫做hs_err_pid pid.log的文件。如果指定的目录没有写权限，这时候文件会创建到/tmp目录下

- ```-XX:MaxDirectMemorySize=size``` <br>
    为NIO的direct-buffer分配时指定最大的内存大小。默认是0，意思是JVM自动选择direct-buffer的大小

- ```-XX:ObjectAlignmentInBytes=alignment``` <br>
    java对象的内存对齐大小。默认是8字节，JVM实际计算堆内存上限的方法是

- ```-XX:OnError=string``` <br>
    在jvm出现错误(不可恢复）的时候，执行哪些命令，具体例子如下：```-XX:OnError="gcore %p;dbx - %p"```

- ```-XX:OnOutOfMemoryError=string``` <br>
    同```-XX:OnError=string```，具体错误是OOM

- ```-XX:-UseCompressedOops``` <br>
    禁止使用压缩命令来压缩指针引用。压缩指针是默认开启的，如果使用压缩命令压缩指针，可以在JVM内存小于32G时做到内存压缩，即在64位机器上做到内存指针对齐只占用32位而不是64位。这样对于小于32G的JVM有非常高的性能提升。该参数只在64位JVM有效

- ```-XX:+UseLargePages``` <br>
    使用大页内存[4]，默认关闭，使用该参数后开启

## 4、JIT编译器参数
主要是在JIT编译时用到

- ```-XX:+BackgroundCompilation``` <br>
    后台编译，默认是开启的，如果要关闭，使用-XX:-BackgroundCompilation或者-Xbatch

- ```-XX:CICompilerCount=threads``` <br>
    编译时的编译器线程数。server版的JVM默认设置为2，client版本默认设置为1。如果tiered编译开启，则会伸缩到核数个线程

- ```-XX:CodeCacheMinimumFreeSpace=size``` <br>
    编译使用的最小空闲空间。默认是500KB，如果最小空闲空间不足，则编译会停止

- ```-XX:CompileThreshold=invocations``` <br>
    编译前解释型方法调用次数。设置这个值，在编译前，会用解释器执行方法若干次用于收集信息，从而可以更高效率的进行编译。默认这个值在JIT中是10000次。可以通过使用-Xcomp参数来禁止编译时的解释执行

- ```-XX:+DoEscapeAnalysis``` <br>
    支持转义分析，默认是开启的

- ```-XX:InitialCodeCacheSize=size``` <br>
    初始化的code cache的大小，默认500KB。这个值应该不小于系统最小内存页的大小

- ```-XX:+Inline``` <br>
    编译时方法内联。默认是开启的

- ```-XX:InlineSmallCode=size``` <br>
    设置最大内联方法的代码长度，默认是1000字节，只有小于这个设置的方法才会被编译内联

- ```-XX:+LogCompilation``` <br>
    编译时日志输出，在编译时会有一个hotspot.log的日志输出到当前工作目录下。可以用```-XX:LogFile```指定不同的目录。默认这个参数是关闭的，即编译日志不输出。这个参数需要和```-XX:UnlockDiagnosticVMOptions```一起使用。也可以使用```-XX:+PrintCompilation```选项在控制台打印编译过程信息

- ```-XX:MaxInlineSize=size``` <br>
    编译内联的方法的最大byte code大小。默认是35，高于35个字节的字节码不会被内联

- ```-XX:+OptimizeStringConcat``` <br>
    字符串concat优化。默认开启

- ```-XX:+PrintAssembly``` <br>
    通过使用外部的disassembler.so库打印汇编的字节码和native方法来辅助分析。默认是不开启的，需要和```-XX:UnlockDiagnosticVMOptions```一起使用

- ```-XX:+PrintCompilation``` <br>
    将方法编译过程打印到控制台。默认不开启

- ```-XX:+PrintInlining``` <br>
    将内联方法打印出来。默认不开启

- ```-XX:ReservedCodeCacheSize=size``` <br>
    设置为了JIT编译代码的最大代码cache大小。这个设置默认是240MB，如果关掉了tiered编译，则大小是48MB。这个设置必须比初始化的```-XX:InitialCodeCacheSize=size```设置值大

- ```-XX:-TieredCompilation``` <br>
    关闭tiered编译，默认是开启的。只有Hotspot支持这个参数

- ```-XX:+UseCodeCacheFlushing``` <br>
    支持在关闭编译器之前清除code cache。默认是开启的，要关闭就把+换成-

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>

- ``` ``` <br>



# 参考文章

* [JVM参数优化](https://www.zybuluo.com/changedi/note/975529)
* [Java8 JVM参数解读](https://blog.csdn.net/liuxinghao/article/details/73963399)
* [JVM命令参数大全](https://blog.csdn.net/zero__007/article/details/52848040)