package aftnos.aftourismserver.portal.controller;

import aftnos.aftourismserver.auth.dto.RegisterRequest;
import aftnos.aftourismserver.auth.service.PortalAuthService;
import aftnos.aftourismserver.common.result.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 门户认证控制器，提供注册接口。
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/portal/auth")
public class PortalAuthController {

    private final PortalAuthService portalAuthService;

    /**
     * 门户用户注册接口
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        log.info("【门户注册】收到注册请求，用户名={}", request.getUsername());
        portalAuthService.register(request);
        return Result.success(null, "注册成功");
    }
}
