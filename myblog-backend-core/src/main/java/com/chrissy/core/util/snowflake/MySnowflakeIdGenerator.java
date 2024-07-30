package com.chrissy.core.util.snowflake;

import com.chrissy.core.async.AsyncUtil;
import com.chrissy.core.util.IpUtil;
import com.chrissy.core.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

/**
 * @author chrissy
 * @description 基于雪花算法的id生成器* <p>
 *  0 + 时间(41) + 数据中心(3位) + 机器id(7位) + 序列号(12位)
 * @date 2024/7/30 14:33
 */
@Slf4j
public class MySnowflakeIdGenerator implements IdGenerator{
    private static final long SEQUENCE_BITS = 12L;
    private static final long WORKER_ID_BITS = 7L;
    private static final long DATA_CENTER_BITS = 3L;
    private static final long SEQUENCE_MASK = (1 << SEQUENCE_BITS) - 1;

    private static final long WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;
    private static final long DATACENTER_LEFT_SHIFT_BITS = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT_BITS = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_BITS;

    private long workId;
    private long dataCenter;

    private long lastTime;
    private long sequence;
    private byte sequenceOffset;

    /**
     * 获取ip，根据ip生成数据中心+机器码。
     */
    public MySnowflakeIdGenerator(){
        try {
            String ip = IpUtil.getLocalIp4Address();
            String[] cells = StringUtils.split(ip, ".");
            this.dataCenter = Integer.parseInt(cells[0]) & ((1 << DATA_CENTER_BITS) - 1);
            this.workId = Integer.parseInt(cells[3]) >> 16 & ((1 << WORKER_ID_BITS) - 1);
        } catch (Exception e) {
            this.dataCenter = 1;
            this.workId = 1;
        }
    }

    /**
     * 生成全局唯一id
     * @return 唯一id
     */
    @Override
    public Long nextId() {
        long nowTime = waitToIncrDiffIfNeed(getNowTimeInSecond());
        if (lastTime == nowTime) {
            if (0L == (sequence = (sequence + 1) & SEQUENCE_MASK)) {
                // 表示当前这一时刻的自增数被用完了，等待下一时间点；如果没用完则直接使用
                nowTime = waitUntilNextTime(nowTime);
            }
        } else {
            // 上一毫秒若以0作为序列号开始值，则这一秒以1为序列号开始值
            vibrateSequenceOffset();
            sequence = sequenceOffset;
        }
        lastTime = nowTime;
        long ans = ((nowTime % DateUtil.ONE_DAY_SECONDS) << TIMESTAMP_LEFT_SHIFT_BITS)
                | (dataCenter << DATACENTER_LEFT_SHIFT_BITS)
                | (workId << WORKER_ID_LEFT_SHIFT_BITS)
                | sequence;
        if (log.isDebugEnabled()) {
            log.debug("seconds:{}, datacenter:{}, work:{}, seq:{}, ans={}", nowTime % DateUtil.ONE_DAY_SECONDS, dataCenter, workId, sequence, ans);
        }
        return Long.parseLong(String.format("%s%011d", getDaySegment(nowTime), ans));
    }

    /**
     * 若当前时间比上次执行时间要小，则等待时间追上来，避免出现时钟回拨导致的数据重复
     * @param nowTime 当前时间戳，秒级
     * @return 返回新的时间戳
     */
    private long waitToIncrDiffIfNeed(final long nowTime) {
        if (lastTime <= nowTime) {
            return nowTime;
        }
        long diff = lastTime - nowTime;
        AsyncUtil.sleep(diff);
        return getNowTimeInSecond();
    }

    /**
     * 等待下一秒
     * @param lastTime 上次的时间戳，秒级
     * @return 上次时间的下一秒
     */
    private long waitUntilNextTime(final long lastTime) {
        long result = getNowTimeInSecond();
        while (result <= lastTime) {
            result = getNowTimeInSecond();
        }
        return result;
    }

    /**
     * 将offset置为1
     */
    private void vibrateSequenceOffset() {
        sequenceOffset = (byte) (~sequenceOffset & 1);
    }

    /**
     * 将年份和一年中的第几天组合成字符串，如，24001，24年第一天
     * @param time 当前时间戳
     * @return 目标字符串
     */
    private static String getDaySegment(long time) {
        LocalDateTime localDate = DateUtil.timestampToLocalDateTime(time * 1000L);
        return String.format("%02d%03d", localDate.getYear() % 100, localDate.getDayOfYear());
    }

    /**
     * 获取当前时间戳，秒级
     * @return 当前时间戳
     */
    private long getNowTimeInSecond() {
        return System.currentTimeMillis() / 1000;
    }
}
