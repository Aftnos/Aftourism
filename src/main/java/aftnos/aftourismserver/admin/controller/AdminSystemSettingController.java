package aftnos.aftourismserver.admin.controller;

import aftnos.aftourismserver.admin.dto.WatermarkSettingUpdateRequest;
import aftnos.aftourismserver.admin.service.AdminSystemSettingService;
import aftnos.aftourismserver.admin.vo.WatermarkSettingVO;
import aftnos.aftourismserver.common.result.Result;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台系统设置接口。
 */
@RestController
@RequestMapping("/admin/system/backend")
public class AdminSystemSettingController {

    private final AdminSystemSettingService adminSystemSettingService;

    public AdminSystemSettingController(AdminSystemSettingService adminSystemSettingService) {
        this.adminSystemSettingService = adminSystemSettingService;
    }

    /**
     * 获取全局水印设置。
     */
    @GetMapping("/watermark")
    public Result<WatermarkSettingVO> loadWatermarkSetting() {
        return Result.success(adminSystemSettingService.loadWatermarkSetting());
    }

    /**
     * 更新全局水印设置。
     */
    @PutMapping("/watermark")
    public Result<Void> updateWatermarkSetting(@Valid @RequestBody WatermarkSettingUpdateRequest request) {
        adminSystemSettingService.updateWatermarkSetting(request);
        return Result.success(null, "水印设置已更新");
    }
}
