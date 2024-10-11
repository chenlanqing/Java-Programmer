public class ResolveEarlyNotify {
    private static String lockObject = "";
    private static boolean isWait = true;

    public static void main(String[] args) {
        WaitThread w = new WaitThread(lockObject);
        NotifyThread n = new NotifyThread(lockObject);
        n.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        w.start();
    }

    static class WaitThread extends Thread {
        private String lock;

        public WaitThread(String lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock) {
                try {
                    while (isWait) {
                        System.out.println(Thread.currentThread().getName() + " 进去代码块");
                        System.out.println(Thread.currentThread().getName() + " 开始wait");
                        lock.wait();
                        System.out.println(Thread.currentThread().getName() + " 结束wait");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class NotifyThread extends Thread {
        private String lock;

        public NotifyThread(String lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock) {
                try {
                    System.out.println(Thread.currentThread().getName() + " 进去代码块");
                    System.out.println(Thread.currentThread().getName() + " 开始notify");
                    lock.notifyAll();
                    isWait = false;
                    System.out.println(Thread.currentThread().getName() + " 结束notify");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
