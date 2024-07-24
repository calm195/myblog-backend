package com.chrissy.core.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author chrissy
 * @description
 * @date 2024/7/24 10:21
 */
public class Md5Util {
    /**
     * 对<code>String</code>类型数据进行加密，获取数字摘要 <P>
     * 使用标准字符集 UTF-8 <P>
     * 默认加密方式，加密起始位置为0，长度为<code>data.length</code>
     * @param data 需要加密的数据
     * @return 返回加密后，并且格式化的数字摘要
     */
    public static String encode(String data){
        byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
        return encode(dataBytes);
    }

    /**
     * 对<code>byte[]</code>类型数据进行加密，获取数字摘要 <P>
     * 默认加密方式，加密起始位置为0，长度为<code>data.length</code>
     * @param data 需要加密的数据
     * @return 返回加密后，并且格式化的数字摘要
     */
    public static String encode(byte[] data){
        return encode(data, 0, data.length);
    }

    /**
     * 对传入的数据<code>data</code>进行md5加密，获取数字摘要，其中偏移量为<code>offset</code>，长度为<code>len</code>
     * @param data 需要加密的数据
     * @param offset 偏移量，即开始位置
     * @param len 加密长度
     * @return 返回加密后，并且格式化的数字摘要
     */
    public static String encode(byte[] data, int offset, int len){
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex){
            throw new RuntimeException(ex);
        }

        md.update(data, offset, len);
        byte[] secretBytes = md.digest();
        return formatBytes(secretBytes);
    }

    /**
     * 将得到的加密数据以规范的8 bits输出
     * @param sourceBytes 加密后的数据
     * @return 返回规范的8 bits字符串
     */
    private static String formatBytes(byte[] sourceBytes){
        if (sourceBytes != null && sourceBytes.length != 0){
            StringBuilder stringBuilder = new StringBuilder(32);
            for (byte item: sourceBytes){
                int formattedNum = item & 255;
                String formattedStr = Integer.toString(formattedNum);
                if (formattedStr.length() < 2){
                    stringBuilder.append(0);
                }

                stringBuilder.append(formattedStr);
            }

            return stringBuilder.toString();
        } else {
            return "";
        }
    }
}
