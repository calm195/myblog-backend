package com.chrissy.core.mdc;

import com.chrissy.core.util.IpUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author chrissy
 * @description 自定义TraceId 生成器
 * @date 2024/7/30 21:20
 */
@Slf4j
public class TraceIdGenerator {
    private final static Integer MIN_INCR_NUMBER = 1000;
    private final static Integer MAX_INCR_NUMBER = 10000;
    private final static  AtomicInteger AUTO_INCREASE_NUMBER = new AtomicInteger(MIN_INCR_NUMBER);

    /**
     * 生成32位traceId，规则是 服务器 IP + 产生ID时的时间 + 当前进程号 + 自增序列 <p>
     * IP 8位：39.105.208.175 -> 2769d0af (16)<p>
     * 产生ID时的时间 13位： 毫秒时间戳 -> 1403169275002<p>
     * 当前进程号 5位： PID<p>
     * 自增序列 4位： 1000-9999循环
     *
     * @return ac13e001.1685348263825.095001000
     */
    public static String generateId() {
        StringBuilder traceId = new StringBuilder();
        try {
            // 1. IP - 8
            traceId.append(convertIp(IpUtil.getLocalIp4Address())).append(".");
            // 2. 时间戳 - 13
            traceId.append(Instant.now().toEpochMilli()).append(".");
            // 3. 当前进程号 - 5
            traceId.append(getProcessId());
            // 4. 自增序列 - 4
            traceId.append(getAutoIncreaseNumber());
        } catch (Exception e) {
            log.error("generate trace id error!", e);
            log.info("generate id by UUID.randomUUID()");
            return UUID.randomUUID().toString().replaceAll("-", "");
        }
        return traceId.toString();
    }

    /**
     * IP转换为十六进制 - 8位
     *
     * @param ip 39.105.208.175
     * @return 2769d0af
     */
    private static String convertIp(String ip) {
        return Arrays.stream(ip.split("\\.")).map(s -> String.format("%02x", Integer.valueOf(s))).collect(Collectors.joining());
    }

    /**
     * 使得自增序列在1000-9999之间循环  - 4位
     *
     * @return 自增序列号
     */
    private static int getAutoIncreaseNumber() {
        if (AUTO_INCREASE_NUMBER.get() >= MAX_INCR_NUMBER) {
            AUTO_INCREASE_NUMBER.set(MIN_INCR_NUMBER);
            return AUTO_INCREASE_NUMBER.get();
        } else {
            return AUTO_INCREASE_NUMBER.incrementAndGet();
        }
    }

    /**
     * 获取当前进程号
     * @return 5位当前进程号
     */
    private static String getProcessId() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String processId = runtime.getName().split("@")[0];
        return String.format("%05d", Integer.parseInt(processId));
    }
}
