declare namespace Api {
  namespace Qualification {
    interface QualificationItem {
      id: number
      userId: number
      userName: string
      realName: string
      organization: string
      contactPhone: string
      applyReason: string
      attachmentUrl?: string
      applyStatus: number
      auditRemark?: string
      createTime: string
      updateTime: string
    }

    type QualificationPage = Api.Common.PaginatedResponse<QualificationItem>

    type QualificationSearchParams = Partial<
      Pick<QualificationItem, 'userName' | 'applyStatus'> & Api.Common.CommonSearchParams
    >

    interface QualificationAuditRequest {
      auditRemark?: string
    }
  }
}
