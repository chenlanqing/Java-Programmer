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

# 基础

## 与 Java 对比

[Java 与 Go 语言完整对比指南](./Go&Java.md)

## init 与 main 函数

### init 函数

go语言中init函数用于包(package)的初始化，该函数是go语言的一个重要特性
- init函数是用于程序执行前做包的初始化的函数，比如初始化包里的变量等；
- 每个包可以拥有多个init函数；
- 包的每个源文件也可以拥有多个init函数；
- 同一个包中多个init函数的执行顺序go语言没有明确的定义(说明)；
- 不同包的init函数按照包导入的依赖关系决定该初始化函数的执行顺序；
- init函数不能被其他函数调用，而是在main函数执行之前，自动被调用；

### main 函数

Go语言程序的默认入口函数(主函数)：`func main()`  
函数体用｛｝一对括号包裹。
```go
func main(){
	//函数体
}
```

### init函数和main函数的异同

相同点：两个函数在定义时不能有任何的参数和返回值，且Go程序自动调用。  

不同点：
- init可以应用于任意包中，且可以重复定义多个。
- main函数只能用于main包中，且只能定义一个。

## 下划线

“_”是特殊标识符，用来忽略结果。

在Golang里，import的作用是导入其他package。 import 下划线（如：import hello/imp）的作用：当导入一个包时，该包下的文件里所有init()函数都会被执行，然而，有些时候我们并不需要把整个包都导入进来，仅仅是是希望它执行init()函数而已。这个时候就可以使用 import 引用该包。即使用【import _ 包路径】只是引用该包，仅仅是为了调用init()函数，所以无法通过包名来调用包中的其他函数
```go
import "database/sql"
import _ "github.com/go-sql-driver/mysql"
```
第二个import就是不直接使用mysql包，只是执行一下这个包的init函数，把mysql的驱动注册到sql包里，然后程序里就可以使用sql包来访问mysql数据库了

忽略返回结果：
```go
buf := make([]byte, 1024)
f, _ := os.Open("/Users/***/Desktop/text.txt")
```
下划线意思是忽略这个变量，比如os.Open，返回值为`*os.File，error`，普通写法是`f,err := os.Open("xxxxxxx")`，如果此时不需要知道返回的错误值，就可以用`f, _ := os.Open("xxxxxx")`，如此则忽略了error变量;

也是占位符，意思是那个位置本应赋给某个值，但是不需要这个值。所以就把该值赋给下划线，意思是丢弃。这样编译器可以更好的优化，任何类型的单个值都可以丢给下划线。这种情况是占位用的，方法返回两个结果，而只想要一个结果。那另一个就用 "_" 占位，而如果用变量的话，不使用，编译器是会报错的。

## 数据类型

| 类型         | 长度(字节) | 默认值  | 说明                                      |
| ------------ | ---------- | ------- | ----------------------------------------- |
| bool         | 1          | false   |                                           |
| byte         | 1          | 0       | uint8                                     |
| rune         | 4          | 0       | Unicode Code Point, int32                 |
| int, uint    | 4或8       | 0       | 32 或 64 位                               |
| int8, uint8  | 1          | 0       | -128 ~ 127, 0 ~ 255，byte是uint8 的别名   |
| int16, uint16| 2          | 0       | -32768 ~ 32767, 0 ~ 65535                 |
| int32, uint32| 4          | 0       | -21亿~21亿, 0 ~ 42亿，rune是int32 的别名  |
| int64, uint64| 8          | 0       |                                           |
| float32      | 4          | 0.0     |                                           |
| float64      | 8          | 0.0     |                                           |
| complex64    | 8          |         |                                           |
| complex128   | 16         |         |                                           |
| uintptr      | 4或8       |         | 以存储指针的 uint32 或 uint64 整数        |
| array        |            |         | 值类型                                    |
| struct       |            |         | 值类型                                    |
| string       |            | ""      | UTF-8字符串                               |
| slice        |            | nil     | 引用类型                                  |
| map          |            | nil     | 引用类型                                  |
| channel      |            | nil     | 引用类型                                  |
| interface    |            | nil     | 接口                                      |
| function     |            | nil     | 函数                                      |

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

多行字符串：Go语言中要定义一个多行字符串时，就必须使用反引号字符，反引号间换行将被作为字符串中的换行，但是所有的转义字符均无效，文本将会原样输出。
```go
s1 := `第一行
    第二行
    第三行
    `
fmt.Println(s1)
```

### bool

值只能为true或false

### 类型转换

Go语言中只有强制类型转换，没有隐式类型转换。该语法只能在两个类型之间支持相互转换的时候使用。

强制类型转换的基本语法如下：`T(表达式)`，其中，T表示要转换的类型。表达式包括变量、复杂算子和函数返回值等

### 静态类型

Go是一个静态强类型语言，静态指的是Go所有变量的类型早在编译期间就已经确定了，在程序的生命周期都不会再发生改变，尽管Go中的短变量声明有点类似动态语言的写法，但其变量类型是由编译器自行推断的，最根本的区别在于它的类型一旦推断出来后不会再发生变化
```go
func main() {
	var a int = 64
	a = "64"
	fmt.Println(a) // cannot use "64" (untyped string constant) as int value in assignment
}
```

## 变量与常量

**全局变量**：全局变量允许声明后不使用，编译不会报错
```go
var name type = value
// 全局变量如果采用这个方式定义，那不能在全局范围内赋值，只能在函数体内给这个全局变量赋值
var name type // value will be defaulted to 0, false, "" based on the type
/* 如果定义上面的全局变量，就不能紧接着在下一行通过name=value的方式对变量name做赋值，
比如 name = 10，会编译报错：
 syntax error: non-declaration statement outside function body
*/
var (
	v1 int = 10
	v2 bool = true
)
var (
	v5 int   // the value will be defaulted to 0
	v6 bool  // the value will be defaulted to false
)
var (
	v3 = 20
	v4 = false
)
```

局部变量多了一种定义方式：`name := value`
- 局部变量定义后必须要被使用，否则编译报错，报错内容为declared but not used。

### 初始值

- 数值：所有数值类型的零值都是0
- 整数，零值是0。byte, rune, uintptr也是整数类型，所以零值也是0。
- 浮点数，零值是0
- 复数，零值是0+0i
- bool，零值是false
- 字符串，零值是空串""
- 指针：`var a *int`，零值是nil
- 切片：var a []int，零值是nil
- map：var a map[string] int，零值是nil
- 函数：var a func(string) int，零值是nil
- channel：var a chan int，通道channel，零值是nil
- 接口：var a interface_type，接口interface，零值是nil
- 结构体: var instance StructName，结构体里每个field的零值是对应field的类型的零值

### 常量

常量定义的时候必须赋值，定义后值不能被修改  
常量(包括全局常量和局部常量)可以定义后不使用，局部变量定义后必须使用，否则编译报错  
常量可以用来定义枚举  
```go
const a int = 10
const b bool = false
// 
const a = 10
const b = false
// 
const a, b int = 1, 2
```

定义枚举：
```go
const (
  unknown = 0
  male = 1
  female = 2
)
```

### iota

iota是go语言的常量计数器，只能在常量的表达式中使用。 iota在const关键字出现时将被重置为0。const中每新增一行常量声明将使iota计数一次(iota可理解为const语句块中的行索引)。 使用iota能简化定义，在定义枚举时很有用
```go
const (
	n1 = iota //0
	n2        //1
	n3        //2
	n4        //3
)
```
使用 `_`跳过某些值
```go
const (
	n1 = iota //0
	n2        //1
	_
	n4        //3
)
```
iota声明中间插队
```go
const (
	n1 = iota //0
	n2 = 100  //100
	n3 = iota //2
	n4        //3
)
const n5 = iota //0
```

## 输入输出

### 标准

```go
var (
   Stdin  = NewFile(uintptr(syscall.Stdin), "/dev/stdin")
   Stdout = NewFile(uintptr(syscall.Stdout), "/dev/stdout")
   Stderr = NewFile(uintptr(syscall.Stderr), "/dev/stderr")
)
```
在os包下有三个外暴露的文件描述符，其类型都是*File，分别是：
1. Stdin - 标准输入  
2. Stdout - 标准输出  
3. Stderr - 标准错误  

### 输出

