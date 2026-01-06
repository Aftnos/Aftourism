import request from '@/utils/http'

/**
 * 获取资质申请分页
 */
export function fetchQualificationPage(params: Api.Qualification.QualificationSearchParams) {
  return request.get<Api.Qualification.QualificationPage>({
    url: '/api/admin/qualification/page',
    params
  })
}

/**
 * 获取资质申请详情
 */
export function fetchQualificationDetail(id: number) {
  return request.get<Api.Qualification.QualificationItem>({
    url: `/api/admin/qualification/${id}`
  })
}

/**
 * 通过资质申请
 */
export function approveQualification(id: number, data?: Api.Qualification.QualificationAuditRequest) {
  return request.put<void>({
    url: `/api/admin/qualification/${id}/approve`,
    data
  })
}

/**
 * 驳回资质申请
 */
export function rejectQualification(id: number, data: Api.Qualification.QualificationAuditRequest) {
  return request.put<void>({
    url: `/api/admin/qualification/${id}/reject`,
    data
  })
}
