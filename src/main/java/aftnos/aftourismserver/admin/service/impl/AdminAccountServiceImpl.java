package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.AdminAccountCreateRequest;
import aftnos.aftourismserver.admin.dto.AdminAccountPageQuery;
import aftnos.aftourismserver.admin.dto.AdminAccountUpdateRequest;
import aftnos.aftourismserver.admin.service.AdminAccountService;
import aftnos.aftourismserver.admin.vo.AdminAccountVO;
import aftnos.aftourismserver.auth.mapper.AdminMapper;
import aftnos.aftourismserver.auth.pojo.Admin;
import aftnos.aftourismserver.common.exception.BusinessException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理员账号管理服务实现。
 */
@Service
public class AdminAccountServiceImpl implements AdminAccountService {

    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;

    public AdminAccountServiceImpl(AdminMapper adminMapper, PasswordEncoder passwordEncoder) {
        this.adminMapper = adminMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PageInfo<AdminAccountVO> page(AdminAccountPageQuery query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<Admin> admins = adminMapper.search(query.getUsername(), query.getRealName(), query.getStatus());
        List<AdminAccountVO> list = admins.stream()
                .map(this::toVO)
                .collect(Collectors.toList());
        return new PageInfo<>(list);
    }

    @Override
    public Long create(AdminAccountCreateRequest request) {
        Admin existed = adminMapper.findByUsername(request.getUsername());
        if (existed != null) {
            throw new BusinessException("账号已存在");
        }
        Admin admin = new Admin();
        admin.setUsername(request.getUsername().trim());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRealName(request.getRealName());
        admin.setPhone(request.getPhone());
        admin.setEmail(request.getEmail());
        admin.setStatus(request.getStatus());
        admin.setRemark(request.getRemark());
        admin.setRoleCode(joinRoles(request.getRoleCodes()));
        admin.setIsSuper(Boolean.TRUE.equals(request.getSuperAdmin()) ? 1 : 0);
        adminMapper.insert(admin);
        return admin.getId();
    }

    @Override
    public void update(Long id, AdminAccountUpdateRequest request) {
        Admin admin = adminMapper.findById(id);
        if (admin == null || (admin.getIsDeleted() != null && admin.getIsDeleted() == 1)) {
            throw new BusinessException("管理员不存在");
        }
        Admin update = new Admin();
        update.setId(id);
        if (StringUtils.hasText(request.getPassword())) {
            update.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        update.setRealName(request.getRealName());
        update.setPhone(request.getPhone());
        update.setEmail(request.getEmail());
        update.setRemark(request.getRemark());
        if (request.getRoleCodes() != null) {
            update.setRoleCode(joinRoles(request.getRoleCodes()));
        }
        if (request.getSuperAdmin() != null) {
            update.setIsSuper(Boolean.TRUE.equals(request.getSuperAdmin()) ? 1 : 0);
        }
        update.setStatus(request.getStatus());
        adminMapper.update(update);
    }

    @Override
    public AdminAccountVO detail(Long id) {
        Admin admin = adminMapper.findById(id);
        if (admin == null || (admin.getIsDeleted() != null && admin.getIsDeleted() == 1)) {
            throw new BusinessException("管理员不存在");
        }
        return toVO(admin);
    }

    @Override
    public void delete(Long id) {
        Admin admin = adminMapper.findById(id);
        if (admin == null || (admin.getIsDeleted() != null && admin.getIsDeleted() == 1)) {
            return;
        }
        adminMapper.softDelete(id);
    }

    private String joinRoles(List<String> roleCodes) {
        if (CollectionUtils.isEmpty(roleCodes)) {
            return "ADMIN";
        }
        return roleCodes.stream()
                .filter(StringUtils::hasText)
                .map(role -> role.trim().toUpperCase())
                .distinct()
                .collect(Collectors.joining(","));
    }

    private List<String> splitRoles(String roleCode) {
        if (!StringUtils.hasText(roleCode)) {
            return List.of();
        }
        return Arrays.stream(roleCode.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }

    private AdminAccountVO toVO(Admin admin) {
        AdminAccountVO vo = new AdminAccountVO();
        vo.setId(admin.getId());
        vo.setUsername(admin.getUsername());
        vo.setRealName(admin.getRealName());
        vo.setPhone(admin.getPhone());
        vo.setEmail(admin.getEmail());
        vo.setStatus(admin.getStatus());
        vo.setSuperAdmin(admin.getIsSuper() != null && admin.getIsSuper() == 1);
        vo.setRemark(admin.getRemark());
        vo.setRoleCodes(splitRoles(admin.getRoleCode()));
        vo.setCreateTime(admin.getCreateTime());
        vo.setUpdateTime(admin.getUpdateTime());
        return vo;
    }
}
