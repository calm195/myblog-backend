# Spring StopWatch

StopWatch是一个轻量级的秒表，允许多个任务的计时，暴露每个命名任务的总运行时间和运行时间。StopWatch并非线程安全，并且未使用同步修饰。从Spring 5.2开始以纳秒为单位进行统计。
> 一个StopWatch实例一次只能开启一个task任务，不能同时start多个task，并且在该task未stop之前不能start一个新的task，必须在该task stop之后才能开启新的task，若要一次开启多个，需要new不同的StopWatch实例。

常用方法如下：

| 方法 | 描述 |
| --- | --- |
| `StopWatch()` | 创建一个新的StopWatch实例 |
| `StopWatch(String id)` | 创建一个新的StopWatch实例，并设置id |
| `void start()` | 开始计时 |
| `void start(String taskName)` | 开始计时特定的任务 |
| `void stop()` | 停止计时 |
| `boolean isRunning()` | 判断是否有计时任务正在运行 |
| `void setKeepTaskList(boolean keepTaskList)` | 设置是否保留任务列表 |
| `long getTotalTimeMillis()` | 获取所有任务的总运行时间（毫秒） |
| `long getTotalTimeSeconds()` | 获取所有任务的总运行时间（纳秒） |
| `long getLastTaskTimeMillis()` | 获取最后一个任务的运行时间（毫秒） |
| `long getLastTaskTimeNanos()` | 获取最后一个任务的运行时间（纳秒） |
| `String prettyPrint()` | 格式化打印所有已执行任务的信息 |
| `String prettyPrint(TimeUnit timeUnit)` | 格式化打印所有已执行任务的信息，指定时间单位 |
