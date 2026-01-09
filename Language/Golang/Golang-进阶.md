## 接口

### 定义

接口是一种抽象的类型，是一组method的集合，里头只有method方法，没有数据成员。当两个或两个以上的类型都有相同的处理方法时才需要用到接口。先定义接口，然后多个struct类型去实现接口里的方法，就可以通过接口变量去调用struct类型里实现的方法。
```go
// 定义接口
type interface_name interface {
  method_name1([参数列表]) [返回值列表]
  method_name2([参数列表]) [返回值列表]
  method_nameN([参数列表]) [返回值列表]
}
// 定义结构体类型
type struct_name struct {
    data_member1 data_type
    data_member2 data_type
    data_memberN data_type
}
// 实现接口interface_name里的方法method_name1
func(struct_var struct_name) method_name1([参数列表])[返回值列表] {
    /*具体方法实现*/
}
// 实现接口interface_name里的方法method_name2
func(struct_var struct_name) method_name2([参数列表])[返回值列表] {
    /*具体方法实现*/
}
/* 实现接口interface_name里的方法method_name3
注意：下面用了指针接受者。函数可以使用值接受者或者指针接受者，上面的method_name1和method_name1使用的是值接受者。
如果用了指针接受者，那给interface变量赋值的时候要传指针
*/
func(struct_var *struct_name) method_name3([参数列表])[返回值列表] {
    /*具体方法实现*/
}
```

示例
```go
// all animals can speak
type Animal interface {
    speak()
}
// cat
type Cat struct {
    name string
    age int
}
func(cat Cat) speak() {
    fmt.Println("cat miaomiaomiao")
}
// dog
type Dog struct {
    name string
    age int
}
func(dog *Dog) speak() {
    fmt.Println("dog wangwangwang")
}
func main() {
    var animal Animal = Cat{"gaffe", 1}
    animal.speak() // cat miaomiaomiao
    /*
    因为Dog的speak方法用的是指针接受者，因此给interface赋值的时候，要赋指针
    */
    animal = &Dog{"caiquan", 2}
    animal.speak() // dog wangwangwang
}
```
注意：
1. struct结构体类型在实现 interface 里的所有方法时，关于interface变量赋值有2个点要注意
- 只要有某个方法的实现使用了指针接受者，那给包含了这个方法的 interface 变量赋值的时候要使用指针。比如上面的Dog类型要赋值给Animal，必须使用指针，因为Dog实现speak方法用了指针接受者；
- 如果全部方法都使用的是值接受者，那给 interface 变量赋值的时候用值或者指针都可以。比如上面的例子，animal 的初始化用下面的方式一样可以
```go
var animal Animal = &Cat{"gaffe", 1}
```
2. 多个 struct 类型可以实现同一个interface：多个类型都有共同的方法(行为)。比如上面示例里的猫和狗都会叫唤，猫和狗就是2个类型，叫唤就是speak方法。
3. 一个 struct 类型可以实现多个interface。比如猫这个类型，既是猫科动物，也是哺乳动物。猫科动物可以是一个 interface，哺乳动物可以是另一个interface，猫这个struct类型可以实现猫科动物和哺乳动物这2个interface里的方法
4. interface可以嵌套：一个interface里包含其它interface
```go
// interface1
type Felines interface {
    feet() 
}
// interface2, 嵌套了interface1
type Mammal interface {
    Felines
    born()
}
// 猫实现Mammal这个interface里的所有方法
type Cat struct {
    name string
    age int
}
func(cat Cat) feet() {
    fmt.Println("cat feet")
}
func(cat *Cat) born() {
    fmt.Println("cat born")
}
```
### 空接口 interface

可以给空接口定义别名：`type any = interface{}`

