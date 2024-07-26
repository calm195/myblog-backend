# jackson

json序列化工具
> 优点：可以自定义序列器

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

## 高级用法

1. 使用自定义序列器`JsonSerializer`
