
在Python中，所有数据类型都可以视为对象，当然也可以自定义对象。自定义的对象数据类型就是面向对象中的类（Class）的概念
# 一、类和对象

## 1、Python面向对象编程

- 类：是抽象的模板；
- 实例：根据类创建的一个个具体的对象，每个对象都永远相同的方法，但各自的数据可能不同；

### 1.1、定义类

```python
class Person:
    pass
```
`class` 后面紧接着是类名，即Student，类名通常是大写开头的单词，紧接着是(object)，表示该类是从哪个类继承下来的；通常，如果没有合适的继承类，就使用object类，这是所有类最终都会继承的类

### 1.2、创建实例

`person = Person()`

Python 是动态的。这意味着可以在运行时动态地为类的实例添加属性。
```py
person.name = 'John'
```
> 但是，如果创建另一个 Person 对象，新对象将不具有 name 属性


要为类的所有实例定义和初始化属性，需要使用 `__init__` 方法。下面的代码定义了 Person 类的两个实例属性 name 和 age：
```python
class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age
```
创建 Person 对象时，Python 会自动调用 `__init__` 方法来初始化实例属性。在 `__init__` 方法中，self 是 Person 类的实例。
- 注意：`__init__`方法的第一个参数永远是`self`，表示创建的实例本身，在`__init__`方法内部，可以把各种属性绑定到self上
- 如果有`__init__`方法，在创建实例的时候，就不能传入空参数，必需传入与`__init__`方法匹配的参数(self参数除外，Python解释器自己会把实例变量传入)
- 在类中定义的函数只有一点不同，就是第一个参数永远是实例变量self，并且，调用时，不用传递该参数

定义一个实例：
```py
person = Person('John', 25)
person.name
```

### 1.3、实例方法

```py
class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age

    def greet(self):
        return f"Hi, it's {self.name}."

# 调用方法：
person = Person('John', 25)
print(person.greet())
```

### 1.4、类属性

与实例属性不同，类属性由类的所有实例共享：
```py
class Person:
    counter = 0
    def __init__(self, name, age):
        self.name = name
        self.age = age

    def greet(self):
        return f"Hi, it's {self.name}."
```
上述 counter 是 Person 类中的类属性，访问类属性：
```py
Person.counter
# 当然也可以如下方式访问：
person = Person('John',25)
person.counter
```

### 1.5、类方法

与类属性一样，类方法也为类的所有实例所共享。类方法的第一个参数就是类本身。按照惯例，它的名字是 cls。Python 会自动将这个参数传递给类方法。此外，您还可以使用 `@classmethod` 装饰器来装饰一个类方法。

```py
class Person:
    counter = 0
    def __init__(self, name, age):
        self.name = name
        self.age = age
        Person.counter += 1
    def greet(self):
        return f"Hi, it's {self.name}."
    @classmethod
    def create_anonymous(cls):
        return Person('Anonymous', 22)
# 调用
anonymous = Person.create_anonymous()
print(anonymous.name)  # Anonymous
```

### 1.6、静态方法

静态方法不与类或类的任何实例绑定。在 Python 中，我们使用静态方法将逻辑上相关的函数组合到一个类中。要定义静态方法，需要使用 `@staticmethod` 装饰器。
```py
class TemperatureConverter:
    @staticmethod
    def celsius_to_fahrenheit(c):
        return 9 * c / 5 + 32

    @staticmethod
    def fahrenheit_to_celsius(f):
        return 5 * (f - 32) / 9
# 调用
f = TemperatureConverter.celsius_to_fahrenheit(30)
print(f)  # 86
```
> 请注意，Python 不会隐式地传递实例 (self) 和类 (cls) 作为静态方法的第一个参数。


### 1.7、单继承

一个类可以通过继承重用另一个类。子类继承父类时，可以访问父类的属性和方法：
```py
class Employee(Person):
    def __init__(self, name, age, job_title):
        super().__init__(name, age)
        self.job_title = job_title
```
Person 是父类，而 Employee 是子类。要覆盖 Person 类中的 greet() 方法，可以在 Employee 类中定义 greet() 方法，如下所示
```py
class Employee(Person):
    def __init__(self, name, age, job_title):
        super().__init__(name, age)
        self.job_title = job_title

    def greet(self):
        return super().greet() + f" I'm a {self.job_title}."
```

## 2、类和对象

```py
class Person:
    pass

print(person) # <__main__.Person object at 0x000001C46D1C47F0>
print(id(person)) # 1943155787760
print(hex(id(person))) # 0x1c46d1c47f0
print(isinstance(person, Person))  # True
print(Person.__name__) # Person
print(type(Person)) # <class 'type'>
```

## 3、类属性

Python 中的一切都是对象，包括类。换句话说，类就是 Python 中的对象
```py
class HtmlDocument:
   pass
```
HtmlDocument 有`__name__` 属性，并且也有type：
```PY
print(HtmlDocument.__name__) # HtmlDocument
print(type(HtmlDocument))  # <class 'type'>
print(isinstance(HtmlDocument, type)) # True
```
另一种获取类变量值的方法是使用 getattr() 函数。getattr() 函数接受一个对象和一个变量名。它会返回类变量的值
```py
class HtmlDocument:
    extension = 'html'
    version = '5'

extension = getattr(HtmlDocument, 'extension')
version = getattr(HtmlDocument, 'version')
print(extension)  # html
print(version)  # 5
```
设置变量值：
```py
HtmlDocument.version = 10
#
setattr(HtmlDocument, 'version', 10)
```
删除类的属性，有两种方式：
```py
delattr(HtmlDocument, 'version')
del HtmlDocument.version
```
Python 将类变量存储在 `__dict__` 属性中。`__dict__` 是一个映射代理，它是一个字典:
```py
from pprint import pprint
class HtmlDocument:
    extension = 'html'
    version = '5'
HtmlDocument.media_type = 'text/html'
pprint(HtmlDocument.__dict__)
```
输出结果：
```py
mappingproxy({'__dict__': <attribute '__dict__' of 'HtmlDocument' objects>,
              '__doc__': None,
              '__module__': '__main__',
              '__weakref__': <attribute '__weakref__' of 'HtmlDocument' objects>,
              'extension': 'html',
              'media_type': 'text/html',
              'version': '5'})
```
Python 不允许直接修改 __dict__。例如，下面的代码会导致错误：
```py
HtmlDocument.__dict__['released'] = 2008
# TypeError: 'mappingproxy' object does not support item assignment
```
类属性可以是任何对象，如函数：
```py
from pprint import pprint
class HtmlDocument:
    extension = 'html'
    version = '5'

    def render():
        print('Rendering the Html doc...')
pprint(HtmlDocument.__dict__)
```
```py
mappingproxy({'__dict__': <attribute '__dict__' of 'HtmlDocument' objects>,
              '__doc__': None,
              '__module__': '__main__',
              '__weakref__': <attribute '__weakref__' of 'HtmlDocument' objects>,
              'extension': 'html',
              'render': <function HtmlDocument.render at 0x0000010710030310>,
              'version': '5'})
Rendering the Html doc...
```

## 4、实例方法

根据定义，方法是与类的实例绑定的函数：
```py
class Request:
    def send():
        print('Sent')
```
send() 是一个函数对象，它是函数类的一个实例，如以下输出所示：
```py
print(Request.send)
# <function Request.send at 0x000001454F11CAE0>
print(type(Request.send))
# <class 'function'>
```
创建一个Request实例：
```py
http_request = Request()
print(http_request.send)
# <bound method Request.send of <__main__.Request object at 0x0000028BD81A7F50>>
print(type(http_request.send))
# <class 'method'>
```
因此，http_request.send 与 Request.send 并不是一个函数。下面的代码会检查 Request.send 是否与 http_request.send 是同一个对象。它将返回 "false"（假）：
```py
print(type(Request.send) is type(http_request.send)) # False
```
原因是 Request.send 的类型是函数，而 http_request.send 的类型是方法；

> 因此，当你在类中定义一个函数时，它纯粹是一个函数。然而，当您通过对象访问该函数时，函数就变成了方法；因此，方法是与类的实例绑定的函数

如果通过 http_request 对象调用 send() 函数，会出现如下 TypeError 错误：
```py
http_request.send()
# TypeError: Request.send() takes 0 positional arguments but 1 was given
```
因为 http_request.send 是一个绑定到 http_request 对象的方法，所以 Python 总是隐式地将对象作为第一个参数传递给该方法。

重新定义 Request 类：
```py
class Request:
    def send(*args):
        print('Sent', args)
Request.send() # Sent ()
http_request = Request()
http_request.send() # Sent (<__main__.Request object at 0x000001F7C69E7FD0>,)
```
在这种情况下，send() 方法接收的对象是 http_request，也就是它绑定的对象。

