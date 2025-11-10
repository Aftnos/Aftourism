package aftnos.aftourismserver.portal.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 门户景区列表展示对象
 */
@Data
public class ScenicSpotSummaryVO {
    private Long id;
    private String name;
    private String imageUrl;
    private String level;
    private BigDecimal ticketPrice;
    private String address;
    private String openTime;
    private Integer sort;
}
