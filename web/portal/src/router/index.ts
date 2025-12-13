import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import Home from '@/views/home/Home.vue';
import NewsList from '@/views/info/NewsList.vue';
import NewsDetail from '@/views/info/NewsDetail.vue';
import NoticeList from '@/views/info/NoticeList.vue';
import NoticeDetail from '@/views/info/NoticeDetail.vue';
import ScenicList from '@/views/scenic/ScenicList.vue';
import ScenicDetail from '@/views/scenic/ScenicDetail.vue';
import VenueList from '@/views/venue/VenueList.vue';
import VenueDetail from '@/views/venue/VenueDetail.vue';
import ActivityList from '@/views/activity/ActivityList.vue';
import ActivityDetail from '@/views/activity/ActivityDetail.vue';
import ActivityApply from '@/views/activity/ActivityApply.vue';
import Login from '@/views/auth/Login.vue';
import Register from '@/views/auth/Register.vue';
import Favorites from '@/views/profile/Favorites.vue';
import MyActivities from '@/views/profile/MyActivities.vue';
import Profile from '@/views/profile/Profile.vue';
import { useUiStore } from '@/store/ui';

// 中文注释：定义路由配置
const routes: RouteRecordRaw[] = [
  { path: '/', component: Home, name: 'home' },
  { path: '/news', component: NewsList, name: 'news' },
  { path: '/news/:id', component: NewsDetail, name: 'newsDetail' },
  { path: '/notices', component: NoticeList, name: 'notices' },
  { path: '/notices/:id', component: NoticeDetail, name: 'noticeDetail' },
  { path: '/scenic', component: ScenicList, name: 'scenic' },
  { path: '/scenic/:id', component: ScenicDetail, name: 'scenicDetail' },
  { path: '/venues', component: VenueList, name: 'venues' },
  { path: '/venues/:id', component: VenueDetail, name: 'venueDetail' },
  { path: '/activities', component: ActivityList, name: 'activities' },
  { path: '/activities/:id', component: ActivityDetail, name: 'activityDetail' },
  { path: '/activities/apply', component: ActivityApply, name: 'activityApply' },
  { path: '/login', component: Login, name: 'login' },
  { path: '/register', component: Register, name: 'register' },
  { path: '/profile/favorites', component: Favorites, name: 'favorites' },
  { path: '/profile/activities', component: MyActivities, name: 'myActivities' },
  { path: '/profile/info', component: Profile, name: 'profile' }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// 路由进度条与错误处理
router.beforeEach((_to, _from, next) => {
  const ui = useUiStore();
  ui.startNavigate();
  next();
});
router.afterEach(() => {
  const ui = useUiStore();
  ui.endNavigate();
});
router.onError(() => {
  const ui = useUiStore();
  ui.endNavigate();
});

export default router;
