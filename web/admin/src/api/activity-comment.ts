import request from '@/utils/http'

export function fetchGetAllCommentsPage(params: Partial<Api.Activity.ActivityCommentSearchParams>) {
  return request.get<{ total: number; list: Api.Activity.ActivityCommentVO[]; pageNum?: number; pageSize?: number }>({
    url: '/api/admin/activity/manage/comment/page',
    params
  })
}

export function fetchGetCommentsByActivity(id: number, params: Partial<Api.Activity.ActivityCommentSearchParams>) {
  return request.get<{ total: number; list: Api.Activity.ActivityCommentVO[]; pageNum?: number; pageSize?: number }>({
    url: `/api/admin/activity/manage/${id}/comment/page`,
    params
  })
}

export function createComment(activityId: number, data: Partial<Api.Activity.ActivityCommentManageDTO>) {
  return request.post<number>({
    url: `/api/admin/activity/manage/${activityId}/comment`,
    data,
    showSuccessMessage: true
  })
}

export function getCommentDetail(commentId: number) {
  return request.get<Api.Activity.ActivityCommentDetailVO>({
    url: `/api/admin/activity/manage/comment/${commentId}`
  })
}

export function updateComment(commentId: number, data: Partial<Api.Activity.ActivityCommentManageDTO>) {
  return request.put<void>({
    url: `/api/admin/activity/manage/comment/${commentId}`,
    data,
    showSuccessMessage: true
  })
}

export function deleteComment(commentId: number) {
  return request.del<void>({
    url: `/api/admin/activity/manage/comment/${commentId}`,
    showSuccessMessage: true
  })
}

export function fetchCommentChildren(commentId: number) {
  return request.get<Api.Activity.ActivityCommentVO[]>({
    url: `/api/admin/activity/manage/comment/${commentId}/children`
  })
}

