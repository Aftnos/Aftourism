package aftnos.aftourismserver.portal.service.impl;

import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.mapper.UserQualificationApplyMapper;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.auth.pojo.UserQualificationApply;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.dto.QualificationApplyRequest;
import aftnos.aftourismserver.portal.service.PortalQualificationService;
import aftnos.aftourismserver.portal.vo.QualificationStatusVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 门户资质申请服务实现。
 */
@Service
public class PortalQualificationServiceImpl implements PortalQualificationService {

    private final UserQualificationApplyMapper qualificationApplyMapper;
    private final UserMapper userMapper;

    public PortalQualificationServiceImpl(UserQualificationApplyMapper qualificationApplyMapper, UserMapper userMapper) {
        this.qualificationApplyMapper = qualificationApplyMapper;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submit(Long userId, QualificationApplyRequest request) {
        User user = userMapper.findById(userId);
        if (user == null || (user.getIsDeleted() != null && user.getIsDeleted() == 1)) {
            throw new BusinessException("用户不存在或已失效");
        }
        if (user.getIsAdvanced() != null && user.getIsAdvanced() == 1) {
            throw new BusinessException("当前账号已具备高级用户资质");
        }

        UserQualificationApply latest = qualificationApplyMapper.findLatestByUserId(userId);
        if (latest != null && latest.getApplyStatus() != null && latest.getApplyStatus() == 0) {
            throw new BusinessException("已有待审核的资质申请，请耐心等待审核结果");
        }

        LocalDateTime now = LocalDateTime.now();
        UserQualificationApply apply = new UserQualificationApply();
        apply.setUserId(userId);
        apply.setRealName(request.getRealName());
        apply.setOrganization(request.getOrganization());
        apply.setContactPhone(request.getContactPhone());
        apply.setApplyReason(request.getApplyReason());
        apply.setAttachmentUrl(request.getAttachmentUrl());
        apply.setApplyStatus(0);
        apply.setIsDeleted(0);
        apply.setCreateTime(now);
        apply.setUpdateTime(now);
        qualificationApplyMapper.insert(apply);
    }

    @Override
    public QualificationStatusVO latestStatus(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            return QualificationStatusVO.builder()
                    .status("NONE")
                    .build();
        }

        UserQualificationApply latest = qualificationApplyMapper.findLatestByUserId(userId);
        if (user.getIsAdvanced() != null && user.getIsAdvanced() == 1) {
            return QualificationStatusVO.builder()
                    .status("APPROVED")
                    .applyTime(latest != null ? latest.getCreateTime() : null)
                    .auditRemark(latest != null ? latest.getAuditRemark() : null)
                    .build();
        }

        if (latest == null || latest.getApplyStatus() == null) {
            return QualificationStatusVO.builder()
                    .status("NONE")
                    .build();
        }

        String status = switch (latest.getApplyStatus()) {
            case 0 -> "PENDING";
            case 1 -> "APPROVED";
            case 2 -> "REJECTED";
            default -> "NONE";
        };
        return QualificationStatusVO.builder()
                .status(status)
                .auditRemark(latest.getAuditRemark())
                .applyTime(latest.getCreateTime())
                .build();
    }
}
