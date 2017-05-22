1.IP 头组成
2.TCP 三次握手和四次挥手
	
1.http是无状态通信，http的请求方式有哪些，可以自己定义新的请求方式么。
2.socket通信，以及长连接，分包，连接异常断开的处理。
3.socket通信模型的使用，AIO和NIO。
4.socket框架netty的使用，以及NIO的实现原理，为什么是异步非阻塞。
5.同步和异步,阻塞和非阻塞。
6.OSI 七层模型,包括TCP,IP的一些基本知识
7.http中,get post的区别
8.说说http,tcp,udp之间关系和区别。
9.说说浏览器访问www.taobao.com，经历了怎样的过程。
10.HTTP 协议,HTTPS 协议,SSL 协议及完整交互过程；
11.tcp的拥塞，快回传，ip的报文丢弃
12.https处理的一个过程，对称加密和非对称加密
13.head各个特点和区别
14.DNS 解析:主要是将域名解析成IP地址
	15.1.DNS 协议:
		是应用层协议,使用客户机/服务器模式在通信的端系统之间运行
	15.2.DNS 解析过程:[/Java知识点/Java/JavaEE/DNS解析过程.png]
		(1).浏览器会检查缓存中有没有这个域名对应的解析过的IP地址,如果缓存中存在,则这个解析过程结束.
			浏览器缓存域名也是有限制的,不仅浏览器的缓存大小有限制,而且缓存的时间也有限制.
		(2).如果用户浏览器缓存中没有数据,浏览器会查找操作系统缓存中是否有这个域名对应的DNS解析结果.操作系统也有一个域名解析的过程.
		 	windows是通过C:\Windows\System32\drivers\etc\hosts文件来设置,在Linux中可以通过/etc/hosts文件来设置,
		 	用户可以将任何域名解析到任何能够访问的IP地址;
		(3).前两个过程无法解析时,就需要用到DNS服务器地址了.操作系统会把这个域名发送给 LDNS(本地区的域名服务器).
		 	专门的域名解析服务器性能都会很好,它们一般都会缓存域名解析结果,当然缓存时间是受到域名的失效时间控制的.
		 	大约80%的域名解析到这里就结束了,所以LDNS主要承担了域名的解析工作;
		(4).如果 LDNS 仍未命中,就直接到 Root Server 域名服务器请求解析;
		(5).根域名服务器返回本地域名服务器一个所查询的主域名服务器(gTLD Server)的地址.
		 	gTLD是国际顶级域名服务器,如.com、.cn、.org等,全球只有13台左右
		(6).本地域名服务器 LDNS 再向上一步返回的 gTLD 服务器发送请求.
		(7).接受请求的gTLD服务器查找并返回此域名对应的Name Server 域名服务器的地址,这个Name Server 通常就是用户注册的
			域名服务器;
		(8).Name Server 域名服务器会查询存储的域名和IP的映射关系表,在正常情况下都根据域名得到目标IP地址,
			连同一个TTL值返回给 DNS Server 域名服务器;
		(9).返回该域名对应的IP和TTL值,LDNS 会缓存这个域名和IP的对应关系,缓存时间由TTL值控制;
		(10).把解析的结果返回给用户,用户根据TTL值缓存在本地系统中,域名解析过程结束.
	15.3.清除缓存的域名:
		缓存解析结果的位置:Local DNS Server 和 用户的本地机器
		(1).windows: ipconfig /flushdns
		(2).Linux 环境下可以通过/etc/init.d/nscd restart来清除缓存
		(3).Java 中 JVM 也会缓存 DNS 解析的结果,这个缓存是在 InetAddress 类中完成的,这个缓存时间比较特殊,
			有两种缓存策略:一种是正确的解析结果,一种是错误的解析结果;
			这两个缓存时间有两个配置项控制,配置项在 %JRE_HOME%/lib/security/java.security 文件中配置的.
			对应配置项分别为:networkaddress.cache.ttl,networkaddress.cache.negative.ttl,默认值分别为 -1(永不失效)
			和 10(缓存10秒).直接修改这两个值就可以了,也可以通过在Java启动参数中增加 -Dsun.net.inetaddr.ttl=xxx来
			修改默认值,也可以通过 InetAddress 类动态修改;
			==> 如果需要使用 InetAddress 类解析域名,必须是单例模式,不然会有验证的性能问题.
	15.4.域名解析方式:
		域名解析记录主要分为 A记录、MX记录、CNAME记录、NS记录 和 TXT记录
		(1).A记录:A 代表 Address,用来指定域名对应的IP地址,如将item.taobao.com指定到115.238.23.xxx,将switch.taobao.com
			指定到121.14.24.xxx. A 记录可以将多个域名解析到一个IP地址,但是不能将一个域名解析到多个IP地址;
		(2).MX记录:Mail Exchange,就是可以将某个域名下的邮件服务器指向自己的 Mail Server,如taobao.com域名的	A记录IP地址
			是115.238.25.xxx,如果将MX记录设置为115.238.25.xxx,即xxx@taobao.com的邮件路由,DNS 会将邮件发送到115.238.25.xxx
			所在的服务器,而正常通过Web请求的话仍然解析到A记录的IP地址;
		(3).CANME 记录:Canonical Name,即别名解析,所谓别名解析就是可以为一个域名设置一个或者多个别名;
		(4).NS记录:为某个域名指定 DNS 解析服务器,也就是这个域名由指定的IP地址的DNS服务器取解析;
		(5).TXT记录:为某个主机名或域名设置说明,如可以为ddd.net设置TXT记录为"这是XXX的博客"这样的说明
