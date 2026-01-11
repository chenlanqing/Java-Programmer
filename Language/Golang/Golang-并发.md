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

### cancelCtx

cancelCtx 以及 timerCtx 都实现了 canceler 接口，接口类型如下
```go
type canceler interface {
    // removeFromParent 表示是否从父上下文中删除自身
    // err 表示取消的原因
  cancel(removeFromParent bool, err error)
    // Done 返回一个管道，用于通知取消的原因
  Done() <-chan struct{}
}
```
cancel 方法不对外暴露，在创建上下文时通过闭包将其包装为返回值以供外界调用，就如 context.WithCancel 源代码中所示
```go
func WithCancel(parent Context) (ctx Context, cancel CancelFunc) {
    if parent == nil {
        panic("cannot create context from nil parent")
    }
    c := newCancelCtx(parent)
    // 尝试将自身添加进父级的children中
    propagateCancel(parent, &c)
    // 返回context和一个函数
    return &c, func() { c.cancel(true, Canceled) }
}
```
cancelCtx 译为可取消的上下文，创建时，如果父级实现了 canceler，就会将自身添加进父级的 children 中，否则就一直向上查找。如果所有的父级都没有实现 canceler，就会启动一个协程等待父级取消，然后当父级结束时取消当前上下文。当调用 cancelFunc 时，Done 通道将会关闭，该上下文的任何子级也会随之取消，最后会将自身从父级中删除
```go
var waitGroup sync.WaitGroup
func main() {
	bkg := context.Background()
	// 返回了一个cancelCtx和cancel函数
	cancelCtx, cancel := context.WithCancel(bkg)
	waitGroup.Add(1)
	go func(ctx context.Context) {
		defer waitGroup.Done()
		for {
			select {
			case <-ctx.Done():
				fmt.Println(ctx.Err())
				return
			default:
				fmt.Println("等待取消中...")
			}
			time.Sleep(time.Millisecond * 200)
		}
	}(cancelCtx)
	time.Sleep(time.Second)
	cancel()
	waitGroup.Wait()
}
```

### timerCtx

timerCtx 在 cancelCtx 的基础之上增加了超时机制，context 包下提供了两种创建的函数，分别是 WithDeadline 和 WithTimeout，两者功能类似，前者是指定一个具体的超时时间，比如指定一个具体时间 2023/3/20 16:32:00，后者是指定一个超时的时间间隔，比如 5 分钟后。两个函数的签名如下
```go
func WithDeadline(parent Context, d time.Time) (Context, CancelFunc)
func WithTimeout(parent Context, timeout time.Duration) (Context, CancelFunc)
```
timerCtx 会在时间到期后自动取消当前上下文，取消的流程除了要额外的关闭 timer 之外，基本与 cancelCtx 一致
```go
var wait sync.WaitGroup
func main() {
    deadline, cancel := context.WithDeadline(context.Background(), time.Now().Add(time.Second))
    defer cancel() // 保险起见，最好手动取消上下文
    wait.Add(1)
    go func(ctx context.Context) {
        defer wait.Done()
        for {
        select {
        case <-ctx.Done():
            fmt.Println("上下文取消", ctx.Err())
            return
        default:
            fmt.Println("等待取消中...")
        }
        time.Sleep(time.Millisecond * 200)
        }
    }(deadline)
    wait.Wait()
}
```
WithTimeout 其实与 WithDealine 非常相似，它的实现也只是稍微封装了一下并调用 WithDeadline

## select

### 基本概念

