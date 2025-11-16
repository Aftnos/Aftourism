import type { RouteRecordRaw } from 'vue-router';
import BaseLayout from '@/layouts/BaseLayout.vue';

export const protectedRoutes: RouteRecordRaw[] = [
  {
    path: '/',
    component: BaseLayout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/pages/dashboard/Index.vue'),
        meta: { title: '总览', icon: 'Odometer' }
      },
      {
        path: 'ai',
        name: 'Ai',
        meta: { title: 'AI 管理', icon: 'Cpu' },
        children: [
          {
            path: 'chat',
            name: 'AiChat',
            component: () => import('@/pages/ai/Chat.vue'),
            meta: { title: '对话', icon: 'ChatDotSquare' }
          },
          {
            path: 'apply',
            name: 'AiApply',
            component: () => import('@/pages/ai/Apply.vue'),
            meta: { title: '申请', icon: 'Document' }
          }
        ]
      },
      {
        path: 'admin',
        name: 'Admin',
        meta: { title: '管理员管理', icon: 'UserFilled', permission: 'ADMIN:READ' },
        children: [
          {
            path: 'managers',
            name: 'AdminManagers',
            component: () => import('@/pages/admin/AdminList.vue'),
            meta: { title: '管理员列表', permission: 'ADMIN:READ' }
          },
          {
            path: 'roles',
            name: 'AdminRoles',
            component: () => import('@/pages/admin/Roles.vue'),
            meta: { title: '角色权限', permission: 'ROLE:READ' }
          }
        ]
      },
      {
        path: 'users',
        name: 'Users',
        component: () => import('@/pages/user/UserList.vue'),
        meta: { title: '普通用户', icon: 'User', permission: 'USER:READ' }
      },
      {
        path: 'news',
        name: 'News',
        component: () => import('@/pages/news/NewsPage.vue'),
        meta: { title: '新闻', icon: 'Reading', permission: 'NEWS:READ' }
      },
      {
        path: 'notice',
        name: 'Notice',
        component: () => import('@/pages/notice/NoticePage.vue'),
        meta: { title: '通知', icon: 'Bell', permission: 'NOTICE:READ' }
      },
      {
        path: 'scenic',
        name: 'Scenic',
        component: () => import('@/pages/scenic/ScenicPage.vue'),
        meta: { title: '景区', icon: 'Location', permission: 'SCENIC:READ' }
      },
      {
        path: 'venue',
        name: 'Venue',
        component: () => import('@/pages/venue/VenuePage.vue'),
        meta: { title: '场馆', icon: 'OfficeBuilding', permission: 'VENUE:READ' }
      },
      {
        path: 'activity',
        name: 'Activity',
        component: () => import('@/pages/activity/ActivityPage.vue'),
        meta: { title: '活动审核', icon: 'List', permission: 'ACTIVITY:READ' }
      },
      {
        path: 'files',
        name: 'File',
        component: () => import('@/pages/file/FilePage.vue'),
        meta: { title: '文件上传', icon: 'UploadFilled', permission: 'FILE:WRITE' }
      },
      {
        path: 'monitor',
        name: 'Monitor',
        component: () => import('@/pages/monitor/MonitorPage.vue'),
        meta: { title: '系统监控', icon: 'DataLine', permission: 'MONITOR:READ' }
      },
      {
        path: 'logs',
        name: 'Logs',
        component: () => import('@/pages/log/OperationLog.vue'),
        meta: { title: '操作日志', icon: 'Tickets', permission: 'LOG:READ' }
      }
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
