package com.chrissy.core.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chrissy
 * @description 文章工具类
 * @date 2024/7/28 12:26
 */
public class ArticleUtil {
    private static final Integer MAX_SUMMARY_CHECK_TXT_LEN = 2000;
    private static final Integer SUMMARY_LEN = 256;
    private static final Pattern LINK_IMG_PATTERN = Pattern.compile("!?\\[(.*?)\\]\\((.*?)\\)");
    private static final Pattern CONTENT_PATTERN = Pattern.compile("[0-9a-zA-Z\u4e00-\u9fa5:;\"'<>,.?/·~！：；“”‘’《》，。？、（）]");
    private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<[^>]+>");

    /**
     * 获取文章中文字部分，移除所有链接，html标签，图片
     * @param summary 文章原始信息
     * @return 文章文字部分
     */
    public static String pickSummary(String summary) {
        if (StringUtils.isBlank(summary)) {
            return StringUtils.EMPTY;
        }

        // 首先移除所有的图片，链接
        summary = summary.substring(0, Math.min(summary.length(), MAX_SUMMARY_CHECK_TXT_LEN)).trim();
        // 移除md的图片、超链
        summary = summary.replaceAll(LINK_IMG_PATTERN.pattern(), "");
        // 移除html标签
        summary = HTML_TAG_PATTERN.matcher(summary).replaceAll("");

        // 匹配对应字符
        StringBuilder result = new StringBuilder();
        Matcher matcher = CONTENT_PATTERN.matcher(summary);
        while (matcher.find()) {
            result.append(summary, matcher.start(), matcher.end());
            if (result.length() >= SUMMARY_LEN) {
                return result.substring(0, SUMMARY_LEN).trim();
            }
        }
        return result.toString().trim();
    }
}
