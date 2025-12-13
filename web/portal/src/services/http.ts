import axios from 'axios';
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
    const resp = response.data;
    if (resp && typeof resp === 'object' && 'code' in resp) {
      if (resp.code === 200) {
        return resp.data;
      }
      ElMessage.error(resp.msg || '请求失败');
      return Promise.reject(new Error(resp.msg || '接口返回错误'));
    }
    return resp;
  },
  (error) => {
    ElMessage.error(error.response?.data?.msg || error.message || '网络异常');
    return Promise.reject(error);
  }
);

export default http;

export interface PageResult<T> {
  total: number;
  list: T[];
  pageNum?: number;
  pageSize?: number;
}
