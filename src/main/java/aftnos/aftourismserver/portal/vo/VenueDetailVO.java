package aftnos.aftourismserver.portal.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 门户场馆详情信息
 */
@Data
public class VenueDetailVO {
    private Long id;
    private String name;
    private String imageUrl;
    private String category;
    private Integer isFree;
    private BigDecimal ticketPrice;
    private String address;
    private String openTime;
    private String description;
    private String phone;
    private String website;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private Integer sort;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
