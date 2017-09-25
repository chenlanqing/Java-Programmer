1.版本控制:是一种记录一个或若干文件内容变化,以便将来查阅特定版本修订情况的系统
	1.1.本地版本控制系统
	1.2.集中化的版本控制系统:一个单一的集中管理的服务器,保存所有文件的修订版本
	1.3.分布式版本控制系统:客户端并不只提取最新版本的文件快照,而是把代码仓库完整地镜像下来
2.Git 简介:
	2.1.特点:
		速度
		简单的设计
		对非线性开发模式的强力支持（允许上千个并行开发的分支）
		完全分布式
		有能力高效管理类似 Linux 内核一样的超大规模项目（速度和数据量）
	2.2.与其他版本控制系统差别:
		(1).Git 只关心文件数据的整体是否发生变化，而大多数其他系统则只关心文件内容的具体差异
		(2).Git 更像是把变化的文件作快照后，记录在一个微型的文件系统中
		(3).在 Git 中的绝大多数操作都只需要访问本地文件和资源，不用连网
		(4).在保存到 Git 之前，所有数据都要进行内容的校验和(checksum)计算,并将此结果作为数据的唯一标识和索引,
			Git 使用 SHA-1 算法计算数据的校验和,通过对文件的内容或目录的结构计算出一个 SHA-1 哈希值，作为指纹字符串
			该字串由 40 个十六进制字符（0-9 及 a-f）组成
		(5).多数操作仅添加数据,一旦提交快照之后就完全不用担心丢失数据
	2.3.文件的三种状态:
		(1).对于任何一个文件,在 Git 内都只有三种状态:已提交(committed)、已修改(modified)和已暂存(staged)
		(2).已提交表示该文件已经被安全地保存在本地数据库中了;
			已修改表示修改了某个文件,但还没有提交保存;
			已暂存表示把已修改的文件放在下次提交时要保存的清单中
		(3).文件流转的三个工作区域:Git 的工作目录、暂存区域以及本地仓库
	2.4.Git 基本工作流程:
		(1).在工作目录中修改某些文件;
		(2).对修改后的文件进行快照,然后保存到暂存区域;
		(3).提交更新,将保存在暂存区域的文件快照永久转储到 Git 目录中;
	2.5.CentOS 下安装 Git
		(1).安装依赖文件:yum install curl-devel expat-devel gettext-devel openssl-devel zlib-devel
		(2).从下面的 Git 官方站点下载最新版本源代码
		(3).编译源码:
			make prefix=/usr/local all
			sudo make prefix=/usr/local install
		直接安装:yum install git-core
	2.6.配置:
		(1).用户信息:
			git config --global user.name chenqing323@hotmail.com
			git config --global user.email chenqing323@hotmail.com
		(2).文本编辑器
			git config --global core.editor vim
		(3).差异分析工具:
			git config --global merge.tool vimdiff
		(4).查看配置信息:
			git config --list
		(5).获取帮助:
			git help <verb>
			git <verb> --help
			man git-<verb>
