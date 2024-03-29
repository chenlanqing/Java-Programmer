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

- idea
- mat
- DBeaver   数据库连接工具
- postman
- 百度网盘
- 滴答清单
- Alfred3
- Charles
- Cheatsheet
- ClashX（VPN）
- CleanMyMac
- Clipber  粘贴板
- iState Menus
- iTerm   终端工具 【https://segmentfault.com/a/1190000014992947】
- JProfiler
- MarkEditor  markdown文件编辑
- rdm  redis客户端
- SecureCRT  SSH连接客户端
- StarUML       uml画图工具
- Sublime
- SwitchHosts
- The Unarchiver   解压缩工具
- Typora    markdown编辑
- Visual Studio Code
- VMware Fusion  虚拟机工具
- WireShark   抓包工具
- Xmin      脑图工具
- atom
- worktile
- sizeup
- https://arctype.com/

## 5、homebrew

使用homebrew下载软件缓慢，可以使用国内镜像：
```
-- 替换brew.git:
cd "$(brew --repo)"
git remote set-url origin https://mirrors.cloud.tencent.com/homebrew/brew.git

-- 替换homebrew-core.git:
cd "$(brew --repo)/Library/Taps/homebrew/homebrew-core"
git remote set-url origin https://mirrors.cloud.tencent.com/homebrew/homebrew-core.git

-- 刷新源
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

sudo lsof -i tcp:port
