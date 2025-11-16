import axios, { AxiosHeaders } from 'axios';
import type { AxiosError, AxiosRequestConfig, AxiosResponse } from 'axios';
import { ElMessage } from 'element-plus';
import { getToken, clearToken } from '@/auth/token';
import { createTraceId } from '@/utils/trace';
import { useAppStore } from '@/store/app';
import { useAuthStore } from '@/store/auth';

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE || '/api',
  timeout: 20000
});

instance.interceptors.request.use((config) => {
  const headers = (config.headers instanceof AxiosHeaders ? config.headers : new AxiosHeaders(config.headers)) as AxiosHeaders;
  const token = getToken();
  if (token) {
    headers.set('Authorization', `Bearer ${token}`);
  }
  headers.set('x-trace-id', createTraceId('fe'));
  config.headers = headers;
  return config;
});

instance.interceptors.response.use(
  (response: AxiosResponse) => {
    const data = response.data;
    if (data?.reason && ['malicious', 'abuse', 'policy'].includes(data.reason)) {
      const appStore = useAppStore();
      appStore.markSafeClose(data.reason);
      ElMessage.error('已关闭会话（安全原因）');
      throw new Error('会话被安全策略关闭');
    }
    return data;
  },
  (error: AxiosError) => {
    const status = error.response?.status;
    if (status === 401) {
      const auth = useAuthStore();
      clearToken();
      auth.logout();
    }
    if (status === 403) {
      ElMessage.error('无权限访问该功能');
    }
    if ((error.response?.data as any)?.reason) {
      const appStore = useAppStore();
      appStore.markSafeClose((error.response?.data as any).reason);
    }
    ElMessage.error((error.response?.data as any)?.message || error.message || '请求失败');
    return Promise.reject(error);
  }
);

export function get<T>(url: string, config?: AxiosRequestConfig) {
  return instance.get<any, T>(url, config);
}

export function post<T>(url: string, data?: any, config?: AxiosRequestConfig) {
  return instance.post<any, T>(url, data, config);
}

export function put<T>(url: string, data?: any, config?: AxiosRequestConfig) {
  return instance.put<any, T>(url, data, config);
}

export default { get, post, put };
