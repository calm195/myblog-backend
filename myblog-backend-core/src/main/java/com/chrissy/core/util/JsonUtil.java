package com.chrissy.core.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author chrissy
 * @description json 转化为特定对象或者字符串的工具类
 * @date 2024/7/23 21:08
 */
public class JsonUtil {
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    /**
     * 把传入的json字符串转化为<code>classType</code>类型
     * @param str 应为Json字符串
     * @param classType 需要转化的目标类型
     * @return 返回实例化的对象
     * @param <T> 泛型
     */
    public static <T> T toObject(String str, Class<T> classType){
        try {
            return JSON_MAPPER.readValue(str, classType);
        } catch (Exception ex){
            throw new UnsupportedOperationException(ex);
        }
    }

    /**
     * 将输入的实体对象json化后，得到json字符串
     * @param object 输入的实体对象
     * @return 返回json字符串
     * @param <T> 泛型
     */
    public static <T> String toJsonString(T object){
        try {
            return JSON_MAPPER.writeValueAsString(object);
        } catch (Exception ex){
            throw new UnsupportedOperationException(ex);
        }
    }

    /**
     * 创建一个涵盖
     * <p>long, Long. long[], Long[], BigDecimal, BigDecimal[], BigInteger, BigInteger[]</p>
     * 转化为string的序列器
     * @return `SimpleModule`实例对象
     */
    public static SimpleModule  newBigIntToStringSimpleModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, newSerializer(String::valueOf));
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(long[].class, newSerializer((Function<Long, String>) String::valueOf));
        simpleModule.addSerializer(Long[].class, newSerializer((Function<Long, String>) String::valueOf));
        simpleModule.addSerializer(BigDecimal.class, newSerializer(BigDecimal::toString));
        simpleModule.addSerializer(BigDecimal[].class, newSerializer(BigDecimal::toString));
        simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
        simpleModule.addSerializer(BigInteger[].class, newSerializer((Function<BigInteger, String>) BigInteger::toString));
        return simpleModule;
    }

    /**
     * 生成json序列器
     * @param func 生成方法
     * @return 返回序列器实例
     * @param <T> 目标类型
     * @param <K> 实际接收数据
     */
    public static <T, K> JsonSerializer<T> newSerializer(Function<K, String> func) {
        return new JsonSerializer<T>() {
            @Override
            public void serialize(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                if (t == null) {
                    jsonGenerator.writeNull();
                    return;
                }

                if (t.getClass().isArray()) {
                    jsonGenerator.writeStartArray();
                    Stream.of(t).forEach(s -> {
                        try {
                            jsonGenerator.writeString(func.apply((K) s));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    jsonGenerator.writeEndArray();
                } else {
                    jsonGenerator.writeString(func.apply((K) t));
                }
            }
        };
    }
}
