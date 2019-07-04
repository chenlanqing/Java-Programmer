<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、源码分析](#%E4%B8%80%E6%BA%90%E7%A0%81%E5%88%86%E6%9E%90)
  - [1、签名](#1%E7%AD%BE%E5%90%8D)
  - [2、构造函数](#2%E6%9E%84%E9%80%A0%E5%87%BD%E6%95%B0)
  - [3、属性](#3%E5%B1%9E%E6%80%A7)
  - [4、方法](#4%E6%96%B9%E6%B3%95)
- [二、注意问题](#%E4%BA%8C%E6%B3%A8%E6%84%8F%E9%97%AE%E9%A2%98)
  - [1、double和float转BigDecimal](#1double%E5%92%8Cfloat%E8%BD%ACbigdecimal)
  - [2、关于除以0的问题](#2%E5%85%B3%E4%BA%8E%E9%99%A4%E4%BB%A50%E7%9A%84%E9%97%AE%E9%A2%98)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


# 一、源码分析

## 1、签名

```java
public class BigDecimal extends Number implements Comparable<BigDecimal>
```

## 2、构造函数

## 3、属性

## 4、方法

# 二、注意问题

## 1、double和float转BigDecimal

将`double`或者`float`数据转换为`BigDecimal`类型，最好使用`BigDecimal.valueOf()`，而不应该使用`new BigDecimal(double)`，因为：`new BigDecimal(0.1`)会把`0.1`当成: `0.1000000000000000055511151231257827021181583404541015625`，而`valueOf`其内部使用了`String`作为构造函数的入参，所以使用valueOf后`0.1`还是`0.1`

## 2、关于除以0的问题

`System.out.println(2 / 0)`  -> `ArithmeticException: / by zero`

`System.out.println(2.0/0);` -> `Infinity`
```
double i = 1.0 / 0;                
System.out.println(i);             //Infinity
System.out.println(i + 1);         //Infinity
System.out.println(i == i + 1);    //true
 
i = 0.0 / 0;
System.out.println(i);             //NaN
System.out.println(i + 1);         //NaN
System.out.println(i == i + 1);    //false
```
  