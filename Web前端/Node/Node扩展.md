# 1、npm

## 1.1、全局安装

查看全局的安装包：
```
npm ls -g
```

## 1.2、npm包管理

在node项目中有一个 package.json 依赖管理
```json
"dependencies": {
    "lodash": "^4.17.21",
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "react-router-dom": "^6.6.1"
}
```
上面版本前面的 `^` 走npm install的时候会自动升级

Semver规范：
- major：新增不可向后兼容的新功能；
- Minor：新增可向后兼容的新功能；
- Patch：修复bug

版本前的符号：（npm install操作时）
- 符号 `^`：主板号固定，其他版本号更新到最新版；
- 符号 `~`：主次版本号固定，更新到最新版；
- 无符号：固定版本号；

锁版本，即无符号，固定版本号；
- 稳定大于一切；
- 代码需要可控；
- 环境需要一致；

# 2、Node-dev

[(Node-dev)-automatically restarts](https://github.com/fgnass/node-dev)

使用该工具时，修改代码后会有对应的提示信息，取消该提示的方法：
- 找到 `/usr/local/lib/node_modules/node-dev/lib/notify.js`，直接修改源码，在if前增加：`notifyEnabled = false;`
- 在工程目录下新增文件：`.node-dev.json`，增加如下内容：`{"notify": false}`
- 或者直接使用命令行参数：`--no-notify`


# 3、Mac升级NodeJS版本

> 第一步，先查看本机node.js版本：

```sh
node -v
```

> 第二步，清除node.js的cache：

```sh
sudo npm cache clean -f
```

> 第三步，安装 n 工具，这个工具是专门用来管理node.js版本的，别怀疑这个工具的名字，是他是他就是他，他的名字就是 "n"

```sh
sudo npm install -g n
```

> 第四步，安装最新版本的node.js

```sh
sudo n stable
```

> 第五步，再次查看本机的node.js版本：

```sh
node -v
```

> 第六步，更新npm到最新版：

```sh
$ sudo npm install npm@latest -g
```

> 第七步，验证

```sh
1、node -v
2、npm -v
```
