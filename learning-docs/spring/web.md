# Spring Web

1. RestTemplate
    RestTemplate类是Spring提供的用于访问REST服务的模板类，提供了访问REST服务的方法，如可以发送GET请求、POST请求、PUT请求、DELETE请求等。
    1. 构造方法
        - `RestTemplate()`：创建RestTemplate对象
    2. 常用方法
        - `<T> ResponseEntity<T>  postForEntity(String url, Object request, Class<T> responseType)`：发送POST请求
        - `<T> ResponseEntity<T>  getForEntity(String url, Class<T> responseType)`：发送GET请求
        - `<T> ResponseEntity<T>  exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType)`：发送HTTP请求，并返回ResponseEntity对象
2. SseEmitter
    SseEmitter是Spring提供的用于服务器端向客户端推送数据的类，可以用于实现服务器端推送技术。客户端发起请求，连接一直保持打开，服务器端可以随时向客户端发送数据。
    1. 构造方法
        - `SseEmitter()`：创建SseEmitter对象
        - `SseEmitter(long timeout)`：创建SseEmitter对象，并指定超时时间
    2. 常用方法
        - `void send(Object object)`：向客户端发送数据
        - `void send(SseEmitter.SseEventBuilder builder)`：向客户端发送数据
        - `void complete()`：完成数据发送
        - `void completeWithError(Throwable ex)`：完成数据发送，并指定错误信息
    3. 优点
        - http协议
        - 轻量，使用简单
        - 默认支持短线重连
        - 文本传输，可自定义发送消息的类型
