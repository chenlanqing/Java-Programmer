# 一、python简介

## 1、适合领域

- web网站和各种网站服务；
- 系统工具和脚本
- 把其他语言开放的模块包装；

## 2、安装python

- 2.x还是3.x ？

	Python有两个版本，一个是`2.x`版，一个是`3.x`版，这两个版本是不兼容的，因为现在Python正在朝着3.x版本进化，在进化过程中，大量的针对2.x版本的代码要修改后才能运行，所以，目前有许多第三方库还暂时无法在3.x上使用

## 3、python编译器

- CPython

	当我们从Python官方网站下载并安装好Python 2.7后，我们就直接获得了一个官方版本的解释器：CPython。这个解释器是用C语言开发的，所以叫CPython。在命令行下运行python就是启动CPython解释器。CPython是使用最广的Python解释器。教程的所有代码也都在CPython下执行。

- IPython

	IPython是基于CPython之上的一个交互式解释器，也就是说，IPython只是在交互方式上有所增强，但是执行Python代码的功能和CPython是完全一样的。好比很多国产浏览器虽然外观不同，但内核其实都是调用了IE。CPython用>>>作为提示符，而IPython用`In [序号]`:作为提示符。

- PyPy

	PyPy是另一个Python解释器，它的目标是执行速度。PyPy采用JIT技术，对Python代码进行动态编译（注意不是解释），所以可以显著提高Python代码的执行速度。绝大部分Python代码都可以在PyPy下运行，但是PyPy和CPython有一些是不同的，这就导致相同的Python代码在两种解释器下执行可能会有不同的结果。如果你的代码要放到PyPy下执行，就需要了解PyPy和CPython的不同点。

- Jython

	Jython是运行在Java平台上的Python解释器，可以直接把Python代码编译成Java字节码执行。

- IronPython

	IronPython和Jython类似，只不过IronPython是运行在微软.Net平台上的Python解释器，可以直接把Python代码编译成.Net的字节码。
		
## 4、直接运行py文件

在Mac和Linux上是可以的，方法是在.py文件的第一行加上：`#!/usr/bin/env python3`

## 5、Mac中Python安装目录

- Mac系统自带python路径为`/System/Library/Frameworks/Python.framework/Version` 这里可能会有多个python版本，里面Current存放系统当前python版本，进入`Current/bin`，在终端输入`./python --version`即可查看系统当前python版本（注：若使用python --version命令是查看用户当前python版本而不是系统python版本）

- HomeBrew安装python路径为`/usr/local/Cellar/python` 里面存放HomeBrew所安装版本，进入`2.7.13/bin`，在终端输入`./python --version` 即可查看用户当前使用的python版本。如果使用brew工具正确安装python的情况下，用户当前python版本会是新安装的python

- 系统命令默认路径在`/usr/bin`，用户命令默认路径在`/usr/local/bin`（brew安装的命令默认在这个路径下）。如果存在相同的命令，则会依据`/etc/paths` 文件中的环境变量顺序（前面优先于后面）依次查找，查看环境变量也可以在终端输入`echo $PATH` 查看，遵循左面路径优先于右面路径

# 二、数据类型

## 1、整数

Python可以处理任意大小的整数，当然包括负整数，在Python程序中，整数的表示方法和数学上的写法一模一样；计算机由于使用二进制，所以，有时候用十六进制表示整数比较方便，十六进制用0x前缀和0-9，a-f表示，

例如：`0xff00，0xa5b4c3d2...`

*Python的整数没有大小限制*

## 2、浮点数

- 浮点数也就是小数，之所以称为浮点数，是因为按照科学记数法表示时，一个浮点数的小数点位置是可变的，比如，1.23x10^9和12.3x10^8是相等的。浮点数可以用数学写法，如1.23，3.14，-9.01，等等。
- 但是对于很大或很小的浮点数，就必须用科学计数法表示，把10用e替代，1.23x10^9就是1.23e9，或者12.3e8，0.000012可以写成1.2e-5，等等。
- 整数和浮点数在计算机内部存储的方式是不同的，整数运算永远是精确的（除法难道也是精确的？是的！），而浮点数运算则可能会有四舍五入的误差。
	
	注意:关于python除法，在python中有两种除法
	- `10 / 3 = 3.333333335， 9 / 3 = 3.0`：除法计算结果是浮点数，即使是两个整数恰好整除，结果也是浮点数
	- `10 // 3 = 3， 9 // 3 = 3`：除法是`//`，称为地板除，两个整数的除法仍然是整数；`//`除法只取结果的整数部分，所以Python还提供一个余数运算，可以得到两个整数相除的余数；

*Python的浮点数也没有大小限制，但是超出一定范围就直接表示为inf（无限大）*

**下划线分割数字：**

如果一个数字太大，不方便阅读，可以增加下划线
```py
count = 10000000000
count = 10_000_000_000
print(count)
```
> 请注意，数字中的下划线从 Python 3.6 开始就可用了

## 3、字符串

字符串表示方式：
- 字符串可以使用：`''`、`""`；
- 多行字符串：`'''  '''`、`""" """`
	```py
	'''Line 1
	Line 2
	Line 3'''
	```
- raw字符串：`message = r'C:\python\bin'`
- 如果字符串内部既包含`"'"`又包含`'"'`怎么办？可以用转义字符` '\''`来标识；
- 使用`len()`函数可以计算字符串长度，对于byte字节，则是计算字节数；
- 字符串是固定不变的，如果需要修改，需要重新创建一个

### 3.1、f-strings

```py
name = 'John'
message = f'Hi {name}'
print(message)
```
> Python 在 3.6 版本中引入了 f-string

### 3.2、字符串拼接

- 使用 `+` 运算符
- `greeting = 'Good ' 'Morning!'` => `Good Morning!`

### 3.3、访问元素

通过`[index]`的方式访问：
```py
str = "Python String"
print(str[0]) # P
print(str[1]) # y
```
如果使用负索引，Python 将返回从字符串末尾开始的字符。例如
```py
str = "Python String"
print(str[-1])  # g
print(str[-2])  # n
```
下列标示了字符串`"Python String"`的索引：
```
+---+---+---+---+---+---+---+---+---+---+---+---+---+
| P | y | t | h | o | n |   | S | t | r | i | n | g | 
+---+---+---+---+---+---+---+---+---+---+---+---+---+
  0   1   2   3   4   5   6   7   8   9   10  11  12
-13  -12 -11 -10 -9  -8  -7  -6  -5  -4  -3  -2  -1 
```

### 3.4、字符串切割

```py
str = "Python String"
print(str[0:2])
```
其中`str[0:2]` 返回包含从`索引 0（包含）`到` 2（排除）`的字符的子串。

分割的语法：`string[start:end]`
- 子串总是包括起始字符，不包括结尾字符。
- 开始和结束是可选项。如果省略开头，默认值为零。如果省略结尾，则默认为字符串的长度
```py
str = "Python String"
print(str[2:]) # thon String
print(str[:4]) # Pyth
```

**unicode字符串**

- 因为Python的诞生比Unicode标准发布的时间还要早，所以最早的Python只支持ASCII编码，普通的字符串'ABC'在Python内部都是ASCII编码的。Python在后来添加了对Unicode的支持，以Unicode表示的字符串用`u'...'`表示，比如：`print(u'中文')`；注意:

	*不加 u ，中文就不能正常显示。*

- 如果中文字符串在Python环境下遇到 UnicodeDecodeError，这是因为`.py`文件保存的格式有问题。可以在第一行添加注释
	```python
	#!/usr/bin/env python3
	# -*- coding: utf-8 -*-
	```
	目的是告诉Python解释器，用UTF-8编码读取源代码。然后用Notepad++ 另存为... 并选择UTF-8格式保存

- 对于单个字符的编码，Python提供了ord()函数获取字符的整数表示，chr()函数把编码转换为对应的字符；

由于Python的字符串类型是str，在内存中以Unicode表示，一个字符对应若干个字节

如果要在网络上传输，或者保存到磁盘上，就需要把str变为以字节为单位的bytes
- Python对bytes类型的数据用带b前缀的单引号或双引号表示：`x = b"ABC"`
- 以Unicode表示的str通过encode()方法可以编码为指定的bytes：`print('中文'.encode('utf-8'))`；中文无法使用ascii编码，因为中文超出了ascii编码的范围
- 如果我们从网络或磁盘上读取了字节流，那么读到的数据就是bytes。要把bytes变为str，就需要用decode()方法：`b'ABC'.decode('ascii')`
			
## 4、布尔值

