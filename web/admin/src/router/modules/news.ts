import { AppRouteRecord } from '@/types/router'

/**
 * 新闻模块路由
 * 
 */
export const newsRoutes: AppRouteRecord = {
  // 路由访问路径，父级路由
  path: '/news',
  // 路由名称，用于程序中导航，必须唯一
  name: 'News',
  // 对应的组件路径，这里通常作为布局组件
  
  component: '/index/index',
  // 路由元信息，用于菜单显示和权限控制
  meta: {
    // 菜单标题，对应国际化语言包中的 key
    title: 'menus.news.title',
    // 菜单图标，使用 Iconify 图标库
    icon: 'ri:chat-unread-line',
    // 是否缓存该页面，false 表示不缓存
    keepAlive: false
  },
  children: [
    {
      path: 'newspage', // 子路由路径
      name: 'NewsPage', // 子路由名称
      component: '/news/newspage', // 子路由组件
      meta: {
        title: 'menus.news.newspage', // 标题
        icon: 'ri:chat-unread-line', // 图标
        keepAlive: false, // 是否开启页面缓存
        fixedTab: false // 是否固定在标签页
      }
    }
  ]
}

