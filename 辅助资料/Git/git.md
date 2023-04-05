
# 1、版本控制

是一种记录一个或若干文件内容变化,以便将来查阅特定版本修订情况的系统

- 1.1、本地版本控制系统
- 1.2、集中化的版本控制系统：一个单一的集中管理的服务器,保存所有文件的修订版本
- 1.3、分布式版本控制系统：客户端并不只提取最新版本的文件快照,而是把代码仓库完整地镜像下来

# 2、Git 简介

## 2.1、特点

	速度
	简单的设计
	对非线性开发模式的强力支持（允许上千个并行开发的分支）
	完全分布式
	有能力高效管理类似 Linux 内核一样的超大规模项目（速度和数据量）

## 2.2、与其他版本控制系统差别

-Git 只关心文件数据的整体是否发生变化，而大多数其他系统则只关心文件内容的具体差异
- Git 更像是把变化的文件作快照后，记录在一个微型的文件系统中
- 在 Git 中的绝大多数操作都只需要访问本地文件和资源，不用连网
- 在保存到 Git 之前，所有数据都要进行内容的校验和(checksum)计算，并将此结果作为数据的唯一标识和索引，Git 使用 SHA-1 算法计算数据的校验和,通过对文件的内容或目录的结构计算出一个 SHA-1 哈希值，作为指纹字符串该字串由 40 个十六进制字符（0-9 及 a-f）组成
- 多数操作仅添加数据,一旦提交快照之后就完全不用担心丢失数据

## 2.3、文件的三种状态

- 对于任何一个文件，在 Git 内都只有三种状态:已提交(committed)、已修改(modified)和已暂存(staged)
- 已提交表示该文件已经被安全地保存在本地数据库中了；
- 已修改表示修改了某个文件，但还没有提交保存；
- 已暂存表示把已修改的文件放在下次提交时要保存的清单中；
- 文件流转的三个工作区域：Git 的工作目录、暂存区域以及本地仓库；

## 2.4、Git 基本工作流程

- 在工作目录中修改某些文件；
- 对修改后的文件进行快照，然后保存到暂存区域；
- 提交更新，将保存在暂存区域的文件快照永久转储到 Git 目录中；

## 2.5、CentOS 下安装 Git

- 安装依赖文件：yum install curl-devel expat-devel gettext-devel openssl-devel zlib-devel
- 从下面的 Git 官方站点下载最新版本源代码
- 编译源码：
	```
	make prefix=/usr/local all
	sudo make prefix=/usr/local install
	```
- 直接安装：`yum install git-core`

## 2.6、配置

- 用户信息
```
git config --global user.name chenqing323@hotmail.com
git config --global user.email chenqing323@hotmail.com
```
- 文本编辑器: `git config --global core.editor vim`
- 差异分析工具: `git config --global merge.tool vimdiff`
- 查看配置信息: `git config --list`
- 获取帮助:
	```
	git help <verb>
	git <verb> --help
	man git-<verb>
	```

## 2.7、工作区和暂存区

- 工作区：当前工程的工作目录
- 版本库：工作区内有一个隐藏目录 .git, 这个是 Git 的版本库；版本库中最重要的是 stage 的暂存区,还有Git为我们自动创建的第一个分支master,以及指向master的一个指针叫 HEAD；
	往 git 版本库中添加的时候,分两步执行的:
	- 第一步是用git add把文件添加进去,实际上就是把文件修改添加到暂存区;
	- 第二步是用git commit提交更改,实际上就是把暂存区的所有内容提交到当前分支;
	- 因为我们创建Git版本库时,Git 自动为我们创建了唯一一个master分支,所以,现在,git commit就是往master分支上提交更改
- 暂存区：Git 和其他版本控制系统如SVN的一个不同之处

## 2.8、git与svn

**最核心的区别：SVN（Subversion）是集中式管理的版本控制器，而Git是分布式管理的版本控制器！**

