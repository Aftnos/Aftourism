import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import Home from '@/views/home/Home.vue';
import NewsList from '@/views/info/NewsList.vue';
import NewsDetail from '@/views/info/NewsDetail.vue';
import NoticeList from '@/views/info/NoticeList.vue';
import NoticeDetail from '@/views/info/NoticeDetail.vue';
import ScenicList from '@/views/scenic/ScenicList.vue';
import ScenicDetail from '@/views/scenic/ScenicDetail.vue';
import ScenicMap from '@/views/scenic/ScenicMap.vue';
import VenueList from '@/views/venue/VenueList.vue';
import VenueDetail from '@/views/venue/VenueDetail.vue';
import VenueMap from '@/views/venue/VenueMap.vue';
import ActivityList from '@/views/activity/ActivityList.vue';
import ActivityDetail from '@/views/activity/ActivityDetail.vue';
import ActivityApply from '@/views/activity/ActivityApply.vue';
import Login from '@/views/auth/Login.vue';
import Register from '@/views/auth/Register.vue';
import Favorites from '@/views/profile/Favorites.vue';
import MyActivities from '@/views/profile/MyActivities.vue';
import Profile from '@/views/profile/Profile.vue';
import FeedbackList from '@/views/feedback/FeedbackList.vue';
import FeedbackDetail from '@/views/feedback/FeedbackDetail.vue';
import ExchangeList from '@/views/exchange/ExchangeList.vue';
import ExchangeDetail from '@/views/exchange/ExchangeDetail.vue';
import ExchangePublish from '@/views/exchange/ExchangePublish.vue';
import Notifications from '@/views/profile/Notifications.vue';
import UserHome from '@/views/user/UserHome.vue';
import { useUiStore } from '@/store/ui';

// 定义路由配置
const routes: RouteRecordRaw[] = [
  { path: '/', component: Home, name: 'home' },
  { path: '/news', component: NewsList, name: 'news' },
  { path: '/news/:id', component: NewsDetail, name: 'newsDetail' },
  { path: '/notices', component: NoticeList, name: 'notices' },
  { path: '/notices/:id', component: NoticeDetail, name: 'noticeDetail' },
  { path: '/scenic', component: ScenicList, name: 'scenic' },
  { path: '/scenic/map', component: ScenicMap, name: 'scenicMap' },
  { path: '/scenic/:id', component: ScenicDetail, name: 'scenicDetail' },
  { path: '/venues', component: VenueList, name: 'venues' },
  { path: '/venues/map', component: VenueMap, name: 'venueMap' },
  { path: '/venues/:id', component: VenueDetail, name: 'venueDetail' },
  { path: '/activities', component: ActivityList, name: 'activities' },
  { path: '/activities/:id', component: ActivityDetail, name: 'activityDetail' },
  { path: '/activities/apply', component: ActivityApply, name: 'activityApply' },
  { path: '/login', component: Login, name: 'login' },
  { path: '/register', component: Register, name: 'register' },
  { path: '/profile/favorites', component: Favorites, name: 'favorites' },
  { path: '/profile/activities', component: MyActivities, name: 'myActivities' },
  { path: '/profile/info', component: Profile, name: 'profile' },
  { path: '/profile/notifications', component: Notifications, name: 'notifications' },
  { path: '/feedback', component: FeedbackList, name: 'feedbackList' },
  { path: '/feedback/:id', component: FeedbackDetail, name: 'feedbackDetail' },
  { path: '/exchange', component: ExchangeList, name: 'exchangeList' },
  { path: '/exchange/publish', component: ExchangePublish, name: 'exchangePublish' },
  { path: '/exchange/:id', component: ExchangeDetail, name: 'exchangeDetail' },
  { path: '/user/:id', component: UserHome, name: 'userHome' }
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
