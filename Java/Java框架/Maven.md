# 一、Maven

Maven是Java的一个构建工具，简化Java工程的构建和依赖管理

## 1、Maven常用命令

- `mvn -v`：查看maven版本
- `mvn compile` 编译命令
- `mvn test` 测试命令
- `mvn packag`e 打包命令
- `mvn clean` 删除target
- `mvn install` 安装jar包到本地仓库， 在本地Repository中安装ja

## 2、创建目录骨架

- `mvn archetype`：generate 按照相关提示进行操作
- `mvn archetype`：generate -DgroupId=组织名，一般为公司网址的反写+项目名称 -DartifactId=项目名次-模块名称 -Dversion=版本号 -Dpackage=代码所存在的包

使用命令行创建工程：
```
mvn archetype:generate -DgroupId=com.demo -DartifactId=simple -DpackageName=com.demo.simple -DarchetypeArtifactId=maven-archetype-webapp
```
参数说明：
- `-DgroupId`：groupId（项目或者组织的唯一标志）。
- `-DartifactId`：artifactId（项目的通用名称）。
- `-DpackageName`：项目的包名。
- `-DarchetypeArtifactId`：模板名称，如：maven-archetype-webapp表示创建一个标准的maven web项目。如果要创建一个普通java项目可不填写此参数。
- `-DinteractiveMode`：是否已交互模式进行，如果是false的话就会采用默认设置建立项目，交互模式下：
	```bash
	[INFO] Using property: groupId = com.demo
	[INFO] Using property: artifactId = simple
	Define value for property 'version' 1.0-SNAPSHOT: : 
	[INFO] Using property: package = com.demo
	Confirm properties configuration:
	groupId: com.demo
	artifactId: simple
	version: 1.0-SNAPSHOT
	package: com.demo
	Y: : 
	```

常用`archetypeArtifactId`：
```
appfuse-basic-jsf (创建一个基于Hibernate，Spring和JSF的Web应用程序的原型) 
appfuse-basic-spring (创建一个基于Hibernate，Spring和Spring MVC的Web应用程序的原型) 
appfuse-basic-struts (创建一个基于Hibernate，Spring和Struts 2的Web应用程序的原型) 
appfuse-basic-tapestry (创建一个基于Hibernate, Spring 和 Tapestry 4的Web应用程序的原型) 
appfuse-core (创建一个基于 Hibernate and Spring 和 XFire的jar应用程序的原型) 
appfuse-modular-jsf (创建一个基于 Hibernate，Spring和JSF的模块化应用原型) 
appfuse-modular-spring (创建一个基于 Hibernate, Spring 和 Spring MVC 的模块化应用原型) 
appfuse-modular-struts (创建一个基于 Hibernate, Spring 和 Struts 2 的模块化应用原型) 
appfuse-modular-tapestry (创建一个基于 Hibernate, Spring 和 Tapestry 4 的模块化应用原型) 
maven-archetype-j2ee-simple (一个简单的J2EE的Java应用程序) 
maven-archetype-marmalade-mojo (一个Maven的 插件开发项目 using marmalade) 
maven-archetype-mojo (一个Maven的Java插件开发项目) 
maven-archetype-portlet (一个简单的portlet应用程序) 
maven-archetype-profiles () 
maven-archetype-quickstart () 
maven-archetype-site-simple (简单的网站生成项目) 
maven-archetype-site (更复杂的网站项目) 
maven-archetype-webapp (一个简单的Java Web应用程序) 
jini-service-archetype (Archetype for Jini service project creation) 
softeu-archetype-seam (JSF+Facelets+Seam Archetype) 
softeu-archetype-seam-simple (JSF+Facelets+Seam (无残留) 原型) 
softeu-archetype-jsf (JSF+Facelets 原型) 
jpa-maven-archetype (JPA 应用程序) 
spring-osgi-bundle-archetype (Spring-OSGi 原型) 
confluence-plugin-archetype (Atlassian 聚合插件原型) 
jira-plugin-archetype (Atlassian JIRA 插件原型) 
maven-archetype-har (Hibernate 存档) 
maven-archetype-sar (JBoss 服务存档) 
wicket-archetype-quickstart (一个简单的Apache Wicket的项目) 
scala-archetype-simple (一个简单的scala的项目) 
lift-archetype-blank (一个 blank/empty liftweb 项目) 
lift-archetype-basic (基本（liftweb）项目) 
cocoon-22-archetype-block-plain ([http://cocoapacorg2/maven-plugins/]) 
cocoon-22-archetype-block ([http://cocoapacorg2/maven-plugins/]) 
cocoon-22-archetype-webapp ([http://cocoapacorg2/maven-plugins/]) 
myfaces-archetype-helloworld (使用MyFaces的一个简单的原型) 
myfaces-archetype-helloworld-facelets (一个使用MyFaces和Facelets的简单原型) 
myfaces-archetype-trinidad (一个使用MyFaces和Trinidad的简单原型) 
myfaces-archetype-jsfcomponents (一种使用MyFaces创建定制JSF组件的简单的原型) 
gmaven-archetype-basic (Groovy的基本原型) 
gmaven-archetype-mojo (Groovy mojo 原型)
```
						 
