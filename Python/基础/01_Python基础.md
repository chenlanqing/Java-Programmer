# 一、python简介

## 1、适合领域

- web网站和各种网站服务；
- 系统工具和脚本
- 把其他语言开放的模块包装；
- 机器学习、AI

## 2、安装python

2.x还是3.x ？

在当前的Python生态中，强烈建议选择Python 3.x（推荐Python 3.7+），Python 2.x已于2020年1月1日停止官方支持，所有主流库和框架均已转向Python 3

## 3、Python解释器

CPython：从Python官方网站下载并安装好Python 2.7后，就直接获得了一个官方版本的解释器：CPython。这个解释器是用C语言开发的，所以叫CPython。在命令行下运行python就是启动CPython解释器。CPython是使用最广的Python解释器。教程的所有代码也都在CPython下执行。

IPython：IPython是基于CPython之上的一个交互式解释器，也就是说，IPython只是在交互方式上有所增强，但是执行Python代码的功能和CPython是完全一样的。好比很多国产浏览器虽然外观不同，但内核其实都是调用了IE。CPython用`>>>`作为提示符，而IPython用`In [序号]`:作为提示符。

PyPy：PyPy是另一个Python解释器，它的目标是执行速度。PyPy采用JIT技术，对Python代码进行动态编译（注意不是解释），所以可以显著提高Python代码的执行速度。绝大部分Python代码都可以在PyPy下运行，但是PyPy和CPython有一些是不同的，这就导致相同的Python代码在两种解释器下执行可能会有不同的结果。如果你的代码要放到PyPy下执行，就需要了解PyPy和CPython的不同点。

Jython：Jython是运行在Java平台上的Python解释器，可以直接把Python代码编译成Java字节码执行。

IronPython：IronPython和Jython类似，只不过IronPython是运行在微软.Net平台上的Python解释器，可以直接把Python代码编译成.Net的字节码。
		
## 4、直接运行py文件

在Mac和Linux上是可以的，方法是在`.py`文件的第一行加上：`#!/usr/bin/env python3`

## 5、Mac中Python安装目录

- Mac系统自带Python路径为`/System/Library/Frameworks/Python.framework/Version` 这里可能会有多个python版本，里面Current存放系统当前python版本，进入`Current/bin`，在终端输入`./python --version`即可查看系统当前python版本（注：若使用python --version命令是查看用户当前python版本而不是系统python版本）

- HomeBrew安装python路径为`/usr/local/Cellar/python` 里面存放HomeBrew所安装版本，进入`2.7.13/bin`，在终端输入`./python --version` 即可查看用户当前使用的python版本。如果使用brew工具正确安装python的情况下，用户当前python版本会是新安装的python

- 系统命令默认路径在`/usr/bin`，用户命令默认路径在`/usr/local/bin`（brew安装的命令默认在这个路径下）。如果存在相同的命令，则会依据`/etc/paths` 文件中的环境变量顺序（前面优先于后面）依次查找，查看环境变量也可以在终端输入`echo $PATH` 查看，遵循左面路径优先于右面路径

# 二、数据类型

## 1、整数

Python可以处理任意大小的整数，当然包括负整数，在Python程序中，整数的表示方法和数学上的写法一模一样；计算机由于使用二进制，所以，有时候用十六进制表示整数比较方便，十六进制用`0x`前缀和`0-9`，`a-f`表示，

例如：`0xff00，0xa5b4c3d2...`

*Python的整数没有大小限制*

## 2、浮点数

- 浮点数也就是小数，之所以称为浮点数，是因为按照科学记数法表示时，一个浮点数的小数点位置是可变的，比如， $1.23 * 10^9$ 和 $12.3 * 10^8$ 是相等的。浮点数可以用数学写法，如 1.23，3.14，-9.01，等等。
- 但是对于很大或很小的浮点数，就必须用科学计数法表示，把10用e替代， $1.23 * 10^9$ 就是 $1.23E^9$，或者 $12.3E^8$ ， $0.000012$ 可以写成 $1.2E^{-5}$ ，等等。
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

