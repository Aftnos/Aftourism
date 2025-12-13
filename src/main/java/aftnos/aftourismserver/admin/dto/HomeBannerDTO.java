package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 后台首页轮播图提交 DTO。
 */
@Data
public class HomeBannerDTO {
    /** 轮播图标题，便于后台标识 */
    private String title;

    /** 图片地址，必填用于展示 */
    @NotBlank(message = "轮播图地址不能为空")
    private String imageUrl;

    /** 跳转链接，可选 */
    private String linkUrl;

    /** 排序值，数值越大越靠前 */
    @Min(value = 0, message = "排序值不能为负数")
    private Integer sort = 0;

    /** 是否启用 */
    private Boolean enabled = true;
}
