<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**目录**

- [1、脚手架](#1%E8%84%9A%E6%89%8B%E6%9E%B6)
- [2、JSX语法与组件](#2jsx%E8%AF%AD%E6%B3%95%E4%B8%8E%E7%BB%84%E4%BB%B6)
  - [2.1、Jsx语法](#21jsx%E8%AF%AD%E6%B3%95)
  - [2.2、class组件](#22class%E7%BB%84%E4%BB%B6)
  - [2.3、函数式组件](#23%E5%87%BD%E6%95%B0%E5%BC%8F%E7%BB%84%E4%BB%B6)
  - [2.4、组件嵌套](#24%E7%BB%84%E4%BB%B6%E5%B5%8C%E5%A5%97)
  - [2.5、组件样式](#25%E7%BB%84%E4%BB%B6%E6%A0%B7%E5%BC%8F)
- [参考资料](#%E5%8F%82%E8%80%83%E8%B5%84%E6%96%99)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->


# 1、脚手架

前提需要安装 node
```
npx create-react-app <your-app-name>
```
npm包的仓库管理软件
```
npm i -g nrm
```
快速切换源`nrm use <registry name>`

# 2、JSX语法与组件

## 2.1、Jsx语法

`jsx = javascript + xml`；所谓的 JSX 其实就是 JavaScript 对象，所以使用 React 和 JSX 的时候一定要经过编译的过程；

`JSX —使用react构造组件，bable进行编译` —> `JavaScript对象` —> `ReactDOM.render()` —> `DOM元素` —> `插入页面`

```js
import React from 'react';
import ReactDOM from 'react-dom';
ReactDOM.render(
    React.createElement("div", {
        id: "aaa",
        class: "bbb"
    }, "Welcome"),
    document.getElementById("root")
)
```

## 2.2、class组件

react创建组件的方式就是使用的类的继承，ES6 class 是目前官方推荐的使用方式，它使用了ES6标准语法来构建
```js
import React from 'react'
import ReactDOM from 'react-dom'

class App extends React.Component { 
    render () { 
        return ( <h1>欢迎进入React的世界</h1> ) 
    } 
}
ReactDOM.render( <App />, document.getElementById('root') )
```
如果通过定义文件的形式来定义class组件，注意class组件的导入导出；
```js
// class组件文件：app_comp.js
import React from 'react';
class App extends React.Component {
    render() {
        return (
            <div className='app' id='appRoot'>
                <h1 className='title'>欢迎进入React的世界</h1>
                <p>React.js 是一个构建页面 UI 的库 </p>
            </div>)
    }
}
export default App // 注意需要导出

// 另一个文件：index.js
import React from 'react';
import ReactDOM from 'react-dom';
import App from 'app_comp'; // 需要导入App
ReactDOM.render(
    <App />,
    document.getElementById("root")
)
```

**注意：**
- 导入组件默认字母是大写的，如果是小写的会报错：`The tag <app> is unrecognized in this browser. If you meant to render a React component, start its name with an uppercase letter.`s
- 定义组件时不能有多个并列的标签，比如下面的，不能存在两个并列的div标签s
    ```js
    return (
        <div className='app' id='appRoot'>
            <h1 className='title'>欢迎进入React的世界</h1>
            <p>React.js 是一个构建页面 UI 的库 </p>
        </div>
        <div></div>
        )
    ```

## 2.3、函数式组件

组件名必须大写
```js
// func_comp.js
function App() {
    return (
        <div>
            函数式组件
            <h1>1111111111</h1>
            <h1>2222222222</h1>
        </div>
    )
}
export default App;
// index.js
import React from 'react';
import ReactDOM from 'react-dom';
import App from 'func_comp'; // 需要导入App
ReactDOM.render(
    <App />,
    document.getElementById("root")
)
```

## 2.4、组件嵌套

组件之间是可以嵌套的，比如有一个根组件，这个组件中有多个子组件，比如如下代码
```js
import React, { Component } from 'react'
class Navbar extends Component {
    render() {
        return (<div>Navbar</div>)
    }
}
function Swiper() {
    return (<div>Swiper</div>)
}
const Tabbar = () => <div>Tabbar</div>
// Navbar、Swiper、Tabbar 是组件App内部的三个子组件
export default class App extends Component {
    render() {
        return (
            <div>
                <Navbar></Navbar> 
                <Swiper></Swiper>
                <Tabbar></Tabbar>
            </div>
        )
    }
}
```

## 2.5、组件样式

React推荐我们使用行内样式，因为React觉得每一个组件都是一个独立的整体
```js
// index.css
.active{
    background-color: red;
}
// app.js
import "index.css" /*需要导入css文件*/
export default class App extends Component {
    render() {
        var myname = "kerwin"
        var obj = {
            backgroundColor:"yellow",
            fontSize:"30px"
        }
        return (
            <div>
                {10+20}-{myname}
                {10>20?'aaa':'bbb'}
                <div style={obj}>11111111111</div>
                <div style={ {background:"red"} }>2222222222</div>
                {/*如果样式是class，需要通过 className来指定格式*/}
                <div className="active">3333333333333333333333</div> 
            </div>
        )
    }
}
```
**行内样式：**

想给虚拟dom添加行内样式，需要使用表达式传入样式对象的方式来实现：
```js
// 注意这里的两个括号，第一个表示我们在要JSX里插入JS了，第二个是对象的括号
<p style={{color:'red', fontSize:'14px'}}>Hello world</p>
```
如果这么写`<p style='color: red'>1111</p>`，会报错：`The "style" prop expects a mapping from style properties to values, not a string. For example, style={{marginRight: spacing + 'em'}} when using JSX.`；行内样式需要写入一个样式对象，而这个样式对象的位置可以放在很多地方，例如 render 函数里、组件原型上、外链js文件中

**使用class**

React推荐我们使用行内样式，因为React觉得每一个组件都是一个独立的整体；需要注意的是，在React中class 需要写成className （因为毕竟是在写类js代码，会受到js规则的限制，而 class 是关键字）

> class ==> className , for ==> htmlFor(label)

另外对于类似 background-color、font-size 等属性，如果定义在样式对象中，需要按照驼峰命名的方式书写：
```
{
    backgroundColor:"yellow",
    fontSize:"30px"
}
```


# 参考资料

- [React英文文档](https://reactjs.org/)
- [React中文文档](https://zh-hans.reactjs.org/)
