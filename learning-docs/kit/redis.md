# Redis

Redis 是一个开源的内存数据库，可以存储键值对数据，支持多种数据结构，如字符串、列表、集合、有序集合、哈希表等。由于数据存储在内存中，读写速度非常快，适合做缓存、计数器、消息队列等。数据量不能太大，一般用于存储热点数据。使用C语言编写。

## 数据结构

### 内置数据结构

1. SDS：简单动态字符串，是Redis的字符串实现，比C语言的字符串更加安全和高效。

    > ```c
    > struct sdshdr {
    >     int len; // 字符串长度
    >     int free; // 空闲空间长度
    >     char buf[]; // 字符串数据
    > };
    >```

2. Linked List：双向链表，是Redis的列表实现，支持快速插入和删除。

    > ```c
    > struct listNode {
    >     struct listNode *prev; // 前一个节点
    >     struct listNode *next; // 后一个节点
    >     void *value; // 节点数据
    > };
    > ```

    > ```c
    > struct list {
    >     listNode *head; // 头节点
    >     listNode *tail; // 尾节点
    >     unsigned long len; // 节点数量
    >     void *dup(void* ); // 复制函数
    >     void *free(void* ); // 释放函数
    >     void *match(void* , void*); // 比较函数
    > };
    > ```

    - 缺点
      - 额外的内存开销
      - 随机访问性能差
      - CPU缓存不友好

3. ZipList：压缩列表，是Redis的列表和有序集合实现，节省内存。

    > ```c
    > struct ziplist {
    >     unsigned int zlbytes; // 压缩列表占用的内存字节数 4字节
    >     unsigned int zltail; // 尾节点距离起始位置的偏移字节数 4字节
    >     unsigned short zllen; // 节点数量 2字节
    >     unsigned char zlend; // 结尾标识 1字节
    >     entry[] // 节点数据 长度由具体数据决定
    > };
    > ```

    - entry内部结构
      - previous_entry_length // 前一个节点的长度 1字节或5字节
      - encoding // 编码方式 1字节或2字节或5字节
      - content // 节点数据 长度由具体数据决定 可以是字节数组或整数
    - 优点
      - 紧凑的数据结构，节省内存
      - 顺序访问性能好
      - 减少内存碎片
    - 缺点
      - 插入和删除操作性能差
      - 需要重新分配内存
    - 适用场景
      - 数据量较小
      - 读多写少
    - 连续更新
        插入一个大于等于254字节的节点，并且后续存在多个连续的、长度在250-253之间的节点，那么后续节点为了保存上一个节点的长度，prev_entry_length字段会从1字节变为5字节，这样会导致后续节点的长度增加4个字节。于是出现连续更新风暴。

4. QuickList：快速列表，是Redis的列表实现，支持快速插入和删除。

    > ```c
    > struct quicklist {
    >     quicklistNode *head; // 头节点
    >     quicklistNode *tail; // 尾节点
    >     unsigned long count; // 节点数量
    >     unsigned int len; // 总长度
    >     int fill; // 压缩列表节点最大长度
    > };
    > ```

    quicklistNode内部结构

    > ```c
    > struct quicklistNode {
    >     struct quicklistNode *prev; // 前一个节点
    >     struct quicklistNode *next; // 后一个节点
    >     unsigned char *zl; // 压缩列表
    >     unsigned int sz; // 节点长度
    >     unsigned int count; // 节点数量
    > };
    > ```

    - 在向quicklist插入数据时，会先检查插入位置的压缩列表节点是否有足够的空间，如果没有则新建一个节点。
    - 这样的设计并没有完全解决压缩列表的连续更新问题，因为连续更新问题根源在于压缩列表的结构设计，但是通过限制压缩列表节点的最大长度和个数，可以减少连续更新的风险。

5. ListPack：列表包，是Redis的列表实现，支持快速插入和删除。

    > ```c
    > struct listpack {
    >     unsigned int total_bytes; // 整个结构的长度
    >     unsigned short size; // entry长度，固定两个字节 
    >     char end; // 结尾标识 永远为0xFF，一个字节
    >     entrys; // 数据
    > };
    > ```

    entry内部结构

    > ```c
    > struct entry {
    >     unsigned char encoding; // 编码方式
    >     unsigned char length; // encoding+content的长度
    >     char content[]; // 数据
    > };
    > ```

    - 根据entry的length属性可以从后向前遍历。

