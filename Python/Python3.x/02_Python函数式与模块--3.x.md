一.函数式编程:
1.函数与函数式:
	1.1.函数:function
	1.2.函数式:functional,是一种编程范式,是一种抽象计算的编程模式;
	1.3.函数式编程特点:
		(1).把计算作为函数而非指令;
		(2).纯函数式编程:不需要变量,没有副作用,测试简单;
		(3).支持高阶函数,代码简洁:
	1.4.python支持的函数式编程:
		(1).不是纯函数式编程:允许有变量;
		(2).支持高阶函数:函数也可以作为变量导入;
		(3).支持闭包:有了闭包就能返回函数;
		(4).有限度的支持匿名函数
		
2.高阶函数:能够接受函数作为参数的函数 ==> 函数式编程就是指这种高度抽象的编程范式
	(1).变量可以指向函数:
		f = abs
		f(-10) ==> 10
	(2).函数名也是变量,
		# 由于abs函数实际上是定义在__builtin__模块中的，
		# 所以要让修改abs变量的指向在其它模块也生效，要用__builtin__.abs = 10
	(3).传入函数:
	
3.python常见内置高阶函数
	3.1.map()函数:是 Python 内置的高阶函数,它接收一个函数 f 和一个 list,
		并通过把函数 f 依次作用在 list 的每个元素上,得到一个新的 list 并返回;
		# map()函数不改变原有的 list，而是返回一个新的 list。
		# E.G.
			假设用户输入的英文名字不规范，没有按照首字母大写，后续字母小写的规则，请利用map()函数，
			把一个 list（包含若干不规范的英文名字）变成一个包含规范英文名字的list：
			输入：['adam', 'LISA', 'barT']
			输出：['Adam', 'Lisa', 'Bart']
		# code:
			def format_name(s):
				return s[0].upper() + s[1:].lower()
			print(map(format_name, ['adam', 'LISA', 'barT']))
			
	3.2.reduce()函数:reduce()函数接收的参数和 map()类似，一个函数 f，一个 list，但行为和 map()不同，
		reduce()传入的函数 f 必须接收两个参数，reduce()对list的每个元素反复调用函数f，并返回最终结果值;
		reduce 把结果继续和序列的下一个元素做累积计算,效果如下:
		==> reduce(f, [x1, x2, x3, x4]) = f(f(f(x1, x2), x3), x4)
		# reduce()还可以接收第3个可选参数，作为计算的初始值;
		E.G.
			def f(x, y):
				return x + y
			reduce(f, [1, 3, 5, 7, 9])
			===> 先计算头两个元素：f(1, 3)，结果为4；
			===> 再把结果和第3个元素计算：f(4, 5)，结果为9；
			===> 再把结果和第4个元素计算：f(9, 7)，结果为16；
			===> 再把结果和第5个元素计算：f(16, 9)，结果为25；
			===> 由于没有更多的元素了，计算结束，返回结果25。
			
			reduce(f, [1, 3, 5, 7, 9],100) ===> 第一轮计算是计算初始值和第一个元素：f(100, 1)，结果为101。
			===> 最早结果:125
		
		# 例子:利用map和reduce编写一个str2float函数，把字符串'123.456'转换成浮点数123.456
		#!/usr/bin/env python3
		# -*- coding: utf-8 -*-
		from functools import reduce
		def char2num(s):
			return {'0': 0, '1': 1, '2': 2, '3': 3, '4': 4, '5': 5, '6': 6, '7': 7, '8': 8, '9': 9}[s]
		def str2float(s):
			if '.' in s:
				s1 = s.split(".")
				sInt = reduce(lambda x,y : x * 10 + y, map(char2num,s1[0]))
				sFloat = reduce(lambda x,y : x * 10 + y, map(char2num,s1[1])) * 0.1 ** len(s1[1])
				return sInt + sFloat
			else :
				return int(s)			
		print(str2float('123.456'))
		
	3.3.filter()函数:用于过滤序列
		(1).接收一个函数 f 和一个list，这个函数 f 的作用是对每个元素进行判断，返回 True或 False，
		filter()根据判断结果自动过滤掉不符合条件的元素，返回由符合条件元素组成的新list
		E.G.
			请利用filter()过滤出1~100中平方根是整数的数，即结果应该是：
			import math
			def is_sqr(x):
				r = int(math.sqrt(x))
				return r*r==x
			print(list(filter(is_sqr, range(1, 101))))
		(2).filter:关键在于正确筛选:
			filter()函数返回的是一个Iterator,也就是一个惰性序列,所以要强迫filter()完成计算结果,
			需要用list()函数获得所有结果并返回list
		(3).用filter求素数:
			计算素数的一个方法是埃氏筛法，它的算法理解起来非常简单：
			=>首先，列出从2开始的所有自然数，构造一个序列：
				2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...
			=>取序列的第一个数2，它一定是素数，然后用2把序列的2的倍数筛掉：
				3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...
			=>取新序列的第一个数3，它一定是素数，然后用3把序列的3的倍数筛掉：
				5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...
			=>取新序列的第一个数5，然后用5把序列的5的倍数筛掉：
				7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, ...
			=>不断筛下去，就可以得到所有的素数。
			=>用Python来实现这个算法，可以先构造一个从3开始的奇数序列：
			# 从 3 开始构造序列生成器
			def _odd_filter():
			    n = 1
			    while True:
			        n = n + 2
			        yield n
			def _not_divisable(n):
			    return lambda x: x % n >0
			def primes():
			    yield 2
			    it = _odd_filter() # 初始化序列
			    while True:
			        n = next(it) # 获取序列的第一个数
			        yield n 
			        it = filter(_not_divisable(n), it) # 构造新序列
			for n in primes():
			    if n < 100:
			        print(n)
			    else:
			        break

