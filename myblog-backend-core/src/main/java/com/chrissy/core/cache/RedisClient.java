package com.chrissy.core.cache;

import com.chrissy.core.util.JsonUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.dao.DataAccessException;
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

    public static void registerRedisTemplate(RedisTemplate<String, String> redisTemplate){
        RedisClient.redisTemplate = redisTemplate;
    }

    public static boolean setStr(String key, String value){
        return Boolean.TRUE.equals(redisTemplate.execute((RedisCallback<Boolean>) con -> con.set(makeKeyBytesBy(key), getBytesOf(value))));
    }

    public static String getStr(String key){
        return redisTemplate.execute((RedisCallback<String>) con -> {
            byte[] value = con.get(makeKeyBytesBy(key));
            return value == null ? null : new String(value);
        });
    }

    // TODO: 使删除结果可知
    public static void del(String key){
        redisTemplate.execute((RedisCallback<Long>) con -> con.del(makeKeyBytesBy(key)));
    }

    public static boolean setExpire(String key, Long expire){
        return Boolean.TRUE.equals(redisTemplate.execute((RedisCallback<Boolean>) con -> con.expire(makeKeyBytesBy(key), expire)));
    }

    public static boolean setValueWithExpire(String key, String value, Long expire) {
        return Boolean.TRUE.equals(redisTemplate.execute((RedisCallback<Boolean>) con -> con.setEx(makeKeyBytesBy(key), expire, getBytesOf(value))));
    }

    public static Long getTtl(String key){
        return redisTemplate.execute((RedisCallback<Long>) con -> con.ttl(makeKeyBytesBy(key)));
    }

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

    public static <T> T hGet(String key, String field, Class<T> clazz){
        return redisTemplate.execute((RedisCallback<T>) con -> {
           byte[] record = con.hGet(makeKeyBytesBy(key), getBytesOf(field));
           if (record == null){
               return null;
           }
           return toObject(record, clazz);
        });
    }

    public static Long hIncrBy(String key, String field, Integer number){
        return redisTemplate.execute((RedisCallback<Long>) con -> con.hIncrBy(makeKeyBytesBy(key), getBytesOf(field), number));
    }

    public static Boolean hDel(String key, String field){
        return redisTemplate.execute((RedisCallback<Boolean>) con -> Objects.requireNonNull(con.hDel(makeKeyBytesBy(key), getBytesOf(field))) > 0);
    }

    public static <T> Boolean hSet(String key, String field, T value){
        return redisTemplate.execute((RedisCallback<Boolean>) con -> con.hSet(makeKeyBytesBy(key), getBytesOf(field), getBytesOf(value)));
    }

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

    public static <T> Boolean sIsMember(String key, T value) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.sIsMember(makeKeyBytesBy(key), getBytesOf(value)));
    }

    public static <T> Set<T> sGetAll(String key, Class<T> clz) {
        return redisTemplate.execute((RedisCallback<Set<T>>) connection -> {
            Set<byte[]> set = connection.sMembers(makeKeyBytesBy(key));
            if (CollectionUtils.isEmpty(set)) {
                return Collections.emptySet();
            }
            return set.stream().map(s -> toObject(s, clz)).collect(Collectors.toSet());
        });
    }

    public static <T> boolean sPut(String key, T val) {
        Long res =  redisTemplate.execute((RedisCallback<Long>) con -> con.sAdd(makeKeyBytesBy(key), getBytesOf(val)));
        return res != null && res > 0;
    }

    public static <T> void sDel(String key, T val) {
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.sRem(makeKeyBytesBy(key), getBytesOf(val));
            return null;
        });
    }

    public static Double zIncrBy(String key, String value, Integer score) {
        return redisTemplate.execute((RedisCallback<Double>) connection -> connection.zIncrBy(makeKeyBytesBy(key), score, getBytesOf(value)));
    }

    public static ImmutablePair<Integer, Double> zRankInfo(String key, String value) {
        double score = zScore(key, value);
        int rank = zRank(key, value);
        return ImmutablePair.of(rank, score);
    }

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

    public static <T> Long lPush(String key, T val) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> connection.lPush(makeKeyBytesBy(key), getBytesOf(val)));
    }

    public static <T> Long rPush(String key, T val) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> connection.rPush(makeKeyBytesBy(key), getBytesOf(val)));
    }

    public static <T> List<T> lRange(String key, int start, int size, Class<T> clz) {
        return redisTemplate.execute((RedisCallback<List<T>>) connection -> {
            List<byte[]> list = connection.lRange(makeKeyBytesBy(key), start, size);
            if (CollectionUtils.isEmpty(list)) {
                return new ArrayList<>();
            }
            return list.stream().map(k -> toObject(k, clz)).collect(Collectors.toList());
        });
    }

    public static void lTrim(String key, int start, int size) {
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.lTrim(makeKeyBytesBy(key), start, size);
            return null;
        });
    }

    public static PipelineAction getPipelineAction(){
        return new PipelineAction();
    }

    public static class PipelineAction {
        private final List<Runnable> actions = new ArrayList<>();
        private RedisConnection redisConnection;

        public PipelineAction add(String key, BiConsumer<RedisConnection, byte[]> con){
            actions.add(() -> con.accept(redisConnection, makeKeyBytesBy(key)));
            return this;
        }

        public PipelineAction add(String key, String field, ThreeConsumer<RedisConnection, byte[], byte[]> con){
            actions.add(() -> con.accept(redisConnection, makeKeyBytesBy(key), getBytesOf(field)));
            return this;
        }

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

    private static Double zScore(String key, String value) {
        return redisTemplate.execute((RedisCallback<Double>) connection -> connection.zScore(makeKeyBytesBy(key), getBytesOf(value)));
    }

    private static Integer zRank(String key, String value) {
        return redisTemplate.execute((RedisCallback<Integer>) connection -> Objects.requireNonNull(connection.zRank(makeKeyBytesBy(key), getBytesOf(value))).intValue());
    }

    private static <T> T toObject(byte[] bytes, Class<T> clazz){
        if (bytes == null) {
            return null;
        }

        if (clazz == String.class){
            return (T) new String(bytes, CHARSET_UTF_8);
        }

        return JsonUtil.toObject(new String(bytes, CHARSET_UTF_8), clazz);
    }

    private static <T> byte[] getBytesOf(T value){
        if (value instanceof String){
            return ((String) value).getBytes(CHARSET_UTF_8);
        } else {
            return JsonUtil.toJsonString(value).getBytes(CHARSET_UTF_8);
        }
    }

    private static byte[] makeKeyBytesBy(String key){
        checkNull(key);
        key = KEY_PREFIX + key;
        return key.getBytes(CHARSET_UTF_8);
    }

    private static void checkNull(Object... args){
        for (Object obj: args){
            if (obj == null){
                throw new IllegalArgumentException("redis argument can not be null");
            }
        }
    }
}
