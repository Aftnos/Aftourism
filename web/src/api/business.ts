import request from './request';

interface PageResult<T> {
  records: T[];
  total: number;
}

export function fetchNews(params: Record<string, any>) {
  return request.get<PageResult<any>>('/admin/news/page', { params });
}

export function saveNews(data: Record<string, any>) {
  return request.post<void>('/admin/news', data);
}

export function fetchNotices(params: Record<string, any>) {
  return request.get<PageResult<any>>('/admin/notices/page', { params });
}

export function saveNotice(data: Record<string, any>) {
  return request.post<void>('/admin/notices', data);
}

export function fetchScenic(params: Record<string, any>) {
  return request.get<PageResult<any>>('/admin/scenic/page', { params });
}

export function fetchVenues(params: Record<string, any>) {
  return request.get<PageResult<any>>('/admin/venue/page', { params });
}

export function fetchActivities(params: Record<string, any>) {
  return request.get<PageResult<any>>('/admin/activity/page', { params });
}

export function reviewActivity(id: string, status: string) {
  return request.put<void>(`/admin/activity/${id}/audit`, { status });
}

export function uploadFile(data: FormData) {
  return request.post<{ url: string }>('/admin/file/upload', data, {
    headers: { 'Content-Type': 'multipart/form-data' }
  });
}

export function fetchMonitorData() {
  return request.get<{ visits: string; redis: string; cpu: string }>('/admin/monitor');
}
