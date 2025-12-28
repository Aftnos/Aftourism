package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.WatermarkSettingUpdateRequest;
import aftnos.aftourismserver.admin.mapper.SystemSettingMapper;
import aftnos.aftourismserver.admin.pojo.SystemSetting;
import aftnos.aftourismserver.admin.service.AdminSystemSettingService;
import aftnos.aftourismserver.admin.vo.WatermarkSettingVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 后台系统设置服务实现。
 */
@Service
public class AdminSystemSettingServiceImpl implements AdminSystemSettingService {

    private static final String KEY_WATERMARK_VISIBLE = "WATERMARK_VISIBLE";
    private static final String KEY_WATERMARK_CONTENT = "WATERMARK_CONTENT";

    private final SystemSettingMapper systemSettingMapper;

    public AdminSystemSettingServiceImpl(SystemSettingMapper systemSettingMapper) {
        this.systemSettingMapper = systemSettingMapper;
    }

    @Override
    public WatermarkSettingVO loadWatermarkSetting() {
        WatermarkSettingVO vo = new WatermarkSettingVO();
        vo.setVisible(loadBooleanSetting(KEY_WATERMARK_VISIBLE, false));
        vo.setContent(loadStringSetting(KEY_WATERMARK_CONTENT, ""));
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWatermarkSetting(WatermarkSettingUpdateRequest request) {
        if (request == null) {
            return;
        }
        if (request.getVisible() != null) {
            saveSetting(KEY_WATERMARK_VISIBLE, request.getVisible() ? "1" : "0", "全局水印开关");
        }
        if (request.getContent() != null) {
            saveSetting(KEY_WATERMARK_CONTENT, trimToEmpty(request.getContent()), "全局水印内容模板");
        }
    }

    private boolean loadBooleanSetting(String key, boolean defaultValue) {
        SystemSetting setting = systemSettingMapper.findByKey(key);
        if (setting == null || !StringUtils.hasText(setting.getSettingValue())) {
            return defaultValue;
        }
        return "1".equals(setting.getSettingValue()) || "true".equalsIgnoreCase(setting.getSettingValue());
    }

    private String loadStringSetting(String key, String defaultValue) {
        SystemSetting setting = systemSettingMapper.findByKey(key);
        if (setting == null || setting.getSettingValue() == null) {
            return defaultValue;
        }
        return setting.getSettingValue();
    }

    private void saveSetting(String key, String value, String remark) {
        SystemSetting setting = new SystemSetting();
        setting.setSettingKey(key);
        setting.setSettingValue(value);
        setting.setRemark(remark);
        systemSettingMapper.upsert(setting);
    }

    private String trimToEmpty(String value) {
        if (!StringUtils.hasText(value)) {
            return "";
        }
        return value.trim();
    }
}
