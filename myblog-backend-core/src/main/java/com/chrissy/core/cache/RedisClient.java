package com.chrissy.core.cache;

import com.chrissy.core.util.JsonUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author chrissy
 * @description 包装redis的工具类
 * @date 2024/7/26 17:21
 */
public class RedisClient {
    // TODO: 使用Options封装方法+RedisSerializer自定义序列器
    private static final Charset CHARSET_UTF_8 = StandardCharsets.UTF_8;
    private static final String KEY_PREFIX = "blog_";
    private static RedisTemplate<String, String> redisTemplate;

    /**
     * 注入RedisTemplate对象
     * @param redisTemplate RedisTemplate对象
     */
    public static void registerRedisTemplate(RedisTemplate<String, String> redisTemplate){
        RedisClient.redisTemplate = redisTemplate;
    }

    /**
     * String类型操作，设置Value
     * @param key key
     * @param value value
     * @return 是否设置成功
     */
    public static boolean setStr(String key, String value){
        return Boolean.TRUE.equals(redisTemplate.execute((RedisCallback<Boolean>) con -> con.set(makeKeyBytesBy(key), getBytesOf(value))));
    }

    /**
     * String类型操作，获取Value
     * @param key key
     * @return Value或者空
     */
    public static String getStr(String key){
        return redisTemplate.execute((RedisCallback<String>) con -> {
            byte[] value = con.get(makeKeyBytesBy(key));
            return value == null ? null : new String(value);
        });
    }

    // TODO: 使删除结果可知
    /**
     * String类型操作，删除
     * @param key key
     */
    public static void del(String key){
        redisTemplate.execute((RedisCallback<Long>) con -> con.del(makeKeyBytesBy(key)));
    }

    /**
     * 设置过期时间
     * @param key key
     * @param expire 过期时间
     * @return 是否设置成功
     */
    public static boolean setExpire(String key, Long expire){
        return Boolean.TRUE.equals(redisTemplate.execute((RedisCallback<Boolean>) con -> con.expire(makeKeyBytesBy(key), expire)));
    }

    /**
     * String类型操作，设置value与过期时间
     * @param key key
     * @param value value
     * @param expire 过期时间
     * @return 是否设置成功
     */
    public static boolean setValueWithExpire(String key, String value, Long expire) {
        return Boolean.TRUE.equals(redisTemplate.execute((RedisCallback<Boolean>) con -> con.setEx(makeKeyBytesBy(key), expire, getBytesOf(value))));
    }

    /**
     * 获取过期时间
     * @param key key
     * @return 过期时间
     */
    public static Long getTtl(String key){
        return redisTemplate.execute((RedisCallback<Long>) con -> con.ttl(makeKeyBytesBy(key)));
    }

    /**
     * Hash操作，获取所有对象
     * @param key key
     * @param clazz 类型
     * @return 所有对象集合
     * @param <T> 泛型
     */
    public static <T> Map<String, T> hGetAll(String key, Class<T> clazz){
        Map<byte[], byte[]> recordMap = redisTemplate.execute((RedisCallback<Map<byte[], byte[]>>) con -> con.hGetAll(makeKeyBytesBy(key)));

        if (recordMap == null){
            return Collections.emptyMap();
        }

        Map<String, T> resultMap = Maps.newHashMapWithExpectedSize(recordMap.size());
        for (Map.Entry<byte[], byte[]> entry: recordMap.entrySet()){
            if (entry.getKey() == null){
                continue;
            }

            resultMap.put(new String(entry.getKey()), toObject(entry.getValue(), clazz));
        }
        return resultMap;
    }

    /**
     * Hash操作，获取key下field对应的内容
     * @param key key
     * @param field field
     * @param clazz 类型
     * @return 对应内容实例化后的对象
     * @param <T> 泛型
     */
    public static <T> T hGet(String key, String field, Class<T> clazz){
        return redisTemplate.execute((RedisCallback<T>) con -> {
           byte[] record = con.hGet(makeKeyBytesBy(key), getBytesOf(field));
           if (record == null){
               return null;
           }
           return toObject(record, clazz);
        });
    }

    /**
     * Hash操作，增加key下field对应的内容
     * @param key key
     * @param field field
     * @param number 增加数量
     * @return
     */
    public static Long hIncrBy(String key, String field, Integer number){
        return redisTemplate.execute((RedisCallback<Long>) con -> con.hIncrBy(makeKeyBytesBy(key), getBytesOf(field), number));
    }

    /**
     * Hash操作，删除key下field对应的内容
     * @param key key
     * @param field field
     * @return 是否删除成功
     */
    public static Boolean hDel(String key, String field){
        return redisTemplate.execute((RedisCallback<Boolean>) con -> Objects.requireNonNull(con.hDel(makeKeyBytesBy(key), getBytesOf(field))) > 0);
    }

