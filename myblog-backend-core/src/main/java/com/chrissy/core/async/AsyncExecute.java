package com.chrissy.core.async;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author chrissy
 * @description 异步执行注解
 * @date 2024/7/24 20:11
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AsyncExecute {
    /**
     * 是否开启异步执行，默认开启
     */
    boolean turnOnAsync() default true;

    /**
     * 超时时间，默认 3个超时时间单位
     */
    int timeOut() default 3;

    /**
     * 超时时间单位，默认 1s
     */
    TimeUnit timeOutUnit() default TimeUnit.SECONDS;

    /**
     * 出现超时的兜底逻辑，支持SpEL<p>
     * 如果返回的是空字符串，则表示抛出异常
     */
    String timeOutResponse() default "";
}