布尔值和布尔代数的表示完全一致，一个布尔值只有`True、False`两种值，要么是`True`，要么是`False`，在Python中，可以直接用`True、False`表示布尔值（请注意大小写），也可以通过布尔运算计算出来。布尔值可以用and、or和not运算。

- `and`：运算是与运算，只有所有都为 True，and运算结果才是 True。
- `or`：运算是或运算，只要其中有一个为 True，or 运算结果就是 True。
- `no`t：运算是非运算，它是一个单目运算符，把 True 变成 False，False 变成 True。

可以通过 bool() 函数确认表达式是 True 还是 False
```py
>>> bool('Hi')
True
>>> bool('')
False
>>> bool(100)
True
>>> bool(0)
False
```

在Python中，以下表示`False`：
- 0
- 空字符串：`''`
- False
- None
- empty list `[]`
- empty tuple `()`
- empty dictionary `{}`

## 5、空值

空值是Python里一个特殊的值，用None表示。None不能理解为0，因为0是有意义的，而None是一个特殊的空值。此外，Python还提供了列表、字典等多种数据类型，还允许创建自定义数据类型；

## 6、类型转换

比如在Python交互环境，通过 input() 获取到的数据都是字符串，如果要将字符串转换为数字，需要使用 int() 函数。更确切地说，int() 函数将字符串转换为整数。
```py
price = input('Enter the price ($):')
tax = input('Enter the tax rate (%):')
net_price = int(price) * int(tax) / 100
print(f'The net price is ${net_price}')
```
其他的类型转换函数：
- float(str)：将一个字符串转换为浮点数.
- bool(val) ：将一个值转换为 True 或 False.
- str(val)：返回字符串的表示

**获取数据类型：**
```py
>>> type(100)
<class 'int'>
>>> type(2.0)
<class 'float'>
>>> type('Hello')
<class 'str'>
>>> type(True)
<class 'bool'>
```

# 三、输出与输入语句

## 1、输出

`print(`)函数也可以跟上多个字符串，用逗号“，”隔开，就可以连成一串输出：`print('Hello World')`

`print()`函数也可以接受多个字符串，用逗号“，”隔开，就可以连成一串输出，依次打印每个字符串，遇到逗号“，”会输出一个空格

格式化输出：采用的格式化方式和C语言是一致的，用`%`实现在字符串内部，`%s`表示用字符串替换，`%d`表示用整数替换，有几个`%?`占位符，后面就跟几个变量或者值，顺序要对应好。如果只有一个`%?`，括号可以省略：
- 常见的占位符有
	```
	%d	整数
	%f	浮点数
	%s	字符串
	%x	十六进制整数
	```
- 格式化整数和浮点数还可以指定是否补0和整数与小数的位数：`'%2d-%02d' % (3， 1) ==>  ' 3-01'`，`'%.2f' % 3.1415926 ===>  '3.14'`

- 字符串里面的`%`是一个普通字符怎么办？这个时候就需要转义，用`%%`来表示一个`%`

## 2、输入

如果要获取用户输入的数据(在python交互环境): `input()`--可以让用户输入字符串，并存放到一个变量里
```
>>> name = input()
Michael
==> input('please enter your name: '):可以提示用户输入信息
```
*注意：*

从input()读取的内容永远以字符串的形式返回；如果需要获得输入的为数字，先用int()或其他方法把字符串转换为我们想要的类型；
		
# 四、注释

`#`为注释，如果多行注释，可以使用多行字符串表示
```py
def increase(salary, percentage, rating):
    """ increase salary base on rating and percentage
    rating 1 - 2 no increase
    rating 3 - 4 increase 5%
    rating 4 - 6 increase 10%
    """
```
使用多行字符串表示注释

# 五、变量与常量

## 1、变量

- 在Python程序中，变量是用一个变量名表示，【`变量名必须是大小写英文、数字和_的组合，且不能用数字开头`】
- 在Python中，等号"="是赋值语句，可以把任意数据类型赋值给变量，同一个变量可以反复赋值，而且可以是不同类型的变量
- 理解变量在计算机内存中的表示也非常重要。当我们写：a = 'ABC'时，Python解释器干了两件事情：
	- 在内存中创建了一个'ABC'的字符串；
	- 在内存中创建了一个名为a的变量，并把它指向'ABC'
		
## 2、常量

Python 不支持常量，要解决这个问题，可以使用大写字母来命名变量，以表示该变量应被视为常量

# 六、运算符

和数学运算不同的地方是：Python的整数运算结果仍然是整数，浮点数运算结果仍然是浮点数，整数和浮点数混合运算的结果就变成浮点数了；

`n ** x`：表示n的x次方，如:`5**2表示5的平方，5**5:表示5的5次方`；

python中除法两种形式：`10 / 3`，`10 // 3 取结果的整数部分`；

## 1、比较运算符

6个比较运算符： `< 、 <= 、 > 、 >= 、 == 、 !=`

## 2、逻辑运算符

- and: 两个都为 True 结果为 True
- or: 两个都为 False 结果为 False
- not: not 运算符只适用于一个条件。它将该条件的结果反转，True 变为 False，False 变为 True。
```py
>>> price = 9.99
>>> not price > 10
True
```
优先级： not > and > or

# 七、条件判断和循环

## 1、if语句

- if 语句后接表达式，然后用`":"`表示代码块开始
	```python
	age = 20
	if age >= 18:
		print('your age is'， age)
		print('adult')
	print('END')
	```
	Python代码的缩进规则：具有相同缩进的代码被视为代码块，上面的3，4行 print 语句就构成一个代码块（但不包括第5行的print）；如果 if 语句判断为 True，就会执行这个代码块。

	缩进请严格按照Python的习惯写法：4个空格，不要使用Tab，更不要混合Tab和空格，否则很容易造成因为缩进引起的语法错误；如果你在Python交互环境下敲代码，还要特别留意缩进，并且退出缩进需要多敲一行回车

- `if...else....`
	```python
	if condition:
		....
	else:
		....
	```
- `if-elif-else`
	```python
	if age >= 18:
		print('adult')
	elif age >= 6:
		print('teenager')
	elif age >= 3:
		print('kid')
	else:
		print('baby')
	```
- if判断条件还可以简写，比如写
	```python
	if x:
		print('True')
	```
	只要x是非零数值、非空字符串、非空list等，就判断为True，否则为False

## 2、三目运算符（Ternary operator）

`value_if_true if condition else value_if_false`

三元运算符对条件进行评估。如果结果为 True，则返回 value_if_true。否则，返回 value_if_false，等价于：
```py
if condition:
	value_if_true
else:
	value_if_false
```
比如：
```py
ticket_price = 20 if int(age) >= 18 else 5 # 意思是如果 age >=18 ticket_price = 20，否则 ticket_price = 5
```

## 3、for语句

基本语法：
```py
# range(n) 从 0 开始生成一个包含 n 个整数的序列。它将数值递增一个，直到达到 n； range(start, stop) 可以指定起始和结束
# range(start, stop, step) 指定步长（step）
for index in range(n):
    statement
```

遍历一个集合
```python
L = ['Adam'， 'Lisa'， 'Bart']
for name in L:
	print(name)
```
name 这个变量是在 for 循环中定义的，意思是，依次取出list中的每一个元素，并把元素赋值给 name，然后执行for循环体

`range()`函数，可以生成一个整数序列，再通过list()函数可以转换为list；`range(5)`生成的序列是从0开始小于5的整数：`list(range(5)) ==> [0，1，2，3，4]`
	
## 4、while循环

语法
```py
while condition:  
   body
```

要从 0 开始打印不大于 N 的整数：
```python
N = 10
x = 0
while x < N:
	print x
	x = x + 1
```
break 退出循环：用 for 循环或者 while 循环时，如果要在循环体内直接退出循环，可以使用 break 语句

continue 继续循环：跳过后续循环代码，继续下一次循环

## 5、pass

举个例子：
```py
counter = 1
max = 10
if counter <= max:
    counter += 1
else:
    # implement later
```
在上面的 else 块中，还没有任何代码，可能后续会补充；如果你运行当前代码，会报错：
```
SyntaxError: unexpected EOF while parsing
```
为了解决上述问题，你可以使用 pass：
```py
counter = 1
max = 10
if counter <= max:
    counter += 1
else:
    pass
```
当运行包含 pass 语句的代码时，Python 解释器会将 pass 语句视为一条语句。因此，它不会发出语法错误。

pass 可以用在 if、while、for、function、class 中；

# 八、函数

