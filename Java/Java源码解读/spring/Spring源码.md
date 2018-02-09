<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [1.Spring 的整体架构:](#1spring-%E7%9A%84%E6%95%B4%E4%BD%93%E6%9E%B6%E6%9E%84)
- [2.](#2)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

#### 1.Spring 的整体架构:
	1.1.Core Container:核心容器,包含 Core、Beans、Context、Expression Language 模块
		Core 和 Beans 模块是框架的基础部分,提供 IOC 和依赖注入特性.这里的基础概念是 BeanFactory,它提供对 Factory 
		模式的经典实现来消除对程序性单例模式的需要
		(1).Core 模块主要包含 Spring 框架基本的核心工具类,Spring 的其他组件都要使用到这个包里的类,该模块是
			其他组件的基本核心;
		(2).Beans 模块是所有应用都要用到的,包含访问配置文件、创建和管理 bean 以及进行 IOC和DI 操作相关的所有类
		(3).Context 模块构建于 Core 和 Beans 模块基础之上,提供了一种类似于 JNDI 注册器的框架式的对象访问方法.
			该模块继承了 Beans 的特性,为 Spring 核心提供了大量的扩展,添加对国际化、事件传播、资源加载和对Context的
			透明创建的支持. ApplicationContext 接口是 Context 模块的关键.
		(4).Expression Language 模块提供了一个强大的表达式语言用于在运行时查询和操作对象
	1.2.Data Access/Integration:包含 JDBC,ORM,OXM,JMS 和 Transaction 模块
		(1).JDBC 模块提供了一个JDBC抽象层
	1.3.Web
	1.4.AOP
	1.5.Test
#### 2.





