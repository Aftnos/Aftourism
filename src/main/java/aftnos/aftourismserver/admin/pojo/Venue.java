package aftnos.aftourismserver.admin.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 场馆实体，对应数据库表 t_venue
 */
@Data
public class Venue {

    /** 主键ID */
    private Long id;
    /** 场馆名称 */
    private String name;
    /** 场馆图片 */
    private String imageUrl;
    /** 场馆类别 */
    private String category;
    /** 是否免费开放：1表示免费，0表示收费 */
    private Integer isFree;
    /** 门票价格（收费场馆需要填写） */
    private BigDecimal ticketPrice;
    /** 场馆详细地址 */
    private String address;
    /** 场馆开放时间描述 */
    private String openTime;
    /** 场馆详细描述 */
    private String description;
    /** 联系电话 */
    private String phone;
    /** 官网地址 */
    private String website;
    /** 经度 */
    private BigDecimal longitude;
    /** 纬度 */
    private BigDecimal latitude;
    /** 排序值，越大越靠前 */
    private Integer sort;
    /** 是否逻辑删除：0-未删除 1-已删除 */
    private Integer isDeleted;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 修改时间 */
    private LocalDateTime updateTime;
}
