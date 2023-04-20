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

### 3.1.1、定义状态

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

### 3.1.2、设置状态：setState

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

**setState 如果是在同步逻辑中是异步的更新的，所以想要获取到最新的state，没有办法获取，就有了第二个参数，这是一个可选的回调函数**
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
$\color{blue}{但是如果 setState 在异步的逻辑中执行，同步更新状态，比如 setTimeout，看如下代码}$
```js
handleAdd2 = () => {
    setTimeout(() => {
        this.setState({ count: this.state.count + 1 })
        console.log(this.state.count)
        this.setState({ count: this.state.count + 1 })
        console.log(this.state.count)
        this.setState({ count: this.state.count + 1 })
        console.log(this.state.count)
    }, 0);
}
```
$\color{red}{注意：有一点需要注意：在最新的React18中，不管是出在同步还是异步的逻辑中， setState 都是同步的；要获取最新的值只能通过 setState函数的第二个的回调参数}$

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

## 3.5、条件渲染

条件渲染，可以通过三目运算符来渲染：
- `{ {this.state.list.length === 0 ? <div>暂无内容</div> : null} }`
- `{this.state.list.length === 0 && <div>暂无内容</div>}`

可以通过样式来控制条件渲染，这种就是本身节点已经存在了，前面两种呢不显示就是节点不存在的
`{<div className={this.state.list.length === 0 ? '' : 'hidden'}>暂无内容</div> }`

## 3.6、属性

props 是正常是外部传入的，组件内部也可以通过一些方式来初始化的设置，属性不能被组件自己更改，但是可以通过父组件主动重新渲染的方式来传入新的 props；

属性是描述性质、特点的，组件自己不能随意更改。

### 3.6.1、基本使用

总的来说，在使用一个组件的时候，可以把参数放在标签的属性当中，所有的属性都会作为组件 props 对象的键值。通过箭头函数创建的组件，需要通过函数的参数来接收 props

比如有一个Navbar组件
```jsx
// Navbar.js
export default class Navbar extends Component {
    render() {
        // 展开赋值，对象解构，title 对应属性名称
        let { title, leftShow } = this.props
        return (
            <div>
                {leftShow && <button>返回</button> }
                Navbar-{title}
                <button>Home</button>
            </div>
        )
    }
}
// App.js
export default class App extends Component {
    render() {
        return (
            <div>
                <div>
                    <h2>首页</h2>
                    <Navbar title="首页" leftShow={false}/>
                </div>
                    <h2>列表</h2>
                    <Navbar title="列表" leftShow={true} />
                </div>
            </div>
        )
    }
}
```
- 在组件上通过`key=value`写属性,通过`this.props`获取属性,这样组件的可复用性提高了；
- 注意，在传参数时，对于布尔类型，不能写成：`leftShow="false"`，可以写成如下：`leftShow={false}`；
- 属性赋值，可以通过如下方式：`let { title, leftShow } = this.props`

类组件一般是通过上面写法：`this.props` 来实现的

但是，对于函数式组件，就不能按照上面的写法，因为函数没有 this，需要通过形参传入的方式：
```jsx
import React from 'react'

export default function Sidebar(props) {
    console.log(props) // 这里接收的就是传入的属性
    var {title} = props
    return (
        <div>
            <ul>
                <li>1111111</li>
                <li>1111111</li>
            </ul>
        </div>
    )
}
```

### 3.6.2、属性验证

前面讲到，对于布尔类型的赋值，如果写成：`leftShow="false"`，那么在使用的时候直接作为判断条件是无法生效的，因为其是一个字符串；

为了对属性的类型作验证，可以使用如下方式：
```jsx
// 类组件和函数式组件都可以按照如下方式写
import propTypes from 'prop-types'
Navbar.propTypes = {
    title: propTypes.string,
    leftShow: propTypes.bool // 定义该属性为 bool，如果传字符串，控制台会报错；
}
```
或者直接使用类属性的定义方式：（只支持类属性的写法）
```jsx
import propTypes from 'prop-types'
export default class Navbar extends Component {
    static propTypes = {
        title: propTypes.string,
        leftShow: propTypes.bool
    }
    render() {
        ...
    }
}
```

