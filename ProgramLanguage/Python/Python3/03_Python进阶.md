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

## 4、bool类

bool类是int类的子类。这意味着bool类继承int类的所有属性和方法。此外，bool类具有与布尔操作相关的特定行为：
```py
is_child_class = issubclass(bool, int)
print(is_child_class) # True
```
事实上，True 和 False 都是 bool 类的单例对象：
```py
isinstance(True, bool) # True
isinstance(False, bool) # True
```
由于 True 和 False 也是 int 对象，因此可以将它们转换为整数：
```py
true_value = int(True)
print(true_value) # 1
false_value = int(False)
print(false_value) # 0
```
请注意，"True"和 "1"不是同一个对象。同样，"False"和 "0 "也不是同一个对象。

因为 True 和 False 是单例对象，在整个程序中始终引用内存中的相同对象;

当您向 bool() 构造函数传递一个对象时，Python 返回该对象的 `__bool__()` 方法的值。
```py
bool(200) 
# 实际执行的是：
200.__bool__()
```

如果对象的类没有 `__bool__()` 方法，Python 将返回 `__len__()` 方法的结果，如果 `__len__()`方法的结果为零，bool() 返回 False。否则，返回 True，这就是为什么空列表总是 False，而至少有一个元素的列表是 True 的原因；

最后，如果一个类没有 `__bool__()` 和 `__len__()` 方法，则该类的实例总是求值为 True

## 5、and操作符

Python 和运算符是一种逻辑运算符。通常，使用 and 运算符对布尔值进行运算并返回布尔值。

and是短路运算符，如果 and 前面返回 False，不管后面是 True 还是 False 都返回 False

## 6、or操作符

或运算符是一种逻辑运算符。通常，使用 or 运算符可以组合两个布尔表达式，并返回一个布尔值

`x or y`：如果 x 是真实的，那么 or 运算符就不需要对 y 进行求值，而是立即返回 x。这就是为什么这种求值方式被称为 "懒求值 "或 "短路求值"。在 Python 中，每个对象都与布尔值相关联。x 和 y 可以是任何对象。

or 操作符允许给一个变量赋默认值：
```py
var_name = value or default
```
如果 value是false，var_name 会给到 var_name 默认值

## 7、float

### 7.1、float类

Python 使用 float 类表示实数，Python float 使用 8 个字节（或 64 位）来表示实数。与整数类型不同，float 类型使用固定的字节数

Python 使用 64 位的情况如下：
- 1 位表示符号（正或负）
- 11 位表示指数 1.5e-5 1.5 x $10^5$（指数为-5），范围为 [-1022, 1023]。
- 52 位有效数字

例如，0.25 有两位有效数字，0.125 有三位有效数字，12.25 有四位有效数字:

$(1.25)_{10} = (1×2^0 + 0×2^{-1} + 1×2^-2)_{10} = (1.01)_2$

有些数字有有限的二进制表示法，但有些没有，例如 0.1。在二进制中是 01.0001100110011...。因此，Python 只能对这些数字使用近似的浮点表示法

如果将一个对象 (obj) 传递给 float(obj)，它将委托给 `obj.__float__()`。如果没有定义 `__float__()` ，它将返回到 `__index__()`；如果不给 float() 传递任何参数，它将返回 0.0

当您使用print()函数时，您将看到数字0.1被精确地表示为0.1。在内部，Python只能近似地表示0.1。要查看Python如何在内部表示0.1，可以使用`format()`函数：
```py
>>> format(0.1, '.20f')
'0.10000000000000000555'
```

float数字比较：
```py
x = 0.1 + 0.1 + 0.1
y = 0.3
print(x == y) # False
print(format(x, '.20f')) # 0.30000000000000004441
print(format(y, '.20f')) # 0.29999999999999998890
```
解决这个问题的一种方法是将等式的两边四舍五入到一个有效数字位数：
```py
x = 0.1 + 0.1 + 0.1
y = 0.3
print(round(x, 3) == round(y, 3)) # True
```
这种变通方法并非在所有情况下都有效； [PEP485](https://www.python.org/dev/peps/pep-0485/)提供的解决方案通过使用相对和绝对公差解决了这一问题，它提供了数学模块中的 `isclose()` 函数，如果两个数字相对接近，则返回 True：
```py
isclose(a, b, rel_tol=1e-9, abs_tol=0.0)
```
示例：
```py
from math import isclose
x = 0.1 + 0.1 + 0.1
y = 0.3
print(isclose(x,y))
```

### 7.2、float 转 int

将浮点数转换为整数时，会出现数据丢失。例如，20.3 可能变成 20 或 21，Python 在数学模块中提供了一些将浮点数转换为 int 的函数，包括：
- Truncation：函数 `trunc(x)` 返回数字 x 的整数部分。
    ```py
    from math import trunc
    print(trunc(12.2)) # 12
    print(trunc(12.5)) # 12
    print(trunc(12.7)) # 12
    ```
    同样，int() 构造函数接受浮点数，并使用截断法将浮点数转换为 int
- Floor：函数 `floor(x)` 返回小于或等于 x 的最大整数
    ```py
    from math import floor
    print(floor(12.2)) # 12
    print(floor(12.5)) # 12
    print(floor(12.7)) # 12
    ```
    对于正数，floor(x) 和 trunc(x) 返回相同的结果；但是对于负数是不同的：
    ```py
    from math import floor, trunc
    print(floor(-12.7)) # 13
    print(trunc(-12.7)) # 12
    ```
- ceiling：函数 ceil(x) 返回大于或等于 x 的最小整数
```py
# 正数
from math import ceil
print(ceil(12.7)) # 13
# 负数：
from math import ceil
print(ceil(-12.7)) # 12
```

### 7.3、四舍五入

四舍五入是指简化一个数字，但使其值接近原值。例如，89 四舍五入到最接近的十就是 90，因为 89 比 80 更接近 90。

在 Python 中，要对一个数字进行四舍五入，需要使用内置的 round() 函数：
```py
round(number [, ndigits])
```
函数 round() 将数字取整为最接近 10 位数的倍数，换句话说，round() 函数返回小数点后四舍五入到 ndigits 位精度的数字，如果省略 ndigits 或为无，round( ) 将返回最接近的整数：
```py
round(1.25) # 1
round(1.25, 0) # 1.0
```
如果 ndigits 是负数：
```py
round(15.5, -1)
```
由于 15.5 位于 10 和 20 之间（10 的倍数），因此更接近 20。因此，round() 函数返回 20。

当您对位于两个数字中间的数字进行四舍五入时，Python 无法找到最接近的数字。:
```py
round(1.25, 1)
```
在这种情况下，Python 使用 IEEE 754 标准进行四舍五入，即银行家四舍五入。在银行四舍五入法中，一个数字四舍五入到最接近的数值，并列时四舍五入到最小有效数字为偶数的数值。
> 一般来说，一个数字中最小的有效数字是最右边的数字。

银行家四舍五入法的原理是，从统计角度看，50% 的样本数四舍，50% 的样本数五入：
```py
round(1.25, 1) # 1.2
```

**如何从零开始四舍五入**

Python 并没有提供从 0 开始四舍五入的直接方法。

将一个数字从零四舍五入的常用方法是使用下面的表达式

函数 copysign() 返回 x 的绝对值，但不返回 y 的符号：
