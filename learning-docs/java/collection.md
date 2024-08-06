# java 集合

Java 集合，也叫作容器，主要是由两大接口派生而来：一个是 Collection接口，主要用于存放单一元素；另一个是 Map 接口，主要用于存放键值对。对于Collection 接口，下面又有三个主要的子接口：List、Set 、Queue。

[Iterable 接口](https://docs.oracle.com/javase/8/docs/api/java/lang/Iterable.html)是 Collection 接口的父接口，提供了迭代器的功能。

| 方法声明 | 说明 |
| --- | --- |
| `Iterator<E> iterator()` | 获取迭代器 |
| `default Spliterator<E> spliterator()` | 获取分割迭代器 |
| `default void forEach(Consumer<? super E> action)` | 以给定的动作遍历 |

[Collection 自身方法](https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html)：

| 方法声明 | 说明 |
| --- | --- |
| `boolean add(E e)` | 添加元素 |
| `boolean addAll(Collection<? extends E> c)` | 在末尾添加集合中的所有元素 |
| `void clear()` | 清空集合 |
| `boolean contains(Object o)` | 判断是否包含某个元素 |
| `boolean containsAll(Collection<?> c)` | 判断是否包含集合中的所有元素 |
| `boolean equals(Object o)` | 判断是否相等 |
| `int hashCode()` | 获取哈希值 |
| `boolean isEmpty()` | 判断是否为空 |
| `default Stream<E> parallelStream()` | 获取以自己本身为源的并行流 |
| `boolean remove(Object o)` | 移除指定元素 |
| `boolean removeAll(Collection<?> c)` | 移除集合中的所有元素 |
| `boolean removeIf(Predicate<? super E> filter)` | 根据条件删除 |
| `boolean retainAll(Collection<?> c)` | 保留指定集合中的所有元素 |
| `int size()` | 获取集合大小 |
| `default Stream<E> stream()` | 获取当前集合的流 |
| `Object[] toArray()` | 转换为数组 |
| `<T> T[] toArray(T[] a)` | 转换为数组 |

## List

储存有序的元素，可以重复。

[List 自身方法](https://docs.oracle.com/javase/8/docs/api/java/util/List.html)：

| 方法声明 | 说明 |
| --- | --- |
| `void add(int index, E element)` | 在指定位置添加元素 |
| `boolean addAll(int index, Collection<? extends E> c)` | 在指定位置添加集合中的所有元素 |
| `E get(int index)` | 获取指定位置的元素 |
| `int indexOf(Object o)` | 获取元素第一次出现的位置 |
| `int lastIndexOf(Object o)` | 获取元素最后一次出现的位置 |
| `ListIterator<E> listIterator()` | 获取列表迭代器 |
| `ListIterator<E> listIterator(int index)` | 获取从特定位置开始的列表迭代器 |
| `E remove(int index)` | 移除指定位置的元素 |
| `boolean replaceAll(UnaryOperator<E> operator)` | 根据提供的操作方法替换所有元素 |
| `E set(int index, E element)` | 替换指定位置的元素 |
| `void sort(Comparator<? super E> c)` | 排序 |
| `List<E> subList(int fromIndex, int toIndex)` | 获取子列表 |

1. ArrayList **（线程不安全）**：
    内部基于动态数组实现，查询快，增删慢。
    自身方法：
    - `ArrayList()`：默认构造方法
    - `ArrayList(int initialCapacity)`：指定初始容量的构造方法
    - `ArrayList(Collection<? extends E> c)`：指定集合的构造方法
    - `Object clone()`：克隆，浅拷贝
    - `void ensureCapacity(int minCapacity)`：确保容量
    - `boolean removeIf(Predicate<? super E> filter)`：根据条件删除
    - `protected void removeRange(int fromIndex, int toIndex)`：删除指定范围
    - `void trimToSize()`：裁剪容量，使得容量和当前实际占用大小相等
2. CopyOnWriteArrayList **（线程安全）**：ReentrantLock 加锁
    内部基于动态数组实现，增删快，查询慢。适用于读多写少的场景。
    自身方法：
    - `CopyOnWriteArrayList()`：默认构造方法
    - `CopyOnWriteArrayList(Collection<? extends E> c)`：指定集合的构造方法
    - `CopyOnWriteArrayList(E[] toCopyIn)`：指定数组的构造方法
    - `boolean addAllAbsent(Collection<? extends E> c)`：添加不重复元素
    - `boolean addIfAbsent(E e)`：添加不重复元素
    - `Object clone()`：克隆，浅拷贝
    - `int indexOf(E e, int index)`：从指定位置开始查找元素，返回第一次出现的位置，若无则返回-1
    - `int lastIndexOf(E e, int index)`：从指定位置开始查找元素，返回最后一次出现的位置，若无则返回-1
    - `String toString()`：转换为字符串
3. LinkedList **（线程不安全）**：
    内部基于双向链表实现，增删快，查询慢。
4. Vector **（线程安全）已淘汰**：
    古老实现类
5. Stack **（线程安全）已淘汰**：
    继承自Vector，后进先出

## Set

储存的元素是无序的，不可以重复。

1. HashSet
2. TreeSet
3. LinkedHashSet

## Queue

储存的元素是有序的，可以重复，按照特定的规则排序

1. PriorityQueue
2. ArrayDeque
3. DelayQueue
4. BlockingQueue
    BlockingQueue 通常用于一个线程生产对象，而另外一个线程消费这些对象的场景。容量是有限的，当队列满时，生产者会被阻塞，直到队列有空间；当队列为空时，消费者会被阻塞，直到队列有数据。
    |  | 抛出异常 | 返回特殊值 | 阻塞 | 超时 |
    | --- | --- | --- | --- | --- |
    | 插入 | `add(e)` | `offer(e)` | `put(e)` | `offer(e, time, unit)` |
    | 移除 | `remove()` | `poll()` | `take()` | `poll(time, unit)` |
    | 检查 | `element()` | `peek()` |  |  |

    相应实现类：
    1. ArrayBlockingQueue：数组阻塞队列，有界，先进先出
    2. DelayQueue：延迟队列，在每个元素的延迟时间到达后才会释放该元素；即过期后调用take()才会释放
    3. LinkedBlockingQueue：链表阻塞队列，有界或无界，先进先出
    4. PriorityBlockingQueue：优先级阻塞队列，无界，按照元素的优先级顺序出队
    5. SynchronousQueue：同步队列，无缓冲区，生产者线程必须等待消费者线程消费，反之亦然。原理为队列内部只能容纳一个元素。

## Map

储存键值对，键是唯一的，值可以重复。每个键最多只能映射到一个值。

1. HashMap
2. LinkedHashMap
3. Hashtable
4. TreeMap
5. ConcurrentHashMap **（线程安全）**
    内部使用Node数组+链表/红黑树实现。当链表长度大于一定长度，链表会转换为红黑树。初始化通过自旋+CAS实现。
    - `void put(K key, V value)`：添加元素
    - `V putIfAbsent(K key, V value)`：如果不存在则添加，返回null；如果存在则返回原值
    - `V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)`：根据键值计算新值。执行函数可能会阻塞线程，因此需要尽可能简单快捷。
    - `V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction)`：根据键值计算新值，如果不存在则添加。执行函数可能会阻塞线程，因此需要尽可能简单快捷。
    - `void remove(Object key)`：移除元素
    - `boolean containsKey(Object key)`：判断是否包含键
