# java 多线程

线程池的组成：

1. 线程池管理器（ThreadPool）：用于创建并管理线程池，包括 创建线程池，销毁线程池，添加新任务；
2. 工作线程（PoolWorker）：线程池中线程，在没有任务时处于等待状态，可以循环的执行任务；
3. 任务接口（Task）：每个任务必须实现的接口，以供工作线程调度任务的执行，它主要规定了任务的入口，任务执行完后的收尾工作，任务的执行状态等；
4. 任务队列（taskQueue）：用于存放没有处理的任务。提供一种缓冲机制。

## ReentrantLock

ReentrantLock是一个可重入的互斥锁，它是Java提供的一种线程同步的工具。它可以保证多个线程互斥的访问共享资源，是一种比synchronized更加灵活的锁机制。
他有公平锁和非公平锁两种实现方式。默认实现为非公平锁。

1. 构造方法
    1. ReentrantLock()：创建一个ReentrantLock实例。
    2. ReentrantLock(boolean fair)：创建一个ReentrantLock实例，并指定是否为公平锁。
2. 常用方法
    1. lock()：获取锁。
    2. unlock()：释放锁。
    3. trylock()：尝试获取锁。
    4. lockInterruptibly()：获取锁，但允许被中断。

## 原子类

原子类是一种提供了原子操作的类，它可以保证线程安全。原子类位于java.util.concurrent.atomic包下，是Java提供的一种线程安全的工具。

| 类型 | 类名 | 说明 |
| :--- | :--- | :--- |
| 基本类型 | AtomicInteger | 提供了原子操作的整型变量。 |
| 基本类型 | AtomicLong | 提供了原子操作的长整型变量。 |
| 基本类型 | AtomicBoolean | 提供了原子操作的布尔变量。 |
| 引用类型 | AtomicReference | 提供了原子操作的引用类型变量。 |
| 数组类型 | AtomicIntegerArray | 提供了原子操作的整型数组。 |
| 数组类型 | AtomicLongArray | 提供了原子操作的长整型数组。 |
| 数组类型 | AtomicReferenceArray | 提供了原子操作的引用类型数组。 |
| ... | ... | ... |

1. 常用方法
    1. get()：获取当前值。
    2. set(int newvalue)：设置新值。
    3. getandset(int newvalue)：获取当前值，并设置新值。
    4. compareandset(int expect, int update)：比较并设置新值。
    5. incrementandget()：自增并获取新值。
    6. decrementandget()：自减并获取新值。
    7. addandget(int delta)：增加并获取新值。
    8. getandincrement()：获取当前值，并自增。
    9. getanddecrement()：获取当前值，并自减。
    10. getandadd(int delta)：获取当前值，并增加。
2. 将普通变量转换为原子变量
   1. 基本类型
        1. atomicintegerfieldupdater：将普通变量转换为原子整型变量。
        2. atomiclongfieldupdater：将普通变量转换为原子长整型变量。
        3. atomicbooleanfieldupdater：将普通变量转换为原子布尔变量。
   2. 在类中  
        1. `atomicintegerfieldupdater<T>`：`atomicIntegerFieldUpdater<T> atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(xxx.class, fieldName);`
3. 注意事项
    1. 原子类只能保证单次操作的原子性，不能保证多次操作的原子性。
    2. 原子类的实现原理是CAS（Compare And Swap）。
        > CAS是一种乐观锁技术，它包含三个操作数：内存值V、旧的预期值A、要修改的新值B。当且仅当V的值等于A时，CAS才会通过原子方式用新值B来更新V的值，否则不会执行任何操作。
    3. 原子类的实现原理是Unsafe类。
        > Unsafe类是JDK中一个很重要的类，它提供了硬件级别的原子操作，可以直接操作内存数据，是Java的核心类库中的一个类。
    4. 高度并发的情况下，LongAdder(累加器)比AtomicLong原子操作效率更高
        > 在高度并发竞争情形下，AtomicLong每次进行add都需要flush和refresh（所有变量操作只能在工作内存中进行，然后写回主内存，其它线程再次读取新值），每次add()都需要同步，在高并发时会有比较多冲突，比较耗时导致效率低；而LongAdder中每个线程会维护自己的一个计数器，在最后执行LongAdder.sum()方法时候才需要同步，把所有计数器全部加起来，不需要flush和refresh操作。

## Fulture

Future是一个接口，它用来表示异步计算的结果。它提供了检查计算是否完成的方法，以等待计算的完成，并获取计算的结果。

1. 内置方法
    1. cancel(boolean mayInterruptIfRunning)：如果成功取消对此任务的执行，返回true。
    2. isCancelled()：如果任务在正常完成前被取消，则返回 true。
    3. isDone()：如果任务已完成，则返回 true。
    4. get()：等待计算完成，然后获取其结果。
    5. get(long timeout, TimeUnit unit)：指定时间内等待计算完成，然后获取其结果。
2. 局限性
    1. 不支持异步任务的编排组合。
    2. get()方法会阻塞当前线程，直到任务完成。
