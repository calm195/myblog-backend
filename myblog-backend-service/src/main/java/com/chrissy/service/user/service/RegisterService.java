package com.chrissy.service.user.service;

import com.chrissy.model.vo.user.login.password.UserPwdRegisterReq;

/**
 * 用户注册服务
 * @author chrissy
 * @date 2024/8/14 13:36
 */
public interface RegisterService {
    /**
     * 通过用户名密码注册
     * @param userPwdRegisterReq 用户注册请求体
     * @return 用户id
     */
    Long registerByUsernameAndPassword(UserPwdRegisterReq userPwdRegisterReq);

    /**
     * 通过微信账公众号注册，绑定微信
     * @param wechatAccountId 微信账户ID
     * @return 用户id
     */
    Long registerByWechatAccount(String wechatAccountId);
}
