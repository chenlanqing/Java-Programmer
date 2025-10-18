# 一、函数式编程

## 1、函数与函数式

- 函数：function
- 函数式：functional，是一种编程范式，是一种抽象计算的编程模式；
- 函数式编程特点：
	- 把计算作为函数而非指令；
	- 纯函数式编程:不需要变量，没有副作用，测试简单；
	- 支持高阶函数，代码简洁

- python支持的函数式编程
	- 不是纯函数式编程：允许有变量；
	- 支持高阶函数：函数也可以作为变量导入；
	- 支持闭包：有了闭包就能返回函数；
	- 有限度的支持匿名函数
		
## 2、高阶函数

能够接受函数作为参数的函数 ==> 函数式编程就是指这种高度抽象的编程范式

- 变量可以指向函数
	```
	f = abs
	f(-10) ==> 10
	```
- 函数名也是变量
	- 由于abs函数实际上是定义在__builtin__模块中的，
	- 所以要让修改abs变量的指向在其它模块也生效，要用`__builtin__.abs = 10`
- 传入函数

## 3、python常见内置高阶函数

### 3.1、map()函数

是 Python 内置的高阶函数，它接收一个函数 f 和一个 list，并通过把函数 f 依次作用在 list 的每个元素上，得到一个新的 list 并返回；`map()`函数不改变原有的 list，而是返回一个新的 list。

假设用户输入的英文名字不规范，没有按照首字母大写，后续字母小写的规则，请利用map()函数，把一个 list（包含若干不规范的英文名字）变成一个包含规范英文名字的list：
```python
# 输入：['adam'， 'LISA'， 'barT']
# 输出：['Adam'， 'Lisa'， 'Bart']
def format_name(s):
	return s[0].upper() + s[1:].lower()
print(map(format_name， ['adam'， 'LISA'， 'barT']))
```		

### 3.2、reduce()函数

`reduce()`函数接收的参数和 `map()`类似，一个函数 f，一个 list，但行为和 map()不同，

`reduce()`传入的函数 f 必须接收两个参数，`reduce()`对list的每个元素反复调用函数f，并返回最终结果值；reduce 把结果继续和序列的下一个元素做累积计算，效果如下:

`reduce(f， [x1， x2， x3， x4]) = f(f(f(x1， x2)， x3)， x4)`

reduce()还可以接收第3个可选参数，作为计算的初始值
```python
def f(x， y):
	return x + y
reduce(f， [1， 3， 5， 7， 9])
```
- 先计算头两个元素：f(1， 3)，结果为4；
- 再把结果和第3个元素计算：f(4， 5)，结果为9；
- 再把结果和第4个元素计算：f(9， 7)，结果为16；
- 再把结果和第5个元素计算：f(16， 9)，结果为25；
- 由于没有更多的元素了，计算结束，返回结果25。

`reduce(f， [1， 3， 5， 7， 9]，100)` 第一轮计算是计算初始值和第一个元素：f(100， 1)，结果为101。最早结果:125
```python	
# 例子:利用map和reduce编写一个str2float函数，把字符串'123.456'转换成浮点数123.456
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from functools import reduce
def char2num(s):
	return {'0': 0， '1': 1， '2': 2， '3': 3， '4': 4， '5': 5， '6': 6， '7': 7， '8': 8， '9': 9}[s]
def str2float(s):
	if '.' in s:
		s1 = s.split(".")
		sInt = reduce(lambda x，y : x * 10 + y， map(char2num，s1[0]))
		sFloat = reduce(lambda x，y : x * 10 + y， map(char2num，s1[1])) * 0.1 ** len(s1[1])
		return sInt + sFloat
	else :
		return int(s)			
print(str2float('123.456'))
```

### 3.3、filter()函数
用于过滤序列

- 接收一个函数 f 和一个list，这个函数 f 的作用是对每个元素进行判断，返回 True或 False，filter()根据判断结果自动过滤掉不符合条件的元素，返回由符合条件元素组成的新list
	```python
	#请利用filter()过滤出1~100中平方根是整数的数，即结果应该是：
	import math
	def is_sqr(x):
		r = int(math.sqrt(x))
		return r*r==x
	print(list(filter(is_sqr， range(1， 101))))
	```

- filter:关键在于正确筛选：filter()函数返回的是一个Iterator，也就是一个惰性序列，所以要强迫filter()完成计算结果，需要用list()函数获得所有结果并返回list
- 用filter求素数
	```python
	# 计算素数的一个方法是埃氏筛法，它的算法理解起来非常简单：
	# （1）首先，列出从2开始的所有自然数，构造一个序列：
		2， 3， 4， 5， 6， 7， 8， 9， 10， 11， 12， 13， 14， 15， 16， 17， 18， 19， 20， ...
	# （2）取序列的第一个数2，它一定是素数，然后用2把序列的2的倍数筛掉：
		3， 4， 5， 6， 7， 8， 9， 10， 11， 12， 13， 14， 15， 16， 17， 18， 19， 20， ...
	# （3）取新序列的第一个数3，它一定是素数，然后用3把序列的3的倍数筛掉：
		5， 6， 7， 8， 9， 10， 11， 12， 13， 14， 15， 16， 17， 18， 19， 20， ...
	# （4）取新序列的第一个数5，然后用5把序列的5的倍数筛掉：
		7， 8， 9， 10， 11， 12， 13， 14， 15， 16， 17， 18， 19， 20， ...
	# 不断筛下去，就可以得到所有的素数。
	# 用Python来实现这个算法，可以先构造一个从3开始的奇数序列：
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
			it = filter(_not_divisable(n)， it) # 构造新序列
	for n in primes():
		if n < 100:
			print(n)
		else:
			break
	```

