package aftnos.aftourismserver.admin.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 场馆后台展示对象
 */
@Data
public class VenueVO {
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
