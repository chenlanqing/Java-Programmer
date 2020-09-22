
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

`System.out.println(2.0/ 0);` -> `Infinity`
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

## 3、BigDecimal对象比较问题

看如下代码，输出是什么？
```java
BigDecimal bigDecimal = new BigDecimal(1);
BigDecimal bigDecimal1 = new BigDecimal(1);
System.out.println(bigDecimal.equals(bigDecimal1)); // true

BigDecimal bigDecimal2 = new BigDecimal(1);
BigDecimal bigDecimal3 = new BigDecimal(1.0);
System.out.println(bigDecimal2.equals(bigDecimal3)); // true

BigDecimal bigDecimal4 = new BigDecimal("1");
BigDecimal bigDecimal5 = new BigDecimal("1.0");
System.out.println(bigDecimal4.equals(bigDecimal5)); // false
```
在使用BigDecimal的equals方法对1和1.0进行比较的时候，有的时候是true（当使用int、double定义BigDecimal时），有的时候是false（当使用String定义BigDecimal时）

首先看equals方法的描述：`Compares this  BigDecimal with the specified Object for equality.  Unlike compareTo, this method considers two BigDecimal objects equal only if they are equal in value and scale (thus 2.0 is not equal to 2.00 when compared by  this method)`

意思是：equals方法和compareTo并不一样，equals方法会比较两部分内容，分别是值（value）和精度（scale）
```java
public boolean equals(Object x) {
    if (!(x instanceof BigDecimal))
        return false;
    BigDecimal xDec = (BigDecimal) x;
    if (x == this)
        return true;
    if (scale != xDec.scale) // 比较精度
        return false;
    long s = this.intCompact;
    long xs = xDec.intCompact;
    if (s != INFLATED) {
        if (xs == INFLATED)
            xs = compactValFor(xDec.intVal);
        return xs == s;
    } else if (xs != INFLATED)
        return xs == compactValFor(this.intVal);

    return this.inflated().equals(xDec.inflated());
}
```

BigDecimal不同的构造函数，对应的精度是不同的

**BigDecimal(long) 和BigDecimal(int)：**因为是整数，所以精度就是0；
```java
public BigDecimal(int val) {
    this.intCompact = val;
    this.scale = 0;
    this.intVal = null;
}
public BigDecimal(long val) {
    this.intCompact = val;
    this.intVal = (val == INFLATED) ? INFLATED_BIGINT : null;
    this.scale = 0;
}
```

**BigDecimal(double)：**当我们使用`new BigDecimal(0.1)`创建一个BigDecimal 的时候，其实创建出来的值并不是整好等于0.1的，而是`0.1000000000000000055511151231257827021181583404541015625` 。这是因为doule自身表示的只是一个近似值，那么他的精度就是这个数字的位数，即55
```java
public BigDecimal(double val) {
    this(val, MathContext.UNLIMITED);
}
public BigDecimal(double val, MathContext mc) {
    ....
}
```

**BigDecimal(string)：**当我们使用`new BigDecimal("0.1")`创建一个BigDecimal 的时候，其实创建出来的值正好就是等于`0.1`的。那么他的精度也就是1；如果使用`new BigDecimal("0.10000")`，那么创建出来的数就是0.10000，精度也就是5。

BigDecimal中提供了compareTo方法，这个方法就可以只比较两个数字的值，如果两个数相等，则返回0
```java
public int compareTo(BigDecimal val) {
    // Quick path for equal scale and non-inflated case.
    if (scale == val.scale) {
        long xs = intCompact;
        long ys = val.intCompact;
        if (xs != INFLATED && ys != INFLATED)
            return xs != ys ? ((xs > ys) ? 1 : -1) : 0;
    }
    int xsign = this.signum();
    int ysign = val.signum();
    if (xsign != ysign)
        return (xsign > ysign) ? 1 : -1;
    if (xsign == 0)
        return 0;
    int cmp = compareMagnitude(val);
    return (xsign > 0) ? cmp : -cmp;
}
```
  