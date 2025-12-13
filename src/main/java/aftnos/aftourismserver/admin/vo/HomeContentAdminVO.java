package aftnos.aftourismserver.admin.vo;

import lombok.Data;

import java.util.List;

/**
 * 后台首页内容聚合 VO，提供给页面编辑使用。
 */
@Data
public class HomeContentAdminVO {
    private HomeIntroAdminVO intro;
    private List<HomeBannerAdminVO> banners;
    private List<HomeScenicAdminVO> scenics;
    /** 风景展示数量上限。 */
    private Integer scenicLimit;
}
