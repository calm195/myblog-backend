package com.chrissy.model.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * web api 通用返回类
 * @author chrissy
 * @date 2024/8/15 16:32
 */
@Data
@AllArgsConstructor
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -510306209659393854L;

    @ApiModelProperty(value = "返回结果说明", required = true)
    private WebState status;

    @ApiModelProperty(value = "返回的实体结果", required = true)
    private T content;

    @ApiModelProperty(value = "无实体正常返回结果")
    private final Result<String> OK = new Result<>(WebState.newWebState(WebStateEnum.SUCCESS), WebStateEnum.SUCCESS.getDesc());

    public Result(WebState status) {
        this.status = status;
    }

    public Result(T t) {
        status = WebState.newWebState(WebStateEnum.SUCCESS);
        this.content = t;
    }

    /**
     * 正常返回时，根据返回对象创建Result实例
     * @param t 返回对象
     * @return Result实例
     * @param <T> 泛型
     */
    public static <T> Result<T> ok(T t) {
        return new Result<>(t);
    }

    /**
     * 失败请求时，依据state类型和args生成返回结果
     * @param stateEnum state类型
     * @param args 信息量
     * @return 失败结果
     * @param <T> 泛型
     */
    public static <T> Result<T> fail(WebStateEnum stateEnum, Object... args) {
        return new Result<>(WebState.newWebState(stateEnum, args));
    }

    /**
     * 失败请求时，依据state生成返回结果
     * @param state state
     * @return 失败结果
     * @param <T> 泛型
     */
    public static <T> Result<T> fail(WebState state) {
        return new Result<>(state);
    }
}
