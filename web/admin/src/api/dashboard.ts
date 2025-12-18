import request from '@/utils/http'

export interface PortalOverview {
  totalPv: number
  totalUv: number
  todayPv: number
  todayUv: number
  onlineVisitors: number
  contentClicks: number
  newUsersToday: number
  totalUsers: number
}

export interface VisitTrendItem {
  statDate: string
  pvCount: number
  uvCount: number
}

export interface NewUserItem {
  id: number
  username: string
  nickname?: string
  gender?: string
  phone?: string
  email?: string
  avatar?: string
  createTime?: string
}

export interface NewUserTrendItem {
  statDate: string
  total: number
}

export interface NewUserStats {
  newUsersToday: number
  newUsersThisWeek: number
  latestUsers: NewUserItem[]
  weeklyTrend: NewUserTrendItem[]
}

export interface ContentBrief {
  id: number
  title: string
  publishTime?: string
  viewCount?: number
}

export interface ContentDigest {
  newsList: ContentBrief[]
  noticeList: ContentBrief[]
}

export function fetchPortalOverview() {
  return request.get<PortalOverview>({
    url: '/api/admin/dashboard/portal/overview'
  })
}

export function fetchPortalVisitTrend(days = 7) {
  return request.get<VisitTrendItem[]>({
    url: '/api/admin/dashboard/portal/visit-trend',
    params: { days }
  })
}

export function fetchPortalMonthlyTrend() {
  return request.get<VisitTrendItem[]>({
    url: '/api/admin/dashboard/portal/monthly-trend'
  })
}

export function fetchPortalNewUsers() {
  return request.get<NewUserStats>({
    url: '/api/admin/dashboard/portal/new-users'
  })
}

export function fetchPortalContentDigest(limit = 6) {
  return request.get<ContentDigest>({
    url: '/api/admin/dashboard/portal/content-digest',
    params: { limit }
  })
}
