package aftnos.aftourismserver.portal.service.impl;

import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.dto.ExchangeCommentCreateDTO;
import aftnos.aftourismserver.portal.dto.ExchangeCommentPageQuery;
import aftnos.aftourismserver.portal.enums.ExchangeArticleStatusEnum;
import aftnos.aftourismserver.portal.enums.PortalNotificationTypeEnum;
import aftnos.aftourismserver.portal.mapper.ExchangeArticleMapper;
import aftnos.aftourismserver.portal.mapper.ExchangeCommentMapper;
import aftnos.aftourismserver.portal.pojo.ExchangeArticle;
import aftnos.aftourismserver.portal.pojo.ExchangeComment;
import aftnos.aftourismserver.portal.service.ExchangeCommentService;
import aftnos.aftourismserver.portal.service.PortalNotificationService;
import aftnos.aftourismserver.portal.vo.ExchangeCommentVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * 交流评论业务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeCommentServiceImpl implements ExchangeCommentService {

    private static final String COMMENT_LIKE_KEY_PREFIX = "exchange:comment:like:";

    private final ExchangeArticleMapper exchangeArticleMapper;
    private final ExchangeCommentMapper exchangeCommentMapper;
    private final UserMapper userMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PortalNotificationService portalNotificationService;

    @Override
    public Long addComment(Long articleId, ExchangeCommentCreateDTO dto, Long userId) {
        log.info("【门户-交流评论】新增评论，文章ID={}，用户ID={}", articleId, userId);
        ExchangeArticle article = exchangeArticleMapper.selectById(articleId);
        if (article == null || (article.getIsDeleted() != null && article.getIsDeleted() == 1)) {
            throw new BusinessException("文章不存在或已删除");
        }
        if (!Objects.equals(article.getStatus(), ExchangeArticleStatusEnum.APPROVED.getCode())
                && !Objects.equals(article.getUserId(), userId)) {
            throw new BusinessException("文章尚未审核通过，暂不可评论");
        }
        Long parentId = dto.getParentId();
        ExchangeComment parent = null;
        if (parentId != null) {
            parent = exchangeCommentMapper.selectById(parentId);
            if (parent == null || parent.getIsDeleted() != null && parent.getIsDeleted() == 1) {
                throw new BusinessException("父级评论不存在或已删除");
            }
            if (!Objects.equals(parent.getArticleId(), articleId)) {
                throw new BusinessException("父级评论不属于当前文章");
            }
        }
        Long mentionUserId = dto.getMentionUserId();
        if (mentionUserId != null) {
            User mentionUser = userMapper.findById(mentionUserId);
            if (mentionUser == null || (mentionUser.getIsDeleted() != null && mentionUser.getIsDeleted() == 1)) {
                throw new BusinessException("被@用户不存在或已失效");
            }
        }

        ExchangeComment comment = new ExchangeComment();
        comment.setArticleId(articleId);
        comment.setUserId(userId);
        comment.setContent(dto.getContent());
        comment.setParentId(parentId);
        comment.setMentionUserId(mentionUserId);
        comment.setLikeCount(0);
        comment.setIsDeleted(0);
        LocalDateTime now = LocalDateTime.now();
        comment.setCreateTime(now);
        comment.setUpdateTime(now);
        exchangeCommentMapper.insert(comment);
        exchangeArticleMapper.increaseCommentCount(articleId, 1, now);

        notifyUsers(article, parent, comment, userId);
        return comment.getId();
    }

    @Override
    public PageInfo<ExchangeCommentVO> pageComments(Long articleId, ExchangeCommentPageQuery query) {
        PageHelper.startPage(query.getCurrent(), query.getSize());
        List<ExchangeCommentVO> list = exchangeCommentMapper.pageList(articleId, query.getParentId());
        PageInfo<ExchangeCommentVO> pageInfo = new PageInfo<>(list);
        if (query.getParentId() == null && !list.isEmpty()) {
            List<Long> parentIds = list.stream().map(ExchangeCommentVO::getId).toList();
            List<ExchangeCommentVO> children = exchangeCommentMapper.listByParentIds(parentIds);
            Map<Long, List<ExchangeCommentVO>> childMap = children.stream()
                    .collect(Collectors.groupingBy(ExchangeCommentVO::getParentId, LinkedHashMap::new, Collectors.toList()));
            for (ExchangeCommentVO vo : list) {
                vo.setChildren(childMap.getOrDefault(vo.getId(), Collections.emptyList()));
            }
            enrichUserInfo(flattenWithChildren(list));
        } else {
            enrichUserInfo(list);
        }
        return pageInfo;
    }

    @Override
    public void likeComment(Long commentId, Long userId) {
        ExchangeComment comment = exchangeCommentMapper.selectById(commentId);
        if (comment == null || comment.getIsDeleted() != null && comment.getIsDeleted() == 1) {
            throw new BusinessException("评论不存在或已删除");
        }
        String key = COMMENT_LIKE_KEY_PREFIX + commentId;
        Boolean already = redisTemplate.opsForSet().isMember(key, userId);
        if (Boolean.TRUE.equals(already)) {
            return;
        }
        redisTemplate.opsForSet().add(key, userId);
        exchangeCommentMapper.increaseLikeCount(commentId, 1, LocalDateTime.now());
        if (!Objects.equals(comment.getUserId(), userId)) {
            portalNotificationService.createNotification(
                    comment.getUserId(),
                    PortalNotificationTypeEnum.LIKE.getCode(),
                    "评论获得点赞",
                    "你的交流评论获得点赞",
                    "COMMENT",
                    commentId
            );
        }
    }

    @Override
    public void deleteOwnComment(Long commentId, Long userId) {
        ExchangeComment comment = exchangeCommentMapper.selectById(commentId);
        if (comment == null || comment.getIsDeleted() != null && comment.getIsDeleted() == 1) {
            throw new BusinessException("评论不存在或已删除");
        }
        if (!Objects.equals(comment.getUserId(), userId)) {
            throw new BusinessException("只能删除自己的评论");
        }
        exchangeCommentMapper.markDeleted(commentId, LocalDateTime.now());
    }

    private void notifyUsers(ExchangeArticle article, ExchangeComment parent, ExchangeComment comment, Long userId) {
        if (parent != null && !Objects.equals(parent.getUserId(), userId)) {
            portalNotificationService.createNotification(
                    parent.getUserId(),
                    PortalNotificationTypeEnum.REPLY.getCode(),
                    "收到评论回复",
                    "有人回复了你的交流评论",
                    "COMMENT",
                    parent.getId()
            );
        } else if (parent == null && !Objects.equals(article.getUserId(), userId)) {
            portalNotificationService.createNotification(
                    article.getUserId(),
                    PortalNotificationTypeEnum.REPLY.getCode(),
                    "收到文章评论",
                    "你的交流文章收到了新的评论",
                    "ARTICLE",
                    article.getId()
            );
        }
        Long mentionUserId = comment.getMentionUserId();
        if (mentionUserId != null && !Objects.equals(mentionUserId, userId)
                && (parent == null || !Objects.equals(mentionUserId, parent.getUserId()))) {
            portalNotificationService.createNotification(
                    mentionUserId,
                    PortalNotificationTypeEnum.REPLY.getCode(),
                    "有人提到了你",
                    "你在交流评论中被@提及",
                    "COMMENT",
                    comment.getId()
            );
        }
    }

    private void enrichUserInfo(List<ExchangeCommentVO> list) {
        if (list == null || list.isEmpty()) {
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
        List<User> users = userMapper.findByIds(userIds.stream().toList());
        Map<Long, User> userMap = users == null ? Collections.emptyMap()
                : users.stream().collect(Collectors.toMap(User::getId, u -> u, (a, b) -> a));
        for (ExchangeCommentVO vo : list) {
            User user = userMap.get(vo.getUserId());
            if (user != null) {
                vo.setUserNickname(user.getNickname());
                vo.setUserAvatar(user.getAvatar());
            } else {
                vo.setUserNickname("用户已注销");
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

    private List<ExchangeCommentVO> flattenWithChildren(List<ExchangeCommentVO> parents) {
        List<ExchangeCommentVO> all = new ArrayList<>();
        for (ExchangeCommentVO parent : parents) {
            all.add(parent);
            if (parent.getChildren() != null && !parent.getChildren().isEmpty()) {
                all.addAll(parent.getChildren());
            }
        }
        return all;
    }
}
