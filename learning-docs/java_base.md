# java 基础
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