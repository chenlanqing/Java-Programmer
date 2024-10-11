/**
 * 通知丢失问题解决方案：可以设置变量表示是否被通知过
 */
public class ResolveLostNotify {
    public static void main(String[] args) {
        Calculator c = new Calculator();
        // 下列写法是正常的
//        new ReadResult(c).start();
//        c.start();

        // 将上述两个线程启动的顺序换一下，Calculator有个是否通知的变量
        c.start();
        new ReadResult(c).start();
    }

    static class Calculator extends Thread {
        int total;
        boolean isSignalled = false;

        @Override
        public void run() {
            synchronized (this) {
                isSignalled = true;
                for (int i = 0; i < 101; i++) {
                    total += i;
                }
                this.notify();
            }
        }
    }

    static class ReadResult extends Thread {

        Calculator c;

        public ReadResult(Calculator c) {
            this.c = c;
        }

        @Override
        public void run() {
            synchronized (c) {
                if (!c.isSignalled) {
                    try {
                        System.out.println(Thread.currentThread() + "等待计算结果...");
                        c.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread() + "计算结果为：" + c.total);
                }
            }
        }
    }
}
