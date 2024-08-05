package com.chrissy.core.dal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chrissy
 * @description 数据源切换
 * @date 2024/8/4 14:10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DataSourceAno {
    /**
     * 数据源类型，默认主库
     * @return 枚举体
     */
    DataSourceTypeEnum preset() default DataSourceTypeEnum.MASTER;

    /**
     * 数据源名称，默认空；如果存在，则代替preset
     * @return 名称
     */
    String value() default "";
}
