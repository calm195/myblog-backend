# JSON Web Token (JWT)

JSON Web Token (JWT) 是一种开放标准（[RFC 7519](https://tools.ietf.org/html/rfc7519)），定义了一种简洁的、自包含的、基于JSON的标准，用于在各方之间传递信息。

常见的使用场景有：

1. **身份认证**：用户登录成功后，服务器返回一个JWT给客户端，客户端在后续请求中携带JWT，服务器验证JWT的有效性，从而实现身份认证。
2. **信息交换**：JWT是一种轻量级的数据交换格式，可以在各方之间安全地传递信息。
3. **跨域单点登录**：多个系统之间共享用户登录状态。
4. **防篡改**：JWT可以计算签名，可以验证数据的完整性。

## 结构

JWT由三部分组成，使用`.`分隔：header.payload.signature。
> JWT对象为一个长字符串。

1. **Header**：头部，包含了类型和签名算法。
    字段：
    - `typ`：类型，固定值为`JWT`。
    - `alg`：算法，用于签名。

    | alg字段值 | 使用的算法 | 描述 |
    | --- | --- | --- |
    | HS256 | HMAC SHA-256 | 使用密钥对消息进行签名 |
    | HS384 | HMAC SHA-384 | 使用密钥对消息进行签名 |
    | HS512 | HMAC SHA-512 | 使用密钥对消息进行签名 |
    | RS256 | RSA SHA-256 | 使用私钥对消息进行签名 |
    | RS384 | RSA SHA-384 | 使用私钥对消息进行签名 |
    | RS512 | RSA SHA-512 | 使用私钥对消息进行签名 |
    | ES256 | ECDSA SHA-256 | 使用私钥对消息进行签名 |
    | ES384 | ECDSA SHA-384 | 使用私钥对消息进行签名 |
    | ES512 | ECDSA SHA-512 | 使用私钥对消息进行签名 |

2. **Payload**：载荷，包含了声明（claims）。默认不加密。
    - **Registered claims**：预定义的声明，不是必须的，但推荐使用。
        - `iss`：Issuer，签发者。
        - `sub`：Subject，主题。
        - `aud`：Audience，接收者。
        - `exp`：Expiration Time，过期时间。
        - `nbf`：Not Before，生效时间。
        - `iat`：Issued At，签发时间。
        - `jti`：JWT ID，JWT的唯一标识。
    - **Public claims**：JWT 签发方可以自定义的声明，但是为了避免冲突，应该在 [IANA JSON Web Token Registry](https://www.iana.org/assignments/jwt/jwt.xhtml) 中定义它们。
    - **Private claims**：JWT 签发方因为项目需要而自定义的声明，更符合实际项目场景使用。
3. **Signature**：签名，由头部、载荷和密钥生成。指定一个密钥，使用Header中指定的算法对Header和Payload进行签名，生成Signature。
    - 计算方法：`HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)`。

## 身份认证流程

1. 用户登录，服务器验证用户名和密码。
2. 服务器生成JWT，返回给客户端。
3. 客户端接收到JWT，存储在本地；后续请求中携带JWT。
4. 服务器接收到JWT，验证JWT的有效性，从而实现身份认证。

> JWT可以存放在localStorage和cookie中，但是存放在cookie中会有CSRF攻击的风险。
>
> - CSRF攻击：跨站请求伪造，攻击者盗用用户的身份，以用户的名义发送恶意请求。
>
> 请求头中携带JWT的常见做法是在`Authorization`字段中使用`Bearer`方案，如：`Authorization: Bearer <token>`。

## java-jwt

`com.auth0.jwt`是一个Java库，用于生成和验证JWT。

maven依赖：

```xml
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>3.18.1</version>
</dependency>
```

1. `JWT`：JWT对象，包含了Header、Payload和Signature。
    - `static JWTCreator.Builder create()`：创建Builder对象。
    - `Verification require(Algorithm algorithm)`：指定算法。
    - `DecodedJWT decode(String token)`：解码JWT。
2. `Algorithm`：算法，用于生成和验证JWT。
    - `static Algorithm HMAC256(String secret)`：HMAC SHA-256算法。
    - `static Algorithm RSA256(PublicKey publicKey, PrivateKey privateKey)`：RSA SHA-256算法。
    - `static Algorithm ES256(PrivateKey privateKey, PublicKey publicKey)`：ECDSA SHA-256算法。
    - `static Algorithm none()`：不使用签名。
3. `JWTVerifier`：JWT验证器，用于验证JWT的有效性。
    - `DecodedJWT verify(String token)`：验证JWT。
4. `Verification`：验证器，用于指定算法。
    - `Verification withIssuer(String issuer)`：指定签发者。
    - `Verification withSubject(String subject)`：指定主题。
    - `Verification withAudience(String audience)`：指定接收者。
    - `Verification withExpiresAt(Date expiresAt)`：指定过期时间。
    - `Verification withNotBefore(Date notBefore)`：指定生效时间。
    - `Verification withIssuedAt(Date issuedAt)`：指定签发时间。
    - `Verification withJWTId(String jwtId)`：指定JWT ID。
    - `JWTVerifier build()`：构建JWT验证器。
5. `DecodedJWT`：解码后的JWT对象。
    - `String getHeader()`：获取头部。
    - `String getPayload()`：获取载荷。
    - `String getSignature()`：获取签名。
    - `String getToken()`：获取JWT字符串。
