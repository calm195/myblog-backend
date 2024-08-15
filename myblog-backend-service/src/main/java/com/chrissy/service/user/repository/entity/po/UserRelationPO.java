package com.chrissy.service.user.repository.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chrissy.model.entity.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户关系表
 * @author chrissy
 * @date 2024/8/13 10:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_relation")
public class UserRelationPO extends BasePO {

    private Long userId;

    private Long followUserId;

    /**
     * 关注状态: 0-未关注，1-已关注，2-取消关注
     */
    private Integer followState;
}
