# 一、Express框架

# 二、NestJS框架

- [NestJS-Introduction](https://docs.nestjs.com/)

# 三、Node工具

## 1、npm

- [npm仓库](https://www.npmjs.com/)
- [npm包版本语义规范](https://www.javascripttutorial.net/nodejs-tutorial/npm-semantic-versioning/)

Npm：Node Package Manager，它是Node JavaScript平台的一个软件包管理器；

一般来说，每个npm项目都有一个叫做`package.json`的文件，位于根目录下，`package.json`是一个纯文本文件，包含npm用来识别项目和处理依赖关系的重要信息，要创建`package.json`文件，进入项目的根目录并执行以下命令：`npm init`，执行该命令之后，会要求你输入如下内容：
```js
Use `npm install <pkg>` afterwards to install a package and
save it as a dependency in the package.json file.
Press ^C at any time to quit.
package name: (learning) // 包名称
version: (1.0.0) // 版本
description:  // 描述
entry point: (index.js) // 入口
test command: // 测试命令
git repository: // 仓库
keywords: // 关键词
author: // 坐着
license: (ISC) // 协议
About to write to D:\node-learning\package.json:
{
  "name": "learning",
  "version": "1.0.0",
  "description": "",
  "main": "index.js",
  "scripts": {
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "author": "",
  "license": "ISC"
}
```
如果都使用默认值，可以使用：`npm init --yes`

### 1.1、安装新包

```bash
npm install <package_name>
npm i <package_name> ## 上面的简写
```
安装完新包之后，会在 `package.json` 中列出来，比如：`npm install express`
```json
"dependencies": {
    "express": "^4.18.2"
}
```
`npm install`会下载并安装所有列在依赖项和devDependencies部分的软件包；

**安装指定版本：**
```bash
npm install <package_name>@version
```
比如你安装：`npm install express@4.x`，其会安装4.x的最高版本，比如：`4.18.2（当前express的最高版本号）`


### 1.2、开发依赖

有时，可能想安装一个只在开发环境中运行的软件包，可以在npm安装命令中使用`-save-dev`选项，语法如下：
```bash
npm install <package_name> --save-dev
```
比如：安装如下`npm install morgan --save-dev`，那么 `package.json`会生成如下依赖：
```json
{
  ...
  "devDependencies": {
    "morgan": "^1.10.0"
  }
}
```

### 1.3、全局安装

```bash
## 全局安装
npm install <package_name> --global
npm i <package_name> -g   ## 上述方式的简写

## 查看全局安装的包
npm ls -g
## 查看全局安装目录
npm config get prefix

# 卸载指定的全局包
npm uninstall -g <package-name>
# 示例：卸载全局安装的 create-react-app
npm uninstall -g create-react-app
```
Mac默认全局安装目录：`/usr/local/lib/node_modules`

### 1.4、npm版本

在node项目中有一个 package.json 依赖管理
```json
"dependencies": {
    "lodash": "^4.17.21",
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "react-router-dom": "^6.6.1"
}
```
上面版本前面的 `^` 走npm install的时候会自动升级；

npm包的版本要符合Semver规范：`major.minor.patch`
- major：新增不可向后兼容的新功能；
- Minor：新增可向后兼容的新功能；
- Patch：修复bug

版本前的符号：（npm install操作时）
- 符号 `^`：主版本号固定，其他版本号更新到最新版；
- 符号 `~`：主次版本号固定，更新到最新版；
- 版本范围：要指定一个稳定版本的范围，你可以使用其中一个比较运算符 `>, <, =, >=, <= `，比如：`1.0.0 - 1.5.0`
- 无符号：固定版本号；
- `<1.2.0 || > 1.2.3`：表示包含多套版本号
- `1.x`：表示匹配任何主版本为1的版本，如1.0.0，1.1.2，1.10.20，等等

锁版本，即无符号，固定版本号；
- 稳定大于一切；
- 代码需要可控；
- 环境需要一致；

一般开始的版本号是：1.0.0

### 1.5、查看本地安装的包

```sh
npm list
npm list --depth=0 ## 指定深度
npm list --prod ## npm list --production  只显示 dependencies 内的包
npm list --omit=dev ## 替代上面的 npm list --prod
npm list --dev ## 显示 dev-dependencies 内的包
npm list --include=dev ## 替代 npm list --dev
```

### 1.6、查看包信息

要查看一个包的信息，通常要到 http://npmjs.com  网站，找到包的名称，并显示其信息，另外在命令行，可以使用：`npm view`，语法：
```sh
npm view <package_name>[@<version>] [<field>?][<.subfield>]
```
`npm view`命令有以下别名`npm info`、`npm show`和`npm v`，不指定版本默认是最新的稳定版本

### 1.7、卸载

从当前node项目中移除包
```sh
npm uninstall <package_name>
```
别名：`npm un`, `npm remove`, `npm rm`, `npm unlink`；

`npm uninstall`命令从当前项目中完全删除该软件包及其依赖项。它还会更新package.json文件，排他属性：
- `--save` or `-S` : 移除 dependencies 下的包.
- `--save-dev` or `-D` : 移除 devDependencies 下的包.
- `--save-optional` or `-O`: 移除 optionalDependencies 下的包.
```sh
## 全局卸载
npm uninstall express --global
```
清理缓存：
```bash
npm cache clean --force
```

### 1.8、publish

如何发布软件包到 npm，需要在npm[上创建账号](https://www.npmjs.com/signup)，在创建完工程和代码之后，接下来：
- 使用`npm login`登录，会提示你输入账号、密码和邮箱；
- 执行`npm publish`，如果失败，可能是存在重复的package

在你将一个包发布到npm注册表之后，其他人可能已经在使用它了。因此，从npm注册表中取消发布软件包通常被认为是不良行为。
```sh
## 取消你发布的包
npm unpublish [<@scope>/]<package_name>[@<version>]
```
将你发布的包或包的版本过期：
```sh
npm deprecate <package_name>[@<version>] <deprecation_message>
```

## 2、Node-dev

[(Node-dev)-automatically restarts](https://github.com/fgnass/node-dev)

使用该工具时，修改代码后会有对应的提示信息，取消该提示的方法：
- 找到 `/usr/local/lib/node_modules/node-dev/lib/notify.js`，直接修改源码，在if前增加：`notifyEnabled = false;`
- 在工程目录下新增文件：`.node-dev.json`，增加如下内容：`{"notify": false}`
- 或者直接使用命令行参数：`--no-notify`


## 3、Mac升级NodeJS版本

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

## 3、nvm

- [nvm](https://github.com/nvm-sh/nvm)
- [Node版本管理](https://www.freecodecamp.org/news/node-version-manager-nvm-install-guide/)

nvm全英文也叫`node.js version management`，是一个nodejs的版本管理工具。nvm和n都是node.js版本管理工具，为了解决node.js各种版本存在不兼容现象可以通过它可以安装和切换不同版本的node.js

### 安装 Node

```bash
nvm install node          # 安装最新版
nvm install --lts         # 安装最新 LTS
nvm install lts/krypton   # 安装指定 LTS 代号
nvm install 22            # 安装指定大版本（自动取最新小版本）
nvm install 22.14.0       # 安装精确版本
```

### 版本切换

```bash
nvm use 22                # 切换到 v22（当前终端生效）
nvm use --lts             # 切换到最新 LTS
nvm use lts/iron          # 切换到指定 LTS 代号
```

### nvm切换版本安装原全局 Package

nvm 有内置命令专门处理这个场景：
```bash
# 安装新版本 node，并自动迁移当前版本的全局包
nvm install 24 --reinstall-packages-from=22
```
`--reinstall-packages-from` 会自动读取旧版本的全局包列表，在新版本安装完成后逐一重新安装，一步到位。

如果已经装好了新版本，补救方式：
```bash
# 切换到新版本
nvm use 24
# 从旧版本迁移全局包
nvm reinstall-packages 22
```

迁移完成后验证一下：
```bash
npm list -g --depth=0
```
> **注意**：`corepack` 和 `npm` 本身是 Node 自带的，迁移时会跳过，这是正常现象。

### nvm 升级

一键升级所有 npm 全局包
```bash
# 安装工具
npm install -g npm-check-updates
# 检查全局包的可用更新
ncu -g
# 一键升级所有全局包
ncu -g --install always
```
示例：
```bash
$ ncu -g
[====================] 20/20 100%

 @google/gemini-cli     0.38.2  →   0.39.1
 @openai/codex         0.122.0  →  0.125.0
 @qwen-code/qwen-code   0.14.5  →   0.15.3
 npm                   11.12.1  →  11.13.0
 npm-check-updates      21.0.3  →   22.0.1
 vite                    8.0.9  →   8.0.10
 zread_cli              0.2.10  →   0.2.12

ncu itself cannot upgrade global packages. Run the following to upgrade all global packages:

npm -g install @google/gemini-cli@0.39.1 @openai/codex@0.125.0 @qwen-code/qwen-code@0.15.3 npm-check-updates@22.0.1 npm@11.13.0 vite@8.0.10 zread_cli@0.2.12
```