## 3、maven中坐标和仓库

**坐标**：即构件，下面三者构成了构件的唯一标识
```xml
<groupId>junit</groupId>
<artifactId>junit</artifactId>
<version>4.10</version>
```
**仓库**：本地仓库和远程仓库；一般先从本地仓库获取，如果没有再到远程仓库获取，在没有，则报错
	
**镜像仓库**：一般是针对无法访问国外网站，配置镜像仓库：修改`settings.xml`文件的`<mirrors>`
```xml
<mirror>
	<id>maven.net.cn</id>
	<mirrorOf>central</mirrorOf>
	<name>Central mirror in China</name>
	<url>http：//maven.net.cn/content/groups/public</url>
</mirror>
```
**本地仓库、私服、中央仓库：**
- 本地仓库就是相当于加了一层jar包缓存，先到这里来查。如果这里查不到，那么就去私服上找，如果私服也找不到，那么去中央仓库去找，找到jar后，会把jar的信息同步到私服和本地仓库中；
- 私服，就是公司内部局域网的一台服务器而已，因此私服中存储了本公司的内部专用的jar！不仅如此，私服还充当了中央仓库的镜像，说白了就是一个代理！；
- 中央仓库：该仓库存储了互联网上的jar，由Maven团队来维护，地址是：`http://repo1.maven.org/maven2/`，依赖搜索：`https://mvnrepository.com/`

## 4、maven的生命周期和插件

完整的项目构建过程：清理、编译、测试、打包、集成测试、验证、部署	

maven的生命周期：分为三个周期，周期之间相互不影响
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

**denpendencyManagement和dependencies区别**
- dependencies 即使在子项目中不写该依赖项，那么子项目仍然会从父项目中继承该依赖项（全部继承）
- dependencyManagement 里只是声明依赖，并不实现引入，因此子项目需要显示的声明需要用的依赖。如果不在子项目中声明依赖，是不会从父项目中继承下来的；只有在子项目中写了该依赖项，并且没有指定具体版本，才会从父项目中继承该项，并且version和scope都读取自父pom；另外如果子项目中指定了版本号，那么会使用子项目中指定的jar版本；
- dependencyManagement 中的 dependencies 元素只表明依赖项版本的优先选择，并不影响项目的依赖项；而 dependencies 元素则影响项目的依赖项；
- 只有当外层的dependencies 元素中没有指明版本信息时， dependencyManagement中的 dependencies 元素才起作用

## 5、pom.xml介绍

