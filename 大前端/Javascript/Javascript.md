# 一、基础知识

## 1、语法

## 2、变量

## 3、数据类型

## 4、数组常用方法

数组的主要方法：

方法 | 含义
-----|-----
Array.push(element) | 数组末尾添加元素
Array.unshift(element) | 数组头部添加元素
Array.pop() | 删除末尾的元素，并返回被删除的元素
Array.shift() | 删除头部的元素，并返回被删除的元素
[Array.splice(position, num)](https://www.javascripttutorial.net/javascript-array-splice/) | 删除指定位置数据，其中position 表示指定要删除的第一个元素的位置，后面 num 要删除的元素个数；该方法会修改原始数组，并将被删除的元素作为一个数组返回；
Array.splice(position,0,new_element_1,new_element_2,...) | 指定位置插入元素，position表示新元素插入的起始位置，第二个参数是0表示不删除数组任何元素，后面的参数表示需要插入的元素，返回一个空数组
Array.splice(position, num,new_element_1,new_element_2,... ) | num > 0 表示要删除position位置的元素
[Array.slice(start, stop)](https://www.javascripttutorial.net/javascript-array-slice/) | 获取数组的子集，从 [start , stop-1]，如果缺省， start默认是从0开始，stop默认是数组的长度
[Array.map(callback)](https://www.javascripttutorial.net/javascript-array-map/) | 将一个数组通过某些条件，转换为另外一个数组；对数组中的每个元素都调用一个 callback，并返回一个包含结果的新数组。callback可以使用箭头函数
[Array.filter(callback)](https://www.javascripttutorial.net/javascript-array-filter/) | 数组中过滤数据
[Array.reduce(callback)](https://www.javascripttutorial.net/javascript-array-reduce/) | 将一个数组转换为一个值


# 二、操作符

# 三、控制流

# 四、函数

# 五、对象与原型

# 六、类

# 七、高级函数

## fetch

- [Fetch API-user guide](https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API)

# 八、Promises & Async/Await

# 九、Iterators & Generators

# 十、正则表达式

- [JavaScript完整正则表达式](https://juejin.cn/post/6844903487155732494)

正则是匹配模式，要么匹配字符，要么匹配位置

## 1、基本使用

```js
let reg = /hi/; // 不需要双引号或单引号
let re = new RegExp('hi');
```
test()方法，允许你测试一个字符串是否包含正则表达式中模式的匹配
```js
let re = /hi/;
let result = re.test('hi John');
console.log(result); // true
```

### 1.2、可选标记

正则表达式有多个标记，可以改变正则表达式的搜索行为

（1）忽略标记：`i`，忽略大小写；正则默认是区分大小写的
```js
let re = /hi/i;
let result = re.test('Hi John');
console.log(result); // true
let re = new RegExp('hi','i');
```
（2）全局标志-`g`：如果没有全局标志，正则表达式对象只检查在一个字符串中是否有匹配，并返回第一个匹配的结果；当`g`标志可用时，正则表达式寻找所有的匹配并返回所有的匹配；

可以合并标志，例如，`gi`标志合并了忽略（`i`）和全局（`g`）标志。

正则表达式的`exec()`方法在一个字符串中进行匹配搜索，并返回一个包含匹配详细信息的数组。如果没有匹配到，返回 null；该方法每次只返回一个匹配的，如果要找到全部匹配的数据，需要执行多次，一般使用 循环来处理：
```js
let message = 'Hi, are you there? hi, HI...';
let re = /hi/gi;
let matches = [];
let match;
do {
    match = re.exec(message);
    if(match) {
      matches.push(match);
    }
} while(match != null)
console.dir(matches);
// 输出结果如下：
[
  [
    'Hi',
    index: 0,
    input: 'Hi, are you there? hi, HI...',
    groups: undefined
  ],
  [
    'hi',
    index: 19,
    input: 'Hi, are you there? hi, HI...',
    groups: undefined
  ],
  [
    'HI',
    index: 23,
    input: 'Hi, are you there? hi, HI...',
    groups: undefined
  ]
]
```

### 1.3、match方法

方法`str.match(regexp)`返回字符串中匹配到正则的数据，如果要找到所有匹配，需要使用`g`标志来处理
```js
let str = "Are you Ok? Yes, I'm OK";
let result = str.match(/OK/gi);
console.log(result);
// 输出
["Ok", "OK"]
```

### 1.4、replace

```js
let str = "Are you OK? Yes, I'm OK.";
let result = str.replace('Ok','fine'); // 只替换一个
console.log(result); // Are you fine? Yes, I'm OK
// 全局替换
let str = "Are you OK? Yes, I'm OK.";
let result = str.replace(/OK/g,'fine');
console.log(result); // Are you fine? Yes, I'm fine.
```

## 2、字符组

范围表示法：比如`[123456abcdefGHIJKLM]`，可以写成`[1-6a-fG-M]`。用连字符-来省略和简写；因为连字符有特殊用途，那么要匹配“a”、“-”、“z”这三者中任意一个字符，该怎么做呢？可以写成如下的方式：[-az]或[az-]或[a\-z]。即要么放在开头，要么放在结尾，要么转义

排除字符：`[^abc]`，表示是一个除"a"、"b"、"c"之外的任意一个字符。字符组的第一位放`^`（脱字符），表示求反的概念`

一个字符类允许你从某一字符集中匹配任何符号。字符类也被称为字符集，通常可以结合使用
- `\d`：匹配单个数字或0至9的字符
- `\s`：匹配一个单一的空白符号，如空格、制表符（Tab）、换行符（N）。
- `\w`：w代表单词字符。它匹配ASCII字符[A-Za-z0-9_]，包括拉丁字母、数字和下划线（_）。
- `\D`：匹配除数字以外的任何字符，例如，字母。
- `\S`：匹配除空格以外的任何字符，例如，一个字母
- `\W`：匹配任何字符，除了单词字符，如非拉丁字母或空格。
- `.`：是一个特殊的字符类，可以匹配除换行之外的任何字符；如果你想使用点（`.`）字符类来匹配任何字符，包括换行，你可以使用`s`标志：`let re = /ES.6/s`

如果要匹配任意字符怎么办？可以使用`[\d\D]`、`[\w\W]`、`[\s\S]`和`[^]`中任何的一个

JS中字符串表示`\`时，也要转义

## 3、量词

量词也称重复。掌握{m,n}的准确含义后，只需要记住一些简写形式

- `{m,}` 表示至少出现m次。
- `{m}` 等价于`{m,m}`，表示出现m次。
- `?` 等价于`{0,1}`，表示出现或者不出现。记忆方式：问号的意思表示，有吗？
- `+` 等价于`{1,}`，表示出现至少一次。记忆方式：加号是追加的意思，得先有一个，然后才考虑追加。
- `*` 等价于`{0,}`，表示出现任意次，有可能不出现。记忆方式：看看天上的星星，可能一颗没有，可能零散有几颗，可能数也数不过来。

**贪婪匹配和惰性匹配**
- 贪婪匹配：只要能力范围内，越多越好；
    ```js
    var regex = /\d{2,5}/g;
    var string = "123 1234 12345 123456";
    console.log( string.match(regex) ); // => ["123", "1234", "12345", "12345"]
    ```
- 惰性匹配：尽可能少的匹配
    ```js
    var regex = /\d{2,5}?/g;
    var string = "123 1234 12345 123456";
    console.log( string.match(regex) ); // => ["12", "12", "34", "12", "34", "12", "34", "56"]
    ```

通过在量词后面加个问号就能实现惰性匹配，因此所有惰性匹配情形如下：
```js
{m,n}?
{m,}?
??
+?
*?
```
量词后面加个问号，问一问你知足了吗，你很贪婪吗？

## 4、多选分支

多选分支可以支持多个子模式任选其一；具体形式如下：`(p1|p2|p3)`，其中`p1`、`p2`和`p3`是子模式，用`|`（管道符）分隔，表示其中任何之一；
```js
var regex = /good|goodbye/g;
var string = "goodbye";
console.log( string.match(regex) ); 
// => ["good"]
var regex = /goodbye|good/g;
var string = "goodbye";
console.log( string.match(regex) ); 
// => ["goodbye"]
```
分支结构也是惰性的，即当前面的匹配上了，后面的就不再尝试了


## 5、位置匹配

正则表达式是匹配模式，要么匹配字符，要么匹配位置；

位置是相邻字符之间的位置，可以把位置理解为老空字符：""，比如hello可以理解为：`"hello" == "" + "h" + "" + "e" + "" + "l" + "" + "l" + "o" + "";`

### 5.1、^和$

- `^`（脱字符）匹配开头，在多行匹配中匹配行开头；
- `$`（美元符号）匹配结尾，在多行匹配中匹配行结尾；

比如把字符串的开头和结尾用"#"替换（位置可以替换成字符的！）：
```js
var result = "hello".replace(/^|$/g, '#');
console.log(result); // => "#hello#"
```
多行匹配模式时，二者是行的概念：
```js
var result = "I\nlove\njavascript".replace(/^|$/gm, '#');
console.log(result);
/*
#I#
#love#
#javascript#
*/
```

### 5.2、\b和\B

- `\b`是单词边界，具体就是`\w`和`\W`之间的位置，也包括`\w`和`^`之间的位置，也包括`\w`和`$`之间的位置
- `\B`就是`\b`的反面的意思，非单词边界，例如在字符串中所有位置中，扣掉`\b`，剩下的都是`\B`的。具体说来就是`\w`与`\w`、`\W`与`\W`、`^`与`\W`，`\W`与`$`之间的位置

```js
var result = "[JS] Lesson_01.mp4".replace(/\b/g, '#');
console.log(result); 
// => "[#JS#] #Lesson_01#.#mp4#"
var result = "[JS] Lesson_01.mp4".replace(/\B/g, '#');
console.log(result); 
// => "#[J#S]# L#e#s#s#o#n#_#0#1.m#p#4"
```

### 5.3、(?=p)和(?!p)

- `(?=p)`，其中p是一个子模式，即p前面的位置，比如`(?=l)`，表示'l'字符前面的位置；
- `(?!p)`就是`(?=p)`的反面意思

### 5.4、案例

千分位：`/\B(?=(\d{3})+\b)/g`，支持 "1234567 123456"



# 参考资料

- [javascript tutorial](https://www.javascripttutorial.net/)
- [V8 Engine](https://v8.dev/)
