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

## 切片Slice

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
slice  := make([]type, len)
slice  := make([]type, len, cap)
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

## 函数

### 基本定义

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

### 返回值命名

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

### nil函数

函数也是一种类型，函数变量的默认值是nil，执行nil函数会引发panic
```go
var f func()
// f是一个函数类型，值是nil
// 编译正常，运行报错panic: runtime error: invalid memory address or nil pointer dereference
f() 
```

### [值传递](https://github.com/jincheng9/go-tutorial/tree/main/workspace/senior/p3)

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

### 函数高级用法

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

## defer

含义：defer 用于注册一个函数调用，使其在包含该 defer 的函数返回之前执行；  
类似 Java 中的 try-finally 块（特别像 finally）  
> defer = 注册一个延迟执行的函数，在函数 return 执行后、真正退出前执行。

### 闭包

匿名函数
```go
func main() {
	/*
	定义2个匿名函数，也就是闭包。
	 闭包可以直接调用，也可以赋值给一个变量，后续调用
	*/
	result1 := func(a int, b int) int {
		return a + b
	}(1, 2) // 直接调用

	var sub = func(a int, b int) int {
		return a - b
	}
	result2 := sub(1, 2)
	/*输出结果：3 -1*/
	fmt.Println(result1, result2)
}
```

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
