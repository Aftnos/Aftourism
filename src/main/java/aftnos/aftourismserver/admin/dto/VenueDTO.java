package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 场馆新增/修改请求参数
 */
@Data
public class VenueDTO {

    /** 场馆名称 */
    @NotBlank(message = "场馆名称不能为空")
    private String name;

    /** 高德POI ID */
    @Size(max = 32, message = "高德ID长度不能超过32位")
    private String amapId;

    /** 类型标签，多个标签使用分号分隔 */
    @Size(max = 255, message = "类型标签长度不能超过255位")
    private String tags;

    /** 场馆图片地址 */
    private String imageUrl;

    /** 场馆图片列表，多个链接使用分号分隔 */
    private String imageUrls;

    /** 场馆类别 */
    private String category;

    /** 是否免费开放：1表示免费，0表示收费 */
    @NotNull(message = "是否免费字段不能为空")
    @Min(value = 0, message = "是否免费字段取值非法")
    @Max(value = 1, message = "是否免费字段取值非法")
    private Integer isFree;

    /** 门票价格（收费场馆需要填写） */
    @DecimalMin(value = "0", inclusive = true, message = "票价不能为负数")
    private BigDecimal ticketPrice;

    /** 场馆地址 */
    private String address;

    /** 省份 */
    @Size(max = 50, message = "省份长度不能超过50位")
    private String province;

    /** 城市 */
    @Size(max = 50, message = "城市长度不能超过50位")
    private String city;

    /** 区县 */
    @Size(max = 50, message = "区县长度不能超过50位")
    private String district;

    /** 开放时间描述 */
    private String openTime;

    /** 场馆描述 */
    private String description;

    /** 联系电话 */
    private String phone;

    /** 官网地址 */
    private String website;

    /** 经度 */
    private BigDecimal longitude;

    /** 纬度 */
    private BigDecimal latitude;

    /** 排序值 */
    private Integer sort;
}
