Javascript兼容性问题收集:
1.Array 中 indexOf() 为ecmaScript5新方法 IE8以下(包括IE8， IE8只支持部分ecma5)不支持
如果需要使用的数组的该方法,可以先判断该方法是否存在,如果不存在,则可以在 Array 上加上 该方法:
	if (!Array.prototype.indexOf) {
		Array.prototype.indexOf = function (item){
			var result =-1, a_item = null;
			if(this.length == 0){
				return result;
			}
			for (var i =0, len = this.length; i < len; i++){
				a_item = this[i];
				if (a_item == item) {
					result = i;
					break;
				}
			}
			return result;
		}
	};
	if (!Array.prototype.forEach) {
	    Array.prototype.forEach = function (fn, thisObj) {
	        var scope = thisObj || window;
	        for (var i = 0, j = this.length; i < j; ++i) {
	            fn.call(scope, this[i], i, this);
	        }
	    };
	}
	if (!Array.prototype.filter) {
	    Array.prototype.filter = function (fn, thisObj) {
	        var scope = thisObj || window;
	        var a = [];
	        for (var i = 0, j = this.length; i < j; ++i) {
	            if (!fn.call(scope, this[i], i, this)) {
	                continue;
	            }
	            a.push(this[i]);
	        }
	        return a;
	    };
	}
