import request from './request';

interface PageResult<T> {
  records: T[];
  total: number;
}

export function fetchAdmins(params: Record<string, any>) {
  return request.get<PageResult<any>>('/admin/admins', { params });
}

export function saveAdmin(data: Record<string, any>) {
  return request.post<void>('/admin/admins', data);
}

export function updateAdmin(id: string, data: Record<string, any>) {
  return request.put<void>(`/admin/admins/${id}`, data);
}

export function disableAdmin(id: string) {
  return request.put<void>(`/admin/admins/${id}/disable`, {});
}

export function fetchUsers(params: Record<string, any>) {
  return request.get<PageResult<any>>('/admin/users', { params });
}

export function saveUser(data: Record<string, any>) {
  return request.post<void>('/admin/users', data);
}

export function updateUser(id: string, data: Record<string, any>) {
  return request.put<void>(`/admin/users/${id}`, data);
}

export function disableUser(id: string) {
  return request.put<void>(`/admin/users/${id}/disable`, {});
}

export function fetchRoles() {
  return request.get<any[]>('/admin/roles');
}

export function saveRole(data: Record<string, any>) {
  return request.post<void>('/admin/roles', data);
}

export function updateRole(code: string, data: Record<string, any>) {
  return request.put<void>(`/admin/roles/${code}`, data);
}

export function fetchRolePermissions(code: string) {
  return request.get<{ permissions: string[] }>(`/admin/roles/${code}/permissions`);
}

export function updateRolePermissions(code: string, permissions: string[]) {
  return request.put<void>(`/admin/roles/${code}/permissions`, { permissions });
}
