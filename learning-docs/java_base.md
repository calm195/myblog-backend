# java 基础

## 零碎知识 - debug，好奇等等收集来的

1. `Long::valueOf`是一个方法引用, 引用的是  
`Long valueOf(long value);`  
`Long valueOf(String s);`  
这两种重载函数。同理则有`String:valueOf`相似的方法引用。  

2. `Function<T, R>`一种方法引用，接受`<T>`返回`<R>`，用法如：
   1. `apply(T t)`方法：直接调用  

      ```java
      Function<String, String> function = a -> a + " Jack!";
      System.out.println(function.apply("Hello")); // Hello Jack!
      ```  

   2. `andThen(Function<? super R,? extends V> after)`：组合函数调用  

      ```java
      Function<String, String> function = a -> a + " Jack!";
      Function<String, String> function1 = a -> a + " Bob!";
      String greet = function.andThen(function1).apply("Hello");
      System.out.println(greet); // Hello Jack! Bob!
      ```

   3. `compose(Function<? super V,? extends T> before)`：复合函数调用

      ```java
      Function<String, String> function = a -> a + " Jack!";
      Function<String, String> function1 = a -> a + " Bob!";
      String greet = function.compose(function1).apply("Hello");
      System.out.println(greet); // Hello Bob! Jack!
      ```

3. `DateTimeFormatter`：时间转化格式器，不可变，线程安全  
   `DateTimeFormatter UTC_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");`  
   `format(TemporalAccessor temporal)`方法： 返回格式化后的字符串

   ```java
   import java.time.LocalDateTime; 
   import java.time.format.DateTimeFormatter;   
   LocalDateTime localDateTime = LocalDateTime.now();
   DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
   String timeStr = dateTimeFormatter.format(localDateTime);
   ```

4. try-with-resources：自动关闭资源
    - try中的资源必须实现`AutoCloseable`接口
    - try中声明的资源隐式的被声明为`final`，所以不能再次赋值
    - 可以声明多个资源，用`;`分隔
    - 资源的声明顺序和关闭顺序相反
    - try中的资源在try结束后会自动关闭，不需要手动关闭

5. `Supplier<T>`：提供者接口，提供一个返回类型为`T`的对象
    - `T get()`方法：返回一个`T`类型的对象

    ```java
    Supplier<String> supplier = () -> "Hello World!";
    System.out.println(supplier.get()); // Hello World!
    ```

6. `NumberFormat`：数字格式化类
    - `format(double number)`方法：格式化数字
    - `setMaximumFractionDigits(int newValue)`方法：设置最大小数位数
    - `setMinimumFractionDigits(int newValue)`方法：设置最小小数位数
    - `setMaximumIntegerDigits(int newValue)`方法：设置最大整数位数
    - `setMinimumIntegerDigits(int newValue)`方法：设置最小整数位数
    - `getInstance()`方法：返回指定语言环境的通用数字格式

      ```java
      NumberFormat numberFormat = NumberFormat.getInstance();
      System.out.println(numberFormat.format(123456789)); // 123,456,789
      ```

    - `getCurrencyInstance()`方法：返回指定语言环境的通用货币格式

      ```java
      NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
      System.out.println(currencyFormat.format(123456789)); // ￥123,456,789.00
      ```

    - `getPercentInstance()`方法：返回指定语言环境的通用百分比格式

      ```java
      NumberFormat percentFormat = NumberFormat.getPercentInstance();
      System.out.println(percentFormat.format(0.123)); // 12%
      ```

7. 四种引用类型：强引用、软引用、弱引用、虚引用
    - 强引用：`Object obj = new Object();`，只要强引用存在，垃圾回收器不会回收对象
    - 软引用：`SoftReference<Object> softReference = new SoftReference<>(new Object());`，内存不足时，垃圾回收器会回收对象
    - 弱引用：`WeakReference<Object> weakReference = new WeakReference<>(new Object());`，只要垃圾回收器运行，就会回收对象
    - 虚引用：`PhantomReference<Object> phantomReference = new PhantomReference<>(new Object(), new ReferenceQueue<>());`，用于跟踪对象被垃圾回收器回收的活动，用队列接收对象即将死亡的通知

