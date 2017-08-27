深入理解JavaScript系列:
	http://www.cnblogs.com/TomXu/archive/2011/12/15/2288411.html
1.关于全局变量:
	(1).在js开发中,减少使用全局变量,以免与第三方js插件存在命名冲突.如果一定要使用全局变量,可以使用命名空间或函数立即自动执行
	(2).全局变量的自动声明:
		①.全局变量的隐式声明:
			function sum (x, y) {
				result  = x  + y; // 不推荐写法,隐式全局变量
				return result;
			}
			函数调用后最后的结果就多一个命名空间;
		②.使用任务链进行部分var声明:
			// 勿使用如下声明
			function foo(){
				var a = b = 0;
				// body...
			}
			此现象发生的原因在于这个从右到左的赋值:
			A:首先,是赋值表达式b=0,此情况下b是未声明的;这个表达式返回0,然后就赋值给了var声明的局部变量a
	(3).通过var声明的全局变量(任何函数之外创建的)是不能被删除的;
		无var创建的隐士全局变量(不管是否在函数中创建)是能被删除的;
		==> 这表明，在技术上,隐式全局变量并不是真正的全局变量,但它们是全局对象的属性.
			属性是可以通过delete操作符删除的,而变量是不能的
		**在ECMAScript5严格模式下,未声明的变量工作时会抛出一个错误
	(4).访问全局对象:
		var global = (function(){
			return this;
		}())
2.单 var 形式声明变量:
	(1).在函数顶部使用单var语句是比较有用的一种形式，其好处在于:
		①.提供了一个单一的地方去寻找功能所需要的所有局部变量
		②.防止变量在定义之前使用的逻辑错误
		③.帮助你记住声明的全局变量，因此较少了全局变量//zxx:此处我自己是有点晕乎的…
		④.少代码（类型啊传值啊单线完成）;
	(2)	var x = 10, y = 10,....;
	(3).var 散布的问题:
		// 反例
		myname = "global"; // 全局变量
		function func() {
		    alert(myname); // "undefined", 因为myname被当做了函数的局部变量(尽管是之后声明的)所有的变量声明当被悬置到函数的顶部了
		    var myname = "local";
		    alert(myname); // "local"
		}
		func();
		// 为了避免上述问题,最好预先声明你在函数中使用的全部变量
		**难点:
			代码处理分两个阶段,第一阶段是变量,函数声明,以及正常格式的参数创建,这是一个解析和进入上下文 的阶段.
			第二个阶段是代码执行,函数表达式和不合格的标识符（为声明的变量）被创建
3.for 循环与 for...in..循环:
	(1).for 循环:使用在数组或类数组对象上;
	(2).for..in...循环:使用在对象上,枚举,
		// 技术上也可以使用for..in循环数组,但不推荐,因为如果数组对象已被自定义的功能增强，就可能发生逻辑错误,
		// 另外，在for-in中，属性列表的顺序（序列）是不能保证的