1. 如果空 interface 作为函数参数，可以接受任何类型的实参
```go
func function_name(x interface{}) {
    do sth
}
```
2. 如果空 interface 作为变量，可以把任何类型的变量赋值给空 interface
```go
var x interface{} // 空接口x
// 定义空接口x
var x interface{}
// 将map变量赋值给空接口x
x = map[string]int{"a":1}
print(x) // type:map[string]int, value:map[a:1]

// 传struct变量估值给空接口x
cat := Cat{"nimo", 2}
x = cat
```
3. 空接口作为map的值，可以实现map的value是不同的数据类型
```go
// 定义一个map类型的变量，key是string类型，value是空接口类型
dict := make(map[string]interface{})
// value可以是int类型
dict["a"] = 1 
// value可以是字符串类型
dict["b"] = "b"
// value可以是bool类型
dict["c"] = true
fmt.Println(dict) // map[a:1 b:b c:true]
fmt.Printf("type:%T, value:%v\n", dict["b"], dict["b"]) // type:string, value:b
```
4. `x.(T)`
- 断言：断言接口变量x是T类型  
语法：value是将x转化为T类型后的变量，ok是布尔值，true表示断言成功，false表示断言失败
```go
// x是接口变量，如果要判断x是不是
value, ok := x.(string)
var x interface{}
x = "a"
// 断言接口变量x的类型是string
v, ok := x.(string)
if ok {
    // 断言成功
    fmt.Println("assert true, value:", v)
} else{
    // 断言失败
	fmt.Println("assert false")
}
```
- 动态判断数据类型
```go
func checkType(x interface{}) {
    /*动态判断x的数据类型*/
    switch v := x.(type) {
    case int:
        fmt.Printf("type: int, value: %v\n", v)
    case string:
        fmt.Printf("type: string，value: %v\n", v)
    case bool:
        fmt.Printf("type: bool, value: %v\n", v)
    case Cat:
        fmt.Printf("type: Cat, value: %v\n", v)
    case map[string]int:
        fmt.Printf("type: map[string]int, value: %v\n", v)
        v["a"] = 10
    default:
        fmt.Printf("type: %T, value: %v\n", x, x)
    }
}
```
5. 空接口比较：在比较空接口时，会对其底层类型进行比较，如果类型不匹配的话则为false，其次才是值的比较，例如
```go
func main() {
  var a interface{}
  var b interface{}
  a = 1
  b = "1"
  fmt.Println(a == b) // false
  a = 1
  b = 1
  fmt.Println(a == b) // true
}
```
如果底层的类型是不可比较的，那么会panic，对于 Go 而言，内置数据类型是否可比较的情况如
| 类型       | 可比较 | 依据                     |
| ---------- | ------ | ------------------------ |
| 数字类型   | 是     | 值是否相等               |
| 字符串类型 | 是     | 值是否相等               |
| 数组类型   | 是     | 数组的全部元素是否相等   |
| 切片类型   | 否     | 不可比较                 |
| 结构体     | 是     | 字段值是否全部相等       |
| map 类型   | 否     | 不可比较                 |
| 通道       | 是     | 地址是否相等             |
| 指针       | 是     | 指针存储的地址是否相等   |
| 接口       | 是     | 底层所存储的数据是否相等 |

在 Go 中有一个专门的接口类型用于代表所有可比较类型，即`comparable`

```go
type comparable interface{ comparable }
```

### 说明

如果把一个结构体变量赋值给interface变量，那结构体需要实现interface里的所有方法，否则会编译报错：xx does not implement yy，表示结构体xx没有实现接口yy

## 泛型

### 基本用法

Go在1.18版本加入了对泛型的支持，泛型是为了解决执行逻辑与类型无关的问题，这类问题不关心给出的类型是什么，只需要完成对应的操作就足够。所以泛型的写法如下
```go
func Sum[T int | float64](a, b T) T {
   return a + b
}
```
- 类型形参：`T`就是一个类型形参，形参具体是什么类型取决于传进来什么类型；
- 类型约束：`int | float64`构成了一个类型约束，这个类型约束内规定了哪些类型是允许的，约束了类型形参的类型范围
- 类型实参：`Sum[int](1,2)`，手动指定了int类型，int就是类型实参；

