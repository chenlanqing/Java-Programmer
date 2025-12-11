#  一、Go基本

##  1、环境安装

- [goenv:Go 环境管理](https://github.com/go-nv/goenv)

##  2、HelloWorld

新建文件： main.go，文件增加如下内容
```go
package main
import "fmt"
func main() {
	fmt.Println("Hello world!")
}
```

编译并运行：
```
go build main.go
./main
```
或者直接运行
```
go run main.go
```
像 go run 这类命令更多用于开发调试阶段，真正的交付成果还是需要使用 go build 命令构建的。

- Go 包是 Go 语言的基本组成单元。一个 Go 程序就是一组包的集合，所有 Go 代码都位于包中；
- Go 源码可以导入其他 Go 包，并使用其中的导出语法元素，包括类型、变量、函数、方法等，而且，main 函数是整个 Go 应用的入口函数；
- Go 源码需要先编译，再分发和运行。如果是单 Go 源文件的情况，我们可以直接使用 go build 命令 +Go 源文件名的方式编译。不过，对于复杂的 Go 项目，我们需要在 Go Module 的帮助下完成项目的构建

##  3、go项目布局

### 3.1、Go可执行程序布局

对于以生产可执行程序为目的的 Go 项目，它的典型项目结构分为五部分：
- 放在项目顶层的 Go Module 相关文件，包括 go.mod 和 go.sum；
- cmd 目录：存放项目要编译构建的可执行文件所对应的 main 包的源码文件；
- 项目包目录：每个项目下的非 main 包都“平铺”在项目的根目录下，每个目录对应一个 Go 包；
- internal 目录：存放仅项目内部引用的 Go 包，这些包无法被项目之外引用；
- vendor 目录：这是一个可选目录，为了兼容 Go 1.5 引入的 vendor 构建模式而存在的。这个目录下的内容均由 Go 命令自动维护，不需要开发者手工干预

典型布局：
```
$ tree -F exe-layout 
exe-layout
├── cmd/                 cmd 目录就是存放项目要编译构建的可执行文件对应的 main 包的源文件，
│   ├── app1/            如果你的项目中有多个可执行文件需要构建，每个可执行文件的 main 包单独放在一个子目录中
│   │   └── main.go
│   └── app2/
│       └── main.go
├── go.mod               Go 语言包依赖管理使用的配置文件
├── go.sum               Go 语言包依赖管理使用的配置文件
├── internal/
│   ├── pkga/
│   │   └── pkg_a.go
│   └── pkgb/
│       └── pkg_b.go
├── pkg1/       这是一个存放项目自身要使用、同样也是可执行文件对应 main 包所要依赖的库文件，同时这些目录下的包还可以被外部项目引用
│   └── pkg1.go
├── pkg2/
│   └── pkg2.go
└── vendor/
```

### 3.2、Go 库项目的典型结构布局

Go 库项目仅对外暴露 Go 包，这类项目的典型布局形式是这样的：
```
$tree -F lib-layout 
lib-layout
├── go.mod
├── internal/
│   ├── pkga/
│   │   └── pkg_a.go
│   └── pkgb/
│       └── pkg_b.go
├── pkg1/
│   └── pkg1.go
└── pkg2/
    └── pkg2.go
```

## 4、包依赖管理问题

### 4.1、构建模式

Go 程序由 Go 包组合而成的，Go 程序的构建过程就是确定包版本、编译包以及将编译后得到的目标文件链接在一起的过程。

Go 语言的构建模式历经了三个迭代和演化过程，分别是最初期的 GOPATH、1.5 版本的 Vendor 机制，以及现在的 Go Module

#### 4.1.1、GOPATH

Go 语言在首次开源时，就内置了一种名为 GOPATH 的构建模式。在这种构建模式下，Go 编译器可以在本地 GOPATH 环境变量配置的路径下，搜寻 Go 程序依赖的第三方包。如果存在，就使用这个本地包进行编译；如果不存在，就会报编译错误；
```go
package main
import "github.com/sirupsen/logrus"
func main() {
    logrus.Println("hello, gopath mode")
}
```
假定 Go 程序导入了 github.com/user/repo 这个包，同时假定当前 GOPATH 环境变量配置的值为：`export GOPATH=/usr/local/goprojects:/home/user/go`，那么在 GOPATH 构建模式下，Go 编译器在编译 Go 程序时，就会在下面两个路径下搜索第三方依赖包是否存在：
```
/usr/local/goprojects/src/github.com/user/repo
/home/user/go/src/github.com/user/repo
```
没有在本地找到程序的第三方依赖包的情况，如何解决呢？可以通过 go get 命令将本地缺失的第三方依赖包下载到本地，比如：
```
go get github.com/sirupsen/logrus
```
这里的 go get 命令，不仅能将 logrus 包下载到 GOPATH 环境变量配置的目录下，它还会检查 logrus 的依赖包在本地是否存在，如果不存在，go get 也会一并将它们下载到本地


# 参考自来

- [一个云原生的 Go 微服务框架](https://github.com/zeromicro/go-zero)
- [《The Go Programming Language》 ](https://github.com/golang-china/gopl-zh)
- [Go By Example](https://gobyexample.com/)
- [Go 101](https://go101.org/article/101.html)
- [Effective Go](https://go.dev/doc/effective_go)
- [Go 精华文章列表](https://go.dev/wiki/Articles)
- [Go 相关资源](https://github.com/avelino/awesome-go)
- [Go lang learning](https://github.com/0voice/awesome_golang_learning)
- [Go教程](https://github.com/jincheng9/go-tutorial)
