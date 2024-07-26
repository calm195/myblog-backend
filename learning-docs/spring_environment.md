# environment

## Environment

## Binder

可以将配置文件中的属性值绑定到一个对象上。

### 使用方法

1. 创建一个配置类，用于绑定配置文件中的属性值

    ```java
    public class Config {
        private String name;
        private int age;

        // getter and setter
    }
    ```

2. 创建一个Binder对象

    ```java
    Config config = new Config();
    Binder binder = Binder.get(environment);
    binder.bind("test", Bindable.ofInstance(config));

    // 或者
    String name = binder.bind("test.name").get();
    String name = binder.bind("test.name", String.class).orElse("default");
    int age = binder.bind("test.age").get();
    int age = binder.bind("test.age", Integer.class).orElse(0);
    ```

3. 在配置文件中配置属性值

    ```properties
    test.name=张三
    test.age=18
    ```

4. 使用绑定后的对象

    ```java
    System.out.println(config.getName());
    System.out.println(config.getAge());
    ```

## EnvironmentAware

凡注册到Spring容器内的bean，实现了EnvironmentAware接口重写setEnvironment方法后，在工程启动时可以获得application.properties的配置文件配置的属性值。
> 具体细节过程：当一个 Bean 实现了 EnvironmentAware 接口时，在该 Bean 实例被实例化后，Spring 容器会调用 setEnvironment 方法，并将该 Bean 所在的环境作为参数传递进去。
> 通过 Environment 可以获取到系统属性、环境变量、配置文件中的属性等信息。

### 使用方法

1. 实现EnvironmentAware接口
2. 重写`setEnvironment(Environment environment)`方法

    ```java
    import org.springframework.context.EnvironmentAware;
    import org.springframework.core.env.Environment;

    public class Test implements EnvironmentAware {
        private Environment environment;

        @Override
        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }

        public void test() {
            String property = environment.getProperty("test.property");
            System.out.println(property);
        }
    }
    ```