select 在 Linux 系统中，是一种 IO 多路复用的解决方案；类似的，在 Go 中，select 是一种管道多路复用的控制结构。什么是多路复用，简单的用一句话概括：在某一时刻，同时监测多个元素是否可用，被监测的可以是网络请求，文件 IO 等。**在 Go 中的 select 监测的元素就是管道，且只能是管道**。select 的语法与 switch 语句类似
```go
func main() {
    // 创建三个管道
    chA := make(chan int)
    chB := make(chan int)
    chC := make(chan int)
    defer func() {
        close(chA)
        close(chB)
        close(chC)
    }()
    select {
    case n, ok := <-chA:
        fmt.Println(n, ok)
    case n, ok := <-chB:
        fmt.Println(n, ok)
    case n, ok := <-chC:
        fmt.Println(n, ok)
    default:
        fmt.Println("所有管道都不可用")
    }
}
```
与 switch 类似：
- select 由多个 case 和一个 default 组成，default 分支可以省略。
- 每一个 case 只能操作一个管道，且只能进行一种操作，要么读要么写；
- 当有多个 case 可用时，select 会伪随机的选择一个 case 来执行。
- 如果所有 case 都不可用，就会执行 default 分支，倘若没有 default 分支，将会阻塞等待，直到至少有一个 case 可用。
- 由于上例中没有对管道写入数据，自然所有的 case 都不可用，所以最终输出为 default 分支的执行结果
```go
func main() {
    chA := make(chan int)
    chB := make(chan int)
    chC := make(chan int)
    defer func() {
        close(chA)
        close(chB)
        close(chC)
    }()

    l := make(chan struct{})

    go Send(chA)
    go Send(chB)
    go Send(chC)

    go func() {
    Loop:
        for {
            select {
                case n, ok := <-chA:
                    fmt.Println("A", n, ok)
                case n, ok := <-chB:
                    fmt.Println("B", n, ok)
                case n, ok := <-chC:
                    fmt.Println("C", n, ok)
                case <-time.After(time.Second): // 设置1秒的超时时间
                    break Loop // 退出循环
            }
        }
        l <- struct{}{} // 告诉主协程可以退出了
    }()
    <-l
}
func Send(ch chan<- int) {
  for i := 0; i < 3; i++ {
    time.Sleep(time.Millisecond)
    ch <- i
  }
}
```

### 超时



### 阻塞

当 select 语句中什么都没有时，就会永久阻塞，例如
```go
func main() {
    fmt.Println("start")
    select {}
    fmt.Println("end")
}
```
end 永远也不会输出，主协程会一直阻塞，这种情况一般是有特殊用途

在 select 的 case 中对值为 nil 的管道进行操作的话，并不会导致阻塞，该 case 则会被忽略，永远也不会被执行；

### 非阻塞

通过使用select的default分支配合管道，可以实现非阻塞的收发操作
```go
func TrySend(ch chan int, ele int) bool  {
	select {
	case ch <- ele:
		return true
	default:
		return false
	}
}
func TryRecv(ch chan int) (int, bool)  {
	select {
	case ele, ok := <-ch:
		return ele, ok
	default:
		return 0, false
	}
}
```
可以实现非阻塞的判断一个context是否已经结束
```go
func IsDone(ctx context.Context) bool {
	select {
	case <-ctx.Done():
		return true
	default:
		return false
	}
}
```

## 锁

Go 中 sync 包下的 Mutex 与 RWMutex 提供了互斥锁与读写锁两种实现，且提供了非常简单易用的 API，加锁只需要 Lock()，解锁也只需要 Unlock()。需要注意的是，Go 所提供的锁都是非递归锁，也就是不可重入锁，所以重复加锁或重复解锁都会导致 fatal。锁的意义在于保护不变量，加锁是希望数据不会被其他协程修改，如下
```go
func DoSomething() {
    Lock()
    // 在这个过程中，数据不会被其他协程修改
    Unlock()
}
```
倘若是递归锁的话，就可能会发生如下情况
```go
func DoSomething() {
    Lock()
        DoOther()
    Unlock()
}
func DoOther() {
    Lock()
    // do other
    Unlock()
}
```
DoSomething 函数显然不知道 DoOther 函数可能会对数据做点什么，从而修改了数据，比如再开几个子协程破坏了不变量。这在 Go 中是行不通的，一旦加锁以后就必须保证不变量的不变性，此时重复加锁解锁都会导致死锁。所以在编写代码时应该避免上述情况，必要时在加锁的同时立即使用 defer 语句解锁

