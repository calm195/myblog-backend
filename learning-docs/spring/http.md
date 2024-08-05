# Spring Http

1. MediaType
    MediaType类表示媒体类型，定义了HTTP请求和响应的媒体类型。
    常量：
    - `MULTIPART_FORM_DATA`：多部分表单数据
    - `APPLICATION_JSON`：JSON数据
    - `APPLICATION_XML`：XML数据

2. HttpHeaders
    HttpHeaders类表示HTTP头，定义了HTTP请求和响应的头信息。该类提供了获取和设置HTTP头信息的方法，
    1. 获取实例
        - `HttpHeaders()`：创建HTTP头
        - `HttpHeaders(MultiValueMap<String, String> headers)`：创建HTTP头
        - `static HttpHeaders readOnlyHttpHeaders(HttpHeaders headers)`：创建只读HTTP头
    2. 设置头信息
        - `void setContentType(MediaType mediaType)`：设置内容类型

3. HttpEntity
    HttpEntity类表示HTTP请求或返回实体，包含HTTP头和HTTP体。
    1. 构造方法
        - `HttpEntity()`：创建新的空HTTP实体
        - `HttpEntity(MultiValueMap<String, String> headers)`：创建无请求体的HTTP实体
        - `HttpEntity(T body)`：创建无请求头的HTTP实体
        - `HttpEntity(T body, MultiValueMap<String, String> headers)`：创建HTTP实体
        - 常量：
            - `HttpEntity.EMPTY`：空HTTP实体
    2. 常用方法
        - `T getBody()`：获取请求体
        - `HttpHeaders getHeaders()`：获取请求头
        - `boolean hasBody()`：判断是否有请求体
    3. 常见实现类
        - `RequestEntity`：HTTP请求实体
        - `ResponseEntity`：HTTP响应实体
            加入了HTTP状态码
            - `ResponseEntity(HttpStatus status)`：创建HTTP响应实体
            - `ResponseEntity(T body, HttpStatus status)`：创建HTTP响应实体
            - `ResponseEntity(MultiValueMap<String, String> headers, HttpStatus status)`：创建HTTP响应实体

4. ClientHttpRequestFactory
    ClientHttpRequestFactory接口表示客户端HTTP请求工厂，定义了创建客户端HTTP请求的方法。
    1. 创建实例
        - `ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod)`：创建客户端HTTP请求
    2. 方法
        - `ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod)`：创建客户端HTTP请求
    3. 实现类
        - `SimpleClientHttpRequestFactory`：简单客户端HTTP请求工厂
        - `BufferingClientHttpRequestFactory`：缓冲客户端HTTP请求工厂
        - `HttpComponentsClientHttpRequestFactory`：基于Apache HttpClient的客户端HTTP请求工厂
