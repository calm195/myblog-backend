# BeanFactory

管理Bean的工厂，是Spring的核心容器之一  
提供最核心最基础的功能，是Spring的基础设施

## 核心API

| 名称                                          | 说明            |
|:--------------------------------------------|:--------------|
| getBean(String name)                        | 根据名称获取Bean    |
| getBean(Class<T> requiredType)              | 根据类型获取Bean    |
| getBean(String name, Class<T> requiredType) | 根据名称和类型获取Bean |
| containsBean(String name)                   | 判断是否包含Bean    |
| isSingleton(String name)                    | 判断是否是单例Bean   |
| isPrototype(String name)                    | 判断是否是原型Bean   |
| getType(String name)                        | 获取Bean的类型     |
| getBeanProvider(Class<T> requiredType)      | 获取Bean的提供者    |