http_request 对象与 Python 传递给 send() 方法的作为第一个参数的对象是相同的，因为它们具有相同的内存地址。换句话说，您可以在 send() 方法中访问作为第一个参数的类实例：
```py
http_request.send()
# 等价于
Request.send(http_request)
```

因此，对象的方法总是以对象为第一个参数。按照惯例，它被称为 self：
```py
class Request:
    def send(self):
        print('Sent', self)
```

## 5、`__init__`

当您创建一个类的新对象时，Python 会自动调用 `__init__()` 方法来初始化对象的属性。

与普通方法不同，`__init__()` 方法两边各有两个下划线 (__)。因此，`__init__()` 通常被称为 dunder init。这个名字是双下划线 init 的缩写。方法 `__init__()` 两边的双下划线表示 Python 将在内部使用该方法。换句话说，不应该显示调用该方法

因为Python创建对象是自动调用该方法，所以一般用来初始化一些数据：
```py
class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age
if __name__ == '__main__':
    person = Person('John', 25)
    print(f"I'm {person.name}. I'm {person.age} years old.")
```
创建一个对象时，Python会做如下事情：
- 首先，通过设置对象的命名空间（如 `__dict__` 属性为空 (`{}`)）来创建 Person 类的新实例。
- 其次，调用 `__init__` 方法初始化新创建对象的属性；

> 请注意，`__init__` 方法并不创建对象，而只是初始化对象的属性。因此，`__init__()` 不是构造函数。

如果 `__init__` 有 self 以外的参数，那么在创建新对象时，就需要像上面的例子一样传递相应的参数。否则会出现错误

**带默认参数的`__init__`**
```py
class Person:
    def __init__(self, name, age=22):
        self.name = name
        self.age = age
if __name__ == '__main__':
    person = Person('John')
    print(f"I'm {person.name}. I'm {person.age} years old.")
```
如果没有传age，那么age是有默认值的

## 6、实例变量

在 Python 中，类变量绑定到一个类，而实例变量绑定到类的一个特定实例。实例变量也称为实例属性：
```py
from pprint import pprint
class HtmlDocument:
    version = 5
    extension = 'html'
pprint(HtmlDocument.__dict__)
print(HtmlDocument.extension)
print(HtmlDocument.version)
```
上述 HtmlDocument 有两个类变量：version 和 extension，Python 将这两个变量存储在 `__dict__` 属性中。

创建HtmlDocument的实例：
```py
home = HtmlDocument()
# home是 HtmlDocument 类的一个实例。它有自己的 __dict__ 属性
pprint(home.__dict__)
# {}
```
`home.__dict__` 保存 home 对象的实例变量，就像 `HtmlDocument.__dict__` 保存 HtmlDocument 类的类变量一样。与类的 `__dict__` 属性不同，实例的 `__dict__` 属性的类型是一个dict：
```py
print(type(home.__dict__))
# <class 'dict'>
```
由于字典是可变的，你可以往字典中添加新的数据；

> 另外Python 允许从类的实例访问类变量，在这种情况下，Python 会首先在 `home.__dict__` 中查找 extension 和 version 变量。如果在那里找不到，它就会进入类，在 `HtmlDocument.__dict__` 中查找；但是，如果 Python 可以在实例的 `__dict__` 中找到变量，它就不会在类的 `__dict__` 中进一步查找了

**初始化实例变量**

一般，初始化变量都是在 `__init__` 方法上：
```py
class HtmlDocument:
    version = 5
    extension = 'html'
    def __init__(self, name, contents):
        self.name = name
        self.contents = contents
```

## 7、类方法

实例方法可以访问同一类中的实例变量。要调用实例方法，首先需要创建一个类的实例。
```py
class Person:
    def __init__(self, first_name, last_name, age):
        self.first_name = first_name
        self.last_name = last_name
        self.age = age
    def get_full_name(self):
        return f"{self.first_name} {self.last_name}"
    def introduce(self):
        return f"Hi. I'm {self.first_name} {self.last_name}. I'm {self.age} years old."
```
Person 类有三个实例方法，包括 `__init__()` 、`get_full_name()` 和 `introduce()`。

而类方法不与任何特定实例绑定。它只与类绑定；

定义一个类方法：
- 首先将 `@classmethod` 装饰器放在方法定义的上方。现在， @classmethod 装饰器会将实例方法更改为类方法。
- 其次，将 `self` 参数重命名为 `cls`。`cls` 的意思是类，其指代的就是当前类本身。不过，class 是一个关键字，因此不能用作参数。
```py
class Person:
	...
    @classmethod
    def create_anonymous(cls):
        return Person('John', 'Doe', 25)
```
create_anonymous() 方法不能访问实例属性。但它可以通过 cls 变量访问类属性。

什么时候使用类方法：当一个方法创建了一个类的实例并返回该实例时，该方法被称为工厂方法，一般用于创建实例

## 8、私有属性

封装是面向对象程序设计的四个基本概念之一，包括抽象、封装、继承和多态性；封装是将数据和处理这些数据的函数封装在一个对象中。通过这种方式，可以向外部隐藏对象的内部状态。这就是所谓的信息隐藏；

类就是封装的一个例子。类将数据和方法捆绑成一个整体。类可以通过方法访问其属性；如果你有一个对外不可见的属性，你可以控制对其值的访问，以确保你的对象始终处于有效状态；
```py
class Counter:
    def __init__(self):
        self.current = 0

    def increment(self):
        self.current += 1

    def value(self):
        return self.current

    def reset(self):
        self.current = 0
```
正常封装一个计数器，但是其实还有一个问题，就是仍然可以通过类实例来访问到current：
```py
counter = Counter()
counter.increment()
counter.increment()
counter.current = -999
print(counter.value()) # -999
```
如何避免上述问题，就有了Python的 **私有属性**；

**私有属性**只能通过类的方法访问。换句话说，它们不能从类的外部访问；Python 没有私有属性的概念。换句话说，所有属性都可以从类的外部访问。按照惯例，您可以在定义私有属性时，在其前缀加上一个下划线 (_)：`_attribute`
```py
class Counter:
    def __init__(self):
        self._current = 0
    def increment(self):
        self._current += 1
    def value(self):
        return self._current
    def reset(self):
        self._current = 0
```

**用双下划线混淆名称**：
```py
__attribute
```
Python 将自动把 `__attribute`的名称改为：`_class__attribute`，这在 Python 中称为名称混淆；这样，就不能从类的外部直接访问 `__attribute` 属性，例如：`instance.__attribute`，然而仍然可以使用 `_class__attribute` 名称来访问它：
```py
class Counter:
    def __init__(self):
        self.__current = 0
    def increment(self):
        self.__current += 1
    def value(self):
        return self.__current
    def reset(self):
        self.__current = 0
counter = Counter()
print(counter.__current)
# AttributeError: 'Counter' object has no attribute '__current'
```
但是可以通过如下方式访问：
```py
counter = Counter()
print(counter.__dict__) # {'_Counter__current': 0}
print(counter._Counter__current) # 0
```

- 有些时候，你会看到以一个下划线开头的实例变量名，比如_name，这样的实例变量外部是可以访问的，但是，按照约定俗成的规定，当你看到这样的变量时，意思就是，“虽然我可以被访问，但是，请把我视为私有变量，不要随意访问”
- 双下划线开头的实例变量是不是一定不能从外部访问呢？其实也不是。不能直接访问`__name`是因为Python解释器对外把`__name`变量改成了`_Student__name`，
所以，仍然可以通过`_Student__name`来访问`__name`变量：

## 9、类属性

类属性是定义在类的属性中，是所有类都共享的，类属性可以通过 类 或者 类实例 来访问：
```py
class Circle:
    pi = 3.14159
    def __init__(self, radius):
        self.radius = radius
    def area(self):
        return self.pi * self.radius**2
    def circumference(self):
        return 2 * self.pi * self.radius
c = Circle(10)
print(c.pi)
print(Circle.pi)
```

**类属性是如何工作的**

通过类的实例访问属性时，Python 会在实例属性列表中查找该属性。如果实例属性列表中没有该属性，Python 将继续在类属性列表中查找该属性。只要 Python 在实例属性列表或类属性列表中找到该属性，就会返回该属性的值；

但是，如果访问一个属性，Python 会直接在类属性列表中搜索该属性：
```py
class Test:
    x = 10
    def __init__(self):
        self.x = 20
test = Test()
print(test.x)  # 20
print(Test.x)  # 10
```

**主要用途**
- 存储类常量；
- 数据跟踪；
- 定义默认值

## 10、静态方法

与实例方法不同，静态方法不与对象绑定。换句话说，静态方法不能访问和修改对象状态。此外，Python 不会将 cls 参数（或 self 参数）隐式传递给静态方法。因此，静态方法不能访问和修改类的状态；

