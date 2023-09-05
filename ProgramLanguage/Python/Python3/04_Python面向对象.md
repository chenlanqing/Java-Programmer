
在Python中，所有数据类型都可以视为对象，当然也可以自定义对象。自定义的对象数据类型就是面向对象中的类（Class）的概念

# 1、面向对象：类和实例

- 类：是抽象的模板；
- 实例：根据类创建的一个个具体的对象，每个对象都永远相同的方法，但各自的数据可能不同；

## 1.1、定义类

```python
class Student(object):
	pass
```
`class` 后面紧接着是类名，即Student，类名通常是大写开头的单词，紧接着是(object)，表示该类是从哪个类继承下来的；通常，如果没有合适的继承类，就使用object类，这是所有类最终都会继承的类

## 1.2、创建实例

`bart = Student()`

可以自由的给一个实例变量绑定属性

在创建实例时，可以将认为是必需绑定的属性强制填写进去，通过定义一个特殊的`__init__`方法，绑定属性:
```python
class Student(object):
	def __init__(self， name， score):
		self.name = name
		self.score = score
```
- 注意：`__init__`方法的第一个参数永远是`self`，表示创建的实例本身，在`__init__`方法内部，可以把各种属性绑定到self上
- 如果有`__init__`方法，在创建实例的时候，就不能传入空参数，必需传入与`__init__`方法匹配的参数(self参数除外，Python解释器自己会把实例变量传入)
- 在类中定义的函数只有一点不同，就是第一个参数永远是实例变量self，并且，调用时，不用传递该参数

# 2、数据封装
封装数据的函数和类本身关联起来，这些函数称之为类的方法

- 定义一个方法，除了第一个参数是 self 之外，其他和普通函数一样；要调用一个方法，只需要在实例变了上直接调用，除了 self 不必传入外，其他参数正常传入
		
- 静态语言不同，Python允许对实例变量绑定任何数据，也就是说，对于两个实例变量，虽然它们都是同一个类的不同实例，但拥有的变量名称都可能不同
	
# 3.访问控制

- 要控制内部属性不被外部访问，可以在每个属性名称前加上:`__`，在python中，实例变量名如果以__开头，就变成了一个私有变量，只能在类内部访问，外部不能访问

- 如果外部需要获取类内部的私有实例变量，可以给类增加get_name和get_score这样的方法:
	```python
	class Student(object):
		...
		def get_name(self):
			return self.__name
	```
- 如果要重新修改内部私有变量，可以给类增加类似set_name的方法
	```python
	class Student(object):
		...
		def set_name(self， name):
			self.__name = name
	```

	因为在方法中，可以对参数做检查，避免传入无效的参数：

- 有些时候，你会看到以一个下划线开头的实例变量名，比如_name，这样的实例变量外部是可以访问的，但是，按照约定俗成的规定，当你看到这样的变量时，意思就是，“虽然我可以被访问，但是，请把我视为私有变量，不要随意访问”

- 双下划线开头的实例变量是不是一定不能从外部访问呢？其实也不是。不能直接访问`__name`是因为Python解释器对外把`__name`变量改成了`_Student__name`，
所以，仍然可以通过`_Student__name`来访问`__name`变量：

建议不要这么干，因为不同版本的Python解释器可能会把__name改成不同的变量名。

# 4、继承和多态

在继承关系中，如果一个实例的数据类型是某个子类，那它的数据类型也可以被看做是父类

## 4.1、继承

子类获得了父类的全部功能
```python
class Animal(object):
	def run(self):
		print('Animal is running...')
# 当我们需要编写Dog和Cat类时，就可以直接从Animal类继承：
class Dog(Animal):
	pass

class Cat(Animal):
	pass
```

- 静态语言与动态语言

	- 对于静态语言（例如Java）来说，如果需要传入Animal类型，则传入的对象必须是Animal类型或者它的子类，否则，将无法调用run()方法。
	- 对于Python这样的动态语言来说，则不一定需要传入Animal类型。我们只需要保证传入的对象有一个run()方法就可以了：就是动态语言的“鸭子类型”，它并不要求严格的继承体系，一个对象只要“看起来像鸭子，走起路来像鸭子”，那它就可以被看做是鸭子