- SVN只有一个单一的集中管理的服务器，保存所有文件的修订版本，而协同工作的人们都通过客户端连到这台服务器，取出最新的文件或者提交更新。集中式版本控制系统最大的毛病就是必须联网才能工作
- Git每一个终端都是一个仓库，客户端并不只提取最新版本的文件快照，而是把原始的代码仓库完整地镜像下来。每一次的提取操作，实际上都是一次对代码仓库的完整备份

# 3、Git 基础

## 3.1、获取项目的 Git 仓库

- 在工作目录中初始化新仓库
	- 要对现有的某个项目开始用 Git 管理，只需到此项目所在的目录，执行：`$ git init`
	- 如果当前目录下有几个文件想要纳入版本控制，需要先用 git add 命令告诉 Git 开始对这些文件进行跟踪，然后提交：
		```
		$ git add *.c
		$ git add README
		$ git commit -m 'initial project version'
		```
- 从现有仓库克隆，克隆仓库的命令格式为 `git clone [url]`
```
$ git clone git://github.com/schacon/grit.git
如果希望在克隆的时候，自己定义要新建的项目目录名称，可以在上面的命令末尾指定新的名字：
$ git clone git://github.com/schacon/grit.git mygit
注意：Git 支持许多数据传输协议
```

## 3.2、记录每次更新到仓库

作目录下面的所有文件都不外乎这两种状态：已跟踪或未跟踪
- 已跟踪的文件是指本来就被纳入版本控制管理的文件，在上次快照中有它们的记录，工作一段时间后，它们的状态可能是未更新.
- 已修改或者已放入暂存区。而所有其他文件都属于未跟踪文件

一些状态操作的命令：
- 检查当前文件状态：`git status`
- 跟踪新文件：`git add`，只要在 "Changes to be committed" 这行下面的，就说明是已暂存状态
- 暂存已修改文件

### 3.2.1、忽略某些文件

创建一个名为 .gitignore 的文件，列出要忽略的文件模式，如
```
$ cat .gitignore
*.[oa] // 告诉 Git 忽略所有以 .o 或 .a 结尾的文件
*~ // 第二行告诉 Git 忽略所有以波浪符（~）结尾的文件
```
文件 `.gitignore` 的格式规范如下：
- 所有空行或者以注释符号 # 开头的行都会被 Git 忽略.
- 可以使用标准的 glob 模式匹配.
- 匹配模式最后跟反斜杠（/）说明要忽略的是目录.
- 要忽略指定模式以外的文件或目录，可以在模式前加上惊叹号（!）取反.
```
# 此为注释 – 将被 Git 忽略
# 忽略所有 .a 结尾的文件
*.a
# 但 lib.a 除外
!lib.a
# 仅仅忽略项目根目录下的 TODO 文件, 不包括 subdir/TODO
/TODO
# 忽略 build/ 目录下的所有文件
build/
# 会忽略 doc/notes.txt 但不包括 doc/server/arch.txt
doc/*.txt    																		*/
```

### 3.2.2、查看已暂存和未暂存的更新

要查看具体修改了什么地方，可以用 git diff 命令
- 要查看尚未暂存的文件更新了哪些部分,不加参数直接输入 git diff
	
	$ git diff // 不是这次工作和上次提交之间的差异

- 若要看已经暂存起来的文件和上次提交时的快照之间的差异：(Git 1.6.1 及更高版本还允许使用 git diff --staged，效果是相同的，但更好记些)
	
	$ git diff --cached
	
### 3.2.3、提交更新

- 每次准备提交前,先用 git status 看下,是不是都已暂存起来了,然后再运行提交命令 git commit，可以用 -m 参数后跟提交说明的方式，在一行命令中提交更新：`$ git commit -m "Story 182: Fix benchmarks for speed"`
- 提交时记录的是放在暂存区域的快照,任何还未暂存的仍然保持已修改状态,可以在下次提交时纳入版本管理
- Git 提供了一个跳过使用暂存区域的方式，只要在提交的时候，给 git commit 加上 -a 选项，Git 就会自动把所有已经跟踪过的文件暂存起来一并提交

