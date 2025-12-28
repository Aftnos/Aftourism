import request from '@/utils/http'

/**
 * 获取管理员个人资料
 */
export function fetchAdminProfile() {
  return request.get<Api.AdminProfile.Profile>({
    url: '/api/admin/profile'
  })
}

/**
 * 更新管理员个人资料
 */
export function updateAdminProfile(data: Api.AdminProfile.UpdateParams) {
  return request.put<void>({
    url: '/api/admin/profile',
    data,
    showSuccessMessage: true
  })
}

/**
 * 修改管理员密码
 */
export function updateAdminPassword(data: Api.AdminProfile.PasswordUpdateParams) {
  return request.put<void>({
    url: '/api/admin/profile/password',
    data,
    showSuccessMessage: true
  })
}
