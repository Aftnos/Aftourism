package com.aftourism.portal.controller;

import com.aftourism.common.pojo.ResponseResult;
import com.aftourism.portal.dto.UserLoginRequest;
import com.aftourism.portal.dto.UserRegisterRequest;
import com.aftourism.portal.service.UserService;
import com.aftourism.portal.vo.UserProfileVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前台用户接口。
 */
@RestController
@RequestMapping("/portal/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /** 用户注册 */
    @PostMapping("/register")
    public ResponseResult<Long> register(@Valid @RequestBody UserRegisterRequest request) {
        return ResponseResult.ok(userService.register(request));
    }

    /** 用户登录 */
    @PostMapping("/login")
    public ResponseResult<UserProfileVO> login(@Valid @RequestBody UserLoginRequest request) {
        return ResponseResult.ok(userService.login(request));
    }
}
