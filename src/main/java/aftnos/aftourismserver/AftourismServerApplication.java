package aftnos.aftourismserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("aftnos.aftourismserver")
public class AftourismServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AftourismServerApplication.class, args);
    }

}
