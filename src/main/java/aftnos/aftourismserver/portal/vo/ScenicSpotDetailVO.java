package aftnos.aftourismserver.portal.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 门户景区详情展示对象
 */
@Data
public class ScenicSpotDetailVO {
    private Long id;
    private String name;
    /** 高德POI ID */
    private String amapId;
    /** 类型标签 */
    private String tags;
    private String imageUrl;
    /** 景区图片列表 */
    private String imageUrls;
    private String level;
    private BigDecimal ticketPrice;
    private String address;
    /** 省份 */
    private String province;
    /** 城市 */
    private String city;
    /** 区县 */
    private String district;
    private String openTime;
    private String intro;
    private String phone;
    private String website;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private Integer sort;
    /** 浏览量，便于前端展示点击热度 */
    private Long viewCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