6. Dict：字典，是Redis的哈希表实现，支持快速查找。

    > ```c
    > struct dict {
    >     dictType *type; // 类型
    >     void *privdata; // 私有数据
    >     dictht ht[2]; // 哈希表
    >     int rehashidx; // 重哈希索引
    >     unsigned long iterators; // 迭代器数量
    > };
    > ```

    - ht[0]：当前哈希表，ht[1]：下一个哈希表。两个哈希表交替使用。

    dictht内部结构

    > ```c
    > struct dictht {
    >     dictEntry **table; // 哈希表数组
    >     unsigned long size; // 哈希表大小
    >     unsigned long sizemask; // 哈希表掩码，用于计算索引
    >     unsigned long used; // 哈希表已使用节点数量
    > };
    > ```

    dictEntry内部结构

    > ```c
    > struct dictEntry {
    >     void *key; // 键
    >     union {
    >         void *val; // 值
    >         uint64_t u64;
    >         int64_t s64;
    >         double d;
    >     } v;
    >     struct dictEntry *next; // 下一个节点
    > };
    > ```

    dictType内部结构

    > ```c
    > struct dictType {
    >     uint64_t (*hashFunction)(const void *key); // 哈希函数
    >     void *(*keyDup)(void *privdata, const void *key); // 复制键
    >     void *(*valDup)(void *privdata, const void *obj); // 复制值
    >     int (*keyCompare)(void *privdata, const void *key1, const void *key2); // 比较键
    >    void (*keyDestructor)(void *privdata, void *key); // 销毁键
    >     void (*valDestructor)(void *privdata, void *obj); // 销毁值
    > };
    > ```

    - rehash
      - 发生条件
        - 负载因子 = 已使用节点数量 / 哈希表大小
        - 当哈希表的负载因子大于等于1时，并且Redis没有在执行bgsave、bgrewriteaof等操作，即没有执行RDB快照或者没有进行AOF重写时，Redis会自动开始rehash操作。
        - 当哈希表的负载因子大于等于5时，Redis会强制开始rehash操作。
      - 拷贝式rehash
        - 一次性将ht[0]的所有节点拷贝到ht[1]，然后释放ht[0]。
        - 优点：操作简单，不会出现哈希冲突。
        - 缺点：可能会发生阻塞，影响redis服务。
      - 渐进式rehash
        - 给ht[1]分配空间
        - 每次哈希表增删改查时，同时将相应的节点从ht[0]移动到ht[1]。
        - 随着时间推移，rehash操作会逐渐完成。
        - 优点：不会阻塞redis服务。

7. IntSet：整数集合，是Redis的集合实现，节省内存。

    > ```c
    > struct intset {
    >     uint32_t encoding; // 编码方式
    >     uint32_t length; // 集合长度
    >     int8_t contents[]; // 数据
    > };
    > ```

    - contents声明为int8_t类型数组，但实际上存储的是不同长度的整数。而类型根据encoding字段的值来确定。
    - 三种编码方式
      - INTSET_ENC_INT16
      - INTSET_ENC_INT32
      - INTSET_ENC_INT64
    - 升级
      - 当插入一个大于当前编码的整数时，会将整数集合升级为更高的编码方式。
      - 首先进行数组扩容，然后将所有整数转换为新的编码方式。
      - 升级后不支持降级。

8. Skiplist：跳跃表，是Redis的有序集合实现，支持快速查找。

    > ```c
    > struct zskiplist {
    >     struct zskiplistNode *header; // 头节点
    >     struct zskiplistNode *tail; // 尾节点
    >     unsigned long length; // 节点数量
    >     int level; // 最大层数
    > };
    > ```

    zskiplistNode内部结构

    > ```c
    > struct zskiplistNode {
    >     double score; // 分值
    >     sds ele; // 元素
    >     struct zskiplistNode *backward; // 后退指针
    >     struct zskiplistLevel {
    >         struct zskiplistNode *forward; // 前进指针
    >         unsigned int span; // 跨度
    >     } level[];
    > };
    > ```

### 基本数据结构

| 数据结构 | 说明 | 实现方式 |
|:-------|:----| :--- |
| String | 一种二进制安全的数据类型，可以用来存储任何类型的数据，比如字符串、整数、图片、序列化后的对象等。 | SDS（Simple Dynamic String） |
| List | 双向链表，支持反向查找和遍历，方便操作但是有额外的内存开销 | Linked List / ZipList / QuickList / ListPack |
| Set | 无序集合，内部元素唯一，没有先后顺序 | Dict / IntSet |
| ZSet | 有序集合，相比无序集合增加了一个权重参数score，可以通过score进行排序和查找 | ZipList / Skiplist |
| Hash | 哈希表， String类型的映射表，特别适合存储对象，可以直接修改对象里的值 | Dict / ZipList |

