import { defineStore } from 'pinia';
import { computed, ref } from 'vue';
import type { UserProfile } from '@/auth/rbac';
import { hasPermission } from '@/auth/rbac';
import { loginApi, type LoginResponse } from '@/api/auth';
import { clearToken, clearStoredProfile, getStoredProfile, getToken, setStoredProfile, setToken } from '@/auth/token';

interface LoginPayload {
  username: string;
  password: string;
}

function convertProfile(res: LoginResponse): UserProfile {
  return {
    principalId: res.principalId,
    principalType: res.principalType,
    userId: res.userId ?? res.principalId,
    username: res.username,
    nickname: res.nickname || res.realName || res.username,
    avatar: res.avatar,
    realName: res.realName,
    phone: res.phone,
    email: res.email,
    status: res.status,
    superAdmin: !!res.superAdmin,
    roles: res.roles || [],
    permissions: res.permissions || []
  };
}

export const useAuthStore = defineStore('auth', () => {
  const token = ref<string>(getToken());
  const profile = ref<UserProfile | null>(getStoredProfile());
  const loading = ref(false);

  const permissions = computed(() => profile.value?.permissions || []);
  const isSuper = computed(() => !!profile.value?.superAdmin);

  function allow(code?: string | string[]) {
    return hasPermission(profile.value, code);
  }

  async function login(payload: LoginPayload) {
    loading.value = true;
    try {
      const res = await loginApi(payload);
      setToken(res.token);
      token.value = res.token;
      profile.value = convertProfile(res);
      setStoredProfile(profile.value);
    } finally {
      loading.value = false;
    }
  }

  function ensureProfileFromCache() {
    if (!profile.value) {
      profile.value = getStoredProfile();
    }
  }

  function logout() {
    clearToken();
    clearStoredProfile();
    token.value = '';
    profile.value = null;
  }

  return { token, profile, permissions, isSuper, loading, login, logout, allow, ensureProfileFromCache };
});
