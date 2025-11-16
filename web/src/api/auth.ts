import request from './request';
import type { UserProfile } from '@/auth/rbac';

interface LoginResult {
  token: string;
  expireAt: string;
}

export function loginApi(data: { username: string; password: string }) {
  return request.post<LoginResult>('/auth/login', data);
}

export function profileApi() {
  return request.get<UserProfile>('/auth/profile');
}
