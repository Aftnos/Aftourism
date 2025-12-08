package aftnos.aftourismserver.auth.service.impl;

import aftnos.aftourismserver.auth.mapper.MenuMapper;
import aftnos.aftourismserver.auth.pojo.Menu;
import aftnos.aftourismserver.auth.pojo.MenuPermission;
import aftnos.aftourismserver.auth.service.MenuQueryService;
import aftnos.aftourismserver.auth.vo.AuthPermissionVO;
import aftnos.aftourismserver.auth.vo.MenuRouteVO;
import aftnos.aftourismserver.auth.vo.RouteMetaVO;
import aftnos.aftourismserver.common.exception.UnauthorizedException;
import aftnos.aftourismserver.common.security.AdminPrincipal;
import aftnos.aftourismserver.common.security.PortalUserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 菜单查询服务实现，严格按照 docs/RBAC/RBAC.md 结构生成菜单树。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MenuQueryServiceImpl implements MenuQueryService {

    private final MenuMapper menuMapper;

    @Override
    public List<MenuRouteVO> loadCurrentUserMenus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UnauthorizedException("未登录或令牌无效");
        }

        Object principal = authentication.getPrincipal();
        // 前台用户暂不返回后台菜单，直接给空列表，避免无权限访问
        if (principal instanceof PortalUserPrincipal) {
            return Collections.emptyList();
        }
        if (!(principal instanceof AdminPrincipal adminPrincipal)) {
            throw new UnauthorizedException("不支持的主体类型");
        }

        boolean superAdmin = adminPrincipal.isSuperAdmin();
        Set<String> roleCodes = adminPrincipal.getRoleCodes();

        // 查询菜单 + 权限
        List<Menu> menus = superAdmin
                ? menuMapper.selectEnabledMenus()
                : menuMapper.selectMenusByRoleCodes(roleCodes);
        if (CollectionUtils.isEmpty(menus)) {
            return Collections.emptyList();
        }

        List<MenuPermission> permissions = superAdmin
                ? menuMapper.selectAllPermissions()
                : menuMapper.selectPermissionsByRoleCodes(roleCodes);
        Map<Long, List<MenuPermission>> permissionMap = permissions.stream()
                .collect(Collectors.groupingBy(MenuPermission::getMenuId));

        // 按照order_num字段从小到大排序
        menus.sort(Comparator.comparing(Menu::getOrderNum, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Menu::getId));

        // 构建树结构
        Map<Long, MenuRouteVO> cached = new HashMap<>();
        List<MenuRouteVO> roots = new ArrayList<>();
        for (Menu menu : menus) {
            MenuRouteVO node = convertToRoute(menu, roleCodes, permissionMap.get(menu.getId()));
            cached.put(menu.getId(), node);
        }
        for (Menu menu : menus) {
            MenuRouteVO current = cached.get(menu.getId());
            Long parentId = menu.getParentId();
            if (parentId == null || parentId == 0L || !cached.containsKey(parentId)) {
                roots.add(current);
                continue;
            }
            cached.get(parentId).getChildren().add(current);
        }
        return roots;
    }

    /**
     * 将数据表中的菜单转换为前端需要的路由节点。
     */
    private MenuRouteVO convertToRoute(Menu menu, Set<String> roleCodes, List<MenuPermission> menuPermissions) {
        MenuRouteVO vo = new MenuRouteVO();
        vo.setId(menu.getId());
        vo.setPath(menu.getPath());
        vo.setName(menu.getName());
        vo.setRedirect(menu.getRedirect());
        vo.setComponent(menu.getComponent());

        RouteMetaVO meta = new RouteMetaVO();
        meta.setTitle(menu.getTitle());
        meta.setIcon(menu.getIcon());
        meta.setIsHide(toBool(menu.getIsHide()));
        meta.setIsHideTab(toBool(menu.getIsHideTab()));
        meta.setShowBadge(toBool(menu.getShowBadge()));
        meta.setShowTextBadge(menu.getShowTextBadge());
        meta.setKeepAlive(toBool(menu.getKeepAlive()));
        meta.setFixedTab(toBool(menu.getFixedTab()));
        meta.setActivePath(menu.getActivePath());
        meta.setLink(menu.getLink());
        meta.setIsIframe(toBool(menu.getIsIframe()));
        meta.setIsFullPage(toBool(menu.getIsFullPage()));
        meta.setIsFirstLevel(toBool(menu.getIsFirstLevel()));
        meta.setParentPath(menu.getParentPath());
        meta.setIsAuthButton(Boolean.FALSE);
        meta.setRoles(new ArrayList<>(roleCodes));

        if (!CollectionUtils.isEmpty(menuPermissions)) {
            List<AuthPermissionVO> authList = menuPermissions.stream()
                    .sorted(Comparator.comparing(MenuPermission::getSort, Comparator.nullsLast(Comparator.reverseOrder()))
                            .thenComparing(MenuPermission::getId))
                    .map(item -> new AuthPermissionVO(item.getTitle(), item.getAuthMark()))
                    .toList();
            meta.setAuthList(authList);
        }
        vo.setMeta(meta);
        return vo;
    }

    private Boolean toBool(Integer flag) {
        return flag != null && flag == 1;
    }
}
