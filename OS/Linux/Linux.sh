参考资料：
①http://www.92csz.com/study/linux/12.htm
②http://see.xidian.edu.cn/cpp/view/6994.html
③http://man.ddvip.com/linux/Mandrakelinuxref/index.html
④http://www.tldp.org/LDP/abs/html/index.html
⑤http://see.xidian.edu.cn/cpp/
一、Linux常用命令
1、echo，显示文字，用于向窗口输出文本
		语 法：echo [-ne][字符串]或 echo [--help][--version]
		补充说明：echo会将输入的字符串送往标准输出。输出的字符串间以空白字符隔开，并在最后加上换行号
		参 数：
			-n 不要在最后自动换行
			-e 若字符串中出现以下字符，则特别加以处理，而不会将它当成一般文字输出：
			\a 发出警告声；
			\b 删除前一个字符；
			\c 最后不加上换行符号；
			\f 换行但光标仍旧停留在原来的位置；
			\n 换行且光标移至行首；
			\r 光标移至行首，但不换行；
			\t 插入tab；
			\v 与\f相同；
			\\ 插入\字符；
			\nnn 插入nnn（八进制）所代表的ASCII字符；
			--help 显示帮助
			--version 显示版本信息
			每一个shell支持的echo指令可能都不太相同。
			-n 不输出最后的\n
			-e解释转义字符
			-E不x解释转义字符
			示例：echo "i am a boy" // 执行后会打印 i am a boy
			i am a boy
			> filename 显示结果定向至文件
			
8、压缩与解压:
	(1).tar(注：tar是打包，不是压缩)
		解包：tar xvf FileName.tar
		打包：tar cvf FileName.tar DirName
	(2).gz
		解压1：gunzip FileName.gz
		解压2：gzip -d FileName.gz
		压缩：gzip FileName

	(3).tar.gz 和 .tgz
		解压：tar zxvf FileName.tar.gz
		压缩：tar zcvf FileName.tar.gz DirName
	(4).bz2
		解压1：bzip2 -d FileName.bz2
		解压2：bunzip2 FileName.bz2
		压缩： bzip2 -z FileName
	(5).tar.bz2
		解压：tar jxvf FileName.tar.bz2
		压缩：tar jcvf FileName.tar.bz2 DirName
	(6).bz
		解压1：bzip2 -d FileName.bz
		解压2：bunzip2 FileName.bz
		压缩：未知

	(7).tar.bz
		解压：tar jxvf FileName.tar.bz
		压缩：未知
	(8).Z
		解压：uncompress FileName.Z
		压缩：compress FileName
	(9).tar.Z
		解压：tar Zxvf FileName.tar.Z
		压缩：tar Zcvf FileName.tar.Z DirName
	(10).zip
		解压：unzip FileName.zip
		压缩：zip FileName.zip DirName
	(11).rar
		解压：rar x FileName.rar
		压缩：rar a FileName.rar DirName

	(12).lha
		解压：lha -e FileName.lha
		压缩：lha -a FileName.lha FileName
	(13).rpm
		解包：rpm2cpio FileName.rpm | cpio -div
	———————————————
	(14).deb
		解包：ar p FileName.deb data.tar.gz | tar zxf -
	———————————————
	(15).tar .tgz .tar.gz .tar.Z .tar.bz .tar.bz2 .zip .cpio .rpm .deb .slp .arj .rar 
		.ace .lha .lzh .lzx .lzs .arc .sda .sfx .lnx .zoo .cab .kar .cpt .pit .sit .sea
		解压：sEx x FileName.*
		压缩：sEx a FileName.* FileName

		sEx只是调用相关程序，本身并无压缩、解压功能，请注意！

	(16).gzip 命令 
		减少文件大小有两个明显的好处，一是可以减少存储空间，二是通过网络传输文件时，可以减少传输的时间。
		gzip 是在 Linux系统中经常使用的一个对文件进行压缩和解压缩的命令，既方便又好用。
		
		语法：gzip [选项] 压缩（解压缩）的文件名该命令的各选项含义如下：

		-c 将输出写到标准输出上，并保留原有文件。-d 将压缩文件解压。-l 对每个压缩文件，显示下列字段：     	
		压缩文件的大小；未压缩文件的大小；压缩比；未压缩文件的名字
		-r 递归式地查找指定目录并压缩其中的所有文件或者是解压缩。
		-t 测试，检查压缩文件是否完整。
		-v 对每一个压缩和解压的文件，显示文件名和压缩比。
		-num 用指定的数字 num 调整压缩的速度，-1 或 -
		-fast 表示最快压缩方法（低压缩比），-9 或-
		-best表示最慢压缩方法（高压缩比）。系统缺省值为 6。指令实例：

		gzip *% 把当前目录下的每个文件压缩成 .gz 文件。gzip -dv *% 把当前目录下每个压缩的文件解压，
		并列出详细的信息。gzip -l *% 详细显示例1中每个压缩的文件的信息，并不解压。gzip usr.tar% 
		压缩 tar 备份文件 usr.tar，此时压缩文件的扩展名为.tar.gz
			(1).【gzip】
		语法： gzip [-d#] filename 其中#为1-9的数字
		-d ：解压缩时使用
		-# ：压缩等级，1压缩最差，9压缩最好，6为默认
		★注意:gzip不可以压缩目录
	(2).【bzip2】
		语法：bzip2 [-dz] filename
		-d ：解压缩
		-z ：压缩

	(3).【tar】
		语法：tar [-zjxcvfpP] filename
		-z ：是否同时用gzip压缩
		-j ：是否同时用bzip2压缩
		-x ：解包或者解压缩
		-t ：查看tar包里面的文件
		-c ：建立一个tar包或者压缩文件包
		-v ：可视化
		-f ：后面跟文件名，压缩时跟-f文件名，意思是压缩后的文件名为filename，解压时跟-f文件名，
			意思是解压filename。请注意，如果是多个参数组合的情况下带有-f，请把f写到最后面。
		-p ：使用原文件的属性，压缩前什么属性压缩后还什么属性。（不常用）
		-P ：可以使用绝对路径。（不常用）
		--exclude filename ：在打包或者压缩时，不要将filename文件包括在内。（不常用）
		zip命令可以用来将文件压缩成为常用的zip格式。unzip命令则用来解压缩zip文件。

1. 我想把一个文件abc.txt和一个目录dir1压缩成为yasuo.zip：

＃ zip -r yasuo.zip abc.txt dir1

2.我下载了一个yasuo.zip文件，想解压缩：

# unzip yasuo.zip

3.我当前目录下有abc1.zip，abc2.zip和abc3.zip，我想一起解压缩它们：

＃ unzip abc\?.zip

注释：?表示一个字符，如果用*表示任意多个字符。

4.我有一个很大的压缩文件large.zip，我不想解压缩，只想看看它里面有什么：

# unzip -v large.zip

5.我下载了一个压缩文件large.zip，想验证一下这个压缩文件是否下载完全了

# unzip -t large.zip

6.我用-v选项发现music.zip压缩文件里面有很多目录和子目录，并且子目录中其实都是歌曲mp3文件，我想把这些文件都下载到第一级目录，而不是一层一层建目录：

# unzip -j music.zip

10、ls [-alrtAFR] [name...]，显示指定工作目录下之内容（列出目前工作目录所含之档案及子目录)
	-a 显示所有档案及目录 (ls内定将档案名或目录名称开头为"."的视为隐藏档，不会列出) 
　　-l 除档案名称外，亦将档案型态、权限、拥有者、档案大小等资讯详细列出 
　　-r 将档案以相反次序显示(原定依英文字母次序) 
　　-t 将档案依建立时间之先后次序列出 
　　-A 同 -a ，但不列出 "." (目前目录) 及 ".." (父目录) 
　　-F 在列出的档案名称后加一符号；例如可执行档则加 "*", 目录则加 "/" 
　　-R 若目录下有档案，则以下之档案亦皆依序列出
	-s：在每个文件旁边显示该文件的大小，单位为千字节
	-d：像一般文件那样处理目录，而不是列出它们的内容

13、date，打印当前系统的时间
	date "+%Y-%m-%d %H:%M:%S" ---> 2014-04-08 11:51:47
	其中：%Y表示年，%m表示月，%d表示日期，%H表示小时，%M表示分钟，%S表示秒	
	#注意%y和%Y的区别
		date "+%y%m%d" ---> 140408
		date "+%Y%m%d" ---> 20140408	
		
	-d 选项也是经常要用到的，它可以打印n天前或者n天后的日期，当然也可以打印n个月/年前或者后的日期	
		date -d "-1 month" "+%Y%m%d" ---> 20140308
		date -d "-1 year" "+%Y%m%d" ---> 20130408
	
	

		