```xml
<project xmlns="http：//maven.apache.org/POM/4.0.0" xmlns：xsi="http：//www.w3.org/2001/XMLSchema-instance"
  xsi：schemaLocation="http：//maven.apache.org/POM/4.0.0 http：//maven.apache.org/xsd/maven-4.0.0.xsd">
  <!-- 指定当前pom的版本 -->
  <modelVersion>4.0.0</modelVersion>

  <groupId>反写公司的网址+项目名</groupId>
  <artifactId>项目名+模块名</artifactId>
  
  <!--
	第一个0表示大版本号，第二个0表示分支版本号，第三个0表示小版本号
	snapshot：快照
	alpha：内部测试版本
	beta：公测版本
	release：稳定版本
	GA：正式发布
  -->
  <version>0.0.1-SNAPSHOT</version>
  <!-- maven打包格式，默认是jar，war，zip, pom -->
  <packaging>jar</packaging>
  <!-- 项目描述名 -->
  <name>demo</name>
  <!-- 项目的地址 -->
  <url>http：//maven.apache.org</url>
  <!-- 项目描述信息 -->
  <description></description>
  <!-- 属性 -->
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <dependencies>
		<dependency>
			<groupId>junit</groupId>
			<artifactId>junit</artifactId>
			<version>3.8.1</version>
				<type></type>
			<!-- 依赖范围 -->
			<scope>test</scope> 
			<!-- 设置依赖是否可选，true or false -->
			<optional></optional> 
			<exclusions>
				<!-- 排除依赖传递列表 -->
				<exclusion></exclusion>
			</exclusions>
		</dependency>
  </dependencies>
  
  <!-- 依赖管理 -->
  	<dependencyManagement>
  		<dependencies>
  			<dependency></dependency>
  		</dependencies>
	</dependencyManagement>
	<!-- 构建项目 -->
	<build> 
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

### 5.1、依赖范围

`<scope>`，6个值
- compile(默认值)
- provided：虽然在编译和测试阶段需要依赖这个库，但在部署应用程序时，该库不会包含在生成的JAR文件或WAR文件中，有助于减小构建产物的大小并确保不会出现重复的依赖
- runtime：测试和运行时有效，如JDBC
- test：测试时有效
- system：与本机系统相关联，可移植性差
- import：导入的范围，只使用在dependencyManagement中，表示从其他的pom中导入dependency配置

### 5.2、多环境

实际场景下，存在多套开发环境：测试环境、开发环境、线上环境等。对于多套环境而言，我们可以抽取出相同的部分，放入到公共的文件当中，把那些跟着环境变化而变化的配置信息，分环境存放，最后根据选择的环境而将那部分配置信息动态注入到公共的文件当中

通过profile定义了多套环境。Maven有一套自己内置的属性，比如`${basedir}，${project.xxx}`，Java相关，操作系统相关等，这些可以直接在pom.xml中进行引用；用户也可以通过`<properties>`来自定义属性，比如上面的例子，我们就可以在pom.xml中通过`${profiles.active}`来指明用户选择的profile；

选择Profile进行打包？实质上就是在执行`mvn package -Pxxx`而已

# 二、依赖

- [Maven依赖机制](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html)

## 1、查找依赖树

查看依赖jar包Tree结构并输出到文件
```
mvn dependency:tree -Doutput=output.txt  
mvn dependency:tree -Dverbose -Dincludes=asm:asm 就会出来asm依赖包的分析信息
```

## 2、依赖传递

依赖传递：如果A依赖B，B依赖C，那么引入A，意味着B和C都会被引入。

Maven的最近依赖策略：如果一个项目依赖相同的groupId、artifactId的多个版本，那么在依赖树（mvn dependency:tree）中离项目最近的那个版本将会被使用。通常指的是依赖树中距离最近的节点

能不能选择高版本的进行依赖么？据了解，Gradle就是version+策略

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

maven-shade-plugin 插件

## 4、Maven依赖加载顺序

- [maven冲突Jar包的加载规则](https://developer.jdcloud.com/article/3117)

### 4.1、jar包加载原则

- 最短路径原则：面对多级（两级及以上）的不同依赖，会优先选择路径最短的依赖；
- 声明优先原则：面对多级（两级及以上）的同级依赖，先声明的依赖会覆盖后声明的依赖；
- 同级依赖中，后声明的依赖会覆盖先声明的依赖；

更简单的描述：
- 本级优先于上级，上级优先于下级；
- 本级依赖版本优先于管理版本；
- 同一个包中后加载的版本覆盖先加载的版本；
- 上级管理版本和本级管理版本会覆盖下级依赖版本；
- 不同下级jar中依赖了不同版本，优先使用先加载下级jar中的版本；
- 与版本号大小没有关系
- 本级无法使用下级管理版本；


### 4.2、最短路径原则

最短路径原则：使用最短路径加载的前提是，项目中存在两级以上的不同依赖jar包，此时项目会优先加载路径最短的jar包；

![](image/Maven-依赖最短路径.png)

分别在模块B 和 模块C 中直接或者间接引入 commons-pool，其中：
- 模块B 引入：`commons-pool2:2.7`
	```xml
	<dependency>
		<groupId>org.apache.commons</groupId>
		<artifactId>commons-pool2</artifactId>
		<version>2.7.0</version>
	</dependency>
	```
- 模块C中引入：`jedis:5.0.0-alpha2`，该依赖包依赖了 `commons-pool2:2.11.1`
	```xml
	<dependency>
		<groupId>redis.clients</groupId>
		<artifactId>jedis</artifactId>
		<version>5.0.0-alpha2</version>
	</dependency>
	```

在IDE中pom可以看到依赖加载的情况：

![](image/Maven-路径冲突-1.png)

可以通过：`mvn dependency:tree` 命令来查看该项目的依赖树，观察发现实际加载的版本是`commons-pool2:2.7.0`，符合maven中的最短路径优先原则
```xml
[INFO] --- maven-dependency-plugin:2.8:tree (default-cli) @ test-a ---
[INFO] io.ebonex:test-a:jar:0.0.1-SNAPSHOT
[INFO] +- io.ebonex:test-b:jar:0.0.1-SNAPSHOT:compile
[INFO] |  \- org.apache.commons:commons-pool2:jar:2.7.0:compile
[INFO] \- io.ebonex:test-c:jar:0.0.1-SNAPSHOT:compile
[INFO]    \- redis.clients:jedis:jar:5.0.0-alpha2:compile
[INFO]       +- org.slf4j:slf4j-api:jar:1.7.36:compile
[INFO]       +- org.json:json:jar:20230227:compile
[INFO]       \- com.google.code.gson:gson:jar:2.10.1:compile
```

### 4.3、声明优先原则

声明优先原则的前提是对于两级以上的同级依赖，先声明的依赖会覆盖后声明的依赖包；

![](image/Maven-声明优先.png)

分别在模块B 和 模块C 中直接引入不同版本的 commons-pool，然后通过改变两个模块在pom文件中声明的先后顺序来观察项目启动后实际加载的jar包；

- 模块B 引入：`commons-pool2:2.7`
	```xml
	<dependency>
		<groupId>org.apache.commons</groupId>
		<artifactId>commons-pool2</artifactId>
		<version>2.7.0</version>
	</dependency>
	```
- 模块C中引入：引入`commons-pool2:2.11.1`
	```xml
	<dependency>
		<groupId>org.apache.commons</groupId>
		<artifactId>commons-pool2</artifactId>
		<version>2.11.1</version>
	</dependency>
	```

**场景1：先引入模块B、再引入模块C**

模块A中引入的顺序：
```xml
<dependencies>
	<dependency>
		<groupId>io.ebonex</groupId>
		<artifactId>test-b</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
	<dependency>
		<groupId>io.ebonex</groupId>
		<artifactId>test-c</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
