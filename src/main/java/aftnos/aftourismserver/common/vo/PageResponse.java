package aftnos.aftourismserver.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * 通用分页返回体，对齐前端 Api.Common.PaginatedResponse 结构。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    /** 数据列表 */
    private List<T> records = Collections.emptyList();

    /** 当前页码 */
    private Integer current = 1;

    /** 每页条数 */
    private Integer size = 10;

    /** 总条数 */
    private Long total = 0L;
}
