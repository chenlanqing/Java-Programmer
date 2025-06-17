import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.*;
import java.util.stream.IntStream;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

public class TaskProcessorSolution {
    
    /**
     * 1. 线程池参数配置
     * 基于IO密集型任务的特点进行优化配置
     */
    private static final int CORE_POOL_SIZE = 20;        // 核心线程数：IO密集型可以是CPU核心数的5倍
    private static final int MAX_POOL_SIZE = 50;         // 最大线程数：考虑外部接口承受能力
    private static final long KEEP_ALIVE_TIME = 60L;     // 线程空闲存活时间
    private static final int QUEUE_CAPACITY = 2000;      // 队列容量：避免内存溢出
    
    // 统计相关
    private static final int BATCH_SIZE = 100;           // 每批任务数量
    private static final int TOTAL_TASKS = 10000;        // 总任务数
    private static final int MAX_RETRY_COUNT = 3;        // 最大重试次数
    
    // 全局统计计数器
    private final AtomicInteger totalSuccess = new AtomicInteger(0);
    private final AtomicInteger totalFailure = new AtomicInteger(0);
    private final AtomicInteger completedTasks = new AtomicInteger(0);
    
    // 批次统计 - 线程安全的Map
    private final ConcurrentHashMap<Integer, BatchStatistics> batchStats = new ConcurrentHashMap<>();
    
    // 失败任务队列
    private final BlockingQueue<FailedTask> failedTaskQueue = new LinkedBlockingQueue<>();
    
    // 线程池
    private final ThreadPoolExecutor executor;
    
    public TaskProcessorSolution() {
        // 创建自定义线程池
        this.executor = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(QUEUE_CAPACITY),
            new CustomThreadFactory("TaskProcessor"),
            new ThreadPoolExecutor.CallerRunsPolicy() // 拒绝策略：调用者执行
        );
        
