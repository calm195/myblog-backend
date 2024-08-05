# Java Management

JDK的java.lang.management包提供了管理接口，用于监控管理JVM及操作系统。如上图所示，JVM有众多监控项，根据JMX规范，每个监控项用MXBean表示，应用程序通过访问MXBean接口获取监控数据。

## 访问方式

1. 直接访问
    通过静态工厂方法ManagementFactory.getXXXMXBean()获取MXBean实例，然后调用其方法获取监控数据。
2. 间接访问
    通过MBeanServer访问：其他程序需要通过特定的MBeanServerConnection接口访问MXBean，这种方式适用于远程访问。

## MXBean类型

1. JVM信息
    - ClassLoadingMXBean
    - CompilationMXBean
    - RuntimeMXBean
      - 获取对象：`RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();`
      - 获取当前进程ID：`runtimeMXBean.getName().split("@")[0]`
      - 获取当前进程Name：`runtimeMXBean.getName()`
2. 内存管理
    - MemoryMXBean
    - MemoryManagerMXBean
    - MemoryPoolMXBean
3. GC
    - GarbageCollectorMXBean
4. 线程
    - ThreadMXBean
