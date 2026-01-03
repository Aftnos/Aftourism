package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.RolePermissionItem;
import aftnos.aftourismserver.admin.dto.RolePermissionUpsertRequest;
import aftnos.aftourismserver.admin.service.RoleAccessManageService;
import aftnos.aftourismserver.admin.vo.PermissionDefinitionVO;
import aftnos.aftourismserver.admin.vo.RolePermissionVO;
import aftnos.aftourismserver.admin.vo.RoleSummaryVO;
import aftnos.aftourismserver.auth.mapper.RoleAccessMapper;
import aftnos.aftourismserver.auth.pojo.RoleAccess;
import aftnos.aftourismserver.common.exception.BusinessException;
import aftnos.aftourismserver.common.security.AdminPermission;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色权限配置服务实现。
 */
@Service
public class RoleAccessManageServiceImpl implements RoleAccessManageService {

    private final RoleAccessMapper roleAccessMapper;

    public RoleAccessManageServiceImpl(RoleAccessMapper roleAccessMapper) {
        this.roleAccessMapper = roleAccessMapper;
    }

    @Override
    public List<RoleSummaryVO> listRoles() {
        List<String> roleCodes = roleAccessMapper.findAllRoleCodes();
        List<RoleSummaryVO> result = new ArrayList<>();
        for (String roleCode : roleCodes) {
            List<RoleAccess> accesses = roleAccessMapper.findByRoleCode(roleCode);
            RoleSummaryVO vo = new RoleSummaryVO();
            vo.setRoleCode(roleCode);
            vo.setPermissions(accesses.stream()
                    .map(access -> new RolePermissionVO(
                            access.getResourceKey(),
                            access.getAction(),
                            access.getAllow() != null && access.getAllow() == 1,
                            access.getRemark()))
                    .collect(Collectors.toList()));
            result.add(vo);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRolePermissions(RolePermissionUpsertRequest request) {
        String roleCode = normalizeRole(request.getRoleCode());
        if (!StringUtils.hasText(roleCode)) {
            throw new BusinessException("角色编码不能为空");
        }
        roleAccessMapper.deleteByRoleCode(roleCode);
        if (CollectionUtils.isEmpty(request.getPermissions())) {
            return;
        }
        Map<String, RoleAccess> merged = new LinkedHashMap<>();
        for (RolePermissionItem item : request.getPermissions()) {
            String resource = normalize(item.getResourceKey());
            String action = normalize(item.getAction());
            String key = resource + ":" + action;
            RoleAccess record = new RoleAccess();
            record.setRoleCode(roleCode);
            record.setResourceKey(resource);
            record.setAction(action);
            record.setAllow(Boolean.TRUE.equals(item.getAllow()) ? 1 : 0);
            // 如果权限项有单独的备注，则使用权限项的备注；否则使用请求中的角色描述
            record.setRemark(StringUtils.hasText(item.getRemark()) ? item.getRemark() : request.getRemark());
            merged.put(key, record);
        }
        if (!merged.isEmpty()) {
            roleAccessMapper.batchInsert(new ArrayList<>(merged.values()));
        }
    }

    @Override
    public List<PermissionDefinitionVO> permissionCatalog() {
        List<PermissionDefinitionVO> list = new ArrayList<>();
        for (AdminPermission permission : AdminPermission.values()) {
            list.add(new PermissionDefinitionVO(
                    permission.asKey(),
                    permission.resourceKey(),
                    permission.action(),
                    permission.description()
            ));
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(String roleCode) {
        String normalized = normalizeRole(roleCode);
        if (!StringUtils.hasText(normalized)) {
            throw new BusinessException("角色编码不能为空");
        }
        roleAccessMapper.deleteByRoleCode(normalized);
    }

    private String normalizeRole(String roleCode) {
        if (roleCode == null) {
            return null;
        }
        return roleCode.trim().toUpperCase();
    }

    private String normalize(String text) {
        if (text == null) {
            return "*";
        }
        String trimmed = text.trim();
        return trimmed.isEmpty() ? "*" : trimmed.toUpperCase();
    }
}
