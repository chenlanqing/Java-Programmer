# 1、概述

Node.js使用单线程、非阻塞和事件驱动的执行模式，这与网络浏览器中JavaScript的执行模式相似
- Node.js 是单线程的，单线程执行模式允许Node.js通过事件循环（Event Loop）轻松处理更多的并发请求；
- 非阻塞io：Node.js通过使用非阻塞式I/O请求解决阻塞式I/O问题；
- 事件驱动

# 2、module

Node.js默认支持一个叫做CommonJS模块的模块系统，在CommonJS模块中，Node.js将每个JavaScript文件视为一个单独的模块。

## 2.1、创建并导出
```js
// logger.js
const error = 'ERROR';
const warning = 'WARNING';
const info = 'INFO';

function log(message, level = info) {
    console.log(`${level}: ${message}`);
}
// 导出模块
module.exports.log = log;
module.exports.error = error; // 有可用使用： module.exports.fatal = error;
module.exports.info = info;
module.exports.warning = warning;
```

## 2.2、导入模块

```js
// app.js
const logger = require('./logger.js'); 
// 或者省略.js： 
const logger = require('./logger');
// 或者使用对象解构的方式
const { log, error, info, warning } = require('./logger');
```
当使用 require() 函数多次包含一个模块时，require() 函数只在第一次调用时对该模块进行一次评估，并将其放入一个缓存中；如果有导入的模块有直接执行的代码，只会执行一次；

## 2.3、模块封装函数

在Node.js执行一个模块之前，它用一个函数包装器来包装该模块内的所有代码，该包装器看起来像下面这样：
```js
(function(exports, require, module, __filename, __dirname) {
    // Module code
});
```
比如上面的例子：
```js
(function (exports, require, module, __filename, __dirname) {
    const error = 'ERROR';
    const warning = 'WARNING';
    const info = 'INFO';

    function log(message, level = info) {
        console.log(`${level}: ${message}`);
    }

    module.exports.log = log;
    module.exports.error = error;
    module.exports.info = info;
    module.exports.warning = warning;
});
```
上述实现的目的是：
- 将顶层变量（var、let和const）保持在模块范围内，而不是在全局对象中；
- 让一些特定模块的变量像全局变量一样，例如 module 和 export；

请注意，exports对象引用了module.exports：`console.log(module.exports === exports); // true`

## 2.4、总结

- 通过将变量和函数分配给module.exports对象的属性，将它们暴露给其他模块。
- 在一个模块中声明的所有变量、常量、函数、类等都在该模块的范围内，而不是全局范围。
- Node.js对一个模块只执行一次，并将结果放在缓存中供下次使用。

## 2.5、路径模块

路径模块是 node的核心模块，不需要安装，可以直接使用；

**常见属性：**
- `path.sep`：路径分隔符
- `path.delimiter`：路径定界符

**路径方法：**
- `path.basename(path, [,ext])`：返回一个指定路径的最后部分，ext参数过滤掉路径中的扩展名
    ```js
    let result = path.basename('/public_html/home/index.html'); // index.html
    result = path.basename('/public_html/home/index.html','.html'); // index
    ```
- `path.dirname(path)`：返回一个指定路径的目录名称
    ```js
    let result = path.basename('/public_html/home/index.html'); // '/public_html/home
    ```
- `path.extname(path)`：返回一个路径的扩展名
- `path.format(pathObj)`：从一个指定的路径对象中返回一个路径字符串
    ```js
    let pathToFile = path.format({
        dir: 'public_html/home/js',
        base: 'app.js'
    }); // public_html/home/js/app.js
    ```
- `path.isAbsolute(path)`：如果指定的路径是一个绝对路径，则返回true
- `path.join(...path)`：使用平台特定的分隔符将一连串的路径段连接起来，作为分隔符；将产生的路径规范化并返回
    ```js
    let pathToDir = path.join('/home', 'js', 'dist', 'app.js');
    console.log(pathToDir);
    ```
- `path.normalize(path)`：对一个指定的路径进行规范化。它还可以解决'...'和'...'段的问题
- `path.parse(path)`：方法返回一个对象，其属性代表路径元素。返回的对象有以下属性
    - root：根目录
    - dir：从根开始的目录路径
    - base：文件名+扩展名
    - name：文件名
    - ext：扩展名
    ```js
    let pathObj = path.parse('d:/nodejs/html/js/app.js');
    // 输出结果
    {
        root: 'd:/',
        dir: 'd:/nodejs/html/js',
        base: 'app.js',
        ext: '.js',
        name: 'app'
    }
    ```
- `path.relative(from, to)`：接受两个参数并返回基于当前工作目录的两个参数之间的相对路径。
- `path.resolve(...path)`：方法接受一个路径或路径段的序列，并将其解析为一个绝对路径。path.resolve()方法从右到左预置每个后续路径，直到完成绝对路径的构建。

## 2.6、OS模块

引入OS模块
```js
const os = require('os');
```
os模块为你提供了许多有用的属性和方法来与操作系统和服务器进行交互。

