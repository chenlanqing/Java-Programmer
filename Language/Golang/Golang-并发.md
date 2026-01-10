# 协程

## 基本概念

协程（coroutine）是一种轻量级的线程，或者说是用户态的线程，不受操作系统直接调度，由 Go 语言自身的调度器进行运行时调度，因此上下文切换开销非常小，这也是为什么 Go 的并发性能很不错的原因之一

在 Go 中，创建一个协程十分的简单，仅需要一个 go 关键字，就能够快速开启一个协程，go 关键字后面必须是一个函数调用

> 注意： 具有返回值的内置函数不允许跟随在 go 关键字后面，例如下面的错误示范
```go
go make([]int,10) //  go discards result of make([]int, 10) (value of type []int)
```
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
以上三种开启协程的方式都是可以的；

协程是并发执行的，系统创建协程需要时间，而在此之前，主协程早已运行结束，一旦主线程退出，其他子协程也就自然退出了。并且协程的执行顺序也是不确定的，无法预判的；最简单的做法就是让主协程等一会儿，需要使用到 time 包下的 Sleep 函数，可以使当前协程暂停一段时间
```go
func main() {
  fmt.Println("start")
  for i := 0; i < 10; i++ {
    go fmt.Println(i)
  }
  // 暂停1ms
  time.Sleep(time.Millisecond)
  fmt.Println("end")
}
```
再次执行输出如下，可以看到所有的数字都完整输出了，没有遗漏，但是顺序还是乱的，因此让每次循环都稍微的等一下
```go
func main() {
   fmt.Println("start")
   for i := 0; i < 10; i++ {
      go fmt.Println(i)
      time.Sleep(time.Millisecond)
   }
   time.Sleep(time.Millisecond)
   fmt.Println("end")
}
```
因此 time.Sleep 并不是一种良好的解决并发的办法，幸运的是 Go 提供了非常多的并发控制手段，常用的并发控制方法有三种，三种方法有着不同的适用情况
- channel：管道，更适合协程间通信
- WaitGroup：信号量，可以动态的控制一组指定数量的协程
- Context：上下文，更适合子孙协程嵌套层级更深的情况

对于较为传统的锁控制，Go 也对此提供了支持：
- Mutex：互斥锁
- RWMutex ：读写互斥锁

## channel

channel，即通过消息来进行内存共享，channel 就是为此而生，它是一种在协程间通信的解决方案，同时也可以用于并发控制；Go 中通过关键字 `chan` 来代表管道类型，同时也必须声明管道的存储类型，来指定其存储的数据是什么类型，下面的例子是一个普通管道的模样
```go
var ch chan int
```
这是一个管道的声明语句，此时管道还未初始化，其值为 nil，不可以直接使用。

### 创建

在创建管道时，有且只有一种方法，那就是使用内置函数 make，对于管道而言，make 函数接收两个参数，第一个是管道的类型，第二个是可选参数为管道的缓冲大小
```go
intCh := make(chan int)
// 缓冲区大小为1的管道
strCh := make(chan string, 1)
```
在使用完一个管道后一定要记得关闭该管道，使用内置函数 close 来关闭一个管道，该函数签名如下
```go
func close(c chan<- Type)
// 示例
func main() {
  intCh := make(chan int)
  // do something
  defer close(intCh)
}
```

### 读写

对于一个管道而言，Go 使用了两种很形象的操作符来表示读写操作：
- `ch <-`：表示对一个管道写入数据
- `<- ch`：表示对一个管道读取数据  
`<-` 很生动的表示了数据的流动方向，来看一个对 int 类型的管道读写的例子
```go
func main() {
    // 如果没有缓冲区则会导致死锁
    intCh := make(chan int, 1)
    defer close(intCh)
    // 写入数据
    intCh <- 114514
    // 读取数据
    fmt.Println(<-intCh)
}
```
对于读取操作而言，还有第二个返回值，一个布尔类型的值，用于表示数据是否读取成功
```go
ints, ok := <-intCh
```
管道中的数据流动方式与队列一样，即先进先出（FIFO），协程对于管道的操作是同步的，在某一个时刻，只有一个协程能够对其写入数据，同时也只有一个协程能够读取管道中的数据

### 无缓冲

