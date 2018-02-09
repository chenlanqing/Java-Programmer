<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [1、SVN创建版本库:](#1svn%E5%88%9B%E5%BB%BA%E7%89%88%E6%9C%AC%E5%BA%93)
- [2、启动服务端程序:](#2%E5%90%AF%E5%8A%A8%E6%9C%8D%E5%8A%A1%E7%AB%AF%E7%A8%8B%E5%BA%8F)
- [3.从Eclipse中上传项目:](#3%E4%BB%8Eeclipse%E4%B8%AD%E4%B8%8A%E4%BC%A0%E9%A1%B9%E7%9B%AE)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

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
		
		
		
		
		
		
		
		
		
		
		
		
		
		