# 6、获取对象信息

## 6.1、type()

判断对象类型，返回对应的Class类
- 基本类型判断:
- 如果一个变量指向函数或者类
- 判断一个对象是否为函数：使用types模块中定义的常量`[需要熟悉常用的常量]`‘

## 6.2、isinstance()

可以判断class类型

- 判断class的类型：`isinstance('a'， str) ==? True`
- 判断一个变量是否是某些类型中的一种：`isinstance([1， 2， 3]， (list， tuple)) ==> True`

## 6.3、dir()

获取一个对象的所有属性和方法，返回一个字符串的list

- dir('ABC') ==> 获取一个字符串的所有方法和属性，类似`__xxx__`的属性和方法在Python中都是有特殊用途的，比如`__len__`方法返回长度
- 仅仅把属性和方法列出来是不够的，配合`getattr()、setattr()以及 hasattr()`，我们可以直接操作一个对象的状态：

	- getattr(对象， 属性名称， [default]):获取对象中的属性；如果获取不存在的属性报AttributeError错误，可以添加一个默认之，如果未找到相关属性，则返回默认值；获取对象的方法可以直接赋值给一个变量；
	- setattr(对象， 属性名称，属性值):给对象设置对应的属性及对应的属性值
	- hasattr(对象， 属性名称):判断对象中是否存在对应的属性
- 能不用 getattr()、setattr() 就不使用这两种方法

## 7、实例属性和类属性

- 给实例绑定属性的方法是通过实例变量，或者通过self变量
	```python
	class Student(object):
		def __init__(self， name):
			self.name = name
	s = Student('Bob')
	s.score = 90
	```
	删除实例属性：del s.score

- 在class中定义属性，这种属性是类属性，属性虽然是归类所有，但是类的所有实例都可以访问

- 如果给实例绑定了与类同名的属性，则实例的属性优先级要高于类属性，但是类属性不会消失且不变
	```python
	>>> class Student(object):
	...     name = 'Student'
	...
	>>> s = Student() # 创建实例s
	>>> print(s.name) # 打印name属性，因为实例并没有name属性，所以会继续查找class的name属性
	Student
	>>> print(Student.name) # 打印类的name属性
	Student
	>>> s.name = 'Michael' # 给实例绑定name属性
	>>> print(s.name) # 由于实例属性优先级比类属性高，因此，它会屏蔽掉类的name属性
	Michael
	>>> print(Student.name) # 但是类属性并未消失，用Student.name仍然可以访问
	Student
	>>> del s.name # 如果删除实例的name属性
	>>> print(s.name) # 再次调用s.name，由于实例的name属性没有找到，类的name属性就显示出来了
	Student
	```

# 8、使用`__slots__`

限制实例可以绑定的属性

- 在动态语言中，正常情况下，创建了一个class的实例后，可以给该实例绑定任何属性和方法:
	- 给一个实例绑定方法:
		```python
		>>> def set_age(self， age): # 定义一个函数作为实例方法
		...     self.age = age
		...
		>>> from types import MethodType
		>>> s.set_age = MethodType(set_age， s) # 给实例绑定一个方法
		>>> s.set_age(25) # 调用实例方法
		>>> s.age # 测试结果
		25
		```
	- 但是该方法对其他的实例不起作用，可以给类绑定方法：
		```python
		>>> def set_score(self， score):
		...     self.score = score
		...
		>>> Student.set_score = MethodType(set_score， Student) # 类绑定方法
		```
- 如果需要限制实例的属性，可以在定义class的时候定义一个特殊的`__slots__`变量，来限制该class实例能添加的属性:
	```python
	class Student(object):
		__slots__ = ('name'， 'age')
	```
	当Student的实例绑定的属性非name和age时，就会报AttributeError