4.类型转换:
	4.1.避免隐式类型转换:在比较值和表达式时始终使用 === 或者 !== 操作符;
	4.2.避免 eval() 函数:该函数接收任意字符串,并当作Javascript代码来处理
		// 反面实例:
		var property = "name";
		alert(eval("obj." + property))
		// 更好的方法
		var property = "name";
		alert(obj[property]);
	4.3.使用 eval() 存在安全隐患:被执行的代码可能已经被篡改;
	4.4.给 setInterval(), setTimeout()和 Function()构造函数传递字符串,大部分情况下,与使用 eval()是类似的,因此要避免:
		(1).
			// 反面示例
			setTimeout("myFunc()", 1000);
			setTimeout("myFunc(1, 2, 3)", 1000);

			// 更好的
			setTimeout(myFunc, 1000);
			setTimeout(function () {
			   myFunc(1, 2, 3);
			}, 1000);
		(2).如果必须使用 eval()时,考虑使用 new Function()代替,好处是:在新 Function()中作代码评估是在局部函数作用域中运行，
			所以代码中任何被评估的通过 var 定义的变量都不会自动变成全局变量:
			console.log(typeof un);    // "undefined"
			console.log(typeof deux); // "undefined"
			console.log(typeof trois); // "undefined"

			var jsstring = "var un = 1; console.log(un);";
			eval(jsstring); // logs "1", un变成了全局变量,污染了命名空间

			jsstring = "var deux = 2; console.log(deux);";
			new Function(jsstring)(); // logs "2"

			jsstring = "var trois = 3; console.log(trois);";
			(function () {
			   eval(jsstring);
			}()); // logs "3"

			console.log(typeof un); // number
			console.log(typeof deux); // "undefined"
			console.log(typeof trois); // "undefined"			
		(3).eval()可以干扰作用域链，而 Function()更安分守己些,你在哪里执行 Function(),它只看到全局作用域.所以其能很好的
			避免本地变量污染,eval()可以访问和修改它外部作用域中的变量,Function() 不存在该问题;
			(function () {
			   var local = 1;
			   eval("local = 3; console.log(local)"); // logs "3"
			   console.log(local); // logs "3", eval()可以修改其外部作用域的变量
			}());

			(function () {
			   var local = 1;
			   Function("console.log(typeof local);")(); // logs undefined, Function()对于其外部的变量都当成全局变量
			}());
	4.5.parseInt()下的数值转换:字符串中获取数值,该方法接受另一个基数参数,这经常省略,但不应该
		在ECMAScript 3中，开头为”0″的字符串被当做8进制处理了，但这已在ECMAScript 5中改变了
5.编码规范:
	(1).缩进
	(2).{}的使用
	(3).空格
	(4).命名规范
6.命名函数表达式:
	6.1.函数表达式和函数声明:
		(1).创建函数最常用的两个方法:
			函数声明:
				function 函数名称([参数]){函数体}
			函数表达式:
				function [函数名称]([参数]){函数体}
		(2).如果声明了函数名称,如何判断是函数表达式还是函数声明:
			ECMAScript是通过上下文来区分的,如果 function foo(){}是作为赋值表达式的一部分的话,
			那它就是一个函数表达式,如果 function foo(){}被包含在一个函数体内,
			或者位于程序的最顶部的话,那它就是一个函数声明;
			function foo(){} // 函数声明,因为其是程序的一部分
			var bar  = function(){..}// 表达式,因为其是赋值表达式的一部分
			new function bar(){}; //表达式,因为其是new表达式

			(function(){
				function bar(){} //声明,因为其是函数体的一部分
			})();
		(3).不常见的函数表达式:(function(){}) // ()是一个分组操作符,其内部只能包含表达式
			function foo(){} // 函数声明
  			(function foo(){}); // 函数表达式：包含在分组操作符内
  		(4).函数声明在页面加载时就被解析
  			alert(fn()); //函数fn是在alert之后声明的，但是在alert执行的时候，fn已经有定义了
  			function fn(){
  				return 10;
  			}
  		(5).函数声明在条件语句内虽然可以用,但是没有被标准化,也就是说不同的环境可能有不同的执行结果,
  			所以这样情况下，最好使用函数表达式:
  				// 千万别这样做,因为有的浏览器会返回first的这个function，而有的浏览器返回的却是第二个
			  if (true) {
			    function foo() {
			      return 'first';
			    }
			  } else {
			    function foo() {
			      return 'second';
			    }
			  }
			  foo();
			  // 相反，这样情况，我们要用函数表达式
			  var foo;
			  if (true) {
			    foo = function() {
			      return 'first';
			    };
			  } else {
			    foo = function() {
			      return 'second';
			    };
			  }
			  foo();
		(6).函数声明只能出现在程序或函数体内,从句法上讲,它们不能出现在Block(块)({ ... })中,例如不能出现在 if、while 或 for 语句中
			因为Block(块)中只能包含Statement语句,而不能包含函数声明这样的源元素;
	6.2.函数语句:在ECMAScript的语法扩展中,有一个是函数语句,目前只有基于Gecko的浏览器实现了该扩展:
		// 这些不常用
	6.3.命名函数表达式:var bar = function foo(){};就是一个有效的命名函数表达式,
		(1).有一点需要记住：这个名字只在新定义的函数作用域内有效,因为规范规定了标示符不能在外围的作用域内有效
			var f = function foo(){
				return typeof foo; // foo 在内部作用域有效,
			};
			console.info(typeof foo); //undefined,foo在外部作用域是不可见的
			f();// function
		(2).命名函数表达式到底有啥用啊？为啥要取名？
			给它一个名字就是可以让调试过程更方便,因为在调试的时候,如果在调用栈中的每个项都有自己名称的描述;
	6.4.jscript的bug:(IE)
		(1).函数表达式的标示符泄露到外部作用域:
			var f = function g(){};
			typeof g; // function
			// IE9貌似已经修复了这个问题
		(2).将命名函数表达式同时当作函数声明和函数表达式:
			typeof g;
			var f = function g(){}
			// JScript实际上是把命名函数表达式当成函数声明了，因为它在实际声明之前就解析了g
		(3).命名函数表达式会创建两个截然不同的函数对象:
			var f = function g(){};
		    f === g; // false
		    f.expando = 'foo';
		    g.expando; // undefined
	6.5.http://www.cnblogs.com/TomXu/archive/2011/12/29/2290308.html
	
