package aftnos.aftourismserver.auth.service.impl;

import aftnos.aftourismserver.auth.dto.UserInfoResponse;
import aftnos.aftourismserver.auth.service.AdminUserInfoService;
import aftnos.aftourismserver.common.exception.UnauthorizedException;
import aftnos.aftourismserver.common.security.AdminPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理端用户信息查询服务实现。
 */
@Service
public class AdminUserInfoServiceImpl implements AdminUserInfoService {

    @Override
    public UserInfoResponse getCurrentAdminInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UnauthorizedException("未登录或令牌无效");
        }
        if (!(authentication.getPrincipal() instanceof AdminPrincipal adminPrincipal)) {
            throw new UnauthorizedException("当前登录主体不是管理员");
        }

        List<String> roles = new ArrayList<>(adminPrincipal.getRoleCodes());
        List<String> buttons = new ArrayList<>(adminPrincipal.getButtonAuthMarks());
        return UserInfoResponse.builder()
                .userId(adminPrincipal.getId())
                .userName(adminPrincipal.getUsername())
                .roles(roles)
                .buttons(buttons)
                .nickName(adminPrincipal.getRealName())
                .phone(adminPrincipal.getPhone())
                .avatar(adminPrincipal.getAvatar())
                .gender(adminPrincipal.getGender())
                .email(adminPrincipal.getEmail())
                .remark(adminPrincipal.getRemark())
                .introduction(adminPrincipal.getIntroduction())
                .build();
    }
}
