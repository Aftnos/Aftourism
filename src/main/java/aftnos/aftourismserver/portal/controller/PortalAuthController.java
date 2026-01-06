package aftnos.aftourismserver.portal.controller;

import aftnos.aftourismserver.auth.dto.LoginRequest;
import aftnos.aftourismserver.auth.dto.LoginResponse;
import aftnos.aftourismserver.auth.dto.PortalUserInfoResponse;
import aftnos.aftourismserver.auth.dto.RegisterRequest;
import aftnos.aftourismserver.auth.dto.UserInfoUpdateRequest;
import aftnos.aftourismserver.auth.service.PortalAuthService;
import aftnos.aftourismserver.auth.service.PortalUserInfoService;
import aftnos.aftourismserver.common.result.Result;
import aftnos.aftourismserver.common.security.SecurityUtils;
import aftnos.aftourismserver.portal.dto.QualificationApplyRequest;
import aftnos.aftourismserver.portal.service.PortalQualificationService;
import aftnos.aftourismserver.portal.vo.QualificationStatusVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    private final PortalUserInfoService portalUserInfoService;
    private final PortalQualificationService portalQualificationService;

    /**
     * 门户用户注册接口
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        log.info("【门户注册】收到注册请求，用户名={}", request.getUsername());
        portalAuthService.register(request);
        return Result.success(null, "注册成功");
    }

    /**
     * 门户用户登录接口
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = portalAuthService.login(request);
        return Result.success(response, "登录成功");
    }

    /**
     * 获取当前门户用户信息
     */
    @GetMapping("/info")
    public Result<PortalUserInfoResponse> getUserInfo() {
        PortalUserInfoResponse response = portalUserInfoService.getCurrentPortalUserInfo();
        return Result.success(response, "请求成功");
    }

    /**
     * 修改当前登录门户用户的基础资料（昵称、性别、联系方式等）。
     *
     * @param request 更新请求体
     * @return 更新结果
     */
    @PutMapping("/info")
    public Result<Void> updateUserInfo(@Valid @RequestBody UserInfoUpdateRequest request) {
        portalUserInfoService.updateCurrentUserInfo(request);
        return Result.success(null, "个人信息已更新");
    }

    /**
     * 门户用户提交资质申请。
     */
    @PostMapping("/qualification/apply")
    public Result<Void> applyQualification(@Valid @RequestBody QualificationApplyRequest request) {
        Long userId = SecurityUtils.currentPortalUserIdOrThrow();
        portalQualificationService.submit(userId, request);
        return Result.success(null, "资质申请已提交");
    }

    /**
     * 查询当前用户最新资质申请状态。
     */
    @GetMapping("/qualification/status")
    public Result<QualificationStatusVO> qualificationStatus() {
        Long userId = SecurityUtils.currentPortalUserIdOrThrow();
        return Result.success(portalQualificationService.latestStatus(userId), "请求成功");
    }
}
