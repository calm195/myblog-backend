package com.chrissy.core.util;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author chrissy
 * @description 事务辅助工具类
 * @date 2024/7/29 23:01
 */
public class TransactionUtil {
    /**
     * 注册事务回调 - 事务提交前执行，如果没在事务中就立即执行
     * @param runnable 事务任务
     */
    public static void registryBeforeCommitOrImmediatelyRun(Runnable runnable) {
        if (runnable == null) {
            return;
        }

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void beforeCommit(boolean readOnly) {
                    runnable.run();
                }
            });
        } else {
            runnable.run();
        }
    }

    /**
     * 事务执行完/回滚完之后执行
     * @param runnable 事务任务
     */
    public static void registryAfterCompletionOrImmediatelyRun(Runnable runnable) {
        if (runnable == null) {
            return;
        }

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(int status) {
                    runnable.run();
                }
            });
        } else {
            // 马上执行
            runnable.run();
        }
    }


    /**
     * 事务正常提交后执行
     * @param runnable 事务任务
     */
    public static void registryAfterCommitOrImmediatelyRun(Runnable runnable) {
        if (runnable == null) {
            return;
        }

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    runnable.run();
                }
            });
        } else {
            runnable.run();
        }
    }
}
