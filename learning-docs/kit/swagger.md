# Swagger

[Swagger](https://swagger.io/)：是一个规范和完整的框架，用于生成、描述、调用和可视化 RESTful 风格的 Web 服务的接口文档

提供了几种开源工具

1. Swagger Codegen：可以将描述文件生成html、cwiki形式的接口文档，也可以生成多种语言的客户端和服务端代码，支持通过jar包，docker，node等方式在本地执行生成。
2. Swagger UI：提供一个可视化的UI页面展示描述文件，可以做一些简单的接口请求测试，支持在线导入描述文件和本地部署。
3. Swagger Editor：类似于markdown编辑器的编辑Swagger描述文件的工具，支持实时预览，支持在线编辑器和本地部署编辑器。
4. Swagger Inspector：提供一个在线的接口请求测试工具，可以直接输入接口地址，请求参数，请求头等信息进行接口测试，类似于在线版的postman。
5. Swagger Hub：集成了上述所有功能，需要注册账号，分为免费版和收费版，可以在线编辑，生成，测试接口文档。

## Swagger2

maven依赖

```xml
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
</dependency>
```

配置类：一个项目只能有一个配置类

一些说明：

1. `@EnableSwagger2`：启用Swagger2
2. `apiInfo()`：设置文档信息，会在api文档页面显示，可选属性填充
3. `apis()`：设置扫描的包，只有该包下的类才会生成api文档
4. `path()`：设置扫描的路径，只有该路径下的类才会生成api文档

示例：

```java
@Configuration
@EnableSwagger2

public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring Boot中使用Swagger2构建RESTful APIs")
                .description("更多Spring Boot相关文章请关注：https://blog.csdn.net/qq_27525611")
                .termsOfServiceUrl("https://blog.csdn.net/qq_27525611")
                .contact("程序猿DD")
                .version("1.0")
                .build();
    }
}
```

注解：在接口文件中增加注解

- `@Api`：用于类上，表示标识这个类是swagger的资源
  - `tags`：说明该类的作用，如果有多个tag，会生成多个list
  - `value`：说明该类的作用
- `@ApiIgnore`：用于类上，表示忽略该类
- `@ApiModel`：用于类上，表示对类进行说明，用于参数用实体类接收
  - `value`：对象名
  - `description`：描述
- `@ApiModelProperty`：用于方法，字段；表示对model属性的说明或者数据操作更改
  - `value`：字段说明
  - `name`：重写属性名
  - `dataType`：重写属性类型
  - `required`：是否必填
  - `example`：举例说明
  - `hidden`：隐藏
- `@ApiOperation`：用于方法上，表示一个http请求的操作
  - `value`：接口说明
  - `notes`：提示内容
  - `tags`：分组
- `@ApiImplicitParam`：用于方法，表示单独的请求参数
  - `name`：参数名
  - `value`：参数说明
  - `dataType`：数据类型
  - `paramType`：参数类型
  - `example`：举例说明
  - `required`：是否必填
- `@ApiImplicitParams`：用于方法，包含多个`@ApiImplicitParam`
- `@ApiParam`：用于方法参数上，表示对参数的描述
  - `name`：参数名
  - `value`：参数说明
  - `required`：是否必填