7.解析Module模式:JavaScript编程中一个非常通用的模式
	7.1.基本特征:
		(1).模块化,可重用;
		(2).封装了变量和 function，和全局的 namaspace 不接触，松耦合
		(3).只暴露可用 public 的方法，其它私有方法全部隐藏
	7.2.基本用法:
		(1).最简单实现:
			var Calculator = function (eq) {
			    //这里可以声明私有成员
			    var eqCtl = document.getElementById(eq);
			    return {
			        // 暴露公开的成员
			        add: function (x, y) {
			            var val = x + y;
			            eqCtl.innerHTML = val;
			        }
			    };
			};
			var calculator = new Calculator('eq');
			calculator.add(2, 2);
		(2).匿名闭包:匿名闭包是让一切成为可能的基础，而这也是 JavaScript 最好的特性,
			函数内部的代码一直存在于闭包内，在整个运行周期内，该闭包都保证了内部的代码处于私有状态
			(function () {
			    // ... 所有的变量和function都在这里声明，并且作用域也只能在这个匿名闭包里
			    // ...但是这里的代码依然可以访问外部全局的对象
			}());
			注意，匿名函数后面的括号，这是JavaScript语言所要求的，因为如果你不声明的话，JavaScript解释器默认是声明
			一个 function 函数，有括号，就是创建一个函数表达式，也就是自执行;
		(3).引用全局变量:将全局变量当成一个参数传入到匿名函数然后使用
			(function ($, YAHOO) {
			    // 这里，我们的代码就可以使用全局的jQuery对象了，YAHOO也是一样
			} (jQuery, YAHOO));

			==> 使用全局变量:通过匿名函数的返回值来返回这个全局变量，这也就是一个基本的Module模式
			var blogModule = (function () {
			    var my = {}, privateName = "博客园";
			    // 方法私有,外部不可见
			    function privateAddTopic(data) {
			        // 这里是内部处理代码
			    }
			    my.Name = privateName;//外部可见
			    my.AddTopic = function (data) {
			        privateAddTopic(data);
			    }; //外部可用函数
			    return my;
			} ());
	7.3.高级用法:
		(1).基本扩展:Module模式的一个限制就是所有的代码都要写在一个文件,
			先将blogModule传进去，添加一个函数属性，然后再返回就达到了我们所说的目的，上代码：
			var blogModule = (function (my) {
			    my.AddPhoto = function () {
			        //添加内部代码  
			    };
			    return my;
			} (blogModule)); 
		(2).松耦合扩展:上述代码尽管可以执行,但必须先声明blogModule,然后再执行上面的扩展代码
			var blogModule = (function (my) {
			    // 添加一些功能  			    
			    return my;
			} (blogModule || {}));  // 在存在的时候直接用，不存在的时候直接赋值为{}
		(3).紧耦合扩展:松耦合扩展可能也会存在一些限制，比如你没办法重写你的一些属性或者函数，
			也不能在初始化的时候就是用Module的属性
			var blogModule = (function (my) {
			    var oldAddPhotoMethod = my.AddPhoto;
			    my.AddPhoto = function () {
			        // 重载方法，依然可通过oldAddPhotoMethod调用旧的方法
			    };
			    return my;
			} (blogModule));
