# ip2region

[ip2region](https://github.com/lionsoul2014/ip2region) - 是一个离线IP地址定位库和IP定位数据管理框架，10微秒级别的查询效率，提供了众多主流编程语言的 xdb 数据生成和查询客户端实现。

maven依赖

```xml
<dependency>
    <groupId>org.lionsoul</groupId>
    <artifactId>ip2region</artifactId>
    <version>2.7.0</version>
</dependency>
```

## 使用方法（Java）

1. 完全基于文件的查询

   ```java
    import org.lionsoul.ip2region.xdb.Searcher;
    import java.io.*;
    import java.util.concurrent.TimeUnit;

    public class SearcherTest {
        public static void main(String[] args) {
            // 1、创建 searcher 对象
            String dbPath = "ip2region.xdb file path";
            Searcher searcher = null;
            try {
                searcher = Searcher.newWithFileOnly(dbPath);
            } catch (IOException e) {
                System.out.printf("failed to create searcher with `%s`: %s\n", dbPath, e);
                return;
            }

            // 2、查询
            try {
                String ip = "1.2.3.4";
                long sTime = System.nanoTime();
                String region = searcher.search(ip);
                long cost = TimeUnit.NANOSECONDS.toMicros((long) (System.nanoTime() - sTime));
                System.out.printf("{region: %s, ioCount: %d, took: %d μs}\n", region, searcher.getIOCount(), cost);
            } catch (Exception e) {
                System.out.printf("failed to search(%s): %s\n", ip, e);
            }

            // 3、关闭资源
            searcher.close();
            
            // 备注：并发使用，每个线程需要创建一个独立的 searcher 对象单独使用。
        }
    }
    ```

2. 缓存`VectorIndex`索引

    ```java
    import org.lionsoul.ip2region.xdb.Searcher;
    import java.io.*;
    import java.util.concurrent.TimeUnit;

    public class SearcherTest {
        public static void main(String[] args) {
            String dbPath = "ip2region.xdb file path";

            // 1、从 dbPath 中预先加载 VectorIndex 缓存，并且把这个得到的数据作为全局变量，后续反复使用。
            byte[] vIndex;
            try {
                vIndex = Searcher.loadVectorIndexFromFile(dbPath);
            } catch (Exception e) {
                System.out.printf("failed to load vector index from `%s`: %s\n", dbPath, e);
                return;
            }

            // 2、使用全局的 vIndex 创建带 VectorIndex 缓存的查询对象。
            Searcher searcher;
            try {
                searcher = Searcher.newWithVectorIndex(dbPath, vIndex);
            } catch (Exception e) {
                System.out.printf("failed to create vectorIndex cached searcher with `%s`: %s\n", dbPath, e);
                return;
            }

            // 3、查询
            try {
                String ip = "1.2.3.4";
                long sTime = System.nanoTime();
                String region = searcher.search(ip);
                long cost = TimeUnit.NANOSECONDS.toMicros((long) (System.nanoTime() - sTime));
                System.out.printf("{region: %s, ioCount: %d, took: %d μs}\n", region, searcher.getIOCount(), cost);
            } catch (Exception e) {
                System.out.printf("failed to search(%s): %s\n", ip, e);
            }
            
            // 4、关闭资源
            searcher.close();

            // 备注：每个线程需要单独创建一个独立的 Searcher 对象，但是都共享全局的制度 vIndex 缓存。
        }
    }
    ```

3. 缓存整个`xdb`数据

    ```java
    import org.lionsoul.ip2region.xdb.Searcher;
    import java.io.*;
    import java.util.concurrent.TimeUnit;

    public class SearcherTest {
        public static void main(String[] args) {
            String dbPath = "ip2region.xdb file path";

            // 1、从 dbPath 加载整个 xdb 到内存。
            byte[] cBuff;
            try {
                cBuff = Searcher.loadContentFromFile(dbPath);
            } catch (Exception e) {
                System.out.printf("failed to load content from `%s`: %s\n", dbPath, e);
                return;
            }

            // 2、使用上述的 cBuff 创建一个完全基于内存的查询对象。
            Searcher searcher;
            try {
                searcher = Searcher.newWithBuffer(cBuff);
            } catch (Exception e) {
                System.out.printf("failed to create content cached searcher: %s\n", e);
                return;
            }

            // 3、查询
            try {
                String ip = "1.2.3.4";
                long sTime = System.nanoTime();
                String region = searcher.search(ip);
                long cost = TimeUnit.NANOSECONDS.toMicros((long) (System.nanoTime() - sTime));
                System.out.printf("{region: %s, ioCount: %d, took: %d μs}\n", region, searcher.getIOCount(), cost);
            } catch (Exception e) {
                System.out.printf("failed to search(%s): %s\n", ip, e);
            }
            
            // 4、关闭资源 - 该 searcher 对象可以安全用于并发，等整个服务关闭的时候再关闭 searcher
            // searcher.close();

            // 备注：并发使用，用整个 xdb 数据缓存创建的查询对象可以安全的用于并发，也就是你可以把这个 searcher 对象做成全局对象去跨线程访问。
        }
    }
    ```
