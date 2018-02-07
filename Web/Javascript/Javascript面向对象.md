* 深入理解JavaScript系列:
	http://www.cnblogs.com/TomXu/archive/2011/12/15/2288411.html

## 1.var 和 function 的提前声明:
	(1).var 和 function 都会提前声明,而且 function 是优于 var 声明的(如果同时存在);
		E.G:
			function fn(a){
				console.log(a);
				var a = 2;
				function a(){}
				console.log(a);
			}
			fn(1);
		OutPut : 提前声明后输出的a是个function，然后代码往下执行a进行重新赋值了，故第二次输出是2
			function a(){}
			2
		函数声明优于变量声明,在代码逐行执行前,函数声明和变量声明会提前进行,而函数声明又会优于变量声明,
		这里的优于可以理解为晚于变量声明后,如果函数名和变量名相同,函数声明就能覆盖变量声明;
		
		foo(); // 1
		var foo = 0;
		function foo(){
		    console.log(1);
		}
		foo(); // foo is not a function,函数声明完后,再重写赋值为 0
		foo = function(){
		    console.log(2);
		};
		foo();
		// 上述代码输出结果:
	(2).函数声明会覆盖变量声明，但不会覆盖变量赋值
		function value(){
		    return 1;
		}
		var value;
		alert(typeof value);    //"function"
		// 如果变量赋值了
		function value(){
		    return 1;
		}
		var value = 1;
		alert(typeof value);    //"number"
	(3).函数声明只能裸露于全局作用域下或位于函数体中,
		从句法上讲，它们不能出现在块中，例如不能出现在 if、while 或 for 语句中。因为块只能包含语句，
## 2.关于全局变量与局部变量:
	function 内声明的变量是局部变量;
	没有使用 var 声明的变量不管在何处都是全局变量;
	while{...},if(){...},for(...)之内的都是全局变量(除非本身包含在function内);
	变量的隐式声明;
	
## 3.给基本类型数据添加属性,不报错,但取值时是undefined

## 4.判断一个字符串中出现次数最多的字符,并统计次数
	(1).HashTable方式:
			var s = 'aaabbbcccaaabbbaaa';
			var obj = {};
			var maxn = -1;
			var letter;
			for(var i=0;i<s.length;i++){
				if(obj[s[i]]){
					obj[s[i]]++;
					if(obj[s[i]] > maxn){
						maxn = obj[s[i]];
						letter = s[i];
					}
				}else{
					obj[s[i]] = 1;
					if(obj[s[i]] > maxn){
						maxn = obj[s[i]];
						letter = s[i];
					}
				}				
			}
			console.log(letter + ":" + maxn);	
	(2).正则表达式:
			var s = 'aaabbbcccaaabbbaaa';
			var a = s.split('');
			a.sort();
			s = a.join('');
			var pattern = /(\w)\1*/g;
			var ans = s.match(pattern);
			ans.sort(function(a,b){
				return a.length < b.length;
			})
			console.log(ans[0][0] + ":" + ans[0].length);

## 6.Javascript面向对象编程:ECMAScript是基于原型实现的面向对象编程语言
	http://www.cnblogs.com/TomXu/archive/2012/02/03/2330295.html
	6.1.概论、范式与思想
		ECMAScript支持包括结构化、面向对象、函数式、命令式等多种编程方式，某些情况下还支持面向方面编程
		6.1.1.基于类特性与基于原型:
			(1).基于类特性:
				①.创建一个对象之前，必须声明类，首先有必要界定其类
				②.因此，该对象将由抽象成自身“象形和相似性”（结构和行为）的类里创建
				③.方法是通过了严格的，直接的，一成不变的继承链来处理
				④.子类包含了继承链中所有的属性（即使其中的某些属性是子类不需要的）;
				⑤.创建类实例，类不能（因为静态模型）来改变其实例的特征（属性或方法）;
				⑥.实例（因为严格的静态模型）除了有该实例所对应类里声明的行为和属性以外，是不能额外的行为或属性的。
			(2).基于原型的OOP:
				原型是一个对象，它是用来作为其他对象的原始copy，或者如果一些对象没有自己的必要特性，
				原型可以作为这些对象的一个委托而当成辅助对象
				关键概念:
					①.基本概念是对象
					②.对象是完全动态可变的（理论上完全可以从一个类型转化到另一个类型）
					③.对象没有描述自己的结构和行为的严格类，对象不需要类
					④.对象没有类类但可以可以有原型，他们如果不能响应消息的话可以委托给原型
					⑤.在运行时随时可以改变对象的原型;
					⑥.在基于委托的模型中，改变原型的特点，将影响到与该原型相关的所有对象;
					⑦.在concatenative原型模型中，原型是从其他对象克隆的原始副本，并进一步成为完全独立的副本原件，原型特性的变换不会影响从它克隆的对象
					⑧.如果不能响应消息，它的调用者可以采取额外的措施（例如，改变调度）
					⑨.对象的失败可以不由它们的层次和所属哪个类来决定，而是由当前特性来决定
	6.2.数据类型:
		标准规范里定义了9种数据类型，但只有6种是在ECMAScript程序里可以直接访问的,
		它们是：Undefined、Null、Boolean、String、Number、Object
		6.2.1.原始值类型:undefined、null、boolean、string、number
			这些值是在底层上直接实现的，他们不是object，所以没有原型，没有构造函数;
		6.2.2.Object 类型:对象是一个包含key-value对的无序集合(不要和Object构造函数混淆了)
			(1).动态性:可以随时添加,删除,修改属性
			(2).内置对象、原生对象及宿主对象:
				内置对象和元素对象是被ECMAScript规范定义和实现的;
				所有ECMAScript实现的对象都是原生对象,宿主对象是由宿主环境提供的;
			(3).String, Boolean, Number 对象:
				这些对象的创建，是通过相应的内置构造器创建，并且包含原生值作为其内部属性，这些对象可以转换省原始值，反之亦然
				也有对象是由特殊的内置构造函数创建:Function(函数对象构造器)、Array(数组构造器), RegExp(正则表达式构造器)、
				Math(数学模块)、 Date(日期的构造器)等等，这些对象也是Object对象类型的值;
				**基于字面量的还是构造器的，正则都是创建新对象
			(4).ES5标准可以让我们创建没原型的对象（使用Object.create(null)方法实现）对;
			(5).对象转换:将对象转化成原始值可以用valueOf方法,但如果不用new关键字就是将对象转化成原始值,就相当于隐式的valueOf方法调用
				valueOf的默认值会根据根据对象的类型改变（如果不被覆盖的话），对某些对象，他返回的是this
				var a = new Number(1);
				var primitiveA = Number(a); // 隐式"valueOf"调用
				var alsoPrimitiveA = a.valueOf(); // 显式调用				 
				alert([
				  typeof a, // "object"
				  typeof primitiveA, // "number"
				  typeof alsoPrimitiveA // "number"
				]);
				var c = {
				  x: 10,
				  y: 20,
				  valueOf: function () {
				    return this.x + this.y;
				  }
				};				 
				var d = {
				  x: 30,
				  y: 40,
				  // 和c的valueOf功能一样
				  valueOf: c.valueOf
				};				 
				alert(c + d); // 100
			(6).和转化成原始值（ToPrimitive）相比，将值转化成对象类型也有一个转化规范
				①.一个显式方法是使用内置的Object构造函数作为function来调用ToObject（有些类似通过new关键字也可以）：
					var n = Object(1); // [object Number]
					var s = Object('test'); // [object String]					 
					// 一些类似，使用new操作符也可以
					var b = new Object(true); // [object Boolean]					 
					// 应用参数new Object的话创建的是简单对象
					var o = new Object(); // [object Object]					 
					// 如果参数是一个现有的对象
					// 那创建的结果就是简单返回该对象
					var a = [];
					alert(a === new Object(a)); // true
					alert(a === Object(a)); // true
				②.关于调用内置构造函数，使用还是不适用new操作符没有通用规则，取决于构造函数。 
				例如Array或Function当使用new操作符的构造函数或者不使用new操作符的简单函数使用产生相同的结果的：
					var a = Array(1, 2, 3); // [object Array]
					var b = new Array(1, 2, 3); // [object Array]
					var c = [1, 2, 3]; // [object Array]					 
					var d = Function(''); // [object Function]
					var e = new Function(''); // [object Function]
		6.2.3.构造函数:是一个函数,用来创建并初始化新创建的对象,对象创建(内存分配)是由构造函数的内部方法[[Construct]]负责的
			而初始化是通过新建对象上下上调用该函数来管理的，这是由构造函数的内部方法[[Call]]来负责任的
			JavaScript里有3中原始包装函数：Number, String, boolean
			// 使用原始包装函数
			var s = new String("my string");
			var n = new Number(101);
			var b = new Boolean(true);
			// 推荐这种
			var s = "my string";
			var n = 101;
			var b = true;
			// 这两者的区别:
			// 原始string
			var greet = "Hello there";
			// 使用split()方法分割
			greet.split(' ')[0]; // "Hello"
			// 给原始类型添加新属性不会报错
			greet.smile = true;
			// 但没法获取这个值
			console.log(typeof greet.smile); // "undefined"
			// 原始string
			var greet = new String("Hello there");
			// 使用split()方法分割
			greet.split(' ')[0]; // "Hello"
			// 给包装函数类型添加新属性不会报错
			greet.smile = true;
			// 可以正常访问新属性
			console.log(typeof greet.smile); // "boolean"
