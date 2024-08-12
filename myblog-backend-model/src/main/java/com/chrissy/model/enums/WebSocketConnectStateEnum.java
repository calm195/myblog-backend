package com.chrissy.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description WebSocket连接状态
 * @date 2024/8/12 15:43
 */
@Getter
@AllArgsConstructor
public enum WebSocketConnectStateEnum {
    EMPTY(0, ""),
    INIT(1, "初始化"),
    CONNECTING(2, "连接中"),
    CONNECTED(3, "已连接"),
    FAILED(4, "连接失败"),
    CLOSED(5, "已关闭"),
    ;

    private final Integer code;
    private final String desc;

    public static WebSocketConnectStateEnum formCode(Integer code) {
        for (WebSocketConnectStateEnum value : WebSocketConnectStateEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return WebSocketConnectStateEnum.EMPTY;
    }
}
