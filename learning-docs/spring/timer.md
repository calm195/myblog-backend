# Spring Timer

Spring定时任务

1. `@Schedule`
    用在public方法上，
    - `cron`：cron表达式，用来定义时间
        > 秒 分 时 日 月 周 年(可选)
        > 例如：`0 0 12 * * ?`表示每天中午12点执行
    - `fixedDelay`：固定延迟时间，等待指定的时间后执行
    - `fixedRate`：固定速率时间，每隔指定的时间执行
    - `initialDelay`：初始延迟时间
    - `zone`：时区
    - `enabled`：是否启用，默认为true