</dependencies>
```
观察下面输出结果，可以发现加载的是：`commons-pool2:2.7.0`
```xml
[INFO] --- maven-dependency-plugin:2.8:tree (default-cli) @ test-a ---
[INFO] io.ebonex:test-a:jar:0.0.1-SNAPSHOT
[INFO] +- io.ebonex:test-b:jar:0.0.1-SNAPSHOT:compile
[INFO] |  \- org.apache.commons:commons-pool2:jar:2.7.0:compile
[INFO] \- io.ebonex:test-c:jar:0.0.1-SNAPSHOT:compile
```

**场景2：先引入模块C、再引入模块B**
```xml
<dependencies>
	<dependency>
		<groupId>io.ebonex</groupId>
		<artifactId>test-c</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
	<dependency>
		<groupId>io.ebonex</groupId>
		<artifactId>test-b</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
</dependencies>
```
观察下面输出结果，可以发现加载的是：`commons-pool2:2.11.1`
```xml
[INFO] --- maven-dependency-plugin:2.8:tree (default-cli) @ test-a ---
[INFO] io.ebonex:test-a:jar:0.0.1-SNAPSHOT
[INFO] +- io.ebonex:test-c:jar:0.0.1-SNAPSHOT:compile
[INFO] |  \- org.apache.commons:commons-pool2:jar:2.11.1:compile
[INFO] \- io.ebonex:test-b:jar:0.0.1-SNAPSHOT:compile
```

### 4.4、同级依赖：覆盖

![](image/Maven-同级依赖.png)

直接在模块A 中引入`commons-pool2:2.7` 和 `commons-pool2:2.11.1`，分别看下引入顺序引起加载版本的问题：

**场景1：`commons-pool2:2.7` 在 `commons-pool2:2.11.1` 之前**
```xml
<dependencies>
	<dependency>
		<groupId>org.apache.commons</groupId>
		<artifactId>commons-pool2</artifactId>
		<version>2.7.0</version>
	</dependency>
	<dependency>
		<groupId>org.apache.commons</groupId>
		<artifactId>commons-pool2</artifactId>
		<version>2.11.1</version>
	</dependency>
