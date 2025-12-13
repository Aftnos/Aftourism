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
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
