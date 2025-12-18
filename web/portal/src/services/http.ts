import axios, { type AxiosRequestConfig } from 'axios';
import { ElMessage } from 'element-plus';

// 中文注释：统一的 HTTP 客户端，负责携带 Token、处理基础错误提示
const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '',
  timeout: 15000
});

http.interceptors.request.use((config) => {
  // 中文注释：从本地读取令牌，避免循环依赖 pinia store
  const token = localStorage.getItem('portal_token');
  if (token) {
    config.headers = config.headers || {};
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

http.interceptors.response.use(
  (response) => {
    const resp = response.data as ApiResponse<unknown> | unknown;
    if (resp && typeof resp === 'object' && 'code' in (resp as ApiResponse<unknown>)) {
      const r = resp as ApiResponse<unknown>;
      if (r.code === 200) {
        return r.data;
      }
      const err = new ApiError(r.msg || '接口返回错误');
      err.code = r.code;
      err.payload = r;
      ElMessage.error(r.msg || '请求失败');
      return Promise.reject(err);
    }
    return resp;
  },
  (error) => {
    const status = error.response?.status;
    const message = error.response?.data?.msg || error.message || '网络异常';
    
    if (status === 401) {
      localStorage.removeItem('portal_token');
      localStorage.removeItem('portal_refresh_token');
      localStorage.removeItem('portal_user');
      localStorage.removeItem('portal_profile');
      ElMessage.error('登录已过期，请重新登录');
      
      if (!window.location.pathname.includes('/login')) {
        setTimeout(() => {
          window.location.href = '/login';
        }, 1500);
      }
    } else {
      ElMessage.error(message);
    }

    const err = new ApiError(message);
    err.code = status;
    err.payload = error.response?.data;
    return Promise.reject(err);
  }
);

export default http;

export interface PageResult<T> {
  total: number;
  list: T[];
  pageNum?: number;
  pageSize?: number;
}

export interface ApiResponse<T> {
  code: number;
  msg?: string;
  data: T;
}

export class ApiError extends Error {
  code?: number;
  payload?: unknown;
}

export const get = async <T>(url: string, config?: AxiosRequestConfig) => {
  return (await http.get<T, T>(url, config)) as T;
};
export const post = async <T>(url: string, data?: unknown, config?: AxiosRequestConfig) => {
  return (await http.post<T, T>(url, data, config)) as T;
};
export const put = async <T>(url: string, data?: unknown, config?: AxiosRequestConfig) => {
  return (await http.put<T, T>(url, data, config)) as T;
};
export const del = async <T>(url: string, config?: AxiosRequestConfig) => {
  return (await http.delete<T, T>(url, config)) as T;
};
