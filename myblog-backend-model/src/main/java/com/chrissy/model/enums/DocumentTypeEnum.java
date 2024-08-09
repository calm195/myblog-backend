package com.chrissy.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 文档类型
 * @date 2024/8/9 17:22
 */
@Getter
@AllArgsConstructor
public enum DocumentTypeEnum {
    EMPTY(0, ""),
    ARTICLE(1, "文章"),
    COMMENT(2, "评论"),
    ;

    private final Integer code;
    private final String desc;

    public static DocumentTypeEnum formCode(Integer code) {
        for (DocumentTypeEnum value : DocumentTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return DocumentTypeEnum.EMPTY;
    }
}
