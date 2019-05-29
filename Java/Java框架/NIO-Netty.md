
# 一、Netty

![](image/Netty通信流程.png)

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
- 经历了大规模的商业应用考虑

Netty5.0被废弃的原因：引入了ForkJoin框架提高了Netty的复杂度，但是对性能没有提升；

## 3、粘包和拆包

### 3.1、TCP粘包/拆包问题

TCP 是个流的协议，是连成一片的，其间没有分界线，TCP 底层并不了解业务数据的具体含义，它会根据 TCP 缓冲区的实际情况进行包的划分，所以在业务上认为，一个完整可能会被TCP拆分成多个包进行发送，也有可能把多个小的包封装成一个大的数据包发送。就是所谓的TCP粘包和拆包问题

TCP本质上是不会发生数据层面的粘包。粘包是数据处理的逻辑层面上发生的粘包

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

## 5、Netty线程模型

Netty可以同时支持Reactor单线程模型、多线程模型和主从Reactor多线程模型。

服务端启动的时候，创建了两个NioEventLoopGroup，他们实际是两个独立的Reactor线程池。一个用于接收客户端的TCO连接，另一个用于处理IO相关的读写操作或者执行系统的Task、定时任务Task；

通过调整线程池的线程个数、是否共享线程池等方式，netty的Reactor线程模型可以在单线程、多线程和主从多线程间切换。

不管是boos线程还是worker线程，所做的事情均分为以下三个步骤：
- 轮询注册在selector上的IO事件
- 处理IO事件
- 执行异步task

对于boos线程来说，第一步轮询出来的基本都是 accept 事件，表示有新的连接，而worker线程轮询出来的基本都是read/write事件，表示网络的读写事件

# 二、Netty服务端启动过程

两个问题：
- 服务端的Socket在哪里初始化？
- 在哪里accept连接？
```java
@Slf4j
public class NettyServer {
    public static void main(String[] args) {
		// 定义两个线程模型，boss表示监听端口，accept新连接的线程组；work表示处理每一条连接的数据读写的线程组；
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(boss, work) // 指定线程模型
				// 指定IO模型
                .channel(NioServerSocketChannel.class)
                // 指定处理新连接数据的读写处理逻辑
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {

                    }
                })
				// 给服务端NioServerSocketChannel指定一些属性，可以通过channle.attr取出该属性
				.attr(AttributeKey.newInstance("serverName"), "nettyServer")
				// 给每条连接指定自定义属性
				.childAttr(AttributeKey.newInstance("clientName"), "nettyClient")
				// 给每条连接设置一些TCP底层相关的属性
				.childOption(ChannelOption.SO_KEEPALIVE,true)
                // 用于指定在服务端启动过程中的一些逻辑
                .handler(new ChannelInitializer<NioServerSocketChannel>() {
                    @Override
                    protected void initChannel(NioServerSocketChannel ch) throws Exception {
                        log.info("Server is starting....");
                    }
                });

        bind(bootstrap, 1021);
    }

    private static void bind(final ServerBootstrap bootstrap, final int port) {
        bootstrap.bind(port).addListener((Future<? super Void> future) -> {
            if (future.isSuccess()) {
                log.info("port {} bind success...", port);
            } else {
                log.info("port {} bind failed...", port);
                bind(bootstrap, port + 1);
            }
        });
    }
}
```
Netty服务端启动过程：
- （1）、创建服务端channel
- （2）、初始化服务端channel
- （3）、注册Selector；
- （4）、端口绑定

![image]()

**服务端创建详细步骤**
- （1）创建ServerBootstrap实例：ServerBootstrap是Netty服务器的启动辅助类，它提供了一系列的方法用于服务端启动相关的参数；该类使用了Builder模式
- （2）设置并绑定Reactor线程池：Netty的Reactor线程池是EventLoopGroup，它实际就是EventLoop数组。

要启动一份Netty服务端，必须具备三个属性：线程模型、IO模型、连接读写处理逻辑


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

# 三、NioEventLoop

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


# 四、Pipeline

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
传播顺序与pipeline的添加顺序一致

### 5.1、什么是inBound事件以及ChannelInboundHandler

### 5.2、ChannelRead事件传播

## 6、outBound事件的传播

传播顺序与pipeline添加顺序逆序

## 7、异常的传播
传播顺序与pipeline添加顺序一致

### 7.1、异常的触发链

# 五、ByteBuf

## 1、ByteBuf的结构

