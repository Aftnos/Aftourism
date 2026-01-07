package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.ContentReportManageDTO;
import aftnos.aftourismserver.admin.dto.ContentReportManagePageQuery;
import aftnos.aftourismserver.admin.service.ContentReportManageService;
import aftnos.aftourismserver.admin.vo.ContentReportManageVO;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.enums.ContentReportReasonEnum;
import aftnos.aftourismserver.portal.enums.ContentReportStatusEnum;
import aftnos.aftourismserver.portal.enums.ContentReportTargetEnum;
import aftnos.aftourismserver.portal.enums.PortalNotificationTypeEnum;
import aftnos.aftourismserver.portal.mapper.ContentReportMapper;
import aftnos.aftourismserver.portal.pojo.ContentReport;
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
        contentReportMapper.updateStatus(id, statusEnum.getCode(), dto.getViolationFlag(), dto.getResultRemark(), LocalDateTime.now());
        if (dto.getViolationFlag() != null && dto.getViolationFlag() == 1) {
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
}
