package aftnos.aftourismserver.admin.vo;

import lombok.Data;

/**
 * 全局水印设置返回对象。
 */
@Data
public class WatermarkSettingVO {

    /** 是否启用水印 */
    private Boolean visible;

    /** 水印内容模板 */
    private String content;
}