        // 初始化批次统计
        int totalBatches = (TOTAL_TASKS + BATCH_SIZE - 1) / BATCH_SIZE;
        for (int i = 0; i < totalBatches; i++) {
            batchStats.put(i, new BatchStatistics(i));
        }
    }
    
    /**
     * 主要处理方法
     */
    public void processAllTasks() {
        System.out.println("开始处理任务，总数: " + TOTAL_TASKS);
        long startTime = System.currentTimeMillis();
        
        // 创建CompletableFuture数组来跟踪所有任务
        CompletableFuture<Void>[] futures = new CompletableFuture[TOTAL_TASKS];
        
        // 提交所有任务
        for (int i = 0; i < TOTAL_TASKS; i++) {
            final int taskId = i;
            final int batchId = i / BATCH_SIZE;
            
            futures[i] = CompletableFuture
                .supplyAsync(() -> executeTask(taskId), executor)
                .thenAccept(result -> handleTaskResult(taskId, batchId, result))
                .exceptionally(throwable -> {
                    handleTaskFailure(taskId, batchId, throwable);
                    return null;
                });
        }
        
        // 等待所有任务完成
        CompletableFuture<Void> allTasks = CompletableFuture.allOf(futures);
        
        try {
            allTasks.get(); // 阻塞等待所有任务完成
            
            long endTime = System.currentTimeMillis();
            printFinalStatistics(endTime - startTime);
            
            // 处理失败任务
            handleFailedTasks();
            
        } catch (Exception e) {
            System.err.println("任务执行过程中发生异常: " + e.getMessage());
        } finally {
            shutdown();
        }
    }
    
    /**
     * 执行单个任务
     */
    private TaskResult executeTask(int taskId) {
        try {
            // 模拟调用外部接口
            boolean success = callExternalAPI(taskId);
            
            if (success) {
                // 模拟数据处理和MySQL写入
                writeToDatabase(taskId);
                return new TaskResult(taskId, true, null, 0);
            } else {
                return new TaskResult(taskId, false, "外部接口调用失败", 0);
            }
            
        } catch (Exception e) {
            return new TaskResult(taskId, false, e.getMessage(), 0);
        }
    }
    
    /**
     * 模拟外部接口调用
     */
    private boolean callExternalAPI(int taskId) throws InterruptedException {
        // 模拟网络延迟 100ms
        Thread.sleep(100);
        
        // 模拟15%的失败率
        return Math.random() > 0.15;
    }
    
    /**
     * 模拟数据库写入
     */
    private void writeToDatabase(int taskId) {
        // 这里应该是实际的数据库写入逻辑
        // 建议使用批量写入优化性能
        System.out.println("任务 " + taskId + " 数据写入数据库成功");
    }
    
    /**
     * 处理任务成功结果
     */
    private void handleTaskResult(int taskId, int batchId, TaskResult result) {
        if (result.isSuccess()) {
            totalSuccess.incrementAndGet();
            batchStats.get(batchId).incrementSuccess();
        } else {
            totalFailure.incrementAndGet();
            batchStats.get(batchId).incrementFailure();
            
            // 将失败任务添加到重试队列
            failedTaskQueue.offer(new FailedTask(taskId, result.getErrorMessage(), 0));
        }
        
        int completed = completedTasks.incrementAndGet();
        
        // 每完成1000个任务输出一次进度
        if (completed % 1000 == 0) {
            System.out.println("已完成任务: " + completed + "/" + TOTAL_TASKS + 
                             " 成功: " + totalSuccess.get() + " 失败: " + totalFailure.get());
        }
    }
    
    /**
     * 处理任务异常
     */
    private void handleTaskFailure(int taskId, int batchId, Throwable throwable) {
        totalFailure.incrementAndGet();
        batchStats.get(batchId).incrementFailure();
        
        failedTaskQueue.offer(new FailedTask(taskId, throwable.getMessage(), 0));
        
        System.err.println("任务 " + taskId + " 执行异常: " + throwable.getMessage());
    }
    
    /**
     * 3. 失败任务处理 - 重试机制
     */
    private void handleFailedTasks() {
        if (failedTaskQueue.isEmpty()) {
            System.out.println("没有失败的任务需要处理");
            return;
        }
        
        System.out.println("开始处理失败任务，数量: " + failedTaskQueue.size());
        
        // 创建专门的重试线程池
        ThreadPoolExecutor retryExecutor = new ThreadPoolExecutor(
            5, 10, 30L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new CustomThreadFactory("RetryProcessor"),
            new ThreadPoolExecutor.CallerRunsPolicy()
        );
        
        List<CompletableFuture<Void>> retryFutures = new ArrayList<>();
        
        while (!failedTaskQueue.isEmpty()) {
            FailedTask failedTask = failedTaskQueue.poll();
            if (failedTask != null) {
                CompletableFuture<Void> retryFuture = CompletableFuture
                    .supplyAsync(() -> retryTask(failedTask), retryExecutor)
                    .thenAccept(result -> handleRetryResult(failedTask, result));
                
                retryFutures.add(retryFuture);
            }
        }
        
        // 等待所有重试任务完成
        CompletableFuture.allOf(retryFutures.toArray(new CompletableFuture[0])).join();
        
        retryExecutor.shutdown();
        System.out.println("失败任务处理完成");
    }
    
    /**
     * 重试单个任务 - 指数退避算法
     */
    private TaskResult retryTask(FailedTask failedTask) {
        int retryCount = failedTask.getRetryCount();
        
        if (retryCount >= MAX_RETRY_COUNT) {
            // 超过最大重试次数，放入死信队列
            System.err.println("任务 " + failedTask.getTaskId() + " 重试次数超限，放入死信队列");
            return new TaskResult(failedTask.getTaskId(), false, "重试次数超限", retryCount);
        }
        
        try {
            // 指数退避：等待时间 = 2^重试次数 秒
            long waitTime = (long) Math.pow(2, retryCount) * 1000;
            Thread.sleep(waitTime);
            
            // 重新执行任务
            boolean success = callExternalAPI(failedTask.getTaskId());
            
            if (success) {
                writeToDatabase(failedTask.getTaskId());
                System.out.println("任务 " + failedTask.getTaskId() + " 重试成功");
                return new TaskResult(failedTask.getTaskId(), true, null, retryCount + 1);
            } else {
                // 重试失败，增加重试次数后重新入队
                failedTask.incrementRetryCount();
                if (failedTask.getRetryCount() < MAX_RETRY_COUNT) {
                    failedTaskQueue.offer(failedTask);
                }
                return new TaskResult(failedTask.getTaskId(), false, "重试仍然失败", retryCount + 1);
            }
            
        } catch (Exception e) {
            failedTask.incrementRetryCount();
            if (failedTask.getRetryCount() < MAX_RETRY_COUNT) {
                failedTaskQueue.offer(failedTask);
            }
            return new TaskResult(failedTask.getTaskId(), false, e.getMessage(), retryCount + 1);
        }
    }
    
    private void handleRetryResult(FailedTask failedTask, TaskResult result) {
        if (result.isSuccess()) {
            System.out.println("重试成功 - 任务ID: " + failedTask.getTaskId());
        } else {
            System.err.println("重试失败 - 任务ID: " + failedTask.getTaskId() + ", 原因: " + result.getErrorMessage());
        }
    }
    
    /**
     * 2. 打印最终统计信息 - 包括每批次的详细统计
     */
    private void printFinalStatistics(long totalTime) {
        System.out.println("\n========== 任务执行统计 ==========");
        System.out.println("总执行时间: " + totalTime + "ms");
        System.out.println("总任务数: " + TOTAL_TASKS);
        System.out.println("成功任务数: " + totalSuccess.get());
        System.out.println("失败任务数: " + totalFailure.get());
        System.out.println("成功率: " + String.format("%.2f%%", (totalSuccess.get() * 100.0 / TOTAL_TASKS)));
        
        System.out.println("\n========== 批次统计详情 ==========");
        batchStats.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                BatchStatistics stats = entry.getValue();
                System.out.println(String.format("批次 %d: 成功 %d, 失败 %d, 成功率 %.2f%%",
                    stats.getBatchId(),
                    stats.getSuccessCount().get(),
                    stats.getFailureCount().get(),
                    stats.getSuccessRate()));
            });
        
        System.out.println("\n========== 线程池统计 ==========");
        System.out.println("核心线程数: " + executor.getCorePoolSize());
        System.out.println("最大线程数: " + executor.getMaximumPoolSize());
        System.out.println("当前线程数: " + executor.getPoolSize());
        System.out.println("曾经的最大线程数: " + executor.getLargestPoolSize());
        System.out.println("已完成任务数: " + executor.getCompletedTaskCount());
    }
    
    private void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }
    
    // ========== 内部类定义 ==========
    
    /**
     * 任务结果封装类
     */
    static class TaskResult {
        private final int taskId;
        private final boolean success;
        private final String errorMessage;
        private final int retryCount;
        
        public TaskResult(int taskId, boolean success, String errorMessage, int retryCount) {
            this.taskId = taskId;
            this.success = success;
            this.errorMessage = errorMessage;
            this.retryCount = retryCount;
        }
        
        // getters
        public int getTaskId() { return taskId; }
        public boolean isSuccess() { return success; }
        public String getErrorMessage() { return errorMessage; }
        public int getRetryCount() { return retryCount; }
    }
    
    /**
     * 批次统计类
     */
    static class BatchStatistics {
        private final int batchId;
        private final AtomicInteger successCount = new AtomicInteger(0);
        private final AtomicInteger failureCount = new AtomicInteger(0);
        
        public BatchStatistics(int batchId) {
            this.batchId = batchId;
        }
        
        public void incrementSuccess() {
            successCount.incrementAndGet();
        }
        
        public void incrementFailure() {
            failureCount.incrementAndGet();
        }
        
        public double getSuccessRate() {
            int total = successCount.get() + failureCount.get();
            return total == 0 ? 0.0 : (successCount.get() * 100.0 / total);
        }
        
        // getters
        public int getBatchId() { return batchId; }
        public AtomicInteger getSuccessCount() { return successCount; }
        public AtomicInteger getFailureCount() { return failureCount; }
    }
    
    /**
     * 失败任务封装类
     */
    static class FailedTask {
        private final int taskId;
        private final String errorMessage;
        private int retryCount;
        
        public FailedTask(int taskId, String errorMessage, int retryCount) {
            this.taskId = taskId;
            this.errorMessage = errorMessage;
            this.retryCount = retryCount;
        }
        
        public void incrementRetryCount() {
            this.retryCount++;
        }
        
        // getters
        public int getTaskId() { return taskId; }
        public String getErrorMessage() { return errorMessage; }
        public int getRetryCount() { return retryCount; }
    }
    
    /**
     * 自定义线程工厂
     */
    static class CustomThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;
        
        public CustomThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix + "-";
        }
        
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, namePrefix + threadNumber.getAndIncrement());
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
    
    /**
     * 主方法 - 演示使用
     */
    public static void main(String[] args) {
        TaskProcessorSolution processor = new TaskProcessorSolution();
        processor.processAllTasks();
    }
}