### 特殊数据结构

| 数据结构 | 说明 | 实现方式 |
|:-------|:----| :--- |
| Bitmap | 位图，可以用来存储用户签到、用户在线状态等信息 | String |
| HyperLogLog | 基数统计 | Bitmap |
| Geo | 地理位置 | ZSet |
| Stream | 消息队列 | List |

## 持久化

Redis支持两种持久化方式：RDB快照和AOF日志。

### RDB快照

RDB快照是Redis的默认开启的持久化方式，将内存中的数据保存到磁盘上，生成一个快照文件。RDB快照是一个二进制文件，保存了某个时间点的全量数据。

- 触发方式
  - save命令：主动触发，在主线程中执行，可能会阻塞服务
  - bgsave命令：后台异步触发，在子线程中执行，不会阻塞服务
  - 配置文件：save m n，表示m秒内有n次写操作则触发bgsave
  - 信号：kill -15 pid，发送SIGTERM信号触发bgsave
  - 关闭服务：shutdown命令会触发bgsave
  - 主从同步：主节点执行bgsave，从节点会自动执行bgsave
- 执行bgsave过程中，主线程仍然可以继续处理操作命令。原理是写时复制技术。
  - 执行bgsave命令时，fork()命令创建子线程，此时子线程和主线程共享内存。
  - 发生修改内存数据时，会复制一份数据到子线程的内存中，主线程继续操作自己的内存数据。
  - 因此，快照保存过程中发生的数据修改不会体现在快照当中。

### AOF日志

AOF日志是Redis的另一种持久化方式，将写操作以追加的方式保存到AOF文件中。AOF日志是一个文本文件，保存了Redis的写操作。Redis先执行命令，然后再写入AOF日志中。
redis中默认不开启AOF，可以通过配置文件开启。redis.conf，如  

> ```txt
> appendonly    yes
> appendfilename "appendonly.aof"  
> appendfsync  写回策略，有三种
> ```

写入AOF日志的过程

1. Redis执行写操作命令
2. 把命令追加到`server.aof_buf`缓冲区
3. I/O系统调用write()，将`server.aof_buf`中的数据写入AOF文件，即拷贝到了内核缓冲区page cache
4. 内核将page cache中的数据写入硬盘

- 三种写回策略
  - always：每次写操作都会同步到磁盘，最安全但性能最差
  - everysec：每秒同步一次，折中方案
  - no：不主动同步，交给操作系统控制，性能最好但安全性最差

- **AOF重写机制**
  对于同一条数据，如果出现了多次写操作，以最新的数据作为结果写入新的AOF文件中。
  - 重写AOF是由后台**进程**`bgrewriteaof`完成的
    - 使用子进程是为了保证共享内存数据以只读的方式进行，如果发生了内存修改，那么就会发生写时复制。
    - 父子进程中各自拥有独立的数据副本，减少了加锁的性能消耗。
    - 主进程在AOF重写时执行的命令将追加到AOF重写缓冲区，重写子进程的工作完成后，将会通知主进程将AOF重写缓冲区中的数据追加到新的AOF文件中
    - 所有工作完成后，新的AOF文件改名，覆盖原有的AOF文件。

## 过期淘汰策略

### 过期删除策略

Redis可以对key设置过期时间，需要有相应的机制删除过期的键值对，即为过期删除策略。

- 过期字典
  每当一个key设置了过期时间，就会被添加到过期字典中

  > ```c
  > struct redisDb {
  >     dict *dict; // 数据字典
  >     dict *expires; // 过期字典
  > };
  > ```

  - 过期字典的key是一个指针，指向某个对象；value是一个long long类型的整数，表示过期时间的毫秒时间戳。

- 过期删除策略
  - 定时删除：每隔一段时间，遍历过期字典，删除过期的键值对
    - 优点：保证过期的键值对能够及时删除，内存能够及时释放
    - 缺点：可能会阻塞服务，影响性能
  - 惰性删除：每次获取key时，检查是否过期，过期则删除
    - 优点：不会阻塞服务，对CPU友好
    - 缺点：可能会有大量的过期键值对没有及时删除，占用内存
  - 定期删除：每隔一段时间，随机抽取一些key，检查是否过期，过期则删除
    - 优点：很少阻塞服务，也可以删除一部分过期键值对
    - 缺点：可能会有一些过期键值对没有及时删除，难以兼顾性能和内存