在实践中，使用静态方法来定义实用方法或在类中具有某些逻辑关系的分组函数。

要定义静态方法，需要使用 `@staticmethod` 装饰器：
```py
class className:
    @staticmethod
    def static_method_name(param_list):
        pass
# 调用
className.static_method_name()
```

Python 静态方法与类方法对比：
- 类方法默认传递 cls参数；静态方法不会默认传递 cls；
- 类方法可以访问和修改类的状态；静态方法不能访问或修改类的状态。
- 使用 `@classmethod` 装饰器定义类方法；使用 `@staticmethod` 装饰器定义静态方法。

# 二、特殊方法

形如`__xxx__`的变量或者函数名就要注意，这些在Python中是有特殊用途的，python的class中有很多类似`__slots__`、`__len__()`特殊用途的函数，可以实现定制类；
	
## 1、`__str__`

```python
class Person:
    def __init__(self, first_name, last_name, age):
        self.first_name = first_name
        self.last_name = last_name
        self.age = age
person = Person('John', 'Doe', 25)
print(person)
# <__main__.Person object at 0x00000000006AC2E8>
```
在内部，当实例调用 str() 方法时，Python 将自动调用` __str__` 方法。只需要定义好`__str__()`方法，就可以按照我们想要输出的内容
```python
class Person:
    def __init__(self, first_name, last_name, age):
        self.first_name = first_name
        self.last_name = last_name
        self.age = age
    def __str__(self):
        return f'Person({self.first_name},{self.last_name},{self.age})'
person = Person('John', 'Doe', 25)
print(person)
# Person(John,Doe,25)
```

## 2、`__repr__`

`__repr__` 方法定义了将类的实例传递给 repr() 时的行为。
```py
class Person:
    def __init__(self, first_name, last_name, age):
        self.first_name = first_name
        self.last_name = last_name
        self.age = age
person = Person('John', 'Doe', 25)
print(repr(person))
# <__main__.Person object at 0x000001F51B3313A0>
```
输出结果包含人对象的内存地址。要自定义对象的字符串表示，可以像下面这样实现 `__repr__` 方法：
```py
class Person:
    def __init__(self, first_name, last_name, age):
        self.first_name = first_name
        self.last_name = last_name
        self.age = age
    def __repr__(self):
        return f'Person("{self.first_name}","{self.last_name}",{self.age})'
person = Person("John", "Doe", 25)
print(repr(person))
#Person("John","Doe",25)
```
当一个类没有实现 `__str__` 方法时，如果将该类的实例传递给 str()，Python 将返回 `__repr__` 方法的结果，因为 `__str__` 方法内部调用了 `__repr__` 方法：

如果一个类实现了 `__str__` 方法，当您将该类的实例传递给 str() 时，Python 将调用 `__str__` 方法。例如：
```py
class Person:
    def __init__(self, first_name, last_name, age):
        self.first_name = first_name
        self.last_name = last_name
        self.age = age
    def __repr__(self):
        return f'Person("{self.first_name}","{self.last_name}",{self.age})'
    def __str__(self):
        return f'({self.first_name},{self.last_name},{self.age})'

person = Person('John', 'Doe', 25)
# use str()
print(person) # (John,Doe,25)
# use repr()
print(repr(person)) # Person("John","Doe",25)
```

**`__str__` vs `__repr__`** 区别： `__str__` 方法返回一个对象的字符串表示，它是人可读的，而 `__repr__` 方法返回一个对象的字符串表示，它是机器可读的。

## 3、`__eq__`

主要是用来比较两个对象的，如果要比较两个对象，需要重写 `__eq__` 方法：
```py
class Person:
    def __init__(self, first_name, last_name, age):
        self.first_name = first_name
        self.last_name = last_name
        self.age = age
	# 比较两个Person 对象是否一致时，直接比较年龄
    def __eq__(self, other):
        return self.age == other.age
```
当您使用 == 操作符比较类的实例时，Python 会自动调用类的 `__eq__` 方法。如果没有为 `__eq__` 方法提供特定的实现，Python 默认使用 is 操作符。
```py
john = Person('John', 'Doe', 25)
jane = Person('Jane', 'Doe', 25)
print(john == jane)  # True
```
但是如下代码会报错：
```py
john = Person('John', 'Doe', 25)
print(john == 20) # AttributeError: 'int' object has no attribute 'age'
```
要解决这个问题，可以修改 `__eq__` 方法，在访问年龄属性之前检查对象是否是 Person 类的实例：
```py
class Person:
	...
    def __eq__(self, other):
        if isinstance(other, Person):
            return self.age == other.age
        return False
```

## 4、`__hash__`

hash() 函数接受一个对象，并以整数形式返回哈希值。向 hash() 函数传递对象时，Python 将执行对象的 `__hash__` 特殊方法;

这意味着，当您将 p1 对象传递给 hash() 函数时：`hash(p1)`，那么python会调用：`p1.__hash__()`；

默认情况下， `__hash__` 使用对象的标识，如果两个对象相同， `__eq__` 返回 True。要覆盖这一默认行为，可以实现 `__eq__` 和 `__hash__`；如果类重载了 `__eq__` 方法，类中的对象就会变得不可散列。这意味着无法在映射类型中使用这些对象。例如，不能将它们用作字典中的键或集合中的元素。

比如：
```py
class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age
    def __eq__(self, other):
        return isinstance(other, Person) and self.age == other.age
members = {
    Person('John', 22),
    Person('Jane', 22)
}
# TypeError: unhashable type: 'Person'
```
此外，Person 对象会失去散列，因为如果实现了 `__eq__`  ， `__hash__` 就会被设置为 None。例如：
```py
hash(Person('John', 22))
```
要使 Person 类可散列，还需要实现 `__hash__` 方法：
```py
class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age
    def __eq__(self, other):
        return isinstance(other, Person) and self.age == other.age
    def __hash__(self):
        return hash(self.age)
```
现在 Person 类，它支持基于年龄的平等，并且是可散列式的

为使 Person 在字典等数据结构中运行良好，类的哈希值应保持不可变。为此，可以将 Person 类的 age 属性设置为只读属性：
```py
class Person:
    def __init__(self, name, age):
        self.name = name
        self._age = age
    @property
    def age(self):
        return self._age
    def __eq__(self, other):
        return isinstance(other, Person) and self.age == other.age
    def __hash__(self):
        return hash(self.age)
```

## 5、`__bool__`

自定义类的对象与布尔值相关联。默认情况下，布尔值为 True。例如：
```py
class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age
if __name__ == '__main__':
    person = Person('John', 25)
```
要覆盖这一默认行为，需要实现 `__bool__` 特殊方法。 `__bool__` 方法必须返回一个布尔值，即 True 或 False：
```py
class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age

    def __bool__(self):
        if self.age < 18 or self.age > 65:
            return False
        return True
if __name__ == '__main__':
    person = Person('Jane', 16)
    print(bool(person))  # False
```

## 6、`__len__`

如果自定义类没有 `__bool__` 方法，Python 将查找 `__len__`() 方法。如果 `__len__` 为零，对象为 False。否则，对象为 True。如果一个类没有实现 `__bool__` 和 `__len__` 方法，该类的对象将返回 True。
```py
class Payroll:
    def __init__(self, length):
        self.length = length

    def __len__(self):
        print('len was called...')
        return self.length
if __name__ == '__main__':
    payroll = Payroll(0)
    print(bool(payroll))  # False
    payroll.length = 10
    print(bool(payroll))  # True
```
由于 Payroll 类没有重载 `__bool__` 方法，所以 Python 在将 Payroll 的对象求值为布尔值时，会查找 `__len__` 方法;

## 7、`__del__`

在 Python 中，垃圾回收器会自动管理内存。垃圾回收器会销毁未被引用的对象。如果对象实现了 `__del__` 方法，Python 就会在垃圾回收器销毁对象之前调用 `__del__` 方法。但是，垃圾回收器会决定何时销毁对象。因此，它决定何时调用 `__del__` 方法。

`__del__` 有时也被称为类的终结器。请注意， `__del__` 不是析构函数，因为垃圾回收器销毁的是对象，而不是 `__del__` 方法;

**Python的 __del__ 陷阱**

当所有对象引用都消失时，Python 会调用 `__del__` 方法。在大多数情况下，您无法控制它。因此，不应使用 `__del__` 方法来清理资源。建议使用上下文管理器。

如果 `__del__` 包含对对象的引用，垃圾回收器也会在调用 `__del__` 时销毁这些对象。如果 `__del__` 引用了全局对象，可能会产生意想不到的行为。

如果在 `__del__` 方法中出现异常，Python 不会引发异常，而是保持沉默。此外，Python 会将异常信息发送到 stderr。因此，主程序可以在最终处理过程中发现异常。

