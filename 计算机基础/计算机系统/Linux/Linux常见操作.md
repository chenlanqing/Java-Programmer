# 一、文件操作命令

## 1、find 命令

```find / -name filename.txt``` 根据名称查找/目录下的filename.txt文件。

```find . -name "*.xml"``` 递归查找所有的xml文件

```find . -name "*" |xargs grep "hello"``` 递归查找所有文件内容中包含hello world的xml文件

- 查找目录：`find /（查找范围） -name '查找关键字' -type d`
- 查找文件：`find /（查找范围） -name 查找关键字 -print`

```find ./ -size 0 | xargs rm -f &``` 删除文件大小为零的文件

`find . -type f -print | wc -l` ： 统计当前目录下文件个数

## 2、grep 命令

表示全局正则表达式版本，它的使用权限是所有用户，它能使用正则表达式搜索文本，并把匹配的行打印出来

其工作方式：它在一个或多个文件中搜索字符串模板。如果模板包括空格，则必须被引用，模板后的所有字符串被看作文件名。搜索的结果被送到标准输出，不影响原文件内容。

grep可用于shell脚本，因为grep通过返回一个状态值来说明搜索的状态：
- 如果模板搜索成功，则返回0；
- 如果搜索不成功，则返回1；
- 如果搜索的文件不存在，则返回2；

**命令格式：** `grep [option] pattern file`

**命令参数：**
```
-a   --text   #不要忽略二进制的数据。   
-A<显示行数>   --after-context=<显示行数>   #除了显示符合范本样式的那一列之外，并显示该行之后的内容。   
-b   --byte-offset   #在显示符合样式的那一行之前，标示出该行第一个字符的编号。   
-B<显示行数>   --before-context=<显示行数>   #除了显示符合样式的那一行之外，并显示该行之前的内容。   
-c    --count   #计算符合样式的列数。   
-C<显示行数>    --context=<显示行数>或-<显示行数>   #除了显示符合样式的那一行之外，并显示该行之前后的内容。   
-d <动作>      --directories=<动作>   #当指定要查找的是目录而非文件时，必须使用这项参数，否则grep指令将回报信息并停止动作。   
-e<范本样式>  --regexp=<范本样式>   #指定字符串做为查找文件内容的样式。   
-E      --extended-regexp   #将样式为延伸的普通表示法来使用。   
-f<规则文件>  --file=<规则文件>   #指定规则文件，其内容含有一个或多个规则样式，让grep查找符合规则条件的文件内容，格式为每行一个规则样式。   
-F   --fixed-regexp   #将样式视为固定字符串的列表。   
-G   --basic-regexp   #将样式视为普通的表示法来使用。   
-h   --no-filename   #在显示符合样式的那一行之前，不标示该行所属的文件名称。   
-H   --with-filename   #在显示符合样式的那一行之前，表示该行所属的文件名称。   
-i   --ignore-case   #忽略字符大小写的差别。   
-l   --file-with-matches   #列出文件内容符合指定的样式的文件名称。   
-L   --files-without-match   #列出文件内容不符合指定的样式的文件名称。   
-n   --line-number   #在显示符合样式的那一行之前，标示出该行的列数编号。   
-q   --quiet或--silent   #不显示任何信息。   
-r   --recursive   #此参数的效果和指定“-d recurse”参数相同。   
-s   --no-messages   #不显示错误信息。   
-v   --revert-match   #显示不包含匹配文本的所有行。   
-V   --version   #显示版本信息。   
-w   --word-regexp   #只显示全字符合的列。   
-x    --line-regexp   #只显示全列符合的列。   
-y   #此参数的效果和指定“-i”参数相同。
```

## 3、cat 命令

cat命令的用途是连接文件或标准输入并打印。这个命令常用来显示文件内容，或者将几个文件连接起来显示，或者从标准输入读取内容并显示，它常与重定向符号配合使用

- 格式：`cat [选项] [文件]`
- cat主要有三大功能：
    - 一次显示整个文件：`cat filename`
    - 从键盘创建一个文件：`cat > filename` 只能创建新文件，不能编辑已有文件.
    - 将几个文件合并为一个文件：`cat file1 file2 > file`