## 2、ByteBuf分类
从三个角度：
- 池化：Pooled 和 Unpooled
    - Pooled池化内存分配每次从预先分配好的一块内存取一段连续内存封装成ByteBuf提供给应用程序,
    - Unpooled非池化每次进行内存分配的时候调用系统API向操作系统申请一块内存

- Unsafe 和 非Unsafe
    - Unsafe直接获取ByteBuf在JVM内存地址调用JDK的Unsafe进行读写操作,通过ByteBuf分配内存首地址和当前指针基于内存偏移地址获取值,
    - 非Unsafe不依赖JDK的Unsafe对象,通过内存数组和索引获取值

- Heap和Direct
    - Heap在堆上进行内存分配,分配内存需要被GC管理,无需手动释放内存,依赖底层byte数组,
    - Direct调用JDK的API进行内存分配,分配内存不受JVM控制最终不会参与GC过程,需要手动释放内存避免造成内存无法释放,依赖DirectByteBuffer对象内存

## 3、内存分配管理器：ByteBufAllocator

AbstractByteBufAllocator

ByteBufAllocator 两大子类：PooledByteBufAllocator、UnpooledByteBufAllocator

### 3.1、UnpooledByteBufAllocator
- Heap内存分配

- Direct内存分配

### 3.2、PooledByteBufAllocator内存分配


# Netty面试题

## 1、服务端的Socket在哪里初始化？

## 2、在哪里accept连接？

## 3、默认情况下，Netty服务端起多少线程？何时启动？

`EventLoopGroup bossGroup = new NioEventLoopGroup();`

```java
// 默认情况下不传，会调用另外一个构造函数，传入的是0
 public NioEventLoopGroup() {
    this(0);
}
// 最终会调用如何构造方法，此时nThreads这个参数的值为0
public NioEventLoopGroup(int nThreads, Executor executor, final SelectorProvider selectorProvider, final SelectStrategyFactory selectStrategyFactory) {
    super(nThreads, executor, selectorProvider, selectStrategyFactory, RejectedExecutionHandlers.reject());
}
// 会调用父类MultithreadEventLoopGroup的构造方法，其中会判断时nThreads是否为0，如果为0，则使用 DEFAULT_EVENT_LOOP_THREADS的值，该值时在静态代码块中初始化的
protected MultithreadEventLoopGroup(int nThreads, Executor executor, Object... args) {
    super(nThreads == 0 ? DEFAULT_EVENT_LOOP_THREADS : nThreads, executor, args);
}
// 如果没有配置变量：io.netty.eventLoopThreads，则默认电脑上默认的CPU核数*2，即取的是逻辑CPU的数量
private static final int DEFAULT_EVENT_LOOP_THREADS;
static {
    DEFAULT_EVENT_LOOP_THREADS = Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", Runtime.getRuntime().availableProcessors() * 2));
    if (logger.isDebugEnabled()) {
        logger.debug("-Dio.netty.eventLoopThreads: {}", DEFAULT_EVENT_LOOP_THREADS);
    }
}
```

## 4、Netty如何解决jdk空轮询bug的？-空轮询次数：512

**epoll空轮询原因：**

若Selector的轮询结果为空，也没有wakeup或新消息处理，则发生空轮询，CPU使用率100%

本质原因：在部分Linux的2.6的kernel中，poll和epoll对于突然中断的连接socket会对返回的eventSet事件集合置为POLLHUP，也可能是POLLERR，eventSet事件集合发生了变化，这就可能导致Selector会被唤醒

**Netty解决办法：**

- 对Selector的select操作周期进行统计，每完成一次空的select操作进行一次计数，
- 若在某个周期内连续发生N次空轮询，则触发了epoll死循环bug。
- 重建Selector，判断是否是其他线程发起的重建请求，若不是则将原SocketChannel从旧的Selector上去除注册，重新注册到新的Selector上，并将原来的Selector关闭

## 5、Netty如何保证异步串行无锁化？

通过串行化设计，即消息的处理尽可能在同一个线程内完成，期间不进行线程切换，这样就避免了多线程竞争和同步锁

Netty采用了串行无锁化设计，在IO线程内部进行串行操作，避免多线程竞争导致的性能下降。表面上看，串行化设计似乎CPU利用率不高，并发程度不够。但是，通过调整NIO线程池的线程参数，可以同时启动多个串行化的线程并行运行，这种局部无锁化的串行线程设计相比一个队列-多个工作线程模型性能更优

