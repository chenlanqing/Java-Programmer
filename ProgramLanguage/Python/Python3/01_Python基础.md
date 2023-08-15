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

## 11、函数小结

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

# 九、列表

list，Python内置的一种数据类型是列表
```python
classmates = ['Michael'， 'Bob'， 'Tracy']
list(['Apple'，'Orange'])
```
- list是一种有序的集合，可以随时添加和删除其中的元素，list是可变的
- 由于Python是动态语言，所以list中包含的元素并不要求都必须是同一种数据类型；使用len()可以获取list元素的个数
- 按照索引访问list，当索引超出了范围时，Python会报一个IndexError错误；可以以负数作为索引，倒序获取集合的值；`"-1"`表示获取最后一个元素
	`·`classmates[-1] => Tracy`
- list中添加新元素
	```python
	# list.append():把新的元素添加到 list 的尾部
	>>>classmates = ['Michael'， 'Bob'， 'Tracy']
	>>>classmates.append('Adam')
	===>['Michael'， 'Bob'， 'Tracy'， 'Adam']
	# list.insert():接受两个参数，第一个参数是索引号，第二个参数是待添加的新元素
	>>> classmates.insert(1， 'Jack')
	['Michael'， 'Jack'， 'Bob'， 'Tracy'， 'Adam']
	```
- list中删除元素
	- list.pop()：总是删除list的最后一个元素，并且返回最后一个元素:`classmates.pop() ===> 'Adam'`
	- list.pop(index)：删除某个位置上的元素，并返回该元素；`classmates.pop(1) ===> 'Jack'`
- list中替换元素：对list中的某一个索引赋值，就可以直接用新的元素替换掉原来的元素，list包含的元素个数保持不变；
- 对list进行切片:即取一个list部分数据(tuple也可以进行切片操作)
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
		
# 十、tuple类型
tuple类型一旦初始化就不能修改

`classmates = ('Michael'， 'Bob'， 'Tracy')`

- `tuple`是另一种有序的列表，中文翻译为`元组`。`tuple` 和 `list` 非常类似，但是，`tuple`一旦创建完毕，就不能修改了：`t = ('Adam'，'Lisa'，'Bart')`；tuple一旦创建完毕，就不能修改了
- 获取 tuple 元素的方式和 list 是一模一样的，我们可以正常使用 t[0]，t[-1]等索引方式访问元素，但是不能赋值成别的元素
- 创建单元素tuple:

	`t = (1) # ==> 1`：t 不是 tuple ，而是整数1。为什么呢？

	`()`既可以表示tuple，也可以作为括号表示运算的优先级，`(1)`被Python解释器计算出结果 1，导致我们得到的不是tuple，而是整数 1
	Python 规定，单元素 tuple 要多加一个逗号`“,”`，t = (1,) # ==>(1,)`

- `可变`的tuple：

	tuple所谓的“不变”是说：tuple的每个元素，指向永远不变。即指向'a'，就不能改成指向'b'，指向一个list，就不能改成指向其他对象，但指向的这个list本身是可变的！

	理解了“指向不变”后，要创建一个内容也不变的tuple怎么做？那就必须保证tuple的每一个元素本身也不能变，如:
	```python
	t = ('a',， 'b', ['A', 'B'])
	L = t(2)
	L[0] = 'X'
	L[1] = 'Y'
	('a', 'b', ['X', 'Y'])
	```
			

# 十二、Dict 与 Set 类型

## 1、dict类型
`dict([('sape'， 4139)， ('guido'， 4127)， ('jack'， 4098)])`

- 花括号`{}` 表示这是一个dict，然后按照 key: value， 写出来即，最后一个 key: value 的逗号可以省略
	`len()`----计算集合的大小
	```python
	d = {
		'Adam': 95，
		'Lisa': 85，
		'Bart': 59
	}
	```

- 可以使用`d[key]`形式来查找对应的 value；

	***注意: 通过 key 访问 dict 的value，只要 key 存在，dict就返回对应的value.如果key不存在.会直接报错:`KeyError`***

	避免 `KeyError`:
	- 先判断一下 key 是否存在，用 in 操作符：
		```python
		if 'Paul' in d:
			print d['Paul']
		```
	- 使用dict本身提供的一个 get 方法，在Key不存在的时候，返回None，也可以自己指定返回的值：`d.get('Thomas', -1)`
	- 要删除一个key，用pop(key)方法，对应的value也会从dict中删除：

- dict特点
	- dict查找速度快，无论dict有10个元素还是10万个元素，查找速度都一样.而list的查找速度随着元素增加而逐渐下降。
		- dict的缺点是占用内存大，还会浪费很多内容；list正好相反，占用内存小，但是查找速度慢；
		- 由于dict是按 key 查找，所以:在一个dict中，key不能重复
	- dict存储的key-value序对是没有顺序的；不能用dict存储有序的集合，dict内存数据的顺序和key的放入顺序无关.
	- 作为 key 的元素必须不可变，Python的基本类型如字符串、整数、浮点数都是不可变的，都可以作为 key；但是list是可变的，就不能作为 key；dict的作用是建立一组 key 和一组 value 的映射关系，dict的key是不能重复的
		- 对于不变对象来说，调用对象自身的任意方法，也不会改变该对象自身的内容。
		- 相反，这些方法会创建新的对象并返回，这样，就保证了不可变对象本身永远是不可变的

