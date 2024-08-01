package com.chrissy.core.autoconf;

import com.chrissy.core.util.SpringUtil;
import com.chrissy.core.util.JsonUtil;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * @author chrissy
 * @description 自定义的配置工厂类，专门用于 ConfDot 属性配置文件的配置加载，支持从自定义的配置源获取
 * @date 2024/8/1 11:16
 */
@Slf4j
@Component
public class DynamicConfigContainer implements EnvironmentAware, ApplicationContextAware, CommandLineRunner {
    private ConfigurableEnvironment environment;
    private ApplicationContext applicationContext;
    /**
     * 存储db中的全局配置，优先级最高
     */
    @Getter
    public Map<String, Object> globalDbConfig;
    private DynamicConfigBinder binder;
    /**
     * 配置变更的回调任务
     */
    @Getter
    private Map<Class<?>, Runnable> refreshCallback = Maps.newHashMap();

    /**
     * 设置ConfigurableEnvironment
     * @param environment ConfigurableEnvironment
     */
    @Override
    public void setEnvironment(@NotNull Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }

    /**
     * 设置当前上下文
     * @param applicationContext 上下文
     * @throws BeansException Bean解析错误
     */
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 依赖注入之后，投入使用之前执行的方法
     */
    @PostConstruct
    public void init() {
        globalDbConfig = Maps.newHashMap();
        bindBeansFromLocalCache("dbConfig", globalDbConfig);
    }

    /**
     * 应用启动之后，执行的动态配置初始化
     * @param args 传入参数
     */
    @Override
    public void run(String... args) {
        reloadConfig();
        registerConfRefreshTask();
    }

    /**
     * 配置绑定
     * @param bindable bindable
     */
    public <T> void bind(Bindable<T> bindable) {
        binder.bind(bindable);
    }

    /**
     * 监听配置的变更
     */
    public void reloadConfig() {
        String before = JsonUtil.toJsonString(globalDbConfig);
        boolean toRefresh = loadAllConfigFromDb();
        if (toRefresh) {
            refreshConfig();
            log.info("配置刷新! 旧:{}, 新:{}", before, JsonUtil.toJsonString(globalDbConfig));
        }
    }

    /**
     * 强制刷新缓存配置
     */
    public void forceRefresh() {
        loadAllConfigFromDb();
        refreshConfig();
        log.info("db配置强制刷新! {}", JsonUtil.toJsonString(globalDbConfig));
    }

    /**
     * 注册配置变更的回调任务
     * @param bean 注册类
     * @param run 运行
     */
    public void registerRefreshCallback(Object bean, Runnable run) {
        refreshCallback.put(bean.getClass(), run);
    }

    /**
     * 注册db的动态配置变更，五分钟扫描一次
     */
    @Scheduled(fixedRate = 300000) // 5 minutes expressed in milliseconds
    private void registerConfRefreshTask() {
        try {
            log.debug("5分钟，自动更新db配置信息!");
            reloadConfig();
        } catch (Exception e) {
            log.warn("自动更新db配置信息异常!", e);
        }
    }

    /**
     * 从db中获取全量的配置信息
     * @return true 表示有信息变更; false 表示无信息变更
     */
    private boolean loadAllConfigFromDb() {
        List<Map<String, Object>> list = SpringUtil.getBean(JdbcTemplate.class).queryForList("select `key`, `value` from global_conf where deleted = 0");
        Map<String, Object> val = Maps.newHashMapWithExpectedSize(list.size());
        for (Map<String, Object> conf : list) {
            val.put(conf.get("key").toString(), conf.get("value").toString());
        }
        if (val.equals(globalDbConfig)) {
            return false;
        }
        globalDbConfig.clear();
        globalDbConfig.putAll(val);
        return true;
    }

    /**
     * 将配置刷新到配置类中，并且热刷新至上下文
     */
    private void refreshConfig() {
        applicationContext.getBeansWithAnnotation(ConfigurationProperties.class).values().forEach(bean -> {
            Bindable<?> target = Bindable.ofInstance(bean).withAnnotations(AnnotationUtils.findAnnotation(bean.getClass(), ConfigurationProperties.class));
            bind(target);
            if (refreshCallback.containsKey(bean.getClass())) {
                refreshCallback.get(bean.getClass()).run();
            }
        });
    }

    /**
     * 获取内存中的配置信息，并且设为最高优先级，以及注册binder
     * @param namespace 内存空间命名
     * @param cache 内存中的配置信息
     */
    private void bindBeansFromLocalCache(String namespace, Map<String, Object> cache) {
        // 将内存的配置信息设置为最高优先级
        MapPropertySource propertySource = new MapPropertySource(namespace, cache);
        environment.getPropertySources().addFirst(propertySource);
        this.binder = new DynamicConfigBinder(this.applicationContext, environment.getPropertySources());
    }
}