## 7.Javascript数据类型:
	7.1.隐式转换:
		"32" - 32 = 0
		String + number---String
		String + bool--String/true or false;
		number +  bool -- number
		bool + bool --number
		// 隐式转换优先级:
			+  ==> string > number > boolean
			-  ==> number > string > boolean
			+'133'(string) ==> 133(number);// 在数字字符串前加上 + 号,表示将该字符串转为数字
			尝试将Object转换成number或string，取决于另外一个对比量的类型
			var undefined;
			undefined == null; 	// true
			1 == true;  		// true
			2 == true; 			// false
			0 == false; 		// true
			0 == '';			// true
			NaN == NaN;			// false
			[] == false; 		// true
			[] == ![]; 			// true
			undefined与null相等，但不恒等（===）
			一个是number一个是string时，会尝试将string转换为number
			尝试将boolean转换为number，0或1
			尝试将Object转换成number或string，取决于另外一个对比量的类型
			对于0、空字符串的判断，建议使用 “===” 。“===”会先判断两边的值类型，类型不匹配时为false
	7.2.typeof:一般只返回 number,boolean,string,function,object,undefined,可以使用 typeof 判断一个变量是否存在;
		E.G.if(typeof a!="undefined");
		对于Array,Null等特 殊对象使用 typeof一律返回object
		** 适合基本类型及function检测，遇到null失效
		typeof undefined ==> "undefined";
		typeof [1,2] 	 ==> "object"
		typeof NaN 		 ==> "number"
		typeof null		 ==> "object" // null是一个只有一个值的数据类型，这个值就是null。表示一个空指针对象
	7.3.instanceof:用于判断一个变量是否某个对象的实例(原型链),实际就是判断左边的对象的原型链上是否有右边函数的prototype属性
		var s = 'Coco';
		// 对于字符串、数字或者布尔值，其都有对应的方法，这些方法来自于对应的原始封装类型：
		// String、Number和Boolean。原始封装类型将被自动创建
		s instanceof String ==> false // 右边必须是函数
		//注意:
		(1).function 的 arguments 实际上不是一个Array实例
		(2).不同 window 或 iframe 间的对象类型检测是不能使用 instanceof
		** 适合自定义对象，也可以用来检测原生对象，在不同iframe和window间检测时失效
		(3).该操作符是和原型链一起工作的，而不是构造函数
	7.4.Object.prototype.toString.apply()
		Object.prototype.toString.apply([]); === “[object Array]”;
		Object.prototype.toString.apply(function(){}); === “[object Function]”;
		Object.prototype.toString.apply(null); === “[object Null]”
		Object.prototype.toString.apply(undefined); === “[object Undefined]”
		// IE6/7/8 Object.prototype.toString.apply(null) 返回”[object Object]”
		** 通过{}.toString拿到，适合内置对象和基元类型，遇到null和undefined失效(IE678等返回[object Object])。
		alert(1..toString()); // "1"
## 8.严格模式:"use strict":
    严格模式是一种特殊的执行模式,它修复了部分语言上的不足,提供更强的错误检查，并增强安全性
    * http://www.imooc.com/article/2214
	8.1.严格模式向上兼容,可以使用在任何位置:
		function foo(){
			"use strict"
		}
		或者:
		"use strict"
		function foo(){

		}
	8.2.严格模式下的特点:
		(1).不允许用with
		(2).所有变量必须声明, 赋值给为声明的变量报错，而不是隐式创建全局变量。
		(3).eval中的代码不能创建eval所在作用域下的变量、函数。而是为eval单独创建一个作用域，并在eval返回时丢弃。
		(4).函数中得特殊对象arguments是静态副本，而不像非严格模式那样，修改arguments或修改参数变量会相互影响。
			不允许对arguments赋值,
		(5).删除configurable=false的属性时报错，而不是忽略
		(6).禁止八进制字面量，如010 (八进制的8)
		(7).eval, arguments变为关键字，不可作为变量名、函数名等
		(8).一般函数调用时(不是对象的方法调用，也不使用apply/call/bind等修改this)this指向null，而不是全局对象。
			在使用构造函数时,忘记加 new,this不再指向全局对象,而是报错(TypeError)
		(9).若使用apply/call，当传入 null 或 undefined 时，this将指向 null 或 undefined ，而不是全局对象。
		(10).试图修改不可写属性(writable=false)，在不可扩展的对象上添加属性时报TypeError，而不是忽略。
		(11).arguments.caller, arguments.callee被禁用;
		(12).严格模式只允许在全局作用域或函数作用域的顶层声明函数。也就是说，不允许在非函数的代码块内声明函数
