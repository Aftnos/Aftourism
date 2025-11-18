package aftnos.aftourismserver.portal.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 门户活动列表展示对象
 * 用于向前端展示活动的基本信息摘要
 */
@Data
public class ActivitySummaryVO {
    /**
     * 活动唯一标识符
     */
    private Long id;
    
    /**
     * 活动名称
     */
    private String name;
    
    /**
     * 活动封面图片URL地址
     */
    private String coverUrl;
    
    /**
     * 活动开始时间
     * 格式化为: yyyy-MM-dd HH:mm:ss
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    
    /**
     * 活动结束时间
     * 格式化为: yyyy-MM-dd HH:mm:ss
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    
    /**
     * 活动分类标签
     */
    private String category;
    
    /**
     * 关联场馆ID
     */
    private Long venueId;
    
    /**
     * 关联场馆名称
     */
    private String venueName;
    
    /**
     * 场馆地址缓存信息
     */
    private String addressCache;
    
    /**
     * 活动上线状态
     * 例如: 0-未上线, 1-已上线
     */
    private Integer onlineStatus;
}