package aftnos.aftourismserver.portal.vo;

import lombok.Data;

/**
 * 门户文旅简介 VO。
 */
@Data
public class HomeIntroPortalVO {
    private String title;
    private String content;
    private String coverUrl;
    /** 封面类型：IMAGE/VIDEO。 */
    private String coverType;
}
