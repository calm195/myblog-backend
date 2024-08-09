package com.chrissy.model.enums.layout;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description Banner类型，切换显示
 * @date 2024/8/9 17:04
 */
@Getter
@AllArgsConstructor
public enum BannerTypeEnum {
    EMPTY(0, ""),
    DAILY(1, "日常"),
    HOLIDAY(2, "节日假日"),
    ;

    private final Integer code;
    private final String desc;

    public static BannerTypeEnum formCode(Integer code) {
        for (BannerTypeEnum value : BannerTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return BannerTypeEnum.EMPTY;
    }
}
