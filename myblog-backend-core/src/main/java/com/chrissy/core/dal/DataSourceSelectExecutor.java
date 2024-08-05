package com.chrissy.core.dal;

import java.util.function.Supplier;

/**
 * @author chrissy
 * @description 手动指定数据源
 * @date 2024/8/5 21:18
 */
public class DataSourceSelectExecutor {
    /**
     * 有返回结果
     * @param ds 数据源
     * @param supplier 业务方法
     * @param <T> 泛型
     * @return 业务方法执行结果
     */
    public static <T> T submit(DataSource ds, Supplier<T> supplier) {
        DataSourceContextHolder.set(ds);
        try {
            return supplier.get();
        } finally {
            DataSourceContextHolder.reset();
        }
    }

    /**
     * 无返回结果
     * @param ds 数据源
     * @param call 业务方法
     */
    public static void execute(DataSource ds, Runnable call) {
        DataSourceContextHolder.set(ds);
        try {
            call.run();
        } finally {
            DataSourceContextHolder.reset();
        }
    }
}
