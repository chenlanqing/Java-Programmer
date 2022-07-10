

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


阅读Vue3的源码最好是熟悉 TypeScript 和 ES6

# 参考资料

- [Vue3官方文档](https://v3.cn.vuejs.org/guide/introduction.html)