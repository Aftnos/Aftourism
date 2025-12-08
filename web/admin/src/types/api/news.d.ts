declare namespace Api {
  namespace News {
    interface NewsItem {
      id: number
      title: string
      author: string
      coverUrl?: string
      status: '1' | '0'
      publishTime?: string
      content?: string
    }

    type NewsList = Api.Common.PaginatedResponse<NewsItem>

    /**
     * 新闻搜索参数类型
     * 
     * 该类型用于定义在查询新闻列表时可传入的搜索条件。
     * 它由两部分合并而成：
     * 1. 从 NewsItem 中仅选取 title（标题）和 status（状态）两个字段，
     *    表示可按新闻标题或发布状态进行筛选。
     * 2. 与 Api.Common.CommonSearchParams 合并，包含通用的分页、排序等查询参数。
     * 
     * 整体使用 Partial<> 包裹，意味着所有字段都是可选的，
     * 方便按需传入搜索条件，不传则忽略该条件。
     */
    type NewsSearchParams = Partial<
      Pick<NewsItem, 'title' | 'status'> & Api.Common.CommonSearchParams
    >
  }
}

