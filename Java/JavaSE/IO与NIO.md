<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、I/O 模型](#%E4%B8%80io-%E6%A8%A1%E5%9E%8B)
  - [1、异步与同步](#1%E5%BC%82%E6%AD%A5%E4%B8%8E%E5%90%8C%E6%AD%A5)
  - [2、阻塞与非阻塞](#2%E9%98%BB%E5%A1%9E%E4%B8%8E%E9%9D%9E%E9%98%BB%E5%A1%9E)
  - [3、阻塞IO与非阻塞IO](#3%E9%98%BB%E5%A1%9Eio%E4%B8%8E%E9%9D%9E%E9%98%BB%E5%A1%9Eio)
  - [4、异步IO与同步IO](#4%E5%BC%82%E6%AD%A5io%E4%B8%8E%E5%90%8C%E6%AD%A5io)
  - [5、五种IO模型](#5%E4%BA%94%E7%A7%8Dio%E6%A8%A1%E5%9E%8B)
  - [6、高性能IO设计模式-多线程与线程池](#6%E9%AB%98%E6%80%A7%E8%83%BDio%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F-%E5%A4%9A%E7%BA%BF%E7%A8%8B%E4%B8%8E%E7%BA%BF%E7%A8%8B%E6%B1%A0)
- [二、Java IO](#%E4%BA%8Cjava-io)
  - [1、输入与输出：数据源和目标媒介](#1%E8%BE%93%E5%85%A5%E4%B8%8E%E8%BE%93%E5%87%BA%E6%95%B0%E6%8D%AE%E6%BA%90%E5%92%8C%E7%9B%AE%E6%A0%87%E5%AA%92%E4%BB%8B)
  - [2、文件-一种常用的数据源或者存储数据的媒介](#2%E6%96%87%E4%BB%B6-%E4%B8%80%E7%A7%8D%E5%B8%B8%E7%94%A8%E7%9A%84%E6%95%B0%E6%8D%AE%E6%BA%90%E6%88%96%E8%80%85%E5%AD%98%E5%82%A8%E6%95%B0%E6%8D%AE%E7%9A%84%E5%AA%92%E4%BB%8B)
  - [3、管道](#3%E7%AE%A1%E9%81%93)
  - [4、网络](#4%E7%BD%91%E7%BB%9C)
  - [5、字节和字符数组](#5%E5%AD%97%E8%8A%82%E5%92%8C%E5%AD%97%E7%AC%A6%E6%95%B0%E7%BB%84)
  - [6、System.in、System.out、System.err：标准输出](#6systeminsystemoutsystemerr%E6%A0%87%E5%87%86%E8%BE%93%E5%87%BA)
  - [7、流-仅仅只是一个连续的数据流](#7%E6%B5%81-%E4%BB%85%E4%BB%85%E5%8F%AA%E6%98%AF%E4%B8%80%E4%B8%AA%E8%BF%9E%E7%BB%AD%E7%9A%84%E6%95%B0%E6%8D%AE%E6%B5%81)
  - [8、Reader、Writer：](#8readerwriter)
- [三、Java NIO](#%E4%B8%89java-nio)
  - [1.Java NIO 概述](#1java-nio-%E6%A6%82%E8%BF%B0)
  - [2.Channel](#2channel)
  - [3、Buffer](#3buffer)
  - [5.通道之间的数据传输](#5%E9%80%9A%E9%81%93%E4%B9%8B%E9%97%B4%E7%9A%84%E6%95%B0%E6%8D%AE%E4%BC%A0%E8%BE%93)
- [参考文章](#%E5%8F%82%E8%80%83%E6%96%87%E7%AB%A0)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->



# 一、I/O 模型
## 1、异步与同步

**1.1、同步**

如果有多个任务或者事件要发生，这些任务或者事件必须逐个地进行，一个事件或者任务的执行会导致整个流程的暂时等待，这些事件没有办法并发地执行；

**1.2、异步**

如果有多个任务或者事件发生，这些事件可以并发地执行，一个事件或者任务的执行不会导致整个流程的暂时等待;

## 2、阻塞与非阻塞
**2.1、阻塞**

当某个事件或者任务在执行过程中，它发出一个请求操作，但是由于该请求操作需要的条件不满足，那么就会一直在那等待，直至条件满足；

**2.2、非阻塞**

当某个事件或者任务在执行过程中，它发出一个请求操作，如果该请求操作需要的条件不满足，会立即返回一个标志信息告知条件不满足，不会一直在那等待;

**2.3、阻塞和非阻塞的区别**

关键在于当发出请求一个操作时，如果条件不满足，是会一直等待还是返回一个标志信息	

## 3、阻塞IO与非阻塞IO

**3.1、IO操作包括-对硬盘的读写、对socket的读写以及外设的读写;**

**3.2、完整的IO请求操作包括两个阶段**

- 查看数据是否就绪;
- 进行数据拷贝(内核将数据拷贝到用户线程);

**3.3、阻塞(blocking IO)和非阻塞(non-blocking IO)的区别：**

其区别就在于第一个阶段，如果数据没有就绪，在查看数据是否就绪的过程中是一直等待，还是直接返回一个标志信息.

## 4、异步IO与同步IO

**4.1、概述**

- 同步IO即如果一个线程请求进行IO操作，在IO操作完成之前，该线程会被阻塞
- 异步IO为如果一个线程请求进行IO操作，IO操作不会导致请求线程被阻塞

**4.2、同步IO和异步IO模型是针对用户线程和内核的交互来说的：**

- 同步IO：当用户发出IO请求操作之后，如果数据没有就绪，需要通过用户线程或者内核不断地去轮询数据是否就绪，当数据就绪时，再将数据从内核拷贝到用户线程；
- 异步IO：只有IO请求操作的发出是由用户线程来进行的，IO 操作的两个阶段都是由内核自动完成，然后发送通知告知用户线程IO操作已经完成.也就是说在异步IO中，不会对用户线程产生任何阻塞

**4.3、同步IO和异步IO关键区别**

同步IO和异步IO的关键区别反映在数据拷贝阶段是由用户线程完成还是内核完成，所以说"异步IO"必须要有操作系统的底层支持;

**4.4、“同步IO和异步IO”与“阻塞IO和非阻塞IO”是不同的两组概念**

阻塞IO和非阻塞IO是反映在当用户请求IO操作时，如果数据没有就绪，是用户线程一直等待数据就绪，还是会收到一个标志信息这一点上面的;也就是说：阻塞IO和非阻塞IO是反映在IO操作的第一个阶段，在查看数据是否就绪时是如何处理的;

## 5、五种IO模型
### 5.1、阻塞IO模型

最传统的一种 IO模型，即在读写数据过程中会发生阻塞现象

当用户线程发出IO请求之后，内核会去查看数据是否就绪，如果没有就绪就会等待数据就绪，而用户线程就会处于阻塞状态，用户线程交出CPU.当数据就绪之后，内核会将数据拷贝到用户线程，并返回结果给用户线程，用户线程才解除block状态

典型的例子：<br>
    data = socket.read();

### 5.2、非阻塞IO模型
	
用户线程发起一个read操作后，并不需要等待，而是马上就得到了一个结果

- 一旦内核中的数据准备好了，并且又再次收到了用户线程的请求，那么它马上就将数据拷贝到了用户线程，然后返回
- 在非阻塞IO模型中，用户线程需要不断地询问内核数据是否就绪，也就说非阻塞IO不会交出CPU，而会一直占用CPU

### 5.3、多路复用IO模型

多路复用IO模型是目前使用得比较多的模型;Java NIO 实际上就是多路复用IO

- 多路复用IO模型中，会有一个线程不断去轮询多个socket的状态，只有当socket真正有读写事件时，才真正调用实际的IO读写操作
- 在多路复用IO模型中，只需要使用一个线程就可以管理多个socket，系统不需要建立新的进程或者线程，也不必维护这些线程和进程，并且只有在真正有socket读写事件进行时，才会使用IO资源，所以它大大减少了资源占用
- 多路复用IO为何比非阻塞IO模型的效率高是因为在非阻塞IO中，不断地询问socket状态时通过用户线程去进行的，而在多路复用IO中，轮询每个socket状态是内核在进行的，这个效率要比用户线程要高的多
- 注意点：多路复用IO模型来说，一旦事件响应体很大，那么就会导致后续的事件迟迟得不到处理，并且会影响新的事件轮询

### 5.4、信号驱动 IO 模型：

在信号驱动IO模型中，当用户线程发起一个IO请求操作，会给对应的socket注册一个信号函数，然后用户线程会继续执行，当内核数据就绪时会发送一个信号给用户线程，用户线程接收到信号之后，便在信号函数中调用IO读写操作来进行实际的IO请求操作

### 5.5、异步 IO 模型

- 最理想的IO模型
- 在异步IO模型中，IO操作的两个阶段都不会阻塞用户线程，这两个阶段都是由内核自动完成，然后发送一个信号告知用户线程操作已完成，用户线程中不需要再次调用IO函数进行具体的读写
- 与信号驱动 IO 模型相比：<br>
    - 在信号驱动模型中，当用户线程接收到信号表示数据已经就绪，然后需要用户线程调用IO函数进行实际的读写操作；
    - 在异步IO模型中，收到信号表示IO操作已经完成，不需要再在用户线程中调用iO函数进行实际的读写操作
- 注意：异步IO是需要操作系统的底层支持，在Java 7中，提供了Asynchronous IO

### 5.6、总结：

前面四种IO模型实际上都属于同步IO，只有最后一种是真正的异步IO；因为：前面四种模型中IO操作的第2个阶段都会引起用户线程阻塞，也就是说内核进行数据拷贝的过程都会让用户线程阻塞

## 6、高性能IO设计模式-多线程与线程池


# 二、Java IO
## 1、输入与输出：数据源和目标媒介

**1.1、Java IO关注的是从原始数据源的读取以及输出原始数据到目标媒介： 数据源--> 程序 --> 目标媒介**

**1.2、一个程序需要InputStream或者Reader从数据源读取数据，需要 OutputStream或者Writer将数据写入到目标媒介中;**

InputStream和Reader与数据源相关联，OutputStream和Writer与目标媒介相关联

**1.3、Java IO操作类都在包 java.io 下，大概有将近 80 个类，但是这些类大概可以分成四组，分别如下：**

- 基于字节操作的 I/O 接口：InputStream 和 OutputStream
- 基于字符操作的 I/O 接口：Writer 和 Reader
- 基于磁盘操作的 I/O 接口：File
- 基于网络操作的 I/O 接口：Socket

前两组主要是根据传输数据的数据格式，后两组主要是根据传输数据的方式I/O 的核心问题要么是数据格式影响 I/O 操作，要么是传输方式影响 I/O 操作

==> "数据格式"和"传输方式"是影响效率最关键的因素了

## 2、文件-一种常用的数据源或者存储数据的媒介
**2.1、读文件：**

- 如果你需要在不同端之间读取文件，你可以根据该文件是二进制文件还是文本文件来选择使用 FileInputStream 或者 FileReader;
- 如果你需要跳跃式地读取文件其中的某些部分，可以使用 RandomAccessFile

**2.2、写文件：**

如果你需要在不同端之间进行文件的写入，你可以根据你要写入的数据是二进制型数据还是字符型数据选用FileOutputStream 或者 FileWriter

**2.3、随机存取文件：通过 RandomAccessFile 对文件进行随机存取**

随机存取并不意味着你可以在真正随机的位置进行读写操作，它只是意味着你可以跳过文件中某些部分进行操作，并且支持同时读写，不要求特定的存取顺序

**2.4、文件和目录信息的获取：**

通过 File 类可以获取文件和目录的信息

## 3、管道

为运行在同一个JVM中的两个线程提供了通信的能力，所以管道也可以作为"数据源以及目标媒介"

**3.1、不能利用管道与不同的JVM中的线程通信(不同的进程)，其与 Unix/Linux 系统的管道不一致**

**3.2、创建管道：**
    
- 通过Java IO 中的PipedOutputStream和PipedInputStream创建管道
- 一个PipedInputStream流应该和一个PipedOutputStream流相关联.
- 一个线程通过PipedOutputStream写入的数据可以被另一个线程通过相关联的PipedInputStream读取出来

**3.3、例子：可以使用两个管道共有的connect()方法使之相关联**
```java
public class PipedExample {
    public static void main(String[] args) throws IOException {
        final PipedOutputStream out = new PipedOutputStream();
        final PipedInputStream in = new PipedInputStream(out);
//		in.connect(out); 可以使用两个管道共有的connect()方法使之相关联
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                try {
                    out.write("Hello，piped output".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                try {
                    int data = in.read();
                    while(data != -1){
                        System.out.println((char)data);
                        data = in.read();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();
        thread2.start();
        out.close();
        in.close();
    }
}
```
**3.4、管道与线程：**

当使用两个相关联的管道流时，务必将它们分配给不同的线程.read()方法和write()方法调用时会导致流阻塞，这意味着如果你尝试在一个线程中同时进行读和写，可能会导致线程死锁;

## 4、网络
**4.1、当两个进程之间建立了网络连接之后，他们通信的方式如同操作文件一样：**

利用 InputStream 读取数据，利用 OutputStream 写入数据，Java 网络 API 用来在不同进程之间建立网络连接，而 Java IO 则用来在建立了连接之后的进程之间交换数据

## 5、字节和字符数组
**5.1、从InputStream或者Reader中读入数组**

用 ByteArrayInputStream或者CharArrayReader封装字节或者字符数组从数组中读取数据：

**5.2、从OutputStream或者Writer中写数组：**

把数据写到ByteArrayOutputStream或者CharArrayWriter 中

**5.3、基于字节流的操作层次结构**
```
InputStream：
    ByteArrayInputStream
    FileInputStream 
        SocketInputStream
    FilterInputStream
        InflaterInputStream <- ZipInputStream
        BufferedInputStream
        DataInputStream
    ObjectInputStream
    PipedInputStream
OutputStream：与上述 InputStream 类似
```
**5.4、Writer/Reader相关类层次结构**
```
Writer：
    OutputStreamWriter
        FileWriter
    BufferedWriter
    StringWriter
    PipedWriter
    PrintWriter
    CharArrayWriter
Reader：类似 Writer
```
**5.5、InputStreamReader-字节流与字符流转换接口**
   
是字节到字符的转化桥梁，InputStream到Reader的过程要指定编码字符集，否则将采用操作系统默认字符集，很可能会出现乱码问题.StreamDecoder正是完成字节到字符的解码的实现类

## 6、System.in、System.out、System.err：标准输出
**6.1、JVM 启动的时候通过Java运行时初始化这3个流，所以你不需要初始化它们**

**6.2、System.in：**

一个典型的连接控制台程序和键盘输入的 InputStream 流

**6.3、System.out：**

System.out 是一个 PrintStream 流.System.out一般会把你写到其中的数据输出到控制台上

**6.4、System.err：**

System.err 是一个 PrintStream 流.System.err 与 System.out 的运行方式类似，但它更多的是用于打印错误文本
   
**6.5、替换系统流：**

System.in、System.out、System.err这3个流是java.lang.System类中的静态成员，并且已经预先在JVM启动的时候初始化完成，你依然可以更改它们；要把一个新的InputStream设置给System.in或者一个新的OutputStream设置给System.out或者System.err；可以使用 System.setIn()， System.setOut()， System.setErr()方法设置新的系统流

注意：请记住，务必在JVM关闭之前冲刷System.out(译者注：调用flush())，确保System.out把数据输出到了文件中

## 7、流-仅仅只是一个连续的数据流

**7.1、流和数组不一样，不能通过索引读写数据.在流中，你也不能像数组那样前后移动读取数据，<br> 除非使用 RandomAccessFile 处理文件.**

**7.2、InputStream：java.io.InputStream 类是所有 Java IO 输入流的基类**

- 如果你需要将读过的数据推回到流中，你必须使用 PushbackInputStream，这意味着你的流变量只能是这个类型，否则在代码中就不能调用 PushbackInputStream的unread()方法；
- 通常使用输入流中的read()方法读取数据.read()方法返回一个整数，代表了读取到的字节的内容，当达到流末尾没有更多数据可以读取的时候，read()方法返回-1<br>
    注意：InputStream的read()方法返回一个字节，意味着这个返回值的范围在0到255之间

**7.3、OutputStream：java.io.OutputStream 是 Java IO 中所有输出流的基类**

**7.4、组合流：将流整合起来以便实现更高级的输入和输出操作**

InputStream input = new BufferedInputStream(new FileInputStream("c：\\data\\input-file.txt"));

## 8、Reader、Writer：
**8.1、Reader类：是Java IO 中所有Reader的基类**

- 子类包括BufferedReader，PushbackReader，InputStreamReader，StringReader和其他Reader
- Reader的read()方法返回一个字符，意味着这个返回值的范围在0到65535之间
- 整合 Reader 与 InputStream：<br>
如果你有一个InputStream输入流，并且想从其中读取字符，可以把这个InputStream包装到InputStreamReader中Reader reader = new InputStreamReader(inputStream);在构造函数中可以指定解码方式
    
**8.2、Writer类：是Java IO中所有Writer的基类，子类包括BufferedWriter和PrintWriter等等**

- 整合 Writer 和 OutputStream：
一个Writer可以和一个 OutputStream 相结合。把OutputStream包装到OutputStreamWriter中，所有写入到OutputStreamWriter的字符都将会传递给OutputStream；<br>
Writer writer = new OutputStreamWriter(outputStream);

**8.3、整合Reader和Writer**

以通过将 Reader 包装到 BufferedReader、Writer 包装到 BufferedWriter 中实现缓冲

# 三、Java NIO
## 1.Java NIO 概述
**1.1.核心概念：**

- Channels
- Buffers
- Selectors

**1.2.Channel 和 Buffer：**

- 基本上，所有的IO在NIO中都从一个Channel开始，数据可以从Channel读到Buffer中，也可以从Buffer写到Channel中
- Channel的几种实现：<br>
	FileChannel<br>
	DatagramChannel<br>
	SocketChannel<br>
	ServerSocketChannel
- Buffer的实现：这些Buffer覆盖了你能通过IO发送的基本数据类型：byte、short、int、long、float、double和char<br>
	ByteBuffer<br>
	CharBuffer<br>
	DoubleBuffer<br>
	FloatBuffer<br>
	IntBuffer<br>
	LongBuffer<br>
	ShortBuffer<br>
	MappedByteBuffer<br>

**1.3.Selector：Selector 允许单线程处理多个Channel**

要使用Selector得向Selector注册Channel，然后调用它的select()方法个方法会一直阻塞到某个注册的通道有事件就绪。一旦这个方法返回，线程就可以处理这些事件，事件的例子有如新连接进来，数据接收；

## 2.Channel

用于源节点与目标节点的连接.在 Java NIO 中负责缓冲区中数据的传输，本身不存储数据

**2.1.与流类似，但有所不同：不能直接访问数据，可以与 Buffer 进行交互.**

- 既可以从通道中读取数据，又可以写数据到通道.但流的读写通常是单向的
- 通道可以异步地读写;
- 通道中的数据总是要先读到一个 Buffer，或者总是要从一个 Buffer 中写入;

**2.2.Java NIO 中最重要的通道的实现**

- FileChannel：从文件中读写数据，一般从流中获取 Channel，不能切换成非阻塞模式
- DatagramChannel：能通过UDP读写网络中的数据.
- SocketChannel：能通过TCP读写网络中的数据.
- ServerSocketChannel：可以监听新进来的TCP连接，像Web服务器那样.对每一个新进来的连接都会创建一个 SocketChannel

**2.3.通道的获取：**

- Java 针对支持通道的类都提供了 getChannel() 的方法;<br>
	输入输出流<br>
	网络IO<br>
- 在JDK7中NIO2.0 针对各个通道提供了静态的方法 open()
- 在JDK7中NIO2.0 的 Files 工具类的 newByteChannel()

**2.4、通道之间的数据传输：**

	transferFrom()
	transferTo()

**2.5、字符集：**
```
(1).编码：字符串 --> 字节数组
(2).解码：字节数组 --> 字符串
	查看支持的字符集：
	Charset.availableCharsets();
A.定义字符集：Charset cs = Charset.forName("UTF-8");
B.获取编码器：
	CharsetEncoder ce = cs.newEncoder();
	ByteBuffer bf = ce.encode(CharBuffer)
C.获取解码器：
	CharsetDecoder cd = cs.newDecoder();
	CharBuffer cf = cd.decode(ByteBuffer);
```
## 3、Buffer

用于和NIO通道进行交互，缓冲区本质上是一块可以写入数据，然后可以从中读取数据的内存

方法名称 |方法描述
--------|-------
Buffer clear()	|清空缓冲区并返回对缓冲区的引用，但是数据还存在
Buffer flip()	|将缓冲区的界限设置为当前位置，并将当前位置充值为0
int capacity()	|返回Buffer 的capacity大小
boolean hasRemaining()|判断缓冲区中是否还有元素
int limit()	|返回Buffer 的界限(limit) 的位置
Buffer.limit(int n)|将设置缓冲区界限为n， 并返回一个具有新limit的缓冲区对象
Buffer mark()|对缓冲区设置标记
int position()|返回缓冲区的当前位置position
Buffer position(int n)|将设置缓冲区的当前位置为n，并返回修改后的Buffer对象
int remaining()	|返回position 和limit 之间的元素个数
Buffer reset()|将位置position 转到以前设置的mark 所在的位置
Buffer rewind()	|将位置设为为0，取消设置的mark

**3.1、Buffer 的基本用法：**

获取缓冲区的方法：(例子)ByteBuffer.allocate()

- 使用Buffer读写数据一般遵循以下四个步骤<br>
	①、写入数据到 Buffer<br>
	②、调用flip()方法<br>
	③、从 Buffer 中读取数据<br>
	④、调用clear()方法或者compact()方法

- 当向buffer写入数据时，buffer会记录下写了多少数据。一旦要读取数据，需要通过flip()方法将Buffer从写模式切换到读模式;在读模式下，可以读取之前写入到buffer的所有数据；

- 一旦读完了所有的数据，就需要清空缓冲区，让它可以再次被写入。有两种方式能清空缓冲区：调用clear()或compact()方法。clear()方法会清空整个缓冲区。compact()方法只会清除已经读过的数据。任何未读的数据都被移到缓冲区的起始处，新写入的数据将放到缓冲区未读数据的后面；

- Buffer 的capacity，position和limit： 关键属性
	- （1）缓冲区本质上是一块可以写入数据，然后可以从中读取数据的内存.<br>
		这块内存被包装成 NIO Buffer 对象，并提供了一组方法，用来方便的访问该块内存
	- （2）Buffer 对象的四个属性：<br>
		capacity<br>
		position<br>
		limit<br>
		mark<br>
		position和limit的含义取决于 Buffer 处在读模式还是写模式。不管Buffer处在什么模式，capacity的含义总是一样的<br>
	- （3）capacity：最大存储数据的容量，一旦声明不能改变；作为一个内存块，Buffer 有一个固定的大小值，也叫"capacity".你只能往里写capacity个 byte、long，char 等类型；一旦Buffer满了，需要将其清空(通过读数据或者清除数据)才能继续写数据往里写数据<br>
	- （4）position：<br>
		①、当你写数据到 Buffer 中时，position表示当前的位置，初始的position值为0，当一个 byte、long 等数据写到 Buffer 后，position会向前移动到下一个可插入数据的 Buffer 单元.position最大可为capacity – 1<br>
		②、当读取数据时，也是从某个特定位置读.当将Buffer从写模式切换到读模式，position会被重置为0. 当从 Buffer的position处读取数据时，position向前移动到下一个可读的位置
	- （5）limit：<br>
		①.在写模式下，Buffer 的limit表示你最多能往Buffer里写多少数据.写模式下，limit等于Buffer的capacity<br>
		②.当切换Buffer到读模式时， limit表示你最多能读到多少数据，当切换Buffer到读模式时，limit会被设置成写模式下的position值<br>
	- （6）mark：标记，表示记录当前position的位置，可以通过reset() 恢复到 mark 的位置;

**3.3、Buffer 的分配：**

每一个 Buffer 类都有一个allocate方法。下面是一个分配48字节capacity的 ByteBuffer 的例子<br>
ByteBuffer buf = ByteBuffer.allocate(48);

**3.4、向 Buffer 中写数据：**

- 从 Channel 写到 Buffer <br>
	int bytesRead = inChannel.read(buf); //read into buffer
- 通过 Buffer 的put()方法写到 Buffer 里：buf.put(127);

**3.5、flip()方法：**

flip方法将 Buffer 从写模式切换到读模式.调用flip()方法会将position设回0，并将limit设置成之前position的值，即position现在用于标记读的位置，limit表示之前写进了多少个 byte、char 等 —— 现在能读取多少个 byte、char 等

**3.6、从 Buffer 中读取数据：**

- 从 Buffer 读取数据到 Channel：int bytesWritten = inChannel.write(buf);
- 使用get()方法从 Buffer 中读取数据 byte aByte = buf.get();

**3.7、rewind()方法**

Buffer.rewind()将position设回0，所以你可以重读Buffer中的所有数据。limit保持不变，仍然表示能从Buffer中读取多少个元素(byte、char等)
	
**3.8、clear()与compact()方法：**

- 一旦读完Buffer中的数据，需要让Buffer准备好再次被写入.可以通过clear()或compact()方法来完成
- 如果调用的是clear()方法，position将被设回0，limit被设置成 capacity的值.换句话说，Buffer 被清空了。Buffer 中的数据并未清除，只是这些标记告诉我们可以从哪里开始往 Buffer 里写数据
- 如果Buffer中有一些未读的数据，调用clear()方法，数据将“被遗忘”，意味着不再有任何标记会告诉你哪些数据被读过，哪些还没有;
- 如果 Buffer 中仍有未读的数据，且后续还需要这些数据，但是此时想要先先写些数据，那么使用compact()方法
- compact()方法将所有未读的数据拷贝到Buffer起始处.然后将position设到最后一个未读元素正后面.limit属性依然像clear()方法一样，设置成capacity.现在Buffer准备好写数据了，但是不会覆盖未读的数据

**3.9、mark()与reset()方法**

通过调用 Buffer.mark()方法，可以标记 Buffer 中的一个特定position。之后可以通过调用 Buffer.reset()方法恢复到这个position

**3.10、equals()与compareTo()方法：**

- equals()，当满足下列条件时，表示两个Buffer相等：<br>
	有相同的类型（byte、char、int等）.<br>
	Buffer 中剩余的 byte、char 等的个数相等.<br>
	Buffer 中所有剩余的 byte、char 等都相同<br>
	==> 实际上，它只比较Buffer中的剩余元素(剩余元素是从 position到limit之间的元素)<br>
- compareTo()方法：<br>
	compareTo()方法比较两个Buffer的剩余元素(byte、char等)， 如果满足下列条件，则认为一个Buffer"小于"另一个Buffer：<br>
	第一个不相等的元素小于另一个Buffer中对应的元素；所有元素都相等，但第一个Buffer比另一个先耗尽(第一个Buffer的元素个数比另一个少)

**3.11、直接缓冲区与非直接缓冲区：**

- 直接缓冲区是通过调用此类的 allocateDirect()工厂方法来创建的，此方法返回的缓冲区进行分配取消分配所需成本通常要高于非直接缓冲区。<br>
	建议将直接缓冲区主要分配给那些易受基础系统的本机I/O 操作影响的大型、持久的缓冲区。<br>
	一般情况下仅在直接缓冲区能带来明显好处时分配它们<br>
- 直接缓冲区还可以通过 FileChannel 的map() 方法将文件区域直接映射到内存中来创建.该方法返回 MappedByteBuffer
- 判断是直接缓冲区还是间接缓冲区，可以通过调用 isDirect() 方法来确定.

## 4.Scatter/Gather

**4.1、Scatter/Gather 用于描述从 Channel 中读取或者写入到 Channel 的操作：**

- Scatter(分散)从 Channel 中读取是指在读操作时将读取的数据写入多个 Buffer 中，Channel 将从 Channel 中读取的数据"分散(scatter)"到多个 Buffer 中
- Gather(聚集)写入 Channel 是指在写操作时将多个 Buffer 的数据写入同一个 Channel，Channel 将多个 Buffer 中的数据"聚集(gather)"后发送到 Channel
- Scatter/Gather 经常用于需要将传输的数据分开处理的场合

**4.2、Scattering Reads：是指数据从一个channel读取到多个buffer中**

(1).代码示例：
```java
ByteBuffer header = ByteBuffer.allocate(128);
ByteBuffer body   = ByteBuffer.allocate(1024);
// 注意buffer首先被插入到数组，然后再将数组作为channel.read() 的输入参数
ByteBuffer[] bufferArray = { header， body };
channel.read(bufferArray);
//read()方法按照buffer在数组中的顺序将从channel中读取的数据写入到buffer，
//当一个buffer被写满后，channel紧接着向另一个buffer中写
```
注意：Scattering Reads 在移动下一个 Buffer 前，必须填满当前的buffer，这也意味着它不适用于动态消息

**4.3、Gathering Writes：是指数据从多个buffer写入到同一个channel**
```java
ByteBuffer header = ByteBuffer.allocate(128);
ByteBuffer body   = ByteBuffer.allocate(1024);
ByteBuffer[] bufferArray = { header， body };
channel.write(bufferArray);
// buffers数组是write()方法的入参，write()方法会按照buffer在数组中的顺序，将数据写入到channel，
// 注意只有position和limit之间的数据才会被写入
```

## 5.通道之间的数据传输

在 Java NIO 中，如果两个通道中有一个是FileChannel，那你可以直接将数据从一个channel传输到另外一个channel

- transferFrom()：数据从源通道传输到 FileChannel 中<br>
	注意：在SoketChannel的实现中，SocketChannel 只会传输此刻准备好的数据
```java
RandomAccessFile fromFile = new RandomAccessFile("fromFile.txt"， "rw");
FileChannel      fromChannel = fromFile.getChannel();
RandomAccessFile toFile = new RandomAccessFile("toFile.txt"， "rw");
FileChannel      toChannel = toFile.getChannel();
long position = 0;
long count = fromChannel.size();
/**
 * 从position处开始向目标文件写入数据
 * count表示最多传输的字节数
 * 如果源通道的剩余空间小于 count 个字节，则所传输的字节数要小于请求的字节数
 */
toChannel.transferFrom(position， count， fromChannel);
```
- transferTo()：将数据从FileChannel传输到其他的channel中

## 6.Selector

是 Java NIO 中能够检测一到多个 NIO 通道，并能够知晓通道是否为诸如读写事件做好准备的组件，这样一个单独的线程可以管理多个channel，从而管理多个网络连接

**6.1、为什么使用 Selector?**

可以只用一个线程处理所有的通道，使用Selector能够处理多个通道;

**6.2、.Selector 的创建**

	Selector selector = Selector.open();

**6.3、向 Selector 注册通道：为了将Channel和Selector配合使用，必须将channel注册到selector上**

通过 SelectableChannel.register()方法来实现
```java
/**
 * 与Selector一起使用时，Channel必须处于非阻塞模式下
 * 这意味着不能将FileChannel与Selector一起使用，因为FileChannel不能切换到非阻塞模式.而套接字通道都可以
*/
channel.configureBlocking(false);
SelectionKey key = channel.register(selector， Selectionkey.OP_READ);
```
- register()方法的第二个参数：是在通过Selector监听Channel时对什么事件感兴趣，可以监听四种不同类型的事件：<br>
	Connect == SelectionKey.OP_CONNECT<br>
	Accept  == SelectionKey.OP_ACCEPT<br>
	Read    == SelectionKey.OP_READ<br>
	Writer  == SelectionKey.OP_WRITE<br>
	如果你对不止一种事件感兴趣，那么可以用“位或”操作符将常量连接起来<br>
	int interestSet = SelectionKey.OP_READ | SelectionKey.OP_WRITE;

**6.4、SelectionKey**
- interest集合：你所选择的感兴趣的事件集合
```java
int interestSet = selectionKey.interestOps();
//用“位与”操作interest 集合和给定的SelectionKey常量，可以确定某个确定的事件是否在interest 集合中
boolean isInterestedInAccept  = (interestSet & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT；
boolean isInterestedInConnect = interestSet & SelectionKey.OP_CONNECT;
boolean isInterestedInRead    = interestSet & SelectionKey.OP_READ;
boolean isInterestedInWrite   = interestSet & SelectionKey.OP_WRITE;
```
- ready集合是通道已经准备就绪的操作的集合<br>
	在一次选择(Selection)之后，你会首先访问这个ready set<br>
	int readySet = selectionKey.readyOps();<br>
	检测channel中什么事件或操作已经就绪<br>
	selectionKey.isAcceptable();<br>
	selectionKey.isConnectable();<br>
	selectionKey.isReadable();<br>
	selectionKey.isWritable();<br>
- Channel + Selector：从SelectionKey访问Channel和Selector很简单<br>
	Channel  channel  = selectionKey.channel();<br>
	Selector selector = selectionKey.selector();<br>
- 附加的对象：可以将一个对象或者更多信息附着到SelectionKey上，这样就能方便的识别某个给定的通道<br>
		selectionKey.attach(theObject);<br>
		Object attachedObj = selectionKey.attachment();<br>
	还可以在用register()方法向Selector注册Channel的时候附加对象<br>
		SelectionKey key = channel.register(selector， SelectionKey.OP_READ， theObject);

**6.5、通过 Selector 选择通道**

# 参考文章
* [Java-NIO系列](http：//ifeve.com/java-nio-all/)
* [浅析I/O模型](http：//www.cnblogs.com/dolphin0520/p/3916526.html)