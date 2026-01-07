package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.ContentReportManageDTO;
import aftnos.aftourismserver.admin.dto.ContentReportManagePageQuery;
import aftnos.aftourismserver.admin.service.ContentReportManageService;
import aftnos.aftourismserver.admin.vo.ContentReportManageVO;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.enums.ContentReportReasonEnum;
import aftnos.aftourismserver.portal.enums.ContentReportStatusEnum;
import aftnos.aftourismserver.portal.enums.ContentReportTargetEnum;
import aftnos.aftourismserver.portal.enums.ExchangeArticleStatusEnum;
import aftnos.aftourismserver.portal.enums.PortalNotificationTypeEnum;
import aftnos.aftourismserver.portal.mapper.ContentReportMapper;
import aftnos.aftourismserver.portal.mapper.ExchangeArticleMapper;
import aftnos.aftourismserver.portal.mapper.ExchangeCommentMapper;
import aftnos.aftourismserver.portal.pojo.ContentReport;
import aftnos.aftourismserver.portal.pojo.ExchangeArticle;
import aftnos.aftourismserver.portal.service.PortalNotificationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 举报后台管理实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ContentReportManageServiceImpl implements ContentReportManageService {

    private final ContentReportMapper contentReportMapper;
    private final ExchangeArticleMapper exchangeArticleMapper;
    private final ExchangeCommentMapper exchangeCommentMapper;
    private final PortalNotificationService portalNotificationService;

    @Override
    public PageInfo<ContentReportManageVO> page(ContentReportManagePageQuery query) {
        PageHelper.startPage(query.getCurrent(), query.getSize());
        List<ContentReportManageVO> list = contentReportMapper.pageListForManage(
                query.getStatus(),
                query.getTargetType(),
                query.getKeyword()
        );
        fillTextInfo(list);
        return new PageInfo<>(list);
    }

    @Override
    public void update(Long id, ContentReportManageDTO dto) {
        ContentReport report = contentReportMapper.selectById(id);
        if (report == null || (report.getIsDeleted() != null && report.getIsDeleted() == 1)) {
            throw new BusinessException("举报记录不存在或已删除");
        }
        ContentReportStatusEnum statusEnum = ContentReportStatusEnum.fromCode(dto.getStatus());
        LocalDateTime now = LocalDateTime.now();
        contentReportMapper.updateStatus(id, statusEnum.getCode(), dto.getViolationFlag(), dto.getResultRemark(), now);
        boolean hasViolation = dto.getViolationFlag() != null && dto.getViolationFlag() == 1;
        if (hasViolation) {
            handleViolationContent(report, dto.getResultRemark(), now);
            String remark = dto.getResultRemark() == null ? "你的内容被判定为违规，请注意社区规范。" : dto.getResultRemark();
            portalNotificationService.createNotification(
                    report.getTargetUserId(),
                    PortalNotificationTypeEnum.VIOLATION.getCode(),
                    "内容违规提醒",
                    remark,
                    report.getTargetType(),
                    report.getTargetId()
            );
        }
        if (statusEnum != ContentReportStatusEnum.PENDING) {
            notifyReporter(report, dto.getResultRemark(), hasViolation);
        }
    }

    private void fillTextInfo(List<ContentReportManageVO> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (ContentReportManageVO vo : list) {
            ContentReportTargetEnum targetEnum = ContentReportTargetEnum.fromCode(vo.getTargetType());
            vo.setTargetTypeText(targetEnum.getText());
            ContentReportReasonEnum reasonEnum = ContentReportReasonEnum.fromCode(vo.getReasonType());
            vo.setReasonTypeText(reasonEnum.getText());
            ContentReportStatusEnum statusEnum = ContentReportStatusEnum.fromCode(vo.getStatus());
            vo.setStatusText(statusEnum.getText());
        }
    }

    private void handleViolationContent(ContentReport report, String resultRemark, LocalDateTime now) {
        ContentReportTargetEnum targetEnum = ContentReportTargetEnum.fromCode(report.getTargetType());
        if (targetEnum == ContentReportTargetEnum.ARTICLE) {
            ExchangeArticle article = exchangeArticleMapper.selectById(report.getTargetId());
            if (article == null || (article.getIsDeleted() != null && article.getIsDeleted() == 1)) {
                log.warn("【举报处理】目标文章不存在或已删除，reportId={}", report.getId());
                return;
            }
            String remarkText = resultRemark == null || resultRemark.isBlank() ? "存在违规内容：已核实" : "存在违规内容：" + resultRemark;
            exchangeArticleMapper.updateStatus(article.getId(), ExchangeArticleStatusEnum.REJECTED.getCode(), remarkText, now);
            return;
        }
        exchangeCommentMapper.markDeleted(report.getTargetId(), now);
    }

    private void notifyReporter(ContentReport report, String resultRemark, boolean hasViolation) {
        String baseContent = hasViolation ? "你的举报已通过，感谢反馈。" : "你的举报未通过，未发现违规内容。";
        if (resultRemark != null && !resultRemark.isBlank()) {
            baseContent = baseContent + " 处理说明：" + resultRemark;
        }
        portalNotificationService.createNotification(
                report.getReporterId(),
                PortalNotificationTypeEnum.REPORT.getCode(),
                "举报处理结果",
                baseContent,
                report.getTargetType(),
                report.getTargetId()
        );
    }
}
