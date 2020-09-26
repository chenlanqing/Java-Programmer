# 一、基本概述

```java
public final class System {
    private static native void registerNatives();
    static {
        registerNatives();
    }
    private System() {
    }
}
```

# 二、属性



# 三、方法

## 1、关于System.nanoTime() 与 System.currentTimeMillis()

三个问题：
	
- 在mac下发现System.nanoTime()在JDK7和JDK8下输出的值怎么完全不一样？
- System.nanoTime()的值很奇怪，究竟是怎么算出来的？
- System.currentTimeMillis()为何不是 System.nanoTime()的 1000000 倍？
	
### 1.1、MAC 不同JDK版本下nanoTime实现异同

分析JDK7和JDK8的C语言实现，发现JDK8下多了一个 __APPLE__ 宏下定义的实现，和JDK7及之前的版本的实现是不一样的。不过其他BSD系统是一样的，只是 MacOC 有点不一样

### 1.2、System.nanoTime()的值很奇怪，究竟是怎么算出来的？

nanoTime其实算出来的是一个相对的时间，相对于系统启动的时候的时间(在linux下JDK7和JDK8的实现都是一样的)

### 1.3、System.currentTimeMillis()为何不是 System.nanoTime()的 1000000 倍：

currentTimeMillis其实是通过gettimeofday来实现的。System.currentTimeMillis()就是返回的当前时间距离 1970/01/01 08：00：00 的毫秒数

### 1.4、为什么计算时间从 “1970年1月1日” 开始？

