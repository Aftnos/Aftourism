package aftnos.aftourismserver.admin.vo;

import lombok.Data;

/**
 * 后台首页风景配置返回对象。
 */
@Data
public class HomeScenicAdminVO {
    private Long id;
    private Long scenicId;
    /** 景区名称，便于前端展示。 */
    private String scenicName;
    /** 景区封面图片。 */
    private String imageUrl;
    /** 排序值。 */
    private Integer sort;
    /** 是否启用展示。 */
    private Integer isEnabled;
}
