package aftnos.aftourismserver.common.util;

import aftnos.aftourismserver.common.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

/**
 * JWT 工具类，负责生成与解析 Token。
 */
@Component
public class JwtUtils {

    private final JwtProperties properties;

    public JwtUtils(JwtProperties properties) {
        this.properties = properties;
    }

    /**
     * 根据用户 ID 生成 JWT Token。
     *
     * @param userId 用户 ID
     * @return 签发好的 Token
     */
    public String generateToken(Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + properties.getExpiration().toMillis());

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析 Token 并返回用户 ID。
     *
     * @param token 待解析的 Token
     * @return Token 中的用户 ID
     */
    public Long parseUserId(String token) {
        Claims claims = parseToken(token).getBody();
        Object userId = claims.get("userId");
        if (userId == null) {
            throw new UnauthorizedException("Token 中缺少用户信息");
        }
        return Long.valueOf(userId.toString());
    }

    /**
     * 解析 Token 返回 Claims。
     */
    private Jws<Claims> parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception ex) {
            throw new UnauthorizedException("无效的 Token 或 Token 已过期");
        }
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public Instant calculateExpiryInstant() {
        return Instant.now().plus(properties.getExpiration());
    }
}
