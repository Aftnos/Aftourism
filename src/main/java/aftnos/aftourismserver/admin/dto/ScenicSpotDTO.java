package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 景区新增或修改请求体
 */
@Data
public class ScenicSpotDTO {
    /** 景区名称 */
    @NotBlank(message = "景区名称不能为空")
    private String name;

    /** 高德POI ID */
    @Size(max = 32, message = "高德ID长度不能超过32位")
    private String amapId;

    /** 类型标签，多个标签使用分号分隔 */
    @Size(max = 255, message = "类型标签长度不能超过255位")
    private String tags;

    /** 景区图片地址 */
    private String imageUrl;

    /** 景区图片列表，多个链接使用分号分隔 */
    private String imageUrls;

    /** 景区等级 */
    private String level;

    /** 门票价格，允许为空 */
    @DecimalMin(value = "0.00", inclusive = true, message = "门票价格不能为负数")
    @Digits(integer = 8, fraction = 2, message = "门票价格格式不正确")
    private BigDecimal ticketPrice;

    /** 景区地址 */
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

    /** 景区简介 */
    private String intro;

    /** 联系电话 */
    @Size(max = 20, message = "联系电话长度不能超过20位")
    private String phone;

    /** 官网地址 */
    private String website;

    /** 经度 */
    @Digits(integer = 4, fraction = 6, message = "经度格式不正确")
    private BigDecimal longitude;

    /** 纬度 */
    @Digits(integer = 4, fraction = 6, message = "纬度格式不正确")
    private BigDecimal latitude;

    /** 排序值，越大越靠前 */
    @Min(value = 0, message = "排序值不能为负数")
    private Integer sort = 0;
}
