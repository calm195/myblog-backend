package com.chrissy.model.enums.article;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 加精状态
 * @date 2024/8/9 17:09
 */
@Getter
@AllArgsConstructor
public enum EssenceStateEnum {
    EMPTY(0, ""),
    INESSENTIAL(1, "不加精"),
    ESSENTIAL(2, "加精"),
    UNESSENTIAL(3, "取消加精"),
    ;

    private final Integer code;
    private final String desc;

    public static EssenceStateEnum formCode(Integer code) {
        for (EssenceStateEnum value : EssenceStateEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return EssenceStateEnum.EMPTY;
    }
}
