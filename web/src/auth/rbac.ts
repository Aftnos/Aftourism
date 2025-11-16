import type { RouteRecordRaw } from 'vue-router';
import type { PrincipalType } from '@/api/auth';

export interface UserProfile {
  principalId: number;
  principalType: PrincipalType;
  userId: number;
  username: string;
  nickname?: string;
  avatar?: string;
  realName?: string;
  phone?: string;
  email?: string;
  status?: number;
  superAdmin: boolean;
  roles: string[];
  permissions: string[];
}

// 判断是否拥有某个权限点
export function hasPermission(user: UserProfile | null, code?: string | string[]) {
  if (!code) return true;
  if (!user) return false;
  if (user.superAdmin) return true;
  const list = Array.isArray(code) ? code : [code];
  return list.some((item) => user.permissions?.includes(item));
}

// 根据权限过滤路由树
export function filterRoutes(user: UserProfile | null, routes: RouteRecordRaw[]) {
  return routes.filter((route) => {
    const meta = route.meta || {};
    const required = meta.permission as string | string[] | undefined;
    if (!user) return false;
    const allow = !required || hasPermission(user, required) || user.superAdmin;
    if (!allow) return false;
    if (route.children) {
      route.children = filterRoutes(user, route.children);
    }
    return true;
  });
}
