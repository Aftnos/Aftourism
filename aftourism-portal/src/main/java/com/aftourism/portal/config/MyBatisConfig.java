package com.aftourism.portal.config;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis 额外配置。
 */
@Configuration
public class MyBatisConfig {

    @Bean
    public ConfigurationCustomizer portalConfigurationCustomizer() {
        return configuration -> configuration.setMapUnderscoreToCamelCase(true);
    }
}
