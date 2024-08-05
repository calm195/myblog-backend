# Spring Redis

Redis 是一个开源的内存数据库，可以存储键值对数据，支持多种数据结构，如字符串、列表、集合、有序集合、哈希表等。由于数据存储在内存中，读写速度非常快，适合做缓存、计数器、消息队列等。数据量不能太大，一般用于存储热点数据。使用C语言编写。

Jedis 是 Redis 官方推荐的 Java 客户端，提供了丰富的 API，支持连接池、分布式、哨兵、集群等特性。

Spring-data-redis是spring大家族的一部分，提供了在srping应用中通过简单的配置访问redis服务，对reids底层开发包(Jedis,  JRedis, and RJC)进行了高度封装，RedisTemplate提供了redis各种操作、异常处理及序列化，支持发布订阅。
spring-data-redis针对jedis提供了如下功能：

1. 连接池自动管理，提供了一个高度封装的“RedisTemplate”类
   针对jedis客户端中大量api进行了归类封装,将同一类型操作封装为operation接口
    - ValueOperations：简单K-V操作
    - SetOperations：set类型数据操作
    - ZSetOperations：zset类型数据操作
    - HashOperations：针对map类型的数据操作
    - ListOperations：针对list类型的数据操作
2. 提供了对key的“bound”(绑定)便捷化操作API，可以通过bound封装指定的key，然后进行一系列的操作而无须“显式”的再次指定Key，即BoundKeyOperations：
    - BoundValueOperations：针对String类型的数据操作
    - BoundSetOperations：针对set类型的数据操作
    - BoundListOperations：针对list类型的数据操作
    - BoundHashOperations：针对map类型的数据操作
3. 将事务操作封装，有容器控制。
    - 针对数据的“序列化/反序列化”，提供了多种可选择策略(RedisSerializer)
    - JdkSerializationRedisSerializer：POJO对象的存取场景，使用JDK本身序列化机制，将pojo类通过ObjectInputStream/ObjectOutputStream进行序列化操作，最终redis-server中将存储字节序列。是目前最常用的序列化策略。
    StringRedisSerializer：Key或者value为字符串的场景，根据指定的charset对数据的字节序列编码成string，是“new String(bytes, charset)”和“string.getBytes(charset)”的直接封装。是最轻量级和高效的策略。
    - JacksonJsonRedisSerializer：jackson-json工具提供了javabean与json之间的转换能力，可以将pojo实例序列化成json格式存储在redis中，也可以将json格式的数据转换成pojo实例。因为jackson工具在序列化和反序列化时，需要明确指定Class类型，因此此策略封装起来稍微复杂。【需要jackson-mapper-asl工具支持】

## maven依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

## 配置

```yaml
spring:
  redis:
    host:
    port:
    password:
    database:
    timeout:
    jedis:
      pool:
        max-active:
        max-wait:
        max-idle:
        min-idle:
```

## 常用API

1. RedisConnection
    封装了Redis各数据类型的增删改查操作(以下copilot生成的，没详细考证)
    String类型：set、get、del、exists、expire、incr、decr
    Hash类型：hset、hget、hdel、hexists、hkeys、hvals、hgetall
    List类型：lpush、rpush、lpop、rpop、lrange、lindex、lrem
    Set类型：sadd、srem、sismember、smembers、scard
    ZSet类型：zAdd、zRem、zRange、zRangeByScore、zCard

2. RedisTemplate
    execute()类型的方法内部时通过RedisCallback接口来实现的，RedisCallback接口是一个函数式接口，只有一个方法doInRedis()，该方法接收一个RedisConnection对象，通过该对象来执行Redis命令。
    1. execute()：执行Redis命令
       1. 函数签名：`<T> T execute(RedisCallback<T> action)`
       2. 参数：`RedisCallback`接口，泛型T为返回值类型
       3. 可以接收lambda表达式作为参数
       4. 用法：`redisTemplate.execute((RedisCallback<Object>) connection -> connection.set("key".getBytes(), "value".getBytes()));`
    2. executePipelined()：执行Redis管道命令
       1. 函数签名：`List<Object> executePipelined(RedisCallback<?> action)`
       2. 参数：`RedisCallback`接口
       3. 可以接收lambda表达式作为参数

3. StringRedisTemplate

    ```java
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }
    ```
