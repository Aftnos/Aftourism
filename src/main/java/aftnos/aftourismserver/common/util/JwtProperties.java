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
     * 用于签发和验证 JWT 的密钥
     */
    @NotBlank(message = "JWT 密钥不能为空")
    private String secret;

    /**
     * Token 过期时间
     */
    @NotNull(message = "JWT 过期时间不能为空")
    private Duration expiration;
}
