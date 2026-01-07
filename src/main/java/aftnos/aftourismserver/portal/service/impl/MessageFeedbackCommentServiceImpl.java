package aftnos.aftourismserver.portal.service.impl;

import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.dto.MessageFeedbackCommentCreateDTO;
import aftnos.aftourismserver.portal.dto.MessageFeedbackCommentPageQuery;
import aftnos.aftourismserver.portal.mapper.MessageFeedbackCommentMapper;
import aftnos.aftourismserver.portal.mapper.MessageFeedbackMapper;
import aftnos.aftourismserver.portal.pojo.MessageFeedback;
import aftnos.aftourismserver.portal.pojo.MessageFeedbackComment;
import aftnos.aftourismserver.portal.service.MessageFeedbackCommentService;
import aftnos.aftourismserver.portal.vo.MessageFeedbackCommentVO;
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
 * 门户留言反馈评论服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageFeedbackCommentServiceImpl implements MessageFeedbackCommentService {

    private final MessageFeedbackMapper messageFeedbackMapper;
    private final MessageFeedbackCommentMapper messageFeedbackCommentMapper;
    private final UserMapper userMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CacheManager cacheManager;

    /** 点赞缓存前缀 */
    private static final String COMMENT_LIKE_KEY_PREFIX = "feedback:comment:like:";

    /** 留言分页缓存名称 */
    private static final String FEEDBACK_COMMENT_PAGE_CACHE = "feedbackCommentPages";

    /** 留言分页缓存前缀 */
    private static final String FEEDBACK_COMMENT_CACHE_PREFIX = "cache:" + FEEDBACK_COMMENT_PAGE_CACHE + ":feedback:";

    @Override
    public Long addComment(Long feedbackId, MessageFeedbackCommentCreateDTO dto, Long userId) {
        log.info("【门户-留言反馈】新增评论，反馈ID={}，用户ID={}", feedbackId, userId);
        MessageFeedback feedback = messageFeedbackMapper.selectById(feedbackId);
        if (feedback == null || feedback.getIsDeleted() != null && feedback.getIsDeleted() == 1) {
            throw new BusinessException("留言反馈不存在或已删除");
        }
        Long parentId = dto.getParentId();
        if (parentId != null) {
            MessageFeedbackComment parent = messageFeedbackCommentMapper.selectById(parentId);
            if (parent == null || parent.getIsDeleted() != null && parent.getIsDeleted() == 1) {
                throw new BusinessException("父级留言不存在或已被删除");
            }
            if (!Objects.equals(parent.getFeedbackId(), feedbackId)) {
                throw new BusinessException("父级留言不属于当前反馈");
            }
        }
        MessageFeedbackComment comment = new MessageFeedbackComment();
        comment.setFeedbackId(feedbackId);
        comment.setUserId(userId);
        comment.setContent(dto.getContent());
        comment.setParentId(parentId);
        comment.setLikeCount(0);
        comment.setIsDeleted(0);
        LocalDateTime now = LocalDateTime.now();
        comment.setCreateTime(now);
        comment.setUpdateTime(now);
        messageFeedbackCommentMapper.insert(comment);
        clearFeedbackCommentCache(feedbackId);
        return comment.getId();
    }

    @Override
    public PageInfo<MessageFeedbackCommentVO> pageComments(Long feedbackId, MessageFeedbackCommentPageQuery query) {
        String cacheKey = buildPageCacheKey(feedbackId, query);
        Cache cache = cacheManager.getCache(FEEDBACK_COMMENT_PAGE_CACHE);
        if (cache != null) {
            PageInfo<MessageFeedbackCommentVO> cached = cache.get(cacheKey, PageInfo.class);
            if (cached != null) {
                return cached;
            }
        }
        PageHelper.startPage(query.getCurrent(), query.getSize());
        List<MessageFeedbackCommentVO> list = messageFeedbackCommentMapper.pageList(feedbackId, query.getParentId());
        PageInfo<MessageFeedbackCommentVO> pageInfo = new PageInfo<>(list);
        if (query.getParentId() == null && !list.isEmpty()) {
            List<Long> parentIds = list.stream().map(MessageFeedbackCommentVO::getId).toList();
            List<MessageFeedbackCommentVO> children = messageFeedbackCommentMapper.listByParentIds(parentIds);
            Map<Long, List<MessageFeedbackCommentVO>> childMap = children.stream()
                    .collect(Collectors.groupingBy(MessageFeedbackCommentVO::getParentId, LinkedHashMap::new, Collectors.toList()));
            for (MessageFeedbackCommentVO vo : list) {
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
        MessageFeedbackComment comment = messageFeedbackCommentMapper.selectById(commentId);
        if (comment == null || comment.getIsDeleted() != null && comment.getIsDeleted() == 1) {
            throw new BusinessException("留言不存在或已删除");
        }
        String key = COMMENT_LIKE_KEY_PREFIX + commentId;
        Boolean already = redisTemplate.opsForSet().isMember(key, userId);
        if (Boolean.TRUE.equals(already)) {
            return;
        }
        redisTemplate.opsForSet().add(key, userId);
        messageFeedbackCommentMapper.increaseLikeCount(commentId, 1, LocalDateTime.now());
        clearFeedbackCommentCache(comment.getFeedbackId());
    }

    @Override
    public void deleteOwnComment(Long commentId, Long userId) {
        MessageFeedbackComment comment = messageFeedbackCommentMapper.selectById(commentId);
        if (comment == null || comment.getIsDeleted() != null && comment.getIsDeleted() == 1) {
            throw new BusinessException("留言不存在或已删除");
        }
        if (!Objects.equals(comment.getUserId(), userId)) {
            throw new BusinessException("只能删除自己的留言");
        }
        int rows = messageFeedbackCommentMapper.markDeleted(commentId, LocalDateTime.now());
        if (rows == 0) {
            throw new BusinessException("删除留言失败，请稍后重试");
        }
        clearFeedbackCommentCache(comment.getFeedbackId());
    }

    private void enrichUserInfo(List<MessageFeedbackCommentVO> list) {
        if (list.isEmpty()) {
            return;
        }
        Set<Long> userIds = list.stream()
                .map(MessageFeedbackCommentVO::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (userIds.isEmpty()) {
            return;
        }
        List<User> users = userMapper.findByIds(userIds.stream().toList());
        Map<Long, User> userMap = users == null ? Collections.emptyMap()
                : users.stream().collect(Collectors.toMap(User::getId, u -> u, (a, b) -> a));
        for (MessageFeedbackCommentVO vo : list) {
            User user = userMap.get(vo.getUserId());
            if (user != null) {
                vo.setUserNickname(user.getNickname());
                vo.setUserAvatar(user.getAvatar());
            } else {
                vo.setUserNickname("用户已注销");
            }
        }
    }

    private List<MessageFeedbackCommentVO> flattenWithChildren(List<MessageFeedbackCommentVO> parents) {
        List<MessageFeedbackCommentVO> all = new ArrayList<>();
        for (MessageFeedbackCommentVO parent : parents) {
            all.add(parent);
            if (parent.getChildren() != null && !parent.getChildren().isEmpty()) {
                all.addAll(parent.getChildren());
            }
        }
        return all;
    }

    private String buildPageCacheKey(Long feedbackId, MessageFeedbackCommentPageQuery query) {
        return "feedback:" + feedbackId + ":comment:page:" + query.getCurrent()
                + ":size:" + query.getSize()
                + ":parent:" + query.getParentId();
    }

    private void clearFeedbackCommentCache(Long feedbackId) {
        String pattern = FEEDBACK_COMMENT_CACHE_PREFIX + feedbackId + ":comment:page:*";
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