</dependencies>
```
观察jar包加载顺序，可以发现加载的是：`commons-pool2:2.11.1`
```xml
[INFO] --- maven-dependency-plugin:2.8:tree (default-cli) @ test-a ---
[INFO] io.ebonex:test-a:jar:0.0.1-SNAPSHOT
[INFO] \- org.apache.commons:commons-pool2:jar:2.11.1:compile
```

**场景2：`commons-pool2:2.11.1` 在 `commons-pool2:2.7` 之前**
```xml
<dependencies>
	<dependency>
		<groupId>org.apache.commons</groupId>
		<artifactId>commons-pool2</artifactId>
		<version>2.11.1</version>
	</dependency>
	<dependency>
		<groupId>org.apache.commons</groupId>
		<artifactId>commons-pool2</artifactId>
		<version>2.7.0</version>
	</dependency>
</dependencies>
```
观察jar包加载顺序，可以发现加载的是：`commons-pool2:2.7`
```xml
[INFO] --- maven-dependency-plugin:2.8:tree (default-cli) @ test-a ---
[INFO] io.ebonex:test-a:jar:0.0.1-SNAPSHOT
[INFO] \- org.apache.commons:commons-pool2:jar:2.7.0:compile
```

### 4.5、常见异常信息

Jar发生冲突后在程序启动时常见异常报错，下面四种异常是能够直观表征Jar包加载冲突：
- 程序抛出：`java.lang.ClassNotFoundException`异常；
- 程序抛出：`java.lang.NoSuchMethodError`异常；
- 程序抛出：`java.lang.NoClassDefFoundError`异常；
- 程序抛出：`java.lang.LinkageError`异常等；

# 三、编写Maven插件

* [Maven插件开发指南](https://maven.apache.org/plugin-developers/index.html)
* [Maven插件官方文档](https://maven.apache.org/plugins/index.html)

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

### 1.8、maven-compiler-plugin

Maven的编译插件： https://maven.apache.org/plugins/maven-compiler-plugin/compile-mojo.html
```xml
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-compiler-plugin</artifactId>
	<version>3.8.1</version>
	<configuration>
		<source>14</source>
		<target>14</target>
		<!-- 注解元素处理器路径 -->
		<annotationProcessorPaths>
			<path>
				<groupId>org.projectlombok</groupId>
				<artifactId>lombok</artifactId>
				<version>${lombok-version}</version>
			</path>
		</annotationProcessorPaths>                 
	</configuration>
</plugin>
```

## 2、编写插件步骤


# 四、Maven常见技巧

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

## 3、批量修改Maven版本号

### 3.1、使用插件versions-maven-plugin

Maven工厂在版本升级的时候就会比较麻烦，因为要遍历的修改所有pom中的版本号。比如要把1.0.0升级到1.0.1，那么就需要把所有的pom中的version都改掉，在主POM中配置如下插件
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
generateBackupPoms用于配置是否生成备份Pom，用于版本回滚。

配置好插件后，如果需要修改版本号
```
mvn versions:set -DnewVersion='1.0.1'
```
如果你后悔更新了且generateBackupPoms设置为true，还可以回退：
```
mvn versions:revert
```
如果你确定了，则输入提交命令，就完成了：`mvn versions:commit`

### 3.2、使用占位符去统一管理

在主POM中配置如下：
```xml
<modelVersion>4.0.0</modelVersion>
<groupId>com.qing.fan</groupId>
<artifactId>security</artifactId>
<packaging>pom</packaging>
<version>${revision}</version>
```
子模块引用其他子模块版本也是同样定义：
```xml
<dependencies>
	<dependency>
		<groupId>com.qing.fan</groupId>
		<artifactId>common</artifactId>
		<version>${revision}</version>
	</dependency>
</dependencies>
```
这个`${reversion}`参数在主pom里定义：
```xml
<properties>
	<revision>2.8.5</revision>
