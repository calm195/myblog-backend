# Spring AOP (Aspect Orient Programmin)

面向切面编程,AOP 是一种编程思想，是面向对象编程（OOP）的一种补充。  
面向切面编程，在不修改源代码的情况下给程序动态统一添加额外功能的一种技术。  
应用主要体现在事务处理、日志管理、权限控制、异常处理等方面。

## 核心概念

| 名称        | 说明                                   |  
|:----------|:-------------------------------------|  
| joinpoint | 连接点，被拦截到的点，在Spring中，指可以被动态代理拦截目标类的方法 |
| pointcut  | 切入点，指定拦截范围                           |
| advice    | 通知，拦截到joinpoint后做的事情，如写入日志等          |
| target    | 目标，代理的目标对象                           |
| weaving   | 植入，把增强代码应用到目标上，生成代理对象的过程             |
| proxy     | 代理，指生成的代理对象                          |
| Aspect    | 切面，切入点和通知的结合                         |

## 常见注解

1. @Aspect:作用是把当前类标识为一个切面供容器读取
2. @Pointcut：Pointcut是植入Advice的触发条件。  
   > 每个Pointcut的定义包括2部分，一是表达式，二是方法签名。  
   > 方法签名必须是 public及void型。  
   > 可以将Pointcut中的方法看作是一个被Advice引用的助记符，因为表达式不直观，因此我们可以通过方法签名的方式为 此表达式命名。  
   > 因此Pointcut中的方法只需要方法签名，而不需要在方法体内编写实际代码。

3. @Around：环绕增强，相当于MethodInterceptor
4. @AfterReturning：后置增强，相当于AfterReturningAdvice，方法正常退出时执行
5. @Before：标识一个前置增强方法，相当于BeforeAdvice的功能
6. @AfterThrowing：异常抛出增强，相当于ThrowsAdvice
7. @After: final增强，不管是抛出异常或者正常退出都会执行

## JoinPoint

JoinPoint是Spring框架中的一个接口，表示一个连接点，可以通过它访问到目标对象的方法和参数。

| 方法名 | 说明 |
|:-----|:----|
| Object[] getArgs() | 获取目标方法的参数 |
| Signature getSignature() | 获取目标方法的签名对象 |
| Object getTarget() | 获取目标对象 |
| Object getThis() | 获取代理对象 |

1. ProceedingJoinPoint：继承JoinPoint，只用在@Around增强中，表示可以执行目标方法
    - `Object proceed() throws Throwable`：执行目标方法
    - `Object proceed(Object[] args) throws Throwable`：执行目标方法，传入新的参数

## Signature

Signature是Spring框架中的一个接口，表示方法或构造函数的签名信息。

1. MethodSignature
    - `Method getMethod()`：获取目标方法
    - `String getName()`：获取方法名
    - `Class<?> getReturnType()`：获取返回值类型
    - `Class<?>[] getParameterTypes()`：获取参数类型
2. ConstructorSignature

## ParameterNameDiscoverer

ParameterNameDiscoverer是Spring框架中的一个接口，用于获取方法参数名。

常用方法：

1. `String[] getParameterNames(Method method)`：获取方法参数名
2. `String[] getParameterNames(Constructor<?> ctor)`：获取构造函数参数名

默认实现：

1. PrioriParameterNameDiscoverer：管理Spring中注册的所有ParameterNameDiscoverer实现类，内部维护一个List，按照注册顺序依次调用
    - `void addDiscoverer(ParameterNameDiscoverer pnd)`：添加ParameterNameDiscoverer实现类
2. DefaultParameterNameDiscoverer：PriorityParameterNameDiscoverer的子类，默认注册了三个解析器
   1. StandardReflectionParameterNameDiscoverer：使用JDK的反射API获取参数名
   2. LocalVariableTableParameterNameDiscoverer：使用ASM库获取参数名
   3. KotlinReflectionParameterNameDiscoverer：使用Kotlin反射API获取参数名

## ReflectionUtils

ReflectionUtils是Spring框架中的一个工具类，用于反射操作。

1. 成员变量
    - `static final Field[] NO_FIELDS`：空Field数组
    - `static final Method[] NO_METHODS`：空Method数组
    - `Map<Class<?>, Field[]> declaredFieldsCache`：缓存类的所有Field
    - `Map<Class<?>, Method[]> declaredMethodsCache`：缓存类的所有Method
    - `CGLIB_RENAMED_METHOD_PREFIX = "CGLIB$"`：CGLIB重命名方法前缀
2. 方法
    1. Filed
        - `static Field findField(Class<?> clazz, String name)`：查找Field
        - `static void doWithFields(Class<?> clazz, FieldCallback fc)`：遍历类的所有Field