4.自定义排序函数
	(1).Python内置的 sorted()函数可对list进行排序：
	sorted()也是一个高阶函数，它可以接收一个比较函数来实现自定义排序，比较函数的定义是:
	传入两个待比较的元素 x, y,通常规定:
		如果认为x < y，则返回-1，
		如果认为x == y，则返回0,
		如果认为x > y，则返回1	
	(2).sorted()函数也是一个高阶函数，它还可以接收一个key函数来实现自定义的排序;
		key指定的函数将作用于list的每一个元素上，并根据key函数返回的结果进行排序
		>>> sorted([36, 5, -12, 9, -21], key=abs)
			[5, 9, -12, -21, 36]
	(3).默认情况下，对字符串排序，是按照ASCII的大小比较的，由于'Z' < 'a'，结果，大写字母Z会排在小写字母a的前面:
		E.G.看一个字符串排序的例子
		>>> sorted(['bob', 'about', 'Zoo', 'Credit'])
		==> ['Credit', 'Zoo', 'about', 'bob'] # 默认情况下，对字符串排序，是按照ASCII的大小比较的
		#如果排序应该忽略大小写，按照字母序排序
		>>> sorted(['bob', 'about', 'Zoo', 'Credit'], key=str.lower) 
		==> ['about', 'bob', 'Credit', 'Zoo']
		# 要进行反向排序，不必改动key函数，可以传入第三个参数reverse=True
		>>> sorted(['bob', 'about', 'Zoo', 'Credit'], key=str.lower, reverse=True)
		==> ['Zoo', 'Credit', 'bob', 'about']
		
