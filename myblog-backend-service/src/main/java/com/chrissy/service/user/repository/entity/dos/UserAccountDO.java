package com.chrissy.service.user.repository.entity.dos;

import com.chrissy.model.entity.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户登录信息
 * @author chrissy
 * @date 2024/8/13 10:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserAccountDO extends BaseDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 第三方用户ID
     */
    private String thirdAccountId;

    /**
     * 登录方式: 0-微信登录，1-账号密码登录
     */
    private Integer loginType;

    /**
     * 删除标记
     */
    private Integer deleted;

    /**
     * 登录用户名
     */
    private String username;

    /**
     * 登录密码，密文存储
     */
    private String password;
}