如何使用呢：
- 第一种用法，显式的指明使用哪种类型，如下：`Sum[int](2012, 2022)`；
- 第二种用法，不指定类型，让编译器自行推断，如下：`Sum(3.1415926, 1.114514)`

### 泛型结构

这是一个泛型切片，类型约束为`int | int32 | int64`
```go
type GenericSlice[T int | int32 | int64] []T
```
这里使用时就不能省略掉类型实参
```go
GenericSlice[int]{1, 2, 3}
```
这是一个泛型哈希表，键的类型必须是可比较的，所以使用comparable接口，值的类型约束为`V int | string | byte`
```go
type GenericMap[K comparable, V int | string | byte] map[K]V
// 基本使用
gmap1 := GenericMap[int, string]{1: "hello world"}
gmap2 := make(GenericMap[string, byte], 0)
```
这是一个泛型结构体，类型约束为`T int | string`
```go
type GenericStruct[T int | string] struct {
   Name string
   Id   T
}
type Company[T int | string, S int | string] struct {
	Name  string
	Id    T
	Stuff []S
}
// 使用
GenericStruct[int]{
   Name: "jack",
   Id:   1024,
}
GenericStruct[string]{
   Name: "Mike",
   Id:   "1024",
}
```
泛型接口：
```go
type SayAble[T int | string] interface {
   Say() T
}
type Person[T int | string] struct {
   msg T
}
func (p Person[T]) Say() T {
   return p.msg
}
func main() {
	var s SayAble[string]
	s = Person[string]{"hello world"}
	fmt.Println(s.Say())
}
```
注意事项：
1. 泛型不能作为一个类型的基本类型，以下写法是错误的，泛型形参T是不能作为基础类型的
```go
type GenericType[T int | int32 | int64] T
```
虽然下列的写法是允许的，不过毫无意义而且可能会造成数值溢出的问题，虽然并不推荐
```go
type GenericType[T int | int32 | int64] int
```
2. 泛型类型无法使用类型断言：对泛型类型使用类型断言将会无法通过编译，泛型要解决的问题是类型无关的，如果一个问题需要根据不同类型做出不同的逻辑，那么就根本不应该使用泛型，应该使用interface{}或者any
```go
func Sum[T int | float64](a, b T) T {
   ints,ok := a.(int) // 不被允许
   switch a.(type) { // 不被允许
   case int:
   case bool:
      ...
   }
   return a + b
}
```
3. 匿名结构不支持泛型：如下的代码将无法通过编译
```go
testStruct := struct[T int | string] {
   Name string
   Id T
}[int]{
   Name: "jack",
   Id: 1  
}
```
4. 匿名函数不支持自定义泛型
```go
// 如下写法都无法通过编译
var sum[T int | string] func (a, b T) T
sum := func[T int | string](a,b T) T{
    ...
}
// 但是可以使用已有的泛型类型，例如闭包中
func Sum[T int | float64](a, b T) T {
	sub := func(c, d T) T {
		return c - d
	}
	return sub(a,b) + a + b
}
```
5. 不支持泛型方法: 方法是不能拥有泛型形参的，但是receiver可以拥有泛型形参。如下的代码将会无法通过编译
```go
type GenericStruct[T int | string] struct {
   Name string
   Id   T
}
func (g GenericStruct[T]) name[S int | float64](a S) S {
   return a
}
```

### 类型集

在1.18以后，接口的定义变为了类型集(type set)，含有类型集的接口又称为General interfaces即通用接口。

类型集主要用于类型约束，不能用作类型声明，既然是集合，就会有空集，并集，交集，接下来将会讲解这三种情况。

#### 并集

接口类型SignedInt是一个类型集，包含了全部有符号整数的并集
```go
type SignedInt interface {
   int8 | int16 | int | int32 | int64
}
```

#### 交集