- 参数
    ```
    -A, --show-all           等价于 -vET
    -b, --number-nonblank    对非空输出行编号
    -e                       等价于 -vE
    -E, --show-ends          在每行结束处显示 $
    -n, --number             对输出的所有行编号,由1开始对所有输出的行数编号
    -s, --squeeze-blank      有连续两行以上的空白行，就代换为一行的空白行 
    -t                       与 -vT 等价
    -T, --show-tabs          将跳格字符显示为 ^I
    -u                       (被忽略)
    -v, --show-nonprinting   使用 ^ 和 M- 引用，除了 LFD 和 TAB 之外
    ```

## 4、more与less命令

### 4.1、more

more命令，功能类似 cat，cat命令是整个文件的内容从上到下显示在屏幕上。more会以一页一页的显示方便使用者逐页阅读，而最基本的指令就是按空白键（space）就往下一页显示，按 b 键就会往回（back）一页显示，而且还有搜寻字串的功能 。more命令从前向后读取文件，因此在启动时就加载整个文件
 
- 命令格式：`more [-dlfpcsu ] [-num ] [+/ pattern] [+ linenum] [file ... ] `
- 命令参数：
    ```
    +n       从笫n行开始显示
    -n       定义屏幕大小为n行
    +/pattern 在每个档案显示前搜寻该字串（pattern），然后从该字串前两行之后开始显示  
    -c       从顶部清屏，然后显示
    -d       提示“Press space to continue，’q’ to quit（按空格键继续，按q键退出）”，禁用响铃功能
    -l        忽略Ctrl+l（换页）字符
    -p       通过清除窗口而不是滚屏来对文件进行换页，与-c选项相似
    -s       把连续的多个空行显示为一行
    -u       把文件内容中的下画线去掉
    ```
- 常用操作命令：
    ```
    Enter    向下n行，需要定义。默认为1行
    Ctrl+F   向下滚动一屏
    空格键  向下滚动一屏
    Ctrl+B  返回上一屏
    =       输出当前行的行号
    ：f     输出文件名和当前行的行号 
    V      调用vi编辑器
    !命令   调用Shell，并执行命令 
    q       退出more
    ```

### 4.2、less

less 工具也是对文件或其它输出进行分页显示的工具，less 的用法比起 more 更加的有弹性；
- 命令格式：`less [参数]  文件`
- 命令功能：less 与 more 类似，但使用 less 可以随意浏览文件，而 more 仅能向前移动，却不能向后移动，而且 less 在查看之前不会加载整个文件
- 命令参数：
    ```
    -b  <缓冲区大小> 设置缓冲区的大小
    -e  当文件显示结束后，自动离开
    -f  强迫打开特殊文件，例如外围设备代号、目录和二进制文件
    -g  只标志最后搜索的关键词
    -i  忽略搜索时的大小写
    -m  显示类似more命令的百分比
    -N  显示每行的行号
    -o <文件名> 将less 输出的内容在指定文件中保存起来
    -Q  不使用警告音
    -s  显示连续空行为一行
    -S  行过长时间将超出部分舍弃
    -x <数字> 将“tab”键显示为规定的数字空格
    /字符串：向下搜索“字符串”的功能
    ?字符串：向上搜索“字符串”的功能
    n： 重复前一个搜索（与 / 或 ? 有关）
    N： 反向重复前一个搜索（与 / 或 ? 有关）
    b   向后翻一页
    d   向后翻半页
    h   显示帮助界面
    Q   退出less 命令
    u   向前滚动半页
    y   向前滚动一行
    空格键  滚动一行
    回车键  滚动一页
    [pagedown]： 向下翻动一页
    [pageup]：   向上翻动一页
    ```

## 5、vi 命令

## 6、tail与head命令

### 6.1、tail命令

tail 命令从指定点开始将文件写到标准输出，用于显示指定文件末尾内容，不指定文件时，作为输入信息进行处理。常用查看日志文件

- 格式：`tail [必要参数] [选择参数] [文件]`
- 命令参数：
    - `-f`：循环读取
    - `-q`：不显示处理信息
    - `-v`：显示详细的处理信息
    - `-c<数目>`：显示的字节数
    - `-n<行数>`：显示行数
    - `--pid=PID`：与-f合用,表示在进程ID,PID死掉之后结束. 
    - `-q`, --quiet, --silent 从不输出给出文件名的首部 
    - `-s`, --sleep-interval=S 与-f合用,表示在每次反复的间隔休眠S秒

