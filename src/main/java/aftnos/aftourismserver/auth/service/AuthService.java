package aftnos.aftourismserver.auth.service;

import aftnos.aftourismserver.auth.dto.LoginRequest;
import aftnos.aftourismserver.auth.dto.LoginResponse;

/**
 * 登录服务接口，负责统一的登录校验与令牌签发。
 */
public interface AuthService {

    /**
     * 统一登录入口，按照用户名判断登录主体（管理员/门户用户），并生成令牌。
     *
     * @param request 登录请求参数
     * @return 登录结果，包含访问令牌与刷新令牌
     */
    LoginResponse login(LoginRequest request);
}