### 3.6.3、默认属性值

```jsx
// 类组件和函数式组件都支持该写法：defaultProps 是固定写法
Navbar.defaultProps = {
    leftShow: true
}
// 或者写成如下写法：
static defaultProps = {
    leftShow: true
}
```

### 3.6.4、注意点

如果定义的对象内部的属性跟引用组件用到的属性是一致的，那么可以直接用简写的写法：`{...obj}`，即对象解构
```jsx
export default class App extends Component {
    render() {
        var obj = {
            title: "测试",
            leftShow : false,
        }
        return (
            <div>
                <div>
                    <h2>首页</h2>
                    <Navbar title="首页" leftShow={false} />
                </div>
                <div>
                    <h2>测试属性相同</h2>
                    {/* 属性相同 */}
                    <Navbar {...obj}/>
                </div>
            </div>
        )
    }
}
```

### 3.6.5、属性与状态

- 相似点：都是纯js对象，都会触发render更新，都具有确定性（状态/属性相同，结果相同）
- 不同点：
    - 属性能从父组件获取，状态不能
    - 属性可以由父组件修改，状态不能
    - 属性能在内部设置默认值，状态也可以，设置方式不一样
    - 属性不在组件内部修改，状态要在组件内部修改
    - 属性能设置子组件初始值，状态不可以
    - 属性可以修改子组件的值，状态不可以

`state` 的主要作用是用于组件保存、控制、修改自己的可变状态。 state 在组件内部初始化，可以被组件自身修改，而外部不能访问也不能修改。你可以认为 `state` 是一个局部的、只能被组件自身控制的数据源。 state 中状态可以通过 `this.setState` 方法进行更新， setState 会导致组件的重新渲
染。

`props` 的主要作用是让使用该组件的父组件可以传入参数来配置该组件。它是外部传进来的配置参数，组件内部无法控制也无法修改。除非外部组件主动传入新的 props ，否则组件的 props 永远保持不变。

没有 state 的组件叫无状态组件（stateless component），设置了 state 的叫做有状态组件（stateful component）。因为状态会带来管理的复杂性，我们尽量多地写无状态组件，尽量少地写有状态的组件。这样会降低代码维护的难度，也会在一定程度上增强组件的可复用性

# 4、表单中的受控组件与非受控

## 4.1、非受控组件

React要编写一个非受控组件，可以 使用 ref 来从 DOM 节点中获取表单数据，就是非受控组件；因为非受控组件将真实数据储存在 DOM 节点中，所以在使用非受控组件时，有时候反而更容易同时集成 React 和非 React 代码。如果你不介意代码美观性，并且希望快速编写代码，使用非受控组件往往可以减少你的代码量。否则，你应该使用受控组件
```jsx
import React, { Component } from 'react'
export default class App extends Component {
    textRef = React.createRef();
    render() {
        return (
            <div>
                <h1>登录页</h1>
                <input ref={this.textRef} value="Jayden"/>
                <button onClick={() => {
                    console.log(this.textRef.current.value);
                }}>登录</button>
                <button onClick={() => {
                    this.textRef.current.value = ""
                }}>重置</button>
            </div>
        )
    }
}
```
**默认值：**
在 React 渲染生命周期时，表单元素上的 value 将会覆盖 DOM 节点中的值，在非受控组件中，你经常希望 React 能赋予组件一个初始值，但是不去控制后续的更新。 在这种情况下, 你可以指定一个 `defaultValue` 属性，而不是 value
```jsx
<input ref={this.textRef} defaultValue="Jayden"/>
```
同样，`<input type="checkbox">` 和 `<input type="radio">` 支持 defaultChecked ， `<select>` 和 `<textarea>` 支持 defaultValue

