## 1.设计模式分类:
	1.1.创建型设计模式:专注于处理对象创建机制,以适合给定情况的方式来创建对象;
		Constructor(构造器模式),Factory(工厂模式),Abstract(抽象),Prototype(原型),
		Singleton(单例),Builder(生成器)	
	1.2.结构型设计模式:与对象组合有关,通常可以用于找出在不同对象之间建立关系的简单方法,
		Decorator(装饰者), Facade(外观模式),Flywight(享元),Adapter(适配器), Proxy(代理)
	1.3.行为设计模式:专注于改善或简化系统中不同对象之间的通信
		Iterator(迭代器), Mediator(中介者),Observe(观察者),Visitor(访问者)

## 2.单例模式:
    单例就是保证一个类只有一个实例，实现的方法一般是先判断实例存在与否，如果存在直接返回，
	如果不存在就创建了再返回，这就确保了一个类只有一个实例对象
	==> 单例一般是用在系统间各种模式的通信协调上
	var Singleton = (function(){
		var instance;
		function init(){
			return {
				publicMethod : function(){
					console.log('.....');
				},
				publicProperty:'test'
			};
		}
		return {
			getInstance : function(){
				if(!instance){
					instance = init();
				}
				return instance;
			}
		}
	})();
	==> 最佳实践:
		var Singleton = (function(){
			function Singleton(args){
				var args = args || {};
				this.name = 'Coco';
				this.pointX = args.pointX || 6;
				this.pointY = args.pointY || 10;
			}
			var instance;
			var _static = {
				name : 'Coco',
				getInstance : function(args){
					if(instance === undefined){
						instance = new Singleton(args);
					}
					return instance;
				}
			}
			return _static;
		})()
	==> 注意:
		Singleton 的存在往往表明系统中的模块要么是紧耦合设计,要么是其逻辑过于分散在代码库的多个部分;

## 3.构造器模式:
	构造函数用于创建特定类型的对象——不仅声明了使用的对象,构造函数还可以接受参数以便第一次创建对象的时候设置对象的成员值
		function Car(model, year, miles) {
		    this.model = model;
		    this.year = year;
		    this.miles = miles;
		    // 自定义一个output输出内容
		    this.output = function () {
		        return this.model + "走了" + this.miles + "公里";
		    }
		}
		//方法1：作为函数调用,如果不适用new直接调用函数的话，this指向的是全局对象window
		Car("大叔", 2009, 20000);  //添加到window对象上
		console.log(window.output());
		//方法2：在另外一个对象的作用域内调用
		var o = new Object();
		Car.call(o, "Dudu", 2010, 5000);
		console.log(o.output()); 
	==> 强制使用 new:
		function Car(model, year, miles) {
		    if (!(this instanceof Car)) {
		        return new Car(model, year, miles);
		    }
		    this.model = model;
		    this.year = year;
		    this.miles = miles;
		    this.output = function () {
		        return this.model + "走了" + this.miles + "公里";
		    }
		}
