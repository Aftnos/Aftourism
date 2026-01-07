package aftnos.aftourismserver.portal.service.impl;

import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.dto.ContentReportCreateDTO;
import aftnos.aftourismserver.portal.enums.ContentReportReasonEnum;
import aftnos.aftourismserver.portal.enums.ContentReportStatusEnum;
import aftnos.aftourismserver.portal.enums.ContentReportTargetEnum;
import aftnos.aftourismserver.portal.mapper.ContentReportMapper;
import aftnos.aftourismserver.portal.mapper.ActivityCommentMapper;
import aftnos.aftourismserver.portal.mapper.ExchangeArticleMapper;
import aftnos.aftourismserver.portal.mapper.ExchangeCommentMapper;
import aftnos.aftourismserver.portal.pojo.ContentReport;
import aftnos.aftourismserver.portal.pojo.ActivityComment;
import aftnos.aftourismserver.portal.pojo.ExchangeArticle;
import aftnos.aftourismserver.portal.pojo.ExchangeComment;
import aftnos.aftourismserver.portal.service.ContentReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 举报业务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContentReportServiceImpl implements ContentReportService {

    private final ContentReportMapper contentReportMapper;
    private final ActivityCommentMapper activityCommentMapper;
    private final ExchangeArticleMapper exchangeArticleMapper;
    private final ExchangeCommentMapper exchangeCommentMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Long createReport(Long userId, ContentReportCreateDTO dto) {
        ContentReportTargetEnum targetEnum = ContentReportTargetEnum.fromCode(dto.getTargetType());
        ContentReportReasonEnum reasonEnum = ContentReportReasonEnum.fromCode(dto.getReasonType());
        Long targetUserId = resolveTargetUserId(targetEnum, dto.getTargetId());
        if (Objects.equals(userId, targetUserId)) {
            throw new BusinessException("不能举报自己的内容");
        }
        ContentReport report = new ContentReport();
        report.setReporterId(userId);
        report.setTargetUserId(targetUserId);
        report.setTargetType(targetEnum.getCode());
        report.setTargetId(dto.getTargetId());
        report.setReasonType(reasonEnum.getCode());
        report.setReason(dto.getReason());
        report.setScreenshotUrls(toJson(dto.getScreenshotUrls()));
        report.setStatus(ContentReportStatusEnum.PENDING.getCode());
        report.setResultRemark(null);
        report.setViolationFlag(0);
        report.setIsDeleted(0);
        LocalDateTime now = LocalDateTime.now();
        report.setCreateTime(now);
        report.setUpdateTime(now);
        contentReportMapper.insert(report);
        return report.getId();
    }

    private Long resolveTargetUserId(ContentReportTargetEnum targetEnum, Long targetId) {
        if (targetEnum == ContentReportTargetEnum.ARTICLE) {
            ExchangeArticle article = exchangeArticleMapper.selectById(targetId);
            if (article == null || (article.getIsDeleted() != null && article.getIsDeleted() == 1)) {
                throw new BusinessException("举报的文章不存在或已删除");
            }
            return article.getUserId();
        }
        if (targetEnum == ContentReportTargetEnum.ACTIVITY_COMMENT) {
            ActivityComment comment = activityCommentMapper.selectById(targetId);
            if (comment == null || (comment.getIsDeleted() != null && comment.getIsDeleted() == 1)) {
                throw new BusinessException("举报的活动评论不存在或已删除");
            }
            return comment.getUserId();
        }
        ExchangeComment comment = exchangeCommentMapper.selectById(targetId);
        if (comment == null || (comment.getIsDeleted() != null && comment.getIsDeleted() == 1)) {
            throw new BusinessException("举报的评论不存在或已删除");
        }
        return comment.getUserId();
    }

    private String toJson(List<String> urls) {
        if (urls == null || urls.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(urls);
        } catch (JsonProcessingException e) {
            log.warn("【门户-举报】截图序列化失败，原因={}", e.getMessage());
            return null;
        }
    }
}