### 6.2、head命令

head 与 tail 就像它的名字一样的浅显易懂，它是用来显示开头或结尾某个数量的文字区块，head 用来显示档案的开头至标准输出中

- 命令格式：`head [参数]... [文件]...  `
- 命令功能：head 用来显示档案的开头至标准输出中，默认head命令打印其相应文件的开头10行
- 命令参数：
    ```
    -q 隐藏文件名
    -v 显示文件名
    -c<字节> 显示字节数
    -n<行数> 显示的行数
    ```
## 7、uname

显示系统信息
```
Usage: uname [OPTION]...
Print certain system information.  With no OPTION, same as -s.
  -a, --all                打印所有信息
  -s, --kernel-name        打印内核名称
  -n, --nodename           打印网络节点主机名
  -r, --kernel-release     打印内核版本
  -v, --kernel-version     打印内核版本
  -m, --machine            打印硬件名称
  -p, --processor          打印处理器类型
  -i, --hardware-platform  打印硬件平台
  -o, --operating-system   打印操作系统
      --help     帮助
      --version  显示版本
```

```
[root@localhost ~]# uname -a
Linux localhost.localdomain 3.10.0-327.el7.x86_64 #1 SMP Thu Nov 19 22:10:57 UTC 2015 x86_64 x86_64 x86_64 GNU/Linux

含义：
Linux：内核名称
localhost.localdomain：主机名
3.10.0-327.el7.x86_64：内核版本
#1 SMP Thu Nov 19 22:10:57 UTC 2015：内核版本
x86_64：硬件名称
x86_64：处理器类型
x86_64：硬件平台
GNU/Linux：操作系统
```

## 8、无法上传文件时该如何操作拷贝文件

有的服务器无法上传文件，也无法下载文件，可以使用 base64 命令绕过，比如需要上传一个 .class 文件到linux服务器上：
（1）在本地先转换.class文件为 base64，再保存为 result.txt
```sh
$ base64 < Test.class > result.txt
```
（2）到服务器上，新建并编辑result.txt，复制本地的内容，粘贴再保存； <br/>
（3）把服务器上的 result.txt 还原为 .class
```sh
$ base64 -d < result.txt > Test.class
```
（4）用 md5 命令计算哈希值，校验是否一致
```sh
$ md5sum result.txt 
5540be09ca04033f4e197807754f2ec1  result.txt
```

# 二、系统监控

## 1、top

top命令是Linux下常用的CPU性能分析工具，能够实时显示系统中各个进程的资源占用状况，常用于服务端性能分析

### 1.1、格式

top [参数]

### 1.2、命令参数
- -b 批处理
- -c 显示完整的治命令
- -I 忽略失效过程
- -s 保密模式
- -S 累积模式
- -i<时间> 设置间隔时间
- -u<用户名> 指定用户名
- -p<进程号> 指定进程
- -n<次数> 循环显示的次数

### 1.3、实例

![image](../../Java/问题排查/image/top.jpg)

统计信息区：前五行是当前系统情况整体的统计信息区。下面我们看每一行信息的具体意义

- 第一行，任务队列信息，同 uptime 命令的执行结果，具体参数说明情况如下：

    - 14:06:23 — 当前系统时间
    - up 70 days, 16:44 — 系统已经运行了70天16小时44分钟（在这期间系统没有重启过的吆！）
    - 2 users — 当前有2个用户登录系统
    - load average: 1.15, 1.42, 1.44 — load average后面的三个数分别是1分钟、5分钟、15分钟的负载情况。
    - load average数据是每隔5秒钟检查一次活跃的进程数，然后按特定算法计算出的数值。如果这个数除以逻辑CPU的数量，结果高于5的时候就表明系统在超负荷运转了

- 第二行，Tasks — 任务（进程），具体信息说明如下：

    系统现在共有206个进程，其中处于运行中的有1个，205个在休眠（sleep），stoped状态的有0个，zombie状态（僵尸）的有0个。

- 第三行，cpu状态信息，具体属性说明如下：

    - 5.9%us — 用户空间占用CPU的百分比。
    - 3.4% sy — 内核空间占用CPU的百分比。
    - 0.0% ni — 改变过优先级的进程占用CPU的百分比
    - 90.4% id — 空闲CPU百分比
    - 0.0% wa — IO等待占用CPU的百分比
    - 0.0% hi — 硬中断（Hardware IRQ）占用CPU的百分比
    - 0.2% si — 软中断（Software Interrupts）占用CPU的百分比

    *备注：在这里CPU的使用比率和windows概念不同，需要理解linux系统用户空间和内核空间的相关知识！*