需要注意的是：`__slots__`定义的属性仅对当前类实例起作用，对继承的子类是不起作用的：

# 9、使用`@property`:

- 在绑定属性时，如果直接把属性暴露出去，没办法检查参数，导致属性的值可以随意更改，可以通过set_score方法来设置值，可以在set_score里检查参数；比较麻烦
		
- Python内置的@property装饰器就是负责把一个方法变成属性调用的：
	```python
	class Student(object):
		@property
		def score(self):
			return self._score

		@score.setter
		def score(self， value):
			if not isinstance(value， int):
				raise ValueError('score must be an integer!')
			if value < 0 or value > 100:
				raise ValueError('score must between 0 ~ 100!')
			self._score = value
	```
- 把一个getter方法变成属性，只需要加上`@property`就可以了，此时，`@property`本身又创建了另一个装饰器`@score.setter`，负责把一个`setter`方法变成属性赋值，于是，我们就拥有一个可控的属性操作

- 还可以定义只读属性，只定义getter方法，不定义setter方法就是一个只读属性：
	```python
	class Student(object):
		@property
		def birth(self):
			return self._birth
		@birth.setter
		def birth(self， value):
			self._birth = value
		@property
		def age(self):
			return 2015 - self._birth
	```

# 10、多重继承
通过多重继承，一个子类就可以同时获得多个父类的所有功能

Mixln
- 在设计类的继承关系时，通常，主线都是单一继承下来的，例如，Ostrich继承自Bird。但是，如果需要“混入”额外的功能，通过多重继承就可以实现，比如，让Ostrich除了继承自Bird外，再同时继承Runnable。这种设计通常称之为MixIn
- MixIn的目的就是给一个类增加多个功能，这样，在设计类的时候，我们优先考虑通过多重继承来组合多个MixIn的功能，而不是设计多层次的复杂的继承关系

编写一个多进程模式的TCP服务，定义如下:
```python
class MyTCPServer(TCPServer， ForkingMixIn):
	pass
```	

# 11、定制类

形如`__xxx__`的变量或者函数名就要注意，这些在Python中是有特殊用途的，python的class中有很多类似`__slots__`、`__len__()`特殊用途的函数，可以实现定制类；
	
## 11.1、`__str__`

```python
class Student(object):
	def __init__(self， name):
		self.name = name

print(Student('Coco'))  ===>  <__main__.Student object at 0x00000000006AC2E8>
```

- 只需要定义好`__str__()`方法，就可以按照我们想要输出的内容
```python
class Student(object):
	def __init__(self， name):
		self.name = name
	def __str__(self):
		return 'Student object(name: %s)' % self.name
print(Student('Coco'))  ===> Student object(name: Coco)
```
- `s = Student('Coco')`

	直接输出变量s，是调用的`__repr__()`两者的区别是`__str__()`返回用户看到的字符串，而`__repr__()`返回程序开发者看到的字符串，也就是说:`__repr__()`是为调试服务的；
	
	解决办法:通常情况下`__str__()`和`__repr__()`代码都是一样的：`__repr__ = __str__`

## 11.2、`__iter__`

如果一个类要被`for...in`循环，必须实现一个`__iter__()`方法，该方法返回一个迭代对象，Python的for循环就会不断调用该迭代对象的`__next__()`方法拿到循环的下一个值，直到遇到StopIteration错误时退出循环
```python
class Fib(object):
	def __init__(self):
		self.a， self.b = 0， 1
	def __iter__(self): #实例本身就是迭代对象，故返回自己
		return self
	def __next__(self):
		self.a， self.b = self.b， self.a + self.b
		if self.a > 10000:
			raise StopIteration()
		return self.a
for n in Fib():
	print(n)	
```

## 11.3、`__getitem__`

- 上述的迭代对象无法像list那样可以按照索引取出数据，如果需要实现该功能，就需要实现`__getitem__()` 方法
	```python
	class Fib(object):
		def __getitem__(self， n):
			a，b = 1，1
			for x in range(n):
				a，b = b， a+b
			return a
	f = Fib()
	f[1] ==> 1
	```
