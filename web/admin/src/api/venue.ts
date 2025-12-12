import request from '@/utils/http'

export function fetchGetVenuePage(params: Api.Venue.VenueSearchParams) {
  return request.get<{ total: number; list: Api.Venue.VenueVO[]; pageNum?: number; pageSize?: number }>({
    url: '/api/admin/venue/page',
    params
  })
}

export function getVenueDetail(id: number) {
  return request.get<Api.Venue.VenueVO>({
    url: `/api/admin/venue/${id}`
  })
}

export function createVenue(data: Partial<Api.Venue.VenueDTO>) {
  return request.post<number>({
    url: '/api/admin/venue',
    data,
    showSuccessMessage: true
  })
}

export function updateVenue(id: number, data: Partial<Api.Venue.VenueDTO>) {
  return request.put<void>({
    url: `/api/admin/venue/${id}`,
    data,
    showSuccessMessage: true
  })
}

export function deleteVenue(id: number) {
  return request.del<void>({
    url: `/api/admin/venue/${id}`,
    showSuccessMessage: true
  })
}

