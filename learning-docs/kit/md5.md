# MessageDigest  

java内置的加密功能之一，主要功能是将输入数据转为固定长度的哈希值。  
常用哈希算法有，MD5, SHA-1, SHA-256。

## 使用方法

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

## 简单全流程

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
