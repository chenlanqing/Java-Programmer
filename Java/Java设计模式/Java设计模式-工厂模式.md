* http://www.jasongj.com/design_pattern/simple_factory/
* http://www.cnblogs.com/zuoxiaolong/p/pattern4.html
# 一.简单工厂-静态工厂方法:由一个工厂对象决定创建出哪一种产品类的实例
    1.简单工厂模式解决的问题是如何去实例化一个合适的对象.
    2.简单工厂模式的核心思想就是:有一个专门的类来负责创建实例的过程
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
# 二.工厂方法:
    1.工厂方法:定义一个创建产品对象的工厂接口,将实际创建工作推迟到子类当中.核心工厂类不再负责产品的创建,
        这样核心类成为一个抽象工厂角色,仅负责具体工厂子类必须实现的接口
    2.工厂方法模式弥补了简单工厂模式不满足开闭原则的诟病,当我们需要增加产品时,只需要增加相应的产品和工厂类,
        而不需要修改现有的代码
    3.实例代码:
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
    4.工厂方法运用: JDBC 的 Driver 和 Connection
        适用的场景就是我们需要一个产品帮我们完成一项任务,但是这个产品有可能有很多品牌(像这里的mysql，oracle),
        为了保持我们对产品操作的一致性,我们就可能要用到工厂方法模式

# 三.抽象工厂
    1.为创建一组相关或相互依赖的对象提供一个接口,而且无需指定他们的具体类

