package com.chrissy.service.user.service;

/**
 * 用户注册服务
 * @author chrissy
 * @date 2024/8/14 13:36
 */
public interface RegisterService {
    /**
     * 通过用户名密码注册
     * @param username 用户名
     * @param password 密码
     * @return 用户id
     */
    Long registerByUsernameAndPassword(String username, String password);

    /**
     * 通过微信账公众号注册，绑定微信
     * @param wechatAccountId 微信账户ID
     * @return 用户id
     */
    Long registerByWechatAccount(String wechatAccountId);
}
