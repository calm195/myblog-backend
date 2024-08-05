# Spring core io

1. ByteArrayResource
    根据给定的字节数组创建一个ByteArrayInputStream对象。
    1. 构造方法
        - `ByteArrayResource(byte[] byteArray)`：根据字节数组创建ByteArrayResource对象
        - `ByteArrayResource(byte[] byteArray, String description)`：根据字节数组和描述创建ByteArrayResource对象
    2. 常用方法
        - `byte[] getByteArray()`：获取字节数组
        - `String getDescription()`：获取描述
        - `String getFilename()`：获取文件名。实例化时需要重写该方法，因为默认返回null
