import { AppRouteRecord } from '@/types/router'

export const userRoutes: AppRouteRecord = {
  path: '/user',
  name: 'UserRoot',
  component: '/index/index',
  meta: {
    title: 'menus.system.user',
    icon: 'ri:user-3-line',
    roles: ['R_SUPER', 'R_ADMIN']
  },
  children: [
    {
      path: 'user',
      name: 'AdminUser',
      component: '/system/user',
      meta: {
        title: 'menus.system.adminUser',
        icon: 'ri:user-line',
        keepAlive: true,
        roles: ['R_SUPER', 'R_ADMIN']
      }
    },
    {
      path: 'portal-user',
      name: 'PortalUser',
      component: '/system/portal-user',
      meta: {
        title: 'menus.system.portalUser',
        icon: 'ri:user-smile-line',
        keepAlive: true,
        roles: ['R_SUPER', 'R_ADMIN']
      }
    },
    {
      path: 'role',
      name: 'Role',
      component: '/system/role',
      meta: {
        title: 'menus.system.role',
        icon: 'ri:user-settings-line',
        keepAlive: true,
        roles: ['R_SUPER']
      }
    }
  ]
}
