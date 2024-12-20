
# 1、Socket

IP地址与端口的结合协议，在网络传输中用于唯一标示两个端点之间的连接

## 1.1、Socket之TCP

- TCP是面向连接的通信协议；
- 由于TCP是面向连接的所有只能用于端到端的通讯；

## 1.2、Socket之UDP

- UDP是面向无连接的通讯协议；
- UDP数据包括目的的端口号和源端口号信息
- 可以实现广播发送，并不局限于端到端

## 1.3、UDP的API

- `DatagramSocket`
    - 用于接收和发送UDP的类；
    - 复制发送某一个UDP包，或者接收UDP包；
    - 不同于TCP，UDP并没有合并到Socket API中；
    - `DatagramSocket()`创建简单实例，不指定端口和IP
    - `DatagramSocket(int port)`创建监听固定端口的实例；
    - `DatagramSocket(int port, InetAddress local)`创建固定端口指定IP的实例
    - `receive(DatagramSocket d) `接收
    - ``send(DatagramSocket d) 发送
- `DatagramPacket`
    - 用于处理报文；
    - 将byte数值、目标地址、目标端口等数据包装成报文或者将报文拆卸成byte数组；
    - UDP的发送实体和接收实体

# 2、基本概念

- 报文段

    报文段是指TCP/IP协议网络传输过程中，起着路由导航的作用；用以查询各个网路路由段、IP地址、交换协议等IP数据包；报文段充当整个TCP/IP协议数据包的导航路由功能；

    报文在传输过程中不断封装分组、包、帧来传输；

- 协议：约定大于配置
- Mac地址：物理地址、硬件地址；用来定义网络设备的位置
- IP：
- 端口：0~1023号端口以及1024到49151都是特殊端口
- 远程服务器


# 参考资料

- [深入网络编程](https://www.tpvlog.com/article/345)

