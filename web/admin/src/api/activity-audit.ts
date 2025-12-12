import request from '@/utils/http'

export function fetchGetAuditPage(params: Api.ActivityAudit.ActivityAuditSearchParams) {
  return request.get<{ total: number; list: Api.ActivityAudit.ActivityAuditItemVO[]; pageNum?: number; pageSize?: number}>({
    url: '/api/admin/activity/audit/page',
    params
  })
}

export function getAuditDetail(id: number) {
  return request.get<Api.ActivityAudit.ActivityAuditDetailVO>({
    url: `/api/admin/activity/audit/${id}`
  })
}

export function approveActivityAudit(id: number) {
  return request.put<void>({
    url: `/api/admin/activity/${id}/approve`,
    showSuccessMessage: true
  })
}

export function rejectActivityAudit(id: number, data: Api.ActivityAudit.ActivityRejectDTO) {
  return request.put<void>({
    url: `/api/admin/activity/${id}/reject`,
    data,
    showSuccessMessage: true
  })
}
