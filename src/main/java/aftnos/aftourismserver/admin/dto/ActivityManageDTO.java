package aftnos.aftourismserver.admin.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 活动管理创建/编辑参数
 */
@Data
public class ActivityManageDTO {

    /** 活动名称 */
    @NotBlank(message = "活动名称不能为空")
    @Size(max = 100, message = "活动名称不能超过100个字符")
    private String name;

    /** 封面地址 */
    @Size(max = 255, message = "封面地址长度不能超过255")
    private String coverUrl;

    /** 开始时间 */
    @NotNull(message = "请选择开始时间")
    private LocalDateTime startTime;

    /** 结束时间 */
    @NotNull(message = "请选择结束时间")
    private LocalDateTime endTime;

    /** 活动类别 */
    @Size(max = 50, message = "活动类别不能超过50个字符")
    private String category;

    /** 场馆ID */
    @NotNull(message = "请选择所属场馆")
    private Long venueId;

    /** 主办方 */
    @Size(max = 100, message = "主办单位不能超过100个字符")
    private String organizer;

    /** 联系电话 */
    @Size(max = 20, message = "联系电话不能超过20个字符")
    private String contactPhone;

    /** 活动简介 */
    @Size(max = 2000, message = "活动简介不能超过2000个字符")
    private String intro;

    /** 地址快照 */
    @Size(max = 255, message = "场馆地址不能超过255个字符")
    private String addressCache;

    /** 上线状态，0-未上线，1-已上线 */
    @Min(value = 0, message = "上线状态不合法")
    @Max(value = 1, message = "上线状态不合法")
    private Integer onlineStatus;
}
