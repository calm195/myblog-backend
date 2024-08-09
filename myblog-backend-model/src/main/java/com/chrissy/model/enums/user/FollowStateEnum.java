package com.chrissy.model.enums.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 关注状态
 * @date 2024/8/9 17:31
 */
@Getter
@AllArgsConstructor
public enum FollowStateEnum {
    EMPTY(0, ""),
    NOT_FOLLOW(1, "不关注"),
    FOLLOW(2, "关注"),
    CANCEL_FOLLOW(3, "取消关注"),
    ;

    private final Integer code;
    private final String desc;

    public static FollowStateEnum formCode(Integer code) {
        for (FollowStateEnum value : FollowStateEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return FollowStateEnum.EMPTY;
    }
}
