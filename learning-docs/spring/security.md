# Spring Security

Spring Security 是一个功能强大且高度可定制的身份验证和访问控制框架。底层基于Spring AOP和Servlet过滤器，实现安全控制。授权粒度可以在Web请求级别和方法调用级别。
前身是 Acegi Security。

核心功能：

1. **认证（Authentication）**：验证用户的身份。
2. **授权（Authorization）**：控制用户访问资源的权限。
3. **攻击防护（Attack Protection）**：防止跨站点请求伪造（CSRF）、会话固定攻击、点击劫持等攻击。
4. **会话管理（Session Management）**：管理用户的会话，即session。
5. **Remember-Me功能**：记住我，可以实现自动登录，token令牌持久化。

maven依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
```

## 认证

Spring Security 提供了多种认证方式：

1. **基于内存的认证**：在内存中配置用户名、密码和角色。
2. **基于数据库的认证**：从数据库中读取用户信息。
3. **LDAP认证**：从LDAP服务器中读取用户信息。
4. **自定义认证**：实现 `UserDetailsService` 接口，自定义用户信息获取逻辑。
5. **OAuth2认证**：OAuth2协议认证。
