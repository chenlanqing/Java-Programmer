一.网络基础知识
1.两台计算机网络通信的条件:
	唯一标识-IP地址:IPv4(32位二进制,eg:(192.168.0.1));
	共同语言--协议;
	区分应用程序--端口号(应用程序的唯一标识):范围0~65535,其中0~1023为系统保留;
	常用端口号--http:80、ftp:21、telnet:23；
2.TCP/IP协议:是以TCP和IP为基础的不同层次上多个协议的集合(TCP/IP协议族、TCP/IP协议桟),处于传输层的协议
	TCP：Transmission Control Protocol 传输控制协议
	IP：Internet Protocol 网络协议
	模型：物理层--数据链路层--网络层--传输层(TCP/IP协议)--应用层
3.其他一些常见应用层协议：
	HTTP 超文件传输协议;
	FTP文件传输协议;
	SMTP简单邮件传送协议;
	Telnet远程登录服务;
4.IP地址和端口号组成了所谓的Socket，Socket是网络上运行的程序之间双向通信链路的终结点，是TCP和UDP的基础
5.针对网络通信不同层次，Java提供的网络功能的四大类：
	InetAddress:用于标识网络上的硬件资源
	URL:统一资源定位符，通过URL可以直接读取或写入网络上的数据
	Sockets:使用TCP协议实现网络通信的Socket相关的类
	Datagram:使用UDP协议，将数据保存在数据报中，通过网络进行通信

二.Java网络功能类
1.InetAddress:获取该对象的实例的方法
	(1).InetAddress.getLocalHost();
	(2).InetAddress.getByName(host);
	......

2.URL:统一资源定位符	

三.Socket编程:
1.Socket通信:
	TCP协议是面向连接,可靠的,有序的,以字节流的方式发送数据;
	客户端:Socket
	服务端:ServerSocket
2.socket通信步骤:
	(1).创建ServerSocket和Socket;
	(2).打开连接到Socket的输入输出流;
	(3).按照协议对Socket进行读写操作;
	(4).关闭输入输出流,关闭Socket;
3.Socket实现TCP编程:
	3.1.服务端编程步骤:
		(1).创建ServerSocket对象,绑定监听端口;
		(2).通过accept()方法监听客户端的请求,返回Socket;
		(3).建立连接后,通过输入流读取客户端发送的请求信息;
		(4).通过输出流向客户端发送响应信息;
		(5).关闭相关资源;
	3.2.客户端编程步骤:
		(1).创建Socket对象,指明需要连接的服务器地址和端口号;
		(2).连接建立后,通过输出流向服务器端发送请求信息;
		(3).通过输入流获取服务端响应的信息;
		(4).关闭相关资源;
4.Socket实现UDP编程
	4.1.UDP 编程:
		特点:udp协议是无连接，无状态，无序的、不可靠的。
		优点:速度快。
		原理:将需要发送的信息封装在数据报（Datagram）,在数据报里指明Socket主机和端口号。然后发送出去。
	4.2.使用的操作类: 
		DatagramPacket 表示数据报包 
		DataSocket 用来发送和接收数据报包的套接字。
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	