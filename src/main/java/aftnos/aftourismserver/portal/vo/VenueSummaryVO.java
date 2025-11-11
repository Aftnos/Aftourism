package aftnos.aftourismserver.portal.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 门户场馆列表信息
 */
@Data
public class VenueSummaryVO {
    private Long id;
    private String name;
    private String imageUrl;
    private String category;
    private Integer isFree;
    private BigDecimal ticketPrice;
    private String address;
    private String openTime;
    private Integer sort;
}