    /**
     * Hash操作，设置key下field对应的内容
     * @param key key
     * @param field field
     * @param value value
     * @return 是否设置成功
     * @param <T> 泛型
     */
    public static <T> Boolean hSet(String key, String field, T value){
        return redisTemplate.execute((RedisCallback<Boolean>) con -> con.hSet(makeKeyBytesBy(key), getBytesOf(field), getBytesOf(value)));
    }

    /**
     * Hash操作，设置key下Map信息
     * @param key key
     * @param fields fields
     * @param <T> 泛型
     */
    public static <T> void hMSet(String key, Map<String, T> fields){
        Map<byte[], byte[]> values = Maps.newHashMapWithExpectedSize(fields.size());
        for(Map.Entry<String, T> field: fields.entrySet()){
            values.put(makeKeyBytesBy(field.getKey()), getBytesOf(field.getValue()));
        }

        redisTemplate.execute((RedisCallback<Object>) con -> {
            con.hMSet(makeKeyBytesBy(key), values);
            return null;
        });
    }

    /**
     * Hash操作，获取key下Map信息
     * @param key key
     * @param fields fields
     * @param clazz 类型
     * @return key下所有信息
     * @param <T> 泛型
     */
    public static <T> Map<String, T> hMGet(String key, final List<String> fields, Class<T> clazz){
        return redisTemplate.execute((RedisCallback<Map<String,T>>) con -> {
            byte[][] fieldsBytes = new byte[fields.size()][];
            IntStream.range(0, fields.size()).forEach(i -> fieldsBytes[i] = getBytesOf(fields.get(i)));
            List<byte[]> resultBytes = con.hMGet(makeKeyBytesBy(key), fieldsBytes);
            if (resultBytes == null) {
                return null;
            }
            Map<String, T> resultMap = Maps.newHashMapWithExpectedSize(fields.size());
            IntStream.range(0, fields.size()).forEach(i -> resultMap.put(fields.get(i), toObject(resultBytes.get(i), clazz)));
            return resultMap;
        });
    }

