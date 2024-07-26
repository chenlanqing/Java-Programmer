# 一、多线程并发最佳实践

- 1、使用本地变量；
- 2、使用不可变类；
- 3、最小化锁的作用域范围:S = 1 / (1 - a + a/n)
- 4、使用线程池，而不是直接new Thread执行；
- 5、宁可使用同步也不要使用线程的wait和notify(可以使用CountDownLatch)；
- 6、使用BlockingQueue实现生产-消费模式；
- 7、使用并发集合而不是加了锁的同步集合；
- 8、使用Semaphore创建有界的访问；
- 9、宁可使用同步代码块也不要使用同步方法(synchronized)；
- 10、避免使用静态变量，如果一定要用静态变量，可以声明为 final；

# 二、生产者与消费者

## 1、问题描述

在并发编程中使用生产者和消费者模式能够解决绝大多数并发问题

- 生产者消费者模式
    在线程世界里，生产者就是生产数据的线程，消费者就是消费数据的线程。生产者消费者模式是通过一个容器来解决生产者和消费者的强耦合问题。生产者和消费者彼此之间不直接通讯，而通过阻塞队列来进行通讯；

- 解决生产者\消费者问题的方法可以分为两类：
    * 采用某种机制保护生产者与消费者的同步；
    * 在生产者和消费者之间建立管道；

    第一种方式有较高的效率，并且易于实现，代码控制性好，属于常用实现模式；<br>
    第二种管道缓冲区不易控制，被传输的数据不易封装.

- 生产者\消费者经典的实现是ThreadPoolExecutor与工作队列的关系

## 2、实现

