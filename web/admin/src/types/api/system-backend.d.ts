declare namespace Api {
  namespace SystemBackend {
    interface WatermarkSetting {
      visible: boolean
      content: string
    }

    interface WatermarkUpdateParams {
      visible?: boolean
      content?: string
    }

    type OperatorType = 'ADMIN' | 'SUPER_ADMIN' | 'PORTAL_USER'

    interface OperationLogItem {
      id: number
      operatorId?: number
      operatorType?: OperatorType
      moduleName?: string
      operationName?: string
      requestUri?: string
      requestMethod?: string
      successFlag?: boolean
      errorMsg?: string
      costMs?: number
      ipAddress?: string
      createTime?: string
    }

    type OperationLogList = Api.Common.PaginatedResponse<OperationLogItem>

    type OperationLogSearchParams = Partial<
      {
        operatorType: OperatorType
        moduleName: string
        operationName: string
        successFlag: boolean
        startTime: string
        endTime: string
      } & Api.Common.CommonSearchParams
    >
  }
}