在实践上，应该避免使用 `__del__`

**示例**
```py
class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age
    def __del__(self):
        print('__del__ was called')
if __name__ == '__main__':
    person = Person('John Doe', 23)
    person = None # 会调用 __del__
	del person # 也会调用 __del__
```

## 8、运算符重载

如果有两个对象，其中某个属性相加生成新的对象，常规做法是定义add 方法：
```py
class Point2D:
    def __init__(self, x, y):
        self.x = x
        self.y = y
    def __str__(self):
        return f'({self.x},{self.y})'
    def add(self, point):
        if not isinstance(point, Point2D):
            raise ValueError('The other must be an instance of the Point2D')
        return Point2D(self.x + point.x, self.y + point.y)
```
但是Python 有更好的实现方法。您可以使用内置运算符 (+) 代替 add() 方法，如下所示：`c = a + b`

在 Point2D 对象上使用 + 运算符时，Python 将调用对象上的特殊方法 `__add__`() 。以下调用是等价的：
```py
c = a + b
c = a.__add__(b)
```
`__add__`() 方法必须返回一个新的 Point2D 对象实例
```py
class Point2D:
    ...
    def __add__(self, point):
        if not isinstance(point, Point2D):
            raise ValueError('The other must be an instance of the Point2D')
        return Point2D(self.x + point.x, self.y + point.y)
if __name__ == '__main__':
    a = Point2D(10, 20)
    b = Point2D(15, 25)
    c = a + b
    print(c)
```
**操作符重载的特殊方法：**

Operator|	Special Methods
------|--------
`+`	| `__add__(self, other)`
`–`	| `__sub__(self, other)`
`*`	| `__mul__(self, other)`
`/`	| `__truediv__(self, other)`
`//`	| `__floordiv__(self, other)`
`%`	| `__mod__(self, other)`
`**`	| `__pow__(self, other)`
`>>`	| `__rshift__(self, other)`
`<<`	| `__lshift__(self, other)`
`&`	| `__and__(self, other)`
`\|`	| `__or__(self, other)`
`^`	| `__xor__(self, other)`

复合运算符重载：

Operator |	Special Method
------|--------
`+=`	|`__iadd__(self, other)`
`-=`	|`__isub__(self, other)`
`*=`	|`__imul__(self, other)`
`/=`	|`__itruediv__(self, other)`
`//=`	|`__ifloordiv__(self, other)`
`%=`	|`__imod__(self, other)`
`**=`	|`__ipow__(self, other)`
`>>=`	|`__irshift__(self, other)`
`<<=`	|`__ilshift__(self, other)`
`&=`	|`__iand__(self, other)`
`\|=`	|`__ior__(self, other)`
`^=`	| `__ixor__(self, other)`

示例：
```py
class Cart:
    def __init__(self):
        self.items = []
    def __iadd__(self, item):
        if not isinstance(item, Item):
            raise ValueError('The item must be an instance of Item')
        self.items.append(item)
        return self
    @property
    def total(self):
        return sum([item.amount for item in self.items])
    def __str__(self):
        if not self.items:
            return 'The cart is empty'
        return '\n'.join([str(item) for item in self.items])
```

## 9、`__iter__`

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

## 10、`__getitem__`

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

## 11、`__getattr__`

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

## 12、`__call__`

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

# 三、Property

## 1、property()

在绑定属性时，如果直接把属性暴露出去，没办法检查参数，导致属性的值可以随意更改，可以通过`set_age`方法来设置值，可以在set_score里检查参数；比较麻烦，但是其存在兼容性问题：
```py
class Person:
    def __init__(self, name, age):
        self.name = name
        self.set_age(age)
    def set_age(self, age):
        if age <= 0:
            raise ValueError('The age must be positive')
        self._age = age
    def get_age(self):
        return self._age
```
property 类返回一个属性对象。property() 类的语法如下
```py
property(fget=None, fset=None, fdel=None, doc=None)
```
property() 有以下参数：
- `fget` 是获取属性值的函数，或 getter 方法。
- `fset` 是设置属性值的函数，或 setter 方法。
- `fdel` 是删除属性的函数。
- `doc` 是 docstring，即注释。

示例：
```py
class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age
    def set_age(self, age):
        if age <= 0:
            raise ValueError('The age must be positive')
        self._age = age
    def get_age(self):
        return self._age
    age = property(fget=get_age, fset=set_age)
```
在 Person 类中，我们通过调用 `property()` 创建一个新的属性对象，并将该属性对象赋值给 age 属性。请注意，age 是类属性，而不是实例属性
```py
print(Person.age)
<property object at 0x000001F5F5149180>
```
创建一个Person实例：
```py
john = Person('John', 18)
print(john.__dict__)
# {'_age': 18, 'name': 'John'}
```
从输出结果中可以清楚地看到，`john.__dict__` 没有 age 属性；执行如下赋值命令：
```py
john.age = 19
```
在本例中，Python 首先查找 `john.__dict__` 中的 age 属性。因为 Python 没有在 `john.__dict__` 中找到 age 属性，所以它会在 `Person.__dict__` 中找到 age 属性。

因为 Python 在 `Person.__dict__` 中找到了 age 属性，所以它会调用 age 属性对象，当执行上面的赋值命令时，Python 将调用分配给 fset 参数的函数，即 `set_age()`，同样，当您读取 age 属性对象时，Python 将执行分配给 fget 参数的函数，即 `get_age()` 方法。

通过使用 `property()` 类，我们可以为类添加属性，同时保持向后兼容性。在实际操作中，首先要定义属性。之后，如果需要，可以将属性添加到类中。

## 2、property装饰器

使用 property() 类处理如下：
```py
class Person:
    def __init__(self, name, age):
        self.name = name
        self._age = age
    def get_age(self):
        return self._age
    age = property(fget=get_age)
```
因此，要获取 Person 对象的age，可以使用 age 属性或 get_age() 方法。这就造成了不必要的冗余；

使用 @property 装饰器，可以简化类的属性定义:
```py
class Person:
    def __init__(self, name, age):
        self.name = name
        self._age = age

    @property
    def age(self):
        return self._age
```

**setter装饰器**
```py
class Person:
    def __init__(self, name, age):
        self.name = name
        self._age = age
    @property
    def age(self):
        return self._age
    def set_age(self, value):
        if value <= 0:
            raise ValueError('The age must be positive')
        self._age = value
    age = age.setter(set_age)
```
setter() 方法接受一个可调用对象，并返回另一个可调用对象（属性对象）。因此，可以在 set_age() 方法中使用装饰器 @age.setter，如下所示：
```py
class Person:
    def __init__(self, name, age):
        self.name = name
        self._age = age
    @property
    def age(self):
        return self._age
    @age.setter
    def set_age(self, value):
        if value <= 0:
            raise ValueError('The age must be positive')
        self._age = value
```

**概括地说，你可以使用以下模式使用装饰器创建属性**
```py
class MyClass:
    def __init__(self, attr):
        self.prop = attr

    @property
    def prop(self):
        return self.__attr

    @prop.setter
    def prop(self, value):
        self.__attr = value
```
在这种模式中，`__attr` 是私有属性，prop 是属性名。

## 3、只读属性

要定义只读属性，需要创建一个只有 getter 的属性。然而，这并不是真正的只读属性，因为你可以随时访问底层属性并对其进行更改。

只读属性在某些情况下非常有用，例如计算属性

## 4、删除属性

要创建一个类的属性，可以使用 `@property` 装饰器。`@property` 装饰器使用的属性类有三个方法：setter、getter 和 deleter。通过使用 deleter，可以删除对象的一个属性。请注意，deleter() 方法删除的是对象的属性，而不是类的属性
```py
from pprint import pprint
class Person:
    def __init__(self, name):
        self._name = name
    @property
    def name(self):
        return self._name
    @name.setter
    def name(self, value):
        if value.strip() == '':
            raise ValueError('name cannot be empty')
        self._name = value
    @name.deleter
    def name(self):
        del self._name
```
调用如下方法可以删除属性：
```py
person = Person('John')
pprint(person.__dict__) # {'_name': 'John'}
del person.name
print(person.name) # AttributeError: 'Person' object has no attribute '_name'
```

# 四、继承

## 1、单继承

```py
class Person:
    def __init__(self, name):
        self.name = name

    def greet(self):
        return f"Hi, it's {self.name}"

class Employee(Person):
    def __init__(self, name, job_title):
        self.name = name
        self.job_title = job_title
```
这是一个单继承，因为 Employee 继承自一个类 (Person)。请注意，Python 也支持多重继承，即一个类继承自多个类;

Employee和Person之间的关系是 IS-A 关系。换句话说，Employee就是一个人。

