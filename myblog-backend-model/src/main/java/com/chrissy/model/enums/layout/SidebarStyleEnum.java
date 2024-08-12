package com.chrissy.model.enums.layout;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 侧边栏风格
 * @date 2024/8/12 15:39
 */
@Getter
@AllArgsConstructor
public enum SidebarStyleEnum {
    EMPTY(0, ""),
    NOTICE(1, "通知"),
    ARTICLES(2, "文章"),
    RECOMMEND(3, "推荐"),
    ABOUT(4, "关于"),
    COLUMN(5, "?"),
    PDF(6, "pdf"),
    SUBSCRIBE(7, "S"),
    ACTIVITY_RANK(8, "rank"),
    ;

    private final Integer code;
    private final String desc;

    public static SidebarStyleEnum formCode(Integer code) {
        for (SidebarStyleEnum value : SidebarStyleEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return SidebarStyleEnum.EMPTY;
    }
}
