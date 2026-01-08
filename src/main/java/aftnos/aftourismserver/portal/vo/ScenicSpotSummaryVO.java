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
    /** 类型标签 */
    private String tags;
    private String imageUrl;
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
    /** 联系电话 */
    private String phone;
    /** 经度 */
    private BigDecimal longitude;
    /** 纬度 */
    private BigDecimal latitude;
    private Integer sort;
}
