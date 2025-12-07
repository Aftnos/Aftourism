package aftnos.aftourismserver.auth.controller;

import aftnos.aftourismserver.auth.dto.LoginRequest;
import aftnos.aftourismserver.auth.dto.LoginResponse;
import aftnos.aftourismserver.auth.dto.RegisterRequest;
import aftnos.aftourismserver.auth.service.AuthService;
import aftnos.aftourismserver.auth.service.PortalAuthService;
import aftnos.aftourismserver.common.result.Result;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 门户用户认证控制器
 * <p>负责处理基于 {@code t_user} 的注册、登录请求，所有接口统一归口到 /portal/auth。</p>
 */
@RestController
@RequestMapping("/portal/auth")
public class PortalAuthController {

    /** 门户认证服务，负责前台用户的注册逻辑。 */
    private final PortalAuthService portalAuthService;
    /** 统一登录服务。 */
    private final AuthService authService;

    public PortalAuthController(PortalAuthService portalAuthService, AuthService authService) {
        this.portalAuthService = portalAuthService;
        this.authService = authService;
    }

    /**
     * 门户用户注册接口
     * <p>处理 POST /portal/auth/register 请求，校验参数后委托业务层完成账号创建。</p>
     *
     * @param request 注册请求体，包含用户名、密码及扩展资料
     * @return 成功时返回统一成功结果
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        portalAuthService.register(request);
        return Result.success();
    }

    /**
     * 门户用户登录接口
     * <p>处理 POST /portal/auth/login 请求，校验凭证后返回携带 JWT 的登录结果。</p>
     *
     * @param request 登录请求体
     * @return 登录成功后的令牌与账号信息
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(authService.login(request), "登录成功");
    }
}
