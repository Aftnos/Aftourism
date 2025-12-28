import request from '@/utils/http'

/**
 * 获取全局水印设置
 */
export function fetchWatermarkSetting() {
  return request.get<Api.SystemBackend.WatermarkSetting>({
    url: '/api/admin/system/backend/watermark'
  })
}

/**
 * 更新全局水印设置
 */
export function updateWatermarkSetting(data: Api.SystemBackend.WatermarkUpdateParams) {
  return request.put<void>({
    url: '/api/admin/system/backend/watermark',
    data,
    showSuccessMessage: true
  })
}

/**
 * 分页查询操作日志
 */
export function fetchOperationLogs(params: Api.SystemBackend.OperationLogSearchParams) {
  return request.get<Api.SystemBackend.OperationLogList>({
    url: '/api/admin/system/backend/operation-logs',
    params
  })
}
