package aftnos.aftourismserver.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

/**
 * 新增用户趋势数据
 */
@Data
public class NewUserTrendVO {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate statDate;
    private Long total;
}
