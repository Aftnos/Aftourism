package aftnos.aftourismserver.portal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 活动申报请求参数
 */
@Data
public class ActivityApplyDTO {

    /** 活动名称 */
    @NotBlank(message = "活动名称不能为空")
    private String name;

    /** 活动封面图 */
    private String coverUrl;

    /** 活动开始时间 */
    @NotNull(message = "活动开始时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /** 活动结束时间 */
    @NotNull(message = "活动结束时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /** 活动类别 */
    private String category;

    /** 场馆ID */
    @NotNull(message = "场馆不能为空")
    private Long venueId;

    /** 主办单位 */
    private String organizer;

    /** 联系电话 */
    private String contactPhone;

    /** 活动简介 */
    private String intro;

    /** 申报备注，供审核人参考 */
    @Size(max = 255, message = "备注不能超过255个字符")
    private String auditRemark;
}
