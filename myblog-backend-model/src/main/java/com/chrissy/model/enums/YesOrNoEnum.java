package com.chrissy.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 是否，赋值判断
 * @date 2024/8/12 15:44
 */
@Getter
@AllArgsConstructor
public enum YesOrNoEnum {
    NO(0, "N","否", "no"),
    YES(1,"Y" ,"是", "yes");

    private final int code;
    private final String desc;
    private final String cnDesc;
    private final String enDesc;

    public static YesOrNoEnum formCode(int code) {
        for (YesOrNoEnum yesOrNoEnum : YesOrNoEnum.values()) {
            if (yesOrNoEnum.getCode() == code) {
                return yesOrNoEnum;
            }
        }
        return YesOrNoEnum.NO;
    }

    // TODO：是否能用上？
    public static boolean equalYN(Integer code) {
        if (code == null) {
            return false;
        }
        return code.equals(YES.code) || code.equals(NO.code);
    }

    // TODO：是否能用上？
    public static boolean isYes(Integer code) {
        if (code == null) {
            return false;
        }
        return YesOrNoEnum.YES.getCode() == code;
    }

}
