<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [1.var 和 function 的提前声明:](#1var-%E5%92%8C-function-%E7%9A%84%E6%8F%90%E5%89%8D%E5%A3%B0%E6%98%8E)
- [2.关于全局变量与局部变量:](#2%E5%85%B3%E4%BA%8E%E5%85%A8%E5%B1%80%E5%8F%98%E9%87%8F%E4%B8%8E%E5%B1%80%E9%83%A8%E5%8F%98%E9%87%8F)
- [3.给基本类型数据添加属性,不报错,但取值时是undefined](#3%E7%BB%99%E5%9F%BA%E6%9C%AC%E7%B1%BB%E5%9E%8B%E6%95%B0%E6%8D%AE%E6%B7%BB%E5%8A%A0%E5%B1%9E%E6%80%A7%E4%B8%8D%E6%8A%A5%E9%94%99%E4%BD%86%E5%8F%96%E5%80%BC%E6%97%B6%E6%98%AFundefined)
- [4.判断一个字符串中出现次数最多的字符,并统计次数](#4%E5%88%A4%E6%96%AD%E4%B8%80%E4%B8%AA%E5%AD%97%E7%AC%A6%E4%B8%B2%E4%B8%AD%E5%87%BA%E7%8E%B0%E6%AC%A1%E6%95%B0%E6%9C%80%E5%A4%9A%E7%9A%84%E5%AD%97%E7%AC%A6%E5%B9%B6%E7%BB%9F%E8%AE%A1%E6%AC%A1%E6%95%B0)
- [5.关于 this, apply, call, new, bind, caller, callee](#5%E5%85%B3%E4%BA%8E-this-apply-call-new-bind-caller-callee)
- [6.Javascript异步编程:](#6javascript%E5%BC%82%E6%AD%A5%E7%BC%96%E7%A8%8B)
- [7.闭包:](#7%E9%97%AD%E5%8C%85)
- [8.with 语句](#8with-%E8%AF%AD%E5%8F%A5)
- [9.Javascript 函数与栈:](#9javascript-%E5%87%BD%E6%95%B0%E4%B8%8E%E6%A0%88)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## 1.var 和 function 的提前声明:
	var 和 function 都会提前声明,而且 function 是优于 var 声明的(如果同时存在);
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
	
## 2.关于全局变量与局部变量:
	function 内声明的变量是局部变量;
	没有使用 var 声明的变量不管在何处都是全局变量;
	while{...},if(){...},for(...)之内的都是全局变量(除非本身包含在function内);
	变量的隐式声明;
	
## 3.给基本类型数据添加属性,不报错,但取值时是undefined

## 4.判断一个字符串中出现次数最多的字符,并统计次数
	(1).Hashtable方式:
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
## 5.关于 this, apply, call, new, bind, caller, callee
	http://www.cnblogs.com/sharpxiajun/p/4148932.html
	5.1.this:一个与执行上下文(execution context，也就是作用域)相关的特殊对象,任何对象都可以做为上下文中的this的值
		this值在进入上下文时确定，并且在上下文运行期间永久不变
		(1).functionName(xxx) ==> functionName.call(window,xxxx);
			如果是在ES5的严格模式下 call()的第一个参数不是 window 而是 undefined
			对匿名函数来说:
				(function(name) {
				        //
			    })("aa");
			    //等价于
			    (function(name) {
			        //
			    }).call(window, "aa");
		(2).可以将this理解为如下:
			foo() ==> foo.call(window)
			obj.foo() ==> obj.foo.call(obj)
		(3).this是执行上下文环境的一个属性,而不是某个变量对象的属性
		5.1.1.全局代码中的 this:this始终是全局对象本身
		5.1.2.函数中的 this:
			(1).影响函数中 this 值变化的因素有:取决于调用函数的方式;
			通过调用方式动态确定 this 的经典例子:
			function foo(){
				alert(this.bar);
			}
			var x = {bar : 10};
			var y = {bar : 20};
			x.test = foo;
			y.tset = foo;
			x.test(); // 10
			y.test(); // 20
		5.1.3.函数调用方式如何影响 this 的值:其内部类型之一——引用类型(Reference type)
			在一个函数上下文中，this由调用者提供，由调用函数的方式来决定。如果调用括号()的左边是引用类型的值，
			this将设为引用类型值的base对象（base object），在其他情况下(与引用类型不同的任何其它属性)，这个值为null。
			不过，实际不存在this的值为null的情况，因为当this的值为null的时候，其值会被隐式转换为全局对象。
			当调用括号的左边不是引用类型而是其它类型，这个值自动设置为null，结果为全局对象
			==>注：第5版的ECMAScript中，已经不强迫转换成全局变量了，而是赋值为undefined
			(function(){
				console.info(this); // window,即全局对象
			})()
		5.1.4.with 语句与this
			如果with对象包含一个函数名属性，在with语句的内部块中调用函数。With语句添加到该对象作用域的最前端，
			即在活动对象的前面
		5.1.5.catch 语句:
			在特定的活动对象中，this指向全局对象。而不是catch对象
	5.2.new

	5.3.apply()、call():call和apply都是为了改变某个函数运行时的上下文(context)而存在的,换句话说,
		就是为了改变函数体内部 this 的指向;
		apply(thisArgs, [])/ call(thisArgs [,args...])
			thisArgs:
			①.不传，或者传 null,undefined， 函数中的this指向window对象;
			②.传递另一个函数的函数名，函数中的this指向这个函数的引用
			③.传递字符串、数值或布尔类型等基础类型，函数中的this指向其对应的包装对象，如 String、Number、Boolean;
			④.传递一个对象，函数中的this指向这个对象
		(1).案例:
			function fruit(){}
			fruit.prototype = {
				color : "red",
				say : function(){
					console.log('My color is ' + this.color);
				}
			}
			var apple = new fruit();
			var banana = {color : "yellow"};
			apple.say.call(banana);
			apple.say.apply(banana);
			当一个对象上没有某个方法时,而另外的对象有该方法,可以借助 call 或 apply 来用其他对象的方法来操作
		(2).apply、call 的区别:对于 apply、call 二者而言，作用完全一样，只是接受参数的方式不太一样
			var func = function(arg1, arg2){}
			func.call(this, arg1, arg2);
			func.apply(this, [arg1, arg2]);
			==> this:可以是指定的上下文,也可以是任何的javascript对象;
			==> call 把参数顺序传递进去, apply 把参数放在数组中
			==> 如果明确函数的参数且知道数量,可以使用 call;
			==> 不确定参数时,可以使用apply,当参数数量不确定时,函数内部也可以通过 arguments 这个数组来遍历所有的参数;
		(3).常见使用:
			// 数组元素追加
			var array1 = [12 , "foo" , {name "Joe"} , -2458];  
			var array2 = ["Doe" , 555 , 100];  
			Array.prototype.push.apply(array1, array2); // array1 值为  [12 , "foo" , {name "Joe"} , -2458 , "Doe" , 555 , 100]
			// number 本身没有 max 方法，但是 Math 有，我们就可以借助 call 或者 apply 使用其方法
			var  numbers = [5, 458 , 120 , -215 ];  
			var maxInNumbers = Math.max.apply(Math, numbers),	//458
				maxInNumbers = Math.max.call(Math,5, 458 , 120 , -215);	//458
			// 类（伪）数组使用数组方法:
			var domNodes = Array.prototype.slice.call(document.getElementsByTagName("*"));
			==> Javascript中存在一种名为伪数组的对象结构。比较特别的是 arguments 对象，还有像调用 getElementsByTagName , 
				document.childNodes 之类的，它们返回NodeList对象都属于伪数组。不能应用 Array下的 push , pop 等方法;
		(4).深入理解:
			==> 定义一个 log 方法,让它可以代理 console.log 方法:
			①.常见方法:
				function log(msg){
					console.log(msg);
				}
				上述可以解决基本需求,但是当传入的参数是不确定的时候,上面的方法就失效了,可以考虑使用 apply call:
			②.传入的参数不确定,可以使用 apply :
				function log(){
					console.log.apply(console, arguments);
				}
			③.在基础上,给输出的数据加上前缀 "app":
				function log(){
					//arguments参数是个伪数组，通过 Array.prototype.slice.call 转化为标准数组
					var args = Array.prototype.slice.call(arguments); 
					args.unshift('(app)');

					console.log.apply(console, args);
				}

	5.4.bind():可以改变函数体内的 this 指向:
		bind()方法会创建一个新函数,称为绑定函数;当调用这个绑定函数时,绑定函数会以创建它时传入 bind() 方法的第一个参数作为 this,
		传入 bind() 方法的第二个以及以后的参数加上绑定函数运行时本身的参数按照顺序作为原函数的参数来调用原函数
		(1).案例:
			var foo = {
				bar : 1,
				eventBind: function(){
					var _this = this;
					$('.someClass').on('click',function(event) {
						/* Act on the event */
						console.log(_this.bar);		//1
					});
				}
			}
			==>
			var foo = {
				bar : 1,
				eventBind: function(){
					$('.someClass').on('click',function(event) {
						/* Act on the event */
						console.log(this.bar);		//1
					}.bind(this));
				}
			}
			var bar = function(){
				console.log(this.x);
			}
			var foo = {
				x:3
			}
			bar(); // undefined
			var func = bar.bind(foo);
			func(); // 3
			==> 创建了一个新的函数func,当使用 bind() 创建一个绑定函数之后,它被执行的时候,它的 this 会被设置成 foo ,
				而不是像我们调用 bar() 时的全局作用域
		(2).如果连续 bind() 两次，亦或者是连续 bind() 三次那么输出的值是什么呢?
			var bar = function(){
				console.log(this.x);
			}
			var foo = {
				x:3
			}
			var sed = {
				x:4
			}
			var func = bar.bind(foo).bind(sed);
			func();	//? 
			var fiv = {
				x:5
			}
			var func = bar.bind(foo).bind(sed).bind(fiv);
			func();	//? 
			==> 在Javascript中，多次 bind() 是无效的.
			==> 更深层次的原因: bind() 的实现,相当于使用函数在内部包了一个 call / apply ,
				第二次 bind() 相当于再包住第一次 bind() ,故第二次以后的 bind 是无法生效的;
		(3).bind()函数的第一个参数为 null 代表作用域不变，后面的不定参数将会和函数本身的参数按次序进行绑定,
			绑定之后执行函数只能从未绑定的参数开始传值;
		(4).bind()的polyfill实现
			if (!Function.prototype.bind) {
			    Function.prototype.bind = function (oThis) {
			        var aArgs = Array.prototype.slice.call(arguments, 1),
			            fToBind = this, //this在这里指向的是目标函数
			            fBound = function () {
			                return fToBind.apply(
			                    //如果外部执行var obj = new fBound(),则将obj作为最终的this，放弃使用oThis
			                    this instanceof fToBind
			                            ? this  //此时的this就是new出的obj
			                            : oThis || this, //如果传递的oThis无效，就将fBound的调用者作为this
			                    //将通过bind传递的参数和调用时传递的参数进行合并，并作为最终的参数传递
			                    aArgs.concat(Array.prototype.slice.call(arguments)));
			            };
			        //将目标函数的原型对象拷贝到新函数中，因为目标函数有可能被当作构造函数使用
			        fBound.prototype = this.prototype;
			        //返回fBond的引用，由外部按需调用
			        return fBound;
			    };
			}
	5.5.apply、call、bind比较:
		在JavaScript中，call、apply和bind 是 Function 对象自带的三个方法，这三个方法的主要作用是改变函数中的this指向
		var obj = {
			x: 81,
		};
		var foo = {
			getX: function() {
				return this.x;
			}
		}
		console.log(foo.getX.bind(obj)());	//81, 注意看使用 bind() 方法的，他后面多了对括号
		console.log(foo.getX.call(obj));	//81
		console.log(foo.getX.apply(obj));	//81
		==> 区别是:当你希望改变上下文环境之后并非立即执行,而是回调执行的时候,使用 bind() 方法.而 apply/call 则会立即执行函数
		==> apply 、 call 、bind 三者都是用来改变函数的this对象的指向的；
		==> apply 、 call 、bind 三者第一个参数都是this要指向的对象，也就是想指定的上下文；
		==> apply 、 call 、bind 三者都可以利用后续参数传参；
		==> bind 是返回对应函数，便于稍后调用；apply 、call 则是立即调用;
		==> 第一个参数为 null 时代表作用域不变;
		// 非严格模式下
		根据ECMAScript262规范规定：如果第一个参数传入的对象调用者是 null 或者 undefined 的话，
		call方法将把全局对象（也就是 window）
		作为this的值。所以，不管你什么时候传入 null，其this都是全局对象 window
	5.6.apply、call、bind 应用场景:
		(1).继承:
			function Animal(name,weight){
			   this.name = name;
			   this.weight = weight;
			}		 
			function Cat(){
			    Animal.call(this,'cat','50');
			  //Animal.apply(this,['cat','50']);			 
			   this.say = function(){
			      console.log("I am " + this.name+",my weight is " + this.weight);
			   }
			}			 
			var cat = new Cat();
			cat.say();//I am cat,my weight is 50
## 6.Javascript异步编程:
    异步模式有四种方法 [/* http://www.ruanyifeng.com/blog/2012/12/asynchronous%EF%BC%BFjavascript.html */]
	6.1.回调函数:异步编程最基本的方法,如下:
		==>假定有两个函数f1和f2，后者等待前者的执行结果:
			f1()
			f2()
		==>如果f1是一个很耗时的任务，可以考虑改写f1，把f2写成f1的回调函数。
			function f1(callback){
				setTimeout(function(){
					// f1 要执行的代码
					callback();
				}, 1000);
			}
		==> 优点:简单、容易理解和部署
			缺点:是不利于代码的阅读和维护,各个部分之间高度耦合(Coupling),流程会很混乱,而且每个任务只能指定一个回调函数;
	6.2.事件监听:采用事件驱动模式,任务的执行不取决于代码的顺序,而取决于某个事件是否发生,"事件"完全可以理解成"信号"
		==>以f1和f2为例。首先，为f1绑定一个事件（这里采用的jQuery的写法）
			f1.on('done', f2); // 当f1发生done事件，就执行f2
		==>
			function f1(){
		　　　　setTimeout(function () {
		　　　　　　// f1的任务代码
		　　　　　　f1.trigger('done'); // 执行完成后，立即触发done事件，从而开始执行f2
		　　　　}, 1000);
		　　}
		==> 优点:是比较容易理解,可以绑定多个事件,每个事件可以指定多个回调函数,而且可以"去耦合"(Decoupling),有利于实现模块化。
			缺点:是整个程序都要变成事件驱动型，运行流程会变得很不清晰
	6.3.发布/订阅(publish-subscribe pattern),又称"观察者模式"(observer pattern)	
		存在一个"信号中心"，某个任务执行完成，就向信号中心"发布"（publish）一个信号，其他任务可以向信号中心"订阅"（subscribe）
		这个信号，从而知道什么时候自己可以开始执行
		// 下面采用的是Ben Alman的Tiny Pub/Sub，这是jQuery的一个插件
		==> 首先，f2向"信号中心"jQuery订阅"done"信号。

	6.4.Promises对象:CommonJS工作组提出的一种规范，目的是为异步编程提供统一接口,
		它的思想是，每一个异步任务返回一个Promise对象，该对象有一个then方法，允许指定回调函数

## 7.闭包:
    闭包是指有权访问另外一个函数作用域中的变量的函数,闭包是代码块和创建该代码块的上下文中数据的结合
	参考文章:http://www.cnblogs.com/TomXu/archive/2012/01/31/2330252.html
	7.1.关于闭包:闭包是一系列代码块(在ECMAScript中是函数)并且静态保存所有父级的作用域.
		通过这些保存的作用域来搜寻到函数中的自由变量
		(1).闭包可以访问当前函数以外的变量;
		(2).即使外部函数已经返回，闭包仍能访问外部函数定义的变量;
		(3).闭包可以更新外部变量的值
		(4).函数在被创建时保存外部作用域,是因为这个 被保存的作用域链(saved scope chain) 将会在未来的函数调用中用于变量查找
	7.2.作用域链:
		(1).定义:当访问一个变量时，解释器会首先在当前作用域查找标示符，如果没有找到，就去父作用域找，
			直到找到该变量的标示符或者不再存在父作用域了
			查找的属性在作用域链中不存在的话就会抛出 ReferenceError
	7.3.函数式语言:(ECMAScript支持这种风格)
		(1).函数即是数据,函数可以赋值给变量，可以当参数传递给其他函数，还可以从函数里返回等等
		(2).函数式参数（“Funarg”） —— 是指值为函数的参数;
		(3).接受函数式参数的函数称为高阶函数
	7.4.Javascript中闭包的实现:ECMAScript只采用静态作用域
		所有对象都引用一个[[Scope]],在ECMAScript中，同一个父上下文中创建的闭包是共用一个[[Scope]]属性的。
		也就是说，某个闭包对其中[[Scope]]的变量做修改会影响到其他闭包对其变量的读取
		如下例子:
			var data = [];
			for (var k = 0; k < 3; k++) {
			  data[k] = function () {
			    alert(k);
			  };
			}
			data[0](); // 3, 而不是0
			data[1](); // 3, 而不是1
			data[2](); // 3, 而不是2
			==> 同一个上下文中创建的闭包是共用一个[[Scope]]属性的。因此上层上下文中的变量“k”是可以很容易就被改变的
				activeContext.Scope = [
				  ... // 其它变量对象
				  {data: [...], k: 3} // 活动对象
				];
				data[0].[[Scope]] === Scope;
				data[1].[[Scope]] === Scope;
				data[2].[[Scope]] === Scope;
			==> 创建一个闭包可以解决上述问题:
				var data = [];
				for (var k = 0; k < 3; k++) {
				  data[k] = (function _helper(x) {
				    return function () {
				      alert(x);
				    };
				  })(k); // 传入"k"值
				}
				// 现在结果是正确的了
				data[0](); // 0
				data[1](); // 1
				data[2](); // 2
			==> 函数“_helper”创建出来之后，通过传入参数“k”激活。其返回值也是个函数，该函数保存在对应的数组元素中。
			这种技术产生了如下效果： 在函数激活时，每次“_helper”都会创建一个新的变量对象，其中含有参数“x”，“x”的值就
			是传递进来的“k”的值。这样一来，返回的函数的[[Scope]]就成了如下所示：
				data[0].[[Scope]] === [
				  ... // 其它变量对象
				  父级上下文中的活动对象AO: {data: [...], k: 3},
				  _helper上下文中的活动对象AO: {x: 0}
				];
				data[1].[[Scope]] === [
				  ... // 其它变量对象
				  父级上下文中的活动对象AO: {data: [...], k: 3},
				  _helper上下文中的活动对象AO: {x: 1}
				];
				data[2].[[Scope]] === [
				  ... // 其它变量对象
				  父级上下文中的活动对象AO: {data: [...], k: 3},
				  _helper上下文中的活动对象AO: {x: 2}
				];
	7.5.因为作用域链，使得所有的函数都是闭包（与函数类型无关： 匿名函数，FE，NFE，FD都是闭包）
		只有一类函数除外，那就是通过Function构造器创建的函数，因为其[[Scope]]只包含全局对象
	7.6.一道面试题:
		1.6.1.题目
			function fun(n,o) {
			  console.log(o)
			  return {
			    fun:function(m){
			      return fun(m,n);
			    }
			  };
			}
			var a = fun(0);  a.fun(1);  a.fun(2);  a.fun(3);//undefined,0,0,0
			var b = fun(0).fun(1).fun(2).fun(3);//undefined,0,1,2
			var c = fun(0).fun(1);  c.fun(2);  c.fun(3);//undefined,0,1,1
		1.6.2.函数分析:
			(1).三个函数 fun 之间的关系:
				function fun(n,o) {
				  console.log(o)
				  return {
				    fun:function(m){
				      //...
				    }
				  };
				}
				第一个函数 fun:标准的具名函数(命名函数)声明,是新创建的函数,返回值是一个对象字面量表达式,属于一个新的object;
				新的对象内部包含一个也叫 fun 的属性,属于匿名函数表达式,即fun这个属性中存放的是一个新创建匿名函数表达式
				"所有声明的匿名函数都是一个新函数;"
				所以第一个fun函数与第二个fun函数不相同，均为新创建的函数。
			(2).第三个函数:(函数表达式内部能不能访问存放当前函数的变量);
				①.
					var o={
					  fn:function (){
					    console.log(fn);
					  }
					};
					o.fn();//ERROR报错, Uncaught ReferenceError: fn is not defined
				②.
					var fn=function (){
					  console.log(fn);
					};
					fn();//function (){console.log(fn);};正确
				==> 使用 var 或是非对象内部的函数表达式内，可以访问到存放当前函数的变量；在对象内部的不能访问到
			==> 最内层的 return 出去的fun函数不是第二层fun函数，是最外层的fun函数
		1.6.3.题目运行分析:
			(1).var a = fun(0);  a.fun(1);  a.fun(2);  a.fun(3);
				第一个fun(0)是在调用第一层fun函数,后面几个fun(1),fun(2),fun(3),函数都是在调用第二层fun函数
				①.在第一次调用fun(0)时，o为undefined；
				②.第二次调用fun(1)时m为1，此时fun闭包了外层函数的n，也就是第一次调用的n=0，即m=1，n=0，
					并在内部调用第一层fun函数fun(1,0);所以o为0；
				③.第三次调用fun(2)时m为2，但依然是调用a.fun，所以还是闭包了第一次调用时的n，所以内部调
					用第一层的fun(2,0);所以o为0;
				④.第四次同理；
			(2).var b = fun(0).fun(1).fun(2).fun(3);
				fun(0)开始看，肯定是调用的第一层fun函数；而他的返回值是一个对象，所以第二个fun(1)调用的是第二层fun函数，
				后面几个也是调用的第二层fun函数;
				①.在第一次调用第一层fun(0)时，o为undefined；
				②.第二次调用 .fun(1)时m为1，此时fun闭包了外层函数的n，也就是第一次调用的n=0，即m=1，n=0，并在内部
					调用第一层fun函数fun(1,0);所以o为0；
				③.第三次调用 .fun(2)时m为2，此时当前的fun函数不是第一次执行的返回对象，而是第二次执行的返回对象。
					而在第二次执行第一层fun函数时时(1,0)所以n=1,o=0,返回时闭包了第二次的n，遂在第三次调用第三层fun函数时
					m=2,n=1，即调用第一层fun函数fun(2,1)，所以o为1；
				④.第四次调用 .fun(3)时m为3，闭包了第三次调用的n，同理，最终调用第一层fun函数为fun(3,2)；所以o为2；
			(3).var c = fun(0).fun(1);  c.fun(2);  c.fun(3);
				fun(0)为执行第一层fun函数，.fun(1)执行的是fun(0)返回的第二层fun函数，这里语句结束，遂c存放的是fun(1)的返回值，
				而不是fun(0)的返回值，所以c中闭包的也是fun(1)第二次执行的n的值。c.fun(2)执行的是fun(1)返回的第二层fun函数，
				c.fun(3)执行的也是fun(1)返回的第二层fun函数
				①.在第一次调用第一层fun(0)时，o为undefined；
				②.第二次调用 .fun(1)时m为1，此时fun闭包了外层函数的n，也就是第一次调用的n=0，即m=1，n=0，并在内部调用
					第一层fun函数fun(1,0);所以o为0；
				③.第三次调用.fun(2)时m为2,此时fun闭包的是第二次调用的n=1,即m=2，n=1，并在内部调用第一
					层fun函数fun(2,1);所以o为1；
				④.第四次.fun(3)时同理，但依然是调用的第二次的返回值，遂最终调用第一层fun函数fun(3,1)，所以o还为1
## 8.with 语句

## 9.Javascript 函数与栈:
	看如下代码:
	function test(){  
	    setTimeout(function() {alert(1)}, 0);  
	    alert(2);  
	}  
	test(); // 2 1
	JavaScript 是单线程的，即同一时间只执行一条代码，所以每一个 JavaScript 代码执行块会 “阻塞” 其它异步事件的执行。
	其次，和其他的编程语言一样，Javascript 中的函数调用也是通过堆栈实现的。在执行函数 test 的时候，test 先入栈，
	如果不给 alert(1)加 setTimeout，那么 alert(1)第 2 个入栈，最后是 alert(2)。但现在给 alert(1)加上 setTimeout 后，
	alert(1)就被加入到了一个新的堆栈中等待，并 “尽可能快” 的执行。这个尽可能快就是指在 a 的堆栈完成后就立刻执行，
	因此实际的执行结果就是先 alert(2)，再 alert(1)。在这里 setTimeout 实际上是让 alert(1)脱离了当前函数调用堆栈
