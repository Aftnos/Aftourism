import request from './request';

export interface LoginResponse {
  principalId: number;
  userId: number;
  username: string;
  nickname?: string;
  realName?: string;
  phone?: string;
  email?: string;
  superAdmin: boolean;
  roles: string[];
  permissions: string[];
  token: string;
  expiresAt: string;
}

export function loginApi(data: { username: string; password: string }) {
  return request.post<LoginResponse>('/admin/auth/login', data);
}
