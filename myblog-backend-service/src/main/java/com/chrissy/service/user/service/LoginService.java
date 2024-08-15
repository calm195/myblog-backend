package com.chrissy.service.user.service;

/**
 * 用户登录服务
 * @author chrissy
 * @date 2024/8/13 14:29
 */
public interface LoginService {
    String SESSION_KEY = "f-session";
    String USER_DEVICE_KEY = "f-device";

    /**
     * 适用于微信公众号登录场景下，自动注册一个用户
     *
     * @param wechatAccountId 微信唯一标识
     * @return 携带用户id的session
     */
    String loginOrAutoRegisterByWechat(String wechatAccountId);

    /**
     * 登出
     *
     * @param session 用户会话
     */
    void logout(String session);

    /**
     * 用户名密码方式登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 携带用户id的session
     */
    String loginByUsernameAndPassword(String username, String password);
}
