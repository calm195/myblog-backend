package com.chrissy.core.dal;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author chrissy
 * @description 多数据源配置
 * @date 2024/8/5 20:55
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "spring.dynamic", name = "primary")
@EnableConfigurationProperties(DataSourceProperty.class)
public class DataSourceConfig {
    private final Environment environment;

    public DataSourceConfig(Environment environment) {
        this.environment = environment;
        log.info("动态数据源初始化!");
    }

    @Bean
    public DataSourceAspect dsAspect() {
        return new DataSourceAspect();
    }

    @Bean
    public SqlStateInterceptor sqlStateInterceptor() {
        return new SqlStateInterceptor();
    }

    /**
     * 整合主从数据源
     * @param dsProperties 数据源配置
     * @return 数据源
     */
    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperty dsProperties) {
        Map<Object, Object> targetDataSources = Maps.newHashMapWithExpectedSize(dsProperties.getDatasource().size());
        dsProperties.getDatasource().forEach((k, v) -> targetDataSources.put(k.toUpperCase(), initDataSource(k, v)));

        if (CollectionUtils.isEmpty(targetDataSources)) {
            log.error("多数据源配置错误，请以 spring.dynamic 开头");
            throw new IllegalStateException("多数据源配置，请以 spring.dynamic 开头");
        }

        RoutingDataSource routingDataSource = new RoutingDataSource();
        Object key = dsProperties.getPrimary().toUpperCase();
        if (!targetDataSources.containsKey(key)) {
            if (targetDataSources.containsKey(DataSourceTypeEnum.MASTER.name())) {
                key = DataSourceTypeEnum.MASTER.name();
            } else {
                key = targetDataSources.keySet().iterator().next();
            }
        }

        log.info("动态数据源，默认启用为： {}", key);
        routingDataSource.setDefaultTargetDataSource(targetDataSources.get(key));
        routingDataSource.setTargetDataSources(targetDataSources);
        return routingDataSource;
    }

    /**
     * 实例化数据库
     * @param prefix 数据库前缀
     * @param properties 数据库配置项
     * @return 数据库
     */
    public DataSource initDataSource(String prefix, DataSourceProperties properties) {
        if (!DruidUtil.hasDruidPkg()) {
            log.info("实例化HikariDataSource: {}", prefix);
            return properties.initializeDataSourceBuilder().build();
        }

        if (properties.getType() == null || !properties.getType().isAssignableFrom(DruidDataSource.class)) {
            log.info("实例化HikariDataSource: {}", prefix);
            return properties.initializeDataSourceBuilder().build();
        }

        log.info("实例化DruidDataSource: {}", prefix);
        // fixme 知识点：手动将配置赋值到实例中的方式
        return Binder.get(environment).bindOrCreate(DataSourceProperty.DS_PREFIX + ".datasource." + prefix, DruidDataSource.class);
    }

    /**
     * 在数据源实例化之后进行创建Druid数据源监控面板
     * @return 服务配置
     */
    @Bean
    @ConditionalOnExpression(value = "T(com.github.paicoding.forum.core.dal.DruidCheckUtil).hasDruidPkg()")
    public ServletRegistrationBean<?> druidStatViewServlet() {
        //先配置管理后台的servLet，访问的入口为/druid/
        ServletRegistrationBean<?> servletRegistrationBean = new ServletRegistrationBean<>(
                new StatViewServlet(), "/druid/*");
        // IP白名单 (没有配置或者为空，则允许所有访问)
        servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
        // IP黑名单 (存在共同时，deny优先于allow)
        servletRegistrationBean.addInitParameter("deny", "");
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "admin");
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        log.info("开启druid数据源监控面板");
        return servletRegistrationBean;
    }

}
