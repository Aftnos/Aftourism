package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.service.UserQualificationManageService;
import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.mapper.UserQualificationApplyMapper;
import aftnos.aftourismserver.auth.pojo.UserQualificationApply;
import aftnos.aftourismserver.common.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 管理端资质审核服务实现。
 */
@Service
public class UserQualificationManageServiceImpl implements UserQualificationManageService {

    private final UserQualificationApplyMapper qualificationApplyMapper;
    private final UserMapper userMapper;

    public UserQualificationManageServiceImpl(UserQualificationApplyMapper qualificationApplyMapper, UserMapper userMapper) {
        this.qualificationApplyMapper = qualificationApplyMapper;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void approve(Long applyId, String auditRemark) {
        UserQualificationApply apply = qualificationApplyMapper.findById(applyId);
        if (apply == null) {
            throw new BusinessException("资质申请不存在");
        }
        qualificationApplyMapper.updateStatus(applyId, 1, trimToNull(auditRemark));
        userMapper.updateAdvanced(apply.getUserId(), 1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(Long applyId, String auditRemark) {
        UserQualificationApply apply = qualificationApplyMapper.findById(applyId);
        if (apply == null) {
            throw new BusinessException("资质申请不存在");
        }
        qualificationApplyMapper.updateStatus(applyId, 2, trimToNull(auditRemark));
    }

    private String trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
