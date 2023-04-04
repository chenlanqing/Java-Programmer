
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
                <div style={{background:"red"}}>2222222222</div>
                {/*如果样式是class，需要通过 className来指定格式*/}
                <div className="active">3333333333333333333333</div> 
            </div>
        )
    }
}
```


# 参考资料

- [React英文文档](https://reactjs.org/)
- [React中文文档](https://zh-hans.reactjs.org/)
