import type { RouteRecordRaw } from 'vue-router';
import BaseLayout from '@/layouts/BaseLayout.vue';

const RBAC_MENU_PERMISSIONS = ['ADMIN_ACCOUNT:READ', 'PORTAL_USER:READ', 'ROLE_ACCESS:READ'];
const AI_USE_PERMISSION = 'AI_ASSIST:USE';
const AI_ADMIN_PERMISSION = 'AI_ADMIN:MANAGE';

export const protectedRoutes: RouteRecordRaw[] = [
  {
    path: '/',
    component: BaseLayout,
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/view/dashboard/Index.vue'),
        meta: { title: '总览', icon: 'Odometer' }
      },
      {
        path: '/ai',
        name: 'Ai',
        meta: { title: 'AI', icon: 'Cpu' },
        children: [
          {
            path: '/ai/chat',
            name: 'AiChat',
            component: () => import('@/view/ai/Chat.vue'),
            meta: { title: 'AI 功能', icon: 'ChatDotSquare', permission: AI_USE_PERMISSION }
          },
          {
            path: '/ai/admin',
            name: 'AiAdmin',
            component: () => import('@/view/ai/Admin.vue'),
            meta: { title: 'AI 管理', icon: 'Setting', permission: AI_ADMIN_PERMISSION }
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
            component: () => import('@/view/admin/AdminList.vue'),
            meta: { title: '管理员列表', permission: 'ADMIN_ACCOUNT:READ' }
          },
          {
            path: '/rbac/roles',
            name: 'AdminRoles',
            component: () => import('@/view/admin/Roles.vue'),
            meta: { title: '角色权限', permission: 'ROLE_ACCESS:READ' }
          }
        ]
      },
      {
        path: '/portal-users',
        name: 'PortalUsers',
        component: () => import('@/view/user/UserList.vue'),
        meta: { title: '门户用户', icon: 'User', permission: 'PORTAL_USER:READ' }
      },
      {
        path: '/news',
        name: 'News',
        component: () => import('@/view/news/NewsPage.vue'),
        meta: { title: '新闻', icon: 'Reading', permission: 'NEWS:READ' }
      },
      {
        path: '/notice',
        name: 'Notice',
        component: () => import('@/view/notice/NoticePage.vue'),
        meta: { title: '通知', icon: 'Bell', permission: 'NOTICE:READ' }
      },
      {
        path: '/scenic',
        name: 'Scenic',
        component: () => import('@/view/scenic/ScenicPage.vue'),
        meta: { title: '景区', icon: 'Location', permission: 'SCENIC:READ' }
      },
      {
        path: '/venue',
        name: 'Venue',
        component: () => import('@/view/venue/VenuePage.vue'),
        meta: { title: '场馆', icon: 'OfficeBuilding', permission: 'VENUE:READ' }
      },
      {
        path: '/activity',
        name: 'ActivityReview',
        component: () => import('@/view/activity/ActivityPage.vue'),
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
        path: '/activity/manage',
        name: 'ActivityManage',
        component: () => import('@/view/activity/ActivityManagePage.vue'),
        meta: {
          title: '活动管理',
          icon: 'Collection',
          permission: 'ACTIVITY_MANAGE:READ'
        }
      },
      {
        path: '/files',
        name: 'File',
        component: () => import('@/view/file/FilePage.vue'),
        meta: { title: '文件上传', icon: 'UploadFilled', permission: 'FILE:UPLOAD' }
      },
      {
        path: '/recycle',
        name: 'Recycle',
        component: () => import('@/view/recycle/RecyclePage.vue'),
        meta: { title: '回收站', icon: 'Delete', permission: 'RECYCLE_BIN:READ' }
      },
      {
        path: '/monitor',
        name: 'Monitor',
        component: () => import('@/view/monitor/PerformanceMonitor.vue'),
        meta: { title: '系统监控', icon: 'DataLine', permission: 'MONITOR:SYSTEM_METRIC' }
      },
      // 后端暂未暴露操作日志查询接口，菜单暂不呈现
    ]
  }
];

export const publicRoutes: RouteRecordRaw[] = [
  {
    path: '/login',
    component: () => import('@/view/auth/Login.vue')
  },
  {
    path: '/403',
    component: () => import('@/view/common/Forbidden.vue')
  },
  {
    path: '/:pathMatch(.*)*',
    component: () => import('@/view/common/NotFound.vue')
  }
];
