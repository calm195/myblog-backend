package com.chrissy.core.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;

/**
 * @author chrissy
 * @description Email工具类
 * @date 2024/7/28 10:11
 */
@Slf4j
public class EmailUtil {
    private static volatile String from;

    /**
     * 获取默认发送者邮箱，同步加锁
     * @return 邮箱字符串
     */
    private static String getFrom(){
        if (from == null){
            synchronized (EmailUtil.class){
                if (from == null){
                    // TODO: getConfig可能返回null
                    from = SpringUtil.getConfig("spring.mail.from", "1804659599@qq.com");
                }
            }
        }
        return from;
    }

    /**
     * 发送邮件
     * @param title 邮件标题
     * @param to 接受者
     * @param content 邮件内容
     * @return 是否发送成功
     */
    public static boolean sendMail(String title, String to, String content) {
        try {
            JavaMailSender javaMailSender = SpringUtil.getBean(JavaMailSender.class);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(getFrom());
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(content, true);
            Thread.currentThread().setContextClassLoader(EmailUtil.class.getClassLoader());
            javaMailSender.send(mimeMessage);
            log.info("send email successfully");
            return true;
        } catch (Exception ex){
            log.warn("send email error {}@{}, {}", title, to, ex.toString());
            return false;
        }
    }
}
