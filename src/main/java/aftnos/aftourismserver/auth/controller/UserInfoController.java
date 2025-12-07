package aftnos.aftourismserver.auth.controller;

import aftnos.aftourismserver.auth.dto.UserInfoResponse;
import aftnos.aftourismserver.auth.service.UserInfoService;
import aftnos.aftourismserver.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户信息接口控制器，遵循 docs/login/login.md 的响应格式返回当前登录用户信息。
 */
@RestController
@RequestMapping("/user")
public class UserInfoController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
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
}
