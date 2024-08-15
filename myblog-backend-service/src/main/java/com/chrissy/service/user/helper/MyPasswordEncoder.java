package com.chrissy.service.user.helper;
// TODO：接入 spring security

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
/**
 * 密码加密器，加盐后MD5加密
 * @author chrissy
 * @date 2024/8/13 15:53
 */
@Component
public class MyPasswordEncoder {
    // TODO: 每个用户都使用独立的盐，提高安全
    @Value("${security.salt}")
    private String salt;

    @Value("${security.salt-index}")
    private Integer saltIndex;

    /**
     * 匹配密码
     * @param plainPwd 明文密码
     * @param encPwd 加密密码
     * @return 是否匹配成功
     */
    public boolean match(String plainPwd, String encPwd) {
        return Objects.equals(encodePassword(plainPwd), encPwd);
    }

    /**
     * 明文密码加密
     *
     * @param plainPwd 明文密码
     * @return 加密密码
     */
    public String encodePassword(String plainPwd) {
        if (plainPwd.length() > saltIndex) {
            plainPwd = plainPwd.substring(0, saltIndex) + salt + plainPwd.substring(saltIndex);
        } else {
            plainPwd = plainPwd + salt;
        }
        return DigestUtils.md5DigestAsHex(plainPwd.getBytes(StandardCharsets.UTF_8));
    }
}
