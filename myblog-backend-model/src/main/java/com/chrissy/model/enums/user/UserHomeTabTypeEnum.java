package com.chrissy.model.enums.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 用户主页选项卡类型
 * @date 2024/8/9 17:50
 */
@Getter
@AllArgsConstructor
public enum UserHomeTabTypeEnum {
    EMPTY(0, ""),
    ARTICLE(1, "文章"),
    COLLECTION(2, "收藏"),
    READ(3, "浏览记录"),
    FOLLOW(4, "关注"),
    FANS(5, "粉丝"),
    ;

    private final Integer code;
    private final String desc;

    public static UserHomeTabTypeEnum formCode(Integer code) {
        for (UserHomeTabTypeEnum value : UserHomeTabTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return UserHomeTabTypeEnum.EMPTY;
    }
}
