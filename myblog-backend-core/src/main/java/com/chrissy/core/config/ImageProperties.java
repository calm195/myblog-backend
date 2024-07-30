package com.chrissy.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author chrissy
 * @description 照片存储配置类
 * @date 2024/7/30 16:37
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "image")
public class ImageProperties {
    private String absImpPath;
    private String webImgPath;
    private String tmpUploadPath;
    private String cdnHost;
    private OssProperties oss;

    /**
     * 检测并生成图片上传路径。//todo 没有进行连通性测试
     * @param url 上传路径或者部分路径
     * @return 上传完整路径
     */
    public String buildImgUrl(String url) {
        if (!url.startsWith(cdnHost)) {
            return cdnHost + url;
        }
        return url;
    }
}