- Redis使用惰性删除和定期删除相结合的方式，保证了性能和内存的平衡。
- Redis的过期删除策略：定期删除+惰性删除的混合策略。
  - 惰性删除：每次获取key时，检查是否过期，过期则删除，可选同步或异步删除
  - 定期删除：每隔一段时间，随机抽取20个key，检查是否过期，过期则删除；如果删除的key超过25%，则重复上述步骤。定期删除的时间间隔由`hz`配置项决定，默认为10，即每秒执行10次。此外，为了避免发生循环循环过度，Redis会限制定期删除的执行时长，如果超过25ms，则会停止执行，等待下一次执行。

### 内存淘汰策略

当Redis的内存使用达到上限时，需要有相应的机制释放内存，即为内存淘汰策略。

配置文件中的`maxmemory`配置项用于设置Redis的最大内存限制，单位为字节。当内存使用达到上限时，Redis会根据`maxmemory-policy`配置项选择相应的内存淘汰策略。

- 内存淘汰策略
  - noeviction **(默认)**：默认策略，当内存使用达到上限时，如果有新的写操作，会报错禁止写入，不淘汰数据，查询和删除操作正常工作。
  - 在设置了过期时间的数据进行淘汰的策略
    - volate-random：随机淘汰过期数据
    - volatile-ttl：淘汰剩余时间最短的数据
    - volatile-lru：淘汰最近最少使用的数据
    - volatile-lfu：淘汰最少使用的数据
  - 在所有数据范围内进行淘汰的策略
    - allkeys-random：随机淘汰数据
    - allkeys-lru：淘汰最近最少使用的数据
    - allkeys-lfu：淘汰最少使用的数据

## 主从架构

### 主从复制

Redis支持主从复制，主节点负责读写操作，从节点负责读操作，提高了读写性能和数据冗余。  
主服务器发生写操作时，会将写操作同步到从服务器，从服务器接收到写操作后，会执行写操作，保持数据一致。

- 第一次同步
   1. 建立链接
      1. 从服务器执行`slaveof` / `replicaof`命令，把自己设置为从服务器
      2. 从服务器向主服务器发送`PSYNC`命令，请求数据同步
          - `psync`命令参数
            - `runid`：主服务器的运行ID，第一次运行时为`?`
            - `offset`：主服务器的复制进度，第一次运行时为`-1`
      3. 主服务器接收到`PSYNC`命令，响应命令
          - `fullresync`：全量同步，主服务器发送RDB快照文件给从服务器
            - 此命令带上主服务器的运行ID和复制进度
   2. 同步数据
      1. 主服务器执行`bgsave`命令，生成RDB快照文件
      2. 主服务器将RDB快照文件发送给从服务器
      3. 从服务器清空数据，加载RDB快照文件
      > 同步数据过程中，生成快照文件后的写操作无法经过快照文件同步到从服务器
      > 为了解决这个问题，主服务器在生成快照文件，发送快照，从服务器加载快照的过程中，会记录主服务器的写操作，这些记录会写在replication buffer中，当从服务器加载完快照后，会将replication buffer中的写操作命令发送给从服务器，从服务器执行这些写操作，保证数据一致。
   3. 增量同步
      1. 从服务器完成全量同步后，向主服务器回复一个确认信息
      2. 主服务器接收到确认信息后，将写操作发送给从服务器
      3. 从服务器接收到写操作后，执行写操作，保持数据一致

- 命令传播
  主从服务器在完成第一次同步之后，双方会维护一个TCP连接。此连接是长连接，避免频繁的TCP连接和断开带来的性能开销。

- 分摊主服务器的压力
   从服务器也可以作为主服务器，充当一个中继器的功能。当有新的从服务器连接时，它可以连接从服务器，从服务器充当主服务器，将数据同步给新的从服务器。

- 增量复制
   当发生网络中断时，主从服务器之间的数据同步会中断。为了保证数据一致，恢复网络后，从服务器会重新连接主服务器，请求增量复制，主服务器会将断开连接期间的写操作发送给从服务器，保证数据一致。
   1. 从服务器向主服务器发送`PSYNC`命令，请求增量复制，此时offset为断开连接时的复制进度
   2. 主服务器接收到`PSYNC`命令，响应命令`CONTINUE`，告知采取使用增量复制
   3. 主服务器将短线期间的写命令发送给从服务器。从服务器接收到写操作后，执行写操作，保持数据一致
    > 增量复制的实现原理是主服务器维护一个复制缓冲区，记录主服务器的写操作，当从服务器请求增量复制时，主服务器将复制缓冲区中的写操作发送给从服务器，从服务器执行这些写操作，保持数据一致。
    > 具体细节如下：
    > 1. 在主从断连后，主服务器维护一个复制缓冲区`repl_backlog_buffer`，记录主服务器的写操作
    > 2. 于此同时，主服务器维护一个偏移量`master_repl_offset`，记录主服务器的复制进度，从服务器的复制进度为`slave_repl_offset`。
    > 3. 从服务器重连时，向主服务器发送`PSYNC`命令，带上自己的复制进度`slave_repl_offset`，主服务器接收到`PSYNC`命令后，判断`slave_repl_offset`是否在复制缓冲区中，如果在，可以进行增量复制。
    > 4. 如果不在，主服务器会发送`FULLRESYNC`命令，进行全量同步。

