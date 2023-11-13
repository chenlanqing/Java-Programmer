# 一、正则基础

## 1、正则处理函数

正则表达式本质上是一种嵌入在 Python 中的专门编程语言。您可以通过 Python 内置的 `re` 模块与正则表达式交互
```py
'\d'
```
在本例中，正则表达式是包含搜索模式的字符串。`\d`是一个数字字符集，可以匹配 0 到 9 之间的任何一位数字。

使用正则表达式：
- 导入模块：`import re;`
- 将正则表达式编译成`Pattern`对象：`p = re.compile('\d')`；
- 使用 Pattern 对象的一种方法来匹配字符串：
    ```py
    s = "Python 3.10 was released on October 04, 2021"
    result = p.findall(s)
    print(result)
    ```
    `findall` 方法返回所有一个包含在字符串s中为数字的集合；

除了 `findall()` 方法外，Pattern 对象还有其他重要方法，可以匹配字符串：
- `match()`：查找字符串开头的模式；
- `search()`：返回字符串中模式的第一个匹配项；
- `findall()`：返回字符串中模式的所有匹配结果；
- `finditer()`：以迭代器的形式返回模式的所有匹配项；

除了Pattern 类之外，`re` 模块还有一些可以匹配字符串模式的函数：
- match()
- search()
- findall()
- finditer()

这些函数的名称与 Pattern 对象的方法相同。此外，它们的参数也与模式对象的相应方法相同。不过，在使用正则表达式之前，不必手动编译它；
```py
import re
s = "Python 3.10 was released on October 04, 2021."
results = re.findall('\d',s)
print(results)
```
使用 re 模块中的函数比使用 Pattern 对象的方法更简洁，因为无需手动编译正则表达式。

这些函数会创建一个Patter对象，并调用相应的方法。它们还将编译后的正则表达式存储在缓存中，以优化速度。这意味着，如果第二次调用相同的正则表达式，这些函数无需重新编译正则表达式。相反，它们会从缓存中获取编译后的正则表达式；

如果在循环中使用正则表达式，Pattern 对象可能会节省一些函数调用。但是，如果在循环之外使用，由于有内部缓存，差别就很小了。

### 1.1、search函数

search() 函数在字符串中搜索模式。如果存在匹配，则返回第一个匹配对象，否则返回 None。
```py
import re
s = "Python 3.10 was released on October 04, 2021."
pattern = '\d{2}'
match = re.search(pattern, s) 
print(type(match)) # <class 're.Match'>
print(match) # <re.Match object; span=(9, 11), match='10'>
```

### 1.2、Match对象

匹配对象提供有关匹配字符串的信息。它有以下重要方法：
- `group()`：返回匹配的字符串
- `start()`：返回匹配字符串的起始位置
- `end()`：返回匹配的结束位置
- `span()`：返回指定匹配位置的元组（start、end）
```py
import re
s = "Python 3.10 was released on October 04, 2021."
result = re.search('\d', s) 

print('Matched string:',result.group())
print('Starting position:', result.start())
print('Ending position:',result.end())
print('Positions:',result.span())
```

### 1.3、match函数

如果 match() 函数在字符串开头找到一个模式，它就会返回一个 Match 对象
```py
import re
l = ['Python', 
    'CPython is an implementation of Python written in C', 
    'Jython is a Java implementation of Python',
     'IronPython is Python on .NET framework']
pattern = '\wython'
for s in l:
    result = re.match(pattern,s)
    print(result)
```

### 1.4、fullmatch

如果整个字符串与模式匹配，函数 fullmatch() 将返回一个 Match 对象，否则返回 None。下面的示例使用 fullmatch() 函数匹配包含四位数字的字符串
```py
import re
s = "2021"
pattern = '\d{4}'
result = re.fullmatch(pattern, s)
print(result)
```

### 1.5、正则表达式与raw 字符串

re 模块是 Python 和正则表达式编程语言之间的接口。它的行为类似于两者之间的解释器。为了构造模式，正则表达式通常使用反斜杠'`\`'，例如 `\d` 和 `\w`。但这与 Python 在字符串字面量中使用反斜杠的目的相冲突。

`s = '\section'`，在 Python 中，反斜线 `(\)` 是一个特殊字符。要构造一个正则表达式，需要在每个反斜线前加上反斜线 `(\)` 来转义任何反斜线：`pattern = '\\section'`

一种解决方案是使用 Python 中的原始字符串进行正则表达式，因为原始字符串将反斜杠 (\)视为字面字符，而不是特殊字符。
```py
import re
s = '\section'
pattern = r'\\section'
result = re.findall(pattern, s)
print(result)

p1 = '\\section'
p2 = '\section'
print(p1==p2) # true
```















