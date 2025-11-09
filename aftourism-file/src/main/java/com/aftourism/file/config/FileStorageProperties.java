package com.aftourism.file.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 本地文件存储配置。
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "file.local")
public class FileStorageProperties {

    /** 文件基础存储路径 */
    private String basePath;

    /** 对外访问域名 */
    private String accessHost;
}
