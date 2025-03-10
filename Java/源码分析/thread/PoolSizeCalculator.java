/**
 * A class that calculates the optimal thread pool boundaries. It takes the desired target utilization and the desired
 * work queue memory consumption as input and retuns thread count and work queue capacity.
 */
public abstract class PoolSizeCalculator {
    /**
     * The sample queue size to calculate the size of a single {@link Runnable} element.
     */
    private final int SAMPLE_QUEUE_SIZE = 1000;
    /**
     * Accuracy of test run. It must finish within 20ms of the testTime otherwise we retry the test. This could be
     * configurable.
     */
    private final int EPSYLON = 20;
    /**
     * Control variable for the CPU time investigation.
     */
    private volatile boolean expired;
    /**
     * Time (millis) of the test run in the CPU time calculation.
     */
    private final long testtime = 3000;
    /**
     * Calculates the boundaries of a thread pool for a given {@link Runnable}.
     *
     * @param targetUtilization    CPU目标利用率 (0 <= targetUtilization <= 1)
     * @param targetQueueSizeBytes BlockingQueue占用内存带下，byte
     */
    protected void calculateBoundaries(BigDecimal targetUtilization, BigDecimal targetQueueSizeBytes) {
        calculateOptimalCapacity(targetQueueSizeBytes);
        Runnable task = creatTask();
        start(task);
        start(task); // warm up phase
        long cputime = getCurrentThreadCPUTime();
        start(task); // test intervall
        cputime = getCurrentThreadCPUTime() - cputime;
        long waittime = (testtime * 1000000) - cputime;
        calculateOptimalThreadCount(cputime, waittime, targetUtilization);
    }
    private void calculateOptimalCapacity(BigDecimal targetQueueSizeBytes) {
        long mem = calculateMemoryUsage();
        BigDecimal queueCapacity = targetQueueSizeBytes.divide(new BigDecimal(mem), RoundingMode.HALF_UP);
        System.out.println("Target queue memory usage (bytes): " + targetQueueSizeBytes);
        System.out.println("createTask() produced " + creatTask().getClass().getName() + " which took " + mem
                + " bytes in a queue");
        System.out.println("Formula: " + targetQueueSizeBytes + " / " + mem);
        System.out.println("* Recommended queue capacity (bytes): " + queueCapacity);
    }
    /**
     * Brian Goetz' optimal thread count formula, see 'Java Concurrency in Practice' (chapter 8.2)
     *
     * @param cpu               cpu time consumed by considered task
     * @param wait              wait time of considered task
     * @param targetUtilization target utilization of the system
     */
    private void calculateOptimalThreadCount(long cpu, long wait, BigDecimal targetUtilization) {
        BigDecimal waitTime = new BigDecimal(wait);
        BigDecimal computeTime = new BigDecimal(cpu);
        BigDecimal numberOfCPU = new BigDecimal(Runtime.getRuntime().availableProcessors());
        BigDecimal optimalthreadcount = numberOfCPU.multiply(targetUtilization).multiply( new BigDecimal(1).add(waitTime.divide(computeTime, RoundingMode.HALF_UP)));
        System.out.println("Number of CPU: " + numberOfCPU);
        System.out.println("Target utilization: " + targetUtilization);
        System.out.println("Elapsed time (nanos): " + (testtime * 1000000));
        System.out.println("Compute time (nanos): " + cpu);
        System.out.println("Wait time (nanos): " + wait);
        System.out.println("Formula: " + numberOfCPU + " * " + targetUtilization + " * (1 + " + waitTime + " / " + computeTime + ")");
        System.out.println("* Optimal thread count: " + optimalthreadcount);
    }
    /**
     * Runs the {@link Runnable} over a period defined in {@link #testtime}. Based on Heinz Kabbutz' ideas
     * (http://www.javaspecialists.eu/archive/Issue124.html).
     *
     * @param task the runnable under investigation
     */
    public void start(Runnable task) {
        long start = 0;
        int runs = 0;
        do {
            if (++runs > 5) {
                throw new IllegalStateException("Test not accurate");
            }
            expired = false;
            start = System.currentTimeMillis();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    expired = true;
                }
            }, testtime);
            while (!expired) {
                task.run();
            }
            start = System.currentTimeMillis() - start;
            timer.cancel();
        } while (Math.abs(start - testtime) > EPSYLON);
        collectGarbage(3);
    }
    private void collectGarbage(int times) {
        for (int i = 0; i < times; i++) {
            System.gc();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    /**
     * Calculates the memory usage of a single element in a work queue. Based on Heinz Kabbutz' ideas
     * (http://www.javaspecialists.eu/archive/Issue029.html).
     * @return memory usage of a single {@link Runnable} element in the thread pools work queue
     */
    public long calculateMemoryUsage() {
        BlockingQueue<Runnable> queue = createWorkQueue();
        for (int i = 0; i < SAMPLE_QUEUE_SIZE; i++) {
            queue.add(creatTask());
        }
        long mem0 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long mem1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        queue = null;
        collectGarbage(15);
        mem0 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        queue = createWorkQueue();
        for (int i = 0; i < SAMPLE_QUEUE_SIZE; i++) {
            queue.add(creatTask());
        }
        collectGarbage(15);
        mem1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return (mem1 - mem0) / SAMPLE_QUEUE_SIZE;
    }
    /**
     * Create your runnable task here.
     * @return an instance of your runnable task under investigation
     */
    protected abstract Runnable creatTask();
    /**
     * Return an instance of the queue used in the thread pool.
     * @return queue instance
     */
    protected abstract BlockingQueue<Runnable> createWorkQueue();
    /**
     * Calculate current cpu time. Various frameworks may be used here, depending on the operating system in use. (e.g.
     * http://www.hyperic.com/products/sigar). The more accurate the CPU time measurement, the more accurate the results
     * for thread count boundaries.
     * @return 当前线程占用的总时间
     */
    protected abstract long getCurrentThreadCPUTime();

}
// 实现Demo
public class MyPoolSizeCalculator extends PoolSizeCalculator {
    public static void main(String[] args) {
        MyPoolSizeCalculator calculator = new MyPoolSizeCalculator();
        calculator.calculateBoundaries(
            new BigDecimal(1.0),
            new BigDecimal(100000));
    }
    @Override
    protected long getCurrentThreadCPUTime() {
        return ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
    }
    @Override
    protected Runnable creatTask() {
        return new AsynchronousTask();
    }
    @Override
    protected BlockingQueue<Runnable> createWorkQueue() {
        return new LinkedBlockingQueue<>();
    }
    static class AsynchronousTask implements Runnable {
        @Override
        public void run() {
        }
    }
}