# Spring注解

## 常见注解

1. `@Slf4j`：门面模式的日志框架，来源于Lombok，使用的是Logback框架。
   - 使用该注解之后，Lombok会为你自动创建一个以类名为参数的Logger对象。

   ```java
   import lombok.extern.slf4j.Slf4j;
   
   @Slf4j
   public class MyClass {
      public void myMethod() {
         log.info("This is a log message");
      }
   }
   ```

2. `@Aspect`：将当前类标识为一个切面，供容器使用
3. `@Component`：将当前POJO实例化到Spring容器中
4. `@ConfigurationProperties`：将配置文件中的属性注入到当前类中
   > 要使用本注解，需要在配置类上加上`@EnableConfigurationProperties`注解，并且指明当前配置类；或者在当前配置类上加上`@Configuration`注解

   常见用法：
   - `@ConfigurationProperties(prefix = "xxx")`：将配置文件中以`xxx`开头的属性注入到当前类中
   - `@Value("${xxx}")`：将配置文件中的`xxx`属性注入到当前类中
5. `@Controller`：将当前类标识为一个控制器
6. `@Data`：Lombok注解，自动生成`toString()`, `equals()`, `hashCode()`, `getter`, `setter`等方法
7. `@Accessors`：Lombok注解
   - `chain`：是否启用链式调用，默认为false，启用后setter方法返回当前对象
   - `fluent`：是否启用fluent风格，默认为false，即getter, setter方法名前不加`get`, `set`，会生成get, set方法
   - `prefix`：该属性是一个字符串数组，当该数组有值时，表示忽略字段中对应的前缀，生成对应的 getter 和 setter 方法。
8. `@Entity`：JPA注解，将当前类标识为一个实体类。并且会映射到数据库表中，默认情况下，表名和类名一致，字段名和属性名一致。
9. `@Resource`：jdk注解，用于注入其他组件。默认使用名称注入，如果找不到对应的名称，则使用类型注入。可以指定`name`和`type`属性。不支持在构造方法和参数上使用。
10. `@Autowired`：Spring注解，用于注入其他组件。默认使用类型注入。
11. `@Qualifier`：Spring注解，用于指定注入的Bean的名称。搭配`@Autowired`使用，当有多个同类型的Bean时，可以根据名称指定注入哪一个。

## 条件注解

根据注解条件决定是否加载某个Bean等动作。

1. `@ConditionalOnProperty`：根据配置文件中的属性值决定是否加载Bean
   - `String prefix`：配置文件中的属性前缀
   - `String[] name`：配置文件中的属性名
   - `String havingValue`：期待属性值
   - `boolean matchIfMissing`：如果配置文件中没有该属性，是否加载Bean，默认为false

## 元注解