比较常用的有三种方法：  
第一种是调用os.Stdout
```go
os.Stdout.WriteString("Hello 世界!")
```
第二种是使用内置函数println
```go
println("Hello 世界!")
```
第三种也是最推荐的一种就是调用fmt包下的Println函数
```go
fmt.Println("Hello 世界!")
```
`fmt.Println`会用到反射，因此输出的内容通常更容易使人阅读，不过性能很差强人意。

### fmt格式化输出

下面是 Go 目前所有的格式化动词。

| 0   | 格式化    | 描述                                            | 接收类型           |
| --- | --------- | ----------------------------------------------- | ------------------ |
| 1   | **`%%`**  | 输出百分号`%`                                   | 任意               |
| 2   | **`%s`**  | 输出`string`/`[] byte`值                        | `string`,`[] byte` |
| 3   | **`%q`**  | 格式化字符串，输出的字符串两端有双引号`""`      | `string`,`[] byte` |
| 4   | **`%d`**  | 输出十进制整型值                                | 整型               |
| 5   | **`%f`**  | 输出浮点数                                      | 浮点               |
| 6   | **`%e`**  | 输出科学计数法形式 ,也可以用于复数              | 浮点               |
| 7   | **`%E`**  | 与`%e`相同                                      | 浮点               |
| 8   | **`%g`**  | 根据实际情况判断输出`%f`或者`%e`,会去掉多余的 0 | 浮点               |
| 9   | **`%b`**  | 输出整型的二进制表现形式                        | 数字               |
| 10  | **`%#b`** | 输出二进制完整的表现形式                        | 数字               |
| 11  | **`%o`**  | 输出整型的八进制表示                            | 整型               |
| 12  | **`%#o`** | 输出整型的完整八进制表示                        | 整型               |
| 13  | **`%x`**  | 输出整型的小写十六进制表示                      | 数字               |
| 14  | **`%#x`** | 输出整型的完整小写十六进制表示                  | 数字               |
| 15  | **`%X`**  | 输出整型的大写十六进制表示                      | 数字               |
| 16  | **`%#X`** | 输出整型的完整大写十六进制表示                  | 数字               |
| 17  | **`%v`**  | 输出值原本的形式，多用于数据结构的输出          | 任意               |
| 18  | **`%+v`** | 输出结构体时将加上字段名                        | 任意               |
| 19  | **`%#v`** | 输出完整 Go 语法格式的值                        | 任意               |
| 20  | **`%t`**  | 输出布尔值                                      | 布尔               |
| 21  | **`%T`**  | 输出值对应的 Go 语言类型值                      | 任意               |
| 22  | **`%c`**  | 输出 Unicode 码对应的字符                       | `int32`            |
| 23  | **`%U`**  | 输出字符对应的 Unicode 码                       | `rune`,`byte`      |
| 24  | **`%p`**  | 输出指针所指向的地址                            | 指针               |

在使用数字时，还可以自动补零。比如
```go
fmt.Printf("%09d", 1)
// 000000001
```
二进制同理
```go
fmt.Printf("%09b", 1<<3)
// 000001000
```

### 输入

**read:** 可以像直接读文件一样，读取输入内容，如下
```go
func main() {
  var buf [1024]byte
  n, _ := os.Stdin.Read(buf[:])
  os.Stdout.Write(buf[:n])
}
```

**fmt**:可以使用`fmt`包提供的几个函数，用起来跟 C 差不多。
```go
// 扫描从os.Stdin读入的文本，根据空格分隔，换行也被当作空格
func Scan(a ...any) (n int, err error)
// 与Scan类似，但是遇到换行停止扫描
func Scanln(a ...any) (n int, err error)
// 根据格式化的字符串扫描
func Scanf(format string, a ...any) (n int, err error)
```
> 需要注意的是，Go中输入的默认分隔符号是空格

读取两个数字
```go
func main() {
  var a, b int
  fmt.Scanln(&a, &b)
  fmt.Printf("%d + %d = %d\n", a, b, a+b)
}
```
读取固定长度的数组
```go
func main() {
  n := 10
  s := make([]int, n)
  for i := range n {
    fmt.Scan(&s[i])
  }
  fmt.Println(s)
}
```
```
1 2 3 4 5 6 7 8 9 10
[1 2 3 4 5 6 7 8 9 10]
```
```go
func main() {
   var s, s2, s3 string
   scanf, err := fmt.Scanf("%s %s \n %s", &s, &s2, &s3)
   if err != nil {
      fmt.Println(scanf, err)
   }
   fmt.Println(s)
   fmt.Println(s2)
   fmt.Println(s3)
}
```

**bufio**:在有大量输入需要读取的时候，就建议使用`bufio.Reader`来进行内容读取
```go
func main() {
    reader := bufio.NewReader(os.Stdin)
    var a, b int
    fmt.Fscanln(reader, &a, &b)
    fmt.Printf("%d + %d = %d\n", a, b, a+b)
}
// 按行读取
func main() {
   // 读
    scanner := bufio.NewScanner(os.Stdin)
    for scanner.Scan() {
        line := scanner.Text()
        if line == "exit" {
        break
        }
        fmt.Println("scan", line)
    }
}
func main() {
   // 写
   writer := bufio.NewWriter(os.Stdout)
   writer.WriteString("hello world!\n")
   writer.Flush()
   fmt.Println(writer.Buffered())
}
```

## 数组

golang 数组特性：  
1. 数组：是同一种数据类型的固定长度的序列。
2. 数组定义：`var a [len]int`，比如：`var a [5]int`，数组长度必须是常量，且是类型的组成部分。一旦定义，长度不能变。
3. 长度是数组类型的一部分，因此，`var a[5] int`和`var a[10]int`是不同的类型。
4. 数组可以通过下标进行访问，下标是从0开始，最后一个元素下标是：len-1
```go
for i := 0; i < len(a); i++ {
}
for index, v := range a {
}
```
5. 访问越界，如果下标在数组合法范围之外，则触发访问越界，会panic
6. 数组是值类型，赋值和传参会复制整个数组，而不是指针。因此改变副本的值，不会改变本身的值。
7. 支持 "=="、"!=" 操作符，因为内存总是被初始化过的。
8. 指针数组 `[n]*T`，数组指针 `*[n]T`。

### 数组初始化

全局：
```go
var arr0 [5]int = [5]int{1, 2, 3} // 未初始化元素值为 0。
var arr1 = [5]int{1, 2, 3, 4, 5}
var arr2 = [...]int{1, 2, 3, 4, 5, 6} // 通过初始化值确定数组长度。
var str = [5]string{3: "hello world", 4: "tom"} // 使用索引号初始化元素
```
局部：
```go
a := [3]int{1, 2}           // 未初始化元素值为 0。
b := [...]int{1, 2, 3, 4}   // 通过初始化值确定数组长度。
c := [5]int{2: 100, 4: 200} // 使用索引号初始化元素。
d := [...]struct {
	name string
	age  uint8
}{
	{"user1", 10}, // 可省略元素类型。
	{"user2", 20}, // 别忘了最后一行的逗号。
}
```

### 多维数组

```go
// 全局
var arr0 [5][3]int
var arr1 [2][3]int = [...][3]int{{1, 2, 3}, {7, 8, 9}}
// 局部：
a := [2][3]int{{1, 2, 3}, {4, 5, 6}}
b := [...][2]int{{1, 1}, {2, 2}, {3, 3}} // 第 2 纬度不能用 "..."。
```

## 切片 Slice

slice 并不是数组或数组指针。切片是对数组的抽象。Go数组的长度在定义后是固定的，不可改变的。  
切片的长度和容量是不固定的，可以动态增加元素，切片的容量也会根据情况自动扩容，它通过内部指针和相关属性引用数组片段，以实现变长方案；   
1. 切片：切片是数组的一个引用，因此切片是引用类型。但自身是结构体，值拷贝传递。
2. 切片的长度可以改变，因此，切片是一个可变的数组。
3. 切片遍历方式和数组一样，可以用len()求长度。表示可用元素数量，读写操作不能超过该限制。 
4. cap可以求出slice最大扩张容量，不能超出数组限制。0 <= len(slice) <= len(array)，其中array是slice引用的数组。
5. 切片的定义：var 变量名 []类型，比如 var str []string  var arr []int。
6. 如果 slice == nil，那么 len、cap 结果都等于 0。

### 创建切片