非空接口的类型集是其所有元素的类型集的交集，翻译成人话就是：如果一个接口包含多个非空类型集，那么该接口就是这些类型集的交集，例子如下
```go
type SignedInt interface {
   int8 | int16 | int | int32 | int64
}
type Integer interface {
   int8 | int16 | int | int32 | int64 | uint8 | uint16 | uint | uint32 | uint64
}
type Number interface {
	SignedInt
	Int
}
```
例子中的交集肯定就是SignedInt
```go
func Do[T Number](n T) T {
   return n
}
Do[int](2)
// Cannot use uint as the type Number
// Type does not implement constraint Number because type is not included in type set (int8, int16, int, int32, int64)
DO[uint](2) //无法通过编译
```

#### 空集

空集就是没有交集，例子如下，下面例子中的Integer就是一个类型空集。
```go
type SignedInt interface {
	int8 | int16 | int | int32 | int64
}
type UnsignedInt interface {
	uint8 | uint16 | uint | uint32 | uint64
}
type Integer interface {
	SignedInt
	UnsignedInt
}
```
因为无符号整数和有符号整数两个肯定没有交集，所以交集就是个空集，下方例子中不管传什么类型都无法通过编译
```go
Do[Integer](1)
Do[Integer](-100)
```

#### 空接口

空接口与空集并不同，空接口是所有类型集的集合，即包含所有类型。
```go
func Do[T interface{}](n T) T {
   return n
}
func main() {
   Do[struct{}](struct{}{})
   Do[any]("abc")
}
```

#### 底层类型

当使用type关键字声明了一个新的类型时，即便其底层类型包含在类型集内，当传入时也依旧会无法通过编译
```go
type Int interface {
   int8 | int16 | int | int32 | int64 | uint8 | uint16 | uint | uint32 | uint64
}
type TinyInt int8
func Do[T Int](n T) T {
   return n
}
func main() {
   Do[TinyInt](1) // 无法通过编译，即便其底层类型属于Int类型集的范围内
}
```
有两种解决办法，第一种是往类型集中并入该类型，但是这毫无意义，因为TinyInt与int8底层类型就是一致的，所以就有了第二种解决办法。使用`~`符号，来表示底层类型，如果一个类型的底层类型属于该类型集，那么该类型就属于该类型集，如下所示
```go
type Int interface {
   ~int8 | ~int16 | ~int | ~int32 | ~int64 | ~uint8 | ~uint16 | ~uint | ~uint32 | ~uint64
}
```

#### 注意事项

1. 带有方法集的接口无法并入类型集  
只要是带有方法集的接口，不论是基本接口，泛型接口，又或者是通用接口，都无法并入类型集中，同样的也无法在类型约束中并入。以下两种写法都是错误的，都无法通过编译。
```go
type Integer interface {
	Sum(int, int) int
	Sub(int, int) int
}
type SignedInt interface {
   int8 | int16 | int | int32 | int64 | Integer
}
func Do[T Integer | float64](n T) T {
	return n
}
```

2. 类型集无法当作类型实参使用  
只要是带有类型集的接口，都无法当作类型实参。
```go
type SignedInt interface {
	int8 | int16 | int | int32 | int64
}
func Do[T SignedInt](n T) T {
   return n
}
func main() {
   Do[SignedInt](1) // 无法通过编译
}
```

3. 类型集中的交集问题  
对于非接口类型，类型并集中不能有交集，例如下例中的TinyInt与~int8有交集。
```go
type Int interface {
   ~int8 | ~int16 | ~int | ~int32 | ~int64 | ~uint8 | ~uint16 | ~uint | ~uint32 | ~uint64 | TinyInt // 无法通过编译
}
type TinyInt int8
```

## 文件

Go 语言提供文件处理的标准库大致以下几个：
1. os库，负责 OS 文件系统交互的具体实现
2. io库，读写 IO 的抽象层
3. fs库，文件系统的抽象层

### 打开文件

