package aftnos.aftourismserver.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

/**
 * 访问趋势数据
 */
@Data
public class VisitTrendVO {

    /** 统计日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate statDate;

    /** 页面访问量 */
    private Long pvCount;

    /** 独立访客 */
    private Long uvCount;
}
