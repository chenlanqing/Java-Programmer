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
## 6、闭包
内层函数引用了外层函数的变量（参数也算变量），然后返回内层函数的情况，称为闭包（Closure）；	例如:
```python
def calc_sum(lst):
	def lazy_sum():
		return sum(lst)
	return lazy_sum
```
闭包的特点：返回的函数还引用了外层函数的局部变量，所以:要正确使用闭包，就要确保引用的局部变量在函数返回后不能变；返回闭包时牢记的一点就是：返回函数不要引用任何循环变量，或者后续会发生变化的变量

希望一次返回3个函数，分别计算1x1，2x2，3x3:
```python
def count():
	fs = []
	for i in range(1， 4):
		def f():
				return i*i
		fs.append(f)
	return fs

	f1， f2， f3 = count()
```
你可能认为调用f1()，f2()和f3()结果应该是1，4，9，但实际结果全部都是 9（请自己动手验证）。原因就是当count()函数返回了3个函数时，这3个函数所引用的变量 i 的值已经变成了3。由于f1、f2、f3并没有被调用，所以，此时他们并未计算 i*i，当 f1 被调用时：
```
>>> f1()
9 
```   
因为f1现在才计算i*i，但现在i的值已经变为3，如果一定要引用循环变量怎么办？方法是再创建一个函数，用该函数的参数绑定循环变量当前的值，无论该循环变量后续如何更改，已绑定到函数参数的值不变

```python
def count():
	fs = []
	for i in range(1， 4):
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
f1， f2， f3 = count()
print(f1()， f2()， f3())
# 返回函数中不要引用任何可能会变化的变量
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

## 8、decorator(装饰器)

在代码运行期间动态增加功能的方式，称之为装饰器如:
```python
def now():
	print('2015-05-04')
# 函数对象有一个__name__属性，可以拿到函数的名字：
```
- 如果要增强now()函数的功能，比如，在函数调用前后自动打印日志，但又不希望修改now()函数的定义，这种在代码运行期间动态增加功能的方式，称之为“装饰器”（Decorator）	
- 本质上，decorator就是一个返回函数的高阶函数:
	```python
	# 所以，我们要定义一个能打印日志的decorator，可以定义如下：
	def log(func):
		def wrapper(*args， **kw):
			print('call %s():' % func.__name__)
			return func(*args， **kw)
		return wrapper
	# 观察上面的log，因为它是一个decorator，所以接受一个函数作为参数，并返回一个函数。
	# 我们要借助Python的@语法，把decorator置于函数的定义处：	
		@log
		def now():
			print '2013-12-25'
	# 调用now()函数，不仅会运行now()函数本身，还会在运行now()函数前打印一行日志：
	# 把@log放到now()函数的定义处，相当于执行了语句：
		now = log(now)
	```
- 如果decorator本身需要传入参数，那就需要编写一个返回decorator的高阶函数，写出来会更复杂:
	```python
	def log(text):
		def decorator(func):
			def wrapper(*args， **kw):
				print('%s %s():' % (text， func.__name__))
				return func(*args， **kw)
			return wrapper
		return decorator
	# 调用:
	@log('execute')
	def now():
		print('2013-12-25')
	#和两层嵌套的decorator相比，3层嵌套的效果是这样的：
		>>> now = log('execute')(now)
	```
- 调用之后调用 `now.__name__ ===> wrapper`；需要把原始函数的`__name__`等属性复制到`wrapper()`函数中，否则，有些依赖函数签名的代码执行就会出错。
	```python
	import functools
	def log(func):
		@functools.wraps(func)
		def wrapper(*args， **kw):
			print('call %s():' % func.__name__)
			return func(*args， **kw)
		return wrapper
	# 或者针对带参数的decorator：
	import functools
	def log(text):
		def decorator(func):
			@functools.wraps(func)
			def wrapper(*args， **kw):
				print('%s %s():' % (text， func.__name__))
				return func(*args， **kw)
			return wrapper
		return decorator
	```

- 请编写一个decorator，能在函数调用的前后打印出'begin call'和'end call'的日志
	```python
	# 大致思路是A装饰B，B装饰C，B是指定函数，A是执行前打印日志，B是执行后打印日志
	def forwardcall(func):
		def wrapper(*args， **kw):
			print('begin call')；
			return func(*args， **kw)；
		return wrapper
	@forwardcall
	def now(func):
		print('业务逻辑处理')
		def wrapper(*args， **kw):
			return func(*args， **kw)
	@now
	def endcall():
		print('end call')
	```

## 9、偏函数(Partial function)

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

# 二、模块

在Python中，一个`.py`文件就称之为一个模块(Module)，每个模块都是一个独立的 Python 源代码文件。模块名称由文件名指定，不含 .py 扩展名

## 1、关于模块

- 使用模块最大的好处是大大提高了代码的可维护性。其次，编写代码不必从零开始。当一个模块编写完毕，就可以被其他地方引用。我们在编写程序的时候，也经常引用其他模块，包括Python内置的模块和来自第三方的模块；使用模块还可以避免函数名和变量名冲突。相同名字的函数和变量完全可以分别存在不同的模块中	

- 为了避免模块名冲突，Python又引入了按目录来组织模块的方法，称为包（Package）：请注意，每一个包目录下面都会有一个`__init__.py`的文件，这个文件是必须存在的，否则，Python就把这个目录当成普通目录，而不是一个包。`__init__.py`可以是空文件，也可以有Python代码，因为`__init__.py`本身就是一个模块，而它的模块名就是mycompany；可以有多级目录，组成多级层次的包结构，引入了包以后，只要顶层的包名不与别人冲突，那所有模块都不会与别人冲突。现在，abc.py模块的名字就变成了mycompany.abc，类似的，xyz.py的模块名变成了mycompany.xyz


## 2、定义模块

定义一个模块：创建一个文件：`pricing.py`
```py
# pricing.py
def get_net_price(price, tax_rate, discount=0):
    return price * (1 + tax_rate) * (1-discount)

