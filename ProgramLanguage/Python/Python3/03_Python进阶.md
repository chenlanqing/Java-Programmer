# 一、变量与内存管理

## 1、引用

变量引用一个保存值的对象。换句话说，变量是引用；变量是指向内存中对象的引用：
```py
counter = 100
```
要查找变量引用对象的内存地址，可将变量传递给内置 id() 函数：
```py
counter = 100
print(id(counter)) # 140717671523072
```
id() 是按照十进制返回的，要将内存地址转换为十六进制字符串，可以使用函数 hex()：
```py
print(hex(id(counter))) # 0x7ffb62d32300
```

内存地址中的对象可以有一个或多个引用，一旦对象没有任何引用，Python 内存管理器将销毁该对象并回收内存；（一旦对象的引用计数为零，内存管理器就会销毁该对象并收回内存。）

要获取对象的引用次数，可以使用 ctypes 模块的 from_address()方法。：
```py
ctypes.c_long.from_address(address).value
```
要使用此方法，需要传递要计算引用次数的对象的内存地址。此外，地址必须是整数：
```py
import ctypes
def ref_count(address):
    return ctypes.c_long.from_address(address).value
```
示例：
```py
import ctypes
def ref_count(address):
    return ctypes.c_long.from_address(address).value
numbers = [1, 2, 3]
numbers_id = id(numbers)

print(ref_count(numbers_id))  # 1

ranks = numbers
print(ref_count(numbers_id))  # 2

ranks = None
print(ref_count(numbers_id))  # 1

numbers = None
print(ref_count(numbers_id))  # 0
```

## 2、GC

不过，引用计数并非在所有时候都能正常工作。例如，当一个对象引用自身或两个对象相互引用时。这就会产生循环引用；

Python 允许您通过内置的 gc 模块与垃圾回收器交互，主要通过 gc 模块解决循环引用问题

看如下示例：
```py
import gc
import ctypes
def ref_count(address):
    return ctypes.c_long.from_address(address).value
def object_exists(object_id):
    for object in gc.get_objects():
        if id(object) == object_id:
            return True
    return False
class A:
    def __init__(self):
        self.b = B(self)
        print(f'A: {hex(id(self))}, B: {hex(id(self.b))}')
class B:
    def __init__(self, a):
        self.a = a
        print(f'B: {hex(id(self))}, A: {hex(id(self.a))}')
# disable the garbage collector
gc.disable()

a = A()

a_id = id(a)
b_id = id(a.b)

print(ref_count(a_id))  # 2
print(ref_count(b_id))  # 1

print(object_exists(a_id))  # True
print(object_exists(b_id))  # True

a = None
print(ref_count(a_id))  # 1
print(ref_count(b_id))  # 1

print(object_exists(a_id))  # True
print(object_exists(b_id))  # True

# run the garbage collector
gc.collect()

# check if object exists
print(object_exists(a_id))  # False
print(object_exists(b_id))  # False

# reference count
print(ref_count(a_id))  # 0
print(ref_count(b_id))  # 0
```

## 3、动态类型

Python是一种动态类型语言。在Python中声明变量时，不需要为它指定类型；
```py
message = 100
```
在 Python 中，message变量只是对字符串对象的引用。message 变量没有相关的类型。要确定变量当前引用的对象类型，可以使用 type() 函数：
```py
message = 'Hello'
print(type(message))
```

## 4、可变与不可变

在 Python 中，一切都是对象。对象有自己的内部状态。有些对象允许您改变其内部状态，有些则不允许。内部状态可变的对象称为可变对象，内部状态不可变的对象称为不可变对象；

以下都是不可变对象：
- Numbers (int, float, bool,…)
- Strings
- Tuples
- Frozen sets

以下是可变对象：
- Lists
- Sets
- Dictionaries

## 5、is运算符

is 运算符比较两个变量，如果它们引用的是同一个对象，则返回 True。如果两个变量引用了不同的对象，则 is 运算符返回 False。换句话说，is 运算符比较两个变量的同一性，如果它们引用的是同一个对象，则返回 True。
```py
a = 100
b = 100

result = a is b
print(result) # True，但是不一定，这个示例的结果可能会有所不同，这取决于 Python 内存管理器是如何实现的
```

**`is` vs `==`**：相等运算符 `(==)` 比较两个变量是否相等，如果相等则返回 True。否则返回 False
```py
a = 100
b = a

is_identical = a is b
is_equal = a == b

print(is_identical) # True
print(is_equal) # True
```
list中如果元素一样，那么 == 比较返回True
```py
ranks = [1, 2, 3]
rates = [1, 2, 3]

is_identical = ranks is rates
is_equal = ranks == rates

print(is_identical) # False
print(is_equal) # True
```

