package com.chrissy.model.vo.user.login.password;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户账号密码注册返回体
 * @author chrissy
 * @date 2024/8/22 2:00
 */
@Data
@Accessors(chain = true)
public class UserPwdRegisterRsp implements Serializable {
    private static final long serialVersionUID = 4512346784628312L;

    private String username;
    private String nickname;
}