```go
//1.声明切片
var s1 []int
assert.True(t, s1 == nil)
// 2.:=
s2 := []int{}
// 3.make()
var s3 []int = make([]int, 0)
fmt.Println(s1, s2, s3)
// 4.初始化赋值
var s4 []int = make([]int, 0, 0)
s5 := []int{1, 2, 3}
// 5.从数组切片
arr := [5]int{1, 2, 3, 4, 5}
var s6 []int
// 前包后不包
s6 = arr[1:4]
```

### 切片初始化

如果slice类型的变量定义后没有初始化赋值，那值就是默认值`nil`。对于`nil`切片，len和cap函数执行结果都是0
```go
// 全局：
var arr = [...]int{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}
var slice0 []int = arr[start:end] 
var slice1 []int = arr[:end]        
var slice2 []int = arr[start:]        
var slice3 []int = arr[:] 
var slice4 = arr[:len(arr)-1]      //去掉切片的最后一个元素
// 局部：
arr2 := [...]int{9, 8, 7, 6, 5, 4, 3, 2, 1, 0}
slice5 := arr[start:end]
slice6 := arr[:end]        
slice7 := arr[start:]     
slice8 := arr[:]  
slice9 := arr[:len(arr)-1] //去掉切片的最后一个元素
```

| 操作              | 含义                                                     |
| ----------------- | -------------------------------------------------------- |
| s[n]              | 切片 s 中索引位置为 n 的项                               |
| s[:]              | 从切片 s 的索引位置 0 到 len(s)-1 处所获得的切片         |
| s[low:]           | 从切片 s 的索引位置 low 到 len(s)-1 处所获得的切片       |
| s[:high]          | 从切片 s 的索引位置 0 到 high 处所获得的切片，len=high   |
| s[low:high]       | 从切片 s 的索引位置 low 到 high 处所获得的切片，len=high-low |
| s[low:high:max]   | 从切片 s 的索引位置 low 到 high 处所获得的切片，len=high-low，cap=max-low |
| len(s)            | 切片 s 的长度，总是<=cap(s)                              |
| cap(s)            | 切片 s 的容量，总是>=len(s)                              |

### 通过make来创建切片

```go
var slice []type = make([]type, len)
slice := make([]type, len)
slice := make([]type, len, cap)
// 示例
var slice0 []int = make([]int, 10)
var slice1 = make([]int, 10)
var slice2 = make([]int, 10, 10)
```
读写操作实际目标是底层数组，只需注意索引号的差别；
```go
s1 := []int{0, 1, 2, 3, 8: 100} // 通过初始化表达式构造，可使用索引号。
fmt.Println(s1, len(s1), cap(s1))
s2 := make([]int, 6, 8) // 使用 make 创建，指定 len 和 cap 值。
fmt.Println(s2, len(s2), cap(s2))
s3 := make([]int, 6) // 省略 cap，相当于 cap = len。
fmt.Println(s3, len(s3), cap(s3))
```
使用 make 动态创建 slice，避免了数组必须用常量做长度的麻烦。  
还可用指针直接访问底层数组，退化成普通数组操作
```go
s := []int{0, 1, 2, 3}
p := &s[2] // *int, 获取底层数组元素指针。
*p += 100
fmt.Println(s)
```

### 切片底层结构

切片slice是个struct结构体:
- 里面实际有个指针array，类型是`unsafe.Pointer`，也就是个指针，指向存放数据的数组。
- len 是切片的长度；
- cap 是切片的容量；
```go
type slice struct {
	array unsafe.Pointer
	len   int
	cap   int
}
```

### 常用函数

len()和cap()函数：类似C++的vector里的size和capacity
- len()：获取切片的长度，也就是实际存储了多少个元素
- cap(): 获取切片的容量。如果切片的元素个数要超过当前容量，会自动扩容

append()：通过append函数给切片加元素
- append不改变原切片的值；
- 只能对切片使用append()函数，不能对数组使用append()
```go
slice := []int{1, 2, 3}
// 往原切片里加一个元素
test := append(slice, 4)
// append不会改变slice的值，除非把append的结果重新赋值给slice
fmt.Println(slice) // [1 2 3]
fmt.Println(test) // [1 2 3 4]

// 通过append给切片添加切片
temp := []int{1,2}
test = append(test, temp...) // 注意，第2个参数有...结尾
fmt.Println(test) // [1 2 3 4 1 2]

/*下面对array数组做append就会报错:  first argument to append must be slice; have [3]int
array := [3]int{1, 2, 3}
array2 := append(array, 1)
fmt.Println(array2)
*/
```
copy()：拷贝一个切片里的数据到另一个切片  
- 只从源切片srcSlice拷贝min(len(srcSlice), len(dstSlice))个元素到目标切片dstSlice里。如果dstSlice的长度是0，那一个都不会从srcSlice拷贝到dstSlice里。如果dstSlice的长度M小于srcSlice的长度N，则只会拷贝srcSlice里的前M个元素到目标切片dstSlice里

### 作为函数参数

slice切片如果是函数参数，函数体内对切片底层数组的修改会影响到实参；  
如果在函数体内通过append直接对切片添加新元素，不会改变外部切片的值；但是如果函数使用切片指针作为参数，在函数体内可以通过切片指针修改外部切片的值
```go
func change1(param []int) {
	param[0] = 100             // 这个会改变外部切片的值
	param = append(param, 200) // append不会改变外部切片的值
}
func change2(param *[]int) {
	*param = append(*param, 300) // 传切片指针，通过这种方式append可以改变外部切片的值
}
```

### 数组与slice

下面将 **Go 语言中数组（array）与切片（slice）的差异** 以一张对比表的形式进行总结，便于快速查阅与记忆。

---

### 数组（array） vs 切片（slice）差异对比表

| 对比维度        | 数组（array）              | 切片（slice）                        |
| ----------- | ---------------------- | -------------------------------- |
| 定义方式        | `var a [3]int`         | `var s []int` / `make([]int, 3)` |
| 长度是否固定      | **固定，编译期确定**           | **不固定，运行期可变**                    |
| 长度是否是类型一部分  | 是（`[3]int` ≠ `[4]int`） | 否（`[]int` 是统一类型）                 |
| 类型语义        | 值类型                    | 引用语义（底层数组的描述符）                   |
| 赋值/函数传参     | 发生整体拷贝                 | 拷贝 slice 头，底层数据共享                |
| 是否支持扩容      | 不支持                    | 支持 `append`                      |
| 是否可基于其创建    | 可被 slice 引用            | 可基于 array / slice 再切片            |
| 底层存储        | 自身即数据                  | 指向底层 array                       |
| 是否共享内存      | 否                      | 是（除非发生扩容）                        |
| `len()`     | 固定值                    | 可变                               |
| `cap()`     | 等于长度                   | ≥ `len`，可能变化                     |
| 是否支持比较 `==` | 支持（元素可比较）              | 不支持（只能与 `nil` 比较）                |
| 零值          | 所有元素为零值                | `nil`                            |
| 常见使用场景      | 固定大小缓冲区、底层结构           | 业务集合、函数参数、动态数据                   |
| 工程实践使用频率    | 很少                     | **极高（Go 中最常用）**                  |

> **array 是“定长值对象”，slice 是“可变长度、共享底层数组的集合抽象”，实际开发中几乎总是使用 slice。**

数组是定长的值类型，长度是类型的一部分；切片是对数组的一个轻量级引用，长度可变，支持扩容，是 Go 中最常用的集合抽象

## 指针

区别于C/C++中的指针，Go语言中的指针不能进行偏移和运算，是安全指针。Go语言中的指针操作非常简单，只需要记住两个符号：&（取地址）和*（根据地址取值）

### 指针地址和指针类型

每个变量在运行时都拥有一个地址，这个地址代表变量在内存中的位置。Go语言中使用`&`字符放在变量前面对变量进行“取地址”操作。 Go语言中的值类型（int、float、bool、string、array、struct）都有对应的指针类型，如：`*int`、`*int64`、`*string`等
```go
ptr := &v    // v的类型为T
// v: 代表被取地址的变量，类型为T
// ptr:用于接收地址的变量，ptr的类型就为 *T，称做 T的 指针类型。*代表指针。
func main() {
    a := 10
    b := &a
    fmt.Printf("a:%d ptr:%p\n", a, &a) // a:10 ptr:0xc00001a078
    fmt.Printf("b:%p type:%T\n", b, b) // b:0xc00001a078 type:*int
    fmt.Println(&b)                    // 0xc00000e018
}
```

