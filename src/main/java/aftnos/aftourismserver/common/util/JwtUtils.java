package aftnos.aftourismserver.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * JWT工具类
 */
public class JwtUtils {

    // 可以改为使用固定密钥
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 设置过期时间（毫秒），这里设置为24小时
    private static final long EXPIRATION_TIME = 86400000;
    
    /**
     * 生成JWT Token
     * @param claims 要存储在token中的信息
     * @return 生成的token字符串
     */
    public static String generateToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SECRET_KEY)
                .compact();
    }
    
    /**
     * 解析JWT Token
     * @param token 要解析的token
     * @return Claims对象，包含token中的信息
     */
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * 验证JWT Token是否有效
     * @param token 要验证的token
     * @return 如果token有效返回true，否则返回false
     */
    public static boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
//-----------------------------------------------------------------------------------------

/*// 创建用户信息映射
Map<String, Object> claims = new HashMap<>();
claims.put("userId", 12345L);
claims.put("username", "zhangsan");
claims.put("role", "admin");

// 生成token
String token = JwtUtils.generateToken(claims);
System.out.println("生成的token: " + token);
*/

//-----------------------------------------------------------------------------------------


/*try {
    // 解析token获取claims
    Claims claims = JwtUtils.parseToken(token);

    // 获取存储的信息
    Long userId = Long.valueOf(claims.get("userId").toString());
    String username = claims.get("username").toString();
    String role = claims.get("role").toString();

    System.out.println("用户ID: " + userId);
    System.out.println("用户名: " + username);
    System.out.println("角色: " + role);
} catch (Exception e) {
    System.out.println("Token解析失败: " + e.getMessage());
}
*/

//-----------------------------------------------------------------------------------------


/*// 验证token是否有效
boolean isValid = JwtUtils.validateToken(token);

if (isValid) {
    System.out.println("Token有效");
    // 继续处理业务逻辑
} else {
    System.out.println("Token无效或已过期");
    // 返回错误信息或重新登录
}
*/

//-----------------------------------------------------------------------------------------