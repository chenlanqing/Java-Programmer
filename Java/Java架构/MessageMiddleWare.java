一.消息中间件

二.ActiveMQ
1.ActiveMQ 基本概念:

2.使用 ActiveMQ:

3.ActiveMQ 集群:
	3.1.集群方式:
		(1).客户端集群:当多个消费者消费同一个队列
		(2).Broker cluster:多个 Broker 之间同步消息
		(3).Master Slave:高可用
	3.2.失效转移:允许当其中一台消息服务器宕机时,客户端在传输层上重新连接到其他消息服务器
		语法:failover:(uri1,...,uriN)?transporOptions
	3.3.Broker Cluster 集群配置:
		3.3.1.网络连接器(NetWorkConnector)
			主要用于配置 ActiveMQ 服务器与服务器之间的网络通信方式,用于服务器透传消息
			分为静态连接器和动态连接器
			(1).静态连接器:
				<networkConnectors>
					<networkConnector uri="static:(tcp://127.0.0.1:61617,tcp://127.0.0.1:61618)"/>
				</networkConnectors>




