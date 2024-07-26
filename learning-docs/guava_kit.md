# guava

简介：Guava是Google提供的一个Java库，包含了很多Google核心库的Java实现，包括集合、缓存、原生类型支持、并发库、通用注解、字符串处理、I/O等等。

## 超时限制器

1. TimeLimiter：超时限制器接口。有两个方法：
    - `T callWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit, boolean amInterruptible)`：执行一个方法，限制方法的执行时间。
    - `T newProxy(T target, Class<T> interfaceType, long timeoutDuration, TimeUnit timeoutUnit)`：创建一个代理对象，限制方法的执行时间。
2. SimpleTimeLimiter：实现了TimeLimiter接口，内部使用Future对象实现了超时限制。
    - `SimpleTimeLimiter.create(ExecutorService executor)`：创建一个SimpleTimeLimiter对象。

    ```java
    SimpleTimeLimiter simpleTimeLimiter = SimpleTimeLimiter.create(Executors.newCachedThreadPool());
    Callable<String> callable = () -> {
        Thread.sleep(1000);
        return "Hello World!";
    };
    String result = simpleTimeLimiter.callWithTimeout(callable, 500, TimeUnit.MILLISECONDS, true);
    System.out.println(result); // java.util.concurrent.TimeoutException
    ```

3. FakeTimeLimiter：实现了TimeLimiter接口，不会限制方法的执行时间，用于debug和测试。
