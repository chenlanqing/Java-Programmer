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
- `+`：与前一个元素匹配一次或多次。
- `?`：与前一个元素匹配 0 次或 1 次。
- `{ n }`：与前一个元素精确匹配 n 次。
- `{ n ,}`：与前一个元素至少匹配 n 次。
- `{ n , m }`：匹配其前一个元素 n 到 m 次。

### 5.1、匹配 0 次或更多次 (`*`)

```py
import re
s = """CPython, IronPython, and JPython 
       are major Python's implementation"""
matches = re.finditer('\w*Python', s)

for match in matches:
    print(match)
```
因此，`\w*Python` 模式会匹配 `CPython`、`IronPython`、`JPython` 和 `Python` 字符串中的

### 5.2、匹配一次或多次 (`+`)

`+` 量词匹配其前一个或多个元素。例如，`\d+` 会匹配一个或多个数字。
```py
import re
s = "Python 3.10 was released in 2021"
matches = re.finditer('\d+', s)
for match in matches:
    print(match)
```

### 5.3、零次或一次匹配（`?`）

`?`量词与前一个元素匹配 0 次或 1 次。
```py
import re
s = "What color / colour do you like?"
matches = re.finditer('colou?r', s)
for match in matches:
    print(match)
```

### 5.4、精确匹配 n 次：`{n}`

`{n}` 量词与其前一个元素精确匹配 n 次，其中 n 为零或正整数。
```py
import re
s = "It was 11:05 AM"
matches = re.finditer('\d{2}:\d{2}', s)
for match in matches:
    print(match)
```

### 5.5、至少匹配 n 次：`{n,}`

量词 `{n,}` 与前一个元素至少匹配 n 次，其中 n 为零或正整数
```py
import re
s = "5-5-2021 or 05-05-2021 or 5/5/2021"
matches = re.finditer('\d{1,}-\d{1,}-\d{4}', s)
for match in matches:
    print(match)
```
### 5.6、从 n 次匹配到 m 次：`{n,m}`

`{n,m}`量词与其前一个元素至少匹配 n 次，但不超过 m 次，其中 n 和 m 均为零或正整数。例如：
```py
import re
s = "5-5-2021 or 05-05-2021 or 5/5/2021"
matches = re.finditer('\d{1,2}-\d{1,2}-\d{4}', s)
for match in matches:
    print(match)
```

## 6、正则贪婪模式

默认情况下，所有量词都以贪婪模式工作。这意味着量词将尽可能匹配其前面的元素；

**贪婪模式下意外结果**

假设有如下文本：
```py
s = '<button type="submit" class="btn">Send</button>'
```
需要匹配引号 ("") 内的文本，如 submit 和 btn。为此，您可以使用以下模式，其中包括引号（"）、点（.）字符集和 (+) 数量符：`".+"`
```py
import re
s = '<button type="submit" class="btn">Send</button>'
pattern = '".+"'
matches = re.finditer(pattern, s)
for match in matches:
    print(match.group()) # "submit" class="btn"
```
默认情况下，量词 (`+`) 以贪婪模式运行，在这种模式下，它会尽量匹配前面的元素（"."）。

要解决这个问题，需要指示量词 (`+`) 使用非贪婪（或懒惰）模式，而不是贪婪模式，在上面的正则表达式基础加个`?`变成：`".+?"`
```py
import re
s = '<button type="submit" class="btn">Send</button>'
pattern = '".+?"'
matches = re.finditer(pattern, s)
for match in matches:
    print(match.group())
```

## 7、正则非贪婪模式

数量词允许您多次匹配其前面的元素。量词有两种工作模式：贪婪和非贪婪（懒惰）。
- 当量词以贪婪模式工作时，它们被称为贪婪量词。同样，当量词以非贪婪模式工作时，它们被称为非贪婪量词或懒惰量词。
- 默认情况下，量词以贪婪模式工作。这意味着贪婪量词将尽可能匹配其前面的元素，以返回最大的匹配值。
- 另一方面，非贪婪量词会尽可能少地匹配，以返回最小的匹配结果。非贪婪量词与贪婪量词正好相反。
- 要将贪婪量词转换为非贪婪量词，需要在量词上添加一个额外的问号（`?`）

下表列出了贪心和相应的非贪心量词：
贪婪量词 | 懒惰量词 | 含义
--------|----------|-----
`*` | `*?` | 与前一个元素匹配 0 次或更多次。
`+` | `+?` | 与前一个元素匹配一次或多次。
`?` | `??` | 匹配前一个元素零次或一次。
`{ n }` |  `{ n }?` | 与前一个元素精确匹配 n 次。
`{ n,}` | `{ n, }?` | 与前一个元素至少匹配 n 次。
`{ n , m }` | `{ n , m }?` | 与前一个元素匹配 n 到 m 次。