### 3.2.4、移除文件

要从 Git 中移除某个文件,就必须要从已跟踪文件清单中移除(确切地说，是从暂存区域移除)然后提交，使用 git rm 命令完成此项工作，并连带从工作目录中删除指定的文件

如果只是简单地从工作目录中手工删除文件,运行 git status 时就会在 “Changes not staged for commit” 部分，然后再运行 git rm 记录此次移除文件的操作
```
$ rm test.txt
$ git rm test.txt
```
最后提交的时候，该文件就不再纳入版本管理了。如果删除之前修改过并且已经放到暂存区域的话，则必须要用强制删除选项 -f,以防误删除文件后丢失修改的内容;

想把文件从 Git 仓库中删除(亦即从暂存区域移除)，但仍然希望保留在当前工作目录中,仅是从跟踪清单中删除：`$ git rm --cached readme.txt`，后面可以列出文件或者目录的名字，也可以使用 glob 模式

### 3.2.5、移动文件

要在 Git 中对文件改名，可以这么做：`$ git mv file_from file_to`

## 3.3、查看提交历史:git log

	3.3.1.git log 选项:
		(1).-p 选项展开显示每次提交的内容差异;
		(2).-2 则仅显示最近的两次更新; -1 表示获取最新一次提交的日志
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
		(7).git reflog用来记录你的每一次命令:
			$ git reflog
			ea34578 HEAD@{0}: reset: moving to HEAD^
			3628164 HEAD@{1}: commit: append GPL
			ea34578 HEAD@{2}: commit: add distributed
			cb926e7 HEAD@{3}: commit (initial): wrote a readme file
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

## 3.4、撤销操作

	[root@localhost learngit]# git log --pretty=oneline
	2a5d1b802fcc17febc4ff822b2cd2280851362e9 modify
	88115cedc87f946094ddc6919360fee30eb4e981 appedn GPL
	2be76a2b59f30dcd5418a8e384cfbdfabcc4fa3d add distributed
	d72d4a771920928ba6ade32d3b7c5ef0101cfeea write a readme file
	2a5d1b802fcc17febc4ff822b2cd2280851362e9 ==> commit id(版本号)
	3.4.1.修改最后一次提交:
		提交完了才发现漏掉了几个文件没有加,或者提交信息写错了.想要撤消刚才的提交操作,可以使用 --amend 选项重新提交	
			$ git commit --amend
		如果刚才提交时忘了暂存某些修改，可以先补上暂存操作，然后再运行 --amend 提交
			$ git commit -m 'initial commit'
			$ git add forgotten_file
			$ git commit --amend // 第二个提交命令修正了第一个的提交内容
	3.4.2.取消已经暂存的文件:git reset HEAD <file>... 的方式取消暂存
		命令既可以回退版本,也可以把暂存区的修改回退到工作区
		git status 命令的输出中有相应的提示信息
		(1).git reset HEAD <file>从缓存中取消
		(2).git reset --hard HEAD^ :从当前版本回退到上一个版本
		(3).git reset --hard 2a5d1b802fcc17febc4ff822b2cd2280851362e9 ==> 回退到之前的某一个版本
			HEAD 指向的版本就是当前版本
	3.4.3.取消对文件的修改:git checkout -- <file>
		注意:在用这条命令前,请务必确定真的不再需要保留刚才的修改
	==> 总结:
		场景1:当你改乱了工作区某个文件的内容,想直接丢弃工作区的修改时,用命令git checkout -- file。
		场景2:当你不但改乱了工作区某个文件的内容,还添加到了暂存区时,想丢弃修改,分两步,第一步用命令git reset HEAD file,就回到了场景1,第二步按场景1操作.
		场景3:已经提交了不合适的修改到版本库时,想要撤销本次提交,参考版本回退一节,不过前提是没有推送到远程库

