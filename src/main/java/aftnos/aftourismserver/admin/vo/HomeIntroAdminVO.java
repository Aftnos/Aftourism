package aftnos.aftourismserver.admin.vo;

import lombok.Data;

/**
 * 后台文旅简介 VO。
 */
@Data
public class HomeIntroAdminVO {
    private Long id;
    private String title;
    private String content;
    private String coverUrl;
    /** 封面类型：IMAGE/VIDEO。 */
    private String coverType;
}
