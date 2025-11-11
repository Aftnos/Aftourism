package aftnos.aftourismserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ConfigurationPropertiesScan("aftnos.aftourismserver")
@EnableScheduling
public class AftourismServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AftourismServerApplication.class, args);
    }

}
