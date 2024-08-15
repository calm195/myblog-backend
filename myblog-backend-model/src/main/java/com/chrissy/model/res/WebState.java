package com.chrissy.model.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author chrissy
 * @date 2024/8/15 16:24
 */
@Data
public class WebState {
    @ApiModelProperty(value = "状态码, 0表示成功返回，其他异常返回", required = true, example = "0")
    private int code;

    @ApiModelProperty(value = "正确返回时为ok，异常时为描述文案", required = true, example = "ok")
    private String desc;

    private WebState(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据网络状态枚举类型以及描述信息序列创建WebState实例
     * @param webStateEnum 网络状态
     * @param descriptions 描述信息
     * @return WebState实例
     */
    public static WebState newWebState(WebStateEnum webStateEnum, Object... descriptions) {
        String description;
        if (descriptions.length > 0) {
            description = String.format(webStateEnum.getDesc(), descriptions);
        } else {
            description = webStateEnum.getDesc();
        }
        return new WebState(webStateEnum.getCode(), description);
    }
}
