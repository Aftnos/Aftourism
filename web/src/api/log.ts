import request from './request';

interface PageResult<T> {
  records: T[];
  total: number;
}

export function fetchOperationLogs(params: Record<string, any>) {
  return request.get<PageResult<any>>('/admin/oplogs/page', { params });
}
