package aftnos.aftourismserver.admin.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知公告实体类，对应表 t_notice
 */
@Data
public class Notice {
    /** 主键ID */
    private Long id;
    /** 通知标题 */
    private String title;
    /** 通知正文内容 */
    private String content;
    /** 发布时间 */
    private LocalDateTime publishTime;
    /** 发布人或发布单位 */
    private String author;
    /** 状态：1已发布 0已下线 */
    private Integer status;
    /** 浏览量 */
    private Long viewCount;
    /** 逻辑删除标识：0否 1是 */
    private Integer isDeleted;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 更新时间 */
    private LocalDateTime updateTime;
}
