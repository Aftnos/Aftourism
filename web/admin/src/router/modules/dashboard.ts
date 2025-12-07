import { AppRouteRecord } from '@/types/router'

// 仪表盘路由配置
export const dashboardRoutes: AppRouteRecord = {
  name: 'Dashboard', // 路由名称，必须唯一
  path: '/dashboard', // 路由路径
  component: '/index/index', // 对应的组件路径
  meta: {
    title: 'menus.dashboard.title', // 菜单标题（支持国际化）
    icon: 'ri:pie-chart-line', // 菜单图标
    roles: ['R_SUPER', 'R_ADMIN'] // 权限角色控制
  },
  children: [
    {
      path: 'console', // 子路由路径
      name: 'Console', // 子路由名称
      component: '/dashboard/console', // 子路由组件
      meta: {
        title: 'menus.dashboard.console', // 标题
        icon: 'ri:home-smile-2-line', // 图标
        keepAlive: false, // 是否开启页面缓存
        fixedTab: true // 是否固定在标签页
      }
    },
    {
      path: 'analysis',
      name: 'Analysis',
      component: '/dashboard/analysis',
      meta: {
        title: 'menus.dashboard.analysis', // 标题
        icon: 'ri:align-item-bottom-line', // 图标
        keepAlive: false // 是否开启页面缓存
      }
    },
    {
      path: 'ecommerce',
      name: 'Ecommerce',
      component: '/dashboard/ecommerce',
      meta: {
        title: 'menus.dashboard.ecommerce', // 标题
        icon: 'ri:bar-chart-box-line', // 图标
        keepAlive: false // 是否开启页面缓存
      }
    }
  ]
}
