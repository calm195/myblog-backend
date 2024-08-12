package com.chrissy.model.enums.user.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 点赞状态
 * @date 2024/8/12 15:02
 */
@Getter
@AllArgsConstructor
public enum PraiseStateEnum {
    EMPTY(0, ""),
    NOT_PRAISE(1, "未点赞"),
    PRAISE(2, "已点赞"),
    CANCEL_PRAISE(3, "取消点赞"),
    ;

    private final Integer code;
    private final String desc;

    public static PraiseStateEnum formCode(Integer code) {
        for (PraiseStateEnum value : PraiseStateEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return PraiseStateEnum.EMPTY;
    }
}