def get_tax(price, tax_rate=0):
    return price * tax_rate
```

## 3、导入模块

要使用模块中定义的来自其他文件的对象，可以使用 import 语句

**（1） `import <module_name>`**
```py
import module_name
# 比如 
import pricing
```
导入模块时，Python 会执行相应文件中的所有代码。有了这个模块名，就可以在当前模块中访问导入模块中的函数、变量等。例如，可以使用以下语法调用导入模块中定义的函数：
```py
module_name.function_name()
```
比如：
```py
# main.py
import pricing
net_price = pricing.get_net_price(
    price=100,
    tax_rate=0.01
)
print(net_price)
```

**（2）`import <module_name> as new_name`**

如果不想在 main.py 中使用 pricing 作为标识符，可以将模块名称重命名为另一个名称，如下所示：
```py
import pricing as selling_price
net_price = selling_price.get_net_price(
    price=100,
    tax_rate=0.01
)
```

**（3）`from <module_name> import <name>`**

如果要引用模块中的对象而不使用模块名前缀，可以使用以下语法显式导入这些对象：
```py
from module_name import fn1, fn2
fn1()
fn2()
```

**（4）`from <module_name> import <name> as <new_name>`: 重命名导入的对象**

使用以下导入语句，可以将导入的名称重命名为另一个名称：
```py
from <module_name> import <name> as <new_name>
```
比如：
```py
from pricing import get_net_price as calculate_net_price
net_price = calculate_net_price(
    price=100,
    tax_rate=0.1,
    discount=0.05
)
```

**（5）`from <module_name> import` * : 导入模块中的所有对象**
```py
from module_name import *
```
该 import 语句将导入所有公共标识符，包括变量、常量、函数、类等。**但是**，这种做法并不好，因为如果导入的模块有相同的对象，第二个模块的对象就会覆盖第一个模块的对象。程序可能无法按预期运行

## 4、模块搜索路径

当在代码中写入如下代码：`import module`，Python 将从以下来源搜索 module.py 文件：
- 执行程序的当前文件夹；
- 环境变量 [PYTHONPATH](https://docs.python.org/3/using/cmdline.html#envvar-PYTHONPATH) 中指定的文件夹列表（如果之前设置过）
- 在安装 Python 时进行了配置文件夹列表

下面的代码显示了当前模块搜索路径：
```py
import sys

for path in sys.path:
    print(path)
