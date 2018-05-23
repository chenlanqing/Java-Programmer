<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、Servlet](#%E4%B8%80servlet)
  - [1、不使用开发工具开发一个servlet](#1%E4%B8%8D%E4%BD%BF%E7%94%A8%E5%BC%80%E5%8F%91%E5%B7%A5%E5%85%B7%E5%BC%80%E5%8F%91%E4%B8%80%E4%B8%AAservlet)
  - [2、load-on-startup](#2load-on-startup)
  - [3、servlet-mapping](#3servlet-mapping)
  - [4、Servlet线程安全问题](#4servlet%E7%BA%BF%E7%A8%8B%E5%AE%89%E5%85%A8%E9%97%AE%E9%A2%98)
  - [5、Servlet初始化参数](#5servlet%E5%88%9D%E5%A7%8B%E5%8C%96%E5%8F%82%E6%95%B0)
  - [6、ServletContext](#6servletcontext)
  - [7、HttpServletResponse](#7httpservletresponse)
  - [8、HttpServletResquest](#8httpservletresquest)
  - [9、状态管理](#9%E7%8A%B6%E6%80%81%E7%AE%A1%E7%90%86)
    - [9.1、Cookie](#91cookie)
    - [9.2、Session](#92session)
    - [9.3、Cookie 与 Session](#93cookie-%E4%B8%8E-session)
    - [9.4、分布式Session](#94%E5%88%86%E5%B8%83%E5%BC%8Fsession)
  - [10、Servlet 与 Servlet 容器](#10servlet-%E4%B8%8E-servlet-%E5%AE%B9%E5%99%A8)
    - [10.1、两者直接的关系](#101%E4%B8%A4%E8%80%85%E7%9B%B4%E6%8E%A5%E7%9A%84%E5%85%B3%E7%B3%BB)
    - [10.2、Servlet 容器的启动过程](#102servlet-%E5%AE%B9%E5%99%A8%E7%9A%84%E5%90%AF%E5%8A%A8%E8%BF%87%E7%A8%8B)
    - [10.3、Servlet 对象创建](#103servlet-%E5%AF%B9%E8%B1%A1%E5%88%9B%E5%BB%BA)
    - [10.4、Servlet 是如何运行的](#104servlet-%E6%98%AF%E5%A6%82%E4%BD%95%E8%BF%90%E8%A1%8C%E7%9A%84)
    - [10.5.servlet的生命周期的四个阶段](#105servlet%E7%9A%84%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F%E7%9A%84%E5%9B%9B%E4%B8%AA%E9%98%B6%E6%AE%B5)
    - [10.6、Servlet架构](#106servlet%E6%9E%B6%E6%9E%84)
    - [10.7、创建Servlet对象的时机](#107%E5%88%9B%E5%BB%BAservlet%E5%AF%B9%E8%B1%A1%E7%9A%84%E6%97%B6%E6%9C%BA)
    - [10.8、销毁Servlet对象的时机](#108%E9%94%80%E6%AF%81servlet%E5%AF%B9%E8%B1%A1%E7%9A%84%E6%97%B6%E6%9C%BA)
- [二、HTTP协议](#%E4%BA%8Chttp%E5%8D%8F%E8%AE%AE)
- [三、Servlet高级应用](#%E4%B8%89servlet%E9%AB%98%E7%BA%A7%E5%BA%94%E7%94%A8)
  - [1.监听器](#1%E7%9B%91%E5%90%AC%E5%99%A8)
  - [2、过滤器：Filter](#2%E8%BF%87%E6%BB%A4%E5%99%A8filter)
- [四、其他](#%E5%9B%9B%E5%85%B6%E4%BB%96)
  - [1、防止表单重复提交](#1%E9%98%B2%E6%AD%A2%E8%A1%A8%E5%8D%95%E9%87%8D%E5%A4%8D%E6%8F%90%E4%BA%A4)
- [参考资料](#%E5%8F%82%E8%80%83%E8%B5%84%E6%96%99)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 一、Servlet
## 1、不使用开发工具开发一个servlet

- （1）在tomcat服务器的webapps下新建一个目录test(应用名)，在test文件夹内再新加一个WEB-INF文件夹，<br>
再在WEB-INF文件夹内新建一个classes文件夹;\apache-tomcat-6.0.14\webapps\test\WEB-INF\classes；
- （2）在classes文件夹新建一个SecondServlet.java文件，如下代码:
```java
	package cn.tarena;
	import java.io.*;
	import javax.servlet.*;
	import javax.servlet.http.*;
	public class SecondServlet extends HttpServlet{
		
		public void service(HttpServletRequest req，HttpServletResponse res)throws ServletException，IOException{
			//PrintWriter out = response.getWriter();
			PrintWriter out = res.getWriter();
			out.println("实现HttpServlet......................");
		}
	}
```
- （3）找到servlet-api.jar文件，复制到当前classes文件夹中，编译java文件:<br>
	javac -cp servlet-api.jar -d . SecondServlet.java--- .表示当前目录;
- （4）配置web.xml文件:在WEB-INF文件夹下新建文件web.xml，添加如下配置:
```xml
	<?xml version="1.0" encoding="UTF-8"?>
	<web-app xmlns="http://java.sun.com/xml/ns/javaee"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
		version="2.5">
			<servlet>
			<servlet-name>secondServlet</servlet-name>
			<servlet-class>cn.tarena.SecondServlet</servlet-class>
		</servlet>
		
		<servlet-mapping>
			<servlet-name>secondServlet</servlet-name>
			<url-pattern>/secondServlet</url-pattern>
		</servlet-mapping>
	</web-app>
```
- 访问该servlet：启动tomcat服务器，地址栏输入:localhost:8080/test/secondServlet

## 2、load-on-startup

服务器启动时初始化servlet，如果在<servlet>元素中配置了一个<load-on-startup>元素，那么WEB应用程序在启动时，<br>
就会装载并创建Servlet的实例对象、以及调用Servlet实例对象的init()方法。<br>
	举例：
```
	<servlet>
		<servlet-name>invoker</servlet-name>
		<servlet-class>
			org.apache.catalina.servlets.InvokerServlet
		</servlet-class>
		<load-on-startup>2</load-on-startup>
	</servlet>
```
是一个正整数，数字越小，先启动。<br>
用途：为web应用写一个InitServlet，这个servlet配置为启动时装载，为整个web应用创建必要的数据库表和数据

## 3、servlet-mapping

如果某个Servlet的映射路径仅仅为一个正斜杠("/")，那么这个Servlet就成为当前Web应用程序的缺省Servlet。<br>
凡是在web.xml文件中找不到匹配的<servlet-mapping>元素的URL，它们的访问请求都将交给缺省Servlet处理，<br>
也就是说，缺省Servlet用于处理所有其他Servlet都不处理的访问请求。<br>
在<tomcat的安装目录>\conf\web.xml文件中，注册了一个名称为:<br>
org.apache.catalina.servlets.DefaultServlet的Servlet，并将这个Servlet设置为了缺省Servlet。<br>
当访问Tomcat服务器中的某个静态HTML文件和图片时，实际上是在访问这个缺省Servlet

## 4、Servlet线程安全问题

## 5、Servlet初始化参数

ServletConfig接口，没有固定值的数据都可以通过配置方式：如数据库，字符集编码;<br>
在web.xml文件中配置参数:(可以配置多个<init-param>)，这里是为每个Servlet配置初始化参数
```
	<init-param>
		<param-name>country</param-name>
		<param-value>China</param-value>
	</init-param>
```
在Servlet中获取值:<br>

	// 根据配置参数名获取参数值
	String value = this.getServletConfig().getInitParameter("country");
	// 获取配置中所有初始化参数
	Enumeration e = this.getServletConfig().getInitParameterNames();

★ ServletConfig对象用于封装servlet的配置信息
	
## 6、ServletContext

可以通过ServletContext来实现多个servlet的资源共享

	ServletContext context = this.getServletContext();

- ①、ServletContext域:这是一个容器，说明了该容器的作用范围，也就是应用程序范围;
- ②可以通过给Web应用配置全局初始化参数

	<context-param>
		<param-name>country</param-name>
		<param-value>China</param-value>
	</context-param>
	获取:this.getServletContext().getInitParameter("country");

- ③、管理web资源文件(.xml	.properties)
	- Ⅰ.读取.properties文件:
```
	InputStream in = this.getServletContext().getResourceAsStream("/WEB-INF/classes/db.properties");
	Properties props = new Properties();
	props.load(in);
```
	注意:
	FileInputStream input = new FileInputStream("classes/db.properties");
	这里流的读取文件的路径是相对路径，在Java中，其是相对与JVM的路径;
	在Web开发中尤其要注意，"classes/db.properties"这样写是相对于服务器里的目录;
	以tomcat为例:
		这里就是相当于tomcat/bin目录的，完整应该是:/bin/classes/db.properties，
		因此在Web开发中读取配置文件最好使用ServletContext来读取;
	◆还有一种方法获取配置文件在服务器上的绝对路径:
		String path = this.getServletContext().getRealPath("/WEB-INF/classes/db.properties");
		那么就可以使用流来读取了:
		FileInputStream input = new FileInputStream(path);
	◆可以使用类装载器来实现配置文件的读取(不在Servlet中读取)
		InputStream input = Demo2.class.getClassLoader().getResourceAsStream("db.properties");
		这里使用类装载器来加载配置文件，那么这里将不能读取db.properties更新后的数据;
		如果需要读取更新后的数据:
		使用类加载器来获取配置文件的路径
			String path = Demo2.class.getClassLoader().getResource("db.properties");
			FileInputStream input = new FileInputStream(path);

## 7、HttpServletResponse
```java
//设置response使用的编码格式，以控制其输出到浏览器的编码
response.setCharacterEncoding("UTF-8"); 
//指定浏览器以什么编码打开服务器发送的数据
response.setHeader("content-type", "text/html;charset=UTF-8");
```

- ①、控制浏览器定时刷新<br>
	response.setHeader("refresh", "3;url=指向地址");//每隔3s刷新页面<br>
	◆用户注册成功，自动跳转页面，可以用此来实现<br>
- ②、控制缓存:<br>
	控制缓存时间<br>
	response.setDateHeader("expires",System.currentTimeMillis() + 1000*3600 );<br>
	禁止浏览器缓存<br>
	response.setDateHeader("expires",-1);<br>
- ③、重定向:服务器发送一个302状态和一个location消息头(值是一个地址，称为重定向地址)<br>
	response.setStatus(302);<br>
	response.setHeader("location"，"url...");<br>
	或者:<br>
	response.sendRedirect(url);<br>
	- 特点:<br>
		Ⅰ.浏览器会向服务器发送两次请求;<br>
		用户登录和显示购物车时通常会用到重定向技术;<br>
	- 注意:<br>
		response.getWriter()与response.getOutputStream()不能同时使用在一个Servlet(包括转发)，<br>
		否则会出现异常:<br>
		java.lang.IllegalStateException: getWriter() has already been called for this response<br>
		重定向不会出现这种问题，因为其是两次响应;<br>
- ④、获取的输出流，可以不去处理吗，Servlet会自动去管理关闭这些流；

## 8、HttpServletResquest

- 乱码问题
```
	可以修改服务器的配置，如tomcat，可以加上属性:URIEncoding="utf-8"
	<Connector port="8088" protocol="HTTP/1.1" 
		connectionTimeout="20000" 
		redirectPort="8443" URIEncoding="utf-8" />
```		
- 请求转发:
		
## 9、状态管理

Session 与 Cookie 的作用都是为了保持访问用户与后端服务器的交互状态

### 9.1、Cookie

是服务器在本地机器上存储的小段文本并随每一个请求发送至同一个服务器，网络服务器用HTTP头向客户端<br>
发送cookies，在客户终端，浏览器解析这些cookies并将它们保存为一个本地文件.

- 当一个用户通过 HTTP 协议访问一个服务器的时候，这个服务器会将一些 Key/Value 键值对返回给客户端浏览器，<br>
	并给这些数据加上一些限制条件，在条件符合时这个用户下次访问这个服务器的时候，数据又被完整地带回给服务器;
- cookie的内容主要包括:名字、值、过期时间、路径和域.路径与域一起构成cookie的作用范围.若不设置过期时间，<br>
	则表示这个cookie的生命期为浏览器会话期间，关闭浏览器窗口，cookie就消失，这种生命期为浏览器会话期的cookie<br>
	被称为会话cookie.会话cookie一般是存储在内存中的;
- 若设置了过期时间，浏览器就会把cookie保存到硬盘上，关闭后再次打开浏览器，这些cookie仍然有效直到超过设定的<br>
	过期时间，存储在硬盘上的cookie可以在不同的浏览器进程间共享

### 9.2、Session

session机制采用的是一种在服务器端保持状态的解决方案.由于采用服务器端保持状态的方案在客户端也需要<br>
保存一个标识，所以session机制可能需要借助于cookie机制来达到保存标识的目的.

- session是针对每一个用户的，变量的值保存在服务器上，用一个sessionID来区分是哪个用户session变量，这个值是<br>
	通过用户的浏览器在访问的时候返回给服务器，当客户禁用cookie时，这个值也可能设置为由get来返回给服务器.
- 就安全性来说，服务器端的session机制更安全些，因为它不会任意读取客户存储的信息.
- 当程序需要为某个客户端的请求创建一个session时，服务器首先检查这个客户端的请求里是否已包含了一个<br>
	session标识(称为sessionId)，如果已包含则说明以前已经为此客户端创建过session，服务器就按照sessionId<br>
	把这个session检索出来使用(检索不到，会新建一个)，如果客户端请求不包含session id，则为此客户端创建一个<br>
	session并且生成一个与此session相关联的session id，session id的值应该是一个既不会重复，又不容易被找<br>
	到规律以仿造的字符串，这个session id将被在本次响应中返回给客户端保存保存这个session id的方式可以采用cookie，<br>
	这样在交互过程中浏览器可以自动的按照规则把这个标识发挥给服务器

### 9.3、Cookie 与 Session

都能够进行会话跟踪，但是完成的原理不太一样

- 存取方式不同
	- Cookie 中只能保管ASCII字符串，假如需求存取Unicode字符或者二进制数据，需求先进行编码;也不能直接存储Java对象;
	- Session 中能够存取任何类型的数据，包括而不限于 String、Integer、List、Map 等;
	- 单个cookie保存的数据不能超过4K，很多浏览器都限制一个站点最多保存20个cookie
- 隐私策略的不同
	- Cookie 存储在客户端阅读器中，对客户端是可见的，客户端的一些程序可能会窥探、复制以至修正Cookie中的内容;
	- Session 存储在服务器上，对客户端是透明的，不存在敏感信息泄露的风险;
- 有效期上的不同
	- Cookie 设置过期时间可以很大，保证长期有效.
	- Session 依赖于名为JSESSIONID的Cookie，而Cookie JSESSIONID 的过期时间默许为–1，只需关闭了阅读器该Session就会失效
- 服务器压力的不同
	- Cookie 保管在客户端，不占用服务器资源，假如并发阅读的用户十分多，Cookie 是很好的选择
	- Session 是保管在服务器端的，每个用户都会产生一个Session.假如并发访问的用户十分多，会产生十分多的Session，耗费大量的内存
- 浏览器支持的不同
	- Cookie 是需要客户端浏览器支持的.假如客户端禁用了Cookie，或者不支持Cookie，则会话跟踪会失效;
	- 假如客户端浏览器不支持Cookie，需要运用Session以及URL地址重写
- 跨域支持上的不同
	- Cookie 支持跨域名访问
	- Session 则不会支持跨域名访问.Session 仅在他所在的域名内有效

### 9.4、分布式Session

[分布式Session](https://github.com/chenlanqing/learningNote/blob/master/Java/Java%E6%9E%B6%E6%9E%84/%E5%88%86%E5%B8%83%E5%BC%8F.md#%E4%B8%89%E5%88%86%E5%B8%83%E5%BC%8Fsession)

## 10、Servlet 与 Servlet 容器

### 10.1、两者直接的关系

- 两者关系有点像枪和子弹的关系，枪是为子弹而生，而子弹又让枪有了杀伤力。从技术角度来说是为了解耦，通过标准化接口来相互协作。
- 以Tomcat如何管理Servlet容器来说：<br>
	Tomcat 的容器等级中Context容器是直接管理Servlet在容器中的包装类Wrapper，所以Context容器如何运行将直接影响Servlet的工作方式.<br>
	一个 Context 对应一个 Web 工程<br>
```
	<Context path="/projectOne " docBase="\user\projects\projectOne" reloadable="true" />
```
### 10.2、Servlet 容器的启动过程

Tomcat7 也开始支持嵌入式功能，增加了一个启动 org.apache.catalina.startup.Tomcat.将Servlet包装成StandardWrapper并作为子容器<br>
添加到 Context 中，其它的所有 web.xml 属性都被解析到 Context 中，所以说 Context 容器才是真正运行 Servlet 的 Servlet 容器

### 10.3、Servlet 对象创建

如果 Servlet 的 load-on-startup 配置项大于 0，那么在 Context 容器启动的时候就会被实例化.<br>
在 conf 下的 web.xml 文件中定义了一些默认的配置项，其定义了两个 Servlet，分别是:<br>
org.apache.catalina.servlets.DefaultServlet 和 org.apache.jasper.servlet.JspServlet 它们的 load-on-startup <br>
分别是 1 和 3，也就是当 Tomcat 启动时这两个 Servlet 就会被启动

### 10.4、Servlet 是如何运行的

servlet容器为servlet运行提供了网络相关的服务：<br>
比如在浏览器地址栏输入地址:	http://ip:port/web01/hello

- Step1：浏览器依据ip，port建立与servlet容器(servlet容器也是一个简单的服务器)之间的链接
- Step2：浏览器将请求参数，请求资源路径等等打包(需按照http协议的要求)
- Step3：浏览器将请求数据包发送给servlet容器
- Step4：容器收到请求之后，对请求的数据包进行解析(拆包)，然后将解析之后的结果封装request对象上，同时容器还会创建一个response对象
- Step5：容器依据请求资源路径("/web01/hello")找到应用所在的文件夹，然后依据web.xml找到对应的servlet配置(servlet的类名)，然后容器创建该servlet对象
- Step6：容器调用servlet对象的service方法(会将事先创建好的request，response作为参数传递进来)
- Step7：ervlet可以通过请求request对象获得请求参数，进行相应的处理，然后将处理结果缓存到response对象上
- Step8：容器从response对象上获取之前处理的结果，然后打包发送给浏览器.
- Step9：浏览器拆包(解析容器返回的响应数据包)，依据获取的数据生成相应的页面;

### 10.5.servlet的生命周期的四个阶段

![image](https://github.com/chenlanqing/learningNote/blob/master/Java/JavaEE/images/Servlet生命周期.png)

**1、实例化**

- 什么是实例化:容器调用servlet构造器创建一个servlet对象;<br>
	在默认情况下，不管有多少请求，容器只会创建一个servlet对象.
- 什么时候实例化？
	- 情况1：在默认情况下，容器收到请求之后才会创建servlet对象;
	- 情况2：容器在启动时，就将某些servlet对象创建;这些servlet必须在web.xml中

	配置一个参数:<load-on-startup>配置，其参数值越小，优先级越高，0为最高优先级
	例如:<load-on-startup>1</load-on-startup>

**2、初始化**

- 什么是初始化：容器创建好servlet对象之后，会立即调用init方法;
- 怎么样实现初始化处理逻辑？
	- 一般情况下，不需要写init方法，因为GenericServlet类依据实现了innit方法:<br>
		// 将容器创建的ServletConfig对象保存下来，<br>
		// 并且提供了getServletConfig方法来获得该对象<br>
		// 调用了一个空的init方法，(该init方法用于子类去override)<br>
		// 建议override无参的init方法<br>
	- 如果要实现自己的初始化处理逻辑，只要override init()方法
	- 初始化方法只会执行一次
	- ServletConfig对象可以用来访问servlet的初始化参数

**3、就绪/调用:service方法调用多次，init方法，构造器都只调用一次**

- 什么是就绪：servlet容器收到请求之后，会调用servlet对象的service方法来处理请求
- 如何编写业务逻辑？
	- 方式一.override HttpServlet的service方法:HttpServlet的service方法实现:<br>
			依据请求类型调用doGet()或者doPost()方法，<br>
			这两方法在默认情况下就只是简单的抛出异常，需要子类去override;
	- 方式二.override HttpServlet的doGet()或者doPost()方法;

**4、销毁:**

- 什么是销毁:容器依据自身的算法，是否销毁servlet对象；容器在销毁之前，会调用servlet对象的destroy()方法;
- destroy方法只会执行一次;

### 10.6、Servlet架构
#### 10.6.1、Servlet框架组成

由两个 java 包组成：javax.servlet与javax.servlet.http
- javax.servlet 定义了所有Servlet类必须实现或者扩展的通用接口和类;
- javax.servlet.http 定义了采用http协议通信的HttpServlet类.

#### 10.6.2、Servlet框架核心是 Servlet类

所有Servlet都必须实现这个接口.在Servlet接口中定义了5个方法，其中3个方法代表了Servlet的生命周期.
- init(ServletConfig)方法：<br>
	负责初始化Servlet对象，在Servlet生命周期中，该方法执行一次;该方法执行在单线程环境中，因此不用考虑线程安全问题;
- service(ServletRequest req，ServletResponse res)方法：
	负责响应客户的请求，为了提高效率，Servlet规范要求一个Servlet实例必须能够同时服务于多个客户端请求，<br>
	即service是运行在多线程环境下，必须保证该方法的线程安全性;
- destroy()方法：当Servlet对象退出生命周期时，负责释放占用的资源;

#### 10.6.3、service方法注意事项

- 如果service方法没有访问servlet的成员变量也没有访问全局资源，如果静态变量、文件、数据库连接，而是只使用了当前线程<br>
	自己的资源，比如指向全局资源的临时变量、request、response等对象，该方法本身就是线程安全的，不需要进行同步控制;
- 如果service()方法访问了Servlet的成员变量，但是对该变量的操作是只读操作，该方法本身就是线程安全的，不必进行任何的同步控制;
- 如果service()方法访问了Servlet的成员变量，并且对该变量的操作既有读又有写，通常需要加上同步控制语句;
- 如果service()方法访问了全局的静态变量，如果同一时刻系统中也可能有其它线程访问该静态变量，如果既有读也有写的操作;通常需要加上同步控制语句
- 如果service()方法访问了全局的资源，比如文件、数据库连接等，通常需要加上同步控制语句;

### 10.7、创建Servlet对象的时机

- 默认情况下，在Servlet容器启动后:客户首次向Servlet发出请求，Servlet容器会判断内存中是否存在指定的Servlet对象，如果没有则创建它，<br>
	然后根据客户的请求创建HttpRequest、HttpResponse对象，从而调用Servlet对象的service方法;
- Servlet容器启动时:当web.xml文件中如果<servlet>元素中指定了<load-on-startup>子元素时，Servlet容器在启动web服务器时，将按照顺序创建并初始化Servlet对象;

### 10.8、销毁Servlet对象的时机

Servlet容器停止或者重新启动:Servlet容器调用Servlet对象的destroy方法来释放资源

# 二、HTTP协议			
[HTTP协议](https://github.com/chenlanqing/learningNote/blob/master/Java/JavaEE/%E7%BD%91%E7%BB%9C%E5%9F%BA%E7%A1%80.md#1http协议)
			
# 三、Servlet高级应用

## 1.监听器

- 1.1、servlet规范当中规定的一种特殊的组件，用于监听servlet容器产生的事件并进行相应的处理<br>
	容器产生的事件主要有两大类：
	- 声明周期相关的事件：容器创建或者销毁request，session，servlet上下文是产生的事件
	- 绑定的相关事件，容器调用了request，session，servlet上下文的setAttribute，removeAttribute时产生的事件

- 1.2.、监听器应用
	统计在线人数和在线用户;<br>
	系统启动时加载初始化信息;<br>
	统计网站访问量;<br>
	结合Spring使用;<br>
- 1.3、servlet组件启动顺序：监听器 ----> 过滤器 ----> Servlet

- 1.4、监听器分类
	- 按监听的对象分类：
		- 用于监听应用程序环境对象(ServletContext)的事件监听器;
		- 用于监听用户会话对象(HttpSession)的事件监听器;
		- 用于监听请求消息对象(ServletRequest)的事件监听器
	- 按监听的事件划分：
		- 监听域对象自身的创建与销毁;ServletContext，HttpSession，ServletRequest
		- 监听域对象的属性增加与删除;
		- 监听绑定到HttpSession域中的某个对象的状态;

- 1.5、监听域对象自身的创建与销毁
	- ServletContext: 需实现:ServletContextListener，有两个事件处理方法：创建或销毁ServletContext对象;
	```java
		// 主要用途：定时器，全局属性对象
		@Override
		public void contextDestroyed(ServletContextEvent sce) {	
			// ServletContext 销毁时调用
			System.out.println("**************销毁监听器*********");
		}
		
		@Override
		public void contextInitialized(ServletContextEvent sce) {
			// ServletContext 创建是调用
			// ServletContextEvent 可以获取 ServletContext
			String username = sce.getServletContext().getInitParameter("");
			System.out.println("**************监听器*********" + username);
		}
	```
	- HttpSession对象:实现了HttpSessionListener
	```java
		@Override
		public void sessionCreated(HttpSessionEvent se) {
			// session创建时调用
		}
		@Override
		public void sessionDestroyed(HttpSessionEvent se) {
			// session销毁时调用
		}
	```
	- ServletRequest对象:实现了ServletRequestListener
	```java
		@Override
		public void requestDestroyed(ServletRequestEvent sre) {
			
		}

		@Override
		public void requestInitialized(ServletRequestEvent sre) {

		}
	```	
- 1.6、监听域对象的属性增加与删除：<br>
	实现接口
	- ServletContextAttributeListener : 属性的增加与删除
	- HttpSessionAttributeListener : Session属性的增加与删除;
	- ServletRequestAttributeListener : 请求参数的增加与删除
		
- 1.7、绑定到HttpSession域中的对象状态的事件监听器

## 2、过滤器：Filter

不能直接处理请求；服务器端的组件，其可以截取用户端的请求与响应信息，并对这些信息过滤;

**2.1、工作原理:**

过滤器将用户请求发送到web资源，web资源发送响应到过滤器，过滤器将web资源发送给用户;

**2.2、生命周期:**

实例化 ---> web.xml(实例化一次)<br>
初始化 ---> init()(执行一次)<br>
过滤   ---> doFilter()(多次执行)<br>
销毁   ---> destroy()(web容器关闭)

**2.3、实现: implements Filter**

**2.4、过滤器链:服务器按照web.xml定义的先后顺序将过滤器组装成一条链**

**2.5、过滤器分类**

- request:用户直接访问页面时，web容器会调用过滤器
- forward:目标资源是RequestDispatcher	的forward访问时，该过滤器被调用;
- include:目标资源是RequestDispatcher	的include访问时，该过滤器被调用;
- error :目标资源是通过声明式异常处理机制调用时，过滤器将被调用 
- async: 异步调用资源------Servlet3.0
	@WebFilter

**2.6、过滤器应用:**

- 对用于请求进行统一认证;
- 编码转换
- 对用户发送的数据进行过滤转换;
- 转换图像格式;
- 对响应内容进行压缩;
- 通过FilterConfig来获取初始化参数

# 四、其他

## 1、防止表单重复提交

- ①、客户端防表单重复提交：（使用Javascript来实现） /servlet/RepeatCommit
```html
	<script type="text/javascript">
		var isCommitted = false;
		function doSubmit(){
			if(!isCommitted){
				isCommitted = true;
				return true;	
			}else{
				alert("请勿重复提交")
				return false;	
			}
		}
		//还可以在提交之后，将提交按钮置为disabled
		function doSubmit(){
			var obj = document.getElementById("submit");
			obj.disabled = "disabled";
		}
	</script>
	<form action="" method="post" onsubmit="return doSubmit();">
		用户名：<input type="text" name="username" /><br />
		<input id="submit" type="submit" value="提交" />					
	</form>
	<!-- Javascript防重复提交不安全，易被攻破-->
```
- ②、服务器端session防表单重复提交：<br>
	表单页面由servlet程序生成，servlet为每次产生的表单页面分配一个唯一的随机标识号，并在FORM表单的一个隐藏字段中设置这个标识号，<br>
	同时在当前用户的Session域中保存这个标识号。当用户提交FORM表单时，负责处理表单提交的serlvet得到表单提交的标识号，并与session<br>
	中存储的标识号比较，如果相同则处理表单提交，处理完后清除当前用户的Session域中存储的标识号		

# 参考资料

* [Servlet生命周期与工作原理](https://my.oschina.net/xianggao/blog/395327)