**type 与 isinstance**
```py
person = Person('Jane')
print(type(person)) # <class '__main__.Person'>

employee = Employee('John', 'Python Developer')
print(type(employee)) # <class '__main__.Employee'>
```
要检查对象是否是类的实例，可以使用 isinstance() 方法：
```py
person = Person('Jane')
print(isinstance(person, Person))  # True
employee = Employee('John', 'Python Developer')
print(isinstance(employee, Person))  # True
print(isinstance(employee, Employee))  # True
print(isinstance(person, Employee))  # False
```

**issubclass**
```py
# 要检查一个类是否是另一个类的子类，可以使用 issubclass() 函数
print(issubclass(Employee, Person)) # True
```
请注意，当定义一个不继承自任何类的类时，它会隐式继承自内置object类。
```py
print(issubclass(Person, object)) # True
```
换句话说，所有类都是object的子类

## 2、重写

重写方法允许子类提供其父类已提供的方法的特定实现方式：
```py
import re
class Parser:
    def __init__(self, text):
        self.text = text

    def email(self):
        match = re.search(r'[a-z0-9\.\-+_]+@[a-z0-9\.\-+_]+\.[a-z]+', self.text)
        if match:
            return match.group(0)
        return None

    def phone(self):
        match = re.search(r'\d{3}-\d{3}-\d{4}', self.text)
        if match:
            return match.group(0)
        return None

    def parse(self):
        return {
            'email': self.email(),
            'phone': self.phone()
        }
class UkParser(Parser):
    def phone(self):
        match = re.search(r'(\+\d{1}-\d{3}-\d{3}-\d{4})', self.text)
        if match:
            return match.group(0)
        return None

if __name__ == '__main__':
    s = 'Contact us via 408-205-5663 or email@test.com'
    parser = Parser(s)
    print(parser.parse())

    s2 = 'Contact me via +1-650-453-3456 or email@test.co.uk'
    parser = UkParser(s2)
    print(parser.parse())
```
UKParser 继承子 Parser，并重写了 phone 方法

除了重写方法还可以重写属性：
```py
import re
class Parser:
    phone_pattern = r'\d{3}-\d{3}-\d{4}'

    def __init__(self, text):
        self.text = text

    def email(self):
        match = re.search(r'[a-z0-9\.\-+_]+@[a-z0-9\.\-+_]+\.[a-z]+', self.text)
        if match:
            return match.group(0)
        return None

    def phone(self):
        match = re.search(self.phone_pattern, self.text)
        if match:
            return match.group(0)
        return None

    def parse(self):
        return {
            'email': self.email(),
            'phone': self.phone()
        }

class UkParser(Parser):
    phone_pattern = r'(\+\d{1}-\d{3}-\d{3}-\d{4})'

if __name__ == '__main__':
    s = 'Contact us via 408-205-5663 or email@test.com'
    parser = Parser(s)
    print(parser.parse())

    s2 = 'Contact me via +1-650-453-3456 or email@test.co.uk'
    parser = UkParser(s2)
    print(parser.parse())
```

## 3、super

如下例子：
```py
class Employee:
    def __init__(self, name, base_pay, bonus):
        self.name = name
        self.base_pay = base_pay
        self.bonus = bonus

    def get_pay(self):
        return self.base_pay + self.bonus

class SalesEmployee(Employee):
    def __init__(self, name, base_pay, bonus, sales_incentive):
        super().__init__(name, base_pay, bonus)
        self.sales_incentive = sales_incentive

    def get_pay(self):
        return self.base_pay + self.bonus + self.sales_incentive
```
super() 会从子类返回父类的引用。

调用父类方法：
```py
class SalesEmployee(Employee):
    def __init__(self, name, base_pay, bonus, sales_incentive):
        super().__init__(name, base_pay, bonus)
        self.sales_incentive = sales_incentive

    def get_pay(self):
        return super().get_pay() + self.sales_incentive
```
使用 super() 可以从子类中调用父类的方法。

## 4、`__slots__`

默认情况下，Python 使用字典来管理实例属性。字典允许您在运行时动态地为实例添加更多属性。然而，它也有一定的内存开销。如果类有许多对象，就会有大量内存开销

为了避免内存开销，Python 引入了插槽。如果一个类只包含固定（或预定）的实例属性，您可以使用槽来指示 Python 使用更紧凑的数据结构，而不是字典

比如一个类只有两个属性，可以使用如下方式：
```py
class Point2D:
    __slots__ = ('x', 'y')

    def __init__(self, x, y):
        self.x = x
        self.y = y
    def __repr__(self):
        return f'Point2D({self.x},{self.y})'
```
使用上述方式之后，下列代码会报错：
```py
point = Point2D(0, 0)
print(point.__dict__) # AttributeError: 'Point2D' object has no attribute __dict__
print(point.__slots__)
```
此外，不能在运行时为实例动态添加更多属性。以下情况将导致错误：
```py
point.z = 0# AttributeError: 'Point2D' object has no attribute 'z'
```
在类实例上添加一个属性：
```py
Point2D.color = 'black'
pprint(Point2D.__dict__)
```
这段代码之所以能运行，是因为 Python 将 `__slots__` 应用于类的实例，而不是类

**`__slots__`和单一继承**
- 基类使用插槽，但子类不使用插槽
```py
class Point2D:
    __slots__ = ('x', 'y')
    def __init__(self, x, y):
        self.x = x
        self.y = y
    def __repr__(self):
        return f'Point2D({self.x},{self.y})'
class Point3D(Point2D):
    def __init__(self, x, y, z):
        super().__init__(x, y)
        self.z = z
if __name__ == '__main__':
    point = Point3D(10, 20, 30)
    print(point.__dict__) # {'z': 30}
```
Point3D 类没有插槽，因此其实例具有 `__dict__` 属性。在这种情况下，子类 Point3D 会使用其基类的插槽（如果可用），并使用实例字典，如果希望 Point3D 类使用插槽，可以像这样定义附加属性：
```py
class Point3D(Point2D):
    __slots__ = ('z',)
    def __init__(self, x, y, z):
        super().__init__(x, y)
        self.z = z
```
> 请注意，不要指定基类的 `__slots__` 中已经指定的属性。

- 基类不使用 `__slots__` ，子类使用使用
    子类的实例同时使用 `__slots__` 和 dictionary 来存储实例属性

## 5、抽象类

Python 并不直接支持抽象类。但它提供了一个模块，允许您定义抽象类；要定义抽象类，需要使用 abc（抽象基类）模块。abc 模块为您提供了定义抽象基类的基础架构：
```py
from abc import ABC
class AbstractClassName(ABC):
    pass
```
要定义抽象方法，可使用 `@abstractmethod` 装饰器：
```py
from abc import ABC, abstractmethod
class AbstractClassName(ABC):
    @abstractmethod
    def abstract_method_name(self):
        pass
```

## 6、协议类

> Python3.8之后具有的

定义一个协议类：
```py
from typing import List, Protocol
class Item(Protocol):
    quantity: float
    price: float
# 定义一个计算总数的方法
def calculate_total(items: List[Item]) -> float:
    return sum([item.quantity * item.price for item in items])
```
这样，可以向 calculate_total() 函数传递任意 Item 对象列表，条件是每个 Item 都有数量和价格两个属性：
```py
class Product:
    def __init__(self, name: str, quantity: float, price: float):
        self.name = name
        self.quantity = quantity
        self.price = price
class Stock:
    def __init__(self, product_name, quantity, price):
        self.product_name = product_name
        self.quantity = quantity
        self.price = price

total = calculate_total([
    Product('A', 10, 150),
    Product('B', 5, 250)
])
total = calculate_total([
    Stock('Tablet', 5, 950),
    Stock('Laptop', 10, 850)
])
```
上面两个类 Product、Stock 都还有属性 quantity、price，符合 Item 协议，都可以作为参数传递到 calculate_total 方法中；

这在 Python 中被称为 duck typing。在duck typing中，对象的行为和属性决定了对象的类型，而不是对象的显式类型。

## 7、多继承

当一个类继承自一个类时，就是单继承。Python 允许一个类从多个类继承。如果一个类继承自两个或更多类，就会有多重继承；要扩展多个类，可以在子类的类名后面的括号（）内指定父类，就像这样：
```py
class ChildClass(ParentClass1, ParentClass2, ParentClass3):
   pass
```
当父类有同名的方法，而子类调用该方法时，Python 会使用方法解析顺序 (MRO) 搜索要调用的正确方法。示例：
```py
class Car:
    def start(self):
        print('Start the Car')
    def go(self):
        print('Going')
class Flyable:
    def start(self):
        print('Start the Flyable object')
    def fly(self):
        print('Flying')
class FlyingCar(Flyable, Car):
    def start(self):
        super().start()
if __name__ == '__main__':
    car = FlyingCar()
    car.start() # Start the Flyable object
```
Python使用`__mro__` 搜索方法：
```py
print(FlyingCar.__mro__)
(<class '__main__.FlyingCar'>, <class '__main__.Flyable'>, <class '__main__.Car'>, <class 'object'>)
```
应该是跟继承的父类类别有关，根据列表的顺序来搜索

