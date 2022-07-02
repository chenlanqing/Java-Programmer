

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

阅读Vue3的源码最好是熟悉 TypeScript 和 ES6

# 参考资料

- [Vue3官方文档](https://v3.cn.vuejs.org/guide/introduction.html)