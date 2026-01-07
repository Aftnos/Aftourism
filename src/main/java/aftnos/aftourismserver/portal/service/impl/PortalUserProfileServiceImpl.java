package aftnos.aftourismserver.portal.service.impl;

import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.mapper.UserQualificationApplyMapper;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.auth.pojo.UserQualificationApply;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.portal.service.PortalUserProfileService;
import aftnos.aftourismserver.portal.vo.PortalUserProfileVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 门户用户主页服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PortalUserProfileServiceImpl implements PortalUserProfileService {

    private final UserMapper userMapper;
    private final UserQualificationApplyMapper qualificationApplyMapper;

    @Override
    public PortalUserProfileVO getProfile(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null || (user.getIsDeleted() != null && user.getIsDeleted() == 1)) {
            throw new BusinessException("用户不存在或已注销");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException("用户已被禁用");
        }
        UserQualificationApply latestApply = qualificationApplyMapper.findLatestByUserId(userId);
        boolean isAdvanced = user.getIsAdvanced() != null && user.getIsAdvanced() == 1;
        PortalUserProfileVO vo = new PortalUserProfileVO();
        vo.setUserId(user.getId());
        vo.setUserName(user.getUsername());
        vo.setNickName(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setGender(user.getGender());
        vo.setRemark(user.getRemark());
        vo.setAdvancedUser(isAdvanced);
        vo.setQualificationStatus(resolveQualificationStatus(isAdvanced, latestApply));
        vo.setCreateTime(user.getCreateTime());
        return vo;
    }

    private String resolveQualificationStatus(boolean isAdvanced, UserQualificationApply latestApply) {
        if (isAdvanced) {
            return "APPROVED";
        }
        if (latestApply == null) {
            return "NONE";
        }
        Integer status = latestApply.getApplyStatus();
        if (status == null) {
            return "NONE";
        }
        return switch (status) {
            case 0 -> "PENDING";
            case 1 -> "APPROVED";
            case 2 -> "REJECTED";
            default -> "NONE";
        };
    }
}