多继承中，super() 调用也是跟搜索顺序来的

## 8、mixin

https://www.pythontutorial.net/python-oop/python-mixin/

mixin 是一种提供方法实现的类，可被多个相关的子类重复使用。但是，这种继承并不意味着 "是-a "关系。一个 mixin 并不定义新的类型；

一个 mixin 可以捆绑一组方法供重复使用。每个 mixin 都应具有单一的特定行为，实现密切相关的方法，通常，子类使用多重继承将混合类与父类结合起来，由于 Python 没有定义定义 mixin 类的正式方法，所以用后缀 Mixin 来命名 mixin 类是一个很好的做法。mixin 类就像 Java 和 C# 中的接口一样，具有实现功能。

示例：
```py
class Person:
    def __init__(self, name):
        self.name = name

class Employee(Person):
    def __init__(self, name, skills, dependents):
        super().__init__(name)
        self.skills = skills
        self.dependents = dependents
```

# 五、枚举

## 1、使用枚举

Python 提供了包含 Enum 类型的 enum 模块，用于定义新的枚举。通过子类化 Enum 类来定义新的枚举类型：
```py
from enum import Enum
class Color(Enum):
    RED = 1
    GREEN = 2
    BLUE = 3
```
请注意，枚举的成员是常量。因此，按照惯例，它们的名称都是大写字母。
```py
print(type(Color.RED))  # <enum 'Color'>
print(isinstance(Color.RED, Color)) # True
```

**要检查某个成员是否在枚举中**，可以使用 in 操作符：`if Color.RED in Color`

**要比较两个成员**，可以使用 is 或 == 操作符：`if Color.RED is Color.BLUE`

请注意，成员和其相关值不相等。以下示例返回 False：
```py
if Color.RED == 1:
    print('Color.RED == 1')
else:
    print('Color.RED != 1')
```

**枚举成员总是可以散列的**。这意味着您可以将枚举成员用作字典中的键或集合中的元素：
```py
rgb = {
    Color.RED: '#ff0000',
    Color.GREEN: '#00ff00',
    Color.BLUE: '#0000ff'
}
```

**访问枚举名称和值**，访问枚举成员的典型方法是使用点符号（.），由于 Enum 实现了` __getitem__` 方法，因此也可以使用方括号 `[]` 语法通过成员的名称来获取成员：
```py
print(Color['RED'])
```

**由于枚举是可调用的**，因此可以通过值来获取成员。例如，下面的代码通过值返回颜色枚举中的 RED 成员：
```py
print(Color(1))
```
比如从外面获取到值，调用枚举，如果枚举存在，则可以获取到具体的枚举值，如果不存在，则会报错：
```py
print(Color(4)) # ValueError: 4 is not a valid Color
```

**枚举是可迭代的**，因此可以使用 for 循环对其进行迭代：
```py
for color in Color:
    print(color)
```
请注意，成员的顺序与枚举定义中的相同；

您可以使用 list() 函数从枚举中返回成员列表：`print(list(Color))`

**枚举是不可变的**。这意味着一旦定义了枚举，就不能添加或删除成员。也不能更改成员值

**枚举不能被继承**，除非它不包含任何成员。下面的示例运行正常，因为Color枚举不包含任何成员：
```py
class Color(Enum):
    pass
class RGB(Color):
    RED = 1
    GREEN = 2
    BLUE = 3
```

## 2、枚举别名

根据定义，枚举成员值是唯一的。但是，可以创建具有相同值的不同成员名
```py
from enum import Enum
class Color(Enum):
    RED = 1
    CRIMSON = 1
    SALMON = 1
    GREEN = 2
    BLUE = 3
```
当在一个枚举中定义多个具有相同值的成员时，Python 不会创建不同的成员，而是创建别名。在这个例子中，RED 是主成员，CRIMSON 和 SALMON 是 RED 成员的别名：
```py
print(Color.RED is Color.CRIMSON)
print(Color.RED is Color.SALMON)
```
按值查找成员时，始终会得到主成员，而不是别名。例如，以下语句返回 RED 成员：
```py
print(Color(1)) # Color.RED
```
当遍历带有别名的枚举成员时，只能得到主要成员，而不能得到别名。要获取包括别名在内的所有成员，需要使用枚举类的 `__member__` 属性：
```py
pprint(Color.__members__)
```
输出结果清楚地表明，CRIMSON 和 SALMON 引用了同一个对象，而 RED 成员引用了同一个对象：
```py
mappingproxy({'BLUE': <Color.BLUE: 3>,
              'CRIMSON': <Color.RED: 1>,
              'GREEN': <Color.GREEN: 2>,
              'RED': <Color.RED: 1>,
              'SALMON': <Color.RED: 1>})
```
枚举别名在某些情况下很有用。假设必须处理来自两个不同系统的 API。每个系统都有不同的响应状态，其含义相同

## 3、@enum.unique 装饰器

要定义一个没有别名的枚举，可以谨慎地为成员使用唯一值。要确保枚举没有别名，可以使用枚举模块中的 `@enum.unique` 修饰符：
```py
import enum
from enum import Enum
@enum.unique
class Day(Enum):
    MON = 'Monday'
    TUE = 'Tuesday'
    WED = 'Wednesday'
    THU = 'Thursday'
    FRI = 'Friday'
    SAT = 'Saturday'
    SUN = 'Sunday'
```
使用 @enum.unique 装饰器装饰枚举时，如果枚举有别名，Python 将抛出异常。

## 4、自定义枚举类

Python 枚举是类。这意味着可以为它们添加方法，或实现 双下线 方法来定制它们的行为：
```py
from enum import Enum
from functools import total_ordering


@total_ordering
class PaymentStatus(Enum):
    PENDING = 1
    COMPLETED = 2
    REFUNDED = 3
    def __str__(self):
        return f'{self.name.lower()}({self.value})'
    def __eq__(self, other):
        if isinstance(other, int):
            return self.value == other
        if isinstance(other, PaymentStatus):
            return self is other
        return False
    def __lt__(self, other):
        if isinstance(other, int):
            return self.value < other
        if isinstance(other, PaymentStatus):
            return self.value < other.value
        return False
    def __bool__(self):
        if self is self.COMPLETED:
            return True
        return False
for member in PaymentStatus:
    print(member, bool(member))
```

**继承枚举：**

Python 不允许扩展枚举类，除非它没有成员。然而，这并不是限制。因为可以定义一个有方法但没有成员的基类，然后扩展这个基类：
```py
from enum import Enum
from functools import total_ordering
@total_ordering
class OrderedEnum(Enum):
    def __lt__(self, other):
        if isinstance(other, OrderedEnum):
            return self.value < other.value
        return NotImplemented
class ApprovalStatus(OrderedEnum):
    PENDING = 1
    IN_PROGRESS = 2
    APPROVED = 3
status = ApprovalStatus(2)
if status < ApprovalStatus.APPROVED:
    print('The request has not been approved.')
```

## 5、auto()函数

为了更加方便，Python 3.6 在枚举模块中引入了 auto() 辅助类，它可以自动为枚举成员生成唯一值。例如：
```py
from enum import Enum, auto
class State(Enum):
    PENDING = auto()
    FULFILLED = auto()
    REJECTED = auto()
    def __str__(self):
        return f'{self.name(self.value)}'
```
**实现原理**：auto() 会调用 `_generate_next_value_()` 方法为成员生成值。下面是 `_generate_next_value_()` 方法的语法：
```py
_generate_next_value_(name, start, count, last_values)
```
默认情况下，`_generate_next_value_()` 会生成从 1 开始的整数序列中的下一个数字。但是，Python 可能会在将来改变这一逻辑。

可以重写 `_generate_next_value_()` 方法，添加生成唯一值的自定义逻辑。如果是这样，则需要将 `_generate_next_value_()` 方法放在定义所有成员之前：
```py
from enum import Enum, auto
class State(Enum):
    def _generate_next_value_(name, start, count, last_values):
        return name.lower()
    PENDING = auto()
    FULFILLED = auto()
    REJECTED = auto()
for state in State:
    print(state.name, state.value)
```

# 六、描述符

