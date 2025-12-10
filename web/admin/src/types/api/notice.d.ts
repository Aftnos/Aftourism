declare namespace Api {
  namespace Notice {
    interface NoticeItem {
      id: number
      title: string
      content: string
      author: string
      status: 0 | 1
      publishTime?: string
      viewCount?: number
      createTime?: string
      updateTime?: string
    }

    type NoticeSearchParams = Partial<
      Pick<NoticeItem, 'title' | 'status'> & Api.Common.CommonSearchParams
    >
  }
}

