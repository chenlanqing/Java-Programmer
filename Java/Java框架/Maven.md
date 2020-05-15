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

如果使用IDEA的话，其有一个 Maven Helper 插件，在插件安装好之后，打开pom.xml文件，在底部会多出一个Dependency Analyzer选项

# 八、编写Maven插件

## 1、常用的maven插件

### 1.1、maven-antrun-plugin

maven-antrun-plugin能让用戶在Maven项目中运行Ant任务。用戶可以直接在该插件的配置以Ant的方式编写Target， 然后交 给该插件的run目标去执行

### 1.2、maven-archetype-plugin

生成maven项目的骨架

### 1.3、maven-assembly-plugin

maven-assembly-plugin的用途是制作项目分发包，该分发包可能包含了项目的可执行文件、源代码、readme、平台脚本等 等。maven-assembly-plugin支持各种主流的格式如`zip、tar.gz、jar和war`等，具体打包哪些文件是高度可控的，例如用戶可以按文件级别的粒度、文件集级别的粒度、模块级别的粒度、以及依赖级别的粒度控制打包，此外，包含和排除配置也是支持的

### 1.4、maven-dependency-plugin

maven-dependency-plugin最大的用途是帮助分析项目依赖，`dependency:list`能够列出项目最终解析到的依赖列 表，`dependency:tree`能进一步的描绘项目依赖树，`dependency:analyze`可以告诉你项目依赖潜在的问题，如果你有直接使用 到的却未声明的依赖，该目标就会发出警告。maven-dependency-plugin还有很多目标帮助你操作依赖文件

### 1.5、maven-enforcer-plugin

maven-enforcer-plugin 允许你创建一系列规则强制大家遵守，包括设定Java版本、设定Maven 版本、禁止某些依赖、禁止 SNAPSHOT依赖。只要在一个父POM配置规则，然后让大家继承，当规则遭到破坏的时候，Maven 就会报错。除了标准的规则之外，你还可以扩展该插 件，编写自己的规则。maven-enforcer-plugin的enforce目标负责检查规 则，它默认绑定到生命周期的validate阶段

### 1.6、maven-release-plugin

maven-release-plugin的用途是帮助自动化项目版本发布，它依赖于POM中的SCM信息。release:prepare用来准备版本发布， 具体的工作包括检查是否有未提交代码、检查是否有SNAPSHOT依赖、升级项目的SNAPSHOT版本至RELEASE版本、为项目打 标签等等。release:perform则 是签出标签中的RELEASE源码，构建并发布。版本发布是非常琐碎的工作，它涉及了各种检查， 而且由于该工作仅仅是偶尔需要，因此手动操作很容易遗漏一 些细节，maven-release-plugin让该工作变得非常快速简便，不 易出错。maven-release-plugin的各种目标通常直接在 命令行调用，因为版本发布显然不是日常构建生命周期的一部分

### 1.7、maven-resources-plugin

## 2、编写插件步骤



# 九、Maven常见技巧

## 1、Maven自动更新jar

设置Maven自动升级jar包，jar包引入常规写法：
```xml
<dependency>
	<groupId>org.seleniumhq.selenium</groupId>
	<artifactId>selenium-java</artifactId>
	<version>2.4.0</version>
</dependency>
```
把写法改成以下形式就可以让Maven自动升级jar包了：
```xml
<dependency>
	<groupId>org.seleniumhq.selenium</groupId>
	<artifactId>selenium-java</artifactId>
	<version>[2.4.0,)</version>
</dependency>
```
注意version中使用的中括号和小括号：这样写version之后，selenium框架就会自动升级了，最低版本为2.4.0，最高版本不限，当然你也可以限制最高版本，语法就是开闭区间的语法

## 2、IDEA自动重置LanguageLevel和JavaCompiler版本的问题

使用IDEA时，导入的Maven项目默认的LanguageLevel和JavaCompiler都是1.5，1.5的情况下连最简单的@Override注解都不支持，所以项目可能出现一堆错；

解决办法就是在pom.xml中指定maven-compiler-plugin的版本，该版本会同时影响LanguageLevel和JavaCompiler，修改后默认就成了这里设置的版本
```xml
<build>
	<plugins>
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-compiler-plugin</artifactId>
			<version>2.3.2</version>
			<configuration>
				<source>1.8</source>
				<target>1.8</target>
			</configuration>
		</plugin>
	</plugins>
</build>
```
或者在pom文件使用properties的方式：
```xml
<project>
	<properties>
		<maven.compiler.source>1.7</maven.compiler.source>
		<maven.compiler.target>1.7</maven.compiler.target>
	</properties>
</project>
```

## 3、一行命令同时修改maven项目中多个mudule的版本号

Maven工厂在版本升级的时候就会比较麻烦，因为要遍历的修改所有pom中的版本号。比如要把1.0.0升级到1.0.1，那么就需要把所有的pom中的version都改掉
```xml
</plugins>
    <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>versions-maven-plugin</artifactId>
        <version>2.7</version>
        <configuration>
            <generateBackupPoms>false</generateBackupPoms>
        </configuration>
    </plugin>
</plugins>
```
generateBackupPoms用于配置是否生成备份Pom，用于版本回滚。配置好插件后，执行命令：
```
mvn versions:set -DnewVersion=1.0.1
```

## 4、避免将依赖的jar包打到最大的jar

定 maven dependency 的 scope 为 provided ，这意味着:依赖关系将在运行时由其容器或 JDK 提供。 具有此范围的依赖关系不会传递，也不会捆绑在诸如 WAR 之类的包中，也不会包含在运行时类路径中

## 5、跳过单元测试

在执行mvn package 或者 install 命令时，在后面跟上参数：`-Dmaven.test.skip=true` 或者 `-DskipTests=true`

这两者区别：
- `-DskipTests=true`：会编译单元测试类，但是不会执行单元测试；
- `-Dmaven.test.skip=true`：不会编译单元测试类，更不会执行单元测试类

## 6、引入本地jar包

时候，需要引入在中央仓库找不到的 jar，但又想通过 maven 进行管理，可以通过设置 dependency 的 scope 为 system 来引入本地 jar：
- 将私有 jar 放置在 `resouces/lib` 下，然后以如下方式添加依赖
- groupId 和 artifactId 可以按照 jar 包中的 package 设置，只要和其他 jar 不冲突即可

比如：
```xml
<dependency>
	<groupId>xxx</groupId>
	<artifactId>xxx</artifactId>
	<version>1.0.0</version>
	<scope>system</scope>
	<systemPath>${project.basedir}/src/main/resources/lib/xxx-6.0.0.jar</systemPath>
</dependency>
```

# 十、Maven私服仓库搭建


# 参考资料

* [Maven工程](https://www.jianshu.com/p/34740cd1fb58)
* [Maven插件开发指南](https://maven.apache.org/plugin-developers/index.html)
* [Maven插件官方文档](http://maven.apache.org/plugins/index.html)







