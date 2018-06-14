<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [1、基本概念](#1%E5%9F%BA%E6%9C%AC%E6%A6%82%E5%BF%B5)
- [2、观察者模式类图](#2%E8%A7%82%E5%AF%9F%E8%80%85%E6%A8%A1%E5%BC%8F%E7%B1%BB%E5%9B%BE)
- [3、基本实现](#3%E5%9F%BA%E6%9C%AC%E5%AE%9E%E7%8E%B0)
- [4、事件驱动模型](#4%E4%BA%8B%E4%BB%B6%E9%A9%B1%E5%8A%A8%E6%A8%A1%E5%9E%8B)
- [5、优缺点](#5%E4%BC%98%E7%BC%BA%E7%82%B9)
  - [5.1、优点](#51%E4%BC%98%E7%82%B9)
  - [5.2、缺点](#52%E7%BC%BA%E7%82%B9)
- [6、应用场景](#6%E5%BA%94%E7%94%A8%E5%9C%BA%E6%99%AF)
- [参考资料](#%E5%8F%82%E8%80%83%E8%B5%84%E6%96%99)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


# 1、基本概念

观察者模式：有时又被称为发布-订阅模式、模型-视图模式、源-收听者模式或从属者模式，是软件设计模式的一种；在此种模式中，一个目标物件管理所有相依于它的观察者物件，并且在它本身的状态改变时主动发出通知。这通常透过呼叫各观察者所提供的方法来实现。此种模式通常被用来实作事件处理系统；

简而言之：就是一个类管理着所有依赖于它的观察者类，并且它状态变化时会主动给这些依赖它的类发出通知

# 2、观察者模式类图

![image](https://github.com/chenlanqing/learningNote/blob/master/Java/Java%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F/uml/观察者模式类图.png)

- Subject：被观察者，定义被观察者必须实现的职责，能够动态取消或者添加观察者；一般是抽象类或者具体实现类，仅仅完成作为被观察者必须实现的职责：管理观察者并通知观察者；
- Observer：观察者，即进行update操作，对接收到的信息进行处理；
- ConcreteSubject：具体被观察者，具体的观察者定义被观察者的自己的业务逻辑，同时定义对哪些事件进行通知；
- ConcreteObserver：具体观察者，不同观察者有不同的处理逻辑

# 3、基本实现
- Subject
```java
public abstract class Subject {
    private String name;
    public Subject(String name) {
        this.name = name;
    }
    public Subject() {
    }
    public String getName() {
        return name;
    }
    /**
     * 注册观察者
     * @param observer
     * @return
     */
    public abstract boolean registerObserver(Observer observer);
    /**
     * 移除观察者
     * @param observer
     * @return
     */
    public abstract boolean removeObserver(Observer observer);
    /**
     * 通知观察者
     */
    public abstract void notifyObserver();
    /**
     * 被观察者更改
     */
    public abstract void change();
}
```
- Observer
```java
public interface Observer {
    /**
     * 更新
     * @param subject
     */
    void update(Subject subject);
}
```
- ConcreteSubject
```java
public class ConcreteSubject1 extends Subject {
    public ConcreteSubject1(String name) {
        super(name);
    }
    private List<Observer> observerList = new ArrayList<>();
    @Override
    public boolean registerObserver(Observer observer) {
        return observerList.add(observer);
    }
    @Override
    public boolean removeObserver(Observer observer) {
        return observerList.remove(observer);
    }
    @Override
    public void notifyObserver() {
        for (Observer o : observerList) {
            o.update(this);
        }
    }
    @Override
    public void change() {
        log.info("我是被观察者：{}，我已经修改了", getName());
        notifyObserver();
    }
}
```
- ConcreteObserver
```java
public class ConcreteObserver implements Observer{
    @Override
    public void update(Subject subject) {
        log.info("我是观察者~~~~~1，被观察者：{} 发生了变化", subject.getName());
        log.info("观察者1作出响应");
    }
}
```

# 4、事件驱动模型

# 5、优缺点
## 5.1、优点

- 抽象主题只依赖于抽象观察者
- 观察者模式支持广播通信
- 观察者模式使信息产生层和响应层分离

## 5.2、缺点

- 如一个主题被大量观察者注册，则通知所有观察者会花费较高代价
- 如果某些观察者的响应方法被阻塞，整个通知过程即被阻塞，其它观察者不能及时被通知
- 每一个观察者都要实现观察者接口，才能添加到被观察者的列表当中，假设一个观察者已经存在，而且我们无法改变其代码，那么就无法让它成为一个观察者了；
- 如果在观察者和观察目标之间有循环依赖的话，观察目标会触发它们之间进行循环调用，可能导致系统崩溃；即广播链问题，一个观察者既可以是观察者也可以是被观察者

# 6、应用场景

- 事件多级触发场景；
- 跨系统的消息交换场景；
- 适用于一对多的场景下消息传递

# 参考资料

* [观察者模式](http://www.cnblogs.com/zuoxiaolong/p/pattern7.html)
* [观察者模式](http://www.jasongj.com/design_pattern/observer/)




















