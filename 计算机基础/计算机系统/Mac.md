## 1、通过iPhone访问Max上的静态html

- 安装node
- 在对应的静态html目录下执行命令：npx http-server -o
- 默认访问地址：  http://127.0.0.1:8080 

## 2、Mac下建立同一个 TCP 连接

可以使用 NetCat 命令行工具：

参考资料：https://www.oschina.net/translate/linux-netcat-command


## 3、Mac下查看TCP状态

```bash
// Mac 下，查询 TCP 连接状态
$ netstat -nat |grep TIME_WAIT

// Mac 下，查询 TCP 连接状态，其中 -E 表示 grep 或的匹配逻辑
$ netstat -nat | grep -E "TIME_WAIT|Local Address"
Proto  Recv-Q Send-Q Local  Address  Foreign  Address  (state)
tcp4 0  0  127.0.0.1.1080  127.0.0.1.59061 TIME_WAIT

// 统计：各种连接的数量
$ netstat -n | awk '/^tcp/ {++S[$NF]} END {for(a in S) print a, S[a]}'
ESTABLISHED 1154
TIME_WAIT 1645
```

## 4、Mac下装机软件

https://xclient.info/

[第三方软件管理](https://aerolite.dev/applite)

### 4.1、文本编辑

- MarkEditor  markdown文件编辑
- Sublime
- Typora    markdown编辑
- Visual Studio Code
- atom
- worktile

### 4.2、软件开发

- idea
- mat
- DBeaver   数据库连接工具
- postman
- iTerm   终端工具 【https://segmentfault.com/a/1190000014992947】
- another redis desktop manager- JProfiler
- SecureCRT  SSH连接客户端
- jclasslib bytecode viewer
- electerm 终端工具
- navicate lite

### 4.3、其他软件

- 百度网盘
- 滴答清单
- Alfred3
- Charles
- Cheatsheet
- ClashX（VPN）
- CleanMyMac
- Clipber  粘贴板
- iState Menus
- StarUML       uml画图工具
- Snipast
- SwitchHosts
- The Unarchiver   解压缩工具
- VMware Fusion  虚拟机工具
- WireShark   抓包工具
- Xmind     脑图工具
- sizeup
- https://arctype.com/
- Arc：浏览器
- Grammarly Desktop：基础语法检验
- iBar：刘海屏处理
- App Cleaner & Uninstaller：强力软件卸载
- Runcat：查看当前 CPU 等信息
- Flomo：记录灵感，针对任务过程 or 结果进行复盘
- FlowUs：复盘产出，读书笔记
- uTools：效率平台

## 5、homebrew

- [homebrew-GUI](https://github.com/milanvarady/Applite)

使用homebrew下载软件缓慢，可以使用国内镜像：
```bash
## 替换brew.git:
cd "$(brew --repo)"
git remote set-url origin https://mirrors.cloud.tencent.com/homebrew/brew.git

## 替换homebrew-core.git:
cd "$(brew --repo)/Library/Taps/homebrew/homebrew-core"
git remote set-url origin https://mirrors.cloud.tencent.com/homebrew/homebrew-core.git

## 刷新源
brew update
```

## 6、关于权限

比如Mac安装了Mysql，但是其安装目录下data目录无法访问，
```sh
/usr/local/mysql#  cd data
cd: permission denied: data
## 可以执行如下命令
sudo chmod -R a+rwx  /usr/local/mysql/data/
```

## 7、端口占用

```bash
sudo lsof -i tcp:port
```
