#  一、Go基本

##  1、环境安装

- [goenv:Go 环境管理](https://github.com/go-nv/goenv)

##  2、HelloWorld

新建文件： main.go，文件增加如下内容
```go
// hello.go
// package declaration
package main
// import package
import "fmt"
// function
func add(a, b int) int {
  return a+b
}
// global variable
var g int = 100

func main() {
  a, b := 1, 2
  res := add(a, b)
  fmt.Println("a=", a, "b=", b, "a+b=", res)
  fmt.Println("g=", g)
  fmt.Println("hello world!")
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
- `func main()`是程序开始执行的函数(但是如果有`func init()`函数，则会先执行`init`函数，再执行`main`函数)

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

# 二、语法

## 1、与 Java 对比

[Java 与 Go 语言完整对比指南](./Go&Java.md)

## 数据类型

### 数字

- 整数：int, uint8, uint16, uint32, uint64, int8, int16, int32, int64
- 浮点数：float32, float64
- 复数：
	- complex64：实部和虚部都是float32类型的值：`var v complex64 = 1 + 0.5i`
	- 注意：虚部为1的情况，1不能省略，否则编译报错: `var v complex64 = 1 + i // compile error: undefined i`

其他数字类型
- byte：等价于uint8，数据范围0-255，定义的时候超过这个范围会编译报错
- rune：等价于int32，数据范围-2147483648-2147483647，字符串里的每一个字符的类型就是rune类型，或者说int32类型
- uint：在32位机器上等价于uint32，在64位机器上等价于uint64
- uintptr: 无符号整数，是内存地址的十进制整数表示形式，应用代码一般用不到（https://stackoverflow.com/questions/59042646/whats-the-difference-between-uint-and-uintptr-in-golang）
- reflect包的TypeOf函数或者fmt.Printf的%T可以用来获取变量的类型
```go
var b byte = 10
var c = 'a'
fmt.Println(reflect.TypeOf(b)) // uint8
fmt.Println(reflect.TypeOf(c)) // int32
fmt.Printf("%T\n", c) // int32
```

### 字符串

string：
- len(str)函数可以获取字符串长度
- 注意：string是immutable的，不能在初始化string变量后，修改string里的值，除非对string变量重新赋值
	```go
	func main() {
		str := "abc"
		str = "def" // ok
		/* 下面的就不行，编译报错：cannot assign to str[0] (strings are immutable)
		str[0] = "d"
		*/
		fmt.Println(str)
	}
	```
- 字符串里字符的访问可以通过str[index]下标索引或者range迭代的方式进行访问
	```go
	func main() {
		str := "abc"
		/*下标访问*/
		size := len(str)
		for i:=0; i<size; i++ {
			fmt.Printf("%d ", str[i])
		}
		fmt.Println()
		
		/*range迭代访问*/
		for _, value := range str {
			fmt.Printf("%d ", value)
		}
		fmt.Println()
	}
	```
- 不能对string里的某个字符取地址：如果s[i]是字符串s里的第i个字符，那&s[i]这种方式是非法的
- string可以使用 `:` 来做字符串截取  
	注意：这里和切片slice的截取有区别
	- 字符串截取后赋值给新变量，对新变量的修改不会影响原字符串的值
	- 切片截取后复制给新变量，对新变量的修改会影响原切片的值
	```go
	func strTest() {
		s := "abc"
		fmt.Println(len(s)) // 3
		s1 := s[:]
		s2 := s[:1]
		s3 := s[0:]
		s4 := s[0:2]
		fmt.Println(s, s1, s2, s3, s4) // abc abc a abc ab
	}
	```
- string可以用+做字符串拼接

### bool

值只能为true或false

## 条件

说明：go 语言没有三目运算符

### if

基本
```go
if x > max {
	x = max
}
// 嵌套
if x <= y {
	min = x
} else {
	min = y
}
```
带初始化语句
```go
if x := f(); x <= y {
	return x
}
```

### switch

每一个case分支都是唯一的，从上往下逐一判断，直到匹配为止。如果某些case分支条件重复了，编译会报错  
默认情况下每个case分支最后自带break效果，匹配成功就不会执行其它case。  
如果需要执行后面的case，可以使用 fallthrough。  
> 使用 fallthrough 会强制执行紧接着的下一个 case 语句，不过fallthrough 不会去分析紧接着的下一条 case 的表达式结果是否满足条件，而是直接执行case里的语句块。
```go
// 写法 1：
switch variable {
  case value1:
    do sth1
  case value3, value4: // 可以匹配多个值，只要一个满足条件即可
    do sth34
  case value5:
    do sth5
  default:
  	do sth
}
```
```go
// 写法 2：
switch os := runtime.GOOS; os {
	case "darwin":
		fmt.Println("OS X.")
	case "linux":
		fmt.Println("Linux.")
	default:
		// freebsd, openbsd,
		// plan9, windows...
		fmt.Printf("%s.\n", os)
}
// 上面的写法和这个等价
os := runtime.GOOS
switch os {
	case "darwin":
		fmt.Println("OS X.")
	case "linux":
		fmt.Println("Linux.")
	default:
		// freebsd, openbsd,
		// plan9, windows...
		fmt.Printf("%s.\n", os)
}
```

case分支的每个condition结果必须是一个bool值，要么为true，要么为false
```go
switch {
  case condition1:
  	do sth1
  case condition2:
  	do sth2
  case condition3, condition4:
  	do sth34
  default:
  	do sth
}
```

只适用于interface的类型判断，而且{要和switch在同一行，{前面不能有分号;
```go
func main() {
	var i interface{} = 10
	switch t := i.(type) {
	case bool:
		fmt.Println("I'm a bool")
	case int:
		fmt.Println("I'm an int")
	default:
		fmt.Printf("Don't know type %T\n", t)
	}
}
```

## 循环

### for

### goto

类似C++里的goto
```go
label: statement
goto label
```
示例代码：
```go
func main() {
	LOOP: 
		println("Enter your age:")
		var age int
		_, err := fmt.Scan(&age) // 接受控制台输入
		if err != nil {
			println("error:", err)
			goto LOOP
		}
		if age < 18 {
			println("You are not eligible to vote!")
			goto LOOP
		} else {
			println("You are eligible to vote!")
		}
		println("all finish")
}
```

### break和label结合

break和label结合使用，可以跳出二重或者多重for循环。

break A直接跳出整个外层for循环，所以下面的例子只执行i=0, j=0这一次循环。
```go
// 最终输出 0 0 Hello, 世界
func main() {
A:
	for i := 0; i < 2; i++ {
		for j := 0; i < 2; j++ {
			print(i, " ", j, " ")
			break A
		}

	}
	fmt.Println("Hello, 世界")
}
```
下面的例子，break只能跳出位于里层的for循环，会执行i=0, j=0和i=1, j=0这2次循环。
```go
// 输出 0 0 1 0 Hello, 世界
func main() {
	for i := 0; i < 2; i++ {
		for j := 0; i < 2; j++ {
			print(i, " ", j, " ")
			break
		}

	}
	fmt.Println("Hello, 世界")
}
```

## defer

含义：defer 用于注册一个函数调用，使其在包含该 defer 的函数返回之前执行；  
类似 Java 中的 try-finally 块（特别像 finally）  
> defer = 注册一个延迟执行的函数，在函数 return 执行后、真正退出前执行。

### defer 的执行时机

Go 所有的 defer 都在 当前函数结束时按照 LIFO（栈）顺序执行：
```go
defer A()
defer B()
defer C()
// 退出函数时执行顺序：C, B, A
```
即：最后 defer 的最先执行

### 典型使用场景

1. 资源释放（最常用）Go 没有 try-finally，常用 defer 自动释放资源：
```go
f, _ := os.Open("file.txt")
defer f.Close()
```
等价于：
```java
try {
    FileInputStream f = ...
} finally {
    f.close();
}
```
2. 解锁 Mutex
```go
mu.Lock()
defer mu.Unlock()
```
数据库连接/事务关闭、性能监控（函数耗时）

### 参数绑定规则

defer 会立刻计算参数，但不会执行函数体
```go
func main() {
    x := 10
    defer fmt.Println(x)
    x = 20
}
// 输出结果： 10
```
因为 defer 在定义时已捕获参数 x 的值；如果使用匿名函数，则可以捕获变量本身
```go
func main() {
    x := 10
    defer func() { fmt.Println(x) }()
    x = 20
}
// 输出结果：20
```

### defer + return 的顺序

执行顺序:
1. 执行 return 表达式，把结果保存起来
2. 执行所有 defer
3. 函数返回保存好的结果值
```go
func f() (result int) {
    defer func() {
        result++
    }()
    return 1
}
// 2
```

### defer 的开销与注意事项

defer 有少量性能损耗（相比 Java 的 finally 要轻量许多）  
不建议在高频操作的循环里写 defer
```go
// 错误用法
for i := 0; i < 100000; i++ {
    f, _ := os.Open("a.txt")
    defer f.Close()   // 循环内会创建成千上万 defer，导致延后执行
}
```

# 命名规范

Go 语言强调简洁、统一和可读性，命名需尽可能短且具有清晰语义。Go 的命名规范核心是：简短、统一、可读。避免过度设计和冗长命名。

### 包（package）命名

- 全部小写，不使用下划线或大写字母。
- 名称应简短、有意义，如：fmt、io、http。
- 导入时避免与常用名称冲突。

### 变量命名

- 尽量使用短变量名，尤其是局部变量：i、n、err。
- 成员变量使用更具描述性的名称。
- 使用驼峰命名（mixedCaps）。如：userName。

### 常量命名

- 使用驼峰命名。
- 对于枚举型常量，一般与类型放在一起，并以类型名为前缀。

### 函数命名

- 使用驼峰命名。
- Exported 函数首字母大写，如：Println。
- Unexported 函数首字母小写，如：computeValue。

### 接口命名

- 单方法接口常以 “-er” 结尾，例如：Reader、Writer。
- 接口名应体现行为，而非结构。

### 结构体命名

- 使用名词或名词短语，如：File、User、Config。

### 方法命名

- 动词开头，表达行为，如：Open、Close、Send。

### 错误命名规范

- 错误变量一般命名为 err。
- 自定义 error 类型的变量采用驼峰命名，如：ErrNotFound。

### 缩写使用

- 固定缩写全部大写，但在驼峰结构中保持一律小写，例如：httpServer、urlReader。
- Exported 名称仍保持首字母大写，例如：HTTPRequest。

### 测试命名

- 测试函数以 Test 开头：TestAdd。
- 基准测试以 Benchmark 开头：BenchmarkSort。
- 示例以 Example 开头：ExampleUsage。

### 文件命名

- 全部小写，使用下划线分隔：http_server.go、user_service.go。
- 测试文件以 _test.go 结尾。

# 指针

区别于C/C++中的指针，Go语言中的指针不能进行偏移和运算，是安全指针。


# 参考资料

- [一个云原生的 Go 微服务框架](https://github.com/zeromicro/go-zero)
- [《The Go Programming Language》 ](https://github.com/golang-china/gopl-zh)
- [Go By Example](https://gobyexample.com/)
- [Go 101](https://go101.org/article/101.html)
- [Effective Go](https://go.dev/doc/effective_go)
- [Go 精华文章列表](https://go.dev/wiki/Articles)
- [Go 相关资源](https://github.com/avelino/awesome-go)
- [Go lang learning](https://github.com/0voice/awesome_golang_learning)
- [Go教程](https://github.com/jincheng9/go-tutorial)
- [Golang 学习教程](https://golang.xiniushu.com/)
- [golang-cheat-sheet是目前GitHub上最流行的golang代码速查表](https://github.com/jincheng9/golang-cheat-sheet-cn)
