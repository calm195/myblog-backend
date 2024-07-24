package com.chrissy.core.util;

import com.google.common.collect.Maps;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author chrissy
 * @description 快速构建HashMap的工具类
 * @date 2024/7/24 16:25
 */
public class MapUtil {
    /**
     * 根据传入的键值对，创建一个<code>HashMap</code>对象<p>
     * 要求最少两个参数，第一个参数作为key,第二个参数作为value。紧接着两种类型变量交替循环，完成HashMap的构建
     * @param k 键
     * @param v 值
     * @param kvs 键值对
     * @return 返回一个HashMap对象
     * @param <K> 键的类型
     * @param <V> 值的类型
     */
    public static <K, V> Map<K, V> create(K k, V v, Object... kvs) {
        Map<K, V> map = Maps.newHashMapWithExpectedSize(kvs.length + 1);
        map.put(k, v);
        for (int i = 0; i < kvs.length; i += 2) {
            map.put((K) kvs[i], (V) kvs[i + 1]);
        }
        return map;
    }

    /**
     * 在给定的集合中抽取元素组成Map
     * @param list 集合实例
     * @param key 方法引用，返回值作为key
     * @param val 方法引用，返回值作为value
     * @return 返回HashMap实例
     * @param <T> 给定集合中的类型
     * @param <K> HashMap中的key类型
     * @param <V> HashMap中的value类型
     */
    public static <T, K, V> Map<K, V> toMap(Collection<T> list, Function<T, K> key, Function<T, V> val) {
        if (CollectionUtils.isEmpty(list)) {
            return Maps.newHashMapWithExpectedSize(0);
        }
        return list.stream().collect(Collectors.toMap(key, val));
    }
}