```
在Windows上输出如下：
```py
d:\notes\Python\module
D:\software\Python\python311.zip
D:\software\Python\DLLs
D:\software\Python\Lib
D:\software\Python
D:\software\Python\Lib\site-packages
```
为确保 Python 始终能找到 module.py，您需要
- 将 module.py 放在执行程序的文件夹中；
- 在PYTHONPATH 环境变量中加入包含 module.py 的文件夹。或者，也可以将 module.py 放在PYTHONPATH 变量中包含的某个文件夹中；
- 将 module.py 放在某个依赖安装的文件夹中；

**运行时修改 Python 模块搜索路径：**

Python 允许通过修改 `sys.path` 变量在运行时修改模块搜索路径。这使您可以将模块文件存储在您选择的任何文件夹中；由于 sys.path 是一个列表，因此可以在其中添加搜索路径

## 5、`__name__`

由于 `__name__` 变量两边都有双下划线，所以称为 dunder name。dunder 代表双下划线；

在 Python 中，`__name__` 是一个特殊的变量。说它特殊，是因为 Python 会根据包含它的脚本的执行方式给它分配不同的值
- 直接运行脚本时，Python 会将 `__name__` 变量设置为`__main__`；
- 但是，如果将文件作为模块导入，Python 会将模块名设置为变量 `__name__`

示例：
```py
# billing.py
def calculate_tax(price, tax):
    return price * tax
def print_billing_doc():
    tax_rate = 0.1
    products = [{'name': 'Book',  'price': 30},
                {'name': 'Pen', 'price': 5}]
    # print billing header
    print(f'Name\tPrice\tTax')
    # print the billing item
    for product in products:
        tax = calculate_tax(product['price'], tax_rate)
        print(f"{product['name']}\t{product['price']}\t{tax}")
print(__name__)
```
另外一个文件：app.py
```py
import billing
```
执行app.py
```bash
> python app.py
billing
```
但是，如果执行：
```bash
> python billing.py
__main__
```
因此，使用 `__name__` 变量可以检查文件是直接执行的，还是作为模块导入的

但是，当您执行 app.py 时，您不会看到 if 块被执行，因为 `__name__` 变量没有设置为`__main__`，而是设置为 "billing"。

## 6、包

通过package，可以在分层结构中组织模块。Python 组织软件包和模块的方式就像操作系统组织文件夹和文件一样。
- 要创建软件包，需要创建一个新文件夹，并将相关模块放入该文件夹中；
- 要指示 Python 将包含文件的文件夹视为软件包，需要在文件夹中创建一个 `__init__.py` 文件。
> 请注意，从 Python 3.3 开始，Python 引入了隐式命名空间包特性。这使得 Python 可以将文件夹视为一个包，而无需使用 `__init__.py`

**导入包：**
```py
import package.module
```
要访问属于软件包的模块中的对象，可以使用dot：
```py
package.module.function
```
为了简化，也可以按照如下方式导入：
```py
from <module> import <function>
```
比如：
```py
# main.py
from sales.order import create_sales_order
from sales.delivery import create_delivery
from sales.billing import create_billing