引用 python 内置函数，需要导入:[import](http://docs.python.org/3/library/functions.html)

## 1、定义函数

函数名其实就是指向一个函数对象的引用，完全可以把函数名赋给一个变量，相当于给这个函数起了一个“别名”，定义一个函数要使用 def 语句，依次写出函数名、括号、括号中的参数和`冒号:`，然后，在缩进块中编写函数体，函数的返回值用 return 语句返回

注意：如果没有return语句，函数执行完毕后也会返回结果，只是结果为 None；函数执行完毕也没有return语句时，自动 return None。
```py
def greet(name):
    """ Display a greeting to users """
    print(f"Hi {name}")
```
- 参数检查：数据类型检查可以用内置函数`isinstance()`实现，如：`isinstance(x, (int, float))`

## 2、内置的转换函数

```python
>>> int('123')
123
>>> int(12.34)
12
>>> float('12.34')
12.34
>>> str(1.23)
'1.23'
>>> str(100)
'100'
>>> bool(1)
True
>>> bool('')
False
>>> hex(8000) # 转16进制值
'0x1f40'
>>> hex(65536)
'0x10000'
```

- python函数可以返回多个值：实际上，函数返回多个值是种假象，Python的函数返回多值其实就是返回一个tuple

## 3、递归函数

使用时需注意防止栈溢出
```python
def fact(n):
	if n== 1:
		return 1
	return n * fact(n - 1)
```
- 解决递归调用栈溢出的方法是通过尾递归优化，事实上尾递归和循环的效果是一样的，所以，把循环看成是一种特殊的尾递归函数也是可以的
	```python
	def fact(n):
		return fact_iter(n， 1)

	def fact_iter(num， product):
		if num == 1:
			return product
		return fact_iter(num - 1， num * product)
	```
	遗憾的是，大多数编程语言没有针对尾递归做优化，Python解释器也没有做优化，所以，即使把上面的fact(n)函数改成尾递归方式，也会导致栈溢出
	
## 4、定义默认参数

函数的默认参数的作用是简化调用，语法如下：
```py
def function_name(param1, param2=value2, param3=value3, ...):
```
示例：
```python
def power(x, n=2):
	s = 1
	while n > 0:
		n = n - 1
		s = s * x
	return s
```
由于函数的参数按从左到右的顺序匹配：必选参数在前，默认参数在后，否则Python的解释器会报错，比如下面的：
```py
def function_name(param1=value1, param2, param3):
```

如何设置默认参数：当函数有多个参数时，把变化大的参数放前面，变化小的参数放后面。变化小的参数就可以作为默认参数:

有多个默认参数时，调用的时候，既可以按顺序提供默认参数，也可以不按顺序提供部分默认参数。当不按顺序提供部分默认参数时，需要把参数名写上：
```py
def greet(name='there', message='Hi'):
    return f"{message} {name}"
greeting = greet(message='Hello')
print(greeting)
```

默认参数很有用，但使用不当，也会掉坑里。默认参数有个最大的坑，如下:
```python
def add_end(L=[]):
	L.append('END')
	return L
>>> add_end()  ===> ['END']
```
再次调用`>>> add_end()  ===> ['END'，'END'] `
- Python函数在定义的时候，默认参数L的值就被计算出来了，即`[]`，因为默认参数L也是一个变量，它指向对象`[]`，
- 每次调用该函数，如果改变了L的内容，则下次调用时，默认参数的内容就变了，不再是函数定义时的`[]`了。

***所以，定义默认参数要牢记一点：默认参数必须指向不变对象！***

要修改上面的例子，我们可以用None这个不变对象来实现：
```python
def add_end(L=None):
	if L is None:
		L = []
	L.append('END')
	return L
```	

**关于不变对象：** 为什么要设计str、None 这样的不变对象呢？因为不变对象一旦创建，对象内部的数据就不能修改，这样就减少了由于修改数据导致的错误。此外，由于对象不变，多任务环境下同时读取对象不需要加锁，同时读一点问题都没有。我们在编写程序时，如果可以设计一个不变对象，那就尽量设计成不变对象；
	
## 5、定义可变参数

可变参数的名字前面有个 `*` 号，我们可以传入0个、1个或多个参数给可变参数：
```python
def calc(*numbers):
	sum = 0
	for n in numbers:
		sum = sum + n * n
	return sum
```			
- Python解释器会把传入的一组参数组装成一个tuple传递给可变参数，因此，在函数内部，直接把变量 args 看成一个 tuple 就好了。
- 如果已经有一个list或tuple，要调用可变参数，python允许在list或tuple前面加上 `*` 号，把list或tuple变成可变参数；
	```python
	num = [5，7，9，52]
	print(calc(*num))
	```

## 6、空函数

如果想定义一个什么事也不做的空函数，可以用pass语句：
```python
def nop():
	pass
```
pass语句什么都不做，那有什么用？实际上pass可以用来作为占位符，比如现在还没想好怎么写函数的代码，就可以先放一个pass，让代码能运行起来。
pass还可以用在其他语句里，比如：
```python
if age >= 18:
	pass
```
缺少了pass，代码运行就会有语法错误
	
## 7、关键字参数

使用关键字参数时，重要的是它们的名称，而不是它们的位置：
```py
fn(parameter2=value2,parameter1=value1)
```
使用关键字参数后，其余参数也需要使用关键字参数;

关键字参数允许你传入0个或任意个含参数名的参数，这些关键字参数在函数内部自动组装为一个dict
```python
def person(name, age, **kw):
	print('name:', name, 'age:', age, 'other:', kw)
```

函数person除了必选参数name和age外，还接受关键字参数kw。在调用该函数时，可以只传入必选参数

- 关键字参数有什么用：它可以扩展函数的功能，利用关键字参数来定义这个函数就能满足注册的需求
- 和可变参数类似，也可以先组装出一个dict，然后，把该dict转换为关键字参数传进去；
	```python
	>>> kw = {'city': 'Beijing'， 'job': 'Engineer'}
	>>> person('Jack', 24, city=kw['city'], job=kw['job'])
	name: Jack age: 24 other: {'city': 'Beijing'， 'job': 'Engineer'}
	```
	简化版:
	```python
	>>> extra  = {'city': 'Beijing'， 'job': 'Engineer'}
	>>> person('Jack'， 24， **extra )
	name: Jack age: 24 other: {'city': 'Beijing'， 'job': 'Engineer'}
	```
	`**extra` 表示把extra这个dict的所有key-value用关键字参数传入到函数的`**kw`参数，`kw`将获得一个dict，注意kw获得的dict是extra的一份拷贝，对kw的改动不会影响到函数外的extra；

## 8、命名关键字参数

对于关键字参数，函数的调用者可以传入任意不受限制的关键字参数。至于到底传入了哪些，就需要在函数内部通过kw检查；

- 如果要限制关键字参数的名字，就可以用命名关键字参数，例如:
	```python
	def person(name, age, *, city, job):
		print(name, age, city, job)
	```
- 和关键字参数`**kw`不同，命名关键字参数需要一个特殊分隔符`*`，`*`后面的参数被视为命名关键字参数；
- 调用方式如下：`person('Jack'， 24， city='Beijing'， job='Engineer')`

	命名关键字参数必须传入参数名，这和位置参数不同。如果没有传入参数名，调用将报错：TypeError: person() takes 2 positional arguments but 4 were given
	
	注意：由于调用时缺少参数名city和job，Python解释器把这4个参数均视为位置参数，但person()函数仅接受2个位置参数

- 命名关键字参数可以有缺省值
	```python
	def person(name， age， *， city='Hangzhou'， job):
		print(name， age， city， job)
	```

**特别注意：**
- 使用命名关键字参数时，要特别注意，`*`不是参数，而是特殊分隔符。
- 如果缺少`*`，Python解释器将无法识别位置参数和命名关键字参数；

## 9、参数组合

在Python中定义函数，可以用必选参数、默认参数、可变参数、关键字参数、命名关键字参数，这5种参数都可以一起使用，或者只用其中某些

注意：除了可变参数无法和命名关键字参数混合，但是请注意，参数定义的顺序必须是：必选参数、默认参数、可变参数或命名关键字参数、关键字参数。所以，对于任意函数，都可以通过类似`func(*args， **kw)`的形式调用它，无论它的参数是如何定义的。

比如定义一个函数，包含上述4种参数：
```python
def func(a， b， c=0， *args， **kw):
	print 'a ='， a， 'b ='， b， 'c ='， c， 'args ='， args， 'kw ='， kw
>>> func(1， 2)
a = 1 b = 2 c = 0 args = () kw = {}
>>> func(1， 2， c=3)
a = 1 b = 2 c = 3 args = () kw = {}
>>> func(1， 2， 3， 'a'， 'b')
a = 1 b = 2 c = 3 args = ('a'， 'b') kw = {}
>>> func(1， 2， 3， 'a'， 'b'， x=99)
a = 1 b = 2 c = 3 args = ('a'， 'b') kw = {'x': 99}
# 最神奇的是通过一个tuple和dict，你也可以调用该函数：

>>> args = (1， 2， 3， 4)
>>> kw = {'x': 99}
>>> func(*args， **kw)
a = 1 b = 2 c = 3 args = (4，) kw = {'x': 99}
```
对于任意函数，都可以通过类似func(*args， **kw)的形式调用它，无论它的参数是如何定义的

## 10、函数的参数检查

调用函数时，如果传入的参数个数不对，python解释器会自动检查出来，并抛出TypeError例如:定义一个函数其函数的参数只能是整数或浮点数，可以使用如下来判断
```python
if not isinstance(x， (int， float)):
	raise TypeError('bad operand type')
```

## 11、lambda函数

lambda函数，即匿名函数，一个 lambda 表达式通常包含一个或多个参数，但只能有一个表达式，语法如下：
```py
lambda parameters: expression
# 等价于
def anonymous(parameters):
    return expression
```
在python中，可以将一个函数传递给另一个函数，或从另一个函数返回一个函数。

将一个函数传递到另一个函数中：
```py
def get_full_name(first_name, last_name, formatter):
    return formatter(first_name, last_name)

full_name = get_full_name(
    'John',
    'Doe',
    lambda first_name, last_name: f"{first_name} {last_name}"
)
print(full_name)

full_name = get_full_name(
    'John',
    'Doe',
    lambda first_name, last_name: f"{last_name} {first_name}"
)
print(full_name)
```
返回一个函数：
```py
def times(n):
    return lambda x: x * n
double = times(2)
result = double(2)
print(result)
result = double(3)
print(result)
```
**lambda在循环中的处理：**
```py
callables = []
for i in (1, 2, 3):
    callables.append(lambda: i)
for f in callables:
    print(f())
# 输出结果如下：
3
3
3
```
上面的问题在于：所有 lambda 表达式都引用了 i 变量，而不是 i 的当前值。要解决这个问题，需要在创建 lambda 表达式时将 i 变量与每个 lambda 表达式绑定
```py
callables = []
for i in (1, 2, 3):
    callables.append(lambda a=i: a)

for f in callables:
    print(f())

```

## 12、函数说明

通过help可以查看文档描述，比如：
```py
help(print)
# 输出内容：
print(...)
    print(value, ..., sep=' ', end='\n', file=sys.stdout, flush=False)

    Prints the values to a stream, or to sys.stdout by default.
    Optional keyword arguments:
    file:  a file-like object (stream); defaults to the current sys.stdout.
    sep:   string inserted between values, default a space.
    end:   string appended after the last value, default a newline.
    flush: whether to forcibly flush the stream.
```
自己定义一个，将字符串（单行或多行字符串）作为函数的第一行，为函数添加文档。
```py
def add(a, b):
    """ Add two arguments
    Arguments:
        a: an integer
        b: an integer
    Returns:
        The sum of the two arguments
    """
    return a + b
```
查看后输出：
```
add(a, b)
    Add the two arguments
    Arguments:
            a: an integer
            b: an integer
        Returns:
            The sum of the two arguments        
```
Python 将 docstrings 保存在函数的 `__doc__` 属性中:
```py
add.__doc__
```

## 13、函数小结

- Python的函数具有非常灵活的参数形态，既可以实现简单的调用，又可以传入非常复杂的参数
- 默认参数一定要用不可变对象，如果是可变对象，运行会有逻辑错误！
- 要注意定义可变参数和关键字参数的语法：
	- `*args`是可变参数，args接收的是一个tuple；
	- `**kw`是关键字参数，kw接收的是一个dict。
	- 以及调用函数时如何传入可变参数和关键字参数的语法：
	- 可变参数既可以直接传入：`func(1， 2， 3)`，又可以先组装list或tuple，再通过`*args`传入：`func(*(1， 2， 3)`)；
	- 关键字参数既可以直接传入：`func(a=1， b=2)`，又可以先组装dict，再通过`**kw`传入：`func(**{'a': 1， 'b': 2})`。
	- 使用`*args`和`**kw`是Python的习惯写法，当然也可以用其他参数名，但最好使用习惯用法
