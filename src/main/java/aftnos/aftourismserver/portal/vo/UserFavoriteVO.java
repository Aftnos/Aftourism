package aftnos.aftourismserver.portal.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户收藏返回对象
 */
@Data
public class UserFavoriteVO {

    /** 收藏记录ID */
    private Long id;

    /** 收藏类型 */
    private String targetType;

    /** 收藏目标ID */
    private Long targetId;

    /** 收藏目标名称 */
    private String targetName;

    /** 收藏目标封面或图片 */
    private String targetCover;

    /** 收藏创建时间 */
    private LocalDateTime createTime;
}