8. `AtomicInteger`：原子整数类，线程安全
    - `get()`方法：获取当前值
    - `set(int newValue)`方法：设置新值
    - `incrementAndGet()`方法：自增并返回新值
    - `decrementAndGet()`方法：自减并返回新值
    - `getAndIncrement()`方法：返回旧值并自增
    - `getAndDecrement()`方法：返回旧值并自减
    - `compareAndSet(int expect, int update)`方法：比较并设置，如果当前值等于期望值，则设置新值

9. `volatile`关键字：保证可见性，禁止指令重排序
    - 保证可见性：当一个线程修改了共享变量的值，其他线程能够立即看到修改后的值
    - 禁止指令重排序：保证代码执行的顺序和预期的一致

10. java中的运算不是原子性的，比如`i++`，实际上是三个步骤：读取i的值，i+1，将i+1的值赋给i。如果多个线程同时执行`i++`，可能会出现问题。可以使用`AtomicInteger`来保证原子性。

11. `Runtime`类：运行时类
    - `getRuntime()`方法：返回当前运行时对象
    - `availableProcessors()`方法：返回可用处理器的数量
    - `freeMemory()`方法：返回JVM的空闲内存量
    - `totalMemory()`方法：返回JVM的总内存量
    - `maxMemory()`方法：返回JVM的最大内存量
    - `gc()`方法：运行垃圾回收器

12. `@FunctionalInterface`注解：函数式接口
    - 只有一个抽象方法的接口，可以使用`@FunctionalInterface`注解标记
    - 可以使用lambda表达式来实现函数式接口
    - 比如`Runnable`、`Callable`、`Comparator`等
    - `@FunctionalInterface`注解不是必须的，但是推荐使用，可以让编译器检查接口是否符合函数式接口的标准

        ```java
        @FunctionalInterface
        public interface MyInterface {
            void method();
        }
        ```

13. java组件类
    这里表述的是包`org.apache.commons.lang3.tuple`中的组件类
    > 它们不是标准的javaBean，序列化和反序列化会有问题，一般用于系统内部数据传递，不要用于RPC、HTTP等外部传输。
    - `Pair<L, R>`：键值对
    - `ImmutablePair<L, R>`：不可变的键值对
    - `MutablePair<L, R>`：可变的键值对
    - `Triple<L, M, R>`：三元组
    - `ImmutableTriple<L, M, R>`：不可变的三元组
    - `MutableTriple<L, M, R>`：可变的三元组

14. `Synchornized`关键字：同步锁

    > 1. 一把锁只能同时被一个线程获取
    > 2. 每个对象都有一个锁，相互之间不影响
        > 锁对象是*.class或修饰的方法是静态方法时，锁住的是整个类，所有对象共享一把锁
    > 3. 修饰的方法无论正常执行还是异常退出，都会释放锁

    - 两种形式
      - 代码块形式：修饰非静态方法，默认锁住的是当前对象 **(this)**
        例如：`synchronized(this) {}`
        可以自定义锁对象，例如：`synchronized(obj) {}`
        当自定义锁对象为*.class时，锁住的是整个类
        例如：`synchronized(MyClass.class) {}`
      - 方法锁形式：修饰普通方法，默认锁住的是当前对象 **(this)**
        例如：`public synchronized void method() {}`
        修饰静态方法，默认锁住的是整个类
        例如：`public static synchronized void method() {}`

