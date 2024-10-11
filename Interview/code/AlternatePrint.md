# 交替打印1~100

## 1、`wait`&`notify`

```java
public class AlternatePrint {
    private static final Object lock = new Object();
    private static int count = 1;
    private static final int MAX_COUNT = 100;

    public static void main(String[] args) {
        // 创建两个线程，分别打印奇数和偶数
        Thread oddThread = new Thread(() -> {
            while (count <= MAX_COUNT) {
                synchronized (lock) {
                    if (count % 2 != 0) {
                        System.out.println(Thread.currentThread().getName() + ": " + count);
                        count++;
                        lock.notify();  // 唤醒另一个线程
                    } else {
                        try {
                            lock.wait();  // 当前线程等待
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
        }, "OddThread");

        Thread evenThread = new Thread(() -> {
            while (count <= MAX_COUNT) {
                synchronized (lock) {
                    if (count % 2 == 0) {
                        System.out.println(Thread.currentThread().getName() + ": " + count);
                        count++;
                        lock.notify();  // 唤醒另一个线程
                    } else {
                        try {
                            lock.wait();  // 当前线程等待
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
        }, "EvenThread");

        // 启动线程
        oddThread.start();
        evenThread.start();
    }
}
```

是的，除了使用 `wait()` 和 `notify()` 进行线程间的同步外，还可以使用其他方式来实现交替打印 1 到 100，比如：

## 2、**使用 `ReentrantLock` 和 `Condition`**

`ReentrantLock` 提供了一种更灵活的锁机制，可以创建多个 `Condition` 对象，类似于 `wait()` 和 `notify()` 的机制。
```java
public class AlternatePrintWithLock {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition oddTurn = lock.newCondition();
    private static final Condition evenTurn = lock.newCondition();
    private static int count = 1;
    private static final int MAX_COUNT = 100;

    public static void main(String[] args) {
        Thread oddThread = new Thread(() -> {
            while (count <= MAX_COUNT) {
                lock.lock();
                try {
                    if (count % 2 == 0) {
                        oddTurn.await();  // 偶数时等待
                    }
                    System.out.println(Thread.currentThread().getName() + ": " + count);
                    count++;
                    evenTurn.signal();  // 唤醒偶数线程
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }
        }, "OddThread");

        Thread evenThread = new Thread(() -> {
            while (count <= MAX_COUNT) {
                lock.lock();
                try {
                    if (count % 2 != 0) {
                        evenTurn.await();  // 奇数时等待
                    }
                    System.out.println(Thread.currentThread().getName() + ": " + count);
                    count++;
                    oddTurn.signal();  // 唤醒奇数线程
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }
        }, "EvenThread");

        oddThread.start();
        evenThread.start();
    }
}
```

## 3、**使用 `Semaphore`**

`Semaphore` 允许线程通过控制许可证的数量来同步进程。可以用来限制某一时刻只有一个线程打印。

```java
import java.util.concurrent.Semaphore;

public class AlternatePrintWithSemaphore {
    private static final Semaphore oddSemaphore = new Semaphore(1);  // 奇数线程信号量
    private static final Semaphore evenSemaphore = new Semaphore(0); // 偶数线程信号量
    private static int count = 1;
    private static final int MAX_COUNT = 100;

    public static void main(String[] args) {
        Thread oddThread = new Thread(() -> {
            while (count <= MAX_COUNT) {
                try {
                    oddSemaphore.acquire();  // 获取奇数线程的信号
                    if (count <= MAX_COUNT) {
                        System.out.println(Thread.currentThread().getName() + ": " + count);
                        count++;
                    }
                    evenSemaphore.release();  // 释放偶数线程的信号
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "OddThread");

        Thread evenThread = new Thread(() -> {
            while (count <= MAX_COUNT) {
                try {
                    evenSemaphore.acquire();  // 获取偶数线程的信号
                    if (count <= MAX_COUNT) {
                        System.out.println(Thread.currentThread().getName() + ": " + count);
                        count++;
                    }
                    oddSemaphore.release();  // 释放奇数线程的信号
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "EvenThread");

        oddThread.start();
        evenThread.start();
    }
}
```

## 4、**使用 `AtomicInteger` 和 `CAS`（Compare-And-Swap）**

`AtomicInteger` 提供了一种无锁的原子性操作方式，可以通过自旋锁的方式来实现交替打印。使用一个 volatile 修饰的布尔变量 oddTurn 来表示当前轮次是奇数线程（true）还是偶数线程（false）
```java
public class AlternatePrintWithAtomic {
    private static final AtomicInteger counter = new AtomicInteger(1);
    private static final int MAX_COUNT = 100;
    private static volatile boolean oddTurn = true; // 标识是否是奇数线程的轮次

    public static void main(String[] args) {
        // 奇数线程
        Thread oddThread = new Thread(() -> {
            while (counter.get() <= MAX_COUNT) {
                if (oddTurn && counter.get() % 2 != 0) { // 判断是否是奇数轮次和当前值
                    System.out.println(Thread.currentThread().getName() + ": " + counter.getAndIncrement());
                    oddTurn = false; // 切换到偶数轮次
                }
            }
        }, "OddThread");

        // 偶数线程
        Thread evenThread = new Thread(() -> {
            while (counter.get() <= MAX_COUNT) {
                if (!oddTurn && counter.get() % 2 == 0) { // 判断是否是偶数轮次和当前值
                    System.out.println(Thread.currentThread().getName() + ": " + counter.getAndIncrement());
                    oddTurn = true; // 切换到奇数轮次
                }
            }
        }, "EvenThread");

        oddThread.start();
        evenThread.start();
    }
}
```

## 5、**使用 `BlockingQueue`**

`BlockingQueue` 也可以用来在线程之间进行通信，交替处理消息。
```java
public class AlternatePrintWithQueue {
    private static final BlockingQueue<Integer> oddQueue = new ArrayBlockingQueue<>(1);
    private static final BlockingQueue<Integer> evenQueue = new ArrayBlockingQueue<>(1);

    public static void main(String[] args) {
        Thread oddThread = new Thread(() -> {
            for (int i = 1; i <= 100; i += 2) {
                try {
                    oddQueue.put(i);  // 放入奇数
                    System.out.println(Thread.currentThread().getName() + ": " + i);
                    evenQueue.take();  // 等待偶数线程处理
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "OddThread");

        Thread evenThread = new Thread(() -> {
            for (int i = 2; i <= 100; i += 2) {
                try {
                    oddQueue.take();  // 等待奇数线程处理
                    System.out.println(Thread.currentThread().getName() + ": " + i);
                    evenQueue.put(i);  // 放入偶数
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "EvenThread");

        oddThread.start();
        evenThread.start();
    }
}
```

## 6、总结

不同的方案适用于不同的场景：
- `wait/notify` 和 `ReentrantLock` 更适合传统同步。
- `Semaphore` 是一个简单易用的同步控制工具。
- `AtomicInteger` 是无锁的实现方式，在某些高并发环境下更高效。
- `BlockingQueue` 适合用于消息传递。
