package aftnos.aftourismserver.auth.service;

import aftnos.aftourismserver.auth.dto.LoginRequest;
import aftnos.aftourismserver.auth.dto.LoginResponse;

/**
 * 后台管理员认证服务接口。
 */
public interface AdminAuthService {

    /**
     * 管理员登录。
     *
     * @param request 登录请求
     * @return 登录成功的响应体
     */
    LoginResponse login(LoginRequest request);
}