### 指针取值

在对普通变量使用`&`操作符取地址后会获得这个变量的指针，然后可以对指针使用`*`操作，也就是指针取值，代码如下
```go
func main() {
    //指针取值
    a := 10
    b := &a // 取变量a的地址，将指针保存到b中
    fmt.Printf("type of b:%T\n", b) // type of b:*int
    c := *b // 指针取值（根据指针去内存取值）
    fmt.Printf("type of c:%T\n", c) // type of c:int
    fmt.Printf("value of c:%v\n", c)
}
```
变量、指针地址、指针变量、取地址、取值的相互关系和特性如下:    
1. 对变量进行取地址（&）操作，可以获得这个变量的指针变量。
2. 指针变量的值是指针地址。
3. 对指针变量进行取值（*）操作，可以获得指针变量指向的原变量的值。

### 空指针

- 当一个指针被定义后没有分配到任何变量时，它的值为 nil
- 空指针的判断
```go
func main() {
    var p *string
    fmt.Println(p)
    fmt.Printf("p的值是%s/n", p)
    if p != nil {
        fmt.Println("非空")
    } else {
        fmt.Println("空值")
    }
}
```

### new 和 make

```go
var a *int
*a = 100
fmt.Println(*a)
// panic: runtime error: invalid memory address or nil pointer 
```
在Go语言中对于引用类型的变量，在使用的时候不仅要声明它，还要为它分配内存空间，否则值就没办法存储。而对于值类型的声明不需要分配内存空间，因为它们在声明的时候已经默认分配好了内存空间。  
要分配内存，Go语言中有 new和make是两个内建函数，用来分配内存

#### new函数

new是一个内置的函数，它的函数签名如下：
```go
func new(Type) *Type
```
1. `Type`表示类型，`new`函数只接受一个参数，这个参数是一个类型；
2. `*Type`表示类型指针，`new`函数返回一个指向该类型内存地址的指针。  

new 函数不太常用，使用new函数得到的是一个类型的指针，并且该指针对应的值为该类型的零值
```go
func main() {
    a := new(int)
    b := new(bool)
    fmt.Printf("%T\n", a) // *int
    fmt.Printf("%T\n", b) // *bool
    fmt.Println(*a)       // 0
    fmt.Println(*b)       // false
}
```
`var a *int`只是声明了一个指针变量a但是没有初始化，指针作为引用类型需要初始化后才会拥有内存空间，才可以给它赋值。应该按照如下方式使用内置的new函数对a进行初始化之后就可以正常对其赋值了：
```go
var a *int
a = new(int)
*a = 10
fmt.Println(*a)
```
对比 C++：  
1. Go的 new 分配的内存可能在栈(stack)上，可能在堆(heap)上。C++ new分配的内存一定在堆上。  
2. Go的 new 分配的内存里的值是对应类型的零值，不能显式初始化指定要分配的值。C++ new 分配内存的时候可以显式指定要存储的值。
3. Go里没有构造函数，Go的 new 不会去调用构造函数。C++的 new 是会调用对应类型的构造函数

#### make

make也是用于内存分配的，区别于new，它只用于 slice、map 以及 chan 的内存创建（如果是用于slice类型，make函数的第2个参数表示slice的长度，这个参数必须给值），而且它返回的类型就是这三个类型本身，而不是他们的指针类型，因为这三种类型就是引用类型。make函数的函数签名如下
```go
func make(t Type, size ...IntegerType) Type
```
make函数是无可替代的，我们在使用slice、map以及channel的时候，都需要使用make进行初始化，然后才可以对它们进行操作
```go
var b map[string]int // 声明完之间执行： b["测试"] = 100，会报错：panic: assignment to entry in nil map
b = make(map[string]int, 10)
b["测试"] = 100
fmt.Println(b)
```

#### 总结

为什么针对slice, map和chan类型专门定义一个make函数？  

	这是因为slice, map和chan的底层结构上要求在使用slice，map和chan的时候必须初始化，如果不初始化，那slice，map和chan的值就是零值，也就是nil

可以用new来创建slice, map和chan么？可以，
```go
a := *new([]int)
fmt.Printf("%T, %v\n", a, a==nil)
b := *new(map[string]int)
fmt.Printf("%T, %v\n", b, b==nil)
c := *new(chan int)
fmt.Printf("%T, %v\n", c, c==nil)
```
为什么slice是nil也可以直接append? 对于nil slice，append会对slice的底层数组做扩容，通过调用mallocgc向Go的内存管理器申请内存空间，再赋值给原来的nil slice

## Map

### 定义

map是一种无序的基于key-value的数据结构，Go语言中的map是引用类型，必须初始化才能使用
```go
var map_var map[KeyType]ValueType = map[KeyType]ValueType{}
var map_var = map[KeyType]ValueType{}
map_var := map[KeyType]ValueType{}
/*cap是map容量，超过后会自动扩容*/
map_var := make(map[KeyType]ValueType, [cap]) 
map[KeyType]ValueType
// KeyType: 表示键的类型。
// ValueType: 表示键对应的值的类型。
```
map类型的变量默认初始值为nil，需要使用make()函数来分配内存。语法为：
```go
make(map[KeyType]ValueType, [cap])
// 其中 cap 表示 map的容量，该参数虽然不是必须的，但是我们应该在初始化map的时候就为其指定一个合适的容量
```

### 基本使用

```go
scoreMap := make(map[string]int, 8)
scoreMap["张三"] = 90
scoreMap["小明"] = 100
fmt.Println(scoreMap)
fmt.Println(scoreMap["小明"])
fmt.Printf("type of a:%T\n", scoreMap)
```
map也支持在声明的时候填充元素，例如：
```go
userInfo := map[string]string{
	"username": "pprof.cn",
	"password": "123456",
}
fmt.Println(userInfo)
```

### 判断 key 是否存在

Go语言中有个判断map中键是否存在的特殊写法，格式如下:
```go
value, ok := map[key]
// 示例：
scoreMap := make(map[string]int)
scoreMap["张三"] = 90
scoreMap["小明"] = 100
// 如果key存在ok为true,v为对应的值；不存在ok为false,v为值类型的零值
v, ok := scoreMap["张三"]
if ok {
	fmt.Println(v)
} else {
	fmt.Println("查无此人")
}
```

### 遍历

Go语言中使用for range遍历map
```go
scoreMap := make(map[string]int)
scoreMap["张三"] = 90
scoreMap["小明"] = 100
scoreMap["王五"] = 60
// 遍历 k,v
for k, v := range scoreMap {
	fmt.Println(k, v)
}
// 只遍历 key
for k := range scoreMap {
	fmt.Println(k)
}
// 遍历 value，忽略 key
for _, v := range scoreMap {
	fmt.Println(v)
}
```
注意： 遍历map时的元素顺序与添加键值对的顺序无关

### 删除

使用delete()内建函数从map中删除一组键值对，delete()函数的格式如下：
```go
delete(map, key)
// map:表示要删除键值对的map
// key:表示要删除的键值对的键
```

### 按照指定顺序遍历map

```go
func main() {
    r := rand.New(rand.NewSource(time.Now().UnixNano()))
	var scoreMap = make(map[string]int, 200)
	for i := 0; i < 100; i++ {
		key := fmt.Sprintf("stu%02d", i) //生成stu开头的字符串
		value := r.Intn(100)             //生成0~99的随机整数
		scoreMap[key] = value
	}
	//取出map中的所有key存入切片keys
	var keys = make([]string, 0, 200)
	for key := range scoreMap {
		keys = append(keys, key)
	}
	//对切片进行排序
	sort.Strings(keys)
	//按照排序后的key遍历map
	for _, key := range keys {
		fmt.Println(key, scoreMap[key])
	}
}
```

### 注意事项

- key必须支持==和!=比较，才能用作map的key。  
因此切片slice，函数类型function，集合map，不能用作map的key
- map不是并发安全的，并发读写要加锁

## 结构体

Go语言中没有“类”的概念，也不支持“类”的继承等面向对象的概念。Go语言中通过结构体的内嵌再配合接口比面向对象具有更高的扩展性和灵活性

### 自定义类型与类型别名

**自定义类型**

在Go语言中有一些基本的数据类型，如string、整型、浮点型、布尔等数据类型，Go语言中可以使用type关键字来定义自定义类型。

