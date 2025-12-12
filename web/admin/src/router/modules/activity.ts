import { AppRouteRecord } from '@/types/router'

export const activityRoutes: AppRouteRecord = {
  path: '/activity',
  name: 'Activity',
  component: '/index/index',
  meta: {
    title: 'menus.activity.title',
    icon: 'ri:calendar-event-line',
    keepAlive: false
  },
  children: [
    {
      path: 'activitypage',
      name: 'ActivityPage',
      component: '/activity/activitypage',
      meta: {
        title: 'menus.activity.activitypage',
        icon: 'ri:calendar-event-line',
        keepAlive: false,
        fixedTab: false
      }
    },
    {
      path: 'commentpage',
      name: 'ActivityCommentPage',
      component: '/activity/commentpage',
      meta: {
        title: 'menus.activity.commentpage',
        icon: 'ri:message-3-line',
        keepAlive: false,
        fixedTab: false
      }
    }
    ,
    {
      path: 'auditpage',
      name: 'ActivityAuditPage',
      component: '/activity/auditpage',
      meta: {
        title: 'menus.activity.auditpage',
        icon: 'ri:check-double-line',
        keepAlive: false,
        fixedTab: false
      }
    }
  ]
}
