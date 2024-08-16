package com.chrissy.service.user.repository.entity.req;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户名密码登录方式的请求信息
 * @author chrissy
 * @date 2024/8/13 14:21
 */
@Data
@Accessors(chain = true)
public class UserPwdLoginReq implements Serializable {
    private static final long serialVersionUID = 2139742660700910738L;

    private Long userId;

    private String username;
    private String password;
}
