package aftnos.aftourismserver.common.util;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

/**
 * JWT 配置属性
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    /**
     *JWT的密钥配置类(在配置文件里面读取)
     */
    @NotBlank(message = "JWT 密钥不能为空")
    private String secret;

    /**
     * Token 过期时间(一样和密钥)
     */
    @NotNull(message = "JWT 过期时间不能为空")
    private Duration expiration;

    /**
     * 刷新令牌过期时间，不配置时默认与访问令牌一致。
     */
    private Duration refreshExpiration;
}
