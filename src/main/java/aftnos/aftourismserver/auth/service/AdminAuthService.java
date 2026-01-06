package aftnos.aftourismserver.auth.service;

import aftnos.aftourismserver.auth.dto.LoginRequest;
import aftnos.aftourismserver.auth.dto.LoginResponse;

/**
 * 管理端认证服务接口。
 */
public interface AdminAuthService {

    /**
     * 管理员登录。
     *
     * @param request 登录请求参数
     * @return 登录响应
     */
    LoginResponse login(LoginRequest request);
}
