package com.chrissy.core.dal;

import com.github.hui.quick.plugin.qrcode.util.ClassUtils;

/**
 * @author chrissy
 * @description Druid工具类
 * @date 2024/8/5 17:09
 */
public class DruidUtil {
    /**
     * 判断是否包含Druid相关的数据包
     * @return 是否包含
     */
    public static boolean hasDruidPkg() {
        return ClassUtils.isPresent("com.alibaba.druid.pool.DruidDataSource", DataSourceConfig.class.getClassLoader());
    }
}
