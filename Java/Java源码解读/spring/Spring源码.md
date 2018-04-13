<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一.Spring 的整体架构:](#%E4%B8%80spring-%E7%9A%84%E6%95%B4%E4%BD%93%E6%9E%B6%E6%9E%84)
  - [1.1.Core Container:](#11core-container)
  - [1.2.Data Access/Integration:包含 JDBC,ORM,OXM,JMS 和 Transaction 模块](#12data-accessintegration%E5%8C%85%E5%90%AB-jdbcormoxmjms-%E5%92%8C-transaction-%E6%A8%A1%E5%9D%97)
  - [1.3.Web](#13web)
  - [1.4.AOP](#14aop)
  - [1.5.Test](#15test)
- [二.IOC](#%E4%BA%8Cioc)
  - [1.IOC的生命周期:](#1ioc%E7%9A%84%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

参考文章：
* [源码解读Spring IOC原理](https://www.cnblogs.com/ITtangtang/p/3978349.html)
* [tiny-spring](https://github.com/code4craft/tiny-spring)
* [源代码](https://github.com/spring-projects/spring-framework)
* [IoC容器及Bean的生命周期](https://www.cnblogs.com/IvySue/p/6484599.html)

# 一.Spring 的整体架构:
## 1.1.Core Container:
	核心容器,包含 Core、Beans、Context、Expression Language 模块
	Core 和 Beans 模块是框架的基础部分,提供 IOC 和依赖注入特性.这里的基础概念是 BeanFactory,它提供对 Factory 
	模式的经典实现来消除对程序性单例模式的需要
	(1).Core 模块主要包含 Spring 框架基本的核心工具类,Spring 的其他组件都要使用到这个包里的类,该模块是
		其他组件的基本核心;
	(2).Beans 模块是所有应用都要用到的,包含访问配置文件、创建和管理 bean 以及进行 IOC和DI 操作相关的所有类
	(3).Context 模块构建于 Core 和 Beans 模块基础之上,提供了一种类似于 JNDI 注册器的框架式的对象访问方法.
		该模块继承了 Beans 的特性,为 Spring 核心提供了大量的扩展,添加对国际化、事件传播、资源加载和对Context的
		透明创建的支持. ApplicationContext 接口是 Context 模块的关键.
	(4).Expression Language 模块提供了一个强大的表达式语言用于在运行时查询和操作对象
## 1.2.Data Access/Integration:包含 JDBC,ORM,OXM,JMS 和 Transaction 模块
	(1).JDBC 模块提供了一个JDBC抽象层
## 1.3.Web
## 1.4.AOP
## 1.5.Test

# 二.IOC
## 1.IOC的生命周期:
	Spring的ioc容器功能非常强大,负责Spring的Bean的创建和管理等功能.
	BeanFactory和ApplicationContext是Spring两种很重要的容器,前者提供了最基本的依赖注入的支持,而后者在继承
	前者的基础进行了功能的拓展,例如增加了事件传播、资源访问和国际化的消息访问等功能.
### 1.1.ApplicationContext Bean 生命周期:
![image](https://github.com/chenlanqing/learningNote/blob/master/Java/Java源码解读/spring/image/ApplicationContext-Bean的生命周期.png)

	(1).Bean的实例化:
		* 首先容器启动后,会对scope为singleton且非懒加载的bean进行实例化;
		* 容器在内部实现的时候,采用“策略模式”来决定采用何种方式初始化bean实例.通常,可以通过反射或者CGLIB动态字
		节码生成来初始化相应的bean实例或者动态生成其子类.默认情况下,容器内部采用 CglibSubclassingInstantiationStartegy.
		容器只要根据相应bean定义的BeanDefinition取得实例化信息,结合CglibSubclassingInstantiationStartegy以及
		不同的bean定义类型,就可以返回实例化完成的对象实例.但不是直接返回构造完成的对象实例,而是以BeanWrapper对构
		造完成的对象实例进行包裹,返回相应的BeanWrapper实例.这个BeanWrapper的实现类BeanWrapperImpl是对某个bean
		进行包裹,然后对包裹后的bean进行操作,比如设置或获取bean的相应属性值;
	(2).设置对象属性:
		BeanWrapper继承了PropertyAccessor接口,可以以同一的方式对对象属性进行访问,同时又继承了PropertyEditorRegistry
		和TypeConverter接口,然后BeanWrapper就可以很方便地对bean注入属性了;
	(3).如果Bean实现了BeanNameAware接口,会回调该接口的setBeanName()方法,传入该bean的id,
		此时该Bean就获得了自己在配置文件中的id;
	(4).如果Bean实现了BeanFactoryAware接口,会回调该接口的setBeanFactory()方法,传入该Bean的BeanFactory,
		这样该Bean就获得了自己所在的BeanFactory
	(5).如果Bean实现了ApplicationContextAware接口,会回调该接口的setApplicationContext()方法,传入该Bean的
		ApplicationContext,,这样该Bean就获得了自己所在的ApplicationContext.
	(6).如果有一个Bean实现了BeanPostProcessor接口,并将该接口配置到配置文件中,则会调用该接口的
		postProcessBeforeInitialization()方法
	(7).如果Bean实现了InitializingBean接口,则会回调该接口的afterPropertiesSet()方法
	(8).如果Bean配置了init-method方法,则会执行init-method配置的方法;
	(9).如果有一个Bean实现了BeanPostProcessor接口,并将该接口配置到配置文件中,则会调用该接口的
		postProcessAfterInitialization方法;
	(10).经过步骤9之后,就可以正式使用该Bean了,对于scope为singleton的Bean,,Spring IoC容器会缓存一份该
		Bean的实例,而对于scope为prototype的Bean,每次被调用都回new一个对象,而且生命周期也交给调用方管理了,
		不再是Spring容器进行管理了;
	(11).容器关闭后,如果Bean实现了DisposableBean接口,则会调用该接口的destroy()方法;
	(12).如果Bean配置了destroy-method方法,则会执行destroy-method配置的方法.至此,整个Bean生命周期结束
### 1.2.BeanFactory Bean生命周期
![image](https://github.com/chenlanqing/learningNote/blob/master/Java/Java源码解读/spring/image/BeanFactory.png)

	BeanFactoty容器中, Bean的生命周期如上图所示,与ApplicationContext相比,有如下几点不同：
	(1).BeanFactory容器中,不会调用ApplicationContextAware接口的setApplicationContext()方法
	(2).BeanPostProcessor接口的postProcessBeforeInitialization方法和postProcessAfterInitialization方
		法不会自动调用，必须自己通过代码手动注册
	(3).BeanFactory容器启动的时候,不会去实例化所有bean,包括所有scope为singleton且非延迟加载的bean也是一样,
		而是在调用的时候去实例化


