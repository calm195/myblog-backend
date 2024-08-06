package com.chrissy.core.sensitive.ibatis;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author chrissy
 * @description 敏感词缓存
 * @date 2024/8/6 21:00
 */
public class SensitiveMetaCache {
    private static final ConcurrentHashMap<String, SensitiveObjectMeta> CACHE = new ConcurrentHashMap<>();

    /**
     * 获取对应的敏感词字段
     * @param key key
     * @return 敏感词字段
     */
    public static SensitiveObjectMeta get(String key) {
        return CACHE.get(key);
    }

    public static void put(String key, SensitiveObjectMeta meta) {
        CACHE.put(key, meta);
    }

    /**
     * 删除对应的敏感词字段
     * @param key key
     */
    public static void remove(String key) {
        CACHE.remove(key);
    }

    /**
     * 是否包含对应的敏感词字段
     * @param key key
     * @return 是否包含
     */
    public static boolean contains(String key) {
        return CACHE.containsKey(key);
    }

    /**
     * 如果不存在对应的敏感词字段则添加相应对应，返回null；如果存在则返回对应实例
     * @param key key
     * @param meta 敏感词字段
     * @return 敏感词字段
     */
    public static SensitiveObjectMeta putIfAbsent(String key, SensitiveObjectMeta meta) {
        return CACHE.putIfAbsent(key, meta);
    }

    /**
     * 如果不存在key，根据执行函数执行后的结果决定是否添加；此操作是原子的，会阻塞线程，所以尽可能简单
     * @param key key
     * @param function 执行函数，尽可能简单
     * @return 结果
     */
    public static SensitiveObjectMeta computeIfAbsent(String key, Function<String, SensitiveObjectMeta> function) {
        return CACHE.computeIfAbsent(key, function);
    }
}