对于无缓冲管道而言，因为缓冲区容量为 0，所以不会临时存放任何数据。正因为无缓冲管道无法存放数据，在向管道写入数据时必须立刻有其他协程来读取数据，否则就会阻塞等待，读取数据时也是同理，这也解释了为什么下面看起来很正常的代码会发生死锁
```go
func main() {
    // 创建无缓冲管道
    ch := make(chan int)
    defer close(ch)
    // 写入数据
    ch <- 123
    // 读取数据
    n := <-ch
    fmt.Println(n)
}
// 输出报错：fatal error: all goroutines are asleep - deadlock!
```
无缓冲管道不应该同步的使用，正确来说应该开启一个新的协程来发送数据
```go
func main() {
    // 创建无缓冲管道
    ch := make(chan int)
    defer close(ch)
    go func() {
        // 写入数据
        ch <- 123
    }()
    // 读取数据
    n := <-ch
    fmt.Println(n)
}
```

### 有缓冲

当管道有了缓冲区，就像是一个阻塞队列一样，读取空的管道和写入已满的管道都会造成阻塞。
- 无缓冲管道在发送数据时，必须立刻有人接收，否则就会一直阻塞。
- 对于有缓冲管道则不必如此：
    - 对于有缓冲管道写入数据时，会先将数据放入缓冲区里，只有当缓冲区容量满了才会阻塞的等待协程来读取管道中的数据。
    - 同样的，读取有缓冲管道时，会先从缓冲区中读取数据，直到缓冲区没数据了，才会阻塞的等待协程来向管道中写入数据。  

因此，无缓冲管道中会造成死锁例子在这里可以顺利运行
```go
func main() {
    // 创建有缓冲管道
    ch := make(chan int, 1)
    defer close(ch)
    // 写入数据
    ch <- 123
    // 读取数据
    n := <-ch
    fmt.Println(n)
}
```
但这种同步读写的方式是非常危险的，一旦管道缓冲区空了或者满了，将会永远阻塞下去，因为没有其他协程来向管道中写入或读取数据

> 说明：
> 通过内置函数 len 可以访问管道缓冲区中数据的个数，通过 cap 可以访问管道缓冲区的大小
```go
func main() {
   ch := make(chan int, 5)
   ch <- 1
   ch <- 2
   ch <- 3
   fmt.Println(len(ch), cap(ch))
}
```
利用管道的阻塞条件，可以很轻易的写出一个主协程等待子协程执行完毕的例子
```go
func main() {
   // 创建一个无缓冲管道
   ch := make(chan struct{})
   defer close(ch)
   go func() {
      fmt.Println(2)
      // 写入
      ch <- struct{}{}
   }()
   // 阻塞等待读取
   <-ch
   fmt.Println(1)
}
```
通过有缓冲管道还可以实现一个简单的互斥锁
```go
var count = 0
// 缓冲区大小为1的管道
var lock = make(chan struct{}, 1)
func Add() {
    // 加锁
  lock <- struct{}{}
  fmt.Println("当前计数为", count, "执行加法")
  count += 1
    // 解锁
  <-lock
}
func Sub() {
    // 加锁
  lock <- struct{}{}
  fmt.Println("当前计数为", count, "执行减法")
  count -= 1
    // 解锁
  <-lock
}
```

### 注意事项

1. 读写无缓冲管道：当对一个无缓冲管道直接进行同步读写操作都会导致该协程阻塞
2. 读取空缓冲区的管道：当读取一个缓冲区为空的管道时，会导致该协程阻塞
```go
func main() {
    // 创建的有缓冲管道
    intCh := make(chan int, 1)
    defer close(intCh)
    // 缓冲区为空，阻塞等待其他协程写入数据
    ints, ok := <-intCh
    fmt.Println(ints, ok)
}
```
3. 写入满缓冲区的管道：当管道的缓冲区已满，对其写入数据会导致该协程阻塞
```go
func main() {
    // 创建的有缓冲管道
    intCh := make(chan int, 1)
    defer close(intCh)
    intCh <- 1
        // 满了，阻塞等待其他协程来读取数据
    intCh <- 1
}
```
4. 管道为 nil：当管道为 nil 时，无论怎样读写都会导致当前协程阻塞


