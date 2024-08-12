package com.chrissy.model.enums.article;

import com.chrissy.model.enums.article.state.EssenceStateEnum;
import com.chrissy.model.enums.article.state.OfficialStateEnum;
import com.chrissy.model.enums.article.state.TopStateEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author chrissy
 * @description 关于文章状态的操作
 * @date 2024/8/12 14:38
 */
@Getter
@AllArgsConstructor
public enum ArticleStateOperationEnum {
    EMPTY(0, ""){
        @Override
        public int getStateCode(){
            return 0;
        }
    },
    OFFICIAL(1, "官方") {
        @Override
        public int getStateCode() {
            return OfficialStateEnum.OFFICIAL.getCode();
        }
    },
    CANCEL_OFFICIAL(2, "非官方"){
        @Override
        public int getStateCode() {
            return OfficialStateEnum.NOT_OFFICIAL.getCode();
        }
    },
    TOPPING(3, "置顶"){
        @Override
        public int getStateCode() {
            return TopStateEnum.TOPPING.getCode();
        }
    },
    CANCEL_TOPPING(4, "不置顶"){
        @Override
        public int getStateCode() {
            return TopStateEnum.NOT_TOPPING.getCode();
        }
    },
    CREAM(5, "加精"){
        @Override
        public int getStateCode() {
            return EssenceStateEnum.ESSENTIAL.getCode();
        }
    },
    CANCEL_CREAM(6, "不加精"){
        @Override
        public int getStateCode() {
            return EssenceStateEnum.UNESSENTIAL.getCode();
        }
    }
    ;

    private final Integer code;
    private final String desc;

    public static ArticleStateOperationEnum formCode(Integer code) {
        for (ArticleStateOperationEnum value : ArticleStateOperationEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ArticleStateOperationEnum.EMPTY;
    }

    public abstract int getStateCode();
}