## 9.对象:Object 对象本身是一个函数对象
	Object.prototype == Object{}
	9.1.对象中包含一系列属性,这些属性是无序的.每个属性都有一个字符串key和对应的value
	9.2.对象结构:
		(1).var obj = {};
		obj.y = 1;
		obj.x = 2;
		对于obj来说,其属性x,y有如下操作配置:writable,enumerable,configurable,value,get/set等;
		其中obj中还有proto(原型),class(属于哪个种类),extensible(是否可添加其他属性)
		(2).function foo(){}
			foo.prototype.z = 3;
			var obj  = new foo();
			obj.hasOwnProperty(z); // false, z是foo原型上的数据
			obj.z = 3;
		(3).关于原始值:
			如果对原始值进行属性访问器取值，访问之前会先对原始值进行对象包装（包括原始值），
			然后通过包装的对象进行访问属性，属性访问以后，包装对象就会被删除
			var a = 10;
			// 但是可以访问方法（就像对象一样）
			alert(a.toString()); // "10"			 
			// 此外，我们可以在a上创建一个心属性
			a.test = 100; // 好像是没问题的			 
			// 但，[[Get]]方法没有返回该属性的值，返回的却是undefined
			alert(a.test); // undefined
			==>解析过程:
				①.正如我们所说，使用属性访问器以后，它已经不是原始值了，而是一个包装过的中间对象（整个例子是使用new Number(a)），
				而toString方法这时候是通过原型链查找到的
					// 执行a.toString()的原理:
					wrapper = new Number(a);
					wrapper.toString(); // "10"
					delete wrapper;
				②.[[Put]]方法创建新属性时候，也是通过包装装的对象进行的
					// 执行a.test = 100的原理：
					wrapper = new Number(a);
					wrapper.test = 100;
					delete wrapper; // 包装已经被删除了，随着新创建的属性页被删除了——删除包装对象本身
				③.然后使用[[Get]]获取test值的时候,再一次创建了包装对象,但这时候包装的对象已经没有test属性了,所以返回的是 undefined
					wrapper = new Number(a);
					wrapper.test; // undefined
			1.toString(); // 语法错误！		 
			(1).toString(); // OK		 
			1..toString(); // OK		 
			1['toString'](); // OK

	9.3.创建对象:【原型链问题查看:Javascript原型与原型链.js】
		(1).字面量创建对象:(对象初始化)
			var obj = {x : 1, y : 2}
		(2).new 创建对象:(构造函数)
			function Foo(){}
			Foo.prototype.z = 3;
			var obj = new Foo();
			obj.y = 2;
			obj.x = 1;
			obj.x; // 1
			obj.y; // 2
			obj.z; // 3
			typeof obj.toString; // 'function', Object.prototype
			'z' in obj; // true, 会按原型链查找
			obj.hasOwnProperty('z'); // false, 对象上的属性
		(3).Object.create(args):创建一个空对象,该对象的原型指向 args
			var obj = Object.create({x : 1}); // {x : 1} 字面量也存在原型
			obj.x // 1
			typeof obj.toString // "function"
			obj.hasOwnProperty('x');// false
			// 上述的原型链是: obj ==> {x:1} ==> Object.prototype ==> null
			var obj = Object.create(null);
			obj.toString // undefined
			// obj ==> null
		(4).var obj = new Object(); // 代码复用度很低，我们想到一个对象就创建一个对象,会造成大量的重复代码
			可以使用工厂模式:创建相似对象的问题，如果抛开它构造对象的对象识别问题，工厂模式挺完美的
			// 用工厂模式创建对象
			function createObj(id,name)	{
			    var o = new Object();
			    o.id = id;
			    o.name = name;			   
			    o.sayHello = function()   {
			        console.log('id:' + this.id + '@!@name:' + this.name);
			    }
			    return o;
			}
			var obj = createObj('002','My Name is obj2');
			obj.sayHello();//id:002@!@name:My Name is obj2@!@teststring:Test Obj2
			obj['sayHello']();//id:002@!@name:My Name is obj2@!@teststring:Test Obj2
			var str = 'sayHello';
			obj[str]();//id:002@!@name:My Name is obj2@!@teststring:Test Obj2
		(5).构造函数与原型对象:
			①.构造函数也是函数，用 new 创建对象时调用的函数,与普通函数的一个区别是:构造函数首字母应该大写
			②.如果将构造函数当作普通函数调用(缺少 new 关键字)，则应该注意 this 指向的问题:
				var name = "Pomy";
				function Per(){
				    console.log("Hello "+this.name);
				}
				var per1 = new Per();  //"Hello undefined", 使用 new 时,会自动创建this对象,其类型为构造函数类型,指向对象实例；
				var per2 = Per();   //"Hello Pomy", 缺少new关键字，this指向全局对象
			③.每个对象在创建时都自动拥有一个 constructor 属性，指向其构造函数(字面量形式或Object构造函数创建的对象,
				指向Object,自定义构造函数创建的对象则指向它的构造函数)
				console.info(per1.constructor === Per) // true
			④.每个对象实例都有一个内部属性：[[Prototype]]，其指向该对象的原型对象。构造函数本身也具有 prototype 
				属性指向原型对象。所有创建的对象都共享该原型对象的属性和方法:
			⑤.实例中的指针仅指向原型,而不是指向构造函数:
				per1.__proto__ === Per.prototype  ====>  true
			⑥.因为原型对象的 constructor 属性是指向构造函数本身，所以在重写原型时，需要注意constructor属性的指向问题:
				===> 使用对象字面量形式改写原型对象改变了构造函数的属性，因此 constructor 指向 Object，而不是 Hello
					function Hello(name){
					    this.name = name;
					}
					//重写原型
					Hello.prototype = {
					    sayHi:function(){
					        console.log(this.name);
					    }
					};
					var hi = new Hello("Pomy");
					console.log(hi instanceof Hello);  //true
					console.log(hi.constructor === Hello); //false
					console.log(hi.constructor === Object); //true
				===>如果constructor指向很重要，则需要在改写原型对象时手动重置其constructor属性
					Hello.prototype = {
					    constructor:Hello,
					    sayHi:function(){
					        console.log(this.name);
					    }
					};
					console.log(hi.constructor === Hello); //true
					console.log(hi.constructor === Object); //false
			(5.1).构造函数返回值问题:
				在传统语言中，构造函数不应该有返回值，实际执行的返回值就是此构造函数的实例化对象
				①.没有返回值则按照其他语言一样返回实例化对象
				②.若有返回值则检查其返回值是否为引用类型。如果是非引用类型，如基本类型(string,number,boolean,null,undefined)
					则与无返回值相同，实际返回其实例化对象
				③.若返回值是引用类型，则实际返回值为这个引用类型
					function F(){return {a:1}}
					new F() ==> Object{a:1}
		(6).从对象创建到Javascript的性能优化:
			===>如何创建对象是提高程序性能的关键所在 ??????
			在函数内部使用全局变量可以说是一种跨作用域操作，如果某个跨作用域的值在函数的内部被使用到一次以上，
			那么我们就把它存储到局部变量里,如:
			function ftn(){
				var doc = document;
				......
			}
	9.4.属性操作:
		(1).读取对象属性:注意属性异常
			var obj = {x : 1};
			obj.x; // 1
			obj.y; // undefined
			var yz = obj.y.z; // TypeError: Cannot read property 'z' of undefined
			obj.y.z = 2; // TypeError: Cannot set property 'z' of undefined
		(2).属性删除:
			var person = {age : 28, title : 'fe'};
			delete person.age; // true
			delete person['title']; // true
			person.age; // undefined
			delete person.age; // true 删除已经不存在的属性仍返回true
			delete Object.prototype; // false,
			var descriptor = Object.getOwnPropertyDescriptor(Object, 'prototype');
			descriptor.configurable; // false
			// 获取属性的描述信息:
			var descriptor = Object.getOwnPropertyDescriptor(对象,属性名); //返回属性的配置信息
			descriptor.value, descriptor.configurable, descriptor.writable, descriptor.enumerable
			①.var 声明的局部变量和全局变量是不能够被删除的,声明的函数也无法被删除;
				隐式创建的变量是可以删除的, eval() 中定义的变量是可以被删除的
				// 全局变量和局部变量无法删除
				var globalVal = 1;
				delete globalVal; // false
				(function() {
				    var localVal = 1;
				    return delete localVal;
				}()); // false
				// 函数也无法被删除
				function fd() {}
				delete fd; // false
				(function() {
				    function fd() {};
				    return delete fd;
				}()); // false
				// 隐式创建的变量
				ohNo = 1;
				window.ohNo; // 1
				delete ohNo; // true
		(3).属性检测: in(会向原型链上查找) , hasOwnProperty(属性是否在对象上,不从原型链上查找),
			propertyIsEnumerable(是否可枚举,即:enumerable 是否为 true,也不从从原型链上查找)
			定义属性的特征:
			①.defineProperty 默认属性都是false
				Object.defineProperty(cat, 'price', {enumerable : false, value : 1000});
				Object.getOwnPropertyDescriptor(cat, 'price')
				==> Object {value: 1000, writable: false, enumerable: false, configurable: false}
			②.字面创建的属性默认所有属性都是 true 的,enumerable, configurable, writable
				cat.legs = 4
				Object.getOwnPropertyDescriptor(cat, 'legs')
				==> Object {value: 4, writable: true, enumerable: true, configurable: true}
			属性检测:
				var cat = {};
				if (cat && cat.legs) {
				    cat.legs *= 2;
				}
				if (cat.legs !== undefined) {
				    // only if cat.legs is not undefined
				}
				if (cat.legs != undefined) {
				    // !== undefined, or, !== null
				}
		(4).属性枚举:for...in...,需要注意的是该枚举会将原型链上的属性全部枚举,需要对枚举的属性进行判断
			跟 enumerable 标签有关: enumerable = false, 则 for...in... 不会显示
			// 大部分原生属性的[[Enumerable]]的值均为false,即该属性不能枚举.可以通过propertyIsEnumerable()检测属性是否可以枚举
		(5).属性的 get/set方法:
			var man = {
			    weibo : '@Bosn',
			    $age : null,
			    get age() {
			        if (this.$age == undefined) {
			            return new Date().getFullYear() - 1988;
			        } else {
			            return this.$age;
			        }
			    },
			    set age(val) {
			        val = +val;
			        if (!isNaN(val) && val > 0 && val < 150) {
			            this.$age = +val;
			        } else {
			            throw new Error('Incorrect val = ' + val);
			        }
			    }
			}
			==> get/set与原型链:
				①.案例1:
					function foo() {}
					Object.defineProperty(foo.prototype, 'z', {get : function(){return 1;}});
					var obj = new foo();
					obj.z; // 1, 在原型链上向上查找 到 foo.prototype中存在 z 属性,返回该值
					obj.z = 10; // 如果当前 obj中不存在 z 属性时,在原型链上找到 z 属性有get/set方法时,尝试给 obj 对象上的 z
					// 赋值时是直接走原型链上的 get/set 方法,而不会给当前 obj 对象添加新属性处理;					
					obj.z; // still 1
					// 如果需要给 obj 对象添加新属性时, 使用 defineProperty方法,如下所示:
					Object.defineProperty(obj, 'z',  {value : 100, configurable: true});
					obj.z; // 100;
					delete obj.z;
					obj.z; // back to 1
				②.案例2:
					var o = {};
					Object.defineProperty(o, 'x', {value : 1}); // writable=false, configurable=false
					var obj = Object.create(o); // 以 o 为原型创建对象
					obj.x; // 1
					obj.x = 200;// 当前对象不存在 x 属性,而原型链上存在 x 属性, 如果原型链上的属性不可写,
					// 那仍然不不能添加属性到当前对象
					obj.x; // still 1, can't change it
					// 如果需要给当前对象添加新属性 x, 仍然使用 defineProperty 方法
					Object.defineProperty(obj, 'x', {writable:true, configurable:true, value : 100});
					obj.x; // 100
					obj.x = 500;
					obj.x; // 500
		(6).属性标签:
			①.给对象添加属性并添加属性标签: 
				Object.defineProperty(对象,对象的属性名,{属性标签对象}) ===> 添加一个属性
				Object.defineProperties(对象,{多个属性})
			②.获取对象的属性标签:
				Object.getOwnPropertyDescriptor(对象, 对象的属性名)
			③.枚举:for...in... 或者 Object.keys(对象)
			④.
			==>configurable=true,writable=true
				修改属性的值,通过属性赋值/修改属性的值,delete 该属性返回 true,修改get/set方法,修改属性标签
			==>configurable=true,writable=false
				修改属性的值(使用 defineProperty 重设value值),delete 该属性返回 true,修改get/set方法,修改属性标签
			==>configurable=false,writable=true
				修改属性的值,通过属性赋值/修改属性的值,修改writable属性 true 为 false
			==>configurable=false,writable=false
				都不能操操作
	9.5.对象标签:
		(1).__proto__(原型标签):指向构造器的 prototype 属性;
		(2).class:表示对象的类型
			var toString = Object.prototype.toString;
			function getType(o){return toString.call(o).slice(8,-1);};
			toString.call(null); // "[object Null]"
			getType(null); // "Null"
			getType(undefined); // "Undefined"
			getType(1); // "Number"
			getType(new Number(1)); // "Number"
			typeof new Number(1); // "object"
			getType(true); // "Boolean"
			getType(new Boolean(true)); // "Boolean"
		(3).extensible:对象是否可以继续添加属性
			var obj = {x : 1, y : 2};
			Object.isExtensible(obj); // true, 判断当前对象 obj 是否可扩展
			Object.preventExtensions(obj); // 设置当前对象为不可扩展,即不能添加新属性
			Object.isExtensible(obj); // false
			obj.z = 1;
			obj.z; // undefined, add new property failed
			Object.getOwnPropertyDescriptor(obj, 'x');
			// Object {value: 1, writable: true, enumerable: true, configurable: true}
			Object.seal(obj); // 设置当前对象的所有属性的 configurable 为 false, 其extensible值为false
			Object.getOwnPropertyDescriptor(obj, 'x');
			// Object {value: 1, writable: true, enumerable: true, configurable: false}
			Object.isSealed(obj); // true, 是否设置所有属性的 configurable 为 false

			Object.freeze(obj); // 冻结当前对象,即当前对象不可写:writable=false
			Object.getOwnPropertyDescriptor(obj, 'x');
			// Object {value: 1, writable: false, enumerable: true, configurable: false}
			Object.isFrozen(obj); // true
			// 注意:
			上述操作只是针对当前对象,不影响到原型链
	9.6.对象序列化:
		(1).序列化:JSON.stringify(对象)
			var obj = {x : 1, y : true, z : [1, 2, 3], nullVal : null};
			JSON.stringify(obj); // "{"x":1,"y":true,"z":[1,2,3],"nullVal":null}"

			obj = {val : undefined, a : NaN, b : Infinity, c : new Date()};
			JSON.stringify(obj); // "{"a":null,"b":null,"c":"2015-01-20T14:15:43.910Z"}"
			// 注意:
			如果一个对象的属性值为 undefined, 那么使用上述方法序列化时,会将其从对象中移除,不进行序列化;
			如果一个对象的属性值为 NaN, Infinity 会将其序列化为 null, 日期类型会序列化对应的日期时间
		(2).反序列化:JSON.parse(json字符串)
			obj = JSON.parse('{"x" : 1}');
			obj.x; // 1
		(3).自定义序列化:
			var obj = {
			    x : 1,
			    y : 2,
			    o : {
			        o1 : 1,
			        o2 : 2,
			        toJSON : function () {
			            return this.o1 + this.o2;
			        }
			    }
			};
			JSON.stringify(obj); // "{"x":1,"y":2,"o":3}"
	9.7.其他对象方法:
		(1).toString()
			var obj = {x : 1, y : 2};
			obj.toString(); // "[object Object]"
			obj.toString = function() {return this.x + this.y};
			"Result " + obj; // "Result 3", by toString
			+obj; // 3, from toString
		(2).valueOf()
			obj.valueOf = function() {return this.x + this.y + 100;};
			+obj; // 103, from valueOf
			"Result " + obj; // still "Result 103"
		(3).关于这两者的使用:
			遇到 "Result " + obj; 类似运算, js会先判断是否重写 valueOf()方法,
			==> 如果其返回的是基本类型非对象,则直接 调用 valueOf()方法
			==> 如果返回的是对象,则继续查找是否重写 toString()方法,
			==> 如果其其返回的是基本类型非对象,则直接 调用 toString()方法,
			==> 如果 toString()也返回对象,则报错
	9.8.实例方法与类方法:
		function Person(name, age){
			this.name = name;
			this.age = age;
		}
		Person.prototype.walk = function(){
			// ...在原型对象上定义实例方法或实例属性
		}
		Person.run = function(){
			// 定义类的第三步是定义类方法，常量和其他必要的类属性，作为构造函数自身的属性，而不是构造函数  
　 			// 原型对象的属性，注意，类方法没有使用关键字 this
		}
