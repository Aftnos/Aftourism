declare namespace Api {
  namespace Home {
    /** 首页轮播项 */
    interface BannerItem {
      id?: number
      title?: string
      imageUrl: string
      linkUrl?: string
      sort?: number
      isEnabled?: number
      enabled?: boolean
    }

    /** 简介信息 */
    interface IntroItem {
      id?: number
      title: string
      content: string
      coverUrl?: string
    }

    /** 首页配置整体 */
    interface HomeContent {
      intro?: IntroItem
      banners: BannerItem[]
    }

    /** 保存请求体 */
    interface HomeContentSave {
      title: string
      content: string
      coverUrl?: string
      banners: Array<Pick<BannerItem, 'title' | 'imageUrl' | 'linkUrl' | 'sort' | 'enabled'>>
    }
  }
}