获取当前系统信息：
```js
let currentOS = {
    name: os.type(),
    architecture: os.arch(),
    platform: os.platform(),
    release: os.release(),
    version: os.version()
};
console.log(currentOS);
```
- `os.uptime()`方法返回系统的正常运行时间
- `os.userInfo()`方法返回关于当前用户的信息。
- `os.totalmem()`方法返回服务器的总内存，单位为字节。
- `os.cpus()`获得CPU的信息；
- `os.networkInterfaces()`方法返回一个包含网络接口信息的对象。

## 2.7、事件模块

Node.js是事件驱动的。它依靠 events 核心模块来实现事件驱动的架构；

在事件驱动模型中，一个 EventEmitter 对象触发事件，导致先前附加的事件的监听器被执行。

一个EventEmitter对象有两个主要功能：
- 发出一个命名的事件。
- 将一个或多个事件监听器附加和分离到指定的事件上

在Node.js中，许多核心模块都继承了EventEmitter类，包括http模块

### 2.7.1、基本使用

使用步骤：
- 引入 events 模块
    ```js
    const EventEmitter = require('events');
    ```
- 创建 EventEmitter 实例
    ```js
    const emitter = new EventEmitter();
    ```
- 通过使用`on()`方法将一个或多个事件处理程序附加到该事件上。
    ```js
    emitter.on('saved', ) => {
        console.log(`A saved event occurred.`);
    });
    ```
- 使用EventEmitter对象的emit()方法，发送保存的事件
    ```js
    emitter.emit('saved');
    ```

### 2.7.2、带参数的事件

```js
const EventEmitter = require('events');
const emitter = new EventEmitter();
emitter.on('saved',  (arg) => {
    console.log(`A saved event occurred: name: ${arg.name}, id: ${arg.id}`);
});
emitter.emit('saved', {
    id: 1,
    name: 'John Doe'
});
```
emit 函数接收两个参数，第一个是事件名称，第二个参数是一个对象

### 2.7.3、移除时间监听器

要从一个事件中移除出一个事件监听器，你可以使用`EventEmitter`对象的`off()`方法
```js
const EventEmitter = require('events');
const emitter = new EventEmitter();
// declare the event handler
function log(arg) {
    console.log(`A saved event occurred, name: ${arg.name}, id: ${arg.id}`);
}
// attach the event listener to the saved event
emitter.on('saved', log);
// emit the saved event
emitter.emit('saved', {
    id: 1,
    name: 'John Doe'
});
// remove the event listener
emitter.off('saved', log);
// no effect
emitter.emit('saved', {
    id: 2,
    name: 'Jane Doe'
});
```

### 2.7.4、继承EventEmitter

```js
const EventEmitter = require('events');
class Stock extends EventEmitter {
    constructor(symbol, price) {
        super();
        this._symbol = symbol;
        this._price = price;
    }
    set price(newPrice) {
        if (newPrice !== this._price) {
            this.emit('PriceChanged', {
                symbol: this._symbol,
                oldPrice: this._price,
                newPrice: newPrice,
                adjustment: ((newPrice - this._price) * 100 / this._price).toFixed(2)
            });
        }
    }
    get price() {
        return this._price;
    }
    get symbol() {
        return this._symbol;
    }
}
// 使用
const stock = new Stock('AAPL', 700);
stock.on('PriceChanged', (arg) => {
    console.log(`The price of the stock ${arg.symbol} has changed ${arg.adjustment}%`);
})
stock.price = 720;
```

## 2.8、http模块

基本使用：
```js
const http = require('http');
const server = http.createServer((req, res) => {
    if (req.url === '/') {
        res.write('<h1>Hello, Node.js!</h1>');
    }
    res.end();
});
server.listen(5000);
console.log(`The HTTP Server is running on port 5000`);
```
日常不会使用 http 模块，而是使用一个 express 模块来处理HTTP请求和响应；

# 3、ES模块

Node.js有两种类型的模块：ES模块和CommonJS模块；默认情况下，Node.js将JavaScript视为CommonJS模块。但可以告诉Node.js将JavaScript代码作为ES模块处理；

如何处理：
- 以`.mjs`结尾的文件；
- 当最近的父级`package.json`文件包含一个顶层字段 "type"，其值为 "module "时，以.js结尾的文件。
- 传入参数：`--eval` 或者 通过STDIN与标志`-input-type=module`连接到节点命令

# 4、express

- [expressjs](https://expressjs.com/)

express 是一个用于node.js的网络框架，使用前需要安装express：`npm install express`
```js
const express = require("express"); // 加载 express模块
const app = express(); // 创建 express实例
// 定义一个路由处理器，，路径是：/，后面是一个回调函数
app.get("/", (request, response) => {
    response.send("Hi there");
});
// 监听3000端口
app.listen(3000, () => {
    console.log("Listen on the port 3000...");
});
```

# 5、process模块

process模块也是node.js的一个核心模块，该模块有 env 属性，包含所有环境变量；
```sh
# windows设置环境变量
SET NODE_ENV=development
# mac linux设置环境变量
EXPORT NODE_ENV=development
```
读取环境变量：
```js
const nodeEnv = process.env.NODE_ENV || 'dev' // 这里设置了，没有设置的话，读取到的环境变量是 undefined
```
可以通过环境变量来控制加载的配置或代码，亦或可以控制代码的运行；



# Node框架

- Express
- [Nest](https://github.com/nestjs/nest)
