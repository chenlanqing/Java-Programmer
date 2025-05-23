# 一、使用

Future 在实际使用过程中存在一些局限性比如不支持异步任务的编排组合、获取计算结果的 get() 方法为阻塞调用

Java 8 才被引入CompletableFuture 类可以解决Future 的这些缺陷。CompletableFuture 除了提供了更为好用和强大的 Future 特性之外，还提供了函数式编程、异步任务编排组合（可以将多个异步任务串联起来，组成一个完整的链式调用）等能力

它实现了Future接口，也就是Future的功能特性CompletableFuture也有；除此之外，它也实现了 CompletionStage 接口，CompletionStage 接口定义了任务编排的方法，执行某一阶段，可以向下执行后续阶段。
```java
public class CompletableFuture<T> implements Future<T>, CompletionStage<T> {
}
```
CompletionStage 接口描述了一个异步计算的阶段。很多计算可以分成多个阶段或步骤，此时可以通过它将所有步骤组合起来，形成异步计算的流水线

CompletableFuture相比于Future最大的改进就是提供了类似观察者模式的回调监听的功能，也就是当上一阶段任务执行结束之后，可以回调你指定的下一阶段任务，而不需要阻塞获取结果之后来处理结果

## 1、创建对象

常见创建 CompletableFuture 对象：
```java
// 使用默认线程池
static CompletableFuture<Void> runAsync(Runnable runnable)
static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier)
// 可以指定线程池  
static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor)
static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor)  
```
**runAsync 和 supplyAsync 方法区别**：Runnable 接口的 run() 方法没有返回值，而 Supplier 接口的 get() 方法是有返回值的；

上面代码中前面两个方法和后面两个方法的区别：前两个使用的是公共的 ForkJoinPool 线程池，后两个方法可以指定线程池参数
```java
// 默认当前CPU核数，也可以通过JVM `-Djava.util.concurrent.ForkJoinPool.common.parallelism=1`来设置ForkJoinPool线程池的线程数
private static final boolean USE_COMMON_POOL = (ForkJoinPool.getCommonPoolParallelism() > 1);
// 默认线程池
private static final Executor ASYNC_POOL = USE_COMMON_POOL ? ForkJoinPool.commonPool() : new ThreadPerTaskExecutor();
public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
    return asyncSupplyStage(ASYNC_POOL, supplier);
}
```
如果所有 CompletableFuture 共享一个线程池，那么一旦有任务执行一些很慢的 `I/O` 操作，比如数据库、文件操作等，就会导致线程池中所有线程都阻塞在 I/O 操作上，从而造成线程饥饿，进而影响整个系统的性能。所以，强烈建议你要根据不同的业务类型创建不同的线程池，以避免互相干扰；

创建完 CompletableFuture 对象之后，会自动地异步执行 `runnable.run()` 方法或者 `supplier.get()` 方法，对于一个异步操作，你需要关注两个问题：一个是异步操作什么时候结束，另一个是如何获取异步操作的执行结果。因为 CompletableFuture 类实现了 Future 接口，所以这两个问题你都可以通过 Future 接口来解决；

## 2、CompletionStage 接口

任务是有时序关系的，比如有串行关系、并行关系、汇聚关系等。

### 2.1、描述串行关系

CompletionStage 接口里面描述串行关系，主要是 thenApply、thenAccept、thenRun 和 thenCompose 这四个系列的接口
- `thenApply` 系列函数里参数 fn 的类型是接口 Function，这个接口里与 CompletionStage 相关的方法是 `R apply(T t)`，这个方法既能接收参数也支持返回值，所以 thenApply 系列方法返回的是CompletionStage。
- `thenAccept` 系列方法里参数 consumer 的类型是接口Consumer，这个接口里与 CompletionStage 相关的方法是 `void accept(T t)`，这个方法虽然支持参数，但却不支持回值，所以 thenAccept 系列方法返回的是CompletionStage。
- `thenRun` 系列方法里 action 的参数是 Runnable，所以 action 既不能接收参数也不支持返回值，所以 thenRun 系列方法返回的也是CompletionStage；
- 这些方法里面 Async 代表的是异步执行 fn、consumer 或者 action。其中，需要你注意的是 thenCompose 系列方法，这个系列的方法会新创建出一个子流程，最终结果和 thenApply 系列是相同的；

