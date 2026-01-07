import { AppRouteRecord } from '@/types/router'

export const feedbackRoutes: AppRouteRecord = {
  path: '/feedback',
  name: 'Feedback',
  component: '/index/index',
  meta: {
    title: 'menus.feedback.title',
    icon: 'ri:chat-smile-2-line',
    roles: ['R_SUPER', 'R_ADMIN']
  },
  children: [
    {
      path: 'manage',
      name: 'FeedbackManage',
      component: '/feedback',
      meta: {
        title: 'menus.feedback.manage',
        icon: 'ri:message-3-line',
        keepAlive: true
      }
    }
  ]
}
