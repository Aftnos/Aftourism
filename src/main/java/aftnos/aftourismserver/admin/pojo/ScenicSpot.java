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
    private String name;
    private String imageUrl;
    private String level;
    private BigDecimal ticketPrice;
    private String address;
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
