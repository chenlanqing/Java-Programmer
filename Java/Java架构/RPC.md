<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一.RPC 原理:](#%E4%B8%80rpc-%E5%8E%9F%E7%90%86)
  - [1.RPC:Remote Procedure Call(远程过程调用):](#1rpcremote-procedure-call%E8%BF%9C%E7%A8%8B%E8%BF%87%E7%A8%8B%E8%B0%83%E7%94%A8)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 一.RPC
## 1.RPC:Remote Procedure Call(远程过程调用):
    (1).一种通过网络从远程计算机程序上请求服务,不需要了解底层网络技术的协议.
    (2).在OSI网络通信模型中，RPC跨越了传输层和应用层
## 2.为什么要使用RPC:
    (1).可以做到分布式,现代化的微服务;
    (2).部署灵活, 解耦服务, 扩展性强;
## 3.工作原理:
	RPC 采用客户机/服务器模式.请求程序是一个客户机,而服务提供程序是一个服务器.
    * 首先,客户机调用进程发送一个有进程参数的调用信息到服务进程,然后等待应答信息.
    * 在服务端, 进程保持睡眠状态直到调用信息到达为止.
    * 当一个调用信息到达,服务端获得进程参数,计算结果,发送答复信息,然后等待下一个调用信息.
    * 最后, 客户端调用进程接收答复信息,获得进程结果,然后调用执行继续进行;
## 4.RPC框架解决的问题:
    (1).通讯问题:即A与B之间通讯,建立TCP连接;
    (2).寻址问题:A通过RPC框架连接到B的服务器及特定端口和调用的方法名;
    (3).参数序列化与反序列化:发起远程调用参数数值需要二进制化,服务接收到二进制参数后需要反序列化
## 5.与HTTP服务相比:
    (1).HTTP服务:
        * HTTP服务主要基于HTTP协议;
        * RESTful风格的服务接口;
        * 主要是进行接口开发;
        * 3次握手,有网络开销;
    (2).RPC服务:
        * RPC主要基于TCP/IP协议;
        * 一般都有注册中心,有丰富的监控管理;
        * 长连接;
        