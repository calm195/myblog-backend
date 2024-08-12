package com.chrissy.model.enums.article.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 是否为官方
 * @date 2024/8/12 14:30
 */
@Getter
@AllArgsConstructor
public enum OfficialStateEnum {
    EMPTY(0, ""),
    NOT_OFFICIAL(1, "非官方"),
    OFFICIAL(2, "官方"),
    ;

    private final Integer code;
    private final String desc;

    public static OfficialStateEnum formCode(Integer code) {
        for (OfficialStateEnum value : OfficialStateEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return OfficialStateEnum.EMPTY;
    }
}
