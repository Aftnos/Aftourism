declare namespace Api {
  namespace Scenic {
    interface ScenicSpotDTO {
      name: string
      amapId?: string
      tags?: string
      imageUrl?: string
      imageUrls?: string
      level?: string
      ticketPrice?: number
      address?: string
      province?: string
      city?: string
      district?: string
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
      { name: string; address: string; level: string; tags: string; city: string } & Api.Common.CommonSearchParams
    >
  }
}
