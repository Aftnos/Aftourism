package com.aftourism.admin.controller;

import com.aftourism.admin.dto.AdminLoginRequest;
import com.aftourism.admin.service.AdminAuthService;
import com.aftourism.admin.vo.AdminLoginVO;
import com.aftourism.common.pojo.ResponseResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员认证接口。
 */
@RestController
@RequestMapping("/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    /**
     * 管理员登录。
     */
    @PostMapping("/login")
    public ResponseResult<AdminLoginVO> login(@Valid @RequestBody AdminLoginRequest request) {
        return ResponseResult.ok(adminAuthService.login(request));
    }
}
