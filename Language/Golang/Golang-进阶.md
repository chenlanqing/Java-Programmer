# 面向对象

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

