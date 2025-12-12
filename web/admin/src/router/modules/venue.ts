import { AppRouteRecord } from '@/types/router'

export const venueRoutes: AppRouteRecord = {
  path: '/venue',
  name: 'Venue',
  component: '/index/index',
  meta: {
    title: 'menus.venue.title',
    icon: 'ri:building-2-line',
    keepAlive: false
  },
  children: [
    {
      path: 'venuepage',
      name: 'VenuePage',
      component: '/venue/venuepage',
      meta: {
        title: 'menus.venue.venuepage',
        icon: 'ri:building-2-line',
        keepAlive: false,
        fixedTab: false
      }
    }
  ]
}

