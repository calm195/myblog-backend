package com.chrissy.core.util;

import java.nio.ByteBuffer;

/**
 * @author chrissy
 * @description 数据压缩工具类
 * @date 2024/7/28 14:03
 */
public class CompressUtil {
    // 进制转换数组
    private static final char[] BINARY_ARRAY = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * 整数的进制转换，转换为62进制（最大压缩）
     *
     * @param num  数字
     * @return 返回String格式的数据
     */
    public static String longToStr(long num) {
        return longToStr(num, BINARY_ARRAY.length);
    }

    /**
     * 整数的进制转换
     *
     * @param num  数字
     * @param size 进制长度
     * @return 返回String格式的数据
     */
    public static String longToStr(long num, int size) {
        if (size > BINARY_ARRAY.length) {
            size = BINARY_ARRAY.length;
        }

        StringBuilder builder = new StringBuilder();
        while (num > 0) {
            builder.insert(0, BINARY_ARRAY[(int) (num % size)]);
            num /= size;
        }
        return builder.toString();
    }
}
