package com.chrissy.core.util;

/**
 * @author chrissy
 * @description 数字工具类
 * @date 2024/7/28 14:34
 */
public class NumberUtil {
    public static boolean isNullOrZero(Long num) {
        return num == null || num == 0L;
    }

    public static boolean isNullOrZero(Integer num) {
        return num == null || num == 0;
    }

    public static boolean isUpZero(Long num) {
        return num != null && num > 0;
    }

    public static boolean isUpZero(Integer num) {
        return num != null && num > 0;
    }
}
