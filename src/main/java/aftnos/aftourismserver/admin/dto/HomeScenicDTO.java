package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 首页风景配置 DTO。
 */
@Data
public class HomeScenicDTO {
    /** 景区主键ID，必填。 */
    @NotNull(message = "请选择展示的景区")
    private Long scenicId;
    /** 排序，越大越靠前。 */
    private Integer sort;
    /** 是否启用展示。 */
    private Boolean enabled;
}
