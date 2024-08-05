# Spring Bean

Spring Bean是Spring框架的核心，是由Spring IoC容器管理的对象。Spring IoC容器负责实例化、配置和组装Bean。

## Bean的生命周期

Spring Bean的生命周期包括以下阶段：

1. 加载Bean
   1. 通过BeanDefinitionReader读取BeanDefinition。
   2. BeanDefinition注册到BeanDefinitionRegistry。
   3. 注册后置增强器，如BeanPostProcessor，BeanFactoryPostProcessor。
   4. 合并，由子向父合并BeanDefinition。
2. 实例化Bean
    1. 前置处理
    2. 实例化：通过反射机制实例化Bean。
    3. 后置处理
    4. 属性修改补充
3. 填充属性
   1. 用户自定义属性填充
   2. 容器属性赋值（执行实现了Aware接口的方法）
4. 初始化Bean
    1. Bean Ware接口回调，如BeanNameAware、BeanFactoryAware、ApplicationContextAware。设置Bean的名称、BeanFactory、ApplicationContext。
    2. BeanPostProcessor前置处理
    3. 初始化方法调用：执行Bean的初始化方法，可以通过`@PostConstruct`注解或`init-method`配置，或者定义时指明`@Bean(initMethod="init")`。
    4. BeanPostProcessor后置处理
5. Bean使用
6. 销毁Bean
    1. `@PreDestroy`注解
    2. 容器关闭后：调用destroy方法，需要实现`DisposableBean`接口
    3. 调用`@Bean(destroyMethod="destroy")`。
    4. 非单例模式则跳过上述过程，交给用户处理。

## Bean的作用域

Spring Bean的作用域决定了Bean的生命周期。Spring Bean的作用域包括以下几种：

1. singleton：单例模式，Spring IoC容器只创建一个Bean实例。
2. prototype：原型模式，Spring IoC容器每次请求时创建一个新的Bean实例。
3. request：请求模式，Spring IoC容器为每个HTTP请求创建一个Bean实例。
4. session：会话模式，Spring IoC容器为每个HTTP会话创建一个Bean实例。

## Bean的配置

Spring Bean的配置包括以下几种方式：

1. XML配置：通过XML配置文件配置Bean。
2. Java配置：通过Java配置类配置Bean。
3. 注解配置：通过注解配置Bean。
4. Groovy配置：通过Groovy配置文件配置Bean。
5. Properties配置：通过Properties配置文件配置Bean。
6. YAML配置：通过YAML配置文件配置Bean。

## BeanDefinition

描述Bean实例的配置信息，包括Bean的类名、作用域、属性、构造器参数等。
主要目的是为了让BeanFactory创建Bean实例以及BeanFactoryPostProcessor修改Bean实例。

参数说明：

| 参数名 | 说明 |
|:-----|:----|
| ROLE_APPLICATION | 应用程序级别的Bean定义 |
| ROLE_INFRASTRUCTURE | 基础设施级别的Bean定义 |
| ROLE_SUPPORT | 支持级别的Bean定义 |
| SCOPE_SINGLETON | 单例模式 |
| SCOPE_PROTOTYPE | 原型模式 |

常用方法：

| 方法名 | 说明 |
|:-----|:----|
| String getBeanClassName() | 获取Bean的类名 |
| String getScope() | 获取Bean的作用域 |
| String[] getDependsOn() | 获取Bean的依赖 |
| String getParentName() | 获取Bean的父Bean名 |
| boolean isSingleton() | 判断Bean是否为单例模式 |
| boolean isPrototype() | 判断Bean是否为原型模式 |

## BeanDefinitionReader

Bean定义读取器，负责读取Bean定义文件，将Bean定义解析为BeanDefinition对象。

实现类：

1. XmlBeanDefinitionReader：基于XML文件读取解析xml文件，通过Parser解析xml文件的标签。针对beans标签，生成对应的BeanDefintions，然后注册到BeanFactory中。
2. AnnotationBeanDefinitionReader：读取注解配置的Bean。典型使用场景是读取@Component、@Service、@Repository、@Controller等注解。
3. ClassPathBeanDefinitionScanner：扫描指定包下的类是否存在@Component及其子注解，生成BeanDefinition，然后注册到BeanFactory中。
4. ConfigurationClassBeanDefinitionReader：处理@Configuration注解的配置类，加在这些配置类上面的注解，即与@Configuration一起使用的注解，如@ComponentScan，@PropertySource，@Import，@Profile等。

## BeanDefinitionRegistry

Bean定义注册器，负责BeanFactory的Bean定义注册与删除。

- SingletonBeanRegistry：单例Bean注册器，负责单例Bean的注册与获取。

## BeanFactory

Spring IoC容器的基础接口，负责管理Bean的生命周期。

实现类：

1. DefaultListableBeanFactory：默认的BeanFactory实现类，负责管理Bean的生命周期。提供BeanDefinition的存储map，Bean对象的存储map。
2. StaticListableBeanFactory：用于存储给定的Bean对象实例，不支持动态注册，是ListableBeanFactory的一个实现。

子接口：

1. ConfigurableBeanFactory：可配置的BeanFactory，提供了设置BeanClassLoader、设置Bean表达式解析器、设置BeanPostProcessor等方法。

## BeanFactoryPostProcessor

Bean工厂后置处理器。在BeanFactory标准初始化之后调用，所有Bean定义已经加载，但是Bean实例还未创建。
作用：

1. 加载更多的Bean定义。使用ConfigurationClassPostProcessor加载@Configuration注解的配置类。
2. 对Bean定义进行修改，补充。比如使用PropertyPlaceholderConfigurer替换占位符。

## BeanPostProcessor

Bean后置处理器。在Bean实例化、配置、初始化之后调用，对Bean实例进行增强处理。
核心方法：

1. postProcessBeforeInitialization：创建好Bean实例，但是还未初始化时调用。
2. postProcessAfterInitialization：Bean实例初始化完成后调用。
