package aftnos.aftourismserver.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "？你没用户名吗")
    private String username;

    @NotBlank(message = "你密码呢？")
    private String password;
}
