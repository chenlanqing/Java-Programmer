```
1.
	if (!("a" in window)) {
	    var a = 1;
	}
	alert(a); 

2.
	var a = 1,
	    b = function a(x) {
	        x && a(--x);
	    };
	alert(a);

3.
	function a(x) {
	    return x * 2;
	}
	var a;
	alert(a);

4.
	function b(x, y, a) {
	    arguments[2] = 10;
	    alert(a);
	}
	b(1, 2, 3);
 
5.
	function a() {
	    alert(this);
	}
	a.call(null);

6.找出数字数组中最大的元素（使用Match.max函数）
7.转化一个数字数组为function数组（每个function都弹出相应的数字）
8.给object数组进行排序（排序条件是每个元素对象的属性个数）
9.利用JavaScript打印出Fibonacci数（不使用全局变量）
10.实现如下语法的功能：var a = (5).plus(3).minus(6); //2
11.实现如下语法的功能：var a = add(2)(3)(4); //9
12.
	(function(foo){//这里这个foo里面只有一个foo属性
      return typeof foo.bar; //undefined
    })({ foo: { bar: 1 } });

13.写出谢列代码的输出结果:
	function Foo() {
	    getName = function () { alert (1); };
	    return this;
	}
	Foo.getName = function () { alert (2);};
	Foo.prototype.getName = function () { alert (3);};
	var getName = function () { alert (4);};
	function getName() { alert (5);}

	//请写出以下输出结果：
	Foo.getName();// 2
	getName(); // 4
	Foo().getName(); // 1
	getName(); // 1
	new Foo.getName(); // 2
	new Foo().getName(); // 3
	new new Foo().getName(); // 3
	题涉及的知识点众多,包括变量定义提升、this指针指向、运算符优先级、原型、继承、全局变量污染、对象属性及原型属性优先级等等
	先看此题的上半部分做了什么，首先定义了一个叫 Foo 的函数，之后为Foo创建了一个叫getName的静态属性存储了一个匿名函数，
	之后为Foo的原型对象新创建了一个叫getName的匿名函数。之后又通过函数变量表达式创建了一个getName的函数，
	最后再声明一个叫getName函数
	(1).Foo.getName();
		Foo函数上存储的静态属性，自然是2
	(2).getName();
		直接调用 getName 函数。既然是直接调用那么就是访问当前上文作用域内的叫getName的函数，所以跟1 2 3都没什么关系。
		此题有无数面试者回答为5。此处有两个坑:一是变量声明提升，二是函数表达式
	(3).Foo().getName();
		先执行了Foo函数，然后调用Foo函数的返回值对象的getName属性函数;
		Foo函数的第一句  getName = function () { alert (1); };  是一句函数赋值语句，注意它没有var声明，
		所以先向当前Foo函数作用域内寻找getName变量，没有。再向当前函数作用域上层，即外层作用域内寻找是否
		含有getName变量，找到了，也就是第二问中的alert(4)函数，将此变量的值赋值为 function(){alert(1)}。
		==> 此处实际上是将外层作用域内的getName函数修改了
		this的指向是由所在函数的调用方式决定的。而此处的直接调用方式，this指向window对象。
		遂Foo函数返回的是window对象，相当于执行 window.getName() ，而window中的getName已经被修改为alert(1)，所以最终会输出1
		此处考察了两个知识点，一个是变量作用域问题，一个是this指向问题。
	(4).getName()
		直接调用getName函数，相当于 window.getName(),因为这个变量已经被Foo函数执行时修改了，遂结果与第三问相同，为1
	(5).new Foo().getName();
		第五问 new Foo.getName(); ,此处考察的是js的运算符优先级问题
		. 的优先级高于 new 操作,等价于:new (Foo.getName)();
		所以实际上将getName函数作为了构造函数来执行，遂弹出2
	(6).new Foo().getName();
		算符优先级括号高于new，实际执行为: (new Foo()).getName()
		遂先执行Foo函数，而Foo此时作为构造函数却有返回值
		返回的是this，而this在构造函数中本来就代表当前实例化对象，遂最终Foo函数返回实例化对象
		之后调用实例化对象的getName函数，因为在Foo构造函数中没有为实例化对象添加任何属性，
		遂到当前对象的原型对象 prototype 中寻找getName，找到了
	(7).new new Foo().getName();
		同样是运算符优先级问题,最终执行:new ((new Foo()).getName)();
		先初始化Foo的实例化对象，然后将其原型上的getName函数作为构造函数再次new
```
































































