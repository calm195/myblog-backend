package com.chrissy.core.permission;

import java.lang.annotation.*;

/**
 * @author chrissy
 * @description Permission注解
 * @date 2024/7/29 17:34
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {
    /**
     * 限定权限，默认是游客VISIT
     *
     * @return 当前权限
     */
    UserRole role() default UserRole.VISIT;
}
