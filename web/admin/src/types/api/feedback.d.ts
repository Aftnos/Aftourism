declare namespace Api {
  namespace Feedback {
    interface FeedbackItem {
      id: number
      userId: number
      userName?: string
      userNickname?: string
      title?: string
      content: string
      contactPhone?: string
      contactEmail?: string
      status: number
      createTime?: string
      updateTime?: string
    }

    type FeedbackPage = {
      list: FeedbackItem[]
      pageNum: number
      pageSize: number
      total: number
    }

    type FeedbackSearchParams = Partial<
      Pick<FeedbackItem, 'status'> & {
        keyword?: string
        current?: number
        size?: number
      }
    >

    interface FeedbackUpdateRequest {
      title?: string
      content: string
      contactPhone?: string
      contactEmail?: string
      status: number
    }
  }
}
