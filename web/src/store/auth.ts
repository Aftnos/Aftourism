import { defineStore } from 'pinia';
import { computed, ref } from 'vue';
import type { UserProfile } from '@/auth/rbac';
import { hasPermission } from '@/auth/rbac';
import { loginApi, profileApi } from '@/api/auth';
import { clearToken, getToken, setToken } from '@/auth/token';

interface LoginPayload {
  username: string;
  password: string;
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string>(getToken());
  const profile = ref<UserProfile | null>(null);
  const loading = ref(false);

  const permissions = computed(() => profile.value?.permissions || []);
  const isSuper = computed(() => !!profile.value?.isSuper);

  function allow(code?: string | string[]) {
    return hasPermission(profile.value, code);
  }

  async function login(payload: LoginPayload) {
    loading.value = true;
    try {
      const res = await loginApi(payload);
      setToken(res.token);
      token.value = res.token;
      await fetchProfile();
    } finally {
      loading.value = false;
    }
  }

  async function fetchProfile() {
    if (!token.value) return;
    profile.value = await profileApi();
  }

  function logout() {
    clearToken();
    token.value = '';
    profile.value = null;
  }

  return { token, profile, permissions, isSuper, loading, login, fetchProfile, logout, allow };
});