- 命名的关键字参数是为了限制调用者可以传入的参数名，同时可以提供默认值。定义命名的关键字参数不要忘了写分隔符`*`，否则定义的将是位置参数

# 九、List

## 1、List

list，Python内置的一种数据类型是列表，使用方括号 (`[]`) 表示列表。下面显示的是一个空 list
```py
empty_list = []
```
- list是一种有序的集合，可以随时添加和删除其中的元素，list是可变的
- 由于Python是动态语言，所以list中包含的元素并不要求都必须是同一种数据类型；使用len()可以获取list元素的个数
- 按照索引访问list，当索引超出了范围时，Python会报一个IndexError错误；可以以负数作为索引，倒序获取集合的值；`"-1"`表示获取最后一个元素
	`·`classmates[-1] => Tracy`

**添加新元素**
- `append()`：把新的元素添加到 list 的尾部
- `insert()`：接受两个参数，第一个参数是索引号，第二个参数是待添加的新元素
```python
# list.append():把新的元素添加到 list 的尾部
>>>classmates = ['Michael'， 'Bob'， 'Tracy']
>>>classmates.append('Adam')
===>['Michael'， 'Bob'， 'Tracy'， 'Adam']
# list.insert():接受两个参数，第一个参数是索引号，第二个参数是待添加的新元素
>>> classmates.insert(1， 'Jack')
['Michael'， 'Jack'， 'Bob'， 'Tracy'， 'Adam']
```

**删除元素**
- del：删除指定位置的元素：`del numbers[0]`
- list.pop()：总是删除list的最后一个元素，并且返回最后一个元素:`classmates.pop() ===> 'Adam'`
- list.pop(index)：删除某个位置上的元素，并返回该元素；`classmates.pop(1) ===> 'Jack'`
- list.remove(value)：删除列表list中第一个等于value的值，无返回值；

**替换元素**：

对list中的某一个索引赋值，就可以直接用新的元素替换掉原来的元素，list包含的元素个数保持不变；
```py
list[index] = new_value
```

**切片**：即取一个list部分数据(tuple也可以进行切片操作)
```python
# Slice
L = ['Adam'， 'Lisa'， 'Bart'， 'Paul']
# L[0:3]:从索引0开始取，直到(不包括)索引3为止，即索引0，1，2，正好是3个元素
L[0:3] ===> ['Adam'， 'Lisa'， 'Bart']
# 如果第一个索引是0，还可以省略：
L[:3]  ===> ['Adam'， 'Lisa'， 'Bart']
# 只用一个 : ，表示从头到尾：
L[:]   ===> ['Adam'， 'Lisa'， 'Bart'， 'Paul']
```
注意：切片操作还可以指定第三个参数：第三个参数表示每N个取一个
`L[::2] ===> ['Adam'， 'Bart']`
- 倒序切片：记住倒数第一个元素的索引是-1。倒序切片包含起始索引，不包含结束索引：`L[::-1]`
- 字符串的操作：字符串 `'xxx'`和 Unicode字符串 u'xxx'也可以看成是一种list，每个元素就是一个字符。因此，字符串也可以用切片操作，只是操作结果仍是字符串：`'ABCDEFG'[:3]`
		
## 2、Tuple

Tuple 是一个不能改变的列表。Python 将不能改变的值称为不可变值。因此，根据定义，Tuple 是不可变的列表。

Tuple与List相似，只是使用的是括号`()`而不是方括号`[]`：`
```py
classmates = ('Michael', 'Bob', 'Tracy')
```
获取 tuple 元素的方式和 list 是一模一样的，我们可以正常使用 `t[0]`，`t[-1]`等索引方式访问元素，但是不能赋值成别的元素；

**创建单元素tuple:**<br/>
`t = (1) # ==> 1`：t 不是 tuple ，而是整数1。为什么呢？ `()`既可以表示tuple，也可以作为括号表示运算的优先级，`(1)`被Python解释器计算出结果 1，导致我们得到的不是tuple，而是整数 1
Python 规定，单元素 tuple 要多加一个逗号`,`，即 `t = (1,) # ==>(1,)`
```py
numbers = (3,)
print(type(numbers))
# <class 'tuple'>
```

