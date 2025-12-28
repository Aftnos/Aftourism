package aftnos.aftourismserver.admin.service;

import aftnos.aftourismserver.admin.dto.WatermarkSettingUpdateRequest;
import aftnos.aftourismserver.admin.vo.WatermarkSettingVO;

/**
 * 后台系统设置服务。
 */
public interface AdminSystemSettingService {

    /**
     * 获取全局水印设置。
     *
     * @return 水印设置
     */
    WatermarkSettingVO loadWatermarkSetting();

    /**
     * 更新全局水印设置。
     *
     * @param request 更新请求
     */
    void updateWatermarkSetting(WatermarkSettingUpdateRequest request);
}
