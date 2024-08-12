package com.chrissy.model.enums.column;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 专栏状态
 * @date 2024/8/12 15:58
 */
@Getter
@AllArgsConstructor
public enum ColumnStateEnum {
    EMPTY(0, ""),
    OFFLINE(1, "未发布"),
    CONTINUE(2, "连载"),
    OVER(3, "已完结"),
    ;

    private final Integer code;
    private final String desc;

    public static ColumnStateEnum formCode(Integer code) {
        for (ColumnStateEnum value : ColumnStateEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ColumnStateEnum.EMPTY;
    }
}