**`可变`的tuple**：Tuple所谓的“不变”是说：tuple的每个元素，指向永远不变。即指向'a'，就不能改成指向'b'，指向一个list，就不能改成指向其他对象，但指向的这个list本身是可变的！

理解了“指向不变”后，要创建一个内容也不变的tuple怎么做？那就必须保证tuple的每一个元素本身也不能变，如:
```python
t = ('a',， 'b', ['A', 'B'])
L = t(2)
L[0] = 'X'
L[1] = 'Y'
('a', 'b', ['X', 'Y'])
```

## 3、Sort List

要对List排序，需要使用 sort() 方法：
```py
list.sort()
```
sort() 方法对List进行原位排序。这意味着，sort() 方法修改了列表中元素的顺序，默认是按从小到大排序；

要对元素从高到低排序，您需要向 sort() 传递 reverse=True 参数。
```py
list.sort(reverse=True)
```

如果List包含字符串，则 `sort()` 方法会按字母顺序对字符串元素进行排序。
```py
guests = ['James', 'Mary', 'John', 'Patricia', 'Robert', 'Jennifer']
guests.sort()
print(guests)
['James', 'Jennifer', 'John', 'Mary', 'Patricia', 'Robert']
```

**对tuple排序：**
- 首先，指定一个sort key并将其传递给 sort() 方法。要定义排序键，需要创建一个函数，该函数接受一个元组，并返回要按以下方式排序的元素
```py
def sort_key(company):
    return company[2]
```
- 其次，将 sort_key 函数传递给 sort() 方法：
```py
companies = [('Google', 2019, 134.81),
             ('Apple', 2019, 260.2),
             ('Facebook', 2019, 70.7)]
# define a sort key
def sort_key(company):
    return company[2]
# sort the companies by revenue
companies.sort(key=sort_key, reverse=True)
# show the sorted companies
print(companies)
```
当然，上面的sort_key函数也可以使用 lambda表达式:
```py
companies.sort(key=lambda company: company[2], reverse=True)
```

## 4、sorted()函数

前面的`list.sort()`会在原List上排序，即会改变原有List的元素顺序，要从原始列表返回新的排序列表，需要使用 sorted() 函数：
```py
sorted(list)
```
sorted(list) 不会改变原有List；默认情况下，sorted() 函数使用小于运算符 (<) 将列表元素从低到高排序。如果您想颠倒排序顺序，可以像下面这样将反向参数传递为 True
```py
sorted(list,reverse=True)
```

## 5、切片操作符

```py
sub_list = list[begin: end: step]
```
- begin索引默认为零，end索引默认为列表的长度，step索引默认为 1。
- 切片操作符中的第一个数（冒号之前）表示切片开始的位置，第二个数（冒号之后）表示切片到哪里结束，第三个数（冒号之后）表示切片间隔数。	
- 如果不指定第一个数，Python就从序列首开始。
- 如果没有指定第二个数，则Python会停止在序列尾。
- begin、end和step可以是正值或负值。正值将列表从第一个元素切到最后一个元素，而负值则将列表从最后一个元素切到第一个元素

注意，返回的序列从开始位置开始，刚好在结束位置之前结束.即开始位置是包含在序列切片中的，而结束位置被排斥在切片外。

### 5.1、要从列表中获取 n 个首元素，可以省略第一个参数

```py
list[:n] # 等价于 list[0:n]
# 示例
colors = ['red', 'orange', 'yellow', 'green', 'blue', 'indigo', 'violet']
sub_colors = colors[:3] # 等价于： colors[0:3]
print(sub_colors)
# ['red', 'orange', 'yellow']
```

### 5.2、要获取列表中倒数第 n 个元素，可以使用负索引

```py
colors = ['red', 'orange', 'yellow', 'green', 'blue', 'indigo', 'violet']
sub_colors = colors[-3:]
print(sub_colors)
# ['blue', 'indigo', 'violet']
```

### 5.3、使用 Python List slice 从列表中获取第 n 个元素

下面的示例使用该步骤返回一个子列表，其中包括颜色列表中的每第二个元素：
```py
colors = ['red', 'orange', 'yellow', 'green', 'blue', 'indigo', 'violet']
sub_colors = colors[::2]
print(sub_colors)
# ['red', 'yellow', 'blue', 'violet']
```

### 5.4、使用slice反转列表

```py
colors = ['red', 'orange', 'yellow', 'green', 'blue', 'indigo', 'violet']
reversed_colors = colors[::-1]
print(reversed_colors)
# ['violet', 'indigo', 'blue', 'green', 'yellow', 'orange', 'red']
```

### 5.5、使用 Python List slice 代替列表的一部分

除了提取列表的一部分外，列表切片还可以更改列表元素。
```py
colors = ['red', 'orange', 'yellow', 'green', 'blue', 'indigo', 'violet']
colors[0:2] = ['black', 'white']
print(colors)
# ['black', 'white', 'yellow', 'green', 'blue', 'indigo', 'violet']
```

### 5.6、使用 Python List slice 部分替换和调整列表大小

```py
# 下面的示例使用 list slice 将第一个和第二个元素替换为新元素，并在列表中添加了一个新元素：
colors = ['red', 'orange', 'yellow', 'green', 'blue', 'indigo', 'violet']
print(f"The list has {len(colors)} elements")

colors[0:2] = ['black', 'white', 'gray']
print(colors)
print(f"The list now has {len(colors)} elements")
```

### 5.7、使用 Python List slice 删除元素

```py
# 删除 第3个、第4个、第5个元素
colors = ['red', 'orange', 'yellow', 'green', 'blue', 'indigo', 'violet']
del colors[2:5]
print(colors)
```

`shoplist[1:3]`返回从位置1开始，包括位置2，但是停止在位置3的一个序列切片，因此返回一个含有两个项目的切片。类似地，`shoplist[:]`返回整个序列的拷贝。`shoplist[::3]`返回位置3，位置6，位置9…的序列切片。

你可以用负数做切片。负数用在从序列尾开始计算的位置。例如，`shoplist[:-1]`会返回除了最后一个项目外包含所有项目的序列切片，`shoplist[::-1]`会返回倒序序列切片


```py
L = ['Michael','Sarah','Tracy','Bob','Jack']
L[0:3] ==>  ['Michael'， 'Sarah'， 'Tracy'] # 从索引0开始取，直到索引3为止
切片操作十分有用。我们先创建一个`0-99`的数列：
>>> L = range(100)
>>> L
[0， 1， 2， 3， ...， 99]
# 可以通过切片轻松取出某一段数列。比如前10个数：
>>> L[:10]
[0， 1， 2， 3， 4， 5， 6， 7， 8， 9]
# 后10个数：
>>> L[-10:]
[90， 91， 92， 93， 94， 95， 96， 97， 98， 99]
# 前11-20个数：
>>> L[10:20]
[10， 11， 12， 13， 14， 15， 16， 17， 18， 19]
# 前10个数，每两个取一个：
>>> L[:10:2]
[0， 2， 4， 6， 8]
#所有数，每5个取一个：
>>> L[::5]
[0， 5， 10， 15， 20， 25， 30， 35， 40， 45， 50， 55， 60， 65， 70， 75， 80， 85， 90， 95]
#甚至什么都不写，只写[:]就可以原样复制一个list：
>>> L[:]
[0， 1， 2， 3， ...， 99]
>>> L[:-1]
[1， 2， 3， 4， 5， 6， 7， 8， 9]
# 倒序切片
>>> L[::-1]
[10， 9， 8， 7， 6， 5， 4， 3， 2， 1]
```

## 6、List解包

为了将List中每个元素赋值给变量，常规做法是：
```py
colors = ['red', 'blue', 'green']
red = colors[0]
blue = colors[1]
green = colors[2]
```

python提供了更好做法，可以通过List unpacking的方式，可以将列表（以及元组）中的元素赋值给多个变量。例如
```py
red, blue, green = colors
```
该语句将颜色列表的第一、第二和第三个元素分别赋值给红、蓝和绿变量，在本例中，左侧变量的个数与右侧列表中元素的个数相同。如果在左侧使用的变量数量较少，就会出错。例如
```py
colors = ['red', 'blue', 'green']
red, blue = colors
# ValueError: too many values to unpack (expected 2)
```

如果您想解压缩列表的前几个元素，而不关心其他元素，可以：
- 将所需元素解包为变量；
- 将剩余的元素打包到一个新的列表中，并将其赋值给另一个变量

在变量名前加上星号 (*)，就可以将剩余的元素打包成一个列表，并将它们赋值给一个变量，比如：
```py
colors = ['red', 'blue', 'green']
red, blue, *other = colors

print(red)
print(blue)
print(other)
```

