一、Java中常用的日志方式有三种：
	jdk自带、common-logging、Log4j、Slf4j、Logback

1、common-logging：
	1.1、概念：是apache提供的一个通用的日志接口。用户可以自由选择第三方的日志组件作为具体实现，像log4j，或者jdk自带的logging， 
		common-logging会通过动态查找的机制，在程序运行时自动找出真正使用的日志库。当然，common-logging内部有一个Simple logger的简单实现，但是功能很弱。
		所以使用common-logging，通常都是配合着log4j来使用。使用它的好处就是，代码依赖是common-logging而非log4j， 避免了和具体的日志方案直接耦合，
		在有必要时，可以更改日志实现的第三方库。
		
	1.2、常用代码
		import org.apache.commons.logging.Log;
		import org.apache.commons.logging.LogFactory;

		public class A {
			private static Log logger = LogFactory.getLog(this.getClass());
		}
	
	1.3、动态查找原理：Log 是一个接口声明。LogFactory 的内部会去装载具体的日志系统，并获得实现该Log 接口的实现类。LogFactory 内部装载日志系统的流程如下：
		(1)、首先，寻找org.apache.commons.logging.LogFactory 属性配置。
		(2)、否则，利用JDK1.3 开始提供的service 发现机制，会扫描classpah 下的META-INF/services/org.apache.commons.logging.LogFactory文件，
		若找到则装载里面的配置，使用里面的配置。
		(3)、否则，从Classpath 里寻找commons-logging.properties ，找到则根据里面的配置加载。
		(4)、否则，使用默认的配置：如果能找到Log4j 则默认使用log4j 实现，如果没有则使用JDK14Logger 实现，再没有则使用commons-logging 内部提供的SimpleLog 实现。


2、Log4j
	