16、ulimit 用于限制 shell 启动进程所占用的资源，支持以下各种类型的限制：
	所创建的内核文件的大小、进程数据块的大小、Shell 进程创建文件的大小、内存锁住的大小、常驻内存集的大小、
	打开文件描述符的数量、分配堆栈的最大大小、CPU 时间、单个用户的最大线程数、Shell 进程所能使用的最大虚拟内存。
	同时，它支持硬资源和软资源的限制  
	参考：http://blog.csdn.net/wanghai__/article/details/6332540
	ulimit 通过一些参数选项来管理不同种类的系统资源：
	(1).ulimit 命令的格式为：ulimit [options] [limit]
		选项 [options]	含义	
		-H	 设置硬资源限制，一旦设置不能增加。	 
				ulimit – Hs 64；限制硬资源，线程栈大小为 64K。
		-S	 设置软资源限制，设置后可以增加，但是不能超过硬资源设置。	 
				ulimit – Sn 32；限制软资源，32 个文件描述符。
		-a	 显示当前所有的 limit 信息。	 
				ulimit – a；显示当前所有的 limit 信息。
		-c	 最大的 core 文件的大小， 以 blocks 为单位。	 
				ulimit – c unlimited； 对生成的 core 文件的大小不进行限制。
		-d	 进程最大的数据段的大小，以 Kbytes 为单位。	 
				ulimit -d unlimited；对进程的数据段大小不进行限制。
		-f	 进程可以创建文件的最大值，以 blocks 为单位。	 
				ulimit – f 2048；限制进程可以创建的最大文件大小为 2048 blocks。
		-l	 最大可加锁内存大小，以 Kbytes 为单位。	 
				ulimit – l 32；限制最大可加锁内存大小为 32 Kbytes。
		-m	 最大内存大小，以 Kbytes 为单位。	
				ulimit – m unlimited；对最大内存不进行限制。
		-n	 可以打开最大文件描述符的数量。	 
				ulimit – n 128；限制最大可以使用 128 个文件描述符。
		-p	 管道缓冲区的大小，以 Kbytes 为单位。	 
				ulimit – p 512；限制管道缓冲区的大小为 512 Kbytes。
		-s	 线程栈大小，以 Kbytes 为单位。	 
				ulimit – s 512；限制线程栈的大小为 512 Kbytes。
		-t	 最大的 CPU 占用时间，以秒为单位。	 
				ulimit – t unlimited；对最大的 CPU 占用时间不进行限制。
		-u	 用户最大可用的进程数。	 
				ulimit – u 64；限制用户最多可以使用 64 个进程。
		-v	 进程最大可用的虚拟内存，以 Kbytes 为单位。	 
				ulimit – v 200000；限制最大可用的虚拟内存为 200000 Kbytes。


25、cd - ：返回访问过的前一个目录
	su ：切换到root用户，ctrl+D 退出root用户
	less：分页器，用来帮助用户一页一页(更准确地说，应该是一屏一屏)地查看长文件的程序
		要在该文件中浏览使用上下方向键,按 Q 就可以退出,less 能做的要比只是显示：“按 H 键显示可用选项帮助”要多得多。

26、常用键盘快捷键：
	(1)、左、右键在当前行移动光标，让您能够编辑您的命令。然而还有别的方式让您一次不止移动一个字符：
	例如 Ctrl-A 和 Ctrl-E 可以移到当前行的开头以及结尾。Backspace 和 Del 正如期望的那样起作用。
	Backspace 相当于 Ctrl-H。Del 和 Ctrl-D 也一样。Ctrl-K 可以从光标处删除到行末。
	Ctrl-W (或者 Alt-Backspace)可以删除光标前的一个单词。

	(2)、在一个空白行中键入 Ctrl-D 将结束当前会话，这要比键入 exit 方便得多。Ctrl-C 将会打断当前正在执行的命令，
	不过如果您是在编辑命令行的过程中，它将会取消所有的编辑并返回到提示符。Ctrl-L 可以清屏。Ctrl-Z 暂停某项任务，
	并将其挂起。该快捷方式在您忘记在命令之后键入 “&” 时非常有用。比如：
	$ xpdf MyDocument.pdf
	不过这样一来您就不能再使用您的 shell 了，因为其前台被任务 xpdf 进程占领。
	要将该任务转入后台运行并恢复您对 shell 的控制，只要按下 Ctrl-Z 后键入 bg。
	最后，Ctrl-S 和 Ctrl-Q 分别用来暂停和继续对屏幕的输出。它们不常用，不过您可能会误按 Ctrl-S (
	毕竟在键盘上 S 和 D 靠的很近)。所以，如果您碰上了不管您输入什么都不能在终端屏幕上看得的怪事时，
	请试着按按 Ctrl-Q。注意：您在 Ctrl-S 和 Ctrl-Q 之间输入的所有字符将会被一起显示到屏幕上
	
二、Linux
1、文件基础
	drwxrwxr-x.  9 hadoop hadoop     4096 Apr 10 15:27 apache-nutch-2.1
	-rw-r--r--.  1 hadoop hadoop  2699959 Oct 22 17:43 apache-nutch-2.1-src.tar.gz
	drwxrwxr-x.  2 hadoop hadoop     4096 Apr 10 16:50 demo
	drwxr-xr-x. 17 hadoop hadoop     4096 Apr  3 11:47 hadoop-1.0.4
	-rw-r--r--.  1 hadoop hadoop 62793050 Oct 22 11:09 hadoop-1.0.4.tar.gz
	drwxr-xr-x.  9 hadoop hadoop     4096 Oct 28 19:04 hbase-0.90.6
	-rw-rw-r--.  1 hadoop hadoop 31645468 Mar 16  2012 hbase-0.90.6.tar.gz
	-rw-rw-r--.  1 hadoop hadoop      100 Sep 18  2013 jstatd.all.policy
	drwxrwxr-x.  5 hadoop hadoop     4096 Nov 27 17:02 LoadTester
	drwxrwxr-x.  2 hadoop hadoop     4096 Apr  4 11:24 nutch
	-rw-rw-r--.  1 hadoop hadoop        0 Oct 16 16:21 rpm
	drwxrwxr-x.  2 hadoop hadoop     4096 Apr  8 14:07 sbin
	-rw-rw-r--.  1 hadoop hadoop      272 Dec 12 16:59 seed.txt
	drwxrwxr-x.  2 hadoop hadoop     4096 Oct 22 11:12 softwares
	drwxrwxr-x.  2 hadoop hadoop     4096 Apr  4 11:02 temp
	drwxr-xr-x.  7 hadoop hadoop     4096 Jul 26  2013 visualvm_136
	-rw-rw-r--.  1 hadoop hadoop   430723 Mar 31 15:15 wget-log
	-rw-rw-r--.  1 hadoop hadoop       17 Apr 10 11:54 xaa

d rwx rwx r-x
- rw- rw- r--
ls -l 命令的结果为(按自左至右顺序)：
$ ls -l
total 1
-rw-r-----   1 zhang    users           0 7月  8 14:11 某一_文件
drwxr-xr--   2 li_si    users        1024 7月  8 14:11 某一_目录/
$ 
(1).前十个字符表示该文件的类型及其权限。其中第一个字符表示该文件的类型：短横(-)表示是普通文件，
字符 d 表示是目录。还有其他文件类型将在下文讨论。接下来的九个字符表示该文件的权限。
实际上，这九个字符分成三组，每组又分为三种权限。
第一组代表了该文件的所有者对它的权限，第二组是该文件所有组中其他成员的权限，而第三组表示了其他人的权限。
#短横(-)表示没有某一权限。
(2).接下来的数字表示到该文件的链接。稍后，我们将会发现某个文件的唯一标识符不是它的名字而是一个数字
(称为inode 号)。而且磁盘上的一个文件可以同时拥有好几个名字。对于目录，该链接数具有特殊的含义，
我们在下文中也会提及。(第二列)
	再接下来的是该文件所有者的名字(第三列:zhang)和所有组的名字(第四列:users)。
	最后是该文件的大小(以字节为单位)、最后修改日期和该文件或目录的名字。

(3)让我们更进一步观察这些文件的权限。
	①首先，我们必须去掉代表文件类型的第一个字符。这样，对于文件某一_文件，
		我们就得到了：rw-r-----。下面对其逐项解释。
	②前三个字符(rw-)代表所有者(在该例中是 zhang)的权限。因此，zhang 有权阅读该文件(r)，
		修改其内容(w)，但却不能执行它(-)。
	③接下来的三个字符(r--)代表了 users 组中除了 zhang 的其他用户的权限。
		他们能够阅读该文件(r)，但是既不能写也不能执行(--)。
	④最后三个字符(---)代表除zhang 及 users 组成员外的其他用户对该文件的权限。
		那些用户对这个文件没有任何权限。

(4)对某一目录这个目录，其权限为 rwxr-xr--，因此：
	目录所有者li_si可以列出其中的文件(r)，在其中添加或删除文件(w)，以及对其遍历(x)。
	users 组中除li_si的其他成员能够列出其中的文件(r)，但是不能够在其中添加或删除文件(-)，
	但却能对其遍历(x)。
	剩下的用户只能列出该目录包含的文件(r)。由于他们没有 wx 权限，他们就不能够写入文件或进入该目录。

(5).表示文件类型有”d”, “-“ ，其实除了这两种外还有”l”, “b”, “c”,”s”等。
	d 表示该文件为目录；
	- 表示该文件为普通文件；
	l 表示该文件为连接文件（linux file），上边提到的软连接即为该类型；
	b 表示该文件为块设备文件，比如磁盘分区
	c 表示该文件为串行端口设备，例如键盘、鼠标。
	s 表示该文件为套接字文件（socket），用于进程间通信。