15. `java.util.regex`：正则表达式包
    - `Pattern`：编译正则表达式后创建一个对应的匹配模式
      - 声明：`public final class Pattern implements java.io.Serializable`
      - 方法 compile()：`Pattern Pattern.compile(String regex, int flag)`
      - 方法 compile()：`Pattern compile(String regex)` 默认为单行匹配模式
      - flag的取值范围，Pattern.*。
        > 在表达式里插入记号可以启用模式，在哪里插入就会在哪里启用，并且可以使用`|`配合使用。

        | 字段 | 说明 |
        | :--- | :--- |
        | UNIX_LINES |  unix行模式，大多数系统的行都是以\n结尾的，但是少数系统，比如Windows，却是以\r\n组合来结尾的，启用这个模式之后，将会只以\n作为行结束符，这会影响到^、$和点号(点号匹配换行符)。通过嵌入式标志表达式 (?d) 也可以启用 Unix 行模式。|
        | CASE_INSENSITIVE | 默认情况下，大小写不敏感的匹配只适用于US-ASCII字符集。这个标志能让表达式忽略大小写进行匹配。要想对Unicode字符进行大小不明感的匹配，只要将UNICODE_CASE与这个标志合起来就行了。通过嵌入式标志表达式(?i)也可以启用不区分大小写的匹配。指定此标志可能对性能产生一些影响。|
        | COMMENTS |  这种模式下，匹配时会忽略(正则表达式里的)空格字符（不是指表达式里的”//s”，而是指表达式里的空格，tab，回车之类）和注释（从#开始，一直到这行结束）。通过嵌入式标志表达式(?x) 也可以启用注释模式。|
        | MULTILINE | 默认情况下，输入的字符串被看作是一行，即便是这一行中包好了换行符也被看作一行。当匹配“^”到“$”之间的内容的时候，整个输入被看成一个一行。启用多行模式之后，包含换行符的输入将被自动转换成多行，然后进行匹配。通过嵌入式标志表达式 (?m) 也可以启用多行模式。 |
        | LITERAL |  启用字面值解析模式。指定此标志后，指定模式的输入字符串就会作为字面值字符序列来对待。输入序列中的元字符或转义序列不具有任何特殊意义。标志CASE_INSENSITIVE 和 UNICODE_CASE 在与此标志一起使用时将对匹配产生影响。其他标志都变得多余了。不存在可以启用字面值解析的嵌入式标志字符。 |
        | DOTALL | 在这种模式中，表达式 .可以匹配任何字符，包括行结束符。默认情况下，此表达式不匹配行结束符。通过嵌入式标志表达式 (?s) 也可以启用此种模式（s 是 “single-line” 模式的助记符，在 Perl 中也使用它）。 |
        | UNICODE_CASE | 在这个模式下，如果你还启用了CASE_INSENSITIVE标志，那么它会对Unicode字符进行大小写不敏感的匹配。默认情况下，大小写不明感的匹配只适用于US-ASCII字符集。指定此标志可能对性能产生影响。|
        | CANON_EQ | 当且仅当两个字符的正规分解(canonical decomposition)都完全相同的情况下，才认定匹配。比如用了这个标志之后，表达式a/u030A会匹配?。默认情况下，不考虑规范相等性(canonical equivalence)。指定此标志可能对性能产生影响。 |
    - `Matcher`：使用`Pattern`实例提供的正则表达式对目标字符串进行搜索
        - 声明：`public final class Matcher extends Object implements MatchResult`
        - 方法
            - 创建实例对象，一般由Pattern创建：`Matcher matcher = xxxParttern.matcher(String targetString);`
            - `boolean find()`：对目标字符串进行匹配
            - `boolean find(int start)`：对目标字符串从`start`位置开始匹配
            - `int start()`：返回当前匹配到的字符串在原目标字符串中的起始索引位置
            - `int end()`：返回当前匹配到的字符串在原目标字符串中的末尾索引位置
            - `Pattern pattern()`：也可以返回创建本实例的`Pattern`
16. `Enumeration`接口：枚举接口
      - 声明：`public interface Enumeration<E>`
      - 方法
        - `boolean hasMoreElements()`：测试此枚举是否包含更多的元素
        - `E nextElement()`：如果此枚举对象至少还有一个可提供的元素，则返回此枚举的下一个元素