## 3.5、远程仓库的使用：远程仓库是指托管在网络上的项目仓库

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
		(2).如果在你推数据前,已经有其他人推送了若干更新,那你的推送操作就会被驳回。你必须先把他们的更新抓取到本地，
			合并到自己的项目中，然后才可以再次推送
			git push -u origin master ==> 
			-u Git 不但会把本地的master分支内容推送的远程新的master分支,
			还会把本地的master分支和远程的master分支关联起来,在以后的推送或者拉取时就可以简化命令
	3.5.5.查看远程仓库信息: git remote show [remote-name] 查看某个远程仓库的详细信息
	3.5.6.远程仓库的删除和重命名:
		在新版 Git 中可以用 git remote rename 命令修改某个远程仓库在本地的简称	
		$ git remote rename pb paul
	3.5.7.远程仓库创建 sshkey:
		(1).ssh-keygen -t rsa -C "youremail@example.com",直接回车,
			生存的目录 .ssh 里包含两个文件:id_rsa是私钥,不能泄露出去;id_rsa.pub是公钥

## 3.6、打标签

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
		(8).默认标签是打在最新提交的commit上的,如果需要在之前的打标,方法是找到历史提交的commit id,然后打上就可以了:
			$ git log --pretty=oneline --abbrev-commit
			6a5819e merged bug fix 101
			cc17032 fix bug 101
			7825a50 merge with no-ff
			6224937 add merge
			59bc1cb conflict fixed
			400b400 & simple
			75a857c AND simple
			fec145a branch test
			d17efd8 remove test.txt
			比方说要对add merge这次提交打标签,它对应的commit id是6224937,敲入命令:
			$ git tag v0.9 6224937
		(9).
			命令git push origin <tagname>可以推送一个本地标签;
			命令git push origin --tags可以推送全部未推送过的本地标签;
			命令git tag -d <tagname>可以删除一个本地标签;
			命令git push origin :refs/tags/<tagname>可以删除一个远程标签

