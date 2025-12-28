package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 全局水印设置更新请求。
 */
@Data
public class WatermarkSettingUpdateRequest {

    /** 是否启用水印 */
    private Boolean visible;

    /** 水印内容模板 */
    @Size(max = 255, message = "水印内容长度不能超过255个字符")
    private String content;
}
