/**
 * notify通知丢失问题：
 * 在获取通知前，通知提前到达，我们先计算结果，计算完后再通知，但是这个时候获取结果没有在等待通知，等到获取结果的线程想获取结果时，
 * 这个通知已经通知过了，所以就发生丢失
 * <p></p>
 * 通知丢失问题解决方案：可以设置变量表示是否被通知过
 */
public class LostNotify {

    public static void main(String[] args) {
        Calculator c = new Calculator();
        // 下列写法是正常的
        new ReadResult(c).start();
        c.start();

        // 将上述两个线程启动的顺序换一下，获取结果的线程一直在等待
//        c.start();
//        new ReadResult(c).start();
    }

    static class Calculator extends Thread {
        int total;
        @Override
        public void run() {
            synchronized (this) {
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