## 3.7、分支管理

	3.7.1.创建分支与合并分支:
		(1).创建dev分支,并切换到dev分支:
			git checkout -b dev ==> git checkout命令加上-b参数表示创建并切换,相当于下面两条命令:
				$ git branch dev
				$ git checkout dev
				Switched to branch 'dev'
		(2).查看当前分支:当前分支前面会标一个*号
			$ git branch
			* dev
				master
		(3).切换分支:
			git checkout master
		(4).合并分支:把某个分支合并到主干上, git merge dev 命令用于合并指定分支到当前分支
			[root@localhost learngit]# git merge dev
			更新 433a4cc..03782e3
			Fast-forward 
				readme.txt | 1 +
				1 file changed, 1 insertion(+)
			==> Fast-forward 合并是"快进模式",也就是直接把master指向dev的当前提交,所以合并速度非常快
		(5).删除分支:
			$ git branch -d dev
			Deleted branch dev (was fec145a).
		查看分支:git branch
		创建分支:git branch <name>
		切换分支:git checkout <name>
		创建+切换分支:git checkout -b <name>
		合并某分支到当前分支:git merge <name>
		删除分支:git branch -d <name>	
	3.7.2.冲突解决:
		(1).合并发生冲突:
			[root@localhost learngit]# git merge feature1
			自动合并 readme.txt
			冲突（内容）：合并冲突于 readme.txt
			自动合并失败，修正冲突然后提交修正的结果。
		(2).查看文件状态,显示冲突:
			[root@localhost learngit]# git status
			# 位于分支 master
			# 您的分支领先 'origin/master' 共 2 个提交。
			#   （使用 "git push" 来发布您的本地提交）
			#
			# 您有尚未合并的路径。
			#   （解决冲突并运行 "git commit"）
			#
			# 未合并的路径：
			#   （使用 "git add <file>..." 标记解决方案）
			#
			#	双方修改：     readme.txt
			#
			修改尚未加入提交（使用 "git add" 和/或 "git commit -a"）
		(3).修改冲突文件后,可以直接提交
		(4).用 git log --graph 命令可以看到分支合并图
			[root@localhost learngit]# git log --graph --pretty=oneline --abbrev-commit
			*   6a52e55 conflict fixed
			|\  
			| * 4dba686 AND simple
			* | 9e8d96e & simple
			|/  
			* 03782e3 brach test
			* 433a4cc delete java file
			* 293127e add java file
			* 40056fc test
			* 2a5d1b8 modify
			* 88115ce appedn GPL
			* 2be76a2 add distributed
			* d72d4a7 write a readme file
	3.7.3.分支管理策略:合并分支时,如果可能,Git 会用 Fast forward 模式,但这种模式下,删除分支后,会丢掉分支信息.
			如果要强制禁用Fast forward模式,Git 就会在merge时生成一个新的commit,这样,从分支历史上就可以看出分支信息.
			[root@localhost learngit]# git merge --no-ff -m "merge with no diff" dev
			Merge made by the 'recursive' strategy.
				readme.txt | 2 +-
				1 file changed, 1 insertion(+), 1 deletion(-)
			(1).master分支应该是非常稳定的,也就是仅用来发布新版本,平时不能在上面干活;
			(2).dev分支是不稳定的,到某个时候,比如1.0版本发布时,再把dev分支合并到master上,在master分支发布1.0版本;
			(3).合并分支时,加上 --no-ff 参数就可以用普通模式合并,合并后的历史有分支,能看出来曾经做过合并,而fast forward合并就看不出来曾经做过合并
	3.7.4.Bug 分支:
		(1).git stash:把当前工作现场"储藏"起来，等以后恢复现场后继续工作
			$ git stash
			Saved working directory and index state WIP on dev: 6224937 add merge
			HEAD is now at 6224937 add merge
		(2).先确定要在哪个分支上修复bug,假定需要在master分支上修复,就从master创建临时分支:
			$ git checkout master ==> 切换到 master分支
			$ git checkout -b issue-101 ==> 创建分支
			修复完成后,切换到master分支,并完成合并最后删除 issue-101分支
			# git stash list => 查看工作区域
			stash@{0}: WIP on dev: 0b12ec6 add no diff merge
			如果恢复到之前的工作区域:
			一是用git stash apply恢复,但是恢复后,stash内容并不删除,你需要用git stash drop来删除;
			另一种方式是用git stash pop,恢复的同时把stash内容也删了;
		(3).如果要丢弃一个没有被合并过的分支,可以通过 git branch -D <name>强行删除
	3.7.5.多人协作:
		(1).查看远程仓库:
			[root@localhost learngit]# git remote
			origin
			[root@localhost learngit]# git remote -v
			origin	https://github.com/chenlanqing/learngit.git (fetch)
			origin	https://github.com/chenlanqing/learngit.git (push)
			如果没有推送权限,就看不到push的地址
		(2).推送分支:把该分支上的所有本地提交推送到远程库,推送时,要指定本地分支,这样,Git 就会把该分支推送到远程库对应的远程分支上
			$ git push origin master
			如果要推送其他分支,比如dev,就改成:
			$ git push origin dev
		(3).并不是一定要把本地分支往远程推送,那么,哪些分支需要推送,哪些不需要呢?
			master分支是主分支,因此要时刻与远程同步;
			dev分支是开发分支,团队所有成员都需要在上面工作,所以也需要与远程同步;
			bug分支只用于在本地修复bug,就没必要推到远程了,除非老板要看看你每周到底修复了几个bug;
			feature分支是否推到远程,取决于你是否和你的小伙伴合作在上面开发
		查看远程库信息，使用git remote -v；
		本地新建的分支如果不推送到远程，对其他人就是不可见的;
		从本地推送分支，使用git push origin branch-name,如果推送失败,先用git pull抓取远程的新提交；
		在本地创建和远程分支对应的分支，使用git checkout -b branch-name origin/branch-name,本地和远程分支的名称最好一致；
		建立本地分支和远程分支的关联,使用 git branch --set-upstream branch-name origin/branch-name；
		从远程抓取分支,使用git pull,如果有冲突,要先处理冲突

