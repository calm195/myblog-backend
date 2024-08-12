package com.chrissy.model.enums.user.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 阅读状态
 * @date 2024/8/12 15:12
 */
@Getter
@AllArgsConstructor
public enum ReadStateEnum {
    EMPTY(0, ""),
    NOT_READ(1, "未读"),
    READ(2, "已读"),
    ;

    private final Integer code;
    private final String desc;

    public static ReadStateEnum formCode(Integer code) {
        for (ReadStateEnum value : ReadStateEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ReadStateEnum.EMPTY;
    }
}