使用os包提供的两个函数，Open函数返回值一个文件指针和一个错误
```go
// 读取的文件仅仅只是只读的，无法被修改
func Open(name string) (*File, error)
// OpenFile能够提供更加细粒度的控制，函数Open就是对OpenFile函数的一个简单封装
func OpenFile(name string, flag int, perm FileMode) (*File, error)
```
文件的查找路径默认为项目`go.mod文`件所在的路径；

因为 IO 错误的类型有很多，所以需要手动的去判断文件是否存在，同样的os包也为此提供了方便函数
```go
func main() {
  file, err := os.Open("README.txt")
  if os.IsNotExist(err) {
    fmt.Println("文件不存在")
  } else if err != nil {
    fmt.Println("文件访问异常")
  } else {
    fmt.Println("文件读取成功", file)
  }
}
```
通过OpenFile函数可以控制更多细节，例如修改文件描述符和文件权限，关于文件描述符，os包下提供了以下常量以供使用
```go
const (
   // 只读，只写，读写 三种必须指定一个
   O_RDONLY int = syscall.O_RDONLY // 以只读的模式打开文件
   O_WRONLY int = syscall.O_WRONLY // 以只写的模式打开文件
   O_RDWR   int = syscall.O_RDWR   // 以读写的模式打开文件
   // 剩余的值用于控制行为
   O_APPEND int = syscall.O_APPEND // 当写入文件时，将数据添加到文件末尾
   O_CREATE int = syscall.O_CREAT  // 如果文件不存在则创建文件
   O_EXCL   int = syscall.O_EXCL   // 与O_CREATE一起使用, 文件必须不存在
   O_SYNC   int = syscall.O_SYNC   // 以同步IO的方式打开文件
   O_TRUNC  int = syscall.O_TRUNC  // 当打开的时候截断可写的文件
)
```
关于文件权限的则提供了以下常量
```go
const (
   ModeDir        = fs.ModeDir        // d: 目录
   ModeAppend     = fs.ModeAppend     // a: 只能添加
   ModeExclusive  = fs.ModeExclusive  // l: 专用
   ModeTemporary  = fs.ModeTemporary  // T: 临时文件
   ModeSymlink    = fs.ModeSymlink    // L: 符号链接
   ModeDevice     = fs.ModeDevice     // D: 设备文件
   ModeNamedPipe  = fs.ModeNamedPipe  // p: 具名管道 (FIFO)
   ModeSocket     = fs.ModeSocket     // S: Unix 域套接字
   ModeSetuid     = fs.ModeSetuid     // u: setuid
   ModeSetgid     = fs.ModeSetgid     // g: setgid
   ModeCharDevice = fs.ModeCharDevice // c: Unix 字符设备, 前提是设置了 ModeDevice
   ModeSticky     = fs.ModeSticky     // t: 黏滞位
   ModeIrregular  = fs.ModeIrregular  // ?: 非常规文件
   // 类型位的掩码. 对于常规文件而言，什么都不会设置.
   ModeType = fs.ModeType
   ModePerm = fs.ModePerm // Unix 权限位, 0o777
)
```
读取文件，不存在则创建：
```go
file, err := os.OpenFile("README.txt", os.O_RDWR|os.O_CREATE, 0666)
```
倘若只是想获取该文件的一些信息，并不想读取该文件，可以使用`os.Stat()`函数进行操作
```go
func main() {
  fileInfo, err := os.Stat("README.txt")
  if err != nil {
    fmt.Println(err)
  } else {
    fmt.Println(fmt.Sprintf("%+v", fileInfo))
  }
}
// &{name:README.txt FileAttributes:32 CreationTime:{LowDateTime:3603459389 HighDateTime:31016791} LastAccessTime:{LowDateTime:3603459389 HighDateTime:31016791} LastWriteTime:{LowDateTime:3603459389 HighDateTime:31016791} FileSizeHigh
// :0 FileSizeLow:0 Reserved0:0 filetype:0 Mutex:{state:0 sema:0} path:README.txt vol:0 idxhi:0 idxlo:0 appendNameToPath:false}
```