8.立即调用的函数表达式:
	8.1.当你声明类似 function foo(){}或 var foo = function(){}函数的时候，通过在后面加个括弧就可以实现自执行，例如foo()
	8.2.自执行函数表达式:
		(function () { /* code */ } ()); // 推荐使用这个
		(function () { /* code */ })(); // 但是这个也是可以用的
		(1).function foo(){ /* code */ }(); // SyntaxError: Unexpected token )
			解析器解析全局的 function 或者 function 内部 function 关键字的时候,默认是认为 function 声明,而不是 function 表达式,
			如果你不显示告诉编译器,它默认会声明成一个缺少名字的 function,并且抛出一个语法错误信息,因为 function 声明需要一个名字;
		(2).由于括弧()和JS的&&,异或,逗号等操作符是在函数表达式和函数声明上消除歧义的
			所以一旦解析器知道其中一个已经是表达式了，其它的也都默认为表达式了
	8.3.自执行匿名函数和立即执行的函数表达式区别:

9.S.O.L.I.D 五大原则:
	(1).The Single Responsibility Principle（单一职责SRP）
	(2).The Open/Closed Principle（开闭原则OCP）
	(3).The Liskov Substitution Principle（里氏替换原则LSP）
	(4).The Interface Segregation Principle（接口分离原则ISP）
	(5).The Dependency Inversion Principle（依赖反转原则DIP）
10.S.O.L.I.D五大原则之:单一职责SRP:
	10.1.遵守单一职责的好处是可以让我们很容易地来维护这个对象，当一个对象封装了很多职责的话，
		一旦一个职责需要修改，势必会影响该对象想的其它职责代码
	10.2.区分职责:
		Information holder – 该对象设计为存储对象并提供对象信息给其它对象。
		Structurer – 该对象设计为维护对象和信息之间的关系
		Service provider – 该对象设计为处理工作并提供服务给其它对象
		Controller – 该对象设计为控制决策一系列负责的任务处理
		Coordinator – 该对象不做任何决策处理工作，只是delegate工作到其它对象上
		Interfacer – 该对象设计为在系统的各个部分转化信息（或请求）
11.S.O.L.I.D五大原则之:开闭原则OCP
	11.1.软件实体（类，模块，方法等等）应当对扩展开放，对修改关闭，即软件实体应当在不修改的前提下扩展。
		open for extension 对扩展开放
		Close for modification 对修改关闭

12.S.O.L.I.D五大原则之:里氏替换原则LSP
	12.1.派生类型必须可以替换它的基类型
		本质不是真的和继承有关,而是行为兼容性
		
13.S.O.L.I.D五大原则之:接口隔离原则ISP(The Interface Segregation Principle), 不应该强迫客户依赖于它们不用的方法
	
14.S.O.L.I.D五大原则之:依赖倒置原则DIP
	14.1.描述:
		高层模块不应该依赖于低层模块，二者都应该依赖于抽象;
		抽象不应该依赖于细节，细节应该依赖于抽象
		确保应用程序或框架的主要组件从非重要的底层组件实现细节解耦出来










































