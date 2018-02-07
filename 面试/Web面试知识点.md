# 一.Javascript:
## 1.https 与 http 
	1.1.两者的区别:
	1.2.https如何加密的 
		http://www.codeceo.com/article/https-knowledge.html
		http://www.codeceo.com/article/https-worker.html
		http://www.codeceo.com/article/https-protocol.html
		http://www.codeceo.com/article/https-performance.html
		http://www.codeceo.com/article/https-protocol-config.html
		http://www.codeceo.com/article/https-protocol-4.html
	1.3.post和get提交的区别
		get的限制是协议的限制吗?
## 2.html访问全过程, dns是基于tcp还是udp的


### 3.跨域:
	http://www.cnblogs.com/cat3/archive/2011/06/15/2081559.html
	http://www.cnblogs.com/scottckt/archive/2011/11/12/2246531.html
	http://www.cnblogs.com/rainman/archive/2011/02/20/1959325.html
	3.1.为什么会发生跨域问题?
	3.2.如何解决跨域问题?
	3.3.跨域大概可以分iframe的跨域，和纯粹的跨全域请求
	3.4.其实正统的跨全域的解决方法大致也就，JSONP,Access Control和服务器代理这么三种
	3.5.JSONP原理
		HTML里面所有带src属性的标签都可以跨域
	
	http://www.cnblogs.com/xxcanghai/p/5205998.html
### 4.事件模型及事件代理/委托
	(1).事件的三个阶段:捕获、目标、冒泡阶段，低版本IE不支持捕获阶段
	(2).IE和W3C不同绑定事件解绑事件的方法有什么区别，参数分别是什么，以及事件对象e有什么区别
	(3).事件的代理/委托的原理以及优缺点:
		靠事件的冒泡机制来实现
		优点:
			可以大量节省内存占用，减少事件注册，比如在table上代理所有td的click事件就非常棒
			可以实现当新增子对象时无需再次对其绑定事件，对于动态内容部分尤为合适
		
	(4).JS原生实现事件代理
	(5).实现事件模型
		即写一个类或是一个模块，有两个函数，一个bind一个trigger，分别实现绑定事件和触发事件，
		核心需求就是可以对某一个事件名称绑定多个事件响应函数，然后触发这个事件名称时，依次按绑定顺序触发相应的响应函数

### 5.前端性能优化:	
	网络性能优化，加快访问速度，浏览器并行加载数量，怎样实现原生JS异步载入，CDN加速的原理，如何将不同静态资源发布
	到多个域名服务器上，发布后这些静态字段的url路径改怎么批量改写，用什么工具进行项目打包，
	css打包后的相对路径怎么转换为绝对路径，用什么工具进行项目模块依赖管理，怎么进行cookie优化等等

### 6.闭包:
	什么情况下会发生闭包，为什么需要闭包，什么场景下需要，闭包闭了谁，
	怎么释放被闭包的变量内存，闭包的优点是什么，缺点是什么等等

### 7.Function.bind函数
	7.1.首先会要求解释下这个函数的作用，以及在什么场景下需要用到它，最后手写一个Function.bind函数
	7.2.掌握核心几点就没问题
		(1).Function.bind返回的也是一个函数，所以注定发生了闭包，
		(2).在返回的这个函数中去调用一个其他的函数，这其实本质上就是函数钩子(HOOK)
	7.2.关于在JS里的函数钩子
		(1).保持函数的this指向
		(2).保持函数的所有参数都传递到目标函数
		(3).保持函数的返回值

	if (!Function.prototype.bind) {
		Function.prototype.bind = function (oThis) {
			if (typeof this !== "function") {
			// closest thing possible to the ECMAScript 5
			// internal IsCallable function
			throw new TypeError("Function.prototype.bind - what is trying to be bound is not callable");
			}

			var aArgs = Array.prototype.slice.call(arguments, 1), 
			fToBind = this, 
			fNOP = function () {},
			fBound = function () {
				return fToBind.apply(this instanceof fNOP
									 ? this
									 : oThis || this,
								   aArgs.concat(Array.prototype.slice.call(arguments)));
			};

			fNOP.prototype = this.prototype;
			fBound.prototype = new fNOP();

			return fBound;
		};
	}

### 8.数组快速排序/去重

### 9.JS的定义提升

### 10.将url的查询参数解析成字典对象:使用正则匹配
	function getQueryObject(url) {
		url = url == null ? window.location.href : url;
		var search = url.substring(url.lastIndexOf("?") + 1);
		var obj = {};
		var reg = /([^?&=]+)=([^?&=]*)/g;
		search.replace(reg, function (rs, $1, $2) {
			var name = decodeURIComponent($1);
			var val = decodeURIComponent($2);                
			val = String(val);
			obj[name] = val;
			return rs;
		});
		return obj;
	}
	
### 11.函数节流	
	http://www.alloyteam.com/2012/11/javascript-throttle/

### 12.设计模式:观察者模式，职责链模式，工厂模式
	
# 二.HTML与CSS
### 1.css垂直居中方法
	问题又可以细分为，被垂直居中的元素是否定高，是文字还是块，文字是单行还是多行文字等等
	
### 2.自适应布局
	这个问题可以划分为，左固定右自适应宽度，上固定下固定中间自适应高度等等布局要求












