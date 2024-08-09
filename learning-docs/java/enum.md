# java enum

所有的枚举类型都继承自`java.lang.Enum`类，所以不能再继承其他类，但是可以实现接口。

特性：

- 枚举类型是一种特殊的类，可以有自己的字段、方法和构造函数。甚至可以拥有`main`方法。
- 枚举类型的构造函数只能是`private`的。
- 枚举类型的字段必须在枚举类型的最前面，字段之间用逗号分隔，最后一个字段后面可以加分号。
- 每一个字段都是枚举类型的一个实例，实际上调用了枚举类型的构造函数。

常用方法：

- `Enum[] values()`：返回枚举类型的所有字段。编译器自动添加的方法。
- `String name()`：返回枚举类型的字段名。
- `int ordinal()`：返回枚举类型的字段顺序，从0开始。
- `static <T extends Enum<T>> T valueOf(Class<T> enumType, String name)`：返回指定字段名的枚举类型字段。

有关方法：

- `public T[] getEnumConstants()`：返回当前枚举类型的所有字段，如果不是枚举类型，则返回`null`。

## EnumSet

`EnumSet`是一个专门为枚举类型设计的`Set`集合，它的元素必须是枚举类型的字段，其中元素的存放次序与枚举类型的字段顺序一致。他的基础是`long`类型的位向量，在枚举类型的字段不超过64个时，`EnumSet`使用`long`类型的位向量来存储，否则使用`long`类型的数组来存储。

`EnumSet`的常用方法：

| 方法签名 | 说明 |
| --- | --- |
| static <E extends Enum<E>> EnumSet<E> allOf(Class<E> elementType) | 创建一个包含指定枚举类型所有字段的`EnumSet`对象 |
| static <E extends Enum<E>> EnumSet<E> noneOf(Class<E> elementType) | 创建一个不包含任何字段的`EnumSet`对象 |
| static <E extends Enum<E>> EnumSet<E> of(E first, E... rest) | 创建一个包含指定字段的`EnumSet`对象 |
| static <E extends Enum<E>> EnumSet<E> range(E from, E to) | 创建一个包含从`from`到`to`之间所有字段的`EnumSet`对象 |
| static <E extends Enum<E>> EnumSet<E> complementOf(EnumSet<E> s) | 创建一个包含`EnumSet`对象中没有的字段的`EnumSet`对象 |
| static <E extends Enum<E>> EnumSet<E> copyOf(Collection<E> c) | 创建一个包含指定集合中所有字段的`EnumSet`对象 |
| static <E extends Enum<E>> EnumSet<E> copyOf(EnumSet<E> s) | 创建一个包含指定`EnumSet`对象中所有字段的`EnumSet`对象 |

`EnumSet`的实现是`EnumSet`类的静态内部类，它有两个实现类：`RegularEnumSet`和`JumboEnumSet`。`RegularEnumSet`使用`long`类型的位向量来存储枚举类型的字段，`JumboEnumSet`使用`long`类型的数组来存储枚举类型的字段。

## EnumMap

`EnumMap`是一个专门为枚举类型设计的`Map`集合，它的键必须是枚举类型的字段。底层由双数组实现，一个数组存储键，一个数组存储值，如果value为null，则会特殊处理为Object对象。元素的存放次序与枚举类型的字段顺序一致。

创建时必须指定枚举类型：

- `EnumMap(Class<K> keyType)`：创建一个指定枚举类型的`EnumMap`对象。
- `EnumMap(Map<K, ? extends V> m)`：创建一个包含指定`Map`对象中所有字段的`EnumMap`对象。
- `EnumMap(EnumMap<K, ? extends V> m)`：创建一个包含指定`EnumMap`对象中所有字段的`EnumMap`对象。
