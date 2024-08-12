package com.chrissy.model.enums.article.state;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 文章发布状态
 * @date 2024/8/12 15:07
 */
@Getter
@AllArgsConstructor
public enum PublishStateEnum {
    EMPTY(0, ""),
    OFFLINE(1, "未发布"),
    REVIEW(2, "审核"),
    ONLINE(3,"已发布"),
    ;

    private final Integer code;
    private final String desc;

    public static PublishStateEnum formCode(Integer code) {
        for (PublishStateEnum value : PublishStateEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return PublishStateEnum.EMPTY;
    }
}
