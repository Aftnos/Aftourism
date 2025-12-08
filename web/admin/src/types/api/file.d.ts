declare namespace Api {
  namespace File {
    interface UploadResponseData {
      url: string
      fileName: string
      originalName: string
      size: number
    }

    type UploadResponse = Api.Common.PaginatedResponse<never> & { // align with BaseResponse usage
      // not used; actual response is BaseResponse<UploadResponseData>
    }

    interface UploadParams {
      file: File
      bizTag?: string
    }
  }
}

