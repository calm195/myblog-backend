# Caffein

[**Caffeine**](https://github.com/ben-manes/caffeine/)：高性能缓存库，本地缓存。是使用java8对Guava的重写版本

内置功能：

1. 可以写入外部缓存
   1. `write(String key, Object value)`：写入外部缓存
   2. `delete(String key, Object value, RemovalCause cause)`：删除外部缓存
2. 数据收集功能
   1. 打开统计功能：`recordStats()`
   2. 获取统计数据：`stats()`

## 淘汰算法

1. FIFO：先进先出
2. LRU：最近最少使用
3. LFU：最少使用
4. W-TinyLFU：最少使用，在最近使用的对象中根据使用频率进行淘汰

## Cache

提供了四种Cache类型对应四种加载策略。

四种加载策略

1. Cache：手动加载
    - 需要手动调用`get`方法加载数据，如果对应的key已存在，那么会覆盖原有内容
    - 调用`getIfPresent`方法可以获取缓存中的数据，如果不存在则返回null；如果有线程在更新对应缓存，会立即返回null不会阻塞。
    - 使用`get(key, k -> value)`方法可以实现自动加载，如果key不存在，会调用value方法加载数据，本方法避免写入竞争。
2. LoadingCache：自动加载
    - 当缓存不存在或者过期时，如果调用`get`方法，会自动调用`load`方法加载数据；如果调用`getAll`方法，则会遍历所有key调用`load`方法加载数据（如果实现了`loadAll`方法，会调用`loadAll`方法加载数据）。
    - 需要指定`CacheLoader`对象，实现`load`方法加载数据。
3. AsyncCache：异步加载
    - 如果多线程下调用`get(key, k -> value)`方法，则会返回同一个`CompletableFuture`对象。
4. AsyncLoadingCache：自动异步加载
    - 以异步的方式自动加载数据

三种回收策略

1. 基于大小回收：`maximumSize`、`maximumWeight`，两者不能同时使用
   1. 基于缓存大小
   2. 基于权重大小
2. 基于时间回收：三种定时驱逐策略
   1. `expireAfterAccess(long, TimeUnit)`：最后一次访问后指定时间过期
   2. `expireAfterWrite(long, TimeUnit)`：最后一次写入后指定时间过期
   3. `expireAfter(Expiry)`：自定义过期策略，过期时间由Expiry实现决定
3. 基于引用回收
    1. `weakKeys()`：弱引用key
    2. `weakValues()`：弱引用value
    3. `softValues()`：软引用value
    - AsyncLoadingCache不支持弱引用和软引用
    - weakValues和softValues不可以同时使用

## Caffeine的使用

Caffeine使用了建造者模式，有以下配置参数：

| 参数 | 说明 |
| :--- | --- |
| `initialCapacity` | 初始容量 |
| `maximumSize` | 最大容量，达到这个大小时开始清除缓存 |
| `maximumWeight` | 最大权重，同时需要定义一个`Weigher<K,V>`计算权重 |
| `weigher` | 计算权重的方法 |
| `expireAfterAccess` | 最后一次访问后过期时间 |
| `expireAfterWrite` | 最后一次写入后过期时间 |
| `refreshAfterWrite` | 最后一次写入后刷新时间 |
| `weakKeys` | 弱引用key |
| `weakValues` | 弱引用value |
| `softValues` | 软引用value |
| `recordStats` | 打开统计功能 |
| `removalListener` | 缓存淘汰监听器，缓存被淘汰时将会触发 |

1. 构造器
    - `Caffeine.newBuilder()`：创建一个新的Caffeine对象
    - `Caffeine.from(String spec)`：从spec设置中创建一个新的Caffeine对象
    - `Caffeine.from(CaffeineSpec spec)`：从CaffeineSpec对象中创建一个新的Caffeine对象
2. 注解

[TODO](https://www.cnblogs.com/booksea/p/17715810.html)