## 4.2、受控组件

由于在表单元素上设置了 value 属性，因此显示的值将始终为 this.state.value ，这使得 React 的 state 成为唯一数据源。由于 handlechange 在每次按键时都会执行并更新 React 的 state，因此显示的值将随着用户输入而更新。
```jsx
import React, { Component } from 'react'
export default class App extends Component {
    state = {
        username: "Jayden"
    }
    render() {
        return (
            <div>
                {/* 如果使用value属性赋值， 必须要有一个 onChange 事件处理 */}
                <input value={this.state.username} onChange={(evt) => {
                    this.setState({ username: evt.target.value })
                }} />
                <button onClick={() => {
                    console.log(this.state.username);
                }}>登录</button>
                <button onClick={() => {
                    this.setState({
                        username : ""
                    })
                }}>重置</button>
            </div>
        )
    }
}
```
对于受控组件来说，输入的值始终由 React 的 state 驱动。你也可以将 value 传递给其他 UI 元素，或者通过其他事件处理函数重置，但这意味着你需要编写更多的代码；也就是说会重新调用render函数

> React组件的数据渲染是否被调用者传递的 props 完全控制，控制则为受控组件，否则非受控组件

# 5、组件通信方式

## 5.1、父子组件通信方式

**受控组件中父子组件通信**：父传子：属性方式；子传父：回调函数
```jsx
class Navbar extends Component {
    render() {
        return (
            <div style={{ background: "red", width: "200px" }}>
                <button onClick={() => {
                    // 调用父类传递的属性，其实这个属性是一个回调函数，可以直接执行
                    this.props.callback()
                }}>click</button>
                <span>Navbar</span>
            </div>
        )
    }
}
class Sidebar extends Component {
    render() {
        return (
            <div style={{ background: "yellow" }}>
                <ul>
                    <li>1111111</li>
                </ul>
            </div>
        )
    }
}
// App 组件包含两个子组件，现在需要通过Navbar组件控制Sidebar组件的显示与否
export default class App extends Component {
    state = {
        isSidebarShow: true,
    }
    render() {
        return (
            <div>
                <Navbar callback={this.handlerChildEvent} />
                {this.state.isSidebarShow && <Sidebar />}
            </div>
        )
    }
    // 该函数被子类通知调用
    handlerChildEvent = () => {
        this.setState({
            isSidebarShow : !this.state.isSidebarShow
        })
    }
}
```

**表单域组件中父子通信方式：**ref标记 (父组件拿到子组件的引用，从而调用子组件的方法)；在父组件中清除子组件的input输入框的value值。`this.refs.form.reset()`
```jsx
class Field extends Component {
    state = {
        value: ""
    }
    clear(){
        this.setState({ value: "" })
    }
    render() {
        return (
            <div style={{ background: "yellow" }}>
                <label>{this.props.label}</label>
                <input type={this.props.type} onChange={(evt) => {
                    this.setState({
                        value: evt.target.value
                    })
                }} value={this.state.value}/>
            </div>
        )
    }
}
export default class App extends Component {
    username = React.createRef();
    password = React.createRef();
    render() {
        return (
            <div>
                <h2>登录页面</h2>
                <Field label="用户名:" type="text" ref={this.username} />
                <Field label="密码:" type="password" ref={this.password} />
                <button onClick={() => {
                    console.log("username:", this.username.current.state.value)
                    console.log("password:", this.password.current.state.value)
                }}>登录</button>
                <button onClick={() => {
                    this.username.current.clear();
                    this.password.current.clear();
                }}>取消</button>
            </div>
        )
    }
}

```


# 开源组件

- [移动端-平滑滚动组件](https://github.com/ustbhuangyi/better-scroll)

# 参考资料

- [React英文文档](https://reactjs.org/)
- [React中文文档](https://zh-hans.reactjs.org/)

