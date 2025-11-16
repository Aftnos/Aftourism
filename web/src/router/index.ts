import { createRouter, createWebHistory } from 'vue-router';
import { ElMessage } from 'element-plus';
import { publicRoutes, protectedRoutes } from './routes';
import { useAuthStore } from '@/store/auth';

const router = createRouter({
  history: createWebHistory(),
  routes: [...publicRoutes, ...protectedRoutes]
});

const whiteList = ['/login'];

router.beforeEach(async (to, from, next) => {
  const auth = useAuthStore();
  if (!auth.token && !whiteList.includes(to.path)) {
    return next('/login');
  }
  if (auth.token && !auth.profile) {
    await auth.fetchProfile();
  }
  if (to.path === '/login' && auth.token) {
    return next('/dashboard');
  }
  const required = to.meta?.permission as string | string[] | undefined;
  if (required && !auth.allow(required)) {
    ElMessage.warning('当前账号无权限访问该页面');
    return next('/403');
  }
  next();
});

export default router;
