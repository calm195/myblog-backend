package com.chrissy.core.util.snowflake;

import cn.hutool.core.lang.Snowflake;

import java.util.Date;

/**
 * @author chrissy
 * @description hutool.core 雪花算法生成ID 包装类
 * @date 2024/7/30 14:28
 */
public class HuToolSnowflakeIdGenerator implements IdGenerator {
    // fixme hutool 里的雪花没有注册中心，不能保证全局唯一，那么单例模式减低重复的效率有多高呢？
    private static final Date EPOC = new Date(2023, 1, 1);
    private final Snowflake snowflake;

    /**
     * 根据数据中心和机器码注册一个id生成器
     * @param workId 机器码
     * @param dataCenter 数据中心id
     */
    public HuToolSnowflakeIdGenerator(int workId, int dataCenter) {
        snowflake = new Snowflake(EPOC, workId, dataCenter, false);
    }

    /**
     * 生成id
     * @return id
     */
    @Override
    public Long nextId() {
        return snowflake.nextId();
    }
}
