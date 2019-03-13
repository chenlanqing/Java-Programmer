<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、各个版本特性](#%E4%B8%80%E5%90%84%E4%B8%AA%E7%89%88%E6%9C%AC%E7%89%B9%E6%80%A7)
  - [1、JDK5](#1jdk5)
  - [2、JDK6](#2jdk6)
  - [3、JDK7](#3jdk7)
  - [4、JDK8](#4jdk8)
  - [5、JDK9.](#5jdk9)
  - [6、JDK10](#6jdk10)
- [二、JDK8新特性](#%E4%BA%8Cjdk8%E6%96%B0%E7%89%B9%E6%80%A7)
  - [1、Java语言新特性](#1java%E8%AF%AD%E8%A8%80%E6%96%B0%E7%89%B9%E6%80%A7)
    - [1.1、Lambda表达式和函数式接口](#11lambda%E8%A1%A8%E8%BE%BE%E5%BC%8F%E5%92%8C%E5%87%BD%E6%95%B0%E5%BC%8F%E6%8E%A5%E5%8F%A3)
    - [1.2、接口的默认方法和静态方法](#12%E6%8E%A5%E5%8F%A3%E7%9A%84%E9%BB%98%E8%AE%A4%E6%96%B9%E6%B3%95%E5%92%8C%E9%9D%99%E6%80%81%E6%96%B9%E6%B3%95)
    - [1.3、方法引用](#13%E6%96%B9%E6%B3%95%E5%BC%95%E7%94%A8)
    - [1.4、重复注解](#14%E9%87%8D%E5%A4%8D%E6%B3%A8%E8%A7%A3)
    - [1.5、更好的类型推断](#15%E6%9B%B4%E5%A5%BD%E7%9A%84%E7%B1%BB%E5%9E%8B%E6%8E%A8%E6%96%AD)
    - [1.6、拓宽注解的应用场景](#16%E6%8B%93%E5%AE%BD%E6%B3%A8%E8%A7%A3%E7%9A%84%E5%BA%94%E7%94%A8%E5%9C%BA%E6%99%AF)
  - [2、编译器新特性](#2%E7%BC%96%E8%AF%91%E5%99%A8%E6%96%B0%E7%89%B9%E6%80%A7)
    - [2.1、参数名称](#21%E5%8F%82%E6%95%B0%E5%90%8D%E7%A7%B0)
  - [3、Java官方库的新特性](#3java%E5%AE%98%E6%96%B9%E5%BA%93%E7%9A%84%E6%96%B0%E7%89%B9%E6%80%A7)
    - [3.1、Optional](#31optional)
    - [3.2、Streams](#32streams)
    - [3.3、Date/Time API(JSR 310)](#33datetime-apijsr-310)
    - [3.4、Nashorn JavaScript引擎](#34nashorn-javascript%E5%BC%95%E6%93%8E)
    - [3.5、Base64](#35base64)
    - [3.6、并行数组](#36%E5%B9%B6%E8%A1%8C%E6%95%B0%E7%BB%84)
    - [3.7、并发性](#37%E5%B9%B6%E5%8F%91%E6%80%A7)
  - [参考资料](#%E5%8F%82%E8%80%83%E8%B5%84%E6%96%99)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 一、各个版本特性

## 1、JDK5

- 泛型
- 枚举
- 自动装箱拆箱
- 可变参数
- 元注解
- foreach循环（增强for、for/in）
- 静态导入

  ```java
  import static java.lang.System.err;
  import static java.lang.System.out;
  err.println(msg); 
  ```
- 格式化（System.out.println 支持%s %d等格式化输出）

  System.out.println("Line %d: %s%n", i++, line);

- 线程框架/数据结构 JUC
- Arrays工具类/StringBuilder/instrument

## 2、JDK6

- 支持脚本语言
- 引入JDBC 4.0 API
- 引入Java Compiler API,可以实现进程内编译，动态产生Java代码；
- 可插拔注解；
- 增加对Native PKI、Java GSS、Kerberos 和 LDAP 的支持
- 继承Web Services

## 3、JDK7

- switch语句块中允许以字符串作为分支条件；
- 在创建泛型对象时应用类型推断；钻石语法:Map<String， List<String>> data = new HashMap()；
- 在一个语句块中捕获多种异常；
- 支持动态语言；
- 支持 try-with-resources；
- 引入Java NIO.2开发包；
- 数值类型可以用2进制字符串表示，并且可以在字符串表示中添加下划线；

  Java7前支持十进制（123）、八进制（0123）、十六进制（0X12AB），Java7添加二进制表示（0B11110001、0b11110001）；

  Java7中支持在数字量中间添加’_'作为分隔符。更直观，如（12_123_456）。下划线仅仅能在数字中间。编译时编译器自己主动删除数字中的下划线

- null 值的自动处理；
- JSR292与InvokeDynamic指令
- fork/join framework

## 4、JDK8

[http://www.open-open.com/lib/view/open1403232177575.html]

- 函数式接口 FunctionalInterface 
- Lambda表达式
- 接口的增强.接口中的默认方法.默认方法的继承.单接口实现情况下，默认方法可以直接用， 多接口实现情况下一旦出现同方法签名的默认方法，那么必须显式覆盖，否则编译不通过.
- Stream 迭代
- 新增时间 API
- JVM 的PermGen空间被移除，取代它的是Metaspace(JEP 122)元空间
- 数组并行(parallel)操作

## 5、JDK9

- Jigsaw 项目；模块化源码
- 简化进程API
- 轻量级 JSON API
- 钱和货币的API
- 改善锁争用机制
- 代码分段缓存
- 智能Java编译， 第二阶段
- HTTP 2.0客户端
- Kulla计划: Java的REPL实现

## 6、JDK10

- 本地变量类型推断
- 统一JDK仓库
- 垃圾回收器接口
- G1的并行Full GC
- 应用程序类数据共享
- ThreadLocal握手机制

# 二、JDK8新特性

## 1、Java语言新特性

### 1.1、Lambda表达式和函数式接口

### 1.2、接口的默认方法和静态方法

### 1.3、方法引用

### 1.4、重复注解

### 1.5、更好的类型推断

### 1.6、拓宽注解的应用场景


## 2、编译器新特性

### 2.1、参数名称


## 3、Java官方库的新特性

### 3.1、Optional

### 3.2、Streams

### 3.3、Date/Time API(JSR 310)

### 3.4、Nashorn JavaScript引擎

### 3.5、Base64

### 3.6、并行数组

### 3.7、并发性


## 参考资料

* [Java各个版本特性最全总结](https://mp.weixin.qq.com/s/wQW3tZmCs50RjzMtbxOgpQ)
* [Java8新特性](https://www.jianshu.com/p/5b800057f2d8)
* [JDK8新特性指南](http://www.open-open.com/lib/view/open1403232177575.html)
* [Java8教程](https://mp.weixin.qq.com/s/qYsjT0QUD2BgeEDIkhymmg)