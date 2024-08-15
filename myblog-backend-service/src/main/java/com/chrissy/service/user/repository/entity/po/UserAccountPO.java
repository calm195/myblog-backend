package com.chrissy.service.user.repository.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chrissy.model.entity.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户登录表
 * @author chrissy
 * @date 2024/8/13 10:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_login")
public class UserAccountPO extends BasePO {

    private String thirdAccountId;

    private Integer loginType;

    private Integer deleted;

    private String username;

    private String password;
}
