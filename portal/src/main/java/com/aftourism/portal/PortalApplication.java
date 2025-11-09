package com.aftourism.portal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.aftourism.common", "com.aftourism.user", "com.aftourism.portal"})
@MapperScan(basePackages = {"com.aftourism.common.news", "com.aftourism.user.mapper"})
public class PortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortalApplication.class, args);
    }
}
