# Spring Web

1. RestTemplate
    RestTemplate类是Spring提供的用于访问REST服务的模板类，提供了访问REST服务的方法，如可以发送GET请求、POST请求、PUT请求、DELETE请求等。
    1. 构造方法
        - `RestTemplate()`：创建RestTemplate对象
    2. 常用方法
        - `<T> ResponseEntity<T>  postForEntity(String url, Object request, Class<T> responseType)`：发送POST请求
        - `<T> ResponseEntity<T>  getForEntity(String url, Class<T> responseType)`：发送GET请求
        - `<T> ResponseEntity<T>  exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType)`：发送HTTP请求，并返回ResponseEntity对象
