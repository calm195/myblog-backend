# Java security

## Provider

Java中的`Provider`类是一个抽象类，用于提供加密算法的实现。提供DSA, RSA, SHA-1, MD5等加密算法。

## Security

`Security`类是一个工具类，用于管理`Provider`类。

常用方法有：

- `int addProvider(Provider provider)`：添加一个`Provider`实例。
- `synchronized void removeProvider(String name)`：移除指定名称的`Provider`实例。
- `static Provider getProvider(String name)`：获取指定名称的`Provider`实例。
- `static Provider[] getProviders()`：获取所有`Provider`实例。

## MessageDigest  

java内置的加密功能之一，主要功能是将输入数据转为固定长度的哈希值。  
常用哈希算法有，MD5, SHA-1, SHA-256。

使用方法

1. 引入依赖  
MessageDigest位于java.security包中。引入语句为：  
`import java.security.MessageDigest;`  
   > `MessageDigest`类以单例模式存在，故具体使用时通过`getInstance()`方法获取实例对象  
   > `MessageDigest md = MessageDigest.getInstance("SHA-256");`  
   > 其中`SHA-256`为使用的哈希算法，可另外使用MD5等。
   >> 算法名不分大小写，但建议全大写。
2. 传入数据  
使用`void update(byte[])`方法传入需要加密的数据：  
`md.update(data)`
3. 哈希计算  
使用`byte[] digest()`方法对数据加密：  
`byte[] target = md.digest();`  

简单全流程

```java
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
 
public class MessageDigestExample {
 
    public static void main(String[] args) {
        String data = "Hello, MessageDigest!";
        
        try {
            // 步骤 1: 创建 MessageDigest 对象，使用 SHA-256 算法
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            // 步骤 2: 使用 update() 方法传入数据
            md.update(data.getBytes());
            
            // 步骤 3: 调用 digest() 方法计算哈希值
            byte[] hashValue = md.digest();
            
            // 输出哈希值
            System.out.println("Hash Value (hex): " + bytesToHex(hashValue));
        } catch (NoSuchAlgorithmException e) {
            System.err.println("SHA-256 algorithm not available.");
            e.printStackTrace();
        }
    }
    
    // 将字节数组转换为十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
```

## 普通类

1. `Subject`：代表一个验证实体。包含以下信息：
    - `Identities`：代表实体的身份，由`Principal`对象组成。
    - `Public credentials`：公共凭证
    - `Private credentials`：私有凭证
2. `Principal`：代表一个实体的身份。包含以下信息：
    - `Name`：实体的名称
    - `Type`：实体的类型
3. `Credential`：代表一个实体的凭证。包含以下信息：
    - `Type`：凭证的类型
    - `Value`：凭证的值
