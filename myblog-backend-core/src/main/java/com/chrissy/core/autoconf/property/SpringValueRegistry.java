package com.chrissy.core.autoconf.property;

import com.chrissy.core.util.SpringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

/**
 * @author chrissy
 * @description SpringValue注册类
 * @date 2024/7/31 14:39
 */
@Slf4j
public class SpringValueRegistry {
    @Data
    public static class SpringValue {
        /**
         * 适合用于：配置是通过set类方法实现注入绑定的方式，只有一个传参，为对应的配置key
         */
        private MethodParameter methodParameter;
        /**
         * 成员变量
         */
        private Field field;
        /**
         * bean实例的弱引用
         */
        private WeakReference<Object> beanRef;
        /**
         * Spring Bean Name
         */
        private String beanName;
        /**
         * 配置对应的key： 如 config.user
         */
        private String key;
        /**
         * 配置引用，如 ${config.user}
         */
        private String placeholder;
        /**
         * 配置绑定的目标类型
         */
        private Class<?> targetType;

        /**
         * 初始化
         * @param key 配置对应的key： 如 config.user
         * @param placeholder 配置引用，如 ${config.user}
         * @param bean bean实例
         * @param beanName bean name
         * @param field 成员变量
         */
        public SpringValue(String key, String placeholder, Object bean, String beanName, Field field) {
            this.beanRef = new WeakReference<>(bean);
            this.beanName = beanName;
            this.field = field;
            this.key = key;
            this.placeholder = placeholder;
            this.targetType = field.getType();
        }

        /**
         * 初始化
         * @param key 配置对应的key： 如 config.user
         * @param placeholder 配置引用，如 ${config.user}
         * @param bean bean实例
         * @param beanName bean name
         * @param method 方法
         */
        public SpringValue(String key, String placeholder, Object bean, String beanName, Method method) {
            this.beanRef = new WeakReference<>(bean);
            this.beanName = beanName;
            this.methodParameter = new MethodParameter(method, 0);
            this.key = key;
            this.placeholder = placeholder;
            Class<?>[] paramTps = method.getParameterTypes();
            this.targetType = paramTps[0];
        }

        /**
         * 配置基于反射的动态变更
         * @param setNewValueFunction String: 配置对应的key   Class: 配置绑定的成员/方法参数类型， Object 新的配置值
         * @throws Exception 异常
         */
        public void update(BiFunction<String, Class<?>, Object> setNewValueFunction) throws Exception {
            if (this.field != null) {
                injectField(setNewValueFunction);
            } else {
                injectMethod(setNewValueFunction);
            }
        }

        /**
         * 反射参数
         * @param setNewValueFunction String: 配置对应的key   Class: 配置绑定的成员/方法参数类型， Object 新的配置值
         * @throws Exception 异常
         */
        private void injectField(BiFunction<String, Class<?>, Object> setNewValueFunction) throws Exception {
            Object bean = beanRef.get();
            if (bean == null) {
                return;
            }
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            field.set(bean, setNewValueFunction.apply(key, field.getType()));
            field.setAccessible(accessible);
            log.info("更新value: {}#{} = {}", beanName, field.getName(), field.get(bean));
        }

        /**
         * 反射方法
         * @param setNewValueFunction String: 配置对应的key   Class: 配置绑定的成员/方法参数类型， Object 新的配置值
         * @throws Exception 异常
         */
        private void injectMethod(BiFunction<String, Class<?>, Object> setNewValueFunction) throws Exception {
            Object bean = beanRef.get();
            if (bean == null) {
                return;
            }
            Object va = setNewValueFunction.apply(key, methodParameter.getParameterType());
            methodParameter.getMethod().invoke(bean, va);
            log.info("更新method: {}#{} = {}", beanName, methodParameter.getMethod().getName(), va);
        }
    }

    private static final Map<String, Set<SpringValue>> VALUES_MAP = new ConcurrentHashMap<>();

    /**
     * 注册配置key绑定的对象
     * @param key @Value内的字符串
     * @param val 匹配的对象值
     */
    public static void register(String key, SpringValue val) {
        if (!VALUES_MAP.containsKey(key)) {
            synchronized (SpringValueRegistry.class) {
                if (!VALUES_MAP.containsKey(key)) {
                    VALUES_MAP.put(key, new HashSet<>());
                }
            }
        }

        Set<SpringValue> set = VALUES_MAP.getOrDefault(key, new HashSet<>());
        set.add(val);
        log.info("注册 {}，值为：{}，现在共有{}个元素", key, val.beanName, set.size());
    }

    /**
     * key对应的配置发生了变更，找到绑定这个配置的属性，进行反射刷新
     * @param key @Value内的字符串
     */
    public static void updateValue(String key) {
        Set<SpringValue> set = VALUES_MAP.getOrDefault(key, new HashSet<>());
        set.forEach(s -> {
            try {
                s.update((s1, aClass) -> SpringUtil.getBinder().bindOrCreate(s1, aClass));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
