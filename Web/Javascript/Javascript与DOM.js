节点层次:
	(1).Document:
		nodeType : 9
		nodeName : #document
		nodeValue : null
		parentNode : null
		子节点可能是一个DocumentType(最多一个),Element(最多一个)
		Document.documentElement ==> HTML页面的<html>元素
		Document.body ==> <body>元素
		Document.doctype ==> 子节点DocumentType,不同浏览器支持不一样
		document.title ==> 文档标题
		document.URL ==> 地址栏中显示的URL
		document.domain ==> 包含页面的域名
		document.referrer ==> 保存这链接到当前页面的那个页面URL
		document.anchors ==> 包含文档中所有带name特性的<a>元素
		document.forms ==> 包含文档中所有的<form>元素
		document.images ==> 包含文档中所有的<img>元素
		
	(2).Element:
		nodeType : 1
		nodeName : 元素的标签名(tagName)
		nodeValue : null
		parentNode : Document或Element
		
	(3).Text
		nodeType : 3
		nodeName : #text
		nodeValue : 节点所包含的文本
		parentNode : Element	
	
	(4).Comment:
		nodeType : 8
		nodeName : #comment
		nodeValue : 注视的内容
		parentNode : Document或Element
	
	(5).DocumentType:不常用,仅Firefox,Safari,Opera和Chrome支持
		nodeType : 10
		nodeName : doctype的名称
		nodeValue : null
		parentNode : Document
*****************************************************************************************************************************
一.DOM 需要注意的点:
1. window 对象作为全局对象，也就是说你可以通过window来访问全局对象。
	(1).属性在对象下面以变量的形式存放，在页面上创建的所有全局对象都会变成window对象的属性。
	(2).方法在对象下面以函数的形式存放，因为左右的函数都存放在window对象下面，所以他们也可以称为方法。
2. DOM为 web 文档创建带有层级的结果,这些层级是通过node节点组成,这里有几种DOM node类型,最重要的是 Element, Text, Document.
	(1).Element节点在页面里展示的是一个元素，所以如果你有段落元素(<p>)，你可以通过这个DOM节点来访问。
	(2).Text节点在页面里展示的所有文本相关的元素，所以如果你的段落有文本在里面的话，你可以直接通过DOM的Text节点来访问这个文本
	(3).Document节点代表是整个文档，它是DOM的根节点。
3. 每个引擎对DOM标准的实现有一些轻微的不同。
	例如，Firefox浏览器使用的Gecko引擎有着很好的实现(尽管没有完全遵守W3C规范),
	但IE浏览器使用的Trident引擎的实现却不完整而且还有bug，给开发人言带来了很多问题
4.DOM 节点查询:
	Node.childNodes: 访问一个单元素下所有的直接子节点元素，可以是一个可循环的类数组对象。
		该节点集合可以保护不同的类型的子节点（比如text节点或其他元素节点）。
	Node.firstChild: 与‘childNodes’数组的第一个项(‘Element.childNodes[0]‘)是同样的效果，仅仅是快捷方式。
	Node.lastChild: 与‘childNodes’数组的最后一个项(‘Element.childNodes[Element.childNodes.length-1]‘)是同样的效果
	Node.parentNode: 访问当前节点的父节点，父节点只能有一个，祖节点可以用‘Node.parentNode.parentNode’的形式来访问。
	Node.nextSibling: 访问DOM树上与当前节点同级别的下一个节点。
	Node.previousSibling: 访问DOM树上与当前节点同级别的上一个节点。
5.nodeType:1是元素，2是属性，3是text节点




































































