package com.chrissy.service.user.service;

import com.chrissy.service.user.repository.entity.vo.UserPwdLoginReq;

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
     * @param uuid 微信唯一标识
     * @return userId 用户主键
     */
    Long autoRegisterWxUserInfo(String uuid);

    /**
     * 登出
     *
     * @param session 用户会话
     */
    void logout(String session);

    /**
     * 给微信公众号的用户生成一个用于登录的会话
     *
     * @param userId 用户主键id
     * @return
     */
    String loginByWx(Long userId);

    /**
     * 用户名密码方式登录
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    String loginByUsernameAndPassword(String username, String password);

    /**
     * 注册登录
     *
     * @param loginReq 登录信息
     * @return
     */
    String registerByUserPwd(UserPwdLoginReq loginReq);
}
