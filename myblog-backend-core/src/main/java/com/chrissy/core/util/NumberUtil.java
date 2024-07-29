package com.chrissy.core.util;

/**
 * @author chrissy
 * @description 数字工具类
 * @date 2024/7/28 14:34
 */
public class NumberUtil {
    /**
     * 判断Long类型数字是否为空或为0
     * @param num Long类型数字
     * @return 是否为空或为0
     */
    public static boolean isNullOrZero(Long num) {
        return num == null || num == 0L;
    }

    /**
     * 判断Integer类型数字是否为空或为0
     * @param num Integer类型数字
     * @return 是否为空或为0
     */
    public static boolean isNullOrZero(Integer num) {
        return num == null || num == 0;
    }

    /**
     * 判断Long类型数字是否大于0
     * @param num Long类型数字
     * @return 是否大于0
     */
    public static boolean isUpZero(Long num) {
        return num != null && num > 0;
    }

    /**
     * 判断Integer类型数字是否大于0
     * @param num Integer类型数字
     * @return 是否大于0
     */
    public static boolean isUpZero(Integer num) {
        return num != null && num > 0;
    }
}
