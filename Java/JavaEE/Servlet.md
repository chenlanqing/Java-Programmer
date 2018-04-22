<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一.Servlet](#%E4%B8%80servlet)
- [二、HTTP协议](#%E4%BA%8Chttp%E5%8D%8F%E8%AE%AE)
- [三、Servlet高级应用:](#%E4%B8%89servlet%E9%AB%98%E7%BA%A7%E5%BA%94%E7%94%A8)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 一.Servlet
## 1.不使用开发工具开发一个servlet
	(1)在tomcat服务器的webapps下新建一个目录test(应用名),在test文件夹内再新加一个WEB-INF文件夹,
	再在WEB-INF文件夹内新建一个classes文件夹;\apache-tomcat-6.0.14\webapps\test\WEB-INF\classes
	(2)在classes文件夹新建一个SecondServlet.java文件,如下代码:
		package cn.tarena;
		import java.io.*;
		import javax.servlet.*;
		import javax.servlet.http.*;
		public class SecondServlet extends HttpServlet{
			
			public void service(HttpServletRequest req,HttpServletResponse res)throws ServletException,IOException{
				//PrintWriter out = response.getWriter();
				PrintWriter out = res.getWriter();
				out.println("实现HttpServlet......................");
			}
		}
	(3)找到servlet-api.jar文件,复制到当前classes文件夹中,编译java文件:
		javac -cp servlet-api.jar -d . SecondServlet.java---.表示当前目录;
	(4)配置web.xml文件:在WEB-INF文件夹下新建文件web.xml,添加如下配置:
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
	(5)访问该servlet:启动tomcat服务器,地址栏输入	:localhost:8080/test/secondServlet
## 2.load-on-startup:
	服务器启动时初始化servlet，如果在<servlet>元素中配置了一个<load-on-startup>元素，
	那么WEB应用程序在启动时，就会装载并创建Servlet的实例对象、以及调用Servlet实例对象的init()方法。
		举例：
		<servlet>
			<servlet-name>invoker</servlet-name>
			<servlet-class>
				org.apache.catalina.servlets.InvokerServlet
			</servlet-class>
			<load-on-startup>2</load-on-startup>
		</servlet>
	是一个正整数，数字越小，先启动。
	用途：为web应用写一个InitServlet，这个servlet配置为启动时装载，为整个web应用创建必要的数据库表和数据

## 3.servlet-mapping
	如果某个Servlet的映射路径仅仅为一个正斜杠("/")，那么这个Servlet就成为当前Web应用程序的缺省Servlet。
	凡是在web.xml文件中找不到匹配的<servlet-mapping>元素的URL，它们的访问请求都将交给缺省Servlet处理，
	也就是说，缺省Servlet用于处理所有其他Servlet都不处理的访问请求。
	在<tomcat的安装目录>\conf\web.xml文件中，注册了一个名称为:
	org.apache.catalina.servlets.DefaultServlet的Servlet，并将这个Servlet设置为了缺省Servlet。
	当访问Tomcat服务器中的某个静态HTML文件和图片时，实际上是在访问这个缺省Servlet

## 4.Servlet线程安全问题:
	
## 5.Servlet初始化参数:
	ServletConfig接口,没有固定值的数据都可以通过配置方式:
	如数据库,字符集编码;
	在web.xml文件中配置参数:(可以配置多个<init-param>),这里是为每个Servlet配置初始化参数
		<init-param>
			<param-name>country</param-name>
			<param-value>China</param-value>
		</init-param>

	在Servlet中获取值:
		//根据配置参数名获取参数值
		String value = this.getServletConfig().getInitParameter("country");
		//获取配置中所有初始化参数
		Enumeration e = this.getServletConfig().getInitParameterNames();
	★ServletConfig对象用于封装servlet的配置信息
	
## 6.ServletContext:可以通过ServletContext来实现多个servlet的资源共享
	ServletContext context = this.getServletContext();
	①ServletContext域:这是一个容器,说明了该容器的作用范围,也就是应用程序范围;
	②可以通过给Web应用配置全局初始化参数
		<context-param>
			<param-name>country</param-name>
			<param-value>China</param-value>
		</context-param>
	获取:this.getServletContext().getInitParameter("country");
	③管理web资源文件(.xml	.properties)
		Ⅰ.读取.properties文件:
			InputStream in = this.getServletContext().getResourceAsStream("/WEB-INF/classes/db.properties");
			Properties props = new Properties();
			props.load(in);
		★★★注意:★★★
			FileInputStream input = new FileInputStream("classes/db.properties");
			这里流的读取文件的路径是相对路径,在Java中,其是相对与JVM的路径;
			在Web开发中尤其要注意,"classes/db.properties"这样写是相对于服务器里的目录;
			以tomcat为例:
				这里就是相当于tomcat/bin目录的,完整应该是:/bin/classes/db.properties,
				因此在Web开发中读取配置文件最好使用ServletContext来读取;
			◆还有一种方法获取配置文件在服务器上的绝对路径:
				String path = this.getServletContext().getRealPath("/WEB-INF/classes/db.properties");
				那么就可以使用流来读取了:
				FileInputStream input = new FileInputStream(path);
			◆可以使用类装载器来实现配置文件的读取(不在Servlet中读取)
				InputStream input = Demo2.class.getClassLoader().getResourceAsStream("db.properties");
				这里使用类装载器来加载配置文件,那么这里将不能读取db.properties更新后的数据;
				如果需要读取更新后的数据:
				使用类加载器来获取配置文件的路径
					String path = Demo2.class.getClassLoader().getResource("db.properties");
					FileInputStream input = new FileInputStream(path);

## 7.HttpServletResponse:服务器的响应(注意乱码问题)
	//设置response使用的编码格式,以控制其输出到浏览器的编码
	response.setCharacterEncoding("UTF-8"); 
	//指定浏览器以什么编码打开服务器发送的数据
	response.setHeader("content-type", "text/html;charset=UTF-8");
	①控制浏览器定时刷新
		response.setHeader("refresh", "3;url=指向地址");//每隔3s刷新页面
		◆用户注册成功,自动跳转页面,可以用此来实现
	②控制缓存:
		控制缓存时间:
		response.setDateHeader("expires",System.currentTimeMillis() + 1000*3600 );
		禁止浏览器缓存:
		response.setDateHeader("expires",-1);
	③重定向:服务器发送一个302状态和一个location消息头(值是一个地址,称为重定向地址)
		response.setStatus(302);
		response.setHeader("location","url...");
		或者:
		response.sendRedirect(url);
		◆特点:
			Ⅰ.浏览器会向服务器发送两次请求;
			用户登录和显示购物车时通常会用到重定向技术;
		★注意:
			response.getWriter()与response.getOutputStream()不能同时使用在一个Servlet(包括转发),
			否则会出现异常:
			java.lang.IllegalStateException: getWriter() has already been called for this response
			重定向不会出现这种问题,因为其是两次响应;
	④获取的输出流,可以不去处理吗,Servlet会自动去管理关闭这些流;
## 8.HttpServletResquest:代表客户端的请求
	(1).乱码问题:
		可以修改服务器的配置,如tomcat,可以加上属性:URIEncoding="utf-8"
		<Connector port="8088" protocol="HTTP/1.1" 
			connectionTimeout="20000" 
			redirectPort="8443" URIEncoding="utf-8" />
		
	(2).请求转发:	
		
## 9.状态管理:
	(1).cookie:
		注意：删除cookie时，如果原cookie有setPath,那么在删除cookie时也要设置path
	
	(2).Session:
		◆如何实现多个IE浏览器共享同一session？(应用：关掉IE后，再开IE，上次购买的商品还在。)
			//利用Cookie把sessionId带给浏览器
			HttpSession session=request.getSession();
			session.setAttribute("name", "洗衣机");
			String id=session.getId();
			Cookie cookie=new Cookie("JSESSIONID",id);
			cookie.setPath("/day07");
			cookie.setMaxAge(30*60);//30 minutes
			response.addCookie(cookie);
	(3).防止表单重复提交
		①.客户端防表单重复提交:(使用Javascript来实现) /servlet/RepeatCommit
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
				//还可以在提交之后,将提交按钮置为disabled
				function doSubmit(){
					var obj = document.getElementById("submit");
					obj.disabled = "disabled";
				}
			</script>
			<form action="" method="post" onsubmit="return doSubmit();">
				用户名：<input type="text" name="username" /><br />
				<input id="submit" type="submit" value="提交" />					
			</form>
			★Javascript防重复提交不安全,易被攻破
		②.服务器端session防表单重复提交:
			表单页面由servlet程序生成，servlet为每次产生的表单页面分配一个唯一的随机标识号，
			并在FORM表单的一个隐藏字段中设置这个标识号，同时在当前用户的Session域中保存这个标识号。 
			当用户提交FORM表单时，负责处理表单提交的serlvet得到表单提交的标识号，并与session中存储的
			标识号比较,如果相同则处理表单提交,处理完后清除当前用户的Session域中存储的标识号

## 10.Servlet 与 Servlet容器	
	Servlet 与 Servlet 容器的关系:
	可以看做是子弹与枪的关系.虽然彼此依存,但是相互发展.从技术角度来看是为了解耦,通过标准化接口来相互协作.
### 10.1.Servlet容器
	(1).Tomcat 容器登记中,Context 容器是直接管理Servlet在容器中的包装类Wrapper.
		一个Context对应一个web工程,在Tomcat的配置文件中可以发现这一点:
		<Context path="/projectOne " docBase="\user\projects\projectOne" reloadable="true" />
	(2).
# 二、HTTP协议			
	1.在HTTP1.0协议中，客户端与web服务器建立连接后，只能获取一个web资源；
	  在HTTP1.1协议中，允许客户端与web服务器建立连接后，在一个连接上获取多个web资源；
	  telnet localhost 8080：连接本机的tomcat服务器
		GET /aa/1.html HTTP/1.1
		Host:	
	★设计页面：减少客户端对服务器的访问次数；
	2.HTTP请求:一个完整的HTTP请求包括:一个请求行,若干请求头,以及实体内容;
		(1).请求行:GET /aa/1.html HTTP/1.1  包含请求方式,请求资源,协议版本
	3.HTTP响应:包括一个状态行,若干消息,以及实体内容	
	4.Range头：HTTP请求头字段，指示服务器只传输一部分web资源，这个头可以用来实现断点续传;
		Range:byte=1000-2000,表示传输范围从1000到2000字节;
		Range:byte=1000-,表示传输web资源第1000个字节以后的所有内容;
		Range:byte=1000,表示传输最后1000个字节;
			
# 三、Servlet高级应用:
## 1.监听器：
	(1).servlet规范当中规定的一种特殊的组件,用于监听servlet容器产生的事件并进行相应的处理
	容器产生的事件主要有两大类:
		a:声明周期相关的事件:容器创建或者销毁request,session,servlet上下文是产生的事件
		b:绑定的相关事件,容器调用了request,session,servlet上下文的setAttribute,removeAttribute时产生的事件
	(2).监听器应用:
		统计在线人数和在线用户;
		系统启动时加载初始化信息;
		统计网站访问量;
		结合Spring使用;
	(3).servlet组件启动顺序:
		监听器 ----> 过滤器 ----> Servlet
	(4).监听器分类:
		(4.1)按监听的对象分类:
			A:用于监听应用程序环境对象(ServletContext)的事件监听器;
			B:用于监听用户会话对象(HttpSession)的事件监听器;
			C:用于监听请求消息对象(ServletRequest)的事件监听器
		(4.2)按监听的事件划分:
			A:监听域对象自身的创建与销毁;ServletContext,HttpSession,ServletRequest
			B:监听域对象的属性增加与删除;
			C:监听绑定到HttpSession域中的某个对象的状态;
	(5).监听域对象自身的创建与销毁
		(5.1)ServletContext:  需实现:ServletContextListener,有两个事件处理方法

			创建或销毁ServletContext对象;
			主要用途:定时器,全局属性对象
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
		(5.2)HttpSession对象: 实现了HttpSessionListener
			@Override
			public void sessionCreated(HttpSessionEvent se) {
				// session创建时调用
			}
			@Override
			public void sessionDestroyed(HttpSessionEvent se) {
				// session销毁时调用
			}
		(5.3)ServletRequest对象: 实现了ServletRequestListener
			@Override
			public void requestDestroyed(ServletRequestEvent sre) {
				
			}

			@Override
			public void requestInitialized(ServletRequestEvent sre) {

			}
			
	(6).监听域对象的属性增加与删除;	
		实现接口:
			ServletContextAttributeListener : 属性的增加与删除
			HttpSessionAttributeListener : Session属性的增加与删除;
			ServletRequestAttributeListener : 请求参数的增加与删除
			
	(7).绑定到HttpSession域中的对象状态的事件监听器
	
## 2.过滤器:(Filter)不能直接处理请求
	服务器端的组件,其可以截取用户端的请求与响应信息,并对这些信息过滤;
	2.1.工作原理:
		过滤器将用户请求发送到web资源,web资源发送响应到过滤器,过滤器将web资源发送给用户;
	2.2.生命周期:
		实例化 ---> web.xml(实例化一次)
		初始化 ---> init()(执行一次)
		过滤   ---> doFilter()(多次执行)
		销毁   ---> destroy()(web容器关闭)
	2.3.实现: implements Filter
	
	2.4.过滤器链:服务器按照web.xml定义的先后顺序将过滤器组装成一条链
	
	2.5.过滤器分类
		(1).request:用户直接访问页面时,web容器会调用过滤器
		(2).forward:目标资源是RequestDispatcher	的forward访问时,该过滤器被调用;
		(3).include:目标资源是RequestDispatcher	的include访问时,该过滤器被调用;
		(4).error :目标资源是通过声明式异常处理机制调用时,过滤器将被调用 
		(5).async: 异步调用资源------Servlet3.0
			@WebFilter
	
	2.6.过滤器应用:
		(1).对用于请求进行统一认证;
		(2).编码转换
		(3).对用户发送的数据进行过滤转换;
		(4).转换图像格式;
		(6).对响应内容进行压缩;
		通过FilterConfig来获取初始化参数
		
