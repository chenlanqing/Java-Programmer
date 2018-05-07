<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一.代理模式基本:](#%E4%B8%80%E4%BB%A3%E7%90%86%E6%A8%A1%E5%BC%8F%E5%9F%BA%E6%9C%AC)
- [二.代理的实现方式:](#%E4%BA%8C%E4%BB%A3%E7%90%86%E7%9A%84%E5%AE%9E%E7%8E%B0%E6%96%B9%E5%BC%8F)
  - [1.静态代理:](#1%E9%9D%99%E6%80%81%E4%BB%A3%E7%90%86)
  - [2.动态代理:](#2%E5%8A%A8%E6%80%81%E4%BB%A3%E7%90%86)
  - [3.静态代理与动态代理:](#3%E9%9D%99%E6%80%81%E4%BB%A3%E7%90%86%E4%B8%8E%E5%8A%A8%E6%80%81%E4%BB%A3%E7%90%86)
- [三.模拟 JDK 动态代理:](#%E4%B8%89%E6%A8%A1%E6%8B%9F-jdk-%E5%8A%A8%E6%80%81%E4%BB%A3%E7%90%86)
- [参考资料](#%E5%8F%82%E8%80%83%E8%B5%84%E6%96%99)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


# 一.代理模式基本:
    1.基本概念:为其他对象提供一种代理以控制对这个对象的访问
    2.代理的分类:
        (1).远程代理:
        (2).虚拟代理:
        (3).保护代理:
        (4).智能引用代理:
# 二.代理的实现方式:
## 1.静态代理:
    1.1.基本概念:代理和被代理的对象在代理之前是确定的.都实现了相同的接口或者继承相同的抽象类;
    1.2.实现方式:
        (1).继承:即代理对象继承被代理的对象,重写其方法时直接调用父类的方法
            代理类一般要持有一个被代理的对象的引用
            对于我们不关心的方法，全部委托给被代理的对象处理
            自己处理我们关心的方法
            静态代理对于这种，被代理的对象很固定，我们只需要去代理一个类或者若干固定的类,数量不是太多的时候,可以使用
        (2).聚合:(在当前类引用其他类对象),代理类与被代理类实现相同的接口
    1.3.最佳实现方式:聚合
## 2.动态代理:
    动态代理有一个强制性要求:就是被代理的类必须实现了某一个接口,或者本身就是接口,就像我们的Connection
    2.1.JDK 动态代理:
        (1).两个核心类:位于 java.lang.reflect 包下
            Proxy:动态代理类:
                public static Object newProxyInstance(ClassLoader loader,Class<?>[] interfaces,InvocationHandler h){}
                返回代理类的一个实例,返回后的代理类可以被当作代理类使用
            InvocationHandler:该接口值定义了一个方法
                public Object invoke(Object proxy, Method method, Object[] args)throws Throwable;
                proxy ==> 被代理的类, method ==> 被代理的方法, args ==> 被代理的方法的参数数组
        (2).JDK 动态代理的实现步骤:
            ①.创建一个实现接口 InvocationHandler 的类,必须实现 invoke 方法;
            ②.创建被代理的类和接口;
            ③.调用 Proxy 的静态方法 newProxyInstance 创建一个代理类;
            ④.通过代理调用方法
        (3).如果不实现接口也可使用动态代理:具体是在实现 InvocationHandler 的方法时写成如下:
            Method sourceMethod = source.getClass()
                                .getDeclaredMethod(method.getName(), method.getParameterTypes());
            sourceMethod.setAccessible(true);
            Object result = sourceMethod.invoke(source, args);
            而一般是写成如下:
            method.invoke(source, args);
    2.2.CGLIB 动态代理:

    2.3.JDK 与 CGLIB 代理的比较:
        (1).JDK:
            只能代理实现了接口的类;没有实现接口的类不能实现 JDK 的动态代理
        (2).CGLIB:针对类来实现代理的,对指定目标产生一个子类,通过方法拦截技术拦截所有父类方法的调用
## 3.静态代理与动态代理:
    3.1.相同点:从虚拟机加载类的角度来讲,本质上是一样:都是在原有类的基础上,加入一些多出的行为,
        甚至完全替换原有的行为
    3.2.区别:
        (1).静态代理:
    3.3.如果在使用静态代理的时候需要写很多重复代码时,考虑改用动态带来来实现
        动态代理的使用是为了解决这样一种问题，就是我们需要代理一系列类的某一些方法:
        最典型的应用就是 SpringAOP
# 三.模拟 JDK 动态代理:

# 参考资料

* [代理模式详解](http://www.cnblogs.com/zuoxiaolong/p/pattern3.html)