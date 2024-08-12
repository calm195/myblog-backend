package com.chrissy.model.enums.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 用户权限角色
 * @date 2024/8/12 14:52
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {
    EMPTY(0, ""),
    VISIT(1, "访客"),
    NORMAL(2, "普通用户"),
    ADMIN(3, "超级用户"),
    ;

    private final Integer code;
    private final String desc;

    public static RoleEnum formCode(Integer code) {
        for (RoleEnum value : RoleEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return RoleEnum.EMPTY;
    }
}