- [描述符指南](https://docs.python.org/3/howto/descriptor.html#complete-practical-example)

描述符是可重用的属性，它把函数调用伪装成对属性的访问

## 1、定义

实现了下列任意一个方法的 Python 对象就是一个描述符（descriptor）:
- `__get__(self, obj, type=None)`
- `__set__(self, obj, value)`
- `__delete__(self, obj)`
- `__set_name__(self, owner, name)`：设置属性名称

这些方法的参数含义如下：
- `self`：是当前定义的描述符对象实例。
- `obj`：是该描述符将作用的对象实例。
- `type`：是该描述符作用的对象的类型（即所属的类）。

上述方法也被称为描述符协议，Python 会在特定的时机按协议传入参数调用某一方法，如果我们未按协议约定的参数定义方法，调用可能会出错

## 2、作用

描述符可以用来控制对属性的访问行为，实现计算属性、懒加载属性、属性访问控制等功能

## 3、描述符类型

根据所实现的协议方法不同，描述符又可分为两类：
- 若实现了 `__set__()` 或 `__delete__()` 任一方法，该描述符是一个数据描述符（data descriptor）。
- 若仅实现 `__get__()` 方法，该描述符是一个非数据描述符（non-data descriptor）

两者的在表现行为上存在差异：
- 数据描述符总是会覆盖实例字典 `__dict__` 中的属性：当一个类有数据描述符时，Python 将首先在数据描述符中查找实例的属性。如果 Python 没有找到属性，它将在实例字典 (`__dict__`) 中查找属性
- 而非数据描述可能会被实例字典 `__dict__` 中定义的属性所覆盖：如果一个类使用了非数据描述符，Python 将首先在实例属性中搜索该属性 `(instance.__dict__)`。如果 Python 在实例属性中找不到属性，它将使用数据描述符；

其实主要是查找链上的不一样，当访问对象的某个属性时，其查找链简单来说就是
- 首先在对应的`数据描述符`中查找此属性；
- 如果失败，则在对象的 `__dict__` 中查找此属性；
- 如果失败，则在`非数据描述符`中查找此属性；
- 如果失败，再去别的地方查找

描述符示例：
```py
class Coordinate:
    def __get__(self, instance, owner):
        print('The __get__ was called')
    def __set__(self, instance, value):
        print('The __set__ was called')
class Point:
    x = Coordinate()
    y = Coordinate()
p = Point()
p.x = 10 # The __set__ was called  Python 调用了 x 描述符的 __set__ 方法
p.x  # The __get__ was called
```

非描述符示例：
```py
class FileCount:
    def __get__(self, instance, owner):
        print('The __get__ was called')
        return len(os.listdir(instance.path))
class Folder:
    count = FileCount()
    def __init__(self, path):
        self.path = path

folder = Folder('/')
print('file count: ', folder.count)
# The __get__ was called
# file count:  32
folder.__dict__['count'] = 100 # 给 count 属性赋值
print('file count: ', folder.count)
# file count:  100   Python 可以在实例字典 __dict__ 中找到 count 属性。因此，它不使用数据描述符
```

# 七、元编程

## 1、`__new__`	

创建类的实例时，Python 首先调用 `__new__()` 方法创建对象，然后调用 `__init__()` 方法初始化对象的属性。`__new__()` 是对象类的静态方法（不需要使用 `@staticmethod` 装饰器，因为 Python 对它进行了特殊处理）。它的签名如下：
```py
object.__new__(class, *args, **kwargs)
```
`__new__`方法的第一个参数是要创建的新对象的类，`*args` 和 `**kwargs` 参数必须与类的 `__init__()` 参数一致。`__new__()`方法应该返回一个新的类对象。但也不是必须的；

当你定义一个新类时，该类隐式继承于对象类。这意味着你可以覆盖 `__new__` 静态方法，并在创建类的新实例之前和之后做一些事情；要创建一个类的对象，需要调用 `super().__new__()` 方法;

一般来讲，您可以调用 `object.__new__()` 方法手动创建对象。但是，在此之后您需要自己手动调用 `__init__()` 方法。如果您显式地使用 `object.__new__()` 方法创建了一个新对象，Python 将不会自动调用 `__init__()` 方法。

示例：
```py
class Person:
    def __init__(self, name):
        self.name = name
person = Person('John')
# 上面等价于：
person = object.__new__(Person, 'John')
person.__init__('John')
```
`__dict__`在调用 `__new__`之后和调用`__init__`之后的区别：
```py
person = object.__new__(Person, 'John')
print(person.__dict__) # {}
person.__init__('John')
print(person.__dict__) # {'name': 'John'}
```
从输出结果中可以清楚地看到，在调用 `__new__()` 方法后，`person.__dict__` 是空的。而在调用`__init__()`方法后，`person.__dict__`包含了值为'John'的 name 属性

下面说明了 Python 在调用类创建新对象时调用 `__new__` 和 `__init__` 方法的顺序：
```py
class Person:
    def __new__(cls, name):
        print(f'Creating a new {cls.__name__} object...')
        obj = object.__new__(cls)
        return obj
    def __init__(self, name):
        print(f'Initializing the person object...')
        self.name = name
person = Person('John')
```

**什么时候使用`__new__()`**
	
实践过程中，当想在实例化时调整对象时，就会使用 `__new__()` 方法。比如：
```py
class SquareNumber(int):
    def __new__(cls, value):
        return super().__new__(cls, value ** 2)
x = SquareNumber(3)
print(x)  # 9
```
> 请注意，您不能使用 `__init__()` 方法来这样做，因为内置 int 的 `__init__()` 方法不需要参数。以下代码将导致错误
	
总结：通常情况下，重载 `__new__()` 方法时，不需要定义 `__init__()` 方法，因为在` __init__()` 方法中可以做的事情，在 `__new__()` 方法中也可以做
	
## 2、type class

Python 使用 type class 来创建其他类。类型类本身是可调用的。下面显示了类型类的一个构造函数：
```py
type(name, bases, dict) -> a new type
```
构造函数有三个参数，用于创建一个新类：
- `name`：是类的名称，例如 Person
- `bases`：是一个元组，包含新类的基类。例如，Person 继承自 object 类，因此 bases 包含一个类（object,)
- `dict`： 是类的命名空间

从技术上讲，您可以使用类型类动态创建一个类。

当 Python 解释器在代码中遇到类定义时，它会：
- 首先，以字符串形式提取类主体。
- 第二，为类命名空间创建类字典。
- 第三，执行类主体以填充类字典。
- 最后，使用上述 type() 构造函数创建一个新的 type 实例

（1）定义一个类主体：
```py
class_body = """
def __init__(self, name, age):
    self.name = name
    self.age = age

def greeting(self):
    return f'Hi, I am {self.name}. I am {self.age} year old.'
"""
```
（2）创建类字典：
```py
class_dict = {}
```
（3）执行类主体并填充类字典：
```py
exec(class_body, globals(), class_dict)
```
（4）使用类型构造函数创建一个新的 Person 类：
```py
Person = type('Person', (object,), class_dict)
```
请注意，Person 是一个类，也是一个对象。Person 类继承于对象类，其命名空间为 class_dict

```py
print(type(Person)) # <class 'type'>
```
它是类型类:
```py
print(isinstance(Person, type)) # True
```
其中 class_dict 包括如下属性：
```py
{'__init__': <function __init__ at 0x000001B581070900>,
 'greeting': <function greeting at 0x000001B5813376A0>}
```
`Person.__dict__` 包括如下属性：
```py
mappingproxy({'__dict__': <attribute '__dict__' of 'Person' objects>,
              '__doc__': None,
              '__init__': <function __init__ at 0x000001B581070900>,
              '__module__': '__main__',
              '__weakref__': <attribute '__weakref__' of 'Person' objects>,
              'greeting': <function greeting at 0x000001B5813376A0>})
```

因为`type`类可以创建其他类，所以我们通常称它为`元类(metaclass)`。`元类(metaclass)`是用来创建其他类的类。

在 Python 中，类是类`type`的实例

## 3、Metaclass

元类是一个可以创建其它类的类。默认情况下，Python 使用 type 元类来创建其他类。

如下定义了一个 Person 类：
```py
class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age
```
当 Python 执行代码时，它会使用 type 元类来创建 Person 类。原因是 Person 类默认使用类型元类；

显式的 Person 类定义如下：
```py
class Person(object, metaclass=type):
    def __init__(self, name, age):
        self.name = name
        self.age = age
```
元类参数允许你指定使用哪个元类来定义类。因此，你可以创建一个自定义元类，用它自己的逻辑来创建其他类。通过使用自定义元类，可以在类创建过程中注入以下功能；

