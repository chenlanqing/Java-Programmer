# 一、响应式基础

## 1、什么是响应式编程

基于事件驱动模式来进行编程的（异步数据流）

## 2、BackPressure

从字面上意思可以理解为回压

首先解释一下回压，它就好比用吸管喝饮料，将吸管内的气体吸掉，吸管内形成低压，进而形成饮料至吸管方向的吸力，此吸力将饮料吸进人嘴里。我们常说人往高处走，水往低处流，水之所以会出现这种现象，其实是重力所致。而现在吸管下方的水上升进入人的口中，说明出现了下游指向上游的逆向压力，而且这个逆向压力大于重力，可以称这种情况为BackPressure。这是一个很直观的词，向后的、往回的压力——Back Pressure；

放在程序中，也就是在数据流从上游源生产者向下游消费者传输的过程中，若上游源生产速度大于下游消费者消费速度，那么可以将下游想象成一个容器，它处理不了这些数据，然后数据就会从容器中溢出，也就出现了类似于吸管例子中的情况。现在，我们要做的事情就是为这个场景提供解决方案，该解决方案被称为BackPressure机制；

BackPressure机制应该具有承载元素的能力，也就是它必须是一个容器，而且其存储与下发的元素应该有先后顺序，那么这里使用队列是最适合的了。BackPressure机制仅起承载作用是不够的，正因为上游进行了承压，所以下游可以按需请求元素，也可以在中间根据实际情况进行限流，以此上下游共同实现了BackPressure机制；

## 3、Flow API

JDK9 中推出了 Flow API，用以支持 Reactive Programming，即响应式编程；在响应式编程中，会有一个数据发布者 Publisher 和数据订阅者 Subscriber，Subscriber 接收 Publisher 发布的数据并进行消费，在 Subscriber 和 Publisher 之间还存在一个 Processor，类似于一个过滤器，可以对数据进行中间处理
```java
public final class Flow {
    private Flow() {} // uninstantiable
    /**
     * Publisher 为数据发布者，这是一个函数式接口，里边只有一个方法，通过这个方法将数据发布出去
     */
    @FunctionalInterface
    public static interface Publisher<T> {
        public void subscribe(Subscriber<? super T> subscriber);
    }
    /**
     * 数据订阅者
     */
    public static interface Subscriber<T> {
        /**
         * 这个是订阅成功的回调方法，用于初始化 Subscription，并且表明可以开始接收订阅数据了
         */
        public void onSubscribe(Subscription subscription);
        /**
         * 接收下一项订阅数据的回调方法
         */
        public void onNext(T item);
        /**
         * 在 Publisher 或 Subcriber 遇到不可恢复的错误时调用此方法，之后 Subscription 不会再调用 Subscriber 其他的方法
         */
        public void onError(Throwable throwable);
        /**
         * 当接收完所有订阅数据，并且发布者已经关闭后会回调这个方法
         */
        public void onComplete();
    }
    /**
     * Subscription 为发布者和订阅者之间的订阅关系，用来控制消息的消费
     */
    public static interface Subscription {
        /**
         * 这个方法用来向数据发布者请求 n 个数据
         */
        public void request(long n);
        /**
         * 取消消息订阅，订阅者将不再接收数据
         */
        public void cancel();
    }

    /**
     * 它既能发布数据也能订阅数据，因此我们可以通过 Processor 来完成一些数据转换的功能，先接收数据进行处理，处理完成后再将数据发布出去
     */
    public static interface Processor<T,R> extends Subscriber<T>, Publisher<R> {
    }
    // Subscriber 会将 Publisher 发布的数据缓存在 Subscription 中，其长度默认为256， 一旦超出这个数据量，publisher 就会降低数据发送速度
    static final int DEFAULT_BUFFER_SIZE = 256;
    public static int defaultBufferSize() {
        return DEFAULT_BUFFER_SIZE;
    }
}
```

FlowAPI解决 back pressure 是通过 DEFAULT_BUFFER_SIZE 来解决的，生产者会先生产 257 条数据，消息则是一条一条的来，由于消费的速度比较慢，所以当缓存中的数据超过 256 条之后，接下来都是消费一条，再发送一条

# 二、Spring WebFlux

## 1、异步Servlet

在 Servlet3.0 之前，Servlet 采用 Thread-Per-Request 的方式处理 Http 请求，即每一次请求都是由某一个线程从头到尾负责处理；

在Servlet3.0之后呢，增加了异步的Servlet来实现：
```java
@WebServlet(urlPatterns = "/async", asyncSupported = true)
```

## 2、什么是WebFlux

Spring WebFlux 是一个异步非阻塞式 IO 模型，通过少量的容器线程就可以支撑大量的并发访问，所以 Spring WebFlux 可以有效提升系统的吞吐量和伸缩性，特别是在一些 IO 密集型应用中，Spring WebFlux 的优势明显；

> 不过需要注意的是，接口的响应时间并不会因为使用了 WebFlux 而缩短，服务端的处理结果还是得由 worker 线程处理完成之后再返回给前端

WebFlux 底层使用 Netty 容器，默认端口是 8080;

## 3、什么是 Reactor

Spring Reactor 是 Pivotal 团队基于反应式编程实现的一种方案，这是一种非阻塞，并且由事件驱动的编程方案，它使用函数式编程实现；

Reactor 是一个用于 JVM 的完全非阻塞的响应式编程框架，具备高效的需求管理，可以很好的处理 “backpressure”，它可以直接与 Java8 的函数式 API 直接集成，例如 CompletableFuture、各种 Stream 等；

Reactor 还提供了异步序列 API Flux（用于 N 个元素）和 Mono（用于 0|1 个元素），并完全遵循和实现了“响应式扩展规范

两个重要API，它俩都是 Publisher
- Mono：实现发布者 Publisher，并返回 0 或 1 个元素。
- Flux：实现发布者 Publisher，并返回 N 个元素


# 参考资料

- [Reactor3官方文档](https://projectreactor.io/docs/core/release/reference/)
- [The introduction to Reactive Programming you've been missing](https://gist.github.com/staltz/868e7e9bc2a7b8c1f754)
- [江南一点雨-WebFlux](http://www.javaboy.org/2021/0622/webflux-mysql.html)
