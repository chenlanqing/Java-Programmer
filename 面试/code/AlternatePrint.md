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

多个线程
```java
public class NotifyOrderPrint {

    private static final Object lock = new Object();
    private static volatile int count = 0;
    private static final int MAX = 100;

    public static void main(String[] args) {
        Thread t1 = new Thread(new NotifyRunnable(0), "Thread-1");
        Thread t2 = new Thread(new NotifyRunnable(1), "Thread-2");
        Thread t3 = new Thread(new NotifyRunnable(2), "Thread-3");
        t1.start();
        t2.start();
        t3.start();
    }

    public static class NotifyRunnable implements Runnable {
        private final int index;

        public NotifyRunnable(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            while (count < MAX) {
                synchronized (lock) {
                    while (count % 3 != index) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (count <= MAX) {
                        System.out.println(count);
                    }
                    count++;
                    lock.notifyAll();
                }
            }
        }
    }
}
```
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

多个线程进行打印：

```java
public class ConditionOrderPrint {

    private static final int MAX = 100;
    private static int count = 0;
    private static final ReentrantLock LOCK = new ReentrantLock();

    public static void main(String[] args) {
        Condition[] conditions = new Condition[3];
        for (int i = 0; i < 3; i++) {
            conditions[i] = LOCK.newCondition();
        }
        ConditionRunnable c1 = new ConditionRunnable(conditions, 0);
        ConditionRunnable c2 = new ConditionRunnable(conditions, 1);
        ConditionRunnable c3 = new ConditionRunnable(conditions, 2);
        Thread t1 = new Thread(c1, "Thread-1");
        Thread t2 = new Thread(c2, "Thread-2");
        Thread t3 = new Thread(c3, "Thread-3");
        t1.start();
        t2.start();
        t3.start();
    }

    private static class ConditionRunnable implements Runnable {
        private final Condition[] conditions;
        private final int index;

        private ConditionRunnable(Condition[] conditions, int index) {
            this.conditions = conditions;
            this.index = index;
        }

        private void signalAll() {
            for (Condition condition : conditions) {
                condition.signalAll();
            }
        }

        @Override
        public void run() {
            while (count < MAX) {
                LOCK.lock();
                try {
                    while (count < MAX &&count % 3 != index) {
                        conditions[index].await();
                    }
                    if (count > MAX) {
                        signalAll();
                        return;
                    }
                    System.out.println(count);
                    count++;
                    int nextIndex = (index + 1) % conditions.length;
                    conditions[nextIndex].signalAll();
                } catch (InterruptedException e) {
                    signalAll();
                } finally {
                    LOCK.unlock();
                }
            }
        }
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
3 个线程打印
```java
public class SemaphoreOrderPrint {

    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(1);
        SemaphoreRunnable semaphoreRunnable = new SemaphoreRunnable(semaphore);
        Thread t1 = new Thread(semaphoreRunnable, "Thread-1");
        Thread t2 = new Thread(semaphoreRunnable, "Thread-2");
        Thread t3 = new Thread(semaphoreRunnable, "Thread-3");
        semaphore.acquire();
        t1.start();
        semaphore.acquire();
        t2.start();
        semaphore.acquire();
        t3.start();
    }

    public static class SemaphoreRunnable implements Runnable {
        private final Semaphore semaphore;

        public SemaphoreRunnable(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                semaphore.release();
            }
        }
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

## 6、使用CountdownLatch

```java
public class CountdownLatchOrderPrint {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch c1 = new CountDownLatch(1);
        CountDownLatch c2 = new CountDownLatch(1);
        CountDownLatch c3 = new CountDownLatch(1);

        Thread t1 = new Thread(new CountdownLatchRunnable(c1), "Thread-1");
        Thread t2 = new Thread(new CountdownLatchRunnable(c2), "Thread-2");
        Thread t3 = new Thread(new CountdownLatchRunnable(c3), "Thread-3");

        t1.start();
        c1.await();
        t2.start();
        c2.await();
        t3.start();
        c3.await();
    }

    public static class CountdownLatchRunnable implements Runnable {

        private final CountDownLatch latchDownLatch;

        public CountdownLatchRunnable(CountDownLatch latchDownLatch) {
            this.latchDownLatch = latchDownLatch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName());
            latchDownLatch.countDown();
        }
    }
}
```

## 7、使用CycleBarrier

```java
public class CycleBarrierOrderPrint {

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        CyclicBarrier cycleBarrier = new CyclicBarrier(2);
        Thread t1 = new Thread(new CycleBarrierRunnable(cycleBarrier), "Thread-1");
        Thread t2 = new Thread(new CycleBarrierRunnable(cycleBarrier), "Thread-2");
        Thread t3 = new Thread(new CycleBarrierRunnable(cycleBarrier), "Thread-3");
        t1.start();
        cycleBarrier.await();
        t2.start();
        cycleBarrier.await();
        t3.start();
        cycleBarrier.await();
    }

    public static class CycleBarrierRunnable implements Runnable {

        private final CyclicBarrier cycleBarrier;

        public CycleBarrierRunnable(CyclicBarrier cycleBarrier) {
            this.cycleBarrier = cycleBarrier;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    cycleBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
```

## 总结

不同的方案适用于不同的场景：
- `wait/notify` 和 `ReentrantLock` 更适合传统同步。
- `Semaphore` 是一个简单易用的同步控制工具。
- `AtomicInteger` 是无锁的实现方式，在某些高并发环境下更高效。
- `BlockingQueue` 适合用于消息传递。
