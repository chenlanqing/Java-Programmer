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

https://github.com/qianguyihao/Mac-list

### 4.1、文本编辑

- Sublime
- Visual Studio Code
- worktile

### 4.2、终端工具

- [iTerm 终端工具 ](https://zhuanlan.zhihu.com/p/550022490)
- [ohmyzsh](https://github.com/ohmyzsh/ohmyzsh)
- termius  SSH连接客户端
- [WindTerm](https://github.com/kingToolbox/WindTerm)
- https://github.com/alacritty/alacritty
- [The minimal, blazing-fast, and infinitely customizable prompt for any shell!](https://github.com/starship/starship)

### 4.3、开发工具

- idea
- pycharm
- clion
- mat
- DBeaver   数据库连接工具
- postman
- another redis desktop manager- JProfiler
- jclasslib bytecode viewer
- Navicate lite
- [chatwize：大模型聊天机器人](https://chatwise.app/)
- [Surge：Mac & iOS 高级网络工具箱](https://nssurge.com/)
- [Lepton：开源代码片段管理](https://hackjutsu.com/Lepton/)
- StarUML       uml画图工具
- WireShark   抓包工具
- Anaconda
- Docker

### 4.4、浏览器

- Arc：浏览器
- brave 浏览器

### 4.5、AI 编程

- cursor
- Claude code
- trae
- Cherry Studio
- Ollama
- ima Copilot
- Code buddy

### 4.6、其他软件

- 滴答清单
- [Dozer：菜单栏隐藏](https://github.com/Mortennn/Dozer)
- Alfred3
- [Raycast-快速启动](https://www.raycast.com/)，包含粘贴板的功能
- Cheatsheet
- Clash Verge（VPN）
- CleanMyMac
- [粘贴板-Maccy](https://maccy.app/)
- iState Menus
- Snipast
- SwitchHosts
- The Unarchiver   解压缩工具
- VMware Fusion  虚拟机工具
- Xmind     脑图工具
- sizeup
- Grammarly Desktop：基础语法检验
- iBar：刘海屏处理
- App Cleaner & Uninstaller：强力软件卸载
- Runcat：查看当前 CPU 等信息
- Flomo：记录灵感，针对任务过程 or 结果进行复盘
- FlowUs：复盘产出，读书笔记
- [motrix-下载软件](https://motrix.app/)
- [AirBattery-电量显示](https://github.com/lihaoyun6/AirBattery)
- [适用于 macOS 的软件更新框架](https://github.com/sparkle-project/Sparkle)

## 5、homebrew

- [homebrew-GUI](https://github.com/milanvarady/Applite)
- [homebrew完整使用指南](https://mp.weixin.qq.com/s/oJqF_vq7BlUvxBe2U2mcjA)

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

## 8、JDK

配置多个版本：
```bash
export JAVA_8_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_151.jdk/Contents/Home
export JAVA_11_HOME=/Library/Java/JavaVirtualMachines/jdk-11.0.8.jdk/Contents/Home
export JAVA_17_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
export JAVA_21_HOME=/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home
export JAVA_23_HOME=/Library/Java/JavaVirtualMachines/jdk-23.jdk/Contents/Home
export JAVA_HOME=$JAVA_17_HOME
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export PATH=$PATH:$JAVA_HOME/bin:$M2_HOME/bin

alias jdk8='export JAVA_HOME=$JAVA_8_HOME'
alias jdk11='export JAVA_HOME=$JAVA_11_HOME'
alias jdk17='export JAVA_HOME=$JAVA_17_HOME'
alias jdk21='export JAVA_HOME=$JAVA_21_HOME'
alias jdk23='export JAVA_HOME=$JAVA_23_HOME'
```
每次使用 jdk8、jdk11、jdk17、jdk21、jdk23，命令切换 jdk 版本时，都可以输入 java -version 来查看是否已经成功。

macOS下 JDK 默认安装在 `/Library/Java/JavaVirtualMachines`目录下，同时提供了一个小工具`/usr/libexec/java_home` 帮助我们快速的查看 JDK 相关的信息。

默认情况下 MacOS 会自动选择 `/Library/Java/JavaVirtualMachines`目录下版本号最高的 JDK 做为默认 JDK
```bash
$ /usr/libexec/java_home
/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
# 列出 当前电脑上安装的所有 jdk 版本
$ /usr/libexec/java_home -V
Matching Java Virtual Machines (8):
    23.0.2 (x86_64) "Oracle Corporation" - "Java SE 23.0.2" /Library/Java/JavaVirtualMachines/jdk-23.jdk/Contents/Home
    21.0.7 (x86_64) "Oracle Corporation" - "Java SE 21.0.7" /Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home
    17 (x86_64) "Oracle Corporation" - "Java SE 17" /Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home
    11.0.8 (x86_64) "Oracle Corporation" - "Java SE 11.0.8" /Library/Java/JavaVirtualMachines/jdk-11.0.8.jdk/Contents/Home
    11.0.5 (x86_64) "Oracle Corporation" - "Java SE 11.0.5" /Library/Java/JavaVirtualMachines/jdk-11.0.5.jdk/Contents/Home
    1.8.151.12 (x86_64) "Oracle Corporation" - "Java" /Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home
    1.8.0_151 (x86_64) "Oracle Corporation" - "Java SE 8" /Library/Java/JavaVirtualMachines/jdk1.8.0_151.jdk/Contents/Home
    1.7.0_80 (x86_64) "Oracle Corporation" - "Java SE 7" /Library/Java/JavaVirtualMachines/jdk1.7.0_80.jdk/Contents/Home
/Library/Java/JavaVirtualMachines/jdk-23.jdk/Contents/Home
```
可以通过java_home的exec选项来执行单次任务：`/usr/libexec/java_home -v version --exec command`

## 9、常用命令

### 9.1、命令输出到粘贴板

```bash
$ cat result.txt
...
Some stuff
...
$ cat result.txt | pbcopy
```

### 9.2、open 命令

打开目录：
```bash
$ open ~/Library/Preferences
$ open /etc
$ open ../..
# 同时打开多个目录：
$ open ~/Documents ~/Desktop ~/Downloads
$ open ~/D*
```

打开文件，会使用默认的程序（通常是Preview），同样你也能一次性打开多个文件
```bash
$ open demo.txt
```
还能指定使用什么程序来打开文件，使用`-a`参数：
```bash
# 使用 vscode 打开 .zshrc 文件
$ open -a "Visual Studio Code" .zshrc
```

### 9.3、查询系统运行时长

在 macOS 中有多种方法可以查看系统运行时长（uptime）：

####  uptime 命令

```bash
uptime
```
显示示例：`13:45  up 3 days,  2:30, 2 users, load averages: 1.25 1.40 1.50`
- "up 3 days, 2:30" 表示系统已运行3天2小时30分钟

#### system_profiler 命令
```bash
system_profiler SPSoftwareDataType | grep "Time"
```
显示系统启动时间

#### sysctl 命令
```bash
sysctl -n kern.boottime
```
显示内核启动时间的时间戳

#### last 命令

```bash
last reboot | head -1
```
查看最近一次重启的时间

**推荐使用 `uptime` 命令**，这是最直接和准确的方法。

## 10、命令别名

比如安装了 oh-my-zsh，查看 git 操作命令的别名：
```bash
$ cd ~/.oh-my-zsh/plugins/git
$ cat git.plugin.zsh
```
`cat ~/.oh-my-zsh/plugins/git/git.plugin.zsh`
