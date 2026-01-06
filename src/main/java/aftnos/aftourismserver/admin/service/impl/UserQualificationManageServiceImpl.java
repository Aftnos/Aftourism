package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.QualificationPageQuery;
import aftnos.aftourismserver.admin.service.UserQualificationManageService;
import aftnos.aftourismserver.admin.vo.QualificationApplyManageVO;
import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.mapper.UserQualificationApplyMapper;
import aftnos.aftourismserver.auth.pojo.UserQualificationApply;
import aftnos.aftourismserver.common.exception.BusinessException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

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
    public PageInfo<QualificationApplyManageVO> page(QualificationPageQuery query) {
        int pageNum = query.getCurrent() != null ? query.getCurrent() : 1;
        int pageSize = query.getSize() != null ? query.getSize() : 10;
        PageHelper.startPage(pageNum, pageSize);
        List<QualificationApplyManageVO> list = qualificationApplyMapper.pageManage(query.getUserName(), query.getApplyStatus());
        return new PageInfo<>(list);
    }

    @Override
    public QualificationApplyManageVO detail(Long applyId) {
        QualificationApplyManageVO detail = qualificationApplyMapper.findManageDetail(applyId);
        if (detail == null) {
            throw new BusinessException("资质申请不存在");
        }
        return detail;
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
