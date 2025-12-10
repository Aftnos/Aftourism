import { AppRouteRecord } from '@/types/router'

export const noticeRoutes: AppRouteRecord = {
  path: '/notice',
  name: 'Notice',
  component: '/index/index',
  meta: {
    title: 'menus.notice.title',
    icon: 'ri:notification-2-line',
    keepAlive: false
  },
  children: [
    {
      path: 'noticepage',
      name: 'NoticePage',
      component: '/notice/noticepage',
      meta: {
        title: 'menus.notice.noticepage',
        icon: 'ri:notification-2-line',
        keepAlive: false,
        fixedTab: false
      }
    }
  ]
}