15.在不使用WebSocket情况下怎么实现服务器推送的一种方法:心跳检测

16.浏览器缓存机制[http://blog.csdn.net/longxibendi/article/details/41630389]
	16.1.浏览器缓存控制机制有两种:HTML Meta 标签 vs. HTTP 头信息
		(1).HTML Meta 标签控制缓存:
			浏览器缓存机制,其实主要就是HTTP协议定义的缓存机制,如:Expires, Cache-control等
			但是也有非HTTP协议定义的缓存机制,如使用HTML Meta 标签,Web 开发者可以在HTML页面的<head>节点中加入<meta>标签
			<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
		(2).HTTP 头信息控制缓存:
			浏览器第一次请求:/Java知识点/Java/JavaEE/浏览器第一次请求流程图.png
			浏览器再次请求时:/Java知识点/Java/JavaEE/浏览器请求流程.png
	16.2.几个重要概念:
		(1).Expires 策略:
			Expires 是Web服务器响应消息头字段,在响应http请求时告诉浏览器在过期时间前浏览器可以直接从浏览器缓存取数据,而无需再次请求
			Expires 是HTTP 1.0的东西,现在默认浏览器均默认使用HTTP 1.1,所以它的作用基本忽略;
			Expires 的一个缺点就是:返回的到期时间是服务器端的时间;
			在HTTP 1.1版开始,使用 Cache-Control: max-age=秒替代;
		(2).Cache-control策略:
			Cache-Control与 Expires的作用一致,都是指明当前资源的有效期,控制浏览器是否直接从浏览器缓存取数据还是重新发
				请求到服务器取数据.如果同时设置的话,其优先级高于Expires;
			值可以是:public、private、no-cache、no- store、no-transform、must-revalidate、proxy-revalidate、max-age
			各个消息中的指令含义如下:
				A.public:指示响应可被任何缓存区缓存.
				B.private:指示对于单个用户的整个或部分响应消息.不能被共享缓存处理.
					这允许服务器仅仅描述当用户的部分响应消息.此响应消息对于其他用户的请求无效。
				C.no-cache:指示请求或响应消息不能缓存,该选项并不是说可以设置"不缓存".容易望文生义.
				D.no-store:用于防止重要的信息被无意的发布.在请求消息中发送将使得请求和响应消息都不使用缓存,完全不存下來.
				E.max-age:指示客户机可以接收生存期不大于指定时间(以秒为单位)的响应.
				F.min-fresh:指示客户机可以接收响应时间小于当前时间加上指定时间的响应.
				G.max-stale:指示客户机可以接收超出超时期间的响应消息.如果指定max-stale消息的值,
					那么客户机可以接收超出超时期指定值之内的响应消息
		(3).Last-Modified/If-Modified-Since:Last-Modified/If-Modified-Since 要配合 Cache-Control 使用.
			Last-Modified:标示这个响应资源的最后修改时间.web服务器在响应请求时,告诉浏览器资源的最后修改时间.
			If-Modified-Since:当资源过期时(使用Cache-Control标识的max-age)发现资源具有 Last-Modified声明,则再次向web服务器请求
				时带上头 If-Modified-Since,表示请求时间.web服务器收到请求后发现有头 If-Modified-Since 则与被请求资源的最后修改时
				间进行比对.若最后修改时间较新,说明资源又被改动过,则响应整片资源内容(写在响应消息包体内)HTTP 200;
				若最后修改时间较旧,说明资源无新修改,则响应HTTP 304 (无需包体，节省浏览),告知浏览器继续使用所保存的cache;
		(4).Etag(实体标识):使用 Last-Modified 已经足以让浏览器知道本地的缓存副本是否足够新,为什么还需要Etag?
			HTTP1.1中Etag的出现主要是为了解决几个 Last-Modified 比较难解决的问题:
				Last-Modified 标注的最后修改只能精确到秒级,如果某些文件在1秒钟以内,被修改多次的话,它将不能准确标注文件的修
				改时间如果某些文件会被定期生成,当有时内容并没有任何变化,但 Last-Modified 却改变了,导致文件没法使用缓存
				有可能存在服务器没有准确获取文件修改时间,或者与代理服务器时间不一致等情形
			Etag 是服务器自动生成或者由开发者生成的对应资源在服务器端的唯一标识符,能够更加准确的控制缓存.
			Last-Modified 与 ETag一起使用时,服务器会优先验证ETag
	16.3.几种状态码的区别:
		(1).200 状态:当浏览器本地没有缓存或者下一层失效时,或者用户点击了 CTRL+F5 时,浏览器直接去服务器下载最新数据;
		(2).304 状态:这一层由 Last-Modified/ETag 控制.当下一层失效时或用户点击refresh,F5时,浏览器就会发送请求给服务器,
			如果服务器端没有变化,则返回304给浏览器;
		(3).200 (form cache):这一层由 expire/cache-control 控制,expires(http1.0有效)是绝对时间,cache-control(http1.1)相对时间,
			两者都存在时,cache-control 覆盖 expires,只要没有失效,浏览器只访问自己的缓存.
	16.4.用户行为与缓存:
		用户行为 	|Expires/Cache-Control| Last-Modified/Etag	
		地址栏回车		有效					有效
		页面链接跳转	有效					有效
		新开窗口		有效					有效
		前进/后退		有效					有效
		F5刷新			无效(BR重置max-age=0)	有效
		Ctrl+F5刷新		无效(重置CC=no-cache)	无效
17.CDN 工作机制

18.负载均衡
