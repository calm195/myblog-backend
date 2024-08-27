package com.chrissy.model.vo.user.login.password;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 密码登录的请求体
 * @author chrissy
 * @date 2024/8/21 22:51
 */
@Data
@Accessors(chain = true)
public class UserPwdLoginReq implements Serializable {
    private static final long serialVersionUID = 2139742660700910738L;

    private String username;
    private String password;
    private String validateCode;
}
