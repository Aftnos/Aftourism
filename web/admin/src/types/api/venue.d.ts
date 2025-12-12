declare namespace Api {
  namespace Venue {
    interface VenueDTO {
      name: string
      imageUrl?: string
      category?: string
      isFree: 0 | 1
      ticketPrice?: number
      address?: string
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
      { name: string; category: string; address: string; isFree: number } & Api.Common.CommonSearchParams
    >
  }
}

