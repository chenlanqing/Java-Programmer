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
