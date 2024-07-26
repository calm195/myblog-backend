# SLF4J(Simple logging facade for Java)  

不是一个真正的日志实现，而是一个抽象层。  
只要实现了相关的接口，那么可以在后台使用任意一个日志类库。

## 依赖  

1. maven依赖：

    ```xml
    <dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>xxx</version>
    </dependency>

    <dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>xxx</version>
    </dependency>
    ```

## 用法

1. 简单用法

    ```java
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    public class Example {
        private static final Logger log = LoggerFactory.getLogger(Example.class);

        public static void main(String[] args) {
            String name = "John";
            int age = 30;

            // 使用占位符
            log.info("User information: Name = {}, Age = {}", name, age);
        }
    }
    ```

2. 带有变量和表达式的占位符

    ```java
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    public class Example {
        private static final Logger log = LoggerFactory.getLogger(Example.class);

        public static void main(String[] args) {
            String name = "John";
            int age = 30;

            // 带有变量和表达式的占位符
            log.info("User information: Name = {}, Age = {}, Is Adult = {}", name, age, age >= 18);
        }
    }
    ```

3. 使用参数索引的占位符

    ```java
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;

    public class Example {
        private static final Logger log = LoggerFactory.getLogger(Example.class);

        public static void main(String[] args) {
            String name = "John";
            int age = 30;

            // 使用参数索引的占位符
            log.info("User information: Name = {1}, Age = {0}", age, name);
        }
    }
    ```
