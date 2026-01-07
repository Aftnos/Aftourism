package aftnos.aftourismserver.portal.service.impl;

import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.dto.ExchangeArticleCreateDTO;
import aftnos.aftourismserver.portal.dto.ExchangeArticlePageQuery;
import aftnos.aftourismserver.portal.enums.ExchangeArticleStatusEnum;
import aftnos.aftourismserver.portal.enums.PortalNotificationTypeEnum;
import aftnos.aftourismserver.portal.mapper.ExchangeArticleMapper;
import aftnos.aftourismserver.portal.pojo.ExchangeArticle;
import aftnos.aftourismserver.portal.service.ExchangeArticleService;
import aftnos.aftourismserver.portal.service.PortalNotificationService;
import aftnos.aftourismserver.portal.vo.ExchangeArticleVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 交流文章业务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeArticleServiceImpl implements ExchangeArticleService {

    private static final String ARTICLE_LIKE_KEY_PREFIX = "exchange:article:like:";

    private final ExchangeArticleMapper exchangeArticleMapper;
    private final UserMapper userMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PortalNotificationService portalNotificationService;

    @Override
    public Long createArticle(Long userId, ExchangeArticleCreateDTO dto) {
        log.info("【门户-交流文章】发布文章，用户ID={}", userId);
        User user = userMapper.findById(userId);
        if (user == null || (user.getIsDeleted() != null && user.getIsDeleted() == 1)) {
            throw new BusinessException("用户不存在或已失效");
        }
        ExchangeArticle article = new ExchangeArticle();
        article.setUserId(userId);
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setCoverUrl(dto.getCoverUrl());
        article.setStatus(ExchangeArticleStatusEnum.PENDING.getCode());
        article.setLikeCount(0);
        article.setCommentCount(0);
        article.setAuditRemark(null);
        article.setIsDeleted(0);
        LocalDateTime now = LocalDateTime.now();
        article.setCreateTime(now);
        article.setUpdateTime(now);
        exchangeArticleMapper.insert(article);
        return article.getId();
    }

    @Override
    public PageInfo<ExchangeArticleVO> pageArticles(ExchangeArticlePageQuery query, Long userId, boolean includeAll) {
        Integer status = includeAll ? null : ExchangeArticleStatusEnum.APPROVED.getCode();
        PageHelper.startPage(query.getCurrent(), query.getSize());
        List<ExchangeArticleVO> list = exchangeArticleMapper.pageList(status, query.getKeyword(), userId);
        fillStatusText(list);
        return new PageInfo<>(list);
    }

    @Override
    public ExchangeArticleVO getDetail(Long id, Long viewerId) {
        ExchangeArticleVO vo = exchangeArticleMapper.selectVOById(id);
        if (vo == null) {
            throw new BusinessException("交流文章不存在或已删除");
        }
        boolean isOwner = viewerId != null && Objects.equals(vo.getUserId(), viewerId);
        if (!isOwner && !Objects.equals(vo.getStatus(), ExchangeArticleStatusEnum.APPROVED.getCode())) {
            throw new BusinessException("交流文章尚未通过审核");
        }
        fillStatusText(List.of(vo));
        return vo;
    }

    @Override
    public void likeArticle(Long articleId, Long userId) {
        ExchangeArticle article = exchangeArticleMapper.selectById(articleId);
        if (article == null || article.getIsDeleted() != null && article.getIsDeleted() == 1) {
            throw new BusinessException("文章不存在或已删除");
        }
        String key = ARTICLE_LIKE_KEY_PREFIX + articleId;
        Boolean already = redisTemplate.opsForSet().isMember(key, userId);
        if (Boolean.TRUE.equals(already)) {
            return;
        }
        redisTemplate.opsForSet().add(key, userId);
        exchangeArticleMapper.increaseLikeCount(articleId, 1, LocalDateTime.now());
        if (!Objects.equals(article.getUserId(), userId)) {
            portalNotificationService.createNotification(
                    article.getUserId(),
                    PortalNotificationTypeEnum.LIKE.getCode(),
                    "文章获得点赞",
                    "你的交流文章收到了新的点赞",
                    "ARTICLE",
                    articleId
            );
        }
    }

    private void fillStatusText(List<ExchangeArticleVO> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (ExchangeArticleVO vo : list) {
            ExchangeArticleStatusEnum statusEnum = ExchangeArticleStatusEnum.fromCode(vo.getStatus());
            vo.setStatusText(statusEnum.getText());
        }
    }
}
