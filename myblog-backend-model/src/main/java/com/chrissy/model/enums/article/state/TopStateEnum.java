package com.chrissy.model.enums.article.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 置顶状态
 * @date 2024/8/12 15:16
 */
@Getter
@AllArgsConstructor
public enum TopStateEnum {
    EMPTY(0, ""),
    NOT_TOPPING(1, "不置顶"),
    TOPPING(2, "置顶"),
    ;

    private final Integer code;
    private final String desc;

    public static TopStateEnum formCode(Integer code) {
        for (TopStateEnum value : TopStateEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return TopStateEnum.EMPTY;
    }
}