## 7、List迭代

就是对于一个集合，无论该集合是有序还是无序，我们用 for 循环总是可以依次取出集合的每一个元素
```py
cities = ['New York', 'Beijing', 'Cairo', 'Mumbai', 'Mexico']
for city in cities:
    print(city)
```

如果在迭代过程中需要访问元素的索引：使用 `enumerate()` 函数，enumerate() 函数返回一个元组，其中包含当前索引和列表中的元素，迭代的每一个元素实际上是一个tuple
```python
L = ['Adam', 'Lisa', 'Bart', 'Paul']
for index, name in enumerate(L):
	print(index, '-',  name)

#  注意:
实际上，enumerate() 函数把：
['Adam', 'Lisa', 'Bart', 'Paul']
变成了类似：
[(0, 'Adam'), (1, 'Lisa'), (2, 'Bart'), (3, 'Paul')]
```
索引迭代也不是真的按索引访问，而是由 enumerate() 函数自动把每个元素变成 (index,element) 这样的tuple，再迭代，就同时获得了索引和元素本身

当使用 enumerate() 函数时也可以指定起始索引，默认为零。
```py
cities = ['New York', 'Beijing', 'Cairo', 'Mumbai', 'Mexico']
for index, city in enumerate(cities,1):
    print(f"{index}: {city}")
```

如果一个对象说自己可迭代，那我们就直接用 for 循环去迭代它，可见，迭代是一种抽象的数据操作，它不对迭代对象内部的数据有任何要求。

默认情况下，dict迭代的是key。如果要迭代value，可以用`for value in d.values()`，如果要同时迭代key和value，可以用`for k， v in d.items()`；由于字符串也是可迭代对象，因此，也可以作用于for循环

## 8、查找元素的索引

要查找列表中某个元素的索引，可以使用 index() 函数
```py
cities = ['New York', 'Beijing', 'Cairo', 'Mumbai', 'Mexico']
result = cities.index('Mumbai')
print(result)
```
但是，如果尝试使用 index() 函数查找列表中不存在的元素，就会出现错误；

要解决这个问题，需要使用 in 运算符。如果某个值在列表中，in 操作符将返回 True。否则，返回 False
```py
cities = ['New York', 'Beijing', 'Cairo', 'Mumbai', 'Mexico']
city = 'Osaka'
if city in cities:
    result = cities.index(city)
    print(f"The {city} has an index of {result}.")
else:
    print(f"{city} doesn't exist in the list.")
```

## 9、map()函数

在处理列表（或元组）时，您经常需要转换列表中的元素，并返回一个包含转换元素的新列表，为此，您可以使用 for 循环遍历元素，将每个元素处理之后，然后将其添加到新列表中；

Python提供了一个map函数，map() 函数遍历列表（或元组）中的所有元素，对每个元素应用一个函数，然后返回一个包含新元素的新迭代器

基本语法如下：
```py
iterator = map(fn, list)
```
比如：
```py
bonuses = [100, 200, 300]
iterator = map(lambda bonus: bonus*2, bonuses)
print(list(iterator)) # 将迭代器转换为list
```

## 10、filter()函数

有时候需要在迭代一个列表的时候过滤符合条件的数据，通常做法是通过 for循环，然后通过if判断来过滤数据；

Python 有一个内置函数 filter()，以一种更好的方式过滤列表（或元组）。
```py
filter(fn, list)
```
filter() 函数遍历列表中的元素，并对每个元素应用 fn() 函数。它会返回一个迭代器，用于迭代 fn() 返回 True 的元素。

示例：
```py
scores = [70, 60, 80, 90, 50]
filtered = filter(lambda score: score >= 70, scores)
print(list(filtered))
```

## 11、reduce函数

有时候，希望将一个list通过函数输出为一个值，通常做法都是通过循环来处理的；

Python 提供了一个名为 reduce() 的函数，可以让您以更简洁的方式来reduce一个列表，语法：
```py
reduce(fn,list)
```
reduce() 函数将两个参数的 fn 函数从左到右累加应用于列表项，从而将列表还原为单一值；

reduce() 并不是 Python 的内置函数。事实上，reduce() 函数属于 functools 模块，如果需要使用的话，需要导入functools模块
```py
from functools import reduce
```
示例：
```py
from functools import reduce
def sum(a, b):
    print(f"a={a}, b={b}, {a} + {b} ={a+b}")
    return a + b
scores = [75, 65, 80, 95, 50]
total = reduce(sum, scores)
# 或者使用 lambda expression
total = reduce(lambda a, b: a + b, scores)
print(total)
```

## 12、list comprehension

list comprehension，列表推导式（或列表解析），为了帮助您根据现有 list 元素的变换创建一个 list，Python 提供了一个称为 list 解析的功能。

下面代码演示了如何使用列表理解从数字列表中生成平方：
```py
numbers = [1, 2, 3, 4, 5]
squares = [number**2 for number in numbers]
print(squares)
```
列表解析基础语法如下：
```py
[output_expression for element in list]
# 其等价于：
output_list = []
for element in list:
    output_list.append(output_expression)
```

**带条件列表解析：**

Python 列表理解提供了一个可选的predicate，允许您为列表元素包含在新列表中指定一个条件：
```py
[output_expression for element in list if condition]
```
示例：
```py
mountains = [
    ['Makalu', 8485],
    ['Lhotse', 8516],
    ['Kanchendzonga', 8586],
    ['K2', 8611],
    ['Everest', 8848]
]
highest_mountains = [m for m in mountains if m[1] > 8600]
print(highest_mountains)
```

# 十、可迭代与迭代器

## 1、可以使用for循环的数据类型

- 集合数据类型，如list、tuple、dict、set、str等；
- generator，包括生成器和带yield的generator function。

这些可以直接使用for循环的对象统称为可迭代对象: Iterable

## 2、获取迭代器

要从可迭代器中获取迭代器，可以使用 iter() 函数
```py
colors = ['red', 'green', 'blue']
colors_iter = iter(colors)
```
有了迭代器之后，就可以使用 next() 从可迭代器中获取下一个元素
```py
colors = ['red', 'green', 'blue']
colors_iter = iter(colors)
color = next(colors_iter)
print(color)
```
如果没有更多元素，而又调用 next() 函数，就会出现异常。
```py
colors = ['red', 'green', 'blue']
colors_iter = iter(colors)
color = next(colors_iter)
color = next(colors_iter)
color = next(colors_iter)
# cause an exception
color = next(colors_iter)
print(color)

```

## 3、使用isinstance()

判断一个对象是否是Iterable对象；
```python
>>> from collections import Iterable
>>> isinstance([]， Iterable)
True
>>> isinstance({}， Iterable)
True
>>> isinstance('abc'， Iterable)
True
>>> isinstance((x for x in range(10))， Iterable)
True
>>> isinstance(100， Iterable)
False
```

- 可以被next()函数调用并不断返回下一个值的对象称为迭代器：`Iterator [是惰性序列]`：可以使用isinstance()判断一个对象是否是Iterator对象

- 生成器都是 Iterator 对象，但 list、dict、str 虽然是 Iterable，却不是 Iterator，但是可以使用 iter() 函数把 list、dict、str 等 Iterable 变成 Iterator：如果函数返回一个:Iterator，可以使用 函数:list()，tuple()等列出其数据

- 为什么 list、dict、str 等数据类型不是Iterator

	因为 Python 的 Iterator 对象表示的是一个数据流，Iterator对象可以被 next()函数调用并不断返回下一个数据，直到没有数据时抛出 StopIteration 错误。可以把这个数据流看做是一个有序序列，但我们却不能提前知道序列的长度，只能不断通过next()函数实现按需计算下一个数据，所以Iterator的计算是惰性的，只有在需要返回下一个数据时它才会计算

## 4、Python的for循环本质

Python的for循环本质上就是通过不断调用next()函数实现的

```python
for x in [1， 2， 3， 4， 5]:
	pass
	
# 实际上等价于
# 首先获得Iterator对象:
it = iter([1， 2， 3， 4， 5])
# 循环:
while True:
	try:
		# 获得下一个值:
		x = next(it)
	except StopIteration:
		# 遇到StopIteration就退出循环
		break
```		

# 十一、Dictionary

## 1、基本概念

- Python 字典是键值对的集合，其中每个键都与一个值相关联。
- 键值对中的值可以是数字、字符串、列表、元组，甚至是另一个字典。事实上，您可以使用 Python 中任何有效类型的值作为键值对中的值。
- 键值对中的键必须是不可变的。换句话说，键不能被更改，例如数字、字符串、元组等。

