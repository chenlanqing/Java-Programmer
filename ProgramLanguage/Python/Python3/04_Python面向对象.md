
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	





