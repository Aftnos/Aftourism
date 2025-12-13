import { defineStore } from 'pinia';
import { ElMessage } from 'element-plus';
import {
  addFavorite,
  fetchFavoritePage,
  fetchUserInfo,
  updateUserInfo,
  login as loginApi,
  removeFavorite,
  type UserInfo,
  type FavoriteItem
} from '@/services/portal';

// 中文注释：用户状态管理，完成登录、登出、收藏同步等接口对接
interface Profile {
  name: string;
  nickName?: string;
  phone?: string;
  email?: string;
  avatar?: string;
  gender?: string;
  remark?: string;
}

type FavType = 'scenic' | 'venue' | 'activity';

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('portal_token') || '',
    refreshToken: localStorage.getItem('portal_refresh_token') || '',
    profile: {
      name: localStorage.getItem('portal_user') || '游客',
      nickName: '',
      phone: '',
      email: '',
      avatar: '',
      gender: '',
      remark: ''
    } as Profile,
    favorites: {
      scenic: [] as number[],
      venue: [] as number[],
      activity: [] as number[]
    },
    submissions: [] as number[]
  }),
  getters: {
    isLogin: (state) => !!state.token
  },
  actions: {
    // 中文注释：调用登录接口并持久化 Token
    async login(account: string, password: string) {
      const data = await loginApi({ userName: account, password });
      this.token = data.token;
      this.refreshToken = data.refreshToken;
      localStorage.setItem('portal_token', data.token);
      localStorage.setItem('portal_refresh_token', data.refreshToken);
      await this.fetchProfile();
      ElMessage.success('登录成功');
    },
    // 中文注释：查询后端用户信息，填充展示名称
    async fetchProfile() {
      const info: UserInfo = await fetchUserInfo();
      this.profile = {
        name: info.userName || '用户',
        nickName: info.nickName,
        phone: info.phone,
        email: info.email,
        avatar: info.avatar,
        gender: info.gender,
        remark: info.remark
      };
      localStorage.setItem('portal_user', this.profile.name);
    },
    logout() {
      this.token = '';
      this.refreshToken = '';
      this.profile = { name: '游客', phone: '', email: '', nickName: '', avatar: '', gender: '', remark: '' };
      this.favorites = { scenic: [], venue: [], activity: [] };
      this.submissions = [];
      localStorage.removeItem('portal_token');
      localStorage.removeItem('portal_refresh_token');
      localStorage.removeItem('portal_user');
    },
    // 中文注释：收藏/取消收藏，调用后端接口并更新本地状态
    async toggleFavorite(type: FavType, id: number) {
      const list = this.favorites[type];
      if (list.includes(id)) {
        await removeFavorite(type, id);
        this.favorites[type] = list.filter((item) => item !== id);
      } else {
        await addFavorite(type, id);
        this.favorites[type].push(id);
      }
    },
    // 中文注释：刷新收藏列表，按类型分组
    async loadFavorites(type?: FavType) {
      const result = await fetchFavoritePage({ current: 1, size: 100, type });
      const scenic: number[] = [];
      const venue: number[] = [];
      const activity: number[] = [];
      result.list.forEach((item: FavoriteItem) => {
        if (item.targetType === 'SCENIC') {
          scenic.push(item.targetId);
        } else if (item.targetType === 'VENUE') {
          venue.push(item.targetId);
        } else if (item.targetType === 'ACTIVITY') {
          activity.push(item.targetId);
        }
      });
      this.favorites = { scenic, venue, activity };
    },
    // 中文注释：更新用户本地资料
    async updateProfile(payload: Partial<Profile>) {
      const info: Partial<UserInfo> = {
          nickName: payload.nickName,
          phone: payload.phone,
          email: payload.email,
          avatar: payload.avatar,
          // 性别枚举与后端保持一致：男/女/未知
          gender: payload.gender || '未知',
          remark: payload.remark
      };
      await updateUserInfo(info);
      await this.fetchProfile();
    },
    // 中文注释：记录提交过的活动编号，便于个人中心展示
    addSubmission(id: number) {
      if (!this.submissions.includes(id)) {
        this.submissions.push(id);
      }
    }
  }
});
