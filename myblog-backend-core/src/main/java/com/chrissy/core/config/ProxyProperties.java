package com.chrissy.core.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.Proxy;
import java.util.List;

/**
 * @author chrissy
 * @description 网络代理配置类
 * @date 2024/7/30 16:40
 */
@Data
@ConfigurationProperties(prefix = "net")
public class ProxyProperties {
    private List<ProxyInfo> proxy;

    @Data
    @Accessors(chain = true)
    public static class ProxyInfo {
        private Proxy.Type type;
        private String ip;
        private Integer port;
    }
}