自定义类型是定义了一个全新的类型。我们可以基于内置的基本类型定义，也可以通过struct定义
```go
//将MyInt定义为int类型
type MyInt int
```

**类型别名**

类型别名规定：TypeAlias只是Type的别名，本质上TypeAlias与Type是同一个类型
```go
type byte = uint8
type rune = int32
```

**类型定义和类型别名的区别**
```go
//类型定义
type NewInt int
//类型别名
type MyInt = int
func main() {
    var a NewInt
    var b MyInt
    fmt.Printf("type of a:%T\n", a) //type of a:main.NewInt
    fmt.Printf("type of b:%T\n", b) //type of b:int
}
```
结果显示a的类型是`main.NewInt`，表示main包下定义的NewInt类型。b的类型是int。MyInt类型只会在代码中存在，编译完成时并不会有MyInt类型

### 结构体的定义

使用type和struct关键字来定义结构体，具体代码格式如下：
```go
type 类型名 struct {
	字段名 字段类型
	字段名 字段类型
	…
}
```
1. 类型名：标识自定义结构体的名称，在同一个包内不能重复。
2. 字段名：表示结构体字段名。结构体中的字段名必须唯一。
3. 字段类型：表示结构体字段的具体类型。
4. 同样类型的字段也可以写在一行
5. 结构体中字段大写开头表示可公开访问，小写表示私有（仅在定义当前结构体的包中可访问）

### 实例化

只有当结构体实例化时，才会真正地分配内存。也就是必须实例化后才能使用结构体的字段。

结构体本身也是一种类型，我们可以像声明内置类型一样使用var关键字声明结构体类型。
```go
var 结构体实例 结构体类型
// 示例
type person struct {
    name string
    city string
    age  int8
}
func main() {
    var p1 person
    fmt.Printf("p1=%#v\n", p1) // p1=main.person{name:"", city:"", age:0}
    p1.name = "pprof.cn"
    p1.city = "北京"
    p1.age = 18
    fmt.Printf("p1=%v\n", p1)  //p1={pprof.cn 北京 18}
    fmt.Printf("p1=%#v\n", p1) //p1=main.person{name:"pprof.cn", city:"北京", age:18}
}
```

#### 指针类型结构体

可以通过使用new关键字对结构体进行实例化，得到的是结构体的地址。 格式如下：
```go
var p2 = new(person)
fmt.Printf("%T\n", p2)     //*main.person, p2是一个结构体指针
fmt.Printf("p2=%#v\n", p2) //p2=&main.person{name:"", city:"", age:0}
```
在Go语言中支持对结构体指针直接使用.来访问结构体的成员。
```go
var p2 = new(person)
p2.name = "测试"
p2.age = 18
p2.city = "北京"
fmt.Printf("p2=%#v\n", p2) //p2=&main.person{name:"测试", city:"北京", age:18}
```

#### 取结构体的地址实例化

使用`&`对结构体进行取地址操作相当于对该结构体类型进行了一次new实例化操作。
```go
p3 := &person{}
fmt.Printf("%T\n", p3)     //*main.person
fmt.Printf("p3=%#v\n", p3) //p3=&main.person{name:"", city:"", age:0}
p3.name = "博客" // p3.name = "博客", 其实在底层是(*p3).name = "博客"，这是Go语言帮我们实现的语法糖
p3.age = 30
p3.city = "成都"
fmt.Printf("p3=%#v\n", p3) //p3=&main.person{name:"博客", city:"成都", age:30}
```

#### 使用键值对初始化

使用键值对对结构体进行初始化时，键对应结构体的字段，值对应该字段的初始值
```go
p5 := person{
    name: "pprof.cn",
    city: "北京",
    age:  18,
}
fmt.Printf("p5=%#v\n", p5) //p5=main.person{name:"pprof.cn", city:"北京", age:18}
```
也可以对结构体指针进行键值对初始化，例如：
```go
p6 := &person{
    name: "pprof.cn",
    city: "北京",
    age:  18,
}
fmt.Printf("p6=%#v\n", p6) //p6=&main.person{name:"pprof.cn", city:"北京", age:18}
```
当某些字段没有初始值的时候，该字段可以不写。此时，没有指定初始值的字段的值就是该字段类型的零值

#### 使用值的列表初始化

初始化结构体的时候可以简写，也就是初始化的时候不写键，直接写值：
```go
p8 := &person{
    "pprof.cn",
    "北京",
    18,
}
fmt.Printf("p8=%#v\n", p8) //p8=&main.person{name:"pprof.cn", city:"北京", age:18}
```
需要注意的是：  
1. 必须初始化结构体的所有字段。
2. 初始值的填充顺序必须与字段在结构体中的声明顺序一致。
3. 该方式不能和键值初始化方式混用。

### 匿名结构体

```go
var user struct{Name string; Age int}
user.Name = "pprof.cn"
user.Age = 18
fmt.Printf("%#v\n", user)
```

### 结构体内存布局

```go
type test struct {
    a int8
    b int8
    c int8
    d int8
}
n := test{
    1, 2, 3, 4,
}
fmt.Printf("n.a %p\n", &n.a) // n.a 0x14000186074
fmt.Printf("n.b %p\n", &n.b) // n.b 0x14000186075
fmt.Printf("n.c %p\n", &n.c) // n.c 0x14000186076
fmt.Printf("n.d %p\n", &n.d) // n.d 0x14000186077
```

### 构造函数

Go语言的结构体没有构造函数，可以自己实现。例如，下方的代码就实现了一个person的构造函数。因为struct是值类型，如果结构体比较复杂的话，值拷贝性能开销会比较大，所以该构造函数返回的是结构体指针类型。
```go
func newPerson(name, city string, age int8) *person {
    return &person{
        name: name,
        city: city,
        age:  age,
    }
}
```

### 方法和接收者

Go语言中的方法（Method）是一种作用于特定类型变量的函数。这种特定类型变量叫做`接收者（Receiver）`。接收者的概念就类似于其他语言中的this或者 self
```go
func (接收者变量 接收者类型) 方法名(参数列表) (返回参数) {
	函数体
}
```
1. 接收者变量：接收者中的参数变量名在命名时，官方建议使用接收者类型名的第一个小写字母，而不是self、this之类的命名。例如，Person类型的接收者变量应该命名为 p，Connector类型的接收者变量应该命名为c等。
2. 接收者类型：接收者类型和参数类似，可以是指针类型和非指针类型。
3. 方法名、参数列表、返回参数：具体格式与函数定义相同。
```go
//Person 结构体
type Person struct {
    name string
    age  int8
}
//Dream Person做梦的方法
func (p Person) Dream() {
    fmt.Printf("%s的梦想是学好Go语言！\n", p.name)
}

func main() {
    p1 := Person{"测试", 25}
    p1.Dream()
}
```
方法与函数的区别是：函数不属于任何类型，方法属于特定的类型

#### 指针类型的接收者

指针类型的接收者由一个结构体的指针组成，由于指针的特性，调用方法时修改接收者指针的任意成员变量，在方法结束后，修改都是有效的。这种方式就十分接近于其他语言中面向对象中的this或者self。 例如为Person添加一个SetAge方法，来修改实例变量的年龄。
```go
// SetAge 设置p的年龄
// 使用指针接收者
func (p *Person) SetAge(newAge int8) {
	p.age = newAge
}
```
**什么时候应该使用指针类型接收者**
1. 需要修改接收者中的值
2. 接收者是拷贝代价比较大的大对象
3. 保证一致性，如果有某个方法使用了指针接收者，那么其他的方法也应该使用指针接收者。

#### 值类型的接收者

当方法作用于值类型接收者时，Go语言会在代码运行时将接收者的值复制一份。在值类型接收者的方法中可以获取接收者的成员值，但修改操作只是针对副本，无法修改接收者变量本身
```go
// SetAge2 设置p的年龄
// 使用值接收者
func (p Person) SetAge2(newAge int8) {
    p.age = newAge
}
func main() {
    p1 := NewPerson("测试", 25)
    p1.Dream()
    fmt.Println(p1.age) // 25
    p1.SetAge2(30) // (*p1).SetAge2(30)
    fmt.Println(p1.age) // 25
}
```

### 任意类型添加方法

