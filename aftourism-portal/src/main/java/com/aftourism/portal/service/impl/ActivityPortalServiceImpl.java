package com.aftourism.portal.service.impl;

import com.aftourism.common.exception.BusinessException;
import com.aftourism.common.pojo.ResultCode;
import com.aftourism.common.util.BeanCopyUtils;
import com.aftourism.portal.dto.ActivityApplyRequest;
import com.aftourism.portal.dto.CommentRequest;
import com.aftourism.portal.mapper.ActivityCommentMapper;
import com.aftourism.portal.mapper.ActivityMapper;
import com.aftourism.portal.mapper.UserMapper;
import com.aftourism.portal.mapper.VenueMapper;
import com.aftourism.portal.pojo.Activity;
import com.aftourism.portal.pojo.ActivityComment;
import com.aftourism.portal.pojo.User;
import com.aftourism.portal.pojo.Venue;
import com.aftourism.portal.service.ActivityPortalService;
import com.aftourism.portal.vo.ActivityDetailVO;
import com.aftourism.portal.vo.ActivityVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 活动服务实现。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityPortalServiceImpl implements ActivityPortalService {

    private final ActivityMapper activityMapper;
    private final ActivityCommentMapper activityCommentMapper;
    private final VenueMapper venueMapper;
    private final UserMapper userMapper;

    @Override
    public List<ActivityVO> listActivities() {
        List<Activity> activities = activityMapper.selectOnlineList();
        return activities.stream().map(activity -> {
            Venue venue = venueMapper.selectById(activity.getVenueId());
            return ActivityVO.builder()
                    .id(activity.getId())
                    .name(activity.getName())
                    .coverUrl(activity.getCoverUrl())
                    .startTime(activity.getStartTime())
                    .endTime(activity.getEndTime())
                    .category(activity.getCategory())
                    .venueName(venue != null ? venue.getName() : "")
                    .favoriteCount(activity.getFavoriteCount())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public ActivityDetailVO getDetail(Long id) {
        Activity activity = activityMapper.selectById(id);
        if (activity == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "活动不存在");
        }
        Venue venue = venueMapper.selectById(activity.getVenueId());
        List<ActivityComment> comments = activityCommentMapper.selectByActivity(id);
        List<ActivityDetailVO.CommentVO> commentVOS = comments.stream().map(comment -> {
            User user = userMapper.findById(comment.getUserId());
            return ActivityDetailVO.CommentVO.builder()
                    .id(comment.getId())
                    .userId(comment.getUserId())
                    .nickname(user != null ? user.getNickname() : "游客")
                    .content(comment.getContent())
                    .createTime(comment.getCreateTime())
                    .build();
        }).collect(Collectors.toList());
        return ActivityDetailVO.builder()
                .id(activity.getId())
                .name(activity.getName())
                .coverUrl(activity.getCoverUrl())
                .startTime(activity.getStartTime())
                .endTime(activity.getEndTime())
                .category(activity.getCategory())
                .venueName(venue != null ? venue.getName() : "")
                .intro(activity.getIntro())
                .organizer(activity.getOrganizer())
                .contactPhone(activity.getContactPhone())
                .comments(commentVOS)
                .build();
    }

    @Override
    public Long apply(ActivityApplyRequest request, Long userId) {
        Activity activity = BeanCopyUtils.copy(request, Activity::new);
        activity.setApplyUserId(userId);
        activity.setApplyStatus(0);
        activity.setOnlineStatus(0);
        activity.setFavoriteCount(0L);
        activity.setViewCount(0L);
        activityMapper.insert(activity);
        log.info("用户:{} 提交活动申报，ID:{}", userId, activity.getId());
        return activity.getId();
    }

    @Override
    public Long comment(CommentRequest request, Long userId) {
        Activity activity = activityMapper.selectById(request.getActivityId());
        if (activity == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "活动不存在");
        }
        ActivityComment comment = new ActivityComment();
        comment.setActivityId(request.getActivityId());
        comment.setUserId(userId);
        comment.setContent(request.getContent());
        comment.setParentId(request.getParentId());
        comment.setLikeCount(0);
        activityCommentMapper.insert(comment);
        return comment.getId();
    }
}
