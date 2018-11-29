一.图形界面:

二.网络编程:
	1.Socket:是网络编程的一个抽象概念
		大多数连接都是可靠的TCP连接。创建TCP连接时，主动发起连接的叫客户端，被动响应连接的叫服务器
	2.客户端:
		import socket
		s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		s.connect(('www.sina.com.cn', 80)) # connect:参数是一个 tuple
		(1).AF_INET指定使用IPv4协议，如果要用更先进的IPv6，就指定为AF_INET6
			SOCK_STREAM指定使用面向流的TCP协议























































