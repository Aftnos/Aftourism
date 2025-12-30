package aftnos.aftourismserver.admin.service.impl;

import aftnos.aftourismserver.admin.dto.MenuPermissionSaveRequest;
import aftnos.aftourismserver.admin.dto.MenuSaveRequest;
import aftnos.aftourismserver.admin.service.MenuManageService;
import aftnos.aftourismserver.auth.mapper.MenuMapper;
import aftnos.aftourismserver.auth.pojo.Menu;
import aftnos.aftourismserver.auth.pojo.MenuPermission;
import aftnos.aftourismserver.auth.vo.AuthPermissionVO;
import aftnos.aftourismserver.auth.vo.MenuRouteVO;
import aftnos.aftourismserver.auth.vo.RouteMetaVO;
import aftnos.aftourismserver.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单管理服务实现。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MenuManageServiceImpl implements MenuManageService {

    private final MenuMapper menuMapper;

    @Override
    public List<MenuRouteVO> listMenuTree() {
        List<Menu> menus = menuMapper.selectAllMenus();
        if (CollectionUtils.isEmpty(menus)) {
            return Collections.emptyList();
        }
        List<Long> menuIds = menus.stream()
                .map(Menu::getId)
                .filter(Objects::nonNull)
                .toList();
        List<MenuPermission> permissions = menuIds.isEmpty()
                ? Collections.emptyList()
                : menuMapper.selectPermissionsByMenuIds(menuIds);
        Map<Long, List<MenuPermission>> permissionMap = permissions.stream()
                .collect(Collectors.groupingBy(MenuPermission::getMenuId));

        menus.sort(Comparator.comparing(Menu::getOrderNum, Comparator.nullsLast(Comparator.naturalOrder()))
                .reversed()
                .thenComparing(Menu::getId));

        Map<Long, MenuRouteVO> cached = new HashMap<>();
        for (Menu menu : menus) {
            cached.put(menu.getId(), toRoute(menu, permissionMap.get(menu.getId())));
        }

        List<MenuRouteVO> roots = new ArrayList<>();
        for (Menu menu : menus) {
            MenuRouteVO current = cached.get(menu.getId());
            Long parentId = menu.getParentId();
            if (parentId == null || parentId == 0 || !cached.containsKey(parentId)) {
                roots.add(current);
            } else {
                cached.get(parentId).getChildren().add(current);
            }
        }
        return roots;
    }

    @Override
    public Long createMenu(MenuSaveRequest request) {
        Menu menu = buildMenuEntity(request, null);
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(menu.getCreateTime());
        menu.setIsDeleted(0);
        menuMapper.insertMenu(menu);
        return menu.getId();
    }

    @Override
    public void updateMenu(Long id, MenuSaveRequest request) {
        Menu existing = menuMapper.selectMenuById(id);
        if (existing == null) {
            throw new BusinessException("菜单不存在或已被删除");
        }
        Menu menu = buildMenuEntity(request, existing);
        menu.setId(id);
        menu.setUpdateTime(LocalDateTime.now());
        int updated = menuMapper.updateMenu(menu);
        if (updated == 0) {
            throw new BusinessException("菜单更新失败，请稍后重试");
        }
    }

    @Override
    public void deleteMenu(Long id) {
        LocalDateTime now = LocalDateTime.now();
        int updated = menuMapper.markMenuDeleted(id, now);
        if (updated == 0) {
            throw new BusinessException("菜单删除失败，请稍后重试");
        }
    }

    @Override
    public Long createPermission(Long menuId, MenuPermissionSaveRequest request) {
        Menu menu = menuMapper.selectMenuById(menuId);
        if (menu == null) {
            throw new BusinessException("菜单不存在或已被删除");
        }
        MenuPermission permission = new MenuPermission();
        permission.setMenuId(menuId);
        permission.setTitle(request.getTitle());
        permission.setAuthMark(request.getAuthMark());
        permission.setSort(request.getSort());
        permission.setRemark(request.getRemark());
        permission.setCreateTime(LocalDateTime.now());
        permission.setUpdateTime(permission.getCreateTime());
        menuMapper.insertMenuPermission(permission);
        return permission.getId();
    }

    @Override
    public void updatePermission(Long permissionId, MenuPermissionSaveRequest request) {
        MenuPermission permission = new MenuPermission();
        permission.setId(permissionId);
        permission.setTitle(request.getTitle());
        permission.setAuthMark(request.getAuthMark());
        permission.setSort(request.getSort());
        permission.setRemark(request.getRemark());
        permission.setUpdateTime(LocalDateTime.now());
        int updated = menuMapper.updateMenuPermission(permission);
        if (updated == 0) {
            throw new BusinessException("权限更新失败，请稍后重试");
        }
    }

    @Override
    public void deletePermission(Long permissionId) {
        int deleted = menuMapper.deleteMenuPermission(permissionId);
        if (deleted == 0) {
            throw new BusinessException("权限删除失败，请稍后重试");
        }
    }

    /**
     * 构建菜单实体。
     */
    private Menu buildMenuEntity(MenuSaveRequest request, Menu existing) {
        Menu menu = existing == null ? new Menu() : existing;
        Long parentId = request.getParentId();
        if (parentId == null && existing != null) {
            parentId = existing.getParentId();
        }
        menu.setParentId(Optional.ofNullable(parentId).orElse(0L));
        menu.setName(request.getName());
        menu.setPath(request.getPath());
        String redirect = request.getRedirect();
        if (redirect == null && existing != null) {
            redirect = existing.getRedirect();
        }
        menu.setRedirect(redirect);
        String component = request.getComponent();
        if (component == null && existing != null) {
            component = existing.getComponent();
        }
        menu.setComponent(component);
        menu.setTitle(request.getTitle());
        String icon = request.getIcon();
        if (icon == null && existing != null) {
            icon = existing.getIcon();
        }
        menu.setIcon(icon);
        menu.setIsHide(request.getIsHide());
        menu.setIsHideTab(request.getIsHideTab());
        menu.setShowBadge(request.getShowBadge());
        String showTextBadge = request.getShowTextBadge();
        if (showTextBadge == null && existing != null) {
            showTextBadge = existing.getShowTextBadge();
        }
        menu.setShowTextBadge(showTextBadge);
        menu.setKeepAlive(request.getKeepAlive());
        menu.setFixedTab(request.getFixedTab());
        String activePath = request.getActivePath();
        if (activePath == null && existing != null) {
            activePath = existing.getActivePath();
        }
        menu.setActivePath(activePath);
        String link = request.getLink();
        if (link == null && existing != null) {
            link = existing.getLink();
        }
        menu.setLink(link);
        menu.setIsIframe(request.getIsIframe());
        menu.setIsFullPage(request.getIsFullPage());
        Integer firstLevel = request.getIsFirstLevel();
        if (firstLevel == null && existing != null) {
            firstLevel = existing.getIsFirstLevel();
        }
        menu.setIsFirstLevel(Optional.ofNullable(firstLevel).orElse(0));
        String parentPath = request.getParentPath();
        if (parentPath == null && existing != null) {
            parentPath = existing.getParentPath();
        }
        menu.setParentPath(parentPath);
        menu.setOrderNum(request.getOrderNum());
        menu.setStatus(request.getStatus());
        String remark = request.getRemark();
        if (remark == null && existing != null) {
            remark = existing.getRemark();
        }
        menu.setRemark(remark);
        return menu;
    }

    /**
     * 将菜单实体转换为前端路由节点。
     */
    private MenuRouteVO toRoute(Menu menu, List<MenuPermission> menuPermissions) {
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
        meta.setSort(menu.getOrderNum());
        meta.setIsEnable(toBool(menu.getStatus()));
        meta.setKeepAlive(toBool(menu.getKeepAlive()));
        meta.setFixedTab(toBool(menu.getFixedTab()));
        meta.setActivePath(menu.getActivePath());
        meta.setLink(menu.getLink());
        meta.setIsIframe(toBool(menu.getIsIframe()));
        meta.setIsFullPage(toBool(menu.getIsFullPage()));
        meta.setIsFirstLevel(toBool(menu.getIsFirstLevel()));
        meta.setParentPath(menu.getParentPath());
        meta.setIsAuthButton(Boolean.FALSE);

        if (!CollectionUtils.isEmpty(menuPermissions)) {
            List<AuthPermissionVO> authList = menuPermissions.stream()
                    .sorted(Comparator.comparing(MenuPermission::getSort, Comparator.nullsLast(Comparator.reverseOrder()))
                            .thenComparing(MenuPermission::getId))
                    .map(item -> new AuthPermissionVO(item.getId(), item.getTitle(), item.getAuthMark(), item.getSort()))
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