```java
CompletionStage<R> thenApply(fn);
CompletionStage<R> thenApplyAsync(fn);
CompletionStage<Void> thenAccept(consumer);
CompletionStage<Void> thenAcceptAsync(consumer);
CompletionStage<Void> thenRun(action);
CompletionStage<Void> thenRunAsync(action);
CompletionStage<R> thenCompose(fn);
CompletionStage<R> thenComposeAsync(fn);
```

### 2.2、描述 AND 汇聚关系

CompletionStage 接口里面描述 AND 汇聚关系，主要是 thenCombine、thenAcceptBoth 和 runAfterBoth 系列的接口，这些接口的区别也是源自 fn、consumer、action 这三个核心参数不同
```java
CompletionStage<R> thenCombine(other, fn);
CompletionStage<R> thenCombineAsync(other, fn);
CompletionStage<Void> thenAcceptBoth(other, consumer);
CompletionStage<Void> thenAcceptBothAsync(other, consumer);
CompletionStage<Void> runAfterBoth(other, action);
CompletionStage<Void> runAfterBothAsync(other, action);
```

### 2.3、描述 OR 汇聚关系

CompletionStage 接口里面描述 OR 汇聚关系，主要是 applyToEither、acceptEither 和 runAfterEither 系列的接口，这些接口的区别也是源自 fn、consumer、action 这三个核心参数不同
```java
CompletionStage applyToEither(other, fn);
CompletionStage applyToEitherAsync(other, fn);
CompletionStage acceptEither(other, consumer);
CompletionStage acceptEitherAsync(other, consumer);
CompletionStage runAfterEither(other, action);
CompletionStage runAfterEitherAsync(other, action);
```

### 2.4、异常处理

上面提到的 fn、consumer、action 它们的核心方法都不允许抛出可检查异常，但是却无法限制它们抛出运行时异常，如下面的代码，执行 7/0 就会出现除零错误这个运行时异常。非异步编程里面，我们可以使用 try{}catch{}来捕获并处理异常，那在异步编程里面，异常该如何处理呢？
```java
CompletableFuture<Integer> f0 = CompletableFuture.supplyAsync(() -> (7 / 0))
        .thenApply(r -> r * 10);
```
CompletionStage 接口给我们提供的方案非常简单，比 try{}catch{}还要简单，下面是相关的方法，使用这些方法进行异常处理和串行操作是一样的，都支持链式编程方式
```java
CompletionStage exceptionally(fn);
CompletionStage<R> whenComplete(consumer);
CompletionStage<R> whenCompleteAsync(consumer);
CompletionStage<R> handle(fn);
CompletionStage<R> handleAsync(fn);
```
- exceptionally() 的使用非常类似于 `try{}catch{}`中的 `catch{}`；方法可以处理异常并重新抛出，以便异常能够传播到后续阶段，而不是让异常被忽略或终止
- `whenComplete()` 和 `handle()` 系列方法就类似于 `try{}finally{}`中的 finally{}，无论是否发生异常都会执行 whenComplete() 中的回调函数 consumer 和 handle() 中的回调函数 fn
- whenComplete() 和 handle() 的区别在于 whenComplete() 不支持返回结果，而 handle() 是支持返回结果的

异常处理示例：
```java
CompletableFuture<Integer> f0 = CompletableFuture.supplyAsync(() -> (7 / 0))
        .thenApply(r -> r * 10)
        .exceptionally(throwable -> {
            throwable.printStackTrace();
            return 0;
        });
System.out.println(f0.join());
```

## 3、使用场景

