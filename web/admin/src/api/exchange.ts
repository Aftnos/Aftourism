import request from '@/utils/http'

export function fetchExchangeArticlePage(params: {
  current?: number
  size?: number
  status?: number
  keyword?: string
}) {
  return request.get<Api.Exchange.ExchangeArticlePage>({
    url: '/api/admin/exchange/article/page',
    params
  })
}

export function fetchExchangeArticleDetail(id: number) {
  return request.get<Api.Exchange.ExchangeArticleItem>({
    url: `/api/admin/exchange/article/${id}`
  })
}

export function auditExchangeArticle(id: number, data: { status: number; auditRemark?: string }) {
  return request.put<void>({
    url: `/api/admin/exchange/article/${id}/audit`,
    data,
    showSuccessMessage: true
  })
}

export function fetchExchangeCommentPage(
  articleId: number,
  params: { current?: number; size?: number; parentId?: number }
) {
  return request.get<Api.Exchange.ExchangeCommentPage>({
    url: `/api/admin/exchange/article/${articleId}/comment/page`,
    params
  })
}

export function updateExchangeComment(commentId: number, data: { content: string }) {
  return request.put<void>({
    url: `/api/admin/exchange/comment/${commentId}`,
    data,
    showSuccessMessage: true
  })
}

export function deleteExchangeComment(commentId: number) {
  return request.delete<void>({
    url: `/api/admin/exchange/comment/${commentId}`,
    showSuccessMessage: true
  })
}

export function fetchExchangeReportPage(params: {
  current?: number
  size?: number
  status?: number
  targetType?: string
  keyword?: string
}) {
  return request.get<Api.Exchange.ExchangeReportPage>({
    url: '/api/admin/report/manage/page',
    params
  })
}

export function updateExchangeReport(id: number, data: { status: number; violationFlag?: number; resultRemark?: string }) {
  return request.put<void>({
    url: `/api/admin/report/manage/${id}`,
    data,
    showSuccessMessage: true
  })
}
