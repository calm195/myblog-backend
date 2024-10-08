package com.chrissy.model.enums.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 用户登录类型
 * @date 2024/8/9 11:58
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnum {
    EMPTY(0, ""),
    USERNAME(1, "登录名-密码"),
    WECHAT(2, "微信-验证码"),
    MAIL(3, "邮箱-密码")
    ;

    private final Integer code;
    private final String desc;

    public static LoginTypeEnum formCode(Integer code) {
        for (LoginTypeEnum value : LoginTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return LoginTypeEnum.EMPTY;
    }
}
