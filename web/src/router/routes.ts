import type { RouteRecordRaw } from 'vue-router';
import BaseLayout from '@/layouts/BaseLayout.vue';

const RBAC_MENU_PERMISSIONS = ['ADMIN_ACCOUNT:READ', 'PORTAL_USER:READ', 'ROLE_ACCESS:READ'];
const AI_CONSOLE_PERMISSION = 'AI_ASSIST:USE';

export const protectedRoutes: RouteRecordRaw[] = [
  {
    path: '/',
    component: BaseLayout,
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/pages/dashboard/Index.vue'),
        meta: { title: '总览', icon: 'Odometer' }
      },
      {
        path: '/ai',
        name: 'Ai',
        meta: { title: 'AI 管理', icon: 'Cpu', permission: AI_CONSOLE_PERMISSION },
        children: [
          {
            path: '/ai/chat',
            name: 'AiChat',
            component: () => import('@/pages/ai/Chat.vue'),
            meta: { title: '对话', icon: 'ChatDotSquare', permission: AI_CONSOLE_PERMISSION }
          },
          {
            path: '/ai/apply',
            name: 'AiApply',
            component: () => import('@/pages/ai/Apply.vue'),
            meta: { title: '工具授权', icon: 'Document', permission: AI_CONSOLE_PERMISSION }
          }
        ]
      },
      {
        path: '/rbac',
        name: 'Rbac',
        meta: { title: '管理员', icon: 'UserFilled', permission: RBAC_MENU_PERMISSIONS },
        children: [
          {
            path: '/rbac/admins',
            name: 'AdminManagers',
            component: () => import('@/pages/admin/AdminList.vue'),
            meta: { title: '管理员列表', permission: 'ADMIN_ACCOUNT:READ' }
          },
          {
            path: '/rbac/roles',
            name: 'AdminRoles',
            component: () => import('@/pages/admin/Roles.vue'),
            meta: { title: '角色权限', permission: 'ROLE_ACCESS:READ' }
          }
        ]
      },
      {
        path: '/portal-users',
        name: 'PortalUsers',
        component: () => import('@/pages/user/UserList.vue'),
        meta: { title: '门户用户', icon: 'User', permission: 'PORTAL_USER:READ' }
      },
      {
        path: '/news',
        name: 'News',
        component: () => import('@/pages/news/NewsPage.vue'),
        meta: { title: '新闻', icon: 'Reading', permission: 'NEWS:READ' }
      },
      {
        path: '/notice',
        name: 'Notice',
        component: () => import('@/pages/notice/NoticePage.vue'),
        meta: { title: '通知', icon: 'Bell', permission: 'NOTICE:READ' }
      },
      {
        path: '/scenic',
        name: 'Scenic',
        component: () => import('@/pages/scenic/ScenicPage.vue'),
        meta: { title: '景区', icon: 'Location', permission: 'SCENIC:READ' }
      },
      {
        path: '/venue',
        name: 'Venue',
        component: () => import('@/pages/venue/VenuePage.vue'),
        meta: { title: '场馆', icon: 'OfficeBuilding', permission: 'VENUE:READ' }
      },
      {
        path: '/activity',
        name: 'Activity',
        component: () => import('@/pages/activity/ActivityPage.vue'),
        meta: {
          title: '活动审核',
          icon: 'List',
          permission: ['ACTIVITY_REVIEW:APPROVE', 'ACTIVITY_REVIEW:REJECT', 'ACTIVITY_REVIEW:ONLINE', 'ACTIVITY_REVIEW:OFFLINE']
        }
      },
      {
        path: '/files',
        name: 'File',
        component: () => import('@/pages/file/FilePage.vue'),
        meta: { title: '文件上传', icon: 'UploadFilled', permission: 'FILE:UPLOAD' }
      },
      {
        path: '/recycle',
        name: 'Recycle',
        component: () => import('@/pages/recycle/RecyclePage.vue'),
        meta: { title: '回收站', icon: 'Delete', permission: 'RECYCLE_BIN:READ' }
      },
      {
        path: '/monitor',
        name: 'Monitor',
        component: () => import('@/pages/monitor/PerformanceMonitor.vue'),
        meta: { title: '系统监控', icon: 'DataLine', permission: 'MONITOR:SYSTEM_METRIC' }
      },
      // 后端暂未暴露操作日志查询接口，菜单暂不呈现
    ]
  }
];

export const publicRoutes: RouteRecordRaw[] = [
  {
    path: '/login',
    component: () => import('@/pages/auth/Login.vue')
  },
  {
    path: '/403',
    component: () => import('@/pages/common/Forbidden.vue')
  },
  {
    path: '/:pathMatch(.*)*',
    component: () => import('@/pages/common/NotFound.vue')
  }
];
