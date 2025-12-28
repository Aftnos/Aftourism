package aftnos.aftourismserver.admin.pojo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统配置实体，对应表 t_system_setting。
 */
@Data
public class SystemSetting {

    /** 主键 */
    private Long id;

    /** 配置键 */
    private String settingKey;

    /** 配置值 */
    private String settingValue;

    /** 备注 */
    private String remark;

    /** 逻辑删除 */
    private Integer isDeleted;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 更新时间 */
    private LocalDateTime updateTime;
}