(6).linux的文件系统格式为Ext2，或者Ext3
	①Ext2文件系统是非日志文件系统
	②Ext3文件系统是直接从Ext2文件系统发展而来，Ext3文件系统带有日志功能，可以跟踪记录文件系统的变化，
	并将变化内容写入日志，写操作首先是对日志记录文件进行操作，若整个写操作由于某种原因 (如系统掉电) 而中断，
	系统重启时，会根据日志记录来恢复中断前的写操作，而且这个过程费时极短
	③Linux文件系统在windows中是不能识别的，但是在linux系统中你可以挂载的windows的文件系统，
	linux目前支持MS-DOS，VFAT，FAT，BSD等格式
2、文件处理命令
	(1).mkdir、touch：创建空的目录、文件
		mkdir [选项] <目录> [目录 ...]
			-p:创建原本不存在的上级目录,想要创建的目录已经存在，则直接返回；否则报错
			-m:这个参数用来指定要创建目录的权限
		touch 
			用来更新文件的访问和修改时间的[3]。不过，如果文件不存在，touch 会创建相应的空文件
	

3、Linux系统关机问题
	(1).要关机确保当前没有用户在线: who
	(2).查看网络的联机状态：netstat -a
	3.1.正确的关机流程为：sysnc>shutdown>reboot>halt
		sync 将数据由内存同步到硬盘中。
		shutdown 关机指令，你可以man shutdown 来看一下帮助文档。例如你可以运行如下命令关机：
		shutdown –h 10 ‘This server will shutdown after 10 mins’ 这个命令告诉大家，
			计算机将在10分钟后关机，并且会显示在登陆用户的当前屏幕中。
		Shutdown –h now 立马关机
		Shutdown –h 20:25 系统会在今天20:25关机
		Shutdown –h +10 十分钟后关机
		Shutdown –r now 系统立马重启
		Shutdown –r +10 系统十分钟后重启
		reboot 就是重启，等同于 shutdown –r now
		halt 关闭系统，等同于shutdown –h now 和 poweroff
	#最后总结一下，不管是重启系统还是关闭系统，首先要运行sync命令，把内存中的数据写到磁盘中。
	#关机的命令有 shutdown –h now halt poweroff 和 init 0 , 
	#重启系统的命令有 shutdown –r now reboot init 6.

4、文件与目录：
	(1)[./ 表示当前目录]，[../指的是当前目录的上一级目录]
	
5、显示文件的内容
	(1). more 也是用来查看一个文件的内容。当文件内容太多，一屏幕不能占下，而你用cat肯定是看不前面的内容的，
	那么使用more就可以解决这个问题了。当看完一屏后按空格键继续看下一屏。但看完所有内容后就会退出。
	如果你想提前退出，只需按q键即可。
		less 作用跟more一样，但比more好在可以上翻，下翻。
	空格键同样可以翻页，而按”j”键可以向下移动（按一下就向下移动一行），按”k”键向上移动。
	在使用more和less查看某个文件时，你可以按一下”/” 键，然后输入一个word回车，这样就可以查找这个word了。
	如果是多个该word可以按”n”键显示下一个。另外你也可以不按”/”而是按”?”后边同样跟word来搜索这个word，
	唯一不同的是，”/”是在当前行向下搜索，而”?”是在当前行向上搜索。
	(2).head 后直接跟文件名，则显示文件的前十行。如果加 –n 选项则显示文件前n行;
		tail 和 head 一样，后面直接跟文件名，则显示文件最后十行。如果加-n 选项则显示文件最后n行;
		-f 动态显示文件的最后十行，如果文件是不断增加的，则用-f 选项。如：tail -f /var/log/messages

6、	更改文件的权限
	(1).更改所属组:
		语法：chgrp [组名] [文件名]
	(2).更改文件的所属主 chown
		语法：chown [ -R ] 账户名 文件名
		chown [ -R ] 账户名：组名 文件名
	这里的-R选项只作用于目录，作用是级联更改，即不仅更改当前目录，连目录里的目录或者文件全部更改
	(3).改变用户对文件的读写执行权限 chmod		
		chmod 语法： chmod [-R] xyz 文件名 (这里的xyz，表示数字)
		-R 选项作用同chown，级联更改;
		①在linux中为了方便更改这些权限，linux使用数字去代替rwx ，具体规则为r: 4 w:2 x:1 -:0 
		举个例子，-rwxrwx---用数字表示就是 770，具体是这样来的
		#在linux系统中，默认一个目录的权限为 755，而一个文件的默认权限为644.
		②chmod 还支持使用rwx的方式来设置权限。！从之前的介绍中我们可以发现，基本上就九个属性分别是:
		[user][group][others] 三群啦！那么我们就可以藉由 u, g, o 来代表三群的属性！
		此外， a 则代表 all 亦即全部的三群！那么读写的属性就可以写成了 r, w, x！也就是可以使用底下的方式来看
		chmod [u|g|o|a] [+|-|=] [r|w|x] 文件/目录
			+:表示加入权限;		-:表示去掉某个权限;		=:设定为某个权限;
		chmod u-x demo/test1.txt
		
7、umask语法： umask xxx (这里的xxx代表三个数字)
	查看umask值只要输入umask然后回车。 umask预设是0022，其代表什么含义？先看一下下面的规则：
	(1).若用户建立为普通文件，则预设“没有可执行权限”，只有rw两个权限。最大为666(-rw-rw-rw-)
	(2).若用户建立为目录，则预设所有权限均开放，即777(drwxrwxrwx)
		umask数值代表的含义为，上边两条规则中的默认值（文件为666，目录为777）需要减掉的权限。
		所以目录的权限为(rwxrwxrwx) – (----w--w-) = (rwxr-xr-x)，
		普通文件的权限为(rw-rw-rw-) – (----w--w-) = (rw-r--r--)。
		umask的值是可以自定义的，比如设定umask 为 002，你再创建目录或者文件时，
		默认权限分别为(rwxrwxrwx) – (-------w-) = (rwxrwxr-x)和(rw-rw-rw-) – (-------w-) = (rw-rw-r--)。
	(3) umask 可以在/etc/bashrc里面更改，预设情况下，root的umask为022，而一般使用者则为002，
		因为可写的权限非常重要，因此预设会去掉写权限。
		