3. **CompletableFultrue**：
    - 定义：`public class CompletableFuture<T> implements Future<T>, CompletionStage<T> {}`
    - 优点：
        - 更强大好用的Future特性。
        - 支持异步任务的编排组合。
        - 提供函数式编程。
            - 来源于`CompletionStage`接口
    - 常见操作：
        - 创建CompletableFuture对象
            - `CompletableFuture<T> resultFuture = new CompletableFuture<>();`
            - `CompletableFuture<T> resultFuture = CompletableFuture.completedFuture(T value);`
            - `CompletableFuture<T> resultFuture = CompletableFuture.supplyAsync(Supplier<T> supplier);`
            - `CompletableFuture<T> resultFuture = CompletableFuture.runAsync(Runnable runnable);`
            - `CompletableFuture<T> resultFuture = CompletableFuture.supplyAsync(Supplier<T> supplier, Executor executor);`
            - `CompletableFuture<T> resultFuture = CompletableFuture.runAsync(Runnable runnable, Executor executor);`
        - 异步任务的编排组合
            - `thenApply(Function<? super T,? extends U> fn)`
            - `thenAccept(Consumer<? super T> action)`
            - `thenRun(Runnable action)`
            - `whenComplete(BiConsumer<? super T,? super Throwable> action)`
        - join：`T join()`
            - 等待异步任务完成，并返回结果。
        - allof：`CompletableFuture<Void> allOf(CompletableFuture<?>... cfs)`
            - 等待所有任务完成，返回一个已完成的CompletableFuture对象。
        - anyof：`CompletableFuture.anyOf(CompletableFuture<?>... cfs)`

## ThreadFactory

ThreadFactory是一个接口，它提供了创建线程的方法。

1. 内置方法
    - Thread newThread(Runnable r)：创建一个新的线程。

2. 实现类
    1. **Executors.DefaultThreadFactory**：一个线程工厂的实现类，它是Executors的一个内部类。
        - 特点：创建的线程都不是守护线程。
        - 创建线程的方法：`Thread newThread(Runnable r)`。
    2. **ThreadFactoryBuilder**：一个线程工厂的实现类，它是Guava提供的一个类。
        - 特点：可以自定义线程的名称、优先级、是否为守护线程。
        - 创建线程的方法：`new Thread(r, name)`。
        - 引入依赖

            > ```xml
            > <dependency>
            >     <groupId>com.google.guava</groupId>
            >     <artifactId>guava</artifactId>
            >     <version>30.1-jre</version>
            > </dependency>
            >   ```

        - 使用方法

            ```java
            ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("thread-%d")
                .setDaemon(true)
                .setPriority(Thread.MAX_PRIORITY)
                .build();
            ```

## ExecutorService

ExecutorService是一个接口，它是Executor的子接口，它提供了更丰富的线程池功能。  
它位于java.util.concurrent包下，是一个线程池的管理工具，可以很方便的创建线程池，执行任务。  

1. 常用方法
    1. execute(Runnable command)：执行一个任务
    2. submit(Runnable task)：提交一个Runnable任务用于执行，并返回一个表示该任务的Future实例。
    3. submit(Callable task)：提交一个Callable任务用于执行，并返回一个表示该任务的Future实例。
2. 具体实现类
    1. **ThreadPoolExecutor(重要)**：一个线程池的实现类，它继承自AbstractExecutorService类。
        > ThreadPoolExecutor是一个灵活的线程池实现，它提供了很多参数，可以灵活的配置线程池。

        ```java
        /**
         * 用给定的初始参数创建一个新的ThreadPoolExecutor。
         */
        public ThreadPoolExecutor(

        int corePoolSize,//线程池的核心线程数量
        int maximumPoolSize,//线程池的最大线程数
        long keepAliveTime,//当线程数大于核心线程数时，多余的空闲线程存活的最长时间
        TimeUnit unit,//时间单位
        BlockingQueue<Runnable> workQueue,//任务队列，用来储存等待执行任务的队列
        ThreadFactory threadFactory,//线程工厂，用来创建线程，一般默认即可
        RejectedExecutionHandler handler//拒绝策略，当提交的任务过多而不能及时处理时，我们可以定制策略来处理任务

        ) {
            if (corePoolSize < 0 ||
                maximumPoolSize <= 0 ||
                maximumPoolSize < corePoolSize ||
                keepAliveTime < 0)
                throw new IllegalArgumentException();
            if (workQueue == null || threadFactory == null || handler == null)
                throw new NullPointerException();
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.workQueue = workQueue;
            this.keepAliveTime = unit.toNanos(keepAliveTime);
            this.threadFactory = threadFactory;
            this.handler = handler;
        }
        ```

        - corePoolSize：核心线程数，任务队列未到达任务容量时，最大可以同时运行的线程数量。
        - maximumPoolSize：最大线程数，线程池中最大的线程数量。
        - workQueue：任务队列，用来存放等待执行的任务。
        - keepAliveTime：当线程数大于核心线程数时，多余的空闲线程存活的最长时间。
        - unit：时间单位。
        - threadFactory：线程工厂，用来创建线程。
        - handler：任务队列已满时，新任务到来时的拒绝策略。

        > 常见的拒绝策略
        > 1. AbortPolicy：抛出RejectedExecutionException异常，拒绝处理新任务。
        > 2. CallerRunsPolicy：直接在调用者线程中运行被拒绝的任务。会降低新任务提交速度。
        > 3. DiscardOldestPolicy：丢弃未处理任务队列中最旧的任务，尝试为当前任务腾出位置。
        > 4. DiscardPolicy **(默认)**：直接丢弃新任务。
    2. ScheduledThreadPoolExecutor：一个线程池的实现类，它继承自ThreadPoolExecutor类，实现了ScheduledExecutorService接口。