特点：
- dict查找速度快，无论dict有10个元素还是10万个元素，查找速度都一样.而list的查找速度随着元素增加而逐渐下降。
	- dict的缺点是占用内存大，还会浪费很多内容；list正好相反，占用内存小，但是查找速度慢；
	- 由于dict是按 key 查找，所以:在一个dict中，key不能重复
- dict存储的key-value序对是没有顺序的；不能用dict存储有序的集合，dict内存数据的顺序和key的放入顺序无关.
- 作为 key 的元素必须不可变，Python的基本类型如字符串、整数、浮点数都是不可变的，都可以作为 key；但是list是可变的，就不能作为 key；dict的作用是建立一组 key 和一组 value 的映射关系，dict的key是不能重复的
	- 对于不变对象来说，调用对象自身的任意方法，也不会改变该对象自身的内容。
	- 相反，这些方法会创建新的对象并返回，这样，就保证了不可变对象本身永远是不可变的

基本定义：
```py
empty_dict = {}
print(type(empty_dict))
# output
<class 'dict'>
```
花括号`{}` 表示这是一个dict，然后按照 key: value， 写出来即，最后一个 key: value 的逗号可以省略
`len()`----计算集合的大小
```python
d = {
	'Adam': 95，
	'Lisa': 85，
	'Bart': 59
}
```

## 2、访问字典值

### 2.1、使用`d[key]`

可以使用`d[key]`形式来查找对应的 value；
```py
person = {
    'first_name': 'John',
    'last_name': 'Doe',
    'age': 25,
    'favorite_colors': ['blue', 'green'],
    'active': True
}
print(person['first_name'])
print(person['last_name'])
```

***注意: 通过 key 访问 dict 的value，只要 key 存在，dict就返回对应的value，如果key不存在.会直接报错:`KeyError`***

**如何避免避免 `KeyError`**
- 先判断一下 key 是否存在，用 in 操作符：
	```python
	if 'Paul' in d:
		print d['Paul']
	```

### 2.2、使用get

dict提供了一个 get 方法，在Key不存在的时候，返回None；如果键不存在，get() 方法也会返回默认值，方法是将默认值传递给第二个参数：`d.get('Thomas', -1)`
```py
person = {
    'first_name': 'John',
    'last_name': 'Doe',
    'age': 25,
    'favorite_colors': ['blue', 'green'],
    'active': True
}
ssn = person.get('ssn', '000-00-0000')
print(ssn)
```

## 3、添加key-value

由于字典具有动态结构，因此可以随时添加新的键值对，添加方式也很简单：
```py
person['gender'] = 'Famale'
```

## 4、修改value

更新dict：直接赋值，如果存在相同的key，则替换以前的值；
```py
dict[key] = new_value
```

## 5、移除key

删除一个key可以使用如下方式：
```py
del dict[key]
```
也可以调用dict.pop(key)方法，对应的value也会从dict中删除：
```py
person = {
    'first_name': 'John',
    'last_name': 'Doe',
    'age': 25,
    'favorite_colors': ['blue', 'green'],
    'active': True
}
del person['active']
# 或者如下方式：
person.pop('active')
print(person)
```

## 6、迭代

当然可以使用 for循环来处理

### 6.1、迭代key-value

`items()`：把dict对象转换成了包含`tuple`的list，我们对这个list进行迭代，可以同时获得key和value
```py
person = {
    'first_name': 'John',
    'last_name': 'Doe',
    'age': 25,
    'favorite_colors': ['blue', 'green'],
    'active': True
}
print(person.items())
# dict_items([('first_name', 'John'), ('last_name', 'Doe'), ('age', 25), ('favorite_colors', ['blue', 'green']), ('active', True)])
for key, value in person.items():
    print(f'{key} -> {value}')
```

### 6.2、迭代key

keys() 方法返回一个对象，其中包含字典中的键列表
```py
print(person.keys())
# 输出：dict_keys(['first_name', 'last_name', 'age', 'favorite_colors', 'active'])
```
实际上，在循环字典时，默认行为是循环所有键。因此，你不需要使用 keys() 方法。
```py
for key in person:
    print(key)
```

### 6.3、迭代value
	
用 for 循环直接迭代 dict，可以每次拿到dict的一个key，如果希望迭代 dict 的values的，使用`values()方法`：把dict转换成一个包含所有value的list
```python
person = {
    'first_name': 'John',
    'last_name': 'Doe',
    'age': 25,
    'favorite_colors': ['blue', 'green'],
    'active': True
}
for value in person.values():
    print(value)
```

## 7、Dictionary Comprehension

Dictionary Comprehension，即字典解析。通过字典解析，可以在字典上运行 for 循环，对每个项目进行转换或过滤等操作，然后返回一个新的字典。

基础语法如下：
```py
{key:value for (key,value) in dict.items() if condition}
```
该字典理解表达式返回一个新字典，其项由表达式 key: value 指定：
```py
stocks = {
    'AAPL': 121,
    'AMZN': 3380,
    'MSFT': 219,
    'BIIB': 280,
    'QDEL': 266,
    'LVGO': 144
}
new_stocks = {symbol: price * 1.02 for (symbol, price) in stocks.items()}
print(new_stocks)
```
包含表达式：
```py
stocks = {
    'AAPL': 121,
    'AMZN': 3380,
    'MSFT': 219,
    'BIIB': 280,
    'QDEL': 266,
    'LVGO': 144
}
selected_stocks = {s: p for (s, p) in stocks.items() if p > 200}
print(selected_stocks)
```

# 十二、Python Set

## 1、Set类型

- set 持有一系列元素，元素没有重复，而且是无序的，这点和 dict 的 key很像；set会自动去掉重复的元素；
- set中的元素不能更改。例如，它们可以是number、string和tuple，但不能是list或dictionary。
```py
empty_set  = set()
```
创建 set 的方式是调用 set() 并传入一个 list，list的元素将作为set的元素；
```py
set(['A'，'B'，'C'])；
# 或者：
basket = {'Apple'，'Banana'，'Orange'，'Apple'}
```
一个空的set返回 False：
```py
skills = set()
if not skills:
    print('Empty sets are falsy')
```

使用len()获取set中元素的个数：
```py
len(set)
```

## 2、in运算符

要检查set是否包含某个元素，可以使用 in 运算符：
```py
element in set
```
示例：
```py
ratings = {1, 2, 3, 4, 5}
rating = 1
if rating in ratings:
    print(f'The set contains {rating}')
```
元素区分大小写；

## 3、添加元素

添加元素时，用set的add()方法：如果添加的元素已经存在于set中，add()不会报错，但是不会加进去了
```py
set.add(element)
# 示例
skills = {'Python programming', 'Software design'}
skills.add('Problem solving')
print(skills)
```

## 4、删除元素

删除set中的元素时，用set的remove()方法：如果删除的元素不存在set中，remove()会报错；
```py
skills = {'Problem solving', 'Software design', 'Python programming'}
skills.remove('Java')
# Error
# KeyError: 'Java'
```
因此使用remove()前需要判断；

为了更方便，集合有 `discard()` 方法，可以删除元素。如果元素不在列表中，它也不会引发错误
```py
set.discard(element)
```

要从集合中删除并返回一个元素，需要使用 `pop()` 方法
```py
skills = {'Problem solving', 'Software design', 'Python programming'}
skill = skills.pop()
print(skill)
```
由于集合中的元素没有特定的顺序，因此 pop() 方法会从集合中删除一个未指定的元素；

删除所有元素：
```py
set.clear()
```

## 5、冻结set

要使集合不可变，可以使用名为 `frozenset()` 的内置函数。`frozenset()`会从现有的集合返回一个新的不可变集合
```py
skills = {'Problem solving', 'Software design', 'Python programming'}
skills = frozenset(skills)
skills.add('Django')
# Error: AttributeError: 'frozenset' object has no attribute 'add'
```

## 6、迭代

遍历set：直接使用 for 循环可以遍历 set 的元素
```py
s = set(['Adam'， 'Lisa'， 'Bart'])
for name in s:
	print(skill)
```
要访问循环内当前元素的索引，可以使用内置的 enumerate() 函数：
```py
skills = {'Problem solving', 'Software design', 'Python programming'}
for index, skill in enumerate(skills):
    print(f"{index}.{skill}")
```
默认情况下，索引从 0 开始。要改变这种情况，可以将起始值传递给 enumerate() 函数的第二个参数：
```py
skills = {'Problem solving', 'Software design', 'Python programming'}
for index, skill in enumerate(skills, 1):
    print(f"{index}.{skill}")
```
**请注意**：每次运行代码时，都会以不同的顺序获得集合元素。

