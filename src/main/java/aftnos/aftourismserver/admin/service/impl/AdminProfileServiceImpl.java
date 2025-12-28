package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.AdminPasswordUpdateRequest;
import aftnos.aftourismserver.admin.dto.AdminProfileUpdateRequest;
import aftnos.aftourismserver.admin.service.AdminProfileService;
import aftnos.aftourismserver.admin.vo.AdminProfileVO;
import aftnos.aftourismserver.auth.mapper.AdminMapper;
import aftnos.aftourismserver.auth.pojo.Admin;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.common.security.AdminPrincipal;
import aftnos.aftourismserver.common.security.RbacAuthorityService;
import aftnos.aftourismserver.common.security.SecurityUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 管理员个人中心服务实现。
 */
@Service
public class AdminProfileServiceImpl implements AdminProfileService {

    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;
    private final RbacAuthorityService rbacAuthorityService;

    public AdminProfileServiceImpl(AdminMapper adminMapper,
                                   PasswordEncoder passwordEncoder,
                                   RbacAuthorityService rbacAuthorityService) {
        this.adminMapper = adminMapper;
        this.passwordEncoder = passwordEncoder;
        this.rbacAuthorityService = rbacAuthorityService;
    }

    @Override
    public AdminProfileVO currentProfile() {
        Admin admin = loadCurrentAdmin();
        return toProfileVO(admin);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProfile(AdminProfileUpdateRequest request) {
        Admin admin = loadCurrentAdmin();
        Admin update = new Admin();
        update.setId(admin.getId());
        update.setRealName(trimToNull(request.getRealName()));
        update.setPhone(trimToNull(request.getPhone()));
        update.setEmail(trimToNull(request.getEmail()));
        update.setAvatar(trimToNull(request.getAvatar()));
        update.setIntroduction(trimToNull(request.getIntroduction()));
        adminMapper.update(update);
        refreshSecurityContext(admin.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(AdminPasswordUpdateRequest request) {
        Admin admin = loadCurrentAdmin();
        if (!passwordEncoder.matches(request.getCurrentPassword(), admin.getPassword())) {
            throw new BusinessException("当前密码不正确");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("两次输入的新密码不一致");
        }
        Admin update = new Admin();
        update.setId(admin.getId());
        update.setPassword(passwordEncoder.encode(request.getNewPassword()));
        adminMapper.update(update);
    }

    /**
     * 获取当前登录的管理员数据，若不存在则抛出异常。
     */
    private Admin loadCurrentAdmin() {
        Long adminId = SecurityUtils.currentAdminIdOrThrow();
        Admin admin = adminMapper.findById(adminId);
        if (admin == null || (admin.getIsDeleted() != null && admin.getIsDeleted() == 1)) {
            throw new BusinessException("管理员不存在或已被删除");
        }
        return admin;
    }

    /**
     * 刷新安全上下文中的管理员信息。
     */
    private void refreshSecurityContext(Long adminId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin latest = adminMapper.findById(adminId);
        if (latest == null || authentication == null) {
            return;
        }
        AdminPrincipal refreshedPrincipal = rbacAuthorityService.buildAdminPrincipal(latest);
        UsernamePasswordAuthenticationToken refreshedAuth = new UsernamePasswordAuthenticationToken(
                refreshedPrincipal,
                authentication.getCredentials(),
                refreshedPrincipal.getAuthorities()
        );
        refreshedAuth.setDetails(authentication.getDetails());
        SecurityContextHolder.getContext().setAuthentication(refreshedAuth);
    }

    /**
     * 将管理员实体转换为个人中心展示对象。
     */
    private AdminProfileVO toProfileVO(Admin admin) {
        AdminProfileVO vo = new AdminProfileVO();
        vo.setId(admin.getId());
        vo.setUsername(admin.getUsername());
        vo.setRealName(admin.getRealName());
        vo.setPhone(admin.getPhone());
        vo.setEmail(admin.getEmail());
        vo.setAvatar(admin.getAvatar());
        vo.setIntroduction(admin.getIntroduction());
        return vo;
    }

    /**
     * 去除首尾空格，空字符串返回 null，方便数据库存储。
     */
    private String trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
