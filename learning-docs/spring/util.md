# Spring Util

1. MultiValueMap
    MultiValueMap接口表示多值映射，是Map的子接口，定义了一对多的映射关系。该接口提供了获取和设置多值的方法。
    - `List<V> get(Object key)`：获取指定键的值
    - `V getFirst(Object key)`：获取指定键的第一个值
    - `void add(K key, V value)`：添加键值对
    - `void addAll(K key, List<? extends V> values)`：添加多值
    - `void addAll(MultiValueMap<K, V> values)`：添加多值
    - `void set(K key, V value)`：设置键值对
    - `void setAll(Map<K, V> values)`：设置多值
    - `Map<K, V> toSingleValueMap()`：转换为单值映射
    1. LinkedMultiValueMap
        LinkedMultiValueMap类是MultiValueMap接口的实现类，其中内部数据结构时LinkedHashMap。该类线程不安全，只用于单线程。
        - `LinkedMultiValueMap<K, V> clone()`：浅复制
        - `LinkedMultiValueMap<K, V> deepCopy()`：深复制

## coding

- `Base64Utils`
    Base64Utils类是Base64编码工具类，提供了Base64编码和解码的方法。
  - `byte[] encode(byte[] src)`：Base64编码
  - `byte[] decode(byte[] src)`：Base64解码
  - `byte[] encodeFromString(String src)`：Base64编码
  - `byte[] decodeFromString(String src)`：Base64解码
  - `String encodeToString(byte[] src)`：Base64编码
  - `String decodeToString(byte[] src)`：Base64解码
