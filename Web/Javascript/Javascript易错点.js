1.关于自执行函数:使用闭包保存状态
	(1).页面有10个<a>标签,按如下代码点击<a>标签,结果是怎样的?
		// 这个代码是错误的，因为变量i从来就没背locked住
		// 相反，当循环执行以后，我们在点击的时候i才获得数值
		// 因为这个时候i操真正获得值
		// 所以说无论点击那个连接，最终显示的都是I am link #10
		var elems = document.getElementsByTagName('a');
		for (var i = 0; i < elems.length; i++) {
		    elems[i].addEventListener('click', function (e) {
		        e.preventDefault();
		        alert('I am link #' + i);
		    }, 'false');
		}
	(2).如果需要得到正确结果,可以写成自执行函数形式:
		// 这个是可以用的，因为他在自执行函数表达式闭包内部
		// i的值作为locked的索引存在，在循环执行结束以后，尽管最后i的值变成了a元素总数（例如10）
		// 但闭包内部的lockedInIndex值是没有改变，因为他已经执行完毕了
		// 所以当点击连接的时候，结果是正确的
		var elems = document.getElementsByTagName('a');
		for (var i = 0; i < elems.length; i++) {
		    (function (lockedInIndex) {
		        elems[i].addEventListener('click', function (e) {
		            e.preventDefault();
		            alert('I am link #' + lockedInIndex);
		        }, 'false');
		    })(i);
		}
2.关于<a>标签中执行函数的写法:
	(1).a href="javascript:void(0)" onclick="fun()"
	(2).a href="javascript:;" onclick="fun()"
	(3).a href="#" onclick="fun()"	
	==> 写法 (1) 和 写法 (2) 没有什么区别,void(0)返回 undefined, 另一个是执行空语句
		(3) 执行完后会跳转到页面顶固
	==> 推荐写法:
		<a href="#" onclick="fun(); return false;">Link</a>
		return false 可以防止 href 里的#继续执行.

3.parseInt('string', radix):可解析一个字符串，并返回一个整数
	(1).radix: 表示转换的基数，也就是我们常说的2进制、8进制、10进制、16进制等。范围从 2~36,如果该参数小于 2 或者大于 36,
		则 parseInt() 将返回 NaN
	(2).parseInt() 方法首先查看位置 0 处的字符，判断它是否是个有效数字；如果不是，该方法将返回 NaN，不再继续执行其他操作,
	但如果该字符是有效数字，该方法将查看位置 1 处的字符，进行同样的测试。这一过程将持续到发现非有效数字的字符为止，
	此时 parseInt() 将把该字符之前的字符串转换成数字

4.关于Javascript中数字的部分知识总结：
	(1).Javascript中，由于其变量内容不同，变量被分为基本数据类型变量和引用数据类型变量。
		基本类型变量用 8 byte 内存，存储基本数据类型(数值、布尔值、null和未定义)的值，
		引用类型变量则只保存对对象、数组和函数等引用类型的值的引用(即内存地址)。
		==> JavaScript内部，所有数字都是以 64 位浮点数形式储存，即使整数也是如此
	(2). JS中的数字是不分类型的，也就是没有 byte/int/float/double 等的差异。

5.不要在Javascript中使用连等操作: 	http://www.cnblogs.com/xxcanghai/p/4998076.html
	var a = {n:1};
	a.x = a = {n:2};
	console.log(a.x); // 输出 undefined
	5.1.赋值顺序:
		假设有一句代码: A=B=C; ，赋值语句的执行顺序是从右至左，所以问题在于：
		是猜想1： B = C; A = C; ?
		还是猜想2： B = C; A = B;  ?
		==> 连等赋值真正的运算规则是  B = C; A = B;  即连续赋值是从右至左永远只取等号右边的表达式结果赋值到等号左侧
	5.2.连续赋值语句虽然是遵从从右至左依次赋值的规则但依然不能将语句拆开来写，至于为什么
		js内部为了保证赋值语句的正确，会在一条赋值语句执行前，先把所有要赋值的引用地址取出一个副本，再依次赋值
		a.x=a={n:2};  的逻辑是：
		(1).在执行前，会先将a和a.x中的a的引用地址都取出来，此值他们都指向{n:1}
		(2).在内存中创建一个新对象{n:2}
		(3).执行a={n:2}，将a的引用从指向{n:1}改为指向新的{n:2}
		(4).执行a.x=a，此时a已经指向了新对象，而a.x因为在执行前保留了原引用，所以a.x的a依然指向原先的{n:1}对象
		所以给原对象新增一个属性x，内容为{n:2}也就是现在a
		(5).语句执行结束，原对象由{n:1}变成{n:1,x:{n:2}}，而原对象因为无人再引用他，所以被GC回收，当前a指向新对象{n:2}
6.JavaScript的分号插入机制
	(1).function func() {
		    return 
		    {
		        name: "Wilber"
		    };
		}
		alert(func());
		// undefined
	(2).function func() {
		    return {
		        name: "Wilber"
		    };
		}
		alert(func());
		// [object]










