    /**
     * Set操作，设置key下内容
     * @param key key
     * @param value value
     * @return 是否设置成功
     * @param <T> 泛型
     */
    public static <T> Boolean sIsMember(String key, T value) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.sIsMember(makeKeyBytesBy(key), getBytesOf(value)));
    }

    /**
     * Set操作，获取key下所有内容
     * @param key key
     * @param clz 类型
     * @return List，key下所有内容
     * @param <T> 泛型
     */
    public static <T> Set<T> sGetAll(String key, Class<T> clz) {
        return redisTemplate.execute((RedisCallback<Set<T>>) connection -> {
            Set<byte[]> set = connection.sMembers(makeKeyBytesBy(key));
            if (CollectionUtils.isEmpty(set)) {
                return Collections.emptySet();
            }
            return set.stream().map(s -> toObject(s, clz)).collect(Collectors.toSet());
        });
    }

    /**
     * Set操作，增加key，内容
     * @param key key
     * @param val value
     * @return 是否设置成功
     * @param <T> 泛型
     */
    public static <T> boolean sPut(String key, T val) {
        Long res =  redisTemplate.execute((RedisCallback<Long>) con -> con.sAdd(makeKeyBytesBy(key), getBytesOf(val)));
        return res != null && res > 0;
    }

    /**
     * Set操作，删除key下内容
     * @param key key
     * @param val value
     * @param <T> 泛型
     */
    public static <T> void sDel(String key, T val) {
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.sRem(makeKeyBytesBy(key), getBytesOf(val));
            return null;
        });
    }

    /**
     * ZSet操作，增加key下内容的权重
     * @param key key
     * @param value value
     * @param score 增加值
     * @return 增加结果
     */
    public static Double zIncrBy(String key, String value, Integer score) {
        return redisTemplate.execute((RedisCallback<Double>) connection -> connection.zIncrBy(makeKeyBytesBy(key), score, getBytesOf(value)));
    }

    /**
     * ZSet操作，获取key下内容的权重
     * @param key key
     * @param value value
     * @return <排名, 权重数值>
     */
    public static ImmutablePair<Integer, Double> zRankInfo(String key, String value) {
        double score = zScore(key, value);
        int rank = zRank(key, value);
        return ImmutablePair.of(rank, score);
    }

    /**
     * ZSet操作，获取key下排名前N名的内容
     * @param key key
     * @param n 前n名
     * @return <内容，分数>
     */
    public static List<ImmutablePair<String, Double>> zTopNScore(String key, int n) {
        return redisTemplate.execute((RedisCallback<List<ImmutablePair<String, Double>>>) connection -> {
            Set<RedisZSetCommands.Tuple> set = connection.zRangeWithScores(makeKeyBytesBy(key), -n, -1);
            if (set == null) {
                return Collections.emptyList();
            }
            return set.stream()
                    .map(tuple -> ImmutablePair.of(toObject(tuple.getValue(), String.class), tuple.getScore()))
                    .sorted((o1, o2) -> Double.compare(o2.getRight(), o1.getRight())).collect(Collectors.toList());
        });
    }

    /**
     * List操作，左边插入数据
     * @param key key
     * @param val value
     * @return 插入结果
     * @param <T> 泛型
     */
    public static <T> Long lPush(String key, T val) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> connection.lPush(makeKeyBytesBy(key), getBytesOf(val)));
    }

    /**
     * List操作，右边插入数据
     * @param key key
     * @param val value
     * @return 插入结果
     * @param <T> 泛型
     */
    public static <T> Long rPush(String key, T val) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> connection.rPush(makeKeyBytesBy(key), getBytesOf(val)));
    }

    /**
     * List操作，获取范围数据
     * @param key key
     * @param start 开始位置
     * @param size 范围
     * @param clz 类型
     * @return List，内容结果
     * @param <T> 泛型
     */
    public static <T> List<T> lRange(String key, int start, int size, Class<T> clz) {
        return redisTemplate.execute((RedisCallback<List<T>>) connection -> {
            List<byte[]> list = connection.lRange(makeKeyBytesBy(key), start, size);
            if (CollectionUtils.isEmpty(list)) {
                return new ArrayList<>();
            }
            return list.stream().map(k -> toObject(k, clz)).collect(Collectors.toList());
        });
    }

    /**
     * List操作，裁剪并保留范围数据
     * @param key key
     * @param start 开始位置
     * @param size 范围
     */
    public static void lTrim(String key, int start, int size) {
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.lTrim(makeKeyBytesBy(key), start, size);
            return null;
        });
    }

    /**
     * 获取流操作对象
     * @return 流操作对象
     */
    public static PipelineAction getPipelineAction(){
        return new PipelineAction();
    }

    public static class PipelineAction {
        private final List<Runnable> actions = new ArrayList<>();
        private RedisConnection redisConnection;

        /**
         * 增加任务
         * @param key key
         * @param con 任务
         * @return 本身this
         */
        public PipelineAction add(String key, BiConsumer<RedisConnection, byte[]> con){
            actions.add(() -> con.accept(redisConnection, makeKeyBytesBy(key)));
            return this;
        }

        /**
         * 增加任务
         * @param key key
         * @param field field
         * @param con 任务
         * @return 本身this
         */
        public PipelineAction add(String key, String field, ThreeConsumer<RedisConnection, byte[], byte[]> con){
            actions.add(() -> con.accept(redisConnection, makeKeyBytesBy(key), getBytesOf(field)));
            return this;
        }

        /**
         * 执行所有任务
         */
        public void execute() {
            redisTemplate.executePipelined((RedisCallback<Object>) con -> {
               PipelineAction.this.redisConnection = con;
               actions.forEach(Runnable::run);
               return null;
            });
        }
    }

    @FunctionalInterface
    public interface ThreeConsumer<T, K, V> {
        void accept(T t, K k, V v);
    }

    /**
     * ZSet操作，获取key下内容对应分数
     * @param key key
     * @param value value
     * @return 分数
     */
    private static Double zScore(String key, String value) {
        return redisTemplate.execute((RedisCallback<Double>) connection -> connection.zScore(makeKeyBytesBy(key), getBytesOf(value)));
    }

    /**
     * ZSet操作，获取key下内容对应排名
     * @param key key
     * @param value value
     * @return 排名
     */
    private static Integer zRank(String key, String value) {
        return redisTemplate.execute((RedisCallback<Integer>) connection -> Objects.requireNonNull(connection.zRank(makeKeyBytesBy(key), getBytesOf(value))).intValue());
    }

    /**
     * 将byte[]字节流转化为{@code clazz}类型对象
     * @param bytes 字节流
     * @param clazz 类型
     * @return {@code clazz}类型对象
     * @param <T> 泛型
     */
    private static <T> T toObject(byte[] bytes, Class<T> clazz){
        if (bytes == null) {
            return null;
        }

        if (clazz == String.class){
            return (T) new String(bytes, CHARSET_UTF_8);
        }

        return JsonUtil.toObject(new String(bytes, CHARSET_UTF_8), clazz);
    }

    /**
     * 将传入内容转换为字节流
     * @param value 内容
     * @return 字节流
     * @param <T> 泛型
     */
    private static <T> byte[] getBytesOf(T value){
        if (value instanceof String){
            return ((String) value).getBytes(CHARSET_UTF_8);
        } else {
            return JsonUtil.toJsonString(value).getBytes(CHARSET_UTF_8);
        }
    }

    /**
     * 将传入的key做一定处理生成本应用专属的key，并生成字节流
     * @param key key
     * @return 本应用专属的key
     */
    private static byte[] makeKeyBytesBy(String key){
        checkNull(key);
        key = KEY_PREFIX + key;
        return key.getBytes(CHARSET_UTF_8);
    }

    /**
     * 检查输入对象流是否存在空对象
     * @param args 对象流
     */
    private static void checkNull(Object... args){
        for (Object obj: args){
            if (obj == null){
                throw new IllegalArgumentException("redis argument can not be null");
            }
        }
    }
}
