package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.RoleMenuAssignRequest;
import aftnos.aftourismserver.admin.dto.RoleMenuPermissionAssignRequest;
import aftnos.aftourismserver.admin.mapper.RoleMenuMapper;
import aftnos.aftourismserver.admin.service.RoleMenuPermissionService;
import aftnos.aftourismserver.common.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色菜单与按钮权限配置服务实现。
 */
@Service
public class RoleMenuPermissionServiceImpl implements RoleMenuPermissionService {

    private final RoleMenuMapper roleMenuMapper;

    public RoleMenuPermissionServiceImpl(RoleMenuMapper roleMenuMapper) {
        this.roleMenuMapper = roleMenuMapper;
    }

    @Override
    public List<Long> listRoleMenuIds(String roleCode) {
        String normalized = normalizeRole(roleCode);
        if (!StringUtils.hasText(normalized)) {
            throw new BusinessException("角色编码不能为空");
        }
        return roleMenuMapper.findMenuIdsByRoleCode(normalized);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleMenus(String roleCode, RoleMenuAssignRequest request) {
        String normalized = normalizeRole(roleCode);
        if (!StringUtils.hasText(normalized)) {
            throw new BusinessException("角色编码不能为空");
        }
        roleMenuMapper.deleteRoleMenus(normalized);
        List<Long> menuIds = normalizeIds(request.getMenuIds());
        if (CollectionUtils.isEmpty(menuIds)) {
            return;
        }
        roleMenuMapper.batchInsertRoleMenus(normalized, menuIds);
    }

    @Override
    public List<Long> listRoleMenuPermissionIds(String roleCode) {
        String normalized = normalizeRole(roleCode);
        if (!StringUtils.hasText(normalized)) {
            throw new BusinessException("角色编码不能为空");
        }
        return roleMenuMapper.findPermissionIdsByRoleCode(normalized);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleMenuPermissions(String roleCode, RoleMenuPermissionAssignRequest request) {
        String normalized = normalizeRole(roleCode);
        if (!StringUtils.hasText(normalized)) {
            throw new BusinessException("角色编码不能为空");
        }
        roleMenuMapper.deleteRoleMenuPermissions(normalized);
        List<Long> permissionIds = normalizeIds(request.getPermissionIds());
        if (CollectionUtils.isEmpty(permissionIds)) {
            return;
        }
        roleMenuMapper.batchInsertRoleMenuPermissions(normalized, permissionIds);
    }

    private String normalizeRole(String roleCode) {
        if (roleCode == null) {
            return null;
        }
        return roleCode.trim().toUpperCase();
    }

    private List<Long> normalizeIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return List.of();
        }
        Set<Long> distinct = new LinkedHashSet<>();
        for (Long id : ids) {
            if (id != null) {
                distinct.add(id);
            }
        }
        return distinct.stream().collect(Collectors.toList());
    }
}
