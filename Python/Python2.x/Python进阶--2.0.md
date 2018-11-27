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
		
2.高阶函数:能够接受函数作为参数的函数
	1.函数名也是变量
		# 由于abs函数实际上是定义在__builtin__模块中的，
		# 所以要让修改abs变量的指向在其它模块也生效，要用__builtin__.abs = 10
	
3.python常见内置高阶函数
	3.1.map()函数:是 Python 内置的高阶函数,它接收一个函数 f 和一个 list,
		并通过把函数 f 依次作用在 list 的每个元素上,得到一个新的 list 并返回;
		# map()函数不改变原有的 list，而是返回一个新的 list。
		# E.G.
			假设用户输入的英文名字不规范，没有按照首字母大写，后续字母小写的规则，请利用map()函数，
			把一个list（包含若干不规范的英文名字）变成一个包含规范英文名字的list：
			输入：['adam', 'LISA', 'barT']
			输出：['Adam', 'Lisa', 'Bart']
		# code:
			def format_name(s):
				return s[0].upper() + s[1:].lower()
			print map(format_name, ['adam', 'LISA', 'barT'])
			
	3.2.reduce()函数:reduce()函数接收的参数和 map()类似，一个函数 f，一个list，但行为和 map()不同，
		reduce()传入的函数 f 必须接收两个参数，reduce()对list的每个元素反复调用函数f，并返回最终结果值;
		reduce把结果继续和序列的下一个元素做累积计算,效果如下:
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
			
	3.3.filter()函数:用于过滤序列
		接收一个函数 f 和一个list，这个函数 f 的作用是对每个元素进行判断，返回 True或 False，
		filter()根据判断结果自动过滤掉不符合条件的元素，返回由符合条件元素组成的新list
		E.G.
			请利用filter()过滤出1~100中平方根是整数的数，即结果应该是：
			import math
			def is_sqr(x):
				r = int(math.sqrt(x))
				return r*r==x
			print filter(is_sqr, range(1, 101))
			
4.自定义排序函数
	(1).Python内置的 sorted()函数可对list进行排序：
	sorted()也是一个高阶函数，它可以接收一个比较函数来实现自定义排序，比较函数的定义是:
	传入两个待比较的元素 x, y，如果 x 应该排在 y 的前面，返回 -1，如果 x 应该排在 y 的后面，
	返回 1。如果 x 和 y 相等，返回 0
	# E.G.:
		def cmp_ignore_case(s1, s2):
			u1 = s1[0].upper()
			u2 = s2[0].upper()
			if u1 > u2:
				return 1
			elif u1 < u2 :
				return -1
			else:
				return 0
		print sorted(['bob', 'about', 'Zoo', 'Credit'], cmp_ignore_case)
		
5.返回函数:Python的函数不但可以返回int、str、list、dict等数据类型，还可以返回函数
	def f():
		print 'Call f()....'
		def g():
			print 'Call g()....'
		return g
	x = f()
	print x
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
		# 请再注意一点，当我们调用calc_sum()时，每次调用都会返回一个新的函数，即使传入相同的参数：
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
	====> 返回函数不要引用任何循环变量，或者后续会发生变化的变量
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
			原因就是当count()函数返回了3个函数时，这3个函数所引用的变量 i 的值已经变成了3。由于f1、f2、f3并没有被调用，所以，此时他们并未计算 i*i，当 f1 被调用时：
			>>> f1()
			9     # 因为f1现在才计算i*i，但现在i的值已经变为3
		# 如果一定要引用循环变量怎么办？方法是再创建一个函数，用该函数的参数绑定循环变量当前的值，
		# 无论该循环变量后续如何更改，已绑定到函数参数的值不变
		*********改造上述代码,使之正确输出1 4 9******
		def count():
			fs = []
			for i in range(1, 4):
				def f(j):
					def g():
						return j*j
					return g
				r = f(i)
				fs.append(r)
			return fs
		f1, f2, f3 = count()
		print f1(), f2(), f3()

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
		def now()
			print '2015-05-04'
	# 函数对象有一个__name__属性，可以拿到函数的名字：
	(1).如果要增强now()函数的功能，比如，在函数调用前后自动打印日志，但又不希望修改now()函数的定义，
	这种在代码运行期间动态增加功能的方式，称之为“装饰器”（Decorator）	
	(2).本质上，decorator就是一个返回函数的高阶函数:
		# 所以，我们要定义一个能打印日志的decorator，可以定义如下：
		def log(func):
			def wrapper(*args, **kw):
				print 'call %s():' % func.__name__
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
					print '%s %s():' % (text, func.__name__)
					return func(*args, **kw)
				return wrapper
			return decorator
		# 调用:
		@log('execute')
		def now():
			print '2013-12-25'
		#和两层嵌套的decorator相比，3层嵌套的效果是这样的：
		 >>> now = log('execute')(now)
	(4).调用之后调用 now.__name__ ===> wrapper
		需要把原始函数的__name__等属性复制到wrapper()函数中，否则，有些依赖函数签名的代码执行就会出错。
			import functools
			def log(func):
				@functools.wraps(func)
				def wrapper(*args, **kw):
					print 'call %s():' % func.__name__
					return func(*args, **kw)
				return wrapper
			# 或者针对带参数的decorator：
			import functools
			def log(text):
				def decorator(func):
					@functools.wraps(func)
					def wrapper(*args, **kw):
						print '%s %s():' % (text, func.__name__)
						return func(*args, **kw)
					return wrapper
				return decorator
		
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
	(2).最后，创建偏函数时，实际上可以接收函数对象、*args和**kw这3个参数，当传入：	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	