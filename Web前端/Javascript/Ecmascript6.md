
# 1、let和const

## 1.1、let

ES6 新增了`let`命令，用来声明变量。它的用法类似于`var`，但是所声明的变量，只在`let`命令所在的代码块内有效
```js
{
  let a = 10;
  var b = 1;
}
a // ReferenceError: a is not defined.
b // 1
```
`for`循环的计数器，就很合适使用`let`命令，计数器i只在for循环体内有效，在循环体外引用就会报错
```js
for (let i = 0; i < 10; i++) {
  // ...
}
console.log(i); // ReferenceError: i is not defined
```

- **不存在变量提升**：var 命令会发生“变量提升”现象，即变量可以在声明之前使用，值为`undefined`；let 命令改变了语法行为，它所声明的变量一定要在声明后使用，否则报错（ReferenceError）；
- **暂时性死区**：只要块级作用域内存在let命令，它所声明的变量就“绑定”（binding）这个区域，不再受外部的影响；
    ```js
    var tmp = 123;
    if (true) {
        tmp = 'abc'; // ReferenceError: Cannot access 'tmp' before initialization
        let tmp;
    }
    ```
    ES6 明确规定，如果区块中存在let和const命令，这个区块对这些命令声明的变量，从一开始就形成了封闭作用域。凡是在声明之前就使用这些变量，就会报错；
    总而言之：在代码块内，使用let命令声明变量之前，该变量都是不可用的。这在语法上，称为“暂时性死区”（temporal dead zone，简称 TDZ）
    “暂时性死区”也意味着typeof不再是一个百分之百安全的操作
- **不允许重复声明**：let不允许在相同作用域内，重复声明同一个变量
    ```js
    function func() {
        let a = 10;
        let a = 1; // SyntaxError: Identifier 'a' has already been declared
    }
    ```

## 1.2、块级作用域

**为什么需要块级作用域？**

ES5 只有全局作用域和函数作用域，没有块级作用域，这带来很多不合理的场景
- 第一种场景，内层变量可能会覆盖外层变量；
- 第二种场景，用来计数的循环变量泄露为全局变量；

**ES6 的块级作用域**
- let实际上为 JavaScript 新增了块级作用域
- ES6 允许块级作用域的任意嵌套
    ```js
    {{{{
        {let insane = 'Hello World'}
        console.log(insane); // 报错
    }}}};
    ```
**块级作用域与函数声明**

ES6 规定，块级作用域之中，函数声明语句的行为类似于let，在块级作用域之外不可引用
```js
// 浏览器的 ES6 环境
function f() { console.log('I am outside!'); }
(function () {
  var f = undefined;
  if (false) {
    function f() { console.log('I am inside!'); }
  }
  f();
}());
// Uncaught TypeError: f is not a function
```
考虑到环境导致的行为差异太大，应该避免在块级作用域内声明函数。如果确实需要，也应该写成函数表达式，而不是函数声明语句
```js
// 块级作用域内部的函数声明语句，建议不要使用
{
  let a = 'secret';
  function f() {
    return a;
  }
}
// 块级作用域内部，优先使用函数表达式
{
  let a = 'secret';
  let f = function () {
    return a;
  };
}
```
注意：ES6 的块级作用域必须有大括号，如果没有大括号，JavaScript 引擎就认为不存在块级作用域

## 1.3、const

const声明一个只读的常量。一旦声明，常量的值就不能改变；const一旦声明变量，就必须立即初始化，不能留到以后赋值
```js
const PI = 3.1415;
PI // 3.1415
PI = 3;
// TypeError: Assignment to constant variable.
```
const的作用域与let命令相同：只在声明所在的块级作用域内有效；const命令声明的常量也是不提升，同样存在暂时性死区，只能在声明的位置后面使用。

const实际上保证的，并不是变量的值不得改动，而是变量指向的那个内存地址所保存的数据不得改动；如果真的想将对象冻结，应该使用Object.freeze方法
```js
const foo = Object.freeze({});
// 常规模式时，下面一行不起作用；
// 严格模式时，该行会报错
foo.prop = 123;
```

## 1.4、顶层对象属性

顶层对象，在浏览器环境指的是window对象，在 Node 指的是global对象。ES5 之中，顶层对象的属性与全局变量是等价的