## 7、Set Comprehension

Set Comprehension，即集合解析，为了使代码更加简洁，Python 提供了如下的集合理解语法：
```py
{expression for element in set if condition}
```
此外，集合解析功能允许您通过 if 子句中的条件选择应用表达式的元素:

**请注意**：集合解析会返回一个新的集合，而不会修改原始集合；

示例：
```py
tags = {'Django', 'Pandas', 'Numpy'}
lowercase_tags = {tag.lower() for tag in tags}
print(lowercase_tags)
```
带条件：
```py
tags = {'Django', 'Pandas', 'Numpy'}
new_tags = {tag.lower() for tag in tags if tag != 'Numpy'}
print(new_tags)
```

## 8、Set Union

**union()方法：**

两个集合的 union 返回一个新集合，其中包含两个集合中的不同元素，基本语法：
```py
new_set = set.union(another_set, ...)
```

**`|`运算符**

Python 为您提供了集合联合运算符 |，它允许您联合两个集合：
```py
new_set = set1 | set2
```
集合联合运算符 (|) 返回一个由集合 1 和集合 2 中不同元素组成的新集合。
```py
s1 = {'Python', 'Java'}
s2 = {'C#', 'Java'}
s = s1 | s2
print(s)
```

**这两者区别：**
- union() 方法接受一个或多个iterables，将iterables转换为集合，然后执行联合；
```py
rates = {1, 2, 3}
ranks = [2, 3, 4]
ratings = rates.union(ranks)
print(ratings)
```
- union 运算符 (`|`) 只允许使用集合，而不能像 union() 方法那样使用迭代；
```py
ratings = rates | ranks
# TypeError: unsupported operand type(s) for |: 'set' and 'list'
```

> 总之，union() 方法接受迭代表，而 union 运算符只允许集合

## 9、Set Intersection

Set Intersection，即集合交集，在 Python 中，您可以使用集合 intersection() 方法或集合相交操作符 (`&`) 来相交两个或多个集合：
```py
new_set = set1.intersection(set2, set3, ...)
new_set = set1 & set2 & set3 & ...
# intersection() 方法和 & 运算符具有相同的性能。
```
两个操作都是返回新的集合；

Set intersection() 方法与 Set 交集运算符 (`&`) 区别：
- Set 交集运算符 (`&`) 只能是 Set；
- intersection() 方法可以是任何 iterables，如字符串、列表和字典。

如果向 intersection() 方法传递 iterables ，它会在交集之前将迭代表转换为集合；不过，如果将集合交集运算符 (&) 用于可迭代表，则会引发错误：
```py
numbers = {1, 2, 3}
scores = [2, 3, 4]
numbers = numbers & scores
print(numbers)
# Output
# TypeError: unsupported operand type(s) for &: 'set' and 'list'
```

## 10、Set Difference

两个集合之间的 difference 会产生一个新的集合，其中包含第一个集合中的元素，而第二个集合中没有这些元素；集合差不是交换式，即反过来结果是不一样的；

在 Python 中，您可以使用集合 difference() 方法或集合差运算符 (`-`) 来查找集合之间的差值：
```py
set1.difference(s2, s3, ...)
```
示例：
```py
s1 = {'Python', 'Java', 'C++'}
s2 = {'C#', 'Java', 'C++'}
s = s1.difference(s2)
print(s) # {'Python'}

s = s2.difference(s1)
print(s) # {'C#'}
```

除了上面的方法之外，Python也提供了差集运算符（`-`）：
```py
s = s1 - s2
```
示例：
```py
s1 = {'Python', 'Java', 'C++'}
s2 = {'C#', 'Java', 'C++'}
s = s1 - s2
print(s)
```

同样的：
- difference() 可以是任何可迭代的对象；
- 差集运算符（`-`）只能应用于 Set

## 11、Symmetric Difference

Symmetric Difference，即对称差集，两个集合之间的对称差是指两个集合中任何一个集合都有的元素集合，但不在它们的交集中

假设有两个集合：
```py
s1 = {'Python', 'Java', 'C++'}
s2 = {'C#', 'Java', 'C++'}
# s1 和 s2 的对称差返回下面的集合：
{'C#', 'Python'}
```
从输出结果中可以清楚地看到，返回集合中的元素要么在 s1 中，要么在 s2 中，但不在它们的交集中

在 Python 中，可以使用集合 symmetric_difference() 方法或对称差运算符 (^) 找出两个或多个集合的对称差。

**symmetric_difference()**，基本语法如下：
```py
new_set = set1.symmetric_difference(set2, set3,...)
```

**对称差运算符 (^)**
```py
new_set = set1 ^ set2 ^...
```

两者的区别：
- symmetric_difference() 方法接受一个或多个可迭代的字符串、列表或字典。如果迭代表不是集合，该方法将把它们转换为集合，然后返回它们的对称差值
- 对称差运算符 (^) 只适用于集合，如果使用到其他不是集合的可迭代对象上，会报错；

## 12、issubset


# 十三、列表生成式

## 1、生成列表

- 要生成`list [1， 2， 3， 4， 5， 6， 7， 8， 9， 10]`，我们可以用`range(1， 11)`：
- 要生成`[1x1， 2x2， 3x3， ...， 10x10]` ==> `[x * x for x in range(1， 11)]`
- 列表生成式:`[x * x for x in range(1， 11)]` 写列表生成式时，把要生成的元素x * x放到前面，后面跟for循环，for循环后面还可以加上if判断可以要生成的数据进行筛选

## 2、复杂表达式

字符串可以通过 `%` 进行格式化，用指定的参数替代 `%s`。字符串的`join()`方法可以把一个 list 拼接成一个字符串

## 3、条件过滤
列表生成式的 for 循环后面还可以加上 if 判断

只想要偶数的平方：`[x * x for x in range(1， 11) if x % 2 == 0]`
还可以使用两层循环，可以生成全排列
```python
>>> [m+n for m in 'ABC' for n in 'XYZ']
['AX'， 'AY'， 'AZ'， 'BX'， 'BY'， 'BZ'， 'CX'， 'CY'， 'CZ']
```

## 4、生成器

如果列表元素可以按照某种算法推算出来，那我们是否可以在循环的过程中不断推算出后续的元素呢？这样就不必创建完整的list，从而节省大量的空间.在Python中，这种一边循环一边计算的机制，称为生成器(Generator)

如何创建一个生成器(Generator)

- 方法1:只要把一个列表生成式的[]改成()，就创建了一个generator
	```python
	>>> L = [x * x for x in range(10)]
	>>> L
	[0， 1， 4， 9， 16， 25， 36， 49， 64， 81]
	>>> g = (x * x for x in range(10))
	>>> g
	<generator object <genexpr> at 0x104feab40>
	# 创建L和g的区别仅在于最外层的[]和()，L是一个list，而g是一个generator。
	# 笨方法:如果要一个一个打印出来，可以通过generator的next()方法：
	```
	正确的方法是使用for循环，因为generator也是可迭代对象

	如果推算的算法比较复杂，用类似列表生成式的for循环无法实现的时候，还可以用函数来实现

	如:名的斐波拉契数列（Fibonacci）定义函数如下:
	```python
	def fib(max):
		n， a， b = 0， 0， 1
		while n < max:
			print b
			a， b = b， a + b # 先计算右边，然后将 b 的值赋给 a， 再将 a+b 的值赋给 b
			n = n + 1
	```
	如何将上述函数转变为生成器：只需要把print b改为yield b就可以了
	```python
	def fib(max):
		n， a， b = 0， 0， 1
		while n < max:
			yield b
			# a， b = b， a + b 等价于：
			temp = a
			a = b
			b = temp + b
			#a， b = b， a + b
			n = n + 1
	```
	如果一个函数定义中包含yield关键字，那么这个函数就不再是一个普通函数，而是一个generator：最难理解的就是generator和函数的执行流程不一样.函数是顺序执行，遇到return语句或者最后一行函数语句就返回。而变成generator的函数，在每次调用next()的时候执行，遇到yield语句返回，再次执行时从上次返回的yield语句处继续执行；

	但是用for循环调用generator时，发现拿不到generator的return语句的返回值。如果想要拿到返回值，必须捕获StopIteration错误，返回值包含在StopIteration的value中
	```python
		while True:
	...     try:
	...         x = next(g)
	...         print('g:'， x)
	...     except StopIteration as e:
	...         print('Generator return value:'， e.value)
	...         break
	```

		
				
				
				
				
				