## 8、集合与范围

### 8.1、集合

例如，`[abc]` 表示三个字符中的任何一个："a"、"b "或 "c"。`[abc]`被称为一个集合。可以使用该集合和正则字符来构建搜索模式
```py
import re
s = 'A licence or license'
pattern = 'licen[cs]e'
matches = re.finditer(pattern, s)
for match in matches:
    print(match.group())
```

### 8.2、范围

当一个字符集由许多字符组成时，例如从 a 到 z 或从 1 到 9，在字符集中列出这些字符会很麻烦。相反，可以使用方括号中的字符范围。例如，`[a-z]` 表示 a 至 z 范围内的字符，`[0-9]` 表示 0 至 9 的数字。

此外，还可以在同一个方括号内使用多个范围。例如，`[a-z0-9]` 有两个范围，可以匹配从 a 到 z 的字符或从 0 到 9 的数字。

同样，也可以在方括号内使用一个或多个字符集，如 `[\d\s]` 表示数字或空格字符。

同样，也可以将字符与字符集混合使用。例如，`[\d_]` 表示数字或下划线。

### 8.3、排除集合和范围

要对一个字符集或范围取反，可以在字符集和范围的开头使用字符：`^`。例如，范围 `[^0-9]`匹配除数字外的任何字符。这与字符集 `\D` 相同。

请注意，regex 还将 (`^`) 用作匹配字符串开头的锚点。但是，如果在方括号内使用圆括号 (`^`)，regex 将把它视为否定运算符，而不是锚点。
```py
import re
s = 'Python'
pattern = '[^aeoiu]'
matches = re.finditer(pattern, s)
for match in matches:
    print(match.group())
```

# 二、正则分组

## 1、匹配组

示例：
```py
import re
s = 'news/100'
pattern = '\w+/(\d+)'
matches = re.finditer(pattern, s)
for match in matches:
    for index in range(0, match.lastindex + 1):
        print(match.group(index))
```

默认情况下，可以使用索引访问匹配中的子组，例如 match.group(1)。有时，使用有意义的名称访问子组更为方便。

可以使用命名捕获组为组指定名称。下面显示了为捕获组指定名称的语法：
```py
(?P<name>rule)
```
在此语法中
- `()`：表示捕获组。
- `P<name>`：指定捕获组的名称。
- `rule`：是模式中的一条规则。

比如如下表达式：`'(?P<resource>\w+)/(?P<id>\d+)'`，在此语法中，resource是第一个捕获组的名称，id 是第二个捕获组的名称
```py
import re
s = 'news/100'
pattern = '(?P<resource>\w+)/(?P<id>\d+)'
matches = re.finditer(pattern, s)
for match in matches:
    print(match.groupdict())
```
输出结果：`{'resource': 'news', 'id': '100'}`

在此示例中，`groupdict()` 方法返回一个键为组名、值为子组的字典

再看一个例子：
```py
import re
s = 'news/2021/12/31'
pattern = '(?P<resource>\w+)/(?P<year>\d{4})/(?P<month>\d{1,2})/(?P<day>\d{1,2})'
matches = re.finditer(pattern, s)
for match in matches:
    print(match.groupdict())
```

## 2、非匹配组

有时可能想创建一个组，但不想在匹配的组中捕获它。为此，可以使用非匹配组，语法如下：`(?:X)`

先看一个示例，匹配 `Python 3.10` 的版本号，和主版本、次版本
```py
import re
s = 'Python 3.10'
pattern = '(\d+)\.(\d+)'
match = re.search(pattern, s)
# show the whole match
print(match.group())
# show the groups
for group in match.groups():
    print(group)
```
输出结果：
```py
3.10
3
10
```
假设不想捕捉字面字符 (`.`) 前面的数字，可以使用这样的非匹配组：
```py
import re
s = 'Python 3.10'
pattern = '(?:\d+)\.(\d+)'
match = re.search(pattern, s)
# show the whole match
print(match.group())
# show the groups
for group in match.groups():
    print(group)
```
其中`(?:\d+)` 就是非匹配组

使用非匹配组的原因是为了节省内存，因为 regex 引擎不需要在缓冲区中存储分组。

## 3、回溯引用


# 参考资料

- [Regex Cheat Sheet](https://www.pythontutorial.net/python-regex/python-regex-cheat-sheet/)
