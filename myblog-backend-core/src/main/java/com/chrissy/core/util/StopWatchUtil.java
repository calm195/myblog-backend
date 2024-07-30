package com.chrissy.core.util;

import org.springframework.util.StopWatch;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author chrissy
 * @description StopWatch工具类，只支持同步工作的耗时打印
 * @date 2024/7/29 22:34
 */
public class StopWatchUtil {
    private final StopWatch stopWatch;

    /**
     * 根据指定的taskID创建一个StopWatch任务实例，若为空则默认创建
     * @param taskId 任务ID
     */
    private StopWatchUtil(String taskId) {
        stopWatch = taskId == null ? new StopWatch() : new StopWatch(taskId);
    }

    /**
     * 初始化StopWatchUtil，根据任务列表的第一个任务名指定名称
     * @param taskId 任务ID列表
     * @return StopWatchUtil对象
     */
    public static StopWatchUtil init(String... taskId) {
        return new StopWatchUtil(taskId.length > 0 ? taskId[0] : null);
    }

    /**
     * 运行任务并且记录任务运行时间
     * @param taskId 任务名
     * @param call 任务实体
     * @return 任务返回
     * @param <T> 泛型
     */
    public <T> T record(String taskId, Callable<T> call) {
        stopWatch.start(taskId);
        try {
            return call.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            stopWatch.stop();
        }
    }

    /**
     * 运行任务并且记录任务运行时间
     * @param taskId 任务名
     * @param run 任务实体
     */
    public void record(String taskId, Runnable run) {
        stopWatch.start(taskId);
        try {
            run.run();
        } finally {
            stopWatch.stop();
        }
    }

    /**
     * 使用StopWatch自带的prettyPrint方法打印任务耗时情况
     * @return 格式化后的字符串
     */
    public String prettyPrint() {
        return stopWatch.prettyPrint();
    }
}
