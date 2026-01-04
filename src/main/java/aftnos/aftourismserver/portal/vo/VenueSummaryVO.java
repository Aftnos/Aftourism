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
    /** 类型标签 */
    private String tags;
    private String imageUrl;
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
    /** 联系电话 */
    private String phone;
    private Integer sort;
}
