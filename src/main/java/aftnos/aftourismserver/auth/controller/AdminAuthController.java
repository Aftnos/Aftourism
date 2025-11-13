package aftnos.aftourismserver.auth.controller;

import aftnos.aftourismserver.auth.dto.LoginRequest;
import aftnos.aftourismserver.auth.dto.LoginResponse;
import aftnos.aftourismserver.auth.service.AdminAuthService;
import aftnos.aftourismserver.common.result.Result;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台管理员认证控制器。
 */
@RestController
@RequestMapping("/admin/auth")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    public AdminAuthController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }

    /**
     * 管理员登录接口。
     *
     * @param request 登录请求体
     * @return 登录成功后返回的 JWT 及基础信息
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(adminAuthService.login(request));
    }
}
