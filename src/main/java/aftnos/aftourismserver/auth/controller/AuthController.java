package aftnos.aftourismserver.auth.controller;

import aftnos.aftourismserver.auth.dto.LoginRequest;
import aftnos.aftourismserver.auth.dto.LoginResponse;
import aftnos.aftourismserver.auth.dto.UserInfoResponse;
import aftnos.aftourismserver.auth.service.AuthService;
import aftnos.aftourismserver.auth.service.MenuQueryService;
import aftnos.aftourismserver.auth.service.UserInfoService;
import aftnos.aftourismserver.auth.vo.MenuRouteVO;
import aftnos.aftourismserver.common.result.Result;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 统一认证控制器，处理登录和用户信息查询等认证相关操作。
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserInfoService userInfoService;
    private final MenuQueryService menuQueryService;

    public AuthController(AuthService authService, UserInfoService userInfoService, MenuQueryService menuQueryService) {
        this.authService = authService;
        this.userInfoService = userInfoService;
        this.menuQueryService = menuQueryService;
    }

    /**
     * 登录接口，遵循 docs/login/login.md 中的字段与返回格式。
     *
     * @param request 登录请求体
     * @return token 与 refreshToken
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return Result.success(response, "登录成功");
    }

    /**
     * 获取当前登录用户信息，需在请求头 Authorization 中携带 Bearer Token。
     *
     * @return 用户基本信息、角色与可用按钮列表
     */
    @GetMapping("/info")
    public Result<UserInfoResponse> getUserInfo() {
        UserInfoResponse response = userInfoService.getCurrentUserInfo();
        return Result.success(response, "请求成功");
    }

    /**
     * 获取当前登录人的动态菜单，结构对齐 docs/RBAC/RBAC.md
     *
     * @return 可访问的菜单树
     */
    @GetMapping("/menus")
    public Result<List<MenuRouteVO>> loadMenus() {
        return Result.success(menuQueryService.loadCurrentUserMenus());
    }
}