2.关于 Javascript 获取DOM元素位置和尺寸大小
	2.1.基本概念:
		(1).每个html元素都有下列属性:
			offsetWidth		clientWidth		scrollWidth
			offsetHeight	clientHeight	scrollHeight
			offsetLeft		clientLeft		scrollLeft
			offsetTop		clientTop		scrollTop
		(2).HTML元素的实际内容有可能比分配用来容纳内容的盒子更大,因此可能会出现滚动条,内容区域是视口,
			当实际内容比视口大的时候,需要把元素的滚动条位置考虑进去
			①. clientHeight 和 clientWidth 用于描述元素内尺寸,是指 元素内容+内边距 大小,
				不包括边框（IE下实际包括）、外边距、滚动条部分
			②. offsetHeight 和 offsetWidth 用于描述元素外尺寸，是指 元素内容+内边距+边框，不包括外边距和滚动条部分
			③. clientTop 和 clientLeft 返回内边距的边缘和边框的外边缘之间的水平和垂直距离，也就是左，上边框宽度
			④. offsetTop 和 offsetLeft 表示该元素的左上角(边框外边缘)与已定位的父容器(offsetParent对象)左上角的距离
			⑤. offsetParent 对象是指元素最近的定位(relative,absolute)祖先元素,递归上溯,如果没有祖先元素是定位的话,会返回null
			⑥. scrollWidth 和 scrollHeight 是元素的内容区域加上内边距加上溢出尺寸，当内容正好和内容区域匹配没有溢出时，
				这些属性与clientWidth和clientHeight相等
			⑦. scrollLeft 和 scrollTop 是指元素滚动条位置，它们是可写的
	2.3.如何计算:
			<div id="divParent" style="padding: 8px; background-color: #aaa; position: relative;">
		        <div id="divDisplay" style="background-color: #0f0; margin: 30px; padding: 10px;
		            height: 200px; width: 200px; border: solid 3px #f00;">
		        </div>
			</div>
		clientHeight就是div的高度+上下各10px的padding，clientWidth同理
		clientLeft和ClientTop即为div左、上边框宽度
		offsetHeight是clientHeight+上下个3px的边框宽度之和，offsetWidth同理
		offsetTop是div 30px的 maggin+offsetparent 8px的 padding，offsetLeft同理
	2.3.scrollWidth 和 scrollHeight 与 scrollLeft 和 scrollTop:
			<div id="divParent" style="background-color: #aaa; padding-top:8px; padding-bottom:10px;border:solid 7px #000; height:200px; width:500px; overflow:auto;">
		        <div id="divDisplay" style="background-color: #0f0; margin: 30px; padding: 10px;
		            height: 400px; width: 200px; border: solid 3px #f00;">
		        </div>
		    </div>
		   Chrome,Opera:
		   		scrollHeight: 504
				scrollWidth: 483
			IE10,FireFox:
				scrollHeight: 494
				scrollWidth: 483
		(1).在FireFox和IE10(IE10以下版本盒模型和W3C标准不一致)下得到结果scrollHeight: 494，
			而在Chrome和Safari下得到结果scrollHeight: 502,差了8px，根据8可以简单推测是divParent的padding
		(2).scrollHeight 如何计算得来:
			scrollHeight肯定包含了divDisplay所需的高度:
				400px的高度+上下各10px的padding+上下各3px的border+上下各30px的margin这样
				400+10*2+3*2+30*2=486
			这样 486+8=494， 486+8*2=502果真是这样，在FireFox和IE10下没有计算下padding(应该是没有计算padding-bottom的值)
	2.4.相对于文档与视口的坐标:计算一个DOM元素位置也就是坐标的时候,会涉及到两种坐标系:文档坐标和视口坐标
		2.4.1.文档坐标与视口坐标:
			(1).document就是整个页面部分,而不仅仅是窗口可见部分,还包括因为窗口大小限制而出现滚动条的部分,
				它的左上角就是我们所谓相对于文档坐标的原点
			(2).视口是显示文档内容的浏览器的一部分，它不包括浏览器外壳(菜单，工具栏，状态栏等)也就是当前窗口显示页面部分,
				不包括滚动条
			(3).如果文档比视口小,说明没有出现滚动,文档左上角和视口左上角相同,一般来讲在两种坐标系之间进行切换,
				需要加上或减去滚动的偏移量(scroll offset)
		2.4.2.坐标系转换:坐标系之间进行转换,需要判定浏览器窗口的滚动条位置
			window 对象的pageXoffset和pageYoffset提供这些值，IE 8及更早版本除外;也可以通过scrollLeft和scrollTop属性
			获得滚动条位置，正常情况下通过查询文档根节点(document.documentElement)来获得这些属性值,
			但在怪异模式下必须通过文档的 body 上查询
				function getScrollOffsets(w) {
		            var w = w || window;
		            if (w.pageXoffset != null) {
		                return { x: w.pageXoffset, y: pageYoffset };
		            }
		            var d = w.document;
		            if (document.compatMode == "CSS1Compat")
		                return { x: d.documentElement.scrollLeft, y: d.documentElement.scrollTop };
		            return { x: d.body.scrollLeft, y: d.body.scrollTop };
		        }
		       function getViewPortSize(w) {
                   var w = w || window;
                   if (w.innerWidth != null)
                       return { w: w.innerWidth, h: w.innerHeight };
                   var d = w.document;
                   if (document.compatMode == "CSS1Compat")
                       return { w: d.documentElement.clientWidth, h: d.documentElement.clientHeight };
                   return { w: d.body.clientWidth, h: d.body.clientHeight };
               }
        2.4.3.文档坐标:任何HTML元素都拥有offectLeft和offectTop属性返回元素的X和Y坐标,对于很多元素,这些值是文档坐标
        	  但是对于以定位元素后代及一些其他元素(表格单元),返回相对于祖先的坐标;
	        	  	function getElementPosition(e) {
			            var x = 0, y = 0;
			            while (e != null) {
			                x += e.offsetLeft;
			                y += e.offsetTop;
			                e = e.offsetParent;
			            }
			            return { x: x, y: y };
		          	}
		        个函数也不总是计算正确的值，当文档中含有滚动条的时候这个方法就不能正常工作了
		2.4.5.视口坐标:
			通过调用元素的getBoundingClientRect方法。方法返回一个有left、right、top、bottom属性的对象,
			分别表示元素四个位置的相对于视口的坐标。
			getBoundingClientRect所返回的坐标包含元素的内边距和边框，不包含外边距。兼容性很好，非常好用


























































