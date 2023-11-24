# 1、概述

gRPC 是一个高性能、开源和通用的 RPC 框架，面向服务端和移动端，基于 HTTP/2 设计

gRPC 是由 Google 开发并开源的一种语言中立的 RPC 框架，当前支持 C、Java 和 Go 语言，其中 C 版本支持 C、C++、Node.js、C# 等

主要特点：
- 语言中立，支持多种语言；
- 基于 IDL 文件定义服务，通过 proto3 工具生成指定语言的数据结构、服务端接口以及客户端 Stub；
- 通信协议基于标准的 HTTP/2 设计，支持双向流、消息头压缩、单 TCP 的多路复用、服务端推送等特性，这些特性使得 gRPC 在移动端设备上更加省电和节省网络流量；
- 序列化支持 PB（Protocol Buffer）和 JSON，PB 是一种语言无关的高性能序列化框架，基于 HTTP/2 + PB, 保障了 RPC 调用的高性能
- gRpc 是基于长连接的；

# 2、gRpc案例

## 2.1、服务端业务代码

服务定义如下（helloworld.proto）：
```proto
service Greeter {
  rpc SayHello (HelloRequest) returns (HelloReply) {}
}
message HelloRequest {
  string name = 1;
}
message HelloReply {
  string message = 1;
}
```
服务端创建代码如下（HelloWorldServer 类）：
```java

private void start() throws IOException {
    /* The port on which the server should run */
    int port = 50051;
    server = ServerBuilder.forPort(port)
        .addService(new GreeterImpl())
        .build()
        .start();
...
}
// 其中，服务端接口实现类（GreeterImpl）如下所示：
static class GreeterImpl extends GreeterGrpc.GreeterImplBase {
    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
      HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
      responseObserver.onNext(reply);
      responseObserver.onCompleted();
    }
}
```

## 2.3、服务端创建流程

gRPC 服务端创建采用 Build 模式，对底层服务绑定、transportServer 和 NettyServer 的创建和实例化做了封装和屏蔽，整体上分为三个过程：
- 创建 Netty HTTP/2 服务端；
- 将需要调用的服务端接口实现类注册到内部的 Registry 中，RPC 调用时，可以根据 RPC 请求消息中的服务定义信息查询到服务接口实现类；
- 创建 gRPC Server，它是 gRPC 服务端的抽象，聚合了各种 Listener，用于 RPC 消息的统一调度和处理

# 3、http2

## 3.1、网络传输目标

数据的传输，都是被切割成一个个小块，包在层层网络协议头里，通过一个个路由器依次转发，最终到达目的地，被重新组装起来，在这个过程中，有两个亘古不变的目标：
- 更快的传输：快的背后就是少，传输的数据越少、越小，整体的速度也就越快。
- 更低的资源消耗：这背后是资源的高效利用，就像cpu那样，压榨的越厉害，就越节约资源。

数据体积拆开来看，有两个部分：
- 请求本身的数据：用Protobuf实现极致的压缩；
- 协议本身的消耗：协议需要自我表达，这会消耗一部分空间；

## 3.2、HTTP1.1问题

- 冗余文本过多，导致传输体积很大：作为一款经典的无状态协议，它使得Web后端可以灵活地转发、横向扩展，但其代价是每个请求都会带上冗余重复的Header，这些文本内容会消耗很多空间，和更快传输的目标相左；
- 并发能力差，网络资源利用率低：HTTP1.1 是基于文本的协议，请求的内容打包在header/body中，内容通过\r\n来分割，同一个TCP连接中，无法区分request/response是属于哪个请求，所以无法通过一个TCP连接并发地发送多个请求，只能等上一个请求的response回来了，才能发送下一个请求，否则无法区分谁是谁；

## 3.3、HTTP2优化

- HTTP2 未改变HTTP的语义(如GET/POST等)，只是在传输上做了优化
- 引入帧、流的概念，在TCP连接中，可以区分出多个request/response
- 一个域名只会有一个TCP连接，借助帧、流可以实现多路复用，降低资源消耗
- 引入二进制编码，降低header带来的空间占用

核心可分为 头部压缩 和 多路复用。这两个点都服务于更快的传输、更低的资源消耗这两个目标

**头部压缩：**

现在的web页面，大多比较复杂，新打开一个地址，动辄产生几十个请求，这会发送大量的header，大部分内容都是一样的内容；

基于这个想法，诞生了`HPACK[2]`，全称为HTTP2头部压缩，提供了两种方式极大地降低了header的传输占用
- 将高频使用的Header编成一个静态表，每个header对应一个数组索引，每次只用传这个索引，而不是冗长的文本;
- 支持动态地在表中增加header;

上面两个分别被成为静态表和动态表。静态表是协议级别的约定，是不变的内容。动态表则是基于当前TCP连接进行协商的结果，发送请求时会相互设置好header，让请求方和服务方维护同一份动态表，后续的请求可复用。连接销毁时，动态表也会注销

# Dubbo与gRpc

gRPC 与其它一些 RPC 框架的差异点是服务接口实现类的调用并不是通过动态代理和反射机制，而是通过 proto 工具生成代码，在服务端启动时，将服务接口实现类实例注册到 gRPC 内部的服务注册中心上。请求消息接入之后，可以根据服务名和方法名，直接调用启动时注册的服务实例，而不需要通过反射的方式进行调用，性能更优

响应协议降级

# 4、什么是ProtoBuf

ProtoBuf(Protocol Buffers)是一种跨平台、语言无关、可扩展的序列化结构数据的方法，可用于网络数据交换及存储。

在序列化结构化数据的机制中，ProtoBuf是灵活、高效、自动化的，相对常见的XML、JSON，描述同样的信息，ProtoBuf序列化后数据量更小、序列化/反序列化速度更快、更简单。

一旦定义了要处理的数据的数据结构之后，就可以利用ProtoBuf的代码生成工具生成相关的代码。只需使用 Protobuf 对数据结构进行一次描述，即可利用各种不同语言(proto3支持C++, Java, Python, Go, Ruby, Objective-C, C#)或从各种不同流中对你的结构化数据轻松读写

# 5、Protobuf存储格式

Protocol Buffers 是一种轻便高效的结构化数据存储格式。它使用 T-L-V（标识 - 长度 - 字段值）的数据格式来存储数据，T 代表字段的正数序列 (tag)，Protocol Buffers 将对象中的每个字段和正数序列对应起来，对应关系的信息是由生成的代码来保证的。在序列化的时候用整数值来代替字段名称，于是传输流量就可以大幅缩减；L 代表 Value 的字节长度，一般也只占一个字节；V 则代表字段值经过编码后的值。这种数据格式不需要分隔符，也不需要空格，同时减少了冗余字段名

# 参考资料

- [gRpc学习文档](https://skyao.gitbooks.io/learning-grpc/)
- [gRpc-Java](https://github.com/grpc/grpc-java)
- [gRpc原理分析](https://segmentfault.com/a/1190000019608421)