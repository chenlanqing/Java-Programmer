<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [一、简单工厂](#%E4%B8%80%E7%AE%80%E5%8D%95%E5%B7%A5%E5%8E%82)
- [二、工厂方法](#%E4%BA%8C%E5%B7%A5%E5%8E%82%E6%96%B9%E6%B3%95)
- [三、抽象工厂](#%E4%B8%89%E6%8A%BD%E8%B1%A1%E5%B7%A5%E5%8E%82)
- [参考资料](#%E5%8F%82%E8%80%83%E8%B5%84%E6%96%99)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

# 一、简单工厂

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

# 二、工厂方法

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

# 三、抽象工厂

- 抽象工厂模式提供了一个创建一系列相关或相互依赖对象的接口，无需指定他们具体的类；
- 适用场景：客户端不依赖产品实例如何被创建、实现等细节；强调一系列相关的产品对象一起使用创建对象需要大量重复代码；提供了产品类的类库
- 优点：具体产品在应用层隔离
- JDK中使用场景：Connection、Statement、PreparedStatment和mybatis中的SqlSessionFactory、SqlSession
- 实现：

    ```java
    
    ```


# 参考资料

* [简单工厂模式](http://www.jasongj.com/design_pattern/simple_factory/)
* [简单工厂模式详解](http://www.cnblogs.com/zuoxiaolong/p/pattern4.html)