> 说明：打开一个文件后永远要记得关闭该文件，通常关闭操作会放在defer语句里
```go
defer file.Close()
```

### 文件读取

当成功的打开文件后，便可以进行读取操作了，关于读取文件的操作，*os.File类型提供了以下几个公开的方法
```go
// 将文件读进传入的字节切片
func (f *File) Read(b []byte) (n int, err error)
// 相较于第一种可以从指定偏移量读取
func (f *File) ReadAt(b []byte, off int64) (n int, err error)
```
针对于第一种方法，需要自行编写逻辑来进行读取时切片的动态扩容
```go
func ReadFile(file *os.File) ([]byte, error) {
  buffer := make([]byte, 0, 512)
  for {
    // 当容量不足时
    if len(buffer) == cap(buffer) {
      buffer = append(buffer, 0)[:len(buffer)] // 扩容
    }
    offset, err := file.Read(buffer[len(buffer):cap(buffer)])// 继续读取文件
    buffer = buffer[:len(buffer)+offset]// 将已写入的数据归入切片
    if err != nil { // 发生错误时
      if errors.Is(err, io.EOF) {
        err = nil
      }
      return buffer, err
    }
  }
}
```
还可以使用两个方便函数来进行文件读取：
1. os包下的ReadFile函数，只需要提供文件路径即可；
```go
func ReadFile(name string) ([]byte, error)
// 使用示例：
bytes, err := os.ReadFile("README.txt")
// 忽略错误判断
fmt.Println(string(bytes))
```
2. io包下的ReadAll函数，需要提供一个io.Reader类型的实现
```go
func ReadAll(r Reader) ([]byte, error)
// 使用示例：忽略错误判断
file, _ := os.OpenFile("README.txt", os.O_RDWR|os.O_CREATE, 0666)
fmt.Println("文件打开成功", file.Name())
bytes, _ := io.ReadAll(file)
fmt.Println(string(bytes))
```

### 写入

`os.File` 结构体提供了以下几种方法以供写入数据
```go
// 写入字节切片
func (f *File) Write(b []byte) (n int, err error)
// 写入字符串
func (f *File) WriteString(s string) (n int, err error)
// 从指定位置开始写，当以 os.O_APPEND 模式打开时，会返回错误
func (f *File) WriteAt(b []byte, off int64) (n int, err error)
```
如果想要对一个文件写入数据，则必须以`O_WRONLY`或`O_RDWR`的模式打开，否则无法成功写入文件
```go
// 如果未开启`O_WRONLY`或`O_RDWR`模式，报错： write README.txt: bad file descriptor
func main() {
  file, err := os.OpenFile("README.txt", os.O_RDWR|os.O_CREATE|os.O_APPEND|os.O_TRUNC, 0666)
  if err != nil {
    fmt.Println("文件访问异常")
  } else {
    fmt.Println("文件打开成功", file.Name())
    for i := 0; i < 5; i++ {
      offset, err := file.WriteString("hello world!\n")
      if err != nil {
        fmt.Println(offset, err)
      }
    }
    fmt.Println(file.Close())
  }
}
```

对于写入文件的操作标准库同样提供了方便函数，分别是：
1. os.WriteFile
```go
func WriteFile(name string, data []byte, perm FileMode) error
// 示例
func main() {
  err := os.WriteFile("README.txt", []byte("hello world!\n"), 0666)
  if err != nil {
    fmt.Println(err)
  }
}
```
2. io.WriteString
```go
func WriteString(w Writer, s string) (n int, err error)
// 示例
func main() {
   file, err := os.OpenFile("README.txt", os.O_RDWR|os.O_CREATE|os.O_APPEND|os.O_TRUNC, 0666)
   fmt.Println("文件打开成功", file.Name())
   for i := 0; i < 5; i++ {
      offset, err := io.WriteString(file, "hello world!\n")
      if err != nil {
         fmt.Println(offset, err)
      }
   }
   fmt.Println(file.Close())
}
```

