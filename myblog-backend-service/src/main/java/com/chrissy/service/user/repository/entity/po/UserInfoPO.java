package com.chrissy.service.user.repository.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.chrissy.model.entity.BasePO;
import com.chrissy.service.user.repository.entity.IpInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户信息表
 * @author chrissy
 * @date 2024/8/13 10:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
// autoResultMap 必须存在，否则ip对象无法正确获取，TODO：why
@TableName(value = "user_info", autoResultMap = true)
public class UserInfoPO extends BasePO {
    private Long userId;

    private String nickName;
    private String photo;
    private String profile;

    private String extend;
    private Integer deleted;

    /**
     * 0 -> 普通用户<p>
     * 1 -> 超级管理员
     */
    private Integer userRole;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private IpInfo ip;

    public IpInfo getIp() {
        if (ip == null) {
            ip = new IpInfo();
        }
        return ip;
    }
}
