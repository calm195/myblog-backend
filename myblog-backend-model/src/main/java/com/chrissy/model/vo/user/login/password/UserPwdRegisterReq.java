package com.chrissy.model.vo.user.login.password;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户账号密码注册请求替
 * @author chrissy
 * @date 2024/8/21 22:59
 */
@Data
@Accessors(chain = true)
public class UserPwdRegisterReq implements Serializable {
    private static final long serialVersionUID = 135134571934712L;

    private String username;
    private String password;

    private String nickname;
    private String photo;
    private String profile;
}
