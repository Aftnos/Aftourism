import { AppRouteRecord } from '@/types/router'

/**
 * 运维保障模块路由配置
 * 包含服务器监控等相关功能的路由定义
 */
export const safeguardRoutes: AppRouteRecord = {
  // 路由访问路径，父级路由
  path: '/safeguard',
  // 路由名称，用于程序中导航，必须唯一
  name: 'Safeguard',
  // 对应的组件路径，这里通常作为布局组件
  component: '/index/index',
  // 路由元信息，用于菜单显示和权限控制
  meta: {
    // 菜单标题，对应国际化语言包中的 key
    title: 'menus.safeguard.title',
    // 菜单图标，使用 Iconify 图标库
    icon: 'ri:shield-check-line',
    // 是否缓存该页面，false 表示不缓存
    keepAlive: false
  },
  // 子路由列表
  children: [
    {
      // 子路由路径，相对于父级路径
      path: 'server',
      // 子路由名称
      name: 'SafeguardServer',
      // 对应的页面组件路径
      component: '/safeguard/server',
      // 子路由元信息
      meta: {
        // 页面标题
        title: 'menus.safeguard.server',
        // 页面图标
        icon: 'ri:hard-drive-3-line',
        // 开启页面缓存
        keepAlive: true
      }
    }
  ]
}

