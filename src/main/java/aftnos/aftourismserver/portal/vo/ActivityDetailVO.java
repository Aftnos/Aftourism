package aftnos.aftourismserver.portal.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 门户端活动详情展示对象
 */
@Data
public class ActivityDetailVO {

    /** 活动ID */
    private Long id;

    /** 活动名称 */
    private String name;

    /** 活动封面图 */
    private String coverUrl;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /** 活动类别 */
    private String category;

    /** 场馆ID */
    private Long venueId;

    /** 场馆名称 */
    private String venueName;

    /** 主办单位 */
    private String organizer;

    /** 联系电话 */
    private String contactPhone;

    /** 活动详情内容（富文本） */
    private String intro;

    /** 场馆地址快照 */
    private String addressCache;

    /** 浏览量 */
    private Long viewCount;

    /** 收藏量 */
    private Long favoriteCount;
}
