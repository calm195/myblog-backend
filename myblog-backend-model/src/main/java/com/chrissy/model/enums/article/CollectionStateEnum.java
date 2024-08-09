package com.chrissy.model.enums.article;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 文章收藏状态
 * @date 2024/8/9 14:20
 */
@Getter
@AllArgsConstructor
public enum CollectionStateEnum {
    EMPTY(0, ""),
    NOT_COLLECT(1, "未收藏"),
    COLLECTED(2, "已收藏"),
    CANCEL_COLLECT(3, "取消收藏")
    ;

    private final Integer code;
    private final String desc;

    public static CollectionStateEnum formCode(Integer code) {
        for (CollectionStateEnum value : CollectionStateEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return CollectionStateEnum.EMPTY;
    }
}