分析：NioEventLoop读取到消息之后，直接调用ChannelPipeline的fireChannelRead(Object msg)方法，只要用户不主动切换线程，一直会由NioEventLoop调用到用户的Handler，期间不进行线程切换。

## 6、Netty是在哪里检测有新连接接入的？

简单来说，新连接的建立可以分为三个步骤
- 检测到有新的连接
- 将新的连接注册到worker线程组
- 注册新连接的读事件

当服务端绑启动之后，服务端的channel已经注册到boos reactor线程中，reactor不断检测有新的事件，直到检测出有accept事件发生。
```java
// NioEventLoop
private void processSelectedKey(SelectionKey k, AbstractNioChannel ch) {
    final AbstractNioChannel.NioUnsafe unsafe = ch.unsafe();
....
}
```
表示boos reactor线程已经轮询到 SelectionKey.OP_ACCEPT 事件，说明有新的连接进入，此时将调用channel的 unsafe来进行实际的操作；

将该条连接通过chooser，选择一条worker reactor线程绑定上去。注册读事件，开始新连接的读写

## 7、新连接是怎样注册到NioEventLoop线程的

## 8、Netty是如何判断ChannelHandler类型的？

## 9、对于ChannelHandler的添加应该遵循什么样的顺序？

ChannelInboundHandler按照注册的先后顺序执行；ChannelOutboundHandler按照注册的先后顺序逆序执行
- 对于channelInboundHandler，总是会从传递事件的开始，向链表末尾方向遍历执行可用的inboundHandler。
- 对于channelOutboundHandler，总是会从write事件执行的开始，向链表头部方向遍历执行可用的outboundHandler

## 10、用户手动触发事件传播，不同的触发方式有什么样的区别？

## 11、Netty内存类别

- 堆内内存/堆外内存
    - 堆内：基于2048byte字节内存数组分配；
    - 堆外：基于JDK的DirectByteBuffer内存分配；

- Unsafe/非Unsafe
    - Unsafe：通过JDK的Unsafe对象基于物理内存地址进行数据读写；
    - 非Unsafe：调用JDK的API进行读写；

- UnPooled/Pooled
    UnPooled：每次分配内存申请内存；
    Pooled：预先分配好一整块内存,分配的时候用一定算法从一整块内存取出一块连续内存；

## 12、如何减少多线程内存分配之间的竞争

PooledByteBufAllocator内存分配器结构维护Arena数组，所有的内存分配都在Arena上进行,

通过PoolThreadCache对象将线程和Arena进行一一绑定，默认情况一个Nio线程管理一个Arena实现多线程内存分配相互不受影响减少多线程内存分配之间的竞争；

## 13、不同大小的内存是如何进行分配的

Page级别的内存分配通过完全二叉树的标记查找某一段连续内存,

Page级别以下的内存分配首先查找到Page然后把此Page按照SubPage大小进行划分最后通过位图的方式进行内存分配

## 14、Netty实现零拷贝

### 14.1、零拷贝（Zero-Copy）技术

零拷贝主要的任务就是避免CPU将数据从一块存储拷贝到另外一块存储，主要就是利用各种零拷贝技术，避免让CPU做大量的数据拷贝任务，减少不必要的拷贝，或者让别的组件来做这一类简单的数据传输任务，让CPU解脱出来专注于别的任务；

通常是指计算机在网络上发送文件时，不需要将文件内容拷贝到用户空间（User Space）而直接在内核空间（Kernel Space）中传输到网络的方式；

### 14.2、零拷贝实现

Linux中的`sendfile()`以及Java NIO中的`FileChannel.transferTo()`方法都实现了零拷贝的功能，而在Netty中也通过在FileRegion中包装了NIO的`FileChannel.transferTo()`方法实现了零拷贝；

在Netty中还有另一种形式的零拷贝，即Netty允许我们将多段数据合并为一整段虚拟数据供用户使用，而过程中不需要对数据进行拷贝操作；

### 14.3、Netty实现零拷贝

Netty 的 Zero-copy 体现在如下几个个方面：
- Netty 提供了 CompositeByteBuf 类, 它可以将多个 ByteBuf 合并为一个逻辑上的 ByteBuf, 避免了各个 ByteBuf 之间的拷贝.
- 通过 wrap 操作, 我们可以将 byte[] 数组、ByteBuf、ByteBuffer等包装成一个 Netty ByteBuf 对象, 进而避免了拷贝操作.
- ByteBuf 支持 slice 操作, 因此可以将 ByteBuf 分解为多个共享同一个存储区域的 ByteBuf, 避免了内存的拷贝.
- 通过 FileRegion 包装的FileChannel.tranferTo 实现文件传输, 可以直接将文件缓冲区的数据发送到目标 Channel, 避免了传统通过循环 write 方式导致的内存拷贝问题