注解的注解，即可以用来修饰注解的注解。  
java.lang.annotation 提供了四种元注解，专门用于注解其他的注解（在自定义注解的时候，需要使用到元注解）  
分别是：  
[@Target](#target) – 注解用于什么地方  
[@Retention](#retention) – 什么时候使用该注解  
[@Documented](#documented) – 注解是否将包含在JavaDoc中  
[@Inherited](#inherited) – 是否允许子类继承该注解  

1. <span id="target"> @Target() – 注解用于什么地方  </span>

   > 该注解可以声明在哪些目标元素之前，如包，类，方法。  
   > 使用方法如：`@Target({ElementType.METHOD})`  
   > > Tip: 大括号内可以共存多个种类，各种类之间用`,`连接

   常用种类有：
    1. ElementType.PACKAGE：该注解只能声明在一个包名前。
    2. ElementType.ANNOTATION_TYPE：该注解只能声明在一个注解类型前。
    3. ElementType.TYPE：该注解只能声明在一个类前。
    4. ElementType.CONSTRUCTOR：该注解只能声明在一个类的构造方法前。
    5. ElementType.LOCAL_VARIABLE：该注解只能声明在一个局部变量前。
    6. ElementType.METHOD：该注解只能声明在一个类的方法前。
    7. ElementType.PARAMETER：该注解只能声明在一个方法参数前。
    8. ElementType.FIELD：该注解只能声明在一个类的字段前。

2. <span id="retention"> @Retention() – 什么时候使用该注解 </span>

   > 该注解告诉编译器如何管理被注解的注解，如何时遗弃等。
   > 使用方法如：`@Retention(RetentionPolicy.RUNTIME)`

   常用种类有：
    1. RetentionPolicy.SOURCE  : 注解只保留在源文件，当Java文件编译成class文件的时候，注解被遗弃；
    2. RetentionPolicy.CLASS  : 注解被保留到class文件，但jvm加载class文件时候被遗弃，这是默认的生命周期；
    3. RetentionPolicy.RUNTIME  : 注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在；

3. <span id="documented"> @Documented – 注解是否将包含在JavaDoc中 </span>

   > 该注解告诉编译器，它所注解的注解应该被javadoc工具记录。
   > 即，注解类型信息会被包括在生成的文档中。  
   >
   > 比如说，
   > > 如果没有注入这个注解，那么生成的文档类似于  
   > > `public class Test`  
   > > 如果注入了这个注解，那么就会变成
   > >
   > > ```java
   > > @API(content="xxxx")...
   > > public class Test
   > > ```

4. <span id="inherited"> @Inherited – 定义该注释和子类的关系 </span>

   > @Inherited 表示某个被标注的类型是被继承的。  
   > 如果一个使用了@Inherited 修饰的annotation 类型被用于一个class，则这个annotation 将被用于该class 的子类。

## 自定义一个注解  

自定义注解类编写的一些规则:
>
> 1. Annotation 型定义为@interface, 所有的Annotation 会自动继承java.lang.Annotation这一接口,并且不能再去继承别的类或是接口.
> 2. 参数成员只能用public 或默认(default) 这两个访问权修饰
> 3. 参数成员只能用基本类型byte、short、char、int、long、float、double、boolean八种基本数据类型和String、Enum、Class、annotations等数据类型，以及这一些类型的数组.
> 4. 要获取类方法和字段的注解信息，必须通过Java的反射技术来获取 Annotation 对象，因为你除此之外没有别的获取注解对象的方法
> 5. 注解也可以没有定义成员，不过一般不会这样。
>
> > PS:自定义注解需要使用到元注解

1. 基本框架  

    > 可以使用Spring提供的注解，如`@Transational()`

    注解的声明

    ```java
    import java.lang.annotation.ElementType;
    import java.lang.annotation.Retention;
    import java.lang.annotation.RetentionPolicy;
    import java.lang.annotation.Target;
    
    @Target({ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotation{
        //定义final静态属性
        //定义抽象方法
    }
    ```

    注解的使用

    ```java
    @TestAnnotation
    public String save(Person person){
    
    }
    ```

2. 属性
   1. 定义属性的方法

      ```java
      import java.lang.annotation.Documented;
      import java.lang.annotation.Retention;
      import java.lang.annotation.Target;
      import static java.lang.annotation.ElementType.FIELD;
      import static java.lang.annotation.RetentionPolicy.RUNTIME;
      
      /**
       * 水果名称注解
         */
      @Target(FIELD)
      @Retention(RUNTIME)
      @Documented
      public @interface FruitName { 
         String value() default "";
      }
      ```

   2. 使用

      ```java
      public class Apple {
         @FruitName("Apple")
         private String appleName;
      
         public void setAppleName(String appleName) {
            this.appleName = appleName;
         }
         public String getAppleName() {
            return appleName;
         }
      }
      ```

3. 注解组件
   1. `@Pointcut`：定义切入点
       - 用于定义切入点，即需要拦截的方法，或者类
   2. `@Around`：环绕通知
       - 用于环绕通知，即在方法执行前后执行的方法

4. 表达式标签
   1. `execution()`：匹配方法的执行
      - 格式：`execution(modifiers-pattern? ret-type-pattern declaring-type-pattern? name-pattern(param-pattern) throws-pattern?)`
      - 带有？的表示可选
      - `modifiers-pattern`：方法的修饰符，如public, private等
      - `ret-type-pattern`：方法的返回值类型
      - `declaring-type-pattern`：方法所在的类的全路径
      - `name-pattern`：方法的名称
      - `param-pattern`：方法的参数
        - ()：表示一个参数
        - (..)：表示所有参数
        - (,String)：表示第一个参数为任意，第二个参数为String
      - `throws-pattern`：方法抛出的异常
      - 例子：
         - `execution(* com.abc.service.*.*(..))`：匹配com.abc.service包下的所有类的所有方法
         - `execution(public * *(..))`：匹配所有public方法
   2. `args()`：匹配当前方法参数为指定类型的方法
   3. `this()`：匹配当前AOP代理对象类型满足条件的类型的方法
   4. `target()`：匹配当前目标对象类型的方法
   5. `within()`：匹配指定类型内的方法
   6. `@annotation`：匹配带有指定注解的方法
   7. `@within`：匹配带有指定注解的类内的方法
   8. `@args`：匹配当前方法的参数上的注解
   9. `@target`：匹配目标对象的类上的注解
   10. 支持的通配符：
       1. `*`：匹配任意数量字符
       2. `..`：匹配任意数量字符的重复
       3. `+`：匹配指定类型的子类型
   11. 括号内内容如果是接口，那么匹配的是实现了该接口的类；如果是类，那么匹配的是该类；如果是注解，那么匹配的是带有该注解的类或方法
   12. 可以使用`&&`，`||`，`!`来组合表达式
