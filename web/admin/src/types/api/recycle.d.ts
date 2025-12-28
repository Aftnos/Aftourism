declare namespace Api {
  namespace Recycle {
    /**
     * 回收站业务类型
     */
    type RecycleType = 'NEWS' | 'NOTICE' | 'SCENIC' | 'VENUE' | 'ACTIVITY'

    /**
     * 回收站列表项
     */
    interface RecycleItem {
      id: number
      type: RecycleType
      title: string
      deletedTime: string
      operator?: string
      extraInfo?: string
    }

    /**
     * 回收站分页数据
     */
    type RecycleList = Api.Common.PaginatedResponse<RecycleItem>

    /**
     * 回收站查询参数
     */
    type RecycleSearchParams = Partial<
      {
        type: RecycleType
        keyword: string
        startTime: string
        endTime: string
      } & Api.Common.CommonSearchParams
    >
  }
}
