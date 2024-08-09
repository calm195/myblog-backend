package com.chrissy.model.enums.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 关注与被关注
 * @date 2024/8/9 17:28
 */
@Getter
@AllArgsConstructor
public enum FollowOrFansEnum {
    EMPTY(0, ""),
    FOLLOW(1, "关注"),
    FANS(2, "粉丝")
    ;

    private final Integer code;
    private final String desc;

    public static FollowOrFansEnum formCode(Integer code) {
        for (FollowOrFansEnum value : FollowOrFansEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return FollowOrFansEnum.EMPTY;
    }
}
