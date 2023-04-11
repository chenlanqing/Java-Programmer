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

## 2.6、事件处理

采用on+事件名的方式来绑定一个事件，注意，这里和原生的事件是有区别的，原生的事件全是小写onclick , React里的事件是驼峰 onClick

React并不会真正的绑定事件到每一个具体的元素上，而是采用事件代理的模式

事件handler写法有四种：
- 直接在render里写行内的箭头函数(不推荐)
- 在组件内使用箭头函数定义一个方法(推荐)
- 直接在组件内定义一个非箭头函数的方法，然后在render里直接使用 `onClick= {this.handleClick.bind(this)} (不推荐)`
- 直接在组件内定义一个非箭头函数的方法，然后在constructor里bind(this)(推荐)

注意，onClick={handleClick} 的结尾没有小括号！不要 调用 事件处理函数：你只需 传递给事件 即可。当用户点击按钮时，React 会调用你的事件处理函数
```js
export default class App extends Component {
    a = 100;
    render() {
        return (
            <div>
                <input />
                <button onClick={() => {console.log("click1", "处理逻辑过多时不推荐", this.a);}}>add1</button>
                {/* 下面这种方法不推荐，因为其会涉及到 this 作用域问题，函数不需要后面加括号，加了就是加载之后立马执行了，在 handlerClick2 无法使用 this关键字*/}
                <button onClick={this.handlerClick2}>add2</button> 
                {/* 如果要在handlerClick2使用this关键字访问a，需要按照如下方式实现 */}
                <button onClick={this.handlerClick2.bind(this)}>add2</button> 
                {/* 如果不传参可以使用该方式来处理 */}
                <button onClick={this.handlerClick3}>add3</button>
                <button onClick={() => {
                    this.handlerClick4();// 推荐使用，可以传参数
                }}>add4</button>
            </div>
        )
    }
    handlerClick2() {
        // 这里的this作用域是调用者本身，而调用者在这里的是 App里的 render 方法
        console.log("handlerClick2", this.a);
    }
    handlerClick3 = (evt) => {
        console.log("handlerClick3", this.a, evt.target);
    }
    handlerClick4 = () => {
        console.log("handlerClick4", this.a);
    }
}
```
和普通浏览器一样，事件handler会被自动传入一个 event 对象，这个对象和普通的浏览器 event 对象所包含的方法和属性都基本一致。不同的是 React中的 event 对象并不是浏览器提供的，而是它自己内部所构建的。它同样具有 event.stopPropagation 、 event.preventDefault 这种常用的方法
```js
handlerClick3 = (evt) => {
    console.log("handlerClick3", this.a, evt.target);
}
```

## 2.7、Ref应用

Ref 允许我们访问 DOM 节点或在 render 方法中创建的 React 元素

一般写法：在input上定义一个 ref属性，然后在事件处理中调用:`this.refs,myName` 可以获取到当前节点；
```js
import React, { Component } from 'react'
export default class App extends Component {
    render() {
        return (
            <div>
                <input ref="myName"/>
                <button onClick={() => {
                    console.log("click", this.refs.myName.value);
                }}>add1</button>
            </div>
        )
    }
}
```
但是上述在严格模式下，在控制台会报错，但是还是可以用的
```js
root.render(
    <React.StrictMode>
        <App />
    </React.StrictMode>
);
```
另外一种写法就是定义一个变量，然后在里面引用：
```js
myRef = React.createRef()
<input ref={this.myRef}/>
<button onClick={() => {
    console.log("click", this.myRef.current.value);
}}>add1</button>
```
要获得ref配置的节点的固定写法：`this.<ref-variable-name>.current`，比如这里的 this.myRef.current ；

# 3、组件数据挂载

## 3.1、状态（state）

状态就是组件描述某种显示情况的数据，由组件自己设置和更改，也就是说由组件自己维护，使用状态的目的就是为了在不同的状态下使组件的显示不同(自己管理)

**定义状态：**
- 方式1：
    ```js
    export default class App extends Component {
        state = {
            condition: true
        }
        render() {
            return (
                <div>
                    <h1>欢迎来到React开发</h1>
                    <button onClick={() => {
                        this.setState({
                            condition: !this.state.condition
                        })
                    }}>{this.state.condition ? '收藏' : '取消收藏'}</button>
                </div>
            )
        }
    }
    ```
- 方式2：
    ```js
    export default class App extends Component {
        constructor() {
            super() // 注意这里因为App继承了Component，这里需要调用super()
            this.state = {
                text: "收藏",
                condition: true
            }
        }
        render() {
            return (
                <div>
                    <h1>欢迎来到React开发</h1>
                    <button onClick={() => {
                        this.setState({
                            condition: !this.state.condition
                        })
                    }}>{this.state.condition ? '收藏' : '取消收藏'}</button>
                </div>
            )
        }
    }
    ```

**设置状态：setState**

对于状态的更改必须要调用 setState 方式来实现：
```js
this.setState({
    ...
})
```
setState有两个参数：第一个参数可以是对象，也可以是方法return一个对象，我们把这个参数叫做 updater
- 参数是对象：
    ```js
    this.setState({
        condition: !this.state.condition
    })
    ```
- 参数是方法：
    ```js
    this.setState((prevState, props) => {
        return {
            // 注意的是这个方法接收两个参数，第一个是上一次的state, 第二个是props
            condition: !prevState.condition
        }
    })
    ```
setState 是异步的，所以想要获取到最新的state，没有办法获取，就有了第二个参数，这是一个可选的回调函数
```js
this.setState((prevState, props) => {
    return {
        // 注意的是这个方法接收两个参数，第一个是上一次的state, 第二个是props
        condition: !prevState.condition
    }
}, () => {
    console.log('回调里的',this.state.condition)
})
console.log('setState外部的',this.state.condition)
```


## 3.3、数据渲染

列表渲染，一般可以使用数组的map函数，比如说数据是
```js
export default class App extends Component {
    state = {
        list: ["1111", "2222", "3333"]
    }
    render() {
        // 也可以定义变量的方式
        // var newList = this.state.list.map(item => <li>{item}</li>)
        return (
            <div>
                <ul>
                    {/* {
                        newList
                    } */}
                    {
                        this.state.list.map(
                            (item, index) => <li key={index}>{item}</li>
                        )
                    }
                </ul>
            </div>
        )
    }
}
```
React的高效依赖于所谓的 Virtual-DOM，尽量不碰 DOM。对于列表元素来说会有一个问题：元素可能会在一个列表中改变位置。要实现这个操作，只需要交换一下 DOM 位置就行了，但是React并不知道其实我们只是改变了元素的位置，所以它会重新渲染后面两个元素（再执行 Virtual-DOM ），这样会大大增加 DOM 操作。但如果给每个元素加上唯一的标识，React 就可以知道这两个元素只是交换了位置，这个标识就是 key ，这个 key 必须是每个元素唯一的标识

## 3.4、dangerousHtml

如果需要展示富文本数据，或者展示后端返回的html数据，可以使用： dangerousSetInnerHTML 来实现：
```js
<span dangerouslySetInnerHTML={
    {
        __html: item.name
    }
}></span>
```
上面的 `__html` 是固定写法，即显示具体的包含html标签的文本

> 注意这里处理的文本必须是足够信任的数据，否则容易被人攻击；

# 4、表单组件

# 参考资料

- [React英文文档](https://reactjs.org/)
- [React中文文档](https://zh-hans.reactjs.org/)
