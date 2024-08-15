package com.chrissy.service.user.repository.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.chrissy.model.entity.BasePO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户足迹表
 * @author chrissy
 * @date 2024/8/13 10:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_foot")
public class UserFootPO extends BasePO {
    private Long userId;

    /**
     * 文档ID（文章/评论）
     */
    private Long documentId;

    /**
     * 文档类型：1-文章，2-评论
     */
    private Integer documentType;

    private Long documentUserId;

    /**
     * 收藏状态: 0-未收藏，1-已收藏
     */
    private Integer collectionState;

    /**
     * 阅读状态: 0-未读，1-已读
     */
    private Integer readState;

    /**
     * 评论状态: 0-未评论，1-已评论
     */
    private Integer commentState;

    /**
     * 点赞状态: 0-未点赞，1-已点赞
     */
    private Integer praiseState;
}
