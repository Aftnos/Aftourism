package aftnos.aftourismserver.common.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 文件存储相关配置入口
 * 该文件是配置类(新用法+1)
 * 用于获取yml文件存储相关配置
 * Aftnos: 2025/11/13
 */
@Configuration
@EnableConfigurationProperties(FileStorageProperties.class)
public class FileStorageConfig {
}
