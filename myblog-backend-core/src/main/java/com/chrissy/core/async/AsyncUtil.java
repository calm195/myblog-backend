package com.chrissy.core.async;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import com.chrissy.core.util.EnvUtil;
import com.google.common.util.concurrent.SimpleTimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.CollectionUtils;

import java.io.Closeable;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * @author chrissy
 * @description 异步工具类，并发实现
 * @date 2024/7/24 23:25
 */
@Slf4j
public class AsyncUtil {
    private static ExecutorService executorService;
    private static SimpleTimeLimiter simpleTimeLimiter;
    private static final TransmittableThreadLocal<TaskPool> THREAD_LOCAL = new TransmittableThreadLocal<>();
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {
        private final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        @Override
        public Thread newThread(@NotNull Runnable r) {
            Thread thread = this.defaultFactory.newThread(r);
            if (!thread.isDaemon()){
                thread.setDaemon(true);
            }

            thread.setName("my blog's thread - " + this.threadNumber.getAndIncrement());
            return thread;
        }
    };

    static {
        // 默认启用CPU * 2核心线程，对于公共服务，该数量足够
        initExecutorService(Runtime.getRuntime().availableProcessors() * 2, 50);
    }

    /**
     * 初始化线程池<P>
     *
     * 由于是公共的执行仓库，不希望被其他线程影响，因此队列长度为 0，线程数超出最大线程则直接当前线程执行。
     * 预想中的异步情况不多，空闲线程直接回收
     *
     * @param coreThreadNumber 核心线程数量
     * @param maxThreadNumber 最大线程数量
     */
    public static void initExecutorService(int coreThreadNumber, int maxThreadNumber){
        maxThreadNumber = Math.max(coreThreadNumber, maxThreadNumber);

        executorService = new ExecutorBuilder()
                .setCorePoolSize(coreThreadNumber)
                .setMaxPoolSize(maxThreadNumber)
                .setKeepAliveTime(0)
                .setKeepAliveTime(0, TimeUnit.SECONDS)
                .setWorkQueue(new SynchronousQueue<>())
                .setHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                .setThreadFactory(THREAD_FACTORY)
                .buildFinalizable();
        executorService = TtlExecutors.getTtlExecutorService(executorService);
        simpleTimeLimiter = SimpleTimeLimiter.create(executorService);
    }

    /**
     * 在限定的时间内尝试执行方法，如果超时，那么返回一个超时异常，内部任务正常执行。执行完毕则正常返回
     * @param timeDuration 任务执行持续时间次数
     * @param timeUnit 时间单位
     * @param callable 可执行任务
     * @return 任务返回内容
     * @param <T> 泛型
     * @throws ExecutionException 执行时异常
     * @throws InterruptedException 任务中断异常
     * @throws TimeoutException 超时异常
     */
    public static <T> T callWithTimeLimit(long timeDuration, TimeUnit timeUnit, Callable<T> callable)
        throws ExecutionException, InterruptedException, TimeoutException {
        return simpleTimeLimiter.callWithTimeout(callable, timeDuration, timeUnit);
    }

    public static void execute(Runnable call) {
        executorService.execute(call);
    }

    public static <T> Future<T> submit(Callable<T> t) {
        return executorService.submit(t);
    }

    public static boolean sleep(Number timeout, TimeUnit timeUnit) {
        try {
            // TimeUnit 中的sleep方法调用Thread.sleep实现线程sleep
            timeUnit.sleep(timeout.longValue());
            return true;
        } catch (InterruptedException ie) {
            return false;
        }
    }

    public static boolean sleep(Number millis) {
        return millis == null || sleep(millis.longValue());
    }