17. `Optional`类：用于解决空指针异常
    - 声明：`public final class Optional<T>`
    - 方法
      - `static <T> Optional<T> empty()`：返回一个空的Optional实例
      - `static <T> Optional<T> of(T value)`：返回一个指定非null值的Optional实例
      - `static <T> Optional<T> ofNullable(T value)`：返回一个指定值的Optional实例，如果值为null，则返回一个空的Optional实例
      - `T get()`：如果值存在，返回值，否则抛出NoSuchElementException异常
      - `boolean isPresent()`：如果值存在，返回true，否则返回false
      - `void ifPresent(Consumer<? super T> consumer)`：如果值存在，则执行consumer，否则不执行
      - `T orElse(T other)`：如果值存在，返回值，否则返回other
      - `T orElseGet(Supplier<? extends T> other)`：如果值存在，返回值，否则返回other提供的值
      - `T orElseThrow(Supplier<? extends X> exceptionSupplier)`：如果值存在，返回值，否则抛出由exceptionSupplier提供的异常

18. `Consumer`接口：消费者接口
    - 声明：`public interface Consumer<T>`
    - 方法
      - `void accept(T t)`：接受一个输入参数并且无返回值
      - `default Consumer<T> andThen(Consumer<? super T> after)`：返回一个组合的Consumer，依次执行当前Consumer和after Consumer

## 内存模型

Java内存模型规定了所有的变量都存储在主内存中，每个线程都有自己的工作内存，线程的工作内存中保存了该线程使用到的变量的副本。  
线程对变量的所有操作都必须在工作内存中进行，不能直接读写主内存中的变量。不同线程之间无法直接访问对方的工作内存，线程间的变量值的传递需要通过主内存来完成。

1. CPU内存模型
   - CPU Cache 缓存的是内存数据用于解决 CPU 处理速度和内存不匹配的问题，内存缓存的是硬盘数据用于解决硬盘访问速度过慢的问题。
2. 指令重排序
    - 编译器为了提高性能，可能会对指令进行重排序，但是不会影响单线程程序的执行结果。
    - 处理器采用了分支预测、乱序执行等技术来实现指令并行，可能会对指令进行重排序，但是不会影响单线程程序的执行结果。
3. java源代码会经历以下过程才变成可执行码
    - 编译器优化重排
    - 指令并行重排
    - 内存系统重排
4. 内存屏障
    - 内存屏障是一种CPU指令，用于禁止处理器指令重排序。
    - 有时会在处理器读写值前，将主内存的值写入cache，清空无效队列。

## SpEL

全称：Spring Expression Language （Spring 表达式语言）  
定义：[SpEL](https://docs.spring.io/spring-framework/reference/core/expressions.html) 是 Spring 定义的一套在 Spring 框架内运行的表达式语言，说是语言，但可以理解为通过特定格式的字符串来让 Spring 框架解析出原来的含义，可简化很多对数据的操作动作。  
后端类似的有 OGNL, MVEL 和 JBoss EL。前端方面，Thymeleaf，FreeMarker 的数据渲染语法也可以理解为一种表达式语言。

1. 简单案例

    ```java
    import org.springframework.expression.spel.standard.SpelExpressionParser;
    @Test
    public void test_hello() {
        // 1 定义解析器
        SpelExpressionParser parser = new SpelExpressionParser();
        // 2 使用解析器解析表达式
        Expression exp = parser.parseExpression("'Hello World'");
        // 3 获取解析结果
        String value = (String) exp.getValue();
        System.out.println(value);
    }
   ```

2. ExpressionParser 接口
    - `Expression parseExpression(String expressionString)`：解析表达式
    - `Expression parseExpression(String expressionString, ParserContext context)`：解析表达式，可以指定解析上下文
    - `Expression parseExpression(String expressionString, ExpressionParserContext context)`：解析表达式，可以指定解析上下文
    - `Expression parseExpression(String expressionString, TemplateParserContext context)`：解析表达式，可以指定解析上下文
