package com.aftourism.portal.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 活动详情对象。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDetailVO {

    private Long id;
    private String name;
    private String coverUrl;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String category;
    private String venueName;
    private String intro;
    private String organizer;
    private String contactPhone;
    private List<CommentVO> comments;

    /** 留言内部类 */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentVO {
        private Long id;
        private Long userId;
        private String nickname;
        private String content;
        private LocalDateTime createTime;
    }
}