## 4、自定义排序函数

- Python内置的 sorted()函数可对list进行排序：sorted()也是一个高阶函数，它可以接收一个比较函数来实现自定义排序，比较函数的定义是；传入两个待比较的元素 x， y，通常规定:
	- 如果认为x < y，则返回-1，
	- 如果认为x == y，则返回0，
	- 如果认为x > y，则返回1	

- sorted()函数也是一个高阶函数，它还可以接收一个key函数来实现自定义的排序；key指定的函数将作用于list的每一个元素上，并根据key函数返回的结果进行排序
	```python
	>>> sorted([36， 5， -12， 9， -21]， key=abs)
		[5， 9， -12， -21， 36]
	```
- 默认情况下，对字符串排序，是按照ASCII的大小比较的，由于'Z' < 'a'，结果，大写字母Z会排在小写字母a的前面:
	```python
	E.G.看一个字符串排序的例子
	>>> sorted(['bob'， 'about'， 'Zoo'， 'Credit'])
	==> ['Credit'， 'Zoo'， 'about'， 'bob'] # 默认情况下，对字符串排序，是按照ASCII的大小比较的
	#如果排序应该忽略大小写，按照字母序排序
	>>> sorted(['bob'， 'about'， 'Zoo'， 'Credit']， key=str.lower) 
	==> ['about'， 'bob'， 'Credit'， 'Zoo']
	# 要进行反向排序，不必改动key函数，可以传入第三个参数reverse=True
	>>> sorted(['bob'， 'about'， 'Zoo'， 'Credit']， key=str.lower， reverse=True)
	==> ['Zoo'， 'Credit'， 'bob'， 'about']
	```	
## 5、返回函
:Python的函数不但可以返回int、str、list、dict等数据类型，还可以返回函数
```python
def f():
	print('Call f()....')
	def g():
		print('Call g()....')
	return g
x = f()
print(x)
x()
```
- 请注意区分返回函数和返回值:
	```python
	def myabs():
		return abs   # 返回函数
	def myabs2(x):
		return abs(x)   # 返回函数调用的结果，返回值是一个数值
	```
- 返回函数可以把一些计算延迟执行：例如，如果定义一个普通的求和函数：
	```python
	def calc_sum(lst):
		return sum(lst)
	# 调用 calc_sum()将立刻计算并得到结果
	# 如果返回一个函数，就可以“延迟计算”：			
		def calc_sum(lst):
			def lazy_sum():
				return sum(lst)
			return lazy_sum
	# 调用calc_sum()并没有计算出结果，而是返回函数:
		>>> f = calc_sum([1， 2， 3， 4])
		>>> f
		<function lazy_sum at 0x1037bfaa0>
	# 对返回的函数进行调用时，才计算出结果:
		>>> f()
		10
	# 注意:请再注意一点，当我们调用calc_sum()时，每次调用都会返回一个新的函数，即使传入相同的参数：
		>>> f1 = calc_sum(1， 3， 5， 7， 9)
		>>> f2 = calc_sum(1， 3， 5， 7， 9)
		>>> f1==f2
		False
	```

## 7、匿名函数

如：`map(lambda x: x * x， [1， 2， 3， 4， 5， 6， 7， 8， 9])`
关键字 lambda 表示匿名函数，冒号前面的 x 表示函数参数。

匿名函数有个限制，就是只能有一个表达式，不写return，返回值就是该表达式的结果

- 匿名函数也是一个函数对象，也可以把匿名函数赋值给一个变量，再利用变量来调用该函数：
	```
	>>> f = lambda x : x * x
	>>> f
	<function <lambda> at 0x021CB930>
	>>> f(5)
	25
	```python

- 也可以把匿名函数作为返回值返回:
	```
	>>> def build(x，y):
	...     return lambda : x * x + y * y
	```


## 8、偏函数(Partial function)

在实践中，如果想减少函数的参数数以简化函数的签名，就会使用偏函数。

如果需要创建偏函数，Python 提供了 functools 标准模块中的偏函数，可以更轻松地定义偏函数。

简单总结`functools.partial`的作用就是：把一个函数的某些参数给固定住（也就是设置默认值），返回一个新的函数，调用这个新函数会更简单。基础语法：
```py
functools.partial(fn, /, *args, **kwargs)
```
该偏函数返回的是可以调用的partial对象，调用partial对象时，Python 会使用位置参数 args 和关键字参数 kwargs 调用 fn 函数。

比如:int()函数提供额外的base参数，默认值为10，如果需要大量转换二进制，可以再定义一个函数:
```py
from functools import partial
def multiply(a, b):
    return a*b

double = partial(multiply, b=2)
result = double(10)
print(result)
```

偏函数与变量：
```py
from functools import partial
def multiply(a, b):
    return a*b
x = 2
f = partial(multiply, x)
result = f(10)  # 20
print(result)
x = 3
result = f(10)  # 20
print(result)
```
因为此时偏函数没有改变过，所以里面x仍然是2；也就说`f = partial(multiply, x)`，在这里已经计算过x了，在后面改成x=3，但是偏函数没有改变；

最后，创建偏函数时，实际上可以接收函数对象、`*args`和`**kw`这3个参数，当传入：
```
int2 = functools.partial(int， base=2)
实际上固定了int()函数的关键字参数base，也就是：
	int2('10010') 
等价于:
	kw = { 'base': 2 }
	int('10010'， **kw)
当传入：
	max2 = functools.partial(max， 10)
实际上会把10作为*args的一部分自动加到左边，也就是：
	max2(5， 6， 7)
相当于：	
	args = (10， 5， 6， 7)
	max(*args)   ===>	10
```


