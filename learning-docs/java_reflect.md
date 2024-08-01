# Java Reflect

Java反射是Java语言的一种特性，可以在运行时检查类、方法、字段等信息，并在运行时获取类的信息。Java反射提供了以下功能：

1. 获取类信息：获取类的构造方法、字段、方法等信息。
2. 获取字段信息：获取字段的类型、修饰符、注解等信息。
3. 获取方法信息：获取方法的参数、返回值、修饰符、注解等信息。
4. 调用方法：通过反射调用类的方法。
5. 创建对象：通过反射创建类的对象。
6. 动态代理：通过反射实现动态代理。
7. 获取注解：获取类、字段、方法的注解信息。

## Class

Java程序运行时，JRE会对所有对象进行RTTI（Run-Time Type Identification），即运行时类型识别。而每个类对应的RTTI信息就是Class对象

> 多态就是基于RTTI实现的。
> 在类加载阶段，类加载器首先检查这个类的Class对象是否已经加载，如果没有加载，类加载器会根据类的全限定名查找并加载这个类的Class对象：查找.class文件，读取并验证.class文件，生成Class对象。
> Class对象是唯一的，即一种类只有一个Class对象。

1. 获取Class对象
    - `Class<?> clazz = Class.forName("com.example.User");` 通过类名获取Class对象
    - `Class<?> clazz = User.class;` 通过类获取Class对象
        > 通过这种方式获取Class对象时，不会初始化类，即不会执行静态代码块。
        > 当使用这个对象时，会将类加载到内存中，执行静态代码块。
    - `Class<?> clazz = obj.getClass();` 通过对象获取Class对象
2. Class类的方法
   > （带有Declared只能获取本类的所有属性，不带Declared的获取本类和父类的Public属性
   1. 获取构造方法（只能获取本类的构造方法，不能获取父类的构造方法）
        - `Constructor<?>[] constructors = clazz.getConstructors();` 获取所有public构造方法
        - `Constructor<?>[] constructors = clazz.getDeclaredConstructors();` 获取所有构造方法
   2. 获取字段
        - `Field[] fields = clazz.getFields();` 获取所有public字段
        - `Field[] fields = clazz.getDeclaredFields();` 获取所有字段
   3. 获取方法
        - `Method[] methods = clazz.getMethods();` 获取所有public方法
        - `Method[] methods = clazz.getDeclaredMethods();` 获取所有方法
   4. 获取注解
        - `Annotation[] annotations = clazz.getAnnotations();` 获取所有注解
   5. 创建对象
        - `Object obj = clazz.newInstance();` 创建对象

## Field

Field类是Java反射中的一个类，用于描述类或接口的字段信息。

1. 取出Field
    - `Field[] fields = clazz.getFields();` 获取所有public字段
    - `Field[] fields = clazz.getDeclaredFields();` 获取所有字段
2. 获取Field属性
    - `String name = field.getName();` 获取字段名
    - `int modifiers = field.getModifiers();` 获取修饰符
      - `String modifier = Modifier.toString(modifiers);` 获取修饰符字符串
    - `Class<?> type = field.getType();` 获取字段类型
    - `String typeName = field.getType().getName();` 获取字段类型名
    - `String simpleName = field.getType().getSimpleName();` 获取字段类型简称
    - `Object value = field.get(obj);` 获取字段值

## Method

Method类是Java反射中的一个类，用于描述类或接口的方法信息。

1. 取出Method
    - `Method[] methods = clazz.getMethods();` 获取所有public方法
    - `Method[] methods = clazz.getDeclaredMethods();` 获取所有方法
2. 获取Method属性
    - `String name = method.getName();` 获取方法名
    - `int modifiers = method.getModifiers();` 获取修饰符
      - `String modifier = Modifier.toString(modifiers);` 获取修饰符字符串
    - `<T extends Annotation> T getAnnotation(Class<T> annotationClass);` 获取注解
    - `Annotation[] annotations = method.getAnnotations();` 获取所有注解
    - `Class<?> returnType = method.getReturnType();` 获取返回值类型
    - `Class<?>[] getExceptionTypes();` 获取异常类型

## Modifier

Modifier类是Java反射中的一个类，用于描述修饰符信息。

1. 获取修饰符
    - `int modifiers = field.getModifiers();` 获取修饰符
    - `String modifier = Modifier.toString(modifiers);` 获取修饰符字符串
2. 判断
    - `boolean isPublic(int modifiers);` 判断是否public
    - `boolean isPrivate(int modifiers);` 判断是否private
    - `boolean isProtected(int modifiers);` 判断是否protected
    - `boolean isStatic(int modifiers);` 判断是否static
    - `boolean isFinal(int modifiers);` 判断是否final
    - `boolean isSynchronized(int modifiers);` 判断是否synchronized
    - `boolean isVolatile(int modifiers);` 判断是否volatile
    - `boolean isTransient(int modifiers);` 判断是否transient
    - `boolean isNative(int modifiers);` 判断是否native
    - ...
3. 修饰符
    - `public static final int PUBLIC = 0x00000001;` public
    - `public static final int PRIVATE = 0x00000002;` private
    - `public static final int PROTECTED = 0x00000004;` protected
    - `public static final int STATIC = 0x00000008;` static
    - `public static final int FINAL = 0x00000010;` final
    - `public static final int SYNCHRONIZED = 0x00000020;` synchronized
    - `public static final int VOLATILE = 0x00000040;` volatile
    - `public static final int TRANSIENT = 0x00000080;` transient
    - `public static final int NATIVE = 0x00000100;` native
    - `public static final int INTERFACE = 0x00000200;` interface
    - `public static final int ABSTRACT = 0x00000400;` abstract
    - `public static final int STRICT = 0x00000800;` strictfp

## Parameter

Parameter类是Java反射中的一个类，用于描述方法的参数信息。

## Constructor

Constructor类是Java反射中的一个类，用于描述类的构造方法信息。Constructor类提供了以下方法：

## Array

Array类是Java反射中的一个类，用于操作数组。