在Go语言中，接收者的类型可以是任何类型，不仅仅是结构体，任何类型都可以拥有方法
```go
//MyInt 将int定义为自定义MyInt类型
type MyInt int
//SayHello 为MyInt添加一个SayHello的方法
func (m MyInt) SayHello() {
    fmt.Println("Hello, 我是一个int。")
}
func main() {
    var m1 MyInt
    m1.SayHello() //Hello, 我是一个int。
    m1 = 100
    fmt.Printf("%#v  %T\n", m1, m1) //100  main.MyInt
}
```

### 结构体的匿名字段

结构体允许其成员字段在声明时没有字段名而只有类型，这种没有名字的字段就称为匿名字段
```go
//Person 结构体Person类型
type Person struct {
    string
    int
}
func main() {
    p1 := Person{"pprof.cn", 18}
    fmt.Printf("%#v\n", p1)        //main.Person{string:"pprof.cn", int:18}
    fmt.Println(p1.string, p1.int) //pprof.cn 18
}
```
匿名字段默认采用类型名作为字段名，结构体要求字段名称必须唯一，因此一个结构体中同种类型的匿名字段只能有一个

### 嵌套结构体

一个结构体中可以嵌套包含另一个结构体或结构体指针
```go
//Address 地址结构体
type Address struct {
    Province string
    City     string
}
//User 用户结构体
type User struct {
    Name    string
    Gender  string
    Address Address
}
func main() {
    user1 := User{
        Name:   "pprof", Gender: "女",
        Address: Address{Province: "黑龙江",City:     "哈尔滨",},
    }
    fmt.Printf("user1=%#v\n", user1)//user1=main.User{Name:"pprof", Gender:"女", Address:main.Address{Province:"黑龙江", City:"哈尔滨"}}
}
```

### tag

Tag是结构体的元信息，可以在运行的时候通过反射的机制读取出来。主要用于在运行时通过反射读取，以指导序列化、反序列化、校验、ORM 映射等框架行为

Tag在结构体字段的后方定义，由一对反引号包裹起来，具体的格式如下： `key1:"value1" key2:"value2"`

结构体标签由一个或多个键值对组成。键与值使用冒号分隔，值用双引号括起来。键值对之间使用一个空格分隔。   
注意事项： 为结构体编写Tag时，必须严格遵守键值对的规则。结构体标签的解析代码的容错能力很差，一旦格式写错，编译和运行时都不会提示任何错误，通过反射也无法正确取值。例如不要在key和value之间添加空格
```go
//Student 学生
type Student struct {
    ID     int    `json:"id"` //通过指定tag实现json序列化该字段时的key
    Gender string //json序列化是默认使用字段名作为key
    name   string //私有不能被json包访问
}
func main() {
    s1 := Student{
        ID:     1,
        Gender: "女",
        name:   "pprof",
    }
    data, err := json.Marshal(s1)
    if err != nil {
        fmt.Println("json marshal failed!")
        return
    }
    fmt.Printf("json str:%s\n", data) //json str:{"id":1,"Gender":"女"}
}
```
tag 的核心作用：告诉“外部系统”如何理解字段

#### 最常见的几类 tag

**JSON（encoding/json）**
```go
type User struct {
    ID     int    `json:"id"`
    Name   string `json:"name"`
    Email  string `json:"email,omitempty"` //  omitempty, 零值时不输出
    Secret string `json:"-"` // 忽略该字段
}
// json:",string" 数值转字符串
```

**数据库 / ORM（如 GORM）**: tag 描述的是字段如何映射到数据库列
```go
type User struct {
    ID    int64  `gorm:"column:user_id;primaryKey"`
    Name  string `gorm:"size:64;not null"`
}
```

**参数校验（validator）**
```go
type User struct {
    Email string `validate:"required,email"`
    Age   int    `validate:"gte=18"`
}
```

**表单 / URL 参数**：常见于 Gin、Echo 等 Web 框架
```go
type LoginReq struct {
    Username string `form:"username" query:"username"`
    Password string `form:"password"`
}
```

#### tag 的读取方式（反射原理）

**通过 reflect 读取 tag**
```go
t := reflect.TypeOf(User{})
field, _ := t.FieldByName("Name")
fmt.Println(field.Tag.Get("json")) // 输出: name
```
Tag 的类型:`type StructTag string`，底层就是字符串，标准库仅提供解析工具

#### 注意事项

1. tag 只在运行时可用，编译期无法通过 tag 控制逻辑
2. tag 不支持表达式："`json:"name" + suffix`"
3. tag 与字段可见性有关：只有导出字段（首字母大写）tag 才有意义
```go
type User struct {
    name string `json:"name"` // 无效，私有字段无法被反射导出
}
```

# 流程控制

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

### select 语句

select 语句类似于 switch 语句，但是select会随机执行一个可运行的case。如果没有case可运行，它将阻塞，直到有case可运行。

select是Go中的一个控制结构，类似于switch语句，用于处理异步IO操作。select会监听case语句中channel的读写操作，当case中channel读写操作为非阻塞状态（即能读写）时，将会触发相应的动作。 select中的case语句必须是一个channel操作

select中的default子句总是可运行的。  
如果有多个case都可以运行，select会随机公平地选出一个执行，其他不会执行。  
如果没有可运行的case语句，且有default语句，那么就会执行default的动作。  
如果没有可运行的case语句，且没有default语句，select将阻塞，直到某个case通信可以运行
```go
select {
    case communication clause  :
       statement(s);      
    case communication clause  :
       statement(s);
    /* 你可以定义任意数量的 case */
    default : /* 可选 */
       statement(s);
}
```
1. 每个case都必须是一个通信
2. 所有channel表达式都会被求值
3. 所有被发送的表达式都会被求值
4. 如果任意某个通信可以进行，它就执行；其他被忽略。
5. 如果有多个case都可以运行，Select会随机公平地选出一个执行。其他不会执行。
6. 否则：
	- 如果有default子句，则执行该语句。
	- 如果没有default字句，select将阻塞，直到某个通信可以运行；Go不会重新对channel或值进行求值。

select可以监听channel的数据流动  
select的用法与switch语法非常类似，由select开始的一个新的选择块，每个选择条件由case语句来描述  
与switch语句可以选择任何使用相等比较的条件相比，select由比较多的限制，其中最大的一条限制就是每个case语句里必须是一个IO操作  
```go
select { //不停的在这里检测
    case <-chanl : //检测有没有数据可以读
    //如果chanl成功读取到数据，则进行该case处理语句
    case chan2 <- 1 : //检测有没有可以写
    //如果成功向chan2写入数据，则进行该case处理语句
    //假如没有default，那么在以上两个条件都不成立的情况下，就会在此阻塞//一般default会不写在里面，select中的default子句总是可运行的，因为会很消耗CPU资源
    default:
    //如果以上都没有符合条件，那么则进行default处理流程
    }
```

**典型用法：**
- 超时判断
- 退出
- 判断channel是否阻塞

## 循环

### for

```go
for init; condition; post { }
for condition { }
for { }
```
init： 一般为赋值表达式，给控制变量赋初值；  
condition： 关系表达式或逻辑表达式，循环控制条件；  
post： 一般为赋值表达式，给控制变量增量或减量。  
for语句执行过程如下：  
- ①先对表达式 init 赋初值；
- ②判别赋值表达式 init 是否满足给定 condition 条件，若其值为真，满足循环条件，则执行循环体内语句，然后执行 post，进入第二次循环，再判别 condition；否则判断 condition 的值为假，不满足条件，就终止for循环，执行循环体外语句。

### range

Golang range类似迭代器操作，返回 (索引, 值) 或 (键, 值)。

for 循环的 range 格式可以对 slice、map、数组、字符串等进行迭代循环。格式如下：
```go
for key, value := range oldMap {
    newMap[key] = value
}
```
可忽略不想要的返回值，或 "_" 这个特殊变量

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

# 函数

## 基本定义

使用关键字 func 定义函数，左大括号依旧不能另起一行，类型相同的相邻参数，参数类型可合并。 多返回值必须用括号
```go
func func_name([parameter list]) [return_types] {
  // do sth
}
// 无参数
func name() int {
  do sth
}
// 无返回值
func name(a int) {
  do sth
}
// 返回多个值
func name(a int) (int, string) {
  //do sth
}
func name(a, b int) (int, string) {
  //do sth
}
func name(a int, b string)(int, string) {
  //do sth
}
func name(a, b int, c, d string) (int, string) {
  //do sth
}
```
1. 不支持 重载 (overload)   
2. 不支持 默认参数 (default parameter)。

