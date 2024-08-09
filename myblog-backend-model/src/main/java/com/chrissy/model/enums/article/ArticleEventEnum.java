package com.chrissy.model.enums.article;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description
 * @date 2024/8/9 14:06
 */
@Getter
@AllArgsConstructor
public enum ArticleEventEnum {
    EMPTY(0, ""),
    CREATE(1, "创建"),
    ONLINE(2, "发布"),
    REVIEW(3, "审核"),
    DELETE(4, "删除"),
    OFFLINE(5, "下线"),
    ;

    private final Integer code;
    private final String desc;

    public static ArticleEventEnum formCode(Integer code) {
        for (ArticleEventEnum value : ArticleEventEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ArticleEventEnum.EMPTY;
    }
}