3.Git 基础:
	3.1.获取项目的 Git 仓库:
		3.1.1.在工作目录中初始化新仓库
			(1).要对现有的某个项目开始用 Git 管理，只需到此项目所在的目录，执行：
				$ git init
			(2).如果当前目录下有几个文件想要纳入版本控制，需要先用 git add 命令告诉 Git 开始对这些文件进行跟踪，然后提交：
				$ git add *.c
			    $ git add README
			    $ git commit -m 'initial project version'
		3.1.2.从现有仓库克隆,克隆仓库的命令格式为 git clone [url]
			$ git clone git://github.com/schacon/grit.git
			如果希望在克隆的时候，自己定义要新建的项目目录名称，可以在上面的命令末尾指定新的名字：
			$ git clone git://github.com/schacon/grit.git mygit
			注意:Git 支持许多数据传输协议
	3.2.记录每次更新到仓库
		作目录下面的所有文件都不外乎这两种状态：已跟踪或未跟踪
		已跟踪的文件是指本来就被纳入版本控制管理的文件，在上次快照中有它们的记录，工作一段时间后，它们的状态可能是未更新.
		已修改或者已放入暂存区。而所有其他文件都属于未跟踪文件
		(1).检查当前文件状态:git status
		(2).跟踪新文件:git add
			只要在 "Changes to be committed" 这行下面的，就说明是已暂存状态
		(3).暂存已修改文件
		3.2.1.忽略某些文件:创建一个名为 .gitignore 的文件，列出要忽略的文件模式,如:
			$ cat .gitignore
			    *.[oa] // 告诉 Git 忽略所有以 .o 或 .a 结尾的文件
			    *~ // 第二行告诉 Git 忽略所有以波浪符（~）结尾的文件
			(1).文件 .gitignore 的格式规范如下：
				所有空行或者以注释符号 ＃ 开头的行都会被 Git 忽略。
				可以使用标准的 glob 模式匹配。
				匹配模式最后跟反斜杠（/）说明要忽略的是目录。
				要忽略指定模式以外的文件或目录，可以在模式前加上惊叹号（!）取反。
			# 此为注释 – 将被 Git 忽略
		    # 忽略所有 .a 结尾的文件
		    *.a
		    # 但 lib.a 除外
		    !lib.a
		    # 仅仅忽略项目根目录下的 TODO 文件，不包括 subdir/TODO
		    /TODO
		    # 忽略 build/ 目录下的所有文件
		    build/
		    # 会忽略 doc/notes.txt 但不包括 doc/server/arch.txt
		    doc/*.txt    																		*/
		3.2.2.查看已暂存和未暂存的更新,要查看具体修改了什么地方,可以用 git diff 命令
			(1).要查看尚未暂存的文件更新了哪些部分,不加参数直接输入 git diff
				$ git diff // 不是这次工作和上次提交之间的差异
			(2).若要看已经暂存起来的文件和上次提交时的快照之间的差异
				(Git 1.6.1 及更高版本还允许使用 git diff --staged，效果是相同的，但更好记些)
				$ git diff --cached
		3.2.3.提交更新:
			(1).每次准备提交前,先用 git status 看下,是不是都已暂存起来了,然后再运行提交命令 git commit
				可以用 -m 参数后跟提交说明的方式，在一行命令中提交更新
				$ git commit -m "Story 182: Fix benchmarks for speed"
			(2).提交时记录的是放在暂存区域的快照,任何还未暂存的仍然保持已修改状态,可以在下次提交时纳入版本管理
			(3).Git 提供了一个跳过使用暂存区域的方式，只要在提交的时候，给 git commit 加上 -a 选项，
				Git 就会自动把所有已经跟踪过的文件暂存起来一并提交
		3.2.4.移除文件:
			(1).要从 Git 中移除某个文件,就必须要从已跟踪文件清单中移除(确切地说，是从暂存区域移除)然后提交:
				使用 git rm 命令完成此项工作，并连带从工作目录中删除指定的文件
			(2).如果只是简单地从工作目录中手工删除文件,运行 git status 时就会在 “Changes not staged for commit” 部分
				然后再运行 git rm 记录此次移除文件的操作;
				$ rm test.txt
				$ git rm test.txt
			(3).最后提交的时候，该文件就不再纳入版本管理了。如果删除之前修改过并且已经放到暂存区域的话，
				则必须要用强制删除选项 -f,以防误删除文件后丢失修改的内容;
			(4).想把文件从 Git 仓库中删除(亦即从暂存区域移除)，但仍然希望保留在当前工作目录中,仅是从跟踪清单中删除:
				$ git rm --cached readme.txt
				后面可以列出文件或者目录的名字，也可以使用 glob 模式
		3.2.5.移动文件:
			(1).要在 Git 中对文件改名，可以这么做:
				$ git mv file_from file_to
	3.3.查看提交历史:git log
		3.3.1.git log 选项:
			(1).-p 选项展开显示每次提交的内容差异;
			(2).-2 则仅显示最近的两次更新;
			(3).-stat 仅显示简要的增改行数统计;每个提交都列出了修改过的文件，以及其中添加和移除的行数，并在最后列出所有增减行数小计
			(4).--pretty 可以指定使用完全不同于默认格式的方式展示提交历史
				$ git log --pretty=oneline
				①.oneline 将每个提交放在一行显示，这在提交数很大时非常有用
				②.short,full 和 fuller 可以用，展示的信息或多或少有些不同
				③.format,可以定制要显示的记录格式，这样的输出便于后期编程提取分析
					$ git log --pretty=format:"%h - %an, %ar : %s"
					ca82a6d - Scott Chacon, 11 months ago : changed the version number
				    085bb3b - Scott Chacon, 11 months ago : removed unnecessary test code
				    a11bef0 - Scott Chacon, 11 months ago : first commit
				    ==>选项 说明
				        %H 提交对象（commit）的完整哈希字串
				        %h 提交对象的简短哈希字串
				        %T 树对象（tree）的完整哈希字串
				        %t 树对象的简短哈希字串
				        %P 父对象（parent）的完整哈希字串
				        %p 父对象的简短哈希字串
				        %an 作者（author）的名字
				        %ae 作者的电子邮件地址
				        %ad 作者修订日期（可以用 -date= 选项定制格式）
				        %ar 作者修订日期，按多久以前的方式显示
				        %cn 提交者(committer)的名字
				        %ce 提交者的电子邮件地址
				        %cd 提交日期
				        %cr 提交日期，按多久以前的方式显示
				        %s 提交说明
			(5).用 oneline 或 format 时结合 --graph 选项，可以看到开头多出一些 ASCII 字符串表示的简单图形
				$ git log --pretty=format:"%h %s" --graph
			(6).选项 说明
			    -p 按补丁格式显示每个更新之间的差异。
			    --stat 显示每次更新的文件修改统计信息。
			    --shortstat 只显示 --stat 中最后的行数修改添加移除统计。
			    --name-only 仅在提交信息后显示已修改的文件清单。
			    --name-status 显示新增、修改、删除的文件清单。
			    --abbrev-commit 仅显示 SHA-1 的前几个字符，而非所有的 40 个字符。
			    --relative-date 使用较短的相对时间显示（比如，“2 weeks ago”）。
			    --graph 显示 ASCII 图形表示的分支合并历史。
			    --pretty 使用其他格式显示历史提交信息。可用的选项包括 oneline，short，full，fuller 和 format（后跟指定格式)
		3.3.2.限制输出长度
			(1).Git 在输出所有提交时会自动调用分页程序（less），要看更早的更新只需翻到下页即可
			(2).按照时间作限制的选项，比如 --since 和 --until.下面的命令列出所有最近两周内的提交:
				$ git log --since=2.weeks
			(3).给出若干搜索条件,列出符合的提交.用 --author 选项显示指定作者的提交,用 --grep 选项搜索提交说明中的关键字
				注意:要得到同时满足这两个选项搜索条件的提交,就必须用--all-match 选项.否则,满足任意一个条件的提交都会被匹配出来
			(4).如果只关心某些文件或者目录的历史提交，可以在 git log 选项的最后指定它们的路径,路径(path)
			(5).选项 说明
				    -(n) 仅显示最近的 n 条提交
				    --since, --after 仅显示指定时间之后的提交。
				    --until, --before 仅显示指定时间之前的提交。
				    --author 仅显示指定作者相关的提交。
				    --committer 仅显示指定提交者相关的提交。
			(6).例子:
				实际的例子，如果要查看 Git 仓库中，2008 年 10 月期间，Junio Hamano 
				提交的但未合并的测试脚本(位于项目的 t/ 目录下的文件)
		3.3.3.使用图形化工具查阅提交历史				
	3.4.撤销操作:
		3.4.1.修改最后一次提交:
			提交完了才发现漏掉了几个文件没有加,或者提交信息写错了.想要撤消刚才的提交操作,可以使用 --amend 选项重新提交	
				$ git commit --amend
			如果刚才提交时忘了暂存某些修改，可以先补上暂存操作，然后再运行 --amend 提交
				$ git commit -m 'initial commit'
			    $ git add forgotten_file
			    $ git commit --amend // 第二个提交命令修正了第一个的提交内容
		3.4.2.取消已经暂存的文件:git reset HEAD <file>... 的方式取消暂存
			git status 命令的输出中有相应的提示信息
		3.4.3.取消对文件的修改:git checkout -- <file>
			注意:在用这条命令前，请务必确定真的不再需要保留刚才的修改
	3.5.远程仓库的使用:远程仓库是指托管在网络上的项目仓库
		3.5.1.查看当前的远程库:
			git remote 命令，它会列出每个远程库的简短名字
			也可以加上 -v 选项,显示对应的克隆地址：
		3.5.2.添加远程仓库
			要添加一个新的远程仓库,可以指定一个简单的名字,以便将来引用,
			运行 git remote add [shortname] [url]
		3.5.3.从远程仓库抓取数据:
			$ git fetch [remote-name] 命令会到远程仓库中拉取所有你本地仓库中还没有的数据
			注意:fetch 命令只是将远端的数据拉到本地仓库，并不自动合并到当前工作分支，只有当你确实准备好了，才能手工合并
		3.5.4.推送数据到远程仓库:
			git push [remote-name] [branch-name]
			(1).如果要把本地的 master 分支推送到 origin 服务器上(再次说明下,克隆操作会自动使用默认的 master 和 origin 名字),
				可以运行下面的命令：$ git push origin master
			(2).如果在你推数据前，已经有其他人推送了若干更新，那你的推送操作就会被驳回。你必须先把他们的更新抓取到本地，
				合并到自己的项目中，然后才可以再次推送
		3.5.5.查看远程仓库信息: git remote show [remote-name] 查看某个远程仓库的详细信息
		3.5.6.远程仓库的删除和重命名:
			在新版 Git 中可以用 git remote rename 命令修改某个远程仓库在本地的简称	
			$ git remote rename pb paul
	3.6.打标签:
		3.6.1.列显已有的标签
			列出现有标签的命令非常简单，直接运行 git tag 即可
			$ git tag -l 'v1.4.2.*'
		3.6.2.新建标签:
			Git 使用的标签有两种类型：轻量级的(lightweight)和含附注的(annotated)
			(1).轻量级标签就像是个不会变化的分支,实际上它就是个指向特定提交对象的引用.
			而含附注标签,实际上是存储在仓库中的一个独立对象,它有自身的校验和信息,包含着标签的名字,电子邮件地址和日期,
			以及标签说明，标签本身也允许使用 GNU Privacy Guard (GPG) 来签署或验证
			(2).含附注的标签:创建一个含附注类型的标签非常简单,用 -a
				$ git tag -a v1.4 -m 'my version 1.4'
				-m 选项则指定了对应的标签说明，Git 会将此说明一同保存在标签对象中
			(3).签署标签,如果你有自己的私钥,还可以用 GPG 来签署标签,只需要把之前的 -a 改为 -s
				$ git tag -s v1.5 -m 'my signed 1.5 tag'
			(4).轻量级标签,轻量级标签实际上就是一个保存着对应提交对象的校验和信息的文件.要创建这样的标签,
				一个 -a，-s 或 -m 选项都不用,直接给出标签名字即可:
				$ git tag v1.4-lw
			(5).验证标签,使用 git tag -v(verify) [tag-name] 方式验证已经签署的标签。此命令会调用 GPG 来验证签名
			(6).后期加注标签:
				$ git tag -a v1.2 9fceb02
			(7).分享标签,git push 并不会把标签传送到远端服务器上,只有通过显式命令才能分享标签到远端仓库.其命令格式如同推送分支
				


















