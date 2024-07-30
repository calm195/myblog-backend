package com.chrissy.core.util;

import com.chrissy.core.util.snowflake.MySnowflakeIdGenerator;
import com.chrissy.core.util.snowflake.SnowflakeIdProducer;

/**
 * @author chrissy
 * @description ID工具类
 * @date 2024/7/30 10:31
 */
public class IdUtil {
    public static SnowflakeIdProducer DEFAULT_ID_PRODUCER = new SnowflakeIdProducer(new MySnowflakeIdGenerator());

    /**
     * 使用ID生成器生成id
     * @return id
     */
    public static Long generateId(){
        return DEFAULT_ID_PRODUCER.generateId();
    }

    /**
     * 使用高进制压缩id长度，并转为字符串
     * @return id字符串
     */
    public static String getIdString(){
        return CompressUtil.longToStr(generateId());
    }
}
