package aftnos.aftourismserver.auth.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class LoginResponse {
    Long userId;
    String token;
    Instant expiresAt;
}
