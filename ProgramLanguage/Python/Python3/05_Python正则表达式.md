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

## 2、Regex 字符集

字符集（或字符类别）是一组字符，例如数字（从 0 到 9）、字母（从 a 到 z）和空白。通过字符集，可以使用patterns来构建正则表达式，使字符串与字符集中的一个或多个字符相匹配。

### 2.1、数字

`\d`-表示数字字符集，匹配 0 到 9 的单个数字。下面的示例使用 finditer() 函数使用 `\d` 字符集匹配字符串中的每一位数字
```py
import re
s = 'Python 3.0 was released in 2008'
matches = re.finditer('\d', s)
for match in matches:
    print(match.group())
```
如果要匹配两个数字，需要使用：`\d\d`

### 2.2、单词

`\w`：正则表达式使用 `\w` 表示单词字符集。`\w` 匹配单个 ASCII 字符，包括拉丁字母、数字和下划线 (_)，但是`\w`中不包括 空格 和`.`；

### 2.3、空白字符集

`\s`-匹配空白，包括空格、制表符、换行符、回车符和垂直制表符。

### 2.4、字符集反转

一个字符集有一个相反的字符集，使用相同的字母，但都是大写字母。下表列出了字符集及其反转字符集：含义

字符集 | 反字符集 | 说明
------|----------|------
`\d` | `\D` | 匹配除数字以外的单个字符
`\w` | `\W` | 匹配非单字的单个字符
`\s` | `\S` | 匹配除空格外的单个字符

```py
import re
phone_no = '+1-(650)-513-0514'
matches = re.finditer('\D', phone_no)
for match in matches:
    print(match.group())
```

### 2.5、dot(`.`)字符集

点 (`.`) 字符集匹配除换行 (`\n`) 以外的任何单个字符。下面的示例使用点（`.`）字符集匹配新行以外的所有字符：
```py
import re
version = "Python\n4"
matches = re.finditer('.', version)
for match in matches:
    print(match.group())
```

## 3、锚点

正则表达式提供了两个匹配字符位置的锚点：
- `^` ：匹配字符串的开头。
- `$` : 匹配字符串的末尾。

```py
import re
time = '12:20'
matches = re.finditer('^\d\d:\d\d$', time)
for match in matches:
    print(match.group())
```

## 4、单词边界

一个字符串有以下可作为单词边界的位置：
- 字符串中第一个字符之前，如果第一个字符是单词字符（`\w`）。
- 字符串中的两个字符之间，如果第一个字符是单词字符（`\w`），而另一个不是（`\W` - 单词字符`\w` 的反字符集）。
- 字符串中最后一个字符之后，如果最后一个字符是单词字符 (`\w`)

正则表达式使用` \b` 表示单词边界。例如，可以使用 `\b` 来匹配以下模式中的整个单词：
```py
r'\bword\b'
```
示例：
```py
import re
s = 'CPython is the implementation of Python in C'
matches = re.finditer(r'\bPython\b', s)
for match in matches:
    print(match.group())
```

## 5、量词

在正则表达式中，量词会多次匹配前面的字符或字符集。下表列出了所有量词及其含义
- `*`：与前一个元素匹配 0 次或更多次。
- `+` 正号 与前一个元素匹配一次或多次。
- `?` 与前一个元素匹配 0 次或 1 次。
- `{ n }`：与前一个元素精确匹配 n 次。
- `{ n ,}`：与前一个元素至少匹配 n 次。
- `{ n , m }`：匹配其前一个元素 n 到 m 次。