**元类示例：**
- 首先，定义一个名为 Human 的自定义元类，该元类的 freedom 属性默认设置为 True：
```py
class Human(type):
    def __new__(mcs, name, bases, class_dict):
        class_ = super().__new__(mcs, name, bases, class_dict)
        class_.freedom = True
        return class_
```
请注意，`__new__` 方法返回一个新类或一个类对象
- 其次，定义使用 Human 元类的 Person 类：
```py
class Person(object, metaclass=Human):
    def __init__(self, name, age):
        self.name = name
        self.age = age
```
Personclass 将具有类变量中显示的 freedom 属性：
```py
pprint(Person.__dict__)
# 输出结果如下
mappingproxy({'__dict__': <attribute '__dict__' of 'Person' objects>,
              '__doc__': None,
              '__init__': <function Person.__init__ at 0x000001E716C71670>,
              '__module__': '__main__',
              '__weakref__': <attribute '__weakref__' of 'Person' objects>,
              'freedom': True})
```

**元类参数：**

要向元类传递参数，可以使用 keyword arguments。例如，下面的代码重新定义了 Human 元类，该元类接受关键字参数，每个参数都成为一个类变量：
```py
class Human(type):
    def __new__(mcs, name, bases, class_dict, **kwargs):
        class_ = super().__new__(mcs, name, bases, class_dict)
        if kwargs:
            for name, value in kwargs.items():
                setattr(class_, name, value)
        return class_

class Person(object, metaclass=Human, country='USA', freedom=True):
    def __init__(self, name, age):
        self.name = name
        self.age = age

pprint(Person.__dict__)
# 输出结果
mappingproxy({'__dict__': <attribute '__dict__' of 'Person' objects>,
              '__doc__': None,
              '__init__': <function Person.__init__ at 0x0000018A334235E0>,
              '__module__': '__main__',
              '__weakref__': <attribute '__weakref__' of 'Person' objects>,
              'country': 'USA',
              'freedom': True})
```

**什么时候使用元类：** 在实践中，您通常不需要使用元类，除非您维护或开发了大型框架的核心，如 Django

元类是一种更深层次的魔法，99% 的用户都不需要担心这个问题。如果你想知道自己是否需要它们，那你就不需要了（真正需要它们的人肯定知道自己需要它们，不需要解释为什么）。

## 4、完整实例

https://www.pythontutorial.net/python-oop/python-metaclass-example/

```py
class Prop:
    def __init__(self, attr):
        self._attr = attr

    def get(self, obj):
        return getattr(obj, self._attr)

    def set(self, obj, value):
        return setattr(obj, self._attr, value)

    def delete(self, obj):
        return delattr(obj, self._attr)


class Data(type):
    def __new__(mcs, name, bases, class_dict):
        class_obj = super().__new__(mcs, name, bases, class_dict)

        # create property
        Data.define_property(class_obj)

        # define __init__
        setattr(class_obj, '__init__', Data.init(class_obj))

        # define __repr__
        setattr(class_obj, '__repr__', Data.repr(class_obj))

        # define __eq__ & __hash__
        setattr(class_obj, '__eq__', Data.eq(class_obj))
        setattr(class_obj, '__hash__', Data.hash(class_obj))

        return class_obj

    @staticmethod
    def eq(class_obj):
        def _eq(self, other):
            if not isinstance(other, class_obj):
                return False

            self_values = [getattr(self, prop) for prop in class_obj.props]
            other_values = [getattr(other, prop) for prop in other.props]

            return self_values == other_values

        return _eq

    @staticmethod
    def hash(class_obj):
        def _hash(self):
            values = (getattr(self, prop) for prop in class_obj.props)
            return hash(tuple(values))

        return _hash

    @staticmethod
    def repr(class_obj):
        def _repr(self):
            prop_values = (getattr(self, prop) for prop in class_obj.props)
            prop_key_values = (f'{key}={value}' for key, value in zip(class_obj.props, prop_values))
            prop_key_values_str = ', '.join(prop_key_values)
            return f'{class_obj.__name__}({prop_key_values_str})'

        return _repr

    @staticmethod
    def init(class_obj):
        def _init(self, *obj_args, **obj_kwargs):
            if obj_kwargs:
                for prop in class_obj.props:
                    if prop in obj_kwargs.keys():
                        setattr(self, prop, obj_kwargs[prop])

            if obj_args:
                for kv in zip(class_obj.props, obj_args):
                    setattr(self, kv[0], kv[1])

        return _init

    @staticmethod
    def define_property(class_obj):
        for prop in class_obj.props:
            attr = f'_{prop}'
            prop_obj = property(
                fget=Prop(attr).get,
                fset=Prop(attr).set,
                fdel=Prop(attr).delete
            )
            setattr(class_obj, prop, prop_obj)

        return class_obj


class Person(metaclass=Data):
    props = ['name', 'age']


def data(cls):
    return Data(cls.__name__, cls.__bases__, dict(cls.__dict__))


@data
class Employee:
    props = ['name', 'job_title']
```

## 5、dataclass

Python 在 3.7 版 (PEP 557) 中引入了数据类。数据类允许您定义代码更少、功能更多的类。

比如有如下类 Person，包含两个属性：
```py
class Person:
    def __init__(self, name, age):
        self.name = name
        self.age = age
```
如果想用字符串表示 Person 对象，需要实现 `__str__` 或 `__repr__` 方法。另外，如果要通过属性比较 Person 类的两个实例，需要实现 `__eq__` 方法。

但是，如果使用数据类，就可以拥有所有这些功能（甚至更多），而不需要实现这些 下划线 方法：
```py
from dataclasses import dataclass
@dataclass
class Person:
    name: str
    age: int
```
在本例中，Person 类有两个属性，分别是 str 类型的 name 和 int 类型的 age。这样，`@dataclass` 装饰器就隐式地创建了 `__init__` 方法，如下所示：
```py
def __init__(name: str, age: int)
```
> 请注意，类中声明的属性顺序将决定 `__init__` 方法中参数的顺序

### 5.1、默认值

要为数据类中的属性定义默认值，可将其分配给该属性，如下所示：
```py
from dataclasses import dataclass
@dataclass
class Person:
    name: str
    age: int
    iq: int = 100
print(Person('John Doe', 25))
```
与参数规则一样，有默认值的属性必须出现在无默认值的属性之后。因此，以下代码将不起作用：
```py
from dataclasses import dataclass
@dataclass
class Person:
    iq: int = 100
    name: str
    age: int
```

### 5.2、转换为元组或字典

数据类模块有 `astuple()` 和 `asdict()` 函数，可将数据类实例转换为元组和字典：
```py
from dataclasses import dataclass, astuple, asdict
@dataclass
class Person:
    name: str
    age: int
    iq: int = 100
p = Person('John Doe', 25)
print(astuple(p))
print(asdict(p))
```

### 5.3、创建不可变对象

要从数据类创建只读对象，可以将数据类装饰器的冻结参数设置为 True：
```py
from dataclasses import dataclass, astuple, asdict
@dataclass(frozen=True)
class Person:
    name: str
    age: int
    iq: int = 100
```

### 5.4、自定义属性行为

如果不想在 `__init__` 方法中初始化属性，可以使用数据类型模块中的 `field()` 函数：
```py
from dataclasses import dataclass, field
class Person:
    name: str
    age: int
    iq: int = 100
    can_vote: bool = field(init=False)
```
field() 函数有多个有趣的参数，如 repr、hash、compare 和 metadata

如果要初始化一个依赖于另一个属性值的属性，可以使用 `__post_init__` 方法。顾名思义，Python 在 `__init__` 方法之后调用 `__post_init__` 方法：
```py
from dataclasses import dataclass, field
@dataclass
class Person:
    name: str
    age: int
    iq: int = 100
    can_vote: bool = field(init=False)
    def __post_init__(self):
        print('called __post_init__ method')
        self.can_vote = 18 <= self.age <= 70
p = Person('Jane Doe', 25)
print(p)
```

### 5.5、排序对象

默认情况下，数据类会实现 `__eq__` 方法，要允许 `__lt__、__lte__、__gt__、__gte__`等不同类型的比较，可以将 `@dataclass` 装饰器的顺序参数设置为 True：
```py
@dataclass(order=True)
```
这样，数据类就会按照每个字段对对象进行排序，直到找到一个不相等的值。在实际操作中，如果希望通过某个属性而不是所有属性来比较对象。为此，需要定义一个名为 `sort_index` 的字段，并将其值设为要排序的属性
```py
from dataclasses import dataclass, field
@dataclass(order=True)
class Person:
    sort_index: int = field(init=False, repr=False)
    name: str
    age: int
    iq: int = 100
    can_vote: bool = field(init=False)

    def __post_init__(self):
        self.can_vote = 18 <= self.age <= 70
        # sort by age
        self.sort_index = self.age
members = [
    Person(name='John', age=25),
    Person(name='Bob', age=35),
    Person(name='Alice', age=30)
]
sorted_members = sorted(members)
for member in sorted_members:
    print(f'{member.name}(age={member.age})')
```
