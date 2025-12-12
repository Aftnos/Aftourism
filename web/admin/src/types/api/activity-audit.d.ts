declare namespace Api {
  namespace ActivityAudit {
    interface ActivityRejectDTO {
      rejectReason: string
    }

    interface ActivityAuditItemVO {
      id: number
      name: string
      category?: string
      venueId?: number
      venueName?: string
      addressCache?: string
      startTime?: string
      endTime?: string
      submitTime?: string
      applyStatus: number
      onlineStatus: number
    }

    interface ActivityAuditDetailVO {
      id: number
      name: string
      coverUrl?: string
      category?: string
      venueId?: number
      venueName?: string
      addressCache?: string
      organizer?: string
      contactPhone?: string
      intro?: string
      applyUserId?: number
      applyStatus: number
      rejectReason?: string
      auditRemark?: string
      activityId?: number
      onlineStatus: number
      startTime?: string
      endTime?: string
      submitTime?: string
    }

    type ActivityAuditSearchParams = Partial<
      { name: string; applyStatus: number; onlineStatus: number; startTimeFrom: string; startTimeTo: string } & Api.Common.CommonSearchParams
    >
  }
}
