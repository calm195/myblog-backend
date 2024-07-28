# Java NIO

Java NIO (New IO) is an alternative IO API for Java (from Java 1.4) to the standard Java IO API. Java NIO offers a different way of working with IO than the standard IO API's.

## CharSet

专门负责字符的解码和编码。
> 编码解码要使用相同的字符集。

1. 创建
    - `public static final Charset forName(String charsetName)`：根据字符集名称获取字符集对象。如，`Charset.forName("UTF-8")`。
    - `public static Charset defaultCharset()`：获取系统默认字符集对象。
2. 编码
    - `public final ByteBuffer encode(CharBuffer cb)`：编码。
    - `public final ByteBuffer encode(String str)`：编码。
    - `public abstract CharsetEncoder newEncoder()`：获取编码器。
3. 解码
    - `public final CharBuffer decode(ByteBuffer bb)`：解码。
    - `public abstract CharsetDecoder newDecoder()`：获取解码器。
