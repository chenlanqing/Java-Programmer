<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [1.问题描述:](#1%E9%97%AE%E9%A2%98%E6%8F%8F%E8%BF%B0)
- [2.实现:](#2%E5%AE%9E%E7%8E%B0)
  - [2.1.wait()/notify()方法:](#21waitnotify%E6%96%B9%E6%B3%95)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


* https://blog.csdn.net/monkey_d_meng/article/details/6251879

# 1.问题描述:
    (1).生产者消费者问题是多线程的一个经典问题.
        它描述是有一块缓冲区作为仓库,生产者可以将产品放入仓库,消费者则可以从仓库中取走产品.
    (2).解决生产者\消费者问题的方法可以分为两类:
        * 采用某种机制保护生产者与消费者的同步;
        * 在生产者和消费者之间建立管道
        第一种方式有较高的效率,并且易于实现,代码控制性好,属于常用实现模式;
        第二种管道缓冲区不易控制,被传输的数据不易封装.
    (3).生产者\消费者经典的实现是ThreadPoolExecutor与工作队列的关系
# 2.实现:
## 2.1.wait()/notify()方法:
    wait()/nofity()方法是基类Object的两个方法：
    wait()方法:当缓冲区已满/空时，生产者/消费者线程停止自己的执行，
        放弃锁，使自己处于等等状态，让其他线程执行。
    notify()方法:当生产者/消费者向缓冲区放入/取出一个产品时，向其他等待的线程发出
        可执行的通知，同时放弃锁，使自己处于等待状态
```java


```