比如任务之间存在依赖关系：一个任务需要依赖另外两个任务执行完之后再执行
```java
// T1
CompletableFuture<Void> futureT1 = CompletableFuture.runAsync(() -> {
    System.out.println("T1 is executing. Current time：" + DateUtil.now());
    // 模拟耗时操作
    ThreadUtil.sleep(1000);
});
// T2
CompletableFuture<Void> futureT2 = CompletableFuture.runAsync(() -> {
    System.out.println("T2 is executing. Current time：" + DateUtil.now());
    ThreadUtil.sleep(1000);
});
// 使用allOf()方法合并T1和T2的CompletableFuture，等待它们都完成
CompletableFuture<Void> bothCompleted = CompletableFuture.allOf(futureT1, futureT2);
// 当T1和T2都完成后，执行T3
bothCompleted.thenRunAsync(() -> System.out.println("T3 is executing after T1 and T2 have completed.Current time：" + DateUtil.now()));
// 等待所有任务完成，验证效果
ThreadUtil.sleep(3000);
```

## 4、最佳实践

- **使用自定义线程池**： CompletableFuture 默认使用 ForkJoinPool.commonPool() 作为执行器，这个线程池是全局共享的，可能会被其他任务占用，导致性能下降或者饥饿。因此，建议使用自定义的线程池来执行 CompletableFuture 的异步任务，可以提高并发度和灵活性；

- **尽量避免使用 get()**： CompletableFuture的get() 方法是阻塞的，尽量避免使用。如果必须要使用的话，需要添加超时时间（`get(long timeout, TimeUnit unit)`），否则可能会导致主线程一直等待，无法执行其他任务；如果是JDK9以上，可以直接使用 orTimeout 和 completeOnTimeout 方法：`CompletableFuture.supplyAsync(() -> {}).orTimeout(3, TimeUnit.SECONDS);`

- **正确进行异常处理**：CompletableFuture 在使用异步处理过程中，需要注意异常的处理，因为 CompletableFuture 很多方法都不能抛出异常，如果在异步执行过程中出现了异常，那么异常将被吞掉了，没有办法显示，为了处理异常，可以按照上述的方式来处理：
    ```java
    CompletableFuture.supplyAsync(() -> (7 / 0))
            .thenApply(r -> r * 10)
            .exceptionally(throwable -> {
                throwable.printStackTrace();
                return 0;
            });
    ```
    使用 exceptionally 或者 whenComplete 来实现来处理异常

- **合理组合多个异步任务**：正确使用 thenCompose() 、 thenCombine() 、acceptEither()、allOf()、anyOf() 等方法来组合多个异步任务，以满足实际业务的需求，提高程序执行效率

- **线程上下文传递问题：** CompletableFuture 默认不会传递线程上下文（如 ThreadLocal），这可能导致上下文丢失

# 二、源码分析

## 1、解决问题

CompletableFuture是由Java 8引入的，在Java8之前我们一般通过Future实现异步。
- Future用于表示异步计算的结果，只能通过阻塞或者轮询的方式获取结果，而且不支持设置回调方法，Java 8之前若要设置回调一般会使用guava的ListenableFuture，回调的引入又会导致臭名昭著的回调地狱（下面的例子会通过ListenableFuture的使用来具体进行展示）。
- CompletableFuture对Future进行了扩展，可以通过设置回调的方式处理计算结果，同时也支持组合操作，支持进一步的编排，同时一定程度解决了回调地狱的问题
```java
// 并行运行所有任务；方法可以组合多个 CompletableFuture，并统一处理所有任务的异常，而不是让异常处理过于冗长或重复
CompletableFuture<Void> allOf(CompletableFuture<?>... cfs)
// 任意一个任务完成
CompletableFuture<Object> anyOf(CompletableFuture<?>... cfs)
```

# 参考资料

- [CompletableFuture原理与实践-外卖商家端API的异步化](https://tech.meituan.com/2022/05/12/principles-and-practices-of-completablefuture.html)
- [asyncTool-并行框架](https://gitee.com/jd-platform-opensource/asyncTool)
