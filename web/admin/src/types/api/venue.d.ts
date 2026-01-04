declare namespace Api {
  namespace Venue {
    interface VenueDTO {
      name: string
      amapId?: string
      tags?: string
      imageUrl?: string
      imageUrls?: string
      category?: string
      isFree: 0 | 1
      ticketPrice?: number
      address?: string
      province?: string
      city?: string
      district?: string
      openTime?: string
      description?: string
      phone?: string
      website?: string
      longitude?: number
      latitude?: number
      sort?: number
    }

    interface VenueVO extends VenueDTO {
      id: number
      createTime?: string
      updateTime?: string
    }

    type VenueSearchParams = Partial<
      { name: string; category: string; address: string; isFree: number; tags: string; city: string } & Api.Common.CommonSearchParams
    >
  }
}
