package aftnos.aftourismserver.auth.service;

import aftnos.aftourismserver.auth.dto.LoginRequest;
import aftnos.aftourismserver.auth.dto.LoginResponse;
import aftnos.aftourismserver.auth.dto.RegisterRequest;

/**
 * 门户用户认证服务接口
 * <p>聚合前台用户注册、登录相关能力。</p>
 */
public interface PortalAuthService {

    /**
     * 注册新的门户用户。
     *
     * @param request 注册请求参数
     */
    void register(RegisterRequest request);

    /**
     * 门户用户登录。
     *
     * @param request 登录请求参数
     * @return 登录响应
     */
    LoginResponse login(LoginRequest request);
}