5.返回函数:Python的函数不但可以返回int、str、list、dict等数据类型，还可以返回函数
	def f():
		print('Call f()....')
		def g():
			print('Call g()....')
		return g
	x = f()
	print(x)
	x()
	(1).请注意区分返回函数和返回值:
		def myabs():
			return abs   # 返回函数
		def myabs2(x):
			return abs(x)   # 返回函数调用的结果，返回值是一个数值
	(2).返回函数可以把一些计算延迟执行。
		例如，如果定义一个普通的求和函数：
			def calc_sum(lst):
				return sum(lst)
		调用 calc_sum()将立刻计算并得到结果
		# 如果返回一个函数，就可以“延迟计算”：			
			def calc_sum(lst):
				def lazy_sum():
					return sum(lst)
				return lazy_sum
		# 调用calc_sum()并没有计算出结果，而是返回函数:
			>>> f = calc_sum([1, 2, 3, 4])
			>>> f
			<function lazy_sum at 0x1037bfaa0>
		# 对返回的函数进行调用时，才计算出结果:
			>>> f()
			10
		# 注意:请再注意一点，当我们调用calc_sum()时，每次调用都会返回一个新的函数，即使传入相同的参数：
			>>> f1 = calc_sum(1, 3, 5, 7, 9)
			>>> f2 = calc_sum(1, 3, 5, 7, 9)
			>>> f1==f2
			False
	
6.闭包:内层函数引用了外层函数的变量（参数也算变量），然后返回内层函数的情况，称为闭包（Closure）:
	例如:
	def calc_sum(lst):
		def lazy_sum():
			return sum(lst)
		return lazy_sum
	6.1.闭包的特点:返回的函数还引用了外层函数的局部变量,所以:要正确使用闭包,就要确保引用的局部变量在函数返回后不能变:
	====> 返回闭包时牢记的一点就是：返回函数不要引用任何循环变量，或者后续会发生变化的变量
		# 希望一次返回3个函数，分别计算1x1,2x2,3x3:
			def count():
				fs = []
				for i in range(1, 4):
					def f():
						 return i*i
					fs.append(f)
				return fs

			f1, f2, f3 = count()
			你可能认为调用f1()，f2()和f3()结果应该是1，4，9，但实际结果全部都是 9（请自己动手验证）。
			原因就是当count()函数返回了3个函数时，这3个函数所引用的变量 i 的值已经变成了3。由于f1、f2、f3并没有被调用，
			所以，此时他们并未计算 i*i，当 f1 被调用时：
			>>> f1()
			9     
			# 因为f1现在才计算i*i，但现在i的值已经变为3
			# 如果一定要引用循环变量怎么办？方法是再创建一个函数，用该函数的参数绑定循环变量当前的值，
			# 无论该循环变量后续如何更改，已绑定到函数参数的值不变
		*********改造上述代码,使之正确输出1 4 9******
		def count():
			fs = []
			for i in range(1, 4):
				def f(j):
					'''
					def g():
						return j*j
					return g
					'''
					return lambda : j * j
				r = f(i)
				fs.append(r)
			return fs
		f1, f2, f3 = count()
		print(f1(), f2(), f3())
		# 返回函数中不要引用任何可能会变化的变量
		
7.匿名函数:
	如: map(lambda x: x * x, [1, 2, 3, 4, 5, 6, 7, 8, 9])
	# 关键字 lambda 表示匿名函数，冒号前面的 x 表示函数参数。
	# 匿名函数有个限制，就是只能有一个表达式，不写return，返回值就是该表达式的结果
	(1).匿名函数也是一个函数对象，也可以把匿名函数赋值给一个变量，再利用变量来调用该函数：
		>>> f = lambda x : x * x
		>>> f
		<function <lambda> at 0x021CB930>
		>>> f(5)
		25
	(2).也可以把匿名函数作为返回值返回:
		>>> def build(x,y):
		...     return lambda : x * x + y * y
		
