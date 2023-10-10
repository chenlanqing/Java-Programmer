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
