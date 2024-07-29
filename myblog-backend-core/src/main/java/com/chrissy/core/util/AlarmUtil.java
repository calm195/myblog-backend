package com.chrissy.core.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * @author chrissy
 * @description 报警工具类
 * @date 2024/7/29 8:55
 */
public class AlarmUtil extends AppenderBase<ILoggingEvent> {
    private static final long INTERVAL = 10 * 1000 * 60;
    private long lastAlarmTime = 0;

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        if (canAlarm()) {
            EmailUtil.sendMail(iLoggingEvent.getLoggerName(),
                    SpringUtil.getConfig("alarm.user", "1804659599@qq.com"),
                    iLoggingEvent.getFormattedMessage());
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
            return false;
        }
    }
}
