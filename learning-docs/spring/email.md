# Spring Email

1. 引入依赖

    ```xml
    <dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-mail</artifactId>
    </dependency>
    ```

2. 配置文件

    ```properties
    spring:
        mail:
            host: 10.110.xxx.xxx   #邮件服务器地址
            port: 25               #邮件服务器端口
            protocol: smtp         #使用的协议
            default-encoding: UTF-8 #默认编码
            username:          #这个是通过邮件服务器认证的用户名和密码，不一定是邮箱，看服务器的要求
            password: 
            properties:            #properties中的属性都是比较灵活可配置的，其实是javax.mail.Session中对应的配置项，可以参考对应文档
                mail.smtp.auth: true   #如果邮件服务器需要实名需要认证开启此选项
                mail.from: 100010@qq.com   #统一设置发件人邮箱
    ```

3. JavaMailSender：邮件发送接口
4. MimeMessage：邮件信息
5. MimeMessageHelper：发送包设置，如接收人等
6. 简单全流程：

    ```java
    JavaMailSender javaMailSender = SpringUtil.getBean(JavaMailSender.class);
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
    mimeMessageHelper.setFrom(getFrom());
    mimeMessageHelper.setTo(to);
    mimeMessageHelper.setSubject(title);
    mimeMessageHelper.setText(content, true);
    Thread.currentThread().setContextClassLoader(EmailUtil.class.getClassLoader());
    javaMailSender.send(mimeMessage);
    ```
