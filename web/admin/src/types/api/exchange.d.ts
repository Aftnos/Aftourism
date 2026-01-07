declare namespace Api {
  namespace Exchange {
    interface ExchangeArticleItem {
      id: number
      userId: number
      userName?: string
      userNickname?: string
      title: string
      coverUrl?: string
      status: number
      statusText?: string
      likeCount?: number
      commentCount?: number
      auditRemark?: string
      createTime?: string
      updateTime?: string
    }

    interface ExchangeReportItem {
      id: number
      reporterId: number
      reporterName?: string
      reporterNickname?: string
      targetUserId: number
      targetUserName?: string
      targetUserNickname?: string
      targetType: string
      targetTypeText?: string
      targetId: number
      reasonType: string
      reasonTypeText?: string
      reason?: string
      screenshotUrls?: string
      status: number
      statusText?: string
      violationFlag?: number
      resultRemark?: string
      createTime?: string
      updateTime?: string
    }

    type ExchangeArticlePage = {
      list: ExchangeArticleItem[]
      pageNum: number
      pageSize: number
      total: number
    }

    type ExchangeReportPage = {
      list: ExchangeReportItem[]
      pageNum: number
      pageSize: number
      total: number
    }
  }
}
