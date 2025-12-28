import request from '@/utils/http'

/**
 * 分页获取回收站数据
 * 调用后台接口 /api/admin/recycle/page，支持类型、关键词及时间范围查询
 * @param params 回收站查询参数
 */
export function fetchRecycleList(params: Api.Recycle.RecycleSearchParams) {
  return request.get<Api.Recycle.RecycleList>({
    url: '/api/admin/recycle/page',
    params
  })
}

/**
 * 恢复回收站记录
 * @param type 回收站类型
 * @param id 记录ID
 */
export function restoreRecycleItem(type: Api.Recycle.RecycleType, id: number) {
  return request.put<void>({
    url: `/api/admin/recycle/${type}/${id}/restore`
  })
}

/**
 * 永久删除回收站记录
 * @param type 回收站类型
 * @param id 记录ID
 */
export function deleteRecycleItem(type: Api.Recycle.RecycleType, id: number) {
  return request.del<void>({
    url: `/api/admin/recycle/${type}/${id}`
  })
}
