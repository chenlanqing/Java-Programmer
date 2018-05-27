<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、Spring的整体架构](#%E4%B8%80spring%E7%9A%84%E6%95%B4%E4%BD%93%E6%9E%B6%E6%9E%84)
  - [1.1、Core Container](#11core-container)
  - [1.2、Data Access/Integration](#12data-accessintegration)
  - [1.3、Web](#13web)
  - [1.4、AOP](#14aop)
  - [1.5、Test](#15test)
- [二、IOC](#%E4%BA%8Cioc)
  - [1、IOC的生命周期](#1ioc%E7%9A%84%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F)
  - [2、ApplicationContext Bean 生命周期](#2applicationcontext-bean-%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F)
  - [3、BeanFactory Bean生命周期-面向Spring本身](#3beanfactory-bean%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F-%E9%9D%A2%E5%90%91spring%E6%9C%AC%E8%BA%AB)
  - [4、IOC容器的启动过程](#4ioc%E5%AE%B9%E5%99%A8%E7%9A%84%E5%90%AF%E5%8A%A8%E8%BF%87%E7%A8%8B)
  - [5、Bean加载过程](#5bean%E5%8A%A0%E8%BD%BD%E8%BF%87%E7%A8%8B)
- [三、AOP](#%E4%B8%89aop)
- [四、spring事务](#%E5%9B%9Bspring%E4%BA%8B%E5%8A%A1)
  - [1、Spring事务管理方式](#1spring%E4%BA%8B%E5%8A%A1%E7%AE%A1%E7%90%86%E6%96%B9%E5%BC%8F)
  - [2、Spring的事务特性](#2spring%E7%9A%84%E4%BA%8B%E5%8A%A1%E7%89%B9%E6%80%A7)
  - [3、Spring事务实现原理](#3spring%E4%BA%8B%E5%8A%A1%E5%AE%9E%E7%8E%B0%E5%8E%9F%E7%90%86)
- [五、相关面试题](#%E4%BA%94%E7%9B%B8%E5%85%B3%E9%9D%A2%E8%AF%95%E9%A2%98)
  - [1、Spring与SpringMVC父子容器配置](#1spring%E4%B8%8Espringmvc%E7%88%B6%E5%AD%90%E5%AE%B9%E5%99%A8%E9%85%8D%E7%BD%AE)
- [参考资料](#%E5%8F%82%E8%80%83%E8%B5%84%E6%96%99)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 一、Spring的整体架构
## 1.1、Core Container

核心容器，包含 Core、Beans、Context、Expression Language模块；Core和Beans模块是框架的基础部分，提供IOC和依赖注入特性.这里的基础概念是BeanFactory，它提供对Factory 模式的经典实现来消除对程序性单例模式的需要

- Core模块主要包含Spring框架基本的核心工具类，Spring的其他组件都要使用到这个包里的类，该模块是其他组件的基本核心；
- Beans模块是所有应用都要用到的，包含访问配置文件、创建和管理bean以及进行IOC和DI 操作相关的所有类；
- Context 模块构建于Core和Beans模块基础之上，提供了一种类似于 JNDI 注册器的框架式的对象访问方法。该模块继承了Beans的特性，为Spring核心提供了大量的扩展，添加对国际化、事件传播、资源加载和对Context的透明创建的支持. ApplicationContext 接口是 Context 模块的关键；
- Expression Language 模块提供了一个强大的表达式语言用于在运行时查询和操作对象

## 1.2、Data Access/Integration

包含JDBC、ORM、OXM、JMS和Transaction模块，其中JDBC模块提供了一个JDBC抽象层

## 1.3、Web
## 1.4、AOP
## 1.5、Test

# 二、IOC

## 1、IOC的生命周期

Spring的ioc容器功能非常强大，负责Spring的Bean的创建和管理等功能。BeanFactory和ApplicationContext是Spring两种很重要的容器，前者提供了最基本的依赖注入的支持，而后者在继承前者的基础进行了功能的拓展，例如增加了事件传播、资源访问和国际化的消息访问等功能；

## 2、ApplicationContext Bean 生命周期
![image](https：//github.com/chenlanqing/learningNote/blob/master/Java/Java源码解读/spring/image/ApplicationContext-Bean的生命周期.png)

面向开发者的，几乎大部分应用场景都是直接使用ApplicationContext 而非底层的BeanFactory

- （1）Bean的实例化：
	* 首先容器启动后，会对scope为singleton且非懒加载的bean进行实例化;
	* 容器在内部实现的时候，采用“策略模式”来决定采用何种方式初始化bean实例.通常，可以通过反射或者CGLIB动态字节码生成来初始化相应的bean实例或者动态生成其子类默认情况下，容器内部采用 CglibSubclassingInstantiationStartegy。容器只要根据相应bean定义的BeanDefinition取得实例化信息，结合CglibSubclassingInstantiationStartegy以及不同的bean定义类型，就可以返回实例化完成的对象实例.但不是直接返回构造完成的对象实例，而是以BeanWrapper对构	造完成的对象实例进行包裹，返回相应的BeanWrapper实例.这个BeanWrapper的实现类BeanWrapperImpl是对某个bean进行包裹，然后对包裹后的bean进行操作，比如设置或获取bean的相应属性值；

- （2）设置对象属性：<br>
	BeanWrapper继承了PropertyAccessor接口，可以以同一的方式对对象属性进行访问，同时又继承了PropertyEditorRegistry和TypeConverter接口，然后BeanWrapper就可以很方便地对bean注入属性了；

- （3）如果Bean实现了BeanNameAware接口，会回调该接口的setBeanName()方法，传入该bean的id，此时该Bean就获得了自己在配置文件中的id；

- （4）如果Bean实现了BeanFactoryAware接口，会回调该接口的setBeanFactory()方法，传入该Bean的BeanFactory，这样该Bean就获得了自己所在的BeanFactory

- （5）如果Bean实现了ApplicationContextAware接口，会回调该接口的setApplicationContext()方法，传入该Bean的ApplicationContext，这样该Bean就获得了自己所在的ApplicationContext

- （6）如果有一个Bean实现了BeanPostProcessor接口，并将该接口配置到配置文件中，则会调用该接口的postProcessBeforeInitialization()方法

- （7）如果Bean实现了InitializingBean接口，则会回调该接口的afterPropertiesSet()方法

- （8）如果Bean配置了init-method方法，则会执行init-method配置的方法；

- （9）如果有一个Bean实现了BeanPostProcessor接口，并将该接口配置到配置文件中，则会调用该接口的postProcessAfterInitialization方法；

- （10）经过步骤9之后，就可以正式使用该Bean了，对于scope为singleton的Bean，Spring IoC容器会缓存一份该Bean的实例，而对于scope为prototype的Bean，每次被调用都回new一个对象，而且生命周期也交给调用方管理了，不再是Spring容器进行管理了；

- （11）容器关闭后，如果Bean实现了DisposableBean接口，则会调用该接口的destroy()方法；

- （12）如果Bean配置了destroy-method方法，则会执行destroy-method配置的方法.至此，整个Bean生命周期结束；

## 3、BeanFactory Bean生命周期-面向Spring本身

![image](https：//github.com/chenlanqing/learningNote/blob/master/Java/Java源码解读/spring/image/BeanFactory.png)

BeanFactoty容器中， Bean的生命周期如上图所示，与ApplicationContext相比，有如下几点不同：

- BeanFactory容器中，不会调用ApplicationContextAware接口的setApplicationContext()方法
- BeanPostProcessor接口的postProcessBeforeInitialization方法和postProcessAfterInitialization方法不会自动调用，必须自己通过代码手动注册
- BeanFactory容器启动的时候，不会去实例化所有bean，包括所有scope为singleton且非延迟加载的bean也是一样，而是在调用的时候去实例化

## 4、IOC容器的启动过程

### 4.1、web环境下Spring\SpringMVC容器启动过程

- （1）对于一个web应用，其部署在web容器中，web容器提供一个全局的上下文环境，即ServletContext，其为后面的SpringIOC容器提宿主环境；

- （2）web.xml中配置ContextLoaderListener.在web容器启动时，会触发容器初始化事件，ContextLoaderListener会监听到这个事件，其contextInitialized()方法被调用，在这个方法中，spring会初始化一个启动上下文，这个上下文被称为根上下文，即WebApplicationContext.其实际实现类是XmlWebApplicationContext。这个就是Spring的IOC容器.其对应的Bean定义的配置由web.xml中的context-param标签指定.在这个IoC容器初始化完毕后，spring容器以WebApplicationContext.ROOTWEBAPPLICATIONCONTEXTATTRIBUTE为属性Key，将其存储到ServletContext中，便于获取；

- （3）ContextLoaderListener监听器初始化完毕后，始初始化web.xml中配置的Servlet，可以有多个。以最常见的DispatcherServlet为例（Spring MVC，这个servlet实际上是一个标准的前端控制器，用以转发、匹配、处理每个servlet请求。DispatcherServlet上下文在初始化的时候会建立自己的IoC上下文容器，用以持有spring mvc相关的bean，这个servlet自己持有的上下文默认实现类也是XmlWebApplicationContext.在建立DispatcherServlet自己的IoC上下文时，会利用WebApplicationContext.ROOTWEBAPPLICATIONCONTEXTATTRIBUTE先从ServletContext中获取之前的根上下文（即WebApplicationContext)作为自己上下文的parent上下文）即第2步中初始化的XmlWebApplicationContext作为自己的父容器）.有了这个parent上下文之后，再初始化自己持有的上下文（这个DispatcherServlet初始化自己上下文的工作在其initStrategies方法中可以看到，大概的工作就是初始化处理器映射、视图解析等）。初始化完毕后，spring以与servlet的名字相关（此处不是简单的以servlet名为Key，而是通过一些转换）的属性为属性Key，也将其存到ServletContext中，以便后续使用.这样每个servlet就持有自己的上下文，即拥有自己独立的bean空间，同时各个servlet共享相同的bean，即根上下文定义的那些bean

## 5、Bean加载过程
![image](https：//github.com/chenlanqing/learningNote/blob/master/Java/Java源码解读/spring/image/Spring-Bean加载过程.png)

- ResourceLoader从存储介质中加载Spring配置信息，并使用Resource表示这个配置文件的资源。

- BeanDefinitionReader读取Resource所指向的配置文件资源，然后解析配置文件。配置文件中每一个<bean>解析成一个BeanDefinition对象，并保存到BeanDefinitionRegistry中；

- 容器扫描BeanDefinitionRegistry中的BeanDefinition，使用Java的反射机制自动识别出Bean工厂后处理后器（实现BeanFactoryPostProcessor接口）的Bean，然后调用这些Bean工厂后处理器对BeanDefinitionRegistry中的BeanDefinition进行加工处理.主要完成以下两项工作：
	* 对使用到占位符的<bean>元素标签进行解析，得到最终的配置值，这意味对一些半成品式的
		BeanDefinition对象进行加工处理并得到成品的BeanDefinition对象;
	* 对BeanDefinitionRegistry中的BeanDefinition进行扫描，通过Java反射机制找出所有属性编辑器的Bean（实现java.beans.PropertyEditor接口的Bean），并自动将它们注册到Spring容器的属性编辑器注册表中（PropertyEditorRegistry）

- Spring容器从BeanDefinitionRegistry中取出加工后的BeanDefinition，并调用InstantiationStrategy着手进行Bean实例化的工作；

- 在实例化Bean时，Spring容器使用BeanWrapper对Bean进行封装，BeanWrapper提供了很多以Java反射机制操作Bean的方法，它将结合该Bean的BeanDefinition以及容器中属性编辑器，完成Bean属性的设置工作；

- 利用容器中注册的Bean后处理器(实现BeanPostProcessor接口的Bean)对已经完成属性设置工作的Bean进行后续加工，直接装配出一个准备就绪的Bean

# 三、AOP


# 四、spring事务

## 1、Spring事务管理方式

- 编程式事务：使用TransactionTemplate，粒度控制在代码块，手动提交
- 声明式事务：xml，注解，粒度只能控制在public方法中

## 2、Spring的事务特性

### 2.1、Spring的事务管理策略

都是基于 org.springframework.transaction.PlatformTransactionManager一般使用的都是 DataSourceTransactionManager，也就是基于数据源的事务管理。DataSourceTransactionManager 实现了两个接口：PlatformTransactionManager和InitializingBean

* 实现了PlatformTransactionManager说明这个类主要功能是进行事务管理;
* 实现了InitializingBean接口，DataSourceTransactionManager进行事务管理的前提是DataSource已经成功注入.

TransactionDefinition接口里面定义了事务的隔离级别和事务的传播行为

### 2.2、Spring的事务隔离级别

在Spring的事务管理中一样，TransactionDefinition定义了5种隔离级别

```java
//底层数据库默认的隔离级别，这个与具体数据库有关系
int ISOLATION_DEFAULT = -1;
// 未提交读
int ISOLATION_READ_UNCOMMITTED = Connection.TRANSACTION_READ_UNCOMMITTED;
// 提交读：只能读取别人commit了的数据
int ISOLATION_READ_COMMITTED = Connection.TRANSACTION_READ_COMMITTED;
// 可重复读：存在幻读，不过MySQL通过MVCC解决这个问题
int ISOLATION_REPEATABLE_READ = Connection.TRANSACTION_REPEATABLE_READ;
// 串行化
int ISOLATION_SERIALIZABLE = Connection.TRANSACTION_SERIALIZABLE;
```

### 2.3、Spring事务传播行为

事务传播行为是指如果在开始当前事务之前，一个事务上下文已经存在了，此时有若干选项可以指定一个事务性方法的执行行为。在TransactionDefinition中同样定义了如下几种事务传播行为

```java
// 如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。这是默认值。
int PROPAGATION_REQUIRED = 0;
// 如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。
int PROPAGATION_SUPPORTS = 1;
// 如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常。
int PROPAGATION_MANDATORY = 2;
// 创建一个新的事务，如果当前存在事务，则把当前事务挂起。
int PROPAGATION_REQUIRES_NEW = 3;
// 以非事务方式运行，如果当前存在事务，则把当前事务挂起。
int PROPAGATION_NOT_SUPPORTED = 4;
// 以非事务方式运行，如果当前存在事务，则抛出异常。
int PROPAGATION_NEVER = 5;
// 如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；
// 如果当前没有事务，则该取值等价于TransactionDefinition.PROPAGATION_REQUIRED。
int PROPAGATION_NESTED = 6;
```	
### 2.4、事务超时时间

一个事务允许执行的最长时间，如果超过这个限制但是事务还没有完成，则自动回滚事务。在TransactionDefinition 以int值表示超时时间，其单位是秒。默认设置为底层事务系统的超时值，如果底层数据库事务系统没有设置超时值，那么就是none，没有超时限制；

### 2.5、事务回滚规则

Spring事务管理器会捕捉任何未处理的异常，然后依据规则决定是否回滚抛出异常的事务。默认配置：spring只有在抛出的异常为运行时unchecked异常时才回滚该事务，也就是抛出的异常为RuntimeException的子类(Errors也会导致事务回滚)，而抛出checked异常则不会导致事务回滚

## 3、Spring事务实现原理

# 五、相关面试题

## 1、Spring与SpringMVC父子容器配置

- Spring和SpringMVC共存时，会有两个容器：一个SpringMVC的ServletWebApplicationContext为子容器，一个Spring的RootWebApplicationContext为父容器。当子容器中找不到对应的Bean会委托于父容器中的Bean。
	* RootWebApplicationContext中的Bean对ServletWebApplicationContext可见，而ServletWebApplicationContext中的Bean对RootWebApplicationContext不可见。
- 如果在父容器中开启了 @AspectJ 注解与事务配置，子容器和父容器均加载了所有Bean。造成子容器中的services覆盖了父容器的Services，导致父容器中的动态代理的services不生效，事务也不生效。

解决上述问题，可以由父容器加载所有Bean，子容器不加载任何Bean


# 参考资料

* [Spring AOP原理](https：//mp.weixin.qq.com/s/f-Nnov2knru68KT6gWtvBQ)
* [源码解读Spring IOC原理](https：//www.cnblogs.com/ITtangtang/p/3978349.html)
* [tiny-spring](https：//github.com/code4craft/tiny-spring)
* [源代码](https：//github.com/spring-projects/spring-framework)
* [IoC容器及Bean的生命周期](https：//www.cnblogs.com/IvySue/p/6484599.html)
* [IOC容器源码分析](https：//javadoop.com/post/spring-ioc)
* [SpringIOC原理](https：//zhuanlan.zhihu.com/p/29344811)
* [Spring加载应用程序Bean类分析](https://blog.csdn.net/u013095337/article/details/53609398)