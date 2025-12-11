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

# 二、语法

## 与 Java 对比

### 1. 代码组织与基本格式 (Structure & Formatting)

| 特性 | Java | Go | 核心差异点 |
| :--- | :--- | :--- | :--- |
| **行尾分号** | 必须 `;` | **不需要** (编译器自动插入) | 如果你想在一行写多条语句才需要。 |
| **代码块** | `{ ... }` | `{ ... }` | **强制要求**左花括号 `{` 不能换行（必须跟在语句末尾）。 |
| **入口函数** | `public static void main(String[] args)` | `func main() { ... }` | 必须位于 `package main` 中，且无参数、无返回值。 |
| **包声明** | `package com.example.demo;` | `package main` | Go 的包名通常只是目录名，不包含反向域名。 |
| **导入** | `import java.util.*;` | `import "fmt"` | 不支持通配符 `*`；未使用的 Import 会导致**编译错误**。 |
| **变量可见性** | `public`, `private`, `protected` | **首字母大小写** | 大写=Public (Exported)，小写=Private (Package visible)。 |

---

### 2. 变量与常量 (Variables & Constants)

| 特性 | Java | Go | 核心差异点 |
| :--- | :--- | :--- | :--- |
| **变量声明** | `int a = 10;` | `var a int = 10` | **类型在变量名之后**。 |
| **类型推断** | `var a = 10;` (Java 10+) | `a := 10` | `:=` 只能用于函数内部，声明并赋值。 |
| **默认值** | 基本类型有值，对象为 `null` | **Zero Value** | `int=0`, `bool=false`, `string=""`, 指针/切片=`nil`。 |
| **常量** | `static final int A = 1;` | `const A = 1` | const 只能是数字、字符串、布尔值（编译期确定）。 |
| **枚举** | `enum Color { RED, BLUE }` | `const ( Red = iota; Blue )` | Go 没有 enum 关键字，用 `const` + `iota` 计数器模拟。 |
| **未使用变量** | 允许 (除了 Checkstyle 警告) | **编译错误** | 函数声明了但未使用的局部变量会导致编译失败（全局变量除外）。 |

---

### 3. 数据类型 (Data Types)

