# java IO

## 基础接口

1. Closeable：关闭资源的接口
    > extends AutoCloseable
    > 该接口中只有一个`close()`方法，用于关闭资源。
    > > `void close() throws IOException`  
2. AutoCloseable：自动关闭资源的接口
    > 实现了本接口的类可以使用`try-with-resources`语句，自动关闭资源。
    > 该接口中只有一个`close()`方法，用于关闭资源。
    > > `void close() throws Exception`
3. Flushable：刷新缓冲区的接口
    > 该接口中只有一个`flush()`方法，用于刷新缓冲区。
    > > `void flush() throws IOException`
4. Serializable：序列化接口
    > 该接口没有任何方法，只是一个标记接口，用于标记类可以被序列化。
