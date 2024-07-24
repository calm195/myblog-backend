package com.chrissy.core.async;

import java.lang.annotation.*;

/**
 * @author chrissy
 * @description 异步执行注解
 * @date 2024/7/24 20:11
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AsyncExecute {

}
