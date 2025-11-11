package aftnos.aftourismserver.portal.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 门户活动列表展示对象
 */
@Data
public class ActivitySummaryVO {
    private Long id;
    private String name;
    private String coverUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    private String category;
    private Long venueId;
    private String venueName;
    private String addressCache;
    private Integer onlineStatus;
}
