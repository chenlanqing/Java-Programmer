<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、IO通信](#%E4%B8%80io%E9%80%9A%E4%BF%A1)
  - [1、IO](#1io)
    - [1.1、LinuxIO模型](#11linuxio%E6%A8%A1%E5%9E%8B)
    - [1.2、IO多路复用技术](#12io%E5%A4%9A%E8%B7%AF%E5%A4%8D%E7%94%A8%E6%8A%80%E6%9C%AF)
  - [2、Java中IO](#2java%E4%B8%ADio)
- [二、Netty](#%E4%BA%8Cnetty)
  - [1、不建议使用原生NIO类库进行开发的原因](#1%E4%B8%8D%E5%BB%BA%E8%AE%AE%E4%BD%BF%E7%94%A8%E5%8E%9F%E7%94%9Fnio%E7%B1%BB%E5%BA%93%E8%BF%9B%E8%A1%8C%E5%BC%80%E5%8F%91%E7%9A%84%E5%8E%9F%E5%9B%A0)
  - [2、Netty 的特点](#2netty-%E7%9A%84%E7%89%B9%E7%82%B9)
  - [3、粘包和拆包](#3%E7%B2%98%E5%8C%85%E5%92%8C%E6%8B%86%E5%8C%85)
    - [3.1、TCP粘包/拆包问题](#31tcp%E7%B2%98%E5%8C%85%E6%8B%86%E5%8C%85%E9%97%AE%E9%A2%98)
    - [3.2、TCP粘包拆包问题说明](#32tcp%E7%B2%98%E5%8C%85%E6%8B%86%E5%8C%85%E9%97%AE%E9%A2%98%E8%AF%B4%E6%98%8E)
    - [3.3、TCP 粘包和拆包发生的原因](#33tcp-%E7%B2%98%E5%8C%85%E5%92%8C%E6%8B%86%E5%8C%85%E5%8F%91%E7%94%9F%E7%9A%84%E5%8E%9F%E5%9B%A0)
    - [3.4、粘包解决策略](#34%E7%B2%98%E5%8C%85%E8%A7%A3%E5%86%B3%E7%AD%96%E7%95%A5)
    - [3.5、Netty 解决粘包和拆包](#35netty-%E8%A7%A3%E5%86%B3%E7%B2%98%E5%8C%85%E5%92%8C%E6%8B%86%E5%8C%85)
- [三、Netty服务端启动过程](#%E4%B8%89netty%E6%9C%8D%E5%8A%A1%E7%AB%AF%E5%90%AF%E5%8A%A8%E8%BF%87%E7%A8%8B)
  - [1、创建服务端channel](#1%E5%88%9B%E5%BB%BA%E6%9C%8D%E5%8A%A1%E7%AB%AFchannel)
  - [2、初始化服务端Channel](#2%E5%88%9D%E5%A7%8B%E5%8C%96%E6%9C%8D%E5%8A%A1%E7%AB%AFchannel)
  - [3、注册Selector](#3%E6%B3%A8%E5%86%8Cselector)
  - [4、端口绑定](#4%E7%AB%AF%E5%8F%A3%E7%BB%91%E5%AE%9A)
- [四、NioEventLoop](#%E5%9B%9Bnioeventloop)
  - [1、NioEventLoop 创建](#1nioeventloop-%E5%88%9B%E5%BB%BA)
  - [2、NioEventLoop 启动](#2nioeventloop-%E5%90%AF%E5%8A%A8)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 一、IO通信
## 1、IO
### 1.1、LinuxIO模型

- 阻塞IO模型：最常用的IO模型缺省情况下，所有文件操作都是阻塞的；
- 非阻塞IO模型；
- IO复用模型：Linux提供select/poll
- 信号驱动IO模型
- 异步IO

### 1.2、IO多路复用技术

- IO多路复用技术通过把多个IO阻塞复用到同一个select的阻塞上，从而使得系统在单线程的清下可以同时处理多个客户端请求；
- 与传统的多线程/多进程模型比，IO多路复用的最大优势是系统开销小，系统不需要创建新的额外进程或者线程，也不需要维护这些进程和线程的运行，降低了系统维护的工作量。
- IO多路复用技术应用场景：
	- 服务器需要同时处理多个处于监听状态或者多个连接状态的套接字；
	- 服务器需要同时处理多种网络协议的套接字；


**对于操作系统而言，底层是支持异步IO通信。**

## 2、Java中IO

- **2.1、BIO：**

	- 采用BIO通信模型的服务端，由一个独立的 Acceptor现场负责监听客户端连接，它接收到客户端连接请求之后为每个客户端创建一个新的线程进行链路处理，处理完成后，通过输出流返回给应答给客户端，线程销毁.这是典型的一请求一应答通信模型。

	- 该模型最大的问题是缺乏弹性伸缩能力，当客户端并发访问量增加后，服务端的线程个数和客户端并发访问数呈1：1的正比关系.由于线程是Java虚拟机的非常宝贵的系统资源，当线程数膨胀后，系统的性能急剧下降，系统可能会发生线程堆栈溢出，创建线程失败等。

- **2.2、伪异步IO：采用线程池和任务队列可以实现伪异步IO**

	- 当有新的客户端接入时，将客户端的Socket封装成一个Task（其实现Runnable接口）传递给后端的线程池中处理，JDK的线程池维护一个消息队列和N个活跃线程，对消息队列中的任务进行处理。

	- 由于线程池和消息队列都是有界的，因此，无论客户端并发连接数多大，它都不会导致线程个数过于膨胀或者内存溢出；

	- 但是由于其底层通信依然采用同步阻塞模型，无法从根本上解决问题

- **2.3、NIO-非阻塞IO**
	
	- jdk1.4引入NIO,弥补了原来阻塞IO的不足，具体参考[Java-NIO](https://github.com/chenlanqing/learningNote/blob/master/Java/JavaSE/IO%E4%B8%8ENIO.md#%E4%B8%89java-nio)

	- NIO相对其他IO来说，优点：
		- 客户端发起的连接操作是异步的，可以通过在多路复用器注册OP_CONNECT等待后续结果，不需要像之前的客户端那样被同步阻塞；
		- SocketChannel 的读写操作都是异步的，如果没有可读写的数据它不会同步等待，直接返回，这样I/O通信线程就可以处理其他链路；
		- 线程模型的优化；

- **2.4、AIO-异步IO**

- **2.5、不同IO对比**

	|  对比参数|同步IO（BIO）|伪异步IO|NIO|AIO|
	|-------|----|----|--------|--------|
	|客户端个数|1：1|M:N（其中M可以大于N）|M：1（1个IO线程处理多个客户端连接）|M：0（不需要启动额外的IO线程，被动回调）|
	|IO类型（阻塞）|阻塞IO|阻塞IO|非阻塞IO|非阻塞IO|
	|IO类型（同步）|同步IO|同步IO|同步IO（IO多路复用）|异步IO|
	|API难度|简单|简单|非常复杂|复杂|
	|调试难度|简单|简单|复杂|复杂|
	|可靠性|非常差|差|高|高|
	|吞吐量|低|中|高|高|

# 二、Netty

## 1、不建议使用原生NIO类库进行开发的原因

- NIO 类库和API繁杂，使用麻烦，你需要熟练掌握 Selector、ServerSocketChannel、SocketChannel、ByteBuffer等；
- 需要具备其他的额外技能做铺垫，例如熟悉Java多线程编程.这时因为NIO编程设计到Reactor模式，你必须对多线程和网络编程非常熟悉，才能编写出高质量的NIO程序；
- 可靠性能力补齐，工作量和难度非常大.例如客户端面临断连重连、网络闪断、半包读写、失败缓存、网络拥塞和异常码流的处理问题.NIO 编程的特点是功能开发相对容易、但是可靠性能力补齐的工作量和难度都非常大；
- JDK NIO的bug，如臭名昭著的 epoll bug会导致Selector空轮询，最终导致CPU 100%；

## 2、Netty 的特点

- API 使用简单，开发门槛低；
- 功能强大，预置了多种编解码功能，支持多种主流协议；
- 定制能力强，可以通过 ChannelHandler 对通信框架进行灵活扩展；
- 性能高，通过与其他业界主流的NIO框架对比，Netty 的综合性能最优；
- 成熟，稳定，Netty 修复了发现了JDK NIO BUG；
- 社区活跃，版本迭代周期短，发现的bug可以即使修复
- 经历了大规模的商业应用考虑.

## 3、粘包和拆包

### 3.1、TCP粘包/拆包问题

TCP 是个流的协议，是连成一片的，其间没有分界线，TCP 底层并不了解业务数据的具体含义，它会根据 TCP 缓冲区的实际情况进行包的划分，所以在业务上认为，一个完整可能会被TCP拆分成多个包进行发送，也有可能把多个小的包封装成一个大的数据包发送.就是所谓的TCP粘包和拆包问题

### 3.2、TCP粘包拆包问题说明

假设客户端分别发送了两个独立的数据包D1和D2给服务器，由于服务端一次读取到的字节数是不确定的，故可能存在以下4中情况：
- （1）服务端分两次收到了两个独立的数据包，分别是D1和D2，没有粘包和拆包；
- （2）服务端一次收到了两个数据包，D1和D2粘合在一起，被称为TCP粘包；
- （3）服务端分两个读取到了两个数据包，第一次读取到了完整的D1包和D2包的部分内容，第二次读取到了D2包的剩余内容，被称为TCP拆包；
- （4）服务端分两次读取到了两个数据包，第一次读取到了D1包的部分内容D1_1，第二次读取到了D1包的剩余内容D1_2和D2包的整包

### 3.3、TCP 粘包和拆包发生的原因

- 应用程序 write 写入的字节大小大于套接口发送的缓冲区大小；
- 进行 MSS 大小的TCP分段；
- 以太网帧 payload 大于 MTU 进行 IP 分片；

### 3.4、粘包解决策略

由于底层的TCP无法理解上层的业务数据，所以在底层是无法保证数据不被拆分和重组的.这个问题只能通过上层的应用协议栈设计来解决

- 消息定长，例如每个报文的大小固定长度200字节，如果不够，空位补空格；
- 在包尾增加回车换行符进行分割，如FTP协议
- 将消息分为消息头和消息体，消息头中包含表示消息总长度的字段，通常设计思路为消息头的第一个字段使用int32来表示消息的总长度。

### 3.5、Netty 解决粘包和拆包

**1、LineBasedFrameDecoder**

- LineBasedFrameDecoder：依次遍历 ByteBuf 中可读的字节，判断是否有"\n" 或者 "\r\n"，如果有就以此位置为结束位置，从可读索引到结束为止取件的字节就组成了一行。它是以换行符为结束标志的解码器，支持携带结束符或者不携带结束符两种解码方式，同时支持配置单行的最大长度。如果读取到最大长度后仍然没有发现换行符，就会抛出异常，同时忽略到之前督导的异常码流；

- StringDecoder：将接收到的对象转换成字符串，然后继续调用后面的handler

LineBasedFrameDecoder + StringDecoder组合就是按行切换的文本解码器，它被设计用来支持TCP的粘包和拆包；

**2、DelimiterBasedFrameDecoder**

可以自动完成以分隔符做结束标志的消息的解码

**3、FixedLengthFrameDecoder**

可以自动完成对定长消息的解码

## 4、编解码技术

### 4.1、Java序列化缺点
- 无法跨语言，如RPC框架，RPC框架是需要跨语言调用的，几乎所有流行的RPC框架都没有使用Java序列化；
- 序列化后码流太大；
- 序列化性能太低

### 4.2、主流与编解码框架
- Google Protobuf
	- 产品成熟度高；
	- 跨语言、支持多种语言，包括C++、Java和python等；
	- 编码后的消息更小，更加有利于存储和传输；
	- 编解码的性能非常高；
	- 支持不同协议版本的前向兼容；
	- 支付定义可选和必选字段；
- Facebook Thrift
- JBoss Marshalling

# 三、Netty服务端启动过程

两个问题：
- 服务端的Socket在哪里初始化？
- 在哪里accept连接？

Netty服务端启动过程：
- （1）、创建服务端channel
- （2）、初始化服务端channel
- （3）、注册Selector；
- （4）、端口绑定

**服务端创建详细步骤**
- （1）创建ServerBootstrap实例：ServerBootstrap是Netty服务器的启动辅助类，它提供了一系列的方法用于服务端启动相关的参数；该类使用了Builder模式
- （2）设置并绑定Reactor线程池：Netty的Reactor线程池是EventLoopGroup，它实际就是EventLoop数组。

## 1、创建服务端channel

- bind()-用户代码入口
	- initAndRegister()-初始化并注册
		- newChannel()-创建服务端channel

反射创建服务端channel
- newSocket()-通过jdk来创建底层jdk channel
- NioServerSocketChannelConfig()-tcp参数配置类
- AbstractChannel()
	- configureBlocking(false) -配置阻塞模式
	- AbstractChannel()-创建id，unsage，pipeline

## 2、初始化服务端Channel

- bind()-代码入口
	- initAndRegister()-初始化并注册
		- newChannel()-创建服务端channel
		- init()-初始化服务端channel
			- set ChannelOptions，ChannelAttrs 
			- set ChildOptions，ChildAttrs
			- config handler-配置服务端pipeline
			- addSeverBootStrapAcceptor-添加连接器

## 3、注册Selector

- AbstractChannel.register(channel)-入口
	- this.eventLoop=eventLoop -绑定线程
	- register() 实际注册
		- doRegister() 调用jdk底层注册
		- invokeHandlerAddedIfNeeded()
		- fireChannelRegistered() 传播事件


## 4、端口绑定

- AbstractUnsafe.bind() 入口
	- doBind()
		- javaChannel.bind() jdk底层绑定
	- pipeline.fireChannleActive() 传播事件
		- HeadContext.readIfIsAutoRead()

# 四、NioEventLoop

三个问题：
- 默认情况下，Netty服务端起多少线程？何时启动？

	默认情况下，是2倍CPU核数

- Netty如何解决jdk空轮询bug的？-空轮询次数：512


- Netty如何保证异步串行无锁化？

## 1、NioEventLoop 创建
- **基本流程：**
	- new NioEventLoopGroup()线程组，默认是2*CPU核数
		- new ThreadPerTaskExecutor() 线程创建器
		- for(){newChild()}-构造NioEventLoop
		- chooserFactory.newChooser()线程选择器


- **ThreadPerTaskExecutor**

	- 每次执行任务都会创建一个线程实体
	- NioEventLoop 线程命名规则 nioEventLoop-1-XX

- **newChild**

## 2、NioEventLoop 启动


# 五、Pipeline
## 1、概述

## 2、初始化
- pipeline在创建Channel的时候被创建
- Pipeline节点的数据结构：ChannelHandlerContext；
- Pipeline中两大节点：head和tail，这两个节点是不可被删除的

## 3、添加ChannelHanndler
- 3.1、主要步骤
	- 判断是否重复添加；
	- 创建节点并添加至链表；
	- 回调添加完成事件

## 4、删除ChannelHandler
主要使用场景：权限校验
- 找到节点，主要是遍历链表
- 链表的删除（默认情况下pipeline都有head和tail节点，不必担心被删除的handler是否头结点或者尾节点）
- 回调删除handler事件

## 5、inBound事件的传播
### 5.1、什么是inBound事件以及ChannelInboundHandler

### 5.2、ChannelRead事件传播

# Netty相关问题
- **1、服务端的Socket在哪里初始化？**

- **2、在哪里accept连接？**

- **3、默认情况下，Netty服务端起多少线程？何时启动？**

- **4、Netty如何解决jdk空轮询bug的？-空轮询次数：512**

- **5、Netty如何保证异步串行无锁化？**

- **6、Netty是在哪里检测有新连接接入的？**

- **7、新连接是怎样注册到NioEventLoop线程的**

- **8、Netty是如何判断ChannelHandler类型的？**

- **9、对于ChannelHandler的添加应该遵循什么样的顺序？**

- **10、用户手动触发事件传播，不同的触发方式有什么样的区别？**

- **11、**

- **12、**

- **13、**

- **14、**

- **15、**

- **16、**
