import request from '@/utils/http'

export function fetchGetActivityPage(params: Api.Activity.ActivitySearchParams) {
  return request.get<{ total: number; list: Api.Activity.ActivityManageVO[]; pageNum?: number; pageSize?: number }>({
    url: '/api/admin/activity/manage/page',
    params
  })
}

export function getActivityDetail(id: number) {
  return request.get<Api.Activity.ActivityManageVO>({
    url: `/api/admin/activity/manage/${id}`
  })
}

export function createActivity(data: Partial<Api.Activity.ActivityManageDTO>) {
  return request.post<number>({
    url: '/api/admin/activity/manage',
    data,
    showSuccessMessage: true
  })
}

export function updateActivity(id: number, data: Partial<Api.Activity.ActivityManageDTO>) {
  return request.put<void>({
    url: `/api/admin/activity/manage/${id}`,
    data,
    showSuccessMessage: true
  })
}

export function deleteActivity(id: number) {
  return request.del<void>({
    url: `/api/admin/activity/manage/${id}`,
    showSuccessMessage: true
  })
}
