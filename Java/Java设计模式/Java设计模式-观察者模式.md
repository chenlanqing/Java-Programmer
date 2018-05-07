<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [1.观察者模式](#1%E8%A7%82%E5%AF%9F%E8%80%85%E6%A8%A1%E5%BC%8F)
- [2.观察者模式分离了观察者和被观察者二者的责任,这样让类之间各自维护自己的功能,专注于自己的功能,](#2%E8%A7%82%E5%AF%9F%E8%80%85%E6%A8%A1%E5%BC%8F%E5%88%86%E7%A6%BB%E4%BA%86%E8%A7%82%E5%AF%9F%E8%80%85%E5%92%8C%E8%A2%AB%E8%A7%82%E5%AF%9F%E8%80%85%E4%BA%8C%E8%80%85%E7%9A%84%E8%B4%A3%E4%BB%BB%E8%BF%99%E6%A0%B7%E8%AE%A9%E7%B1%BB%E4%B9%8B%E9%97%B4%E5%90%84%E8%87%AA%E7%BB%B4%E6%8A%A4%E8%87%AA%E5%B7%B1%E7%9A%84%E5%8A%9F%E8%83%BD%E4%B8%93%E6%B3%A8%E4%BA%8E%E8%87%AA%E5%B7%B1%E7%9A%84%E5%8A%9F%E8%83%BD)
- [3.JDK 有现成的观察者还被观察者](#3jdk-%E6%9C%89%E7%8E%B0%E6%88%90%E7%9A%84%E8%A7%82%E5%AF%9F%E8%80%85%E8%BF%98%E8%A2%AB%E8%A7%82%E5%AF%9F%E8%80%85)
- [参考资料](#%E5%8F%82%E8%80%83%E8%B5%84%E6%96%99)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


# 1.观察者模式
	(有时又被称为发布-订阅模式、模型-视图模式、源-收听者模式或从属者模式)是软件设计模式的一种.
	在此种模式中,一个目标物件管理所有相依于它的观察者物件,并且在它本身的状态改变时主动发出通知.
	这通常透过呼叫各观察者所提供的方法来实现.此种模式通常被用来实作事件处理系统;
==> 就是一个类管理着所有依赖于它的观察者类,并且它状态变化时会主动给这些依赖它的类发出通知
# 2.观察者模式分离了观察者和被观察者二者的责任,这样让类之间各自维护自己的功能,专注于自己的功能,
	会提高系统的可维护性和可重用性
# 3.JDK 有现成的观察者还被观察者
	Observer(观察者)  Observable(被观察者)

# 参考资料

* [观察者模式](http://www.cnblogs.com/zuoxiaolong/p/pattern7.html)
* [ 观察者模式](http://www.jasongj.com/design_pattern/observer/)




