ES6 为了改变这一点，一方面规定，为了保持兼容性，var命令和function命令声明的全局变量，依旧是顶层对象的属性；另一方面规定，let命令、const命令、class命令声明的全局变量，不属于顶层对象的属性。也就是说，从 ES6 开始，全局变量将逐步与顶层对象的属性脱钩
```js
var a = 1;
window.a // 1
let b = 1;
window.b // undefined
```

## 1.5、globalThis 对象

JavaScript 语言存在一个顶层对象，它提供全局环境（即全局作用域），所有代码都是在这个环境中运行。但是，顶层对象在各种实现里面是不统一的
- 浏览器里面，顶层对象是window，但 Node 和 Web Worker 没有window。
- 浏览器和 Web Worker 里面，self也指向顶层对象，但是 Node 没有self。
- Node 里面，顶层对象是global，但其他环境都不支持；

同一段代码为了能够在各种环境，都能取到顶层对象，现在一般是使用this关键字，但是有局限性
- 全局环境中，this会返回顶层对象。但是，Node.js 模块中this返回的是当前模块，ES6 模块中this返回的是undefined。
- 函数里面的this，如果函数不是作为对象的方法运行，而是单纯作为函数运行，this会指向顶层对象。但是，严格模式下，这时this会返回undefined。
- 不管是严格模式，还是普通模式，new Function('return this')()，总是会返回全局对象。但是，如果浏览器用了 CSP（Content Security Policy，内容安全策略），那么eval、new Function这些方法都可能无法使用
```js
// 方法一
(typeof window !== 'undefined'
   ? window
   : (typeof process === 'object' &&
      typeof require === 'function' &&
      typeof global === 'object')
     ? global
     : this);

// 方法二
var getGlobal = function () {
  if (typeof self !== 'undefined') { return self; }
  if (typeof window !== 'undefined') { return window; }
  if (typeof global !== 'undefined') { return global; }
  throw new Error('unable to locate global object');
};
```

ES2020 在语言标准的层面，引入globalThis作为顶层对象。也就是说，任何环境下，globalThis都是存在的，都可以从它拿到顶层对象，指向全局环境下的this

使用库[global-this库](https://github.com/ungap/global-this)，可以在所有环境拿到globalThis

# 2、变量解构赋值

## 2.1、数组解构赋值

ES6 允许按照一定模式，从数组和对象中提取值，对变量进行赋值，这被称为解构（Destructuring）
```js
let [a, b, c] = [1, 2, 3];
```
本质上，这种写法属于“模式匹配”，只要等号两边的模式相同，左边的变量就会被赋予对应的值，如果解构不成功，变量的值就等于undefined
```js
let [foo, [[bar], baz]] = [1, [[2], 3]];
    foo // 1
    bar // 2
    baz // 3
let [ , , third] = ["foo", "bar", "baz"];
    third // "baz"
let [x, , y] = [1, 2, 3];
    x // 1
    y // 3
let [head, ...tail] = [1, 2, 3, 4];
head // 1
    tail // [2, 3, 4]
let [x, y, ...z] = ['a'];
    x // "a"
    y // undefined
    z // []
```
另一种情况是不完全解构，即等号左边的模式，只匹配一部分的等号右边的数组。这种情况下，解构依然可以成功
```js
let [x, y] = [1, 2, 3];
x // 1
y // 2
```
如果等号的右边不是数组（或者严格地说，不是可遍历的结构(不具备 Iterator 接口)），那么将会报错

**默认值**

解构赋值允许指定默认值
```js
let [foo = true] = [];
foo // true
let [x, y = 'b'] = ['a']; // x='a', y='b'
let [x, y = 'b'] = ['a', undefined]; // x='a', y='b'
```
注意，ES6 内部使用严格相等运算符（===），判断一个位置是否有值。所以，只有当一个数组成员严格等于undefined，默认值才会生效；

如果默认值是一个表达式，那么这个表达式是惰性求值的，即只有在用到的时候，才会求值；

默认值可以引用解构赋值的其他变量，但该变量必须已经声明

## 2.2、对象解构赋值

对象的解构与数组有一个重要的不同。数组的元素是按次序排列的，变量的取值由它的位置决定；而对象的属性没有次序，变量必须与属性同名，才能取到正确的值；

如果解构失败，变量的值等于undefined

对象的解构赋值，可以很方便地将现有对象的方法，赋值到某个变量：

# 参考资料

- [阮一峰：ECMAScript 6 入门教程](https://es6.ruanyifeng.com/)
- [ES6](https://www.javascripttutorial.net/es6/)