#### 14.3.1、通过 CompositeByteBuf 实现零拷贝

如果希望将两个ByteBuf合并为一个ByteBuf，通常做法是：
```java
ByteBuf header = ...
ByteBuf body = ...
ByteBuf allBuf = Unpooled.buffer(header.readableBytes() + body.readableBytes());
allBuf.writeBytes(header);
allBuf.writeBytes(body);
```
将 header 和 body 都拷贝到了新的 allBuf 中了, 这无形中增加了两次额外的数据拷贝操作了；

CompositeByteBuf实现合并：
```java
ByteBuf header = ...
ByteBuf body = ...

CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
compositeByteBuf.addComponents(true, header, body);
// 或者使用如下方式
ByteBuf allByteBuf = Unpooled.wrappedBuffer(header, body);
```

不过需要注意的是, 虽然看起来 CompositeByteBuf 是由两个 ByteBuf 组合而成的, 不过在 CompositeByteBuf 内部, 这两个 ByteBuf 都是单独存在的, CompositeByteBuf 只是逻辑上是一个整体；

Unpooled.wrappedBuffer 方法, 它底层封装了 CompositeByteBuf 操作

#### 14.3.2、通过 wrap 操作实现零拷贝

有一个 byte 数组, 希望将它转换为一个 ByteBuf 对象，通常做法是：
```java
byte[] bytes = ...
ByteBuf byteBuf = Unpooled.buffer();
byteBuf.writeBytes(bytes);
```
显然这样的方式也是有一个额外的拷贝操作的, 我们可以使用 Unpooled 的相关方法, 包装这个 byte 数组, 生成一个新的 ByteBuf 实例, 而不需要进行拷贝操作. 上面的代码可以改为：
```java
byte[] bytes = ...
ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
```
通过 `Unpooled.wrappedBuffer`方法来将 bytes 包装成为一个 `UnpooledHeapByteBuf` 对象, 而在包装的过程中，是不会有拷贝操作的。即最后我们生成的生成的 ByteBuf 对象是和 bytes 数组共用了同一个存储空间，对 bytes 的修改也会反映到 ByteBuf 对象中；

#### 14.3.3、通过 slice 操作实现零拷贝

slice 操作和 wrap 操作刚好相反, Unpooled.wrappedBuffer 可以将多个 ByteBuf 合并为一个, 而 slice 操作可以将一个 ByteBuf 切片 为多个共享一个存储区域的 ByteBuf 对象

#### 14.3.4、通过 FileRegion 实现零拷贝

Netty 中使用 FileRegion 实现文件传输的零拷贝, 不过在底层 FileRegion 是依赖于 `Java NIO FileChannel.transfer` 的零拷贝功能；

通过 RandomAccessFile 打开一个文件, 然后 Netty 使用了 DefaultFileRegion 来封装一个 FileChannel：`new DefaultFileRegion(raf.getChannel(), 0, length)`；
有了 FileRegion 后, 我们就可以直接通过它将文件的内容直接写入 Channel 中, 而不需要像传统的做法: 拷贝文件内容到临时 buffer, 然后再将 buffer 写入 Channel

## 15、Netty的高性能体现

## 16、Netty组件之间的关系

Channel ----> Socket </br>
EventLoop ----> 控制流，多线程处理，并发；</br>
ChannelHandler和ChannelPipeline</br>
Bootstrap 和 ServerBootstrap</br>
Channel 接口

**一个 channel 对应一个channelPipeline ,一个 channelPipeline 对应多个channelHandler**

ChannelPipeline 为 ChannelHandler 链提供了容器，当 channel 创建时，就会被自动分配到它专属的 ChannelPipeline ，这个关联是永久性的

EventLoop 是用来处理连接的生命周期中所发生的事情，EventLoop, channel, Thread 以及 EventLoopGroup之间的关系：
- 一个 EventLoopGroup 包含多个 EventLoop
- 一个 EventLoop 在他的生命周期中只和一个 Thread 绑定
- 所有的 EventLoop 处理的 I/O 事件都将在专有的 Thread 上处理
- 一个 Channel 在他的生命周期中只会注册一个 EventLoop
- 一个 EventLoop 会被分配给多个 Channel;
