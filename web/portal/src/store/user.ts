import { defineStore } from 'pinia';

// 中文注释：用户状态管理，模拟登录与资料维护
interface Profile {
  name: string;
  phone: string;
  email: string;
}

export const useUserStore = defineStore('user', {
  state: () => ({
    token: '',
    profile: {
      name: '游客',
      phone: '',
      email: ''
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
    login(account: string) {
      this.token = 'mock-token';
      this.profile.name = account;
    },
    logout() {
      this.token = '';
      this.profile = { name: '游客', phone: '', email: '' };
    },
    toggleFavorite(type: 'scenic' | 'venue' | 'activity', id: number) {
      const list = this.favorites[type];
      const index = list.indexOf(id);
      if (index > -1) {
        list.splice(index, 1);
      } else {
        list.push(id);
      }
    },
    updateProfile(payload: Partial<Profile>) {
      this.profile = { ...this.profile, ...payload };
    },
    addSubmission(id: number) {
      if (!this.submissions.includes(id)) {
        this.submissions.push(id);
      }
    }
  }
});
