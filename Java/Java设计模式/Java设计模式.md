<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、设计模式](#%E4%B8%80%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F)
  - [1、OOP 基本特性](#1oop-%E5%9F%BA%E6%9C%AC%E7%89%B9%E6%80%A7)
  - [2、面向对象设计原则](#2%E9%9D%A2%E5%90%91%E5%AF%B9%E8%B1%A1%E8%AE%BE%E8%AE%A1%E5%8E%9F%E5%88%99)
  - [3、为什么适用设计模式](#3%E4%B8%BA%E4%BB%80%E4%B9%88%E9%80%82%E7%94%A8%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F)
  - [4、设计模式分类](#4%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F%E5%88%86%E7%B1%BB)
    - [4.1、创建型模式](#41%E5%88%9B%E5%BB%BA%E5%9E%8B%E6%A8%A1%E5%BC%8F)
    - [4.2、结构型模式](#42%E7%BB%93%E6%9E%84%E5%9E%8B%E6%A8%A1%E5%BC%8F)
    - [4.3、行为型模式](#43%E8%A1%8C%E4%B8%BA%E5%9E%8B%E6%A8%A1%E5%BC%8F)
- [二、单例模式](#%E4%BA%8C%E5%8D%95%E4%BE%8B%E6%A8%A1%E5%BC%8F)
  - [1、什么是单例](#1%E4%BB%80%E4%B9%88%E6%98%AF%E5%8D%95%E4%BE%8B)
  - [2、懒汉模式](#2%E6%87%92%E6%B1%89%E6%A8%A1%E5%BC%8F)
    - [2.1、基本写法](#21%E5%9F%BA%E6%9C%AC%E5%86%99%E6%B3%95)
    - [2.2、保证线程安全](#22%E4%BF%9D%E8%AF%81%E7%BA%BF%E7%A8%8B%E5%AE%89%E5%85%A8)
    - [2.3、防止反射](#23%E9%98%B2%E6%AD%A2%E5%8F%8D%E5%B0%84)
    - [2.4、防止序列化](#24%E9%98%B2%E6%AD%A2%E5%BA%8F%E5%88%97%E5%8C%96)
    - [2.5、完整懒汉式](#25%E5%AE%8C%E6%95%B4%E6%87%92%E6%B1%89%E5%BC%8F)
  - [3、恶汉模式](#3%E6%81%B6%E6%B1%89%E6%A8%A1%E5%BC%8F)
    - [3.1、基本写法](#31%E5%9F%BA%E6%9C%AC%E5%86%99%E6%B3%95)
    - [3.2、完整饿汉式](#32%E5%AE%8C%E6%95%B4%E9%A5%BF%E6%B1%89%E5%BC%8F)
    - [3.3、Runtime类](#33runtime%E7%B1%BB)
  - [4、双重校验锁](#4%E5%8F%8C%E9%87%8D%E6%A0%A1%E9%AA%8C%E9%94%81)
    - [4.1、基本实现](#41%E5%9F%BA%E6%9C%AC%E5%AE%9E%E7%8E%B0)
    - [4.2、改进](#42%E6%94%B9%E8%BF%9B)
  - [5、静态内部类](#5%E9%9D%99%E6%80%81%E5%86%85%E9%83%A8%E7%B1%BB)
    - [5.1、基本写法](#51%E5%9F%BA%E6%9C%AC%E5%86%99%E6%B3%95)
    - [5.2、防止反射](#52%E9%98%B2%E6%AD%A2%E5%8F%8D%E5%B0%84)
  - [6、枚举实现](#6%E6%9E%9A%E4%B8%BE%E5%AE%9E%E7%8E%B0)
  - [7、反序列化问题](#7%E5%8F%8D%E5%BA%8F%E5%88%97%E5%8C%96%E9%97%AE%E9%A2%98)
  - [8、容器单例](#8%E5%AE%B9%E5%99%A8%E5%8D%95%E4%BE%8B)
  - [9、ThreadLocal与单例](#9threadlocal%E4%B8%8E%E5%8D%95%E4%BE%8B)
  - [10、单例与JVM垃圾回收](#10%E5%8D%95%E4%BE%8B%E4%B8%8Ejvm%E5%9E%83%E5%9C%BE%E5%9B%9E%E6%94%B6)
    - [10.1、分析思路](#101%E5%88%86%E6%9E%90%E6%80%9D%E8%B7%AF)
    - [单例与static](#%E5%8D%95%E4%BE%8B%E4%B8%8Estatic)
  - [11、总结](#11%E6%80%BB%E7%BB%93)
  - [12、不使用synchronized和lock实现线程安全的单例](#12%E4%B8%8D%E4%BD%BF%E7%94%A8synchronized%E5%92%8Clock%E5%AE%9E%E7%8E%B0%E7%BA%BF%E7%A8%8B%E5%AE%89%E5%85%A8%E7%9A%84%E5%8D%95%E4%BE%8B)
- [三、工厂模式](#%E4%B8%89%E5%B7%A5%E5%8E%82%E6%A8%A1%E5%BC%8F)
  - [1、简单工厂](#1%E7%AE%80%E5%8D%95%E5%B7%A5%E5%8E%82)
  - [2、工厂方法](#2%E5%B7%A5%E5%8E%82%E6%96%B9%E6%B3%95)
  - [3、抽象工厂](#3%E6%8A%BD%E8%B1%A1%E5%B7%A5%E5%8E%82)
- [四、观察者模式](#%E5%9B%9B%E8%A7%82%E5%AF%9F%E8%80%85%E6%A8%A1%E5%BC%8F)
  - [1、基本概念](#1%E5%9F%BA%E6%9C%AC%E6%A6%82%E5%BF%B5)
  - [2、观察者模式类图](#2%E8%A7%82%E5%AF%9F%E8%80%85%E6%A8%A1%E5%BC%8F%E7%B1%BB%E5%9B%BE)
  - [3、基本实现](#3%E5%9F%BA%E6%9C%AC%E5%AE%9E%E7%8E%B0)
  - [4、事件驱动模型](#4%E4%BA%8B%E4%BB%B6%E9%A9%B1%E5%8A%A8%E6%A8%A1%E5%9E%8B)
  - [5、优缺点](#5%E4%BC%98%E7%BC%BA%E7%82%B9)
    - [5.1、优点](#51%E4%BC%98%E7%82%B9)
    - [5.2、缺点](#52%E7%BC%BA%E7%82%B9)
  - [6、应用场景](#6%E5%BA%94%E7%94%A8%E5%9C%BA%E6%99%AF)
- [五、策略模式](#%E4%BA%94%E7%AD%96%E7%95%A5%E6%A8%A1%E5%BC%8F)
  - [1、定义](#1%E5%AE%9A%E4%B9%89)
  - [2、适用场景](#2%E9%80%82%E7%94%A8%E5%9C%BA%E6%99%AF)
  - [3、优缺点](#3%E4%BC%98%E7%BC%BA%E7%82%B9)
  - [4、UML类图](#4uml%E7%B1%BB%E5%9B%BE)
- [六、责任链模式](#%E5%85%AD%E8%B4%A3%E4%BB%BB%E9%93%BE%E6%A8%A1%E5%BC%8F)
  - [1、定义](#1%E5%AE%9A%E4%B9%89-1)
  - [2、适用场景](#2%E9%80%82%E7%94%A8%E5%9C%BA%E6%99%AF-1)
  - [3、实际应用](#3%E5%AE%9E%E9%99%85%E5%BA%94%E7%94%A8)
  - [4、UML类图](#4uml%E7%B1%BB%E5%9B%BE-1)
  - [5、注意事项](#5%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9)
- [七、适配器模式](#%E4%B8%83%E9%80%82%E9%85%8D%E5%99%A8%E6%A8%A1%E5%BC%8F)
  - [1、适配器模式](#1%E9%80%82%E9%85%8D%E5%99%A8%E6%A8%A1%E5%BC%8F)
  - [2、适用场景](#2%E9%80%82%E7%94%A8%E5%9C%BA%E6%99%AF-2)
  - [3、JDK与框架中使用适配器](#3jdk%E4%B8%8E%E6%A1%86%E6%9E%B6%E4%B8%AD%E4%BD%BF%E7%94%A8%E9%80%82%E9%85%8D%E5%99%A8)
  - [4、UML类图](#4uml%E7%B1%BB%E5%9B%BE-2)
  - [5、应用场景](#5%E5%BA%94%E7%94%A8%E5%9C%BA%E6%99%AF)
  - [6、相关设计模式](#6%E7%9B%B8%E5%85%B3%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F)
    - [6.1、与装饰模式、外观模式比较](#61%E4%B8%8E%E8%A3%85%E9%A5%B0%E6%A8%A1%E5%BC%8F%E5%A4%96%E8%A7%82%E6%A8%A1%E5%BC%8F%E6%AF%94%E8%BE%83)
- [八、装饰器模式](#%E5%85%AB%E8%A3%85%E9%A5%B0%E5%99%A8%E6%A8%A1%E5%BC%8F)
  - [1、基本概念](#1%E5%9F%BA%E6%9C%AC%E6%A6%82%E5%BF%B5-1)
  - [2、适用场景](#2%E9%80%82%E7%94%A8%E5%9C%BA%E6%99%AF-3)
  - [3、优缺点](#3%E4%BC%98%E7%BC%BA%E7%82%B9-1)
  - [4、装饰者模式与其他设计模式](#4%E8%A3%85%E9%A5%B0%E8%80%85%E6%A8%A1%E5%BC%8F%E4%B8%8E%E5%85%B6%E4%BB%96%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F)
  - [5、UML类图](#5uml%E7%B1%BB%E5%9B%BE)
  - [6、一般实现](#6%E4%B8%80%E8%88%AC%E5%AE%9E%E7%8E%B0)
  - [7、JDK中装饰器模式](#7jdk%E4%B8%AD%E8%A3%85%E9%A5%B0%E5%99%A8%E6%A8%A1%E5%BC%8F)
    - [7.1、深入剖析 InputStream 中的装饰模式](#71%E6%B7%B1%E5%85%A5%E5%89%96%E6%9E%90-inputstream-%E4%B8%AD%E7%9A%84%E8%A3%85%E9%A5%B0%E6%A8%A1%E5%BC%8F)
    - [7.2、深入剖析 OutputStream 中的装饰模式](#72%E6%B7%B1%E5%85%A5%E5%89%96%E6%9E%90-outputstream-%E4%B8%AD%E7%9A%84%E8%A3%85%E9%A5%B0%E6%A8%A1%E5%BC%8F)
    - [7.3、字符输入流](#73%E5%AD%97%E7%AC%A6%E8%BE%93%E5%85%A5%E6%B5%81)
    - [7.4、字符输出流](#74%E5%AD%97%E7%AC%A6%E8%BE%93%E5%87%BA%E6%B5%81)
- [九、外观（门面）模式](#%E4%B9%9D%E5%A4%96%E8%A7%82%E9%97%A8%E9%9D%A2%E6%A8%A1%E5%BC%8F)
  - [1、定义](#1%E5%AE%9A%E4%B9%89-2)
  - [2、适用场景](#2%E9%80%82%E7%94%A8%E5%9C%BA%E6%99%AF-4)
  - [3、优缺点](#3%E4%BC%98%E7%BC%BA%E7%82%B9-2)
  - [4、UML类图](#4uml%E7%B1%BB%E5%9B%BE-3)
  - [5、使用场景](#5%E4%BD%BF%E7%94%A8%E5%9C%BA%E6%99%AF)
- [十、模板模式](#%E5%8D%81%E6%A8%A1%E6%9D%BF%E6%A8%A1%E5%BC%8F)
  - [1、定义](#1%E5%AE%9A%E4%B9%89-3)
  - [2、适用场景](#2%E9%80%82%E7%94%A8%E5%9C%BA%E6%99%AF-5)
  - [3、优缺点](#3%E4%BC%98%E7%BC%BA%E7%82%B9-3)
  - [4、UML类图](#4uml%E7%B1%BB%E5%9B%BE-4)
  - [5、使用场景](#5%E4%BD%BF%E7%94%A8%E5%9C%BA%E6%99%AF-1)
  - [6、与其他设计模式](#6%E4%B8%8E%E5%85%B6%E4%BB%96%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F)
- [十一、桥接模式](#%E5%8D%81%E4%B8%80%E6%A1%A5%E6%8E%A5%E6%A8%A1%E5%BC%8F)
  - [1、定义](#1%E5%AE%9A%E4%B9%89-4)
  - [2、适用场景](#2%E9%80%82%E7%94%A8%E5%9C%BA%E6%99%AF-6)
  - [3、UML类图](#3uml%E7%B1%BB%E5%9B%BE)
  - [4、优缺点](#4%E4%BC%98%E7%BC%BA%E7%82%B9)
  - [5、JDK或者框架中使用场景](#5jdk%E6%88%96%E8%80%85%E6%A1%86%E6%9E%B6%E4%B8%AD%E4%BD%BF%E7%94%A8%E5%9C%BA%E6%99%AF)
- [十二、代理模式](#%E5%8D%81%E4%BA%8C%E4%BB%A3%E7%90%86%E6%A8%A1%E5%BC%8F)
  - [1、基本概念](#1%E5%9F%BA%E6%9C%AC%E6%A6%82%E5%BF%B5-2)
  - [2、代理的分类](#2%E4%BB%A3%E7%90%86%E7%9A%84%E5%88%86%E7%B1%BB)
  - [3、适用场景](#3%E9%80%82%E7%94%A8%E5%9C%BA%E6%99%AF)
  - [4、优缺点](#4%E4%BC%98%E7%BC%BA%E7%82%B9-1)
  - [5、代理的实现方式](#5%E4%BB%A3%E7%90%86%E7%9A%84%E5%AE%9E%E7%8E%B0%E6%96%B9%E5%BC%8F)
    - [5.1、静态代理](#51%E9%9D%99%E6%80%81%E4%BB%A3%E7%90%86)
    - [5.2、动态代理](#52%E5%8A%A8%E6%80%81%E4%BB%A3%E7%90%86)
    - [5.3、静态代理与动态代理](#53%E9%9D%99%E6%80%81%E4%BB%A3%E7%90%86%E4%B8%8E%E5%8A%A8%E6%80%81%E4%BB%A3%E7%90%86)
- [十四、建造者模式](#%E5%8D%81%E5%9B%9B%E5%BB%BA%E9%80%A0%E8%80%85%E6%A8%A1%E5%BC%8F)
  - [1、定义](#1%E5%AE%9A%E4%B9%89-5)
  - [2、适用场景](#2%E9%80%82%E7%94%A8%E5%9C%BA%E6%99%AF-7)
  - [3、优点](#3%E4%BC%98%E7%82%B9)
  - [4、使用场景](#4%E4%BD%BF%E7%94%A8%E5%9C%BA%E6%99%AF)
  - [5、代码](#5%E4%BB%A3%E7%A0%81)
  - [6、UML类图](#6uml%E7%B1%BB%E5%9B%BE)
- [十五、组合模式](#%E5%8D%81%E4%BA%94%E7%BB%84%E5%90%88%E6%A8%A1%E5%BC%8F)
  - [1、定义](#1%E5%AE%9A%E4%B9%89-6)
  - [2、适用场景](#2%E9%80%82%E7%94%A8%E5%9C%BA%E6%99%AF-8)
  - [3、UML类图](#3uml%E7%B1%BB%E5%9B%BE-1)
  - [4、优缺点](#4%E4%BC%98%E7%BC%BA%E7%82%B9-2)
- [十六、状态模式](#%E5%8D%81%E5%85%AD%E7%8A%B6%E6%80%81%E6%A8%A1%E5%BC%8F)
- [1、定义](#1%E5%AE%9A%E4%B9%89-7)
- [2、适用场景](#2%E9%80%82%E7%94%A8%E5%9C%BA%E6%99%AF-9)
- [3、实际应用](#3%E5%AE%9E%E9%99%85%E5%BA%94%E7%94%A8-1)
- [4、UML类图](#4uml%E7%B1%BB%E5%9B%BE-5)
- [5、注意事项](#5%E6%B3%A8%E6%84%8F%E4%BA%8B%E9%A1%B9-1)
- [十七、原型模式](#%E5%8D%81%E4%B8%83%E5%8E%9F%E5%9E%8B%E6%A8%A1%E5%BC%8F)
  - [1、原型模式](#1%E5%8E%9F%E5%9E%8B%E6%A8%A1%E5%BC%8F)
  - [2、适用场景](#2%E9%80%82%E7%94%A8%E5%9C%BA%E6%99%AF-10)
  - [3、优缺点](#3%E4%BC%98%E7%BC%BA%E7%82%B9-4)
  - [4、示例代码](#4%E7%A4%BA%E4%BE%8B%E4%BB%A3%E7%A0%81)
  - [5、JDK使用场景](#5jdk%E4%BD%BF%E7%94%A8%E5%9C%BA%E6%99%AF)
- [十八、享元模式](#%E5%8D%81%E5%85%AB%E4%BA%AB%E5%85%83%E6%A8%A1%E5%BC%8F)
  - [1、定义](#1%E5%AE%9A%E4%B9%89-8)
  - [2、适用场景](#2%E9%80%82%E7%94%A8%E5%9C%BA%E6%99%AF-11)
  - [3、状态](#3%E7%8A%B6%E6%80%81)
  - [4、UML类图](#4uml%E7%B1%BB%E5%9B%BE-6)
  - [5、优缺点](#5%E4%BC%98%E7%BC%BA%E7%82%B9-1)
  - [6、JDK使用场景](#6jdk%E4%BD%BF%E7%94%A8%E5%9C%BA%E6%99%AF)
- [参考资料](#%E5%8F%82%E8%80%83%E8%B5%84%E6%96%99)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


# 一、设计模式

## 1、OOP 基本特性

- 封装：也就是把客观事物封装成抽象的类，并且类可以把自己的属性和方法只让可信的类操作，对不可信的进行信息隐藏。
- 继承：是指这样一种能力，它可以使用现有的类的所有功能，并在无需重新编写原来类的情况下对这些功能进行扩展。
- 多态：指一个类实例的相同方法在不同情形有不同的表现形式。具体来说就是不同实现类对公共接口有不同的实现方式。但这些操作可以通过相同的方式（公共接口)予以调用。

## 2、面向对象设计原则

- 开闭原则：对修改关闭，对扩展开放，用抽象构建框架，用实现扩展细节；提高软件系统的可复用性及可维护性
- 里氏替换原则：一个子类应该可以替换掉父类并且可以正常工作；子类可以扩展父类的功能，但不能改变父类原有的功能；
    - 子类可以实现父类的抽象方法，但是不能覆盖父类的非抽象方法；
    - 子类中可以增加自己特有的方法；
    - 当子类的方法重载父类的方法时，方法的前置条件（即方法的入参）要比父类方法的输入参数更宽松；
    - 当子类的方法实现父类的方法时（重写/重载或实现抽象方法），方法的后置条件（即返回值）要比父类更严格或者相等
- 依赖倒置原则：设计和实现要依赖于抽象而非具体。高层模块不该依赖于低层模块，二者都应该依赖于抽象，抽象不应该依赖于细节，细节应该依赖于抽象，针对接口编程，不要针对实现编程
- 接口隔离原则：接口最小化原则，强调的是一个接口拥有的行为应该尽可能的小，尽量细化接口，使类具有很好的可读性、可扩展性和可维护性；
- 单一职责原则：每个类都只负责单一的功能，切不可太多，并且一个类应当尽量的把一个功能做到极致，降低类的复杂度，提高类的可读性，提高系统的可维护性，降低变更引起的风险；
- 迪米特原则：也称最小知道原则，即一个类应该尽量不要知道其他类太多的东西，不要和陌生的类有太多接触低耦合高内聚
- 合成/聚合复用原则：如果新对象的某些功能在别的已经创建好的对象里面已经实现，那么应当尽量使用别的对象提供的功能，使之成为新对象的一部分，而不要再重新创建.如桥接模式

## 3、为什么适用设计模式

- 什么是设计模式：

	是一套被反复使用的、多数人知晓的、经过分类编目的、代码设计经验的总结.使用设计模式
	是为了重用代码、让代码更容易被他人理解、保证代码可靠性；

- 每个设计模式都有其适合范围，并解决特定问题.所以项目实践中应该针对特定使用场景选用合适的设计模式

## 4、设计模式分类

总共23种，可以分为三大类

### 4.1、创建型模式

提供了一种在创建对象的同时隐藏创建逻辑的方式，而不是使用新的运算符直接实例化对象。
这使得程序在判断针对某个给定实例需要创建哪些对象时更加灵活:

- （1）工厂模式（Factory Pattern）
- （2）抽象工厂模式（Abstract Factory Pattern）
- （3）单例模式（Singleton Pattern）
- （4）建造者模式（Builder Pattern）
- （5）原型模式（Prototype Pattern）

### 4.2、结构型模式

这些设计模式关注类和对象的组合。继承的概念被用来组合接口和定义组合对象获得新功能的方式:

- （6）适配器模式（Adapter Pattern）
- （7）桥接模式（Bridge Pattern）
- （8）过滤器模式（Filter、Criteria Pattern）
- （9）组合模式（Composite Pattern）
- （10）装饰器模式（Decorator Pattern）
- （11）外观模式（Facade Pattern）
- （12）享元模式（Flyweight Pattern）
- （13）代理模式（Proxy Pattern）

### 4.3、行为型模式

这些设计模式特别关注对象之间的通信

- （14）责任链模式（Chain of Responsibility Pattern）
- （15）命令模式（Command Pattern）
- （16）解释器模式（Interpreter Pattern）
- （17）迭代器模式（Iterator Pattern）
- （18）中介者模式（Mediator Pattern）
- （19）备忘录模式（Memento Pattern）
- （20）观察者模式（Observer Pattern）
- （21）状态模式（State Pattern）
- （22）空对象模式（Null Object Pattern）
- （23）策略模式（Strategy Pattern）
- （24）模板模式（Template Pattern）
- （25）访问者模式（Visitor Pattern）


# 二、单例模式

## 1、什么是单例

只能有一个实例、单例类必须自己创建自己的唯一实例、单例类必须给所有其他对象提供这一实例。如：配置文件、线程池、工具类、缓存、日志对象；在应用中如果有两个或者两个以上的实例会引起错误，又或者换句话说，就是这些类，在整个应用中，同一时刻，有且只能有一种状态

单例的实现方式：懒汉、恶汉、双重检验锁、静态内部类、枚举

## 2、懒汉模式

### 2.1、基本写法

- 是否 Lazy 初始化：是
- 是否多线程安全：否
- 实现难度：易
- 最大缺点：不支持多线程

```java
@NonThreadSafe
public class LazySingleton {
    private static LazySingleton lazySingleton;
    private LazySingleton(){}

    public static LazySingleton getInstance(){
        if (lazySingleton == null) {
            lazySingleton = new LazySingleton();
        }
        return lazySingleton;
    }
}
```

### 2.2、保证线程安全

```java
@ThreadSafe
public class LazyThreadSafeSingleton {
    private static LazyThreadSafeSingleton lazySingleton;
    private LazyThreadSafeSingleton(){}
    public synchronized static LazyThreadSafeSingleton getInstance(){
        if (lazySingleton == null) {
            lazySingleton = new LazyThreadSafeSingleton();
        }
        return lazySingleton;
    }
}
```

### 2.3、防止反射

懒汉模式的写法多线程不能完全防止反射的发生

```java
public class LazySingletonAvoidReflection {
    // 主要是构造器中加入判断，如果实例不为空，则抛异常
    private LazySingletonAvoidReflection(){
        if (lazySingleton != null){
            throw new RuntimeException("单例不能通过反射构建实例");
        }
    }
}
```

### 2.4、防止序列化

只需要在对应的单例应用中加入如下方法
```java
private Object readResolve(){
    return lazySingleton;
}
```

### 2.5、完整懒汉式

```java
public class LazySingletonComplete implements Serializable{
    private static LazySingletonComplete instance;
    private LazySingletonComplete(){
        if (instance != null){
            throw new RuntimeException("单例不能通过反射构建实例");
        }
    }
    public static LazySingletonComplete getInstance(){
        synchronized (LazySingletonComplete.class) {
            if (instance == null){
                instance = new LazySingletonComplete();
            }
            return instance;
        }
    }
    private Object readResolve(){
        return instance;
    }
}
```

## 3、饿汉模式

- 是否 Lazy 初始化：否
- 是否多线程安全：是
- 实现难度：易
- 优点：没有加锁，执行效率会提高
- 缺点：类加载时就初始化，浪费内存

### 3.1、基本写法

```java
@ThreadSafe
public class HungrySingleton {
    private final static HungrySingleton hungrySingleton = new HungrySingleton();
    private HungrySingleton(){}
    private static HungrySingleton getInstance(){
        return hungrySingleton;
    }
}
```

### 3.2、完整饿汉式

防止反射与序列化

```java
@ThreadSafe
public class HungrySingleton {
    private final static HungrySingleton hungrySingleton = new HungrySingleton();
    private HungrySingleton(){
        if (hungrySingleton != null){
            throw new RuntimeException("单例不能通过反射构建实例");
        }
    }
    private static HungrySingleton getInstance(){
        return hungrySingleton;
    }
    private Object readResolve(){
        return hungrySingleton;
    }
}
```

### 3.3、Runtime类

JDK中的`Runtime`类使用的就是恶汉模式来实现的
```java
public class Runtime {
    private static Runtime currentRuntime = new Runtime();
    public static Runtime getRuntime() {
        return currentRuntime;
    }
    private Runtime() {}
}
```

## 4、双重校验锁

### 4.1、基本实现

```java
public class LazyDoubleCheckSingleton {
    private static LazyDoubleCheckSingleton lazySingleton;
    private LazyDoubleCheckSingleton(){}
    public static LazyDoubleCheckSingleton getInstance(){
        if (lazySingleton == null) {
            synchronized (LazyDoubleCheckSingleton.class) {
                if (lazySingleton == null) {
                    lazySingleton = new LazyDoubleCheckSingleton();
                }
            }
        }
        return lazySingleton;
    }
}
```
存在问题：
- 上述代码也有问题：`lazySingleton = new LazyDoubleCheckSingleton();` 不是原子操作，其在JVM上大概做了三件事情：
	- （1）给 lazySingleton 分配内存
	- （2）调用 LazyDoubleCheckSingleton 的构造函数来初始化成员变量;
	- （3）将 lazySingleton 对象指向分配的内存空间（执行完这步 singleton 就为非 null 了）

但是在 JVM 的即时编译器中存在指令重排序的优化，上面的第二步骤和第三步骤执行的顺序是不能保障的；如果在步骤3执行完毕，步骤2还未执行之前，被其他线程抢占，这时 lazySingleton 已经是非 null 了但是未被初始化；所以线程会直接返回 lazySingleton 然后使用，然后顺理成章地报错；

### 4.2、改进

volatile 会禁止指令重排序优化

```java
public class LazyDoubleCheckSingleton {
    private volatile static LazyDoubleCheckSingleton lazySingleton;
    private LazyDoubleCheckSingleton(){}
    public static LazyDoubleCheckSingleton getInstance(){
        if (lazySingleton == null) {
            synchronized (LazyDoubleCheckSingleton.class) {
                if (lazySingleton == null) {
                    lazySingleton = new LazyDoubleCheckSingleton();
                }
            }
        }
        return lazySingleton;
    }
}
```

***注意：***
在Java5以前的版本使用了volatile的双检锁还是有问题的，Java5以前的JMM（Java 内存模型）是存在缺陷的，即时将变量声明成volatile也不能完全避免重排序，主要是volatile变量前后的代码仍然存在重排序问题

## 5、静态内部类

推荐的单例写法

### 5.1、基本写法

```java
public class StaticInnerClassSingleton {
    private static class StaticInnerClassSingletonHolder {
        private static final StaticInnerClassSingleton INSTANCE = new StaticInnerClassSingleton();
    }
    private StaticInnerClassSingleton(){}
    public static StaticInnerClassSingleton getInstance(){
        return StaticInnerClassSingletonHolder.INSTANCE;
    }
    // 防止序列化
    private Object readResolve() {
        return getInstance();
    }
}
```

- 这种方式同样利用了classloder的机制来保证初始化instance时只有一个线程，跟饿汉式不同的是：饿汉式只要 StaticInnerClassSingleton 类被装载了，那么instance就会被实例化（没有达到lazy loading效果），而这种方式是 StaticInnerClassSingleton 类被装载了，instance不一定被初始化，因为 StaticInnerClassSingletonHolder 类没有被主动使用，只有显示通过调用getInstance方法时，才会显示装载StaticInnerClassSingletonHolder类，从而实例化instance：

	* 加载一个类时，其内部类不会同时被加载；
	* 一个类被加载，当且仅当其某个静态成员（静态域、构造器、静态方法等）被调用时发生

- 由于对象实例化是在内部类加载的时候创建的，因此是线程安全的（因为在方法中创建，才存在并发问题，静态内部类随着方法调用而被加载，只加载一次，不存在并发问题）
- 反射会破坏上述单例结构

### 5.2、防止反射

只要通过变量来控制的，都无法完全防止反射的发生

```java
public class InnerClassSingleton {
    private static boolean initialized = false;
    private InnerClassSingleton(){
        synchronized (InnerClassSingleton.class){
            if (!initialized){
                initialized = !initialized;
            } else {
                throw new RuntimeException("无法构造单例");
            }
        }
    }
    static class InnerClassSingletonHolder{
        private static final InnerClassSingleton instance = new InnerClassSingleton();
    }
    public static InnerClassSingleton getInstance() {
        return InnerClassSingletonHolder.instance;
    }
}
```

## 6、枚举实现

目前最好的方式，避免了反射的攻击和序列化的问题

```java
public enum SingletonEnum {
	INSTANCE;
	// 在反射时，通过私有构造器newInstance是会抛出非法参数异常：IllegalArgumentException
	// Exception in thread "main" java.lang.IllegalArgumentException: Cannot reflectively create enum objects
	public static void main(String... args)throws Exception{
		Constructor[] array = SingletonEnum.INSTANCE.getClass().getDeclaredConstructors();
		for (Constructor constructor : array) {
			constructor.setAccessible(true);
			constructor.newInstance(null);
		}
	}
}
```

[枚举本质](../Java基础/Java基础知识.md#十八枚举类)

## 7、反序列化问题

在反序列化的过程中到底发生了什么，使得反序列化后的单例不是唯一的？分析一下 ObjectInputputStream 的readObject 方法执行情况到底是怎样的

- ObjectInputStream 的readObject的调用栈：readObject--->readObject0--->readOrdinaryObject--->checkResolve

- 看下 readOrdinaryObject方法的代码片段			
```java
/**
 * 这里创建的这个obj对象，就是本方法要返回的对象，也可以暂时理解为是ObjectInputStream的readObject返回的对象
 * (1).isInstantiable：如果一个serializable/externalizable的类可以在运行时被实例化，那么该方法就返回true.
 * 针对serializable和externalizable我会在其他文章中介绍.
 * (2).desc.newInstance：该方法通过反射的方式调用无参构造方法新建一个对象
 */
Object obj;
try {
	obj = desc.isInstantiable() ? desc.newInstance() : null;
} catch (Exception ex) {
	throw (IOException) new InvalidClassException(desc.forClass().getName()，"unable to create instance").initCause(ex);
}
```

- 结论：为什么序列化可以破坏单例了？ 序列化会通过反射调用无参数的构造方法创建一个新的对象
```java
/**
 * hasReadResolveMethod:如果实现了serializable 或者 externalizable接口的类中包含readResolve则返回true
 * invokeReadResolve:通过反射的方式调用要被反序列化的类的readResolve方法
 */
if (obj != null &&	handles.lookupException(passHandle) == null &&	desc.hasReadResolveMethod()){
	Object rep = desc.invokeReadResolve(obj);
	if (unshared && rep.getClass().isArray()) {
		rep = cloneArray(rep);
	}
	if (rep != obj) {
		handles.setObject(passHandle， obj = rep);
	}
}
```
- 如何防止序列化/反序列化破坏单例模式：在类中定义readResolve就可以解决该问题，定义的readResolve方法，并在该方法中指定要返回的对象的生成策略，就可以防止单例被破坏
```java
/**
 * 使用双重校验锁方式实现单例
 */
public class Singleton implements Serializable{
	private volatile static Singleton singleton;
	private Singleton (){
		// 防止反射创建新的实例
		if (singleton != null){
			throw new IllegalArgumentException("cannot exist two instance");
		}
	}
	public static Singleton getSingleton() {
		if (singleton == null) {
			synchronized (Singleton.class) {
				if (singleton == null) {
					singleton = new Singleton();
				}
			}
		}
		return singleton;
	}
	private Object readResolve() {
		return singleton;
	}
}
```

## 8、容器单例

```java
public class ContainerSingleton {
    private static Map<String, Object> singleMap = new HashMap<>();
    private ContainerSingleton(){}

    public static void putInstance(String key, Object instance) {
        if (StringUtils.isNotBlank(key) && instance != null) {
            if (!singleMap.containsKey(key)){
                singleMap.put(key,instance);
            }
        }
    }
    public static Object getInstance(String key) {
        return singleMap.get(key);
    }
}
```

可以参考`java.awt.Desktop`，其中有使用该类单例

如果应用中有比较多的单例，可以通过容器来进行管理，类比SPring的IOC容器

## 9、ThreadLocal与单例

ThreadLocal 可以确保在一个线程中对象是唯一的，但是无法保证在整个应用中对象时唯一的，如Mybatis的`ErrorContext`类

```java
public class ThreadLocalSingleton {
    private static ThreadLocal<ThreadLocalSingleton> instance = new ThreadLocal<>(){
        @Override
        protected Object initialValue() {
            return new ThreadLocalSingleton();
        }
    };
    private ThreadLocalSingleton(){}
    public static ThreadLocalSingleton getInstance(){
        return instance.get();
    }
}
```

## 10、单例与JVM垃圾回收

当一个单例的对象长久不用时，会不会被jvm的垃圾收集机制回收?(Hotspot 虚拟机)

### 10.1、分析思路

- Hotspot 垃圾收集算法:GC Root
- 方法区的垃圾收集方法，JVM卸载类的判断方法

### 单例与static

当需要共享的变量很多时，使用static变量占用内存的时间过长，在类的整个生命周期，而对象只是存在于对象的整个生命周期。卸载一个实例比卸载一个类容易；

使用单例模式可以限制对象实例的个数，除了返回实例的操作之外不能被new出来。这在某些需要限制对象访问的场合下是有用的。使用static的话并不能限制对象实例的个数

## 11、总结

有两个问题需要注意

- 如果单例由不同的类装载器装入，那便有可能存在多个单例类的实例。假定不是远端存取，例如一些servlet容器对每个servlet使用完全不同的类装载器，这样的话如果有两个servlet访问一个单例类，它们就都会有各自的实例，解决方案:
```java
private static Class getClass(String classname)throws ClassNotFoundException {
	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		if(classLoader == null)     
			classLoader = Singleton.class.getClassLoader();     

		return (classLoader.loadClass(classname));     
	}     
}  
```
- 如果Singleton实现了java.io.Serializable 接口，那么这个类的实例就可能被序列化和复原。不管怎样，如果你序列化一个单例类的对象，接下来复原多个那个对象，那你就会有多个单例类的实例:
```java
public class Singleton implements java.io.Serializable {     
	public static Singleton INSTANCE = new Singleton(); 
	protected Singleton() { }     
	private Object readResolve() {     
			return INSTANCE;     
	}    
} 
```

- 如果单例接口实现了cloneable接口，那么有可能会破坏单例模式，所以在重写clone方法时需要特殊处理
```java
public class Singleton implements Cloneable{
    private Singleton(){}
    private static Singleton instance = new Singleton();
    public static Singleton getInstance(){
        return instance;
    } 
    protected Object clone() throws UnsupportedException{
        return getInstance();
    }
}
```

## 12、不使用synchronized和lock实现线程安全的单例

上面的代码要么显示或者隐式的使用了synchronized或者lock，饿汉模式和内部类模式是因为类的初始化是由ClassLoader完成的，这其实就是利用了ClassLoader的线程安全机制啊；ClassLoader的loadClass方法在加载类的时候使用了synchronized关键字；

可以使用CAS来实现线程安全的单例；借助CAS（AtomicReference）实现单例模式

```java
public class CASSingleton {
    private static AtomicReference<CASSingleton> INSTANCE = new AtomicReference<>();
    private CASSingleton(){}
    public static CASSingleton getInstance() {
        for (;;){
            CASSingleton instance = INSTANCE.get();
            // 如果singleton不为空，就返回singleton
            if (instance != null) {
                return instance;
            }
            instance = new CASSingleton();
            // CAS操作，预期值是NULL，新值是singleton
            // 如果成功，返回singleton
            // 如果失败，进入第二次循环，singletonAtomicReference.get()就不会为空了
            if (INSTANCE.compareAndSet(null, instance)) {
                return instance;
            }
        }
    }
}
```

## 13、JDK中使用场景

- java.lang.Runtime#getRuntime

# 三、工厂模式

## 1、简单工厂

静态工厂方法：由一个工厂对象决定创建出哪一种产品类的实例

- 简单工厂模式解决的问题是如何去实例化一个合适的对象，但是其违背了开闭原则
- 简单工厂模式的核心思想就是：有一个专门的类来负责创建实例的过程
- 适用场景：
    - 工厂类负责创建的对象比较少
    - 客户端只知道传入工厂类的参数，对于如何创建对象不关系

```java
public interface IProduct {
    public void method();
}
public class ProductA implements IProduct{
    public void method() {
        System.out.println("产品A方法");
    }
}
public class ProductB implements IProduct{
    public void method() {
        System.out.println("产品B方法");
    }
}
public class Creator {
    private Creator(){}
    public static IProduct createProduct(String productName){
        if (productName == null) { return null;}
        if (productName.equals("A")) {
            return new ProductA();
        }else if (productName.equals("B")) {
            return new ProductB();
        }else {
            return null;
        }
    }
}
```

JDK中有使用的了简单工厂模式：
- Calendar类
- DriverManager

## 2、工厂方法

- 工厂方法：定义一个创建产品对象的工厂接口，将实际创建工作推迟到子类当中。核心工厂类不再负责产品的创建，这样核心类成为一个抽象工厂角色，仅负责具体工厂子类必须实现的接口
- 工厂方法模式弥补了简单工厂模式不满足开闭原则的诟病，当我们需要增加产品时，只需要增加相应的产品和工厂类，而不需要修改现有的代码，其是符合开闭原则的；但是其类的个数容易过多，增加复杂度
- 实例代码：

    ```java
    // 抽象产品接口
    public interface Light {
        public void turnOn();
        public void turnOff();
    }
    // 具体的产品
    public class BuldLight implements Light{
        public void turnOn() {System.out.println("BuldLight On"); }
        public void turnOff() {System.out.println("BuldLight Off");}
    }
    public class TubeLight implements Light{
        public void turnOn() {System.out.println("TubeLight On");}
        public void turnOff() {System.out.println("TubeLight Off");}
    }
    // 抽象的工厂接口
    public interface Creator {
        public Light createLight();
    }
    // 创建指定产品的具体工厂
    public class BuldCreator implements Creator{
        public Light createLight() {return new BuldLight();}
    }
    public class TubeCreator implements Creator{
        public Light createLight() {return new TubeLight();}
    }
    ```
- 工厂方法运用：JDBC的Driver和Connection适用的场景就是我们需要一个产品帮我们完成一项任务，但是这个产品有可能有很多品牌（像这里的mysql，oracle），为了保持我们对产品操作的一致性，我们就可能要用到工厂方法模式

- JDK中运用的工厂方法：Iterator；URLStreamHandlerFactory与URLStreamHandler
- logback中的实现类

## 3、抽象工厂

- 抽象工厂模式提供了一个创建一系列相关或相互依赖对象的接口，无需指定他们具体的类；
- 适用场景：客户端不依赖产品实例如何被创建、实现等细节；强调一系列相关的产品对象一起使用创建对象需要大量重复代码；提供了产品类的类库
- 优点：具体产品在应用层隔离
- JDK中使用场景：Connection、Statement、PreparedStatment和mybatis中的SqlSessionFactory、SqlSession
- 实现：

    ```java
    
    ```
- JDK使用场景：
    
    - java.util.Calendar#getInstance()

# 四、观察者模式

## 1、基本概念

观察者模式：有时又被称为发布-订阅模式、模型-视图模式、源-收听者模式或从属者模式，是软件设计模式的一种；在此种模式中，一个目标物件管理所有相依于它的观察者物件，并且在它本身的状态改变时主动发出通知。这通常透过呼叫各观察者所提供的方法来实现。此种模式通常被用来实作事件处理系统；

简而言之：就是一个类管理着所有依赖于它的观察者类，并且它状态变化时会主动给这些依赖它的类发出通知

## 2、观察者模式类图

![image](uml/观察者模式类图.png)

- Subject：被观察者，定义被观察者必须实现的职责，能够动态取消或者添加观察者；一般是抽象类或者具体实现类，仅仅完成作为被观察者必须实现的职责：管理观察者并通知观察者；
- Observer：观察者，即进行update操作，对接收到的信息进行处理；
- ConcreteSubject：具体被观察者，具体的观察者定义被观察者的自己的业务逻辑，同时定义对哪些事件进行通知；
- ConcreteObserver：具体观察者，不同观察者有不同的处理逻辑

## 3、基本实现
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

## 4、事件驱动模型

## 5、优缺点
### 5.1、优点

- 抽象主题只依赖于抽象观察者
- 观察者模式支持广播通信
- 观察者模式使信息产生层和响应层分离

### 5.2、缺点

- 如一个主题被大量观察者注册，则通知所有观察者会花费较高代价
- 如果某些观察者的响应方法被阻塞，整个通知过程即被阻塞，其它观察者不能及时被通知
- 每一个观察者都要实现观察者接口，才能添加到被观察者的列表当中，假设一个观察者已经存在，而且我们无法改变其代码，那么就无法让它成为一个观察者了；
- 如果在观察者和观察目标之间有循环依赖的话，观察目标会触发它们之间进行循环调用，可能导致系统崩溃；即广播链问题，一个观察者既可以是观察者也可以是被观察者

## 6、应用场景

- 事件多级触发场景；
- 跨系统的消息交换场景；
- 适用于一对多的场景下消息传递

# 五、策略模式

## 1、定义

它定义了算法家族，分别封装起来，让它们之间可以互相替换，此模式让算法的变化不会影响到使用算法的客户端

- 策略类：定义所有支持的算法的公共接口
- 具体策略类：封装了具体的算法或行为，继承于Stratege类；
- 上下文类：可以使用工厂方法模式维护一个对Stratege对象的引用；

## 2、适用场景

- 当实现某个功能需要有不同算法要求时；
- 不同时间应用不同的业务规则时

一句话：当代码中使用了大量的if...else语句时，可以考虑使用策略模式

## 3、优缺点

- 优点：
    - 策略模式是一种定义一系列算法的方法，从概念上来看，所有算法完成的都是相同的工作，只是实现不同，它可以以相同的方式调用所有的算法，减少了各种算法类与使用算法类之间的耦合；
    - 策略模式的Stratege类为Context定义了一系列的可供重用的算法或行为。继承有助于析取出这些算法的公共功能；
    - 策略模式每个算法都有自己的类，可以通过自己的接口单独测试。因而简化了单元测试；
    - 策略模式将具体算法或行为封装到Stratege类中，可以在使用这些类中消除条件分支（避免了不同行为堆砌到一个类中）；
- 缺点：
    - 将选择具体策略的职责交给了客户端，并转给Context对象

## 4、UML类图

![](uml/策略模式类图.png)

# 六、责任链模式

## 1、定义

通过把请求从一个对象传递到链条中下一个对象的方式来解除对象之间的耦合，直到请求被处理完毕。链中的对象是同一接口或抽象类的不同实现。

## 2、适用场景

## 3、实际应用

- Servlet中的Filter，FilterChain，我们实现的Filter就是责任，FilterChain是一个链条
- JS中事件的冒泡机制

## 4、UML类图

![](uml/责任链模式类图.png)

[代码参考](https://github.com/chenlanqing/java-code/tree/master/java-design/java-design-pattern/src/main/java/com/blue/fish/design/pattern/behavioral/chainofresponsibility)

## 5、注意事项

# 七、适配器模式


## 1、适配器模式

适配器模式将一个类的接口，转化成客户期望的另一个接口，适配器让原本接口不兼容的类可以并存合作。

从实现上可以分为类适配器和对象适配器，这两种的区别在于实现方式的不同：一种是采用继承，一种是采用组合的方式；

另外从使用目的上来说，也可以分为两种：特殊适配器和缺省适配器，这两种的区别在于使用目的上的不同，一种为了复用原有的代码并适配当前的接口，一种为了提供缺省的实现，避免子类需要实现不该实现的方法；

需要注意的是：适配器模式是补救措施，所以在系统设计过程中请忘掉这个设计模式，这个模式只是在你无可奈何时的补救方式

## 2、适用场景

场景通常情况下是，系统中有一套完整的类结构，而我们需要利用其中某一个类的功能（通俗点说可以说是方法），但是我们的客户端只认识另外一个和这个类结构不相关的接口，此时适配器就可以上场了，我们可以将这个现有的类与我们的目标接口进行适配，最终获得一个符合需要的接口并且包含待复用的类的功能的类

- 类适配器：一般是针对适配目标是接口的情况下使用；
- 对象适配器：一般是针对适配目标是类或者是需要复用的对象多于一个的时候使用；
- 缺省适配器：一般是为了弥补接口过大所犯下的过错；但由于JAVA语言规则的原因，实现一个接口必须实现它的全部方法，所以我们的子类不得不被迫写一堆空方法在那；这时候缺省适配器用上了；使用的时候只需要继承缺省的接口就可以

## 3、JDK与框架中使用适配器

- XmlAdapter
- AdvisorAdapter
- JpaVendorAdapter
- HandlerAdapter

## 4、UML类图

![](uml/适配器模式类图.png)

## 5、应用场景

- JDK中早期集合的都实现了一个elements()方法，其会返回一个Enumeration；JDK新的集合中开始使用Iterator（迭代器），和Enumeration接口很像，但是其支持删除元素能力，为了将枚举适配到迭代器，对应的集合中有个Enumerator，其实现了两个接口，比如Hashtable中`private class Enumerator<T> implements Enumeration<T>, Iterator<T>`，因为枚举是个只读接口，适配器无法实现一个有实际功能的remove方法，只能抛出异常；
    - 将一个枚举适配为迭代器
        ```java
        /**
        * 将一个枚举适配成迭代器
        */
        public class EnumerationIteratorAdapter<T> implements Iterator<T> {
            private Enumeration<T> enumeration;
            @Override
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }
            @Override
            public T next() {
                return enumeration.nextElement();
            }
            // 这个remove方法可以不实现，因为该方法在Iterator接口中已经是默认方法了
            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        }
        ```
    - 将一个迭代器适配为枚举
        ```java
        /**
        * 将一个迭代器适配为枚举
        */
        public class IteratorEnumerationAdapter<T> implements Enumeration<T> {
            private Iterator<T> iterator;
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }
            @Override
            public T nextElement() {
                return iterator.next();
            }
        }
        ```

## 6、相关设计模式

### 6.1、与装饰模式、外观模式比较

- 装饰模式：不改变接口，加入新的责任；
- 适配器模式：将一个接口转成另一个接口；
- 外观模式：提供给子系统的一个简化接口。

# 八、装饰器模式


## 1、基本概念

装饰模式是在不必改变原类文件和使用继承的情况下，动态的扩展一个对象的功能。它是通过创建一个包装对象，也就是装饰来包裹真实的对象

注意几点
- 不改变原类文件
- 不使用继承
- 动态扩展

## 2、适用场景

- 扩展一个类的功能或者给一个类添加职责；
- 动态的给一个对象添加功能，这些功能是可以动态的撤销；

## 3、优缺点

- 继承的有力补充，比继承灵活，不改变原有对象的情况下给一个对象扩展功能；
- 符合开闭原则；

## 4、装饰者模式与其他设计模式

- 装饰者与代理模式


- 装饰者与适配器模式

## 5、UML类图

![](image/装饰器模式类图.png)

## 6、一般实现


## 7、JDK中装饰器模式

### 7.1、深入剖析 InputStream 中的装饰模式

![](image/InputStream类图.png)

从上图可以看出

- 抽象组件：InputStream，这是一个抽象类，为各种子类型提供统一的接口；
- 具体组件：ByteArrayInputStream、FileInputStream、PipedInputStream、StringBufferInputStream(JDK8 以后过期)等，它们实现了抽象组件所规定的接口；
- 抽象装饰者：FilterInputStream，它实现了 InputStream 所规定的接口；
- 具体装饰者：BufferedInputStream、DataInputStream 以及两个不常用到的类LineNumberInputStream（JDK8 以后过期）、PushbackInputStream 等；

**注意：**

- ①、InputStream 类型中的装饰模式是半透明的；
- ②、PushbackInputStream 是一个半透明的装饰类，这个装饰类提供了额外的方法unread()，换言之，它破坏了理想的装饰模式的要求如果客户端持有一个类型为InputStream对象的引用in的话，那么如果in的真实类型是 PushbackInputStream的话，只要客户端不需要使用unread()方法，那么客户端一般没有问题.但是如果客户端必须使用这个方法，就必须进行向下类型转换.将in的类型转换成为PushbackInputStream之后才可能调用这个方法.但是，这个类型转换意味着客户端必须知道它拿到的引用是指向一个类型为 PushbackInputStream 的对象。这就破坏了使用装饰模式的原始用意；

### 7.2、深入剖析 OutputStream 中的装饰模式

![](image/OutputStream类图.png)

从图可以看出:
- 抽象组件：OutputStream，这是一个抽象类，为各种子类型提供统一的接口；
- 具体组件：ByteArrayOutputStream、FileOutputStream、ObjectOutputStream、PipedOutputStream等，它们实现了抽象组件所规定的接口；
- 抽象装饰者：FilterOutputStream，它实现了 OutputStream 所规定的接口；
- 具体装饰者：BufferedOutputStream、CheckedOutputStream、CipheOutputSteam、DataOutputStream 等，

### 7.3、字符输入流

- 抽象组件：Reader，这是一个抽象类，为各种子类型提供统一的接口；
- 具体组件：CharArayReader、FilterReader、InputStreamReader、PipedReader、StringReader 等，它们实现了抽象组件所规定的接口；
- 抽象装饰者：BufferedReader 、FilterReader、InputStreamReader，它实现了Reader所规定的接口；
- 具体装饰者：LineNumberReader、PushbackReader、FileReader 等；

### 7.4、字符输出流

- 抽象组件：Writer，这是一个抽象类，为各种子类型提供统一的接口；
- 具体组件：BufferedWriter、CharArrayWriter、FilterWriter、OutputStreamWriter、PipedWriter、PrintWriter、StringWriter等，它们实现了抽象组件所规定的接口；
- 抽象装饰者：OutputStreamWriter，它实现了 Writer 所规定的接口；
- 具体装饰者：FileWriter；

# 九、外观（门面）模式


## 1、定义

又叫门面模式，提供了一个统一的接口，用来访问子系统中的一群接口；

外观模式定义了一个高层接口，让子系统更容易使用；其属于结构型设计模式。

外观是提供了更直接的操作，并未将原来的子系统阻隔起来。如果需要子系统类的更高层功能，还是可以使用原来的子系统。

其实是有点类似我们的web架构：action -> service -> dao

## 2、适用场景

子系统越来越复杂，增加外观模式提供简单调用接口；

构建多层系统结构，利用外观对象作为每层的入口，简化层间调用；

## 3、优缺点

- 优点：
    - 简化了调用过程，无需了解深入子系统，防止带来风险；
    - 减少系统依赖，松散耦合
    - 符合迪米特法则；
- 缺点：
    - 不符合开闭原则

## 4、UML类图

![](image/外观模式类图.png)

它主要由两部分组成，一部分是子系统（包括接口，实现类，等等），一部分是外观接口和实现类，外观接口负责提供客户端定制的服务，外观实现则负责组合子系统中的各个类和接口完成这些服务，外观接口则是提供给客户端使用的，这样就解除了客户端与子系统的依赖，而让客户端只依赖于外观接口，这是一个优秀的解耦实践。

以积分兑换为业务，三个流程：判断库存、积分兑换资格、运输为三个子系统，详细代码参考：[门面模式](https://github.com/chenlanqing/java-code/tree/master/java-design/java-design-pattern/src/main/java/com/blue/fish/design/pattern/structural/facade)

## 5、使用场景

- Tomcat中有大量的Facade类：RequestFacade、ResponseFacade等
- Slf4j日志框架：屏蔽了各种日志框架的差异，提供了一个统一的日志接口给用户使用

# 十、模板模式


## 1、定义

定义一个操作中的算法的骨架，而将步骤延迟到子类中。模板方法使得子类可以不改变一个算法的结构即可重定义算法的某些特定步骤；
- 抽象类：实现了模板方法，定义了算法的骨架；
- 具体类：实现抽象类中的抽象方法，以完成完整的算法。

## 2、适用场景

- 在某些类的算法中，用了相同的方法，造成代码的重复；
- 控制子类扩展，子类必须遵守算法规则；

比如：员工入职到一家公司，刚入职的时候需要走入职流程，一般都有固定的流程，但是其中有些流程员工可以选择忽略，比如调档案、迁户口等员工可以选择的流程；

## 3、优缺点

- 优点：
    - 模板方法模式通过把不变的行为搬移到超类，去除了子类中的重复代码
    - 子类实现算法的某些细节，有助于算法的扩展；
    - 通过一个父类调用子类实现的操作，通过子类扩展增加新的行为，符合“开放-封闭原则”；
- 缺点：每个不同的实现都需要定义一个子类，这会导致类的个数的增加，设计更加抽象


## 4、UML类图

![](uml/模板模式类图.png)

## 5、使用场景

- JDK中的集合，比如AbstractList、AbstractSet、AbstractMap；
- JDK中的：AbstractQueuedSynchronizer，其模板方式实现有些特殊，没有抽象类存在，取而代之的是需要子类去实现那些方法通过一个方法体;
- JDK中类加载过程，ClassLoader类就使用了模板模式，去保证类加载过程中的唯一性，类加载过程如下：
    - 首先看是否有已经加载好的类。
    - 如果父类加载器不为空，则首先从父类类加载器加载。
    - 如果父类加载器为空，则尝试从启动加载器加载。
    - 如果两者都失败，才尝试从findClass方法加载。

## 6、与其他设计模式

- 工厂方法模式是模板方法模式的一种特殊实现

# 十一、桥接模式


## 1、定义

将抽象部分与它的实现部分分离，使它们都可以独立地变化。是结构型

更容易理解的表述是：实现系统可从多种维度分类，桥接模式将各维度抽象出来，各维度独立变化，之后可通过聚合，将各维度组合起来，减少了各维度间的耦合；

将抽象和抽象的具体实现进行解耦，这样可以使得抽象和抽象的具体实现可以独立进行变化，通过组合的方式建立两个类之间的联系，而不是继承

## 2、适用场景

- 当一个对象有多个变化因素时，可以考虑使用桥接模式，通过抽象这些变化因素，将依赖具体实现修改为依赖抽象；
- 当我们期望一个对象的多个变化因素可以动态变化，而且不影响客户端的程序使用时；
- 如果使用继承的实现方案，会导致产生很多子类，任何一个变化因素都需要产生多个类来完成，就要考虑桥接模式

## 3、UML类图

![](uml/桥接模式类图.png)

上述类图中即不同的银行有不同账户体系

## 4、优缺点

- 优点：
    - 降低了沿着两个或多个维度扩展时的复杂度，防止类的过度膨胀；
    - 解除了两个或多个维度之间的耦合，使它们沿着各自方向变化而不互相影响；
    - 分离了抽象部分及其具体实现部分；
    - 提高了系统可用性；

## 5、JDK或者框架中使用场景

- 只要你用到面向接口编程，其实都是在用桥接模式，比如JDBC，Driver、Connection等

# 十二、代理模式

## 1、基本概念

给目标对象提供一个代理对象，并由代理对象控制对目标对象的引用，通过引入代理对象的方式来间接访问目标对象，防止直接访问目标对象给系统带来的不必要复杂性

## 2、代理的分类

- 远程代理
- 虚拟代理
- 保护代理
- 智能引用代理

## 3、适用场景

## 4、优缺点

- 优点：
    - 代理模式能将代理对象与真实被调用的目标对象分离；
    - 一定程度上降低了系统的耦合度，扩展性好；
    - 保护目标对象；
    - 增强目标对象；
- 缺点：
    - 造成系统设计中类的数据增加；
    - 在客户端和目标对象增加一个代理对象，会造成请求处理速度变慢；
    - 增加系统的复杂度

## 5、代理的实现方式

### 5.1、静态代理

#### 5.1.1、基本概念
    
代理和被代理的对象在代理之前是确定的.都实现了相同的接口或者继承相同的抽象类;

#### 5.1.2、实现方式

- 继承：

    即代理对象继承被代理的对象，重写其方法时直接调用父类的方法；代理类一般要持有一个被代理的对象的引用，对于我们不关心的方法，全部委托给被代理的对象处理 自己处理我们关心的方法，静态代理对于这种，被代理的对象很固定，我们只需要去代理一个类或者若干固定的类，数量不是太多的时候，可以使用

- 聚合：（在当前类引用其他类对象），代理类与被代理类实现相同的接口

#### 5.1.3、最佳实现方式：聚合

### 5.2、动态代理

动态代理有一个强制性要求：就是被代理的类必须实现了某一个接口，或者本身就是接口，就像我们的Connection

#### 5.2.1、JDK 动态代理

- 两个核心类：位于`java.lang.reflect`包下

    - Proxy：动态代理类：`public static Object newProxyInstance(ClassLoader loader，Class<?>[] interfaces，InvocationHandler h)`返回代理类的一个实例，返回后的代理类可以被当作代理类使用

    - InvocationHandler：该接口值定义了一个方法，`public Object invoke(Object proxy， Method method， Object[] args)throws Throwable;`
    
        proxy ==> 被代理的类， method ==> 被代理的方法， args ==> 被代理的方法的参数数组

    - 生成代理类：byte[] classFile = ProxyGenerator.generateProxyClass("被代理的类", ConcreteSubject.class.getInterfaces());

- JDK 动态代理的实现步骤：
    - ①.创建一个实现接口 InvocationHandler 的类，必须实现 invoke 方法;
    - ②.创建被代理的类和接口;
    - ③.调用 Proxy 的静态方法 newProxyInstance 创建一个代理类;
    - ④.通过代理调用方法
- 如果不实现接口也可使用动态代理：具体是在实现 InvocationHandler 的方法时写成如下：
    ```java
    Method sourceMethod = source.getClass().getDeclaredMethod(method.getName()， method.getParameterTypes());
    sourceMethod.setAccessible(true);
    Object result = sourceMethod.invoke(source， args);
    // 而一般是写成如下：
    method.invoke(source， args);
    ```

#### 5.2.2、CGLIB 动态代理

##### 5.2.2.1、介绍

它的底层是通过使用一个小而快的字节码处理框架ASM（Java字节码操控框架）来转换字节码并生成新的类

##### 5.2.2.2、CGLib代理实现

使用cglib实现动态代理，需要在`MethodInterceptor`实现类中定义代理行为

代理行为在intercept方法中定义，同时通过getInstance方法（该方法名可以自定义）获取动态代理的实例，并且可以通过向该方法传入类对象指定被代理对象的类型。

由CGLIB创建的代理类，不会包含父类中的私有方法

#### 5.2.3、JDK 与 CGLIB 代理的比较

- JDK：只能代理实现了接口的类；没有实现接口的类不能实现 JDK 的动态代理
- CGLIB：针对类来实现代理的，对指定目标产生一个子类，通过方法拦截技术拦截所有父类方法的调用；CGLib是通过继承来实现的，因此不能代理被final修饰的类；
- JDK动态代理通过JVM实现代理类字节码的创建，cglib通过ASM创建字节码

#### 5.2.4、Spring中代理的选择

- 当Bean有实现接口时，Spring就会使用JDK的动态代理；
- 当Bean没有实现接口时，Spring使用CGLib
- 当然也可以强制使用CGLib，只需要在Spring配置中增加：`<aop:aspectj-autoproxy proxy-target-class="true" />`，使用Springboot时使用配置：`spring.aop.proxy-target-class=true`
- 在SpringBoot中，默认使用的就是CGLIB方式来创建代理：在它的配置文件中，spring.aop.proxy-target-class默认是true
    ```json
    {
      "name": "spring.aop.proxy-target-class",
      "type": "java.lang.Boolean",
      "description": "Whether subclass-based (CGLIB) proxies are to be created (true), as opposed to standard Java interface-based proxies (false).",
      "defaultValue": true
    }
    ```

Spring中动态代理的核心类：
- ProxyFactoryBean
- JdkDynamicAopProxy
- CglibAopProxy

### 5.3、静态代理与动态代理

#### 5.3.1、相同点

从虚拟机加载类的角度来讲，本质上是一样：都是在原有类的基础上，加入一些多出的行为，甚至完全替换原有的行为；

#### 5.3.2、区别

静态代理

如果在使用静态代理的时候需要写很多重复代码时，考虑改用动态带来来实现

动态代理的使用是为了解决这样一种问题，就是我们需要代理一系列类的某一些方法：最典型的应用就是 SpringAOP

# 十四、建造者模式


## 1、定义

将一个复杂的对象构建与它的表示分离，使得同样的构建过程可以创建不同的表示；

用户只需要指定需要见着的类型就可以得到他们，建造过程及细节不需要知道

## 2、适用场景

如果一个对象有非常复杂的内部结构，或者想把复杂的对象的创建和适用分离

## 3、优点



## 4、使用场景

- JDK：StringBuilder(StringBuffer)
- Guava：CacheBuilder
- Spring：BeanDefinitionBuilder
- Mybatis：SqlSessionFactoryBuilder

## 5、代码

```java
@Data
public class Computer {
    private String mainBoard;
    private String memory;
    private String hardDisk;
    private String cpu;
    private String power;
    public Computer(ComputerBuilder computerBuilder) {
        this.mainBoard = computerBuilder.mainBoard;
        this.memory = computerBuilder.memory;
        this.hardDisk = computerBuilder.hardDisk;
        this.cpu = computerBuilder.cpu;
        this.power = computerBuilder.power;
    }
    public static class ComputerBuilder{
        private String mainBoard;
        private String memory;
        private String hardDisk;
        private String cpu;
        private String power;

        public ComputerBuilder buildMainBoard(String mainBoard){
            this.mainBoard = mainBoard;
            return this;
        }
        public ComputerBuilder buildMemory(String memory) {
            this.memory = memory;
            return this;
        }
        public ComputerBuilder buildHardDisk(String hardDisk) {
            this.hardDisk = hardDisk;
            return this;
        }
        public ComputerBuilder buildCpu(String cpu) {
            this.cpu = cpu;
            return this;
        }
        public ComputerBuilder buildPower(String power) {
            this.power = power;
            return this;
        }
        public Computer assembleComputer(){
            return new Computer(this);
        }
    }
}

public class TestBuilderV2 {
    public static void main(String[] args) {
        Computer computer = new Computer.ComputerBuilder()
                .buildMainBoard("七彩虹")
                .buildCpu("因特尔")
                .buildHardDisk("希捷")
                .buildMemory("金士顿")
                .buildPower("金田")
                .assembleComputer();
        System.out.println(computer);
    }
}
```

## 6、UML类图

![](image/建造者模式类图.png)

首先建造者接口（Builder）和具体的建造者（ActualBuilder）应该是要新建的，而指挥者（Director）、产品（Product）

# 十五、组合模式

## 1、定义

将对象组合成树形结构以表示“部分整体”的层次结构。组合模式使得用户对单个对象和组合对象的使用具有一致性

组合模式用来表示部分与整体的层次结构（类似于树结构），而且也可以使用户对单个对象（叶子节点）以及组合对象（非叶子节点）的使用具有一致性，一致性的意思就是说，这些对象都拥有相同的接口

常见的场景有：公司、子公司以及部门；书的目录、文件系统、网站的菜单等，都是数结构

## 2、适用场景

- 如果你想表示“部分整体”的层次结构，可以使用组合模式；
- 如果你想让客户端可以忽略复杂的层次结构，使用统一的方式去操作层次结构中的所有对象，也可以使用组合模式

## 3、UML类图

![](uml/组合模式类图.png)

## 4、优缺点

- 优点：
    - 使客户端调用简单，它可以一致使用组合结构或是其中单个对象，简化了客户端代码；
    - 容易在组合体内增加对象部件。客户端不必因加入了新的部件而更改代码。有利于功能的扩展；
- 缺点：
    - 需要抉择使用透明方式还是安全方式；
    - 透明方式违背了面向对象的单一职责原则；安全方式增加了客户需要端判定的负担

# 十六、状态模式

# 1、定义

# 2、适用场景

# 3、实际应用

# 4、UML类图

# 5、注意事项

# 十七、原型模式

## 1、原型模式

指原型实例指定创建对象的种类，并且通过拷贝这些原型创建新的对象；

不需要知道任何创建的细节，不调用构造函数

## 2、适用场景

- 类初始化消耗较多资源
- new产生的一个对象需要非常繁琐的过程；
- 构造函数比较复杂
- 循环体中产生大量的对象

## 3、优缺点

- 必须配备克隆方法
- 深拷贝与浅拷贝要运用得当

不过实际当中我们使用原型模式时，也可以写一个基类实现Cloneable接口重写clone方法，然后让需要具有拷贝功能的子类继承自该类，这是一种节省代码量的常用方式

原型模式中的克隆可以破坏单例模式

## 4、示例代码

实际就是克隆代码的使用：注意浅拷贝与深拷贝的使用


## 5、JDK使用场景

所有实现了cloneable接口的都是使用了原型模式

# 十八、享元模式

## 1、定义

它使用共享物件，用来尽可能减少内存使用量以及分享资讯给尽可能多的相似物件；它适合用于当大量物件只是重复因而导致无法令人接受的使用大量内存。通常物件中的部分状态是可以分享。常见做法是把它们放在外部数据结构，当需要使用时再将它们传递给享元；

又称轻量级模式，其实结构型模式

## 2、适用场景

当对象数量太多时，将导致对象创建及垃圾回收的代价过高，造成性能下降等问题。享元模式通过共享相同或者相似的细粒度对象解决了这一类问题；

## 3、状态

- 内部状态：是存储在享元对象内部，一般在构造时确定或通过setter设置，并且不会随环境改变而改变的状态，因此内部状态可以共享；
- 外部状态：是随环境改变而改变、不可以共享的状态。外部状态在需要使用时通过客户端传入享元对象。外部状态必须由客户端保存

## 4、UML类图

![](uml/享元模式类图.png)

- FlyWeight：享元接口或者（抽象享元类），定义共享接口
- ConcreteFlyWeight：具体享元类，该类实例将实现共享
- UnSharedConcreteFlyWeight：非共享享元实现类
- FlyWeightFactory：享元工厂类，控制实例的创建和共享

## 5、优缺点

- 优点：
    - 享元模式的外部状态相对独立，使得对象可以在不同的环境中被复用（共享对象可以适应不同的外部环境）
    - 享元模式可共享相同或相似的细粒度对象，从而减少了内存消耗，同时降低了对象创建与垃圾回收的开销

- 缺点：
    - 外部状态由客户端保存，共享对象读取外部状态的开销可能比较大
    - 享元模式要求将内部状态与外部状态分离，这使得程序的逻辑复杂化，同时也增加了状态维护成本

## 6、JDK使用场景

- Integer中的IntegerCache


# 参考资料

* [JDK中设计模式](https://juejin.im/post/5cd842d56fb9a0323070efc0)
* [设计模式大杂烩](http://www.cnblogs.com/zuoxiaolong/p/pattern26.html)
* [Java设计模式](http://www.jasongj.com/design_pattern/summary/)
* [设计模式](http://www.cnblogs.com/wangjq/category/389973.html)
* [设计模式的应用](https://www.itcodemonkey.com/article/14407.html)
* [单例模式的七种写法](http://www.hollischuang.com/archives/205)
* [实现牛逼的单例模式](http://www.cnblogs.com/rjzheng/p/8946889.html)
* [单例与序列化](http://www.hollischuang.com/archives/1144)
* [单例模式与垃圾回收](http://blog.csdn.net/zhengzhb/article/details/7331354)
* [简单工厂模式](http://www.jasongj.com/design_pattern/simple_factory/)
* [简单工厂模式详解](http://www.cnblogs.com/zuoxiaolong/p/pattern4.html)
* [观察者模式](http://www.cnblogs.com/zuoxiaolong/p/pattern7.html)
* [观察者模式](http://www.jasongj.com/design_pattern/observer/)
* [观察者模式](https://www.cnblogs.com/wangjq/archive/2012/07/12/2587966.html)
* [策略模式](http://www.cnblogs.com/zuoxiaolong/p/pattern8.html)
* [代理模式详解](http://www.cnblogs.com/zuoxiaolong/p/pattern3.html)