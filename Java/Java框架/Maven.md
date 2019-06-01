# 一、Maven



# 二、Maven常用命令

- mvn -v：查看maven版本
- mvn compile 编译命令
- mvn test 测试命令
- mvn package 打包命令
- mvn clean 删除target
- mvn install 安装jar包到本地仓库， 在本地Repository中安装ja

# 三、自动创建目录骨架

- mvn archetype：generate 按照相关提示进行操作
- mvn archetype：generate -DgroupId=组织名，一般为公司网址的反写+项目名称 -DartifactId=项目名次-模块名称 -Dversion=版本号 -Dpackage=代码所存在的包
						 
# 四、maven中坐标和仓库

- 坐标：即构件，下面三者构成了构件的唯一标识
	```xml
	<groupId>junit</groupId>
	<artifactId>junit</artifactId>
	<version>4.10</version>
	```

- 仓库：本地仓库和远程仓库；一般先从本地仓库获取，如果没有再到远程仓库获取，在没有，则报错
	
- 镜像仓库：一般是针对无法访问国外网站，配置镜像仓库：修改settings.xml文件的`<mirrors>`
	```xml
	<mirror>
      <id>maven.net.cn</id>
      <mirrorOf>central</mirrorOf>
      <name>Central mirror in China</name>
      <url>http：//maven.net.cn/content/groups/public</url>
    </mirror>
	```

本地仓库、私服、中央仓库：
- 本地仓库就是相当于加了一层jar包缓存，先到这里来查。如果这里查不到，那么就去私服上找，如果私服也找不到，那么去中央仓库去找，找到jar后，会把jar的信息同步到私服和本地仓库中；
- 私服，就是公司内部局域网的一台服务器而已，因此私服中存储了本公司的内部专用的jar！不仅如此，私服还充当了中央仓库的镜像，说白了就是一个代理！；
- 中央仓库：该仓库存储了互联网上的jar，由Maven团队来维护，地址是：http://repo1.maven.org/maven2/

# 五、maven的生命周期和插件

- 完整的项目构建过程：清理、编译、测试、打包、集成测试、验证、部署	

- maven的生命周期：分为三个周期，周期之间相互不影响
	```
	clean：清理项目，分为三个阶段：
		(1).pre-clean：执行清理前的工作
		(2).clean：清理上一次构建生成的项目
		(3).post-clean：执行清理后的文件
	
	default：构建项目(最核心)
		compile，test，package，install
			
	site：生成项目站点
		(1).pre-site：在生成项目站点前要完成的工作
		(2).site：生成项目的站点文档
		(3).post-site：在生成项目站点后要完成的工作
		(4).site-deploy：发布生成的站点到服务器上
	```

- DenpendencyManagement和dependencies区别
	- dependencies即使在子项目中不写该依赖项，那么子项目仍然会从父项目中继承该依赖项（全部继承）
	- dependencyManagement里只是声明依赖，并不实现引入，因此子项目需要显示的声明需要用的依赖。如果不在子项目中声明依赖，是不会从父项目中继承下来的；只有在子项目中写了该依赖项，并且没有指定具体版本，才会从父项目中继承该项，并且version和scope都读取自父pom；另外如果子项目中指定了版本号，那么会使用子项目中指定的jar版本；
	- dependencyManagement 中的 dependencies 元素只表明依赖项版本的优先选择，并不影响项目的依赖项；而 dependencies 元素则影响项目的依赖项；
	- 只有当外层的dependencies 元素中没有指明版本信息时， dependencyManagement中的 dependencies 元素才起作用

# 六、pom.xml介绍

