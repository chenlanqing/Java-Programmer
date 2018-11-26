
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
  