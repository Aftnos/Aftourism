package aftnos.aftourismserver.admin.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 后台首页内容保存 DTO，包含轮播和文旅简介配置。
 */
@Data
public class HomeContentSaveDTO {
    /** 文旅简介标题，可用于前台展示主标题 */
    @NotBlank(message = "简介标题不能为空")
    private String title;

    /** 文旅简介正文，支持富文本 */
    @NotBlank(message = "简介内容不能为空")
    private String content;

    /** 简介配图，可选 */
    private String coverUrl;

    /** 封面类型：IMAGE/VIDEO，默认 IMAGE */
    private String coverType;

    /** 风景展示数量 */
    @Min(value = 1, message = "风景展示数量至少为1")
    private Integer scenicLimit;

    /** 轮播图列表 */
    @Valid
    @Size(max = 10, message = "轮播图数量最多10条")
    private List<HomeBannerDTO> banners;

    /** 风景展示配置 */
    @Valid
    @Size(max = 12, message = "风景展示数量最多12条")
    private List<HomeScenicDTO> scenics;
}
