package aftnos.aftourismserver.admin.vo;

import lombok.Data;

/**
 * 后台首页轮播图 VO。
 */
@Data
public class HomeBannerAdminVO {
    private Long id;
    private String title;
    private String imageUrl;
    private String linkUrl;
    private Integer sort;
    private Integer isEnabled;
}
