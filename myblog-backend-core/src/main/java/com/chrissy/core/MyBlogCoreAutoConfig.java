package com.chrissy.core;

import com.chrissy.core.config.ProxyProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author chrissy
 * @description 自动配置core包
 * @date 2024/7/30 16:48
 */
@Configuration
@EnableConfigurationProperties(ProxyProperties.class)
@ComponentScan(basePackages = "com.chrissy.core")
public class MyBlogCoreAutoConfig {
}
