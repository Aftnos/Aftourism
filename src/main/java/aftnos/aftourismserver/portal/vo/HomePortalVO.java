package aftnos.aftourismserver.portal.vo;

import lombok.Data;

import java.util.List;

/**
 * 门户首页聚合展示对象，包含轮播与简介信息。
 */
@Data
public class HomePortalVO {
    private List<HomeBannerPortalVO> banners;
    private HomeIntroPortalVO intro;
    private List<HomeScenicPortalVO> scenics;
    /** 风景展示数量上限，便于前端展示提示。 */
    private Integer scenicLimit;
}
