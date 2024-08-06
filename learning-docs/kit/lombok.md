# lombok

## 原理  

Lombok通过在编译时期，使用注解处理器（Annotation Processor）来生成代码，从而避免了开发者手动编写一些重复性的代码，例如Getter和Setter方法、toString方法、equals方法等。  
在使用Lombok的类上加上注解后，Lombok会在编译时扫描这些注解，然后在生成字节码文件时，根据注解生成相应的方法和字段。这样，开发者就能够以更简洁的方式书写Java代码。  

## 引入依赖

   ```xml
   <dependency>
       <groupId>org.projectlombok</groupId>
       <artifactId>lombok</artifactId>
       <version>1.18.24</version>
       <scope>provided</scope>
   </dependency>
   ```

## 使用

1. @Getter
2. @Setter
3. @ToString
4. @Slf4j
5. @Data
    注解在类上，相当于同时加上了@Getter、@Setter、@ToString、@EqualsAndHashCode、@RequiredArgsConstructor这几个注解。
