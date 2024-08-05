# Spring Boot中的日志

Spring Boot默认使用Logback作为日志框架，但也支持使用其他日志框架，如Log4j2、Log4j、JUL等。Spring Boot提供了一个`LoggingSystem`接口，用于支持不同的日志框架。Spring Boot的日志配置文件`application.properties`或`application.yml`中的`logging.config`属性用于指定日志配置文件的位置，如果不指定则使用默认的配置。Spring Boot还提供了`logging.level`属性用于设置日志级别，`logging.file`属性用于设置日志文件的位置。

## Logback

[Logback](http://logback.qos.ch) is a logging framework that is intended as a successor to the popular log4j project. It is divided into three modules, logback-core, logback-classic and logback-access.

logback-core为基础核心，另外两个均依赖它。其中logback-classic实现了简单日志门面SLF4J；logback-access主要作为一个与Servlet容器交互的模块，提供与HTTP访问相关的一些功能。

通常使用时直接引入logback-classic的依赖，便可自动引入logback-core，当然为保险起见也可以显式的引入两者。Spring Boot已经将logback做为默认集成的日志框架，所以在Spring Boot项目中无需额外引入logback依赖。

1. 依赖
    - 非Spring Boot项目中引入logback依赖：

        ```xml
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
        </dependency>
        ```

    - Spring Boot项目中无需引入logback依赖。

        ```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        ```

2. 简单使用

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

3. 配置
   1. application.properties

        ```properties
        # 日志级别，指定包名的日志级别，logging.level.* = LEVEL
        # LEVEL: TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF
        logging.level.root=INFO
        logging.level.org.springframework=INFO
        logging.level.com.example=DEBUG

        # 日志文件和日志目录不可同时设置，同时设置时日志文件优先
        # 日志文件
        logging.file=/logs/myapp.log
        # 日志目录，会在该目录下生成spring.log文件
        logging.path=/var/log
        ```

   2. xml配置
        如果logback.xml和logback-spring.xml同时存在，logback-spring.xml会优先加载。
        Spring Boot优先使用带有-spring后缀的配置文件。
        - logback-spring.xml
        - logback.xml
        - logback-spring.groovy
        - logback.groovy

   3. xml属性详解
      1. configuration
         - scan：是否自动扫描配置文件的变化，默认为true，如果改变会重新加载。
         - scanPeriod：扫描配置文件的时间间隔，默认单位为毫秒，默认为1分钟。
         - debug：是否开启debug模式，默认为false，可以打印出logback内部日志信息。
      2. contextName：设置上下文名称。经过定义之后，在其他property属性或appender中便可通过“%contextName”来获取和使用该上下文名称了。
      3. property：定义属性，可以在配置文件中定义一些变量，然后在其他地方引用。它有name和value两个属性。变量可以使“${name}”来使用变量。作用类似于代码中的常量字符串，定义之后公共地方便可以统一使用。如日志文件名称前缀、日志路径、日志输出格式等。
      4. springProperty：Spring项目特有，可以通过配置文件读取value值。  `<springProperty scope="context" name="springAppName" source="spring.application.name"/>`
      5. appender：定义日志输出格式，日志如何过滤以及日志文件的处理。
         - name：定义appender的名称。
         - class：定义appender的实现类，需要填入实现类的完全限定名。默认提供了几种appender。自定义实现appender时，需要继承AppenderBase抽象类。
           - ConsoleAppender：控制台输出，完全限定名为ch.qos.logback.core.ConsoleAppender。可以通过设置encoder属性来定义输出格式。具体格式定义使用pattern属性。

                ```xml
                <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
                    <encoder>
                        <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
                    </encoder>
                </appender>
                ```

           - FileAppender：文件输出，完全限定名为ch.qos.logback.core.FileAppender。
           - RollingFileAppender：滚动文件输出，完全限定名为ch.qos.logback.core.rolling.RollingFileAppender。
             - encoder：定义输出格式与编码等。如pattern属性定义输出格式，charset属性定义编码格式。
             - file：定义日志文件路径+文件名。可以使用变量定义路径，如“${logPath}/myapp.log”。
             - filter：定义过滤器，可以定义多个过滤器。
               - ThresholdFilter：阈值过滤器，过滤低于指定临界值的日志，完全限定名为ch.qos.logback.classic.filter.ThresholdFilter。
               - LevelFilter：级别过滤器，过滤指定级别的日志，完全限定名为ch.qos.logback.classic.filter.LevelFilter。level指定日志级别，onMath指定符合过滤条件的操作接收（ACCEPT），onMismatch指定不符合条件的拒绝（DENY）。

               ```xml
               <!-- 过滤器，只记录debug级别的日志 -->
                <filter class="ch.qos.logback.classic.filter.LevelFilter">
                    <level>DEBUG</level>
                    <OnMismatch>DENY</OnMismatch>
                    <OnMatch>ACCEPT</OnMatch>
                </filter>
                ```

             - RollingPolicy：定义滚动策略，可以定义多个滚动策略。
               - TimeBasedRollingPolicy：基于时间的滚动策略，完全限定名为ch.qos.logback.core.rolling.TimeBasedRollingPolicy。fileNamePattern定义日志文件名格式，如“${logPath}/myapp.%d{yyyy-MM-dd}.log”。maxHistory定义保留的日志文件最大天数。
               - SizeAndTimeBasedRollingPolicy：基于时间和大小的滚动策略，完全限定名为ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy。maxFileSize定义单个日志文件的最大大小，totalSizeCap定义日志文件总大小上限，fileNamePattern定义日志文件名格式。
               - FixedWindowRollingPolicy：固定窗口滚动策略，完全限定名为ch.qos.logback.core.rolling.FixedWindowRollingPolicy。minIndex定义最小索引，maxIndex定义最大索引，fileNamePattern定义日志文件名格式。
               - SizeBasedTriggeringPolicy：基于大小触发策略，完全限定名为ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy。maxFileSize定义单个日志文件的最大大小。
      6. logger：定义日志记录器，可以定义多个日志记录器。
         - name：要输出日志的包名或类名，必选，可以使用通配符“*”。
         - level：定义日志级别，可以定义多个级别，如“DEBUG, INFO, WARN, ERROR”，可选，缺省时向上继承。
         - additivity：是否将日志向上级传递，默认为true。
         - appender-ref：定义日志记录器的appender，可以定义多个appender。ref属性指向appender的name属性。
      7. root：定义根日志记录器，所有logger的根节点，只能有一个。设置属性debug为true时，会打印logback内部日志信息。
         - level：定义日志级别，默认为Debug。
         - appender-ref：定义日志记录器的appender，可以定义多个appender。ref属性指向appender的name属性。
      8. springProfile：Spring项目特有，可以根据Spring的profile来定义不同的日志输出。可以定义多个springProfile。

         ```xml
            <!-- 开发环境 -->
            <springProfile name="dev">
                <logger name="com" level="debug" />
                <root level="info">
                    <appender-ref ref="CONSOLE" />
                </root>
            </springProfile>
            <!-- 测试环境+生产环境 -->
            <springProfile name="test,prod">
                <logger name="com" level="info" />
                <root level="info">
                    <appender-ref ref="INFO_FILE" />
                    <appender-ref ref="ERROR_FILE" />
                </root>
            </springProfile>
            ```

      9. statusListener：定义状态监听器，输出logback的内部状态信息。完全限定名为ch.qos.logback.core.status.OnConsoleStatusListener。

4. 日志级别

    - TRACE：最详细的日志信息，通常只在开发和调试时使用。
    - DEBUG：详细的日志信息，通常用于调试。
    - INFO：一般的信息，通常用于生产环境中输出重要信息。
    - WARN：警告信息，表示可能出现问题。
    - ERROR：错误信息，表示出现严重问题。
    - FATAL：致命错误，表示程序无法继续运行。

5. Appender

    实现的Appender必须继承 ch.qos.logback.core.Appender 接口。
    doAppend可以说是logback框架最重要的部分，它负责将日志按照一定格式输出到指定设备。Appender最终都会负责输出日志，但是他们也可能将日志格式化的工作交给Layout，或者Encoder对象。
    每一个Layout、Encoder只属于一个Appender。有些appender内置了固定的日志格式，所以并不需要layout/encoder声明。

    ```java
    package ch.qos.logback.core;

    import ch.qos.logback.core.spi.ContextAware;
    import ch.qos.logback.core.spi.FilterAttachable;
    import ch.qos.logback.core.spi.LifeCycle;

    public interface Appender<E> extends LifeCycle, ContextAware, FilterAttachable {

        public String getName();
        public void setName(String name);
        void doAppend(E event);

    }
    ```

    **AppenderBase**：实现了Appender接口的抽象类，提供了一些通用的方法，如添加过滤器、获取上下文等。类定义：
    `abstract public class AppenderBase<E> extends ContextAwareBase implements Appender<E>`
    方法：
    - `synchronized void doAppend(E event)`：实现Appender接口的方法，负责将日志输出到指定设备。
    - `protected abstract void append(E event)`：必须实现，具体逻辑由子类实现。

6. 事件

logback-classic模块只会处理实现了`ch.qos.logback.classic.spi.ILoggingEvent`接口的事件。

## SLF4J(Simple logging facade for Java)  

不是一个真正的日志实现，而是一个抽象层。  
只要实现了相关的接口，那么可以在后台使用任意一个日志类库。

1. 依赖  

    maven依赖：

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

2. 用法

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

## Log4j2

## MDC

MDC（Mapped Diagnostic Context）是Log4j和Logback提供的一种方便在多线程环境中记录日志的功能。MDC是一个Map结构，可以看作是一个与当前线程绑定的哈希表。它可以存储一些上下文信息，如用户ID、请求ID等，这些信息会被自动添加到日志中。MDC的信息是线程私有的，不会被其他线程访问。

常用方法：

- `put(String key, String value)`：向MDC中添加键值对。
- `get(String key)`：获取MDC中指定键的值。
- `remove(String key)`：从MDC中移除指定键。
- `clear()`：清空MDC。