- Java 起源于 UNIX 系统，而 UNIX 认为 1970 年 1 月 1 日 0 点是时间纪元；
- 最初计算机操作系统是32 位，而时间也是用 32 位表示，System.out.println(Integer.MAX_VALUE); 2147483647

	因为用32 位来表示时间的最大间隔是68年，而最早出现的UNIX操作系统考虑到计算机产生的年代和应用的时限综合取了1970年1月1日作为UNIX TIME的纪元时间，Integer在JAVA内用32位表示，因此32位能表示的最大值是2147483647，另外1年 365天的总秒数是31536000，2147483647/31536000 = 68.1，也就是说32 位能表示的最长时间是68年，而实际上到2038年01月19日03时14分07秒，便会到达最大时间；这就是 [2038](https://en.wikipedia.org/wiki/Year_2038_problem) 问题
- 至于时间回归的现象相信随着64 为操作系统 的产生逐渐得到解决，因为用64位操作系统可以表示到“292，277，026，596年12月4日15时30分08秒”；
- System.out.println(new Date(0))，打印出来的时间是8点而非0点存在系统时间和本地时间的问题，其实系统时间依然是0点，不过跟你所在时区有关。

## 2、System.gc方法

### 2.1、方法实现

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
### 2.2、作用

- 做一次full gc
- 执行后会暂停整个进程。
- System.gc我们可以禁掉，使用-XX:+DisableExplicitGC，其实一般在cms gc下我们通过-XX:+ExplicitGCInvokesConcurrent也可以做稍微高效一点的gc，也就是并行gc。
- 最常见的场景是RMI/NIO下的堆外内存分配等；

## 3、getProperties 与 getEnv

### 3.1、getProperties

Java平台使用Properties对象来提供关于本地系统和配置的信息，我们称之为系统属性。

系统属性包括当前用户、Java运行时的当前版本和文件路径名分隔符等信息。

可以使用`System.getProperty("log_dir")`读取属性log_dir的值。还可以使用了默认值参数，如果属性不存在，getProperty返回`/tmp/log`
```java
String log_dir = System.getProperty("log_dir","/tmp/log");
// 如果在运行时需要更新，可以调用如下方法：
System.setProperty("log_dir", "/tmp/log");
```
也可以使用命令行的形式设置Property，这样通过System.getProperty 返回的是个字符串
```java
java -jar jarName -DpropertyName=value
```

所有系统的Properties属性：
<table summary="Shows property keys and associated values">
    <tr><th>Key</th><th>Description of Associated Value</th></tr>
    <tr><td><code>java.version</code></td><td>Java Runtime Environment version</td></tr>
    <tr><td><code>java.vendor</code></td><td>Java Runtime Environment vendor</td></tr>
    <tr><td><code>java.vendor.url</code></td><td>Java vendor URL</td></tr>
    <tr><td><code>java.home</code></td><td>Java installation directory</td></tr>
    <tr><td><code>java.vm.specification.version</code></td><td>Java Virtual Machine specification version</td></tr>
    <tr><td><code>java.vm.specification.vendor</code></td><td>Java Virtual Machine specification vendor</td></tr>
    <tr><td><code>java.vm.specification.name</code></td><td>Java Virtual Machine specification name</td></tr>
    <tr><td><code>java.vm.version</code></td><td>Java Virtual Machine implementation version</td></tr>
    <tr><td><code>java.vm.vendor</code></td><td>Java Virtual Machine implementation vendor</td></tr>
    <tr><td><code>java.vm.name</code></td><td>Java Virtual Machine implementation name</td></tr>
    <tr><td><code>java.specification.version</code></td><td>Java Runtime Environment specification  version</td></tr>
    <tr><td><code>java.specification.vendor</code></td><td>Java Runtime Environment specification  vendor</td></tr>
    <tr><td><code>java.specification.name</code></td><td>Java Runtime Environment specification  name</td></tr>
    <tr><td><code>java.class.version</code></td><td>Java class format version number</td></tr>
    <tr><td><code>java.class.path</code></td><td>Java class path</td></tr>
    <tr><td><code>java.library.path</code></td><td>List of paths to search when loading libraries</td></tr>
    <tr><td><code>java.io.tmpdir</code></td><td>Default temp file path</td></tr>
    <tr><td><code>java.compiler</code></td><td>Name of JIT compiler to use</td></tr>
    <tr><td><code>java.ext.dirs</code></td><td>Path of extension directory or directories<b>Deprecated.</b> <i>This property, and the mechanism which implements it, may be removed in a future release.</i> </td></tr>
    <tr><td><code>os.name</code></td><td>Operating system name</td></tr>
    <tr><td><code>os.arch</code></td><td>Operating system architecture</td></tr>
    <tr><td><code>os.version</code></td><td>Operating system version</td></tr>
    <tr><td><code>file.separator</code></td><td>File separator ("/" on UNIX)</td></tr>
    <tr><td><code>path.separator</code></td><td>Path separator (":" on UNIX)</td></tr>
    <tr><td><code>line.separator</code></td><td>Line separator ("\n" on UNIX)</td></tr>
    <tr><td><code>user.name</code></td><td>User's account name</td></tr>
    <tr><td><code>user.home</code></td><td>User's home directory</td></tr>
    <tr><td><code>user.dir</code></td><td>User's current working directory</td></tr>
</table>


### 3.2、getEnv

环境变量像Properties一样，是一个键值对，操作系统使用环境变量来允许将配置信息传递到应用程序；设置环境变量的方法因操作系统的不同而不同。例如，在Windows中，我们使用来自控制面板的系统实用程序，而在Unix中，我们使用shell脚本

getEnv 返回的是一个只读的Map，如果向Map中添加值的话，会报UnsupportedOperationException；

如果需要向环境变量添加新变量，要在Java中创建一个新进程，可以使用ProcessBuilder类，它有一个名为environment的方法。这个方法返回一个映射，但是这次映射不是只读的，这意味着我们可以向它添加元素
```java
List<String> command = new ArrayList<>();
command.add("/bin/ls");
command.add("/root");

ProcessBuilder pb = new ProcessBuilder(command);
Map<String, String> env = pb.environment();
env.put("log_dir", "/tmp/log");
Process process = pb.start();
```

## 4、load 与  loadLibrary

JDK 提供给了我们两个方法用于载入库文件，一个是`System.load(String filename)`方法，另一个是`System.loadLibrary(String libname)`方法，他们的区别主要如下：

- 加载的路径不同：
    - `System.load(String filename)` 是从作为动态库的本地文件系统中以指定的文件名加载代码文件，文件名参数必须是完整的路径名且带文件后缀
    - `System.loadLibrary(String libname)` 是加载由`libname`参数指定的系统库（系统库指的是`java.library.path`，可以通过` System.getProperty(String key)` 方法查看 java.library.path 指向的目录内容），将库名映射到实际系统库的方法取决于系统实现，譬如在 Android 平台系统会自动去系统目录、应用 lib 目录下去找 libname 参数拼接了 lib 前缀的库文件;
- 是否自动加载库的依赖库:

    譬如libA.so 和 libB.so 有依赖关系
    - 如果选择 `System.load("/sdcard/path/libA.so")`，即使 libB.so 也放在 `/sdcard/path/` 路径下，load 方法还是会因为找不到依赖的 libB.so 文件而失败，因为虚拟机在载入 libA.so 的时候发现它依赖于 libB.so，那么会先去 java.library.path 下载入 libB.so，而 libB.so 并不位于 `java.library.path` 下，所以会报错；
    - 使用 `System.loadLibrary("A")`，然后把 libA.so 和 libB.so 都放在 `java.library.path` 下即可

## 参考文章

* [System.nanoTime的实现原理](https：//yq.aliyun.com/articles/67089？spm=5176.8091938.0.0.eWP39h)