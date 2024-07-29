package com.chrissy.core.util;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author chrissy
 * @description 验证码工具类
 * @date 2024/7/28 13:52
 */
public class VerificationCodeUtil {
    public static final Integer CODE_LEN = 3;

    private static final Random RANDOM = new Random();

    private static final List<String> SPECIAL_CODES = Arrays.asList(
            "666", "888", "000", "999", "555", "222", "333", "777",
            "520", "911",
            "234", "345", "456", "567", "678", "789"
    );

    /**
     * 根据备选码产生验证码，验证码为三位自然数 201-999
     * @param cnt 备选码
     * @return 验证码字符串
     */
    public static String genCode(int cnt) {
        if (cnt >= SPECIAL_CODES.size()) {
            int num = RANDOM.nextInt(1000);
            if (num >= 100 && num <= 200) {
                // 100-200之间的数字作为关键词回复，不用于验证码
                return genCode(cnt);
            }
            return String.format("%0" + CODE_LEN + "d", num);
        } else {
            return SPECIAL_CODES.get(cnt);
        }
    }

    /**
     * 判断是否是合规的验证码
     * @param content 代验证字符串
     * @return 是否合规
     */
    public static boolean isVerifyCode(String content) {
        if (!NumberUtils.isDigits(content) || content.length() != CODE_LEN) {
            return false;
        }

        int num = Integer.parseInt(content);
        return num < 100 || num > 200;
    }
}
