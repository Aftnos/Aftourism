import axios, { AxiosHeaders } from 'axios';
import type { AxiosError, AxiosRequestConfig, AxiosResponse } from 'axios';
import { ElMessage } from 'element-plus';
import { getToken, clearToken } from '@/auth/token';
import { createTraceId } from '@/utils/trace';
import { useAppStore } from '@/store/app';
import { useAuthStore } from '@/store/auth';

interface ApiResult<T> {
  code?: number;
  msg?: string;
  data: T;
  reason?: string;
}

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE || '',
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
    const payload = response.data as ApiResult<any>;
    if (payload?.reason && ['malicious', 'abuse', 'policy'].includes(payload.reason)) {
      const appStore = useAppStore();
      appStore.markSafeClose(payload.reason);
      ElMessage.error('已关闭会话（安全原因）');
      throw new Error('会话被安全策略关闭');
    }
    if (typeof payload?.code !== 'number') {
      // 某些第三方接口非统一结构，直接返回
      return payload;
    }
    if (payload.code !== 1) {
      ElMessage.error(payload.msg || '请求失败');
      return Promise.reject(new Error(payload.msg || '请求失败'));
    }
    return payload.data;
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
    const backendMsg = (error.response?.data as any)?.msg || (error.response?.data as any)?.message;
    ElMessage.error(backendMsg || error.message || '请求失败');
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

export function del<T>(url: string, config?: AxiosRequestConfig) {
  return instance.delete<any, T>(url, config);
}

export default { get, post, put, del };
