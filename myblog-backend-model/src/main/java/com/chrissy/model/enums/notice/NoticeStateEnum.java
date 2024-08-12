package com.chrissy.model.enums.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 通知状态
 * @date 2024/8/12 14:10
 */
@Getter
@AllArgsConstructor
public enum NoticeStateEnum {
    EMPTY(0, ""),
    UNREAD(1, "未读"),
    READ(2, "已读"),
    ;

    private final Integer code;
    private final String desc;

    public static NoticeStateEnum formCode(Integer code) {
        for (NoticeStateEnum value : NoticeStateEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return NoticeStateEnum.EMPTY;
    }
}
