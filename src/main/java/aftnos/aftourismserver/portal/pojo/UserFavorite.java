package aftnos.aftourismserver.portal.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户收藏实体，映射 t_user_favorite 表
 */
@Data
public class UserFavorite {

    /** 主键ID */
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 收藏目标类型：SCENIC/VENUE/ACTIVITY */
    private String targetType;

    /** 收藏目标ID */
    private Long targetId;

    /** 逻辑删除标记：0-正常 1-删除 */
    private Integer isDeleted;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
