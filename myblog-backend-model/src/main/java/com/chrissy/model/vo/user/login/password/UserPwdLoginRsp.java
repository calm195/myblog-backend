package com.chrissy.model.vo.user.login.password;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户账号密码登录返回体
 * @author chrissy
 * @date 2024/8/22 2:00
 */
@Data
@Accessors(chain = true)
public class UserPwdLoginRsp implements Serializable {
    private static final long serialVersionUID = 13516541324112L;

    private String nickname;
    private String photo;
}
