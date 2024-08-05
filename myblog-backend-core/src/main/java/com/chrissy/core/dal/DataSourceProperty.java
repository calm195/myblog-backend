package com.chrissy.core.dal;

import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author chrissy
 * @description 加载配置多数据源
 * @date 2024/8/4 14:59
 */
@Data
@ConfigurationProperties(prefix = DataSourceProperty.DS_PREFIX)
public class DataSourceProperty {
    public static final String DS_PREFIX = "spring.dynamic";
    /**
     * 默认数据源
     */
    private String primary;

    /**
     * 多数据源配置
     */
    private Map<String, DataSourceProperties> datasource;
}