8、文件的特殊属性
	(1)chattr 修改文件的特殊属性
	语法： chattr [+-=][ASaci [文件或者目录名]
		+-= ：分别为增加、减少、设定
		A：增加该属性后，文件或目录的atime将不可被修改；
		S：增加该属性后，会将数据同步写入磁盘中；
		a：增加该属性后，只能追加不能删除，非root用户不能设定该属性；
		c：自动压缩该文件，读取时会自动解压；
		i：增加后，使文件不能被删除、重命名、设定连接、写入、新增数据、不能在该目录中建立文件；
	(2)lsattr 列出文件/目录的特殊属性
	语法： lsattr [-aR] [文件/目录名]
		-a：类似与ls 的-a 选项，即连同隐藏文件一同列出；
		-R：连同子目录的数据一同列出
		
9、在Linux下搜索文件:
	(1) whereis 通过预先生成的一个文件列表库去查找跟给出的文件名相关的文件。
		语法： whereis [-bmsu] [文件名称]
		-b：只找binary 文件
		-m：只找在说明文件manual路径下的文件
		-s：只找source来源文件
		-u：没有说明档的文件
	(2) locate:通过查找预先生成的文件列表库来告诉用户要查找的文件在哪里。后边直接跟文件名;
	(3) find 语法： find [路径] [参数] 
		-atime +n ：访问或执行时间大于n天的文件
		-ctime +n ：写入、更改inode属性（例如更改所有者、权限或者连接）时间大于n天的文件
		-mtime +n ：写入时间大于n天的文件
	#注:
		Ⅰ.文件的 Access time，atime 是在读取文件或者执行文件时更改的。
		文件的 Modified time，mtime 是在写入文件时随文件内容的更改而更改的。
		文件的 Create time，ctime 是在写入文件、更改所有者、权限或链接设置时随 Inode 的内容更改而更改的。 
		因此，更改文件的内容即会更改 mtime 和 ctime，但是文件的 ctime 可能会在 mtime 未发生任何变化时更改，
		例如，更改了文件的权限，但是文件内容没有变化。 
		Ⅱ.如何获得一个文件的atime mtime 以及ctime ？
			ls -l 命令可用来列出文件的 atime、ctime 和 mtime。
			ls -lc filename         列出文件的 ctime
			ls -lu filename         列出文件的 atime
			ls -l filename          列出文件的 mtime  
			atime不一定在访问文件之后被修改，因为：使用ext3文件系统的时候，
			如果在mount的时候使用了noatime参数那么就不会更新atime的信息。
			而这是加了 noatime 取消了, 不代表真实情況。反正, 這三個 time stamp 都放在 inode 中。
			若 mtime, atime 修改inode 就一定會改, 既然 inode 改了, 那 ctime 也就跟著要改了
		find 参数:
			①.-name filename 直接查找该文件名的文件;
			②.-type type ：通过文件类型查找.type 包含了 f, b, c, d, l, s 等等		
		
10、ln 建立连接档
	 ln [-s] [来源文件] [目的文件]
	 ln 常用的选项就一个-s ，如果不加就是建立硬连接，加上就建立软连接。
	 
11、Linux用户以及用户组
	(1)./etc/passwd和/etc/shadow
		①/etc/passwd
			root:x:0:0:root:/root:/bin/bash
		/etc/passwd由’:’分割成7个字段，每个字段的具体含义是：
			1）用户名（如第一行中的root就是用户名），代表用户账号的字符串。
			用户名字符可以是大小写字母、数字、减号（不能出现在首位）、点以及下划线，其他字符不合法。
			虽然用户名中可以出现点，但不建议使用，尤其是首位为点时，另外减号也不建议使用，因为容易造成混淆。
			2）存放的就是该账号的口令，为什么是’x’呢？早期的unix系统口令确实是存放在这里，
			但基于安全因素，后来就将其存放到/etc/shadow中了，在这里只用一个’x’代替。
			3）这个数字代表用户标识号，也叫做uid。系统识别用户身份就是通过这个数字来的，0就是root，
			也就是说你可以修改test用户的uid为0，那么系统会认为root和test为同一个账户。通常uid的取值范围是0~65535，
			0是超级用户（root）的标识号，1~499由系统保留，作为管理账号，普通用户的标识号从500开始，
			如果我们自定义建立一个普通用户，你会看到该账户的标识号是大于或等于500的。
			4）表示组标识号，也叫做gid。这个字段对应着/etc/group 中的一条记录，
			其实/etc/group和/etc/passwd基本上类似。
			5）注释说明，该字段没有实际意义，通常记录该用户的一些属性，例如姓名、电话、地址等等。不过，
			当你使用finger的功能时就会显示这些信息的（稍后做介绍）。
			6）用户的家目录，当用户登录时就处在这个目录下。root的家目录是/root，普
			通用户的家目录则为/home/username，这个字段是可以自定义的，比如你建立一个普通用户test1，
			要想让test1的家目录在/data目录下，只要修改/etc/passwd文件中test1那行中的该字段为/data即可。
			7）shell，用户登录后要启动一个进程，用来将用户下达的指令传给内核，这就是shell。
			Linux的shell有很多种sh, csh, ksh, tcsh, bash等，而Redhat/CentOS的shell就是bash。
			查看/etc/passwd文件，该字段中除了/bin/bash外还有/sbin/nologin比较多，它表示不允许该账号登录。
			如果你想建立一个账号不让他登录，那么就可以把该字段改成/sbin/nologin，默认是/bin/bash
		②/etc/shadow
			root:!:16167:0:99999:7:::
		/etc/shadow这个文件，和/etc/passwd类似，用”:”分割成9个字段。
			1）用户名，跟/etc/passwd对应。
			2）用户密码，这个才是该账号的真正的密码，不过这个密码已经加密过了，但是有些黑客还是能够解密的。
			所以为了安全，该文件属性设置为600，只允许root读写。
			3）上次更改密码的日期，这个数字是这样计算得来的，距离1970年1月1日到上次更改密码的日期，
			例如上次更改密码的日期为2012年1月1日，则这个值就是365*（2012-1970）+1=15331。
			4）要过多少天才可以更改密码，默认是0，即不限制。
			5）密码多少天后到期。即在多少天内必须更改密码，例如这里设置成30，则30天内必须更改一次密码，
			否则将不能登录系统，默认是99999，可以理解为永远不需要改。
			6）密码到期前的警告期限，若这个值设置成7，则表示当7天后密码过期时，系统就发出警告告诉用户，
			提醒用户他的密码将在7天后到期。
			7）账号失效期限。你可以这样理解，如果设置这个值为3，则表示：密码已经到期，
			然而用户并没有在到期前修改密码，那么再过3天，则这个账号就失效了，即锁定了。
			8）账号的生命周期，跟第三段一样，是按距离1970年1月1日多少天算的。
			它表示的含义是，账号在这个日期前可以使用，到期后账号作废。
			9）作为保留用的，没有什么意义。
	(2).新增/删除用户和用户组
		a.新增一个组 groupadd [-g GID] groupname
			不加-g 则按照系统默认的gid创建组，跟用户一样，gid也是从500开始的;
		b. 删除组 gropudel groupname
		c. 增加用户 useradd [-u UID] [-g GID] [-d HOME] [-M] [-s]
			-u 自定义UID
			-g 使其属于已经存在的某个GID
			-d 自定义用户的家目录
			-M	不建立家目录
			-s	自定义shell
		d. 删除用户 userdel [-r] username:-r 选项的作用是删除用户时，连同用户的家目录一起删除
			
	(3).创建/修改一个用户的密码 “passwd [username]”
		创建完账户后，默认是没有设置密码的，虽然没有密码，但该账户同样登录不了系统。只有设置好密码后方可登录系统。
		使用 su - 就可以切换成root身份，前提是知道root的密码。
		①su 的语法为： su [-] username:加”-“后会连同用户的环境变量一起切换过来
		echo $LOGNAME来查看当前登录的用户名
		②默认只有root用户能使用sudo命令，普通用户想要使用sudo，是需要root预先设定的，
		即使用visudo命令去编辑相关的配置文件/etc/sudoers。
		如果没有visudo这个命令，请使用” yum install -y sudo”安装。

12、Linux磁盘管理：
	(1).查看磁盘或者目录的容量 df 和 du
		①df 查看已挂载磁盘的总容量、使用容量、剩余容量等，可以不加任何参数，默认是按k为单位显示的
		常用参数有 –i -h -k –m
			-i 使用inodes 显示结果
			-h 使用合适的单位显示，例如G
			-k -m 分别为使用K，M为单位显示
		②du 用来查看某个目录所占空间大小
		语法：du [-abckmsh] [文件或者目录名] 常用的参数有：
			-a：全部文件与目录大小都列出来。如果不加任何选项和参数只列出目录（包含子目录）大小;
			-b：列出的值以bytes为单位输出，默认是以Kbytes;
			-c：最后加总;
			-k：以KB为单位输出;
			-m：以MB为单位输出;
			-s：只列出总和;
			-h：系统自动调节单位，例如文件太小可能就几K，那么就以K为单位显示，
			如果大到几G，则就以G为单位显示。笔者习惯用 du –sh filename 这样的形式。
	(2).磁盘的分区和格式化
		①.fdisk linux下的硬盘分区工具
			语法： fdisk [-l ] [设备名称]
			-l ：后边不跟设备名会直接列出系统中所有的磁盘设备以及分区表，加上设备名会列出该设备的分区表。
			如果不加-l 则进入另一个模式，在该模式下，可以对磁盘进行分区操作。
		②mkfs.ext2 / mkfs.ext3 /mke2fs 格式化linux硬盘分区
		常用的选项有：
			-b：分区时设定每个数据区块占用空间大小，目前支持1024, 2048 以及4096 bytes每个块。
			-i：设定inode大小
			-N：设定inode数量，有时使用默认的inode数不够用，所以要自定设定inode数量。
			-c：在格式化前先检测一下磁盘是否有问题，加上这个选项后会非常慢
			-L：预设该分区的标签label
			-j：建立ext3格式的分区，如果使用mkfs.ext3 就不用加这个选项了
		③e2label 用来查看或者修改分区的标签（label）
		④fsck 检查硬盘有没有坏道
		语法： fsck [-Aar] [分区]
		-A ：加该参数时，后不需要跟分区名作为参数。
			它会自动检查/etc/fstab 文件下的所有分区(开机过程中就会执行一次该操作)；
		-a ：自动修复检查到有问题的分区；
		-r ：当检查到有坏道的分区时会让用户决定是否修复
		#当你使用fsck检查磁盘有无坏道时，会提示用户“跑这个任务可能会导致某些挂载的文件系统损坏”，
		#所以这个命令不要轻易运行。否则真的遇到问题，系统甚至都不能启动了。
	(3).挂载/卸载磁盘
		在挂载某个分区前需要先建立一个挂载点，这个挂载点是以目录的形式出现的。
		一旦把某一个分区挂载到了这个挂载点（目录）下，那么再往这个目录写数据使，
		则都会写到该分区中。这就需要你注意一下，在挂载该分区前，挂载点（目录）下必须是个空目录。
		其实目录不为空并不影响所挂载分区的使用，但是一旦挂载上了，那么该目录下以前的东西就不能看到了。
		只有卸载掉该分区后才能看到
		①mount 挂载设备
		#注:文件 /etc/fstab
			这个文件是系统启动时，需要挂载的各个分区。第一列就是分区的label；第二列是挂载点；第三列是分区的格式；
			第四列则是mount的一些挂载参数，等下会详细介绍一下有哪些参数，一般情况下，直接写defaults即可；
			第五列的数字表示是否被dump备份，是的话这里就是1，否则就是0；第六列是开机时是否自检磁盘，
			就是刚才讲过的那个fsck检测。1，2都表示检测，0表示不检测;
			◆第四列中常用到的参数了。
				async/sync ：async表示和磁盘和内存不同步，系统每隔一段时间把内存数据写入磁盘中，
					而sync则会时时同步内存和磁盘中数据；
				auto/noauto ：开机自动挂载/不自动挂载；
				default：按照大多数永久文件系统的缺省值设置挂载定义，
					它包含了rw, suid, dev, exec, auto, nouser,async ；
				ro：按只读权限挂载 ；
				rw：按可读可写权限挂载 ；
				exec/noexec ：允许/不允许可执行文件执行，但千万不要把根分区挂载为noexec，
					那就无法使用系统了，连mount命令都无法使用了，这时只有重新做系统了；
				user/nouser ：允许/不允许root外的其他用户挂载分区，为了安全考虑，请用nouser ；
				suid/nosuid ：允许/不允许分区有suid属性，一般设置nosuid ；
				usrquota ：启动使用者磁盘配额模式，磁盘配额相关内容在后续章节会做介绍；
				grquota ：启动群组磁盘配额模式；
				可以自己修改这个文件，增加一行来挂载新增分区

			mount -a:挂载增加的那行，不用重启就可以直接使用;
			mount -o:重新挂载一个分区，并同时指定想要的选项;
			mount -t:后边指定文件系统的类型,比如挂载软盘时就需要指定 vfat，而挂载光盘时就需要指定iso9660;
				系统都是智能识别所要挂载分区的系统格式类别
		②blkid:用来显示磁盘分区uuid的
			uuid其实就是一大串字符，在linux系统中每一个分区都会有唯一的一个uuid
		③umount 卸载设备
			umount -l 卸载设备:强制卸载
	(4).建立一个swap文件:类似与windows的虚拟内存
		★基本的思路就是：建立swapfile --> 格式化为swap格式 --> 启用该虚拟磁盘
		①dd if=/dev/zero of=/tmp/newdisk bs=4k count=102400
		利用dd 来创建一个419M的文件/tmp/newdisk出来，其中if代表从哪个文件读，
		/dev/zero是linux下特有的一个0生成器，of表示输出到哪个文件，bs即块大小，count则定义有多少个块
		② mkswap 这个命令是专门格式化swap格式的分区的
		③ free 是用来查看系统内存以及虚拟内存使用情况的，-m选项是以M的形式查看
		④ swapoff 关闭启用的swap文件
	(5).磁盘配额
		在linux中，用来管理磁盘配额的东西就是quota了,quota 这个模块主要分为
		quota quotacheck quotaoff quotaon quotastats edquota setquota warnquota repquota这几个命令
		①quota 用来显示某个组或者某个使用者的限额。
			语法：quota [-guvs] [user,group]
			-g ：显示某个组的限额
			-u ：显示某个用户的限额
			-v ：显示的意思
			-s ：选择inod或硬盘空间来显示
		②quotacheck 用来扫描某一个磁盘的quota空间。
			语法：quotacheck [-auvg] /path
			-a ：扫描所有已经mount的具有quota支持的磁盘
			-u ：扫描某个使用者的文件以及目录
			-g ：扫描某个组的文件以及目录
			-v ：显示扫描过程
			-m ：强制进行扫描
		③edquota 用来编辑某个用户或者组的quota值
			语法：edquota [-u user] [-g group] [-t]
			edquota -p user -u user
			-u ：编辑某个用户的quota
			-g ：编辑某个组的quota
			-t ：编辑宽限时间
			-p ：拷贝某个用户或组的quta到另一个用户或组
		④quotaon 启动quta，在编辑好quota后，需要启动才能是quta生效
			语法：quotaon [-a] [-uvg directory]
			-a ：全部设定的quota启动
			-u ：启动某个用户的quota
			-g ：启动某个组的quota
			-s ：显示相关信息
		⑤quotaoff 关闭quota:该命令常用只有一种情况 quotaoff -a 关闭全部的quota
	
13、文本编辑工具vim
	(1).vim的三种模式：一般模式、编辑模式、命令模式:
	◆一般模式： 当你vim filename 编辑一个文件时，一进入该文件就是一般模式了。
	在这个模式下，你可以做的操作有，上下移动光标；删除某个字符；删除某行；复制、粘贴一行或者多行。
	◆编辑模式：一般模式下，是不可以修改某一个字符的，只能到编辑模式了。
	从一般模式进入编辑模式，只需你按一个键即可（i,I,a,A,o,O,r,R）。当进入编辑模式时，
	会在屏幕的最下一行出现“INSERT或REPLACE”的字样。从编辑模式回到一般模式只需要按一下键盘左上方的ESC键即可。
	◆命令模式：在一般模式下，输入”:”或者”/”即可进入命令模式。
		在该模式下，你可以搜索某个字符或者字符串，也可以保存、替换、退出、显示行号等等。
	①.一般模式下移动光标
	h或向左方向键			光标向左移动一个字符	★
	j或者向下方向键			光标向下移动一个字符	★
	K或者向上方向键			光标向上移动一个字符	★
	l或者向右方向键			光标向右移动一个字符	★	
	Ctrl + f 或者pageUP键	屏幕向前移动一页		★
	Ctrl + b 或者pageDOWN键	屏幕向后移动一页		★
	Ctrl + d				屏幕向前移动半页
	Ctrl + u				屏幕向后移动半页
	+						光标移动到非空格符的下一列
	-						光标移动到非空格符的上一列
	n空格（n是数字）		按下数字n然后按空格，则光标向右移动n个字符，
							如果该行字符数小于n，则光标继续从下行开始向右移动，一直到n
	0（数字0）或者Shift+6	移动到本行行首			★
	Shift+4					即’$’移动到本行行尾		★
	H						光标移动到当前屏幕的最顶行
	M						光标移动到当前屏幕的中央那一行
	L						光标移动到当前屏幕的最底行
	G						光标移动到文本的最末行	★
	nG（n是数字）			移动到该文本的第n行		★
	gg						移动带该文本的首行		★
	n回车（n是数字）		光标向下移动n行
	②一般模式下查找与替换
	/word					向光标之后寻找一个字符串名为word的字符串，当找到第一个word后，按”n”继续搜后一个	★
	?word					想光标之前寻找一个字符串名为word的字符串，当找到第一个word后，按”n”继续搜前一个	★
	:n1,n2s/word1/word2/g	在n1和n2行间查找word1这个字符串并替换为word2，你也可以把”/”换成”#”	★
	:1,$s/word1/word2/g		从第一行到最末行，查找word1并替换成word2	★
	:1,$s/word1/word2/gc	加上c的作用是，在替换前需要用户确认
	③一般模式下删除、复制粘贴
	x,X						x为向后删除一个字符，X为向前删除一个字符	★
	nx（n为数字）			向后删除n个字符
	dd						删除光标所在的那一行						★
	ndd（n为数字）			删除光标所在的向下n行						★
	d1G						删除光标所在行到第一行的所有数据
	dG						删除光标所在行到末行的所有数据
	yy						复制光标所在的那行							★
	nyy						复制从光标所在行起向下n行					★
	p,P						p复制的数据从光标下一行粘贴，P则从光标上一行粘贴	★
	y1G						复制光标所在行到第一行的所有数据
	yG						复制光标所在行到末行的所有数据
	J						讲光标所在行与下一行的数据结合成同一行
	u						还原过去的操作
	④进入编辑模式
	i						在当前字符前插入字符	★
	I						在当前行行首插入字符	★
	a						在当前字符后插入字符	★
	A						在当前行行末插入字符	★
	o						在当前行下插入新的一行	★
	O						在当前行上插入新的一行	★
	r						替换光标所在的字符，只替换一次
	R						一直替换光标所在的字符，一直到按下ESC
	⑤命令模式
	:w						将编辑过的文本保存				★
	:w!						若文本属性为只读时，强制保存	★
	:q						退出vim							★
	:q!						不管编辑或未编辑都不保存退出	★
	:wq						保存，退出						★
	:e!						将文档还原成最原始状态
	ZZ						若文档没有改动，则不储存离开，若文档改动过，则储存后离开，等同于:wq
	:w [filename]			编辑后的文档另存为filename
	:r [filename]			在当前光标所在行的下面读入filename文档的内容
	:set nu					在每行的行首显示行号			★
	:set nonu				取消行号						★
	n1,n2 w [filename]		将n1到n2的内容另存为filename这个文档
	:! command				暂时离开vim运行某个linux命令，例如 :! ls /home 暂时列出/home

14、正则表达式：
	(1)、grep [-cinvABC] ‘word’ filename
		-c ：打印符合要求的行数
		-i ：忽略大小写
		-n ：在输出符合要求的行的同时连同行号一起输出
		-v ：打印不符合要求的行
		-A ：后跟一个数字（有无空格都可以），例如 –A2则表示打印符合要求的行以及下面两行
		-B ：后跟一个数字，例如 –B2 则表示打印符合要求的行以及上面两行
		-C ：后跟一个数字，例如 –C2 则表示打印符合要求的行以及上下各两行
		①过滤出带有某个关键词的行并输出行号
			grep -n 'root' /etc/passwd --> 1:root:x:0:0:root:/root:/bin/bash
		②过滤不带有某个关键词的行，并输出行号
			grep -vn 'nologin' /etc/passwd
				1:root:x:0:0:root:/root:/bin/bash
				2:daemon:x:1:1:daemon:/usr/sbin:/bin/sh
				3:bin:x:2:2:bin:/bin:/bin/sh
				4:sys:x:3:3:sys:/dev:/bin/sh
				......
		③过滤出所有包含数字的行	
			grep [0-9] test.txt
		#注意：果是数字的话就用[0-9]这样的形式，当然有时候也可以用这样的形式[15]即只含有1或者5，
		#注意，它不会认为是15。如果要过滤出数字以及大小写字母则要这样写[0-9a-zA-Z]。另外[ ]
		#还有一种形式，就是[^字符] 表示除[ ]内的字符之外的字符
		④筛选包含oo字符串，但是不包含r字符
			grep '[^r]oo' /etc/passwd
		⑤过滤出文档中以某个字符开头或者以某个字符结尾的行
			grep '^r' /etc/passwd --> 刷选以‘r’开头的行
			grep 'h$' /etc/passwd --> 刷选以‘h’开头的行
		#在正则表达式中，”^”表示行的开始，”$”表示行的结尾，那么空行则表示”^$”,
		#如果你只想筛选出非空行，则可以使用 “grep -v ‘^$’ filename”得到你想要的结果
		⑥输出不以英文字母开头的行
			grep '^[^a-zA-Z]' test.txt
		⑦过滤任意一个字符与重复字符
			grep 'r..o' /etc/passwd
			“.”表示任意一个字符，上例中，就是把符合r与o之间有两个任意字符的行过滤出来。
			“*”表示零个或多个前面的字符
		⑧指定要过滤字符出现的次数
			grep 'o\{2\}' /etc/passwd
		#这里用到了{ }，其内部为数字，表示前面的字符要重复的次数。上例中表示包含有两个o
		#即’oo’的行。注意，{ }左右都需要加上脱意字符’\’。另外，使用{ }我们还可以表示一个范围的，
		#具体格式是 ‘\{n1,n2\}’其中n1<n2，表示重复n1到n2次前面的字符，n2还可以为空，则表示大于等于n1次
		
	(2)egrep :grep的升级版
		①筛选一个或一个以上前面的字符
			egrep 'o+' test.txt
			egrep 'oo+' test.txt
			egrep 'ooo+' test.txt
		#和grep 不同的是，egrep这里是使用’+’的。
		②筛选零个或一个前面的字符
			egrep 'o?' test.txt
			egrep 'oo?' test.txt
		③筛选字符串1或者字符串2
			egrep '111|aaa' test.txt --中间有一个’|’表示或者的意思
		④egrep中’( )’的应用
			egrep 'r(oo)|(at)o' test.txt
			#用’( )’表示一个整体，例如(oo)+就表示1个’oo’或者多个’oo’
		
	(3)sed 工具的使用:实现把替换的文本输出到屏幕
		①打印某行 sed -n ‘n’p filename:单引号内的n是一个数字，表示第几行
			sed -n '2'p test.txt
		②打印多行 打印整个文档用 -n ‘1,$’p
			sed -n '2,4'p test.txt
			sed -n '1,$'p test.txt
		③打印包含某个字符串的行
			sed -n '/root/'p test.txt	目录下的文件，然后会提示按回车回到vim
			sed -n '/^1/'p test.txt
			sed -n '/in$/'p test.txt
		④-e 可以实现多个行为
			sed -e '1'p -e '/111/'p -n test.txt
				rot:x:0:0:/rot:/bin/bash
				111111111111111111111111111111
		⑤删除某行或者多行:
			‘d’ 这个字符就是删除的动作了，不仅可以删除指定的单行以及多行，
			而且还可以删除匹配某个字符的行，另外还可以删除从某一行一直到文档末行。
			sed '1'd test.txt
		⑥替换字符或字符串			
			sed '1,2s/ot/to/g' test.txt
			#’s’就是替换的命令，’g’为本行中全局替换，如果不加’g’，只换该行中出现的第一个;
			#除了可以使用’/’外，还可以使用其他特殊字符例如’#’或者’@’都没有问题。
			思考:删除文档中的所有数字或者字母？
				sed 's/[0-9]//g' test.txt
		⑦调换两个字符串的位置
			sed 's/\(rot\)\(.*\)\(bash\)/\3\2\1/' test.txt
			除了调换两个字符串的位置外，还常常用到在某一行前或者后增加指定内容
			sed 's/^.*$/123&/' test.txt #每一行前面加上123
			sed 's/^.*$/&123/' test.txt #每一行最后加上123
		⑧直接修改文件的内容
			sed -i 's/:/#/g' test.txt
	(4).awk工具的使用
		①截取文档中的某个段
			head -n2 test.txt | awk -F':' '{print $1}'
			-F 选项的作用是指定分隔符，如果不加-F指定，则以空格或者tab为分隔符
			Print为打印的动作，用来打印出某个字段。$1为第一个字段，$2为第二个字段，
			依次类推，有一个特殊的那就是$0，它表示整行
			★注意:
			注意awk的格式，-F后紧跟单引号，然后里面为分隔符，print的动作要用’{ }’括起来，否则会报错。
			print还可以打印自定义的内容，但是自定义的内容要用双引号括起来
		②匹配字符或字符串
15、Linux日常管理
	(1).监控系统的状态:
		① w 查看当前系统的负载
			 18:04:09 up  3:25,  2 users,  load average: 0.00, 0.01, 0.05
			USER     TTY      FROM             LOGIN@   IDLE   JCPU   PCPU WHAT
			chenlanq tty7     :0               14:40    3:24m  2:24   0.70s gnome-session --
			chenlanq pts/0    192.168.56.2     17:55    0.00s  0.19s  0.01s w
			关注:load average的三个值
				这个值的意义是，单位时间段内CPU活动进程数。当然这个值越大就说明你的服务器压力越大;
				一般情况下这个值只要不超过你服务器的cpu数量就没有关系
			★问:如何查看服务器有几个cpu？
				cat /proc/cpuinfo
				查看当前系统有几个CPU: grep -c 'processor' /proc/cpuinfo
		② vmstat 监控系统的状态:可以查看系统具体的负载状态;
			◆vmstat 1 5 表示每隔1秒钟打印一次系统状态，连续打印5次
			◆vmstat 1 表示每隔1秒钟打印一次系统状态，一直打印
			procs -----------memory---------- ---swap-- -----io---- -system-- ----cpu----
			 r  b   swpd   free   buff  cache   si   so    bi    bo   in   cs us sy id wa
			 0  0   2000  68688  76424 356784    0    0    47     3  109  412  2  1 97  1
			Ⅰ.procs 显示进程相关信息
				r ：表示运行和等待cpu时间片的进程数，如果长期大于服务器cpu的个数，则说明cpu不够用了；
				b ：表示等待资源的进程数，比如等待I/O, 内存等，这列的值如果长时间大于1，则需要你关注一下了；
			Ⅱ.memory 内存相关信息
				swpd ：表示切换到交换分区中的内存数量 ；
				free ：当前空闲的内存数量；
				buff ：缓冲大小，（即将写入磁盘的）；
				cache ：缓存大小，（从磁盘中读取的）；	
			Ⅲ.swap 内存交换情况
				si ：由内存进入交换区的数量；
				so ：由交换区进入内存的数量；
			Ⅳ.io 磁盘使用情况
				bi ：从块设备读取数据的量（读磁盘）；
				bo： 从块设备写入数据的量（写磁盘）；
			Ⅴ.system 显示采集间隔内发生的中断次数
				in ：表示在某一时间间隔中观测到的每秒设备中断数；
				cs ：表示每秒产生的上下文切换次数；
			Ⅵ.CPU 显示cpu的使用状态
				us ：显示了用户下所花费 cpu 时间的百分比；
				sy ：显示系统花费cpu时间百分比；
				id ：表示cpu处于空闲状态的时间百分比；
				wa ：表示I/O等待所占用cpu时间百分比；
				st ：表示被偷走的cpu所占百分比（一般都为0，不用关注）；				
			★以上所介绍的各个参数中，常常关注r列，b列，和wa列，三列代表的含义在上边说得已经很清楚。
			IO部分的bi以及bo也是经常参考的对象。如果磁盘io压力很大时，这两列的数值会比较高。
			另外当si, so两列的数值比较高，并且在不断变化时，说明内存不够了，
			内存中的数据频繁交换到交换分区中，这往往对系统性能影响极大			
		③ top 显示进程所占系统资源:用于动态监控进程所占系统资源，每隔3秒变一次;
			★top命令时还常常使用-bn1 这个组合选项，它表示非动态打印系统资源使用情况，可以用在脚本中
		④ sar 监控系统状态		
			可以监控系统所有资源状态，比如平均负载、网卡流量、磁盘状态、内存使用等等	
			Ⅰ.查看网卡流量:sar -n DEV
			   实时查看网卡流量:sar -n DEV 1 5
			   查看某一天的网卡流量历史，使用-f选项，后面跟文件名:sar -n DEV -f filename
16.iptables 规则:
	16.1.查看 iptables 规格:
		[root@localhost opt]# iptables -L
		Chain INPUT (policy ACCEPT)
		target     prot opt source               destination         
		Chain FORWARD (policy ACCEPT)
		target     prot opt source               destination         
		Chain OUTPUT (policy ACCEPT)
		target     prot opt source               destination  				
	16.2.关闭 iptables 规则:
		iptables -F		
	
				
				
				
				
				
				
				
				
				
				
				
三、Shell脚本							
1、Shell一些简单命令
	(1).记录历史命令：
		与命令历史有关的有一个有意思的字符那就是”!”了。常用的有这么几个应用：
		①!! (连续两个”!”)，表示执行上一条指令；
		②!n (这里的n是数字)，表示执行命令历史中第n条指令，例如”!100”表示执行命令历史中第100个命令；
		③!字符串 (字符串大于等于1)，例如!ta，表示执行命令历史中最近一次以ta为开头的指令。
	(2).别名:alias
		把一个常用的并且很长的指令别名一个简洁易记的指令。如果不想用了，还可以用unalias解除别名功能。
		直接敲alias会看到目前系统预设的alias ：
		alias [命令别名]=[’具体的命令’]。
	(3).通配符:在bash下，可以使用*来匹配零个或多个字符，而用?匹配一个字符;
	(4).输入输出从定向:
		输入重定向用于改变命令的输入，输出重定向用于改变命令的输出。
		输出重定向更为常用，它经常用于将命令的结果输入到文件中，而不是屏幕上。
		输入重定向的命令是<，输出重定向的命令是>，另外还有错误重定向2>，以及追加重定向>>;
	(5).管道符:”|”，就是把前面的命令运行的结果丢给后面的命令;
	(6).作业控制:当运行一个进程时，你可以使它暂停（按Ctrl+z），然后使用fg命令恢复它，
		利用bg命令使他到后台运行，你也可以使它终止（按Ctrl+c）	
	
2、变量:shell预设的变量都是大写
	(1).linux下设置自定义变量有哪些规则呢？
		a. 设定变量的格式为”a=b”，其中a为变量名，b为变量的内容，等号两边不能有空格；
		b. 变量名只能由英、数字以及下划线组成，而且不能以数字开头；		
		c. 当变量内容带有特殊字符（如空格）时，需要加上单引号；
			myname='Qing Chen'
			#有一种情况，需要你注意，就是变量内容中本身带有单引号，这就需要用到双引号了。
			myname="Qing's"
		d. 如果变量内容中需要用到其他命令运行结果则可以使用反引号；
			myname=`pwd`
		e. 变量内容可以累加其他变量的内容，需要加双引号，如果是单引号，得到意想不到的效果
			myname="$LOGNAME"Qing
			
		①、使用变量：使用一个定义过的变量，只要在变量名前面加美元符号($)即可
			your_name="tom"
			echo $your_name
			echo ${your_name}
			#变量可以重新定义
	(2).env，列出系统预设的全部环境变量
	   set，列出系统的全部变量(包括系统预设与自定义，自定义变量只在shell中生效)
	   which 用来查找一个命令的绝对路径,which只能用来查找PATH环境变量中出现的路径下的可执行文件
		①要想系统内所有用户登录后都能使用该变量
			需要在/etc/profile文件最末行加入 “export myname=Aming” 然后运行”source /etc/profile”就可以生效了。
			此时你再运行bash命令或者直接su - test账户看看。
		②只想让当前用户使用该变量:
			需要在用户主目录下的.bashrc文件最后一行加入“export myname=Aming” 然后运行”source .bashrc”就可以生效了。
			这时候再登录test账户，myname变量则不会生效了。上面用的source命令的作用是，
			将目前设定的配置刷新，即不用注销再登录也能生效
	(3).source /etc/profile，刷新设定的配置，即不用注销再登录也能生效
	(4).pstree，打印linux系统中所有进程通过树形结构		pstree |grep bash		
	(5).export，声明一下这个变量	unset 变量名：取消某个变量
	(6).系统环境变量与个人环境变量的配置文件
		那么在linux系统中，这些变量被存到了哪里呢，为什么用户一登陆shell就自动有了这些变量呢？
		①./etc/profile:文件预设了几个重要的变量.如PATH, USER, LOGNAME,MAIL,INPUTRC, HOSTNAME, HISTSIZE,umas等。
		②./etc/bashrc: 这个文件主要预设umask以及PS1。这个PS1就是我们在敲命令时，前面那串字符了，
		例如linux系统PS1就是 [hadoop@vdlbnconsulting01 ~] ，你不妨看一下PS1的值。
			echo $PS1
			[\u@\h \W]\$
			#：\u就是用户，\h 主机名， \W 则是当前目录，\$就是那个’#’了，如果是普通用户则显示为’$’
		除了两个系统级别的配置文件外，每个用户的主目录下还有几个这样的隐藏文件：
		.bash_profile ：定义了用户的个人化路径与环境变量的文件名称。每个用户都可使用该文件输入专
			用于自己使用的shell信息,当用户登录时,该文件仅仅执行一次。
		.bashrc ：该文件包含专用于你的shell的bash信息,当登录时以及每次打开新的shell时,该该文件被读取。
			例如你可以将用户自定义的alias或者自定义变量写到这个文件中。
		.bash_history ：记录命令历史用的。
		.bash_logout ：当退出shell时，会执行该文件。可以把一些清理的工作放到这个文件中	
		
3、linux shell中的特殊符号
	(1).* ：代表零个或多个字符或数字
		? ：只代表一个任意的字符
		# ：这个符号在linux中表示注释说明的意思，即”#”后面的内容linux忽略掉;
		\ ：脱意字符，将后面的特殊符号（例如”*” ）还原为普通字符;
		$ ：除了用于变量前面的标识符外，还有一个妙用，就是和’!’结合起来使用
	(2).shell中$0,$?,$!等的特殊用法
		$$：Shell本身的PID（ProcessID）
		$!：Shell最后运行的后台Process的PID
		$?：最后运行的命令的结束代码（返回值）
		$-：使用Set命令设定的Flag一览
		$*：所有参数列表。如"$*"用「 」括起来的情况、以 $1 $2 … $n 的形式输出所有参数
		$@：所有参数列表。如"$@"用「 」括起来的情况、以"$1" "$2" … "$n" 的形式输出所有参数。
		$#：加到Shell的参数个数
		$0：Shell本身的文件名
		$1～$n：添加到Shell的各参数值。$1是第1参数、$2是第2参数…
		#案例：先写一个简单的脚本，执行以后再解释各个变量的意义
			# touch variable
			# vi variable
		脚本内容如下：
			#!/bin/sh
			echo "number:$#"
			echo "scname:$0"
			echo "first :$1"
			echo "second:$2"
			echo "argume:$@"
		保存退出
		赋予脚本执行权限
			chmod +x variable
		执行脚本
			./variable aa bb
		结果输出
			number:2
			scname:./variable
			first: aa
			second:bb
			argume:aa bb
		通过显示结果可以看到：
			$#：是传给脚本的参数个数
			$0：是脚本本身的名字
			$1：是传递给该shell脚本的第一个参数
			$2：是传递给该shell脚本的第二个参数
			$@：是传给脚本的所有参数的列表

4、一些命令:
	(1).cut:截取某一个字段
		语法：cut -d “分隔字符” [-cf] n 这里的n是数字
		-d ：后面跟分隔字符，分隔字符要用双引号括起来
		-c ：后面接的是第几个字符,后面可以是1个数字n，也可以是一个区间n1-n2，还可以是多个数字n1,n2,n3
		-f ：后面接的是第几个区块
	(2).sort ：用做排序
		语法：sort [-t 分隔符] [-kn1,n2] [-nru] 这里的n1 < n2
		-t 分隔符 ：作用跟cut的-d一个意思
		-n ：使用纯数字排序
		-r ：反向排序
		-u ：去重复
		-kn1,n2 ：由n1区间排序到n2区间，可以只写-kn1，即对n1字段排序
	(3).wc ：统计文档的行数、字符数、词数，常用的选项为:
		-l ：统计行数
		-m ：统计字符数
		-w ：统计词数
	(4). uniq ：去重复的行，常用的选项有：
		-c ：统计重复的行数，并把行数写在前面
		#有一点需要注意，在进行uniq之前，需要先用sort排序然后才能uniq，否则你将得不到你想要的
	(5).tee ：后跟文件名，类似与重定向”>”，但是比重定向多了一个功能，
		在把文件写入后面所跟的文件中的同时，还显示在屏幕上
	(6).tr ：替换字符，常用来处理文档中出现的特殊符号，如DOS文档中出现的^M符号。常用的选项有两个：
		-d ：删除某个字符，-d 后面跟要删除的字符
		-s ：把重复的字符去掉
		最常用的就是把小写变大写： tr '[a-z]' '[A-Z]'
	(7).split ：切割文档，常用选项：
		-b ：依据大小来分割文档，单位为byte
		★ split -b 500 /etc/passwd passwd
		格式如上例，后面的passwd为分割后文件名的前缀，分割后的文件名为passwdaa, passwdab, passwdac …		 
		-l ：依据行数来分割文档
		★ split -l 10 /etc/passwd passwd
	(8).& ：如果想把一条命令放到后台执行的话，则需要加上这个符号。通常用于命令运行时间非常长的情况
		①使用 jobs 可以查看当前shell中后台执行的任务。用 fg 可以调到前台执行;
		②如果是多任务情况下，想要把任务调到前台执行的话，fg后面跟任务号，任务号可以使用jobs命令得到;
	(9). >, >>, 2>, 2>> ：前面讲过重定向符号> 以及>>　分别表示取代和追加的意思，
		然后还有两个符号就是这里的2> 和 2>>　分别表示错误重定向和错误追加重定向，当我们运行一个命令报错时，
		报错信息会输出到当前的屏幕，如果想重定向到一个文本里，则要用2>或者2>>。	
	
14、在判断数值大小除了可以用”(( ))”的形式外，还可以使用”[ ]”。但是就不能使用>, < , = 这样的符号了，
	要使用 -lt （小于），-gt （大于），-le （小于等于），-ge （大于等于），-eq （等于），-ne （不等于）。
	test

17、shell与if相关参数
	[ -a FILE ]	如果 FILE 存在则为真。
	[ -b FILE ]	如果 FILE 存在且是一个块特殊文件则为真。
	[ -c FILE ]	如果 FILE 存在且是一个字特殊文件则为真。
	[ -d FILE ]	如果 FILE 存在且是一个目录则为真。判断制定的是否为目录
	[ -e FILE ]	如果 FILE 存在则为真。
	[ -f FILE ]	如果 FILE 存在且是一个普通文件则为真。判断制定的是否为文件
	[ -g FILE ]	如果 FILE 存在且已经设置了SGID则为真。
	[ -h FILE ]	如果 FILE 存在且是一个符号连接则为真。
	[ -k FILE ]	如果 FILE 存在且已经设置了粘制位则为真。
	[ -p FILE ]	如果 FILE 存在且是一个名字管道(F如果O)则为真。
	[ -r FILE ]	如果 FILE 存在且是可读的则为真。判断制定的是否可读
	[ -s FILE ]	如果 FILE 存在且大小不为0则为真。判断存在的对象长度是否为0
	[ -t FD ]	如果文件描述符 FD 打开且指向一个终端则为真。
	[ -u FILE ]	如果 FILE 存在且设置了SUID (set user ID)则为真。
	[ -w FILE ]	如果 FILE 如果 FILE 存在且是可写的则为真。判断制定的是否可写
	[ -x FILE ]	如果 FILE 存在且是可执行的则为真。判断存在的对象是否可以执行
	[ -O FILE ]	如果 FILE 存在且属有效用户ID则为真。
	[ -G FILE ]	如果 FILE 存在且属有效用户组则为真。
	[ -L FILE ]	如果 FILE 存在且是一个符号连接则为真。判断制定的是否为符号链接
	[ -N FILE ]	如果 FILE 存在 and has been mod如果ied since it was last read则为真。
	[ -S FILE ]	如果 FILE 存在且是一个套接字则为真。
	[ FILE1 -nt FILE2 ]	如果 FILE1 has been changed more recently than FILE2, 
		or 如果 FILE1 exists and FILE2 does not则为真。
	[ FILE1 -ot FILE2 ]	如果 FILE1 比 FILE2 要老, 或者 FILE2 存在且 FILE1 不存在则为真。
	[ FILE1 -ef FILE2 ]	如果 FILE1 和 FILE2 指向相同的设备和节点号则为真。
	[ -o OPTIONNAME ]	如果 shell选项 “OPTIONNAME” 开启则为真。
	[ -z STRING ]	“STRING” 的长度为零则为真。判断制定的变量是否存在值
	[ -n STRING ] or [ STRING ]	“STRING” 的长度为非零 non-zero则为真。
	[ STRING1 == STRING2 ]	如果2个字符串相同。 “=” may be used instead of “==” for strict POSIX compliance则为真。
	[ STRING1 != STRING2 ]	如果字符串不相等则为真。
	[ STRING1 < STRING2 ]	如果 “STRING1” sorts before “STRING2” lexicographically in the current locale则为真。
	[ STRING1 > STRING2 ]	如果 “STRING1” sorts after “STRING2” lexicographically in the current locale则为真。
	[ ARG1 OP ARG2 ]	“OP” is one of -eq, -ne, -lt, -le, -gt or -ge. These arithmetic binary operators 
		return true if “ARG1” is equal to, not equal to, less than, less than or equal to, greater than, 
		or greater than or equal to “ARG2”, respectively. “ARG1” and “ARG2” are integers.

18、>/dev/null 2>&1 详解
	分解这个组合：“>/dev/null 2>&1” 为五部分。
	(1)：> 代表重定向到哪里，例如：echo "123" > /home/123.txt
	(2)：/dev/null 代表空设备文件
	(3)：2> 表示stderr标准错误
	(4)：& 表示等同于的意思，2>&1，表示2的输出重定向等同于1
	(5)：1 表示stdout标准输出，系统默认值是1，所以">/dev/null"等同于 "1>/dev/null"


19、Shell脚本解释器：bash，sh，ash，csh，ksh
	(1):bash是Linux系统默认使用的shell
	(2):sh 由Steve Bourne开发，是Bourne Shell的缩写，各种UNIX系统都配有sh
	(3):ash shell 是由Kenneth Almquist编写的，Linux中占用系统资源最少的一个小shell，
		它只包含24个内部命令，因而使用起来很不方便;
	(4):csh 是Linux比较大的内核，它由以William Joy为代表的共计47位作者编成，共有52个内部命令。
		该shell其实是指向/bin/tcsh这样的一个shell，也就是说，csh其实就是tcsh
	(5):ksh 是Korn shell的缩写，由Eric Gisin编写，共有42条内部命令。该shell最大的优点是几乎和商业发行版的ksh完全兼容，
		这样就可以在不用花钱购买商业版本的情况下尝试商业版本的性能了

20、编译型语言和解释型语言。
	(1):编译型语言
		很多传统的程序设计语言，例如Fortran、Ada、Pascal、C、C++和Java，都是编译型语言。
		这类语言需要预先将我们写好的源代码(source code)转换成目标代码(object code)，这个过程被称作“编译”。
		运行程序时，直接读取目标代码(object code)。由于编译后的目标代码(object code)非常接近计算机底层，
		因此执行效率很高，这是编译型语言的优点。
		但是，由于编译型语言多半运作于底层，所处理的是字节、整数、浮点数或是其他机器层级的对象，
		往往实现一个简单的功能需要大量复杂的代码。例如，在C++里，就很难进行“将一个目录里所有的文件复制到
		另一个目录中”之类的简单操作。
	(2):解释型语言
		解释型语言也被称作“脚本语言”。执行这类程序时，解释器(interpreter)需要读取我们编写的源代码(source code)，
		并将其转换成目标代码(object code)，再由计算机运行。因为每次执行程序都多了编译的过程，因此效率有所下降。
		使用脚本编程语言的好处是，它们多半运行在比编译型语言还高的层级，能够轻易处理文件与目录之类的对象；
		缺点是它们的效率通常不如编译型语言。不过权衡之下，通常使用脚本编程还是值得的：花一个小时写成的简单脚本，
		同样的功能用C或C++来编写实现，可能需要两天，而且一般来说，脚本执行的速度已经够快了，
		快到足以让人忽略它性能上的问题。脚本编程语言的例子有awk、Perl、Python、Ruby与Shell。

21、shell脚本：
	(1)#!/bin/bash	---表示脚本需要什么解释器来执行，即使用哪一种Shell
	chmod +x ./test.sh  #使脚本具有执行权限
	./test.sh  #执行脚本
	#注意，一定要写成./test.sh，而不是test.sh。
	运行其它二进制的程序也一样，直接写test.sh，linux系统会去PATH里寻找有没有叫test.sh的，
	而只有/bin, /sbin, /usr/bin，/usr/sbin等在PATH里，你的当前目录通常不在PATH里，
	所以写成test.sh是会找不到命令的，要用./test.sh告诉系统说，就在当前目录找
	(2)如果在开发过程中，遇到大段的代码需要临时注释起来，过一会儿又取消注释，怎么办呢？每一行加个#符号
	太费力了，可以把这一段要注释的代码用一对花括号括起来，定义成一个函数，没有地方调用这个函数，这块代
	码就不会执行，达到了和注释一样的效果。

22、字符串
	①单引号
		str='this is a string'
		#单引号字符串的限制：
		单引号里的任何字符都会原样输出，单引号字符串中的变量是无效的；
		单引号字串中不能出现单引号（对单引号使用转义符后也不行）。
	②双引号
		your_name='qinjx'
		str="Hello, I know your are \"$your_name\"! \n"
		#双引号的优点：
		双引号里可以有变量
		双引号里可以出现转义字符
	(1)拼接字符串
		your_name="qinjx"
		greeting="hello, "$your_name" !"
		greeting_1="hello, ${your_name} !"
		echo $greeting $greeting_1
	(2)获取字符串长度
		string="abcd"
		echo ${#string} #输出 4
	(3)提取子字符串
		string="alibaba is a great company"
		echo ${string:1:4} #输出liba
	(4)查找子字符串
		string="alibaba is a great company"
		echo `expr index "$string" is`


23、Shell数组：
	bash支持一维数组（不支持多维数组），并且没有限定数组的大小。类似与C语言，数组元素的下标由0开始编号。
	获取数组中的元素要利用下标，下标可以是整数或算术表达式，其值应大于或等于0
	(1)、定义数组：Shell中，用括号来表示数组，数组元素用“空格”符号分割开。定义数组的一般形式为：
		数组名=(值1 值2 ... 值n)
		E.G. array_name=(value0 value1 value2 value3)
		也可如此：
		array_name[0]=value0
		array_name[1]=value1
		array_name[n]=valuen
	(2)、读取数组：读取数组元素值的一般格式是：
		${数组名[下标]}
		E.G. valuen=${array_name[n]}
		#使用@符号可以获取数组中的所有元素，例如：
		echo ${array_name[@]}
	(3)、获取数组的长度
		# 取得数组元素的个数
		length=${#array_name[@]}
		# 或者
		length=${#array_name[*]}
		# 取得数组单个元素的长度
		lengthn=${#array_name[n]}


24、case语句为多选择语句。可以用case语句匹配一个值与一个模式，如果匹配成功，执行相匹配的命令。case语句格式如下：
	case 值 in
		模式1)
			command1
			command2
			...
			commandN
			;;
		模式2）
			command1
			command2
			...
			commandN
			;;
	esac
	














































































































































































































































   
		
		