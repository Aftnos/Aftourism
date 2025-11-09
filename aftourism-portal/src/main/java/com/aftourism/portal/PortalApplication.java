package com.aftourism.portal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 前台门户启动类。
 */
@SpringBootApplication(scanBasePackages = "com.aftourism")
@MapperScan("com.aftourism.portal.mapper")
public class PortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortalApplication.class, args);
    }
}
