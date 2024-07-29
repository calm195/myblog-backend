package com.chrissy.core.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chrissy
 * @description 报警工具类
 * @date 2024/7/29 8:55
 */
@Slf4j
public class AlarmUtil extends AppenderBase<ILoggingEvent> {
    private static final long INTERVAL = 10 * 1000 * 60;
    private long lastAlarmTime = 0;

    /**
     * 如果条件符合，将报警信息发送给默认邮箱
     * @param iLoggingEvent logback-classic模块处理事件的接口实现
     */
    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        if (canAlarm()) {
            if (EmailUtil.sendMail(iLoggingEvent.getLoggerName(),
                    SpringUtil.getConfig("alarm.user", "1804659599@qq.com"),
                    iLoggingEvent.getFormattedMessage())){
                log.info("alarm has happened, sent to {}", SpringUtil.getConfig("alarm.user", "1804659599@qq.com"));
            }
        }
    }

    /**
     * 是否可以发送报警，使用简单的时间限制：一条报警 / 1 min
     * @return 是否符合条件
     */
    private boolean canAlarm() {
        long now = System.currentTimeMillis();
        if (now - lastAlarmTime >= INTERVAL) {
            lastAlarmTime = now;
            return true;
        } else {
            log.info("alarm has happened, but more than one time per minutes");
            return false;
        }
    }
}