### 互斥锁

`sync.Mutex` 是 Go 提供的互斥锁实现，其实现了 `sync.Locker` 接口
```go
type Locker interface {
    // 加锁
    Lock()
    // 解锁
    Unlock()
}
```
示例：
```go
var wait sync.WaitGroup
var count = 0

var lock sync.Mutex

func main() {
  wait.Add(10)
  for i := 0; i < 10; i++ {
    go func(data *int) {
      // 加锁
      lock.Lock()
      // 模拟访问耗时
      time.Sleep(time.Millisecond * time.Duration(rand.Intn(1000)))
      // 访问数据
      temp := *data
      // 模拟计算耗时
      time.Sleep(time.Millisecond * time.Duration(rand.Intn(1000)))
      ans := 1
      // 修改数据
      *data = temp + ans
      // 解锁
      lock.Unlock()
      fmt.Println(*data)
      wait.Done()
    }(&count)
  }
  wait.Wait()
  fmt.Println("最终结果", count)
}
```

### 读写锁

互斥锁适合读操作与写操作频率都差不多的情况，对于一些读多写少的数据，如果使用互斥锁，会造成大量的不必要的协程竞争锁，这会消耗很多的系统资源，这时候就需要用到读写锁，即读写互斥锁，对于一个协程而言：
- 如果获得了读锁，其他协程进行写操作时会阻塞，其他协程进行读操作时不会阻塞
- 如果获得了写锁，其他协程进行写操作时会阻塞，其他协程进行读操作时会阻塞

Go 中读写互斥锁的实现是 sync.RWMutex，它也同样实现了 Locker 接口，但它提供了更多可用的方法，如下：
```go
// 加读锁
func (rw *RWMutex) RLock()
// 尝试加读锁
func (rw *RWMutex) TryRLock() bool
// 解读锁
func (rw *RWMutex) RUnlock()
// 加写锁
func (rw *RWMutex) Lock()
// 尝试加写锁
func (rw *RWMutex) TryLock() bool
// 解写锁
func (rw *RWMutex) Unlock()
```
其中 TryRLock 与 TryLock 两个尝试加锁的操作是非阻塞式的，成功加锁会返回 true，无法获得锁时并不会阻塞而是返回 false。读写互斥锁内部实现依旧是互斥锁，并不是说分读锁和写锁就有两个锁，从始至终都只有一个锁

> 对于锁而言，不应该将其作为值传递和存储，应该使用指针

### 条件变量

条件变量，与互斥锁一同出现和使用，所以有些人可能会误称为条件锁，但它并不是锁，是一种通讯机制。Go 中的 sync.Cond 对此提供了实现，而创建条件变量的函数签名如下：
```go
func NewCond(l Locker) *Cond
```
创建一个条件变量前提就是需要创建一个锁，sync.Cond 提供了如下的方法以供使用
```go
// 阻塞等待条件生效，直到被唤醒
func (c *Cond) Wait()
// 唤醒一个因条件阻塞的协程
func (c *Cond) Signal()
// 唤醒所有因条件阻塞的协程
func (c *Cond) Broadcast()
```
> 对于条件变量，应该使用 for 而不是 if，应该使用循环来判断条件是否满足，因为协程被唤醒时并不能保证当前条件就已经满足了
```go
for !condition {
    cond.Wait()
}
```

## Once

当在使用一些数据结构时，如果这些数据结构太过庞大，可以考虑采用懒加载的方式，即真正要用到它的时候才会初始化该数据结构
```go
type MySlice []int
func (m *MySlice) Get(i int) (int, bool) {
    if *m == nil {
        return 0, false
    } else {
        return (*m)[i], true
    }
}
func (m *MySlice) Add(i int) {
   // 当真正用到切片的时候，才会考虑去初始化
   if *m == nil {
      *m = make([]int, 0, 10)
   }
   *m = append(*m, i)
}
```
如果只有一个协程使用肯定是没有任何问题的，但是如果有多个协程访问的话就可能会出现问题了。比如协程 A 和 B 同时调用了 Add 方法，A 执行的稍微快一些，已经初始化完毕了，并且将数据成功添加，随后协程 B 又初始化了一遍，这样一来将协程 A 添加的数据直接覆盖掉了，这就是问题所在;

