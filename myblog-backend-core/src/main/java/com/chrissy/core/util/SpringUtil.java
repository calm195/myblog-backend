package com.chrissy.core.util;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author chrissy
 * @description Spring 上下文，配置读取，事件广播
 * @date 2024/7/25 10:09
 */
@Component
@Getter
public class SpringUtil implements ApplicationContextAware, EnvironmentAware {
    private volatile static ApplicationContext context;
    private volatile static Environment environment;
    private static Binder binder;

    /**
     * 设置应用上下文
     * @param applicationContext 应用上下文
     * @throws BeansException Beans错误
     */
    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        SpringUtil.context = applicationContext;
    }

    /**
     * 设置配置环境
     * @param environment 配置环境
     */
    @Override
    public void setEnvironment(@NotNull Environment environment) {
        SpringUtil.environment = environment;
        binder = Binder.get(environment);
    }

    /**
     * 发布事件
     * @param event 事件
     */
    public static void publishEvent(ApplicationEvent event){
        context.publishEvent(event);
    }

    /**
     * 获取容器中的Bean对象
     * @param bean Bean对象类型
     * @return 容器中的Bean对象
     * @param <T> 泛型
     */
    public static <T> T getBean(Class<T> bean){
        return context.getBean(bean);
    }

    /**
     * 获取容器中的Bean对象或不存在则返回空
     * @param bean Bean对象类型
     * @return 容器中的Bean对象或空
     * @param <T> 泛型
     */
    public static <T> T getBeanOrNull(Class<T> bean) {
        try {
            return context.getBean(bean);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 获取容器中的Bean对象
     * @param beanName Bean对象名
     * @return 容器中的Bean对象
     */
    public static Object getBean(String beanName){
        return context.getBean(beanName);
    }

    /**
     * 获取容器中的Bean对象或不存在则返回空
     * @param beanName Bean对象名
     * @return 容器中的Bean对象或空
     */
    public static Object getBeanOrNull(String beanName) {
        try {
            return context.getBean(beanName);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 获取配置信息
     * @param key 关键词
     * @return 配置信息字段
     */
    public static String getConfig(String key) {
        return environment.getProperty(key);
    }

    /**
     * 获取配置信息，若无则返回指定值
     * @param key 关键词
     * @return 配置信息字段或指定值
     */
    public static String getConfig(String key, String defaultVal) {
        return environment.getProperty(key, defaultVal);
    }

    /**
     * 获取主要关键词的配置信息，若无则获取候选关键词的配置信息
     * @param mainKey 主要关键词
     * @param slaveKey 候选关键词
     * @return 配置信息字段
     */
    public static String getConfigOrElse(String mainKey, String slaveKey){
        String ans = environment.getProperty(mainKey);
        if (ans == null){
            return environment.getProperty(slaveKey);
        }
        return ans;
    }
}
