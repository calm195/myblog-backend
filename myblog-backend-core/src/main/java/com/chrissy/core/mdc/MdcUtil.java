package com.chrissy.core.mdc;

import org.slf4j.MDC;

/**
 * @author chrissy
 * @description Mdc工具类
 * @date 2024/7/30 21:19
 */
public class MdcUtil {
    public static final String TRACE_ID_KEY = "traceId";

    /**
     * 加入键值对
     * @param key key
     * @param val value
     */
    public static void add(String key, String val) {
        MDC.put(key, val);
    }

    /**
     * 加入TraceID
     */
    public static void addTraceId() {
        MDC.put(TRACE_ID_KEY, TraceIdGenerator.generateId());
    }

    /**
     * 获取TraceID
     * @return 自定义自增TraceID
     */
    public static String getTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }

    /**
     * 重置当前Trace ID下的MDC <p>
     * 清空MDC，保留Trace ID Key: Trace ID
     */
    public static void reset() {
        String traceId = MDC.get(TRACE_ID_KEY);
        MDC.clear();
        MDC.put(TRACE_ID_KEY, traceId);
    }

    /**
     * 清空MDC
     */
    public static void clear() {
        MDC.clear();
    }
}
