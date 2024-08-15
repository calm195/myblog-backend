package com.chrissy.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 自动读取配置数据库
 * @author chrissy
 * @date 2024/8/13 10:48
 */
@Configuration
@ComponentScan("com.chrissy.service")
@MapperScan(basePackages = {
        "com.chrissy.service.user.repository.mapper"
})
public class MapperAutoConfig {
}
