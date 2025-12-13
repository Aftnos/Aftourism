package aftnos.aftourismserver.admin.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 首页文旅简介实体，对应表 t_home_intro。
 */
@Data
public class HomeIntro {
    private Long id;
    private String title;
    private String content;
    private String coverUrl;
    /** 封面类型：IMAGE 或 VIDEO，默认为 IMAGE。 */
    private String coverType;
    /** 首页风景展示数量上限。 */
    private Integer scenicLimit;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
