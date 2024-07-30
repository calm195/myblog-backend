# Spring 事务

Spring 事务的本质，其实就是通过 Spring AOP 切面技术，在合适的地方开启事务，接着在合适的地方提交事务或回滚事务，从而实现了业务编程层面的事务操作。

## 事务

事务其实是一个并发控制单位，是用户定义的一个操作序列，这些操作要么全部完成，要不全部不完成，是一个不可分割的工作单位。事务有 ACID 四个特性，即：

1. Atomicity（原子性）：事务中的所有操作，或者全部完成，或者全部不完成，不会结束在中间某个环节。
2. 一致性（Consistency）：在事务开始之前和事务结束以后，数据库的完整性没有被破坏。
3. 事务隔离（Isolation）：多个事务之间是独立的，不相互影响的。
4. 持久性（Durability）：事务处理结束后，对数据的修改就是永久的，即便系统故障也不会丢失。

## Spring 事务管理

1. Spring 事务管理的核心接口是 `PlatformTransactionManager`，它是 Spring 事务管理的核心接口，它的实现类负责事务的管理。Spring 提供了多种事务管理器，如：
    > 定义了三个方法：
    >
    > 1. `TransationStatus getTransation(@Nullable TransationDefinition)`
    > 2. `void commit(TransationStatus)`
    > 3. `void rollback(TransationStatus)`
    1. `DataSourceTransactionManager`：基于 JDBC 的事务管理器。
    2. `HibernateTransactionManager`：基于 Hibernate 的事务管理器。
    3. `JpaTransactionManager`：基于 JPA 的事务管理器。
    4. `JtaTransactionManager`：基于 JTA 的事务管理器。
    5. `JmsTransactionManager`：基于 JMS 的事务管理器。
2. `TransactionDefinition`：事务定义接口，定义了事务的隔离级别、传播行为、超时时间等。
3. `TransactionStatus`：事务运行状态。
4. `TransactionSynchronization`：事务同步器，用于在事务提交或回滚时执行一些操作。
    > 定义了多个方法：
    >
    > 1. `triggerBeforeCommit(boolean)`：在事务提交前执行，抛出异常则回滚。
    > 2. `triggerBeforeCompletion()`：在事务完成前执行，抛出异常自身处理，不会回滚。
    > 3. `triggerAfterCommit()`：在事务提交后执行，抛出异常不会回滚，但会触发`afterCompletion`。
    > 4. `triggerAfterCompletion(int)`：在`triggerAfterCommit`后执行，抛出异常。
5. `TransactionSynchronizationManager`：事务同步管理器，用于注册和获取事务同步器。保存的是各个线程中的事务信息。Spring在事务过程中通过此类管理事务。
6. `TransactionEventListener`：事务事件监听器，用于监听事务的提交、回滚等事件。使用Spring监听器实现了`TransactionSynchronization`接口，可以在事务提交或回滚时执行一些操作。

## Spring 事务使用

1. 声明式事务管理：通过 Spring AOP 实现，使用 `@Transactional` 注解。
    `@Transactional`常用配置参数
    1. propagation：事务的传播行为。
    2. isolation：事务的隔离级别。
    3. timeout：事务的超时时间。
    4. readOnly：是否只读事务。
    5. rollbackFor：指定能触发回滚的异常类型

    ```java
    @Service
    public class UserServiceImpl implements UserService {
    
        @Autowired
        private UserDao userDao;
    
        @Override
        @Transactional
        public void transfer(String from, String to, Double money) {
            userDao.reduceMoney(from, money);
            int i = 1 / 0;
            userDao.addMoney(to, money);
        }
    }
    ```

