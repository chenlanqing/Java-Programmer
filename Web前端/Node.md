# Node-dev

[(Node-dev)-automatically restarts](https://github.com/fgnass/node-dev)

使用该工具时，修改代码后会有对应的提示信息，取消该提示的方法：
- 找到 `/usr/local/lib/node_modules/node-dev/lib/notify.js`，直接修改源码，在if前增加：`notifyEnabled = false;`
- 在工程目录下新增文件：`.node-dev.json`，增加如下内容：`{"notify": false}`
- 或者直接使用命令行参数：`--no-notify`


# Mac升级NodeJS版本

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
