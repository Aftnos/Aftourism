package aftnos.aftourismserver.admin.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 景区实体对象，对应表 t_scenic_spot
 */
@Data
public class ScenicSpot {
    private Long id;
    /** 高德POI ID，方便与地图平台数据关联 */
    private String amapId;
    private String name;
    /** 类型标签（来自高德类型标签，多个以分号分隔） */
    private String tags;
    private String imageUrl;
    /** 景区图片列表，多个链接使用分号分隔 */
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
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
