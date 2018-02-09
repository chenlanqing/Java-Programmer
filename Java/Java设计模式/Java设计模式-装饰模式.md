<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [1.基本概念:](#1%E5%9F%BA%E6%9C%AC%E6%A6%82%E5%BF%B5)
- [2.用途:](#2%E7%94%A8%E9%80%94)
- [3.用途:](#3%E7%94%A8%E9%80%94)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

#### 1.基本概念:
	装饰模式是在不必改变原类文件和使用继承的情况下,动态的扩展一个对象的功能.
	它是通过创建一个包装对象,也就是装饰来包裹真实的对象
	==> 注意几点
	(1).不改变原类文件
	(2).不使用继承
	(3).动态扩展
#### 2.用途:

#### 3.用途:
	3.3.深入剖析 InputStream 中的装饰模式:
		从文件图中(InputStream类图.png)可以看出:
		(1).抽象组件:InputStream,这是一个抽象类,为各种子类型提供统一的接口;
		(2).具体组件:ByteArrayInputStream、FileInputStream、PipedInputStream、
			StringBufferInputStream(JDK8 以后过期)等,它们实现了抽象组件所规定的接口;
		(3).抽象装饰者:FilterInputStream,它实现了 InputStream 所规定的接口;
		(4).具体装饰者:BufferedInputStream、DataInputStream 以及两个不常用到的类
			LineNumberInputStream(JDK8 以后过期)、PushbackInputStream 等;
		注意:
			①.InputStream 类型中的装饰模式是半透明的;
			②.PushbackInputStream 是一个半透明的装饰类,这个装饰类提供了额外的方法unread(),
				换言之,它破坏了理想的装饰模式的要求如果客户端持有一个类型为 InputStream 对象的
				引用in的话,那么如果in的真实类型是 PushbackInputStream的话,只要客户端不需要使用
				unread()方法,那么客户端一般没有问题.但是如果客户端必须使用这个方法,就必须进行向
				下类型转换.将in的类型转换成为 PushbackInputStream 之后才可能调用这个方法.但是,
				这个类型转换意味着客户端必须知道它拿到的引用是指向一个类型为 PushbackInputStream 
				的对象.这就破坏了使用装饰模式的原始用意;
	3.2.深入剖析 OutputStream 中的装饰模式:
		从文件图中:(OutputStream类图.png)可以看出:
		(1).抽象组件:OutputStream,这是一个抽象类,为各种子类型提供统一的接口;
		(2).具体组件:ByteArrayOutputStream、FileOutputStream、ObjectOutputStream、
			PipedOutputStream 等,它们实现了抽象组件所规定的接口;
		(3).抽象装饰者:FilterOutputStream,它实现了 OutputStream 所规定的接口;
		(4).具体装饰者:BufferedOutputStream、CheckedOutputStream、CipheOutputSteam、DataOutputStream 等,
	3.3.字符输入流:
		(1).抽象组件:Reader,这是一个抽象类,为各种子类型提供统一的接口;
		(2).具体组件:CharArayReader、FilterReader、InputStreamReader、PipedReader、StringReader 等,
			它们实现了抽象组件所规定的接口;
		(3).抽象装饰者:BufferedReader 、FilterReader、InputStreamReader,它实现了Reader所规定的接口;
		(4).具体装饰者:LineNumberReader、PushbackReader、FileReader 等;
	3.4.字符输出流:
		(1).抽象组件:Writer,这是一个抽象类,为各种子类型提供统一的接口;
		(2).具体组件:BufferedWriter、CharArrayWriter、FilterWriter、OutputStreamWriter、PipedWriter、
			PrintWriter、StringWriter 等,它们实现了抽象组件所规定的接口;
		(3).抽象装饰者:OutputStreamWriter,它实现了 Writer 所规定的接口;
		(4).具体装饰者:FileWriter;


















