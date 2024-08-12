package com.chrissy.model.enums.column;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 专栏阅读权限
 * @date 2024/8/12 16:00
 */
@Getter
@AllArgsConstructor
public enum ColumnAccessTypeEnum {
    EMPTY(0, ""),
    VISIT(1, "路人可读"),
    LOGIN(2, "需要登录"),
    FANS(3, "他/她的粉丝"),
    ;

    private final Integer code;
    private final String desc;

    public static ColumnAccessTypeEnum formCode(Integer code) {
        for (ColumnAccessTypeEnum value : ColumnAccessTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ColumnAccessTypeEnum.EMPTY;
    }
}
