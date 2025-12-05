import request from './request';

export type PrincipalType = 'ADMIN' | 'SUPER_ADMIN' | 'PORTAL_USER';

export interface LoginResponse {
  principalId: number;
  principalType: PrincipalType;
  userId: number;
  username: string;
  nickname?: string;
  realName?: string;
  avatar?: string;
  phone?: string;
  email?: string;
  status?: number;
  superAdmin: boolean;
  roles: string[];
  permissions: string[];
  token: string;
  expiresAt: string;
}

export function loginApi(data: { username: string; password: string }) {
  return request.post<LoginResponse>('/admin/auth/login', data);
}
