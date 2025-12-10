import request from '@/utils/http'

export function fetchGetNoticePage(params: Api.Notice.NoticeSearchParams) {
  return request.get<{ total: number; list: Api.Notice.NoticeItem[]; pageNum?: number; pageSize?: number }>({
    url: '/api/admin/notice/page',
    params
  })
}

export function createNotice(data: Partial<Api.Notice.NoticeItem>) {
  return request.post<number>({
    url: '/api/admin/notice',
    data,
    showSuccessMessage: true
  })
}

export function updateNotice(id: number, data: Partial<Api.Notice.NoticeItem>) {
  return request.put<void>({
    url: `/api/admin/notice/${id}`,
    data,
    showSuccessMessage: true
  })
}

export function deleteNotice(id: number) {
  return request.del<void>({
    url: `/api/admin/notice/${id}`,
    // showSuccessMessage: true
  })
}

