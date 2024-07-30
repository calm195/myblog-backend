package com.chrissy.core.util.snowflake;

/**
 * @author chrissy
 * @description ID生成接口
 * @date 2024/7/30 10:32
 */
public interface IdGenerator {
    /**
     * 获取一个全局唯一id
     * @return id
     */
    Long nextId();
}
