package aftnos.aftourismserver.auth.service.impl;

import aftnos.aftourismserver.auth.dto.PortalUserInfoResponse;
import aftnos.aftourismserver.auth.dto.UserInfoUpdateRequest;
import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.mapper.UserQualificationApplyMapper;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.auth.pojo.UserQualificationApply;
import aftnos.aftourismserver.auth.service.PortalUserInfoService;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.common.exception.UnauthorizedException;
import aftnos.aftourismserver.common.security.PortalUserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 门户用户信息服务实现。
 */
@Service
public class PortalUserInfoServiceImpl implements PortalUserInfoService {

    private final UserMapper userMapper;
    private final UserQualificationApplyMapper qualificationApplyMapper;

    public PortalUserInfoServiceImpl(UserMapper userMapper, UserQualificationApplyMapper qualificationApplyMapper) {
        this.userMapper = userMapper;
        this.qualificationApplyMapper = qualificationApplyMapper;
    }

    @Override
    public PortalUserInfoResponse getCurrentPortalUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UnauthorizedException("未登录或令牌无效");
        }
        if (!(authentication.getPrincipal() instanceof PortalUserPrincipal portalUserPrincipal)) {
            throw new UnauthorizedException("当前登录主体不是门户用户");
        }

        User user = userMapper.findById(portalUserPrincipal.getId());
        if (user == null) {
            throw new UnauthorizedException("用户不存在或已失效");
        }

        UserQualificationApply latestApply = qualificationApplyMapper.findLatestByUserId(user.getId());
        boolean isAdvanced = user.getIsAdvanced() != null && user.getIsAdvanced() == 1;
        String status = resolveQualificationStatus(isAdvanced, latestApply);
        String remark = latestApply != null ? latestApply.getAuditRemark() : null;
        return PortalUserInfoResponse.builder()
                .userId(user.getId())
                .userName(user.getUsername())
                .nickName(user.getNickname())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .email(user.getEmail())
                .remark(user.getRemark())
                .advancedUser(isAdvanced)
                .qualificationStatus(status)
                .qualificationRemark(remark)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCurrentUserInfo(UserInfoUpdateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UnauthorizedException("未登录或令牌无效");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof PortalUserPrincipal portalUser)) {
            throw new BusinessException("仅门户用户可以修改个人资料");
        }

        String nickname = trimToNull(request.getNickName());
        String phone = trimToNull(request.getPhone());
        String email = trimToNull(request.getEmail());
        String avatar = trimToNull(request.getAvatar());
        String remark = trimToNull(request.getRemark());
        String gender = normalizeGender(request.getGender());

        userMapper.updateProfile(portalUser.getId(), nickname, gender, phone, email, avatar, remark);

        User latest = userMapper.findById(portalUser.getId());
        if (latest != null) {
            PortalUserPrincipal refreshedPrincipal = PortalUserPrincipal.fromUser(latest);
            UsernamePasswordAuthenticationToken refreshedAuthentication = new UsernamePasswordAuthenticationToken(
                    refreshedPrincipal,
                    authentication.getCredentials(),
                    refreshedPrincipal.getAuthorities()
            );
            refreshedAuthentication.setDetails(authentication.getDetails());
            SecurityContextHolder.getContext().setAuthentication(refreshedAuthentication);
        }
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

    private String normalizeGender(String rawGender) {
        if (!StringUtils.hasText(rawGender)) {
            return "未知";
        }
        String value = rawGender.trim();
        if ("1".equals(value) || "男".equalsIgnoreCase(value)) {
            return "男";
        }
        if ("2".equals(value) || "女".equalsIgnoreCase(value)) {
            return "女";
        }
        return "未知";
    }

    private String trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
