# Spring Event

Spring事件机制是基于观察者模式实现的。在该模式中，存在两种角色：事件源（Publisher）和事件监听器（Listener）。事件源负责发布事件，而事件监听器则负责订阅并处理事件。

> 核心接口
>
> | 名称 | 说明 |
> |:----|:----|
> | ApplicationEvent | Spring事件的基本接口 |
> | ApplicationListener | 事件监听器接口 |
> | ApplicationEventPublisher | 事件发布器接口 |

## 简单样例

1. 创建事件

    ```java
    import org.springframework.context.ApplicationEvent;

    public class MyEvent extends ApplicationEvent {
        public MyEvent(Object source) {
            super(source);
        }
    }
    ```

2. 创建事件监听器

    ```java
    import org.springframework.context.ApplicationListener;
    import org.springframework.stereotype.Component;

    @Component
    public class MyListener implements ApplicationListener<MyEvent> {
        @Override
        public void onApplicationEvent(MyEvent event) {
            System.out.println("监听到事件：" + event);
        }
    }
    ```

3. 发布事件

    ```java
    import org.springframework.context.ApplicationEventPublisher;
    import org.springframework.stereotype.Component;

    @Component
    public class MyPublisher {
        private final ApplicationEventPublisher publisher;

        public MyPublisher(ApplicationEventPublisher publisher) {
            this.publisher = publisher;
        }

        public void publish() {
            publisher.publishEvent(new MyEvent(this));
        }
    }
    ```

4. 测试

    ```java
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.context.ConfigurableApplicationContext;

    @SpringBootApplication
    public class Application {
        public static void main(String[] args) {
            ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
            MyPublisher publisher = context.getBean(MyPublisher.class);
            publisher.publish();
        }
    }
    ```

## Event

## Publisher

## Listener
