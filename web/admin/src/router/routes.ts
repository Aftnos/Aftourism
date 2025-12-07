// 导入 Vue Router 的类型定义，用于 TypeScript 类型检查
import type { RouteRecordRaw } from 'vue-router';
// 导入基础布局组件，作为大部分页面的外层容器
import BaseLayout from '@/layouts/BaseLayout.vue';

// 定义权限常量，用于控制菜单访问权限
// RBAC 菜单权限集合：包含管理员账户、门户用户和角色权限查看权限
const RBAC_MENU_PERMISSIONS = ['ADMIN_ACCOUNT:READ', 'PORTAL_USER:READ', 'ROLE_ACCESS:READ'];
// AI 使用权限：普通用户使用 AI 功能的权限
const AI_USE_PERMISSION = 'AI_ASSIST:USE';
// AI 管理权限：管理员管理 AI 相关设置的权限
const AI_ADMIN_PERMISSION = 'AI_ADMIN:MANAGE';

// 受保护的路由配置：需要登录后才能访问的页面路由
export const protectedRoutes: RouteRecordRaw[] = [
  {
    // 根路径，所有子路由都在此路径下
    path: '/',
    // 使用基础布局组件包裹子页面
    component: BaseLayout,
    // 默认重定向到仪表盘页面
    redirect: '/dashboard',
    // 子路由配置
    children: [
      {
        // 仪表盘页面路径
        path: '/dashboard',
        // 路由名称，在代码中可以通过该名称进行跳转
        name: 'Dashboard',
        // 页面组件，使用动态导入方式加载
        component: () => import('@/view/dashboard/Index.vue'),
        // 元信息配置，包含标题和图标
        meta: { title: '总览', icon: 'Odometer' }
      },
      {
        // AI 功能父级路由
        path: '/ai',
        name: 'Ai',
        // 父级路由的元信息，包含整体标题和图标
        meta: { title: 'AI', icon: 'Cpu' },
        // AI 功能的子路由
        children: [
          {
            // AI 聊天页面路径
            path: '/ai/chat',
            name: 'AiChat',
            // 页面组件
            component: () => import('@/view/ai/Chat.vue'),
            // 页面元信息，包含标题、图标和访问权限
            meta: { title: 'AI 功能', icon: 'ChatDotSquare', permission: AI_USE_PERMISSION }
          },
          {
            // AI 管理页面路径
            path: '/ai/admin',
            name: 'AiAdmin',
            // 页面组件
            component: () => import('@/view/ai/Admin.vue'),
            // 页面元信息，包含标题、图标和访问权限
            meta: { title: 'AI 管理', icon: 'Setting', permission: AI_ADMIN_PERMISSION }
          }
        ]
      },
      {
        // 权限管理系统（RBAC）父级路由
        path: '/rbac',
        name: 'Rbac',
        // 元信息配置，包含标题、图标和所需权限
        meta: { title: '管理员', icon: 'UserFilled', permission: RBAC_MENU_PERMISSIONS },
        // 权限管理子路由
        children: [
          {
            // 管理员列表页面路径
            path: '/rbac/admins',
            name: 'AdminManagers',
            // 页面组件
            component: () => import('@/view/admin/AdminList.vue'),
            // 页面元信息，包含标题和所需权限
            meta: { title: '管理员列表', permission: 'ADMIN_ACCOUNT:READ' }
          },
          {
            // 角色权限页面路径
            path: '/rbac/roles',
            name: 'AdminRoles',
            // 页面组件
            component: () => import('@/view/admin/Roles.vue'),
            // 页面元信息，包含标题和所需权限
            meta: { title: '角色权限', permission: 'ROLE_ACCESS:READ' }
          }
        ]
      },
      {
        // 门户用户管理页面路由
        path: '/portal-users',
        name: 'PortalUsers',
        // 页面组件
        component: () => import('@/view/user/UserList.vue'),
        // 页面元信息，包含标题、图标和所需权限
        meta: { title: '门户用户', icon: 'User', permission: 'PORTAL_USER:READ' }
      },
      {
        // 新闻管理页面路由
        path: '/news',
        name: 'News',
        // 页面组件
        component: () => import('@/view/news/NewsPage.vue'),
        // 页面元信息，包含标题、图标和所需权限
        meta: { title: '新闻', icon: 'Reading', permission: 'NEWS:READ' }
      },
      {
        // 通知管理页面路由
        path: '/notice',
        name: 'Notice',
        // 页面组件
        component: () => import('@/view/notice/NoticePage.vue'),
        // 页面元信息，包含标题、图标和所需权限
        meta: { title: '通知', icon: 'Bell', permission: 'NOTICE:READ' }
      },
      {
        // 景区管理页面路由
        path: '/scenic',
        name: 'Scenic',
        // 页面组件
        component: () => import('@/view/scenic/ScenicPage.vue'),
        // 页面元信息，包含标题、图标和所需权限
        meta: { title: '景区', icon: 'Location', permission: 'SCENIC:READ' }
      },
      {
        // 场馆管理页面路由
        path: '/venue',
        name: 'Venue',
        // 页面组件
        component: () => import('@/view/venue/VenuePage.vue'),
        // 页面元信息，包含标题、图标和所需权限
        meta: { title: '场馆', icon: 'OfficeBuilding', permission: 'VENUE:READ' }
      },
      {
        // 活动审核页面路由
        path: '/activity',
        name: 'ActivityReview',
        // 页面组件
        component: () => import('@/view/activity/ActivityPage.vue'),
        // 页面元信息，包含标题、图标和所需权限
        meta: {
          title: '活动审核',
          icon: 'List',
          permission: [
            'ACTIVITY_REVIEW:APPROVE',
            'ACTIVITY_REVIEW:REJECT'
          ]
        }
      },
      {
        // 活动管理页面路由
        path: '/activity/manage',
        name: 'ActivityManage',
        // 页面组件
        component: () => import('@/view/activity/ActivityManagePage.vue'),
        // 页面元信息，包含标题、图标和所需权限
        meta: {
          title: '活动管理',
          icon: 'Collection',
          permission: 'ACTIVITY_MANAGE:READ'
        }
      },
      {
        // 文件上传页面路由
        path: '/files',
        name: 'File',
        // 页面组件
        component: () => import('@/view/file/FilePage.vue'),
        // 页面元信息，包含标题、图标和所需权限
        meta: { title: '文件上传', icon: 'UploadFilled', permission: 'FILE:UPLOAD' }
      },
      {
        // 回收站页面路由
        path: '/recycle',
        name: 'Recycle',
        // 页面组件
        component: () => import('@/view/recycle/RecyclePage.vue'),
        // 页面元信息，包含标题、图标和所需权限
        meta: { title: '回收站', icon: 'Delete', permission: 'RECYCLE_BIN:READ' }
      },
      {
        // 系统监控页面路由
        path: '/monitor',
        name: 'Monitor',
        // 页面组件
        component: () => import('@/view/monitor/PerformanceMonitor.vue'),
        // 页面元信息，包含标题、图标和所需权限
        meta: { title: '系统监控', icon: 'DataLine', permission: 'MONITOR:SYSTEM_METRIC' }
      },
      // 后端暂未暴露操作日志查询接口，菜单暂不呈现
    ]
  }
];

// 公共路由配置：无需登录即可访问的页面路由
export const publicRoutes: RouteRecordRaw[] = [
  {
    // 登录页面路径
    path: '/login',
    // 页面组件
    component: () => import('@/view/auth/Login.vue')
  },
  {
    // 403 无权限访问页面路径
    path: '/403',
    // 页面组件
    component: () => import('@/view/common/Forbidden.vue')
  },
  {
    // 404 页面未找到路由：匹配所有未定义的路径
    path: '/:pathMatch(.*)*',
    // 页面组件
    component: () => import('@/view/common/NotFound.vue')
  }
];