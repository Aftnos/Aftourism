package aftnos.aftourismserver.admin.vo;

import aftnos.aftourismserver.admin.enums.RecycleType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 回收站展示对象
 */
@Data
public class RecycleItemVO {

    /** 主键ID */
    private Long id;
    /** 数据类型 */
    private RecycleType type;
    /** 标题或名称 */
    private String title;
    /** 删除时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deletedTime;
    /** 操作人，如有记录 */
    private String operator;
    /** 额外提示信息 */
    private String extraInfo;
}
