package com.chrissy.core.config;

import lombok.Data;

/**
 * @author chrissy
 * @description oss配置 - 文件存储
 * @date 2024/7/30 16:34
 */
@Data
public class OssProperties {
    /**
     * 上传文件前缀路径
     */
    private String prefix;

    private String type;
    private String endpoint;
    private String ak;
    private String sk;
    private String bucket;
    private String host;
}
