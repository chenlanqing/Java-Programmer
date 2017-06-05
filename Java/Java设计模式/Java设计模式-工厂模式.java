// http://www.jasongj.com/design_pattern/simple_factory/
// http://www.cnblogs.com/zuoxiaolong/p/pattern4.html
一.简单工厂-静态工厂方法:由一个工厂对象决定创建出哪一种产品类的实例
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
二.工厂方法:
1.工厂方法:定义一个创建产品对象的工厂接口,将实际创建工作推迟到子类当中.核心工厂类不再负责产品的创建,
	这样核心类成为一个抽象工厂角色,仅负责具体工厂子类必须实现的接口
2.工厂方法模式弥补了简单工厂模式不满足开闭原则的诟病,当我们需要增加产品时,只需要增加相应的产品和工厂类,
	而不需要修改现有的代码
三.抽象工厂
































































