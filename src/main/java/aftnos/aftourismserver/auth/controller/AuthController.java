package aftnos.aftourismserver.auth.controller;

import aftnos.aftourismserver.auth.dto.LoginRequest;
import aftnos.aftourismserver.auth.dto.LoginResponse;
import aftnos.aftourismserver.auth.dto.RegisterRequest;
import aftnos.aftourismserver.auth.service.AuthService;
import aftnos.aftourismserver.common.result.Result;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 认证控制器类
 * 处理用户认证相关的HTTP请求，包括用户注册和登录功能
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * 构造器代替@Autowired注解
     */
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 用户注册接口
     * 处理POST请求 /api/auth/register
     * @param request 注册请求数据传输对象，包含用户名和密码等信息
     * @return 注册结果，成功返回空数据的Result对象
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return Result.success();
    }

    /**
     * 用户登录接口
     * 处理POST请求 /api/auth/login
     * @param request 登录请求数据传输对象，包含用户名和密码
     * @return 登录结果，成功返回包含JWT令牌的LoginResponse对象
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }
}