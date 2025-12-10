declare namespace Api {
  namespace Scenic {
    interface ScenicSpotDTO {
      name: string
      imageUrl?: string
      level?: string
      ticketPrice?: number
      address?: string
      openTime?: string
      intro?: string
      phone?: string
      website?: string
      longitude?: number
      latitude?: number
      sort?: number
    }

    interface ScenicSpotVO extends ScenicSpotDTO {
      id: number
      createTime?: string
      updateTime?: string
    }

    type ScenicSearchParams = Partial<
      { name: string; address: string } & Api.Common.CommonSearchParams
    >
  }
}