8.decorator(装饰器):在代码运行期间动态增加功能的方式,称之为装饰器
	如:
		def now():
			print('2015-05-04')
		# 函数对象有一个__name__属性，可以拿到函数的名字：
	(1).如果要增强now()函数的功能，比如，在函数调用前后自动打印日志，但又不希望修改now()函数的定义，
	这种在代码运行期间动态增加功能的方式，称之为“装饰器”（Decorator）	
	(2).本质上，decorator就是一个返回函数的高阶函数:
		# 所以，我们要定义一个能打印日志的decorator，可以定义如下：
		def log(func):
			def wrapper(*args, **kw):
				print('call %s():' % func.__name__)
				return func(*args, **kw)
			return wrapper
		# 观察上面的log，因为它是一个decorator，所以接受一个函数作为参数，并返回一个函数。
		# 我们要借助Python的@语法，把decorator置于函数的定义处：	
			@log
			def now():
				print '2013-12-25'
		# 调用now()函数，不仅会运行now()函数本身，还会在运行now()函数前打印一行日志：
		# 把@log放到now()函数的定义处，相当于执行了语句：
			now = log(now)
	(3).如果decorator本身需要传入参数，那就需要编写一个返回decorator的高阶函数，写出来会更复杂:
		def log(text):
			def decorator(func):
				def wrapper(*args, **kw):
					print('%s %s():' % (text, func.__name__))
					return func(*args, **kw)
				return wrapper
			return decorator
		# 调用:
		@log('execute')
		def now():
			print('2013-12-25')
		#和两层嵌套的decorator相比，3层嵌套的效果是这样的：
		 >>> now = log('execute')(now)
	(4).调用之后调用 now.__name__ ===> wrapper
		需要把原始函数的__name__等属性复制到wrapper()函数中，否则，有些依赖函数签名的代码执行就会出错。
			import functools
			def log(func):
				@functools.wraps(func)
				def wrapper(*args, **kw):
					print('call %s():' % func.__name__)
					return func(*args, **kw)
				return wrapper
			# 或者针对带参数的decorator：
			import functools
			def log(text):
				def decorator(func):
					@functools.wraps(func)
					def wrapper(*args, **kw):
						print('%s %s():' % (text, func.__name__))
						return func(*args, **kw)
					return wrapper
				return decorator
	(5).请编写一个decorator，能在函数调用的前后打印出'begin call'和'end call'的日志
		# 大致思路是A装饰B,B装饰C，B是指定函数，A是执行前打印日志，B是执行后打印日志
		def forwardcall(func):
			def wrapper(*args, **kw):
				print('begin call');
				return func(*args, **kw);
			return wrapper
		@forwardcall
		def now(func):
			print('业务逻辑处理')
			def wrapper(*args, **kw):
				return func(*args, **kw)
		@now
		def endcall():
			print('end call')
		
9.偏函数(Partial functio):functools模块提供的功能
	(1).简单总结functools.partial的作用就是:
		把一个函数的某些参数给固定住（也就是设置默认值），返回一个新的函数，调用这个新函数会更简单
		==>比如:int()函数提供额外的base参数,默认值为10
		如果需要大量转换二进制,可以再定义一个函数:
			def int2(x, base = 2):
				return int(x,base)
		# functools.partial就是帮助我们创建一个偏函数的，不需要我们自己定义int2()，可以直接使用下面的代码创建一个新的函数int2
		import functools
		int2 = functools.partial(int, base=2)
		# 注意到上面的新的int2函数，仅仅是把base参数重新设定默认值为2，但也可以在函数调用时传入其他值：
	(2).最后，创建偏函数时，实际上可以接收函数对象、*args和**kw这3个参数，当传入：	
		int2 = functools.partial(int, base=2)
		实际上固定了int()函数的关键字参数base，也就是：
			int2('10010') 
		等价于:
			kw = { 'base': 2 }
			int('10010', **kw)
		当传入：
			max2 = functools.partial(max, 10)
		实际上会把10作为*args的一部分自动加到左边，也就是：
			max2(5, 6, 7)
		相当于：	
			args = (10, 5, 6, 7)
			max(*args)   ===>	10


