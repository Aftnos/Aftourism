import type { AppRouteRecord } from '@/types/router'

export const exchangeRoutes: AppRouteRecord = {
  path: '/exchange',
  name: 'Exchange',
  component: '/index/index',
  meta: {
    title: 'menus.exchange.title',
    icon: 'ri:chat-smile-3-line'
  },
  children: [
    {
      path: 'article-list',
      name: 'ExchangeArticleManage',
      component: '/exchange/article-list',
      meta: {
        title: 'menus.exchange.articleManage',
        icon: 'ri:article-line'
      }
    },
    {
      path: 'report',
      name: 'ExchangeReportManage',
      component: '/exchange/report',
      meta: {
        title: 'menus.exchange.reportManage',
        icon: 'ri:spam-2-line'
      }
    }
  ]
}
