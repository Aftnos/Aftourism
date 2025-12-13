package aftnos.aftourismserver.admin.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 首页风景配置实体，对应表 t_home_scenic。
 */
@Data
public class HomeScenic {
    private Long id;
    /** 关联的景区ID。 */
    private Long scenicId;
    /** 展示排序，值越大越靠前。 */
    private Integer sort;
    /** 是否启用展示：1启用 0禁用。 */
    private Integer isEnabled;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