**`is not`运算符**

要否定 `is` 操作符，可以使用 `not` 操作符。如果两个变量引用同一个对象，`is not` 运算符返回 False。否则，返回 True：
```py
ranks = [1, 2, 3]
rates = [1, 2, 3]

result = ranks is not rates
print(result)  # True
```

## 6、None

在 Python 中，None 是 NoneType 类的一个特殊对象。要使用 None 值，可以按如下方式指定 None：
```py
None
print(type(None)) # <class 'NoneType'>
```
None 是 NoneType 类的单例对象。这意味着 Python 在运行时会创建一个且仅有一个 None 对象，因此，如果使用相等 (==) 或 is 运算符来比较 None 和 None，结果将是 True；所以在实践中使用 is 或 is not 操作符将一个值与 None 进行比较是一种好的做法；

还需要注意的是，None 对象具有以下功能：
- None 不等于零（0、0.0、......）。
- None 不等于 False。
- None 不等于空字符串（''）。
- 将 None 与任何值比较都会返回 False，除了 None 本身。

主要用途：
- 使用 None 作为变量的初始值；
- 使用 None 对象修复可变默认参数问题：
    ```py
    def append(color, colors=None):
        if colors is None:
            colors = []

        colors.append(color)
        return colors
    hsl = append('hue')
    print(hsl)
    rgb = append('red')
    print(rgb)
    ```
- 使用 None 对象作为函数的返回值；当函数没有返回值时，默认返回 None

# 二、数字类型

## 1、整数

整数是整数，包括负数、零和正数，如 -3、-2、-1、0、1、2、3。Python 使用类 int 来表示所有整数。所有整数都是对象；

Python 并不使用固定位数来存储整数。相反，Python 使用可变的位数来存储整数。例如，8 位、16 位、32 位、64 位、128 位，等等，Python 能表示的最大整数取决于可用内存。

另外，整数是对象。Python 需要为每个整数额外增加固定数量的字节作为开销。

值得注意的是，整数越大，"+"、"-"......等运算的速度就越慢。

```py
counter = 10
print(type(counter)) # <class 'int'>
```
要获取整数的大小，可以使用 sys 模块的 `getsizeof()` 函数：
```py
from sys import getsizeof
counter = 0
size = getsizeof(counter)
print(size)  # 24 bytes
```
因此，您可以认为 Python 使用 24 字节作为存储整数对象的开销。

整数的 `+` 、`-` 、`*` 、`**` 结果返回的都是整数，但是 除法（/） 返回的是 float

## 2、向下取整除

Python 使用两个运算符 `//` 和 `%` 来返回除法的结果：
```py
101 // 4 = 25 # 整除
101 % 4 = 1 # 取余数
```
`//` 被称 floor division 或 div。而 % 被称为模运算符或 mod
```py
101 = 4 * (101 // 4) + (101 % 4)
101 = 4 * 25         + 1
```
一般来说，如果 N 是分子，D 是分母，那么除法底层运算符和mod运算符总是满足以下等式：
```py
N = D * ( N // D) + (N % D)
```

实数的底数是小于或等于该数的最大整数：
```py
floor(3.4) = 4
floor(3.9) = 3
floor(3) = 3
```
对正数是上面的逻辑，但是对于负数来说：例如，根据下限定义，-3.4 的下限返回 -4，而不是 -3。同样，-3.9 的下限也返回 -4。

向下取整可以定义为：
```py
n // d = floor(n/d)
```

下表说明了两个整数 a 和 b 的 向下取整除法：
a	| b	| a // b
----|----|----
10	|3	|3
-10	|-3	|3
10	|-3	|-4
-10	|3	|-3

**`math.floor()`函数**

math模块的 floor() 函数返回两个整数的向下取整除法结果：
```py
from math import floor
a = 10
b = 3
print(a//b)  # 3
print(floor(a/b))  # 3
```
从输出中可以清楚地看到，floor() 函数返回的结果与 floor 除法运算符 (`//`) 返回的结果相同。负数也是如此

## 3、模运算

Python 使用百分号 (%) 作为模运算符。%运算符总是满足以下等式：
```py
N = D * ( N // D) + (N % D)
```
其中 N是分子，D 是分母

用途：
- 判断整数是否奇偶
- 使用模运算符转换单位，比如日期转换等：
```py
from math import floor
def get_time(total_seconds):
    return {
        'days': floor(total_seconds / 60 / 60 / 24),
        'hours': floor(total_seconds / 60 / 60) % 24,
        'minutes': floor(total_seconds / 60) % 60,
        'seconds': total_seconds % 60,
    }
print(get_time(93750))
```