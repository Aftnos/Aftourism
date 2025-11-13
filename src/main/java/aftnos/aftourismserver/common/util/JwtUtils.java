package aftnos.aftourismserver.common.util;

import aftnos.aftourismserver.common.exception.UnauthorizedException;
import aftnos.aftourismserver.common.security.PrincipalType;
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
    public static final String CLAIM_PRINCIPAL_ID = "pid";
    public static final String CLAIM_PRINCIPAL_TYPE = "pt";

    /**
     * 生成携带主体ID与类型的 JWT Token。
     *
     * @param principalId   主体ID
     * @param principalType 主体类型
     * @return 签发的 Token
     */
    public String generateToken(Long principalId, PrincipalType principalType) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + properties.getExpiration().toMillis());

        return Jwts.builder()
                .setSubject(principalType.name())
                .claim(CLAIM_PRINCIPAL_ID, principalId)
                .claim(CLAIM_PRINCIPAL_TYPE, principalType.name())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
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

    /**
     * 解析 Token 并返回主体载荷。
     */
    public JwtPayload parsePayload(String token) {
        Claims claims = parseToken(token).getBody();
        Object principalId = claims.get(CLAIM_PRINCIPAL_ID);
        Object principalType = claims.get(CLAIM_PRINCIPAL_TYPE);
        if (principalId == null || principalType == null) {
            throw new UnauthorizedException("Token 中缺少身份信息");
        }
        try {
            PrincipalType type = PrincipalType.valueOf(principalType.toString());
            return new JwtPayload(Long.valueOf(principalId.toString()), type);
        } catch (IllegalArgumentException ex) {
            throw new UnauthorizedException("Token 中主体类型无效");
        }
    }

    /**
     * 兼容旧逻辑的便捷方法，返回主体ID。
     */
    public Long parseUserId(String token) {
        return parsePayload(token).principalId();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public Instant calculateExpiryInstant() {
        return Instant.now().plus(properties.getExpiration());
    }

    /**
     * JWT 载荷信息封装。
     */
    public record JwtPayload(Long principalId, PrincipalType principalType) {
    }
}
