import type { RouteRecordRaw } from 'vue-router';

export interface UserProfile {
  userId: string;
  username: string;
  roleCode: string;
  isSuper: boolean;
  permissions: string[];
}

// 判断是否拥有某个权限点
export function hasPermission(user: UserProfile | null, code?: string | string[]) {
  if (!code) return true;
  if (!user) return false;
  if (user.isSuper) return true;
  const list = Array.isArray(code) ? code : [code];
  return list.some((item) => user.permissions?.includes(item));
}

// 根据权限过滤路由树
export function filterRoutes(user: UserProfile | null, routes: RouteRecordRaw[]) {
  return routes.filter((route) => {
    const meta = route.meta || {};
    const required = meta.permission as string | string[] | undefined;
    if (!user) return false;
    const allow = !required || hasPermission(user, required) || user.isSuper;
    if (!allow) return false;
    if (route.children) {
      route.children = filterRoutes(user, route.children);
    }
    return true;
  });
}