- 更新dict：直接赋值，如果存在相同的key，则替换以前的值；
	
- 迭代dict的value：用 for 循环直接迭代 dict，可以每次拿到dict的一个key，如果希望迭代 dict 的values的

	`values()方法`：把dict转换成一个包含所有value的list
	```python
	d = { 'Adam': 95， 'Lisa': 85， 'Bart': 59 }
	print d.values()
	# dict_values([59， 70， 61， 91， 81])
	for v in d.values():
		print v
	# 85
	# 95
	# 59
	```		
- 迭代dict的 key 和 value

	`items()`：把dict对象转换成了包含tuple的list，我们对这个list进行迭代，可以同时获得key和value
	```python
	d = { 'Adam': 95， 'Lisa': 85， 'Bart': 59 }
	print d.items()
	# dict_items([('E'， 59)， ('C'， 70)， ('D'， 61)， ('A'， 91)， ('B'， 81)])
	for key， value in d.items():
		print key， ':'， value				
	#Lisa : 85
	#Adam : 95
	#Bart : 59
	```

## 2、Set类型

- set 持有一系列元素，元素没有重复，而且是无序的，这点和 dict 的 key很像；set会自动去掉重复的元素；
- 创建 set 的方式是调用 set() 并传入一个 list，list的元素将作为set的元素；
	```
	set(['A'，'B'，'C'])；
	或者：
	basket = {'Apple'，'Banana'，'Orange'，'Apple'}
	```
- 获取set元素: 访问 set中的某个元素实际上就是判断一个元素是否在set中：`'A' in set`；元素区分大小写；
- set的特点
	- set的内部结构和dict很像，唯一区别是不存储value；
	- set存储的元素和dict的key类似，必须是不变对象；
	- set存储的元素也是没有顺序的
- 遍历set：直接使用 for 循环可以遍历 set 的元素
	```
	s = set(['Adam'， 'Lisa'， 'Bart'])
	for name in s:
	```
- 更新set集合：
	- 添加元素时，用set的add()方法：如果添加的元素已经存在于set中，add()不会报错，但是不会加进去了
	- 删除set中的元素时，用set的remove()方法：如果删除的元素不存在set中，remove()会报错；因此使用remove()前需要判断；
		
# 十四、切片

## 1、list 或 tuple 切片

```python
L = ['Michael'， 'Sarah'， 'Tracy'， 'Bob'， 'Jack']
L[0:3] ==>  ['Michael'， 'Sarah'， 'Tracy'] # 从索引0开始取，直到索引3为止
切片操作十分有用。我们先创建一个0-99的数列：
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

## 2、切片操作符
切片操作符中的第一个数（冒号之前）表示切片开始的位置，第二个数（冒号之后）表示切片到哪里结束，第三个数（冒号之后）表示切片间隔数。	

如果不指定第一个数，Python就从序列首开始。

如果没有指定第二个数，则Python会停止在序列尾。

注意，返回的序列从开始位置开始，刚好在结束位置之前结束.即开始位置是包含在序列切片中的，而结束位置被排斥在切片外。

`shoplist[1:3]`返回从位置1开始，包括位置2，但是停止在位置3的一个序列切片，因此返回一个含有两个项目的切片。类似地，`shoplist[:]`返回整个序列的拷贝。`shoplist[::3]`返回位置3，位置6，位置9…的序列切片。

你可以用负数做切片。负数用在从序列尾开始计算的位置。例如，`shoplist[:-1]`会返回除了最后一个项目外包含所有项目的序列切片，`shoplist[::-1]`会返回倒序序列切片	

# 十五、迭代

就是对于一个集合，无论该集合是有序还是无序，我们用 for 循环总是可以依次取出集合的每一个元素

- 迭代取出有序集合的索引：使用 `enumerate()` 函数
	```python
	L = ['Adam'， 'Lisa'， 'Bart'， 'Paul']
	for index， name in enumerate(L):
		print index， '-'， name

	#  注意:
		实际上，enumerate() 函数把：
		['Adam'， 'Lisa'， 'Bart'， 'Paul']
		变成了类似：
		[(0， 'Adam')， (1， 'Lisa')， (2， 'Bart')， (3， 'Paul')]
	```
	迭代的每一个元素实际上是一个tuple

	索引迭代也不是真的按索引访问，而是由 enumerate() 函数自动把每个元素变成 (index， element) 这样的tuple，再迭代，就同时获得了索引和元素本身

- 如果一个对象说自己可迭代，那我们就直接用 for 循环去迭代它，可见，迭代是一种抽象的数据操作，它不对迭代对象内部的数据有任何要求。
- 默认情况下，dict迭代的是key。如果要迭代value，可以用`for value in d.values()`，如果要同时迭代key和value，可以用`for k， v in d.items()`；由于字符串也是可迭代对象，因此，也可以作用于for循环

# 十六、列表生成式

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

# 十七、迭代器

## 1、可以使用for循环的数据类型

- 集合数据类型，如list、tuple、dict、set、str等；
- generator，包括生成器和带yield的generator function。

这些可以直接使用for循环的对象统称为可迭代对象: Iterable

## 2、使用isinstance()
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

## 3、Python的for循环本质

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
				
				
				
				
				
