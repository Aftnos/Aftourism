package aftnos.aftourismserver.portal.vo;

import lombok.Data;

/**
 * 门户首页风景展示 VO。
 */
@Data
public class HomeScenicPortalVO {
    private Long id;
    private String name;
    private String imageUrl;
    private String description;
    private Integer sort;
}