# 或者重命名
from sales.order import create_sales_order as create_order
from sales.delivery import create_delivery as start_delivery
from sales.billing import create_billing as issue_billing
```

**初始化包：**

当您导入一个软件包时，Python 将执行该软件包中的 `__init__.py`，因此，可以将代码放在 `__init__.py` 文件中，以初始化包级数据，比如：
```py
# __init__.py
# default sales tax rate
TAX_RATE = 0.07
```
在 main.py 文件中，可以这样访问该包 `__init__.py`中的 TAX_RATE：
```py
# main.py
from sales import TAX_RATE
print(TAX_RATE)
```
除了初始化package级数据外，`__init__.py` 还允许你自动从软件包中导入模块:
```py
# __init__.py
# import the order module automatically
from sales.order import create_sales_order
# default sales tax rate
TAX_RATE = 0.07
```
然后从 main.py 文件中导入销售软件包，创建 create_sales_order  函数就会自动可用，如下所示
```py
# main.py
import sales
sales.order.create_sales_order()
```

**`from <package> import *`**
```py
from <package> import *
```
当使用上面语句从package导入所有对象时，Python 将查找 `__init__.py` 文件。如果 `__init__.py` 文件存在，它将加载文件中名为 `__all__` 的特殊列表中指定的所有模块。
```py
# __init__.py
__all__ = [
    'order',
    'delivery'
]
```
并在 main.py 中使用以下导入语句：
```py
# main.py
from sales import *
order.create_sales_order()
delivery.create_delivery()
# cannot access the billing module
```

**子包：**

软件包可以包含子软件包。通过子软件包，可以进一步组织模块

## 7、使用示例

```python
#!/usr/bin/env python3 ==> 标准注释，表示该文件可以之unix环境直接运行
# -*- coding: utf-8 -*- ==>文件的编码格式
'a test module' # 一个字符串，表示模块的文档注释，任何模块代码的第一个字符串都被视为模块的文档注释；
__author__ = 'BlueFish' # 变量把作者写进去
import sys  # 导入sys模块后，我们就有了变量sys指向该模块，利用sys这个变量，就可以访问sys模块的所有功能
def test():
	args = sys.argv
	if len(args)==1:
			print('Hello， world!')
	elif len(args)==2:
		print('Hello， %s!' % args[1])
	else:
		print('Too many arguments!')
if __name__=='__main__':
	test()
```
- sys模块有一个argv变量，用list存储了命令行的所有参数`.argv`至少有一个元素.因为第一个参数永远是该`.py`文件的名称：运行python3 hello.py获得的sys.argv就是`['hello.py']`；
	运行`python3 hello.py Michael`获得的sys.argv就是['hello.py'， 'Michael']。

- 上述最后两行代码:
	当我们在命令行运行hello模块文件时，Python解释器把一个特殊变量`__name__`置为`__main__`，而如果在其他地方导入该hello模块时，if判断将失败，因此，这种if测试可以让一个模块通过命令行运行时执行一些额外的代码，最常见的就是运行测试
	
- 作用域：在python中通过`"_"_`前缀来实现对作用域的控制:
	- `__xxx__`这样的变量是特殊变量，可以被直接引用，但是有特殊用途，比如上面的__author__，__name__就是特殊变量；
	- `_xxx`和__xxx`这样的函数或变量就是非公开的（private），不应该被直接引用，比如_abc，__abc等
		- 注意:private函数和变量“不应该”被直接引用，而不是“不能”被直接引用，是因为Python并没有一种方法可以完全限制访问private函数或变量
	- 外部不需要引用的函数全部定义成private，只有外部需要引用的函数才定义为public

## 8、private函数	

假设某个模块 mail.py 中有如下两个方式：
```py
def send(email, message):
    print(f'Sending "{message}" to {email}')

def attach_file(filename):
    print(f'Attach {filename} to the message')
```
只想暴露 send 方法给到外部，如何实现呢？

**函数前加 `_`**
```py
def send(email, message):
    print(f'Sending "{message}" to {email}')

def _attach_file(filename):
    print(f'Attach {filename} to the message')
```
使用时：
```py
from mail import *
# 只能看到 send 方法
send('test@example.com','Hello')
```
如果尝试调用 `_attach_file` 方法，会报相应的错误：
```py
NameError: name '_attach_file' is not defined
```

**使用`__all__`变量**

`__all__` 指定了其他模块可以使用的函数（以及变量和其他对象）列表。换句话说，只要不在 `__all__` 变量中列出某个函数，它就可以被私有化：
```py
# mail.py
__all__ = ['send']
def send(email, message):
    print(f'Sending "{message}" to {email}')
def attach_file(filename):
    print(f'Attach {filename} to the message')
```
使用包结构，有如下包结构：
```
├── mail
|  ├── email.py
|  └── __init__.py
└── main.py
```
emial.py代码：
```py
# email.py
__all__ = ['send']
def send(email, message):
    print(f'Sending "{message}" to {email}')
def attach_file(filename):
    print(f'Attach {filename} to the message')
```
`__init__.py`增加如下diamante：
```py
from .email import * 
__all__ = email.__all__
```
这样，mail 包只公开 `email.__all__` 变量中指定的 send() 函数。它从外部隐藏了 `attach_file()` 函数
		
# 三、第三方模块