## 10.数组:
    值的有序集合.每个值叫做元素,每个元素在数组中都有数字位置编号,也就是索引.
	JS中的数组是弱类型的，数组中可以含有不同类型的元素。数组元素甚至可以是对象或其它数组;
	// 数组索引从 0 开始
	var arr = [] 或者 var arr = new Array() 或者 arr = new Array(100) 或 new Array(true, false, null, 1, 2, "hi");
	arr = [1,2,3,4]
	10.1.动态的,无需指定大小:
		arr.length -= 1 // [1,2,3], 4 is removed
		delete arr[2] // arr => [1,2,undefined], delete 数组元素,数组的长度不会改变,被删除的位置上的元素变为 undefined
	10.2.稀疏数组:稀疏数组并不含有从0开始的连续索引。一般length属性值比实际元素个数大
		所以如果一个数组是稀疏数组的话,迭代是需要使用 in 去判断,
		var arr1 = [undefined];
		var arr2 = new Array(1); // 只是创建了一个包含一个元素的数组
		0 in arr1; // true
		0 in arr2; // false ,0 这个 key 目前还不存在
		arr1.length = 100;
		arr1[99] = 123;
		99 in arr1; // true
		98 in arr1; // false
		var arr = [,,]; // 原因同上
		0 in arr; // false
	10.3.数组的方法-1:基本上所有浏览器都支持
		(1).Array.prototype.join():将数组转化为字符串
			var arr = [1, 2, 3];
			arr.join(); // "1,2,3"
			arr.join("_"); // "1_2_3"
			function repeatString(str, n) { // 将某个字符串重复 n 次
			     return new Array(n + 1).join(str);
			}
			repeatString("a", 3); // "aaa"
			repeatString("Hi", 5); // "HiHiHiHiHi
		(2).Array.prototype.reverse():数组逆序,原数组也被改变
			var arr = [1, 2, 3];
			arr.reverse(); // [3, 2, 1]
			arr; // [3, 2, 1]
		(3).Array.prototype.sort():默认按照ASCII编码排序,原数组也被修改, sort()方法可以接收一个函数作为参数
			var arr = ["a", "d", "c", "b"];
			arr.sort(); // ["a", "b", "c", "d"]
			arr = [13, 24, 51, 3];
			arr.sort(); // [13, 24, 3, 51]
			arr; // [13, 24, 3, 51]
			arr.sort(function(a,b){
				return a - b; //升序, return b - a;//降序
			});
		(4).Array.prototype.concat():数组合并,原数组不会被改变
			var arr = [1, 2, 3];
			arr.concat(4, 5); // [1, 2, 3, 4, 5]
			arr; // [1, 2, 3]
			arr.concat([10, 11], 13); // [1, 2, 3, 10, 11, 13] ,数组被扁平化处理
			arr.concat([1, [2, 3]]); // [1, 2, 3, 1, [2, 3]] ,如果 concat 参数里包含数组,数组内再包含数组,则外层数组会被扁平化处理
		(5).Array.prototype.slice():返回部分数组,原数组不会被改变: 左开右闭,即包含左边索引的值,不包含右边索引的值
			var arr = [1, 2, 3, 4, 5];
			arr.slice(1, 3); // [2, 3]
			arr.slice(1); // [2, 3, 4, 5]
			arr.slice(1, -1); // [2, 3, 4], 数组最后一个元素的索引可以表示为 -1
			arr.slice(-4, -3); // [2]
		(6).Array.prototype.splice():用于插入、删除、替换数组的元素,原数组被修改
			splice(index, howmany, element1,..., elements2):
			==> index:必须参数,开始插入或替换或删除元素的起始位置;
			==> howmany:必须参数,规定应该删除多少元素; 也可以是0; 如未写,则删除从index位置开始到原数组结尾的所有元素;
			==> element1,..., elements2,规定要添加到数组中的元素
			var arr = [1, 2, 3, 4, 5];
			arr.splice(2); // returns [3, 4, 5]
			arr; // [1, 2];

			arr = [1, 2, 3, 4, 5];
			arr.splice(2, 2); // returns [3, 4]
			arr; // [1, 2, 5];

			arr = [1, 2, 3, 4, 5];
			arr.splice(1, 1, 'a', 'b'); // returns [2]
			arr; // [1, "a", "b", 3, 4, 5]
	10.4.数组的方法-2:ECMAScript5 规范新的方法,IE8及IE8以下不支持如下方法:
		(1).Array.prototype.forEach():迭代数组
			var arr = [1, 2, 3, 4, 5];
			arr.forEach(function(x, index, a){// x:当前元素, index:当前元素的索引, a:被迭代的数组
			    console.log(x + '|' + index + '|' + (a === arr));
			});
		(2).Array.prototype.map():数组映射,接收一个函数,作用在数组的每个元素上,原数组不会被改变
			var arr = [1, 2, 3];
			arr.map(function(x) {
			     return x + 10;
			}); // [11, 12, 13]
			arr; // [1, 2, 3]
		(3).Array.prototype.filter():数组过滤,原数组不会被改变
			var arr = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
			arr.filter(function(x, index) {// x:当前元素, index:当前元素的索引
			     return index % 3 === 0 || x >= 8;
			}); // returns [1, 4, 7, 8, 9, 10]
			arr; // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
		(4).Array.prototype.every() & some():判断数组元素
			==> every():数组中所有元素必须满足条件,才返回 true, 只要有一个不满足,就返回 false,并跳出every()
			==> some():数组中只要有一个元素满足条件即返回 true;
				var arr = [1, 2, 3, 4, 5];
				arr.every(function(x) {
				     return x < 10;
				}); // true
				arr.every(function(x) {
				     return x < 3;
				}); // false
				arr.some(function(x) {
				     return x === 3;
				}); // true
				arr.some(function(x) {
				     return x === 100;
				}); // false
		(5).Array.prototype.reduce() & reduceRight():将数组元素聚合成一个唯一的结果,如数组元素相加相乘等;
			原数组不会被改变
			==> reduce():从左往右计算;
			==> reduce():从右往左计算
				var arr = [1, 2, 3];
				var sum = arr.reduce(function(x, y) {
				     return x + y
				}, 0); // 6
				arr; //[1, 2, 3]

				arr = [3, 9, 6];
				var max = arr.reduce(function(x, y) {
				     console.log(x + "|" + y);
				     return x > y ? x : y;
				});
				// 3|9
				// 9|6
				max; // 9
				max = arr.reduceRight(function(x, y) {
				     console.log(x + "|" + y);
				     return x > y ? x : y;
				});
				// 6|9
				// 9|3
				max; // 9
		(6).Array.prototype.indexOf() & lastIndexOf():数组检索, element:被查找的元素, formIndex:可选参数,从哪个位置开始查找
			==> indexOf(element, [fromIndex]):从左往右查找,
			==> lastIndexOf(element, [fromIndex]): 从右往左查找
				var arr = [1, 2, 3, 2, 1];
				arr.indexOf(2); // 1
				arr.indexOf(99); // -1
				arr.indexOf(1, 1); // 4
				arr.indexOf(1, -3); // 4
				arr.indexOf(2, -1); // -1
				arr.lastIndexOf(2); // 3
				arr.lastIndexOf(2, -2); // 3
				arr.lastIndexOf(2, -3); // 1
		(7).Array.isArray():接收数组为参数,数组不能直接调用
			[] instanceof Array; // true
			({}).toString.apply([]) === '[object Array]'; // true
			[].constructor === Array; // true,构造器可以被更改
	10.5.判断变量是否为 "数组":数组使用 typeof 为 "object"
		(1).obj instanceof Array 在某些IE版本中不正确
		(2).在ECMA Script5中定义了新方法Array.isArray(), 保证其兼容性，最好的方法如下
			if(typeof Array.isArray==="undefined")
			{
			  Array.isArray = function(arg){
			        return Object.prototype.toString.call(arg)==="[object Array]"
			    };  
			}
		(3).使用 constructor 属性:
			if(typeof Array.isArray === "undefined"){
				Array.isArray = function(arr){
					return arr.constructor.toString().indexOf("Array") > -1
				}
			}
	10.6.ArrayBuffer:表示二进制数据的原始缓冲区
		10.6.1.该缓冲区用于存储各种类型化数组的数据。 无法直接读取或写入 ArrayBuffer，但可根据需要将其传递
			到类型化数组或 DataView 对象 来解释原始缓冲区;
		10.6.2.ArrayBuffer 的创建:
			var buffer = new ArrayBuffer(30);

