import request from '@/utils/http'

export function fetchFeedbackPage(params: Api.Feedback.FeedbackSearchParams) {
  return request.get<Api.Feedback.FeedbackPage>({
    url: '/api/admin/feedback/manage/page',
    params
  })
}

export function fetchFeedbackDetail(id: number) {
  return request.get<Api.Feedback.FeedbackItem>({
    url: `/api/admin/feedback/manage/${id}`
  })
}

export function updateFeedback(id: number, data: Api.Feedback.FeedbackUpdateRequest) {
  return request.put<void>({
    url: `/api/admin/feedback/manage/${id}`,
    data,
    showSuccessMessage: true
  })
}

export function deleteFeedback(id: number) {
  return request.del<void>({
    url: `/api/admin/feedback/manage/${id}`,
    showSuccessMessage: true
  })
}
