package aftnos.aftourismserver.portal.service.impl;

import aftnos.aftourismserver.admin.mapper.ActivityMapper;
import aftnos.aftourismserver.admin.pojo.Activity;
import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.dto.ActivityCommentCreateDTO;
import aftnos.aftourismserver.portal.dto.ActivityCommentPageQuery;
import aftnos.aftourismserver.portal.enums.PortalNotificationTypeEnum;
import aftnos.aftourismserver.portal.mapper.ActivityCommentMapper;
import aftnos.aftourismserver.portal.pojo.ActivityComment;
import aftnos.aftourismserver.portal.service.PortalNotificationService;
import aftnos.aftourismserver.portal.service.ActivityCommentService;
import aftnos.aftourismserver.portal.vo.ActivityCommentVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 活动留言业务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityCommentServiceImpl implements ActivityCommentService {

    private final ActivityMapper activityMapper;
    private final ActivityCommentMapper activityCommentMapper;
    private final UserMapper userMapper;
    private final PortalNotificationService portalNotificationService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CacheManager cacheManager;

    /** 留言点赞缓存前缀，值为点赞用户ID集合 */
    private static final String COMMENT_LIKE_KEY_PREFIX = "activity:comment:like:";

    /** 留言分页缓存名称，与缓存管理器前缀组合后存储 */
    private static final String ACTIVITY_COMMENT_PAGE_CACHE = "activityCommentPages";

    /** 留言分页缓存前缀，便于按活动 ID 批量失效 */
    private static final String ACTIVITY_COMMENT_CACHE_PREFIX = "cache:" + ACTIVITY_COMMENT_PAGE_CACHE + ":activity:";

    @Override
    public Long addComment(Long activityId, ActivityCommentCreateDTO dto, Long userId) {
        log.info("【门户-活动留言】新增留言，活动ID={}，用户ID={}，parentId={}", activityId, userId, dto.getParentId());
        Activity activity = activityMapper.selectById(activityId);
        if (activity == null) {
            throw new BusinessException("活动不存在或已下架");
        }

        Long parentId = dto.getParentId();
        Long mentionUserId = dto.getMentionUserId();
        ActivityComment parent = null;
        if (parentId != null) {
            parent = activityCommentMapper.selectById(parentId);
            if (parent == null || parent.getIsDeleted() != null && parent.getIsDeleted() == 1) {
                throw new BusinessException("父级留言不存在或已被删除");
            }
            if (!Objects.equals(parent.getActivityId(), activityId)) {
                throw new BusinessException("父级留言不属于当前活动");
            }
            if (Objects.equals(parent.getUserId(), mentionUserId)) {
                mentionUserId = parent.getUserId();
            }
        }

        ActivityComment comment = new ActivityComment();
        comment.setActivityId(activityId);
        comment.setUserId(userId);
        comment.setContent(dto.getContent());
        comment.setParentId(parentId);
        comment.setMentionUserId(mentionUserId);
        comment.setLikeCount(0);
        comment.setIsDeleted(0);
        LocalDateTime now = LocalDateTime.now();
        comment.setCreateTime(now);
        comment.setUpdateTime(now);
        activityCommentMapper.insert(comment);
        log.info("【门户-活动留言】新增留言成功，生成ID={}", comment.getId());
        clearActivityCommentCache(activityId);
        notifyUsers(parent, comment, userId);
        return comment.getId();
    }

    @Override
    public PageInfo<ActivityCommentVO> pageComments(Long activityId, ActivityCommentPageQuery query) {
        log.info("【门户-活动留言】分页查询留言，活动ID={}，parentId={}，页码={}，每页={}",
                activityId, query.getParentId(), query.getCurrent(), query.getSize());
        String cacheKey = buildPageCacheKey(activityId, query);
        Cache cache = cacheManager.getCache(ACTIVITY_COMMENT_PAGE_CACHE);
        if (cache != null) {
            PageInfo<ActivityCommentVO> cached = cache.get(cacheKey, PageInfo.class);
            if (cached != null) {
                log.info("【门户-活动留言】分页留言命中缓存，key={}", cacheKey);
                return cached;
            }
        }

        PageHelper.startPage(query.getCurrent(), query.getSize());
        List<ActivityCommentVO> list = activityCommentMapper.pageList(activityId, query.getParentId());
        PageInfo<ActivityCommentVO> pageInfo = new PageInfo<>(list);

        // 中文注释：在第一页加载楼中楼回复，减少前端额外查询
        if (query.getParentId() == null && !list.isEmpty()) {
            List<Long> parentIds = list.stream().map(ActivityCommentVO::getId).toList();
            List<ActivityCommentVO> children = activityCommentMapper.listByParentIds(parentIds);
            Map<Long, List<ActivityCommentVO>> childMap = children.stream()
                    .collect(Collectors.groupingBy(ActivityCommentVO::getParentId, LinkedHashMap::new, Collectors.toList()));
            for (ActivityCommentVO vo : list) {
                vo.setChildren(childMap.getOrDefault(vo.getId(), Collections.emptyList()));
            }
            enrichUserInfo(flattenWithChildren(list));
        } else {
            enrichUserInfo(list);
        }
        if (cache != null) {
            cache.put(cacheKey, pageInfo);
        }
        return pageInfo;
    }

    @Override
    public void likeComment(Long commentId, Long userId) {
        log.info("【门户-活动留言】开始点赞处理，留言ID={}，用户ID={}", commentId, userId);
        ActivityComment comment = activityCommentMapper.selectById(commentId);
        if (comment == null || comment.getIsDeleted() != null && comment.getIsDeleted() == 1) {
            throw new BusinessException("留言不存在或已被删除");
        }
        String key = COMMENT_LIKE_KEY_PREFIX + commentId;
        Boolean already = redisTemplate.opsForSet().isMember(key, userId);
        if (Boolean.TRUE.equals(already)) {
            log.info("【门户-活动留言】用户已点赞过，无需重复处理，留言ID={}，用户ID={}", commentId, userId);
            return;
        }
        redisTemplate.opsForSet().add(key, userId);
        activityCommentMapper.increaseLikeCount(commentId, 1, LocalDateTime.now());
        clearActivityCommentCache(comment.getActivityId());
        if (!Objects.equals(comment.getUserId(), userId)) {
            portalNotificationService.createNotification(
                    comment.getUserId(),
                    PortalNotificationTypeEnum.LIKE.getCode(),
                    "留言获得点赞",
                    "你的活动留言获得点赞",
                    "ACTIVITY_COMMENT",
                    commentId
            );
        }
    }

    @Override
    public void deleteOwnComment(Long commentId, Long userId) {
        log.info("【门户-活动留言】删除留言，留言ID={}，用户ID={}", commentId, userId);
        ActivityComment comment = activityCommentMapper.selectById(commentId);
        if (comment == null || comment.getIsDeleted() != null && comment.getIsDeleted() == 1) {
            throw new BusinessException("留言不存在或已删除");
        }
        if (!Objects.equals(comment.getUserId(), userId)) {
            throw new BusinessException("只能删除自己的留言");
        }
        int rows = activityCommentMapper.markDeleted(commentId, LocalDateTime.now());
        if (rows == 0) {
            throw new BusinessException("删除留言失败，请稍后重试");
        }
        clearActivityCommentCache(comment.getActivityId());
    }

    /**
     * 为留言补充用户昵称和头像信息
     */
    private void enrichUserInfo(List<ActivityCommentVO> list) {
        if (list.isEmpty()) {
            return;
        }
        Set<Long> userIds = list.stream()
                .flatMap(vo -> {
                    List<Long> ids = new ArrayList<>();
                    if (vo.getUserId() != null) {
                        ids.add(vo.getUserId());
                    }
                    if (vo.getMentionUserId() != null) {
                        ids.add(vo.getMentionUserId());
                    }
                    return ids.stream();
                })
                .collect(Collectors.toSet());
        if (userIds.isEmpty()) {
            return;
        }
        List<User> users = userMapper.findByIds(userIds.stream().collect(Collectors.toList()));
        Map<Long, User> userMap = users == null ? Collections.emptyMap()
                : users.stream().collect(Collectors.toMap(User::getId, u -> u, (a, b) -> a));
        for (ActivityCommentVO vo : list) {
            User user = userMap.get(vo.getUserId());
            if (user != null) {
                vo.setUserNickname(user.getNickname());
                vo.setUserAvatar(user.getAvatar());
            } else {
                vo.setUserNickname("用户已注销");
                vo.setUserAvatar(null);
            }
            if (vo.getMentionUserId() != null) {
                User mentionUser = userMap.get(vo.getMentionUserId());
                if (mentionUser != null) {
                    vo.setMentionUserNickname(mentionUser.getNickname());
                    vo.setMentionUserAvatar(mentionUser.getAvatar());
                } else {
                    vo.setMentionUserNickname("用户已注销");
                }
            }
        }
    }

    /**
     * 将父子留言展开为一个扁平集合，便于统一补充用户信息
     */
    private List<ActivityCommentVO> flattenWithChildren(List<ActivityCommentVO> parents) {
        List<ActivityCommentVO> all = new ArrayList<>();
        for (ActivityCommentVO parent : parents) {
            all.add(parent);
            if (parent.getChildren() != null && !parent.getChildren().isEmpty()) {
                all.addAll(parent.getChildren());
            }
        }
        return all;
    }

    private void notifyUsers(ActivityComment parent, ActivityComment comment, Long userId) {
        if (parent != null && !Objects.equals(parent.getUserId(), userId)) {
            portalNotificationService.createNotification(
                    parent.getUserId(),
                    PortalNotificationTypeEnum.REPLY.getCode(),
                    "收到留言回复",
                    "有人回复了你的活动留言",
                    "ACTIVITY_COMMENT",
                    parent.getId()
            );
        }
        Long mentionUserId = comment.getMentionUserId();
        if (mentionUserId != null && !Objects.equals(mentionUserId, userId)
                && (parent == null || !Objects.equals(mentionUserId, parent.getUserId()))) {
            portalNotificationService.createNotification(
                    mentionUserId,
                    PortalNotificationTypeEnum.REPLY.getCode(),
                    "有人提到了你",
                    "你在活动留言中被@提及",
                    "ACTIVITY_COMMENT",
                    comment.getId()
            );
        }
    }

    /**
     * 生成留言分页缓存的唯一 key。
     */
    private String buildPageCacheKey(Long activityId, ActivityCommentPageQuery query) {
        return "activity:" + activityId + ":comment:page:" + query.getCurrent()
                + ":size:" + query.getSize()
                + ":parent:" + query.getParentId();
    }

    /**
     * 按活动 ID 失效所有留言分页缓存，避免脏读。
     */
    private void clearActivityCommentCache(Long activityId) {
        String pattern = ACTIVITY_COMMENT_CACHE_PREFIX + activityId + ":comment:page:*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("【门户-活动留言】已清理活动留言分页缓存，活动ID={}，key数量={}", activityId, keys.size());
        }
    }
}
