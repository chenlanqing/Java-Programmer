多线程编程题

## 1、实现一个容器，提供两个方法，add，size 写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束

### 1.1、使用wait和notify实现

```java
private final List<Integer> list = new ArrayList<>();
public void add(int i) {
    list.add(i);
}
public int getSize() {
    return list.size();
}
public static void main(String[] args) {
    TwoThreadDemo d = new TwoThreadDemo();
    Object lock = new Object();

    new Thread(() -> {
        synchronized (lock) {
            System.out.println("t2 启动");
            if (d.getSize() != 5) {
                try {
                    /**会释放锁*/
                    lock.wait();
                    System.out.println("t2 结束");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lock.notify();
        }
    }, "t2").start();

    new Thread(() -> {
        synchronized (lock) {
            System.out.println("t1 启动");
            for (int i = 0; i < 9; i++) {
                d.add(i);
                System.out.println("add" + i);
                if (d.getSize() == 5) {
                    lock.notify();
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }, "t1").start();
}
```

### 1.2、使用countDownLatch

```java
private final List<Integer> list = new ArrayList<>();
public void add(int i) {
    list.add(i);
}
public int getSize() {
    return list.size();
}
public static void main(String[] args) {
    TwoThreadDemoCountdown d = new TwoThreadDemoCountdown();
    CountDownLatch countDownLatch = new CountDownLatch(1);

    new Thread(() -> {
        System.out.println("t2启动");
        if (d.getSize() != 5) {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("t2结束");
    }, "t2").start();

    new Thread(() -> {
        System.out.println("t1启动");
        for (int i = 0; i < 10; i++) {
            d.add(i);
            System.out.println("add_" + i);
            if (d.getSize() == 5) {
                countDownLatch.countDown();
            }
        }
        System.out.println("t1结束");
    }, "t1").start();
}
```