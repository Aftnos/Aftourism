import request from '@/utils/http'

/**
 * 分页获取新闻列表
 * 调用后台管理接口 /api/admin/news/page，根据传入的查询参数返回带分页的新闻数据
 * @param params 新闻搜索参数，类型为 Api.News.NewsSearchParams
 * @returns 返回 Promise，resolve 值为 Api.Common.PaginatedResponse<Api.News.NewsItem>，
 *          包含当前页的新闻列表及分页信息
 */
export function fetchGetNewsList(params: Api.News.NewsSearchParams) {
  return request.get<Api.Common.PaginatedResponse<Api.News.NewsItem>>({
    url: '/api/admin/news/page',
    params
  })
}
/**
 * 创建新闻
 * 调用 POST /api/admin/news，将新闻数据以 JSON 形式放在请求体（data）中提交；
 * 与 updateNews 的区别：
 * 1. 使用 POST 方法，表示“新增”资源，服务端通常会在数据库生成新记录并返回新 ID；
 * 2. URL 中不含具体 ID，所有字段通过请求体传递。
 */
export function createNews(data: Partial<Api.News.NewsItem>) {
  return request.post<void>({
    url: '/api/admin/news',
    data,
    // showSuccessMessage: true
  })
}

/**
 * 更新指定 ID 的新闻
 * 调用 PUT /api/admin/news/${id}，将需要更新的字段以 JSON 形式放在请求体（data）中提交；
 * 与 createNews 的区别：
 * 1. 使用 PUT 方法，表示“全量更新”已有资源，URL 中必须携带目标资源 ID；
 * 2. 服务端根据 ID 定位记录并用请求体数据覆盖原有内容；
 * 3. 通常用于编辑已存在的新闻，而非新建。
 */
export function updateNews(id: number, data: Partial<Api.News.NewsItem>) {
  // 使用 PUT 方法向 /api/admin/news/${id} 发起请求，将 data 作为请求体提交，实现全量更新指定 ID 的新闻；
  // showSuccessMessage: true 表示请求成功后自动弹出全局成功提示
  return request.put<void>({
    url: `/api/admin/news/${id}`,
    data,
    // showSuccessMessage: true
  })
}

export function deleteNews(id: number) {
  return request.del<void>({
    url: `/api/admin/news/${id}`,
    // showSuccessMessage: true
  })
}

