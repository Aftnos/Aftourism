import request from '@/utils/http'
import { AppRouteRecord } from '@/types/router'

// 获取管理员账户列表
export function fetchGetAdminAccountList(params: Api.SystemManage.AdminAccountSearchParams) {
  return request.get<Api.SystemManage.AdminAccountList>({
    url: '/api/admin/rbac/admins/page',
    params
  })
}

// 获取管理员账户详情
export function fetchGetAdminAccountDetail(id: number) {
  return request.get<Api.SystemManage.AdminAccountItem>({
    url: `/api/admin/rbac/admins/${id}`
  })
}

// 新增管理员账户
export function fetchCreateAdminAccount(data: {
  username: string
  password: string
  realName?: string
  phone?: string
  email?: string
  roleCodes: string[]
  superAdmin?: boolean
  status?: number
  remark?: string
}) {
  return request.post<number>({
    url: '/api/admin/rbac/admins',
    data
  })
}

// 更新管理员账户
export function fetchUpdateAdminAccount(id: number, data: {
  password?: string
  realName?: string
  phone?: string
  email?: string
  roleCodes?: string[]
  superAdmin?: boolean
  status?: number
  remark?: string
}) {
  return request.put<void>({
    url: `/api/admin/rbac/admins/${id}`,
    data
  })
}

// 删除管理员账户
export function fetchDeleteAdminAccount(id: number) {
  return request.delete<void>({
    url: `/api/admin/rbac/admins/${id}`
  })
}

// 获取用户列表
export function fetchGetUserList(params: Api.SystemManage.UserSearchParams) {
  return request.get<Api.SystemManage.UserList>({
    url: '/api/admin/user/list',
    params
  })
}

// 获取角色列表
export function fetchGetRoleList(params: Api.SystemManage.RoleSearchParams) {
  return request.get<Api.SystemManage.RoleList>({
    url: '/api/admin/role/list',
    params
  })
}

// 获取角色权限汇总
export function fetchGetRolePermissions() {
  return request.get<Api.SystemManage.RolePermissionSummary[]>({
    url: '/api/admin/rbac/roles'
  })
}

// 获取权限点目录
export function fetchGetPermissionCatalog() {
  return request.get<Api.SystemManage.PermissionDefinition[]>({
    url: '/api/admin/rbac/permissions/catalog'
  })
}

// 保存角色权限
export function fetchSaveRolePermissions(data: {
  roleCode: string
  permissions: Api.SystemManage.RolePermissionItem[]
}) {
  return request.post<void>({
    url: '/api/admin/rbac/roles/permissions',
    data
  })
}

// 删除角色
export function fetchDeleteRole(roleCode: string) {
  return request.delete<void>({
    url: `/api/admin/rbac/roles/${roleCode}`
  })
}

// 获取角色菜单ID列表
export function fetchGetRoleMenuIds(roleCode: string) {
  return request.get<number[]>({
    url: `/api/admin/rbac/roles/${roleCode}/menus`
  })
}

// 保存角色菜单分配
export function fetchSaveRoleMenus(roleCode: string, data: Api.SystemManage.RoleMenuAssignRequest) {
  return request.post<void>({
    url: `/api/admin/rbac/roles/${roleCode}/menus`,
    data
  })
}

// 获取角色菜单按钮权限ID列表
export function fetchGetRoleMenuPermissionIds(roleCode: string) {
  return request.get<number[]>({
    url: `/api/admin/rbac/roles/${roleCode}/menu-permissions`
  })
}

// 保存角色菜单按钮权限分配
export function fetchSaveRoleMenuPermissions(
  roleCode: string,
  data: Api.SystemManage.RoleMenuPermissionAssignRequest
) {
  return request.post<void>({
    url: `/api/admin/rbac/roles/${roleCode}/menu-permissions`,
    data
  })
}

// 获取菜单列表
export function fetchGetMenuList() {
  return request.get<AppRouteRecord[]>({
    url: '/api/auth/menus'
  })
}

// 获取菜单管理列表
export function fetchGetMenuManageList() {
  return request.get<AppRouteRecord[]>({
    url: '/api/admin/rbac/menus'
  })
}

// 新增菜单
export function fetchCreateMenu(data: Api.SystemManage.MenuSaveRequest) {
  return request.post<number>({
    url: '/api/admin/rbac/menus',
    data
  })
}

// 更新菜单
export function fetchUpdateMenu(id: number, data: Api.SystemManage.MenuSaveRequest) {
  return request.put<void>({
    url: `/api/admin/rbac/menus/${id}`,
    data
  })
}

// 删除菜单
export function fetchDeleteMenu(id: number) {
  return request.delete<void>({
    url: `/api/admin/rbac/menus/${id}`
  })
}

// 新增菜单权限
export function fetchCreateMenuPermission(menuId: number, data: Api.SystemManage.MenuPermissionSaveRequest) {
  return request.post<number>({
    url: `/api/admin/rbac/menus/${menuId}/permissions`,
    data
  })
}

// 更新菜单权限
export function fetchUpdateMenuPermission(
  permissionId: number,
  data: Api.SystemManage.MenuPermissionSaveRequest
) {
  return request.put<void>({
    url: `/api/admin/rbac/menus/permissions/${permissionId}`,
    data
  })
}

// 删除菜单权限
export function fetchDeleteMenuPermission(permissionId: number) {
  return request.delete<void>({
    url: `/api/admin/rbac/menus/permissions/${permissionId}`
  })
}
