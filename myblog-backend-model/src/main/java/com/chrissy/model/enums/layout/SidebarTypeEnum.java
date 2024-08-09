package com.chrissy.model.enums.layout;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 侧边栏类型
 * @date 2024/8/9 15:29
 */
@Getter
@AllArgsConstructor
public enum SidebarTypeEnum {
    EMPTY(0, ""),
    PERSONAL(1, "个人信息"),
    NOTICE(2, "公告"),
    HOT(3, "热门"),
    ;

    private final Integer code;
    private final String desc;

    public static SidebarTypeEnum formCode(Integer code) {
        for (SidebarTypeEnum value : SidebarTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return SidebarTypeEnum.EMPTY;
    }
}
