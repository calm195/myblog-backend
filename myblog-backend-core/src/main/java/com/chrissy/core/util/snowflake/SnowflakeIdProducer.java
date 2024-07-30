package com.chrissy.core.util.snowflake;

import com.chrissy.core.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author chrissy
 * @description 基于雪花算法的id提供者
 * @date 2024/7/30 10:44
 */
@Slf4j
public class SnowflakeIdProducer {
    // TODO: 拆分逻辑
    private final BlockingQueue<Long> snowflakeIdQueue;
    public static final Long ID_EXPIRE_TIME_INTER = DateUtil.ONE_DAY_MILL;
    private static final int QUEUE_SIZE = 10;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor((Runnable r) -> {
        Thread t = new Thread(r);
        t.setName("SnowflakeID-generate-thread");
        t.setDaemon(true);
        return t;
    });

    /**
     * 将注入的id生成器注入线程池
     * @param generator id生成器
     */
    public SnowflakeIdProducer(final IdGenerator generator) {
        snowflakeIdQueue = new LinkedBlockingQueue<>(QUEUE_SIZE);

        executorService.submit(() -> {
            long lastTime = System.currentTimeMillis();
            while (true) {
                try {
                    Long id = generator.nextId();
                    if (snowflakeIdQueue.offer(id, 1, TimeUnit.MINUTES)){
                        log.info("generate id: {}", id.toString());
                    }
                } catch (InterruptedException ignored) {
                } catch (Exception e) {
                    log.info("generate id error! {}", e.getMessage());
                }

                // 当出现跨天时，自动重置业务id
                try {
                    long now = System.currentTimeMillis();
                    if (now / ID_EXPIRE_TIME_INTER - lastTime / ID_EXPIRE_TIME_INTER > 0) {
                        // 跨天，清空队列
                        snowflakeIdQueue.clear();
                        log.info("清空id队列，重新设置");
                    }
                    lastTime = now;

                } catch (Exception e) {
                    log.info("auto remove illegal ids error! {}", e.getMessage());
                }
            }
        });
    }

    /**
     * 从id队列中取出一个id
     * @return id
     */
    public Long generateId() {
        try {
            return snowflakeIdQueue.take();
        } catch (InterruptedException e) {
            log.error("雪花算法生成逻辑异常", e);
            throw new RuntimeException("雪花算法生成id异常!", e);
        }
    }
}
