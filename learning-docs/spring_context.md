# Context

## ApplicationContext  

由BeanFactory派生而来。  
被称为应用上下文，是Spring的核心容器。
特点：
    1. 一个应用只会创建一个ApplicationContext对象
    2. 线程安全
    3. 提供了更多的功能，如国际化、事件传播、资源加载等
    4. 会预先实例化单例Bean

### 常见实现

1. ClassPathXmlApplicationContext：从类路径加载配置文件
2. FileSystemXmlApplicationContext：从文件系统加载配置文件
3. AnnotationConfigApplicationContext：基于注解的配置
4. WebApplicationContext：Web应用上下文
5. GenericWebApplicationContext：泛型Web应用上下文
6. GenericApplicationContext：泛型应用上下文

## ApplicationContextAware

放弃了一些控制反转，将注入容器的控制权交给了Bean本身。  
> 即，Bean自己可以通过实现ApplicationContextAware接口，获取ApplicationContext对象，从而获取其他Bean。
> 如调用`applicationContext.getBean("beanName")`获取其他Bean。

### 使用方法

1. 实现ApplicationContextAware接口

2. 重写`setApplicationContext(ApplicationContext applicationContext)`方法

    ```java
    import org.springframework.context.ApplicationContext;
    import org.springframework.context.ApplicationContextAware;

    public class Test implements ApplicationContextAware {
        private ApplicationContext applicationContext;

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }
    }
    ```

3. 在配置文件中配置Bean (可选)

    ```xml
    <bean id="Other" class="com.example.Other"/>
    ```

4. 获取其他Bean

    ```java
    Other other = (Other) applicationContext.getBean("Other");
    ```

### 原理

1. Spring在初始化Bean时，会检查Bean是否实现了ApplicationContextAware接口
2. 如果实现了，会调用`setApplicationContext(ApplicationContext applicationContext)`方法
3. 通过该方法，Bean可以获取ApplicationContext对象
4. 通过ApplicationContext对象，Bean可以获取其他Bean
