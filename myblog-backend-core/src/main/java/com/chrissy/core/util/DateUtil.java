package com.chrissy.core.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author chrissy
 * @description 时间工具类
 * @date 2024/7/24 0:16
 */
public class DateUtil {
    /**
     * 各类型时间格式
     */
    public static final DateTimeFormatter UTC_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public static final DateTimeFormatter DB_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final DateTimeFormatter BLOG_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm");
    public static final DateTimeFormatter BLOG_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy年MM月dd日");

    /**
     * 一天中的秒数分钟数等
     */
    public static final Long ONE_DAY_MILL = 86400_000L;
    public static final Long ONE_DAY_SECONDS = 86400L;
    // 假设一个月31天，31 * 86400L = 2_678_400L TODO：把一个月天数实际落地
    public static final Long ONE_MONTH_SECONDS = 2_678_400L;

    /**
     * 将时间戳转化为 yyyy年MM月dd日 HH:mm
     * @param timestamp 时间戳
     * @return 格式化后的时间字符串
     */
    public static String timestampToBlogTime(long timestamp){
        return format(BLOG_TIME_FORMAT, timestamp);
    }

    /**
     * 将时间戳转化为 yyyy年MM月dd日
     * @param timestamp 时间戳
     * @return 格式化后的时间字符串
     */
    public static String timestampToBlogDate(long timestamp){
        return format(BLOG_DATE_FORMAT, timestamp);
    }

    /**
     * 将时间戳转化为 yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     * @param timestamp 时间戳
     * @return 格式化后的时间字符串
     */
    public static String timestampToUtcMillTime(long timestamp){
        return format(UTC_FORMAT, timestamp);
    }

    /**
     * 将时间戳转化为 yyyy-MM-dd HH:mm:ss.SSS
     * @param timestamp 时间戳
     * @return 格式化后的时间字符串
     */
    public static String timestampToDbTime(long timestamp){
        return format(DB_FORMAT, timestamp);
    }

    /**
     * 将时间戳转化为当地时间
     * @param timestamp 时间戳
     * @return LocalDateTime实例
     */
    public static LocalDateTime timestampToLocalDateTime(long timestamp){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    /**
     * 根据指定的时间格式，将时间戳转化为当地时间
     * @param formatter 指定的时间格式
     * @param timestamp 时间戳
     * @return 符合格式的时间字符串
     */
    public static String format(DateTimeFormatter formatter, long timestamp){
        LocalDateTime localDateTime = timestampToLocalDateTime(timestamp);
        return formatter.format(localDateTime);
    }
}
