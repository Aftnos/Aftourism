package aftnos.aftourismserver.portal.vo;

import lombok.Data;

/**
 * 门户轮播图展示 VO。
 */
@Data
public class HomeBannerPortalVO {
    private Long id;
    private String title;
    private String imageUrl;
    private String linkUrl;
    private Integer sort;
}
