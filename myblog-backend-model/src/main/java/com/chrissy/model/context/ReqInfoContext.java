package com.chrissy.model.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.Data;

import java.security.Principal;

/**
 * 请求上下文，携带用户身份相关信息
 * @author chrissy
 * @date 2024/8/13 16:04
 */
public class ReqInfoContext {
    private static final TransmittableThreadLocal<ReqInfo> CONTEXTS = new TransmittableThreadLocal<>();

    public static void addReqInfo(ReqInfo reqInfo) {
        CONTEXTS.set(reqInfo);
    }

    public static void clear() {
        CONTEXTS.remove();
    }

    public static ReqInfo getReqInfo() {
        return CONTEXTS.get();
    }

    @Data
    public static class ReqInfo implements Principal {
        /**
         * appKey
         */
        private String appKey;
        /**
         * 访问的域名
         */
        private String host;
        /**
         * 访问路径
         */
        private String path;
        /**
         * 客户端ip
         */
        private String clientIp;
        /**
         * referer
         */
        private String referer;
        /**
         * post 表单参数
         */
        private String payload;
        /**
         * 设备信息
         */
        private String userAgent;

        /**
         * 登录的会话
         */
        private String session;

        /**
         * 用户id
         */
        private Long userId;
        /**
         * 用户信息
         */
//        private BaseUserInfoDTO user;
        /**
         * 消息数量
         */
        private Integer msgNum;

//        private Seo seo;

        private String deviceId;

        @Override
        public String getName() {
            return session;
        }
    }
}
