# jackson

json序列化工具
> 优点：可以自定义序列器

核心模块：

- `json-core`：核心功能，提供基于流的解析器和生成器
- `json-annotations`：注解，提供注解功能
- `json-databind`：数据绑定，基于“对象绑定-树模型”和“对象绑定-树模型-流模型”两种模式的解析绑定

## 依赖

当引入`jackson-databind`时，会自动引入`jackson-core`和`jackson-annotations`，所以只需要引入`jackson-databind`即可

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-core</artifactId>
    <version>2.13.2</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-annotations</artifactId>
    <version>2.13.2</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.13.2</version>
</dependency>
```

## ObjectMapper

`ObjectMapper`是jackson的核心类，用于序列化和反序列化对象。

配置：

- `ObjectMapper configure(DeserializationFeature feature, boolean state)`：配置反序列化特性
- `ObjectMapper configure(SerializationFeature feature, boolean state)`：配置序列化特性
- `ObjectMapper setSerializationInclusion(JsonInclude.Include inclusion)`：设置序列化时包含的字段
- `ObjectMapper setDateFormat(DateFormat dateFormat)`：设置日期格式
- `ObjectMapper setDefaultPropertyInclusion(JsonInclude.Value defaultInclusion)`：设置默认的序列化属性

方法：

- `T readValue(String content, Class<T> valueType)`：将json字符串转化为指定类型
- `void writeValue(Writer w, Object value)`：将对象序列化为json字符串

## 常用注解

| 注解 | 说明 | 用法 |
| --- | --- | --- |
| `@JsonProperty` | 将属性的名称序列化为另一个名称 | `@JsonProperty("name")` |
| `@JsonFormat` | 格式化日期 | `@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")` |
| `@JsonIgnore` | 忽略属性 | `@JsonIgnore` |
| `@JsonPropertyOrder` | 指定属性序列化的顺序 | `@JsonPropertyOrder({"name", "age"})` |
| `@JsonIgnoreProperties` | 忽略指定属性 | `@JsonIgnoreProperties(value = {"xx"})` |
| `@JsonNaming` | 指定属性命名策略 | `@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)` |

## 序列化规则

默认：

- 如果属性修饰符为`public`，则可序列化和反序列化
- 如果属性具有public的`getter`和`setter`方法，则可序列化和反序列化
  - `getter`方法用于序列化
  - `setter`方法用于反序列化

> 如果需要修改默认的序列化规则，调用`ObjectMapper`的`setVisibility`方法

## 自定义序列化器

序列化：

1. 创建一个类继承`JsonSerializer`类
2. 重写`serialize`方法

反序列化：

1. 创建一个类继承`JsonDeserializer`类
2. 重写`deserialize`方法

## 树模型 [todo]

## 入门使用流程

1. 引入依赖

    ```xml
    <dependency>
          <groupId>com.fasterxml.jackson.core</groupId>
          <artifactId>jackson-databind</artifactId>
          <version>2.13.2</version>
    </dependency>
    ```

2. 使用`ObjectMapper`类

`readValue(string, classType)`：将传入的json字符串转化为指定类型，返回类型实例  
`writeValueAsString(object)`：将输入的实例对象转化为Json字符串，返回字符串

   ```java
   import com.fasterxml.jackson.databind.ObjectMapper;
   import java.util.*;
   
   public class TestJackson {
       class Test{
         public String test;
         
         @Override
         public String toString(){
             return "Test: " + test;
         }
       }
       public static void main(String[] args){
         ObjectMapper objectMapper = new ObjectMapper();
         
         // json to class
         String str = "{\"test\":\"json to class\"}";
         System.out.println(objectMapper.readValue(str, Test.class).toString());
   
         // class to json
         Test a = new Test();
         a.test = "class to json";
         System.out.println(objectMapper.writeValueAsString(a));
       }
   }
   ```
