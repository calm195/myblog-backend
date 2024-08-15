# Spring MVC

Spring MVC是Spring框架的一个模块，用于构建Web应用程序。Spring MVC提供了一种基于Java的配置方式，可以通过Java类配置Web应用程序，而不需要XML配置文件。

## 核心概念

| 名称        | 说明                                   |
|:----------|:-------------------------------------|
| DispatcherServlet | 前端控制器，负责请求的分发和处理 |
| HandlerMapping | 处理器映射，根据请求找到对应的处理器 |
| HandlerAdapter | 处理器适配器，调用处理器方法 |
| HandlerInterceptor | 处理器拦截器，拦截处理器方法的执行 |
| HandlerMethod | 处理器方法，处理请求的方法 |
| HandlerMethodArgumentResolver | 处理器方法参数解析器，解析处理器方法的参数 |
| HandlerMethodReturnValueHandler | 处理器方法返回值处理器，处理处理器方法的返回值 |
| HandlerExceptionResolver | 异常处理器，处理请求过程中的异常 |
| ViewResolver | 视图解析器，根据视图名解析视图 |
| View | 视图，渲染视图 |
| Model | 模型，存储数据 |
| ModelAndView | 模型视图，包含模型和视图 |

## 常见注解

1. @Controller：标识一个控制器类
    - 返回值为String时，表示视图名；视图名会交给视图解析器解析；视图解析器在配置文件`**mvc.xml`中配置
    - 返回值为void时，表示不返回视图，可以通过`HttpServletResponse`返回数据。视图默认跳转到请求路径
    - 返回值为ModelAndView时，表示返回模型视图；可以通过`ModelAndView`设置返回视图和数据。
2. @RequestMapping：映射请求路径；可以用在类上，表示类中所有方法的请求路径的前缀；可以用在方法上，表示方法的请求路径。
   - `value`：请求路径，默认属性，如果只指定value可以省略
   - `method`：请求方法，GET、POST等
   - `params`：请求参数，指定必须包含或者必须不包含的参数
   - `headers`：请求头，指定必须包含或者必须不包含的请求头