函数`os.Create`函数用于创建文件，本质上也是对OpenFile的封装
```go
func Create(name string) (*File, error) {
   return OpenFile(name, O_RDWR|O_CREATE|O_TRUNC, 0666)
}
```

> ⚠️ **注意**
>
> 在创建一个文件时，如果其父目录不存在，将创建失败并会返回错误。

### 复制

1. 将原文件中的数据读取出来，然后写入目标文件中
```go
func main() {
   // 从原文件中读取数据
   data, err := os.ReadFile("README.txt")
   // 写入目标文件
   err = os.WriteFile("README(1).txt", data, 0666)
   fmt.Println("复制成功")
}
```
2. 使用`os.File`提供的方法`ReadFrom`，打开文件时，一个只读，一个只写
```go
func (f *File) ReadFrom(r io.Reader) (n int64, err error)
```
示例
```go
func main() {
  // 以只读的方式打开原文件
  origin, err := os.OpenFile("README.txt", os.O_RDONLY, 0666)
  defer origin.Close()
  // 以只写的方式打开副本文件
  target, err := os.OpenFile("README(1).txt", os.O_WRONLY|os.O_CREATE|os.O_TRUNC, 0666)
  defer target.Close()
  // 从原文件中读取数据，然后写入副本文件
  offset, err := target.ReadFrom(origin)
  fmt.Println("文件复制成功", offset)
}
```
> 文件特别大的时候不建议这么做

3. 使用`io.Copy`函数，它则是一边读一边写，先将内容读到缓冲区中，再写入到目标文件中，缓冲区默认大小为 32KB
```go
func Copy(dst Writer, src Reader) (written int64, err error)
// 示例
func main() {
   // 以只读的方式打开原文件
   origin, err := os.OpenFile("README.txt", os.O_RDONLY, 0666)
   defer origin.Close()
   // 以只写的方式打开副本文件
   target, err := os.OpenFile("README(1).txt", os.O_WRONLY|os.O_CREATE|os.O_TRUNC, 0666)
   defer target.Close()
   // 复制
   written, err := io.Copy(target, origin)
   fmt.Println(written)
}
```
可以使用`io.CopyBuffer`来指定缓冲区大小

### 重命名

可以理解为移动文件，会用到os包下的Rename函数。
```go
func Rename(oldpath, newpath string) error
```
该函数对于文件夹也是同样的效果

### 删除

os包下的两个函数
```go
// 删除单个文件或者空目录，当目录不为空时会返回错误
func Remove(name string) error
// 删除指定目录的所有文件和目录包括子目录与子文件
func RemoveAll(path string) error
```

### 刷新

`os.Sync`这一个函数封装了底层的系统调用Fsync，用于将操作系统中缓存的 IO 写入落实到磁盘上
```go
func main() {
  create, err := os.Create("test.txt")
  defer create.Close()
  _, err = create.Write([]byte("hello"))
    // 刷盘
  if err := create.Sync();err != nil {
    return
  }
}
```

### 文件夹

#### 读取

1. `os.ReadDir`
```go
func ReadDir(name string) ([]DirEntry, error)
// 
func main() {
	// 当前目录
	dir, err := os.ReadDir(".")
	if err != nil {
		fmt.Println(err)
	} else {
		for _, entry := range dir {
			fmt.Println(entry.Name())
		}
	}
}
```

2. `*os.File.ReadDir`函数：os.ReadDir本质上也只是对*os.File.ReadDir的一层简单封装。
```go
// n < 0时，则读取文件夹下所有的内容
func (f *File) ReadDir(n int) ([]DirEntry, error)
```

#### 创建

创建文件夹操作会用到os包下的两个函数
```go
// 用指定的权限创建指定名称的目录
func Mkdir(name string, perm FileMode) error
// 相较于前者该函数会创建一切必要的父目录
func MkdirAll(path string, perm FileMode) error
```

## 反射



