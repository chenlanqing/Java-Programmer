文章链接: 
/*
http://www.nowamagic.net/librarys/veda/detail/1648
http://web.jobbole.com/9648/
http://www.imooc.com/article/2088 [图例]
*/
理论上:只有函数对象才具有原型对象属性

图: Javascript原型关系图.png
1.关于Javascript继承:
	JavaScript 不包含传统的类继承模型,而是使用 prototypal 原型模型,
	虽然这经常被当作是 JavaScript 的缺点被提及，其实基于原型的继承模型比传统的类继承还要强大.
	实现传统的类继承模型是很简单，但是实现 JavaScript 中的原型继承则要困难的多。
	JavaScript 是唯一一个被广泛使用的基于原型继承的语言;

2.原型:原型是一个对象,其他对象可以通过它实现属性继承.任何一个对象都可以成为原型
	所有的对象在默认的情况下都有一个原型，因为原型本身也是对象，
	所以每个原型自身又有一个原型(只有一种例外,默认的对象原型在原型链的顶端)
	(1).获取对象的原型:
		var a = {}; 
		//Firefox 3.6 and Chrome 5 
		Object.getPrototypeOf(a); //[object Object] 
		//Firefox 3.6, Chrome 5 and Safari 4 
		a.__proto__; //[object Object] 
		//all browsers 
		a.constructor.prototype; //[object Object]
	(2).函数的原型属性(function’s prototype property )其实和实际的原型("prototype")没有关系
		①.函数A的原型属性(prototype property )是一个对象，当这个函数被用作构造函数来创建实例时，
		该函数的原型属性将被作为原型赋值给所有对象实例
		==> 一个函数的原型属性(function’s prototype property )其实和实际的原型("prototype")是没有关系的
		②.对象本身是没有 "constructor" 属性的,通过原型链查找的
		在JavaScript的原型对象中，还包含一个"constructor"属性，这个属性对应创建所有指向该原型的实例的构造函数
			通过"constructor"这个属性，我们可以来判断一个对象是不是数组类型
		③.在JavaScript中，每个函数都有一个 "prototype" 属性,当一个函数被用作构造函数来创建实例时,
		该函数的"prototype"属性值将被作为原型赋值给所有对象实例(也就是设置实例的"__proto__"属性)，
		也就是说，所有实例的原型引用的是函数的"prototype"属性
		④."prototype"属性是函数对象特有的，如果不是函数对象，将不会有这样一个属性
		//(example fails in IE) 
		var A = function(name) { 
		  	this.name = name; 
		} 
		A.prototype == A.__proto__; //false 
		A.__proto__ == Function.prototype; //true - A's prototype is set to its constructor's prototype property
		//创建一个函数b
		var b = function(){ var one; }
		//使用b创建一个对象实例c
		var c = new b();
		//查看b 和c的构造函数
		b.constructor;  // function Function() { [native code]}
		b.constructor==Function.constructor; //true
		c.constructor; //实例c的构造函数 即 b function(){ var one; }
		c.constructor==b //true
		//b是一个函数，查看b的原型如下
		b.constructor.prototype // function (){}
		b.__proto__  //function (){}

		//b是一个函数，由于javascript没有在构造函数constructor和函数function之间做区分，所以函数像constructor一样，
		//有一个原型属性，这和函数的原型(b.__proto__ 或者b.construtor.prototype)是不一样的
		b.prototype //[object Object]   函数b的原型属性

		b.prototype==b.constructor.prototype //fasle
		b.prototype==b.__proto__  //false
		b.__proto__==b.constructor.prototype //true

		//c是一个由b创建的对象实例，查看c的原型如下
		c.constructor.prototype //[object Object] 这是对象的原型
		c.__proto__ //[object Object] 这是对象的原型

		c.constructor.prototype==b.constructor.prototype;  //false  c的原型和b的原型比较
		c.constructor.prototype==b.prototype;  //true c的原型和b的原型属性比较

		//为函数b的原型属性添加一个属性max
		b.prototype.max = 3
		//实例c也有了一个属性max
		c.max  //3
		// 上面的例子中，对象实例c的原型和函数的b的原型属性是一样的，如果改变b的原型属性，则对象实例c 的原型也会改变
	(3).在JavaScript中有个 Function 对象（类似 Object），这个对象本身是个函数；
		所有的函数（包括 Function，Object）的原型（proto）都是"Function.prototype"
		Function.prototype.__proto__ ===> Object{}
	(4).关于 "__proto__" 与 "prototype":
		①.prototype：原型对象, 通过原型可以实现对象的属性继承(理论上，只有函数对象才具有原型对象属性),
			原型对象中的属性可以被持有该原型对象的函数对象访问，也可以被该函数对象的实例对象访问;
		②.[[Prototype]]：原型引用, 现在部分浏览器实现为非标准的proto。JS中每个对象都有一个proto属性，
			这个属性指向称该对象为实例对象的那个函数的 prototype (注意:原型对象也是对象,所以原型对象也有__proto__属性)。
			对象通过__proto__属性,可以访问 prototype 中的属性
		③.实例对象: 一个对象（包括函数对象）的__proto__指向一个函数的prototype，我们把这个对象称为这个函数的实例对象;
		④.继承:一个函数对象的 prototype.__proto__指向另一个函数的 prototype,我们称第一个函数继承于第二个函数，
			继承只能在函数对象之间发生;
		==> 所有函数对象的__proto__指向 Function.prototype, 我们称自定义的函数对象是 Function 函数对象的实例对象
		==> Function.prototype.__proto__指向 Object.prototype,Function 的原型对象是 Object 函数对象的实例
		==> 对于所有的对象，都有"__proto__"属性，这个属性对应该对象的原型;
		==> 对于函数对象，除了"__proto__"属性之外，还有"prototype"属性，当一个函数被用作构造函数来创建实例时，
			该函数的"prototype"属性值将被作为原型赋值给所有对象实例(也就是设置实例的"__proto__"属性)
	(5).关于原型与原型对象的总结:
		==> 所有的对象都有"proto"属性，该属性对应该对象的原型
		==> 所有的函数对象都有"prototype"属性，该属性的值会被赋值给该函数创建的对象的"proto"属性
		==> 所有的原型对象都有"constructor"属性，该属性对应创建所有指向该原型的实例的构造函数
		==> 函数对象和原型对象通过"prototype"和"constructor"属性进行相互关联
	2.1.原型的使用:
		(1).不使用原型:
				var decimalDigits = 2,
					tax = 5;
				function add(x, y) {
				    return x + y;
				}
				function subtract(x, y) {
				    return x - y;
				}
				//alert(add(1, 3));
		(2).使用原型方式1:
				var Calculator = function (decimalDigits, tax) {
				    this.decimalDigits = decimalDigits;
				    this.tax = tax;
				};
				// 通过给Calculator对象的prototype属性赋值对象字面量来设定Calculator对象的原型
				Calculator.prototype = {
				    add: function (x, y) {
				        return x + y;
				    },
				    subtract: function (x, y) {
				        return x - y;
				    }
				};
				//alert((new Calculator()).add(1, 3));
		(3).使用原型方式2:在赋值原型prototype的时候使用function立即执行的表达式来赋值，即如下格式
				Calculator.prototype = function () { } ();
				// 就是可以封装私有的function，通过return的形式暴露出简单的使用名称，以达到public/private的效果
				Calculator.prototype = function () {
				    add = function (x, y) {
				        return x + y;
				    },
				    subtract = function (x, y) {
				        return x - y;
				    }
				    return {
				        add: add,
				        subtract: subtract
				    }
				} ();
				//alert((new Calculator()).add(11, 3));
		(4).分步声明:上述使用原型的时候，有一个限制就是一次性设置了原型对象
				var BaseCalculator = function () {
				    //为每个实例都声明一个小数位数
				    this.decimalDigits = 2;
				};			        
				//使用原型给BaseCalculator扩展2个对象方法
				BaseCalculator.prototype.add = function (x, y) {
				    return x + y;
				};
				BaseCalculator.prototype.subtract = function (x, y) {
				    return x - y;
				};
			主要目的是:看如何将BaseCalculator对象设置到真正的Calculator的原型上,声明如下代码:
				var BaseCalculator = function() {
				    this.decimalDigits = 2;
				};
				BaseCalculator.prototype = {
				    add: function(x, y) {
				        return x + y;
				    },
				    subtract: function(x, y) {
				        return x - y;
				    }
				};
				// 声明对象
				var Calculator = function () {
				    //为每个实例都声明一个税收数字
				    this.tax = 5;
				};			        
				Calculator.prototype = new BaseCalculator();
			看到 Calculator 的原型是指向到BaseCalculator的一个实例上，目的是让 Calculator 集成它的add(x,y)和subtract(x,y)
			这2个function，还有一点要说的是，由于它的原型是 BaseCalculator 的一个实例，所以不管你创建多少个 Calculator
			对象实例，他们的原型指向的都是同一个实例
				var calc = new Calculator();
				alert(calc.add(1, 1));
				//BaseCalculator 里声明的decimalDigits属性，在 Calculator里是可以访问到的
				alert(calc.decimalDigits); 
			上面的代码，运行以后，我们可以看到因为Calculator的原型是指向BaseCalculator的实例上的，
			所以可以访问他的decimalDigits属性值，那如果我不想让Calculator访问BaseCalculator的构造函数里声明的属性值，
			那怎么办呢？
				var Calculator = function () {
				    this.tax= 5;
				};
				Calculator.prototype = BaseCalculator.prototype;
			通过将BaseCalculator的原型赋给Calculator的原型，这样你在Calculator的实例上就访问不到那个decimalDigits值了;

	2.2.重写原型:在使用第三方JS类库的时候，往往有时候他们定义的原型方法是不能满足我们的需要，但是又离不开这个类库，
		所以这时候我们就需要重写他们的原型中的一个或者多个属性或 function:
		//覆盖前面Calculator的add() function 
		Calculator.prototype.add = function (x, y) {
		    return x + y + this.tax;
		};
		var calc = new Calculator();
		alert(calc.add(1, 1));
		// 有一点需要注意：那就是重写的代码需要放在最后，这样才能覆盖前面的代码

	2.3.原型链:对象的创建影响原型链
		2.3.1.属性查找:
			(1).当查找一个对象的属性时，JavaScript 会向上遍历原型链，直到找到给定名称的属性为止，
			到查找到达原型链的顶部 - 也就是 Object.prototype - 但是仍然没有找到指定的属性，
			就会返回 undefined，我们来看一个例子:
				function foo() {
				    this.add = function (x, y) {
				        return x + y;
				    }
				}
				foo.prototype.add = function (x, y) {
				    return x + y + 10;
				}
				Object.prototype.subtract = function (x, y) {
				    return x - y;
				}
				var f = new foo();
				alert(f.add(1, 2)); //结果是3，而不是13
				alert(f.subtract(1, 2)); //结果是-1
			(2).就是属性在查找的时候是先查找自身的属性，如果没有再查找原型，再没有，再往上走，
			一直插到Object的原型上，所以在某种层面上说，用 for in语句遍历属性的时候，效率也是个问题
			(3).可以赋值任何类型的对象到原型上，但是不能赋值原子类型的值，比如如下代码是无效的：
				function Foo() {}
				Foo.prototype = 1; // 无效
			(4).hasOwnProperty()函数:Object.prototype的一个方法,能判断一个对象是否包含自定义属性而不是原型链上的属性,
				因为hasOwnProperty 是 JavaScript 中唯一一个处理属性但是不查找原型链的函数
				如果 hasOwnProperty 被非法占用:
					var foo = {
					    hasOwnProperty: function() {
					        return false;
					    },
					    bar: 'Here be dragons'
					};
					foo.hasOwnProperty('bar'); // 总是返回 false
					// 使用{}对象的 hasOwnProperty，并将其上下为设置为foo
					{}.hasOwnProperty.call(foo, 'bar'); // true
		var a = {};
		a.__proto__ ---> Object.prototype 
		Object.prototype.__proto__ ---> null
