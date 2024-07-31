package com.chrissy.core.mdc;

import java.lang.annotation.*;

/**
 * @author chrissy
 * @description MDC 注解
 * @date 2024/7/30 17:57
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MdcDot {
    /**
     * 日志条目名称
     * @return 实际日志条目名称
     */
    String bizCode() default "";
}
