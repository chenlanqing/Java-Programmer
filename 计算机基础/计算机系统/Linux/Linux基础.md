# 一、Linux装机

## 1、vmware虚拟机

- 系统分区：对于CentOS来说，分区：挂载点：
	- `/boot`： 文件格式：ext4，大小：200M；
	- `/swap`： 文件格式：swap，大小：不超过2G，为内存2倍；
	- `/home`： 文件格式：ext4，大小：根据实际情况分配；
	- `/`	：  文件格式：ext4，大小：其余剩下空间
	
- 配置网络：
	- A：host-only： ifconfig eth0 ip地址(ip地址为window下VMvet1下的ip地址)；
	- B：桥接：

## 2、服务器注意事项

远程服务器不允许关机，只能重启；重启时应关闭相应的服务；

# 二、Linux命令

- [Master the command line, in one page](https://github.com/jlevy/the-art-of-command-line)

- 命令格式： 命令 [-选项] [参数]
- 简化选项： -a 或 -all

## 1、帮助命令

- 查看 Shell 内部命令的帮助信息：help
- 查看命令的简要说明：whatis
- 查看命令的详细说明：info
- 查看命令的位置：which
- 定位指令的二进制程序、源代码文件和 man 手册页等相关文件的路径：whereis
- 查看命令的帮助手册（包含说明、用法等信息）：man
- 只记得部分命令关键字：man -k

**help**：用于查看 Shell 内部命令的帮助信息。而对于外部命令的帮助信息只能使用 man 或者 info 命令查看

**whatis**：用于查询一个命令执行什么功能
```bash
# 查看 man 命令的简要说明
$ whatis man
# 查看以 loca 开拓的命令的简要说明
$ whatis -w "loca*"
```
**info**： Linux 下 info 格式的帮助指令
```bash
# 查看 man 命令的详细说明
$ info man
```
**which**：用于查找并显示给定命令的绝对路径，环境变量 PATH 中保存了查找命令时需要遍历的目录。which 指令会在环境变量`$PATH` 设置的目录里查找符合条件的文件。也就是说，使用 which 命令，就可以看到某个系统命令是否存在，以及执行的到底是哪一个位置的命令
```bash
$ which pwd # 查找命令的路径
```
**whereis**：用来定位指令的二进制程序、源代码文件和 man 手册页等相关文件的路径，只能用于程序名的搜索，而且只搜索二进制文件（参数-b）、man 说明文件（参数-m）和源代码文件（参数-s）。如果省略参数，则返回所有信息：
```bash
$ whereis git # 将相关的文件都查找出来
git: /usr/bin/git /usr/share/man/man1/git.1.gz
```
**[man](https://man7.org/linux/man-pages/man1/man.1.html)**： Linux 下的帮助指令，通过 man 指令可以查看 Linux 中的指令帮助、配置文件帮助和编程帮助等信息
```bash
$ man date # 查看 date 命令的帮助手册
$ man 3 printf # 查看 printf 命令的帮助手册中的第 3 类
$ man -k keyword # 根据命令中部分关键字来查询命令
```

## 2、文件以及目录处理

文件管理不外乎文件或目录的创建、删除、查询、移动，有mkdir/rm/mv

### 2.1、目录命令

**ls、ll**：来显示一个文件的属性以及文件所属的用户和组
```
-a 显示所有档案及目录 (ls内定将档案名或目录名称开头为"."的视为隐藏档，不会列出) 
-l 除档案名称外，亦将档案型态、权限、拥有者、档案大小等资讯详细列出 
-r 将档案以相反次序显示(原定依英文字母次序) 
-t 将档案依建立时间之先后次序列出 
-A 同 -a ，但不列出 "." (目前目录) 及 ".." (父目录) 
-F 在列出的档案名称后加一符号；例如可执行档则加 "*"， 目录则加 "/" 
-R 若目录下有档案，则以下之档案亦皆依序列出
-s：在每个文件旁边显示该文件的大小，单位为千字节
-d：像一般文件那样处理目录，而不是列出它们的内容
-i：显示文件索引号
-h： 以容易理解的格式列出文件大小
```
示例：
```
drwxrwxr-x.  9 hadoop hadoop     4096 Apr 10 15：27 apache-nutch-2.1
-rw-r--r--.  1 hadoop hadoop  2699959 Oct 22 17：43 apache-nutch-2.1-src.tar.gz
drwxrwxr-x.  2 hadoop hadoop     4096 Apr 10 16：50 demo
```
说明：
```
0 123 456 789
d rwx rwx r-x
- rw- rw- r--
第 0 位确定文件类型
第 1、4、7 位表示读权限，如果用"r"字符表示，则有读权限，如果用"-"字符表示，则没有读权限。
第 2、5、8 位表示写权限，如果用"w"字符表示，则有写权限，如果用"-"字符表示没有写权限。
第 3、6、9 位表示可执行权限，如果用"x"字符表示，则有执行权限，如果用"-"字符表示，则没有执行权限
```
- 前十个字符表示该文件的类型及其权限。其中第一个字符表示该文件的类型：
	- 短横(-)表示是普通文件，
	- 字符 d 表示是目录。
	- 接下来的九个字符表示该文件的权限。短横(-)表示没有某一权限，这九个字符分成三组，每组又分为三种权限：
		- 第一组代表了该文件的所有者对它的权限;
		- 第二组是该文件所有者组中其他成员的权限；
		- 第三组表示了其他人的权限；
- 接下来的数字表示到该文件的链接。某个文件的唯一标识符不是它的名字而是一个数字(称为inode 号)。而且磁盘上的一个文件可以同时拥有好几个名字。对于目录，该链接数具有特殊的含义，(第二列)再接下来的是该文件所有者的名字(第三列：zhang)和所有组的名字(第四列：users)。最后是该文件的大小(以字节为单位)、最后修改日期和该文件或目录的名字。
- 文件的权限。
	- 首先，我们必须去掉代表文件类型的第一个字符。这样，对于文件某一_文件，我们就得到了：rw-r-----。下面对其逐项解释。
	- 前三个字符(rw-)代表所有者(在该例中是 zhang)的权限。因此，zhang 有权阅读该文件(r)，修改其内容(w)，但却不能执行它(-)。
	- 接下来的三个字符(r--)代表了 users 组中除了 zhang 的其他用户的权限。他们能够阅读该文件(r)，但是既不能写也不能执行(--)。
	- 最后三个字符(---)代表除zhang 及 users 组成员外的其他用户对该文件的权限。那些用户对这个文件没有任何权限。
- 对某一目录这个目录，其权限为 rwxr-xr--，因此：
	目录所有者li_si可以列出其中的文件(r)，在其中添加或删除文件(w)，以及对其遍历(x)。users 组中除li_si的其他成员能够列出其中的文件(r)，但是不能够在其中添加或删除文件(-)，但却能对其遍历(x)。剩下的用户只能列出该目录包含的文件(r)。由于他们没有 wx 权限，他们就不能够写入文件或进入该目录。
- 表示文件类型有”d”， “-“ ，其实除了这两种外还有”l”， “b”， “c”，”s”等。
	```
	d 表示该文件为目录；
	- 表示该文件为普通文件；
	l 表示该文件为连接文件（linux file），上边提到的软连接即为该类型；
	b 表示该文件为块设备文件，比如磁盘分区
	c 表示该文件为串行端口设备，例如键盘、鼠标。
	s 表示该文件为套接字文件（socket），用于进程间通信。
	```
- linux的文件系统格式为Ext2，或者Ext3
	- Ext2文件系统是非日志文件系统
	- Ext3文件系统是直接从Ext2文件系统发展而来，Ext3文件系统带有日志功能，可以跟踪记录文件系统的变化，并将变化内容写入日志，写操作首先是对日志记录文件进行操作，若整个写操作由于某种原因 (如系统掉电) 而中断，系统重启时，会根据日志记录来恢复中断前的写操作，而且这个过程费时极短
	- Linux文件系统在windows中是不能识别的，但是在linux系统中你可以挂载的windows的文件系统，linux目前支持MS-DOS，VFAT，FAT，BSD等格式

**目录的增删改：**
- `mkdir`：创建目录，可以同时创建多个目录，选项：			
	- `-m`， --mode=模式，设定权限<模式> (类似 chmod)，而不是 rwxrwxrwx 减 umask
	- `-p`， --parents，加上此选项后，系统将自动建立不存在的目录，即一次可以建立多个目录； 
	- `-v`， --verbose  每次创建新目录都显示信息

	例子：一个命令创建项目的目录结构：`mkdir -vp scf/{lib/，bin/，doc/{info，product}，logs/{info，product}，service/deploy/{info，product}}`
- `rmdir`：删除空目录
- `rm -rf [删除文件/目录]`
- `pwd -P` 显示文件实际路径，而非使用链接的路径
- `tree`：树状显示命令
	
### 2.2、文件处理命令

- `touch`：创建的文件不具有可执行权限
- `cat/tac`：-A 显示所有
- `more` ：显示文件内容，每次显示一屏
	```
	more [文件名]
		f：翻页
		回车：换行
		q或Q退出
	```
- `less` ：可向上翻页，显示文件内容，每次显示一屏
- `head -n [行号]` ：显示指定文件的开头若干行
- `tail`：显示指定文件的末尾若干行，常用于实时打印日志文件内容 
- `stat`：显示文件信息
- `file`：探测文件类型
- `grep`：用正则表达式搜索文本，并把匹配的行打印出来

### 2.3、链接命令

ln -s [源文件] [目标文件] ：生成软链接，类似windows快捷方式

ln [源文件] [目标文件] ：同步更新；生成硬链接，不能跨分区，不能针对不能使用，通过i节点识别[硬链接节点与原文件节点一致]
		
### 2.4、文件权限管理

**[chmod](https://www.geeksforgeeks.org/chmod-command-linux/)**：设置文件或目录的权限，相关写法：
- `chmod [{ugoa}{+-=}{rwx}][文件或/目录] -R`
- `chmod [mode=421][文件/目录] -R`
其中：
- `ugoa`：u => 文件所属用户，g => 用户所属组，o => 其他用户，a => all 
- `-R` ： 递归修改文件权限
- 数字表示权限：
	- `r => 4`，对文件可以查看文件内容，可以列出目录内容
	- `w => 2`，对文件可以修改文件内容，可以在目录中创建、删除文件
	- `x => 1`，对文件可以执行文件，可以进入目录
> 注意：理解写权限对文件和目录的操作；

**其他权限管理命令：**
- [`chown [用户名][文件/目录]`](https://www.geeksforgeeks.org/chown-command-in-linux-with-examples/)：改变文件/目录所有者，只有管理员root可以操作
- [`chgrp [用户组][文件/目录]`](https://www.geeksforgeeks.org/chgrp-command-in-linux-with-examples/)：改变文件/目录的所属组
- [`umask [-S]`](https://www.geeksforgeeks.org/umask-command-in-linux-with-examples/)：查看新创建文件/目录的预设权限；修改预设权限：umask [xxx]，三个数字；查看umask值只要输入umask然后回车。 umask预设是0022，其代表什么含义？先看一下下面的规则
	- 若用户建立为普通文件，则预设“没有可执行权限”，只有rw两个权限。最大为666(-rw-rw-rw-)
	- 若用户建立为目录，则预设所有权限均开放，即777(drwxrwxrwx)：umask数值代表的含义为，上边两条规则中的默认值（文件为666，目录为777）需要减掉的权限。
		- 所以目录的权限为 `(rwxrwxrwx) – (----w--w-) = (rwxr-xr-x) => 777-022=755`
		- 普通文件的权限为 `(rw-rw-rw-) – (----w--w-) = (rw-r--r--) => 666-022=644`
		- umask的值是可以自定义的，比如设定umask 为 002，你再创建目录或者文件时，
		- 默认权限分别为`(rwxrwxrwx) – (-------w-) = (rwxrwxr-x)`和`(rw-rw-rw-) – (-------w-) = (rw-rw-r--)`。
	- umask 可以在`/etc/bashrc`里面更改，预设情况下，root的umask为`022`，而一般使用者则为`002`，因为可写的权限非常重要，因此预设会去掉写权限。
	
	权限 |	八进制 | 二进制 |	描述
	----|---------|-------|-----
	—	|0	|000	|没有权限
	–x	|1	|001	|只有执行权限
	-w-	|2	|010	|只有写权限
	-wx	|3	|011	|写和执行权限
	r–	|4	|100	|只有读权限
	r-x	|5	|101	|读和执行权限
	rw-	|6	|110	|读和写权限
	rwx	|7	|111	|所有权限

### 2.5、文件搜索命令

**find 命令**：`find [搜索范围] [匹配条件]`，避免在根目录下全局查找文件，避免在服务器高峰期使用 find 命令
- 根据文件名查找文件：
	```
	find /ect -name init  ==> 查找文件 init，完全匹配
	find /etc -name *init* ==> 查找包含 init 的文件
	find /etc -name init??? ==> 查找以init开头，且后面包含三个字符的文件
	find /etc -iname init ==> 不区分大小写
	```
- 根据文件大小查找： +n 大于， -n 小于， n 等于
	```
	find / -size +2048000 ==> 在根目录下查找大于100M的文件，文件大小使用数据块，1 个数据块 = 512 字节 = 0.5K
	```
- 根据所有者查找文件：
	- `find /home -user root` ==> 在根目录下查找所有者为 root 的文件
	- `find /home -group root` ==> 根据所有组

- 根据时间属性查找文件：`find /etc -cmin -5`，在 etc 下查找5分钟内被修改过的属性的文件和目录
	- `-cmin` ：文件属性 change
	- `-amin` ：访问时间 access ，如权限等
	- `-mmin` ：文件内容 modify，修改的是文件内容
- 组合查询： `-a` ==> 连个条件同时满足， `-o` ==> 两个条件满足任意一个即可
	- `find /etc -size +163840 -a -size -204800` ==> 在etc下查找打下在80M~100M之间的文件
- 其他条件：`find /etc -name  init -exec ls -l {}` \
	- `-exec/-ok` 命令 {}\； 对搜索结果进行操作， -exec 直接执行命令， -ok 询问是否执行命令<br/>
	- `-type`：根据文件类型查找  f：文件， d：目录， l：软链接文件<br/>
	- `-inum`：根据 i 节点查找，每个文件都存在 i 节点，在一个文件带有特殊字符情况下，可以使用该命令<br/>

**其他搜索命令**

- `locate`：在文件资料库中查找文件；locate 文件名，在文件资料库中查找，/tmp 目录下的文件不再资料库中；`updatedb` 更新资料库
	- `locate -i 文件名`：不区分大小写
- `which`：搜索命令所在的目录及别名信息
	```
	[root@localhost test]# which ls
	alias ls='ls --color=auto'
	/usr/bin/ls
	```
- `whereis [命令]`：搜索命令所在目录及帮助文档路径
	```
	[root@localhost test]# whereis ls
	ls： /usr/bin/ls /usr/share/man/man1/ls.1.gz /usr/share/man/man1p/ls.1p.gz
	```
- `grep -iv [指定字符串] [文件]`：在文件中搜寻字符串匹配的行并输出：-i 不区分大小写；-v  排除指定字符串	

### 2.6、通用命令

- `cp`：复制文件或目录
- `scp`：复制文件或目录到远程服务器
- `mv`：移动文件或目录
- `rm`：删除文件或目录

### 2.7、打包与压缩

- `gzip [文件]`：压缩文件，文件格式 .gz，只能压缩目录且不保留源文件；gunzip [压缩文件]：解压缩.gz文件

- `tar -zcvf` 压缩后的文件名 目录： 打包目录， `.tar.gz`
	```bash
	-c 打包
	-x 解包
	-v 显示详细信息
	-f 指定的文件名/解压缩文件名
	-z 打包同时压缩
	-j 打包程.tar.bz2文件
	示例：
	tar -cvf log.tar log2012.log            # 仅打包，不压缩
	tar -zcvf log.tar.gz log2012.log        # 打包后，以 gzip 压缩
	tar -jcvf log.tar.bz2 log2012.log       # 打包后，以 bzip2 压缩

	tar -ztvf log.tar.gz                    # 查阅上述 tar 包内有哪些文件
	tar -zxvf log.tar.gz                    # 将 tar 包解压缩
	tar -zxvf log30.tar.gz log2013.log      # 只将 tar 内的部分文件解压出来
	```

- `zip [-r] 压缩后的文件名 [文件或目录]`：压缩命令，windows和linux都支持的压缩格式；-r ： 压缩目录
- `unzip 压缩文件`：解压缩.zip文件
- `bzip2 -k [文件]`：　只能压缩文件， .bz2
	```
	-k ：产生压缩文件后保留源文件
	tar -cjf node-v4.2.1.tar.bz2 node-v4.2.1 ==> 压缩程 .tar.bz2 
	bunzip2 -k [文件] 解压缩文件
	-k ： 保留压缩文件
	tar -xjf node-v4.2.1.tar.bz2
	```

## 3、文本处理

### 3.1、vim 常用操作

- 插入命令
	```
	a 		在光标所在字符后插入
	A 		在光标所在行尾插入
	i 		在光标所在字符前插入
	I 		在光标所在行行首插入
	o 		在光标下插入新行
	O 		在光标上插入新行
	```
- 定位命令
	```
	：set nu 	设置显示行号
	：set nonu	取消行号
	gg			到第一行
	G 			到最后一行
	nG			到第 n 行
	：n 			到第 n 行
	$			移至行尾
	0			移至行首
	```
- 删除命令
	```
	x 		删除光标所在处字符
	nx 		删除光标所在处后 n 个字符
	dd 		删除光标所在行，ndd 删除 n行；
	dG		删除光标所在行到文件末尾的内容
	D 		删除光标所在处到行尾的内容；
	：n1，n2d 删除n1行到n2行的内容.即指定范围的内容
	```
- 复制和剪切命令
	```
	yy		复制当前行；
	nyy		复制当前行以下的 n 行
	dd 		剪切当前行
	ndd		剪切当前行以下n行
	p 		粘贴在当前光标所在行的下面
	P      	粘贴到当前光标所在行的上面
	```
- 替换和取消命令
	```
	r  		替换光标所在出字符
	R 		从光标所在处开始替换字符，按ESC结束
	u 		取消上一步操作
	```
- 搜索和替换命令
	```
	/string			搜索指定的字符串
	：set ic 		搜索时忽略大小写， ：set noic 不忽略大小写
	n 				搜索时指定字符串的下一个出现的位置
	：%s/要替换的字符串/替换新的字符串/g 	全文替换指定的字符串， /g 不询问直接替换， /c 询问是否替换
	：n1，n2s/要替换的字符串/替换新的字符串/g 	在n1行到n2行之间替换指定的字符串
	```
- 保存和退出命令
	```
	：w 			保存修改
	：w filename	另存为指定文件
	：wq 		保存修改并退出
	ZZ 			快捷键，保存修改并退出
	：q!			不保存修改直接退出
	：wq!		保存修改并退出(文件所有着及root可使用)
	```

### 3.2、文本编辑器技巧

- 导入命令：
	```
	：r filename 	导入指定文件到当前编辑器编辑的文件
	!命令 			可以查看命令
	：r !命令 		导入命令的执行结果到当前文件
	```
- 定义快捷键：  ：map 快捷键 触发命令
	```
	# 注意： ^p 是由ctrl + v + p 来实现输入的
	：map ^P I#<ESC>  表示按 ctrl + p 快捷键，可以在每行行首加入注释，并保持在命令模式
	：map ^b 0x		 表示定义的快捷键 ctrl + b 删除行首的字符
	```
- 连续行注释：
	```
	# ^ 表示行首
	：n1，n2s/^/#/g 		 将n1行与n2行之间的行首替换为 "#"字符串
	：n1，n2s/^#//g 		 将n1行与n2行之间的行首 "#"替换掉
	：n1，n2s/^/\/\//g 	 将n1行与n2行之间的行首替换为 //
	```
- 替换：ab myemail chenlanq@qq.com：在文本编辑时输入 myemail， 回车或空格会自动替换为邮箱
- 如果要使上述定义的=命令永久有效，则需要在用户的根目录下创建文件：.vimrc 里面可以存放编辑模式下的命令

## 4、磁盘管理

- df ： 查看磁盘空间 - 使用 df
- du ： 查看文件或目录的磁盘空间 - 使用 du

## 5、进程管理

- ps
- top
- kill

## 6、性能监控

- sar:
- top
- iotop
- free

## 7、网络工具

- curl、wget：下载文件或者访问远程地址
- telnet ： telnet 方式登录远程主机，对远程主机进行管理
- ip ： 查看或操纵 Linux 主机的路由、网络设备、策略路由和隧道
- hostname：查看和设置系统的主机名
- firewalld：查看、设置防火墙（Centos7）
- write 用户名：给在线用户发信息，以 ctrl + D 保存结束
- wall 信息：发广播信息，给所有用户发送信息；		
- ping [-c] ip地址：测试网络连通性；-c：指定发送次数
- ifconfig 网卡名称 ip地址：查看和设置网卡信息		
- mail 用户名：发送的电子邮件，mail：查看邮件
- last：列出目前与过去登录系统的用户信息；lastlog -u [用户的uid]：查看所有或某个特定用户上次登录的时间	
- traceroute：显示数据包到主机间的路径		
- route：查看和设置 Linux 内核中的网络路由表
- netstat [选项]：显示网络相关信息；
	```		
	-t ： TCP协议
	-u ： UDP协议
	-l ： 监听
	-r ： 路由
	-n ： 显示IP地址和端口号 	
	netstat -tlun ： 查看本机监听的端口
	netstat -an ： 查看本机所有的网络连接
	netstat -rn ： 查看本机路由表	
	```
- setup：配置网络，永久生效[不是所有的Linux都有]		

## 8、用户管理命令

- `groupadd`：创建用户组
- `groupdel`：删除用户组
- `groupmod`：修改用户组信息
- `useradd`：创建用户
- `userdel`：删除用户
- `usermod`：修改用户信息
- `passwd`：设置用户认证信息
- `su`：切换用户
- `sudo`：当前用户想执行没有权限执行的命令时，使用其他用户身份去执行

## 9、系统管理

- 查看 Linux 系统发行版本
	- 使用 `lsb_release`（此命令适用于所有的 Linux 发行版本）
	- 使用 `cat /etc/redhat-release`（此方法只适合 Redhat 系的 Linux）
- 查看 CPU 信息：`cat /proc/cpuinfo`
- 重新启动 Linux 操作系统：`reboot`
- 退出 shell，并返回给定值：`exit`
- 关闭系统：`shutdown`
- 查看或设置系统时间与日期：`date`
- 挂载文件系统：`mount`
- 取消挂载文件系统：`umount`
- 查看系统当前进程状态：`ps`
- 删除当前正在运行的进程：`kill`
- 启动、停止、重启、关闭、显示系统服务（Centos7），使用 `systemctl`
- 启动、停止、重启、关闭、显示系统服务（Centos7 以前），使用 `service`
- 管理需要周期性执行的任务，使用 `crontab`

### 9.1、查询硬件信息

- 查看CPU使用情况 [sar](https://www.geeksforgeeks.org/sar-command-linux-monitor-system-performance/): `$ sar -u 5 10`
- 查询CPU信息:`$ cat /proc/cpuinfo`
- 查看CPU的核的个数: `$ cat /proc/cpuinfo | grep processor | wc -l`
- 查看内存信息:`$ cat /proc/meminfo`
- 显示内存page大小（以KByte为单位）:`getconf PAGESIZE` 或者 `getconf PAGE_SIZE.`
- 显示架构:`$ arch`

## 10、关于环境变量

`.bash_profile`、`.bashrc`

每次交互式登录时，Bash shell 都会执行 `.bash_profile`。如果在主目录中找不到 `.bash_profile`，Bash 将执行从 `.bash_login` 和 `.`. 中找到的第一个可读文件。而在每次交互式非登录 shell 启动时，Bash 都会执行 `.bashrc`

# 三、软件管理

## 1、软件包管理简介

软件包分类：
- 源码包：大部分都是 c和c++语言来实现的脚本安装包
	- 开源，如果有足够的能力，可以修改源代码；
	- 可以自由选择所需的功能；
	- 软件是编译安装，所以更加适合自己的系统，更加稳定也效率更高；
	- 卸载方便；
	- 安装过程步骤较多，尤其安装较大的软件集合时(如LAMP环境搭建)，容易出现 拼写错误；
	- 编译过程时间较长，安装比二进制安装时间长；
	- 因为是编译安装，安装过程中一旦报错新手很难解决
- 二进制包：rpm包，系统默认包，源代码经过编译之后
	- 包管理系统简单，只通过几个命令就可以 实现包的安装、升级、查询和卸载；
	- 安装速度比源码包安装快的多；
	- 经过编译，不再可以看到源代码；
	- 功能选择不如源码包灵活；
	- 依赖性

## 2、RPM包管理-rpm命令管理

### 2.1、RPM包命令规则

```
httpd-2.2.15-15.el6.centos.1.i686.rpm  软件包全名
httpd 		软件包名
2.2.15		软件版本
15			软件发布的次数
el6.centos 	适合的Linux平台
i686		适合的硬件平台
rpm 		rpm包扩展名
```

### 2.2、rpm 包依赖性

```
树形依赖： a --> b --> c
环形依赖： a --> b --> c --> a
模块依赖： 模块依赖查询网站：www.rpmfind.net
```
- 包全名与报名
	- 包全名：操作的包是没有安装的软件包时，使用包全名，而且要注意路径，在安装和升级使用
	- 包名：操作已经安装的软件包时，使用包名，是搜索`/var/lib/rpm/`中的数据库

### 2.3、rpm安装

```
rpm -ivh 包全名
	-i 	安装 
	-v 	显示详细信息
	-h 	显示进度
	--nodeps	不检测依赖性
	-U 	升级
rpm -e 包名：卸载
```


- `[root@localhost ~]# rpm -q 包名`：查询包是否安装
- `[root@localhost ~]# rpm –qa`：查询所有已经安装的RPM包
- `rpm –qi 包名`：查询软件包的详细信息
- `rpm –ql 包名`：查询包文件安装位置
- `rpm –qf 系统文件名`：查询系统文件属于哪个软件包
- `rpm –qR 包名`：查询软件包的依赖性
- `rpm –V 已安装的包名`：RPM包校验

## 3、RPM包管理-yum在线管理

### 3.1、网络yum源

```bash
[root@localhost yum.repos.d]# vi /etc/yum.repos.d/CentOS-Base.repo
[base]
name=CentOS-$releasever - Base - 163.com
baseurl=http://mirrors.163.com/centos/$releasever/os/$basearch/
gpgcheck=1
gpgkey=http://mirrors.163.com/centos/RPM-GPG-KEY-CentOS-7
```
- `[base]`：容器名称，一定要放在[]中
- name：容器说明，可以自己随便写
- mirrorlist：镜像站点，这个可以注释掉
- baseurl：我们的yum源服务器的地址。默认是CentOS官方的yum源服务 器，是可以使用的，如果你觉得慢可以改成你喜欢的yum源地 址
- enabled：此容器是否生效，如果不写或写成enable=1都是生效，写成 enable=0就是不生效
- gpgcheck：如果是1是指RPM的数字证书生效，如果是0则不生效
- gpgkey：数字证书的公钥文件保存位置。不用修改

### 3.2、yum命令

- `yum list`：查询所有可以用的软件包列表
- `yum search 关键字`：搜索服务器上所有和关键字相关的包
- `yum –y install 包名`：`-y`表示自动回答yes，安装软件
- `yum -y update 包名`：软件升级
- `yum -y remove 包名`：软件卸载
- `yum grouplist`：列出所有可用的软件组列表
- `yum groupinstall 软件组名`：安装指定软件组，组名可以由grouplist查询出来
- `yum groupremove 软件组名`：卸载指定软件组
- `yum erase 包名`：写在 

### 3.3、搭建光盘源

光盘yum源搭建步骤
- 挂载光盘：`mount /dev/cdrom /mnt/cdrom/`
- 让网络yum源文件失效：
	```bash
	[root@localhost ~]# cd /etc/yum.repos.d/
	[root@localhost yum.repos.d]# mv CentOS-Base.repo CentOS-Base.repo.bak
	[root@localhost yum.repos.d]# mv CentOS-Debuginfo.repo CentOS-Debuginfo.repo.bak
	[root@localhost yum.repos.d]# mv CentOS-Vault.repo CentOS-V ault.repo.bak
	```
- 修改光盘yum源文件
	```bash
	[root@localhost yum.repos.d]# vim CentOS-Media.repo [c6-media]
	name=CentOS-$releasever - Media baseurl=file:///mnt/cdrom #地址为你自己的光盘挂载地址
	# file:///media/cdrom/
	# file:///media/cdrecorder/
	#注释这两个不存在的地址
	gpgcheck=1
	enabled=1 #把enabled=0改为enabled=1，让这个yum源配置文件生效 gpgkey=file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-6
	```

## 4、源码包管理

### 4.1、源码包和RPM包的区别

- RPM包安装位置，其是安装在默认位置的
	- `/etc/`：配置文件安装目录
	- `/usr/bin/`：可执行的命令安装目录
	- `/usr/lib/`：程序所使用的函数库保存位置
	- `/usr/share/doc/`：基本的软件使用手册保存位置
	- `/usr/share/man/`：帮助文件保存位置
- 源码包安装位置：安装在指定位置当中，一般是`/usr/local/软件名/`


## 5、脚本安装包与软件包选择


## 6、软件路径

主执行文件会放在 `/usr/bin` 或者 `/usr/sbin` 下面，其他的库文件会放在 `/var` 下面，配置文件会放在 `/etc` 下面

# 四、系统管理

## 1、用户与用户组管理

### 1.1、用户信息文件

用户信息存放在文件：`/etc/passwd`，内容如下：

```
root:x:0:0:root:/root:/bin/bash
...
```
其是使用`:` 进行分割的，各个字段含义：

- 第1个字段：用户名称
- 第2个字段：密码标志，密码是存储在：`/etc/shadow`
- 第3个字段：UID（用户ID）
	- 0：超级用户
	- 1-499：系统用户（伪用户）
	- 500-65535：普通用户
- 第4个字段：GID（用户初始组ID） `初始组`就是用户一登录就就立刻拥有这个用户组的相关权限，每个用户的初始组只能有一个，一般就是和这个用户的用户名相同的组名作为这个用户的初始组；`附加组`指用户可以加入多个其他的用户组，并拥有这些组的权限，附加组可以有多个；
- 第5个字段：用户说明
- 第6个字段：家目录
	- 普通用户：/home/用户名/
	- 超级用户：/root/
- 第7个字段：登录之后的shell，即Linux的命令解释器，除了标准的`/bin/bash`之外，还可以写成：`/sbin/nologin, /usr/bin/passwd`等

### 1.2、影子文件

该文件位置：`/etc/shadow`
```
root:$1$RQF6Hgs/$8zbWgDRZfty9DGNWuhWSY.:17277:0:99999:7:::
```
其是使用`:` 进行分割的，各个字段含义：
- 第1字段：用户名
- 第2字段：加密密码
	- 加密算法升级为SHA512散列加密算法
	- 如果密码位是`!!`或`*`代表没有密码，不能登录；
- 第3字段：密码最后一次修改日期，使用1970年1月1日作为标准时间，每过一天时间戳加1
- 第4字段：两次密码的修改间隔时间（和第3字段相比）
- 第5字段：密码有效期（和第3字段相比）
- 第6字段：密码修改到期前的警告天数（和第5字段相比）
- 第7字段：密码过期后的宽限天数（和第5字段相比）
	- 0：代表密码过期后立即失效
	- -1：则代表密码永远不会失效
- 第8字段：账号失效时间，要用时间戳表示
- 第9字段：保留字段

**时间戳换算**
- 把时间戳换算为日期：`date -d "1970-01-01 17277 days"`
- 把日期换算为时间戳：`echo $(($(date --date="2018/11/16" +%s)/86400+1))`

### 1.3、组信息文件

**组信息文件`/etc/group`，`root:x:0:`**

- 第一字段：组名
- 第二字段：组密码标志
- 第三字段：GID
- 第四字段：组中附加用户

**组密码文件`/etc/gshadow`，`root:::`**

- 第一字段：组名
- 第二字段：组密码
- 第三字段：组管理员用户名
- 第四字段：组中附加用户

### 1.4、用户管理相关文件

- 用户的家目录：
	- 普通用户：`/home/用户名/`，所有者和所属组都是此用户，权限是700；
	- 超级用户：`/root/`，所有者和所属组都是 root用户，权限是550；
- 用户的邮箱：`/var/spool/mail/用户名/`；
- 用户模板目录：`/etc/skel/`，创建一个用户后，默认家目录下有如下文件：` .bash_logout  .bash_profile  .bashr`；

### 1.5、用户管理命令

#### 1.5.1、添加用户：useradd命令

`[root@localhost ~]#useradd [选项] 用户名`

- `-u` UID：手工指定用户的UID号
- `-d` 家目录：手工指定用户的家目录
- `-c` 用户说明： 手工指定用户的说明
- `-g` 组名：手工指定用户的初始组
- `-G` 组名：指定用户的附加组
- `-s` shell：手工指定用户的登录shell。默认是/bin/bash

添加的默认用户，实际上是在往下列文件中写内容
```
[root@localhost ~]# useradd user1
[root@localhost ~]# grep "user1" /etc/passwd
[root@localhost ~]# grep "user1" /etc/shadow
[root@localhost ~]# grep "user1" /etc/group
[root@localhost ~]# grep "user1" /etc/gshadow
[root@localhost ~]# ll -d /home/user1/
[root@localhost ~]# ll /var/spool/mail/user1
```

指定选项添加用户：
```
groupadd user2
useradd -u 550 -g user1 -G root -d /home/user2 -c "test user" -s /bin/bash user1
```

用户默认值文件
- `/etc/default/useradd`
	```
	GROUP=100	#用户默认组    
	HOME=/home	#用户家目录
	INACTIVE=-1	#密码过期宽限天数，为/etc/shadowd第7字段
	EXPIRE=		#密码失效时间，为/etc/shadowd第8字段
	SHELL=/bin/bash	#默认shell
	SKEL=/etc/skel	#模板目录
	CREATE_MAIL_SPOOL=yes	#是否建立邮箱
	```
- `/etc/login.defs`
	```
	PASS_MAX_DAYS 99999	#密码有效期，为/etc/shadowd第5字段
	PASS_MIN_DAYS 0	#密码修改间隔，为/etc/shadowd第4字段
	PASS_MIN_LEN 5	#密码最小5位(PAM)
	PASS_WARN_AGE 7	#密码到期警告，为`/etc/shadowd` 第6字段
	UID_MIN 500 	#最小和最大UID范围
	GID_MAX 60000	
	ENCRYPT_METHOD	SHA512 #加密模式
	```

#### 1.5.2、修改密码：passwd命令

`[root@localhost ~]#passwd [选项] 用户名`，如果不写用户名，则是为当前登录用户给密码
- `-S`：查询用户密码的密码状态。仅root用户 可用。
- `-l`：暂时锁定用户，仅root用户可用
- `-u`： 解锁用户，仅root用户可用
- `--stdin`：可以通过管道符输出的数据作为用户的密码。

查看密码状态：
```bash
[root@localhost ~]# passwd -S user1
user1 PS 2018-11-18 0 99999 7 -1 (Password set, MD5 crypt.)
#用户名密码设定时间(2018-11-18) 密码修改间隔时间(0) #密码有效期(99999) 警告时间(7) 密码不失效(-1)
```

锁定用户和解锁用户：`passwd -l user1`，`passwd -u user1`

使用字符串作为用户的密码：`echo "123" | passwd --stdin user2`

#### 1.5.3、修改用户信息：usermod

`[root@localhost ~]#usermod [选项] 用户名 选项`
- `-u UID`：修改用户的UID
- `-c` 用户说明：修改用户的说明信息
- `-G` 组名：修改用户的附加组
- `-L`：临时锁定用户
- `-U`

```bash
[root@localhost ~]# usermod -c "test user" lamp #修改用户的说明
[root@localhost ~]# usermod -G root lamp #把lamp用户加入root组
[root@localhost ~]# usermod -L lamp #锁定用户
[root@localhost ~]# usermod -U lamp #解锁用户
```

#### 1.5.4、修改用户密码状态：chage

`[root@localhost ~]#chage [选项] 用户名`
- `-l`: 列出用户的详细密码状态
- `-d` 日期：修改密码最后一次更改日期(shadow3字段)
- `-m` 天数：两次密码修改间隔(4字段) 
- `-M` 天数：密码有效期(5字段)
- `-W` 天数：密码过期前警告天数(6字段) 
- `-I` 天数：密码过后宽限天数(7字段) 
- `-E` 日期：账号失效时间(8字段)

```bash
[root@localhost ~]# chage -d 0 user1
#这个命令其实是把密码修改日期归0了(shadow第3字段) #这样用户一登陆就要修改密码
```

#### 1.5.5、删除用户：userdel

`[root@localhost ~]# userdel [-r] 用户名`，`-r`:删除用户的同时删除用户家目录

手工删除用户：
```bash
[root@localhost ~]# vi /etc/passwd
[root@localhost ~]# vi /etc/shadow
[root@localhost ~]# vi /etc/group
[root@localhost ~]# vi /etc/gshadow
[root@localhost ~]# rm -rf /var/spool/mail/user1
[root@localhost ~]# rm -rf /home/user1/
```

查看用户ID：`id 用户名`

#### 1.5.6、切换用户身份：su

`[root@localhost ~]# su [选项] 用户名`
- `-`：选项只使用`-`代表连带用户的环境 变量一起切换
- `-c` 命令：仅执行一次命令，而不切换用户身份

`su - root -c "useradd user3"`：不切换成root，但是执行useradd命令添加user1用户


## 2、权限管理

## 3、文件系统

## 4、服务管理

## 5、进程管理

## 6、日志管理

## 7、启动管理

## 8、备份与恢复


# 五、其他

## 1、CRONTAB

- [crontab-16个常用案例](https://www.linuxteck.com/basic-cron-command-in-linux-with-examples/)

CRONTAB是一种用于设置周期性执行任务的工具

yum install -y cornie crontabs 

crontab -e  编辑crontab表单

crontab -l  查看crontab表单任务

systemctl restart crond  重启crond服务

    * * * * * mycommand

配置文件

/etc/crontab