## 4.观察者模式:
    * http://www.cnblogs.com/TomXu/archive/2012/03/02/2355128.html
	(1).又叫发布订阅模式(Publish/Subscribe)，它定义了一种一对多的关系，让多个观察者对象同时监听某一个主题对象，
	这个主题对象的状态发生变化时就会通知所有的观察者对象,使得它们能够自动更新自己
	==> 观察者的使用场合就是：
		当一个对象的改变需要同时改变其它对象，并且它不知道具体有多少对象需要改变的时候，就应该考虑使用观察者模式
	(2).优点:
		支持简单的广播通信，自动通知所有已经订阅过的对象。
		页面载入后目标对象很容易与观察者存在一种动态关联，增加了灵活性。
		目标对象与观察者之间的抽象耦合关系能够单独扩展以及重用。
	(3).实现1:
		var pubsub = {};
		(function(q){
			var topics = {},
				subUid = -1;
			q.publish = function(topic, args){
				if(!topics[topic]){
					return false;
				}
				setTimeout(function(){
					var subscribers = topics[topic],
						len = subscribers ? subscribers.length : 0;
					while(len--){
						subscribers[len].func(topic, args);
					}

				}, 0);
			};
			q.subscrib =  function(topic, func){
				if(!topics[topic]){
					topics[topic] = [];
				}
				var token = (++subUid).toString();
				topics[topic].push({
					token : token,
					func : func
				});
				return token;
			};
			q.unsubscrib = function(token){
				for(var m in topics){
					if(topics[m]){
						for(var i =0, j=topics[m].length;i<j;i++){
							if(topics[m].token === token){
								topics[m].splice(i, 1);
								return token;
							}
						}
					}
				}
			}
		})(pubsub);
	(4).实现2:如果想让多个对象都具有观察者发布订阅的功能,可以定义一个通用的函数,然后将该函数的功能应用到需要观察者功能的对象上
		//通用代码
		var observer = {
		    //订阅
		    addSubscriber: function (callback) {
		        this.subscribers[this.subscribers.length] = callback;
		    },
		    //退订
		    removeSubscriber: function (callback) {
		        for (var i = 0; i < this.subscribers.length; i++) {
		            if (this.subscribers[i] === callback) {
		                delete (this.subscribers[i]);
		            }
		        }
		    },
		    //发布
		    publish: function (what) {
		        for (var i = 0; i < this.subscribers.length; i++) {
		            if (typeof this.subscribers[i] === 'function') {
		                this.subscribers[i](what);
		            }
		        }
		    },
		    // 将对象o具有观察者功能
		    make: function (o) { 
		        for (var i in this) {
		            o[i] = this[i];
		            o.subscribers = [];
		        }
		    }
		};
		// 然后订阅2个对象blogger和user，使用observer.make方法将这2个对象具有观察者功能
		var blogger = {
		    recommend: function (id) {
		        var msg = 'dudu 推荐了的帖子:' + id;
		        this.publish(msg);
		    }
		};

		var user = {
		    vote: function (id) {
		        var msg = '有人投票了!ID=' + id;
		        this.publish(msg);
		    }
		};

		observer.make(blogger);
		observer.make(user);
		//使用方法就比较简单了，订阅不同的回调函数，以便可以注册到不同的观察者对象里
		var tom = {
		    read: function (what) {
		        console.log('Tom看到了如下信息：' + what)
		    }
		};

		var mm = {
		    show: function (what) {
		        console.log('mm看到了如下信息：' + what)
		    }
		};
		// 订阅
		blogger.addSubscriber(tom.read);
		blogger.addSubscriber(mm.show);
		blogger.recommend(123); //调用发布

		//退订
		blogger.removeSubscriber(mm.show);
		blogger.recommend(456); //调用发布

		//另外一个对象的订阅
		user.addSubscriber(mm.show);
		user.vote(789); //调用发布
	(5).jQuery版本:根据jQuery1.7版新增的on/off功能，我们也可以定义jQuery版的观察者
		(function ($) {
		    var o = $({});
		    $.subscribe = function () {
		        o.on.apply(o, arguments);
		    };
		    $.unsubscribe = function () {
		        o.off.apply(o, arguments);
		    };
		    $.publish = function () {
		        o.trigger.apply(o, arguments);
		    };
		} (jQuery));
		// 订阅和退订使用的是字符串名称，而不是回调函数名称，所以即便传入的是匿名函数，我们也是可以退订的
		//回调函数
		function handle(e, a, b, c) {
		    // `e`是事件对象，不需要关注
		    console.log(a + b + c);
		};
		//订阅
		$.subscribe("/some/topic", handle);
		//发布
		$.publish("/some/topic", ["a", "b", "c"]); // 输出abc 
		$.unsubscribe("/some/topic", handle); // 退订
		//订阅
		$.subscribe("/some/topic", function (e, a, b, c) {
		    console.log(a + b + c);
		});
		$.publish("/some/topic", ["a", "b", "c"]); // 输出abc
		//退订（退订使用的是/some/topic名称，而不是回调函数哦，和版本一的例子不一样
		$.unsubscribe("/some/topic"); 

## 5.工厂模式:
	(1).概念:工厂模式定义一个用于创建对象的接口，这个接口由子类决定实例化哪一个类。该模式使一个类的实例化延迟到了子类。
		而子类可以重写接口方法以便创建的时候指定自己的对象类型；
	(2).什么时候使用工厂模式
		对象的构建十分复杂
		需要依赖具体环境创建不同实例
		处理大量具有相同属性的小对象
	(3).什么时候不该用工厂模式
		不滥用运用工厂模式，有时候仅仅只是给代码增加了不必要的复杂度，同时使得测试难以运行下去;
		
## 6.建造模式:
    可以将一个复杂对象的构建与其表示相分离，使得同样的构建过程可以创建不同的表示
	(1).概念:建造者模式实际就是一个指挥者,一个建造者,一个使用指挥者调用具体的建造者工作得出结果的客户;
	(2).用途:
		用于分布构建一个复杂的对象
		解耦封装过程和具体创建的组建
		无需关心组件如何组装
	(3).实现:
