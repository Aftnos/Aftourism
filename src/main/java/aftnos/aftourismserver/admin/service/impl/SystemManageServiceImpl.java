package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.SystemRoleSearchQuery;
import aftnos.aftourismserver.admin.dto.SystemUserSearchQuery;
import aftnos.aftourismserver.admin.service.SystemManageService;
import aftnos.aftourismserver.admin.vo.SystemRoleVO;
import aftnos.aftourismserver.admin.vo.SystemUserVO;
import aftnos.aftourismserver.auth.mapper.AdminMapper;
import aftnos.aftourismserver.auth.mapper.RoleAccessMapper;
import aftnos.aftourismserver.auth.mapper.UserMapper;
import aftnos.aftourismserver.auth.pojo.RoleAccess;
import aftnos.aftourismserver.auth.pojo.User;
import aftnos.aftourismserver.common.vo.PageResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 系统管理接口实现，复用现有 RBAC 表结构输出前端需要的字段。
 */
@Service
public class SystemManageServiceImpl implements SystemManageService {

    private static final String DEFAULT_USER_GENDER = "未知";
    private static final String DEFAULT_AUDITOR = "system";

    private final UserMapper userMapper;
    private final AdminMapper adminMapper;
    private final RoleAccessMapper roleAccessMapper;

    public SystemManageServiceImpl(UserMapper userMapper, AdminMapper adminMapper, RoleAccessMapper roleAccessMapper) {
        this.userMapper = userMapper;
        this.adminMapper = adminMapper;
        this.roleAccessMapper = roleAccessMapper;
    }

    @Override
    public PageResponse<SystemUserVO> pageUsers(SystemUserSearchQuery query) {
        PageHelper.startPage(query.getCurrent(), query.getSize());
        List<User> users = userMapper.searchForManage(query.getUserName(), query.getNickName(), query.getUserPhone(), query.getUserEmail(), query.getStatus());
        PageInfo<User> pageInfo = new PageInfo<>(users);
        List<SystemUserVO> records = users.stream()
                .map(this::convertUser)
                .collect(Collectors.toList());
        return new PageResponse<>(records, pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal());
    }

    @Override
    public PageResponse<SystemRoleVO> pageRoles(SystemRoleSearchQuery query) {
        Map<String, LocalDateTime> roleCreateMap = loadRoleCreateTime();
        Set<String> roleCodes = loadAllRoleCodes();
        List<SystemRoleVO> allRoles = new ArrayList<>();
        long index = 1L;
        for (String code : roleCodes) {
            if (!matchesRoleFilter(code, query)) {
                continue;
            }
            SystemRoleVO vo = new SystemRoleVO();
            vo.setRoleId(index++);
            vo.setRoleCode(code);
            vo.setRoleName(buildRoleName(code));
            vo.setDescription(buildRoleDescription(code, query.getDescription()));
            vo.setEnabled(query.getEnabled() == null || Boolean.TRUE.equals(query.getEnabled()));
            vo.setCreateTime(roleCreateMap.getOrDefault(code, LocalDateTime.now()));
            allRoles.add(vo);
        }
        // 简单内存分页，角色数量通常较少
        int fromIndex = Math.max((query.getCurrent() - 1) * query.getSize(), 0);
        int toIndex = Math.min(fromIndex + query.getSize(), allRoles.size());
        List<SystemRoleVO> pageRecords = fromIndex >= toIndex ? List.of() : allRoles.subList(fromIndex, toIndex);
        return new PageResponse<>(pageRecords, query.getCurrent(), query.getSize(), (long) allRoles.size());
    }

    /**
     * 将用户实体转换为前端所需的 VO。
     */
    private SystemUserVO convertUser(User user) {
        SystemUserVO vo = new SystemUserVO();
        vo.setId(user.getId());
        vo.setAvatar(user.getAvatar());
        vo.setStatus(String.valueOf(user.getStatus()));
        vo.setUserName(user.getUsername());
        vo.setUserGender(DEFAULT_USER_GENDER);
        vo.setNickName(user.getNickname());
        vo.setUserPhone(user.getPhone());
        vo.setUserEmail(user.getEmail());
        vo.setUserRoles(splitRoles(user.getRoleCode()));
        vo.setCreateBy(DEFAULT_AUDITOR);
        vo.setCreateTime(user.getCreateTime());
        vo.setUpdateBy(DEFAULT_AUDITOR);
        vo.setUpdateTime(user.getUpdateTime());
        return vo;
    }

    /**
     * 载入全部角色编码，合并角色权限表与管理员角色字段。
     */
    private Set<String> loadAllRoleCodes() {
        Set<String> roleCodes = new LinkedHashSet<>(roleAccessMapper.findAllRoleCodes());
        List<String> adminRoleCodes = adminMapper.findAllRoleCodes();
        for (String roleCode : adminRoleCodes) {
            roleCodes.addAll(splitRoles(roleCode));
        }
        return roleCodes;
    }

    /**
     * 读取角色的创建时间（以角色权限表中的最早时间为准）。
     */
    private Map<String, LocalDateTime> loadRoleCreateTime() {
        Map<String, LocalDateTime> map = new LinkedHashMap<>();
        List<RoleAccess> accessList = roleAccessMapper.findAll();
        for (RoleAccess access : accessList) {
            LocalDateTime existed = map.get(access.getRoleCode());
            if (existed == null || (access.getCreateTime() != null && access.getCreateTime().isBefore(existed))) {
                map.put(access.getRoleCode(), access.getCreateTime());
            }
        }
        return map;
    }

    /**
     * 美化角色名称。
     */
    private String buildRoleName(String roleCode) {
        String normalized = roleCode.replace('_', ' ').toLowerCase(Locale.ROOT);
        return Arrays.stream(normalized.split(" "))
                .filter(StringUtils::hasText)
                .map(word -> word.substring(0, 1).toUpperCase(Locale.ROOT) + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    /**
     * 生成描述，若前端传递描述关键字则直接回填，便于搜索体验。
     */
    private String buildRoleDescription(String roleCode, String descriptionFilter) {
        if (StringUtils.hasText(descriptionFilter)) {
            return descriptionFilter;
        }
        return "角色编码：" + roleCode;
    }

    /**
     * 角色查询条件匹配逻辑。
     */
    private boolean matchesRoleFilter(String roleCode, SystemRoleSearchQuery query) {
        if (query == null) {
            return true;
        }
        if (StringUtils.hasText(query.getRoleCode()) && !roleCode.equalsIgnoreCase(query.getRoleCode().trim())) {
            return false;
        }
        if (StringUtils.hasText(query.getRoleName())) {
            String beautified = buildRoleName(roleCode);
            if (!beautified.toLowerCase(Locale.ROOT).contains(query.getRoleName().toLowerCase(Locale.ROOT))) {
                return false;
            }
        }
        if (query.getEnabled() != null && !query.getEnabled()) {
            return false;
        }
        if (StringUtils.hasText(query.getDescription()) && !("角色编码：" + roleCode).contains(query.getDescription())) {
            return false;
        }
        return true;
    }

    /**
     * 分割角色编码字符串为集合。
     */
    private List<String> splitRoles(String roleCode) {
        if (!StringUtils.hasText(roleCode)) {
            return List.of();
        }
        return Arrays.stream(roleCode.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .map(String::toUpperCase)
                .distinct()
                .collect(Collectors.toList());
    }
}