## 线程安全的集合结构

## ThreadLocal  

ThreadLocal是一个线程局部变量，它提供了线程的局部变量，每个线程都可以通过ThreadLocal对象来访问自己的局部变量。每个线程相互独立，各自拥有一样的局部变量副本。  
内部数据结构为ThreadLocalMap，它是一个Entry数组，每个Entry是一个键值对，键为ThreadLocal对象，值为线程的局部变量。  
ThreadLocal类内部存在一个类型为ThreadLocal.ThreadLocalMap的实例变量threadLocals。

1. ThreadLocalMap

    1. 结构：ThreadLocalMap是一个Entry数组。
        - Entry：一个键值对，键为ThreadLocal弱引用对象，值为线程的局部变量。
        - key: extends WeakReference<ThreadLocal<?>>。
    2. 线程隔离的实现：每个线程都有一个ThreadLocalMap对象，用来存放线程的局部变量。每个线程在往ThreadLocal里放值的时候，都会往自己的ThreadLocalMap里存，读也是以ThreadLocal作为引用，在自己的map里找对应的key，从而实现了线程隔离。

    3. 常用方法

        1. get()：获取当前线程的局部变量。
        2. set(T value)：设置当前线程的局部变量。

    4. 相关类

        1. InheritableThreadLocal：继承自ThreadLocal，它提供了一个特性，就是子线程可以继承父线程的局部变量。为了解决异步场景下，子线程无法获取父线程的局部变量的问题。但是仍然存在缺陷。
            > 缺陷：InheritableThreadLocal不支持线程池，因为它是在父线程创建子线程时复制的，而线程池中的线程是复用的，子线程只复制一次，这样子线程就无法获取到父线程的局部变量。
        2. TransmittableThreadLocal：继承自InheritableThreadLocal，解决了InheritableThreadLocal的缺陷，可以在异步场景下，子线程获取父线程的局部变量。
            1. 缺陷解决原理：在父线程向线程池提交任务时复制父线程的上下文环境即可。
            2. 引入依赖  

                > ```xml
                > <dependency>
                >     <groupId>com.alibaba</groupId>
                >     <artifactId>transmittable-thread-local</artifactId>
                >     <version>2.11.0</version>
                > </dependency>
                >   ```

            3. 使用方法

                ```java
                import com.alibaba.ttl.TransmittableThreadLocal;
                TransmittableThreadLocal<String> parent = new TransmittableThreadLocal<>();
                parent.set("parent");
                Runnable runnable = () -> {
                    System.out.println(parent.get());
                };
                new Thread(runnable).start();
                ```

## 零散的知识点

1. **线程池的状态**
    - RUNNING：接受新任务，并处理阻塞队列中的任务。
    - SHUTDOWN：不接受新任务，但处理阻塞队列中的任务。
    - STOP：不接受新任务，不处理阻塞队列中的任务，并中断正在执行的任务。
    - TIDYING：所有任务都已终止，workerCount为0，线程池的状态将会变为TIDYING。
    - TERMINATED：线程池彻底终止。

2. **线程池的关闭**
    - shutdown()：线程池状态变为SHUTDOWN，不再接受新任务，但会等待已经提交的任务执行完成。
    - shutdownNow()：线程池状态变为STOP，不再接受新任务，尝试终止正在执行的任务，并返回等待执行的任务列表。

3. **Runnable vs Callable**
    - Runnable接口是Java提供的一个接口，用来表示一个任务，它只有一个run方法，没有返回值，不抛出异常。
    - Callable接口也是Java提供的一个接口，用来表示一个任务，它只有一个call方法，有返回值，并且可以抛出异常。
    - Excutors类提供了一些静态方法，可以将Runnable对象转换为Callable对象。

    ```java
    Runnable runnable = () -> System.out.println("runnable");
    Callable<String> callable = Executors.callable(runnable, "callable");
    // or
    Callable<String> callable = Executors.callable(runnable);
    ```

4. **execute() vs submit()**
    - execute()方法用于提交一个Runnable任务，没有返回值。自身无法处理异常，需要通过TreadFactory捕获UncaughtException，或者ThreadPoolExecutor设置afterExecute()方法。
    - submit()方法用于提交一个Runnable或Callable任务，返回一个Future对象。自身可以处理异常。

5. **isTerminate() vs isShutdown()**
    - isShutdown()：调用shutdown()方法后返回为true。
    - isTerminate()：调用shutdown()方法后，并且所有提交的任务均已完成，返回为true。
