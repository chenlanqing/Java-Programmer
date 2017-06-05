一.MVC简介
1.MVC核心思想:业务数据的抽取同业务数据呈现相分离
	(1).前端控制器:
2.MVC:
	model:视图层,业务数据的信息显示,关注支撑业务的信息构成,通常是多个业务实体的组合
	view:视图层,为用户提供UI,关注数据的显示
	controller:控制层,调用业务逻辑产生合适的数据,并传递给视图层呈现
	(1).什么是MVC:
		①.MVC是一种架构模式:程序分工,分工合作,既相互独立又协同工作
		②.MVC是一种思考模式:需要展示哪些数据给用户,如何布局,调用哪些业务逻辑
3.常用的MVC框架：
(1).运行性能比较:
	Jsp+Servlet > struts1 > SpringMVC > struts+freemarker > struts2 + OGNL值栈
	开放效率正好相反
	Struts2的性能低的原因是因为OGNL和值栈造成的。所以，如果你的系统并发量高，可以使用freemaker进行显示，而不是采用OGNL和值栈

二.SpringMVC
1.DispatcherServlet:
2.Controller



































































