- 第四行,内存状态，具体信息如下：

    - 32949016k total — 物理内存总量（32GB）
    - 14411180k used — 使用中的内存总量（14GB）
    - 18537836k free — 空闲内存总量（18GB）
    - 169884k buffers — 缓存的内存量 （169M）   

- 第五行，swap交换分区信息，具体信息说明如下：

    - 32764556k total — 交换区总量（32GB）
    - 0k used — 使用的交换区总量（0K）
    - 32764556k free — 空闲交换区总量（32GB）
    - 3612636k cached — 缓冲的交换区总量（3.6GB）

- 第六行，空行。

- 第七行以下：各进程（任务）的状态监控，项目列信息说明如下：
    - PID : 进程 id
    - USER : 进程所有者
    - PR : 进程优先级
    - NI : nice 值。负值表示高优先级，正值表示低优先级
    - VIRT : 进程使用的虚拟内存总量，单位 kb。VIRT=SWAP+RES
    - RES : 进程使用的、未被换出的物理内存大小，单位 kb。RES=CODE+DATA
    - SHR : 共享内存大小，单位 kb
    - S : 进程状态。D= 不可中断的睡眠状态、R= 运行、S= 睡眠、T= 跟踪 / 停止、Z= 僵尸进程
    - %CPU : 上次更新到现在的 CPU 时间占用百分比
    - %MEM : 进程使用的物理内存百分比
    - TIME+ : 进程使用的 CPU 时间总计，单位 1/100 秒
    - COMMAND : 进程名称

## 2、ps 命令

用于报告当前系统的进程状态。可以搭配kill指令随时中断、删除不必要的程序。ps命令是最基本同时也是非常强大的进程查看命令，使用该命令可以确定有哪些进程正在运行和运行的状态、进程是否结束、进程有没有僵死、哪些进程占用了过多的资源等等，总之大部分信息都是可以通过执行该命令得到的

**常用命令参数：**
```bash
ps axo pid,comm,pcpu # 查看进程的PID、名称以及CPU 占用率
ps aux | sort -rnk 4 # 按内存资源的使用量对进程进行排序
ps aux | sort -nk 3  # 按 CPU 资源的使用量对进程进行排序
ps -A # 显示所有进程信息
ps -u root # 显示指定用户信息
ps -efL # 查看线程数
ps -e -o "%C : %p :%z : %a"|sort -k5 -nr # 查看进程并按内存使用大小排列
ps -ef # 显示所有进程信息，连同命令行
ps -ef | grep ssh # ps 与grep 常用组合用法，查找特定进程
ps -C nginx # 通过名字或命令搜索进程
ps aux --sort=-pcpu,+pmem # CPU或者内存进行排序,-降序，+升序
ps -f --forest -C nginx # 用树的风格显示进程的层次关系
ps -o pid,uname,comm -C nginx # 显示一个父进程的子进程
ps -e -o pid,uname=USERNAME,pcpu=CPU_USAGE,pmem,comm # 重定义标签
ps -e -o pid,comm,etime # 显示进程运行的时间
ps -aux | grep named # 查看named进程详细信息
ps -o command -p 91730 | sed -n 2p # 通过进程id获取服务名称
```

**查看进程信息：**
- `ps –ef|grep tomcat` 查看所有有关tomcat的进程
- `ps -ef|grep --color java` 高亮要查询的关键字
- `ps aux|grep java` 查看java进程
- `lsof -i:8080` 查看端口属于哪个进程
- `ps -ef | grep nginx | grep -v grep` 判断nginx进程是否存在，使用`echo $?` 返回1 

