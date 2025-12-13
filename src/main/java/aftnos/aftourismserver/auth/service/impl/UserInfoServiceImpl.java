package aftnos.aftourismserver.auth.service.impl;

import aftnos.aftourismserver.auth.dto.UserInfoResponse;
import aftnos.aftourismserver.auth.dto.UserInfoUpdateRequest;
import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.auth.service.UserInfoService;
import aftnos.aftourismserver.common.exception.UnauthorizedException;
import aftnos.aftourismserver.common.security.AdminPrincipal;
import aftnos.aftourismserver.common.security.PortalUserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用户信息查询服务实现类，从 Spring Security 上下文中解析出当前登录人的信息。
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    private final UserMapper userMapper;

    public UserInfoServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

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
                    .nickName(adminPrincipal.getRealName())
                    .phone(adminPrincipal.getPhone())
                    .avatar(adminPrincipal.getAvatar())
                    .gender(adminPrincipal.getGender())
                    .email(adminPrincipal.getEmail())
                    .remark(adminPrincipal.getRemark())
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
                    .nickName(portalUserPrincipal.getNickname())
                    .phone(portalUserPrincipal.getPhone())
                    .avatar(portalUserPrincipal.getAvatar())
                    .gender(portalUserPrincipal.getGender())
                    .email(portalUserPrincipal.getEmail())
                    .remark(portalUserPrincipal.getRemark())
                    .build();
        }

        // 非预期的主体类型
        throw new UnauthorizedException("不支持的登录主体类型");
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
            // 目前仅开放门户用户个人资料修改，后台管理员另有独立配置入口
            throw new BusinessException("仅门户用户可以修改个人资料");
        }

        // 字段统一去除首尾空格，性别字段做枚举兜底
        String nickname = trimToNull(request.getNickName());
        String phone = trimToNull(request.getPhone());
        String email = trimToNull(request.getEmail());
        String avatar = trimToNull(request.getAvatar());
        String remark = trimToNull(request.getRemark());
        String gender = normalizeGender(request.getGender());

        // 执行数据库更新，确保符合 t_user 设计
        userMapper.updateProfile(portalUser.getId(), nickname, gender, phone, email, avatar, remark);

        // 重新拉取最新的用户信息，刷新安全上下文，保证后续 /auth/info 立即返回新数据
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

    /**
     * 将前端传入的性别值规范化为「男/女/未知」。
     */
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

    /**
     * 去除首尾空格，如果为空字符串则返回 null 方便数据库存储 NULL。
     */
    private String trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}