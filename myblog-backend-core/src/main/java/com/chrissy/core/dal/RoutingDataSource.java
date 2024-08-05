package com.chrissy.core.dal;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.annotation.Nullable;

/**
 * @author chrissy
 * @description 数据源配置获取
 * @date 2024/8/5 17:05
 */
public class RoutingDataSource extends AbstractRoutingDataSource {
    /**
     * 选择当前数据源
     * @return 数据源
     */
    @Nullable
    @Override
    protected Object determineCurrentLookupKey(){
        return DataSourceContextHolder.get();
    }
}
