## 协程

协程（coroutine）是一种轻量级的线程，或者说是用户态的线程，不受操作系统直接调度，由 Go 语言自身的调度器进行运行时调度，因此上下文切换开销非常小，这也是为什么 Go 的并发性能很不错的原因之一

在 Go 中，创建一个协程十分的简单，仅需要一个 go 关键字，就能够快速开启一个协程，go 关键字后面必须是一个函数调用
```go
func main() {
  go fmt.Println("hello world!")
  go hello()
  go func() {
    fmt.Println("hello world!")
  }()
}
func hello() {
  fmt.Println("hello world!")
}
```
注意： 具有返回值的内置函数不允许跟随在 go 关键字后面，例如下面的错误示范
```go
go make([]int,10) //  go discards result of make([]int, 10) (value of type []int)
```