package com.aftourism.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.aftourism.common", "com.aftourism.user", "com.aftourism.admin"})
@MapperScan(basePackages = {"com.aftourism.common.news", "com.aftourism.admin.mapper", "com.aftourism.user.mapper"})
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
