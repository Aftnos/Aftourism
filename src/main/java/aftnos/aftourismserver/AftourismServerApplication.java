package aftnos.aftourismserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring Boot 应用启动入口，负责加载 Aftourism Server 所有模块。
 */
@SpringBootApplication
@ConfigurationPropertiesScan("aftnos.aftourismserver")
@EnableScheduling
public class AftourismServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AftourismServerApplication.class, args);
    }

}