- [String Methods](https://www.pythontutorial.net/python-string-methods/)

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

- [sophisticated format rules](https://docs.python.org/3/library/string.html#format-specification-mini-language)
- [格式化字符串](https://rgb-24bit.github.io/blog/2018/python-format-string.html)

> Python 在 3.6 版本中引入了 f-string

f-strings 提供了一种将变量和表达式嵌入字符串字面的方法，其语法比 format() 方法更清晰
```py
name = 'John'
message = f'Hi {name}'
print(message)
```
需要注意的是，Python 在运行时会对 `f-string` 中的表达式进行求值。它会用表达式的值替换 f-string 中的表达式。
```py
name = 'John'
s = F'Hello, {name.upper()}!'
print(s)
# Hello, JOHN!
```
`f-string`中也可以有多个大括号：
```py
first_name = 'John'
last_name = 'Doe'
s = F'Hello, {first_name} {last_name}!'
print(s)
```
上面结果等同于：
```py
first_name = 'John'
last_name = 'Doe'
s = F'Hello, {" ".join((first_name, last_name))}!'
print(s)
```

多个`f-string`：
```py
name = 'John'
website = 'PythonTutorial.net'
# 方式1：
message = (
    f'Hello {name}. '
    f"You're learning Python at {website}." 
)
# 方式2：
message = f'Hello {name}. ' \
          f"You're learning Python at {website}." 
# 方式3：
message = f"""Hello {name}.
You're learning Python at {website}."""
print(message)
```

当求值 f-string 时，Python 会用单个大括号替换双大括号。但是，双大括号并不表示表达式的开始：
```py
s = f'{{1+2}}'
print(s)
# {1+2}
```
带3个大括号的f-string：
```py
s = f'{{{1+2}}}'
print(s)
# {3}
```
在这个示例中，Python 将 {1+2} 作为表达式进行求值，返回 3。此外，它还用单个大括号替换了剩余的双大括号。

**Python f-string 中表达式的求值顺序：** Python 按从左到右的顺序计算 f-string 中的表达式。如果表达式有副作用，这一点就很明显了，比如下面的例子：
```py
def inc(numbers, value):
    numbers[0] += value
    return numbers[0]
numbers = [0]
s = f'{inc(numbers,1)},{inc(numbers,2)}'
print(s)
# 1,3
```

**使用f-string格式化数字**
```py
# 使用 f-string 将整数格式化为十六进制
number = 16
s = f'{number:x}'
print(s)  # 10

# 使用 f-string 将数字格式化为科学记数法
number = 0.01
s = f'{number:e}'
print(s)  # 1.000000e-02

# 如果要在数字开头填充零，可以使用 f-string 格式，如下所示
number = 200
s = f'{number: 06}' # 06 是结果数字字符串的总数，包括前导零
print(s)  # 00200

# 要指定小数位数，也可以使用 f-string
number = 9.98567
s = f'{number: .2f}'
print(s)  # 9.99

# 如果数字过大，可以使用数字分隔符使其更容易读取
number = 400000000000
s = f'{number: ,}'  # also can use _
print(s)  # 400,000,000,000

# 要将数字格式化为百分比，可使用以下 f 字符串格式
number = 0.1259
s = f'{number: .2%}'
print(s)  # 12.59%
s = f'{number: .1%}'
print(s)  # 12.5%
```

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

逆序输出：`str[::-1]`

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
通过 split 切割:
```
In [1]:  a = '123456'
In [4]: a.split('4')
Out[4]: ['123', '56']
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

### 3.5、Raw String

在 Python 中，如果在字符串前加上字母 r 或 R，如 r'...' 和 R'...'，该字符串就会变成原始字符串。与普通字符串不同，原始字符串将反斜线 (\)视为字面字符，为了表示制表符和换行符等特殊字符，Python 使用反斜杠 (\) 来表示转义序列的开始。例如
```py
s = 'lang\tver\nPython\t3'
print(s)
```
但是，raw string 会将反斜杠 (\) 作为字面字符处理。例如:
```py
s = r'lang\tver\nPython\t3'
print(s)
```
raw string和普通字符串一样，反斜线 (\) 表示为双反斜线 (\)：
```py
s1 = r'lang\tver\nPython\t3'
s2 = 'lang\\tver\\nPython\\t3'
print(s1 == s2) # True
```
在正则字符串中，Python 将转义序列视为一个字符：
```py
s = '\n'
print(len(s)) # 1
```
但是，在raw string中，Python 将反斜杠 (\) 计算为一个字符：
```py
s = r'\n'
print(len(s)) # 2
```
由于反斜线 (\) 可以转义单引号 (')或双引号 (")，因此raw string不能以奇数个反斜线结束：
```py
s = r'\'
s = r'\\\'
# 上面都会报错，SyntaxError: EOL while scanning string literal
```

### 3.6、字符串其他操作

str.count 统计：
```py
In [6]: a = '123abcd345234'
In [7]: a.count('a')
Out[7]: 1
In [8]: a.count('3')
Out[8]: 3
```

str.zfill() 左边填充0：
```py
In [9]: b = '4'
In [11]: b.zfill(5)  # 5位，补位
Out[11]: '00004'
```
			
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
- 空list `[]`
- 空元祖 `()`
- 空字典 `{}`

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

`print()`函数也可以跟上多个字符串，用逗号“，”隔开，就可以连成一串输出：`print('Hello World')`

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
- print() 函数默认是换行的，但是也可以自定义：`print(end='\t')`

## 2、输入

如果要获取用户输入的数据(在python交互环境): `input()`，可以让用户输入字符串，并存放到一个变量里
```
>>> name = input()
Michael
==> input('please enter your name: '):可以提示用户输入信息
```
*注意：*

从input()读取的内容永远以字符串的形式返回；如果需要获得输入的为数字，先用`int()`或其他方法把字符串转换为我们想要的类型；
		
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
```py
for index in range(n):
    statement
else:
    # for 循环正常执行之后，会执行该代码；
```
	
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

## 6、match

- [What's new in Python3.10](https://peps.python.org/pep-0634/)

Python 3.10 增加了 match...case 的条件判断，类似Java里的Switch
```py
match subject:
    case <pattern_1>:
        <action_1>
    case <pattern_2>:
        <action_2>
    case pattern3 if condition:
        # 处理pattern3并且满足condition的逻辑
        <action_3>
    case 401|403|404:    # 一个 case 也可以设置多个匹配条件，条件使用 ｜ 隔开
        <action_4>
    case _:    # default
        <action_wildcard>
```

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
- 如果已经有一个list或tuple，要调用可变参数，python允许在list或tuple前面加上 `*` 号，把list或tuple变成可变参数；即解包操作
	```python
	num = [5,7,9,52]
	print(calc(*num))
	```
如果使用了可变参数，就不能添加其他位置的参数：
```py
def add(x, y, *args, z):
    return x + y + sum(args) + z
add(10, 20, 30, 40, 50)
# TypeError: add() missing 1 required keyword-only argument: 'z'
```
要解决这个问题，需要在 *args 参数后使用关键字参数，如下所示：
```py
def add(x, y, *args, z):
    return x + y + sum(args) + z
add(10, 20, 30, 40, z=50)
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
def connect(**kwargs):
    print(type(kwargs))
    print(kwargs)
# <class 'dict'>
# {}
```
在调用该函数时，可以只传入必选参数；如果函数有 `**kwargs` 参数和其他参数，则需要将 **kwargs 放在其他参数之后。否则会出现错误

关键字参数有什么用：它可以扩展函数的功能，利用关键字参数来定义这个函数就能满足注册的需求

和可变参数类似，也可以先组装出一个dict，然后，把该dict转换为关键字参数传进去；
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

同时使用可变参数和关键字参数：
```py
def fn(*args, **kwargs):
    print(args)
    print(kwargs)
fn(1, 2, x=10, y=20)
# (1, 2)
# {'x': 10, 'y': 20}
```
- fn 函数可以接受数量可变的位置参数。Python 会将它们打包成一个元组，并将元组赋值给 args 参数。
- fn 函数也接受数量可变的关键字参数。Python 将把它们打包成字典，并将字典赋值给 kwargs 参数

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
- `extend()`：把别的元素添加到 list 的尾部；
```python
# list.append():把新的元素添加到 list 的尾部
>>>classmates = ['Michael'， 'Bob'， 'Tracy']
>>>classmates.append('Adam')
===>['Michael'， 'Bob'， 'Tracy'， 'Adam']
# list.insert():接受两个参数，第一个参数是索引号，第二个参数是待添加的新元素
>>> classmates.insert(1， 'Jack')
['Michael'， 'Jack'， 'Bob'， 'Tracy'， 'Adam']
```
extend 与 append：
- extend 参数是一个集合时，会将集合的元素拆开放到集合中；
- append 参数是一个集合时，会将该集合作为一个整体放到集合中；
```python
In [1]: a = [4 , 5, 6]
In [2]: b = [7, 8, 9]
In [3]: c = ['a', 'b']
In [4]: a.append(b)
In [5]: a
Out[5]: [4, 5, 6, [7, 8, 9]]
In [6]: c.extend(b)
In [7]: c
Out[7]: ['a', 'b', 7, 8, 9]
In [8]: b
Out[8]: [7, 8, 9]
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
- 列表起始索引为：`0`，结束为`n-1`；起始为：`-n`，结束为 `-1`
		
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

## 13、Tuple解包

回顾一下，可以使用 tuple() 或者 (1,)、`1,`等来表示空或者单元素的tuple；

tuple 解包意味着将元组元素拆分成单个变量。例如
```py
x, y = (1, 2)
# 左边 x,y 表示一个由 x 和 y 两个变量组成的元组；右边也是由两个整数 1 和 2 组成的元组
```
该表达式根据每个元素的相对位置，将右侧的元组元素（1，2）分配给左侧的每个变量（x，y）。
```py
numbers = 10, 20, 30
print(type(numbers))
# <class 'tuple'>
```

**使用解包元组交换两个变量的值**

传统交换两个变量的值一般通过中间变量来处理，在python中可用使用解包元祖方式来处理：
```py
x = 10
y = 20
print(f'x={x}, y={y}')
x, y = y, x
print(f'x={x}, y={y}')
```
交换的关键在于：`x, y = y, x`

**解包错误**
```py
x, y = 10, 20, 30
# ValueError: too many values to unpack (expected 2)
```
出现这个错误的原因是，右侧返回三个值，而左侧只有两个变量。要解决可用按照如下方式：
```py
x, y, _ = 10, 20, 30
```
`_` 变量是 Python 中的一个常规变量。按照惯例，它被称为哑变量，如果有多个，默认是最后一个，比如：
```py
x, y,_ , _= 10, 20, 30, 40
print(x, y, _, _) # 10 20 40 40
```

**使用 `*` 操作符扩展解包**

有时候并不想解包元组中的每一个项目。例如，可能想解包第一个和第二个元素。在这种情况下，可以使用 * 操作符
```py
r, g, *other = (192, 210, 100, 0.5)
192
210
[100, 0.5]
```
请注意，在解包赋值的左侧只能使用一次 `*` 操作符

**右侧使用`*`操作符**

Python 允许在右侧使用 * 操作符，假设有如下两个元祖：
```py
odd_numbers = (1, 3, 5)
even_numbers = (2, 4, 6)
```
下面的示例使用 * 操作符解包这些元组，并将它们合并为一个元组：
```py
numbers = (*odd_numbers, *even_numbers)
print(numbers)
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

```py
a.setdefault('a', [])
```
存在则取值，不存在则插入值，并返回值；

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
{key:expression(key) for (key) in iterator if condition}
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

# 十二、Set

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
copy()函数：是深度拷贝的

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

假设有两个集合 A 和 B。如果集合 A 的所有元素都是集合 B 的元素，那么集合 A 就是集合 B 的子集。集合 A 和集合 B 可以相等。如果集合 A 和集合 B 不相等，则 A 是 B 的真子集；

在 Python 中，您可以使用集合 issubset() 方法来检查一个集合是否是另一个集合的子集：
```py
set_a.issubset(set_b)
```
如果 set_a 是 set_b 的子集，则 issubset() 方法返回 True。否则，返回 False。

根据定义，集合也是自身的子集。下面的示例返回 True：
```py
numbers = {1, 2, 3, 4, 5}
scores = {1, 2, 3}
print(numbers.issubset(scores))
```

除了使用 issubset() 方法，还可以使用子集操作符 (`<=`) 来检查一个集合是否是另一个集合的子集：
```py
set_a <= set_b
```
如果 set_a 是 set_b 的子集，子集运算符 (`<=`) 返回 True。否则，返回 False

**真子集：**`set_a < set_b`：
```py
numbers = {1, 2, 3, 4, 5}
scores = {1, 2, 3}

result = scores < numbers
print(result)  # True

result = numbers < numbers
print(result)  # False
```

## 13、issuperset

假设有两个集合：A 和 B：如果集合 B 的所有元素都是集合 A 的元素，那么集合 A 就是集合 B 的超集。如果集合 A 和集合 B 不相等，则集合 A 是集合 B 的真超集。

在 Python 中，您可以使用集合 issuperset() 方法来检查一个集合是否是另一个集合的超集
```py
set_a.issuperset(set_b)
```
如果 set_a 是 set_b 的超集，则 issuperset() 返回 True。否则返回 False
```py
numbers = {1, 2, 3, 4, 5}
scores = {1, 2, 3}
result = numbers.issuperset(scores)
print(result)
```
> 由于分数集的所有元素都存在于数字集中，因此数字集是分数集的超集。

**超集操作符：**`>=` 运算符确定一个集合是否是另一个集合的超集：
```py
set_a >= set_b
```
如果 set_a 是 set_b 的超集，则 `>=` 运算符返回 True。否则，返回 False;

**检查是否是真超集**：要检查一个集合是否是另一个集合的真超集，可以使用 `>` 运算符：`set_a > set_b`
```py
numbers = {1, 2, 3, 4, 5}
scores = {1, 2, 3}

result = numbers > scores
print(result)  # True

result = numbers > numbers
print(result)  # False
```

## 14、Disjoint Sets

当两个集合没有共同元素时，它们就是不相交的。换句话说，两个不相交集合的交集是空集。

Python isdisjoint() 方法用于检查两个集合是否相交：
```py
set_a.isdisjoint(set_b)
```
如果 set_a 和 set_b 不相交，则 isdisjoint() 方法返回 True。否则，返回 False。

isdisjoint() 方法也接受任何可迭代对象，而不仅仅是集合。如果传递的是列表、元组或字典，isdisjoint() 方法会在检查前将其转换为集合。

# 十三、Loop With Else

## 1、for...else

在 Python 中，for 语句可以有一个可选的 else 子句，下面显示了带有 else 子句的 for 语句的语法：
```py
for item in iterables:
    # process item 
else:
    # statement
```
在这种语法中，只有当循环正常运行时，else 子句才会执行。换句话说，如果循环遇到 break 语句，else 子句不会执行；此外，当 iterables 对象没有项目时，else 子句也会执行。

示例：
```py
people = [{'name': 'John', 'age': 25},
        {'name': 'Jane', 'age': 22},
        {'name': 'Peter', 'age': 30},
        {'name': 'Jenifer', 'age': 28}]
name = input('Enter a name:')

for person in people:
    if person['name'] == name:
        print(person)
        break # 当循环遇到 break 语句时，else 子句不会执行
else:
    print(f'{name} not found!')
```
通过使用 for else 语句，程序无需在循环后使用标志和 if 语句。

## 2、while...else

在 Python 中，while 语句可以有一个可选的 else 子句，下面显示了带有 else 子句的 while 语句的语法：
```py
while condition:
    # code block to run
else:
    # else clause code block
```
当条件变为 False 且循环正常运行时，else 子句将执行。但是，如果循环被 break 或 return 语句提前终止，则 else 子句根本不会执行；

示例：
```py
basket = [
    {'fruit': 'apple', 'qty': 20},
    {'fruit': 'banana', 'qty': 30},
    {'fruit': 'orange', 'qty': 10}
]
fruit = input('Enter a fruit:')
index = 0
while index < len(basket):
    item = basket[index]
    # check the fruit name
    if item['fruit'] == fruit:
        print(f"The basket has {item['qty']} {item['fruit']}(s)")
        found_it = True
        break
    index += 1
else:
    qty = int(input(f'Enter the qty for {fruit}:'))
    basket.append({'fruit': fruit, 'qty': qty})
    print(basket)
```

## 3、模拟do…while

Python 是不支持 do...while 语句的，所以可以使用如下方式来模式：
```py
while True:
    # code block

    # break out of the loop
    if condition
        break
```

示例：
```py
from random import randint
# determine the range
MIN = 0
MAX = 10
# generate a secret number
secret_number = randint(MIN, MAX)
# initialize the attempt
attempt = 0

while True:
    attempt += 1

    input_number = int(input(f'Enter a number between {MIN} and {MAX}:'))

    if input_number > secret_number:
        print('It should be smaller.')
    elif input_number < secret_number:
        print('It should be bigger.')
    else:
        print(f'Bingo! {attempt} attempt(s)')
        break

```

# 十四、类型提示

## 1、什么是类型提示

类似于C/C++，Python 的类型提示为您提供了可选的静态类型，以充分利用静态和动态类型的优点；

比如如下有如下函数定义：
```py
def say_hi(name):
    return f'Hi {name}'
greeting = say_hi('John')
print(greeting)
```
如果加上类型提示：
```py
def say_hi(name: str) -> str:
    return f'Hi {name}'
greeting = say_hi('John')
print(greeting)
```
`name: str` 这个表示：name是str类型，并且函数的返回值也是：str；除 str 类型外，您还可以使用其他内置类型（如 int、float、bool 和字节）进行类型提示；

> 需要注意的是，Python 解释器会完全忽略类型提示。如果向 say_hi() 函数传递一个数字，程序运行时不会出现任何警告或错误
```py
def say_hi(name: str) -> str:
    return f'Hi {name}'
greeting = say_hi(123)
print(greeting) # 这里也不会报错
```
如果为了需要检查语法，需要安装静态类型检查工具；

## 2、静态类型检查工具：mypy

Python 没有官方的静态类型检查工具。目前，最流行的第三方工具是 Mypy。由于 Mypy 是一个第三方软件包，因此需要使用以下 pip 命令来安装它：
```py
pip instal mypy
```
直接运行命令：
```bash
mypy app.py
```
检查上面代码运行结果：
```py
app.py:5: error: Argument 1 to "say_hi" has incompatible type "int"; expected "str"
Found 1 error in 1 file (checked 1 source file)
```
错误说明 say_hi 的参数是 int，而预期类型是 str

## 3、类型提示与类型推断

当定义变量一个时可以指定：
```py
name: str = 'John'
```
name 变量的类型是字符串。如果为 name 变量赋值的值不是字符串，静态类型检查程序会出错。

没有必要为变量添加类型，因为静态类型检查程序通常可以根据分配给变量的值推断类型；在本例中，name 的值是一个字面字符串，因此静态类型检查程序会将 name 变量的类型推断为 str。

## 4、为多个类型添加类型提示

比如，如下的例子：
```py
def add(x, y):
    return x + y
```
数字可以是整数或浮点数。使用该模块为多种类型设置类型提示:
```py
# 首先导入Union from typing 模块
from typing import Union
# 使用联合类型创建包括 int 和 float 的联合类型
def add(x: Union[int, float], y: Union[int, float]) -> Union[int, float]:
    return x + y
```
从 Python 3.10 开始，您可以使用 X | Y 语法创建联合类型：
```py
def add(x: int | float, y: int | float) -> int | float:
    return x + y
```

## 5、类型别名

Python 允许为类型指定别名，并使用别名进行类型提示：
```py
from typing import Union
number = Union[int, float]
def add(x: number, y: number) -> number:
    return x + y
```

## 6、lists/dictionaries/sets类型替身

```py
ratings: list = [1, 2, 3]
ratings = {1: 'Bad', 2: 'average', 3: 'Good'}
```
报错信息：
```py
app.py:3: error: Incompatible types in assignment (expression has type "Dict[int, str]", variable has type "List[Any]")
Found 1 error in 1 file (checked 1 source file)
```

要指定列表、字典和集合中值的类型，可以使用类型模块中的类型别名：
Type Alias	| Built-in Type
-----------|------------
List	|list
Tuple	|tuple
Dict	|dict
Set	|set
Frozenset	|frozenset
Sequence	|For list, tuple, and any other sequence data type.
Mapping	|For dictionary (dict), set, frozenset, and any other mapping data type
ByteString	|bytes, bytearray, and memoryview types.

比如，定义一个list，元素都是integer
```py
from typing import List
ratings: List[int] = [1, 2, 3]
```

## 7、None Type

如果函数没有明确返回值，可以使用 None 来键入返回值。例如
```py
def log(message: str) -> None:
    print(message)
```

# 十五、列表生成式


## 1、生成列表

- 要生成`list [1, 2, 3, 4, 5,6, 7, 8, 9, 10]`，我们可以用`range(1, 11)`：
- 要生成`[1x1， 2x2， 3x3， ...， 10x10]` ==> `[x * x for x in range(1， 11)]`
- 列表生成式:`[x * x for x in range(1, 11)]` 写列表生成式时，把要生成的元素`x * x`放到前面，后面跟for循环，for循环后面还可以加上if判断可以要生成的数据进行筛选

## 2、复杂表达式

字符串可以通过 `%` 进行格式化，用指定的参数替代 `%s`。字符串的`join()`方法可以把一个 list 拼接成一个字符串

## 3、条件过滤

列表生成式的 for 循环后面还可以加上 if 判断

只想要偶数的平方：`[x * x for x in range(1, 11) if x % 2 == 0]`
还可以使用两层循环，可以生成全排列
```python
>>> [m+n for m in 'ABC' for n in 'XYZ']
['AX'， 'AY'， 'AZ'， 'BX'， 'BY'， 'BZ'， 'CX'， 'CY'， 'CZ']
```
如果是 if...else 的话，需要将其放在 for 循环前面，如下：
```py
In [17]: [i if i != 3 else '3' for i in range(1,10)]
Out[17]: [1, 2, '3', 4, 5, 6, 7, 8, 9]
```

## 4、generator

- [Generator](03_Python进阶.md#八generator)

## 5、zip函数

基本语法：
```py
zip(*iterables)
```
zip() 函数接收迭代表（可以是 0 个或多个），将它们聚合成一个元组并返回：
```py
languages = ['Java', 'Python', 'JavaScript']
versions = [14, 3, 6]
result = zip(languages, versions)
print(list(result))
# Output: [('Java', 14), ('Python', 3), ('JavaScript', 6)]
```
zip() 函数根据可迭代对象返回一个元组迭代器。
- 如果不传递任何参数，zip() 返回一个空迭代器
- 如果只传递一个可迭代对象，zip() 会返回一个元组迭代器，每个元组只有一个元素。
- 如果传递了多个可迭代对象，zip() 将返回一个元组迭代器，每个元组都包含来自所有可迭代对象的元素。

如果要获取数据的话，可以通过类似：
```py
print(list(result))
print(dict(result))
```

假设向 zip() 传递了两个迭代表，其中一个包含三个元素，另一个包含五个元素。那么，返回的迭代器将包含三个元组。这是因为迭代器会在最短迭代器用完时停止。所以需要注意数据丢失问题；

## 6、代码示例

生成九九剩法表：
```py
loop = 9
# for循环
for m in range(1, loop +1):
    for n in range(1, m +1):
        print(f"{n} * {m} = {n*m}", end='\t')
    print(end='\n')

# 列表生成式
print("".join([f"{n} * {m} = {n * m}\n" if n == m else f"{n} * {m} = {n * m}\t" for m in range(1, loop + 1) for n in range(1, m + 1)]))
```

# 十六、模块

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
		
## 9、运行模块

在 Python 里，若要通过 `python -m` 命令执行一个包，该包需要包含 `__main__` 模块（即 `__main__.py` 文件）

# 十七、包管理

## 1、pip

- [Python Package Index.](https://pypi.org/)

### 1.1、基本使用

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
# 批量卸载包：下面例子为 批量卸载 openai- 开头的包
pip list | grep 'openai-' | awk '{print $1}' | xargs pip uninstall -y
# 清空当前环境所有的依赖包
pip freeze | sed 's/==.*//; s/@.*//; s/\[.*\]//' | xargs -n1 -r pip uninstall -y
```

**列出软件包的依赖项**

安装软件包时，如果该软件包使用了其他软件包，pip 会安装该软件包及其依赖包，以及依赖包的依赖包，依此类推:
```bash
pip show <package_name>
```

指定镜像源：
```bash
pip install --no-cache-dir -i https://pypi.tuna.tsinghua.edu.cn/simple -r requirements.txt
```
常见镜像源：
- 清华：`pip install **** -i https://pypi.tuna.tsinghua.edu.cn/simple`
- 阿里云：`pip install **** -i https://mirrors.aliyun.com/pypi/simple/`

### 1.2、pip-sync

pip-sync 是 pip-tools 工具集里的一个命令，常用来保证虚拟环境里的依赖版本和 requirements.txt 文件完全一致
```bash
#  安装
pip install pip-tools
# 使用
pip-sync requirements.txt
```
- 安装 requirements.txt 里缺少的包；
- 升级/降级到文件里指定的版本；
- 卸载掉所有文件里未列出的包

## 2、虚拟环境

- [Python 相关环境](../Python环境.md)

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

在一个Python目录下，执行如下命令，创建一个虚拟环境
```bash
python -m venv project_venv
```
运行 project_venv/bin/activate 文件，激活虚拟环境：
```bash
#mac os
source project_venv/bin/activate
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

## 3、uv 和 anaconda

选 UV：
- 如果你主要做纯 Python 项目（比如 Web 开发、小工具），追求速度和简洁。
- 不需要管理复杂的非 Python 依赖。
- 想尝试现代化的工具和工作流。
- 示例命令：uv init myproject, uv add numpy, uv run script.py。

选 Conda：
- 如果你在数据科学、AI 或科学计算领域，需要安装复杂的依赖（比如 TensorFlow、PyTorch 的 GPU 版）。
- 项目需要在不同操作系统间保持一致性。
- 你已经习惯 Conda 或团队依赖它的生态。
- 示例命令：conda create -n myenv python=3.10, conda install numpy。

混合使用：
- 如果你既想要 Conda 的强大依赖管理，又想要 UV 的速度，可以在 Conda 环境里用 UV。
- 方法：先用 Conda 创建环境（`conda create -n myenv python=3.10`），激活后安装 UV（c`onda activate myenv; pip install uv`），然后用 UV 管理 PyPI 包。

## 4、uv

- [Rust编写的Python依赖管理](https://github.com/astral-sh/uv)

基本安装
```bash
# On macOS and Linux.
curl -LsSf https://astral.sh/uv/install.sh | sh
# On Windows.
powershell -ExecutionPolicy ByPass -c "irm https://astral.sh/uv/install.ps1 | iex"
# With pip.
pip install uv
```

### 4.1、基本使用

**初始化一个项目**
```bash
uv init test-uv
```
生成的目录结构：
```bash
test-uv
    ├─ .git
    ├─.gitignore
    ├─.python-version  # 记录了当前项目使用的Python环境版本，可以自行编辑修改
    ├─README.md
    ├─main.py
    ├─pyproject.toml # 项目配置信息，类似于JS中的package.json
```

**运行脚本**
```bash
uv run xxx
```
uv run 的执行逻辑为：
- 检查当前目录中是否存在 `.venv`目录，若不存在则创建新环境；
- 验证环境是否包含脚本所需依赖，如果缺失依赖则自动安装；
- 在当前的虚拟环境中执行命令，不会与其他环境产生冲突；

### 4.2、依赖管理

**添加依赖**

用于安装包并自动更新项目配置文件（pyproject.toml）和锁定文件（uv.lock）
```bash
# 安装最新版包
uv add requests
# 安装指定版本
uv add "flask>=2.0.0"
uv add git+https://github.com/psf/requests.git
```
uv add 可以理解为 uv pip install的增强版，底层同样是利用了pip进行安装，但是uv add额外增加了更新项目配置文件的功能

**remove依赖**

用于卸载包并更新项目配置
```bash
# 同步所有依赖（包括dev）
uv sync
# 仅同步生产依赖
uv sync --production
# 同步并清理多余包
uv sync --clean
```

### 4.3、管理虚拟环境

默认情况下，在工作目录中创建名为 `.venv` 的虚拟环境
```bash
uv venv [OPTIONS] [PATH]
```
如果目标路径中存在虚拟环境，则将删除该虚拟环境，并创建一个新的空虚拟环境；默认使用的 Python 最新版本；
```bash
uv venv --python 3.11.0
uv pip install -r requirements.txt
```

## 5、包冲突

### 解决方案 1：调整 `sys.path` 顺序

在项目入口文件（比如 `main.py`）最开头，强制把 **site-packages 的路径** 提前：
```python
import sys
import site

# 把第三方依赖的路径插到 sys.path 前面
site_packages = site.getsitepackages()[0]
if site_packages not in sys.path:
    sys.path.insert(0, site_packages)

from langchain.globals import set_debug as langchain_set_debug
```

这样 Python 会先去 `site-packages/langchain` 找，而不是你本地的 `langchain` 模块。

---

### 解决方案 2：动态导入

借助 `importlib`，你可以直接指定路径去加载 `site-packages` 里的 `langchain`：

```python
import importlib.util
import site
import os

site_packages = site.getsitepackages()[0]
langchain_path = os.path.join(site_packages, "langchain", "__init__.py")

spec = importlib.util.spec_from_file_location("langchain_official", langchain_path)
langchain = importlib.util.module_from_spec(spec)
spec.loader.exec_module(langchain)

from langchain.globals import set_debug as langchain_set_debug
```
这样你就可以强制从官方库里加载，而不会用到你本地的 `langchain`。


# 十八、异常处理

在 Python 中，主要有两种错误：语法错误和异常。
- 语法错误：比如无效Python代码，Python 解释器会显示发生错误的文件名和行号，以便您修复错误。
- 异常：在 Python 中，执行过程中出现的错误，产生异常的原因主要来自代码执行的环境；

出现异常时，程序不会自动处理，结果就是错误信息；

## 1、错误与异常处理

### 1.1、异常处理机制

语法：
```py
try:
    # code that may cause error
except:
    # handle errors
finally:
	# 
```
示例：
```python
try:
	print('try...')
	r = 10 / 0
	print('result'， r)
except ZeroDivisionError as e: # 处理特定异常信息
	print('Except:'， e)
finally:
	print('finally...')
print('End')
```
当我们认为某些代码可能会出错时，就可以用try来运行这段代码，如果执行出错，则后续代码不会继续执行，而是直接跳转至错误处理代码，即except语句块，执行完except后，如果有finally语句块，则执行finally语句块，至此，执行完毕；

可以处理多个异常：
```py
try:
    # code that may cause an exception
except Exception1 as e1:
    # handle exception
except Exception2 as e2:
    # handle exception
except Exception3 as e3:
    # handle exception 
```
如果希望对某些类型的异常做出相同的响应，可以将它们归类到一个 except 子句中：
```py
try:
    # code that may cause an exception
except (Exception1, Exception2):
    # handle exception
```
此外，如果没有错误发生，可以在except语句块后面加一个else，当没有错误发生时，会自动执行else语句；如果包含 finally 子句，else 子句会在 try 子句之后、finally 子句之前执行
```python
try:
	print('try...')
	r = 10 / int('2')
	print('result:'， r)
except ValueError as e:
	print('ValueError:'， e)
except ZeroDivisionError as e:
	print('ZeroDivisionError:'， e)
else:
	print('no error!')
finally:
	print('finally...')
print('END')
```
python其实也是class，所有的错误类型都继承自BaseException，不需要在每个可能出错的地方去捕获错误，只要在合适的层次去捕获错误就可以了；

将 catch Exception 代码块放在列表末尾，以捕获其他一般错误，也是一种很好的做法：
```py
try:
	# handler code
    print(result)
except ValueError:
    print('Error! Please enter a number for net sales.')
except ZeroDivisionError:
    print('Error! The prior net sales cannot be zero.')
except Exception as error:
    print(error)
```

try...catch...finally可以有三种写法：
```py
# 写法1：
try:
    # code that may cause an exception
except Exception1 as e1:
    # handle exception
except Exception2 as e2:
    # handle exception
except Exception3 as e3:
    # handle exception 

# 写法2：
try:
    # code that may cause an exception
except (Exception1, Exception2):
    # handle exception
finally:
	# final code

# 写法3：通常情况下，在无法处理异常但又想清理资源时会使用该语句。例如，您想关闭已打开的文件
try:
    # the code that may cause an exception
finally:
    # the code that always executes

# 写法4：
try:
    # code that may cause errors
except:
    # code that handle exceptions
else:
    # code that executes when no exception occurs
```

### 1.2、调用堆栈

如果错误没有被捕获，它就会一直往上抛，最后被Python解释器捕获，打印一个错误信息，然后程序退出；

### 1.3、记录错误

如果不捕获错误，自然可以让Python解释器来打印出错误堆栈，但程序也被结束了。既然我们能捕获错误，就可以把错误堆栈打印出来，然后分析错误原因，同时，让程序继续执行下去

Python内置的logging模块可以非常容易地记录错误信息，通过配置，logging还可以把错误记录到日志文件里
```python
# 同样是出错，但程序打印完错误信息后会继续执行，并正常退出：
import logging
def foo(s):
	return 10 / int(s)
def bar(s):
	return foo(s) * 2
def main():
	try:
		bar('0')
	except Exception as e:
		logging.exception(e)
main()
print('END')
```

### 1.4、抛出错误

要抛出错误，首先根据需要，可以定义一个错误的class，选择好继承关系，然后用raise语句抛出一个错误的实例:
```python
class FooError(ValueError):
	pass
def foo(s):
	n = int(s)
	if n == 0:
		raise FooError('invalid value %s' % s)
	return 10 / n
foo('0')
```
只有在必要的时候才定义我们自己的错误类型。如果可以选择Python已有的内置的错误类型(比如ValueError，TypeError)，尽量使用Python内置的错误类型

### 1.5、其他处理方式

```python
def foo(s):
	n = int(s)
	if n==0:
		raise ValueError('invalid value: %s' % s)
	return 10 / n
def bar():
	try:
		foo('0')
	except ValueError as e:
		print('ValueError!')
		raise
bar()
```
- 由于当前函数不知道应该怎么处理该错误，所以，最恰当的方式是继续往上抛，让顶层调用者去处理
- raise语句如果不带参数，就会把当前错误原样抛出.此外，在except中raise一个Error，还可以把一种类型的错误转化成另一种类型

## 2、更多

- [Exceptions](02_Python面向对象.md#八Exceptions)

# 十九、文件与文件夹处理

## 1、读文件

下列代码是读取一个文件到string中：
```py
with open('readme.txt') as f:
    lines = f.readlines()
```
读取一个文件的步骤：
- 首先，使用 `open()` 函数打开一个文本文件供读取；
- 其次，使用文件对象的 `file` `read()`、`readline()` 或 `readlines()` 方法从文本文件中读取文本
- 第三步，使用文件 `close()` 方法关闭文件；

### 1.1、open()函数

open() 函数有多个参数，但是一般只需要关注第一个：
```py
# path_to_file 指定要读取文件的路径
open(path_to_file, mode)
```
当然如果你的程序和需要读取的文件是在同一个文件夹中，只需要填写文件名称；要指定文件路径，即使是在 Windows 系统中，也要使用正斜线（'/'）。

`mode`是一个可选参数。它是一个字符串，用于指定打开文件的模式。下表列出了打开文本文件的可用模式：
- `r`：打开文本文件以读取文本；
- `w`：打开文本文件以写文本；
- `a`：打开文本文件以追加文本

比如你要读取一个文件：`f = open('the-zen-of-python.txt','r')`；

open() 函数返回一个文件对象，你可以用它从文本文件中读取文本。

### 1.2、读取文本的方法

文件对象为您提供了三种从文本文件读取文本的方法：
- `read(size)`：根据可选的大小读取文件的部分内容，并以字符串形式返回。如果省略 size，read() 方法会从上次读取的位置读取，直到文件结束。如果文件已结束，read() 方法将返回空字符串
- `readline()`：从文本文件中读取一行，并以字符串形式返回。如果已到达文件末尾，readline() 将返回空字符串
- `readlines()`：将文本文件的所有行添加到字符串列表中。如果您有一个小文件，但想操作该文件的全部文本，这种方法非常有用

### 1.3、close()方法

在使用 close() 方法关闭文件之前，您打开的文件将一直保持打开状态。一般直接使用：`f.close()`

要在不调用 close() 方法的情况下自动关闭文件，可以使用 with 语句，如下所示：
```py
with open(path_to_file) as f:
    contents = f.readlines()
```

### 1.4、读文件示例

```py
with open('the-zen-of-python.txt') as f:
    [print(line.strip()) for line in f.readlines()]
```
open() 函数返回的文件对象是一个可迭代对象。因此，可以使用 for 循环遍历文本文件的行，如下所示
```py
with open('the-zen-of-python.txt') as f:
    for line in f:
        print(line.strip())
```

### 1.5、读UTF-8文件

要打开 UTF-8 文本文件，需要将 `encoding='utf-8'` 传递给 open() 函数，以指示它期望从文件中获取 UTF-8 字符
```py
with open('quotes.txt', encoding='utf8') as f:
    for line in f:
        print(line.strip())
```
到有些编码不规范的文件，你可能会遇到UnicodeDecodeError，因为在文本文件中可能夹杂了一些非法编码的字符。遇到这种情况，open()函数还接收一个errors参数，表示如果遇到编码错误后如何处理。最简单的方式是直接忽略：
```py
f = open('/Users/michael/gbk.txt'， 'r'， encoding='gbk'， errors='ignore')
```

### 1.6、二进制文件读取

要读取二进制文件，比如图片、视频等等，用'rb'模式打开文件即可
```py
f = open('/Users/michael/test.jpg'， 'rb')
```

## 2、写文件

写文件一般格式：
```py
with open('readme.txt', 'w') as f:
    f.write('readme')
```
写文件的步骤：
- 首先，使用 `open()` 函数打开文本文件以进行写入（或添加）操作
- 其次，使用 `write()` 或 `writelines()` 方法写入文本文件；
- 最后，使用 `close()` 方法关闭文件

**open()函数**

`f = open(file, mode)`
- `file`参数指定了要打开用于写入的文本文件的路径；
- `mode`参数用于指定打开文本文件的模式；

写文件时，mode参数：
- `w`：打开一个文本文件以供书写。如果文件存在，函数会在打开文件后立即截断所有内容。如果文件不存在，函数将创建一个新文件；
- `a`：打开用于添加文本的文本文件。如果文件存在，函数会在文件末尾添加内容；
- `+`：打开文本文件进行更新（读取和写入）。

**写函数：**
- `write()` 方法将字符串写入文本文件。
- `writelines()` 方法会将一系列字符串一次性写入文件。

`writelines()` 方法接受一个可迭代对象，而不仅仅是一个列表，所以可以向 `writelines()` 方法传递一个字符串元组、一个字符串集合等：
```py
f.write('\n')
f.writelines('\n')
```

示例：
```py
lines = ['Readme', 'How to write text files in Python']
with open('readme.txt', 'w') as f:
    for line in lines:
        f.write(line)
        f.write('\n')
# 或者如下方式：
lines = ['Readme', 'How to write text files in Python']
with open('readme.txt', 'w') as f:
    f.writelines(lines)
```

**写UTF-8数据**
```py
quote = '中华人民共和国'
with open('quotes.txt', 'w', encoding='utf-8') as f:
    f.write(quote)
```

## 3、io

### 3.1、StringIO

在内存中读写str，也就是数据读写不一定是文件，也可以在内存中读写；

要把str写入StringIO，我们需要先创建一个StringIO，然后，像文件一样写入即可：
```py
from io import StringIO
f = StringIO()
f.write('hello')
print(f.getvalue())
```
`getvalue()`方法用于获得写入后的str

读取StringIO可以用一个str初始化StringIO，然后像文件一样读取：
```py
from io import StringIO
f = StringIO('Hello!\nHi!\nGoodbye!')
while True:
	s = f.readline()
	if s == '':
		break
	print(s.strip())
```

### 3.2、BytesIO

操作二进制数据，BytesIO实现了在内存中读写bytes，写入的不是str，而是经过UTF-8编码的bytes
```py
from io import BytesIO, StringIO
f = BytesIO()
f.write('中文'.encode('utf-8'))
print(f.getvalue())
b'\xe4\xb8\xad\xe6\x96\x87'

f = BytesIO(b'\xe4\xb8\xad\xe6\x96\x87')
f.read()
b'\xe4\xb8\xad\xe6\x96\x87'
```

## 4、创建文件与判断文件是否存在

### 4.1、创建文件

要创建一个新文本文件，需要使用 open() 函数。open() 函数有很多参数。不过，只需要关注前两个参数：
```py
f = open(path_to_file, mode)
```
mode参数可以是：
- `w`：打开一个文件供写入。如果文件不存在，open() 函数会创建一个新文件。否则，它会覆盖现有文件的内容；
- `x`：打开一个文件以创建独占文件。如果文件存在，open() 函数会引发错误 (FileExistsError)。否则，它将创建文本文件

如果要在指定目录下创建文件，例如 docs/readme.text，则需要在创建文件前确保 docs 目录存在。否则会出现异常
```py
with open('docs/readme.txt', 'w') as f:
    f.write('Create a new text file!')
# IOError: [Errno 2] No such file or directory: 'docs/readme.txt'
```
要解决这个问题，你需要先创建 docs 目录，然后在该文件夹中创建 readme.txt 文件。

如果不想在文本文件已经存在的情况下创建新文件，可以在调用 open() 函数时使用 "x "模式：
```py
with open('readme.txt', 'x') as f:
    f.write('Create a new text file!')
```

### 4.2、判断文件是否存在

处理文件时，在对文件进行读取或写入等其他操作之前，您通常需要检查文件是否存在。为此，可以使用 `os.path` 模块中的 `exists()` 函数或 `pathlib` 模块中的 Path 类中的 `is_file()` 方法

- `os.path.exists()`
```py
from os.path import exists
file_exists = exists(path_to_file)
```
- `Path.is_file()`
```py
from pathlib import Path
path = Path(path_to_file)
path.is_file()
```

示例1：
```py
import os.path
os.path.exists(path_to_file)
```
如果文件存在，`exists()` 函数返回 True。否则，返回 False。

示例2：
```py
from pathlib import Path
path = Path(path_to_file)
path.is_file()
```
```py
from pathlib import Path
path_to_file = 'readme.txt'
path = Path(path_to_file)
if path.is_file():
    print(f'The file {path_to_file} exists')
else:
    print(f'The file {path_to_file} does not exist')
```

## 5、CSV文件处理

CSV 是逗号分隔值的缩写。CSV 文件是一种使用逗号分隔数值的分隔文本文件。CSV 文件由一行或多行组成。每一行是一条数据记录。每条数据记录由一个或多个数值组成，以逗号分隔。此外，CSV 文件的所有行都有相同数量的值。

### 5.1、读取CSV文件

```py
# 导入CSV模块
import csv
# 打开文件，或者使用UTF8编码
f = open('path/to/csv_file') # f = open('path/to/csv_file', encoding='UTF8')
# 读取文件
csv_reader = csv.reader(f)
f.close()
```
`csv_reader` 是 CSV 文件行数的可迭代对象。因此，可以使用 for 循环遍历 CSV 文件的行：
```py
for line in csv_reader:
    print(line)
```
每一行都是一个值列表。要访问每个值，需要使用方括号符号 [] 。第一个值的索引为 0，第二个值的索引为 1，以此类推。
```py
import csv
with open('path/to/csv_file', 'r') as f:
    csv_reader = csv.reader(f)
    for line in csv_reader:
        # process each line
        print(line)
```

一般csv文件包含一个头的行，要分离标题和数据，可以使用 enumerate() 函数获取每一行的索引：
```py
import csv
with open('country.csv', 'r') as f:
    csv_reader = csv.reader(f)
    for line_no, line in enumerate(csv_reader, 1):
        if line_no == 1:
            print('Header:')
            print(line)  # header
            print('Data:')
        else:
            print(line)  # data
```
跳过header另一种方法是使用 `next()` 函数。next() 函数将读者转到下一行。例如
```py
import csv
with open('country.csv', 'r') as f:
    csv_reader = csv.reader(f)

    # skip the first row
    next(csv_reader)

    # show the data
    for line in csv_reader:
        print(line)
```

### 5.2、DictReader

使用 csv.reader() 函数时，可以使用括号符号（如 line[0]、line[1]等）访问 CSV 文件的值。不过，使用 csv.reader() 函数有两个主要限制：
- 从 CSV 文件中访问值的方法并不明显。例如，`line[0]` 隐含地表示国家名称。如果能像 `line['country_name']` 这样访问国家名，会更方便；
- 当 CSV 文件中列的顺序发生变化或添加了新列时，需要修改代码以获取正确的数据；

DictReader 类允许你创建一个类似于普通 CSV 阅读器的对象。但它会将每一行的信息映射到一个字典（dict）中，该字典的键由第一行的值指定。通过使用 DictReader 类，可以访问 `country.csv` 文件中的值，如行['name']、行['area']、行['country_code2']和行['country_code3']。
```py
import csv
with open('country.csv', 'r') as f:
    csv_reader = csv.DictReader(f)
    # skip the header
    next(csv_reader)
    # show the data
    for line in csv_reader:
        print(f"The area of {line['name']} is {line['area']} km2")
```
如果想使用第一行中指定的字段名以外的其他字段名，可以像这样通过向 DictReader() 构造函数传递字段名列表来明确指定字段名：
```py
import csv
fieldnames = ['country_name', 'area', 'code2', 'code3']
with open('country.csv', encoding="utf8") as f:
    csv_reader = csv.DictReader(f, fieldnames)
    next(csv_reader)
    for line in csv_reader:
        print(f"The area of {line['country_name']} is {line['area']} km2")
```

### 5.3、写CSV文件

写CSV文件的一般步骤：
- 使用 open() 函数打开 CSV 文件进行写入（w 模式）。
- 调用 csv 模块的 `writer()` 函数，创建 CSV 写入器对象。
- 通过调用 CSV 写入器对象的 `writerow()` 或 `writerows()（写入多行）` 方法，将数据写入 CSV 文件；
- 一旦完成数据写入，关闭文件。

基本代码：
```py
import csv
# open the file in the write mode
f = open('path/to/csv_file', 'w')
# create the csv writer
writer = csv.writer(f)
# write a row to the csv file
writer.writerow(row)
# close the file
f.close()
```
如果使用 with 语句，就不需要调用 close() 方法来明确关闭文件，这样会更简短：
```py
import csv
# open the file in the write mode
with open('path/to/csv_file', 'w') as f:
    # create the csv writer
    writer = csv.writer(f)

    # write a row to the csv file
    writer.writerow(row)
```
写UTF-8文件：
```py
import csv
# open the file in the write mode
with open('path/to/csv_file', 'w', encoding='UTF8') as f:
    # create the csv writer
    writer = csv.writer(f)
    # write a row to the csv file
    writer.writerow(row)
```

示例：
```py
import csv  
header = ['name', 'area', 'country_code2', 'country_code3']
data = ['Afghanistan', 652090, 'AF', 'AFG']
with open('countries.csv', 'w', encoding='UTF8') as f:
    writer = csv.writer(f)

    # write the header
    writer.writerow(header)
    # write the data
    writer.writerow(data)
```

如果要删除空行，可向 open() 函数传递关键字参数 newline=''，如下所示：
```py
import csv
header = ['name', 'area', 'country_code2', 'country_code3']
data = ['Afghanistan', 652090, 'AF', 'AFG']
with open('countries.csv', 'w', encoding='UTF8', newline='') as f:
    writer = csv.writer(f)
    # write the header
    writer.writerow(header)
    # write the data
    writer.writerow(data)
```

**使用DictWriter**

如果 CSV 文件的每一行都是字典，则可以使用 csv 模块的 DictWriter 类将字典写入 CSV 文件：
```py
import csv
# csv header
fieldnames = ['name', 'area', 'country_code2', 'country_code3']
# csv data
rows = [
    {'name': 'Albania', 'area': 28748, 'country_code2': 'AL', 'country_code3': 'ALB'},
    {'name': 'Algeria', 'area': 2381741, 'country_code2': 'DZ', 'country_code3': 'DZA'},
    {'name': 'American Samoa', 'area': 199, 'country_code2': 'AS', 'country_code3': 'ASM'}
]
with open('countries.csv', 'w', encoding='UTF8', newline='') as f:
    writer = csv.DictWriter(f, fieldnames=fieldnames)
    writer.writeheader()
    writer.writerows(rows)
```

## 6、删除文件

要删除文件，需要使用 os 内置模块的 remove() 函数。例如，下面的代码使用 os.remove() 函数删除 readme.txt 文件
```py
import os
os.remove('readme.txt')
```
如果 readme.txt 文件不存在，os.remove() 函数就会出错：
```py
FileNotFoundError: [Errno 2] No such file or directory: 'readme.txt'
```
为避免出现该错误，可以在删除文件前检查该文件是否存在，就像这样：
```py
import os
filename = 'readme.txt'
if os.path.exists(filename):
    os.remove(filename)
```
或者使用异常处理的方式：
```py
import os
try:
    os.remove('readme.txt')
except FileNotFoundError as e:
    print(e)
```

## 7、文件重命名

要重命名文件，需要使用 `os.rename()` 函数：
```py
os.rename(src,dst)
```
如果 src 文件不存在，`os.rename()` 函数会引发 `FileNotFound` 错误。同样，如果 dst 文件已经存在，`os.rename()` 函数会引发 FileExistsError 错误：
```py
import os
os.rename('readme.txt', 'notes.txt')
```
示例：
```py
import os

try:
    os.rename('readme.txt', 'notes.txt')
except FileNotFoundError as e:
    print(e)
except FileExistsError as e:
    print(e)
```

## 8、目录

### 8.1、获取当前工作目录

当前工作目录是运行 Python 脚本的目录。要获取当前工作目录，可以使用 `os.getcwd()`，如下所示：
```py
import os
cwd = os.getcwd()
print(cwd)
```
要更改当前工作目录，可以使用函数 `os.chdir()`：
```py
import os
os.chdir('/script')
cwd = os.getcwd()
print(cwd)
```

### 8.2、连接和分割路径

要让程序在 Windows、Linux 和 macOS 等平台上运行，需要使用与平台无关的文件和目录路径；Python 提供了一个子模块 `os.path`，其中包含几个有用的函数和常量，用于连接和分割路径；
- `join()` 函数将路径组件连接在一起，并返回带有相应路径分隔符的路径。例如，在 Windows 中使用反斜线 (\)，在 macOS 或 Linux 中使用正斜线 (/)
- `split()` 函数将路径分割为不带路径分隔符的多个部分。

示例：
```py
import os

fp = os.path.join('temp', 'python')
print(fp)  # temp\python (on Windows)

pc = os.path.split(fp)
print(pc)  # ('temp', 'python')
```

### 8.3、判断路径是否为目录

要检查路径是否存在和是否是目录，可以使用函数 `os.path.exists()` 和 `os.path.isdir()` 函数。
```py
import os
dir = os.path.join("C:\\", "temp")
print(dir)
if os.path.exists(dir) or os.path.isdir(dir):
    print(f'The {dir} is a directory')
```

### 8.4、创建目录

要创建新目录，需要使用 `os.mkdir()` 函数。在创建新目录之前，应首先检查目录是否存在:
```py
import os

dir = os.path.join("C:\\", "temp", "python")
if not os.path.exists(dir):
    os.mkdir(dir)
```

### 8.5、重命名目录

要重命名目录，需要使用 os.rename() 函数：
```py
import os
oldpath = os.path.join("C:\\", "temp", "python")
newpath = os.path.join("C:\\", "temp", "python3")
if os.path.exists(oldpath) and not os.path.exists(newpath):
    os.rename(oldpath, newpath)
    print("'{0}' was renamed to '{1}'".format(oldpath, newpath))
```

### 8.6、删除目录

要删除一个目录，可以使用 os.rmdir() 函数，删除目录前也需要判断目录是否存在，如下所示
```py
import os
dir = os.path.join("C:\\","temp","python")
if os.path.exists(dir):
    os.rmdir(dir)
    print(dir + ' is removed.')
```

### 8.7、递归遍历目录

`os.walk()` 函数允许你递归遍历一个目录。`os.walk()` 函数返回根目录、子目录和文件
```py
import os

path = "c:\\temp"
for root, dirs, files in os.walk(path):
    print("{0} has {1} files".format(root, len(files)))
```

## 9、文件列表

如果要列出一个目录中的所有文件进行处理。

os.walk() 函数通过自上而下或自下而上地行走目录树来生成目录中的文件名。os.walk() 函数为目录树中的每个目录生成一个包含三个字段（dirpath、dirnames 和文件名）的元组；

请注意，os.walk() 函数会检查整个目录树。因此，可以用它从根目录的所有目录及其子目录中获取所有文件

假设有一个目录，目录内结构如下：
```
D:\web
├── assets
|  ├── css
|  |  └── style.css
|  └── js
|     └── app.js
├── blog
|  ├── read-file.html
|  └── write-file.html
├── about.html
├── contact.html
└── index.html
```
找出文件中的html文件
```py
import os
path = 'D:\\web'
html_files = []
for dirpath, dirnames, filenames in os.walk(path):
    for filename in filenames:
        if filename.endswith('.html'):
            html_files.append(os.path.join(dirpath, filename))
for html_file in html_files:
    print(html_file)
```

定义可重复使用的列表文件功能：
```py
import os
def list_files(path, extentions=None):
    """ List all files in a directory specified by path
    Args:
        path - the root directory path
        extensions - a iterator of file extensions to include, pass None to get all files.
    Returns:
        A list of files specified by extensions
    """
    filepaths = []
    for root, _, files in os.walk(path):
        for file in files:
            if extentions is None:
                filepaths.append(os.path.join(root, file))
            else:
                for ext in extentions:
                    if file.endswith(ext):
                        filepaths.append(os.path.join(root, file))
    return filepaths

if __name__ == '__main__':
    filepaths = list_files(r'D:\web', ('.html', '.css'))
    for filepath in filepaths:
        print(filepath)
```
上述方法针对文件比较小是相对方便的，但是，当文件数量较多时，返回大量文件列表的内存效率不高。

要解决这个问题，可以使用生成器一次生成每个文件，而不是返回一个列表：
```py
import os
def list_files(path, extentions=None):
    """ List all files in a directory specified by path
    Args:
        path - the root directory path
        extensions - a iterator of file extensions to include, pass None to get all files.
    Returns:
        A list of files specified by extensions
    """
    for root, _, files in os.walk(path):
        for file in files:
            if extentions is None:
                yield os.path.join(root, file)
            else:
                for ext in extentions:
                    if file.endswith(ext):
                        yield os.path.join(root, file)
if __name__ == '__main__':
    filepaths = list_files(r'D:\web', ('.html', '.css'))
    for filepath in filepaths:
        print(filepath)
```

## 10、环境变量

在操作系统中定义的环境变量，全部保存在 `os.environ` 这个变量中，可以直接查看；要获取某个环境变量的值，可以调用os.environ.get('key')；
	
				
				
				
