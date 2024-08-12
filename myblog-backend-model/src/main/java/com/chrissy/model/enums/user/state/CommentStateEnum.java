package com.chrissy.model.enums.user.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 评论状态
 * @date 2024/8/9 14:24
 */
@Getter
@AllArgsConstructor
public enum CommentStateEnum {
    EMPTY(0, ""),
    NOT_COMMENT(1, "未评论"),
    COMMENT(2, "已评论"),
    DELETE_COMMENT(3, "删除评论")
    ;

    private final Integer code;
    private final String desc;

    public static CommentStateEnum formCode(Integer code) {
        for (CommentStateEnum value : CommentStateEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return CommentStateEnum.EMPTY;
    }
}
