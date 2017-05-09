Java设计模式
一.设计模式:
1.是一套被反复使用的、多数人知晓的、经过分类编目的、代码设计经验的总结。使用设计模式
	是为了重用代码、让代码更容易被他人理解、保证代码可靠性
2.设计模式总共23种,可以分为三大类:
	2.1.创建型模式:提供了一种在创建对象的同时隐藏创建逻辑的方式，而不是使用新的运算符直接实例化对象。
		这使得程序在判断针对某个给定实例需要创建哪些对象时更加灵活:
		(1).工厂模式（Factory Pattern）
		(2).抽象工厂模式（Abstract Factory Pattern）
		(3).单例模式（Singleton Pattern）
		(4).建造者模式（Builder Pattern）
		(5).原型模式（Prototype Pattern）
	2.2.结构型模式:这些设计模式关注类和对象的组合。继承的概念被用来组合接口和定义组合对象获得新功能的方式:
		(6).适配器模式（Adapter Pattern）
		(7).桥接模式（Bridge Pattern）
		(8).过滤器模式（Filter、Criteria Pattern）
		(9).组合模式（Composite Pattern）
		(10).装饰器模式（Decorator Pattern）
		(11).外观模式（Facade Pattern）
		(12).享元模式（Flyweight Pattern）
		(13).代理模式（Proxy Pattern）
	2.3.行为型模式:这些设计模式特别关注对象之间的通信
		(14).责任链模式（Chain of Responsibility Pattern）
		(15).命令模式（Command Pattern）
		(16).解释器模式（Interpreter	Pattern）
		(17).迭代器模式（Iterator Pattern）
		(18).中介者模式（Mediator Pattern）
		(19).备忘录模式（Memento Pattern）
		(20).观察者模式（Observer Pattern）
		(21).状态模式（State Pattern）
		(22).空对象模式（Null Object	Pattern）
		(23).策略模式（Strategy Pattern）
		(24).模板模式（Template Pattern）
		(25).访问者模式（Visitor Pattern）

二.工厂模式与抽象工厂模式:
1.工厂模式:
	(1).意图：定义一个创建对象的接口，让其子类自己决定实例化哪一个工厂类，工厂模式使其创建过程延迟到子类进行。
	(2).主要解决：主要解决接口选择的问题。
	(3).何时使用：我们明确地计划不同条件下创建不同实例时。
		复杂对象适合使用工厂模式，而简单对象，特别是只需要通过 new 就可以完成创建的对象，无需使用工厂模式
	(4).如何解决：让其子类实现工厂接口，返回的也是一个抽象的产品。
	(5).关键代码：创建过程在其子类执行		
	
2.抽象工厂模式
	(1).意图：提供一个创建一系列相关或相互依赖对象的接口，而无需指定它们具体的类。
	(2).主要解决：主要解决接口选择的问题。
	(3).何时使用：系统的产品有多于一个的产品族，而系统只消费其中某一族的产品。
	(4).如何解决：在一个产品族里面，定义多个产品。
	(5).关键代码：在一个工厂里聚合多个同类产品	
	

四.代理模式	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	