## 1、pip

- [Python Package Index.](https://pypi.org/)

在Python中，安装第三方模块，是通过包管理工具pip完成的；

Python 软件包使用由三部分组成的版本号：主版本、次版本和补丁：
```
major.minor.patch
```

为了安装第三方模块，可以使用如下：
```bash
pip install requests
```
pip 是 Python 的软件包安装程序。Pip 允许你从 PyPI 和其他软件源安装软件包。python自带Pip：
```py
pip -V
```
安装包：
```py
pip install <package_name>
```
要安装特定版本的软件包，可使用以下命令：
```py
pip install <package_name>==<version>
pip install requests==2.20.1
```

**列出安装的包**
```bash
pip list
```
要检查哪些软件包已过期，可使用以下命令:
```bash
pip list --outdated
```
卸载包：
```bash
pip uninstall <package_name>
```

**列出软件包的依赖项**

安装软件包时，如果该软件包使用了其他软件包，pip 会安装该软件包及其依赖包，以及依赖包的依赖包，依此类推:
```bash
pip show <package_name>
```

## 2、虚拟环境

### 2.1、为什么需要虚拟环境

安装 Python 时，Python 会将所有系统包存储在指定的文件夹中。通常，大多数系统包都位于 sys.prefix 中指定路径的子文件夹中。要找到这个路径，可以导入 sys 模块并显示如下：
```py
import sys
print(sys.prefix)
```
当使用 pip 安装第三方软件包时，Python 会将这些软件包存储在由 site.getsitepackges() 函数指定的不同文件夹中：
```py
import site
print(site.getsitepackages())
```
假设有两个项目使用不同版本的库。由于只有一个位置可以存储第三方软件包，因此无法同时保存不同的版本；一种变通办法是使用 pip 命令，通过安装/卸载软件包来切换版本。不过，这将耗费大量时间，而且不能很好地扩展；

这就是虚拟环境发挥作用的地方

### 2.2、什么是虚拟环境

Python 使用虚拟环境为每个项目创建一个孤立的环境。换句话说，每个项目都有自己的目录来存储第三方软件包；如果有多个项目使用不同版本的软件包，可以将它们分别存储在不同的虚拟环境中。

> 自 3.3 版起，Python 将虚拟环境模块 (venv) 作为标准库。因此，要使用 venv 模块，您必须安装 Python 3.3 或更高版本

### 2.3、如何使用

在一个Python目录下，执行如下命令，创建一个名为 project_env 的虚拟环境
```bash
python -m venv project_env
```
运行 project_env/Scripts 目录中的 `activate.bat` 文件，激活虚拟环境：
```bash
project_env\Scripts\activate
```
在终端中，可以看到如下信息：
```bash
(project_env) D:\test_env\project_env\Scripts>
```
前缀（project_env）表示您正处于 project_env 虚拟环境中，执行`where python`：
```bash
D:\test_env\project_env\Scripts\python.exe
C:\Python\python.exe
```
第一行显示 python.exe 位于 `project_env/Scripts` 文件夹中。这意味着如果在 project_env 中运行 python 命令，执行的将是 `D:\test_env\project_env\Scripts\python.exe` 而不是 `C:\Python\python.exe`

创建 project_env 虚拟环境时，venv 模块已经安装了两个软件包：pip 和 setuptools：
```bash
(project_env) D:\test_env\web_crawler>pip list
Package    Version
---------- -------
pip        23.1.2
setuptools 65.5.0
```
安装依赖之后，执行如下命令创建 `requirements.txt` file
```bash
pip freeze > requirements.txt
```
`requirements.txt` 文件包含项目使用的 project_env 虚拟环境中安装的所有软件包版本，将项目复制到另一台机器时，可以运行 `pip install` 命令来安装 `requirements.txt` 文件中列出的所有软件包

比如在当前工程下安装`pip install requests`，那么 `requirements.txt` 内容如下：
```
certifi==2023.7.22
charset-normalizer==3.2.0
idna==3.4
requests==2.31.0
urllib3==2.0.4
```
要停用虚拟环境，可以运行 deactivate 命令：
```bash
(project_env) D:\test_env\web_crawler>deactivate
D:\test_env\web_crawler>
```