```xml
<project xmlns="http：//maven.apache.org/POM/4.0.0" xmlns：xsi="http：//www.w3.org/2001/XMLSchema-instance"
  xsi：schemaLocation="http：//maven.apache.org/POM/4.0.0 http：//maven.apache.org/xsd/maven-4.0.0.xsd">
  //<!-- 指定当前pom的版本 -->
  <modelVersion>4.0.0</modelVersion>

  <groupId>反写公司的网址+项目名</groupId>
  <artifactId>项目名+模块名</artifactId>
  /*
  <!--
	第一个0表示大版本号，第二个0表示分支版本号，第三个0表示小版本号
	snapshot：快照
	alpha：内部测试版本
	beta：公测版本
	release：稳定版本
	GA：正式发布
  -->
  */
  <version>0.0.1-SNAPSHOT</version>
  // maven打包格式，默认是jar，war，zip
  <packaging>jar</packaging>
  // 项目描述名
  <name>demo</name>
  // 项目的地址
  <url>http：//maven.apache.org</url>
  // 项目描述信息
  <description></description>
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
	  	<type></type>
      <scope>test</scope> // 依赖范围
	  <optional></optional> //设置依赖是否可选，true or false
	  <exclusions> // 排除依赖传递列表
      	<exclusion></exclusion>
      </exclusions>
    </dependency>
  </dependencies>
  
  <dependencyManagement> // 依赖管理
  	<dependencies>
  		<dependency></dependency>
  	</dependencies>
	
  <build> // 构建项目
  	<plugins>
  		<plugin>
  			<groupId>org.apache.maven.plugins</groupId>
  			<artifactId>maven-source-plugin</artifactId>
  			<version>2.4</version>
  			<executions>
  				<execution>
  					<phase>package</phase>
  					<goals>
  						<goal>jar-no-fork</goal>
  					</goals>
  				</execution>
  			</executions>
  		</plugin>
  	</plugins>
  </build>

	<profiles>
		<profile>
				<id>local</id>
				<properties>
						<spring.profiles.active>local</spring.profiles.active>
				</properties>
		</profile>
	<profiles>
</project>
```

## 1、依赖范围

`<scope>`，6个值
- compile(默认值)
- provided：编译和测试时有效，j2ee的servlet
- runtime：测试和运行时有效，如JDBC
- test：测试时有效
- system：与本机系统相关联，可移植性差
- import：导入的范围，只使用在dependencyManagement中，表示从其他的pom中导入dependency配置

## 2、多环境

实际场景下，存在多套开发环境：测试环境、开发环境、线上环境等。对于多套环境而言，我们可以抽取出相同的部分，放入到公共的文件当中，把那些跟着环境变化而变化的配置信息，分环境存放，最后根据选择的环境而将那部分配置信息动态注入到公共的文件当中

通过profile定义了多套环境。Maven有一套自己内置的属性，比如`${basedir}，${project.xxx}`，Java相关，操作系统相关等，这些可以直接在pom.xml中进行引用；用户也可以通过`<properties>`来自定义属性，比如上面的例子，我们就可以在pom.xml中通过`${profiles.active}`来指明用户选择的profile；

选择Profile进行打包？实质上就是在执行`mvn package -Pxxx`而已

# 七、依赖：冲突、聚合、继承

## 1、查找依赖树

查看依赖jar包Tree结构并输出到文件
```
mvn dependency:tree -Doutput=output.txt  
mvn dependency:tree -Dverbose -Dincludes=asm:asm 就会出来asm依赖包的分析信息
```

## 2、依赖传递

依赖传递：如果A依赖B，B依赖C，那么引入A，意味着B和C都会被引入。

Maven的最近依赖策略：如果一个项目依赖相同的groupId、artifactId的多个版本，那么在依赖树（mvn dependency:tree）中离项目最近的那个版本将会被使用。（能不能选择高版本的进行依赖么？据了解，Gradle就是version+策略）


## 3、依赖冲突

对于Maven而言，同一个groupId同一个artifactId下，只能使用一个version！

比如工程中需要引入A、B，而A依赖1.0版本的C，B依赖2.0版本的C，那么问题来了，C使用的版本将由引入A、B的顺序而定？这显然不靠谱！如果A的依赖写在B的依赖后面，将意味着最后引入的是1.0版本的C，很可能在运行阶段出现类（ClassNotFoundException）、方法（NoSuchMethodError）找不到的错误（因为B使用的是高版本的C）

查看冲突：`mvn dependency:tree -Dverbose -Doutput=output.txt`  根据导出的文件

冲突解决：
- 使用`<dependencyManagement>` ，这种主要用于子模块的版本一致性中；
- 使用`<exclusions>`，在实际中我们可以在IDEA中直接利用插件帮助我们生成；
- 可以直接使用`<dependency>`显示指定的冲突的依赖；

如果我们新加入一个依赖的话，那么先通过`mvn dependency:tree`命令形成依赖树，看看我们新加入的依赖，是否存在传递依赖，传递依赖中是否和依赖树中的版本存在冲突，如果存在多个版本冲突，利用上文的方式进行解决

# 八、编写Maven插件

// TODO 


# 参考资料

* [Maven工程](https://www.jianshu.com/p/34740cd1fb58)
* [Maven插件开发指南](https://maven.apache.org/plugin-developers/index.html)








