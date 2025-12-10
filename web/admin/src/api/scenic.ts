import request from '@/utils/http'

export function fetchGetScenicPage(params: Api.Scenic.ScenicSearchParams) {
  return request.get<{ total: number; list: Api.Scenic.ScenicSpotVO[]; pageNum?: number; pageSize?: number }>({
    url: '/api/admin/scenic/page',
    params
  })
}

export function getScenicDetail(id: number) {
  return request.get<Api.Scenic.ScenicSpotVO>({
    url: `/api/admin/scenic/${id}`
  })
}

export function createScenic(data: Partial<Api.Scenic.ScenicSpotDTO>) {
  return request.post<number>({
    url: '/api/admin/scenic',
    data,
    showSuccessMessage: true
  })
}

export function updateScenic(id: number, data: Partial<Api.Scenic.ScenicSpotDTO>) {
  return request.put<void>({
    url: `/api/admin/scenic/${id}`,
    data,
    showSuccessMessage: true
  })
}

export function deleteScenic(id: number) {
  return request.del<void>({
    url: `/api/admin/scenic/${id}`,
    showSuccessMessage: true
  })
}

