import { AppRouteRecord } from '@/types/router'

export const scenicRoutes: AppRouteRecord = {
  path: '/scenic',
  name: 'Scenic',
  component: '/index/index',
  meta: {
    title: 'menus.scenic.title',
    icon: 'ri:landscape-line',
    keepAlive: false
  },
  children: [
    {
      path: 'scenicpage',
      name: 'ScenicPage',
      component: '/scenic/scenicpage',
      meta: {
        title: 'menus.scenic.scenicpage',
        icon: 'ri:landscape-line',
        keepAlive: false,
        fixedTab: false
      }
    }
  ]
}