二.模块:在Python中,一个.py文件就称之为一个模块(Module)
1.关于模块:
	(1).使用模块最大的好处是大大提高了代码的可维护性。其次，编写代码不必从零开始。当一个模块编写完毕，
	就可以被其他地方引用。我们在编写程序的时候，也经常引用其他模块，包括Python内置的模块和来自第三方的模块;
	使用模块还可以避免函数名和变量名冲突。相同名字的函数和变量完全可以分别存在不同的模块中	
	
	(2).为了避免模块名冲突，Python又引入了按目录来组织模块的方法，称为包（Package）:
		请注意，每一个包目录下面都会有一个__init__.py的文件，这个文件是必须存在的，
		否则，Python就把这个目录当成普通目录，而不是一个包。__init__.py可以是空文件，
		也可以有Python代码，因为__init__.py本身就是一个模块，而它的模块名就是mycompany
		#可以有多级目录，组成多级层次的包结构
		引入了包以后，只要顶层的包名不与别人冲突，那所有模块都不会与别人冲突。现在，abc.py模块的名字
		就变成了mycompany.abc，类似的，xyz.py的模块名变成了mycompany.xyz
		
2.使用模块:
	#!/usr/bin/env python3 ==> 标准注释,表示该文件可以之unix环境直接运行
	# -*- coding: utf-8 -*- ==>文件的编码格式
	'a test module' ==> 一个字符串，表示模块的文档注释，任何模块代码的第一个字符串都被视为模块的文档注释；
	__author__ = 'BlueFish' ==> 变量把作者写进去
	import sys  ==> 导入sys模块后，我们就有了变量sys指向该模块，利用sys这个变量，就可以访问sys模块的所有功能
	def test():
	    args = sys.argv
	    if len(args)==1:
	            print('Hello, world!')
	    elif len(args)==2:
	        print('Hello, %s!' % args[1])
	    else:
	        print('Too many arguments!')
	if __name__=='__main__':
		test()
	2.1.sys模块有一个argv变量，用list存储了命令行的所有参数.argv至少有一个元素.因为第一个参数永远是该.py文件的名称:
		运行python3 hello.py获得的sys.argv就是['hello.py']；
		运行python3 hello.py Michael获得的sys.argv就是['hello.py', 'Michael']。
	2.2.上述最后两行代码:
		当我们在命令行运行hello模块文件时，Python解释器把一个特殊变量__name__置为__main__，
		而如果在其他地方导入该hello模块时，if判断将失败，因此，这种if测试可以让一个模块通过命令行
		运行时执行一些额外的代码，最常见的就是运行测试
		
	2.3.作用域:在python中通过"_"_前缀来实现对作用域的控制:
		(1).__xxx__这样的变量是特殊变量,可以被直接引用,但是有特殊用途，比如上面的__author__，__name__就是特殊变量;
		(2)._xxx和__xxx这样的函数或变量就是非公开的（private），不应该被直接引用，比如_abc，__abc等
			# 注意:private函数和变量“不应该”被直接引用,而不是“不能”被直接引用,
			# 是因为Python并没有一种方法可以完全限制访问private函数或变量
		(3).外部不需要引用的函数全部定义成private,只有外部需要引用的函数才定义为public
3.安装第三方模块:在Python中，安装第三方模块，是通过包管理工具pip完成的
	3.1.一般来说,第三方库都会在Python官方的pypi.python.org网站注册,
		要安装一个第三方库,必须先知道该库的名称,可以在官网或者pypi上搜索
	3.2.如安装Pillow库:pip install Pillow	
		
	3.3.模块搜索路径:
		(1).当我们试图加载一个模块时，Python会在指定的路径下搜索对应的.py文件，如果找不到，就会报错
		(2).默认情况下,Python解释器会搜索当前目录、所有已安装的内置模块和第三方模块,搜索路径存放在sys模块的path变量中:
			sys.path
		(3).如果要添加自己的搜索目录:
			①.直接修改sys.path,添加要搜索的目录:
				sys.path.append('/Users/michael/my_py_scripts') # 这种方法是在运行时修改，运行结束后失效
			②.第二种方法是设置环境变量PYTHONPATH，该环境变量的内容会被自动添加到模块搜索路径中
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	