    public static boolean sleep(long millis) {
        if (millis > 0L) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException ie) {
                return false;
            }
        }

        return true;
    }

    public static void release(){
        THREAD_LOCAL.remove();
    }

    public static class TaskPool implements Closeable {
        private final List<CompletableFuture> taskList;
        private final Map<String, Long> tasksCost;
        private final String taskPoolName;
        private boolean isAllTasksCompleted;
        private final ExecutorService executorService;

        public TaskPool(ExecutorService executorService, String taskPoolName) {
            this.taskPoolName = taskPoolName;
            taskList = new CopyOnWriteArrayList<>();
            tasksCost = new ConcurrentSkipListMap<>();
            tasksCost.put(taskPoolName, System.currentTimeMillis());
            this.executorService = TtlExecutors.getTtlExecutorService(executorService);
            this.isAllTasksCompleted = true;
        }

        public TaskPool() {
            this(AsyncUtil.executorService, "task pool name(template)");
        }

        /**
         * 异步执行，带返回结果
         *
         * @param supplier 执行任务
         * @param name     耗时标识
         * @return 代理任务的返回结果
         */
        public <T> TaskPool async(Supplier<T> supplier, String name) {
            taskList.add(CompletableFuture.supplyAsync(supplyWithCalcTime(supplier, name), this.executorService));
            return this;
        }

        /**
         * 同步执行，待返回结果
         *
         * @param supplier 执行任务
         * @param name     耗时标识
         * @param <T>      返回类型
         * @return 代理任务的返回结果
         */
        public <T> T sync(Supplier<T> supplier, String name) {
            return supplyWithCalcTime(supplier, name).get();
        }

        /**
         * 异步执行，无返回结果
         *
         * @param run  执行任务
         * @param name 耗时标识
         * @return CompletableFutureBridge本身
         */
        public TaskPool async(Runnable run, String name) {
            taskList.add(CompletableFuture.runAsync(runWithCalcTime(run, name), this.executorService));
            return this;
        }

        /**
         * 同步执行，无返回结果
         *
         * @param run  执行任务
         * @param name 耗时标识
         * @return TaskPool本身
         */
        public TaskPool sync(Runnable run, String name) {
            runWithCalcTime(run, name).run();
            return this;
        }

        public TaskPool executeAll() {
            if (!CollectionUtils.isEmpty(taskList)) {
                // 并行执行所有的任务，并且阻塞等待返回
                CompletableFuture.allOf(ArrayUtil.toArray(taskList, CompletableFuture.class)).join();
            }

            this.isAllTasksCompleted = true;
            stopCalcTaskExecTime(this.taskPoolName);

            return this;
        }

        public void print() {
            if (EnvUtil.isProd()){
                return;
            }

            if (!this.isAllTasksCompleted){
                this.executeAll();
            }

            StringBuilder sb = new StringBuilder();
            sb.append('\n');
            long totalCost = tasksCost.remove(taskPoolName);
            sb.append("StopWatch '").append(taskPoolName).append("': running time = ").append(totalCost).append(" ms");
            sb.append('\n');
            if (tasksCost.size() <= 1) {
                sb.append("No task info kept");
            } else {
                sb.append("---------------------------------------------\n");
                sb.append("ms         %     Task name\n");
                sb.append("---------------------------------------------\n");
                NumberFormat pf = NumberFormat.getPercentInstance();
                pf.setMinimumIntegerDigits(2);
                pf.setMinimumFractionDigits(2);
                pf.setGroupingUsed(false);
                for (Map.Entry<String, Long> entry : tasksCost.entrySet()) {
                    sb.append(entry.getValue()).append("\t\t");
                    // TODO: 这个除法是否可以优化
                    sb.append(pf.format(entry.getValue() / (double) totalCost)).append("\t\t");
                    sb.append(entry.getKey()).append("\n");
                }
            }

            log.info("\n---------------------\n{}\n--------------------\n", sb);
        }

        @Override
        public void close() throws IOException {
            try {
                if (!this.isAllTasksCompleted){
                    this.executeAll();
                }

                AsyncUtil.release();
                this.print();
            } catch (Exception ex){
                log.error("释放耗时上下文异常! {}", taskPoolName, ex);
            }
        }

        private Runnable runWithCalcTime(Runnable runnable, String taskName){
            return () -> {
                startCalcTaskExecTime(taskName);
                try {
                    runnable.run();
                } finally {
                    stopCalcTaskExecTime(taskName);
                }
            };
        }

        private <T> Supplier<T> supplyWithCalcTime(Supplier<T> supplier, String taskName){
            return () -> {
                startCalcTaskExecTime(taskName);
                try {
                    return supplier.get();
                } finally {
                    stopCalcTaskExecTime(taskName);
                }
            };
        }

        private void startCalcTaskExecTime(String taskName){
            tasksCost.put(taskName, System.currentTimeMillis());
        }

        private void stopCalcTaskExecTime(String taskName){
            long now = System.currentTimeMillis();
            long before = tasksCost.getOrDefault(taskName, now);
            if (before <= now){
                tasksCost.put(taskName, now - before);
            }
        }
    }

    public static TaskPool currentTaskPool(String... name) {
        if (name.length > 0) {
            return new TaskPool(AsyncUtil.executorService, name[0]);
        }
        return new TaskPool();
    }

    public static TaskPool startTaskPool(ExecutorService executorService, String taskPoolName) {
        TaskPool bridge = new TaskPool(executorService, taskPoolName);
        THREAD_LOCAL.set(bridge);
        return bridge;
    }

    public static TaskPool getTaskPool() {
        return THREAD_LOCAL.get();
    }
}