**以下几种情况还会导致 panic：**
1. 关闭一个 nil 管道：当管道为 nil 时，使用 close 函数对其进行关闭操作会导致 panic`（panic: close of nil channel）
2. 写入已关闭的管道：对一个已关闭的管道写入数据会导致 panic（panic: send on closed channel）
3. 关闭已关闭的管道：在一些情况中，管道可能经过层层传递，调用者或许也不知道到底该由谁来关闭管道，如此一来，可能会发生关闭一个已经关闭了的管道，就会发生 panic（panic: close of closed channel）

### 单向管道

双向管道指的是既可以写，也可以读，即可以在管道两边进行操作。  
单向管道指的是只读或只写的管道，即只能在管道的一边进行操作。手动创建的一个只读或只写的管道没有什么太大的意义，因为不能对管道读写就失去了其存在的作用。  
单向管道通常是用来限制通道的行为，一般会在函数的形参和返回值中出现，例如用于关闭通道的内置函数 close 的函数签名就用到了单向通道
```go
func close(c chan<- Type)
// time
func After(d Duration) <-chan Time
```
close 函数的形参是一个只写通道，After 函数的返回值是一个只读通道，所以单向通道的语法如下：
- 箭头符号 `<-` 在前，就是只读通道，如 `<-chan int`
- 箭头符号 `<-` 在后，就是只写通道，如 `chan<- string`

当尝试对只读的管道写入数据时，将会无法通过编译
```go
func main() {
  timeCh := time.After(time.Second)
  timeCh <- time.Now()
  // invalid operation: cannot send to receive-only channel timeCh (variable of type <-chan time.Time)
}
```
双向管道可以转换为单向管道，反过来则不可以。通常情况下，将双向管道传给某个协程或函数并且不希望它读取/发送数据，就可以用到单向管道来限制另一方的行为
```go
func main() {
   ch := make(chan int, 1)
   go write(ch)
   fmt.Println(<-ch)
}
func write(ch chan<- int) {
   // 只能对管道发送数据
   ch <- 1
}
```

### 遍历管道数据

通过 for range 语句，可以遍历读取缓冲管道中的数据
```go
func main() {
    ch := make(chan int, 10)
    go func() {
        for i := 0; i < 10; i++ {
        ch <- i
        }
    }()
    for n := range ch {
        fmt.Println(n)
    }
}
```
通常来说，for range 遍历其他可迭代数据结构时，会有两个返回值，第一个是索引，第二个元素值，但是对于管道而言，有且仅有一个返回值，for range 会不断读取管道中的元素，**当管道缓冲区为空或无缓冲时，就会阻塞等待，直到有其他协程向管道中写入数据才会继续读取数据**

上面的代码会发生死锁，因为子协程已经执行完毕了，而主协程还在阻塞等待其他协程来向管道中写入数据，所以应该管道在写入完毕后将其关闭。修改为如下代码
```go
func main() {
   ch := make(chan int, 10)
   go func() {
      for i := 0; i < 10; i++ {
         ch <- i
      }
      // 关闭管道
      close(ch)
   }()
   for n := range ch {
      fmt.Println(n)
   }
}
```
$\color{red}{说明：关于管道关闭的时机，应该尽量在向管道发送数据的那一方关闭管道，而不要在接收方关闭管道，因为大多数情况下接收方只知道接收数据，并不知道该在什么时候关闭管道}$

## WaitGroup

`sync.WaitGroup` 是 sync 包下提供的一个结构体，WaitGroup 即等待执行，使用它可以很轻易的实现等待一组协程的效果。该结构体只对外暴露三个方法  
Add 方法用于指明要等待的协程的数量
```go
func (wg *WaitGroup) Add(delta int)
```
Done 方法表示当前协程已经执行完毕
```go
func (wg *WaitGroup) Done()
```
Wait 方法等待子协程结束，否则就阻塞
```go
func (wg *WaitGroup) Wait()
```
WaitGroup 使用起来十分简单，属于开箱即用。其内部的实现是计数器+信号量：程序开始时调用 Add 初始化计数，每当一个协程执行完毕时调用 Done，计数就-1，直到减为 0，而在此期间，主协程调用 Wait 会一直阻塞直到全部计数减为 0，然后才会被唤醒
```go
func main() {
    var wait sync.WaitGroup
    // 指定子协程的数量
    wait.Add(1)
    go func() {
        fmt.Println(1)
        // 执行完毕
        wait.Done()
    }()
    // 等待子协程
    wait.Wait()
    fmt.Println(2)
}
```
修改前面的例子
```go
func main() {
   var mainWait sync.WaitGroup
   var wait sync.WaitGroup
   // 计数10
   mainWait.Add(10)
   fmt.Println("start")
   for i := 0; i < 10; i++ {
      // 循环内计数1
      wait.Add(1)
      go func() {
         fmt.Println(i)
         // 两个计数-1
         wait.Done()
         mainWait.Done()
      }()
      // 等待当前循环的协程执行完毕
      wait.Wait()
   }
   // 等待所有的协程执行完毕
   mainWait.Wait()
   fmt.Println("end")
}
```
WaitGroup 通常适用于`可动态调整协程数量`的时候，例如事先知晓协程的数量，又或者在运行过程中需要动态调整。  
WaitGroup 的值不应该被复制，复制后的值也不应该继续使用，尤其是将其作为函数参数传递时，应该传递指针而不是值。倘若使用复制的值，计数完全无法作用到真正的 WaitGroup 上，这可能会导致主协程一直阻塞等待，程序将无法正常运行。例如下方的代码
```go
func main() {
    var mainWait sync.WaitGroup
    mainWait.Add(1)
    hello(mainWait)
    mainWait.Wait()
    fmt.Println("end")
}
func hello(wait sync.WaitGroup) { // 应该使用指针来进行传递
    fmt.Println("hello")
    wait.Done()
}
```
> 当计数变为负数，或者计数数量大于子协程数量时，将会引发 panic

## Context

Context 译为上下文，是 Go 提供的一种并发控制的解决方案，相比于管道和 WaitGroup，它可以更好的控制子孙协程以及层级更深的协程。Context 本身是一个接口，只要实现了该接口都可以称之为上下文；

context 标准库也提供了几个实现，分别是：
- emptyCtx
- cancelCtx
- timerCtx
- valueCtx

### Context 接口

Context 接口的定义
```go
type Context interface {
   Deadline() (deadline time.Time, ok bool)
   Done() <-chan struct{}
   Err() error
   Value(key any) any
}
```
1. Deadline()：该方法具有两个返回值，deadline 是截止时间，即上下文应该取消的时间。第二个值是是否设置 deadline，如果没有设置则一直为 false
2. Done()：其返回值是一个空结构体类型的只读管道，该管道仅仅起到通知作用，不传递任何数据。当上下文所做的工作应该取消时，该通道就会被关闭，对于一些不支持取消的上下文，可能会返回 nil
3. Err()：该方法返回一个 error，用于表示上下关闭的原因。当 Done 管道没有关闭时，返回 nil，如果关闭过后，会返回一个 err 来解释为什么会关闭
4. Value()：该方法返回对应的键值，如果 key 不存在，或者不支持该方法，就会返回 nil

### emptyCtx

emptyCtx 就是空的上下文，context 包下所有的实现都是不对外暴露的，但是提供了对应的函数来创建上下文。emptyCtx 就可以通过 context.Background 和 context.TODO 来进行创建。两个函数如下
```go
type emptyCtx struct{}
type backgroundCtx struct{ emptyCtx }
type todoCtx struct{ emptyCtx }
func Background() Context {
	return backgroundCtx{}
}
func TODO() Context {
	return todoCtx{}
}
```
可以看到仅仅只是返回了 emptyCtx 指针。emptyCtx 的底层类型实际上是一个 struct，之所以不使用空结构体，是因为 emptyCtx 的实例必须要有不同的内存地址，它没法被取消，没有 deadline，也不能取值，实现的方法都是返回零值
```go
type emptyCtx struct{}
func (emptyCtx) Deadline() (deadline time.Time, ok bool) {
	return
}
func (emptyCtx) Done() <-chan struct{} {
	return nil
}
func (emptyCtx) Err() error {
	return nil
}
func (emptyCtx) Value(key any) any {
	return nil
}
```
emptyCtx 通常是用来当作最顶层的上下文，在创建其他三种上下文时作为父上下文传入。

### valueCtx

valueCtx 实现比较简单，其内部只包含一对键值对，和一个内嵌的 Context 类型的字段
```go
type valueCtx struct {
   Context
   key, val any
}
```
其本身只实现了 Value 方法，逻辑也很简单，当前上下文找不到就去父上下文找
```go
func (c *valueCtx) Value(key any) any {
	if c.key == key {
		return c.val
	}
	return value(c.Context, key)
}
```
valueCtx 多用于在多级协程中传递一些数据，无法被取消，因此 ctx.Done 永远会返回 nil，select 会忽略掉 nil 管道
```go
var waitGroup sync.WaitGroup
func main() {
    waitGroup.Add(1)
    // 传入上下文
    go Do(context.WithValue(context.Background(), 1, 2))
    waitGroup.Wait()
}

func Do(ctx context.Context) {
    // 新建定时器
    ticker := time.NewTimer(time.Second)
    defer waitGroup.Done()
    for {
        select {
            case <-ctx.Done(): // 永远也不会执行
            case <-ticker.C:
                fmt.Println("timeout")
                return
            default:
                fmt.Println(ctx.Value(1))
        }
        time.Sleep(time.Millisecond * 100)
    }
}
```


