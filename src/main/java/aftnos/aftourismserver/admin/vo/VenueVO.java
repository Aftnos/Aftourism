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
    /** 高德POI ID */
    private String amapId;
    /** 类型标签 */
    private String tags;
    private String imageUrl;
    /** 场馆图片列表 */
    private String imageUrls;
    private String category;
    private Integer isFree;
    private BigDecimal ticketPrice;
    private String address;
    /** 省份 */
    private String province;
    /** 城市 */
    private String city;
    /** 区县 */
    private String district;
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
