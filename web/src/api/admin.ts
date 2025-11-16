import request from './request';

// 定义分页响应结构，兼容 PageHelper 返回值
export interface PageInfo<T> {
  list: T[];
  total: number;
  pageNum: number;
  pageSize: number;
}

export interface AdminAccount {
  id?: number;
  username: string;
  realName?: string;
  phone?: string;
  email?: string;
  status?: number;
  superAdmin?: boolean;
  roleCodes: string[];
  remark?: string;
  password?: string;
  createTime?: string;
  updateTime?: string;
}

export interface PortalUserSummary {
  id: number;
  username: string;
  nickname: string;
  phone?: string;
  email?: string;
  roleCode?: string;
  status: number;
  createTime?: string;
  updateTime?: string;
}

export interface RoleSummary {
  roleCode: string;
  permissions: { resourceKey: string; action: string; allow: boolean; remark?: string }[];
}

export interface PermissionDefinition {
  key: string;
  resourceKey: string;
  action: string;
  description: string;
}

export function fetchAdminPage(params: Record<string, any>) {
  return request.get<PageInfo<AdminAccount>>('/admin/rbac/admins/page', { params });
}

export function createAdmin(payload: AdminAccount) {
  return request.post<number>('/admin/rbac/admins', payload);
}

export function updateAdmin(id: number, payload: Partial<AdminAccount>) {
  return request.put<void>(`/admin/rbac/admins/${id}`, payload);
}

export function deleteAdmin(id: number) {
  return request.del<void>(`/admin/rbac/admins/${id}`);
}

export function fetchPortalUsers(params: Record<string, any>) {
  return request.get<PageInfo<PortalUserSummary>>('/admin/rbac/users/page', { params });
}

export function updatePortalUser(id: number, payload: { roleCode?: string; status?: number }) {
  return request.put<void>(`/admin/rbac/users/${id}`, payload);
}

export function fetchRoles() {
  return request.get<RoleSummary[]>('/admin/rbac/roles');
}

export function fetchPermissionCatalog() {
  return request.get<PermissionDefinition[]>('/admin/rbac/permissions/catalog');
}

export function saveRolePermissions(payload: { roleCode: string; permissions: { resourceKey: string; action: string; allow: boolean; remark?: string }[] }) {
  return request.post<void>('/admin/rbac/roles/permissions', payload);
}