### 哨兵机制

Redis的主从复制存在单点故障，当主服务器宕机时，整个系统不可用。为了解决这个问题，Redis引入了哨兵机制，实现主从切换和故障恢复。

- 哨兵机制
  - 哨兵是一个独立的进程，负责监控主从服务器的状态，当主服务器宕机时，哨兵会选举一个从服务器作为新的主服务器，保证系统的可用性。
  - 哨兵的工作流程
    1. 哨兵监控主服务器的状态，当主服务器宕机时，哨兵会选举一个从服务器作为新的主服务器
        - 哨兵每隔1秒向所有服务器发送`PING`命令，各节点返回`PONG`命令，哨兵判断节点是否存活
        - 如果在`down-after-milliseconds`时间内，服务器没有返回`PONG`命令，哨兵认为服务器宕机，即主观下线
        - 为了减少误判，哨兵一般会部署多个节点，当多个哨兵都认为服务器宕机时，才会认为服务器真正宕机，即客观下线
           > 投票机制：当哨兵发现主服务器宕机时，会向其他哨兵发送`SENTINEL is-master-down-by-addr`命令，请求其他哨兵投票，如果超过`quorum`的哨兵认为主服务器宕机，则主服务器宕机
           > `quorum`：投票数，一般为哨兵节点数的一半加1
    2. 哨兵会通知所有的从服务器，切换到新的主服务器
        - 第一个判断主节点下线的哨兵收到`quorum`托票数将主节点客观下线之后，其自身转换为候选者，开始选举新的主节点
        - 候选者向其他哨兵发出命令，表示希望成为leader；其他哨兵收到命令后开始投票，每个哨兵只有一次投票机会，并且候选者只能投给自己。
        - 候选者收到至少`quorum`的投票，并且拿到超过所有哨兵节点数一半的投票数，就会成为新的leader
        - 由于投票机制，哨兵集群至少需要三个哨兵节点，才能保证一定能选出leader
        - 如果哨兵故障数量过多，则需要人为干预。
    3. 哨兵会通知客户端，主服务器发生了变化 **主从故障转移**
        1. 在原主节点的从节点中选举一个新的主节点
           1. 首先排除网络状态差的从节点。如果发生断连的次数超过10次，就会被排除
           2. 根据优先度排序，优先度在`slave-priority`配置项中设置，值越小优先级越高
           3. 再根据复制偏移量排序，即比较`slave-repl-offset`，值越大优先级越高
           4. 最后根据节点ID排序
           5. 选举出一个新的主节点后，哨兵Leader会向该节点发送`SLAVEOF no one`命令，让其成为新的主节点
           6. 当哨兵接收到的`INFO`变为`role:master`时，认为新的主节点选举成功
        2. 通知其他从节点切换到新的主节点
           1. 哨兵Leader向所有从节点发送`SLAVEOF new_master_ip new_master_port`命令，让其切换到新的主节点
        3. 通过发布订阅机制通知客户端，主服务器发生了变化
        4. 继续监视原主节点，当原主节点恢复时，将其设置为新的从节点
  - 哨兵的配置
    - `sentinel monitor`：监控主服务器的IP和端口
    - `sentinel down-after-milliseconds`：主服务器宕机后，哨兵等待多久才认为主服务器宕机
    - `sentinel failover-timeout`：主服务器宕机后，哨兵等待多久才进行故障转移
    - `sentinel parallel-syncs`：哨兵同时同步的从服务器数量
  - 哨兵的工作原理
    1. 哨兵监控主服务器的状态，当主服务器宕机时，哨兵会等待`down-after-milliseconds`时间，确认主服务器宕机
    2. 哨兵会选举一个从服务器作为新的主服务器
    3. 哨兵会通知所有的从服务器，切换到新的主服务器
    4. 哨兵会通知客户端，主服务器发生了变化
