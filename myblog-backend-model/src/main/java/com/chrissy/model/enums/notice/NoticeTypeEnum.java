package com.chrissy.model.enums.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 通知类型
 * @date 2024/8/12 14:27
 */
@Getter
@AllArgsConstructor
public enum NoticeTypeEnum {
    // TODO: 细化分类notice各类型
    EMPTY(0, ""),
    COMMENT(1, "评论"),
    REPLY(2, "回复"),
    PRAISE(3, "点赞"),
    COLLECT(4, "收藏"),
    FOLLOW(5, "关注消息"),
    SYSTEM(6, "系统消息"),
    DELETE_COMMENT(1, "删除评论"),
    DELETE_REPLY(2, "删除回复"),
    CANCEL_PRAISE(3, "取消点赞"),
    CANCEL_COLLECT(4, "取消收藏"),
    CANCEL_FOLLOW(5, "取消关注"),
    REGISTER(6, "用户注册"),
    BIND(6, "绑定星球"),
    LOGIN(6, "用户登录"),
    ;

    private final Integer code;
    private final String desc;

    public static NoticeTypeEnum formCode(Integer code) {
        for (NoticeTypeEnum value : NoticeTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return NoticeTypeEnum.EMPTY;
    }
}
