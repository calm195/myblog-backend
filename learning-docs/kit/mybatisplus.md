# baomidou MybatisPlus

[mybatis-plus](https://github.com/baomidou/mybatis-plus) 是一个 MyBatis 的增强工具，在 MyBatis 的基础上只做增强不做改变，为简化开发、提高效率而生。

依赖：

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.4.3</version>
</dependency>
```

配置：

```yaml
mybatis-plus:
    configuration:
        log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # 日志输出
    global-config:
        db-config:
            logic-delete-value: 1 # 逻辑删除后的字段
            logic-not-delete-value: 0 # 逻辑删除前的字段
            logic-delete-field: deleted # 逻辑删除字段，对应实体类的字段，写了这个实体类就不用写逻辑删除注解
```

## 常用注解

- `@TableName`：表名注解
  - `value`：表名
  - `schema`：数据库 schema
  - `keepGlobalPrefix`：是否保持全局的表前缀
- `@TableField`：字段注解
  - `value`：字段名
  - `exist`：是否为数据库字段
  - `condition`：字段筛选条件，where 条件
  - `update`：更新字段，update set
  - `fill`：字段自动填充
    - `FieldFill.INSERT`：第一次插入时填充
    - `FieldFill.UPDATE`：更新时填充
    - `FieldFill.INSERT_UPDATE`：插入和更新时填充
- `@TableId`：主键注解
  - `value`：主键字段名
  - `type`：主键类型
    - `IdType.AUTO`：数据库自增，雪花算法，自行注入的ID会被自动生成的ID覆盖；自动生成的ID会回填到java对象中
    - `IdType.INPUT`：数据库自增，雪花算法，但自行注入的ID覆盖自动生成的ID；自动生成的ID不会回填到java对象中
    - `IdType.NONE`：无状态，跟随全局，雪花算法实现
    - `IdType.ASSIGN_ID`：MP 自带的分配 ID 策略
    - `IdType.ASSIGN_UUID`：分配 UUID 策略
- `@Version`：乐观锁注解
  - 乐观锁字段，类型为 int/Integer
  - 乐观锁字段在更新时会自动 +1
- `@TableLogic`：逻辑删除注解
  - 在配置文件中配置逻辑删除属性
