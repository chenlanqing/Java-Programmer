

## 计算属性

https://v3.cn.vuejs.org/guide/computed.html#%E8%AE%A1%E7%AE%97%E5%B1%9E%E6%80%A7


## 监听

- 能够监听数据的变化；
```js
data() {
    return {
        uname: "张无忌"
    }
}
```
- 监听不到对象的属性变化，如果需要使用的时候，需要使用深度监听
```js
data() {
    return {
        user:{
            uname : "张无忌",
            age:20
        }
    }
},
watch: {
    user: {
        handler: function (newValue) {
            console.log(newValue);
        },
        deep: true // 表示是否深度监听，watch会一层层的向下遍历，给对象每个属性都加上监听器
    }
}
```
对上面进行优化，值监听uname:
```js
"user.name": {// 使用字符串只会监听对象的单独的属性
    handler: function (newValue) {
        console.log(newValue);
    },
    deep: true
}
```

## 条件渲染

v-if 和 v-show 区别：
- v-if：如果在初始渲染时条件为false，则什么也不做——直到条件第一次变为true时，才会开始渲染条件块，也就是说只有v-if内的条件为 true，才会渲染dom；
- v-show：不管初始条件是什么，元素总是会被渲染，并且只是简单地基于 CSS 进行切换，会通过`display: none;`来控制是否显示；
- 一般来说，v-if 有更高的切换开销，而 v-show 有更高的初始渲染开销。因此，如果需要非常频繁地切换，则使用 v-show 较好；如果在运行时条件很少改变，则使用 v-if 较好

## 列表渲染

v-for 加上 key 值是为了给 vue一个提示，以便其能跟踪每个节点的身份，从而重用和重新排序现有元素，key是唯一标识，快速找到节点

## 表单

v-model 在内部为不同的输入元素使用不同的 property 并抛出不同的事件：
- text 和 textarea 元素使用 value property 和 input 事件；
- checkbox 和 radio 使用 checked property 和 change 事件；
- select 字段将 value 作为 prop 并将 change 作为事件。

### 复选框

- 单个复选框：v-model的值为布尔值
- 多个复选框：v-model绑定的是数组


## 组件

### 通过prop向子组件传值

- 在父组件中引用了子组件 Content，定义了 message、aaa等属性
```html
<template>
    <div>
        <Content :message="msg" :aaa="bbb"></Content>
        <h1>{{ msg }}</h1>
    </div>
</template>
```
- 在子组件 Content.vue 中添加 props属性，
```js
// https://v3.cn.vuejs.org/guide/component-props.html#prop-%E7%B1%BB%E5%9E%8B
<script>
export default {
    data() {
        return {
            msg: "你是Vue"
        }
    }
    props: {
        message: {
            type: String,
            required: true
        },
        aaa: {
            type: Number
        }
    }
}
</script>
```

### 子组件向父组件传值

- 子组件通过调用内建的 `$emit` 方法并传入事件名称来触发一个事件：
```js
// 第一个参数事件的名称，第二个参数是发送的事件参数
this.$emit('injectSubData', this.msg);
```
- 在父组件中，通过v-on监听子组件中自定义的事件:`@injectSubData` 对应的为子组件中定义的事件，getChildData为父组件定义的事件
```js
import Content from "./components/Content.vue"
// Content为子组件
<template>
    <Content :message="msg" :aaa="bbb" @injectSubData="getChildData"></Content>
</template>
```

### 父组件访问子组件

- 父组件访问子组件通过 `$refs` 访问；
```html
<!-- ref：用来给元素或者子组件注册引用信息，$refs.contendId.msg -->
<Content :message="msg" :aaa="bbb" @injectSubData="getChildData" ref="contentId"></Content>
```
- 子组件访问父组件通过 `$parent` 访问，在开发中尽量少用，因为要做到子组件的复用性
- 子组件访问根组件通过 `$root` 访问

### 插槽

- 子组件使用 slot 占位：
```html
<template>
    <div>
        <h2>我是Context内容</h2>
        <div>
            <slot></slot>
        </div>
    </div>
</template>
```
- 父组件引用：
```html
<template>
    <div>
        <SlotContext>
            <!-- 填充按钮 -->
            <button>按钮</button>
        </SlotContext>
        <SlotContext>
            <!-- 填充输入框 -->
            <input type="text" />
        </SlotContext>
    </div>
</template>
```

> 父级模板里的所有内容都是在父级作用域中编译的；子模板里的所有内容都是在子作用域中编译的

### 具名插槽

有时我们需要多个插槽，对于这样的情况，`<slot>` 元素有一个特殊的 attribute：name。通过它可以为不同的插槽分配独立的 ID，也就能够以此来决定内容应该渲染到什么地方：
```html
<template>
    <div>
        <h2>具名插槽</h2>
        <slot name="button"></slot>
        <slot name="input"></slot>
        <slot name="h2"></slot>
    </div>
</template>
<script>
export default {
    data() {
        return {
            message: "子组件"
        }
    }
}
</script>
```
在向具名插槽提供内容的时候，我们可以在一个 `<template>` 元素上使用 `v-slot` 指令，并以 `v-slot` 的参数的形式提供其名称：
```html
<template>
    <div>
        <NamedSlot>
            <button>按钮</button>
        </NamedSlot>
        <NamedSlot>
            <input type="text" />
        </NamedSlot>
        <NamedSlot>
            <!-- 只会显示父级模板的里数据 -->
            <template v-slot:button><button>按钮{{ message }}</button></template>
            <template v-slot:input><input type="text" /></template>
            <template v-slot:h2>
                <h2>测试</h2>
            </template>
        </NamedSlot>
    </div>
</template>
<script>
import NamedSlot from './components/NamedSlot.vue'
export default {
    data() {
        return {
            message: "父组件"
        }
    },
    components: {
        NamedSlot
    }
}
</script>
```

### 作用域插槽

作用域插槽主要是父组件替换插槽的标签，但是插槽的数据由子组件来提供
- 在子组件中定义如下插槽和数据
```html
<template>
    <div>
        <h2>作用域插槽</h2>
        <slot :list="list" :msg="message" name="attrSlot"></slot>
    </div>
</template>

<script>
export default {
    data() {
        return {
            message: "子组件",
            list: [1, 2, 3, 4, 5, 6]
        }
    }
}
</script>
```
- 在父组件中如下使用：
```html
<template>
    <div>
        <NamedSlot>
            <!-- attrSlot 为插槽名字，slotProps：包含所有插槽 prop 的对象 -->
            <template v-slot:attrSlot="slotProps">
                <ul>
                    <li v-for="item in slotProps.list" :key="item">
                        {{ item }}
                    </li>
                </ul>
            </template>
        </NamedSlot>
    </div>
</template>
```

### provide和inject

默认情况下，provide/inject 绑定并不是响应式的


阅读Vue3的源码最好是熟悉 TypeScript 和 ES6

# 参考资料

- [Vue3官方文档](https://v3.cn.vuejs.org/guide/introduction.html)