将目前属于您自己这次登入的 PID 与相关信息列示出来
```bash
[root@bluefish ~]# ps -l
F S   UID   PID  PPID  C PRI  NI ADDR SZ WCHAN  TTY          TIME CMD
4 S     0 11315  7342  0  80   0 -   993 poll_s pts/0    00:00:00 bash
4 S     0 26278 26276  0  80   0 - 28992 do_wai pts/0    00:00:00 bash
0 R     0 27483 26278  0  80   0 - 38301 -      pts/0    00:00:00 ps
```
ps aux 查看进程时可能存在以下结果：
```bash
$ ps aux | grep /app
root      4009  0.0  0.0   4376  1008 pts/0    Ss+  05:51   0:00 /app
root      4287  0.6  0.4  37280 33660 pts/0    D+   05:54   0:00 /app
root      4288  0.6  0.4  37280 33668 pts/0    D+   05:54   0:00 /app
```
其中 s 表示这个进程是一个会话的领导进程，而 `+` 表示前台进程组。进程组和会话。它们用来管理一组相互关联的进程，意思其实很好理解：
- 进程组表示一组相互关联的进程，比如每个子进程都是父进程所在组的成员；
- 会话是指共享同一个控制终端的一个或多个进程组。


## 3、netstat 命令

- [how to use netstat command](https://www.redhat.com/sysadmin/netstat)

用来打印Linux中网络系统的状态信息，可让你得知整个Linux系统的网络情况，选项：
```
-a或--all：显示所有连线中的Socket；
-A<网络类型>或--<网络类型>：列出该网络类型连线中的相关地址；
-c或--continuous：持续列出网络状态；
-C或--cache：显示路由器配置的快取信息；
-e或--extend：显示网络其他相关信息；
-F或--fib：显示FIB；
-g或--groups：显示多重广播功能群组组员名单；
-h或--help：在线帮助；
-i或--interfaces：显示网络界面信息表单；
-l或--listening：显示监控中的服务器的Socket；
-M或--masquerade：显示伪装的网络连线；
-n或--numeric：直接使用ip地址，而不通过域名服务器；
-N或--netlink或--symbolic：显示网络硬件外围设备的符号连接名称；
-o或--timers：显示计时器；
-p或--programs：显示正在使用Socket的程序识别码和程序名称；
-r或--route：显示Routing Table；
-s或--statistice：显示网络工作信息统计表；
-t或--tcp：显示TCP传输协议的连线状况；
-u或--udp：显示UDP传输协议的连线状况；
-v或--verbose：显示指令执行过程；
-V或--version：显示版本信息；
-w或--raw：显示RAW传输协议的连线状况；
-x或--unix：此参数的效果和指定"-A unix"参数相同；
--ip或--inet：此参数的效果和指定"-A inet"参数相同。
```

**列出所有端口 (包括监听和未监听的)：**
```bash
netstat -a     #列出所有端口
netstat -at    #列出所有tcp端口
netstat -au    #列出所有udp端口       
```

**列出所有处于监听状态的 Sockets：**
```bash
netstat -l        #只显示监听端口
netstat -lt       #只列出所有监听 tcp 端口
netstat -lu       #只列出所有监听 udp 端口
netstat -lx       #只列出所有监听 UNIX 端口
```

显示每个协议的统计信息：
```bash
netstat -s   显示所有端口的统计信息
netstat -st   显示TCP端口的统计信息
netstat -su   显示UDP端口的统计信息
```

**查看端口8080的使用情况：**

`netstat -tln | grep 8080` 查看端口8080的使用情况
- Linux：`netstat -nltp | grep PID`
- Mac下查看进程占用端口：`lsof -nP -iTCP -sTCP:LISTEN | grep PID`

**列出监控中的TCP和UDP端口：**

`netstat -tlun`

**找出程序运行的端口：** `netstat -ap | grep ssh`

**通过端口找进程ID：** `netstat -anp|grep 3306 | grep LISTEN|awk '{printf $7}'|cut -d/ -f1`

**查看连接某服务端口最多的的IP地址：** 

`netstat -ntu | grep :80 | awk '{print $5}' | cut -d: -f1 | awk '{++ip[$1]} END {for(i in ip) print ip[i],"\t",i}' | sort -nr`

**TCP各种状态列表：**

`netstat -nt | grep -e 127.0.0.1 -e 0.0.0.0 -e ::: -v | awk '/^tcp/ {++state[$NF]} END {for(i in state) print i,"\t",state[i]}'`

## 4、查看空间

### 4.2、df 命令

显示磁盘的相关信息，用于显示磁盘分区上的可使用的磁盘空间。默认显示单位为KB。可以利用该命令来获取硬盘被占用了多少空间，目前还剩下多少空间等信息

```
-a或--all：包含全部的文件系统；
--block-size=<区块大小>：以指定的区块大小来显示区块数目；
-h或--human-readable：以可读性较高的方式来显示信息；
-H或--si：与-h参数相同，但在计算时是以1000 Bytes为换算单位而非1024 Bytes；
-i或--inodes：显示inode的信息；
-k或--kilobytes：指定区块大小为1024字节；
-l或--local：仅显示本地端的文件系统；
-m或--megabytes：指定区块大小为1048576字节；
--no-sync：在取得磁盘使用信息前，不要执行sync指令，此为预设值；
-P或--portability：使用POSIX的输出格式；
--sync：在取得磁盘使用信息前，先执行sync指令；
-t<文件系统类型>或--type=<文件系统类型>：仅显示指定文件系统类型的磁盘信息；
-T或--print-type：显示文件系统的类型；
-x<文件系统类型>或--exclude-type=<文件系统类型>：不要显示指定文件系统类型的磁盘信息；
--help：显示帮助；
--version：显示版本信息
```

查看系统磁盘设备，默认是KB为单位：
```
[root@bluefish ~]# df
Filesystem     1K-blocks    Used Available Use% Mounted on
/dev/vda1       51473888 4594408  44241732  10% /
devtmpfs          930600       0    930600   0% /dev
tmpfs             941376      24    941352   1% /dev/shm
tmpfs             941376     560    940816   1% /run
tmpfs             941376       0    941376   0% /sys/fs/cgroup
tmpfs             188276       0    188276   0% /run/user/0
```
使用-h选项以KB以上的单位来显示，可读性高：
```
[root@bluefish ~]# df -h
Filesystem      Size  Used Avail Use% Mounted on
/dev/vda1        50G  4.4G   43G  10% /
devtmpfs        909M     0  909M   0% /dev
tmpfs           920M   24K  920M   1% /dev/shm
tmpfs           920M  552K  919M   1% /run
tmpfs           920M     0  920M   0% /sys/fs/cgroup
tmpfs           184M     0  184M   0% /run/user/0
```

### 4.2、du 命令

显示每个文件和目录的磁盘使用空间

```
-a或-all 显示目录中个别文件的大小。
-b或-bytes 显示目录或文件大小时，以byte为单位。
-c或--total 除了显示个别目录或文件的大小外，同时也显示所有目录或文件的总和。
-k或--kilobytes 以KB(1024bytes)为单位输出。
-m或--megabytes 以MB为单位输出。
-s或--summarize 仅显示总计，只列出最后加总的值。
-h或--human-readable 以K，M，G为单位，提高信息的可读性。
-x或--one-file-xystem 以一开始处理时的文件系统为准，若遇上其它不同的文件系统目录则略过。
-L<符号链接>或--dereference<符号链接> 显示选项中所指定符号链接的源文件大小。
-S或--separate-dirs 显示个别目录的大小时，并不含其子目录的大小。
-X<文件>或--exclude-from=<文件> 在<文件>指定目录或文件。
--exclude=<目录或文件> 略过指定的目录或文件。
-D或--dereference-args 显示指定符号链接的源文件大小。
-H或--si 与-h参数相同，但是K，M，G是以1000为换算单位。
-l或--count-links 重复计算硬件链接的文件。
```

```
[root@bluefish ~]# du
31836   ./software
8       ./.cache/abrt
12      ./.cache
8       ./.pip
4       ./.config/abrt
8       ./.config
4       ./.pki/nssdb
8       ./.pki
4       ./.ssh
31916   .
```
只显示当前目录下面的子目录的目录大小和当前目录的总的大小，最下面的1288为当前目录的总大小

## 5、free 命令



## 6、[监控工具SAR](https://mp.weixin.qq.com/s/CyYhAJMET_8kYSkmJDcqWA)


# 三、常用命令

## 1、查看Linux发行版本

```bash
cat /etc/redhat-release
cat /etc/os-release
cat /etc/system-release
cat /etc/redhat-release
cat /etc/centos-release
cat /etc/issue
执行 lsb_release -a
```

## 2、级联创建目录

mkdir -p project/{a,b,c}/src，该命令最终创建的目录结构：
```
.
└── project
    ├── a
    │   └── src
    ├── b
    │   └── src
    └── c
        └── src
```

## 3、安装rz、sz命令

`yum install -y lrzsz`

## 4、远程拷贝文件与文件夹

- 拷贝文件：`scp shutdown-tomcat.sh root@192.168.56.104:/root/`
- 拷贝文件夹：`scp -rq /root/software/ root@192.168.56.104:/root/`
- 远程下载文件：`cp -r root@192.168.56.104:/root/ /root/software/`

## 5、永久关闭防火墙

针对CentOS7：
- 查看状态：`systemctl status firewalld.service`；
- 停止防火墙：`systemctl stop firewalld.service`
- 禁止防火墙：`systemctl disable firewalld.service`

## 6、查看某个进程的线程数

- 查看所有进程：`pstree -p <pid>`
- 查看所有的进程数量`pstree -p <pid> | wc -l`

## 7、切换用户

- 添加用户：`useradd esuser`
- 授权用户：`chown -R esuser:esuser /usr/local/elasticsearch-7.4.2`，授权某个目录
- 切换到新建的用户：`su esuser`
- 查看当前用户：`whoami`

## 8、过滤掉配置的注释

`more elasticsearch.yml | grep ^[^#]`

## 9、查看脚本过程

`sh -x str.sh`

## 10、修改hostname

`hostnamectl set-hostname <yourhostname>`

## 11、ssh免密登录

机器环境，有三台机器，配置了对应的hosts和hostname
- 192.168.89.141 hadoop001
- 192.168.89.142 hadoop002
- 192.168.89.143 hadoop003

### 11.1、本机免密登录

在hadoop001机器上免密登录 hadoop001，那么操作如下：
- 生成公钥：`ssh-keygen -t rsa`，执行这个命令以后，需要连续按 4 次回车键回到 linux 命令行才表示这个操作执行 结束，在按回车的时候不需要输入任何内容
- 向本机复制公钥：`cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys`
- 免密登录：`ssh hadoop100`

### 11.2、免密登录远程机器

比如上面hadoop001需要免密登录 hadoop002、hadoop003 两台机器，由于hadoop001上已经生成了公钥，分别执行如下命令：
```
ssh-copy-id -i kafka2
ssh-copy-id -i kafka3
```

## 12、nohup

nohup 全称：no hang up（不挂起），即当前交互命令行退出的时候，程序还在运行；
```bash
nohup Command [Arg...] [ &]
最后的" &"：表示后台运行，不霸占交互命令行
如果不将 nohup 命令的输出重定向，输出将附加到当前目录的 nohup.out 文件中。
如果当前目录的 nohup.out 文件不可写，输出重定向到 $HOME/nohup.out 文件中。
如果没有文件能创建或打开以用于追加，那么 Command 参数指定的命令不可调用。
如果标准错误是一个终端，那么把指定的命令写给标准错误的所有输出作为标准输出重定向到相同的文件描述符。

nohup命令提交作业时，指定输出文件：

nohup command > myout.file 2>&1 &

0 – stdin (standard input，标准输入) 
1 – stdout (standard output，标准输出)
2 – stderr (standard error，标准错误输出) 
2>&1解释：
将标准错误（2）重定向到标准输出（&1），
标准输出（&1）再被重定向输入到myout.file文件中。

如：./startup.sh 2>&1 | tee startup.log
表示将执行startup.sh脚本后到标准输出和标准错误输出内容写入到startup.log文件中。tee 默认是覆盖的方式写入文件中，加-a表示追加。

最终解释：表示命令执行后退出交互命令行也不挂起，通过最后一个 &，表示后台运行，不霸占交互命令行，同时将标准输出和标准错误输出合并到myout.file文件中。
```

## 13、历史命令

- 关闭history记录功能：`set +o history`；

- 打开history记录功能：`set -o history`；

- 清空记录
    - `history -c` 记录被清空，重新登录后恢复。
    - `rm -f $HOME/.bash_history` 删除记录文件，清空历史。
- 临时不记录：在执行的命令前添加空格。例如：` history`

## 14、删除文件报错解决方法

使用root用户的，提示删除文件失败
```
[root@xiaoxiao .ssh]# rm -rf authorized_keys 
rm: cannot remove ‘authorized_keys’: Operation not permitted
```
可以通过如下方式解决：
```
# 查看文件属性
[root@xiaoxiao .ssh]# lsattr authorized_keys 
----i--------e-- authorized_keys

# 取消被设置的属性
[root@xiaoxiao .ssh]# chattr -i authorized_keys 

# 删除文件成功
# rm -f authorized_keys
```

## 15、bash中获取pid

`pid=$(ps aux | grep elasticsearch | grep config | awk '{print $2}')`

## 16、查看TCP连接

在端口45678上的TCP连接总数：`lsof -nP -i4TCP:45678 | wc -l`

在端口45678上的TCP连接状态：`lsof -nP -i4TCP:45678`

## 17、注册系统服务

比如需要将gateway注册为一个服务，可以在目录：`/etc/systemd/system` 新建一个文件：`gateway.service`，内容如下：
```bash
[Unit]
Description=gateway
Requires=network.target remote-fs.target
#After=kafka.service zookeeper.service nginx.service emqttd.service mysqld.service redisd.service

[Service]
Type=simple
User=root
Environment=HOME=/home/gateway
#Environment=JAVA_HOME=/data/tools/java
WorkingDirectory=/home/gateway
ExecStart=/bin/sh -c 'java -jar /home/gateway/gateway-1.0-SNAPSHOT.jar --spring.profiles.active=dev >/home/gateway/nohup.out 2>&1 '
ExecStop=/usr/bin/kill -9 
Restart=on-failure
[Install]
WantedBy=multi-user.target
```
那么针对这个服务，可以使用：
```
systemctl start gateway
systemctl restart gateway
```

## 18、时区

- 设置UTC时区：`sudo timedatectl set-timezone UTC`
- 查看所有时区： `timedatectl list-timezones`

## 19、Ubuntu启用root登录

（1）使用Ubuntu用户账号登录；

（2）编辑`/etc/ssh/sshd_config`文件：`sudo vim /etc/ssh/sshd_config`，找到配置参数：`PermitRootLogin`，将该参数后面的值修改为`yes`即可，保存退出；
```config
#Port 22
#AddressFamily any
#ListenAddress 0.0.0.0
#ListenAddress ::

#HostKey /etc/ssh/ssh_host_rsa_key
#HostKey /etc/ssh/ssh_host_ecdsa_key
#HostKey /etc/ssh/ssh_host_ed25519_key

# Ciphers and keying
#RekeyLimit default none

# Logging
#SyslogFacility AUTH
#LogLevel INFO

# Authentication:

#LoginGraceTime 2m
#PermitRootLogin prohibit-password
PermitRootLogin yes
#StrictModes yes
#MaxAuthTries 6
#MaxSessions 10
```
（3）给root用户设置密码；`sudo passwd root`，输入两遍密码；

（4）重启ssh服务：`sudo systemctl restart ssh`

## 20、查看端口占用

- `netstat -anp |grep 端口号`
- `netstat -nultp`：列出已经使用的端口号；
- `lsof -i:端口号`

## 21、配置别名

```bash
alias base="cd /home/root/software"
alias webapps="cd /home/root/software/apache-tomcat-7.0.69/tomcat_instance1/webapps"
alias log="tail -f /home/root/software/apache-tomcat-7.0.69/tomcat_instance1/logs/catalina.out"
alias log2="tail -f /home/root/software/apache-tomcat-7.0.69/tomcat_instance2/logs/catalina.out"
alias cdlog="cd /home/root/software/apache-tomcat-7.0.69/tomcat_instance1/logs"
```

# 参考资料

- [Linux命令搜索](https://github.com/jaywcjlove/linux-command)
- [Linux调优指令](https://mp.weixin.qq.com/s/ZKVpfO6VaqwmNs5Yed7Ang)
- [Linux常用命令](https://mp.weixin.qq.com/s/gDTe5dF5VoZ7xhGP8aIpCA)
- [AWK操作技巧](https://mp.weixin.qq.com/s/aRy3QlMUpSNOKf2pyN6Uuw)
- [tldr-pages-简化man命令说明](https://github.com/tldr-pages/tldr)
- [Linux沙箱纯净版](https://github.com/chenlanqing/instantbox)
- [Linux基础](https://linuxtools-rst.readthedocs.io/zh-cn/latest/base/index.html)
* [Linux从入门到精通](https://github.com/ForceInjection/linux-from-beginner-to-master)
* [什么是Linux](https://www.linux.com/what-is-linux/)