| 特性 | Java | Go | 核心差异点 |
| :--- | :--- | :--- | :--- |
| **基本类型** | `int`, `long`, `boolean`, `byte` | `int`, `int64`, `bool`, `byte`, `rune` | `byte` 是 `uint8` 别名；`rune` 是 `int32` 别名（用于 Unicode 字符）。 |
| **字符串** | `String` (对象) | `string` (基本类型) | Go 字符串不可变，底层是字节数组；支持多行字符串 (反引号 \`)。 |
| **数组** | `int[] arr = new int[5];` | `var arr [5]int` | **Go 数组是值类型**！赋值或传参会拷贝整个数组。长度是类型的一部分。 |
| **动态数组** | `ArrayList<Integer>` | `[]int` (Slice 切片) | 切片是对底层数组的引用，最常用。 |
| **Map** | `HashMap<K, V>` | `map[K]V` | 内置类型。使用 `make(map[K]V)` 创建。 |
| **指针** | 无 (隐式引用) | `*int`, `&a` | 支持指针，但不支持指针运算。 |
| **类型转换** | `(int) longValue` | `int(longValue)` | **必须显式转换**，即便是 `int` 转 `int64` 也必须转。 |

---

### 4. 流程控制 (Control Flow)

这是最容易踩坑的地方，Go 砍掉了很多关键字。

| 特性 | Java | Go | 核心差异点 |
| :--- | :--- | :--- | :--- |
| **If 条件** | `if (x > 0) { ... }` | `if x > 0 { ... }` | **无括号**。 |
| **If 初始化** | 不支持 | `if v := get(); v > 0 { }` | 可在判断前执行一段简短语句，变量仅在 if 块内有效。 |
| **循环** | `for`, `while`, `do-while` | **只有 `for`** | `while(cond)` 写成 `for cond { ... }`；死循环写成 `for { ... }`。 |
| **Switch** | 需 `break` 防止穿透 | **默认 Break** | 想穿透（Fallthrough）需显式加 `fallthrough` 关键字。 |
| **Switch 增强** | 仅支持值/枚举/字符串 | 可替代 if-else 链 | `switch { case x > 0: ... }` (无表达式 switch)。 |
| **三元运算符** | `a ? b : c` | **不支持** | 必须写成 `if-else`。 |
| **自增/自减** | `a = b++` (表达式) | `a++` (语句) | `++` 只能单独一行，不能放在赋值等号右边，**不支持 `++i`**。 |

---

### 5. 函数与方法 (Functions & Methods)

| 特性 | Java | Go | 核心差异点 |
| :--- | :--- | :--- | :--- |
| **定义** | `public int add(int a, int b)` | `func add(a, b int) int` | 关键字 `func`。参数类型合并简写。 |
| **返回值** | 单个返回值 | **多返回值** | `func swap() (int, int) { return 1, 2 }`。 |
| **重载** | 支持 (Overloading) | **不支持** | 同一个包内函数名不能重复。 |
| **命名参数** | 不支持 | 支持命名返回值 | `func f() (res int) { res=1; return }`。 |
| **方法定义** | 在 class 内部 | 函数名前加 (Receiver) | `func (s *Student) getName() string`。 |
| **不定参数** | `Type... args` | `args ...Type` | 使用时传递切片需解包 `arr...`。 |
| **函数类型** | Lambda 表达式 | 一等公民 | 函数可以赋值给变量，作为参数传递。 |

---

### 6. 面向对象 (OOP) - 这里的差异最大

Go 没有 `class`，没有 `extends`。

| 特性 | Java | Go | 核心差异点 |
| :--- | :--- | :--- | :--- |
| **类** | `class Person { ... }` | `struct Person { ... }` | 只有结构体。 |
| **构造函数** | `public Person() { ... }` | **无** | 通常用工厂函数：`func NewPerson() *Person`。 |
| **继承** | `extends Parent` | **嵌入 (Embedding)** | `struct Child { Parent }`。直接复用字段和方法（组合优于继承）。 |
| **多态/接口** | `interface A`, `implements A` | `interface A` | **隐式实现**。只要方法签名对上了，就自动算作实现了接口。 |
| **this 指针** | 隐式 `this` | 显式 Receiver | `func (p *Person) Say()`，这里的 `p` 就是 `this`，名字你可以随便取。 |
| **泛型** | List<String> (类型擦除) | List[String] (Go 1.18+) | 使用方括号 `[]` 定义泛型参数。 |
| **注解** | `@Override`, `@Deprecated` | **Struct Tag** | 仅用于结构体字段元数据，如 `json:"name" xml:"name"`。 |

---

### 7. 异常与错误处理 (Error Handling)

| 特性 | Java | Go | 核心差异点 |
| :--- | :--- | :--- | :--- |
| **异常流** | `try-catch-finally` | **Check Error** | 通过多返回值返回 `error` 接口。 |
| **资源释放** | `finally` 或 try-with-resources | **`defer`** | `defer f.Close()`。函数返回前按**后进先出**顺序执行。 |
| **致命错误** | `throw new RuntimeException()` | `panic("msg")` | 导致程序崩溃并打印堆栈。 |
| **恢复** | `catch` | `recover()` | 仅在 `defer` 中捕获 `panic`，防止程序崩溃。 |

---

### 8. 并发编程 (Concurrency)

| 特性 | Java | Go | 核心差异点 |
| :--- | :--- | :--- | :--- |
| **线程** | `new Thread().start()` | `go func()` | `go` 关键字开启协程（Goroutine）。 |
| **通信** | 共享内存 (Volatile, Lock) | **Channel** | `ch <- val` (发), `val := <-ch` (收)。 |
| **多路复用** | `Selector` (NIO) | **`select`** | 专门用于监听多个 Channel 的读写状态。 |
| **同步** | `synchronized`, `Lock` | `sync.Mutex`, `sync.WaitGroup` | Go 也有锁，但推荐用 Channel。 |

---

### 9. 内存与指针 (Memory & Pointers)

*   **Java**: 只有引用类型（Object）和基本类型。对象都在堆上（除非逃逸分析优化）。
*   **Go**:
    *   **值语义**：结构体赋值是拷贝。
    *   **指针语义**：`&` 取地址，`*` 解引用。
    *   **nil**: 只有指针、接口、Map、Slice、Channel、Function 可以是 `nil`。Struct 实例永远不是 `nil`（是零值）。

### 5 个 Go 语法细节

1.  **左花括号不换行**：写成 `func main() \n {` 会直接编译报错。
2.  **变量声明不用分号**，且**类型在后面**。
3.  **Error 处理太“啰嗦”**：每调用一个函数都要写 `if err != nil`。
4.  **没有 `null` 处理对象**：你不能判断 `if person == nil`（除非 person 是指针），你需要判断 `person` 是否等于空的结构体。
5.  **切片的 `append`**：必须把结果赋值回去 `s = append(s, item)`，因为底层数组可能扩容导致地址变化。

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
