package aftnos.aftourismserver.admin.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 景区后台展示对象
 */
@Data
public class ScenicSpotVO {
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
