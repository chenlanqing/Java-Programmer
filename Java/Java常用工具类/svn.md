#### 1、SVN创建版本库:
	在需创建的文件目录下，执行
	svnadmin create D:\Dev\svnrep\oa
	
#### 2、启动服务端程序:
	(1).svnserve -d -r D:\Dev\svnrep\oa
	-d : 表示后台执行
	-r ： 表示版本库根目录
	
	(2).svn服务是监听3690端口，使用netstat -an查看端口
	
	(3).注册到windows服务:	在这个命令中,等号左边都没有空格,右边都有一个空格
		------------------------------------------------------------------------------------------------
		|主命令  |子命令 | 参数1  | 参数2                                 | 参数3      | 参数四        |
		------------------------------------------------------------------------------------------------
		|sc      |create | 服务名 | binpath= "运行服务的二进制文件及参数" | start= auto| depend= Tcpip |
		------------------------------------------------------------------------------------------------
		
		◆binpath结构组成:
		svnserve.exe路径: svn安装目录\bin\svnserve.exe;
		--service :　svnserve命令参数，表示以服务方式启动subversion
		-r : 表示版本库根目录;
		参数3: 表示版本库目录
		
		◆单仓库与多仓库
		单仓库:指定与具体项目对应的仓库目录;
		多仓库:指定版本库的根目录;
		
		命令举例:
		sc create svnservice binpath= "D:\Dev\svn\bin\svnserve.exe --service -r D:\Dev\svnrep" start= auto depend= Tcpip
		
#### 3.从Eclipse中上传项目:
	在项目上右键：
	Team --> Share project,选择svn,输入仓库地址:
	svn://服务器主机地址/具体仓库目录/
		
		
		
		
		
		
		
		
		
		
		
		
		
		