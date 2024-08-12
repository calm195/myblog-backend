package com.chrissy.model.enums.article;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 文章来源类型
 * @date 2024/8/12 14:54
 */
@Getter
@AllArgsConstructor
public enum ArticleSourceTypeEnum {
    EMPTY(0, ""),
    REPRINT(1, "转载"),
    ORIGINAL(2, "原创"),
    TRANSLATION(3, "翻译"),
    ;

    private final Integer code;
    private final String desc;

    public static ArticleSourceTypeEnum formCode(Integer code) {
        for (ArticleSourceTypeEnum value : ArticleSourceTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ArticleSourceTypeEnum.EMPTY;
    }
}
