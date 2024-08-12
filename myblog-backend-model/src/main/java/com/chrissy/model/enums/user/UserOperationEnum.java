package com.chrissy.model.enums.user;

import com.chrissy.model.enums.DocumentTypeEnum;
import com.chrissy.model.enums.notice.NoticeTypeEnum;
import com.chrissy.model.enums.user.state.CommentStateEnum;
import com.chrissy.model.enums.user.state.PraiseStateEnum;
import com.chrissy.model.enums.user.state.CollectionStateEnum;
import com.chrissy.model.enums.user.state.ReadStateEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 用户操作动作
 * @date 2024/8/12 15:19
 */
@Getter
@AllArgsConstructor
public enum UserOperationEnum {
    EMPTY(0, "") {
        @Override
        public int getStateCode() {
            return 0;
        }
    },
    READ(1, "阅读") {
        @Override
        public int getStateCode() {
            return ReadStateEnum.READ.getCode();
        }
    },
    PRAISE(2, "点赞") {
        @Override
        public int getStateCode() {
            return PraiseStateEnum.PRAISE.getCode();
        }
    },
    COLLECTION(3, "收藏") {
        @Override
        public int getStateCode() {
            return CollectionStateEnum.COLLECTED.getCode();
        }
    },
    CANCEL_PRAISE(4, "取消点赞") {
        @Override
        public int getStateCode() {
            return PraiseStateEnum.CANCEL_PRAISE.getCode();
        }
    },
    CANCEL_COLLECTION(5, "取消收藏") {
        @Override
        public int getStateCode() {
            return CollectionStateEnum.CANCEL_COLLECT.getCode();
        }
    },
    COMMENT(6, "评论") {
        @Override
        public int getStateCode() {
            return CommentStateEnum.COMMENT.getCode();
        }
    },
    DELETE_COMMENT(7, "删除评论") {
        @Override
        public int getStateCode() {
            return CommentStateEnum.DELETE_COMMENT.getCode();
        }
    },
    ;

    private final Integer code;
    private final String desc;

    public static UserOperationEnum formCode(Integer code) {
        for (UserOperationEnum value : UserOperationEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return UserOperationEnum.EMPTY;
    }

    public abstract int getStateCode();

    /**
     * 判断操作的是否是文章
     * @param type 用户操作类型
     * @return true 表示文章的相关操作 false 表示评论的相关文章
     */
    public static DocumentTypeEnum getOperateDocumentType(UserOperationEnum type) {
        return (type == COMMENT || type == DELETE_COMMENT)
                ? DocumentTypeEnum.COMMENT : DocumentTypeEnum.ARTICLE;
    }

    public static NoticeTypeEnum getNoticeType(UserOperationEnum type) {
        switch (type) {
            case PRAISE:
                return NoticeTypeEnum.PRAISE;
            case CANCEL_PRAISE:
                return NoticeTypeEnum.CANCEL_PRAISE;
            case COLLECTION:
                return NoticeTypeEnum.COLLECT;
            case CANCEL_COLLECTION:
                return NoticeTypeEnum.CANCEL_COLLECT;
            default:
                return null;
        }
    }
}
