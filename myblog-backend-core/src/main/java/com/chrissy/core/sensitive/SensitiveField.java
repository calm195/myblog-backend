package com.chrissy.core.sensitive;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chrissy
 * @description 敏感词绑定db字段
 * @date 2024/8/6 20:21
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface SensitiveField {
    /**
     * 绑定的db中的哪个字段
     * @return db字段
     */
    String bind() default "";
}
