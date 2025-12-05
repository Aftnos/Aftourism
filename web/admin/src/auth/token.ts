import type { UserProfile } from '@/auth/rbac';

const TOKEN_KEY = 'aftourism_admin_token';
const PROFILE_KEY = 'aftourism_admin_profile';

// 读取 token
export function getToken() {
  return window.localStorage.getItem(TOKEN_KEY) || '';
}

// 持久化 token
export function setToken(token: string) {
  window.localStorage.setItem(TOKEN_KEY, token);
}

// 清理 token
export function clearToken() {
  window.localStorage.removeItem(TOKEN_KEY);
}

// 读取本地缓存的用户资料
export function getStoredProfile(): UserProfile | null {
  const raw = window.localStorage.getItem(PROFILE_KEY);
  if (!raw) return null;
  try {
    return JSON.parse(raw) as UserProfile;
  } catch {
    return null;
  }
}

// 写入用户资料
export function setStoredProfile(profile: UserProfile) {
  window.localStorage.setItem(PROFILE_KEY, JSON.stringify(profile));
}

// 清理资料缓存
export function clearStoredProfile() {
  window.localStorage.removeItem(PROFILE_KEY);
}
