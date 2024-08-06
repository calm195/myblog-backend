package com.chrissy.core.sensitive;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chrissy
 * @description 敏感词配置
 * @date 2024/8/6 22:31
 */
@Data
@Component
@ConfigurationProperties(prefix = SensitiveProperty.SENSITIVE_KEY_PREFIX)
public class SensitiveProperty {
    public static final String SENSITIVE_KEY_PREFIX = "blog.sensitive";
    private Boolean enable;
    private List<String> deny;
    private List<String> allow;
}
