import request from '@/utils/http'

/**
 * 获取首页配置（轮播 + 简介）。
 */
export function fetchHomeContent() {
  return request.get<Api.Home.HomeContent>({
    url: '/api/admin/home/content'
  })
}

/**
 * 保存首页配置。
 */
export function saveHomeContent(data: Api.Home.HomeContentSave) {
  return request.put<void>({
    url: '/api/admin/home/content',
    data,
    showSuccessMessage: true
  })
}
