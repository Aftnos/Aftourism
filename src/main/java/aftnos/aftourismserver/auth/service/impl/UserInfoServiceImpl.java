package aftnos.aftourismserver.auth.service.impl;

import aftnos.aftourismserver.auth.dto.UserInfoResponse;
import aftnos.aftourismserver.auth.service.UserInfoService;
import aftnos.aftourismserver.common.exception.UnauthorizedException;
import aftnos.aftourismserver.common.security.AdminPrincipal;
import aftnos.aftourismserver.common.security.PortalUserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用户信息查询服务实现类，从 Spring Security 上下文中解析出当前登录人的信息。
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Override
    public UserInfoResponse getCurrentUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 没有认证信息时直接抛出未授权异常
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UnauthorizedException("未登录或令牌无效");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof AdminPrincipal adminPrincipal) {
            // 管理端用户：返回角色列表与权限按钮集合
            List<String> roles = new ArrayList<>(adminPrincipal.getRoleCodes());
            List<String> buttons = new ArrayList<>(adminPrincipal.getButtonAuthMarks());
            return UserInfoResponse.builder()
                    .userId(adminPrincipal.getId())
                    .userName(adminPrincipal.getUsername())
                    .roles(roles)
                    .buttons(buttons)
                    .email(adminPrincipal.getEmail())
                    .build();
        }

        if (principal instanceof PortalUserPrincipal portalUserPrincipal) {
            // 门户用户：角色仅有一个，按钮权限暂时留空
            List<String> roles = Collections.singletonList(portalUserPrincipal.getRoleCode());
            return UserInfoResponse.builder()
                    .userId(portalUserPrincipal.getId())
                    .userName(portalUserPrincipal.getUsername())
                    .roles(roles)
                    .buttons(Collections.emptyList())
                    .email(portalUserPrincipal.getEmail())
                    .build();
        }

        // 非预期的主体类型
        throw new UnauthorizedException("不支持的登录主体类型");
    }
}