2. 编程式事务管理：通过编程的方式实现事务管理
   1. 使用 `TransactionTemplate`。

        ```java
        @Service
        public class UserServiceImpl implements UserService {
        
            @Autowired
            private UserDao userDao;
        
            @Autowired
            private TransactionTemplate transactionTemplate;
        
            @Override
            public void transfer(String from, String to, Double money) {
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(TransactionStatus status) {
                        userDao.reduceMoney(from, money);
                        int i = 1 / 0;
                        userDao.addMoney(to, money);
                    }
                });
            }
        }
        ```

   2. 使用 `TransactionManager`。

        ```java
        @Service
        public class UserServiceImpl implements UserService {
        
            @Autowired
            private UserDao userDao;
        
            @Autowired
            private PlatformTransactionManager transactionManager;
        
            @Override
            public void transfer(String from, String to, Double money) {
                DefaultTransactionDefinition def = new DefaultTransactionDefinition();
                TransactionStatus status = transactionManager.getTransaction(def);
                try {
                    userDao.reduceMoney(from, money);
                    int i = 1 / 0;
                    userDao.addMoney(to, money);
                    transactionManager.commit(status);
                } catch (Exception e) {
                    transactionManager.rollback(status);
                }
            }
        }
        ```

## Spring 事务隔离级别

Spring事务隔离级别有五种：

1. `TransactionDefinition.ISOLATION_DEFAULT` :使用后端数据库默认的隔离级别，MySQL 默认采用的 REPEATABLE_READ , Oracle 默认采用的 READ_COMMITTED .
2. `TransactionDefinition.ISOLATION_READ_UNCOMMITTED` :最低的隔离级别，使用这个隔离级别很少，因为它允许读取尚未提交的数据变更，可能会导致脏读、幻读或不可重复读
3. `TransactionDefinition.ISOLATION_READ_COMMITTED` : 允许读取并发事务已经提交的数据，可以阻止脏读，但是幻读或不可重复读仍有可能发生
4. `TransactionDefinition.ISOLATION_REPEATABLE_READ` : 对同一字段的多次读取结果都是一致的，除非数据是被本身事务自己所修改，可以阻止脏读和不可重复读，但幻读仍有可能发生。
5. `TransactionDefinition.ISOLATION_SERIALIZABLE` : 最高的隔离级别，完全服从 ACID 的隔离级别。所有的事务依次逐个执行，这样事务之间就完全不可能产生干扰，也就是说，该级别可以防止脏读、不可重复读以及幻读。但是这将严重影响程序的性能。通常情况下也不会用到该级别。

## Spring 事务传播行为

Spring 事务传播行为是指在多个事务方法相互调用时，事务如何传播的问题。Spring 事务传播行为有 7 种：

1. `REQUIRED`： **（默认）** 如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中。
    > 如果当前存在事务，则加入这个事务，如果当前没有事务，则新建一个事务。父子事务共用一个事务，如果存在回滚，则父子事务都会回滚。
2. `REQUIRED_NEW`：新建一个事务，如果当前存在事务，把当前事务挂起。
    > 如果当前存在事务，则把当前事务挂起，新建一个事务。父子事务互不影响，如果存在回滚，则只回滚当前事务。
3. `NESTED`：如果当前存在事务，则新建一个事务作为嵌套事务，在嵌套事务内执行，如果当前没有事务，则新建一个事务。
    > 如果当前存在事务，则在嵌套事务内执行。子方法回滚时，回滚子方法内的事务，父方法如果没捕捉异常，那么父方法也回滚。如果父方法回滚，子方法会回滚。
4. `SUPPORTS`：支持当前事务，如果当前没有事务，就以非事务方式执行。
5. `NOT_SUPPORTED`：以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
6. `MANDATORY`：使用当前的事务，如果当前没有事务，就抛出异常。
7. `NEVER`：以非事务方式执行，如果当前存在事务，则抛出异常。

## Spring 事务回滚规则

1. 默认情况下，事务只有遇到运行期异常（RuntimeException 的子类）时才会回滚，Error 也会导致事务回滚，但是，在遇到检查型（Checked）异常时不会回滚。
2. 自定义回滚规则：`@Transactional(rollbackFor= MyException.class)`

## Spring 事务失效

1. 没有`@Transactional`注解的方法调用有`@Transactional`注解的方法，事务不会生效。
2. 直接调用而不经过代理对象的方法，事务不会生效。
3. `@Transactional`注解的方法不是`public`的，事务不会生效。
4. `@Transactional`注解的方法所在类没有被 Spring 容器管理，事务不会生效。
5. 底层数据库不支持事务，事务不会生效。
