import request from '@/utils/http'

export async function uploadFile(params: { file: File; bizTag?: string }) {
  const form = new FormData()
  form.append('file', params.file)
  if (params.bizTag) form.append('bizTag', params.bizTag)
  return request.post<Api.File.UploadResponseData>({
    url: '/api/file/upload',
    data: form,
    showSuccessMessage: true
  })
}

