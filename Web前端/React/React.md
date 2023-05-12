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
React18按照如下写法：
```jsx
const container = document.getElementById('root'); // 获取到dom根节点
const root = ReactDOM.createRoot(container); // 将根节点传入到 ReactDOM中创建一个根，并接手管理它里面的DOM
root.render(
    // <React.StrictMode>
        <App />
    // </React.StrictMode>
);
// React.StrictMode 标记是严格模式
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

### 3.6.6、特殊属性

什么是插槽，其实是一个特殊的props（children）来将这些无法提前预知内容的子组件传递到渲染到结果中；jsx中的所有内容都会通过children prop传递到父组件中，使用react组合的方式可以实现类似于Vue插槽的功能；

基本使用：
```jsx
export default class App extends Component {
    render() {
        return (
            <div>
                <Child>
                    <div>111111111</div>
                    <div>222222222</div>
                    <div>333333333</div>
                </Child>
            </div>
        )
    }
}
class Child extends Component {
    render() {
        return (
            <div>
                App
                {this.props.children[0]}
                {this.props.children[2]}
                {this.props.children[1]}
            </div>
        )
    }
}
```
`this.prop.children` 如果有多个可以是数字，读取的是 Child 组件的子标签

插槽用处：
- 为了复用，比如你实现了一个轮播组件，就可以使用插槽的方式实现，因为你不确定使用方想用标签来操作
- 一定程度上减少父子通信

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

## 5.2、非父子组件通信方式

- 状态提升（中间人模式）：React的状态提升，就是将多个组件需要共享的状态提升到它们最近的父组件上。在父组件上改变这个状态然后通过props分发给子组件；大致思路：先在父组件A上定义一个状态，然后在A1组件上调用A传递过来的属性回调函数，通过回调函数设置到状态值，再通过属性分发到A2组件上；

    详细代码参考：[state_enhance](https://gitee.com/chenlanqing/react-basic/blob/master/src/02-advanced/05_state_enhance.js)

- 发布订阅模式实现：

    详细代码参考：[pub_sub](https://gitee.com/chenlanqing/react-basic/blob/master/src/02-advanced/06_pub_sub.js)

- [Context](https://react.dev/reference/react/createContext)状态树参考：Context 提供了一个无需为每层组件手动添加 props，就能在组件树间进行数据传递的方法
  
    主要步骤：
    - 先定义全局对象：`const GlobalContext = React.createContext();`
    - 并使用`GlobalContext.Provider（生产者）`
        ```jsx
        render() {
            return (
                <GlobalContext.Provider value={{
                    info: this.state.info,
                    changeInfo: (value) => {
                        this.setState({
                            info: value
                        })
                    }
                }}>
                    <div>
                        {
                            this.state.filmList.map(item =>
                                <FilmItem key={item.filmId} {...item}></FilmItem>
                            )
                        }
                        <FilmDetail></FilmDetail>
                    </div>
                </GlobalContext.Provider>
            )
        }
        ```
    - 任意组件引入GlobalContext并调用context，使用GlobalContext.Consumer（消费者）
        ```jsx
        render() {
            return (
                <GlobalContext.Consumer>
                    {
                        (value) => {
                            return (
                                <div className="filmDetail">
                                    {value.info}
                                </div>
                            )
                        }
                    }
                </GlobalContext.Consumer>
            )
        }
        ```
    注意：`GlobalContext.Consumer`内必须是回调函数，通过context方法改变根组件状态

    详细代码参考：[context](https://gitee.com/chenlanqing/react-basic/blob/master/src/02-advanced/07_context.js)

# 6、生命周期

- [React Guidebook-生命周期](https://tsejx.github.io/react-guidebook/foundation/main-concepts/lifecycle)

指的是类的生命周期，生命周期指 React 组件从装载至卸载的全过程，这个过程内置多个函数供开发者在组件的不同阶段执行需要的逻辑

详细生命周期，参考：[main-concepts/lifecycle.md](https://github.com/chenlanqing/react-guidebook/blob/master/docs/foundation/main-concepts/lifecycle.md)

## 6.1、装载阶段

组件的渲染并且构造 DOM 元素插入到页面的过程称为组件的初始化，

装载阶段执行的函数会在组件实例被创建和插入 DOM 中时被触发，这个过程主要实现组件状态的初始化

### constructor

构造函数，语法：`constructor(props, context, updater)`
- `props`：继承 React.Component 的属性方法，它是不可变的 read-only
- `context`：全局上下文。
- `updater`：包含一些更新方法的对象
    - `this.setState` 最终调用的是 `this.updater.enqueueSetState`
    - `this.forceUpdate` 最终调用的是 `this.updater.enqueueForceUpdate` 方法，所以这些 API 更多是 React 调用使用，暴露出来以备开发者不时之需；

**触发时机**：在组件初始化的时候触发一次；

**使用建议**：
- 设置初始化状态：因为组件的生命周期中任何函数都可能要访问 State，那么整个周期中第一个被调用的构造函数便是初始化 state 最理想的地方；
- 绑定成员函数上下文引用：建议定义函数方法时直接使用箭头函数，就无须在构造函数中进行函数的 bind 操作。

在 ES6 中，在构造函数中通过 this.state 赋值完成状态初始化；通过给类属性（注意是类属性，而不是类实例对象的属性）defaultProps 赋值指定的 props 初始值。
```jsx
class Sample extends React.Component {
  constructor(props, context, updater) {
    super(props);
    this.state = {
      foo: 'InitailValue',
    };
  }
}
Sample.defaultProps = {
  bar: 'InitialValue',
};
```

### static getDerivedStateFromProps

**语法**：`static getDerivedStateFromProps(nextProps, prevState)`

**触发时机**：该函数会在组件化实例化后和重新渲染前调用（生成 VirtualDOM 之后，实际 DOM 挂载之前），意味着无论是父组件的更新、props 的变化或通过 setState 更新组件内部的 State，它都会被调用；

**返回值**：该生命周期函数必须有返回值，它需要返回一个对象来更新 State，或者返回 null 来表明新 props 不需要更新任何 state；

**新特性**：当组件实例化时，该方法替代了 componentWillMount，而当接收新的 props 时，该方法替代了 componentWillReceiveProps 和 componentWillUpdate

**注意事项：**
- 在组件装载和更新阶段都会触发。
- 如果父组件导致了组件的重新渲染，即使属性没有更新，这一方法也会被触发；
- 如果你只想处理 props 的前后变化，你需要将上一个 props 值存到 state 里作为镜像；
- 该生命周期函数是一个静态函数，所以函数体内无法访问指向当前组件实例的指针 this；
- 当需要更新 state 时，需要返回一个对象，否则，返回一个 null

**适用场景**：表单获取默认值

> 为什么该生命周期函数要设计成静态方法呢？

该生命周期函数被设计成静态方法的目的是为了**保持该方法的纯粹**。通过更具父组件输入的 `props` 按需更新 `state`，这种 `state` 叫做衍生 `state`，返回的对象就是要增量更新的 `state`，除此之外不应该在里面执行任何操作。

通过设计成静态方法，能够起到限制开发者无法访问 `this` 也就是实例的作用，这样就不能在里面调用实例方法或者 `setState` 以破坏该生命周期函数的功能。

这个生命周期函数也经历了一些波折，原本它是被设计成 `初始化`、`父组件更新` 和 `接收到 Props` 才会触发，现在只要渲染就会触发，也就是 `初始化` 和 `更新阶段` 都会触发。

**示例：**
```jsx
static getDerivedStateFromProps(nextProps, prevState) {
  if (nextProps.translateX !== prevState.translateX) {
    return {
      translateX: nextProps.translateX
    }
  }
  if (nextProps.data !== prevState.data){
      return {
          data: nextProps.data
      }
  }
  return null;
}
```

### componentWillMount

> 使用该函数会告警：aRct-dom.development.js:86 Warning: componentWillMount has been renamed, and is not recommended for use. See https://reactjs.org/link/unsafe-component-lifecycles for details.

* Move code with side effects to componentDidMount, and set initial state in the constructor.
* Rename componentWillMount to UNSAFE_componentWillMount to suppress this warning in non-strict mode. In React 18.x, only the UNSAFE_ name will work. To rename all deprecated lifecycles to their new names, you can run `npx react-codemod rename-unsafe-lifecycles` in your project source folder.

> 此生命周期函数将在 React v17 正式废弃。

**预装载函数**。

**触发时机**：在构造函数和装载组件（将 DOM 树渲染到浏览器中）之间触发。装载组件后将执行 `render` 渲染函数。因此在此生命周期函数里使用 `setState` 同步设置组件内部状态 `state` 将不会触发重新渲染。

**注意事项**：避免在该方法中引入任何的副作用（Effects）或订阅（Subscription）。对于这些使用场景，建议提前到构造函数中。

### render

渲染函数，保持 render() 纯粹，可以使服务器端渲染更加切实可行，也使组件更容易被理解

### componentDidMount

装载成功函数，组件完全挂载到网页上后触发。

适用场景：发送网络请求；任何依赖于 DOM 的初始化操作；添加事件监听；如果使用了 Redux 之类的数据管理工具，也能触发 action 处理数据变化逻辑

## 6.2、更新阶段

属性（Props）或状态（State）的改变会触发一次更新阶段，但是组件未必会重新渲染，这取决于 shouldComponentUpdate

- `UNSAFE_componentWillReceiveProps(nextProps)`：当父组件的渲染函数被调用，在渲染函数中被渲染的子组件就会经历更新阶段，不管父组件传给子组件的 props 有没有改变，都会触发该生命周期函数。当组件内部调用 setState 更新内部状态 state 时触发更新阶段不会触发该函数；适合用于父子组件之间的联动，适合父组件根据某个状态控制子组件的渲染或者销毁。通过对比 this.props 和 nextProps 来对本组件内的 state 进行变更，或执行某些方法来进行组件的重新渲染
- `static getDerivedStateFromProps`
- `shouldComponentUpdate` : 返回false 会阻止组件渲染，请勿在此函数中使用 setState 方法，会导致循环调用；如果性能是个瓶颈，尤其是有几十个甚至上百个组件的时候，使用 shouldComponentUpdate 可以优化渲染效率，提升应用的性能；
- `UNSAFE_componentWillUpdate`
- `render`
- `getSnapshotBeforeUpdate`：该生命周期函数会在组件即将挂载时触发，它的触发在 render 渲染函数之后。由此可见，render 函数并没有完成挂载操作，而是进行构建抽象 UI（也就是 Virtual DOM）的工作。该生命周期函数执行完毕后就会立即触发 componentDidUpdate 生命周期钩子
- `componentDidUpdate`

相比装载阶段的生命周期函数，更新阶段的生命周期函数使用的相对来说要少一些。常用的是 getDerivedStateFromProps、shouldComponentUpdate，前者经常用于根据新 props 的数据去设置组件的 State，而后者则是常用于优化，避免不必要的渲染

## 6.3、卸载阶段

- componentWillUnmount：在组件卸载和销毁之前触发。可以利用这个生命周期方法去执行任何清理任务。用于注销事件监听器；取消网络请求；取消定时器；解绑 DOM 事件。

## 6.4、捕捉错误

- static getDerivedStateFromError
- componentDidCatch


# 开源组件

- [移动端-平滑滚动组件](https://github.com/ustbhuangyi/better-scroll)

# 参考资料

- [React英文文档](https://reactjs.org/)
- [React中文文档](https://zh-hans.reactjs.org/)
- [React学习路线](https://github.com/adam-golab/react-developer-roadmap)