3.原型继承:
	prototype 通过__proto__被链接了起来我们把 prototype 对象之间的关系叫做原型链
	3.1.基本模式:这种是最简单实现原型继承的方法，直接把父类的对象赋值给子类构造函数的原型,
		// 这种方法的优点很明显，实现十分简单，不需要任何特殊的操作；同时缺点也很明显，如果子类需要做跟父类构造函数
		// 中相同的初始化动作，那么就得在子类构造函数中再重复一遍父类中的操作：
		var Parent = function(name){
		    this.name = name || 'parent' ;
		} ;
		Parent.prototype.getName = function(){
		    return this.name ;
		} ;
		Parent.prototype.obj = {a : 1} ;

		var Child = function(name){
		    this.name = name || 'child' ;
		} ;
		Child.prototype = new Parent() ;

		var parent = new Parent('myParent') ;
		var child = new Child('myChild') ;
		console.log(parent.getName()) ; //myParent
		console.log(child.getName()) ; //myChild
	3.2.借用构造函数
		var Parent = function(name){
		    this.name = name || 'parent' ;
		} ;
		Parent.prototype.getName = function(){
		    return this.name ;
		} ;
		Parent.prototype.obj = {a : 1} ;

		var Child = function(name){
		    Parent.apply(this,arguments) ;
		} ;
		Child.prototype = new Parent() ;
		var parent = new Parent('myParent') ;
		var child = new Child('myChild') ;
		console.log(parent.getName()) ; //myParent
		console.log(child.getName()) ; //myChild



4.关于原型的例子:
	function Person(){}
	var will = new Person();
	以下输出结果:
	(1).will.__proto__ === Person.prototype // true
		will.__proto__ ==> Person{} //包含两个属性:constructor, __proto__
	
5.JavaScript 中两个内置函数对象: Function, Object
	5.1.function Object() :名字为 Object 的函数对象
		Object.prototype = function Object()
		Object.prototype.constructor = function Object() { [native code] }
		Object.__proto__ = function(){}
		Object.__proto__.constructor = function Function(){ [native code] }
		Object.prototype.__proto__ = null;
	5.2.function Function() 名为 Function 的函数对象
		Function.prototype = function (){}
		Function.prototype.constructor = function Function() { [native code] }
		Function.prototype.__proto__ = Object {}
		Function.__proto__=function(){}
		Function.__proto__.constructor= function Function(){ [native code] }
		Function.__proto__.__proto__=Object{}
		Function 的原型和原型引用指向的是同一个 function() 的匿名函数
	5.3.函数对象(包括自定义和内建的函数)都是 Function 的实例对象。所有的非函数对象都是 Object 的实例对象




