## 11.函数(function):
    是一块JavaScript代码,被定义一次,但可执行和调用多次.JS中的函数也是对象,所以JS函数可以像其它对象那样操作和传递
	所以我们也常叫JS中的函数为函数对象
	// 不论哪里直接调用函数，里面的this都是指向全局的
	11.1.函数声明与表达式:
		11.1.1.函数声明:
			function foo(){
				// do something
			}
		11.1.2.函数表达式:
			var add = function(){...}
			(function(){})()// 立即调用函数表达式
			return function(){...}
			var add = function foo(){...} //命名函数表达式
		11.1.3.函数声明与函数表达式对比:
			(1).函数声明会提前,因此在函数声明前可以直接调用该函数;
			(2).在函数表达式前调用会提示函数未定义,如:var add = function(){...},在该表达式之前调用 add(),提示 undefined is not a function
				var 声明变量提前, add 变量初始值为 undefined;
		11.1.3.命名函数表达式:[参看:深入理解Javascript系列---学习笔记.js]
	11.2.Function 构造器:很少使用
		var func = new Function('a', 'b', 'console.log(a + b);'); // 第三个参数为函数体
		func(1, 2); // 3
		var func = Function('a', 'b', 'console.log(a + b);');
		func(1, 2); // 3
		(1).构造器里创建的变量仍然为局部变量
			Function('var localVal = "local"; console.log(localVal);')();
			console.log(typeof localVal); // undefined
		(2).构造器可以使用全局变量,
			var globalVal = 'global';
			(function() {
				var localVal = 'local';
				Function('console.log(typeof localVal, typeof globalVal);')(); // local不可访问，全局变量global可以访问
			})();
		(3).在把函数当作构造器使用的时候，如果手动返回了一个值，要看这个值是否简单类型，如果是，等同于不写返回，
			如果不是简单类型，得到的就是手动返回的值。如果，不手动写返回值，就会默认从原型创建一个对象用于返回;
			x = {};
	        function bar() {
	            this.x = 2;
	            return x;
	        }
	        var foo = new bar();
	        alert(foo.x); // undefined
	11.3.this:只要是函数调用 this 指向的方法和函数都是指向 window 的，即全局对象;
		它用在对象的方法中，this 总是指向调用该方法的对象
		(1).全局的this: 等同于 window
			console.log(this.document === document); // true
			console.log(this === window); // true
			this.a = 37;
			console.log(window.a); // 37
		(2).一般的函数的this:
			function f1(){
				return this;
			}
			f1() === window; // true, global object
			function f2(){
				"use strict"; // see strict mode
				return this; // this 指向 undefined
			}
			f2() === undefined; // true
		(3).作为对象方法的函数的this:一般指向对象
			var o = {
				prop: 37,
				f: function() {
					return this.prop;
				}
			};
			console.log(o.f()); // logs 37
		(4).对象原型链上的this: 原型链上的对象的 this 仍然可以取到当前对象
			var o = {f:function(){ return this.a + this.b; }};
			var p = Object.create(o);
			p.a = 1;
			p.b = 4;
			console.log(p.f()); // 5
		(5).get/set方法与this:与对象上的this类似
		(6).构造器中的this:如果 function 中没有返回或者返回的是基本类型,则返回的就是 this 
			如果返回的是一个对象, 则this就指向该返回的对象
			function MyClass(){
				this.a = 37;
			}
			var o = new MyClass(); // new 对象后,this指向原型为 MyClass.prototype 的空对象, 默认的this作为返回值
			console.log(o.a); // 37
			function C2(){
				this.a = 37;
				return {a : 38};
			}
			o = new C2();//
			console.log(o.a); // 38
		(7).call/apply 中的 this: apply()的参数为空时，默认调用全局对象
			function add(c, d){
				return this.a + this.b + c + d;
			}
			var o = {a:1, b:3};
			add.call(o, 5, 7); // 1 + 3 + 5 + 7 = 16
			add.apply(o, [10, 20]); // 1 + 3 + 10 + 20 = 34
			function bar() {
				console.log(Object.prototype.toString.call(this));
			}
			bar.call(7); // "[object Number]"
	11.4.函数属性:
		11.4.1.arguments
			function foo(x, y, z) {
				arguments.length; // 2
				arguments[0]; // 1
				arguments[0] = 10;
				x; // change to 10;
				arguments[2] = 100;
				z; // still undefined !!!
				arguments.callee === foo; // true
			}
			foo(1, 2);
			foo.length; // 3
			foo.name; // "foo"
			(1).arguments 中数据与传入的实参进行对应且会进行数据绑定,但是对于为传入的实参,不存在绑定关系,
				如上 arguments[2] = 100; 但 z 的值并未改变
			(2).foo.length 表示的形参的个数, arguments.length 表示的是实参的个数;
			(3).在严格模式事,无法通过 arguments[0] = 10;修改 x 的值, 且 callee,caller等属性不能使用
		11.4.2.apply()/call():改变函数的作用域, 将this指针指向方法的第一个参数。
			如果在javascript语言里没有通过new（包括对象字面量定义）、call和apply改变函数的this指针，函数的this指针都是指向window的
			function foo(x, y) {
				console.log(x, y, this);
			}
			foo.call(100, 1, 2); // 1, 2, Number(100)
			foo.apply(true, [3, 4]); // 3, 4, Boolean(true)
			foo.apply(null); // undefined, undefined, window
			foo.apply(undefined); // undefined, undefined, window
			(1).call():第一个参数为对象,后面为与函数一一对应的参数;
			(2).apply():第一个参数为对象,后面为一个数组,数组的元素与函数的参数一一对应;
			(3).在严格模式下:
				foo.call(100, 1, 2); ==> 1, 2, 100
				foo.apply(true, [3, 4]); ==> 3, 4, true
				foo.apply(null); ==> undefined, undefined, null
				foo.apply(undefined); ==> undefined, undefined, undefined
		11.4.3.bind():(ECMAScript5 提供的方法),改变函数this的指向
			this.x = 9;
			var module = {
				x: 81,
				getX: function() { return this.x; }
			};
			module.getX(); // 81
			var getX = module.getX;
			getX(); // 9, 这里 this 指向的是 window
			var boundGetX = getX.bind(module);
			boundGetX(); // 81, 通过使用bind方法,可以改变this的指向
			(1).bind() 与 currying: 可以通过 bind 将某个函数生成一个带默认值的参数
				function add(a, b, c) {
					return a + b + c;
				}
				var func = add.bind(undefined, 100); // 这里将 100 绑定到参数 a 中,
				func(1, 2); // 103
				var func2 = func.bind(undefined, 200);// 因为参数 a之前绑定了100, 这里的200将绑定到参数 b中
				func2(10); // 310

				function getConfig(colors, size, otherOptions) {
					console.log(colors, size, otherOptions);
				}
				var defaultConfig = getConfig.bind(null, "#CC0000", "1024 * 768");
				defaultConfig("123"); // #CC0000 1024 * 768 123
				defaultConfig("456"); // #CC0000 1024 * 768 456
			(2).bind 与 new:

	11.5.闭包:指有权访问另外一个函数作用域中的变量的函数
		!function() {
			// do sth here
			var a, b;
		}();
	11.6.作用域:作用域其实是Function对象实例的一个内部属性【Scope】,作用域决定了那些变量能被函数所访问
		作用域也确定了this指针的指向
		11.6.1.全局,函数,eval
			(1).全局作用域
			(2).函数作用域
			(3).eval
		11.6.2.作用域链:保证执行环境里有权访问的变量和函数是有序的
			函数被创建时候【Scope】这个内部属性包含了这个函数的作用域内所有对象的集合，这个集合叫做作用域链
			作用域链决定了函数能访问到那些数据
			作用域链的变量只能向上访问，变量访问到window对象即被终止，作用域链向下访问变量是不被允许的
			var b1 = "b1";
		    function ftn1(){
		        var b2 = "b2";
		        var b1 = "bbb";
		        function ftn2(){
		            var b3 = "b3";
		            b2 = b1;
		            b1 = b3;
		            console.log("b1:" + b1 + ";b2:" + b2 + ";b3:" + b3);// 运行结果：b1:b3;b2:bbb;b3:b3
		        }
		        ftn2();
		    }
		    ftn1();
			console.log(b1);// 运行结果：b1

			Object.prototype.x = 10;			 
			var w = 20;
			var y = 30;				
			// 例如，全局上下文的变量对象是从"Object.prototype"继承到的
			// 所以我们可以得到“没有声明的全局变量”
			// 因为可以从原型链中获取			 
			console.log(x); // 10			 
			(function foo() {			 
			  // "foo" 是局部变量
			  var w = 40;
			  var x = 100;			 
			  // "x" 可以从"Object.prototype"得到，注意值是10哦
			  // 因为{z: 50}是从它那里继承的			 
			  with ({z: 50}) {
			    console.log(w, x, y , z); // 40, 10, 30, 50
			  }			 
			  // 在"with"对象从作用域链删除之后
			  // x又可以从foo的上下文中得到了，注意这次值又回到了100哦
			  // "w" 也是局部变量
			  console.log(x, w); // 100, 40			 
			  // 在浏览器里
			  // 我们可以通过如下语句来得到全局的w值
			  console.log(window.w); // 20			 
			})();
		11.6.3.作用域的范围:javascript并不是以代码段为作用域,而是以函数,再根据命名提升的原则
			看下列代码:
				foo();
				var a = true;
				if(a){
				    function foo(){
				        console.log('a');
				    }
				} else {
				    function foo(){
				        console.log('b');
				    }
				}
			上述代码可以改为:
				function foo(){
				    console.log('a');
				}
				function foo(){
				    console.log('b');
				}
				foo();
				var a = true;
				if(a){
				} else {
				}
	11.7.ES3执行上下文(Execution Contexts):
		11.7.1.执行上下文:ECMA-262标准里的一个抽象概念,用于同可执行代码(executable code)概念进行区分
			(1).活动的执行上下文组在逻辑上组成一个堆栈。堆栈底部永远都是全局上下文(global context),
			而顶部就是当前(活动的)执行上下文.堆栈在EC类型进入和退出上下文的时候被修改(推入或弹出);
			(2).在某些时刻，可执行代码与执行上下文完全有可能是等价的
			(3).相关代码执行完以后，ECStack只会包含全局上下文(global context)，一直到整个应用程序结束
			(4).在版本号1.7以上的SpiderMonkey(内置于Firefox,Thunderbird)的实现中，可以把调用上下文作为第二个参数传递给eval.
			那么，如果这个上下文存在，就有可能影响“私有”(有人喜欢这样叫它)变量
		11.7.2.可执行代码(函数作用域):
			全局作用域,函数作用域,eval作用域(调用上下文(calling context))
		11.7.3.变量对象(Variable Object):是一个与执行上下文相关的特殊对象,用于存储执行上下文的变量、函数声明、函数参数等
			填充VO的顺序是: 函数的形参 -> 函数申明 -> 变量申明
			activeExecutionContext = {
			  VO: {
			    // 上下文数据（var, FD, function arguments)
			  }
			};
			==> 看个例子:
					var a = 10;				 
					function test(x) {
					  var b = 20;
					};				 
					test(30);
				对应的变量对象是:
					// 全局上下文的变量对象
					VO(globalContext) = {
					  a: 10,
					  test: <reference to function>
					};					 
					// test函数上下文的变量对象
					VO(test functionContext) = {
					  x: 30,
					  b: 20
					};
			(1).不同执行上下文中的变量对象:
				A.全局上下文中的变量对象:
					①.全局对象(Global object) 是在进入任何执行上下文之前就已经创建了的对象；
					②.这个对象只存在一份，它的属性在程序中任何地方都可以访问，全局对象的生命周期终止于程序退出那一刻;
					==>结论:
					回到全局上下文中的变量对象——在这里，变量对象就是全局对象自己
					VO(globalContext) === global;
				B.函数上下文中的变量对象:
					①.在函数执行上下文中,VO是不能直接访问的,此时由活动对象(activation object,缩写为AO)扮演VO的角色
						==>VO(functionContext) === AO;
					②.活动对象在进入函数上下文时刻被创建的,它通过函数的arguments属性初始化.arguments属性的值是Arguments对象:
						callee — 指向当前函数的引用
			(2).VO按照如下顺序填充:
				①.函数参数(若未传入,初始化该参数值为 undefined);
				②.函数声明(若发生命名冲突,会覆盖);
				③.变量声明(初始化值为 undefined,若发生命名冲突,会忽略);
		11.7.4.处理上下文代码的2个阶段:执行上下文的代码被分成两个基本的阶段来处理:
			==>这2个阶段的处理是一般行为,和上下文的类型无关,在全局上下文和函数上下文中的表现是一样的
			(1).进入执行上下文:当进入执行上下文(代码执行之前)时，VO里已经包含了下列属性
				①. 函数的所有形参(如果我们是在函数执行上下文中);
					由名称和对应值组成的一个变量对象的属性被创建；
					没有传递对应参数的话，那么由名称和undefined值组成的一种变量对象的属性也将被创建
				②.所有函数声明(FunctionDeclaration, FD):
					由名称和对应值（函数对象(function-object)）组成一个变量对象的属性被创建；
					如果变量对象已经存在相同名称的属性，则完全替换这个属性
				③.所有变量声明(var, VariableDeclaration):
					由名称和对应值（undefined）组成一个变量对象的属性被创建；
					如果变量名称跟已经声明的形式参数或函数相同，则变量声明不会干扰已经存在的这类属性
					函数表达式不会影响VO
			(2).执行代码:AO/VO已经拥有了属性(不过，并不是所有的属性都有值，大部分属性的值还是系统默认的初始值 undefined):

	11.8.执行环境与作用域链:
		11.8.1.执行环境:全局环境与局部环境:
			(1).全局环境:整个页面里被共享的方法和属性就是在全局环境，相对于全局环境;
				全局执行环境被认为是window对象，因此全局变量和函数都是作为window对象的方法和属性来创建的;
			(2).局部环境:函数{}号里的执行环境就是局部环境
			执行环境定义了变量或函数有权访问的其他数据,决定了它们各自的行为,每个执行环境都定义了一个与之相关的变量对象,
			环境中定义的所有变量和函数都保存在这个对象里
		11.8.2.运行期上下文:
			函数执行时,函数会创建一个运行期上下文(Execution Context),每个运行期上下文都有自己的作用域链,用于标识符解析
				function foo() {
				  var x = 1;
				  return function () { alert(x); };
				};				 
				var bar = foo();				 
				bar(); // 1				 
				eval('x = 2', bar); // 传入上下文，影响了内部的var x 变量				 
				bar( // 2
		11.8.3.作用域链:内部上下文所有变量对象（包括父变量对象）的列表
			==> 参考文章:http://www.cnblogs.com/TomXu/archive/2012/01/18/2312463.html
			作用域链与一个执行上下文相关，变量对象的链用于在标识符解析中变量查找
			(1).函数上下文的作用域链在函数调用时创建的，包含活动对象和这个函数内部的[[scope]]属性
				activeExecutionContext = {
				    VO: {...}, // or AO
				    this: thisValue,
				    Scope: [ // Scope chain
				      // 所有变量对象的列表
				      // for identifiers lookup
				      // Scope = AO + [[Scope]]
				    ]
				};
			(2).函数的生命周期:分为创建和激活阶段（调用时）
				var x = 10;				 
				function foo() {
				  var y = 20;
				  alert(x + y);
				}				 
				foo(); // 30
				(2.1).函数创建:
					变量“y”在函数“foo”中定义(意味着它在foo上下文的AO中),但是变量“x”并未在“foo”上下文中定义,
					相应地,它也不会添加到“foo”的AO中;
					fooContext.AO = {
					  y: undefined // undefined – 进入上下文的时候是20 – at activation
					};
					函数“foo”如何访问到变量“x”是通过函数内部的[[scope]]属性来实现的
					[[scope]]是所有父变量对象的层级链，处于当前函数上下文之上，在函数创建时存于其中
				==> 注意:
					①.[[scope]]在函数创建时被存储－－静态（不变的），永远永远，直至函数销毁。即：函数可以永不调用,
					但[[scope]]属性已经写入，并存储在函数对象中.
					②.与作用域链对比，[[scope]]是函数的一个属性而不是上下文
					foo.[[Scope]] = [
					  globalContext.VO // === Global
					];
				(2.2).函数激活:
					var x = 10;					 
					function foo() {
					  var y = 20;					 
					  function bar() {
					    var z = 30;
					    alert(x +  y + z);
					  }					 
					  bar();
					}					 
					foo(); // 60
					函数的的[[scope]]属性以及上下文的作用域链：
					①.全局上下文的变量对象是：
						globalContext.VO === Global = {
						  x: 10
						  foo: <reference to function>
						};
					②.在“foo”创建时，“foo”的[[scope]]属性是：
						foo.[[Scope]] = [
						  globalContext.VO
						];
					③.“foo”激活时（进入上下文），“foo”上下文的活动对象是：
						fooContext.AO = {
						  y: 20,
						  bar: <reference to function>
						};
					④.“foo”上下文的作用域链为：
						fooContext.Scope = fooContext.AO + foo.[[Scope]] // i.e.:						 
						fooContext.Scope = [
						  fooContext.AO,
						  globalContext.VO
						];
					⑤.内部函数“bar”创建时，其[[scope]]为：
						bar.[[Scope]] = [
						  fooContext.AO,
						  globalContext.VO
						];
					⑥.在“bar”激活时，“bar”上下文的活动对象为：
						barContext.AO = {
						  z: 30
						};
					⑦.“bar”上下文的作用域链为：
						barContext.Scope = barContext.AO + bar.[[Scope]] // i.e.:						 
						barContext.Scope = [
						  barContext.AO,
						  fooContext.AO,
						  globalContext.VO
						];
					⑧.对“x”、“y”、“z”的标识符解析如下：
						- "x"
						-- barContext.AO // not found
						-- fooContext.AO // not found
						-- globalContext.VO // found - 10
						- "y"
						-- barContext.AO // not found
						-- fooContext.AO // found - 20
						- "z"
						-- barContext.AO // found - 30
			(3).作用域特征:通过构造函数创建的函数的[[scope]]:
					var x = 10;					 
					function foo() {					 
					  var y = 20;					 
					  function barFD() { // 函数声明
					    alert(x);   alert(y);
					  }					 
					  var barFE = function () { // 函数表达式
					    alert(x);    alert(y);
					  };					 
					  var barFn = Function('alert(x); alert(y);');					 
					  barFD(); // 10, 20
					  barFE(); // 10, 20
					  barFn(); // 10, "y" is not defined					 
					}					 
					foo();
					==>通过函数构造函数（Function constructor）创建的函数“bar”，是不能访问变量“y”的
						问题在于通过函构造函数创建的函数的[[scope]]属性总是唯一的全局对象
			(4).在ECMAScript 中，在代码执行阶段有两个声明能修改作用域链。这就是with声明和catch语句:
				var x = 10, y = 10;				 
				with ({x: 20}) {				 
				  var x = 30, y = 30;				 
				  alert(x); // 30
				  alert(y); // 30
				}				 
				alert(x); // 10
				alert(y); // 30
				==>思路:
					①.x = 10, y = 10;
					②.对象{x:20}添加到作用域的前端;
					③.在with内部，遇到了var声明，当然什么也没创建，因为在进入上下文时，所有变量已被解析添加;
					④.在第二步中，仅修改变量“x”，实际上对象中的“x”现在被解析，并添加到作用域链的最前端，“x”为20，变为30;
					⑤.同样也有变量对象“y”的修改，被解析后其值也相应的由10变为30;
					⑥.此外，在with声明完成后，它的特定对象从作用域链中移除（已改变的变量“x”－－30也从那个对象中移除），
						即作用域链的结构恢复到with得到加强以前的状态。
					⑦.在最后两个alert中，当前变量对象的“x”保持同一，“y”的值现在等于30，在with声明运行中已发生改变
	11.9.ECMAScript向函数function传递参数的策略,即求值策略:
		11.9.1.一般理论:
			(1).按值传递:
				参数的值是调用者传递的对象值的拷贝(copy of value)，函数内部改变参数的值不会影响到外面的对象(该参数在外面的值)，
				一般来说，是重新分配了新内存,该新内存块的值是外部对象的拷贝，并且它的值是用到函数内部的;
			(2).按引用传递:对象的隐式引用，如该对象在外部的直接引用地址
				函数内部对参数的任何改变都是影响该对象在函数外部的值，因为两者引用的是同一个对象
			(3).按共享传递(Call by sharing)[“按对象传递”或“按对象共享传递”]
				==> 要点:函数接收的是对象对于的拷贝(副本)该引用拷贝和形参以及其值相关联
		11.9.2.ECMAScript 实现:
			==>ECMAScript中将对象作为参数传递的策略了——按共享传递:修改参数的属性将会影响到外部,而重新赋值将不会影响到外部对象;
			
## 12.OOP:面向对象编程是一种程序设计范型,同时也是一种程序开发的方法	
	12.1.基于原型的继承:
		function Foo(){}
		typeof Foo.prototype; // "object"
		Foo.prototype.x = 1
		12.1.1.prototype 属性与原型:
			(1).prototype: 对象上的预设的对象属性
			(2).原型:对象上的原型,通常都是其构造器的 prototype 属性
			Foo.prototype 包含:
			{
				constructor : Foo,
				__proto__: Object.prototype,//chrome上暴露了该属性
				x : 1
			}
		12.1.2.基于原型的继承例子:
			function Person(name, age){
				this.name = name;
				this.age = age;
			}
			Person.prototype.hi = function(){
				console.info('Hi, My name is ' + this.name + ", I'm " + this.age);
			}
			Person.prototype.LEGS_NUM = 2;
			Person.prototype.ARMS_NUM = 2;
			Person.prototype.walk = function(){
				console.info(this.name + " is walking");
			}
			function Student(name, age, className){
				Person.call(this, name, age);
				this.className = className;
			}
			// 如果直接将 Person.prototype 赋值给 Student.prototype,那么如果往 Student的 prototype 上增加属性或方法时
			// 会相应的影响到 Person.prototype
			Student.prototype = Object.create(Person.prototype);
			Student.prototype.constructor = Student;
			Student.prototype.hi = function(){
				console.info('Hi, My name is ' + this.name + ", I'm " + this.age + " and from " + this.className);
			}
			Student.prototype.learn = function(subject){
				console.log(this.name  + " is learning " + subject + ' at '+  this.className);
			}
			var coco = new Student('Coco', 27, 'Class 2 Grade 2');
			coco.hi();
	12.2.原型链:[看图:【再谈原型链.PNG】] Object.getPrototypeOf()
		分析上诉代码:
		coco.__proto__ === Student.prototype // true 即 coco 实例的 __proto__属性指向的是 构造器的 prototype 属性
		(1).并不是所有的对象原型链上都有 Object.prototype:
			var obj = Object.create(null);
			obj.__proto__ = undefined;
		(2).并不是所有的函数对象都有 prototype 属性:
			bind 返回的函数没有 prototype 属性：
			function foo(){}
			foo.prototype = foo {}
			var binded = foo.bind(null)
			binded.prototype = undefined
			typeof binded = 'function'
		(3).改变对象的 prototype 属性的指向,并不能改变已经创建的实例对象的__proto__属性的指向,会影响到后续创建的实例对象
			如果给 prototype 添加/删除一些属性,会影响到已经创建的实例对象:
			Student.prototype.x = 1;
			coco.x; // 1
			Student.prototype = {y : 2};
			coco.y; // undefined
			coco.x; // 1
			var obj = new Student('Holiday',3,'Class LOL  Keng');
			obj.x;// undefined
			obj.y; // 2
		(4).内置构造器的 prototype:尽量不要修改
			Object.prototype.x = 10
			Object.defineProperty(Object.prototype, 'x', {writable : true, value : 10})
	12.3.实现继承的方式:
		function Person(){}
		function Student(){}
		(1).Studen.prototype = Person.prototype   [不推荐]
			当改写 Student 的同时也会同时改变 Person
		(2).Student.prototype = new Person()
			存在的问题:
			如果Person 的构造函数是含有参数的,则看起来很奇怪
			function extend(subClass, superClass){
				var F = function(){};
				F.prototype = superClass.prototype;
				subClass.prototype = new F();
				subClass.prototype.constructor = subClass;
			}
		(3).Student.prototype = Object.create(Person.prototype); ==> 比较好的继承实现方式
			但是 Object.create() 方法是在 ECMAScript5 之后才支持的,可以模拟实现:
				if (!Object.create) {
					Object.create = function(proto){
						function F(){}
						F.prototype = proto;
						return new F
					}
				}
	12.4.模拟重载:
		使用对 arguments 对象判断来实现
	12.5.调用基类的方法:
		function Person(name){
			this.name = name;
		}
		function Student(name, className){
			this.className = className;
			Person.call(this, name);
		}
		var coco = new Student('Coco', 'SkyLine 411');
		Person.prototype.init = function(){}
		Student.prototype.init = function(){
			// do something
			Person.prototype.init.apply(this, arguments)
		}
	12.6.链式调用:方法的返回值一般都是当前对象
		function ClassManager(){}
		ClassManager.prototype.addClass = function(str){
			console.log("Class:" + str + " added");
			return this;
		}
		var manager = new ClassManager()
		manager.addClass("Class A").addClass("Class B").addClass("Class C");
	12.7.模拟抽象类:
		function ClassName(){
			throw new Error('Abstract class can not be init...');
		}
	12.8.综合实例:
		!function(global){
			function DetectorBase(configs){
				if( !this instanceof DetectorBase){
					throw new Error("Do not invoke whithout new.");
				}
				this.configs = configs;
				this.analyze();
			}
			DetectorBase.prototype.detect = function(){
				throw new Error("Not implemented.");
			};
			DetectorBase.prototype.analyze = function(){
				console.log('analyzeing....');
				this.data = "###data###";
			};
			function LinkedDetector(links){
				if(!this instanceof LinkedDetector){
					throw new Error("Do not invoke whithout new");
				}
				this.links = links;
				DetectorBase.apply(this, arguments);
			}
			function ContainerDetector(containers){
				if(!this instanceof ContainerDetector){
					throw new Error("Do not invoke whithout new");
				}
				this.containers = containers;
				DetectorBase.apply(this, arguments);
			}
			inherit(LinkedDetector, DetectorBase);
			inherit(ContainerDetector, DetectorBase);
			LinkedDetector.prototype.detect = function(){
				console.log("Lodaing data:"+  this.data);
				console.log("linked detcetion started");
				console.log("scanning the links:" +  this.links);
			};
			ContainerDetector.prototype.detect = function(){
				console.log("Lodaing data:"+  this.data);
				console.log("containner detcetion started");
				console.log("scanning the containner:" +  this.containers);
			};
			Object.freeze(DetectorBase);
			Object.freeze(DetectorBase.prototype);
			Object.freeze(LinkedDetector);
			Object.freeze(LinkedDetector.prototype);
			Object.freeze(ContainerDetector);
			Object.freeze(ContainerDetector.prototype);

			Object.defineProperties(global, {
				LinkedDetector : {value : LinkedDetector},
				ContainerDetector : {value : ContainerDetector},
				DetectorBase : {value : DetectorBase}
			});
			function inherit(subClass, superClass){
				subClass.prototype = Object.create(superClass.prototype);
				subClass.prototype.constructor = subClass;
			}
		}(this);

		var cd = new ContainerDetector('#abc #def #ghi');
		var ld = new LinkedDetector('http://www.taobao.com http://www.tmall.com http://www.baidu.com');

		cd.detect();
		ld.detect();
## 13.正则表达式:
	13.1.正则表达式的方法: RegExp
		(1).直接量:/ pattern / attributes
		(2).创建对象: new RegExp(pattern, attributes);
			参数pattern是一个字符串，指定了正则表达式的模式；
			参数attributes是一个可选的参数，包含属性 g，i，m，分别使用与全局匹配，不区分大小写匹配，多行匹配
	13.2.支持正则表达式的 String 对象的方法:
		13.2.1.search():该方法用于检索字符串中指定的子字符串，或检索与正 则表达式相匹配的字符串
			(1).基本语法: string.search(regexp);
				参数regexp可以需要在string中检索的字符串，也可以 是需要检索的RegExp对象
				return(返回值) stringObject中第一个与regexp对象相匹配的子串的起始位置.如果没有找到任何匹配的子串，则返回-1
				==>注意:
					search()方法不执行全局匹配，它将忽略标志"g"，同时它也没有regexp对象的lastIndex的属性，
					且总是从字符串开始位置进行查找，总是返回的是stringObject匹配的第一个位置
			(2).测试Demo:
				var str = "hello world,hello world";
				// 返回匹配到的第一个位置(使用的regexp对象检索)
				console.log(str.search(/hello/)); // 0
				// 没有全局的概念 总是返回匹配到的第一个位置
				console.log(str.search(/hello/g)); //0				 
				console.log(str.search(/world/)); // 6				 
				// 也可以是检索字符串中的字符
				console.log(str.search("wo")); // 6				 
				// 如果没有检索到的话，则返回-1
				console.log(str.search(/longen/)); // -1				 
				// 我们检索的时候 可以忽略大小写来检索
				var str2 = "Hello";
				console.log(str2.search(/hello/i)); // 0
		13.2.3.match():该方法用于在字符串内检索指定的值，或找到一个或者多个正则表达式的匹配。
			该方法类似于 indexOf()或者 lastIndexOf(); 但是它返回的是指定的值，而不是字符串的位置；
			(1).基本:
				stringObject.match(searchValue) 或者 stringObject.match(regexp)
				==> 参数:
					searchValue 需要检索字符串的值；
					regexp: 需要匹配模式的RegExp对象；
				==> 返回值:
					存放匹配成功的数组; 它可以全局匹配模式，全局匹配的话，它返回的是一个数组。
					如果没有找到任何的一个匹配，那么它将返回的是null；
					返回的数组内有三个元素，第一个元素的存放的是匹配的文本，还有二个对象属性；
					index属性表明的是匹配文本的起始字符在stringObject中的位置；input属性声明的是对stringObject对象的引用；
			(2).测试Demo:
				var str = "hello world";
				console.log(str.match("hello")); // ["hello", index: 0, input: "hello world"]
				console.log(str.match("Hello")); // null
				console.log(str.match(/hello/)); // ["hello", index: 0, input: "hello world"]
				// 全局匹配
				var str2="1 plus 2 equal 3"
				console.log(str2.match(/d+/g)); //["1", "2", "3"]
		13.2.4.replace()方法：该方法用于在字符串中使用一些字符替换另一些字符，或者替换一个与正则表达式匹配的子字符串；
			(1).基本语法:stringObject.replace(regexp/substr,replacement);
				==> 参数:
					regexp/substr; 字符串或者需要替换模式的RegExp对象。
					replacement：一个字符串的值，被替换的文本或者生成替换文本的函数
				==> 返回:返回替换后的新字符串
				==> 注意:
					字符串的stringObject的 replace()方法执行的是查找和替换操作，替换的模式有 2 种:
					既可以是字符串，也可以是正则匹配模式，如果是正则匹配模式的话，那么它可以加修饰符"g",代表全局替换，
					否则的话，它只替换第一个匹配的字符串；		
			(2).replacement既可以是字符串,也可以是函数,如果它是字符串的话,那么匹配的将与字符串替换,
				replacement中的$有具体的含义,如下：
				①.$1,$2,$3,…,$99 含义是：与regexp中的第1到第99个子表达式相匹配的文本;
				②.$& 的含义是：与RegExp相匹配的子字符串;
				③.lastMatch 或 RegExp["$_"] 的含义是：返回任何正则表达式搜索过程中的最后匹配的字符;
				④.lastParen 或 RegExp["$+"] 的含义是：返回任何正则表达式查找过程中最后括号的子匹配;
				⑤.leftContext 或 RegExp["$`"]的含义是：返回被查找的字符串从字符串开始的位置到最后匹配之前的位置之间的字符
				⑥.rightContext 或 RegExp["$'"]的含义是：返回被搜索的字符串中从最后一个匹配位置开始到字符串结尾之间的字符
			(3).测试Demo:
				var str = "hello world";
				// 替换字符串
				var s1 = str.replace("hello","a");
				console.log(s1);// a world
				// 使用正则替换字符串
				var s2 = str.replace(/hello/,"b");
				console.log(s2); // b world
				// 使用正则全局替换 字符串
				var s3 = str.replace(/l/g,'');
				console.log(s3); // heo word
				// $1,$2 代表的是第一个和第二个子表达式相匹配的文本
				// 子表达式需要使用小括号括起来,代表的含义是分组
				var name = "longen,yunxi";
				var s4 = name.replace(/(\w+),(\w+)/,"$2 $1");
				console.log(s4); // "yunxi,longen"

				// $& 是与RegExp相匹配的子字符串
				var name = "hello I am a chinese people";
				var regexp = /am/g;
				if(regexp.test(name)) {
				    //返回正则表达式匹配项的字符串
				    console.log(RegExp['$&']);  // am
				    //返回被搜索的字符串中从最后一个匹配位置开始到字符串结尾之间的字符。
				    console.log(RegExp["$'"]); // a chinese people
				    //返回被查找的字符串从字符串开始的位置到最后匹配之前的位置之间的字符。
				    console.log(RegExp['$`']);  // hello I 
				    // 返回任何正则表达式查找过程中最后括号的子匹配。
				    console.log(RegExp['$+']); // 空字符串
				    //返回任何正则表达式搜索过程中的最后匹配的字符。
				    console.log(RegExp['$_']);  // hello I am a chinese people
				}

				// replace 第二个参数也可以是一个function 函数
				var name2 = "123sdasadsr44565dffghg987gff33234";
				name2.replace(/\d+/g,function(v){
				    console.log(v); 
				    /*
				     * 第一次打印123
				     * 第二次打印44565
				     * 第三次打印987
				     * 第四次打印 33234
				     */
				});
				/*
				 * 如下函数，回调函数参数一共有四个
				 * 第一个参数的含义是 匹配的字符串
				 * 第二个参数的含义是 正则表达式分组内容，没有分组的话，就没有该参数,
				 * 如果没有该参数的话那么第四个参数就是undefined
				 * 第三个参数的含义是 匹配项在字符串中的索引index
				 * 第四个参数的含义是 原字符串
				 */
				 name2.replace(/(\d+)/g,function(a,b,c,d){
				    console.log(a);
				    console.log(b);
				    console.log(c);
				    console.log(d);
				    /*
				     * 如上会执行四次，值分别如下(正则使用小括号，代表分组)：
				     * 第一次： 123,123,0,123sdasadsr44565dffghg987gff33234
				     * 第二次： 44565,44565,11,123sdasadsr44565dffghg987gff33234
				     * 第三次： 987,987,22,123sdasadsr44565dffghg987gff33234
				     * 第四次： 33234,33234,28,123sdasadsr44565dffghg987gff33234
				     */
				 });
		13.2.5.split():该方法把一个字符串分割成字符串数组
			(1).基本语法:stringObject.split(separator,howmany);
				==> 参数: 
					separator[必填项]，字符串或正则表达式，该参数指定的地方分割stringObject; 
					howmany[可选] 该参数指定返回的数组的最大长度，如果设置了该参数，返回的子字符串不会多于这个参数指定的数组。
					如果没有设置该参数的话，整个字符串都会被分割，不考虑他的长度
				==> @return(返回值) 一个字符串数组。该数组通过在separator指定的边界处将字符串stringObject分割成子字符串
			(2).测试Demo:
				var str = "what are you doing?";
				// 以" "分割字符串
				console.log(str.split(" "));
				// 打印 ["what", "are", "you", "doing?"]				 
				// 以 "" 分割字符串
				console.log(str.split(""));				
				// 打印：["w", "h", "a", "t", " ", "a", "r", "e", " ", "y", "o", "u", " ", "d", "o", "i", "n", "g", "?"]				
				// 指定返回数组的最大长度为3
				console.log(str.split("",3));
				// 打印 ["w", "h", "a"]
	13.3.RegExp 对象方法:
		13.3.1.test()方法:该方法用于检测一个字符串是否匹配某个模式；
			(1).基本语法:RegExpObject.test(str);
				@param(参数) str是需要检测的字符串；
				@return (返回值) 如果字符串str中含有与RegExpObject匹配的文本的话，返回true，否则返回false；
			(2).测试代码:
				var str = "longen and yunxi";
				console.log(/longen/.test(str)); // true
				console.log(/longlong/.test(str)); //false				 
				// 或者创建RegExp对象模式
				var regexp = new RegExp("longen");
				console.log(regexp.test(str)); // true
		13.3.2.exec()方法: 该方法用于检索字符串中的正则表达式的匹配
			(1).基本语法:
				@param(参数)：string【必填项】要检索的字符串。
				@return(返回值)：返回一个数组，存放匹配的结果，如果未找到匹配，则返回值为null；
				该返回的数组的第一个元素是与正则表达式相匹配的文本
			(2).测试代码:
				var str = "longen and yunxi";
				console.log(/longen/.exec(str)); 
				// 打印 ["longen"]				 
				// 假如没有找到的话，则返回null
				console.log(/wo/.exec(str)); // null
	13.4.正则中的普通字符:
		字母，数字，汉字，下划线及一些没有特殊定义的标点符号
	13.5.正则中的方括号[]的含义:
		方括号包含一系列字符，能够匹配其中任意一个字符
		[abc]:  查找在方括号中的任意一个字符；
		[^abc]: 查找不在方括号中的任意一个字符；
		[0-9]: 查找0-9中的任意一个数字；
		[a-z]: 查找从小写a到z中的任意一个字符；
		(red|blue|green); 查找小括号中的任意一项，小括号中的 | 是或者的意思；
	13.6.Javascript的元字符:
		.	查找任意的单个字符，除换行符外
		\w	任意一个字母或数字或下划线，A_Za_Z0_9,_中任意一个
		\W	查找非单词的字符，等价于[^A_Za_z0_9_]
		\d	匹配一个数字字符，等价于[0-9]
		\D	匹配一个非数字字符，等价于[^0-9]
		\s	匹配任何空白字符，包括空格，制表符，换行符等等。等价于[\f\n\r\t\v]
		\S	匹配任何非空白字符，等价于[^\f\n\r\t\v]
		\b	匹配一个单词边界，也就是指单词和空格间的位置，比如’erb’可以匹配”never”中的”er”,但是不能匹配”verb”中的”er”
			\b 匹配一个单词边界，也就是单词与空格之间的位置，不匹配任何字符；
			var str="my name is longen";		 
			// 匹配单词边界的字符		 
			console.log(str.match(/\bname\b/g)); //["name"]		 
			// 如果不是单词边界的地方，就匹配失败		 
			console.log(str.match(/\blong\b/g)); // null

		\B	匹配非单词边界,’erB’能匹配’verb’中的’er’,但不能匹配’never’中的’er’
		\0	匹配 NULL (U+0000) 字符， 不要在这后面跟其它小数，因为 \0<digits> 是一个八进制转义序列
		\n	匹配一个换行符
		\f	匹配一个换页符
		\r	匹配一个回车符
		\t	匹配一个制表符
		\v	匹配一个垂直制表符
		\xxx	查找一个以八进制数xxx规定的字符
		\xdd	查找以16进制数dd规定的字符
		\uxxxx	查找以16进制数的xxxx规定的Unicode字符。
	13.7.特殊字符:
		$ 匹配输入字符串的结尾位置，如果需要匹配$本身的话，使用$
		^ 匹配输入字符串的开始位置，匹配^本身的话，使用^
		* 匹配前面的子表达式的零次或者多次，匹配*本身的话，使用*
		+ 匹配子表达式的1次或者多次，匹配+本身的话，使用+
		. 匹配除换行符之外的任何一个字符，匹配.本身的话，使用.
		[ 匹配一个中括号开始，匹配本身的，使用[
		? 匹配前面的子表达式的零次或者1次，或指明一个非贪婪限定符，要匹配本身的话，使用? 匹配本身的话，请使用\
		{ 标记限定符开始的地方，要匹配{ ,请使用{
		| 指明多项中的一个选择，可以理解含义为或的意思，匹配本身的话，使用|
	13.8.量词:
		n+		匹配任何至少包含一个n的字符串
		n*		匹配零个或者多个n的字符串
		n?		匹配零个或者1个n的字符串
		n{x}	匹配包含x个n的序列字符串
		n{x,y}	匹配至少x个，最多y个n的字符串
		n{x,}	匹配至少x个的字符串
		n$		匹配以n结尾的字符串
		^n		匹配以n开头的字符串
		?=n		匹配其后紧接指定的n字符串
		?!n		匹配其后没有紧接指定的n字符串
	13.9.贪婪模式与非贪婪模式:
		(1).Javascript中的正则贪婪与非贪婪模式的区别是：
			被量词修饰的子表达式的匹配行为；贪婪模式在整个表达式匹配成功的情况下尽可能多的匹配；
			非贪婪模式在整个表达式匹配成功的前提下，尽可能少的匹配；
		(2).一些常见的修饰贪婪模式的量词如下：
			{x,y} ,  {x,} ,  ? ,  * , 和  +
		(3).那么非贪婪模式就是在如上贪婪模式后加上一个?(问号)，就可以变成非贪婪模式的量词；如下：
			{x,y}?, {x,}?, ??, *?, +?
## 14.Javscript的事件:
	14.1.事件流模型
		(1).事件冒泡:事件开始由最具体的元素接受,然后逐级向上传播;
		(2).事件捕捉:事件由最不具体的节点先接收，然后逐级向下，一直到最具体的
		(3).DOM事件流:三个阶段,事件捕捉、目标阶段、事件冒泡
	14.2.事件流:描述的是在页面接受事件的顺序

		
## 15.Ajax 与 JSON
	15.1.Ajax:异步JavaScript和XML，用于在Web页面中实现异步数据交互
		15.1.1.优点:
			(1).可以使得页面不重载全部内容的情况下加载局部内容，降低数据传输量;
			(2).避免用户不断刷新或者跳转页面，提高用户体验
		15.1.2.缺点:
			(1).对搜索引擎不友好;
			(2).要实现ajax下的前后退功能成本较大
			(3).可能造成请求数的增加
			(4).跨域问题限制

	15.2.
## 16.异常机制:
	16.1.异常的捕获:
		try{
			// 发生异常的代码块
		} catch (err){
			// 异常处理
		}
	16.2.Throw语句:通过 throw 语句创建一个自定义错误
		function demo(id){
			try{
				if(!id){
					throw "参数不能为null";
				}
			}catch(err){
			}
		}

## 17.Javascript 浏览器对象:
	17.1.window 对象:
		17.1.1.BOM的核心,window 对象指当前的浏览器窗口, HTML DOM 中的 document 也是 window 对象的属性之一
		17.1.2.window 尺寸和方法
			window.innerHeight  浏览器窗口的内部高度
			window.innerWidth   浏览器窗口的内部宽度
			window.open(url)	打开新窗口
			window.close()		关闭当前窗口
	17.2.计时器:
		17.2.1.计时方法:
			(1).setInterval(function, time):间隔指定的毫秒数不停的执行指定的代码
				clearInterval():用于停止 setInterval()方法执行的函数代码
			(2).setTimeout():暂停指定的毫秒数后执行指定的代码
				clearTimeout():用于停止执行 setTimeout()方法的函数代码
	17.3.History:包含浏览器的历史集合
		history.back()--与在浏览器点击后退按钮相同
		history.forward()--与在浏览器点击前进按钮相同
		history.go()--进入历史中的某个页面
	17.4.Location 对象:
		获取当前页面的地址,并把浏览器重定向到新的页面
		location.hostname : 返回web主机的域名
		location.pathname 返回当前页面的路径和文件名
		location.port 返回web主机的端口
		location.protocol  返回当前页面web协议
		location.href 属性返回当前页面的URL
		location.assign() 方法加载新的文档
	17.5.Screen:
		screen.availHeight:当前页面可用高度
		screen.availWidth: 当前页面可用宽度

## 18.Javascript 运算符优先级:
    .[ ] ( )							字段访问、数组索引、函数调用和表达式分组 
    ++ -- - ~ ! delete new typeof void	一元运算符、返回数据类型、对象创建、未定义的值      
    * / %      
    + - +      							相加、相减、字符串串联
    << >> >>>   						移位   
    < <= > >= instanceof 				小于、小于或等于、大于、大于或等于、是否为特定类的实例     
    == != === !==    					相等、不相等、全等，不全等  
    &      
    ^      
    |      
    &&      
    ||     
    ?: 									条件运算     
    = OP=  								赋值、赋值运算（如 += 和 &=）   
    ,									多个计算




