这就是 sync.Once 要解决的问题，顾名思义，Once 译为一次，sync.Once 保证了在并发条件下指定操作只会执行一次。它的使用非常简单，只对外暴露了一个 Do 方法，签名如下：
```go
func (o *Once) Do(f func())
```
在使用时，只需要将初始化操作传入 Do 方法即可，其原理就是锁+原子操作。
```go
func (o *Once) Do(f func())
```

## Pool

sync.Pool 的设计目的是用于存储临时对象以便后续的复用，是一个临时的并发安全对象池，将暂时用不到的对象放入池中，在后续使用中就不需要再额外的创建对象可以直接复用，减少内存的分配与释放频率，最重要的一点就是降低 GC 压力。sync.Pool 总共只有两个方法，如下：
```go
// 申请一个对象
func (p *Pool) Get() any
// 放入一个对象
func (p *Pool) Put(x any)
```
并且 sync.Pool 有一个对外暴露的 New 字段，用于对象池在申请不到对象时初始化一个对象
```go
New func() any
```
示例
```go
var wait sync.WaitGroup
// 临时对象池
var pool sync.Pool
// 用于计数过程中总共创建了多少个对象
var numOfObject atomic.Int64
// BigMemData 假设这是一个占用内存很大的结构体
type BigMemData struct {
     M string
}
func main() {
    pool.New = func() any {
        numOfObject.Add(1)
        return BigMemData{"大内存"}
    }
    wait.Add(1000)
    // 这里开启1000个协程
    for i := 0; i < 1000; i++ {
        go func() {
            // 申请对象
            val := pool.Get()
            // 使用对象
            _ = val.(BigMemData)
            // 用完之后再释放对象
            pool.Put(val)
            wait.Done()
        }()
    }
    wait.Wait()
    fmt.Println(numOfObject.Load())
}
```
在使用 sync.Pool 时需要注意几个点：
- 临时对象：sync.Pool 只适合存放临时对象，池中的对象可能会在没有任何通知的情况下被 GC 移除，所以并不建议将网络链接，数据库连接这类存入 sync.Pool 中。
- 不可预知：sync.Pool 在申请对象时，无法预知这个对象是新创建的还是复用的，也无法知晓池中有几个对象
- 并发安全：官方保证 sync.Pool 一定是并发安全，但并不保证用于创建对象的 New 函数就一定是并发安全的，New 函数是由使用者传入的，所以 New 函数的并发安全性要由使用者自己来维护，这也是为什么上例中对象计数要用到原子值的原因。

标准库 fmt 包下就有一个对象池的使用案例，在 `fmt.Fprintf` 函数中

## Map

sync.Map 是官方提供的一种并发安全 Map 的实现，开箱即用，使用起来十分的简单，下面是该结构体对外暴露的方法：
```go
// 根据一个key读取值，返回值会返回对应的值和该值是否存在
func (m *Map) Load(key any) (value any, ok bool)
// 存储一个键值对
func (m *Map) Store(key, value any)
// 删除一个键值对
func (m *Map) Delete(key any)
// 如果该key已存在，就返回原有的值，否则将新的值存入并返回，当成功读取到值时，loaded为true，否则为false
func (m *Map) LoadOrStore(key, value any) (actual any, loaded bool)
// 删除一个键值对，并返回其原有的值，loaded的值取决于key是否存在
func (m *Map) LoadAndDelete(key any) (value any, loaded bool)
// 遍历Map，当f()返回false时，就会停止遍历
func (m *Map) Range(f func(key, value any) bool)
```
为了并发安全肯定需要做出一定的牺牲，sync.Map 的性能要比 map 低 10-100 倍左右

