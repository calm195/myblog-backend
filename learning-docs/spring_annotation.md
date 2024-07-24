# Spring框架下常见注解

## 元注解

注解的注解，即可以用来修饰注解的注解。  
java.lang.annotation 提供了四种元注解，专门用于注解其他的注解（在自定义注解的时候，需要使用到元注解）  
分别是：  
[@Target](#target) – 注解用于什么地方  
[@Retention](#retention) – 什么时候使用该注解  
[@Documented](#documented) – 注解是否将包含在JavaDoc中  
@Inherited – 是否允许子类继承该注解  

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
   > > ```java
   > > @API(content="xxxx")...
   > > public class Test
   > > ```

## 自定义一个注解  
1. 基本框架  
    > 也可以使用Spring提供的注解，如`@Transational()`
   
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
3. 方法
