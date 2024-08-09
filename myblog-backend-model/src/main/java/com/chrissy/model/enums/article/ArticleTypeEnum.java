package com.chrissy.model.enums.article;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 文章类型
 * @date 2024/8/9 13:42
 */
@Getter
@AllArgsConstructor
public enum ArticleTypeEnum {
    EMPTY(0, ""),
    BLOG(1, "博文"),
    ANSWER(2, "问答"),
    COLUMN(3, "专栏文章"),
    ;

    private final Integer code;
    private final String desc;

    public static ArticleTypeEnum formCode(Integer code) {
        for (ArticleTypeEnum value : ArticleTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ArticleTypeEnum.EMPTY;
    }
}