## 返回值命名

"_"标识符，用来忽略函数的某个返回值  
Go 的返回值可以被命名，并且就像在函数体开头声明的变量那样使用
```go
/*
函数add的返回值有2个，类型是int，标识符分别是c和d
可以在函数体内直接给c和d赋值，return后面可以带，也可以不带返回值
*/
func addAndSub(a int, b int) (c int, d int) {
	c = a + b
	d = a - b
	return // 这一行写为 return c, d 也可以
}
func main() {
	a1, b1 := 1, 2
	c1, d1 := addAndSub(a1, b1)
	/*输出结果是：3 -1*/
	fmt.Println(c1, d1)
}
```
- 函数的参数列表不允许部分形参有命名，部分形参没命名，如果违背这个原则，就会报如下的编译错误。
- 函数的返回值列表不允许部分返回值变量有命名，部分返回值变量没命名，如果违背这个原则，就会报如下的编译错误
```go
syntax error: mixed named and unnamed function parameters
```
> 要么都不命名，要么都命名(都命名的情况下，允许形参或者返回值变量使用_作为命名)。
- Golang返回值不能用容器对象接收多返回值。只能用多个变量，或 "_" 忽略；
- 命名返回参数可被同名局部变量遮蔽，此时需要显式返回；
- 命名返回参数允许 defer 延迟调用通过闭包读取和修改

## nil函数

函数也是一种类型，函数变量的默认值是nil，执行nil函数会引发panic
```go
var f func()
// f是一个函数类型，值是nil
// 编译正常，运行报错panic: runtime error: invalid memory address or nil pointer dereference
f() 
```

## [值传递](https://github.com/jincheng9/go-tutorial/tree/main/workspace/senior/p3)

Go语言里没有引用变量和引用传递  
在Go语言里，不可能有2个变量有相同的内存地址，也就不存在引用变量了。   
> 注意：这里说的是不可能2个变量有相同的内存地址，但是2个变量指向同一个内存地址是可以的，这2个是不一样的。参考下面的例子：
```go
func main() {
	a := 10
	var p1 *int = &a
	var p2 *int = &a
	fmt.Println("p1 value:", p1, " address:", &p1)
	fmt.Println("p2 value:", p2, " address:", &p2)
}
```
在Go语言里是不存在引用变量的，也就自然没有引用传递了

再看如下例子：
```go
func initMap(data map[string]int) {
	data = make(map[string]int)
	fmt.Println("in function initMap, data == nil:", data == nil)
}
func main() {
	var data map[string]int
	fmt.Println("before init, data == nil:", data == nil)
	initMap(data)
	fmt.Println("after init, data == nil:", data == nil)
}
```

## 函数高级用法

函数作为其它函数的实参：函数定义后可以作为另一个函数的实参，比如下例的函数realFunc作为函数calValue的实参
```go
// define function getSquareRoot1
func getSquareRoot1(x float64) float64 {
	return math.Sqrt(x)
}
// define a function variable
var getSquareRoot2 = func(x float64) float64 {
	return math.Sqrt(x)
}
// define a function type
type callback_func func(int) int
// function calValue accepts a function variable cb as its second argument
func calValue(x int, cb callback_func) int{
	fmt.Println("[func|calValue]")
	return cb(x)
}

func realFunc(x int) int {
	fmt.Println("[func|realFunc]callback function")
	return x*x
}
func main() {
	num := 100.00
	result1 := getSquareRoot1(num)
	result2 := getSquareRoot2(num)
	fmt.Println("result1=", result1)
	fmt.Println("result2=", result2)

	value := 81
	result3 := calValue(value, realFunc) // use function realFunc as argument of calValue
	fmt.Println("result3=", result3)
}
```

## 闭包

闭包是由函数及其相关引用环境组合而成的实体
```go
func f(i int) func() int {
    return func() int {
        i++
        return i
    }
}
```
函数f返回了一个函数，返回的这个函数，返回的这个函数就是一个闭包。这个函数中本身是没有定义变量i的，而是引用了它所在的环境（函数f）中的变量i
- 捕获的是“变量本身”，不是值:Go 闭包捕获的是变量的引用（准确说是地址）

当 Go 编译器发现：内部函数引用了外部变量，它会自动进行 逃逸分析（escape analysis）：

**使用场景**
1. 函数式选项（Functional Options）
```go
type Option func(*Server)

func WithTimeout(d time.Duration) Option {
    return func(s *Server) {
        s.timeout = d
    }
}
```
2. 延迟执行 / 回调
```go
func retry(fn func() error) {
    for i := 0; i < 3; i++ {
        if err := fn(); err == nil {
            return
        }
    }
}
```
3. 中间件（HTTP / RPC）
```go
func Logger(next http.Handler) http.Handler {
    return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
        log.Println(r.URL)
        next.ServeHTTP(w, r)
    })
}
```

## defer

含义：defer 用于注册一个函数调用，使其在包含该 defer 的函数返回之前执行；  
类似 Java 中的 try-finally 块（特别像 finally）  
> defer = 注册一个延迟执行的函数，在函数 return 执行后、真正退出前执行。

特性：
1. 关键字 defer 用于注册延迟调用。
2. 这些调用直到 return 前才被执。因此，可以用来做资源清理。
3. 多个defer语句，按先进后出的方式执行。
4. defer语句中的变量，在defer声明时就决定了。

### defer 的执行时机

Go 所有的 defer 都在 当前函数结束时按照 LIFO（栈）顺序执行：
```go
defer A()
defer B()
defer C()
// 退出函数时执行顺序：C, B, A
```
即：最后 defer 的最先执行，后面的语句会依赖前面的资源，因此如果先前面的资源先释放了，后面的语句就没法执行了

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

# 错误

在 Go 中的异常有三种级别：
- error：正常的流程出错，需要处理，直接忽略掉不处理程序也不会崩溃
- panic：很严重的问题，程序应该在处理完问题后立即退出
- fatal：非常致命的问题，程序应该立即退出

准确的来说，Go 语言并没有异常，它是通过错误来体现，同样的，Go 中也并没有try-catch-finally这种语句，大多数情况会将其作为函数的返回值来返回
```go
func main() {
  // 打开一个文件
  if file, err := os.Open("README.txt"); err != nil {
    fmt.Println(err)
        return
  }
    fmt.Println(file.Name())
}
```
错误中没有堆栈信息（需要第三方包解决或者自己封装）

## error

error 属于是一种正常的流程错误，它的出现是可以被接受的，大多数情况下应该对其进行处理，当然也可以忽略不管，error 的严重级别不足以停止整个程序的运行。error本身是一个预定义的接口，该接口下只有一个方法Error()，该方法的返回值是字符串，用于输出错误信息
```go
type error interface {
   Error() string
}
```

### 创建error

1. 第一种是使用errors包下的New函数
```go
err := errors.New("这是一个错误")
```
2. 第二种是使用fmt包下的`Errorf`函数，可以得到一个格式化参数的 error
```go
err := fmt.Errorf("这是%d个格式化参数的的错误", 1)
```
大部分情况，为了更好的维护性，一般都不会临时创建 error，而是会将常用的 error 当作全局变量使用，例如下方节选自`os\erros.go`文件的代码
```go
var (
  ErrInvalid = fs.ErrInvalid // "invalid argument"

  ErrPermission = fs.ErrPermission // "permission denied"
  ErrExist      = fs.ErrExist      // "file already exists"
  ErrNotExist   = fs.ErrNotExist   // "file does not exist"
  ErrClosed     = fs.ErrClosed     // "file already closed"

  ErrNoDeadline       = errNoDeadline()       // "file type does not support deadline"
  ErrDeadlineExceeded = errDeadlineExceeded() // "i/o timeout"
)
```

### 自定义错误

通过实现`Error()`方法，可以很轻易的自定义 error，例如erros包下的errorString就是一个很简单的实现
```go
func New(text string) error {
   return &errorString{text}
}
// errorString结构体
type errorString struct {
   s string
}
func (e *errorString) Error() string {
   return e.s
}
```
因为errorString实现太过于简单，表达能力不足，所以很多开源库包括官方库都会选择自定义 error，以满足不同的错误需求

### 传递