# 4、自定义git

## 4.1、忽略文件

	在工作区目录下新建文件一个特殊的.gitignore文件,然后把要忽略的文件名填进去,Git 就会自动忽略这些文件
	// https://github.com/github/gitignore
	4.1.1.忽略文件的原则是:
		(1).忽略操作系统自动生成的文件,比如缩略图等;
		(2).忽略编译生成的中间文件、可执行文件等,也就是如果一个文件是通过另一个文件自动生成的;那自动生成的文件就没必要放进版本库;比如Java编译产生的.class文件；
		(3).忽略你自己的带有敏感信息的配置文件,比如存放口令的配置文件
	4.1.2.在windows环境下,发现文件添加不了,原因是这个文件被.gitignore忽略了.
		如果你确实想添加该文件,可以用-f强制添加到Git:
		git add -f readme.txt
	4.1.3.git check-ignore命令检查 .gitignore 文件规则是不是有问题	
		[root@localhost learngit]# git check-ignore -v test.iml 
		.gitignore:3:*.iml	test.iml
		==> .gitignore 第3行规则忽略了该文件,可以知道需要修改哪个规则
		
## 4.2、配置别名

```
$ git config --global alias.st status
可以用 git.st  替代  git status
$ git config --global alias.co checkout
$ git config --global alias.ci commit
$ git config --global alias.br branch
-- 配置 git log 为 git lg,并设置相应的颜色显示
$ git config --global alias.lg "log --color --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit"
[root@localhost learngit]# git lg
```

## 4.3、配置文件

配置Git的时候，加上`--global`是针对当前用户起作用的；如果不加，那只针对当前的仓库起作用。个仓库的Git配置文件都放在`.git/config`文件中

# 5、常见操作

## 5.1、修改远程仓库地址

`git remote set-url origin [远程仓库地址]`

## 5.2、版本回退

### 5.2.1、已提交，没有push

- `git reset --soft <版本号>` 撤销commit
- `git reset --mixed <版本号>g` 撤销commit 和 add 两个动作

### 5.2.2、已提交，并且push

- `git reset --hard <指定需要回退版本的之前一个版本号>` 撤销并舍弃版本号之后的提交记录，使用需谨慎
- `git revert <需要回退版本的当前版本号>` 撤销，但是保留了提交记录

## 5.3、统计代码量

```
git log --author="username"  --since=2022-04-01 --until=2022-08-30 --format='%aN' | sort -u | while read name; do echo -en "$name\t"; git log --author="$name" --pretty=tformat: --numstat | grep "\(.html\|.java\|.xml\|.properties\)$" | awk '{ add += $1; subs += $2; loc += $1 - $2 } END { printf "added lines: %s, removed lines: %s, total lines: %s\n", add, subs, loc }' -; done
```

## 5.3、代理问题

解决：OpenSSL SSL_connect: SSL_ERROR_SYSCALL in connection to github.com:443
```
git config --global --unset http.proxy
# 如果是https设置为
git config --global --unset https.proxy
```
设置完以后重启一下编译器，成功解决！

# 参考资料

* [Git教程](https://www.liaoxuefeng.com/wiki/0013739516305929606dd18361248578c67b8067c8c017b000)
* [Git与SVN](https://www.cnblogs.com/kevingrace/p/5904595.html)