- list有个切片方法，但对于上述Fib进行切片却报错?
	原因:` __getitem__()` 传入的参数可能是个int也可能是个切片对象，因此可以加上判断
	```python
	class Fib(object):
		def __getitem__(self， n):
			if isinstance(n， int): # n是索引
				a， b = 1， 1
				for x in range(n):
					a， b = b， a + b
				return a
			if isinstance(n， slice): # n是切片
				start = n.start
				stop = n.stop
				if start is None:
					start = 0
				a， b = 1， 1
				L = []
				for x in range(stop):
					if x >= start:
						L.append(a)
					a， b = b， a + b
				return L
	```
- 要正确实现一个`__getitem__()`还是有很多工作要做的
- 与之对应的是`__setitem__()`方法，把对象视作 list 或 dict 来对集合赋值.最后，还有一个`__delitem__()`方法，用于删除某个元素；

## 11.4、`__getattr__`
当我们调用类的方法或属性时，如果不存在，就会报错

- 要避免这种错误，可以给类写一个`__getattr__()`方法，动态返回一个属性:
	```python
	class Student(object):
		def __getattr__(self， attr):
			if attr=='age':
				return lambda: 25
	```
- 只有在没有找到属性的情况下，才调用`__getattr__`，已有的属性
- 注意到任意调用如s.abc都会返回None，这是因为我们定义的`__getattr__`默认返回就是None。
	要让class只响应特定的几个属性，我们就要按照约定，抛出AttributeError的错误
	```python
	class Student(object):
		def __getattr__(self， attr):
			if attr=='age':
				return lambda: 25
			raise AttributeError('\'Student\' object has no attribute \'%s\'' % attr)
	```
- 可以把一个类的所有属性和方法调用全部动态化处理了，不需要任何特殊手段。这种完全动态调用的特性有什么实际作用呢？作用就是，可以针对完全动态的情况作调用

## 11.5、`__call__`

- 一个对象实例可以有自己的属性和方法，当我们调用实例方法时，我们用instance.method()来调用任何类，只需要定义一个`__call__()`方法，就可以直接对实例进行调用
	```python
	class Student(object):
		def __init__(self， name):
			self.name = name
		def __call__(self):
			print('My name is %s.' % self.name)
	s = Student('Coco')
	s()	# self 参数不需要传入，输出结果为:My name is Coco.	
	```
- 对实例进行直接调用，好比对一个函数进行调用，完全可以把对象当成函数，把函数当成对象，这两张没有啥本质区别；如果你把对象看成函数，那么函数本身其实也可以在运行期动态创建出来，因为类的实例都是运行期创建出来的，这么一来，我们就模糊了对象和函数的界限；怎么判断一个变量是函数还是对象？更多时候，判断一个对象是否被调用，能被调用的对象是一个 Callable 对象，即类实现的了`__call__()`方法
	```python
	# 通过callable()函数，我们就可以判断一个对象是否是“可调用”对象
	>>> callable(Student())
	True
	>>> callable(max)
	True
	>>> callable([1， 2， 3])
	False
	>>> callable(None)
	False
	>>> callable('str')
	False
	```

## 12、使用枚举类
Python提供了Enum类来实现这个功能`from enum import Enum`

```python
from enum import Enum
Month = Enum('Month'， ('Jan'， 'Feb'， 'Mar'， 'Apr'， 'May'， 'Jun'， 'Jul'， 'Aug'， 'Sep'， 'Oct'， 'Nov'， 'Dec'))
获取可以直接Month.Jan，也可以枚举其所有成员
for name， member in Month.__members__.items():
	print(name， '=>'， member，'，'，member.value)
# value属性则是自动赋给成员的int常量，默认从1开始计数
```