在一些情况中，调用者调用的函数返回了一个错误，但是调用者本身不负责处理错误，于是也将错误作为返回值返回，抛给上一层调用者，这个过程叫传递，错误在传递的过程中可能会层层包装，当上层调用者想要判断错误的类型来做出不同的处理时，可能会无法判别错误的类别或者误判，而链式错误正是为了解决这种情况而出现的
```go
type wrapError struct {
   msg string
   err error
}
func (e *wrapError) Error() string {
   return e.msg
}
func (e *wrapError) Unwrap() error {
   return e.err
}
```
wrapError同样实现了error接口，也多了一个方法Unwrap，用于返回其内部对于原 error 的引用，层层包装下就形成了一条错误链表，顺着链表上寻找，很容易就能找到原始错误。由于该结构体并不对外暴露，所以只能使用fmt.Errorf函数来进行创建，例如
```go
err := errors.New("这是一个原始错误")
wrapErr := fmt.Errorf("错误，%w", err)
```
使用时，必须使用`%w`格式动词，且参数只能是一个有效的 error

### 处理错误

错误处理中的最后一步就是如何处理和检查错误，`errors`包提供了几个方便函数用于处理错误。
```go
func Unwrap(err error) error
```
`errors.Unwrap()`函数用于解包一个错误链，其内部实现也很简单
```go
func Unwrap(err error) error {
   u, ok := err.(interface { // 类型断言，是否实现该方法
      Unwrap() error
   })
   if !ok { //没有实现说明是一个基础的error
      return nil
   }
   return u.Unwrap() // 否则调用Unwrap
}
```
解包后会返回当前错误链所包裹的错误，被包裹的错误可能依旧是一个错误链，如果想要在错误链中找到对应的值或类型，可以递归进行查找匹配，不过标准库已经提供好了类似的函数。
```go
func Is(err, target error) bool
```
`errors.Is`函数的作用是判断错误链中是否包含指定的错误，例子如下
```go
var originalErr = errors.New("this is an error")
func wrap1() error { // 包裹原始错误
   return fmt.Errorf("wrapp error %w", wrap2())
}
func wrap2() error { // 原始错误
   return originalErr
}
func main() {
   err := wrap1()
   if errors.Is(err, originalErr) { // 如果使用if err == originalErr 将会是false
      fmt.Println("original")
   }
}
```
所以在判断错误时，不应该使用`==`操作符，而是应该使用`errors.Is()`。
```go
func As(err error, target any) bool
```
`errors.As()`函数的作用是在错误链中寻找第一个类型匹配的错误，并将值赋值给传入的`err`。有些情况下需要将`error`类型的错误转换为具体的错误实现类型，以获得更详细的错误细节，而对一个错误链使用类型断言是无效的，因为原始错误是被结构体包裹起来的，这也是为什么需要`As`函数的原因。例子如下
```go
type TimeError struct { // 自定义error
   Msg  string
   Time time.Time //记录发生错误的时间
}
func (m TimeError) Error() string {
   return m.Msg
}
func NewMyError(msg string) error {
   return &TimeError{
      Msg:  msg,
      Time: time.Now(),
   }
}
func wrap1() error { // 包裹原始错误
   return fmt.Errorf("wrapp error %w", wrap2())
}
func wrap2() error { // 原始错误
   return NewMyError("original error")
}
func main() {
   var myerr *TimeError
   err := wrap1()
   // 检查错误链中是否有*TimeError类型的错误
   if errors.As(err, &myerr) { // 输出TimeError的时间
      fmt.Println("original", myerr.Time)
   }
}
```
`target`必须是指向`error`的指针，由于在创建结构体时返回的是结构体指针，所以`error`实际上`*TimeError`类型的，那么`target`就必须是`**TimeError`类型的。

不过官方提供的`errors`包其实并不够用，因为它没有堆栈信息，不能定位，一般会比较推荐使用官方的另一个增强包
```
github.com/pkg/errors
```
例子
```go
import (
  "fmt"
  "github.com/pkg/errors"
)
func Do() error {
  return errors.New("error")
}
func main() {
  if err := Do(); err != nil {
    fmt.Printf("%+v", err)
  }
}
```
输出
```
some unexpected error happened
main.Do
        D:/WorkSpace/Code/GoLeran/golearn/main.go:9
main.main
        D:/WorkSpace/Code/GoLeran/golearn/main.go:13
runtime.main
        D:/WorkSpace/Library/go/root/go1.21.3/src/runtime/proc.go:267
runtime.goexit
        D:/WorkSpace/Library/go/root/go1.21.3/src/runtime/asm_amd64.s:1650
```
通过格式化输出，就可以看到堆栈信息了，默认情况下是不会输出堆栈的。这个包相当于是标准库`errors`包的加强版，同样都是官方写的；

## panic

panic中文译为恐慌，表示十分严重的程序问题，程序需要立即停止来处理该问题，否则程序立即停止运行并输出堆栈信息，panic是 Go 是运行时异常的表达形式，通常在一些危险操作中会出现，主要是为了及时止损，从而避免造成更加严重的后果。不过panic在退出之前会做好程序的善后工作，同时panic也可以被恢复来保证程序继续运行

### 创建

显式的创建panic十分简单，使用内置函数panic即可，函数签名如下：
```go
func panic(v any)
```
panic函数接收一个类型为 any的参数v，当输出错误堆栈信息时，v也会被输出
```go
func main() {
  initDataBase("", 0)
}
func initDataBase(host string, port int) {
  if len(host) == 0 || port == 0 {
    panic("非法的数据链接参数")
  }
    // ...其他的逻辑
}
```

### 后置处理

程序因为panic退出之前会做一些善后工作，例如执行defer语句
```go
func main() {
  defer fmt.Println("A")
  defer func() {
    func() {
      panic("panicA")
      defer fmt.Println("E")
    }()
  }()
  fmt.Println("C")
  dangerOp()
  defer fmt.Println("D")
}
func dangerOp() {
  defer fmt.Println(1)
  defer fmt.Println(2)
  panic("panicB")
  defer fmt.Println(3)
}
```
当发生panic时，会立即退出所在函数，并且执行当前函数的善后工作，例如defer，然后层层上抛，上游函数同样的也进行善后工作，直到程序停止运行。

当子协程发生panic时，不会触发当前协程的善后工作，如果直到子协程退出都没有恢复panic，那么程序将会直接停止运行

### 恢复

当发生`panic`时，使用内置函数`recover()`可以及时的处理并且保证程序继续运行，必须要在defer语句中运行，使用示例如下
```go
func main() {
   dangerOp()
   fmt.Println("程序正常退出")
}
func dangerOp() {
   defer func() {
      if err := recover(); err != nil {
         fmt.Println(err)
         fmt.Println("panic恢复")
      }
   }()
   panic("发生panic")
}
```
但事实上`recover()`的使用有许多隐含的陷阱。例如在`defer`中再次闭包使用`recover`
```go
func main() {
  dangerOp()
  fmt.Println("程序正常退出")
}
func dangerOp() {
  defer func() {
    func() {
      if err := recover(); err != nil {
        fmt.Println(err)
        fmt.Println("panic恢复")
      }
    }()
  }()
  panic("发生panic")
}
```
除此之外，还有一种很极端的情况，那就是panic()的参数是nil。这种情况panic确实会恢复，但是不会输出任何的错误信息

总的来说recover函数有几个注意点
1. 必须在defer中使用
2. 多次使用也只会有一个能恢复panic
3. 闭包recover不会恢复外部函数的任何panic
4. panic的参数禁止使用nil

## fatal

fatal是一种极其严重的问题，当发生fatal时，程序需要立刻停止运行，不会执行任何善后工作，通常情况下是调用os包下的Exit函数退出程序，如下所示
```go
func main() {
  dangerOp("")
}
func dangerOp(str string) {
  if len(str) == 0 {
    fmt.Println("fatal")
    os.Exit(1)
  }
  fmt.Println("正常逻辑")
}
```

# 方法

## 方法集

Golang方法集 ：每个类型都有与之关联的方法集，这会影响到接口实现规则
- 类型 T 方法集包含全部 receiver T 方法。
- 类型 *T 方法集包含全部 receiver T + *T 方法。
- 如类型 S 包含匿名字段 T，则 S 和 *S 方法集包含 T 方法。 
- 如类型 S 包含匿名字段 *T，则 S 和 *S 方法集包含 T + *T 方法。 
- 不管嵌入 T 或 *T，*S 方法集总是包含 T + *T 方法。

## 表达式



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

