package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.AdminPasswordUpdateRequest;
import aftnos.aftourismserver.admin.dto.AdminProfileUpdateRequest;
import aftnos.aftourismserver.admin.service.AdminProfileService;
import aftnos.aftourismserver.admin.vo.AdminProfileVO;
import aftnos.aftourismserver.common.result.Result;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理员个人中心接口。
 */
@RestController
@RequestMapping("/admin/profile")
public class AdminProfileController {

    private final AdminProfileService adminProfileService;

    public AdminProfileController(AdminProfileService adminProfileService) {
        this.adminProfileService = adminProfileService;
    }

    /**
     * 获取当前管理员资料。
     */
    @GetMapping
    public Result<AdminProfileVO> profile() {
        return Result.success(adminProfileService.currentProfile());
    }

    /**
     * 更新当前管理员资料。
     */
    @PutMapping
    public Result<Void> updateProfile(@Valid @RequestBody AdminProfileUpdateRequest request) {
        adminProfileService.updateProfile(request);
        return Result.success(null, "个人资料更新成功");
    }

    /**
     * 更新当前管理员密码。
     */
    @PutMapping("/password")
    public Result<Void> updatePassword(@Valid @RequestBody AdminPasswordUpdateRequest request) {
        adminProfileService.updatePassword(request);
        return Result.success(null, "密码修改成功");
    }
}