- 为了更精确的控制枚举类型，可以从Enum派生出自定义类
	```python
	from enum import Enum， unique
	@unique
	class Weekday(Enum):
		Sun = 0 # Sun的value被设定为0
		Mon = 1
		Tue = 2
		Wed = 3
		Thu = 4
		Fri = 5
		Sat = 6
	# @unique装饰器可以帮助我们检查保证没有重复值。
		访问这些枚举类型可以有若干种方法：
		>>> day1 = Weekday.Mon
		>>> print(day1)
		Weekday.Mon
		>>> print(Weekday.Tue)
		Weekday.Tue
		>>> print(Weekday['Tue'])
		Weekday.Tue
		>>> print(Weekday.Tue.value)
		2
		>>> print(day1 == Weekday.Mon)
		True
		>>> print(day1 == Weekday.Tue)
		False
		>>> print(Weekday(1))
		Weekday.Mon
		>>> print(day1 == Weekday(1))
		True
		>>> Weekday(7)
		Traceback (most recent call last):
			...
		ValueError: 7 is not a valid Weekday
		>>> for name， member in Weekday.__members__.items():
		...     print(name， '=>'， member)
		...
		Sun => Weekday.Sun
		Mon => Weekday.Mon
		Tue => Weekday.Tue
		Wed => Weekday.Wed
		Thu => Weekday.Thu
		Fri => Weekday.Fri
		Sat => Weekday.Sat
	```
	可见，既可以用成员名称引用枚举常量，又可以直接根据value的值获得枚举常量
 
 - Enum可以把一组相关常量定义在一个class中，且class不可变，而且成员可以直接比较
	```
		isinstance(Month， Enum) ==> False
		issubclass(Month， Enum) ==> True
		==> Month 不是 Enum 的实例， 而是其子类
	```
# 13、使用元类

## 13.1、type()

- 动态语言和静态语言最大的不同，就是函数和类的定义，不是编译时定义的，而是运行时动态创建的，如:
	```python
	class Hello(object):
		def hello(self， name='world'):
			print('Hello， %s.' % name)
	```
	当Python解释器载入hello模块时，就会依次执行该模块的所有语句，执行结果就是动态创建出一个Hello的class对象，测试如下：
	```python
	>>> from hello import Hello
	>>> h = Hello()
	>>> h.hello()
	Hello， world.
	>>> print(type(Hello))
	<class 'type'>
	>>> print(type(h))
	<class 'hello.Hello'>
	```
- `type()`函数可以查看一个类型或变量的类型，Hello是一个class，它的类型就是type，而h是一个实例，它的类型就是class Hello；class的定义是运行时动态创建的，而创建class的方法就是使用`type()`函数
- `type()`函数既可以返回一个对象的类型，又可以创建出新的类型，可以通过type()函数创建出Hello类，而无需通过class Hello(object)...的定义:
	```python
	>>> def fn(self， name='world'): # 先定义函数
	...     print('Hello， %s.' % name)
	...
	>>> Hello = type('Hello'， (object，)， dict(hello=fn)) # 创建Hello class
	>>> h = Hello()
	>>> h.hello()
	Hello， world.
	>>> print(type(Hello))
	<class 'type'>
	>>> print(type(h))
	<class '__main__.Hello'>
	```
	要创建一个class对象，type()函数依次传入3个参数：
	- class的名称；
	- 继承的父类集合，注意Python支持多重继承，如果只有一个父类，别忘了tuple的单元素写法；
	- class的方法名称与函数绑定，这里我们把函数fn绑定到方法名hello上，type()函数也允许我们动态创建出类来，也就是说，动态语言本身支持运行期动态创建类

## 13.2、metaclass:控制类的创建行为

- 元类(metaclass):当我们定义了类以后，就可以根据这个类创建出实例，所以:先定义类，然后创建实例；要创建出类，那就必须根据metaclass创建出类，所以:先定义metaclass，然后创建类；先定义metaclass，就可以创建类，最后创建实例；
- 创建metaclass:默认习惯，metaclass的类名总是以Metaclass结尾，以便清楚地表示这是一个metaclass，且metaclass是类的模板，所以必须从"type"类型派生：
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	