## 原子

### 类型

Go 标准库 sync/atomic 包下已经提供了原子操作相关的 API，其提供了以下几种类型以供进行原子操作
```go
atomic.Bool{}
atomic.Pointer[]{}
atomic.Int32{}
atomic.Int64{}
atomic.Uint32{}
atomic.Uint64{}
atomic.Uintptr{}
atomic.Value{}
```
其中 Pointer 原子类型支持泛型，Value 类型支持存储任何类型，除此之外，还提供了许多函数来方便操作。因为原子操作的粒度过细，在大多数情况下，更适合处理这些基础的数据类型  
> atmoic 包下原子操作只有函数签名，没有具体实现，具体的实现是由 plan9 汇编编写

### 使用

每一个原子类型都会提供以下三个方法：
- Load()：原子的获取值
- Swap(newVal type) (old type)：原子的交换值，并且返回旧值
- Store(val type)：原子的存储值

不同的类型可能还会有其他的额外方法，比如整型类型都会提供 Add 方法来实现原子加减操作。下面以一个 int64 类型演示为例：
```go
func main() {
    var aint64 atomic.Uint64
    // 存储值
    aint64.Store(64)
    // 交换值
    aint64.Swap(128)
    // 增加
    aint64.Add(112)
    // 加载值
    fmt.Println(aint64.Load())
}
// 直接使用函数
func main() {
    var aint64 int64
    // 存储值
    atomic.StoreInt64(&aint64, 64)
    // 交换值
    atomic.SwapInt64(&aint64, 128)
    // 增加
    atomic.AddInt64(&aint64, 112)
    // 加载
    fmt.Println(atomic.LoadInt64(&aint64))
}
```

### CAS

atomic 包还提供了 CompareAndSwap 操作，也就是 CAS。它是实现乐观锁和无锁数据结构的核心。  
乐观锁本身并不是锁，是一种并发条件下无锁化并发控制方式：线程/协程在修改数据前，不会先加锁，而是先读取数据，进行计算，然后在提交修改时使用CAS来判断在此期间是否有其他线程修改过该数据。如果没有（值仍等于之前读取的值），则修改成功；否则，失败并重试。  
因此之所以被称作乐观锁，是因为它总是乐观的假设共享数据不会被修改，仅当发现数据未被修改时才会去执行对应操作，而互斥量就是悲观锁，互斥量总是悲观的认为共享数据肯定会被修改，所以在操作时会加锁，操作完毕后就会解锁。  
由于无锁化实现的并发，其安全性和效率相对于锁要高一些，许多并发安全的数据结构都采用了 CAS 来进行实现，不过真正的效率要结合具体使用场景来看
```go
var count int64
func Add(num int64) {
    for {
        expect := atomic.LoadInt64(&count)
        if atomic.CompareAndSwapInt64(&count, expect, expect+num) {
            break
        }
    }
}
```
对于 CAS 而言，有三个参数：内存值，期望值，新值。执行时，CAS 会将期望值与当前内存值进行比较，如果内存值与期望值相同，就会执行后续的操作，否则的话什么也不做。对于 Go 中 atomic 包下的原子操作，CAS 相关的函数则需要传入地址、期望值、新值，且会返回是否成功替换的布尔值。例如 int64 类型的 CAS 操作函数签名如下：
```go
func CompareAndSwapInt64(addr *int64, old, new int64) (swapped bool)
```
大多数情况下，仅仅只是比较值是无法做到并发安全的，比如因 CAS 引起 ABA 问题，就需要使用额外加入 version 来解决问题

### Value

atomic.Value 结构体，可以存储任意类型的值，结构体如下
```go
type Value struct {
   // any类型
   v any
}
```
尽管可以存储任意类型，但是它不能存储 nil，并且前后存储的值类型应当一致

> 需要注意的是，所有的原子类型都不应该复制值，而是应该使用它们的指针