* [各种实现代码](https://github.com/chenlanqing/example/blob/master/src/main/java/com/learning/example/thread/producer/readme.md)

### 2.1、wait()/notify()方法

wait() / nofity()[notifyAll()]方法是基类Object的两个方法：
- wait()方法：当缓冲区已满/空时，生产者/消费者线程停止自己的执行，放弃锁，使自己处于等等状态，让其他线程执行。
- notify()方法：当生产者/消费者向缓冲区放入/取出一个产品时，向其他等待的线程发出可执行的通知，同时放弃锁，使自己处于等待状态
```java
public class WaitAndNotify {
    private static Integer count = 0;
    private static final Integer FULL = 10;
    private static Object LOCK = new Object();
    public static void main(String[] args) {
        WaitAndNotify w = new WaitAndNotify();
        new Thread(w.new Producer()).start();
        new Thread(w.new Producer()).start();
        new Thread(w.new Producer()).start();
        new Thread(w.new Producer()).start();
        new Thread(w.new Producer()).start();
        new Thread(w.new Consumer()).start();
        new Thread(w.new Consumer()).start();
        new Thread(w.new Consumer()).start();
        new Thread(w.new Consumer()).start();
        new Thread(w.new Consumer()).start();
    }
    class Producer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (LOCK) {
                    while (count == FULL) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    count++;
                    System.out.println(Thread.currentThread().getName() + " ~~~~~生产者生产， 目前总共有:" + count);
                    LOCK.notifyAll();
                }
            }
        }
    }
    class Consumer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (LOCK) {
                    while (count == 0) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    count--;
                    System.out.println(Thread.currentThread().getName() + " ~~~~~消费者消费， 目前总共有:" + count);
                    LOCK.notifyAll();
                }
            }
        }
    }
}

```

### 2.2、ReentrantLock实现

使用到 Condition的 await和signal()/singnalAll() 来实现线程的通信

```java
public class LockExample {
    private static Integer count = 0;
    private static final Integer FULL = 10;
    private Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    public static void main(String[] args) {
        LockExample w = new LockExample();
        new Thread(w.new Producer()).start();
        new Thread(w.new Producer()).start();
        new Thread(w.new Producer()).start();
        new Thread(w.new Producer()).start();
        new Thread(w.new Producer()).start();
        new Thread(w.new Consumer()).start();
        new Thread(w.new Consumer()).start();
        new Thread(w.new Consumer()).start();
        new Thread(w.new Consumer()).start();
        new Thread(w.new Consumer()).start();
    }
    class Producer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.lock();
                try {
                    while (count == FULL){
                        try {
                            notFull.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    count++;
                    System.out.println(Thread.currentThread().getName() + " ~~~~~生产者生产， 目前总共有:" + count);
                    notEmpty.signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
    class Consumer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.lock();
                try {
                    while (count == 0){
                        try {
                            notEmpty.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    count--;
                    System.out.println(Thread.currentThread().getName() + " ~~~~~消费者消费， 目前总共有:" + count);
                    notFull.signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}

```
### 2.3、阻塞队列BlockingQueue的实现

被阻塞的情况主要有如下两种:
- 当队列满了的时候进行入队列操作
- 当队列空了的时候进行出队列操作

使用take()和put()方法，这里生产者和生产者，消费者和消费者之间不存在同步，所以会出现连续生成和连续消费的现象。
```java
public class BlockingQueueExample {
    private static Integer count = 0;
    final BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(10);
    public static void main(String[] args) {
        BlockingQueueExample w = new BlockingQueueExample();
        new Thread(w.new Producer()).start();
        new Thread(w.new Producer()).start();
        new Thread(w.new Producer()).start();
        new Thread(w.new Producer()).start();
        new Thread(w.new Producer()).start();
        new Thread(w.new Consumer()).start();
        new Thread(w.new Consumer()).start();
        new Thread(w.new Consumer()).start();
        new Thread(w.new Consumer()).start();
        new Thread(w.new Consumer()).start();
    }
    class Producer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    blockingQueue.put(1);
                    count++;
                    System.out.println(Thread.currentThread().getName() + " ~~~~~生产者生产， 目前总共有:" + count);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
    class Consumer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    blockingQueue.take();
                    count--;
                    System.out.println(Thread.currentThread().getName() + " ^^^^^^消费者消费， 目前总共有:" + count);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
```
### 2.4、信号量Semaphore的实现

Java中的Semaphore维护了一个许可集，一开始先设定这个许可集的数量，可以使用acquire()方法获得一个许可，当许可不足时会被阻塞，release()添加一个许可。加入了另外一个mutex信号量，维护生产者消费者之间的同步关系，保证生产者和消费者之间的交替进行
```java
public class SemaphoreExample {
    private static Integer count = 0;
    final Semaphore notFull = new Semaphore(10);
    final Semaphore notEmpty = new Semaphore(0);
    final Semaphore mutex = new Semaphore(1);
    public static void main(String[] args) {
        SemaphoreExample w = new SemaphoreExample();
        new Thread(w.new Producer()).start();
        new Thread(w.new Producer()).start();
        new Thread(w.new Producer()).start();
        new Thread(w.new Producer()).start();
        new Thread(w.new Producer()).start();
        new Thread(w.new Consumer()).start();
        new Thread(w.new Consumer()).start();
        new Thread(w.new Consumer()).start();
        new Thread(w.new Consumer()).start();
        new Thread(w.new Consumer()).start();
    }
    class Producer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    notFull.acquire();
                    mutex.acquire();
                    count++;
                    System.out.println(Thread.currentThread().getName() + " ~~~~~生产者生产， 目前总共有:" + count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.release();
                    notEmpty.release();
                }
            }
        }
    }
    class Consumer implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    notEmpty.acquire(); 
                    mutex.acquire();
                    count--;
                    System.out.println(Thread.currentThread().getName() + " ^^^^^^消费者消费， 目前总共有:" + count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.release();
                    notFull.release();
                }
            }
        }
    }
}
```
### 2.5、管道输入输出流实现

PipedOutputStream和PipedInputStream分别是管道输出流和管道输入流.它们的作用是让多线程可以通过管道进行线程间的通讯.在使用管道通信时，必须将PipedOutputStream和PipedInputStream配套使用。

使用方法：

先创建一个管道输入流和管道输出流，然后将输入流和输出流进行连接，用生产者线程往管道输出流中写入数据，消费者在管道输入流中读取数据，这样就可以实现了不同线程间的相互通讯。

但是这种方式在生产者和生产者、消费者和消费者之间不能保证同步，也就是说在一个生产者和一个消费者的情况下是可以生产者和消费者之间交替运行的，多个生成者和多个消费者者之间则不行

```java
public class PipedExample {
    final PipedInputStream pis = new PipedInputStream();
    final PipedOutputStream pos = new PipedOutputStream();
    {
        try {
            pis.connect(pos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        PipedExample p = new PipedExample();
        new Thread(p.new Producer()).start();
        new Thread(p.new Consumer()).start();
    }
    class Producer implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(1000);
                    int num = (int) (Math.random() * 255);
                    System.out.println(Thread.currentThread().getName() + "生产者生产了一个数字，该数字为： " + num);
                    pos.write(num);
                    pos.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    pos.close();
                    pis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class Consumer implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(1000);
                    int num = pis.read();
                    System.out.println("消费者消费了一个数字，该数字为：" + num);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    pos.close();
                    pis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
```

### 2.6、Exchanger

```java
public class ExchangerDemo {
    public static void main(String[] args) {
        List<String> buffer1 = new ArrayList<String>();
        List<String> buffer2 = new ArrayList<String>();
        Exchanger<List<String>> exchanger = new Exchanger<List<String>>();
        Thread producerThread = new Thread(new Producer(buffer1,exchanger));
        Thread consumerThread = new Thread(new Consumer(buffer2,exchanger));
        producerThread.start();
        consumerThread.start();
    }
    static class Producer implements Runnable {
        // 生产者消费者交换的数据结构
        private List<String> buffer;
        // 生产者和消费者的交换对象
        private Exchanger<List<String>> exchanger;
        public Producer(List<String> buffer, Exchanger<List<String>> exchanger) {
            this.buffer = buffer;
            this.exchanger = exchanger;
        }
        @Override
        public void run() {
            for (int i = 1; i < 5; i++) {
                System.out.println("生产者第" + i + "次提供");
                for (int j = 1; j <= 3; j++) {
                    System.out.println("生产者装入" + i + "--" + j);
                    buffer.add("buffer：" + i + "--" + j);
                }
                System.out.println("生产者装满，等待与消费者交换...");
                try {
                    exchanger.exchange(buffer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    static class Consumer implements Runnable {
        private List<String> buffer;
        private final Exchanger<List<String>> exchanger;
        public Consumer(List<String> buffer, Exchanger<List<String>> exchanger) {
            this.buffer = buffer;
            this.exchanger = exchanger;
        }
        @Override
        public void run() {
            for (int i = 1; i < 5; i++) {
                //调用exchange()与消费者进行数据交换
                try {
                    buffer = exchanger.exchange(buffer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("消费者第" + i + "次提取");
                for (int j = 1; j <= 3; j++) {
                    System.out.println("消费者 : " + buffer.get(0));
                    buffer.remove(0);
                }
            }
        }
    }
}
```

### 2.7、LockSupport实现



# 三、Disruptor

- [高性能队列——Disruptor](https://tech.meituan.com/2016/11/18/disruptor.html)
- [Disruptor-使用指南](https://lmax-exchange.github.io/disruptor/user-guide/index.html)
* [The LMAX Architecture](https://martinfowler.com/articles/lmax.html)
* [LMAX架构](https://www.jianshu.com/p/5e0c4481efb7)

## 1、LMAX架构

LMAX是一种新型零售金融交易平台，它能够以很低的延迟(latency)产生大量交易(吞吐量)。

这个系统是建立在JVM平台上，核心是一个业务逻辑处理器，它能够在一个线程里每秒处理6百万订单。业务逻辑处理器完全是运行在内存中(in-memory)，使用事件源驱动方式(event sourcing). 业务逻辑处理器的核心是`Disruptors`，这是一个并发组件，能够在无锁的情况下实现网络的Queue并发操作；

## 2、Disruptor介绍

Disruptor 是一款高性能的有界内存队列，高性能原因：
- 内存分配更加合理，利用RingBuffer数据结构，数组元素在初始化时一次性全部创建；
- 提高缓存命中率；对象循环利用，避免频繁GC；
- 避免伪共享，提高缓存利用率：使用缓存行填充，每个变量占用一个缓存行，不共享缓存行
- 采用无锁算法，避免频繁加锁、解锁的性能消耗，比如Disruptor入队操作：如果没有足够的空余位置，就让出CPU使用权，然后重新计算，反之则使用CAS设置入队索引
- 支持批量消费，消费者可以无锁方式消费多个消息；

### 2.1、RingBuffer提高性能

RingBuffer本质上也是数组，其在结构上做了很多优化，其中一项是和内存分配相关的。首先看下局部性原理：所谓局部性原理是指在一段时间内程序的执行还有限定在一个局部范围内，局部性可以从两个方面来理解：
* 时间局部性：如果某个数据被访问，那么在不久的将来它很可能再次被访问；
* 空间局部性：如果某块内存被访问，不久之后这块内存附近的内存也很可能被访问；

首先是 ArrayBlockingQueue。生产者线程向 ArrayBlockingQueue 增加一个元素，每次增加元素 E 之前，都需要创建一个对象 E，如下图所示，ArrayBlockingQueue 内部有 6 个元素，这 6 个元素都是由生产者线程创建的，由于创建这些元素的时间基本上是离散的，所以这些元素的内存地址大概率也不是连续的。

Disruptor 内部的 RingBuffer 也是用数组实现的，但是这个数组中的所有元素在初始化时是一次性全部创建的，所以这些元素的内存地址大概率是连续的，相关的代码如下所示。
```java
for (int i=0; i<bufferSize; i++){
  //entries[]就是RingBuffer内部的数组
  //eventFactory就是前面示例代码中传入的LongEvent::new
  entries[BUFFER_PAD + i] 
    = eventFactory.newInstance();
}
```
**数组中所有元素内存地址连续能提升性能吗？**能！为什么呢？因为消费者线程在消费的时候，是遵循空间局部性原理的，消费完第 1 个元素，很快就会消费第 2 个元素；当消费第 1 个元素 E1 的时候，CPU 会把内存中 E1 后面的数据也加载进 Cache，如果 E1 和 E2 在内存中的地址是连续的，那么 E2 也就会被加载进 Cache 中，然后当消费第 2 个元素的时候，由于 E2 已经在 Cache 中了，所以就不需要从内存中加载了，这样就能大大提升性能。

除此之外，在 Disruptor 中，生产者线程通过 publishEvent() 发布 Event 的时候，并不是创建一个新的 Event，而是通过 event.set() 方法修改 Event， 也就是说 RingBuffer 创建的 Event 是可以循环利用的，这样还能避免频繁创建、删除 Event 导致的频繁 GC 问题。

## 3、Disruptor快速入门

- （1）构架一个Event
    ```java
    @Data
    public class OrderEvent {
        private Long value;
    }
    ```
- （2）构建一个EventFactory
    ```java
    public class OrderEventFactory implements EventFactory<OrderEvent> {
        @Override
        public OrderEvent newInstance() {
            return new OrderEvent();
        }
    }
    ```

- （3）构建一个EventHandler
    ```java
    public class OrderEventHandler implements EventHandler<OrderEvent> {
        @Override
        public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
            System.out.println("消费者：" + event.getValue());
        }
    }
    ```

- （4）构建一个Disruptor
    ```java
    /**
     * 1 eventFactory: 消息(event)工厂对象
     * 2 ringBufferSize: 容器的长度
     * 3 executor: 线程池(建议使用自定义线程池) RejectedExecutionHandler
     * 4 ProducerType: 单生产者 还是 多生产者
     * 5 waitStrategy: 等待策略
     */
    //1. 实例化disruptor对象
    Disruptor<OrderEvent> disruptor = new Disruptor<OrderEvent>(orderEventFactory,
            ringBufferSize,
            executor,
            ProducerType.SINGLE,
            new BlockingWaitStrategy());
    //2. 添加消费者的监听 (构建disruptor 与 消费者的一个关联关系)
    disruptor.handleEventsWith(new OrderEventHandler());
    //3. 启动disruptor
    disruptor.start();
    ```
    关于RingBuffer的使用
    ```java
    //1 在生产者发送消息的时候, 首先 需要从我们的ringBuffer里面 获取一个可用的序号
    long sequence = ringBuffer.next();	//0	
    try {
        //2 根据这个序号, 找到具体的 "OrderEvent" 元素 注意:此时获取的OrderEvent对象是一个没有被赋值的"空对象"
        OrderEvent event = ringBuffer.get(sequence);
        //3 进行实际的赋值处理
        event.setValue(data.getLong(0));			
    } finally {
        //4 提交发布操作
        ringBuffer.publish(sequence);			
    }
    ```


## 4、Disruptor核心组件

### 4.1、RingBuffer

基于数组的缓存实现，也是创建sequence与定义WaitStrategy的入口

### 4.2、Disruptor


### 4.3、Sequence

- 通过顺序递增的序号来编号，管理进行交互的数据；
- 对数据的处理过程总是沿着序号逐个递增处理；
- 一个sequence用于跟踪标识某个特定的事件处理者（RingBuffer、Producer、Consumer）的处理进度
- Sequence可以看成是一个AtomicLong用于标识进度；
- 还有另外一个目的是防止不同的Sequence之间CPU缓存伪共享的问题

### 4.4、Sequencer

- Disruptor真正的核心
- 此接口有两个实现类：MultiProducerSequencer、SingleProducerSequencer
- 主要实现生产者和消费者之间快速、正确的传递数据的并发算法

### 4.5、SequenceBarrier

- 用于保持对RingBuffer的Mian Published Sequence和Consumer之间的平衡关系；SequenceBarrier还定义了决定Cosnumer是否还有可处理的事件的逻辑；

### 4.6、WaitStrategy

- 决定一个消费者将如何等待生产者将Event置入Disruptor

- BlockingWaitStrategy：是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现；
- SleepingWaitStrategy：其性能跟BlockingWaitStrategy差不多，对CPU的消耗类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景；
- YieldingWaitStrategy：其性能最好，适合用于低延迟的系统。在要求极高性能且事件处理数小于CPU逻辑核心数的场景中，推荐使用此策略；比如CPU开启超线程的特性；

### 4.7、Event

- 从生产者到消费者过程中所处理的数据单元

### 4.8、EventProcessor

- 主要事件循环，处理Disruptor的Event，拥有消费者的Sequence
- 它有个实现类是BatchEventProcessor，包含了event loop有效的实现，并且将回调到一个EventHandler接口的实现对象；

### 4.9、EventHandler

由用户实现并且代表了Disruptor中的一个消费者的接口，也就是消费者的处理逻辑都需要写在这里；

### 4.10、WorkProcessor

确保每个sequence只能被一个processor消费，在同一个workpool中处理多个WorkProcessor不会被消费统一的sequence

### 4.11、Disruptor编程模型

![](image/Disruptor图解.png)

## 5、Disruptor高级应用

### 5.1、核心链路应用场景

如京东中下单：用户添加到购物车 -> 下单 —> 支付 —> 配送 -> 收货

核心链路的代码实现，业务逻辑非常复杂；核心链路特点：至关重要且业务复杂

主要实现方式：
- 传统的完全解耦模式
- 模板模式

上述方式不适合，解决手段：
- 领域模型的高度抽象
- 寻找更好的框架帮助进行编码：
    - 有限状态机框架，比如：spring-statemachine
    - 使用Disruptor

### 5.2、并行计算

- 串行操作：使用链式调用的方式：`disruptor.handleEventsWith(new Handler1()).handleEventsWith(new Handler2()).handleEventsWith(new Handler3());`
- 并行操作：使用单独调用的方式：
    - 单独调用：
        ```java
        disruptor.handleEventsWith(new Handler1());
        disruptor.handleEventsWith(new Handler2());
        disruptor.handleEventsWith(new Handler3());
        ```
    - 传入多个元素：`disruptor.handleEventsWith(new Handler1(), new Handler2(), new Handler3());`

### 5.3、多边形高端操作

Disruptor可以实现串并行同时编码
- 菱形操作
    ```java
    disruptor.handleEventsWith(new Handler1(), new Handler2())
            .handleEventsWith(new Handler3());
            
    // 或者 通过handlerEventsWith方法的返回参数：EventHandlerGroup 来处理
    EventHandlerGroup<Trade> ehGroup = disruptor.handleEventsWith(new Handler1(), new Handler2());
    ehGroup.then(new Handler3());
    ```
- 六边形操作
    ```java
    // 执行顺序：h1和h4是并行操作，h2在h1执行完成后执行；h5在h4执行完成后执行；在h2和h5都执行成功之后，在执行h3
    Handler1 h1 = new Handler1();
    Handler2 h2 = new Handler2();
    Handler3 h3 = new Handler3();
    Handler4 h4 = new Handler4();
    Handler5 h5 = new Handler5();
    disruptor.handleEventsWith(h1, h4);
    disruptor.after(h1).handleEventsWith(h2);
    disruptor.after(h4).handleEventsWith(h5);
    disruptor.after(h2, h5).handleEventsWith(h3);
    ```
    **注意：单消费者模式下，上述代码执行跟构造Disruptor时传入的线程池有关，因为上述代码有5个EventHandler，所以，线程池中线程数量必须至少有5个；这是因为单消费者下是由BatchEventProcessor来操作的，而BatchEventProcessor就代表一个线程，其实现了Runnable接口；对于多消费者是不存在该问题的；**

### 5.4、多生产者与多消费者

使用WorkPool来处理

## 6、源码分析

### 6.1、Disruptor底层性能特点

- 数据结构方面：是否环形结构、数组、内存预加载；
- 使用单线程写方式、内存屏障
- 消除伪共享（填充缓存行）
- 序号栅栏和序号配合使用来消除锁和CAS；

### 6.2、数据结构与内存预加载机制

- RingBuffer使用数组Object[] entries作为存储元素

### 6.3、使用单线程写

Disruptor的RingBuffer，做到完全无锁是因为单线程写；注入Redis、Netty等高性能技术框架的设计都是这个核心思想

其是基于系统内核

### 6.4、内存优化：内存屏障

要正确的实现无锁，还需另外一个关键技术：内存屏障

### 6.5、系统缓存优化：消除伪共享

- [伪共享问题](Java并发与多线程.md#15伪共享问题)

### 6.6、算法优化：序号栅栏机制

- Disruptor3.0中，序号栅栏SequenceBarrier和序号Sequence搭配使用，协调和管理消费者与生产者的工作节奏，避免了锁和CAS的使用；
- Disruptor3.0中，每个消费者和生产者都持有自己的序号，这些序号的变化必须满足如下基本条件：
    - 消费者序号数值必须小于生产者序号数值；
    - 消费者序号必须小于其前置（依赖关系）消费者的序号数值；
    - 生产者序号数值不能大于消费者中最小的序号数值，以避免生产者速度过快，将还未来得及消费的消息覆盖；

### 6.7、WaitStrategy等待策略

对于YieldingWaitStrategy实现类，尝试去修改源代码，降低高性能对CPU和内存资源的损耗

### 6.8、EventProcessor核心机制