</properties>
```
再加上一个插件，这个插件的作用是在编译打包时，会自动替换`${reversion}`占位：
```xml
<plugin>
	<groupId>org.codehaus.mojo</groupId>
	<artifactId>flatten-maven-plugin</artifactId>
	<version>1.2.7</version>
	<configuration>
		<updatePomFile>true</updatePomFile>
		<flattenMode>resolveCiFriendliesOnly</flattenMode>
	</configuration>
	<executions>
		<execution>
			<id>flatten</id>
			<phase>process-resources</phase>
			<goals>
				<goal>flatten</goal>
			</goals>
		</execution>
		<execution>
			<id>flatten.clean</id>
			<phase>clean</phase>
			<goals>
				<goal>clean</goal>
			</goals>
		</execution>
	</executions>
</plugin>
```
做好以上这几步，那么你要修改版本号的时候，只需要在主pom里把reversion修改一次就可以了，不管多少子模块，都可以生效

## 4、避免将依赖的jar包打到最大的jar

定 maven dependency 的 scope 为 provided ，这意味着：依赖关系将在运行时由其容器或 JDK 提供。 具有此范围的依赖关系不会传递，也不会捆绑在诸如 WAR 之类的包中，也不会包含在运行时类路径中

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

## 7、下载源码

当使用Maven下载源码时报错，Can not download sources，可以尝试使用：`mvn dependency:sources`

# 五、Maven私服仓库搭建

# 六、Maven运行web项目

## 1、idea运行maven项目

- 创建maven项目，项目结构如下：
	```
	├── pom.xml
	├── src
	│   ├── main
	│   │   ├── java
	│   │   │   └── com
	│   │   │       └── blue
	│   │   │           └── fish
	│   │   │               ├── HelloServlet.java
	│   │   ├── resources
	│   │   │   └── log4j.properties
	│   └── test
	│       └── java
	```
	HelloServlet.java：
	```java
	@WebServlet("/hello")
	public class HelloServlet extends HttpServlet {
		@Override
		public  void init(){
			System.out.println("初始化Servlet ...");
		}
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			String name = "webservlet";
			log.debug("name is " + name);
			req.setAttribute("name", name);
			req.getRequestDispatcher("/WEB-INF/jsp/hello.jsp").forward(req, resp);
		}
		@Override
		public  void destroy(){
			System.out.println("Destroy...");
		}
	}
	```

- 添加依赖：
	```xml
	<!-- 注意打包类型选择：war -->
	<packaging>war</packaging>
	<properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencies>
        <!-- https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>javax.servlet.jsp</groupId>
            <artifactId>javax.servlet.jsp-api</artifactId>
            <version>2.3.3</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
    <build>
        <finalName>custom-framework</finalName>
        <pluginManagement>
            <plugins>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>3.8.1</version>
                    <configuration>
                        <source>8</source>
                        <target>8</target>
                    </configuration>
                </plugin>
                <plugin>
					<!-- 内嵌tomcat，该插件只兼容到tomcat7 -->
                    <groupId>org.apache.tomcat.maven</groupId>
                    <artifactId>tomcat7-maven-plugin</artifactId>
                    <version>2.2</version>
                    <configuration>
                        <path>/${project.artifactId}</path>
                        <systemProperties>
                            <java.util.logging.SimpleFormatter.format>%1$tT %3$s %5$s %n</java.util.logging.SimpleFormatter.format>
                        </systemProperties>
                    </configuration>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>
	```
- 添加webapp目录，最终目录结构如下；在servlet3.0之后，可以不用web.xml来配置，使用注解可以替代
	```
	├── pom.xml
	├── src
	│   ├── main
	│   │   ├── java
	│   │   │   └── com
	│   │   │       └── blue
	│   │   │           └── fish
	│   │   │               ├── HelloServlet.java
	│   │   ├── resources
	│   │   │   └── log4j.properties
	│   │   └── webapp
	│   │       ├── WEB-INF
	│   │       │   └── jsp
	│   │       │       └── hello.jsp
	│   │       ├── static
	│   │       └── templates
	│   └── test
	│       └── java
	```
- 运行项目，在idea中如下操作：

	![](image/maven-运行web项目.png)


# 七、其他

## 1、发布包

https://www.jitpack.io/










