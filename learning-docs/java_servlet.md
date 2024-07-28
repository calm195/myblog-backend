# Java Servlet

一般情况下，浏览器（客户端）通过 HTTP 协议来访问服务器的资源，Servlet 主要用来处理 HTTP 请求。

Servlet处理请求的流程如下：

1. Servlet 容器接收到来自客户端的 HTTP 请求后，容器会针对该请求分别创建一个 HttpServletRequest 对象和 HttpServletReponse 对象。
2. 容器将 HttpServletRequest 对象和 HttpServletReponse 对象以参数的形式传入 service() 方法内，并调用该方法。
3. 在 service() 方法中 Servlet 通过 HttpServletRequest 对象获取客户端信息以及请求的相关信息。
4. 对 HTTP 请求进行处理。
5. 请求处理完成后，将响应信息封装到 HttpServletReponse 对象中。
6. Servlet 容器将响应信息返回给客户端。
7. 当 Servlet 容器将响应信息返回给客户端后，HttpServletRequest 对象和 HttpServletReponse 对象被销毁。

## HttpServletRequest

在 Servlet API 中，定义了一个 HttpServletRequest 接口，它继承自 ServletRequest 接口。HttpServletRequest 对象专门用于封装 HTTP 请求消息，简称 request 对象。

HTTP 请求消息分为请求行、请求消息头和请求消息体三部分，所以 HttpServletRequest 接口中定义了获取请求行、请求头和请求消息体的相关方法。

1. 获取请求行信息

    | 方法 | 描述 |
    | --- | --- |
    | getMethod() | 获取请求方式(如GET) |
    | getRequestURI() | 获取请求的 URI，即位于 URL 的主机和端口之后，参数部分之前的部分。 |
    | getQueryString() | 获取请求的查询字符串，URL 中“?”以后的所有内容 |
    | getContextPath() | 获取请求的上下文路径 |
    | getServletPath() | 获取请求的 Servlet 路径 |
    | getRemoteAddr() | 获取客户端的 IP 地址 |
    | getRemoteHost() | 该方法用于获取客户端的完整主机名，如果无法解析出客户机的完整主机名，则该方法将会返回客户端的 IP 地址。获取客户端的主机名 |

2. 获取请求头信息

    | 方法 | 描述 |
    | --- | --- |
    | getHeader(String name) | 该方法用于获取一个指定头字段的值。如果请求消息中包含多个指定名称的头字段，则该方法返回其中第一个头字段的值。获取指定名称的请求头信息 |
    | getHeaders(String name) | 该方法返回指定头字段的所有值的枚举集合，在多数情况下，一个头字段名在请求消息中只出现一次，但有时可能会出现多次。返回一个 Enumeration 对象 |
    | getHeaderNames() | 获取所有请求头的枚举集合，返回一个 Enumeration 对象 |
    | getContentType() | 获取Content-Type 头字段的值。 |
    | getContentLength() | 获取请求Content-Length 头字段的值 |
    | getCharacterEncoding() | 获取请求的字符编码 |

3. 获取请求消息体

    | 方法 | 描述 |
    | --- | --- |
    | getParameter(String name) | 返回指定参数名的参数值。 |
    | getParameterValues (String name)  | 以字符串数组的形式返回指定参数名的所有参数值（HTTP 请求中可以有多个相同参数名的参数）。 |
    | getParameterNames() | 以枚举集合的形式返回请求中所有参数名。 |
    | getParameterMap() | 用于将请求中的所有参数名和参数值装入一个 Map 对象中返回。 |

## HttpServletResponse

HttpServletResponse对象代表服务器的响应。继承了ServletResponse的接口。这个对象可以设置响应数据、设置响应头、设置响应状态码。

1. 设置响应数据

    | 方法 | 描述 |
    | --- | --- |
    | PrintWriter getWriter() | 获取一个 PrintWriter 对象，用于向客户端发送字符数据。 |
    | ServletOutputStream getOutputStream() | 获取一个 ServletOutputStream 对象，用于向客户端发送二进制数据。 |

    > 注意：在一个 Servlet 中，要么使用 PrintWriter 对象向客户端发送字符数据，要么使用 ServletOutputStream 对象向客户端发送二进制数据，不能同时使用。
    > 实际上PrintWriter是基于ServletOutputStream封装的。

2. 设置响应头

    | 方法 | 描述 |
    | --- | --- |
    | setHeader(String name, String value) | 设置响应头 |
    | addHeader(String name, String value) | 添加响应头 |
    | setContentType(String type) | 设置响应的 MIME 类型 |
    | setCharacterEncoding(String charset) | 设置响应的字符编码 |

    > 注意：在设置响应头时，如果使用 setHeader() 方法设置相同名称的响应头，则后者会覆盖前者；如果使用 addHeader() 方法设置相同名称的响应头，则会添加多个相同名称的响应头。

3. 设置响应状态码

    | 方法 | 描述 |
    | --- | --- |
    | setStatus(int sc) | 设置响应状态码 |
    | sendRedirect(String location) | 重定向到指定的 URL 地址 |
